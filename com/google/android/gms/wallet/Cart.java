package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.ArrayList;

public final class Cart extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<Cart> CREATOR = new zzg();
    String zzao;
    String zzap;
    ArrayList<LineItem> zzaq;

    public final class Builder {
        private final /* synthetic */ Cart zzar;

        private Builder(Cart cart) {
            this.zzar = cart;
        }

        public final Builder setTotalPrice(String str) {
            this.zzar.zzao = str;
            return this;
        }

        public final Builder setCurrencyCode(String str) {
            this.zzar.zzap = str;
            return this;
        }

        public final Builder addLineItem(LineItem lineItem) {
            this.zzar.zzaq.add(lineItem);
            return this;
        }

        public final Cart build() {
            return this.zzar;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzao, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzap, false);
        SafeParcelWriter.writeTypedList(parcel, 4, this.zzaq, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    Cart(String str, String str2, ArrayList<LineItem> arrayList) {
        this.zzao = str;
        this.zzap = str2;
        this.zzaq = arrayList;
    }

    Cart() {
        this.zzaq = new ArrayList();
    }
}
