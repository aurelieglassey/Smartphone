package smartphone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;

public class SmartButton extends JButton
{

	/**
	 * Premier constructeur avec du text en paramètre.
	 * @param text
	 */
	public SmartButton (String text)
	{
		super(text);
		apparenceButton();
	}
	
	/**
	 * Deuxième constructeur avec un image en paramètre 
	 * @param icon
	 */
	public SmartButton (Icon icon)
	{
		super( icon );
		apparenceButton();
	}
	
	private void apparenceButton()
	{
		setBackground(new Color(30, 30, 30, 255));
		setForeground(Color.WHITE);
		
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY));
		setFont(new Font ("Raleway", Font.PLAIN, 24));
	}
	
}
