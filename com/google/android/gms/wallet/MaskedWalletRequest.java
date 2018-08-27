package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.identity.intents.model.CountrySpecification;
import java.util.ArrayList;

public final class MaskedWalletRequest extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<MaskedWalletRequest> CREATOR = new zzz();
    ArrayList<Integer> zzaj;
    String zzap;
    String zzay;
    Cart zzbi;
    boolean zzde;
    boolean zzdf;
    boolean zzdg;
    String zzdh;
    String zzdi;
    private boolean zzdj;
    boolean zzdk;
    private CountrySpecification[] zzdl;
    boolean zzdm;
    boolean zzdn;
    ArrayList<CountrySpecification> zzdo;
    PaymentMethodTokenizationParameters zzdp;
    String zzi;

    public final class Builder {
        private final /* synthetic */ MaskedWalletRequest zzdq;

        private Builder(MaskedWalletRequest maskedWalletRequest) {
            this.zzdq = maskedWalletRequest;
        }

        public final Builder setEstimatedTotalPrice(String str) {
            this.zzdq.zzdh = str;
            return this;
        }

        public final Builder setCurrencyCode(String str) {
            this.zzdq.zzap = str;
            return this;
        }

        public final Builder setPaymentMethodTokenizationParameters(PaymentMethodTokenizationParameters paymentMethodTokenizationParameters) {
            this.zzdq.zzdp = paymentMethodTokenizationParameters;
            return this;
        }

        public final MaskedWalletRequest build() {
            return this.zzdq;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzay, false);
        SafeParcelWriter.writeBoolean(parcel, 3, this.zzde);
        SafeParcelWriter.writeBoolean(parcel, 4, this.zzdf);
        SafeParcelWriter.writeBoolean(parcel, 5, this.zzdg);
        SafeParcelWriter.writeString(parcel, 6, this.zzdh, false);
        SafeParcelWriter.writeString(parcel, 7, this.zzap, false);
        SafeParcelWriter.writeString(parcel, 8, this.zzdi, false);
        SafeParcelWriter.writeParcelable(parcel, 9, this.zzbi, i, false);
        SafeParcelWriter.writeBoolean(parcel, 10, this.zzdj);
        SafeParcelWriter.writeBoolean(parcel, 11, this.zzdk);
        SafeParcelWriter.writeTypedArray(parcel, 12, this.zzdl, i, false);
        SafeParcelWriter.writeBoolean(parcel, 13, this.zzdm);
        SafeParcelWriter.writeBoolean(parcel, 14, this.zzdn);
        SafeParcelWriter.writeTypedList(parcel, 15, this.zzdo, false);
        SafeParcelWriter.writeParcelable(parcel, 16, this.zzdp, i, false);
        SafeParcelWriter.writeIntegerList(parcel, 17, this.zzaj, false);
        SafeParcelWriter.writeString(parcel, 18, this.zzi, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    MaskedWalletRequest(String str, boolean z, boolean z2, boolean z3, String str2, String str3, String str4, Cart cart, boolean z4, boolean z5, CountrySpecification[] countrySpecificationArr, boolean z6, boolean z7, ArrayList<CountrySpecification> arrayList, PaymentMethodTokenizationParameters paymentMethodTokenizationParameters, ArrayList<Integer> arrayList2, String str5) {
        this.zzay = str;
        this.zzde = z;
        this.zzdf = z2;
        this.zzdg = z3;
        this.zzdh = str2;
        this.zzap = str3;
        this.zzdi = str4;
        this.zzbi = cart;
        this.zzdj = z4;
        this.zzdk = z5;
        this.zzdl = countrySpecificationArr;
        this.zzdm = z6;
        this.zzdn = z7;
        this.zzdo = arrayList;
        this.zzdp = paymentMethodTokenizationParameters;
        this.zzaj = arrayList2;
        this.zzi = str5;
    }

    MaskedWalletRequest() {
        this.zzdm = true;
        this.zzdn = true;
    }
}
