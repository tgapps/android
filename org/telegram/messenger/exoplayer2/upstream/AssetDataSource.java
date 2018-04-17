package org.telegram.messenger.exoplayer2.upstream;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public final class AssetDataSource implements DataSource {
    private final AssetManager assetManager;
    private long bytesRemaining;
    private InputStream inputStream;
    private final TransferListener<? super AssetDataSource> listener;
    private boolean opened;
    private Uri uri;

    public static final class AssetDataSourceException extends IOException {
        public AssetDataSourceException(IOException cause) {
            super(cause);
        }
    }

    public long open(org.telegram.messenger.exoplayer2.upstream.DataSpec r1) throws org.telegram.messenger.exoplayer2.upstream.AssetDataSource.AssetDataSourceException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.AssetDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long
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
        r0 = r12.uri;	 Catch:{ IOException -> 0x0074 }
        r11.uri = r0;	 Catch:{ IOException -> 0x0074 }
        r0 = r11.uri;	 Catch:{ IOException -> 0x0074 }
        r0 = r0.getPath();	 Catch:{ IOException -> 0x0074 }
        r1 = "/android_asset/";	 Catch:{ IOException -> 0x0074 }
        r1 = r0.startsWith(r1);	 Catch:{ IOException -> 0x0074 }
        r2 = 1;	 Catch:{ IOException -> 0x0074 }
        if (r1 == 0) goto L_0x001b;	 Catch:{ IOException -> 0x0074 }
    L_0x0013:
        r1 = 15;	 Catch:{ IOException -> 0x0074 }
        r1 = r0.substring(r1);	 Catch:{ IOException -> 0x0074 }
        r0 = r1;	 Catch:{ IOException -> 0x0074 }
        goto L_0x0028;	 Catch:{ IOException -> 0x0074 }
    L_0x001b:
        r1 = "/";	 Catch:{ IOException -> 0x0074 }
        r1 = r0.startsWith(r1);	 Catch:{ IOException -> 0x0074 }
        if (r1 == 0) goto L_0x0028;	 Catch:{ IOException -> 0x0074 }
    L_0x0023:
        r1 = r0.substring(r2);	 Catch:{ IOException -> 0x0074 }
        r0 = r1;	 Catch:{ IOException -> 0x0074 }
    L_0x0028:
        r1 = r11.assetManager;	 Catch:{ IOException -> 0x0074 }
        r1 = r1.open(r0, r2);	 Catch:{ IOException -> 0x0074 }
        r11.inputStream = r1;	 Catch:{ IOException -> 0x0074 }
        r1 = r11.inputStream;	 Catch:{ IOException -> 0x0074 }
        r3 = r12.position;	 Catch:{ IOException -> 0x0074 }
        r3 = r1.skip(r3);	 Catch:{ IOException -> 0x0074 }
        r5 = r12.position;	 Catch:{ IOException -> 0x0074 }
        r1 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));	 Catch:{ IOException -> 0x0074 }
        if (r1 >= 0) goto L_0x0044;	 Catch:{ IOException -> 0x0074 }
    L_0x003e:
        r1 = new java.io.EOFException;	 Catch:{ IOException -> 0x0074 }
        r1.<init>();	 Catch:{ IOException -> 0x0074 }
        throw r1;	 Catch:{ IOException -> 0x0074 }
    L_0x0044:
        r5 = r12.length;	 Catch:{ IOException -> 0x0074 }
        r7 = -1;	 Catch:{ IOException -> 0x0074 }
        r1 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));	 Catch:{ IOException -> 0x0074 }
        if (r1 == 0) goto L_0x0051;	 Catch:{ IOException -> 0x0074 }
    L_0x004c:
        r5 = r12.length;	 Catch:{ IOException -> 0x0074 }
        r11.bytesRemaining = r5;	 Catch:{ IOException -> 0x0074 }
        goto L_0x0065;	 Catch:{ IOException -> 0x0074 }
    L_0x0051:
        r1 = r11.inputStream;	 Catch:{ IOException -> 0x0074 }
        r1 = r1.available();	 Catch:{ IOException -> 0x0074 }
        r5 = (long) r1;	 Catch:{ IOException -> 0x0074 }
        r11.bytesRemaining = r5;	 Catch:{ IOException -> 0x0074 }
        r5 = r11.bytesRemaining;	 Catch:{ IOException -> 0x0074 }
        r9 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;	 Catch:{ IOException -> 0x0074 }
        r1 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1));	 Catch:{ IOException -> 0x0074 }
        if (r1 != 0) goto L_0x0065;	 Catch:{ IOException -> 0x0074 }
        r11.bytesRemaining = r7;	 Catch:{ IOException -> 0x0074 }
        r11.opened = r2;
        r0 = r11.listener;
        if (r0 == 0) goto L_0x0071;
        r0 = r11.listener;
        r0.onTransferStart(r11, r12);
        r0 = r11.bytesRemaining;
        return r0;
    L_0x0074:
        r0 = move-exception;
        r1 = new org.telegram.messenger.exoplayer2.upstream.AssetDataSource$AssetDataSourceException;
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.AssetDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long");
    }

    public AssetDataSource(Context context) {
        this(context, null);
    }

    public AssetDataSource(Context context, TransferListener<? super AssetDataSource> listener) {
        this.assetManager = context.getAssets();
        this.listener = listener;
    }

    public int read(byte[] buffer, int offset, int readLength) throws AssetDataSourceException {
        if (readLength == 0) {
            return 0;
        }
        if (this.bytesRemaining == 0) {
            return -1;
        }
        try {
            int bytesRead = this.inputStream.read(buffer, offset, this.bytesRemaining == -1 ? readLength : (int) Math.min(this.bytesRemaining, (long) readLength));
            if (bytesRead != -1) {
                if (this.bytesRemaining != -1) {
                    this.bytesRemaining -= (long) bytesRead;
                }
                if (this.listener != null) {
                    this.listener.onBytesTransferred(this, bytesRead);
                }
                return bytesRead;
            } else if (this.bytesRemaining == -1) {
                return -1;
            } else {
                throw new AssetDataSourceException(new EOFException());
            }
        } catch (IOException e) {
            throw new AssetDataSourceException(e);
        }
    }

    public Uri getUri() {
        return this.uri;
    }

    public void close() throws AssetDataSourceException {
        this.uri = null;
        try {
            if (this.inputStream != null) {
                this.inputStream.close();
            }
            this.inputStream = null;
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd(this);
                }
            }
        } catch (IOException e) {
            throw new AssetDataSourceException(e);
        } catch (Throwable th) {
            this.inputStream = null;
            if (this.opened) {
                this.opened = false;
                if (this.listener != null) {
                    this.listener.onTransferEnd(this);
                }
            }
        }
    }
}
