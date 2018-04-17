package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzeg implements Creator<zzef> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        Parcel parcel2 = parcel;
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        long j = 0;
        long j2 = j;
        long j3 = j2;
        String str = null;
        String str2 = str;
        zzjs com_google_android_gms_internal_measurement_zzjs = str2;
        String str3 = com_google_android_gms_internal_measurement_zzjs;
        zzeu com_google_android_gms_internal_measurement_zzeu = str3;
        zzeu com_google_android_gms_internal_measurement_zzeu2 = com_google_android_gms_internal_measurement_zzeu;
        zzeu com_google_android_gms_internal_measurement_zzeu3 = com_google_android_gms_internal_measurement_zzeu2;
        boolean z = false;
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
                    com_google_android_gms_internal_measurement_zzjs = (zzjs) SafeParcelReader.createParcelable(parcel2, readHeader, zzjs.CREATOR);
                    break;
                case 5:
                    j = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 6:
                    z = SafeParcelReader.readBoolean(parcel2, readHeader);
                    break;
                case 7:
                    str3 = SafeParcelReader.createString(parcel2, readHeader);
                    break;
                case 8:
                    com_google_android_gms_internal_measurement_zzeu = (zzeu) SafeParcelReader.createParcelable(parcel2, readHeader, zzeu.CREATOR);
                    break;
                case 9:
                    j2 = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 10:
                    com_google_android_gms_internal_measurement_zzeu2 = (zzeu) SafeParcelReader.createParcelable(parcel2, readHeader, zzeu.CREATOR);
                    break;
                case 11:
                    j3 = SafeParcelReader.readLong(parcel2, readHeader);
                    break;
                case 12:
                    com_google_android_gms_internal_measurement_zzeu3 = (zzeu) SafeParcelReader.createParcelable(parcel2, readHeader, zzeu.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel2, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel2, validateObjectHeader);
        return new zzef(str, str2, com_google_android_gms_internal_measurement_zzjs, j, z, str3, com_google_android_gms_internal_measurement_zzeu, j2, com_google_android_gms_internal_measurement_zzeu2, j3, com_google_android_gms_internal_measurement_zzeu3);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzef[i];
    }
}
