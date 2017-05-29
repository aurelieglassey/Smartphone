package smartphone;

import java.awt.Color;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class ImageButton extends JButton
{
	private Image image;
	private int imageWidth;
	private int imageHeight;
	

	public ImageButton( BufferedImage image )
	{
		this( image, 50, 50 );
	}
	
	public ImageButton( BufferedImage image, int width, int height )
	{
		this.image = image;
		this.imageWidth = width;
		this.imageHeight = height;
		
		this.setIcon( new ImageIcon( Utils.resizeImage(image, width, height) ) );
		
		this.setBorder( BorderFactory.createLineBorder(Color.WHITE, 1) );
	}
}
