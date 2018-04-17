package com.googlecode.mp4parser.authoring.samples;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieBox;
import com.coremedia.iso.boxes.SampleSizeBox;
import com.coremedia.iso.boxes.SampleToChunkBox.Entry;
import com.coremedia.iso.boxes.TrackBox;
import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.util.CastUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import org.telegram.tgnet.ConnectionsManager;

public class DefaultMp4SampleList extends AbstractList<Sample> {
    ByteBuffer[][] cache = null;
    int[] chunkNumsStartSampleNum;
    long[] chunkOffsets;
    long[] chunkSizes;
    int lastChunk = 0;
    long[][] sampleOffsetsWithinChunks;
    SampleSizeBox ssb;
    Container topLevel;
    TrackBox trackBox = null;

    public DefaultMp4SampleList(long track, Container topLevel) {
        long j = track;
        Container container = topLevel;
        this.topLevel = container;
        MovieBox movieBox = (MovieBox) container.getBoxes(MovieBox.class).get(0);
        List<TrackBox> trackBoxes = movieBox.getBoxes(TrackBox.class);
        for (TrackBox tb : trackBoxes) {
            MovieBox movieBox2 = movieBox;
            List<TrackBox> list = trackBoxes;
            if (tb.getTrackHeaderBox().getTrackId() == j) {
                r0.trackBox = tb;
            }
            movieBox = movieBox2;
            trackBoxes = list;
            container = topLevel;
        }
        if (r0.trackBox == null) {
            StringBuilder stringBuilder = new StringBuilder("This MP4 does not contain track ");
            stringBuilder.append(j);
            throw new RuntimeException(stringBuilder.toString());
        }
        int s2cIndex;
        r0.chunkOffsets = r0.trackBox.getSampleTableBox().getChunkOffsetBox().getChunkOffsets();
        r0.chunkSizes = new long[r0.chunkOffsets.length];
        r0.cache = new ByteBuffer[r0.chunkOffsets.length][];
        r0.sampleOffsetsWithinChunks = new long[r0.chunkOffsets.length][];
        r0.ssb = r0.trackBox.getSampleTableBox().getSampleSizeBox();
        List<Entry> s2chunkEntries = r0.trackBox.getSampleTableBox().getSampleToChunkBox().getEntries();
        Entry[] entries = (Entry[]) s2chunkEntries.toArray(new Entry[s2chunkEntries.size()]);
        int s2cIndex2 = 0 + 1;
        Entry next = entries[0];
        int currentChunkNo = 0;
        int currentSamplePerChunk = 0;
        long nextFirstChunk = next.getFirstChunk();
        int nextSamplePerChunk = CastUtils.l2i(next.getSamplesPerChunk());
        int currentSampleNo = 1;
        int lastSampleNo = size();
        while (true) {
            int currentSamplePerChunk2;
            int lastSampleNo2 = lastSampleNo;
            currentChunkNo++;
            movieBox2 = movieBox;
            if (((long) currentChunkNo) == nextFirstChunk) {
                currentSamplePerChunk2 = nextSamplePerChunk;
                if (entries.length > s2cIndex2) {
                    movieBox = s2cIndex2 + 1;
                    next = entries[s2cIndex2];
                    int currentSamplePerChunk3 = currentSamplePerChunk2;
                    int s2cIndex3 = movieBox;
                    nextSamplePerChunk = CastUtils.l2i(next.getSamplesPerChunk());
                    nextFirstChunk = next.getFirstChunk();
                    currentSamplePerChunk = currentSamplePerChunk3;
                    s2cIndex2 = s2cIndex3;
                } else {
                    nextSamplePerChunk = -1;
                    nextFirstChunk = Long.MAX_VALUE;
                    currentSamplePerChunk = currentSamplePerChunk2;
                }
            }
            list = trackBoxes;
            r0.sampleOffsetsWithinChunks[currentChunkNo - 1] = new long[currentSamplePerChunk];
            currentSamplePerChunk2 = currentSampleNo + currentSamplePerChunk;
            currentSampleNo = currentSamplePerChunk2;
            movieBox = lastSampleNo2;
            if (currentSamplePerChunk2 > movieBox) {
                break;
            }
            MovieBox movieBox3 = movieBox;
            movieBox = movieBox2;
            trackBoxes = list;
            lastSampleNo = movieBox3;
            container = topLevel;
        }
        r0.chunkNumsStartSampleNum = new int[(currentChunkNo + 1)];
        int s2cIndex4 = 0 + 1;
        Entry next2 = entries[0];
        int currentChunkNo2 = 0;
        s2cIndex2 = 0;
        long nextFirstChunk2 = next2.getFirstChunk();
        int nextSamplePerChunk2 = CastUtils.l2i(next2.getSamplesPerChunk());
        int currentSampleNo2 = 1;
        while (true) {
            currentSampleNo = currentChunkNo2 + 1;
            r0.chunkNumsStartSampleNum[currentChunkNo2] = currentSampleNo2;
            List<Entry> s2chunkEntries2 = s2chunkEntries;
            if (((long) currentSampleNo) == nextFirstChunk2) {
                int currentSamplePerChunk4;
                currentChunkNo2 = nextSamplePerChunk2;
                if (entries.length > s2cIndex4) {
                    s2cIndex = s2cIndex4 + 1;
                    next2 = entries[s2cIndex4];
                    currentSamplePerChunk4 = currentChunkNo2;
                    s2cIndex4 = CastUtils.l2i(next2.getSamplesPerChunk());
                    s2cIndex2 = next2.getFirstChunk();
                    nextSamplePerChunk2 = s2cIndex4;
                    s2cIndex4 = s2cIndex;
                } else {
                    currentSamplePerChunk4 = currentChunkNo2;
                    s2cIndex2 = Long.MAX_VALUE;
                    nextSamplePerChunk2 = -1;
                }
                nextFirstChunk2 = s2cIndex2;
                s2cIndex2 = currentSamplePerChunk4;
            }
            currentChunkNo2 = currentSampleNo2 + s2cIndex2;
            currentSampleNo2 = currentChunkNo2;
            if (currentChunkNo2 > movieBox) {
                break;
            }
            int i = s2cIndex4;
            currentChunkNo2 = currentSampleNo;
            s2chunkEntries = s2chunkEntries2;
        }
        r0.chunkNumsStartSampleNum[currentSampleNo] = ConnectionsManager.DEFAULT_DATACENTER_ID;
        currentChunkNo2 = 0;
        long sampleSum = 0;
        s2cIndex = 1;
        while (true) {
            Entry next3 = next2;
            int lastSampleNo3 = movieBox;
            i = s2cIndex4;
            if (((long) s2cIndex) <= r0.ssb.getSampleCount()) {
                while (s2cIndex == r0.chunkNumsStartSampleNum[currentChunkNo2]) {
                    currentChunkNo2++;
                    sampleSum = 0;
                }
                next2 = r0.chunkSizes;
                movieBox = currentChunkNo2 - 1;
                Entry[] entries2 = entries;
                next2[movieBox] = next2[movieBox] + r0.ssb.getSampleSizeAtIndex(s2cIndex - 1);
                r0.sampleOffsetsWithinChunks[currentChunkNo2 - 1][s2cIndex - r0.chunkNumsStartSampleNum[currentChunkNo2 - 1]] = sampleSum;
                s2cIndex++;
                sampleSum += r0.ssb.getSampleSizeAtIndex(s2cIndex - 1);
                movieBox = lastSampleNo3;
                next2 = next3;
                s2cIndex4 = i;
                entries = entries2;
            } else {
                return;
            }
        }
    }

