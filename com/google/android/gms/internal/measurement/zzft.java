package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences.Editor;
import com.google.android.gms.common.internal.Preconditions;

public final class zzft {
    private boolean value;
    private final boolean zzako = true;
    private boolean zzakp;
    private final /* synthetic */ zzfr zzakq;
    private final String zzny;

    public zzft(zzfr com_google_android_gms_internal_measurement_zzfr, String str, boolean z) {
        this.zzakq = com_google_android_gms_internal_measurement_zzfr;
        Preconditions.checkNotEmpty(str);
        this.zzny = str;
    }

    public final boolean get() {
        if (!this.zzakp) {
            this.zzakp = true;
            this.value = this.zzakq.zziy().getBoolean(this.zzny, this.zzako);
        }
        return this.value;
    }

    public final void set(boolean z) {
        Editor edit = this.zzakq.zziy().edit();
        edit.putBoolean(this.zzny, z);
        edit.apply();
        this.value = z;
    }
}
