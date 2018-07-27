package com.google.firebase.iid;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zzai<T> {
    final int what;
    final int zzbw;
    final TaskCompletionSource<T> zzbx = new TaskCompletionSource();
    final Bundle zzby;

    zzai(int i, int i2, Bundle bundle) {
        this.zzbw = i;
        this.what = i2;
        this.zzby = bundle;
    }

    final void finish(T t) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(t);
            Log.d("MessengerIpcClient", new StringBuilder((String.valueOf(valueOf).length() + 16) + String.valueOf(valueOf2).length()).append("Finishing ").append(valueOf).append(" with ").append(valueOf2).toString());
        }
        this.zzbx.setResult(t);
    }

    public String toString() {
        int i = this.what;
        int i2 = this.zzbw;
        return "Request { what=" + i + " id=" + i2 + " oneWay=" + zzaa() + "}";
    }

    final void zza(zzaj com_google_firebase_iid_zzaj) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(com_google_firebase_iid_zzaj);
            Log.d("MessengerIpcClient", new StringBuilder((String.valueOf(valueOf).length() + 14) + String.valueOf(valueOf2).length()).append("Failing ").append(valueOf).append(" with ").append(valueOf2).toString());
        }
        this.zzbx.setException(com_google_firebase_iid_zzaj);
    }

    abstract boolean zzaa();

    abstract void zzb(Bundle bundle);
}
