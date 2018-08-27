package com.google.android.exoplayer2.drm;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.media.NotProvisionedException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.drm.DrmInitData.SchemeData;
import com.google.android.exoplayer2.drm.DrmSession.DrmSessionException;
import com.google.android.exoplayer2.util.EventDispatcher;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@TargetApi(18)
class DefaultDrmSession<T extends ExoMediaCrypto> implements DrmSession<T> {
    private static final int MAX_LICENSE_DURATION_TO_RENEW = 60;
    private static final int MSG_KEYS = 1;
    private static final int MSG_PROVISION = 0;
    private static final String TAG = "DefaultDrmSession";
    final MediaDrmCallback callback;
    private Object currentKeyRequest;
    private Object currentProvisionRequest;
    private final EventDispatcher<DefaultDrmSessionEventListener> eventDispatcher;
    private final int initialDrmRequestRetryCount;
    private DrmSessionException lastException;
    private T mediaCrypto;
    private final ExoMediaDrm<T> mediaDrm;
    private final int mode;
    private byte[] offlineLicenseKeySetId;
    private int openCount;
    private final HashMap<String, String> optionalKeyRequestParameters;
    private PostRequestHandler postRequestHandler;
    final PostResponseHandler postResponseHandler;
    private final ProvisioningManager<T> provisioningManager;
    private HandlerThread requestHandlerThread;
    private final SchemeData schemeData;
    private byte[] sessionId;
    private int state;
    final UUID uuid;

    @SuppressLint({"HandlerLeak"})
    private class PostRequestHandler extends Handler {
        public PostRequestHandler(Looper backgroundLooper) {
            super(backgroundLooper);
        }

