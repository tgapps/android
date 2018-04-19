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
            i = (com_google_android_gms_internal_firebase_abt_zzl.zzac.length + (zzb.zzf(com_google_android_gms_internal_firebase_abt_zzl.tag) + 0)) + i;
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
                if (this.value instanceof zzj) {
                    com_google_android_gms_internal_firebase_abt_zzg.value = (zzj) ((zzj) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    com_google_android_gms_internal_firebase_abt_zzg.value = ((byte[]) this.value).clone();
                } else if (this.value instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.value;
                    r4 = new byte[bArr.length][];
                    com_google_android_gms_internal_firebase_abt_zzg.value = r4;
                    for (r2 = 0; r2 < bArr.length; r2++) {
                        r4[r2] = (byte[]) bArr[r2].clone();
                    }
                } else if (this.value instanceof boolean[]) {
                    com_google_android_gms_internal_firebase_abt_zzg.value = ((boolean[]) this.value).clone();
                } else if (this.value instanceof int[]) {
                    com_google_android_gms_internal_firebase_abt_zzg.value = ((int[]) this.value).clone();
                } else if (this.value instanceof long[]) {
                    com_google_android_gms_internal_firebase_abt_zzg.value = ((long[]) this.value).clone();
                } else if (this.value instanceof float[]) {
                    com_google_android_gms_internal_firebase_abt_zzg.value = ((float[]) this.value).clone();
                } else if (this.value instanceof double[]) {
                    com_google_android_gms_internal_firebase_abt_zzg.value = ((double[]) this.value).clone();
                } else if (this.value instanceof zzj[]) {
                    zzj[] com_google_android_gms_internal_firebase_abt_zzjArr = (zzj[]) this.value;
                    r4 = new zzj[com_google_android_gms_internal_firebase_abt_zzjArr.length];
                    com_google_android_gms_internal_firebase_abt_zzg.value = r4;
                    for (r2 = 0; r2 < com_google_android_gms_internal_firebase_abt_zzjArr.length; r2++) {
                        r4[r2] = (zzj) com_google_android_gms_internal_firebase_abt_zzjArr[r2].clone();
                    }
                }
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
            return this.zzy == com_google_android_gms_internal_firebase_abt_zzg.zzy ? !this.zzy.zzt.isArray() ? this.value.equals(com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) com_google_android_gms_internal_firebase_abt_zzg.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) com_google_android_gms_internal_firebase_abt_zzg.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) com_google_android_gms_internal_firebase_abt_zzg.value) : false;
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
            return Arrays.hashCode(toByteArray()) + 527;
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
