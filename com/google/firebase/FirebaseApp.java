package com.google.firebase;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import com.google.android.gms.common.api.internal.BackgroundDetector;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.util.ProcessUtils;
import com.google.firebase.components.Component;
import com.google.firebase.components.zzc;
import com.google.firebase.components.zzg;
import com.google.firebase.internal.zzb;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.concurrent.GuardedBy;

public class FirebaseApp {
    private static final Object sLock = new Object();
    private static final List<String> zza = Arrays.asList(new String[]{"com.google.firebase.auth.FirebaseAuth", "com.google.firebase.iid.FirebaseInstanceId"});
    private static final List<String> zzb = Collections.singletonList("com.google.firebase.crash.FirebaseCrash");
    private static final List<String> zzc = Arrays.asList(new String[]{"com.google.android.gms.measurement.AppMeasurement"});
    private static final List<String> zzd = Arrays.asList(new String[0]);
    private static final Set<String> zze = Collections.emptySet();
    @GuardedBy("sLock")
    static final Map<String, FirebaseApp> zzf = new ArrayMap();
    private final String mName;
    private final Context zzg;
    private final FirebaseOptions zzh;
    private final zzg zzi;
    private final AtomicBoolean zzj = new AtomicBoolean(false);
    private final AtomicBoolean zzk = new AtomicBoolean();
    private final List<Object> zzl = new CopyOnWriteArrayList();
    private final List<BackgroundStateChangeListener> zzm = new CopyOnWriteArrayList();
    private final List<Object> zzn = new CopyOnWriteArrayList();
    private IdTokenListenersCountChangedListener zzp;

    public interface BackgroundStateChangeListener {
        void onBackgroundStateChanged(boolean z);
    }

    public interface IdTokenListenersCountChangedListener {
    }

    @TargetApi(24)
    private static class zza extends BroadcastReceiver {
        private static AtomicReference<zza> zzq = new AtomicReference();
        private final Context zzg;

        private zza(Context context) {
            this.zzg = context;
        }

        private static void zza(Context context) {
            if (zzq.get() == null) {
                BroadcastReceiver com_google_firebase_FirebaseApp_zza = new zza(context);
                if (zzq.compareAndSet(null, com_google_firebase_FirebaseApp_zza)) {
                    context.registerReceiver(com_google_firebase_FirebaseApp_zza, new IntentFilter("android.intent.action.USER_UNLOCKED"));
                }
            }
        }

        public final void onReceive(Context context, Intent intent) {
            synchronized (FirebaseApp.sLock) {
                for (FirebaseApp zza : FirebaseApp.zzf.values()) {
                    zza.zzc();
                }
            }
            this.zzg.unregisterReceiver(this);
        }
    }

    private FirebaseApp(Context context, String str, FirebaseOptions firebaseOptions) {
        this.zzg = (Context) Preconditions.checkNotNull(context);
        this.mName = Preconditions.checkNotEmpty(str);
        this.zzh = (FirebaseOptions) Preconditions.checkNotNull(firebaseOptions);
        this.zzp = new com.google.firebase.internal.zza();
        this.zzi = new zzg(new zzc(this.zzg).zzj(), Component.of(Context.class, this.zzg), Component.of(FirebaseApp.class, this), Component.of(FirebaseOptions.class, this.zzh));
    }

    public static FirebaseApp getInstance() {
        FirebaseApp firebaseApp;
        synchronized (sLock) {
            firebaseApp = (FirebaseApp) zzf.get("[DEFAULT]");
            if (firebaseApp == null) {
                String myProcessName = ProcessUtils.getMyProcessName();
                throw new IllegalStateException(new StringBuilder(String.valueOf(myProcessName).length() + 116).append("Default FirebaseApp is not initialized in this process ").append(myProcessName).append(". Make sure to call FirebaseApp.initializeApp(Context) first.").toString());
            }
        }
        return firebaseApp;
    }

    public static FirebaseApp initializeApp(Context context) {
        FirebaseApp instance;
        synchronized (sLock) {
            if (zzf.containsKey("[DEFAULT]")) {
                instance = getInstance();
            } else {
                FirebaseOptions fromResource = FirebaseOptions.fromResource(context);
                if (fromResource == null) {
                    instance = null;
                } else {
                    instance = initializeApp(context, fromResource);
                }
            }
        }
        return instance;
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions) {
        return initializeApp(context, firebaseOptions, "[DEFAULT]");
    }

