package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Keep;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.GuardedBy;

public class FirebaseInstanceId {
    private static final long zzbqi = TimeUnit.HOURS.toSeconds(8);
    private static Map<String, FirebaseInstanceId> zzbqj = new ArrayMap();
    private static zzaa zzbqk;
    @GuardedBy("FirebaseInstanceId.class")
    private static ScheduledThreadPoolExecutor zzbql;
    private final FirebaseApp zzbqm;
    private final zzw zzbqn;
    private final zzx zzbqo;
    @GuardedBy("this")
    private KeyPair zzbqp;
    @GuardedBy("this")
    private boolean zzbqq = false;
    @GuardedBy("this")
    private boolean zzbqr;

    private FirebaseInstanceId(FirebaseApp firebaseApp, zzw com_google_firebase_iid_zzw) {
        if (zzw.zza(firebaseApp) == null) {
            throw new IllegalStateException("FirebaseInstanceId failed to initialize, FirebaseApp is missing project ID");
        }
        this.zzbqm = firebaseApp;
        this.zzbqn = com_google_firebase_iid_zzw;
        this.zzbqo = new zzx(firebaseApp.getApplicationContext(), com_google_firebase_iid_zzw);
        this.zzbqr = zzsm();
        if (zzso()) {
            zzse();
        }
    }

    public static FirebaseInstanceId getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    @Keep
    public static synchronized FirebaseInstanceId getInstance(FirebaseApp firebaseApp) {
        FirebaseInstanceId firebaseInstanceId;
        synchronized (FirebaseInstanceId.class) {
            firebaseInstanceId = (FirebaseInstanceId) zzbqj.get(firebaseApp.getOptions().getApplicationId());
            if (firebaseInstanceId == null) {
                if (zzbqk == null) {
                    zzbqk = new zzaa(firebaseApp.getApplicationContext());
                }
                firebaseInstanceId = new FirebaseInstanceId(firebaseApp, new zzw(firebaseApp.getApplicationContext()));
                zzbqj.put(firebaseApp.getOptions().getApplicationId(), firebaseInstanceId);
            }
        }
        return firebaseInstanceId;
    }

    private final synchronized KeyPair getKeyPair() {
        if (this.zzbqp == null) {
            this.zzbqp = zzbqk.zzfd(TtmlNode.ANONYMOUS_REGION_ID);
        }
        if (this.zzbqp == null) {
            this.zzbqp = zzbqk.zzfb(TtmlNode.ANONYMOUS_REGION_ID);
        }
        return this.zzbqp;
    }

    private final synchronized void startSync() {
        if (!this.zzbqq) {
            zzan(0);
        }
    }

    static void zza(Runnable runnable, long j) {
        synchronized (FirebaseInstanceId.class) {
            if (zzbql == null) {
                zzbql = new ScheduledThreadPoolExecutor(1);
            }
            zzbql.schedule(runnable, j, TimeUnit.SECONDS);
        }
    }

