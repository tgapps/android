package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.util.Map;

public final class zzdu extends zzhg {
    private final Map<String, Long> zzadf = new ArrayMap();
    private final Map<String, Integer> zzadg = new ArrayMap();
    private long zzadh;

    public zzdu(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final void zza(long j, zzie com_google_android_gms_internal_measurement_zzie) {
        if (com_google_android_gms_internal_measurement_zzie == null) {
            zzge().zzit().log("Not logging ad exposure. No active activity");
        } else if (j < 1000) {
            zzge().zzit().zzg("Not logging ad exposure. Less than 1000 ms. exposure", Long.valueOf(j));
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong("_xt", j);
            zzif.zza(com_google_android_gms_internal_measurement_zzie, bundle, true);
            zzfu().logEvent("am", "_xa", bundle);
        }
    }

    private final void zza(String str, long j) {
        zzab();
        Preconditions.checkNotEmpty(str);
        if (this.zzadg.isEmpty()) {
            this.zzadh = j;
        }
        Integer num = (Integer) this.zzadg.get(str);
        if (num != null) {
            this.zzadg.put(str, Integer.valueOf(num.intValue() + 1));
        } else if (this.zzadg.size() >= 100) {
            zzge().zzip().log("Too many ads visible");
        } else {
            this.zzadg.put(str, Integer.valueOf(1));
            this.zzadf.put(str, Long.valueOf(j));
        }
    }

    private final void zza(String str, long j, zzie com_google_android_gms_internal_measurement_zzie) {
        if (com_google_android_gms_internal_measurement_zzie == null) {
            zzge().zzit().log("Not logging ad unit exposure. No active activity");
        } else if (j < 1000) {
            zzge().zzit().zzg("Not logging ad unit exposure. Less than 1000 ms. exposure", Long.valueOf(j));
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("_ai", str);
            bundle.putLong("_xt", j);
            zzif.zza(com_google_android_gms_internal_measurement_zzie, bundle, true);
            zzfu().logEvent("am", "_xu", bundle);
        }
    }

    private final void zzb(String str, long j) {
        zzab();
        Preconditions.checkNotEmpty(str);
        Integer num = (Integer) this.zzadg.get(str);
        if (num != null) {
            zzie zzkc = zzfy().zzkc();
            int intValue = num.intValue() - 1;
            if (intValue == 0) {
                this.zzadg.remove(str);
                Long l = (Long) this.zzadf.get(str);
                if (l == null) {
                    zzge().zzim().log("First ad unit exposure time was never set");
                } else {
                    long longValue = j - l.longValue();
                    this.zzadf.remove(str);
                    zza(str, longValue, zzkc);
                }
                if (!this.zzadg.isEmpty()) {
                    return;
                }
                if (this.zzadh == 0) {
                    zzge().zzim().log("First ad exposure time was never set");
                    return;
                }
                zza(j - this.zzadh, zzkc);
                this.zzadh = 0;
                return;
            }
            this.zzadg.put(str, Integer.valueOf(intValue));
            return;
        }
        zzge().zzim().zzg("Call to endAdUnitExposure for unknown ad unit id", str);
    }

    private final void zzl(long j) {
        for (String put : this.zzadf.keySet()) {
            this.zzadf.put(put, Long.valueOf(j));
        }
        if (!this.zzadf.isEmpty()) {
            this.zzadh = j;
        }
    }

    public final void beginAdUnitExposure(String str) {
        if (str == null || str.length() == 0) {
            zzge().zzim().log("Ad unit id must be a non-empty string");
            return;
        }
        zzgd().zzc(new zzdv(this, str, zzbt().elapsedRealtime()));
    }

    public final void endAdUnitExposure(String str) {
        if (str == null || str.length() == 0) {
            zzge().zzim().log("Ad unit id must be a non-empty string");
            return;
        }
        zzgd().zzc(new zzdw(this, str, zzbt().elapsedRealtime()));
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ void zzab() {
        super.zzab();
    }

    public final /* bridge */ /* synthetic */ Clock zzbt() {
        return super.zzbt();
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ void zzfs() {
        super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzdu zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhk zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzii zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzif zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzfe zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzka zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjh zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzfg zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzef zzgg() {
        return super.zzgg();
    }

    public final void zzk(long j) {
        zzie zzkc = zzfy().zzkc();
        for (String str : this.zzadf.keySet()) {
            zza(str, j - ((Long) this.zzadf.get(str)).longValue(), zzkc);
        }
        if (!this.zzadf.isEmpty()) {
            zza(j - this.zzadh, zzkc);
        }
        zzl(j);
    }
}
