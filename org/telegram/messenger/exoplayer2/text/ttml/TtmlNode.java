package org.telegram.messenger.exoplayer2.text.ttml;

import android.text.SpannableStringBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.text.Cue;
import org.telegram.messenger.exoplayer2.util.Assertions;

final class TtmlNode {
    public static final String ANONYMOUS_REGION_ID = "";
    public static final String ATTR_ID = "id";
    public static final String ATTR_TTS_BACKGROUND_COLOR = "backgroundColor";
    public static final String ATTR_TTS_COLOR = "color";
    public static final String ATTR_TTS_DISPLAY_ALIGN = "displayAlign";
    public static final String ATTR_TTS_EXTENT = "extent";
    public static final String ATTR_TTS_FONT_FAMILY = "fontFamily";
    public static final String ATTR_TTS_FONT_SIZE = "fontSize";
    public static final String ATTR_TTS_FONT_STYLE = "fontStyle";
    public static final String ATTR_TTS_FONT_WEIGHT = "fontWeight";
    public static final String ATTR_TTS_ORIGIN = "origin";
    public static final String ATTR_TTS_TEXT_ALIGN = "textAlign";
    public static final String ATTR_TTS_TEXT_DECORATION = "textDecoration";
    public static final String BOLD = "bold";
    public static final String CENTER = "center";
    public static final String END = "end";
    public static final String ITALIC = "italic";
    public static final String LEFT = "left";
    public static final String LINETHROUGH = "linethrough";
    public static final String NO_LINETHROUGH = "nolinethrough";
    public static final String NO_UNDERLINE = "nounderline";
    public static final String RIGHT = "right";
    public static final String START = "start";
    public static final String TAG_BODY = "body";
    public static final String TAG_BR = "br";
    public static final String TAG_DIV = "div";
    public static final String TAG_HEAD = "head";
    public static final String TAG_LAYOUT = "layout";
    public static final String TAG_METADATA = "metadata";
    public static final String TAG_P = "p";
    public static final String TAG_REGION = "region";
    public static final String TAG_SMPTE_DATA = "smpte:data";
    public static final String TAG_SMPTE_IMAGE = "smpte:image";
    public static final String TAG_SMPTE_INFORMATION = "smpte:information";
    public static final String TAG_SPAN = "span";
    public static final String TAG_STYLE = "style";
    public static final String TAG_STYLING = "styling";
    public static final String TAG_TT = "tt";
    public static final String UNDERLINE = "underline";
    private List<TtmlNode> children;
    public final long endTimeUs;
    public final boolean isTextNode;
    private final HashMap<String, Integer> nodeEndsByRegion;
    private final HashMap<String, Integer> nodeStartsByRegion;
    public final String regionId;
    public final long startTimeUs;
    public final TtmlStyle style;
    private final String[] styleIds;
    public final String tag;
    public final String text;

    private android.text.SpannableStringBuilder cleanUpText(android.text.SpannableStringBuilder r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.text.ttml.TtmlNode.cleanUpText(android.text.SpannableStringBuilder):android.text.SpannableStringBuilder
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
        r0 = this;
        r0 = r7.length();
        r1 = 0;
        r2 = r0;
        r0 = r1;
    L_0x0007:
        r3 = 32;
        if (r0 >= r2) goto L_0x0031;
    L_0x000b:
        r4 = r7.charAt(r0);
        if (r4 != r3) goto L_0x002e;
    L_0x0011:
        r4 = r0 + 1;
    L_0x0013:
        r5 = r7.length();
        if (r4 >= r5) goto L_0x0022;
    L_0x0019:
        r5 = r7.charAt(r4);
        if (r5 != r3) goto L_0x0022;
    L_0x001f:
        r4 = r4 + 1;
        goto L_0x0013;
    L_0x0022:
        r3 = r0 + 1;
        r3 = r4 - r3;
        if (r3 <= 0) goto L_0x002e;
    L_0x0028:
        r5 = r0 + r3;
        r7.delete(r0, r5);
        r2 = r2 - r3;
    L_0x002e:
        r0 = r0 + 1;
        goto L_0x0007;
    L_0x0031:
        if (r2 <= 0) goto L_0x003f;
    L_0x0033:
        r0 = r7.charAt(r1);
        if (r0 != r3) goto L_0x003f;
    L_0x0039:
        r0 = 1;
        r7.delete(r1, r0);
        r2 = r2 + -1;
    L_0x003f:
        r0 = r1;
    L_0x0040:
        r4 = r2 + -1;
        r5 = 10;
        if (r0 >= r4) goto L_0x0060;
    L_0x0046:
        r4 = r7.charAt(r0);
        if (r4 != r5) goto L_0x005d;
    L_0x004c:
        r4 = r0 + 1;
        r4 = r7.charAt(r4);
        if (r4 != r3) goto L_0x005d;
    L_0x0054:
        r4 = r0 + 1;
        r5 = r0 + 2;
        r7.delete(r4, r5);
        r2 = r2 + -1;
    L_0x005d:
        r0 = r0 + 1;
        goto L_0x0040;
    L_0x0060:
        if (r2 <= 0) goto L_0x0071;
    L_0x0062:
        r0 = r2 + -1;
        r0 = r7.charAt(r0);
        if (r0 != r3) goto L_0x0071;
        r0 = r2 + -1;
        r7.delete(r0, r2);
        r2 = r2 + -1;
        r0 = r1;
        r1 = r2 + -1;
        if (r0 >= r1) goto L_0x008f;
        r1 = r7.charAt(r0);
        if (r1 != r3) goto L_0x008c;
        r1 = r0 + 1;
        r1 = r7.charAt(r1);
        if (r1 != r5) goto L_0x008c;
        r1 = r0 + 1;
        r7.delete(r0, r1);
        r2 = r2 + -1;
        r1 = r0 + 1;
        goto L_0x0072;
        if (r2 <= 0) goto L_0x009e;
        r0 = r2 + -1;
        r0 = r7.charAt(r0);
        if (r0 != r5) goto L_0x009e;
        r0 = r2 + -1;
        r7.delete(r0, r2);
        return r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.text.ttml.TtmlNode.cleanUpText(android.text.SpannableStringBuilder):android.text.SpannableStringBuilder");
    }

