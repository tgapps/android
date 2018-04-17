package org.telegram.messenger.exoplayer2.source.hls.playlist;

import android.net.Uri;
import android.util.Base64;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ParserException;
import org.telegram.messenger.exoplayer2.drm.DrmInitData;
import org.telegram.messenger.exoplayer2.drm.DrmInitData.SchemeData;
import org.telegram.messenger.exoplayer2.source.UnrecognizedInputFormatException;
import org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMediaPlaylist.Segment;
import org.telegram.messenger.exoplayer2.upstream.ParsingLoadable.Parser;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.exoplayer2.util.Util;

public final class HlsPlaylistParser implements Parser<HlsPlaylist> {
    private static final String ATTR_CLOSED_CAPTIONS_NONE = "CLOSED-CAPTIONS=NONE";
    private static final String BOOLEAN_FALSE = "NO";
    private static final String BOOLEAN_TRUE = "YES";
    private static final String KEYFORMAT_IDENTITY = "identity";
    private static final String KEYFORMAT_WIDEVINE_PSSH_BINARY = "urn:uuid:edef8ba9-79d6-4ace-a3c8-27dcd51d21ed";
    private static final String KEYFORMAT_WIDEVINE_PSSH_JSON = "com.widevine";
    private static final String METHOD_AES_128 = "AES-128";
    private static final String METHOD_NONE = "NONE";
    private static final String METHOD_SAMPLE_AES = "SAMPLE-AES";
    private static final String METHOD_SAMPLE_AES_CENC = "SAMPLE-AES-CENC";
    private static final String METHOD_SAMPLE_AES_CTR = "SAMPLE-AES-CTR";
    private static final String PLAYLIST_HEADER = "#EXTM3U";
    private static final Pattern REGEX_ATTR_BYTERANGE = Pattern.compile("BYTERANGE=\"(\\d+(?:@\\d+)?)\\b\"");
    private static final Pattern REGEX_AUDIO = Pattern.compile("AUDIO=\"(.+?)\"");
    private static final Pattern REGEX_AUTOSELECT = compileBooleanAttrPattern("AUTOSELECT");
    private static final Pattern REGEX_AVERAGE_BANDWIDTH = Pattern.compile("AVERAGE-BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_BANDWIDTH = Pattern.compile("[^-]BANDWIDTH=(\\d+)\\b");
    private static final Pattern REGEX_BYTERANGE = Pattern.compile("#EXT-X-BYTERANGE:(\\d+(?:@\\d+)?)\\b");
    private static final Pattern REGEX_CODECS = Pattern.compile("CODECS=\"(.+?)\"");
    private static final Pattern REGEX_DEFAULT = compileBooleanAttrPattern("DEFAULT");
    private static final Pattern REGEX_FORCED = compileBooleanAttrPattern("FORCED");
    private static final Pattern REGEX_FRAME_RATE = Pattern.compile("FRAME-RATE=([\\d\\.]+)\\b");
    private static final Pattern REGEX_GROUP_ID = Pattern.compile("GROUP-ID=\"(.+?)\"");
    private static final Pattern REGEX_INSTREAM_ID = Pattern.compile("INSTREAM-ID=\"((?:CC|SERVICE)\\d+)\"");
    private static final Pattern REGEX_IV = Pattern.compile("IV=([^,.*]+)");
    private static final Pattern REGEX_KEYFORMAT = Pattern.compile("KEYFORMAT=\"(.+?)\"");
    private static final Pattern REGEX_LANGUAGE = Pattern.compile("LANGUAGE=\"(.+?)\"");
    private static final Pattern REGEX_MEDIA_DURATION = Pattern.compile("#EXTINF:([\\d\\.]+)\\b");
    private static final Pattern REGEX_MEDIA_SEQUENCE = Pattern.compile("#EXT-X-MEDIA-SEQUENCE:(\\d+)\\b");
    private static final Pattern REGEX_METHOD = Pattern.compile("METHOD=(NONE|AES-128|SAMPLE-AES|SAMPLE-AES-CENC|SAMPLE-AES-CTR)");
    private static final Pattern REGEX_NAME = Pattern.compile("NAME=\"(.+?)\"");
    private static final Pattern REGEX_PLAYLIST_TYPE = Pattern.compile("#EXT-X-PLAYLIST-TYPE:(.+)\\b");
    private static final Pattern REGEX_RESOLUTION = Pattern.compile("RESOLUTION=(\\d+x\\d+)");
    private static final Pattern REGEX_TARGET_DURATION = Pattern.compile("#EXT-X-TARGETDURATION:(\\d+)\\b");
    private static final Pattern REGEX_TIME_OFFSET = Pattern.compile("TIME-OFFSET=(-?[\\d\\.]+)\\b");
    private static final Pattern REGEX_TYPE = Pattern.compile("TYPE=(AUDIO|VIDEO|SUBTITLES|CLOSED-CAPTIONS)");
    private static final Pattern REGEX_URI = Pattern.compile("URI=\"(.+?)\"");
    private static final Pattern REGEX_VERSION = Pattern.compile("#EXT-X-VERSION:(\\d+)\\b");
    private static final String TAG_BYTERANGE = "#EXT-X-BYTERANGE";
    private static final String TAG_DISCONTINUITY = "#EXT-X-DISCONTINUITY";
    private static final String TAG_DISCONTINUITY_SEQUENCE = "#EXT-X-DISCONTINUITY-SEQUENCE";
    private static final String TAG_ENDLIST = "#EXT-X-ENDLIST";
    private static final String TAG_INDEPENDENT_SEGMENTS = "#EXT-X-INDEPENDENT-SEGMENTS";
    private static final String TAG_INIT_SEGMENT = "#EXT-X-MAP";
    private static final String TAG_KEY = "#EXT-X-KEY";
    private static final String TAG_MEDIA = "#EXT-X-MEDIA";
    private static final String TAG_MEDIA_DURATION = "#EXTINF";
    private static final String TAG_MEDIA_SEQUENCE = "#EXT-X-MEDIA-SEQUENCE";
    private static final String TAG_PLAYLIST_TYPE = "#EXT-X-PLAYLIST-TYPE";
    private static final String TAG_PREFIX = "#EXT";
    private static final String TAG_PROGRAM_DATE_TIME = "#EXT-X-PROGRAM-DATE-TIME";
    private static final String TAG_START = "#EXT-X-START";
    private static final String TAG_STREAM_INF = "#EXT-X-STREAM-INF";
    private static final String TAG_TARGET_DURATION = "#EXT-X-TARGETDURATION";
    private static final String TAG_VERSION = "#EXT-X-VERSION";
    private static final String TYPE_AUDIO = "AUDIO";
    private static final String TYPE_CLOSED_CAPTIONS = "CLOSED-CAPTIONS";
    private static final String TYPE_SUBTITLES = "SUBTITLES";
    private static final String TYPE_VIDEO = "VIDEO";

    private static class LineIterator {
        private final Queue<String> extraLines;
        private String next;
        private final BufferedReader reader;

        public LineIterator(Queue<String> extraLines, BufferedReader reader) {
            this.extraLines = extraLines;
            this.reader = reader;
        }

        public boolean hasNext() throws IOException {
            if (this.next != null) {
                return true;
            }
            if (this.extraLines.isEmpty()) {
                do {
                    String readLine = this.reader.readLine();
                    this.next = readLine;
                    if (readLine == null) {
                        return false;
                    }
                    this.next = this.next.trim();
                } while (this.next.isEmpty());
                return true;
            }
            this.next = (String) this.extraLines.poll();
            return true;
        }

        public String next() throws IOException {
            if (!hasNext()) {
                return null;
            }
            String result = this.next;
            this.next = null;
            return result;
        }
    }

    private static org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMasterPlaylist parseMasterPlaylist(org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylistParser.LineIterator r1, java.lang.String r2) throws java.io.IOException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylistParser.parseMasterPlaylist(org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylistParser$LineIterator, java.lang.String):org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMasterPlaylist
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = new java.util.HashSet;
        r0.<init>();
        r1 = new java.util.HashMap;
        r1.<init>();
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = new java.util.ArrayList;
        r3.<init>();
        r11 = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r12 = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r13 = r3;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r14 = r3;
        r3 = 0;
        r4 = 0;
        r6 = 0;
        r15 = r6;
        r6 = r35.hasNext();
        if (r6 == 0) goto L_0x0127;
    L_0x0031:
        r6 = r35.next();
        r9 = "#EXT";
        r9 = r6.startsWith(r9);
        if (r9 == 0) goto L_0x0040;
    L_0x003d:
        r14.add(r6);
    L_0x0040:
        r9 = "#EXT-X-MEDIA";
        r9 = r6.startsWith(r9);
        if (r9 == 0) goto L_0x0055;
    L_0x0048:
        r13.add(r6);
        r31 = r0;
        r27 = r3;
        r28 = r4;
        r26 = 0;
        goto L_0x011e;
    L_0x0055:
        r9 = "#EXT-X-STREAM-INF";
        r9 = r6.startsWith(r9);
        if (r9 == 0) goto L_0x0116;
    L_0x005d:
        r9 = "CLOSED-CAPTIONS=NONE";
        r9 = r6.contains(r9);
        r9 = r9 | r15;
        r10 = REGEX_BANDWIDTH;
        r10 = parseIntAttr(r6, r10);
        r15 = REGEX_AVERAGE_BANDWIDTH;
        r15 = parseOptionalStringAttr(r6, r15);
        if (r15 == 0) goto L_0x0076;
    L_0x0072:
        r10 = java.lang.Integer.parseInt(r15);
    L_0x0076:
        r7 = REGEX_CODECS;
        r7 = parseOptionalStringAttr(r6, r7);
        r8 = REGEX_RESOLUTION;
        r8 = parseOptionalStringAttr(r6, r8);
        if (r8 == 0) goto L_0x00a8;
    L_0x0084:
        r5 = "x";
        r5 = r8.split(r5);
        r27 = r3;
        r26 = 0;
        r3 = r5[r26];
        r3 = java.lang.Integer.parseInt(r3);
        r28 = r4;
        r16 = 1;
        r4 = r5[r16];
        r4 = java.lang.Integer.parseInt(r4);
        if (r3 <= 0) goto L_0x00a2;
    L_0x00a0:
        if (r4 > 0) goto L_0x00a4;
    L_0x00a2:
        r3 = -1;
        r4 = -1;
        r22 = r4;
        goto L_0x00b1;
    L_0x00a8:
        r27 = r3;
        r28 = r4;
        r26 = 0;
        r3 = -1;
        r22 = -1;
        r4 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r5 = REGEX_FRAME_RATE;
        r5 = parseOptionalStringAttr(r6, r5);
        if (r5 == 0) goto L_0x00bf;
        r4 = java.lang.Float.parseFloat(r5);
        r29 = r5;
        r5 = REGEX_AUDIO;
        r5 = parseOptionalStringAttr(r6, r5);
        if (r5 == 0) goto L_0x00d6;
        if (r7 == 0) goto L_0x00d6;
        r30 = r6;
        r6 = 1;
        r6 = org.telegram.messenger.exoplayer2.util.Util.getCodecsOfType(r7, r6);
        r1.put(r5, r6);
        goto L_0x00d8;
        r30 = r6;
        r6 = r35.next();
        r16 = r0.add(r6);
        if (r16 == 0) goto L_0x010b;
        r31 = r0;
        r0 = r2.size();
        r16 = java.lang.Integer.toString(r0);
        r17 = "application/x-mpegURL";
        r18 = 0;
        r24 = 0;
        r25 = 0;
        r19 = r7;
        r20 = r10;
        r21 = r3;
        r23 = r4;
        r0 = org.telegram.messenger.exoplayer2.Format.createVideoContainerFormat(r16, r17, r18, r19, r20, r21, r22, r23, r24, r25);
        r32 = r3;
        r3 = new org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMasterPlaylist$HlsUrl;
        r3.<init>(r6, r0);
        r2.add(r3);
        goto L_0x010d;
        r31 = r0;
        r6 = r9;
        r3 = r27;
        r4 = r28;
        r0 = r31;
        goto L_0x002a;
    L_0x0116:
        r31 = r0;
        r27 = r3;
        r28 = r4;
        r26 = 0;
    L_0x011e:
        r6 = r15;
        r3 = r27;
        r4 = r28;
        r0 = r31;
        goto L_0x002a;
    L_0x0127:
        r31 = r0;
        r27 = r3;
        r28 = r4;
        r6 = 1;
        r26 = 0;
        r0 = r26;
        r3 = r13.size();
        if (r0 >= r3) goto L_0x0245;
        r3 = r13.get(r0);
        r3 = (java.lang.String) r3;
        r4 = parseSelectionFlags(r3);
        r5 = REGEX_URI;
        r5 = parseOptionalStringAttr(r3, r5);
        r7 = REGEX_NAME;
        r7 = parseStringAttr(r3, r7);
        r8 = REGEX_LANGUAGE;
        r8 = parseOptionalStringAttr(r3, r8);
        r9 = REGEX_GROUP_ID;
        r9 = parseOptionalStringAttr(r3, r9);
        r10 = REGEX_TYPE;
        r10 = parseStringAttr(r3, r10);
        r6 = r10.hashCode();
        r33 = r13;
        r13 = -959297733; // 0xffffffffc6d2473b float:-26915.615 double:NaN;
        r34 = r2;
        r2 = 2;
        if (r6 == r13) goto L_0x018e;
        r13 = -333210994; // 0xffffffffec239a8e float:-7.911391E26 double:NaN;
        if (r6 == r13) goto L_0x0184;
        r13 = 62628790; // 0x3bba3b6 float:1.1028458E-36 double:3.09427336E-316;
        if (r6 == r13) goto L_0x0179;
        goto L_0x0198;
        r6 = "AUDIO";
        r6 = r10.equals(r6);
        if (r6 == 0) goto L_0x0198;
        r6 = r26;
        goto L_0x0199;
        r6 = "CLOSED-CAPTIONS";
        r6 = r10.equals(r6);
        if (r6 == 0) goto L_0x0198;
        r6 = r2;
        goto L_0x0199;
        r6 = "SUBTITLES";
        r6 = r10.equals(r6);
        if (r6 == 0) goto L_0x0198;
        r6 = 1;
        goto L_0x0199;
        r6 = -1;
        switch(r6) {
            case 0: goto L_0x0207;
            case 1: goto L_0x01ec;
            case 2: goto L_0x019e;
            default: goto L_0x019c;
        };
        goto L_0x023c;
        r6 = REGEX_INSTREAM_ID;
        r6 = parseStringAttr(r3, r6);
        r10 = "CC";
        r10 = r6.startsWith(r10);
        if (r10 == 0) goto L_0x01ba;
        r10 = "application/cea-608";
        r2 = r6.substring(r2);
        r2 = java.lang.Integer.parseInt(r2);
        r23 = r2;
        r2 = r10;
        goto L_0x01c7;
        r2 = "application/cea-708";
        r10 = 7;
        r10 = r6.substring(r10);
        r10 = java.lang.Integer.parseInt(r10);
        r23 = r10;
        if (r28 != 0) goto L_0x01d1;
        r10 = new java.util.ArrayList;
        r10.<init>();
        r28 = r10;
        goto L_0x01d3;
        r10 = r28;
        r17 = 0;
        r19 = 0;
        r20 = -1;
        r16 = r7;
        r18 = r2;
        r21 = r4;
        r22 = r8;
        r13 = org.telegram.messenger.exoplayer2.Format.createTextContainerFormat(r16, r17, r18, r19, r20, r21, r22, r23);
        r10.add(r13);
        r28 = r10;
        goto L_0x023c;
        r17 = "application/x-mpegURL";
        r18 = "text/vtt";
        r19 = 0;
        r20 = -1;
        r16 = r7;
        r21 = r4;
        r22 = r8;
        r2 = org.telegram.messenger.exoplayer2.Format.createTextContainerFormat(r16, r17, r18, r19, r20, r21, r22);
        r6 = new org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMasterPlaylist$HlsUrl;
        r6.<init>(r5, r2);
        r12.add(r6);
        goto L_0x023c;
        r2 = r1.get(r9);
        r2 = (java.lang.String) r2;
        if (r2 == 0) goto L_0x0216;
        r6 = org.telegram.messenger.exoplayer2.util.MimeTypes.getMediaMimeType(r2);
        r18 = r6;
        goto L_0x0218;
        r6 = 0;
        goto L_0x0213;
        r17 = "application/x-mpegURL";
        r20 = -1;
        r21 = -1;
        r22 = -1;
        r23 = 0;
        r16 = r7;
        r19 = r2;
        r24 = r4;
        r25 = r8;
        r6 = org.telegram.messenger.exoplayer2.Format.createAudioContainerFormat(r16, r17, r18, r19, r20, r21, r22, r23, r24, r25);
        if (r5 != 0) goto L_0x0233;
        r27 = r6;
        goto L_0x023c;
        r10 = new org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMasterPlaylist$HlsUrl;
        r10.<init>(r5, r6);
        r11.add(r10);
        r0 = r0 + 1;
        r13 = r33;
        r2 = r34;
        r6 = 1;
        goto L_0x0132;
        r34 = r2;
        r33 = r13;
        if (r15 == 0) goto L_0x0251;
        r0 = java.util.Collections.emptyList();
        r28 = r0;
        r0 = new org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
        r3 = r0;
        r4 = r36;
        r5 = r14;
        r6 = r34;
        r7 = r11;
        r8 = r12;
        r9 = r27;
        r10 = r28;
        r3.<init>(r4, r5, r6, r7, r8, r9, r10);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylistParser.parseMasterPlaylist(org.telegram.messenger.exoplayer2.source.hls.playlist.HlsPlaylistParser$LineIterator, java.lang.String):org.telegram.messenger.exoplayer2.source.hls.playlist.HlsMasterPlaylist");
    }

