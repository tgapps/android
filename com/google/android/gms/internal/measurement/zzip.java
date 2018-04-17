package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzip implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ zzil zzapy;

    zzip(zzil com_google_android_gms_internal_measurement_zzil, zzec com_google_android_gms_internal_measurement_zzec) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
    }

    public final void run() {
        zzey zzd = this.zzapy.zzaps;
        if (zzd == null) {
            this.zzapy.zzgg().zzil().log("Discarding data. Failed to send app launch");
            return;
        }
        try {
            zzd.zza(this.zzanq);
            this.zzapy.zza(zzd, null, this.zzanq);
            this.zzapy.zzcu();
        } catch (RemoteException e) {
            this.zzapy.zzgg().zzil().zzg("Failed to send app launch to the service", e);
        }
    }
}
