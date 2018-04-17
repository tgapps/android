package com.google.android.gms.wallet.fragment;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzf implements Creator<WalletFragmentOptions> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int i = 1;
        int i2 = 0;
        WalletFragmentStyle walletFragmentStyle = null;
        int i3 = 1;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    i = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 3:
                    i2 = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                case 4:
                    walletFragmentStyle = (WalletFragmentStyle) SafeParcelReader.createParcelable(parcel, readHeader, WalletFragmentStyle.CREATOR);
                    break;
                case 5:
                    i3 = SafeParcelReader.readInt(parcel, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new WalletFragmentOptions(i, i2, walletFragmentStyle, i3);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new WalletFragmentOptions[i];
    }
}
