package com.google.android.gms.internal.measurement;

public class zzwa {
    private static final zzuz zzbtt = zzuz.zzvo();
    private zzud zzcad;
    private volatile zzwt zzcae;
    private volatile zzud zzcaf;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzwa)) {
            return false;
        }
        zzwa com_google_android_gms_internal_measurement_zzwa = (zzwa) obj;
        zzwt com_google_android_gms_internal_measurement_zzwt = this.zzcae;
        zzwt com_google_android_gms_internal_measurement_zzwt2 = com_google_android_gms_internal_measurement_zzwa.zzcae;
        if (com_google_android_gms_internal_measurement_zzwt == null && com_google_android_gms_internal_measurement_zzwt2 == null) {
            return zztt().equals(com_google_android_gms_internal_measurement_zzwa.zztt());
        }
        if (com_google_android_gms_internal_measurement_zzwt != null && com_google_android_gms_internal_measurement_zzwt2 != null) {
            return com_google_android_gms_internal_measurement_zzwt.equals(com_google_android_gms_internal_measurement_zzwt2);
        }
        if (com_google_android_gms_internal_measurement_zzwt != null) {
            return com_google_android_gms_internal_measurement_zzwt.equals(com_google_android_gms_internal_measurement_zzwa.zzh(com_google_android_gms_internal_measurement_zzwt.zzwf()));
        }
        return zzh(com_google_android_gms_internal_measurement_zzwt2.zzwf()).equals(com_google_android_gms_internal_measurement_zzwt2);
    }

    public int hashCode() {
        return 1;
    }

    private final zzwt zzh(zzwt com_google_android_gms_internal_measurement_zzwt) {
        if (this.zzcae == null) {
            synchronized (this) {
                if (this.zzcae != null) {
                } else {
                    try {
                        this.zzcae = com_google_android_gms_internal_measurement_zzwt;
                        this.zzcaf = zzud.zzbtz;
                    } catch (zzvt e) {
                        this.zzcae = com_google_android_gms_internal_measurement_zzwt;
                        this.zzcaf = zzud.zzbtz;
                    }
                }
            }
        }
        return this.zzcae;
    }

    public final zzwt zzi(zzwt com_google_android_gms_internal_measurement_zzwt) {
        zzwt com_google_android_gms_internal_measurement_zzwt2 = this.zzcae;
        this.zzcad = null;
        this.zzcaf = null;
        this.zzcae = com_google_android_gms_internal_measurement_zzwt;
        return com_google_android_gms_internal_measurement_zzwt2;
    }

    public final int zzvu() {
        if (this.zzcaf != null) {
            return this.zzcaf.size();
        }
        if (this.zzcae != null) {
            return this.zzcae.zzvu();
        }
        return 0;
    }

    public final zzud zztt() {
        if (this.zzcaf != null) {
            return this.zzcaf;
        }
        synchronized (this) {
            if (this.zzcaf != null) {
                zzud com_google_android_gms_internal_measurement_zzud = this.zzcaf;
                return com_google_android_gms_internal_measurement_zzud;
            }
            if (this.zzcae == null) {
                this.zzcaf = zzud.zzbtz;
            } else {
                this.zzcaf = this.zzcae.zztt();
            }
            com_google_android_gms_internal_measurement_zzud = this.zzcaf;
            return com_google_android_gms_internal_measurement_zzud;
        }
    }
}
