package smartphone;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class SmartLabel extends JLabel
{

	/**
	 * Constructeur de la classe SmartLabel
	 * @param text
	 */
	public SmartLabel(String text)
	{
		super(text);
		setFont(new Font ("Raleway", Font.PLAIN, 24));
		setBackground(new Color(250, 250, 250));
//		setForeground(Color.WHITE);
		setOpaque(true);

	}
	
}
