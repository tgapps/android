package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.util.ArrayUtils;
import java.util.ArrayList;

public final class LabelValueRow extends AbstractSafeParcelable {
    public static final Creator<LabelValueRow> CREATOR = new zze();
    @Deprecated
    String zzgp;
    @Deprecated
    String zzgq;
    ArrayList<LabelValue> zzgr;

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzgp, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzgq, false);
        SafeParcelWriter.writeTypedList(parcel, 4, this.zzgr, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }

    LabelValueRow(String str, String str2, ArrayList<LabelValue> arrayList) {
        this.zzgp = str;
        this.zzgq = str2;
        this.zzgr = arrayList;
    }

    LabelValueRow() {
        this.zzgr = ArrayUtils.newArrayList();
    }
}
