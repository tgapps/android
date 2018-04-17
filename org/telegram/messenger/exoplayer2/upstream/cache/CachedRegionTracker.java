package org.telegram.messenger.exoplayer2.upstream.cache;

import java.util.Arrays;
import java.util.Iterator;
import java.util.TreeSet;
import org.telegram.messenger.exoplayer2.extractor.ChunkIndex;
import org.telegram.messenger.exoplayer2.upstream.cache.Cache.Listener;

public final class CachedRegionTracker implements Listener {
    public static final int CACHED_TO_END = -2;
    public static final int NOT_CACHED = -1;
    private static final String TAG = "CachedRegionTracker";
    private final Cache cache;
    private final String cacheKey;
    private final ChunkIndex chunkIndex;
    private final Region lookupRegion = new Region(0, 0);
    private final TreeSet<Region> regions = new TreeSet();

    private static class Region implements Comparable<Region> {
        public long endOffset;
        public int endOffsetIndex;
        public long startOffset;

        public Region(long position, long endOffset) {
            this.startOffset = position;
            this.endOffset = endOffset;
        }

        public int compareTo(Region another) {
            if (this.startOffset < another.startOffset) {
                return -1;
            }
            return this.startOffset == another.startOffset ? 0 : 1;
        }
    }

    public CachedRegionTracker(Cache cache, String cacheKey, ChunkIndex chunkIndex) {
        this.cache = cache;
        this.cacheKey = cacheKey;
        this.chunkIndex = chunkIndex;
        synchronized (this) {
            Iterator<CacheSpan> spanIterator = cache.addListener(cacheKey, this).descendingIterator();
            while (spanIterator.hasNext()) {
                mergeSpan((CacheSpan) spanIterator.next());
            }
        }
    }

