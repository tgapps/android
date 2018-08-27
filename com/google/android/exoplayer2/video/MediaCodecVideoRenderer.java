package com.google.android.exoplayer2.video;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.media.MediaCodec;
import android.media.MediaCodec.OnFrameRenderedListener;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import com.google.android.exoplayer2.BaseRenderer;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.FrameworkMediaCrypto;
import com.google.android.exoplayer2.mediacodec.MediaCodecInfo;
import com.google.android.exoplayer2.mediacodec.MediaCodecRenderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil;
import com.google.android.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import com.google.android.exoplayer2.mediacodec.MediaFormatUtil;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.MimeTypes;
import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener.EventDispatcher;
import java.nio.ByteBuffer;
import java.util.List;

@TargetApi(16)
public class MediaCodecVideoRenderer extends MediaCodecRenderer {
    private static final String KEY_CROP_BOTTOM = "crop-bottom";
    private static final String KEY_CROP_LEFT = "crop-left";
    private static final String KEY_CROP_RIGHT = "crop-right";
    private static final String KEY_CROP_TOP = "crop-top";
    private static final int MAX_PENDING_OUTPUT_STREAM_OFFSET_COUNT = 10;
    private static final int[] STANDARD_LONG_EDGE_VIDEO_PX = new int[]{1920, 1600, 1440, 1280, 960, 854, 640, 540, 480};
    private static final String TAG = "MediaCodecVideoRenderer";
    private static boolean deviceNeedsSetOutputSurfaceWorkaround;
    private static boolean evaluatedDeviceNeedsSetOutputSurfaceWorkaround;
    private final long allowedJoiningTimeMs;
    private int buffersInCodecCount;
    private CodecMaxValues codecMaxValues;
    private boolean codecNeedsSetOutputSurfaceWorkaround;
    private int consecutiveDroppedFrameCount;
    private final Context context;
    private int currentHeight;
    private float currentPixelWidthHeightRatio;
    private int currentUnappliedRotationDegrees;
    private int currentWidth;
    private final boolean deviceNeedsAutoFrcWorkaround;
    private long droppedFrameAccumulationStartTimeMs;
    private int droppedFrames;
    private Surface dummySurface;
    private final EventDispatcher eventDispatcher;
    private final VideoFrameReleaseTimeHelper frameReleaseTimeHelper;
    private long initialPositionUs;
    private long joiningDeadlineMs;
    private long lastInputTimeUs;
    private long lastRenderTimeUs;
    private final int maxDroppedFramesToNotify;
    private long outputStreamOffsetUs;
    private int pendingOutputStreamOffsetCount;
    private final long[] pendingOutputStreamOffsetsUs;
    private final long[] pendingOutputStreamSwitchTimesUs;
    private float pendingPixelWidthHeightRatio;
    private int pendingRotationDegrees;
    private boolean renderedFirstFrame;
    private int reportedHeight;
    private float reportedPixelWidthHeightRatio;
    private int reportedUnappliedRotationDegrees;
    private int reportedWidth;
    private int scalingMode;
    private Surface surface;
    private boolean tunneling;
    private int tunnelingAudioSessionId;
    OnFrameRenderedListenerV23 tunnelingOnFrameRenderedListener;

    protected static final class CodecMaxValues {
        public final int height;
        public final int inputSize;
        public final int width;

        public CodecMaxValues(int width, int height, int inputSize) {
            this.width = width;
            this.height = height;
            this.inputSize = inputSize;
        }
    }

    @TargetApi(23)
    private final class OnFrameRenderedListenerV23 implements OnFrameRenderedListener {
        private OnFrameRenderedListenerV23(MediaCodec codec) {
            codec.setOnFrameRenderedListener(this, new Handler());
        }

        public void onFrameRendered(MediaCodec codec, long presentationTimeUs, long nanoTime) {
            if (this == MediaCodecVideoRenderer.this.tunnelingOnFrameRenderedListener) {
                MediaCodecVideoRenderer.this.maybeNotifyRenderedFirstFrame();
            }
        }
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector) {
        this(context, mediaCodecSelector, 0);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long allowedJoiningTimeMs) {
        this(context, mediaCodecSelector, allowedJoiningTimeMs, null, null, -1);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long allowedJoiningTimeMs, Handler eventHandler, VideoRendererEventListener eventListener, int maxDroppedFrameCountToNotify) {
        this(context, mediaCodecSelector, allowedJoiningTimeMs, null, false, eventHandler, eventListener, maxDroppedFrameCountToNotify);
    }

