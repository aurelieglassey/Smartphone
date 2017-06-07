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

	protected static ArrayList<Contact> contactlist = new ArrayList<Contact>();
	private static Contact newcontact;
	
	
	/*************************** Méthodes de ContactRepertory ***************************/

	//Méthode qui ajoute un contact
	public static void addContact(String name, String fristname, String mail, String phone) 
	{
		newcontact = new Contact (name, fristname, mail, phone);
		contactlist.add(newcontact);
		System.out.println(newcontact);
		Utils.serializeObjects(new File (".\\Contactlist.ser"), contactlist);
	}
	
	
	public static void removeContact(Contact contactSelected)
	{
		contactlist.remove(contactSelected);
		Utils.serializeObjects(new File (".\\Contactlist.ser"), contactlist);
		
	}
	
	
	
}

