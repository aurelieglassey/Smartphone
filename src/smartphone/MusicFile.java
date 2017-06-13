package smartphone;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;

/**
 * Cette classe permet de stocker les informations d'un fichier son. Les formats
 * de métadonnées reconnus sont le INFO-LIST et les tags ID3.
 * @author Fabien Terrani
 */
public class MusicFile extends File
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * Chaîne à afficher si le titre n'est pas connu.
	 */
	private static final String UNKNOWN_TITLE = "<Titre inconnu>";
	
	/**
	 * Chaîne à afficher si l'artiste n'est pas connu.
	 */
	private static final String UNKNOWN_ARTIST = "<Artiste inconnu>";
	
	/**
	 * Titre.
	 */
	private String title;
	
	/**
	 * Artiste.
	 */
	private String artist;
	
	/**
	 * Album.
	 */
	private String album;
	
	/**
	 * Date de copyright.
	 */
	private String copyrightDate;
	
	/**
	 * Numéro de piste.
	 */
	private String track;
	
	/**
	 * Crée un nouveau fichier son
	 * @param parent Répertoire parent
	 * @param child Nom du fichier
	 */
	public MusicFile( File parent, String child )
	{
		super( parent, child );
		init();
	}
	
	/**
	 * Crée un nouveau fichier son
	 * @param parent Chemin du répertoire parent
	 * @param child Nom du fichier
	 */
	public MusicFile( String parent, String child )
	{
		super( parent, child );
		init();
	}
	
	/**
	 * Crée un nouveau fichier son
	 * @param pathname Chemin vers le fichier
	 */
	public MusicFile( String pathname )
	{
		super( pathname );
		init();
	}
	
	/**
	 * Initialise les champs des métadonnées à des valeurs par défaut et
	 * recherche les valeurs présentes dans le fichier son.
	 */
	private void init()
	{
		title = UNKNOWN_TITLE;
		artist = UNKNOWN_ARTIST;
		album = "";
		copyrightDate = "";
		track = "";
		
		fetchMetadata();
	}
	
	/**
	 * Lit le fichier son et enregistre les métadonnées dans les champs de la classe.
	 */
	private void fetchMetadata()
	{
		boolean id3Found = false;
		
		try
		{
			InputStream fis = new BufferedInputStream( new FileInputStream( this ) );
			RIFFReader rr = new RIFFReader( fis );
			
			while (rr.hasNextChunk() && !id3Found)
			{
				RIFFReader chunk = rr.nextChunk();
				
				
				
				// Si on tombe sur le chunk LIST et qu'il est de type INFO, on lit les tags
				if ( chunk.getFormat().equals("LIST") && chunk.getType().equals("INFO") )
				{
					// ATTENTION ! LA longueur annoncée par LIST ne compte pas les caractères NULL de padding à la fin des tags.
					// Ces caractères semblent être ajoutés si le nombre de caractères de la valeur plus le NULL placé à la fin est impair.
					String key, value;
					long len;
					
					String[] infoReadTags = {"INAM","IPRD","IART","ICRD","ITRK"};
					
					// Trie les noms de tags pris en compte pour les futurs appels à binarySearch()
					Arrays.sort( infoReadTags );
					
					while ( chunk.available() > 0 )
					{
						// On lit 4 bytes (longueur standard pour le nom des tags LIST(INFO)
						key = chunk.readString(4);
						
						// Nombre de bytes à lire pour avoir la valeur du champ (y compris le caractère NUL en fin de valeur)
						len = chunk.readUnsignedInt();
						
						if ( Arrays.binarySearch( infoReadTags, key ) >= 0 )
						{
							// readString() lit le nombre de bytes voulu puis retourne la chaîne trouvée en s'arrêtant au premier
							// caractère NUL rencontré
							value = chunk.readString( (int) len );
							
							// On affiche uniquement les clefs qu'on connaît
							switch( key )
							{
								case "INAM": title = value; break;
								case "IPRD": album = value; break;
								case "IART": artist = value; break;
								case "ICRD": copyrightDate = value; break;
								case "ITRK": track = value; break;
							}
						}
						
						else
						{
							chunk.skipBytes( (int) len );
						}
						
						// Pour une raison étrange, si la longueur de la valeur et de son caractère NUL de padding est impair,
						// un caractère NUL supplémentaire est ajouté... Il faut donc l'ignorer.
						if (len % 2 != 0) chunk.read();
					}
				}
				
				if ( chunk.getFormat().toLowerCase().equals("id3 ") )
				{
					id3Found = true;
					String id3Magic = chunk.readString(3);
					
					byte[] id3Version = new byte[2];
					byte id3Flags = 0;
					byte[] id3TagSize = new byte[4];

					chunk.readFully( id3Version );
					id3Flags = chunk.readByte();
					chunk.readFully( id3TagSize );

					boolean unsynchronized = ((id3Flags & 0b10000000) != 0);
					boolean extendedHeader = ((id3Flags & 0b01000000) != 0);
					boolean crcPresent = ((id3Flags & 0b00100000) != 0);
					boolean restrictedTags = ((id3Flags & 0b00010000) != 0);
					
					int tagSize = (id3TagSize[0] << 21) | (id3TagSize[1] << 14) | (id3TagSize[2] << 7) | id3TagSize[3];

					//System.out.println( "Partie fixe: \"" + id3Magic + "\"" );
					//System.out.println( "Version: " + id3Version[0] + "." + id3Version[1] );
					//System.out.println( "Unsynchronized ? " + unsynchronized );
					//System.out.println( "Extended header ? " + extendedHeader );
					//System.out.println( "CRC present ? " + crcPresent );
					//System.out.println( "Restricted tags ? " + restrictedTags );
					//System.out.println( "Tag size in bytes : " + tagSize ); // Taille total du tag ID3
					
					if ( id3Magic.equals("ID3") && !extendedHeader )
					{
						String[] id3ReadTags = {"TPE1","TALB","TIT2","TDRC"};
						
						// Trie les noms de tags pris en compte pour les futurs appels à binarySearch()
						Arrays.sort( id3ReadTags );
						
						boolean paddingReached = false;
						
						while ( chunk.available() >= 11 && !paddingReached ) // A frame must be at least 1 byte long, plus the 10 bytes-long header
						{
							String id3FrameId = chunk.readString( 4 );
							
							byte[] id3FrameSize = new byte[4];
							chunk.readFully( id3FrameSize );
							int frameSize = (id3FrameSize[0] << 21) | (id3FrameSize[1] << 14) | (id3FrameSize[2] << 7) | id3FrameSize[3];
							
							
	
							byte[] id3FrameFlags = new byte[2];
							chunk.readFully( id3FrameFlags );
							
							//boolean preserveIfTagsAltered = ((id3FrameFlags[0] & 0b01000000) != 0);
							//boolean preserveIfFileAltered = ((id3FrameFlags[0] & 0b00100000) != 0);
							//boolean readOnlyFrame = ((id3FrameFlags[0] & 0b00010000) != 0);
							//boolean frameInGroup = ((id3FrameFlags[1] & 0b01000000) != 0);
							boolean zlibCompressed = ((id3FrameFlags[1] & 0b00001000) != 0);
							boolean frameEncrypted = ((id3FrameFlags[1] & 0b00000100) != 0);
							//boolean framWasUnsynchronized = ((id3FrameFlags[1] & 0b00000010) != 0);
							//boolean dataLengthIndicator = ((id3FrameFlags[1] & 0b00000001) != 0);
							
							
							
							
							if ( id3FrameId.equals("") ) // RIFFReader returns "" if a string containing only NUL chars is read
							{
								paddingReached = true;
							}
							
							else if ( id3FrameId.startsWith("T") && !zlibCompressed && !frameEncrypted )
							{
								// On récupère la valeur du tag s'il fait partie des tags pris en compte
								if ( Arrays.binarySearch(id3ReadTags, id3FrameId) >= 0 )
								{
									byte encoding = chunk.readByte();
									
									byte[] id3FrameContent = new byte[ frameSize-1 ];
									chunk.readFully( id3FrameContent );
									
									String enc = null;
									
									switch (encoding)
									{
										case 0: enc = "ISO-8859-1"; break;
										case 1: enc = "UTF-16LE"; break;
										case 2: enc = "UTF-16BE"; break;
										case 3: enc = "UTF-8"; break;
									}
									
									String value = new String( id3FrameContent, enc );
									
									switch (id3FrameId)
									{
										case "TIT2": title = value; break;
										case "TPE1": artist = value; break;
										case "TALB": album = value; break;
										case "TDRC": copyrightDate = value; break;
										case "TRCK": track = value; break;
									}
								}
								
								else
								{
									chunk.skipBytes( frameSize );
								}
							}
							
							else
							{
								// On saute le tag s'il n'est pas reconnu
								chunk.skipBytes( frameSize );
							}
						}
					}
				}
				
				chunk.close();
			}
			
			rr.close();
			fis.close();
		}
		
		catch (Exception e)
		{
			// Erreur lors de la récupération des métadonnées
		}
	}
	
	/**
	 * Retourne les métadonnées du fichier son (titre et artiste) ou le nom du fichier
	 * si ces informations sont manquantes.
	 */
	public String toString()
	{
		if ( title == UNKNOWN_TITLE || artist == UNKNOWN_ARTIST )
			return getName();
		else
			return getSongInfo();
	}
	
	/**
	 * Retourne l'artiste et le titre du fichier son.
	 * @return L'artiste et le titre du fichier son.
	 */
	public String getSongInfo()
	{
		return getSongInfo( true );
	}
	
	/**
	 * Retourne l'artiste et le titre du fichier son dans l'ordre défini par artisteFirst.
	 * @param artistFirst Retourne l'artiste puis le titre si TRUE, ou le titre puis l'artiste si FALSE.
	 * @return Une chaîne contenant le titre et l'artiste, dans l'ordre déterminé par artistFirst.
	 */
	public String getSongInfo( boolean artistFirst )
	{
		if ( artistFirst )
			return (artist + " - " + title);
		else
			return (title + " - " + artist);
	}
	
	/**
	 * Retourne les informations sur l'album du fichier son.
	 * @return Une chaîne contenant le nom de l'album et l'année de copyright entre parenthèses.
	 */
	public String getAlbumInfo()
	{
		String info = null;
		
		if (album != "")
		{
			info = album;
			
			if ( copyrightDate != "")
				info += " (" + copyrightDate + ")";
		}
		
		return info;
	}

	/**
	 * Retourne le titre du fichier son.
	 * @return Le titre du fichier son.
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * Retourne l'artiste du fichier son.
	 * @return Le nom de l'artiste du fichier son.
	 */
	public String getArtist()
	{
		return artist;
	}

	/**
	 * Retourne l'album du fichier son.
	 * @return Le nom de l'album du fichier son, ou une chaîne vide
	 * s'il n'a pas été trouvé.
	 */
	public String getAlbum()
	{
		return album;
	}
	
	/**
	 * Retourne la date de copyright du fichier son.
	 * @return La date de copyright du fichier son, ou une chaîne vide
	 * si elle n'a pas pu être trouvée.
	 */
	public String getCopyrightDate()
	{
		return copyrightDate;
	}
	
	/**
	 * Retourne le numéro de piste du fichier son.
	 * @return Le numéro de piste du fichier son, ou une chaîne vide
	 * s'il n'a pas été trouvé.
	 */
	public String getTrack()
	{
		return album;
	}
}
