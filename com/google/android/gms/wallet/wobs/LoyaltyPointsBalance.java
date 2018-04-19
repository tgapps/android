package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class LoyaltyPointsBalance extends AbstractSafeParcelable {
    public static final Creator<LoyaltyPointsBalance> CREATOR = new zzh();
    String zzbn;
    int zzgt;
    String zzgu;
    double zzgv;
    long zzgw;
    int zzgx;

    LoyaltyPointsBalance() {
        this.zzgx = -1;
        this.zzgt = -1;
        this.zzgv = -1.0d;
    }

    LoyaltyPointsBalance(int i, String str, double d, String str2, long j, int i2) {
        this.zzgt = i;
        this.zzgu = str;
        this.zzgv = d;
        this.zzbn = str2;
        this.zzgw = j;
        this.zzgx = i2;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.zzgt);
        SafeParcelWriter.writeString(parcel, 3, this.zzgu, false);
        SafeParcelWriter.writeDouble(parcel, 4, this.zzgv);
        SafeParcelWriter.writeString(parcel, 5, this.zzbn, false);
        SafeParcelWriter.writeLong(parcel, 6, this.zzgw);
        SafeParcelWriter.writeInt(parcel, 7, this.zzgx);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
