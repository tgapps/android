package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.measurement.AppMeasurement;

public final class zzap extends zzcp {
    private long zzadt = -1;
    private char zzalw = '\u0000';
    private String zzalx;
    private final zzar zzaly = new zzar(this, 6, false, false);
    private final zzar zzalz = new zzar(this, 6, true, false);
    private final zzar zzama = new zzar(this, 6, false, true);
    private final zzar zzamb = new zzar(this, 5, false, false);
    private final zzar zzamc = new zzar(this, 5, true, false);
    private final zzar zzamd = new zzar(this, 5, false, true);
    private final zzar zzame = new zzar(this, 4, false, false);
    private final zzar zzamf = new zzar(this, 3, false, false);
    private final zzar zzamg = new zzar(this, 2, false, false);

    zzap(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
    }

    public final zzar zzjd() {
        return this.zzaly;
    }

    public final zzar zzje() {
        return this.zzalz;
    }

    public final zzar zzjf() {
        return this.zzama;
    }

    public final zzar zzjg() {
        return this.zzamb;
    }

    public final zzar zzjh() {
        return this.zzamc;
    }

    public final zzar zzji() {
        return this.zzamd;
    }

    public final zzar zzjj() {
        return this.zzame;
    }

    public final zzar zzjk() {
        return this.zzamf;
    }

    public final zzar zzjl() {
        return this.zzamg;
    }

    protected final boolean zzgt() {
        return false;
    }

    protected static Object zzbv(String str) {
        if (str == null) {
            return null;
        }
        return new zzas(str);
    }

    protected final void zza(int i, boolean z, boolean z2, String str, Object obj, Object obj2, Object obj3) {
        int i2 = 0;
        if (!z && isLoggable(i)) {
            zza(i, zza(false, str, obj, obj2, obj3));
        }
        if (!z2 && i >= 5) {
            Preconditions.checkNotNull(str);
            zzcp zzkh = this.zzadj.zzkh();
            if (zzkh == null) {
                zza(6, "Scheduler not set. Not logging error/warn");
            } else if (zzkh.isInitialized()) {
                if (i >= 0) {
                    i2 = i;
                }
                if (i2 >= 9) {
                    i2 = 8;
                }
                zzkh.zzc(new zzaq(this, i2, str, obj, obj2, obj3));
            } else {
                zza(6, "Scheduler not initialized. Not logging error/warn");
            }
        }
    }

    protected final boolean isLoggable(int i) {
        return Log.isLoggable(zzjm(), i);
    }

    protected final void zza(int i, String str) {
        Log.println(i, zzjm(), str);
    }

    private final String zzjm() {
        String str;
        synchronized (this) {
            if (this.zzalx == null) {
                if (this.zzadj.zzkm() != null) {
                    this.zzalx = this.zzadj.zzkm();
                } else {
                    this.zzalx = zzn.zzht();
                }
            }
            str = this.zzalx;
        }
        return str;
    }

