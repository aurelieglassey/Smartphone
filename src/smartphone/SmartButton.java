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

/**
 * Classe qui met en forme l'apparence d'un bouton dans l'application contact
 * Cette classe contient deux constructeurs, un avec un texte en paramètre et l'autre avec une image en paramètre
 * @author Aurélie
 *
 */
public class SmartButton extends JButton
{

	/**
	 * Premier constructeur avec du text en paramètre.
	 * @param text
	 */
	public SmartButton (String text)
	{
		super(text);
		setBackground(new Color(255, 225, 228, 255));
		setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(192, 0, 0)));
		setFont(new Font ("Raleway", Font.PLAIN, 24));
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
		setBackground( Smartphone.getBackgroundColor().brighter().brighter() );
		setForeground( Color.WHITE );
		setFont( Smartphone.getSmartFont("smart") );
		
		Border empty = BorderFactory.createEmptyBorder( 5, 15, 5, 15);
		setBorder( empty );
		setFocusPainted( false );
	}
}
