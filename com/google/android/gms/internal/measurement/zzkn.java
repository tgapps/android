package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkn extends zzabd<zzkn> {
    private static volatile zzkn[] zzauh;
    public String name;
    public String zzajf;
    private Float zzaqw;
    public Double zzaqx;
    public Long zzasz;
    public Long zzaui;

    public zzkn() {
        this.zzaui = null;
        this.name = null;
        this.zzajf = null;
        this.zzasz = null;
        this.zzaqw = null;
        this.zzaqx = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzkn[] zzlg() {
        if (zzauh == null) {
            synchronized (zzabh.zzbzr) {
                if (zzauh == null) {
                    zzauh = new zzkn[0];
                }
            }
        }
        return zzauh;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkn)) {
            return false;
        }
        zzkn com_google_android_gms_internal_measurement_zzkn = (zzkn) obj;
        if (this.zzaui == null) {
            if (com_google_android_gms_internal_measurement_zzkn.zzaui != null) {
                return false;
            }
        } else if (!this.zzaui.equals(com_google_android_gms_internal_measurement_zzkn.zzaui)) {
            return false;
        }
        if (this.name == null) {
            if (com_google_android_gms_internal_measurement_zzkn.name != null) {
                return false;
            }
        } else if (!this.name.equals(com_google_android_gms_internal_measurement_zzkn.name)) {
            return false;
        }
        if (this.zzajf == null) {
            if (com_google_android_gms_internal_measurement_zzkn.zzajf != null) {
                return false;
            }
        } else if (!this.zzajf.equals(com_google_android_gms_internal_measurement_zzkn.zzajf)) {
            return false;
        }
        if (this.zzasz == null) {
            if (com_google_android_gms_internal_measurement_zzkn.zzasz != null) {
                return false;
            }
        } else if (!this.zzasz.equals(com_google_android_gms_internal_measurement_zzkn.zzasz)) {
            return false;
        }
        if (this.zzaqw == null) {
            if (com_google_android_gms_internal_measurement_zzkn.zzaqw != null) {
                return false;
            }
        } else if (!this.zzaqw.equals(com_google_android_gms_internal_measurement_zzkn.zzaqw)) {
            return false;
        }
        if (this.zzaqx == null) {
            if (com_google_android_gms_internal_measurement_zzkn.zzaqx != null) {
                return false;
            }
        } else if (!this.zzaqx.equals(com_google_android_gms_internal_measurement_zzkn.zzaqx)) {
            return false;
        }
        return (this.zzbzh == null || this.zzbzh.isEmpty()) ? com_google_android_gms_internal_measurement_zzkn.zzbzh == null || com_google_android_gms_internal_measurement_zzkn.zzbzh.isEmpty() : this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkn.zzbzh);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzaqx == null ? 0 : this.zzaqx.hashCode()) + (((this.zzaqw == null ? 0 : this.zzaqw.hashCode()) + (((this.zzasz == null ? 0 : this.zzasz.hashCode()) + (((this.zzajf == null ? 0 : this.zzajf.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + (((this.zzaui == null ? 0 : this.zzaui.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzbzh == null || this.zzbzh.isEmpty())) {
            i = this.zzbzh.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzaui != null) {
            zza += zzabb.zzc(1, this.zzaui.longValue());
        }
        if (this.name != null) {
            zza += zzabb.zzd(2, this.name);
        }
        if (this.zzajf != null) {
            zza += zzabb.zzd(3, this.zzajf);
        }
        if (this.zzasz != null) {
            zza += zzabb.zzc(4, this.zzasz.longValue());
        }
        if (this.zzaqw != null) {
            this.zzaqw.floatValue();
            zza += zzabb.zzas(5) + 4;
        }
        if (this.zzaqx == null) {
            return zza;
        }
        this.zzaqx.doubleValue();
        return zza + (zzabb.zzas(6) + 8);
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzaui != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(1, this.zzaui.longValue());
        }
        if (this.name != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(2, this.name);
        }
        if (this.zzajf != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(3, this.zzajf);
        }
        if (this.zzasz != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(4, this.zzasz.longValue());
        }
        if (this.zzaqw != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(5, this.zzaqw.floatValue());
        }
        if (this.zzaqx != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(6, this.zzaqx.doubleValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            switch (zzvo) {
                case 0:
                    break;
                case 8:
                    this.zzaui = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    continue;
                case 18:
                    this.name = com_google_android_gms_internal_measurement_zzaba.readString();
                    continue;
                case 26:
                    this.zzajf = com_google_android_gms_internal_measurement_zzaba.readString();
                    continue;
                case 32:
                    this.zzasz = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
                    continue;
                case 45:
                    this.zzaqw = Float.valueOf(Float.intBitsToFloat(com_google_android_gms_internal_measurement_zzaba.zzvu()));
                    continue;
                case 49:
                    this.zzaqx = Double.valueOf(Double.longBitsToDouble(com_google_android_gms_internal_measurement_zzaba.zzvv()));
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
