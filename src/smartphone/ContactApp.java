package smartphone;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Application contact qui contient les interfaces, les boutons, les méthodes nécessaire et la liste de contact.
 * On peut ajouter, supprimer ou modifier un contact à partir de l'arraylist.
 * @author Aurélie Glassey
 */
public class ContactApp extends AbstractApp
{
	/**
	 * Contient la liste de contact 
	 */
	private ArrayList<Contact> contactListData = new ArrayList<Contact>();
	
	/**
	 * Contient l'arraylit de contact
	 */
	private JList<Contact> contactList;
	
	//Boutons de l'application contact
	private JButton bAddContact = new SmartButton ( new ImageIcon("smartphone_root/sys/addContact.jpg"));
	private JButton bSaveContact = new SmartButton ("Save");
	private JButton bCancel = new SmartButton ("Cancel"); 
	private JButton bRemove = new SmartButton ("Remove contact");
	private JButton bModify = new SmartButton ("Modify contact");
	private JButton bPhoto = new JButton (new ImageIcon("smartphone_root/sys/pictogram_contact.PNG"));
	
	//Panel de l'application contact
	private JPanel panelNorth = new JPanel();
	private JPanel panelList = new JPanel();
	private JPanel panelAddContact = null;
	private JPanel panelModifyContact = null;
	
	//Label de l'application contact
	private JLabel lName = new SmartLabel("Name");
	private JLabel lFirstName = new SmartLabel("Firstname");
	private JLabel lEmail = new SmartLabel("Email");
	private JLabel lPhoneNumber = new SmartLabel("Phone number");
	private JLabel lTitreAdd = new SmartLabel ("Add a contact");
	private JLabel lTitreModif = new SmartLabel ("Modify a contact");
	private JLabel lChampsObligatoires = new SmartLabel ("*Champs obligatoires : Name ou Firstname, et Phone");

	//TextField de l'application contact
	private JTextField tName = new SmartTextField();
	private JTextField tFirstName = new SmartTextField();
	private JTextField tEmail = new SmartTextField();
	private JTextField tPhoneNumber = new SmartTextField();

	/**
	 * Contact selectionné
	 */
	private Contact contactSelected ;

	/**
	 * Constructeur de l'application contact. On y trouve une Jlist qui stockera tous les contacts ainsi qu'un bouton d'ajout (pour un contact)
	 * @param phone 
	 */
	public ContactApp( Smartphone phone )
	{
		super( phone, "Contact app", "contact" );

		this.getMainPanel().setLayout(new BorderLayout());
		this.getMainPanel().setBackground(new Color(40, 40, 40, 255));
		
		
		File contactSer = this.getPhone().getContactFile();
		
		Object[] obj = Utils.deserializeObject( contactSer );

		if ( obj.length > 0 && obj[0] instanceof ArrayList<?> )
		{
			contactListData = ((ArrayList<Contact>) obj[0]);
		}
		
		else
		{
			contactListData = new ArrayList<Contact>();
		}
		
		contactList = new SmartList<Contact>();
		sortAndRefreshList();
		contactList.setCellRenderer( new ContactListCellRenderer() );
		contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contactList.addMouseListener(new DoubleClickListener());
		
		
		
		panelList.setBackground( new Color(250, 250, 250, 255) );
		panelList.setLayout( new GridLayout(1,1) );
		panelList.add( new SmartScrollPane(contactList) );

		bAddContact.setOpaque(false);
		bAddContact.setBackground( new Color(250, 250, 250, 255) );
		
		JLabel title = new SmartLabel( "My contacts" );
		title.setFont( Smartphone.getSmartFont("large") );
		panelNorth.setBackground(new Color(250, 250, 250, 255));
		//panelNorth.setBackground( Color.RED );
		panelNorth.setLayout(new BorderLayout());
		panelNorth.add(title, BorderLayout.WEST);
		panelNorth.add(bAddContact, BorderLayout.EAST);
		
		this.getMainPanel().add(panelList, BorderLayout.CENTER);
		this.getMainPanel().add(panelNorth, BorderLayout.NORTH);	
		
		//Actions des boutons
		bAddContact.addActionListener(new ListenerContact());
		bSaveContact.addActionListener(new ListenerContact());
		bCancel.addActionListener(new ListenerContact());
		bRemove.addActionListener(new ListenerContact());
		bModify.addActionListener(new ListenerContact());
		bPhoto.addActionListener(new ListenerContact());
	}
	