    static String zza(boolean z, String str, Object obj, Object obj2, Object obj3) {
        if (str == null) {
            str = TtmlNode.ANONYMOUS_REGION_ID;
        }
        Object zza = zza(z, obj);
        Object zza2 = zza(z, obj2);
        Object zza3 = zza(z, obj3);
        StringBuilder stringBuilder = new StringBuilder();
        String str2 = TtmlNode.ANONYMOUS_REGION_ID;
        if (!TextUtils.isEmpty(str)) {
            stringBuilder.append(str);
            str2 = ": ";
        }
        if (!TextUtils.isEmpty(zza)) {
            stringBuilder.append(str2);
            stringBuilder.append(zza);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zza2)) {
            stringBuilder.append(str2);
            stringBuilder.append(zza2);
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(zza3)) {
            stringBuilder.append(str2);
            stringBuilder.append(zza3);
        }
        return stringBuilder.toString();
    }

    private static String zza(boolean z, Object obj) {
        if (obj == null) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        Object valueOf;
        if (obj instanceof Integer) {
            valueOf = Long.valueOf((long) ((Integer) obj).intValue());
        } else {
            valueOf = obj;
        }
        if (valueOf instanceof Long) {
            if (!z) {
                return String.valueOf(valueOf);
            }
            if (Math.abs(((Long) valueOf).longValue()) < 100) {
                return String.valueOf(valueOf);
            }
            String str = String.valueOf(valueOf).charAt(0) == '-' ? "-" : TtmlNode.ANONYMOUS_REGION_ID;
            String valueOf2 = String.valueOf(Math.abs(((Long) valueOf).longValue()));
            return new StringBuilder((String.valueOf(str).length() + 43) + String.valueOf(str).length()).append(str).append(Math.round(Math.pow(10.0d, (double) (valueOf2.length() - 1)))).append("...").append(str).append(Math.round(Math.pow(10.0d, (double) valueOf2.length()) - 1.0d)).toString();
        } else if (valueOf instanceof Boolean) {
            return String.valueOf(valueOf);
        } else {
            if (valueOf instanceof Throwable) {
                Throwable th = (Throwable) valueOf;
                StringBuilder stringBuilder = new StringBuilder(z ? th.getClass().getName() : th.toString());
                String zzbw = zzbw(AppMeasurement.class.getCanonicalName());
                String zzbw2 = zzbw(zzbt.class.getCanonicalName());
                for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                    if (!stackTraceElement.isNativeMethod()) {
                        String className = stackTraceElement.getClassName();
                        if (className != null) {
                            className = zzbw(className);
                            if (className.equals(zzbw) || className.equals(zzbw2)) {
                                stringBuilder.append(": ");
                                stringBuilder.append(stackTraceElement);
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                }
                return stringBuilder.toString();
            } else if (valueOf instanceof zzas) {
                return ((zzas) valueOf).zzamp;
            } else {
                if (z) {
                    return "-";
                }
                return String.valueOf(valueOf);
            }
        }
    }

    private static String zzbw(String str) {
        if (TextUtils.isEmpty(str)) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf != -1 ? str.substring(0, lastIndexOf) : str;
    }

    public final String zzjn() {
        Pair zzfm = zzgp().zzand.zzfm();
        if (zzfm == null || zzfm == zzba.zzanc) {
            return null;
        }
        String valueOf = String.valueOf(zzfm.second);
        String str = (String) zzfm.first;
        return new StringBuilder((String.valueOf(valueOf).length() + 1) + String.valueOf(str).length()).append(valueOf).append(":").append(str).toString();
    }

    public final /* bridge */ /* synthetic */ void zzga() {
        super.zzga();
    }

    public final /* bridge */ /* synthetic */ void zzgb() {
        super.zzgb();
    }

    public final /* bridge */ /* synthetic */ void zzgc() {
        super.zzgc();
    }

    public final /* bridge */ /* synthetic */ void zzaf() {
        super.zzaf();
    }

    public final /* bridge */ /* synthetic */ zzx zzgk() {
        return super.zzgk();
    }

    public final /* bridge */ /* synthetic */ Clock zzbx() {
        return super.zzbx();
    }

    public final /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public final /* bridge */ /* synthetic */ zzan zzgl() {
        return super.zzgl();
    }

    public final /* bridge */ /* synthetic */ zzfk zzgm() {
        return super.zzgm();
    }

    public final /* bridge */ /* synthetic */ zzbo zzgn() {
        return super.zzgn();
    }

    public final /* bridge */ /* synthetic */ zzap zzgo() {
        return super.zzgo();
    }

    public final /* bridge */ /* synthetic */ zzba zzgp() {
        return super.zzgp();
    }

    public final /* bridge */ /* synthetic */ zzn zzgq() {
        return super.zzgq();
    }

    public final /* bridge */ /* synthetic */ zzk zzgr() {
        return super.zzgr();
    }
}
