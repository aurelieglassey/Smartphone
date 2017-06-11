package smartphone;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

public class ContactListCellRenderer extends SmartListCellRenderer
{
	public ContactListCellRenderer()
	{
		setIconTextGap( 10 );
	}
	
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus)
	{
		JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		if ( value instanceof Contact )
		{
			Contact c = (Contact) value;
			label.setText( c.getFirstname() + " " + c.getName() );
			//label.setIcon(new ImageIcon( c.getImageFile() ) );
			
			ImageIcon test = new ImageIcon("C:\\Users\\Fabien\\Documents\\GitHub\\Smartphone\\smartphone_root\\storage\\images\\Dock.jpg");
			
			// Redimensionnement de l'image du contact en 60x60
			test = Utils.resizeIcon(test, 60, 60);
			
			
			Point center = new Point(30, 30);
			ImageFilter filter = new RGBImageFilter() {
				
				public int filterRGB(int x, int y, int rgb)
				{
					
					double dist = center.distance(x, y);
					
					if (dist > 30)
					{
						rgb &= 0x00ffffff;
					}
					
					return rgb;
				}
			};
			
			// Arrondissement de l'image
			test = Utils.applyImageFilter(test, filter);
			
			label.setIcon( test );
		}
		
		return label;
	}
}
