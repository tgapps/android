package org.telegram.messenger.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.NotProvisionedException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.DefaultLoadControl;
import org.telegram.messenger.exoplayer2.drm.DefaultDrmSessionManager.EventListener;
import org.telegram.messenger.exoplayer2.drm.DrmSession.DrmSessionException;
import org.telegram.messenger.exoplayer2.drm.ExoMediaDrm.DefaultKeyRequest;
import org.telegram.messenger.exoplayer2.drm.ExoMediaDrm.KeyRequest;

@TargetApi(18)
class DefaultDrmSession<T extends ExoMediaCrypto> implements DrmSession<T> {
    private static final int MAX_LICENSE_DURATION_TO_RENEW = 60;
    private static final int MSG_KEYS = 1;
    private static final int MSG_PROVISION = 0;
    private static final String TAG = "DefaultDrmSession";
    final MediaDrmCallback callback;
    private final Handler eventHandler;
    private final EventListener eventListener;
    private final byte[] initData;
    private final int initialDrmRequestRetryCount;
    private DrmSessionException lastException;
    private T mediaCrypto;
    private final ExoMediaDrm<T> mediaDrm;
    private final String mimeType;
    private final int mode;
    private byte[] offlineLicenseKeySetId;
    private int openCount;
    private final HashMap<String, String> optionalKeyRequestParameters;
    private PostRequestHandler postRequestHandler;
    final PostResponseHandler postResponseHandler;
    private final ProvisioningManager<T> provisioningManager;
    private HandlerThread requestHandlerThread;
    private byte[] sessionId;
    private int state = 2;
    final UUID uuid;

    @SuppressLint({"HandlerLeak"})
    private class PostRequestHandler extends Handler {
        public void handleMessage(android.os.Message r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.PostRequestHandler.handleMessage(android.os.Message):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = r4.what;	 Catch:{ Exception -> 0x0030 }
            switch(r0) {
                case 0: goto L_0x0019;
                case 1: goto L_0x0008;
                default: goto L_0x0005;
            };	 Catch:{ Exception -> 0x0030 }
        L_0x0005:
            r0 = new java.lang.RuntimeException;	 Catch:{ Exception -> 0x0030 }
            goto L_0x002c;	 Catch:{ Exception -> 0x0030 }
        L_0x0008:
            r0 = org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.this;	 Catch:{ Exception -> 0x0030 }
            r0 = r0.callback;	 Catch:{ Exception -> 0x0030 }
            r1 = org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.this;	 Catch:{ Exception -> 0x0030 }
            r1 = r1.uuid;	 Catch:{ Exception -> 0x0030 }
            r2 = r4.obj;	 Catch:{ Exception -> 0x0030 }
            r2 = (org.telegram.messenger.exoplayer2.drm.ExoMediaDrm.KeyRequest) r2;	 Catch:{ Exception -> 0x0030 }
            r0 = r0.executeKeyRequest(r1, r2);	 Catch:{ Exception -> 0x0030 }
            goto L_0x002a;	 Catch:{ Exception -> 0x0030 }
        L_0x0019:
            r0 = org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.this;	 Catch:{ Exception -> 0x0030 }
            r0 = r0.callback;	 Catch:{ Exception -> 0x0030 }
            r1 = org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.this;	 Catch:{ Exception -> 0x0030 }
            r1 = r1.uuid;	 Catch:{ Exception -> 0x0030 }
            r2 = r4.obj;	 Catch:{ Exception -> 0x0030 }
            r2 = (org.telegram.messenger.exoplayer2.drm.ExoMediaDrm.ProvisionRequest) r2;	 Catch:{ Exception -> 0x0030 }
            r0 = r0.executeProvisionRequest(r1, r2);	 Catch:{ Exception -> 0x0030 }
            goto L_0x0039;	 Catch:{ Exception -> 0x0030 }
        L_0x002c:
            r0.<init>();	 Catch:{ Exception -> 0x0030 }
            throw r0;	 Catch:{ Exception -> 0x0030 }
        L_0x0030:
            r0 = move-exception;
            r1 = r3.maybeRetryRequest(r4);
            if (r1 == 0) goto L_0x0038;
            return;
            r1 = org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.this;
            r1 = r1.postResponseHandler;
            r2 = r4.what;
            r1 = r1.obtainMessage(r2, r0);
            r1.sendToTarget();
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.PostRequestHandler.handleMessage(android.os.Message):void");
        }

        public PostRequestHandler(Looper backgroundLooper) {
            super(backgroundLooper);
        }

