package net.hockeyapp.android.utils;

import java.io.UnsupportedEncodingException;
import org.telegram.messenger.exoplayer2.C;

public class Base64 {

    static abstract class Coder {
        public int op;
        public byte[] output;

        Coder() {
        }
    }

    static class Encoder extends Coder {
        private static final byte[] ENCODE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
        private static final byte[] ENCODE_WEBSAFE = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        private final byte[] alphabet;
        private int count;
        public final boolean do_cr;
        public final boolean do_newline;
        public final boolean do_padding;
        private final byte[] tail;
        int tailLen;

        public boolean process(byte[] r1, int r2, int r3, boolean r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.utils.Base64.Encoder.process(byte[], int, int, boolean):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = r17;
            r1 = r0.alphabet;
            r2 = r0.output;
            r3 = 0;
            r4 = r0.count;
            r5 = r19;
            r6 = r20 + r19;
            r7 = -1;
            r8 = r0.tailLen;
            r9 = 0;
            r10 = 1;
            switch(r8) {
                case 0: goto L_0x0057;
                case 1: goto L_0x0037;
                case 2: goto L_0x0016;
                default: goto L_0x0015;
            };
            goto L_0x0058;
            r8 = r5 + 1;
            if (r8 > r6) goto L_0x0058;
            r8 = r0.tail;
            r8 = r8[r9];
            r8 = r8 & 255;
            r8 = r8 << 16;
            r11 = r0.tail;
            r11 = r11[r10];
            r11 = r11 & 255;
            r11 = r11 << 8;
            r8 = r8 | r11;
            r11 = r5 + 1;
            r5 = r18[r5];
            r5 = r5 & 255;
            r7 = r8 | r5;
            r0.tailLen = r9;
            r8 = r11;
            goto L_0x0059;
            r8 = r5 + 2;
            if (r8 > r6) goto L_0x0058;
            r8 = r0.tail;
            r8 = r8[r9];
            r8 = r8 & 255;
            r8 = r8 << 16;
            r11 = r5 + 1;
            r5 = r18[r5];
            r5 = r5 & 255;
            r5 = r5 << 8;
            r5 = r5 | r8;
            r8 = r11 + 1;
            r11 = r18[r11];
            r11 = r11 & 255;
            r7 = r5 | r11;
            r0.tailLen = r9;
            goto L_0x0059;
            r8 = r5;
            r5 = -1;
            r9 = 13;
            r11 = 10;
            if (r7 == r5) goto L_0x009a;
            r5 = r3 + 1;
            r12 = r7 >> 18;
            r12 = r12 & 63;
            r12 = r1[r12];
            r2[r3] = r12;
            r3 = r5 + 1;
            r12 = r7 >> 12;
            r12 = r12 & 63;
            r12 = r1[r12];
            r2[r5] = r12;
            r5 = r3 + 1;
            r12 = r7 >> 6;
            r12 = r12 & 63;
            r12 = r1[r12];
            r2[r3] = r12;
            r3 = r5 + 1;
            r12 = r7 & 63;
            r12 = r1[r12];
            r2[r5] = r12;
            r4 = r4 + -1;
            if (r4 != 0) goto L_0x009a;
            r5 = r0.do_cr;
            if (r5 == 0) goto L_0x0093;
            r5 = r3 + 1;
            r2[r3] = r9;
            r3 = r5;
            r5 = r3 + 1;
            r2[r3] = r11;
            r4 = 19;
            r3 = r5;
            r5 = r8 + 3;
            if (r5 > r6) goto L_0x00f1;
            r5 = r18[r8];
            r5 = r5 & 255;
            r5 = r5 << 16;
            r12 = r8 + 1;
            r12 = r18[r12];
            r12 = r12 & 255;
            r12 = r12 << 8;
            r5 = r5 | r12;
            r12 = r8 + 2;
            r12 = r18[r12];
            r12 = r12 & 255;
            r7 = r5 | r12;
            r5 = r7 >> 18;
            r5 = r5 & 63;
            r5 = r1[r5];
            r2[r3] = r5;
            r5 = r3 + 1;
            r12 = r7 >> 12;
            r12 = r12 & 63;
            r12 = r1[r12];
            r2[r5] = r12;
            r5 = r3 + 2;
            r12 = r7 >> 6;
            r12 = r12 & 63;
            r12 = r1[r12];
            r2[r5] = r12;
            r5 = r3 + 3;
            r12 = r7 & 63;
            r12 = r1[r12];
            r2[r5] = r12;
            r8 = r8 + 3;
            r3 = r3 + 4;
            r4 = r4 + -1;
            if (r4 != 0) goto L_0x009a;
            r5 = r0.do_cr;
            if (r5 == 0) goto L_0x00ea;
            r5 = r3 + 1;
            r2[r3] = r9;
            r3 = r5;
            r5 = r3 + 1;
            r2[r3] = r11;
            r4 = 19;
            goto L_0x0099;
            if (r21 == 0) goto L_0x01f1;
            r12 = r0.tailLen;
            r12 = r8 - r12;
            r13 = r6 + -1;
            r14 = 61;
            if (r12 != r13) goto L_0x014d;
            r12 = 0;
            r13 = r0.tailLen;
            if (r13 <= 0) goto L_0x010c;
            r13 = r0.tail;
            r15 = r12 + 1;
            r12 = r13[r12];
            r13 = r8;
            r8 = r12;
            r12 = r15;
            goto L_0x0110;
            r13 = r8 + 1;
            r8 = r18[r8];
            r8 = r8 & 255;
            r7 = r8 << 4;
            r8 = r0.tailLen;
            r8 = r8 - r12;
            r0.tailLen = r8;
            r8 = r3 + 1;
            r15 = r7 >> 6;
            r15 = r15 & 63;
            r15 = r1[r15];
            r2[r3] = r15;
            r3 = r8 + 1;
            r15 = r7 & 63;
            r15 = r1[r15];
            r2[r8] = r15;
            r8 = r0.do_padding;
            if (r8 == 0) goto L_0x0137;
            r8 = r3 + 1;
            r2[r3] = r14;
            r3 = r8 + 1;
            r2[r8] = r14;
            r8 = r0.do_newline;
            if (r8 == 0) goto L_0x0149;
            r8 = r0.do_cr;
            if (r8 == 0) goto L_0x0144;
            r8 = r3 + 1;
            r2[r3] = r9;
            goto L_0x0145;
            r8 = r3;
            r3 = r8 + 1;
            r2[r8] = r11;
            r8 = r13;
            goto L_0x01dc;
            r12 = r0.tailLen;
            r12 = r8 - r12;
            r13 = r6 + -2;
            if (r12 != r13) goto L_0x01c4;
            r12 = 0;
            r13 = r0.tailLen;
            if (r13 <= r10) goto L_0x0164;
            r13 = r0.tail;
            r15 = r12 + 1;
            r12 = r13[r12];
            r13 = r8;
            r8 = r12;
            r12 = r15;
            goto L_0x0168;
            r13 = r8 + 1;
            r8 = r18[r8];
            r8 = r8 & 255;
            r8 = r8 << r11;
            r10 = r0.tailLen;
            if (r10 <= 0) goto L_0x0177;
            r10 = r0.tail;
            r15 = r12 + 1;
            r10 = r10[r12];
            r12 = r15;
            goto L_0x0180;
            r10 = r13 + 1;
            r13 = r18[r13];
            r16 = r13;
            r13 = r10;
            r10 = r16;
            r10 = r10 & 255;
            r10 = r10 << 2;
            r7 = r8 | r10;
            r8 = r0.tailLen;
            r8 = r8 - r12;
            r0.tailLen = r8;
            r8 = r3 + 1;
            r10 = r7 >> 12;
            r10 = r10 & 63;
            r10 = r1[r10];
            r2[r3] = r10;
            r3 = r8 + 1;
            r10 = r7 >> 6;
            r10 = r10 & 63;
            r10 = r1[r10];
            r2[r8] = r10;
            r8 = r3 + 1;
            r10 = r7 & 63;
            r10 = r1[r10];
            r2[r3] = r10;
            r3 = r0.do_padding;
            if (r3 == 0) goto L_0x01b0;
            r3 = r8 + 1;
            r2[r8] = r14;
            r8 = r3;
            r3 = r0.do_newline;
            if (r3 == 0) goto L_0x01c2;
            r3 = r0.do_cr;
            if (r3 == 0) goto L_0x01bd;
            r3 = r8 + 1;
            r2[r8] = r9;
            r8 = r3;
            r3 = r8 + 1;
            r2[r8] = r11;
            r8 = r3;
            r3 = r8;
            goto L_0x014a;
            r10 = r0.do_newline;
            if (r10 == 0) goto L_0x01dc;
            if (r3 <= 0) goto L_0x01dc;
            r10 = 19;
            if (r4 == r10) goto L_0x01dc;
            r10 = r0.do_cr;
            if (r10 == 0) goto L_0x01d7;
            r10 = r3 + 1;
            r2[r3] = r9;
            goto L_0x01d8;
            r10 = r3;
            r3 = r10 + 1;
            r2[r10] = r11;
            r9 = r0.tailLen;
            if (r9 == 0) goto L_0x01e7;
            r9 = "BASE64";
            r10 = "Error during encoding";
            net.hockeyapp.android.utils.HockeyLog.error(r9, r10);
            if (r8 == r6) goto L_0x0220;
            r9 = "BASE64";
            r10 = "Error during encoding";
            net.hockeyapp.android.utils.HockeyLog.error(r9, r10);
            goto L_0x0220;
            r9 = r6 + -1;
            if (r8 != r9) goto L_0x0202;
            r9 = r0.tail;
            r10 = r0.tailLen;
            r11 = r10 + 1;
            r0.tailLen = r11;
            r11 = r18[r8];
            r9[r10] = r11;
            goto L_0x0220;
            r9 = r6 + -2;
            if (r8 != r9) goto L_0x0220;
            r9 = r0.tail;
            r10 = r0.tailLen;
            r11 = r10 + 1;
            r0.tailLen = r11;
            r11 = r18[r8];
            r9[r10] = r11;
            r9 = r0.tail;
            r10 = r0.tailLen;
            r11 = r10 + 1;
            r0.tailLen = r11;
            r11 = r8 + 1;
            r11 = r18[r11];
            r9[r10] = r11;
            r0.op = r3;
            r0.count = r4;
            r9 = 1;
            return r9;
            */
            throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.utils.Base64.Encoder.process(byte[], int, int, boolean):boolean");
        }

