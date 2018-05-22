package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkl extends zzaby<zzkl> {
    private static volatile zzkl[] zzasu;
    public String value;
    public String zzny;

    public zzkl() {
        this.zzny = null;
        this.value = null;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkl[] zzlj() {
        if (zzasu == null) {
            synchronized (zzacc.zzbxg) {
                if (zzasu == null) {
                    zzasu = new zzkl[0];
                }
            }
        }
        return zzasu;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkl)) {
            return false;
        }
        zzkl com_google_android_gms_internal_measurement_zzkl = (zzkl) obj;
        if (this.zzny == null) {
            if (com_google_android_gms_internal_measurement_zzkl.zzny != null) {
                return false;
            }
        } else if (!this.zzny.equals(com_google_android_gms_internal_measurement_zzkl.zzny)) {
            return false;
        }
        if (this.value == null) {
            if (com_google_android_gms_internal_measurement_zzkl.value != null) {
                return false;
            }
        } else if (!this.value.equals(com_google_android_gms_internal_measurement_zzkl.value)) {
            return false;
        }
        return (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzkl.zzbww == null || com_google_android_gms_internal_measurement_zzkl.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzkl.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.value == null ? 0 : this.value.hashCode()) + (((this.zzny == null ? 0 : this.zzny.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzny != null) {
            zza += zzabw.zzc(1, this.zzny);
        }
        return this.value != null ? zza + zzabw.zzc(2, this.value) : zza;
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        if (this.zzny != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(1, this.zzny);
        }
        if (this.value != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(2, this.value);
        }
        super.zza(com_google_android_gms_internal_measurement_zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        while (true) {
            int zzuw = com_google_android_gms_internal_measurement_zzabv.zzuw();
            switch (zzuw) {
                case 0:
                    break;
                case 10:
                    this.zzny = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 18:
                    this.value = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzabv, zzuw)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
