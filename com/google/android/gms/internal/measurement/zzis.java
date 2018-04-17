package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzis implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ zzil zzapy;

    zzis(zzil com_google_android_gms_internal_measurement_zzil, zzec com_google_android_gms_internal_measurement_zzec) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
    }

    public final void run() {
        zzey zzd = this.zzapy.zzaps;
        if (zzd == null) {
            this.zzapy.zzgg().zzil().log("Failed to send measurementEnabled to service");
            return;
        }
        try {
            zzd.zzb(this.zzanq);
            this.zzapy.zzcu();
        } catch (RemoteException e) {
            this.zzapy.zzgg().zzil().zzg("Failed to send measurementEnabled to the service", e);
        }
    }
}
