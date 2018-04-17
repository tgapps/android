package org.telegram.messenger.exoplayer2.source.dash.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.drm.DrmInitData;
import org.telegram.messenger.exoplayer2.drm.DrmInitData.SchemeData;
import org.telegram.messenger.exoplayer2.extractor.mp4.PsshAtomUtil;
import org.telegram.messenger.exoplayer2.metadata.emsg.EventMessage;
import org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SegmentList;
import org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SegmentTemplate;
import org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SegmentTimelineElement;
import org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SingleSegmentBase;
import org.telegram.messenger.exoplayer2.upstream.ParsingLoadable.Parser;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.exoplayer2.util.UriUtil;
import org.telegram.messenger.exoplayer2.util.Util;
import org.telegram.messenger.exoplayer2.util.XmlPullParserUtil;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

public class DashManifestParser extends DefaultHandler implements Parser<DashManifest> {
    private static final Pattern CEA_608_ACCESSIBILITY_PATTERN = Pattern.compile("CC([1-4])=.*");
    private static final Pattern CEA_708_ACCESSIBILITY_PATTERN = Pattern.compile("([1-9]|[1-5][0-9]|6[0-3])=.*");
    private static final Pattern FRAME_RATE_PATTERN = Pattern.compile("(\\d+)(?:/(\\d+))?");
    private static final String TAG = "MpdParser";
    private final String contentId;
    private final XmlPullParserFactory xmlParserFactory;

    protected static final class RepresentationInfo {
        public final String baseUrl;
        public final ArrayList<SchemeData> drmSchemeDatas;
        public final String drmSchemeType;
        public final Format format;
        public final ArrayList<Descriptor> inbandEventStreams;
        public final long revisionId;
        public final SegmentBase segmentBase;

        public RepresentationInfo(Format format, String baseUrl, SegmentBase segmentBase, String drmSchemeType, ArrayList<SchemeData> drmSchemeDatas, ArrayList<Descriptor> inbandEventStreams, long revisionId) {
            this.format = format;
            this.baseUrl = baseUrl;
            this.segmentBase = segmentBase;
            this.drmSchemeType = drmSchemeType;
            this.drmSchemeDatas = drmSchemeDatas;
            this.inbandEventStreams = inbandEventStreams;
            this.revisionId = revisionId;
        }
    }

