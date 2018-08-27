package com.google.android.gms.internal.measurement;

import java.util.Comparator;

final class zzuf implements Comparator<zzud> {
    zzuf() {
    }

    public final /* synthetic */ int compare(Object obj, Object obj2) {
        zzud com_google_android_gms_internal_measurement_zzud = (zzud) obj;
        zzud com_google_android_gms_internal_measurement_zzud2 = (zzud) obj2;
        zzuj com_google_android_gms_internal_measurement_zzuj = (zzuj) com_google_android_gms_internal_measurement_zzud.iterator();
        zzuj com_google_android_gms_internal_measurement_zzuj2 = (zzuj) com_google_android_gms_internal_measurement_zzud2.iterator();
        while (com_google_android_gms_internal_measurement_zzuj.hasNext() && com_google_android_gms_internal_measurement_zzuj2.hasNext()) {
            int compare = Integer.compare(zzud.zza(com_google_android_gms_internal_measurement_zzuj.nextByte()), zzud.zza(com_google_android_gms_internal_measurement_zzuj2.nextByte()));
            if (compare != 0) {
                return compare;
            }
        }
        return Integer.compare(com_google_android_gms_internal_measurement_zzud.size(), com_google_android_gms_internal_measurement_zzud2.size());
    }
}
