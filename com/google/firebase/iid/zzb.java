package com.google.firebase.iid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import com.google.android.gms.common.util.concurrent.NamedThreadFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class zzb extends Service {
    private final Object lock;
    final ExecutorService zzall;
    private Binder zzbpo;
    private int zzbpp;
    private int zzbpq;

    public zzb() {
        String str = "Firebase-";
        String valueOf = String.valueOf(getClass().getSimpleName());
        this.zzall = Executors.newSingleThreadExecutor(new NamedThreadFactory(valueOf.length() != 0 ? str.concat(valueOf) : new String(str)));
        this.lock = new Object();
        this.zzbpq = 0;
    }

    private final void zze(Intent intent) {
        if (intent != null) {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
        synchronized (this.lock) {
            this.zzbpq--;
            if (this.zzbpq == 0) {
                stopSelfResult(this.zzbpp);
            }
        }
    }

    public final synchronized IBinder onBind(Intent intent) {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            Log.d("EnhancedIntentService", "Service received bind request");
        }
        if (this.zzbpo == null) {
            this.zzbpo = new zzf(this);
        }
        return this.zzbpo;
    }

    public final int onStartCommand(Intent intent, int i, int i2) {
        synchronized (this.lock) {
            this.zzbpp = i2;
            this.zzbpq++;
        }
        Intent zzf = zzf(intent);
        if (zzf == null) {
            zze(intent);
            return 2;
        } else if (zzg(zzf)) {
            zze(intent);
            return 2;
        } else {
            this.zzall.execute(new zzc(this, zzf, intent));
            return 3;
        }
    }

    protected Intent zzf(Intent intent) {
        return intent;
    }

    public boolean zzg(Intent intent) {
        return false;
    }

    public abstract void zzh(Intent intent);
}
