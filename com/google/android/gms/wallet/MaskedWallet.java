package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.identity.intents.model.UserAddress;

public final class MaskedWallet extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<MaskedWallet> CREATOR = new zzx();
    String zzax;
    String zzay;
    String zzba;
    private zza zzbb;
    private zza zzbc;
    String[] zzbd;
    UserAddress zzbe;
    UserAddress zzbf;
    InstrumentInfo[] zzbg;
    private LoyaltyWalletObject[] zzdb;
    private OfferWalletObject[] zzdc;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzax, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzay, false);
        SafeParcelWriter.writeStringArray(parcel, 4, this.zzbd, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzba, false);
        SafeParcelWriter.writeParcelable(parcel, 6, this.zzbb, i, false);
        SafeParcelWriter.writeParcelable(parcel, 7, this.zzbc, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 8, this.zzdb, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 9, this.zzdc, i, false);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzbe, i, false);
        SafeParcelWriter.writeParcelable(parcel, 11, this.zzbf, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 12, this.zzbg, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    MaskedWallet(String str, String str2, String[] strArr, String str3, zza com_google_android_gms_wallet_zza, zza com_google_android_gms_wallet_zza2, LoyaltyWalletObject[] loyaltyWalletObjectArr, OfferWalletObject[] offerWalletObjectArr, UserAddress userAddress, UserAddress userAddress2, InstrumentInfo[] instrumentInfoArr) {
        this.zzax = str;
        this.zzay = str2;
        this.zzbd = strArr;
        this.zzba = str3;
        this.zzbb = com_google_android_gms_wallet_zza;
        this.zzbc = com_google_android_gms_wallet_zza2;
        this.zzdb = loyaltyWalletObjectArr;
        this.zzdc = offerWalletObjectArr;
        this.zzbe = userAddress;
        this.zzbf = userAddress2;
        this.zzbg = instrumentInfoArr;
    }

    private MaskedWallet() {
    }

    public final String getGoogleTransactionId() {
        return this.zzax;
    }
}
