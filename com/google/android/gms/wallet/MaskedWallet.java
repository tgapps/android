package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.identity.intents.model.UserAddress;

public final class MaskedWallet extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<MaskedWallet> CREATOR = new zzx();
    String zzaw;
    String zzax;
    String zzaz;
    private zza zzba;
    private zza zzbb;
    String[] zzbc;
    UserAddress zzbd;
    UserAddress zzbe;
    InstrumentInfo[] zzbf;
    private LoyaltyWalletObject[] zzda;
    private OfferWalletObject[] zzdb;

    private MaskedWallet() {
    }

    MaskedWallet(String str, String str2, String[] strArr, String str3, zza com_google_android_gms_wallet_zza, zza com_google_android_gms_wallet_zza2, LoyaltyWalletObject[] loyaltyWalletObjectArr, OfferWalletObject[] offerWalletObjectArr, UserAddress userAddress, UserAddress userAddress2, InstrumentInfo[] instrumentInfoArr) {
        this.zzaw = str;
        this.zzax = str2;
        this.zzbc = strArr;
        this.zzaz = str3;
        this.zzba = com_google_android_gms_wallet_zza;
        this.zzbb = com_google_android_gms_wallet_zza2;
        this.zzda = loyaltyWalletObjectArr;
        this.zzdb = offerWalletObjectArr;
        this.zzbd = userAddress;
        this.zzbe = userAddress2;
        this.zzbf = instrumentInfoArr;
    }

    public final String getGoogleTransactionId() {
        return this.zzaw;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzaw, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzax, false);
        SafeParcelWriter.writeStringArray(parcel, 4, this.zzbc, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzaz, false);
        SafeParcelWriter.writeParcelable(parcel, 6, this.zzba, i, false);
        SafeParcelWriter.writeParcelable(parcel, 7, this.zzbb, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 8, this.zzda, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 9, this.zzdb, i, false);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzbd, i, false);
        SafeParcelWriter.writeParcelable(parcel, 11, this.zzbe, i, false);
        SafeParcelWriter.writeTypedArray(parcel, 12, this.zzbf, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