    synchronized int getChunkForSample(int index) {
        int sampleNum = index + 1;
        if (sampleNum >= this.chunkNumsStartSampleNum[this.lastChunk] && sampleNum < this.chunkNumsStartSampleNum[this.lastChunk + 1]) {
            return this.lastChunk;
        } else if (sampleNum < this.chunkNumsStartSampleNum[this.lastChunk]) {
            this.lastChunk = 0;
            while (this.chunkNumsStartSampleNum[this.lastChunk + 1] <= sampleNum) {
                this.lastChunk++;
            }
            return this.lastChunk;
        } else {
            this.lastChunk++;
            while (this.chunkNumsStartSampleNum[this.lastChunk + 1] <= sampleNum) {
                this.lastChunk++;
            }
            return this.lastChunk;
        }
    }

    public Sample get(int index) {
        int i = index;
        if (((long) i) >= this.ssb.getSampleCount()) {
            throw new IndexOutOfBoundsException();
        }
        long j;
        ByteBuffer[] chunkBuffers;
        int chunkNumber = getChunkForSample(index);
        int chunkStartSample = r8.chunkNumsStartSampleNum[chunkNumber] - 1;
        long chunkOffset = r8.chunkOffsets[CastUtils.l2i((long) chunkNumber)];
        int sampleInChunk = i - chunkStartSample;
        long[] sampleOffsetsWithinChunk = r8.sampleOffsetsWithinChunks[CastUtils.l2i((long) chunkNumber)];
        long offsetWithInChunk = sampleOffsetsWithinChunk[sampleInChunk];
        ByteBuffer[] chunkBuffers2 = r8.cache[CastUtils.l2i((long) chunkNumber)];
        long chunkOffset2;
        if (chunkBuffers2 == null) {
            ByteBuffer[] chunkBuffers3;
            long currentStart;
            List<ByteBuffer> _chunkBuffers = new ArrayList();
            long currentStart2 = 0;
            int i2 = 0;
            while (true) {
                j = offsetWithInChunk;
                try {
                    if (i2 >= sampleOffsetsWithinChunk.length) {
                        break;
                    }
                    chunkBuffers3 = chunkBuffers2;
                    currentStart = currentStart2;
                    chunkOffset2 = chunkOffset;
                    if ((sampleOffsetsWithinChunk[i2] + r8.ssb.getSampleSizeAtIndex(i2 + chunkStartSample)) - currentStart > 268435456) {
                        _chunkBuffers.add(r8.topLevel.getByteBuffer(chunkOffset2 + currentStart, sampleOffsetsWithinChunk[i2] - currentStart));
                        currentStart2 = sampleOffsetsWithinChunk[i2];
                    } else {
                        currentStart2 = currentStart;
                    }
                    i2++;
                    offsetWithInChunk = j;
                    chunkBuffers2 = chunkBuffers3;
                    chunkOffset = chunkOffset2;
                } catch (IOException e) {
                    chunkBuffers3 = chunkBuffers2;
                    currentStart = currentStart2;
                    chunkOffset2 = chunkOffset;
                    offsetWithInChunk = e;
                }
            }
            try {
            } catch (IOException e2) {
                currentStart = currentStart2;
                offsetWithInChunk = e2;
                throw new IndexOutOfBoundsException(offsetWithInChunk.getMessage());
            }
            try {
                _chunkBuffers.add(r8.topLevel.getByteBuffer(chunkOffset + currentStart2, ((-currentStart2) + sampleOffsetsWithinChunk[sampleOffsetsWithinChunk.length - 1]) + r8.ssb.getSampleSizeAtIndex((sampleOffsetsWithinChunk.length + chunkStartSample) - 1)));
                chunkBuffers2 = (ByteBuffer[]) _chunkBuffers.toArray(new ByteBuffer[_chunkBuffers.size()]);
            } catch (IOException e22) {
                offsetWithInChunk = e22;
            }
            try {
                r8.cache[CastUtils.l2i((long) chunkNumber)] = chunkBuffers2;
                chunkBuffers = chunkBuffers2;
            } catch (IOException e222) {
                offsetWithInChunk = e222;
                chunkBuffers3 = chunkBuffers2;
                throw new IndexOutOfBoundsException(offsetWithInChunk.getMessage());
            }
        }
        j = offsetWithInChunk;
        chunkOffset2 = chunkOffset;
        chunkBuffers = chunkBuffers2;
        ByteBuffer correctPartOfChunk = null;
        int length = chunkBuffers.length;
        int i3 = 0;
        while (i3 < length) {
            ByteBuffer chunkBuffer = chunkBuffers[i3];
            if (j < ((long) chunkBuffer.limit())) {
                correctPartOfChunk = chunkBuffer;
                break;
            }
            i3++;
            j -= (long) chunkBuffer.limit();
        }
        final ByteBuffer finalCorrectPartOfChunk = correctPartOfChunk;
        final long finalOffsetWithInChunk = j;
        final long sampleSizeAtIndex = r8.ssb.getSampleSizeAtIndex(i);
        return new Sample() {
            public String toString() {
                StringBuilder stringBuilder = new StringBuilder("DefaultMp4Sample(size:");
                stringBuilder.append(sampleSizeAtIndex);
                stringBuilder.append(")");
                return stringBuilder.toString();
            }
        };
    }

    public int size() {
        return CastUtils.l2i(this.trackBox.getSampleTableBox().getSampleSizeBox().getSampleCount());
    }
}