        Message obtainMessage(int what, Object object, boolean allowRetry) {
            return obtainMessage(what, allowRetry, 0, object);
        }

        private boolean maybeRetryRequest(Message originalMsg) {
            if (!(originalMsg.arg1 == 1)) {
                return false;
            }
            int errorCount = originalMsg.arg2 + 1;
            if (errorCount > DefaultDrmSession.this.initialDrmRequestRetryCount) {
                return false;
            }
            Message retryMsg = Message.obtain(originalMsg);
            retryMsg.arg2 = errorCount;
            sendMessageDelayed(retryMsg, getRetryDelayMillis(errorCount));
            return true;
        }

        private long getRetryDelayMillis(int errorCount) {
            return (long) Math.min((errorCount - 1) * 1000, DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS);
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class PostResponseHandler extends Handler {
        public PostResponseHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DefaultDrmSession.this.onProvisionResponse(msg.obj);
                    return;
                case 1:
                    DefaultDrmSession.this.onKeyResponse(msg.obj);
                    return;
                default:
                    return;
            }
        }
    }

    public interface ProvisioningManager<T extends ExoMediaCrypto> {
        void onProvisionCompleted();

        void onProvisionError(Exception exception);

        void provisionRequired(DefaultDrmSession<T> defaultDrmSession);
    }

