package smartphone;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;


public class Contact implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	//imageFile file;
	private String name;
	private String firstname;
	private String email;
	private String phone;

	/**
	 * Création du contact avec les 2 paramètres minimums
	 * @param firstname
	 * @param phone
	 */
	public Contact (String phone, String firstname)
	{
		this( "", firstname, "", phone );
	}
	
	/**
	 * Création du contact avec 3 paramètres 
	 * @param name
	 * @param firstname
	 * @param phone
	 */
	public Contact ( String phone, String firstname, String name )
	{
		this( name, firstname, "", phone );
	}

	/**
	 * Création du contact
	 * @param name //nom du contact
	 * @param firstname //prénom du contact
	 * @param email //email du contact
	 * @param phone //numéro de téléphone
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
	 * Getters et setters des variables de la classe Contact
	 * @return
	 */
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getFirstname()
	{
		return firstname;
	}
	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String address)
	{
		this.email = email;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	
	/**
	 * Affichage des données d'un contact
	 */
	public String toString ()
	{
		return this.name + " " + this.firstname + " " +this.email + " " + this.phone;
	}
	
}
