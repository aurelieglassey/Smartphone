package smartphone;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

/**
 * Renderer utilisé pour adapter l'apparence des items de liste au smartphone.
 * @author Fabien Terrani
 */
public class SmartListCellRenderer extends DefaultListCellRenderer
{
	/**
	 * Crée un nouveau cell renderer.
	 */
	public SmartListCellRenderer()
	{
		super();
	}
	
	/**
	 * Retourne le composant utilisé pour afficher la valeur de la liste.
	 */
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		JComponent c = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		//Border originalBorder = c.getBorder();
		Border emptyBorder = BorderFactory.createEmptyBorder(10,10,10,10);
		//CompoundBorder compound = new CompoundBorder( originalBorder, emptyBorder );
		//c.setBorder( compound );
		c.setBorder( emptyBorder );
		
		if (isSelected)
		{
			c.setForeground( Color.WHITE );
			c.setBackground( new Color(0, 150, 255, 255) );
		}
		
		return c;
	}
	
	/**
	 * Ajoute un dégradé gris-clair et blanc en fond des items de liste.
	 */
	public void paintComponent( Graphics g )
	{
		if( g instanceof Graphics2D )
		{
			Graphics2D g2d = (Graphics2D) g;
			
			LinearGradientPaint gradient = new LinearGradientPaint(
				0.0f, 0.0f, 0.0f, this.getHeight(),
				new float[] {0.0f, 0.5f, 1.0f},
				new Color[] {new Color(250, 250, 250), new Color(255, 255, 255), new Color(245, 245, 245) }
			);
			
			g2d.setPaint( gradient );
			g2d.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		}
		
		super.paintComponent(g);
	}
}