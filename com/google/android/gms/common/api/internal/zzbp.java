package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api.AnyClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.BaseImplementation.ApiMethodImpl;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public interface zzbp {
    ConnectionResult blockingConnect();

    void connect();

    void disconnect();

    void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    <A extends AnyClient, R extends Result, T extends ApiMethodImpl<R, A>> T enqueue(T t);

    <A extends AnyClient, T extends ApiMethodImpl<? extends Result, A>> T execute(T t);

    boolean isConnected();

    void zzz();
}
