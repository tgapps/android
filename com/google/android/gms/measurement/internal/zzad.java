package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzad extends AbstractSafeParcelable {
    public static final Creator<zzad> CREATOR = new zzae();
    public final String name;
    public final String origin;
    public final zzaa zzaid;
    public final long zzaip;

    public zzad(String str, zzaa com_google_android_gms_measurement_internal_zzaa, String str2, long j) {
        this.name = str;
        this.zzaid = com_google_android_gms_measurement_internal_zzaa;
        this.origin = str2;
        this.zzaip = j;
    }

    zzad(zzad com_google_android_gms_measurement_internal_zzad, long j) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzad);
        this.name = com_google_android_gms_measurement_internal_zzad.name;
        this.zzaid = com_google_android_gms_measurement_internal_zzad.zzaid;
        this.origin = com_google_android_gms_measurement_internal_zzad.origin;
        this.zzaip = j;
    }

    public final String toString() {
        String str = this.origin;
        String str2 = this.name;
        String valueOf = String.valueOf(this.zzaid);
        return new StringBuilder(((String.valueOf(str).length() + 21) + String.valueOf(str2).length()) + String.valueOf(valueOf).length()).append("origin=").append(str).append(",name=").append(str2).append(",params=").append(valueOf).toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzaid, i, false);
        SafeParcelWriter.writeString(parcel, 4, this.origin, false);
        SafeParcelWriter.writeLong(parcel, 5, this.zzaip);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
