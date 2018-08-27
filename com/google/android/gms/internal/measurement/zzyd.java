package com.google.android.gms.internal.measurement;

import java.io.IOException;

final class zzyd extends zzyb<zzyc, zzyc> {
    zzyd() {
    }

    final boolean zza(zzxi com_google_android_gms_internal_measurement_zzxi) {
        return false;
    }

    private static void zza(Object obj, zzyc com_google_android_gms_internal_measurement_zzyc) {
        ((zzvm) obj).zzbym = com_google_android_gms_internal_measurement_zzyc;
    }

    final void zzu(Object obj) {
        ((zzvm) obj).zzbym.zzsm();
    }

    final /* synthetic */ int zzae(Object obj) {
        return ((zzyc) obj).zzvu();
    }

    final /* synthetic */ int zzaj(Object obj) {
        return ((zzyc) obj).zzyh();
    }

    final /* synthetic */ Object zzh(Object obj, Object obj2) {
        zzyc com_google_android_gms_internal_measurement_zzyc = (zzyc) obj;
        zzyc com_google_android_gms_internal_measurement_zzyc2 = (zzyc) obj2;
        if (com_google_android_gms_internal_measurement_zzyc2.equals(zzyc.zzyf())) {
            return com_google_android_gms_internal_measurement_zzyc;
        }
        return zzyc.zza(com_google_android_gms_internal_measurement_zzyc, com_google_android_gms_internal_measurement_zzyc2);
    }

    final /* synthetic */ void zzc(Object obj, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
        ((zzyc) obj).zza(com_google_android_gms_internal_measurement_zzyw);
    }

    final /* synthetic */ void zza(Object obj, zzyw com_google_android_gms_internal_measurement_zzyw) throws IOException {
        ((zzyc) obj).zzb(com_google_android_gms_internal_measurement_zzyw);
    }

    final /* synthetic */ void zzg(Object obj, Object obj2) {
        zza(obj, (zzyc) obj2);
    }

    final /* synthetic */ Object zzai(Object obj) {
        zzyc com_google_android_gms_internal_measurement_zzyc = ((zzvm) obj).zzbym;
        if (com_google_android_gms_internal_measurement_zzyc != zzyc.zzyf()) {
            return com_google_android_gms_internal_measurement_zzyc;
        }
        com_google_android_gms_internal_measurement_zzyc = zzyc.zzyg();
        zza(obj, com_google_android_gms_internal_measurement_zzyc);
        return com_google_android_gms_internal_measurement_zzyc;
    }

    final /* synthetic */ Object zzah(Object obj) {
        return ((zzvm) obj).zzbym;
    }

    final /* synthetic */ void zzf(Object obj, Object obj2) {
        zza(obj, (zzyc) obj2);
    }

    final /* synthetic */ Object zzab(Object obj) {
        zzyc com_google_android_gms_internal_measurement_zzyc = (zzyc) obj;
        com_google_android_gms_internal_measurement_zzyc.zzsm();
        return com_google_android_gms_internal_measurement_zzyc;
    }

    final /* synthetic */ Object zzye() {
        return zzyc.zzyg();
    }

    final /* synthetic */ void zza(Object obj, int i, Object obj2) {
        ((zzyc) obj).zzb((i << 3) | 3, (zzyc) obj2);
    }

    final /* synthetic */ void zza(Object obj, int i, zzud com_google_android_gms_internal_measurement_zzud) {
        ((zzyc) obj).zzb((i << 3) | 2, (Object) com_google_android_gms_internal_measurement_zzud);
    }

    final /* synthetic */ void zzb(Object obj, int i, long j) {
        ((zzyc) obj).zzb((i << 3) | 1, Long.valueOf(j));
    }

    final /* synthetic */ void zzc(Object obj, int i, int i2) {
        ((zzyc) obj).zzb((i << 3) | 5, Integer.valueOf(i2));
    }

    final /* synthetic */ void zza(Object obj, int i, long j) {
        ((zzyc) obj).zzb(i << 3, Long.valueOf(j));
    }
}
