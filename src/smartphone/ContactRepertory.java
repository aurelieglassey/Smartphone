package smartphone;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;



public class ContactRepertory
{

	static ArrayList<Contact> contactlist = new ArrayList<Contact>();
	
		
	Contact newcontact;
	
	
	
	public void addContact(String name, String fristname, String mail, String phone) 
	//ajout du contact dans l'arraylist et sauvegarde de l'arraylist ou le contact à été ajouté
	{
		newcontact = new Contact (name, fristname, mail, phone);
		this.contactlist.add(newcontact);
		//serializeContact(this.contactlist);
		//ajout d'un label contact enregistré 
	}
	
	
	
}



	
	





