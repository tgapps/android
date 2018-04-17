package org.telegram.messenger.exoplayer2.text;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.exoplayer2.BaseRenderer;
import org.telegram.messenger.exoplayer2.ExoPlaybackException;
import org.telegram.messenger.exoplayer2.Format;
import org.telegram.messenger.exoplayer2.FormatHolder;
import org.telegram.messenger.exoplayer2.util.Assertions;
import org.telegram.messenger.exoplayer2.util.MimeTypes;

public final class TextRenderer extends BaseRenderer implements Callback {
    private static final int MSG_UPDATE_OUTPUT = 0;
    private static final int REPLACEMENT_STATE_NONE = 0;
    private static final int REPLACEMENT_STATE_SIGNAL_END_OF_STREAM = 1;
    private static final int REPLACEMENT_STATE_WAIT_END_OF_STREAM = 2;
    private SubtitleDecoder decoder;
    private final SubtitleDecoderFactory decoderFactory;
    private int decoderReplacementState;
    private final FormatHolder formatHolder;
    private boolean inputStreamEnded;
    private SubtitleInputBuffer nextInputBuffer;
    private SubtitleOutputBuffer nextSubtitle;
    private int nextSubtitleEventIndex;
    private final TextOutput output;
    private final Handler outputHandler;
    private boolean outputStreamEnded;
    private Format streamFormat;
    private SubtitleOutputBuffer subtitle;

    @Retention(RetentionPolicy.SOURCE)
    private @interface ReplacementState {
    }

    @Deprecated
    public interface Output extends TextOutput {
    }

