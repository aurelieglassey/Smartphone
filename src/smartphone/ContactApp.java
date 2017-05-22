package smartphone;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.awt.Color;
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
	
	JButton add = new JButton ("Add contact");
	JButton modify = new JButton ("Modifiy contact");
	JButton name = new JButton("Name");
	JButton Firstname = new JButton("Firstname");
	JButton email = new JButton("email");
	JButton PhoneNumber = new JButton("Phone number");
		
	JTextField textname = new JTextField();
	JTextField textfirstname = new JTextField();
	JTextField textemail = new JTextField();
	JTextField textphonenumber = new JTextField();
	
	
	public ContactApp()
	{
		super("Contact app");
				
		this.panel.add();
		this.panel.add(list);
		
		
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
