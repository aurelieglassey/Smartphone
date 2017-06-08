package smartphone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton
{
	private Image image;
	private Image imgIcon;
	private Image imgRolloverIcon;
	private Image imgPressedIcon;
	private int imageWidth;
	private int imageHeight;
	

	public ImageButton( Image image )
	{
		this( image, 50, 50 );
	}
	
	public ImageButton( Image image, int width, int height )
	{
		// Stockage des données de l'image
		this.image = image;
		this.imageWidth = width;
		this.imageHeight = height;
		
		
		this.imgIcon = Utils.resizeImage(image, width, height);
		this.imgRolloverIcon = Utils.applyImageFilter( this.imgIcon, new YellowFilter() );
		this.imgPressedIcon = Utils.applyImageFilter( this.imgIcon, new WhiteFilter() );
		
		this.setIcon( new ImageIcon( this.imgIcon ));
		this.setRolloverIcon( new ImageIcon( imgRolloverIcon ));
		this.setPressedIcon( new ImageIcon( imgPressedIcon ));
		
		this.setBackground( new Color(0,0,0,0) );
		
		// Préférer setContentAreaFilled() pour les boutons icônes !
		//this.setOpaque( false );
		this.setContentAreaFilled( false );
		
		this.setBorder( null );
	}

	
	
	public Image getImage()
	{
		return image;
	}

	private static class WhiteFilter extends RGBImageFilter
	{
        public WhiteFilter() {
            canFilterIndexColorModel = true;
        }

        public int filterRGB(int x, int y, int rgb)
        {
        	int a = ((rgb & 0xff000000) >> 24);
        	int r = ((rgb & 0xff0000) >> 16);
        	int g = ((rgb & 0xff00) >> 8);
        	int b = (rgb & 0xff);
        	
        	//a = Math.min(0x7f, a);
        	r += 0.5*(0xff-r);
        	g += 0.5*(0xff-g);
        	b += 0.5*(0xff-b);
        	
        	
        	return ((a<<24) | (r<<16) | (g<<8) | b);
        }
    }
	
	private static class YellowFilter extends RGBImageFilter
	{
        public YellowFilter() {
            canFilterIndexColorModel = true;
        }

        public int filterRGB(int x, int y, int rgb)
        {
        	int a = ((rgb & 0xff000000) >> 24);
        	int r = ((rgb & 0xff0000) >> 16);
        	int g = ((rgb & 0xff00) >> 8);
        	int b = (rgb & 0xff);
        	
        	//a = Math.min(0x7f, a);
        	r += 0.5*(0xff-r);
        	g += 0.5*(0xcc-g);
        	b += 0.5*(0x00-b);
        	
        	
        	return ((a<<24) | (r<<16) | (g<<8) | b);
        }
    }
}
