package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences.Editor;
import com.google.android.gms.common.internal.Preconditions;

public final class zzbf {
    private String value;
    private boolean zzanx;
    private final /* synthetic */ zzba zzany;
    private final String zzaod = null;
    private final String zzoj;

    public zzbf(zzba com_google_android_gms_measurement_internal_zzba, String str, String str2) {
        this.zzany = com_google_android_gms_measurement_internal_zzba;
        Preconditions.checkNotEmpty(str);
        this.zzoj = str;
    }

    public final String zzjz() {
        if (!this.zzanx) {
            this.zzanx = true;
            this.value = this.zzany.zzjr().getString(this.zzoj, null);
        }
        return this.value;
    }

    public final void zzcc(String str) {
        if (!zzfk.zzu(str, this.value)) {
            Editor edit = this.zzany.zzjr().edit();
            edit.putString(this.zzoj, str);
            edit.apply();
            this.value = str;
        }
    }
}
