package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class zzaf extends GoogleApiClient {
    private final String zzhe;

    public zzaf(String str) {
        this.zzhe = str;
    }

    public ConnectionResult blockingConnect() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void connect() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void disconnect() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public boolean isConnected() {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        throw new UnsupportedOperationException(this.zzhe);
    }

    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {
        throw new UnsupportedOperationException(this.zzhe);
    }
}
