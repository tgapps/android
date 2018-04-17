package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzke extends zzabd<zzke> {
    private static volatile zzke[] zzasg;
    public String name;
    public Boolean zzash;
    public Boolean zzasi;
    public Integer zzasj;

    public zzke() {
        this.name = null;
        this.zzash = null;
        this.zzasi = null;
        this.zzasj = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzke[] zzla() {
        if (zzasg == null) {
            synchronized (zzabh.zzbzr) {
                if (zzasg == null) {
                    zzasg = new zzke[0];
                }
            }
        }
        return zzasg;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzke)) {
            return false;
        }
        zzke com_google_android_gms_internal_measurement_zzke = (zzke) obj;
        if (this.name == null) {
            if (com_google_android_gms_internal_measurement_zzke.name != null) {
                return false;
            }
        } else if (!this.name.equals(com_google_android_gms_internal_measurement_zzke.name)) {
            return false;
        }
        if (this.zzash == null) {
            if (com_google_android_gms_internal_measurement_zzke.zzash != null) {
                return false;
            }
        } else if (!this.zzash.equals(com_google_android_gms_internal_measurement_zzke.zzash)) {
            return false;
        }
        if (this.zzasi == null) {
            if (com_google_android_gms_internal_measurement_zzke.zzasi != null) {
                return false;
            }
        } else if (!this.zzasi.equals(com_google_android_gms_internal_measurement_zzke.zzasi)) {
            return false;
        }
        if (this.zzasj == null) {
            if (com_google_android_gms_internal_measurement_zzke.zzasj != null) {
                return false;
            }
        } else if (!this.zzasj.equals(com_google_android_gms_internal_measurement_zzke.zzasj)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzke.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzke.zzbzh == null || com_google_android_gms_internal_measurement_zzke.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((527 + getClass().getName().hashCode()) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzash == null ? 0 : this.zzash.hashCode())) * 31) + (this.zzasi == null ? 0 : this.zzasi.hashCode())) * 31) + (this.zzasj == null ? 0 : this.zzasj.hashCode())) * 31;
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                i = this.zzbzh.hashCode();
            }
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.name != null) {
            zza += zzabb.zzd(1, this.name);
        }
        if (this.zzash != null) {
            this.zzash.booleanValue();
            zza += zzabb.zzas(2) + 1;
        }
        if (this.zzasi != null) {
            this.zzasi.booleanValue();
            zza += zzabb.zzas(3) + 1;
        }
        return this.zzasj != null ? zza + zzabb.zzf(4, this.zzasj.intValue()) : zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.name != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(1, this.name);
        }
        if (this.zzash != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(2, this.zzash.booleanValue());
        }
        if (this.zzasi != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(3, this.zzasi.booleanValue());
        }
        if (this.zzasj != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(4, this.zzasj.intValue());
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
                this.name = com_google_android_gms_internal_measurement_zzaba.readString();
            } else if (zzvo == 16) {
                this.zzash = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
            } else if (zzvo == 24) {
                this.zzasi = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
            } else if (zzvo == 32) {
                this.zzasj = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
            } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                return this;
            }
        }
    }
}