    public HlsPlaylist parse(Uri uri, InputStream inputStream) throws IOException {
        Closeable reader = new BufferedReader(new InputStreamReader(inputStream));
        Queue<String> extraLines = new ArrayDeque();
        try {
            if (checkPlaylistHeader(reader)) {
                String readLine;
                HlsPlaylist startsWith;
                while (true) {
                    readLine = reader.readLine();
                    String line = readLine;
                    if (readLine != null) {
                        readLine = line.trim();
                        if (!readLine.isEmpty()) {
                            startsWith = readLine.startsWith(TAG_STREAM_INF);
                            if (startsWith == null) {
                                if (readLine.startsWith(TAG_TARGET_DURATION) || readLine.startsWith(TAG_MEDIA_SEQUENCE) || readLine.startsWith(TAG_MEDIA_DURATION) || readLine.startsWith(TAG_KEY) || readLine.startsWith(TAG_BYTERANGE) || readLine.equals(TAG_DISCONTINUITY) || readLine.equals(TAG_DISCONTINUITY_SEQUENCE)) {
                                    break;
                                } else if (readLine.equals(TAG_ENDLIST)) {
                                    break;
                                } else {
                                    extraLines.add(readLine);
                                }
                            } else {
                                extraLines.add(readLine);
                                startsWith = parseMasterPlaylist(new LineIterator(extraLines, reader), uri.toString());
                                Util.closeQuietly(reader);
                                return startsWith;
                            }
                        }
                    } else {
                        Util.closeQuietly(reader);
                        throw new ParserException("Failed to parse the playlist, could not identify any tags.");
                    }
                }
                extraLines.add(readLine);
                startsWith = parseMediaPlaylist(new LineIterator(extraLines, reader), uri.toString());
                return startsWith;
            }
            throw new UnrecognizedInputFormatException("Input does not start with the #EXTM3U header.", uri);
        } finally {
            Util.closeQuietly(reader);
        }
    }

