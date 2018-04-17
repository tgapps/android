package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences.Editor;
import com.google.android.gms.common.internal.Preconditions;

public final class zzft {
    private boolean value;
    private final boolean zzakn = true;
    private boolean zzako;
    private final /* synthetic */ zzfr zzakp;
    private final String zznt;

    public zzft(zzfr com_google_android_gms_internal_measurement_zzfr, String str, boolean z) {
        this.zzakp = com_google_android_gms_internal_measurement_zzfr;
        Preconditions.checkNotEmpty(str);
        this.zznt = str;
    }

    public final boolean get() {
        if (!this.zzako) {
            this.zzako = true;
            this.value = this.zzakp.zziu().getBoolean(this.zznt, this.zzakn);
        }
        return this.value;
    }

    public final void set(boolean z) {
        Editor edit = this.zzakp.zziu().edit();
        edit.putBoolean(this.zznt, z);
        edit.apply();
        this.value = z;
    }
}
