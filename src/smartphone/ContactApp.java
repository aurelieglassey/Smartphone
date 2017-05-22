package smartphone;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ContactApp extends AbstractApp
{
	protected ArrayList<Contact> contactlist = new ArrayList<>();
	protected JList<Contact> list = new JList(contactlist.toArray());
	
	JButton bAddContact = new JButton ("Add contact");
	JButton bModifyContact = new JButton ("Modifiy contact");
	JButton bSaveContact = new JButton ("Save");
	JButton bCancel = new JButton ("Cancel"); //pas oublier 
	
	JButton bname = new JButton("Name");
	JButton bFirstname = new JButton("Firstname");
	JButton bemail = new JButton("email");
	JButton bPhoneNumber = new JButton("Phone number");
		
	JTextField textname = new JTextField();
	JTextField textfirstname = new JTextField();
	JTextField textemail = new JTextField();
	JTextField textphonenumber = new JTextField();
	
	
	public ContactApp()
	{
		super("Contact app");
				
		bAddContact.addActionListener(new ListenerAdd());
		this.panel.add(bAddContact);
		this.panel.add(list);
		
		
		
	}
	
	class ListenerAdd implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==bAddContact)
			{
				ContactApp.this.panel.remove(ContactApp.this.panel);
				
				JPanel panelAddContact = new JPanel();
				
				System.out.println("d");
				
				
			}
			
		}
		
	}
	

	
	private void addContact(Contact c)
	{
		this.contactlist.add(c);
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
		//trier par ordre alphabétique
	}
	
	*/
	
	
	public static void serializeContact(ArrayList<Contact> contactlist){ 
		
		try 
		{
			FileOutputStream fichier = new FileOutputStream ("C:\\Users\\Aurélie\\Desktop");
			ObjectOutputStream output = new ObjectOutputStream(fichier);
			output.writeObject(contactlist);
			output.close();
			
		}
		catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}
	
}
