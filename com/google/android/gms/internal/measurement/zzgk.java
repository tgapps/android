package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzgk extends zzza<zzgk> {
    private static volatile zzgk[] zzayi;
    public Integer zzawq;
    public long[] zzayj;

    public static zzgk[] zzmt() {
        if (zzayi == null) {
            synchronized (zzze.zzcfl) {
                if (zzayi == null) {
                    zzayi = new zzgk[0];
                }
            }
        }
        return zzayi;
    }

    public zzgk() {
        this.zzawq = null;
        this.zzayj = zzzj.zzcfr;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgk)) {
            return false;
        }
        zzgk com_google_android_gms_internal_measurement_zzgk = (zzgk) obj;
        if (this.zzawq == null) {
            if (com_google_android_gms_internal_measurement_zzgk.zzawq != null) {
                return false;
            }
        } else if (!this.zzawq.equals(com_google_android_gms_internal_measurement_zzgk.zzawq)) {
            return false;
        }
        if (!zzze.equals(this.zzayj, com_google_android_gms_internal_measurement_zzgk.zzayj)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgk.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgk.zzcfc == null || com_google_android_gms_internal_measurement_zzgk.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((((this.zzawq == null ? 0 : this.zzawq.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31) + zzze.hashCode(this.zzayj)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzawq != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(1, this.zzawq.intValue());
        }
        if (this.zzayj != null && this.zzayj.length > 0) {
            for (long zzi : this.zzayj) {
                com_google_android_gms_internal_measurement_zzyy.zzi(2, zzi);
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzawq != null) {
            zzf += zzyy.zzh(1, this.zzawq.intValue());
        }
        if (this.zzayj == null || this.zzayj.length <= 0) {
            return zzf;
        }
        int i = 0;
        int i2 = 0;
        while (i < this.zzayj.length) {
            i++;
            i2 = zzyy.zzbi(this.zzayj[i]) + i2;
        }
        return (zzf + i2) + (this.zzayj.length * 1);
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            int zzb;
            switch (zzug) {
                case 0:
                    break;
                case 8:
                    this.zzawq = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 16:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 16);
                    if (this.zzayj == null) {
                        zzug = 0;
                    } else {
                        zzug = this.zzayj.length;
                    }
                    Object obj = new long[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzayj, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = com_google_android_gms_internal_measurement_zzyx.zzuz();
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = com_google_android_gms_internal_measurement_zzyx.zzuz();
                    this.zzayj = obj;
                    continue;
                case 18:
                    int zzaq = com_google_android_gms_internal_measurement_zzyx.zzaq(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    zzb = com_google_android_gms_internal_measurement_zzyx.getPosition();
                    zzug = 0;
                    while (com_google_android_gms_internal_measurement_zzyx.zzyr() > 0) {
                        com_google_android_gms_internal_measurement_zzyx.zzuz();
                        zzug++;
                    }
                    com_google_android_gms_internal_measurement_zzyx.zzby(zzb);
                    zzb = this.zzayj == null ? 0 : this.zzayj.length;
                    Object obj2 = new long[(zzug + zzb)];
                    if (zzb != 0) {
                        System.arraycopy(this.zzayj, 0, obj2, 0, zzb);
                    }
                    while (zzb < obj2.length) {
                        obj2[zzb] = com_google_android_gms_internal_measurement_zzyx.zzuz();
                        zzb++;
                    }
                    this.zzayj = obj2;
                    com_google_android_gms_internal_measurement_zzyx.zzar(zzaq);
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzyx, zzug)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
