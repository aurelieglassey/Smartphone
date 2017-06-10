package smartphone;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;

public class JUnitContact
{

	@Test
	public void nullConstructor()
	{		
		try
		{
			Contact c = new Contact ("Auré", "Glassey", "", "079 101 49 18");
		}
		
		catch (IllegalArgumentException expected)
		{
			fail("Exception générée alors que les données sont valides");
		}
	}
	
	@Test
	public void noPhoneNumber()
	{		
		try
		{
			Contact c = new Contact ("Auré", "Glassey", "", "");
			fail("Aucune exception générée alors qu'il n'y a aucun numéro de téléphone");
		}
		
		catch (IllegalArgumentException expected)
		{
			
		}
	}
}
