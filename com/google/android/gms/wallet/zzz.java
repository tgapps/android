package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.identity.intents.model.CountrySpecification;
import java.util.ArrayList;

public final class zzz implements Creator<MaskedWalletRequest> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        boolean z = true;
        boolean z2 = z;
        boolean z3 = false;
        boolean z4 = z3;
        boolean z5 = z4;
        boolean z6 = z5;
        boolean z7 = z6;
        String str = null;
        String str2 = str;
        String str3 = str2;
        String str4 = str3;
        Cart cart = str4;
        CountrySpecification[] countrySpecificationArr = cart;
        ArrayList arrayList = countrySpecificationArr;
        PaymentMethodTokenizationParameters paymentMethodTokenizationParameters = arrayList;
        ArrayList arrayList2 = paymentMethodTokenizationParameters;
        String str5 = arrayList2;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 3:
                    z3 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 4:
                    z4 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 5:
                    z5 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 6:
                    str2 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 7:
                    str3 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 8:
                    str4 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 9:
                    cart = (Cart) SafeParcelReader.createParcelable(parcel2, readHeader, Cart.CREATOR);
                    break;
                case 10:
                    z6 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 11:
                    z7 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 12:
                    countrySpecificationArr = (CountrySpecification[]) SafeParcelReader.createTypedArray(parcel2, readHeader, CountrySpecification.CREATOR);
                    break;
                case 13:
                    z = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 14:
                    z2 = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 15:
                    arrayList = SafeParcelReader.createTypedList(parcel2, readHeader, CountrySpecification.CREATOR);
                    break;
                case 16:
                    paymentMethodTokenizationParameters = (PaymentMethodTokenizationParameters) SafeParcelReader.createParcelable(parcel2, readHeader, PaymentMethodTokenizationParameters.CREATOR);
                    break;
                case 17:
                    arrayList2 = SafeParcelReader.createIntegerList(parcel2, readHeader);
                    break;
                case 18:
                    str5 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new MaskedWalletRequest(str, z3, z4, z5, str2, str3, str4, cart, z6, z7, countrySpecificationArr, z, z2, arrayList, paymentMethodTokenizationParameters, arrayList2, str5);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new MaskedWalletRequest[i];
    }
}
