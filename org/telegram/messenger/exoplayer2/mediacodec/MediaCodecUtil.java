package org.telegram.messenger.exoplayer2.mediacodec;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecList;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.source.ExtractorMediaSource;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.exoplayer2.util.Util;
import org.telegram.tgnet.ConnectionsManager;

@TargetApi(16)
@SuppressLint({"InlinedApi"})
public final class MediaCodecUtil {
    private static final SparseIntArray AVC_LEVEL_NUMBER_TO_CONST = new SparseIntArray();
    private static final SparseIntArray AVC_PROFILE_NUMBER_TO_CONST = new SparseIntArray();
    private static final String CODEC_ID_AVC1 = "avc1";
    private static final String CODEC_ID_AVC2 = "avc2";
    private static final String CODEC_ID_HEV1 = "hev1";
    private static final String CODEC_ID_HVC1 = "hvc1";
    private static final String GOOGLE_RAW_DECODER_NAME = "OMX.google.raw.decoder";
    private static final Map<String, Integer> HEVC_CODEC_STRING_TO_PROFILE_LEVEL = new HashMap();
    private static final String MTK_RAW_DECODER_NAME = "OMX.MTK.AUDIO.DECODER.RAW";
    private static final MediaCodecInfo PASSTHROUGH_DECODER_INFO = MediaCodecInfo.newPassthroughInstance(GOOGLE_RAW_DECODER_NAME);
    private static final Pattern PROFILE_PATTERN = Pattern.compile("^\\D?(\\d+)$");
    private static final String TAG = "MediaCodecUtil";
    private static final HashMap<CodecKey, List<MediaCodecInfo>> decoderInfosCache = new HashMap();
    private static int maxH264DecodableFrameSize = -1;

    private static final class CodecKey {
        public final String mimeType;
        public final boolean secure;

        public CodecKey(String mimeType, boolean secure) {
            this.mimeType = mimeType;
            this.secure = secure;
        }

