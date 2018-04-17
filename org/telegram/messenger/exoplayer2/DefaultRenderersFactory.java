package org.telegram.messenger.exoplayer2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import org.telegram.messenger.exoplayer2.audio.AudioProcessor;
import org.telegram.messenger.exoplayer2.audio.AudioRendererEventListener;
import org.telegram.messenger.exoplayer2.drm.DrmSessionManager;
import org.telegram.messenger.exoplayer2.drm.FrameworkMediaCrypto;
import org.telegram.messenger.exoplayer2.metadata.MetadataOutput;
import org.telegram.messenger.exoplayer2.metadata.MetadataRenderer;
import org.telegram.messenger.exoplayer2.text.TextOutput;
import org.telegram.messenger.exoplayer2.text.TextRenderer;
import org.telegram.messenger.exoplayer2.video.VideoRendererEventListener;

public class DefaultRenderersFactory implements RenderersFactory {
    public static final long DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS = 5000;
    public static final int EXTENSION_RENDERER_MODE_OFF = 0;
    public static final int EXTENSION_RENDERER_MODE_ON = 1;
    public static final int EXTENSION_RENDERER_MODE_PREFER = 2;
    protected static final int MAX_DROPPED_VIDEO_FRAME_COUNT_TO_NOTIFY = 50;
    private static final String TAG = "DefaultRenderersFactory";
    private final long allowedVideoJoiningTimeMs;
    private final Context context;
    private final DrmSessionManager<FrameworkMediaCrypto> drmSessionManager;
    private final int extensionRendererMode;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ExtensionRendererMode {
    }