    private static boolean checkPlaylistHeader(BufferedReader reader) throws IOException {
        int last = reader.read();
        if (last == 239) {
            if (reader.read() == 187) {
                if (reader.read() == 191) {
                    last = reader.read();
                }
            }
            return false;
        }
        char last2 = skipIgnorableWhitespace(reader, true, last);
        int playlistHeaderLength = PLAYLIST_HEADER.length();
        char last3 = last2;
        for (last = 0; last < playlistHeaderLength; last++) {
            if (last3 != PLAYLIST_HEADER.charAt(last)) {
                return false;
            }
            last3 = reader.read();
        }
        return Util.isLinebreak(skipIgnorableWhitespace(reader, false, last3));
    }

    private static int skipIgnorableWhitespace(BufferedReader reader, boolean skipLinebreaks, int c) throws IOException {
        while (c != -1 && Character.isWhitespace(c) && (skipLinebreaks || !Util.isLinebreak(c))) {
            c = reader.read();
        }
        return c;
    }

    private static int parseSelectionFlags(String line) {
        int i = 0;
        int parseBooleanAttribute = parseBooleanAttribute(line, REGEX_DEFAULT, false) | (parseBooleanAttribute(line, REGEX_FORCED, false) ? 2 : 0);
        if (parseBooleanAttribute(line, REGEX_AUTOSELECT, false)) {
            i = 4;
        }
        return parseBooleanAttribute | i;
    }

