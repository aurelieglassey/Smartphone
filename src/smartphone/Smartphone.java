package smartphone;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * Cette classe représente un smartphone. Chaque instance de la classe contient
 * un écran d'accueil et un ensemble d'applications.
 * @author Fabien Terrani
 */
public class Smartphone extends JFrame implements ActionListener
{
	/**
	 * Nom de la carte contenant le homescreen
	 */
	private Card homeCard;
	
	/**
	 * Nom de la carte affichée
	 */
	private Card currentCard;
	
	/**
	 * CardLayout utilisé sur le panneau central
	 */
	private CardLayout cLayout = new CardLayout();
	
	private ArrayList<AbstractApp> apps = new ArrayList<>();
	/**
	 * Ensemble des piles de JPanel associés aux apps
	 */
	private HashMap< AbstractApp, ArrayList<Card> > appPanels = new HashMap<>();
	
	/**
	 * Liste de toutes les cartes affichables
	 */
	private ArrayList<Card> cards = new ArrayList<>();
	
	/**
	 * panneau central (écran du smartphone)
	 */
	private JPanel panelcenter = new JPanel();
	
	/**
	 * Panneau sud (boutons de contrôle en-dessous de l'écran)
	 */
	private JPanel panelsouth = new JPanel();
	
	
	/**
	 * Dimensions de l'écran du smartphone en pixels
	 */
	private Dimension screenSize = new Dimension( 480, 800 );
	
	/**
	 * Dimensions des boutons d'applications en pixels.
	 */
	private Dimension appButtonSize = new Dimension( 100, 100 );
	
	/**
	 * Couleur d'arrière-plan du smartphone.
	 */
	private static final Color bgColor = new Color(30,30,30,255);
	
	/**
	 * Police de caractère du smartphone (petite)
	 */
	private static final Font smallFont = new Font( "Raleway", Font.PLAIN, 16 );

	/**
	 * Police de caractère du smartphone (moyenne)
	 */
	private static final Font mediumFont = new Font( "Raleway", Font.PLAIN, 24 );

	/**
	 * Police de caractère du smartphone (grande)
	 */
	private static final Font largeFont = new Font( "Raleway", Font.PLAIN, 32 );
	
	/**
	 * Bouton d'affichage des apps actuellement démarrées (pas encore implémenté)
	 */
	private JButton btnApps;
	
	/**
	 * Bouton Home permettant de revenir à l'écran d'accueil.
	 */
	private JButton btnHome;
	
	/**
	 * Bouton Retour permettant de revenir en arrière dans les menus/écrans.
	 */
	private JButton btnReturn;
	
	/**
	 * Répertoire racine du smartphone.
	 */
	private File rootFolder;
	
	/**
	 * Répertoire des ressources système.
	 */
	private File sysFolder;
	
	/**
	 * Répertoire des applications.
	 */
	private File appsFolder;
	
	/**
	 * Répertoire de stockage des données utilisateur.
	 */
	private File storageFolder;
	
	/**
	 * Répertoire des images de l'utilisateur.
	 */
	private File imageFolder;
	
	/**
	 * Répertoire des sons de l'utilisateur.
	 */
	private File soundFolder;
	
	/**
	 * Fichier contenant la liste des contacts de l'utilisateur.
	 */
	private File contactFile;
	
	/**
	 * Largeur des scrollbars verticales du smartphone en pixels
	 */
	private static int scrollbarThickness = 30;
	
