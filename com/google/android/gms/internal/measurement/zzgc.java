package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzgc extends zzza<zzgc> {
    private static volatile zzgc[] zzawk;
    public String value;
    public String zzoj;

    public static zzgc[] zzmn() {
        if (zzawk == null) {
            synchronized (zzze.zzcfl) {
                if (zzawk == null) {
                    zzawk = new zzgc[0];
                }
            }
        }
        return zzawk;
    }

    public zzgc() {
        this.zzoj = null;
        this.value = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgc)) {
            return false;
        }
        zzgc com_google_android_gms_internal_measurement_zzgc = (zzgc) obj;
        if (this.zzoj == null) {
            if (com_google_android_gms_internal_measurement_zzgc.zzoj != null) {
                return false;
            }
        } else if (!this.zzoj.equals(com_google_android_gms_internal_measurement_zzgc.zzoj)) {
            return false;
        }
        if (this.value == null) {
            if (com_google_android_gms_internal_measurement_zzgc.value != null) {
                return false;
            }
        } else if (!this.value.equals(com_google_android_gms_internal_measurement_zzgc.value)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgc.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgc.zzcfc == null || com_google_android_gms_internal_measurement_zzgc.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.value == null ? 0 : this.value.hashCode()) + (((this.zzoj == null ? 0 : this.zzoj.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzoj != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(1, this.zzoj);
        }
        if (this.value != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(2, this.value);
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzoj != null) {
            zzf += zzyy.zzc(1, this.zzoj);
        }
        if (this.value != null) {
            return zzf + zzyy.zzc(2, this.value);
        }
        return zzf;
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            switch (zzug) {
                case 0:
                    break;
                case 10:
                    this.zzoj = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 18:
                    this.value = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzyx, zzug)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
