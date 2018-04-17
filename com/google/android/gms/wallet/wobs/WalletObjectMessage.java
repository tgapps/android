package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class WalletObjectMessage extends AbstractSafeParcelable {
    public static final Creator<WalletObjectMessage> CREATOR = new zzn();
    String zzgz;
    String zzha;
    TimeInterval zzhe;
    UriData zzhf;
    UriData zzhg;

    WalletObjectMessage() {
    }

    WalletObjectMessage(String str, String str2, TimeInterval timeInterval, UriData uriData, UriData uriData2) {
        this.zzgz = str;
        this.zzha = str2;
        this.zzhe = timeInterval;
        this.zzhf = uriData;
        this.zzhg = uriData2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzgz, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzha, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzhe, i, false);
        SafeParcelWriter.writeParcelable(parcel, 5, this.zzhf, i, false);
        SafeParcelWriter.writeParcelable(parcel, 6, this.zzhg, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