    private static HlsMediaPlaylist parseMediaPlaylist(LineIterator iterator, String baseUri) throws IOException {
        boolean z;
        int i;
        int playlistType = 0;
        long startOffsetUs = C.TIME_UNSET;
        List<Segment> segments = new ArrayList();
        List<String> tags = new ArrayList();
        DrmInitData drmInitData = null;
        long targetDurationUs = C.TIME_UNSET;
        boolean hasEndTag = false;
        Segment initializationSegment = null;
        long segmentDurationUs = 0;
        boolean hasDiscontinuitySequence = false;
        int playlistDiscontinuitySequence = 0;
        int relativeDiscontinuitySequence = 0;
        long segmentStartTimeUs = 0;
        long segmentByteRangeOffset = 0;
        long segmentByteRangeLength = -1;
        int segmentMediaSequence = 0;
        String encryptionKeyUri = null;
        String encryptionIV = null;
        int version = 1;
        boolean hasIndependentSegmentsTag = false;
        int mediaSequence = 0;
        long playlistStartTimeUs = 0;
        while (iterator.hasNext()) {
            String line = iterator.next();
            if (line.startsWith(TAG_PREFIX)) {
                tags.add(line);
            }
            String playlistTypeString;
            if (line.startsWith(TAG_PLAYLIST_TYPE)) {
                playlistTypeString = parseStringAttr(line, REGEX_PLAYLIST_TYPE);
                if ("VOD".equals(playlistTypeString)) {
                    playlistType = 1;
                } else if ("EVENT".equals(playlistTypeString)) {
                    playlistType = 2;
                }
            } else if (line.startsWith(TAG_START)) {
                startOffsetUs = (long) (parseDoubleAttr(line, REGEX_TIME_OFFSET) * 1000000.0d);
            } else {
                String uri;
                if (line.startsWith(TAG_INIT_SEGMENT)) {
                    uri = parseStringAttr(line, REGEX_URI);
                    playlistTypeString = parseOptionalStringAttr(line, REGEX_ATTR_BYTERANGE);
                    if (playlistTypeString != null) {
                        z = hasIndependentSegmentsTag;
                        hasIndependentSegmentsTag = playlistTypeString.split("@");
                        segmentByteRangeLength = Long.parseLong(hasIndependentSegmentsTag[0]);
                        i = version;
                        if (hasIndependentSegmentsTag.length > 1) {
                            segmentByteRangeOffset = Long.parseLong(hasIndependentSegmentsTag[1]);
                        }
                    } else {
                        i = version;
                        z = hasIndependentSegmentsTag;
                        String str = playlistTypeString;
                    }
                    initializationSegment = new Segment(uri, segmentByteRangeOffset, segmentByteRangeLength);
                    segmentByteRangeOffset = 0;
                    segmentByteRangeLength = -1;
                } else {
                    i = version;
                    z = hasIndependentSegmentsTag;
                    if (line.startsWith(TAG_TARGET_DURATION)) {
                        targetDurationUs = ((long) parseIntAttr(line, REGEX_TARGET_DURATION)) * C.MICROS_PER_SECOND;
                    } else if (line.startsWith(TAG_MEDIA_SEQUENCE)) {
                        mediaSequence = parseIntAttr(line, REGEX_MEDIA_SEQUENCE);
                        segmentMediaSequence = mediaSequence;
                    } else if (line.startsWith(TAG_VERSION)) {
                        version = parseIntAttr(line, REGEX_VERSION);
                        hasIndependentSegmentsTag = z;
                    } else if (line.startsWith(TAG_MEDIA_DURATION)) {
                        segmentDurationUs = (long) (parseDoubleAttr(line, REGEX_MEDIA_DURATION) * 4696837146684686336L);
                    } else {
                        int i2;
                        if (line.startsWith(TAG_KEY)) {
                            version = parseOptionalStringAttr(line, REGEX_METHOD);
                            String keyFormat = parseOptionalStringAttr(line, REGEX_KEYFORMAT);
                            String encryptionKeyUri2 = null;
                            if (METHOD_NONE.equals(version)) {
                                i2 = mediaSequence;
                                encryptionIV = null;
                                encryptionKeyUri = encryptionKeyUri2;
                            } else {
                                String encryptionIV2;
                                playlistTypeString = parseOptionalStringAttr(line, REGEX_IV);
                                String str2;
                                if (KEYFORMAT_IDENTITY.equals(keyFormat)) {
                                    i2 = mediaSequence;
                                    str2 = keyFormat;
                                    encryptionIV2 = playlistTypeString;
                                } else if (keyFormat == null) {
                                    i2 = mediaSequence;
                                    str2 = keyFormat;
                                    encryptionIV2 = playlistTypeString;
                                } else if (version == 0 || parseWidevineSchemeData(line, keyFormat) == null) {
                                    i2 = mediaSequence;
                                    encryptionIV2 = playlistTypeString;
                                    encryptionKeyUri = encryptionKeyUri2;
                                    encryptionIV = encryptionIV2;
                                } else {
                                    encryptionIV2 = playlistTypeString;
                                    if (!METHOD_SAMPLE_AES_CENC.equals(version)) {
                                        if (!METHOD_SAMPLE_AES_CTR.equals(version)) {
                                            playlistTypeString = C.CENC_TYPE_cbcs;
                                            i2 = mediaSequence;
                                            drmInitData = new DrmInitData(playlistTypeString, uri);
                                            encryptionKeyUri = encryptionKeyUri2;
                                            encryptionIV = encryptionIV2;
                                        }
                                    }
                                    playlistTypeString = C.CENC_TYPE_cenc;
                                    i2 = mediaSequence;
                                    drmInitData = new DrmInitData(playlistTypeString, uri);
                                    encryptionKeyUri = encryptionKeyUri2;
                                    encryptionIV = encryptionIV2;
                                }
                                if (METHOD_AES_128.equals(version) != 0) {
                                    encryptionKeyUri = parseStringAttr(line, REGEX_URI);
                                    encryptionIV = encryptionIV2;
                                }
                                encryptionKeyUri = encryptionKeyUri2;
                                encryptionIV = encryptionIV2;
                            }
                        } else {
                            i2 = mediaSequence;
                            if (line.startsWith(TAG_BYTERANGE) != 0) {
                                String[] splitByteRange = parseStringAttr(line, REGEX_BYTERANGE).split("@");
                                segmentByteRangeLength = Long.parseLong(splitByteRange[0]);
                                if (splitByteRange.length > 1) {
                                    segmentByteRangeOffset = Long.parseLong(splitByteRange[1]);
                                }
                            } else if (line.startsWith(TAG_DISCONTINUITY_SEQUENCE) != 0) {
                                hasDiscontinuitySequence = true;
                                playlistDiscontinuitySequence = Integer.parseInt(line.substring(line.indexOf(58) + 1));
                            } else if (line.equals(TAG_DISCONTINUITY) != 0) {
                                relativeDiscontinuitySequence++;
                            } else if (line.startsWith(TAG_PROGRAM_DATE_TIME) != 0) {
                                if (playlistStartTimeUs == 0) {
                                    playlistStartTimeUs = C.msToUs(Util.parseXsDateTime(line.substring(line.indexOf(58) + 1))) - segmentStartTimeUs;
                                }
                            } else if (line.startsWith("#") == 0) {
                                long segmentByteRangeOffset2;
                                if (encryptionKeyUri == null) {
                                    mediaSequence = 0;
                                } else if (encryptionIV != null) {
                                    mediaSequence = encryptionIV;
                                } else {
                                    mediaSequence = Integer.toHexString(segmentMediaSequence);
                                }
                                String segmentEncryptionIV = mediaSequence;
                                segmentMediaSequence++;
                                if (segmentByteRangeLength == -1) {
                                    segmentByteRangeOffset2 = 0;
                                } else {
                                    segmentByteRangeOffset2 = segmentByteRangeOffset;
                                }
                                segments.add(new Segment(line, segmentDurationUs, relativeDiscontinuitySequence, segmentStartTimeUs, encryptionKeyUri, segmentEncryptionIV, segmentByteRangeOffset2, segmentByteRangeLength));
                                long segmentStartTimeUs2 = segmentStartTimeUs + segmentDurationUs;
                                segmentDurationUs = 0;
                                if (segmentByteRangeLength != -1) {
                                    segmentByteRangeOffset = segmentByteRangeOffset2 + segmentByteRangeLength;
                                } else {
                                    segmentByteRangeOffset = segmentByteRangeOffset2;
                                }
                                segmentByteRangeLength = -1;
                                segmentStartTimeUs = segmentStartTimeUs2;
                            } else if (line.equals(TAG_INDEPENDENT_SEGMENTS) != 0) {
                                hasIndependentSegmentsTag = true;
                                version = i;
                                mediaSequence = i2;
                            } else if (line.equals(TAG_ENDLIST) != 0) {
                                hasEndTag = true;
                            }
                        }
                        hasIndependentSegmentsTag = z;
                        version = i;
                        mediaSequence = i2;
                    }
                }
                hasIndependentSegmentsTag = z;
                version = i;
            }
        }
        i = version;
        z = hasIndependentSegmentsTag;
        return new HlsMediaPlaylist(playlistType, baseUri, tags, startOffsetUs, playlistStartTimeUs, hasDiscontinuitySequence, playlistDiscontinuitySequence, mediaSequence, i, targetDurationUs, z, hasEndTag, playlistStartTimeUs != 0, drmInitData, initializationSegment, segments);
    }

