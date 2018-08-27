package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzh extends AbstractSafeParcelable {
    public static final Creator<zzh> CREATOR = new zzi();
    public final String packageName;
    public final long zzadt;
    public final String zzafx;
    public final String zzafz;
    public final long zzagd;
    public final String zzage;
    public final long zzagf;
    public final boolean zzagg;
    public final long zzagh;
    public final boolean zzagi;
    public final boolean zzagj;
    public final String zzagk;
    public final String zzagv;
    public final boolean zzagw;
    public final long zzagx;
    public final int zzagy;
    public final boolean zzagz;
    public final String zzts;

    zzh(String str, String str2, String str3, long j, String str4, long j2, long j3, String str5, boolean z, boolean z2, String str6, long j4, long j5, int i, boolean z3, boolean z4, boolean z5, String str7) {
        Preconditions.checkNotEmpty(str);
        this.packageName = str;
        if (TextUtils.isEmpty(str2)) {
            str2 = null;
        }
        this.zzafx = str2;
        this.zzts = str3;
        this.zzagd = j;
        this.zzage = str4;
        this.zzadt = j2;
        this.zzagf = j3;
        this.zzagv = str5;
        this.zzagg = z;
        this.zzagw = z2;
        this.zzafz = str6;
        this.zzagh = j4;
        this.zzagx = j5;
        this.zzagy = i;
        this.zzagi = z3;
        this.zzagj = z4;
        this.zzagz = z5;
        this.zzagk = str7;
    }

    zzh(String str, String str2, String str3, String str4, long j, long j2, String str5, boolean z, boolean z2, long j3, String str6, long j4, long j5, int i, boolean z3, boolean z4, boolean z5, String str7) {
        this.packageName = str;
        this.zzafx = str2;
        this.zzts = str3;
        this.zzagd = j3;
        this.zzage = str4;
        this.zzadt = j;
        this.zzagf = j2;
        this.zzagv = str5;
        this.zzagg = z;
        this.zzagw = z2;
        this.zzafz = str6;
        this.zzagh = j4;
        this.zzagx = j5;
        this.zzagy = i;
        this.zzagi = z3;
        this.zzagj = z4;
        this.zzagz = z5;
        this.zzagk = str7;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzafx, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzts, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzage, false);
        SafeParcelWriter.writeLong(parcel, 6, this.zzadt);
        SafeParcelWriter.writeLong(parcel, 7, this.zzagf);
        SafeParcelWriter.writeString(parcel, 8, this.zzagv, false);
        SafeParcelWriter.writeBoolean(parcel, 9, this.zzagg);
        SafeParcelWriter.writeBoolean(parcel, 10, this.zzagw);
        SafeParcelWriter.writeLong(parcel, 11, this.zzagd);
        SafeParcelWriter.writeString(parcel, 12, this.zzafz, false);
        SafeParcelWriter.writeLong(parcel, 13, this.zzagh);
        SafeParcelWriter.writeLong(parcel, 14, this.zzagx);
        SafeParcelWriter.writeInt(parcel, 15, this.zzagy);
        SafeParcelWriter.writeBoolean(parcel, 16, this.zzagi);
        SafeParcelWriter.writeBoolean(parcel, 17, this.zzagj);
        SafeParcelWriter.writeBoolean(parcel, 18, this.zzagz);
        SafeParcelWriter.writeString(parcel, 19, this.zzagk, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