        public int hashCode() {
            return (31 * ((31 * 1) + (this.mimeType == null ? 0 : this.mimeType.hashCode()))) + (this.secure ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj != null) {
                if (obj.getClass() == CodecKey.class) {
                    CodecKey other = (CodecKey) obj;
                    if (!TextUtils.equals(this.mimeType, other.mimeType) || this.secure != other.secure) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }
    }

    public static class DecoderQueryException extends Exception {
        private DecoderQueryException(Throwable cause) {
            super("Failed to query underlying media codecs", cause);
        }
    }

    private interface MediaCodecListCompat {
        int getCodecCount();

        MediaCodecInfo getCodecInfoAt(int i);

        boolean isSecurePlaybackSupported(String str, CodecCapabilities codecCapabilities);

        boolean secureDecodersExplicit();
    }

    private static final class MediaCodecListCompatV16 implements MediaCodecListCompat {
        private MediaCodecListCompatV16() {
        }

        public int getCodecCount() {
            return MediaCodecList.getCodecCount();
        }

        public MediaCodecInfo getCodecInfoAt(int index) {
            return MediaCodecList.getCodecInfoAt(index);
        }

        public boolean secureDecodersExplicit() {
            return false;
        }

        public boolean isSecurePlaybackSupported(String mimeType, CodecCapabilities capabilities) {
            return "video/avc".equals(mimeType);
        }
    }

    @TargetApi(21)
    private static final class MediaCodecListCompatV21 implements MediaCodecListCompat {
        private final int codecKind;
        private MediaCodecInfo[] mediaCodecInfos;

        public MediaCodecListCompatV21(boolean includeSecure) {
            this.codecKind = includeSecure;
        }

        public int getCodecCount() {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos.length;
        }

        public MediaCodecInfo getCodecInfoAt(int index) {
            ensureMediaCodecInfosInitialized();
            return this.mediaCodecInfos[index];
        }

        public boolean secureDecodersExplicit() {
            return true;
        }

        public boolean isSecurePlaybackSupported(String mimeType, CodecCapabilities capabilities) {
            return capabilities.isFeatureSupported("secure-playback");
        }

        private void ensureMediaCodecInfosInitialized() {
            if (this.mediaCodecInfos == null) {
                this.mediaCodecInfos = new MediaCodecList(this.codecKind).getCodecInfos();
            }
        }
    }

    private static android.util.Pair<java.lang.Integer, java.lang.Integer> getAvcProfileAndLevel(java.lang.String r1, java.lang.String[] r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.getAvcProfileAndLevel(java.lang.String, java.lang.String[]):android.util.Pair<java.lang.Integer, java.lang.Integer>
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 2;
        r1 = 0;
        r2 = r10.length;
        if (r2 >= r0) goto L_0x001c;
    L_0x0005:
        r0 = "MediaCodecUtil";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Ignoring malformed AVC codec string: ";
        r2.append(r3);
        r2.append(r9);
        r2 = r2.toString();
        android.util.Log.w(r0, r2);
        return r1;
    L_0x001c:
        r2 = 1;
        r3 = r10[r2];	 Catch:{ NumberFormatException -> 0x00cf }
        r3 = r3.length();	 Catch:{ NumberFormatException -> 0x00cf }
        r4 = 6;	 Catch:{ NumberFormatException -> 0x00cf }
        if (r3 != r4) goto L_0x0047;	 Catch:{ NumberFormatException -> 0x00cf }
    L_0x0026:
        r3 = r10[r2];	 Catch:{ NumberFormatException -> 0x00cf }
        r4 = 0;	 Catch:{ NumberFormatException -> 0x00cf }
        r0 = r3.substring(r4, r0);	 Catch:{ NumberFormatException -> 0x00cf }
        r3 = 16;	 Catch:{ NumberFormatException -> 0x00cf }
        r0 = java.lang.Integer.parseInt(r0, r3);	 Catch:{ NumberFormatException -> 0x00cf }
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ NumberFormatException -> 0x00cf }
        r2 = r10[r2];	 Catch:{ NumberFormatException -> 0x00cf }
        r4 = 4;	 Catch:{ NumberFormatException -> 0x00cf }
        r2 = r2.substring(r4);	 Catch:{ NumberFormatException -> 0x00cf }
        r2 = java.lang.Integer.parseInt(r2, r3);	 Catch:{ NumberFormatException -> 0x00cf }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ NumberFormatException -> 0x00cf }
        goto L_0x0062;	 Catch:{ NumberFormatException -> 0x00cf }
    L_0x0047:
        r3 = r10.length;	 Catch:{ NumberFormatException -> 0x00cf }
        r4 = 3;	 Catch:{ NumberFormatException -> 0x00cf }
        if (r3 < r4) goto L_0x00b8;	 Catch:{ NumberFormatException -> 0x00cf }
        r2 = r10[r2];	 Catch:{ NumberFormatException -> 0x00cf }
        r2 = java.lang.Integer.parseInt(r2);	 Catch:{ NumberFormatException -> 0x00cf }
        r2 = java.lang.Integer.valueOf(r2);	 Catch:{ NumberFormatException -> 0x00cf }
        r0 = r10[r0];	 Catch:{ NumberFormatException -> 0x00cf }
        r0 = java.lang.Integer.parseInt(r0);	 Catch:{ NumberFormatException -> 0x00cf }
        r0 = java.lang.Integer.valueOf(r0);	 Catch:{ NumberFormatException -> 0x00cf }
        r8 = r2;
        r2 = r0;
        r0 = r8;
        r3 = AVC_PROFILE_NUMBER_TO_CONST;
        r4 = r0.intValue();
        r3 = r3.get(r4);
        r3 = java.lang.Integer.valueOf(r3);
        if (r3 != 0) goto L_0x008b;
        r4 = "MediaCodecUtil";
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "Unknown AVC profile: ";
        r5.append(r6);
        r5.append(r0);
        r5 = r5.toString();
        android.util.Log.w(r4, r5);
        return r1;
        r4 = AVC_LEVEL_NUMBER_TO_CONST;
        r5 = r2.intValue();
        r4 = r4.get(r5);
        r4 = java.lang.Integer.valueOf(r4);
        if (r4 != 0) goto L_0x00b2;
        r5 = "MediaCodecUtil";
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Unknown AVC level: ";
        r6.append(r7);
        r6.append(r2);
        r6 = r6.toString();
        android.util.Log.w(r5, r6);
        return r1;
        r1 = new android.util.Pair;
        r1.<init>(r3, r4);
        return r1;
        r0 = "MediaCodecUtil";	 Catch:{ NumberFormatException -> 0x00cf }
        r2 = new java.lang.StringBuilder;	 Catch:{ NumberFormatException -> 0x00cf }
        r2.<init>();	 Catch:{ NumberFormatException -> 0x00cf }
        r3 = "Ignoring malformed AVC codec string: ";	 Catch:{ NumberFormatException -> 0x00cf }
        r2.append(r3);	 Catch:{ NumberFormatException -> 0x00cf }
        r2.append(r9);	 Catch:{ NumberFormatException -> 0x00cf }
        r2 = r2.toString();	 Catch:{ NumberFormatException -> 0x00cf }
        android.util.Log.w(r0, r2);	 Catch:{ NumberFormatException -> 0x00cf }
        return r1;
    L_0x00cf:
        r0 = move-exception;
        r2 = "MediaCodecUtil";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Ignoring malformed AVC codec string: ";
        r3.append(r4);
        r3.append(r9);
        r3 = r3.toString();
        android.util.Log.w(r2, r3);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.getAvcProfileAndLevel(java.lang.String, java.lang.String[]):android.util.Pair<java.lang.Integer, java.lang.Integer>");
    }

