package org.telegram.messenger.exoplayer2.util;

import android.text.TextUtils;
import com.coremedia.iso.boxes.sampleentry.AudioSampleEntry;
import com.coremedia.iso.boxes.sampleentry.VisualSampleEntry;

public final class MimeTypes {
    public static final String APPLICATION_CAMERA_MOTION = "application/x-camera-motion";
    public static final String APPLICATION_CEA608 = "application/cea-608";
    public static final String APPLICATION_CEA708 = "application/cea-708";
    public static final String APPLICATION_DVBSUBS = "application/dvbsubs";
    public static final String APPLICATION_EMSG = "application/x-emsg";
    public static final String APPLICATION_EXIF = "application/x-exif";
    public static final String APPLICATION_ID3 = "application/id3";
    public static final String APPLICATION_M3U8 = "application/x-mpegURL";
    public static final String APPLICATION_MP4 = "application/mp4";
    public static final String APPLICATION_MP4CEA608 = "application/x-mp4-cea-608";
    public static final String APPLICATION_MP4VTT = "application/x-mp4-vtt";
    public static final String APPLICATION_MPD = "application/dash+xml";
    public static final String APPLICATION_PGS = "application/pgs";
    public static final String APPLICATION_RAWCC = "application/x-rawcc";
    public static final String APPLICATION_SCTE35 = "application/x-scte35";
    public static final String APPLICATION_SS = "application/vnd.ms-sstr+xml";
    public static final String APPLICATION_SUBRIP = "application/x-subrip";
    public static final String APPLICATION_TTML = "application/ttml+xml";
    public static final String APPLICATION_TX3G = "application/x-quicktime-tx3g";
    public static final String APPLICATION_VOBSUB = "application/vobsub";
    public static final String APPLICATION_WEBM = "application/webm";
    public static final String AUDIO_AAC = "audio/mp4a-latm";
    public static final String AUDIO_AC3 = "audio/ac3";
    public static final String AUDIO_ALAC = "audio/alac";
    public static final String AUDIO_ALAW = "audio/g711-alaw";
    public static final String AUDIO_AMR_NB = "audio/3gpp";
    public static final String AUDIO_AMR_WB = "audio/amr-wb";
    public static final String AUDIO_DTS = "audio/vnd.dts";
    public static final String AUDIO_DTS_EXPRESS = "audio/vnd.dts.hd;profile=lbr";
    public static final String AUDIO_DTS_HD = "audio/vnd.dts.hd";
    public static final String AUDIO_E_AC3 = "audio/eac3";
    public static final String AUDIO_E_AC3_JOC = "audio/eac3-joc";
    public static final String AUDIO_FLAC = "audio/flac";
    public static final String AUDIO_MLAW = "audio/g711-mlaw";
    public static final String AUDIO_MP4 = "audio/mp4";
    public static final String AUDIO_MPEG = "audio/mpeg";
    public static final String AUDIO_MPEG_L1 = "audio/mpeg-L1";
    public static final String AUDIO_MPEG_L2 = "audio/mpeg-L2";
    public static final String AUDIO_MSGSM = "audio/gsm";
    public static final String AUDIO_OPUS = "audio/opus";
    public static final String AUDIO_RAW = "audio/raw";
    public static final String AUDIO_TRUEHD = "audio/true-hd";
    public static final String AUDIO_UNKNOWN = "audio/x-unknown";
    public static final String AUDIO_VORBIS = "audio/vorbis";
    public static final String AUDIO_WEBM = "audio/webm";
    public static final String BASE_TYPE_APPLICATION = "application";
    public static final String BASE_TYPE_AUDIO = "audio";
    public static final String BASE_TYPE_TEXT = "text";
    public static final String BASE_TYPE_VIDEO = "video";
    public static final String TEXT_SSA = "text/x-ssa";
    public static final String TEXT_VTT = "text/vtt";
    public static final String VIDEO_H263 = "video/3gpp";
    public static final String VIDEO_H264 = "video/avc";
    public static final String VIDEO_H265 = "video/hevc";
    public static final String VIDEO_MP4 = "video/mp4";
    public static final String VIDEO_MP4V = "video/mp4v-es";
    public static final String VIDEO_MPEG = "video/mpeg";
    public static final String VIDEO_MPEG2 = "video/mpeg2";
    public static final String VIDEO_UNKNOWN = "video/x-unknown";
    public static final String VIDEO_VC1 = "video/wvc1";
    public static final String VIDEO_VP8 = "video/x-vnd.on2.vp8";
    public static final String VIDEO_VP9 = "video/x-vnd.on2.vp9";
    public static final String VIDEO_WEBM = "video/webm";

