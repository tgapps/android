package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;

final class zzit implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ String zzaoc;
    private final /* synthetic */ zzeu zzaod;
    private final /* synthetic */ zzil zzapy;
    private final /* synthetic */ boolean zzaqb = true;
    private final /* synthetic */ boolean zzaqc;

    zzit(zzil com_google_android_gms_internal_measurement_zzil, boolean z, boolean z2, zzeu com_google_android_gms_internal_measurement_zzeu, zzec com_google_android_gms_internal_measurement_zzec, String str) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzaqc = z2;
        this.zzaod = com_google_android_gms_internal_measurement_zzeu;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
        this.zzaoc = str;
    }

    public final void run() {
        zzey zzd = this.zzapy.zzaps;
        if (zzd == null) {
            this.zzapy.zzgg().zzil().log("Discarding data. Failed to send event to service");
            return;
        }
        if (this.zzaqb) {
            this.zzapy.zza(zzd, this.zzaqc ? null : this.zzaod, this.zzanq);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzaoc)) {
                    zzd.zza(this.zzaod, this.zzanq);
                } else {
                    zzd.zza(this.zzaod, this.zzaoc, this.zzapy.zzgg().zzit());
                }
            } catch (RemoteException e) {
                this.zzapy.zzgg().zzil().zzg("Failed to send event to the service", e);
            }
        }
        this.zzapy.zzcu();
    }
}