    private static android.util.Pair<java.lang.Integer, java.lang.Integer> getHevcProfileAndLevel(java.lang.String r1, java.lang.String[] r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.getHevcProfileAndLevel(java.lang.String, java.lang.String[]):android.util.Pair<java.lang.Integer, java.lang.Integer>
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 0;
        r1 = 4;
        r2 = r10.length;
        if (r2 >= r1) goto L_0x001c;
    L_0x0005:
        r1 = "MediaCodecUtil";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Ignoring malformed HEVC codec string: ";
        r2.append(r3);
        r2.append(r9);
        r2 = r2.toString();
        android.util.Log.w(r1, r2);
        return r0;
    L_0x001c:
        r1 = PROFILE_PATTERN;
        r2 = 1;
        r3 = r10[r2];
        r1 = r1.matcher(r3);
        r3 = r1.matches();
        if (r3 != 0) goto L_0x0042;
    L_0x002b:
        r2 = "MediaCodecUtil";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Ignoring malformed HEVC codec string: ";
        r3.append(r4);
        r3.append(r9);
        r3 = r3.toString();
        android.util.Log.w(r2, r3);
        return r0;
    L_0x0042:
        r3 = r1.group(r2);
        r4 = "1";
        r4 = r4.equals(r3);
        if (r4 == 0) goto L_0x0050;
    L_0x004e:
        r4 = 1;
        goto L_0x005a;
    L_0x0050:
        r4 = "2";
        r4 = r4.equals(r3);
        if (r4 == 0) goto L_0x008d;
        r4 = 2;
        goto L_0x004f;
        r5 = HEVC_CODEC_STRING_TO_PROFILE_LEVEL;
        r6 = 3;
        r6 = r10[r6];
        r5 = r5.get(r6);
        r5 = (java.lang.Integer) r5;
        if (r5 != 0) goto L_0x0083;
        r6 = "MediaCodecUtil";
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "Unknown HEVC level string: ";
        r7.append(r8);
        r2 = r1.group(r2);
        r7.append(r2);
        r2 = r7.toString();
        android.util.Log.w(r6, r2);
        return r0;
        r0 = new android.util.Pair;
        r2 = java.lang.Integer.valueOf(r4);
        r0.<init>(r2, r5);
        return r0;
        r2 = "MediaCodecUtil";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Unknown HEVC profile string: ";
        r4.append(r5);
        r4.append(r3);
        r4 = r4.toString();
        android.util.Log.w(r2, r4);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.getHevcProfileAndLevel(java.lang.String, java.lang.String[]):android.util.Pair<java.lang.Integer, java.lang.Integer>");
    }

