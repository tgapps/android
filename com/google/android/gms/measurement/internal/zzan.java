package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.measurement.AppMeasurement.Event;
import com.google.android.gms.measurement.AppMeasurement.Param;
import com.google.android.gms.measurement.AppMeasurement.UserProperty;
import java.util.concurrent.atomic.AtomicReference;

public final class zzan extends zzcp {
    private static final AtomicReference<String[]> zzalt = new AtomicReference();
    private static final AtomicReference<String[]> zzalu = new AtomicReference();
    private static final AtomicReference<String[]> zzalv = new AtomicReference();

    zzan(zzbt com_google_android_gms_measurement_internal_zzbt) {
        super(com_google_android_gms_measurement_internal_zzbt);
    }

    protected final boolean zzgt() {
        return false;
    }

    private final boolean zzjc() {
        zzgr();
        return this.zzadj.zzkj() && this.zzadj.zzgo().isLoggable(3);
    }

    protected final String zzbs(String str) {
        if (str == null) {
            return null;
        }
        return zzjc() ? zza(str, Event.zzadl, Event.zzadk, zzalt) : str;
    }

    protected final String zzbt(String str) {
        if (str == null) {
            return null;
        }
        return zzjc() ? zza(str, Param.zzadn, Param.zzadm, zzalu) : str;
    }

    protected final String zzbu(String str) {
        if (str == null) {
            return null;
        }
        if (!zzjc()) {
            return str;
        }
        if (!str.startsWith("_exp_")) {
            return zza(str, UserProperty.zzadp, UserProperty.zzado, zzalv);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("experiment_id");
        stringBuilder.append("(");
        stringBuilder.append(str);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private static String zza(String str, String[] strArr, String[] strArr2, AtomicReference<String[]> atomicReference) {
        int i = 0;
        Preconditions.checkNotNull(strArr);
        Preconditions.checkNotNull(strArr2);
        Preconditions.checkNotNull(atomicReference);
        Preconditions.checkArgument(strArr.length == strArr2.length);
        while (i < strArr.length) {
            if (zzfk.zzu(str, strArr[i])) {
                synchronized (atomicReference) {
                    String[] strArr3 = (String[]) atomicReference.get();
                    if (strArr3 == null) {
                        strArr3 = new String[strArr2.length];
                        atomicReference.set(strArr3);
                    }
                    if (strArr3[i] == null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(strArr2[i]);
                        stringBuilder.append("(");
                        stringBuilder.append(strArr[i]);
                        stringBuilder.append(")");
                        strArr3[i] = stringBuilder.toString();
                    }
                    str = strArr3[i];
                }
                return str;
            }
            i++;
        }
        return str;
    }

    protected final String zzb(zzad com_google_android_gms_measurement_internal_zzad) {
        if (com_google_android_gms_measurement_internal_zzad == null) {
            return null;
        }
        if (!zzjc()) {
            return com_google_android_gms_measurement_internal_zzad.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("origin=");
        stringBuilder.append(com_google_android_gms_measurement_internal_zzad.origin);
        stringBuilder.append(",name=");
        stringBuilder.append(zzbs(com_google_android_gms_measurement_internal_zzad.name));
        stringBuilder.append(",params=");
        stringBuilder.append(zzb(com_google_android_gms_measurement_internal_zzad.zzaid));
        return stringBuilder.toString();
    }

    protected final String zza(zzy com_google_android_gms_measurement_internal_zzy) {
        if (com_google_android_gms_measurement_internal_zzy == null) {
            return null;
        }
        if (!zzjc()) {
            return com_google_android_gms_measurement_internal_zzy.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Event{appId='");
        stringBuilder.append(com_google_android_gms_measurement_internal_zzy.zztt);
        stringBuilder.append("', name='");
        stringBuilder.append(zzbs(com_google_android_gms_measurement_internal_zzy.name));
        stringBuilder.append("', params=");
        stringBuilder.append(zzb(com_google_android_gms_measurement_internal_zzy.zzaid));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private final String zzb(zzaa com_google_android_gms_measurement_internal_zzaa) {
        if (com_google_android_gms_measurement_internal_zzaa == null) {
            return null;
        }
        if (zzjc()) {
            return zzd(com_google_android_gms_measurement_internal_zzaa.zziv());
        }
        return com_google_android_gms_measurement_internal_zzaa.toString();
    }

    protected final String zzd(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (!zzjc()) {
            return bundle.toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : bundle.keySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(", ");
            } else {
                stringBuilder.append("Bundle[{");
            }
            stringBuilder.append(zzbt(str));
            stringBuilder.append("=");
            stringBuilder.append(bundle.get(str));
        }
        stringBuilder.append("}]");
        return stringBuilder.toString();
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
