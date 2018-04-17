package org.telegram.messenger.exoplayer2.ext.ffmpeg;

import org.telegram.messenger.exoplayer2.ExoPlayerLibraryInfo;

public final class FfmpegLibrary {
    private static native String ffmpegGetVersion();

    private static native boolean ffmpegHasDecoder(String str);

    static {
        ExoPlayerLibraryInfo.registerModule("goog.exo.ffmpeg");
    }

    private FfmpegLibrary() {
    }

    public static String getVersion() {
        return ffmpegGetVersion();
    }

    public static boolean supportsFormat(String mimeType) {
        String codecName = getCodecName(mimeType);
        return codecName != null && ffmpegHasDecoder(codecName);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.lang.String getCodecName(java.lang.String r1) {
        /*
        r0 = r1.hashCode();
        switch(r0) {
            case -1606874997: goto L_0x00a0;
            case -1095064472: goto L_0x0096;
            case -1003765268: goto L_0x008b;
            case -432837260: goto L_0x0081;
            case -432837259: goto L_0x0077;
            case -53558318: goto L_0x006d;
            case 187078296: goto L_0x0063;
            case 1503095341: goto L_0x0058;
            case 1504470054: goto L_0x004d;
            case 1504578661: goto L_0x0043;
            case 1504619009: goto L_0x0037;
            case 1504831518: goto L_0x002c;
            case 1504891608: goto L_0x0020;
            case 1505942594: goto L_0x0014;
            case 1556697186: goto L_0x0009;
            default: goto L_0x0007;
        };
    L_0x0007:
        goto L_0x00ab;
    L_0x0009:
        r0 = "audio/true-hd";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x0011:
        r0 = 6;
        goto L_0x00ac;
    L_0x0014:
        r0 = "audio/vnd.dts.hd";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x001c:
        r0 = 8;
        goto L_0x00ac;
    L_0x0020:
        r0 = "audio/opus";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x0028:
        r0 = 10;
        goto L_0x00ac;
    L_0x002c:
        r0 = "audio/mpeg";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x0034:
        r0 = 1;
        goto L_0x00ac;
    L_0x0037:
        r0 = "audio/flac";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x003f:
        r0 = 13;
        goto L_0x00ac;
    L_0x0043:
        r0 = "audio/eac3";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x004b:
        r0 = 5;
        goto L_0x00ac;
    L_0x004d:
        r0 = "audio/alac";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x0055:
        r0 = 14;
        goto L_0x00ac;
    L_0x0058:
        r0 = "audio/3gpp";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x0060:
        r0 = 11;
        goto L_0x00ac;
    L_0x0063:
        r0 = "audio/ac3";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x006b:
        r0 = 4;
        goto L_0x00ac;
    L_0x006d:
        r0 = "audio/mp4a-latm";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x0075:
        r0 = 0;
        goto L_0x00ac;
    L_0x0077:
        r0 = "audio/mpeg-L2";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x007f:
        r0 = 3;
        goto L_0x00ac;
    L_0x0081:
        r0 = "audio/mpeg-L1";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x0089:
        r0 = 2;
        goto L_0x00ac;
    L_0x008b:
        r0 = "audio/vorbis";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x0093:
        r0 = 9;
        goto L_0x00ac;
    L_0x0096:
        r0 = "audio/vnd.dts";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x009e:
        r0 = 7;
        goto L_0x00ac;
    L_0x00a0:
        r0 = "audio/amr-wb";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x00ab;
    L_0x00a8:
        r0 = 12;
        goto L_0x00ac;
    L_0x00ab:
        r0 = -1;
    L_0x00ac:
        switch(r0) {
            case 0: goto L_0x00d2;
            case 1: goto L_0x00cf;
            case 2: goto L_0x00cf;
            case 3: goto L_0x00cf;
            case 4: goto L_0x00cc;
            case 5: goto L_0x00c9;
            case 6: goto L_0x00c6;
            case 7: goto L_0x00c3;
            case 8: goto L_0x00c3;
            case 9: goto L_0x00c0;
            case 10: goto L_0x00bd;
            case 11: goto L_0x00ba;
            case 12: goto L_0x00b7;
            case 13: goto L_0x00b4;
            case 14: goto L_0x00b1;
            default: goto L_0x00af;
        };
    L_0x00af:
        r0 = 0;
        return r0;
    L_0x00b1:
        r0 = "alac";
        return r0;
    L_0x00b4:
        r0 = "flac";
        return r0;
    L_0x00b7:
        r0 = "amrwb";
        return r0;
    L_0x00ba:
        r0 = "amrnb";
        return r0;
    L_0x00bd:
        r0 = "opus";
        return r0;
    L_0x00c0:
        r0 = "vorbis";
        return r0;
    L_0x00c3:
        r0 = "dca";
        return r0;
    L_0x00c6:
        r0 = "truehd";
        return r0;
    L_0x00c9:
        r0 = "eac3";
        return r0;
    L_0x00cc:
        r0 = "ac3";
        return r0;
    L_0x00cf:
        r0 = "mp3";
        return r0;
    L_0x00d2:
        r0 = "aac";
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.ext.ffmpeg.FfmpegLibrary.getCodecName(java.lang.String):java.lang.String");
    }
}