    static {
        AVC_PROFILE_NUMBER_TO_CONST.put(66, 1);
        AVC_PROFILE_NUMBER_TO_CONST.put(77, 2);
        AVC_PROFILE_NUMBER_TO_CONST.put(88, 4);
        AVC_PROFILE_NUMBER_TO_CONST.put(100, 8);
        AVC_LEVEL_NUMBER_TO_CONST.put(10, 1);
        AVC_LEVEL_NUMBER_TO_CONST.put(11, 4);
        AVC_LEVEL_NUMBER_TO_CONST.put(12, 8);
        AVC_LEVEL_NUMBER_TO_CONST.put(13, 16);
        AVC_LEVEL_NUMBER_TO_CONST.put(20, 32);
        AVC_LEVEL_NUMBER_TO_CONST.put(21, 64);
        AVC_LEVEL_NUMBER_TO_CONST.put(22, 128);
        AVC_LEVEL_NUMBER_TO_CONST.put(30, 256);
        AVC_LEVEL_NUMBER_TO_CONST.put(31, 512);
        AVC_LEVEL_NUMBER_TO_CONST.put(32, 1024);
        AVC_LEVEL_NUMBER_TO_CONST.put(40, 2048);
        AVC_LEVEL_NUMBER_TO_CONST.put(41, 4096);
        AVC_LEVEL_NUMBER_TO_CONST.put(42, MessagesController.UPDATE_MASK_CHANNEL);
        AVC_LEVEL_NUMBER_TO_CONST.put(50, MessagesController.UPDATE_MASK_CHAT_ADMINS);
        AVC_LEVEL_NUMBER_TO_CONST.put(51, 32768);
        AVC_LEVEL_NUMBER_TO_CONST.put(52, C.DEFAULT_BUFFER_SEGMENT_SIZE);
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L30", Integer.valueOf(1));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L60", Integer.valueOf(4));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L63", Integer.valueOf(16));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L90", Integer.valueOf(64));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L93", Integer.valueOf(256));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L120", Integer.valueOf(1024));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L123", Integer.valueOf(4096));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L150", Integer.valueOf(MessagesController.UPDATE_MASK_CHAT_ADMINS));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L153", Integer.valueOf(C.DEFAULT_BUFFER_SEGMENT_SIZE));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L156", Integer.valueOf(262144));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L180", Integer.valueOf(ExtractorMediaSource.DEFAULT_LOADING_CHECK_INTERVAL_BYTES));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L183", Integer.valueOf(4194304));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("L186", Integer.valueOf(16777216));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H30", Integer.valueOf(2));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H60", Integer.valueOf(8));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H63", Integer.valueOf(32));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H90", Integer.valueOf(128));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H93", Integer.valueOf(512));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H120", Integer.valueOf(2048));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H123", Integer.valueOf(MessagesController.UPDATE_MASK_CHANNEL));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H150", Integer.valueOf(32768));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H153", Integer.valueOf(131072));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H156", Integer.valueOf(524288));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H180", Integer.valueOf(2097152));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H183", Integer.valueOf(8388608));
        HEVC_CODEC_STRING_TO_PROFILE_LEVEL.put("H186", Integer.valueOf(ConnectionsManager.FileTypeVideo));
    }

    private MediaCodecUtil() {
    }

    public static void warmDecoderInfoCache(String mimeType, boolean secure) {
        try {
            getDecoderInfos(mimeType, secure);
        } catch (DecoderQueryException e) {
            Log.e(TAG, "Codec warming failed", e);
        }
    }

    public static MediaCodecInfo getPassthroughDecoderInfo() {
        return PASSTHROUGH_DECODER_INFO;
    }

    public static MediaCodecInfo getDecoderInfo(String mimeType, boolean secure) throws DecoderQueryException {
        List<MediaCodecInfo> decoderInfos = getDecoderInfos(mimeType, secure);
        return decoderInfos.isEmpty() ? null : (MediaCodecInfo) decoderInfos.get(0);
    }

    public static synchronized List<MediaCodecInfo> getDecoderInfos(String mimeType, boolean secure) throws DecoderQueryException {
        synchronized (MediaCodecUtil.class) {
            CodecKey key = new CodecKey(mimeType, secure);
            List<MediaCodecInfo> cachedDecoderInfos = (List) decoderInfosCache.get(key);
            if (cachedDecoderInfos != null) {
                return cachedDecoderInfos;
            }
            MediaCodecListCompat mediaCodecList = Util.SDK_INT >= 21 ? new MediaCodecListCompatV21(secure) : new MediaCodecListCompatV16();
            ArrayList<MediaCodecInfo> decoderInfos = getDecoderInfosInternal(key, mediaCodecList, mimeType);
            if (secure && decoderInfos.isEmpty() && 21 <= Util.SDK_INT && Util.SDK_INT <= 23) {
                mediaCodecList = new MediaCodecListCompatV16();
                decoderInfos = getDecoderInfosInternal(key, mediaCodecList, mimeType);
                if (!decoderInfos.isEmpty()) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("MediaCodecList API didn't list secure decoder for: ");
                    stringBuilder.append(mimeType);
                    stringBuilder.append(". Assuming: ");
                    stringBuilder.append(((MediaCodecInfo) decoderInfos.get(0)).name);
                    Log.w(str, stringBuilder.toString());
                }
            }
            if (MimeTypes.AUDIO_E_AC3_JOC.equals(mimeType)) {
                decoderInfos.addAll(getDecoderInfosInternal(new CodecKey(MimeTypes.AUDIO_E_AC3, key.secure), mediaCodecList, mimeType));
            }
            applyWorkarounds(decoderInfos);
            List<MediaCodecInfo> unmodifiableDecoderInfos = Collections.unmodifiableList(decoderInfos);
            decoderInfosCache.put(key, unmodifiableDecoderInfos);
            return unmodifiableDecoderInfos;
        }
    }

    public static int maxH264DecodableFrameSize() throws DecoderQueryException {
        if (maxH264DecodableFrameSize == -1) {
            int result = 0;
            int i = 0;
            MediaCodecInfo decoderInfo = getDecoderInfo("video/avc", false);
            if (decoderInfo != null) {
                CodecProfileLevel[] profileLevels = decoderInfo.getProfileLevels();
                int length = profileLevels.length;
                while (i < length) {
                    result = Math.max(avcLevelToMaxFrameSize(profileLevels[i].level), result);
                    i++;
                }
                result = Math.max(result, Util.SDK_INT >= 21 ? 345600 : 172800);
            }
            maxH264DecodableFrameSize = result;
        }
        return maxH264DecodableFrameSize;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.util.Pair<java.lang.Integer, java.lang.Integer> getCodecProfileAndLevel(java.lang.String r6) {
        /*
        r0 = 0;
        if (r6 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = "\\.";
        r1 = r6.split(r1);
        r2 = 0;
        r3 = r1[r2];
        r4 = -1;
        r5 = r3.hashCode();
        switch(r5) {
            case 3006243: goto L_0x0033;
            case 3006244: goto L_0x0029;
            case 3199032: goto L_0x0020;
            case 3214780: goto L_0x0016;
            default: goto L_0x0015;
        };
    L_0x0015:
        goto L_0x003d;
    L_0x0016:
        r2 = "hvc1";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x003d;
    L_0x001e:
        r2 = 1;
        goto L_0x003e;
    L_0x0020:
        r5 = "hev1";
        r3 = r3.equals(r5);
        if (r3 == 0) goto L_0x003d;
    L_0x0028:
        goto L_0x003e;
    L_0x0029:
        r2 = "avc2";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x003d;
    L_0x0031:
        r2 = 3;
        goto L_0x003e;
    L_0x0033:
        r2 = "avc1";
        r2 = r3.equals(r2);
        if (r2 == 0) goto L_0x003d;
    L_0x003b:
        r2 = 2;
        goto L_0x003e;
    L_0x003d:
        r2 = r4;
    L_0x003e:
        switch(r2) {
            case 0: goto L_0x0047;
            case 1: goto L_0x0047;
            case 2: goto L_0x0042;
            case 3: goto L_0x0042;
            default: goto L_0x0041;
        };
    L_0x0041:
        return r0;
    L_0x0042:
        r0 = getAvcProfileAndLevel(r6, r1);
        return r0;
    L_0x0047:
        r0 = getHevcProfileAndLevel(r6, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.getCodecProfileAndLevel(java.lang.String):android.util.Pair<java.lang.Integer, java.lang.Integer>");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.ArrayList<org.telegram.messenger.exoplayer2.mediacodec.MediaCodecInfo> getDecoderInfosInternal(org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.CodecKey r23, org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.MediaCodecListCompat r24, java.lang.String r25) throws org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException {
        /*
        r1 = r23;
        r2 = r24;
        r3 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0114 }
        r3.<init>();	 Catch:{ Exception -> 0x0114 }
        r4 = r1.mimeType;	 Catch:{ Exception -> 0x0114 }
        r5 = r24.getCodecCount();	 Catch:{ Exception -> 0x0114 }
        r6 = r24.secureDecodersExplicit();	 Catch:{ Exception -> 0x0114 }
        r8 = 0;
    L_0x0014:
        if (r8 >= r5) goto L_0x010f;
    L_0x0016:
        r9 = r2.getCodecInfoAt(r8);	 Catch:{ Exception -> 0x0114 }
        r10 = r9.getName();	 Catch:{ Exception -> 0x0114 }
        r11 = r25;
        r12 = isCodecUsableDecoder(r9, r10, r6, r11);	 Catch:{ Exception -> 0x010d }
        if (r12 == 0) goto L_0x0101;
    L_0x0026:
        r12 = r9.getSupportedTypes();	 Catch:{ Exception -> 0x010d }
        r13 = r12.length;	 Catch:{ Exception -> 0x010d }
        r14 = 0;
    L_0x002c:
        if (r14 >= r13) goto L_0x0101;
    L_0x002e:
        r15 = r12[r14];	 Catch:{ Exception -> 0x010d }
        r16 = r15.equalsIgnoreCase(r4);	 Catch:{ Exception -> 0x010d }
        if (r16 == 0) goto L_0x00f1;
    L_0x0036:
        r16 = r9.getCapabilitiesForType(r15);	 Catch:{ Exception -> 0x009f }
        r17 = r16;
        r7 = r17;
        r16 = r2.isSecurePlaybackSupported(r4, r7);	 Catch:{ Exception -> 0x009f }
        r18 = r16;
        r16 = codecNeedsDisableAdaptationWorkaround(r10);	 Catch:{ Exception -> 0x009f }
        r19 = r16;
        if (r6 == 0) goto L_0x005c;
    L_0x004c:
        r2 = r1.secure;	 Catch:{ Exception -> 0x0055 }
        r20 = r5;
        r5 = r18;
        if (r2 == r5) goto L_0x0066;
    L_0x0054:
        goto L_0x0060;
    L_0x0055:
        r0 = move-exception;
        r20 = r5;
        r1 = r0;
        r21 = r9;
        goto L_0x00a5;
    L_0x005c:
        r20 = r5;
        r5 = r18;
    L_0x0060:
        if (r6 != 0) goto L_0x007b;
    L_0x0062:
        r2 = r1.secure;	 Catch:{ Exception -> 0x0076 }
        if (r2 != 0) goto L_0x007b;
    L_0x0066:
        r21 = r9;
        r2 = r19;
        r1 = 0;
        r9 = org.telegram.messenger.exoplayer2.mediacodec.MediaCodecInfo.newInstance(r10, r4, r7, r2, r1);	 Catch:{ Exception -> 0x0073 }
        r3.add(r9);	 Catch:{ Exception -> 0x0073 }
        goto L_0x009e;
    L_0x0073:
        r0 = move-exception;
        r1 = r0;
        goto L_0x00a5;
    L_0x0076:
        r0 = move-exception;
        r21 = r9;
        r1 = r0;
        goto L_0x00a5;
    L_0x007b:
        r21 = r9;
        r2 = r19;
        r1 = 0;
        if (r6 != 0) goto L_0x009e;
    L_0x0082:
        if (r5 == 0) goto L_0x009e;
    L_0x0084:
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0073 }
        r9.<init>();	 Catch:{ Exception -> 0x0073 }
        r9.append(r10);	 Catch:{ Exception -> 0x0073 }
        r1 = ".secure";
        r9.append(r1);	 Catch:{ Exception -> 0x0073 }
        r1 = r9.toString();	 Catch:{ Exception -> 0x0073 }
        r9 = 1;
        r1 = org.telegram.messenger.exoplayer2.mediacodec.MediaCodecInfo.newInstance(r1, r4, r7, r2, r9);	 Catch:{ Exception -> 0x0073 }
        r3.add(r1);	 Catch:{ Exception -> 0x0073 }
        return r3;
    L_0x009e:
        goto L_0x00f5;
    L_0x009f:
        r0 = move-exception;
        r20 = r5;
        r21 = r9;
        r1 = r0;
    L_0x00a5:
        r2 = org.telegram.messenger.exoplayer2.util.Util.SDK_INT;	 Catch:{ Exception -> 0x010d }
        r5 = 23;
        if (r2 > r5) goto L_0x00cd;
    L_0x00ab:
        r2 = r3.isEmpty();	 Catch:{ Exception -> 0x010d }
        if (r2 != 0) goto L_0x00cd;
    L_0x00b1:
        r2 = "MediaCodecUtil";
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x010d }
        r5.<init>();	 Catch:{ Exception -> 0x010d }
        r7 = "Skipping codec ";
        r5.append(r7);	 Catch:{ Exception -> 0x010d }
        r5.append(r10);	 Catch:{ Exception -> 0x010d }
        r7 = " (failed to query capabilities)";
        r5.append(r7);	 Catch:{ Exception -> 0x010d }
        r5 = r5.toString();	 Catch:{ Exception -> 0x010d }
        android.util.Log.e(r2, r5);	 Catch:{ Exception -> 0x010d }
        goto L_0x00f5;
    L_0x00cd:
        r2 = "MediaCodecUtil";
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x010d }
        r5.<init>();	 Catch:{ Exception -> 0x010d }
        r7 = "Failed to query codec ";
        r5.append(r7);	 Catch:{ Exception -> 0x010d }
        r5.append(r10);	 Catch:{ Exception -> 0x010d }
        r7 = " (";
        r5.append(r7);	 Catch:{ Exception -> 0x010d }
        r5.append(r15);	 Catch:{ Exception -> 0x010d }
        r7 = ")";
        r5.append(r7);	 Catch:{ Exception -> 0x010d }
        r5 = r5.toString();	 Catch:{ Exception -> 0x010d }
        android.util.Log.e(r2, r5);	 Catch:{ Exception -> 0x010d }
        throw r1;	 Catch:{ Exception -> 0x010d }
    L_0x00f1:
        r20 = r5;
        r21 = r9;
    L_0x00f5:
        r14 = r14 + 1;
        r5 = r20;
        r9 = r21;
        r1 = r23;
        r2 = r24;
        goto L_0x002c;
    L_0x0101:
        r20 = r5;
        r8 = r8 + 1;
        r5 = r20;
        r1 = r23;
        r2 = r24;
        goto L_0x0014;
    L_0x010d:
        r0 = move-exception;
        goto L_0x0117;
    L_0x010f:
        r11 = r25;
        r20 = r5;
        return r3;
    L_0x0114:
        r0 = move-exception;
        r11 = r25;
    L_0x0117:
        r1 = r0;
        r2 = new org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil$DecoderQueryException;
        r3 = 0;
        r2.<init>(r1);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.getDecoderInfosInternal(org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil$CodecKey, org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil$MediaCodecListCompat, java.lang.String):java.util.ArrayList<org.telegram.messenger.exoplayer2.mediacodec.MediaCodecInfo>");
    }

    private static boolean isCodecUsableDecoder(MediaCodecInfo info, String name, boolean secureDecodersExplicit, String requestedMimeType) {
        if (!info.isEncoder()) {
            if (secureDecodersExplicit || !name.endsWith(".secure")) {
                if (Util.SDK_INT < 21 && ("CIPAACDecoder".equals(name) || "CIPMP3Decoder".equals(name) || "CIPVorbisDecoder".equals(name) || "CIPAMRNBDecoder".equals(name) || "AACDecoder".equals(name) || "MP3Decoder".equals(name))) {
                    return false;
                }
                if (Util.SDK_INT < 18 && "OMX.SEC.MP3.Decoder".equals(name)) {
                    return false;
                }
                if (Util.SDK_INT < 18 && "OMX.MTK.AUDIO.DECODER.AAC".equals(name) && ("a70".equals(Util.DEVICE) || ("Xiaomi".equals(Util.MANUFACTURER) && Util.DEVICE.startsWith("HM")))) {
                    return false;
                }
                if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.mp3".equals(name) && ("dlxu".equals(Util.DEVICE) || "protou".equals(Util.DEVICE) || "ville".equals(Util.DEVICE) || "villeplus".equals(Util.DEVICE) || "villec2".equals(Util.DEVICE) || Util.DEVICE.startsWith("gee") || "C6602".equals(Util.DEVICE) || "C6603".equals(Util.DEVICE) || "C6606".equals(Util.DEVICE) || "C6616".equals(Util.DEVICE) || "L36h".equals(Util.DEVICE) || "SO-02E".equals(Util.DEVICE))) {
                    return false;
                }
                if (Util.SDK_INT == 16 && "OMX.qcom.audio.decoder.aac".equals(name) && ("C1504".equals(Util.DEVICE) || "C1505".equals(Util.DEVICE) || "C1604".equals(Util.DEVICE) || "C1605".equals(Util.DEVICE))) {
                    return false;
                }
                if (Util.SDK_INT < 24 && (("OMX.SEC.aac.dec".equals(name) || "OMX.Exynos.AAC.Decoder".equals(name)) && Util.MANUFACTURER.equals("samsung") && (Util.DEVICE.startsWith("zeroflte") || Util.DEVICE.startsWith("zerolte") || Util.DEVICE.startsWith("zenlte") || Util.DEVICE.equals("SC-05G") || Util.DEVICE.equals("marinelteatt") || Util.DEVICE.equals("404SC") || Util.DEVICE.equals("SC-04G") || Util.DEVICE.equals("SCV31")))) {
                    return false;
                }
                if (Util.SDK_INT <= 19 && "OMX.SEC.vp8.dec".equals(name) && "samsung".equals(Util.MANUFACTURER) && (Util.DEVICE.startsWith("d2") || Util.DEVICE.startsWith("serrano") || Util.DEVICE.startsWith("jflte") || Util.DEVICE.startsWith("santos") || Util.DEVICE.startsWith("t0"))) {
                    return false;
                }
                if (Util.SDK_INT <= 19 && Util.DEVICE.startsWith("jflte") && "OMX.qcom.video.decoder.vp8".equals(name)) {
                    return false;
                }
                if (MimeTypes.AUDIO_E_AC3_JOC.equals(requestedMimeType) && "OMX.MTK.AUDIO.DECODER.DSPAC3".equals(name)) {
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private static void applyWorkarounds(List<MediaCodecInfo> decoderInfos) {
        if (Util.SDK_INT < 26) {
            int i = 1;
            if (decoderInfos.size() > 1 && MTK_RAW_DECODER_NAME.equals(((MediaCodecInfo) decoderInfos.get(0)).name)) {
                while (true) {
                    int i2 = i;
                    if (i2 < decoderInfos.size()) {
                        MediaCodecInfo decoderInfo = (MediaCodecInfo) decoderInfos.get(i2);
                        if (GOOGLE_RAW_DECODER_NAME.equals(decoderInfo.name)) {
                            decoderInfos.remove(i2);
                            decoderInfos.add(0, decoderInfo);
                            return;
                        }
                        i = i2 + 1;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    private static boolean codecNeedsDisableAdaptationWorkaround(String name) {
        return Util.SDK_INT <= 22 && ((Util.MODEL.equals("ODROID-XU3") || Util.MODEL.equals("Nexus 10")) && ("OMX.Exynos.AVC.Decoder".equals(name) || "OMX.Exynos.AVC.Decoder.secure".equals(name)));
    }

    private static int avcLevelToMaxFrameSize(int avcLevel) {
        switch (avcLevel) {
            case 1:
                return 25344;
            case 2:
                return 25344;
            case 8:
                return 101376;
            case 16:
                return 101376;
            case 32:
                return 101376;
            case 64:
                return 202752;
            case 128:
                return 414720;
            case 256:
                return 414720;
            case 512:
                return 921600;
            case 1024:
                return 1310720;
            case 2048:
                return 2097152;
            case 4096:
                return 2097152;
            case MessagesController.UPDATE_MASK_CHANNEL /*8192*/:
                return 2228224;
            case MessagesController.UPDATE_MASK_CHAT_ADMINS /*16384*/:
                return 5652480;
            case 32768:
                return 9437184;
            case C.DEFAULT_BUFFER_SEGMENT_SIZE /*65536*/:
                return 9437184;
            default:
                return -1;
        }
    }
}
