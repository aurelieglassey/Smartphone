package smartphone;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class AbstractApp
{
	protected String name;
	protected JPanel panel;
	protected JButton button;
	
	public AbstractApp( String appName )
	{
		this.name = appName;
		
		this.panel = new JPanel();
		this.panel.setName( this.name );
		this.panel.setBackground( Color.WHITE );
		
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
