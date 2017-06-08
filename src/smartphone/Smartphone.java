package smartphone;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.HashMap;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;


public class Smartphone extends JFrame implements ActionListener
{
	/**
	 * Nom de la carte contenant le homescreen
	 */
	private Card homeCard;
	
	private Card currentCard;
	
	/**
	 * CardLayout utilisé sur le panneau centrale
	 */
	private CardLayout cLayout = new CardLayout();
	
	/**
	 * Ensemble des piles de JPanel associés aux apps
	 */
	private HashMap< AbstractApp, ArrayList<Card> > appPanels = new HashMap<>();
	
	private ArrayList<Card> cards = new ArrayList<>();
	
	/**
	 * Panneau centrale (écran du smartphone)
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
	
	private JButton btnApps = new JButton (new ImageIcon("smartphone_root/sys/apps.png")); //Apps
	private JButton btnHome = new JButton (new ImageIcon("smartphone_root/sys/home.PNG")); //Home
	private JButton btnReturn = new JButton (new ImageIcon("smartphone_root/sys/return.PNG")); //Return
	
	private File rootFolder;
	private File imageFolder;
	private File soundFolder;
	
	/**
	 * Largeur des scrollbars verticales du smartphone en pixels
	 */
	private int scrollBarWidth = 20;
	
	// TODO retirer ceci...
	private ArrayList<JButton> appButtons = new ArrayList<>();
	
	public Smartphone ()
	{
		homeCard = new Card( Card.CARD_TYPE_HOME, "homescreen", 0, new JPanel() );
		
		try
		{
			this.rootFolder = (new File(".\\smartphone_root\\")).getCanonicalFile();
			this.imageFolder = new File( this.rootFolder, "images");
			this.soundFolder = new File( this.rootFolder, "sounds");

			System.out.println( this.rootFolder.exists() );
			System.out.println( this.imageFolder.exists() );
			System.out.println( this.soundFolder.exists() );
			
			if ( ! this.imageFolder.exists() ) this.imageFolder.mkdir();
			if ( ! this.soundFolder.exists() ) this.soundFolder.mkdir();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(
				null,
				"Impossible d'accéder au dossier racine du smartphone. Le programme va s'arrêter."
			);
		}
		
		//Panel du centre avec dimensions
		panelcenter.setPreferredSize( this.screenSize );
		panelcenter.setLayout( cLayout );
		
		// Ajout des applications au téléphone
		this.prepareApps();
		
		// Affichage des boutons des apps
		this.showAppButtons();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Smartphone");
		//this.setResizable(false); //désactiver les boutons de redimensionnement de la fenêtre
		
		
		// On ajoute la carte du homescreen de base
		addCard( homeCard );
		showCard( homeCard );
		
		
		//Panel sud avec 3 boutons 
		panelsouth.setLayout( new GridLayout(1, 3) );

		btnApps.addActionListener( this );
		btnHome.addActionListener( this );
		btnReturn.addActionListener( this );
		
		btnApps.setBackground(new Color(40, 40, 40, 255));
		btnHome.setBackground(new Color(40, 40, 40, 255));
		btnReturn.setBackground(new Color(40, 40, 40, 255));
		
		panelsouth.add(btnApps);
		panelsouth.add(btnHome);
		panelsouth.add(btnReturn);
		
		// Ajout du panneau centrale au smartphone
		this.add(panelcenter, BorderLayout.CENTER);
		this.add(panelsouth, BorderLayout.SOUTH);
		
		this.pack();
	}
	
	private int getCardPosition( Card card )
	{
		Component[] c = panelcenter.getComponents();
		ArrayList<Component> c2 = new ArrayList<>( Arrays.asList(c) );
		
		return c2.indexOf( card.panel );
	}
	
	/**
	 * Ajoute une nouvelle carte
	 * @param panel Le JPanel qui fera office de carte
	 * @param cardName Le nom de la carte à ajouter
	 */
	private void addCard( Card card, int insertPos )
	{
		cards.add( card );
		panelcenter.add( card.panel, card.getCardName(), insertPos );
	}
	
