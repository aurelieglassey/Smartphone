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
		setBackground(new Color(30, 30, 30, 255));
		setForeground(Color.WHITE);
		
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY));
		setFont(new Font ("Raleway", Font.PLAIN, 24));
	}
	
}
