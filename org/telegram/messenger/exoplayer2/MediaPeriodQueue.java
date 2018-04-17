package org.telegram.messenger.exoplayer2;

import org.telegram.messenger.exoplayer2.Timeline.Period;
import org.telegram.messenger.exoplayer2.Timeline.Window;
import org.telegram.messenger.exoplayer2.source.MediaPeriod;
import org.telegram.messenger.exoplayer2.source.MediaSource;
import org.telegram.messenger.exoplayer2.source.MediaSource.MediaPeriodId;
import org.telegram.messenger.exoplayer2.trackselection.TrackSelector;
import org.telegram.messenger.exoplayer2.trackselection.TrackSelectorResult;
import org.telegram.messenger.exoplayer2.upstream.Allocator;
import org.telegram.messenger.exoplayer2.util.Assertions;

final class MediaPeriodQueue {
    private static final int MAXIMUM_BUFFER_AHEAD_PERIODS = 100;
    private int length;
    private MediaPeriodHolder loading;
    private final Period period = new Period();
    private MediaPeriodHolder playing;
    private MediaPeriodHolder reading;
    private int repeatMode;
    private boolean shuffleModeEnabled;
    private Timeline timeline;
    private final Window window = new Window();

    private org.telegram.messenger.exoplayer2.MediaPeriodInfo getFollowingMediaPeriodInfo(org.telegram.messenger.exoplayer2.MediaPeriodInfo r1, long r2, long r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.MediaPeriodQueue.getFollowingMediaPeriodInfo(org.telegram.messenger.exoplayer2.MediaPeriodInfo, long, long):org.telegram.messenger.exoplayer2.MediaPeriodInfo
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
        r6 = r20;
        r7 = r21;
        r0 = r7.isLastInTimelinePeriod;
        r1 = -1;
        r2 = 0;
        if (r0 == 0) goto L_0x007b;
    L_0x000a:
        r8 = r6.timeline;
        r0 = r7.id;
        r9 = r0.periodIndex;
        r10 = r6.period;
        r11 = r6.window;
        r12 = r6.repeatMode;
        r13 = r6.shuffleModeEnabled;
        r0 = r8.getNextPeriodIndex(r9, r10, r11, r12, r13);
        if (r0 != r1) goto L_0x001f;
    L_0x001e:
        return r2;
    L_0x001f:
        r1 = r6.timeline;
        r3 = r6.period;
        r1 = r1.getPeriod(r0, r3);
        r4 = r1.windowIndex;
        r1 = r6.timeline;
        r3 = r6.window;
        r1 = r1.getWindow(r4, r3);
        r1 = r1.firstPeriodIndex;
        r8 = 0;
        if (r1 != r0) goto L_0x006b;
    L_0x0037:
        r10 = r7.durationUs;
        r12 = r22 + r10;
        r14 = r12 - r24;
        r1 = r6.timeline;
        r3 = r6.window;
        r10 = r6.period;
        r12 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r16 = java.lang.Math.max(r8, r14);
        r8 = r1;
        r9 = r3;
        r11 = r4;
        r18 = r14;
        r14 = r16;
        r1 = r8.getPeriodPosition(r9, r10, r11, r12, r14);
        if (r1 != 0) goto L_0x005a;
        return r2;
        r2 = r1.first;
        r2 = (java.lang.Integer) r2;
        r0 = r2.intValue();
        r2 = r1.second;
        r2 = (java.lang.Long) r2;
        r8 = r2.longValue();
        goto L_0x006c;
        r10 = r0;
        r11 = r6.resolveMediaPeriodIdForAds(r10, r8);
        r0 = r6;
        r1 = r11;
        r2 = r8;
        r12 = r4;
        r4 = r8;
        r0 = r0.getMediaPeriodInfo(r1, r2, r4);
        return r0;
    L_0x007b:
        r8 = r7.id;
        r0 = r8.isAd();
        r3 = -9223372036854775808;
        if (r0 == 0) goto L_0x00d1;
        r9 = r8.adGroupIndex;
        r0 = r6.timeline;
        r5 = r8.periodIndex;
        r10 = r6.period;
        r0.getPeriod(r5, r10);
        r0 = r6.period;
        r10 = r0.getAdCountInAdGroup(r9);
        if (r10 != r1) goto L_0x0099;
        return r2;
        r0 = r8.adIndexInAdGroup;
        r11 = r0 + 1;
        if (r11 >= r10) goto L_0x00b4;
        r0 = r6.period;
        r0 = r0.isAdAvailable(r9, r11);
        if (r0 != 0) goto L_0x00a8;
        goto L_0x00b3;
        r1 = r8.periodIndex;
        r4 = r7.contentPositionUs;
        r0 = r6;
        r2 = r9;
        r3 = r11;
        r2 = r0.getMediaPeriodInfoForAd(r1, r2, r3, r4);
        return r2;
        r0 = r6.period;
        r12 = r7.contentPositionUs;
        r12 = r0.getAdGroupIndexAfterPositionUs(r12);
        if (r12 != r1) goto L_0x00c0;
        r4 = r3;
        goto L_0x00c7;
        r0 = r6.period;
        r0 = r0.getAdGroupTimeUs(r12);
        r4 = r0;
        r1 = r8.periodIndex;
        r2 = r7.contentPositionUs;
        r0 = r6;
        r0 = r0.getMediaPeriodInfoForContent(r1, r2, r4);
        return r0;
        r0 = r7.endPositionUs;
        r5 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1));
        r0 = 0;
        if (r5 == 0) goto L_0x00f5;
        r1 = r6.period;
        r3 = r7.endPositionUs;
        r9 = r1.getAdGroupIndexForPositionUs(r3);
        r1 = r6.period;
        r0 = r1.isAdAvailable(r9, r0);
        if (r0 != 0) goto L_0x00e9;
        goto L_0x00f4;
        r1 = r8.periodIndex;
        r3 = 0;
        r4 = r7.endPositionUs;
        r0 = r6;
        r2 = r9;
        r2 = r0.getMediaPeriodInfoForAd(r1, r2, r3, r4);
        return r2;
        r1 = r6.period;
        r9 = r1.getAdGroupCount();
        if (r9 == 0) goto L_0x0130;
        r1 = r6.period;
        r5 = r9 + -1;
        r10 = r1.getAdGroupTimeUs(r5);
        r1 = (r10 > r3 ? 1 : (r10 == r3 ? 0 : -1));
        if (r1 != 0) goto L_0x0130;
        r1 = r6.period;
        r3 = r9 + -1;
        r1 = r1.hasPlayedAdGroup(r3);
        if (r1 != 0) goto L_0x0130;
        r1 = r6.period;
        r3 = r9 + -1;
        r0 = r1.isAdAvailable(r3, r0);
        if (r0 != 0) goto L_0x011e;
        goto L_0x0130;
        r0 = r6.period;
        r10 = r0.getDurationUs();
        r1 = r8.periodIndex;
        r2 = r9 + -1;
        r3 = 0;
        r0 = r6;
        r4 = r10;
        r0 = r0.getMediaPeriodInfoForAd(r1, r2, r3, r4);
        return r0;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.MediaPeriodQueue.getFollowingMediaPeriodInfo(org.telegram.messenger.exoplayer2.MediaPeriodInfo, long, long):org.telegram.messenger.exoplayer2.MediaPeriodInfo");
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public void setRepeatMode(int repeatMode) {
        this.repeatMode = repeatMode;
    }

    public void setShuffleModeEnabled(boolean shuffleModeEnabled) {
        this.shuffleModeEnabled = shuffleModeEnabled;
    }

    public boolean isLoading(MediaPeriod mediaPeriod) {
        return this.loading != null && this.loading.mediaPeriod == mediaPeriod;
    }

    public void reevaluateBuffer(long rendererPositionUs) {
        if (this.loading != null) {
            this.loading.reevaluateBuffer(rendererPositionUs);
        }
    }

    public boolean shouldLoadNextMediaPeriod() {
        if (this.loading != null) {
            if (this.loading.info.isFinal || !this.loading.isFullyBuffered() || this.loading.info.durationUs == C.TIME_UNSET || this.length >= MAXIMUM_BUFFER_AHEAD_PERIODS) {
                return false;
            }
        }
        return true;
    }

    public MediaPeriodInfo getNextMediaPeriodInfo(long rendererPositionUs, PlaybackInfo playbackInfo) {
        if (this.loading == null) {
            return getFirstMediaPeriodInfo(playbackInfo);
        }
        return getFollowingMediaPeriodInfo(this.loading.info, this.loading.getRendererOffset(), rendererPositionUs);
    }

    public MediaPeriod enqueueNextMediaPeriod(RendererCapabilities[] rendererCapabilities, long rendererTimestampOffsetUs, TrackSelector trackSelector, Allocator allocator, MediaSource mediaSource, Object uid, MediaPeriodInfo info) {
        MediaPeriodInfo mediaPeriodInfo;
        long rendererPositionOffsetUs;
        if (this.loading == null) {
            mediaPeriodInfo = info;
            rendererPositionOffsetUs = mediaPeriodInfo.startPositionUs + rendererTimestampOffsetUs;
        } else {
            mediaPeriodInfo = info;
            rendererPositionOffsetUs = r0.loading.getRendererOffset() + r0.loading.info.durationUs;
        }
        MediaPeriodHolder newPeriodHolder = new MediaPeriodHolder(rendererCapabilities, rendererPositionOffsetUs, trackSelector, allocator, mediaSource, uid, mediaPeriodInfo);
        if (r0.loading != null) {
            Assertions.checkState(hasPlayingPeriod());
            r0.loading.next = newPeriodHolder;
        }
        r0.loading = newPeriodHolder;
        r0.length++;
        return newPeriodHolder.mediaPeriod;
    }

    public TrackSelectorResult handleLoadingPeriodPrepared(float playbackSpeed) throws ExoPlaybackException {
        return this.loading.handlePrepared(playbackSpeed);
    }

    public MediaPeriodHolder getLoadingPeriod() {
        return this.loading;
    }

    public MediaPeriodHolder getPlayingPeriod() {
        return this.playing;
    }

    public MediaPeriodHolder getReadingPeriod() {
        return this.reading;
    }

    public MediaPeriodHolder getFrontPeriod() {
        return hasPlayingPeriod() ? this.playing : this.loading;
    }

    public boolean hasPlayingPeriod() {
        return this.playing != null;
    }

    public MediaPeriodHolder advanceReadingPeriod() {
        boolean z = (this.reading == null || this.reading.next == null) ? false : true;
        Assertions.checkState(z);
        this.reading = this.reading.next;
        return this.reading;
    }

    public MediaPeriodHolder advancePlayingPeriod() {
        if (this.playing != null) {
            if (this.playing == this.reading) {
                this.reading = this.playing.next;
            }
            this.playing.release();
            this.playing = this.playing.next;
            this.length--;
            if (this.length == 0) {
                this.loading = null;
            }
        } else {
            this.playing = this.loading;
            this.reading = this.loading;
        }
        return this.playing;
    }

    public boolean removeAfter(MediaPeriodHolder mediaPeriodHolder) {
        Assertions.checkState(mediaPeriodHolder != null);
        boolean removedReading = false;
        this.loading = mediaPeriodHolder;
        while (mediaPeriodHolder.next != null) {
            mediaPeriodHolder = mediaPeriodHolder.next;
            if (mediaPeriodHolder == this.reading) {
                this.reading = this.playing;
                removedReading = true;
            }
            mediaPeriodHolder.release();
            this.length--;
        }
        this.loading.next = null;
        return removedReading;
    }

    public void clear() {
        MediaPeriodHolder front = getFrontPeriod();
        if (front != null) {
            front.release();
            removeAfter(front);
        }
        this.playing = null;
        this.loading = null;
        this.reading = null;
        this.length = 0;
    }

    public MediaPeriodInfo getUpdatedMediaPeriodInfo(MediaPeriodInfo mediaPeriodInfo) {
        return getUpdatedMediaPeriodInfo(mediaPeriodInfo, mediaPeriodInfo.id);
    }

    public MediaPeriodInfo getUpdatedMediaPeriodInfo(MediaPeriodInfo mediaPeriodInfo, int newPeriodIndex) {
        return getUpdatedMediaPeriodInfo(mediaPeriodInfo, mediaPeriodInfo.id.copyWithPeriodIndex(newPeriodIndex));
    }

    public MediaPeriodId resolveMediaPeriodIdForAds(int periodIndex, long positionUs) {
        this.timeline.getPeriod(periodIndex, this.period);
        int adGroupIndex = this.period.getAdGroupIndexForPositionUs(positionUs);
        if (adGroupIndex == -1) {
            return new MediaPeriodId(periodIndex);
        }
        return new MediaPeriodId(periodIndex, adGroupIndex, this.period.getNextAdIndexToPlay(adGroupIndex));
    }

    private MediaPeriodInfo getFirstMediaPeriodInfo(PlaybackInfo playbackInfo) {
        return getMediaPeriodInfo(playbackInfo.periodId, playbackInfo.contentPositionUs, playbackInfo.startPositionUs);
    }

    private MediaPeriodInfo getUpdatedMediaPeriodInfo(MediaPeriodInfo info, MediaPeriodId newId) {
        long adDurationUs;
        long durationUs;
        MediaPeriodInfo mediaPeriodInfo = info;
        MediaPeriodId mediaPeriodId = newId;
        long startPositionUs = mediaPeriodInfo.startPositionUs;
        long endPositionUs = mediaPeriodInfo.endPositionUs;
        boolean isLastInPeriod = isLastInPeriod(mediaPeriodId, endPositionUs);
        boolean isLastInTimeline = isLastInTimeline(mediaPeriodId, isLastInPeriod);
        this.timeline.getPeriod(mediaPeriodId.periodIndex, this.period);
        if (newId.isAd()) {
            adDurationUs = r0.period.getAdDurationUs(mediaPeriodId.adGroupIndex, mediaPeriodId.adIndexInAdGroup);
        } else if (endPositionUs == Long.MIN_VALUE) {
            adDurationUs = r0.period.getDurationUs();
        } else {
            durationUs = endPositionUs;
            return new MediaPeriodInfo(mediaPeriodId, startPositionUs, endPositionUs, mediaPeriodInfo.contentPositionUs, durationUs, isLastInPeriod, isLastInTimeline);
        }
        durationUs = adDurationUs;
        return new MediaPeriodInfo(mediaPeriodId, startPositionUs, endPositionUs, mediaPeriodInfo.contentPositionUs, durationUs, isLastInPeriod, isLastInTimeline);
    }

    private MediaPeriodInfo getMediaPeriodInfo(MediaPeriodId id, long contentPositionUs, long startPositionUs) {
        this.timeline.getPeriod(id.periodIndex, this.period);
        if (!id.isAd()) {
            long j;
            int nextAdGroupIndex = this.period.getAdGroupIndexAfterPositionUs(startPositionUs);
            if (nextAdGroupIndex == -1) {
                j = Long.MIN_VALUE;
            } else {
                j = this.period.getAdGroupTimeUs(nextAdGroupIndex);
            }
            return getMediaPeriodInfoForContent(id.periodIndex, startPositionUs, j);
        } else if (this.period.isAdAvailable(id.adGroupIndex, id.adIndexInAdGroup)) {
            return getMediaPeriodInfoForAd(id.periodIndex, id.adGroupIndex, id.adIndexInAdGroup, contentPositionUs);
        } else {
            return null;
        }
    }

    private MediaPeriodInfo getMediaPeriodInfoForAd(int periodIndex, int adGroupIndex, int adIndexInAdGroup, long contentPositionUs) {
        int i = adGroupIndex;
        int i2 = adIndexInAdGroup;
        MediaPeriodId id = new MediaPeriodId(periodIndex, i, i2);
        boolean isLastInPeriod = isLastInPeriod(id, Long.MIN_VALUE);
        boolean isLastInTimeline = isLastInTimeline(id, isLastInPeriod);
        boolean isLastInPeriod2 = isLastInPeriod;
        return new MediaPeriodInfo(id, i2 == this.period.getNextAdIndexToPlay(i) ? r0.period.getAdResumePositionUs() : 0, Long.MIN_VALUE, contentPositionUs, this.timeline.getPeriod(id.periodIndex, this.period).getAdDurationUs(id.adGroupIndex, id.adIndexInAdGroup), isLastInPeriod, isLastInTimeline);
    }

    private MediaPeriodInfo getMediaPeriodInfoForContent(int periodIndex, long startPositionUs, long endUs) {
        long j = endUs;
        MediaPeriodId id = new MediaPeriodId(periodIndex);
        boolean isLastInPeriod = isLastInPeriod(id, j);
        boolean isLastInTimeline = isLastInTimeline(id, isLastInPeriod);
        this.timeline.getPeriod(id.periodIndex, this.period);
        boolean isLastInPeriod2 = isLastInPeriod;
        return new MediaPeriodInfo(id, startPositionUs, j, C.TIME_UNSET, j == Long.MIN_VALUE ? r0.period.getDurationUs() : j, isLastInPeriod, isLastInTimeline);
    }

    private boolean isLastInPeriod(MediaPeriodId id, long endPositionUs) {
        int adGroupCount = this.timeline.getPeriod(id.periodIndex, this.period).getAdGroupCount();
        boolean z = true;
        if (adGroupCount == 0) {
            return true;
        }
        int lastAdGroupIndex = adGroupCount - 1;
        boolean isAd = id.isAd();
        if (this.period.getAdGroupTimeUs(lastAdGroupIndex) != Long.MIN_VALUE) {
            if (isAd || endPositionUs != Long.MIN_VALUE) {
                z = false;
            }
            return z;
        }
        int postrollAdCount = this.period.getAdCountInAdGroup(lastAdGroupIndex);
        if (postrollAdCount == -1) {
            return false;
        }
        boolean isLastAd = isAd && id.adGroupIndex == lastAdGroupIndex && id.adIndexInAdGroup == postrollAdCount - 1;
        if (!isLastAd) {
            if (isAd || this.period.getNextAdIndexToPlay(lastAdGroupIndex) != postrollAdCount) {
                z = false;
            }
        }
        return z;
    }

    private boolean isLastInTimeline(MediaPeriodId id, boolean isLastMediaPeriodInPeriod) {
        return !this.timeline.getWindow(this.timeline.getPeriod(id.periodIndex, this.period).windowIndex, this.window).isDynamic && this.timeline.isLastPeriod(id.periodIndex, this.period, this.window, this.repeatMode, this.shuffleModeEnabled) && isLastMediaPeriodInPeriod;
    }
}
