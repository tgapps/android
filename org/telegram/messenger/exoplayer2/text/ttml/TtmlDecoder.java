package org.telegram.messenger.exoplayer2.text.ttml;

import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.exoplayer2.text.SimpleSubtitleDecoder;
import org.telegram.messenger.exoplayer2.text.SubtitleDecoderException;
import org.telegram.messenger.exoplayer2.util.Util;
import org.telegram.messenger.exoplayer2.util.XmlPullParserUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public final class TtmlDecoder extends SimpleSubtitleDecoder {
    private static final String ATTR_BEGIN = "begin";
    private static final String ATTR_DURATION = "dur";
    private static final String ATTR_END = "end";
    private static final String ATTR_REGION = "region";
    private static final String ATTR_STYLE = "style";
    private static final Pattern CLOCK_TIME = Pattern.compile("^([0-9][0-9]+):([0-9][0-9]):([0-9][0-9])(?:(\\.[0-9]+)|:([0-9][0-9])(?:\\.([0-9]+))?)?$");
    private static final FrameAndTickRate DEFAULT_FRAME_AND_TICK_RATE = new FrameAndTickRate(30.0f, 1, 1);
    private static final int DEFAULT_FRAME_RATE = 30;
    private static final Pattern FONT_SIZE = Pattern.compile("^(([0-9]*.)?[0-9]+)(px|em|%)$");
    private static final Pattern OFFSET_TIME = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?)(h|m|s|ms|f|t)$");
    private static final Pattern PERCENTAGE_COORDINATES = Pattern.compile("^(\\d+\\.?\\d*?)% (\\d+\\.?\\d*?)%$");
    private static final String TAG = "TtmlDecoder";
    private static final String TTP = "http://www.w3.org/ns/ttml#parameter";
    private final XmlPullParserFactory xmlParserFactory;

    private static final class FrameAndTickRate {
        final float effectiveFrameRate;
        final int subFrameRate;
        final int tickRate;

        FrameAndTickRate(float effectiveFrameRate, int subFrameRate, int tickRate) {
            this.effectiveFrameRate = effectiveFrameRate;
            this.subFrameRate = subFrameRate;
            this.tickRate = tickRate;
        }
    }

    private static void parseFontSize(java.lang.String r1, org.telegram.messenger.exoplayer2.text.ttml.TtmlStyle r2) throws org.telegram.messenger.exoplayer2.text.SubtitleDecoderException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder.parseFontSize(java.lang.String, org.telegram.messenger.exoplayer2.text.ttml.TtmlStyle):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = "\\s+";
        r0 = r9.split(r0);
        r1 = 2;
        r2 = 1;
        r3 = r0.length;
        if (r3 != r2) goto L_0x0012;
    L_0x000b:
        r3 = FONT_SIZE;
        r3 = r3.matcher(r9);
        goto L_0x0024;
    L_0x0012:
        r3 = r0.length;
        if (r3 != r1) goto L_0x00b7;
        r3 = FONT_SIZE;
        r4 = r0[r2];
        r3 = r3.matcher(r4);
        r4 = "TtmlDecoder";
        r5 = "Multiple values in fontSize attribute. Picking the second value for vertical font size and ignoring the first.";
        android.util.Log.w(r4, r5);
        r4 = r3.matches();
        if (r4 == 0) goto L_0x009b;
        r4 = 3;
        r5 = r3.group(r4);
        r6 = -1;
        r7 = r5.hashCode();
        r8 = 37;
        if (r7 == r8) goto L_0x0056;
        r8 = 3240; // 0xca8 float:4.54E-42 double:1.601E-320;
        if (r7 == r8) goto L_0x004c;
        r8 = 3592; // 0xe08 float:5.033E-42 double:1.7747E-320;
        if (r7 == r8) goto L_0x0042;
        goto L_0x005f;
        r7 = "px";
        r7 = r5.equals(r7);
        if (r7 == 0) goto L_0x005f;
        r6 = 0;
        goto L_0x005f;
        r7 = "em";
        r7 = r5.equals(r7);
        if (r7 == 0) goto L_0x005f;
        r6 = r2;
        goto L_0x005f;
        r7 = "%";
        r7 = r5.equals(r7);
        if (r7 == 0) goto L_0x005f;
        r6 = r1;
        switch(r6) {
            case 0: goto L_0x0086;
            case 1: goto L_0x0082;
            case 2: goto L_0x007e;
            default: goto L_0x0062;
        };
        r1 = new org.telegram.messenger.exoplayer2.text.SubtitleDecoderException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = "Invalid unit for fontSize: '";
        r2.append(r4);
        r2.append(r5);
        r4 = "'.";
        r2.append(r4);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
        r10.setFontSizeUnit(r4);
        goto L_0x008a;
        r10.setFontSizeUnit(r1);
        goto L_0x008a;
        r10.setFontSizeUnit(r2);
        r1 = r3.group(r2);
        r1 = java.lang.Float.valueOf(r1);
        r1 = r1.floatValue();
        r10.setFontSize(r1);
        return;
        r1 = new org.telegram.messenger.exoplayer2.text.SubtitleDecoderException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r4 = "Invalid expression for fontSize: '";
        r2.append(r4);
        r2.append(r9);
        r4 = "'.";
        r2.append(r4);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
        r1 = new org.telegram.messenger.exoplayer2.text.SubtitleDecoderException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Invalid number of entries for fontSize: ";
        r2.append(r3);
        r3 = r0.length;
        r2.append(r3);
        r3 = ".";
        r2.append(r3);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder.parseFontSize(java.lang.String, org.telegram.messenger.exoplayer2.text.ttml.TtmlStyle):void");
    }

    public TtmlDecoder() {
        super(TAG);
        try {
            this.xmlParserFactory = XmlPullParserFactory.newInstance();
            this.xmlParserFactory.setNamespaceAware(true);
        } catch (XmlPullParserException e) {
            throw new RuntimeException("Couldn't create XmlPullParserFactory instance", e);
        }
    }

    protected TtmlSubtitle decode(byte[] bytes, int length, boolean reset) throws SubtitleDecoderException {
        XmlPullParserException e;
        IOException e2;
        int i;
        try {
            XmlPullParser xmlParser = this.xmlParserFactory.newPullParser();
            Map<String, TtmlStyle> globalStyles = new HashMap();
            Map<String, TtmlRegion> regionMap = new HashMap();
            regionMap.put(TtmlNode.ANONYMOUS_REGION_ID, new TtmlRegion(null));
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes, 0, length);
                xmlParser.setInput(inputStream, null);
                TtmlSubtitle ttmlSubtitle = null;
                LinkedList<TtmlNode> nodeStack = new LinkedList();
                int unsupportedNodeDepth = 0;
                int eventType = xmlParser.getEventType();
                FrameAndTickRate frameAndTickRate = DEFAULT_FRAME_AND_TICK_RATE;
                while (eventType != 1) {
                    ByteArrayInputStream byteArrayInputStream;
                    TtmlNode parent = (TtmlNode) nodeStack.peekLast();
                    if (unsupportedNodeDepth == 0) {
                        String name = xmlParser.getName();
                        String name2;
                        if (eventType == 2) {
                            name2 = name;
                            if (TtmlNode.TAG_TT.equals(name2)) {
                                frameAndTickRate = parseFrameAndTickRates(xmlParser);
                            }
                            if (isSupportedTag(name2)) {
                                byteArrayInputStream = inputStream;
                                if (TtmlNode.TAG_HEAD.equals(name2) != null) {
                                    parseHeader(xmlParser, globalStyles, regionMap);
                                } else {
                                    try {
                                        inputStream = parseNode(xmlParser, parent, regionMap, frameAndTickRate);
                                        nodeStack.addLast(inputStream);
                                        if (parent != null) {
                                            parent.addChild(inputStream);
                                        }
                                    } catch (SubtitleDecoderException e3) {
                                        Log.w(TAG, "Suppressing parser error", e3);
                                        unsupportedNodeDepth++;
                                    }
                                }
                            } else {
                                String str = TAG;
                                byteArrayInputStream = inputStream;
                                inputStream = new StringBuilder();
                                inputStream.append("Ignoring unsupported tag: ");
                                inputStream.append(xmlParser.getName());
                                Log.i(str, inputStream.toString());
                                unsupportedNodeDepth++;
                            }
                        } else {
                            byteArrayInputStream = inputStream;
                            name2 = name;
                            if (eventType == 4) {
                                parent.addChild(TtmlNode.buildTextNode(xmlParser.getText()));
                            } else if (eventType == 3) {
                                if (xmlParser.getName().equals(TtmlNode.TAG_TT)) {
                                    ttmlSubtitle = new TtmlSubtitle((TtmlNode) nodeStack.getLast(), globalStyles, regionMap);
                                }
                                nodeStack.removeLast();
                            }
                        }
                    } else {
                        byteArrayInputStream = inputStream;
                        if (eventType == 2) {
                            unsupportedNodeDepth++;
                        } else if (eventType == 3) {
                            unsupportedNodeDepth--;
                        }
                    }
                    xmlParser.next();
                    eventType = xmlParser.getEventType();
                    inputStream = byteArrayInputStream;
                    byte[] bArr = bytes;
                }
                return ttmlSubtitle;
            } catch (XmlPullParserException e4) {
                e = e4;
            } catch (IOException e5) {
                e2 = e5;
            }
        } catch (XmlPullParserException e6) {
            e = e6;
            i = length;
            throw new SubtitleDecoderException("Unable to decode source", e);
        } catch (IOException e7) {
            e2 = e7;
            i = length;
            throw new IllegalStateException("Unexpected error when reading input.", e2);
        }
    }

    private FrameAndTickRate parseFrameAndTickRates(XmlPullParser xmlParser) throws SubtitleDecoderException {
        int frameRate = DEFAULT_FRAME_RATE;
        String frameRateString = xmlParser.getAttributeValue(TTP, "frameRate");
        if (frameRateString != null) {
            frameRate = Integer.parseInt(frameRateString);
        }
        float frameRateMultiplier = 1.0f;
        String frameRateMultiplierString = xmlParser.getAttributeValue(TTP, "frameRateMultiplier");
        if (frameRateMultiplierString != null) {
            String[] parts = frameRateMultiplierString.split(" ");
            if (parts.length != 2) {
                throw new SubtitleDecoderException("frameRateMultiplier doesn't have 2 parts");
            }
            frameRateMultiplier = ((float) Integer.parseInt(parts[0])) / ((float) Integer.parseInt(parts[1]));
        }
        int subFrameRate = DEFAULT_FRAME_AND_TICK_RATE.subFrameRate;
        String subFrameRateString = xmlParser.getAttributeValue(TTP, "subFrameRate");
        if (subFrameRateString != null) {
            subFrameRate = Integer.parseInt(subFrameRateString);
        }
        int tickRate = DEFAULT_FRAME_AND_TICK_RATE.tickRate;
        String tickRateString = xmlParser.getAttributeValue(TTP, "tickRate");
        if (tickRateString != null) {
            tickRate = Integer.parseInt(tickRateString);
        }
        return new FrameAndTickRate(((float) frameRate) * frameRateMultiplier, subFrameRate, tickRate);
    }

    private Map<String, TtmlStyle> parseHeader(XmlPullParser xmlParser, Map<String, TtmlStyle> globalStyles, Map<String, TtmlRegion> globalRegions) throws IOException, XmlPullParserException {
        do {
            xmlParser.next();
            if (XmlPullParserUtil.isStartTag(xmlParser, "style")) {
                String parentStyleId = XmlPullParserUtil.getAttributeValue(xmlParser, "style");
                TtmlStyle style = parseStyleAttributes(xmlParser, new TtmlStyle());
                if (parentStyleId != null) {
                    for (String id : parseStyleIds(parentStyleId)) {
                        style.chain((TtmlStyle) globalStyles.get(id));
                    }
                }
                if (style.getId() != null) {
                    globalStyles.put(style.getId(), style);
                }
            } else if (XmlPullParserUtil.isStartTag(xmlParser, "region")) {
                TtmlRegion ttmlRegion = parseRegionAttributes(xmlParser);
                if (ttmlRegion != null) {
                    globalRegions.put(ttmlRegion.id, ttmlRegion);
                }
            }
        } while (!XmlPullParserUtil.isEndTag(xmlParser, TtmlNode.TAG_HEAD));
        return globalStyles;
    }

    private TtmlRegion parseRegionAttributes(XmlPullParser xmlParser) {
        String str;
        StringBuilder stringBuilder;
        XmlPullParser xmlPullParser = xmlParser;
        String regionId = XmlPullParserUtil.getAttributeValue(xmlPullParser, TtmlNode.ATTR_ID);
        if (regionId == null) {
            return null;
        }
        String regionOrigin = XmlPullParserUtil.getAttributeValue(xmlPullParser, TtmlNode.ATTR_TTS_ORIGIN);
        if (regionOrigin != null) {
            Matcher originMatcher = PERCENTAGE_COORDINATES.matcher(regionOrigin);
            String str2;
            StringBuilder stringBuilder2;
            if (originMatcher.matches()) {
                int i = 1;
                try {
                    float position = Float.parseFloat(originMatcher.group(1)) / 100.0f;
                    float line = Float.parseFloat(originMatcher.group(2)) / 100.0f;
                    String regionExtent = XmlPullParserUtil.getAttributeValue(xmlPullParser, TtmlNode.ATTR_TTS_EXTENT);
                    if (regionExtent != null) {
                        originMatcher = PERCENTAGE_COORDINATES.matcher(regionExtent);
                        if (originMatcher.matches()) {
                            try {
                                float width = Float.parseFloat(originMatcher.group(1)) / 100.0f;
                                float height = Float.parseFloat(originMatcher.group(2)) / 100.0f;
                                int lineAnchor = 0;
                                String displayAlign = XmlPullParserUtil.getAttributeValue(xmlPullParser, TtmlNode.ATTR_TTS_DISPLAY_ALIGN);
                                if (displayAlign != null) {
                                    String toLowerInvariant = Util.toLowerInvariant(displayAlign);
                                    int hashCode = toLowerInvariant.hashCode();
                                    if (hashCode == -1364013995) {
                                        if (toLowerInvariant.equals(TtmlNode.CENTER)) {
                                            i = 0;
                                            switch (i) {
                                                case 0:
                                                    lineAnchor = 1;
                                                    line += height / 2.0f;
                                                    break;
                                                case 1:
                                                    lineAnchor = 2;
                                                    line += height;
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    } else if (hashCode == 92734940) {
                                        if (toLowerInvariant.equals("after")) {
                                            switch (i) {
                                                case 0:
                                                    lineAnchor = 1;
                                                    line += height / 2.0f;
                                                    break;
                                                case 1:
                                                    lineAnchor = 2;
                                                    line += height;
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                    }
                                    i = -1;
                                    switch (i) {
                                        case 0:
                                            lineAnchor = 1;
                                            line += height / 2.0f;
                                            break;
                                        case 1:
                                            lineAnchor = 2;
                                            line += height;
                                            break;
                                        default:
                                            break;
                                    }
                                }
                                return new TtmlRegion(regionId, position, line, 0, lineAnchor, width);
                            } catch (NumberFormatException e) {
                                str = TAG;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Ignoring region with malformed extent: ");
                                stringBuilder.append(regionOrigin);
                                Log.w(str, stringBuilder.toString());
                                return null;
                            }
                        }
                        str2 = TAG;
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Ignoring region with unsupported extent: ");
                        stringBuilder2.append(regionOrigin);
                        Log.w(str2, stringBuilder2.toString());
                        return null;
                    }
                    Log.w(TAG, "Ignoring region without an extent");
                    return null;
                } catch (NumberFormatException e2) {
                    NumberFormatException numberFormatException = e2;
                    str = TAG;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Ignoring region with malformed origin: ");
                    stringBuilder.append(regionOrigin);
                    Log.w(str, stringBuilder.toString());
                    return null;
                }
            }
            str2 = TAG;
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Ignoring region with unsupported origin: ");
            stringBuilder2.append(regionOrigin);
            Log.w(str2, stringBuilder2.toString());
            return null;
        }
        Log.w(TAG, "Ignoring region without an origin");
        return null;
    }

    private String[] parseStyleIds(String parentStyleIds) {
        return parentStyleIds.split("\\s+");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.telegram.messenger.exoplayer2.text.ttml.TtmlStyle parseStyleAttributes(org.xmlpull.v1.XmlPullParser r12, org.telegram.messenger.exoplayer2.text.ttml.TtmlStyle r13) {
        /*
        r11 = this;
        r0 = r12.getAttributeCount();
        r1 = 0;
        r2 = r13;
        r13 = r1;
    L_0x0007:
        if (r13 >= r0) goto L_0x0215;
    L_0x0009:
        r3 = r12.getAttributeValue(r13);
        r4 = r12.getAttributeName(r13);
        r5 = r4.hashCode();
        r6 = 4;
        r7 = 3;
        r8 = 2;
        r9 = -1;
        r10 = 1;
        switch(r5) {
            case -1550943582: goto L_0x0070;
            case -1224696685: goto L_0x0066;
            case -1065511464: goto L_0x005c;
            case -879295043: goto L_0x0051;
            case -734428249: goto L_0x0047;
            case 3355: goto L_0x003d;
            case 94842723: goto L_0x0033;
            case 365601008: goto L_0x0029;
            case 1287124693: goto L_0x001f;
            default: goto L_0x001d;
        };
    L_0x001d:
        goto L_0x007a;
    L_0x001f:
        r5 = "backgroundColor";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007a;
    L_0x0027:
        r4 = r10;
        goto L_0x007b;
    L_0x0029:
        r5 = "fontSize";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007a;
    L_0x0031:
        r4 = r6;
        goto L_0x007b;
    L_0x0033:
        r5 = "color";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007a;
    L_0x003b:
        r4 = r8;
        goto L_0x007b;
    L_0x003d:
        r5 = "id";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007a;
    L_0x0045:
        r4 = r1;
        goto L_0x007b;
    L_0x0047:
        r5 = "fontWeight";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007a;
    L_0x004f:
        r4 = 5;
        goto L_0x007b;
    L_0x0051:
        r5 = "textDecoration";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007a;
    L_0x0059:
        r4 = 8;
        goto L_0x007b;
    L_0x005c:
        r5 = "textAlign";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007a;
    L_0x0064:
        r4 = 7;
        goto L_0x007b;
    L_0x0066:
        r5 = "fontFamily";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007a;
    L_0x006e:
        r4 = r7;
        goto L_0x007b;
    L_0x0070:
        r5 = "fontStyle";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x007a;
    L_0x0078:
        r4 = 6;
        goto L_0x007b;
    L_0x007a:
        r4 = r9;
    L_0x007b:
        switch(r4) {
            case 0: goto L_0x01fd;
            case 1: goto L_0x01d9;
            case 2: goto L_0x01b5;
            case 3: goto L_0x01ac;
            case 4: goto L_0x018a;
            case 5: goto L_0x017a;
            case 6: goto L_0x016a;
            case 7: goto L_0x00ef;
            case 8: goto L_0x0080;
            default: goto L_0x007e;
        };
    L_0x007e:
        goto L_0x0211;
    L_0x0080:
        r4 = org.telegram.messenger.exoplayer2.util.Util.toLowerInvariant(r3);
        r5 = r4.hashCode();
        r6 = -1461280213; // 0xffffffffa8e6a22b float:-2.5605459E-14 double:NaN;
        if (r5 == r6) goto L_0x00bb;
    L_0x008d:
        r6 = -1026963764; // 0xffffffffc2c9c6cc float:-100.888275 double:NaN;
        if (r5 == r6) goto L_0x00b1;
    L_0x0092:
        r6 = 913457136; // 0x36723ff0 float:3.6098027E-6 double:4.5130779E-315;
        if (r5 == r6) goto L_0x00a7;
    L_0x0097:
        r6 = 1679736913; // 0x641ec051 float:1.1713774E22 double:8.29900303E-315;
        if (r5 == r6) goto L_0x009d;
    L_0x009c:
        goto L_0x00c4;
    L_0x009d:
        r5 = "linethrough";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x00c4;
    L_0x00a5:
        r7 = r1;
        goto L_0x00c5;
    L_0x00a7:
        r5 = "nolinethrough";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x00c4;
    L_0x00af:
        r7 = r10;
        goto L_0x00c5;
    L_0x00b1:
        r5 = "underline";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x00c4;
    L_0x00b9:
        r7 = r8;
        goto L_0x00c5;
    L_0x00bb:
        r5 = "nounderline";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x00c4;
    L_0x00c3:
        goto L_0x00c5;
    L_0x00c4:
        r7 = r9;
    L_0x00c5:
        switch(r7) {
            case 0: goto L_0x00e4;
            case 1: goto L_0x00db;
            case 2: goto L_0x00d2;
            case 3: goto L_0x00c9;
            default: goto L_0x00c8;
        };
    L_0x00c8:
        goto L_0x00ed;
    L_0x00c9:
        r4 = r11.createIfNull(r2);
        r2 = r4.setUnderline(r1);
        goto L_0x00ed;
    L_0x00d2:
        r4 = r11.createIfNull(r2);
        r2 = r4.setUnderline(r10);
        goto L_0x00ed;
    L_0x00db:
        r4 = r11.createIfNull(r2);
        r2 = r4.setLinethrough(r1);
        goto L_0x00ed;
    L_0x00e4:
        r4 = r11.createIfNull(r2);
        r2 = r4.setLinethrough(r10);
    L_0x00ed:
        goto L_0x0211;
    L_0x00ef:
        r4 = org.telegram.messenger.exoplayer2.util.Util.toLowerInvariant(r3);
        r5 = r4.hashCode();
        switch(r5) {
            case -1364013995: goto L_0x0123;
            case 100571: goto L_0x0119;
            case 3317767: goto L_0x010f;
            case 108511772: goto L_0x0105;
            case 109757538: goto L_0x00fb;
            default: goto L_0x00fa;
        };
    L_0x00fa:
        goto L_0x012c;
    L_0x00fb:
        r5 = "start";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x012c;
    L_0x0103:
        r6 = r10;
        goto L_0x012d;
    L_0x0105:
        r5 = "right";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x012c;
    L_0x010d:
        r6 = r8;
        goto L_0x012d;
    L_0x010f:
        r5 = "left";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x012c;
    L_0x0117:
        r6 = r1;
        goto L_0x012d;
    L_0x0119:
        r5 = "end";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x012c;
    L_0x0121:
        r6 = r7;
        goto L_0x012d;
    L_0x0123:
        r5 = "center";
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x012c;
    L_0x012b:
        goto L_0x012d;
    L_0x012c:
        r6 = r9;
    L_0x012d:
        switch(r6) {
            case 0: goto L_0x015d;
            case 1: goto L_0x0152;
            case 2: goto L_0x0147;
            case 3: goto L_0x013c;
            case 4: goto L_0x0131;
            default: goto L_0x0130;
        };
    L_0x0130:
        goto L_0x0168;
    L_0x0131:
        r4 = r11.createIfNull(r2);
        r5 = android.text.Layout.Alignment.ALIGN_CENTER;
        r2 = r4.setTextAlign(r5);
        goto L_0x0168;
    L_0x013c:
        r4 = r11.createIfNull(r2);
        r5 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        r2 = r4.setTextAlign(r5);
        goto L_0x0168;
    L_0x0147:
        r4 = r11.createIfNull(r2);
        r5 = android.text.Layout.Alignment.ALIGN_OPPOSITE;
        r2 = r4.setTextAlign(r5);
        goto L_0x0168;
    L_0x0152:
        r4 = r11.createIfNull(r2);
        r5 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r2 = r4.setTextAlign(r5);
        goto L_0x0168;
    L_0x015d:
        r4 = r11.createIfNull(r2);
        r5 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r2 = r4.setTextAlign(r5);
    L_0x0168:
        goto L_0x0211;
    L_0x016a:
        r4 = r11.createIfNull(r2);
        r5 = "italic";
        r5 = r5.equalsIgnoreCase(r3);
        r2 = r4.setItalic(r5);
        goto L_0x0211;
    L_0x017a:
        r4 = r11.createIfNull(r2);
        r5 = "bold";
        r5 = r5.equalsIgnoreCase(r3);
        r2 = r4.setBold(r5);
        goto L_0x0211;
    L_0x018a:
        r4 = r11.createIfNull(r2);	 Catch:{ SubtitleDecoderException -> 0x0194 }
        r2 = r4;
        parseFontSize(r3, r2);	 Catch:{ SubtitleDecoderException -> 0x0194 }
        goto L_0x0211;
    L_0x0194:
        r4 = move-exception;
        r5 = "TtmlDecoder";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Failed parsing fontSize value: ";
        r6.append(r7);
        r6.append(r3);
        r6 = r6.toString();
        android.util.Log.w(r5, r6);
        goto L_0x0211;
    L_0x01ac:
        r4 = r11.createIfNull(r2);
        r2 = r4.setFontFamily(r3);
        goto L_0x0211;
    L_0x01b5:
        r2 = r11.createIfNull(r2);
        r4 = org.telegram.messenger.exoplayer2.util.ColorParser.parseTtmlColor(r3);	 Catch:{ IllegalArgumentException -> 0x01c1 }
        r2.setFontColor(r4);	 Catch:{ IllegalArgumentException -> 0x01c1 }
        goto L_0x0211;
    L_0x01c1:
        r4 = move-exception;
        r5 = "TtmlDecoder";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Failed parsing color value: ";
        r6.append(r7);
        r6.append(r3);
        r6 = r6.toString();
        android.util.Log.w(r5, r6);
        goto L_0x0211;
    L_0x01d9:
        r2 = r11.createIfNull(r2);
        r4 = org.telegram.messenger.exoplayer2.util.ColorParser.parseTtmlColor(r3);	 Catch:{ IllegalArgumentException -> 0x01e5 }
        r2.setBackgroundColor(r4);	 Catch:{ IllegalArgumentException -> 0x01e5 }
        goto L_0x0211;
    L_0x01e5:
        r4 = move-exception;
        r5 = "TtmlDecoder";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Failed parsing background value: ";
        r6.append(r7);
        r6.append(r3);
        r6 = r6.toString();
        android.util.Log.w(r5, r6);
        goto L_0x0211;
    L_0x01fd:
        r4 = "style";
        r5 = r12.getName();
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x0211;
    L_0x0209:
        r4 = r11.createIfNull(r2);
        r2 = r4.setId(r3);
    L_0x0211:
        r13 = r13 + 1;
        goto L_0x0007;
    L_0x0215:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder.parseStyleAttributes(org.xmlpull.v1.XmlPullParser, org.telegram.messenger.exoplayer2.text.ttml.TtmlStyle):org.telegram.messenger.exoplayer2.text.ttml.TtmlStyle");
    }

    private TtmlStyle createIfNull(TtmlStyle style) {
        return style == null ? new TtmlStyle() : style;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private org.telegram.messenger.exoplayer2.text.ttml.TtmlNode parseNode(org.xmlpull.v1.XmlPullParser r27, org.telegram.messenger.exoplayer2.text.ttml.TtmlNode r28, java.util.Map<java.lang.String, org.telegram.messenger.exoplayer2.text.ttml.TtmlRegion> r29, org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder.FrameAndTickRate r30) throws org.telegram.messenger.exoplayer2.text.SubtitleDecoderException {
        /*
        r26 = this;
        r0 = r26;
        r1 = r27;
        r2 = r28;
        r3 = r30;
        r4 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r6 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r8 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r10 = "";
        r11 = 0;
        r12 = r27.getAttributeCount();
        r13 = 0;
        r13 = r0.parseStyleAttributes(r1, r13);
        r22 = r10;
        r24 = r4;
        r5 = r11;
        r10 = r24;
        r4 = 0;
    L_0x002b:
        if (r4 >= r12) goto L_0x00b3;
    L_0x002d:
        r15 = r1.getAttributeName(r4);
        r14 = r1.getAttributeValue(r4);
        r16 = -1;
        r17 = r15.hashCode();
        switch(r17) {
            case -934795532: goto L_0x0071;
            case 99841: goto L_0x0065;
            case 100571: goto L_0x0059;
            case 93616297: goto L_0x004d;
            case 109780401: goto L_0x0041;
            default: goto L_0x003e;
        };
    L_0x003e:
        r23 = r12;
        goto L_0x007d;
    L_0x0041:
        r23 = r12;
        r12 = "style";
        r12 = r15.equals(r12);
        if (r12 == 0) goto L_0x007d;
    L_0x004b:
        r12 = 3;
        goto L_0x007f;
    L_0x004d:
        r23 = r12;
        r12 = "begin";
        r12 = r15.equals(r12);
        if (r12 == 0) goto L_0x007d;
    L_0x0057:
        r12 = 0;
        goto L_0x007f;
    L_0x0059:
        r23 = r12;
        r12 = "end";
        r12 = r15.equals(r12);
        if (r12 == 0) goto L_0x007d;
    L_0x0063:
        r12 = 1;
        goto L_0x007f;
    L_0x0065:
        r23 = r12;
        r12 = "dur";
        r12 = r15.equals(r12);
        if (r12 == 0) goto L_0x007d;
    L_0x006f:
        r12 = 2;
        goto L_0x007f;
    L_0x0071:
        r23 = r12;
        r12 = "region";
        r12 = r15.equals(r12);
        if (r12 == 0) goto L_0x007d;
    L_0x007b:
        r12 = 4;
        goto L_0x007f;
    L_0x007d:
        r12 = r16;
    L_0x007f:
        switch(r12) {
            case 0: goto L_0x00a6;
            case 1: goto L_0x00a1;
            case 2: goto L_0x009c;
            case 3: goto L_0x0090;
            case 4: goto L_0x0083;
            default: goto L_0x0082;
        };
    L_0x0082:
        goto L_0x00ab;
    L_0x0083:
        r12 = r29;
        r16 = r12.containsKey(r14);
        if (r16 == 0) goto L_0x00ab;
    L_0x008b:
        r16 = r14;
        r22 = r16;
        goto L_0x00ab;
    L_0x0090:
        r12 = r29;
        r12 = r0.parseStyleIds(r14);
        r0 = r12.length;
        if (r0 <= 0) goto L_0x00ab;
    L_0x0099:
        r0 = r12;
        r5 = r0;
        goto L_0x00ab;
    L_0x009c:
        r10 = parseTimeExpression(r14, r3);
        goto L_0x00ab;
    L_0x00a1:
        r8 = parseTimeExpression(r14, r3);
        goto L_0x00ab;
    L_0x00a6:
        r6 = parseTimeExpression(r14, r3);
    L_0x00ab:
        r4 = r4 + 1;
        r12 = r23;
        r0 = r26;
        goto L_0x002b;
    L_0x00b3:
        r23 = r12;
        r14 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        if (r2 == 0) goto L_0x00d8;
    L_0x00bc:
        r3 = r2.startTimeUs;
        r0 = (r3 > r14 ? 1 : (r3 == r14 ? 0 : -1));
        if (r0 == 0) goto L_0x00d8;
    L_0x00c2:
        r0 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1));
        if (r0 == 0) goto L_0x00cb;
    L_0x00c6:
        r3 = r2.startTimeUs;
        r16 = r6 + r3;
        goto L_0x00cd;
    L_0x00cb:
        r16 = r6;
    L_0x00cd:
        r0 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1));
        if (r0 == 0) goto L_0x00d6;
    L_0x00d1:
        r3 = r2.startTimeUs;
        r6 = r8 + r3;
        r8 = r6;
    L_0x00d6:
        r6 = r16;
    L_0x00d8:
        r0 = (r8 > r14 ? 1 : (r8 == r14 ? 0 : -1));
        if (r0 != 0) goto L_0x00ee;
    L_0x00dc:
        r0 = (r10 > r14 ? 1 : (r10 == r14 ? 0 : -1));
        if (r0 == 0) goto L_0x00e3;
    L_0x00e0:
        r3 = r6 + r10;
    L_0x00e2:
        goto L_0x00ef;
    L_0x00e3:
        if (r2 == 0) goto L_0x00ee;
    L_0x00e5:
        r3 = r2.endTimeUs;
        r0 = (r3 > r14 ? 1 : (r3 == r14 ? 0 : -1));
        if (r0 == 0) goto L_0x00ee;
    L_0x00eb:
        r3 = r2.endTimeUs;
        goto L_0x00e2;
    L_0x00ee:
        r3 = r8;
    L_0x00ef:
        r14 = r27.getName();
        r15 = r6;
        r17 = r3;
        r19 = r13;
        r20 = r5;
        r21 = r22;
        r0 = org.telegram.messenger.exoplayer2.text.ttml.TtmlNode.buildNode(r14, r15, r17, r19, r20, r21);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder.parseNode(org.xmlpull.v1.XmlPullParser, org.telegram.messenger.exoplayer2.text.ttml.TtmlNode, java.util.Map, org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder$FrameAndTickRate):org.telegram.messenger.exoplayer2.text.ttml.TtmlNode");
    }

    private static boolean isSupportedTag(String tag) {
        if (!(tag.equals(TtmlNode.TAG_TT) || tag.equals(TtmlNode.TAG_HEAD) || tag.equals(TtmlNode.TAG_BODY) || tag.equals(TtmlNode.TAG_DIV) || tag.equals(TtmlNode.TAG_P) || tag.equals(TtmlNode.TAG_SPAN) || tag.equals(TtmlNode.TAG_BR) || tag.equals("style") || tag.equals(TtmlNode.TAG_STYLING) || tag.equals(TtmlNode.TAG_LAYOUT) || tag.equals("region") || tag.equals(TtmlNode.TAG_METADATA) || tag.equals(TtmlNode.TAG_SMPTE_IMAGE) || tag.equals(TtmlNode.TAG_SMPTE_DATA))) {
            if (!tag.equals(TtmlNode.TAG_SMPTE_INFORMATION)) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static long parseTimeExpression(java.lang.String r17, org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder.FrameAndTickRate r18) throws org.telegram.messenger.exoplayer2.text.SubtitleDecoderException {
        /*
        r0 = r17;
        r1 = r18;
        r2 = CLOCK_TIME;
        r2 = r2.matcher(r0);
        r3 = r2.matches();
        r6 = 5;
        r7 = 4;
        r8 = 3;
        r9 = 2;
        r10 = 1;
        if (r3 == 0) goto L_0x0077;
    L_0x0015:
        r3 = r2.group(r10);
        r10 = java.lang.Long.parseLong(r3);
        r12 = 3600; // 0xe10 float:5.045E-42 double:1.7786E-320;
        r10 = r10 * r12;
        r10 = (double) r10;
        r9 = r2.group(r9);
        r12 = java.lang.Long.parseLong(r9);
        r14 = 60;
        r12 = r12 * r14;
        r12 = (double) r12;
        r10 = r10 + r12;
        r8 = r2.group(r8);
        r12 = java.lang.Long.parseLong(r8);
        r12 = (double) r12;
        r10 = r10 + r12;
        r7 = r2.group(r7);
        r12 = 0;
        if (r7 == 0) goto L_0x0045;
    L_0x0040:
        r14 = java.lang.Double.parseDouble(r7);
        goto L_0x0046;
    L_0x0045:
        r14 = r12;
    L_0x0046:
        r10 = r10 + r14;
        r6 = r2.group(r6);
        if (r6 == 0) goto L_0x0057;
    L_0x004d:
        r14 = java.lang.Long.parseLong(r6);
        r14 = (float) r14;
        r15 = r1.effectiveFrameRate;
        r14 = r14 / r15;
        r14 = (double) r14;
        goto L_0x0058;
    L_0x0057:
        r14 = r12;
    L_0x0058:
        r10 = r10 + r14;
        r14 = 6;
        r14 = r2.group(r14);
        if (r14 == 0) goto L_0x006e;
    L_0x0060:
        r12 = java.lang.Long.parseLong(r14);
        r12 = (double) r12;
        r15 = r1.subFrameRate;
        r4 = (double) r15;
        r12 = r12 / r4;
        r4 = r1.effectiveFrameRate;
        r4 = (double) r4;
        r12 = r12 / r4;
    L_0x006e:
        r10 = r10 + r12;
        r4 = 4696837146684686336; // 0x412e848000000000 float:0.0 double:1000000.0;
        r4 = r4 * r10;
        r4 = (long) r4;
        return r4;
    L_0x0077:
        r3 = OFFSET_TIME;
        r2 = r3.matcher(r0);
        r3 = r2.matches();
        if (r3 == 0) goto L_0x010d;
    L_0x0083:
        r3 = r2.group(r10);
        r4 = java.lang.Double.parseDouble(r3);
        r11 = r2.group(r9);
        r12 = -1;
        r13 = r11.hashCode();
        r14 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        if (r13 == r14) goto L_0x00d9;
    L_0x0098:
        r7 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        if (r13 == r7) goto L_0x00cf;
    L_0x009c:
        r7 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        if (r13 == r7) goto L_0x00c5;
    L_0x00a0:
        r7 = 3494; // 0xda6 float:4.896E-42 double:1.7263E-320;
        if (r13 == r7) goto L_0x00bb;
    L_0x00a4:
        switch(r13) {
            case 115: goto L_0x00b1;
            case 116: goto L_0x00a8;
            default: goto L_0x00a7;
        };
    L_0x00a7:
        goto L_0x00e3;
    L_0x00a8:
        r7 = "t";
        r7 = r11.equals(r7);
        if (r7 == 0) goto L_0x00e3;
    L_0x00b0:
        goto L_0x00e4;
    L_0x00b1:
        r6 = "s";
        r6 = r11.equals(r6);
        if (r6 == 0) goto L_0x00e3;
    L_0x00b9:
        r6 = r9;
        goto L_0x00e4;
    L_0x00bb:
        r6 = "ms";
        r6 = r11.equals(r6);
        if (r6 == 0) goto L_0x00e3;
    L_0x00c3:
        r6 = r8;
        goto L_0x00e4;
    L_0x00c5:
        r6 = "m";
        r6 = r11.equals(r6);
        if (r6 == 0) goto L_0x00e3;
    L_0x00cd:
        r6 = r10;
        goto L_0x00e4;
    L_0x00cf:
        r6 = "h";
        r6 = r11.equals(r6);
        if (r6 == 0) goto L_0x00e3;
    L_0x00d7:
        r6 = 0;
        goto L_0x00e4;
    L_0x00d9:
        r6 = "f";
        r6 = r11.equals(r6);
        if (r6 == 0) goto L_0x00e3;
    L_0x00e1:
        r6 = r7;
        goto L_0x00e4;
    L_0x00e3:
        r6 = r12;
    L_0x00e4:
        switch(r6) {
            case 0: goto L_0x00fe;
            case 1: goto L_0x00fa;
            case 2: goto L_0x00f9;
            case 3: goto L_0x00f2;
            case 4: goto L_0x00ed;
            case 5: goto L_0x00e8;
            default: goto L_0x00e7;
        };
    L_0x00e7:
        goto L_0x0105;
    L_0x00e8:
        r6 = r1.tickRate;
        r6 = (double) r6;
        r4 = r4 / r6;
        goto L_0x0105;
    L_0x00ed:
        r6 = r1.effectiveFrameRate;
        r6 = (double) r6;
        r4 = r4 / r6;
        goto L_0x0105;
    L_0x00f2:
        r6 = 4652007308841189376; // 0x408f400000000000 float:0.0 double:1000.0;
        r4 = r4 / r6;
        goto L_0x0105;
    L_0x00f9:
        goto L_0x0105;
    L_0x00fa:
        r6 = 4633641066610819072; // 0x404e000000000000 float:0.0 double:60.0;
        r4 = r4 * r6;
        goto L_0x0105;
    L_0x00fe:
        r6 = 4660134898793709568; // 0x40ac200000000000 float:0.0 double:3600.0;
        r4 = r4 * r6;
    L_0x0105:
        r6 = 4696837146684686336; // 0x412e848000000000 float:0.0 double:1000000.0;
        r6 = r6 * r4;
        r6 = (long) r6;
        return r6;
    L_0x010d:
        r3 = new org.telegram.messenger.exoplayer2.text.SubtitleDecoderException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Malformed time expression: ";
        r4.append(r5);
        r4.append(r0);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder.parseTimeExpression(java.lang.String, org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder$FrameAndTickRate):long");
    }
}
