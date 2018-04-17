package com.google.android.gms.internal.firebase_abt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class zzg implements Cloneable {
    private Object value;
    private zze<?, ?> zzy;
    private List<zzl> zzz = new ArrayList();

    zzg() {
    }

    private final byte[] toByteArray() throws IOException {
        if (this.value != null) {
            throw new NoSuchMethodError();
        }
        int i = 0;
        for (zzl com_google_android_gms_internal_firebase_abt_zzl : this.zzz) {
            i += (zzb.zzf(com_google_android_gms_internal_firebase_abt_zzl.tag) + 0) + com_google_android_gms_internal_firebase_abt_zzl.zzac.length;
        }
        byte[] bArr = new byte[i];
        zzb zzb = zzb.zzb(bArr);
        if (this.value != null) {
            throw new NoSuchMethodError();
        }
        for (zzl com_google_android_gms_internal_firebase_abt_zzl2 : this.zzz) {
            zzb.zze(com_google_android_gms_internal_firebase_abt_zzl2.tag);
            zzb.zzc(com_google_android_gms_internal_firebase_abt_zzl2.zzac);
        }
        return bArr;
    }

    private final zzg zzk() {
        zzg com_google_android_gms_internal_firebase_abt_zzg = new zzg();
        try {
            com_google_android_gms_internal_firebase_abt_zzg.zzy = this.zzy;
            if (this.zzz == null) {
                com_google_android_gms_internal_firebase_abt_zzg.zzz = null;
            } else {
                com_google_android_gms_internal_firebase_abt_zzg.zzz.addAll(this.zzz);
            }
            if (this.value != null) {
                Object obj;
                if (this.value instanceof zzj) {
                    obj = (zzj) ((zzj) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    obj = ((byte[]) this.value).clone();
                } else {
                    int i = 0;
                    Object obj2;
                    if (this.value instanceof byte[][]) {
                        byte[][] bArr = (byte[][]) this.value;
                        obj2 = new byte[bArr.length][];
                        com_google_android_gms_internal_firebase_abt_zzg.value = obj2;
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
                    } else if (this.value instanceof zzj[]) {
                        zzj[] com_google_android_gms_internal_firebase_abt_zzjArr = (zzj[]) this.value;
                        obj2 = new zzj[com_google_android_gms_internal_firebase_abt_zzjArr.length];
                        com_google_android_gms_internal_firebase_abt_zzg.value = obj2;
                        while (i < com_google_android_gms_internal_firebase_abt_zzjArr.length) {
                            obj2[i] = (zzj) com_google_android_gms_internal_firebase_abt_zzjArr[i].clone();
                            i++;
                        }
                    }
                }
                com_google_android_gms_internal_firebase_abt_zzg.value = obj;
                return com_google_android_gms_internal_firebase_abt_zzg;
            }
            return com_google_android_gms_internal_firebase_abt_zzg;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzk();
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzg)) {
            return false;
        }
        zzg com_google_android_gms_internal_firebase_abt_zzg = (zzg) obj;
        if (this.value != null && com_google_android_gms_internal_firebase_abt_zzg.value != null) {
            return this.zzy != com_google_android_gms_internal_firebase_abt_zzg.zzy ? false : !this.zzy.zzt.isArray() ? this.value.equals(com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) com_google_android_gms_internal_firebase_abt_zzg.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) com_google_android_gms_internal_firebase_abt_zzg.value);
        } else {
            if (this.zzz != null && com_google_android_gms_internal_firebase_abt_zzg.zzz != null) {
                return this.zzz.equals(com_google_android_gms_internal_firebase_abt_zzg.zzz);
            }
            try {
                return Arrays.equals(toByteArray(), com_google_android_gms_internal_firebase_abt_zzg.toByteArray());
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

    final void zza(zzl com_google_android_gms_internal_firebase_abt_zzl) throws IOException {
        if (this.zzz != null) {
            this.zzz.add(com_google_android_gms_internal_firebase_abt_zzl);
        } else if (this.value instanceof zzj) {
            byte[] bArr = com_google_android_gms_internal_firebase_abt_zzl.zzac;
            zza zza = zza.zza(bArr, 0, bArr.length);
            int zzg = zza.zzg();
            if (zzg != bArr.length - (zzg >= 0 ? zzb.zzf(zzg) : 10)) {
                throw zzi.zzl();
            }
            zzj zza2 = ((zzj) this.value).zza(zza);
            this.zzy = this.zzy;
            this.value = zza2;
            this.zzz = null;
        } else if (this.value instanceof zzj[]) {
            Collections.singletonList(com_google_android_gms_internal_firebase_abt_zzl);
            throw new NoSuchMethodError();
        } else {
            Collections.singletonList(com_google_android_gms_internal_firebase_abt_zzl);
            throw new NoSuchMethodError();
        }
    }
}
