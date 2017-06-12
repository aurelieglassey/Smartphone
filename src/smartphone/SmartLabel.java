package smartphone;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * Classe qui met en forme l'apparence d'un label dans l'application contact
 * @author Aurélie
 *
 */
public class SmartLabel extends JLabel
{

	/**
	 * Constructeur de la classe SmartLabel
	 * @param text
	 */
	public SmartLabel(String text)
	{
		super(text);
		setBorder(BorderFactory.createEmptyBorder(10, 10 , 10, 10));
		setFont(new Font ("Raleway", Font.PLAIN, 24));
		setBackground(new Color(250, 250, 250));
		setOpaque(true);

	}
	
}
