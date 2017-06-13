/*
 * Copyright (c) 2007, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package smartphone;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;


/**
 * Décodeur de flux au format RIFF (Resource Interchange File Format).
 * Les données sont découpées en chunks commençant par un nom de 4 caractères
 * suivi de la longueur du chunk codée sur 4 bytes en LITTLE ENDIAN).
 * 
 * Cette classe a été reprise du package com.sun.media.sound et son code n'a pas
 * été modifié.
 * @author Karl Helgason
 */
public final class RIFFReader extends InputStream
{
	/**
	 * Instance racine du décodeur RIFF
	 */
    private final RIFFReader root;
    
    /**
     * Nombre de bytes lu en tout (sans considérer les instances filles)
     */
    private long filepointer = 0;
    
    /**
     * Nom du chunk ("RIFF", "fmt "...)
     */
    private final String fourcc;
    
    /**
     * Type du chunk, pour les chunks RIFF et LIST notamment : ("WAVE", "INFO"...)
     */
    private String riff_type = null;
    
    /**
     * Taille du chunk en bytes
     */
    private long ckSize = Integer.MAX_VALUE;
    
    /**
     * Flux d'entrée en cours de lecture
     */
    private InputStream stream;
    
    /**
     * Nombre de bytes restants à lire dans le chunk en cours
     */
    private long avail = Integer.MAX_VALUE;
    
    /**
     * Contient le chunk suivant s'il existe, null sinon
     */
    private RIFFReader lastiterator = null;
	
	/**
	 * Crée un nouveau RIFFReader à partir d'un InputStream
	 * @param stream Le flux d'entrée
	 * @throws IOException Exception rejetée en cas de problème de lecture
	 */
    public RIFFReader(InputStream stream) throws IOException
    {
		// Une instance RIFFReader peut être créée à partir d'une autre instance RIFFReader
		// (RIFFReader étend InputStream). Dans ce cas, on conserve toujours une référence
		// vers l'instance racine
        if (stream instanceof RIFFReader) {
            root = ((RIFFReader) stream).root;
        } else {
            root = this;
        }

        this.stream = stream;

		// Apparemment, on peut s'attendre à trouver des caractères NUL de padding entre
		// des chunks ou au début du fichier ?
        // Check for RIFF null paddings,
        int b;
        while (true) {
            b = read();
            if (b == -1) {
                fourcc = ""; // don't put null value into fourcc,
                // because it is expected to
                // always contain a string value
                riff_type = null;
                avail = 0;
                return;
            }
            if (b != 0)
                break;
        }

		// On lit les 4 premiers caractères après le padding de NUL
		// Ils donnent le format du chunk
        byte[] fourcc = new byte[4];
        fourcc[0] = (byte) b;
        readFully(fourcc, 1, 3);
        this.fourcc = new String(fourcc, "ascii");
        ckSize = readUnsignedInt();
        avail = ckSize; // avail stocke le nombre de bytes qui n'ont pas encore été lus
		
		// Pour les chunks RIFF et LIST, un type peut être défini
        if (getFormat().equals("RIFF") || getFormat().equals("LIST")) {
            if (avail > Integer.MAX_VALUE) {
                throw new IOException("Chunk size too big");
                //throw new RIFFInvalidDataException("Chunk size too big");
            }
            byte[] format = new byte[4];
            readFully(format);
            this.riff_type = new String(format, "ascii");
        }
    }
	
    /**
     * Retourne le nombre de caractères lu dans le fichier, indépendamment des instances
     * créées pour lire les chunks.
     * @return Le nomre de caractères lu dans le fichier
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public long getFilePointer() throws IOException
    {
        return root.filepointer;
    }
	
	/**
	 * Retourne un booléen permettant de savoir s'il reste un chunk dans le flux.
	 * @return TRUE s'il y a encore des caractères à lire dans le fichier, FALSE sinon
	 * @throws IOException Exception rejetée en cas de problème de lecture
	 */
    public boolean hasNextChunk() throws IOException
    {
        if (lastiterator != null)
            lastiterator.finish();
        return avail != 0;
    }

    /**
     * Retourne une instance RIFFReader pour lire le prochain chunk.
     * @return Une instance RIFFReader pour lire le prochain chunk. L'instance est liée
     * à une instance parente.
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public RIFFReader nextChunk() throws IOException
    {
        if (lastiterator != null)
            lastiterator.finish();
        if (avail == 0)
            return null;
        lastiterator = new RIFFReader(this);
        return lastiterator;
    }
	
	/**
	 * Retourne le format du chunk.
	 * @return Le format du chunk (RIFF, 'fmt ', "data", "LIST", ...)
	 */
    public String getFormat()
    {
        return fourcc;
    }
    
