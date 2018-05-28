package com.google.android.gms.vision.face.internal.client;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzc extends AbstractSafeParcelable {
    public static final Creator<zzc> CREATOR = new zzd();
    public int mode;
    public int zzby;
    public boolean zzbz;
    public int zzca;
    public boolean zzcb;
    public float zzcc;

    public zzc(int i, int i2, int i3, boolean z, boolean z2, float f) {
        this.mode = i;
        this.zzby = i2;
        this.zzca = i3;
        this.zzbz = z;
        this.zzcb = z2;
        this.zzcc = f;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.mode);
        SafeParcelWriter.writeInt(parcel, 3, this.zzby);
        SafeParcelWriter.writeInt(parcel, 4, this.zzca);
        SafeParcelWriter.writeBoolean(parcel, 5, this.zzbz);
        SafeParcelWriter.writeBoolean(parcel, 6, this.zzcb);
        SafeParcelWriter.writeFloat(parcel, 7, this.zzcc);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
