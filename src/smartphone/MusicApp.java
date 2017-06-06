package smartphone;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;




import java.util.Arrays;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


//ajouter dans ma fenêtre mes fichiers de son
//double cliquer et démarrer la lecture de d'un son

public class MusicApp extends AbstractApp
{
	private JFileChooser chooser = new JFileChooser();
	
	private JButton bsearchMusic = new JButton("Search new Music");
	private JButton baddPlaylist = new JButton ("Add a Playlist");
	private JButton bremovePlaylist = new JButton ("Remove a Playlist");
	
	private JPanel panelButton = new JPanel ();
	
	private JLabel ltitre = new JLabel("My Playlists");
	
	protected ArrayList<File> musiclist = new ArrayList<File>();
	private JList <File>jlist ;
	
	
	public MusicApp( Smartphone phone )
	{
		super( phone, "Music app", "music" );
		
		this.mainPanel.setLayout(new BorderLayout());
	
		File dossierMusic = this.phone.getSoundFolder();
		
		//insertion des fichiers dans un tableau
		File[] listofMusic = dossierMusic.listFiles();

		
		musiclist.addAll( Arrays.asList(listofMusic) );
		
		
		jlist= new JList(musiclist.toArray());
		
		
		this.mainPanel.add(panelButton, BorderLayout.SOUTH);
		this.mainPanel.add(jlist);
		this.mainPanel.add(ltitre, BorderLayout.NORTH);
		
		panelButton.setLayout(new FlowLayout());	
		panelButton.add(bsearchMusic);
		panelButton.add(baddPlaylist);
		panelButton.add(bremovePlaylist);
		
		bsearchMusic.addActionListener( new Listener() );
		jlist.getSelectionModel();
		jlist.addListSelectionListener( new JlistListener());
	}
	

	class JlistListener implements ListSelectionListener
	{

		public void valueChanged(ListSelectionEvent e)
		{
			if(!jlist.getValueIsAdjusting()){
				System.out.println("Click sur la Jlist");
				
			}
			
			
		}
		
		
	}

	public class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			
			
			if(e.getSource()==bsearchMusic)
			{
				System.out.println("Search Music");
				 int returnVal = chooser.showOpenDialog( null );
				 
				 if(returnVal == JFileChooser.CANCEL_OPTION)
				 {
					 System.out.println("You chose to cancel this action ");
				 }
				 
				 if(returnVal == JFileChooser.APPROVE_OPTION)
				 {
					 File file = chooser.getSelectedFile();
					 System.out.println("Nom fichier : " +file.getName());
					 
				 } 
			}	 
		}
	}

	public JPanel generateMainPanel()
	{
		return new JPanel();
	}
	
	//soundfolder
	public void lectureFile (){
		
		File f = new File ("C:\\Users\\Aurélie\\Desktop\\MusicSmartphone");
		
		try {
			//lecture d'un son .waw
		    AudioFileFormat audioFile = AudioSystem.getAudioFileFormat( f );
	        AudioFormat format = audioFile.getFormat();
	      
	        // On décrit un Clip (son chargé en mémoire) qui a le format du fichier à lire
	        Info dtli = new DataLine.Info( Clip.class, format );
	      
	        Clip clip = (Clip) AudioSystem.getLine( dtli );
	      
	        AudioInputStream ais = AudioSystem.getAudioInputStream( f );
	        clip.open( ais );
	        clip.start( );
	      
	      // On attend tant que la ligne ne joue pas
	      //while ( !clip.isRunning() ) Thread.sleep(10);
	      
	      // On attend tant que la ligne joue... malin !
	      //while ( clip.isRunning() ) Thread.sleep(10);

		}
		
		catch (IOException e) 
		{
			e.printStackTrace();
		} 
		
		catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		} 
		
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
		} 
	}
}
