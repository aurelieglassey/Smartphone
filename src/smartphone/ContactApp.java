package smartphone;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//Manque image du contact
//méthode de sérialization
//commenter le code avec /**
//Ecriture du smartphone : Railway
//Design

//Junit trouver 1 méthode dont on peut savoir le résultat



//renomer les varaible avec de majuscules
//réordrer tout le code avec l'ordre de fabien
//deux méthode : récupérer la liste de contact 
	//+ associer une photo à un contact (File, contact)



public class ContactApp extends AbstractApp
{
	private Contact[] listContact;
	private JList<Contact> jlist;

	private JButton bAddContact = new SmartButton ( new ImageIcon("smartphone_root/sys/addContact.jpg"));
	private JButton bSaveContact = new SmartButton ("Save");
	private JButton bCancel = new SmartButton ("Cancel"); 
	private JButton bRemove = new SmartButton ("Remove contact");
	private JButton bModify = new SmartButton ("Modify contact");

	private JPanel panelnorth = new JPanel();
	private JPanel panellist = new JPanel();
	private JPanel panelAddContact = null;
	private JPanel panelModifyContact = null;

	private JLabel lname = new SmartLabel("Name");
	private JLabel lFirstname = new SmartLabel("Firstname");
	private JLabel lemail = new SmartLabel("email (facultatif)");
	private JLabel lPhoneNumber = new SmartLabel("Phone number");
	private JLabel ltitreaAdd = new SmartLabel ("Add a Contact");
	private JLabel ltitreModif = new SmartLabel ("Modify a Contact");
	
	private JButton bphoto = new JButton (new ImageIcon("smartphone_root/sys/return.PNG"));

	private JTextField tname = new SmartTextField();
	private JTextField tfirstname = new SmartTextField();
	private JTextField temail = new SmartTextField();
	private JTextField tphonenumber = new SmartTextField();

	private Contact contactSelected ;

	/**
	 * Constructeur de l'application de contact. On y trouve une Jlist ainsi qu'un bouton d'ajout (pour un contact)
	 * @param phone
	 */
	public ContactApp( Smartphone phone )
	{
		super( phone, "Contact app", "contact" );

		this.mainPanel.setLayout(new BorderLayout());
		this.mainPanel.setBackground(new Color(40, 40, 40, 255));
		
		File contactSer = new File(".\\Contactlist.ser");
		try
		{
			contactSer.createNewFile();
		} 
		catch (IOException e)
		{
			System.err.println( "Impossible de créer " + contactSer );
		}
		
		Object[] obj = Utils.deserializeObject( contactSer );
		
		// if ( obj[0] instanceof ArrayList<Contact> )
		ContactRepertory.setContactlist( ((ArrayList<Contact>) obj[0]) );
		
		jlist = new JList(ContactRepertory.getContactlist().toArray());
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.addListSelectionListener(new SelectionListener());

		panellist.setBackground( new Color(40, 40, 40, 255) );
		panellist.add(jlist);
		
		panelnorth.setBackground(new Color(40, 40, 40, 255));
		panelnorth.setLayout(new BorderLayout());
		panelnorth.add(bAddContact, BorderLayout.EAST);
		
		this.mainPanel.add(panellist, BorderLayout.CENTER);
		this.mainPanel.add(panelnorth, BorderLayout.NORTH);	
		
		bAddContact.addActionListener(new ListenerContact());
		bSaveContact.addActionListener(new ListenerContact());
		bCancel.addActionListener(new ListenerContact());
		bRemove.addActionListener(new ListenerContact());
		bModify.addActionListener(new ListenerContact());
		
	}

	
	
