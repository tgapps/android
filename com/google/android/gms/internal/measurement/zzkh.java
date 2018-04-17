package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkh extends zzabd<zzkh> {
    private static volatile zzkh[] zzasq;
    public Integer zzarg;
    public zzkm zzasr;
    public zzkm zzass;
    public Boolean zzast;

    public zzkh() {
        this.zzarg = null;
        this.zzasr = null;
        this.zzass = null;
        this.zzast = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzkh[] zzlc() {
        if (zzasq == null) {
            synchronized (zzabh.zzbzr) {
                if (zzasq == null) {
                    zzasq = new zzkh[0];
                }
            }
        }
        return zzasq;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkh)) {
            return false;
        }
        zzkh com_google_android_gms_internal_measurement_zzkh = (zzkh) obj;
        if (this.zzarg == null) {
            if (com_google_android_gms_internal_measurement_zzkh.zzarg != null) {
                return false;
            }
        } else if (!this.zzarg.equals(com_google_android_gms_internal_measurement_zzkh.zzarg)) {
            return false;
        }
        if (this.zzasr == null) {
            if (com_google_android_gms_internal_measurement_zzkh.zzasr != null) {
                return false;
            }
        } else if (!this.zzasr.equals(com_google_android_gms_internal_measurement_zzkh.zzasr)) {
            return false;
        }
        if (this.zzass == null) {
            if (com_google_android_gms_internal_measurement_zzkh.zzass != null) {
                return false;
            }
        } else if (!this.zzass.equals(com_google_android_gms_internal_measurement_zzkh.zzass)) {
            return false;
        }
        if (this.zzast == null) {
            if (com_google_android_gms_internal_measurement_zzkh.zzast != null) {
                return false;
            }
        } else if (!this.zzast.equals(com_google_android_gms_internal_measurement_zzkh.zzast)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkh.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzkh.zzbzh == null || com_google_android_gms_internal_measurement_zzkh.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((527 + getClass().getName().hashCode()) * 31) + (this.zzarg == null ? 0 : this.zzarg.hashCode());
        zzkm com_google_android_gms_internal_measurement_zzkm = this.zzasr;
        hashCode = (hashCode * 31) + (com_google_android_gms_internal_measurement_zzkm == null ? 0 : com_google_android_gms_internal_measurement_zzkm.hashCode());
        com_google_android_gms_internal_measurement_zzkm = this.zzass;
        hashCode = ((((hashCode * 31) + (com_google_android_gms_internal_measurement_zzkm == null ? 0 : com_google_android_gms_internal_measurement_zzkm.hashCode())) * 31) + (this.zzast == null ? 0 : this.zzast.hashCode())) * 31;
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                i = this.zzbzh.hashCode();
            }
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzarg != null) {
            zza += zzabb.zzf(1, this.zzarg.intValue());
        }
        if (this.zzasr != null) {
            zza += zzabb.zzb(2, this.zzasr);
        }
        if (this.zzass != null) {
            zza += zzabb.zzb(3, this.zzass);
        }
        if (this.zzast == null) {
            return zza;
        }
        this.zzast.booleanValue();
        return zza + (zzabb.zzas(4) + 1);
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzarg != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(1, this.zzarg.intValue());
        }
        if (this.zzasr != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(2, this.zzasr);
        }
        if (this.zzass != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(3, this.zzass);
        }
        if (this.zzast != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(4, this.zzast.booleanValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            if (zzvo == 0) {
                return this;
            }
            if (zzvo != 8) {
                zzabj com_google_android_gms_internal_measurement_zzabj;
                if (zzvo == 18) {
                    if (this.zzasr == null) {
                        this.zzasr = new zzkm();
                    }
                    com_google_android_gms_internal_measurement_zzabj = this.zzasr;
                } else if (zzvo == 26) {
                    if (this.zzass == null) {
                        this.zzass = new zzkm();
                    }
                    com_google_android_gms_internal_measurement_zzabj = this.zzass;
                } else if (zzvo == 32) {
                    this.zzast = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
                } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                    return this;
                }
                com_google_android_gms_internal_measurement_zzaba.zza(com_google_android_gms_internal_measurement_zzabj);
            } else {
                this.zzarg = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
            }
        }
    }
}
