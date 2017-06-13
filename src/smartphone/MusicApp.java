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
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Cette classe représente une application permettant la lecture de fichiers son.
 * Le seul format disponible est le format WAVE. Les formats de tags reconnus des fichiers
 * sont le INFO-LIST et le ID3.
 * @author Fabien Terrani
 */
public class MusicApp extends AbstractApp implements ActionListener, ChangeListener, LineListener
{
	/**
	 * Panneau avec scrollbars contenant la liste des musiques.
	 */
	private JScrollPane musicPane;
	
	/**
	 * Liste des musiques.
	 */
	private JList <MusicFile> musicList;
	
	/**
	 * Modèle de la liste des musiques.
	 */
	private DefaultListModel<MusicFile> listModel;
	
	/**
	 * Panneau centrale contenant la visualisation ou la liste des musiques.
	 */
	private JPanel centerPanel;
	
	/**
	 * Panneau de la zone inférieure contenant les contrôles du lecteur.
	 */
	private JPanel bottomPanel;
	
	/**
	 * Le fichier son en cours de lecture.
	 */
	private MusicFile currentTrack;
	
	/**
	 * Flux d'entrée audio lié au fichier son sélectionné.
	 */
	private AudioInputStream ais;
	
	/**
	 * Format du fichier audio sélectionné.
	 */
	private AudioFileFormat audioFile;
	
	/**
	 * Format du son stocké dans le fichier son sélectionné.
	 */
	private AudioFormat format;
	
	/**
	 * Son du fichier contenu en mémoire.
	 */
	private Clip clip;
	
	/**
	 * Booléen permettant de gérer le déplacement automatique du slider.
	 * S'il est à TRUE, les changements de valeur du slider provoqués par
	 * la lecture du fichier sont ignorés. S'il est à FALSE, le changement
	 * de valeur est pris en compte pour atteindre une nouvelle position
	 * dans la piste audio.
	 */
	private volatile boolean ignoreChanges = false;
	
	/**
	 * Label contenant le titre et l'artiste du fichier son sélectionné.
	 */
	private JLabel currentTrackInfo;
	
	/**
	 * Label contenant l'album du fichier son sélectionné.
	 */
	private JLabel albumInfo;
	
	/**
	 * Label contenant la position temporelle actuelle.
	 */
	private JLabel currentTime;
	
	/**
	 * Label contenant la durée totale du fichier son sélectionné.
	 */
	private JLabel totalTime;
	
	/**
	 * Bouton permettant d'arrêter la lecture du son.
	 */
	private JButton stop;
	
	/**
	 * Bouton permettant de mettre en pause ou de reprendre la lecture.
	 */
	private JButton playPause;
	
	/**
	 * Bouton permettant d'afficher ou non la visualisation du son.
	 */
	private JButton toggleVisualization;
	
	/**
	 * Icône play.
	 */
	private ImageIcon playIcon;
	
	/**
	 * Icône pause.
	 */
	private ImageIcon pauseIcon;
	
	/**
	 * Icône stop.
	 */
	private ImageIcon stopIcon;
	
	/**
	 * Icône liste.
	 */
	private ImageIcon listIcon;
	
	/**
	 * Icône visualisation.
	 */
	private ImageIcon visuIcon;
	
	/**
	 * Slider utilisé pour afficher la position temporelle actuelle.
	 */
	private JSlider slider;
	
	/**
	 * Timer utilisé pour mettre à jour la position du slider.
	 */
	Timer t;
	
	/**
	 * Tâche de mise à jour de la position du slider.
	 */
  	TimerTask moveSlider = null;
  	
  	/**
  	 * Visualisation du son du fichier sélectionné.
  	 */
  	private AudioVisualization visualization;
  	
  	/**
  	 * Booléen permettant d'afficher la visualisation audio (TRUE) ou
  	 * la liste de musiques (FALSE).
  	 */
  	private boolean showingWave = false;
	
