package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class LineItem extends AbstractSafeParcelable {
    public static final Creator<LineItem> CREATOR = new zzt();
    String description;
    String zzan;
    String zzao;
    String zzca;
    String zzcb;
    int zzcc;

    public final class Builder {
        private final /* synthetic */ LineItem zzcd;

        private Builder(LineItem lineItem) {
            this.zzcd = lineItem;
        }

        public final LineItem build() {
            return this.zzcd;
        }

        public final Builder setCurrencyCode(String str) {
            this.zzcd.zzao = str;
            return this;
        }

        public final Builder setDescription(String str) {
            this.zzcd.description = str;
            return this;
        }

        public final Builder setQuantity(String str) {
            this.zzcd.zzca = str;
            return this;
        }

        public final Builder setTotalPrice(String str) {
            this.zzcd.zzan = str;
            return this;
        }

        public final Builder setUnitPrice(String str) {
            this.zzcd.zzcb = str;
            return this;
        }
    }

    LineItem() {
        this.zzcc = 0;
    }

    LineItem(String str, String str2, String str3, String str4, int i, String str5) {
        this.description = str;
        this.zzca = str2;
        this.zzcb = str3;
        this.zzan = str4;
        this.zzcc = i;
        this.zzao = str5;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.description, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzca, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzcb, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzan, false);
        SafeParcelWriter.writeInt(parcel, 6, this.zzcc);
        SafeParcelWriter.writeString(parcel, 7, this.zzao, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
