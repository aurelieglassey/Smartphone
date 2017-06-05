package smartphone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public abstract class AbstractApp implements Serializable
{
	protected Smartphone phone;
	protected String name;
	protected String label;
	protected JPanel mainPanel;
	protected JButton button;
	protected boolean started = false;
	
	public AbstractApp( Smartphone phone, String appName, String appLabel )
	{
		this.phone = phone;
		this.name = appName;
		this.label = appLabel;
		
		this.mainPanel = generateMainPanel();
		
		
		
		//this.mainScrollPane = new JScrollPane( this.mainPanel );
		//this.mainScrollPane.getVerticalScrollBar().setPreferredSize( new Dimension(this.phone.getScrollBarWidth(), 0) );
		//this.scrollPane.setBorder( null );
		
		this.button = new JButton( this.name );
	}
	
	/**
	 * Permet � une application de retourner sa propre version d'un JPanel
	 * pour lui permettre d'avoir un contr�le sur le processus de painting.
	 * @return Un objet de classe JPanel ou d'une classe �tendant JPanel
	 */
	public abstract JPanel generateMainPanel();
	
	public JPanel getMainPanel()
	{
		return this.mainPanel;
	}
	
	public JButton getButton()
	{
		return this.button;
	}

	public String getName()
	{
		return this.name;
	}
	
	public String getLabel()
	{
		return this.label;
	}

	public void startApp()
	{
		started = true;
		pushPanel( this.getMainPanel() );
	}
	
	public void stopApp()
	{
		started = false;
		popPanel();
	}
	
	// Ajoute et affiche un nouveau panel
	public void pushPanel( JPanel panel )
	{
		this.phone.pushAppPanel( this, panel );
	}
	
	// Masque et retire le panel existant
	public void popPanel()
	{
		this.phone.popAppPanel( this );
	}
}
