package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzvm.zzd;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class zzuz {
    private static volatile boolean zzbvj = false;
    private static final Class<?> zzbvk = zzvn();
    private static volatile zzuz zzbvl;
    static final zzuz zzbvm = new zzuz(true);
    private final Map<zza, zzd<?, ?>> zzbvn;

    static final class zza {
        private final int number;
        private final Object object;

        zza(Object obj, int i) {
            this.object = obj;
            this.number = i;
        }

        public final int hashCode() {
            return (System.identityHashCode(this.object) * 65535) + this.number;
        }

        public final boolean equals(Object obj) {
            if (!(obj instanceof zza)) {
                return false;
            }
            zza com_google_android_gms_internal_measurement_zzuz_zza = (zza) obj;
            if (this.object == com_google_android_gms_internal_measurement_zzuz_zza.object && this.number == com_google_android_gms_internal_measurement_zzuz_zza.number) {
                return true;
            }
            return false;
        }
    }

    private static Class<?> zzvn() {
        try {
            return Class.forName("com.google.protobuf.Extension");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static zzuz zzvo() {
        return zzuy.zzvl();
    }

    public static zzuz zzvp() {
        zzuz com_google_android_gms_internal_measurement_zzuz = zzbvl;
        if (com_google_android_gms_internal_measurement_zzuz == null) {
            synchronized (zzuz.class) {
                com_google_android_gms_internal_measurement_zzuz = zzbvl;
                if (com_google_android_gms_internal_measurement_zzuz == null) {
                    com_google_android_gms_internal_measurement_zzuz = zzuy.zzvm();
                    zzbvl = com_google_android_gms_internal_measurement_zzuz;
                }
            }
        }
        return com_google_android_gms_internal_measurement_zzuz;
    }

    static zzuz zzvm() {
        return zzvk.zzd(zzuz.class);
    }

    public final <ContainingType extends zzwt> zzd<ContainingType, ?> zza(ContainingType containingType, int i) {
        return (zzd) this.zzbvn.get(new zza(containingType, i));
    }

    zzuz() {
        this.zzbvn = new HashMap();
    }

    private zzuz(boolean z) {
        this.zzbvn = Collections.emptyMap();
    }
}