    public static TtmlNode buildTextNode(String text) {
        return new TtmlNode(null, TtmlRenderUtil.applyTextElementSpacePolicy(text), C.TIME_UNSET, C.TIME_UNSET, null, null, ANONYMOUS_REGION_ID);
    }

    public static TtmlNode buildNode(String tag, long startTimeUs, long endTimeUs, TtmlStyle style, String[] styleIds, String regionId) {
        return new TtmlNode(tag, null, startTimeUs, endTimeUs, style, styleIds, regionId);
    }

    private TtmlNode(String tag, String text, long startTimeUs, long endTimeUs, TtmlStyle style, String[] styleIds, String regionId) {
        this.tag = tag;
        this.text = text;
        this.style = style;
        this.styleIds = styleIds;
        this.isTextNode = text != null;
        this.startTimeUs = startTimeUs;
        this.endTimeUs = endTimeUs;
        this.regionId = (String) Assertions.checkNotNull(regionId);
        this.nodeStartsByRegion = new HashMap();
        this.nodeEndsByRegion = new HashMap();
    }

    public boolean isActive(long timeUs) {
        return (this.startTimeUs == C.TIME_UNSET && this.endTimeUs == C.TIME_UNSET) || ((this.startTimeUs <= timeUs && this.endTimeUs == C.TIME_UNSET) || ((this.startTimeUs == C.TIME_UNSET && timeUs < this.endTimeUs) || (this.startTimeUs <= timeUs && timeUs < this.endTimeUs)));
    }

    public void addChild(TtmlNode child) {
        if (this.children == null) {
            this.children = new ArrayList();
        }
        this.children.add(child);
    }

    public TtmlNode getChild(int index) {
        if (this.children != null) {
            return (TtmlNode) this.children.get(index);
        }
        throw new IndexOutOfBoundsException();
    }

    public int getChildCount() {
        return this.children == null ? 0 : this.children.size();
    }

    public long[] getEventTimesUs() {
        TreeSet<Long> eventTimeSet = new TreeSet();
        getEventTimes(eventTimeSet, false);
        long[] eventTimes = new long[eventTimeSet.size()];
        int i = 0;
        Iterator it = eventTimeSet.iterator();
        while (it.hasNext()) {
            int i2 = i + 1;
            eventTimes[i] = ((Long) it.next()).longValue();
            i = i2;
        }
        return eventTimes;
    }

    private void getEventTimes(TreeSet<Long> out, boolean descendsPNode) {
        boolean isPNode = TAG_P.equals(this.tag);
        if (descendsPNode || isPNode) {
            if (this.startTimeUs != C.TIME_UNSET) {
                out.add(Long.valueOf(this.startTimeUs));
            }
            if (this.endTimeUs != C.TIME_UNSET) {
                out.add(Long.valueOf(this.endTimeUs));
            }
        }
        if (this.children != null) {
            for (int i = 0; i < this.children.size(); i++) {
                boolean z;
                TtmlNode ttmlNode = (TtmlNode) this.children.get(i);
                if (!descendsPNode) {
                    if (!isPNode) {
                        z = false;
                        ttmlNode.getEventTimes(out, z);
                    }
                }
                z = true;
                ttmlNode.getEventTimes(out, z);
            }
        }
    }

