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
            long j;
            String str;
            String str2;
            String packageName;
            if (this.zzaqa == null) {
                j = 0;
                str = null;
                str2 = null;
                packageName = this.zzapy.getContext().getPackageName();
            } else {
                j = this.zzaqa.zzapb;
                str = this.zzaqa.zzug;
                str2 = this.zzaqa.zzapa;
                packageName = this.zzapy.getContext().getPackageName();
            }
            zzd.zza(j, str, str2, packageName);
            this.zzapy.zzcu();
        } catch (RemoteException e) {
            this.zzapy.zzgg().zzil().zzg("Failed to send current screen to the service", e);
        }
    }
}
