package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.List;

public final class zzge extends AbstractSafeParcelable {
    public static final Creator<zzge> CREATOR = new zzgf();
    private final int statusCode;
    private final long zzep;
    private final List<zzfs> zzer;

    public zzge(int i, long j, List<zzfs> list) {
        this.statusCode = i;
        this.zzep = j;
        this.zzer = list;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.statusCode);
        SafeParcelWriter.writeLong(parcel, 3, this.zzep);
        SafeParcelWriter.writeTypedList(parcel, 4, this.zzer, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
