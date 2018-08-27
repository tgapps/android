package com.coremedia.iso;

import com.google.android.exoplayer2.C;
import java.io.UnsupportedEncodingException;

public final class Utf8 {
    public static byte[] convert(String s) {
        if (s == null) {
            return null;
        }
        try {
            return s.getBytes(C.UTF8_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
    }

    public static String convert(byte[] b) {
        if (b == null) {
            return null;
        }
        try {
            return new String(b, C.UTF8_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }
    }

    public static int utf8StringLengthInBytes(String utf8) {
        if (utf8 == null) {
            return 0;
        }
        try {
            return utf8.getBytes(C.UTF8_NAME).length;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }
}
