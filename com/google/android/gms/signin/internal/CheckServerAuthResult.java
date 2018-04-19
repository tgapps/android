package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.List;

public class CheckServerAuthResult extends AbstractSafeParcelable {
    public static final Creator<CheckServerAuthResult> CREATOR = new CheckServerAuthResultCreator();
    private final boolean zzadp;
    private final List<Scope> zzadq;
    private final int zzal;

    CheckServerAuthResult(int i, boolean z, List<Scope> list) {
        this.zzal = i;
        this.zzadp = z;
        this.zzadq = list;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zzal);
        SafeParcelWriter.writeBoolean(parcel, 2, this.zzadp);
        SafeParcelWriter.writeTypedList(parcel, 3, this.zzadq, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
