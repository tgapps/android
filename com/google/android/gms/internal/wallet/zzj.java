package com.google.android.gms.internal.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.widget.RemoteViews;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzj extends AbstractSafeParcelable {
    public static final Creator<zzj> CREATOR = new zzk();
    private String[] zzex;
    private int[] zzey;
    private RemoteViews zzez;
    private byte[] zzfa;

    private zzj() {
    }

    public zzj(String[] strArr, int[] iArr, RemoteViews remoteViews, byte[] bArr) {
        this.zzex = strArr;
        this.zzey = iArr;
        this.zzez = remoteViews;
        this.zzfa = bArr;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeStringArray(parcel, 1, this.zzex, false);
        SafeParcelWriter.writeIntArray(parcel, 2, this.zzey, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzez, i, false);
        SafeParcelWriter.writeByteArray(parcel, 4, this.zzfa, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
