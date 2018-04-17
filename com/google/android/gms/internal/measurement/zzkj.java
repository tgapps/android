package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkj extends zzabd<zzkj> {
    private static volatile zzkj[] zzasy;
    public String name;
    public String zzajf;
    private Float zzaqw;
    public Double zzaqx;
    public Long zzasz;

    public zzkj() {
        this.name = null;
        this.zzajf = null;
        this.zzasz = null;
        this.zzaqw = null;
        this.zzaqx = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzkj[] zzle() {
        if (zzasy == null) {
            synchronized (zzabh.zzbzr) {
                if (zzasy == null) {
                    zzasy = new zzkj[0];
                }
            }
        }
        return zzasy;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkj)) {
            return false;
        }
        zzkj com_google_android_gms_internal_measurement_zzkj = (zzkj) obj;
        if (this.name == null) {
            if (com_google_android_gms_internal_measurement_zzkj.name != null) {
                return false;
            }
        } else if (!this.name.equals(com_google_android_gms_internal_measurement_zzkj.name)) {
            return false;
        }
        if (this.zzajf == null) {
            if (com_google_android_gms_internal_measurement_zzkj.zzajf != null) {
                return false;
            }
        } else if (!this.zzajf.equals(com_google_android_gms_internal_measurement_zzkj.zzajf)) {
            return false;
        }
        if (this.zzasz == null) {
            if (com_google_android_gms_internal_measurement_zzkj.zzasz != null) {
                return false;
            }
        } else if (!this.zzasz.equals(com_google_android_gms_internal_measurement_zzkj.zzasz)) {
            return false;
        }
        if (this.zzaqw == null) {
            if (com_google_android_gms_internal_measurement_zzkj.zzaqw != null) {
                return false;
            }
        } else if (!this.zzaqw.equals(com_google_android_gms_internal_measurement_zzkj.zzaqw)) {
            return false;
        }
        if (this.zzaqx == null) {
            if (com_google_android_gms_internal_measurement_zzkj.zzaqx != null) {
                return false;
            }
        } else if (!this.zzaqx.equals(com_google_android_gms_internal_measurement_zzkj.zzaqx)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkj.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzkj.zzbzh == null || com_google_android_gms_internal_measurement_zzkj.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((527 + getClass().getName().hashCode()) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzajf == null ? 0 : this.zzajf.hashCode())) * 31) + (this.zzasz == null ? 0 : this.zzasz.hashCode())) * 31) + (this.zzaqw == null ? 0 : this.zzaqw.hashCode())) * 31) + (this.zzaqx == null ? 0 : this.zzaqx.hashCode())) * 31;
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
        if (this.zzajf != null) {
            zza += zzabb.zzd(2, this.zzajf);
        }
        if (this.zzasz != null) {
            zza += zzabb.zzc(3, this.zzasz.longValue());
        }
        if (this.zzaqw != null) {
            this.zzaqw.floatValue();
            zza += zzabb.zzas(4) + 4;
        }
        if (this.zzaqx == null) {
            return zza;
        }
        this.zzaqx.doubleValue();
        return zza + (zzabb.zzas(5) + 8);
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.name != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(1, this.name);
        }
        if (this.zzajf != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(2, this.zzajf);
        }
        if (this.zzasz != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(3, this.zzasz.longValue());
        }
        if (this.zzaqw != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(4, this.zzaqw.floatValue());
        }
        if (this.zzaqx != null) {
            com_google_android_gms_internal_measurement_zzabb.zza(5, this.zzaqx.doubleValue());
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
            } else if (zzvo == 18) {
                this.zzajf = com_google_android_gms_internal_measurement_zzaba.readString();
            } else if (zzvo == 24) {
                this.zzasz = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
            } else if (zzvo == 37) {
                this.zzaqw = Float.valueOf(Float.intBitsToFloat(com_google_android_gms_internal_measurement_zzaba.zzvu()));
            } else if (zzvo == 41) {
                this.zzaqx = Double.valueOf(Double.longBitsToDouble(com_google_android_gms_internal_measurement_zzaba.zzvv()));
            } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                return this;
            }
        }
    }
}