	public JPanel generateMainPanel()
	{
		return new JPanel();
	}
	
	
	private JPanel generatepanel( JTextField name, JTextField firstName, JTextField email, JTextField phone, boolean flushFields, JButton left, JButton right, JLabel titre )
	{
		//Générer les panels d'ajout et de modification
		
		if ( flushFields )
		{
			name.setText("");
			firstName.setText("");
			email.setText("");
			phone.setText("");
		}
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		
		JPanel panelTitre = new JPanel();
		panelTitre.setBackground(new Color(250, 250, 250));
		panelTitre.add(titre);

		JPanel panelBox = new JPanel (); //Pour les données à remplir d'un contact
		panelBox.setBackground(new Color(250, 250, 250));
		
		panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));
		
		bphoto.setPreferredSize(new Dimension(400, 200));
		bphoto.setOpaque(true);
		bphoto.setBackground(new Color(250, 250, 250));
		bphoto.setBorder(null);
		
		panelBox.add(bphoto);
		
		panelBox.add(lname);
		panelBox.add(name);

		panelBox.add(lFirstname);
		panelBox.add(firstName);

		panelBox.add(lemail);
		panelBox.add(email);

		panelBox.add(lPhoneNumber);
		panelBox.add(phone);
		

		JPanel panelSouthButton = new JPanel(); //pour les boutons cancel et save
		panelSouthButton.setPreferredSize(new Dimension(480, 50));
		panelSouthButton.setLayout(new GridLayout(1, 2));
		panelSouthButton.add(left);
		panelSouthButton.add(right);

		p.add(panelTitre, BorderLayout.NORTH);
		p.add(panelBox, BorderLayout.CENTER);
		p.add(panelSouthButton, BorderLayout.SOUTH);
		
		return p;
	}
	
	
	/**
	 * Cette classe contient tous les ActionListener des boutons de l'application Contact
	 * @author Aurélie
	 *
	 */
	class ListenerContact implements ActionListener 
	{
		public void actionPerformed(ActionEvent e)
		{


			if(e.getSource()==bAddContact)
			{
				System.out.println("Ajout contact");
				panelAddContact = generatepanel( tname, tfirstname, temail, tphonenumber, true, bCancel, bSaveContact, ltitreaAdd );
				pushPanel(panelAddContact);
				refreshlist();
			}
			
			if (e.getSource()==bCancel)
			{
				System.out.println("cancel");
				panelAddContact = null;
				popPanel();
				
			}

			if (e.getSource()==bSaveContact)
			{
				//vérification que le nom, prénom et le téléphone ne manquent pas
				if(tname.getText().equals("") || tfirstname.getText().equals("") || tphonenumber.getText().equals(""))
				{
					System.out.println("Manque name, firstname ou phone");
				}
				else {
					System.out.println("Save");
					ContactRepertory.addContact(tname.getText(), tfirstname.getText(), temail.getText(), tphonenumber.getText());
					panelAddContact = null;
					popPanel();
					refreshlist();
				}
				
			}
			
			if (e.getSource()==bRemove)
			{
				ContactRepertory.removeContact(jlist.getSelectedValue());
				panelModifyContact = null;
				popPanel();
				
				refreshlist();
			}
			
			if (e.getSource()==bModify)
			{
				if(tname.getText().equals("") || tfirstname.getText().equals("") || tphonenumber.getText().equals(""))
				{
					System.out.println("Manque name, firstname ou phone");
				}
				else {
					//suppression du contact sélectionner et ajout du contact modifier
					ContactRepertory.removeContact(jlist.getSelectedValue());
					ContactRepertory.addContact(tname.getText(), tfirstname.getText(), temail.getText(), tphonenumber.getText());
					panelModifyContact = null;
					popPanel();
					refreshlist();
				}
				
			}
			
		}	
	}
	
	/**
	 * Cette classe est nécessaire pour la selection d'un objet dans la JList
	 * @author Aurélie
	 *
	 */
	public class SelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if(e.getSource()==jlist)
			{
				if(!jlist.getValueIsAdjusting()){
					jlist.getSelectedValue();
					contactSelected = (Contact) jlist.getSelectedValue();
					
					if ( contactSelected != null ) //si la selection a bien eu lieu dans notre Jlist
					{
						System.out.println("Contact selectionné :" +contactSelected);
						
						tname.setText(contactSelected.getName());
						tfirstname.setText(contactSelected.getFirstname());
						temail.setText(contactSelected.getemail());
						tphonenumber.setText(contactSelected.getPhone());
						
						//Nouveau Panel de modification
						panelModifyContact = generatepanel(tname, tfirstname, temail, tphonenumber, false, bRemove, bModify, ltitreModif );
						pushPanel(panelModifyContact);
					}
				}
			}
		}
	}
	
	private void refreshlist() //méthode de rafraîchissement de la liste lors d'un ajout, modification ou suppression d'un contact.
	{
		Contact[] tmp = new Contact[0];
		tmp = ContactRepertory.getContactlist().toArray(tmp);
		jlist.setListData( tmp );
	}
	
	/**
	 * Méthode pour le bouton du retour
	 */
	public void returnPressed()
	{
		JPanel removed = popPanel();
		
		if(panelAddContact!=null)
		{
			panelAddContact.remove(removed);
			panelAddContact = null;
		}
		
		if(panelModifyContact!=null)
		{
			panelModifyContact.remove(removed);
			panelModifyContact=null;
		}
	}
	
	public void associate (File f, Contact c)
	{
		//chercher le contact c dans ma liste (contains(c), retourn un boolean)
		//si pas trouver rien ne se passe
		//si trouvé, stocker ImageFile dans contact
		//sérialisation
	}
	
	
}
