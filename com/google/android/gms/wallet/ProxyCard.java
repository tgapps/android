package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

@Deprecated
public final class ProxyCard extends AbstractSafeParcelable {
    public static final Creator<ProxyCard> CREATOR = new zzal();
    private String zzej;
    private String zzek;
    private int zzel;
    private int zzem;

    public ProxyCard(String str, String str2, int i, int i2) {
        this.zzej = str;
        this.zzek = str2;
        this.zzel = i;
        this.zzem = i2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzej, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzek, false);
        SafeParcelWriter.writeInt(parcel, 4, this.zzel);
        SafeParcelWriter.writeInt(parcel, 5, this.zzem);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
