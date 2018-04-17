package com.google.android.gms.wearable.internal;

import com.google.android.gms.common.data.DataBufferRef;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.wearable.DataItem;

public final class zzcy extends DataBufferRef {
    private final int zzdl;

    public zzcy(DataHolder dataHolder, int i, int i2) {
        super(dataHolder, i);
        this.zzdl = i2;
    }

    public final DataItem getDataItem() {
        return new zzdf(this.mDataHolder, this.mDataRow, this.zzdl);
    }

    public final int getType() {
        return getInteger("event_type");
    }

    public final String toString() {
        String str = getType() == 1 ? "changed" : getType() == 2 ? "deleted" : "unknown";
        String valueOf = String.valueOf(getDataItem());
        StringBuilder stringBuilder = new StringBuilder((32 + String.valueOf(str).length()) + String.valueOf(valueOf).length());
        stringBuilder.append("DataEventRef{ type=");
        stringBuilder.append(str);
        stringBuilder.append(", dataitem=");
        stringBuilder.append(valueOf);
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}
