package com.google.android.gms.internal.measurement;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class zzacb implements Cloneable {
    private Object value;
    private zzabz<?, ?> zzbxe;
    private List<zzacg> zzbxf = new ArrayList();

    zzacb() {
    }

    private final byte[] toByteArray() throws IOException {
        byte[] bArr = new byte[zza()];
        zza(zzabw.zzj(bArr));
        return bArr;
    }

    private final zzacb zzvg() {
        zzacb com_google_android_gms_internal_measurement_zzacb = new zzacb();
        try {
            com_google_android_gms_internal_measurement_zzacb.zzbxe = this.zzbxe;
            if (this.zzbxf == null) {
                com_google_android_gms_internal_measurement_zzacb.zzbxf = null;
            } else {
                com_google_android_gms_internal_measurement_zzacb.zzbxf.addAll(this.zzbxf);
            }
            if (this.value != null) {
                if (this.value instanceof zzace) {
                    com_google_android_gms_internal_measurement_zzacb.value = (zzace) ((zzace) this.value).clone();
                } else if (this.value instanceof byte[]) {
                    com_google_android_gms_internal_measurement_zzacb.value = ((byte[]) this.value).clone();
                } else if (this.value instanceof byte[][]) {
                    byte[][] bArr = (byte[][]) this.value;
                    r4 = new byte[bArr.length][];
                    com_google_android_gms_internal_measurement_zzacb.value = r4;
                    for (r2 = 0; r2 < bArr.length; r2++) {
                        r4[r2] = (byte[]) bArr[r2].clone();
                    }
                } else if (this.value instanceof boolean[]) {
                    com_google_android_gms_internal_measurement_zzacb.value = ((boolean[]) this.value).clone();
                } else if (this.value instanceof int[]) {
                    com_google_android_gms_internal_measurement_zzacb.value = ((int[]) this.value).clone();
                } else if (this.value instanceof long[]) {
                    com_google_android_gms_internal_measurement_zzacb.value = ((long[]) this.value).clone();
                } else if (this.value instanceof float[]) {
                    com_google_android_gms_internal_measurement_zzacb.value = ((float[]) this.value).clone();
                } else if (this.value instanceof double[]) {
                    com_google_android_gms_internal_measurement_zzacb.value = ((double[]) this.value).clone();
                } else if (this.value instanceof zzace[]) {
                    zzace[] com_google_android_gms_internal_measurement_zzaceArr = (zzace[]) this.value;
                    r4 = new zzace[com_google_android_gms_internal_measurement_zzaceArr.length];
                    com_google_android_gms_internal_measurement_zzacb.value = r4;
                    for (r2 = 0; r2 < com_google_android_gms_internal_measurement_zzaceArr.length; r2++) {
                        r4[r2] = (zzace) com_google_android_gms_internal_measurement_zzaceArr[r2].clone();
                    }
                }
            }
            return com_google_android_gms_internal_measurement_zzacb;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public final /* synthetic */ Object clone() throws CloneNotSupportedException {
        return zzvg();
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzacb)) {
            return false;
        }
        zzacb com_google_android_gms_internal_measurement_zzacb = (zzacb) obj;
        if (this.value != null && com_google_android_gms_internal_measurement_zzacb.value != null) {
            return this.zzbxe == com_google_android_gms_internal_measurement_zzacb.zzbxe ? !this.zzbxe.zzbwx.isArray() ? this.value.equals(com_google_android_gms_internal_measurement_zzacb.value) : this.value instanceof byte[] ? Arrays.equals((byte[]) this.value, (byte[]) com_google_android_gms_internal_measurement_zzacb.value) : this.value instanceof int[] ? Arrays.equals((int[]) this.value, (int[]) com_google_android_gms_internal_measurement_zzacb.value) : this.value instanceof long[] ? Arrays.equals((long[]) this.value, (long[]) com_google_android_gms_internal_measurement_zzacb.value) : this.value instanceof float[] ? Arrays.equals((float[]) this.value, (float[]) com_google_android_gms_internal_measurement_zzacb.value) : this.value instanceof double[] ? Arrays.equals((double[]) this.value, (double[]) com_google_android_gms_internal_measurement_zzacb.value) : this.value instanceof boolean[] ? Arrays.equals((boolean[]) this.value, (boolean[]) com_google_android_gms_internal_measurement_zzacb.value) : Arrays.deepEquals((Object[]) this.value, (Object[]) com_google_android_gms_internal_measurement_zzacb.value) : false;
        } else {
            if (this.zzbxf != null && com_google_android_gms_internal_measurement_zzacb.zzbxf != null) {
                return this.zzbxf.equals(com_google_android_gms_internal_measurement_zzacb.zzbxf);
            }
            try {
                return Arrays.equals(toByteArray(), com_google_android_gms_internal_measurement_zzacb.toByteArray());
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
            zzabz com_google_android_gms_internal_measurement_zzabz = this.zzbxe;
            Object obj = this.value;
            if (!com_google_android_gms_internal_measurement_zzabz.zzbwy) {
                return com_google_android_gms_internal_measurement_zzabz.zzv(obj);
            }
            int length = Array.getLength(obj);
            int i2 = 0;
            for (int i3 = 0; i3 < length; i3++) {
                if (Array.get(obj, i3) != null) {
                    i2 += com_google_android_gms_internal_measurement_zzabz.zzv(Array.get(obj, i3));
                }
            }
            return i2;
        }
        for (zzacg com_google_android_gms_internal_measurement_zzacg : this.zzbxf) {
            i = (com_google_android_gms_internal_measurement_zzacg.zzbrc.length + (zzabw.zzas(com_google_android_gms_internal_measurement_zzacg.tag) + 0)) + i;
        }
        return i;
    }

    final void zza(zzabw com_google_android_gms_internal_measurement_zzabw) throws IOException {
        if (this.value != null) {
            zzabz com_google_android_gms_internal_measurement_zzabz = this.zzbxe;
            Object obj = this.value;
            if (com_google_android_gms_internal_measurement_zzabz.zzbwy) {
                int length = Array.getLength(obj);
                for (int i = 0; i < length; i++) {
                    Object obj2 = Array.get(obj, i);
                    if (obj2 != null) {
                        com_google_android_gms_internal_measurement_zzabz.zza(obj2, com_google_android_gms_internal_measurement_zzabw);
                    }
                }
                return;
            }
            com_google_android_gms_internal_measurement_zzabz.zza(obj, com_google_android_gms_internal_measurement_zzabw);
            return;
        }
        for (zzacg com_google_android_gms_internal_measurement_zzacg : this.zzbxf) {
            com_google_android_gms_internal_measurement_zzabw.zzar(com_google_android_gms_internal_measurement_zzacg.tag);
            com_google_android_gms_internal_measurement_zzabw.zzk(com_google_android_gms_internal_measurement_zzacg.zzbrc);
        }
    }

    final void zza(zzacg com_google_android_gms_internal_measurement_zzacg) throws IOException {
        if (this.zzbxf != null) {
            this.zzbxf.add(com_google_android_gms_internal_measurement_zzacg);
            return;
        }
        Object zzb;
        if (this.value instanceof zzace) {
            byte[] bArr = com_google_android_gms_internal_measurement_zzacg.zzbrc;
            zzabv zza = zzabv.zza(bArr, 0, bArr.length);
            int zzuy = zza.zzuy();
            if (zzuy != bArr.length - zzabw.zzao(zzuy)) {
                throw zzacd.zzvh();
            }
            zzb = ((zzace) this.value).zzb(zza);
        } else if (this.value instanceof zzace[]) {
            zzace[] com_google_android_gms_internal_measurement_zzaceArr = (zzace[]) this.zzbxe.zzi(Collections.singletonList(com_google_android_gms_internal_measurement_zzacg));
            zzace[] com_google_android_gms_internal_measurement_zzaceArr2 = (zzace[]) this.value;
            zzace[] com_google_android_gms_internal_measurement_zzaceArr3 = (zzace[]) Arrays.copyOf(com_google_android_gms_internal_measurement_zzaceArr2, com_google_android_gms_internal_measurement_zzaceArr2.length + com_google_android_gms_internal_measurement_zzaceArr.length);
            System.arraycopy(com_google_android_gms_internal_measurement_zzaceArr, 0, com_google_android_gms_internal_measurement_zzaceArr3, com_google_android_gms_internal_measurement_zzaceArr2.length, com_google_android_gms_internal_measurement_zzaceArr.length);
        } else {
            zzb = this.zzbxe.zzi(Collections.singletonList(com_google_android_gms_internal_measurement_zzacg));
        }
        this.zzbxe = this.zzbxe;
        this.value = zzb;
        this.zzbxf = null;
    }
}
