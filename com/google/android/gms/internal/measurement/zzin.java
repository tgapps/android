package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzin implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ zzil zzapy;

    zzin(zzil com_google_android_gms_internal_measurement_zzil, zzec com_google_android_gms_internal_measurement_zzec) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
    }

    public final void run() {
        zzey zzd = this.zzapy.zzaps;
        if (zzd == null) {
            this.zzapy.zzgg().zzil().log("Failed to reset data on the service; null service");
            return;
        }
        try {
            zzd.zzd(this.zzanq);
        } catch (RemoteException e) {
            this.zzapy.zzgg().zzil().zzg("Failed to reset data on the service", e);
        }
        this.zzapy.zzcu();
    }
}
