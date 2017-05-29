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

	static ArrayList<Contact> contactlist = new ArrayList<Contact>();
	
		
	static Contact newcontact;
	
	
	
	public static void addContact(String name, String fristname, String mail, String phone) 
	//ajout du contact dans l'arraylist et sauvegarde de l'arraylist ou le contact à été ajouté
	{
		newcontact = new Contact (name, fristname, mail, phone);
		contactlist.add(newcontact);
		serializeContact(); //sérialization de l'arraylist entière avec le nouveau contact
	}
	
	public static void serializeContact() //en paramètre ArrayList<Contact> contactlist
	{
		addContact("Essai", "dans", "méthode", "de/serializeContact");
		addContact("Essai", "dans", "méthode", "de/serializeContact");
		try
		{
			System.out.println("je suis dans la méthode sérializeConact - j'écris le contact");
			FileOutputStream fos = new FileOutputStream("Contactlist.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(contactlist);
			oos.close(); 
		} 
		
		catch (IOException e) //si fichier inaccessible, on lève l'exception
		{
			e.printStackTrace();
		}
	} 
	
	public static void deserializeContact() //en paramètre ArrayList<Contact> contactlist
	{
		try
		{
			System.out.println("je suis dans la méthode deserializeConact - je lis le contact");
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



	
	





