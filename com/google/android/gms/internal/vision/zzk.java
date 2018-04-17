package com.google.android.gms.internal.vision;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.vision.Frame;

public final class zzk extends AbstractSafeParcelable {
    public static final Creator<zzk> CREATOR = new zzl();
    public int height;
    private int id;
    public int rotation;
    public int width;
    private long zzck;

    public zzk(int i, int i2, int i3, long j, int i4) {
        this.width = i;
        this.height = i2;
        this.id = i3;
        this.zzck = j;
        this.rotation = i4;
    }

    public static zzk zzc(Frame frame) {
        zzk com_google_android_gms_internal_vision_zzk = new zzk();
        com_google_android_gms_internal_vision_zzk.width = frame.getMetadata().getWidth();
        com_google_android_gms_internal_vision_zzk.height = frame.getMetadata().getHeight();
        com_google_android_gms_internal_vision_zzk.rotation = frame.getMetadata().getRotation();
        com_google_android_gms_internal_vision_zzk.id = frame.getMetadata().getId();
        com_google_android_gms_internal_vision_zzk.zzck = frame.getMetadata().getTimestampMillis();
        return com_google_android_gms_internal_vision_zzk;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.width);
        SafeParcelWriter.writeInt(parcel, 3, this.height);
        SafeParcelWriter.writeInt(parcel, 4, this.id);
        SafeParcelWriter.writeLong(parcel, 5, this.zzck);
        SafeParcelWriter.writeInt(parcel, 6, this.rotation);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
