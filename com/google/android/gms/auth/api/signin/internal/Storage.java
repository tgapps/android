package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import org.json.JSONException;

public class Storage {
    private static final Lock zzaf = new ReentrantLock();
    @GuardedBy("sLk")
    private static Storage zzag;
    private final Lock zzah = new ReentrantLock();
    @GuardedBy("mLk")
    private final SharedPreferences zzai;

    private Storage(Context context) {
        this.zzai = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }

    public static Storage getInstance(Context context) {
        Preconditions.checkNotNull(context);
        zzaf.lock();
        try {
            if (zzag == null) {
                zzag = new Storage(context.getApplicationContext());
            }
            Storage storage = zzag;
            return storage;
        } finally {
            zzaf.unlock();
        }
    }

    private static String zza(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder((1 + String.valueOf(str).length()) + String.valueOf(str2).length());
        stringBuilder.append(str);
        stringBuilder.append(":");
        stringBuilder.append(str2);
        return stringBuilder.toString();
    }

    @Nullable
    private final GoogleSignInAccount zzb(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        str = getFromStore(zza("googleSignInAccount", str));
        if (str != null) {
            try {
                return GoogleSignInAccount.fromJsonString(str);
            } catch (JSONException e) {
            }
        }
        return null;
    }

    @Nullable
    protected String getFromStore(String str) {
        this.zzah.lock();
        try {
            str = this.zzai.getString(str, null);
            return str;
        } finally {
            this.zzah.unlock();
        }
    }

    @Nullable
    public GoogleSignInAccount getSavedDefaultGoogleSignInAccount() {
        return zzb(getFromStore("defaultGoogleSignInAccount"));
    }
}
