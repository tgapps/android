package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;

final class zzeq {
    final String name;
    final long zzafp;
    final long zzafq;
    final long zzafr;
    final long zzafs;
    final Long zzaft;
    final Long zzafu;
    final Boolean zzafv;
    final String zztd;

    zzeq(String str, String str2, long j, long j2, long j3, long j4, Long l, Long l2, Boolean bool) {
        zzeq com_google_android_gms_internal_measurement_zzeq = this;
        long j5 = j;
        long j6 = j2;
        long j7 = j4;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        boolean z = false;
        Preconditions.checkArgument(j5 >= 0);
        Preconditions.checkArgument(j6 >= 0);
        if (j7 >= 0) {
            z = true;
        }
        Preconditions.checkArgument(z);
        com_google_android_gms_internal_measurement_zzeq.zztd = str;
        com_google_android_gms_internal_measurement_zzeq.name = str2;
        com_google_android_gms_internal_measurement_zzeq.zzafp = j5;
        com_google_android_gms_internal_measurement_zzeq.zzafq = j6;
        com_google_android_gms_internal_measurement_zzeq.zzafr = j3;
        com_google_android_gms_internal_measurement_zzeq.zzafs = j7;
        com_google_android_gms_internal_measurement_zzeq.zzaft = l;
        com_google_android_gms_internal_measurement_zzeq.zzafu = l2;
        com_google_android_gms_internal_measurement_zzeq.zzafv = bool;
    }

    final zzeq zza(Long l, Long l2, Boolean bool) {
        zzeq com_google_android_gms_internal_measurement_zzeq = this;
        Boolean bool2 = (bool == null || bool.booleanValue()) ? bool : null;
        return new zzeq(com_google_android_gms_internal_measurement_zzeq.zztd, com_google_android_gms_internal_measurement_zzeq.name, com_google_android_gms_internal_measurement_zzeq.zzafp, com_google_android_gms_internal_measurement_zzeq.zzafq, com_google_android_gms_internal_measurement_zzeq.zzafr, com_google_android_gms_internal_measurement_zzeq.zzafs, l, l2, bool2);
    }

    final zzeq zzad(long j) {
        return new zzeq(this.zztd, this.name, this.zzafp, this.zzafq, this.zzafr, j, this.zzaft, this.zzafu, this.zzafv);
    }

    final zzeq zzie() {
        String str = this.zztd;
        String str2 = this.name;
        long j = this.zzafp + 1;
        long j2 = this.zzafq + 1;
        long j3 = this.zzafr;
        long j4 = this.zzafs;
        return new zzeq(str, str2, j, j2, j3, j4, this.zzaft, this.zzafu, this.zzafv);
    }
}