	/**
	 * Crée un nouveau smartphone.
	 */
	public Smartphone ()
	{
		prepareFolders();
		
		btnApps = new SmartButton(new ImageIcon( new File(sysFolder, "pictogram_apps.png").toString() )); //Apps
		btnHome = new SmartButton(new ImageIcon( new File(sysFolder, "pictogram_home.png").toString() )); //Home
		btnReturn = new SmartButton(new ImageIcon( new File(sysFolder, "pictogram_return.png").toString() )); //Return
		
		JButton[] btns = {btnApps, btnHome, btnReturn};
		
		for (JButton b : btns)
		{
			b.setBackground( bgColor );
			b.setBorder( BorderFactory.createLineBorder(bgColor.brighter().brighter().brighter(), 1) );
			b.addActionListener( this );
		}
		
		//Panel du centre avec dimensions
		panelcenter.setPreferredSize( this.screenSize );
		panelcenter.setLayout( cLayout );
		
		// Ajout des applications au téléphone
		this.registerApps();
		
		
		Set<AbstractApp> keys = appPanels.keySet();
		AbstractApp[] appsArray = new AbstractApp[0];
		appsArray = apps.toArray( appsArray );
				
		homeCard = new Card(
			Card.CARD_TYPE_HOME,
			"homescreen",
			0,
			new HomePanel( this, appsArray )
		);
		
		// Affichage des boutons des apps
		//this.showAppButtons();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Smartphone");
		this.setResizable(false);
		
		
		// On ajoute la carte du homescreen de base
		addCard( homeCard );
		showCard( homeCard );
		
		
		//Panel sud avec 3 boutons 
		panelsouth.setLayout( new GridLayout(1, 3) );
		
		panelsouth.add(btnApps);
		panelsouth.add(btnHome);
		panelsouth.add(btnReturn);
		
		// Ajout du panneau central au smartphone
		this.add(panelcenter, BorderLayout.CENTER);
		this.add(panelsouth, BorderLayout.SOUTH);
		
		this.pack();
		this.setLocationRelativeTo( null );
	}
	
