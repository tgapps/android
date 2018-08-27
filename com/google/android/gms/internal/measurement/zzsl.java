package com.google.android.gms.internal.measurement;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build.VERSION;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import javax.annotation.Nullable;

public abstract class zzsl<T> {
    private static final Object zzbqy = new Object();
    private static boolean zzbqz = false;
    private static volatile Boolean zzbra = null;
    @SuppressLint({"StaticFieldLeak"})
    private static Context zzri = null;
    private final zzsv zzbrb;
    final String zzbrc;
    private final String zzbrd;
    private final T zzbre;
    private T zzbrf;
    private volatile zzsi zzbrg;
    private volatile SharedPreferences zzbrh;

    public static void init(Context context) {
        synchronized (zzbqy) {
            if (VERSION.SDK_INT < 24 || !context.isDeviceProtectedStorage()) {
                Context applicationContext = context.getApplicationContext();
                if (applicationContext != null) {
                    context = applicationContext;
                }
            }
            if (zzri != context) {
                zzbra = null;
            }
            zzri = context;
        }
        zzbqz = false;
    }

    protected abstract T zzfj(String str);

    private zzsl(zzsv com_google_android_gms_internal_measurement_zzsv, String str, T t) {
        this.zzbrf = null;
        this.zzbrg = null;
        this.zzbrh = null;
        if (com_google_android_gms_internal_measurement_zzsv.zzbrn == null) {
            throw new IllegalArgumentException("Must pass a valid SharedPreferences file name or ContentProvider URI");
        }
        this.zzbrb = com_google_android_gms_internal_measurement_zzsv;
        String valueOf = String.valueOf(com_google_android_gms_internal_measurement_zzsv.zzbro);
        String valueOf2 = String.valueOf(str);
        this.zzbrd = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        valueOf = String.valueOf(com_google_android_gms_internal_measurement_zzsv.zzbrp);
        valueOf2 = String.valueOf(str);
        this.zzbrc = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
        this.zzbre = t;
    }

    public final T getDefaultValue() {
        return this.zzbre;
    }

    public final T get() {
        if (zzri == null) {
            throw new IllegalStateException("Must call PhenotypeFlag.init() first");
        }
        zzsv com_google_android_gms_internal_measurement_zzsv = this.zzbrb;
        T zzte = zzte();
        if (zzte != null) {
            return zzte;
        }
        zzte = zztf();
        return zzte == null ? this.zzbre : zzte;
    }

    @TargetApi(24)
    @Nullable
    private final T zzte() {
        boolean z = false;
        zzsv com_google_android_gms_internal_measurement_zzsv = this.zzbrb;
        if (zzd("gms:phenotype:phenotype_flag:debug_bypass_phenotype", false)) {
            z = true;
        }
        String valueOf;
        if (z) {
            String str = "PhenotypeFlag";
            String str2 = "Bypass reading Phenotype values for flag: ";
            valueOf = String.valueOf(this.zzbrc);
            Log.w(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        } else if (this.zzbrb.zzbrn != null) {
            zzsi zztg = zztg();
            if (zztg != null) {
                valueOf = (String) zza(new zzsm(this, zztg));
                if (valueOf != null) {
                    return zzfj(valueOf);
                }
            }
        } else {
            zzsv com_google_android_gms_internal_measurement_zzsv2 = this.zzbrb;
        }
        return null;
    }

    @Nullable
    private final T zztf() {
        String str;
        zzsv com_google_android_gms_internal_measurement_zzsv = this.zzbrb;
        if (zzth()) {
            try {
                str = (String) zza(new zzsn(this));
                if (str != null) {
                    return zzfj(str);
                }
            } catch (Throwable e) {
                Throwable th = e;
                String str2 = "PhenotypeFlag";
                String str3 = "Unable to read GServices for flag: ";
                str = String.valueOf(this.zzbrc);
                Log.e(str2, str.length() != 0 ? str3.concat(str) : new String(str3), th);
            }
        }
        return null;
    }

    private static <V> V zza(zzsu<V> com_google_android_gms_internal_measurement_zzsu_V) {
        V zztj;
        long clearCallingIdentity;
        try {
            zztj = com_google_android_gms_internal_measurement_zzsu_V.zztj();
        } catch (SecurityException e) {
            clearCallingIdentity = Binder.clearCallingIdentity();
            zztj = com_google_android_gms_internal_measurement_zzsu_V.zztj();
        } finally {
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
        return zztj;
    }

    private final zzsi zztg() {
        if (this.zzbrg == null) {
            try {
                this.zzbrg = zzsi.zza(zzri.getContentResolver(), this.zzbrb.zzbrn);
            } catch (SecurityException e) {
            }
        }
        return this.zzbrg;
    }

    static boolean zzd(String str, boolean z) {
        try {
            if (zzth()) {
                return ((Boolean) zza(new zzso(str, false))).booleanValue();
            }
            return false;
        } catch (Throwable e) {
            Log.e("PhenotypeFlag", "Unable to read GServices, returning default value.", e);
            return false;
        }
    }

    private static boolean zzth() {
        boolean z = false;
        if (zzbra == null) {
            if (zzri == null) {
                return false;
            }
            if (PermissionChecker.checkSelfPermission(zzri, "com.google.android.providers.gsf.permission.READ_GSERVICES") == 0) {
                z = true;
            }
            zzbra = Boolean.valueOf(z);
        }
        return zzbra.booleanValue();
    }

    private static zzsl<Long> zza(zzsv com_google_android_gms_internal_measurement_zzsv, String str, long j) {
        return new zzsp(com_google_android_gms_internal_measurement_zzsv, str, Long.valueOf(j));
    }

    private static zzsl<Integer> zza(zzsv com_google_android_gms_internal_measurement_zzsv, String str, int i) {
        return new zzsq(com_google_android_gms_internal_measurement_zzsv, str, Integer.valueOf(i));
    }

    private static zzsl<Boolean> zza(zzsv com_google_android_gms_internal_measurement_zzsv, String str, boolean z) {
        return new zzsr(com_google_android_gms_internal_measurement_zzsv, str, Boolean.valueOf(z));
    }

    private static zzsl<Double> zza(zzsv com_google_android_gms_internal_measurement_zzsv, String str, double d) {
        return new zzss(com_google_android_gms_internal_measurement_zzsv, str, Double.valueOf(d));
    }

    private static zzsl<String> zza(zzsv com_google_android_gms_internal_measurement_zzsv, String str, String str2) {
        return new zzst(com_google_android_gms_internal_measurement_zzsv, str, str2);
    }

    final /* synthetic */ String zzti() {
        return zzsg.zza(zzri.getContentResolver(), this.zzbrd, null);
    }
}
