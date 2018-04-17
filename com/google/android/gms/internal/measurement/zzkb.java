package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkb extends zzabd<zzkb> {
    public Integer zzaru;
    public Boolean zzarv;
    public String zzarw;
    public String zzarx;
    public String zzary;

    public zzkb() {
        this.zzaru = null;
        this.zzarv = null;
        this.zzarw = null;
        this.zzarx = null;
        this.zzary = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    private final zzkb zzd(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        int zzvs;
        StringBuilder stringBuilder;
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            if (zzvo == 0) {
                return this;
            }
            if (zzvo == 8) {
                try {
                    zzvs = com_google_android_gms_internal_measurement_zzaba.zzvs();
                    if (zzvs < 0 || zzvs > 4) {
                        stringBuilder = new StringBuilder(46);
                        stringBuilder.append(zzvs);
                        stringBuilder.append(" is not a valid enum ComparisonType");
                    } else {
                        this.zzaru = Integer.valueOf(zzvs);
                    }
                } catch (IllegalArgumentException e) {
                    com_google_android_gms_internal_measurement_zzaba.zzao(com_google_android_gms_internal_measurement_zzaba.getPosition());
                    zza(com_google_android_gms_internal_measurement_zzaba, zzvo);
                }
            } else if (zzvo == 16) {
                this.zzarv = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
            } else if (zzvo == 26) {
                this.zzarw = com_google_android_gms_internal_measurement_zzaba.readString();
            } else if (zzvo == 34) {
                this.zzarx = com_google_android_gms_internal_measurement_zzaba.readString();
            } else if (zzvo == 42) {
                this.zzary = com_google_android_gms_internal_measurement_zzaba.readString();
            } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                return this;
            }
        }
        stringBuilder = new StringBuilder(46);
        stringBuilder.append(zzvs);
        stringBuilder.append(" is not a valid enum ComparisonType");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkb)) {
            return false;
        }
        zzkb com_google_android_gms_internal_measurement_zzkb = (zzkb) obj;
        if (this.zzaru == null) {
            if (com_google_android_gms_internal_measurement_zzkb.zzaru != null) {
                return false;
            }
        } else if (!this.zzaru.equals(com_google_android_gms_internal_measurement_zzkb.zzaru)) {
            return false;
        }
        if (this.zzarv == null) {
            if (com_google_android_gms_internal_measurement_zzkb.zzarv != null) {
                return false;
            }
        } else if (!this.zzarv.equals(com_google_android_gms_internal_measurement_zzkb.zzarv)) {
            return false;
        }
        if (this.zzarw == null) {
            if (com_google_android_gms_internal_measurement_zzkb.zzarw != null) {
                return false;
            }
        } else if (!this.zzarw.equals(com_google_android_gms_internal_measurement_zzkb.zzarw)) {
            return false;
        }
        if (this.zzarx == null) {
            if (com_google_android_gms_internal_measurement_zzkb.zzarx != null) {
                return false;
            }
        } else if (!this.zzarx.equals(com_google_android_gms_internal_measurement_zzkb.zzarx)) {
            return false;
        }
        if (this.zzary == null) {
            if (com_google_android_gms_internal_measurement_zzkb.zzary != null) {
                return false;
            }
        } else if (!this.zzary.equals(com_google_android_gms_internal_measurement_zzkb.zzary)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkb.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzkb.zzbzh == null || com_google_android_gms_internal_measurement_zzkb.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((527 + getClass().getName().hashCode()) * 31) + (this.zzaru == null ? 0 : this.zzaru.intValue())) * 31) + (this.zzarv == null ? 0 : this.zzarv.hashCode())) * 31) + (this.zzarw == null ? 0 : this.zzarw.hashCode())) * 31) + (this.zzarx == null ? 0 : this.zzarx.hashCode())) * 31) + (this.zzary == null ? 0 : this.zzary.hashCode())) * 31;
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                i = this.zzbzh.hashCode();
            }
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzaru != null) {
            zza += zzabb.zzf(1, this.zzaru.intValue());
        }
        if (this.zzarv != null) {
            this.zzarv.booleanValue();
            zza += zzabb.zzas(2) + 1;
        }
        if (this.zzarw != null) {
            zza += zzabb.zzd(3, this.zzarw);
        }
        if (this.zzarx != null) {
            zza += zzabb.zzd(4, this.zzarx);
        }
        return this.zzary != null ? zza + zzabb.zzd(5, this.zzary) : zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzaru != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(1, this.zzaru.intValue());
        }
        if (this.zzarv != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(2, this.zzarv.booleanValue());
        }
        if (this.zzarw != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(3, this.zzarw);
        }
        if (this.zzarx != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(4, this.zzarx);
        }
        if (this.zzary != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(5, this.zzary);
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        return zzd(com_google_android_gms_internal_measurement_zzaba);
    }
}