	/**
	 * Prépare les répertoires nécessaires au smartphone pour fonctionner.
	 * Arrête le programme si les répertoires n'ont pas pu être créés.
	 */
	private void prepareFolders()
	{
		try
		{
			this.rootFolder = (new File(".", "smartphone_root"));
			this.sysFolder = new File( this.rootFolder, "sys" );
			this.appsFolder = new File( this.rootFolder, "apps" );
			this.storageFolder = new File( this.rootFolder, "storage" );
			this.imageFolder = new File( this.storageFolder, "images" );
			this.soundFolder = new File( this.storageFolder, "sounds" );
			
			this.contactFile = new File( this.storageFolder, "contactlist.ser" );

			File[] folders = {
				this.rootFolder,
				this.storageFolder,
				this.imageFolder,
				this.soundFolder
			};
			
			File[] files = {
					this.contactFile
				};
			
			// Création des répertoires nécessaires au smartphone s'ils n'existent pas
			for (File folder : folders)
			{
				if ( !folder.exists() ) folder.mkdirs();
			}
			
			// Création des fichiers nécessaires au smartphone s'ils n'existent pas
			for (File file : files)
			{
				if ( !file.exists() ) file.createNewFile();
			}
		}
		
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(
				null,
				"Impossible d'accéder au dossier racine du smartphone. Le programme va s'arrêter."
			);
			
			System.exit(0);
		}
	}
	
	/**
	 * Retourne la position du composant de la carte passée en paramètre dans
	 * le panneau central.
	 * @param card la carte contenant le composant dont on recherche la position
	 * @return L'index du composant
	 */
	private int getCardPosition( Card card )
	{
		Component[] c = panelcenter.getComponents();
		ArrayList<Component> c2 = new ArrayList<>( Arrays.asList(c) );
		
		return c2.indexOf( card.panel );
	}
	
	/**
	 * Ajoute une nouvelle carte
	 * @param card La carte à ajouter
	 * @param insertPos La position à laquelle ajouter le composant de la carte dans le panneau central
	 */
	private void addCard( Card card, int insertPos )
	{
		cards.add( card );
		panelcenter.add( card.panel, card.getCardName(), insertPos );
	}
	
	/**
	 * Ajoute une nouvelle carte à la pile de cartes du panneau central.
	 * @param card La carte à ajouter
	 */
	private void addCard( Card card )
	{
		int insertPos = -1;
		

		if ( card.type == Card.CARD_TYPE_HOME )
		{
			insertPos = 0;
		}
		
		if ( card.type == Card.CARD_TYPE_APP )
		{
			ArrayList<Card> appCards = appPanels.get( card.app );
			
			insertPos = getCardPosition( appCards.get( appCards.size()-1 ) ) + 1;
		}
		
		addCard( card, insertPos );
	}
	
	/**
	 * Retire la carte passée en paramètre des cartes du panneau central.
	 * @param card La carte à retirer
	 */
	private void removeCard( Card card )
	{
		cards.remove( card );
		
		panelcenter.remove(
			card.panel
		);
	}
	
	/**
	 * Affiche la carte passée en paramètre.
	 * @param cardName La carte à afficher
	 */
	private void showCard( Card card )
	{
		// La méthode show() de CardLayout ne nous dit pas si une carte avec le nom correspondant a été trouvée ou non...
		cLayout.show( panelcenter, card.getCardName() );
		
		// La valeur de currentCard n'est donc pas 100% fiable...
		currentCard = card;
	}
	
	/**
	 * Ajoute le panel fourni en paramètre à la liste des panels de l'application app.
	 * Cette méthode crée une nouvelle carte, l'ajoute au panneau central et l'affiche.
	 * @param app L'application pour laquelle on va ajouter le panel
	 * @param panel Le panel à ajouter sur la pile
	 */
	public void pushAppPanel( AbstractApp app, JPanel panel )
	{
		if ( app == null )
		{
			System.err.println( "pushAppPanel appelé sans application !" );
			return;
		}
		
		ArrayList<Card> panels = appPanels.get(app);
		
		Card c = new Card( Card.CARD_TYPE_APP, app, panels.size(), panel );
		
		// Ajout à la liste des panels de l'app
		panels.add( c );
		
		// Ajout du panel en tant que nouvelle carte
		addCard( c );
		
		showCard( c );
	}
	
	/**
	 * Retire le panel au sommet de la pile de l'application app et le retourne.
	 * @param app L'application dont on veut retirer le panel
	 * @return Le panel retiré
	 */
	public JPanel popAppPanel( AbstractApp app )
	{
		if ( app == null )
		{
			System.err.println( "popAppPanel appelé sans application !" );
			return null;
		}
		
		ArrayList<Card> panels = appPanels.get(app);
		
		if ( panels.size() <= 1 ) return null;
		
		// Récupération du dernier panel ajouté dans la liste des panels de l'app
		Card removed = panels.remove( panels.size()-1 );
		
		// Retrait de la carte correspondant au panel le plus haut
		removeCard( removed );
		
		if ( panels.size() > 0 )
		{
			showCard( panels.get( panels.size()-1 ) );
		}
		
		return removed.panel;
	}
	
	/**
	 * Affiche l'application passée en paramètre. (Recherche la carte au sommet
	 * de la pile de cartes de l'application et l'affiche.)
	 * @param app L'application à afficher
	 */
	public void showApp( AbstractApp app )
	{
		ArrayList<Card> panels = appPanels.get(app);
		
		showCard( panels.get( panels.size()-1 ) );
	}
	
	/**
	 * Affiche l'écran d'accueil
	 */
	private void showHome()
	{
		showCard( homeCard );
	}
	
	/**
	 * Retourne l'épaisseur des scrollbars du smartphone
	 * @return L'épaisseur des scrollbars en pixels
	 */
	public static int getScrollbarThickness()
	{
		return scrollbarThickness;
	}
	
	/**
	 * Retourne la couleur de fond du smartphone.
	 * @return la couleur de fond du smartphone
	 */
	public static Color getBackgroundColor()
	{
		return bgColor;
	}
	
	/**
	 * Retourne une des polices utilisées dans le smartphone selon la taille
	 * passée en paramètre.
	 * @param fontType La taille de la police ("small", "medium", "large")
	 * @return Un objet Font représentant la police demandée
	 */
	public static Font getSmartFont( String fontType )
	{
		Font f = null;
		
		switch ( fontType )
		{
			case "small": f = smallFont; break;
			case "medium": f = mediumFont; break;
			case "large": f = largeFont; break;
			default: f = mediumFont;
		}
		
		return f;
	}
	
	/**
	 * Installe les applications contact, galerie, musique et dummy sur
	 * le smartphone
	 */
	private void registerApps()
	{
		registerApp( new ContactApp( this ) );
		registerApp( new GalleryApp( this ) );
		registerApp( new MusicApp( this ) );
		registerApp( new DummyApp( this ) );
	}
	
	/**
	 * Ajoute l'application app au smartphone. Enregistre l'application
	 * dans la liste des apps, lui associe une pile de cartes et ajoute
	 * un listener au bouton de l'application.
	 * @param app L'application à installer
	 */
	private void registerApp( AbstractApp app )
	{
		apps.add( app );
		
		// Création d'une pile de cartes que l'application va pouvoir utiliser
		ArrayList<Card> panels = new ArrayList<Card>();
		this.appPanels.put(app, panels);
		
		// Récupération du bouton de l'application
		JButton appButton = app.getButton();
		appButton.addActionListener( this );
		//this.appButtons.add( appButton );
	}
	
	/**
	 * Retourne l'icône associée à l'application passée en paramètre
	 * @param app L'application dont on doit retourner l'icône
	 * @return Un objet Image représentant l'icône de l'application
	 */
	public Image getAppImage( AbstractApp app )
	{
		Image img = null;
		
		try
		{
			File appImage = new File( getAppsFolder(), app.getLabel() + ".png" );
			
			img = ImageIO.read( appImage );
			
			if ( img.getWidth(null) != 100 || img.getHeight(null) != 100 )
			{
				img = img.getScaledInstance(100, 100, Image.SCALE_FAST);
			}
		}
		catch (Exception e)
		{
			img = new BufferedImage( 100, 100, BufferedImage.TYPE_INT_ARGB );
			Graphics g = img.getGraphics();
			g.setColor( Color.WHITE );
			g.drawLine(0, 0, img.getWidth(null), img.getHeight(null));
			g.drawLine(0, img.getHeight(null), img.getWidth(null), 0);
		}
		
		return img;
	}
	
	/**
	 * Retourne l'instance d'application de la classe passée en paramètre
	 * @param classObj La classe de l'application à retourner
	 * @return L'instance de l'application demandée, en tant que AbstractApp
	 */
	public AbstractApp getAppInstance( Class classObj )
	{
		AbstractApp found = null;
		
		for (AbstractApp app : apps)
		{
			if ( app.getClass().isAssignableFrom(classObj) )
			{
				found = app;
				break;
			}
		}
		
		return found;
	}
	
	/**
	 * Gère l'interaction avec les trois boutons inférieurs du smartphone
	 * (apps, home, return).
	 */
	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnHome )
		{
			showHome();
		}
		
		else if ( e.getSource() == this.btnReturn )
		{
			if ( currentCard.type == Card.CARD_TYPE_APP && currentCard.app != null )
			{
				if ( appPanels.get( currentCard.app ).size() > 1 )
				{
					currentCard.app.returnPressed();
				}
				else
					showHome();
			}
		}
		
		else
		{
			for ( AbstractApp app : this.apps )
			{
				if ( e.getSource() == app.getButton() )
				{
					if ( appPanels.get(app).size() == 0 )
					{
						app.startApp();
					}
					
					else
					{
						showApp( app );
					}
				}
			}
		}
	}
	
	/**
	 * Retourne le fichier d'arrière-plan de l'écran d'accueil
	 * @return Le fichier d'arrière-plan de l'écran d'accueil
	 */
	public File getBackgroundFile()
	{
		return new File( this.sysFolder, "background.png" );
	}
	
	/**
	 * Retourne le répertoire racine du smartphone
	 * @return Le répertoire racine du smartphone
	 */
	public File getRootFolder()
	{
		return this.rootFolder;
	}
	
	/**
	 * Retourne le répertoire système.
	 * @return Le répertoire système
	 */
	public File getSysFolder()
	{
		return this.sysFolder;
	}
	
	/**
	 * Retourne le répertoire des apps.
	 * @return Le répertoire des apps
	 */
	public File getAppsFolder()
	{
		return this.appsFolder;
	}
	
	/**
	 * Retourne le répertoire des images.
	 * @return Le répertoire des images
	 */
	public File getImageFolder()
	{
		return this.imageFolder;
	}
	
	/**
	 * Retourne le répertoire des sons.
	 * @return Le répertoire des sons
	 */
	public File getSoundFolder()
	{
		return this.soundFolder;
	}
	
	/**
	 * Retourne le fichier stockant la liste des contacts.
	 * @return Le fichier stockant la liste des contacts
	 */
	public File getContactFile()
	{
		return this.contactFile;
	}
	
	/**
	 * Retourne la dimension de l'écran du smartphone
	 * @return Un objet Dimension contenant la largeur et la hauteur de l'écran en pixels
	 */
	public Dimension getScreenSize()
	{
		return ((Dimension) this.screenSize.clone());
	}
	
	/**
	 * Retourne la dimension des boutons d'application
	 * @return Un objet Dimension contenant la largeur et la hauteur des boutons d'application
	 * en pixels
	 */
	public Dimension getAppButtonSize()
	{
		return ((Dimension) this.appButtonSize.clone());
	}
	
	/**
	 * Cette classe regroupe différentes informations pour modéliser et organiser les cartes
	 * de la classe Swing CardLayout.
	 * @author Fabien Terrani
	 */
	private class Card
	{
		/**
		 * Carte d'un type inconnu.
		 */
		public static final String CARD_TYPE_UNKNOWN = "UNK";
		
		/**
		 * Carte contenant un espace d'affichage pour une application du smartphone.
		 */
		public static final String CARD_TYPE_APP = "APP";
		
		/**
		 * Carte contenant l'écran d'accueil du smartphone.
		 */
		public static final String CARD_TYPE_HOME = "HOM";
		
		/**
		 * Type de la carte.
		 */
		private String type;
		
		/**
		 * Nom de la carte.
		 */
		private String name;
		
		/**
		 * Application liée à la carte. Utilisé uniquement si la carte est
		 * de type CARD_TYPE_APP ; contient null sinon.
		 */
		private AbstractApp app;
		
		/**
		 * Un identifiant numérique ajouté à la carte pour la différencier des cartes
		 * du même nom.
		 */
		private int position;
		
		/**
		 * Le JPanel contenu dans la carte.
		 */
		private JPanel panel;
		
		/**
		 * Crée une nouvelle carte.
		 * @param type Le type de la carte
		 * @param name Le nom de la carte
		 * @param position Un identifiant numérique pour différencier la carte de celles qui ont le même nom
		 * @param panel Le panel correspondant à la carte
		 */
		private Card( String type, String name, int position, JPanel panel )
		{
			this.type = type;
			this.app = null;
			this.name = name;
			this.position = position;
			this.panel = panel;
		}
		
		/**
		 * Crée une nouvelle carte.
		 * @param type Le type de la carte
		 * @param app L'application propriétaire de la carte
		 * @param position Le numéro de la carte dans la pile de cartes de l'application
		 * @param panel Le panel correspondant à la carte
		 */
		private Card( String type, AbstractApp app, int position, JPanel panel )
		{
			this.type = type;
			this.app = app;
			this.name = app.getLabel();
			this.position = position;
			this.panel = panel;
		}
		
		/**
		 * Retourne le nom de la carte.
		 * @return Une chaîne suivant le format "[type]_[name]_[position]"
		 */
		public String getCardName()
		{
			return type + "_" + name + "_" + position;
		}
		
		/**
		 * Retourne le nom de la carte.
		 */
		public String toString()
		{
			return getCardName();
		}
	}
}
