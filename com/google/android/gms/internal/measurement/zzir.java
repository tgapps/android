package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;

final class zzir implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ boolean zzapg = true;
    private final /* synthetic */ boolean zzaph;
    private final /* synthetic */ zzed zzapi;
    private final /* synthetic */ zzed zzapj;

    zzir(zzii com_google_android_gms_internal_measurement_zzii, boolean z, boolean z2, zzed com_google_android_gms_internal_measurement_zzed, zzdz com_google_android_gms_internal_measurement_zzdz, zzed com_google_android_gms_internal_measurement_zzed2) {
        this.zzape = com_google_android_gms_internal_measurement_zzii;
        this.zzaph = z2;
        this.zzapi = com_google_android_gms_internal_measurement_zzed;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
        this.zzapj = com_google_android_gms_internal_measurement_zzed2;
    }

    public final void run() {
        zzey zzd = this.zzape.zzaoy;
        if (zzd == null) {
            this.zzape.zzge().zzim().log("Discarding data. Failed to send conditional user property to service");
            return;
        }
        if (this.zzapg) {
            this.zzape.zza(zzd, this.zzaph ? null : this.zzapi, this.zzane);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzapj.packageName)) {
                    zzd.zza(this.zzapi, this.zzane);
                } else {
                    zzd.zzb(this.zzapi);
                }
            } catch (RemoteException e) {
                this.zzape.zzge().zzim().zzg("Failed to send conditional user property to the service", e);
            }
        }
        this.zzape.zzcu();
    }
}