	/**
	 * Ajoute une nouvelle carte
	 * @param panel Le JPanel qui fera office de carte
	 * @param cardName Le nom de la carte à ajouter
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
	 * Retire la carte associée à panel
	 * @param panelLe JPanel correspondant à la carte à retirer
	 */
	private void removeCard( Card card )
	{
		cards.remove( card );
		
		panelcenter.remove(
			card.panel
		);
	}
	
	/**
	 * Affiche la carte cardName
	 * @param cardName Le nom de la carte à afficher
	 */
	private void showCard( Card card )
	{
		// La méthode show() de CardLayout ne nous dit pas si une carte avec le nom correspondant a été trouvée ou non...
		cLayout.show( panelcenter, card.getCardName() );
		
		// La valeur de currentCard n'est donc pas 100% fiable...
		currentCard = card;
	}
	
	/**
	 * Ajoute le panel fourni en paramètre à la liste des panels de l'application app
	 * @param app
	 * @param panel
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
	

	public void showApp( AbstractApp app )
	{
		ArrayList<Card> panels = appPanels.get(app);
		
		showCard( panels.get( panels.size()-1 ) );
	}
	
	private void showHome()
	{
		showCard( homeCard );
	}
	
	public int getScrollBarWidth()
	{
		return this.scrollBarWidth;
	}
	
	private void prepareApps()
	{
		addApp( new MusicApp( this ) );
		addApp( new ContactApp( this ) );
		addApp( new GalleryApp( this ) );
		addApp( new DummyApp( this ) );
	}

	/*private static String generateCardName( String cardType, String name )
	{
		return generateCardName( cardType, name, -1 );
	}
	
	private static String generateCardName( String cardType, String name, int index )
	{
		String newName = cardType + "_" + name;
		
		if (index >= 0) newName += "_" + index;
		
		return newName;
	}*/
	
	private void addApp( AbstractApp app )
	{
		// Ajout de l'application à la liste des apps installées
		//this.apps.add( app );
		
		ArrayList<Card> panels = new ArrayList<Card>();

		this.appPanels.put(app, panels);
		
		
		//JPanel mainPanel = app.getMainPanel();
		
		
		// Ajout du composant chargé du rendu de l'application aux cartes (CardLayout)
		//addCard(
		//	app.getMainPanel(),
		//	generateCardName( CARD_TYPE_APP, app.getName(), 0 )
		//);
		
		//panels.add( mainPanel );
		
		
		
		// Récupération du bouton de l'application
		JButton appButton = app.getButton();
		appButton.addActionListener( this );
		this.appButtons.add( appButton );
	}

	private void showAppButtons()
	{
		homeCard.panel.setLayout( new FlowLayout(FlowLayout.LEFT, 70, 70) );
		
		for ( JButton appButton : this.appButtons )
			homeCard.panel.add( appButton );
	}
	
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
			for ( AbstractApp app : this.appPanels.keySet() )
			{
				if ( e.getSource() == app.getButton() )
				{
					System.out.println( app.getName() );
					
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

	public File getRootFolder()
	{
		return this.rootFolder;
	}

	public File getImageFolder()
	{
		return this.imageFolder;
	}
	
	public File getSoundFolder()
	{
		return this.soundFolder;
	}
	
	public Dimension getScreenSize()
	{
		return this.screenSize;
	}
	
	private class Card
	{
		// Constantes pour identifier les types de cartes utilisés
		public static final String CARD_TYPE_UNKNOWN = "UNK";
		public static final String CARD_TYPE_APP = "APP";
		public static final String CARD_TYPE_HOME = "HOM";
		
		private String type;
		private String name;
		private AbstractApp app;
		private int position;
		private JPanel panel;

		private Card( String type, String name, int position, JPanel panel )
		{
			this.type = type;
			this.app = null;
			this.name = name;
			this.position = position;
			this.panel = panel;
		}
		
		private Card( String type, AbstractApp app, int position, JPanel panel )
		{
			this.type = type;
			this.app = app;
			this.name = app.getLabel();
			this.position = position;
			this.panel = panel;
		}
		
		public String getCardName()
		{
			return type + "_" + name + "_" + position;
		}
		
		public String toString()
		{
			return getCardName();
		}
	}
}
