package com.google.android.gms.internal.measurement;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

final class zzxf {
    private static final zzxf zzcbs = new zzxf();
    private final zzxk zzcbt;
    private final ConcurrentMap<Class<?>, zzxj<?>> zzcbu = new ConcurrentHashMap();

    public static zzxf zzxn() {
        return zzcbs;
    }

    public final <T> zzxj<T> zzi(Class<T> cls) {
        zzvo.zza(cls, "messageType");
        zzxj<T> com_google_android_gms_internal_measurement_zzxj_T = (zzxj) this.zzcbu.get(cls);
        if (com_google_android_gms_internal_measurement_zzxj_T != null) {
            return com_google_android_gms_internal_measurement_zzxj_T;
        }
        zzxj<T> zzh = this.zzcbt.zzh(cls);
        zzvo.zza(cls, "messageType");
        zzvo.zza(zzh, "schema");
        com_google_android_gms_internal_measurement_zzxj_T = (zzxj) this.zzcbu.putIfAbsent(cls, zzh);
        return com_google_android_gms_internal_measurement_zzxj_T != null ? com_google_android_gms_internal_measurement_zzxj_T : zzh;
    }

    public final <T> zzxj<T> zzag(T t) {
        return zzi(t.getClass());
    }

    private zzxf() {
        zzxk com_google_android_gms_internal_measurement_zzxk = null;
        String[] strArr = new String[]{"com.google.protobuf.AndroidProto3SchemaFactory"};
        for (int i = 0; i <= 0; i++) {
            com_google_android_gms_internal_measurement_zzxk = zzgb(strArr[0]);
            if (com_google_android_gms_internal_measurement_zzxk != null) {
                break;
            }
        }
        if (com_google_android_gms_internal_measurement_zzxk == null) {
            com_google_android_gms_internal_measurement_zzxk = new zzwi();
        }
        this.zzcbt = com_google_android_gms_internal_measurement_zzxk;
    }

    private static zzxk zzgb(String str) {
        try {
            return (zzxk) Class.forName(str).getConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Throwable th) {
            return null;
        }
    }
}
