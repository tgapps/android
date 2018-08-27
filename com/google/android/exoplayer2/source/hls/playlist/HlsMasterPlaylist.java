package com.google.android.exoplayer2.source.hls.playlist;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.util.MimeTypes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class HlsMasterPlaylist extends HlsPlaylist {
    public static final int GROUP_INDEX_AUDIO = 1;
    public static final int GROUP_INDEX_SUBTITLE = 2;
    public static final int GROUP_INDEX_VARIANT = 0;
    public final List<HlsUrl> audios;
    public final Format muxedAudioFormat;
    public final List<Format> muxedCaptionFormats;
    public final List<HlsUrl> subtitles;
    public final List<HlsUrl> variants;

    public static final class HlsUrl {
        public final Format format;
        public final String url;

        public static HlsUrl createMediaPlaylistHlsUrl(String url) {
            return new HlsUrl(url, Format.createContainerFormat("0", null, MimeTypes.APPLICATION_M3U8, null, null, -1, 0, null));
        }

        public HlsUrl(String url, Format format) {
            this.url = url;
            this.format = format;
        }
    }

    public HlsMasterPlaylist(String baseUri, List<String> tags, List<HlsUrl> variants, List<HlsUrl> audios, List<HlsUrl> subtitles, Format muxedAudioFormat, List<Format> muxedCaptionFormats, boolean hasIndependentSegments) {
        super(baseUri, tags, hasIndependentSegments);
        this.variants = Collections.unmodifiableList(variants);
        this.audios = Collections.unmodifiableList(audios);
        this.subtitles = Collections.unmodifiableList(subtitles);
        this.muxedAudioFormat = muxedAudioFormat;
        this.muxedCaptionFormats = muxedCaptionFormats != null ? Collections.unmodifiableList(muxedCaptionFormats) : null;
    }

    public HlsMasterPlaylist copy(List<StreamKey> streamKeys) {
        return new HlsMasterPlaylist(this.baseUri, this.tags, copyRenditionsList(this.variants, 0, streamKeys), copyRenditionsList(this.audios, 1, streamKeys), copyRenditionsList(this.subtitles, 2, streamKeys), this.muxedAudioFormat, this.muxedCaptionFormats, this.hasIndependentSegments);
    }

    public static HlsMasterPlaylist createSingleVariantMasterPlaylist(String variantUrl) {
        List<HlsUrl> variant = Collections.singletonList(HlsUrl.createMediaPlaylistHlsUrl(variantUrl));
        List<HlsUrl> emptyList = Collections.emptyList();
        return new HlsMasterPlaylist(null, Collections.emptyList(), variant, emptyList, emptyList, null, null, false);
    }

    private static List<HlsUrl> copyRenditionsList(List<HlsUrl> renditions, int groupIndex, List<StreamKey> streamKeys) {
        List<HlsUrl> copiedRenditions = new ArrayList(streamKeys.size());
        int i = 0;
        while (i < renditions.size()) {
            HlsUrl rendition = (HlsUrl) renditions.get(i);
            for (int j = 0; j < streamKeys.size(); j++) {
                StreamKey streamKey = (StreamKey) streamKeys.get(j);
                if (streamKey.groupIndex == groupIndex && streamKey.trackIndex == i) {
                    copiedRenditions.add(rendition);
                    break;
                }
            }
            i++;
        }
        return copiedRenditions;
    }
}
