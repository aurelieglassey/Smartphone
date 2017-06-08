package smartphone;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;


public class Contact implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	//private Photo photocontact;
	private String name;
	private String firstname;
	private String email;
	private String phone;
	
	/**
	 * Constructeur de la classe Contact avec en paramètre le nom, prénom, email et téléphone
	 * @param name
	 * @param firstname
	 * @param email
	 * @param phone
	 */
	public Contact (String name, String firstname, String email, String phone){
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
	public String getemail()
	{
		return email;
	}
	public void setemail(String address)
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
		
	public String toString ()
	{
		return this.name + " " + this.firstname + " " +this.email + " " + this.phone;
	}
	
}
