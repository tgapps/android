package com.google.firebase.components;

public final class Dependency {
    private final Class<?> zzam;
    private final int zzan;
    private final int zzao;

    public final boolean equals(Object obj) {
        if (!(obj instanceof Dependency)) {
            return false;
        }
        Dependency dependency = (Dependency) obj;
        return this.zzam == dependency.zzam && this.zzan == dependency.zzan && this.zzao == dependency.zzao;
    }

    public final int hashCode() {
        return ((((this.zzam.hashCode() ^ 1000003) * 1000003) ^ this.zzan) * 1000003) ^ this.zzao;
    }

    public final String toString() {
        boolean z = true;
        StringBuilder append = new StringBuilder("Dependency{interface=").append(this.zzam).append(", required=").append(this.zzan == 1).append(", direct=");
        if (this.zzao != 0) {
            z = false;
        }
        return append.append(z).append("}").toString();
    }

    public final Class<?> zzn() {
        return this.zzam;
    }

    public final boolean zzo() {
        return this.zzan == 1;
    }

    public final boolean zzp() {
        return this.zzao == 0;
    }
}