    protected org.telegram.messenger.exoplayer2.source.dash.manifest.AdaptationSet parseAdaptationSet(org.xmlpull.v1.XmlPullParser r1, java.lang.String r2, org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase r3) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifestParser.parseAdaptationSet(org.xmlpull.v1.XmlPullParser, java.lang.String, org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase):org.telegram.messenger.exoplayer2.source.dash.manifest.AdaptationSet
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
        r14 = r37;
        r15 = r38;
        r0 = "id";
        r1 = -1;
        r16 = parseInt(r15, r0, r1);
        r0 = r37.parseContentType(r38);
        r2 = "mimeType";
        r13 = 0;
        r17 = r15.getAttributeValue(r13, r2);
        r2 = "codecs";
        r18 = r15.getAttributeValue(r13, r2);
        r2 = "width";
        r19 = parseInt(r15, r2, r1);
        r2 = "height";
        r20 = parseInt(r15, r2, r1);
        r2 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r21 = parseFrameRate(r15, r2);
        r2 = -1;
        r3 = "audioSamplingRate";
        r22 = parseInt(r15, r3, r1);
        r1 = "lang";
        r1 = r15.getAttributeValue(r13, r1);
        r3 = 0;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r12 = r4;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r11 = r4;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r10 = r4;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r9 = r4;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r8 = r4;
        r4 = 0;
        r23 = 0;
        r7 = r39;
        r27 = r40;
        r5 = r0;
        r6 = r1;
        r26 = r2;
        r24 = r3;
        r25 = r4;
        r0 = r23;
        r28 = r0;
        r38.next();
        r0 = "BaseURL";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x00a5;
    L_0x0078:
        if (r28 != 0) goto L_0x0093;
    L_0x007a:
        r0 = parseBaseUrl(r15, r7);
        r1 = 1;
        r30 = r0;
        r28 = r1;
        r29 = r6;
        r6 = r8;
        r32 = r9;
        r33 = r10;
        r8 = r11;
        r35 = r12;
        r36 = r13;
        r7 = r15;
        r9 = r5;
        goto L_0x01f0;
    L_0x0093:
        r1 = r5;
        r29 = r6;
        r30 = r7;
        r6 = r8;
        r32 = r9;
        r33 = r10;
        r8 = r11;
        r35 = r12;
        r36 = r13;
        r7 = r15;
        goto L_0x01ef;
    L_0x00a5:
        r0 = "ContentProtection";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x00ca;
    L_0x00ad:
        r0 = r37.parseContentProtection(r38);
        r1 = r0.first;
        if (r1 == 0) goto L_0x00bb;
    L_0x00b5:
        r1 = r0.first;
        r24 = r1;
        r24 = (java.lang.String) r24;
    L_0x00bb:
        r1 = r0.second;
        if (r1 == 0) goto L_0x00c4;
    L_0x00bf:
        r1 = r0.second;
        r12.add(r1);
        r29 = r6;
        r30 = r7;
        goto L_0x0085;
    L_0x00ca:
        r0 = "ContentComponent";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x00f6;
        r0 = "lang";
        r0 = r15.getAttributeValue(r13, r0);
        r6 = checkLanguageConsistency(r6, r0);
        r0 = r37.parseContentType(r38);
        r0 = checkContentTypeConsistency(r5, r0);
        r29 = r6;
        r30 = r7;
        r6 = r8;
        r32 = r9;
        r33 = r10;
        r8 = r11;
        r35 = r12;
        r36 = r13;
        r7 = r15;
        r9 = r0;
        goto L_0x01f0;
        r0 = "Role";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x0105;
        r0 = r37.parseRole(r38);
        r25 = r25 | r0;
        goto L_0x00c5;
        r0 = "AudioChannelConfiguration";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x0112;
        r26 = r37.parseAudioChannelConfiguration(r38);
        goto L_0x00c5;
        r0 = "Accessibility";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x0125;
        r0 = "Accessibility";
        r0 = parseDescriptor(r15, r0);
        r10.add(r0);
        goto L_0x0093;
        r0 = "SupplementalProperty";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x0138;
        r0 = "SupplementalProperty";
        r0 = parseDescriptor(r15, r0);
        r9.add(r0);
        goto L_0x0093;
        r0 = "Representation";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x0185;
        r0 = r14;
        r1 = r15;
        r2 = r7;
        r3 = r17;
        r4 = r18;
        r15 = r5;
        r5 = r19;
        r29 = r6;
        r6 = r20;
        r30 = r7;
        r7 = r21;
        r31 = r8;
        r8 = r26;
        r32 = r9;
        r9 = r22;
        r33 = r10;
        r10 = r29;
        r34 = r11;
        r11 = r25;
        r35 = r12;
        r12 = r33;
        r36 = r13;
        r13 = r27;
        r0 = r0.parseRepresentation(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        r1 = r0.format;
        r1 = r14.getContentType(r1);
        r1 = checkContentTypeConsistency(r15, r1);
        r6 = r31;
        r6.add(r0);
        r9 = r1;
        r8 = r34;
        r7 = r38;
        goto L_0x01f0;
        r15 = r5;
        r29 = r6;
        r30 = r7;
        r6 = r8;
        r32 = r9;
        r33 = r10;
        r34 = r11;
        r35 = r12;
        r36 = r13;
        r0 = "SegmentBase";
        r1 = r15;
        r7 = r38;
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r7, r0);
        if (r0 == 0) goto L_0x01ae;
        r0 = r27;
        r0 = (org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SingleSegmentBase) r0;
        r0 = r14.parseSegmentBase(r7, r0);
        r27 = r0;
        r9 = r1;
        r8 = r34;
        goto L_0x01f0;
        r0 = "SegmentList";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r7, r0);
        if (r0 == 0) goto L_0x01bf;
        r0 = r27;
        r0 = (org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SegmentList) r0;
        r0 = r14.parseSegmentList(r7, r0);
        goto L_0x01a8;
        r0 = "SegmentTemplate";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r7, r0);
        if (r0 == 0) goto L_0x01d0;
        r0 = r27;
        r0 = (org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SegmentTemplate) r0;
        r0 = r14.parseSegmentTemplate(r7, r0);
        goto L_0x01a8;
        r0 = "InbandEventStream";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r7, r0);
        if (r0 == 0) goto L_0x01e4;
        r0 = "InbandEventStream";
        r0 = parseDescriptor(r7, r0);
        r8 = r34;
        r8.add(r0);
        goto L_0x01ef;
        r8 = r34;
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r38);
        if (r0 == 0) goto L_0x01ef;
        r37.parseAdaptationSetChild(r38);
    L_0x01ef:
        r9 = r1;
    L_0x01f0:
        r0 = "AdaptationSet";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isEndTag(r7, r0);
        if (r0 == 0) goto L_0x0232;
        r0 = new java.util.ArrayList;
        r1 = r6.size();
        r0.<init>(r1);
        r10 = r0;
        r11 = r23;
        r0 = r6.size();
        if (r11 >= r0) goto L_0x0224;
        r0 = r6.get(r11);
        r1 = r0;
        r1 = (org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifestParser.RepresentationInfo) r1;
        r2 = r14.contentId;
        r0 = r14;
        r3 = r24;
        r4 = r35;
        r5 = r8;
        r0 = r0.buildRepresentation(r1, r2, r3, r4, r5);
        r10.add(r0);
        r23 = r11 + 1;
        goto L_0x0203;
        r0 = r14;
        r1 = r16;
        r2 = r9;
        r3 = r10;
        r4 = r33;
        r5 = r32;
        r0 = r0.buildAdaptationSet(r1, r2, r3, r4, r5);
        return r0;
        r15 = r7;
        r11 = r8;
        r5 = r9;
        r0 = r28;
        r7 = r30;
        r9 = r32;
        r10 = r33;
        r12 = r35;
        r13 = r36;
        r8 = r6;
        r6 = r29;
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifestParser.parseAdaptationSet(org.xmlpull.v1.XmlPullParser, java.lang.String, org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase):org.telegram.messenger.exoplayer2.source.dash.manifest.AdaptationSet");
    }

    protected org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifestParser.RepresentationInfo parseRepresentation(org.xmlpull.v1.XmlPullParser r1, java.lang.String r2, java.lang.String r3, java.lang.String r4, int r5, int r6, float r7, int r8, int r9, java.lang.String r10, int r11, java.util.List<org.telegram.messenger.exoplayer2.source.dash.manifest.Descriptor> r12, org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase r13) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifestParser.parseRepresentation(org.xmlpull.v1.XmlPullParser, java.lang.String, java.lang.String, java.lang.String, int, int, float, int, int, java.lang.String, int, java.util.List, org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase):org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifestParser$RepresentationInfo
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
        r14 = r32;
        r15 = r33;
        r0 = "id";
        r1 = 0;
        r16 = r15.getAttributeValue(r1, r0);
        r0 = "bandwidth";
        r1 = -1;
        r17 = parseInt(r15, r0, r1);
        r0 = "mimeType";
        r13 = r35;
        r18 = parseString(r15, r0, r13);
        r0 = "codecs";
        r12 = r36;
        r19 = parseString(r15, r0, r12);
        r0 = "width";
        r11 = r37;
        r20 = parseInt(r15, r0, r11);
        r0 = "height";
        r10 = r38;
        r21 = parseInt(r15, r0, r10);
        r9 = r39;
        r22 = parseFrameRate(r15, r9);
        r0 = r40;
        r1 = "audioSamplingRate";
        r8 = r41;
        r23 = parseInt(r15, r1, r8);
        r1 = 0;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r7 = r2;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r6 = r2;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r5 = r2;
        r2 = 0;
        r3 = r45;
        r4 = r1;
        r1 = r0;
        r0 = r34;
        r33.next();
        r24 = r1;
        r1 = "BaseURL";
        r1 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r1);
        if (r1 == 0) goto L_0x0080;
    L_0x0069:
        if (r2 != 0) goto L_0x007c;
    L_0x006b:
        r0 = parseBaseUrl(r15, r0);
        r1 = 1;
        r25 = r0;
        r27 = r3;
        r28 = r4;
        r26 = r24;
        r24 = r1;
        goto L_0x0124;
    L_0x007c:
        r25 = r0;
        goto L_0x011c;
    L_0x0080:
        r1 = "AudioChannelConfiguration";
        r1 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r1);
        if (r1 == 0) goto L_0x0098;
    L_0x0088:
        r1 = r32.parseAudioChannelConfiguration(r33);
        r25 = r0;
        r26 = r1;
        r24 = r2;
        r27 = r3;
        r28 = r4;
        goto L_0x0124;
    L_0x0098:
        r1 = "SegmentBase";
        r1 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r1);
        if (r1 == 0) goto L_0x00b3;
    L_0x00a0:
        r1 = r3;
        r1 = (org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SingleSegmentBase) r1;
        r1 = r14.parseSegmentBase(r15, r1);
    L_0x00a7:
        r25 = r0;
        r27 = r1;
        r28 = r4;
        r26 = r24;
        r24 = r2;
        goto L_0x0124;
    L_0x00b3:
        r1 = "SegmentList";
        r1 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r1);
        if (r1 == 0) goto L_0x00c3;
    L_0x00bb:
        r1 = r3;
        r1 = (org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SegmentList) r1;
        r1 = r14.parseSegmentList(r15, r1);
        goto L_0x00a7;
    L_0x00c3:
        r1 = "SegmentTemplate";
        r1 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r1);
        if (r1 == 0) goto L_0x00d3;
    L_0x00cb:
        r1 = r3;
        r1 = (org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase.SegmentTemplate) r1;
        r1 = r14.parseSegmentTemplate(r15, r1);
        goto L_0x00a7;
    L_0x00d3:
        r1 = "ContentProtection";
        r1 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r1);
        if (r1 == 0) goto L_0x00f7;
    L_0x00db:
        r1 = r32.parseContentProtection(r33);
        r25 = r0;
        r0 = r1.first;
        if (r0 == 0) goto L_0x00ea;
    L_0x00e5:
        r0 = r1.first;
        r4 = r0;
        r4 = (java.lang.String) r4;
    L_0x00ea:
        r0 = r1.second;
        if (r0 == 0) goto L_0x00f3;
    L_0x00ee:
        r0 = r1.second;
        r7.add(r0);
        r27 = r3;
        goto L_0x00ab;
    L_0x00f7:
        r25 = r0;
        r0 = "InbandEventStream";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x010b;
        r0 = "InbandEventStream";
        r0 = parseDescriptor(r15, r0);
        r6.add(r0);
        goto L_0x011c;
        r0 = "SupplementalProperty";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isStartTag(r15, r0);
        if (r0 == 0) goto L_0x011c;
        r0 = "SupplementalProperty";
        r0 = parseDescriptor(r15, r0);
        r5.add(r0);
    L_0x011c:
        r27 = r3;
        r28 = r4;
        r26 = r24;
        r24 = r2;
    L_0x0124:
        r0 = "Representation";
        r0 = org.telegram.messenger.exoplayer2.util.XmlPullParserUtil.isEndTag(r15, r0);
        if (r0 == 0) goto L_0x016e;
        r0 = r14;
        r1 = r16;
        r2 = r18;
        r3 = r20;
        r4 = r21;
        r29 = r5;
        r5 = r22;
        r30 = r6;
        r6 = r26;
        r31 = r7;
        r7 = r23;
        r8 = r17;
        r9 = r42;
        r10 = r43;
        r11 = r44;
        r12 = r19;
        r13 = r29;
        r0 = r0.buildFormat(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);
        if (r27 == 0) goto L_0x0156;
        r6 = r27;
        goto L_0x015c;
        r1 = new org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase$SingleSegmentBase;
        r1.<init>();
        r6 = r1;
        r1 = new org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifestParser$RepresentationInfo;
        r10 = -1;
        r3 = r1;
        r4 = r0;
        r5 = r25;
        r7 = r28;
        r8 = r31;
        r9 = r30;
        r3.<init>(r4, r5, r6, r7, r8, r9, r10);
        return r1;
        r13 = r35;
        r12 = r36;
        r11 = r37;
        r10 = r38;
        r9 = r39;
        r8 = r41;
        r2 = r24;
        r0 = r25;
        r1 = r26;
        r3 = r27;
        r4 = r28;
        goto L_0x005c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifestParser.parseRepresentation(org.xmlpull.v1.XmlPullParser, java.lang.String, java.lang.String, java.lang.String, int, int, float, int, int, java.lang.String, int, java.util.List, org.telegram.messenger.exoplayer2.source.dash.manifest.SegmentBase):org.telegram.messenger.exoplayer2.source.dash.manifest.DashManifestParser$RepresentationInfo");
    }

    public DashManifestParser() {
        this(null);
    }

    public DashManifestParser(String contentId) {
        this.contentId = contentId;
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    public DashManifest parse(Uri uri, InputStream inputStream) throws IOException {
        try {
            XmlPullParser xpp = this.xmlParserFactory.newPullParser();
            xpp.setInput(inputStream, null);
            if (xpp.next() == 2) {
                if ("MPD".equals(xpp.getName())) {
                    return parseMediaPresentationDescription(xpp, uri.toString());
                }
            }
            throw new ParserException("inputStream does not contain a valid media presentation description");
        } catch (Throwable e) {
            throw new ParserException(e);
        }
    }

    protected DashManifest parseMediaPresentationDescription(XmlPullParser xpp, String baseUrl) throws XmlPullParserException, IOException {
        DashManifestParser dashManifestParser;
        XmlPullParser xmlPullParser = xpp;
        long availabilityStartTime = parseDateTime(xmlPullParser, "availabilityStartTime", C.TIME_UNSET);
        long durationMs = parseDuration(xmlPullParser, "mediaPresentationDuration", C.TIME_UNSET);
        long minBufferTimeMs = parseDuration(xmlPullParser, "minBufferTime", C.TIME_UNSET);
        String typeString = xmlPullParser.getAttributeValue(null, "type");
        boolean seenFirstBaseUrl = false;
        boolean z = typeString != null && typeString.equals("dynamic");
        boolean dynamic = z;
        long minUpdateTimeMs = dynamic ? parseDuration(xmlPullParser, "minimumUpdatePeriod", C.TIME_UNSET) : C.TIME_UNSET;
        long timeShiftBufferDepthMs = dynamic ? parseDuration(xmlPullParser, "timeShiftBufferDepth", C.TIME_UNSET) : C.TIME_UNSET;
        long suggestedPresentationDelayMs = dynamic ? parseDuration(xmlPullParser, "suggestedPresentationDelay", C.TIME_UNSET) : C.TIME_UNSET;
        long publishTimeMs = parseDateTime(xmlPullParser, "publishTime", C.TIME_UNSET);
        List<Period> periods = new ArrayList();
        boolean seenEarlyAccessPeriod = false;
        long nextPeriodStartMs = dynamic ? C.TIME_UNSET : 0;
        Uri location = null;
        UtcTimingElement utcTiming = null;
        String baseUrl2 = baseUrl;
        while (true) {
            String typeString2;
            boolean seenFirstBaseUrl2;
            String baseUrl3;
            UtcTimingElement utcTimingElement;
            Uri uri;
            boolean seenEarlyAccessPeriod2;
            xpp.next();
            long nextPeriodStartMs2;
            if (!XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (!XmlPullParserUtil.isStartTag(xmlPullParser, "UTCTiming")) {
                    if (!XmlPullParserUtil.isStartTag(xmlPullParser, "Location")) {
                        if (XmlPullParserUtil.isStartTag(xmlPullParser, "Period") && !seenEarlyAccessPeriod) {
                            typeString2 = typeString;
                            typeString = parsePeriod(xmlPullParser, baseUrl2, nextPeriodStartMs);
                            nextPeriodStartMs2 = nextPeriodStartMs;
                            Period period = typeString.first;
                            seenFirstBaseUrl2 = seenFirstBaseUrl;
                            baseUrl3 = baseUrl2;
                            if (period.startMs != C.TIME_UNSET) {
                                long j;
                                seenFirstBaseUrl = ((Long) typeString.second).longValue();
                                if (seenFirstBaseUrl == C.TIME_UNSET) {
                                    utcTimingElement = utcTiming;
                                    uri = location;
                                    j = C.TIME_UNSET;
                                } else {
                                    utcTimingElement = utcTiming;
                                    uri = location;
                                    j = period.startMs + seenFirstBaseUrl;
                                }
                                utcTiming = j;
                                periods.add(period);
                                nextPeriodStartMs = utcTiming;
                                seenEarlyAccessPeriod2 = seenEarlyAccessPeriod;
                                if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
                                    break;
                                }
                                seenEarlyAccessPeriod = seenEarlyAccessPeriod2;
                                typeString = typeString2;
                                baseUrl2 = baseUrl3;
                                seenFirstBaseUrl = seenFirstBaseUrl2;
                                utcTiming = utcTimingElement;
                                location = uri;
                            } else if (dynamic) {
                                seenEarlyAccessPeriod2 = true;
                                utcTimingElement = utcTiming;
                                uri = location;
                                nextPeriodStartMs = nextPeriodStartMs2;
                                if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
                                    break;
                                }
                                seenEarlyAccessPeriod = seenEarlyAccessPeriod2;
                                typeString = typeString2;
                                baseUrl2 = baseUrl3;
                                seenFirstBaseUrl = seenFirstBaseUrl2;
                                utcTiming = utcTimingElement;
                                location = uri;
                            } else {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Unable to determine start of period ");
                                stringBuilder.append(periods.size());
                                throw new ParserException(stringBuilder.toString());
                            }
                        }
                        dashManifestParser = this;
                        typeString2 = typeString;
                        nextPeriodStartMs2 = nextPeriodStartMs;
                        seenFirstBaseUrl2 = seenFirstBaseUrl;
                        baseUrl3 = baseUrl2;
                        utcTimingElement = utcTiming;
                        uri = location;
                        seenEarlyAccessPeriod2 = seenEarlyAccessPeriod;
                        nextPeriodStartMs = nextPeriodStartMs2;
                        if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
                            break;
                        }
                        seenEarlyAccessPeriod = seenEarlyAccessPeriod2;
                        typeString = typeString2;
                        baseUrl2 = baseUrl3;
                        seenFirstBaseUrl = seenFirstBaseUrl2;
                        utcTiming = utcTimingElement;
                        location = uri;
                    } else {
                        location = Uri.parse(xpp.nextText());
                    }
                } else {
                    utcTiming = parseUtcTiming(xpp);
                }
            } else if (seenFirstBaseUrl) {
                dashManifestParser = this;
                typeString2 = typeString;
                nextPeriodStartMs2 = nextPeriodStartMs;
                seenFirstBaseUrl2 = seenFirstBaseUrl;
                baseUrl3 = baseUrl2;
                utcTimingElement = utcTiming;
                uri = location;
                seenEarlyAccessPeriod2 = seenEarlyAccessPeriod;
                nextPeriodStartMs = nextPeriodStartMs2;
                if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
                    break;
                }
                seenEarlyAccessPeriod = seenEarlyAccessPeriod2;
                typeString = typeString2;
                baseUrl2 = baseUrl3;
                seenFirstBaseUrl = seenFirstBaseUrl2;
                utcTiming = utcTimingElement;
                location = uri;
            } else {
                baseUrl2 = parseBaseUrl(xmlPullParser, baseUrl2);
                seenFirstBaseUrl = true;
            }
            dashManifestParser = this;
            typeString2 = typeString;
            seenFirstBaseUrl2 = seenFirstBaseUrl;
            baseUrl3 = baseUrl2;
            utcTimingElement = utcTiming;
            uri = location;
            seenEarlyAccessPeriod2 = seenEarlyAccessPeriod;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "MPD")) {
                break;
            }
            seenEarlyAccessPeriod = seenEarlyAccessPeriod2;
            typeString = typeString2;
            baseUrl2 = baseUrl3;
            seenFirstBaseUrl = seenFirstBaseUrl2;
            utcTiming = utcTimingElement;
            location = uri;
        }
        if (durationMs == C.TIME_UNSET) {
            if (nextPeriodStartMs != C.TIME_UNSET) {
                durationMs = nextPeriodStartMs;
            } else if (!dynamic) {
                throw new ParserException("Unable to determine duration of static manifest.");
            }
        }
        long durationMs2 = durationMs;
        if (periods.isEmpty()) {
            throw new ParserException("No periods found.");
        }
        return dashManifestParser.buildMediaPresentationDescription(availabilityStartTime, durationMs2, minBufferTimeMs, dynamic, minUpdateTimeMs, timeShiftBufferDepthMs, suggestedPresentationDelayMs, publishTimeMs, utcTimingElement, uri, periods);
    }

    protected DashManifest buildMediaPresentationDescription(long availabilityStartTime, long durationMs, long minBufferTimeMs, boolean dynamic, long minUpdateTimeMs, long timeShiftBufferDepthMs, long suggestedPresentationDelayMs, long publishTimeMs, UtcTimingElement utcTiming, Uri location, List<Period> periods) {
        return new DashManifest(availabilityStartTime, durationMs, minBufferTimeMs, dynamic, minUpdateTimeMs, timeShiftBufferDepthMs, suggestedPresentationDelayMs, publishTimeMs, utcTiming, location, periods);
    }

    protected UtcTimingElement parseUtcTiming(XmlPullParser xpp) {
        return buildUtcTimingElement(xpp.getAttributeValue(null, "schemeIdUri"), xpp.getAttributeValue(null, "value"));
    }

    protected UtcTimingElement buildUtcTimingElement(String schemeIdUri, String value) {
        return new UtcTimingElement(schemeIdUri, value);
    }

    protected Pair<Period, Long> parsePeriod(XmlPullParser xpp, String baseUrl, long defaultStartMs) throws XmlPullParserException, IOException {
        DashManifestParser dashManifestParser = this;
        XmlPullParser xmlPullParser = xpp;
        String id = xmlPullParser.getAttributeValue(null, TtmlNode.ATTR_ID);
        long startMs = parseDuration(xmlPullParser, TtmlNode.START, defaultStartMs);
        long durationMs = parseDuration(xmlPullParser, "duration", C.TIME_UNSET);
        List adaptationSets = new ArrayList();
        List<EventStream> eventStreams = new ArrayList();
        boolean seenFirstBaseUrl = false;
        SegmentBase segmentBase = null;
        String baseUrl2 = baseUrl;
        while (true) {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "BaseURL")) {
                if (!seenFirstBaseUrl) {
                    baseUrl2 = parseBaseUrl(xmlPullParser, baseUrl2);
                    seenFirstBaseUrl = true;
                }
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "AdaptationSet")) {
                adaptationSets.add(parseAdaptationSet(xmlPullParser, baseUrl2, segmentBase));
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "EventStream")) {
                eventStreams.add(parseEventStream(xpp));
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentBase")) {
                segmentBase = parseSegmentBase(xmlPullParser, null);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentList")) {
                segmentBase = parseSegmentList(xmlPullParser, null);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTemplate")) {
                segmentBase = parseSegmentTemplate(xmlPullParser, null);
            }
            String baseUrl3 = baseUrl2;
            boolean seenFirstBaseUrl2 = seenFirstBaseUrl;
            SegmentBase segmentBase2 = segmentBase;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "Period")) {
                return Pair.create(buildPeriod(id, startMs, adaptationSets, eventStreams), Long.valueOf(durationMs));
            }
            baseUrl2 = baseUrl3;
            seenFirstBaseUrl = seenFirstBaseUrl2;
            segmentBase = segmentBase2;
        }
    }

    protected Period buildPeriod(String id, long startMs, List<AdaptationSet> adaptationSets, List<EventStream> eventStreams) {
        return new Period(id, startMs, adaptationSets, eventStreams);
    }

    protected AdaptationSet buildAdaptationSet(int id, int contentType, List<Representation> representations, List<Descriptor> accessibilityDescriptors, List<Descriptor> supplementalProperties) {
        return new AdaptationSet(id, contentType, representations, accessibilityDescriptors, supplementalProperties);
    }

    protected int parseContentType(XmlPullParser xpp) {
        String contentType = xpp.getAttributeValue(null, "contentType");
        if (TextUtils.isEmpty(contentType)) {
            return -1;
        }
        if (MimeTypes.BASE_TYPE_AUDIO.equals(contentType)) {
            return 1;
        }
        if (MimeTypes.BASE_TYPE_VIDEO.equals(contentType)) {
            return 2;
        }
        if (MimeTypes.BASE_TYPE_TEXT.equals(contentType)) {
            return 3;
        }
        return -1;
    }

    protected int getContentType(Format format) {
        String sampleMimeType = format.sampleMimeType;
        if (TextUtils.isEmpty(sampleMimeType)) {
            return -1;
        }
        if (MimeTypes.isVideo(sampleMimeType)) {
            return 2;
        }
        if (MimeTypes.isAudio(sampleMimeType)) {
            return 1;
        }
        if (mimeTypeIsRawText(sampleMimeType)) {
            return 3;
        }
        return -1;
    }

    protected Pair<String, SchemeData> parseContentProtection(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String toLowerInvariant;
        String schemeType = null;
        byte[] data = null;
        UUID uuid = null;
        boolean requiresSecureDecoder = false;
        SchemeData schemeData = null;
        String schemeIdUri = xpp.getAttributeValue(null, "schemeIdUri");
        if (schemeIdUri != null) {
            int i;
            String[] defaultKidStrings;
            UUID[] defaultKids;
            int i2;
            toLowerInvariant = Util.toLowerInvariant(schemeIdUri);
            int hashCode = toLowerInvariant.hashCode();
            if (hashCode == 489446379) {
                if (toLowerInvariant.equals("urn:uuid:9a04f079-9840-4286-ab92-e65be0885f95")) {
                    i = 1;
                    switch (i) {
                        case 0:
                            schemeType = xpp.getAttributeValue(null, "value");
                            toLowerInvariant = xpp.getAttributeValue(null, "cenc:default_KID");
                            defaultKidStrings = toLowerInvariant.split("\\s+");
                            defaultKids = new UUID[defaultKidStrings.length];
                            for (i2 = 0; i2 < defaultKidStrings.length; i2++) {
                                defaultKids[i2] = UUID.fromString(defaultKidStrings[i2]);
                            }
                            data = PsshAtomUtil.buildPsshAtom(C.COMMON_PSSH_UUID, defaultKids, null);
                            uuid = C.COMMON_PSSH_UUID;
                            break;
                        case 1:
                            uuid = C.PLAYREADY_UUID;
                            break;
                        case 2:
                            uuid = C.WIDEVINE_UUID;
                            break;
                        default:
                            break;
                    }
                }
            } else if (hashCode == 755418770) {
                if (toLowerInvariant.equals("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed")) {
                    i = 2;
                    switch (i) {
                        case 0:
                            schemeType = xpp.getAttributeValue(null, "value");
                            toLowerInvariant = xpp.getAttributeValue(null, "cenc:default_KID");
                            defaultKidStrings = toLowerInvariant.split("\\s+");
                            defaultKids = new UUID[defaultKidStrings.length];
                            for (i2 = 0; i2 < defaultKidStrings.length; i2++) {
                                defaultKids[i2] = UUID.fromString(defaultKidStrings[i2]);
                            }
                            data = PsshAtomUtil.buildPsshAtom(C.COMMON_PSSH_UUID, defaultKids, null);
                            uuid = C.COMMON_PSSH_UUID;
                            break;
                        case 1:
                            uuid = C.PLAYREADY_UUID;
                            break;
                        case 2:
                            uuid = C.WIDEVINE_UUID;
                            break;
                        default:
                            break;
                    }
                }
            } else if (hashCode == 1812765994) {
                if (toLowerInvariant.equals("urn:mpeg:dash:mp4protection:2011")) {
                    i = 0;
                    switch (i) {
                        case 0:
                            schemeType = xpp.getAttributeValue(null, "value");
                            toLowerInvariant = xpp.getAttributeValue(null, "cenc:default_KID");
                            if (!(TextUtils.isEmpty(toLowerInvariant) || "00000000-0000-0000-0000-000000000000".equals(toLowerInvariant))) {
                                defaultKidStrings = toLowerInvariant.split("\\s+");
                                defaultKids = new UUID[defaultKidStrings.length];
                                for (i2 = 0; i2 < defaultKidStrings.length; i2++) {
                                    defaultKids[i2] = UUID.fromString(defaultKidStrings[i2]);
                                }
                                data = PsshAtomUtil.buildPsshAtom(C.COMMON_PSSH_UUID, defaultKids, null);
                                uuid = C.COMMON_PSSH_UUID;
                                break;
                            }
                        case 1:
                            uuid = C.PLAYREADY_UUID;
                            break;
                        case 2:
                            uuid = C.WIDEVINE_UUID;
                            break;
                        default:
                            break;
                    }
                }
            }
            i = -1;
            switch (i) {
                case 0:
                    schemeType = xpp.getAttributeValue(null, "value");
                    toLowerInvariant = xpp.getAttributeValue(null, "cenc:default_KID");
                    defaultKidStrings = toLowerInvariant.split("\\s+");
                    defaultKids = new UUID[defaultKidStrings.length];
                    for (i2 = 0; i2 < defaultKidStrings.length; i2++) {
                        defaultKids[i2] = UUID.fromString(defaultKidStrings[i2]);
                    }
                    data = PsshAtomUtil.buildPsshAtom(C.COMMON_PSSH_UUID, defaultKids, null);
                    uuid = C.COMMON_PSSH_UUID;
                    break;
                case 1:
                    uuid = C.PLAYREADY_UUID;
                    break;
                case 2:
                    uuid = C.WIDEVINE_UUID;
                    break;
                default:
                    break;
            }
        }
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xpp, "widevine:license")) {
                toLowerInvariant = xpp.getAttributeValue(null, "robustness_level");
                boolean z = toLowerInvariant != null && toLowerInvariant.startsWith("HW");
                requiresSecureDecoder = z;
            } else if (data == null) {
                if (XmlPullParserUtil.isStartTag(xpp, "cenc:pssh") && xpp.next() == 4) {
                    data = Base64.decode(xpp.getText(), 0);
                    uuid = PsshAtomUtil.parseUuid(data);
                    if (uuid == null) {
                        Log.w(TAG, "Skipping malformed cenc:pssh data");
                        data = null;
                    }
                } else if (C.PLAYREADY_UUID.equals(uuid) && XmlPullParserUtil.isStartTag(xpp, "mspr:pro") && xpp.next() == 4) {
                    data = PsshAtomUtil.buildPsshAtom(C.PLAYREADY_UUID, Base64.decode(xpp.getText(), 0));
                }
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "ContentProtection"));
        if (uuid != null) {
            schemeData = new SchemeData(uuid, MimeTypes.VIDEO_MP4, data, requiresSecureDecoder);
        }
        return Pair.create(schemeType, schemeData);
    }

    protected int parseRole(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String schemeIdUri = parseString(xpp, "schemeIdUri", null);
        String value = parseString(xpp, "value", null);
        do {
            xpp.next();
        } while (!XmlPullParserUtil.isEndTag(xpp, "Role"));
        return ("urn:mpeg:dash:role:2011".equals(schemeIdUri) && "main".equals(value)) ? 1 : 0;
    }

    protected void parseAdaptationSetChild(XmlPullParser xpp) throws XmlPullParserException, IOException {
    }

    protected Format buildFormat(String id, String containerMimeType, int width, int height, float frameRate, int audioChannels, int audioSamplingRate, int bitrate, String language, int selectionFlags, List<Descriptor> accessibilityDescriptors, String codecs, List<Descriptor> supplementalProperties) {
        String sampleMimeType;
        String str = containerMimeType;
        String str2 = codecs;
        String sampleMimeType2 = getSampleMimeType(str, str2);
        if (sampleMimeType2 != null) {
            if (MimeTypes.AUDIO_E_AC3.equals(sampleMimeType2)) {
                sampleMimeType2 = parseEac3SupplementalProperties(supplementalProperties);
            }
            sampleMimeType = sampleMimeType2;
            if (MimeTypes.isVideo(sampleMimeType)) {
                return Format.createVideoContainerFormat(id, str, sampleMimeType, str2, bitrate, width, height, frameRate, null, selectionFlags);
            }
            if (MimeTypes.isAudio(sampleMimeType)) {
                return Format.createAudioContainerFormat(id, str, sampleMimeType, str2, bitrate, audioChannels, audioSamplingRate, null, selectionFlags, language);
            }
            if (mimeTypeIsRawText(sampleMimeType)) {
                int parseCea608AccessibilityChannel;
                if (MimeTypes.APPLICATION_CEA608.equals(sampleMimeType)) {
                    parseCea608AccessibilityChannel = parseCea608AccessibilityChannel(accessibilityDescriptors);
                } else if (MimeTypes.APPLICATION_CEA708.equals(sampleMimeType)) {
                    parseCea608AccessibilityChannel = parseCea708AccessibilityChannel(accessibilityDescriptors);
                } else {
                    parseCea608AccessibilityChannel = -1;
                }
                return Format.createTextContainerFormat(id, str, sampleMimeType, str2, bitrate, selectionFlags, language, parseCea608AccessibilityChannel);
            }
        }
        sampleMimeType = sampleMimeType2;
        return Format.createContainerFormat(id, str, sampleMimeType, str2, bitrate, selectionFlags, language);
    }

    protected Representation buildRepresentation(RepresentationInfo representationInfo, String contentId, String extraDrmSchemeType, ArrayList<SchemeData> extraDrmSchemeDatas, ArrayList<Descriptor> extraInbandEventStreams) {
        RepresentationInfo representationInfo2 = representationInfo;
        Format format = representationInfo2.format;
        String drmSchemeType = representationInfo2.drmSchemeType != null ? representationInfo2.drmSchemeType : extraDrmSchemeType;
        List drmSchemeDatas = representationInfo2.drmSchemeDatas;
        drmSchemeDatas.addAll(extraDrmSchemeDatas);
        if (!drmSchemeDatas.isEmpty()) {
            filterRedundantIncompleteSchemeDatas(drmSchemeDatas);
            format = format.copyWithDrmInitData(new DrmInitData(drmSchemeType, drmSchemeDatas));
        }
        ArrayList<Descriptor> inbandEventStreams = representationInfo2.inbandEventStreams;
        inbandEventStreams.addAll(extraInbandEventStreams);
        return Representation.newInstance(contentId, representationInfo2.revisionId, format, representationInfo2.baseUrl, representationInfo2.segmentBase, inbandEventStreams);
    }

    protected SingleSegmentBase parseSegmentBase(XmlPullParser xpp, SingleSegmentBase parent) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = xpp;
        SingleSegmentBase singleSegmentBase = parent;
        long timescale = parseLong(xmlPullParser, "timescale", singleSegmentBase != null ? singleSegmentBase.timescale : 1);
        long j = 0;
        long presentationTimeOffset = parseLong(xmlPullParser, "presentationTimeOffset", singleSegmentBase != null ? singleSegmentBase.presentationTimeOffset : 0);
        long j2 = singleSegmentBase != null ? singleSegmentBase.indexStart : 0;
        if (singleSegmentBase != null) {
            j = singleSegmentBase.indexLength;
        }
        RangedUri initialization = null;
        String indexRangeText = xmlPullParser.getAttributeValue(null, "indexRange");
        if (indexRangeText != null) {
            String[] indexRange = indexRangeText.split("-");
            j2 = Long.parseLong(indexRange[0]);
            j = (Long.parseLong(indexRange[1]) - j2) + 1;
        }
        long indexLength = j;
        long indexStart = j2;
        if (singleSegmentBase != null) {
            initialization = singleSegmentBase.initialization;
        }
        while (true) {
            RangedUri initialization2 = initialization;
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                initialization2 = parseInitialization(xpp);
            }
            RangedUri initialization3 = initialization2;
            if (XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentBase")) {
                return buildSingleSegmentBase(initialization3, timescale, presentationTimeOffset, indexStart, indexLength);
            }
            initialization = initialization3;
        }
    }

    protected SingleSegmentBase buildSingleSegmentBase(RangedUri initialization, long timescale, long presentationTimeOffset, long indexStart, long indexLength) {
        return new SingleSegmentBase(initialization, timescale, presentationTimeOffset, indexStart, indexLength);
    }

    protected SegmentList parseSegmentList(XmlPullParser xpp, SegmentList parent) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = xpp;
        SegmentList segmentList = parent;
        long timescale = parseLong(xmlPullParser, "timescale", segmentList != null ? segmentList.timescale : 1);
        long presentationTimeOffset = parseLong(xmlPullParser, "presentationTimeOffset", segmentList != null ? segmentList.presentationTimeOffset : 0);
        long duration = parseLong(xmlPullParser, "duration", segmentList != null ? segmentList.duration : C.TIME_UNSET);
        int startNumber = parseInt(xmlPullParser, "startNumber", segmentList != null ? segmentList.startNumber : 1);
        RangedUri initialization = null;
        List<SegmentTimelineElement> timeline = null;
        List<RangedUri> segments = null;
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                initialization = parseInitialization(xpp);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTimeline")) {
                timeline = parseSegmentTimeline(xpp);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentURL")) {
                if (segments == null) {
                    segments = new ArrayList();
                }
                segments.add(parseSegmentUrl(xpp));
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentList"));
        if (segmentList != null) {
            initialization = initialization != null ? initialization : segmentList.initialization;
            timeline = timeline != null ? timeline : segmentList.segmentTimeline;
            segments = segments != null ? segments : segmentList.mediaSegments;
        }
        return buildSegmentList(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, segments);
    }

    protected SegmentList buildSegmentList(RangedUri initialization, long timescale, long presentationTimeOffset, int startNumber, long duration, List<SegmentTimelineElement> timeline, List<RangedUri> segments) {
        return new SegmentList(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, segments);
    }

    protected SegmentTemplate parseSegmentTemplate(XmlPullParser xpp, SegmentTemplate parent) throws XmlPullParserException, IOException {
        DashManifestParser dashManifestParser = this;
        XmlPullParser xmlPullParser = xpp;
        SegmentTemplate segmentTemplate = parent;
        long timescale = parseLong(xmlPullParser, "timescale", segmentTemplate != null ? segmentTemplate.timescale : 1);
        long presentationTimeOffset = parseLong(xmlPullParser, "presentationTimeOffset", segmentTemplate != null ? segmentTemplate.presentationTimeOffset : 0);
        long duration = parseLong(xmlPullParser, "duration", segmentTemplate != null ? segmentTemplate.duration : C.TIME_UNSET);
        int startNumber = parseInt(xmlPullParser, "startNumber", segmentTemplate != null ? segmentTemplate.startNumber : 1);
        List<SegmentTimelineElement> timeline = null;
        UrlTemplate mediaTemplate = parseUrlTemplate(xmlPullParser, "media", segmentTemplate != null ? segmentTemplate.mediaTemplate : null);
        UrlTemplate initializationTemplate = parseUrlTemplate(xmlPullParser, "initialization", segmentTemplate != null ? segmentTemplate.initializationTemplate : null);
        RangedUri initialization = null;
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Initialization")) {
                initialization = parseInitialization(xpp);
            } else if (XmlPullParserUtil.isStartTag(xmlPullParser, "SegmentTimeline")) {
                timeline = parseSegmentTimeline(xpp);
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "SegmentTemplate"));
        if (segmentTemplate != null) {
            initialization = initialization != null ? initialization : segmentTemplate.initialization;
            timeline = timeline != null ? timeline : segmentTemplate.segmentTimeline;
        }
        return buildSegmentTemplate(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, initializationTemplate, mediaTemplate);
    }

    protected SegmentTemplate buildSegmentTemplate(RangedUri initialization, long timescale, long presentationTimeOffset, int startNumber, long duration, List<SegmentTimelineElement> timeline, UrlTemplate initializationTemplate, UrlTemplate mediaTemplate) {
        return new SegmentTemplate(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, initializationTemplate, mediaTemplate);
    }

    protected EventStream parseEventStream(XmlPullParser xpp) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = xpp;
        String schemeIdUri = parseString(xmlPullParser, "schemeIdUri", TtmlNode.ANONYMOUS_REGION_ID);
        String value = parseString(xmlPullParser, "value", TtmlNode.ANONYMOUS_REGION_ID);
        long timescale = parseLong(xmlPullParser, "timescale", 1);
        List<EventMessage> eventMessages = new ArrayList();
        ByteArrayOutputStream scratchOutputStream = new ByteArrayOutputStream(512);
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xmlPullParser, "Event")) {
                eventMessages.add(parseEvent(xmlPullParser, schemeIdUri, value, timescale, scratchOutputStream));
            }
        } while (!XmlPullParserUtil.isEndTag(xmlPullParser, "EventStream"));
        long[] presentationTimesUs = new long[eventMessages.size()];
        EventMessage[] events = new EventMessage[eventMessages.size()];
        for (int i = 0; i < eventMessages.size(); i++) {
            EventMessage event = (EventMessage) eventMessages.get(i);
            presentationTimesUs[i] = event.presentationTimeUs;
            events[i] = event;
        }
        return buildEventStream(schemeIdUri, value, timescale, presentationTimesUs, events);
    }

    protected EventStream buildEventStream(String schemeIdUri, String value, long timescale, long[] presentationTimesUs, EventMessage[] events) {
        return new EventStream(schemeIdUri, value, timescale, presentationTimesUs, events);
    }

    protected EventMessage parseEvent(XmlPullParser xpp, String schemeIdUri, String value, long timescale, ByteArrayOutputStream scratchOutputStream) throws IOException, XmlPullParserException {
        XmlPullParser xmlPullParser = xpp;
        long id = parseLong(xmlPullParser, TtmlNode.ATTR_ID, 0);
        long duration = parseLong(xmlPullParser, "duration", C.TIME_UNSET);
        long presentationTime = parseLong(xmlPullParser, "presentationTime", 0);
        return buildEvent(schemeIdUri, value, id, Util.scaleLargeTimestamp(duration, 1000, timescale), parseEventObject(xmlPullParser, scratchOutputStream), Util.scaleLargeTimestamp(presentationTime, C.MICROS_PER_SECOND, timescale));
    }

    protected byte[] parseEventObject(XmlPullParser xpp, ByteArrayOutputStream scratchOutputStream) throws XmlPullParserException, IOException {
        scratchOutputStream.reset();
        XmlSerializer xmlSerializer = Xml.newSerializer();
        xmlSerializer.setOutput(scratchOutputStream, null);
        xpp.nextToken();
        while (!XmlPullParserUtil.isEndTag(xpp, "Event")) {
            int i = 0;
            switch (xpp.getEventType()) {
                case 0:
                    xmlSerializer.startDocument(null, Boolean.valueOf(false));
                    break;
                case 1:
                    xmlSerializer.endDocument();
                    break;
                case 2:
                    xmlSerializer.startTag(xpp.getNamespace(), xpp.getName());
                    while (true) {
                        int i2 = i;
                        if (i2 >= xpp.getAttributeCount()) {
                            break;
                        }
                        xmlSerializer.attribute(xpp.getAttributeNamespace(i2), xpp.getAttributeName(i2), xpp.getAttributeValue(i2));
                        i = i2 + 1;
                    }
                case 3:
                    xmlSerializer.endTag(xpp.getNamespace(), xpp.getName());
                    break;
                case 4:
                    xmlSerializer.text(xpp.getText());
                    break;
                case 5:
                    xmlSerializer.cdsect(xpp.getText());
                    break;
                case 6:
                    xmlSerializer.entityRef(xpp.getText());
                    break;
                case 7:
                    xmlSerializer.ignorableWhitespace(xpp.getText());
                    break;
                case 8:
                    xmlSerializer.processingInstruction(xpp.getText());
                    break;
                case 9:
                    xmlSerializer.comment(xpp.getText());
                    break;
                case 10:
                    xmlSerializer.docdecl(xpp.getText());
                    break;
                default:
                    break;
            }
            xpp.nextToken();
        }
        xmlSerializer.flush();
        return scratchOutputStream.toByteArray();
    }

    protected EventMessage buildEvent(String schemeIdUri, String value, long id, long durationMs, byte[] messageData, long presentationTimeUs) {
        return new EventMessage(schemeIdUri, value, durationMs, id, messageData, presentationTimeUs);
    }

    protected List<SegmentTimelineElement> parseSegmentTimeline(XmlPullParser xpp) throws XmlPullParserException, IOException {
        List<SegmentTimelineElement> segmentTimeline = new ArrayList();
        long elapsedTime = 0;
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xpp, "S")) {
                elapsedTime = parseLong(xpp, "t", elapsedTime);
                long duration = parseLong(xpp, "d", C.TIME_UNSET);
                int i = 0;
                int count = 1 + parseInt(xpp, "r", 0);
                while (true) {
                    int i2 = i;
                    if (i2 >= count) {
                        break;
                    }
                    segmentTimeline.add(buildSegmentTimelineElement(elapsedTime, duration));
                    i = i2 + 1;
                    elapsedTime += duration;
                }
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "SegmentTimeline"));
        return segmentTimeline;
    }

    protected SegmentTimelineElement buildSegmentTimelineElement(long elapsedTime, long duration) {
        return new SegmentTimelineElement(elapsedTime, duration);
    }

    protected UrlTemplate parseUrlTemplate(XmlPullParser xpp, String name, UrlTemplate defaultValue) {
        String valueString = xpp.getAttributeValue(null, name);
        if (valueString != null) {
            return UrlTemplate.compile(valueString);
        }
        return defaultValue;
    }

    protected RangedUri parseInitialization(XmlPullParser xpp) {
        return parseRangedUrl(xpp, "sourceURL", "range");
    }

    protected RangedUri parseSegmentUrl(XmlPullParser xpp) {
        return parseRangedUrl(xpp, "media", "mediaRange");
    }

    protected RangedUri parseRangedUrl(XmlPullParser xpp, String urlAttribute, String rangeAttribute) {
        String urlText = xpp.getAttributeValue(null, urlAttribute);
        long rangeStart = 0;
        long rangeLength = -1;
        String rangeText = xpp.getAttributeValue(null, rangeAttribute);
        if (rangeText != null) {
            String[] rangeTextArray = rangeText.split("-");
            rangeStart = Long.parseLong(rangeTextArray[0]);
            if (rangeTextArray.length == 2) {
                rangeLength = (Long.parseLong(rangeTextArray[1]) - rangeStart) + 1;
            }
        }
        return buildRangedUri(urlText, rangeStart, rangeLength);
    }

    protected RangedUri buildRangedUri(String urlText, long rangeStart, long rangeLength) {
        return new RangedUri(urlText, rangeStart, rangeLength);
    }

    protected int parseAudioChannelConfiguration(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String schemeIdUri = parseString(xpp, "schemeIdUri", null);
        int audioChannels = -1;
        if ("urn:mpeg:dash:23003:3:audio_channel_configuration:2011".equals(schemeIdUri)) {
            audioChannels = parseInt(xpp, "value", -1);
        } else if ("tag:dolby.com,2014:dash:audio_channel_configuration:2011".equals(schemeIdUri)) {
            audioChannels = parseDolbyChannelConfiguration(xpp);
        }
        do {
            xpp.next();
        } while (!XmlPullParserUtil.isEndTag(xpp, "AudioChannelConfiguration"));
        return audioChannels;
    }

    private static void filterRedundantIncompleteSchemeDatas(ArrayList<SchemeData> schemeDatas) {
        for (int i = schemeDatas.size() - 1; i >= 0; i--) {
            SchemeData schemeData = (SchemeData) schemeDatas.get(i);
            if (!schemeData.hasData()) {
                for (int j = 0; j < schemeDatas.size(); j++) {
                    if (((SchemeData) schemeDatas.get(j)).canReplace(schemeData)) {
                        schemeDatas.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private static String getSampleMimeType(String containerMimeType, String codecs) {
        if (MimeTypes.isAudio(containerMimeType)) {
            return MimeTypes.getAudioMediaMimeType(codecs);
        }
        if (MimeTypes.isVideo(containerMimeType)) {
            return MimeTypes.getVideoMediaMimeType(codecs);
        }
        if (mimeTypeIsRawText(containerMimeType)) {
            return containerMimeType;
        }
        if (!MimeTypes.APPLICATION_MP4.equals(containerMimeType)) {
            if (MimeTypes.APPLICATION_RAWCC.equals(containerMimeType) && codecs != null) {
                if (codecs.contains("cea708")) {
                    return MimeTypes.APPLICATION_CEA708;
                }
                if (codecs.contains("eia608") || codecs.contains("cea608")) {
                    return MimeTypes.APPLICATION_CEA608;
                }
            }
            return null;
        } else if ("stpp".equals(codecs)) {
            return MimeTypes.APPLICATION_TTML;
        } else {
            if ("wvtt".equals(codecs)) {
                return MimeTypes.APPLICATION_MP4VTT;
            }
        }
        return null;
    }

    private static boolean mimeTypeIsRawText(String mimeType) {
        if (!(MimeTypes.isText(mimeType) || MimeTypes.APPLICATION_TTML.equals(mimeType) || MimeTypes.APPLICATION_MP4VTT.equals(mimeType) || MimeTypes.APPLICATION_CEA708.equals(mimeType))) {
            if (!MimeTypes.APPLICATION_CEA608.equals(mimeType)) {
                return false;
            }
        }
        return true;
    }

    private static String checkLanguageConsistency(String firstLanguage, String secondLanguage) {
        if (firstLanguage == null) {
            return secondLanguage;
        }
        if (secondLanguage == null) {
            return firstLanguage;
        }
        Assertions.checkState(firstLanguage.equals(secondLanguage));
        return firstLanguage;
    }

    private static int checkContentTypeConsistency(int firstType, int secondType) {
        if (firstType == -1) {
            return secondType;
        }
        if (secondType == -1) {
            return firstType;
        }
        Assertions.checkState(firstType == secondType);
        return firstType;
    }

    protected static Descriptor parseDescriptor(XmlPullParser xpp, String tag) throws XmlPullParserException, IOException {
        String schemeIdUri = parseString(xpp, "schemeIdUri", TtmlNode.ANONYMOUS_REGION_ID);
        String value = parseString(xpp, "value", null);
        String id = parseString(xpp, TtmlNode.ATTR_ID, null);
        do {
            xpp.next();
        } while (!XmlPullParserUtil.isEndTag(xpp, tag));
        return new Descriptor(schemeIdUri, value, id);
    }

    protected static int parseCea608AccessibilityChannel(List<Descriptor> accessibilityDescriptors) {
        for (int i = 0; i < accessibilityDescriptors.size(); i++) {
            Descriptor descriptor = (Descriptor) accessibilityDescriptors.get(i);
            if ("urn:scte:dash:cc:cea-608:2015".equals(descriptor.schemeIdUri) && descriptor.value != null) {
                Matcher accessibilityValueMatcher = CEA_608_ACCESSIBILITY_PATTERN.matcher(descriptor.value);
                if (accessibilityValueMatcher.matches()) {
                    return Integer.parseInt(accessibilityValueMatcher.group(1));
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to parse CEA-608 channel number from: ");
                stringBuilder.append(descriptor.value);
                Log.w(str, stringBuilder.toString());
            }
        }
        return -1;
    }

    protected static int parseCea708AccessibilityChannel(List<Descriptor> accessibilityDescriptors) {
        for (int i = 0; i < accessibilityDescriptors.size(); i++) {
            Descriptor descriptor = (Descriptor) accessibilityDescriptors.get(i);
            if ("urn:scte:dash:cc:cea-708:2015".equals(descriptor.schemeIdUri) && descriptor.value != null) {
                Matcher accessibilityValueMatcher = CEA_708_ACCESSIBILITY_PATTERN.matcher(descriptor.value);
                if (accessibilityValueMatcher.matches()) {
                    return Integer.parseInt(accessibilityValueMatcher.group(1));
                }
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to parse CEA-708 service block number from: ");
                stringBuilder.append(descriptor.value);
                Log.w(str, stringBuilder.toString());
            }
        }
        return -1;
    }

    protected static String parseEac3SupplementalProperties(List<Descriptor> supplementalProperties) {
        for (int i = 0; i < supplementalProperties.size(); i++) {
            Descriptor descriptor = (Descriptor) supplementalProperties.get(i);
            if ("tag:dolby.com,2014:dash:DolbyDigitalPlusExtensionType:2014".equals(descriptor.schemeIdUri) && "ec+3".equals(descriptor.value)) {
                return MimeTypes.AUDIO_E_AC3_JOC;
            }
        }
        return MimeTypes.AUDIO_E_AC3;
    }

    protected static float parseFrameRate(XmlPullParser xpp, float defaultValue) {
        float frameRate = defaultValue;
        String frameRateAttribute = xpp.getAttributeValue(null, "frameRate");
        if (frameRateAttribute == null) {
            return frameRate;
        }
        Matcher frameRateMatcher = FRAME_RATE_PATTERN.matcher(frameRateAttribute);
        if (!frameRateMatcher.matches()) {
            return frameRate;
        }
        int numerator = Integer.parseInt(frameRateMatcher.group(1));
        String denominatorString = frameRateMatcher.group(2);
        if (TextUtils.isEmpty(denominatorString)) {
            return (float) numerator;
        }
        return ((float) numerator) / ((float) Integer.parseInt(denominatorString));
    }

    protected static long parseDuration(XmlPullParser xpp, String name, long defaultValue) {
        String value = xpp.getAttributeValue(null, name);
        if (value == null) {
            return defaultValue;
        }
        return Util.parseXsDuration(value);
    }

    protected static long parseDateTime(XmlPullParser xpp, String name, long defaultValue) throws ParserException {
        String value = xpp.getAttributeValue(null, name);
        if (value == null) {
            return defaultValue;
        }
        return Util.parseXsDateTime(value);
    }

    protected static String parseBaseUrl(XmlPullParser xpp, String parentBaseUrl) throws XmlPullParserException, IOException {
        xpp.next();
        return UriUtil.resolve(parentBaseUrl, xpp.getText());
    }

    protected static int parseInt(XmlPullParser xpp, String name, int defaultValue) {
        String value = xpp.getAttributeValue(null, name);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    protected static long parseLong(XmlPullParser xpp, String name, long defaultValue) {
        String value = xpp.getAttributeValue(null, name);
        return value == null ? defaultValue : Long.parseLong(value);
    }

    protected static String parseString(XmlPullParser xpp, String name, String defaultValue) {
        String value = xpp.getAttributeValue(null, name);
        return value == null ? defaultValue : value;
    }

    protected static int parseDolbyChannelConfiguration(XmlPullParser xpp) {
        String value = Util.toLowerInvariant(xpp.getAttributeValue(null, "value"));
        if (value == null) {
            return -1;
        }
        int hashCode = value.hashCode();
        if (hashCode != 1596796) {
            if (hashCode != 2937391) {
                if (hashCode != 3094035) {
                    if (hashCode == 3133436) {
                        if (value.equals("fa01")) {
                            hashCode = 3;
                            switch (hashCode) {
                                case 0:
                                    return 1;
                                case 1:
                                    return 2;
                                case 2:
                                    return 6;
                                case 3:
                                    return 8;
                                default:
                                    return -1;
                            }
                        }
                    }
                } else if (value.equals("f801")) {
                    hashCode = 2;
                    switch (hashCode) {
                        case 0:
                            return 1;
                        case 1:
                            return 2;
                        case 2:
                            return 6;
                        case 3:
                            return 8;
                        default:
                            return -1;
                    }
                }
            } else if (value.equals("a000")) {
                hashCode = 1;
                switch (hashCode) {
                    case 0:
                        return 1;
                    case 1:
                        return 2;
                    case 2:
                        return 6;
                    case 3:
                        return 8;
                    default:
                        return -1;
                }
            }
        } else if (value.equals("4000")) {
            hashCode = 0;
            switch (hashCode) {
                case 0:
                    return 1;
                case 1:
                    return 2;
                case 2:
                    return 6;
                case 3:
                    return 8;
                default:
                    return -1;
            }
        }
        hashCode = -1;
        switch (hashCode) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 6;
            case 3:
                return 8;
            default:
                return -1;
        }
    }
}
