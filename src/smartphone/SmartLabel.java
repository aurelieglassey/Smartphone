package smartphone;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * Classe qui met en forme l'apparence d'un label 
 * @author Aurélie
 *
 */
public class SmartLabel extends JLabel
{

	/**
	 * Constructeur de la classe SmartLabel
	 * Mise en forme des bords, de la couleur et de l'écriture d'un label
	 * @param text text des labels
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
