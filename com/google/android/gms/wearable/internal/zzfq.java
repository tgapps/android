package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzfq extends AbstractSafeParcelable {
    public static final Creator<zzfq> CREATOR = new zzfr();
    public final int statusCode;
    public final zzay zzck;

    public zzfq(int i, zzay com_google_android_gms_wearable_internal_zzay) {
        this.statusCode = i;
        this.zzck = com_google_android_gms_wearable_internal_zzay;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.statusCode);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzck, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
