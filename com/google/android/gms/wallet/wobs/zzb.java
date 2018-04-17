package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public final class zzb implements Creator<CommonWalletObject> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        ArrayList newArrayList = ArrayUtils.newArrayList();
        ArrayList newArrayList2 = ArrayUtils.newArrayList();
        ArrayList newArrayList3 = ArrayUtils.newArrayList();
        ArrayList newArrayList4 = ArrayUtils.newArrayList();
        ArrayList arrayList = newArrayList;
        ArrayList arrayList2 = newArrayList2;
        ArrayList arrayList3 = newArrayList3;
        ArrayList arrayList4 = newArrayList4;
        ArrayList newArrayList5 = ArrayUtils.newArrayList();
        ArrayList newArrayList6 = ArrayUtils.newArrayList();
        int i = 0;
        boolean z = i;
        String str = null;
        String str2 = str;
        String str3 = str2;
        String str4 = str3;
        String str5 = str4;
        String str6 = str5;
        String str7 = str6;
        String str8 = str7;
        TimeInterval timeInterval = str8;
        String str9 = timeInterval;
        String str10 = str9;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 3:
                    str2 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 4:
                    str3 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 5:
                    str4 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 6:
                    str5 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 7:
                    str6 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 8:
                    str7 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 9:
                    str8 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 10:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 11:
                    arrayList = SafeParcelReader.createTypedList(parcel2, readHeader, WalletObjectMessage.CREATOR);
                    break;
                case 12:
                    timeInterval = (TimeInterval) SafeParcelReader.createParcelable(parcel2, readHeader, TimeInterval.CREATOR);
                    break;
                case 13:
                    arrayList2 = SafeParcelReader.createTypedList(parcel2, readHeader, LatLng.CREATOR);
                    break;
                case 14:
                    str9 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 15:
                    str10 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 16:
                    arrayList3 = SafeParcelReader.createTypedList(parcel2, readHeader, LabelValueRow.CREATOR);
                    break;
                case 17:
                    z = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 18:
                    arrayList4 = SafeParcelReader.createTypedList(parcel2, readHeader, UriData.CREATOR);
                    break;
                case 19:
                    newArrayList5 = SafeParcelReader.createTypedList(parcel2, readHeader, TextModuleData.CREATOR);
                    break;
                case 20:
                    newArrayList6 = SafeParcelReader.createTypedList(parcel2, readHeader, UriData.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new CommonWalletObject(str, str2, str3, str4, str5, str6, str7, str8, i, arrayList, timeInterval, arrayList2, str9, str10, arrayList3, z, arrayList4, newArrayList5, newArrayList6);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new CommonWalletObject[i];
    }
}
