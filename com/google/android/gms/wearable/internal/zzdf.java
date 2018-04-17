package com.google.android.gms.wearable.internal;

import android.net.Uri;
import android.util.Log;
import com.google.android.gms.common.data.DataBufferRef;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemAsset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.telegram.messenger.exoplayer2.upstream.DataSchemeDataSource;

public final class zzdf extends DataBufferRef implements DataItem {
    private final int zzdl;

    public zzdf(DataHolder dataHolder, int i, int i2) {
        super(dataHolder, i);
        this.zzdl = i2;
    }

    public final Map<String, DataItemAsset> getAssets() {
        Map<String, DataItemAsset> hashMap = new HashMap(this.zzdl);
        for (int i = 0; i < this.zzdl; i++) {
            zzdb com_google_android_gms_wearable_internal_zzdb = new zzdb(this.mDataHolder, this.mDataRow + i);
            if (com_google_android_gms_wearable_internal_zzdb.getDataItemKey() != null) {
                hashMap.put(com_google_android_gms_wearable_internal_zzdb.getDataItemKey(), com_google_android_gms_wearable_internal_zzdb);
            }
        }
        return hashMap;
    }

    public final byte[] getData() {
        return getByteArray(DataSchemeDataSource.SCHEME_DATA);
    }

    public final Uri getUri() {
        return Uri.parse(getString("path"));
    }

    public final String toString() {
        boolean isLoggable = Log.isLoggable("DataItem", 3);
        byte[] data = getData();
        Map assets = getAssets();
        StringBuilder stringBuilder = new StringBuilder("DataItemRef{ ");
        String valueOf = String.valueOf(getUri());
        StringBuilder stringBuilder2 = new StringBuilder(4 + String.valueOf(valueOf).length());
        stringBuilder2.append("uri=");
        stringBuilder2.append(valueOf);
        stringBuilder.append(stringBuilder2.toString());
        String valueOf2 = String.valueOf(data == null ? "null" : Integer.valueOf(data.length));
        stringBuilder2 = new StringBuilder(9 + String.valueOf(valueOf2).length());
        stringBuilder2.append(", dataSz=");
        stringBuilder2.append(valueOf2);
        stringBuilder.append(stringBuilder2.toString());
        int size = assets.size();
        stringBuilder2 = new StringBuilder(23);
        stringBuilder2.append(", numAssets=");
        stringBuilder2.append(size);
        stringBuilder.append(stringBuilder2.toString());
        if (isLoggable && !assets.isEmpty()) {
            stringBuilder.append(", assets=[");
            String str = TtmlNode.ANONYMOUS_REGION_ID;
            for (Entry entry : assets.entrySet()) {
                valueOf = (String) entry.getKey();
                String id = ((DataItemAsset) entry.getValue()).getId();
                StringBuilder stringBuilder3 = new StringBuilder(((2 + String.valueOf(str).length()) + String.valueOf(valueOf).length()) + String.valueOf(id).length());
                stringBuilder3.append(str);
                stringBuilder3.append(valueOf);
                stringBuilder3.append(": ");
                stringBuilder3.append(id);
                stringBuilder.append(stringBuilder3.toString());
                str = ", ";
            }
            stringBuilder.append("]");
        }
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}
