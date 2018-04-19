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
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkArgument(j >= 0);
        Preconditions.checkArgument(j2 >= 0);
        Preconditions.checkArgument(j4 >= 0);
        this.zztd = str;
        this.name = str2;
        this.zzafp = j;
        this.zzafq = j2;
        this.zzafr = j3;
        this.zzafs = j4;
        this.zzaft = l;
        this.zzafu = l2;
        this.zzafv = bool;
    }

    final zzeq zza(Long l, Long l2, Boolean bool) {
        Boolean bool2 = (bool == null || bool.booleanValue()) ? bool : null;
        return new zzeq(this.zztd, this.name, this.zzafp, this.zzafq, this.zzafr, this.zzafs, l, l2, bool2);
    }

    final zzeq zzad(long j) {
        return new zzeq(this.zztd, this.name, this.zzafp, this.zzafq, this.zzafr, j, this.zzaft, this.zzafu, this.zzafv);
    }

    final zzeq zzie() {
        return new zzeq(this.zztd, this.name, this.zzafp + 1, this.zzafq + 1, this.zzafr, this.zzafs, this.zzaft, this.zzafu, this.zzafv);
    }
}
