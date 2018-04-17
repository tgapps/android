package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.tasks.TaskCompletionSource;

public abstract class RegisterListenerMethod<A extends AnyClient, L> {
    private final ListenerHolder<L> zzls;

    public void clearListener() {
        this.zzls.clear();
    }

    protected abstract void registerListener(A a, TaskCompletionSource<Void> taskCompletionSource) throws RemoteException;
}
