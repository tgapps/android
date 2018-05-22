package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzkr extends zzaby<zzkr> {
    public long[] zzauk;
    public long[] zzaul;

    public zzkr() {
        this.zzauk = zzach.zzbxm;
        this.zzaul = zzach.zzbxm;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzkr)) {
            return false;
        }
        zzkr com_google_android_gms_internal_measurement_zzkr = (zzkr) obj;
        return !zzacc.equals(this.zzauk, com_google_android_gms_internal_measurement_zzkr.zzauk) ? false : !zzacc.equals(this.zzaul, com_google_android_gms_internal_measurement_zzkr.zzaul) ? false : (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzkr.zzbww == null || com_google_android_gms_internal_measurement_zzkr.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzkr.zzbww);
    }

    public final int hashCode() {
        int hashCode = (((((getClass().getName().hashCode() + 527) * 31) + zzacc.hashCode(this.zzauk)) * 31) + zzacc.hashCode(this.zzaul)) * 31;
        int hashCode2 = (this.zzbww == null || this.zzbww.isEmpty()) ? 0 : this.zzbww.hashCode();
        return hashCode2 + hashCode;
    }

    protected final int zza() {
        int i;
        int i2;
        int zza = super.zza();
        if (this.zzauk == null || this.zzauk.length <= 0) {
            i = zza;
        } else {
            i2 = 0;
            for (long zzao : this.zzauk) {
                i2 += zzabw.zzao(zzao);
            }
            i = (zza + i2) + (this.zzauk.length * 1);
        }
        if (this.zzaul == null || this.zzaul.length <= 0) {
            return i;
        }
        zza = 0;
        for (long zzao2 : this.zzaul) {
            zza += zzabw.zzao(zzao2);
        }
        return (i + zza) + (this.zzaul.length * 1);
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        int i = 0;
        if (this.zzauk != null && this.zzauk.length > 0) {
            for (long zza : this.zzauk) {
                com_google_android_gms_internal_measurement_zzabw.zza(1, zza);
            }
        }
        if (this.zzaul != null && this.zzaul.length > 0) {
            while (i < this.zzaul.length) {
                com_google_android_gms_internal_measurement_zzabw.zza(2, this.zzaul[i]);
                i++;
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        while (true) {
            int zzuw = com_google_android_gms_internal_measurement_zzabv.zzuw();
            int zzb;
            Object obj;
            int zzaf;
            Object obj2;
            switch (zzuw) {
                case 0:
                    break;
                case 8:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 8);
                    zzuw = this.zzauk == null ? 0 : this.zzauk.length;
                    obj = new long[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzauk, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = com_google_android_gms_internal_measurement_zzabv.zzuz();
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = com_google_android_gms_internal_measurement_zzabv.zzuz();
                    this.zzauk = obj;
                    continue;
                case 10:
                    zzaf = com_google_android_gms_internal_measurement_zzabv.zzaf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    zzb = com_google_android_gms_internal_measurement_zzabv.getPosition();
                    zzuw = 0;
                    while (com_google_android_gms_internal_measurement_zzabv.zzvc() > 0) {
                        com_google_android_gms_internal_measurement_zzabv.zzuz();
                        zzuw++;
                    }
                    com_google_android_gms_internal_measurement_zzabv.zzam(zzb);
                    zzb = this.zzauk == null ? 0 : this.zzauk.length;
                    obj2 = new long[(zzuw + zzb)];
                    if (zzb != 0) {
                        System.arraycopy(this.zzauk, 0, obj2, 0, zzb);
                    }
                    while (zzb < obj2.length) {
                        obj2[zzb] = com_google_android_gms_internal_measurement_zzabv.zzuz();
                        zzb++;
                    }
                    this.zzauk = obj2;
                    com_google_android_gms_internal_measurement_zzabv.zzal(zzaf);
                    continue;
                case 16:
                    zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 16);
                    zzuw = this.zzaul == null ? 0 : this.zzaul.length;
                    obj = new long[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzaul, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = com_google_android_gms_internal_measurement_zzabv.zzuz();
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = com_google_android_gms_internal_measurement_zzabv.zzuz();
                    this.zzaul = obj;
                    continue;
                case 18:
                    zzaf = com_google_android_gms_internal_measurement_zzabv.zzaf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    zzb = com_google_android_gms_internal_measurement_zzabv.getPosition();
                    zzuw = 0;
                    while (com_google_android_gms_internal_measurement_zzabv.zzvc() > 0) {
                        com_google_android_gms_internal_measurement_zzabv.zzuz();
                        zzuw++;
                    }
                    com_google_android_gms_internal_measurement_zzabv.zzam(zzb);
                    zzb = this.zzaul == null ? 0 : this.zzaul.length;
                    obj2 = new long[(zzuw + zzb)];
                    if (zzb != 0) {
                        System.arraycopy(this.zzaul, 0, obj2, 0, zzb);
                    }
                    while (zzb < obj2.length) {
                        obj2[zzb] = com_google_android_gms_internal_measurement_zzabv.zzuz();
                        zzb++;
                    }
                    this.zzaul = obj2;
                    com_google_android_gms_internal_measurement_zzabv.zzal(zzaf);
                    continue;
                default:
                    if (!super.zza(com_google_android_gms_internal_measurement_zzabv, zzuw)) {
                        break;
                    }
                    continue;
            }
            return this;
        }
    }
}
