package com.google.firebase.iid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.concurrent.GuardedBy;

public final class zzaa {
    @GuardedBy("MessengerIpcClient.class")
    private static zzaa zzbq;
    private final ScheduledExecutorService zzbr;
    @GuardedBy("this")
    private zzac zzbs = new zzac();
    @GuardedBy("this")
    private int zzbt = 1;
    private final Context zzv;

    public static synchronized zzaa zzc(Context context) {
        zzaa com_google_firebase_iid_zzaa;
        synchronized (zzaa.class) {
            if (zzbq == null) {
                zzbq = new zzaa(context, Executors.newSingleThreadScheduledExecutor());
            }
            com_google_firebase_iid_zzaa = zzbq;
        }
        return com_google_firebase_iid_zzaa;
    }

    private zzaa(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zzbr = scheduledExecutorService;
        this.zzv = context.getApplicationContext();
    }

    public final Task<Void> zza(int i, Bundle bundle) {
        return zza(new zzai(zzw(), 2, bundle));
    }

    public final Task<Bundle> zzb(int i, Bundle bundle) {
        return zza(new zzal(zzw(), 1, bundle));
    }

    private final synchronized <T> Task<T> zza(zzaj<T> com_google_firebase_iid_zzaj_T) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(com_google_firebase_iid_zzaj_T);
            Log.d("MessengerIpcClient", new StringBuilder(String.valueOf(valueOf).length() + 9).append("Queueing ").append(valueOf).toString());
        }
        if (!this.zzbs.zzb(com_google_firebase_iid_zzaj_T)) {
            this.zzbs = new zzac();
            this.zzbs.zzb(com_google_firebase_iid_zzaj_T);
        }
        return com_google_firebase_iid_zzaj_T.zzcd.getTask();
    }

    private final synchronized int zzw() {
        int i;
        i = this.zzbt;
        this.zzbt = i + 1;
        return i;
    }
}
