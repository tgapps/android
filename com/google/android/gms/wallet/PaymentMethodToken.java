package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class PaymentMethodToken extends AbstractSafeParcelable {
    public static final Creator<PaymentMethodToken> CREATOR = new zzaf();
    private int zzeb;
    private String zzec;

    private PaymentMethodToken() {
    }

    PaymentMethodToken(int i, String str) {
        this.zzeb = i;
        this.zzec = str;
    }

    public final String getToken() {
        return this.zzec;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.zzeb);
        SafeParcelWriter.writeString(parcel, 3, this.zzec, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
