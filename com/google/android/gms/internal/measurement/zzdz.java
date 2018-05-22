package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzdz extends AbstractSafeParcelable {
    public static final Creator<zzdz> CREATOR = new zzea();
    public final String packageName;
    public final String zzadm;
    public final String zzado;
    public final long zzads;
    public final String zzadt;
    public final long zzadu;
    public final long zzadv;
    public final boolean zzadw;
    public final long zzadx;
    public final boolean zzady;
    public final boolean zzadz;
    public final String zzaek;
    public final boolean zzael;
    public final long zzaem;
    public final int zzaen;
    public final boolean zzaeo;
    public final String zzth;

    zzdz(String str, String str2, String str3, long j, String str4, long j2, long j3, String str5, boolean z, boolean z2, String str6, long j4, long j5, int i, boolean z3, boolean z4, boolean z5) {
        Preconditions.checkNotEmpty(str);
        this.packageName = str;
        if (TextUtils.isEmpty(str2)) {
            str2 = null;
        }
        this.zzadm = str2;
        this.zzth = str3;
        this.zzads = j;
        this.zzadt = str4;
        this.zzadu = j2;
        this.zzadv = j3;
        this.zzaek = str5;
        this.zzadw = z;
        this.zzael = z2;
        this.zzado = str6;
        this.zzadx = j4;
        this.zzaem = j5;
        this.zzaen = i;
        this.zzady = z3;
        this.zzadz = z4;
        this.zzaeo = z5;
    }

    zzdz(String str, String str2, String str3, String str4, long j, long j2, String str5, boolean z, boolean z2, long j3, String str6, long j4, long j5, int i, boolean z3, boolean z4, boolean z5) {
        this.packageName = str;
        this.zzadm = str2;
        this.zzth = str3;
        this.zzads = j3;
        this.zzadt = str4;
        this.zzadu = j;
        this.zzadv = j2;
        this.zzaek = str5;
        this.zzadw = z;
        this.zzael = z2;
        this.zzado = str6;
        this.zzadx = j4;
        this.zzaem = j5;
        this.zzaen = i;
        this.zzady = z3;
        this.zzadz = z4;
        this.zzaeo = z5;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzadm, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzth, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzadt, false);
        SafeParcelWriter.writeLong(parcel, 6, this.zzadu);
        SafeParcelWriter.writeLong(parcel, 7, this.zzadv);
        SafeParcelWriter.writeString(parcel, 8, this.zzaek, false);
        SafeParcelWriter.writeBoolean(parcel, 9, this.zzadw);
        SafeParcelWriter.writeBoolean(parcel, 10, this.zzael);
        SafeParcelWriter.writeLong(parcel, 11, this.zzads);
        SafeParcelWriter.writeString(parcel, 12, this.zzado, false);
        SafeParcelWriter.writeLong(parcel, 13, this.zzadx);
        SafeParcelWriter.writeLong(parcel, 14, this.zzaem);
        SafeParcelWriter.writeInt(parcel, 15, this.zzaen);
        SafeParcelWriter.writeBoolean(parcel, 16, this.zzady);
        SafeParcelWriter.writeBoolean(parcel, 17, this.zzadz);
        SafeParcelWriter.writeBoolean(parcel, 18, this.zzaeo);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
