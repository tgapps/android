package com.coremedia.iso;

import java.io.ByteArrayOutputStream;
import org.telegram.messenger.exoplayer2.extractor.ts.PsExtractor;

public class Hex {
    private static final char[] DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String encodeHex(byte[] data) {
        return encodeHex(data, 0);
    }

    public static String encodeHex(byte[] data, int group) {
        int l = data.length;
        char[] out = new char[((l << 1) + (group > 0 ? l / group : 0))];
        int i = 0;
        int j = 0;
        while (i < l) {
            int j2;
            if (group > 0 && i % group == 0 && j > 0) {
                j2 = j + 1;
                out[j] = '-';
                j = j2;
            }
            j2 = j + 1;
            out[j] = DIGITS[(PsExtractor.VIDEO_STREAM_MASK & data[i]) >>> 4];
            j = j2 + 1;
            out[j2] = DIGITS[15 & data[i]];
            i++;
        }
        return new String(out);
    }

    public static byte[] decodeHex(String hexString) {
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        for (int i = 0; i < hexString.length(); i += 2) {
            bas.write(Integer.parseInt(hexString.substring(i, i + 2), 16));
        }
        return bas.toByteArray();
    }
}
