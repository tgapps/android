package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class UriData extends AbstractSafeParcelable {
    public static final Creator<UriData> CREATOR = new zzl();
    private String description;
    private String zzhd;

    UriData() {
    }

    public UriData(String str, String str2) {
        this.zzhd = str;
        this.description = str2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzhd, false);
        SafeParcelWriter.writeString(parcel, 3, this.description, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
