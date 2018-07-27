package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;

final class zzir implements Runnable {
    private final /* synthetic */ zzdz zzano;
    private final /* synthetic */ String zzant;
    private final /* synthetic */ zzew zzanu;
    private final /* synthetic */ zzij zzapn;
    private final /* synthetic */ boolean zzapp;
    private final /* synthetic */ boolean zzapq;

    zzir(zzij com_google_android_gms_internal_measurement_zzij, boolean z, boolean z2, zzew com_google_android_gms_internal_measurement_zzew, zzdz com_google_android_gms_internal_measurement_zzdz, String str) {
        this.zzapn = com_google_android_gms_internal_measurement_zzij;
        this.zzapp = z;
        this.zzapq = z2;
        this.zzanu = com_google_android_gms_internal_measurement_zzew;
        this.zzano = com_google_android_gms_internal_measurement_zzdz;
        this.zzant = str;
    }

    public final void run() {
        zzez zzd = this.zzapn.zzaph;
        if (zzd == null) {
            this.zzapn.zzgf().zzis().log("Discarding data. Failed to send event to service");
            return;
        }
        if (this.zzapp) {
            this.zzapn.zza(zzd, this.zzapq ? null : this.zzanu, this.zzano);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzant)) {
                    zzd.zza(this.zzanu, this.zzano);
                } else {
                    zzd.zza(this.zzanu, this.zzant, this.zzapn.zzgf().zzjb());
                }
            } catch (RemoteException e) {
                this.zzapn.zzgf().zzis().zzg("Failed to send event to the service", e);
            }
        }
        this.zzapn.zzcu();
    }
}
