package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.messenger.exoplayer2.RendererCapabilities;

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
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            int position;
            switch (zzvo) {
                case 0:
                    break;
                case 8:
                    position = com_google_android_gms_internal_measurement_zzaba.getPosition();
                    try {
                        int zzvs = com_google_android_gms_internal_measurement_zzaba.zzvs();
                        if (zzvs < 0 || zzvs > 6) {
                            throw new IllegalArgumentException(zzvs + " is not a valid enum MatchType");
                        }
                        this.zzasc = Integer.valueOf(zzvs);
                        continue;
                    } catch (IllegalArgumentException e) {
                        com_google_android_gms_internal_measurement_zzaba.zzao(position);
                        zza(com_google_android_gms_internal_measurement_zzaba, zzvo);
                        break;
                    }
                case 18:
                    this.zzasd = com_google_android_gms_internal_measurement_zzaba.readString();
                    continue;
                case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                    this.zzase = Boolean.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvr());
                    continue;
                case 34:
                    position = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 34);
                    zzvo = this.zzasf == null ? 0 : this.zzasf.length;
                    Object obj = new String[(position + zzvo)];
                    if (zzvo != 0) {
                        System.arraycopy(this.zzasf, 0, obj, 0, zzvo);
                    }
                    while (zzvo < obj.length - 1) {
                        obj[zzvo] = com_google_android_gms_internal_measurement_zzaba.readString();
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        zzvo++;
                    }
                    obj[zzvo] = com_google_android_gms_internal_measurement_zzaba.readString();
                    this.zzasf = obj;
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
        return !zzabh.equals(this.zzasf, com_google_android_gms_internal_measurement_zzkd.zzasf) ? false : (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzkd.zzbzh == null || com_google_android_gms_internal_measurement_zzkd.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkd.zzbzh);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((this.zzase == null ? 0 : this.zzase.hashCode()) + (((this.zzasd == null ? 0 : this.zzasd.hashCode()) + (((this.zzasc == null ? 0 : this.zzasc.intValue()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31) + zzabh.hashCode(this.zzasf)) * 31;
        if (!(this.zzbzh == null || this.zzbzh.isEmpty())) {
            i = this.zzbzh.hashCode();
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
        int i3 = 0;
        while (i < this.zzasf.length) {
            int zzfp;
            String str = this.zzasf[i];
            if (str != null) {
                i3++;
                zzfp = zzabb.zzfp(str) + i2;
            } else {
                zzfp = i2;
            }
            i++;
            i2 = zzfp;
        }
        return (zza + i2) + (i3 * 1);
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
