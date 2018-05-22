package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzke extends zzaby<zzke> {
    private static volatile zzke[] zzaro;
    public Integer zzarp;
    public String zzarq;
    public zzkf[] zzarr;
    private Boolean zzars;
    public zzkg zzart;

    public zzke() {
        this.zzarp = null;
        this.zzarq = null;
        this.zzarr = zzkf.zzlg();
        this.zzars = null;
        this.zzart = null;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzke[] zzlf() {
        if (zzaro == null) {
            synchronized (zzacc.zzbxg) {
                if (zzaro == null) {
                    zzaro = new zzke[0];
                }
            }
        }
        return zzaro;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzke)) {
            return false;
        }
        zzke com_google_android_gms_internal_measurement_zzke = (zzke) obj;
        if (this.zzarp == null) {
            if (com_google_android_gms_internal_measurement_zzke.zzarp != null) {
                return false;
            }
        } else if (!this.zzarp.equals(com_google_android_gms_internal_measurement_zzke.zzarp)) {
            return false;
        }
        if (this.zzarq == null) {
            if (com_google_android_gms_internal_measurement_zzke.zzarq != null) {
                return false;
            }
        } else if (!this.zzarq.equals(com_google_android_gms_internal_measurement_zzke.zzarq)) {
            return false;
        }
        if (!zzacc.equals(this.zzarr, com_google_android_gms_internal_measurement_zzke.zzarr)) {
            return false;
        }
        if (this.zzars == null) {
            if (com_google_android_gms_internal_measurement_zzke.zzars != null) {
                return false;
            }
        } else if (!this.zzars.equals(com_google_android_gms_internal_measurement_zzke.zzars)) {
            return false;
        }
        if (this.zzart == null) {
            if (com_google_android_gms_internal_measurement_zzke.zzart != null) {
                return false;
            }
        } else if (!this.zzart.equals(com_google_android_gms_internal_measurement_zzke.zzart)) {
            return false;
        }
        return (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzke.zzbww == null || com_google_android_gms_internal_measurement_zzke.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzke.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (this.zzars == null ? 0 : this.zzars.hashCode()) + (((((this.zzarq == null ? 0 : this.zzarq.hashCode()) + (((this.zzarp == null ? 0 : this.zzarp.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31) + zzacc.hashCode(this.zzarr)) * 31);
        zzkg com_google_android_gms_internal_measurement_zzkg = this.zzart;
        hashCode = ((com_google_android_gms_internal_measurement_zzkg == null ? 0 : com_google_android_gms_internal_measurement_zzkg.hashCode()) + (hashCode * 31)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.zzarp != null) {
            zza += zzabw.zzf(1, this.zzarp.intValue());
        }
        if (this.zzarq != null) {
            zza += zzabw.zzc(2, this.zzarq);
        }
        if (this.zzarr != null && this.zzarr.length > 0) {
            int i = zza;
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzarr) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    i += zzabw.zzb(3, com_google_android_gms_internal_measurement_zzace);
                }
            }
            zza = i;
        }
        if (this.zzars != null) {
            this.zzars.booleanValue();
            zza += zzabw.zzaq(4) + 1;
        }
        return this.zzart != null ? zza + zzabw.zzb(5, this.zzart) : zza;
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        if (this.zzarp != null) {
            com_google_android_gms_internal_measurement_zzabw.zze(1, this.zzarp.intValue());
        }
        if (this.zzarq != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(2, this.zzarq);
        }
        if (this.zzarr != null && this.zzarr.length > 0) {
            for (zzace com_google_android_gms_internal_measurement_zzace : this.zzarr) {
                if (com_google_android_gms_internal_measurement_zzace != null) {
                    com_google_android_gms_internal_measurement_zzabw.zza(3, com_google_android_gms_internal_measurement_zzace);
                }
            }
        }
        if (this.zzars != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(4, this.zzars.booleanValue());
        }
        if (this.zzart != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(5, this.zzart);
        }
        super.zza(com_google_android_gms_internal_measurement_zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        while (true) {
            int zzuw = com_google_android_gms_internal_measurement_zzabv.zzuw();
            switch (zzuw) {
                case 0:
                    break;
                case 8:
                    this.zzarp = Integer.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuy());
                    continue;
                case 18:
                    this.zzarq = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 26:
                    int zzb = zzach.zzb(com_google_android_gms_internal_measurement_zzabv, 26);
                    zzuw = this.zzarr == null ? 0 : this.zzarr.length;
                    Object obj = new zzkf[(zzb + zzuw)];
                    if (zzuw != 0) {
                        System.arraycopy(this.zzarr, 0, obj, 0, zzuw);
                    }
                    while (zzuw < obj.length - 1) {
                        obj[zzuw] = new zzkf();
                        com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                        com_google_android_gms_internal_measurement_zzabv.zzuw();
                        zzuw++;
                    }
                    obj[zzuw] = new zzkf();
                    com_google_android_gms_internal_measurement_zzabv.zza(obj[zzuw]);
                    this.zzarr = obj;
                    continue;
                case 32:
                    this.zzars = Boolean.valueOf(com_google_android_gms_internal_measurement_zzabv.zzux());
                    continue;
                case 42:
                    if (this.zzart == null) {
                        this.zzart = new zzkg();
                    }
                    com_google_android_gms_internal_measurement_zzabv.zza(this.zzart);
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
