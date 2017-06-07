package smartphone;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//Manque image du contact
//commentaires
//refresh ne marche pas
//méthode de sérialization


public class ContactApp extends AbstractApp
{
	private Contact[] listContact;
	protected JList<Contact> jlist;

	private JButton bAddContact = new JButton ("Add contact");
	private JButton bSaveContact = new JButton ("Save");
	private JButton bCancel = new JButton ("Cancel"); 
	private JButton bRemove = new JButton ("Remove contact");
	private JButton bModify = new JButton ("Modify contact");

	private JPanel panelnorth = new JPanel();
	private JPanel panellist = new JPanel();
	private JPanel panelAddContact = null;
	private JPanel panelModifyContact = null;

	private JLabel lname = new JLabel("Name");
	private JLabel lFirstname = new JLabel("Firstname");
	private JLabel lemail = new JLabel("email");
	private JLabel lPhoneNumber = new JLabel("Phone number");
	private JLabel ltitreaAdd = new JLabel ("Add a Contact");
	private JLabel ltitreModif = new JLabel ("Modify a Contact");

	private JTextField textname = new JTextField();
	private JTextField textfirstname = new JTextField();
	private JTextField textemail = new JTextField();
	private JTextField textphonenumber = new JTextField();

	Contact contactSelected ;

	


	/*************************** Constructeur ContactApp ***************************/

	public ContactApp( Smartphone phone )
	{
		super( phone, "Contact app", "contact" );

		this.mainPanel.setLayout(new BorderLayout());
		
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
		System.out.println( obj.length );
		System.out.println( "Arraylist? " + (obj[0] instanceof ArrayList) );
		
		
		ContactRepertory.contactlist = ((ArrayList<Contact>) obj[0]);

		jlist = new JList(ContactRepertory.contactlist.toArray());
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.addListSelectionListener(new SelectionListener());

		panellist.add(jlist);
		
		panelnorth.add(bAddContact);
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
		panelTitre.add(titre);

		JPanel panelBox = new JPanel (); //Pour les données à remplir d'un contact
		panelBox.setPreferredSize(new Dimension(480, 400));

		panelBox.setLayout(new BoxLayout(panelBox, BoxLayout.Y_AXIS));

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
	
	
	/*************************** Listener des boutons ***************************/
	class ListenerContact implements ActionListener 
	{

		public void actionPerformed(ActionEvent e)
		{

			if(e.getSource()==bAddContact)
			{
				System.out.println("Ajout contact");
				panelAddContact = generatepanel( textname, textfirstname, textemail, textphonenumber, true, bCancel, bSaveContact, ltitreaAdd );
				pushPanel(panelAddContact);
			}

			if (e.getSource()==bCancel)
			{
				System.out.println("cancel");
				panelAddContact = null;
				popPanel();
			}

			if (e.getSource()==bSaveContact)
			{
				System.out.println("Save");
				ContactRepertory.addContact(textname.getText(), textfirstname.getText(), textemail.getText(), textphonenumber.getText());
				panelAddContact = null;
				popPanel();
			}
			
			if (e.getSource()==bRemove)
			{
				ContactRepertory.removeContact(jlist.getSelectedValue());
				panelModifyContact = null;
				popPanel();
			}
			
			if (e.getSource()==bModify)
			{
				//suppression du contact sélectionner et ajout du contact modifier
				ContactRepertory.removeContact(jlist.getSelectedValue());
				ContactRepertory.addContact(textname.getText(), textfirstname.getText(), textemail.getText(), textphonenumber.getText());
				panelModifyContact = null;
				popPanel();
			}
		}	
	}
	
	public class SelectionListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if(e.getSource()==jlist)
			{
				if(!jlist.getValueIsAdjusting()){
					jlist.getSelectedValue();
					contactSelected = (Contact) jlist.getSelectedValue();
					//Contact c = new Contact(contactSelected.getName(), contactSelected.getFirstname(), contactSelected.getMail(), contactSelected.getPhone());
					
					System.out.println("Contact selectionné :" +contactSelected);
					
					textname.setText(contactSelected.getName());
					textfirstname.setText(contactSelected.getFirstname());
					textemail.setText(contactSelected.getMail());
					textphonenumber.setText(contactSelected.getPhone());
					
					panelModifyContact = generatepanel( textname, textfirstname, textemail, textphonenumber, false, bRemove, bModify, ltitreModif );
					pushPanel(panelModifyContact);
				}
			}
		}
	}
	
	public void refreshPanel(JPanel p)
	{
		p.revalidate();
		p.repaint();
	}
	
}

	
	
