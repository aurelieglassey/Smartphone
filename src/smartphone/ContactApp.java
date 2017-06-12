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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

/**
 * Application contact qui contient les interfaces et ce que les boutons génèrent comme action
 * @author Aurélie
 *
 */
public class ContactApp extends AbstractApp
{
	/**
	 * Contient la liste de contact 
	 */
	private ArrayList<Contact> contactListData = new ArrayList<Contact>();
	
	private JList<Contact> contactList;
	
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
		
		
		File contactSer = this.phone.getContactFile();
		
		Object[] obj = Utils.deserializeObject( contactSer );

		if ( obj.length > 0 && obj[0] instanceof ArrayList<?> )
		{
			contactListData = ((ArrayList<Contact>) obj[0]);
		}
		
		else
		{
			contactListData = new ArrayList<Contact>();
		}
		
		contactList = new SmartList<Contact>( contactListData.toArray(new Contact[0]) );
		contactList.setCellRenderer( new ContactListCellRenderer() );
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contactList.addMouseListener(new DoubleClickListener());

		panelList.setBackground( new Color(40, 40, 40, 255) );
		panelList.setLayout( new GridLayout(1,1) );
		panelList.add( new SmartScrollPane(contactList) );

		
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
	 * @param c : null pour un JPanel d'ajout, ou un object Contact pour un JPanel de modification
	 * @param left : bouton gauche de l'appareil
	 * @param right : bouton droite de l'appareil
	 * @param titre : Titre d'ajout ou de modification
	 * @return Le panel d'ajout ou de modification
	 */
	private JPanel generatePanel( Contact c, JButton left, JButton right, JLabel titre )
	{
		//Générer les panels d'ajout et de modification
		String phone = "";
		String firstName = "";
		String name = "";
		String email = "";
		
		if ( c != null ) //met vide les données de contact lors d'un ajout
		{
			phone = c.getPhone();
			firstName = c.getFirstname();
			name = c.getName();
			email = c.getEmail();
		}
		
		tPhoneNumber.setText( phone );
		tFirstName.setText( firstName );
		tName.setText( name );
		tEmail.setText( email );
		
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
		
		if ( c != null && c.getImageFile() != null )
		{
			ImageIcon icon = new ImageIcon( c.getImageFile().toString() );
			icon = Utils.resizeIcon(icon, 100, 100);
			bPhoto.setIcon( icon );
		}
		
		box.add(bPhoto);
		
		box.add(lName);
		box.add(tName);
			
		box.add(lFirstName);
		box.add(tFirstName);
		
		box.add(lEmail);
		box.add(tEmail);
		
		box.add(lPhoneNumber);
		box.add(tPhoneNumber);
		
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
		Contact[] tmp = contactListData.toArray( new Contact[0] );
		contactList.setListData( tmp );
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

	
	public void associate (File f, Contact c)
	{
		System.out.println( "Fichier reçu : " + f );
		System.out.println( "Contact : " + c );
		
		if(contactListData.contains(c)) //chercher le contact c dans ma liste (contains(c), retourn un boolean)
		{
			System.out.println( "Contact trouvé !" );
			//si trouvé, stocker ImageFile dans contact + sérialisation
			
			// Association de l'image au contact
			c.setImageFile( f );
			
			System.out.println( "Image associée !" );
			
			for (Contact c2 : contactListData)
			{
				System.out.println( c2.getFirstname() + " " + c2.getPhone() + " " + c2.getImageFile() );
			}
			
			// Sérialization
			Utils.serializeObjects( this.phone.getContactFile(), contactListData);
		}
	}
	
	/**
	 * Ajout d'un contact dans l'arraylist et sérialization de l'object
	 * @param phone
	 * @param firstname
	 * @param name
	 * @param email
	 */
	private void addContact(String phone, String firstname, String name, String email) 
	{
		Contact newContact = new Contact (phone, firstname, name, email);
		contactListData.add(newContact);
		Utils.serializeObjects( this.phone.getContactFile(), contactListData);
		
	}
	
	/**
	 *Effacer un contact dasn l'arraylist + sérialization
	 */
	private void removeContact(Contact contactSelected)
	{
		contactListData.remove(contactSelected);
		Utils.serializeObjects( this.phone.getContactFile(), contactListData);
		
	}
	
	/**
	 * Getters et setters de l'ArrayList
	 * @return
	 */
	public ArrayList<Contact> getContactlist()
	{
		return contactListData;
	}

	public void setContactlist(ArrayList<Contact> contactlist)
	{
		contactlist = contactlist;
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
				panelAddContact = generatePanel( null, bCancel, bSaveContact, lTitreAdd );
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
					addContact(tPhoneNumber.getText(), tFirstName.getText(), tName.getText(), tEmail.getText() );
					panelAddContact = null;
					popPanel();
					refreshlist();
				}	
			}
			
			if (e.getSource()==bRemove)
			{
				removeContact(contactList.getSelectedValue());
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
					removeContact(contactList.getSelectedValue());
					addContact(tPhoneNumber.getText(), tFirstName.getText(), tName.getText(), tEmail.getText());
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
	public class DoubleClickListener extends MouseAdapter
	{
		public void mouseClicked( MouseEvent e )
		{
			if (e.getClickCount() == 2)
			{
				if (e.getSource() == contactList)
				{
					if(!contactList.getValueIsAdjusting()){
						contactList.getSelectedValue();
						contactSelected = (Contact) contactList.getSelectedValue();
						
						if ( contactSelected != null ) //si la selection a bien eu lieu dans notre Jlist
						{
							System.out.println("Contact selectionné :" +contactSelected);
							
							//Nouveau Panel pour la modification
							panelModifyContact = generatePanel( contactSelected, bRemove, bModify, lTitreModif );
							pushPanel(panelModifyContact);
						}
					}
				}
			}
		}
	}
	
}
