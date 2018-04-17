package org.telegram.messenger.exoplayer2.audio;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioFormat;
import android.media.AudioTimestamp;
import android.media.AudioTrack;
import android.os.ConditionVariable;
import android.os.SystemClock;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.ArrayList;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.PlaybackParameters;
import org.telegram.messenger.exoplayer2.audio.AudioSink.InitializationException;
import org.telegram.messenger.exoplayer2.audio.AudioSink.Listener;
import org.telegram.messenger.exoplayer2.audio.AudioSink.WriteException;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.Util;

public final class DefaultAudioSink implements AudioSink {
    private static final int BUFFER_MULTIPLICATION_FACTOR = 4;
    private static final int ERROR_BAD_VALUE = -2;
    private static final long MAX_AUDIO_TIMESTAMP_OFFSET_US = 5000000;
    private static final long MAX_BUFFER_DURATION_US = 750000;
    private static final long MAX_LATENCY_US = 5000000;
    private static final int MAX_PLAYHEAD_OFFSET_COUNT = 10;
    private static final long MIN_BUFFER_DURATION_US = 250000;
    private static final int MIN_PLAYHEAD_OFFSET_SAMPLE_INTERVAL_US = 30000;
    private static final int MIN_TIMESTAMP_SAMPLE_INTERVAL_US = 500000;
    private static final int MODE_STATIC = 0;
    private static final int MODE_STREAM = 1;
    private static final long PASSTHROUGH_BUFFER_DURATION_US = 250000;
    private static final int PLAYSTATE_PAUSED = 2;
    private static final int PLAYSTATE_PLAYING = 3;
    private static final int PLAYSTATE_STOPPED = 1;
    private static final int START_IN_SYNC = 1;
    private static final int START_NEED_SYNC = 2;
    private static final int START_NOT_SET = 0;
    private static final int STATE_INITIALIZED = 1;
    private static final String TAG = "AudioTrack";
    @SuppressLint({"InlinedApi"})
    private static final int WRITE_NON_BLOCKING = 1;
    public static boolean enablePreV21AudioSessionWorkaround = false;
    public static boolean failOnSpuriousAudioTimestamp = false;
    private AudioAttributes audioAttributes;
    private final AudioCapabilities audioCapabilities;
    private AudioProcessor[] audioProcessors;
    private int audioSessionId;
    private boolean audioTimestampSet;
    private AudioTrack audioTrack;
    private final AudioTrackUtil audioTrackUtil;
    private ByteBuffer avSyncHeader;
    private int bufferSize;
    private long bufferSizeUs;
    private int bytesUntilNextAvSync;
    private boolean canApplyPlaybackParameters;
    private int channelConfig;
    private final ChannelMappingAudioProcessor channelMappingAudioProcessor;
    private int drainingAudioProcessorIndex;
    private PlaybackParameters drainingPlaybackParameters;
    private final boolean enableConvertHighResIntPcmToFloat;
    private int framesPerEncodedSample;
    private Method getLatencyMethod;
    private boolean handledEndOfStream;
    private boolean hasData;
    private ByteBuffer inputBuffer;
    private int inputSampleRate;
    private boolean isInputPcm;
    private AudioTrack keepSessionIdAudioTrack;
    private long lastFeedElapsedRealtimeMs;
    private long lastPlayheadSampleTimeUs;
    private long lastTimestampSampleTimeUs;
    private long latencyUs;
    private Listener listener;
    private int nextPlayheadOffsetIndex;
    private ByteBuffer outputBuffer;
    private ByteBuffer[] outputBuffers;
    private int outputEncoding;
    private int outputPcmFrameSize;
    private int pcmFrameSize;
    private PlaybackParameters playbackParameters;
    private final ArrayDeque<PlaybackParametersCheckpoint> playbackParametersCheckpoints;
    private long playbackParametersOffsetUs;
    private long playbackParametersPositionUs;
    private int playheadOffsetCount;
    private final long[] playheadOffsets;
    private boolean playing;
    private byte[] preV21OutputBuffer;
    private int preV21OutputBufferOffset;
    private boolean processingEnabled;
    private final ConditionVariable releasingConditionVariable;
    private long resumeSystemTimeUs;
    private int sampleRate;
    private boolean shouldConvertHighResIntPcmToFloat;
    private long smoothedPlayheadOffsetUs;
    private final SonicAudioProcessor sonicAudioProcessor;
    private int startMediaTimeState;
    private long startMediaTimeUs;
    private long submittedEncodedFrames;
    private long submittedPcmBytes;
    private final AudioProcessor[] toFloatPcmAvailableAudioProcessors;
    private final AudioProcessor[] toIntPcmAvailableAudioProcessors;
    private final TrimmingAudioProcessor trimmingAudioProcessor;
    private boolean tunneling;
    private float volume;
    private long writtenEncodedFrames;
    private long writtenPcmBytes;

    private static class AudioTrackUtil {
        private static final long FORCE_RESET_WORKAROUND_TIMEOUT_MS = 200;
        protected AudioTrack audioTrack;
        private long endPlaybackHeadPosition;
        private long forceResetWorkaroundTimeMs;
        private long lastRawPlaybackHeadPosition;
        private boolean needsPassthroughWorkaround;
        private long passthroughWorkaroundPauseOffset;
        private long rawPlaybackHeadWrapCount;
        private int sampleRate;
        private long stopPlaybackHeadPosition;
        private long stopTimestampUs;

        private AudioTrackUtil() {
        }

        public void reconfigure(AudioTrack audioTrack, boolean needsPassthroughWorkaround) {
            this.audioTrack = audioTrack;
            this.needsPassthroughWorkaround = needsPassthroughWorkaround;
            this.stopTimestampUs = C.TIME_UNSET;
            this.forceResetWorkaroundTimeMs = C.TIME_UNSET;
            this.lastRawPlaybackHeadPosition = 0;
            this.rawPlaybackHeadWrapCount = 0;
            this.passthroughWorkaroundPauseOffset = 0;
            if (audioTrack != null) {
                this.sampleRate = audioTrack.getSampleRate();
            }
        }

        public void handleEndOfStream(long writtenFrames) {
            this.stopPlaybackHeadPosition = getPlaybackHeadPosition();
            this.stopTimestampUs = SystemClock.elapsedRealtime() * 1000;
            this.endPlaybackHeadPosition = writtenFrames;
            this.audioTrack.stop();
        }

        public void pause() {
            if (this.stopTimestampUs == C.TIME_UNSET) {
                this.audioTrack.pause();
            }
        }

        public boolean needsReset(long writtenFrames) {
            return this.forceResetWorkaroundTimeMs != C.TIME_UNSET && writtenFrames > 0 && SystemClock.elapsedRealtime() - this.forceResetWorkaroundTimeMs >= FORCE_RESET_WORKAROUND_TIMEOUT_MS;
        }

