package com.google.android.gms.wearable.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.wearable.Channel;
import com.google.android.gms.wearable.Channel.GetInputStreamResult;
import com.google.android.gms.wearable.Channel.GetOutputStreamResult;
import com.google.android.gms.wearable.ChannelClient;

public final class zzay extends AbstractSafeParcelable implements Channel, ChannelClient.Channel {
    public static final Creator<zzay> CREATOR = new zzbi();
    private final String zzce;
    private final String zzcl;
    private final String zzo;

    public zzay(String str, String str2, String str3) {
        this.zzce = (String) Preconditions.checkNotNull(str);
        this.zzo = (String) Preconditions.checkNotNull(str2);
        this.zzcl = (String) Preconditions.checkNotNull(str3);
    }

    public final PendingResult<Status> close(GoogleApiClient googleApiClient) {
        return googleApiClient.enqueue(new zzaz(this, googleApiClient));
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof zzay)) {
            return false;
        }
        zzay com_google_android_gms_wearable_internal_zzay = (zzay) obj;
        return this.zzce.equals(com_google_android_gms_wearable_internal_zzay.zzce) && Objects.equal(com_google_android_gms_wearable_internal_zzay.zzo, this.zzo) && Objects.equal(com_google_android_gms_wearable_internal_zzay.zzcl, this.zzcl);
    }

    public final PendingResult<GetInputStreamResult> getInputStream(GoogleApiClient googleApiClient) {
        return googleApiClient.enqueue(new zzbb(this, googleApiClient));
    }

    public final String getNodeId() {
        return this.zzo;
    }

    public final PendingResult<GetOutputStreamResult> getOutputStream(GoogleApiClient googleApiClient) {
        return googleApiClient.enqueue(new zzbc(this, googleApiClient));
    }

    public final String getPath() {
        return this.zzcl;
    }

    public final int hashCode() {
        return this.zzce.hashCode();
    }

    public final String toString() {
        String substring;
        char[] toCharArray = this.zzce.toCharArray();
        int i = 0;
        int i2 = i;
        while (i < toCharArray.length) {
            i2 += toCharArray[i];
            i++;
        }
        String trim = this.zzce.trim();
        int length = trim.length();
        if (length > 25) {
            substring = trim.substring(0, 10);
            trim = trim.substring(length - 10, length);
            StringBuilder stringBuilder = new StringBuilder((16 + String.valueOf(substring).length()) + String.valueOf(trim).length());
            stringBuilder.append(substring);
            stringBuilder.append("...");
            stringBuilder.append(trim);
            stringBuilder.append("::");
            stringBuilder.append(i2);
            trim = stringBuilder.toString();
        }
        substring = this.zzo;
        String str = this.zzcl;
        StringBuilder stringBuilder2 = new StringBuilder(((31 + String.valueOf(trim).length()) + String.valueOf(substring).length()) + String.valueOf(str).length());
        stringBuilder2.append("Channel{token=");
        stringBuilder2.append(trim);
        stringBuilder2.append(", nodeId=");
        stringBuilder2.append(substring);
        stringBuilder2.append(", path=");
        stringBuilder2.append(str);
        stringBuilder2.append("}");
        return stringBuilder2.toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        i = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzce, false);
        SafeParcelWriter.writeString(parcel, 3, getNodeId(), false);
        SafeParcelWriter.writeString(parcel, 4, getPath(), false);
        SafeParcelWriter.finishObjectHeader(parcel, i);
    }
}