    private final String zzb(String str, String str2, Bundle bundle) throws IOException {
        bundle.putString("scope", str2);
        bundle.putString("sender", str);
        bundle.putString("subtype", str);
        bundle.putString("appid", getId());
        bundle.putString("gmp_app_id", this.zzbqm.getOptions().getApplicationId());
        bundle.putString("gmsv", Integer.toString(this.zzbqn.zzsx()));
        bundle.putString("osv", Integer.toString(VERSION.SDK_INT));
        bundle.putString("app_ver", this.zzbqn.zzsv());
        bundle.putString("app_ver_name", this.zzbqn.zzsw());
        bundle.putString("cliv", "fiid-12451000");
        Bundle zzi = this.zzbqo.zzi(bundle);
        if (zzi == null) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
        String string = zzi.getString("registration_id");
        if (string == null) {
            string = zzi.getString("unregistered");
            if (string == null) {
                string = zzi.getString("error");
                if ("RST".equals(string)) {
                    zzsk();
                    throw new IOException("INSTANCE_ID_RESET");
                } else if (string != null) {
                    throw new IOException(string);
                } else {
                    String valueOf = String.valueOf(zzi);
                    Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 21).append("Unexpected response: ").append(valueOf).toString(), new Throwable());
                    throw new IOException("SERVICE_NOT_AVAILABLE");
                }
            }
        }
        return string;
    }

    private final void zzse() {
        zzab zzsg = zzsg();
        if (zzsg == null || zzsg.zzff(this.zzbqn.zzsv()) || zzbqk.zztc() != null) {
            startSync();
        }
    }

    static zzaa zzsi() {
        return zzbqk;
    }

    static boolean zzsj() {
        return Log.isLoggable("FirebaseInstanceId", 3) || (VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseInstanceId", 3));
    }

    private final boolean zzsm() {
        Context applicationContext = this.zzbqm.getApplicationContext();
        SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("com.google.firebase.messaging", 0);
        if (sharedPreferences.contains("auto_init")) {
            return sharedPreferences.getBoolean("auto_init", true);
        }
        try {
            PackageManager packageManager = applicationContext.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(applicationContext.getPackageName(), 128);
                if (!(applicationInfo == null || applicationInfo.metaData == null || !applicationInfo.metaData.containsKey("firebase_messaging_auto_init_enabled"))) {
                    return applicationInfo.metaData.getBoolean("firebase_messaging_auto_init_enabled");
                }
            }
        } catch (NameNotFoundException e) {
        }
        return zzsn();
    }

    private final boolean zzsn() {
        try {
            Class.forName("com.google.firebase.messaging.FirebaseMessaging");
            return true;
        } catch (ClassNotFoundException e) {
            Context applicationContext = this.zzbqm.getApplicationContext();
            Intent intent = new Intent("com.google.firebase.MESSAGING_EVENT");
            intent.setPackage(applicationContext.getPackageName());
            ResolveInfo resolveService = applicationContext.getPackageManager().resolveService(intent, 0);
            return (resolveService == null || resolveService.serviceInfo == null) ? false : true;
        }
    }

    public String getId() {
        zzse();
        return zzw.zza(getKeyPair());
    }

    public String getToken() {
        zzab zzsg = zzsg();
        if (zzsg == null || zzsg.zzff(this.zzbqn.zzsv())) {
            startSync();
        }
        return zzsg != null ? zzsg.zzbsb : null;
    }

    public String getToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        zzab zzj = zzbqk.zzj(TtmlNode.ANONYMOUS_REGION_ID, str, str2);
        if (zzj != null && !zzj.zzff(this.zzbqn.zzsv())) {
            return zzj.zzbsb;
        }
        String zzb = zzb(str, str2, new Bundle());
        if (zzb == null) {
            return zzb;
        }
        zzbqk.zza(TtmlNode.ANONYMOUS_REGION_ID, str, str2, zzb, this.zzbqn.zzsv());
        return zzb;
    }

    final synchronized void zzan(long j) {
        zza(new zzac(this, this.zzbqn, Math.min(Math.max(30, j << 1), zzbqi)), j);
        this.zzbqq = true;
    }

    final void zzew(String str) throws IOException {
        zzab zzsg = zzsg();
        if (zzsg == null || zzsg.zzff(this.zzbqn.zzsv())) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String str2 = "gcm.topic";
        String valueOf = String.valueOf("/topics/");
        String valueOf2 = String.valueOf(str);
        bundle.putString(str2, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        String str3 = zzsg.zzbsb;
        str2 = String.valueOf("/topics/");
        valueOf2 = String.valueOf(str);
        zzb(str3, valueOf2.length() != 0 ? str2.concat(valueOf2) : new String(str2), bundle);
    }

    final void zzex(String str) throws IOException {
        zzab zzsg = zzsg();
        if (zzsg == null || zzsg.zzff(this.zzbqn.zzsv())) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String str2 = "gcm.topic";
        String valueOf = String.valueOf("/topics/");
        String valueOf2 = String.valueOf(str);
        bundle.putString(str2, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        bundle.putString("delete", "1");
        String str3 = zzsg.zzbsb;
        str2 = String.valueOf("/topics/");
        valueOf2 = String.valueOf(str);
        zzb(str3, valueOf2.length() != 0 ? str2.concat(valueOf2) : new String(str2), bundle);
    }

    final FirebaseApp zzsf() {
        return this.zzbqm;
    }

    final zzab zzsg() {
        return zzbqk.zzj(TtmlNode.ANONYMOUS_REGION_ID, zzw.zza(this.zzbqm), "*");
    }

    final String zzsh() throws IOException {
        return getToken(zzw.zza(this.zzbqm), "*");
    }

    final synchronized void zzsk() {
        zzbqk.zztd();
        this.zzbqp = null;
        if (zzso()) {
            startSync();
        }
    }

    final void zzsl() {
        zzbqk.zzfc(TtmlNode.ANONYMOUS_REGION_ID);
        startSync();
    }

    public final synchronized boolean zzso() {
        return this.zzbqr;
    }

    final synchronized void zzu(boolean z) {
        this.zzbqq = z;
    }
}
