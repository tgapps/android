package org.telegram.messenger.exoplayer2.upstream;

import android.net.Uri;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class FileDataSource implements DataSource {
    private long bytesRemaining;
    private RandomAccessFile file;
    private final TransferListener<? super FileDataSource> listener;
    private boolean opened;
    private Uri uri;

    public static class FileDataSourceException extends IOException {
        public FileDataSourceException(IOException cause) {
            super(cause);
        }
    }

    public long open(org.telegram.messenger.exoplayer2.upstream.DataSpec r1) throws org.telegram.messenger.exoplayer2.upstream.FileDataSource.FileDataSourceException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.FileDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long
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
        r0 = r7.uri;	 Catch:{ IOException -> 0x004f }
        r6.uri = r0;	 Catch:{ IOException -> 0x004f }
        r0 = new java.io.RandomAccessFile;	 Catch:{ IOException -> 0x004f }
        r1 = r7.uri;	 Catch:{ IOException -> 0x004f }
        r1 = r1.getPath();	 Catch:{ IOException -> 0x004f }
        r2 = "r";	 Catch:{ IOException -> 0x004f }
        r0.<init>(r1, r2);	 Catch:{ IOException -> 0x004f }
        r6.file = r0;	 Catch:{ IOException -> 0x004f }
        r0 = r6.file;	 Catch:{ IOException -> 0x004f }
        r1 = r7.position;	 Catch:{ IOException -> 0x004f }
        r0.seek(r1);	 Catch:{ IOException -> 0x004f }
        r0 = r7.length;	 Catch:{ IOException -> 0x004f }
        r2 = -1;	 Catch:{ IOException -> 0x004f }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ IOException -> 0x004f }
        if (r4 != 0) goto L_0x002d;	 Catch:{ IOException -> 0x004f }
    L_0x0022:
        r0 = r6.file;	 Catch:{ IOException -> 0x004f }
        r0 = r0.length();	 Catch:{ IOException -> 0x004f }
        r2 = r7.position;	 Catch:{ IOException -> 0x004f }
        r4 = r0 - r2;	 Catch:{ IOException -> 0x004f }
        goto L_0x002f;	 Catch:{ IOException -> 0x004f }
    L_0x002d:
        r4 = r7.length;	 Catch:{ IOException -> 0x004f }
    L_0x002f:
        r6.bytesRemaining = r4;	 Catch:{ IOException -> 0x004f }
        r0 = r6.bytesRemaining;	 Catch:{ IOException -> 0x004f }
        r2 = 0;	 Catch:{ IOException -> 0x004f }
        r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));	 Catch:{ IOException -> 0x004f }
        if (r4 >= 0) goto L_0x003f;	 Catch:{ IOException -> 0x004f }
    L_0x0039:
        r0 = new java.io.EOFException;	 Catch:{ IOException -> 0x004f }
        r0.<init>();	 Catch:{ IOException -> 0x004f }
        throw r0;	 Catch:{ IOException -> 0x004f }
        r0 = 1;
        r6.opened = r0;
        r0 = r6.listener;
        if (r0 == 0) goto L_0x004c;
        r0 = r6.listener;
        r0.onTransferStart(r6, r7);
        r0 = r6.bytesRemaining;
        return r0;
    L_0x004f:
        r0 = move-exception;
        r1 = new org.telegram.messenger.exoplayer2.upstream.FileDataSource$FileDataSourceException;
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.FileDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long");
    }

    public FileDataSource() {
        this(null);
    }

    public FileDataSource(TransferListener<? super FileDataSource> listener) {
        this.listener = listener;
    }

    public int read(byte[] buffer, int offset, int readLength) throws FileDataSourceException {
        if (readLength == 0) {
            return 0;
        }
        if (this.bytesRemaining == 0) {
            return -1;
        }
        try {
            int bytesRead = this.file.read(buffer, offset, (int) Math.min(this.bytesRemaining, (long) readLength));
            if (bytesRead > 0) {
                this.bytesRemaining -= (long) bytesRead;
                if (this.listener != null) {
                    this.listener.onBytesTransferred(this, bytesRead);
                }
            }
            return bytesRead;
        } catch (IOException e) {
            throw new FileDataSourceException(e);
        }
    }

    public Uri getUri() {
        return this.uri;
    }

    public void close() throws FileDataSourceException {
        this.uri = null;
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
            throw new FileDataSourceException(e);
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
