package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable;

public class zzp {
    private static final ClassLoader zzqg = zzp.class.getClassLoader();

    private zzp() {
    }

    public static void zza(Parcel parcel, Parcelable parcelable) {
        if (parcelable == null) {
            parcel.writeInt(0);
            return;
        }
        parcel.writeInt(1);
        parcelable.writeToParcel(parcel, 0);
    }

    public static void zza(Parcel parcel, boolean z) {
        parcel.writeInt(z);
    }
}
