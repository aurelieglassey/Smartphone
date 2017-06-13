package smartphone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

/**
 * Cette classe génère une visualisation du son contenu dans un fichier.
 * L'amplitude de l'onde sonore est représentée par une série de barres
 * verticales de différentes hauteurs. Tout le son contenu dans le fichier
 * est lu et évalué afin de calculer l'amplitude maximum de l'onde à chaque
 * rafraîchissement de l'animation.
 * @author Fabien Terrani
 */
public class AudioVisualization extends JPanel
{
	/**
	 * Les 500 dernières amplitudes affichées dans la visualisation.
	 */
	private float[] values = new float[500];
	
	/**
	 * L'espacement entre deux barres en pixels.
	 */
	private static int hGap = 2;
	
	/**
	 * La largeur d'une barre en pixels.
	 */
	private static int barWidth = 2;
	
	/**
	 * Le nombre de barres à afficher dans la visualisation. Ne peut
	 * pas être supérieur à values.length et varie dynamiquement avec
	 * la largeur du composant.
	 */
	private int usedLength = 90;
	
	/**
	 * La hauteur maximum d'une barre par rapport à la hauteur du composant.
	 */
	private static float maxHeightFactor = 0.95f;
	
	/**
	 * Identifiant du jeu de couleurs par défaut.
	 */
	public static final int THEME_DEFAULT = 1;
	
	/**
	 * Identifiant du jeu de couleurs feu.
	 */
	public static final int THEME_FIRE = 2;
	
	/**
	 * Identifiant du jeu de couleurs eau.
	 */
	public static final int THEME_WATER = 3;
	
	/**
	 * Identifiant du jeu de couleurs actuellement utilisé.
	 */
	private int currentTheme = THEME_DEFAULT;
	
	/**
	 * Fréquence de rafraîchissement de l'animation.
	 */
	private static final int ANIMATION_FREQUENCY = 40;
	
	/**
	 * Tableau contenant les amplitudes maximum pour chaque période
	 * de l'animation. Sa longueur est variable et dépend de la longueur
	 * du son contenu dans le fichier.
	 */
	private float[] maxAmplitudes;
	
	/**
	 * Timer utilisé pour rafraîchir l'animation.
	 */
	private Timer t;
	
	/**
	 * Tâche exécutée par le Timer pour rafraîchir l'animation.
	 */
	private TimerTask animTask;
	
	/**
	 * L'index de la prochaine amplitude à afficher dans l'animation.
	 * Cet index correspond au tableau maxAmplitudes.
	 */
	private int currentValueIndex = 0;
	
	/**
	 * Booléen représentant l'état de l'animation. TRUE si l'animation
	 * est en pause (des amplitudes à 0.0 sont envoyées pour l'affichage),
	 * FALSE si l'animation est en train d'être affichée. Après la construction
	 * de l'objet, l'animation est en pause par défaut.
	 */
	private boolean paused = true;
	
	/**
	 * Crée une nouvelle visualisation audio (en pause par défaut).
	 */
	public AudioVisualization()
	{
		setBackground( Color.BLACK );
		
		addMouseListener( new MouseAdapter()
		{
			public void mouseClicked( MouseEvent e )
			{
				currentTheme++;
				
				if (currentTheme > 3)
					currentTheme = 1;
			}
		});
		
		t = new Timer();
		
		initAnimation();
		pauseAnimation();
	}
	
	/**
	 * Crée une nouvelle visualisation audio associée au son du fichier
	 * fourni en paramètre. L'animation est en pause par défaut.
	 * @param f Le fichier WAVE contenant le son à utiliser pour l'animation
	 * @throws Exception Émet une exception si l'encodage du son dans le fichier
	 * n'est pas supporté. Actuellement, seul le PCM par entiers signé et
	 * non signé est supporté.
	 */
	public AudioVisualization( File f ) throws Exception
	{
		this();
		fetchFileData( f );
	}
	
	/**
	 * Crée une nouvelle visualisation audio associée au son du fichier
	 * fourni en paramètre. L'animation est en pause par défaut.
	 * @param f Le fichier WAVE contenant le son à utiliser pour l'animation
	 * @param startImmediately TRUE pour démarrer l'animation immédiatement, FALSE pour la laisser en pause.
	 * @throws Exception Émet une exception si l'encodage du son dans le fichier
	 * n'est pas supporté. Actuellement, seul le PCM par entiers signé et
	 * non signé est supporté.
	 */
	public AudioVisualization( File f, boolean startImmediately ) throws Exception
	{
		this( f );
		
		if (startImmediately)
			resumeAnimation();
	}
	
