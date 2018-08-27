package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.Preconditions;

final class zzz {
    final String name;
    final long zzaie;
    final long zzaif;
    final long zzaig;
    final long zzaih;
    final Long zzaii;
    final Long zzaij;
    final Long zzaik;
    final Boolean zzail;
    final String zztt;

    zzz(String str, String str2, long j, long j2, long j3, long j4, Long l, Long l2, Long l3, Boolean bool) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str2);
        Preconditions.checkArgument(j >= 0);
        Preconditions.checkArgument(j2 >= 0);
        Preconditions.checkArgument(j4 >= 0);
        this.zztt = str;
        this.name = str2;
        this.zzaie = j;
        this.zzaif = j2;
        this.zzaig = j3;
        this.zzaih = j4;
        this.zzaii = l;
        this.zzaij = l2;
        this.zzaik = l3;
        this.zzail = bool;
    }

    final zzz zziu() {
        return new zzz(this.zztt, this.name, this.zzaie + 1, this.zzaif + 1, this.zzaig, this.zzaih, this.zzaii, this.zzaij, this.zzaik, this.zzail);
    }

    final zzz zzai(long j) {
        return new zzz(this.zztt, this.name, this.zzaie, this.zzaif, j, this.zzaih, this.zzaii, this.zzaij, this.zzaik, this.zzail);
    }

    final zzz zza(long j, long j2) {
        return new zzz(this.zztt, this.name, this.zzaie, this.zzaif, this.zzaig, j, Long.valueOf(j2), this.zzaij, this.zzaik, this.zzail);
    }

    final zzz zza(Long l, Long l2, Boolean bool) {
        Boolean bool2;
        if (bool == null || bool.booleanValue()) {
            bool2 = bool;
        } else {
            bool2 = null;
        }
        return new zzz(this.zztt, this.name, this.zzaie, this.zzaif, this.zzaig, this.zzaih, this.zzaii, l, l2, bool2);
    }
}
