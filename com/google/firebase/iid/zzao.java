package com.google.firebase.iid;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Map;

final class zzao {
    private final SharedPreferences zzcq;
    private final zzp zzcr;
    private final Map<String, zzq> zzcs;
    private final Context zzz;

    public zzao(Context context) {
        this(context, new zzp());
    }

    private zzao(Context context, zzp com_google_firebase_iid_zzp) {
        this.zzcs = new ArrayMap();
        this.zzz = context;
        this.zzcq = context.getSharedPreferences("com.google.android.gms.appid", 0);
        this.zzcr = com_google_firebase_iid_zzp;
        File file = new File(ContextCompat.getNoBackupFilesDir(this.zzz), "com.google.android.gms.appid-no-backup");
        if (!file.exists()) {
            try {
                if (file.createNewFile() && !isEmpty()) {
                    Log.i("FirebaseInstanceId", "App restored, clearing state");
                    zzag();
                    FirebaseInstanceId.getInstance().zzk();
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
        return this.zzcq.getAll().isEmpty();
    }

    private static String zza(String str, String str2, String str3) {
        return new StringBuilder(((String.valueOf(str).length() + 4) + String.valueOf(str2).length()) + String.valueOf(str3).length()).append(str).append("|T|").append(str2).append("|").append(str3).toString();
    }

    static String zzd(String str, String str2) {
        return new StringBuilder((String.valueOf(str).length() + 3) + String.valueOf(str2).length()).append(str).append("|S|").append(str2).toString();
    }

    public final synchronized void zza(String str, String str2, String str3, String str4, String str5) {
        String zza = zzap.zza(str4, str5, System.currentTimeMillis());
        if (zza != null) {
            Editor edit = this.zzcq.edit();
            edit.putString(zza(str, str2, str3), zza);
            edit.commit();
        }
    }

    public final synchronized String zzaf() {
        String str = null;
        synchronized (this) {
            String string = this.zzcq.getString("topic_operaion_queue", null);
            if (string != null) {
                String[] split = string.split(",");
                if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
                    str = split[1];
                }
            }
        }
        return str;
    }

    public final synchronized void zzag() {
        this.zzcs.clear();
        zzp.zza(this.zzz);
        this.zzcq.edit().clear().commit();
    }

    public final synchronized zzap zzb(String str, String str2, String str3) {
        return zzap.zzi(this.zzcq.getString(zza(str, str2, str3), null));
    }

    public final synchronized boolean zzf(String str) {
        boolean z;
        String string = this.zzcq.getString("topic_operaion_queue", TtmlNode.ANONYMOUS_REGION_ID);
        String valueOf = String.valueOf(",");
        String valueOf2 = String.valueOf(str);
        if (string.startsWith(valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf))) {
            valueOf = String.valueOf(",");
            valueOf2 = String.valueOf(str);
            this.zzcq.edit().putString("topic_operaion_queue", string.substring((valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf)).length())).apply();
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public final synchronized zzq zzg(String str) {
        zzq com_google_firebase_iid_zzq;
        com_google_firebase_iid_zzq = (zzq) this.zzcs.get(str);
        if (com_google_firebase_iid_zzq == null) {
            try {
                com_google_firebase_iid_zzq = this.zzcr.zzb(this.zzz, str);
            } catch (zzr e) {
                Log.w("FirebaseInstanceId", "Stored data is corrupt, generating new identity");
                FirebaseInstanceId.getInstance().zzk();
                com_google_firebase_iid_zzq = this.zzcr.zzc(this.zzz, str);
            }
            this.zzcs.put(str, com_google_firebase_iid_zzq);
        }
        return com_google_firebase_iid_zzq;
    }

    public final synchronized void zzh(String str) {
        String concat = String.valueOf(str).concat("|T|");
        Editor edit = this.zzcq.edit();
        for (String str2 : this.zzcq.getAll().keySet()) {
            if (str2.startsWith(concat)) {
                edit.remove(str2);
            }
        }
        edit.commit();
    }
}
