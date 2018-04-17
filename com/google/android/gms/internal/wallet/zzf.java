package com.google.android.gms.internal.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzf extends AbstractSafeParcelable {
    public static final Creator<zzf> CREATOR = new zzg();
    private byte[] zzev;

    zzf() {
        this(new byte[0]);
    }

    public zzf(byte[] bArr) {
        this.zzev = bArr;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeByteArray(parcel, 2, this.zzev, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
