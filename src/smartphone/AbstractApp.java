package smartphone;

import java.awt.Color;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AbstractApp implements Serializable
{
	protected Smartphone phone;
	protected String name;
	protected JPanel panel;
	protected JButton button;
	
	public AbstractApp( Smartphone phone, String appName )
	{
		this.phone = phone;
		this.name = appName;
		
		this.panel = new JPanel();
		this.panel.setName( this.name );
		//this.panel.setBackground( new Color(255,0,0,50) );
		
		this.button = new JButton( this.name );
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