    public void release() {
        this.cache.removeListener(this.cacheKey, this);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int getRegionEndTimeMs(long r12) {
        /*
        r11 = this;
        monitor-enter(r11);
        r0 = r11.lookupRegion;	 Catch:{ all -> 0x0069 }
        r0.startOffset = r12;	 Catch:{ all -> 0x0069 }
        r0 = r11.regions;	 Catch:{ all -> 0x0069 }
        r1 = r11.lookupRegion;	 Catch:{ all -> 0x0069 }
        r0 = r0.floor(r1);	 Catch:{ all -> 0x0069 }
        r0 = (org.telegram.messenger.exoplayer2.upstream.cache.CachedRegionTracker.Region) r0;	 Catch:{ all -> 0x0069 }
        r1 = -1;
        if (r0 == 0) goto L_0x0067;
    L_0x0012:
        r2 = r0.endOffset;	 Catch:{ all -> 0x0069 }
        r4 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1));
        if (r4 > 0) goto L_0x0067;
    L_0x0018:
        r2 = r0.endOffsetIndex;	 Catch:{ all -> 0x0069 }
        if (r2 != r1) goto L_0x001d;
    L_0x001c:
        goto L_0x0067;
    L_0x001d:
        r1 = r0.endOffsetIndex;	 Catch:{ all -> 0x0069 }
        r2 = r11.chunkIndex;	 Catch:{ all -> 0x0069 }
        r2 = r2.length;	 Catch:{ all -> 0x0069 }
        r2 = r2 + -1;
        if (r1 != r2) goto L_0x003f;
    L_0x0027:
        r2 = r0.endOffset;	 Catch:{ all -> 0x0069 }
        r4 = r11.chunkIndex;	 Catch:{ all -> 0x0069 }
        r4 = r4.offsets;	 Catch:{ all -> 0x0069 }
        r5 = r4[r1];	 Catch:{ all -> 0x0069 }
        r4 = r11.chunkIndex;	 Catch:{ all -> 0x0069 }
        r4 = r4.sizes;	 Catch:{ all -> 0x0069 }
        r4 = r4[r1];	 Catch:{ all -> 0x0069 }
        r7 = (long) r4;
        r9 = r5 + r7;
        r4 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1));
        if (r4 != 0) goto L_0x003f;
    L_0x003c:
        r2 = -2;
        monitor-exit(r11);
        return r2;
    L_0x003f:
        r2 = r11.chunkIndex;	 Catch:{ all -> 0x0069 }
        r2 = r2.durationsUs;	 Catch:{ all -> 0x0069 }
        r3 = r2[r1];	 Catch:{ all -> 0x0069 }
        r5 = r0.endOffset;	 Catch:{ all -> 0x0069 }
        r2 = r11.chunkIndex;	 Catch:{ all -> 0x0069 }
        r2 = r2.offsets;	 Catch:{ all -> 0x0069 }
        r7 = r2[r1];	 Catch:{ all -> 0x0069 }
        r9 = r5 - r7;
        r3 = r3 * r9;
        r2 = r11.chunkIndex;	 Catch:{ all -> 0x0069 }
        r2 = r2.sizes;	 Catch:{ all -> 0x0069 }
        r2 = r2[r1];	 Catch:{ all -> 0x0069 }
        r5 = (long) r2;	 Catch:{ all -> 0x0069 }
        r3 = r3 / r5;
        r2 = r3;
        r4 = r11.chunkIndex;	 Catch:{ all -> 0x0069 }
        r4 = r4.timesUs;	 Catch:{ all -> 0x0069 }
        r5 = r4[r1];	 Catch:{ all -> 0x0069 }
        r7 = r5 + r2;
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = r7 / r4;
        r4 = (int) r7;
        monitor-exit(r11);
        return r4;
    L_0x0067:
        monitor-exit(r11);
        return r1;
    L_0x0069:
        r12 = move-exception;
        monitor-exit(r11);
        throw r12;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.cache.CachedRegionTracker.getRegionEndTimeMs(long):int");
    }

    public synchronized void onSpanAdded(Cache cache, CacheSpan span) {
        mergeSpan(span);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void onSpanRemoved(org.telegram.messenger.exoplayer2.upstream.cache.Cache r10, org.telegram.messenger.exoplayer2.upstream.cache.CacheSpan r11) {
        /*
        r9 = this;
        monitor-enter(r9);
        r0 = new org.telegram.messenger.exoplayer2.upstream.cache.CachedRegionTracker$Region;	 Catch:{ all -> 0x006f }
        r1 = r11.position;	 Catch:{ all -> 0x006f }
        r3 = r11.position;	 Catch:{ all -> 0x006f }
        r5 = r11.length;	 Catch:{ all -> 0x006f }
        r7 = r3 + r5;
        r0.<init>(r1, r7);	 Catch:{ all -> 0x006f }
        r1 = r9.regions;	 Catch:{ all -> 0x006f }
        r1 = r1.floor(r0);	 Catch:{ all -> 0x006f }
        r1 = (org.telegram.messenger.exoplayer2.upstream.cache.CachedRegionTracker.Region) r1;	 Catch:{ all -> 0x006f }
        if (r1 != 0) goto L_0x0021;
    L_0x0018:
        r2 = "CachedRegionTracker";
        r3 = "Removed a span we were not aware of";
        android.util.Log.e(r2, r3);	 Catch:{ all -> 0x006f }
        monitor-exit(r9);
        return;
    L_0x0021:
        r2 = r9.regions;	 Catch:{ all -> 0x006f }
        r2.remove(r1);	 Catch:{ all -> 0x006f }
        r2 = r1.startOffset;	 Catch:{ all -> 0x006f }
        r4 = r0.startOffset;	 Catch:{ all -> 0x006f }
        r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r6 >= 0) goto L_0x004f;
    L_0x002e:
        r2 = new org.telegram.messenger.exoplayer2.upstream.cache.CachedRegionTracker$Region;	 Catch:{ all -> 0x006f }
        r3 = r1.startOffset;	 Catch:{ all -> 0x006f }
        r5 = r0.startOffset;	 Catch:{ all -> 0x006f }
        r2.<init>(r3, r5);	 Catch:{ all -> 0x006f }
        r3 = r9.chunkIndex;	 Catch:{ all -> 0x006f }
        r3 = r3.offsets;	 Catch:{ all -> 0x006f }
        r4 = r2.endOffset;	 Catch:{ all -> 0x006f }
        r3 = java.util.Arrays.binarySearch(r3, r4);	 Catch:{ all -> 0x006f }
        if (r3 >= 0) goto L_0x0047;
    L_0x0043:
        r4 = -r3;
        r4 = r4 + -2;
        goto L_0x0048;
    L_0x0047:
        r4 = r3;
    L_0x0048:
        r2.endOffsetIndex = r4;	 Catch:{ all -> 0x006f }
        r4 = r9.regions;	 Catch:{ all -> 0x006f }
        r4.add(r2);	 Catch:{ all -> 0x006f }
    L_0x004f:
        r2 = r1.endOffset;	 Catch:{ all -> 0x006f }
        r4 = r0.endOffset;	 Catch:{ all -> 0x006f }
        r6 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r6 <= 0) goto L_0x006d;
    L_0x0057:
        r2 = new org.telegram.messenger.exoplayer2.upstream.cache.CachedRegionTracker$Region;	 Catch:{ all -> 0x006f }
        r3 = r0.endOffset;	 Catch:{ all -> 0x006f }
        r5 = 1;
        r7 = r3 + r5;
        r3 = r1.endOffset;	 Catch:{ all -> 0x006f }
        r2.<init>(r7, r3);	 Catch:{ all -> 0x006f }
        r3 = r1.endOffsetIndex;	 Catch:{ all -> 0x006f }
        r2.endOffsetIndex = r3;	 Catch:{ all -> 0x006f }
        r3 = r9.regions;	 Catch:{ all -> 0x006f }
        r3.add(r2);	 Catch:{ all -> 0x006f }
    L_0x006d:
        monitor-exit(r9);
        return;
    L_0x006f:
        r10 = move-exception;
        monitor-exit(r9);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.cache.CachedRegionTracker.onSpanRemoved(org.telegram.messenger.exoplayer2.upstream.cache.Cache, org.telegram.messenger.exoplayer2.upstream.cache.CacheSpan):void");
    }

    public void onSpanTouched(Cache cache, CacheSpan oldSpan, CacheSpan newSpan) {
    }

    private void mergeSpan(CacheSpan span) {
        Region newRegion = new Region(span.position, span.position + span.length);
        Region floorRegion = (Region) this.regions.floor(newRegion);
        Region ceilingRegion = (Region) this.regions.ceiling(newRegion);
        boolean floorConnects = regionsConnect(floorRegion, newRegion);
        if (regionsConnect(newRegion, ceilingRegion)) {
            if (floorConnects) {
                floorRegion.endOffset = ceilingRegion.endOffset;
                floorRegion.endOffsetIndex = ceilingRegion.endOffsetIndex;
            } else {
                newRegion.endOffset = ceilingRegion.endOffset;
                newRegion.endOffsetIndex = ceilingRegion.endOffsetIndex;
                this.regions.add(newRegion);
            }
            this.regions.remove(ceilingRegion);
        } else if (floorConnects) {
            floorRegion.endOffset = newRegion.endOffset;
            index = floorRegion.endOffsetIndex;
            while (index < this.chunkIndex.length - 1 && this.chunkIndex.offsets[index + 1] <= floorRegion.endOffset) {
                index++;
            }
            floorRegion.endOffsetIndex = index;
        } else {
            index = Arrays.binarySearch(this.chunkIndex.offsets, newRegion.endOffset);
            newRegion.endOffsetIndex = index < 0 ? (-index) - 2 : index;
            this.regions.add(newRegion);
        }
    }

    private boolean regionsConnect(Region lower, Region upper) {
        return (lower == null || upper == null || lower.endOffset != upper.startOffset) ? false : true;
    }
}
