package com.google.android.gms.internal.measurement;

import com.google.android.exoplayer2.C;
import java.nio.charset.Charset;
import java.util.Arrays;

public final class zzze {
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    protected static final Charset UTF_8 = Charset.forName(C.UTF8_NAME);
    public static final Object zzcfl = new Object();

    public static boolean equals(long[] jArr, long[] jArr2) {
        if (jArr == null || jArr.length == 0) {
            return jArr2 == null || jArr2.length == 0;
        } else {
            return Arrays.equals(jArr, jArr2);
        }
    }

    public static boolean equals(Object[] objArr, Object[] objArr2) {
        boolean length = objArr == null ? false : objArr.length;
        int length2 = objArr2 == null ? 0 : objArr2.length;
        int i = 0;
        boolean z = false;
        while (true) {
            if (z >= length || objArr[z] != null) {
                boolean z2;
                int i2 = i;
                while (i2 < length2 && objArr2[i2] == null) {
                    i2++;
                }
                boolean z3 = z >= length;
                if (i2 >= length2) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z3 && z2) {
                    return true;
                }
                if (z3 != z2 || !objArr[z].equals(objArr2[i2])) {
                    return false;
                }
                i = i2 + 1;
                z++;
            } else {
                z++;
            }
        }
    }

    public static int hashCode(long[] jArr) {
        return (jArr == null || jArr.length == 0) ? 0 : Arrays.hashCode(jArr);
    }

    public static int hashCode(Object[] objArr) {
        int length = objArr == null ? 0 : objArr.length;
        int i = 0;
        int i2 = 0;
        while (i < length) {
            int hashCode;
            Object obj = objArr[i];
            if (obj != null) {
                hashCode = obj.hashCode() + (i2 * 31);
            } else {
                hashCode = i2;
            }
            i++;
            i2 = hashCode;
        }
        return i2;
    }

    public static void zza(zzza com_google_android_gms_internal_measurement_zzza, zzza com_google_android_gms_internal_measurement_zzza2) {
        if (com_google_android_gms_internal_measurement_zzza.zzcfc != null) {
            com_google_android_gms_internal_measurement_zzza2.zzcfc = (zzzc) com_google_android_gms_internal_measurement_zzza.zzcfc.clone();
        }
    }
}
