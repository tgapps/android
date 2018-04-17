package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences.Editor;
import com.google.android.gms.common.internal.Preconditions;

public final class zzfu {
    private long value;
    private boolean zzako;
    private final /* synthetic */ zzfr zzakp;
    private final long zzakq;
    private final String zznt;

    public zzfu(zzfr com_google_android_gms_internal_measurement_zzfr, String str, long j) {
        this.zzakp = com_google_android_gms_internal_measurement_zzfr;
        Preconditions.checkNotEmpty(str);
        this.zznt = str;
        this.zzakq = j;
    }

    public final long get() {
        if (!this.zzako) {
            this.zzako = true;
            this.value = this.zzakp.zziu().getLong(this.zznt, this.zzakq);
        }
        return this.value;
    }

    public final void set(long j) {
        Editor edit = this.zzakp.zziu().edit();
        edit.putLong(this.zznt, j);
        edit.apply();
        this.value = j;
    }
}
