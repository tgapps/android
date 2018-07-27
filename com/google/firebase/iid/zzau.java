package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Map;

final class zzau {
    private final SharedPreferences zzct;
    private final zzw zzcu;
    private final Map<String, zzx> zzcv;
    private final Context zzv;

    public zzau(Context context) {
        this(context, new zzw());
    }

    private zzau(Context context, zzw com_google_firebase_iid_zzw) {
        this.zzcv = new ArrayMap();
        this.zzv = context;
        this.zzct = context.getSharedPreferences("com.google.android.gms.appid", 0);
        this.zzcu = com_google_firebase_iid_zzw;
        File file = new File(ContextCompat.getNoBackupFilesDir(this.zzv), "com.google.android.gms.appid-no-backup");
        if (!file.exists()) {
            try {
                if (file.createNewFile() && !isEmpty()) {
                    Log.i("FirebaseInstanceId", "App restored, clearing state");
                    zzak();
                    FirebaseInstanceId.getInstance().zzl();
                }
            } catch (IOException e) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    String str = "FirebaseInstanceId";
                    String str2 = "Error creating file in no backup dir: ";
                    String valueOf = String.valueOf(e.getMessage());
                    Log.d(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
                }
            }
        }
    }

    private final synchronized boolean isEmpty() {
        return this.zzct.getAll().isEmpty();
    }

    private static String zzb(String str, String str2, String str3) {
        return new StringBuilder(((String.valueOf(str).length() + 4) + String.valueOf(str2).length()) + String.valueOf(str3).length()).append(str).append("|T|").append(str2).append("|").append(str3).toString();
    }

    static String zzc(String str, String str2) {
        return new StringBuilder((String.valueOf(str).length() + 3) + String.valueOf(str2).length()).append(str).append("|S|").append(str2).toString();
    }

    public final synchronized void zza(String str, String str2, String str3, String str4, String str5) {
        String zza = zzav.zza(str4, str5, System.currentTimeMillis());
        if (zza != null) {
            Editor edit = this.zzct.edit();
            edit.putString(zzb(str, str2, str3), zza);
            edit.commit();
        }
    }

    public final synchronized String zzaj() {
        return this.zzct.getString("topic_operaion_queue", TtmlNode.ANONYMOUS_REGION_ID);
    }

    public final synchronized void zzak() {
        this.zzcv.clear();
        zzw.zza(this.zzv);
        this.zzct.edit().clear().commit();
    }

    public final synchronized zzav zzc(String str, String str2, String str3) {
        return zzav.zzi(this.zzct.getString(zzb(str, str2, str3), null));
    }

    public final synchronized void zzf(String str) {
        this.zzct.edit().putString("topic_operaion_queue", str).apply();
    }

    public final synchronized zzx zzg(String str) {
        zzx com_google_firebase_iid_zzx;
        com_google_firebase_iid_zzx = (zzx) this.zzcv.get(str);
        if (com_google_firebase_iid_zzx == null) {
            try {
                com_google_firebase_iid_zzx = this.zzcu.zzb(this.zzv, str);
            } catch (zzy e) {
                Log.w("FirebaseInstanceId", "Stored data is corrupt, generating new identity");
                FirebaseInstanceId.getInstance().zzl();
                com_google_firebase_iid_zzx = this.zzcu.zzc(this.zzv, str);
            }
            this.zzcv.put(str, com_google_firebase_iid_zzx);
        }
        return com_google_firebase_iid_zzx;
    }

    public final synchronized void zzh(String str) {
        String concat = String.valueOf(str).concat("|T|");
        Editor edit = this.zzct.edit();
        for (String str2 : this.zzct.getAll().keySet()) {
            if (str2.startsWith(concat)) {
                edit.remove(str2);
            }
        }
        edit.commit();
    }
}
