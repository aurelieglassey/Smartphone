package smartphone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * HomePanel est un JPanel représentant l'écran d'accueil du smartphone. Il affiche une horloge
 * ainsi que les boutons des applications.
 * @author Fabien Terrani
 */
public class HomePanel extends JPanel
{
	private Smartphone phone;
	private AbstractApp[] apps = new AbstractApp[0];
	
	private JLabel clock;
	private Timer t;
	private TimerTask refreshClock;
	
	private JPanel appPanel = null;
	
	public HomePanel( Smartphone phone, AbstractApp[] appButtons )
	{
		this.phone = phone;
		
		setBackground( Color.PINK );
		setLayout( new BorderLayout() );
		setPreferredSize( this.phone.getScreenSize() );
		
		initClock();

		Dimension appButtonSize = this.phone.getAppButtonSize();
		Dimension screenSize = this.phone.getScreenSize();
		int spaceWidth = (screenSize.width - (3*appButtonSize.width)) / 4;
		
		appPanel = new JPanel();
		appPanel.setOpaque( false );
		appPanel.setBackground( new Color(0,0,0,0) );
		appPanel.setLayout( new FlowLayout( FlowLayout.LEFT, spaceWidth, spaceWidth) );
		
		
		setAppButtons( appButtons );
		
		
		add( clock, BorderLayout.NORTH );
		add( appPanel, BorderLayout.CENTER );
	}
	
	public void setAppButtons( AbstractApp[] appButtons )
	{
		if (appButtons != null)
		{
			apps = Arrays.copyOf( appButtons, appButtons.length );
		}
		
		refreshApps();
	}
	
	private void refreshApps()
	{
		appPanel.removeAll();
		
		for ( AbstractApp app : apps )
		{
			JButton btn = app.getButton();
			appPanel.add( btn );
		}
	}

	private void initClock()
	{
		clock = new JLabel();
		clock.setForeground( Color.WHITE );
		clock.setFont( new Font("Raleway", Font.BOLD, 72 ) );
		clock.setHorizontalAlignment( SwingConstants.CENTER );
		
		refreshClock = new TimerTask()
		{
			public void run()
			{
				clock.setText(
					LocalTime.now().format( DateTimeFormatter.ofPattern("HH:mm"))
				);
			}
		};
		
		t = new Timer();
		t.schedule( refreshClock, 0, 1000 );
	}
	
	public void paintComponent( Graphics g )
	{
		Dimension screen = this.phone.getScreenSize();
		File bg = this.phone.getBackgroundFile();
		Image img = null;
		
		try
		{
			img = Utils.resizeImage( ImageIO.read( bg ), screen.width, screen.height );
		}
		catch (Exception e) {}
		
		if ( img != null )
		{
			g.drawImage(img, 0, 0, null);
		}
		
		else if (g instanceof Graphics2D)
		{
			Graphics2D g2d = (Graphics2D) g;
			
			LinearGradientPaint gradient = new LinearGradientPaint(
				0.0f, 0.0f, 0.0f, this.getHeight(),
				new float[] {0.0f, 0.5f, 1.0f},
				new Color[] {new Color(20,20,20), new Color(50,50,50), new Color(10,10,10) }
			);
			
			g2d.setPaint( gradient );
			g2d.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		}
		
		else super.paintComponent( g );
	}
}
