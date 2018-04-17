package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class TimeInterval extends AbstractSafeParcelable {
    public static final Creator<TimeInterval> CREATOR = new zzk();
    private long zzhb;
    private long zzhc;

    TimeInterval() {
    }

    public TimeInterval(long j, long j2) {
        this.zzhb = j;
        this.zzhc = j2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeLong(parcel, 2, this.zzhb);
        SafeParcelWriter.writeLong(parcel, 3, this.zzhc);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
