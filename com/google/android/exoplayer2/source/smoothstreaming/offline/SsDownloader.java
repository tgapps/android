package com.google.android.exoplayer2.source.smoothstreaming.offline;

import android.net.Uri;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.offline.SegmentDownloader;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifest.StreamElement;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsManifestParser;
import com.google.android.exoplayer2.source.smoothstreaming.manifest.SsUtil;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.ParsingLoadable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SsDownloader extends SegmentDownloader<SsManifest> {
    public SsDownloader(Uri manifestUri, List<StreamKey> streamKeys, DownloaderConstructorHelper constructorHelper) {
        super(SsUtil.fixManifestUri(manifestUri), streamKeys, constructorHelper);
    }

    protected SsManifest getManifest(DataSource dataSource, Uri uri) throws IOException {
        return (SsManifest) ParsingLoadable.load(dataSource, new SsManifestParser(), uri, 4);
    }

    protected List<Segment> getSegments(DataSource dataSource, SsManifest manifest, boolean allowIncompleteList) {
        ArrayList<Segment> segments = new ArrayList();
        for (StreamElement streamElement : manifest.streamElements) {
            for (int i = 0; i < streamElement.formats.length; i++) {
                for (int j = 0; j < streamElement.chunkCount; j++) {
                    segments.add(new Segment(streamElement.getStartTimeUs(j), new DataSpec(streamElement.buildRequestUri(i, j))));
                }
            }
        }
        return segments;
    }
}
