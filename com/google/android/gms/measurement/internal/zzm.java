package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzm implements Creator<zzl> {
    public final /* synthetic */ Object[] newArray(int i) {
        return new zzl[i];
    }

    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        String str2 = null;
        zzfh com_google_android_gms_measurement_internal_zzfh = null;
        long j = 0;
        boolean z = false;
        String str3 = null;
        zzad com_google_android_gms_measurement_internal_zzad = null;
        long j2 = 0;
        zzad com_google_android_gms_measurement_internal_zzad2 = null;
        long j3 = 0;
        zzad com_google_android_gms_measurement_internal_zzad3 = null;
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
                    com_google_android_gms_measurement_internal_zzfh = (zzfh) SafeParcelReader.createParcelable(parcel, readHeader, zzfh.CREATOR);
                    break;
                case 5:
                    j = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 6:
                    z = SafeParcelReader.readBoolean(parcel, readHeader);
                    break;
                case 7:
                    str3 = SafeParcelReader.createString(parcel, readHeader);
                    break;
                case 8:
                    com_google_android_gms_measurement_internal_zzad = (zzad) SafeParcelReader.createParcelable(parcel, readHeader, zzad.CREATOR);
                    break;
                case 9:
                    j2 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 10:
                    com_google_android_gms_measurement_internal_zzad2 = (zzad) SafeParcelReader.createParcelable(parcel, readHeader, zzad.CREATOR);
                    break;
                case 11:
                    j3 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 12:
                    com_google_android_gms_measurement_internal_zzad3 = (zzad) SafeParcelReader.createParcelable(parcel, readHeader, zzad.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzl(str, str2, com_google_android_gms_measurement_internal_zzfh, j, z, str3, com_google_android_gms_measurement_internal_zzad, j2, com_google_android_gms_measurement_internal_zzad2, j3, com_google_android_gms_measurement_internal_zzad3);
    }
}