        public Encoder(int flags, byte[] output) {
            this.output = output;
            boolean z = true;
            this.do_padding = (flags & 1) == 0;
            this.do_newline = (flags & 2) == 0;
            if ((flags & 4) == 0) {
                z = false;
            }
            this.do_cr = z;
            this.alphabet = (flags & 8) == 0 ? ENCODE : ENCODE_WEBSAFE;
            this.tail = new byte[2];
            this.tailLen = 0;
            this.count = this.do_newline ? 19 : -1;
        }
    }

    public static byte[] encode(byte[] r1, int r2, int r3, int r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: net.hockeyapp.android.utils.Base64.encode(byte[], int, int, int):byte[]
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = new net.hockeyapp.android.utils.Base64$Encoder;
        r1 = 0;
        r0.<init>(r8, r1);
        r1 = r7 / 3;
        r1 = r1 * 4;
        r2 = r0.do_padding;
        if (r2 == 0) goto L_0x0015;
    L_0x000e:
        r2 = r7 % 3;
        if (r2 <= 0) goto L_0x0022;
    L_0x0012:
        r1 = r1 + 4;
        goto L_0x0022;
    L_0x0015:
        r2 = r7 % 3;
        switch(r2) {
            case 0: goto L_0x0021;
            case 1: goto L_0x001e;
            case 2: goto L_0x001b;
            default: goto L_0x001a;
        };
        goto L_0x0022;
        r1 = r1 + 3;
        goto L_0x0022;
        r1 = r1 + 2;
        goto L_0x0022;
    L_0x0022:
        r2 = r0.do_newline;
        r3 = 1;
        if (r2 == 0) goto L_0x0037;
        if (r7 <= 0) goto L_0x0037;
        r2 = r7 + -1;
        r2 = r2 / 57;
        r2 = r2 + r3;
        r4 = r0.do_cr;
        if (r4 == 0) goto L_0x0034;
        r4 = 2;
        goto L_0x0035;
        r4 = r3;
        r2 = r2 * r4;
        r1 = r1 + r2;
        r2 = new byte[r1];
        r0.output = r2;
        r0.process(r5, r6, r7, r3);
        r2 = r0.op;
        if (r2 == r1) goto L_0x0048;
        r2 = new java.lang.AssertionError;
        r2.<init>();
        throw r2;
        r2 = r0.output;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: net.hockeyapp.android.utils.Base64.encode(byte[], int, int, int):byte[]");
    }

    public static String encodeToString(byte[] input, int flags) {
        try {
            return new String(encode(input, flags), C.ASCII_NAME);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] encode(byte[] input, int flags) {
        return encode(input, 0, input.length, flags);
    }
}
