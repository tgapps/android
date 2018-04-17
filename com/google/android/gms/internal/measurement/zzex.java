package com.google.android.gms.internal.measurement;

public final class zzex<V> {
    private final V zzaid;
    private final String zznt;
    private final V zzzw;

    private zzex(String str, V v, V v2) {
        this.zzzw = v;
        this.zzaid = v2;
        this.zznt = str;
    }

    static zzex<Double> zza(String str, double d, double d2) {
        zzex<Double> com_google_android_gms_internal_measurement_zzex = new zzex(str, Double.valueOf(-3.0d), Double.valueOf(-3.0d));
        zzew.zzage.add(com_google_android_gms_internal_measurement_zzex);
        return com_google_android_gms_internal_measurement_zzex;
    }

    static zzex<Long> zzb(String str, long j, long j2) {
        zzex<Long> com_google_android_gms_internal_measurement_zzex = new zzex(str, Long.valueOf(j), Long.valueOf(j2));
        zzew.zzagb.add(com_google_android_gms_internal_measurement_zzex);
        return com_google_android_gms_internal_measurement_zzex;
    }

    static zzex<Boolean> zzb(String str, boolean z, boolean z2) {
        zzex<Boolean> com_google_android_gms_internal_measurement_zzex = new zzex(str, Boolean.valueOf(z), Boolean.valueOf(z2));
        zzew.zzagc.add(com_google_android_gms_internal_measurement_zzex);
        return com_google_android_gms_internal_measurement_zzex;
    }

    static zzex<Integer> zzc(String str, int i, int i2) {
        zzex<Integer> com_google_android_gms_internal_measurement_zzex = new zzex(str, Integer.valueOf(i), Integer.valueOf(i2));
        zzew.zzaga.add(com_google_android_gms_internal_measurement_zzex);
        return com_google_android_gms_internal_measurement_zzex;
    }

    static zzex<String> zzd(String str, String str2, String str3) {
        zzex<String> com_google_android_gms_internal_measurement_zzex = new zzex(str, str2, str3);
        zzew.zzagd.add(com_google_android_gms_internal_measurement_zzex);
        return com_google_android_gms_internal_measurement_zzex;
    }

    public final V get() {
        return this.zzzw;
    }

    public final V get(V v) {
        return v != null ? v : this.zzzw;
    }

    public final String getKey() {
        return this.zznt;
    }
}
