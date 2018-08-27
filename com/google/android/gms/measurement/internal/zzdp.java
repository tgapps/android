package com.google.android.gms.measurement.internal;

import android.os.Bundle;

final class zzdp implements Runnable {
    private final /* synthetic */ boolean zzaru;
    private final /* synthetic */ zzdn zzarv;
    private final /* synthetic */ zzdn zzarw;
    private final /* synthetic */ zzdo zzarx;

    zzdp(zzdo com_google_android_gms_measurement_internal_zzdo, boolean z, zzdn com_google_android_gms_measurement_internal_zzdn, zzdn com_google_android_gms_measurement_internal_zzdn2) {
        this.zzarx = com_google_android_gms_measurement_internal_zzdo;
        this.zzaru = z;
        this.zzarv = com_google_android_gms_measurement_internal_zzdn;
        this.zzarw = com_google_android_gms_measurement_internal_zzdn2;
    }

    public final void run() {
        if (this.zzaru && this.zzarx.zzaro != null) {
            this.zzarx.zza(this.zzarx.zzaro);
        }
        boolean z = (this.zzarv != null && this.zzarv.zzarm == this.zzarw.zzarm && zzfk.zzu(this.zzarv.zzarl, this.zzarw.zzarl) && zzfk.zzu(this.zzarv.zzuw, this.zzarw.zzuw)) ? false : true;
        if (z) {
            Bundle bundle = new Bundle();
            zzdo.zza(this.zzarw, bundle, true);
            if (this.zzarv != null) {
                if (this.zzarv.zzuw != null) {
                    bundle.putString("_pn", this.zzarv.zzuw);
                }
                bundle.putString("_pc", this.zzarv.zzarl);
                bundle.putLong("_pi", this.zzarv.zzarm);
            }
            this.zzarx.zzge().zza("auto", "_vs", bundle);
        }
        this.zzarx.zzaro = this.zzarw;
        this.zzarx.zzgg().zzb(this.zzarw);
    }
}
