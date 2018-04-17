package org.telegram.messenger.exoplayer2.upstream;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.net.Uri;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public final class RawResourceDataSource implements DataSource {
    public static final String RAW_RESOURCE_SCHEME = "rawresource";
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private InputStream inputStream;
    private final TransferListener<? super RawResourceDataSource> listener;
    private boolean opened;
    private final Resources resources;
    private Uri uri;

    public static class RawResourceDataSourceException extends IOException {
        public RawResourceDataSourceException(String message) {
            super(message);
        }

        public RawResourceDataSourceException(IOException e) {
            super(e);
        }
    }

    public void close() throws org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource.RawResourceDataSourceException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource.close():void
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
        r0 = 0;
        r4.uri = r0;
        r1 = 0;
        r2 = r4.inputStream;	 Catch:{ IOException -> 0x0048 }
        if (r2 == 0) goto L_0x000d;	 Catch:{ IOException -> 0x0048 }
    L_0x0008:
        r2 = r4.inputStream;	 Catch:{ IOException -> 0x0048 }
        r2.close();	 Catch:{ IOException -> 0x0048 }
    L_0x000d:
        r4.inputStream = r0;
        r2 = r4.assetFileDescriptor;	 Catch:{ IOException -> 0x002d }
        if (r2 == 0) goto L_0x0018;	 Catch:{ IOException -> 0x002d }
    L_0x0013:
        r2 = r4.assetFileDescriptor;	 Catch:{ IOException -> 0x002d }
        r2.close();	 Catch:{ IOException -> 0x002d }
    L_0x0018:
        r4.assetFileDescriptor = r0;
        r0 = r4.opened;
        if (r0 == 0) goto L_0x0029;
    L_0x001e:
        r4.opened = r1;
        r0 = r4.listener;
        if (r0 == 0) goto L_0x0029;
        r0 = r4.listener;
        r0.onTransferEnd(r4);
        return;
    L_0x002b:
        r2 = move-exception;
        goto L_0x0034;
    L_0x002d:
        r2 = move-exception;
        r3 = new org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource$RawResourceDataSourceException;	 Catch:{ all -> 0x002b }
        r3.<init>(r2);	 Catch:{ all -> 0x002b }
        throw r3;	 Catch:{ all -> 0x002b }
        r4.assetFileDescriptor = r0;
        r0 = r4.opened;
        if (r0 == 0) goto L_0x0045;
        r4.opened = r1;
        r0 = r4.listener;
        if (r0 == 0) goto L_0x0045;
        r0 = r4.listener;
        r0.onTransferEnd(r4);
        throw r2;
    L_0x0046:
        r2 = move-exception;
        goto L_0x004f;
    L_0x0048:
        r2 = move-exception;
        r3 = new org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource$RawResourceDataSourceException;	 Catch:{ all -> 0x0046 }
        r3.<init>(r2);	 Catch:{ all -> 0x0046 }
        throw r3;	 Catch:{ all -> 0x0046 }
        r4.inputStream = r0;
        r3 = r4.assetFileDescriptor;	 Catch:{ IOException -> 0x006e }
        if (r3 == 0) goto L_0x005a;	 Catch:{ IOException -> 0x006e }
        r3 = r4.assetFileDescriptor;	 Catch:{ IOException -> 0x006e }
        r3.close();	 Catch:{ IOException -> 0x006e }
        r4.assetFileDescriptor = r0;
        r0 = r4.opened;
        if (r0 == 0) goto L_0x006b;
        r4.opened = r1;
        r0 = r4.listener;
        if (r0 == 0) goto L_0x006b;
        r0 = r4.listener;
        r0.onTransferEnd(r4);
        throw r2;
    L_0x006c:
        r2 = move-exception;
        goto L_0x0075;
    L_0x006e:
        r2 = move-exception;
        r3 = new org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource$RawResourceDataSourceException;	 Catch:{ all -> 0x006c }
        r3.<init>(r2);	 Catch:{ all -> 0x006c }
        throw r3;	 Catch:{ all -> 0x006c }
        r4.assetFileDescriptor = r0;
        r0 = r4.opened;
        if (r0 == 0) goto L_0x0086;
        r4.opened = r1;
        r0 = r4.listener;
        if (r0 == 0) goto L_0x0086;
        r0 = r4.listener;
        r0.onTransferEnd(r4);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource.close():void");
    }

    public long open(org.telegram.messenger.exoplayer2.upstream.DataSpec r1) throws org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource.RawResourceDataSourceException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long
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
        r0 = r10.uri;	 Catch:{ IOException -> 0x0092 }
        r9.uri = r0;	 Catch:{ IOException -> 0x0092 }
        r0 = "rawresource";	 Catch:{ IOException -> 0x0092 }
        r1 = r9.uri;	 Catch:{ IOException -> 0x0092 }
        r1 = r1.getScheme();	 Catch:{ IOException -> 0x0092 }
        r0 = android.text.TextUtils.equals(r0, r1);	 Catch:{ IOException -> 0x0092 }
        if (r0 != 0) goto L_0x001a;	 Catch:{ IOException -> 0x0092 }
    L_0x0012:
        r0 = new org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource$RawResourceDataSourceException;	 Catch:{ IOException -> 0x0092 }
        r1 = "URI must use scheme rawresource";	 Catch:{ IOException -> 0x0092 }
        r0.<init>(r1);	 Catch:{ IOException -> 0x0092 }
        throw r0;	 Catch:{ IOException -> 0x0092 }
    L_0x001a:
        r0 = r9.uri;	 Catch:{ NumberFormatException -> 0x0089 }
        r0 = r0.getLastPathSegment();	 Catch:{ NumberFormatException -> 0x0089 }
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x0089 }
        r1 = r9.resources;	 Catch:{ IOException -> 0x0092 }
        r1 = r1.openRawResourceFd(r0);	 Catch:{ IOException -> 0x0092 }
        r9.assetFileDescriptor = r1;	 Catch:{ IOException -> 0x0092 }
        r1 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0092 }
        r2 = r9.assetFileDescriptor;	 Catch:{ IOException -> 0x0092 }
        r2 = r2.getFileDescriptor();	 Catch:{ IOException -> 0x0092 }
        r1.<init>(r2);	 Catch:{ IOException -> 0x0092 }
        r9.inputStream = r1;	 Catch:{ IOException -> 0x0092 }
        r1 = r9.inputStream;	 Catch:{ IOException -> 0x0092 }
        r2 = r9.assetFileDescriptor;	 Catch:{ IOException -> 0x0092 }
        r2 = r2.getStartOffset();	 Catch:{ IOException -> 0x0092 }
        r1.skip(r2);	 Catch:{ IOException -> 0x0092 }
        r1 = r9.inputStream;	 Catch:{ IOException -> 0x0092 }
        r2 = r10.position;	 Catch:{ IOException -> 0x0092 }
        r1 = r1.skip(r2);	 Catch:{ IOException -> 0x0092 }
        r3 = r10.position;	 Catch:{ IOException -> 0x0092 }
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ IOException -> 0x0092 }
        if (r5 >= 0) goto L_0x005a;	 Catch:{ IOException -> 0x0092 }
    L_0x0054:
        r3 = new java.io.EOFException;	 Catch:{ IOException -> 0x0092 }
        r3.<init>();	 Catch:{ IOException -> 0x0092 }
        throw r3;	 Catch:{ IOException -> 0x0092 }
    L_0x005a:
        r3 = r10.length;	 Catch:{ IOException -> 0x0092 }
        r5 = -1;	 Catch:{ IOException -> 0x0092 }
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));	 Catch:{ IOException -> 0x0092 }
        if (r7 == 0) goto L_0x0067;	 Catch:{ IOException -> 0x0092 }
    L_0x0062:
        r3 = r10.length;	 Catch:{ IOException -> 0x0092 }
        r9.bytesRemaining = r3;	 Catch:{ IOException -> 0x0092 }
        goto L_0x0079;	 Catch:{ IOException -> 0x0092 }
    L_0x0067:
        r3 = r9.assetFileDescriptor;	 Catch:{ IOException -> 0x0092 }
        r3 = r3.getLength();	 Catch:{ IOException -> 0x0092 }
        r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));	 Catch:{ IOException -> 0x0092 }
        if (r7 != 0) goto L_0x0072;	 Catch:{ IOException -> 0x0092 }
        goto L_0x0077;	 Catch:{ IOException -> 0x0092 }
        r5 = r10.position;	 Catch:{ IOException -> 0x0092 }
        r7 = r3 - r5;	 Catch:{ IOException -> 0x0092 }
        r5 = r7;	 Catch:{ IOException -> 0x0092 }
        r9.bytesRemaining = r5;	 Catch:{ IOException -> 0x0092 }
        r0 = 1;
        r9.opened = r0;
        r0 = r9.listener;
        if (r0 == 0) goto L_0x0086;
        r0 = r9.listener;
        r0.onTransferStart(r9, r10);
        r0 = r9.bytesRemaining;
        return r0;
    L_0x0089:
        r0 = move-exception;
        r1 = new org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource$RawResourceDataSourceException;	 Catch:{ IOException -> 0x0092 }
        r2 = "Resource identifier must be an integer.";	 Catch:{ IOException -> 0x0092 }
        r1.<init>(r2);	 Catch:{ IOException -> 0x0092 }
        throw r1;	 Catch:{ IOException -> 0x0092 }
    L_0x0092:
        r0 = move-exception;
        r1 = new org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource$RawResourceDataSourceException;
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.RawResourceDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long");
    }

    public static Uri buildRawResourceUri(int rawResourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("rawresource:///");
        stringBuilder.append(rawResourceId);
        return Uri.parse(stringBuilder.toString());
    }

    public RawResourceDataSource(Context context) {
        this(context, null);
    }

    public RawResourceDataSource(Context context, TransferListener<? super RawResourceDataSource> listener) {
        this.resources = context.getResources();
        this.listener = listener;
    }

    public int read(byte[] buffer, int offset, int readLength) throws RawResourceDataSourceException {
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
                throw new RawResourceDataSourceException(new EOFException());
            }
        } catch (IOException e) {
            throw new RawResourceDataSourceException(e);
        }
    }

    public Uri getUri() {
        return this.uri;
    }
}
