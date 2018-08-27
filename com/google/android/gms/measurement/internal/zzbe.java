package com.google.android.gms.measurement.internal;

import android.content.SharedPreferences.Editor;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;

public final class zzbe {
    private final long zzabv;
    private final /* synthetic */ zzba zzany;
    private final String zzaoa;
    private final String zzaob;
    private final String zzaoc;

    private zzbe(zzba com_google_android_gms_measurement_internal_zzba, String str, long j) {
        this.zzany = com_google_android_gms_measurement_internal_zzba;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkArgument(j > 0);
        this.zzaoa = String.valueOf(str).concat(":start");
        this.zzaob = String.valueOf(str).concat(":count");
        this.zzaoc = String.valueOf(str).concat(":value");
        this.zzabv = j;
    }

    private final void zzfl() {
        this.zzany.zzaf();
        long currentTimeMillis = this.zzany.zzbx().currentTimeMillis();
        Editor edit = this.zzany.zzjr().edit();
        edit.remove(this.zzaob);
        edit.remove(this.zzaoc);
        edit.putLong(this.zzaoa, currentTimeMillis);
        edit.apply();
    }

    public final void zzc(String str, long j) {
        this.zzany.zzaf();
        if (zzfn() == 0) {
            zzfl();
        }
        if (str == null) {
            str = TtmlNode.ANONYMOUS_REGION_ID;
        }
        long j2 = this.zzany.zzjr().getLong(this.zzaob, 0);
        if (j2 <= 0) {
            Editor edit = this.zzany.zzjr().edit();
            edit.putString(this.zzaoc, str);
            edit.putLong(this.zzaob, 1);
            edit.apply();
            return;
        }
        Object obj = (this.zzany.zzgm().zzmd().nextLong() & Long.MAX_VALUE) < Long.MAX_VALUE / (j2 + 1) ? 1 : null;
        Editor edit2 = this.zzany.zzjr().edit();
        if (obj != null) {
            edit2.putString(this.zzaoc, str);
        }
        edit2.putLong(this.zzaob, j2 + 1);
        edit2.apply();
    }

    public final Pair<String, Long> zzfm() {
        this.zzany.zzaf();
        this.zzany.zzaf();
        long zzfn = zzfn();
        if (zzfn == 0) {
            zzfl();
            zzfn = 0;
        } else {
            zzfn = Math.abs(zzfn - this.zzany.zzbx().currentTimeMillis());
        }
        if (zzfn < this.zzabv) {
            return null;
        }
        if (zzfn > (this.zzabv << 1)) {
            zzfl();
            return null;
        }
        String string = this.zzany.zzjr().getString(this.zzaoc, null);
        long j = this.zzany.zzjr().getLong(this.zzaob, 0);
        zzfl();
        if (string == null || j <= 0) {
            return zzba.zzanc;
        }
        return new Pair(string, Long.valueOf(j));
    }

    private final long zzfn() {
        return this.zzany.zzjr().getLong(this.zzaoa, 0);
    }
}
