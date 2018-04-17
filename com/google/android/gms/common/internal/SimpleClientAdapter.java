package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import com.google.android.gms.common.api.Api.SimpleClient;

public class SimpleClientAdapter<T extends IInterface> extends GmsClient<T> {
    private final SimpleClient<T> zzva;

    protected T createServiceInterface(IBinder iBinder) {
        return this.zzva.createServiceInterface(iBinder);
    }

    public SimpleClient<T> getClient() {
        return this.zzva;
    }

    public int getMinApkVersion() {
        return super.getMinApkVersion();
    }

    protected String getServiceDescriptor() {
        return this.zzva.getServiceDescriptor();
    }

    protected String getStartServiceAction() {
        return this.zzva.getStartServiceAction();
    }

    protected void onSetConnectState(int i, T t) {
        this.zzva.setState(i, t);
    }
}
