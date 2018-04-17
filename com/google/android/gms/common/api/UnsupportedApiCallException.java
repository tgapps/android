package com.google.android.gms.common.api;

import com.google.android.gms.common.Feature;

public final class UnsupportedApiCallException extends UnsupportedOperationException {
    private final Feature zzdr;

    public UnsupportedApiCallException(Feature feature) {
        this.zzdr = feature;
    }

    public final String getMessage() {
        String valueOf = String.valueOf(this.zzdr);
        StringBuilder stringBuilder = new StringBuilder(8 + String.valueOf(valueOf).length());
        stringBuilder.append("Missing ");
        stringBuilder.append(valueOf);
        return stringBuilder.toString();
    }
}
