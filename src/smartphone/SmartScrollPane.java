package smartphone;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
/**
 * Classe qui met en forme un ScrollPane pour l'adapter au smartphone.
 * Retire ses bords, applique la couleur de fond par défaut et élargit
 * les scrollbars.
 * @author Fabien Terrani
 */
public class SmartScrollPane extends JScrollPane
{
	/**
	 * Crée un nouveau SmartScrollPane.
	 */
	public SmartScrollPane()
	{
		super();
		customizeScrollPane();
	}

	/**
	 * Crée un nouveau SmartScrollPane affichant le composant passé en paramètre.
	 * @param view Le composant à parcourir par le scrollpane
	 */
	public SmartScrollPane( Component view )
	{
		super( view );
		customizeScrollPane();
	}
	
	/**
	 * Met en forme le scrollPane : adapte l'épaisseur des scrollbars, enlève les bords
	 * et applique la couleur de fond par défaut.
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
