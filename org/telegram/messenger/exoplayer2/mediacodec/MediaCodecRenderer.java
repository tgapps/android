package org.telegram.messenger.exoplayer2.mediacodec;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCodec.CodecException;
import android.media.MediaCodec.CryptoInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.exoplayer2.BaseRenderer;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.ExoPlaybackException;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.FormatHolder;
import org.telegram.messenger.exoplayer2.decoder.DecoderCounters;
import org.telegram.messenger.exoplayer2.decoder.DecoderInputBuffer;
import org.telegram.messenger.exoplayer2.drm.DrmSession;
import org.telegram.messenger.exoplayer2.drm.DrmSessionManager;
import org.telegram.messenger.exoplayer2.drm.FrameworkMediaCrypto;
import org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.TraceUtil;
import org.telegram.messenger.exoplayer2.util.Util;

@TargetApi(16)
public abstract class MediaCodecRenderer extends BaseRenderer {
    private static final byte[] ADAPTATION_WORKAROUND_BUFFER = Util.getBytesFromHexString("0000016742C00BDA259000000168CE0F13200000016588840DCE7118A0002FBF1C31C3275D78");
    private static final int ADAPTATION_WORKAROUND_MODE_ALWAYS = 2;
    private static final int ADAPTATION_WORKAROUND_MODE_NEVER = 0;
    private static final int ADAPTATION_WORKAROUND_MODE_SAME_RESOLUTION = 1;
    private static final int ADAPTATION_WORKAROUND_SLICE_WIDTH_HEIGHT = 32;
    private static final long MAX_CODEC_HOTSWAP_TIME_MS = 1000;
    private static final int RECONFIGURATION_STATE_NONE = 0;
    private static final int RECONFIGURATION_STATE_QUEUE_PENDING = 2;
    private static final int RECONFIGURATION_STATE_WRITE_PENDING = 1;
    private static final int REINITIALIZATION_STATE_NONE = 0;
    private static final int REINITIALIZATION_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REINITIALIZATION_STATE_WAIT_END_OF_STREAM = 2;
    private static final String TAG = "MediaCodecRenderer";
    private final DecoderInputBuffer buffer;
    private MediaCodec codec;
    private int codecAdaptationWorkaroundMode;
    private long codecHotswapDeadlineMs;
    private MediaCodecInfo codecInfo;
    private boolean codecNeedsAdaptationWorkaroundBuffer;
    private boolean codecNeedsDiscardToSpsWorkaround;
    private boolean codecNeedsEosFlushWorkaround;
    private boolean codecNeedsEosOutputExceptionWorkaround;
    private boolean codecNeedsEosPropagationWorkaround;
    private boolean codecNeedsFlushWorkaround;
    private boolean codecNeedsMonoChannelCountWorkaround;
    private boolean codecReceivedBuffers;
    private boolean codecReceivedEos;
    private int codecReconfigurationState;
    private boolean codecReconfigured;
    private int codecReinitializationState;
    private final List<Long> decodeOnlyPresentationTimestamps;
    protected DecoderCounters decoderCounters;
    private DrmSession<FrameworkMediaCrypto> drmSession;
    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private final DecoderInputBuffer flagsOnlyBuffer;
    private Format format;
    private final FormatHolder formatHolder;
    private ByteBuffer[] inputBuffers;
    private int inputIndex;
    private boolean inputStreamEnded;
    private final MediaCodecSelector mediaCodecSelector;
    private ByteBuffer outputBuffer;
    private final BufferInfo outputBufferInfo;
    private ByteBuffer[] outputBuffers;
    private int outputIndex;
    private boolean outputStreamEnded;
    private DrmSession<FrameworkMediaCrypto> pendingDrmSession;
    private final boolean playClearSamplesWithoutKeys;
    private boolean shouldSkipAdaptationWorkaroundOutputBuffer;
    private boolean shouldSkipOutputBuffer;
    private boolean waitingForFirstSyncFrame;
    private boolean waitingForKeys;

    @Retention(RetentionPolicy.SOURCE)
    private @interface AdaptationWorkaroundMode {
    }

    public static class DecoderInitializationException extends Exception {
        private static final int CUSTOM_ERROR_CODE_BASE = -50000;
        private static final int DECODER_QUERY_ERROR = -49998;
        private static final int NO_SUITABLE_DECODER_ERROR = -49999;
        public final String decoderName;
        public final String diagnosticInfo;
        public final String mimeType;
        public final boolean secureDecoderRequired;

        public DecoderInitializationException(Format format, Throwable cause, boolean secureDecoderRequired, int errorCode) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Decoder init failed: [");
            stringBuilder.append(errorCode);
            stringBuilder.append("], ");
            stringBuilder.append(format);
            super(stringBuilder.toString(), cause);
            this.mimeType = format.sampleMimeType;
            this.secureDecoderRequired = secureDecoderRequired;
            this.decoderName = null;
            this.diagnosticInfo = buildCustomDiagnosticInfo(errorCode);
        }

        public DecoderInitializationException(Format format, Throwable cause, boolean secureDecoderRequired, String decoderName) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Decoder init failed: ");
            stringBuilder.append(decoderName);
            stringBuilder.append(", ");
            stringBuilder.append(format);
            super(stringBuilder.toString(), cause);
            this.mimeType = format.sampleMimeType;
            this.secureDecoderRequired = secureDecoderRequired;
            this.decoderName = decoderName;
            this.diagnosticInfo = Util.SDK_INT >= 21 ? getDiagnosticInfoV21(cause) : null;
        }

