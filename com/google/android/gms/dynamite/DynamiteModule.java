package com.google.android.gms.dynamite;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.DynamiteApi;
import com.google.android.gms.dynamic.IObjectWrapper;
import com.google.android.gms.dynamic.ObjectWrapper;
import com.google.android.gms.dynamite.IDynamiteLoaderV2.Stub;
import java.lang.reflect.Field;
import javax.annotation.concurrent.GuardedBy;

public final class DynamiteModule {
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION = new zzd();
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = new zze();
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION = new zzf();
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION_NO_FORCE_STAGING = new zzg();
    public static final VersionPolicy PREFER_LOCAL = new zzc();
    public static final VersionPolicy PREFER_REMOTE = new zzb();
    @GuardedBy("DynamiteModule.class")
    private static Boolean zzabr;
    @GuardedBy("DynamiteModule.class")
    private static IDynamiteLoader zzabs;
    @GuardedBy("DynamiteModule.class")
    private static IDynamiteLoaderV2 zzabt;
    @GuardedBy("DynamiteModule.class")
    private static String zzabu;
    private static final ThreadLocal<zza> zzabv = new ThreadLocal();
    private static final IVersions zzabw = new zza();
    private final Context zzabx;

    @DynamiteApi
    public static class DynamiteLoaderClassLoader {
        @GuardedBy("DynamiteLoaderClassLoader.class")
        public static ClassLoader sClassLoader;
    }

    public static class LoadingException extends Exception {
        private LoadingException(String str) {
            super(str);
        }

        private LoadingException(String str, Throwable th) {
            super(str, th);
        }
    }

    public interface VersionPolicy {

        public interface IVersions {
            int getLocalVersion(Context context, String str);

            int getRemoteVersion(Context context, String str, boolean z) throws LoadingException;
        }

        public static class SelectionResult {
            public int localVersion = 0;
            public int remoteVersion = 0;
            public int selection = 0;
        }

        SelectionResult selectModule(Context context, String str, IVersions iVersions) throws LoadingException;
    }

    private static class zza {
        public Cursor zzaby;

        private zza() {
        }
    }

    private static class zzb implements IVersions {
        private final int zzabz;
        private final int zzaca = 0;

        public zzb(int i, int i2) {
            this.zzabz = i;
        }

        public final int getLocalVersion(Context context, String str) {
            return this.zzabz;
        }

        public final int getRemoteVersion(Context context, String str, boolean z) {
            return 0;
        }
    }

    private DynamiteModule(Context context) {
        this.zzabx = (Context) Preconditions.checkNotNull(context);
    }

