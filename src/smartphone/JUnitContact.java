package smartphone;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
//Insipir� de : http://www.java2s.com/Code/JavaAPI/junit.framework/AssertassertEqualsStringexpectedStringactual.htm

public class JUnitContact
{

	/**
	 * M�thdes de test avec JUnit pour tester les param�tres d'entr�es d'un Contact
	 */
	@Test
	public void nullConstructor()
	{		
		try
		{
			Contact c = new Contact ("Aur�", "Glassey", "", "079 101 49 18");
		}
		
		catch (IllegalArgumentException expected)
		{
			fail("Exception g�n�r�e alors que les donn�es sont valides");
		}
	}
	
	@Test
	public void noPhoneNumber()
	{		
		try
		{
			Contact c = new Contact ("Aur�", "Glassey", "", "");
			fail("Aucune exception g�n�r�e alors qu'il n'y a aucun num�ro de t�l�phone");
		}
		
		catch (IllegalArgumentException expected)
		{
			
		}
	}
}
