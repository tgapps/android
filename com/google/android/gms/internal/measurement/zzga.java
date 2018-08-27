package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzga extends zzza<zzga> {
    private static volatile zzga[] zzawa;
    public String name;
    public Boolean zzawb;
    public Boolean zzawc;
    public Integer zzawd;

    public static zzga[] zzmm() {
        if (zzawa == null) {
            synchronized (zzze.zzcfl) {
                if (zzawa == null) {
                    zzawa = new zzga[0];
                }
            }
        }
        return zzawa;
    }

    public zzga() {
        this.name = null;
        this.zzawb = null;
        this.zzawc = null;
        this.zzawd = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzga)) {
            return false;
        }
        zzga com_google_android_gms_internal_measurement_zzga = (zzga) obj;
        if (this.name == null) {
            if (com_google_android_gms_internal_measurement_zzga.name != null) {
                return false;
            }
        } else if (!this.name.equals(com_google_android_gms_internal_measurement_zzga.name)) {
            return false;
        }
        if (this.zzawb == null) {
            if (com_google_android_gms_internal_measurement_zzga.zzawb != null) {
                return false;
            }
        } else if (!this.zzawb.equals(com_google_android_gms_internal_measurement_zzga.zzawb)) {
            return false;
        }
        if (this.zzawc == null) {
            if (com_google_android_gms_internal_measurement_zzga.zzawc != null) {
                return false;
            }
        } else if (!this.zzawc.equals(com_google_android_gms_internal_measurement_zzga.zzawc)) {
            return false;
        }
        if (this.zzawd == null) {
            if (com_google_android_gms_internal_measurement_zzga.zzawd != null) {
                return false;
            }
        } else if (!this.zzawd.equals(com_google_android_gms_internal_measurement_zzga.zzawd)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzga.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzga.zzcfc == null || com_google_android_gms_internal_measurement_zzga.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzawd == null ? 0 : this.zzawd.hashCode()) + (((this.zzawc == null ? 0 : this.zzawc.hashCode()) + (((this.zzawb == null ? 0 : this.zzawb.hashCode()) + (((this.name == null ? 0 : this.name.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.name != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(1, this.name);
        }
        if (this.zzawb != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(2, this.zzawb.booleanValue());
        }
        if (this.zzawc != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(3, this.zzawc.booleanValue());
        }
        if (this.zzawd != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(4, this.zzawd.intValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.name != null) {
            zzf += zzyy.zzc(1, this.name);
        }
        if (this.zzawb != null) {
            this.zzawb.booleanValue();
            zzf += zzyy.zzbb(2) + 1;
        }
        if (this.zzawc != null) {
            this.zzawc.booleanValue();
            zzf += zzyy.zzbb(3) + 1;
        }
        if (this.zzawd != null) {
            return zzf + zzyy.zzh(4, this.zzawd.intValue());
        }
        return zzf;
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            switch (zzug) {
                case 0:
                    break;
                case 10:
                    this.name = com_google_android_gms_internal_measurement_zzyx.readString();
                    continue;
                case 16:
                    this.zzawb = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
                    continue;
                case 24:
                    this.zzawc = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
                    continue;
                case 32:
                    this.zzawd = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
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
