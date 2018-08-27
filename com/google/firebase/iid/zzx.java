package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.google.android.exoplayer2.C;
import com.google.android.gms.internal.firebase_messaging.zza;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Properties;

final class zzx {
    zzx() {
    }

    final zzy zzb(Context context, String str) throws zzz {
        zzy zzd = zzd(context, str);
        return zzd != null ? zzd : zzc(context, str);
    }

    final zzy zzc(Context context, String str) {
        zzy com_google_firebase_iid_zzy = new zzy(zza.zzb(), System.currentTimeMillis());
        zzy zza = zza(context, str, com_google_firebase_iid_zzy, true);
        if (zza == null || zza.equals(com_google_firebase_iid_zzy)) {
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Generated new key");
            }
            zza(context, str, com_google_firebase_iid_zzy);
            return com_google_firebase_iid_zzy;
        } else if (!Log.isLoggable("FirebaseInstanceId", 3)) {
            return zza;
        } else {
            Log.d("FirebaseInstanceId", "Loaded key after generating new one, using loaded one");
            return zza;
        }
    }

    static void zza(Context context) {
        for (File file : zzb(context).listFiles()) {
            if (file.getName().startsWith("com.google.InstanceId")) {
                file.delete();
            }
        }
    }

    private final zzy zzd(Context context, String str) throws zzz {
        zzz com_google_firebase_iid_zzz;
        zzz com_google_firebase_iid_zzz2;
        try {
            zzy zze = zze(context, str);
            if (zze != null) {
                zza(context, str, zze);
                return zze;
            }
            com_google_firebase_iid_zzz = null;
            try {
                zze = zza(context.getSharedPreferences("com.google.android.gms.appid", 0), str);
                if (zze != null) {
                    zza(context, str, zze, false);
                    return zze;
                }
                com_google_firebase_iid_zzz2 = com_google_firebase_iid_zzz;
                if (com_google_firebase_iid_zzz2 == null) {
                    return null;
                }
                throw com_google_firebase_iid_zzz2;
            } catch (zzz e) {
                com_google_firebase_iid_zzz2 = e;
            }
        } catch (zzz com_google_firebase_iid_zzz22) {
            com_google_firebase_iid_zzz = com_google_firebase_iid_zzz22;
        }
    }

    private static KeyPair zzc(String str, String str2) throws zzz {
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
                throw new zzz(e);
            } catch (NoSuchAlgorithmException e3) {
                e = e3;
                valueOf = String.valueOf(e);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 19).append("Invalid key stored ").append(valueOf).toString());
                throw new zzz(e);
            }
        } catch (Exception e4) {
            throw new zzz(e4);
        }
    }

    private final zzy zze(Context context, String str) throws zzz {
        Object e;
        String valueOf;
        File zzf = zzf(context, str);
        if (!zzf.exists()) {
            return null;
        }
        try {
            return zza(zzf);
        } catch (zzz e2) {
            e = e2;
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                valueOf = String.valueOf(e);
                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 40).append("Failed to read key from file, retrying: ").append(valueOf).toString());
            }
            try {
                return zza(zzf);
            } catch (Exception e3) {
                String valueOf2 = String.valueOf(e3);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf2).length() + 45).append("IID file exists, but failed to read from it: ").append(valueOf2).toString());
                throw new zzz(e3);
            }
        } catch (IOException e4) {
            e = e4;
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                valueOf = String.valueOf(e);
                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 40).append("Failed to read key from file, retrying: ").append(valueOf).toString());
            }
            return zza(zzf);
        }
    }

    private final zzy zza(Context context, String str, zzy com_google_firebase_iid_zzy, boolean z) {
        Object e;
        String valueOf;
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Writing key to properties file");
        }
        Properties properties = new Properties();
        properties.setProperty("pub", com_google_firebase_iid_zzy.zzu());
        properties.setProperty("pri", com_google_firebase_iid_zzy.zzv());
        properties.setProperty("cre", String.valueOf(com_google_firebase_iid_zzy.zzbp));
        File zzf = zzf(context, str);
        try {
            zzf.createNewFile();
            RandomAccessFile randomAccessFile = new RandomAccessFile(zzf, "rw");
            Throwable th;
            Throwable th2;
            try {
                FileChannel channel = randomAccessFile.getChannel();
                try {
                    channel.lock();
                    if (z && channel.size() > 0) {
                        try {
                            channel.position(0);
                            com_google_firebase_iid_zzy = zza(channel);
                            if (channel != null) {
                                zza(null, channel);
                            }
                            zza(null, randomAccessFile);
                            return com_google_firebase_iid_zzy;
                        } catch (IOException e2) {
                            e = e2;
                            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                                valueOf = String.valueOf(e);
                                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 64).append("Tried reading key pair before writing new one, but failed with: ").append(valueOf).toString());
                            }
                            channel.position(0);
                            properties.store(Channels.newOutputStream(channel), null);
                            if (channel != null) {
                                zza(null, channel);
                            }
                            zza(null, randomAccessFile);
                            return com_google_firebase_iid_zzy;
                        } catch (zzz e3) {
                            e = e3;
                            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                                valueOf = String.valueOf(e);
                                Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 64).append("Tried reading key pair before writing new one, but failed with: ").append(valueOf).toString());
                            }
                            channel.position(0);
                            properties.store(Channels.newOutputStream(channel), null);
                            if (channel != null) {
                                zza(null, channel);
                            }
                            zza(null, randomAccessFile);
                            return com_google_firebase_iid_zzy;
                        }
                    }
                    channel.position(0);
                    properties.store(Channels.newOutputStream(channel), null);
                    if (channel != null) {
                        zza(null, channel);
                    }
                    zza(null, randomAccessFile);
                    return com_google_firebase_iid_zzy;
                } catch (Throwable th3) {
                    th = th3;
                    th2 = th;
                }
                if (channel != null) {
                    zza(th2, channel);
                }
                throw th;
                zza(th2, randomAccessFile);
                throw th;
            } catch (Throwable th4) {
                th = th4;
                th2 = th;
            }
        } catch (IOException e4) {
            valueOf = String.valueOf(e4);
            Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(valueOf).length() + 21).append("Failed to write key: ").append(valueOf).toString());
            return null;
        }
    }

    private static File zzb(Context context) {
        File noBackupFilesDir = ContextCompat.getNoBackupFilesDir(context);
        if (noBackupFilesDir != null && noBackupFilesDir.isDirectory()) {
            return noBackupFilesDir;
        }
        Log.w("FirebaseInstanceId", "noBackupFilesDir doesn't exist, using regular files directory instead");
        return context.getFilesDir();
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

    private final zzy zza(File file) throws zzz, IOException {
        Throwable th = null;
        FileInputStream fileInputStream = new FileInputStream(file);
        Throwable th2;
        try {
            Throwable th3;
            Throwable th4;
            FileChannel channel = fileInputStream.getChannel();
            try {
                channel.lock(0, Long.MAX_VALUE, true);
                zzy zza = zza(channel);
                if (channel != null) {
                    zza(null, channel);
                }
                zza(null, fileInputStream);
                return zza;
            } catch (Throwable th5) {
                th3 = th5;
                th4 = th;
            }
            zza(th, fileInputStream);
            throw th2;
            if (channel != null) {
                zza(th4, channel);
            }
            throw th3;
        } catch (Throwable th6) {
            th2 = th6;
            th = th;
        }
    }

    private static zzy zza(FileChannel fileChannel) throws zzz, IOException {
        Properties properties = new Properties();
        properties.load(Channels.newInputStream(fileChannel));
        String property = properties.getProperty("pub");
        String property2 = properties.getProperty("pri");
        if (property == null || property2 == null) {
            throw new zzz("Invalid properties file");
        }
        try {
            return new zzy(zzc(property, property2), Long.parseLong(properties.getProperty("cre")));
        } catch (Exception e) {
            throw new zzz(e);
        }
    }

    private static zzy zza(SharedPreferences sharedPreferences, String str) throws zzz {
        String string = sharedPreferences.getString(zzav.zzd(str, "|P|"), null);
        String string2 = sharedPreferences.getString(zzav.zzd(str, "|K|"), null);
        if (string == null || string2 == null) {
            return null;
        }
        return new zzy(zzc(string, string2), zzb(sharedPreferences, str));
    }

    private final void zza(Context context, String str, zzy com_google_firebase_iid_zzy) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.google.android.gms.appid", 0);
        try {
            if (com_google_firebase_iid_zzy.equals(zza(sharedPreferences, str))) {
                return;
            }
        } catch (zzz e) {
        }
        if (Log.isLoggable("FirebaseInstanceId", 3)) {
            Log.d("FirebaseInstanceId", "Writing key to shared preferences");
        }
        Editor edit = sharedPreferences.edit();
        edit.putString(zzav.zzd(str, "|P|"), com_google_firebase_iid_zzy.zzu());
        edit.putString(zzav.zzd(str, "|K|"), com_google_firebase_iid_zzy.zzv());
        edit.putString(zzav.zzd(str, "cre"), String.valueOf(com_google_firebase_iid_zzy.zzbp));
        edit.commit();
    }

    private static long zzb(SharedPreferences sharedPreferences, String str) {
        String string = sharedPreferences.getString(zzav.zzd(str, "cre"), null);
        if (string != null) {
            try {
                return Long.parseLong(string);
            } catch (NumberFormatException e) {
            }
        }
        return 0;
    }

    private static /* synthetic */ void zza(Throwable th, FileChannel fileChannel) {
        if (th != null) {
            try {
                fileChannel.close();
                return;
            } catch (Throwable th2) {
                zza.zza(th, th2);
                return;
            }
        }
        fileChannel.close();
    }

    private static /* synthetic */ void zza(Throwable th, RandomAccessFile randomAccessFile) {
        if (th != null) {
            try {
                randomAccessFile.close();
                return;
            } catch (Throwable th2) {
                zza.zza(th, th2);
                return;
            }
        }
        randomAccessFile.close();
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
}
