package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzdk extends AbstractSafeParcelable {
    public static final Creator<zzdk> CREATOR = new zzdl();
    public final int statusCode;
    public final zzah zzdq;

    public zzdk(int i, zzah com_google_android_gms_wearable_internal_zzah) {
        this.statusCode = i;
        this.zzdq = com_google_android_gms_wearable_internal_zzah;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.statusCode);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzdq, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
