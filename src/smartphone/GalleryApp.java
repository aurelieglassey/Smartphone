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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageFilter;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
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

	private static final String PROP_IMAGEFILE = "imageFile";
	
	private static final String ACTION_ZOOM_IMAGE = "zoomImage";
	private static final String ACTION_ADD_TO_CONTACT = "addToContact";
	private static final String ACTION_ASSOCIATE_TO_CONTACT = "associateToContact";
	private static final String ACTION_DELETE_IMAGE = "deleteImage";
	private static final String ACTION_MODIFY_IMAGE = "modifyImage";
	private static final String ACTION_APPLY_FILTER = "applyFilter";
	private static final String ACTION_SAVE_IMAGE = "saveImage";
	private static final String ACTION_RETURN = "return";
	
	

	private JPanel zoomPanel = null;
	private File chosenImage = null;
	
	private JPanel modifyPanel;
	private ImageButton chosenImageButton;
	private JLabel zoomLabel;
	private JLabel previewLabel;
	private Image preview;
	private HashMap<String,ImageFilter> imgFilters;
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
		
		this.mainPanel.setBackground( Smartphone.getBackgroundColor() );
		this.mainPanel.setLayout( new BorderLayout() );
		
		final JLabel loading = new JLabel("Loading gallery...");
		loading.setFont( Smartphone.getSmartFont("large") );
		loading.setForeground( Color.WHITE );
		loading.setPreferredSize( this.phone.getScreenSize() );
		loading.setHorizontalAlignment( SwingConstants.CENTER );
		loading.setVerticalAlignment( SwingConstants.CENTER );
		this.mainPanel.add( loading, BorderLayout.CENTER );
		
		Runnable loadGallery = new Runnable()
		{
			public void run()
			{
				System.out.println("Loading gallery...");
				loadFromFiles();
				GalleryApp.this.mainPanel.remove( loading );
				GalleryApp.this.mainPanel.revalidate();
				
				System.out.println("Done! :)");
			}
		};
		
		Thread loadingThread = new Thread( loadGallery );
		loadingThread.start();
		
		initImageFilters();
	}

	private void loadFromFiles()
	{
		this.folder = this.phone.getImageFolder();
		this.imageButtons = new ArrayList<ImageButton>();
		
		thumbMargin = 20;
		scrollBarWidth = Smartphone.getScrollBarWidth();
		
		thumbWidth = (int) Math.round( (this.phone.getScreenSize().getWidth()-scrollBarWidth - 4 * thumbMargin) / 3 );
		thumbHeight = thumbWidth;
		
		File[] foundImageFiles = this.folder.listFiles( GalleryApp.imageFilter );
		BufferedImage bimg;
		
		for ( File galleryFile : foundImageFiles )
		{
			try
			{
				bimg = ImageIO.read( galleryFile );
				
				ImageButton imgButton = new ImageButton( bimg, thumbWidth, thumbHeight );
				imgButton.putClientProperty(PROP_IMAGEFILE, galleryFile );
				imgButton.setActionCommand( ACTION_ZOOM_IMAGE );
				imgButton.addActionListener( this );
				this.imageButtons.add( imgButton );
			}
			
			catch (IOException e)
			{
				// Problème pendant la création d'un fichier image
				System.out.println( "Problème lors de la lecture de l'image : " + galleryFile );
			}
		}
		
		imgPanel.setBackground( Smartphone.getBackgroundColor() );
		imgPanel.setLayout( new FlowLayout( FlowLayout.CENTER, this.thumbMargin, this.thumbMargin ));
		
		for (ImageButton imgBtn : this.imageButtons)
		{
			imgPanel.add( imgBtn );
		}
		
		setPreferredGallerySize();
		
		JScrollPane pane = new SmartScrollPane( imgPanel );
		pane.setPreferredSize( this.phone.getScreenSize() );
		this.mainPanel.add( pane, BorderLayout.CENTER );
	}
	
	private void initImageFilters()
	{
		imgFilters = new HashMap<String, ImageFilter>();
		
		imgFilters.put("Image originale", null);
		imgFilters.put("Noir et blanc", new GrayFilter());
		imgFilters.put("Sépia", new SepiaFilter());
		imgFilters.put("Contraste", new ContrastFilter());
		imgFilters.put("Saturation", new SaturateFilter());
		
		ArrayList<String> a = new ArrayList<>( imgFilters.keySet() );
		Comparator<String> sortFilters = new Comparator<String>()
		{
			public int compare(String a, String b)
			{
				if (a == "Image originale")
					return -1;
				
				return 0;
			}
			
		};
		
		a.sort( sortFilters );
		
		filterBox = new JComboBox<>( a.toArray( new String[0] ) );
		filterBox.setFont( Smartphone.getSmartFont("medium") );
		filterBox.setActionCommand( ACTION_APPLY_FILTER );
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

	public void actionPerformed( ActionEvent e )
	{
		String command = e.getActionCommand();
		
		if ( command.equals(ACTION_APPLY_FILTER) )
		{
			String selectedFilter = filterBox.getItemAt( filterBox.getSelectedIndex() );
			ImageFilter filter = imgFilters.get( selectedFilter );
			
			if (filter != null)
			{
				System.out.println( "Selected filter: " + filter.getClass().getSimpleName() );
				Image modified = Utils.applyImageFilter( preview, filter );
				previewLabel.setIcon( new ImageIcon(modified) );
			}
			
			else
			{
				previewLabel.setIcon( new ImageIcon(preview) );
			}
		}

		else if ( command.equals(ACTION_ZOOM_IMAGE) )
		{
			for ( ImageButton imgButton : this.imageButtons )
			{
				if ( e.getSource() == imgButton )
				{
					chosenImage = (File) imgButton.getClientProperty( PROP_IMAGEFILE );
					break;
				}
			}
			
			if ( chosenImage != null )
			{
				zoomPanel = new JPanel();
				zoomPanel.setLayout( new BorderLayout() );
				zoomPanel.setBackground( Smartphone.getBackgroundColor() );
				zoomPanel.setBackground( Color.RED );
				
				ImageIcon icon = new ImageIcon( chosenImage.toString() );
				zoomLabel  = new JLabel( icon );
				zoomLabel.setBackground( Color.GREEN );
				
				Dimension imgSize = new Dimension(
					icon.getIconWidth(),
					icon.getIconHeight()
				);
				zoomLabel.setPreferredSize( imgSize );
				
				JScrollPane pane = new SmartScrollPane( zoomLabel );
				pane.setBorder( null );
				pane.setBackground( Smartphone.getBackgroundColor() );
				pane.setBackground( Color.BLUE );
				
				/*Dimension screenSize = this.phone.getScreenSize();
				Dimension paneSize = new Dimension(200, 200);
				pane.setPreferredSize( paneSize );
				
				JPanel p = new JPanel();
				p.add( pane );*/
				
				zoomPanel.add( pane, BorderLayout.CENTER );
				
				
				
				JButton modify = new SmartButton("Modify");
				modify.setActionCommand( ACTION_MODIFY_IMAGE );
				modify.addActionListener( this );
				
				JButton imageToContact = new SmartButton("Add to contact...");
				imageToContact.setActionCommand( ACTION_ADD_TO_CONTACT );
				imageToContact.addActionListener( this );
				
				JPanel buttons = new JPanel();
				buttons.setBackground( Smartphone.getBackgroundColor() );
				buttons.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10) );
				buttons.add( modify );
				buttons.add( imageToContact );
				
				zoomPanel.add( buttons, BorderLayout.SOUTH );
				
				pushPanel( zoomPanel );
			}
		}
		
		else if ( command.equals(ACTION_MODIFY_IMAGE) )
		{
			try
			{
				preview = getModifyPreview( ImageIO.read( chosenImage ) );
			}
			catch( Exception ex )
			{
				preview = null;
				return;
			}
			
			modifyPanel = new JPanel();
			modifyPanel.setLayout( new BorderLayout() );
			modifyPanel.setBackground( Smartphone.getBackgroundColor() );
			
			previewLabel = new JLabel( new ImageIcon( preview ) );
			
			//modifyPanel.setLayout( new BoxLayout( modifyPanel, BoxLayout.Y_AXIS) );
			
			//imgLabel.setAlignmentX( Component.CENTER_ALIGNMENT );
			modifyPanel.add( previewLabel, BorderLayout.CENTER );
			
			JButton save = new SmartButton("Save");
			save.setActionCommand( ACTION_SAVE_IMAGE );
			save.addActionListener( this );
			
			JButton cancel = new SmartButton("Cancel");
			cancel.setActionCommand( ACTION_RETURN );
			cancel.addActionListener( this );
			
			JPanel p = new JPanel();
			p.setBackground( Smartphone.getBackgroundColor() );
			p.setLayout( new FlowLayout( FlowLayout.CENTER, 20, 100 ) );
			p.add( filterBox );
			p.add( cancel );
			p.add( save );
			
			//p.setAlignmentX( Component.CENTER_ALIGNMENT );
			modifyPanel.add( p, BorderLayout.SOUTH );
			
			pushPanel( modifyPanel );
		}
		
		else if ( command.equals(ACTION_DELETE_IMAGE) )
		{
			// TODO
		}
		
		else if ( command.equals(ACTION_SAVE_IMAGE) )
		{
			try
			{
				BufferedImage bimg = ImageIO.read( chosenImage );
				
				String selectedFilter = filterBox.getItemAt( filterBox.getSelectedIndex() );
				ImageFilter filter = imgFilters.get( selectedFilter );
				
				Image modifiedImage = Utils.applyImageFilter( bimg, filter );
				
				bimg = new BufferedImage(
					modifiedImage.getWidth(null),
					modifiedImage.getHeight(null),
					BufferedImage.TYPE_INT_ARGB
				);
				
				bimg.getGraphics().drawImage(modifiedImage, 0, 0, null);
				
				ImageIO.write( bimg, "PNG", chosenImage );
				
				zoomLabel.setIcon( new ImageIcon(bimg) );
				returnPressed();
			}
			
			catch (Exception ex)
			{
				ex.printStackTrace();
				return;
			}
			
			returnPressed();
		}

		else if ( command.equals(ACTION_ADD_TO_CONTACT) )
		{
			AbstractApp app = this.phone.getAppInstance( ContactApp.class );
			
			if (app != null && app instanceof ContactApp)
			{
				ContactApp contactApp = (ContactApp) app;
				
				JPanel addToContact = new JPanel();
				addToContact.setLayout( new BorderLayout() );
				
				JList<Contact> contactList = new SmartList<Contact>();
				contactList.setListData(
					contactApp.getContactlist().toArray( new Contact[0] )
				);
				contactList.setCellRenderer( new ContactListCellRenderer() );
				contactList.addMouseListener( new MouseAdapter()
				{
					public void mouseClicked( MouseEvent e )
					{
						if (e.getClickCount() == 2 && !contactList.getValueIsAdjusting() )
						{
							ActionEvent event = new ActionEvent(
								contactList,
								ActionEvent.ACTION_PERFORMED,
								ACTION_ASSOCIATE_TO_CONTACT
							);
							
							actionPerformed( event );
						}
					}
				});
				
				addToContact.add( contactList, BorderLayout.CENTER );
				
				pushPanel( addToContact );
				
				//contactApp.associate(chosenImage, c);
			}
		}
		
		else if ( command.equals(ACTION_ASSOCIATE_TO_CONTACT) )
		{
			System.out.println("association..." );
			
			String content = "Erreur lors de l'association de l'image au contact !";
			
			
			if ( e.getSource() instanceof JList<?> )
			{
				JList<Contact> contactList = (JList<Contact>) e.getSource();
				
				Contact c = contactList.getSelectedValue();
				AbstractApp app = this.phone.getAppInstance( ContactApp.class );
				
				if (app != null && app instanceof ContactApp)
				{
					ContactApp contactApp = (ContactApp) app;
					
					contactApp.associate( chosenImage, c );
					
					content = "Contact associé !";
				}
			}
			
			JPanel message = new JPanel();
			message.setLayout( new GridLayout(2,1) );
			message.setBackground( Smartphone.getBackgroundColor() );
			
			JLabel info = new JLabel( content );
			info.setFont( Smartphone.getSmartFont("large"));
			info.setForeground( Color.WHITE );
			info.setHorizontalAlignment( SwingConstants.CENTER );
			
			JButton pop = new SmartButton("Ok");
			pop.setActionCommand( ACTION_RETURN );
			pop.addActionListener( this );

			message.add( info );
			message.add( pop );
			
			popPanel();
			pushPanel( message );
		}
		
		else if ( command.equals(ACTION_RETURN) )
		{
			returnPressed();
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
