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

public final class zzal {
    @GuardedBy("this")
    private String zzbz;
    @GuardedBy("this")
    private String zzca;
    @GuardedBy("this")
    private int zzcb;
    @GuardedBy("this")
    private int zzcc = 0;
    private final Context zzv;

    public zzal(Context context) {
        this.zzv = context;
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

    private final synchronized void zzaf() {
        PackageInfo zze = zze(this.zzv.getPackageName());
        if (zze != null) {
            this.zzbz = Integer.toString(zze.versionCode);
            this.zzca = zze.versionName;
        }
    }

    private final PackageInfo zze(String str) {
        try {
            return this.zzv.getPackageManager().getPackageInfo(str, 0);
        } catch (NameNotFoundException e) {
            String valueOf = String.valueOf(e);
            Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 23).append("Failed to find package ").append(valueOf).toString());
            return null;
        }
    }

    public final synchronized int zzab() {
        int i = 0;
        synchronized (this) {
            if (this.zzcc != 0) {
                i = this.zzcc;
            } else {
                PackageManager packageManager = this.zzv.getPackageManager();
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
                            this.zzcc = 1;
                            i = this.zzcc;
                        }
                    }
                    intent = new Intent("com.google.iid.TOKEN_REQUEST");
                    intent.setPackage("com.google.android.gms");
                    queryIntentServices = packageManager.queryBroadcastReceivers(intent, 0);
                    if (queryIntentServices == null || queryIntentServices.size() <= 0) {
                        Log.w("FirebaseInstanceId", "Failed to resolve IID implementation package, falling back");
                        if (PlatformVersion.isAtLeastO()) {
                            this.zzcc = 2;
                        } else {
                            this.zzcc = 1;
                        }
                        i = this.zzcc;
                    } else {
                        this.zzcc = 2;
                        i = this.zzcc;
                    }
                }
            }
        }
        return i;
    }

    public final synchronized String zzac() {
        if (this.zzbz == null) {
            zzaf();
        }
        return this.zzbz;
    }

    public final synchronized String zzad() {
        if (this.zzca == null) {
            zzaf();
        }
        return this.zzca;
    }

    public final synchronized int zzae() {
        if (this.zzcb == 0) {
            PackageInfo zze = zze("com.google.android.gms");
            if (zze != null) {
                this.zzcb = zze.versionCode;
            }
        }
        return this.zzcb;
    }
}
