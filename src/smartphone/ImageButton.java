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

/**
 * Cette classe paramètre des boutons avec un fond transparent prévus
 * pour afficher uniquement une image. Cette dernière est redimensionnée
 * sans déformation pour remplir les dimensions spécifiées pour le bouton (ce
 * qui implique que certaines zones de l'image ne seront pas visible si le ratio
 * de l'image et de la zone diffèrent ; il n'y a par contre aucun endroit non couvert
 * par l'image).
 * 
 * Lorsqu'on survole ou clique sur le bouton, l'image se colore légèrement pour
 * fournir un retour à l'utilisateur.
 * @author Fabien Terrani
 */
public class ImageButton extends JButton
{
	/**
	 * Crée un nouveau bouton image. Insère l'image fournie dans une zone de 50x50 pixels.
	 * @param image L'image à afficher dans le bouton
	 */
	public ImageButton( Image image )
	{
		this( image, 50, 50 );
	}
	
	/**
	 * Crée un nouveau bouton image. Insère l'image fournie dans une zone ayant les dimensions
	 * passées en paramètre.
	 * @param image L'image à afficher dans le bouton
	 * @param width La largeur de la zone d'affichage de l'image en pixels
	 * @param height La hauteur de la zone d'affichage de l'image en pixels
	 */
	public ImageButton( Image image, int width, int height )
	{
		this.setBackground( new Color(0,0,0,0) );
		this.setContentAreaFilled( false ); // PLus approprié que setOpaque() pour les fonds transparents
		this.setBorder( null );
		
		this.setImage( image, width, height );
	}
	
	/**
	 * Change l'image et les dimensions du bouton.
	 * @param image La nouvelle image à afficher
	 * @param width La nouvelle largeur de la zone d'affichage en pixels
	 * @param height La nouvelle hauteur de la zone d'affichage en pixels
	 */
	public void setImage( Image image, int width, int height )
	{
		Image imgIcon = Utils.resizeImage(image, width, height);
		Image imgRolloverIcon = Utils.applyImageFilter( imgIcon, new YellowFilter() );
		Image imgPressedIcon = Utils.applyImageFilter( imgIcon, new WhiteFilter() );
		
		this.setIcon( new ImageIcon( imgIcon ));
		this.setRolloverIcon( new ImageIcon( imgRolloverIcon ));
		this.setPressedIcon( new ImageIcon( imgPressedIcon ));
	}
	
	/**
	 * Filtre recouvrant l'image d'un blanc transparent.
	 * @author Fabien Terrani
	 */
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
	
	/**
	 * Filtre recouvrant l'image d'un jaune transparent.
	 * @author Fabien Terrani
	 */
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
