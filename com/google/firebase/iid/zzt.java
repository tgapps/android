package com.google.firebase.iid;

import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.TaskCompletionSource;

abstract class zzt<T> {
    final int what;
    final int zzbrh;
    final TaskCompletionSource<T> zzbri = new TaskCompletionSource();
    final Bundle zzbrj;

    zzt(int i, int i2, Bundle bundle) {
        this.zzbrh = i;
        this.what = i2;
        this.zzbrj = bundle;
    }

    final void finish(T t) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(t);
            Log.d("MessengerIpcClient", new StringBuilder((String.valueOf(valueOf).length() + 16) + String.valueOf(valueOf2).length()).append("Finishing ").append(valueOf).append(" with ").append(valueOf2).toString());
        }
        this.zzbri.setResult(t);
    }

    public String toString() {
        int i = this.what;
        int i2 = this.zzbrh;
        return "Request { what=" + i + " id=" + i2 + " oneWay=" + zzst() + "}";
    }

    final void zza(zzu com_google_firebase_iid_zzu) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(com_google_firebase_iid_zzu);
            Log.d("MessengerIpcClient", new StringBuilder((String.valueOf(valueOf).length() + 14) + String.valueOf(valueOf2).length()).append("Failing ").append(valueOf).append(" with ").append(valueOf2).toString());
        }
        this.zzbri.setException(com_google_firebase_iid_zzu);
    }

    abstract void zzh(Bundle bundle);

    abstract boolean zzst();
}
