package com.google.firebase.remoteconfig;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.wrappers.Wrappers;
import com.google.android.gms.internal.config.zzal;
import com.google.android.gms.internal.config.zzam;
import com.google.android.gms.internal.config.zzan;
import com.google.android.gms.internal.config.zzao;
import com.google.android.gms.internal.config.zzaq;
import com.google.android.gms.internal.config.zzar;
import com.google.android.gms.internal.config.zzas;
import com.google.android.gms.internal.config.zzat;
import com.google.android.gms.internal.config.zzau;
import com.google.android.gms.internal.config.zzav;
import com.google.android.gms.internal.config.zzaw;
import com.google.android.gms.internal.config.zzax;
import com.google.android.gms.internal.config.zzay;
import com.google.android.gms.internal.config.zzbh;
import com.google.android.gms.internal.config.zze;
import com.google.android.gms.internal.config.zzj;
import com.google.android.gms.internal.config.zzk;
import com.google.android.gms.internal.config.zzv;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.abt.FirebaseABTesting;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.concurrent.GuardedBy;
import org.telegram.tgnet.ConnectionsManager;

public class FirebaseRemoteConfig {
    public static final byte[] DEFAULT_VALUE_FOR_BYTE_ARRAY = new byte[0];
    @GuardedBy("FirebaseRemoteConfig.class")
    private static FirebaseRemoteConfig zzaf;
    private final Context mContext;
    private zzao zzag;
    private zzao zzah;
    private zzao zzai;
    private zzar zzaj;
    private final FirebaseApp zzak;
    private final ReadWriteLock zzal = new ReentrantReadWriteLock(true);
    private final FirebaseABTesting zzam;

    private FirebaseRemoteConfig(Context context, zzao com_google_android_gms_internal_config_zzao, zzao com_google_android_gms_internal_config_zzao2, zzao com_google_android_gms_internal_config_zzao3, zzar com_google_android_gms_internal_config_zzar) {
        this.mContext = context;
        if (com_google_android_gms_internal_config_zzar == null) {
            com_google_android_gms_internal_config_zzar = new zzar();
        }
        this.zzaj = com_google_android_gms_internal_config_zzar;
        this.zzaj.zzc(zzd(this.mContext));
        this.zzag = com_google_android_gms_internal_config_zzao;
        this.zzah = com_google_android_gms_internal_config_zzao2;
        this.zzai = com_google_android_gms_internal_config_zzao3;
        this.zzak = FirebaseApp.initializeApp(this.mContext);
        this.zzam = zzf(this.mContext);
    }

    public static FirebaseRemoteConfig getInstance() {
        return zzc(FirebaseApp.getInstance().getApplicationContext());
    }

    private static zzao zza(zzas com_google_android_gms_internal_config_zzas) {
        int i = 0;
        if (com_google_android_gms_internal_config_zzas == null) {
            return null;
        }
        Map hashMap = new HashMap();
        for (zzav com_google_android_gms_internal_config_zzav : com_google_android_gms_internal_config_zzas.zzbf) {
            String str = com_google_android_gms_internal_config_zzav.namespace;
            Map hashMap2 = new HashMap();
            for (zzat com_google_android_gms_internal_config_zzat : com_google_android_gms_internal_config_zzav.zzbo) {
                hashMap2.put(com_google_android_gms_internal_config_zzat.zzbi, com_google_android_gms_internal_config_zzat.zzbj);
            }
            hashMap.put(str, hashMap2);
        }
        byte[][] bArr = com_google_android_gms_internal_config_zzas.zzbg;
        List arrayList = new ArrayList();
        int length = bArr.length;
        while (i < length) {
            arrayList.add(bArr[i]);
            i++;
        }
        return new zzao(hashMap, com_google_android_gms_internal_config_zzas.timestamp, arrayList);
    }

