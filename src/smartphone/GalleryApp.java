package smartphone;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GalleryApp extends AbstractApp
{
	File folder;
	ArrayList<ImageButton> imageButtons;
	
	private int thumbMargin;
	private int thumbWidth;
	private int thumbHeight;
	private int scrollBarWidth;
	
	// Filtre permettant de sélectionner les noms de fichiers ayant une extension prise en charge par ImageIO
	private static final FileNameExtensionFilter extFilter = new FileNameExtensionFilter(
		"Fichiers image",
		ImageIO.getReaderFileSuffixes()
	);
	
	// Filtre permettant de sélectionner les fichiers compatibles avec ImageIO
	private static final FileFilter imageFilter = new FileFilter()
	{
		public boolean accept(File pathname)
		{
			return GalleryApp.extFilter.accept( pathname );
		}
	};
	
	
	
	public GalleryApp( Smartphone phone )
	{
		super( phone, "Gallery app", "gallery" );
		
		this.mainPanel.setBackground( new Color(40, 40, 40, 255) );
		
		this.folder = this.phone.getImageFolder();
		this.imageButtons = new ArrayList<ImageButton>();
		
		thumbMargin = 20;
		scrollBarWidth = this.phone.getScrollBarWidth();
		
		thumbWidth = (int) Math.round( (this.phone.getScreenSize().getWidth()-scrollBarWidth - 4 * thumbMargin) / 3 );
		thumbHeight = thumbWidth;
		
		System.out.println( "Computed thumb size: " + thumbWidth + " x " + thumbHeight );
		
		
		File[] foundImageFiles = this.folder.listFiles( GalleryApp.imageFilter );
		
		for ( File galleryFile : foundImageFiles )
		{
			try
			{
				BufferedImage bimg = ImageIO.read( galleryFile );
				
				this.imageButtons.add(
					new ImageButton( bimg, thumbWidth, thumbHeight )
				);
			}
			
			catch (IOException e)
			{
				// Problème pendant la création d'un fichier image
				System.out.println( "Problème lors de la lecture de l'image : " + galleryFile );
			}
		}
		
		
		
		this.mainPanel.setLayout( new FlowLayout( FlowLayout.CENTER, this.thumbMargin, this.thumbMargin ));
		
		for (ImageButton imgBtn : this.imageButtons)
		{
			this.mainPanel.add( imgBtn );
		}
		
		setPreferredGallerySize();
		
		//this.panel.setLayout( new GridLayout(this.imageButtons.size() / 3 + 1, 3, this.thumbMargin, this.thumbMargin ) );
		
		
		
		/*
		this.panel.setLayout( new BoxLayout( this.panel, BoxLayout.PAGE_AXIS ) );
		ImageButton imgBtn;
		JPanel row;
		
		
		for (int i = 0; i < this.imageButtons.size(); i+=3) //ImageButton imgBtn : this.imageButtons )
		{
			row = new JPanel();
			row.setBackground( new Color( (float) Math.random(), (float) Math.random(), (float) Math.random(), 1.0f) );
			row.setLayout( new FlowLayout(FlowLayout.CENTER, this.thumbMargin, this.thumbMargin) );
			
			for (int k = i; k < (i+3) && k < this.imageButtons.size(); k++)
				row.add( this.imageButtons.get(k) );
			
			this.panel.add(row);
		}*/
	}
	
	private void setPreferredGallerySize()
	{
		int thumbsPerRow = (int) this.phone.getScreenSize().getWidth() / this.thumbWidth;
		
		this.mainPanel.setPreferredSize( new Dimension(
			(int) this.phone.getScreenSize().getWidth()-scrollBarWidth,
			(this.imageButtons.size() / thumbsPerRow + 1) * (this.thumbHeight+2 + this.thumbMargin) + this.thumbMargin
		));
	}
	
	private class GalleryPanel extends JPanel
	{
		public GalleryPanel()
		{
		}
		
		public Dimension getPreferredSize()
		{
			System.out.println( "PreferredSizeAsked" + super.getPreferredSize() );
			return super.getPreferredSize();
		}

		public Dimension getSize()
		{
			System.out.println( "GET size called : " + super.getSize() );
			return super.getSize( );
		}

		public void setSize( Dimension d)
		{
			System.out.println( "SET size called : " + d );
			super.setSize(d );
		}

		public void setPreferredSize( Dimension d)
		{
			System.out.println( "SET PREF size called : " + d );
			super.setPreferredSize(d );
		}
		
		public void paintComponent( Graphics g )
		{
			//System.out.println( ">> " + this.getSize() );
			
			GalleryApp.this.setPreferredGallerySize();
			
			
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
	
	public JPanel generateMainPanel()
	{
		return new GalleryPanel();
	}
}