    private void doLicense(boolean r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.doLicense(boolean):void
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
        r0 = r7.mode;
        r1 = 2;
        switch(r0) {
            case 0: goto L_0x0026;
            case 1: goto L_0x0026;
            case 2: goto L_0x0014;
            case 3: goto L_0x0008;
            default: goto L_0x0006;
        };
    L_0x0006:
        goto L_0x0086;
    L_0x0008:
        r0 = r7.restoreKeys();
        if (r0 == 0) goto L_0x0086;
    L_0x000e:
        r0 = 3;
        r7.postKeyRequest(r0, r8);
        goto L_0x0086;
    L_0x0014:
        r0 = r7.offlineLicenseKeySetId;
        if (r0 != 0) goto L_0x001c;
    L_0x0018:
        r7.postKeyRequest(r1, r8);
        goto L_0x0086;
    L_0x001c:
        r0 = r7.restoreKeys();
        if (r0 == 0) goto L_0x0086;
    L_0x0022:
        r7.postKeyRequest(r1, r8);
        goto L_0x0086;
    L_0x0026:
        r0 = r7.offlineLicenseKeySetId;
        if (r0 != 0) goto L_0x002f;
    L_0x002a:
        r0 = 1;
        r7.postKeyRequest(r0, r8);
        goto L_0x0086;
    L_0x002f:
        r0 = r7.state;
        r2 = 4;
        if (r0 == r2) goto L_0x003a;
    L_0x0034:
        r0 = r7.restoreKeys();
        if (r0 == 0) goto L_0x0086;
    L_0x003a:
        r3 = r7.getLicenseDurationRemainingSec();
        r0 = r7.mode;
        if (r0 != 0) goto L_0x0062;
    L_0x0042:
        r5 = 60;
        r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r0 > 0) goto L_0x0062;
    L_0x0048:
        r0 = "DefaultDrmSession";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r5 = "Offline license has expired or will expire soon. Remaining seconds: ";
        r2.append(r5);
        r2.append(r3);
        r2 = r2.toString();
        android.util.Log.d(r0, r2);
        r7.postKeyRequest(r1, r8);
        goto L_0x0085;
    L_0x0062:
        r0 = 0;
        r5 = (r3 > r0 ? 1 : (r3 == r0 ? 0 : -1));
        if (r5 > 0) goto L_0x0071;
        r0 = new org.telegram.messenger.exoplayer2.drm.KeysExpiredException;
        r0.<init>();
        r7.onError(r0);
        goto L_0x0085;
        r7.state = r2;
        r0 = r7.eventHandler;
        if (r0 == 0) goto L_0x0085;
        r0 = r7.eventListener;
        if (r0 == 0) goto L_0x0085;
        r0 = r7.eventHandler;
        r1 = new org.telegram.messenger.exoplayer2.drm.DefaultDrmSession$1;
        r1.<init>();
        r0.post(r1);
    L_0x0086:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.doLicense(boolean):void");
    }

    private boolean openInternal(boolean r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.openInternal(boolean):boolean
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
        r0 = r3.isOpen();
        r1 = 1;
        if (r0 == 0) goto L_0x0008;
    L_0x0007:
        return r1;
    L_0x0008:
        r0 = r3.mediaDrm;	 Catch:{ NotProvisionedException -> 0x0023, Exception -> 0x001e }
        r0 = r0.openSession();	 Catch:{ NotProvisionedException -> 0x0023, Exception -> 0x001e }
        r3.sessionId = r0;	 Catch:{ NotProvisionedException -> 0x0023, Exception -> 0x001e }
        r0 = r3.mediaDrm;	 Catch:{ NotProvisionedException -> 0x0023, Exception -> 0x001e }
        r2 = r3.sessionId;	 Catch:{ NotProvisionedException -> 0x0023, Exception -> 0x001e }
        r0 = r0.createMediaCrypto(r2);	 Catch:{ NotProvisionedException -> 0x0023, Exception -> 0x001e }
        r3.mediaCrypto = r0;	 Catch:{ NotProvisionedException -> 0x0023, Exception -> 0x001e }
        r0 = 3;	 Catch:{ NotProvisionedException -> 0x0023, Exception -> 0x001e }
        r3.state = r0;	 Catch:{ NotProvisionedException -> 0x0023, Exception -> 0x001e }
        return r1;
    L_0x001e:
        r0 = move-exception;
        r3.onError(r0);
        goto L_0x0030;
    L_0x0023:
        r0 = move-exception;
        if (r4 == 0) goto L_0x002c;
    L_0x0026:
        r1 = r3.provisioningManager;
        r1.provisionRequired(r3);
        goto L_0x002f;
    L_0x002c:
        r3.onError(r0);
    L_0x0030:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.exoplayer2.drm.DefaultDrmSession.openInternal(boolean):boolean");
    }

    public DefaultDrmSession(UUID uuid, ExoMediaDrm<T> mediaDrm, ProvisioningManager<T> provisioningManager, byte[] initData, String mimeType, int mode, byte[] offlineLicenseKeySetId, HashMap<String, String> optionalKeyRequestParameters, MediaDrmCallback callback, Looper playbackLooper, Handler eventHandler, EventListener eventListener, int initialDrmRequestRetryCount) {
        this.uuid = uuid;
        this.provisioningManager = provisioningManager;
        this.mediaDrm = mediaDrm;
        this.mode = mode;
        this.offlineLicenseKeySetId = offlineLicenseKeySetId;
        this.optionalKeyRequestParameters = optionalKeyRequestParameters;
        this.callback = callback;
        this.initialDrmRequestRetryCount = initialDrmRequestRetryCount;
        this.eventHandler = eventHandler;
        this.eventListener = eventListener;
        this.postResponseHandler = new PostResponseHandler(playbackLooper);
        this.requestHandlerThread = new HandlerThread("DrmRequestHandler");
        this.requestHandlerThread.start();
        this.postRequestHandler = new PostRequestHandler(this.requestHandlerThread.getLooper());
        if (offlineLicenseKeySetId == null) {
            this.initData = initData;
            this.mimeType = mimeType;
            return;
        }
        this.initData = null;
        this.mimeType = null;
    }

    public void acquire() {
        int i = this.openCount + 1;
        this.openCount = i;
        if (i == 1 && this.state != 1 && openInternal(true)) {
            doLicense(true);
        }
    }

    public boolean release() {
        int i = this.openCount - 1;
        this.openCount = i;
        if (i != 0) {
            return false;
        }
        this.state = 0;
        this.postResponseHandler.removeCallbacksAndMessages(null);
        this.postRequestHandler.removeCallbacksAndMessages(null);
        this.postRequestHandler = null;
        this.requestHandlerThread.quit();
        this.requestHandlerThread = null;
        this.mediaCrypto = null;
        this.lastException = null;
        if (this.sessionId != null) {
            this.mediaDrm.closeSession(this.sessionId);
            this.sessionId = null;
        }
        return true;
    }

    public boolean hasInitData(byte[] initData) {
        return Arrays.equals(this.initData, initData);
    }

    public boolean hasSessionId(byte[] sessionId) {
        return Arrays.equals(this.sessionId, sessionId);
    }

    public void provision() {
        this.postRequestHandler.obtainMessage(0, this.mediaDrm.getProvisionRequest(), true).sendToTarget();
    }

    public void onProvisionCompleted() {
        if (openInternal(false)) {
            doLicense(true);
        }
    }

    public void onProvisionError(Exception error) {
        onError(error);
    }

    public final int getState() {
        return this.state;
    }

    public final DrmSessionException getError() {
        return this.state == 1 ? this.lastException : null;
    }

    public final T getMediaCrypto() {
        return this.mediaCrypto;
    }

    public Map<String, String> queryKeyStatus() {
        return this.sessionId == null ? null : this.mediaDrm.queryKeyStatus(this.sessionId);
    }

    public byte[] getOfflineLicenseKeySetId() {
        return this.offlineLicenseKeySetId;
    }

    private void onProvisionResponse(Object response) {
        if (this.state != 2 && !isOpen()) {
            return;
        }
        if (response instanceof Exception) {
            this.provisioningManager.onProvisionError((Exception) response);
            return;
        }
        try {
            this.mediaDrm.provideProvisionResponse((byte[]) response);
            this.provisioningManager.onProvisionCompleted();
        } catch (Exception e) {
            this.provisioningManager.onProvisionError(e);
        }
    }

    private boolean restoreKeys() {
        try {
            this.mediaDrm.restoreKeys(this.sessionId, this.offlineLicenseKeySetId);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Error trying to restore Widevine keys.", e);
            onError(e);
            return false;
        }
    }

    private long getLicenseDurationRemainingSec() {
        if (!C.WIDEVINE_UUID.equals(this.uuid)) {
            return Long.MAX_VALUE;
        }
        Pair<Long, Long> pair = WidevineUtil.getLicenseDurationRemainingSec(this);
        return Math.min(((Long) pair.first).longValue(), ((Long) pair.second).longValue());
    }

    private void postKeyRequest(int type, boolean allowRetry) {
        try {
            KeyRequest request = this.mediaDrm.getKeyRequest(type == 3 ? this.offlineLicenseKeySetId : this.sessionId, this.initData, this.mimeType, type, this.optionalKeyRequestParameters);
            if (C.CLEARKEY_UUID.equals(this.uuid)) {
                request = new DefaultKeyRequest(ClearKeyUtil.adjustRequestData(request.getData()), request.getDefaultUrl());
            }
            this.postRequestHandler.obtainMessage(1, request, allowRetry).sendToTarget();
        } catch (Exception e) {
            onKeysError(e);
        }
    }

    private void onKeyResponse(Object response) {
        if (!isOpen()) {
            return;
        }
        if (response instanceof Exception) {
            onKeysError((Exception) response);
            return;
        }
        try {
            byte[] responseData = (byte[]) response;
            if (C.CLEARKEY_UUID.equals(this.uuid)) {
                responseData = ClearKeyUtil.adjustResponseData(responseData);
            }
            if (this.mode == 3) {
                this.mediaDrm.provideKeyResponse(this.offlineLicenseKeySetId, responseData);
                if (!(this.eventHandler == null || this.eventListener == null)) {
                    this.eventHandler.post(new Runnable() {
                        public void run() {
                            DefaultDrmSession.this.eventListener.onDrmKeysRemoved();
                        }
                    });
                }
            } else {
                byte[] keySetId = this.mediaDrm.provideKeyResponse(this.sessionId, responseData);
                if (!((this.mode != 2 && (this.mode != 0 || this.offlineLicenseKeySetId == null)) || keySetId == null || keySetId.length == 0)) {
                    this.offlineLicenseKeySetId = keySetId;
                }
                this.state = 4;
                if (!(this.eventHandler == null || this.eventListener == null)) {
                    this.eventHandler.post(new Runnable() {
                        public void run() {
                            DefaultDrmSession.this.eventListener.onDrmKeysLoaded();
                        }
                    });
                }
            }
        } catch (Exception e) {
            onKeysError(e);
        }
    }

    private void onKeysExpired() {
        if (this.state == 4) {
            this.state = 3;
            onError(new KeysExpiredException());
        }
    }

    private void onKeysError(Exception e) {
        if (e instanceof NotProvisionedException) {
            this.provisioningManager.provisionRequired(this);
        } else {
            onError(e);
        }
    }

    private void onError(final Exception e) {
        this.lastException = new DrmSessionException(e);
        if (!(this.eventHandler == null || this.eventListener == null)) {
            this.eventHandler.post(new Runnable() {
                public void run() {
                    DefaultDrmSession.this.eventListener.onDrmSessionManagerError(e);
                }
            });
        }
        if (this.state != 4) {
            this.state = 1;
        }
    }

    private boolean isOpen() {
        if (this.state != 3) {
            if (this.state != 4) {
                return false;
            }
        }
        return true;
    }

    public void onMediaDrmEvent(int what) {
        if (isOpen()) {
            switch (what) {
                case 1:
                    this.state = 3;
                    this.provisioningManager.provisionRequired(this);
                    break;
                case 2:
                    doLicense(false);
                    break;
                case 3:
                    onKeysExpired();
                    break;
                default:
                    break;
            }
        }
    }
}
