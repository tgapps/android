package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences.Editor;
import com.google.android.gms.common.internal.Preconditions;

public final class zzfw {
    private String value;
    private boolean zzako;
    private final /* synthetic */ zzfr zzakp;
    private final String zzaku = null;
    private final String zznt;

    public zzfw(zzfr com_google_android_gms_internal_measurement_zzfr, String str, String str2) {
        this.zzakp = com_google_android_gms_internal_measurement_zzfr;
        Preconditions.checkNotEmpty(str);
        this.zznt = str;
    }

    public final void zzbn(String str) {
        if (!zzjv.zzs(str, this.value)) {
            Editor edit = this.zzakp.zziu().edit();
            edit.putString(this.zznt, str);
            edit.apply();
            this.value = str;
        }
    }

    public final String zzjc() {
        if (!this.zzako) {
            this.zzako = true;
            this.value = this.zzakp.zziu().getString(this.zznt, null);
        }
        return this.value;
    }
}
