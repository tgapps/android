package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzfh extends AbstractSafeParcelable {
    public static final Creator<zzfh> CREATOR = new zzfi();
    public final String name;
    public final String origin;
    private final int versionCode;
    private final String zzamp;
    public final long zzaue;
    private final Long zzauf;
    private final Float zzaug;
    private final Double zzauh;

    zzfh(zzfj com_google_android_gms_measurement_internal_zzfj) {
        this(com_google_android_gms_measurement_internal_zzfj.name, com_google_android_gms_measurement_internal_zzfj.zzaue, com_google_android_gms_measurement_internal_zzfj.value, com_google_android_gms_measurement_internal_zzfj.origin);
    }

    zzfh(String str, long j, Object obj, String str2) {
        Preconditions.checkNotEmpty(str);
        this.versionCode = 2;
        this.name = str;
        this.zzaue = j;
        this.origin = str2;
        if (obj == null) {
            this.zzauf = null;
            this.zzaug = null;
            this.zzauh = null;
            this.zzamp = null;
        } else if (obj instanceof Long) {
            this.zzauf = (Long) obj;
            this.zzaug = null;
            this.zzauh = null;
            this.zzamp = null;
        } else if (obj instanceof String) {
            this.zzauf = null;
            this.zzaug = null;
            this.zzauh = null;
            this.zzamp = (String) obj;
        } else if (obj instanceof Double) {
            this.zzauf = null;
            this.zzaug = null;
            this.zzauh = (Double) obj;
            this.zzamp = null;
        } else {
            throw new IllegalArgumentException("User attribute given of un-supported type");
        }
    }

    zzfh(int i, String str, long j, Long l, Float f, String str2, String str3, Double d) {
        Double d2 = null;
        this.versionCode = i;
        this.name = str;
        this.zzaue = j;
        this.zzauf = l;
        this.zzaug = null;
        if (i == 1) {
            if (f != null) {
                d2 = Double.valueOf(f.doubleValue());
            }
            this.zzauh = d2;
        } else {
            this.zzauh = d;
        }
        this.zzamp = str2;
        this.origin = str3;
    }

    public final Object getValue() {
        if (this.zzauf != null) {
            return this.zzauf;
        }
        if (this.zzauh != null) {
            return this.zzauh;
        }
        if (this.zzamp != null) {
            return this.zzamp;
        }
        return null;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeString(parcel, 2, this.name, false);
        SafeParcelWriter.writeLong(parcel, 3, this.zzaue);
        SafeParcelWriter.writeLongObject(parcel, 4, this.zzauf, false);
        SafeParcelWriter.writeFloatObject(parcel, 5, null, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzamp, false);
        SafeParcelWriter.writeString(parcel, 7, this.origin, false);
        SafeParcelWriter.writeDoubleObject(parcel, 8, this.zzauh, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
