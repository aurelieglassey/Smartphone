package smartphone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
/**
 * Classe qui met en forme l'apparence d'un textfield dans l'application contact
 * @author Aurélie
 *
 */
public class SmartTextField extends JTextField
{

	/**
	 * Constructeur de la classe SmartTextField
	 */
	public SmartTextField()
	{
		super();
		setBackground(new Color(250, 250, 250));
		//setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(162, 28, 60), new Color(245, 245, 245)));
		setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(192, 0, 0)));
		setFont(new Font ("Raleway", Font.PLAIN, 24));
		setForeground(Color.GRAY);
	}

}
