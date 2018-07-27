package com.google.firebase.iid;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.concurrent.GuardedBy;

public final class zzz {
    @GuardedBy("MessengerIpcClient.class")
    private static zzz zzbk;
    private final ScheduledExecutorService zzbl;
    @GuardedBy("this")
    private zzab zzbm = new zzab();
    @GuardedBy("this")
    private int zzbn = 1;
    private final Context zzv;

    private zzz(Context context, ScheduledExecutorService scheduledExecutorService) {
        this.zzbl = scheduledExecutorService;
        this.zzv = context.getApplicationContext();
    }

    private final synchronized <T> Task<T> zza(zzai<T> com_google_firebase_iid_zzai_T) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            String valueOf = String.valueOf(com_google_firebase_iid_zzai_T);
            Log.d("MessengerIpcClient", new StringBuilder(String.valueOf(valueOf).length() + 9).append("Queueing ").append(valueOf).toString());
        }
        if (!this.zzbm.zzb(com_google_firebase_iid_zzai_T)) {
            this.zzbm = new zzab();
            this.zzbm.zzb(com_google_firebase_iid_zzai_T);
        }
        return com_google_firebase_iid_zzai_T.zzbx.getTask();
    }

    public static synchronized zzz zzc(Context context) {
        zzz com_google_firebase_iid_zzz;
        synchronized (zzz.class) {
            if (zzbk == null) {
                zzbk = new zzz(context, Executors.newSingleThreadScheduledExecutor());
            }
            com_google_firebase_iid_zzz = zzbk;
        }
        return com_google_firebase_iid_zzz;
    }

    private final synchronized int zzw() {
        int i;
        i = this.zzbn;
        this.zzbn = i + 1;
        return i;
    }

    public final Task<Void> zza(int i, Bundle bundle) {
        return zza(new zzah(zzw(), 2, bundle));
    }

    public final Task<Bundle> zzb(int i, Bundle bundle) {
        return zza(new zzak(zzw(), 1, bundle));
    }
}
