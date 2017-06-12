package smartphone;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * Classe qui met en forme l'apparence d'un label dans l'application contact
 * @author Aurélie Glassey
 */
public class SmartLabel extends JLabel
{

	/**
	 * Crée un nouveau label.
	 * @param text Le texte affiché par le label
	 */
	public SmartLabel(String text)
	{
		super(text);
		setBorder(BorderFactory.createEmptyBorder(10, 10 , 10, 10));
		setFont( Smartphone.getSmartFont("medium") );
		setBackground(new Color(250, 250, 250));
		setOpaque(true);
	}
}
