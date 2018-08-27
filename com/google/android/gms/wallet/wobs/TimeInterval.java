package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class TimeInterval extends AbstractSafeParcelable {
    public static final Creator<TimeInterval> CREATOR = new zzk();
    private long zzhd;
    private long zzhe;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeLong(parcel, 2, this.zzhd);
        SafeParcelWriter.writeLong(parcel, 3, this.zzhe);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public TimeInterval(long j, long j2) {
        this.zzhd = j;
        this.zzhe = j2;
    }

    TimeInterval() {
    }
}
