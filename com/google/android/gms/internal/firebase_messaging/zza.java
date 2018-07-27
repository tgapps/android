package com.google.android.gms.internal.firebase_messaging;

import java.io.PrintStream;

public final class zza {
    private static final zzb zza;
    private static final int zzb;

    static final class zza extends zzb {
        zza() {
        }

        public final void zza(Throwable th, Throwable th2) {
        }
    }

    static {
        Integer num = null;
        zzb com_google_android_gms_internal_firebase_messaging_zze;
        try {
            num = zza();
            if (num == null || num.intValue() < 19) {
                com_google_android_gms_internal_firebase_messaging_zze = (!Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic") ? 1 : null) != null ? new zze() : new zza();
                zza = com_google_android_gms_internal_firebase_messaging_zze;
                zzb = num != null ? 1 : num.intValue();
            }
            com_google_android_gms_internal_firebase_messaging_zze = new zzf();
            zza = com_google_android_gms_internal_firebase_messaging_zze;
            if (num != null) {
            }
            zzb = num != null ? 1 : num.intValue();
        } catch (Throwable th) {
            PrintStream printStream = System.err;
            String name = zza.class.getName();
            printStream.println(new StringBuilder(String.valueOf(name).length() + 132).append("An error has occured when initializing the try-with-resources desuguring strategy. The default strategy ").append(name).append("will be used. The error is: ").toString());
            th.printStackTrace(System.err);
            com_google_android_gms_internal_firebase_messaging_zze = new zza();
        }
    }

    private static Integer zza() {
        try {
            return (Integer) Class.forName("android.os.Build$VERSION").getField("SDK_INT").get(null);
        } catch (Exception e) {
            System.err.println("Failed to retrieve value from android.os.Build$VERSION.SDK_INT due to the following exception.");
            e.printStackTrace(System.err);
            return null;
        }
    }

    public static void zza(Throwable th, Throwable th2) {
        zza.zza(th, th2);
    }
}
