package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzjz extends AbstractSafeParcelable {
    public static final Creator<zzjz> CREATOR = new zzka();
    public final String name;
    public final String origin;
    private final int versionCode;
    private final String zzajo;
    public final long zzarl;
    private final Long zzarm;
    private final Float zzarn;
    private final Double zzaro;

    zzjz(int i, String str, long j, Long l, Float f, String str2, String str3, Double d) {
        Double d2 = null;
        this.versionCode = i;
        this.name = str;
        this.zzarl = j;
        this.zzarm = l;
        this.zzarn = null;
        if (i == 1) {
            if (f != null) {
                d2 = Double.valueOf(f.doubleValue());
            }
            this.zzaro = d2;
        } else {
            this.zzaro = d;
        }
        this.zzajo = str2;
        this.origin = str3;
    }

    zzjz(String str, long j, Object obj, String str2) {
        Preconditions.checkNotEmpty(str);
        this.versionCode = 2;
        this.name = str;
        this.zzarl = j;
        this.origin = str2;
        if (obj == null) {
            this.zzarm = null;
            this.zzarn = null;
            this.zzaro = null;
            this.zzajo = null;
        } else if (obj instanceof Long) {
            this.zzarm = (Long) obj;
            this.zzarn = null;
            this.zzaro = null;
            this.zzajo = null;
        } else if (obj instanceof String) {
            this.zzarm = null;
            this.zzarn = null;
            this.zzaro = null;
            this.zzajo = (String) obj;
        } else if (obj instanceof Double) {
            this.zzarm = null;
            this.zzarn = null;
            this.zzaro = (Double) obj;
            this.zzajo = null;
        } else {
            throw new IllegalArgumentException("User attribute given of un-supported type");
        }
    }

    public final Object getValue() {
        return this.zzarm != null ? this.zzarm : this.zzaro != null ? this.zzaro : this.zzajo != null ? this.zzajo : null;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeLong(parcel, 3, this.zzarl);
        SafeParcelWriter.writeLongObject(parcel, 4, this.zzarm, false);
        SafeParcelWriter.writeFloatObject(parcel, 5, null, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzajo, false);
        SafeParcelWriter.writeString(parcel, 7, this.origin, false);
        SafeParcelWriter.writeDoubleObject(parcel, 8, this.zzaro, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
