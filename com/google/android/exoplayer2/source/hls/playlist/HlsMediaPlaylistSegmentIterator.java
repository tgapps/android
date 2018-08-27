package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.source.chunk.BaseMediaChunkIterator;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.util.UriUtil;

public final class HlsMediaPlaylistSegmentIterator extends BaseMediaChunkIterator {
    private final HlsMediaPlaylist playlist;
    private final long startOfPlaylistInPeriodUs;

    public HlsMediaPlaylistSegmentIterator(HlsMediaPlaylist playlist, long startOfPlaylistInPeriodUs, int chunkIndex) {
        super((long) chunkIndex, (long) (playlist.segments.size() - 1));
        this.playlist = playlist;
        this.startOfPlaylistInPeriodUs = startOfPlaylistInPeriodUs;
    }

    public DataSpec getDataSpec() {
        checkInBounds();
        Segment segment = (Segment) this.playlist.segments.get((int) getCurrentIndex());
        return new DataSpec(UriUtil.resolveToUri(this.playlist.baseUri, segment.url), segment.byterangeOffset, segment.byterangeLength, null);
    }

    public long getChunkStartTimeUs() {
        checkInBounds();
        return this.startOfPlaylistInPeriodUs + ((Segment) this.playlist.segments.get((int) getCurrentIndex())).relativeStartTimeUs;
    }

    public long getChunkEndTimeUs() {
        checkInBounds();
        Segment segment = (Segment) this.playlist.segments.get((int) getCurrentIndex());
        return segment.durationUs + (this.startOfPlaylistInPeriodUs + segment.relativeStartTimeUs);
    }
}
