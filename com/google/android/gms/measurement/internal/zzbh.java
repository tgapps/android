package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.android.gms.internal.measurement.zzu;
import com.google.android.gms.internal.measurement.zzv;

public final class zzbh implements ServiceConnection {
    private final String packageName;
    final /* synthetic */ zzbg zzaoe;

    zzbh(zzbg com_google_android_gms_measurement_internal_zzbg, String str) {
        this.zzaoe = com_google_android_gms_measurement_internal_zzbg;
        this.packageName = str;
    }

    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (iBinder == null) {
            this.zzaoe.zzadj.zzgo().zzjg().zzbx("Install Referrer connection returned with null binder");
            return;
        }
        try {
            zzu zza = zzv.zza(iBinder);
            if (zza == null) {
                this.zzaoe.zzadj.zzgo().zzjg().zzbx("Install Referrer Service implementation was not found");
                return;
            }
            this.zzaoe.zzadj.zzgo().zzjj().zzbx("Install Referrer Service connected");
            this.zzaoe.zzadj.zzgn().zzc(new zzbi(this, zza, this));
        } catch (Exception e) {
            this.zzaoe.zzadj.zzgo().zzjg().zzg("Exception occurred while calling Install Referrer API", e);
        }
    }

    public final void onServiceDisconnected(ComponentName componentName) {
        this.zzaoe.zzadj.zzgo().zzjj().zzbx("Install Referrer Service disconnected");
    }
}
