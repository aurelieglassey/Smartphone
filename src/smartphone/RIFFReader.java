
// Classe reprise du package com.sun.media.sound
// �crite par Karl Helgason
// Le code n'a pas �t� modifi�. Seuls des commentaires ont �t� ajout�s pour
// mieux d�crire le comportement de la classe


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


// Cette classe sert � lire un fichier structur� suivant le format RIFF (d�coupage du
// fichier en chunks commen�ant par un nom de 4 caract�rse suivi de la longueur du
// chunk cod�e sur 4 bytes en LITTLE ENDIAN)
/**
 * Resource Interchange File Format (RIFF) stream decoder.
 *
 * @author Karl Helgason
 */
public final class RIFFReader extends InputStream {

    private final RIFFReader root;
    private long filepointer = 0;
    private final String fourcc;
    private String riff_type = null;
    private long ckSize = Integer.MAX_VALUE;
    private InputStream stream;
    private long avail = Integer.MAX_VALUE;
    private RIFFReader lastiterator = null;
	
	// Cr�e un nouveau RIFFReader � partir d'un InputStream
    public RIFFReader(InputStream stream) throws IOException {

		// Une instance RIFFReader peut �tre cr��e � partir d'une autre instance RIFFReader
		// (RIFFReader �tend InputStream). Dans ce cas, on conserve toujours une r�f�rence
		// vers l'instance racine
        if (stream instanceof RIFFReader) {
            root = ((RIFFReader) stream).root;
        } else {
            root = this;
        }

        this.stream = stream;

		// Apparemment, on peut s'attendre � trouver des caract�res NUL de padding entre
		// des chunks ou au d�but du fichier ?
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

		// On lit les 4 premiers caract�res apr�s le padding de NUL
		// Ils donnent le format du chunk
        byte[] fourcc = new byte[4];
        fourcc[0] = (byte) b;
        readFully(fourcc, 1, 3);
        this.fourcc = new String(fourcc, "ascii");
        ckSize = readUnsignedInt();
        avail = ckSize; // avail stocke le nombre de bytes qui n'ont pas encore �t� lus
		
		// Pour les chunks RIFF et LIST, un type peut �tre d�fini
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
	
	// filepointer contient le nombre de caract�res lu dans le fichier,
	// ind�pendamment des instances cr��es pour lire les chunks
    public long getFilePointer() throws IOException {
        return root.filepointer;
    }
	
	// Retourne TRUE s'il y a encore des caract�res � lire dans le fichier, FALSE sinon
    public boolean hasNextChunk() throws IOException {
        if (lastiterator != null)
            lastiterator.finish();
        return avail != 0;
    }

	// Retourne une instance RIFFReader pour lire le prochain chunk
    public RIFFReader nextChunk() throws IOException {
        if (lastiterator != null)
            lastiterator.finish();
        if (avail == 0)
            return null;
        lastiterator = new RIFFReader(this);
        return lastiterator;
    }
	
	// Retourne le format du chunk (RIFF, 'fmt ', data, LIST, ...)
    public String getFormat() {
        return fourcc;
    }
	// Retourne le type du chunk (par exemple WAVE pour RIFF, ou INFO pour LIST)
    public String getType() {
        return riff_type;
    }
	
	// Retourne la taille du contenu du chunk en bytes, � partir du byte suivant imm�diatement
	// la longueur annonc�e dans le fichier. La valeur est directement reprise du fichier (voir constructeur)
    public long getSize() {
        return ckSize;
    }

	// Lit un byte et le retourne sous forme d'un int allant de 0 � 255
    public int read() throws IOException {
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
	
	// Lit len bytes dans le flux, puis les stocke dans le tableau b en commen�ant � la cellule offset
    public int read(byte[] b, int offset, int len) throws IOException {
        if (avail == 0) {
            return -1; // On retourne directement -1 s'il n'y a plus de caract�res disponibles
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
	
	// Remplit b avec les b.length prochains bytes en commen�ant au d�but du tableau
    public final void readFully(byte b[]) throws IOException {
        readFully(b, 0, b.length);
    }
	
    public final void readFully(byte b[], int off, int len) throws IOException {
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
	
	// Saute n bytes dans le flux SANS mettre � jour avail et filepointer
    public final long skipBytes(long n) throws IOException {
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
	
	// Saute n bytes dans le flux en mettant � jour avail et filepointer
    public long skip(long n) throws IOException {
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
	
	// Retourne le nombre de bytes encore non lus du flux
    public int available() {
        return (int)avail;
    }
	
	// Saute tous les bytes restant du flux
    public void finish() throws IOException {
        if (avail != 0) {
            skipBytes(avail);
        }
    }

	// readString lit n bytes dans le flux, puis en fait une cha�ne de
	// caract�res encod�e ASCII. Cette m�thode retourne la partie de la cha�ne
	// allant du tout premier caract�re jusqu'� avant le premier caract�re NUL rencontr�
    // Read ASCII chars from stream
    public String readString(final int len) throws IOException {
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
	
	// Lit un byte dans le flux
    // Read 8 bit signed integer from stream
    public byte readByte() throws IOException {
        int ch = read();
        if (ch < 0)
            throw new EOFException();
        return (byte) ch;
    }
	
	// Lit un short en LITTLE ENDIAN dans le flux
    // Read 16 bit signed integer from stream
    public short readShort() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if (ch1 < 0)
            throw new EOFException();
        if (ch2 < 0)
            throw new EOFException();
        return (short)(ch1 | (ch2 << 8));
    }

	// Lit un int en LITTLE ENDIAN dans le flux
    // Read 32 bit signed integer from stream
    public int readInt() throws IOException {
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

	// Lit un long en LITTLE ENDIAN dans le flux
    // Read 64 bit signed integer from stream
    public long readLong() throws IOException {
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
	
	// Lit un byte non sign� dans le flux et le retourne comme int
    // Read 8 bit unsigned integer from stream
    public int readUnsignedByte() throws IOException {
        int ch = read();
        if (ch < 0)
            throw new EOFException();
        return ch;
    }
	// Lit un short non sign� en LITTLE ENDIAN dans le flux et le retourne comme int
    // Read 16 bit unsigned integer from stream
    public int readUnsignedShort() throws IOException {
        int ch1 = read();
        int ch2 = read();
        if (ch1 < 0)
            throw new EOFException();
        if (ch2 < 0)
            throw new EOFException();
        return ch1 | (ch2 << 8);
    }
	// Lit un int non sign� en LITTLE ENDIAN dans le flux et le retourne comme long
    // Read 32 bit unsigned integer from stream
    public long readUnsignedInt() throws IOException {
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
	// Saute les caract�res restants du chunk courant et ferme le flux
    public void close() throws IOException {
        finish();
        if (this == root)
            stream.close();
        stream = null;
    }
}