    private final Task<Void> zza(long j, zzv com_google_android_gms_internal_config_zzv) {
        int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzal.readLock().lock();
        try {
            long convert;
            zzj com_google_android_gms_internal_config_zzj = new zzj();
            com_google_android_gms_internal_config_zzj.zza(j);
            if (this.zzak != null) {
                com_google_android_gms_internal_config_zzj.zza(this.zzak.getOptions().getApplicationId());
            }
            if (this.zzaj.isDeveloperModeEnabled()) {
                com_google_android_gms_internal_config_zzj.zza("_rcn_developer", "true");
            }
            com_google_android_gms_internal_config_zzj.zza(10300);
            if (!(this.zzah == null || this.zzah.getTimestamp() == -1)) {
                convert = TimeUnit.SECONDS.convert(System.currentTimeMillis() - this.zzah.getTimestamp(), TimeUnit.MILLISECONDS);
                com_google_android_gms_internal_config_zzj.zzc(convert < 2147483647L ? (int) convert : ConnectionsManager.DEFAULT_DATACENTER_ID);
            }
            if (!(this.zzag == null || this.zzag.getTimestamp() == -1)) {
                convert = TimeUnit.SECONDS.convert(System.currentTimeMillis() - this.zzag.getTimestamp(), TimeUnit.MILLISECONDS);
                if (convert < 2147483647L) {
                    i = (int) convert;
                }
                com_google_android_gms_internal_config_zzj.zzb(i);
            }
            zze.zze.zza(com_google_android_gms_internal_config_zzv.asGoogleApiClient(), com_google_android_gms_internal_config_zzj.zzf()).setResultCallback(new zza(this, taskCompletionSource));
            return taskCompletionSource.getTask();
        } finally {
            this.zzal.readLock().unlock();
        }
    }

    private final void zza(TaskCompletionSource<Void> taskCompletionSource, Status status) {
        if (status == null) {
            Log.w("FirebaseRemoteConfig", "Received null IPC status for failure.");
        } else {
            int statusCode = status.getStatusCode();
            String statusMessage = status.getStatusMessage();
            Log.w("FirebaseRemoteConfig", new StringBuilder(String.valueOf(statusMessage).length() + 25).append("IPC failure: ").append(statusCode).append(":").append(statusMessage).toString());
        }
        this.zzal.writeLock().lock();
        try {
            this.zzaj.zzf(1);
            taskCompletionSource.setException(new FirebaseRemoteConfigFetchException());
            zzn();
        } finally {
            this.zzal.writeLock().unlock();
        }
    }

    private static void zza(Runnable runnable) {
        AsyncTask.SERIAL_EXECUTOR.execute(runnable);
    }

    private static FirebaseRemoteConfig zzc(Context context) {
        synchronized (FirebaseRemoteConfig.class) {
            if (zzaf == null) {
                zzao zza;
                zzao zza2;
                zzao zza3;
                zzar com_google_android_gms_internal_config_zzar;
                zzaw zze = zze(context);
                if (zze != null) {
                    if (Log.isLoggable("FirebaseRemoteConfig", 3)) {
                        Log.d("FirebaseRemoteConfig", "Initializing from persisted config.");
                    }
                    zza = zza(zze.zzbp);
                    zza2 = zza(zze.zzbq);
                    zza3 = zza(zze.zzbr);
                    zzau com_google_android_gms_internal_config_zzau = zze.zzbs;
                    if (com_google_android_gms_internal_config_zzau == null) {
                        com_google_android_gms_internal_config_zzar = null;
                    } else {
                        com_google_android_gms_internal_config_zzar = new zzar();
                        com_google_android_gms_internal_config_zzar.zzf(com_google_android_gms_internal_config_zzau.zzbk);
                        com_google_android_gms_internal_config_zzar.zza(com_google_android_gms_internal_config_zzau.zzbl);
                    }
                    if (com_google_android_gms_internal_config_zzar != null) {
                        zzax[] com_google_android_gms_internal_config_zzaxArr = zze.zzbt;
                        Map hashMap = new HashMap();
                        if (com_google_android_gms_internal_config_zzaxArr != null) {
                            for (zzax com_google_android_gms_internal_config_zzax : com_google_android_gms_internal_config_zzaxArr) {
                                hashMap.put(com_google_android_gms_internal_config_zzax.namespace, new zzal(com_google_android_gms_internal_config_zzax.resourceId, com_google_android_gms_internal_config_zzax.zzbv));
                            }
                        }
                        com_google_android_gms_internal_config_zzar.zza(hashMap);
                    }
                } else if (Log.isLoggable("FirebaseRemoteConfig", 3)) {
                    Log.d("FirebaseRemoteConfig", "No persisted config was found. Initializing from scratch.");
                    com_google_android_gms_internal_config_zzar = null;
                    zza3 = null;
                    zza2 = null;
                    zza = null;
                } else {
                    com_google_android_gms_internal_config_zzar = null;
                    zza3 = null;
                    zza2 = null;
                    zza = null;
                }
                zzaf = new FirebaseRemoteConfig(context, zza, zza2, zza3, com_google_android_gms_internal_config_zzar);
            }
        }
        return zzaf;
    }

