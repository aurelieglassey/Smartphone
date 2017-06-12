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
import javax.swing.SwingConstants;

public class DummyApp extends AbstractApp implements ActionListener
{
	private ArrayList<JPanel> panels = new ArrayList<>();
	
	public DummyApp( Smartphone phone )
	{
		super( phone, "Dummy app", "dummy" );
	}
	
	public void returnPressed()
	{
		JPanel removed = popPanel();
		panels.remove( removed );
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
		p.setLayout( new BorderLayout() );
		
		JLabel lab = new JLabel( "" + (panels != null ? panels.size() : 0) );
		lab.setFont( new Font("Arial", Font.PLAIN, 300 ) );
		lab.setForeground( Color.WHITE );
		lab.setHorizontalAlignment( SwingConstants.CENTER );

		JButton btnPush = new SmartButton("Push");
		btnPush.setActionCommand("push");
		btnPush.addActionListener( this );
		
		JButton btnPop = new SmartButton("Pop");
		btnPop.setActionCommand("pop");
		btnPop.addActionListener( this );
		
		JPanel south = new JPanel();
		south.setBackground( p.getBackground() );
		south.setLayout( new FlowLayout( FlowLayout.CENTER, 20, 200 ) );
		south.add( btnPush );
		south.add( btnPop );
		
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

	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();
		
		switch( command )
		{
			case "push":
				JPanel p = makePanel();
				pushPanel( p );
				break;
			
			case "pop":
				returnPressed();
				break;
		}
	}
}
