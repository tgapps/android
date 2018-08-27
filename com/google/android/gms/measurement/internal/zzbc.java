package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences.Editor;
import com.google.android.gms.common.internal.Preconditions;

public final class zzbc {
    private boolean value;
    private final boolean zzanw = true;
    private boolean zzanx;
    private final /* synthetic */ zzba zzany;
    private final String zzoj;

    public zzbc(zzba com_google_android_gms_measurement_internal_zzba, String str, boolean z) {
        this.zzany = com_google_android_gms_measurement_internal_zzba;
        Preconditions.checkNotEmpty(str);
        this.zzoj = str;
    }

    public final boolean get() {
        if (!this.zzanx) {
            this.zzanx = true;
            this.value = this.zzany.zzjr().getBoolean(this.zzoj, this.zzanw);
        }
        return this.value;
    }

    public final void set(boolean z) {
        Editor edit = this.zzany.zzjr().edit();
        edit.putBoolean(this.zzoj, z);
        edit.apply();
        this.value = z;
    }
}
