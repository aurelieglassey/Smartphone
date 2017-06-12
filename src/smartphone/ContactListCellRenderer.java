package smartphone;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ContactListCellRenderer extends SmartListCellRenderer
{
	private int iconSize = 60;

	public ContactListCellRenderer( int size )
	{
		this();
		iconSize = size;
	}
	public ContactListCellRenderer()
	{
		setIconTextGap( 10 );
	}
	
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus)
	{
		// Ce ListCellRenderer est basé sur DefaultListCellRenderer qui hérite de JLabel.
		// DefaultListCellRenderer se renvoie lui-même pour dessiner l'item de liste.
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		if ( value instanceof Contact )
		{
			Contact c = (Contact) value;
			label.setText( c.getFirstname() + " " + c.getName() );
			
			Image contactImage;
			
			try
			{
				contactImage = ImageIO.read( new File(".") );
				
				// Redimensionnement de l'image du contact en 60x60
				contactImage = Utils.resizeImage( contactImage, iconSize, iconSize );
			}
			
			catch( Exception ex)
			{
				BufferedImage bimg = new BufferedImage( iconSize, iconSize, BufferedImage.TYPE_INT_ARGB );
				Graphics g = bimg.getGraphics();
				g.setColor( new Color(240, 240, 240, 255) );
				g.fillRect(0,  0,  bimg.getWidth(),  bimg.getHeight() );
				
				contactImage = bimg;
			}
			
			Point center = new Point( iconSize/2, iconSize/2 );
			
			ImageFilter filter = new RGBImageFilter()
			{
				public int filterRGB(int x, int y, int rgb)
				{
					
					double dist = center.distance(x, y);
					
					if (dist > iconSize/2)
					{
						rgb &= 0x00ffffff;
					}
					
					return rgb;
				}
			};
			
			// Arrondissement de l'image
			contactImage = Utils.applyImageFilter(contactImage, filter);
			
			label.setIcon( new ImageIcon(contactImage) );
		}
		
		return label;
	}
}
