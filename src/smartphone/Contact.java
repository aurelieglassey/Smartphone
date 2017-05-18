package smartphone;

public class Contact
{

	//private Photo photocontact;
	private String name;
	private String firstname;
	private String address;
	private String phone;
	
	Contact (String name, String firstname, String addrSess, String phone){
		this.name = name; 
		this.firstname=firstname;
		this.address = address;
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
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
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
	
	
	
	
	
	
}
