package smartphone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class ModifyContact
{
	
	private Contact c;
	
	public ModifyContact (Contact c){
		this.c = c;
		//FENETRE LORS DE LA SELECTION D'UN CONTACT
		JPanel panelfab = new JPanel();
		panelfab.setLayout(new BorderLayout());
		panelfab.setBackground(Color.black);
		add(panelfab);
		
		JPanel panelTitre = new JPanel();
		panelTitre.add(ltitreContact);

		JPanel panelBox = new JPanel (); //Pour les données à remplir d'un contact
		panelBox.setPreferredSize(new Dimension(480, 400));
		
		panelBox.setBackground(Color.red);
		
		panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));

		
		
		panelBox.add(lname);
		panelBox.add(textname.setText(c.getName()));
	
		panelBox.add(lFirstname);
		panelBox.add(textfirstname.setText(c.getFirstname()));
		
		panelBox.add(lemail);
		panelBox.add(textemail.setText(c.getemail());
		
		panelBox.add(lPhoneNumber);
		panelBox.add(textphonenumber.setText(c.getPhone());
		
		
		JPanel panelSouthButton = new JPanel(); //pour les boutons cancel et save
		panelSouthButton.setPreferredSize(new Dimension(480, 50));
		panelSouthButton.setLayout(new GridLayout(1, 2));
		panelSouthButton.add(bModify);
		panelSouthButton.add(bRemove);
		
		
		panelfab.add(panelTitre, BorderLayout.NORTH);
		panelfab.add(panelBox, BorderLayout.CENTER);
		panelfab.add(panelSouthButton, BorderLayout.SOUTH);		
		
		
			
	}
	
	
	
	

}
