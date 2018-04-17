package com.coremedia.iso.boxes;

import com.coremedia.iso.IsoTypeReader;
import com.coremedia.iso.IsoTypeWriter;
import com.googlecode.mp4parser.AbstractFullBox;
import com.googlecode.mp4parser.RequiresParseDetailAspect;
import com.googlecode.mp4parser.util.CastUtils;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.aspectj.lang.JoinPoint.StaticPart;
import org.aspectj.runtime.reflect.Factory;

public class TimeToSampleBox extends AbstractFullBox {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String TYPE = "stts";
    private static final /* synthetic */ StaticPart ajc$tjp_0 = null;
    private static final /* synthetic */ StaticPart ajc$tjp_1 = null;
    private static final /* synthetic */ StaticPart ajc$tjp_2 = null;
    static Map<List<Entry>, SoftReference<long[]>> cache = new WeakHashMap();
    List<Entry> entries = Collections.emptyList();

    public static class Entry {
        long count;
        long delta;

        public Entry(long count, long delta) {
            this.count = count;
            this.delta = delta;
        }

        public long getCount() {
            return this.count;
        }

        public long getDelta() {
            return this.delta;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public void setDelta(long delta) {
            this.delta = delta;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("Entry{count=");
            stringBuilder.append(this.count);
            stringBuilder.append(", delta=");
            stringBuilder.append(this.delta);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }

    private static /* synthetic */ void ajc$preClinit() {
        Factory factory = new Factory("TimeToSampleBox.java", TimeToSampleBox.class);
        ajc$tjp_0 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "getEntries", "com.coremedia.iso.boxes.TimeToSampleBox", TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, "java.util.List"), 79);
        ajc$tjp_1 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "setEntries", "com.coremedia.iso.boxes.TimeToSampleBox", "java.util.List", "entries", TtmlNode.ANONYMOUS_REGION_ID, "void"), 83);
        ajc$tjp_2 = factory.makeSJP("method-execution", factory.makeMethodSig("1", "toString", "com.coremedia.iso.boxes.TimeToSampleBox", TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, TtmlNode.ANONYMOUS_REGION_ID, "java.lang.String"), 87);
    }

    static {
        ajc$preClinit();
    }

    public TimeToSampleBox() {
        super(TYPE);
    }

    protected long getContentSize() {
        return (long) (8 + (this.entries.size() * 8));
    }

    public void _parseDetails(ByteBuffer content) {
        parseVersionAndFlags(content);
        int entryCount = CastUtils.l2i(IsoTypeReader.readUInt32(content));
        this.entries = new ArrayList(entryCount);
        for (int i = 0; i < entryCount; i++) {
            this.entries.add(new Entry(IsoTypeReader.readUInt32(content), IsoTypeReader.readUInt32(content)));
        }
    }

    protected void getContent(ByteBuffer byteBuffer) {
        writeVersionAndFlags(byteBuffer);
        IsoTypeWriter.writeUInt32(byteBuffer, (long) this.entries.size());
        for (Entry entry : this.entries) {
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getCount());
            IsoTypeWriter.writeUInt32(byteBuffer, entry.getDelta());
        }
    }

    public List<Entry> getEntries() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_0, this, this));
        return this.entries;
    }

    public void setEntries(List<Entry> entries) {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_1, (Object) this, (Object) this, (Object) entries));
        this.entries = entries;
    }

    public String toString() {
        RequiresParseDetailAspect.aspectOf().before(Factory.makeJP(ajc$tjp_2, this, this));
        StringBuilder stringBuilder = new StringBuilder("TimeToSampleBox[entryCount=");
        stringBuilder.append(this.entries.size());
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static synchronized long[] blowupTimeToSamples(List<Entry> entries) {
        synchronized (TimeToSampleBox.class) {
            long[] jArr;
            SoftReference softReference = (SoftReference) cache.get(entries);
            SoftReference cacheEntry = softReference;
            if (softReference != null) {
                jArr = (long[]) cacheEntry.get();
                long[] cacheVal = jArr;
                if (jArr != null) {
                    return cacheVal;
                }
            }
            long numOfSamples = 0;
            for (Entry entry : entries) {
                numOfSamples += entry.getCount();
            }
            jArr = new long[((int) numOfSamples)];
            int current = 0;
            for (Entry entry2 : entries) {
                int i = 0;
                while (((long) i) < entry2.getCount()) {
                    int current2 = current + 1;
                    jArr[current] = entry2.getDelta();
                    i++;
                    current = current2;
                }
            }
            cache.put(entries, new SoftReference(jArr));
            return jArr;
        }
    }
}
