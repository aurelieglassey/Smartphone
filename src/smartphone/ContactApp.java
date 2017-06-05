package smartphone;

import javax.swing.JPanel;

public class ContactApp extends AbstractApp
{
	public ContactApp( Smartphone phone )
	{
		super( phone, "Contact app", "contact" );
	}
	
	public JPanel generateMainPanel()
	{
		return new JPanel();
	}
}
