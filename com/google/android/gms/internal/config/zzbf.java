package com.google.android.gms.internal.config;

import java.nio.charset.Charset;
import java.util.Arrays;
import org.telegram.messenger.exoplayer2.C;

public final class zzbf {
    private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
    protected static final Charset UTF_8 = Charset.forName(C.UTF8_NAME);
    public static final Object zzcq = new Object();

    public static boolean equals(Object[] objArr, Object[] objArr2) {
        int length = objArr == null ? 0 : objArr.length;
        int length2 = objArr2 == null ? 0 : objArr2.length;
        int i = 0;
        int i2 = i;
        while (true) {
            if (i >= length || objArr[i] != null) {
                while (i2 < length2 && objArr2[i2] == null) {
                    i2++;
                }
                boolean z = i >= length;
                boolean z2 = i2 >= length2;
                if (z && z2) {
                    return true;
                }
                if (z != z2 || !objArr[i].equals(objArr2[i2])) {
                    return false;
                }
                i++;
                i2++;
            } else {
                i++;
            }
        }
    }

    public static int hashCode(Object[] objArr) {
        int i = 0;
        int length = objArr == null ? 0 : objArr.length;
        int i2 = 0;
        while (i < length) {
            Object obj = objArr[i];
            if (obj != null) {
                i2 = (i2 * 31) + obj.hashCode();
            }
            i++;
        }
        return i2;
    }

    public static int zza(byte[][] bArr) {
        int i = 0;
        int length = bArr == null ? 0 : bArr.length;
        int i2 = 0;
        while (i < length) {
            byte[] bArr2 = bArr[i];
            if (bArr2 != null) {
                i2 = (i2 * 31) + Arrays.hashCode(bArr2);
            }
            i++;
        }
        return i2;
    }

    public static void zza(zzbb com_google_android_gms_internal_config_zzbb, zzbb com_google_android_gms_internal_config_zzbb2) {
        if (com_google_android_gms_internal_config_zzbb.zzci != null) {
            com_google_android_gms_internal_config_zzbb2.zzci = (zzbd) com_google_android_gms_internal_config_zzbb.zzci.clone();
        }
    }

    public static boolean zza(byte[][] bArr, byte[][] bArr2) {
        int length = bArr == null ? 0 : bArr.length;
        int length2 = bArr2 == null ? 0 : bArr2.length;
        int i = 0;
        int i2 = i;
        while (true) {
            if (i >= length || bArr[i] != null) {
                while (i2 < length2 && bArr2[i2] == null) {
                    i2++;
                }
                boolean z = i >= length;
                boolean z2 = i2 >= length2;
                if (z && z2) {
                    return true;
                }
                if (z != z2 || !Arrays.equals(bArr[i], bArr2[i2])) {
                    return false;
                }
                i++;
                i2++;
            } else {
                i++;
            }
        }
    }
}
