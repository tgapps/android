package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.offline.StreamKey;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;

public final class HlsMediaPlaylist extends HlsPlaylist {
    public static final int PLAYLIST_TYPE_EVENT = 2;
    public static final int PLAYLIST_TYPE_UNKNOWN = 0;
    public static final int PLAYLIST_TYPE_VOD = 1;
    public final int discontinuitySequence;
    public final DrmInitData drmInitData;
    public final long durationUs;
    public final boolean hasDiscontinuitySequence;
    public final boolean hasEndTag;
    public final boolean hasProgramDateTime;
    public final long mediaSequence;
    public final int playlistType;
    public final List<Segment> segments;
    public final long startOffsetUs;
    public final long startTimeUs;
    public final long targetDurationUs;
    public final int version;

    @Retention(RetentionPolicy.SOURCE)
    public @interface PlaylistType {
    }

    public static final class Segment implements Comparable<Long> {
        public final long byterangeLength;
        public final long byterangeOffset;
        public final long durationUs;
        public final String encryptionIV;
        public final String fullSegmentEncryptionKeyUri;
        public final boolean hasGapTag;
        public final Segment initializationSegment;
        public final int relativeDiscontinuitySequence;
        public final long relativeStartTimeUs;
        public final String title;
        public final String url;

        public Segment(String uri, long byterangeOffset, long byterangeLength) {
            this(uri, null, TtmlNode.ANONYMOUS_REGION_ID, 0, -1, C.TIME_UNSET, null, null, byterangeOffset, byterangeLength, false);
        }

        public Segment(String url, Segment initializationSegment, String title, long durationUs, int relativeDiscontinuitySequence, long relativeStartTimeUs, String fullSegmentEncryptionKeyUri, String encryptionIV, long byterangeOffset, long byterangeLength, boolean hasGapTag) {
            this.url = url;
            this.initializationSegment = initializationSegment;
            this.title = title;
            this.durationUs = durationUs;
            this.relativeDiscontinuitySequence = relativeDiscontinuitySequence;
            this.relativeStartTimeUs = relativeStartTimeUs;
            this.fullSegmentEncryptionKeyUri = fullSegmentEncryptionKeyUri;
            this.encryptionIV = encryptionIV;
            this.byterangeOffset = byterangeOffset;
            this.byterangeLength = byterangeLength;
            this.hasGapTag = hasGapTag;
        }

        public int compareTo(Long relativeStartTimeUs) {
            if (this.relativeStartTimeUs > relativeStartTimeUs.longValue()) {
                return 1;
            }
            return this.relativeStartTimeUs < relativeStartTimeUs.longValue() ? -1 : 0;
        }
    }

    public HlsMediaPlaylist(int playlistType, String baseUri, List<String> tags, long startOffsetUs, long startTimeUs, boolean hasDiscontinuitySequence, int discontinuitySequence, long mediaSequence, int version, long targetDurationUs, boolean hasIndependentSegments, boolean hasEndTag, boolean hasProgramDateTime, DrmInitData drmInitData, List<Segment> segments) {
        super(baseUri, tags, hasIndependentSegments);
        this.playlistType = playlistType;
        this.startTimeUs = startTimeUs;
        this.hasDiscontinuitySequence = hasDiscontinuitySequence;
        this.discontinuitySequence = discontinuitySequence;
        this.mediaSequence = mediaSequence;
        this.version = version;
        this.targetDurationUs = targetDurationUs;
        this.hasEndTag = hasEndTag;
        this.hasProgramDateTime = hasProgramDateTime;
        this.drmInitData = drmInitData;
        this.segments = Collections.unmodifiableList(segments);
        if (segments.isEmpty()) {
            this.durationUs = 0;
        } else {
            Segment last = (Segment) segments.get(segments.size() - 1);
            this.durationUs = last.relativeStartTimeUs + last.durationUs;
        }
        if (startOffsetUs == C.TIME_UNSET) {
            startOffsetUs = C.TIME_UNSET;
        } else if (startOffsetUs < 0) {
            startOffsetUs += this.durationUs;
        }
        this.startOffsetUs = startOffsetUs;
    }

    public HlsMediaPlaylist copy(List<StreamKey> list) {
        return this;
    }

    public boolean isNewerThan(HlsMediaPlaylist other) {
        if (other == null || this.mediaSequence > other.mediaSequence) {
            return true;
        }
        if (this.mediaSequence < other.mediaSequence) {
            return false;
        }
        int segmentCount = this.segments.size();
        int otherSegmentCount = other.segments.size();
        if (segmentCount > otherSegmentCount || (segmentCount == otherSegmentCount && this.hasEndTag && !other.hasEndTag)) {
            return true;
        }
        return false;
    }

    public long getEndTimeUs() {
        return this.startTimeUs + this.durationUs;
    }

    public HlsMediaPlaylist copyWith(long startTimeUs, int discontinuitySequence) {
        return new HlsMediaPlaylist(this.playlistType, this.baseUri, this.tags, this.startOffsetUs, startTimeUs, true, discontinuitySequence, this.mediaSequence, this.version, this.targetDurationUs, this.hasIndependentSegments, this.hasEndTag, this.hasProgramDateTime, this.drmInitData, this.segments);
    }

    public HlsMediaPlaylist copyWithMasterPlaylistInfo(HlsMasterPlaylist masterPlaylist) {
        if (this.hasIndependentSegments || !masterPlaylist.hasIndependentSegments) {
            return this;
        }
        int i = this.playlistType;
        String str = this.baseUri;
        List list = this.tags;
        long j = this.startOffsetUs;
        long j2 = this.startTimeUs;
        boolean z = this.hasDiscontinuitySequence;
        int i2 = this.discontinuitySequence;
        long j3 = this.mediaSequence;
        int i3 = this.version;
        long j4 = this.targetDurationUs;
        boolean z2 = this.hasIndependentSegments || masterPlaylist.hasIndependentSegments;
        return new HlsMediaPlaylist(i, str, list, j, j2, z, i2, j3, i3, j4, z2, this.hasEndTag, this.hasProgramDateTime, this.drmInitData, this.segments);
    }

    public HlsMediaPlaylist copyWithEndTag() {
        return this.hasEndTag ? this : new HlsMediaPlaylist(this.playlistType, this.baseUri, this.tags, this.startOffsetUs, this.startTimeUs, this.hasDiscontinuitySequence, this.discontinuitySequence, this.mediaSequence, this.version, this.targetDurationUs, this.hasIndependentSegments, true, this.hasProgramDateTime, this.drmInitData, this.segments);
    }
}
