package com.google.android.gms.internal.measurement;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.Iterator;

public final class zzer extends AbstractSafeParcelable implements Iterable<String> {
    public static final Creator<zzer> CREATOR = new zzet();
    private final Bundle zzafw;

    zzer(Bundle bundle) {
        this.zzafw = bundle;
    }

    public final Iterator<String> iterator() {
        return new zzes(this);
    }

    public final String toString() {
        return this.zzafw.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, zzif(), false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }

    public final Bundle zzif() {
        return new Bundle(this.zzafw);
    }
}
