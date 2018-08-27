package com.google.android.gms.measurement.internal;

import android.os.RemoteException;

final class zzdy implements Runnable {
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzdr zzasg;

    zzdy(zzdr com_google_android_gms_measurement_internal_zzdr, zzh com_google_android_gms_measurement_internal_zzh) {
        this.zzasg = com_google_android_gms_measurement_internal_zzdr;
        this.zzaqn = com_google_android_gms_measurement_internal_zzh;
    }

    public final void run() {
        zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Failed to send measurementEnabled to service");
            return;
        }
        try {
            zzd.zzb(this.zzaqn);
            this.zzasg.zzcy();
        } catch (RemoteException e) {
            this.zzasg.zzgo().zzjd().zzg("Failed to send measurementEnabled to the service", e);
        }
    }
}