  	/**
  	 * Crée une nouvelle application musique.
  	 * @param phone Le smartphone sur lequel l'application est installée.
  	 */
	public MusicApp( Smartphone phone )
	{
		super( phone, "Music app", "music" );
		
		t = new Timer();
		
		this.getMainPanel().setBackground( Smartphone.getBackgroundColor() );
		this.getMainPanel().setLayout(new BorderLayout());
		
		// Création du titre de l'application
		JLabel appTitle = new JLabel("My music");
		appTitle.setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
		appTitle.setForeground( Color.WHITE );
		appTitle.setFont( Smartphone.getSmartFont("large") );
		appTitle.setHorizontalAlignment( SwingConstants.CENTER );
		this.getMainPanel().add(appTitle, BorderLayout.NORTH);
		
		
		File dossierMusic = this.getPhone().getSoundFolder();
		File[] files = dossierMusic.listFiles();
		
		listModel = new DefaultListModel<>();
		
		for (File f : files)
		{
			listModel.addElement( new MusicFile( f.toString() ) );
		}
		
		musicList = new SmartList<MusicFile>( listModel );
		musicList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		musicList.setCellRenderer( new SmartListCellRenderer() );
		musicPane = new SmartScrollPane( musicList );
		
		centerPanel = new JPanel();
		centerPanel.setLayout( new BorderLayout() );
		centerPanel.setBackground( Smartphone.getBackgroundColor() );
		centerPanel.add( musicPane, BorderLayout.CENTER );
		
		
		bottomPanel = new JPanel();
		bottomPanel.setBackground( Smartphone.getBackgroundColor() );
		bottomPanel.setLayout( new BoxLayout(bottomPanel, BoxLayout.PAGE_AXIS) );
		bottomPanel.setBorder( BorderFactory.createEmptyBorder(10,10,10,10) );
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout( new BorderLayout() );
		infoPanel.setBorder( BorderFactory.createEmptyBorder(10,10,10,10) );
		infoPanel.setBackground( Smartphone.getBackgroundColor() );
		
		
		
		currentTrackInfo = new JLabel("");
		currentTrackInfo.setFont( Smartphone.getSmartFont("medium") );
		currentTrackInfo.setForeground( Color.WHITE );
		currentTrackInfo.setHorizontalAlignment( SwingConstants.CENTER );
		infoPanel.add( currentTrackInfo, BorderLayout.NORTH );
		
		albumInfo = new JLabel("");
		albumInfo.setFont( Smartphone.getSmartFont("small") );
		albumInfo.setForeground( Color.WHITE );
		albumInfo.setHorizontalAlignment( SwingConstants.CENTER );
		infoPanel.add( albumInfo, BorderLayout.SOUTH );
		
		bottomPanel.add( infoPanel );
		
		JPanel timePanel = new JPanel();
		timePanel.setLayout( new FlowLayout() );
		timePanel.setBorder( BorderFactory.createEmptyBorder(10,10,10,10) );
		timePanel.setBackground( Smartphone.getBackgroundColor() );
		
		currentTime = new JLabel( clipPosition( null ) );
		currentTime.setFont( Smartphone.getSmartFont("medium") );
		currentTime.setForeground( Color.WHITE );
		
		JLabel separator = new JLabel("/");
		separator.setFont( Smartphone.getSmartFont("medium") );
		separator.setForeground( Color.WHITE );
		
		totalTime = new JLabel( clipDuration( null ) );
		totalTime.setFont( Smartphone.getSmartFont("medium") );
		totalTime.setForeground( Color.WHITE );

		timePanel.add( currentTime );
		timePanel.add( separator );
		timePanel.add( totalTime );
		
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout( new BorderLayout() );
		sliderPanel.setBackground( Smartphone.getBackgroundColor() );
		
		slider = new JSlider( 0, 0, 0 );
		slider.setForeground( Color.WHITE );
		slider.setBackground( Smartphone.getBackgroundColor() );
		slider.addChangeListener( this );

		sliderPanel.add( slider, BorderLayout.CENTER );
		sliderPanel.add( timePanel, BorderLayout.WEST );
		
		bottomPanel.add( sliderPanel );
		
		JPanel buttonArea = new JPanel();
		buttonArea.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10) );
		buttonArea.setBackground( Smartphone.getBackgroundColor() );

		playIcon = new ImageIcon( new File(this.getPhone().getSysFolder(), "pictogram_play.png").toString() );
		pauseIcon = new ImageIcon( new File(this.getPhone().getSysFolder(), "pictogram_pause.png").toString() );
		stopIcon = new ImageIcon( new File(this.getPhone().getSysFolder(), "pictogram_stop.png").toString() );
		listIcon = new ImageIcon( new File(this.getPhone().getSysFolder(), "pictogram_list.png").toString() );
		visuIcon = new ImageIcon( new File(this.getPhone().getSysFolder(), "pictogram_visualization.png").toString() );
		
		
		playPause = new SmartButton( playIcon );
		stop = new SmartButton( stopIcon );
		toggleVisualization = new SmartButton( visuIcon );
		
		playPause.addActionListener( this );
		stop.addActionListener( this );
		toggleVisualization.addActionListener( this );
		
		buttonArea.add( stop );
		buttonArea.add( playPause );
		buttonArea.add( toggleVisualization );
		//buttonArea.add( currentTime );
		//buttonArea.add( separator );
		//buttonArea.add( totalTime );
		bottomPanel.add( buttonArea );
		
		this.getMainPanel().add( centerPanel, BorderLayout.CENTER );
		this.getMainPanel().add( bottomPanel, BorderLayout.SOUTH );
		
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
		
		visualization = new AudioVisualization();
	}
	
	/**
	 * Gère les changements de valeur du slider.
	 */
	public void stateChanged( ChangeEvent e )
	{
		if (ignoreChanges) return;
		
		if ( clip != null )
		{
			long newPosition = slider.getValue() * 1000000;
			clip.setMicrosecondPosition( newPosition );
		}
		
		updateUIFromClip();
	}
	
	/**
	 * Met à jour les éléments de l'interface graphique en fonction
	 * du fichier son sélectionné.
	 */
	private void updateUIFromClip()
	{
		if (clip != null && clip.isActive() )
		{
			playPause.setIcon( pauseIcon );
			
			currentTime.setText( clipPosition(clip) );
			totalTime.setText( clipDuration(clip) );
			slider.setMaximum( (int)(clip.getMicrosecondLength() / 1000000) );
			slider.setValue( (int)(clip.getMicrosecondPosition() / 1000000) );
			startMovingSlider();
			
			
			float currentProgress = ((float)clip.getMicrosecondPosition()) / clip.getMicrosecondLength();
			
			visualization.resumeAnimation();
			visualization.setProgress( currentProgress );
		}
		
		else
		{
			playPause.setIcon( playIcon );
			
			currentTime.setText( clipPosition(clip) );
			totalTime.setText( clipDuration(clip) );
			
			if (clip != null)
			{
				slider.setMaximum( (int)(clip.getMicrosecondLength() / 1000000) );
				slider.setValue( (int)(clip.getMicrosecondPosition() / 1000000) );
			}
			
			stopMovingSlider();
			
			visualization.pauseAnimation();
		}
	}
	
	/**
	 * Gère l'interaction avec les différents éléments de l'interface du lecteur.
	 */
	public void actionPerformed( ActionEvent e )
	{
		if ( e.getSource() == stop )
		{
			stop();
			updateUIFromClip();
		}

		else if (e.getSource() == playPause )
		{
			if ( clip != null && clip.isActive() )
				pause();
			else
				play();
		}

		else if (e.getSource() == toggleVisualization )
		{
			if (!showingWave)
				showVisualization();
			else
				showMusicList();
		}
	}
	
	/**
	 * Affiche la visualisation audio dans la zone centrale.
	 */
	private void showVisualization()
	{
		centerPanel.remove( musicPane );
		centerPanel.add( visualization );
		toggleVisualization.setIcon( listIcon );
		
		centerPanel.revalidate();
		centerPanel.repaint();
		
		showingWave = true;
	}
	
	/**
	 * Affiche la liste des musiques dans la zone centrale.
	 */
	private void showMusicList()
	{
		centerPanel.remove( visualization );
		centerPanel.add( musicPane );
		toggleVisualization.setIcon( visuIcon );

		centerPanel.revalidate();
		centerPanel.repaint();
		
		showingWave = false;
	}
	
	/**
	 * Gère l'appui sur le bouton Retour. Dans cette application, cette méthode
	 * ne fait rien.
	 */
	public void returnPressed()
	{
		
	}
	
	/**
	 * Stoppe la lecture du son.
	 */
	private void stop()
	{
		if ( clip != null )
			clip.close();
		
		updateUIFromClip();
		
		//currentTime.setText( clipPosition(null) );
		//slider.setValue(0);
	}
	
	/**
	 * Met en pause la lecture du son.
	 */
	private void pause()
	{
		if ( clip != null && clip.isActive() )
			clip.stop();
		
		updateUIFromClip();
	}
	
	/**
	 * Reprend la lecture du son.
	 */
	private void play()
	{
		if ( currentTrack == null && !musicList.isSelectionEmpty() )
		{
			setCurrentTrack( musicList.getSelectedValue() );
		}
		
		if (clip != null)
		{
			try
			{
				if ( !clip.isOpen() )
				{
					ais = AudioSystem.getAudioInputStream( currentTrack );
					clip.open( ais );
				}
				
				clip.start();
			}
			
			catch( Exception ex )
			{
				JOptionPane.showMessageDialog(null, "Ligne indisponible !");
			}
		}
		
		updateUIFromClip();
	}
	
	/**
	 * Active la mise à jour du temps et de la position du slider.
	 */
	private void startMovingSlider()
	{
		moveSlider = new TimerTask()
     	{
			public void run()
			{
				updateTimeAndCursorPosition();
			}
		};
		
		// La position du curseur est mise à jour 20 fois par seconde
		t.schedule( moveSlider, 0, 1000/20 );
	}
	
	/**
	 * Met à jour le temps actuel et la position du curseur du slider.
	 */
	private void updateTimeAndCursorPosition()
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
	
	/**
	 * Désactive la mise à jour du temps et de la position du slider.
	 */
	private void stopMovingSlider()
	{
		if ( moveSlider != null )
			moveSlider.cancel();
		
		t.purge();
	}
	
	/**
	 * Prépare le lecteur pour la lecture du fichier passé en paramètre.
	 * @param file Le fichier son à lire.
	 */
	private void setCurrentTrack( MusicFile file )
	{
		stop();
		
		currentTrack = file;
		currentTrackInfo.setText( file.getSongInfo() );
		albumInfo.setText( file.getAlbumInfo() );
		
		try
		{
			visualization.fetchFileData( file );
			
			//lecture d'un son .wav
		    audioFile = AudioSystem.getAudioFileFormat( file );
	        format = audioFile.getFormat();
	        //ais = AudioSystem.getAudioInputStream( currentTrack );
	      
	        // On décrit un Clip (son chargé en mémoire) qui a le format du fichier à lire
	        Line.Info dtli = new DataLine.Info( Clip.class, format );
	      
	        clip = (Clip) AudioSystem.getLine( dtli );
	        clip.addLineListener( this );
	        //clip.open( ais );
	        
	        play();
		}
		
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(null, "Erreur lors de la lecture du fichier." );
		}
	}
	
	/**
	 * Convertit le temps en microsecondes passé en paramètre en minutes et secondes.
	 * @param microSeconds Le temps à convertir en microsecondes
	 * @return Une chaîne contenant le temps formatté en minutes et secondes suivant le format m:ss
	 */
	private String usToMinSec( long microSeconds )
	{
		long seconds = Math.round( microSeconds / 1000000.0 );
		
		return String.format("%d:%02d", (seconds/60), (seconds%60));
	}
	
	/**
	 * Retourne la durée d'un clip en minutes et secondes.
	 * @param c Le clip dont il faut calculer la durée
	 * @return Une chaîne contenant la durée formattée en minutes et secondes suivant
	 * le format m:ss
	 */
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
	
	/**
	 * Retourne la position temporelle d'un clip en minutes et secondes.
	 * @param c Le clip dont il faut calculer la position temporelle
	 * @return Une chaîne contenant la position temporelle formattée en minutes et secondes
	 * suivant le format m:ss
	 */
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
	
	/**
	 * Met à jour l'interface graphique du lecteur lorsque la ligne de sortie du son
	 * change de statut.
	 */
	public void update( LineEvent e )
	{
		updateUIFromClip();
	}
}
