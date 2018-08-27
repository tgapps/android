package com.google.android.gms.internal.measurement;

final class zzua {
    private static final Class<?> zzbtv = zzfu("libcore.io.Memory");
    private static final boolean zzbtw = (zzfu("org.robolectric.Robolectric") != null);

    static boolean zzty() {
        return (zzbtv == null || zzbtw) ? false : true;
    }

    static Class<?> zztz() {
        return zzbtv;
    }

    private static <T> Class<T> zzfu(String str) {
        try {
            return Class.forName(str);
        } catch (Throwable th) {
            return null;
        }
    }
}
