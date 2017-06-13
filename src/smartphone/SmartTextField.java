package smartphone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
/**
 * Classe qui met en forme l'apparence d'un textfield 
 * @author Aurélie Glassey
 */
public class SmartTextField extends JTextField
{
	/**
	 * Constructeur de la classe SmartTextField
	 * Mise en forme des bords, de la couleur de fond et de l'écriture d'un JTextField
	 */
	public SmartTextField()
	{
		super();
		setBackground(new Color(250, 250, 250));
		setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(192, 0, 0)));
		setFont(new Font ("Raleway", Font.PLAIN, 24));
		setForeground(Color.GRAY);
	}

}
