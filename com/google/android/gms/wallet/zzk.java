package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.identity.intents.model.UserAddress;

public final class zzk implements Creator<FullWallet> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        String str2 = str;
        ProxyCard proxyCard = str2;
        String str3 = proxyCard;
        zza com_google_android_gms_wallet_zza = str3;
        zza com_google_android_gms_wallet_zza2 = com_google_android_gms_wallet_zza;
        String[] strArr = com_google_android_gms_wallet_zza2;
        UserAddress userAddress = strArr;
        UserAddress userAddress2 = userAddress;
        InstrumentInfo[] instrumentInfoArr = userAddress2;
        PaymentMethodToken paymentMethodToken = instrumentInfoArr;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 3:
                    str2 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 4:
                    proxyCard = (ProxyCard) SafeParcelReader.createParcelable(parcel, readHeader, ProxyCard.CREATOR);
                    break;
                case 5:
                    str3 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 6:
                    com_google_android_gms_wallet_zza = (zza) SafeParcelReader.createParcelable(parcel, readHeader, zza.CREATOR);
                    break;
                case 7:
                    com_google_android_gms_wallet_zza2 = (zza) SafeParcelReader.createParcelable(parcel, readHeader, zza.CREATOR);
                    break;
                case 8:
                    strArr = SafeParcelReader.createStringArray(parcel, readHeader);
                    break;
                case 9:
                    userAddress = (UserAddress) SafeParcelReader.createParcelable(parcel, readHeader, UserAddress.CREATOR);
                    break;
                case 10:
                    userAddress2 = (UserAddress) SafeParcelReader.createParcelable(parcel, readHeader, UserAddress.CREATOR);
                    break;
                case 11:
                    instrumentInfoArr = (InstrumentInfo[]) SafeParcelReader.createTypedArray(parcel, readHeader, InstrumentInfo.CREATOR);
                    break;
                case 12:
                    paymentMethodToken = (PaymentMethodToken) SafeParcelReader.createParcelable(parcel, readHeader, PaymentMethodToken.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new FullWallet(str, str2, proxyCard, str3, com_google_android_gms_wallet_zza, com_google_android_gms_wallet_zza2, strArr, userAddress, userAddress2, instrumentInfoArr, paymentMethodToken);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new FullWallet[i];
    }
}
