package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzeu extends AbstractSafeParcelable {
    public static final Creator<zzeu> CREATOR = new zzev();
    public final String name;
    public final String origin;
    public final zzer zzafq;
    public final long zzagb;

    zzeu(zzeu com_google_android_gms_internal_measurement_zzeu, long j) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzeu);
        this.name = com_google_android_gms_internal_measurement_zzeu.name;
        this.zzafq = com_google_android_gms_internal_measurement_zzeu.zzafq;
        this.origin = com_google_android_gms_internal_measurement_zzeu.origin;
        this.zzagb = j;
    }

    public zzeu(String str, zzer com_google_android_gms_internal_measurement_zzer, String str2, long j) {
        this.name = str;
        this.zzafq = com_google_android_gms_internal_measurement_zzer;
        this.origin = str2;
        this.zzagb = j;
    }

    public final String toString() {
        String str = this.origin;
        String str2 = this.name;
        String valueOf = String.valueOf(this.zzafq);
        return new StringBuilder(((String.valueOf(str).length() + 21) + String.valueOf(str2).length()) + String.valueOf(valueOf).length()).append("origin=").append(str).append(",name=").append(str2).append(",params=").append(valueOf).toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzafq, i, false);
        SafeParcelWriter.writeString(parcel, 4, this.origin, false);
        SafeParcelWriter.writeLong(parcel, 5, this.zzagb);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
