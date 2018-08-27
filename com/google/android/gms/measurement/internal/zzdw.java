package com.google.android.gms.measurement.internal;

import android.os.RemoteException;

final class zzdw implements Runnable {
    private final /* synthetic */ zzdn zzary;
    private final /* synthetic */ zzdr zzasg;

    zzdw(zzdr com_google_android_gms_measurement_internal_zzdr, zzdn com_google_android_gms_measurement_internal_zzdn) {
        this.zzasg = com_google_android_gms_measurement_internal_zzdr;
        this.zzary = com_google_android_gms_measurement_internal_zzdn;
    }

    public final void run() {
        zzag zzd = this.zzasg.zzasa;
        if (zzd == null) {
            this.zzasg.zzgo().zzjd().zzbx("Failed to send current screen to service");
            return;
        }
        try {
            if (this.zzary == null) {
                zzd.zza(0, null, null, this.zzasg.getContext().getPackageName());
            } else {
                zzd.zza(this.zzary.zzarm, this.zzary.zzuw, this.zzary.zzarl, this.zzasg.getContext().getPackageName());
            }
            this.zzasg.zzcy();
        } catch (RemoteException e) {
            this.zzasg.zzgo().zzjd().zzg("Failed to send current screen to the service", e);
        }
    }
}
