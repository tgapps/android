package com.google.android.gms.internal.vision;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zze extends AbstractSafeParcelable {
    public static final Creator<zze> CREATOR = new zzf();
    public int zzbn;

    public zze(int i) {
        this.zzbn = i;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.zzbn);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
