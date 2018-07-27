package com.google.firebase;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.gms.common.util.Strings;

public final class FirebaseOptions {
    private final String zza;
    private final String zzb;
    private final String zzc;
    private final String zzd;
    private final String zze;
    private final String zzf;
    private final String zzg;

    private FirebaseOptions(String applicationId, String apiKey, String databaseUrl, String gaTrackingId, String gcmSenderId, String storageBucket, String projectId) {
        Preconditions.checkState(!Strings.isEmptyOrWhitespace(applicationId), "ApplicationId must be set.");
        this.zzb = applicationId;
        this.zza = apiKey;
        this.zzc = databaseUrl;
        this.zzd = gaTrackingId;
        this.zze = gcmSenderId;
        this.zzf = storageBucket;
        this.zzg = projectId;
    }

    public static FirebaseOptions fromResource(Context context) {
        StringResourceValueReader stringResourceValueReader = new StringResourceValueReader(context);
        Object string = stringResourceValueReader.getString("google_app_id");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return new FirebaseOptions(string, stringResourceValueReader.getString("google_api_key"), stringResourceValueReader.getString("firebase_database_url"), stringResourceValueReader.getString("ga_trackingId"), stringResourceValueReader.getString("gcm_defaultSenderId"), stringResourceValueReader.getString("google_storage_bucket"), stringResourceValueReader.getString("project_id"));
    }

    public final String getApplicationId() {
        return this.zzb;
    }

    public final String getGcmSenderId() {
        return this.zze;
    }

    public final boolean equals(Object o) {
        if (!(o instanceof FirebaseOptions)) {
            return false;
        }
        FirebaseOptions firebaseOptions = (FirebaseOptions) o;
        if (Objects.equal(this.zzb, firebaseOptions.zzb) && Objects.equal(this.zza, firebaseOptions.zza) && Objects.equal(this.zzc, firebaseOptions.zzc) && Objects.equal(this.zzd, firebaseOptions.zzd) && Objects.equal(this.zze, firebaseOptions.zze) && Objects.equal(this.zzf, firebaseOptions.zzf) && Objects.equal(this.zzg, firebaseOptions.zzg)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzb, this.zza, this.zzc, this.zzd, this.zze, this.zzf, this.zzg);
    }

    public final String toString() {
        return Objects.toStringHelper(this).add("applicationId", this.zzb).add("apiKey", this.zza).add("databaseUrl", this.zzc).add("gcmSenderId", this.zze).add("storageBucket", this.zzf).add("projectId", this.zzg).toString();
    }
}
