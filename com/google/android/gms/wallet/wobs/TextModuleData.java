package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class TextModuleData extends AbstractSafeParcelable {
    public static final Creator<TextModuleData> CREATOR = new zzj();
    private String zzgz;
    private String zzha;

    TextModuleData() {
    }

    public TextModuleData(String str, String str2) {
        this.zzgz = str;
        this.zzha = str2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzgz, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzha, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
