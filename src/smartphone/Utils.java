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
	public static ImageIcon resizeIcon( ImageIcon icon, int newWidth, int newHeight )
	{
		Image img = icon.getImage();
		img = Utils.resizeImage( img, newWidth, newHeight );
		return new ImageIcon( img );
	}
	
	public static Image resizeImage( Image img, int newWidth, int newHeight )
	{
		// Largeur et hauteur de l'image avant modification
		int w = img.getWidth( null );
		int h = img.getHeight( null );
		
		// Ratio largeur/hauteur de l'image
		float imgRatio = ((float) w) / h;
		
		// Facteur de redimensionnement
		float resizeFactor = 1.0f;
		
		// Coordonnées auxquelles commencer le rognage
		int cropStartX = 0;
		int cropStartY = 0;
		
		// Vrai si réduire l'image à la hauteur de la vignette ne laisse pas de vide
		if ( newWidth * (1.f/imgRatio) >= newHeight )
		{
			resizeFactor = ((float)newWidth) / w;
			cropStartY = Math.round((resizeFactor*h - newHeight) / 2);
		}
		
		// Vrai si réduire l'image à la largeur de la vignette ne laisse pas de vide
		else if ( newHeight * imgRatio >= newHeight )
		{
			resizeFactor = ((float)newHeight) / h;
			cropStartX = Math.round((resizeFactor*w - newWidth) / 2);
		}
		
		// Image redimensionnée
		Image scaled = img.getScaledInstance( (int)(resizeFactor*w), (int)(resizeFactor*h), Image.SCALE_FAST );
		
		// On rogne les parties de l'image qui dépasse la dimension de la vignette
		ImageFilter cropFilter = new CropImageFilter( cropStartX, cropStartY, newWidth, newHeight );
		FilteredImageSource fis = new FilteredImageSource( scaled.getSource(), cropFilter );
		
		// Retour de l'image rognée et redimensionnée
		return Toolkit.getDefaultToolkit().createImage( fis );
	}

	public static ImageIcon applyImageFilter( ImageIcon icon, ImageFilter filter )
	{
		Image img = icon.getImage();
		img = Utils.applyImageFilter( img, filter );
		return new ImageIcon( img );
	}
	
	public static Image applyImageFilter( Image img, ImageFilter filter )
	{
		FilteredImageSource fis = new FilteredImageSource( img.getSource(), filter );
		
		// Retour de l'image rognée et redimensionnée
		return Toolkit.getDefaultToolkit().createImage( fis );
	}
	
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
