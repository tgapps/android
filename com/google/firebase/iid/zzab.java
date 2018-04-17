package com.google.firebase.iid;

import android.text.TextUtils;
import android.util.Log;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

final class zzab {
    private static final long zzbsa = TimeUnit.DAYS.toMillis(7);
    private final long timestamp;
    final String zzbsb;
    private final String zztc;

    private zzab(String str, String str2, long j) {
        this.zzbsb = str;
        this.zztc = str2;
        this.timestamp = j;
    }

    static String zza(String str, String str2, long j) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("token", str);
            jSONObject.put("appVersion", str2);
            jSONObject.put("timestamp", j);
            return jSONObject.toString();
        } catch (JSONException e) {
            str = String.valueOf(e);
            StringBuilder stringBuilder = new StringBuilder(24 + String.valueOf(str).length());
            stringBuilder.append("Failed to encode token: ");
            stringBuilder.append(str);
            Log.w("FirebaseInstanceId", stringBuilder.toString());
            return null;
        }
    }

    static zzab zzfe(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (!str.startsWith("{")) {
            return new zzab(str, null, 0);
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            return new zzab(jSONObject.getString("token"), jSONObject.getString("appVersion"), jSONObject.getLong("timestamp"));
        } catch (JSONException e) {
            str = String.valueOf(e);
            StringBuilder stringBuilder = new StringBuilder(23 + String.valueOf(str).length());
            stringBuilder.append("Failed to parse token: ");
            stringBuilder.append(str);
            Log.w("FirebaseInstanceId", stringBuilder.toString());
            return null;
        }
    }

    final boolean zzff(String str) {
        if (System.currentTimeMillis() <= this.timestamp + zzbsa) {
            if (str.equals(this.zztc)) {
                return false;
            }
        }
        return true;
    }
}
