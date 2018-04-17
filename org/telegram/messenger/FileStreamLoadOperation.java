package org.telegram.messenger;

import android.net.Uri;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.DataSpec;
import org.telegram.messenger.exoplayer2.upstream.TransferListener;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;

public class FileStreamLoadOperation implements DataSource {
    private long bytesRemaining;
    private CountDownLatch countDownLatch;
    private int currentAccount;
    private int currentOffset;
    private Document document;
    private RandomAccessFile file;
    private final TransferListener<? super FileStreamLoadOperation> listener;
    private FileLoadOperation loadOperation;
    private boolean opened;
    private Uri uri;

    public int read(byte[] r1, int r2, int r3) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.FileStreamLoadOperation.read(byte[], int, int):int
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = 0;
        if (r10 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r7.bytesRemaining;
        r3 = 0;
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        if (r5 != 0) goto L_0x000e;
    L_0x000c:
        r0 = -1;
        return r0;
        r1 = r7.bytesRemaining;	 Catch:{ Exception -> 0x0065 }
        r3 = (long) r10;	 Catch:{ Exception -> 0x0065 }
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ Exception -> 0x0065 }
        if (r5 >= 0) goto L_0x0019;	 Catch:{ Exception -> 0x0065 }
        r1 = r7.bytesRemaining;	 Catch:{ Exception -> 0x0065 }
        r10 = (int) r1;	 Catch:{ Exception -> 0x0065 }
        if (r0 != 0) goto L_0x0049;	 Catch:{ Exception -> 0x0065 }
        r1 = r7.loadOperation;	 Catch:{ Exception -> 0x0065 }
        r2 = r7.currentOffset;	 Catch:{ Exception -> 0x0065 }
        r1 = r1.getDownloadedLengthFromOffset(r2, r10);	 Catch:{ Exception -> 0x0065 }
        r0 = r1;	 Catch:{ Exception -> 0x0065 }
        if (r0 != 0) goto L_0x0019;	 Catch:{ Exception -> 0x0065 }
        r1 = r7.loadOperation;	 Catch:{ Exception -> 0x0065 }
        r1 = r1.isPaused();	 Catch:{ Exception -> 0x0065 }
        if (r1 == 0) goto L_0x003b;	 Catch:{ Exception -> 0x0065 }
        r1 = r7.currentAccount;	 Catch:{ Exception -> 0x0065 }
        r1 = org.telegram.messenger.FileLoader.getInstance(r1);	 Catch:{ Exception -> 0x0065 }
        r2 = r7.document;	 Catch:{ Exception -> 0x0065 }
        r3 = r7.currentOffset;	 Catch:{ Exception -> 0x0065 }
        r1.loadStreamFile(r7, r2, r3);	 Catch:{ Exception -> 0x0065 }
        r1 = new java.util.concurrent.CountDownLatch;	 Catch:{ Exception -> 0x0065 }
        r2 = 1;	 Catch:{ Exception -> 0x0065 }
        r1.<init>(r2);	 Catch:{ Exception -> 0x0065 }
        r7.countDownLatch = r1;	 Catch:{ Exception -> 0x0065 }
        r1 = r7.countDownLatch;	 Catch:{ Exception -> 0x0065 }
        r1.await();	 Catch:{ Exception -> 0x0065 }
        goto L_0x0019;	 Catch:{ Exception -> 0x0065 }
        r1 = r7.file;	 Catch:{ Exception -> 0x0065 }
        r1.readFully(r8, r9, r0);	 Catch:{ Exception -> 0x0065 }
        r1 = r7.currentOffset;	 Catch:{ Exception -> 0x0065 }
        r1 = r1 + r0;	 Catch:{ Exception -> 0x0065 }
        r7.currentOffset = r1;	 Catch:{ Exception -> 0x0065 }
        r1 = r7.bytesRemaining;	 Catch:{ Exception -> 0x0065 }
        r3 = (long) r0;	 Catch:{ Exception -> 0x0065 }
        r5 = r1 - r3;	 Catch:{ Exception -> 0x0065 }
        r7.bytesRemaining = r5;	 Catch:{ Exception -> 0x0065 }
        r1 = r7.listener;	 Catch:{ Exception -> 0x0065 }
        if (r1 == 0) goto L_0x0063;	 Catch:{ Exception -> 0x0065 }
        r1 = r7.listener;	 Catch:{ Exception -> 0x0065 }
        r1.onBytesTransferred(r7, r0);	 Catch:{ Exception -> 0x0065 }
        return r0;
    L_0x0065:
        r1 = move-exception;
        r2 = new java.io.IOException;
        r2.<init>(r1);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.FileStreamLoadOperation.read(byte[], int, int):int");
    }

    public FileStreamLoadOperation() {
        this(null);
    }

    public FileStreamLoadOperation(TransferListener<? super FileStreamLoadOperation> listener) {
        this.listener = listener;
    }

    public long open(DataSpec dataSpec) throws IOException {
        this.uri = dataSpec.uri;
        this.currentAccount = Utilities.parseInt(this.uri.getQueryParameter("account")).intValue();
        this.document = new TL_document();
        this.document.access_hash = Utilities.parseLong(this.uri.getQueryParameter("hash")).longValue();
        this.document.id = Utilities.parseLong(this.uri.getQueryParameter(TtmlNode.ATTR_ID)).longValue();
        this.document.size = Utilities.parseInt(this.uri.getQueryParameter("size")).intValue();
        this.document.dc_id = Utilities.parseInt(this.uri.getQueryParameter("dc")).intValue();
        this.document.mime_type = this.uri.getQueryParameter("mime");
        TL_documentAttributeFilename filename = new TL_documentAttributeFilename();
        filename.file_name = this.uri.getQueryParameter("name");
        this.document.attributes.add(filename);
        if (this.document.mime_type.startsWith(MimeTypes.BASE_TYPE_VIDEO)) {
            this.document.attributes.add(new TL_documentAttributeVideo());
        } else if (this.document.mime_type.startsWith(MimeTypes.BASE_TYPE_AUDIO)) {
            this.document.attributes.add(new TL_documentAttributeAudio());
        }
        FileLoader instance = FileLoader.getInstance(this.currentAccount);
        Document document = this.document;
        int i = (int) dataSpec.position;
        this.currentOffset = i;
        this.loadOperation = instance.loadStreamFile(this, document, i);
        this.bytesRemaining = dataSpec.length == -1 ? ((long) this.document.size) - dataSpec.position : dataSpec.length;
        if (this.bytesRemaining < 0) {
            throw new EOFException();
        }
        this.opened = true;
        if (this.listener != null) {
            this.listener.onTransferStart(this, dataSpec);
        }
        this.file = new RandomAccessFile(this.loadOperation.getCurrentFile(), "r");
        this.file.seek((long) this.currentOffset);
        return this.bytesRemaining;
    }

    public Uri getUri() {
        return this.uri;
    }

    public void close() {
        if (this.loadOperation != null) {
            this.loadOperation.removeStreamListener(this);
        }
        if (this.countDownLatch != null) {
            this.countDownLatch.countDown();
        }
        if (this.file != null) {
            try {
                this.file.close();
            } catch (Throwable e) {
                FileLog.e(e);
            }
            this.file = null;
        }
        this.uri = null;
        if (this.opened) {
            this.opened = false;
            if (this.listener != null) {
                this.listener.onTransferEnd(this);
            }
        }
    }

    protected void newDataAvailable() {
        if (this.countDownLatch != null) {
            this.countDownLatch.countDown();
        }
    }
}
