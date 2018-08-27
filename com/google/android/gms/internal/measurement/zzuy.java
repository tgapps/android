package com.google.android.gms.internal.measurement;

final class zzuy {
    private static final Class<?> zzbvi = zzvk();

    private static Class<?> zzvk() {
        try {
            return Class.forName("com.google.protobuf.ExtensionRegistry");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static zzuz zzvl() {
        if (zzbvi != null) {
            try {
                return zzfz("getEmptyRegistry");
            } catch (Exception e) {
            }
        }
        return zzuz.zzbvm;
    }

    static zzuz zzvm() {
        zzuz com_google_android_gms_internal_measurement_zzuz = null;
        if (zzbvi != null) {
            try {
                com_google_android_gms_internal_measurement_zzuz = zzfz("loadGeneratedRegistry");
            } catch (Exception e) {
            }
        }
        if (com_google_android_gms_internal_measurement_zzuz == null) {
            com_google_android_gms_internal_measurement_zzuz = zzuz.zzvm();
        }
        return com_google_android_gms_internal_measurement_zzuz == null ? zzvl() : com_google_android_gms_internal_measurement_zzuz;
    }

    private static final zzuz zzfz(String str) throws Exception {
        return (zzuz) zzbvi.getDeclaredMethod(str, new Class[0]).invoke(null, new Object[0]);
    }
}
