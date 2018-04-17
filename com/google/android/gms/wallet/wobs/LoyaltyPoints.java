package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class LoyaltyPoints extends AbstractSafeParcelable {
    public static final Creator<LoyaltyPoints> CREATOR = new zzi();
    String label;
    TimeInterval zzcp;
    LoyaltyPointsBalance zzgr;

    LoyaltyPoints() {
    }

    LoyaltyPoints(String str, LoyaltyPointsBalance loyaltyPointsBalance, TimeInterval timeInterval) {
        this.label = str;
        this.zzgr = loyaltyPointsBalance;
        this.zzcp = timeInterval;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.label, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zzgr, i, false);
        SafeParcelWriter.writeParcelable(parcel, 5, this.zzcp, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