        @TargetApi(21)
        private static String getDiagnosticInfoV21(Throwable cause) {
            if (cause instanceof CodecException) {
                return ((CodecException) cause).getDiagnosticInfo();
            }
            return null;
        }

        private static String buildCustomDiagnosticInfo(int errorCode) {
            String sign = errorCode < 0 ? "neg_" : TtmlNode.ANONYMOUS_REGION_ID;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("com.google.android.exoplayer.MediaCodecTrackRenderer_");
            stringBuilder.append(sign);
            stringBuilder.append(Math.abs(errorCode));
            return stringBuilder.toString();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface ReconfigurationState {
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface ReinitializationState {
    }

    private boolean feedInputBuffer() throws org.telegram.messenger.exoplayer2.ExoPlaybackException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.mediacodec.MediaCodecRenderer.feedInputBuffer():boolean
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
        r0 = r14.codec;
        r1 = 0;
        if (r0 == 0) goto L_0x01ab;
    L_0x0005:
        r0 = r14.codecReinitializationState;
        r2 = 2;
        if (r0 == r2) goto L_0x01ab;
    L_0x000a:
        r0 = r14.inputStreamEnded;
        if (r0 == 0) goto L_0x0010;
    L_0x000e:
        goto L_0x01ab;
    L_0x0010:
        r0 = r14.inputIndex;
        if (r0 >= 0) goto L_0x0032;
    L_0x0014:
        r0 = r14.codec;
        r3 = 0;
        r0 = r0.dequeueInputBuffer(r3);
        r14.inputIndex = r0;
        r0 = r14.inputIndex;
        if (r0 >= 0) goto L_0x0023;
    L_0x0022:
        return r1;
    L_0x0023:
        r0 = r14.buffer;
        r3 = r14.inputIndex;
        r3 = r14.getInputBuffer(r3);
        r0.data = r3;
        r0 = r14.buffer;
        r0.clear();
    L_0x0032:
        r0 = r14.codecReinitializationState;
        r3 = 1;
        if (r0 != r3) goto L_0x0050;
    L_0x0037:
        r0 = r14.codecNeedsEosPropagationWorkaround;
        if (r0 == 0) goto L_0x003c;
    L_0x003b:
        goto L_0x004d;
    L_0x003c:
        r14.codecReceivedEos = r3;
        r4 = r14.codec;
        r5 = r14.inputIndex;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r10 = 4;
        r4.queueInputBuffer(r5, r6, r7, r8, r10);
        r14.resetInputBuffer();
    L_0x004d:
        r14.codecReinitializationState = r2;
        return r1;
    L_0x0050:
        r0 = r14.codecNeedsAdaptationWorkaroundBuffer;
        if (r0 == 0) goto L_0x0073;
    L_0x0054:
        r14.codecNeedsAdaptationWorkaroundBuffer = r1;
        r0 = r14.buffer;
        r0 = r0.data;
        r1 = ADAPTATION_WORKAROUND_BUFFER;
        r0.put(r1);
        r4 = r14.codec;
        r5 = r14.inputIndex;
        r6 = 0;
        r0 = ADAPTATION_WORKAROUND_BUFFER;
        r7 = r0.length;
        r8 = 0;
        r10 = 0;
        r4.queueInputBuffer(r5, r6, r7, r8, r10);
        r14.resetInputBuffer();
        r14.codecReceivedBuffers = r3;
        return r3;
    L_0x0073:
        r0 = 0;
        r4 = r14.waitingForKeys;
        if (r4 == 0) goto L_0x007a;
    L_0x0078:
        r4 = -4;
        goto L_0x00af;
    L_0x007a:
        r4 = r14.codecReconfigurationState;
        if (r4 != r3) goto L_0x009f;
    L_0x007e:
        r4 = r1;
    L_0x007f:
        r5 = r14.format;
        r5 = r5.initializationData;
        r5 = r5.size();
        if (r4 >= r5) goto L_0x009d;
    L_0x0089:
        r5 = r14.format;
        r5 = r5.initializationData;
        r5 = r5.get(r4);
        r5 = (byte[]) r5;
        r6 = r14.buffer;
        r6 = r6.data;
        r6.put(r5);
        r4 = r4 + 1;
        goto L_0x007f;
    L_0x009d:
        r14.codecReconfigurationState = r2;
    L_0x009f:
        r4 = r14.buffer;
        r4 = r4.data;
        r0 = r4.position();
        r4 = r14.formatHolder;
        r5 = r14.buffer;
        r4 = r14.readSource(r4, r5, r1);
    L_0x00af:
        r5 = -3;
        if (r4 != r5) goto L_0x00b3;
    L_0x00b2:
        return r1;
    L_0x00b3:
        r5 = -5;
        if (r4 != r5) goto L_0x00c9;
    L_0x00b6:
        r1 = r14.codecReconfigurationState;
        if (r1 != r2) goto L_0x00c1;
    L_0x00ba:
        r1 = r14.buffer;
        r1.clear();
        r14.codecReconfigurationState = r3;
    L_0x00c1:
        r1 = r14.formatHolder;
        r1 = r1.format;
        r14.onInputFormatChanged(r1);
        return r3;
    L_0x00c9:
        r5 = r14.buffer;
        r5 = r5.isEndOfStream();
        if (r5 == 0) goto L_0x0108;
    L_0x00d1:
        r5 = r14.codecReconfigurationState;
        if (r5 != r2) goto L_0x00dc;
    L_0x00d5:
        r2 = r14.buffer;
        r2.clear();
        r14.codecReconfigurationState = r3;
    L_0x00dc:
        r14.inputStreamEnded = r3;
        r2 = r14.codecReceivedBuffers;
        if (r2 != 0) goto L_0x00e6;
    L_0x00e2:
        r14.processEndOfStream();
        return r1;
    L_0x00e6:
        r2 = r14.codecNeedsEosPropagationWorkaround;	 Catch:{ CryptoException -> 0x00fe }
        if (r2 == 0) goto L_0x00eb;	 Catch:{ CryptoException -> 0x00fe }
    L_0x00ea:
        goto L_0x00fc;	 Catch:{ CryptoException -> 0x00fe }
    L_0x00eb:
        r14.codecReceivedEos = r3;	 Catch:{ CryptoException -> 0x00fe }
        r5 = r14.codec;	 Catch:{ CryptoException -> 0x00fe }
        r6 = r14.inputIndex;	 Catch:{ CryptoException -> 0x00fe }
        r7 = 0;	 Catch:{ CryptoException -> 0x00fe }
        r8 = 0;	 Catch:{ CryptoException -> 0x00fe }
        r9 = 0;	 Catch:{ CryptoException -> 0x00fe }
        r11 = 4;	 Catch:{ CryptoException -> 0x00fe }
        r5.queueInputBuffer(r6, r7, r8, r9, r11);	 Catch:{ CryptoException -> 0x00fe }
        r14.resetInputBuffer();	 Catch:{ CryptoException -> 0x00fe }
        return r1;
    L_0x00fe:
        r1 = move-exception;
        r2 = r14.getIndex();
        r2 = org.telegram.messenger.exoplayer2.ExoPlaybackException.createForRenderer(r1, r2);
        throw r2;
    L_0x0108:
        r5 = r14.waitingForFirstSyncFrame;
        if (r5 == 0) goto L_0x0120;
        r5 = r14.buffer;
        r5 = r5.isKeyFrame();
        if (r5 != 0) goto L_0x0120;
        r1 = r14.buffer;
        r1.clear();
        r1 = r14.codecReconfigurationState;
        if (r1 != r2) goto L_0x011f;
        r14.codecReconfigurationState = r3;
        return r3;
        r14.waitingForFirstSyncFrame = r1;
        r2 = r14.buffer;
        r2 = r2.isEncrypted();
        r5 = r14.shouldWaitForKeys(r2);
        r14.waitingForKeys = r5;
        r5 = r14.waitingForKeys;
        if (r5 == 0) goto L_0x0133;
        return r1;
        r5 = r14.codecNeedsDiscardToSpsWorkaround;
        if (r5 == 0) goto L_0x014d;
        if (r2 != 0) goto L_0x014d;
        r5 = r14.buffer;
        r5 = r5.data;
        org.telegram.messenger.exoplayer2.util.NalUnitUtil.discardToSps(r5);
        r5 = r14.buffer;
        r5 = r5.data;
        r5 = r5.position();
        if (r5 != 0) goto L_0x014b;
        return r3;
        r14.codecNeedsDiscardToSpsWorkaround = r1;
        r5 = r14.buffer;	 Catch:{ CryptoException -> 0x01a1 }
        r5 = r5.timeUs;	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r14.buffer;	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r7.isDecodeOnly();	 Catch:{ CryptoException -> 0x01a1 }
        if (r7 == 0) goto L_0x0162;	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r14.decodeOnlyPresentationTimestamps;	 Catch:{ CryptoException -> 0x01a1 }
        r8 = java.lang.Long.valueOf(r5);	 Catch:{ CryptoException -> 0x01a1 }
        r7.add(r8);	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r14.buffer;	 Catch:{ CryptoException -> 0x01a1 }
        r7.flip();	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r14.buffer;	 Catch:{ CryptoException -> 0x01a1 }
        r14.onQueueInputBuffer(r7);	 Catch:{ CryptoException -> 0x01a1 }
        if (r2 == 0) goto L_0x017f;	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r14.buffer;	 Catch:{ CryptoException -> 0x01a1 }
        r10 = getFrameworkCryptoInfo(r7, r0);	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r14.codec;	 Catch:{ CryptoException -> 0x01a1 }
        r8 = r14.inputIndex;	 Catch:{ CryptoException -> 0x01a1 }
        r9 = 0;	 Catch:{ CryptoException -> 0x01a1 }
        r13 = 0;	 Catch:{ CryptoException -> 0x01a1 }
        r11 = r5;	 Catch:{ CryptoException -> 0x01a1 }
        r7.queueSecureInputBuffer(r8, r9, r10, r11, r13);	 Catch:{ CryptoException -> 0x01a1 }
        goto L_0x0191;	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r14.codec;	 Catch:{ CryptoException -> 0x01a1 }
        r8 = r14.inputIndex;	 Catch:{ CryptoException -> 0x01a1 }
        r9 = 0;	 Catch:{ CryptoException -> 0x01a1 }
        r10 = r14.buffer;	 Catch:{ CryptoException -> 0x01a1 }
        r10 = r10.data;	 Catch:{ CryptoException -> 0x01a1 }
        r10 = r10.limit();	 Catch:{ CryptoException -> 0x01a1 }
        r13 = 0;	 Catch:{ CryptoException -> 0x01a1 }
        r11 = r5;	 Catch:{ CryptoException -> 0x01a1 }
        r7.queueInputBuffer(r8, r9, r10, r11, r13);	 Catch:{ CryptoException -> 0x01a1 }
        r14.resetInputBuffer();	 Catch:{ CryptoException -> 0x01a1 }
        r14.codecReceivedBuffers = r3;	 Catch:{ CryptoException -> 0x01a1 }
        r14.codecReconfigurationState = r1;	 Catch:{ CryptoException -> 0x01a1 }
        r1 = r14.decoderCounters;	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r1.inputBufferCount;	 Catch:{ CryptoException -> 0x01a1 }
        r7 = r7 + r3;	 Catch:{ CryptoException -> 0x01a1 }
        r1.inputBufferCount = r7;	 Catch:{ CryptoException -> 0x01a1 }
        return r3;
    L_0x01a1:
        r1 = move-exception;
        r3 = r14.getIndex();
        r3 = org.telegram.messenger.exoplayer2.ExoPlaybackException.createForRenderer(r1, r3);
        throw r3;
    L_0x01ab:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.mediacodec.MediaCodecRenderer.feedInputBuffer():boolean");
    }

    protected abstract void configureCodec(MediaCodecInfo mediaCodecInfo, MediaCodec mediaCodec, Format format, MediaCrypto mediaCrypto) throws DecoderQueryException;

    protected abstract boolean processOutputBuffer(long j, long j2, MediaCodec mediaCodec, ByteBuffer byteBuffer, int i, int i2, long j3, boolean z) throws ExoPlaybackException;

    protected abstract int supportsFormat(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, Format format) throws DecoderQueryException;

    public MediaCodecRenderer(int trackType, MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys) {
        super(trackType);
        Assertions.checkState(Util.SDK_INT >= 16);
        this.mediaCodecSelector = (MediaCodecSelector) Assertions.checkNotNull(mediaCodecSelector);
        this.drmSessionManager = drmSessionManager;
        this.playClearSamplesWithoutKeys = playClearSamplesWithoutKeys;
        this.buffer = new DecoderInputBuffer(0);
        this.flagsOnlyBuffer = DecoderInputBuffer.newFlagsOnlyInstance();
        this.formatHolder = new FormatHolder();
        this.decodeOnlyPresentationTimestamps = new ArrayList();
        this.outputBufferInfo = new BufferInfo();
        this.codecReconfigurationState = 0;
        this.codecReinitializationState = 0;
    }

    public final int supportsMixedMimeTypeAdaptation() {
        return 8;
    }

    public final int supportsFormat(Format format) throws ExoPlaybackException {
        try {
            return supportsFormat(this.mediaCodecSelector, this.drmSessionManager, format);
        } catch (DecoderQueryException e) {
            throw ExoPlaybackException.createForRenderer(e, getIndex());
        }
    }

    protected MediaCodecInfo getDecoderInfo(MediaCodecSelector mediaCodecSelector, Format format, boolean requiresSecureDecoder) throws DecoderQueryException {
        return mediaCodecSelector.getDecoderInfo(format.sampleMimeType, requiresSecureDecoder);
    }

    protected final void maybeInitCodec() throws ExoPlaybackException {
        if (this.codec == null) {
            if (this.format != null) {
                String str;
                StringBuilder stringBuilder;
                this.drmSession = this.pendingDrmSession;
                String mimeType = this.format.sampleMimeType;
                MediaCrypto wrappedMediaCrypto = null;
                boolean drmSessionRequiresSecureDecoder = false;
                if (this.drmSession != null) {
                    FrameworkMediaCrypto mediaCrypto = (FrameworkMediaCrypto) this.drmSession.getMediaCrypto();
                    if (mediaCrypto != null) {
                        wrappedMediaCrypto = mediaCrypto.getWrappedMediaCrypto();
                        drmSessionRequiresSecureDecoder = mediaCrypto.requiresSecureDecoderComponent(mimeType);
                    } else if (this.drmSession.getError() == null) {
                        return;
                    }
                }
                if (this.codecInfo == null) {
                    try {
                        this.codecInfo = getDecoderInfo(this.mediaCodecSelector, this.format, drmSessionRequiresSecureDecoder);
                        if (this.codecInfo == null && drmSessionRequiresSecureDecoder) {
                            this.codecInfo = getDecoderInfo(this.mediaCodecSelector, this.format, false);
                            if (this.codecInfo != null) {
                                str = TAG;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("Drm session requires secure decoder for ");
                                stringBuilder.append(mimeType);
                                stringBuilder.append(", but no secure decoder available. Trying to proceed with ");
                                stringBuilder.append(this.codecInfo.name);
                                stringBuilder.append(".");
                                Log.w(str, stringBuilder.toString());
                            }
                        }
                    } catch (Throwable e) {
                        throwDecoderInitError(new DecoderInitializationException(this.format, e, drmSessionRequiresSecureDecoder, -49998));
                    }
                    if (this.codecInfo == null) {
                        throwDecoderInitError(new DecoderInitializationException(this.format, null, drmSessionRequiresSecureDecoder, -49999));
                    }
                }
                if (shouldInitCodec(this.codecInfo)) {
                    str = this.codecInfo.name;
                    this.codecAdaptationWorkaroundMode = codecAdaptationWorkaroundMode(str);
                    this.codecNeedsDiscardToSpsWorkaround = codecNeedsDiscardToSpsWorkaround(str, this.format);
                    this.codecNeedsFlushWorkaround = codecNeedsFlushWorkaround(str);
                    this.codecNeedsEosPropagationWorkaround = codecNeedsEosPropagationWorkaround(str);
                    this.codecNeedsEosFlushWorkaround = codecNeedsEosFlushWorkaround(str);
                    this.codecNeedsEosOutputExceptionWorkaround = codecNeedsEosOutputExceptionWorkaround(str);
                    this.codecNeedsMonoChannelCountWorkaround = codecNeedsMonoChannelCountWorkaround(str, this.format);
                    try {
                        long codecInitializingTimestamp = SystemClock.elapsedRealtime();
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("createCodec:");
                        stringBuilder.append(str);
                        TraceUtil.beginSection(stringBuilder.toString());
                        this.codec = MediaCodec.createByCodecName(str);
                        TraceUtil.endSection();
                        TraceUtil.beginSection("configureCodec");
                        configureCodec(this.codecInfo, this.codec, this.format, wrappedMediaCrypto);
                        TraceUtil.endSection();
                        TraceUtil.beginSection("startCodec");
                        this.codec.start();
                        TraceUtil.endSection();
                        long codecInitializedTimestamp = SystemClock.elapsedRealtime();
                        onCodecInitialized(str, codecInitializedTimestamp, codecInitializedTimestamp - codecInitializingTimestamp);
                        getCodecBuffers();
                    } catch (Throwable e2) {
                        throwDecoderInitError(new DecoderInitializationException(this.format, e2, drmSessionRequiresSecureDecoder, str));
                    }
                    this.codecHotswapDeadlineMs = getState() == 2 ? SystemClock.elapsedRealtime() + MAX_CODEC_HOTSWAP_TIME_MS : C.TIME_UNSET;
                    resetInputBuffer();
                    resetOutputBuffer();
                    this.waitingForFirstSyncFrame = true;
                    DecoderCounters decoderCounters = this.decoderCounters;
                    decoderCounters.decoderInitCount++;
                }
            }
        }
    }

    private void throwDecoderInitError(DecoderInitializationException e) throws ExoPlaybackException {
        throw ExoPlaybackException.createForRenderer(e, getIndex());
    }

    protected boolean shouldInitCodec(MediaCodecInfo codecInfo) {
        return true;
    }

    protected final MediaCodec getCodec() {
        return this.codec;
    }

    protected final MediaCodecInfo getCodecInfo() {
        return this.codecInfo;
    }

    protected final MediaFormat getMediaFormatForPlayback(Format format) {
        MediaFormat mediaFormat = format.getFrameworkMediaFormatV16();
        if (Util.SDK_INT >= 23) {
            configureMediaFormatForPlaybackV23(mediaFormat);
        }
        return mediaFormat;
    }

    protected void onEnabled(boolean joining) throws ExoPlaybackException {
        this.decoderCounters = new DecoderCounters();
    }

    protected void onPositionReset(long positionUs, boolean joining) throws ExoPlaybackException {
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.codec != null) {
            flushCodec();
        }
    }

