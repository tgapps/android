package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

@Deprecated
public class CountrySpecification extends AbstractSafeParcelable {
    public static final Creator<CountrySpecification> CREATOR = new zzh();
    private String zzh;

    public CountrySpecification(String str) {
        this.zzh = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzh, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
