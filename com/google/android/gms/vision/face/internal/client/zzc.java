package com.google.android.gms.vision.face.internal.client;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzc extends AbstractSafeParcelable {
    public static final Creator<zzc> CREATOR = new zzd();
    public int mode;
    public int zzcd;
    public int zzce;
    public boolean zzcf;
    public boolean zzcg;
    public float zzch;

    public zzc(int i, int i2, int i3, boolean z, boolean z2, float f) {
        this.mode = i;
        this.zzcd = i2;
        this.zzce = i3;
        this.zzcf = z;
        this.zzcg = z2;
        this.zzch = f;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.mode);
        SafeParcelWriter.writeInt(parcel, 3, this.zzcd);
        SafeParcelWriter.writeInt(parcel, 4, this.zzce);
        SafeParcelWriter.writeBoolean(parcel, 5, this.zzcf);
        SafeParcelWriter.writeBoolean(parcel, 6, this.zzcg);
        SafeParcelWriter.writeFloat(parcel, 7, this.zzch);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
