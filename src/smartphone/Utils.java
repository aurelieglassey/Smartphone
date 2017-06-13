package smartphone;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Classe regroupant divers utilitaires pour la gestion d'images et la sérialization.
 * @author Fabien Terrani
 */
public class Utils
{
	/**
	 * Redimensionne l'icône fournie pour qu'elle recouvre la zone définie.
	 * @param icon L'icône à redimensionner
	 * @param width La largeur de la zone à couvrir en pixels
	 * @param height La hauteur de la zone à couvrir en pixels
	 * @return Une nouvelle icône ayant les dimensions fournies et recouverte par l'icône passée en paramètre
	 */
	public static ImageIcon resizeIcon( ImageIcon icon, int width, int height )
	{
		Image img = icon.getImage();
		img = Utils.resizeImage( img, width, height );
		return new ImageIcon( img );
	}
	
	/**
	 * Redimensionne l'image fournie pour qu'elle recouvre la zone définie.
	 * @param img L'image à redimensionner
	 * @param width La largeur de la zone à couvrir en pixels
	 * @param height La hauteur de la zone à couvrir en pixels
	 * @return Une nouvelle image ayant les dimensions fournies et recouverte par l'image passée en paramètre
	 */
	public static Image resizeImage( Image img, int width, int height )
	{
		// Largeur et hauteur de l'image avant modification
		int imgWidth = img.getWidth( null );
		int imgHeight = img.getHeight( null );
		
		// Ratio largeur/hauteur de l'image
		float imgRatio = ((float) imgWidth) / imgHeight;
		
		// Facteur de redimensionnement
		float resizeFactor = 1.0f;
		
		// Coordonnées auxquelles commencer le rognage
		int cropStartX = 0;
		int cropStartY = 0;
		
		// Vrai si réduire l'image à la hauteur de la vignette ne laisse pas de vide
		if ( width * (1.f/imgRatio) >= height )
		{
			resizeFactor = ((float)width) / imgWidth;
			cropStartY = Math.round((resizeFactor*imgHeight - height) / 2);
		}
		
		// Vrai si réduire l'image à la largeur de la vignette ne laisse pas de vide
		else if ( height * imgRatio >= height )
		{
			resizeFactor = ((float)height) / imgHeight;
			cropStartX = Math.round((resizeFactor*imgWidth - width) / 2);
		}
		
		// Image redimensionnée
		Image scaled = img.getScaledInstance( (int)(resizeFactor*imgWidth), (int)(resizeFactor*imgHeight), Image.SCALE_FAST );
		
		// On rogne les parties de l'image qui dépasse la dimension de la vignette
		ImageFilter cropFilter = new CropImageFilter( cropStartX, cropStartY, width, height );
		FilteredImageSource fis = new FilteredImageSource( scaled.getSource(), cropFilter );
		
		// Retour de l'image rognée et redimensionnée
		return Toolkit.getDefaultToolkit().createImage( fis );
	}
	
	/**
	 * Crée une copie de l'icône passée en paramètre, applique le filtre fourni et retourne la copie.
	 * @param icon L'icône sur laquelle appliquer le filtre
	 * @param filter Le filtre à appliquer
	 * @return Une copie de l'icône après application du filtre
	 */
	public static ImageIcon applyImageFilter( ImageIcon icon, ImageFilter filter )
	{
		Image img = icon.getImage();
		img = Utils.applyImageFilter( img, filter );
		return new ImageIcon( img );
	}
	
	/**
	 * Crée une copie de l'image passée en paramètre, applique le filtre fourni et retourne la copie.
	 * @param img L'image sur laquelle appliquer le filtre
	 * @param filter Le filtre à appliquer
	 * @return Une copie de l'image après application du filtre
	 */
	public static Image applyImageFilter( Image img, ImageFilter filter )
	{
		FilteredImageSource fis = new FilteredImageSource( img.getSource(), filter );
		
		// Retour de l'image rognée et redimensionnée
		return Toolkit.getDefaultToolkit().createImage( fis );
	}
	
	/**
	 * Sérialise les objets passés en paramètre dans le fichier f.
	 * @param f Le fichier dans lequel stocker les objets sérialisés
	 * @param objects Les objets à sérialiser
	 */
	public static void serializeObjects(File f, Object... objects )
	{
		try
		{
			System.out.println("Méthode serializeObjects");
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			for (Object o : objects)
			{
				oos.writeObject(o);
			}
			
			oos.close(); 
		} 
		
		catch (Exception e) //si fichier inaccessible, on lève l'exception
		{
			System.err.println( "Problème lors de la serialization" );
		}
	}
	
	/**
	 * Déserialise les objets contenus dans le fichier f et les retourne.
	 * @param f Le fichier contenant les objets à déserialiser
	 * @return Un tableau contenant les objets déserialisés
	 */
	public static Object[] deserializeObject(File f)
	{
		ArrayList <Object> arraylist= new ArrayList<>();
		try
		{
			System.out.println("Méthode deserializeObject");
			FileInputStream fis = new FileInputStream( f );
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			while (true)
			{
				arraylist.add(ois.readObject());
			}
			
		} 
		
		catch (Exception e) 
		{}
		
		return arraylist.toArray();
	}
}
