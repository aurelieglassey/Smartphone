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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

//regarder comment récupérer le clique sur la bonne colonne de l'arraylist getselectedrow ou je sais plus
public class ContactApp extends AbstractApp
{
	private Contact[] listContact;
	protected JList<Contact> list;
	
	JButton temporaire = new JButton ("Frame temporaire");
	JButton bAddContact = new JButton ("Add contact");
	JButton bSaveContact = new JButton ("Save");
	JButton bCancel = new JButton ("Cancel"); 
	JButton bRemove = new JButton ("Remove contact");
	JButton bModify = new JButton ("Modify contact");
	
	JPanel panelnorth = new JPanel();
	JPanel panellist = new JPanel();
	JPanel panelAddContact;
	
	JLabel lname = new JLabel("Name");
	JLabel lFirstname = new JLabel("Firstname");
	JLabel lemail = new JLabel("email");
	JLabel lPhoneNumber = new JLabel("Phone number");
	JLabel ltitreaAdd = new JLabel ("Add a Contact");
	
	JTextField textname = new JTextField();
	JTextField textfirstname = new JTextField();
	JTextField textemail = new JTextField();
	JTextField textphonenumber = new JTextField();
	
	Contact contactSelected ;
	Contact newcontact;
	JButton bModifyContact = new JButton ("Modifiy contact");
	JPanel panelAddContactCenter;
	GridLayout gridlayout;
	
	/*************************** Constructeur ContactApp ***************************/

	public ContactApp( Smartphone phone )
	{
		super( phone, "Contact app", "contact" );
		
		this.mainPanel.setLayout(new BorderLayout());
		
		this.mainPanel.add(temporaire);
		temporaire.addActionListener(new ListenerContact());
		
		//Ajour temporaire de contact dans l'arraylist
		Contact c = new Contact("Glassey ", "Aurélie ", "@@@", "079");
		Contact c1 = new Contact("Ducrey ", "Cécile ", "@@@", "070");
		ContactRepertory.contactlist.add(c);
		ContactRepertory.contactlist.add(c1);
		
		list = new JList(ContactRepertory.contactlist.toArray());
		
		
	}
	
	public JPanel generateMainPanel()
	{
		return new JPanel();
	}
	
	/*************************** Fenêtre de l'application temporaire Contact avec la liste et le bouton d'ajout ***************************/

	public class FrameTempContact extends JFrame
	{
		public FrameTempContact ()
		{
			setVisible(true);
			setPreferredSize(new Dimension(480, 800));
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			
			ContactRepertory.deserializeContact();
			
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
	
	
	
	/*************************** Listener des boutons ***************************/
	
	class ListenerContact implements ActionListener //création d'une fenêtre temporaire après contactApp
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource()==temporaire){
				FrameTempContact f= new FrameTempContact();
				f.setVisible(true);
				
			}
			
			if(e.getSource()==bAddContact)
			{
				//appel d'un nouveau panel avec la méthode à fab
				//panelfab
				JPanel panelfab = new JPanel();
				panelfab.setLayout(new BorderLayout());
				//add(panelfab);
				
				JPanel panelTitre = new JPanel();
				panelTitre.add(ltitreaAdd);
				
				JPanel panelBox = new JPanel (); //Pour les données à remplir d'un contact
				panelBox.setPreferredSize(new Dimension(480, 400));
				
				
				panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));

				panelBox.add(lname);
				panelBox.add(textname);
			
				panelBox.add(lFirstname);
				panelBox.add(textfirstname);
				
				panelBox.add(lemail);
				panelBox.add(textemail);
				
				panelBox.add(lPhoneNumber);
				panelBox.add(textphonenumber);
				
				
				JPanel panelSouthButton = new JPanel(); //pour les boutons cancel et save
				panelSouthButton.setPreferredSize(new Dimension(480, 50));
				panelSouthButton.setLayout(new GridLayout(1, 2));
				panelSouthButton.add(bCancel);
				panelSouthButton.add(bSaveContact);
				
				
				panelfab.add(panelTitre, BorderLayout.NORTH);
				panelfab.add(panelBox, BorderLayout.CENTER);
				panelfab.add(panelSouthButton, BorderLayout.SOUTH);				
				
				if (e.getSource()==bModify){
				}
				//pack();				
			}
			
			if (e.getSource()==bCancel)
			{
				//retour au panel précédent avec AddContact
			}
			
			if (e.getSource()==bSaveContact)
			{
				
				ContactRepertory.addContact(textname.getText(), textfirstname.getText(), textemail.getText(), textphonenumber.getText());
			}
			
			if (e.getSource()==list.getSelectedValue())
			{
				contactSelected = (Contact) list.getSelectedValue();
				ModifyContact m = new ModifyContact (contactSelected);
				
				
			}

			if (e.getSource()==bRemove)
			{
				ContactRepertory.removeContact(contactSelected);

			}
			
			if (e.getSource()==bModifyContact)
			{
				ContactRepertory.removeContact(contactSelected);
				ContactRepertory.addContact(textname.getText(), textfirstname.getText(), textemail.getText(), textphonenumber.getText());
			}
		}
	}

	public void returnPressed()
	{
		// TODO Auto-generated method stub
		
	}
}