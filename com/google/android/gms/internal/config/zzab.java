package com.google.android.gms.internal.config;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.List;

public final class zzab extends AbstractSafeParcelable {
    public static final Creator<zzab> CREATOR = new zzac();
    private final String mPackageName;
    private final List<zzl> zzaa;
    private final int zzh;
    private final int zzi;
    private final int zzj;
    private final long zzu;
    private final DataHolder zzv;
    private final String zzw;
    private final String zzx;
    private final String zzy;
    private final List<String> zzz;

    public zzab(String str, long j, DataHolder dataHolder, String str2, String str3, String str4, List<String> list, int i, List<zzl> list2, int i2, int i3) {
        this.mPackageName = str;
        this.zzu = j;
        this.zzv = dataHolder;
        this.zzw = str2;
        this.zzx = str3;
        this.zzy = str4;
        this.zzz = list;
        this.zzh = i;
        this.zzaa = list2;
        this.zzj = i2;
        this.zzi = i3;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.mPackageName, false);
        SafeParcelWriter.writeLong(parcel, 3, this.zzu);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzv, i, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzw, false);
        SafeParcelWriter.writeString(parcel, 6, this.zzx, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzy, false);
        SafeParcelWriter.writeStringList(parcel, 8, this.zzz, false);
        SafeParcelWriter.writeInt(parcel, 9, this.zzh);
        SafeParcelWriter.writeTypedList(parcel, 10, this.zzaa, false);
        SafeParcelWriter.writeInt(parcel, 11, this.zzj);
        SafeParcelWriter.writeInt(parcel, 12, this.zzi);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
