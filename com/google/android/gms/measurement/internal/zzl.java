package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzl extends AbstractSafeParcelable {
    public static final Creator<zzl> CREATOR = new zzm();
    public boolean active;
    public long creationTimestamp;
    public String origin;
    public String packageName;
    public long timeToLive;
    public String triggerEventName;
    public long triggerTimeout;
    public zzfh zzahb;
    public zzad zzahc;
    public zzad zzahd;
    public zzad zzahe;

    zzl(zzl com_google_android_gms_measurement_internal_zzl) {
        Preconditions.checkNotNull(com_google_android_gms_measurement_internal_zzl);
        this.packageName = com_google_android_gms_measurement_internal_zzl.packageName;
        this.origin = com_google_android_gms_measurement_internal_zzl.origin;
        this.zzahb = com_google_android_gms_measurement_internal_zzl.zzahb;
        this.creationTimestamp = com_google_android_gms_measurement_internal_zzl.creationTimestamp;
        this.active = com_google_android_gms_measurement_internal_zzl.active;
        this.triggerEventName = com_google_android_gms_measurement_internal_zzl.triggerEventName;
        this.zzahc = com_google_android_gms_measurement_internal_zzl.zzahc;
        this.triggerTimeout = com_google_android_gms_measurement_internal_zzl.triggerTimeout;
        this.zzahd = com_google_android_gms_measurement_internal_zzl.zzahd;
        this.timeToLive = com_google_android_gms_measurement_internal_zzl.timeToLive;
        this.zzahe = com_google_android_gms_measurement_internal_zzl.zzahe;
    }

    zzl(String str, String str2, zzfh com_google_android_gms_measurement_internal_zzfh, long j, boolean z, String str3, zzad com_google_android_gms_measurement_internal_zzad, long j2, zzad com_google_android_gms_measurement_internal_zzad2, long j3, zzad com_google_android_gms_measurement_internal_zzad3) {
        this.packageName = str;
        this.origin = str2;
        this.zzahb = com_google_android_gms_measurement_internal_zzfh;
        this.creationTimestamp = j;
        this.active = z;
        this.triggerEventName = str3;
        this.zzahc = com_google_android_gms_measurement_internal_zzad;
        this.triggerTimeout = j2;
        this.zzahd = com_google_android_gms_measurement_internal_zzad2;
        this.timeToLive = j3;
        this.zzahe = com_google_android_gms_measurement_internal_zzad3;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.origin, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzahb, i, false);
        SafeParcelWriter.writeLong(parcel, 5, this.creationTimestamp);
        SafeParcelWriter.writeBoolean(parcel, 6, this.active);
        SafeParcelWriter.writeString(parcel, 7, this.triggerEventName, false);
        SafeParcelWriter.writeParcelable(parcel, 8, this.zzahc, i, false);
        SafeParcelWriter.writeLong(parcel, 9, this.triggerTimeout);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzahd, i, false);
        SafeParcelWriter.writeLong(parcel, 11, this.timeToLive);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzahe, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
