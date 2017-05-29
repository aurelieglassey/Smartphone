package smartphone;

import javax.swing.JPanel;

public class ContactApp extends AbstractApp
{
	public ContactApp( Smartphone phone )
	{
		super( phone, "Contact app" );
	}
	
	public JPanel getAppPanel()
	{
		return new JPanel();
	}
}
