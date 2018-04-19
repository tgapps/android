package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class zzabg implements Cloneable {
    private Object value;
    private zzabe<?, ?> zzbzp;
    private List<zzabl> zzbzq = new ArrayList();

    zzabg() {
    }

    private final byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zza()];
        zza(zzabb.zzk(bArr));
        return bArr;
    }

    private final zzabg zzwa() {
        zzabg com_google_android_gms_internal_measurement_zzabg = new zzabg();
        try {
            com_google_android_gms_internal_measurement_zzabg.zzbzp = this.zzbzp;
            if (this.zzbzq == null) {
                com_google_android_gms_internal_measurement_zzabg.zzbzq = null;
            } else {
                com_google_android_gms_internal_measurement_zzabg.zzbzq.addAll(this.zzbzq);
            }
            if (this.value != null) {
                if (this.value instanceof zzabj) {
                    com_google_android_gms_internal_measurement_zzabg.value = (zzabj) ((zzabj) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    com_google_android_gms_internal_measurement_zzabg.value = ((byte[]) this.value).clone();
                } else if (this.value instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.value;
                    r4 = new byte[bArr.length][];
                    com_google_android_gms_internal_measurement_zzabg.value = r4;
                    for (r2 = 0; r2 < bArr.length; r2++) {
                        r4[r2] = (byte[]) bArr[r2].clone();
                    }
                } else if (this.value instanceof boolean[]) {
                    com_google_android_gms_internal_measurement_zzabg.value = ((boolean[]) this.value).clone();
                } else if (this.value instanceof int[]) {
                    com_google_android_gms_internal_measurement_zzabg.value = ((int[]) this.value).clone();
                } else if (this.value instanceof long[]) {
                    com_google_android_gms_internal_measurement_zzabg.value = ((long[]) this.value).clone();
                } else if (this.value instanceof float[]) {
                    com_google_android_gms_internal_measurement_zzabg.value = ((float[]) this.value).clone();
                } else if (this.value instanceof double[]) {
                    com_google_android_gms_internal_measurement_zzabg.value = ((double[]) this.value).clone();
                } else if (this.value instanceof zzabj[]) {
                    zzabj[] com_google_android_gms_internal_measurement_zzabjArr = (zzabj[]) this.value;
                    r4 = new zzabj[com_google_android_gms_internal_measurement_zzabjArr.length];
                    com_google_android_gms_internal_measurement_zzabg.value = r4;
                    for (r2 = 0; r2 < com_google_android_gms_internal_measurement_zzabjArr.length; r2++) {
                        r4[r2] = (zzabj) com_google_android_gms_internal_measurement_zzabjArr[r2].clone();
                    }
                }
            }
            return com_google_android_gms_internal_measurement_zzabg;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzwa();
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzabg)) {
            return false;
        }
        zzabg com_google_android_gms_internal_measurement_zzabg = (zzabg) obj;
        if (this.value != null && com_google_android_gms_internal_measurement_zzabg.value != null) {
            return this.zzbzp == com_google_android_gms_internal_measurement_zzabg.zzbzp ? !this.zzbzp.zzbzi.isArray() ? this.value.equals(com_google_android_gms_internal_measurement_zzabg.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) com_google_android_gms_internal_measurement_zzabg.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) com_google_android_gms_internal_measurement_zzabg.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) com_google_android_gms_internal_measurement_zzabg.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) com_google_android_gms_internal_measurement_zzabg.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) com_google_android_gms_internal_measurement_zzabg.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) com_google_android_gms_internal_measurement_zzabg.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) com_google_android_gms_internal_measurement_zzabg.value) : false;
        } else {
            if (this.zzbzq != null && com_google_android_gms_internal_measurement_zzabg.zzbzq != null) {
                return this.zzbzq.equals(com_google_android_gms_internal_measurement_zzabg.zzbzq);
            }
            try {
                return Arrays.equals(toByteArray(), com_google_android_gms_internal_measurement_zzabg.toByteArray());
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

    final int zza() {
        int i = 0;
        if (this.value != null) {
            zzabe com_google_android_gms_internal_measurement_zzabe = this.zzbzp;
            Object obj = this.value;
            if (!com_google_android_gms_internal_measurement_zzabe.zzbzj) {
                return com_google_android_gms_internal_measurement_zzabe.zzx(obj);
            }
            int length = Array.getLength(obj);
            int i2 = 0;
            for (int i3 = 0; i3 < length; i3++) {
                if (Array.get(obj, i3) != null) {
                    i2 += com_google_android_gms_internal_measurement_zzabe.zzx(Array.get(obj, i3));
                }
            }
            return i2;
        }
        for (zzabl com_google_android_gms_internal_measurement_zzabl : this.zzbzq) {
            i = (com_google_android_gms_internal_measurement_zzabl.zzbto.length + (zzabb.zzau(com_google_android_gms_internal_measurement_zzabl.tag) + 0)) + i;
        }
        return i;
    }

    final void zza(zzabb com_google_android_gms_internal_measurement_zzabb) throws IOException {
        if (this.value != null) {
            zzabe com_google_android_gms_internal_measurement_zzabe = this.zzbzp;
            Object obj = this.value;
            if (com_google_android_gms_internal_measurement_zzabe.zzbzj) {
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    Object obj2 = Array.get(obj, i);
                    if (obj2 != null) {
                        com_google_android_gms_internal_measurement_zzabe.zza(obj2, com_google_android_gms_internal_measurement_zzabb);
                    }
                }
                return;
            }
            com_google_android_gms_internal_measurement_zzabe.zza(obj, com_google_android_gms_internal_measurement_zzabb);
            return;
        }
        for (zzabl com_google_android_gms_internal_measurement_zzabl : this.zzbzq) {
            com_google_android_gms_internal_measurement_zzabb.zzat(com_google_android_gms_internal_measurement_zzabl.tag);
            com_google_android_gms_internal_measurement_zzabb.zzl(com_google_android_gms_internal_measurement_zzabl.zzbto);
        }
    }

    final void zza(zzabl com_google_android_gms_internal_measurement_zzabl) throws IOException {
        if (this.zzbzq != null) {
            this.zzbzq.add(com_google_android_gms_internal_measurement_zzabl);
            return;
        }
        Object zzb;
        if (this.value instanceof zzabj) {
            byte[] bArr = com_google_android_gms_internal_measurement_zzabl.zzbto;
            zzaba zza = zzaba.zza(bArr, 0, bArr.length);
            int zzvs = zza.zzvs();
            if (zzvs != bArr.length - zzabb.zzaq(zzvs)) {
                throw zzabi.zzwb();
            }
            zzb = ((zzabj) this.value).zzb(zza);
        } else if (this.value instanceof zzabj[]) {
            zzabj[] com_google_android_gms_internal_measurement_zzabjArr = (zzabj[]) this.zzbzp.zzi(Collections.singletonList(com_google_android_gms_internal_measurement_zzabl));
            zzabj[] com_google_android_gms_internal_measurement_zzabjArr2 = (zzabj[]) this.value;
            zzabj[] com_google_android_gms_internal_measurement_zzabjArr3 = (zzabj[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzabjArr2, com_google_android_gms_internal_measurement_zzabjArr2.length + com_google_android_gms_internal_measurement_zzabjArr.length);
            System.arraycopy(com_google_android_gms_internal_measurement_zzabjArr, 0, com_google_android_gms_internal_measurement_zzabjArr3, com_google_android_gms_internal_measurement_zzabjArr2.length, com_google_android_gms_internal_measurement_zzabjArr.length);
        } else {
            zzb = this.zzbzp.zzi(Collections.singletonList(com_google_android_gms_internal_measurement_zzabl));
        }
        this.zzbzp = this.zzbzp;
        this.value = zzb;
        this.zzbzq = null;
    }
}
