package com.google.android.gms.internal.measurement;

import com.google.android.gms.common.internal.Preconditions;

final class zzju {
    final String name;
    final Object value;
    final String zzaek;
    final long zzaqu;
    final String zztd;

    zzju(String str, String str2, String str3, long j, Object obj) {
        Preconditions.checkNotEmpty(str);
        Preconditions.checkNotEmpty(str3);
        Preconditions.checkNotNull(obj);
        this.zztd = str;
        this.zzaek = str2;
        this.name = str3;
        this.zzaqu = j;
        this.value = obj;
    }
}
