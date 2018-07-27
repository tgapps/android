package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.Iterator;

public final class zzet extends AbstractSafeParcelable implements Iterable<String> {
    public static final Creator<zzet> CREATOR = new zzev();
    private final Bundle zzafz;

    zzet(Bundle bundle) {
        this.zzafz = bundle;
    }

    public final Iterator<String> iterator() {
        return new zzeu(this);
    }

    public final String toString() {
        return this.zzafz.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, zzij(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public final Bundle zzij() {
        return new Bundle(this.zzafz);
    }
}
