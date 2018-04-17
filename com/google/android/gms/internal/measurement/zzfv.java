package com.google.android.gms.internal.measurement;

import android.content.SharedPreferences.Editor;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;

public final class zzfv {
    private final long zzabe;
    private final /* synthetic */ zzfr zzakp;
    private final String zzakr;
    private final String zzaks;
    private final String zzakt;

    private zzfv(zzfr com_google_android_gms_internal_measurement_zzfr, String str, long j) {
        this.zzakp = com_google_android_gms_internal_measurement_zzfr;
        Preconditions.checkNotEmpty(str);
        Preconditions.checkArgument(j > 0);
        this.zzakr = String.valueOf(str).concat(":start");
        this.zzaks = String.valueOf(str).concat(":count");
        this.zzakt = String.valueOf(str).concat(":value");
        this.zzabe = j;
    }

    private final void zzfg() {
        this.zzakp.zzab();
        long currentTimeMillis = this.zzakp.zzbt().currentTimeMillis();
        Editor edit = this.zzakp.zziu().edit();
        edit.remove(this.zzaks);
        edit.remove(this.zzakt);
        edit.putLong(this.zzakr, currentTimeMillis);
        edit.apply();
    }

    private final long zzfi() {
        return this.zzakp.zziu().getLong(this.zzakr, 0);
    }

    public final void zzc(String str, long j) {
        this.zzakp.zzab();
        if (zzfi() == 0) {
            zzfg();
        }
        if (str == null) {
            str = TtmlNode.ANONYMOUS_REGION_ID;
        }
        j = this.zzakp.zziu().getLong(this.zzaks, 0);
        if (j <= 0) {
            Editor edit = this.zzakp.zziu().edit();
            edit.putString(this.zzakt, str);
            edit.putLong(this.zzaks, 1);
            edit.apply();
            return;
        }
        long j2 = j + 1;
        Object obj = (this.zzakp.zzgc().zzku().nextLong() & Long.MAX_VALUE) < Long.MAX_VALUE / j2 ? 1 : null;
        Editor edit2 = this.zzakp.zziu().edit();
        if (obj != null) {
            edit2.putString(this.zzakt, str);
        }
        edit2.putLong(this.zzaks, j2);
        edit2.apply();
    }

    public final Pair<String, Long> zzfh() {
        this.zzakp.zzab();
        this.zzakp.zzab();
        long zzfi = zzfi();
        if (zzfi == 0) {
            zzfg();
            zzfi = 0;
        } else {
            zzfi = Math.abs(zzfi - this.zzakp.zzbt().currentTimeMillis());
        }
        if (zzfi < this.zzabe) {
            return null;
        }
        if (zzfi > (this.zzabe << 1)) {
            zzfg();
            return null;
        }
        String string = this.zzakp.zziu().getString(this.zzakt, null);
        long j = this.zzakp.zziu().getLong(this.zzaks, 0);
        zzfg();
        if (string != null) {
            if (j > 0) {
                return new Pair(string, Long.valueOf(j));
            }
        }
        return zzfr.zzajr;
    }
}
