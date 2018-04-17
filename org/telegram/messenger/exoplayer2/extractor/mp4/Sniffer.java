package org.telegram.messenger.exoplayer2.extractor.mp4;

import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;
import java.io.IOException;
import org.telegram.messenger.exoplayer2.extractor.ExtractorInput;
import org.telegram.messenger.exoplayer2.util.Util;

final class Sniffer {
    private static final int[] COMPATIBLE_BRANDS = new int[]{Util.getIntegerCodeForString("isom"), Util.getIntegerCodeForString("iso2"), Util.getIntegerCodeForString("iso3"), Util.getIntegerCodeForString("iso4"), Util.getIntegerCodeForString("iso5"), Util.getIntegerCodeForString("iso6"), Util.getIntegerCodeForString(VisualSampleEntry.TYPE3), Util.getIntegerCodeForString(VisualSampleEntry.TYPE6), Util.getIntegerCodeForString(VisualSampleEntry.TYPE7), Util.getIntegerCodeForString("mp41"), Util.getIntegerCodeForString("mp42"), Util.getIntegerCodeForString("3g2a"), Util.getIntegerCodeForString("3g2b"), Util.getIntegerCodeForString("3gr6"), Util.getIntegerCodeForString("3gs6"), Util.getIntegerCodeForString("3ge6"), Util.getIntegerCodeForString("3gg6"), Util.getIntegerCodeForString("M4V "), Util.getIntegerCodeForString("M4A "), Util.getIntegerCodeForString("f4v "), Util.getIntegerCodeForString("kddi"), Util.getIntegerCodeForString("M4VP"), Util.getIntegerCodeForString("qt  "), Util.getIntegerCodeForString("MSNV")};
    private static final int SEARCH_LENGTH = 4096;