    public void render(long r1, long r3) throws org.telegram.messenger.exoplayer2.ExoPlaybackException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.text.TextRenderer.render(long, long):void
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
        r0 = this;
        r0 = r9.outputStreamEnded;
        if (r0 == 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = r9.nextSubtitle;
        if (r0 != 0) goto L_0x0023;
    L_0x0009:
        r0 = r9.decoder;
        r0.setPositionUs(r10);
        r0 = r9.decoder;	 Catch:{ SubtitleDecoderException -> 0x0019 }
        r0 = r0.dequeueOutputBuffer();	 Catch:{ SubtitleDecoderException -> 0x0019 }
        r0 = (org.telegram.messenger.exoplayer2.text.SubtitleOutputBuffer) r0;	 Catch:{ SubtitleDecoderException -> 0x0019 }
        r9.nextSubtitle = r0;	 Catch:{ SubtitleDecoderException -> 0x0019 }
        goto L_0x0023;
    L_0x0019:
        r0 = move-exception;
        r1 = r9.getIndex();
        r1 = org.telegram.messenger.exoplayer2.ExoPlaybackException.createForRenderer(r0, r1);
        throw r1;
    L_0x0023:
        r0 = r9.getState();
        r1 = 2;
        if (r0 == r1) goto L_0x002b;
    L_0x002a:
        return;
    L_0x002b:
        r0 = 0;
        r2 = r9.subtitle;
        r3 = 1;
        if (r2 == 0) goto L_0x0044;
    L_0x0031:
        r4 = r9.getNextEventTime();
    L_0x0035:
        r2 = (r4 > r10 ? 1 : (r4 == r10 ? 0 : -1));
        if (r2 > 0) goto L_0x0044;
    L_0x0039:
        r2 = r9.nextSubtitleEventIndex;
        r2 = r2 + r3;
        r9.nextSubtitleEventIndex = r2;
        r4 = r9.getNextEventTime();
        r0 = 1;
        goto L_0x0035;
    L_0x0044:
        r2 = r9.nextSubtitle;
        r4 = 0;
        if (r2 == 0) goto L_0x008e;
    L_0x0049:
        r2 = r9.nextSubtitle;
        r2 = r2.isEndOfStream();
        if (r2 == 0) goto L_0x006e;
    L_0x0051:
        if (r0 != 0) goto L_0x008e;
    L_0x0053:
        r5 = r9.getNextEventTime();
        r7 = 9223372036854775807; // 0x7fffffffffffffff float:NaN double:NaN;
        r2 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r2 != 0) goto L_0x008e;
    L_0x0060:
        r2 = r9.decoderReplacementState;
        if (r2 != r1) goto L_0x0068;
    L_0x0064:
        r9.replaceDecoder();
        goto L_0x008e;
    L_0x0068:
        r9.releaseBuffers();
        r9.outputStreamEnded = r3;
        goto L_0x008e;
    L_0x006e:
        r2 = r9.nextSubtitle;
        r5 = r2.timeUs;
        r2 = (r5 > r10 ? 1 : (r5 == r10 ? 0 : -1));
        if (r2 > 0) goto L_0x008e;
    L_0x0076:
        r2 = r9.subtitle;
        if (r2 == 0) goto L_0x007f;
    L_0x007a:
        r2 = r9.subtitle;
        r2.release();
    L_0x007f:
        r2 = r9.nextSubtitle;
        r9.subtitle = r2;
        r9.nextSubtitle = r4;
        r2 = r9.subtitle;
        r2 = r2.getNextEventTimeIndex(r10);
        r9.nextSubtitleEventIndex = r2;
        r0 = 1;
    L_0x008e:
        if (r0 == 0) goto L_0x0099;
    L_0x0090:
        r2 = r9.subtitle;
        r2 = r2.getCues(r10);
        r9.updateOutput(r2);
    L_0x0099:
        r2 = r9.decoderReplacementState;
        if (r2 != r1) goto L_0x009e;
    L_0x009d:
        return;
    L_0x009e:
        r2 = r9.inputStreamEnded;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        if (r2 != 0) goto L_0x0100;	 Catch:{ SubtitleDecoderException -> 0x0102 }
    L_0x00a2:
        r2 = r9.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        if (r2 != 0) goto L_0x00b5;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2 = r9.decoder;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2 = r2.dequeueInputBuffer();	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2 = (org.telegram.messenger.exoplayer2.text.SubtitleInputBuffer) r2;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r9.nextInputBuffer = r2;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2 = r9.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        if (r2 != 0) goto L_0x00b5;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        return;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2 = r9.decoderReplacementState;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        if (r2 != r3) goto L_0x00cb;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2 = r9.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r3 = 4;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2.setFlags(r3);	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2 = r9.decoder;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r3 = r9.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2.queueInputBuffer(r3);	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r9.nextInputBuffer = r4;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r9.decoderReplacementState = r1;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        return;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2 = r9.formatHolder;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5 = r9.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r6 = 0;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r2 = r9.readSource(r2, r5, r6);	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5 = -4;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        if (r2 != r5) goto L_0x00fb;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5 = r9.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5 = r5.isEndOfStream();	 Catch:{ SubtitleDecoderException -> 0x0102 }
        if (r5 == 0) goto L_0x00e2;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r9.inputStreamEnded = r3;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        goto L_0x00f1;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5 = r9.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r6 = r9.formatHolder;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r6 = r6.format;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r6 = r6.subsampleOffsetUs;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5.subsampleOffsetUs = r6;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5 = r9.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5.flip();	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5 = r9.decoder;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r6 = r9.nextInputBuffer;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r5.queueInputBuffer(r6);	 Catch:{ SubtitleDecoderException -> 0x0102 }
        r9.nextInputBuffer = r4;	 Catch:{ SubtitleDecoderException -> 0x0102 }
        goto L_0x00ff;
        r5 = -3;
        if (r2 != r5) goto L_0x00ff;
        return;
        goto L_0x009e;
        return;
    L_0x0102:
        r1 = move-exception;
        r2 = r9.getIndex();
        r2 = org.telegram.messenger.exoplayer2.ExoPlaybackException.createForRenderer(r1, r2);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.text.TextRenderer.render(long, long):void");
    }

    public TextRenderer(TextOutput output, Looper outputLooper) {
        this(output, outputLooper, SubtitleDecoderFactory.DEFAULT);
    }

    public TextRenderer(TextOutput output, Looper outputLooper, SubtitleDecoderFactory decoderFactory) {
        super(3);
        this.output = (TextOutput) Assertions.checkNotNull(output);
        this.outputHandler = outputLooper == null ? null : new Handler(outputLooper, this);
        this.decoderFactory = decoderFactory;
        this.formatHolder = new FormatHolder();
    }

    public int supportsFormat(Format format) {
        if (this.decoderFactory.supportsFormat(format)) {
            return BaseRenderer.supportsFormatDrm(null, format.drmInitData) ? 4 : 2;
        } else if (MimeTypes.isText(format.sampleMimeType)) {
            return 1;
        } else {
            return 0;
        }
    }

    protected void onStreamChanged(Format[] formats, long offsetUs) throws ExoPlaybackException {
        this.streamFormat = formats[0];
        if (this.decoder != null) {
            this.decoderReplacementState = 1;
        } else {
            this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
        }
    }

    protected void onPositionReset(long positionUs, boolean joining) {
        clearOutput();
        this.inputStreamEnded = false;
        this.outputStreamEnded = false;
        if (this.decoderReplacementState != 0) {
            replaceDecoder();
            return;
        }
        releaseBuffers();
        this.decoder.flush();
    }

    protected void onDisabled() {
        this.streamFormat = null;
        clearOutput();
        releaseDecoder();
    }

    public boolean isEnded() {
        return this.outputStreamEnded;
    }

    public boolean isReady() {
        return true;
    }

    private void releaseBuffers() {
        this.nextInputBuffer = null;
        this.nextSubtitleEventIndex = -1;
        if (this.subtitle != null) {
            this.subtitle.release();
            this.subtitle = null;
        }
        if (this.nextSubtitle != null) {
            this.nextSubtitle.release();
            this.nextSubtitle = null;
        }
    }

    private void releaseDecoder() {
        releaseBuffers();
        this.decoder.release();
        this.decoder = null;
        this.decoderReplacementState = 0;
    }

    private void replaceDecoder() {
        releaseDecoder();
        this.decoder = this.decoderFactory.createDecoder(this.streamFormat);
    }

    private long getNextEventTime() {
        if (this.nextSubtitleEventIndex != -1) {
            if (this.nextSubtitleEventIndex < this.subtitle.getEventTimeCount()) {
                return this.subtitle.getEventTime(this.nextSubtitleEventIndex);
            }
        }
        return Long.MAX_VALUE;
    }

    private void updateOutput(List<Cue> cues) {
        if (this.outputHandler != null) {
            this.outputHandler.obtainMessage(0, cues).sendToTarget();
        } else {
            invokeUpdateOutputInternal(cues);
        }
    }

    private void clearOutput() {
        updateOutput(Collections.emptyList());
    }

    public boolean handleMessage(Message msg) {
        if (msg.what != 0) {
            throw new IllegalStateException();
        }
        invokeUpdateOutputInternal((List) msg.obj);
        return true;
    }

    private void invokeUpdateOutputInternal(List<Cue> cues) {
        this.output.onCues(cues);
    }
}
