package smartphone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HomePanel extends JPanel
{
	private Smartphone phone;
	private ArrayList<JButton> appButtons = new ArrayList<>();
	
	private JLabel clock;
	private Timer t;
	private TimerTask refreshClock;
	
	private JPanel appPanel;
	
	public HomePanel( Smartphone phone, JButton[] appButtons )
	{
		this.phone = phone;
		
		if (appButtons != null)
		{
			this.appButtons.addAll( Arrays.asList(appButtons) );
		}
		
		setLayout( new BorderLayout() );
		setBackground( Color.BLACK );
		
		initClock();
		add( clock, BorderLayout.NORTH );
		
		appPanel = new JPanel();
		appPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 50, 50) );
		add( appPanel, BorderLayout.CENTER );
		
		refreshApps();
	}
	
	private void refreshApps()
	{
		appPanel.removeAll();
		
		for ( JButton appButton : appButtons ) add( appButton );
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
					LocalTime.now().format( DateTimeFormatter.ofPattern("HH:mm:ss"))
				);
			}
		};
		
		t = new Timer();
		t.schedule( refreshClock, 0, 1000 );
	}
	
	public void addAppButton( JButton appButton )
	{
		if ( appButton == null ) return;
		
		appButtons.add( appButton );
		refreshApps();
	}
	
	public void paintComponent( Graphics g )
	{
		if (g instanceof Graphics2D)
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
