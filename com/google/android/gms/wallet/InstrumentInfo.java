package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class InstrumentInfo extends AbstractSafeParcelable {
    public static final Creator<InstrumentInfo> CREATOR = new zzp();
    private int zzag;
    private String zzbs;
    private String zzbt;

    private InstrumentInfo() {
    }

    public InstrumentInfo(String str, String str2, int i) {
        this.zzbs = str;
        this.zzbt = str2;
        this.zzag = i;
    }

    public final int getCardClass() {
        switch (this.zzag) {
            case 1:
            case 2:
            case 3:
                return this.zzag;
            default:
                return 0;
        }
    }

    public final String getInstrumentDetails() {
        return this.zzbt;
    }

    public final String getInstrumentType() {
        return this.zzbs;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, getInstrumentType(), false);
        SafeParcelWriter.writeString(parcel, 3, getInstrumentDetails(), false);
        SafeParcelWriter.writeInt(parcel, 4, getCardClass());
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
