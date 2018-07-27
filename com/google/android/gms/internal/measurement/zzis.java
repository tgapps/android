package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;

final class zzis implements Runnable {
    private final /* synthetic */ zzdz zzano;
    private final /* synthetic */ zzij zzapn;
    private final /* synthetic */ boolean zzapp;
    private final /* synthetic */ boolean zzapq;
    private final /* synthetic */ zzee zzapr;
    private final /* synthetic */ zzee zzaps;

    zzis(zzij com_google_android_gms_internal_measurement_zzij, boolean z, boolean z2, zzee com_google_android_gms_internal_measurement_zzee, zzdz com_google_android_gms_internal_measurement_zzdz, zzee com_google_android_gms_internal_measurement_zzee2) {
        this.zzapn = com_google_android_gms_internal_measurement_zzij;
        this.zzapp = z;
        this.zzapq = z2;
        this.zzapr = com_google_android_gms_internal_measurement_zzee;
        this.zzano = com_google_android_gms_internal_measurement_zzdz;
        this.zzaps = com_google_android_gms_internal_measurement_zzee2;
    }

    public final void run() {
        zzez zzd = this.zzapn.zzaph;
        if (zzd == null) {
            this.zzapn.zzgf().zzis().log("Discarding data. Failed to send conditional user property to service");
            return;
        }
        if (this.zzapp) {
            this.zzapn.zza(zzd, this.zzapq ? null : this.zzapr, this.zzano);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzaps.packageName)) {
                    zzd.zza(this.zzapr, this.zzano);
                } else {
                    zzd.zzb(this.zzapr);
                }
            } catch (RemoteException e) {
                this.zzapn.zzgf().zzis().zzg("Failed to send conditional user property to the service", e);
            }
        }
        this.zzapn.zzcu();
    }
}
