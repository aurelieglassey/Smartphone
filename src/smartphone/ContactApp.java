package smartphone;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.w3c.dom.events.MouseEvent;

//Manque méthode image du contact : deux méthode : récupérer la liste de contact //+ associer une photo à un contact (File, contact)
//commenter les statics
//image d'ajout du contact

/**
 * Application contact qui contient les interfaces et ce que les boutons génèrent comme action
 * @author Aurélie
 *
 */
public class ContactApp extends AbstractApp
{
	private JList<Contact> jlist;
	
	private JButton bAddContact = new SmartButton ( new ImageIcon("smartphone_root/sys/addContact.jpg"));
	private JButton bSaveContact = new SmartButton ("Save");
	private JButton bCancel = new SmartButton ("Cancel"); 
	private JButton bRemove = new SmartButton ("Remove contact");
	private JButton bModify = new SmartButton ("Modify contact");
	private JButton bPhoto = new JButton (new ImageIcon("smartphone_root/sys/addpicture.PNG"));
	
	private JPanel panelNorth = new JPanel();
	private JPanel panelList = new JPanel();
	private JPanel panelAddContact = null;
	private JPanel panelModifyContact = null;

	private JLabel lName = new SmartLabel("Name");
	private JLabel lFirstName = new SmartLabel("Firstname");
	private JLabel lEmail = new SmartLabel("Email");
	private JLabel lPhoneNumber = new SmartLabel("Phone number");
	private JLabel lTitreAdd = new SmartLabel ("Add a Contact");
	private JLabel lTitreModif = new SmartLabel ("Modify a Contact");

	private JTextField tName = new SmartTextField();
	private JTextField tFirstName = new SmartTextField();
	private JTextField tEmail = new SmartTextField();
	private JTextField tPhoneNumber = new SmartTextField();

	private Contact contactSelected ;

	/**
	 * Constructeur de l'application contact. On y trouve une Jlist qui stockera tous les contacts ainsi qu'un bouton d'ajout (pour un contact)
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

		panelList.setBackground( new Color(40, 40, 40, 255) );
		panelList.add(jlist);

		
		bAddContact.setOpaque(true);
		
		panelNorth.setBackground(new Color(40, 40, 40, 255));
		panelNorth.setLayout(new BorderLayout());
		panelNorth.add(bAddContact, BorderLayout.EAST);
		
		this.mainPanel.add(panelList, BorderLayout.CENTER);
		this.mainPanel.add(panelNorth, BorderLayout.NORTH);	
		
		bAddContact.addActionListener(new ListenerContact());
		bSaveContact.addActionListener(new ListenerContact());
		bCancel.addActionListener(new ListenerContact());
		bRemove.addActionListener(new ListenerContact());
		bModify.addActionListener(new ListenerContact());
		bPhoto.addActionListener(new ListenerContact());
	}
	
	/**
	 * Nouveau Panel
	 */
	public JPanel generateMainPanel()
	{
		return new JPanel();
	}
	