        void post(int what, Object request, boolean allowRetry) {
            obtainMessage(what, allowRetry ? 1 : 0, 0, request).sendToTarget();
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void handleMessage(android.os.Message r11) {
            /*
            r10 = this;
            r5 = r11.obj;
            r7 = r11.what;	 Catch:{ Exception -> 0x000d }
            switch(r7) {
                case 0: goto L_0x0015;
                case 1: goto L_0x0037;
                default: goto L_0x0007;
            };	 Catch:{ Exception -> 0x000d }
        L_0x0007:
            r7 = new java.lang.RuntimeException;	 Catch:{ Exception -> 0x000d }
            r7.<init>();	 Catch:{ Exception -> 0x000d }
            throw r7;	 Catch:{ Exception -> 0x000d }
        L_0x000d:
            r1 = move-exception;
            r7 = r10.maybeRetryRequest(r11);
            if (r7 == 0) goto L_0x0050;
        L_0x0014:
            return;
        L_0x0015:
            r7 = com.google.android.exoplayer2.drm.DefaultDrmSession.this;	 Catch:{ Exception -> 0x000d }
            r8 = r7.callback;	 Catch:{ Exception -> 0x000d }
            r7 = com.google.android.exoplayer2.drm.DefaultDrmSession.this;	 Catch:{ Exception -> 0x000d }
            r9 = r7.uuid;	 Catch:{ Exception -> 0x000d }
            r0 = r5;
            r0 = (com.google.android.exoplayer2.drm.ExoMediaDrm.ProvisionRequest) r0;	 Catch:{ Exception -> 0x000d }
            r7 = r0;
            r6 = r8.executeProvisionRequest(r9, r7);	 Catch:{ Exception -> 0x000d }
        L_0x0025:
            r7 = com.google.android.exoplayer2.drm.DefaultDrmSession.this;
            r7 = r7.postResponseHandler;
            r8 = r11.what;
            r9 = android.util.Pair.create(r5, r6);
            r7 = r7.obtainMessage(r8, r9);
            r7.sendToTarget();
            goto L_0x0014;
        L_0x0037:
            r0 = r5;
            r0 = (android.util.Pair) r0;	 Catch:{ Exception -> 0x000d }
            r2 = r0;
            r4 = r2.first;	 Catch:{ Exception -> 0x000d }
            r4 = (com.google.android.exoplayer2.drm.ExoMediaDrm.KeyRequest) r4;	 Catch:{ Exception -> 0x000d }
            r3 = r2.second;	 Catch:{ Exception -> 0x000d }
            r3 = (java.lang.String) r3;	 Catch:{ Exception -> 0x000d }
            r7 = com.google.android.exoplayer2.drm.DefaultDrmSession.this;	 Catch:{ Exception -> 0x000d }
            r7 = r7.callback;	 Catch:{ Exception -> 0x000d }
            r8 = com.google.android.exoplayer2.drm.DefaultDrmSession.this;	 Catch:{ Exception -> 0x000d }
            r8 = r8.uuid;	 Catch:{ Exception -> 0x000d }
            r6 = r7.executeKeyRequest(r8, r4, r3);	 Catch:{ Exception -> 0x000d }
            goto L_0x0025;
        L_0x0050:
            r6 = r1;
            goto L_0x0025;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer2.drm.DefaultDrmSession.PostRequestHandler.handleMessage(android.os.Message):void");
        }

        private boolean maybeRetryRequest(Message originalMsg) {
            boolean allowRetry;
            if (originalMsg.arg1 == 1) {
                allowRetry = true;
            } else {
                allowRetry = false;
            }
            if (!allowRetry) {
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
            Pair<?, ?> requestAndResponse = msg.obj;
            Object request = requestAndResponse.first;
            Object response = requestAndResponse.second;
            switch (msg.what) {
                case 0:
                    DefaultDrmSession.this.onProvisionResponse(request, response);
                    return;
                case 1:
                    DefaultDrmSession.this.onKeyResponse(request, response);
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

    public DefaultDrmSession(UUID uuid, ExoMediaDrm<T> mediaDrm, ProvisioningManager<T> provisioningManager, SchemeData schemeData, int mode, byte[] offlineLicenseKeySetId, HashMap<String, String> optionalKeyRequestParameters, MediaDrmCallback callback, Looper playbackLooper, EventDispatcher<DefaultDrmSessionEventListener> eventDispatcher, int initialDrmRequestRetryCount) {
        this.uuid = uuid;
        this.provisioningManager = provisioningManager;
        this.mediaDrm = mediaDrm;
        this.mode = mode;
        this.offlineLicenseKeySetId = offlineLicenseKeySetId;
        if (offlineLicenseKeySetId != null) {
            schemeData = null;
        }
        this.schemeData = schemeData;
        this.optionalKeyRequestParameters = optionalKeyRequestParameters;
        this.callback = callback;
        this.initialDrmRequestRetryCount = initialDrmRequestRetryCount;
        this.eventDispatcher = eventDispatcher;
        this.state = 2;
        this.postResponseHandler = new PostResponseHandler(playbackLooper);
        this.requestHandlerThread = new HandlerThread("DrmRequestHandler");
        this.requestHandlerThread.start();
        this.postRequestHandler = new PostRequestHandler(this.requestHandlerThread.getLooper());
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
        this.currentKeyRequest = null;
        this.currentProvisionRequest = null;
        if (this.sessionId != null) {
            this.mediaDrm.closeSession(this.sessionId);
            this.sessionId = null;
        }
        return true;
    }

    public boolean hasInitData(byte[] initData) {
        return Arrays.equals(this.schemeData != null ? this.schemeData.data : null, initData);
    }

    public boolean hasSessionId(byte[] sessionId) {
        return Arrays.equals(this.sessionId, sessionId);
    }

    public void onMediaDrmEvent(int what) {
        if (isOpen()) {
            switch (what) {
                case 1:
                    this.state = 3;
                    this.provisioningManager.provisionRequired(this);
                    return;
                case 2:
                    doLicense(false);
                    return;
                case 3:
                    onKeysExpired();
                    return;
                default:
                    return;
            }
        }
    }

    public void provision() {
        this.currentProvisionRequest = this.mediaDrm.getProvisionRequest();
        this.postRequestHandler.post(0, this.currentProvisionRequest, true);
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

    private boolean openInternal(boolean allowProvisioning) {
        if (isOpen()) {
            return true;
        }
        try {
            this.sessionId = this.mediaDrm.openSession();
            this.mediaCrypto = this.mediaDrm.createMediaCrypto(this.sessionId);
            this.state = 3;
            return true;
        } catch (NotProvisionedException e) {
            if (allowProvisioning) {
                this.provisioningManager.provisionRequired(this);
            } else {
                onError(e);
            }
            return false;
        } catch (Exception e2) {
            onError(e2);
            return false;
        }
    }

    private void onProvisionResponse(Object request, Object response) {
        if (request != this.currentProvisionRequest) {
            return;
        }
        if (this.state == 2 || isOpen()) {
            this.currentProvisionRequest = null;
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
    }

    private void doLicense(boolean allowRetry) {
        switch (this.mode) {
            case 0:
            case 1:
                if (this.offlineLicenseKeySetId == null) {
                    postKeyRequest(1, allowRetry);
                    return;
                } else if (this.state == 4 || restoreKeys()) {
                    long licenseDurationRemainingSec = getLicenseDurationRemainingSec();
                    if (this.mode == 0 && licenseDurationRemainingSec <= 60) {
                        Log.d(TAG, "Offline license has expired or will expire soon. Remaining seconds: " + licenseDurationRemainingSec);
                        postKeyRequest(2, allowRetry);
                        return;
                    } else if (licenseDurationRemainingSec <= 0) {
                        onError(new KeysExpiredException());
                        return;
                    } else {
                        this.state = 4;
                        this.eventDispatcher.dispatch(DefaultDrmSession$$Lambda$0.$instance);
                        return;
                    }
                } else {
                    return;
                }
            case 2:
                if (this.offlineLicenseKeySetId == null) {
                    postKeyRequest(2, allowRetry);
                    return;
                } else if (restoreKeys()) {
                    postKeyRequest(2, allowRetry);
                    return;
                } else {
                    return;
                }
            case 3:
                if (restoreKeys()) {
                    postKeyRequest(3, allowRetry);
                    return;
                }
                return;
            default:
                return;
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
        byte[] scope = type == 3 ? this.offlineLicenseKeySetId : this.sessionId;
        byte[] initData = null;
        String mimeType = null;
        String licenseServerUrl = null;
        if (this.schemeData != null) {
            initData = this.schemeData.data;
            mimeType = this.schemeData.mimeType;
            licenseServerUrl = this.schemeData.licenseServerUrl;
        }
        try {
            this.currentKeyRequest = Pair.create(this.mediaDrm.getKeyRequest(scope, initData, mimeType, type, this.optionalKeyRequestParameters), licenseServerUrl);
            this.postRequestHandler.post(1, this.currentKeyRequest, allowRetry);
        } catch (Exception e) {
            onKeysError(e);
        }
    }

    private void onKeyResponse(Object request, Object response) {
        if (request == this.currentKeyRequest && isOpen()) {
            this.currentKeyRequest = null;
            if (response instanceof Exception) {
                onKeysError((Exception) response);
                return;
            }
            try {
                byte[] responseData = (byte[]) response;
                if (this.mode == 3) {
                    this.mediaDrm.provideKeyResponse(this.offlineLicenseKeySetId, responseData);
                    this.eventDispatcher.dispatch(DefaultDrmSession$$Lambda$1.$instance);
                    return;
                }
                byte[] keySetId = this.mediaDrm.provideKeyResponse(this.sessionId, responseData);
                if (!((this.mode != 2 && (this.mode != 0 || this.offlineLicenseKeySetId == null)) || keySetId == null || keySetId.length == 0)) {
                    this.offlineLicenseKeySetId = keySetId;
                }
                this.state = 4;
                this.eventDispatcher.dispatch(DefaultDrmSession$$Lambda$2.$instance);
            } catch (Exception e) {
                onKeysError(e);
            }
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

    private void onError(Exception e) {
        this.lastException = new DrmSessionException(e);
        this.eventDispatcher.dispatch(new DefaultDrmSession$$Lambda$3(e));
        if (this.state != 4) {
            this.state = 1;
        }
    }

    private boolean isOpen() {
        return this.state == 3 || this.state == 4;
    }
}
