package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzeg extends AbstractSafeParcelable {
    public static final Creator<zzeg> CREATOR = new zzeh();
    public final int statusCode;
    public final zzfo zzea;

    public zzeg(int i, zzfo com_google_android_gms_wearable_internal_zzfo) {
        this.statusCode = i;
        this.zzea = com_google_android_gms_wearable_internal_zzfo;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.statusCode);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzea, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
