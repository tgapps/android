package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class TextModuleData extends AbstractSafeParcelable {
    public static final Creator<TextModuleData> CREATOR = new zzj();
    private String zzhb;
    private String zzhc;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzhb, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzhc, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public TextModuleData(String str, String str2) {
        this.zzhb = str;
        this.zzhc = str2;
    }

    TextModuleData() {
    }
}