    private MimeTypes() {
    }

    public static boolean isAudio(String mimeType) {
        return BASE_TYPE_AUDIO.equals(getTopLevelType(mimeType));
    }

    public static boolean isVideo(String mimeType) {
        return BASE_TYPE_VIDEO.equals(getTopLevelType(mimeType));
    }

    public static boolean isText(String mimeType) {
        return BASE_TYPE_TEXT.equals(getTopLevelType(mimeType));
    }

    public static boolean isApplication(String mimeType) {
        return BASE_TYPE_APPLICATION.equals(getTopLevelType(mimeType));
    }

    public static String getVideoMediaMimeType(String codecs) {
        if (codecs == null) {
            return null;
        }
        for (String codec : codecs.split(",")) {
            String mimeType = getMediaMimeType(codec);
            if (mimeType != null && isVideo(mimeType)) {
                return mimeType;
            }
        }
        return null;
    }

    public static String getAudioMediaMimeType(String codecs) {
        if (codecs == null) {
            return null;
        }
        for (String codec : codecs.split(",")) {
            String mimeType = getMediaMimeType(codec);
            if (mimeType != null && isAudio(mimeType)) {
                return mimeType;
            }
        }
        return null;
    }

    public static String getMediaMimeType(String codec) {
        if (codec == null) {
            return null;
        }
        codec = codec.trim();
        if (!codec.startsWith(VisualSampleEntry.TYPE3)) {
            if (!codec.startsWith(VisualSampleEntry.TYPE4)) {
                if (!codec.startsWith(VisualSampleEntry.TYPE7)) {
                    if (!codec.startsWith(VisualSampleEntry.TYPE6)) {
                        if (!codec.startsWith("vp9")) {
                            if (!codec.startsWith("vp09")) {
                                if (!codec.startsWith("vp8")) {
                                    if (!codec.startsWith("vp08")) {
                                        if (codec.startsWith(AudioSampleEntry.TYPE3)) {
                                            return AUDIO_AAC;
                                        }
                                        if (!codec.startsWith(AudioSampleEntry.TYPE8)) {
                                            if (!codec.startsWith("dac3")) {
                                                if (!codec.startsWith(AudioSampleEntry.TYPE9)) {
                                                    if (!codec.startsWith("dec3")) {
                                                        if (codec.startsWith("ec+3")) {
                                                            return AUDIO_E_AC3_JOC;
                                                        }
                                                        if (!codec.startsWith("dtsc")) {
                                                            if (!codec.startsWith(AudioSampleEntry.TYPE13)) {
                                                                if (!codec.startsWith(AudioSampleEntry.TYPE12)) {
                                                                    if (!codec.startsWith(AudioSampleEntry.TYPE11)) {
                                                                        if (codec.startsWith("opus")) {
                                                                            return AUDIO_OPUS;
                                                                        }
                                                                        if (codec.startsWith("vorbis")) {
                                                                            return AUDIO_VORBIS;
                                                                        }
                                                                        return null;
                                                                    }
                                                                }
                                                                return AUDIO_DTS_HD;
                                                            }
                                                        }
                                                        return AUDIO_DTS;
                                                    }
                                                }
                                                return AUDIO_E_AC3;
                                            }
                                        }
                                        return AUDIO_AC3;
                                    }
                                }
                                return VIDEO_VP8;
                            }
                        }
                        return VIDEO_VP9;
                    }
                }
                return VIDEO_H265;
            }
        }
        return "video/avc";
    }