        public long getPlaybackHeadPosition() {
            if (this.stopTimestampUs != C.TIME_UNSET) {
                return Math.min(this.endPlaybackHeadPosition, this.stopPlaybackHeadPosition + ((((long) this.sampleRate) * ((SystemClock.elapsedRealtime() * 1000) - this.stopTimestampUs)) / C.MICROS_PER_SECOND));
            }
            int state = this.audioTrack.getPlayState();
            if (state == 1) {
                return 0;
            }
            long rawPlaybackHeadPosition;
            long rawPlaybackHeadPosition2 = 4294967295L & ((long) this.audioTrack.getPlaybackHeadPosition());
            if (this.needsPassthroughWorkaround) {
                if (state == 2 && rawPlaybackHeadPosition2 == 0) {
                    this.passthroughWorkaroundPauseOffset = this.lastRawPlaybackHeadPosition;
                }
                rawPlaybackHeadPosition = rawPlaybackHeadPosition2 + this.passthroughWorkaroundPauseOffset;
            } else {
                rawPlaybackHeadPosition = rawPlaybackHeadPosition2;
            }
            if (Util.SDK_INT <= 26) {
                if (rawPlaybackHeadPosition == 0 && this.lastRawPlaybackHeadPosition > 0 && state == 3) {
                    if (this.forceResetWorkaroundTimeMs == C.TIME_UNSET) {
                        this.forceResetWorkaroundTimeMs = SystemClock.elapsedRealtime();
                    }
                    return this.lastRawPlaybackHeadPosition;
                }
                this.forceResetWorkaroundTimeMs = C.TIME_UNSET;
            }
            if (this.lastRawPlaybackHeadPosition > rawPlaybackHeadPosition) {
                this.rawPlaybackHeadWrapCount++;
            }
            this.lastRawPlaybackHeadPosition = rawPlaybackHeadPosition;
            return rawPlaybackHeadPosition + (this.rawPlaybackHeadWrapCount << 32);
        }

        public long getPositionUs() {
            return (getPlaybackHeadPosition() * C.MICROS_PER_SECOND) / ((long) this.sampleRate);
        }

        public boolean updateTimestamp() {
            return false;
        }

        public long getTimestampNanoTime() {
            throw new UnsupportedOperationException();
        }

        public long getTimestampFramePosition() {
            throw new UnsupportedOperationException();
        }
    }

    public static final class InvalidAudioTrackTimestampException extends RuntimeException {
        public InvalidAudioTrackTimestampException(String message) {
            super(message);
        }
    }

    private static final class PlaybackParametersCheckpoint {
        private final long mediaTimeUs;
        private final PlaybackParameters playbackParameters;
        private final long positionUs;

