package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.ArrayList;

public final class IsReadyToPayRequest extends AbstractSafeParcelable {
    public static final Creator<IsReadyToPayRequest> CREATOR = new zzr();
    ArrayList<Integer> zzai;
    private String zzbu;
    private String zzbv;
    ArrayList<Integer> zzbw;
    boolean zzbx;
    private String zzby;

    public final class Builder {
        private final /* synthetic */ IsReadyToPayRequest zzbz;

        private Builder(IsReadyToPayRequest isReadyToPayRequest) {
            this.zzbz = isReadyToPayRequest;
        }

        public final IsReadyToPayRequest build() {
            return this.zzbz;
        }
    }

    IsReadyToPayRequest() {
    }

    IsReadyToPayRequest(ArrayList<Integer> arrayList, String str, String str2, ArrayList<Integer> arrayList2, boolean z, String str3) {
        this.zzai = arrayList;
        this.zzbu = str;
        this.zzbv = str2;
        this.zzbw = arrayList2;
        this.zzbx = z;
        this.zzby = str3;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeIntegerList(parcel, 2, this.zzai, false);
        SafeParcelWriter.writeString(parcel, 4, this.zzbu, false);
        SafeParcelWriter.writeString(parcel, 5, this.zzbv, false);
        SafeParcelWriter.writeIntegerList(parcel, 6, this.zzbw, false);
        SafeParcelWriter.writeBoolean(parcel, 7, this.zzbx);
        SafeParcelWriter.writeString(parcel, 8, this.zzby, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
