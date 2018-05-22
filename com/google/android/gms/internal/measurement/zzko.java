package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.messenger.exoplayer2.RendererCapabilities;

public final class zzko extends zzaby<zzko> {
    private static volatile zzko[] zzatd;
    public String name;
    public String zzajf;
    private Float zzarb;
    public Double zzarc;
    public Long zzate;

    public zzko() {
        this.name = null;
        this.zzajf = null;
        this.zzate = null;
        this.zzarb = null;
        this.zzarc = null;
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzko[] zzlm() {
        if (zzatd == null) {
            synchronized (zzacc.zzbxg) {
                if (zzatd == null) {
                    zzatd = new zzko[0];
                }
            }
        }
        return zzatd;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzko)) {
            return false;
        }
        zzko com_google_android_gms_internal_measurement_zzko = (zzko) obj;
        if (this.name == null) {
            if (com_google_android_gms_internal_measurement_zzko.name != null) {
                return false;
            }
        } else if (!this.name.equals(com_google_android_gms_internal_measurement_zzko.name)) {
            return false;
        }
        if (this.zzajf == null) {
            if (com_google_android_gms_internal_measurement_zzko.zzajf != null) {
                return false;
            }
        } else if (!this.zzajf.equals(com_google_android_gms_internal_measurement_zzko.zzajf)) {
            return false;
        }
        if (this.zzate == null) {
            if (com_google_android_gms_internal_measurement_zzko.zzate != null) {
                return false;
            }
        } else if (!this.zzate.equals(com_google_android_gms_internal_measurement_zzko.zzate)) {
            return false;
        }
        if (this.zzarb == null) {
            if (com_google_android_gms_internal_measurement_zzko.zzarb != null) {
                return false;
            }
        } else if (!this.zzarb.equals(com_google_android_gms_internal_measurement_zzko.zzarb)) {
            return false;
        }
        if (this.zzarc == null) {
            if (com_google_android_gms_internal_measurement_zzko.zzarc != null) {
                return false;
            }
        } else if (!this.zzarc.equals(com_google_android_gms_internal_measurement_zzko.zzarc)) {
            return false;
        }
        return (this.zzbww == null || this.zzbww.isEmpty()) ? com_google_android_gms_internal_measurement_zzko.zzbww == null || com_google_android_gms_internal_measurement_zzko.zzbww.isEmpty() : this.zzbww.equals(com_google_android_gms_internal_measurement_zzko.zzbww);
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzarc == null ? 0 : this.zzarc.hashCode()) + (((this.zzarb == null ? 0 : this.zzarb.hashCode()) + (((this.zzate == null ? 0 : this.zzate.hashCode()) + (((this.zzajf == null ? 0 : this.zzajf.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzbww == null || this.zzbww.isEmpty())) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    protected final int zza() {
        int zza = super.zza();
        if (this.name != null) {
            zza += zzabw.zzc(1, this.name);
        }
        if (this.zzajf != null) {
            zza += zzabw.zzc(2, this.zzajf);
        }
        if (this.zzate != null) {
            zza += zzabw.zzc(3, this.zzate.longValue());
        }
        if (this.zzarb != null) {
            this.zzarb.floatValue();
            zza += zzabw.zzaq(4) + 4;
        }
        if (this.zzarc == null) {
            return zza;
        }
        this.zzarc.doubleValue();
        return zza + (zzabw.zzaq(5) + 8);
    }

    public final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        if (this.name != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(1, this.name);
        }
        if (this.zzajf != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(2, this.zzajf);
        }
        if (this.zzate != null) {
            com_google_android_gms_internal_measurement_zzabw.zzb(3, this.zzate.longValue());
        }
        if (this.zzarb != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(4, this.zzarb.floatValue());
        }
        if (this.zzarc != null) {
            com_google_android_gms_internal_measurement_zzabw.zza(5, this.zzarc.doubleValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzabw);
    }

    public final /* synthetic */ zzace zzb(zzabv com_google_android_gms_internal_measurement_zzabv) throws IOException {
        while (true) {
            int zzuw = com_google_android_gms_internal_measurement_zzabv.zzuw();
            switch (zzuw) {
                case 0:
                    break;
                case 10:
                    this.name = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case 18:
                    this.zzajf = com_google_android_gms_internal_measurement_zzabv.readString();
                    continue;
                case RendererCapabilities.ADAPTIVE_SUPPORT_MASK /*24*/:
                    this.zzate = Long.valueOf(com_google_android_gms_internal_measurement_zzabv.zzuz());
                    continue;
                case 37:
                    this.zzarb = Float.valueOf(Float.intBitsToFloat(com_google_android_gms_internal_measurement_zzabv.zzva()));
                    continue;
                case 41:
                    this.zzarc = Double.valueOf(Double.longBitsToDouble(com_google_android_gms_internal_measurement_zzabv.zzvb()));
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