	/**
	 * Generatepanel est appelé chaque fois qu'un nouveau panel est nécessaire (ici lors de l'ajout d'un contact ou la modification d'un contact)
	 * Lors d'un ajout de contact, les champs à remplir (nom, prénom, email, téléphone) sont mis vide
	 * @param c null pour un JPanel d'ajout, ou un object Contact pour un JPanel de modification
	 * @param left bouton gauche de l'appareil
	 * @param right bouton droite de l'appareil
	 * @param titre Titre d'ajout ou de modification
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
		panelTitre.setLayout( new BorderLayout() );
		panelTitre.setBackground(new Color(250, 250, 250, 255));
		titre.setFont(new Font ("Raleway", Font.PLAIN, 30));
		titre.setHorizontalAlignment( SwingConstants.CENTER );
		panelTitre.add(titre, BorderLayout.NORTH);
		
		
		
		bPhoto.setOpaque(true);
		bPhoto.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.BLACK, Color.BLACK));
		
	
		//si il y a une image au contact
		if ( c != null && c.getImageFile() != null )
		{
			ImageIcon icon = new ImageIcon( c.getImageFile().toString() );
			icon = Utils.resizeIcon(icon, 100, 100);
			bPhoto.setIcon( icon );
		}
		else //si pas d'image associée au contact
		{
			File defaultImage = new File (this.getPhone().getSysFolder(), "pictogram_contact.PNG");
			bPhoto.setIcon(new ImageIcon(defaultImage.toString()));
		}
		
		JPanel photoPanel = new JPanel();
		photoPanel.setBackground( new Color(250,250,250) );
		photoPanel.setLayout( new FlowLayout(FlowLayout.CENTER) );
		photoPanel.add( bPhoto );
		panelTitre.add( photoPanel, BorderLayout.SOUTH);
		
		
		
		JPanel box = new JPanel();
		box.setLayout( new BoxLayout(box, BoxLayout.PAGE_AXIS) );
		box.setBackground(new Color(250, 250, 250));
			
		lChampsObligatoires.setFont(new Font ("Raleway", Font.ITALIC, 15));
		
		
		//Ajout des champs pour remplir la page de modification ou d'ajout
		box.add(lChampsObligatoires);

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
	private void sortAndRefreshList() 
	{
		Contact[] tmp = contactListData.toArray( new Contact[0] );
		
		// sort() appelle la méthode compareTo() de la classe Contact
		Arrays.sort( tmp );
		
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

	/**
	 * Méthode qui associe une image à un contact selectionné dans la liste.
	 * Après l'ajout de la photo, sérialization de la liste de contact.
	 * @param f Fichier ou se trouve l'image
	 * @param c Contact auquel on souhaite ajouter une photo
	 */
	protected void associate (File f, Contact c)
	{
		if(contactListData.contains(c)) //chercher le contact c dans ma liste (contains(c), retourn un boolean)
		{
			//si trouvé, stocker ImageFile dans contact + sérialisation
			
			// Association de l'image au contact
			c.setImageFile( f );
			
			// Sérialization
			Utils.serializeObjects( this.getPhone().getContactFile(), contactListData);
		}
	}
	
	/**
	 * Ajout d'un contact dans l'arraylist et sérialization de l'object
	 * @param phone numéro de téléphone
	 * @param firstname prénom du contact
	 * @param name nom de famille du contact
	 * @param email email du contact
	 */
	private void addContact(String phone, String firstname, String name, String email) 
	{
		Contact newContact = new Contact (phone, firstname, name, email);
		contactListData.add(newContact);
		Utils.serializeObjects( this.getPhone().getContactFile(), contactListData);
		
	}
	
	/**
	 *Effacer un contact dans l'arraylist + sérialization
	 */
	private void removeContact(Contact contactSelected)
	{
		contactListData.remove(contactSelected);
		Utils.serializeObjects( this.getPhone().getContactFile(), contactListData);
		
	}
	
	/**
	 * Retourne l'arrayList de contact
	 * @return arraylist de contact
	 */
	public ArrayList<Contact> getContactlist()
	{
		return contactListData;
	}

	/**
	 * Permet de modifier l'arraylist de contact
	 * @param contactListData liste de contact
	 */
	public void setContactlist(ArrayList<Contact> contactListData)
	{
		this.contactListData = contactListData;
		sortAndRefreshList();
	}
	
	/**
	 * Cette classe contient tous les ActionListener des boutons de l'application Contact ainsi que chaque action des boutons
	 * @author Aurélie Glassey
	 */
	private class ListenerContact implements ActionListener 
	{
		public void actionPerformed(ActionEvent e)
		{	
			if(e.getSource()==bAddContact)
			{
				panelAddContact = generatePanel( null, bCancel, bSaveContact, lTitreAdd );
				pushPanel(panelAddContact);
				sortAndRefreshList();
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
					JOptionPane.showMessageDialog(null, "Données incomplètes à l'ajout : Manque name, firstname ou phone");
				}
				else {
					addContact(tPhoneNumber.getText(), tFirstName.getText(), tName.getText(), tEmail.getText() );
					panelAddContact = null;
					popPanel();
					sortAndRefreshList();
				}	
			}
			
			if (e.getSource()==bRemove)
			{
				JOptionPane optionPane = new JOptionPane();
				
				int ret = optionPane.showConfirmDialog(null, "Delete contact ?", "", JOptionPane.CANCEL_OPTION);
			
				if (ret == JOptionPane.YES_OPTION){
					removeContact(contactList.getSelectedValue());
					panelModifyContact = null;
					popPanel();
					
					sortAndRefreshList();
				}
			}
			
			if (e.getSource()==bModify)
			{
				if(tName.getText().equals("") && tFirstName.getText().equals("") || tPhoneNumber.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "Données incomplètes à la modification : Manque name, firstname ou phone");
				}
				else {
					//suppression du contact sélectionner et ajout du contact modifier
					lTitreModif.setBackground(new Color(250, 250, 250));
					removeContact(contactList.getSelectedValue());
					addContact(tPhoneNumber.getText(), tFirstName.getText(), tName.getText(), tEmail.getText());
					panelModifyContact = null;
					popPanel();
					sortAndRefreshList();
				}
			}
		}	
	}
	
	/**
	 * Cette classe est nécessaire pour la selection d'un objet dans la JList + double clique nécessaire sur la liste pour qu'elle ouvre le contact
	 * @author Aurélie Glassey
	 *
	 */
	private class DoubleClickListener extends MouseAdapter
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
