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
import javax.swing.JPanel;


public class Smartphone extends JFrame implements ActionListener
{
	private JPanel panelcenter = new JPanel();
	private JPanel panelsouth = new JPanel();
	private JPanel homescreen = new JPanel();
	
	private JButton btnApps = new JButton ("Apps");
	private JButton btnHome = new JButton ("Home");
	private JButton btnReturn = new JButton ("Return");

	
	private ArrayList<AbstractApp> apps = new ArrayList<>();
	private ArrayList<JButton> appButtons = new ArrayList<>();
	
	public Smartphone ()
	{
		//Panel du centre avec dimensions
		panelcenter.setPreferredSize(new Dimension(480, 800));
		panelcenter.setLayout( new CardLayout() );
		
		homescreen.setName( "Homescreen" );
		homescreen.setBackground( Color.BLACK );
		
		this.prepareApps();
		
		this.showAppButtons();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Smartphone");
		this.setResizable(false); //désactiver les boutons de redimensionnement de la fenêtre
		
		
		
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
		addApp( new MusicApp() );
		addApp( new ContactApp() );
	}
	
	private void addApp( AbstractApp app )
	{
		this.apps.add( app );
		panelcenter.add( app.getPanel(), app.getName() );
		
		JButton appButton = app.getButton();
		
		appButton.addActionListener( this );
		this.appButtons.add( appButton );
	}

	private void showAppButtons()
	{
		this.homescreen.setLayout( new FlowLayout() );
		
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
}
