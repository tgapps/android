package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class LineItem extends AbstractSafeParcelable {
    public static final Creator<LineItem> CREATOR = new zzt();
    String description;
    String zzao;
    String zzap;
    String zzcb;
    String zzcc;
    int zzcd;

    public final class Builder {
        private final /* synthetic */ LineItem zzce;

        private Builder(LineItem lineItem) {
            this.zzce = lineItem;
        }

        public final Builder setDescription(String str) {
            this.zzce.description = str;
            return this;
        }

        public final Builder setQuantity(String str) {
            this.zzce.zzcb = str;
            return this;
        }

        public final Builder setUnitPrice(String str) {
            this.zzce.zzcc = str;
            return this;
        }

        public final Builder setTotalPrice(String str) {
            this.zzce.zzao = str;
            return this;
        }

        public final Builder setCurrencyCode(String str) {
            this.zzce.zzap = str;
            return this;
        }

        public final LineItem build() {
            return this.zzce;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.description, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzcb, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzcc, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzao, false);
        SafeParcelWriter.writeInt(parcel, 6, this.zzcd);
        SafeParcelWriter.writeString(parcel, 7, this.zzap, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    LineItem(String str, String str2, String str3, String str4, int i, String str5) {
        this.description = str;
        this.zzcb = str2;
        this.zzcc = str3;
        this.zzao = str4;
        this.zzcd = i;
        this.zzap = str5;
    }

    LineItem() {
        this.zzcd = 0;
    }
}
