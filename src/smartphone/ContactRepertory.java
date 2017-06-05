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
	
		
	protected static Contact newcontact;
	
	
	/*************************** M�thodes de ContactRepertory ***************************/

	public static void addContact(String name, String fristname, String mail, String phone) 
	//ajout du contact dans l'arraylist et sauvegarde de l'arraylist ou le contact � �t� ajout�
	{
		newcontact = new Contact (name, fristname, mail, phone);
		contactlist.add(newcontact);
		serializeContact(); //s�rialization de l'arraylist enti�re avec le nouveau contact
	}
	
	
	public static void removeContact(Contact contactSelected)
	{
		contactlist.remove(contactSelected);
	}
	
	
	
	
	
	
	
	
	
	
	
	/*************************** S�rialization et d�serialization de l'arraylist ***************************/

	public static void serializeContact(File f) //en param�tre ArrayList<Contact> contactlist
	{
		addContact("Essai", "dans", "m�thode", "de/serializeContact");
		addContact("Essai", "dans", "m�thode", "de/serializeContact");
		try
		{
			System.out.println("je suis dans la m�thode s�rializeConact - j'�cris la liste");
			FileOutputStream fos = new FileOutputStream(f);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(contactlist);
			oos.close(); 
		} 
		
		catch (IOException e) //si fichier inaccessible, on l�ve l'exception
		{
			e.printStackTrace();
		}
	} 
	
	public static void deserializeContact() //en param�tre ArrayList<Contact> contactlist
	{
		try
		{
			System.out.println("je suis dans la m�thode deserializeConact - je lis la liste");
			FileInputStream fis = new FileInputStream("Contactlist.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			ArrayList <Contact> contaclist= (ArrayList <Contact>)ois.readObject();
			System.out.println(contaclist);
		} 
		
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	} 
	
}

