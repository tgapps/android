package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

@Deprecated
public final class ProxyCard extends AbstractSafeParcelable {
    public static final Creator<ProxyCard> CREATOR = new zzak();
    private String zzeh;
    private String zzei;
    private int zzej;
    private int zzek;

    public ProxyCard(String str, String str2, int i, int i2) {
        this.zzeh = str;
        this.zzei = str2;
        this.zzej = i;
        this.zzek = i2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzeh, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzei, false);
        SafeParcelWriter.writeInt(parcel, 4, this.zzej);
        SafeParcelWriter.writeInt(parcel, 5, this.zzek);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
