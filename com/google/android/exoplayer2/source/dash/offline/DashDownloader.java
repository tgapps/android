package com.google.android.exoplayer2.source.dash.offline;

import android.net.Uri;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.extractor.ChunkIndex;
import com.google.android.exoplayer2.offline.DownloadException;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.offline.SegmentDownloader;
import com.google.android.exoplayer2.offline.StreamKey;
import com.google.android.exoplayer2.source.dash.DashSegmentIndex;
import com.google.android.exoplayer2.source.dash.DashUtil;
import com.google.android.exoplayer2.source.dash.DashWrappingSegmentIndex;
import com.google.android.exoplayer2.source.dash.manifest.AdaptationSet;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.dash.manifest.Period;
import com.google.android.exoplayer2.source.dash.manifest.RangedUri;
import com.google.android.exoplayer2.source.dash.manifest.Representation;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class DashDownloader extends SegmentDownloader<DashManifest> {
    public DashDownloader(Uri manifestUri, List<StreamKey> streamKeys, DownloaderConstructorHelper constructorHelper) {
        super(manifestUri, streamKeys, constructorHelper);
    }

    protected DashManifest getManifest(DataSource dataSource, Uri uri) throws IOException {
        return DashUtil.loadManifest(dataSource, uri);
    }

    protected List<Segment> getSegments(DataSource dataSource, DashManifest manifest, boolean allowIncompleteList) throws InterruptedException, IOException {
        ArrayList<Segment> segments = new ArrayList();
        for (int i = 0; i < manifest.getPeriodCount(); i++) {
            Period period = manifest.getPeriod(i);
            long periodStartUs = C.msToUs(period.startMs);
            long periodDurationUs = manifest.getPeriodDurationUs(i);
            List<AdaptationSet> adaptationSets = period.adaptationSets;
            for (int j = 0; j < adaptationSets.size(); j++) {
                addSegmentsForAdaptationSet(dataSource, (AdaptationSet) adaptationSets.get(j), periodStartUs, periodDurationUs, allowIncompleteList, segments);
            }
        }
        return segments;
    }

    private static void addSegmentsForAdaptationSet(DataSource dataSource, AdaptationSet adaptationSet, long periodStartUs, long periodDurationUs, boolean allowIncompleteList, ArrayList<Segment> out) throws IOException, InterruptedException {
        int i = 0;
        while (i < adaptationSet.representations.size()) {
            Representation representation = (Representation) adaptationSet.representations.get(i);
            try {
                DashSegmentIndex index = getSegmentIndex(dataSource, adaptationSet.type, representation);
                if (index == null) {
                    throw new DownloadException("Missing segment index");
                }
                int segmentCount = index.getSegmentCount(periodDurationUs);
                if (segmentCount == -1) {
                    throw new DownloadException("Unbounded segment index");
                }
                String baseUrl = representation.baseUrl;
                RangedUri initializationUri = representation.getInitializationUri();
                if (initializationUri != null) {
                    addSegment(periodStartUs, baseUrl, initializationUri, out);
                }
                RangedUri indexUri = representation.getIndexUri();
                if (indexUri != null) {
                    addSegment(periodStartUs, baseUrl, indexUri, out);
                }
                long firstSegmentNum = index.getFirstSegmentNum();
                long lastSegmentNum = (((long) segmentCount) + firstSegmentNum) - 1;
                for (long j = firstSegmentNum; j <= lastSegmentNum; j++) {
                    addSegment(index.getTimeUs(j) + periodStartUs, baseUrl, index.getSegmentUrl(j), out);
                }
                i++;
            } catch (IOException e) {
                if (!allowIncompleteList) {
                    throw e;
                }
            }
        }
    }

    private static void addSegment(long startTimeUs, String baseUrl, RangedUri rangedUri, ArrayList<Segment> out) {
        out.add(new Segment(startTimeUs, new DataSpec(rangedUri.resolveUri(baseUrl), rangedUri.start, rangedUri.length, null)));
    }

    private static DashSegmentIndex getSegmentIndex(DataSource dataSource, int trackType, Representation representation) throws IOException, InterruptedException {
        DashSegmentIndex index = representation.getIndex();
        if (index != null) {
            return index;
        }
        ChunkIndex seekMap = DashUtil.loadChunkIndex(dataSource, trackType, representation);
        return seekMap == null ? null : new DashWrappingSegmentIndex(seekMap, representation.presentationTimeOffsetUs);
    }
}
