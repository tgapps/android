package org.telegram.messenger.exoplayer2.source.dash.offline;

import android.net.Uri;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.exoplayer2.extractor.ChunkIndex;
import org.telegram.messenger.exoplayer2.offline.DownloaderConstructorHelper;
import org.telegram.messenger.exoplayer2.offline.SegmentDownloader;
import org.telegram.messenger.exoplayer2.source.dash.DashSegmentIndex;
import org.telegram.messenger.exoplayer2.source.dash.DashUtil;
import org.telegram.messenger.exoplayer2.source.dash.DashWrappingSegmentIndex;
import org.telegram.messenger.exoplayer2.source.dash.manifest.AdaptationSet;
import org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifest;
import org.telegram.messenger.exoplayer2.source.dash.manifest.RangedUri;
import org.telegram.messenger.exoplayer2.source.dash.manifest.Representation;
import org.telegram.messenger.exoplayer2.source.dash.manifest.RepresentationKey;
import org.telegram.messenger.exoplayer2.upstream.DataSource;
import org.telegram.messenger.exoplayer2.upstream.DataSpec;

public final class DashDownloader extends SegmentDownloader<DashManifest, RepresentationKey> {
    protected java.util.List<org.telegram.messenger.exoplayer2.offline.SegmentDownloader.Segment> getSegments(org.telegram.messenger.exoplayer2.upstream.DataSource r1, org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifest r2, org.telegram.messenger.exoplayer2.source.dash.manifest.RepresentationKey[] r3, boolean r4) throws java.lang.InterruptedException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.dash.offline.DashDownloader.getSegments(org.telegram.messenger.exoplayer2.upstream.DataSource, org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifest, org.telegram.messenger.exoplayer2.source.dash.manifest.RepresentationKey[], boolean):java.util.List<org.telegram.messenger.exoplayer2.offline.SegmentDownloader$Segment>
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
        r1 = r28;
        r2 = r29;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r4 = 0;
        r5 = r2.length;
        if (r4 >= r5) goto L_0x00da;
    L_0x000d:
        r6 = r2[r4];
        r7 = r26;
        r8 = r27;
        r9 = r7.getSegmentIndex(r8, r1, r6);	 Catch:{ IOException -> 0x00c6 }
        if (r9 != 0) goto L_0x0038;
    L_0x0019:
        r10 = new org.telegram.messenger.exoplayer2.offline.DownloadException;	 Catch:{ IOException -> 0x0030 }
        r11 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0030 }
        r11.<init>();	 Catch:{ IOException -> 0x0030 }
        r12 = "No index for representation: ";	 Catch:{ IOException -> 0x0030 }
        r11.append(r12);	 Catch:{ IOException -> 0x0030 }
        r11.append(r6);	 Catch:{ IOException -> 0x0030 }
        r11 = r11.toString();	 Catch:{ IOException -> 0x0030 }
        r10.<init>(r11);	 Catch:{ IOException -> 0x0030 }
        throw r10;	 Catch:{ IOException -> 0x0030 }
    L_0x0030:
        r0 = move-exception;
        r1 = r0;
        r24 = r5;
        r23 = r6;
        goto L_0x00cc;
        r10 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r10 = r9.getSegmentCount(r10);
        r11 = -1;
        if (r10 != r11) goto L_0x005d;
        r4 = new org.telegram.messenger.exoplayer2.offline.DownloadException;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r11 = "Unbounded index for representation: ";
        r5.append(r11);
        r5.append(r6);
        r5 = r5.toString();
        r4.<init>(r5);
        throw r4;
        r11 = r6.periodIndex;
        r11 = r1.getPeriod(r11);
        r12 = r11.adaptationSets;
        r13 = r6.adaptationSetIndex;
        r12 = r12.get(r13);
        r12 = (org.telegram.messenger.exoplayer2.source.dash.manifest.AdaptationSet) r12;
        r12 = r12.representations;
        r13 = r6.representationIndex;
        r12 = r12.get(r13);
        r12 = (org.telegram.messenger.exoplayer2.source.dash.manifest.Representation) r12;
        r13 = r11.startMs;
        r13 = org.telegram.messenger.exoplayer2.C.msToUs(r13);
        r15 = r12.baseUrl;
        r1 = r12.getInitializationUri();
        if (r1 == 0) goto L_0x0088;
        addSegment(r3, r13, r15, r1);
        r16 = r1;
        r1 = r12.getIndexUri();
        if (r1 == 0) goto L_0x0093;
        addSegment(r3, r13, r15, r1);
        r17 = r9.getFirstSegmentNum();
        r18 = r17 + r10;
        r19 = r1;
        r1 = r18 + -1;
        r18 = r17;
        r20 = r18;
        r2 = r20;
        if (r2 > r1) goto L_0x00c3;
        r21 = r9.getTimeUs(r2);
        r24 = r5;
        r23 = r6;
        r5 = r13 + r21;
        r25 = r1;
        r1 = r9.getSegmentUrl(r2);
        addSegment(r3, r5, r15, r1);
        r18 = r2 + 1;
        r6 = r23;
        r5 = r24;
        r1 = r25;
        r2 = r29;
        goto L_0x009f;
        r24 = r5;
        goto L_0x00cf;
    L_0x00c6:
        r0 = move-exception;
        r24 = r5;
        r23 = r6;
        r1 = r0;
        if (r30 == 0) goto L_0x00d9;
        r4 = r4 + 1;
        r5 = r24;
        r1 = r28;
        r2 = r29;
        goto L_0x000b;
        throw r1;
    L_0x00da:
        r7 = r26;
        r8 = r27;
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.dash.offline.DashDownloader.getSegments(org.telegram.messenger.exoplayer2.upstream.DataSource, org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifest, org.telegram.messenger.exoplayer2.source.dash.manifest.RepresentationKey[], boolean):java.util.List<org.telegram.messenger.exoplayer2.offline.SegmentDownloader$Segment>");
    }

    public DashDownloader(Uri manifestUri, DownloaderConstructorHelper constructorHelper) {
        super(manifestUri, constructorHelper);
    }

    public DashManifest getManifest(DataSource dataSource, Uri uri) throws IOException {
        return DashUtil.loadManifest(dataSource, uri);
    }

    protected List<Segment> getAllSegments(DataSource dataSource, DashManifest manifest, boolean allowIndexLoadErrors) throws InterruptedException, IOException {
        ArrayList<Segment> segments = new ArrayList();
        for (int periodIndex = 0; periodIndex < manifest.getPeriodCount(); periodIndex++) {
            List<AdaptationSet> adaptationSets = manifest.getPeriod(periodIndex).adaptationSets;
            for (int adaptationIndex = 0; adaptationIndex < adaptationSets.size(); adaptationIndex++) {
                RepresentationKey[] keys = new RepresentationKey[((AdaptationSet) adaptationSets.get(adaptationIndex)).representations.size()];
                for (int i = 0; i < keys.length; i++) {
                    keys[i] = new RepresentationKey(periodIndex, adaptationIndex, i);
                }
                segments.addAll(getSegments(dataSource, manifest, keys, allowIndexLoadErrors));
            }
        }
        return segments;
    }

    private DashSegmentIndex getSegmentIndex(DataSource dataSource, DashManifest manifest, RepresentationKey key) throws IOException, InterruptedException {
        AdaptationSet adaptationSet = (AdaptationSet) manifest.getPeriod(key.periodIndex).adaptationSets.get(key.adaptationSetIndex);
        Representation representation = (Representation) adaptationSet.representations.get(key.representationIndex);
        DashSegmentIndex index = representation.getIndex();
        if (index != null) {
            return index;
        }
        ChunkIndex seekMap = DashUtil.loadChunkIndex(dataSource, adaptationSet.type, representation);
        return seekMap == null ? null : new DashWrappingSegmentIndex(seekMap);
    }

    private static void addSegment(ArrayList<Segment> segments, long startTimeUs, String baseUrl, RangedUri rangedUri) {
        segments.add(new Segment(startTimeUs, new DataSpec(rangedUri.resolveUri(baseUrl), rangedUri.start, rangedUri.length, null)));
    }
}
