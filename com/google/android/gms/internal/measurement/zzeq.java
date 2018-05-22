package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;

final class zzeq {
    final String name;
    final long zzafr;
    final long zzafs;
    final long zzaft;
    final long zzafu;
    final Long zzafv;
    final Long zzafw;
    final Boolean zzafx;
    final String zzti;

    zzeq(String str, String str2, long j, long j2, long j3, long j4, Long l, Long l2, Boolean bool) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkArgument(j >= 0);
        Preconditions.checkArgument(j2 >= 0);
        Preconditions.checkArgument(j4 >= 0);
        this.zzti = str;
        this.name = str2;
        this.zzafr = j;
        this.zzafs = j2;
        this.zzaft = j3;
        this.zzafu = j4;
        this.zzafv = l;
        this.zzafw = l2;
        this.zzafx = bool;
    }

    final zzeq zza(Long l, Long l2, Boolean bool) {
        Boolean bool2 = (bool == null || bool.booleanValue()) ? bool : null;
        return new zzeq(this.zzti, this.name, this.zzafr, this.zzafs, this.zzaft, this.zzafu, l, l2, bool2);
    }

    final zzeq zzac(long j) {
        return new zzeq(this.zzti, this.name, this.zzafr, this.zzafs, j, this.zzafu, this.zzafv, this.zzafw, this.zzafx);
    }

    final zzeq zzad(long j) {
        return new zzeq(this.zzti, this.name, this.zzafr, this.zzafs, this.zzaft, j, this.zzafv, this.zzafw, this.zzafx);
    }

    final zzeq zzie() {
        return new zzeq(this.zzti, this.name, this.zzafr + 1, this.zzafs + 1, this.zzaft, this.zzafu, this.zzafv, this.zzafw, this.zzafx);
    }
}
