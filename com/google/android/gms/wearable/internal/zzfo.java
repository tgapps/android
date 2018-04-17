package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.wearable.Node;

public final class zzfo extends AbstractSafeParcelable implements Node {
    public static final Creator<zzfo> CREATOR = new zzfp();
    private final String zzbk;
    private final String zzdm;
    private final int zzen;
    private final boolean zzeo;

    public zzfo(String str, String str2, int i, boolean z) {
        this.zzdm = str;
        this.zzbk = str2;
        this.zzen = i;
        this.zzeo = z;
    }

    public final boolean equals(Object obj) {
        return !(obj instanceof zzfo) ? false : ((zzfo) obj).zzdm.equals(this.zzdm);
    }

    public final String getDisplayName() {
        return this.zzbk;
    }

    public final String getId() {
        return this.zzdm;
    }

    public final int hashCode() {
        return this.zzdm.hashCode();
    }

    public final boolean isNearby() {
        return this.zzeo;
    }

    public final String toString() {
        String str = this.zzbk;
        String str2 = this.zzdm;
        int i = this.zzen;
        boolean z = this.zzeo;
        StringBuilder stringBuilder = new StringBuilder((45 + String.valueOf(str).length()) + String.valueOf(str2).length());
        stringBuilder.append("Node{");
        stringBuilder.append(str);
        stringBuilder.append(", id=");
        stringBuilder.append(str2);
        stringBuilder.append(", hops=");
        stringBuilder.append(i);
        stringBuilder.append(", isNearby=");
        stringBuilder.append(z);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, getId(), false);
        SafeParcelWriter.writeString(parcel, 3, getDisplayName(), false);
        SafeParcelWriter.writeInt(parcel, 4, this.zzen);
        SafeParcelWriter.writeBoolean(parcel, 5, isNearby());
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
