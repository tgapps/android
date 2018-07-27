package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.internal.firebase_messaging.zza;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.telegram.messenger.exoplayer2.C;

final class zzw {
    zzw() {
    }

    private static zzx zza(SharedPreferences sharedPreferences, String str) throws zzy {
        String string = sharedPreferences.getString(zzau.zzc(str, "|P|"), null);
        String string2 = sharedPreferences.getString(zzau.zzc(str, "|K|"), null);
        return (string == null || string2 == null) ? null : new zzx(zzb(string, string2), zzb(sharedPreferences, str));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.google.firebase.iid.zzx zza(java.io.File r8) throws com.google.firebase.iid.zzy, java.io.IOException {
        /*
        r1 = 0;
        r4 = new java.io.FileInputStream;
        r4.<init>(r8);
        r0 = new java.util.Properties;	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        r0.<init>();	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        r0.load(r4);	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        r2 = "pub";
        r2 = r0.getProperty(r2);	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        r3 = "pri";
        r3 = r0.getProperty(r3);	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        if (r2 == 0) goto L_0x0020;
    L_0x001e:
        if (r3 != 0) goto L_0x0025;
    L_0x0020:
        zza(r1, r4);
        r0 = r1;
    L_0x0024:
        return r0;
    L_0x0025:
        r2 = zzb(r2, r3);	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        r3 = "cre";
        r0 = r0.getProperty(r3);	 Catch:{ NumberFormatException -> 0x003d }
        r6 = java.lang.Long.parseLong(r0);	 Catch:{ NumberFormatException -> 0x003d }
        r0 = new com.google.firebase.iid.zzx;	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        r0.<init>(r2, r6);	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        zza(r1, r4);
        goto L_0x0024;
    L_0x003d:
        r0 = move-exception;
        r2 = new com.google.firebase.iid.zzy;	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        r2.<init>(r0);	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        throw r2;	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
    L_0x0044:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0046 }
    L_0x0046:
        r1 = move-exception;
        r2 = r1;
        r3 = r0;
    L_0x0049:
        zza(r3, r4);
        throw r2;
    L_0x004d:
        r0 = move-exception;
        r2 = r0;
        r3 = r1;
        goto L_0x0049;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzw.zza(java.io.File):com.google.firebase.iid.zzx");
    }

    static void zza(Context context) {
        for (File file : zzb(context).listFiles()) {
            if (file.getName().startsWith("com.google.InstanceId")) {
                file.delete();
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void zza(android.content.Context r6, java.lang.String r7, com.google.firebase.iid.zzx r8) {
        /*
        r1 = 0;
        r0 = "FirebaseInstanceId";
        r2 = 3;
        r0 = android.util.Log.isLoggable(r0, r2);	 Catch:{ IOException -> 0x0057 }
        if (r0 == 0) goto L_0x0014;
    L_0x000b:
        r0 = "FirebaseInstanceId";
        r2 = "Writing key to properties file";
        android.util.Log.d(r0, r2);	 Catch:{ IOException -> 0x0057 }
    L_0x0014:
        r0 = zzf(r6, r7);	 Catch:{ IOException -> 0x0057 }
        r0.createNewFile();	 Catch:{ IOException -> 0x0057 }
        r2 = new java.util.Properties;	 Catch:{ IOException -> 0x0057 }
        r2.<init>();	 Catch:{ IOException -> 0x0057 }
        r3 = "pub";
        r4 = r8.zzu();	 Catch:{ IOException -> 0x0057 }
        r2.setProperty(r3, r4);	 Catch:{ IOException -> 0x0057 }
        r3 = "pri";
        r4 = r8.zzv();	 Catch:{ IOException -> 0x0057 }
        r2.setProperty(r3, r4);	 Catch:{ IOException -> 0x0057 }
        r3 = "cre";
        r4 = r8.zzbj;	 Catch:{ IOException -> 0x0057 }
        r4 = java.lang.String.valueOf(r4);	 Catch:{ IOException -> 0x0057 }
        r2.setProperty(r3, r4);	 Catch:{ IOException -> 0x0057 }
        r3 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x0057 }
        r3.<init>(r0);	 Catch:{ IOException -> 0x0057 }
        r0 = 0;
        r2.store(r3, r0);	 Catch:{ Throwable -> 0x0050 }
        r0 = 0;
        zza(r0, r3);	 Catch:{ IOException -> 0x0057 }
    L_0x004f:
        return;
    L_0x0050:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0052 }
    L_0x0052:
        r0 = move-exception;
        zza(r1, r3);	 Catch:{ IOException -> 0x0057 }
        throw r0;	 Catch:{ IOException -> 0x0057 }
    L_0x0057:
        r0 = move-exception;
        r1 = "FirebaseInstanceId";
        r0 = java.lang.String.valueOf(r0);
        r2 = java.lang.String.valueOf(r0);
        r2 = r2.length();
        r2 = r2 + 21;
        r3 = new java.lang.StringBuilder;
        r3.<init>(r2);
        r2 = "Failed to write key: ";
        r2 = r3.append(r2);
        r0 = r2.append(r0);
        r0 = r0.toString();
        android.util.Log.w(r1, r0);
        goto L_0x004f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzw.zza(android.content.Context, java.lang.String, com.google.firebase.iid.zzx):void");
    }

    private static /* synthetic */ void zza(Throwable th, FileInputStream fileInputStream) {
        if (th != null) {
            try {
                fileInputStream.close();
                return;
            } catch (Throwable th2) {
                zza.zza(th, th2);
                return;
            }
        }
        fileInputStream.close();
    }

    private static /* synthetic */ void zza(Throwable th, FileOutputStream fileOutputStream) {
        if (th != null) {
            try {
                fileOutputStream.close();
                return;
            } catch (Throwable th2) {
                zza.zza(th, th2);
                return;
            }
        }
        fileOutputStream.close();
    }

    private static long zzb(SharedPreferences sharedPreferences, String str) {
        String string = sharedPreferences.getString(zzau.zzc(str, "cre"), null);
        if (string != null) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException e) {
            }
        }
        return 0;
    }

