package com.google.android.gms.internal.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.widget.RemoteViews;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class zzl extends AbstractSafeParcelable {
    public static final Creator<zzl> CREATOR = new zzm();
    private String[] zzez;
    private int[] zzfa;
    private RemoteViews zzfb;
    private byte[] zzfc;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeStringArray(parcel, 1, this.zzez, false);
        SafeParcelWriter.writeIntArray(parcel, 2, this.zzfa, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzfb, i, false);
        SafeParcelWriter.writeByteArray(parcel, 4, this.zzfc, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public zzl(String[] strArr, int[] iArr, RemoteViews remoteViews, byte[] bArr) {
        this.zzez = strArr;
        this.zzfa = iArr;
        this.zzfb = remoteViews;
        this.zzfc = bArr;
    }

    private zzl() {
    }
}
