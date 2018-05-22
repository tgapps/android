package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;

public final class zzee implements Creator<zzed> {
    public final /* synthetic */ Object createFromParcel(Parcel parcel) {
        int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        String str = null;
        String str2 = null;
        zzjx com_google_android_gms_internal_measurement_zzjx = null;
        long j = 0;
        boolean z = false;
        String str3 = null;
        zzeu com_google_android_gms_internal_measurement_zzeu = null;
        long j2 = 0;
        zzeu com_google_android_gms_internal_measurement_zzeu2 = null;
        long j3 = 0;
        zzeu com_google_android_gms_internal_measurement_zzeu3 = null;
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
                    com_google_android_gms_internal_measurement_zzjx = (zzjx) SafeParcelReader.createParcelable(parcel, readHeader, zzjx.CREATOR);
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
                    com_google_android_gms_internal_measurement_zzeu = (zzeu) SafeParcelReader.createParcelable(parcel, readHeader, zzeu.CREATOR);
                    break;
                case 9:
                    j2 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 10:
                    com_google_android_gms_internal_measurement_zzeu2 = (zzeu) SafeParcelReader.createParcelable(parcel, readHeader, zzeu.CREATOR);
                    break;
                case 11:
                    j3 = SafeParcelReader.readLong(parcel, readHeader);
                    break;
                case 12:
                    com_google_android_gms_internal_measurement_zzeu3 = (zzeu) SafeParcelReader.createParcelable(parcel, readHeader, zzeu.CREATOR);
                    break;
                default:
                    SafeParcelReader.skipUnknownField(parcel, readHeader);
                    break;
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new zzed(str, str2, com_google_android_gms_internal_measurement_zzjx, j, z, str3, com_google_android_gms_internal_measurement_zzeu, j2, com_google_android_gms_internal_measurement_zzeu2, j3, com_google_android_gms_internal_measurement_zzeu3);
    }

    public final /* synthetic */ Object[] newArray(int i) {
        return new zzed[i];
    }
}
