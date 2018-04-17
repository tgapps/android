package org.telegram.messenger.secretmedia;

import android.net.Uri;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.TransferListener;

public final class EncryptedFileDataSource implements DataSource {
    private long bytesRemaining;
    private RandomAccessFile file;
    private int fileOffset;
    private byte[] iv;
    private byte[] key;
    private final TransferListener<? super EncryptedFileDataSource> listener;
    private boolean opened;
    private Uri uri;

    public static class EncryptedFileDataSourceException extends IOException {
        public EncryptedFileDataSourceException(IOException cause) {
            super(cause);
        }
    }

    public long open(org.telegram.messenger.exoplayer2.upstream.DataSpec r1) throws org.telegram.messenger.secretmedia.EncryptedFileDataSource.EncryptedFileDataSourceException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.secretmedia.EncryptedFileDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r11.uri;	 Catch:{ IOException -> 0x008b }
        r10.uri = r0;	 Catch:{ IOException -> 0x008b }
        r0 = new java.io.File;	 Catch:{ IOException -> 0x008b }
        r1 = r11.uri;	 Catch:{ IOException -> 0x008b }
        r1 = r1.getPath();	 Catch:{ IOException -> 0x008b }
        r0.<init>(r1);	 Catch:{ IOException -> 0x008b }
        r1 = r0.getName();	 Catch:{ IOException -> 0x008b }
        r2 = new java.io.File;	 Catch:{ IOException -> 0x008b }
        r3 = org.telegram.messenger.FileLoader.getInternalCacheDir();	 Catch:{ IOException -> 0x008b }
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x008b }
        r4.<init>();	 Catch:{ IOException -> 0x008b }
        r4.append(r1);	 Catch:{ IOException -> 0x008b }
        r5 = ".key";	 Catch:{ IOException -> 0x008b }
        r4.append(r5);	 Catch:{ IOException -> 0x008b }
        r4 = r4.toString();	 Catch:{ IOException -> 0x008b }
        r2.<init>(r3, r4);	 Catch:{ IOException -> 0x008b }
        r3 = new java.io.RandomAccessFile;	 Catch:{ IOException -> 0x008b }
        r4 = "r";	 Catch:{ IOException -> 0x008b }
        r3.<init>(r2, r4);	 Catch:{ IOException -> 0x008b }
        r4 = r10.key;	 Catch:{ IOException -> 0x008b }
        r3.read(r4);	 Catch:{ IOException -> 0x008b }
        r4 = r10.iv;	 Catch:{ IOException -> 0x008b }
        r3.read(r4);	 Catch:{ IOException -> 0x008b }
        r3.close();	 Catch:{ IOException -> 0x008b }
        r4 = new java.io.RandomAccessFile;	 Catch:{ IOException -> 0x008b }
        r5 = "r";	 Catch:{ IOException -> 0x008b }
        r4.<init>(r0, r5);	 Catch:{ IOException -> 0x008b }
        r10.file = r4;	 Catch:{ IOException -> 0x008b }
        r4 = r10.file;	 Catch:{ IOException -> 0x008b }
        r5 = r11.position;	 Catch:{ IOException -> 0x008b }
        r4.seek(r5);	 Catch:{ IOException -> 0x008b }
        r4 = r11.position;	 Catch:{ IOException -> 0x008b }
        r4 = (int) r4;	 Catch:{ IOException -> 0x008b }
        r10.fileOffset = r4;	 Catch:{ IOException -> 0x008b }
        r4 = r11.length;	 Catch:{ IOException -> 0x008b }
        r6 = -1;	 Catch:{ IOException -> 0x008b }
        r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));	 Catch:{ IOException -> 0x008b }
        if (r8 != 0) goto L_0x0069;	 Catch:{ IOException -> 0x008b }
    L_0x005e:
        r4 = r10.file;	 Catch:{ IOException -> 0x008b }
        r4 = r4.length();	 Catch:{ IOException -> 0x008b }
        r6 = r11.position;	 Catch:{ IOException -> 0x008b }
        r8 = r4 - r6;	 Catch:{ IOException -> 0x008b }
        goto L_0x006b;	 Catch:{ IOException -> 0x008b }
    L_0x0069:
        r8 = r11.length;	 Catch:{ IOException -> 0x008b }
    L_0x006b:
        r10.bytesRemaining = r8;	 Catch:{ IOException -> 0x008b }
        r4 = r10.bytesRemaining;	 Catch:{ IOException -> 0x008b }
        r6 = 0;	 Catch:{ IOException -> 0x008b }
        r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));	 Catch:{ IOException -> 0x008b }
        if (r8 >= 0) goto L_0x007b;	 Catch:{ IOException -> 0x008b }
    L_0x0075:
        r4 = new java.io.EOFException;	 Catch:{ IOException -> 0x008b }
        r4.<init>();	 Catch:{ IOException -> 0x008b }
        throw r4;	 Catch:{ IOException -> 0x008b }
        r0 = 1;
        r10.opened = r0;
        r0 = r10.listener;
        if (r0 == 0) goto L_0x0088;
        r0 = r10.listener;
        r0.onTransferStart(r10, r11);
        r0 = r10.bytesRemaining;
        return r0;
    L_0x008b:
        r0 = move-exception;
        r1 = new org.telegram.messenger.secretmedia.EncryptedFileDataSource$EncryptedFileDataSourceException;
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.secretmedia.EncryptedFileDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long");
    }

    public EncryptedFileDataSource() {
        this(null);
    }

    public EncryptedFileDataSource(TransferListener<? super EncryptedFileDataSource> listener) {
        this.key = new byte[32];
        this.iv = new byte[16];
        this.listener = listener;
    }

    public int read(byte[] buffer, int offset, int readLength) throws EncryptedFileDataSourceException {
        if (readLength == 0) {
            return 0;
        }
        if (this.bytesRemaining == 0) {
            return -1;
        }
        try {
            int bytesRead = this.file.read(buffer, offset, (int) Math.min(this.bytesRemaining, (long) readLength));
            Utilities.aesCtrDecryptionByteArray(buffer, this.key, this.iv, offset, bytesRead, this.fileOffset);
            this.fileOffset += bytesRead;
            if (bytesRead > 0) {
                this.bytesRemaining -= (long) bytesRead;
                if (this.listener != null) {
                    this.listener.onBytesTransferred(this, bytesRead);
                }
            }
            return bytesRead;
        } catch (IOException e) {
            throw new EncryptedFileDataSourceException(e);
        }
    }

    public Uri getUri() {
        return this.uri;
    }

    public void close() throws EncryptedFileDataSourceException {
        this.uri = null;
        this.fileOffset = 0;
        try {
            if (this.file != null) {
                this.file.close();
            }
            this.file = null;
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd(this);
                }
            }
        } catch (IOException e) {
            throw new EncryptedFileDataSourceException(e);
        } catch (Throwable th) {
            this.file = null;
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd(this);
                }
            }
        }
    }
}
