package com.google.android.gms.internal.measurement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public final class zzabe<M extends zzabd<M>, T> {
    public final int tag;
    private final int type;
    protected final Class<T> zzbzi;
    protected final boolean zzbzj;

    private final Object zzf(zzaba com_google_android_gms_internal_measurement_zzaba) {
        String valueOf;
        StringBuilder stringBuilder;
        Class componentType = this.zzbzj ? this.zzbzi.getComponentType() : this.zzbzi;
        try {
            zzabj com_google_android_gms_internal_measurement_zzabj;
            switch (this.type) {
                case 10:
                    com_google_android_gms_internal_measurement_zzabj = (zzabj) componentType.newInstance();
                    com_google_android_gms_internal_measurement_zzaba.zza(com_google_android_gms_internal_measurement_zzabj, this.tag >>> 3);
                    return com_google_android_gms_internal_measurement_zzabj;
                case 11:
                    com_google_android_gms_internal_measurement_zzabj = (zzabj) componentType.newInstance();
                    com_google_android_gms_internal_measurement_zzaba.zza(com_google_android_gms_internal_measurement_zzabj);
                    return com_google_android_gms_internal_measurement_zzabj;
                default:
                    int i = this.type;
                    StringBuilder stringBuilder2 = new StringBuilder(24);
                    stringBuilder2.append("Unknown type ");
                    stringBuilder2.append(i);
                    throw new IllegalArgumentException(stringBuilder2.toString());
            }
        } catch (Throwable e) {
            valueOf = String.valueOf(componentType);
            stringBuilder = new StringBuilder(33 + String.valueOf(valueOf).length());
            stringBuilder.append("Error creating instance of class ");
            stringBuilder.append(valueOf);
            throw new IllegalArgumentException(stringBuilder.toString(), e);
        } catch (Throwable e2) {
            valueOf = String.valueOf(componentType);
            stringBuilder = new StringBuilder(33 + String.valueOf(valueOf).length());
            stringBuilder.append("Error creating instance of class ");
            stringBuilder.append(valueOf);
            throw new IllegalArgumentException(stringBuilder.toString(), e2);
        } catch (Throwable e22) {
            throw new IllegalArgumentException("Error reading extension field", e22);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzabe)) {
            return false;
        }
        zzabe com_google_android_gms_internal_measurement_zzabe = (zzabe) obj;
        return this.type == com_google_android_gms_internal_measurement_zzabe.type && this.zzbzi == com_google_android_gms_internal_measurement_zzabe.zzbzi && this.tag == com_google_android_gms_internal_measurement_zzabe.tag && this.zzbzj == com_google_android_gms_internal_measurement_zzabe.zzbzj;
    }

    public final int hashCode() {
        return ((((((1147 + this.type) * 31) + this.zzbzi.hashCode()) * 31) + this.tag) * 31) + this.zzbzj;
    }

    protected final void zza(Object obj, zzabb com_google_android_gms_internal_measurement_zzabb) {
        try {
            com_google_android_gms_internal_measurement_zzabb.zzat(this.tag);
            switch (this.type) {
                case 10:
                    int i = this.tag >>> 3;
                    ((zzabj) obj).zza(com_google_android_gms_internal_measurement_zzabb);
                    com_google_android_gms_internal_measurement_zzabb.zzg(i, 4);
                    return;
                case 11:
                    com_google_android_gms_internal_measurement_zzabb.zzb((zzabj) obj);
                    return;
                default:
                    int i2 = this.type;
                    StringBuilder stringBuilder = new StringBuilder(24);
                    stringBuilder.append("Unknown type ");
                    stringBuilder.append(i2);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    final T zzi(List<zzabl> list) {
        if (list == null) {
            return null;
        }
        if (this.zzbzj) {
            List arrayList = new ArrayList();
            int i = 0;
            for (int i2 = 0; i2 < list.size(); i2++) {
                zzabl com_google_android_gms_internal_measurement_zzabl = (zzabl) list.get(i2);
                if (com_google_android_gms_internal_measurement_zzabl.zzbto.length != 0) {
                    arrayList.add(zzf(zzaba.zzj(com_google_android_gms_internal_measurement_zzabl.zzbto)));
                }
            }
            int size = arrayList.size();
            if (size == 0) {
                return null;
            }
            T cast = this.zzbzi.cast(Array.newInstance(this.zzbzi.getComponentType(), size));
            while (i < size) {
                Array.set(cast, i, arrayList.get(i));
                i++;
            }
            return cast;
        } else if (list.isEmpty()) {
            return null;
        } else {
            return this.zzbzi.cast(zzf(zzaba.zzj(((zzabl) list.get(list.size() - 1)).zzbto)));
        }
    }

    protected final int zzx(Object obj) {
        int i = this.tag >>> 3;
        switch (this.type) {
            case 10:
                return (zzabb.zzas(i) << 1) + ((zzabj) obj).zzwg();
            case 11:
                return zzabb.zzb(i, (zzabj) obj);
            default:
                i = this.type;
                StringBuilder stringBuilder = new StringBuilder(24);
                stringBuilder.append("Unknown type ");
                stringBuilder.append(i);
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }
}
