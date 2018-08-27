package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzvm.zzc;
import java.io.IOException;
import java.util.Map.Entry;

final class zzvb extends zzva<Object> {
    zzvb() {
    }

    final boolean zze(zzwt com_google_android_gms_internal_measurement_zzwt) {
        return com_google_android_gms_internal_measurement_zzwt instanceof zzc;
    }

    final zzvd<Object> zzs(Object obj) {
        return ((zzc) obj).zzbys;
    }

    final void zza(Object obj, zzvd<Object> com_google_android_gms_internal_measurement_zzvd_java_lang_Object) {
        ((zzc) obj).zzbys = com_google_android_gms_internal_measurement_zzvd_java_lang_Object;
    }

    final zzvd<Object> zzt(Object obj) {
        zzvd<Object> zzs = zzs(obj);
        if (!zzs.isImmutable()) {
            return zzs;
        }
        zzvd com_google_android_gms_internal_measurement_zzvd = (zzvd) zzs.clone();
        zza(obj, com_google_android_gms_internal_measurement_zzvd);
        return com_google_android_gms_internal_measurement_zzvd;
    }

    final void zzu(Object obj) {
        zzs(obj).zzsm();
    }

    final <UT, UB> UB zza(zzxi com_google_android_gms_internal_measurement_zzxi, Object obj, zzuz com_google_android_gms_internal_measurement_zzuz, zzvd<Object> com_google_android_gms_internal_measurement_zzvd_java_lang_Object, UB ub, zzyb<UT, UB> com_google_android_gms_internal_measurement_zzyb_UT__UB) throws IOException {
        throw new NoSuchMethodError();
    }

    final int zzb(Entry<?, ?> entry) {
        entry.getKey();
        throw new NoSuchMethodError();
    }

    final void zza(zzyw com_google_android_gms_internal_measurement_zzyw, Entry<?, ?> entry) throws IOException {
        entry.getKey();
        throw new NoSuchMethodError();
    }

    final Object zza(zzuz com_google_android_gms_internal_measurement_zzuz, zzwt com_google_android_gms_internal_measurement_zzwt, int i) {
        return com_google_android_gms_internal_measurement_zzuz.zza(com_google_android_gms_internal_measurement_zzwt, i);
    }

    final void zza(zzxi com_google_android_gms_internal_measurement_zzxi, Object obj, zzuz com_google_android_gms_internal_measurement_zzuz, zzvd<Object> com_google_android_gms_internal_measurement_zzvd_java_lang_Object) throws IOException {
        throw new NoSuchMethodError();
    }

    final void zza(zzud com_google_android_gms_internal_measurement_zzud, Object obj, zzuz com_google_android_gms_internal_measurement_zzuz, zzvd<Object> com_google_android_gms_internal_measurement_zzvd_java_lang_Object) throws IOException {
        throw new NoSuchMethodError();
    }
}
