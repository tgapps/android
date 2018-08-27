package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.nio.charset.Charset;

class zzum extends zzul {
    protected final byte[] zzbug;

    zzum(byte[] bArr) {
        if (bArr == null) {
            throw new NullPointerException();
        }
        this.zzbug = bArr;
    }

    public byte zzal(int i) {
        return this.zzbug[i];
    }

    public int size() {
        return this.zzbug.length;
    }

    public final zzud zzb(int i, int i2) {
        int zzb = zzud.zzb(0, i2, size());
        if (zzb == 0) {
            return zzud.zzbtz;
        }
        return new zzuh(this.zzbug, zzud(), zzb);
    }

    final void zza(zzuc com_google_android_gms_internal_measurement_zzuc) throws IOException {
        com_google_android_gms_internal_measurement_zzuc.zza(this.zzbug, zzud(), size());
    }

    protected final String zza(Charset charset) {
        return new String(this.zzbug, zzud(), size(), charset);
    }

    public final boolean zzub() {
        int zzud = zzud();
        return zzyj.zzf(this.zzbug, zzud, size() + zzud);
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzud)) {
            return false;
        }
        if (size() != ((zzud) obj).size()) {
            return false;
        }
        if (size() == 0) {
            return true;
        }
        if (!(obj instanceof zzum)) {
            return obj.equals(this);
        }
        zzum com_google_android_gms_internal_measurement_zzum = (zzum) obj;
        int zzuc = zzuc();
        int zzuc2 = com_google_android_gms_internal_measurement_zzum.zzuc();
        if (zzuc == 0 || zzuc2 == 0 || zzuc == zzuc2) {
            return zza((zzum) obj, 0, size());
        }
        return false;
    }

    final boolean zza(zzud com_google_android_gms_internal_measurement_zzud, int i, int i2) {
        if (i2 > com_google_android_gms_internal_measurement_zzud.size()) {
            throw new IllegalArgumentException("Length too large: " + i2 + size());
        } else if (i2 > com_google_android_gms_internal_measurement_zzud.size()) {
            throw new IllegalArgumentException("Ran off end of other: 0, " + i2 + ", " + com_google_android_gms_internal_measurement_zzud.size());
        } else if (!(com_google_android_gms_internal_measurement_zzud instanceof zzum)) {
            return com_google_android_gms_internal_measurement_zzud.zzb(0, i2).equals(zzb(0, i2));
        } else {
            zzum com_google_android_gms_internal_measurement_zzum = (zzum) com_google_android_gms_internal_measurement_zzud;
            byte[] bArr = this.zzbug;
            byte[] bArr2 = com_google_android_gms_internal_measurement_zzum.zzbug;
            int zzud = zzud() + i2;
            int zzud2 = zzud();
            int zzud3 = com_google_android_gms_internal_measurement_zzum.zzud();
            while (zzud2 < zzud) {
                if (bArr[zzud2] != bArr2[zzud3]) {
                    return false;
                }
                zzud2++;
                zzud3++;
            }
            return true;
        }
    }

    protected final int zza(int i, int i2, int i3) {
        return zzvo.zza(i, this.zzbug, zzud(), i3);
    }

    protected int zzud() {
        return 0;
    }
}
