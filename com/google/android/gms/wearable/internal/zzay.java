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
        int i = 0;
        for (char c : this.zzce.toCharArray()) {
            i += c;
        }
        String trim = this.zzce.trim();
        int length = trim.length();
        if (length > 25) {
            substring = trim.substring(0, 10);
            trim = trim.substring(length - 10, length);
            trim = new StringBuilder((String.valueOf(substring).length() + 16) + String.valueOf(trim).length()).append(substring).append("...").append(trim).append("::").append(i).toString();
        }
        substring = this.zzo;
        String str = this.zzcl;
        return new StringBuilder(((String.valueOf(trim).length() + 31) + String.valueOf(substring).length()) + String.valueOf(str).length()).append("Channel{token=").append(trim).append(", nodeId=").append(substring).append(", path=").append(str).append("}").toString();
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeString(parcel, 2, this.zzce, false);
        SafeParcelWriter.writeString(parcel, 3, getNodeId(), false);
        SafeParcelWriter.writeString(parcel, 4, getPath(), false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
