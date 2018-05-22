package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzik implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzii zzape;

    zzik(zzii com_google_android_gms_internal_measurement_zzii, zzdz com_google_android_gms_internal_measurement_zzdz) {
        this.zzape = com_google_android_gms_internal_measurement_zzii;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
    }

    public final void run() {
        zzey zzd = this.zzape.zzaoy;
        if (zzd == null) {
            this.zzape.zzge().zzim().log("Failed to reset data on the service; null service");
            return;
        }
        try {
            zzd.zzd(this.zzane);
        } catch (RemoteException e) {
            this.zzape.zzge().zzim().zzg("Failed to reset data on the service", e);
        }
        this.zzape.zzcu();
    }
}
