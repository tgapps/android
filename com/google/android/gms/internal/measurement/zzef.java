package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzef extends AbstractSafeParcelable {
    public static final Creator<zzef> CREATOR = new zzeg();
    public String packageName;
    public String zzaek;
    public zzjs zzael;
    public long zzaem;
    public boolean zzaen;
    public String zzaeo;
    public zzeu zzaep;
    public long zzaeq;
    public zzeu zzaer;
    public long zzaes;
    public zzeu zzaet;

    zzef(zzef com_google_android_gms_internal_measurement_zzef) {
        Preconditions.checkNotNull(com_google_android_gms_internal_measurement_zzef);
        this.packageName = com_google_android_gms_internal_measurement_zzef.packageName;
        this.zzaek = com_google_android_gms_internal_measurement_zzef.zzaek;
        this.zzael = com_google_android_gms_internal_measurement_zzef.zzael;
        this.zzaem = com_google_android_gms_internal_measurement_zzef.zzaem;
        this.zzaen = com_google_android_gms_internal_measurement_zzef.zzaen;
        this.zzaeo = com_google_android_gms_internal_measurement_zzef.zzaeo;
        this.zzaep = com_google_android_gms_internal_measurement_zzef.zzaep;
        this.zzaeq = com_google_android_gms_internal_measurement_zzef.zzaeq;
        this.zzaer = com_google_android_gms_internal_measurement_zzef.zzaer;
        this.zzaes = com_google_android_gms_internal_measurement_zzef.zzaes;
        this.zzaet = com_google_android_gms_internal_measurement_zzef.zzaet;
    }

    zzef(String str, String str2, zzjs com_google_android_gms_internal_measurement_zzjs, long j, boolean z, String str3, zzeu com_google_android_gms_internal_measurement_zzeu, long j2, zzeu com_google_android_gms_internal_measurement_zzeu2, long j3, zzeu com_google_android_gms_internal_measurement_zzeu3) {
        this.packageName = str;
        this.zzaek = str2;
        this.zzael = com_google_android_gms_internal_measurement_zzjs;
        this.zzaem = j;
        this.zzaen = z;
        this.zzaeo = str3;
        this.zzaep = com_google_android_gms_internal_measurement_zzeu;
        this.zzaeq = j2;
        this.zzaer = com_google_android_gms_internal_measurement_zzeu2;
        this.zzaes = j3;
        this.zzaet = com_google_android_gms_internal_measurement_zzeu3;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzaek, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzael, i, false);
        SafeParcelWriter.writeLong(parcel, 5, this.zzaem);
        SafeParcelWriter.writeBoolean(parcel, 6, this.zzaen);
        SafeParcelWriter.writeString(parcel, 7, this.zzaeo, false);
        SafeParcelWriter.writeParcelable(parcel, 8, this.zzaep, i, false);
        SafeParcelWriter.writeLong(parcel, 9, this.zzaeq);
        SafeParcelWriter.writeParcelable(parcel, 10, this.zzaer, i, false);
        SafeParcelWriter.writeLong(parcel, 11, this.zzaes);
        SafeParcelWriter.writeParcelable(parcel, 12, this.zzaet, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
