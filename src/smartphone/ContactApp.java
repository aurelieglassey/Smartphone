package smartphone;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;


public class ContactApp extends AbstractApp
{
	protected ArrayList<Contact> contactlist = new ArrayList<>();
	protected JList<Contact> list = new JList(contactlist.toArray());
	
	Contact newcontact;
	
	JButton bAddContact = new JButton ("Add contact");
	JButton bModifyContact = new JButton ("Modifiy contact");
	JButton bSaveContact = new JButton ("Save");
	JButton bCancel = new JButton ("Cancel"); //pas oublier 
	
	JLabel lname = new JLabel("Name");
	JLabel lFirstname = new JLabel("Firstname");
	JLabel lemail = new JLabel("email");
	JLabel lPhoneNumber = new JLabel("Phone number");
		
	JTextField textname = new JTextField();
	JTextField textfirstname = new JTextField();
	JTextField textemail = new JTextField();
	JTextField textphonenumber = new JTextField();
	
	
	JPanel panelAddContactNorth;
	JPanel panelAddContactCenter;
	BoxLayout boxlayout;
	SpringLayout springlayout;
	GridLayout gridlayout;
	
	
	public ContactApp()
	{
		super("Contact app");
		
		this.panel.setLayout(new BorderLayout());
		bAddContact.addActionListener(new ListenerAdd());
		
		JPanel tmp = new JPanel();
		tmp.setBackground( Color.GREEN );
		tmp.setLayout( new FlowLayout() );
		tmp.add( bAddContact );
		tmp.add( list, BorderLayout.CENTER );
		
		this.panel.add( tmp, BorderLayout.NORTH );
		

		
		//Ajouter les Boutons save et cancel + actionlistener
		
		
		
	}
	
	class ListenerAdd implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==bAddContact)
			{
				JPanel p = new JPanel();
				p.setBackground( Color.RED );
				
				
				p.add( new JButton("xxx") );
				ContactApp.this.panel.add( p, BorderLayout.CENTER );
				
				/*panelAddContactNorth = new JPanel();
				panelAddContactCenter = new JPanel();
				
				panelAddContactNorth.setBackground(Color.RED);
				panelAddContactCenter.setBackground(Color.BLUE);
				
				
				System.out.println("d");
				//ContactApp.this.panel.remove( bAddContact );
				//ContactApp.this.panel.remove( list );
				
				
				JPanel tmp = new JPanel();
				
				tmp.setLayout( new BorderLayout() );
				
				tmp.add(panelAddContactNorth, BorderLayout.NORTH);
				tmp.add(panelAddContactCenter, BorderLayout.CENTER);
				
				ContactApp.this.panel.add( tmp, BorderLayout.CENTER );/*
				
					
				/*
				gridlayout = new GridLayout(1, 2);
				panelAddContactNorth.setLayout(gridlayout);
				panelAddContactNorth.add(bCancel);
				panelAddContactNorth.add(bSaveContact);
				
				
				springlayout = new SpringLayout();
				boxlayout = new BoxLayout(panelAddContactCenter, boxlayout.PAGE_AXIS);
				
				
				
				panelAddContactCenter.setLayout(boxlayout);
				
				panelAddContactCenter.add(lname);
				panelAddContactCenter.add(textname);
				
				panelAddContactCenter.add(lFirstname);
				panelAddContactCenter.add(textfirstname);
				
				panelAddContactCenter.add(lemail);
				panelAddContactCenter.add(textemail);
				
				panelAddContactCenter.add(lPhoneNumber);
				panelAddContactCenter.add(textphonenumber);*/
				
				
				
				
				
				
				
				
			}
			
			if (e.getSource()==bSaveContact)
			{
				addContact(newcontact);
				
				
			}
			
			if (e.getSource()==bCancel)
			{
				//retour au panel pr�c�dent avec AddContact
			}
			
		}
		
	}
	

	
	public void addContact(Contact c) //ajout du contact dans l'arraylist et sauvegarde de l'arraylist ou le contact � �t� ajout�
	{
		newcontact = new Contact (textname.getText(), textfirstname.getText(), textemail.getText(), textphonenumber.getText() );
		this.contactlist.add(newcontact);
		serializeContact(contactlist);
		//ajout d'un label contact enregistr� 
	}
	
	/*private void modifyContact(Contact c)
	{
		//this.contactlist.
	}

	private void removeContact(Contact c)
	{
		this.contactlist.remove(c);
	}
	
	private void refreshlist(ArrayList<Contact> contactlist)
	{
		//trier par ordre alphab�tique
	}
	
	*/
	
	
	public static void serializeContact(ArrayList<Contact> contactlist) throws IOException{ 
		
		
		FileOutputStream fichier = new FileOutputStream ("C:\\Users\\Aur�lie\\Desktop");
		ObjectOutputStream output = new ObjectOutputStream(fichier);
		output.writeObject(contactlist);
		output.close();
			
		
	}
	
}