    public static FirebaseApp initializeApp(Context context, FirebaseOptions firebaseOptions, String str) {
        FirebaseApp firebaseApp;
        zzb.zze(context);
        if (PlatformVersion.isAtLeastIceCreamSandwich() && (context.getApplicationContext() instanceof Application)) {
            BackgroundDetector.initialize((Application) context.getApplicationContext());
            BackgroundDetector.getInstance().addListener(new zza());
        }
        String trim = str.trim();
        if (context.getApplicationContext() != null) {
            context = context.getApplicationContext();
        }
        synchronized (sLock) {
            Preconditions.checkState(!zzf.containsKey(trim), new StringBuilder(String.valueOf(trim).length() + 33).append("FirebaseApp name ").append(trim).append(" already exists!").toString());
            Preconditions.checkNotNull(context, "Application context cannot be null.");
            firebaseApp = new FirebaseApp(context, trim, firebaseOptions);
            zzf.put(trim, firebaseApp);
        }
        zzb.zzb(firebaseApp);
        firebaseApp.zzc();
        return firebaseApp;
    }

    public static void onBackgroundStateChanged(boolean z) {
        synchronized (sLock) {
            ArrayList arrayList = new ArrayList(zzf.values());
            int size = arrayList.size();
            int i = 0;
            while (i < size) {
                Object obj = arrayList.get(i);
                i++;
                FirebaseApp firebaseApp = (FirebaseApp) obj;
                if (firebaseApp.zzj.get()) {
                    firebaseApp.zza(z);
                }
            }
        }
    }

    private final void zza() {
        Preconditions.checkState(!this.zzk.get(), "FirebaseApp was deleted");
    }

    private static <T> void zza(Class<T> cls, T t, Iterable<String> iterable, boolean z) {
        for (String str : iterable) {
            String str2;
            if (z) {
                try {
                    if (!zzd.contains(str2)) {
                    }
                } catch (ClassNotFoundException e) {
                    if (zze.contains(str2)) {
                        throw new IllegalStateException(String.valueOf(str2).concat(" is missing, but is required. Check if it has been removed by Proguard."));
                    }
                    Log.d("FirebaseApp", String.valueOf(str2).concat(" is not linked. Skipping initialization."));
                } catch (NoSuchMethodException e2) {
                    throw new IllegalStateException(String.valueOf(str2).concat("#getInstance has been removed by Proguard. Add keep rule to prevent it."));
                } catch (Throwable e3) {
                    Log.wtf("FirebaseApp", "Firebase API initialization failure.", e3);
                } catch (Throwable e4) {
                    String str3 = "FirebaseApp";
                    String str4 = "Failed to initialize ";
                    str2 = String.valueOf(str2);
                    Log.wtf(str3, str2.length() != 0 ? str4.concat(str2) : new String(str4), e4);
                }
            }
            Method method = Class.forName(str2).getMethod("getInstance", new Class[]{cls});
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)) {
                method.invoke(null, new Object[]{t});
            }
        }
    }

    private final void zza(boolean z) {
        Log.d("FirebaseApp", "Notifying background state change listeners.");
        for (BackgroundStateChangeListener onBackgroundStateChanged : this.zzm) {
            onBackgroundStateChanged.onBackgroundStateChanged(z);
        }
    }

    private final void zzc() {
        boolean isDeviceProtectedStorage = ContextCompat.isDeviceProtectedStorage(this.zzg);
        if (isDeviceProtectedStorage) {
            zza.zza(this.zzg);
        } else {
            this.zzi.zzb(isDefaultApp());
        }
        zza(FirebaseApp.class, this, zza, isDeviceProtectedStorage);
        if (isDefaultApp()) {
            zza(FirebaseApp.class, this, zzb, isDeviceProtectedStorage);
            zza(Context.class, this.zzg, zzc, isDeviceProtectedStorage);
        }
    }

    public boolean equals(Object obj) {
        return !(obj instanceof FirebaseApp) ? false : this.mName.equals(((FirebaseApp) obj).getName());
    }

    public <T> T get(Class<T> cls) {
        zza();
        return this.zzi.get(cls);
    }

    public Context getApplicationContext() {
        zza();
        return this.zzg;
    }

    public String getName() {
        zza();
        return this.mName;
    }

    public FirebaseOptions getOptions() {
        zza();
        return this.zzh;
    }

    public int hashCode() {
        return this.mName.hashCode();
    }

    public boolean isDefaultApp() {
        return "[DEFAULT]".equals(getName());
    }

    public String toString() {
        return Objects.toStringHelper(this).add("name", this.mName).add("options", this.zzh).toString();
    }
}
