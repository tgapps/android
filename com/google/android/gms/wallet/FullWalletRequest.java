package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class FullWalletRequest extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<FullWalletRequest> CREATOR = new zzm();
    String zzax;
    String zzay;
    Cart zzbi;

    public final class Builder {
        private final /* synthetic */ FullWalletRequest zzbj;

        private Builder(FullWalletRequest fullWalletRequest) {
            this.zzbj = fullWalletRequest;
        }

        public final Builder setGoogleTransactionId(String str) {
            this.zzbj.zzax = str;
            return this;
        }

        public final Builder setCart(Cart cart) {
            this.zzbj.zzbi = cart;
            return this;
        }

        public final FullWalletRequest build() {
            return this.zzbj;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzax, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzay, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzbi, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    FullWalletRequest(String str, String str2, Cart cart) {
        this.zzax = str;
        this.zzay = str2;
        this.zzbi = cart;
    }

    FullWalletRequest() {
    }
}
