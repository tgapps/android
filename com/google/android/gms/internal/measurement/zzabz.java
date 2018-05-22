package com.google.android.gms.internal.measurement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public final class zzabz<M extends zzaby<M>, T> {
    public final int tag;
    private final int type;
    protected final Class<T> zzbwx;
    protected final boolean zzbwy;

    private final Object zzf(zzabv com_google_android_gms_internal_measurement_zzabv) {
        String valueOf;
        Class componentType = this.zzbwy ? this.zzbwx.getComponentType() : this.zzbwx;
        try {
            zzace com_google_android_gms_internal_measurement_zzace;
            switch (this.type) {
                case 10:
                    com_google_android_gms_internal_measurement_zzace = (zzace) componentType.newInstance();
                    com_google_android_gms_internal_measurement_zzabv.zza(com_google_android_gms_internal_measurement_zzace, this.tag >>> 3);
                    return com_google_android_gms_internal_measurement_zzace;
                case 11:
                    com_google_android_gms_internal_measurement_zzace = (zzace) componentType.newInstance();
                    com_google_android_gms_internal_measurement_zzabv.zza(com_google_android_gms_internal_measurement_zzace);
                    return com_google_android_gms_internal_measurement_zzace;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (Throwable e) {
            valueOf = String.valueOf(componentType);
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf).length() + 33).append("Error creating instance of class ").append(valueOf).toString(), e);
        } catch (Throwable e2) {
            valueOf = String.valueOf(componentType);
            throw new IllegalArgumentException(new StringBuilder(String.valueOf(valueOf).length() + 33).append("Error creating instance of class ").append(valueOf).toString(), e2);
        } catch (Throwable e22) {
            throw new IllegalArgumentException("Error reading extension field", e22);
        }
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof zzabz)) {
            return false;
        }
        zzabz com_google_android_gms_internal_measurement_zzabz = (zzabz) obj;
        return this.type == com_google_android_gms_internal_measurement_zzabz.type && this.zzbwx == com_google_android_gms_internal_measurement_zzabz.zzbwx && this.tag == com_google_android_gms_internal_measurement_zzabz.tag && this.zzbwy == com_google_android_gms_internal_measurement_zzabz.zzbwy;
    }

    public final int hashCode() {
        return (this.zzbwy ? 1 : 0) + ((((((this.type + 1147) * 31) + this.zzbwx.hashCode()) * 31) + this.tag) * 31);
    }

    protected final void zza(Object obj, zzabw com_google_android_gms_internal_measurement_zzabw) {
        try {
            com_google_android_gms_internal_measurement_zzabw.zzar(this.tag);
            switch (this.type) {
                case 10:
                    int i = this.tag >>> 3;
                    ((zzace) obj).zza(com_google_android_gms_internal_measurement_zzabw);
                    com_google_android_gms_internal_measurement_zzabw.zzg(i, 4);
                    return;
                case 11:
                    com_google_android_gms_internal_measurement_zzabw.zzb((zzace) obj);
                    return;
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException(e);
    }

    final T zzi(List<zzacg> list) {
        int i = 0;
        if (list == null) {
            return null;
        }
        if (this.zzbwy) {
            int i2;
            List arrayList = new ArrayList();
            for (i2 = 0; i2 < list.size(); i2++) {
                zzacg com_google_android_gms_internal_measurement_zzacg = (zzacg) list.get(i2);
                if (com_google_android_gms_internal_measurement_zzacg.zzbrc.length != 0) {
                    arrayList.add(zzf(zzabv.zzi(com_google_android_gms_internal_measurement_zzacg.zzbrc)));
                }
            }
            i2 = arrayList.size();
            if (i2 == 0) {
                return null;
            }
            T cast = this.zzbwx.cast(Array.newInstance(this.zzbwx.getComponentType(), i2));
            while (i < i2) {
                Array.set(cast, i, arrayList.get(i));
                i++;
            }
            return cast;
        } else if (list.isEmpty()) {
            return null;
        } else {
            return this.zzbwx.cast(zzf(zzabv.zzi(((zzacg) list.get(list.size() - 1)).zzbrc)));
        }
    }

    protected final int zzv(Object obj) {
        int i = this.tag >>> 3;
        switch (this.type) {
            case 10:
                return (zzabw.zzaq(i) << 1) + ((zzace) obj).zzvm();
            case 11:
                return zzabw.zzb(i, (zzace) obj);
            default:
                throw new IllegalArgumentException("Unknown type " + this.type);
        }
    }
}
