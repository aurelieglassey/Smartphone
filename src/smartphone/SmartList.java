package smartphone;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

/**
 * Liste personnalisée pour avoir l'apparence voulue sur le smartphone.
 * 
 * @author Fabien Terrani
 *
 * @param <E> Le type d'objets contenus dans la liste
 */
public class SmartList<E> extends JList<E>
{
	/**
	 * Crée une nouvelle liste.
	 */
	public SmartList()
	{
		super();
		customizeList();
	}

	/**
	 * Crée une nouvelle liste.
	 * @param listData Tableau contenant les données de la liste
	 */
	public SmartList( E[] listData )
	{
		super( listData );
		customizeList();
	}

	/**
	 * Crée une nouvelle liste.
	 * @param dataModel Le modèle de liste à utiliser
	 */
	public SmartList( ListModel<E> dataModel )
	{
		super( dataModel );
		customizeList();
	}
	
	/**
	 * Crée une nouvelle liste.
	 * @param listData Vector contenant les données de la liste
	 */
	public SmartList( Vector<? extends E> listData )
	{
		super( listData );
		customizeList();
	}
	

	 /**
	  * Met en forme la liste pour l'adapter au smartphone.
	  */
	private void customizeList()
	{
		setCellRenderer( new SmartListCellRenderer() );
		
		//this.getModel().addListDataListener(arg0);
		setFont( Smartphone.getSmartFont("medium") );
		setBackground( Color.WHITE );
	}
}
