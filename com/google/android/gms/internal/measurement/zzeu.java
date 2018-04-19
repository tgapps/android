package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzeu extends AbstractSafeParcelable {
    public static final Creator<zzeu> CREATOR = new zzev();
    public final String name;
    public final String zzaek;
    public final zzer zzafo;
    public final long zzafz;

    public zzeu(String str, zzer com_google_android_gms_internal_measurement_zzer, String str2, long j) {
        this.name = str;
        this.zzafo = com_google_android_gms_internal_measurement_zzer;
        this.zzaek = str2;
        this.zzafz = j;
    }

    public final String toString() {
        String str = this.zzaek;
        String str2 = this.name;
        String valueOf = String.valueOf(this.zzafo);
        return new StringBuilder(((String.valueOf(str).length() + 21) + String.valueOf(str2).length()) + String.valueOf(valueOf).length()).append("origin=").append(str).append(",name=").append(str2).append(",params=").append(valueOf).toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzafo, i, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzaek, false);
        SafeParcelWriter.writeLong(parcel, 5, this.zzafz);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
