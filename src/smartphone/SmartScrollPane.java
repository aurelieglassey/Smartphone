package smartphone;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;

public class SmartScrollPane extends JScrollPane
{
	public SmartScrollPane()
	{
		super();
		customizeScrollPane();
	}

	public SmartScrollPane( Component view )
	{
		super( view );
		customizeScrollPane();
	}
	
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