    public MediaCodecVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector, long allowedJoiningTimeMs, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, VideoRendererEventListener eventListener, int maxDroppedFramesToNotify) {
        super(2, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, 30.0f);
        this.allowedJoiningTimeMs = allowedJoiningTimeMs;
        this.maxDroppedFramesToNotify = maxDroppedFramesToNotify;
        this.context = context.getApplicationContext();
        this.frameReleaseTimeHelper = new VideoFrameReleaseTimeHelper(this.context);
        this.eventDispatcher = new EventDispatcher(eventHandler, eventListener);
        this.deviceNeedsAutoFrcWorkaround = deviceNeedsAutoFrcWorkaround();
        this.pendingOutputStreamOffsetsUs = new long[10];
        this.pendingOutputStreamSwitchTimesUs = new long[10];
        this.outputStreamOffsetUs = C.TIME_UNSET;
        this.lastInputTimeUs = C.TIME_UNSET;
        this.joiningDeadlineMs = C.TIME_UNSET;
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.scalingMode = 1;
        clearReportedVideoSize();
    }

    protected int supportsFormat(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, Format format) throws DecoderQueryException {
        if (!MimeTypes.isVideo(format.sampleMimeType)) {
            return 0;
        }
        boolean requiresSecureDecryption = false;
        DrmInitData drmInitData = format.drmInitData;
        if (drmInitData != null) {
            for (int i = 0; i < drmInitData.schemeDataCount; i++) {
                requiresSecureDecryption |= drmInitData.get(i).requiresSecureDecryption;
            }
        }
        List<MediaCodecInfo> decoderInfos = mediaCodecSelector.getDecoderInfos(format, requiresSecureDecryption);
        if (decoderInfos.isEmpty()) {
            return (!requiresSecureDecryption || mediaCodecSelector.getDecoderInfos(format, false).isEmpty()) ? 1 : 2;
        } else {
            if (!BaseRenderer.supportsFormatDrm(drmSessionManager, drmInitData)) {
                return 2;
            }
            MediaCodecInfo decoderInfo = (MediaCodecInfo) decoderInfos.get(0);
            boolean decoderCapable = decoderInfo.isCodecSupported(format.codecs);
            if (decoderCapable && format.width > 0 && format.height > 0) {
                if (Util.SDK_INT >= 21) {
                    decoderCapable = decoderInfo.isVideoSizeAndRateSupportedV21(format.width, format.height, (double) format.frameRate);
                } else {
                    decoderCapable = format.width * format.height <= MediaCodecUtil.maxH264DecodableFrameSize();
                    if (!decoderCapable) {
                        Log.d(TAG, "FalseCheck [legacyFrameSize, " + format.width + "x" + format.height + "] [" + Util.DEVICE_DEBUG_INFO + "]");
                    }
                }
            }
            return ((decoderInfo.adaptive ? 16 : 8) | (decoderInfo.tunneling ? 32 : 0)) | (decoderCapable ? 4 : 3);
        }
    }

    protected void onEnabled(boolean joining) throws ExoPlaybackException {
        super.onEnabled(joining);
        this.tunnelingAudioSessionId = getConfiguration().tunnelingAudioSessionId;
        this.tunneling = this.tunnelingAudioSessionId != 0;
        this.eventDispatcher.enabled(this.decoderCounters);
        this.frameReleaseTimeHelper.enable();
    }

    protected void onStreamChanged(Format[] formats, long offsetUs) throws ExoPlaybackException {
        if (this.outputStreamOffsetUs == C.TIME_UNSET) {
            this.outputStreamOffsetUs = offsetUs;
        } else {
            if (this.pendingOutputStreamOffsetCount == this.pendingOutputStreamOffsetsUs.length) {
                Log.w(TAG, "Too many stream changes, so dropping offset: " + this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1]);
            } else {
                this.pendingOutputStreamOffsetCount++;
            }
            this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1] = offsetUs;
            this.pendingOutputStreamSwitchTimesUs[this.pendingOutputStreamOffsetCount - 1] = this.lastInputTimeUs;
        }
        super.onStreamChanged(formats, offsetUs);
    }

    protected void onPositionReset(long positionUs, boolean joining) throws ExoPlaybackException {
        super.onPositionReset(positionUs, joining);
        clearRenderedFirstFrame();
        this.initialPositionUs = C.TIME_UNSET;
        this.consecutiveDroppedFrameCount = 0;
        this.lastInputTimeUs = C.TIME_UNSET;
        if (this.pendingOutputStreamOffsetCount != 0) {
            this.outputStreamOffsetUs = this.pendingOutputStreamOffsetsUs[this.pendingOutputStreamOffsetCount - 1];
            this.pendingOutputStreamOffsetCount = 0;
        }
        if (joining) {
            setJoiningDeadlineMs();
        } else {
            this.joiningDeadlineMs = C.TIME_UNSET;
        }
    }

    public boolean isReady() {
        if (super.isReady() && (this.renderedFirstFrame || ((this.dummySurface != null && this.surface == this.dummySurface) || getCodec() == null || this.tunneling))) {
            this.joiningDeadlineMs = C.TIME_UNSET;
            return true;
        } else if (this.joiningDeadlineMs == C.TIME_UNSET) {
            return false;
        } else {
            if (SystemClock.elapsedRealtime() < this.joiningDeadlineMs) {
                return true;
            }
            this.joiningDeadlineMs = C.TIME_UNSET;
            return false;
        }
    }

    protected void onStarted() {
        super.onStarted();
        this.droppedFrames = 0;
        this.droppedFrameAccumulationStartTimeMs = SystemClock.elapsedRealtime();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000;
    }

    protected void onStopped() {
        this.joiningDeadlineMs = C.TIME_UNSET;
        maybeNotifyDroppedFrames();
        super.onStopped();
    }

    protected void onDisabled() {
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.currentPixelWidthHeightRatio = -1.0f;
        this.pendingPixelWidthHeightRatio = -1.0f;
        this.outputStreamOffsetUs = C.TIME_UNSET;
        this.lastInputTimeUs = C.TIME_UNSET;
        this.pendingOutputStreamOffsetCount = 0;
        clearReportedVideoSize();
        clearRenderedFirstFrame();
        this.frameReleaseTimeHelper.disable();
        this.tunnelingOnFrameRenderedListener = null;
        this.tunneling = false;
        try {
            super.onDisabled();
        } finally {
            this.decoderCounters.ensureUpdated();
            this.eventDispatcher.disabled(this.decoderCounters);
        }
    }

    public void handleMessage(int messageType, Object message) throws ExoPlaybackException {
        if (messageType == 1) {
            setSurface((Surface) message);
        } else if (messageType == 4) {
            this.scalingMode = ((Integer) message).intValue();
            MediaCodec codec = getCodec();
            if (codec != null) {
                codec.setVideoScalingMode(this.scalingMode);
            }
        } else {
            super.handleMessage(messageType, message);
        }
    }

    private void setSurface(Surface surface) throws ExoPlaybackException {
        if (surface == null) {
            if (this.dummySurface != null) {
                surface = this.dummySurface;
            } else {
                MediaCodecInfo codecInfo = getCodecInfo();
                if (codecInfo != null && shouldUseDummySurface(codecInfo)) {
                    this.dummySurface = DummySurface.newInstanceV17(this.context, codecInfo.secure);
                    surface = this.dummySurface;
                }
            }
        }
        if (this.surface != surface) {
            this.surface = surface;
            int state = getState();
            if (state == 1 || state == 2) {
                MediaCodec codec = getCodec();
                if (Util.SDK_INT < 23 || codec == null || surface == null || this.codecNeedsSetOutputSurfaceWorkaround) {
                    releaseCodec();
                    maybeInitCodec();
                } else {
                    setOutputSurfaceV23(codec, surface);
                }
            }
            if (surface == null || surface == this.dummySurface) {
                clearReportedVideoSize();
                clearRenderedFirstFrame();
                return;
            }
            maybeRenotifyVideoSizeChanged();
            clearRenderedFirstFrame();
            if (state == 2) {
                setJoiningDeadlineMs();
            }
        } else if (surface != null && surface != this.dummySurface) {
            maybeRenotifyVideoSizeChanged();
            maybeRenotifyRenderedFirstFrame();
        }
    }

    protected boolean shouldInitCodec(MediaCodecInfo codecInfo) {
        return this.surface != null || shouldUseDummySurface(codecInfo);
    }

    protected void configureCodec(MediaCodecInfo codecInfo, MediaCodec codec, Format format, MediaCrypto crypto, float codecOperatingRate) throws DecoderQueryException {
        this.codecMaxValues = getCodecMaxValues(codecInfo, format, getStreamFormats());
        MediaFormat mediaFormat = getMediaFormat(format, this.codecMaxValues, codecOperatingRate, this.deviceNeedsAutoFrcWorkaround, this.tunnelingAudioSessionId);
        if (this.surface == null) {
            Assertions.checkState(shouldUseDummySurface(codecInfo));
            if (this.dummySurface == null) {
                this.dummySurface = DummySurface.newInstanceV17(this.context, codecInfo.secure);
            }
            this.surface = this.dummySurface;
        }
        codec.configure(mediaFormat, this.surface, crypto, 0);
        if (Util.SDK_INT >= 23 && this.tunneling) {
            this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(codec);
        }
    }

    protected int canKeepCodec(MediaCodec codec, MediaCodecInfo codecInfo, Format oldFormat, Format newFormat) {
        if (!areAdaptationCompatible(codecInfo.adaptive, oldFormat, newFormat) || newFormat.width > this.codecMaxValues.width || newFormat.height > this.codecMaxValues.height || getMaxInputSize(codecInfo, newFormat) > this.codecMaxValues.inputSize) {
            return 0;
        }
        return oldFormat.initializationDataEquals(newFormat) ? 1 : 3;
    }

    protected void releaseCodec() {
        try {
            super.releaseCodec();
        } finally {
            int i = 0;
            this.buffersInCodecCount = 0;
            if (this.dummySurface != null) {
                if (this.surface == this.dummySurface) {
                    this.surface = null;
                }
                this.dummySurface.release();
                this.dummySurface = null;
            }
        }
    }

    protected void flushCodec() throws ExoPlaybackException {
        super.flushCodec();
        this.buffersInCodecCount = 0;
    }

    protected float getCodecOperatingRate(float operatingRate, Format format, Format[] streamFormats) {
        float maxFrameRate = -1.0f;
        for (Format streamFormat : streamFormats) {
            float streamFrameRate = streamFormat.frameRate;
            if (streamFrameRate != -1.0f) {
                maxFrameRate = Math.max(maxFrameRate, streamFrameRate);
            }
        }
        if (maxFrameRate == -1.0f) {
            return -1.0f;
        }
        return maxFrameRate * operatingRate;
    }

    protected void onCodecInitialized(String name, long initializedTimestampMs, long initializationDurationMs) {
        this.eventDispatcher.decoderInitialized(name, initializedTimestampMs, initializationDurationMs);
        this.codecNeedsSetOutputSurfaceWorkaround = codecNeedsSetOutputSurfaceWorkaround(name);
    }

    protected void onInputFormatChanged(Format newFormat) throws ExoPlaybackException {
        super.onInputFormatChanged(newFormat);
        this.eventDispatcher.inputFormatChanged(newFormat);
        this.pendingPixelWidthHeightRatio = newFormat.pixelWidthHeightRatio;
        this.pendingRotationDegrees = newFormat.rotationDegrees;
    }

    protected void onQueueInputBuffer(DecoderInputBuffer buffer) {
        this.buffersInCodecCount++;
        this.lastInputTimeUs = Math.max(buffer.timeUs, this.lastInputTimeUs);
        if (Util.SDK_INT < 23 && this.tunneling) {
            maybeNotifyRenderedFirstFrame();
        }
    }

    protected void onOutputFormatChanged(MediaCodec codec, MediaFormat outputFormat) {
        int integer;
        boolean hasCrop = outputFormat.containsKey(KEY_CROP_RIGHT) && outputFormat.containsKey(KEY_CROP_LEFT) && outputFormat.containsKey(KEY_CROP_BOTTOM) && outputFormat.containsKey(KEY_CROP_TOP);
        if (hasCrop) {
            integer = (outputFormat.getInteger(KEY_CROP_RIGHT) - outputFormat.getInteger(KEY_CROP_LEFT)) + 1;
        } else {
            integer = outputFormat.getInteger("width");
        }
        this.currentWidth = integer;
        if (hasCrop) {
            integer = (outputFormat.getInteger(KEY_CROP_BOTTOM) - outputFormat.getInteger(KEY_CROP_TOP)) + 1;
        } else {
            integer = outputFormat.getInteger("height");
        }
        this.currentHeight = integer;
        this.currentPixelWidthHeightRatio = this.pendingPixelWidthHeightRatio;
        if (Util.SDK_INT < 21) {
            this.currentUnappliedRotationDegrees = this.pendingRotationDegrees;
        } else if (this.pendingRotationDegrees == 90 || this.pendingRotationDegrees == 270) {
            int rotatedHeight = this.currentWidth;
            this.currentWidth = this.currentHeight;
            this.currentHeight = rotatedHeight;
            this.currentPixelWidthHeightRatio = 1.0f / this.currentPixelWidthHeightRatio;
        }
        codec.setVideoScalingMode(this.scalingMode);
    }

    protected boolean processOutputBuffer(long positionUs, long elapsedRealtimeUs, MediaCodec codec, ByteBuffer buffer, int bufferIndex, int bufferFlags, long bufferPresentationTimeUs, boolean shouldSkip) throws ExoPlaybackException {
        if (this.initialPositionUs == C.TIME_UNSET) {
            this.initialPositionUs = positionUs;
        }
        long presentationTimeUs = bufferPresentationTimeUs - this.outputStreamOffsetUs;
        if (shouldSkip) {
            skipOutputBuffer(codec, bufferIndex, presentationTimeUs);
            return true;
        }
        long earlyUs = bufferPresentationTimeUs - positionUs;
        if (this.surface != this.dummySurface) {
            long elapsedRealtimeNowUs = SystemClock.elapsedRealtime() * 1000;
            boolean isStarted = getState() == 2;
            if (!this.renderedFirstFrame || (isStarted && shouldForceRenderOutputBuffer(earlyUs, elapsedRealtimeNowUs - this.lastRenderTimeUs))) {
                if (Util.SDK_INT >= 21) {
                    renderOutputBufferV21(codec, bufferIndex, presentationTimeUs, System.nanoTime());
                } else {
                    renderOutputBuffer(codec, bufferIndex, presentationTimeUs);
                }
                return true;
            } else if (!isStarted || positionUs == this.initialPositionUs) {
                return false;
            } else {
                earlyUs -= elapsedRealtimeNowUs - elapsedRealtimeUs;
                long systemTimeNs = System.nanoTime();
                long adjustedReleaseTimeNs = this.frameReleaseTimeHelper.adjustReleaseTime(bufferPresentationTimeUs, systemTimeNs + (1000 * earlyUs));
                earlyUs = (adjustedReleaseTimeNs - systemTimeNs) / 1000;
                if (shouldDropBuffersToKeyframe(earlyUs, elapsedRealtimeUs) && maybeDropBuffersToKeyframe(codec, bufferIndex, presentationTimeUs, positionUs)) {
                    return false;
                }
                if (shouldDropOutputBuffer(earlyUs, elapsedRealtimeUs)) {
                    dropOutputBuffer(codec, bufferIndex, presentationTimeUs);
                    return true;
                }
                if (Util.SDK_INT >= 21) {
                    if (earlyUs < 50000) {
                        renderOutputBufferV21(codec, bufferIndex, presentationTimeUs, adjustedReleaseTimeNs);
                        return true;
                    }
                } else if (earlyUs < 30000) {
                    if (earlyUs > 11000) {
                        try {
                            Thread.sleep((earlyUs - 10000) / 1000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return false;
                        }
                    }
                    renderOutputBuffer(codec, bufferIndex, presentationTimeUs);
                    return true;
                }
                return false;
            }
        } else if (!isBufferLate(earlyUs)) {
            return false;
        } else {
            skipOutputBuffer(codec, bufferIndex, presentationTimeUs);
            return true;
        }
    }

    protected void onProcessedOutputBuffer(long presentationTimeUs) {
        this.buffersInCodecCount--;
        while (this.pendingOutputStreamOffsetCount != 0 && presentationTimeUs >= this.pendingOutputStreamSwitchTimesUs[0]) {
            this.outputStreamOffsetUs = this.pendingOutputStreamOffsetsUs[0];
            this.pendingOutputStreamOffsetCount--;
            System.arraycopy(this.pendingOutputStreamOffsetsUs, 1, this.pendingOutputStreamOffsetsUs, 0, this.pendingOutputStreamOffsetCount);
            System.arraycopy(this.pendingOutputStreamSwitchTimesUs, 1, this.pendingOutputStreamSwitchTimesUs, 0, this.pendingOutputStreamOffsetCount);
        }
    }

    protected boolean shouldDropOutputBuffer(long earlyUs, long elapsedRealtimeUs) {
        return isBufferLate(earlyUs);
    }

    protected boolean shouldDropBuffersToKeyframe(long earlyUs, long elapsedRealtimeUs) {
        return isBufferVeryLate(earlyUs);
    }

    protected boolean shouldForceRenderOutputBuffer(long earlyUs, long elapsedSinceLastRenderUs) {
        return isBufferLate(earlyUs) && elapsedSinceLastRenderUs > 100000;
    }

    protected void skipOutputBuffer(MediaCodec codec, int index, long presentationTimeUs) {
        TraceUtil.beginSection("skipVideoBuffer");
        codec.releaseOutputBuffer(index, false);
        TraceUtil.endSection();
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.skippedOutputBufferCount++;
    }

    protected void dropOutputBuffer(MediaCodec codec, int index, long presentationTimeUs) {
        TraceUtil.beginSection("dropVideoBuffer");
        codec.releaseOutputBuffer(index, false);
        TraceUtil.endSection();
        updateDroppedBufferCounters(1);
    }

    protected boolean maybeDropBuffersToKeyframe(MediaCodec codec, int index, long presentationTimeUs, long positionUs) throws ExoPlaybackException {
        int droppedSourceBufferCount = skipSource(positionUs);
        if (droppedSourceBufferCount == 0) {
            return false;
        }
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.droppedToKeyframeCount++;
        updateDroppedBufferCounters(this.buffersInCodecCount + droppedSourceBufferCount);
        flushCodec();
        return true;
    }

    protected void updateDroppedBufferCounters(int droppedBufferCount) {
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.droppedBufferCount += droppedBufferCount;
        this.droppedFrames += droppedBufferCount;
        this.consecutiveDroppedFrameCount += droppedBufferCount;
        this.decoderCounters.maxConsecutiveDroppedBufferCount = Math.max(this.consecutiveDroppedFrameCount, this.decoderCounters.maxConsecutiveDroppedBufferCount);
        if (this.droppedFrames >= this.maxDroppedFramesToNotify) {
            maybeNotifyDroppedFrames();
        }
    }

    protected void renderOutputBuffer(MediaCodec codec, int index, long presentationTimeUs) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        codec.releaseOutputBuffer(index, true);
        TraceUtil.endSection();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000;
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        maybeNotifyRenderedFirstFrame();
    }

    @TargetApi(21)
    protected void renderOutputBufferV21(MediaCodec codec, int index, long presentationTimeUs, long releaseTimeNs) {
        maybeNotifyVideoSizeChanged();
        TraceUtil.beginSection("releaseOutputBuffer");
        codec.releaseOutputBuffer(index, releaseTimeNs);
        TraceUtil.endSection();
        this.lastRenderTimeUs = SystemClock.elapsedRealtime() * 1000;
        DecoderCounters decoderCounters = this.decoderCounters;
        decoderCounters.renderedOutputBufferCount++;
        this.consecutiveDroppedFrameCount = 0;
        maybeNotifyRenderedFirstFrame();
    }

    private boolean shouldUseDummySurface(MediaCodecInfo codecInfo) {
        return Util.SDK_INT >= 23 && !this.tunneling && !codecNeedsSetOutputSurfaceWorkaround(codecInfo.name) && (!codecInfo.secure || DummySurface.isSecureSupported(this.context));
    }

    private void setJoiningDeadlineMs() {
        this.joiningDeadlineMs = this.allowedJoiningTimeMs > 0 ? SystemClock.elapsedRealtime() + this.allowedJoiningTimeMs : C.TIME_UNSET;
    }

    private void clearRenderedFirstFrame() {
        this.renderedFirstFrame = false;
        if (Util.SDK_INT >= 23 && this.tunneling) {
            MediaCodec codec = getCodec();
            if (codec != null) {
                this.tunnelingOnFrameRenderedListener = new OnFrameRenderedListenerV23(codec);
            }
        }
    }

    void maybeNotifyRenderedFirstFrame() {
        if (!this.renderedFirstFrame) {
            this.renderedFirstFrame = true;
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }

    private void maybeRenotifyRenderedFirstFrame() {
        if (this.renderedFirstFrame) {
            this.eventDispatcher.renderedFirstFrame(this.surface);
        }
    }

    private void clearReportedVideoSize() {
        this.reportedWidth = -1;
        this.reportedHeight = -1;
        this.reportedPixelWidthHeightRatio = -1.0f;
        this.reportedUnappliedRotationDegrees = -1;
    }

    private void maybeNotifyVideoSizeChanged() {
        if (this.currentWidth != -1 || this.currentHeight != -1) {
            if (this.reportedWidth != this.currentWidth || this.reportedHeight != this.currentHeight || this.reportedUnappliedRotationDegrees != this.currentUnappliedRotationDegrees || this.reportedPixelWidthHeightRatio != this.currentPixelWidthHeightRatio) {
                this.eventDispatcher.videoSizeChanged(this.currentWidth, this.currentHeight, this.currentUnappliedRotationDegrees, this.currentPixelWidthHeightRatio);
                this.reportedWidth = this.currentWidth;
                this.reportedHeight = this.currentHeight;
                this.reportedUnappliedRotationDegrees = this.currentUnappliedRotationDegrees;
                this.reportedPixelWidthHeightRatio = this.currentPixelWidthHeightRatio;
            }
        }
    }

    private void maybeRenotifyVideoSizeChanged() {
        if (this.reportedWidth != -1 || this.reportedHeight != -1) {
            this.eventDispatcher.videoSizeChanged(this.reportedWidth, this.reportedHeight, this.reportedUnappliedRotationDegrees, this.reportedPixelWidthHeightRatio);
        }
    }

    private void maybeNotifyDroppedFrames() {
        if (this.droppedFrames > 0) {
            long now = SystemClock.elapsedRealtime();
            this.eventDispatcher.droppedFrames(this.droppedFrames, now - this.droppedFrameAccumulationStartTimeMs);
            this.droppedFrames = 0;
            this.droppedFrameAccumulationStartTimeMs = now;
        }
    }

    private static boolean isBufferLate(long earlyUs) {
        return earlyUs < -30000;
    }

    private static boolean isBufferVeryLate(long earlyUs) {
        return earlyUs < -500000;
    }

    @TargetApi(23)
    private static void setOutputSurfaceV23(MediaCodec codec, Surface surface) {
        codec.setOutputSurface(surface);
    }

    @TargetApi(21)
    private static void configureTunnelingV21(MediaFormat mediaFormat, int tunnelingAudioSessionId) {
        mediaFormat.setFeatureEnabled("tunneled-playback", true);
        mediaFormat.setInteger("audio-session-id", tunnelingAudioSessionId);
    }

    @SuppressLint({"InlinedApi"})
    protected MediaFormat getMediaFormat(Format format, CodecMaxValues codecMaxValues, float codecOperatingRate, boolean deviceNeedsAutoFrcWorkaround, int tunnelingAudioSessionId) {
        MediaFormat mediaFormat = new MediaFormat();
        mediaFormat.setString("mime", format.sampleMimeType);
        mediaFormat.setInteger("width", format.width);
        mediaFormat.setInteger("height", format.height);
        MediaFormatUtil.setCsdBuffers(mediaFormat, format.initializationData);
        MediaFormatUtil.maybeSetFloat(mediaFormat, "frame-rate", format.frameRate);
        MediaFormatUtil.maybeSetInteger(mediaFormat, "rotation-degrees", format.rotationDegrees);
        MediaFormatUtil.maybeSetColorInfo(mediaFormat, format.colorInfo);
        mediaFormat.setInteger("max-width", codecMaxValues.width);
        mediaFormat.setInteger("max-height", codecMaxValues.height);
        MediaFormatUtil.maybeSetInteger(mediaFormat, "max-input-size", codecMaxValues.inputSize);
        if (Util.SDK_INT >= 23) {
            mediaFormat.setInteger("priority", 0);
            if (codecOperatingRate != -1.0f) {
                mediaFormat.setFloat("operating-rate", codecOperatingRate);
            }
        }
        if (deviceNeedsAutoFrcWorkaround) {
            mediaFormat.setInteger("auto-frc", 0);
        }
        if (tunnelingAudioSessionId != 0) {
            configureTunnelingV21(mediaFormat, tunnelingAudioSessionId);
        }
        return mediaFormat;
    }

    protected CodecMaxValues getCodecMaxValues(MediaCodecInfo codecInfo, Format format, Format[] streamFormats) throws DecoderQueryException {
        int maxWidth = format.width;
        int maxHeight = format.height;
        int maxInputSize = getMaxInputSize(codecInfo, format);
        if (streamFormats.length == 1) {
            return new CodecMaxValues(maxWidth, maxHeight, maxInputSize);
        }
        boolean haveUnknownDimensions = false;
        for (Format streamFormat : streamFormats) {
            if (areAdaptationCompatible(codecInfo.adaptive, format, streamFormat)) {
                int i;
                if (streamFormat.width == -1 || streamFormat.height == -1) {
                    i = 1;
                } else {
                    i = 0;
                }
                haveUnknownDimensions |= i;
                maxWidth = Math.max(maxWidth, streamFormat.width);
                maxHeight = Math.max(maxHeight, streamFormat.height);
                maxInputSize = Math.max(maxInputSize, getMaxInputSize(codecInfo, streamFormat));
            }
        }
        if (haveUnknownDimensions) {
            Log.w(TAG, "Resolutions unknown. Codec max resolution: " + maxWidth + "x" + maxHeight);
            Point codecMaxSize = getCodecMaxSize(codecInfo, format);
            if (codecMaxSize != null) {
                maxWidth = Math.max(maxWidth, codecMaxSize.x);
                maxHeight = Math.max(maxHeight, codecMaxSize.y);
                maxInputSize = Math.max(maxInputSize, getMaxInputSize(codecInfo, format.sampleMimeType, maxWidth, maxHeight));
                Log.w(TAG, "Codec max resolution adjusted to: " + maxWidth + "x" + maxHeight);
            }
        }
        return new CodecMaxValues(maxWidth, maxHeight, maxInputSize);
    }

    private static Point getCodecMaxSize(MediaCodecInfo codecInfo, Format format) throws DecoderQueryException {
        boolean isVerticalVideo = format.height > format.width;
        int formatLongEdgePx = isVerticalVideo ? format.height : format.width;
        int formatShortEdgePx = isVerticalVideo ? format.width : format.height;
        float aspectRatio = ((float) formatShortEdgePx) / ((float) formatLongEdgePx);
        for (int longEdgePx : STANDARD_LONG_EDGE_VIDEO_PX) {
            int longEdgePx2;
            int shortEdgePx = (int) (((float) longEdgePx2) * aspectRatio);
            if (longEdgePx2 <= formatLongEdgePx || shortEdgePx <= formatShortEdgePx) {
                return null;
            }
            if (Util.SDK_INT >= 21) {
                int i;
                if (isVerticalVideo) {
                    i = shortEdgePx;
                } else {
                    i = longEdgePx2;
                }
                Point alignedSize = codecInfo.alignVideoSizeV21(i, isVerticalVideo ? longEdgePx2 : shortEdgePx);
                if (codecInfo.isVideoSizeAndRateSupportedV21(alignedSize.x, alignedSize.y, (double) format.frameRate)) {
                    return alignedSize;
                }
            } else {
                longEdgePx2 = Util.ceilDivide(longEdgePx2, 16) * 16;
                shortEdgePx = Util.ceilDivide(shortEdgePx, 16) * 16;
                if (longEdgePx2 * shortEdgePx <= MediaCodecUtil.maxH264DecodableFrameSize()) {
                    int i2 = isVerticalVideo ? shortEdgePx : longEdgePx2;
                    if (!isVerticalVideo) {
                        longEdgePx2 = shortEdgePx;
                    }
                    return new Point(i2, longEdgePx2);
                }
            }
        }
        return null;
    }

    private static int getMaxInputSize(MediaCodecInfo codecInfo, Format format) {
        if (format.maxInputSize == -1) {
            return getMaxInputSize(codecInfo, format.sampleMimeType, format.width, format.height);
        }
        int totalInitializationDataSize = 0;
        for (int i = 0; i < format.initializationData.size(); i++) {
            totalInitializationDataSize += ((byte[]) format.initializationData.get(i)).length;
        }
        return format.maxInputSize + totalInitializationDataSize;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static int getMaxInputSize(com.google.android.exoplayer2.mediacodec.MediaCodecInfo r6, java.lang.String r7, int r8, int r9) {
        /*
        r5 = 16;
        r2 = -1;
        if (r8 == r2) goto L_0x0007;
    L_0x0005:
        if (r9 != r2) goto L_0x0008;
    L_0x0007:
        return r2;
    L_0x0008:
        r3 = r7.hashCode();
        switch(r3) {
            case -1664118616: goto L_0x001d;
            case -1662541442: goto L_0x0049;
            case 1187890754: goto L_0x0028;
            case 1331836730: goto L_0x0033;
            case 1599127256: goto L_0x003e;
            case 1599127257: goto L_0x0054;
            default: goto L_0x000f;
        };
    L_0x000f:
        r3 = r2;
    L_0x0010:
        switch(r3) {
            case 0: goto L_0x0014;
            case 1: goto L_0x0014;
            case 2: goto L_0x005f;
            case 3: goto L_0x009f;
            case 4: goto L_0x00a4;
            case 5: goto L_0x00a4;
            default: goto L_0x0013;
        };
    L_0x0013:
        goto L_0x0007;
    L_0x0014:
        r0 = r8 * r9;
        r1 = 2;
    L_0x0017:
        r2 = r0 * 3;
        r3 = r1 * 2;
        r2 = r2 / r3;
        goto L_0x0007;
    L_0x001d:
        r3 = "video/3gpp";
        r3 = r7.equals(r3);
        if (r3 == 0) goto L_0x000f;
    L_0x0026:
        r3 = 0;
        goto L_0x0010;
    L_0x0028:
        r3 = "video/mp4v-es";
        r3 = r7.equals(r3);
        if (r3 == 0) goto L_0x000f;
    L_0x0031:
        r3 = 1;
        goto L_0x0010;
    L_0x0033:
        r3 = "video/avc";
        r3 = r7.equals(r3);
        if (r3 == 0) goto L_0x000f;
    L_0x003c:
        r3 = 2;
        goto L_0x0010;
    L_0x003e:
        r3 = "video/x-vnd.on2.vp8";
        r3 = r7.equals(r3);
        if (r3 == 0) goto L_0x000f;
    L_0x0047:
        r3 = 3;
        goto L_0x0010;
    L_0x0049:
        r3 = "video/hevc";
        r3 = r7.equals(r3);
        if (r3 == 0) goto L_0x000f;
    L_0x0052:
        r3 = 4;
        goto L_0x0010;
    L_0x0054:
        r3 = "video/x-vnd.on2.vp9";
        r3 = r7.equals(r3);
        if (r3 == 0) goto L_0x000f;
    L_0x005d:
        r3 = 5;
        goto L_0x0010;
    L_0x005f:
        r3 = "BRAVIA 4K 2015";
        r4 = com.google.android.exoplayer2.util.Util.MODEL;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x0007;
    L_0x006a:
        r3 = "Amazon";
        r4 = com.google.android.exoplayer2.util.Util.MANUFACTURER;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x008f;
    L_0x0075:
        r3 = "KFSOWI";
        r4 = com.google.android.exoplayer2.util.Util.MODEL;
        r3 = r3.equals(r4);
        if (r3 != 0) goto L_0x0007;
    L_0x0080:
        r3 = "AFTS";
        r4 = com.google.android.exoplayer2.util.Util.MODEL;
        r3 = r3.equals(r4);
        if (r3 == 0) goto L_0x008f;
    L_0x008b:
        r3 = r6.secure;
        if (r3 != 0) goto L_0x0007;
    L_0x008f:
        r2 = com.google.android.exoplayer2.util.Util.ceilDivide(r8, r5);
        r3 = com.google.android.exoplayer2.util.Util.ceilDivide(r9, r5);
        r2 = r2 * r3;
        r2 = r2 * 16;
        r0 = r2 * 16;
        r1 = 2;
        goto L_0x0017;
    L_0x009f:
        r0 = r8 * r9;
        r1 = 2;
        goto L_0x0017;
    L_0x00a4:
        r0 = r8 * r9;
        r1 = 4;
        goto L_0x0017;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.MediaCodecVideoRenderer.getMaxInputSize(com.google.android.exoplayer2.mediacodec.MediaCodecInfo, java.lang.String, int, int):int");
    }

    private static boolean areAdaptationCompatible(boolean codecIsAdaptive, Format first, Format second) {
        return first.sampleMimeType.equals(second.sampleMimeType) && first.rotationDegrees == second.rotationDegrees && ((codecIsAdaptive || (first.width == second.width && first.height == second.height)) && Util.areEqual(first.colorInfo, second.colorInfo));
    }

    private static boolean deviceNeedsAutoFrcWorkaround() {
        return Util.SDK_INT <= 22 && "foster".equals(Util.DEVICE) && "NVIDIA".equals(Util.MANUFACTURER);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected boolean codecNeedsSetOutputSurfaceWorkaround(java.lang.String r8) {
        /*
        r7 = this;
        r3 = 27;
        r0 = 0;
        r2 = 1;
        r1 = com.google.android.exoplayer2.util.Util.SDK_INT;
        if (r1 >= r3) goto L_0x0011;
    L_0x0008:
        r1 = "OMX.google";
        r1 = r8.startsWith(r1);
        if (r1 == 0) goto L_0x0012;
    L_0x0011:
        return r0;
    L_0x0012:
        r4 = com.google.android.exoplayer2.video.MediaCodecVideoRenderer.class;
        monitor-enter(r4);
        r1 = evaluatedDeviceNeedsSetOutputSurfaceWorkaround;	 Catch:{ all -> 0x061d }
        if (r1 != 0) goto L_0x002a;
    L_0x0019:
        r5 = com.google.android.exoplayer2.util.Util.DEVICE;	 Catch:{ all -> 0x061d }
        r1 = -1;
        r6 = r5.hashCode();	 Catch:{ all -> 0x061d }
        switch(r6) {
            case -2144781245: goto L_0x0215;
            case -2144781185: goto L_0x0222;
            case -2144781160: goto L_0x022f;
            case -2097309513: goto L_0x02ff;
            case -2022874474: goto L_0x00eb;
            case -1978993182: goto L_0x039b;
            case -1978990237: goto L_0x03a8;
            case -1936688988: goto L_0x042a;
            case -1936688066: goto L_0x0437;
            case -1936688065: goto L_0x0444;
            case -1931988508: goto L_0x009d;
            case -1696512866: goto L_0x05f1;
            case -1680025915: goto L_0x00de;
            case -1615810839: goto L_0x0451;
            case -1554255044: goto L_0x0596;
            case -1481772737: goto L_0x03f6;
            case -1481772730: goto L_0x0403;
            case -1481772729: goto L_0x0410;
            case -1320080169: goto L_0x01fb;
            case -1217592143: goto L_0x00c4;
            case -1180384755: goto L_0x02cb;
            case -1139198265: goto L_0x04fa;
            case -1052835013: goto L_0x0381;
            case -993250464: goto L_0x004e;
            case -965403638: goto L_0x0514;
            case -958336948: goto L_0x016d;
            case -879245230: goto L_0x0562;
            case -842500323: goto L_0x038e;
            case -821392978: goto L_0x006f;
            case -797483286: goto L_0x0507;
            case -794946968: goto L_0x05a3;
            case -788334647: goto L_0x05b0;
            case -782144577: goto L_0x03b5;
            case -575125681: goto L_0x01ee;
            case -521118391: goto L_0x0208;
            case -430914369: goto L_0x045e;
            case -290434366: goto L_0x0521;
            case -282781963: goto L_0x00b7;
            case -277133239: goto L_0x05fe;
            case -173639913: goto L_0x0146;
            case -56598463: goto L_0x05ca;
            case 2126: goto L_0x00d1;
            case 2564: goto L_0x04c6;
            case 2715: goto L_0x056f;
            case 2719: goto L_0x0589;
            case 3483: goto L_0x035a;
            case 73405: goto L_0x02f2;
            case 75739: goto L_0x0326;
            case 76779: goto L_0x0374;
            case 78669: goto L_0x03dc;
            case 79305: goto L_0x0478;
            case 80618: goto L_0x04e0;
            case 88274: goto L_0x060b;
            case 98846: goto L_0x0112;
            case 98848: goto L_0x011f;
            case 99329: goto L_0x012c;
            case 101481: goto L_0x01e1;
            case 1513190: goto L_0x002e;
            case 1514184: goto L_0x0038;
            case 1514185: goto L_0x0043;
            case 2436959: goto L_0x03cf;
            case 2463773: goto L_0x0492;
            case 2464648: goto L_0x04ac;
            case 2689555: goto L_0x05e4;
            case 3351335: goto L_0x0367;
            case 3386211: goto L_0x03c2;
            case 41325051: goto L_0x034d;
            case 55178625: goto L_0x00aa;
            case 61542055: goto L_0x0059;
            case 65355429: goto L_0x0139;
            case 66214468: goto L_0x0186;
            case 66214470: goto L_0x0193;
            case 66214473: goto L_0x01a0;
            case 66215429: goto L_0x01ad;
            case 66215431: goto L_0x01ba;
            case 66215433: goto L_0x01c7;
            case 66216390: goto L_0x01d4;
            case 76402249: goto L_0x0485;
            case 76404105: goto L_0x049f;
            case 76404911: goto L_0x04b9;
            case 80963634: goto L_0x057c;
            case 82882791: goto L_0x05d7;
            case 102844228: goto L_0x030c;
            case 165221241: goto L_0x0064;
            case 182191441: goto L_0x0105;
            case 245388979: goto L_0x0340;
            case 287431619: goto L_0x0263;
            case 307593612: goto L_0x0085;
            case 308517133: goto L_0x0091;
            case 316215098: goto L_0x052e;
            case 316215116: goto L_0x053b;
            case 316246811: goto L_0x0548;
            case 316246818: goto L_0x0555;
            case 407160593: goto L_0x046b;
            case 507412548: goto L_0x04d3;
            case 793982701: goto L_0x023c;
            case 794038622: goto L_0x0249;
            case 794040393: goto L_0x0256;
            case 835649806: goto L_0x0333;
            case 917340916: goto L_0x007a;
            case 958008161: goto L_0x02e5;
            case 1060579533: goto L_0x03e9;
            case 1150207623: goto L_0x0319;
            case 1176899427: goto L_0x02d8;
            case 1280332038: goto L_0x027d;
            case 1306947716: goto L_0x017a;
            case 1349174697: goto L_0x0270;
            case 1522194893: goto L_0x05bd;
            case 1691543273: goto L_0x00f8;
            case 1709443163: goto L_0x02b1;
            case 1865889110: goto L_0x04ed;
            case 1906253259: goto L_0x041d;
            case 1977196784: goto L_0x02be;
            case 2029784656: goto L_0x028a;
            case 2030379515: goto L_0x0297;
            case 2047190025: goto L_0x0153;
            case 2047252157: goto L_0x0160;
            case 2048319463: goto L_0x02a4;
            default: goto L_0x0023;
        };	 Catch:{ all -> 0x061d }
    L_0x0023:
        r0 = r1;
    L_0x0024:
        switch(r0) {
            case 0: goto L_0x0618;
            case 1: goto L_0x0618;
            case 2: goto L_0x0618;
            case 3: goto L_0x0618;
            case 4: goto L_0x0618;
            case 5: goto L_0x0618;
            case 6: goto L_0x0618;
            case 7: goto L_0x0618;
            case 8: goto L_0x0618;
            case 9: goto L_0x0618;
            case 10: goto L_0x0618;
            case 11: goto L_0x0618;
            case 12: goto L_0x0618;
            case 13: goto L_0x0618;
            case 14: goto L_0x0618;
            case 15: goto L_0x0618;
            case 16: goto L_0x0618;
            case 17: goto L_0x0618;
            case 18: goto L_0x0618;
            case 19: goto L_0x0618;
            case 20: goto L_0x0618;
            case 21: goto L_0x0618;
            case 22: goto L_0x0618;
            case 23: goto L_0x0618;
            case 24: goto L_0x0618;
            case 25: goto L_0x0618;
            case 26: goto L_0x0618;
            case 27: goto L_0x0618;
            case 28: goto L_0x0618;
            case 29: goto L_0x0618;
            case 30: goto L_0x0618;
            case 31: goto L_0x0618;
            case 32: goto L_0x0618;
            case 33: goto L_0x0618;
            case 34: goto L_0x0618;
            case 35: goto L_0x0618;
            case 36: goto L_0x0618;
            case 37: goto L_0x0618;
            case 38: goto L_0x0618;
            case 39: goto L_0x0618;
            case 40: goto L_0x0618;
            case 41: goto L_0x0618;
            case 42: goto L_0x0618;
            case 43: goto L_0x0618;
            case 44: goto L_0x0618;
            case 45: goto L_0x0618;
            case 46: goto L_0x0618;
            case 47: goto L_0x0618;
            case 48: goto L_0x0618;
            case 49: goto L_0x0618;
            case 50: goto L_0x0618;
            case 51: goto L_0x0618;
            case 52: goto L_0x0618;
            case 53: goto L_0x0618;
            case 54: goto L_0x0618;
            case 55: goto L_0x0618;
            case 56: goto L_0x0618;
            case 57: goto L_0x0618;
            case 58: goto L_0x0618;
            case 59: goto L_0x0618;
            case 60: goto L_0x0618;
            case 61: goto L_0x0618;
            case 62: goto L_0x0618;
            case 63: goto L_0x0618;
            case 64: goto L_0x0618;
            case 65: goto L_0x0618;
            case 66: goto L_0x0618;
            case 67: goto L_0x0618;
            case 68: goto L_0x0618;
            case 69: goto L_0x0618;
            case 70: goto L_0x0618;
            case 71: goto L_0x0618;
            case 72: goto L_0x0618;
            case 73: goto L_0x0618;
            case 74: goto L_0x0618;
            case 75: goto L_0x0618;
            case 76: goto L_0x0618;
            case 77: goto L_0x0618;
            case 78: goto L_0x0618;
            case 79: goto L_0x0618;
            case 80: goto L_0x0618;
            case 81: goto L_0x0618;
            case 82: goto L_0x0618;
            case 83: goto L_0x0618;
            case 84: goto L_0x0618;
            case 85: goto L_0x0618;
            case 86: goto L_0x0618;
            case 87: goto L_0x0618;
            case 88: goto L_0x0618;
            case 89: goto L_0x0618;
            case 90: goto L_0x0618;
            case 91: goto L_0x0618;
            case 92: goto L_0x0618;
            case 93: goto L_0x0618;
            case 94: goto L_0x0618;
            case 95: goto L_0x0618;
            case 96: goto L_0x0618;
            case 97: goto L_0x0618;
            case 98: goto L_0x0618;
            case 99: goto L_0x0618;
            case 100: goto L_0x0618;
            case 101: goto L_0x0618;
            case 102: goto L_0x0618;
            case 103: goto L_0x0618;
            case 104: goto L_0x0618;
            case 105: goto L_0x0618;
            case 106: goto L_0x0618;
            case 107: goto L_0x0618;
            case 108: goto L_0x0618;
            case 109: goto L_0x0618;
            case 110: goto L_0x0618;
            case 111: goto L_0x0618;
            case 112: goto L_0x0618;
            case 113: goto L_0x0618;
            case 114: goto L_0x0618;
            case 115: goto L_0x0618;
            case 116: goto L_0x0618;
            case 117: goto L_0x0618;
            default: goto L_0x0027;
        };	 Catch:{ all -> 0x061d }
    L_0x0027:
        r0 = 1;
        evaluatedDeviceNeedsSetOutputSurfaceWorkaround = r0;	 Catch:{ all -> 0x061d }
    L_0x002a:
        monitor-exit(r4);	 Catch:{ all -> 0x061d }
        r0 = deviceNeedsSetOutputSurfaceWorkaround;
        goto L_0x0011;
    L_0x002e:
        r2 = "1601";
        r2 = r5.equals(r2);	 Catch:{ all -> 0x061d }
        if (r2 == 0) goto L_0x0023;
    L_0x0037:
        goto L_0x0024;
    L_0x0038:
        r0 = "1713";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0041:
        r0 = r2;
        goto L_0x0024;
    L_0x0043:
        r0 = "1714";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x004c:
        r0 = 2;
        goto L_0x0024;
    L_0x004e:
        r0 = "A10-70F";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0057:
        r0 = 3;
        goto L_0x0024;
    L_0x0059:
        r0 = "A1601";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0062:
        r0 = 4;
        goto L_0x0024;
    L_0x0064:
        r0 = "A2016a40";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x006d:
        r0 = 5;
        goto L_0x0024;
    L_0x006f:
        r0 = "A7000-a";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0078:
        r0 = 6;
        goto L_0x0024;
    L_0x007a:
        r0 = "A7000plus";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0083:
        r0 = 7;
        goto L_0x0024;
    L_0x0085:
        r0 = "A7010a48";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x008e:
        r0 = 8;
        goto L_0x0024;
    L_0x0091:
        r0 = "A7020a48";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x009a:
        r0 = 9;
        goto L_0x0024;
    L_0x009d:
        r0 = "AquaPowerM";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x00a6:
        r0 = 10;
        goto L_0x0024;
    L_0x00aa:
        r0 = "Aura_Note_2";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x00b3:
        r0 = 11;
        goto L_0x0024;
    L_0x00b7:
        r0 = "BLACK-1X";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x00c0:
        r0 = 12;
        goto L_0x0024;
    L_0x00c4:
        r0 = "BRAVIA_ATV2";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x00cd:
        r0 = 13;
        goto L_0x0024;
    L_0x00d1:
        r0 = "C1";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x00da:
        r0 = 14;
        goto L_0x0024;
    L_0x00de:
        r0 = "ComioS1";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x00e7:
        r0 = 15;
        goto L_0x0024;
    L_0x00eb:
        r0 = "CP8676_I02";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x00f4:
        r0 = 16;
        goto L_0x0024;
    L_0x00f8:
        r0 = "CPH1609";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0101:
        r0 = 17;
        goto L_0x0024;
    L_0x0105:
        r0 = "CPY83_I00";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x010e:
        r0 = 18;
        goto L_0x0024;
    L_0x0112:
        r0 = "cv1";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x011b:
        r0 = 19;
        goto L_0x0024;
    L_0x011f:
        r0 = "cv3";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0128:
        r0 = 20;
        goto L_0x0024;
    L_0x012c:
        r0 = "deb";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0135:
        r0 = 21;
        goto L_0x0024;
    L_0x0139:
        r0 = "E5643";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0142:
        r0 = 22;
        goto L_0x0024;
    L_0x0146:
        r0 = "ELUGA_A3_Pro";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x014f:
        r0 = 23;
        goto L_0x0024;
    L_0x0153:
        r0 = "ELUGA_Note";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x015c:
        r0 = 24;
        goto L_0x0024;
    L_0x0160:
        r0 = "ELUGA_Prim";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0169:
        r0 = 25;
        goto L_0x0024;
    L_0x016d:
        r0 = "ELUGA_Ray_X";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0176:
        r0 = 26;
        goto L_0x0024;
    L_0x017a:
        r0 = "EverStar_S";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0183:
        r0 = r3;
        goto L_0x0024;
    L_0x0186:
        r0 = "F3111";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x018f:
        r0 = 28;
        goto L_0x0024;
    L_0x0193:
        r0 = "F3113";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x019c:
        r0 = 29;
        goto L_0x0024;
    L_0x01a0:
        r0 = "F3116";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x01a9:
        r0 = 30;
        goto L_0x0024;
    L_0x01ad:
        r0 = "F3211";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x01b6:
        r0 = 31;
        goto L_0x0024;
    L_0x01ba:
        r0 = "F3213";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x01c3:
        r0 = 32;
        goto L_0x0024;
    L_0x01c7:
        r0 = "F3215";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x01d0:
        r0 = 33;
        goto L_0x0024;
    L_0x01d4:
        r0 = "F3311";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x01dd:
        r0 = 34;
        goto L_0x0024;
    L_0x01e1:
        r0 = "flo";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x01ea:
        r0 = 35;
        goto L_0x0024;
    L_0x01ee:
        r0 = "GiONEE_CBL7513";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x01f7:
        r0 = 36;
        goto L_0x0024;
    L_0x01fb:
        r0 = "GiONEE_GBL7319";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0204:
        r0 = 37;
        goto L_0x0024;
    L_0x0208:
        r0 = "GIONEE_GBL7360";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0211:
        r0 = 38;
        goto L_0x0024;
    L_0x0215:
        r0 = "GIONEE_SWW1609";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x021e:
        r0 = 39;
        goto L_0x0024;
    L_0x0222:
        r0 = "GIONEE_SWW1627";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x022b:
        r0 = 40;
        goto L_0x0024;
    L_0x022f:
        r0 = "GIONEE_SWW1631";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0238:
        r0 = 41;
        goto L_0x0024;
    L_0x023c:
        r0 = "GIONEE_WBL5708";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0245:
        r0 = 42;
        goto L_0x0024;
    L_0x0249:
        r0 = "GIONEE_WBL7365";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0252:
        r0 = 43;
        goto L_0x0024;
    L_0x0256:
        r0 = "GIONEE_WBL7519";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x025f:
        r0 = 44;
        goto L_0x0024;
    L_0x0263:
        r0 = "griffin";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x026c:
        r0 = 45;
        goto L_0x0024;
    L_0x0270:
        r0 = "htc_e56ml_dtul";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0279:
        r0 = 46;
        goto L_0x0024;
    L_0x027d:
        r0 = "hwALE-H";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0286:
        r0 = 47;
        goto L_0x0024;
    L_0x028a:
        r0 = "HWBLN-H";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0293:
        r0 = 48;
        goto L_0x0024;
    L_0x0297:
        r0 = "HWCAM-H";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x02a0:
        r0 = 49;
        goto L_0x0024;
    L_0x02a4:
        r0 = "HWVNS-H";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x02ad:
        r0 = 50;
        goto L_0x0024;
    L_0x02b1:
        r0 = "iball8735_9806";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x02ba:
        r0 = 51;
        goto L_0x0024;
    L_0x02be:
        r0 = "Infinix-X572";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x02c7:
        r0 = 52;
        goto L_0x0024;
    L_0x02cb:
        r0 = "iris60";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x02d4:
        r0 = 53;
        goto L_0x0024;
    L_0x02d8:
        r0 = "itel_S41";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x02e1:
        r0 = 54;
        goto L_0x0024;
    L_0x02e5:
        r0 = "j2xlteins";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x02ee:
        r0 = 55;
        goto L_0x0024;
    L_0x02f2:
        r0 = "JGZ";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x02fb:
        r0 = 56;
        goto L_0x0024;
    L_0x02ff:
        r0 = "K50a40";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0308:
        r0 = 57;
        goto L_0x0024;
    L_0x030c:
        r0 = "le_x6";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0315:
        r0 = 58;
        goto L_0x0024;
    L_0x0319:
        r0 = "LS-5017";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0322:
        r0 = 59;
        goto L_0x0024;
    L_0x0326:
        r0 = "M5c";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x032f:
        r0 = 60;
        goto L_0x0024;
    L_0x0333:
        r0 = "manning";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x033c:
        r0 = 61;
        goto L_0x0024;
    L_0x0340:
        r0 = "marino_f";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0349:
        r0 = 62;
        goto L_0x0024;
    L_0x034d:
        r0 = "MEIZU_M5";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0356:
        r0 = 63;
        goto L_0x0024;
    L_0x035a:
        r0 = "mh";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0363:
        r0 = 64;
        goto L_0x0024;
    L_0x0367:
        r0 = "mido";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0370:
        r0 = 65;
        goto L_0x0024;
    L_0x0374:
        r0 = "MX6";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x037d:
        r0 = 66;
        goto L_0x0024;
    L_0x0381:
        r0 = "namath";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x038a:
        r0 = 67;
        goto L_0x0024;
    L_0x038e:
        r0 = "nicklaus_f";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0397:
        r0 = 68;
        goto L_0x0024;
    L_0x039b:
        r0 = "NX541J";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x03a4:
        r0 = 69;
        goto L_0x0024;
    L_0x03a8:
        r0 = "NX573J";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x03b1:
        r0 = 70;
        goto L_0x0024;
    L_0x03b5:
        r0 = "OnePlus5T";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x03be:
        r0 = 71;
        goto L_0x0024;
    L_0x03c2:
        r0 = "p212";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x03cb:
        r0 = 72;
        goto L_0x0024;
    L_0x03cf:
        r0 = "P681";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x03d8:
        r0 = 73;
        goto L_0x0024;
    L_0x03dc:
        r0 = "P85";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x03e5:
        r0 = 74;
        goto L_0x0024;
    L_0x03e9:
        r0 = "panell_d";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x03f2:
        r0 = 75;
        goto L_0x0024;
    L_0x03f6:
        r0 = "panell_dl";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x03ff:
        r0 = 76;
        goto L_0x0024;
    L_0x0403:
        r0 = "panell_ds";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x040c:
        r0 = 77;
        goto L_0x0024;
    L_0x0410:
        r0 = "panell_dt";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0419:
        r0 = 78;
        goto L_0x0024;
    L_0x041d:
        r0 = "PB2-670M";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0426:
        r0 = 79;
        goto L_0x0024;
    L_0x042a:
        r0 = "PGN528";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0433:
        r0 = 80;
        goto L_0x0024;
    L_0x0437:
        r0 = "PGN610";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0440:
        r0 = 81;
        goto L_0x0024;
    L_0x0444:
        r0 = "PGN611";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x044d:
        r0 = 82;
        goto L_0x0024;
    L_0x0451:
        r0 = "Phantom6";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x045a:
        r0 = 83;
        goto L_0x0024;
    L_0x045e:
        r0 = "Pixi4-7_3G";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0467:
        r0 = 84;
        goto L_0x0024;
    L_0x046b:
        r0 = "Pixi5-10_4G";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0474:
        r0 = 85;
        goto L_0x0024;
    L_0x0478:
        r0 = "PLE";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0481:
        r0 = 86;
        goto L_0x0024;
    L_0x0485:
        r0 = "PRO7S";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x048e:
        r0 = 87;
        goto L_0x0024;
    L_0x0492:
        r0 = "Q350";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x049b:
        r0 = 88;
        goto L_0x0024;
    L_0x049f:
        r0 = "Q4260";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x04a8:
        r0 = 89;
        goto L_0x0024;
    L_0x04ac:
        r0 = "Q427";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x04b5:
        r0 = 90;
        goto L_0x0024;
    L_0x04b9:
        r0 = "Q4310";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x04c2:
        r0 = 91;
        goto L_0x0024;
    L_0x04c6:
        r0 = "Q5";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x04cf:
        r0 = 92;
        goto L_0x0024;
    L_0x04d3:
        r0 = "QM16XE_U";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x04dc:
        r0 = 93;
        goto L_0x0024;
    L_0x04e0:
        r0 = "QX1";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x04e9:
        r0 = 94;
        goto L_0x0024;
    L_0x04ed:
        r0 = "santoni";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x04f6:
        r0 = 95;
        goto L_0x0024;
    L_0x04fa:
        r0 = "Slate_Pro";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0503:
        r0 = 96;
        goto L_0x0024;
    L_0x0507:
        r0 = "SVP-DTV15";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0510:
        r0 = 97;
        goto L_0x0024;
    L_0x0514:
        r0 = "s905x018";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x051d:
        r0 = 98;
        goto L_0x0024;
    L_0x0521:
        r0 = "taido_row";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x052a:
        r0 = 99;
        goto L_0x0024;
    L_0x052e:
        r0 = "TB3-730F";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0537:
        r0 = 100;
        goto L_0x0024;
    L_0x053b:
        r0 = "TB3-730X";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0544:
        r0 = 101; // 0x65 float:1.42E-43 double:5.0E-322;
        goto L_0x0024;
    L_0x0548:
        r0 = "TB3-850F";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0551:
        r0 = 102; // 0x66 float:1.43E-43 double:5.04E-322;
        goto L_0x0024;
    L_0x0555:
        r0 = "TB3-850M";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x055e:
        r0 = 103; // 0x67 float:1.44E-43 double:5.1E-322;
        goto L_0x0024;
    L_0x0562:
        r0 = "tcl_eu";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x056b:
        r0 = 104; // 0x68 float:1.46E-43 double:5.14E-322;
        goto L_0x0024;
    L_0x056f:
        r0 = "V1";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0578:
        r0 = 105; // 0x69 float:1.47E-43 double:5.2E-322;
        goto L_0x0024;
    L_0x057c:
        r0 = "V23GB";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0585:
        r0 = 106; // 0x6a float:1.49E-43 double:5.24E-322;
        goto L_0x0024;
    L_0x0589:
        r0 = "V5";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0592:
        r0 = 107; // 0x6b float:1.5E-43 double:5.3E-322;
        goto L_0x0024;
    L_0x0596:
        r0 = "vernee_M5";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x059f:
        r0 = 108; // 0x6c float:1.51E-43 double:5.34E-322;
        goto L_0x0024;
    L_0x05a3:
        r0 = "watson";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x05ac:
        r0 = 109; // 0x6d float:1.53E-43 double:5.4E-322;
        goto L_0x0024;
    L_0x05b0:
        r0 = "whyred";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x05b9:
        r0 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        goto L_0x0024;
    L_0x05bd:
        r0 = "woods_f";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x05c6:
        r0 = 111; // 0x6f float:1.56E-43 double:5.5E-322;
        goto L_0x0024;
    L_0x05ca:
        r0 = "woods_fn";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x05d3:
        r0 = 112; // 0x70 float:1.57E-43 double:5.53E-322;
        goto L_0x0024;
    L_0x05d7:
        r0 = "X3_HK";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x05e0:
        r0 = 113; // 0x71 float:1.58E-43 double:5.6E-322;
        goto L_0x0024;
    L_0x05e4:
        r0 = "XE2X";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x05ed:
        r0 = 114; // 0x72 float:1.6E-43 double:5.63E-322;
        goto L_0x0024;
    L_0x05f1:
        r0 = "XT1663";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x05fa:
        r0 = 115; // 0x73 float:1.61E-43 double:5.7E-322;
        goto L_0x0024;
    L_0x05fe:
        r0 = "Z12_PRO";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0607:
        r0 = 116; // 0x74 float:1.63E-43 double:5.73E-322;
        goto L_0x0024;
    L_0x060b:
        r0 = "Z80";
        r0 = r5.equals(r0);	 Catch:{ all -> 0x061d }
        if (r0 == 0) goto L_0x0023;
    L_0x0614:
        r0 = 117; // 0x75 float:1.64E-43 double:5.8E-322;
        goto L_0x0024;
    L_0x0618:
        r0 = 1;
        deviceNeedsSetOutputSurfaceWorkaround = r0;	 Catch:{ all -> 0x061d }
        goto L_0x0027;
    L_0x061d:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x061d }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.video.MediaCodecVideoRenderer.codecNeedsSetOutputSurfaceWorkaround(java.lang.String):boolean");
    }
}
