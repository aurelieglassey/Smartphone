package smartphone;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javafx.scene.paint.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class ContactApp extends AbstractApp
{
	
	
	public ContactApp()
	{
		super("Contact app");
		JPanel paneltitre = new JPanel ();
		JLabel titre = new JLabel ("Titre");
		
		paneltitre.add(titre);
		this.panel.add(paneltitre, BorderLayout.NORTH);
		
		//titre.setBackground(Color.BLUEVIOLET); 
		//this.panel.add(titre);
		
	}
	
	
}
