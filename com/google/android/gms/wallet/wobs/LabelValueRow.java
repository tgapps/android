package com.google.android.gms.wallet.wobs;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.util.ArrayUtils;
import java.util.ArrayList;

public final class LabelValueRow extends AbstractSafeParcelable {
    public static final Creator<LabelValueRow> CREATOR = new zze();
    String zzgn;
    String zzgo;
    ArrayList<LabelValue> zzgp;

    LabelValueRow() {
        this.zzgp = ArrayUtils.newArrayList();
    }

    LabelValueRow(String str, String str2, ArrayList<LabelValue> arrayList) {
        this.zzgn = str;
        this.zzgo = str2;
        this.zzgp = arrayList;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzgn, false);
        SafeParcelWriter.writeString(parcel, 3, this.zzgo, false);
        SafeParcelWriter.writeTypedList(parcel, 4, this.zzgp, false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
