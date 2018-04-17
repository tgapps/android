package org.telegram.messenger.exoplayer2.source.chunk;

import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.FormatHolder;
import org.telegram.messenger.exoplayer2.SeekParameters;
import org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer;
import org.telegram.messenger.exoplayer2.source.MediaSourceEventListener.EventDispatcher;
import org.telegram.messenger.exoplayer2.source.SampleQueue;
import org.telegram.messenger.exoplayer2.source.SampleStream;
import org.telegram.messenger.exoplayer2.source.SequenceableLoader;
import org.telegram.messenger.exoplayer2.upstream.Allocator;
import org.telegram.messenger.exoplayer2.upstream.Loader;
import org.telegram.messenger.exoplayer2.upstream.Loader.Callback;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.Util;

public class ChunkSampleStream<T extends ChunkSource> implements SampleStream, SequenceableLoader, Callback<Chunk>, org.telegram.messenger.exoplayer2.upstream.Loader.ReleaseCallback {
    private static final String TAG = "ChunkSampleStream";
    private final SequenceableLoader.Callback<ChunkSampleStream<T>> callback;
    private final T chunkSource;
    long decodeOnlyUntilPositionUs;
    private final SampleQueue[] embeddedSampleQueues;
    private final Format[] embeddedTrackFormats;
    private final int[] embeddedTrackTypes;
    private final boolean[] embeddedTracksSelected;
    private final EventDispatcher eventDispatcher;
    private long lastSeekPositionUs;
    private final Loader loader = new Loader("Loader:ChunkSampleStream");
    boolean loadingFinished;
    private final BaseMediaChunkOutput mediaChunkOutput;
    private final ArrayList<BaseMediaChunk> mediaChunks = new ArrayList();
    private final int minLoadableRetryCount;
    private final ChunkHolder nextChunkHolder = new ChunkHolder();
    private long pendingResetPositionUs;
    private Format primaryDownstreamTrackFormat;
    private final SampleQueue primarySampleQueue;
    public final int primaryTrackType;
    private final List<BaseMediaChunk> readOnlyMediaChunks = Collections.unmodifiableList(this.mediaChunks);
    private ReleaseCallback<T> releaseCallback;

    public interface ReleaseCallback<T extends ChunkSource> {
        void onSampleStreamReleased(ChunkSampleStream<T> chunkSampleStream);
    }

    public final class EmbeddedSampleStream implements SampleStream {
        private boolean formatNotificationSent;
        private final int index;
        public final ChunkSampleStream<T> parent;
        private final SampleQueue sampleQueue;

        public EmbeddedSampleStream(ChunkSampleStream<T> parent, SampleQueue sampleQueue, int index) {
            this.parent = parent;
            this.sampleQueue = sampleQueue;
            this.index = index;
        }

        public boolean isReady() {
            if (!ChunkSampleStream.this.loadingFinished) {
                if (ChunkSampleStream.this.isPendingReset() || !this.sampleQueue.hasNextSample()) {
                    return false;
                }
            }
            return true;
        }

        public int skipData(long positionUs) {
            int skipCount;
            if (!ChunkSampleStream.this.loadingFinished || positionUs <= this.sampleQueue.getLargestQueuedTimestampUs()) {
                skipCount = this.sampleQueue.advanceTo(positionUs, true, true);
                if (skipCount == -1) {
                    skipCount = 0;
                }
            } else {
                skipCount = this.sampleQueue.advanceToEnd();
            }
            if (skipCount > 0) {
                maybeNotifyTrackFormatChanged();
            }
            return skipCount;
        }

        public void maybeThrowError() throws IOException {
        }

        public int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired) {
            if (ChunkSampleStream.this.isPendingReset()) {
                return -3;
            }
            int result = this.sampleQueue.read(formatHolder, buffer, formatRequired, ChunkSampleStream.this.loadingFinished, ChunkSampleStream.this.decodeOnlyUntilPositionUs);
            if (result == -4) {
                maybeNotifyTrackFormatChanged();
            }
            return result;
        }

        public void release() {
            Assertions.checkState(ChunkSampleStream.this.embeddedTracksSelected[this.index]);
            ChunkSampleStream.this.embeddedTracksSelected[this.index] = false;
        }

