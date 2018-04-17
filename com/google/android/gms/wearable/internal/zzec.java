package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzec extends AbstractSafeParcelable {
    public static final Creator<zzec> CREATOR = new zzed();
    public final int statusCode;
    public final zzdd zzdy;

    public zzec(int i, zzdd com_google_android_gms_wearable_internal_zzdd) {
        this.statusCode = i;
        this.zzdy = com_google_android_gms_wearable_internal_zzdd;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.statusCode);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzdy, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
