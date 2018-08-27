package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class LoyaltyPointsBalance extends AbstractSafeParcelable {
    public static final Creator<LoyaltyPointsBalance> CREATOR = new zzh();
    String zzbo;
    int zzgv;
    String zzgw;
    double zzgx;
    long zzgy;
    int zzgz;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.zzgv);
        SafeParcelWriter.writeString(parcel, 3, this.zzgw, false);
        SafeParcelWriter.writeDouble(parcel, 4, this.zzgx);
        SafeParcelWriter.writeString(parcel, 5, this.zzbo, false);
        SafeParcelWriter.writeLong(parcel, 6, this.zzgy);
        SafeParcelWriter.writeInt(parcel, 7, this.zzgz);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    LoyaltyPointsBalance(int i, String str, double d, String str2, long j, int i2) {
        this.zzgv = i;
        this.zzgw = str;
        this.zzgx = d;
        this.zzbo = str2;
        this.zzgy = j;
        this.zzgz = i2;
    }

    LoyaltyPointsBalance() {
        this.zzgz = -1;
        this.zzgv = -1;
        this.zzgx = -1.0d;
    }
}