    private final long zzd(Context context) {
        long j = 0;
        try {
            return Wrappers.packageManager(this.mContext).getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
        } catch (NameNotFoundException e) {
            String packageName = context.getPackageName();
            Log.e("FirebaseRemoteConfig", new StringBuilder(String.valueOf(packageName).length() + 25).append("Package [").append(packageName).append("] was not found!").toString());
            return j;
        }
    }

    private static zzaw zze(Context context) {
        Throwable e;
        if (context == null) {
            return null;
        }
        FileInputStream openFileInput;
        try {
            openFileInput = context.openFileInput("persisted_config");
            try {
                OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = openFileInput.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byte[] toByteArray = byteArrayOutputStream.toByteArray();
                zzay zza = zzay.zza(toByteArray, 0, toByteArray.length);
                zzbh com_google_android_gms_internal_config_zzaw = new zzaw();
                com_google_android_gms_internal_config_zzaw.zza(zza);
                if (openFileInput != null) {
                    try {
                        openFileInput.close();
                    } catch (Throwable e2) {
                        Log.e("FirebaseRemoteConfig", "Failed to close persisted config file.", e2);
                    }
                }
                return com_google_android_gms_internal_config_zzaw;
            } catch (FileNotFoundException e3) {
                e = e3;
                try {
                    if (Log.isLoggable("FirebaseRemoteConfig", 3)) {
                        Log.d("FirebaseRemoteConfig", "Persisted config file was not found.", e);
                    }
                    if (openFileInput != null) {
                        return null;
                    }
                    try {
                        openFileInput.close();
                        return null;
                    } catch (Throwable e4) {
                        Log.e("FirebaseRemoteConfig", "Failed to close persisted config file.", e4);
                        return null;
                    }
                } catch (Throwable e22) {
                    e4 = e22;
                    if (openFileInput != null) {
                        try {
                            openFileInput.close();
                        } catch (Throwable e222) {
                            Log.e("FirebaseRemoteConfig", "Failed to close persisted config file.", e222);
                        }
                    }
                    throw e4;
                }
            } catch (IOException e5) {
                e4 = e5;
                Log.e("FirebaseRemoteConfig", "Cannot initialize from persisted config.", e4);
                if (openFileInput != null) {
                    return null;
                }
                try {
                    openFileInput.close();
                    return null;
                } catch (Throwable e42) {
                    Log.e("FirebaseRemoteConfig", "Failed to close persisted config file.", e42);
                    return null;
                }
            }
        } catch (FileNotFoundException e6) {
            e42 = e6;
            openFileInput = null;
            if (Log.isLoggable("FirebaseRemoteConfig", 3)) {
                Log.d("FirebaseRemoteConfig", "Persisted config file was not found.", e42);
            }
            if (openFileInput != null) {
                return null;
            }
            openFileInput.close();
            return null;
        } catch (IOException e7) {
            e42 = e7;
            openFileInput = null;
            Log.e("FirebaseRemoteConfig", "Cannot initialize from persisted config.", e42);
            if (openFileInput != null) {
                return null;
            }
            openFileInput.close();
            return null;
        } catch (Throwable th) {
            e42 = th;
            openFileInput = null;
            if (openFileInput != null) {
                openFileInput.close();
            }
            throw e42;
        }
    }

    private static FirebaseABTesting zzf(Context context) {
        try {
            return new FirebaseABTesting(context, "frc", 1);
        } catch (NoClassDefFoundError e) {
            Log.w("FirebaseRemoteConfig", "Unable to use ABT: Analytics library is missing.");
            return null;
        }
    }

    private final void zzn() {
        this.zzal.readLock().lock();
        try {
            zza(new zzan(this.mContext, this.zzag, this.zzah, this.zzai, this.zzaj));
        } finally {
            this.zzal.readLock().unlock();
        }
    }

    public boolean activateFetched() {
        this.zzal.writeLock().lock();
        try {
            if (this.zzag == null) {
                return false;
            }
            if (this.zzah == null || this.zzah.getTimestamp() < this.zzag.getTimestamp()) {
                long timestamp = this.zzag.getTimestamp();
                this.zzah = this.zzag;
                this.zzah.setTimestamp(System.currentTimeMillis());
                this.zzag = new zzao(null, timestamp, null);
                zza(new zzam(this.zzam, this.zzah.zzg()));
                zzn();
                this.zzal.writeLock().unlock();
                return true;
            }
            this.zzal.writeLock().unlock();
            return false;
        } finally {
            this.zzal.writeLock().unlock();
        }
    }

    public Task<Void> fetch(long j) {
        return zza(j, new zzv(this.mContext));
    }