    protected void onDisabled() {
        this.format = null;
        try {
            releaseCodec();
            try {
                if (this.drmSession != null) {
                    this.drmSessionManager.releaseSession(this.drmSession);
                }
                try {
                    if (!(this.pendingDrmSession == null || this.pendingDrmSession == this.drmSession)) {
                        this.drmSessionManager.releaseSession(this.pendingDrmSession);
                    }
                    this.drmSession = null;
                    this.pendingDrmSession = null;
                } catch (Throwable th) {
                    this.drmSession = null;
                    this.pendingDrmSession = null;
                }
            } catch (Throwable th2) {
                this.drmSession = null;
                this.pendingDrmSession = null;
            }
        } catch (Throwable th3) {
            this.drmSession = null;
            this.pendingDrmSession = null;
        }
    }

    protected void releaseCodec() {
        this.codecHotswapDeadlineMs = C.TIME_UNSET;
        resetInputBuffer();
        resetOutputBuffer();
        this.waitingForKeys = false;
        this.shouldSkipOutputBuffer = false;
        this.decodeOnlyPresentationTimestamps.clear();
        resetCodecBuffers();
        this.codecInfo = null;
        this.codecReconfigured = false;
        this.codecReceivedBuffers = false;
        this.codecNeedsDiscardToSpsWorkaround = false;
        this.codecNeedsFlushWorkaround = false;
        this.codecAdaptationWorkaroundMode = 0;
        this.codecNeedsEosPropagationWorkaround = false;
        this.codecNeedsEosFlushWorkaround = false;
        this.codecNeedsMonoChannelCountWorkaround = false;
        this.codecNeedsAdaptationWorkaroundBuffer = false;
        this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
        this.codecReceivedEos = false;
        this.codecReconfigurationState = 0;
        this.codecReinitializationState = 0;
        if (this.codec != null) {
            DecoderCounters decoderCounters = this.decoderCounters;
            decoderCounters.decoderReleaseCount++;
            try {
                this.codec.stop();
                try {
                    this.codec.release();
                    this.codec = null;
                    if (!(this.drmSession == null || this.pendingDrmSession == this.drmSession)) {
                        try {
                            this.drmSessionManager.releaseSession(this.drmSession);
                        } finally {
                            this.drmSession = null;
                        }
                    }
                } catch (Throwable th) {
                    this.codec = null;
                    if (!(this.drmSession == null || this.pendingDrmSession == this.drmSession)) {
                        this.drmSessionManager.releaseSession(this.drmSession);
                    }
                } finally {
                    this.drmSession = null;
                }
            } catch (Throwable th2) {
                this.codec = null;
                if (!(this.drmSession == null || this.pendingDrmSession == this.drmSession)) {
                    try {
                        this.drmSessionManager.releaseSession(this.drmSession);
                    } finally {
                        this.drmSession = null;
                    }
                }
            } finally {
                this.drmSession = null;
            }
        }
    }

