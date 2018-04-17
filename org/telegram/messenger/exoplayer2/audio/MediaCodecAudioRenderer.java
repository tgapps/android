package org.telegram.messenger.exoplayer2.audio;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.os.Handler;
import org.telegram.messenger.exoplayer2.BaseRenderer;
import org.telegram.messenger.exoplayer2.ExoPlaybackException;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.PlaybackParameters;
import org.telegram.messenger.exoplayer2.audio.AudioRendererEventListener.EventDispatcher;
import org.telegram.messenger.exoplayer2.audio.AudioSink.ConfigurationException;
import org.telegram.messenger.exoplayer2.audio.AudioSink.Listener;
import org.telegram.messenger.exoplayer2.audio.AudioSink.WriteException;
import org.telegram.messenger.exoplayer2.drm.DrmInitData;
import org.telegram.messenger.exoplayer2.drm.DrmSessionManager;
import org.telegram.messenger.exoplayer2.drm.FrameworkMediaCrypto;
import org.telegram.messenger.exoplayer2.mediacodec.MediaCodecInfo;
import org.telegram.messenger.exoplayer2.mediacodec.MediaCodecRenderer;
import org.telegram.messenger.exoplayer2.mediacodec.MediaCodecSelector;
import org.telegram.messenger.exoplayer2.mediacodec.MediaCodecUtil.DecoderQueryException;
import org.telegram.messenger.exoplayer2.util.MediaClock;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.exoplayer2.util.Util;

@TargetApi(16)
public class MediaCodecAudioRenderer extends MediaCodecRenderer implements MediaClock {
    private boolean allowPositionDiscontinuity;
    private final AudioSink audioSink;
    private int channelCount;
    private boolean codecNeedsDiscardChannelsWorkaround;
    private long currentPositionUs;
    private int encoderDelay;
    private int encoderPadding;
    private final EventDispatcher eventDispatcher;
    private boolean passthroughEnabled;
    private MediaFormat passthroughMediaFormat;
    private int pcmEncoding;

    private final class AudioSinkListener implements Listener {
        private AudioSinkListener() {
        }

        public void onAudioSessionId(int audioSessionId) {
            MediaCodecAudioRenderer.this.eventDispatcher.audioSessionId(audioSessionId);
            MediaCodecAudioRenderer.this.onAudioSessionId(audioSessionId);
        }

        public void onPositionDiscontinuity() {
            MediaCodecAudioRenderer.this.onAudioTrackPositionDiscontinuity();
            MediaCodecAudioRenderer.this.allowPositionDiscontinuity = true;
        }

        public void onUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
            MediaCodecAudioRenderer.this.eventDispatcher.audioTrackUnderrun(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
            MediaCodecAudioRenderer.this.onAudioTrackUnderrun(bufferSize, bufferSizeMs, elapsedSinceLastFeedMs);
        }
    }

