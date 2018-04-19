package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import java.util.Map;

public final class zzdx extends zzhj {
    private final Map<String, Long> zzada = new ArrayMap();
    private final Map<String, Integer> zzadb = new ArrayMap();
    private long zzadc;

    public zzdx(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final void zza(long j, zzig com_google_android_gms_internal_measurement_zzig) {
        if (com_google_android_gms_internal_measurement_zzig == null) {
            zzgg().zzir().log("Not logging ad exposure. No active activity");
        } else if (j < 1000) {
            zzgg().zzir().zzg("Not logging ad exposure. Less than 1000 ms. exposure", Long.valueOf(j));
        } else {
            Bundle bundle = new Bundle();
            bundle.putLong("_xt", j);
            zzih.zza(com_google_android_gms_internal_measurement_zzig, bundle, true);
            zzfu().zza("am", "_xa", bundle);
        }
    }

    private final void zza(String str, long j) {
        zzab();
        Preconditions.checkNotEmpty(str);
        if (this.zzadb.isEmpty()) {
            this.zzadc = j;
        }
        Integer num = (Integer) this.zzadb.get(str);
        if (num != null) {
            this.zzadb.put(str, Integer.valueOf(num.intValue() + 1));
        } else if (this.zzadb.size() >= 100) {
            zzgg().zzin().log("Too many ads visible");
        } else {
            this.zzadb.put(str, Integer.valueOf(1));
            this.zzada.put(str, Long.valueOf(j));
        }
    }

    private final void zza(String str, long j, zzig com_google_android_gms_internal_measurement_zzig) {
        if (com_google_android_gms_internal_measurement_zzig == null) {
            zzgg().zzir().log("Not logging ad unit exposure. No active activity");
        } else if (j < 1000) {
            zzgg().zzir().zzg("Not logging ad unit exposure. Less than 1000 ms. exposure", Long.valueOf(j));
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("_ai", str);
            bundle.putLong("_xt", j);
            zzih.zza(com_google_android_gms_internal_measurement_zzig, bundle, true);
            zzfu().zza("am", "_xu", bundle);
        }
    }

    private final void zzb(String str, long j) {
        zzab();
        Preconditions.checkNotEmpty(str);
        Integer num = (Integer) this.zzadb.get(str);
        if (num != null) {
            zzig zzkk = zzfy().zzkk();
            int intValue = num.intValue() - 1;
            if (intValue == 0) {
                this.zzadb.remove(str);
                Long l = (Long) this.zzada.get(str);
                if (l == null) {
                    zzgg().zzil().log("First ad unit exposure time was never set");
                } else {
                    long longValue = j - l.longValue();
                    this.zzada.remove(str);
                    zza(str, longValue, zzkk);
                }
                if (!this.zzadb.isEmpty()) {
                    return;
                }
                if (this.zzadc == 0) {
                    zzgg().zzil().log("First ad exposure time was never set");
                    return;
                }
                zza(j - this.zzadc, zzkk);
                this.zzadc = 0;
                return;
            }
            this.zzadb.put(str, Integer.valueOf(intValue));
            return;
        }
        zzgg().zzil().zzg("Call to endAdUnitExposure for unknown ad unit id", str);
    }

    private final void zzl(long j) {
        for (String put : this.zzada.keySet()) {
            this.zzada.put(put, Long.valueOf(j));
        }
        if (!this.zzada.isEmpty()) {
            this.zzadc = j;
        }
    }

    public final void beginAdUnitExposure(String str) {
        if (str == null || str.length() == 0) {
            zzgg().zzil().log("Ad unit id must be a non-empty string");
            return;
        }
        zzgf().zzc(new zzdy(this, str, zzbt().elapsedRealtime()));
    }

    public final void endAdUnitExposure(String str) {
        if (str == null || str.length() == 0) {
            zzgg().zzil().log("Ad unit id must be a non-empty string");
            return;
        }
        zzgf().zzc(new zzdz(this, str, zzbt().elapsedRealtime()));
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

    public final /* bridge */ /* synthetic */ void zzfq() {
        super.zzfq();
    }

    public final /* bridge */ /* synthetic */ void zzfr() {
        super.zzfr();
    }

    public final /* bridge */ /* synthetic */ zzdx zzfs() {
        return super.zzfs();
    }

    public final /* bridge */ /* synthetic */ zzee zzft() {
        return super.zzft();
    }

    public final /* bridge */ /* synthetic */ zzhm zzfu() {
        return super.zzfu();
    }

    public final /* bridge */ /* synthetic */ zzfb zzfv() {
        return super.zzfv();
    }

    public final /* bridge */ /* synthetic */ zzeo zzfw() {
        return super.zzfw();
    }

    public final /* bridge */ /* synthetic */ zzil zzfx() {
        return super.zzfx();
    }

    public final /* bridge */ /* synthetic */ zzih zzfy() {
        return super.zzfy();
    }

    public final /* bridge */ /* synthetic */ zzfc zzfz() {
        return super.zzfz();
    }

    public final /* bridge */ /* synthetic */ zzei zzga() {
        return super.zzga();
    }

    public final /* bridge */ /* synthetic */ zzfe zzgb() {
        return super.zzgb();
    }

    public final /* bridge */ /* synthetic */ zzjv zzgc() {
        return super.zzgc();
    }

    public final /* bridge */ /* synthetic */ zzgf zzgd() {
        return super.zzgd();
    }

    public final /* bridge */ /* synthetic */ zzjk zzge() {
        return super.zzge();
    }

    public final /* bridge */ /* synthetic */ zzgg zzgf() {
        return super.zzgf();
    }

    public final /* bridge */ /* synthetic */ zzfg zzgg() {
        return super.zzgg();
    }

    public final /* bridge */ /* synthetic */ zzfr zzgh() {
        return super.zzgh();
    }

    public final /* bridge */ /* synthetic */ zzeh zzgi() {
        return super.zzgi();
    }

    public final void zzk(long j) {
        zzig zzkk = zzfy().zzkk();
        for (String str : this.zzada.keySet()) {
            zza(str, j - ((Long) this.zzada.get(str)).longValue(), zzkk);
        }
        if (!this.zzada.isEmpty()) {
            zza(j - this.zzadc, zzkk);
        }
        zzl(j);
    }
}
