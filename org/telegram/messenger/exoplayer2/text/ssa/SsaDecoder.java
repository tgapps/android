package org.telegram.messenger.exoplayer2.text.ssa;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.text.Cue;
import org.telegram.messenger.exoplayer2.text.SimpleSubtitleDecoder;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.LongArray;
import org.telegram.messenger.exoplayer2.util.ParsableByteArray;

public final class SsaDecoder extends SimpleSubtitleDecoder {
    private static final String DIALOGUE_LINE_PREFIX = "Dialogue: ";
    private static final String FORMAT_LINE_PREFIX = "Format: ";
    private static final Pattern SSA_TIMECODE_PATTERN = Pattern.compile("(?:(\\d+):)?(\\d+):(\\d+)(?::|\\.)(\\d+)");
    private static final String TAG = "SsaDecoder";
    private int formatEndIndex;
    private int formatKeyCount;
    private int formatStartIndex;
    private int formatTextIndex;
    private final boolean haveInitializationData;

    public SsaDecoder() {
        this(null);
    }

    public SsaDecoder(List<byte[]> initializationData) {
        super(TAG);
        if (initializationData == null || initializationData.isEmpty()) {
            this.haveInitializationData = false;
            return;
        }
        this.haveInitializationData = true;
        String formatLine = new String((byte[]) initializationData.get(0));
        Assertions.checkArgument(formatLine.startsWith(FORMAT_LINE_PREFIX));
        parseFormatLine(formatLine);
        parseHeader(new ParsableByteArray((byte[]) initializationData.get(1)));
    }

    protected SsaSubtitle decode(byte[] bytes, int length, boolean reset) {
        ArrayList<Cue> cues = new ArrayList();
        LongArray cueTimesUs = new LongArray();
        ParsableByteArray data = new ParsableByteArray(bytes, length);
        if (!this.haveInitializationData) {
            parseHeader(data);
        }
        parseEventBody(data, cues, cueTimesUs);
        Cue[] cuesArray = new Cue[cues.size()];
        cues.toArray(cuesArray);
        return new SsaSubtitle(cuesArray, cueTimesUs.toArray());
    }

    private void parseHeader(ParsableByteArray data) {
        String currentLine;
        do {
            currentLine = data.readLine();
            if (currentLine == null) {
                return;
            }
        } while (!currentLine.startsWith("[Events]"));
    }