	/**
	 * Retourne le type du chunk
	 * @return Le type du chunk ("WAVE" pour RIFF ou "INFO" pour LIST, null sinon)
	 */
    public String getType()
    {
        return riff_type;
    }
	
    /**
     * Retourne la taille du contenu du chunk en bytes.
     * @return La taille du contenu du chunk en bytes, à partir du byte suivant immédiatement
     * la longueur annoncée dans le fichier. La valeur est directement reprise du fichier
     * (voir code du constructeur)
     */
    public long getSize()
    {
        return ckSize;
    }
    
    /**
     * Lit un byte et le retourne sous forme d'un int allant de 0 à 255. Retourne -1 si la fin du flux
     * a été atteinte.
     */
    public int read() throws IOException
    {
        if (avail == 0) {
            return -1;
        }
        int b = stream.read();
        if (b == -1) {
            avail = 0;
            return -1;
        }
        avail--;
        filepointer++;
        return b;
    }
	
    /**
     * Lit len bytes dans le flux, puis les stocke dans le tableau b en commençant à la cellule offset.
     */
    public int read(byte[] b, int offset, int len) throws IOException
    {
        if (avail == 0) {
            return -1; // On retourne directement -1 s'il n'y a plus de caractères disponibles
        }
        if (len > avail) {
            int rlen = stream.read(b, offset, (int)avail);
            if (rlen != -1)
                filepointer += rlen;
            avail = 0;
            return rlen;
        } else {
            int ret = stream.read(b, offset, len);
            if (ret == -1) {
                avail = 0;
                return -1;
            }
            avail -= ret;
            filepointer += ret;
            return ret;
        }
    }
	
	/**
	 * Remplit b avec les b.length prochains bytes en commençant au début du tableau.
	 * @param b Un tableau de bytes à remplir entièrement avec les prochains bytes du flux
	 * @throws IOException Exception rejetée en cas de problème de lecture
	 */
    public final void readFully(byte b[]) throws IOException
    {
        readFully(b, 0, b.length);
    }
	
    /**
     * Lit les len prochains bytes du flux et les insère dans le tableau b, à partir de
     * la cellule offset.
     * @param b Le tableau de bytes dans lequel stocker les bytes récupérés
     * @param off La cellule à laquelle commencer l'insertion
     * @param len Le nombre de bytes à lire
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public final void readFully(byte b[], int off, int len) throws IOException
    {
        if (len < 0)
            throw new IndexOutOfBoundsException();
        while (len > 0) {
            int s = read(b, off, len);
            if (s < 0)
                throw new EOFException();
            if (s == 0)
                Thread.yield();
            off += s;
            len -= s;
        }
    }
	
	/**
     * Ignore n bytes dans le flux. Se charge d'effectuer des appels multiples
     * à skip si nécessaire.
     * @param n Le nombre de bytes à ignorer
     * @return Le nombre de bytes effectivement ignorés
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public final long skipBytes(long n) throws IOException
    {
        if (n < 0)
            return 0;
        long skipped = 0;
        while (skipped != n) {
            long s = skip(n - skipped);
            if (s < 0)
                break;
            if (s == 0)
                Thread.yield();
            skipped += s;
        }
        return skipped;
    }
	
	/**
     * Ignore n bytes dans le flux. Met à jour les variables avail et filepointer.
     */
    public long skip(long n) throws IOException
    {
        if (avail == 0)
            return -1;
        if (n > avail) {
            long len = stream.skip(avail);
            if (len != -1)
                filepointer += len;
            avail = 0;
            return len;
        } else {
            long ret = stream.skip(n);
            if (ret == -1) {
                avail = 0;
                return -1;
            }
            avail -= ret;
            filepointer += ret;
            return ret;
        }
    }
	
    /**
     * Retourne le nombre de bytes encore non lus dans le flux.
     */
    public int available()
    {
        return (int)avail;
    }
	