    private static SchemeData parseWidevineSchemeData(String line, String keyFormat) throws ParserException {
        if (KEYFORMAT_WIDEVINE_PSSH_BINARY.equals(keyFormat)) {
            String uriString = parseStringAttr(line, REGEX_URI);
            return new SchemeData(C.WIDEVINE_UUID, MimeTypes.VIDEO_MP4, Base64.decode(uriString.substring(uriString.indexOf(44)), 0));
        } else if (!KEYFORMAT_WIDEVINE_PSSH_JSON.equals(keyFormat)) {
            return null;
        } else {
            try {
                return new SchemeData(C.WIDEVINE_UUID, "hls", line.getBytes(C.UTF8_NAME));
            } catch (Throwable e) {
                throw new ParserException(e);
            }
        }
    }

    private static int parseIntAttr(String line, Pattern pattern) throws ParserException {
        return Integer.parseInt(parseStringAttr(line, pattern));
    }

    private static double parseDoubleAttr(String line, Pattern pattern) throws ParserException {
        return Double.parseDouble(parseStringAttr(line, pattern));
    }

    private static String parseOptionalStringAttr(String line, Pattern pattern) {
        Matcher matcher = pattern.matcher(line);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static String parseStringAttr(String line, Pattern pattern) throws ParserException {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find() && matcher.groupCount() == 1) {
            return matcher.group(1);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Couldn't match ");
        stringBuilder.append(pattern.pattern());
        stringBuilder.append(" in ");
        stringBuilder.append(line);
        throw new ParserException(stringBuilder.toString());
    }

    private static boolean parseBooleanAttribute(String line, Pattern pattern, boolean defaultValue) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1).equals(BOOLEAN_TRUE);
        }
        return defaultValue;
    }

    private static Pattern compileBooleanAttrPattern(String attribute) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(attribute);
        stringBuilder.append("=(");
        stringBuilder.append(BOOLEAN_FALSE);
        stringBuilder.append("|");
        stringBuilder.append(BOOLEAN_TRUE);
        stringBuilder.append(")");
        return Pattern.compile(stringBuilder.toString());
    }
}
