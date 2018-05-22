package com.google.android.gms.internal.firebase_messaging;

import java.io.PrintStream;

public final class zzh {
    private static final zzi zze;
    private static final int zzf;

    static final class zza extends zzi {
        zza() {
        }

        public final void zza(Throwable th, Throwable th2) {
        }
    }

    static {
        Integer num = null;
        zzi com_google_android_gms_internal_firebase_messaging_zzl;
        try {
            num = zza();
            if (num == null || num.intValue() < 19) {
                com_google_android_gms_internal_firebase_messaging_zzl = (!Boolean.getBoolean("com.google.devtools.build.android.desugar.runtime.twr_disable_mimic") ? 1 : null) != null ? new zzl() : new zza();
                zze = com_google_android_gms_internal_firebase_messaging_zzl;
                zzf = num != null ? 1 : num.intValue();
            }
            com_google_android_gms_internal_firebase_messaging_zzl = new zzm();
            zze = com_google_android_gms_internal_firebase_messaging_zzl;
            if (num != null) {
            }
            zzf = num != null ? 1 : num.intValue();
        } catch (Throwable th) {
            PrintStream printStream = System.err;
            String name = zza.class.getName();
            printStream.println(new StringBuilder(String.valueOf(name).length() + 132).append("An error has occured when initializing the try-with-resources desuguring strategy. The default strategy ").append(name).append("will be used. The error is: ").toString());
            th.printStackTrace(System.err);
            com_google_android_gms_internal_firebase_messaging_zzl = new zza();
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
        zze.zza(th, th2);
    }
}
