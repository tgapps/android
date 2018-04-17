package com.google.android.gms.internal.measurement;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.common.util.Clock;

public final class zzjk extends zzhk {
    private Handler handler;
    private long zzaqo = zzbt().elapsedRealtime();
    private final zzem zzaqp = new zzjl(this, this.zzacr);
    private final zzem zzaqq = new zzjm(this, this.zzacr);

    zzjk(zzgl com_google_android_gms_internal_measurement_zzgl) {
        super(com_google_android_gms_internal_measurement_zzgl);
    }

    private final void zzaf(long j) {
        zzem com_google_android_gms_internal_measurement_zzem;
        long j2;
        zzab();
        zzkq();
        this.zzaqp.cancel();
        this.zzaqq.cancel();
        zzgg().zzir().zzg("Activity resumed, time", Long.valueOf(j));
        this.zzaqo = j;
        if (zzbt().currentTimeMillis() - zzgh().zzaki.get() > zzgh().zzakk.get()) {
            zzgh().zzakj.set(true);
            zzgh().zzakl.set(0);
        }
        if (zzgh().zzakj.get()) {
            com_google_android_gms_internal_measurement_zzem = this.zzaqp;
            j2 = zzgh().zzakh.get();
        } else {
            com_google_android_gms_internal_measurement_zzem = this.zzaqq;
            j2 = 3600000;
        }
        com_google_android_gms_internal_measurement_zzem.zzh(Math.max(0, j2 - zzgh().zzakl.get()));
    }

    private final void zzag(long j) {
        zzab();
        zzkq();
        this.zzaqp.cancel();
        this.zzaqq.cancel();
        zzgg().zzir().zzg("Activity paused, time", Long.valueOf(j));
        if (this.zzaqo != 0) {
            zzgh().zzakl.set(zzgh().zzakl.get() + (j - this.zzaqo));
        }
    }

    private final void zzkq() {
        synchronized (this) {
            if (this.handler == null) {
                this.handler = new Handler(Looper.getMainLooper());
            }
        }
    }

    private final void zzkr() {
        zzab();
        zzm(false);
        zzfs().zzk(zzbt().elapsedRealtime());
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

    protected final boolean zzhh() {
        return false;
    }

    public final boolean zzm(boolean z) {
        zzab();
        zzch();
        long elapsedRealtime = zzbt().elapsedRealtime();
        zzgh().zzakk.set(zzbt().currentTimeMillis());
        long j = elapsedRealtime - this.zzaqo;
        if (z || j >= 1000) {
            zzgh().zzakl.set(j);
            zzgg().zzir().zzg("Recording user engagement, ms", Long.valueOf(j));
            Bundle bundle = new Bundle();
            bundle.putLong("_et", j);
            zzih.zza(zzfy().zzkk(), bundle, true);
            zzfu().zza("auto", "_e", bundle);
            this.zzaqo = elapsedRealtime;
            this.zzaqq.cancel();
            this.zzaqq.zzh(Math.max(0, 3600000 - zzgh().zzakl.get()));
            return true;
        }
        zzgg().zzir().zzg("Screen exposed for less than 1000 ms. Event not sent. time", Long.valueOf(j));
        return false;
    }
}
