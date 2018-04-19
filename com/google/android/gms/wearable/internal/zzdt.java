package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzdt extends AbstractSafeParcelable {
    public static final Creator<zzdt> CREATOR = new zzds();
    private final int statusCode;
    private final boolean zzdt;
    private final boolean zzdu;

    public zzdt(int i, boolean z, boolean z2) {
        this.statusCode = i;
        this.zzdt = z;
        this.zzdu = z2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.statusCode);
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzdt);
        SafeParcelWriter.writeBoolean(parcel, 4, this.zzdu);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
