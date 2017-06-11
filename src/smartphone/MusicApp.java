package smartphone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;




import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.*;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class MusicApp extends AbstractApp implements ActionListener, ChangeListener
{
	private JLabel appTitle;
	
	private JList <MusicFile> musicList;
	private DefaultListModel<MusicFile> listModel;
	
	private JPanel bottomPanel;
	
	private MusicFile currentTrack;
	private AudioInputStream ais;
	private AudioFileFormat audioFile;
	private AudioFormat format;
	private Clip clip;
	
	private volatile boolean ignoreChanges = false;
	private volatile boolean movingSlider = false;
	
	
	private JLabel currentTrackInfo;
	private JLabel currentTime;
	private JLabel totalTime;
	private JButton stop;
	private JButton play;
	private ImageIcon playIcon;
	private ImageIcon pauseIcon;
	private ImageIcon stopIcon;
	private JSlider slider;
	
	Timer t;
  	TimerTask moveSlider = null;
	
	public MusicApp( Smartphone phone )
	{
		super( phone, "Music app", "music" );
		
		t = new Timer();
		
		this.mainPanel.setBackground( this.phone.getBackgroundColor() );
		this.mainPanel.setLayout(new BorderLayout());
		
		// Création du titre de l'application
		appTitle = new JLabel("My music");
		appTitle.setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
		appTitle.setForeground( Color.WHITE );
		appTitle.setFont( this.phone.getSmartFont("large") );
		appTitle.setHorizontalAlignment( SwingConstants.CENTER );
		this.mainPanel.add(appTitle, BorderLayout.NORTH);
		
		
		File dossierMusic = this.phone.getSoundFolder();
		File[] files = dossierMusic.listFiles();
		
		listModel = new DefaultListModel<>();
		
		for (File f : files)
		{
			try
			{
				listModel.addElement( new MusicFile(f.getCanonicalPath()) );
			}
			
			catch (IOException e)
			{
				System.err.println( "Erreur lors de l'ajout dans la liste de musiques" );
			}
		}
		
		musicList = new SmartList<MusicFile>( listModel );
		musicList.setCellRenderer( new ContactListCellRenderer() );
		
		this.mainPanel.add(musicList, BorderLayout.CENTER);
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground( this.phone.getBackgroundColor() );
		bottomPanel.setLayout( new GridLayout( 3, 1, 5, 5) );
		bottomPanel.setBorder( BorderFactory.createEmptyBorder(20, 20, 20, 20) );
		
		currentTrackInfo = new JLabel("");
		currentTrackInfo.setFont( this.phone.getSmartFont("medium") );
		currentTrackInfo.setForeground( Color.WHITE );
		currentTrackInfo.setHorizontalAlignment( SwingConstants.CENTER );
		bottomPanel.add( currentTrackInfo );
		
		slider = new JSlider( 0, 0, 0 );
		slider.setForeground( Color.WHITE );
		slider.setBackground( this.phone.getBackgroundColor() );
		slider.addChangeListener( this );
		bottomPanel.add( slider );
		
		JPanel buttonArea = new JPanel();
		buttonArea.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 5) );
		buttonArea.setBackground( this.phone.getBackgroundColor() );

		playIcon = new ImageIcon( new File(this.phone.getSysFolder(), "pictogram_play.png").toString() );
		pauseIcon = new ImageIcon( new File(this.phone.getSysFolder(), "pictogram_pause.png").toString() );
		stopIcon = new ImageIcon( new File(this.phone.getSysFolder(), "pictogram_stop.png").toString() );
		
		currentTime = new JLabel( clipPosition( null ) );
		currentTime.setFont( this.phone.getSmartFont("medium") );
		currentTime.setForeground( Color.WHITE );
		
		JLabel separator = new JLabel("/");
		separator.setFont( this.phone.getSmartFont("medium") );
		separator.setForeground( Color.WHITE );
		
		totalTime = new JLabel( clipDuration( null ) );
		totalTime.setFont( this.phone.getSmartFont("medium") );
		totalTime.setForeground( Color.WHITE );
		play = new SmartButton( playIcon );
		stop = new SmartButton( stopIcon );
		
		play.addActionListener( this );
		stop.addActionListener( this );
		
		buttonArea.add( play );
		buttonArea.add( stop );
		buttonArea.add( currentTime );
		buttonArea.add( separator );
		buttonArea.add( totalTime );
		bottomPanel.add( buttonArea );
		
		this.mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		musicList.addMouseListener( new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				if (e.getClickCount() == 2)
				{
					setCurrentTrack( musicList.getSelectedValue() );
				}
			}
		});
	}
	
	public void stateChanged( ChangeEvent e )
	{
		if (ignoreChanges) return;
		
		if ( clip != null )
		{
			long newPosition = slider.getValue() * 1000000;
			clip.setMicrosecondPosition( newPosition );
		}
	}

	public void actionPerformed( ActionEvent e )
	{
		if ( e.getSource() == stop )
		{
			stop();
		}
		
		else if (e.getSource() == play )
		{
			if (clip != null)
			{
				if ( clip.isActive() )
					pause();
				else
					play();
			}
		}
	}

	public JPanel generateMainPanel()
	{
		return new JPanel();
	}
	
	private void stop()
	{
		if ( clip != null )
			clip.close();
		
		currentTime.setText( clipPosition(null) );
		slider.setValue(0);
		
		stopMovingSlider();
	}

	private void pause()
	{
		if ( clip != null && clip.isActive() )
			clip.stop();
		
		stopMovingSlider();
	}
	
	private void play()
	{
		if (currentTrack != null)
		{
			try
			{
				if ( !clip.isOpen() )
				{
					ais = AudioSystem.getAudioInputStream( currentTrack );
					clip.open( ais );
				}
				
				clip.start();
				startMovingSlider();
			}
			
			catch( Exception ex )
			{
				System.err.println( "Ligne indisponible !" );
			}
		}
	}
	
	private void startMovingSlider()
	{
		if (movingSlider) return;
		
		moveSlider = new TimerTask()
     	{
			public void run()
			{
				updateCursorPosition();
			}
		};
		
		// La position du curseur est mise à jour 20 fois par seconde
		t.schedule( moveSlider, 0, 1000/20 );
		
		movingSlider = true;
	}
	
	private void updateCursorPosition()
	{
		if ( clip != null )
		{
			currentTime.setText( clipPosition(clip) );
			
			long secPosition = Math.round( clip.getMicrosecondPosition() / 1000000.0 );
			
			ignoreChanges = true;
			slider.setValue( (int) secPosition );
			ignoreChanges = false;
		}
	}
	
	private void stopMovingSlider()
	{
		if (!movingSlider) return;
		
		moveSlider.cancel();
		t.purge();
		movingSlider = false;
	}
	
	private void setCurrentTrack( MusicFile file )
	{
		stop();
		
		currentTrack = file;
		currentTrackInfo.setText( file.getSongInfo() );
		
		try
		{
			//lecture d'un son .wav
		    audioFile = AudioSystem.getAudioFileFormat( file );
	        format = audioFile.getFormat();
	        ais = AudioSystem.getAudioInputStream( currentTrack );
	      
	        // On décrit un Clip (son chargé en mémoire) qui a le format du fichier à lire
	        Line.Info dtli = new DataLine.Info( Clip.class, format );
	      
	        clip = (Clip) AudioSystem.getLine( dtli );
	        clip.open( ais );
	        
	        totalTime.setText( clipDuration(clip) );
	        currentTime.setText( clipPosition(null) );
	        
	        slider.setMaximum( (int)(clip.getMicrosecondLength() /1000000) ); 
	        
	        play();
		}
		
		catch (Exception e)
		{
			//e.printStackTrace();
			System.err.println( "Erreur lors de la lecture du fichier." );
		}
	}
	
	private String usToMinSec( long microSeconds )
	{
		long seconds = Math.round( microSeconds / 1000000.0 );
		
		return String.format("%d:%02d", (seconds/60), (seconds%60));
	}
	
	private String clipDuration( Clip c )
	{
		long minSec = 0;
		
		if (c != null)
		{
			long length = c.getMicrosecondLength();
			
			if ( length != AudioSystem.NOT_SPECIFIED )
			{
				minSec = length;
			}
		}
		
		return usToMinSec( minSec );
	}
	
	private String clipPosition( Clip c )
	{
		long minSec = 0;
		
		if (c != null)
		{
			long pos = c.getMicrosecondPosition();
			
			if ( pos != AudioSystem.NOT_SPECIFIED )
			{
				minSec = pos;
			}
		}
		
		return usToMinSec( minSec );
	}

	public void returnPressed()
	{
		
	}
}
