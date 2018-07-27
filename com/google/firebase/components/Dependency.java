package com.google.firebase.components;

import com.google.android.gms.common.internal.Preconditions;

public final class Dependency {
    private final Class<?> zza;
    private final int zzb;
    private final int zzc;

    private Dependency(Class<?> anInterface, int type, int injection) {
        this.zza = (Class) Preconditions.checkNotNull(anInterface, "Null dependency anInterface.");
        this.zzb = type;
        this.zzc = injection;
    }

    public static Dependency required(Class<?> anInterface) {
        return new Dependency(anInterface, 1, 0);
    }

    public final Class<?> zza() {
        return this.zza;
    }

    public final boolean zzb() {
        return this.zzb == 1;
    }

    public final boolean zzc() {
        return this.zzc == 0;
    }

    public final boolean equals(Object o) {
        if (!(o instanceof Dependency)) {
            return false;
        }
        Dependency dependency = (Dependency) o;
        if (this.zza == dependency.zza && this.zzb == dependency.zzb && this.zzc == dependency.zzc) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return ((((this.zza.hashCode() ^ 1000003) * 1000003) ^ this.zzb) * 1000003) ^ this.zzc;
    }

    public final String toString() {
        boolean z = true;
        StringBuilder append = new StringBuilder("Dependency{anInterface=").append(this.zza).append(", required=").append(this.zzb == 1).append(", direct=");
        if (this.zzc != 0) {
            z = false;
        }
        return append.append(z).append("}").toString();
    }
}
