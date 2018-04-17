package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.Node;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.concurrent.GuardedBy;

public final class zzah extends AbstractSafeParcelable implements CapabilityInfo {
    public static final Creator<zzah> CREATOR = new zzai();
    private final Object lock = new Object();
    private final String name;
    @GuardedBy("lock")
    private Set<Node> zzbt;
    private final List<zzfo> zzca;

    public zzah(String str, List<zzfo> list) {
        this.name = str;
        this.zzca = list;
        this.zzbt = null;
        Preconditions.checkNotNull(this.name);
        Preconditions.checkNotNull(this.zzca);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        zzah com_google_android_gms_wearable_internal_zzah = (zzah) obj;
        if (this.name != null) {
            if (!this.name.equals(com_google_android_gms_wearable_internal_zzah.name)) {
                return false;
            }
        } else if (com_google_android_gms_wearable_internal_zzah.name != null) {
            return false;
        }
        if (this.zzca != null) {
            if (!this.zzca.equals(com_google_android_gms_wearable_internal_zzah.zzca)) {
                return false;
            }
        } else if (com_google_android_gms_wearable_internal_zzah.zzca != null) {
            return false;
        }
        return true;
    }

    public final String getName() {
        return this.name;
    }

    public final Set<Node> getNodes() {
        Set<Node> set;
        synchronized (this.lock) {
            if (this.zzbt == null) {
                this.zzbt = new HashSet(this.zzca);
            }
            set = this.zzbt;
        }
        return set;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = ((this.name != null ? this.name.hashCode() : 0) + 31) * 31;
        if (this.zzca != null) {
            i = this.zzca.hashCode();
        }
        return hashCode + i;
    }

    public final String toString() {
        String str = this.name;
        String valueOf = String.valueOf(this.zzca);
        StringBuilder stringBuilder = new StringBuilder((18 + String.valueOf(str).length()) + String.valueOf(valueOf).length());
        stringBuilder.append("CapabilityInfo{");
        stringBuilder.append(str);
        stringBuilder.append(", ");
        stringBuilder.append(valueOf);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, getName(), false);
        SafeParcelWriter.writeTypedList(parcel, 3, this.zzca, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
