package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;

final class zziu implements Runnable {
    private final /* synthetic */ zzec zzanq;
    private final /* synthetic */ zzil zzapy;
    private final /* synthetic */ boolean zzaqb = true;
    private final /* synthetic */ boolean zzaqc;
    private final /* synthetic */ zzef zzaqd;
    private final /* synthetic */ zzef zzaqe;

    zziu(zzil com_google_android_gms_internal_measurement_zzil, boolean z, boolean z2, zzef com_google_android_gms_internal_measurement_zzef, zzec com_google_android_gms_internal_measurement_zzec, zzef com_google_android_gms_internal_measurement_zzef2) {
        this.zzapy = com_google_android_gms_internal_measurement_zzil;
        this.zzaqc = z2;
        this.zzaqd = com_google_android_gms_internal_measurement_zzef;
        this.zzanq = com_google_android_gms_internal_measurement_zzec;
        this.zzaqe = com_google_android_gms_internal_measurement_zzef2;
    }

    public final void run() {
        zzey zzd = this.zzapy.zzaps;
        if (zzd == null) {
            this.zzapy.zzgg().zzil().log("Discarding data. Failed to send conditional user property to service");
            return;
        }
        if (this.zzaqb) {
            this.zzapy.zza(zzd, this.zzaqc ? null : this.zzaqd, this.zzanq);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzaqe.packageName)) {
                    zzd.zza(this.zzaqd, this.zzanq);
                } else {
                    zzd.zzb(this.zzaqd);
                }
            } catch (RemoteException e) {
                this.zzapy.zzgg().zzil().zzg("Failed to send conditional user property to the service", e);
            }
        }
        this.zzapy.zzcu();
    }
}
