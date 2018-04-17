package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.firebase.FirebaseApp;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import javax.annotation.concurrent.GuardedBy;

public final class zzw {
    @GuardedBy("this")
    private String zzbrk;
    @GuardedBy("this")
    private String zzbrl;
    @GuardedBy("this")
    private int zzbrm;
    @GuardedBy("this")
    private int zzbrn = 0;
    private final Context zzqs;

    public zzw(Context context) {
        this.zzqs = context;
    }

    public static String zza(FirebaseApp firebaseApp) {
        String gcmSenderId = firebaseApp.getOptions().getGcmSenderId();
        if (gcmSenderId != null) {
            return gcmSenderId;
        }
        String applicationId = firebaseApp.getOptions().getApplicationId();
        if (!applicationId.startsWith("1:")) {
            return applicationId;
        }
        String[] split = applicationId.split(":");
        if (split.length < 2) {
            return null;
        }
        applicationId = split[1];
        return applicationId.isEmpty() ? null : applicationId;
    }

    public static String zza(KeyPair keyPair) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA1").digest(keyPair.getPublic().getEncoded());
            digest[0] = (byte) (112 + (digest[0] & 15));
            return Base64.encodeToString(digest, 0, 8, 11);
        } catch (NoSuchAlgorithmException e) {
            Log.w("FirebaseInstanceId", "Unexpected error, device missing required algorithms");
            return null;
        }
    }

    private final PackageInfo zzey(String str) {
        try {
            return this.zzqs.getPackageManager().getPackageInfo(str, 0);
        } catch (NameNotFoundException e) {
            str = String.valueOf(e);
            StringBuilder stringBuilder = new StringBuilder(23 + String.valueOf(str).length());
            stringBuilder.append("Failed to find package ");
            stringBuilder.append(str);
            Log.w("FirebaseInstanceId", stringBuilder.toString());
            return null;
        }
    }

    private final synchronized void zzsy() {
        PackageInfo zzey = zzey(this.zzqs.getPackageName());
        if (zzey != null) {
            this.zzbrk = Integer.toString(zzey.versionCode);
            this.zzbrl = zzey.versionName;
        }
    }

    public final synchronized int zzsu() {
        if (this.zzbrn != 0) {
            return this.zzbrn;
        }
        PackageManager packageManager = this.zzqs.getPackageManager();
        if (packageManager.checkPermission("com.google.android.c2dm.permission.SEND", "com.google.android.gms") == -1) {
            Log.e("FirebaseInstanceId", "Google Play services missing or without correct permission.");
            return 0;
        }
        Intent intent;
        if (!PlatformVersion.isAtLeastO()) {
            intent = new Intent("com.google.android.c2dm.intent.REGISTER");
            intent.setPackage("com.google.android.gms");
            List queryIntentServices = packageManager.queryIntentServices(intent, 0);
            if (queryIntentServices != null && queryIntentServices.size() > 0) {
                this.zzbrn = 1;
                return this.zzbrn;
            }
        }
        intent = new Intent("com.google.iid.TOKEN_REQUEST");
        intent.setPackage("com.google.android.gms");
        List queryBroadcastReceivers = packageManager.queryBroadcastReceivers(intent, 0);
        if (queryBroadcastReceivers == null || queryBroadcastReceivers.size() <= 0) {
            Log.w("FirebaseInstanceId", "Failed to resolve IID implementation package, falling back");
            if (PlatformVersion.isAtLeastO()) {
                this.zzbrn = 2;
            } else {
                this.zzbrn = 1;
            }
            return this.zzbrn;
        }
        this.zzbrn = 2;
        return this.zzbrn;
    }

    public final synchronized String zzsv() {
        if (this.zzbrk == null) {
            zzsy();
        }
        return this.zzbrk;
    }

    public final synchronized String zzsw() {
        if (this.zzbrl == null) {
            zzsy();
        }
        return this.zzbrl;
    }

    public final synchronized int zzsx() {
        if (this.zzbrm == 0) {
            PackageInfo zzey = zzey("com.google.android.gms");
            if (zzey != null) {
                this.zzbrm = zzey.versionCode;
            }
        }
        return this.zzbrm;
    }
}
