package com.google.firebase.iid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.concurrent.GuardedBy;

public final class zzk {
    @GuardedBy("MessengerIpcClient.class")
    private static zzk zzbqv;
    private final ScheduledExecutorService zzbqw;
    @GuardedBy("this")
    private zzm zzbqx = new zzm();
    @GuardedBy("this")
    private int zzbqy = 1;
    private final Context zzqs;

    private zzk(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zzbqw = scheduledExecutorService;
        this.zzqs = context.getApplicationContext();
    }

    private final synchronized <T> Task<T> zza(zzt<T> com_google_firebase_iid_zzt_T) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(com_google_firebase_iid_zzt_T);
            StringBuilder stringBuilder = new StringBuilder(9 + String.valueOf(valueOf).length());
            stringBuilder.append("Queueing ");
            stringBuilder.append(valueOf);
            Log.d("MessengerIpcClient", stringBuilder.toString());
        }
        if (!this.zzbqx.zzb(com_google_firebase_iid_zzt_T)) {
            this.zzbqx = new zzm();
            this.zzbqx.zzb(com_google_firebase_iid_zzt_T);
        }
        return com_google_firebase_iid_zzt_T.zzbri.getTask();
    }

    private final synchronized int zzsp() {
        int i;
        i = this.zzbqy;
        this.zzbqy = i + 1;
        return i;
    }

    public static synchronized zzk zzv(Context context) {
        zzk com_google_firebase_iid_zzk;
        synchronized (zzk.class) {
            if (zzbqv == null) {
                zzbqv = new zzk(context, Executors.newSingleThreadScheduledExecutor());
            }
            com_google_firebase_iid_zzk = zzbqv;
        }
        return com_google_firebase_iid_zzk;
    }

    public final Task<Void> zza(int i, Bundle bundle) {
        return zza(new zzs(zzsp(), 2, bundle));
    }

    public final Task<Bundle> zzb(int i, Bundle bundle) {
        return zza(new zzv(zzsp(), 1, bundle));
    }
}
