package smartphone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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


public class MainFrameSmartphone extends JFrame{

	
	
	private JPanel panelcenter = new JPanel (); 
	private JPanel panelsouth = new JPanel();
	private JButton showapplication = new JButton ("showapplication");
	private JButton home = new JButton ("home");
	private JButton retrun = new JButton ("return");
	private JButton music = new JButton("music");
	private GridLayout g = new GridLayout(1, 3); 
	
	public MainFrameSmartphone (){
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Phone");
		this.setResizable(false); //désactiver les boutons de redimensionnement de la fenêtre
		
		//Panel du centre avec dimensions
		panelcenter.setPreferredSize(new Dimension(480, 800));
		panelcenter.add(music);
		add(panelcenter);
		add(panelcenter, BorderLayout.CENTER);
		
		//Panel sud avec 3 bouttons 
		panelsouth.setLayout(g);
		panelsouth.add(showapplication);
		panelsouth.add(home);
		panelsouth.add(retrun);
		add(panelsouth, BorderLayout.SOUTH);
		
		
		music.addActionListener(new MusicList());
		
		
		
		
		pack();
		
		
		
	}
	
	class MusicList implements ActionListener {

		
		public void actionPerformed(ActionEvent e) {
			
			FrameMusic framemusic = new FrameMusic();
			framemusic.setVisible(true);
			
		}

	}
	
	class AppMusic extends JPanel{
		
		private JFileChooser chooser = new JFileChooser();
		private JButton open = new JButton("Ouvrir");
		public AppMusic (){
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setTitle("My music");
			
			open.addActionListener( new Listener() );
			add(open);
			
			pack();
		}
	
		class Listener implements ActionListener{
	
			public void actionPerformed(ActionEvent e) {
				
				 int returnVal = chooser.showOpenDialog(AppMusic.this);
				 if(returnVal == JFileChooser.CANCEL_OPTION) {
				       System.out.println("You chose to cancel this action ");
				 }
				 
				 if(returnVal == JFileChooser.APPROVE_OPTION) {
				      
				       File file = chooser.getSelectedFile();
				       System.out.println("Nom fichier : " +file.getName());
				       
				       //salut fabien
				      
				       
				    
				 }
			}
			
		}
	}
	
}


	
	

