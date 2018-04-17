package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkm extends zzabd<zzkm> {
    public long[] zzauf;
    public long[] zzaug;

    public zzkm() {
        this.zzauf = zzabm.zzbzy;
        this.zzaug = zzabm.zzbzy;
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkm)) {
            return false;
        }
        zzkm com_google_android_gms_internal_measurement_zzkm = (zzkm) obj;
        if (!zzabh.equals(this.zzauf, com_google_android_gms_internal_measurement_zzkm.zzauf) || !zzabh.equals(this.zzaug, com_google_android_gms_internal_measurement_zzkm.zzaug)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzkm.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzkm.zzbzh == null || com_google_android_gms_internal_measurement_zzkm.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int hashCode;
        int hashCode2 = (((((527 + getClass().getName().hashCode()) * 31) + zzabh.hashCode(this.zzauf)) * 31) + zzabh.hashCode(this.zzaug)) * 31;
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                hashCode = this.zzbzh.hashCode();
                return hashCode2 + hashCode;
            }
        }
        hashCode = 0;
        return hashCode2 + hashCode;
    }

    protected final int zza() {
        int i;
        int zza = super.zza();
        int i2 = 0;
        if (this.zzauf != null && this.zzauf.length > 0) {
            i = 0;
            int i3 = i;
            while (i < this.zzauf.length) {
                i3 += zzabb.zzap(this.zzauf[i]);
                i++;
            }
            zza = (zza + i3) + (this.zzauf.length * 1);
        }
        if (this.zzaug == null || this.zzaug.length <= 0) {
            return zza;
        }
        i = 0;
        while (i2 < this.zzaug.length) {
            i += zzabb.zzap(this.zzaug[i2]);
            i2++;
        }
        return (zza + i) + (1 * this.zzaug.length);
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        int i = 0;
        if (this.zzauf != null && this.zzauf.length > 0) {
            for (long zza : this.zzauf) {
                com_google_android_gms_internal_measurement_zzabb.zza(1, zza);
            }
        }
        if (this.zzaug != null && this.zzaug.length > 0) {
            while (i < this.zzaug.length) {
                com_google_android_gms_internal_measurement_zzabb.zza(2, this.zzaug[i]);
                i++;
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabb);
    }

    public final /* synthetic */ zzabj zzb(zzaba com_google_android_gms_internal_measurement_zzaba) throws IOException {
        while (true) {
            int zzvo = com_google_android_gms_internal_measurement_zzaba.zzvo();
            if (zzvo == 0) {
                return this;
            }
            int position;
            Object obj;
            if (zzvo != 8) {
                int i;
                Object obj2;
                if (zzvo == 10) {
                    zzvo = com_google_android_gms_internal_measurement_zzaba.zzah(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    position = com_google_android_gms_internal_measurement_zzaba.getPosition();
                    i = 0;
                    while (com_google_android_gms_internal_measurement_zzaba.zzvw() > 0) {
                        com_google_android_gms_internal_measurement_zzaba.zzvt();
                        i++;
                    }
                    com_google_android_gms_internal_measurement_zzaba.zzao(position);
                    position = this.zzauf == null ? 0 : this.zzauf.length;
                    obj2 = new long[(i + position)];
                    if (position != 0) {
                        System.arraycopy(this.zzauf, 0, obj2, 0, position);
                    }
                    while (position < obj2.length) {
                        obj2[position] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                        position++;
                    }
                    this.zzauf = obj2;
                } else if (zzvo == 16) {
                    zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 16);
                    position = this.zzaug == null ? 0 : this.zzaug.length;
                    obj = new long[(zzvo + position)];
                    if (position != 0) {
                        System.arraycopy(this.zzaug, 0, obj, 0, position);
                    }
                    while (position < obj.length - 1) {
                        obj[position] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                        com_google_android_gms_internal_measurement_zzaba.zzvo();
                        position++;
                    }
                    obj[position] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                    this.zzaug = obj;
                } else if (zzvo == 18) {
                    zzvo = com_google_android_gms_internal_measurement_zzaba.zzah(com_google_android_gms_internal_measurement_zzaba.zzvs());
                    position = com_google_android_gms_internal_measurement_zzaba.getPosition();
                    i = 0;
                    while (com_google_android_gms_internal_measurement_zzaba.zzvw() > 0) {
                        com_google_android_gms_internal_measurement_zzaba.zzvt();
                        i++;
                    }
                    com_google_android_gms_internal_measurement_zzaba.zzao(position);
                    position = this.zzaug == null ? 0 : this.zzaug.length;
                    obj2 = new long[(i + position)];
                    if (position != 0) {
                        System.arraycopy(this.zzaug, 0, obj2, 0, position);
                    }
                    while (position < obj2.length) {
                        obj2[position] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                        position++;
                    }
                    this.zzaug = obj2;
                } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                    return this;
                }
                com_google_android_gms_internal_measurement_zzaba.zzan(zzvo);
            } else {
                zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 8);
                position = this.zzauf == null ? 0 : this.zzauf.length;
                obj = new long[(zzvo + position)];
                if (position != 0) {
                    System.arraycopy(this.zzauf, 0, obj, 0, position);
                }
                while (position < obj.length - 1) {
                    obj[position] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                    com_google_android_gms_internal_measurement_zzaba.zzvo();
                    position++;
                }
                obj[position] = com_google_android_gms_internal_measurement_zzaba.zzvt();
                this.zzauf = obj;
            }
        }
    }
}
