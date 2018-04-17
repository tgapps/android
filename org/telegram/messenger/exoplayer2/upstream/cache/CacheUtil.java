package org.telegram.messenger.exoplayer2.upstream.cache;

import android.net.Uri;
import java.io.EOFException;
import java.io.IOException;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.DataSpec;
import org.telegram.messenger.exoplayer2.upstream.cache.Cache.CacheException;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.PriorityTaskManager;

public final class CacheUtil {
    public static final int DEFAULT_BUFFER_SIZE_BYTES = 131072;

    public static class CachingCounters {
        public volatile long alreadyCachedBytes;
        public volatile long contentLength = -1;
        public volatile long newlyCachedBytes;

        public long totalCachedBytes() {
            return this.alreadyCachedBytes + this.newlyCachedBytes;
        }
    }

    public static void getCached(org.telegram.messenger.exoplayer2.upstream.DataSpec r1, org.telegram.messenger.exoplayer2.upstream.cache.Cache r2, org.telegram.messenger.exoplayer2.upstream.cache.CacheUtil.CachingCounters r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.cache.CacheUtil.getCached(org.telegram.messenger.exoplayer2.upstream.DataSpec, org.telegram.messenger.exoplayer2.upstream.cache.Cache, org.telegram.messenger.exoplayer2.upstream.cache.CacheUtil$CachingCounters):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r20;
        r1 = r22;
        r8 = getKey(r20);
        r2 = r0.absoluteStreamPosition;
        r4 = r0.length;
        r9 = -1;
        r6 = (r4 > r9 ? 1 : (r4 == r9 ? 0 : -1));
        if (r6 == 0) goto L_0x0017;
    L_0x0012:
        r4 = r0.length;
        r11 = r21;
        goto L_0x001d;
    L_0x0017:
        r11 = r21;
        r4 = r11.getContentLength(r8);
    L_0x001d:
        r1.contentLength = r4;
        r12 = 0;
        r1.alreadyCachedBytes = r12;
        r1.newlyCachedBytes = r12;
        r16 = r2;
        r14 = r4;
        r2 = (r14 > r12 ? 1 : (r14 == r12 ? 0 : -1));
        if (r2 == 0) goto L_0x0063;
    L_0x002c:
        r2 = (r14 > r9 ? 1 : (r14 == r9 ? 0 : -1));
        r18 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        if (r2 == 0) goto L_0x0037;
    L_0x0035:
        r6 = r14;
        goto L_0x003a;
        r6 = r18;
        r2 = r11;
        r3 = r8;
        r4 = r16;
        r2 = r2.getCachedLength(r3, r4, r6);
        r4 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1));
        if (r4 <= 0) goto L_0x004d;
        r4 = r1.alreadyCachedBytes;
        r6 = r4 + r2;
        r1.alreadyCachedBytes = r6;
        goto L_0x0053;
        r2 = -r2;
        r4 = (r2 > r18 ? 1 : (r2 == r18 ? 0 : -1));
        if (r4 != 0) goto L_0x0053;
        return;
        r4 = r16 + r2;
        r6 = (r14 > r9 ? 1 : (r14 == r9 ? 0 : -1));
        if (r6 != 0) goto L_0x005b;
        r6 = r12;
        goto L_0x005c;
        r6 = r2;
        r2 = r14 - r6;
        r14 = r2;
        r16 = r4;
        goto L_0x0028;
    L_0x0063:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.cache.CacheUtil.getCached(org.telegram.messenger.exoplayer2.upstream.DataSpec, org.telegram.messenger.exoplayer2.upstream.cache.Cache, org.telegram.messenger.exoplayer2.upstream.cache.CacheUtil$CachingCounters):void");
    }

    public static String generateKey(Uri uri) {
        return uri.toString();
    }

    public static String getKey(DataSpec dataSpec) {
        return dataSpec.key != null ? dataSpec.key : generateKey(dataSpec.uri);
    }

    public static void cache(DataSpec dataSpec, Cache cache, DataSource upstream, CachingCounters counters) throws IOException, InterruptedException {
        cache(dataSpec, cache, new CacheDataSource(cache, upstream), new byte[131072], null, 0, counters, false);
    }

    public static void cache(DataSpec dataSpec, Cache cache, CacheDataSource dataSource, byte[] buffer, PriorityTaskManager priorityTaskManager, int priority, CachingCounters counters, boolean enableEOFException) throws IOException, InterruptedException {
        DataSpec dataSpec2 = dataSpec;
        Cache cache2 = cache;
        CachingCounters cachingCounters = counters;
        Assertions.checkNotNull(dataSource);
        Assertions.checkNotNull(buffer);
        if (cachingCounters != null) {
            getCached(dataSpec2, cache2, cachingCounters);
        } else {
            cachingCounters = new CachingCounters();
        }
        CachingCounters counters2 = cachingCounters;
        String key = getKey(dataSpec);
        long start = dataSpec2.absoluteStreamPosition;
        long left = dataSpec2.length != -1 ? dataSpec2.length : cache2.getContentLength(key);
        long start2 = start;
        while (true) {
            long left2 = left;
            long j = 0;
            if (left2 != 0) {
                long blockLength;
                start = cache2.getCachedLength(key, start2, left2 != -1 ? left2 : Long.MAX_VALUE);
                if (start <= 0) {
                    long blockLength2 = -start;
                    blockLength = blockLength2;
                    if (readAndDiscard(dataSpec2, start2, blockLength2, dataSource, buffer, priorityTaskManager, priority, counters2) < blockLength) {
                        break;
                    }
                }
                blockLength = start;
                start = start2 + blockLength;
                if (left2 != -1) {
                    j = blockLength;
                }
                start2 = start;
                left = left2 - j;
            } else {
                return;
            }
        }
        if (enableEOFException && left2 != -1) {
            throw new EOFException();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long readAndDiscard(org.telegram.messenger.exoplayer2.upstream.DataSpec r23, long r24, long r26, org.telegram.messenger.exoplayer2.upstream.DataSource r28, byte[] r29, org.telegram.messenger.exoplayer2.util.PriorityTaskManager r30, int r31, org.telegram.messenger.exoplayer2.upstream.cache.CacheUtil.CachingCounters r32) throws java.io.IOException, java.lang.InterruptedException {
        /*
        r3 = r28;
        r4 = r29;
        r5 = r32;
        r6 = r23;
    L_0x0008:
        if (r30 == 0) goto L_0x000d;
    L_0x000a:
        r30.proceed(r31);
    L_0x000d:
        r8 = java.lang.Thread.interrupted();	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        if (r8 == 0) goto L_0x0019;
    L_0x0013:
        r8 = new java.lang.InterruptedException;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r8.<init>();	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        throw r8;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
    L_0x0019:
        r8 = new org.telegram.messenger.exoplayer2.upstream.DataSpec;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r10 = r6.uri;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r11 = r6.postBody;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r12 = r6.position;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r14 = r12 + r24;
        r12 = r6.absoluteStreamPosition;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r16 = r14 - r12;
        r18 = -1;
        r14 = r6.key;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r9 = r6.flags;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r20 = r9 | 2;
        r9 = r8;
        r12 = r24;
        r21 = r14;
        r14 = r16;
        r16 = r18;
        r18 = r21;
        r19 = r20;
        r9.<init>(r10, r11, r12, r14, r16, r18, r19);	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r6 = r8;
        r8 = r3.open(r6);	 Catch:{ PriorityTooLowException -> 0x00b8, all -> 0x00b3 }
        r10 = r5.contentLength;	 Catch:{ PriorityTooLowException -> 0x00b8, all -> 0x00b3 }
        r12 = -1;
        r14 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r14 != 0) goto L_0x0056;
    L_0x004c:
        r10 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1));
        if (r10 == 0) goto L_0x0056;
    L_0x0050:
        r10 = r6.absoluteStreamPosition;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r14 = r10 + r8;
        r5.contentLength = r14;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
    L_0x0056:
        r10 = 0;
    L_0x0058:
        r14 = (r10 > r26 ? 1 : (r10 == r26 ? 0 : -1));
        if (r14 == 0) goto L_0x00ad;
    L_0x005c:
        r14 = java.lang.Thread.interrupted();	 Catch:{ PriorityTooLowException -> 0x00b8, all -> 0x00b3 }
        if (r14 == 0) goto L_0x0068;
    L_0x0062:
        r12 = new java.lang.InterruptedException;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r12.<init>();	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        throw r12;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
    L_0x0068:
        r15 = (r26 > r12 ? 1 : (r26 == r12 ? 0 : -1));
        if (r15 == 0) goto L_0x0076;
    L_0x006c:
        r15 = r4.length;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r12 = (long) r15;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r14 = r26 - r10;
        r12 = java.lang.Math.min(r12, r14);	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r12 = (int) r12;
        goto L_0x0077;
    L_0x0076:
        r12 = r4.length;	 Catch:{ PriorityTooLowException -> 0x00b8, all -> 0x00b3 }
    L_0x0077:
        r13 = 0;
        r12 = r3.read(r4, r13, r12);	 Catch:{ PriorityTooLowException -> 0x00b8, all -> 0x00b3 }
        r13 = -1;
        if (r12 != r13) goto L_0x0090;
    L_0x007f:
        r13 = r5.contentLength;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r15 = -1;
        r17 = (r13 > r15 ? 1 : (r13 == r15 ? 0 : -1));
        if (r17 != 0) goto L_0x008d;
    L_0x0087:
        r13 = r6.absoluteStreamPosition;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
        r1 = r13 + r10;
        r5.contentLength = r1;	 Catch:{ PriorityTooLowException -> 0x00c2, all -> 0x00bc }
    L_0x008d:
        r22 = r6;
        goto L_0x00af;
    L_0x0090:
        r15 = -1;
        r1 = (long) r12;
        r13 = r10 + r1;
        r1 = r5.newlyCachedBytes;	 Catch:{ PriorityTooLowException -> 0x00b8, all -> 0x00b3 }
        r10 = (long) r12;
        r22 = r6;
        r6 = r1 + r10;
        r5.newlyCachedBytes = r6;	 Catch:{ PriorityTooLowException -> 0x00a9, all -> 0x00a4 }
        r10 = r13;
        r12 = r15;
        r6 = r22;
        goto L_0x0058;
    L_0x00a4:
        r0 = move-exception;
        r1 = r0;
        r6 = r22;
        goto L_0x00be;
    L_0x00a9:
        r0 = move-exception;
        r6 = r22;
        goto L_0x00c3;
    L_0x00ad:
        r22 = r6;
    L_0x00af:
        org.telegram.messenger.exoplayer2.util.Util.closeQuietly(r28);
        return r10;
    L_0x00b3:
        r0 = move-exception;
        r22 = r6;
        r1 = r0;
        goto L_0x00be;
    L_0x00b8:
        r0 = move-exception;
        r22 = r6;
        goto L_0x00c3;
    L_0x00bc:
        r0 = move-exception;
        r1 = r0;
    L_0x00be:
        org.telegram.messenger.exoplayer2.util.Util.closeQuietly(r28);
        throw r1;
    L_0x00c2:
        r0 = move-exception;
    L_0x00c3:
        org.telegram.messenger.exoplayer2.util.Util.closeQuietly(r28);
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.cache.CacheUtil.readAndDiscard(org.telegram.messenger.exoplayer2.upstream.DataSpec, long, long, org.telegram.messenger.exoplayer2.upstream.DataSource, byte[], org.telegram.messenger.exoplayer2.util.PriorityTaskManager, int, org.telegram.messenger.exoplayer2.upstream.cache.CacheUtil$CachingCounters):long");
    }

    public static void remove(Cache cache, String key) {
        for (CacheSpan cachedSpan : cache.getCachedSpans(key)) {
            try {
                cache.removeSpan(cachedSpan);
            } catch (CacheException e) {
            }
        }
    }

    private CacheUtil() {
    }
}
