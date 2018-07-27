package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzee extends AbstractSafeParcelable {
    public static final Creator<zzee> CREATOR = new zzef();
    public boolean active;
    public long creationTimestamp;
    public String origin;
    public String packageName;
    public long timeToLive;
    public String triggerEventName;
    public long triggerTimeout;
    public zzjz zzaeq;
    public zzew zzaer;
    public zzew zzaes;
    public zzew zzaet;

    zzee(zzee com_google_android_gms_internal_measurement_zzee) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzee);
        this.packageName = com_google_android_gms_internal_measurement_zzee.packageName;
        this.origin = com_google_android_gms_internal_measurement_zzee.origin;
        this.zzaeq = com_google_android_gms_internal_measurement_zzee.zzaeq;
        this.creationTimestamp = com_google_android_gms_internal_measurement_zzee.creationTimestamp;
        this.active = com_google_android_gms_internal_measurement_zzee.active;
        this.triggerEventName = com_google_android_gms_internal_measurement_zzee.triggerEventName;
        this.zzaer = com_google_android_gms_internal_measurement_zzee.zzaer;
        this.triggerTimeout = com_google_android_gms_internal_measurement_zzee.triggerTimeout;
        this.zzaes = com_google_android_gms_internal_measurement_zzee.zzaes;
        this.timeToLive = com_google_android_gms_internal_measurement_zzee.timeToLive;
        this.zzaet = com_google_android_gms_internal_measurement_zzee.zzaet;
    }

    zzee(String str, String str2, zzjz com_google_android_gms_internal_measurement_zzjz, long j, boolean z, String str3, zzew com_google_android_gms_internal_measurement_zzew, long j2, zzew com_google_android_gms_internal_measurement_zzew2, long j3, zzew com_google_android_gms_internal_measurement_zzew3) {
        this.packageName = str;
        this.origin = str2;
        this.zzaeq = com_google_android_gms_internal_measurement_zzjz;
        this.creationTimestamp = j;
        this.active = z;
        this.triggerEventName = str3;
        this.zzaer = com_google_android_gms_internal_measurement_zzew;
        this.triggerTimeout = j2;
        this.zzaes = com_google_android_gms_internal_measurement_zzew2;
        this.timeToLive = j3;
        this.zzaet = com_google_android_gms_internal_measurement_zzew3;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.origin, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzaeq, i, false);
        SafeParcelWriter.writeLong(parcel, 5, this.creationTimestamp);
        SafeParcelWriter.writeBoolean(parcel, 6, this.active);
        SafeParcelWriter.writeString(parcel, 7, this.triggerEventName, false);
        SafeParcelWriter.writeParcelable(parcel, 8, this.zzaer, i, false);
        SafeParcelWriter.writeLong(parcel, 9, this.triggerTimeout);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzaes, i, false);
        SafeParcelWriter.writeLong(parcel, 11, this.timeToLive);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzaet, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
