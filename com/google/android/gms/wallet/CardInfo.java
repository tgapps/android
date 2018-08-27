package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.identity.intents.model.UserAddress;

public final class CardInfo extends AbstractSafeParcelable {
    public static final Creator<CardInfo> CREATOR = new zzc();
    private String zzae;
    private String zzaf;
    private String zzag;
    private int zzah;
    private UserAddress zzai;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 1, this.zzae, false);
        SafeParcelWriter.writeString(parcel, 2, this.zzaf, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzag, false);
        SafeParcelWriter.writeInt(parcel, 4, this.zzah);
        SafeParcelWriter.writeParcelable(parcel, 5, this.zzai, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    CardInfo(String str, String str2, String str3, int i, UserAddress userAddress) {
        this.zzae = str;
        this.zzaf = str2;
        this.zzag = str3;
        this.zzah = i;
        this.zzai = userAddress;
    }

    private CardInfo() {
    }
}
