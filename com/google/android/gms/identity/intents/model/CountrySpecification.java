package com.google.android.gms.identity.intents.model;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public class CountrySpecification extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<CountrySpecification> CREATOR = new zza();
    private String zzk;

    public CountrySpecification(String str) {
        this.zzk = str;
    }

    public void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzk, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
