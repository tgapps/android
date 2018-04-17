package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.ArrayList;

public final class Cart extends AbstractSafeParcelable implements ReflectedParcelable {
    public static final Creator<Cart> CREATOR = new zzg();
    String zzan;
    String zzao;
    ArrayList<LineItem> zzap;

    public final class Builder {
        private final /* synthetic */ Cart zzaq;

        private Builder(Cart cart) {
            this.zzaq = cart;
        }

        public final Builder addLineItem(LineItem lineItem) {
            this.zzaq.zzap.add(lineItem);
            return this;
        }

        public final Cart build() {
            return this.zzaq;
        }

        public final Builder setCurrencyCode(String str) {
            this.zzaq.zzao = str;
            return this;
        }

        public final Builder setTotalPrice(String str) {
            this.zzaq.zzan = str;
            return this;
        }
    }

    Cart() {
        this.zzap = new ArrayList();
    }

    Cart(String str, String str2, ArrayList<LineItem> arrayList) {
        this.zzan = str;
        this.zzao = str2;
        this.zzap = arrayList;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzan, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzao, false);
        SafeParcelWriter.writeTypedList(parcel, 4, this.zzap, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
