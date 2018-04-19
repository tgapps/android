package com.google.android.gms.internal.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class zzbe implements Cloneable {
    private Object value;
    private zzbc<?, ?> zzco;
    private List<zzbj> zzcp = new ArrayList();

    zzbe() {
    }

    private final byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zzu()];
        zza(zzaz.zza(bArr));
        return bArr;
    }

    private final zzbe zzaf() {
        zzbe com_google_android_gms_internal_config_zzbe = new zzbe();
        try {
            com_google_android_gms_internal_config_zzbe.zzco = this.zzco;
            if (this.zzcp == null) {
                com_google_android_gms_internal_config_zzbe.zzcp = null;
            } else {
                com_google_android_gms_internal_config_zzbe.zzcp.addAll(this.zzcp);
            }
            if (this.value != null) {
                if (this.value instanceof zzbh) {
                    com_google_android_gms_internal_config_zzbe.value = (zzbh) ((zzbh) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    com_google_android_gms_internal_config_zzbe.value = ((byte[]) this.value).clone();
                } else if (this.value instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.value;
                    r4 = new byte[bArr.length][];
                    com_google_android_gms_internal_config_zzbe.value = r4;
                    for (r2 = 0; r2 < bArr.length; r2++) {
                        r4[r2] = (byte[]) bArr[r2].clone();
                    }
                } else if (this.value instanceof boolean[]) {
                    com_google_android_gms_internal_config_zzbe.value = ((boolean[]) this.value).clone();
                } else if (this.value instanceof int[]) {
                    com_google_android_gms_internal_config_zzbe.value = ((int[]) this.value).clone();
                } else if (this.value instanceof long[]) {
                    com_google_android_gms_internal_config_zzbe.value = ((long[]) this.value).clone();
                } else if (this.value instanceof float[]) {
                    com_google_android_gms_internal_config_zzbe.value = ((float[]) this.value).clone();
                } else if (this.value instanceof double[]) {
                    com_google_android_gms_internal_config_zzbe.value = ((double[]) this.value).clone();
                } else if (this.value instanceof zzbh[]) {
                    zzbh[] com_google_android_gms_internal_config_zzbhArr = (zzbh[]) this.value;
                    r4 = new zzbh[com_google_android_gms_internal_config_zzbhArr.length];
                    com_google_android_gms_internal_config_zzbe.value = r4;
                    for (r2 = 0; r2 < com_google_android_gms_internal_config_zzbhArr.length; r2++) {
                        r4[r2] = (zzbh) com_google_android_gms_internal_config_zzbhArr[r2].clone();
                    }
                }
            }
            return com_google_android_gms_internal_config_zzbe;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzaf();
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzbe)) {
            return false;
        }
        zzbe com_google_android_gms_internal_config_zzbe = (zzbe) obj;
        if (this.value != null && com_google_android_gms_internal_config_zzbe.value != null) {
            return this.zzco == com_google_android_gms_internal_config_zzbe.zzco ? !this.zzco.zzcj.isArray() ? this.value.equals(com_google_android_gms_internal_config_zzbe.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) com_google_android_gms_internal_config_zzbe.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) com_google_android_gms_internal_config_zzbe.value) : false;
        } else {
            if (this.zzcp != null && com_google_android_gms_internal_config_zzbe.zzcp != null) {
                return this.zzcp.equals(com_google_android_gms_internal_config_zzbe.zzcp);
            }
            try {
                return Arrays.equals(toByteArray(), com_google_android_gms_internal_config_zzbe.toByteArray());
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public final int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    final void zza(zzaz com_google_android_gms_internal_config_zzaz) throws IOException {
        if (this.value != null) {
            throw new NoSuchMethodError();
        }
        for (zzbj com_google_android_gms_internal_config_zzbj : this.zzcp) {
            com_google_android_gms_internal_config_zzaz.zzm(com_google_android_gms_internal_config_zzbj.tag);
            com_google_android_gms_internal_config_zzaz.zzc(com_google_android_gms_internal_config_zzbj.zzcs);
        }
    }

    final void zza(zzbj com_google_android_gms_internal_config_zzbj) throws IOException {
        if (this.zzcp != null) {
            this.zzcp.add(com_google_android_gms_internal_config_zzbj);
        } else if (this.value instanceof zzbh) {
            byte[] bArr = com_google_android_gms_internal_config_zzbj.zzcs;
            zzay zza = zzay.zza(bArr, 0, bArr.length);
            int zzz = zza.zzz();
            if (zzz != bArr.length - zzaz.zzj(zzz)) {
                throw zzbg.zzag();
            }
            zzbh zza2 = ((zzbh) this.value).zza(zza);
            this.zzco = this.zzco;
            this.value = zza2;
            this.zzcp = null;
        } else if (this.value instanceof zzbh[]) {
            Collections.singletonList(com_google_android_gms_internal_config_zzbj);
            throw new NoSuchMethodError();
        } else {
            Collections.singletonList(com_google_android_gms_internal_config_zzbj);
            throw new NoSuchMethodError();
        }
    }

    final int zzu() {
        if (this.value != null) {
            throw new NoSuchMethodError();
        }
        int i = 0;
        for (zzbj com_google_android_gms_internal_config_zzbj : this.zzcp) {
            i = (com_google_android_gms_internal_config_zzbj.zzcs.length + (zzaz.zzn(com_google_android_gms_internal_config_zzbj.tag) + 0)) + i;
        }
        return i;
    }
}
