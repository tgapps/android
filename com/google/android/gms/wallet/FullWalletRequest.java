package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class FullWalletRequest extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<FullWalletRequest> CREATOR = new zzm();
    String zzaw;
    String zzax;
    Cart zzbh;

    public final class Builder {
        private final /* synthetic */ FullWalletRequest zzbi;

        private Builder(FullWalletRequest fullWalletRequest) {
            this.zzbi = fullWalletRequest;
        }

        public final FullWalletRequest build() {
            return this.zzbi;
        }

        public final Builder setCart(Cart cart) {
            this.zzbi.zzbh = cart;
            return this;
        }

        public final Builder setGoogleTransactionId(String str) {
            this.zzbi.zzaw = str;
            return this;
        }
    }

    FullWalletRequest() {
    }

    FullWalletRequest(String str, String str2, Cart cart) {
        this.zzaw = str;
        this.zzax = str2;
        this.zzbh = cart;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzaw, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzax, false);
        SafeParcelWriter.writeParcelable(parcel, 4, this.zzbh, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
