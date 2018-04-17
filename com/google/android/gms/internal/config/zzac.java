package com.google.android.gms.internal.config;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import java.util.List;

public final class zzac implements Creator<zzab> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int i = 0;
        int i2 = i;
        int i3 = i2;
        String str = null;
        DataHolder dataHolder = str;
        String str2 = dataHolder;
        String str3 = str2;
        String str4 = str3;
        List list = str4;
        List list2 = list;
        long j = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 2:
                    str = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 3:
                    j = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 4:
                    dataHolder = (DataHolder) SafeParcelReader.createParcelable(parcel2, readHeader, DataHolder.CREATOR);
                    break;
                case 5:
                    str2 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 6:
                    str3 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 7:
                    str4 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 8:
                    list = SafeParcelReader.createStringList(parcel2, readHeader);
                    break;
                case 9:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 10:
                    list2 = SafeParcelReader.createTypedList(parcel2, readHeader, zzl.CREATOR);
                    break;
                case 11:
                    i2 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 12:
                    i3 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new zzab(str, j, dataHolder, str2, str3, str4, list, i, list2, i2, i3);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzab[i];
    }
}
