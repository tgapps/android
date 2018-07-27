package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;
import android.os.Looper;
import android.support.annotation.Keep;
import android.util.Log;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.concurrent.GuardedBy;

public class FirebaseInstanceId {
    private static final long zzaf = TimeUnit.HOURS.toSeconds(8);
    private static zzau zzag;
    @GuardedBy("FirebaseInstanceId.class")
    private static ScheduledThreadPoolExecutor zzah;
    private final Executor zzai;
    private final FirebaseApp zzaj;
    private final zzal zzak;
    private MessagingChannel zzal;
    private final zzao zzam;
    private final zzay zzan;
    @GuardedBy("this")
    private boolean zzao;
    @GuardedBy("this")
    private boolean zzap;

    FirebaseInstanceId(FirebaseApp firebaseApp) {
        this(firebaseApp, new zzal(firebaseApp.getApplicationContext()), zzi.zze(), zzi.zze());
    }

    private FirebaseInstanceId(FirebaseApp firebaseApp, zzal com_google_firebase_iid_zzal, Executor executor, Executor executor2) {
        this.zzam = new zzao();
        this.zzao = false;
        if (zzal.zza(firebaseApp) == null) {
            throw new IllegalStateException("FirebaseInstanceId failed to initialize, FirebaseApp is missing project ID");
        }
        synchronized (FirebaseInstanceId.class) {
            if (zzag == null) {
                zzag = new zzau(firebaseApp.getApplicationContext());
            }
        }
        this.zzaj = firebaseApp;
        this.zzak = com_google_firebase_iid_zzal;
        if (this.zzal == null) {
            MessagingChannel messagingChannel = (MessagingChannel) firebaseApp.get(MessagingChannel.class);
            if (messagingChannel == null || !messagingChannel.isAvailable()) {
                this.zzal = new zzp(firebaseApp, com_google_firebase_iid_zzal, executor);
            } else {
                this.zzal = messagingChannel;
            }
        }
        this.zzal = this.zzal;
        this.zzai = executor2;
        this.zzan = new zzay(zzag);
        this.zzap = zzq();
        if (zzs()) {
            zzf();
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

    private final Task<InstanceIdResult> zza(String str, String str2) {
        String zzd = zzd(str2);
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzai.execute(new zzm(this, str, str2, taskCompletionSource, zzd));
        return taskCompletionSource.getTask();
    }

    private final <T> T zza(Task<T> task) throws IOException {
        try {
            return Tasks.await(task, 30000, TimeUnit.MILLISECONDS);
        } catch (Throwable e) {
            Throwable th = e;
            Throwable e2 = th.getCause();
            if (e2 instanceof IOException) {
                if ("INSTANCE_ID_RESET".equals(e2.getMessage())) {
                    zzl();
                }
                throw ((IOException) e2);
            } else if (e2 instanceof RuntimeException) {
                throw ((RuntimeException) e2);
            } else {
                throw new IOException(th);
            }
        } catch (InterruptedException e3) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        } catch (TimeoutException e4) {
            throw new IOException("SERVICE_NOT_AVAILABLE");
        }
    }

    static void zza(Runnable runnable, long j) {
        synchronized (FirebaseInstanceId.class) {
            if (zzah == null) {
                zzah = new ScheduledThreadPoolExecutor(1);
            }
            zzah.schedule(runnable, j, TimeUnit.SECONDS);
        }
    }

    private static String zzd(String str) {
        return (str.isEmpty() || str.equalsIgnoreCase(AppMeasurement.FCM_ORIGIN) || str.equalsIgnoreCase("gcm")) ? "*" : str;
    }

    private final void zzf() {
        zzav zzi = zzi();
        if (!zzn() || zzi == null || zzi.zzj(this.zzak.zzac()) || this.zzan.zzap()) {
            startSync();
        }
    }

    private static String zzh() {
        return zzal.zza(zzag.zzg(TtmlNode.ANONYMOUS_REGION_ID).getKeyPair());
    }

    static boolean zzk() {
        return Log.isLoggable("FirebaseInstanceId", 3) || (VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseInstanceId", 3));
    }

    private final boolean zzq() {
        Context applicationContext = this.zzaj.getApplicationContext();
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
        return zzr();
    }

    private final boolean zzr() {
        try {
            Class.forName("com.google.firebase.messaging.FirebaseMessaging");
            return true;
        } catch (ClassNotFoundException e) {
            Context applicationContext = this.zzaj.getApplicationContext();
            Intent intent = new Intent("com.google.firebase.MESSAGING_EVENT");
            intent.setPackage(applicationContext.getPackageName());
            ResolveInfo resolveService = applicationContext.getPackageManager().resolveService(intent, 0);
            return (resolveService == null || resolveService.serviceInfo == null) ? false : true;
        }
    }

    public String getId() {
        zzf();
        return zzh();
    }

    @Deprecated
    public String getToken() {
        zzav zzi = zzi();
        if (zzi == null || zzi.zzj(this.zzak.zzac())) {
            startSync();
        }
        return zzi != null ? zzi.zzbh : null;
    }

    public String getToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            return ((InstanceIdResult) zza(zza(str, str2))).getToken();
        }
        throw new IOException("MAIN_THREAD");
    }

