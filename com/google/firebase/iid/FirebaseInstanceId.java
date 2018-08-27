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
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.DataCollectionDefaultChange;
import com.google.firebase.FirebaseApp;
import com.google.firebase.events.EventHandler;
import com.google.firebase.events.Subscriber;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.concurrent.GuardedBy;

public class FirebaseInstanceId {
    private static final long zzaf = TimeUnit.HOURS.toSeconds(8);
    private static zzav zzag;
    @GuardedBy("FirebaseInstanceId.class")
    private static ScheduledThreadPoolExecutor zzah;
    private final Executor zzai;
    private final FirebaseApp zzaj;
    private final zzam zzak;
    private MessagingChannel zzal;
    private final zzap zzam;
    private final zzaz zzan;
    @GuardedBy("this")
    private boolean zzao;
    private final zza zzap;

    private class zza {
        private final boolean zzaw = zzt();
        private final Subscriber zzax;
        @GuardedBy("this")
        private EventHandler<DataCollectionDefaultChange> zzay;
        @GuardedBy("this")
        private Boolean zzaz = zzs();
        final /* synthetic */ FirebaseInstanceId zzba;

        zza(FirebaseInstanceId firebaseInstanceId, Subscriber subscriber) {
            this.zzba = firebaseInstanceId;
            this.zzax = subscriber;
            if (this.zzaz == null && this.zzaw) {
                this.zzay = new zzp(this);
                subscriber.subscribe(DataCollectionDefaultChange.class, this.zzay);
            }
        }

        final synchronized boolean isEnabled() {
            boolean booleanValue;
            booleanValue = this.zzaz != null ? this.zzaz.booleanValue() : this.zzaw && this.zzba.zzaj.isDataCollectionDefaultEnabled();
            return booleanValue;
        }

        private final Boolean zzs() {
            Context applicationContext = this.zzba.zzaj.getApplicationContext();
            SharedPreferences sharedPreferences = applicationContext.getSharedPreferences("com.google.firebase.messaging", 0);
            if (sharedPreferences.contains("auto_init")) {
                return Boolean.valueOf(sharedPreferences.getBoolean("auto_init", false));
            }
            try {
                PackageManager packageManager = applicationContext.getPackageManager();
                if (packageManager != null) {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(applicationContext.getPackageName(), 128);
                    if (!(applicationInfo == null || applicationInfo.metaData == null || !applicationInfo.metaData.containsKey("firebase_messaging_auto_init_enabled"))) {
                        return Boolean.valueOf(applicationInfo.metaData.getBoolean("firebase_messaging_auto_init_enabled"));
                    }
                }
            } catch (NameNotFoundException e) {
            }
            return null;
        }

