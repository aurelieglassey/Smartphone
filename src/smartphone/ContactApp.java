package smartphone;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javafx.scene.shape.Box;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

//regarder comment r�cup�rer le clique sur la bonne colonne de l'arraylist getselectedrow ou je sais plus
public class ContactApp extends AbstractApp
{
	private Contact[] listContact;
	protected JList<Contact> list;
	
	JButton temporaire = new JButton ("Frame temporaire");
	JButton bAddContact = new JButton ("Add contact");
	JButton bSaveContact = new JButton ("Save");
	JButton bCancel = new JButton ("Cancel"); 
	JButton bRemove = new JButton ("Remove contact");
	
	JPanel panelnorth = new JPanel();
	JPanel panellist = new JPanel();
	JPanel panelAddContact;
	
	JLabel lname = new JLabel("Name");
	JLabel lFirstname = new JLabel("Firstname");
	JLabel lemail = new JLabel("email");
	JLabel lPhoneNumber = new JLabel("Phone number");
		
	JTextField textname = new JTextField();
	JTextField textfirstname = new JTextField();
	JTextField textemail = new JTextField();
	JTextField textphonenumber = new JTextField();
	
	
	
	
	
	
	
	
	
	
	Contact newcontact;
	
	JButton bModifyContact = new JButton ("Modifiy contact");
	
	
	
	
	
	JPanel panelAddContactCenter;
	
	
	GridLayout gridlayout;
	
	
	public ContactApp()
	{
		super("Contact app");
		
		this.panel.setLayout(new BorderLayout());
		
		this.panel.add(temporaire);
		temporaire.addActionListener(new ListenerContact());
		
		Contact c = new Contact("Glassey ", "Aur�lie ", "@@@", "079");
		Contact c1 = new Contact("Ducrey ", "C�cile ", "@@@", "070");
		ContactRepertory.contactlist.add(c);
		ContactRepertory.contactlist.add(c1);
		
		list = new JList(ContactRepertory.contactlist.toArray());
		
		
		
		
		
	}
	
	public class FrameTempContact extends JFrame
	{
		public FrameTempContact ()
		{
			setVisible(true);
			setPreferredSize(new Dimension(480, 800));
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			panellist.setBackground(Color.GREEN);
			panellist.add(list);
			
			panelnorth.add(bAddContact);
			add(panellist, BorderLayout.CENTER);
			add(panelnorth, BorderLayout.NORTH);
			
			bAddContact.addActionListener(new ListenerContact());
			bSaveContact.addActionListener(new ListenerContact());
			bCancel.addActionListener(new ListenerContact());
			bRemove.addActionListener(new ListenerContact());
			
					
			pack();
		
		}
	}

	
	class ListenerContact implements ActionListener //cr�ation d'une fen�tre temporaire apr�s contactApp
	{

		public void actionPerformed(ActionEvent e)
		{
			
			if(e.getSource()==temporaire){
				FrameTempContact f= new FrameTempContact();
				f.setVisible(true);
				
			}
			
			
			
			if(e.getSource()==bAddContact)
			{
				//appel d'un nouveau panel avec la m�thode � fab
				//panelfab
				JPanel panelfab = new JPanel();
				panelfab.setLayout(new BorderLayout());
				panelfab.setBackground(Color.black);
				add(panelfab);
				
				//pour les boutons cancel et save
				JPanel panelnorth = new JPanel(); 
				panelnorth.setPreferredSize(new Dimension(480, 50));
				panelnorth.setLayout(new GridLayout(1, 2));
				panelnorth.add(bCancel);
				panelnorth.add(bSaveContact);
				
				//Pour les donn�es � remplir d'un contact
				JPanel panelBox = new JPanel (); 
				panelBox.setPreferredSize(new Dimension(480, 400));
				
				panelBox.setBackground(Color.red);
				panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));

				panelBox.add(lname);
				panelBox.add(textname);
				
				panelBox.add(lFirstname);
				panelBox.add(textfirstname);

				panelBox.add(lemail);
				panelBox.add(textemail);
				
				panelBox.add(lPhoneNumber);
				panelBox.add(textphonenumber);
				
				panelfab.add(panelnorth, BorderLayout.NORTH);
				panelfab.add(panelBox, BorderLayout.CENTER);		
				
				//pack();
				
			}
			
			
			if (e.getSource()==bCancel)
			{
				//retour au panel pr�c�dent avec AddContact
			}
			
			
			if (e.getSource()==bSaveContact)
			{
				//est ce que fabien veut une "erreur" si deux fois le m�me pr�nom entr� ?
				ContactRepertory.addContact(textname.getText(), textfirstname.getText(), textemail.getText(), textphonenumber.getText());
				//JLabel l = new JLabel("Contact enregistr�");//ajout d'un label contact enregistr� 
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			if (e.getSource()==list.getSelectedValue())
			{
				modifyContact(newcontact);
			}
			
			if (e.getSource()==bRemove)
			{
				//remove(newcontact);
			}
			
			
		}
		
	}
	

	public static void removeContact(Contact c)
	{
		contactlist.remove(c);
	}
	
	
	private void modifyContact(Contact c)
	{
		//this.contactlist.deserialize
		textname.getText();
		textfirstname.getText();
		textemail.getText();
		textphonenumber.getText();
		
		//removeContact(c);
		//on appuie sur Save : sauvegarde 
		
	}

	
	
	
	/*private void refreshlist(ArrayList<Contact> contactlist)
	{
		//trier par ordre alphab�tique
		Collections.sort(this.contactlist, new Comparator<Contact>());
		
	}
	
	*/
	
	
	
	
	
	public static void serializeContact(ArrayList<Contact> contactlist) throws IOException{ 
		
		
		FileOutputStream fichier = new FileOutputStream ("C:\\Users\\Aur�lie\\Desktop\\Contacts.ser");
		ObjectOutputStream output = new ObjectOutputStream(fichier);
		output.writeObject(contactlist);
		output.close();
			
		
	}
	
}
