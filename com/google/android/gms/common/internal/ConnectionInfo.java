package com.google.android.gms.common.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;

public class ConnectionInfo extends AbstractSafeParcelable {
    public static final Creator<ConnectionInfo> CREATOR = new ConnectionInfoCreator();
    private Bundle zzsf;
    private Feature[] zzsg;

    ConnectionInfo(Bundle bundle, Feature[] featureArr) {
        this.zzsf = bundle;
        this.zzsg = featureArr;
    }

    public Feature[] getAvailableFeatures() {
        return this.zzsg;
    }

    public Bundle getResolutionBundle() {
        return this.zzsf;
    }

    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 1, this.zzsf, false);
        SafeParcelWriter.writeTypedArray(parcel, 2, this.zzsg, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
