package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

final class zzaa {
    private final SharedPreferences zzbrz;
    private final Context zzqs;

    public zzaa(Context context) {
        this(context, "com.google.android.gms.appid");
    }

    private zzaa(Context context, String str) {
        this.zzqs = context;
        this.zzbrz = context.getSharedPreferences(str, 0);
        String valueOf = String.valueOf(str);
        String valueOf2 = String.valueOf("-no-backup");
        File file = new File(ContextCompat.getNoBackupFilesDir(this.zzqs), valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf));
        if (!file.exists()) {
            try {
                if (file.createNewFile() && !isEmpty()) {
                    Log.i("FirebaseInstanceId", "App restored, clearing state");
                    zztd();
                    FirebaseInstanceId.getInstance().zzsk();
                }
            } catch (IOException e) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    valueOf = "FirebaseInstanceId";
                    String str2 = "Error creating file in no backup dir: ";
                    valueOf2 = String.valueOf(e.getMessage());
                    Log.d(valueOf, valueOf2.length() != 0 ? str2.concat(valueOf2) : new String(str2));
                }
            }
        }
    }

    private final synchronized boolean isEmpty() {
        return this.zzbrz.getAll().isEmpty();
    }

    private static String zzi(String str, String str2, String str3) {
        return new StringBuilder(((String.valueOf(str).length() + 4) + String.valueOf(str2).length()) + String.valueOf(str3).length()).append(str).append("|T|").append(str2).append("|").append(str3).toString();
    }

    private static String zzv(String str, String str2) {
        return new StringBuilder((String.valueOf(str).length() + 3) + String.valueOf(str2).length()).append(str).append("|S|").append(str2).toString();
    }

    public final synchronized void zza(String str, String str2, String str3, String str4, String str5) {
        String zza = zzab.zza(str4, str5, System.currentTimeMillis());
        if (zza != null) {
            Editor edit = this.zzbrz.edit();
            edit.putString(zzi(str, str2, str3), zza);
            edit.commit();
        }
    }

    public final synchronized boolean zzez(String str) {
        boolean z;
        String string = this.zzbrz.getString("topic_operaion_queue", TtmlNode.ANONYMOUS_REGION_ID);
        String valueOf = String.valueOf(",");
        String valueOf2 = String.valueOf(str);
        if (string.startsWith(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf))) {
            valueOf = String.valueOf(",");
            valueOf2 = String.valueOf(str);
            this.zzbrz.edit().putString("topic_operaion_queue", string.substring((valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf)).length())).apply();
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    final synchronized KeyPair zzfb(String str) {
        KeyPair zzsc;
        zzsc = zza.zzsc();
        long currentTimeMillis = System.currentTimeMillis();
        Editor edit = this.zzbrz.edit();
        edit.putString(zzv(str, "|P|"), Base64.encodeToString(zzsc.getPublic().getEncoded(), 11));
        edit.putString(zzv(str, "|K|"), Base64.encodeToString(zzsc.getPrivate().getEncoded(), 11));
        edit.putString(zzv(str, "cre"), Long.toString(currentTimeMillis));
        edit.commit();
        return zzsc;
    }

    public final synchronized void zzfc(String str) {
        String concat = String.valueOf(str).concat("|T|");
        Editor edit = this.zzbrz.edit();
        for (String str2 : this.zzbrz.getAll().keySet()) {
            if (str2.startsWith(concat)) {
                edit.remove(str2);
            }
        }
        edit.commit();
    }

    public final synchronized KeyPair zzfd(String str) {
        KeyPair keyPair;
        Object e;
        String string = this.zzbrz.getString(zzv(str, "|P|"), null);
        String string2 = this.zzbrz.getString(zzv(str, "|K|"), null);
        if (string == null || string2 == null) {
            keyPair = null;
        } else {
            try {
                byte[] decode = Base64.decode(string, 8);
                byte[] decode2 = Base64.decode(string2, 8);
                KeyFactory instance = KeyFactory.getInstance("RSA");
                keyPair = new KeyPair(instance.generatePublic(new X509EncodedKeySpec(decode)), instance.generatePrivate(new PKCS8EncodedKeySpec(decode2)));
            } catch (InvalidKeySpecException e2) {
                e = e2;
                string = String.valueOf(e);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(string).length() + 19).append("Invalid key stored ").append(string).toString());
                FirebaseInstanceId.getInstance().zzsk();
                keyPair = null;
                return keyPair;
            } catch (NoSuchAlgorithmException e3) {
                e = e3;
                string = String.valueOf(e);
                Log.w("FirebaseInstanceId", new StringBuilder(String.valueOf(string).length() + 19).append("Invalid key stored ").append(string).toString());
                FirebaseInstanceId.getInstance().zzsk();
                keyPair = null;
                return keyPair;
            }
        }
        return keyPair;
    }

    public final synchronized zzab zzj(String str, String str2, String str3) {
        return zzab.zzfe(this.zzbrz.getString(zzi(str, str2, str3), null));
    }

    public final synchronized String zztc() {
        String str = null;
        synchronized (this) {
            String string = this.zzbrz.getString("topic_operaion_queue", null);
            if (string != null) {
                String[] split = string.split(",");
                if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
                    str = split[1];
                }
            }
        }
        return str;
    }

    public final synchronized void zztd() {
        this.zzbrz.edit().clear().commit();
    }
}