    protected void buildAudioRenderers(android.content.Context r1, org.telegram.messenger.exoplayer2.drm.DrmSessionManager<org.telegram.messenger.exoplayer2.drm.FrameworkMediaCrypto> r2, org.telegram.messenger.exoplayer2.audio.AudioProcessor[] r3, android.os.Handler r4, org.telegram.messenger.exoplayer2.audio.AudioRendererEventListener r5, int r6, java.util.ArrayList<org.telegram.messenger.exoplayer2.Renderer> r7) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.DefaultRenderersFactory.buildAudioRenderers(android.content.Context, org.telegram.messenger.exoplayer2.drm.DrmSessionManager, org.telegram.messenger.exoplayer2.audio.AudioProcessor[], android.os.Handler, org.telegram.messenger.exoplayer2.audio.AudioRendererEventListener, int, java.util.ArrayList):void
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
        r9 = r19;
        r10 = r20;
        r11 = new org.telegram.messenger.exoplayer2.audio.MediaCodecAudioRenderer;
        r2 = org.telegram.messenger.exoplayer2.mediacodec.MediaCodecSelector.DEFAULT;
        r7 = org.telegram.messenger.exoplayer2.audio.AudioCapabilities.getCapabilities(r14);
        r4 = 1;
        r1 = r11;
        r3 = r15;
        r5 = r17;
        r6 = r18;
        r8 = r16;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);
        r10.add(r11);
        if (r9 != 0) goto L_0x001e;
    L_0x001d:
        return;
    L_0x001e:
        r1 = r20.size();
        r2 = 2;
        if (r9 != r2) goto L_0x0027;
    L_0x0025:
        r1 = r1 + -1;
    L_0x0027:
        r3 = 0;
        r4 = 3;
        r5 = 1;
        r6 = "org.telegram.messenger.exoplayer2.ext.opus.LibopusAudioRenderer";	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r6 = java.lang.Class.forName(r6);	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r7 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r8 = android.os.Handler.class;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r7[r3] = r8;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r8 = org.telegram.messenger.exoplayer2.audio.AudioRendererEventListener.class;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r7[r5] = r8;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r8 = org.telegram.messenger.exoplayer2.audio.AudioProcessor[].class;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r7[r2] = r8;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r7 = r6.getConstructor(r7);	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r8 = new java.lang.Object[r4];	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r8[r3] = r17;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r8[r5] = r18;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r8[r2] = r16;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r8 = r7.newInstance(r8);	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r8 = (org.telegram.messenger.exoplayer2.Renderer) r8;	 Catch:{ ClassNotFoundException -> 0x006a, Exception -> 0x0061 }
        r11 = r1 + 1;
        r10.add(r1, r8);	 Catch:{ ClassNotFoundException -> 0x005f, Exception -> 0x005d }
        r1 = "DefaultRenderersFactory";	 Catch:{ ClassNotFoundException -> 0x005f, Exception -> 0x005d }
        r12 = "Loaded LibopusAudioRenderer.";	 Catch:{ ClassNotFoundException -> 0x005f, Exception -> 0x005d }
        android.util.Log.i(r1, r12);	 Catch:{ ClassNotFoundException -> 0x005f, Exception -> 0x005d }
        goto L_0x006c;
    L_0x005d:
        r0 = move-exception;
        goto L_0x0063;
    L_0x005f:
        r0 = move-exception;
        goto L_0x006c;
    L_0x0061:
        r0 = move-exception;
        r11 = r1;
        r1 = r0;
        r2 = new java.lang.RuntimeException;
        r2.<init>(r1);
        throw r2;
    L_0x006a:
        r0 = move-exception;
        r11 = r1;
        r1 = "org.telegram.messenger.exoplayer2.ext.flac.LibflacAudioRenderer";	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r6 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r7 = android.os.Handler.class;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r6[r3] = r7;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r7 = org.telegram.messenger.exoplayer2.audio.AudioRendererEventListener.class;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r6[r5] = r7;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r7 = org.telegram.messenger.exoplayer2.audio.AudioProcessor[].class;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r6[r2] = r7;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r6 = r1.getConstructor(r6);	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r7 = new java.lang.Object[r4];	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r7[r3] = r17;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r7[r5] = r18;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r7[r2] = r16;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r7 = r6.newInstance(r7);	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r7 = (org.telegram.messenger.exoplayer2.Renderer) r7;	 Catch:{ ClassNotFoundException -> 0x00ae, Exception -> 0x00a6 }
        r8 = r11 + 1;
        r10.add(r11, r7);	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x00a0 }
        r11 = "DefaultRenderersFactory";	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x00a0 }
        r12 = "Loaded LibflacAudioRenderer.";	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x00a0 }
        android.util.Log.i(r11, r12);	 Catch:{ ClassNotFoundException -> 0x00a4, Exception -> 0x00a0 }
        goto L_0x00b0;
    L_0x00a0:
        r0 = move-exception;
        r1 = r0;
        r11 = r8;
        goto L_0x00a8;
    L_0x00a4:
        r0 = move-exception;
        goto L_0x00b0;
    L_0x00a6:
        r0 = move-exception;
        r1 = r0;
        r2 = new java.lang.RuntimeException;
        r2.<init>(r1);
        throw r2;
    L_0x00ae:
        r0 = move-exception;
        r8 = r11;
        r1 = "org.telegram.messenger.exoplayer2.ext.ffmpeg.FfmpegAudioRenderer";	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r1 = java.lang.Class.forName(r1);	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r6 = new java.lang.Class[r4];	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r7 = android.os.Handler.class;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r6[r3] = r7;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r7 = org.telegram.messenger.exoplayer2.audio.AudioRendererEventListener.class;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r6[r5] = r7;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r7 = org.telegram.messenger.exoplayer2.audio.AudioProcessor[].class;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r6[r2] = r7;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r6 = r1.getConstructor(r6);	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r4 = new java.lang.Object[r4];	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r4[r3] = r17;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r4[r5] = r18;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r4[r2] = r16;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r2 = r6.newInstance(r4);	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r2 = (org.telegram.messenger.exoplayer2.Renderer) r2;	 Catch:{ ClassNotFoundException -> 0x00f2, Exception -> 0x00ea }
        r3 = r8 + 1;
        r10.add(r8, r2);	 Catch:{ ClassNotFoundException -> 0x00e8, Exception -> 0x00e4 }
        r4 = "DefaultRenderersFactory";	 Catch:{ ClassNotFoundException -> 0x00e8, Exception -> 0x00e4 }
        r5 = "Loaded FfmpegAudioRenderer.";	 Catch:{ ClassNotFoundException -> 0x00e8, Exception -> 0x00e4 }
        android.util.Log.i(r4, r5);	 Catch:{ ClassNotFoundException -> 0x00e8, Exception -> 0x00e4 }
        goto L_0x00f4;
    L_0x00e4:
        r0 = move-exception;
        r1 = r0;
        r8 = r3;
        goto L_0x00ec;
    L_0x00e8:
        r0 = move-exception;
        goto L_0x00f4;
    L_0x00ea:
        r0 = move-exception;
        r1 = r0;
        r2 = new java.lang.RuntimeException;
        r2.<init>(r1);
        throw r2;
    L_0x00f2:
        r0 = move-exception;
        r3 = r8;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.DefaultRenderersFactory.buildAudioRenderers(android.content.Context, org.telegram.messenger.exoplayer2.drm.DrmSessionManager, org.telegram.messenger.exoplayer2.audio.AudioProcessor[], android.os.Handler, org.telegram.messenger.exoplayer2.audio.AudioRendererEventListener, int, java.util.ArrayList):void");
    }

    protected void buildVideoRenderers(android.content.Context r1, org.telegram.messenger.exoplayer2.drm.DrmSessionManager<org.telegram.messenger.exoplayer2.drm.FrameworkMediaCrypto> r2, long r3, android.os.Handler r5, org.telegram.messenger.exoplayer2.video.VideoRendererEventListener r6, int r7, java.util.ArrayList<org.telegram.messenger.exoplayer2.Renderer> r8) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.DefaultRenderersFactory.buildVideoRenderers(android.content.Context, org.telegram.messenger.exoplayer2.drm.DrmSessionManager, long, android.os.Handler, org.telegram.messenger.exoplayer2.video.VideoRendererEventListener, int, java.util.ArrayList):void
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
        r1 = r21;
        r2 = r22;
        r13 = new org.telegram.messenger.exoplayer2.video.MediaCodecVideoRenderer;
        r5 = org.telegram.messenger.exoplayer2.mediacodec.MediaCodecSelector.DEFAULT;
        r9 = 0;
        r12 = 50;
        r3 = r13;
        r4 = r15;
        r6 = r17;
        r8 = r16;
        r10 = r19;
        r11 = r20;
        r3.<init>(r4, r5, r6, r8, r9, r10, r11, r12);
        r2.add(r13);
        if (r1 != 0) goto L_0x001e;
    L_0x001d:
        return;
    L_0x001e:
        r3 = r22.size();
        r4 = 2;
        if (r1 != r4) goto L_0x0027;
    L_0x0025:
        r3 = r3 + -1;
    L_0x0027:
        r5 = "org.telegram.messenger.exoplayer2.ext.vp9.LibvpxVideoRenderer";	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r5 = java.lang.Class.forName(r5);	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r6 = 5;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r7 = new java.lang.Class[r6];	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r8 = java.lang.Boolean.TYPE;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r9 = 0;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r7[r9] = r8;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r8 = java.lang.Long.TYPE;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r10 = 1;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r7[r10] = r8;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r8 = android.os.Handler.class;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r7[r4] = r8;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r8 = org.telegram.messenger.exoplayer2.video.VideoRendererEventListener.class;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r11 = 3;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r7[r11] = r8;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r8 = java.lang.Integer.TYPE;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r12 = 4;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r7[r12] = r8;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r7 = r5.getConstructor(r7);	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r6 = new java.lang.Object[r6];	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r8 = java.lang.Boolean.valueOf(r10);	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r6[r9] = r8;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r8 = java.lang.Long.valueOf(r17);	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r6[r10] = r8;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r6[r4] = r19;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r6[r11] = r20;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r9 = 50;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r9 = java.lang.Integer.valueOf(r9);	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r6[r12] = r9;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r6 = r7.newInstance(r6);	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r6 = (org.telegram.messenger.exoplayer2.Renderer) r6;	 Catch:{ ClassNotFoundException -> 0x0086, Exception -> 0x007d }
        r9 = r3 + 1;
        r2.add(r3, r6);	 Catch:{ ClassNotFoundException -> 0x007b, Exception -> 0x0079 }
        r3 = "DefaultRenderersFactory";	 Catch:{ ClassNotFoundException -> 0x007b, Exception -> 0x0079 }
        r10 = "Loaded LibvpxVideoRenderer.";	 Catch:{ ClassNotFoundException -> 0x007b, Exception -> 0x0079 }
        android.util.Log.i(r3, r10);	 Catch:{ ClassNotFoundException -> 0x007b, Exception -> 0x0079 }
        goto L_0x0088;
    L_0x0079:
        r0 = move-exception;
        goto L_0x007f;
    L_0x007b:
        r0 = move-exception;
        goto L_0x0088;
    L_0x007d:
        r0 = move-exception;
        r9 = r3;
        r3 = r0;
        r5 = new java.lang.RuntimeException;
        r5.<init>(r3);
        throw r5;
    L_0x0086:
        r0 = move-exception;
        r9 = r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.DefaultRenderersFactory.buildVideoRenderers(android.content.Context, org.telegram.messenger.exoplayer2.drm.DrmSessionManager, long, android.os.Handler, org.telegram.messenger.exoplayer2.video.VideoRendererEventListener, int, java.util.ArrayList):void");
    }

    public DefaultRenderersFactory(Context context) {
        this(context, null);
    }

    public DefaultRenderersFactory(Context context, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager) {
        this(context, drmSessionManager, 0);
    }

    public DefaultRenderersFactory(Context context, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int extensionRendererMode) {
        this(context, drmSessionManager, extensionRendererMode, DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
    }

    public DefaultRenderersFactory(Context context, DrmSessionManager<FrameworkMediaCrypto> drmSessionManager, int extensionRendererMode, long allowedVideoJoiningTimeMs) {
        this.context = context;
        this.drmSessionManager = drmSessionManager;
        this.extensionRendererMode = extensionRendererMode;
        this.allowedVideoJoiningTimeMs = allowedVideoJoiningTimeMs;
    }

    public Renderer[] createRenderers(Handler eventHandler, VideoRendererEventListener videoRendererEventListener, AudioRendererEventListener audioRendererEventListener, TextOutput textRendererOutput, MetadataOutput metadataRendererOutput) {
        ArrayList<Renderer> renderersList = new ArrayList();
        buildVideoRenderers(this.context, this.drmSessionManager, this.allowedVideoJoiningTimeMs, eventHandler, videoRendererEventListener, this.extensionRendererMode, renderersList);
        buildAudioRenderers(this.context, this.drmSessionManager, buildAudioProcessors(), eventHandler, audioRendererEventListener, this.extensionRendererMode, renderersList);
        ArrayList<Renderer> arrayList = renderersList;
        buildTextRenderers(this.context, textRendererOutput, eventHandler.getLooper(), this.extensionRendererMode, arrayList);
        buildMetadataRenderers(this.context, metadataRendererOutput, eventHandler.getLooper(), this.extensionRendererMode, arrayList);
        buildMiscellaneousRenderers(this.context, eventHandler, this.extensionRendererMode, renderersList);
        return (Renderer[]) renderersList.toArray(new Renderer[renderersList.size()]);
    }

    protected void buildTextRenderers(Context context, TextOutput output, Looper outputLooper, int extensionRendererMode, ArrayList<Renderer> out) {
        out.add(new TextRenderer(output, outputLooper));
    }

    protected void buildMetadataRenderers(Context context, MetadataOutput output, Looper outputLooper, int extensionRendererMode, ArrayList<Renderer> out) {
        out.add(new MetadataRenderer(output, outputLooper));
    }

    protected void buildMiscellaneousRenderers(Context context, Handler eventHandler, int extensionRendererMode, ArrayList<Renderer> arrayList) {
    }

    protected AudioProcessor[] buildAudioProcessors() {
        return new AudioProcessor[0];
    }
}
