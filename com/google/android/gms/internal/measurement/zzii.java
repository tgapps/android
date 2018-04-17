package com.google.android.gms.internal.measurement;

import android.os.Bundle;

final class zzii implements Runnable {
    private final /* synthetic */ boolean zzapl;
    private final /* synthetic */ zzig zzapm;
    private final /* synthetic */ zzik zzapn;
    private final /* synthetic */ zzih zzapo;

    zzii(zzih com_google_android_gms_internal_measurement_zzih, boolean z, zzig com_google_android_gms_internal_measurement_zzig, zzik com_google_android_gms_internal_measurement_zzik) {
        this.zzapo = com_google_android_gms_internal_measurement_zzih;
        this.zzapl = z;
        this.zzapm = com_google_android_gms_internal_measurement_zzig;
        this.zzapn = com_google_android_gms_internal_measurement_zzik;
    }

    public final void run() {
        boolean z;
        Bundle bundle;
        if (this.zzapl && this.zzapo.zzapc != null) {
            this.zzapo.zza(this.zzapo.zzapc);
        }
        if (this.zzapm != null && this.zzapm.zzapb == this.zzapn.zzapb && zzjv.zzs(this.zzapm.zzapa, this.zzapn.zzapa)) {
            if (zzjv.zzs(this.zzapm.zzug, this.zzapn.zzug)) {
                z = false;
                if (z) {
                    bundle = new Bundle();
                    zzih.zza(this.zzapn, bundle, true);
                    if (this.zzapm != null) {
                        if (this.zzapm.zzug != null) {
                            bundle.putString("_pn", this.zzapm.zzug);
                        }
                        bundle.putString("_pc", this.zzapm.zzapa);
                        bundle.putLong("_pi", this.zzapm.zzapb);
                    }
                    this.zzapo.zzfu().zza("auto", "_vs", bundle);
                }
                this.zzapo.zzapc = this.zzapn;
                this.zzapo.zzfx().zza(this.zzapn);
            }
        }
        z = true;
        if (z) {
            bundle = new Bundle();
            zzih.zza(this.zzapn, bundle, true);
            if (this.zzapm != null) {
                if (this.zzapm.zzug != null) {
                    bundle.putString("_pn", this.zzapm.zzug);
                }
                bundle.putString("_pc", this.zzapm.zzapa);
                bundle.putLong("_pi", this.zzapm.zzapb);
            }
            this.zzapo.zzfu().zza("auto", "_vs", bundle);
        }
        this.zzapo.zzapc = this.zzapn;
        this.zzapo.zzfx().zza(this.zzapn);
    }
}