        private void maybeNotifyTrackFormatChanged() {
            if (!this.formatNotificationSent) {
                ChunkSampleStream.this.eventDispatcher.downstreamFormatChanged(ChunkSampleStream.this.embeddedTrackTypes[this.index], ChunkSampleStream.this.embeddedTrackFormats[this.index], 0, null, ChunkSampleStream.this.lastSeekPositionUs);
                this.formatNotificationSent = true;
            }
        }
    }

    public void seekToUs(long r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.chunk.ChunkSampleStream.seekToUs(long):void
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
        r8.lastSeekPositionUs = r9;
        r0 = r8.primarySampleQueue;
        r0.rewind();
        r0 = r8.isPendingReset();
        r1 = 1;
        r2 = 0;
        if (r0 == 0) goto L_0x0012;
    L_0x000f:
        r0 = 0;
        r3 = r0;
        goto L_0x0061;
    L_0x0012:
        r0 = 0;
        r3 = r2;
    L_0x0014:
        r4 = r8.mediaChunks;
        r4 = r4.size();
        if (r3 >= r4) goto L_0x0034;
    L_0x001c:
        r4 = r8.mediaChunks;
        r4 = r4.get(r3);
        r4 = (org.telegram.messenger.exoplayer2.source.chunk.BaseMediaChunk) r4;
        r5 = r4.startTimeUs;
        r7 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1));
        if (r7 != 0) goto L_0x002c;
    L_0x002a:
        r0 = r4;
        goto L_0x0034;
    L_0x002c:
        r7 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1));
        if (r7 <= 0) goto L_0x0031;
    L_0x0030:
        goto L_0x0034;
    L_0x0031:
        r3 = r3 + 1;
        goto L_0x0014;
    L_0x0034:
        if (r0 == 0) goto L_0x0045;
    L_0x0036:
        r3 = r8.primarySampleQueue;
        r4 = r0.getFirstSampleIndex(r2);
        r3 = r3.setReadPosition(r4);
        r4 = -9223372036854775808;
        r8.decodeOnlyUntilPositionUs = r4;
        goto L_0x0061;
    L_0x0045:
        r3 = r8.primarySampleQueue;
        r4 = r8.getNextLoadPositionUs();
        r6 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1));
        if (r6 >= 0) goto L_0x0051;
    L_0x004f:
        r4 = r1;
        goto L_0x0053;
        r4 = r2;
        r3 = r3.advanceTo(r9, r1, r4);
        r4 = -1;
        if (r3 == r4) goto L_0x005c;
        r3 = r1;
        goto L_0x005d;
        r3 = r2;
        r4 = r8.lastSeekPositionUs;
        r8.decodeOnlyUntilPositionUs = r4;
    L_0x0061:
        if (r3 == 0) goto L_0x0074;
        r0 = r8.embeddedSampleQueues;
        r4 = r0.length;
        r5 = r2;
        if (r5 >= r4) goto L_0x009d;
        r6 = r0[r5];
        r6.rewind();
        r6.advanceTo(r9, r1, r2);
        r5 = r5 + 1;
        goto L_0x0067;
        r8.pendingResetPositionUs = r9;
        r8.loadingFinished = r2;
        r0 = r8.mediaChunks;
        r0.clear();
        r0 = r8.loader;
        r0 = r0.isLoading();
        if (r0 == 0) goto L_0x008b;
        r0 = r8.loader;
        r0.cancelLoading();
        goto L_0x009d;
        r0 = r8.primarySampleQueue;
        r0.reset();
        r0 = r8.embeddedSampleQueues;
        r1 = r0.length;
        if (r2 >= r1) goto L_0x009d;
        r4 = r0[r2];
        r4.reset();
        r2 = r2 + 1;
        goto L_0x0093;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.chunk.ChunkSampleStream.seekToUs(long):void");
    }

    public ChunkSampleStream(int primaryTrackType, int[] embeddedTrackTypes, Format[] embeddedTrackFormats, T chunkSource, SequenceableLoader.Callback<ChunkSampleStream<T>> callback, Allocator allocator, long positionUs, int minLoadableRetryCount, EventDispatcher eventDispatcher) {
        int i = primaryTrackType;
        int[] iArr = embeddedTrackTypes;
        Allocator allocator2 = allocator;
        long j = positionUs;
        this.primaryTrackType = i;
        this.embeddedTrackTypes = iArr;
        this.embeddedTrackFormats = embeddedTrackFormats;
        this.chunkSource = chunkSource;
        this.callback = callback;
        this.eventDispatcher = eventDispatcher;
        this.minLoadableRetryCount = minLoadableRetryCount;
        int i2 = 0;
        int embeddedTrackCount = iArr == null ? 0 : iArr.length;
        r0.embeddedSampleQueues = new SampleQueue[embeddedTrackCount];
        r0.embeddedTracksSelected = new boolean[embeddedTrackCount];
        int[] trackTypes = new int[(1 + embeddedTrackCount)];
        SampleQueue[] sampleQueues = new SampleQueue[(1 + embeddedTrackCount)];
        r0.primarySampleQueue = new SampleQueue(allocator2);
        trackTypes[0] = i;
        sampleQueues[0] = r0.primarySampleQueue;
        while (i2 < embeddedTrackCount) {
            SampleQueue sampleQueue = new SampleQueue(allocator2);
            r0.embeddedSampleQueues[i2] = sampleQueue;
            sampleQueues[i2 + 1] = sampleQueue;
            trackTypes[i2 + 1] = iArr[i2];
            i2++;
            i = primaryTrackType;
        }
        r0.mediaChunkOutput = new BaseMediaChunkOutput(trackTypes, sampleQueues);
        r0.pendingResetPositionUs = j;
        r0.lastSeekPositionUs = j;
    }

    public void discardBuffer(long positionUs, boolean toKeyframe) {
        int oldFirstIndex = this.primarySampleQueue.getFirstIndex();
        this.primarySampleQueue.discardTo(positionUs, toKeyframe, true);
        int newFirstIndex = this.primarySampleQueue.getFirstIndex();
        if (newFirstIndex > oldFirstIndex) {
            long discardToUs = this.primarySampleQueue.getFirstTimestampUs();
            for (int i = 0; i < this.embeddedSampleQueues.length; i++) {
                this.embeddedSampleQueues[i].discardTo(discardToUs, toKeyframe, this.embeddedTracksSelected[i]);
            }
            discardDownstreamMediaChunks(newFirstIndex);
        }
    }

    public EmbeddedSampleStream selectEmbeddedTrack(long positionUs, int trackType) {
        for (int i = 0; i < this.embeddedSampleQueues.length; i++) {
            if (this.embeddedTrackTypes[i] == trackType) {
                Assertions.checkState(this.embeddedTracksSelected[i] ^ true);
                this.embeddedTracksSelected[i] = true;
                this.embeddedSampleQueues[i].rewind();
                this.embeddedSampleQueues[i].advanceTo(positionUs, true, true);
                return new EmbeddedSampleStream(this, this.embeddedSampleQueues[i], i);
            }
        }
        throw new IllegalStateException();
    }

    public T getChunkSource() {
        return this.chunkSource;
    }

    public long getBufferedPositionUs() {
        if (this.loadingFinished) {
            return Long.MIN_VALUE;
        }
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        long bufferedPositionUs = this.lastSeekPositionUs;
        BaseMediaChunk lastMediaChunk = getLastMediaChunk();
        BaseMediaChunk lastCompletedMediaChunk = lastMediaChunk.isLoadCompleted() ? lastMediaChunk : this.mediaChunks.size() > 1 ? (BaseMediaChunk) this.mediaChunks.get(this.mediaChunks.size() - 2) : null;
        if (lastCompletedMediaChunk != null) {
            bufferedPositionUs = Math.max(bufferedPositionUs, lastCompletedMediaChunk.endTimeUs);
        }
        return Math.max(bufferedPositionUs, this.primarySampleQueue.getLargestQueuedTimestampUs());
    }

    public long getAdjustedSeekPositionUs(long positionUs, SeekParameters seekParameters) {
        return this.chunkSource.getAdjustedSeekPositionUs(positionUs, seekParameters);
    }

    public void release() {
        release(null);
    }

    public void release(ReleaseCallback<T> callback) {
        this.releaseCallback = callback;
        if (!this.loader.release(this)) {
            this.primarySampleQueue.discardToEnd();
            for (SampleQueue embeddedSampleQueue : this.embeddedSampleQueues) {
                embeddedSampleQueue.discardToEnd();
            }
        }
    }

    public void onLoaderReleased() {
        this.primarySampleQueue.reset();
        for (SampleQueue embeddedSampleQueue : this.embeddedSampleQueues) {
            embeddedSampleQueue.reset();
        }
        if (this.releaseCallback != null) {
            this.releaseCallback.onSampleStreamReleased(this);
        }
    }

    public boolean isReady() {
        if (!this.loadingFinished) {
            if (isPendingReset() || !this.primarySampleQueue.hasNextSample()) {
                return false;
            }
        }
        return true;
    }

    public void maybeThrowError() throws IOException {
        this.loader.maybeThrowError();
        if (!this.loader.isLoading()) {
            this.chunkSource.maybeThrowError();
        }
    }

    public int readData(FormatHolder formatHolder, DecoderInputBuffer buffer, boolean formatRequired) {
        if (isPendingReset()) {
            return -3;
        }
        int result = this.primarySampleQueue.read(formatHolder, buffer, formatRequired, this.loadingFinished, this.decodeOnlyUntilPositionUs);
        if (result == -4) {
            maybeNotifyPrimaryTrackFormatChanged(this.primarySampleQueue.getReadIndex(), 1);
        }
        return result;
    }

    public int skipData(long positionUs) {
        if (isPendingReset()) {
            return 0;
        }
        int skipCount;
        if (!this.loadingFinished || positionUs <= this.primarySampleQueue.getLargestQueuedTimestampUs()) {
            skipCount = this.primarySampleQueue.advanceTo(positionUs, true, true);
            if (skipCount == -1) {
                skipCount = 0;
            }
        } else {
            skipCount = this.primarySampleQueue.advanceToEnd();
        }
        if (skipCount > 0) {
            maybeNotifyPrimaryTrackFormatChanged(this.primarySampleQueue.getReadIndex(), skipCount);
        }
        return skipCount;
    }

    public void onLoadCompleted(Chunk loadable, long elapsedRealtimeMs, long loadDurationMs) {
        Chunk chunk = loadable;
        this.chunkSource.onChunkLoadCompleted(chunk);
        this.eventDispatcher.loadCompleted(chunk.dataSpec, chunk.type, this.primaryTrackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        this.callback.onContinueLoadingRequested(this);
    }

    public void onLoadCanceled(Chunk loadable, long elapsedRealtimeMs, long loadDurationMs, boolean released) {
        Chunk chunk = loadable;
        this.eventDispatcher.loadCanceled(chunk.dataSpec, chunk.type, this.primaryTrackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, elapsedRealtimeMs, loadDurationMs, loadable.bytesLoaded());
        if (!released) {
            r0.primarySampleQueue.reset();
            for (SampleQueue embeddedSampleQueue : r0.embeddedSampleQueues) {
                embeddedSampleQueue.reset();
            }
            r0.callback.onContinueLoadingRequested(r0);
        }
    }

    public int onLoadError(Chunk loadable, long elapsedRealtimeMs, long loadDurationMs, IOException error) {
        boolean z;
        boolean cancelable;
        boolean canceled;
        Chunk chunk = loadable;
        long bytesLoaded = loadable.bytesLoaded();
        boolean isMediaChunk = isMediaChunk(loadable);
        boolean z2 = true;
        int lastChunkIndex = this.mediaChunks.size() - 1;
        if (bytesLoaded != 0 && isMediaChunk) {
            if (haveReadFromMediaChunk(lastChunkIndex)) {
                z = false;
                cancelable = z;
                z = false;
                if (r0.chunkSource.onChunkLoadError(chunk, cancelable, error)) {
                    if (cancelable) {
                        Log.w(TAG, "Ignoring attempt to cancel non-cancelable load.");
                    } else {
                        z = true;
                        if (isMediaChunk) {
                            if (discardUpstreamMediaChunksFromIndex(lastChunkIndex) == chunk) {
                                z2 = false;
                            }
                            Assertions.checkState(z2);
                            if (r0.mediaChunks.isEmpty()) {
                                r0.pendingResetPositionUs = r0.lastSeekPositionUs;
                            }
                        }
                    }
                }
                canceled = z;
                r0.eventDispatcher.loadError(chunk.dataSpec, chunk.type, r0.primaryTrackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, elapsedRealtimeMs, loadDurationMs, bytesLoaded, error, canceled);
                if (canceled) {
                    return 0;
                }
                r0.callback.onContinueLoadingRequested(r0);
                return 2;
            }
        }
        z = true;
        cancelable = z;
        z = false;
        if (r0.chunkSource.onChunkLoadError(chunk, cancelable, error)) {
            if (cancelable) {
                z = true;
                if (isMediaChunk) {
                    if (discardUpstreamMediaChunksFromIndex(lastChunkIndex) == chunk) {
                        z2 = false;
                    }
                    Assertions.checkState(z2);
                    if (r0.mediaChunks.isEmpty()) {
                        r0.pendingResetPositionUs = r0.lastSeekPositionUs;
                    }
                }
            } else {
                Log.w(TAG, "Ignoring attempt to cancel non-cancelable load.");
            }
        }
        canceled = z;
        r0.eventDispatcher.loadError(chunk.dataSpec, chunk.type, r0.primaryTrackType, chunk.trackFormat, chunk.trackSelectionReason, chunk.trackSelectionData, chunk.startTimeUs, chunk.endTimeUs, elapsedRealtimeMs, loadDurationMs, bytesLoaded, error, canceled);
        if (canceled) {
            return 0;
        }
        r0.callback.onContinueLoadingRequested(r0);
        return 2;
    }

    public boolean continueLoading(long positionUs) {
        boolean resetToMediaChunk = false;
        if (!this.loadingFinished) {
            if (!r0.loader.isLoading()) {
                MediaChunk previousChunk;
                long j;
                boolean pendingReset = isPendingReset();
                if (pendingReset) {
                    previousChunk = null;
                    j = r0.pendingResetPositionUs;
                } else {
                    previousChunk = getLastMediaChunk();
                    j = previousChunk.endTimeUs;
                }
                r0.chunkSource.getNextChunk(previousChunk, positionUs, j, r0.nextChunkHolder);
                boolean endOfStream = r0.nextChunkHolder.endOfStream;
                Chunk loadable = r0.nextChunkHolder.chunk;
                r0.nextChunkHolder.clear();
                if (endOfStream) {
                    r0.pendingResetPositionUs = C.TIME_UNSET;
                    r0.loadingFinished = true;
                    return true;
                } else if (loadable == null) {
                    return false;
                } else {
                    if (isMediaChunk(loadable)) {
                        BaseMediaChunk mediaChunk = (BaseMediaChunk) loadable;
                        if (pendingReset) {
                            if (mediaChunk.startTimeUs == r0.pendingResetPositionUs) {
                                resetToMediaChunk = true;
                            }
                            r0.decodeOnlyUntilPositionUs = resetToMediaChunk ? Long.MIN_VALUE : r0.pendingResetPositionUs;
                            r0.pendingResetPositionUs = C.TIME_UNSET;
                        }
                        mediaChunk.init(r0.mediaChunkOutput);
                        r0.mediaChunks.add(mediaChunk);
                    }
                    r0.eventDispatcher.loadStarted(loadable.dataSpec, loadable.type, r0.primaryTrackType, loadable.trackFormat, loadable.trackSelectionReason, loadable.trackSelectionData, loadable.startTimeUs, loadable.endTimeUs, r0.loader.startLoading(loadable, r0, r0.minLoadableRetryCount));
                    return true;
                }
            }
        }
        return false;
    }

    public long getNextLoadPositionUs() {
        if (isPendingReset()) {
            return this.pendingResetPositionUs;
        }
        return this.loadingFinished ? Long.MIN_VALUE : getLastMediaChunk().endTimeUs;
    }

    public void reevaluateBuffer(long positionUs) {
        if (!this.loader.isLoading()) {
            if (!isPendingReset()) {
                int currentQueueSize = this.mediaChunks.size();
                int preferredQueueSize = this.chunkSource.getPreferredQueueSize(positionUs, this.readOnlyMediaChunks);
                if (currentQueueSize > preferredQueueSize) {
                    int newQueueSize = currentQueueSize;
                    for (int i = preferredQueueSize; i < currentQueueSize; i++) {
                        if (!haveReadFromMediaChunk(i)) {
                            newQueueSize = i;
                            break;
                        }
                    }
                    if (newQueueSize != currentQueueSize) {
                        long endTimeUs = getLastMediaChunk().endTimeUs;
                        BaseMediaChunk firstRemovedChunk = discardUpstreamMediaChunksFromIndex(newQueueSize);
                        if (this.mediaChunks.isEmpty()) {
                            this.pendingResetPositionUs = this.lastSeekPositionUs;
                        }
                        this.loadingFinished = false;
                        this.eventDispatcher.upstreamDiscarded(this.primaryTrackType, firstRemovedChunk.startTimeUs, endTimeUs);
                    }
                }
            }
        }
    }

    private boolean isMediaChunk(Chunk chunk) {
        return chunk instanceof BaseMediaChunk;
    }

    private boolean haveReadFromMediaChunk(int mediaChunkIndex) {
        BaseMediaChunk mediaChunk = (BaseMediaChunk) this.mediaChunks.get(mediaChunkIndex);
        if (this.primarySampleQueue.getReadIndex() > mediaChunk.getFirstSampleIndex(0)) {
            return true;
        }
        for (int i = 0; i < this.embeddedSampleQueues.length; i++) {
            if (this.embeddedSampleQueues[i].getReadIndex() > mediaChunk.getFirstSampleIndex(i + 1)) {
                return true;
            }
        }
        return false;
    }

    boolean isPendingReset() {
        return this.pendingResetPositionUs != C.TIME_UNSET;
    }

    private void discardDownstreamMediaChunks(int discardToPrimaryStreamIndex) {
        int discardToMediaChunkIndex = primaryStreamIndexToMediaChunkIndex(discardToPrimaryStreamIndex, 0);
        if (discardToMediaChunkIndex > 0) {
            Util.removeRange(this.mediaChunks, 0, discardToMediaChunkIndex);
        }
    }

    private void maybeNotifyPrimaryTrackFormatChanged(int toPrimaryStreamReadIndex, int readCount) {
        int fromMediaChunkIndex = primaryStreamIndexToMediaChunkIndex(toPrimaryStreamReadIndex - readCount, 0);
        int toMediaChunkIndexInclusive = readCount == 1 ? fromMediaChunkIndex : primaryStreamIndexToMediaChunkIndex(toPrimaryStreamReadIndex - 1, fromMediaChunkIndex);
        for (int i = fromMediaChunkIndex; i <= toMediaChunkIndexInclusive; i++) {
            maybeNotifyPrimaryTrackFormatChanged(i);
        }
    }

    private void maybeNotifyPrimaryTrackFormatChanged(int mediaChunkReadIndex) {
        BaseMediaChunk currentChunk = (BaseMediaChunk) this.mediaChunks.get(mediaChunkReadIndex);
        Format trackFormat = currentChunk.trackFormat;
        if (!trackFormat.equals(this.primaryDownstreamTrackFormat)) {
            this.eventDispatcher.downstreamFormatChanged(this.primaryTrackType, trackFormat, currentChunk.trackSelectionReason, currentChunk.trackSelectionData, currentChunk.startTimeUs);
        }
        this.primaryDownstreamTrackFormat = trackFormat;
    }

    private int primaryStreamIndexToMediaChunkIndex(int primaryStreamIndex, int minChunkIndex) {
        for (int i = minChunkIndex + 1; i < this.mediaChunks.size(); i++) {
            if (((BaseMediaChunk) this.mediaChunks.get(i)).getFirstSampleIndex(0) > primaryStreamIndex) {
                return i - 1;
            }
        }
        return this.mediaChunks.size() - 1;
    }

    private BaseMediaChunk getLastMediaChunk() {
        return (BaseMediaChunk) this.mediaChunks.get(this.mediaChunks.size() - 1);
    }

    private BaseMediaChunk discardUpstreamMediaChunksFromIndex(int chunkIndex) {
        BaseMediaChunk firstRemovedChunk = (BaseMediaChunk) this.mediaChunks.get(chunkIndex);
        Util.removeRange(this.mediaChunks, chunkIndex, this.mediaChunks.size());
        int i = 0;
        this.primarySampleQueue.discardUpstreamSamples(firstRemovedChunk.getFirstSampleIndex(0));
        while (true) {
            int i2 = i;
            if (i2 >= this.embeddedSampleQueues.length) {
                return firstRemovedChunk;
            }
            this.embeddedSampleQueues[i2].discardUpstreamSamples(firstRemovedChunk.getFirstSampleIndex(i2 + 1));
            i = i2 + 1;
        }
    }
}
