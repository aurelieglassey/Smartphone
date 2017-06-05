package smartphone;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;


public class Contact implements Serializable 
{

	
	//private Photo photocontact;
	private String name;
	private String firstname;
	private String email;
	private String phone;
	
	Contact (String name, String firstname, String email, String phone){
		this.name = name; 
		this.firstname=firstname;
		this.email = email;
		this.phone = phone;
	}
	
	
	//Getters and Setters
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
	public String getMail()
	{
		return mail;
	}
	public void setMail(String mail)
	{
		this.mail = mail;
	}
	private String mail;
	
	
	public String toString ()
	{
		return this.name + this.firstname +this.email + this.phone;
	}
	
	
	
	
}
