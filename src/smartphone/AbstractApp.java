package smartphone;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Cette classe représente une application du smartphone. Les applications étendent
 * cette classe pour fournir leur propre implémentation. Chaque application maintient
 * un lien avec le smartphone correspondant et fournit des méthodes pour accéder
 * à des données telles que le nom de l'application ou le bouton à afficher sur
 * l'écran d'accueil. Chaque application reçoit un JPanel racine (mainPanel) pour pouvoir
 * s'afficher. D'autres JPanel peuvent être fournis au smartphone pour affichage (pushPanel)
 * ou retirés de la pile (popPanel).
 * @author Fabien Terrani
 */
public abstract class AbstractApp implements Serializable
{
	/**
	 * Le smartphone sur lequel l'application est installée.
	 */
	private Smartphone phone;
	
	/**
	 * Le nom de l'application (éventuellement affiché à l'utilisateur).
	 */
	private String name;
	
	/**
	 * Le nom de l'application pour les besoins techniques. Composé uniquement
	 * des caractères a-z minuscules et du underscore_.
	 */
	private String label;
	
	/**
	 * Le JPanel racine de l'application.
	 */
	private JPanel mainPanel;
	
	/**
	 * Le bouton à afficher sur l'écran d'accueil pour démarrer l'application.
	 */
	private JButton button;
	
	/**
	 * Un booléen informant sur l'état de l'application. TRUE si l'application
	 * est démarrée et si son JPanel racine existe dans le smartphone, FALSE sinon.
	 */
	private boolean started = false;
	
	/**
	 * Construit une nouvelle application smartphone.
	 * @param phone Le smartphone sur lequel l'application est installée
	 * @param appName Le nom technique de l'application
	 * @param appLabel Le nom d'affichage de l'application
	 */
	public AbstractApp( Smartphone phone, String appName, String appLabel )
	{
		this.phone = phone;
		this.name = appName;
		this.label = appLabel;
		
		this.mainPanel = generateMainPanel();
		
		
		
		//this.mainScrollPane = new JScrollPane( this.mainPanel );
		//this.mainScrollPane.getVerticalScrollBar().setPreferredSize( new Dimension(this.phone.getScrollBarWidth(), 0) );
		//this.scrollPane.setBorder( null );
		
		Image appImage = this.phone.getAppImage( this );
		
		this.button = new ImageButton( appImage, 100, 100 );
	}
	
	/**
	 * Permet à une application de retourner sa propre version d'un JPanel
	 * pour lui permettre d'avoir un contrôle sur le processus de painting.
	 * @return Un objet de classe JPanel ou d'une classe étendant JPanel
	 */
	public JPanel generateMainPanel()
	{
		return new JPanel();
	}
	
	/**
	 * Méthode appelée lorsqu'un clic a été détecté sur le bouton Retour.
	 */
	public abstract void returnPressed();
	
	/**
	 * Retourne le téléphone de l'application.
	 * @return Un objet Smartphone représentant le téléphone sur lequel
	 * l'application est installée.
	 */
	public Smartphone getPhone()
	{
		return this.phone;
	}
	
	/**
	 * Retourne le panel racine de l'application.
	 * @return Un objet JPanel représentant l'espace d'affichage racine de
	 * l'application.
	 */
	public JPanel getMainPanel()
	{
		return this.mainPanel;
	}
	
	/**
	 * Retourne le bouton de l'application.
	 * @return Un objet JButton à afficher pour démarrer l'application.
	 */
	public JButton getButton()
	{
		return this.button;
	}
	
	/**
	 * Retourne le nom d'affichage de l'application.
	 * @return Une chaîne de caractères contenant le nom de l'application
	 * et prévue pour être affichée à l'utilisateur.
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * Retourne le nom technique de l'application.
	 * @return Une chaîne de caractères contenant le nom de l'application
	 * et prévue pour un usage technique.
	 */
	public String getLabel()
	{
		return this.label;
	}
	
	/**
	 * Démarre l'application et envoie l'espace d'affichage racine au smartphone.
	 */
	public void startApp()
	{
		started = true;
		pushPanel( this.getMainPanel() );
	}
	
	/**
	 * Envoie un nouveau panel au smartphone à afficher par-dessus les autres.
	 * @param panel Un objet JPanel à afficher
	 */
	public void pushPanel( JPanel panel )
	{
		this.phone.pushAppPanel( this, panel );
	}
	
	/**
	 * Retire le dernier panel affiché pour l'application par le smartphone
	 * et le retourne.
	 * @return Le dernier panel affiché
	 */
	public JPanel popPanel()
	{
		return this.phone.popAppPanel( this );
	}
}
