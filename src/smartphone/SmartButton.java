package smartphone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
/**
 * Classe qui met en forme l'apparence d'un bouton dans l'application contact
 * Cette classe contient deux constructeurs, un avec un texte en param�tre et l'autre avec une image en param�tre
 * @author Aur�lie
 *
 */
public class SmartButton extends JButton
{

	/**
	 * Premier constructeur avec du text en param�tre.
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
	 * Deuxi�me constructeur avec un image en param�tre 
	 * @param icon
	 */
	public SmartButton (Icon icon)
	{
		super( icon );
		setBackground(new Color(30, 30, 30, 255));
		setForeground(Color.WHITE);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY));
		setFont(new Font ("Raleway", Font.PLAIN, 24));
	}
	
}
