package com.google.android.exoplayer2.drm;

import com.google.android.exoplayer2.drm.DrmSession.DrmSessionException;
import com.google.android.exoplayer2.util.Assertions;
import java.util.Map;

public final class ErrorStateDrmSession<T extends ExoMediaCrypto> implements DrmSession<T> {
    private final DrmSessionException error;

    public ErrorStateDrmSession(DrmSessionException error) {
        this.error = (DrmSessionException) Assertions.checkNotNull(error);
    }

    public int getState() {
        return 1;
    }

    public DrmSessionException getError() {
        return this.error;
    }

    public T getMediaCrypto() {
        return null;
    }

    public Map<String, String> queryKeyStatus() {
        return null;
    }

    public byte[] getOfflineLicenseKeySetId() {
        return null;
    }
}
