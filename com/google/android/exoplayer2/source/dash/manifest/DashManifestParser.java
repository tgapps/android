package com.google.android.exoplayer2.source.dash.manifest;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.extractor.mp4.PsshAtomUtil;
import com.google.android.exoplayer2.metadata.emsg.EventMessage;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentList;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentTemplate;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SegmentTimelineElement;
import com.google.android.exoplayer2.source.dash.manifest.SegmentBase.SingleSegmentBase;
import com.google.android.exoplayer2.upstream.ParsingLoadable.Parser;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.UriUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.util.XmlPullParserUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            if (xpp.next() == 2 && "MPD".equals(xpp.getName())) {
                return parseMediaPresentationDescription(xpp, uri.toString());
            }
            throw new ParserException("inputStream does not contain a valid media presentation description");
        } catch (Throwable e) {
            throw new ParserException(e);
        }
    }

    protected DashManifest parseMediaPresentationDescription(XmlPullParser xpp, String baseUrl) throws XmlPullParserException, IOException {
        long availabilityStartTime = parseDateTime(xpp, "availabilityStartTime", C.TIME_UNSET);
        long durationMs = parseDuration(xpp, "mediaPresentationDuration", C.TIME_UNSET);
        long minBufferTimeMs = parseDuration(xpp, "minBufferTime", C.TIME_UNSET);
        String typeString = xpp.getAttributeValue(null, "type");
        boolean dynamic = typeString != null && "dynamic".equals(typeString);
        long minUpdateTimeMs = dynamic ? parseDuration(xpp, "minimumUpdatePeriod", C.TIME_UNSET) : C.TIME_UNSET;
        long timeShiftBufferDepthMs = dynamic ? parseDuration(xpp, "timeShiftBufferDepth", C.TIME_UNSET) : C.TIME_UNSET;
        long suggestedPresentationDelayMs = dynamic ? parseDuration(xpp, "suggestedPresentationDelay", C.TIME_UNSET) : C.TIME_UNSET;
        long publishTimeMs = parseDateTime(xpp, "publishTime", C.TIME_UNSET);
        UtcTimingElement utcTiming = null;
        Uri location = null;
        List<Period> periods = new ArrayList();
        long nextPeriodStartMs = dynamic ? C.TIME_UNSET : 0;
        boolean seenEarlyAccessPeriod = false;
        boolean seenFirstBaseUrl = false;
        do {
            xpp.next();
            if (!XmlPullParserUtil.isStartTag(xpp, "BaseURL")) {
                if (XmlPullParserUtil.isStartTag(xpp, "UTCTiming")) {
                    utcTiming = parseUtcTiming(xpp);
                } else {
                    if (XmlPullParserUtil.isStartTag(xpp, "Location")) {
                        location = Uri.parse(xpp.nextText());
                    } else {
                        if (XmlPullParserUtil.isStartTag(xpp, "Period") && !seenEarlyAccessPeriod) {
                            Pair<Period, Long> periodWithDurationMs = parsePeriod(xpp, baseUrl, nextPeriodStartMs);
                            Period period = periodWithDurationMs.first;
                            if (period.startMs != C.TIME_UNSET) {
                                long periodDurationMs = ((Long) periodWithDurationMs.second).longValue();
                                if (periodDurationMs == C.TIME_UNSET) {
                                    nextPeriodStartMs = C.TIME_UNSET;
                                } else {
                                    nextPeriodStartMs = period.startMs + periodDurationMs;
                                }
                                periods.add(period);
                            } else if (dynamic) {
                                seenEarlyAccessPeriod = true;
                            } else {
                                throw new ParserException("Unable to determine start of period " + periods.size());
                            }
                        }
                    }
                }
            } else if (!seenFirstBaseUrl) {
                baseUrl = parseBaseUrl(xpp, baseUrl);
                seenFirstBaseUrl = true;
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "MPD"));
        if (durationMs == C.TIME_UNSET) {
            if (nextPeriodStartMs != C.TIME_UNSET) {
                durationMs = nextPeriodStartMs;
            } else if (!dynamic) {
                throw new ParserException("Unable to determine duration of static manifest.");
            }
        }
        if (!periods.isEmpty()) {
            return buildMediaPresentationDescription(availabilityStartTime, durationMs, minBufferTimeMs, dynamic, minUpdateTimeMs, timeShiftBufferDepthMs, suggestedPresentationDelayMs, publishTimeMs, utcTiming, location, periods);
        }
        throw new ParserException("No periods found.");
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
        String id = xpp.getAttributeValue(null, TtmlNode.ATTR_ID);
        long startMs = parseDuration(xpp, TtmlNode.START, defaultStartMs);
        long durationMs = parseDuration(xpp, "duration", C.TIME_UNSET);
        SegmentBase segmentBase = null;
        List<AdaptationSet> adaptationSets = new ArrayList();
        List<EventStream> eventStreams = new ArrayList();
        boolean seenFirstBaseUrl = false;
        do {
            xpp.next();
            if (!XmlPullParserUtil.isStartTag(xpp, "BaseURL")) {
                if (XmlPullParserUtil.isStartTag(xpp, "AdaptationSet")) {
                    adaptationSets.add(parseAdaptationSet(xpp, baseUrl, segmentBase));
                } else {
                    if (XmlPullParserUtil.isStartTag(xpp, "EventStream")) {
                        eventStreams.add(parseEventStream(xpp));
                    } else {
                        if (XmlPullParserUtil.isStartTag(xpp, "SegmentBase")) {
                            segmentBase = parseSegmentBase(xpp, null);
                        } else {
                            if (XmlPullParserUtil.isStartTag(xpp, "SegmentList")) {
                                segmentBase = parseSegmentList(xpp, null);
                            } else {
                                if (XmlPullParserUtil.isStartTag(xpp, "SegmentTemplate")) {
                                    segmentBase = parseSegmentTemplate(xpp, null);
                                }
                            }
                        }
                    }
                }
            } else if (!seenFirstBaseUrl) {
                baseUrl = parseBaseUrl(xpp, baseUrl);
                seenFirstBaseUrl = true;
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "Period"));
        return Pair.create(buildPeriod(id, startMs, adaptationSets, eventStreams), Long.valueOf(durationMs));
    }

    protected Period buildPeriod(String id, long startMs, List<AdaptationSet> adaptationSets, List<EventStream> eventStreams) {
        return new Period(id, startMs, adaptationSets, eventStreams);
    }

    protected AdaptationSet parseAdaptationSet(XmlPullParser xpp, String baseUrl, SegmentBase segmentBase) throws XmlPullParserException, IOException {
        int id = parseInt(xpp, TtmlNode.ATTR_ID, -1);
        int contentType = parseContentType(xpp);
        String mimeType = xpp.getAttributeValue(null, "mimeType");
        String codecs = xpp.getAttributeValue(null, "codecs");
        int width = parseInt(xpp, "width", -1);
        int height = parseInt(xpp, "height", -1);
        float frameRate = parseFrameRate(xpp, -1.0f);
        int audioChannels = -1;
        int audioSamplingRate = parseInt(xpp, "audioSamplingRate", -1);
        String language = xpp.getAttributeValue(null, "lang");
        String label = xpp.getAttributeValue(null, "label");
        String drmSchemeType = null;
        ArrayList<SchemeData> drmSchemeDatas = new ArrayList();
        ArrayList<Descriptor> inbandEventStreams = new ArrayList();
        ArrayList<Descriptor> accessibilityDescriptors = new ArrayList();
        ArrayList<Descriptor> supplementalProperties = new ArrayList();
        List<RepresentationInfo> representationInfos = new ArrayList();
        int selectionFlags = 0;
        boolean seenFirstBaseUrl = false;
        do {
            xpp.next();
            if (!XmlPullParserUtil.isStartTag(xpp, "BaseURL")) {
                if (XmlPullParserUtil.isStartTag(xpp, "ContentProtection")) {
                    Pair<String, SchemeData> contentProtection = parseContentProtection(xpp);
                    if (contentProtection.first != null) {
                        drmSchemeType = contentProtection.first;
                    }
                    if (contentProtection.second != null) {
                        drmSchemeDatas.add(contentProtection.second);
                    }
                } else {
                    if (XmlPullParserUtil.isStartTag(xpp, "ContentComponent")) {
                        language = checkLanguageConsistency(language, xpp.getAttributeValue(null, "lang"));
                        contentType = checkContentTypeConsistency(contentType, parseContentType(xpp));
                    } else {
                        if (XmlPullParserUtil.isStartTag(xpp, "Role")) {
                            selectionFlags |= parseRole(xpp);
                        } else {
                            if (XmlPullParserUtil.isStartTag(xpp, "AudioChannelConfiguration")) {
                                audioChannels = parseAudioChannelConfiguration(xpp);
                            } else {
                                if (XmlPullParserUtil.isStartTag(xpp, "Accessibility")) {
                                    accessibilityDescriptors.add(parseDescriptor(xpp, "Accessibility"));
                                } else {
                                    if (XmlPullParserUtil.isStartTag(xpp, "SupplementalProperty")) {
                                        supplementalProperties.add(parseDescriptor(xpp, "SupplementalProperty"));
                                    } else {
                                        if (XmlPullParserUtil.isStartTag(xpp, "Representation")) {
                                            RepresentationInfo representationInfo = parseRepresentation(xpp, baseUrl, label, mimeType, codecs, width, height, frameRate, audioChannels, audioSamplingRate, language, selectionFlags, accessibilityDescriptors, segmentBase);
                                            contentType = checkContentTypeConsistency(contentType, getContentType(representationInfo.format));
                                            representationInfos.add(representationInfo);
                                        } else {
                                            if (XmlPullParserUtil.isStartTag(xpp, "SegmentBase")) {
                                                segmentBase = parseSegmentBase(xpp, (SingleSegmentBase) segmentBase);
                                            } else {
                                                if (XmlPullParserUtil.isStartTag(xpp, "SegmentList")) {
                                                    segmentBase = parseSegmentList(xpp, (SegmentList) segmentBase);
                                                } else {
                                                    if (XmlPullParserUtil.isStartTag(xpp, "SegmentTemplate")) {
                                                        segmentBase = parseSegmentTemplate(xpp, (SegmentTemplate) segmentBase);
                                                    } else {
                                                        if (XmlPullParserUtil.isStartTag(xpp, "InbandEventStream")) {
                                                            inbandEventStreams.add(parseDescriptor(xpp, "InbandEventStream"));
                                                        } else if (XmlPullParserUtil.isStartTag(xpp)) {
                                                            parseAdaptationSetChild(xpp);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (!seenFirstBaseUrl) {
                baseUrl = parseBaseUrl(xpp, baseUrl);
                seenFirstBaseUrl = true;
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "AdaptationSet"));
        List<Representation> arrayList = new ArrayList(representationInfos.size());
        for (int i = 0; i < representationInfos.size(); i++) {
            arrayList.add(buildRepresentation((RepresentationInfo) representationInfos.get(i), this.contentId, drmSchemeType, drmSchemeDatas, inbandEventStreams));
        }
        return buildAdaptationSet(id, contentType, arrayList, accessibilityDescriptors, supplementalProperties);
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
        if ("text".equals(contentType)) {
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
        String schemeType = null;
        String licenseServerUrl = null;
        byte[] data = null;
        UUID uuid = null;
        boolean requiresSecureDecoder = false;
        String schemeIdUri = xpp.getAttributeValue(null, "schemeIdUri");
        if (schemeIdUri != null) {
            String toLowerInvariant = Util.toLowerInvariant(schemeIdUri);
            Object obj = -1;
            switch (toLowerInvariant.hashCode()) {
                case 489446379:
                    if (toLowerInvariant.equals("urn:uuid:9a04f079-9840-4286-ab92-e65be0885f95")) {
                        obj = 1;
                        break;
                    }
                    break;
                case 755418770:
                    if (toLowerInvariant.equals("urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed")) {
                        obj = 2;
                        break;
                    }
                    break;
                case 1812765994:
                    if (toLowerInvariant.equals("urn:mpeg:dash:mp4protection:2011")) {
                        obj = null;
                        break;
                    }
                    break;
            }
            switch (obj) {
                case null:
                    schemeType = xpp.getAttributeValue(null, "value");
                    String defaultKid = xpp.getAttributeValue(null, "cenc:default_KID");
                    if (!(TextUtils.isEmpty(defaultKid) || "00000000-0000-0000-0000-000000000000".equals(defaultKid))) {
                        String[] defaultKidStrings = defaultKid.split("\\s+");
                        UUID[] defaultKids = new UUID[defaultKidStrings.length];
                        for (int i = 0; i < defaultKidStrings.length; i++) {
                            defaultKids[i] = UUID.fromString(defaultKidStrings[i]);
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
            }
        }
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xpp, "ms:laurl")) {
                licenseServerUrl = xpp.getAttributeValue(null, "licenseUrl");
            } else {
                if (XmlPullParserUtil.isStartTag(xpp, "widevine:license")) {
                    String robustnessLevel = xpp.getAttributeValue(null, "robustness_level");
                    requiresSecureDecoder = robustnessLevel != null && robustnessLevel.startsWith("HW");
                } else if (data == null) {
                    if (XmlPullParserUtil.isStartTag(xpp, "cenc:pssh") && xpp.next() == 4) {
                        data = Base64.decode(xpp.getText(), 0);
                        uuid = PsshAtomUtil.parseUuid(data);
                        if (uuid == null) {
                            Log.w(TAG, "Skipping malformed cenc:pssh data");
                            data = null;
                        }
                    } else if (C.PLAYREADY_UUID.equals(uuid)) {
                        if (XmlPullParserUtil.isStartTag(xpp, "mspr:pro") && xpp.next() == 4) {
                            data = PsshAtomUtil.buildPsshAtom(C.PLAYREADY_UUID, Base64.decode(xpp.getText(), 0));
                        }
                    }
                }
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "ContentProtection"));
        return Pair.create(schemeType, uuid != null ? new SchemeData(uuid, licenseServerUrl, MimeTypes.VIDEO_MP4, data, requiresSecureDecoder) : null);
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

    protected RepresentationInfo parseRepresentation(XmlPullParser xpp, String baseUrl, String label, String adaptationSetMimeType, String adaptationSetCodecs, int adaptationSetWidth, int adaptationSetHeight, float adaptationSetFrameRate, int adaptationSetAudioChannels, int adaptationSetAudioSamplingRate, String adaptationSetLanguage, int adaptationSetSelectionFlags, List<Descriptor> adaptationSetAccessibilityDescriptors, SegmentBase segmentBase) throws XmlPullParserException, IOException {
        String id = xpp.getAttributeValue(null, TtmlNode.ATTR_ID);
        int bandwidth = parseInt(xpp, "bandwidth", -1);
        String mimeType = parseString(xpp, "mimeType", adaptationSetMimeType);
        String codecs = parseString(xpp, "codecs", adaptationSetCodecs);
        int width = parseInt(xpp, "width", adaptationSetWidth);
        int height = parseInt(xpp, "height", adaptationSetHeight);
        float frameRate = parseFrameRate(xpp, adaptationSetFrameRate);
        int audioChannels = adaptationSetAudioChannels;
        int audioSamplingRate = parseInt(xpp, "audioSamplingRate", adaptationSetAudioSamplingRate);
        String drmSchemeType = null;
        ArrayList<SchemeData> drmSchemeDatas = new ArrayList();
        ArrayList<Descriptor> inbandEventStreams = new ArrayList();
        ArrayList<Descriptor> supplementalProperties = new ArrayList();
        boolean seenFirstBaseUrl = false;
        do {
            xpp.next();
            if (!XmlPullParserUtil.isStartTag(xpp, "BaseURL")) {
                if (XmlPullParserUtil.isStartTag(xpp, "AudioChannelConfiguration")) {
                    audioChannels = parseAudioChannelConfiguration(xpp);
                } else {
                    if (XmlPullParserUtil.isStartTag(xpp, "SegmentBase")) {
                        segmentBase = parseSegmentBase(xpp, (SingleSegmentBase) segmentBase);
                    } else {
                        if (XmlPullParserUtil.isStartTag(xpp, "SegmentList")) {
                            segmentBase = parseSegmentList(xpp, (SegmentList) segmentBase);
                        } else {
                            if (XmlPullParserUtil.isStartTag(xpp, "SegmentTemplate")) {
                                segmentBase = parseSegmentTemplate(xpp, (SegmentTemplate) segmentBase);
                            } else {
                                if (XmlPullParserUtil.isStartTag(xpp, "ContentProtection")) {
                                    Pair<String, SchemeData> contentProtection = parseContentProtection(xpp);
                                    if (contentProtection.first != null) {
                                        drmSchemeType = contentProtection.first;
                                    }
                                    if (contentProtection.second != null) {
                                        drmSchemeDatas.add(contentProtection.second);
                                    }
                                } else {
                                    if (XmlPullParserUtil.isStartTag(xpp, "InbandEventStream")) {
                                        inbandEventStreams.add(parseDescriptor(xpp, "InbandEventStream"));
                                    } else {
                                        if (XmlPullParserUtil.isStartTag(xpp, "SupplementalProperty")) {
                                            supplementalProperties.add(parseDescriptor(xpp, "SupplementalProperty"));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (!seenFirstBaseUrl) {
                baseUrl = parseBaseUrl(xpp, baseUrl);
                seenFirstBaseUrl = true;
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "Representation"));
        Format format = buildFormat(id, label, mimeType, width, height, frameRate, audioChannels, audioSamplingRate, bandwidth, adaptationSetLanguage, adaptationSetSelectionFlags, adaptationSetAccessibilityDescriptors, codecs, supplementalProperties);
        if (segmentBase == null) {
            segmentBase = new SingleSegmentBase();
        }
        return new RepresentationInfo(format, baseUrl, segmentBase, drmSchemeType, drmSchemeDatas, inbandEventStreams, -1);
    }

    protected Format buildFormat(String id, String label, String containerMimeType, int width, int height, float frameRate, int audioChannels, int audioSamplingRate, int bitrate, String language, int selectionFlags, List<Descriptor> accessibilityDescriptors, String codecs, List<Descriptor> supplementalProperties) {
        String sampleMimeType = getSampleMimeType(containerMimeType, codecs);
        if (sampleMimeType != null) {
            if (MimeTypes.AUDIO_E_AC3.equals(sampleMimeType)) {
                sampleMimeType = parseEac3SupplementalProperties(supplementalProperties);
            }
            if (MimeTypes.isVideo(sampleMimeType)) {
                return Format.createVideoContainerFormat(id, label, containerMimeType, sampleMimeType, codecs, bitrate, width, height, frameRate, null, selectionFlags);
            }
            if (MimeTypes.isAudio(sampleMimeType)) {
                return Format.createAudioContainerFormat(id, label, containerMimeType, sampleMimeType, codecs, bitrate, audioChannels, audioSamplingRate, null, selectionFlags, language);
            }
            if (mimeTypeIsRawText(sampleMimeType)) {
                int accessibilityChannel;
                if (MimeTypes.APPLICATION_CEA608.equals(sampleMimeType)) {
                    accessibilityChannel = parseCea608AccessibilityChannel(accessibilityDescriptors);
                } else if (MimeTypes.APPLICATION_CEA708.equals(sampleMimeType)) {
                    accessibilityChannel = parseCea708AccessibilityChannel(accessibilityDescriptors);
                } else {
                    accessibilityChannel = -1;
                }
                return Format.createTextContainerFormat(id, label, containerMimeType, sampleMimeType, codecs, bitrate, selectionFlags, language, accessibilityChannel);
            }
        }
        return Format.createContainerFormat(id, label, containerMimeType, sampleMimeType, codecs, bitrate, selectionFlags, language);
    }

    protected Representation buildRepresentation(RepresentationInfo representationInfo, String contentId, String extraDrmSchemeType, ArrayList<SchemeData> extraDrmSchemeDatas, ArrayList<Descriptor> extraInbandEventStreams) {
        String drmSchemeType;
        Format format = representationInfo.format;
        if (representationInfo.drmSchemeType != null) {
            drmSchemeType = representationInfo.drmSchemeType;
        } else {
            drmSchemeType = extraDrmSchemeType;
        }
        List drmSchemeDatas = representationInfo.drmSchemeDatas;
        drmSchemeDatas.addAll(extraDrmSchemeDatas);
        if (!drmSchemeDatas.isEmpty()) {
            filterRedundantIncompleteSchemeDatas(drmSchemeDatas);
            format = format.copyWithDrmInitData(new DrmInitData(drmSchemeType, drmSchemeDatas));
        }
        ArrayList<Descriptor> inbandEventStreams = representationInfo.inbandEventStreams;
        inbandEventStreams.addAll(extraInbandEventStreams);
        return Representation.newInstance(contentId, representationInfo.revisionId, format, representationInfo.baseUrl, representationInfo.segmentBase, inbandEventStreams);
    }

    protected SingleSegmentBase parseSegmentBase(XmlPullParser xpp, SingleSegmentBase parent) throws XmlPullParserException, IOException {
        long timescale = parseLong(xpp, "timescale", parent != null ? parent.timescale : 1);
        long presentationTimeOffset = parseLong(xpp, "presentationTimeOffset", parent != null ? parent.presentationTimeOffset : 0);
        long indexStart = parent != null ? parent.indexStart : 0;
        long indexLength = parent != null ? parent.indexLength : 0;
        String indexRangeText = xpp.getAttributeValue(null, "indexRange");
        if (indexRangeText != null) {
            String[] indexRange = indexRangeText.split("-");
            indexStart = Long.parseLong(indexRange[0]);
            indexLength = (Long.parseLong(indexRange[1]) - indexStart) + 1;
        }
        RangedUri initialization = parent != null ? parent.initialization : null;
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xpp, "Initialization")) {
                initialization = parseInitialization(xpp);
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "SegmentBase"));
        return buildSingleSegmentBase(initialization, timescale, presentationTimeOffset, indexStart, indexLength);
    }

    protected SingleSegmentBase buildSingleSegmentBase(RangedUri initialization, long timescale, long presentationTimeOffset, long indexStart, long indexLength) {
        return new SingleSegmentBase(initialization, timescale, presentationTimeOffset, indexStart, indexLength);
    }

    protected SegmentList parseSegmentList(XmlPullParser xpp, SegmentList parent) throws XmlPullParserException, IOException {
        long timescale = parseLong(xpp, "timescale", parent != null ? parent.timescale : 1);
        long presentationTimeOffset = parseLong(xpp, "presentationTimeOffset", parent != null ? parent.presentationTimeOffset : 0);
        long duration = parseLong(xpp, "duration", parent != null ? parent.duration : C.TIME_UNSET);
        long startNumber = parseLong(xpp, "startNumber", parent != null ? parent.startNumber : 1);
        RangedUri initialization = null;
        List<SegmentTimelineElement> timeline = null;
        List<RangedUri> segments = null;
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xpp, "Initialization")) {
                initialization = parseInitialization(xpp);
            } else {
                if (XmlPullParserUtil.isStartTag(xpp, "SegmentTimeline")) {
                    timeline = parseSegmentTimeline(xpp);
                } else {
                    if (XmlPullParserUtil.isStartTag(xpp, "SegmentURL")) {
                        if (segments == null) {
                            segments = new ArrayList();
                        }
                        segments.add(parseSegmentUrl(xpp));
                    }
                }
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "SegmentList"));
        if (parent != null) {
            if (initialization == null) {
                initialization = parent.initialization;
            }
            if (timeline == null) {
                timeline = parent.segmentTimeline;
            }
            if (segments == null) {
                segments = parent.mediaSegments;
            }
        }
        return buildSegmentList(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, segments);
    }

    protected SegmentList buildSegmentList(RangedUri initialization, long timescale, long presentationTimeOffset, long startNumber, long duration, List<SegmentTimelineElement> timeline, List<RangedUri> segments) {
        return new SegmentList(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, segments);
    }

    protected SegmentTemplate parseSegmentTemplate(XmlPullParser xpp, SegmentTemplate parent) throws XmlPullParserException, IOException {
        long timescale = parseLong(xpp, "timescale", parent != null ? parent.timescale : 1);
        long presentationTimeOffset = parseLong(xpp, "presentationTimeOffset", parent != null ? parent.presentationTimeOffset : 0);
        long duration = parseLong(xpp, "duration", parent != null ? parent.duration : C.TIME_UNSET);
        long startNumber = parseLong(xpp, "startNumber", parent != null ? parent.startNumber : 1);
        UrlTemplate mediaTemplate = parseUrlTemplate(xpp, "media", parent != null ? parent.mediaTemplate : null);
        UrlTemplate initializationTemplate = parseUrlTemplate(xpp, "initialization", parent != null ? parent.initializationTemplate : null);
        RangedUri initialization = null;
        List<SegmentTimelineElement> timeline = null;
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xpp, "Initialization")) {
                initialization = parseInitialization(xpp);
            } else {
                if (XmlPullParserUtil.isStartTag(xpp, "SegmentTimeline")) {
                    timeline = parseSegmentTimeline(xpp);
                }
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "SegmentTemplate"));
        if (parent != null) {
            if (initialization == null) {
                initialization = parent.initialization;
            }
            if (timeline == null) {
                timeline = parent.segmentTimeline;
            }
        }
        return buildSegmentTemplate(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, initializationTemplate, mediaTemplate);
    }

    protected SegmentTemplate buildSegmentTemplate(RangedUri initialization, long timescale, long presentationTimeOffset, long startNumber, long duration, List<SegmentTimelineElement> timeline, UrlTemplate initializationTemplate, UrlTemplate mediaTemplate) {
        return new SegmentTemplate(initialization, timescale, presentationTimeOffset, startNumber, duration, timeline, initializationTemplate, mediaTemplate);
    }

    protected EventStream parseEventStream(XmlPullParser xpp) throws XmlPullParserException, IOException {
        String schemeIdUri = parseString(xpp, "schemeIdUri", TtmlNode.ANONYMOUS_REGION_ID);
        String value = parseString(xpp, "value", TtmlNode.ANONYMOUS_REGION_ID);
        long timescale = parseLong(xpp, "timescale", 1);
        List<EventMessage> eventMessages = new ArrayList();
        ByteArrayOutputStream scratchOutputStream = new ByteArrayOutputStream(512);
        do {
            xpp.next();
            if (XmlPullParserUtil.isStartTag(xpp, "Event")) {
                eventMessages.add(parseEvent(xpp, schemeIdUri, value, timescale, scratchOutputStream));
            }
        } while (!XmlPullParserUtil.isEndTag(xpp, "EventStream"));
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
        long id = parseLong(xpp, TtmlNode.ATTR_ID, 0);
        long duration = parseLong(xpp, "duration", C.TIME_UNSET);
        long presentationTime = parseLong(xpp, "presentationTime", 0);
        return buildEvent(schemeIdUri, value, id, Util.scaleLargeTimestamp(duration, 1000, timescale), parseEventObject(xpp, scratchOutputStream), Util.scaleLargeTimestamp(presentationTime, 1000000, timescale));
    }

    protected byte[] parseEventObject(XmlPullParser xpp, ByteArrayOutputStream scratchOutputStream) throws XmlPullParserException, IOException {
        scratchOutputStream.reset();
        XmlSerializer xmlSerializer = Xml.newSerializer();
        xmlSerializer.setOutput(scratchOutputStream, null);
        xpp.nextToken();
        while (!XmlPullParserUtil.isEndTag(xpp, "Event")) {
            switch (xpp.getEventType()) {
                case 0:
                    xmlSerializer.startDocument(null, Boolean.valueOf(false));
                    break;
                case 1:
                    xmlSerializer.endDocument();
                    break;
                case 2:
                    xmlSerializer.startTag(xpp.getNamespace(), xpp.getName());
                    for (int i = 0; i < xpp.getAttributeCount(); i++) {
                        xmlSerializer.attribute(xpp.getAttributeNamespace(i), xpp.getAttributeName(i), xpp.getAttributeValue(i));
                    }
                    break;
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
                int count = parseInt(xpp, "r", 0) + 1;
                for (int i = 0; i < count; i++) {
                    segmentTimeline.add(buildSegmentTimelineElement(elapsedTime, duration));
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
        int audioChannels = -1;
        String schemeIdUri = parseString(xpp, "schemeIdUri", null);
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
        if (MimeTypes.APPLICATION_MP4.equals(containerMimeType)) {
            if ("stpp".equals(codecs)) {
                return MimeTypes.APPLICATION_TTML;
            }
            if ("wvtt".equals(codecs)) {
                return MimeTypes.APPLICATION_MP4VTT;
            }
        } else if (MimeTypes.APPLICATION_RAWCC.equals(containerMimeType)) {
            if (codecs != null) {
                if (codecs.contains("cea708")) {
                    return MimeTypes.APPLICATION_CEA708;
                }
                if (codecs.contains("eia608") || codecs.contains("cea608")) {
                    return MimeTypes.APPLICATION_CEA608;
                }
            }
            return null;
        }
        return null;
    }

    private static boolean mimeTypeIsRawText(String mimeType) {
        return MimeTypes.isText(mimeType) || MimeTypes.APPLICATION_TTML.equals(mimeType) || MimeTypes.APPLICATION_MP4VTT.equals(mimeType) || MimeTypes.APPLICATION_CEA708.equals(mimeType) || MimeTypes.APPLICATION_CEA608.equals(mimeType);
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
                Log.w(TAG, "Unable to parse CEA-608 channel number from: " + descriptor.value);
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
                Log.w(TAG, "Unable to parse CEA-708 service block number from: " + descriptor.value);
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
        return value == null ? defaultValue : Util.parseXsDuration(value);
    }

    protected static long parseDateTime(XmlPullParser xpp, String name, long defaultValue) throws ParserException {
        String value = xpp.getAttributeValue(null, name);
        return value == null ? defaultValue : Util.parseXsDateTime(value);
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected static int parseDolbyChannelConfiguration(org.xmlpull.v1.XmlPullParser r6) {
        /*
        r3 = 2;
        r2 = 1;
        r1 = -1;
        r4 = 0;
        r5 = "value";
        r4 = r6.getAttributeValue(r4, r5);
        r0 = com.google.android.exoplayer2.util.Util.toLowerInvariant(r4);
        if (r0 != 0) goto L_0x0012;
    L_0x0011:
        return r1;
    L_0x0012:
        r4 = r0.hashCode();
        switch(r4) {
            case 1596796: goto L_0x0020;
            case 2937391: goto L_0x002b;
            case 3094035: goto L_0x0036;
            case 3133436: goto L_0x0041;
            default: goto L_0x0019;
        };
    L_0x0019:
        r4 = r1;
    L_0x001a:
        switch(r4) {
            case 0: goto L_0x001e;
            case 1: goto L_0x004c;
            case 2: goto L_0x004e;
            case 3: goto L_0x0050;
            default: goto L_0x001d;
        };
    L_0x001d:
        goto L_0x0011;
    L_0x001e:
        r1 = r2;
        goto L_0x0011;
    L_0x0020:
        r4 = "4000";
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0019;
    L_0x0029:
        r4 = 0;
        goto L_0x001a;
    L_0x002b:
        r4 = "a000";
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0019;
    L_0x0034:
        r4 = r2;
        goto L_0x001a;
    L_0x0036:
        r4 = "f801";
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0019;
    L_0x003f:
        r4 = r3;
        goto L_0x001a;
    L_0x0041:
        r4 = "fa01";
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0019;
    L_0x004a:
        r4 = 3;
        goto L_0x001a;
    L_0x004c:
        r1 = r3;
        goto L_0x0011;
    L_0x004e:
        r1 = 6;
        goto L_0x0011;
    L_0x0050:
        r1 = 8;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.source.dash.manifest.DashManifestParser.parseDolbyChannelConfiguration(org.xmlpull.v1.XmlPullParser):int");
    }
}