        private PlaybackParametersCheckpoint(PlaybackParameters playbackParameters, long mediaTimeUs, long positionUs) {
            this.playbackParameters = playbackParameters;
            this.mediaTimeUs = mediaTimeUs;
            this.positionUs = positionUs;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    private @interface StartMediaTimeState {
    }

    @TargetApi(19)
    private static class AudioTrackUtilV19 extends AudioTrackUtil {
        private final AudioTimestamp audioTimestamp = new AudioTimestamp();
        private long lastRawTimestampFramePosition;
        private long lastTimestampFramePosition;
        private long rawTimestampFramePositionWrapCount;

        public AudioTrackUtilV19() {
            super();
        }

        public void reconfigure(AudioTrack audioTrack, boolean needsPassthroughWorkaround) {
            super.reconfigure(audioTrack, needsPassthroughWorkaround);
            this.rawTimestampFramePositionWrapCount = 0;
            this.lastRawTimestampFramePosition = 0;
            this.lastTimestampFramePosition = 0;
        }

        public boolean updateTimestamp() {
            boolean updated = this.audioTrack.getTimestamp(this.audioTimestamp);
            if (updated) {
                long rawFramePosition = this.audioTimestamp.framePosition;
                if (this.lastRawTimestampFramePosition > rawFramePosition) {
                    this.rawTimestampFramePositionWrapCount++;
                }
                this.lastRawTimestampFramePosition = rawFramePosition;
                this.lastTimestampFramePosition = rawFramePosition + (this.rawTimestampFramePositionWrapCount << 32);
            }
            return updated;
        }

        public long getTimestampNanoTime() {
            return this.audioTimestamp.nanoTime;
        }

        public long getTimestampFramePosition() {
            return this.lastTimestampFramePosition;
        }
    }

    public void configure(int r1, int r2, int r3, int r4, int[] r5, int r6, int r7) throws org.telegram.messenger.exoplayer2.audio.AudioSink.ConfigurationException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.audio.DefaultAudioSink.configure(int, int, int, int, int[], int, int):void
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
        r1 = r24;
        r2 = r25;
        r3 = r28;
        r4 = 0;
        r5 = r27;
        r1.inputSampleRate = r5;
        r6 = r26;
        r7 = r5;
        r8 = isEncodingPcm(r25);
        r1.isInputPcm = r8;
        r8 = r1.enableConvertHighResIntPcmToFloat;
        if (r8 == 0) goto L_0x0028;
    L_0x0018:
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = r1.isEncodingSupported(r8);
        if (r8 == 0) goto L_0x0028;
    L_0x0020:
        r8 = org.telegram.messenger.exoplayer2.util.Util.isEncodingHighResolutionIntegerPcm(r25);
        if (r8 == 0) goto L_0x0028;
    L_0x0026:
        r8 = 1;
        goto L_0x0029;
    L_0x0028:
        r8 = 0;
    L_0x0029:
        r1.shouldConvertHighResIntPcmToFloat = r8;
        r8 = r1.isInputPcm;
        if (r8 == 0) goto L_0x0035;
    L_0x002f:
        r8 = org.telegram.messenger.exoplayer2.util.Util.getPcmFrameSize(r2, r6);
        r1.pcmFrameSize = r8;
    L_0x0035:
        r8 = r2;
        r11 = r1.isInputPcm;
        if (r11 == 0) goto L_0x003f;
    L_0x003a:
        r11 = 4;
        if (r2 == r11) goto L_0x003f;
    L_0x003d:
        r11 = 1;
        goto L_0x0040;
    L_0x003f:
        r11 = 0;
    L_0x0040:
        if (r11 == 0) goto L_0x0048;
    L_0x0042:
        r12 = r1.shouldConvertHighResIntPcmToFloat;
        if (r12 != 0) goto L_0x0048;
    L_0x0046:
        r12 = 1;
        goto L_0x0049;
    L_0x0048:
        r12 = 0;
    L_0x0049:
        r1.canApplyPlaybackParameters = r12;
        if (r11 == 0) goto L_0x0094;
    L_0x004d:
        r12 = r1.trimmingAudioProcessor;
        r13 = r30;
        r14 = r31;
        r12.setTrimSampleCount(r13, r14);
        r12 = r1.channelMappingAudioProcessor;
        r15 = r29;
        r12.setChannelMap(r15);
        r12 = r24.getAvailableAudioProcessors();
        r9 = r12.length;
        r10 = r8;
        r8 = r7;
        r7 = r4;
        r4 = 0;
    L_0x0066:
        if (r4 >= r9) goto L_0x009d;
    L_0x0068:
        r16 = r12[r4];
        r17 = r16;
        r2 = r17;
        r16 = r2.configure(r8, r6, r10);	 Catch:{ UnhandledFormatException -> 0x008c }
        r7 = r7 | r16;
        r16 = r2.isActive();
        if (r16 == 0) goto L_0x0087;
    L_0x007b:
        r6 = r2.getOutputChannelCount();
        r8 = r2.getOutputSampleRateHz();
        r10 = r2.getOutputEncoding();
    L_0x0087:
        r4 = r4 + 1;
        r2 = r25;
        goto L_0x0066;
    L_0x008c:
        r0 = move-exception;
        r4 = r0;
        r9 = new org.telegram.messenger.exoplayer2.audio.AudioSink$ConfigurationException;
        r9.<init>(r4);
        throw r9;
    L_0x0094:
        r15 = r29;
        r13 = r30;
        r14 = r31;
        r10 = r8;
        r8 = r7;
        r7 = r4;
    L_0x009d:
        switch(r6) {
            case 1: goto L_0x00d4;
            case 2: goto L_0x00d1;
            case 3: goto L_0x00ce;
            case 4: goto L_0x00cb;
            case 5: goto L_0x00c8;
            case 6: goto L_0x00c5;
            case 7: goto L_0x00c2;
            case 8: goto L_0x00bf;
            default: goto L_0x00a0;
        };
    L_0x00a0:
        r19 = r7;
        r20 = r8;
        r21 = r10;
        r23 = r11;
        r2 = new org.telegram.messenger.exoplayer2.audio.AudioSink$ConfigurationException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unsupported channel count: ";
        r3.append(r4);
        r3.append(r6);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x00bf:
        r2 = org.telegram.messenger.exoplayer2.C.CHANNEL_OUT_7POINT1_SURROUND;
        goto L_0x00d6;
    L_0x00c2:
        r2 = 1276; // 0x4fc float:1.788E-42 double:6.304E-321;
        goto L_0x00d6;
    L_0x00c5:
        r2 = 252; // 0xfc float:3.53E-43 double:1.245E-321;
        goto L_0x00d6;
    L_0x00c8:
        r2 = 220; // 0xdc float:3.08E-43 double:1.087E-321;
        goto L_0x00d6;
    L_0x00cb:
        r2 = 204; // 0xcc float:2.86E-43 double:1.01E-321;
        goto L_0x00d6;
    L_0x00ce:
        r2 = 28;
        goto L_0x00d6;
    L_0x00d1:
        r2 = 12;
        goto L_0x00d6;
    L_0x00d4:
        r2 = 4;
        r4 = org.telegram.messenger.exoplayer2.util.Util.SDK_INT;
        r9 = 23;
        r12 = 5;
        if (r4 > r9) goto L_0x0101;
        r4 = "foster";
        r9 = org.telegram.messenger.exoplayer2.util.Util.DEVICE;
        r4 = r4.equals(r9);
        if (r4 == 0) goto L_0x0101;
        r4 = "NVIDIA";
        r9 = org.telegram.messenger.exoplayer2.util.Util.MANUFACTURER;
        r4 = r4.equals(r9);
        if (r4 == 0) goto L_0x0101;
        r4 = 3;
        if (r6 == r4) goto L_0x00fe;
        if (r6 == r12) goto L_0x00fe;
        r4 = 7;
        if (r6 == r4) goto L_0x00fb;
        goto L_0x0101;
        r2 = org.telegram.messenger.exoplayer2.C.CHANNEL_OUT_7POINT1_SURROUND;
        goto L_0x0101;
        r2 = 252; // 0xfc float:3.53E-43 double:1.245E-321;
        r4 = org.telegram.messenger.exoplayer2.util.Util.SDK_INT;
        r9 = 25;
        if (r4 > r9) goto L_0x011b;
        r4 = "fugu";
        r9 = org.telegram.messenger.exoplayer2.util.Util.DEVICE;
        r4 = r4.equals(r9);
        if (r4 == 0) goto L_0x011b;
        r4 = r1.isInputPcm;
        if (r4 != 0) goto L_0x011b;
        r4 = 1;
        if (r6 != r4) goto L_0x011c;
        r2 = 12;
        goto L_0x011c;
        r4 = 1;
        if (r7 != 0) goto L_0x0131;
        r9 = r24.isInitialized();
        if (r9 == 0) goto L_0x0131;
        r9 = r1.outputEncoding;
        if (r9 != r10) goto L_0x0131;
        r9 = r1.sampleRate;
        if (r9 != r8) goto L_0x0131;
        r9 = r1.channelConfig;
        if (r9 != r2) goto L_0x0131;
        return;
        r24.reset();
        r1.processingEnabled = r11;
        r1.sampleRate = r8;
        r1.channelConfig = r2;
        r1.outputEncoding = r10;
        r9 = r1.isInputPcm;
        if (r9 == 0) goto L_0x0148;
        r9 = r1.outputEncoding;
        r9 = org.telegram.messenger.exoplayer2.util.Util.getPcmFrameSize(r9, r6);
        r1.outputPcmFrameSize = r9;
        if (r3 == 0) goto L_0x0158;
        r1.bufferSize = r3;
        r18 = r2;
        r19 = r7;
        r20 = r8;
        r21 = r10;
        r23 = r11;
        goto L_0x01c4;
        r9 = r1.isInputPcm;
        if (r9 == 0) goto L_0x019b;
        r9 = r1.outputEncoding;
        r9 = android.media.AudioTrack.getMinBufferSize(r8, r2, r9);
        r12 = -2;
        if (r9 == r12) goto L_0x0166;
        goto L_0x0167;
        r4 = 0;
        org.telegram.messenger.exoplayer2.util.Assertions.checkState(r4);
        r4 = r9 * 4;
        r18 = r2;
        r2 = 250000; // 0x3d090 float:3.50325E-40 double:1.235164E-318;
        r2 = r1.durationUsToFrames(r2);
        r2 = (int) r2;
        r3 = r1.outputPcmFrameSize;
        r2 = r2 * r3;
        r19 = r7;
        r20 = r8;
        r7 = (long) r9;
        r22 = r9;
        r21 = r10;
        r9 = 750000; // 0xb71b0 float:1.050974E-39 double:3.70549E-318;
        r9 = r1.durationUsToFrames(r9);
        r3 = r1.outputPcmFrameSize;
        r23 = r11;
        r11 = (long) r3;
        r9 = r9 * r11;
        r7 = java.lang.Math.max(r7, r9);
        r3 = (int) r7;
        r7 = org.telegram.messenger.exoplayer2.util.Util.constrainValue(r4, r2, r3);
        r1.bufferSize = r7;
        goto L_0x01c4;
        r18 = r2;
        r19 = r7;
        r20 = r8;
        r21 = r10;
        r23 = r11;
        r2 = r1.outputEncoding;
        if (r2 == r12) goto L_0x01c0;
        r2 = r1.outputEncoding;
        r3 = 6;
        if (r2 != r3) goto L_0x01af;
        goto L_0x01c0;
        r2 = r1.outputEncoding;
        r3 = 7;
        if (r2 != r3) goto L_0x01ba;
        r2 = 49152; // 0xc000 float:6.8877E-41 double:2.42843E-319;
        r1.bufferSize = r2;
        goto L_0x01c4;
        r2 = 294912; // 0x48000 float:4.1326E-40 double:1.45706E-318;
        r1.bufferSize = r2;
        goto L_0x01c4;
        r2 = 20480; // 0x5000 float:2.8699E-41 double:1.01185E-319;
        r1.bufferSize = r2;
        r2 = r1.isInputPcm;
        if (r2 == 0) goto L_0x01d3;
        r2 = r1.bufferSize;
        r3 = r1.outputPcmFrameSize;
        r2 = r2 / r3;
        r2 = (long) r2;
        r2 = r1.framesToDurationUs(r2);
        goto L_0x01d8;
        r2 = -9223372036854775807; // 0x8000000000000001 float:1.4E-45 double:-4.9E-324;
        r1.bufferSizeUs = r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.audio.DefaultAudioSink.configure(int, int, int, int, int[], int, int):void");
    }

