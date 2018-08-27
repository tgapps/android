package com.google.android.gms.measurement.internal;

import android.os.Bundle;

public final class zzak {
    public final String origin;
    public final long zzadt;
    public final long zzadu;
    public final boolean zzadv;
    public final String zzadw;
    public final String zzadx;
    public final Bundle zzady;

    zzak(long j, long j2, boolean z, String str, String str2, String str3, Bundle bundle) {
        this.zzadt = j;
        this.zzadu = j2;
        this.zzadv = z;
        this.zzadw = str;
        this.origin = str2;
        this.zzadx = str3;
        this.zzady = bundle;
    }

    public static final zzak zzc(Bundle bundle) {
        return new zzak(0, 0, true, null, null, null, bundle);
    }
}
