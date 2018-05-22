package com.google.android.gms.common.util;

public class Hex {
    private static final char[] zzaaa = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] zzzz = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String bytesToStringLowercase(byte[] bArr) {
        char[] cArr = new char[(bArr.length << 1)];
        int i = 0;
        int i2 = 0;
        while (i < bArr.length) {
            int i3 = bArr[i] & 255;
            int i4 = i2 + 1;
            cArr[i2] = zzaaa[i3 >>> 4];
            int i5 = i4 + 1;
            cArr[i4] = zzaaa[i3 & 15];
            i++;
            i2 = i5;
        }
        return new String(cArr);
    }
}