    final /* synthetic */ Task zza(String str, String str2, String str3) {
        return this.zzal.getToken(str, str2, str3);
    }

    final synchronized void zza(long j) {
        zza(new zzaw(this, this.zzak, this.zzan, Math.min(Math.max(30, j << 1), zzaf)), j);
        this.zzao = true;
    }

    final /* synthetic */ void zza(String str, String str2, TaskCompletionSource taskCompletionSource, String str3) {
        String zzh = zzh();
        zzav zzc = zzag.zzc(TtmlNode.ANONYMOUS_REGION_ID, str, str2);
        if (zzc == null || zzc.zzj(this.zzak.zzac())) {
            this.zzam.zza(str, str3, new zzn(this, zzh, str, str3)).addOnCompleteListener(this.zzai, new zzo(this, str, str3, taskCompletionSource, zzh));
        } else {
            taskCompletionSource.setResult(new zzv(zzh, zzc.zzbh));
        }
    }

    final /* synthetic */ void zza(String str, String str2, TaskCompletionSource taskCompletionSource, String str3, Task task) {
        if (task.isSuccessful()) {
            String str4 = (String) task.getResult();
            zzag.zza(TtmlNode.ANONYMOUS_REGION_ID, str, str2, str4, this.zzak.zzac());
            taskCompletionSource.setResult(new zzv(str3, str4));
            return;
        }
        taskCompletionSource.setException(task.getException());
    }

    final synchronized void zza(boolean z) {
        this.zzao = z;
    }

    final void zzb(String str) throws IOException {
        zzav zzi = zzi();
        if (zzi == null || zzi.zzj(this.zzak.zzac())) {
            throw new IOException("token not available");
        }
        zza(this.zzal.subscribeToTopic(zzh(), zzi.zzbh, str));
    }

    final void zzc(String str) throws IOException {
        zzav zzi = zzi();
        if (zzi == null || zzi.zzj(this.zzak.zzac())) {
            throw new IOException("token not available");
        }
        zza(this.zzal.unsubscribeFromTopic(zzh(), zzi.zzbh, str));
    }

    final FirebaseApp zzg() {
        return this.zzaj;
    }

    final zzav zzi() {
        return zzag.zzc(TtmlNode.ANONYMOUS_REGION_ID, zzal.zza(this.zzaj), "*");
    }

    final String zzj() throws IOException {
        return getToken(zzal.zza(this.zzaj), "*");
    }

    final synchronized void zzl() {
        zzag.zzak();
        if (zzs()) {
            startSync();
        }
    }

    final boolean zzm() {
        return this.zzal.isAvailable();
    }

    final boolean zzn() {
        return this.zzal.isChannelBuilt();
    }

    final void zzo() throws IOException {
        String zzh = zzh();
        zzav zzi = zzi();
        zza(this.zzal.buildChannel(zzh, zzi == null ? null : zzi.zzbh));
    }

    final void zzp() {
        zzag.zzh(TtmlNode.ANONYMOUS_REGION_ID);
        startSync();
    }

    public final synchronized boolean zzs() {
        return this.zzap;
    }
}
