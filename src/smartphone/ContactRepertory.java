package smartphone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JLabel;



public class ContactRepertory
{

	private static ArrayList<Contact> contactlist = new ArrayList<Contact>();
	private static Contact newcontact;
	
	//Méthode qui ajoute un contact dans l'arraylist et sérialize l'object
	public static void addContact(String name, String fristname, String mail, String phone) 
	{
		if(name != ""){
			System.out.println("rien");
			//ajout du contact dans l'arraylist et sauvegarde de l'arraylist ou le contact a été ajouté
			newcontact = new Contact (name, fristname, mail, phone);
			contactlist.add(newcontact);
			
			Utils.serializeObjects(new File (".\\Contactlist.ser"), contactlist);
		}
	}
	
	//Méthode qui efface un contact
	public static void removeContact(Contact contactSelected)
	{
		contactlist.remove(contactSelected);
		Utils.serializeObjects(new File (".\\Contactlist.ser"), contactlist);
		
	}
	
	/**
	 * Getters et setters de l'ArrayList
	 * @return
	 */
	public static ArrayList<Contact> getContactlist()
	{
		return contactlist;
	}

	public static void setContactlist(ArrayList<Contact> contactlist)
	{
		ContactRepertory.contactlist = contactlist;
	}
	
	
}