    protected boolean processOutputBuffer(long r1, long r3, android.media.MediaCodec r5, java.nio.ByteBuffer r6, int r7, int r8, long r9, boolean r11) throws org.telegram.messenger.exoplayer2.ExoPlaybackException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.audio.MediaCodecAudioRenderer.processOutputBuffer(long, long, android.media.MediaCodec, java.nio.ByteBuffer, int, int, long, boolean):boolean
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
        r0 = r3.passthroughEnabled;
        r1 = 0;
        r2 = 1;
        if (r0 == 0) goto L_0x000e;
    L_0x0006:
        r0 = r11 & 2;
        if (r0 == 0) goto L_0x000e;
    L_0x000a:
        r8.releaseOutputBuffer(r10, r1);
        return r2;
    L_0x000e:
        if (r14 == 0) goto L_0x0020;
    L_0x0010:
        r8.releaseOutputBuffer(r10, r1);
        r0 = r3.decoderCounters;
        r1 = r0.skippedOutputBufferCount;
        r1 = r1 + r2;
        r0.skippedOutputBufferCount = r1;
        r0 = r3.audioSink;
        r0.handleDiscontinuity();
        return r2;
    L_0x0020:
        r0 = r3.audioSink;	 Catch:{ InitializationException -> 0x0035, InitializationException -> 0x0035 }
        r0 = r0.handleBuffer(r9, r12);	 Catch:{ InitializationException -> 0x0035, InitializationException -> 0x0035 }
        if (r0 == 0) goto L_0x0033;	 Catch:{ InitializationException -> 0x0035, InitializationException -> 0x0035 }
    L_0x0028:
        r8.releaseOutputBuffer(r10, r1);	 Catch:{ InitializationException -> 0x0035, InitializationException -> 0x0035 }
        r0 = r3.decoderCounters;	 Catch:{ InitializationException -> 0x0035, InitializationException -> 0x0035 }
        r1 = r0.renderedOutputBufferCount;	 Catch:{ InitializationException -> 0x0035, InitializationException -> 0x0035 }
        r1 = r1 + r2;	 Catch:{ InitializationException -> 0x0035, InitializationException -> 0x0035 }
        r0.renderedOutputBufferCount = r1;	 Catch:{ InitializationException -> 0x0035, InitializationException -> 0x0035 }
        return r2;
        return r1;
    L_0x0035:
        r0 = move-exception;
        r1 = r3.getIndex();
        r1 = org.telegram.messenger.exoplayer2.ExoPlaybackException.createForRenderer(r0, r1);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.audio.MediaCodecAudioRenderer.processOutputBuffer(long, long, android.media.MediaCodec, java.nio.ByteBuffer, int, int, long, boolean):boolean");
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector) {
        this(mediaCodecSelector, null, true);
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys) {
        this(mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, null, null);
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector, Handler eventHandler, AudioRendererEventListener eventListener) {
        this(mediaCodecSelector, null, true, eventHandler, eventListener);
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, AudioRendererEventListener eventListener) {
        this(mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener, (AudioCapabilities) null, new AudioProcessor[0]);
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, AudioRendererEventListener eventListener, AudioCapabilities audioCapabilities, AudioProcessor... audioProcessors) {
        this(mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys, eventHandler, eventListener, new DefaultAudioSink(audioCapabilities, audioProcessors));
    }

    public MediaCodecAudioRenderer(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, boolean playClearSamplesWithoutKeys, Handler eventHandler, AudioRendererEventListener eventListener, AudioSink audioSink) {
        super(1, mediaCodecSelector, drmSessionManager, playClearSamplesWithoutKeys);
        this.eventDispatcher = new EventDispatcher(eventHandler, eventListener);
        this.audioSink = audioSink;
        audioSink.setListener(new AudioSinkListener());
    }

    protected int supportsFormat(MediaCodecSelector mediaCodecSelector, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, Format format) throws DecoderQueryException {
        MediaCodecAudioRenderer mediaCodecAudioRenderer = this;
        MediaCodecSelector mediaCodecSelector2 = mediaCodecSelector;
        Format format2 = format;
        String mimeType = format2.sampleMimeType;
        if (!MimeTypes.isAudio(mimeType)) {
            return 0;
        }
        int tunnelingSupport = Util.SDK_INT >= 21 ? 32 : 0;
        boolean supportsFormatDrm = BaseRenderer.supportsFormatDrm(drmSessionManager, format2.drmInitData);
        if (supportsFormatDrm && allowPassthrough(mimeType) && mediaCodecSelector.getPassthroughDecoderInfo() != null) {
            return (8 | tunnelingSupport) | 4;
        }
        boolean z = true;
        if ((MimeTypes.AUDIO_RAW.equals(mimeType) && !mediaCodecAudioRenderer.audioSink.isEncodingSupported(format2.pcmEncoding)) || !mediaCodecAudioRenderer.audioSink.isEncodingSupported(2)) {
            return 1;
        }
        boolean requiresSecureDecryption = false;
        DrmInitData drmInitData = format2.drmInitData;
        if (drmInitData != null) {
            boolean requiresSecureDecryption2 = false;
            for (int i = 0; i < drmInitData.schemeDataCount; i++) {
                requiresSecureDecryption2 |= drmInitData.get(i).requiresSecureDecryption;
            }
            requiresSecureDecryption = requiresSecureDecryption2;
        }
        MediaCodecInfo decoderInfo = mediaCodecSelector2.getDecoderInfo(mimeType, requiresSecureDecryption);
        if (decoderInfo == null) {
            int i2;
            if (requiresSecureDecryption && mediaCodecSelector2.getDecoderInfo(mimeType, false) != null) {
                i2 = 2;
            }
            return i2;
        } else if (!supportsFormatDrm) {
            return 2;
        } else {
            if (Util.SDK_INT >= 21) {
                if (format2.sampleRate == -1 || decoderInfo.isAudioSampleRateSupportedV21(format2.sampleRate)) {
                    if (format2.channelCount != -1) {
                        if (decoderInfo.isAudioChannelCountSupportedV21(format2.channelCount)) {
                        }
                    }
                }
                z = false;
            }
            return (8 | tunnelingSupport) | (z ? 4 : 3);
        }
    }

    protected MediaCodecInfo getDecoderInfo(MediaCodecSelector mediaCodecSelector, Format format, boolean requiresSecureDecoder) throws DecoderQueryException {
        if (allowPassthrough(format.sampleMimeType)) {
            MediaCodecInfo passthroughDecoderInfo = mediaCodecSelector.getPassthroughDecoderInfo();
            if (passthroughDecoderInfo != null) {
                this.passthroughEnabled = true;
                return passthroughDecoderInfo;
            }
        }
        this.passthroughEnabled = false;
        return super.getDecoderInfo(mediaCodecSelector, format, requiresSecureDecoder);
    }

    protected boolean allowPassthrough(String mimeType) {
        int encoding = MimeTypes.getEncoding(mimeType);
        return encoding != 0 && this.audioSink.isEncodingSupported(encoding);
    }

    protected void configureCodec(MediaCodecInfo codecInfo, MediaCodec codec, Format format, MediaCrypto crypto) {
        this.codecNeedsDiscardChannelsWorkaround = codecNeedsDiscardChannelsWorkaround(codecInfo.name);
        MediaFormat mediaFormat = getMediaFormatForPlayback(format);
        if (this.passthroughEnabled) {
            this.passthroughMediaFormat = mediaFormat;
            this.passthroughMediaFormat.setString("mime", MimeTypes.AUDIO_RAW);
            codec.configure(this.passthroughMediaFormat, null, crypto, 0);
            this.passthroughMediaFormat.setString("mime", format.sampleMimeType);
            return;
        }
        codec.configure(mediaFormat, null, crypto, 0);
        this.passthroughMediaFormat = null;
    }

    public MediaClock getMediaClock() {
        return this;
    }

    protected void onCodecInitialized(String name, long initializedTimestampMs, long initializationDurationMs) {
        this.eventDispatcher.decoderInitialized(name, initializedTimestampMs, initializationDurationMs);
    }

    protected void onInputFormatChanged(Format newFormat) throws ExoPlaybackException {
        super.onInputFormatChanged(newFormat);
        this.eventDispatcher.inputFormatChanged(newFormat);
        this.pcmEncoding = MimeTypes.AUDIO_RAW.equals(newFormat.sampleMimeType) ? newFormat.pcmEncoding : 2;
        this.channelCount = newFormat.channelCount;
        int i = 0;
        this.encoderDelay = newFormat.encoderDelay != -1 ? newFormat.encoderDelay : 0;
        if (newFormat.encoderPadding != -1) {
            i = newFormat.encoderPadding;
        }
        this.encoderPadding = i;
    }

    protected void onOutputFormatChanged(MediaCodec codec, MediaFormat outputFormat) throws ExoPlaybackException {
        int encoding;
        MediaFormat format;
        int[] iArr;
        if (this.passthroughMediaFormat != null) {
            encoding = MimeTypes.getEncoding(this.passthroughMediaFormat.getString("mime"));
            format = this.passthroughMediaFormat;
        } else {
            encoding = this.pcmEncoding;
            format = outputFormat;
        }
        int channelCount = format.getInteger("channel-count");
        int sampleRate = format.getInteger("sample-rate");
        if (this.codecNeedsDiscardChannelsWorkaround && channelCount == 6 && this.channelCount < 6) {
            iArr = new int[this.channelCount];
            for (int i = 0; i < this.channelCount; i++) {
                iArr[i] = i;
            }
        } else {
            iArr = null;
        }
        try {
            this.audioSink.configure(encoding, channelCount, sampleRate, 0, iArr, this.encoderDelay, this.encoderPadding);
        } catch (ConfigurationException e) {
            throw ExoPlaybackException.createForRenderer(e, getIndex());
        }
    }

    protected void onAudioSessionId(int audioSessionId) {
    }

    protected void onAudioTrackPositionDiscontinuity() {
    }

    protected void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {
    }

    protected void onEnabled(boolean joining) throws ExoPlaybackException {
        super.onEnabled(joining);
        this.eventDispatcher.enabled(this.decoderCounters);
        int tunnelingAudioSessionId = getConfiguration().tunnelingAudioSessionId;
        if (tunnelingAudioSessionId != 0) {
            this.audioSink.enableTunnelingV21(tunnelingAudioSessionId);
        } else {
            this.audioSink.disableTunneling();
        }
    }

    protected void onPositionReset(long positionUs, boolean joining) throws ExoPlaybackException {
        super.onPositionReset(positionUs, joining);
        this.audioSink.reset();
        this.currentPositionUs = positionUs;
        this.allowPositionDiscontinuity = true;
    }

    protected void onStarted() {
        super.onStarted();
        this.audioSink.play();
    }

    protected void onStopped() {
        this.audioSink.pause();
        updateCurrentPosition();
        super.onStopped();
    }

    protected void onDisabled() {
        try {
            this.audioSink.release();
            try {
                super.onDisabled();
            } finally {
                this.decoderCounters.ensureUpdated();
                this.eventDispatcher.disabled(this.decoderCounters);
            }
        } catch (Throwable th) {
            super.onDisabled();
        } finally {
            this.decoderCounters.ensureUpdated();
            this.eventDispatcher.disabled(this.decoderCounters);
        }
    }

    public boolean isEnded() {
        return super.isEnded() && this.audioSink.isEnded();
    }

    public boolean isReady() {
        if (!this.audioSink.hasPendingData()) {
            if (!super.isReady()) {
                return false;
            }
        }
        return true;
    }

    public long getPositionUs() {
        if (getState() == 2) {
            updateCurrentPosition();
        }
        return this.currentPositionUs;
    }

    public PlaybackParameters setPlaybackParameters(PlaybackParameters playbackParameters) {
        return this.audioSink.setPlaybackParameters(playbackParameters);
    }

    public PlaybackParameters getPlaybackParameters() {
        return this.audioSink.getPlaybackParameters();
    }

    protected void renderToEndOfStream() throws ExoPlaybackException {
        try {
            this.audioSink.playToEndOfStream();
        } catch (WriteException e) {
            throw ExoPlaybackException.createForRenderer(e, getIndex());
        }
    }

    public void handleMessage(int messageType, Object message) throws ExoPlaybackException {
        switch (messageType) {
            case 2:
                this.audioSink.setVolume(((Float) message).floatValue());
                return;
            case 3:
                this.audioSink.setAudioAttributes((AudioAttributes) message);
                return;
            default:
                super.handleMessage(messageType, message);
                return;
        }
    }

    private void updateCurrentPosition() {
        long newCurrentPositionUs = this.audioSink.getCurrentPositionUs(isEnded());
        if (newCurrentPositionUs != Long.MIN_VALUE) {
            this.currentPositionUs = this.allowPositionDiscontinuity ? newCurrentPositionUs : Math.max(this.currentPositionUs, newCurrentPositionUs);
            this.allowPositionDiscontinuity = false;
        }
    }

    private static boolean codecNeedsDiscardChannelsWorkaround(String codecName) {
        return Util.SDK_INT < 24 && "OMX.SEC.aac.dec".equals(codecName) && "samsung".equals(Util.MANUFACTURER) && (Util.DEVICE.startsWith("zeroflte") || Util.DEVICE.startsWith("herolte") || Util.DEVICE.startsWith("heroqlte"));
    }
}
