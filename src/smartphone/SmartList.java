package smartphone;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

/**
 * 
 * @author Fabien
 *
 * @param <E>
 */
public class SmartList<E> extends JList<E>
{
	/**
	 * Constructeur de la classe SmartList sans paramètres
	 * Mise en forme avec la méthode customizeList();
	 */
	public SmartList()
	{
		super();
		customizeList();
	}

	/**
	 * Constructeur de la classe SmartList avec un tableau en paramètre
	 * Mise en forme du tableau avec la méthode customizeList();
	 * @param listData liste de données
	 */
	public SmartList( E[] listData )
	{
		super( listData );
		customizeList();
	}

	/**
	 * Constructeur de la classe SmartList avec une arraylist en paramètre
	 * Mise en forme de l'arraylist avec la méthode customizeList();
	 * @param dataModel liste de données
	 */
	public SmartList( ListModel<E> dataModel )
	{
		super( dataModel );
		customizeList();
	}
	
	/**
	 * 
	 * @param listData
	 */
	public SmartList( Vector<? extends E> listData )
	{
		super( listData );
		customizeList();
	}
	

	 /**
	  * Méthode qui met en forme une SmartList
	  * Mise en forme des bords et de la couleur de fond d'une liste/tableau
	  */
	private void customizeList()
	{
		setCellRenderer( new SmartListCellRenderer() );
		
		//this.getModel().addListDataListener(arg0);
		setFont( Smartphone.getSmartFont("medium") );
		setBackground( Color.WHITE );
	}
}
