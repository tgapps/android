package com.google.firebase.messaging;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.Map;

public final class RemoteMessage extends AbstractSafeParcelable {
    public static final Creator<RemoteMessage> CREATOR = new zzf();
    Bundle zzafw;
    private Map<String, String> zzrr;

    public RemoteMessage(Bundle bundle) {
        this.zzafw = bundle;
    }

    public final Map<String, String> getData() {
        if (this.zzrr == null) {
            this.zzrr = new ArrayMap();
            for (String str : this.zzafw.keySet()) {
                Object obj = this.zzafw.get(str);
                if (obj instanceof String) {
                    String str2 = (String) obj;
                    if (!(str.startsWith("google.") || str.startsWith("gcm.") || str.equals("from") || str.equals("message_type") || str.equals("collapse_key"))) {
                        this.zzrr.put(str, str2);
                    }
                }
            }
        }
        return this.zzrr;
    }

    public final String getFrom() {
        return this.zzafw.getString("from");
    }

    public final long getSentTime() {
        Object obj = this.zzafw.get("google.sent_time");
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        }
        if (obj instanceof String) {
            try {
                return Long.parseLong((String) obj);
            } catch (NumberFormatException e) {
                String valueOf = String.valueOf(obj);
                Log.w("FirebaseMessaging", new StringBuilder(String.valueOf(valueOf).length() + 19).append("Invalid sent time: ").append(valueOf).toString());
            }
        }
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeBundle(parcel, 2, this.zzafw, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
