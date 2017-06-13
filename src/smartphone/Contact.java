package smartphone;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Cette classe contient toutes les données nécessaires pour un contact
 * la class Contact est sérializable car lors de l'enregistrement d'un contact, la liste est sérializée
 * @author Aurélie Glassey
 */
public class Contact implements Serializable, Comparable<Contact>
{
	private static final long serialVersionUID = 1L;
	
	//Paramètres pour l'entregistrement d'un contact
	private File imageFile;
	private String name;
	private String firstname;
	private String email;
	private String phone;

	/**
	 * Création du contact avec les 2 paramètres minimums
	 * @param firstname prénom du contact
	 * @param phone numéro de téléphone du contact
	 */
	public Contact (String phone, String firstname)
	{
		this( "", firstname, "", phone );
	}
	
	/**
	 * Création du contact avec 3 paramètres 
	 * @param name nom du contact
	 * @param firstname prénom du contact
	 * @param phone numéro de téléphone du contact
	 */
	public Contact ( String phone, String firstname, String name )
	{
		this( name, firstname, "", phone );
	}

	/**
	 * Création du contact
	 * @param name nom du contact
	 * @param firstname prénom du contact
	 * @param email email du contact
	 * @param phone numéro de téléphone
	 */
	public Contact (String phone, String firstname, String name, String email )
	{
		if((name.equals("") && firstname.equals("")) || phone.equals(""))
		{
			throw new IllegalArgumentException("Données insuffisantes (au moins un numéro et un nom/prénom requis)");
		}

		this.name = name; 
		this.firstname=firstname;
		this.email = email;
		this.phone = phone;
	}
	
	/**
	 * Retourne le nom de famille du contact
	 * @return nom de famille du contact
	 */
	public String getName()
	{
		return name;
	}
	/**
	 * Permet de modifier le nom de famille du contact
	 * @param name nom de famille
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Retourne le prénom du contact
	 * @return le prénom du contact
	 */
	public String getFirstname()
	{
		return firstname;
	}
	
	/**
	 * Permet de modifier le prénom du contact
	 * @param firstname prénom du contact
	 */
	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}
	
	/**
	 * Retourne l'email du contact
	 * @return email du contact
	 */
	public String getEmail()
	{
		return email;
	}
	
	/**
	 * Permet de modifier l'email du contact
	 * @param email email du contact
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	/**
	 * Retourne le numéro de téléphone du contact
	 * @return le numéro de téléphone du contact, ne peut pas être null
	 */
	public String getPhone()
	{
		return phone;
	}
	
	/**
	 * Permet de modifier le numéro de téléphone
	 * @param phone numéro de téléphone du contact, ne peut pas être null
	 */
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	/**
	 * Retourne l'image du contact si il y en a une
	 * @return l'image du contact, si il y en a une
	 */
	public File getImageFile()
	{
		return imageFile;
	}

	/**
	 * Permet de changer une image dans le contact
	 * @param imageFile image du contact, si il y en a une
	 */
	public void setImageFile(File imageFile)
	{
		this.imageFile = imageFile;
	}
	
	/**
	 * Affichage des données d'un contact
	 */
	public String toString ()
	{
		String str = firstname;
		
		if ( !name.isEmpty() )
		{
			//si la chaîne n'est pas vide, on ajoute un espace pour séparer le nom et le prénom
			if ( !str.isEmpty() ) str += " ";
			str += name;
		}
		
		return str;
	}
	
	/**
	 * Compare un contact à un autre en les convertissant en chaînes
	 * puis en les comparant avec la méthode compareTo() de la classe
	 * String.
	 */
	public int compareTo( Contact c )
	{
		return toString().compareTo( c.toString() );
	}
	
}
