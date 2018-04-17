package com.google.android.gms.vision.face.internal.client;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzb implements Creator<FaceParcel> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        int i = 0;
        int i2 = i;
        float f = 0.0f;
        float f2 = f;
        float f3 = f2;
        float f4 = f3;
        float f5 = f4;
        float f6 = f5;
        float f7 = f6;
        float f8 = f7;
        float f9 = f8;
        LandmarkParcel[] landmarkParcelArr = null;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(readHeader)) {
                case 1:
                    i = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 2:
                    i2 = SafeParcelReader.readInt(parcel2, readHeader);
                    break;
                case 3:
                    f = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 4:
                    f2 = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 5:
                    f3 = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 6:
                    f4 = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 7:
                    f5 = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 8:
                    f6 = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 9:
                    landmarkParcelArr = (LandmarkParcel[]) SafeParcelReader.createTypedArray(parcel2, readHeader, LandmarkParcel.CREATOR);
                    break;
                case 10:
                    f7 = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 11:
                    f8 = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                case 12:
                    f9 = SafeParcelReader.readFloat(parcel2, readHeader);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new FaceParcel(i, i2, f, f2, f3, f4, f5, f6, landmarkParcelArr, f7, f8, f9);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new FaceParcel[i];
    }
}
