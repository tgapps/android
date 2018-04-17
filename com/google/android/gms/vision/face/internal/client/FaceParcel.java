package com.google.android.gms.vision.face.internal.client;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.apps.common.proguard.UsedByNative;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

@UsedByNative("wrapper.cc")
public class FaceParcel extends AbstractSafeParcelable {
    public static final Creator<FaceParcel> CREATOR = new zzb();
    public final float centerX;
    public final float centerY;
    public final float height;
    public final int id;
    private final int versionCode;
    public final float width;
    public final float zzbx;
    public final float zzby;
    public final LandmarkParcel[] zzbz;
    public final float zzca;
    public final float zzcb;
    public final float zzcc;

    public FaceParcel(int i, int i2, float f, float f2, float f3, float f4, float f5, float f6, LandmarkParcel[] landmarkParcelArr, float f7, float f8, float f9) {
        this.versionCode = i;
        this.id = i2;
        this.centerX = f;
        this.centerY = f2;
        this.width = f3;
        this.height = f4;
        this.zzbx = f5;
        this.zzby = f6;
        this.zzbz = landmarkParcelArr;
        this.zzca = f7;
        this.zzcb = f8;
        this.zzcc = f9;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.versionCode);
        SafeParcelWriter.writeInt(parcel, 2, this.id);
        SafeParcelWriter.writeFloat(parcel, 3, this.centerX);
        SafeParcelWriter.writeFloat(parcel, 4, this.centerY);
        SafeParcelWriter.writeFloat(parcel, 5, this.width);
        SafeParcelWriter.writeFloat(parcel, 6, this.height);
        SafeParcelWriter.writeFloat(parcel, 7, this.zzbx);
        SafeParcelWriter.writeFloat(parcel, 8, this.zzby);
        SafeParcelWriter.writeTypedArray(parcel, 9, this.zzbz, i, false);
        SafeParcelWriter.writeFloat(parcel, 10, this.zzca);
        SafeParcelWriter.writeFloat(parcel, 11, this.zzcb);
        SafeParcelWriter.writeFloat(parcel, 12, this.zzcc);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