    public String[] getStyleIds() {
        return this.styleIds;
    }

    public List<Cue> getCues(long timeUs, Map<String, TtmlStyle> globalStyles, Map<String, TtmlRegion> regionMap) {
        TreeMap<String, SpannableStringBuilder> regionOutputs = new TreeMap();
        traverseForText(timeUs, false, this.regionId, regionOutputs);
        traverseForStyle(globalStyles, regionOutputs);
        List<Cue> cues = new ArrayList();
        for (Entry<String, SpannableStringBuilder> entry : regionOutputs.entrySet()) {
            TtmlRegion region = (TtmlRegion) regionMap.get(entry.getKey());
            Cue cue = r8;
            Cue cue2 = new Cue(cleanUpText((SpannableStringBuilder) entry.getValue()), null, region.line, region.lineType, region.lineAnchor, region.position, Integer.MIN_VALUE, region.width);
            cues.add(cue);
        }
        Map<String, TtmlRegion> map = regionMap;
        return cues;
    }

    private void traverseForText(long timeUs, boolean descendsPNode, String inheritedRegion, Map<String, SpannableStringBuilder> regionOutputs) {
        this.nodeStartsByRegion.clear();
        this.nodeEndsByRegion.clear();
        String resolvedRegionId = this.regionId;
        if (ANONYMOUS_REGION_ID.equals(resolvedRegionId)) {
            resolvedRegionId = inheritedRegion;
        }
        if (this.isTextNode && descendsPNode) {
            getRegionOutput(resolvedRegionId, regionOutputs).append(this.text);
        } else if (TAG_BR.equals(this.tag) && descendsPNode) {
            getRegionOutput(resolvedRegionId, regionOutputs).append('\n');
        } else if (!TAG_METADATA.equals(this.tag)) {
            if (isActive(timeUs)) {
                boolean isPNode = TAG_P.equals(this.tag);
                for (Entry<String, SpannableStringBuilder> entry : regionOutputs.entrySet()) {
                    this.nodeStartsByRegion.put(entry.getKey(), Integer.valueOf(((SpannableStringBuilder) entry.getValue()).length()));
                }
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= getChildCount()) {
                        break;
                    }
                    boolean z;
                    TtmlNode child = getChild(i2);
                    if (!descendsPNode) {
                        if (!isPNode) {
                            z = false;
                            child.traverseForText(timeUs, z, resolvedRegionId, regionOutputs);
                            i = i2 + 1;
                        }
                    }
                    z = true;
                    child.traverseForText(timeUs, z, resolvedRegionId, regionOutputs);
                    i = i2 + 1;
                }
                if (isPNode) {
                    TtmlRenderUtil.endParagraph(getRegionOutput(resolvedRegionId, regionOutputs));
                }
                for (Entry<String, SpannableStringBuilder> entry2 : regionOutputs.entrySet()) {
                    this.nodeEndsByRegion.put(entry2.getKey(), Integer.valueOf(((SpannableStringBuilder) entry2.getValue()).length()));
                }
            }
        }
    }

    private static SpannableStringBuilder getRegionOutput(String resolvedRegionId, Map<String, SpannableStringBuilder> regionOutputs) {
        if (!regionOutputs.containsKey(resolvedRegionId)) {
            regionOutputs.put(resolvedRegionId, new SpannableStringBuilder());
        }
        return (SpannableStringBuilder) regionOutputs.get(resolvedRegionId);
    }

    private void traverseForStyle(Map<String, TtmlStyle> globalStyles, Map<String, SpannableStringBuilder> regionOutputs) {
        for (Entry<String, Integer> entry : this.nodeEndsByRegion.entrySet()) {
            String regionId = (String) entry.getKey();
            int i = 0;
            applyStyleToOutput(globalStyles, (SpannableStringBuilder) regionOutputs.get(regionId), this.nodeStartsByRegion.containsKey(regionId) ? ((Integer) this.nodeStartsByRegion.get(regionId)).intValue() : 0, ((Integer) entry.getValue()).intValue());
            while (i < getChildCount()) {
                getChild(i).traverseForStyle(globalStyles, regionOutputs);
                i++;
            }
        }
    }

    private void applyStyleToOutput(Map<String, TtmlStyle> globalStyles, SpannableStringBuilder regionOutput, int start, int end) {
        if (start != end) {
            TtmlStyle resolvedStyle = TtmlRenderUtil.resolveStyle(this.style, this.styleIds, globalStyles);
            if (resolvedStyle != null) {
                TtmlRenderUtil.applyStylesToSpan(regionOutput, start, end, resolvedStyle);
            }
        }
    }
}
