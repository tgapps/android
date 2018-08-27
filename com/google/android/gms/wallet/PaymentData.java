package com.google.android.gms.wallet;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.identity.intents.model.UserAddress;

public final class PaymentData extends AbstractSafeParcelable {
    public static final Creator<PaymentData> CREATOR = new zzad();
    private String zzax;
    private String zzba;
    private PaymentMethodToken zzbh;
    private String zzbz;
    private CardInfo zzdt;
    private UserAddress zzdu;
    private Bundle zzdv;

    PaymentData(String str, CardInfo cardInfo, UserAddress userAddress, PaymentMethodToken paymentMethodToken, String str2, Bundle bundle, String str3) {
        this.zzba = str;
        this.zzdt = cardInfo;
        this.zzdu = userAddress;
        this.zzbh = paymentMethodToken;
        this.zzax = str2;
        this.zzdv = bundle;
        this.zzbz = str3;
    }

    private PaymentData() {
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, this.zzba, false);
        SafeParcelWriter.writeParcelable(parcel, 2, this.zzdt, i, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzdu, i, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzbh, i, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzax, false);
        SafeParcelWriter.writeBundle(parcel, 6, this.zzdv, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzbz, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