    private static File zzb(Context context) {
        File noBackupFilesDir = ContextCompat.getNoBackupFilesDir(context);
        if (noBackupFilesDir != null && noBackupFilesDir.isDirectory()) {
            return noBackupFilesDir;
        }
        Log.w("FirebaseInstanceId", "noBackupFilesDir doesn't exist, using regular files directory instead");
        return context.getFilesDir();
    }

    private static KeyPair zzb(String str, String str2) throws zzy {
        Exception e;
        String valueOf;
        try {
            byte[] decode = Base64.decode(str, 8);
            byte[] decode2 = Base64.decode(str2, 8);
            try {
                KeyFactory instance = KeyFactory.getInstance("RSA");
                return new KeyPair(instance.generatePublic(new X509EncodedKeySpec(decode)), instance.generatePrivate(new PKCS8EncodedKeySpec(decode2)));
            } catch (InvalidKeySpecException e2) {
                e = e2;
                valueOf = String.valueOf(e);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 19).append("Invalid key stored ").append(valueOf).toString());
                throw new zzy(e);
            } catch (NoSuchAlgorithmException e3) {
                e = e3;
                valueOf = String.valueOf(e);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 19).append("Invalid key stored ").append(valueOf).toString());
                throw new zzy(e);
            }
        } catch (Exception e4) {
            throw new zzy(e4);
        }
    }

    private final void zzb(Context context, String str, zzx com_google_firebase_iid_zzx) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gms.appid", 0);
        try {
            if (com_google_firebase_iid_zzx.equals(zza(sharedPreferences, str))) {
                return;
            }
        } catch (zzy e) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Writing key to shared preferences");
        }
        Editor edit = sharedPreferences.edit();
        edit.putString(zzau.zzc(str, "|P|"), com_google_firebase_iid_zzx.zzu());
        edit.putString(zzau.zzc(str, "|K|"), com_google_firebase_iid_zzx.zzv());
        edit.putString(zzau.zzc(str, "cre"), String.valueOf(com_google_firebase_iid_zzx.zzbj));
        edit.commit();
    }

    private final zzx zzd(Context context, String str) throws zzy {
        zzy com_google_firebase_iid_zzy;
        zzy com_google_firebase_iid_zzy2;
        try {
            zzx zze = zze(context, str);
            if (zze != null) {
                zzb(context, str, zze);
                return zze;
            }
            com_google_firebase_iid_zzy = null;
            try {
                zze = zza(context.getSharedPreferences("com.google.android.gms.appid", 0), str);
                if (zze != null) {
                    zza(context, str, zze);
                    return zze;
                }
                com_google_firebase_iid_zzy2 = com_google_firebase_iid_zzy;
                if (com_google_firebase_iid_zzy2 == null) {
                    return null;
                }
                throw com_google_firebase_iid_zzy2;
            } catch (zzy e) {
                com_google_firebase_iid_zzy2 = e;
            }
        } catch (zzy com_google_firebase_iid_zzy22) {
            com_google_firebase_iid_zzy = com_google_firebase_iid_zzy22;
        }
    }

    private final zzx zze(Context context, String str) throws zzy {
        File zzf = zzf(context, str);
        if (!zzf.exists()) {
            return null;
        }
        try {
            return zza(zzf);
        } catch (IOException e) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                String valueOf = String.valueOf(e);
                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 40).append("Failed to read key from file, retrying: ").append(valueOf).toString());
            }
            try {
                return zza(zzf);
            } catch (Exception e2) {
                String valueOf2 = String.valueOf(e2);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf2).length() + 45).append("IID file exists, but failed to read from it: ").append(valueOf2).toString());
                throw new zzy(e2);
            }
        }
    }

    private static File zzf(Context context, String str) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            str2 = "com.google.InstanceId.properties";
        } else {
            try {
                str2 = Base64.encodeToString(str.getBytes(C.UTF8_NAME), 11);
                str2 = new StringBuilder(String.valueOf(str2).length() + 33).append("com.google.InstanceId_").append(str2).append(".properties").toString();
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }
        return new File(zzb(context), str2);
    }

    final zzx zzb(Context context, String str) throws zzy {
        zzx zzd = zzd(context, str);
        return zzd != null ? zzd : zzc(context, str);
    }

    final zzx zzc(Context context, String str) {
        zzx com_google_firebase_iid_zzx = new zzx(zza.zzb(), System.currentTimeMillis());
        try {
            zzx zzd = zzd(context, str);
            if (zzd != null) {
                if (!Log.isLoggable("FirebaseInstanceId", 3)) {
                    return zzd;
                }
                Log.d("FirebaseInstanceId", "Loaded key after generating new one, using loaded one");
                return zzd;
            }
        } catch (zzy e) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Generated new key");
        }
        zza(context, str, com_google_firebase_iid_zzx);
        zzb(context, str, com_google_firebase_iid_zzx);
        return com_google_firebase_iid_zzx;
    }
}
