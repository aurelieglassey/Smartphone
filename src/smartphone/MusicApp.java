package smartphone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class MusicApp extends AbstractApp
{
	private JFileChooser chooser = new JFileChooser();
	private JButton open = new JButton("Ouvrir");
	
	public MusicApp( Smartphone phone )
	{
		super( phone, "Music app" );
		
		open.addActionListener( new Listener() );
		this.panel.add(open);
	}

	class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			
			 int returnVal = chooser.showOpenDialog( null );
			 
			 if(returnVal == JFileChooser.CANCEL_OPTION)
			 {
				 System.out.println("You chose to cancel this action ");
			 }
			 
			 if(returnVal == JFileChooser.APPROVE_OPTION)
			 {
				 File file = chooser.getSelectedFile();
				 System.out.println("Nom fichier : " +file.getName());
			 }
		}
	}

	public JPanel getAppPanel()
	{
		return new JPanel();
	}
}
