package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzdr extends AbstractSafeParcelable {
    public static final Creator<zzdr> CREATOR = new zzdq();
    private final int statusCode;
    private final boolean zzds;

    public zzdr(int i, boolean z) {
        this.statusCode = i;
        this.zzds = z;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.statusCode);
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzds);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
