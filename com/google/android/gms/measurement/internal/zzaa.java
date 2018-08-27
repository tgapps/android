package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.Iterator;

public final class zzaa extends AbstractSafeParcelable implements Iterable<String> {
    public static final Creator<zzaa> CREATOR = new zzac();
    private final Bundle zzaim;

    zzaa(Bundle bundle) {
        this.zzaim = bundle;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, zziv(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    final Object get(String str) {
        return this.zzaim.get(str);
    }

    final Long getLong(String str) {
        return Long.valueOf(this.zzaim.getLong(str));
    }

    final Double zzbq(String str) {
        return Double.valueOf(this.zzaim.getDouble(str));
    }

    final String getString(String str) {
        return this.zzaim.getString(str);
    }

    public final int size() {
        return this.zzaim.size();
    }

    public final String toString() {
        return this.zzaim.toString();
    }

    public final Bundle zziv() {
        return new Bundle(this.zzaim);
    }

    public final Iterator<String> iterator() {
        return new zzab(this);
    }
}
