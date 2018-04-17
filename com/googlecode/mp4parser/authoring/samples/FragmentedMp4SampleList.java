package com.googlecode.mp4parser.authoring.samples;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TrackBox;
import com.coremedia.iso.boxes.fragment.MovieFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackExtendsBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentBox;
import com.coremedia.iso.boxes.fragment.TrackFragmentHeaderBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox;
import com.coremedia.iso.boxes.fragment.TrackRunBox.Entry;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.Path;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FragmentedMp4SampleList extends AbstractList<Sample> {
    private List<TrackFragmentBox> allTrafs;
    private int[] firstSamples;
    IsoFile[] fragments;
    private SoftReference<Sample>[] sampleCache;
    private int size_ = -1;
    Container topLevel;
    TrackBox trackBox = null;
    TrackExtendsBox trex = null;
    private Map<TrackRunBox, SoftReference<ByteBuffer>> trunDataCache = new HashMap();

    public FragmentedMp4SampleList(long track, Container topLevel, IsoFile... fragments) {
        this.topLevel = topLevel;
        this.fragments = fragments;
        for (TrackBox tb : Path.getPaths(topLevel, "moov[0]/trak")) {
            if (tb.getTrackHeaderBox().getTrackId() == track) {
                this.trackBox = tb;
            }
        }
        if (this.trackBox == null) {
            StringBuilder stringBuilder = new StringBuilder("This MP4 does not contain track ");
            stringBuilder.append(track);
            throw new RuntimeException(stringBuilder.toString());
        }
        for (TrackExtendsBox box : Path.getPaths(topLevel, "moov[0]/mvex[0]/trex")) {
            if (box.getTrackId() == this.trackBox.getTrackHeaderBox().getTrackId()) {
                this.trex = box;
            }
        }
        this.sampleCache = (SoftReference[]) Array.newInstance(SoftReference.class, size());
        initAllFragments();
    }

    private List<TrackFragmentBox> initAllFragments() {
        if (this.allTrafs != null) {
            return this.allTrafs;
        }
        int i;
        List<TrackFragmentBox> trafs = new ArrayList();
        for (MovieFragmentBox moof : this.topLevel.getBoxes(MovieFragmentBox.class)) {
            for (TrackFragmentBox trackFragmentBox : moof.getBoxes(TrackFragmentBox.class)) {
                if (trackFragmentBox.getTrackFragmentHeaderBox().getTrackId() == this.trackBox.getTrackHeaderBox().getTrackId()) {
                    trafs.add(trackFragmentBox);
                }
            }
        }
        if (this.fragments != null) {
            for (IsoFile fragment : this.fragments) {
                for (MovieFragmentBox moof2 : fragment.getBoxes(MovieFragmentBox.class)) {
                    for (TrackFragmentBox trackFragmentBox2 : moof2.getBoxes(TrackFragmentBox.class)) {
                        if (trackFragmentBox2.getTrackFragmentHeaderBox().getTrackId() == this.trackBox.getTrackHeaderBox().getTrackId()) {
                            trafs.add(trackFragmentBox2);
                        }
                    }
                }
            }
        }
        this.allTrafs = trafs;
        int firstSample = 1;
        this.firstSamples = new int[this.allTrafs.size()];
        for (i = 0; i < this.allTrafs.size(); i++) {
            this.firstSamples[i] = firstSample;
            firstSample += getTrafSize((TrackFragmentBox) this.allTrafs.get(i));
        }
        return trafs;
    }

    private int getTrafSize(TrackFragmentBox traf) {
        List<Box> boxes = traf.getBoxes();
        int size = 0;
        for (int i = 0; i < boxes.size(); i++) {
            Box b = (Box) boxes.get(i);
            if (b instanceof TrackRunBox) {
                size += CastUtils.l2i(((TrackRunBox) b).getSampleCount());
            }
        }
        return size;
    }

    public Sample get(int index) {
        FragmentedMp4SampleList fragmentedMp4SampleList;
        IOException e;
        if (this.sampleCache[index] != null) {
            Sample sample = (Sample) fragmentedMp4SampleList.sampleCache[index].get();
            Sample cachedSample = sample;
            if (sample != null) {
                return cachedSample;
            }
        }
        int targetIndex = index + 1;
        int j = fragmentedMp4SampleList.firstSamples.length - 1;
        while (targetIndex - fragmentedMp4SampleList.firstSamples[j] < 0) {
            j--;
            fragmentedMp4SampleList = this;
        }
        TrackFragmentBox trackFragmentBox = (TrackFragmentBox) fragmentedMp4SampleList.allTrafs.get(j);
        int sampleIndexWithInTraf = targetIndex - fragmentedMp4SampleList.firstSamples[j];
        MovieFragmentBox moof = (MovieFragmentBox) trackFragmentBox.getParent();
        int previousTrunsSize = 0;
        for (Box box : trackFragmentBox.getBoxes()) {
            if (box instanceof TrackRunBox) {
                TrackRunBox trun = (TrackRunBox) box;
                if (trun.getEntries().size() < sampleIndexWithInTraf - previousTrunsSize) {
                    previousTrunsSize += trun.getEntries().size();
                } else {
                    long defaultSampleSize;
                    long defaultSampleSize2;
                    SoftReference<ByteBuffer> softReference;
                    TrackFragmentHeaderBox trackFragmentHeaderBox;
                    ByteBuffer trunData;
                    List<Entry> trackRunEntries = trun.getEntries();
                    TrackFragmentHeaderBox tfhd = trackFragmentBox.getTrackFragmentHeaderBox();
                    boolean sampleSizePresent = trun.isSampleSizePresent();
                    boolean hasDefaultSampleSize = tfhd.hasDefaultSampleSize();
                    if (sampleSizePresent) {
                        defaultSampleSize = 0;
                    } else {
                        if (hasDefaultSampleSize) {
                            defaultSampleSize2 = tfhd.getDefaultSampleSize();
                        } else if (fragmentedMp4SampleList.trex == null) {
                            defaultSampleSize = 0;
                            throw new RuntimeException("File doesn't contain trex box but track fragments aren't fully self contained. Cannot determine sample size.");
                        } else {
                            defaultSampleSize = 0;
                            defaultSampleSize2 = fragmentedMp4SampleList.trex.getDefaultSampleSize();
                        }
                        defaultSampleSize = defaultSampleSize2;
                    }
                    SoftReference<ByteBuffer> trunDataRef = (SoftReference) fragmentedMp4SampleList.trunDataCache.get(trun);
                    ByteBuffer trunData2 = trunDataRef != null ? (ByteBuffer) trunDataRef.get() : null;
                    int j2;
                    TrackFragmentBox trackFragmentBox2;
                    if (trunData2 == null) {
                        Container parent;
                        long dataOffset;
                        long offset = 0;
                        if (tfhd.hasBaseDataOffset()) {
                            long offset2 = 0 + tfhd.getBaseDataOffset();
                            parent = moof.getParent();
                            offset = offset2;
                        } else {
                            parent = moof;
                        }
                        if (trun.isDataOffsetPresent()) {
                            softReference = trunDataRef;
                            trackFragmentHeaderBox = tfhd;
                            dataOffset = offset + ((long) trun.getDataOffset());
                        } else {
                            softReference = trunDataRef;
                            trackFragmentHeaderBox = tfhd;
                            dataOffset = offset;
                        }
                        int size = null;
                        trunData2 = trackRunEntries.iterator();
                        targetIndex = size;
                        while (trunData2.hasNext()) {
                            ByteBuffer byteBuffer;
                            Container base;
                            long offset3;
                            j2 = j;
                            trackFragmentBox2 = trackFragmentBox;
                            Entry e2 = (Entry) trunData2.next();
                            if (sampleSizePresent) {
                                byteBuffer = trunData2;
                                base = parent;
                                offset3 = dataOffset;
                                targetIndex = (int) (((long) targetIndex) + e2.getSampleSize());
                            } else {
                                byteBuffer = trunData2;
                                base = parent;
                                offset3 = dataOffset;
                                targetIndex = (int) (((long) targetIndex) + defaultSampleSize);
                            }
                            j = j2;
                            trackFragmentBox = trackFragmentBox2;
                            parent = base;
                            trunData2 = byteBuffer;
                            dataOffset = offset3;
                        }
                        try {
                            trunData2 = parent.getByteBuffer(dataOffset, (long) targetIndex);
                            try {
                                fragmentedMp4SampleList.trunDataCache.put(trun, new SoftReference(trunData2));
                                trunData = trunData2;
                            } catch (IOException e3) {
                                e = e3;
                                trunData = trunData2;
                                throw new RuntimeException(e);
                            }
                        } catch (IOException e4) {
                            e = e4;
                            throw new RuntimeException(e);
                        }
                    }
                    trunData = trunData2;
                    softReference = trunDataRef;
                    trackFragmentHeaderBox = tfhd;
                    int i = targetIndex;
                    j2 = j;
                    trackFragmentBox2 = trackFragmentBox;
                    int i2 = 0;
                    targetIndex = 0;
                    while (i2 < sampleIndexWithInTraf - previousTrunsSize) {
                        int offset4;
                        TrackRunBox trun2 = trun;
                        j = trackRunEntries;
                        TrackFragmentHeaderBox tfhd2 = trackFragmentHeaderBox;
                        SoftReference<ByteBuffer> trunDataRef2 = softReference;
                        if (sampleSizePresent) {
                            offset4 = (int) (((long) targetIndex) + ((Entry) j.get(i2)).getSampleSize());
                        } else {
                            offset4 = (int) (((long) targetIndex) + defaultSampleSize);
                        }
                        targetIndex = offset4;
                        i2++;
                        trackRunEntries = j;
                        trackFragmentHeaderBox = tfhd2;
                        trun = trun2;
                        softReference = trunDataRef2;
                        fragmentedMp4SampleList = this;
                    }
                    if (sampleSizePresent) {
                        defaultSampleSize2 = ((Entry) trackRunEntries.get(sampleIndexWithInTraf - previousTrunsSize)).getSampleSize();
                    } else {
                        defaultSampleSize2 = defaultSampleSize;
                    }
                    final ByteBuffer finalTrunData = trunData;
                    final int finalOffset = targetIndex;
                    AnonymousClass1 trun3 = new Sample() {
                    };
                    fragmentedMp4SampleList.sampleCache[index] = new SoftReference(trun3);
                    return trun3;
                }
            }
        }
        throw new RuntimeException("Couldn't find sample in the traf I was looking");
    }

    public int size() {
        if (this.size_ != -1) {
            return r0.size_;
        }
        int i = 0;
        Iterator it = r0.topLevel.getBoxes(MovieFragmentBox.class).iterator();
        while (true) {
            int i2 = 0;
            if (!it.hasNext()) {
                break;
            }
            for (TrackFragmentBox trackFragmentBox : ((MovieFragmentBox) it.next()).getBoxes(TrackFragmentBox.class)) {
                if (trackFragmentBox.getTrackFragmentHeaderBox().getTrackId() == r0.trackBox.getTrackHeaderBox().getTrackId()) {
                    i = (int) (((long) i) + ((TrackRunBox) trackFragmentBox.getBoxes(TrackRunBox.class).get(0)).getSampleCount());
                }
            }
        }
        IsoFile[] isoFileArr = r0.fragments;
        int length = isoFileArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            for (MovieFragmentBox moof : isoFileArr[i3].getBoxes(MovieFragmentBox.class)) {
                for (TrackFragmentBox trackFragmentBox2 : moof.getBoxes(TrackFragmentBox.class)) {
                    if (trackFragmentBox2.getTrackFragmentHeaderBox().getTrackId() == r0.trackBox.getTrackHeaderBox().getTrackId()) {
                        i = (int) (((long) i) + ((TrackRunBox) trackFragmentBox2.getBoxes(TrackRunBox.class).get(i2)).getSampleCount());
                        length = length;
                        i2 = 0;
                    }
                }
            }
        }
        r0.size_ = i;
        return i;
    }
}
