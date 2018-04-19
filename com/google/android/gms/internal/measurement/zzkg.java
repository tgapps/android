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
        return (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzkg.zzbzh == null || com_google_android_gms_internal_measurement_zzkg.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkg.zzbzh);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.value == null ? 0 : this.value.hashCode()) + (((this.zznt == null ? 0 : this.zznt.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
        if (!(this.zzbzh == null || this.zzbzh.isEmpty())) {
            i = this.zzbzh.hashCode();
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
            switch (zzvo) {
                case 0:
                    break;
                case 10:
                    this.zznt = com_google_android_gms_internal_measurement_zzaba.readString();
                    continue;
                case 18:
                    this.value = com_google_android_gms_internal_measurement_zzaba.readString();
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
