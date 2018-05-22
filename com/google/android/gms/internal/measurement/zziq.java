package com.google.android.gms.internal.measurement;

import android.os.RemoteException;
import android.text.TextUtils;

final class zziq implements Runnable {
    private final /* synthetic */ zzdz zzane;
    private final /* synthetic */ String zzanj;
    private final /* synthetic */ zzeu zzank;
    private final /* synthetic */ zzii zzape;
    private final /* synthetic */ boolean zzapg = true;
    private final /* synthetic */ boolean zzaph;

    zziq(zzii com_google_android_gms_internal_measurement_zzii, boolean z, boolean z2, zzeu com_google_android_gms_internal_measurement_zzeu, zzdz com_google_android_gms_internal_measurement_zzdz, String str) {
        this.zzape = com_google_android_gms_internal_measurement_zzii;
        this.zzaph = z2;
        this.zzank = com_google_android_gms_internal_measurement_zzeu;
        this.zzane = com_google_android_gms_internal_measurement_zzdz;
        this.zzanj = str;
    }

    public final void run() {
        zzey zzd = this.zzape.zzaoy;
        if (zzd == null) {
            this.zzape.zzge().zzim().log("Discarding data. Failed to send event to service");
            return;
        }
        if (this.zzapg) {
            this.zzape.zza(zzd, this.zzaph ? null : this.zzank, this.zzane);
        } else {
            try {
                if (TextUtils.isEmpty(this.zzanj)) {
                    zzd.zza(this.zzank, this.zzane);
                } else {
                    zzd.zza(this.zzank, this.zzanj, this.zzape.zzge().zziv());
                }
            } catch (RemoteException e) {
                this.zzape.zzge().zzim().zzg("Failed to send event to the service", e);
            }
        }
        this.zzape.zzcu();
    }
}
