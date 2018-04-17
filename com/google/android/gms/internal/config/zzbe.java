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
                Object obj;
                if (this.value instanceof zzbh) {
                    obj = (zzbh) ((zzbh) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    obj = ((byte[]) this.value).clone();
                } else {
                    int i = 0;
                    Object obj2;
                    if (this.value instanceof byte[][]) {
                        byte[][] bArr = (byte[][]) this.value;
                        obj2 = new byte[bArr.length][];
                        com_google_android_gms_internal_config_zzbe.value = obj2;
                        while (i < bArr.length) {
                            obj2[i] = (byte[]) bArr[i].clone();
                            i++;
                        }
                    } else if (this.value instanceof boolean[]) {
                        obj = ((boolean[]) this.value).clone();
                    } else if (this.value instanceof int[]) {
                        obj = ((int[]) this.value).clone();
                    } else if (this.value instanceof long[]) {
                        obj = ((long[]) this.value).clone();
                    } else if (this.value instanceof float[]) {
                        obj = ((float[]) this.value).clone();
                    } else if (this.value instanceof double[]) {
                        obj = ((double[]) this.value).clone();
                    } else if (this.value instanceof zzbh[]) {
                        zzbh[] com_google_android_gms_internal_config_zzbhArr = (zzbh[]) this.value;
                        obj2 = new zzbh[com_google_android_gms_internal_config_zzbhArr.length];
                        com_google_android_gms_internal_config_zzbe.value = obj2;
                        while (i < com_google_android_gms_internal_config_zzbhArr.length) {
                            obj2[i] = (zzbh) com_google_android_gms_internal_config_zzbhArr[i].clone();
                            i++;
                        }
                    }
                }
                com_google_android_gms_internal_config_zzbe.value = obj;
                return com_google_android_gms_internal_config_zzbe;
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
            return this.zzco != com_google_android_gms_internal_config_zzbe.zzco ? false : !this.zzco.zzcj.isArray() ? this.value.equals(com_google_android_gms_internal_config_zzbe.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) com_google_android_gms_internal_config_zzbe.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) com_google_android_gms_internal_config_zzbe.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) com_google_android_gms_internal_config_zzbe.value);
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
            return 527 + Arrays.hashCode(toByteArray());
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
            i += (zzaz.zzn(com_google_android_gms_internal_config_zzbj.tag) + 0) + com_google_android_gms_internal_config_zzbj.zzcs.length;
        }
        return i;
    }
}