    private static boolean sniffInternal(org.telegram.messenger.exoplayer2.extractor.ExtractorInput r1, boolean r2) throws java.io.IOException, java.lang.InterruptedException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.extractor.mp4.Sniffer.sniffInternal(org.telegram.messenger.exoplayer2.extractor.ExtractorInput, boolean):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r25;
        r1 = r25.getLength();
        r3 = -1;
        r5 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
        r6 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        if (r5 == 0) goto L_0x0015;
    L_0x000e:
        r5 = (r1 > r6 ? 1 : (r1 == r6 ? 0 : -1));
        if (r5 <= 0) goto L_0x0013;
    L_0x0012:
        goto L_0x0015;
    L_0x0013:
        r6 = r1;
    L_0x0015:
        r5 = (int) r6;
        r6 = new org.telegram.messenger.exoplayer2.util.ParsableByteArray;
        r7 = 64;
        r6.<init>(r7);
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = r8;
        r8 = r7;
        r7 = r9;
    L_0x0023:
        if (r8 >= r5) goto L_0x00e0;
    L_0x0025:
        r12 = 8;
        r6.reset(r12);
        r13 = r6.data;
        r0.peekFully(r13, r9, r12);
        r13 = r6.readUnsignedInt();
        r15 = r6.readInt();
        r16 = 1;
        r18 = (r13 > r16 ? 1 : (r13 == r16 ? 0 : -1));
        r11 = 8;
        if (r18 != 0) goto L_0x0050;
    L_0x003f:
        r12 = 16;
        r9 = r6.data;
        r0.peekFully(r9, r11, r11);
        r9 = 16;
        r6.setLimit(r9);
        r13 = r6.readUnsignedLongToLong();
        goto L_0x0067;
    L_0x0050:
        r16 = 0;
        r9 = (r13 > r16 ? 1 : (r13 == r16 ? 0 : -1));
        if (r9 != 0) goto L_0x0067;
    L_0x0056:
        r16 = r25.getLength();
        r9 = (r16 > r3 ? 1 : (r16 == r3 ? 0 : -1));
        if (r9 == 0) goto L_0x0067;
    L_0x005e:
        r19 = r25.getPosition();
        r21 = r16 - r19;
        r3 = (long) r12;
        r13 = r21 + r3;
    L_0x0067:
        r3 = (long) r12;
        r9 = (r13 > r3 ? 1 : (r13 == r3 ? 0 : -1));
        if (r9 >= 0) goto L_0x006e;
    L_0x006c:
        r3 = 0;
        return r3;
    L_0x006e:
        r8 = r8 + r12;
        r3 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_moov;
        if (r15 != r3) goto L_0x0078;
    L_0x0074:
        r3 = -1;
        r9 = 0;
        goto L_0x0023;
    L_0x0078:
        r3 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_moof;
        if (r15 == r3) goto L_0x00da;
    L_0x007c:
        r3 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_mvex;
        if (r15 != r3) goto L_0x0085;
    L_0x0080:
        r23 = r12;
        r9 = 0;
        r11 = 1;
        goto L_0x00de;
    L_0x0085:
        r3 = (long) r8;
        r16 = r3 + r13;
        r3 = (long) r12;
        r19 = r16 - r3;
        r3 = (long) r5;
        r9 = (r19 > r3 ? 1 : (r19 == r3 ? 0 : -1));
        if (r9 < 0) goto L_0x0093;
    L_0x0091:
        r9 = 0;
        goto L_0x00e0;
    L_0x0093:
        r3 = (long) r12;
        r23 = r12;
        r11 = r13 - r3;
        r3 = (int) r11;
        r8 = r8 + r3;
        r4 = org.telegram.messenger.exoplayer2.extractor.mp4.Atom.TYPE_ftyp;
        if (r15 != r4) goto L_0x00cf;
    L_0x009e:
        r4 = 8;
        if (r3 >= r4) goto L_0x00a4;
    L_0x00a2:
        r4 = 0;
        return r4;
    L_0x00a4:
        r4 = 0;
        r6.reset(r3);
        r9 = r6.data;
        r0.peekFully(r9, r4, r3);
        r4 = r3 / 4;
        r9 = 0;
    L_0x00b0:
        if (r9 >= r4) goto L_0x00c9;
    L_0x00b2:
        r11 = 1;
        if (r9 != r11) goto L_0x00ba;
    L_0x00b5:
        r12 = 4;
        r6.skipBytes(r12);
        goto L_0x00c6;
    L_0x00ba:
        r12 = r6.readInt();
        r12 = isCompatibleBrand(r12);
        if (r12 == 0) goto L_0x00c6;
    L_0x00c4:
        r10 = 1;
        goto L_0x00c9;
    L_0x00c6:
        r9 = r9 + 1;
        goto L_0x00b0;
    L_0x00c9:
        if (r10 != 0) goto L_0x00cd;
    L_0x00cb:
        r9 = 0;
        return r9;
    L_0x00cd:
        r9 = 0;
        goto L_0x00d5;
    L_0x00cf:
        r9 = 0;
        if (r3 == 0) goto L_0x00d5;
        r0.advancePeekPosition(r3);
        r3 = -1;
        goto L_0x0023;
    L_0x00da:
        r23 = r12;
        r9 = 0;
        r11 = 1;
    L_0x00de:
        r7 = 1;
        goto L_0x00e1;
    L_0x00e0:
        r11 = 1;
        if (r10 == 0) goto L_0x00e9;
        r3 = r26;
        if (r3 != r7) goto L_0x00eb;
        r9 = r11;
        goto L_0x00eb;
        r3 = r26;
        return r9;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.extractor.mp4.Sniffer.sniffInternal(org.telegram.messenger.exoplayer2.extractor.ExtractorInput, boolean):boolean");
    }

    public static boolean sniffFragmented(ExtractorInput input) throws IOException, InterruptedException {
        return sniffInternal(input, true);
    }

    public static boolean sniffUnfragmented(ExtractorInput input) throws IOException, InterruptedException {
        return sniffInternal(input, false);
    }

    private static boolean isCompatibleBrand(int brand) {
        if ((brand >>> 8) == Util.getIntegerCodeForString("3gp")) {
            return true;
        }
        for (int compatibleBrand : COMPATIBLE_BRANDS) {
            if (compatibleBrand == brand) {
                return true;
            }
        }
        return false;
    }

    private Sniffer() {
    }
}
