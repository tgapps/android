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
        if (com_google_android_gms_internal_config_zzas == null) {
            return null;
        }
        Map hashMap = new HashMap();
        int i = 0;
        for (zzav com_google_android_gms_internal_config_zzav : com_google_android_gms_internal_config_zzas.zzbg) {
            String str = com_google_android_gms_internal_config_zzav.namespace;
            Map hashMap2 = new HashMap();
            for (zzat com_google_android_gms_internal_config_zzat : com_google_android_gms_internal_config_zzav.zzbp) {
                hashMap2.put(com_google_android_gms_internal_config_zzat.zzbj, com_google_android_gms_internal_config_zzat.zzbk);
            }
            hashMap.put(str, hashMap2);
        }
        byte[][] bArr = com_google_android_gms_internal_config_zzas.zzbh;
        List arrayList = new ArrayList();
        int length = bArr.length;
        while (i < length) {
            arrayList.add(bArr[i]);
            i++;
        }
        return new zzao(hashMap, com_google_android_gms_internal_config_zzas.timestamp, arrayList);
    }

    private final Task<Void> zza(long j, zzv com_google_android_gms_internal_config_zzv) {
        TaskCompletionSource taskCompletionSource = new TaskCompletionSource();
        this.zzal.readLock().lock();
        try {
            zzj com_google_android_gms_internal_config_zzj = new zzj();
            com_google_android_gms_internal_config_zzj.zza(j);
            if (this.zzak != null) {
                com_google_android_gms_internal_config_zzj.zza(this.zzak.getOptions().getApplicationId());
            }
            if (this.zzaj.isDeveloperModeEnabled()) {
                com_google_android_gms_internal_config_zzj.zza("_rcn_developer", "true");
            }
            com_google_android_gms_internal_config_zzj.zza(10300);
            zzao com_google_android_gms_internal_config_zzao = this.zzah;
            int i = ConnectionsManager.DEFAULT_DATACENTER_ID;
            if (!(com_google_android_gms_internal_config_zzao == null || this.zzah.getTimestamp() == -1)) {
                long convert = TimeUnit.SECONDS.convert(System.currentTimeMillis() - this.zzah.getTimestamp(), TimeUnit.MILLISECONDS);
                com_google_android_gms_internal_config_zzj.zzc(convert < 2147483647L ? (int) convert : ConnectionsManager.DEFAULT_DATACENTER_ID);
            }
            if (!(this.zzag == null || this.zzag.getTimestamp() == -1)) {
                long convert2 = TimeUnit.SECONDS.convert(System.currentTimeMillis() - this.zzag.getTimestamp(), TimeUnit.MILLISECONDS);
                if (convert2 < 2147483647L) {
                    i = (int) convert2;
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
            StringBuilder stringBuilder = new StringBuilder(25 + String.valueOf(statusMessage).length());
            stringBuilder.append("IPC failure: ");
            stringBuilder.append(statusCode);
            stringBuilder.append(":");
            stringBuilder.append(statusMessage);
            Log.w("FirebaseRemoteConfig", stringBuilder.toString());
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
                zzar com_google_android_gms_internal_config_zzar;
                zzao com_google_android_gms_internal_config_zzao;
                zzao com_google_android_gms_internal_config_zzao2;
                zzar com_google_android_gms_internal_config_zzar2 = new zzar();
                zzaw zze = zze(context);
                zzao com_google_android_gms_internal_config_zzao3 = null;
                if (zze == null) {
                    if (Log.isLoggable("FirebaseRemoteConfig", 3)) {
                        Log.d("FirebaseRemoteConfig", "No persisted config was found. Initializing from scratch.");
                    }
                    com_google_android_gms_internal_config_zzar = com_google_android_gms_internal_config_zzar2;
                    com_google_android_gms_internal_config_zzao = null;
                    com_google_android_gms_internal_config_zzao2 = com_google_android_gms_internal_config_zzao;
                } else {
                    zzar com_google_android_gms_internal_config_zzar3;
                    if (Log.isLoggable("FirebaseRemoteConfig", 3)) {
                        Log.d("FirebaseRemoteConfig", "Initializing from persisted config.");
                    }
                    zzao zza = zza(zze.zzbq);
                    com_google_android_gms_internal_config_zzao = zza(zze.zzbr);
                    com_google_android_gms_internal_config_zzao2 = zza(zze.zzbs);
                    zzau com_google_android_gms_internal_config_zzau = zze.zzbt;
                    if (com_google_android_gms_internal_config_zzau != null) {
                        com_google_android_gms_internal_config_zzar3 = new zzar();
                        com_google_android_gms_internal_config_zzar3.zzf(com_google_android_gms_internal_config_zzau.zzbl);
                        com_google_android_gms_internal_config_zzar3.zza(com_google_android_gms_internal_config_zzau.zzbm);
                        com_google_android_gms_internal_config_zzar3.zzd(com_google_android_gms_internal_config_zzau.zzbn);
                    }
                    if (com_google_android_gms_internal_config_zzar3 != null) {
                        zzax[] com_google_android_gms_internal_config_zzaxArr = zze.zzbu;
                        Map hashMap = new HashMap();
                        if (com_google_android_gms_internal_config_zzaxArr != null) {
                            for (zzax com_google_android_gms_internal_config_zzax : com_google_android_gms_internal_config_zzaxArr) {
                                hashMap.put(com_google_android_gms_internal_config_zzax.namespace, new zzal(com_google_android_gms_internal_config_zzax.resourceId, com_google_android_gms_internal_config_zzax.zzbw));
                            }
                        }
                        com_google_android_gms_internal_config_zzar3.zza(hashMap);
                    }
                    com_google_android_gms_internal_config_zzar = com_google_android_gms_internal_config_zzar3;
                    com_google_android_gms_internal_config_zzao3 = com_google_android_gms_internal_config_zzao;
                    com_google_android_gms_internal_config_zzao = zza;
                }
                zzaf = new FirebaseRemoteConfig(context, com_google_android_gms_internal_config_zzao, com_google_android_gms_internal_config_zzao3, com_google_android_gms_internal_config_zzao2, com_google_android_gms_internal_config_zzar);
            }
        }
        return zzaf;
    }

    private final long zzd(Context context) {
        try {
            return Wrappers.packageManager(this.mContext).getPackageInfo(context.getPackageName(), 0).lastUpdateTime;
        } catch (NameNotFoundException e) {
            String packageName = context.getPackageName();
            StringBuilder stringBuilder = new StringBuilder(25 + String.valueOf(packageName).length());
            stringBuilder.append("Package [");
            stringBuilder.append(packageName);
            stringBuilder.append("] was not found!");
            Log.e("FirebaseRemoteConfig", stringBuilder.toString());
            return 0;
        }
    }

    private static zzaw zze(Context context) {
        Throwable e;
        Throwable th;
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
                        return com_google_android_gms_internal_config_zzaw;
                    } catch (Throwable e2) {
                        Log.e("FirebaseRemoteConfig", "Failed to close persisted config file.", e2);
                    }
                }
                return com_google_android_gms_internal_config_zzaw;
            } catch (FileNotFoundException e3) {
                e = e3;
                if (Log.isLoggable("FirebaseRemoteConfig", 3)) {
                    Log.d("FirebaseRemoteConfig", "Persisted config file was not found.", e);
                }
                if (openFileInput != null) {
                    try {
                        openFileInput.close();
                        return null;
                    } catch (Throwable e22) {
                        Log.e("FirebaseRemoteConfig", "Failed to close persisted config file.", e22);
                        return null;
                    }
                }
                return null;
            } catch (IOException e4) {
                e = e4;
                try {
                    Log.e("FirebaseRemoteConfig", "Cannot initialize from persisted config.", e);
                    if (openFileInput != null) {
                        try {
                            openFileInput.close();
                            return null;
                        } catch (Throwable e222) {
                            Log.e("FirebaseRemoteConfig", "Failed to close persisted config file.", e222);
                            return null;
                        }
                    }
                    return null;
                } catch (Throwable th2) {
                    th = th2;
                    if (openFileInput != null) {
                        try {
                            openFileInput.close();
                        } catch (Throwable e2222) {
                            Log.e("FirebaseRemoteConfig", "Failed to close persisted config file.", e2222);
                        }
                    }
                    throw th;
                }
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            openFileInput = null;
            if (Log.isLoggable("FirebaseRemoteConfig", 3)) {
                Log.d("FirebaseRemoteConfig", "Persisted config file was not found.", e);
            }
            if (openFileInput != null) {
                openFileInput.close();
                return null;
            }
            return null;
        } catch (IOException e6) {
            e = e6;
            openFileInput = null;
            Log.e("FirebaseRemoteConfig", "Cannot initialize from persisted config.", e);
            if (openFileInput != null) {
                openFileInput.close();
                return null;
            }
            return null;
        } catch (Throwable e22222) {
            th = e22222;
            openFileInput = null;
            if (openFileInput != null) {
                openFileInput.close();
            }
            throw th;
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
            if (this.zzag != null) {
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
            }
            this.zzal.writeLock().unlock();
            return false;
        } catch (Throwable th) {
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
        try {
            String str3;
            if (this.zzah != null && this.zzah.zzb(str, str2)) {
                str3 = new String(this.zzah.zzc(str, str2), zzaq.UTF_8);
            } else if (this.zzai == null || !this.zzai.zzb(str, str2)) {
                str = TtmlNode.ANONYMOUS_REGION_ID;
                this.zzal.readLock().unlock();
                return str;
            } else {
                str3 = new String(this.zzai.zzc(str, str2), zzaq.UTF_8);
            }
            this.zzal.readLock().unlock();
            return str3;
        } catch (Throwable th) {
            this.zzal.readLock().unlock();
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final void zza(com.google.android.gms.tasks.TaskCompletionSource<java.lang.Void> r11, com.google.android.gms.internal.config.zzk r12) {
        /*
        r10 = this;
        r0 = 0;
        if (r12 == 0) goto L_0x0150;
    L_0x0003:
        r1 = r12.getStatus();
        if (r1 != 0) goto L_0x000b;
    L_0x0009:
        goto L_0x0150;
    L_0x000b:
        r1 = r12.getStatus();
        r1 = r1.getStatusCode();
        r2 = r10.zzal;
        r2 = r2.writeLock();
        r2.lock();
        r2 = -6508; // 0xffffffffffffe694 float:NaN double:NaN;
        r3 = -1;
        if (r1 == r2) goto L_0x00d1;
    L_0x0021:
        r2 = 6507; // 0x196b float:9.118E-42 double:3.215E-320;
        if (r1 == r2) goto L_0x00be;
    L_0x0025:
        switch(r1) {
            case -6506: goto L_0x00d1;
            case -6505: goto L_0x005e;
            default: goto L_0x0028;
        };
    L_0x0028:
        switch(r1) {
            case 6500: goto L_0x0059;
            case 6501: goto L_0x0059;
            case 6502: goto L_0x00be;
            case 6503: goto L_0x0059;
            case 6504: goto L_0x0059;
            default: goto L_0x002b;
        };
    L_0x002b:
        r0 = r12.getStatus();	 Catch:{ all -> 0x0056 }
        r0 = r0.isSuccess();	 Catch:{ all -> 0x0056 }
        if (r0 == 0) goto L_0x004d;
    L_0x0035:
        r0 = "FirebaseRemoteConfig";
        r2 = 45;
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0056 }
        r3.<init>(r2);	 Catch:{ all -> 0x0056 }
        r2 = "Unknown (successful) status code: ";
        r3.append(r2);	 Catch:{ all -> 0x0056 }
        r3.append(r1);	 Catch:{ all -> 0x0056 }
        r1 = r3.toString();	 Catch:{ all -> 0x0056 }
        android.util.Log.w(r0, r1);	 Catch:{ all -> 0x0056 }
    L_0x004d:
        r12 = r12.getStatus();	 Catch:{ all -> 0x0056 }
    L_0x0051:
        r10.zza(r11, r12);	 Catch:{ all -> 0x0056 }
        goto L_0x013c;
    L_0x0056:
        r11 = move-exception;
        goto L_0x0146;
    L_0x0059:
        r12 = r12.getStatus();	 Catch:{ all -> 0x0056 }
        goto L_0x0051;
    L_0x005e:
        r1 = r12.zzh();	 Catch:{ all -> 0x0056 }
        r2 = new java.util.HashMap;	 Catch:{ all -> 0x0056 }
        r2.<init>();	 Catch:{ all -> 0x0056 }
        r4 = r1.keySet();	 Catch:{ all -> 0x0056 }
        r4 = r4.iterator();	 Catch:{ all -> 0x0056 }
    L_0x006f:
        r5 = r4.hasNext();	 Catch:{ all -> 0x0056 }
        if (r5 == 0) goto L_0x00a2;
    L_0x0075:
        r5 = r4.next();	 Catch:{ all -> 0x0056 }
        r5 = (java.lang.String) r5;	 Catch:{ all -> 0x0056 }
        r6 = new java.util.HashMap;	 Catch:{ all -> 0x0056 }
        r6.<init>();	 Catch:{ all -> 0x0056 }
        r7 = r1.get(r5);	 Catch:{ all -> 0x0056 }
        r7 = (java.util.Set) r7;	 Catch:{ all -> 0x0056 }
        r7 = r7.iterator();	 Catch:{ all -> 0x0056 }
    L_0x008a:
        r8 = r7.hasNext();	 Catch:{ all -> 0x0056 }
        if (r8 == 0) goto L_0x009e;
    L_0x0090:
        r8 = r7.next();	 Catch:{ all -> 0x0056 }
        r8 = (java.lang.String) r8;	 Catch:{ all -> 0x0056 }
        r9 = r12.zza(r8, r0, r5);	 Catch:{ all -> 0x0056 }
        r6.put(r8, r9);	 Catch:{ all -> 0x0056 }
        goto L_0x008a;
    L_0x009e:
        r2.put(r5, r6);	 Catch:{ all -> 0x0056 }
        goto L_0x006f;
    L_0x00a2:
        r1 = new com.google.android.gms.internal.config.zzao;	 Catch:{ all -> 0x0056 }
        r4 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0056 }
        r12 = r12.zzg();	 Catch:{ all -> 0x0056 }
        r1.<init>(r2, r4, r12);	 Catch:{ all -> 0x0056 }
        r10.zzag = r1;	 Catch:{ all -> 0x0056 }
        r12 = r10.zzaj;	 Catch:{ all -> 0x0056 }
        r12.zzf(r3);	 Catch:{ all -> 0x0056 }
        r11.setResult(r0);	 Catch:{ all -> 0x0056 }
    L_0x00b9:
        r10.zzn();	 Catch:{ all -> 0x0056 }
        goto L_0x013c;
    L_0x00be:
        r0 = r10.zzaj;	 Catch:{ all -> 0x0056 }
        r1 = 2;
        r0.zzf(r1);	 Catch:{ all -> 0x0056 }
        r0 = new com.google.firebase.remoteconfig.FirebaseRemoteConfigFetchThrottledException;	 Catch:{ all -> 0x0056 }
        r1 = r12.getThrottleEndTimeMillis();	 Catch:{ all -> 0x0056 }
        r0.<init>(r1);	 Catch:{ all -> 0x0056 }
        r11.setException(r0);	 Catch:{ all -> 0x0056 }
        goto L_0x00b9;
    L_0x00d1:
        r1 = r10.zzaj;	 Catch:{ all -> 0x0056 }
        r1.zzf(r3);	 Catch:{ all -> 0x0056 }
        r1 = r10.zzag;	 Catch:{ all -> 0x0056 }
        if (r1 == 0) goto L_0x0137;
    L_0x00da:
        r1 = r10.zzag;	 Catch:{ all -> 0x0056 }
        r1 = r1.zzq();	 Catch:{ all -> 0x0056 }
        if (r1 != 0) goto L_0x0137;
    L_0x00e2:
        r1 = r12.zzh();	 Catch:{ all -> 0x0056 }
        r2 = new java.util.HashMap;	 Catch:{ all -> 0x0056 }
        r2.<init>();	 Catch:{ all -> 0x0056 }
        r3 = r1.keySet();	 Catch:{ all -> 0x0056 }
        r3 = r3.iterator();	 Catch:{ all -> 0x0056 }
    L_0x00f3:
        r4 = r3.hasNext();	 Catch:{ all -> 0x0056 }
        if (r4 == 0) goto L_0x0126;
    L_0x00f9:
        r4 = r3.next();	 Catch:{ all -> 0x0056 }
        r4 = (java.lang.String) r4;	 Catch:{ all -> 0x0056 }
        r5 = new java.util.HashMap;	 Catch:{ all -> 0x0056 }
        r5.<init>();	 Catch:{ all -> 0x0056 }
        r6 = r1.get(r4);	 Catch:{ all -> 0x0056 }
        r6 = (java.util.Set) r6;	 Catch:{ all -> 0x0056 }
        r6 = r6.iterator();	 Catch:{ all -> 0x0056 }
    L_0x010e:
        r7 = r6.hasNext();	 Catch:{ all -> 0x0056 }
        if (r7 == 0) goto L_0x0122;
    L_0x0114:
        r7 = r6.next();	 Catch:{ all -> 0x0056 }
        r7 = (java.lang.String) r7;	 Catch:{ all -> 0x0056 }
        r8 = r12.zza(r7, r0, r4);	 Catch:{ all -> 0x0056 }
        r5.put(r7, r8);	 Catch:{ all -> 0x0056 }
        goto L_0x010e;
    L_0x0122:
        r2.put(r4, r5);	 Catch:{ all -> 0x0056 }
        goto L_0x00f3;
    L_0x0126:
        r1 = new com.google.android.gms.internal.config.zzao;	 Catch:{ all -> 0x0056 }
        r3 = r10.zzag;	 Catch:{ all -> 0x0056 }
        r3 = r3.getTimestamp();	 Catch:{ all -> 0x0056 }
        r12 = r12.zzg();	 Catch:{ all -> 0x0056 }
        r1.<init>(r2, r3, r12);	 Catch:{ all -> 0x0056 }
        r10.zzag = r1;	 Catch:{ all -> 0x0056 }
    L_0x0137:
        r11.setResult(r0);	 Catch:{ all -> 0x0056 }
        goto L_0x00b9;
    L_0x013c:
        r11 = r10.zzal;
        r11 = r11.writeLock();
        r11.unlock();
        return;
    L_0x0146:
        r12 = r10.zzal;
        r12 = r12.writeLock();
        r12.unlock();
        throw r11;
    L_0x0150:
        r10.zza(r11, r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.remoteconfig.FirebaseRemoteConfig.zza(com.google.android.gms.tasks.TaskCompletionSource, com.google.android.gms.internal.config.zzk):void");
    }
}
