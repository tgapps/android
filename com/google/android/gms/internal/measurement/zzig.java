package com.google.android.gms.internal.measurement;

import android.os.Bundle;

final class zzig implements Runnable {
    private final /* synthetic */ boolean zzaos;
    private final /* synthetic */ zzie zzaot;
    private final /* synthetic */ zzie zzaou;
    private final /* synthetic */ zzif zzaov;

    zzig(zzif com_google_android_gms_internal_measurement_zzif, boolean z, zzie com_google_android_gms_internal_measurement_zzie, zzie com_google_android_gms_internal_measurement_zzie2) {
        this.zzaov = com_google_android_gms_internal_measurement_zzif;
        this.zzaos = z;
        this.zzaot = com_google_android_gms_internal_measurement_zzie;
        this.zzaou = com_google_android_gms_internal_measurement_zzie2;
    }

    public final void run() {
        if (this.zzaos && this.zzaov.zzaol != null) {
            this.zzaov.zza(this.zzaov.zzaol);
        }
        boolean z = (this.zzaot != null && this.zzaot.zzaoj == this.zzaou.zzaoj && zzka.zzs(this.zzaot.zzaoi, this.zzaou.zzaoi) && zzka.zzs(this.zzaot.zzul, this.zzaou.zzul)) ? false : true;
        if (z) {
            Bundle bundle = new Bundle();
            zzif.zza(this.zzaou, bundle, true);
            if (this.zzaot != null) {
                if (this.zzaot.zzul != null) {
                    bundle.putString("_pn", this.zzaot.zzul);
                }
                bundle.putString("_pc", this.zzaot.zzaoi);
                bundle.putLong("_pi", this.zzaot.zzaoj);
            }
            this.zzaov.zzfu().zza("auto", "_vs", bundle);
        }
        this.zzaov.zzaol = this.zzaou;
        this.zzaov.zzfx().zzb(this.zzaou);
    }
}
