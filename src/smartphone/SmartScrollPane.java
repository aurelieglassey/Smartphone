package smartphone;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
/**
 * Classe qui met en forme un ScrollPane pour la liste de contact et la liste de musique
 * @author Fabien
 *
 */
public class SmartScrollPane extends JScrollPane
{
	/**
	 * Constructeur de la classe SmartScrollPane sans paramètres
	 */
	public SmartScrollPane()
	{
		super();
		customizeScrollPane();
	}

	/**
	 * Constructeur de la classe SmartScrollPane avec un paramètre view
	 * @param view
	 */
	public SmartScrollPane( Component view )
	{
		super( view );
		customizeScrollPane();
	}
	
	/**
	 * Méthode qui met en forme le scrollPane.
	 * Adapte la largeur, enlève les bords et met en forme la couleur de fond d'un ScrollPane
	 */
	public void customizeScrollPane()
	{
		getVerticalScrollBar().setPreferredSize(
			new Dimension( Smartphone.getScrollbarThickness(), 0 )
		);
		
		getHorizontalScrollBar().setPreferredSize(
			new Dimension( 0, Smartphone.getScrollbarThickness() )
		);
		
		setBackground( Smartphone.getBackgroundColor() );
		setBorder( null );
	}
}