    public static int getTrackType(String mimeType) {
        if (TextUtils.isEmpty(mimeType)) {
            return -1;
        }
        if (isAudio(mimeType)) {
            return 1;
        }
        if (isVideo(mimeType)) {
            return 2;
        }
        if (!(isText(mimeType) || APPLICATION_CEA608.equals(mimeType) || APPLICATION_CEA708.equals(mimeType) || APPLICATION_MP4CEA608.equals(mimeType) || APPLICATION_SUBRIP.equals(mimeType) || APPLICATION_TTML.equals(mimeType) || APPLICATION_TX3G.equals(mimeType) || APPLICATION_MP4VTT.equals(mimeType) || APPLICATION_RAWCC.equals(mimeType) || APPLICATION_VOBSUB.equals(mimeType) || APPLICATION_PGS.equals(mimeType))) {
            if (!APPLICATION_DVBSUBS.equals(mimeType)) {
                if (!(APPLICATION_ID3.equals(mimeType) || APPLICATION_EMSG.equals(mimeType) || APPLICATION_SCTE35.equals(mimeType))) {
                    if (!APPLICATION_CAMERA_MOTION.equals(mimeType)) {
                        return -1;
                    }
                }
                return 4;
            }
        }
        return 3;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getEncoding(java.lang.String r3) {
        /*
        r0 = r3.hashCode();
        r1 = 0;
        r2 = 5;
        switch(r0) {
            case -2123537834: goto L_0x003c;
            case -1095064472: goto L_0x0032;
            case 187078296: goto L_0x0028;
            case 1504578661: goto L_0x001e;
            case 1505942594: goto L_0x0014;
            case 1556697186: goto L_0x000a;
            default: goto L_0x0009;
        };
    L_0x0009:
        goto L_0x0046;
    L_0x000a:
        r0 = "audio/true-hd";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0046;
    L_0x0012:
        r0 = r2;
        goto L_0x0047;
    L_0x0014:
        r0 = "audio/vnd.dts.hd";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0046;
    L_0x001c:
        r0 = 4;
        goto L_0x0047;
    L_0x001e:
        r0 = "audio/eac3";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0046;
    L_0x0026:
        r0 = 1;
        goto L_0x0047;
    L_0x0028:
        r0 = "audio/ac3";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0046;
    L_0x0030:
        r0 = r1;
        goto L_0x0047;
    L_0x0032:
        r0 = "audio/vnd.dts";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0046;
    L_0x003a:
        r0 = 3;
        goto L_0x0047;
    L_0x003c:
        r0 = "audio/eac3-joc";
        r0 = r3.equals(r0);
        if (r0 == 0) goto L_0x0046;
    L_0x0044:
        r0 = 2;
        goto L_0x0047;
    L_0x0046:
        r0 = -1;
    L_0x0047:
        switch(r0) {
            case 0: goto L_0x0055;
            case 1: goto L_0x0053;
            case 2: goto L_0x0053;
            case 3: goto L_0x0051;
            case 4: goto L_0x004e;
            case 5: goto L_0x004b;
            default: goto L_0x004a;
        };
    L_0x004a:
        return r1;
    L_0x004b:
        r0 = 14;
        return r0;
    L_0x004e:
        r0 = 8;
        return r0;
    L_0x0051:
        r0 = 7;
        return r0;
    L_0x0053:
        r0 = 6;
        return r0;
    L_0x0055:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.util.MimeTypes.getEncoding(java.lang.String):int");
    }

    public static int getTrackTypeOfCodec(String codec) {
        return getTrackType(getMediaMimeType(codec));
    }

    private static String getTopLevelType(String mimeType) {
        if (mimeType == null) {
            return null;
        }
        int indexOfSlash = mimeType.indexOf(47);
        if (indexOfSlash != -1) {
            return mimeType.substring(0, indexOfSlash);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid mime type: ");
        stringBuilder.append(mimeType);
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
