package org.telegram.messenger.exoplayer2.text;

import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.util.MimeTypes;

public interface SubtitleDecoderFactory {
    public static final SubtitleDecoderFactory DEFAULT = new SubtitleDecoderFactory() {
        public boolean supportsFormat(Format format) {
            String mimeType = format.sampleMimeType;
            if (!(MimeTypes.TEXT_VTT.equals(mimeType) || MimeTypes.TEXT_SSA.equals(mimeType) || MimeTypes.APPLICATION_TTML.equals(mimeType) || MimeTypes.APPLICATION_MP4VTT.equals(mimeType) || MimeTypes.APPLICATION_SUBRIP.equals(mimeType) || MimeTypes.APPLICATION_TX3G.equals(mimeType) || MimeTypes.APPLICATION_CEA608.equals(mimeType) || MimeTypes.APPLICATION_MP4CEA608.equals(mimeType) || MimeTypes.APPLICATION_CEA708.equals(mimeType) || MimeTypes.APPLICATION_DVBSUBS.equals(mimeType))) {
                if (!MimeTypes.APPLICATION_PGS.equals(mimeType)) {
                    return false;
                }
            }
            return true;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public org.telegram.messenger.exoplayer2.text.SubtitleDecoder createDecoder(org.telegram.messenger.exoplayer2.Format r4) {
            /*
            r3 = this;
            r0 = r4.sampleMimeType;
            r1 = r0.hashCode();
            switch(r1) {
                case -1351681404: goto L_0x0072;
                case -1248334819: goto L_0x0067;
                case -1026075066: goto L_0x005d;
                case -1004728940: goto L_0x0053;
                case 691401887: goto L_0x0049;
                case 822864842: goto L_0x003f;
                case 930165504: goto L_0x0035;
                case 1566015601: goto L_0x002b;
                case 1566016562: goto L_0x0020;
                case 1668750253: goto L_0x0016;
                case 1693976202: goto L_0x000b;
                default: goto L_0x0009;
            };
        L_0x0009:
            goto L_0x007d;
        L_0x000b:
            r1 = "application/ttml+xml";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x0013:
            r0 = 3;
            goto L_0x007e;
        L_0x0016:
            r1 = "application/x-subrip";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x001e:
            r0 = 4;
            goto L_0x007e;
        L_0x0020:
            r1 = "application/cea-708";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x0028:
            r0 = 8;
            goto L_0x007e;
        L_0x002b:
            r1 = "application/cea-608";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x0033:
            r0 = 6;
            goto L_0x007e;
        L_0x0035:
            r1 = "application/x-mp4-cea-608";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x003d:
            r0 = 7;
            goto L_0x007e;
        L_0x003f:
            r1 = "text/x-ssa";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x0047:
            r0 = 1;
            goto L_0x007e;
        L_0x0049:
            r1 = "application/x-quicktime-tx3g";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x0051:
            r0 = 5;
            goto L_0x007e;
        L_0x0053:
            r1 = "text/vtt";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x005b:
            r0 = 0;
            goto L_0x007e;
        L_0x005d:
            r1 = "application/x-mp4-vtt";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x0065:
            r0 = 2;
            goto L_0x007e;
        L_0x0067:
            r1 = "application/pgs";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x006f:
            r0 = 10;
            goto L_0x007e;
        L_0x0072:
            r1 = "application/dvbsubs";
            r0 = r0.equals(r1);
            if (r0 == 0) goto L_0x007d;
        L_0x007a:
            r0 = 9;
            goto L_0x007e;
        L_0x007d:
            r0 = -1;
        L_0x007e:
            switch(r0) {
                case 0: goto L_0x00cb;
                case 1: goto L_0x00c3;
                case 2: goto L_0x00bd;
                case 3: goto L_0x00b7;
                case 4: goto L_0x00b1;
                case 5: goto L_0x00a9;
                case 6: goto L_0x009f;
                case 7: goto L_0x009f;
                case 8: goto L_0x0097;
                case 9: goto L_0x008f;
                case 10: goto L_0x0089;
                default: goto L_0x0081;
            };
        L_0x0081:
            r0 = new java.lang.IllegalArgumentException;
            r1 = "Attempted to create decoder for unsupported format";
            r0.<init>(r1);
            throw r0;
        L_0x0089:
            r0 = new org.telegram.messenger.exoplayer2.text.pgs.PgsDecoder;
            r0.<init>();
            return r0;
        L_0x008f:
            r0 = new org.telegram.messenger.exoplayer2.text.dvb.DvbDecoder;
            r1 = r4.initializationData;
            r0.<init>(r1);
            return r0;
        L_0x0097:
            r0 = new org.telegram.messenger.exoplayer2.text.cea.Cea708Decoder;
            r1 = r4.accessibilityChannel;
            r0.<init>(r1);
            return r0;
        L_0x009f:
            r0 = new org.telegram.messenger.exoplayer2.text.cea.Cea608Decoder;
            r1 = r4.sampleMimeType;
            r2 = r4.accessibilityChannel;
            r0.<init>(r1, r2);
            return r0;
        L_0x00a9:
            r0 = new org.telegram.messenger.exoplayer2.text.tx3g.Tx3gDecoder;
            r1 = r4.initializationData;
            r0.<init>(r1);
            return r0;
        L_0x00b1:
            r0 = new org.telegram.messenger.exoplayer2.text.subrip.SubripDecoder;
            r0.<init>();
            return r0;
        L_0x00b7:
            r0 = new org.telegram.messenger.exoplayer2.text.ttml.TtmlDecoder;
            r0.<init>();
            return r0;
        L_0x00bd:
            r0 = new org.telegram.messenger.exoplayer2.text.webvtt.Mp4WebvttDecoder;
            r0.<init>();
            return r0;
        L_0x00c3:
            r0 = new org.telegram.messenger.exoplayer2.text.ssa.SsaDecoder;
            r1 = r4.initializationData;
            r0.<init>(r1);
            return r0;
        L_0x00cb:
            r0 = new org.telegram.messenger.exoplayer2.text.webvtt.WebvttDecoder;
            r0.<init>();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.text.SubtitleDecoderFactory.1.createDecoder(org.telegram.messenger.exoplayer2.Format):org.telegram.messenger.exoplayer2.text.SubtitleDecoder");
        }
    };

    SubtitleDecoder createDecoder(Format format);

    boolean supportsFormat(Format format);
}
