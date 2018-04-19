package com.google.android.gms.wallet;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class PaymentMethodTokenizationParameters extends AbstractSafeParcelable {
    public static final Creator<PaymentMethodTokenizationParameters> CREATOR = new zzah();
    int zzeb;
    Bundle zzed = new Bundle();

    public final class Builder {
        private final /* synthetic */ PaymentMethodTokenizationParameters zzee;

        private Builder(PaymentMethodTokenizationParameters paymentMethodTokenizationParameters) {
            this.zzee = paymentMethodTokenizationParameters;
        }

        public final Builder addParameter(String str, String str2) {
            Preconditions.checkNotEmpty(str, "Tokenization parameter name must not be empty");
            Preconditions.checkNotEmpty(str2, "Tokenization parameter value must not be empty");
            this.zzee.zzed.putString(str, str2);
            return this;
        }

        public final PaymentMethodTokenizationParameters build() {
            return this.zzee;
        }

        public final Builder setPaymentMethodTokenizationType(int i) {
            this.zzee.zzeb = i;
            return this;
        }
    }

    private PaymentMethodTokenizationParameters() {
    }

    PaymentMethodTokenizationParameters(int i, Bundle bundle) {
        this.zzeb = i;
        this.zzed = bundle;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, this.zzeb);
        SafeParcelWriter.writeBundle(parcel, 3, this.zzed, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