    protected void onStarted() {
    }

    protected void onStopped() {
    }

    public void render(long positionUs, long elapsedRealtimeUs) throws ExoPlaybackException {
        if (this.outputStreamEnded) {
            renderToEndOfStream();
            return;
        }
        int result;
        if (this.format == null) {
            this.flagsOnlyBuffer.clear();
            result = readSource(this.formatHolder, this.flagsOnlyBuffer, true);
            if (result == -5) {
                onInputFormatChanged(this.formatHolder.format);
            } else if (result == -4) {
                Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                this.inputStreamEnded = true;
                processEndOfStream();
                return;
            } else {
                return;
            }
        }
        maybeInitCodec();
        if (this.codec != null) {
            TraceUtil.beginSection("drainAndFeed");
            while (drainOutputBuffer(positionUs, elapsedRealtimeUs)) {
            }
            while (feedInputBuffer()) {
            }
            TraceUtil.endSection();
        } else {
            DecoderCounters decoderCounters = this.decoderCounters;
            decoderCounters.skippedInputBufferCount += skipSource(positionUs);
            this.flagsOnlyBuffer.clear();
            result = readSource(this.formatHolder, this.flagsOnlyBuffer, false);
            if (result == -5) {
                onInputFormatChanged(this.formatHolder.format);
            } else if (result == -4) {
                Assertions.checkState(this.flagsOnlyBuffer.isEndOfStream());
                this.inputStreamEnded = true;
                processEndOfStream();
            }
        }
        this.decoderCounters.ensureUpdated();
    }