    private void parseEventBody(ParsableByteArray data, List<Cue> cues, LongArray cueTimesUs) {
        while (true) {
            String currentLine = data.readLine();
            if (currentLine == null) {
                return;
            }
            if (!this.haveInitializationData && currentLine.startsWith(FORMAT_LINE_PREFIX)) {
                parseFormatLine(currentLine);
            } else if (currentLine.startsWith(DIALOGUE_LINE_PREFIX)) {
                parseDialogueLine(currentLine, cues, cueTimesUs);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseFormatLine(java.lang.String r8) {
        /*
        r7 = this;
        r4 = 0;
        r5 = -1;
        r3 = "Format: ";
        r3 = r3.length();
        r3 = r8.substring(r3);
        r6 = ",";
        r2 = android.text.TextUtils.split(r3, r6);
        r3 = r2.length;
        r7.formatKeyCount = r3;
        r7.formatStartIndex = r5;
        r7.formatEndIndex = r5;
        r7.formatTextIndex = r5;
        r0 = 0;
    L_0x001e:
        r3 = r7.formatKeyCount;
        if (r0 >= r3) goto L_0x0064;
    L_0x0022:
        r3 = r2[r0];
        r3 = r3.trim();
        r1 = org.telegram.messenger.exoplayer2.util.Util.toLowerInvariant(r3);
        r3 = r1.hashCode();
        switch(r3) {
            case 100571: goto L_0x0045;
            case 3556653: goto L_0x0050;
            case 109757538: goto L_0x003a;
            default: goto L_0x0033;
        };
    L_0x0033:
        r3 = r5;
    L_0x0034:
        switch(r3) {
            case 0: goto L_0x005b;
            case 1: goto L_0x005e;
            case 2: goto L_0x0061;
            default: goto L_0x0037;
        };
    L_0x0037:
        r0 = r0 + 1;
        goto L_0x001e;
    L_0x003a:
        r3 = "start";
        r3 = r1.equals(r3);
        if (r3 == 0) goto L_0x0033;
    L_0x0043:
        r3 = r4;
        goto L_0x0034;
    L_0x0045:
        r3 = "end";
        r3 = r1.equals(r3);
        if (r3 == 0) goto L_0x0033;
    L_0x004e:
        r3 = 1;
        goto L_0x0034;
    L_0x0050:
        r3 = "text";
        r3 = r1.equals(r3);
        if (r3 == 0) goto L_0x0033;
    L_0x0059:
        r3 = 2;
        goto L_0x0034;
    L_0x005b:
        r7.formatStartIndex = r0;
        goto L_0x0037;
    L_0x005e:
        r7.formatEndIndex = r0;
        goto L_0x0037;
    L_0x0061:
        r7.formatTextIndex = r0;
        goto L_0x0037;
    L_0x0064:
        r3 = r7.formatStartIndex;
        if (r3 == r5) goto L_0x0070;
    L_0x0068:
        r3 = r7.formatEndIndex;
        if (r3 == r5) goto L_0x0070;
    L_0x006c:
        r3 = r7.formatTextIndex;
        if (r3 != r5) goto L_0x0072;
    L_0x0070:
        r7.formatKeyCount = r4;
    L_0x0072:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.text.ssa.SsaDecoder.parseFormatLine(java.lang.String):void");
    }

    private void parseDialogueLine(String dialogueLine, List<Cue> cues, LongArray cueTimesUs) {
        if (this.formatKeyCount == 0) {
            Log.w(TAG, "Skipping dialogue line before complete format: " + dialogueLine);
            return;
        }
        String[] lineValues = dialogueLine.substring(DIALOGUE_LINE_PREFIX.length()).split(",", this.formatKeyCount);
        if (lineValues.length != this.formatKeyCount) {
            Log.w(TAG, "Skipping dialogue line with fewer columns than format: " + dialogueLine);
            return;
        }
        long startTimeUs = parseTimecodeUs(lineValues[this.formatStartIndex]);
        if (startTimeUs == C.TIME_UNSET) {
            Log.w(TAG, "Skipping invalid timing: " + dialogueLine);
            return;
        }
        long endTimeUs = C.TIME_UNSET;
        String endTimeString = lineValues[this.formatEndIndex];
        if (!endTimeString.trim().isEmpty()) {
            endTimeUs = parseTimecodeUs(endTimeString);
            if (endTimeUs == C.TIME_UNSET) {
                Log.w(TAG, "Skipping invalid timing: " + dialogueLine);
                return;
            }
        }
        cues.add(new Cue(lineValues[this.formatTextIndex].replaceAll("\\{.*?\\}", TtmlNode.ANONYMOUS_REGION_ID).replaceAll("\\\\N", "\n").replaceAll("\\\\n", "\n")));
        cueTimesUs.add(startTimeUs);
        if (endTimeUs != C.TIME_UNSET) {
            cues.add(null);
            cueTimesUs.add(endTimeUs);
        }
    }

    public static long parseTimecodeUs(String timeString) {
        Matcher matcher = SSA_TIMECODE_PATTERN.matcher(timeString);
        if (matcher.matches()) {
            return (((((Long.parseLong(matcher.group(1)) * 60) * 60) * C.MICROS_PER_SECOND) + ((Long.parseLong(matcher.group(2)) * 60) * C.MICROS_PER_SECOND)) + (Long.parseLong(matcher.group(3)) * C.MICROS_PER_SECOND)) + (Long.parseLong(matcher.group(4)) * 10000);
        }
        return C.TIME_UNSET;
    }
}
