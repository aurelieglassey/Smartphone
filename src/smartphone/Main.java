package smartphone;

public class Main
{
	public static void main(String[] args)
	{
		Smartphone frame = new Smartphone();
		
		frame.setVisible( true );
		
		Contact c = new Contact("Aure", "Glassey", "@", "079");
		ContactApp.addContact(c);
		
		
	}
}