    protected void flushCodec() throws ExoPlaybackException {
        this.codecHotswapDeadlineMs = C.TIME_UNSET;
        resetInputBuffer();
        resetOutputBuffer();
        this.waitingForFirstSyncFrame = true;
        this.waitingForKeys = false;
        this.shouldSkipOutputBuffer = false;
        this.decodeOnlyPresentationTimestamps.clear();
        this.codecNeedsAdaptationWorkaroundBuffer = false;
        this.shouldSkipAdaptationWorkaroundOutputBuffer = false;
        if (!this.codecNeedsFlushWorkaround) {
            if (!this.codecNeedsEosFlushWorkaround || !this.codecReceivedEos) {
                if (this.codecReinitializationState != 0) {
                    releaseCodec();
                    maybeInitCodec();
                } else {
                    this.codec.flush();
                    this.codecReceivedBuffers = false;
                }
                if (this.codecReconfigured && this.format != null) {
                    this.codecReconfigurationState = 1;
                    return;
                }
            }
        }
        releaseCodec();
        maybeInitCodec();
        if (this.codecReconfigured) {
        }
    }

    private void getCodecBuffers() {
        if (Util.SDK_INT < 21) {
            this.inputBuffers = this.codec.getInputBuffers();
            this.outputBuffers = this.codec.getOutputBuffers();
        }
    }

