package org.telegram.messenger.audioinfo.mp3;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.messenger.audioinfo.AudioInfo;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;

public class ID3v2Info extends AudioInfo {
    static final Logger LOGGER = Logger.getLogger(ID3v2Info.class.getName());
    private byte coverPictureType;
    private final Level debugLevel;

    static class AttachedPicture {
        static final byte TYPE_COVER_FRONT = (byte) 3;
        static final byte TYPE_OTHER = (byte) 0;
        final String description;
        final byte[] imageData;
        final String imageType;
        final byte type;

        public AttachedPicture(byte type, String description, String imageType, byte[] imageData) {
            this.type = type;
            this.description = description;
            this.imageType = imageType;
            this.imageData = imageData;
        }
    }

    static class CommentOrUnsynchronizedLyrics {
        final String description;
        final String language;
        final String text;

        public CommentOrUnsynchronizedLyrics(String language, String description, String text) {
            this.language = language;
            this.description = description;
            this.text = text;
        }
    }

    void parseFrame(org.telegram.messenger.audioinfo.mp3.ID3v2FrameBody r1) throws java.io.IOException, org.telegram.messenger.audioinfo.mp3.ID3v2Exception {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.audioinfo.mp3.ID3v2Info.parseFrame(org.telegram.messenger.audioinfo.mp3.ID3v2FrameBody):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = LOGGER;
        r1 = r9.debugLevel;
        r0 = r0.isLoggable(r1);
        if (r0 == 0) goto L_0x002a;
    L_0x000a:
        r0 = LOGGER;
        r1 = r9.debugLevel;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Parsing frame: ";
        r2.append(r3);
        r3 = r10.getFrameHeader();
        r3 = r3.getFrameId();
        r2.append(r3);
        r2 = r2.toString();
        r0.log(r1, r2);
    L_0x002a:
        r0 = r10.getFrameHeader();
        r0 = r0.getFrameId();
        r1 = -1;
        r2 = r0.hashCode();
        r3 = 2;
        r4 = 4;
        r5 = 3;
        r6 = 0;
        r7 = 1;
        switch(r2) {
            case 66913: goto L_0x01b2;
            case 79210: goto L_0x01a8;
            case 82815: goto L_0x019e;
            case 82878: goto L_0x0193;
            case 82880: goto L_0x0188;
            case 82881: goto L_0x017e;
            case 82883: goto L_0x0173;
            case 83149: goto L_0x0168;
            case 83253: goto L_0x015d;
            case 83254: goto L_0x0151;
            case 83269: goto L_0x0145;
            case 83341: goto L_0x0139;
            case 83377: goto L_0x012d;
            case 83378: goto L_0x0121;
            case 83552: goto L_0x0115;
            case 84125: goto L_0x0109;
            case 2015625: goto L_0x00fe;
            case 2074380: goto L_0x00f3;
            case 2567331: goto L_0x00e8;
            case 2569298: goto L_0x00dd;
            case 2569357: goto L_0x00d1;
            case 2569358: goto L_0x00c5;
            case 2569360: goto L_0x00b9;
            case 2570401: goto L_0x00ad;
            case 2575250: goto L_0x00a1;
            case 2575251: goto L_0x0095;
            case 2577697: goto L_0x0089;
            case 2581512: goto L_0x007d;
            case 2581513: goto L_0x0071;
            case 2581856: goto L_0x0065;
            case 2583398: goto L_0x0059;
            case 2590194: goto L_0x004d;
            case 2614438: goto L_0x0041;
            default: goto L_0x003f;
        };
    L_0x003f:
        goto L_0x01bb;
    L_0x0041:
        r2 = "USLT";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0049:
        r1 = 32;
        goto L_0x01bb;
    L_0x004d:
        r2 = "TYER";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0055:
        r1 = 30;
        goto L_0x01bb;
    L_0x0059:
        r2 = "TRCK";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0061:
        r1 = 24;
        goto L_0x01bb;
    L_0x0065:
        r2 = "TPOS";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x006d:
        r1 = 22;
        goto L_0x01bb;
    L_0x0071:
        r2 = "TPE2";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0079:
        r1 = 20;
        goto L_0x01bb;
    L_0x007d:
        r2 = "TPE1";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0085:
        r1 = 18;
        goto L_0x01bb;
    L_0x0089:
        r2 = "TLEN";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0091:
        r1 = 16;
        goto L_0x01bb;
    L_0x0095:
        r2 = "TIT2";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x009d:
        r1 = 28;
        goto L_0x01bb;
    L_0x00a1:
        r2 = "TIT1";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x00a9:
        r1 = 26;
        goto L_0x01bb;
    L_0x00ad:
        r2 = "TDRC";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x00b5:
        r1 = 14;
        goto L_0x01bb;
    L_0x00b9:
        r2 = "TCOP";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x00c1:
        r1 = 13;
        goto L_0x01bb;
    L_0x00c5:
        r2 = "TCON";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x00cd:
        r1 = 11;
        goto L_0x01bb;
    L_0x00d1:
        r2 = "TCOM";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x00d9:
        r1 = 9;
        goto L_0x01bb;
    L_0x00dd:
        r2 = "TCMP";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x00e5:
        r1 = 7;
        goto L_0x01bb;
    L_0x00e8:
        r2 = "TALB";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x00f0:
        r1 = 5;
        goto L_0x01bb;
    L_0x00f3:
        r2 = "COMM";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x00fb:
        r1 = r5;
        goto L_0x01bb;
    L_0x00fe:
        r2 = "APIC";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0106:
        r1 = r7;
        goto L_0x01bb;
    L_0x0109:
        r2 = "ULT";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0111:
        r1 = 31;
        goto L_0x01bb;
    L_0x0115:
        r2 = "TYE";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x011d:
        r1 = 29;
        goto L_0x01bb;
    L_0x0121:
        r2 = "TT2";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0129:
        r1 = 27;
        goto L_0x01bb;
    L_0x012d:
        r2 = "TT1";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0135:
        r1 = 25;
        goto L_0x01bb;
    L_0x0139:
        r2 = "TRK";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0141:
        r1 = 23;
        goto L_0x01bb;
    L_0x0145:
        r2 = "TPA";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x014d:
        r1 = 21;
        goto L_0x01bb;
    L_0x0151:
        r2 = "TP2";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0159:
        r1 = 19;
        goto L_0x01bb;
    L_0x015d:
        r2 = "TP1";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0165:
        r1 = 17;
        goto L_0x01bb;
    L_0x0168:
        r2 = "TLE";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0170:
        r1 = 15;
        goto L_0x01bb;
    L_0x0173:
        r2 = "TCR";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x017b:
        r1 = 12;
        goto L_0x01bb;
    L_0x017e:
        r2 = "TCP";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0186:
        r1 = 6;
        goto L_0x01bb;
    L_0x0188:
        r2 = "TCO";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x0190:
        r1 = 10;
        goto L_0x01bb;
    L_0x0193:
        r2 = "TCM";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x019b:
        r1 = 8;
        goto L_0x01bb;
    L_0x019e:
        r2 = "TAL";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x01a6:
        r1 = r4;
        goto L_0x01bb;
    L_0x01a8:
        r2 = "PIC";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x01b0:
        r1 = r6;
        goto L_0x01bb;
    L_0x01b2:
        r2 = "COM";
        r0 = r0.equals(r2);
        if (r0 == 0) goto L_0x01bb;
    L_0x01ba:
        r1 = r3;
    L_0x01bb:
        r0 = 47;
        switch(r1) {
            case 0: goto L_0x047f;
            case 1: goto L_0x047f;
            case 2: goto L_0x0463;
            case 3: goto L_0x0463;
            case 4: goto L_0x045b;
            case 5: goto L_0x045b;
            case 6: goto L_0x044d;
            case 7: goto L_0x044d;
            case 8: goto L_0x0445;
            case 9: goto L_0x0445;
            case 10: goto L_0x03f3;
            case 11: goto L_0x03f3;
            case 12: goto L_0x03eb;
            case 13: goto L_0x03eb;
            case 14: goto L_0x03ad;
            case 15: goto L_0x0378;
            case 16: goto L_0x0378;
            case 17: goto L_0x0370;
            case 18: goto L_0x0370;
            case 19: goto L_0x0368;
            case 20: goto L_0x0368;
            case 21: goto L_0x02c1;
            case 22: goto L_0x02c1;
            case 23: goto L_0x021a;
            case 24: goto L_0x021a;
            case 25: goto L_0x0212;
            case 26: goto L_0x0212;
            case 27: goto L_0x020a;
            case 28: goto L_0x020a;
            case 29: goto L_0x01d0;
            case 30: goto L_0x01d0;
            case 31: goto L_0x01c2;
            case 32: goto L_0x01c2;
            default: goto L_0x01c0;
        };
    L_0x01c0:
        goto L_0x051a;
    L_0x01c2:
        r0 = r9.lyrics;
        if (r0 != 0) goto L_0x051a;
    L_0x01c6:
        r0 = r9.parseCommentOrUnsynchronizedLyricsFrame(r10);
        r0 = r0.text;
        r9.lyrics = r0;
        goto L_0x051a;
    L_0x01d0:
        r0 = r9.parseTextFrame(r10);
        r1 = r0.length();
        if (r1 <= 0) goto L_0x051a;
    L_0x01da:
        r1 = java.lang.Short.valueOf(r0);	 Catch:{ NumberFormatException -> 0x01e5 }
        r1 = r1.shortValue();	 Catch:{ NumberFormatException -> 0x01e5 }
        r9.year = r1;	 Catch:{ NumberFormatException -> 0x01e5 }
        goto L_0x0208;
    L_0x01e5:
        r1 = move-exception;
        r2 = LOGGER;
        r3 = r9.debugLevel;
        r2 = r2.isLoggable(r3);
        if (r2 == 0) goto L_0x0208;
    L_0x01f0:
        r2 = LOGGER;
        r3 = r9.debugLevel;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Could not parse year: ";
        r4.append(r5);
        r4.append(r0);
        r4 = r4.toString();
        r2.log(r3, r4);
    L_0x0208:
        goto L_0x051a;
    L_0x020a:
        r0 = r9.parseTextFrame(r10);
        r9.title = r0;
        goto L_0x051a;
    L_0x0212:
        r0 = r9.parseTextFrame(r10);
        r9.grouping = r0;
        goto L_0x051a;
    L_0x021a:
        r1 = r9.parseTextFrame(r10);
        r2 = r1.length();
        if (r2 <= 0) goto L_0x051a;
    L_0x0224:
        r0 = r1.indexOf(r0);
        if (r0 >= 0) goto L_0x0259;
    L_0x022a:
        r2 = java.lang.Short.valueOf(r1);	 Catch:{ NumberFormatException -> 0x0235 }
        r2 = r2.shortValue();	 Catch:{ NumberFormatException -> 0x0235 }
        r9.track = r2;	 Catch:{ NumberFormatException -> 0x0235 }
        goto L_0x0258;
    L_0x0235:
        r2 = move-exception;
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r3 = r3.isLoggable(r4);
        if (r3 == 0) goto L_0x0258;
    L_0x0240:
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Could not parse track number: ";
        r5.append(r6);
        r5.append(r1);
        r5 = r5.toString();
        r3.log(r4, r5);
    L_0x0258:
        goto L_0x02bf;
    L_0x0259:
        r2 = r1.substring(r6, r0);	 Catch:{ NumberFormatException -> 0x0268 }
        r2 = java.lang.Short.valueOf(r2);	 Catch:{ NumberFormatException -> 0x0268 }
        r2 = r2.shortValue();	 Catch:{ NumberFormatException -> 0x0268 }
        r9.track = r2;	 Catch:{ NumberFormatException -> 0x0268 }
        goto L_0x028b;
    L_0x0268:
        r2 = move-exception;
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r3 = r3.isLoggable(r4);
        if (r3 == 0) goto L_0x028b;
    L_0x0273:
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Could not parse track number: ";
        r5.append(r6);
        r5.append(r1);
        r5 = r5.toString();
        r3.log(r4, r5);
    L_0x028b:
        r2 = r0 + 1;
        r2 = r1.substring(r2);	 Catch:{ NumberFormatException -> 0x029c }
        r2 = java.lang.Short.valueOf(r2);	 Catch:{ NumberFormatException -> 0x029c }
        r2 = r2.shortValue();	 Catch:{ NumberFormatException -> 0x029c }
        r9.tracks = r2;	 Catch:{ NumberFormatException -> 0x029c }
        goto L_0x02bf;
    L_0x029c:
        r2 = move-exception;
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r3 = r3.isLoggable(r4);
        if (r3 == 0) goto L_0x02bf;
    L_0x02a7:
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Could not parse number of tracks: ";
        r5.append(r6);
        r5.append(r1);
        r5 = r5.toString();
        r3.log(r4, r5);
    L_0x02bf:
        goto L_0x051a;
    L_0x02c1:
        r1 = r9.parseTextFrame(r10);
        r2 = r1.length();
        if (r2 <= 0) goto L_0x051a;
    L_0x02cb:
        r0 = r1.indexOf(r0);
        if (r0 >= 0) goto L_0x0300;
    L_0x02d1:
        r2 = java.lang.Short.valueOf(r1);	 Catch:{ NumberFormatException -> 0x02dc }
        r2 = r2.shortValue();	 Catch:{ NumberFormatException -> 0x02dc }
        r9.disc = r2;	 Catch:{ NumberFormatException -> 0x02dc }
        goto L_0x02ff;
    L_0x02dc:
        r2 = move-exception;
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r3 = r3.isLoggable(r4);
        if (r3 == 0) goto L_0x02ff;
    L_0x02e7:
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Could not parse disc number: ";
        r5.append(r6);
        r5.append(r1);
        r5 = r5.toString();
        r3.log(r4, r5);
    L_0x02ff:
        goto L_0x0366;
    L_0x0300:
        r2 = r1.substring(r6, r0);	 Catch:{ NumberFormatException -> 0x030f }
        r2 = java.lang.Short.valueOf(r2);	 Catch:{ NumberFormatException -> 0x030f }
        r2 = r2.shortValue();	 Catch:{ NumberFormatException -> 0x030f }
        r9.disc = r2;	 Catch:{ NumberFormatException -> 0x030f }
        goto L_0x0332;
    L_0x030f:
        r2 = move-exception;
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r3 = r3.isLoggable(r4);
        if (r3 == 0) goto L_0x0332;
    L_0x031a:
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Could not parse disc number: ";
        r5.append(r6);
        r5.append(r1);
        r5 = r5.toString();
        r3.log(r4, r5);
    L_0x0332:
        r2 = r0 + 1;
        r2 = r1.substring(r2);	 Catch:{ NumberFormatException -> 0x0343 }
        r2 = java.lang.Short.valueOf(r2);	 Catch:{ NumberFormatException -> 0x0343 }
        r2 = r2.shortValue();	 Catch:{ NumberFormatException -> 0x0343 }
        r9.discs = r2;	 Catch:{ NumberFormatException -> 0x0343 }
        goto L_0x0366;
    L_0x0343:
        r2 = move-exception;
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r3 = r3.isLoggable(r4);
        if (r3 == 0) goto L_0x0366;
    L_0x034e:
        r3 = LOGGER;
        r4 = r9.debugLevel;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Could not parse number of discs: ";
        r5.append(r6);
        r5.append(r1);
        r5 = r5.toString();
        r3.log(r4, r5);
    L_0x0366:
        goto L_0x051a;
    L_0x0368:
        r0 = r9.parseTextFrame(r10);
        r9.albumArtist = r0;
        goto L_0x051a;
    L_0x0370:
        r0 = r9.parseTextFrame(r10);
        r9.artist = r0;
        goto L_0x051a;
    L_0x0378:
        r0 = r9.parseTextFrame(r10);
        r1 = java.lang.Long.valueOf(r0);	 Catch:{ NumberFormatException -> 0x0388 }
        r1 = r1.longValue();	 Catch:{ NumberFormatException -> 0x0388 }
        r9.duration = r1;	 Catch:{ NumberFormatException -> 0x0388 }
        goto L_0x051a;
    L_0x0388:
        r1 = move-exception;
        r2 = LOGGER;
        r3 = r9.debugLevel;
        r2 = r2.isLoggable(r3);
        if (r2 == 0) goto L_0x03ab;
    L_0x0393:
        r2 = LOGGER;
        r3 = r9.debugLevel;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Could not parse track duration: ";
        r4.append(r5);
        r4.append(r0);
        r4 = r4.toString();
        r2.log(r3, r4);
    L_0x03ab:
        goto L_0x051a;
    L_0x03ad:
        r0 = r9.parseTextFrame(r10);
        r1 = r0.length();
        if (r1 < r4) goto L_0x051a;
    L_0x03b7:
        r1 = r0.substring(r6, r4);	 Catch:{ NumberFormatException -> 0x03c6 }
        r1 = java.lang.Short.valueOf(r1);	 Catch:{ NumberFormatException -> 0x03c6 }
        r1 = r1.shortValue();	 Catch:{ NumberFormatException -> 0x03c6 }
        r9.year = r1;	 Catch:{ NumberFormatException -> 0x03c6 }
        goto L_0x03e9;
    L_0x03c6:
        r1 = move-exception;
        r2 = LOGGER;
        r3 = r9.debugLevel;
        r2 = r2.isLoggable(r3);
        if (r2 == 0) goto L_0x03e9;
    L_0x03d1:
        r2 = LOGGER;
        r3 = r9.debugLevel;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Could not parse year from: ";
        r4.append(r5);
        r4.append(r0);
        r4 = r4.toString();
        r2.log(r3, r4);
    L_0x03e9:
        goto L_0x051a;
    L_0x03eb:
        r0 = r9.parseTextFrame(r10);
        r9.copyright = r0;
        goto L_0x051a;
    L_0x03f3:
        r0 = r9.parseTextFrame(r10);
        r1 = r0.length();
        if (r1 <= 0) goto L_0x051a;
    L_0x03fd:
        r9.genre = r0;
        r1 = 0;
        r2 = r0.charAt(r6);	 Catch:{ NumberFormatException -> 0x0442 }
        r3 = 40;	 Catch:{ NumberFormatException -> 0x0442 }
        if (r2 != r3) goto L_0x0430;	 Catch:{ NumberFormatException -> 0x0442 }
    L_0x0408:
        r2 = 41;	 Catch:{ NumberFormatException -> 0x0442 }
        r2 = r0.indexOf(r2);	 Catch:{ NumberFormatException -> 0x0442 }
        if (r2 <= r7) goto L_0x042f;	 Catch:{ NumberFormatException -> 0x0442 }
    L_0x0410:
        r3 = r0.substring(r7, r2);	 Catch:{ NumberFormatException -> 0x0442 }
        r3 = java.lang.Integer.parseInt(r3);	 Catch:{ NumberFormatException -> 0x0442 }
        r3 = org.telegram.messenger.audioinfo.mp3.ID3v1Genre.getGenre(r3);	 Catch:{ NumberFormatException -> 0x0442 }
        r1 = r3;	 Catch:{ NumberFormatException -> 0x0442 }
        if (r1 != 0) goto L_0x042f;	 Catch:{ NumberFormatException -> 0x0442 }
    L_0x041f:
        r3 = r0.length();	 Catch:{ NumberFormatException -> 0x0442 }
        r4 = r2 + 1;	 Catch:{ NumberFormatException -> 0x0442 }
        if (r3 <= r4) goto L_0x042f;	 Catch:{ NumberFormatException -> 0x0442 }
    L_0x0427:
        r3 = r2 + 1;	 Catch:{ NumberFormatException -> 0x0442 }
        r3 = r0.substring(r3);	 Catch:{ NumberFormatException -> 0x0442 }
        r9.genre = r3;	 Catch:{ NumberFormatException -> 0x0442 }
    L_0x042f:
        goto L_0x0439;	 Catch:{ NumberFormatException -> 0x0442 }
    L_0x0430:
        r2 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x0442 }
        r2 = org.telegram.messenger.audioinfo.mp3.ID3v1Genre.getGenre(r2);	 Catch:{ NumberFormatException -> 0x0442 }
        r1 = r2;	 Catch:{ NumberFormatException -> 0x0442 }
    L_0x0439:
        if (r1 == 0) goto L_0x0443;	 Catch:{ NumberFormatException -> 0x0442 }
    L_0x043b:
        r2 = r1.getDescription();	 Catch:{ NumberFormatException -> 0x0442 }
        r9.genre = r2;	 Catch:{ NumberFormatException -> 0x0442 }
        goto L_0x0443;
    L_0x0442:
        r1 = move-exception;
    L_0x0443:
        goto L_0x051a;
    L_0x0445:
        r0 = r9.parseTextFrame(r10);
        r9.composer = r0;
        goto L_0x051a;
    L_0x044d:
        r0 = "1";
        r1 = r9.parseTextFrame(r10);
        r0 = r0.equals(r1);
        r9.compilation = r0;
        goto L_0x051a;
    L_0x045b:
        r0 = r9.parseTextFrame(r10);
        r9.album = r0;
        goto L_0x051a;
    L_0x0463:
        r0 = r9.parseCommentOrUnsynchronizedLyricsFrame(r10);
        r1 = r9.comment;
        if (r1 == 0) goto L_0x0479;
    L_0x046b:
        r1 = r0.description;
        if (r1 == 0) goto L_0x0479;
    L_0x046f:
        r1 = "";
        r2 = r0.description;
        r1 = r1.equals(r2);
        if (r1 == 0) goto L_0x051a;
    L_0x0479:
        r1 = r0.text;
        r9.comment = r1;
        goto L_0x051a;
    L_0x047f:
        r0 = r9.cover;
        if (r0 == 0) goto L_0x0487;
    L_0x0483:
        r0 = r9.coverPictureType;
        if (r0 == r5) goto L_0x051a;
    L_0x0487:
        r0 = r9.parseAttachedPictureFrame(r10);
        r1 = r9.cover;
        if (r1 == 0) goto L_0x0497;
    L_0x048f:
        r1 = r0.type;
        if (r1 == r5) goto L_0x0497;
    L_0x0493:
        r1 = r0.type;
        if (r1 != 0) goto L_0x0519;
    L_0x0497:
        r1 = r0.imageData;	 Catch:{ Throwable -> 0x0511 }
        r2 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x0511 }
        r2.<init>();	 Catch:{ Throwable -> 0x0511 }
        r2.inJustDecodeBounds = r7;	 Catch:{ Throwable -> 0x0511 }
        r2.inSampleSize = r7;	 Catch:{ Throwable -> 0x0511 }
        r4 = r1.length;	 Catch:{ Throwable -> 0x0511 }
        android.graphics.BitmapFactory.decodeByteArray(r1, r6, r4, r2);	 Catch:{ Throwable -> 0x0511 }
        r4 = r2.outWidth;	 Catch:{ Throwable -> 0x0511 }
        r5 = 800; // 0x320 float:1.121E-42 double:3.953E-321;	 Catch:{ Throwable -> 0x0511 }
        if (r4 > r5) goto L_0x04b0;	 Catch:{ Throwable -> 0x0511 }
        r4 = r2.outHeight;	 Catch:{ Throwable -> 0x0511 }
        if (r4 <= r5) goto L_0x04c3;	 Catch:{ Throwable -> 0x0511 }
        r4 = r2.outWidth;	 Catch:{ Throwable -> 0x0511 }
        r8 = r2.outHeight;	 Catch:{ Throwable -> 0x0511 }
        r4 = java.lang.Math.max(r4, r8);	 Catch:{ Throwable -> 0x0511 }
        if (r4 <= r5) goto L_0x04c3;	 Catch:{ Throwable -> 0x0511 }
        r8 = r2.inSampleSize;	 Catch:{ Throwable -> 0x0511 }
        r8 = r8 * r3;	 Catch:{ Throwable -> 0x0511 }
        r2.inSampleSize = r8;	 Catch:{ Throwable -> 0x0511 }
        r8 = r4 / 2;	 Catch:{ Throwable -> 0x0511 }
        r4 = r8;	 Catch:{ Throwable -> 0x0511 }
        goto L_0x04b8;	 Catch:{ Throwable -> 0x0511 }
        r2.inJustDecodeBounds = r6;	 Catch:{ Throwable -> 0x0511 }
        r3 = r1.length;	 Catch:{ Throwable -> 0x0511 }
        r3 = android.graphics.BitmapFactory.decodeByteArray(r1, r6, r3, r2);	 Catch:{ Throwable -> 0x0511 }
        r9.cover = r3;	 Catch:{ Throwable -> 0x0511 }
        r3 = r9.cover;	 Catch:{ Throwable -> 0x0511 }
        if (r3 == 0) goto L_0x0510;	 Catch:{ Throwable -> 0x0511 }
        r3 = r9.cover;	 Catch:{ Throwable -> 0x0511 }
        r3 = r3.getWidth();	 Catch:{ Throwable -> 0x0511 }
        r4 = r9.cover;	 Catch:{ Throwable -> 0x0511 }
        r4 = r4.getHeight();	 Catch:{ Throwable -> 0x0511 }
        r3 = java.lang.Math.max(r3, r4);	 Catch:{ Throwable -> 0x0511 }
        r3 = (float) r3;	 Catch:{ Throwable -> 0x0511 }
        r4 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;	 Catch:{ Throwable -> 0x0511 }
        r3 = r3 / r4;	 Catch:{ Throwable -> 0x0511 }
        r4 = 0;	 Catch:{ Throwable -> 0x0511 }
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));	 Catch:{ Throwable -> 0x0511 }
        if (r4 <= 0) goto L_0x0504;	 Catch:{ Throwable -> 0x0511 }
        r4 = r9.cover;	 Catch:{ Throwable -> 0x0511 }
        r5 = r9.cover;	 Catch:{ Throwable -> 0x0511 }
        r5 = r5.getWidth();	 Catch:{ Throwable -> 0x0511 }
        r5 = (float) r5;	 Catch:{ Throwable -> 0x0511 }
        r5 = r5 / r3;	 Catch:{ Throwable -> 0x0511 }
        r5 = (int) r5;	 Catch:{ Throwable -> 0x0511 }
        r6 = r9.cover;	 Catch:{ Throwable -> 0x0511 }
        r6 = r6.getHeight();	 Catch:{ Throwable -> 0x0511 }
        r6 = (float) r6;	 Catch:{ Throwable -> 0x0511 }
        r6 = r6 / r3;	 Catch:{ Throwable -> 0x0511 }
        r6 = (int) r6;	 Catch:{ Throwable -> 0x0511 }
        r4 = android.graphics.Bitmap.createScaledBitmap(r4, r5, r6, r7);	 Catch:{ Throwable -> 0x0511 }
        r9.smallCover = r4;	 Catch:{ Throwable -> 0x0511 }
        goto L_0x0508;	 Catch:{ Throwable -> 0x0511 }
        r4 = r9.cover;	 Catch:{ Throwable -> 0x0511 }
        r9.smallCover = r4;	 Catch:{ Throwable -> 0x0511 }
        r4 = r9.smallCover;	 Catch:{ Throwable -> 0x0511 }
        if (r4 != 0) goto L_0x0510;	 Catch:{ Throwable -> 0x0511 }
        r4 = r9.cover;	 Catch:{ Throwable -> 0x0511 }
        r9.smallCover = r4;	 Catch:{ Throwable -> 0x0511 }
        goto L_0x0515;
    L_0x0511:
        r1 = move-exception;
        r1.printStackTrace();
        r1 = r0.type;
        r9.coverPictureType = r1;
    L_0x051a:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.audioinfo.mp3.ID3v2Info.parseFrame(org.telegram.messenger.audioinfo.mp3.ID3v2FrameBody):void");
    }

    public static boolean isID3v2StartPosition(InputStream input) throws IOException {
        input.mark(3);
        try {
            boolean z = input.read() == 73 && input.read() == 68 && input.read() == 51;
            input.reset();
            return z;
        } catch (Throwable th) {
            input.reset();
        }
    }

    public ID3v2Info(InputStream input) throws IOException, ID3v2Exception {
        this(input, Level.FINEST);
    }

    public ID3v2Info(InputStream input, Level debugLevel) throws IOException, ID3v2Exception {
        this.debugLevel = debugLevel;
        if (isID3v2StartPosition(input)) {
            ID3v2TagHeader tagHeader = new ID3v2TagHeader(input);
            this.brand = "ID3";
            this.version = String.format("2.%d.%d", new Object[]{Integer.valueOf(tagHeader.getVersion()), Integer.valueOf(tagHeader.getRevision())});
            ID3v2TagBody tagBody = tagHeader.tagBody(input);
            while (tagBody.getRemainingLength() > 10) {
                ID3v2FrameHeader frameHeader = new ID3v2FrameHeader(tagBody);
                if (frameHeader.isPadding()) {
                    break;
                } else if (((long) frameHeader.getBodySize()) > tagBody.getRemainingLength()) {
                    if (LOGGER.isLoggable(debugLevel)) {
                        LOGGER.log(debugLevel, "ID3 frame claims to extend frames area");
                    }
                } else if (!frameHeader.isValid() || frameHeader.isEncryption()) {
                    tagBody.getData().skipFully((long) frameHeader.getBodySize());
                } else {
                    ID3v2DataInput data;
                    long remainingLength;
                    ID3v2FrameBody frameBody = tagBody.frameBody(frameHeader);
                    try {
                        parseFrame(frameBody);
                        try {
                            data = frameBody.getData();
                            remainingLength = frameBody.getRemainingLength();
                        } catch (ID3v2Exception e) {
                            if (LOGGER.isLoggable(debugLevel)) {
                                Logger logger = LOGGER;
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("ID3 exception occured: ");
                                stringBuilder.append(e.getMessage());
                                logger.log(debugLevel, stringBuilder.toString());
                            }
                        } catch (Throwable th) {
                            frameBody.getData().skipFully(frameBody.getRemainingLength());
                        }
                    } catch (ID3v2Exception e2) {
                        if (LOGGER.isLoggable(debugLevel)) {
                            LOGGER.log(debugLevel, String.format("ID3 exception occured in frame %s: %s", new Object[]{frameHeader.getFrameId(), e2.getMessage()}));
                        }
                        data = frameBody.getData();
                        remainingLength = frameBody.getRemainingLength();
                    }
                    data.skipFully(remainingLength);
                }
            }
            tagBody.getData().skipFully(tagBody.getRemainingLength());
            if (tagHeader.getFooterSize() > 0) {
                input.skip((long) tagHeader.getFooterSize());
            }
        }
    }

    String parseTextFrame(ID3v2FrameBody frame) throws IOException, ID3v2Exception {
        return frame.readFixedLengthString((int) frame.getRemainingLength(), frame.readEncoding());
    }

    CommentOrUnsynchronizedLyrics parseCommentOrUnsynchronizedLyricsFrame(ID3v2FrameBody data) throws IOException, ID3v2Exception {
        ID3v2Encoding encoding = data.readEncoding();
        return new CommentOrUnsynchronizedLyrics(data.readFixedLengthString(3, ID3v2Encoding.ISO_8859_1), data.readZeroTerminatedString(Callback.DEFAULT_DRAG_ANIMATION_DURATION, encoding), data.readFixedLengthString((int) data.getRemainingLength(), encoding));
    }

    AttachedPicture parseAttachedPictureFrame(ID3v2FrameBody data) throws IOException, ID3v2Exception {
        String imageType;
        ID3v2Encoding encoding = data.readEncoding();
        if (data.getTagHeader().getVersion() == 2) {
            String toUpperCase = data.readFixedLengthString(3, ID3v2Encoding.ISO_8859_1).toUpperCase();
            Object obj = -1;
            int hashCode = toUpperCase.hashCode();
            if (hashCode != 73665) {
                if (hashCode == 79369) {
                    if (toUpperCase.equals("PNG")) {
                        obj = null;
                    }
                }
            } else if (toUpperCase.equals("JPG")) {
                obj = 1;
            }
            switch (obj) {
                case null:
                    toUpperCase = "image/png";
                    break;
                case 1:
                    toUpperCase = "image/jpeg";
                    break;
                default:
                    toUpperCase = "image/unknown";
                    break;
            }
            imageType = toUpperCase;
        } else {
            imageType = data.readZeroTerminatedString(20, ID3v2Encoding.ISO_8859_1);
        }
        return new AttachedPicture(data.getData().readByte(), data.readZeroTerminatedString(Callback.DEFAULT_DRAG_ANIMATION_DURATION, encoding), imageType, data.getData().readFully((int) data.getRemainingLength()));
    }
}
