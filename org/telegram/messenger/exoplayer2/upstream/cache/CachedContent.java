package org.telegram.messenger.exoplayer2.upstream.cache;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.TreeSet;
import org.telegram.messenger.exoplayer2.upstream.cache.Cache.CacheException;
import org.telegram.messenger.exoplayer2.util.Assertions;

final class CachedContent {
    private final TreeSet<SimpleCacheSpan> cachedSpans;
    public final int id;
    public final String key;
    private long length;
    private boolean locked;

    public long getCachedBytesLength(long r1, long r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.upstream.cache.CachedContent.getCachedBytesLength(long, long):long
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
        r0 = r18;
        r2 = r15.getSpan(r16);
        r3 = r2.isHoleSpan();
        if (r3 == 0) goto L_0x0020;
    L_0x000c:
        r3 = r2.isOpenEnded();
        if (r3 == 0) goto L_0x0018;
    L_0x0012:
        r3 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        goto L_0x001a;
    L_0x0018:
        r3 = r2.length;
    L_0x001a:
        r3 = java.lang.Math.min(r3, r0);
        r3 = -r3;
        return r3;
    L_0x0020:
        r3 = r16 + r0;
        r5 = r2.position;
        r7 = r2.length;
        r9 = r5 + r7;
        r5 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r5 >= 0) goto L_0x0060;
    L_0x002c:
        r5 = r15;
        r6 = r5.cachedSpans;
        r7 = 0;
        r6 = r6.tailSet(r2, r7);
        r6 = r6.iterator();
        r7 = r6.hasNext();
        if (r7 == 0) goto L_0x0060;
    L_0x003e:
        r7 = r6.next();
        r7 = (org.telegram.messenger.exoplayer2.upstream.cache.SimpleCacheSpan) r7;
        r11 = r7.position;
        r8 = (r11 > r9 ? 1 : (r11 == r9 ? 0 : -1));
        if (r8 <= 0) goto L_0x004b;
    L_0x004a:
        goto L_0x0060;
    L_0x004b:
        r11 = r7.position;
        r13 = r6;
        r5 = r7.length;
        r14 = r7;
        r7 = r11 + r5;
        r9 = java.lang.Math.max(r9, r7);
        r5 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r5 < 0) goto L_0x005c;
    L_0x005b:
        goto L_0x0060;
        r6 = r13;
        r5 = r15;
        goto L_0x0038;
    L_0x0060:
        r5 = r9 - r16;
        r5 = java.lang.Math.min(r5, r0);
        return r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.upstream.cache.CachedContent.getCachedBytesLength(long, long):long");
    }

    public CachedContent(DataInputStream input) throws IOException {
        this(input.readInt(), input.readUTF(), input.readLong());
    }

    public CachedContent(int id, String key, long length) {
        this.id = id;
        this.key = key;
        this.length = length;
        this.cachedSpans = new TreeSet();
    }

    public void writeToStream(DataOutputStream output) throws IOException {
        output.writeInt(this.id);
        output.writeUTF(this.key);
        output.writeLong(this.length);
    }

    public long getLength() {
        return this.length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public void addSpan(SimpleCacheSpan span) {
        this.cachedSpans.add(span);
    }

    public TreeSet<SimpleCacheSpan> getSpans() {
        return this.cachedSpans;
    }

    public SimpleCacheSpan getSpan(long position) {
        SimpleCacheSpan lookupSpan = SimpleCacheSpan.createLookup(this.key, position);
        SimpleCacheSpan floorSpan = (SimpleCacheSpan) this.cachedSpans.floor(lookupSpan);
        if (floorSpan != null && floorSpan.position + floorSpan.length > position) {
            return floorSpan;
        }
        SimpleCacheSpan createOpenHole;
        SimpleCacheSpan ceilSpan = (SimpleCacheSpan) this.cachedSpans.ceiling(lookupSpan);
        if (ceilSpan == null) {
            createOpenHole = SimpleCacheSpan.createOpenHole(this.key, position);
        } else {
            createOpenHole = SimpleCacheSpan.createClosedHole(this.key, position, ceilSpan.position - position);
        }
        return createOpenHole;
    }

    public SimpleCacheSpan touch(SimpleCacheSpan cacheSpan) throws CacheException {
        Assertions.checkState(this.cachedSpans.remove(cacheSpan));
        SimpleCacheSpan newCacheSpan = cacheSpan.copyWithUpdatedLastAccessTime(this.id);
        if (cacheSpan.file.renameTo(newCacheSpan.file)) {
            this.cachedSpans.add(newCacheSpan);
            return newCacheSpan;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Renaming of ");
        stringBuilder.append(cacheSpan.file);
        stringBuilder.append(" to ");
        stringBuilder.append(newCacheSpan.file);
        stringBuilder.append(" failed.");
        throw new CacheException(stringBuilder.toString());
    }

    public boolean isEmpty() {
        return this.cachedSpans.isEmpty();
    }

    public boolean removeSpan(CacheSpan span) {
        if (!this.cachedSpans.remove(span)) {
            return false;
        }
        span.file.delete();
        return true;
    }

    public int headerHashCode() {
        return (31 * ((31 * this.id) + this.key.hashCode())) + ((int) (this.length ^ (this.length >>> 32)));
    }
}
