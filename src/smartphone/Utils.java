package smartphone;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utils
{
	public static Image resizeImage( Image img, int newWidth, int newHeight )
	{
		// Largeur et hauteur de l'image avant modification
		int w = img.getWidth( null );
		int h = img.getHeight( null );
		
		// Ratio largeur/hauteur de l'image
		float imgRatio = ((float) w) / h;
		
		// Facteur de redimensionnement
		float resizeFactor = 1.0f;
		
		// Coordonn�es auxquelles commencer le rognage
		int cropStartX = 0;
		int cropStartY = 0;
		
		// Vrai si r�duire l'image � la hauteur de la vignette ne laisse pas de vide
		if ( newWidth * (1.f/imgRatio) >= newHeight )
		{
			resizeFactor = ((float)newWidth) / w;
			cropStartY = Math.round((resizeFactor*h - newHeight) / 2);
		}
		
		// Vrai si r�duire l'image � la largeur de la vignette ne laisse pas de vide
		else if ( newHeight * imgRatio >= newHeight )
		{
			resizeFactor = ((float)newHeight) / h;
			cropStartX = Math.round((resizeFactor*w - newWidth) / 2);
		}
		
		// Image redimensionn�e
		Image scaled = img.getScaledInstance( (int)(resizeFactor*w), (int)(resizeFactor*h), Image.SCALE_FAST );
		
		// On rogne les parties de l'image qui d�passe la dimension de la vignette
		ImageFilter cropFilter = new CropImageFilter( cropStartX, cropStartY, newWidth, newHeight );
		FilteredImageSource fis = new FilteredImageSource( scaled.getSource(), cropFilter );
		
		// Retour de l'image rogn�e et redimensionn�e
		return Toolkit.getDefaultToolkit().createImage( fis );
	}
	
	public static Image applyImageFilter( Image img, ImageFilter filter )
	{
		FilteredImageSource fis = new FilteredImageSource( img.getSource(), filter );
		
		// Retour de l'image rogn�e et redimensionn�e
		return Toolkit.getDefaultToolkit().createImage( fis );
	}
}
