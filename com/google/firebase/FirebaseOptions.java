package com.google.firebase;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.gms.common.util.Strings;

public final class FirebaseOptions {
    private final String zzr;
    private final String zzs;
    private final String zzt;
    private final String zzu;
    private final String zzv;
    private final String zzw;
    private final String zzx;

    private FirebaseOptions(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        Preconditions.checkState(!Strings.isEmptyOrWhitespace(str), "ApplicationId must be set.");
        this.zzs = str;
        this.zzr = str2;
        this.zzt = str3;
        this.zzu = str4;
        this.zzv = str5;
        this.zzw = str6;
        this.zzx = str7;
    }

    public static FirebaseOptions fromResource(Context context) {
        StringResourceValueReader stringResourceValueReader = new StringResourceValueReader(context);
        Object string = stringResourceValueReader.getString("google_app_id");
        return TextUtils.isEmpty(string) ? null : new FirebaseOptions(string, stringResourceValueReader.getString("google_api_key"), stringResourceValueReader.getString("firebase_database_url"), stringResourceValueReader.getString("ga_trackingId"), stringResourceValueReader.getString("gcm_defaultSenderId"), stringResourceValueReader.getString("google_storage_bucket"), stringResourceValueReader.getString("project_id"));
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof FirebaseOptions)) {
            return false;
        }
        FirebaseOptions firebaseOptions = (FirebaseOptions) obj;
        return Objects.equal(this.zzs, firebaseOptions.zzs) && Objects.equal(this.zzr, firebaseOptions.zzr) && Objects.equal(this.zzt, firebaseOptions.zzt) && Objects.equal(this.zzu, firebaseOptions.zzu) && Objects.equal(this.zzv, firebaseOptions.zzv) && Objects.equal(this.zzw, firebaseOptions.zzw) && Objects.equal(this.zzx, firebaseOptions.zzx);
    }

    public final String getApplicationId() {
        return this.zzs;
    }

    public final String getGcmSenderId() {
        return this.zzv;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzs, this.zzr, this.zzt, this.zzu, this.zzv, this.zzw, this.zzx);
    }

    public final String toString() {
        return Objects.toStringHelper(this).add("applicationId", this.zzs).add("apiKey", this.zzr).add("databaseUrl", this.zzt).add("gcmSenderId", this.zzv).add("storageBucket", this.zzw).add("projectId", this.zzx).toString();
    }
}
