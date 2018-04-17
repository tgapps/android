package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkg extends zzabd<zzkg> {
    private static volatile zzkg[] zzasp;
    public String value;
    public String zznt;

    public zzkg() {
        this.zznt = null;
        this.value = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzkg[] zzlb() {
        if (zzasp == null) {
            synchronized (zzabh.zzbzr) {
                if (zzasp == null) {
                    zzasp = new zzkg[0];
                }
            }
        }
        return zzasp;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkg)) {
            return false;
        }
        zzkg com_google_android_gms_internal_measurement_zzkg = (zzkg) obj;
        if (this.zznt == null) {
            if (com_google_android_gms_internal_measurement_zzkg.zznt != null) {
                return false;
            }
        } else if (!this.zznt.equals(com_google_android_gms_internal_measurement_zzkg.zznt)) {
            return false;
        }
        if (this.value == null) {
            if (com_google_android_gms_internal_measurement_zzkg.value != null) {
                return false;
            }
        } else if (!this.value.equals(com_google_android_gms_internal_measurement_zzkg.value)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkg.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzkg.zzbzh == null || com_google_android_gms_internal_measurement_zzkg.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((527 + getClass().getName().hashCode()) * 31) + (this.zznt == null ? 0 : this.zznt.hashCode())) * 31) + (this.value == null ? 0 : this.value.hashCode())) * 31;
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                i = this.zzbzh.hashCode();
            }
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zznt != null) {
            zza += zzabb.zzd(1, this.zznt);
        }
        return this.value != null ? zza + zzabb.zzd(2, this.value) : zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zznt != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(1, this.zznt);
        }
        if (this.value != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(2, this.value);
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
                this.zznt = com_google_android_gms_internal_measurement_zzaba.readString();
            } else if (zzvo == 18) {
                this.value = com_google_android_gms_internal_measurement_zzaba.readString();
            } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                return this;
            }
        }
    }
}
