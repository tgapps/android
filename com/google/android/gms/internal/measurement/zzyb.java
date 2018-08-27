package com.google.android.gms.internal.measurement;

import java.io.IOException;
import org.telegram.tgnet.ConnectionsManager;

abstract class zzyb<T, B> {
    zzyb() {
    }

    abstract void zza(B b, int i, long j);

    abstract void zza(B b, int i, zzud com_google_android_gms_internal_measurement_zzud);

    abstract void zza(B b, int i, T t);

    abstract void zza(T t, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException;

    abstract boolean zza(zzxi com_google_android_gms_internal_measurement_zzxi);

    abstract T zzab(B b);

    abstract int zzae(T t);

    abstract T zzah(Object obj);

    abstract B zzai(Object obj);

    abstract int zzaj(T t);

    abstract void zzb(B b, int i, long j);

    abstract void zzc(B b, int i, int i2);

    abstract void zzc(T t, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException;

    abstract void zzf(Object obj, T t);

    abstract void zzg(Object obj, B b);

    abstract T zzh(T t, T t2);

    abstract void zzu(Object obj);

    abstract B zzye();

    final boolean zza(B b, zzxi com_google_android_gms_internal_measurement_zzxi) throws IOException {
        int tag = com_google_android_gms_internal_measurement_zzxi.getTag();
        int i = tag >>> 3;
        switch (tag & 7) {
            case 0:
                zza((Object) b, i, com_google_android_gms_internal_measurement_zzxi.zzui());
                return true;
            case 1:
                zzb(b, i, com_google_android_gms_internal_measurement_zzxi.zzuk());
                return true;
            case 2:
                zza((Object) b, i, com_google_android_gms_internal_measurement_zzxi.zzuo());
                return true;
            case 3:
                Object zzye = zzye();
                int i2 = (i << 3) | 4;
                while (com_google_android_gms_internal_measurement_zzxi.zzve() != ConnectionsManager.DEFAULT_DATACENTER_ID) {
                    if (!zza(zzye, com_google_android_gms_internal_measurement_zzxi)) {
                        if (i2 == com_google_android_gms_internal_measurement_zzxi.getTag()) {
                            throw zzvt.zzwn();
                        }
                        zza((Object) b, i, zzab(zzye));
                        return true;
                    }
                }
                if (i2 == com_google_android_gms_internal_measurement_zzxi.getTag()) {
                    zza((Object) b, i, zzab(zzye));
                    return true;
                }
                throw zzvt.zzwn();
            case 4:
                return false;
            case 5:
                zzc(b, i, com_google_android_gms_internal_measurement_zzxi.zzul());
                return true;
            default:
                throw zzvt.zzwo();
        }
    }
}
