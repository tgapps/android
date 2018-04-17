package org.telegram.messenger.exoplayer2.upstream.cache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.telegram.messenger.exoplayer2.upstream.DataSink;
import org.telegram.messenger.exoplayer2.upstream.DataSpec;
import org.telegram.messenger.exoplayer2.upstream.cache.Cache.CacheException;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.ReusableBufferedOutputStream;

public final class CacheDataSink implements DataSink {
    public static final int DEFAULT_BUFFER_SIZE = 20480;
    private final int bufferSize;
    private ReusableBufferedOutputStream bufferedOutputStream;
    private final Cache cache;
    private DataSpec dataSpec;
    private long dataSpecBytesWritten;
    private File file;
    private final long maxCacheFileSize;
    private OutputStream outputStream;
    private long outputStreamBytesWritten;
    private FileOutputStream underlyingFileOutputStream;

    public static class CacheDataSinkException extends CacheException {
        public CacheDataSinkException(IOException cause) {
            super((Throwable) cause);
        }
    }

    private void closeCurrentOutputStream() throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.cache.CacheDataSink.closeCurrentOutputStream():void
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
        r0 = r4.outputStream;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = 0;
        r1 = 0;
        r2 = r4.outputStream;	 Catch:{ all -> 0x002e }
        r2.flush();	 Catch:{ all -> 0x002e }
        r2 = r4.underlyingFileOutputStream;	 Catch:{ all -> 0x002e }
        r2 = r2.getFD();	 Catch:{ all -> 0x002e }
        r2.sync();	 Catch:{ all -> 0x002e }
        r0 = 1;
        r2 = r4.outputStream;
        org.telegram.messenger.exoplayer2.util.Util.closeQuietly(r2);
        r4.outputStream = r1;
        r2 = r4.file;
        r4.file = r1;
        if (r0 == 0) goto L_0x0029;
    L_0x0023:
        r1 = r4.cache;
        r1.commitFile(r2);
        goto L_0x002c;
    L_0x0029:
        r2.delete();
        return;
    L_0x002e:
        r2 = move-exception;
        r3 = r4.outputStream;
        org.telegram.messenger.exoplayer2.util.Util.closeQuietly(r3);
        r4.outputStream = r1;
        r3 = r4.file;
        r4.file = r1;
        if (r0 == 0) goto L_0x0042;
        r1 = r4.cache;
        r1.commitFile(r3);
        goto L_0x0045;
        r3.delete();
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.cache.CacheDataSink.closeCurrentOutputStream():void");
    }

    public void write(byte[] r1, int r2, int r3) throws org.telegram.messenger.exoplayer2.upstream.cache.CacheDataSink.CacheDataSinkException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.cache.CacheDataSink.write(byte[], int, int):void
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
        r0 = r9.dataSpec;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = 0;
        if (r0 >= r12) goto L_0x0042;
    L_0x0008:
        r1 = r9.outputStreamBytesWritten;	 Catch:{ IOException -> 0x003b }
        r3 = r9.maxCacheFileSize;	 Catch:{ IOException -> 0x003b }
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ IOException -> 0x003b }
        if (r5 != 0) goto L_0x0016;	 Catch:{ IOException -> 0x003b }
        r9.closeCurrentOutputStream();	 Catch:{ IOException -> 0x003b }
        r9.openNextOutputStream();	 Catch:{ IOException -> 0x003b }
        r1 = r12 - r0;	 Catch:{ IOException -> 0x003b }
        r1 = (long) r1;	 Catch:{ IOException -> 0x003b }
        r3 = r9.maxCacheFileSize;	 Catch:{ IOException -> 0x003b }
        r5 = r9.outputStreamBytesWritten;	 Catch:{ IOException -> 0x003b }
        r7 = r3 - r5;	 Catch:{ IOException -> 0x003b }
        r1 = java.lang.Math.min(r1, r7);	 Catch:{ IOException -> 0x003b }
        r1 = (int) r1;	 Catch:{ IOException -> 0x003b }
        r2 = r9.outputStream;	 Catch:{ IOException -> 0x003b }
        r3 = r11 + r0;	 Catch:{ IOException -> 0x003b }
        r2.write(r10, r3, r1);	 Catch:{ IOException -> 0x003b }
        r0 = r0 + r1;	 Catch:{ IOException -> 0x003b }
        r2 = r9.outputStreamBytesWritten;	 Catch:{ IOException -> 0x003b }
        r4 = (long) r1;	 Catch:{ IOException -> 0x003b }
        r6 = r2 + r4;	 Catch:{ IOException -> 0x003b }
        r9.outputStreamBytesWritten = r6;	 Catch:{ IOException -> 0x003b }
        r2 = r9.dataSpecBytesWritten;	 Catch:{ IOException -> 0x003b }
        r4 = (long) r1;	 Catch:{ IOException -> 0x003b }
        r6 = r2 + r4;	 Catch:{ IOException -> 0x003b }
        r9.dataSpecBytesWritten = r6;	 Catch:{ IOException -> 0x003b }
        goto L_0x0006;
    L_0x003b:
        r0 = move-exception;
        r1 = new org.telegram.messenger.exoplayer2.upstream.cache.CacheDataSink$CacheDataSinkException;
        r1.<init>(r0);
        throw r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.cache.CacheDataSink.write(byte[], int, int):void");
    }

    public CacheDataSink(Cache cache, long maxCacheFileSize) {
        this(cache, maxCacheFileSize, DEFAULT_BUFFER_SIZE);
    }

    public CacheDataSink(Cache cache, long maxCacheFileSize, int bufferSize) {
        this.cache = (Cache) Assertions.checkNotNull(cache);
        this.maxCacheFileSize = maxCacheFileSize;
        this.bufferSize = bufferSize;
    }

    public void open(DataSpec dataSpec) throws CacheDataSinkException {
        if (dataSpec.length != -1 || dataSpec.isFlagSet(2)) {
            this.dataSpec = dataSpec;
            this.dataSpecBytesWritten = 0;
            try {
                openNextOutputStream();
                return;
            } catch (IOException e) {
                throw new CacheDataSinkException(e);
            }
        }
        this.dataSpec = null;
    }

    public void close() throws CacheDataSinkException {
        if (this.dataSpec != null) {
            try {
                closeCurrentOutputStream();
            } catch (IOException e) {
                throw new CacheDataSinkException(e);
            }
        }
    }

    private void openNextOutputStream() throws IOException {
        long j;
        if (this.dataSpec.length == -1) {
            j = this.maxCacheFileSize;
        } else {
            j = Math.min(this.dataSpec.length - this.dataSpecBytesWritten, this.maxCacheFileSize);
        }
        this.file = this.cache.startFile(this.dataSpec.key, this.dataSpec.absoluteStreamPosition + this.dataSpecBytesWritten, j);
        this.underlyingFileOutputStream = new FileOutputStream(this.file);
        if (this.bufferSize > 0) {
            if (this.bufferedOutputStream == null) {
                this.bufferedOutputStream = new ReusableBufferedOutputStream(this.underlyingFileOutputStream, this.bufferSize);
            } else {
                this.bufferedOutputStream.reset(this.underlyingFileOutputStream);
            }
            this.outputStream = this.bufferedOutputStream;
        } else {
            this.outputStream = this.underlyingFileOutputStream;
        }
        this.outputStreamBytesWritten = 0;
    }
}
