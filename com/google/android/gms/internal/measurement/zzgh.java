package com.google.android.gms.internal.measurement;

import java.io.IOException;

public final class zzgh extends zzza<zzgh> {
    public zzgi[] zzawy;

    public zzgh() {
        this.zzawy = zzgi.zzms();
        this.zzcfc = null;
        this.zzcfm = -1;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzgh)) {
            return false;
        }
        zzgh com_google_android_gms_internal_measurement_zzgh = (zzgh) obj;
        if (!zzze.equals(this.zzawy, com_google_android_gms_internal_measurement_zzgh.zzawy)) {
            return false;
        }
        if (this.zzcfc != null && !this.zzcfc.isEmpty()) {
            return this.zzcfc.equals(com_google_android_gms_internal_measurement_zzgh.zzcfc);
        }
        if (com_google_android_gms_internal_measurement_zzgh.zzcfc == null || com_google_android_gms_internal_measurement_zzgh.zzcfc.isEmpty()) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        int i;
        int hashCode = (((getClass().getName().hashCode() + 527) * 31) + zzze.hashCode(this.zzawy)) * 31;
        if (this.zzcfc == null || this.zzcfc.isEmpty()) {
            i = 0;
        } else {
            i = this.zzcfc.hashCode();
        }
        return i + hashCode;
    }

    public final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.zzawy != null && this.zzawy.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzawy) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    com_google_android_gms_internal_measurement_zzyy.zza(1, com_google_android_gms_internal_measurement_zzzg);
                }
            }
        }
        super.zza(com_google_android_gms_internal_measurement_zzyy);
    }

    protected final int zzf() {
        int zzf = super.zzf();
        if (this.zzawy != null && this.zzawy.length > 0) {
            for (zzzg com_google_android_gms_internal_measurement_zzzg : this.zzawy) {
                if (com_google_android_gms_internal_measurement_zzzg != null) {
                    zzf += zzyy.zzb(1, com_google_android_gms_internal_measurement_zzzg);
                }
            }
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
                    int zzb = zzzj.zzb(com_google_android_gms_internal_measurement_zzyx, 10);
                    zzug = this.zzawy == null ? 0 : this.zzawy.length;
                    Object obj = new zzgi[(zzb + zzug)];
                    if (zzug != 0) {
                        System.arraycopy(this.zzawy, 0, obj, 0, zzug);
                    }
                    while (zzug < obj.length - 1) {
                        obj[zzug] = new zzgi();
                        com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                        com_google_android_gms_internal_measurement_zzyx.zzug();
                        zzug++;
                    }
                    obj[zzug] = new zzgi();
                    com_google_android_gms_internal_measurement_zzyx.zza(obj[zzug]);
                    this.zzawy = obj;
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