	/**
	 * Lit le son du fichier passé en paramètre pour générer l'animation.
	 * @param f Le fichier WAVE contenant le son à utiliser pour l'animation
	 * @throws Exception Émet une exception si l'encodage du son dans le fichier
	 * n'est pas supporté. Actuellement, seul le PCM par entiers signé et
	 * non signé est supporté.
	 */
	public void fetchFileData( File f ) throws Exception
	{
		AudioFileFormat audioFile = AudioSystem.getAudioFileFormat( f );
		AudioFormat format = audioFile.getFormat();
		AudioFormat.Encoding encoding = format.getEncoding();
		AudioInputStream ais = AudioSystem.getAudioInputStream( f );
		
		if (encoding != AudioFormat.Encoding.PCM_SIGNED && encoding != AudioFormat.Encoding.PCM_UNSIGNED)
			throw new Exception("Le fichier n'est ni PCM_SIGNED, ni PCM_UNSIGNED");
		
		//float sampleRate = format.getSampleRate();
		float frameRate = format.getFrameRate();
		int bytesPerFrame = format.getFrameSize();
		//int channels = format.getChannels();
		boolean bigEndian = format.isBigEndian();
		int bitsPerSample = format.getSampleSizeInBits();
		int bytesPerSample = bitsPerSample / 8;
		int samplesPerFrame = bytesPerFrame / bytesPerSample;

		
		
		double msDuration = ais.getFrameLength() / format.getFrameRate() * 1000.0;
		
		
		int updatePerSecond = ANIMATION_FREQUENCY; // Fréquence à laquelle on va mettre à jour l'animation
		double msChunkDuration = 1000.0/updatePerSecond; // L'intervalle de temps en ms sur lequel on recherche l'amplitude maximum
		double framePerChunk = frameRate * (msChunkDuration/1000.0);
		
		// Une amplitude par chunk
		this.maxAmplitudes = new float[(int) Math.ceil( msDuration / msChunkDuration )];
		byte[] readBuffer = new byte[ ais.available() ];
		int readBytes;
		
		//long bytesLength = ais.getFrameLength() * format.getFrameSize();
		long frameIndex = 0;
		long sampleBits = 0;
		int sampleOffset = 0;
		
		double amplitude = 0.0;
		double middleValue = Math.pow(2, bitsPerSample-1);

		long valueMask = ~(((~0L) >> bitsPerSample) << bitsPerSample);
		long signBit = 0x1 << (bitsPerSample-1);
		
		while ( (readBytes = ais.read(readBuffer, 0, readBuffer.length)) >= 0 )
		{
			int chunkIndex;
			
			// On parcourt toutes les frames renvoyées et on recherche l'échantillon avec la plus grande amplitude
			for (int i = 0; i < readBytes; i+= bytesPerFrame)
			{
				chunkIndex = (int) Math.min(
					this.maxAmplitudes.length-1,
					Math.floor(frameIndex / framePerChunk)
				);
				
				for (int k = 0; k < samplesPerFrame; k++)
				{
					sampleOffset = i + k*bytesPerSample;
					sampleBits = 0;
					
					for (int j = 0; j < bytesPerSample; j++)
					{
						if (bigEndian)
							sampleBits |= readBuffer[ sampleOffset + j ] << (8 * (bytesPerSample-1-j));
						else
							sampleBits |= readBuffer[ sampleOffset + j ] << (8 * j);
					}
					
					// On calcule la version positive du nombre si l'encodage est signé
					if ( (sampleBits & signBit) > 0)
					{
						sampleBits = (~sampleBits + 1) & valueMask;
					}
					else
					{
						sampleBits = (long)(sampleBits - middleValue);
					}
					
					
					amplitude = sampleBits / middleValue;
					
					// On ne garde que la plus haute amplitude rencontrée
					if ( amplitude > this.maxAmplitudes[chunkIndex] )
						this.maxAmplitudes[chunkIndex] = (float) amplitude;
				}
				
				frameIndex++;
			}
		}
		
		// Contrôle des valeurs obtenues
		for ( int i = 0; i < this.maxAmplitudes.length; i++)
		{
			if( this.maxAmplitudes[i] > 1.0 )
				this.maxAmplitudes[i] = 1.0f;
			else if (this.maxAmplitudes[i] < 0.0)
				this.maxAmplitudes[i] = 0.0f;
		}
	}
	
