package smartphone;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GalleryApp extends AbstractApp
{
	File folder;
	ArrayList<File> imageFiles;
	ArrayList<BufferedImage> bImages;
	FileNameExtensionFilter filter;
	
	public GalleryApp( Smartphone phone )
	{
		super( phone, "Gallery app" );
		
		this.folder = this.phone.getImageFolder();
		this.filter = new FileNameExtensionFilter(
			"Fichiers image",
			ImageIO.getReaderFileSuffixes()
		);
		
		// On crée un FileFilter anonyme basé sur FileNameExtensionFilter (qui n'implémente pas FileFilter...)
		this.imageFiles = new ArrayList<File>(
			Arrays.asList(
				folder.listFiles( new FileFilter()
				{
					public boolean accept(File pathname)
					{
						return GalleryApp.this.filter.accept( pathname );
					}
				})
			)
		);
		
		this.bImages = new BufferedImage[ this.imageFiles.length ];
		
		for ( File imageFile : this.imageFiles )
		{
			try
			{
				BufferedImage bimg = ImageIO.read( imageFile );
			}
			
			catch (IOException e)
			{
				
			}
		}
		
		this.panel.setLayout( new FlowLayout() );
	}
}
