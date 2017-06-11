package smartphone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class SmartButton extends JButton
{

	/**
	 * Premier constructeur avec du text en param�tre.
	 * @param text
	 */
	public SmartButton (String text)
	{
		super(text);
		apparenceButton();
	}
	
	/**
	 * Deuxi�me constructeur avec un image en param�tre 
	 * @param icon
	 */
	public SmartButton (Icon icon)
	{
		super( icon );
		apparenceButton();
	}
	
	private void apparenceButton()
	{
		setBackground( Smartphone.getBackgroundColor().brighter().brighter() );
		setForeground( Color.WHITE );
		setFont( Smartphone.getSmartFont("smart") );
		
		Border empty = BorderFactory.createEmptyBorder( 5, 15, 5, 15);
		setBorder( empty );
		setFocusPainted( false );
	}
	
}
