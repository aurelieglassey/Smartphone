package smartphone;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GalleryApp extends AbstractApp implements ActionListener
{
	File folder;
	ArrayList<ImageButton> imageButtons;
	
	JPanel imgPanel = new JPanel();
	
	private int thumbMargin;
	private int thumbWidth;
	private int thumbHeight;
	private int scrollBarWidth;

	private JPanel zoomPanel = null;
	private Image chosenImage = null;
	private JButton modify = null;
	
	private JPanel modifyPanel = null;
	private JLabel imgLabel = null;
	private Image preview = null;
	private HashMap<String,ImageFilter> imgFilters = new HashMap<>();
	private JComboBox<String> filterBox;
	
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
		
		this.folder = this.phone.getImageFolder();
		this.imageButtons = new ArrayList<ImageButton>();
		
		thumbMargin = 20;
		scrollBarWidth = this.phone.getScrollBarWidth();
		
		thumbWidth = (int) Math.round( (this.phone.getScreenSize().getWidth()-scrollBarWidth - 4 * thumbMargin) / 3 );
		thumbHeight = thumbWidth;
		
		File[] foundImageFiles = this.folder.listFiles( GalleryApp.imageFilter );
		
		for ( File galleryFile : foundImageFiles )
		{
			try
			{
				BufferedImage bimg = ImageIO.read( galleryFile );
				
				ImageButton imgButton = new ImageButton( bimg, thumbWidth, thumbHeight );
				imgButton.addActionListener( this );
				this.imageButtons.add( imgButton );
			}
			
			catch (IOException e)
			{
				// Problème pendant la création d'un fichier image
				System.out.println( "Problème lors de la lecture de l'image : " + galleryFile );
			}
		}
		
		imgPanel.setBackground( this.phone.getBackgroundColor() );
		imgPanel.setLayout( new FlowLayout( FlowLayout.CENTER, this.thumbMargin, this.thumbMargin ));
		
		for (ImageButton imgBtn : this.imageButtons)
		{
			imgPanel.add( imgBtn );
		}
		
		setPreferredGallerySize();
		
		JScrollPane pane = new JScrollPane( imgPanel );
		pane.getVerticalScrollBar().setPreferredSize( new Dimension(this.phone.getScrollBarWidth(), 0) );
		pane.setBackground( this.phone.getBackgroundColor() );
		pane.setBorder( null );
		pane.setPreferredSize( this.phone.getScreenSize() );
		this.mainPanel.add( pane );
		
		imgFilters.put("Image originale", null);
		imgFilters.put("Noir et blanc", new GrayFilter());
		imgFilters.put("Sépia", new SepiaFilter());
		imgFilters.put("Contraste", new ContrastFilter());
		imgFilters.put("Saturation", new SaturateFilter());
		
		String[] keys = new String[0];
		keys = imgFilters.keySet().toArray( keys );
		
		
		
		for (String k : keys) System.out.println(k);
		
		filterBox = new JComboBox<>( keys );
		filterBox.addActionListener( this );
	}
	
	private void setPreferredGallerySize()
	{
		int thumbsPerRow = (int) this.phone.getScreenSize().getWidth() / this.thumbWidth;
		
		this.imgPanel.setPreferredSize( new Dimension(
			(int) this.phone.getScreenSize().getWidth()-scrollBarWidth,
			(this.imageButtons.size() / thumbsPerRow + 1) * (this.thumbHeight+2 + this.thumbMargin) + this.thumbMargin
		));
	}
	
	public JPanel generateMainPanel()
	{
		return new JPanel();
	}
	
	public void returnPressed()
	{
		JPanel popped = popPanel();
		
		if ( popped == modifyPanel )
		{
			modifyPanel = null;
		}
		
		else if ( popped == zoomPanel )
		{
			zoomPanel = null;
			chosenImage = null;
		}
	}
	
	private Image getModifyPreview( Image img )
	{
		float resizeFactor = 1.0f;

		int w = img.getWidth( null );
		int h = img.getHeight( null );
		int modifyWidth = this.phone.getScreenSize().width - 20;
		
		if ( w > modifyWidth )
		{
			resizeFactor = ((float) modifyWidth) / w;
		}
		
		Image preview = img.getScaledInstance(
			(int)(resizeFactor * w),
			(int)(resizeFactor * h),
			Image.SCALE_FAST
		);
		
		return preview;
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( e.getSource() == filterBox )
		{
			String selectedFilter = filterBox.getItemAt( filterBox.getSelectedIndex() );
			ImageFilter filter = imgFilters.get( selectedFilter );
			
			if (filter != null)
			{
				System.out.println( "Selected filter: " + filter.getClass().getSimpleName() );
				Image modified = Utils.applyImageFilter( preview, filter );
				imgLabel.setIcon( new ImageIcon(modified) );
			}
			
			else
			{
				imgLabel.setIcon( new ImageIcon(preview) );
			}
		}
		
		else if ( e.getSource() == modify )
		{
			modifyPanel = new JPanel();
			
			preview = getModifyPreview( chosenImage );
			
			imgLabel = new JLabel( new ImageIcon( preview ) );
			
			modifyPanel.setLayout( new BoxLayout( modifyPanel, BoxLayout.Y_AXIS) );
			
			imgLabel.setAlignmentX( Component.CENTER_ALIGNMENT );
			modifyPanel.add( imgLabel );
			
			JPanel p = new JPanel();
			p.setLayout( new FlowLayout( FlowLayout.CENTER, 5, 5 ) );
			p.add( filterBox );
			
			p.setAlignmentX( Component.CENTER_ALIGNMENT );
			modifyPanel.add( p );
			
			pushPanel( modifyPanel );
		}
		
		else
		{
			for ( ImageButton imgButton : this.imageButtons )
			{
				if ( e.getSource() == imgButton )
				{
					chosenImage = imgButton.getImage();
					break;
				}
			}
			
			if ( chosenImage != null )
			{
				zoomPanel = new JPanel();
				zoomPanel.setBackground( this.phone.getBackgroundColor() );
				
				Dimension imgSize = new Dimension(
					chosenImage.getWidth(null),
					chosenImage.getHeight(null)
				);
				
				JLabel imgLabel = new JLabel( new ImageIcon(chosenImage) );
				imgLabel.setPreferredSize( imgSize );
				
				JScrollPane pane = new JScrollPane( imgLabel );
				pane.setBorder( null );
				pane.setBackground( this.phone.getBackgroundColor() );
				
				//pane.setPreferredSize( this.phone.getScreenSize() );
				
				zoomPanel.add( pane, BorderLayout.CENTER );
				
				modify = new SmartButton("Modify");
				modify.addActionListener( this );
				zoomPanel.add( modify, BorderLayout.NORTH );
				
				pushPanel( zoomPanel );
			}
		}
	}
	
	private static class GrayFilter extends RGBImageFilter
	{
		public int filterRGB(int x, int y, int rgb)
        {
        	int a = ((rgb & 0xff000000) >> 24);
        	int r = ((rgb & 0xff0000) >> 16);
        	int g = ((rgb & 0xff00) >> 8);
        	int b = (rgb & 0xff);

        	int avg = Math.min( 255, (r+g+b)/3 );
        	
        	rgb = ((a<<24) | (avg<<16) | (avg<<8) | avg);
        	
        	return rgb;
        }
	}

	private static class SepiaFilter extends GrayFilter
	{
		public int filterRGB(int x, int y, int rgb)
        {
        	rgb = super.filterRGB( x, y, rgb );
        	
        	int a = ((rgb & 0xff000000) >> 24);
        	int r = ((rgb & 0xff0000) >> 16);
        	int g = ((rgb & 0xff00) >> 8);
        	int b = (rgb & 0xff);

        	r = Math.min( 255, (r+50) );
        	g = Math.min( 255, (g+20) );
        	
        	rgb = ((a<<24) | (r<<16) | (g<<8) | b);
        	
        	return rgb;
        }
	}

	private static class ContrastFilter extends RGBImageFilter
	{
		public int filterRGB(int x, int y, int rgb)
        {
        	int a = ((rgb & 0xff000000) >> 24);
        	int r = ((rgb & 0xff0000) >> 16);
        	int g = ((rgb & 0xff00) >> 8);
        	int b = (rgb & 0xff);
        	

        	r = (int)(linearToSinus(r/255.0) * 255);
        	g = (int)(linearToSinus(g/255.0) * 255);
        	b = (int)(linearToSinus(b/255.0) * 255);
        	
        	rgb = ((a<<24) | (r<<16) | (g<<8) | b);
        	
        	return rgb;
        }
		
		private double linearToSinus( double value )
		{
			return ((Math.sin( value * Math.PI - (Math.PI/2.0) ) + 1.0) / 2.0);
		}
	}
	
	private static class SaturateFilter extends RGBImageFilter
	{
		public int filterRGB(int x, int y, int rgb)
        {
        	int a = ((rgb & 0xff000000) >> 24);
        	int r = ((rgb & 0xff0000) >> 16);
        	int g = ((rgb & 0xff00) >> 8);
        	int b = (rgb & 0xff);
        	

        	r = (r <= 127 ? 0 : 255);
        	g = (g <= 127 ? 0 : 255);
        	b = (b <= 127 ? 0 : 255);
        	
        	rgb = ((a<<24) | (r<<16) | (g<<8) | b);
        	
        	return rgb;
        }
	}
}
