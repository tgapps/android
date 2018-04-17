package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.tasks.TaskCompletionSource;

public abstract class TaskApiCall<A extends AnyClient, ResultT> {
    private final Feature[] zzlz;
    private final boolean zzma;

    protected abstract void doExecute(A a, TaskCompletionSource<ResultT> taskCompletionSource) throws RemoteException;

    public boolean shouldAutoResolveMissingFeatures() {
        return this.zzma;
    }

    public final Feature[] zzca() {
        return this.zzlz;
    }
}
