package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzde implements Creator<zzdd> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        Uri uri = null;
        Bundle bundle = null;
        byte[] bArr = bundle;
        while (parcel.dataPosition() < validateObjectHeader) {
            int readHeader = SafeParcelReader.readHeader(parcel);
            int fieldId = SafeParcelReader.getFieldId(readHeader);
            if (fieldId != 2) {
                switch (fieldId) {
                    case 4:
                        bundle = SafeParcelReader.createBundle(parcel, readHeader);
                        break;
                    case 5:
                        bArr = SafeParcelReader.createByteArray(parcel, readHeader);
                        break;
                    default:
                        SafeParcelReader.skipUnknownField(parcel, readHeader);
                        break;
                }
            }
            uri = (Uri) SafeParcelReader.createParcelable(parcel, readHeader, Uri.CREATOR);
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzdd(uri, bundle, bArr);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzdd[i];
    }
}
