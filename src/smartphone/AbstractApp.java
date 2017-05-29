package smartphone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public abstract class AbstractApp implements Serializable
{
	protected Smartphone phone;
	protected String name;
	protected JPanel panel;
	protected JScrollPane scrollPane;
	protected JButton button;
	
	public AbstractApp( Smartphone phone, String appName )
	{
		this.phone = phone;
		this.name = appName;
		
		this.panel = getAppPanel();
		this.panel.setName( this.name );
		//this.panel.setBackground( new Color(255,0,0,50) );
		
		
		
		this.scrollPane = new JScrollPane( this.panel );
		
		
		//this.scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
		//this.scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		//this.scrollPane.setPreferredSize( new Dimension(200, 200) );
		//this.scrollPane.setBorder( null );
		
		//this.panel.setPreferredSize( this.phone.getScreenSize() );
		
		this.button = new JButton( this.name );
	}
	
	/**
	 * Permet à une application de retourner sa propre version d'un JPanel
	 * pour lui permettre d'avoir un contrôle sur le processus de painting.
	 * @return Un objet de classe JPanel ou d'une classe étendant JPanel
	 */
	public abstract JPanel getAppPanel();

	public JScrollPane getScrollPane()
	{
		return this.scrollPane;
	}
	
	public JPanel getPanel()
	{
		return this.panel;
	}
	
	public JButton getButton()
	{
		return this.button;
	}
	
	public String getName()
	{
		return this.name;
	}
}
