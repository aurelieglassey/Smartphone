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
}
