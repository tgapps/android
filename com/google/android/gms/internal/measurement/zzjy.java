package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzjy extends zzabd<zzjy> {
    private static volatile zzjy[] zzarf;
    public Integer zzarg;
    public zzkc[] zzarh;
    public zzjz[] zzari;

    public zzjy() {
        this.zzarg = null;
        this.zzarh = zzkc.zzkz();
        this.zzari = zzjz.zzkx();
        this.zzbzh = null;
        this.zzbzs = -1;
    }

    public static zzjy[] zzkw() {
        if (zzarf == null) {
            synchronized (zzabh.zzbzr) {
                if (zzarf == null) {
                    zzarf = new zzjy[0];
                }
            }
        }
        return zzarf;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzjy)) {
            return false;
        }
        zzjy com_google_android_gms_internal_measurement_zzjy = (zzjy) obj;
        if (this.zzarg == null) {
            if (com_google_android_gms_internal_measurement_zzjy.zzarg != null) {
                return false;
            }
        } else if (!this.zzarg.equals(com_google_android_gms_internal_measurement_zzjy.zzarg)) {
            return false;
        }
        if (!zzabh.equals(this.zzarh, com_google_android_gms_internal_measurement_zzjy.zzarh) || !zzabh.equals(this.zzari, com_google_android_gms_internal_measurement_zzjy.zzari)) {
            return false;
        }
        if (this.zzbzh != null) {
            if (!this.zzbzh.isEmpty()) {
                return this.zzbzh.equals(com_google_android_gms_internal_measurement_zzjy.zzbzh);
            }
        }
        return com_google_android_gms_internal_measurement_zzjy.zzbzh == null || com_google_android_gms_internal_measurement_zzjy.zzbzh.isEmpty();
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((527 + getClass().getName().hashCode()) * 31) + (this.zzarg == null ? 0 : this.zzarg.hashCode())) * 31) + zzabh.hashCode(this.zzarh)) * 31) + zzabh.hashCode(this.zzari)) * 31;
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
        int i = 0;
        if (this.zzarh != null && this.zzarh.length > 0) {
            int i2 = zza;
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzarh) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    i2 += zzabb.zzb(2, com_google_android_gms_internal_measurement_zzabj);
                }
            }
            zza = i2;
        }
        if (this.zzari != null && this.zzari.length > 0) {
            while (i < this.zzari.length) {
                zzabj com_google_android_gms_internal_measurement_zzabj2 = this.zzari[i];
                if (com_google_android_gms_internal_measurement_zzabj2 != null) {
                    zza += zzabb.zzb(3, com_google_android_gms_internal_measurement_zzabj2);
                }
                i++;
            }
        }
        return zza;
    }

    public final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.zzarg != null) {
            com_google_android_gms_internal_measurement_zzabb.zze(1, this.zzarg.intValue());
        }
        int i = 0;
        if (this.zzarh != null && this.zzarh.length > 0) {
            for (zzabj com_google_android_gms_internal_measurement_zzabj : this.zzarh) {
                if (com_google_android_gms_internal_measurement_zzabj != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(2, com_google_android_gms_internal_measurement_zzabj);
                }
            }
        }
        if (this.zzari != null && this.zzari.length > 0) {
            while (i < this.zzari.length) {
                zzabj com_google_android_gms_internal_measurement_zzabj2 = this.zzari[i];
                if (com_google_android_gms_internal_measurement_zzabj2 != null) {
                    com_google_android_gms_internal_measurement_zzabb.zza(3, com_google_android_gms_internal_measurement_zzabj2);
                }
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
            if (zzvo == 8) {
                this.zzarg = Integer.valueOf(com_google_android_gms_internal_measurement_zzaba.zzvs());
            } else if (zzvo == 18) {
                zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 18);
                r1 = this.zzarh == null ? 0 : this.zzarh.length;
                r0 = new zzkc[(zzvo + r1)];
                if (r1 != 0) {
                    System.arraycopy(this.zzarh, 0, r0, 0, r1);
                }
                while (r1 < r0.length - 1) {
                    r0[r1] = new zzkc();
                    com_google_android_gms_internal_measurement_zzaba.zza(r0[r1]);
                    com_google_android_gms_internal_measurement_zzaba.zzvo();
                    r1++;
                }
                r0[r1] = new zzkc();
                com_google_android_gms_internal_measurement_zzaba.zza(r0[r1]);
                this.zzarh = r0;
            } else if (zzvo == 26) {
                zzvo = zzabm.zzb(com_google_android_gms_internal_measurement_zzaba, 26);
                r1 = this.zzari == null ? 0 : this.zzari.length;
                r0 = new zzjz[(zzvo + r1)];
                if (r1 != 0) {
                    System.arraycopy(this.zzari, 0, r0, 0, r1);
                }
                while (r1 < r0.length - 1) {
                    r0[r1] = new zzjz();
                    com_google_android_gms_internal_measurement_zzaba.zza(r0[r1]);
                    com_google_android_gms_internal_measurement_zzaba.zzvo();
                    r1++;
                }
                r0[r1] = new zzjz();
                com_google_android_gms_internal_measurement_zzaba.zza(r0[r1]);
                this.zzari = r0;
            } else if (!super.zza(com_google_android_gms_internal_measurement_zzaba, zzvo)) {
                return this;
            }
        }
    }
}
