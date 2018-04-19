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
        gcmSenderId = firebaseApp.getOptions().getApplicationId();
        if (!gcmSenderId.startsWith("1:")) {
            return gcmSenderId;
        }
        String[] split = gcmSenderId.split(":");
        if (split.length < 2) {
            return null;
        }
        gcmSenderId = split[1];
        return gcmSenderId.isEmpty() ? null : gcmSenderId;
    }

    public static String zza(KeyPair keyPair) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA1").digest(keyPair.getPublic().getEncoded());
            digest[0] = (byte) ((digest[0] & 15) + 112);
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
            String valueOf = String.valueOf(e);
            Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 23).append("Failed to find package ").append(valueOf).toString());
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
        int i = 0;
        synchronized (this) {
            if (this.zzbrn != 0) {
                i = this.zzbrn;
            } else {
                PackageManager packageManager = this.zzqs.getPackageManager();
                if (packageManager.checkPermission("com.google.android.c2dm.permission.SEND", "com.google.android.gms") == -1) {
                    Log.e("FirebaseInstanceId", "Google Play services missing or without correct permission.");
                } else {
                    Intent intent;
                    List queryIntentServices;
                    if (!PlatformVersion.isAtLeastO()) {
                        intent = new Intent("com.google.android.c2dm.intent.REGISTER");
                        intent.setPackage("com.google.android.gms");
                        queryIntentServices = packageManager.queryIntentServices(intent, 0);
                        if (queryIntentServices != null && queryIntentServices.size() > 0) {
                            this.zzbrn = 1;
                            i = this.zzbrn;
                        }
                    }
                    intent = new Intent("com.google.iid.TOKEN_REQUEST");
                    intent.setPackage("com.google.android.gms");
                    queryIntentServices = packageManager.queryBroadcastReceivers(intent, 0);
                    if (queryIntentServices == null || queryIntentServices.size() <= 0) {
                        Log.w("FirebaseInstanceId", "Failed to resolve IID implementation package, falling back");
                        if (PlatformVersion.isAtLeastO()) {
                            this.zzbrn = 2;
                        } else {
                            this.zzbrn = 1;
                        }
                        i = this.zzbrn;
                    } else {
                        this.zzbrn = 2;
                        i = this.zzbrn;
                    }
                }
            }
        }
        return i;
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