    private void resetCodecBuffers() {
        if (Util.SDK_INT < 21) {
            this.inputBuffers = null;
            this.outputBuffers = null;
        }
    }

    private ByteBuffer getInputBuffer(int inputIndex) {
        if (Util.SDK_INT >= 21) {
            return this.codec.getInputBuffer(inputIndex);
        }
        return this.inputBuffers[inputIndex];
    }

    private ByteBuffer getOutputBuffer(int outputIndex) {
        if (Util.SDK_INT >= 21) {
            return this.codec.getOutputBuffer(outputIndex);
        }
        return this.outputBuffers[outputIndex];
    }

    private boolean hasOutputBuffer() {
        return this.outputIndex >= 0;
    }

    private void resetInputBuffer() {
        this.inputIndex = -1;
        this.buffer.data = null;
    }

    private void resetOutputBuffer() {
        this.outputIndex = -1;
        this.outputBuffer = null;
    }

    private static CryptoInfo getFrameworkCryptoInfo(DecoderInputBuffer buffer, int adaptiveReconfigurationBytes) {
        CryptoInfo cryptoInfo = buffer.cryptoInfo.getFrameworkCryptoInfoV16();
        if (adaptiveReconfigurationBytes == 0) {
            return cryptoInfo;
        }
        if (cryptoInfo.numBytesOfClearData == null) {
            cryptoInfo.numBytesOfClearData = new int[1];
        }
        int[] iArr = cryptoInfo.numBytesOfClearData;
        iArr[0] = iArr[0] + adaptiveReconfigurationBytes;
        return cryptoInfo;
    }

