package smartphone;

import javax.imageio.ImageIO;

/**
 * Classe contenant la méthode main, utilisée comme point d'entrée du programme.
 * @author Fabien Terrani
 */
public class Main
{
	/**
	 * Point d'entrée du programme.
	 * @param args Arguments transmis par le système (non utilisé)
	 */
	public static void main( String[] args )
	{
		// Désactivation de la mise en cache des images (activée par défaut)
		ImageIO.setUseCache( false );
		
		Smartphone frame = new Smartphone();
		frame.setVisible( true );
	}
}