    /**
     * Ignore tous les bytes restants dans le flux.
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public void finish() throws IOException
    {
        if (avail != 0) {
            skipBytes(avail);
        }
    }
    
    /**
     * Lit n bytes dans le flux, puis en fait une chaîne de caractères encodée ASCII.
     * Cette méthode retourne la partie de la chaîne allant du tout premier caractère
     * jusqu'à avant le premier caractère NUL rencontré.
     * @param len Le nombre de bytes à lire
     * @return Une chaîne de caractères ASCII constituée des bytes lus jusqu'à la fin ou
     * jusqu'à la position précédant le premier caractère NULL
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public String readString(final int len) throws IOException
    {
        final byte[] buff;
        try {
            buff = new byte[len];
        } catch (final OutOfMemoryError oom) {
            throw new IOException("Length too big", oom);
        }
        readFully(buff);
        for (int i = 0; i < buff.length; i++) {
            if (buff[i] == 0) {
                return new String(buff, 0, i, "ascii");
            }
        }
        return new String(buff, "ascii");
    }
	
    /**
     * Lit un byte dans le flux (signé).
     * @return Un byte en tant qu'entier 8 bits signé
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public byte readByte() throws IOException
    {
        int ch = read();
        if (ch < 0)
            throw new EOFException();
        return (byte) ch;
    }
	
	/**
     * Lit un short dans le flux (signé).
     * @return Un short en tant qu'entier 16 bits signé (2 bytes lus en LITTLE ENDIAN)
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public short readShort() throws IOException
    {
        int ch1 = read();
        int ch2 = read();
        if (ch1 < 0)
            throw new EOFException();
        if (ch2 < 0)
            throw new EOFException();
        return (short)(ch1 | (ch2 << 8));
    }

    /**
     * Lit un int dans le flux (signé).
     * @return Un int en tant qu'entier 32 bits signé (4 bytes lus en LITTLE ENDIAN)
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public int readInt() throws IOException
    {
        int ch1 = read();
        int ch2 = read();
        int ch3 = read();
        int ch4 = read();
        if (ch1 < 0)
            throw new EOFException();
        if (ch2 < 0)
            throw new EOFException();
        if (ch3 < 0)
            throw new EOFException();
        if (ch4 < 0)
            throw new EOFException();
        return ch1 + (ch2 << 8) | (ch3 << 16) | (ch4 << 24);
    }

    /**
     * Lit un long dans le flux (signé).
     * @return Un long en tant qu'entier 64 bits signé (8 bytes lus en LITTLE ENDIAN)
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public long readLong() throws IOException
    {
        long ch1 = read();
        long ch2 = read();
        long ch3 = read();
        long ch4 = read();
        long ch5 = read();
        long ch6 = read();
        long ch7 = read();
        long ch8 = read();
        if (ch1 < 0)
            throw new EOFException();
        if (ch2 < 0)
            throw new EOFException();
        if (ch3 < 0)
            throw new EOFException();
        if (ch4 < 0)
            throw new EOFException();
        if (ch5 < 0)
            throw new EOFException();
        if (ch6 < 0)
            throw new EOFException();
        if (ch7 < 0)
            throw new EOFException();
        if (ch8 < 0)
            throw new EOFException();
        return ch1 | (ch2 << 8) | (ch3 << 16) | (ch4 << 24)
                | (ch5 << 32) | (ch6 << 40) | (ch7 << 48) | (ch8 << 56);
    }
	
    /**
     * Lit un byte dans le flux (non signé).
     * @return Un int en tant qu'entier 8 bits non signé
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public int readUnsignedByte() throws IOException
    {
        int ch = read();
        if (ch < 0)
            throw new EOFException();
        return ch;
    }
    
    /**
     * Lit un short dans le flux (non signé).
     * @return Un int en tant qu'entier 16 bits non signé (2 bytes lus en LITTLE ENDIAN)
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public int readUnsignedShort() throws IOException
    {
        int ch1 = read();
        int ch2 = read();
        if (ch1 < 0)
            throw new EOFException();
        if (ch2 < 0)
            throw new EOFException();
        return ch1 | (ch2 << 8);
    }
    
    /**
     * Lit un int dans le flux (non signé).
     * @return Un long en tant qu'entier 32 bits non signé (4 bytes lus en LITTLE ENDIAN)
     * @throws IOException Exception rejetée en cas de problème de lecture
     */
    public long readUnsignedInt() throws IOException
    {
        long ch1 = read();
        long ch2 = read();
        long ch3 = read();
        long ch4 = read();
        if (ch1 < 0)
            throw new EOFException();
        if (ch2 < 0)
            throw new EOFException();
        if (ch3 < 0)
            throw new EOFException();
        if (ch4 < 0)
            throw new EOFException();
        return ch1 + (ch2 << 8) | (ch3 << 16) | (ch4 << 24);
    }
    
	/**
     * Saute les caractères restants du chunk courant et ferme le flux
     */
    public void close() throws IOException
    {
        finish();
        if (this == root)
            stream.close();
        stream = null;
    }
}
