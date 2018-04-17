package com.google.android.gms.internal.config;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzz extends AbstractSafeParcelable {
    public static final Creator<zzz> CREATOR = new zzaa();
    private final String mName;
    private final String mValue;

    public zzz(String str, String str2) {
        this.mName = str;
        this.mValue = str2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.mName, false);
        SafeParcelWriter.writeString(parcel, 3, this.mValue, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
