package smartphone;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
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

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Smartphone extends JFrame implements ActionListener
{
	private JPanel panelcenter = new JPanel();
	private JPanel panelsouth = new JPanel();
	private JPanel homescreen = new JPanel();
	
	private Dimension screenSize = new Dimension( 480, 800 );
	
	private JButton btnApps = new JButton ("Apps");
	private JButton btnHome = new JButton ("Home");
	private JButton btnReturn = new JButton ("Return");

	private File rootFolder;
	private File imageFolder;
	private File soundFolder;
	
	private ArrayList<AbstractApp> apps = new ArrayList<>();
	private ArrayList<JButton> appButtons = new ArrayList<>();
	
	public Smartphone ()
	{
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
				"Impossible d'acc�der au dossier racine du smartphone. Le programme va s'arr�ter."
			);
		}
		
		//Panel du centre avec dimensions
		panelcenter.setPreferredSize( this.screenSize );
		panelcenter.setLayout( new CardLayout() );
		
		homescreen.setName( "Homescreen" );
		
		this.prepareApps();
		
		this.showAppButtons();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Smartphone");
		//this.setResizable(false); //d�sactiver les boutons de redimensionnement de la fen�tre
		
		
		
		panelcenter.add( homescreen, homescreen.getName() );
		((CardLayout) panelcenter.getLayout()).show( panelcenter, homescreen.getName() );
		this.add(panelcenter);
		
		
		//Panel sud avec 3 boutons 
		panelsouth.setLayout( new GridLayout(1, 3) );

		btnApps.addActionListener( this );
		btnHome.addActionListener( this );
		btnReturn.addActionListener( this );
		
		panelsouth.add(btnApps);
		panelsouth.add(btnHome);
		panelsouth.add(btnReturn);
		
		this.add(panelcenter, BorderLayout.CENTER);
		this.add(panelsouth, BorderLayout.SOUTH);
		
		this.pack();
	}

	private void prepareApps()
	{
		addApp( new MusicApp( this ) );
		addApp( new ContactApp( this ) );
		addApp( new GalleryApp( this ) );
	}
	
	private void addApp( AbstractApp app )
	{
		// Ajout de l'application � la liste des apps install�es
		this.apps.add( app );
		
		// Ajout du composant charg� du rendu de l'application aux cartes (CardLayout)
		panelcenter.add( app.getScrollPane(), app.getName() );
		
		// R�cup�ration du bouton de l'application
		JButton appButton = app.getButton();
		appButton.addActionListener( this );
		this.appButtons.add( appButton );
	}

	private void showAppButtons()
	{
		this.homescreen.setLayout( new FlowLayout(FlowLayout.LEFT, 70, 70) );
		
		for ( JButton appButton : this.appButtons )
			this.homescreen.add( appButton );
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == this.btnHome )
		{
			((CardLayout) panelcenter.getLayout()).show( panelcenter, homescreen.getName() );
		}
		
		else
		{
			for ( AbstractApp app : this.apps )
			{
				if ( e.getSource() == app.getButton() )
				{
					System.out.println( app.getName() );
					((CardLayout) panelcenter.getLayout()).show( panelcenter, app.getPanel().getName() );
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
	
	protected JPanel getNewPanel()
	{
		return null;
	}
}