	/**
	 * Generatepanel est appelé chaque fois qu'un nouveau panel est nécessaire (ici lors de l'ajout d'un contact ou la modification d'un contact)
	 * @param phone : numéro de téléphone
	 * @param firstName : prénom
	 * @param name : nom
	 * @param email : email
	 * @param flushFields : si besoin d'effacer le contenus des JTextField
	 * @param left : bouton gauche de l'appareil
	 * @param right : bouton droite de l'appareil
	 * @param titre : Titre d'ajout ou de modification
	 * @return
	 */
	private JPanel generatepanel( JTextField phone, JTextField firstName, JTextField name, JTextField email, boolean flushFields, JButton left, JButton right, JLabel titre )
	{
		//Générer les panels d'ajout et de modification
		
		if ( flushFields ) //met vide les données de contact lors d'un ajout
		{
			phone.setText("");
			firstName.setText("");
			name.setText("");
			email.setText("");
		}
		
		JPanel p = new JPanel();
		p.setBackground(new Color(250, 250, 250));
		p.setLayout(new BorderLayout());
		
		JPanel panelTitre = new JPanel();
		titre.setBackground(new Color(255, 225, 228));
		panelTitre.setBackground(new Color(255, 225, 228, 255));
		panelTitre.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(192, 0, 0)));
		panelTitre.add(titre);
		
		JPanel panelBox = new JPanel (); //Pour les données à remplir d'un contact
		panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));
		Box box = Box.createVerticalBox();
		box.add(panelBox);
		box.setBackground(new Color(250, 250, 250));
			
		bPhoto.setOpaque(true);
		bPhoto.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		
		box.add(bPhoto);
		
		box.add(lName);
		box.add(name);
			
		box.add(lFirstName);
		box.add(firstName);
		
		box.add(lEmail);
		box.add(email);
		
		box.add(lPhoneNumber);
		box.add(phone);
		
		JPanel panelSouthButton = new JPanel(); //pour les boutons cancel et save
		panelSouthButton.setPreferredSize(new Dimension(480, 50));
		panelSouthButton.setLayout(new GridLayout(1, 2));
		panelSouthButton.add(left);
		panelSouthButton.add(right);

		
		p.add(panelTitre, BorderLayout.NORTH);
		p.add(box, BorderLayout.CENTER);
		p.add(panelSouthButton, BorderLayout.SOUTH);
		
		return p;
	}
	
	/**
	 * Méthode de rafraîchissement de la liste lors d'un ajout, d'une modification ou d'une suppression d'un contact.
	 */
	private void refreshlist() 
	{
		Contact[] tmp = new Contact[0];
		tmp = ContactRepertory.getContactlist().toArray(tmp);
		jlist.setListData( tmp );
	}
	
	/**
	 * Méthode pour le bouton du retour de l'appareil
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

//
	public void associate (File f, Contact c)
	{
		if(ContactRepertory.getContactlist().contains(c)) //chercher le contact c dans ma liste (contains(c), retourn un boolean)
		{
			//si trouvé, stocker ImageFile dans contact + sérialisation
			Utils.serializeObjects(new File (".\\Contactlist.ser"), ContactRepertory.getContactlist());
		}		
		System.out.println("Si pas trouver le contact dans liste, rien ne se passe"); //si pas trouver rien ne se passe
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
				panelAddContact = generatepanel( tPhoneNumber, tFirstName, tName, tEmail, true, bCancel, bSaveContact, lTitreAdd );
				pushPanel(panelAddContact);
				refreshlist();
			}
			
			if (e.getSource()==bCancel)
			{
				panelAddContact = null;
				popPanel();
			}

			if (e.getSource()==bSaveContact)
			{
				//vérification que le nom, prénom et le téléphone ne manquent pas
				if((tName.getText().equals("") && tFirstName.getText().equals("")) || tPhoneNumber.getText().equals(""))
				{
					System.out.println("Données incomplètes à l'ajout : Manque name, firstname ou phone");
				}
				else {
					ContactRepertory.addContact(tPhoneNumber.getText(), tFirstName.getText(), tName.getText(), tEmail.getText() );
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
				if(tName.getText().equals("") && tFirstName.getText().equals("") || tPhoneNumber.getText().equals(""))
				{
					System.out.println("Données incomplètes à la modification : Manque name, firstname ou phone");
				}
				else {
					//suppression du contact sélectionner et ajout du contact modifier
					lTitreModif.setBackground(new Color(255, 225, 228));
					ContactRepertory.removeContact(jlist.getSelectedValue());
					ContactRepertory.addContact(tPhoneNumber.getText(), tFirstName.getText(), tName.getText(), tEmail.getText());
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
						
						tPhoneNumber.setText(contactSelected.getPhone());
						tFirstName.setText(contactSelected.getFirstname());
						tName.setText(contactSelected.getName());
						tEmail.setText(contactSelected.getEmail());
						
						//Nouveau Panel pour la modification
						panelModifyContact = generatepanel( tPhoneNumber, tFirstName, tName, tEmail, false, bRemove, bModify, lTitreModif );
						pushPanel(panelModifyContact);
					}
				}
			}
		}
	}
	
}