        private final boolean zzt() {
            try {
                Class.forName("com.google.firebase.messaging.FirebaseMessaging");
                return true;
            } catch (ClassNotFoundException e) {
                Context applicationContext = this.zzba.zzaj.getApplicationContext();
                Intent intent = new Intent("com.google.firebase.MESSAGING_EVENT");
                intent.setPackage(applicationContext.getPackageName());
                ResolveInfo resolveService = applicationContext.getPackageManager().resolveService(intent, 0);
                if (resolveService == null || resolveService.serviceInfo == null) {
                    return false;
                }
                return true;
            }
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

    FirebaseInstanceId(FirebaseApp firebaseApp, Subscriber subscriber) {
        this(firebaseApp, new zzam(firebaseApp.getApplicationContext()), zzi.zze(), zzi.zze(), subscriber);
    }

    private FirebaseInstanceId(FirebaseApp firebaseApp, zzam com_google_firebase_iid_zzam, Executor executor, Executor executor2, Subscriber subscriber) {
        this.zzam = new zzap();
        this.zzao = false;
        if (zzam.zza(firebaseApp) == null) {
            throw new IllegalStateException("FirebaseInstanceId failed to initialize, FirebaseApp is missing project ID");
        }
        synchronized (FirebaseInstanceId.class) {
            if (zzag == null) {
                zzag = new zzav(firebaseApp.getApplicationContext());
            }
        }
        this.zzaj = firebaseApp;
        this.zzak = com_google_firebase_iid_zzam;
        if (this.zzal == null) {
            MessagingChannel messagingChannel = (MessagingChannel) firebaseApp.get(MessagingChannel.class);
            if (messagingChannel == null || !messagingChannel.isAvailable()) {
                this.zzal = new zzq(firebaseApp, com_google_firebase_iid_zzam, executor);
            } else {
                this.zzal = messagingChannel;
            }
        }
        this.zzal = this.zzal;
        this.zzai = executor2;
        this.zzan = new zzaz(zzag);
        this.zzap = new zza(this, subscriber);
        if (this.zzap.isEnabled()) {
            zzf();
        }
    }

    private final void zzf() {
        zzaw zzi = zzi();
        if (!zzn() || zzi == null || zzi.zzj(this.zzak.zzac()) || this.zzan.zzap()) {
            startSync();
        }
    }

    final FirebaseApp zzg() {
        return this.zzaj;
    }

    final synchronized void zza(boolean z) {
        this.zzao = z;
    }

    private final synchronized void startSync() {
        if (!this.zzao) {
            zza(0);
        }
    }

    final synchronized void zza(long j) {
        zza(new zzax(this, this.zzak, this.zzan, Math.min(Math.max(30, j << 1), zzaf)), j);
        this.zzao = true;
    }

    static void zza(Runnable runnable, long j) {
        synchronized (FirebaseInstanceId.class) {
            if (zzah == null) {
                zzah = new ScheduledThreadPoolExecutor(1);
            }
            zzah.schedule(runnable, j, TimeUnit.SECONDS);
        }
    }

    public String getId() {
        zzf();
        return zzh();
    }

    private static String zzh() {
        return zzam.zza(zzag.zzg(TtmlNode.ANONYMOUS_REGION_ID).getKeyPair());
    }

    private final Task<InstanceIdResult> zza(String str, String str2) {
        String zzd = zzd(str2);
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzai.execute(new zzm(this, str, str2, taskCompletionSource, zzd));
        return taskCompletionSource.getTask();
    }

    @Deprecated
    public String getToken() {
        zzaw zzi = zzi();
        if (zzi == null || zzi.zzj(this.zzak.zzac())) {
            startSync();
        }
        return zzi != null ? zzi.zzbn : null;
    }

    final zzaw zzi() {
        return zzb(zzam.zza(this.zzaj), "*");
    }

    private static zzaw zzb(String str, String str2) {
        return zzag.zzb(TtmlNode.ANONYMOUS_REGION_ID, str, str2);
    }

    final String zzj() throws IOException {
        return getToken(zzam.zza(this.zzaj), "*");
    }

    public String getToken(String str, String str2) throws IOException {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            return ((InstanceIdResult) zza(zza(str, str2))).getToken();
        }
        throw new IOException("MAIN_THREAD");
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

    final void zzb(String str) throws IOException {
        zzaw zzi = zzi();
        if (zzi == null || zzi.zzj(this.zzak.zzac())) {
            throw new IOException("token not available");
        }
        zza(this.zzal.subscribeToTopic(zzh(), zzi.zzbn, str));
    }

    final void zzc(String str) throws IOException {
        zzaw zzi = zzi();
        if (zzi == null || zzi.zzj(this.zzak.zzac())) {
            throw new IOException("token not available");
        }
        zza(this.zzal.unsubscribeFromTopic(zzh(), zzi.zzbn, str));
    }

    static boolean zzk() {
        return Log.isLoggable("FirebaseInstanceId", 3) || (VERSION.SDK_INT == 23 && Log.isLoggable("FirebaseInstanceId", 3));
    }

    final synchronized void zzl() {
        zzag.zzak();
        if (this.zzap.isEnabled()) {
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
        zza(this.zzal.buildChannel(zzh(), zzaw.zza(zzi())));
    }

    final void zzp() {
        zzag.zzh(TtmlNode.ANONYMOUS_REGION_ID);
        startSync();
    }

    private static String zzd(String str) {
        if (str.isEmpty() || str.equalsIgnoreCase("fcm") || str.equalsIgnoreCase("gcm")) {
            return "*";
        }
        return str;
    }

    final /* synthetic */ void zza(String str, String str2, TaskCompletionSource taskCompletionSource, String str3) {
        String zzh = zzh();
        zzaw zzb = zzb(str, str2);
        if (zzb == null || zzb.zzj(this.zzak.zzac())) {
            this.zzam.zza(str, str3, new zzn(this, zzh, zzaw.zza(zzb), str, str3)).addOnCompleteListener(this.zzai, new zzo(this, str, str3, taskCompletionSource, zzh));
            return;
        }
        taskCompletionSource.setResult(new zzw(zzh, zzb.zzbn));
    }

    final /* synthetic */ void zza(String str, String str2, TaskCompletionSource taskCompletionSource, String str3, Task task) {
        if (task.isSuccessful()) {
            String str4 = (String) task.getResult();
            zzag.zza(TtmlNode.ANONYMOUS_REGION_ID, str, str2, str4, this.zzak.zzac());
            taskCompletionSource.setResult(new zzw(str3, str4));
            return;
        }
        taskCompletionSource.setException(task.getException());
    }

    final /* synthetic */ Task zza(String str, String str2, String str3, String str4) {
        return this.zzal.getToken(str, str2, str3, str4);
    }
}