    public DefaultAudioSink(AudioCapabilities audioCapabilities, AudioProcessor[] audioProcessors) {
        this(audioCapabilities, audioProcessors, false);
    }

    public DefaultAudioSink(AudioCapabilities audioCapabilities, AudioProcessor[] audioProcessors, boolean enableConvertHighResIntPcmToFloat) {
        this.audioCapabilities = audioCapabilities;
        this.enableConvertHighResIntPcmToFloat = enableConvertHighResIntPcmToFloat;
        this.releasingConditionVariable = new ConditionVariable(true);
        if (Util.SDK_INT >= 18) {
            try {
                this.getLatencyMethod = AudioTrack.class.getMethod("getLatency", (Class[]) null);
            } catch (NoSuchMethodException e) {
            }
        }
        if (Util.SDK_INT >= 19) {
            this.audioTrackUtil = new AudioTrackUtilV19();
        } else {
            this.audioTrackUtil = new AudioTrackUtil();
        }
        this.channelMappingAudioProcessor = new ChannelMappingAudioProcessor();
        this.trimmingAudioProcessor = new TrimmingAudioProcessor();
        this.sonicAudioProcessor = new SonicAudioProcessor();
        this.toIntPcmAvailableAudioProcessors = new AudioProcessor[(4 + audioProcessors.length)];
        this.toIntPcmAvailableAudioProcessors[0] = new ResamplingAudioProcessor();
        this.toIntPcmAvailableAudioProcessors[1] = this.channelMappingAudioProcessor;
        this.toIntPcmAvailableAudioProcessors[2] = this.trimmingAudioProcessor;
        System.arraycopy(audioProcessors, 0, this.toIntPcmAvailableAudioProcessors, 3, audioProcessors.length);
        this.toIntPcmAvailableAudioProcessors[3 + audioProcessors.length] = this.sonicAudioProcessor;
        this.toFloatPcmAvailableAudioProcessors = new AudioProcessor[]{new FloatResamplingAudioProcessor()};
        this.playheadOffsets = new long[10];
        this.volume = 1.0f;
        this.startMediaTimeState = 0;
        this.audioAttributes = AudioAttributes.DEFAULT;
        this.audioSessionId = 0;
        this.playbackParameters = PlaybackParameters.DEFAULT;
        this.drainingAudioProcessorIndex = -1;
        this.audioProcessors = new AudioProcessor[0];
        this.outputBuffers = new ByteBuffer[0];
        this.playbackParametersCheckpoints = new ArrayDeque();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public boolean isEncodingSupported(int encoding) {
        boolean z = true;
        if (isEncodingPcm(encoding)) {
            if (encoding == 4) {
                if (Util.SDK_INT < 21) {
                    z = false;
                }
            }
            return z;
        }
        if (this.audioCapabilities == null || !this.audioCapabilities.supportsEncoding(encoding)) {
            z = false;
        }
        return z;
    }

    public long getCurrentPositionUs(boolean sourceEnded) {
        if (!hasCurrentPositionUs()) {
            return Long.MIN_VALUE;
        }
        long positionUs;
        if (this.audioTrack.getPlayState() == 3) {
            maybeSampleSyncParams();
        }
        long systemClockUs = System.nanoTime() / 1000;
        if (this.audioTimestampSet) {
            positionUs = framesToDurationUs(this.audioTrackUtil.getTimestampFramePosition() + durationUsToFrames(systemClockUs - (this.audioTrackUtil.getTimestampNanoTime() / 1000)));
        } else {
            if (this.playheadOffsetCount == 0) {
                positionUs = this.audioTrackUtil.getPositionUs();
            } else {
                positionUs = systemClockUs + this.smoothedPlayheadOffsetUs;
            }
            if (!sourceEnded) {
                positionUs -= this.latencyUs;
            }
        }
        return this.startMediaTimeUs + applySpeedup(Math.min(positionUs, framesToDurationUs(getWrittenFrames())));
    }

    private void resetAudioProcessors() {
        ArrayList<AudioProcessor> newAudioProcessors = new ArrayList();
        int i = 0;
        for (AudioProcessor audioProcessor : getAvailableAudioProcessors()) {
            if (audioProcessor.isActive()) {
                newAudioProcessors.add(audioProcessor);
            } else {
                audioProcessor.flush();
            }
        }
        int count = newAudioProcessors.size();
        this.audioProcessors = (AudioProcessor[]) newAudioProcessors.toArray(new AudioProcessor[count]);
        this.outputBuffers = new ByteBuffer[count];
        while (i < count) {
            AudioProcessor audioProcessor2 = this.audioProcessors[i];
            audioProcessor2.flush();
            this.outputBuffers[i] = audioProcessor2.getOutput();
            i++;
        }
    }

    private void initialize() throws InitializationException {
        this.releasingConditionVariable.block();
        this.audioTrack = initializeAudioTrack();
        setPlaybackParameters(this.playbackParameters);
        resetAudioProcessors();
        int audioSessionId = this.audioTrack.getAudioSessionId();
        if (enablePreV21AudioSessionWorkaround && Util.SDK_INT < 21) {
            if (!(this.keepSessionIdAudioTrack == null || audioSessionId == this.keepSessionIdAudioTrack.getAudioSessionId())) {
                releaseKeepSessionIdAudioTrack();
            }
            if (this.keepSessionIdAudioTrack == null) {
                this.keepSessionIdAudioTrack = initializeKeepSessionIdAudioTrack(audioSessionId);
            }
        }
        if (this.audioSessionId != audioSessionId) {
            this.audioSessionId = audioSessionId;
            if (this.listener != null) {
                this.listener.onAudioSessionId(audioSessionId);
            }
        }
        this.audioTrackUtil.reconfigure(this.audioTrack, needsPassthroughWorkarounds());
        setVolumeInternal();
        this.hasData = false;
    }

    public void play() {
        this.playing = true;
        if (isInitialized()) {
            this.resumeSystemTimeUs = System.nanoTime() / 1000;
            this.audioTrack.play();
        }
    }

    public void handleDiscontinuity() {
        if (this.startMediaTimeState == 1) {
            this.startMediaTimeState = 2;
        }
    }

    public boolean handleBuffer(ByteBuffer buffer, long presentationTimeUs) throws InitializationException, WriteException {
        boolean z;
        long expectedPresentationTimeUs;
        int i;
        ByteBuffer byteBuffer = buffer;
        long j = presentationTimeUs;
        if (this.inputBuffer != null) {
            if (byteBuffer != r0.inputBuffer) {
                z = false;
                Assertions.checkArgument(z);
                if (!isInitialized()) {
                    initialize();
                    if (r0.playing) {
                        play();
                    }
                }
                if (needsPassthroughWorkarounds()) {
                    if (r0.audioTrack.getPlayState() != 2) {
                        r0.hasData = false;
                        return false;
                    } else if (r0.audioTrack.getPlayState() == 1 && r0.audioTrackUtil.getPlaybackHeadPosition() != 0) {
                        return false;
                    }
                }
                z = r0.hasData;
                r0.hasData = hasPendingData();
                if (!(!z || r0.hasData || r0.audioTrack.getPlayState() == 1 || r0.listener == null)) {
                    r0.listener.onUnderrun(r0.bufferSize, C.usToMs(r0.bufferSizeUs), SystemClock.elapsedRealtime() - r0.lastFeedElapsedRealtimeMs);
                }
                if (r0.inputBuffer == null) {
                    if (!buffer.hasRemaining()) {
                        return true;
                    }
                    if (!r0.isInputPcm && r0.framesPerEncodedSample == 0) {
                        r0.framesPerEncodedSample = getFramesPerEncodedSample(r0.outputEncoding, byteBuffer);
                        if (r0.framesPerEncodedSample == 0) {
                            return true;
                        }
                    }
                    if (r0.drainingPlaybackParameters != null) {
                        if (!drainAudioProcessorsToEndOfStream()) {
                            return false;
                        }
                        ArrayDeque arrayDeque = r0.playbackParametersCheckpoints;
                        PlaybackParametersCheckpoint playbackParametersCheckpoint = r12;
                        PlaybackParametersCheckpoint playbackParametersCheckpoint2 = new PlaybackParametersCheckpoint(r0.drainingPlaybackParameters, Math.max(0, j), framesToDurationUs(getWrittenFrames()));
                        arrayDeque.add(playbackParametersCheckpoint);
                        r0.drainingPlaybackParameters = null;
                        resetAudioProcessors();
                    }
                    if (r0.startMediaTimeState != 0) {
                        r0.startMediaTimeUs = Math.max(0, j);
                        r0.startMediaTimeState = 1;
                    } else {
                        expectedPresentationTimeUs = r0.startMediaTimeUs + inputFramesToDurationUs(getSubmittedFrames());
                        if (r0.startMediaTimeState == 1 || Math.abs(expectedPresentationTimeUs - j) <= 200000) {
                            i = 2;
                        } else {
                            String str = TAG;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Discontinuity detected [expected ");
                            stringBuilder.append(expectedPresentationTimeUs);
                            stringBuilder.append(", got ");
                            stringBuilder.append(j);
                            stringBuilder.append("]");
                            Log.e(str, stringBuilder.toString());
                            i = 2;
                            r0.startMediaTimeState = 2;
                        }
                        if (r0.startMediaTimeState == i) {
                            r0.startMediaTimeUs += j - expectedPresentationTimeUs;
                            r0.startMediaTimeState = 1;
                            if (r0.listener != null) {
                                r0.listener.onPositionDiscontinuity();
                            }
                        }
                    }
                    if (r0.isInputPcm) {
                        r0.submittedEncodedFrames += (long) r0.framesPerEncodedSample;
                    } else {
                        r0.submittedPcmBytes += (long) buffer.remaining();
                    }
                    r0.inputBuffer = byteBuffer;
                }
                if (r0.processingEnabled) {
                    writeBuffer(r0.inputBuffer, j);
                } else {
                    processBuffers(j);
                }
                if (!r0.inputBuffer.hasRemaining()) {
                    r0.inputBuffer = null;
                    return true;
                } else if (r0.audioTrackUtil.needsReset(getWrittenFrames())) {
                    return false;
                } else {
                    Log.w(TAG, "Resetting stalled audio track");
                    reset();
                    return true;
                }
            }
        }
        z = true;
        Assertions.checkArgument(z);
        if (isInitialized()) {
            initialize();
            if (r0.playing) {
                play();
            }
        }
        if (needsPassthroughWorkarounds()) {
            if (r0.audioTrack.getPlayState() != 2) {
                return false;
            }
            r0.hasData = false;
            return false;
        }
        z = r0.hasData;
        r0.hasData = hasPendingData();
        r0.listener.onUnderrun(r0.bufferSize, C.usToMs(r0.bufferSizeUs), SystemClock.elapsedRealtime() - r0.lastFeedElapsedRealtimeMs);
        if (r0.inputBuffer == null) {
            if (!buffer.hasRemaining()) {
                return true;
            }
            r0.framesPerEncodedSample = getFramesPerEncodedSample(r0.outputEncoding, byteBuffer);
            if (r0.framesPerEncodedSample == 0) {
                return true;
            }
            if (r0.drainingPlaybackParameters != null) {
                if (!drainAudioProcessorsToEndOfStream()) {
                    return false;
                }
                ArrayDeque arrayDeque2 = r0.playbackParametersCheckpoints;
                PlaybackParametersCheckpoint playbackParametersCheckpoint3 = playbackParametersCheckpoint2;
                PlaybackParametersCheckpoint playbackParametersCheckpoint22 = new PlaybackParametersCheckpoint(r0.drainingPlaybackParameters, Math.max(0, j), framesToDurationUs(getWrittenFrames()));
                arrayDeque2.add(playbackParametersCheckpoint3);
                r0.drainingPlaybackParameters = null;
                resetAudioProcessors();
            }
            if (r0.startMediaTimeState != 0) {
                expectedPresentationTimeUs = r0.startMediaTimeUs + inputFramesToDurationUs(getSubmittedFrames());
                if (r0.startMediaTimeState == 1) {
                }
                i = 2;
                if (r0.startMediaTimeState == i) {
                    r0.startMediaTimeUs += j - expectedPresentationTimeUs;
                    r0.startMediaTimeState = 1;
                    if (r0.listener != null) {
                        r0.listener.onPositionDiscontinuity();
                    }
                }
            } else {
                r0.startMediaTimeUs = Math.max(0, j);
                r0.startMediaTimeState = 1;
            }
            if (r0.isInputPcm) {
                r0.submittedEncodedFrames += (long) r0.framesPerEncodedSample;
            } else {
                r0.submittedPcmBytes += (long) buffer.remaining();
            }
            r0.inputBuffer = byteBuffer;
        }
        if (r0.processingEnabled) {
            writeBuffer(r0.inputBuffer, j);
        } else {
            processBuffers(j);
        }
        if (!r0.inputBuffer.hasRemaining()) {
            r0.inputBuffer = null;
            return true;
        } else if (r0.audioTrackUtil.needsReset(getWrittenFrames())) {
            return false;
        } else {
            Log.w(TAG, "Resetting stalled audio track");
            reset();
            return true;
        }
    }

    private void processBuffers(long avSyncPresentationTimeUs) throws WriteException {
        int count = this.audioProcessors.length;
        int index = count;
        while (index >= 0) {
            ByteBuffer input = index > 0 ? this.outputBuffers[index - 1] : this.inputBuffer != null ? this.inputBuffer : AudioProcessor.EMPTY_BUFFER;
            if (index == count) {
                writeBuffer(input, avSyncPresentationTimeUs);
            } else {
                AudioProcessor audioProcessor = this.audioProcessors[index];
                audioProcessor.queueInput(input);
                ByteBuffer output = audioProcessor.getOutput();
                this.outputBuffers[index] = output;
                if (output.hasRemaining()) {
                    index++;
                }
            }
            if (!input.hasRemaining()) {
                index--;
            } else {
                return;
            }
        }
    }

    private void writeBuffer(ByteBuffer buffer, long avSyncPresentationTimeUs) throws WriteException {
        if (buffer.hasRemaining()) {
            int bytesRemaining;
            int originalPosition;
            boolean z = true;
            if (this.outputBuffer != null) {
                Assertions.checkArgument(this.outputBuffer == buffer);
            } else {
                this.outputBuffer = buffer;
                if (Util.SDK_INT < 21) {
                    bytesRemaining = buffer.remaining();
                    if (this.preV21OutputBuffer == null || this.preV21OutputBuffer.length < bytesRemaining) {
                        this.preV21OutputBuffer = new byte[bytesRemaining];
                    }
                    originalPosition = buffer.position();
                    buffer.get(this.preV21OutputBuffer, 0, bytesRemaining);
                    buffer.position(originalPosition);
                    this.preV21OutputBufferOffset = 0;
                }
            }
            bytesRemaining = buffer.remaining();
            originalPosition = 0;
            if (Util.SDK_INT < 21) {
                int bytesToWrite = this.bufferSize - ((int) (this.writtenPcmBytes - (this.audioTrackUtil.getPlaybackHeadPosition() * ((long) this.outputPcmFrameSize))));
                if (bytesToWrite > 0) {
                    originalPosition = this.audioTrack.write(this.preV21OutputBuffer, this.preV21OutputBufferOffset, Math.min(bytesRemaining, bytesToWrite));
                    if (originalPosition > 0) {
                        this.preV21OutputBufferOffset += originalPosition;
                        buffer.position(buffer.position() + originalPosition);
                    }
                }
            } else if (this.tunneling) {
                if (avSyncPresentationTimeUs == C.TIME_UNSET) {
                    z = false;
                }
                Assertions.checkState(z);
                originalPosition = writeNonBlockingWithAvSyncV21(this.audioTrack, buffer, bytesRemaining, avSyncPresentationTimeUs);
            } else {
                originalPosition = writeNonBlockingV21(this.audioTrack, buffer, bytesRemaining);
            }
            this.lastFeedElapsedRealtimeMs = SystemClock.elapsedRealtime();
            if (originalPosition < 0) {
                throw new WriteException(originalPosition);
            }
            if (this.isInputPcm) {
                this.writtenPcmBytes += (long) originalPosition;
            }
            if (originalPosition == bytesRemaining) {
                if (!this.isInputPcm) {
                    this.writtenEncodedFrames += (long) this.framesPerEncodedSample;
                }
                this.outputBuffer = null;
            }
        }
    }

    public void playToEndOfStream() throws WriteException {
        if (!this.handledEndOfStream) {
            if (isInitialized()) {
                if (drainAudioProcessorsToEndOfStream()) {
                    this.audioTrackUtil.handleEndOfStream(getWrittenFrames());
                    this.bytesUntilNextAvSync = 0;
                    this.handledEndOfStream = true;
                }
            }
        }
    }

    private boolean drainAudioProcessorsToEndOfStream() throws WriteException {
        boolean audioProcessorNeedsEndOfStream = false;
        if (this.drainingAudioProcessorIndex == -1) {
            this.drainingAudioProcessorIndex = this.processingEnabled ? 0 : this.audioProcessors.length;
            audioProcessorNeedsEndOfStream = true;
        }
        while (this.drainingAudioProcessorIndex < this.audioProcessors.length) {
            AudioProcessor audioProcessor = this.audioProcessors[this.drainingAudioProcessorIndex];
            if (audioProcessorNeedsEndOfStream) {
                audioProcessor.queueEndOfStream();
            }
            processBuffers(C.TIME_UNSET);
            if (!audioProcessor.isEnded()) {
                return false;
            }
            audioProcessorNeedsEndOfStream = true;
            this.drainingAudioProcessorIndex++;
        }
        if (this.outputBuffer != null) {
            writeBuffer(this.outputBuffer, C.TIME_UNSET);
            if (this.outputBuffer != null) {
                return false;
            }
        }
        this.drainingAudioProcessorIndex = -1;
        return true;
    }

    public boolean isEnded() {
        if (isInitialized()) {
            if (!this.handledEndOfStream || hasPendingData()) {
                return false;
            }
        }
        return true;
    }

    public boolean hasPendingData() {
        return isInitialized() && (getWrittenFrames() > this.audioTrackUtil.getPlaybackHeadPosition() || overrideHasPendingData());
    }

    public PlaybackParameters setPlaybackParameters(PlaybackParameters playbackParameters) {
        if (!isInitialized() || this.canApplyPlaybackParameters) {
            playbackParameters = new PlaybackParameters(this.sonicAudioProcessor.setSpeed(playbackParameters.speed), this.sonicAudioProcessor.setPitch(playbackParameters.pitch));
            PlaybackParameters lastSetPlaybackParameters = this.drainingPlaybackParameters != null ? this.drainingPlaybackParameters : !this.playbackParametersCheckpoints.isEmpty() ? ((PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.getLast()).playbackParameters : this.playbackParameters;
            if (!playbackParameters.equals(lastSetPlaybackParameters)) {
                if (isInitialized()) {
                    this.drainingPlaybackParameters = playbackParameters;
                } else {
                    this.playbackParameters = playbackParameters;
                }
            }
            return this.playbackParameters;
        }
        this.playbackParameters = PlaybackParameters.DEFAULT;
        return this.playbackParameters;
    }

    public PlaybackParameters getPlaybackParameters() {
        return this.playbackParameters;
    }

    public void setAudioAttributes(AudioAttributes audioAttributes) {
        if (!this.audioAttributes.equals(audioAttributes)) {
            this.audioAttributes = audioAttributes;
            if (!this.tunneling) {
                reset();
                this.audioSessionId = 0;
            }
        }
    }

    public void setAudioSessionId(int audioSessionId) {
        if (this.audioSessionId != audioSessionId) {
            this.audioSessionId = audioSessionId;
            reset();
        }
    }

    public void enableTunnelingV21(int tunnelingAudioSessionId) {
        Assertions.checkState(Util.SDK_INT >= 21);
        if (!this.tunneling || this.audioSessionId != tunnelingAudioSessionId) {
            this.tunneling = true;
            this.audioSessionId = tunnelingAudioSessionId;
            reset();
        }
    }

    public void disableTunneling() {
        if (this.tunneling) {
            this.tunneling = false;
            this.audioSessionId = 0;
            reset();
        }
    }

    public void setVolume(float volume) {
        if (this.volume != volume) {
            this.volume = volume;
            setVolumeInternal();
        }
    }

    private void setVolumeInternal() {
        if (!isInitialized()) {
            return;
        }
        if (Util.SDK_INT >= 21) {
            setVolumeInternalV21(this.audioTrack, this.volume);
        } else {
            setVolumeInternalV3(this.audioTrack, this.volume);
        }
    }

    public void pause() {
        this.playing = false;
        if (isInitialized()) {
            resetSyncParams();
            this.audioTrackUtil.pause();
        }
    }

    public void reset() {
        if (isInitialized()) {
            this.submittedPcmBytes = 0;
            this.submittedEncodedFrames = 0;
            this.writtenPcmBytes = 0;
            this.writtenEncodedFrames = 0;
            this.framesPerEncodedSample = 0;
            if (this.drainingPlaybackParameters != null) {
                this.playbackParameters = this.drainingPlaybackParameters;
                this.drainingPlaybackParameters = null;
            } else if (!this.playbackParametersCheckpoints.isEmpty()) {
                this.playbackParameters = ((PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.getLast()).playbackParameters;
            }
            this.playbackParametersCheckpoints.clear();
            this.playbackParametersOffsetUs = 0;
            this.playbackParametersPositionUs = 0;
            this.inputBuffer = null;
            this.outputBuffer = null;
            for (int i = 0; i < this.audioProcessors.length; i++) {
                AudioProcessor audioProcessor = this.audioProcessors[i];
                audioProcessor.flush();
                this.outputBuffers[i] = audioProcessor.getOutput();
            }
            this.handledEndOfStream = false;
            this.drainingAudioProcessorIndex = -1;
            this.avSyncHeader = null;
            this.bytesUntilNextAvSync = 0;
            this.startMediaTimeState = 0;
            this.latencyUs = 0;
            resetSyncParams();
            if (this.audioTrack.getPlayState() == 3) {
                this.audioTrack.pause();
            }
            final AudioTrack toRelease = this.audioTrack;
            this.audioTrack = null;
            this.audioTrackUtil.reconfigure(null, false);
            this.releasingConditionVariable.close();
            new Thread() {
                public void run() {
                    try {
                        toRelease.flush();
                        toRelease.release();
                    } finally {
                        DefaultAudioSink.this.releasingConditionVariable.open();
                    }
                }
            }.start();
        }
    }

    public void release() {
        reset();
        releaseKeepSessionIdAudioTrack();
        for (AudioProcessor audioProcessor : this.toIntPcmAvailableAudioProcessors) {
            audioProcessor.reset();
        }
        for (AudioProcessor audioProcessor2 : this.toFloatPcmAvailableAudioProcessors) {
            audioProcessor2.reset();
        }
        this.audioSessionId = 0;
        this.playing = false;
    }

    private void releaseKeepSessionIdAudioTrack() {
        if (this.keepSessionIdAudioTrack != null) {
            final AudioTrack toRelease = this.keepSessionIdAudioTrack;
            this.keepSessionIdAudioTrack = null;
            new Thread() {
                public void run() {
                    toRelease.release();
                }
            }.start();
        }
    }

    private boolean hasCurrentPositionUs() {
        return isInitialized() && this.startMediaTimeState != 0;
    }

    private long applySpeedup(long positionUs) {
        while (!this.playbackParametersCheckpoints.isEmpty() && positionUs >= ((PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.getFirst()).positionUs) {
            PlaybackParametersCheckpoint checkpoint = (PlaybackParametersCheckpoint) this.playbackParametersCheckpoints.remove();
            this.playbackParameters = checkpoint.playbackParameters;
            this.playbackParametersPositionUs = checkpoint.positionUs;
            this.playbackParametersOffsetUs = checkpoint.mediaTimeUs - this.startMediaTimeUs;
        }
        if (this.playbackParameters.speed == 1.0f) {
            return (positionUs + this.playbackParametersOffsetUs) - this.playbackParametersPositionUs;
        }
        if (this.playbackParametersCheckpoints.isEmpty()) {
            return this.playbackParametersOffsetUs + this.sonicAudioProcessor.scaleDurationForSpeedup(positionUs - this.playbackParametersPositionUs);
        }
        return this.playbackParametersOffsetUs + Util.getMediaDurationForPlayoutDuration(positionUs - this.playbackParametersPositionUs, this.playbackParameters.speed);
    }

    private void maybeSampleSyncParams() {
        long playbackPositionUs = this.audioTrackUtil.getPositionUs();
        if (playbackPositionUs != 0) {
            long systemClockUs = System.nanoTime() / 1000;
            if (systemClockUs - r1.lastPlayheadSampleTimeUs >= 30000) {
                r1.playheadOffsets[r1.nextPlayheadOffsetIndex] = playbackPositionUs - systemClockUs;
                r1.nextPlayheadOffsetIndex = (r1.nextPlayheadOffsetIndex + 1) % 10;
                if (r1.playheadOffsetCount < 10) {
                    r1.playheadOffsetCount++;
                }
                r1.lastPlayheadSampleTimeUs = systemClockUs;
                r1.smoothedPlayheadOffsetUs = 0;
                for (int i = 0; i < r1.playheadOffsetCount; i++) {
                    r1.smoothedPlayheadOffsetUs += r1.playheadOffsets[i] / ((long) r1.playheadOffsetCount);
                }
            }
            if (!needsPassthroughWorkarounds() && systemClockUs - r1.lastTimestampSampleTimeUs >= 500000) {
                StringBuilder stringBuilder;
                r1.audioTimestampSet = r1.audioTrackUtil.updateTimestamp();
                if (r1.audioTimestampSet) {
                    long audioTimestampUs = r1.audioTrackUtil.getTimestampNanoTime() / 1000;
                    long audioTimestampFramePosition = r1.audioTrackUtil.getTimestampFramePosition();
                    if (audioTimestampUs < r1.resumeSystemTimeUs) {
                        r1.audioTimestampSet = false;
                    } else if (Math.abs(audioTimestampUs - systemClockUs) > 5000000) {
                        message = new StringBuilder();
                        message.append("Spurious audio timestamp (system clock mismatch): ");
                        message.append(audioTimestampFramePosition);
                        message.append(", ");
                        message.append(audioTimestampUs);
                        message.append(", ");
                        message.append(systemClockUs);
                        message.append(", ");
                        message.append(playbackPositionUs);
                        message.append(", ");
                        message.append(getSubmittedFrames());
                        message.append(", ");
                        message.append(getWrittenFrames());
                        message = message.toString();
                        if (failOnSpuriousAudioTimestamp) {
                            throw new InvalidAudioTrackTimestampException(message);
                        }
                        Log.w(TAG, message);
                        r1.audioTimestampSet = false;
                    } else if (Math.abs(framesToDurationUs(audioTimestampFramePosition) - playbackPositionUs) > 5000000) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Spurious audio timestamp (frame position mismatch): ");
                        stringBuilder.append(audioTimestampFramePosition);
                        stringBuilder.append(", ");
                        stringBuilder.append(audioTimestampUs);
                        stringBuilder.append(", ");
                        stringBuilder.append(systemClockUs);
                        stringBuilder.append(", ");
                        stringBuilder.append(playbackPositionUs);
                        stringBuilder.append(", ");
                        stringBuilder.append(getSubmittedFrames());
                        stringBuilder.append(", ");
                        stringBuilder.append(getWrittenFrames());
                        message = stringBuilder.toString();
                        if (failOnSpuriousAudioTimestamp) {
                            throw new InvalidAudioTrackTimestampException(message);
                        }
                        Log.w(TAG, message);
                        r1.audioTimestampSet = false;
                    }
                }
                if (r1.getLatencyMethod != null && r1.isInputPcm) {
                    try {
                        r1.latencyUs = (((long) ((Integer) r1.getLatencyMethod.invoke(r1.audioTrack, (Object[]) null)).intValue()) * 1000) - r1.bufferSizeUs;
                        r1.latencyUs = Math.max(r1.latencyUs, 0);
                        if (r1.latencyUs > 5000000) {
                            String str = TAG;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("Ignoring impossibly large audio latency: ");
                            stringBuilder.append(r1.latencyUs);
                            Log.w(str, stringBuilder.toString());
                            r1.latencyUs = 0;
                        }
                    } catch (Exception e) {
                        r1.getLatencyMethod = null;
                    }
                }
                r1.lastTimestampSampleTimeUs = systemClockUs;
            }
        }
    }

    private boolean isInitialized() {
        return this.audioTrack != null;
    }

    private long inputFramesToDurationUs(long frameCount) {
        return (C.MICROS_PER_SECOND * frameCount) / ((long) this.inputSampleRate);
    }

    private long framesToDurationUs(long frameCount) {
        return (C.MICROS_PER_SECOND * frameCount) / ((long) this.sampleRate);
    }

    private long durationUsToFrames(long durationUs) {
        return (((long) this.sampleRate) * durationUs) / C.MICROS_PER_SECOND;
    }

    private long getSubmittedFrames() {
        return this.isInputPcm ? this.submittedPcmBytes / ((long) this.pcmFrameSize) : this.submittedEncodedFrames;
    }

    private long getWrittenFrames() {
        return this.isInputPcm ? this.writtenPcmBytes / ((long) this.outputPcmFrameSize) : this.writtenEncodedFrames;
    }

    private void resetSyncParams() {
        this.smoothedPlayheadOffsetUs = 0;
        this.playheadOffsetCount = 0;
        this.nextPlayheadOffsetIndex = 0;
        this.lastPlayheadSampleTimeUs = 0;
        this.audioTimestampSet = false;
        this.lastTimestampSampleTimeUs = 0;
    }

    private boolean needsPassthroughWorkarounds() {
        return Util.SDK_INT < 23 && (this.outputEncoding == 5 || this.outputEncoding == 6);
    }

    private boolean overrideHasPendingData() {
        return needsPassthroughWorkarounds() && this.audioTrack.getPlayState() == 2 && this.audioTrack.getPlaybackHeadPosition() == 0;
    }

    private AudioTrack initializeAudioTrack() throws InitializationException {
        AudioTrack audioTrack;
        if (Util.SDK_INT >= 21) {
            audioTrack = createAudioTrackV21();
        } else {
            int streamType = Util.getStreamTypeForAudioUsage(this.audioAttributes.usage);
            if (this.audioSessionId == 0) {
                audioTrack = new AudioTrack(streamType, this.sampleRate, this.channelConfig, this.outputEncoding, this.bufferSize, 1);
            } else {
                audioTrack = new AudioTrack(streamType, this.sampleRate, this.channelConfig, this.outputEncoding, this.bufferSize, 1, this.audioSessionId);
            }
        }
        int state = audioTrack.getState();
        if (state == 1) {
            return audioTrack;
        }
        try {
            audioTrack.release();
        } catch (Exception e) {
        }
        throw new InitializationException(state, this.sampleRate, this.channelConfig, this.bufferSize);
    }

    @TargetApi(21)
    private AudioTrack createAudioTrackV21() {
        AudioAttributes build;
        if (this.tunneling) {
            build = new Builder().setContentType(3).setFlags(16).setUsage(1).build();
        } else {
            build = this.audioAttributes.getAudioAttributesV21();
        }
        return new AudioTrack(build, new AudioFormat.Builder().setChannelMask(this.channelConfig).setEncoding(this.outputEncoding).setSampleRate(this.sampleRate).build(), this.bufferSize, 1, this.audioSessionId != 0 ? this.audioSessionId : 0);
    }

    private AudioTrack initializeKeepSessionIdAudioTrack(int audioSessionId) {
        return new AudioTrack(3, 4000, 4, 2, 2, 0, audioSessionId);
    }

    private AudioProcessor[] getAvailableAudioProcessors() {
        return this.shouldConvertHighResIntPcmToFloat ? this.toFloatPcmAvailableAudioProcessors : this.toIntPcmAvailableAudioProcessors;
    }

    private static boolean isEncodingPcm(int encoding) {
        if (!(encoding == 3 || encoding == 2 || encoding == Integer.MIN_VALUE || encoding == 1073741824)) {
            if (encoding != 4) {
                return false;
            }
        }
        return true;
    }

    private static int getFramesPerEncodedSample(int encoding, ByteBuffer buffer) {
        if (encoding != 7) {
            if (encoding != 8) {
                if (encoding == 5) {
                    return Ac3Util.getAc3SyncframeAudioSampleCount();
                }
                if (encoding == 6) {
                    return Ac3Util.parseEAc3SyncframeAudioSampleCount(buffer);
                }
                if (encoding == 14) {
                    return Ac3Util.parseTrueHdSyncframeAudioSampleCount(buffer) * 8;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected audio encoding: ");
                stringBuilder.append(encoding);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
        return DtsUtil.parseDtsAudioSampleCount(buffer);
    }

    @TargetApi(21)
    private static int writeNonBlockingV21(AudioTrack audioTrack, ByteBuffer buffer, int size) {
        return audioTrack.write(buffer, size, 1);
    }

    @TargetApi(21)
    private int writeNonBlockingWithAvSyncV21(AudioTrack audioTrack, ByteBuffer buffer, int size, long presentationTimeUs) {
        int result;
        if (this.avSyncHeader == null) {
            this.avSyncHeader = ByteBuffer.allocate(16);
            this.avSyncHeader.order(ByteOrder.BIG_ENDIAN);
            this.avSyncHeader.putInt(1431633921);
        }
        if (this.bytesUntilNextAvSync == 0) {
            this.avSyncHeader.putInt(4, size);
            this.avSyncHeader.putLong(8, 1000 * presentationTimeUs);
            this.avSyncHeader.position(0);
            this.bytesUntilNextAvSync = size;
        }
        int avSyncHeaderBytesRemaining = this.avSyncHeader.remaining();
        if (avSyncHeaderBytesRemaining > 0) {
            result = audioTrack.write(this.avSyncHeader, avSyncHeaderBytesRemaining, 1);
            if (result < 0) {
                this.bytesUntilNextAvSync = 0;
                return result;
            } else if (result < avSyncHeaderBytesRemaining) {
                return 0;
            }
        }
        result = writeNonBlockingV21(audioTrack, buffer, size);
        if (result < 0) {
            this.bytesUntilNextAvSync = 0;
            return result;
        }
        this.bytesUntilNextAvSync -= result;
        return result;
    }

    @TargetApi(21)
    private static void setVolumeInternalV21(AudioTrack audioTrack, float volume) {
        audioTrack.setVolume(volume);
    }

    private static void setVolumeInternalV3(AudioTrack audioTrack, float volume) {
        audioTrack.setStereoVolume(volume, volume);
    }
}
