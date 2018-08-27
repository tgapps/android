package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzfw extends zzza<zzfw> {
    private static volatile zzfw[] zzavj;
    public zzfz zzavk;
    public zzfx zzavl;
    public Boolean zzavm;
    public String zzavn;

    public static zzfw[] zzmk() {
        if (zzavj == null) {
            synchronized (zzze.zzcfl) {
                if (zzavj == null) {
                    zzavj = new zzfw[0];
                }
            }
        }
        return zzavj;
    }

    public zzfw() {
        this.zzavk = null;
        this.zzavl = null;
        this.zzavm = null;
        this.zzavn = null;
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzfw)) {
            return false;
        }
        zzfw com_google_android_gms_internal_measurement_zzfw = (zzfw) obj;
        if (this.zzavk == null) {
            if (com_google_android_gms_internal_measurement_zzfw.zzavk != null) {
                return false;
            }
        } else if (!this.zzavk.equals(com_google_android_gms_internal_measurement_zzfw.zzavk)) {
            return false;
        }
        if (this.zzavl == null) {
            if (com_google_android_gms_internal_measurement_zzfw.zzavl != null) {
                return false;
            }
        } else if (!this.zzavl.equals(com_google_android_gms_internal_measurement_zzfw.zzavl)) {
            return false;
        }
        if (this.zzavm == null) {
            if (com_google_android_gms_internal_measurement_zzfw.zzavm != null) {
                return false;
            }
        } else if (!this.zzavm.equals(com_google_android_gms_internal_measurement_zzfw.zzavm)) {
            return false;
        }
        if (this.zzavn == null) {
            if (com_google_android_gms_internal_measurement_zzfw.zzavn != null) {
                return false;
            }
        } else if (!this.zzavn.equals(com_google_android_gms_internal_measurement_zzfw.zzavn)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzfw.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzfw.zzcfc == null || com_google_android_gms_internal_measurement_zzfw.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = getClass().getName().hashCode() + 527;
        zzfz com_google_android_gms_internal_measurement_zzfz = this.zzavk;
        hashCode = (com_google_android_gms_internal_measurement_zzfz == null ? 0 : com_google_android_gms_internal_measurement_zzfz.hashCode()) + (hashCode * 31);
        zzfx com_google_android_gms_internal_measurement_zzfx = this.zzavl;
        hashCode = ((this.zzavn == null ? 0 : this.zzavn.hashCode()) + (((this.zzavm == null ? 0 : this.zzavm.hashCode()) + (((com_google_android_gms_internal_measurement_zzfx == null ? 0 : com_google_android_gms_internal_measurement_zzfx.hashCode()) + (hashCode * 31)) * 31)) * 31)) * 31;
        if (!(this.zzcfc == null || this.zzcfc.isEmpty())) {
            i = this.zzcfc.hashCode();
        }
        return hashCode + i;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzavk != null) {
            com_google_android_gms_internal_measurement_zzyy.zza(1, this.zzavk);
        }
        if (this.zzavl != null) {
            com_google_android_gms_internal_measurement_zzyy.zza(2, this.zzavl);
        }
        if (this.zzavm != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(3, this.zzavm.booleanValue());
        }
        if (this.zzavn != null) {
            com_google_android_gms_internal_measurement_zzyy.zzb(4, this.zzavn);
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzavk != null) {
            zzf += zzyy.zzb(1, this.zzavk);
        }
        if (this.zzavl != null) {
            zzf += zzyy.zzb(2, this.zzavl);
        }
        if (this.zzavm != null) {
            this.zzavm.booleanValue();
            zzf += zzyy.zzbb(3) + 1;
        }
        if (this.zzavn != null) {
            return zzf + zzyy.zzc(4, this.zzavn);
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
                    if (this.zzavk == null) {
                        this.zzavk = new zzfz();
                    }
                    com_google_android_gms_internal_measurement_zzyx.zza(this.zzavk);
                    continue;
                case 18:
                    if (this.zzavl == null) {
                        this.zzavl = new zzfx();
                    }
                    com_google_android_gms_internal_measurement_zzyx.zza(this.zzavl);
                    continue;
                case 24:
                    this.zzavm = Boolean.valueOf(com_google_android_gms_internal_measurement_zzyx.zzum());
                    continue;
                case 34:
                    this.zzavn = com_google_android_gms_internal_measurement_zzyx.readString();
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