    private boolean shouldWaitForKeys(boolean bufferEncrypted) throws ExoPlaybackException {
        boolean z = false;
        if (this.drmSession != null) {
            if (bufferEncrypted || !this.playClearSamplesWithoutKeys) {
                int drmSessionState = this.drmSession.getState();
                if (drmSessionState == 1) {
                    throw ExoPlaybackException.createForRenderer(this.drmSession.getError(), getIndex());
                }
                if (drmSessionState != 4) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    protected void onCodecInitialized(String name, long initializedTimestampMs, long initializationDurationMs) {
    }

    protected void onInputFormatChanged(Format newFormat) throws ExoPlaybackException {
        Format oldFormat = this.format;
        this.format = newFormat;
        boolean z = true;
        if (Util.areEqual(this.format.drmInitData, oldFormat == null ? null : oldFormat.drmInitData) ^ true) {
            if (this.format.drmInitData == null) {
                this.pendingDrmSession = null;
            } else if (this.drmSessionManager == null) {
                throw ExoPlaybackException.createForRenderer(new IllegalStateException("Media requires a DrmSessionManager"), getIndex());
            } else {
                this.pendingDrmSession = this.drmSessionManager.acquireSession(Looper.myLooper(), this.format.drmInitData);
                if (this.pendingDrmSession == this.drmSession) {
                    this.drmSessionManager.releaseSession(this.pendingDrmSession);
                }
            }
        }
        if (this.pendingDrmSession == this.drmSession && this.codec != null && canReconfigureCodec(this.codec, this.codecInfo.adaptive, oldFormat, this.format)) {
            this.codecReconfigured = true;
            this.codecReconfigurationState = 1;
            if (this.codecAdaptationWorkaroundMode != 2) {
                if (this.codecAdaptationWorkaroundMode != 1 || this.format.width != oldFormat.width || this.format.height != oldFormat.height) {
                    z = false;
                }
            }
            this.codecNeedsAdaptationWorkaroundBuffer = z;
        } else if (this.codecReceivedBuffers) {
            this.codecReinitializationState = 1;
        } else {
            releaseCodec();
            maybeInitCodec();
        }
    }

    protected void onOutputFormatChanged(MediaCodec codec, MediaFormat outputFormat) throws ExoPlaybackException {
    }

    protected void onQueueInputBuffer(DecoderInputBuffer buffer) {
    }

    protected void onProcessedOutputBuffer(long presentationTimeUs) {
    }

    protected boolean canReconfigureCodec(MediaCodec codec, boolean codecIsAdaptive, Format oldFormat, Format newFormat) {
        return false;
    }

    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    public boolean isReady() {
        return (this.format == null || this.waitingForKeys || (!isSourceReady() && !hasOutputBuffer() && (this.codecHotswapDeadlineMs == C.TIME_UNSET || SystemClock.elapsedRealtime() >= this.codecHotswapDeadlineMs))) ? false : true;
    }

    protected long getDequeueOutputBufferTimeoutUs() {
        return 0;
    }

    private boolean drainOutputBuffer(long positionUs, long elapsedRealtimeUs) throws ExoPlaybackException {
        MediaCodecRenderer mediaCodecRenderer = this;
        if (!hasOutputBuffer()) {
            int outputIndex;
            if (mediaCodecRenderer.codecNeedsEosOutputExceptionWorkaround && mediaCodecRenderer.codecReceivedEos) {
                try {
                    outputIndex = mediaCodecRenderer.codec.dequeueOutputBuffer(mediaCodecRenderer.outputBufferInfo, getDequeueOutputBufferTimeoutUs());
                } catch (IllegalStateException e) {
                    processEndOfStream();
                    if (mediaCodecRenderer.outputStreamEnded) {
                        releaseCodec();
                    }
                    return false;
                }
            }
            outputIndex = mediaCodecRenderer.codec.dequeueOutputBuffer(mediaCodecRenderer.outputBufferInfo, getDequeueOutputBufferTimeoutUs());
            if (outputIndex >= 0) {
                if (mediaCodecRenderer.shouldSkipAdaptationWorkaroundOutputBuffer) {
                    mediaCodecRenderer.shouldSkipAdaptationWorkaroundOutputBuffer = false;
                    mediaCodecRenderer.codec.releaseOutputBuffer(outputIndex, false);
                    return true;
                } else if ((mediaCodecRenderer.outputBufferInfo.flags & 4) != 0) {
                    processEndOfStream();
                    return false;
                } else {
                    mediaCodecRenderer.outputIndex = outputIndex;
                    mediaCodecRenderer.outputBuffer = getOutputBuffer(outputIndex);
                    if (mediaCodecRenderer.outputBuffer != null) {
                        mediaCodecRenderer.outputBuffer.position(mediaCodecRenderer.outputBufferInfo.offset);
                        mediaCodecRenderer.outputBuffer.limit(mediaCodecRenderer.outputBufferInfo.offset + mediaCodecRenderer.outputBufferInfo.size);
                    }
                    mediaCodecRenderer.shouldSkipOutputBuffer = shouldSkipOutputBuffer(mediaCodecRenderer.outputBufferInfo.presentationTimeUs);
                }
            } else if (outputIndex == -2) {
                processOutputFormat();
                return true;
            } else if (outputIndex == -3) {
                processOutputBuffersChanged();
                return true;
            } else {
                if (mediaCodecRenderer.codecNeedsEosPropagationWorkaround && (mediaCodecRenderer.inputStreamEnded || mediaCodecRenderer.codecReinitializationState == 2)) {
                    processEndOfStream();
                }
                return false;
            }
        }
        if (mediaCodecRenderer.codecNeedsEosOutputExceptionWorkaround && mediaCodecRenderer.codecReceivedEos) {
            try {
                boolean processedOutputBuffer = processOutputBuffer(positionUs, elapsedRealtimeUs, mediaCodecRenderer.codec, mediaCodecRenderer.outputBuffer, mediaCodecRenderer.outputIndex, mediaCodecRenderer.outputBufferInfo.flags, mediaCodecRenderer.outputBufferInfo.presentationTimeUs, mediaCodecRenderer.shouldSkipOutputBuffer);
            } catch (IllegalStateException e2) {
                processEndOfStream();
                if (mediaCodecRenderer.outputStreamEnded) {
                    releaseCodec();
                }
                return false;
            }
        }
        processedOutputBuffer = processOutputBuffer(positionUs, elapsedRealtimeUs, mediaCodecRenderer.codec, mediaCodecRenderer.outputBuffer, mediaCodecRenderer.outputIndex, mediaCodecRenderer.outputBufferInfo.flags, mediaCodecRenderer.outputBufferInfo.presentationTimeUs, mediaCodecRenderer.shouldSkipOutputBuffer);
        if (!processedOutputBuffer) {
            return false;
        }
        onProcessedOutputBuffer(mediaCodecRenderer.outputBufferInfo.presentationTimeUs);
        resetOutputBuffer();
        return true;
    }

    private void processOutputFormat() throws ExoPlaybackException {
        MediaFormat format = this.codec.getOutputFormat();
        if (this.codecAdaptationWorkaroundMode != 0 && format.getInteger("width") == 32 && format.getInteger("height") == 32) {
            this.shouldSkipAdaptationWorkaroundOutputBuffer = true;
            return;
        }
        if (this.codecNeedsMonoChannelCountWorkaround) {
            format.setInteger("channel-count", 1);
        }
        onOutputFormatChanged(this.codec, format);
    }

    private void processOutputBuffersChanged() {
        if (Util.SDK_INT < 21) {
            this.outputBuffers = this.codec.getOutputBuffers();
        }
    }

    protected void renderToEndOfStream() throws ExoPlaybackException {
    }

    private void processEndOfStream() throws ExoPlaybackException {
        if (this.codecReinitializationState == 2) {
            releaseCodec();
            maybeInitCodec();
            return;
        }
        this.outputStreamEnded = true;
        renderToEndOfStream();
    }

    private boolean shouldSkipOutputBuffer(long presentationTimeUs) {
        int size = this.decodeOnlyPresentationTimestamps.size();
        for (int i = 0; i < size; i++) {
            if (((Long) this.decodeOnlyPresentationTimestamps.get(i)).longValue() == presentationTimeUs) {
                this.decodeOnlyPresentationTimestamps.remove(i);
                return true;
            }
        }
        return false;
    }

    @TargetApi(23)
    private static void configureMediaFormatForPlaybackV23(MediaFormat mediaFormat) {
        mediaFormat.setInteger("priority", 0);
    }

    private static boolean codecNeedsFlushWorkaround(String name) {
        if (Util.SDK_INT >= 18 && !(Util.SDK_INT == 18 && ("OMX.SEC.avc.dec".equals(name) || "OMX.SEC.avc.dec.secure".equals(name)))) {
            if (Util.SDK_INT == 19 && Util.MODEL.startsWith("SM-G800")) {
                if (!"OMX.Exynos.avc.dec".equals(name)) {
                    if ("OMX.Exynos.avc.dec.secure".equals(name)) {
                    }
                }
            }
            return false;
        }
        return true;
    }

    private int codecAdaptationWorkaroundMode(String name) {
        if (Util.SDK_INT <= 25 && "OMX.Exynos.avc.dec.secure".equals(name) && (Util.MODEL.startsWith("SM-T585") || Util.MODEL.startsWith("SM-A510") || Util.MODEL.startsWith("SM-A520") || Util.MODEL.startsWith("SM-J700"))) {
            return 2;
        }
        if (Util.SDK_INT >= 24 || ((!"OMX.Nvidia.h264.decode".equals(name) && !"OMX.Nvidia.h264.decode.secure".equals(name)) || (!"flounder".equals(Util.DEVICE) && !"flounder_lte".equals(Util.DEVICE) && !"grouper".equals(Util.DEVICE) && !"tilapia".equals(Util.DEVICE)))) {
            return 0;
        }
        return 1;
    }

    private static boolean codecNeedsDiscardToSpsWorkaround(String name, Format format) {
        return Util.SDK_INT < 21 && format.initializationData.isEmpty() && "OMX.MTK.VIDEO.DECODER.AVC".equals(name);
    }

    private static boolean codecNeedsEosPropagationWorkaround(String name) {
        return Util.SDK_INT <= 17 && ("OMX.rk.video_decoder.avc".equals(name) || "OMX.allwinner.video.decoder.avc".equals(name));
    }

    private static boolean codecNeedsEosFlushWorkaround(String name) {
        return (Util.SDK_INT <= 23 && "OMX.google.vorbis.decoder".equals(name)) || (Util.SDK_INT <= 19 && "hb2000".equals(Util.DEVICE) && ("OMX.amlogic.avc.decoder.awesome".equals(name) || "OMX.amlogic.avc.decoder.awesome.secure".equals(name)));
    }

    private static boolean codecNeedsEosOutputExceptionWorkaround(String name) {
        return Util.SDK_INT == 21 && "OMX.google.aac.decoder".equals(name);
    }

    private static boolean codecNeedsMonoChannelCountWorkaround(String name, Format format) {
        if (Util.SDK_INT <= 18 && format.channelCount == 1 && "OMX.MTK.AUDIO.DECODER.MP3".equals(name)) {
            return true;
        }
        return false;
    }
}
