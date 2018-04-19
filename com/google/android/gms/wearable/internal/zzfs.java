package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzfs extends AbstractSafeParcelable {
    public static final Creator<zzfs> CREATOR = new zzft();
    private final String label;
    private final String packageName;
    private final long zzep;

    public zzfs(String str, String str2, long j) {
        this.packageName = str;
        this.label = str2;
        this.zzep = j;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.label, false);
        SafeParcelWriter.writeLong(parcel, 4, this.zzep);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
