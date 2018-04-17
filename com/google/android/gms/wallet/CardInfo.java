package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.identity.intents.model.UserAddress;

public final class CardInfo extends AbstractSafeParcelable {
    public static final Creator<CardInfo> CREATOR = new zzc();
    private String zzad;
    private String zzae;
    private String zzaf;
    private int zzag;
    private UserAddress zzah;

    private CardInfo() {
    }

    CardInfo(String str, String str2, String str3, int i, UserAddress userAddress) {
        this.zzad = str;
        this.zzae = str2;
        this.zzaf = str3;
        this.zzag = i;
        this.zzah = userAddress;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, this.zzad, false);
        SafeParcelWriter.writeString(parcel, 2, this.zzae, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzaf, false);
        SafeParcelWriter.writeInt(parcel, 4, this.zzag);
        SafeParcelWriter.writeParcelable(parcel, 5, this.zzah, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