    public String getString(String str) {
        return getString(str, "configns:firebase");
    }

    public String getString(String str, String str2) {
        if (str2 == null) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        this.zzal.readLock().lock();
        String str3;
        if (this.zzah == null || !this.zzah.zzb(str, str2)) {
            try {
                if (this.zzai == null || !this.zzai.zzb(str, str2)) {
                    str3 = TtmlNode.ANONYMOUS_REGION_ID;
                    this.zzal.readLock().unlock();
                    return str3;
                }
                str3 = new String(this.zzai.zzc(str, str2), zzaq.UTF_8);
                this.zzal.readLock().unlock();
                return str3;
            } finally {
                this.zzal.readLock().unlock();
            }
        } else {
            str3 = new String(this.zzah.zzc(str, str2), zzaq.UTF_8);
            return str3;
        }
    }

    public void setConfigSettings(FirebaseRemoteConfigSettings firebaseRemoteConfigSettings) {
        this.zzal.writeLock().lock();
        try {
            boolean isDeveloperModeEnabled = this.zzaj.isDeveloperModeEnabled();
            boolean isDeveloperModeEnabled2 = firebaseRemoteConfigSettings == null ? false : firebaseRemoteConfigSettings.isDeveloperModeEnabled();
            this.zzaj.zza(isDeveloperModeEnabled2);
            if (isDeveloperModeEnabled != isDeveloperModeEnabled2) {
                zzn();
            }
            this.zzal.writeLock().unlock();
        } catch (Throwable th) {
            this.zzal.writeLock().unlock();
        }
    }

    final void zza(TaskCompletionSource<Void> taskCompletionSource, zzk com_google_android_gms_internal_config_zzk) {
        if (com_google_android_gms_internal_config_zzk == null || com_google_android_gms_internal_config_zzk.getStatus() == null) {
            zza((TaskCompletionSource) taskCompletionSource, null);
            return;
        }
        int statusCode = com_google_android_gms_internal_config_zzk.getStatus().getStatusCode();
        this.zzal.writeLock().lock();
        Map zzh;
        Map hashMap;
        Map hashMap2;
        switch (statusCode) {
            case -6508:
            case -6506:
                this.zzaj.zzf(-1);
                if (!(this.zzag == null || this.zzag.zzq())) {
                    zzh = com_google_android_gms_internal_config_zzk.zzh();
                    hashMap = new HashMap();
                    for (String str : zzh.keySet()) {
                        hashMap2 = new HashMap();
                        for (String str2 : (Set) zzh.get(str)) {
                            hashMap2.put(str2, com_google_android_gms_internal_config_zzk.zza(str2, null, str));
                        }
                        hashMap.put(str, hashMap2);
                    }
                    this.zzag = new zzao(hashMap, this.zzag.getTimestamp(), com_google_android_gms_internal_config_zzk.zzg());
                }
                taskCompletionSource.setResult(null);
                zzn();
                break;
            case -6505:
                zzh = com_google_android_gms_internal_config_zzk.zzh();
                hashMap = new HashMap();
                for (String str3 : zzh.keySet()) {
                    hashMap2 = new HashMap();
                    for (String str22 : (Set) zzh.get(str3)) {
                        hashMap2.put(str22, com_google_android_gms_internal_config_zzk.zza(str22, null, str3));
                    }
                    hashMap.put(str3, hashMap2);
                }
                this.zzag = new zzao(hashMap, System.currentTimeMillis(), com_google_android_gms_internal_config_zzk.zzg());
                this.zzaj.zzf(-1);
                taskCompletionSource.setResult(null);
                zzn();
                break;
            case 6500:
            case 6501:
            case 6503:
            case 6504:
                zza((TaskCompletionSource) taskCompletionSource, com_google_android_gms_internal_config_zzk.getStatus());
                break;
            case 6502:
            case 6507:
                this.zzaj.zzf(2);
                taskCompletionSource.setException(new FirebaseRemoteConfigFetchThrottledException(com_google_android_gms_internal_config_zzk.getThrottleEndTimeMillis()));
                zzn();
                break;
            default:
                try {
                    if (com_google_android_gms_internal_config_zzk.getStatus().isSuccess()) {
                        Log.w("FirebaseRemoteConfig", "Unknown (successful) status code: " + statusCode);
                    }
                    zza((TaskCompletionSource) taskCompletionSource, com_google_android_gms_internal_config_zzk.getStatus());
                    break;
                } catch (Throwable th) {
                    this.zzal.writeLock().unlock();
                }
        }
        this.zzal.writeLock().unlock();
    }
}
