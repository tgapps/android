package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class WalletObjectMessage extends AbstractSafeParcelable {
    public static final Creator<WalletObjectMessage> CREATOR = new zzn();
    String zzhb;
    String zzhc;
    TimeInterval zzhg;
    @Deprecated
    UriData zzhh;
    @Deprecated
    UriData zzhi;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzhb, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzhc, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzhg, i, false);
        SafeParcelWriter.writeParcelable(parcel, 5, this.zzhh, i, false);
        SafeParcelWriter.writeParcelable(parcel, 6, this.zzhi, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    WalletObjectMessage(String str, String str2, TimeInterval timeInterval, UriData uriData, UriData uriData2) {
        this.zzhb = str;
        this.zzhc = str2;
        this.zzhg = timeInterval;
        this.zzhh = uriData;
        this.zzhi = uriData2;
    }

    WalletObjectMessage() {
    }
}
