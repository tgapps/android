package com.google.android.gms.internal.measurement;

import android.os.RemoteException;

final class zzin implements Runnable {
    private final /* synthetic */ zzie zzaow;
    private final /* synthetic */ zzii zzape;

    zzin(zzii com_google_android_gms_internal_measurement_zzii, zzie com_google_android_gms_internal_measurement_zzie) {
        this.zzape = com_google_android_gms_internal_measurement_zzii;
        this.zzaow = com_google_android_gms_internal_measurement_zzie;
    }

    public final void run() {
        zzey zzd = this.zzape.zzaoy;
        if (zzd == null) {
            this.zzape.zzge().zzim().log("Failed to send current screen to service");
            return;
        }
        try {
            if (this.zzaow == null) {
                zzd.zza(0, null, null, this.zzape.getContext().getPackageName());
            } else {
                zzd.zza(this.zzaow.zzaoj, this.zzaow.zzul, this.zzaow.zzaoi, this.zzape.getContext().getPackageName());
            }
            this.zzape.zzcu();
        } catch (RemoteException e) {
            this.zzape.zzge().zzim().zzg("Failed to send current screen to the service", e);
        }
    }
}
