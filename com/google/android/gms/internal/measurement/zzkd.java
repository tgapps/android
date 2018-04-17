package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkd extends zzabd<zzkd> {
    public Integer zzasc;
    public String zzasd;
    public Boolean zzase;
    public String[] zzasf;

    public zzkd() {
        this.zzasc = null;
        this.zzasd = null;
        this.zzase = null;
        this.zzasf = zzabm.zzcac;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    private final zzkd zze(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
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
                    if (zzvs < 0 || zzvs > 6) {
                        stringBuilder = new StringBuilder(41);
                        stringBuilder.append(zzvs);
                        stringBuilder.append(" is not a valid enum MatchType");
                    } else {
                        this.zzasc = Integer.valueOf(zzvs);
                    }
                } catch (IllegalArgumentException e) {
                    com_google_android_gms_internal_measurement_zzaba.zzao(com_google_android_gms_internal_measurement_zzaba.getPosition());
                    zza(com_google_android_gms_internal_measurement_zzaba, zzvo);
                }
            } else if (zzvo == 18) {
                this.zzasd = com_google_android_gms_internal_measurement_zzaba.readString();
            } else if (zzvo == 24) {
                this.zzase = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
            } else if (zzvo == 34) {
                zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 34);
                int length = this.zzasf == null ? 0 : this.zzasf.length;
                Object obj = new String[(zzvo + length)];
                if (length != 0) {
                    System.arraycopy(this.zzasf, 0, obj, 0, length);
                }
                while (length < obj.length - 1) {
                    obj[length] = com_google_android_gms_internal_measurement_zzaba.readString();
                    com_google_android_gms_internal_measurement_zzaba.zzvo();
                    length++;
                }
                obj[length] = com_google_android_gms_internal_measurement_zzaba.readString();
                this.zzasf = obj;
            } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                return this;
            }
        }
        stringBuilder = new StringBuilder(41);
        stringBuilder.append(zzvs);
        stringBuilder.append(" is not a valid enum MatchType");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkd)) {
            return false;
        }
        zzkd com_google_android_gms_internal_measurement_zzkd = (zzkd) obj;
        if (this.zzasc == null) {
            if (com_google_android_gms_internal_measurement_zzkd.zzasc != null) {
                return false;
            }
        } else if (!this.zzasc.equals(com_google_android_gms_internal_measurement_zzkd.zzasc)) {
            return false;
        }
        if (this.zzasd == null) {
            if (com_google_android_gms_internal_measurement_zzkd.zzasd != null) {
                return false;
            }
        } else if (!this.zzasd.equals(com_google_android_gms_internal_measurement_zzkd.zzasd)) {
            return false;
        }
        if (this.zzase == null) {
            if (com_google_android_gms_internal_measurement_zzkd.zzase != null) {
                return false;
            }
        } else if (!this.zzase.equals(com_google_android_gms_internal_measurement_zzkd.zzase)) {
            return false;
        }
        if (!zzabh.equals(this.zzasf, com_google_android_gms_internal_measurement_zzkd.zzasf)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkd.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzkd.zzbzh == null || com_google_android_gms_internal_measurement_zzkd.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((527 + getClass().getName().hashCode()) * 31) + (this.zzasc == null ? 0 : this.zzasc.intValue())) * 31) + (this.zzasd == null ? 0 : this.zzasd.hashCode())) * 31) + (this.zzase == null ? 0 : this.zzase.hashCode())) * 31) + zzabh.hashCode(this.zzasf)) * 31;
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                i = this.zzbzh.hashCode();
            }
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzasc != null) {
            zza += zzabb.zzf(1, this.zzasc.intValue());
        }
        if (this.zzasd != null) {
            zza += zzabb.zzd(2, this.zzasd);
        }
        if (this.zzase != null) {
            this.zzase.booleanValue();
            zza += zzabb.zzas(3) + 1;
        }
        if (this.zzasf == null || this.zzasf.length <= 0) {
            return zza;
        }
        int i = 0;
        int i2 = 0;
        int i3 = i2;
        while (i < this.zzasf.length) {
            String str = this.zzasf[i];
            if (str != null) {
                i3++;
                i2 += zzabb.zzfp(str);
            }
            i++;
        }
        return (zza + i2) + (1 * i3);
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzasc != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(1, this.zzasc.intValue());
        }
        if (this.zzasd != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(2, this.zzasd);
        }
        if (this.zzase != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(3, this.zzase.booleanValue());
        }
        if (this.zzasf != null && this.zzasf.length > 0) {
            for (String str : this.zzasf) {
                if (str != null) {
                    com_google_android_gms_internal_measurement_zzabb.zzc(4, str);
                }
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        return zze(com_google_android_gms_internal_measurement_zzaba);
    }
}
