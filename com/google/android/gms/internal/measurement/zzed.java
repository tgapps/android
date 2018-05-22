package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzed extends AbstractSafeParcelable {
    public static final Creator<zzed> CREATOR = new zzee();
    public boolean active;
    public long creationTimestamp;
    public String origin;
    public String packageName;
    public long timeToLive;
    public String triggerEventName;
    public long triggerTimeout;
    public zzjx zzaep;
    public zzeu zzaeq;
    public zzeu zzaer;
    public zzeu zzaes;

    zzed(zzed com_google_android_gms_internal_measurement_zzed) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzed);
        this.packageName = com_google_android_gms_internal_measurement_zzed.packageName;
        this.origin = com_google_android_gms_internal_measurement_zzed.origin;
        this.zzaep = com_google_android_gms_internal_measurement_zzed.zzaep;
        this.creationTimestamp = com_google_android_gms_internal_measurement_zzed.creationTimestamp;
        this.active = com_google_android_gms_internal_measurement_zzed.active;
        this.triggerEventName = com_google_android_gms_internal_measurement_zzed.triggerEventName;
        this.zzaeq = com_google_android_gms_internal_measurement_zzed.zzaeq;
        this.triggerTimeout = com_google_android_gms_internal_measurement_zzed.triggerTimeout;
        this.zzaer = com_google_android_gms_internal_measurement_zzed.zzaer;
        this.timeToLive = com_google_android_gms_internal_measurement_zzed.timeToLive;
        this.zzaes = com_google_android_gms_internal_measurement_zzed.zzaes;
    }

    zzed(String str, String str2, zzjx com_google_android_gms_internal_measurement_zzjx, long j, boolean z, String str3, zzeu com_google_android_gms_internal_measurement_zzeu, long j2, zzeu com_google_android_gms_internal_measurement_zzeu2, long j3, zzeu com_google_android_gms_internal_measurement_zzeu3) {
        this.packageName = str;
        this.origin = str2;
        this.zzaep = com_google_android_gms_internal_measurement_zzjx;
        this.creationTimestamp = j;
        this.active = z;
        this.triggerEventName = str3;
        this.zzaeq = com_google_android_gms_internal_measurement_zzeu;
        this.triggerTimeout = j2;
        this.zzaer = com_google_android_gms_internal_measurement_zzeu2;
        this.timeToLive = j3;
        this.zzaes = com_google_android_gms_internal_measurement_zzeu3;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.origin, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzaep, i, false);
        SafeParcelWriter.writeLong(parcel, 5, this.creationTimestamp);
        SafeParcelWriter.writeBoolean(parcel, 6, this.active);
        SafeParcelWriter.writeString(parcel, 7, this.triggerEventName, false);
        SafeParcelWriter.writeParcelable(parcel, 8, this.zzaeq, i, false);
        SafeParcelWriter.writeLong(parcel, 9, this.triggerTimeout);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzaer, i, false);
        SafeParcelWriter.writeLong(parcel, 11, this.timeToLive);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzaes, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
