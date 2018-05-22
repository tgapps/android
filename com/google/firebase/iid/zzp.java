package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.gms.internal.firebase_messaging.zzh;
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

final class zzp {
    zzp() {
    }

    private static zzq zza(SharedPreferences sharedPreferences, String str) throws zzr {
        String string = sharedPreferences.getString(zzao.zzd(str, "|P|"), null);
        String string2 = sharedPreferences.getString(zzao.zzd(str, "|K|"), null);
        return (string == null || string2 == null) ? null : new zzq(zzc(string, string2), zzb(sharedPreferences, str));
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.google.firebase.iid.zzq zza(java.io.File r8) throws com.google.firebase.iid.zzr, java.io.IOException {
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
        r2 = zzc(r2, r3);	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        r3 = "cre";
        r0 = r0.getProperty(r3);	 Catch:{ NumberFormatException -> 0x003d }
        r6 = java.lang.Long.parseLong(r0);	 Catch:{ NumberFormatException -> 0x003d }
        r0 = new com.google.firebase.iid.zzq;	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        r0.<init>(r2, r6);	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
        zza(r1, r4);
        goto L_0x0024;
    L_0x003d:
        r0 = move-exception;
        r2 = new com.google.firebase.iid.zzr;	 Catch:{ Throwable -> 0x0044, all -> 0x004d }
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
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzp.zza(java.io.File):com.google.firebase.iid.zzq");
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
    private static void zza(android.content.Context r6, java.lang.String r7, com.google.firebase.iid.zzq r8) {
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
        r4 = r8.zzq();	 Catch:{ IOException -> 0x0057 }
        r2.setProperty(r3, r4);	 Catch:{ IOException -> 0x0057 }
        r3 = "pri";
        r4 = r8.zzr();	 Catch:{ IOException -> 0x0057 }
        r2.setProperty(r3, r4);	 Catch:{ IOException -> 0x0057 }
        r3 = "cre";
        r4 = r8.zzbe;	 Catch:{ IOException -> 0x0057 }
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
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.zzp.zza(android.content.Context, java.lang.String, com.google.firebase.iid.zzq):void");
    }

    private static /* synthetic */ void zza(Throwable th, FileInputStream fileInputStream) {
        if (th != null) {
            try {
                fileInputStream.close();
                return;
            } catch (Throwable th2) {
                zzh.zza(th, th2);
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
                zzh.zza(th, th2);
                return;
            }
        }
        fileOutputStream.close();
    }

    private static long zzb(SharedPreferences sharedPreferences, String str) {
        String string = sharedPreferences.getString(zzao.zzd(str, "cre"), null);
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

    private final void zzb(Context context, String str, zzq com_google_firebase_iid_zzq) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gms.appid", 0);
        try {
            if (com_google_firebase_iid_zzq.equals(zza(sharedPreferences, str))) {
                return;
            }
        } catch (zzr e) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Writing key to shared preferences");
        }
        Editor edit = sharedPreferences.edit();
        edit.putString(zzao.zzd(str, "|P|"), com_google_firebase_iid_zzq.zzq());
        edit.putString(zzao.zzd(str, "|K|"), com_google_firebase_iid_zzq.zzr());
        edit.putString(zzao.zzd(str, "cre"), String.valueOf(com_google_firebase_iid_zzq.zzbe));
        edit.commit();
    }

    private static KeyPair zzc(String str, String str2) throws zzr {
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
                throw new zzr(e);
            } catch (NoSuchAlgorithmException e3) {
                e = e3;
                valueOf = String.valueOf(e);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 19).append("Invalid key stored ").append(valueOf).toString());
                throw new zzr(e);
            }
        } catch (Exception e4) {
            throw new zzr(e4);
        }
    }

    private final zzq zzd(Context context, String str) throws zzr {
        zzr com_google_firebase_iid_zzr;
        zzr com_google_firebase_iid_zzr2;
        try {
            zzq zze = zze(context, str);
            if (zze != null) {
                zzb(context, str, zze);
                return zze;
            }
            com_google_firebase_iid_zzr2 = null;
            try {
                zze = zza(context.getSharedPreferences("com.google.android.gms.appid", 0), str);
                if (zze != null) {
                    zza(context, str, zze);
                    return zze;
                }
                com_google_firebase_iid_zzr = com_google_firebase_iid_zzr2;
                if (com_google_firebase_iid_zzr == null) {
                    return null;
                }
                throw com_google_firebase_iid_zzr;
            } catch (zzr e) {
                com_google_firebase_iid_zzr = e;
            }
        } catch (zzr com_google_firebase_iid_zzr3) {
            com_google_firebase_iid_zzr2 = com_google_firebase_iid_zzr3;
        }
    }

    private final zzq zze(Context context, String str) throws zzr {
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
                throw new zzr(e2);
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

    final zzq zzb(Context context, String str) throws zzr {
        zzq zzd = zzd(context, str);
        return zzd != null ? zzd : zzc(context, str);
    }

    final zzq zzc(Context context, String str) {
        zzq com_google_firebase_iid_zzq = new zzq(zza.zzb(), System.currentTimeMillis());
        try {
            zzq zzd = zzd(context, str);
            if (zzd != null) {
                if (!Log.isLoggable("FirebaseInstanceId", 3)) {
                    return zzd;
                }
                Log.d("FirebaseInstanceId", "Loaded key after generating new one, using loaded one");
                return zzd;
            }
        } catch (zzr e) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Generated new key");
        }
        zza(context, str, com_google_firebase_iid_zzq);
        zzb(context, str, com_google_firebase_iid_zzq);
        return com_google_firebase_iid_zzq;
    }
}
