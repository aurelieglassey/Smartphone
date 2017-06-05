package smartphone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DummyApp extends AbstractApp implements ActionListener
{
	private ArrayList<JPanel> panels = new ArrayList<>();
	
	public DummyApp( Smartphone phone )
	{
		super( phone, "Dummy app", "dummy" );
	}
	
	private JPanel makePanel()
	{
		JPanel p = new JPanel();
		Random r = new Random();
		
		p.setBackground( new Color(
			r.nextFloat(),
			r.nextFloat(),
			r.nextFloat(),
			1.0f
		));
		
		JLabel lab = new JLabel( "" + (panels != null ? panels.size() : 0) );
		lab.setFont( new Font("Arial", Font.PLAIN, 60 ) );
		lab.setForeground( Color.WHITE );

		JButton btnPush = new JButton("Push a panel");
		
		btnPush.addActionListener( this );
		
		JPanel south = new JPanel();
		south.setLayout( new FlowLayout() );
		south.add( btnPush );
		
		p.add( south, BorderLayout.SOUTH );
		p.add( lab, BorderLayout.CENTER );
		
		return p;
	}
	
	public JPanel generateMainPanel()
	{
		return makePanel();
	}

	public void pushPanel( JPanel p )
	{
		panels.add( p );
		super.pushPanel( p );
	}
	
	public void popPanel()
	{
		JPanel p = panels.remove( panels.size()-1 );
		super.pushPanel( p );
	}

	public void actionPerformed(ActionEvent e)
	{
		JPanel p = makePanel();
		pushPanel( p );
	}
}
