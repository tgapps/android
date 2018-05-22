package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzip implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzii zzape;

    zzip(zzii com_google_android_gms_internal_measurement_zzii, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzape = com_google_android_gms_internal_measurement_zzii;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    public final void run() {
        zzey zzd = this.zzape.zzaoy;
        if (zzd == null) {
            this.zzape.zzge().zzim().log("Failed to send measurementEnabled to service");
            return;
        }
        try {
            zzd.zzb(this.zzane);
            this.zzape.zzcu();
        } catch (RemoteException e) {
            this.zzape.zzge().zzim().zzg("Failed to send measurementEnabled to the service", e);
        }
    }
}
