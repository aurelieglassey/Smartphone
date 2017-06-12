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


/**
 * ContactRepertory contient l'arraylist pour regrouper les contacts.
 * Elle contient les méthodes d'ajout et de suppression des contacts dans l'arraylist.
 * @author Aurélie
 *
 */
public class ContactRepertory
{

	/**
	 * Contient la liste de contact 
	 */
	private static ArrayList<Contact> contactlist = new ArrayList<Contact>();
	
	/**
	 * Variable qui concerve le contact qui vient d'être fait
	 */
	private static Contact newcontact;
	
	/**
	 * Ajout d'un contact dans l'arraylist et sérialization de l'object
	 * @param phone
	 * @param fristname
	 * @param name
	 * @param email
	 */
	protected static void addContact(String phone, String fristname, String name, String email) 
	{
		newcontact = new Contact (phone, fristname, name, email);
		contactlist.add(newcontact);
		Utils.serializeObjects(new File (".\\Contactlist.ser"), contactlist);
		
	}
	
	/**
	 *Effacer un contact dasn l'arraylist + sérialization
	 */
	protected static void removeContact(Contact contactSelected)
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
