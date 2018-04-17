package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.wallet.wobs.CommonWalletObject;

public final class OfferWalletObject extends AbstractSafeParcelable {
    public static final Creator<OfferWalletObject> CREATOR = new zzab();
    private final int versionCode;
    CommonWalletObject zzbj;
    String zzce;
    String zzdq;

    OfferWalletObject() {
        this.versionCode = 3;
    }

    OfferWalletObject(int i, String str, String str2, CommonWalletObject commonWalletObject) {
        this.versionCode = i;
        this.zzdq = str2;
        if (i < 3) {
            this.zzbj = CommonWalletObject.zze().zza(str).zzf();
        } else {
            this.zzbj = commonWalletObject;
        }
    }

    public final int getVersionCode() {
        return this.versionCode;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, getVersionCode());
        SafeParcelWriter.writeString(parcel, 2, this.zzce, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzdq, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzbj, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
