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
 * @author Aurélie Glassey
 */
public class SmartButton extends JButton
{

	/**
	 * Premier constructeur avec du text en paramètre.
	 * Appel de la méthode apparenceButton(); pour la mise en forme du bouton
	 * @param text Texte du bouton
	 */
	public SmartButton (String text)
	{
		super(text);
		apparenceButton();
	}
	
	/**
	 * Deuxième constructeur avec un image en paramètre 
	 * Appel de la méthode apparenceButton(); pour la mise en forme du bouton
	 * @param icon L'icône à afficher dans le bouton
	 */
	public SmartButton (Icon icon)
	{
		super( icon );
		apparenceButton();
	}
	
	/**
	 * Adapte l'apparence du bouton au smartphone. Que le paramètre d'entrée d'un bouton soit une Icon ou
   * un String, l'apparence du bouton est la même. Adapte la couleur de fond, les bords et l'écriture d'un bouton.
	 */
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
