package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.wearable.MessageEvent;

public final class zzfe extends AbstractSafeParcelable implements MessageEvent {
    public static final Creator<zzfe> CREATOR = new zzff();
    private final byte[] data;
    private final String zzcl;
    private final int zzeh;
    private final String zzek;

    public zzfe(int i, String str, byte[] bArr, String str2) {
        this.zzeh = i;
        this.zzcl = str;
        this.data = bArr;
        this.zzek = str2;
    }

    public final byte[] getData() {
        return this.data;
    }

    public final String getPath() {
        return this.zzcl;
    }

    public final int getRequestId() {
        return this.zzeh;
    }

    public final String getSourceNodeId() {
        return this.zzek;
    }

    public final String toString() {
        int i = this.zzeh;
        String str = this.zzcl;
        String valueOf = String.valueOf(this.data == null ? "null" : Integer.valueOf(this.data.length));
        StringBuilder stringBuilder = new StringBuilder((43 + String.valueOf(str).length()) + String.valueOf(valueOf).length());
        stringBuilder.append("MessageEventParcelable[");
        stringBuilder.append(i);
        stringBuilder.append(",");
        stringBuilder.append(str);
        stringBuilder.append(", size=");
        stringBuilder.append(valueOf);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 2, getRequestId());
        SafeParcelWriter.writeString(parcel, 3, getPath(), false);
        SafeParcelWriter.writeByteArray(parcel, 4, getData(), false);
        SafeParcelWriter.writeString(parcel, 5, getSourceNodeId(), false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
