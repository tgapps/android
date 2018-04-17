package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzki extends zzabd<zzki> {
    private static volatile zzki[] zzasu;
    public Integer count;
    public String name;
    public zzkj[] zzasv;
    public Long zzasw;
    public Long zzasx;

    public zzki() {
        this.zzasv = zzkj.zzle();
        this.name = null;
        this.zzasw = null;
        this.zzasx = null;
        this.count = null;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzki[] zzld() {
        if (zzasu == null) {
            synchronized (zzabh.zzbzr) {
                if (zzasu == null) {
                    zzasu = new zzki[0];
                }
            }
        }
        return zzasu;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzki)) {
            return false;
        }
        zzki com_google_android_gms_internal_measurement_zzki = (zzki) obj;
        if (!zzabh.equals(this.zzasv, com_google_android_gms_internal_measurement_zzki.zzasv)) {
            return false;
        }
        if (this.name == null) {
            if (com_google_android_gms_internal_measurement_zzki.name != null) {
                return false;
            }
        } else if (!this.name.equals(com_google_android_gms_internal_measurement_zzki.name)) {
            return false;
        }
        if (this.zzasw == null) {
            if (com_google_android_gms_internal_measurement_zzki.zzasw != null) {
                return false;
            }
        } else if (!this.zzasw.equals(com_google_android_gms_internal_measurement_zzki.zzasw)) {
            return false;
        }
        if (this.zzasx == null) {
            if (com_google_android_gms_internal_measurement_zzki.zzasx != null) {
                return false;
            }
        } else if (!this.zzasx.equals(com_google_android_gms_internal_measurement_zzki.zzasx)) {
            return false;
        }
        if (this.count == null) {
            if (com_google_android_gms_internal_measurement_zzki.count != null) {
                return false;
            }
        } else if (!this.count.equals(com_google_android_gms_internal_measurement_zzki.count)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzki.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzki.zzbzh == null || com_google_android_gms_internal_measurement_zzki.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((527 + getClass().getName().hashCode()) * 31) + zzabh.hashCode(this.zzasv)) * 31) + (this.name == null ? 0 : this.name.hashCode())) * 31) + (this.zzasw == null ? 0 : this.zzasw.hashCode())) * 31) + (this.zzasx == null ? 0 : this.zzasx.hashCode())) * 31) + (this.count == null ? 0 : this.count.hashCode())) * 31;
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                i = this.zzbzh.hashCode();
            }
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzasv != null && this.zzasv.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzasv) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    zza += zzabb.zzb(1, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        if (this.name != null) {
            zza += zzabb.zzd(2, this.name);
        }
        if (this.zzasw != null) {
            zza += zzabb.zzc(3, this.zzasw.longValue());
        }
        if (this.zzasx != null) {
            zza += zzabb.zzc(4, this.zzasx.longValue());
        }
        return this.count != null ? zza + zzabb.zzf(5, this.count.intValue()) : zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzasv != null && this.zzasv.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzasv) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(1, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        if (this.name != null) {
            com_google_android_gms_internal_measurement_zzabb.zzc(2, this.name);
        }
        if (this.zzasw != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(3, this.zzasw.longValue());
        }
        if (this.zzasx != null) {
            com_google_android_gms_internal_measurement_zzabb.zzb(4, this.zzasx.longValue());
        }
        if (this.count != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(5, this.count.intValue());
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
                int length = this.zzasv == null ? 0 : this.zzasv.length;
                Object obj = new zzkj[(zzvo + length)];
                if (length != 0) {
                    System.arraycopy(this.zzasv, 0, obj, 0, length);
                }
                while (length < obj.length - 1) {
                    obj[length] = new zzkj();
                    com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                    com_google_android_gms_internal_measurement_zzaba.zzvo();
                    length++;
                }
                obj[length] = new zzkj();
                com_google_android_gms_internal_measurement_zzaba.zza(obj[length]);
                this.zzasv = obj;
            } else if (zzvo == 18) {
                this.name = com_google_android_gms_internal_measurement_zzaba.readString();
            } else if (zzvo == 24) {
                this.zzasw = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
            } else if (zzvo == 32) {
                this.zzasx = Long.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvt());
            } else if (zzvo == 40) {
                this.count = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
            } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                return this;
            }
        }
    }
}
