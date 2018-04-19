package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zziq implements Runnable {
    private final /* synthetic */ zzil zzapy;
    private final /* synthetic */ zzig zzaqa;

    zziq(zzil com_google_android_gms_internal_measurement_zzil, zzig com_google_android_gms_internal_measurement_zzig) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzaqa = com_google_android_gms_internal_measurement_zzig;
    }

    public final void run() {
        zzey zzd = this.zzapy.zzaps;
        if (zzd == null) {
            this.zzapy.zzgg().zzil().log("Failed to send current screen to service");
            return;
        }
        try {
            if (this.zzaqa == null) {
                zzd.zza(0, null, null, this.zzapy.getContext().getPackageName());
            } else {
                zzd.zza(this.zzaqa.zzapb, this.zzaqa.zzug, this.zzaqa.zzapa, this.zzapy.getContext().getPackageName());
            }
            this.zzapy.zzcu();
        } catch (RemoteException e) {
            this.zzapy.zzgg().zzil().zzg("Failed to send current screen to the service", e);
        }
    }
}
