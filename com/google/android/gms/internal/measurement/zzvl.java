package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.zzvm.zze;

final class zzvl implements zzws {
    private static final zzvl zzbyl = new zzvl();

    private zzvl() {
    }

    public static zzvl zzwb() {
        return zzbyl;
    }

    public final boolean zze(Class<?> cls) {
        return zzvm.class.isAssignableFrom(cls);
    }

    public final zzwr zzf(Class<?> cls) {
        if (zzvm.class.isAssignableFrom(cls)) {
            try {
                return (zzwr) zzvm.zzg(cls.asSubclass(zzvm.class)).zza(zze.zzbyv, null, null);
            } catch (Throwable e) {
                Throwable th = e;
                String str = "Unable to get message info for ";
                String valueOf = String.valueOf(cls.getName());
                throw new RuntimeException(valueOf.length() != 0 ? str.concat(valueOf) : new String(str), th);
            }
        }
        String str2 = "Unsupported message type: ";
        valueOf = String.valueOf(cls.getName());
        throw new IllegalArgumentException(valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
    }
}
