package com.google.android.gms.internal.measurement;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzjs extends AbstractSafeParcelable {
    public static final Creator<zzjs> CREATOR = new zzjt();
    public final String name;
    private final int versionCode;
    public final String zzaek;
    private final String zzajf;
    public final long zzaqu;
    private final Long zzaqv;
    private final Float zzaqw;
    private final Double zzaqx;

    zzjs(int i, String str, long j, Long l, Float f, String str2, String str3, Double d) {
        Double d2 = null;
        this.versionCode = i;
        this.name = str;
        this.zzaqu = j;
        this.zzaqv = l;
        this.zzaqw = null;
        if (i == 1) {
            if (f != null) {
                d2 = Double.valueOf(f.doubleValue());
            }
            this.zzaqx = d2;
        } else {
            this.zzaqx = d;
        }
        this.zzajf = str2;
        this.zzaek = str3;
    }

    zzjs(String str, long j, Object obj, String str2) {
        Preconditions.checkNotEmpty(str);
        this.versionCode = 2;
        this.name = str;
        this.zzaqu = j;
        this.zzaek = str2;
        if (obj == null) {
            this.zzaqv = null;
            this.zzaqw = null;
            this.zzaqx = null;
            this.zzajf = null;
        } else if (obj instanceof Long) {
            this.zzaqv = (Long) obj;
            this.zzaqw = null;
            this.zzaqx = null;
            this.zzajf = null;
        } else if (obj instanceof String) {
            this.zzaqv = null;
            this.zzaqw = null;
            this.zzaqx = null;
            this.zzajf = (String) obj;
        } else if (obj instanceof Double) {
            this.zzaqv = null;
            this.zzaqw = null;
            this.zzaqx = (Double) obj;
            this.zzajf = null;
        } else {
            throw new IllegalArgumentException("User attribute given of un-supported type");
        }
    }

    public final Object getValue() {
        return this.zzaqv != null ? this.zzaqv : this.zzaqx != null ? this.zzaqx : this.zzajf != null ? this.zzajf : null;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeLong(parcel, 3, this.zzaqu);
        SafeParcelWriter.writeLongObject(parcel, 4, this.zzaqv, false);
        SafeParcelWriter.writeFloatObject(parcel, 5, null, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzajf, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzaek, false);
        SafeParcelWriter.writeDoubleObject(parcel, 8, this.zzaqx, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
