package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public final class InstrumentInfo extends AbstractSafeParcelable {
    public static final Creator<InstrumentInfo> CREATOR = new zzp();
    private int zzah;
    private String zzbt;
    private String zzbu;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, getInstrumentType(), false);
        SafeParcelWriter.writeString(parcel, 3, getInstrumentDetails(), false);
        SafeParcelWriter.writeInt(parcel, 4, getCardClass());
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    public InstrumentInfo(String str, String str2, int i) {
        this.zzbt = str;
        this.zzbu = str2;
        this.zzah = i;
    }

    private InstrumentInfo() {
    }

    public final String getInstrumentType() {
        return this.zzbt;
    }

    public final String getInstrumentDetails() {
        return this.zzbu;
    }

    public final int getCardClass() {
        switch (this.zzah) {
            case 1:
            case 2:
            case 3:
                return this.zzah;
            default:
                return 0;
        }
    }
}
