package smartphone;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

public class SmartList<E> extends JList<E>
{
	public SmartList()
	{
		super();
		customizeList();
	}

	public SmartList( E[] listData )
	{
		super( listData );
		customizeList();
	}

	public SmartList( ListModel<E> dataModel )
	{
		super( dataModel );
		customizeList();
	}
	
	public SmartList( Vector<? extends E> listData )
	{
		super( listData );
		customizeList();
	}
	
	private void customizeList()
	{
		setCellRenderer( new SmartListCellRenderer() );
		
		//this.getModel().addListDataListener(arg0);
		setFont( Smartphone.getSmartFont("medium") );
		setBackground( Color.WHITE );
	}
}
