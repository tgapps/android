package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class PaymentMethodToken extends AbstractSafeParcelable {
    public static final Creator<PaymentMethodToken> CREATOR = new zzag();
    private int zzed;
    private String zzee;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.zzed);
        SafeParcelWriter.writeString(parcel, 3, this.zzee, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    PaymentMethodToken(int i, String str) {
        this.zzed = i;
        this.zzee = str;
    }

    private PaymentMethodToken() {
    }

    public final String getToken() {
        return this.zzee;
    }
}
