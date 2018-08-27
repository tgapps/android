package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.offline.FilterableManifest;
import java.util.Collections;
import java.util.List;

public abstract class HlsPlaylist implements FilterableManifest<HlsPlaylist> {
    public final String baseUri;
    public final boolean hasIndependentSegments;
    public final List<String> tags;

    protected HlsPlaylist(String baseUri, List<String> tags, boolean hasIndependentSegments) {
        this.baseUri = baseUri;
        this.tags = Collections.unmodifiableList(tags);
        this.hasIndependentSegments = hasIndependentSegments;
    }
}
