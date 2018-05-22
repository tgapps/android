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
import android.util.Log;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.concurrent.GuardedBy;

public class FirebaseInstanceId {
    private static final long zzah = TimeUnit.HOURS.toSeconds(8);
    private static zzao zzai;
    @GuardedBy("FirebaseInstanceId.class")
    private static ScheduledThreadPoolExecutor zzaj;
    private final FirebaseApp zzak;
    private final zzae zzal;
    private final zzo zzam;
    private final zzah zzan;
    @GuardedBy("this")
    private boolean zzao;
    @GuardedBy("this")
    private boolean zzap;

    FirebaseInstanceId(FirebaseApp firebaseApp) {
        this(firebaseApp, new zzae(firebaseApp.getApplicationContext()));
    }

    private FirebaseInstanceId(FirebaseApp firebaseApp, zzae com_google_firebase_iid_zzae) {
        this.zzan = new zzah();
        this.zzao = false;
        if (zzae.zza(firebaseApp) == null) {
            throw new IllegalStateException("FirebaseInstanceId failed to initialize, FirebaseApp is missing project ID");
        }
        synchronized (FirebaseInstanceId.class) {
            if (zzai == null) {
                zzai = new zzao(firebaseApp.getApplicationContext());
            }
        }
        this.zzak = firebaseApp;
        this.zzal = com_google_firebase_iid_zzae;
        this.zzam = new zzl(firebaseApp, this, com_google_firebase_iid_zzae);
        this.zzap = zzm();
        if (zzo()) {
            zzd();
        }
    }

    public static FirebaseInstanceId getInstance() {
        return getInstance(FirebaseApp.getInstance());
    }

    @Keep
    public static synchronized FirebaseInstanceId getInstance(FirebaseApp firebaseApp) {
        FirebaseInstanceId firebaseInstanceId;
        synchronized (FirebaseInstanceId.class) {
            firebaseInstanceId = (FirebaseInstanceId) firebaseApp.get(FirebaseInstanceId.class);
        }
        return firebaseInstanceId;
    }

    private final synchronized void startSync() {
        if (!this.zzao) {
            zza(0);
        }
    }

    private static <T> T zza(Task<T> task) throws IOException {
        try {
            return Tasks.await(task);
        } catch (Throwable e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw ((IOException) cause);
            } else if (cause instanceof RuntimeException) {
                throw new IOException(cause);
            } else {
                throw new IOException(e);
            }
        } catch (InterruptedException e2) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
    }

    private final String zza(String str, String str2, Bundle bundle) throws IOException {
        return ((zzl) this.zzam).zza(str, str2, bundle);
    }

    static void zza(Runnable runnable, long j) {
        synchronized (FirebaseInstanceId.class) {
            if (zzaj == null) {
                zzaj = new ScheduledThreadPoolExecutor(1);
            }
            zzaj.schedule(runnable, j, TimeUnit.SECONDS);
        }
    }

    private static String zzd(String str) {
        return (str.isEmpty() || str.equalsIgnoreCase(AppMeasurement.FCM_ORIGIN) || str.equalsIgnoreCase("gcm")) ? "*" : str;
    }

    private final void zzd() {
        zzap zzg = zzg();
        if (zzg == null || zzg.zzj(this.zzal.zzy()) || zzai.zzaf() != null) {
            startSync();
        }
    }

    static String zzf() {
        return zzae.zza(zzai.zzg(TtmlNode.ANONYMOUS_REGION_ID).getKeyPair());
    }

    static zzao zzi() {
        return zzai;
    }

    static boolean zzj() {
        return Log.isLoggable("FirebaseInstanceId", 3) || (VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseInstanceId", 3));
    }

    private final boolean zzm() {
        Context applicationContext = this.zzak.getApplicationContext();
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
        return zzn();
    }

    private final boolean zzn() {
        try {
            Class.forName("com.google.firebase.messaging.FirebaseMessaging");
            return true;
        } catch (ClassNotFoundException e) {
            Context applicationContext = this.zzak.getApplicationContext();
            Intent intent = new Intent("com.google.firebase.MESSAGING_EVENT");
            intent.setPackage(applicationContext.getPackageName());
            ResolveInfo resolveService = applicationContext.getPackageManager().resolveService(intent, 0);
            return (resolveService == null || resolveService.serviceInfo == null) ? false : true;
        }
    }

    public String getId() {
        zzd();
        return zzf();
    }

    public String getToken() {
        zzap zzg = zzg();
        if (zzg == null || zzg.zzj(this.zzal.zzy())) {
            startSync();
        }
        return zzg != null ? zzg.zzcu : null;
    }

    public String getToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            throw new IOException("MAIN_THREAD");
        }
        String zzd = zzd(str2);
        zzap zzb = zzai.zzb(TtmlNode.ANONYMOUS_REGION_ID, str, zzd);
        return (zzb == null || zzb.zzj(this.zzal.zzy())) ? this.zzan.zza(str, zzd, new zzk(this, str, zzd)) : zzb.zzcu;
    }

    final /* synthetic */ String zza(String str, String str2) throws IOException {
        String str3 = (String) zza(this.zzam.zzb(str, str2));
        zzai.zza(TtmlNode.ANONYMOUS_REGION_ID, str, str2, str3, this.zzal.zzy());
        return str3;
    }

    final synchronized void zza(long j) {
        zza(new zzaq(this, this.zzal, Math.min(Math.max(30, j << 1), zzah)), j);
        this.zzao = true;
    }

    final synchronized void zza(boolean z) {
        this.zzao = z;
    }

    final void zzb(String str) throws IOException {
        zzap zzg = zzg();
        if (zzg == null || zzg.zzj(this.zzal.zzy())) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String str2 = "gcm.topic";
        String valueOf = String.valueOf("/topics/");
        String valueOf2 = String.valueOf(str);
        bundle.putString(str2, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        String str3 = zzg.zzcu;
        str2 = String.valueOf("/topics/");
        valueOf2 = String.valueOf(str);
        zza(str3, valueOf2.length() != 0 ? str2.concat(valueOf2) : new String(str2), bundle);
    }

    final void zzc(String str) throws IOException {
        zzap zzg = zzg();
        if (zzg == null || zzg.zzj(this.zzal.zzy())) {
            throw new IOException("token not available");
        }
        Bundle bundle = new Bundle();
        String str2 = "gcm.topic";
        String valueOf = String.valueOf("/topics/");
        String valueOf2 = String.valueOf(str);
        bundle.putString(str2, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        bundle.putString("delete", "1");
        String str3 = zzg.zzcu;
        str2 = String.valueOf("/topics/");
        valueOf2 = String.valueOf(str);
        zza(str3, valueOf2.length() != 0 ? str2.concat(valueOf2) : new String(str2), bundle);
    }

    final FirebaseApp zze() {
        return this.zzak;
    }

    final zzap zzg() {
        return zzai.zzb(TtmlNode.ANONYMOUS_REGION_ID, zzae.zza(this.zzak), "*");
    }

    final String zzh() throws IOException {
        return getToken(zzae.zza(this.zzak), "*");
    }

    final synchronized void zzk() {
        zzai.zzag();
        if (zzo()) {
            startSync();
        }
    }

    final void zzl() {
        zzai.zzh(TtmlNode.ANONYMOUS_REGION_ID);
        startSync();
    }

    public final synchronized boolean zzo() {
        return this.zzap;
    }
}
