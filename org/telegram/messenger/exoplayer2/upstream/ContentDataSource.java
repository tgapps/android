package org.telegram.messenger.exoplayer2.upstream;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;

public final class ContentDataSource implements DataSource {
    private AssetFileDescriptor assetFileDescriptor;
    private long bytesRemaining;
    private FileInputStream inputStream;
    private final TransferListener<? super ContentDataSource> listener;
    private boolean opened;
    private final ContentResolver resolver;
    private Uri uri;

    public static class ContentDataSourceException extends IOException {
        public ContentDataSourceException(IOException cause) {
            super(cause);
        }
    }

    public void close() throws org.telegram.messenger.exoplayer2.upstream.ContentDataSource.ContentDataSourceException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.ContentDataSource.close():void
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
        r3 = new org.telegram.messenger.exoplayer2.upstream.ContentDataSource$ContentDataSourceException;	 Catch:{ all -> 0x002b }
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
        r3 = new org.telegram.messenger.exoplayer2.upstream.ContentDataSource$ContentDataSourceException;	 Catch:{ all -> 0x0046 }
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
        r3 = new org.telegram.messenger.exoplayer2.upstream.ContentDataSource$ContentDataSourceException;	 Catch:{ all -> 0x006c }
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
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.ContentDataSource.close():void");
    }

    public long open(org.telegram.messenger.exoplayer2.upstream.DataSpec r1) throws org.telegram.messenger.exoplayer2.upstream.ContentDataSource.ContentDataSourceException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.ContentDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long
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
        r0 = r15.uri;	 Catch:{ IOException -> 0x009e }
        r14.uri = r0;	 Catch:{ IOException -> 0x009e }
        r0 = r14.resolver;	 Catch:{ IOException -> 0x009e }
        r1 = r14.uri;	 Catch:{ IOException -> 0x009e }
        r2 = "r";	 Catch:{ IOException -> 0x009e }
        r0 = r0.openAssetFileDescriptor(r1, r2);	 Catch:{ IOException -> 0x009e }
        r14.assetFileDescriptor = r0;	 Catch:{ IOException -> 0x009e }
        r0 = r14.assetFileDescriptor;	 Catch:{ IOException -> 0x009e }
        if (r0 != 0) goto L_0x002d;	 Catch:{ IOException -> 0x009e }
    L_0x0014:
        r0 = new java.io.FileNotFoundException;	 Catch:{ IOException -> 0x009e }
        r1 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x009e }
        r1.<init>();	 Catch:{ IOException -> 0x009e }
        r2 = "Could not open file descriptor for: ";	 Catch:{ IOException -> 0x009e }
        r1.append(r2);	 Catch:{ IOException -> 0x009e }
        r2 = r14.uri;	 Catch:{ IOException -> 0x009e }
        r1.append(r2);	 Catch:{ IOException -> 0x009e }
        r1 = r1.toString();	 Catch:{ IOException -> 0x009e }
        r0.<init>(r1);	 Catch:{ IOException -> 0x009e }
        throw r0;	 Catch:{ IOException -> 0x009e }
    L_0x002d:
        r0 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x009e }
        r1 = r14.assetFileDescriptor;	 Catch:{ IOException -> 0x009e }
        r1 = r1.getFileDescriptor();	 Catch:{ IOException -> 0x009e }
        r0.<init>(r1);	 Catch:{ IOException -> 0x009e }
        r14.inputStream = r0;	 Catch:{ IOException -> 0x009e }
        r0 = r14.assetFileDescriptor;	 Catch:{ IOException -> 0x009e }
        r0 = r0.getStartOffset();	 Catch:{ IOException -> 0x009e }
        r2 = r14.inputStream;	 Catch:{ IOException -> 0x009e }
        r3 = r15.position;	 Catch:{ IOException -> 0x009e }
        r5 = r0 + r3;	 Catch:{ IOException -> 0x009e }
        r2 = r2.skip(r5);	 Catch:{ IOException -> 0x009e }
        r4 = r2 - r0;	 Catch:{ IOException -> 0x009e }
        r2 = r15.position;	 Catch:{ IOException -> 0x009e }
        r6 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));	 Catch:{ IOException -> 0x009e }
        if (r6 == 0) goto L_0x0058;	 Catch:{ IOException -> 0x009e }
    L_0x0052:
        r2 = new java.io.EOFException;	 Catch:{ IOException -> 0x009e }
        r2.<init>();	 Catch:{ IOException -> 0x009e }
        throw r2;	 Catch:{ IOException -> 0x009e }
    L_0x0058:
        r2 = r15.length;	 Catch:{ IOException -> 0x009e }
        r6 = -1;	 Catch:{ IOException -> 0x009e }
        r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));	 Catch:{ IOException -> 0x009e }
        if (r8 == 0) goto L_0x0065;	 Catch:{ IOException -> 0x009e }
    L_0x0060:
        r2 = r15.length;	 Catch:{ IOException -> 0x009e }
        r14.bytesRemaining = r2;	 Catch:{ IOException -> 0x009e }
        goto L_0x008e;	 Catch:{ IOException -> 0x009e }
    L_0x0065:
        r2 = r14.assetFileDescriptor;	 Catch:{ IOException -> 0x009e }
        r2 = r2.getLength();	 Catch:{ IOException -> 0x009e }
        r8 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1));	 Catch:{ IOException -> 0x009e }
        if (r8 != 0) goto L_0x008a;	 Catch:{ IOException -> 0x009e }
        r8 = r14.inputStream;	 Catch:{ IOException -> 0x009e }
        r8 = r8.getChannel();	 Catch:{ IOException -> 0x009e }
        r9 = r8.size();	 Catch:{ IOException -> 0x009e }
        r11 = 0;	 Catch:{ IOException -> 0x009e }
        r13 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));	 Catch:{ IOException -> 0x009e }
        if (r13 != 0) goto L_0x0080;	 Catch:{ IOException -> 0x009e }
        goto L_0x0087;	 Catch:{ IOException -> 0x009e }
        r6 = r8.position();	 Catch:{ IOException -> 0x009e }
        r11 = r9 - r6;	 Catch:{ IOException -> 0x009e }
        r6 = r11;	 Catch:{ IOException -> 0x009e }
        r14.bytesRemaining = r6;	 Catch:{ IOException -> 0x009e }
        goto L_0x008e;	 Catch:{ IOException -> 0x009e }
        r6 = r2 - r4;	 Catch:{ IOException -> 0x009e }
        r14.bytesRemaining = r6;	 Catch:{ IOException -> 0x009e }
        r0 = 1;
        r14.opened = r0;
        r0 = r14.listener;
        if (r0 == 0) goto L_0x009b;
        r0 = r14.listener;
        r0.onTransferStart(r14, r15);
        r0 = r14.bytesRemaining;
        return r0;
    L_0x009e:
        r0 = move-exception;
        r1 = new org.telegram.messenger.exoplayer2.upstream.ContentDataSource$ContentDataSourceException;
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.ContentDataSource.open(org.telegram.messenger.exoplayer2.upstream.DataSpec):long");
    }

    public ContentDataSource(Context context) {
        this(context, null);
    }

    public ContentDataSource(Context context, TransferListener<? super ContentDataSource> listener) {
        this.resolver = context.getContentResolver();
        this.listener = listener;
    }

    public int read(byte[] buffer, int offset, int readLength) throws ContentDataSourceException {
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
                throw new ContentDataSourceException(new EOFException());
            }
        } catch (IOException e) {
            throw new ContentDataSourceException(e);
        }
    }

    public Uri getUri() {
        return this.uri;
    }
}
