package com.google.android.gms.internal.measurement;

import com.googlecode.mp4parser.authoring.tracks.h265.NalUnitTypes;
import java.io.IOException;

public final class zzgj extends zzza<zzgj> {
    public long[] zzaye;
    public long[] zzayf;
    public zzge[] zzayg;
    public zzgk[] zzayh;

    public zzgj() {
        this.zzaye = zzzj.zzcfr;
        this.zzayf = zzzj.zzcfr;
        this.zzayg = zzge.zzmp();
        this.zzayh = zzgk.zzmt();
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgj)) {
            return false;
        }
        zzgj com_google_android_gms_internal_measurement_zzgj = (zzgj) obj;
        if (!zzze.equals(this.zzaye, com_google_android_gms_internal_measurement_zzgj.zzaye)) {
            return false;
        }
        if (!zzze.equals(this.zzayf, com_google_android_gms_internal_measurement_zzgj.zzayf)) {
            return false;
        }
        if (!zzze.equals(this.zzayg, com_google_android_gms_internal_measurement_zzgj.zzayg)) {
            return false;
        }
        if (!zzze.equals(this.zzayh, com_google_android_gms_internal_measurement_zzgj.zzayh)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgj.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgj.zzcfc == null || com_google_android_gms_internal_measurement_zzgj.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i;
        int hashCode = (((((((((getClass().getName().hashCode() + 527) * 31) + zzze.hashCode(this.zzaye)) * 31) + zzze.hashCode(this.zzayf)) * 31) + zzze.hashCode(this.zzayg)) * 31) + zzze.hashCode(this.zzayh)) * 31;
        if (this.zzcfc == null || this.zzcfc.isEmpty()) {
            i = 0;
        } else {
            i = this.zzcfc.hashCode();
        }
        return i + hashCode;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        int i = 0;
        if (this.zzaye != null && this.zzaye.length > 0) {
            for (long zza : this.zzaye) {
                com_google_android_gms_internal_measurement_zzyy.zza(1, zza);
            }
        }
        if (this.zzayf != null && this.zzayf.length > 0) {
            for (long zza2 : this.zzayf) {
                com_google_android_gms_internal_measurement_zzyy.zza(2, zza2);
            }
        }
        if (this.zzayg != null && this.zzayg.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzayg) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(3, com_google_android_gms_internal_measurement_zzzg);
                }
            }
        }
        if (this.zzayh != null && this.zzayh.length > 0) {
            while (i < this.zzayh.length) {
                zzzg com_google_android_gms_internal_measurement_zzzg2 = this.zzayh[i];
                if (com_google_android_gms_internal_measurement_zzzg2 != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(4, com_google_android_gms_internal_measurement_zzzg2);
                }
                i++;
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int i;
        int i2;
        int i3 = 0;
        int zzf = super.zzf();
        if (this.zzaye == null || this.zzaye.length <= 0) {
            i = zzf;
        } else {
            i2 = 0;
            for (long zzbi : this.zzaye) {
                i2 += zzyy.zzbi(zzbi);
            }
            i = (zzf + i2) + (this.zzaye.length * 1);
        }
        if (this.zzayf != null && this.zzayf.length > 0) {
            zzf = 0;
            for (long zzbi2 : this.zzayf) {
                zzf += zzyy.zzbi(zzbi2);
            }
            i = (i + zzf) + (this.zzayf.length * 1);
        }
        if (this.zzayg != null && this.zzayg.length > 0) {
            zzf = i;
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzayg) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    zzf += zzyy.zzb(3, com_google_android_gms_internal_measurement_zzzg);
                }
            }
            i = zzf;
        }
        if (this.zzayh != null && this.zzayh.length > 0) {
            while (i3 < this.zzayh.length) {
                zzzg com_google_android_gms_internal_measurement_zzzg2 = this.zzayh[i3];
                if (com_google_android_gms_internal_measurement_zzzg2 != null) {
                    i += zzyy.zzb(4, com_google_android_gms_internal_measurement_zzzg2);
                }
                i3++;
            }
        }
        return i;
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            int zzb;
            Object obj;
            int zzaq;
            Object obj2;
            switch (zzug) {
                case 0:
                    break;
                case 8:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 8);
                    if (this.zzaye == null) {
                        zzug = 0;
                    } else {
                        zzug = this.zzaye.length;
                    }
                    obj = new long[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzaye, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = com_google_android_gms_internal_measurement_zzyx.zzuz();
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = com_google_android_gms_internal_measurement_zzyx.zzuz();
                    this.zzaye = obj;
                    continue;
                case 10:
                    zzaq = com_google_android_gms_internal_measurement_zzyx.zzaq(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    zzb = com_google_android_gms_internal_measurement_zzyx.getPosition();
                    zzug = 0;
                    while (com_google_android_gms_internal_measurement_zzyx.zzyr() > 0) {
                        com_google_android_gms_internal_measurement_zzyx.zzuz();
                        zzug++;
                    }
                    com_google_android_gms_internal_measurement_zzyx.zzby(zzb);
                    zzb = this.zzaye == null ? 0 : this.zzaye.length;
                    obj2 = new long[(zzug + zzb)];
                    if (zzb != 0) {
                        System.arraycopy(this.zzaye, 0, obj2, 0, zzb);
                    }
                    while (zzb < obj2.length) {
                        obj2[zzb] = com_google_android_gms_internal_measurement_zzyx.zzuz();
                        zzb++;
                    }
                    this.zzaye = obj2;
                    com_google_android_gms_internal_measurement_zzyx.zzar(zzaq);
                    continue;
                case 16:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 16);
                    if (this.zzayf == null) {
                        zzug = 0;
                    } else {
                        zzug = this.zzayf.length;
                    }
                    obj = new long[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzayf, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = com_google_android_gms_internal_measurement_zzyx.zzuz();
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = com_google_android_gms_internal_measurement_zzyx.zzuz();
                    this.zzayf = obj;
                    continue;
                case 18:
                    zzaq = com_google_android_gms_internal_measurement_zzyx.zzaq(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    zzb = com_google_android_gms_internal_measurement_zzyx.getPosition();
                    zzug = 0;
                    while (com_google_android_gms_internal_measurement_zzyx.zzyr() > 0) {
                        com_google_android_gms_internal_measurement_zzyx.zzuz();
                        zzug++;
                    }
                    com_google_android_gms_internal_measurement_zzyx.zzby(zzb);
                    zzb = this.zzayf == null ? 0 : this.zzayf.length;
                    obj2 = new long[(zzug + zzb)];
                    if (zzb != 0) {
                        System.arraycopy(this.zzayf, 0, obj2, 0, zzb);
                    }
                    while (zzb < obj2.length) {
                        obj2[zzb] = com_google_android_gms_internal_measurement_zzyx.zzuz();
                        zzb++;
                    }
                    this.zzayf = obj2;
                    com_google_android_gms_internal_measurement_zzyx.zzar(zzaq);
                    continue;
                case NalUnitTypes.NAL_TYPE_RSV_VCL26 /*26*/:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 26);
                    zzug = this.zzayg == null ? 0 : this.zzayg.length;
                    obj = new zzge[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzayg, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzge();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzge();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzayg = obj;
                    continue;
                case 34:
                    zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 34);
                    zzug = this.zzayh == null ? 0 : this.zzayh.length;
                    obj = new zzgk[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzayh, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzgk();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzgk();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzayh = obj;
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
