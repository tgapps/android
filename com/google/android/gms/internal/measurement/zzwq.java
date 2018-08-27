package com.google.android.gms.internal.measurement;

final class zzwq {
    private static final zzwo zzcav = zzxf();
    private static final zzwo zzcaw = new zzwp();

    static zzwo zzxd() {
        return zzcav;
    }

    static zzwo zzxe() {
        return zzcaw;
    }

    private static zzwo zzxf() {
        try {
            return (zzwo) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception e) {
            return null;
        }
    }
}
