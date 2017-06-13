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

/**
 * Cette classe représente une application de démonstration, utilisée
 * pour tester la génération et le retrait de panels des applications.
 * @author Fabien Terrani
 */
public class DummyApp extends AbstractApp implements ActionListener
{
	/**
	 * Ensemble des panels de test affichés par l'application.
	 */
	private ArrayList<JPanel> panels = new ArrayList<>();
	
	/**
	 * Crée l'application et la connecte au téléphone passé en paramètre.
	 * @param phone L'objet Smartphone sur lequel l'application est installée
	 */
	public DummyApp( Smartphone phone )
	{
		super( phone, "Dummy app", "dummy" );
	}
	
	/**
	 * Gère l'appui sur le bouton Retour
	 */
	public void returnPressed()
	{
		JPanel removed = popPanel();
		panels.remove( removed );
	}
	
	/**
	 * Crée un JPanel avec une couleur de fond aléatoire et un numéro indiquant
	 * la profondeur du panel.
	 * @return Le nouveau JPanel créé
	 */
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
	
	/**
	 * Génère le panel racine de l'application.
	 */
	public JPanel generateMainPanel()
	{
		return makePanel();
	}
	
	/**
	 * Ajoute un nouveau panel sur la pile de panels de l'application.
	 * Méthode surchargée pour ajouter le panel au tableau panels.
	 */
	public void pushPanel( JPanel p )
	{
		panels.add( p );
		super.pushPanel( p );
	}
	
	/**
	 * Méthode de gestion des clics sur les boutons.
	 */
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