	/**
	 * Méthode surchargée pour dessiner le frame courant de l'animation.
	 */
	public void paintComponent( Graphics gr )
	{
		if (gr instanceof Graphics2D)
		{
			usedLength = getWidth()/ 2 / (hGap+barWidth);
			
			gr.setColor( new Color(0.0f,0.0f,0.0f,1.0f) );
			gr.fillRect(0, 0, getWidth(), this.getHeight() );
			
			
			
			int xPos, yPos, barHeight;
			Color barColor, c;
			float[] color = new float[4];
			
			// Boucle pour afficher les barres
			for (int i = 0; i < usedLength && i < values.length; i++)
			{
				if (values[i] == 0) continue;
				
				switch( currentTheme )
				{
					case THEME_FIRE:
						color[0] = values[i]*0.3f + 0.7f;
						color[1] = values[i];
						color[2] = values[i]*0.1f;
						color[3] = 1.0f;
						break;
					
					case THEME_WATER:
						color[0] = 0;
						color[1] = values[i];
						color[2] = 1.0f;
						color[3] = 1.0f;
						break;
					
					case THEME_DEFAULT:
					default:
						color[0] = values[i];
						color[1] = (float)(Math.sin(100.0f*values[i]*Math.PI/64   + Math.PI/2 )+1)/2;
						color[2] = (float)(Math.sin(i*Math.PI/32   + Math.PI/2 )+1)/2;
						color[3] = 1.0f - values[i]*0.2f;
						break;
				}
				
				
				for (int j = 0; j < color.length; j++)
				{
					color[j] = Math.abs(color[j]);
					if (color[j] > 1.0f) color[j] = 1.0f;
				}
				
				
				barColor = new Color( color[0], color[1], color[2], color[3] );
				
				
				barHeight = (int)(values[i]*this.getHeight() * ((float)usedLength-i)/usedLength );
				barHeight *= maxHeightFactor;
				
				xPos = (int)(this.getWidth()/2) + i*(hGap+barWidth);
				yPos = (this.getHeight() - barHeight)/2;
				
				// Boucle pour appliquer un flou sur les barres
				for (int j = 0; j < 3+ (int)((values[i]/100.0f) * 4); j++)
				{
					c = new Color(
						barColor.getRed(),
						barColor.getGreen(),
						barColor.getBlue(),
						(int)( barColor.getAlpha()/(1+j) )
					);
					
					gr.setColor( c );
					
					// Boucle pour dupliquer les barres à gauche et à droite
					for (int k = 0; k < 2; k++)
					{
						if (k != 0 && i != 0)
						{
							xPos -= 2*(xPos - this.getWidth()/2);
						}
					
						gr.fillRect( xPos-j, yPos-j, barWidth+2*j, barHeight+2*j );
					}
				}
			}
		}
		
		else super.paintComponent( gr );
	}
	
	/**
	 * Initialise et démarre le rafraîchissement de l'animation.
	 */
	public void initAnimation()
	{
		animTask = new TimerTask()
		{
			public void run()
			{
				nextFrame();
			}
		};
		
		t.schedule( animTask, 0, 1000/ANIMATION_FREQUENCY );
	}
	
	/**
	 * Met l'animation en pause en stoppant la génération de nouvelles barres.
	 */
	public void pauseAnimation()
	{
		paused = true;
	}
	
	/**
	 * Reprend l'animation et génère de nouvelles barres.
	 */
	public void resumeAnimation()
	{
		paused = false;
	}
	
	/**
	 * Change l'amplitude actuelle pour celle désignée par le nombre flottant
	 * passé en paramètre.
	 * @param progress Le moment de l'animation auquel se rendre. 0.0 désigne
	 * le début du son (première amplitude) et 1.0 la fin du son (dernière amplitude).
	 */
	public void setProgress( float progress )
	{
		if (progress > 1.0f) progress = 1.0f;
		else if (progress < 0.0f) progress = 0.0f;
		
		currentValueIndex = Math.round(progress * (maxAmplitudes.length-1));
	}
	
	/**
	 * Affiche l'image suivante de l'animation. Si l'animation est en pause,
	 * génère des barres avec une hauteur de 0 pixels.
	 */
	public void nextFrame()
	{
		float amplitude = 0.0f;
		
		if ( !paused )
		{
			if (currentValueIndex < maxAmplitudes.length-1)
			{
				currentValueIndex++;
				amplitude = maxAmplitudes[ currentValueIndex ];
			}
			
			if (amplitude < 0.0f) amplitude = 0.0f;
			else if (amplitude > 1.0f) amplitude = 1.0f;
		}
		
		for (int i = values.length-1; i >= 1; i--)
		{
			values[i] = values[i-1];
		}
		
		values[0] = amplitude;
		
		repaint();
	}
	
	/**
	 * Met fin au thread utilisé pour le rafraîchissement de l'animation.
	 */
	public void finalize()
	{
		t.cancel();
	}
}
