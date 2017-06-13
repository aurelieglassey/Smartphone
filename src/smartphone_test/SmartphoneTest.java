package smartphone_test;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Test;
//Inspiré de : http://www.java2s.com/Code/JavaAPI/junit.framework/AssertassertEqualsStringexpectedStringactual.htm

import smartphone.Contact;

/**
 * SmartPhoneTest est une classe créée pour tester les JUnit
 * @author Aurélie Glassey
 *
 */
public class SmartphoneTest
{

	/**
	 * Méthode pour tester les paramètres d'entrée s'il sont null ou non lors de la création d'un Contact
	 * Test : si une exception est quand même appelée alors que tous les paramètres obligatoires sont rentrés
	 */
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
	
	/**
	 * Méthode pour tester le paramètre du numéro de téléphone d'un contact
	 * Le numéro de téléphone n'est pas obligatoire. 
	 * Test : si une exception est appelée lorsqu'un numéro de téléphone n'est pas rempli
	 */
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
