package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzar extends AbstractSafeParcelable {
    public static final Creator<zzar> CREATOR = new zzas();
    private String zzeu;

    private zzar() {
    }

    zzar(String str) {
        this.zzeu = str;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzeu, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
