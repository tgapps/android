package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class zzzd implements Cloneable {
    private Object value;
    private zzzb<?, ?> zzcfj;
    private List<zzzi> zzcfk = new ArrayList();

    zzzd() {
    }

    final void zza(zzzi com_google_android_gms_internal_measurement_zzzi) throws IOException {
        if (this.zzcfk != null) {
            this.zzcfk.add(com_google_android_gms_internal_measurement_zzzi);
            return;
        }
        Object zza;
        if (this.value instanceof zzzg) {
            byte[] bArr = com_google_android_gms_internal_measurement_zzzi.zzbug;
            zzyx zzj = zzyx.zzj(bArr, 0, bArr.length);
            int zzuy = zzj.zzuy();
            if (zzuy != bArr.length - zzyy.zzbc(zzuy)) {
                throw zzzf.zzyw();
            }
            zza = ((zzzg) this.value).zza(zzj);
        } else if (this.value instanceof zzzg[]) {
            zzzg[] com_google_android_gms_internal_measurement_zzzgArr = (zzzg[]) this.zzcfj.zzah(Collections.singletonList(com_google_android_gms_internal_measurement_zzzi));
            zzzg[] com_google_android_gms_internal_measurement_zzzgArr2 = (zzzg[]) this.value;
            zzzg[] com_google_android_gms_internal_measurement_zzzgArr3 = (zzzg[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzzgArr2, com_google_android_gms_internal_measurement_zzzgArr2.length + com_google_android_gms_internal_measurement_zzzgArr.length);
            System.arraycopy(com_google_android_gms_internal_measurement_zzzgArr, 0, com_google_android_gms_internal_measurement_zzzgArr3, com_google_android_gms_internal_measurement_zzzgArr2.length, com_google_android_gms_internal_measurement_zzzgArr.length);
        } else {
            zza = this.zzcfj.zzah(Collections.singletonList(com_google_android_gms_internal_measurement_zzzi));
        }
        this.zzcfj = this.zzcfj;
        this.value = zza;
        this.zzcfk = null;
    }

    final int zzf() {
        int i = 0;
        if (this.value != null) {
            zzzb com_google_android_gms_internal_measurement_zzzb = this.zzcfj;
            Object obj = this.value;
            if (!com_google_android_gms_internal_measurement_zzzb.zzcfe) {
                return com_google_android_gms_internal_measurement_zzzb.zzak(obj);
            }
            int length = Array.getLength(obj);
            int i2 = 0;
            for (int i3 = 0; i3 < length; i3++) {
                if (Array.get(obj, i3) != null) {
                    i2 += com_google_android_gms_internal_measurement_zzzb.zzak(Array.get(obj, i3));
                }
            }
            return i2;
        }
        for (zzzi com_google_android_gms_internal_measurement_zzzi : this.zzcfk) {
            i = (com_google_android_gms_internal_measurement_zzzi.zzbug.length + (zzyy.zzbj(com_google_android_gms_internal_measurement_zzzi.tag) + 0)) + i;
        }
        return i;
    }

    final void zza(zzyy com_google_android_gms_internal_measurement_zzyy) throws IOException {
        if (this.value != null) {
            zzzb com_google_android_gms_internal_measurement_zzzb = this.zzcfj;
            Object obj = this.value;
            if (com_google_android_gms_internal_measurement_zzzb.zzcfe) {
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    Object obj2 = Array.get(obj, i);
                    if (obj2 != null) {
                        com_google_android_gms_internal_measurement_zzzb.zza(obj2, com_google_android_gms_internal_measurement_zzyy);
                    }
                }
                return;
            }
            com_google_android_gms_internal_measurement_zzzb.zza(obj, com_google_android_gms_internal_measurement_zzyy);
            return;
        }
        for (zzzi com_google_android_gms_internal_measurement_zzzi : this.zzcfk) {
            com_google_android_gms_internal_measurement_zzyy.zzca(com_google_android_gms_internal_measurement_zzzi.tag);
            com_google_android_gms_internal_measurement_zzyy.zzp(com_google_android_gms_internal_measurement_zzzi.zzbug);
        }
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzzd)) {
            return false;
        }
        zzzd com_google_android_gms_internal_measurement_zzzd = (zzzd) obj;
        if (this.value == null || com_google_android_gms_internal_measurement_zzzd.value == null) {
            if (this.zzcfk != null && com_google_android_gms_internal_measurement_zzzd.zzcfk != null) {
                return this.zzcfk.equals(com_google_android_gms_internal_measurement_zzzd.zzcfk);
            }
            try {
                return Arrays.equals(toByteArray(), com_google_android_gms_internal_measurement_zzzd.toByteArray());
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        } else if (this.zzcfj != com_google_android_gms_internal_measurement_zzzd.zzcfj) {
            return false;
        } else {
            if (!this.zzcfj.zzcfd.isArray()) {
                return this.value.equals(com_google_android_gms_internal_measurement_zzzd.value);
            }
            if (this.value instanceof byte[]) {
                return Arrays.equals((byte[]) this.value, (byte[]) com_google_android_gms_internal_measurement_zzzd.value);
            }
            if (this.value instanceof int[]) {
                return Arrays.equals((int[]) this.value, (int[]) com_google_android_gms_internal_measurement_zzzd.value);
            }
            if (this.value instanceof long[]) {
                return Arrays.equals((long[]) this.value, (long[]) com_google_android_gms_internal_measurement_zzzd.value);
            }
            if (this.value instanceof float[]) {
                return Arrays.equals((float[]) this.value, (float[]) com_google_android_gms_internal_measurement_zzzd.value);
            }
            if (this.value instanceof double[]) {
                return Arrays.equals((double[]) this.value, (double[]) com_google_android_gms_internal_measurement_zzzd.value);
            }
            if (this.value instanceof boolean[]) {
                return Arrays.equals((boolean[]) this.value, (boolean[]) com_google_android_gms_internal_measurement_zzzd.value);
            }
            return Arrays.deepEquals((Object[]) this.value, (Object[]) com_google_android_gms_internal_measurement_zzzd.value);
        }
    }

    public final int hashCode() {
        try {
            return Arrays.hashCode(toByteArray()) + 527;
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    private final byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zzf()];
        zza(zzyy.zzo(bArr));
        return bArr;
    }

    private final zzzd zzyv() {
        zzzd com_google_android_gms_internal_measurement_zzzd = new zzzd();
        try {
            com_google_android_gms_internal_measurement_zzzd.zzcfj = this.zzcfj;
            if (this.zzcfk == null) {
                com_google_android_gms_internal_measurement_zzzd.zzcfk = null;
            } else {
                com_google_android_gms_internal_measurement_zzzd.zzcfk.addAll(this.zzcfk);
            }
            if (this.value != null) {
                if (this.value instanceof zzzg) {
                    com_google_android_gms_internal_measurement_zzzd.value = (zzzg) ((zzzg) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    com_google_android_gms_internal_measurement_zzzd.value = ((byte[]) this.value).clone();
                } else if (this.value instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.value;
                    r4 = new byte[bArr.length][];
                    com_google_android_gms_internal_measurement_zzzd.value = r4;
                    for (r2 = 0; r2 < bArr.length; r2++) {
                        r4[r2] = (byte[]) bArr[r2].clone();
                    }
                } else if (this.value instanceof boolean[]) {
                    com_google_android_gms_internal_measurement_zzzd.value = ((boolean[]) this.value).clone();
                } else if (this.value instanceof int[]) {
                    com_google_android_gms_internal_measurement_zzzd.value = ((int[]) this.value).clone();
                } else if (this.value instanceof long[]) {
                    com_google_android_gms_internal_measurement_zzzd.value = ((long[]) this.value).clone();
                } else if (this.value instanceof float[]) {
                    com_google_android_gms_internal_measurement_zzzd.value = ((float[]) this.value).clone();
                } else if (this.value instanceof double[]) {
                    com_google_android_gms_internal_measurement_zzzd.value = ((double[]) this.value).clone();
                } else if (this.value instanceof zzzg[]) {
                    zzzg[] com_google_android_gms_internal_measurement_zzzgArr = (zzzg[]) this.value;
                    r4 = new zzzg[com_google_android_gms_internal_measurement_zzzgArr.length];
                    com_google_android_gms_internal_measurement_zzzd.value = r4;
                    for (r2 = 0; r2 < com_google_android_gms_internal_measurement_zzzgArr.length; r2++) {
                        r4[r2] = (zzzg) com_google_android_gms_internal_measurement_zzzgArr[r2].clone();
                    }
                }
            }
            return com_google_android_gms_internal_measurement_zzzd;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzyv();
    }
}
