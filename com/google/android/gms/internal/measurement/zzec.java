package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzec extends AbstractSafeParcelable {
    public static final Creator<zzec> CREATOR = new zzed();
    public final String packageName;
    public final String zzadh;
    public final String zzadj;
    public final long zzadn;
    public final String zzado;
    public final long zzadp;
    public final long zzadq;
    public final boolean zzadr;
    public final long zzads;
    public final boolean zzadt;
    public final boolean zzadu;
    public final String zzaef;
    public final boolean zzaeg;
    public final long zzaeh;
    public final int zzaei;
    public final boolean zzaej;
    public final String zztc;

    zzec(String str, String str2, String str3, long j, String str4, long j2, long j3, String str5, boolean z, boolean z2, String str6, long j4, long j5, int i, boolean z3, boolean z4, boolean z5) {
        Preconditions.checkNotEmpty(str);
        this.packageName = str;
        r0.zzadh = TextUtils.isEmpty(str2) ? null : str2;
        r0.zztc = str3;
        r0.zzadn = j;
        r0.zzado = str4;
        r0.zzadp = j2;
        r0.zzadq = j3;
        r0.zzaef = str5;
        r0.zzadr = z;
        r0.zzaeg = z2;
        r0.zzadj = str6;
        r0.zzads = j4;
        r0.zzaeh = j5;
        r0.zzaei = i;
        r0.zzadt = z3;
        r0.zzadu = z4;
        r0.zzaej = z5;
    }

    zzec(String str, String str2, String str3, String str4, long j, long j2, String str5, boolean z, boolean z2, long j3, String str6, long j4, long j5, int i, boolean z3, boolean z4, boolean z5) {
        this.packageName = str;
        this.zzadh = str2;
        this.zztc = str3;
        this.zzadn = j3;
        this.zzado = str4;
        this.zzadp = j;
        this.zzadq = j2;
        this.zzaef = str5;
        this.zzadr = z;
        this.zzaeg = z2;
        this.zzadj = str6;
        this.zzads = j4;
        this.zzaeh = j5;
        this.zzaei = i;
        this.zzadt = z3;
        this.zzadu = z4;
        this.zzaej = z5;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.packageName, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzadh, false);
        SafeParcelWriter.writeString(parcel, 4, this.zztc, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzado, false);
        SafeParcelWriter.writeLong(parcel, 6, this.zzadp);
        SafeParcelWriter.writeLong(parcel, 7, this.zzadq);
        SafeParcelWriter.writeString(parcel, 8, this.zzaef, false);
        SafeParcelWriter.writeBoolean(parcel, 9, this.zzadr);
        SafeParcelWriter.writeBoolean(parcel, 10, this.zzaeg);
        SafeParcelWriter.writeLong(parcel, 11, this.zzadn);
        SafeParcelWriter.writeString(parcel, 12, this.zzadj, false);
        SafeParcelWriter.writeLong(parcel, 13, this.zzads);
        SafeParcelWriter.writeLong(parcel, 14, this.zzaeh);
        SafeParcelWriter.writeInt(parcel, 15, this.zzaei);
        SafeParcelWriter.writeBoolean(parcel, 16, this.zzadt);
        SafeParcelWriter.writeBoolean(parcel, 17, this.zzadu);
        SafeParcelWriter.writeBoolean(parcel, 18, this.zzaej);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
