package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.ArrayList;

public final class zzr implements Creator<IsReadyToPayRequest> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        ArrayList arrayList = null;
        String str = arrayList;
        String str2 = str;
        ArrayList arrayList2 = str2;
        String str3 = arrayList2;
        boolean z = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            int fieldId = SafeParcelReader.getFieldId(readHeader);
            if (fieldId != 2) {
                switch (fieldId) {
                    case 4:
                        str = SafeParcelReader.createString(parcel, readHeader);
                        break;
                    case 5:
                        str2 = SafeParcelReader.createString(parcel, readHeader);
                        break;
                    case 6:
                        arrayList2 = SafeParcelReader.createIntegerList(parcel, readHeader);
                        break;
                    case 7:
                        z = SafeParcelReader.readBoolean(parcel, readHeader);
                        break;
                    case 8:
                        str3 = SafeParcelReader.createString(parcel, readHeader);
                        break;
                    default:
                        SafeParcelReader.skipUnknownField(parcel, readHeader);
                        break;
                }
            }
            arrayList = SafeParcelReader.createIntegerList(parcel, readHeader);
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new IsReadyToPayRequest(arrayList, str, str2, arrayList2, z, str3);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new IsReadyToPayRequest[i];
    }
}
