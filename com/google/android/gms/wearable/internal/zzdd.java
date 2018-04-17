package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class zzdd extends AbstractSafeParcelable implements DataItem {
    public static final Creator<zzdd> CREATOR = new zzde();
    private byte[] data;
    private final Uri uri;
    private final Map<String, DataItemAsset> zzdo;

    zzdd(Uri uri, Bundle bundle, byte[] bArr) {
        this.uri = uri;
        Map hashMap = new HashMap();
        bundle.setClassLoader(DataItemAssetParcelable.class.getClassLoader());
        for (String str : bundle.keySet()) {
            hashMap.put(str, (DataItemAssetParcelable) bundle.getParcelable(str));
        }
        this.zzdo = hashMap;
        this.data = bArr;
    }

    public final byte[] getData() {
        return this.data;
    }

    public final Uri getUri() {
        return this.uri;
    }

    public final String toString() {
        String str;
        boolean isLoggable = Log.isLoggable("DataItem", 3);
        StringBuilder stringBuilder = new StringBuilder("DataItemParcelable[");
        stringBuilder.append("@");
        stringBuilder.append(Integer.toHexString(hashCode()));
        String valueOf = String.valueOf(this.data == null ? "null" : Integer.valueOf(this.data.length));
        StringBuilder stringBuilder2 = new StringBuilder(8 + String.valueOf(valueOf).length());
        stringBuilder2.append(",dataSz=");
        stringBuilder2.append(valueOf);
        stringBuilder.append(stringBuilder2.toString());
        int size = this.zzdo.size();
        stringBuilder2 = new StringBuilder(23);
        stringBuilder2.append(", numAssets=");
        stringBuilder2.append(size);
        stringBuilder.append(stringBuilder2.toString());
        valueOf = String.valueOf(this.uri);
        stringBuilder2 = new StringBuilder(6 + String.valueOf(valueOf).length());
        stringBuilder2.append(", uri=");
        stringBuilder2.append(valueOf);
        stringBuilder.append(stringBuilder2.toString());
        if (isLoggable) {
            stringBuilder.append("]\n  assets: ");
            for (String valueOf2 : this.zzdo.keySet()) {
                String valueOf3 = String.valueOf(this.zzdo.get(valueOf2));
                StringBuilder stringBuilder3 = new StringBuilder((7 + String.valueOf(valueOf2).length()) + String.valueOf(valueOf3).length());
                stringBuilder3.append("\n    ");
                stringBuilder3.append(valueOf2);
                stringBuilder3.append(": ");
                stringBuilder3.append(valueOf3);
                stringBuilder.append(stringBuilder3.toString());
            }
            str = "\n  ]";
        } else {
            str = "]";
        }
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeParcelable(parcel, 2, getUri(), i, false);
        Bundle bundle = new Bundle();
        bundle.setClassLoader(DataItemAssetParcelable.class.getClassLoader());
        for (Entry entry : this.zzdo.entrySet()) {
            bundle.putParcelable((String) entry.getKey(), new DataItemAssetParcelable((DataItemAsset) entry.getValue()));
        }
        SafeParcelWriter.writeBundle(parcel, 4, bundle, false);
        SafeParcelWriter.writeByteArray(parcel, 5, getData(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
