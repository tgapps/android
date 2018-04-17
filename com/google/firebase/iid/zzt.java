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
            StringBuilder stringBuilder = new StringBuilder((16 + String.valueOf(valueOf).length()) + String.valueOf(valueOf2).length());
            stringBuilder.append("Finishing ");
            stringBuilder.append(valueOf);
            stringBuilder.append(" with ");
            stringBuilder.append(valueOf2);
            Log.d("MessengerIpcClient", stringBuilder.toString());
        }
        this.zzbri.setResult(t);
    }

    public String toString() {
        int i = this.what;
        int i2 = this.zzbrh;
        boolean zzst = zzst();
        StringBuilder stringBuilder = new StringBuilder(55);
        stringBuilder.append("Request { what=");
        stringBuilder.append(i);
        stringBuilder.append(" id=");
        stringBuilder.append(i2);
        stringBuilder.append(" oneWay=");
        stringBuilder.append(zzst);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    final void zza(zzu com_google_firebase_iid_zzu) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(this);
            String valueOf2 = String.valueOf(com_google_firebase_iid_zzu);
            StringBuilder stringBuilder = new StringBuilder((14 + String.valueOf(valueOf).length()) + String.valueOf(valueOf2).length());
            stringBuilder.append("Failing ");
            stringBuilder.append(valueOf);
            stringBuilder.append(" with ");
            stringBuilder.append(valueOf2);
            Log.d("MessengerIpcClient", stringBuilder.toString());
        }
        this.zzbri.setException(com_google_firebase_iid_zzu);
    }

    abstract void zzh(Bundle bundle);

    abstract boolean zzst();
}
