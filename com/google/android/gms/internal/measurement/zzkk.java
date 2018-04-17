package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkk extends zzabd<zzkk> {
    public zzkl[] zzata;

    public zzkk() {
        this.zzata = zzkl.zzlf();
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkk)) {
            return false;
        }
        zzkk com_google_android_gms_internal_measurement_zzkk = (zzkk) obj;
        if (!zzabh.equals(this.zzata, com_google_android_gms_internal_measurement_zzkk.zzata)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkk.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzkk.zzbzh == null || com_google_android_gms_internal_measurement_zzkk.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int hashCode;
        int hashCode2 = (((527 + getClass().getName().hashCode()) * 31) + zzabh.hashCode(this.zzata)) * 31;
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                hashCode = this.zzbzh.hashCode();
                return hashCode2 + hashCode;
            }
        }
        hashCode = 0;
        return hashCode2 + hashCode;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzata != null && this.zzata.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzata) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    zza += zzabb.zzb(1, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        return zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzata != null && this.zzata.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzata) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(1, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            if (zzvo == 0) {
                return this;
            }
            if (zzvo == 10) {
                zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 10);
                int length = this.zzata == null ? 0 : this.zzata.length;
                Object obj = new zzkl[(zzvo + length)];
                if (length != 0) {
                    System.arraycopy(this.zzata, 0, obj, 0, length);
                }
                while (length < obj.length - 1) {
                    obj[length] = new zzkl();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                    com_google_android_gms_internal_measurement_zzaba.zzvo();
                    length++;
                }
                obj[length] = new zzkl();
                com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                this.zzata = obj;
            } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                return this;
            }
        }
    }
}
