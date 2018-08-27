package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzge extends zzza<zzge> {
    private static volatile zzge[] zzawp;
    public Integer zzawq;
    public Long zzawr;

    public static zzge[] zzmp() {
        if (zzawp == null) {
            synchronized (zzze.zzcfl) {
                if (zzawp == null) {
                    zzawp = new zzge[0];
                }
            }
        }
        return zzawp;
    }

    public zzge() {
        this.zzawq = null;
        this.zzawr = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzge)) {
            return false;
        }
        zzge com_google_android_gms_internal_measurement_zzge = (zzge) obj;
        if (this.zzawq == null) {
            if (com_google_android_gms_internal_measurement_zzge.zzawq != null) {
                return false;
            }
        } else if (!this.zzawq.equals(com_google_android_gms_internal_measurement_zzge.zzawq)) {
            return false;
        }
        if (this.zzawr == null) {
            if (com_google_android_gms_internal_measurement_zzge.zzawr != null) {
                return false;
            }
        } else if (!this.zzawr.equals(com_google_android_gms_internal_measurement_zzge.zzawr)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzge.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzge.zzcfc == null || com_google_android_gms_internal_measurement_zzge.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.zzawr == null ? 0 : this.zzawr.hashCode()) + (((this.zzawq == null ? 0 : this.zzawq.hashCode()) + ((getClass().getName().hashCode() + 527) * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzawq != null) {
            com_google_android_gms_internal_measurement_zzyy.zzd(1, this.zzawq.intValue());
        }
        if (this.zzawr != null) {
            com_google_android_gms_internal_measurement_zzyy.zzi(2, this.zzawr.longValue());
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzawq != null) {
            zzf += zzyy.zzh(1, this.zzawq.intValue());
        }
        if (this.zzawr != null) {
            return zzf + zzyy.zzd(2, this.zzawr.longValue());
        }
        return zzf;
    }

    public final /* synthetic */ zzzg zza(zzyx com_google_android_gms_internal_measurement_zzyx) throws IOException {
        while (true) {
            int zzug = com_google_android_gms_internal_measurement_zzyx.zzug();
            switch (zzug) {
                case 0:
                    break;
                case 8:
                    this.zzawq = Integer.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuy());
                    continue;
                case 16:
                    this.zzawr = Long.valueOf(com_google_android_gms_internal_measurement_zzyx.zzuz());
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
