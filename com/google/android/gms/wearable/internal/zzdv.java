package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzdv extends AbstractSafeParcelable {
    public static final Creator<zzdv> CREATOR = new zzdu();
    private final boolean enabled;
    private final int statusCode;

    public zzdv(int i, boolean z) {
        this.statusCode = i;
        this.enabled = z;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.statusCode);
        SafeParcelWriter.writeBoolean(parcel, 3, this.enabled);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