    public static int getLocalVersion(Context context, String str) {
        StringBuilder stringBuilder;
        String valueOf;
        try {
            ClassLoader classLoader = context.getApplicationContext().getClassLoader();
            stringBuilder = new StringBuilder(61 + String.valueOf(str).length());
            stringBuilder.append("com.google.android.gms.dynamite.descriptors.");
            stringBuilder.append(str);
            stringBuilder.append(".ModuleDescriptor");
            Class loadClass = classLoader.loadClass(stringBuilder.toString());
            Field declaredField = loadClass.getDeclaredField("MODULE_ID");
            Field declaredField2 = loadClass.getDeclaredField("MODULE_VERSION");
            if (declaredField.get(null).equals(str)) {
                return declaredField2.getInt(null);
            }
            valueOf = String.valueOf(declaredField.get(null));
            StringBuilder stringBuilder2 = new StringBuilder((51 + String.valueOf(valueOf).length()) + String.valueOf(str).length());
            stringBuilder2.append("Module descriptor id '");
            stringBuilder2.append(valueOf);
            stringBuilder2.append("' didn't match expected id '");
            stringBuilder2.append(str);
            stringBuilder2.append("'");
            Log.e("DynamiteModule", stringBuilder2.toString());
            return 0;
        } catch (ClassNotFoundException e) {
            stringBuilder = new StringBuilder(45 + String.valueOf(str).length());
            stringBuilder.append("Local module descriptor class for ");
            stringBuilder.append(str);
            stringBuilder.append(" not found.");
            Log.w("DynamiteModule", stringBuilder.toString());
            return 0;
        } catch (Exception e2) {
            str = "DynamiteModule";
            valueOf = "Failed to load module descriptor class: ";
            String valueOf2 = String.valueOf(e2.getMessage());
            Log.e(str, valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
            return 0;
        }
    }

    public static Uri getQueryUri(String str, boolean z) {
        String str2 = z ? "api_force_staging" : "api";
        StringBuilder stringBuilder = new StringBuilder((42 + String.valueOf(str2).length()) + String.valueOf(str).length());
        stringBuilder.append("content://com.google.android.gms.chimera/");
        stringBuilder.append(str2);
        stringBuilder.append("/");
        stringBuilder.append(str);
        return Uri.parse(stringBuilder.toString());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getRemoteVersion(android.content.Context r8, java.lang.String r9, boolean r10) {
        /*
        r0 = com.google.android.gms.dynamite.DynamiteModule.class;
        monitor-enter(r0);
        r1 = zzabr;	 Catch:{ all -> 0x00ea }
        if (r1 != 0) goto L_0x00b7;
    L_0x0007:
        r1 = r8.getApplicationContext();	 Catch:{ ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d }
        r1 = r1.getClassLoader();	 Catch:{ ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d }
        r2 = com.google.android.gms.dynamite.DynamiteModule.DynamiteLoaderClassLoader.class;
        r2 = r2.getName();	 Catch:{ ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d }
        r1 = r1.loadClass(r2);	 Catch:{ ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d }
        r2 = "sClassLoader";
        r2 = r1.getDeclaredField(r2);	 Catch:{ ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d }
        monitor-enter(r1);	 Catch:{ ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d }
        r3 = 0;
        r4 = r2.get(r3);	 Catch:{ all -> 0x008a }
        r4 = (java.lang.ClassLoader) r4;	 Catch:{ all -> 0x008a }
        if (r4 == 0) goto L_0x003a;
    L_0x0029:
        r2 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ all -> 0x008a }
        if (r4 != r2) goto L_0x0032;
    L_0x002f:
        r2 = java.lang.Boolean.FALSE;	 Catch:{ all -> 0x008a }
        goto L_0x0087;
    L_0x0032:
        zza(r4);	 Catch:{ LoadingException -> 0x0036 }
        goto L_0x0037;
    L_0x0036:
        r2 = move-exception;
    L_0x0037:
        r2 = java.lang.Boolean.TRUE;	 Catch:{ all -> 0x008a }
        goto L_0x0087;
    L_0x003a:
        r4 = "com.google.android.gms";
        r5 = r8.getApplicationContext();	 Catch:{ all -> 0x008a }
        r5 = r5.getPackageName();	 Catch:{ all -> 0x008a }
        r4 = r4.equals(r5);	 Catch:{ all -> 0x008a }
        if (r4 == 0) goto L_0x0052;
    L_0x004a:
        r4 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ all -> 0x008a }
        r2.set(r3, r4);	 Catch:{ all -> 0x008a }
        goto L_0x002f;
    L_0x0052:
        r4 = zzb(r8, r9, r10);	 Catch:{ LoadingException -> 0x007e }
        r5 = zzabu;	 Catch:{ LoadingException -> 0x007e }
        if (r5 == 0) goto L_0x007b;
    L_0x005a:
        r5 = zzabu;	 Catch:{ LoadingException -> 0x007e }
        r5 = r5.isEmpty();	 Catch:{ LoadingException -> 0x007e }
        if (r5 == 0) goto L_0x0063;
    L_0x0062:
        goto L_0x007b;
    L_0x0063:
        r5 = new com.google.android.gms.dynamite.zzh;	 Catch:{ LoadingException -> 0x007e }
        r6 = zzabu;	 Catch:{ LoadingException -> 0x007e }
        r7 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ LoadingException -> 0x007e }
        r5.<init>(r6, r7);	 Catch:{ LoadingException -> 0x007e }
        zza(r5);	 Catch:{ LoadingException -> 0x007e }
        r2.set(r3, r5);	 Catch:{ LoadingException -> 0x007e }
        r5 = java.lang.Boolean.TRUE;	 Catch:{ LoadingException -> 0x007e }
        zzabr = r5;	 Catch:{ LoadingException -> 0x007e }
        monitor-exit(r1);	 Catch:{ all -> 0x008a }
        monitor-exit(r0);	 Catch:{ all -> 0x00ea }
        return r4;
    L_0x007b:
        monitor-exit(r1);	 Catch:{ all -> 0x008a }
        monitor-exit(r0);	 Catch:{ all -> 0x00ea }
        return r4;
    L_0x007e:
        r4 = move-exception;
        r4 = java.lang.ClassLoader.getSystemClassLoader();	 Catch:{ all -> 0x008a }
        r2.set(r3, r4);	 Catch:{ all -> 0x008a }
        goto L_0x002f;
    L_0x0087:
        monitor-exit(r1);	 Catch:{ all -> 0x008a }
        r1 = r2;
        goto L_0x00b5;
    L_0x008a:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x008a }
        throw r2;	 Catch:{ ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d, ClassNotFoundException -> 0x008d }
    L_0x008d:
        r1 = move-exception;
        r2 = "DynamiteModule";
        r1 = java.lang.String.valueOf(r1);	 Catch:{ all -> 0x00ea }
        r3 = 30;
        r4 = java.lang.String.valueOf(r1);	 Catch:{ all -> 0x00ea }
        r4 = r4.length();	 Catch:{ all -> 0x00ea }
        r3 = r3 + r4;
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r4.<init>(r3);	 Catch:{ all -> 0x00ea }
        r3 = "Failed to load module via V2: ";
        r4.append(r3);	 Catch:{ all -> 0x00ea }
        r4.append(r1);	 Catch:{ all -> 0x00ea }
        r1 = r4.toString();	 Catch:{ all -> 0x00ea }
        android.util.Log.w(r2, r1);	 Catch:{ all -> 0x00ea }
        r1 = java.lang.Boolean.FALSE;	 Catch:{ all -> 0x00ea }
    L_0x00b5:
        zzabr = r1;	 Catch:{ all -> 0x00ea }
    L_0x00b7:
        monitor-exit(r0);	 Catch:{ all -> 0x00ea }
        r0 = r1.booleanValue();
        if (r0 == 0) goto L_0x00e5;
    L_0x00be:
        r8 = zzb(r8, r9, r10);	 Catch:{ LoadingException -> 0x00c3 }
        return r8;
    L_0x00c3:
        r8 = move-exception;
        r9 = "DynamiteModule";
        r10 = "Failed to retrieve remote module version: ";
        r8 = r8.getMessage();
        r8 = java.lang.String.valueOf(r8);
        r0 = r8.length();
        if (r0 == 0) goto L_0x00db;
    L_0x00d6:
        r8 = r10.concat(r8);
        goto L_0x00e0;
    L_0x00db:
        r8 = new java.lang.String;
        r8.<init>(r10);
    L_0x00e0:
        android.util.Log.w(r9, r8);
        r8 = 0;
        return r8;
    L_0x00e5:
        r8 = zza(r8, r9, r10);
        return r8;
    L_0x00ea:
        r8 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x00ea }
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.getRemoteVersion(android.content.Context, java.lang.String, boolean):int");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.google.android.gms.dynamite.DynamiteModule load(android.content.Context r10, com.google.android.gms.dynamite.DynamiteModule.VersionPolicy r11, java.lang.String r12) throws com.google.android.gms.dynamite.DynamiteModule.LoadingException {
        /*
        r0 = zzabv;
        r0 = r0.get();
        r0 = (com.google.android.gms.dynamite.DynamiteModule.zza) r0;
        r1 = new com.google.android.gms.dynamite.DynamiteModule$zza;
        r2 = 0;
        r1.<init>();
        r3 = zzabv;
        r3.set(r1);
        r3 = zzabw;	 Catch:{ all -> 0x0132 }
        r3 = r11.selectModule(r10, r12, r3);	 Catch:{ all -> 0x0132 }
        r4 = "DynamiteModule";
        r5 = r3.localVersion;	 Catch:{ all -> 0x0132 }
        r6 = r3.remoteVersion;	 Catch:{ all -> 0x0132 }
        r7 = 68;
        r8 = java.lang.String.valueOf(r12);	 Catch:{ all -> 0x0132 }
        r8 = r8.length();	 Catch:{ all -> 0x0132 }
        r7 = r7 + r8;
        r8 = java.lang.String.valueOf(r12);	 Catch:{ all -> 0x0132 }
        r8 = r8.length();	 Catch:{ all -> 0x0132 }
        r7 = r7 + r8;
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0132 }
        r8.<init>(r7);	 Catch:{ all -> 0x0132 }
        r7 = "Considering local module ";
        r8.append(r7);	 Catch:{ all -> 0x0132 }
        r8.append(r12);	 Catch:{ all -> 0x0132 }
        r7 = ":";
        r8.append(r7);	 Catch:{ all -> 0x0132 }
        r8.append(r5);	 Catch:{ all -> 0x0132 }
        r5 = " and remote module ";
        r8.append(r5);	 Catch:{ all -> 0x0132 }
        r8.append(r12);	 Catch:{ all -> 0x0132 }
        r5 = ":";
        r8.append(r5);	 Catch:{ all -> 0x0132 }
        r8.append(r6);	 Catch:{ all -> 0x0132 }
        r5 = r8.toString();	 Catch:{ all -> 0x0132 }
        android.util.Log.i(r4, r5);	 Catch:{ all -> 0x0132 }
        r4 = r3.selection;	 Catch:{ all -> 0x0132 }
        if (r4 == 0) goto L_0x0108;
    L_0x0063:
        r4 = r3.selection;	 Catch:{ all -> 0x0132 }
        r5 = -1;
        if (r4 != r5) goto L_0x006c;
    L_0x0068:
        r4 = r3.localVersion;	 Catch:{ all -> 0x0132 }
        if (r4 == 0) goto L_0x0108;
    L_0x006c:
        r4 = r3.selection;	 Catch:{ all -> 0x0132 }
        r6 = 1;
        if (r4 != r6) goto L_0x0077;
    L_0x0071:
        r4 = r3.remoteVersion;	 Catch:{ all -> 0x0132 }
        if (r4 != 0) goto L_0x0077;
    L_0x0075:
        goto L_0x0108;
    L_0x0077:
        r4 = r3.selection;	 Catch:{ all -> 0x0132 }
        if (r4 != r5) goto L_0x008e;
    L_0x007b:
        r10 = zzd(r10, r12);	 Catch:{ all -> 0x0132 }
        r11 = r1.zzaby;
        if (r11 == 0) goto L_0x0088;
    L_0x0083:
        r11 = r1.zzaby;
        r11.close();
    L_0x0088:
        r11 = zzabv;
        r11.set(r0);
        return r10;
    L_0x008e:
        r4 = r3.selection;	 Catch:{ all -> 0x0132 }
        if (r4 != r6) goto L_0x00ed;
    L_0x0092:
        r4 = r3.remoteVersion;	 Catch:{ LoadingException -> 0x00a7 }
        r4 = zza(r10, r12, r4);	 Catch:{ LoadingException -> 0x00a7 }
        r10 = r1.zzaby;
        if (r10 == 0) goto L_0x00a1;
    L_0x009c:
        r10 = r1.zzaby;
        r10.close();
    L_0x00a1:
        r10 = zzabv;
        r10.set(r0);
        return r4;
    L_0x00a7:
        r4 = move-exception;
        r6 = "DynamiteModule";
        r7 = "Failed to load remote module: ";
        r8 = r4.getMessage();	 Catch:{ all -> 0x0132 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ all -> 0x0132 }
        r9 = r8.length();	 Catch:{ all -> 0x0132 }
        if (r9 == 0) goto L_0x00bf;
    L_0x00ba:
        r7 = r7.concat(r8);	 Catch:{ all -> 0x0132 }
        goto L_0x00c5;
    L_0x00bf:
        r8 = new java.lang.String;	 Catch:{ all -> 0x0132 }
        r8.<init>(r7);	 Catch:{ all -> 0x0132 }
        r7 = r8;
    L_0x00c5:
        android.util.Log.w(r6, r7);	 Catch:{ all -> 0x0132 }
        r6 = r3.localVersion;	 Catch:{ all -> 0x0132 }
        if (r6 == 0) goto L_0x00e5;
    L_0x00cc:
        r6 = new com.google.android.gms.dynamite.DynamiteModule$zzb;	 Catch:{ all -> 0x0132 }
        r3 = r3.localVersion;	 Catch:{ all -> 0x0132 }
        r7 = 0;
        r6.<init>(r3, r7);	 Catch:{ all -> 0x0132 }
        r11 = r11.selectModule(r10, r12, r6);	 Catch:{ all -> 0x0132 }
        r11 = r11.selection;	 Catch:{ all -> 0x0132 }
        if (r11 != r5) goto L_0x00e5;
    L_0x00dc:
        r10 = zzd(r10, r12);	 Catch:{ all -> 0x0132 }
        r11 = r1.zzaby;
        if (r11 == 0) goto L_0x0088;
    L_0x00e4:
        goto L_0x0083;
    L_0x00e5:
        r10 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException;	 Catch:{ all -> 0x0132 }
        r11 = "Remote load failed. No local fallback found.";
        r10.<init>(r11, r4);	 Catch:{ all -> 0x0132 }
        throw r10;	 Catch:{ all -> 0x0132 }
    L_0x00ed:
        r10 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException;	 Catch:{ all -> 0x0132 }
        r11 = r3.selection;	 Catch:{ all -> 0x0132 }
        r12 = 47;
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0132 }
        r3.<init>(r12);	 Catch:{ all -> 0x0132 }
        r12 = "VersionPolicy returned invalid code:";
        r3.append(r12);	 Catch:{ all -> 0x0132 }
        r3.append(r11);	 Catch:{ all -> 0x0132 }
        r11 = r3.toString();	 Catch:{ all -> 0x0132 }
        r10.<init>(r11);	 Catch:{ all -> 0x0132 }
        throw r10;	 Catch:{ all -> 0x0132 }
    L_0x0108:
        r10 = new com.google.android.gms.dynamite.DynamiteModule$LoadingException;	 Catch:{ all -> 0x0132 }
        r11 = r3.localVersion;	 Catch:{ all -> 0x0132 }
        r12 = r3.remoteVersion;	 Catch:{ all -> 0x0132 }
        r3 = 91;
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0132 }
        r4.<init>(r3);	 Catch:{ all -> 0x0132 }
        r3 = "No acceptable module found. Local version is ";
        r4.append(r3);	 Catch:{ all -> 0x0132 }
        r4.append(r11);	 Catch:{ all -> 0x0132 }
        r11 = " and remote version is ";
        r4.append(r11);	 Catch:{ all -> 0x0132 }
        r4.append(r12);	 Catch:{ all -> 0x0132 }
        r11 = ".";
        r4.append(r11);	 Catch:{ all -> 0x0132 }
        r11 = r4.toString();	 Catch:{ all -> 0x0132 }
        r10.<init>(r11);	 Catch:{ all -> 0x0132 }
        throw r10;	 Catch:{ all -> 0x0132 }
    L_0x0132:
        r10 = move-exception;
        r11 = r1.zzaby;
        if (r11 == 0) goto L_0x013c;
    L_0x0137:
        r11 = r1.zzaby;
        r11.close();
    L_0x013c:
        r11 = zzabv;
        r11.set(r0);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.dynamite.DynamiteModule.load(android.content.Context, com.google.android.gms.dynamite.DynamiteModule$VersionPolicy, java.lang.String):com.google.android.gms.dynamite.DynamiteModule");
    }

    public static Cursor queryForDynamiteModule(Context context, String str, boolean z) {
        return context.getContentResolver().query(getQueryUri(str, z), null, null, null, null);
    }

    private static int zza(Context context, String str, boolean z) {
        IDynamiteLoader zzg = zzg(context);
        if (zzg == null) {
            return 0;
        }
        try {
            return zzg.getModuleVersion2(ObjectWrapper.wrap(context), str, z);
        } catch (RemoteException e) {
            str = "DynamiteModule";
            String str2 = "Failed to retrieve remote module version: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.w(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            return 0;
        }
    }

    private static Context zza(Context context, String str, int i, Cursor cursor, IDynamiteLoaderV2 iDynamiteLoaderV2) {
        try {
            return (Context) ObjectWrapper.unwrap(iDynamiteLoaderV2.loadModule2(ObjectWrapper.wrap(context), str, i, ObjectWrapper.wrap(cursor)));
        } catch (Exception e) {
            str = "DynamiteModule";
            String str2 = "Failed to load DynamiteLoader: ";
            String valueOf = String.valueOf(e.toString());
            Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            return null;
        }
    }

    private static DynamiteModule zza(Context context, String str, int i) throws LoadingException {
        synchronized (DynamiteModule.class) {
            Boolean bool = zzabr;
        }
        if (bool != null) {
            return bool.booleanValue() ? zzc(context, str, i) : zzb(context, str, i);
        } else {
            throw new LoadingException("Failed to determine which loading route to use.");
        }
    }

    @GuardedBy("DynamiteModule.class")
    private static void zza(ClassLoader classLoader) throws LoadingException {
        try {
            zzabt = Stub.asInterface((IBinder) classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (Throwable e) {
            throw new LoadingException("Failed to instantiate dynamite loader", e);
        }
    }

    private static int zzb(Context context, String str, boolean z) throws LoadingException {
        Cursor queryForDynamiteModule;
        Throwable e;
        try {
            queryForDynamiteModule = queryForDynamiteModule(context, str, z);
            if (queryForDynamiteModule != null) {
                try {
                    if (queryForDynamiteModule.moveToFirst()) {
                        int i = queryForDynamiteModule.getInt(0);
                        if (i > 0) {
                            synchronized (DynamiteModule.class) {
                                zzabu = queryForDynamiteModule.getString(2);
                            }
                            zza com_google_android_gms_dynamite_DynamiteModule_zza = (zza) zzabv.get();
                            if (com_google_android_gms_dynamite_DynamiteModule_zza != null && com_google_android_gms_dynamite_DynamiteModule_zza.zzaby == null) {
                                com_google_android_gms_dynamite_DynamiteModule_zza.zzaby = queryForDynamiteModule;
                                queryForDynamiteModule = null;
                            }
                        }
                        if (queryForDynamiteModule != null) {
                            queryForDynamiteModule.close();
                        }
                        return i;
                    }
                } catch (Exception e2) {
                    e = e2;
                }
            }
            Log.w("DynamiteModule", "Failed to retrieve remote module version.");
            throw new LoadingException("Failed to connect to dynamite module ContentResolver.");
        } catch (Exception e3) {
            e = e3;
            queryForDynamiteModule = null;
            try {
                if (e instanceof LoadingException) {
                    throw e;
                }
                throw new LoadingException("V2 version check failed", e);
            } catch (Throwable th) {
                e = th;
                if (queryForDynamiteModule != null) {
                    queryForDynamiteModule.close();
                }
                throw e;
            }
        } catch (Throwable th2) {
            e = th2;
            queryForDynamiteModule = null;
            if (queryForDynamiteModule != null) {
                queryForDynamiteModule.close();
            }
            throw e;
        }
    }

    private static DynamiteModule zzb(Context context, String str, int i) throws LoadingException {
        StringBuilder stringBuilder = new StringBuilder(51 + String.valueOf(str).length());
        stringBuilder.append("Selected remote version of ");
        stringBuilder.append(str);
        stringBuilder.append(", version >= ");
        stringBuilder.append(i);
        Log.i("DynamiteModule", stringBuilder.toString());
        IDynamiteLoader zzg = zzg(context);
        if (zzg == null) {
            throw new LoadingException("Failed to create IDynamiteLoader.");
        }
        try {
            IObjectWrapper createModuleContext = zzg.createModuleContext(ObjectWrapper.wrap(context), str, i);
            if (ObjectWrapper.unwrap(createModuleContext) != null) {
                return new DynamiteModule((Context) ObjectWrapper.unwrap(createModuleContext));
            }
            throw new LoadingException("Failed to load remote module.");
        } catch (Throwable e) {
            throw new LoadingException("Failed to load remote module.", e);
        }
    }

    private static DynamiteModule zzc(Context context, String str, int i) throws LoadingException {
        StringBuilder stringBuilder = new StringBuilder(51 + String.valueOf(str).length());
        stringBuilder.append("Selected remote version of ");
        stringBuilder.append(str);
        stringBuilder.append(", version >= ");
        stringBuilder.append(i);
        Log.i("DynamiteModule", stringBuilder.toString());
        synchronized (DynamiteModule.class) {
            IDynamiteLoaderV2 iDynamiteLoaderV2 = zzabt;
        }
        if (iDynamiteLoaderV2 == null) {
            throw new LoadingException("DynamiteLoaderV2 was not cached.");
        }
        zza com_google_android_gms_dynamite_DynamiteModule_zza = (zza) zzabv.get();
        if (com_google_android_gms_dynamite_DynamiteModule_zza != null) {
            if (com_google_android_gms_dynamite_DynamiteModule_zza.zzaby != null) {
                context = zza(context.getApplicationContext(), str, i, com_google_android_gms_dynamite_DynamiteModule_zza.zzaby, iDynamiteLoaderV2);
                if (context != null) {
                    return new DynamiteModule(context);
                }
                throw new LoadingException("Failed to get module context");
            }
        }
        throw new LoadingException("No result cursor");
    }

    private static DynamiteModule zzd(Context context, String str) {
        String str2 = "DynamiteModule";
        String str3 = "Selected local version of ";
        str = String.valueOf(str);
        Log.i(str2, str.length() != 0 ? str3.concat(str) : new String(str3));
        return new DynamiteModule(context.getApplicationContext());
    }

    private static IDynamiteLoader zzg(Context context) {
        synchronized (DynamiteModule.class) {
            IDynamiteLoader iDynamiteLoader;
            if (zzabs != null) {
                iDynamiteLoader = zzabs;
                return iDynamiteLoader;
            } else if (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) != 0) {
                return null;
            } else {
                try {
                    iDynamiteLoader = IDynamiteLoader.Stub.asInterface((IBinder) context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance());
                    if (iDynamiteLoader != null) {
                        zzabs = iDynamiteLoader;
                        return iDynamiteLoader;
                    }
                } catch (Exception e) {
                    String str = "DynamiteModule";
                    String str2 = "Failed to load IDynamiteLoader from GmsCore: ";
                    String valueOf = String.valueOf(e.getMessage());
                    Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                }
            }
        }
        return null;
    }

    public final Context getModuleContext() {
        return this.zzabx;
    }

    public final IBinder instantiate(String str) throws LoadingException {
        try {
            return (IBinder) this.zzabx.getClassLoader().loadClass(str).newInstance();
        } catch (Throwable e) {
            String str2 = "Failed to instantiate module class: ";
            str = String.valueOf(str);
            throw new LoadingException(str.length() != 0 ? str2.concat(str) : new String(str2), e);
        }
    }
}
