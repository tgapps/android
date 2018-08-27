package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import java.io.IOException;

final class zzax implements Runnable {
    private final zzam zzak;
    private final zzaz zzan;
    private final long zzde;
    private final WakeLock zzdf = ((PowerManager) getContext().getSystemService("power")).newWakeLock(1, "fiid-sync");
    private final FirebaseInstanceId zzdg;

    zzax(FirebaseInstanceId firebaseInstanceId, zzam com_google_firebase_iid_zzam, zzaz com_google_firebase_iid_zzaz, long j) {
        this.zzdg = firebaseInstanceId;
        this.zzak = com_google_firebase_iid_zzam;
        this.zzan = com_google_firebase_iid_zzaz;
        this.zzde = j;
        this.zzdf.setReferenceCounted(false);
    }

    public final void run() {
        this.zzdf.acquire();
        try {
            this.zzdg.zza(true);
            if (!this.zzdg.zzm()) {
                this.zzdg.zza(false);
            } else if (zzan()) {
                if (zzal() && zzam() && this.zzan.zzc(this.zzdg)) {
                    this.zzdg.zza(false);
                } else {
                    this.zzdg.zza(this.zzde);
                }
                this.zzdf.release();
            } else {
                new zzay(this).zzao();
                this.zzdf.release();
            }
        } finally {
            this.zzdf.release();
        }
    }

    private final boolean zzal() {
        try {
            if (!this.zzdg.zzn()) {
                this.zzdg.zzo();
            }
            return true;
        } catch (IOException e) {
            String str = "FirebaseInstanceId";
            String str2 = "Build channel failed: ";
            String valueOf = String.valueOf(e.getMessage());
            Log.e(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
            return false;
        }
    }

    private final boolean zzam() {
        Exception e;
        String str;
        String valueOf;
        zzaw zzi = this.zzdg.zzi();
        if (zzi != null && !zzi.zzj(this.zzak.zzac())) {
            return true;
        }
        String zzj;
        try {
            zzj = this.zzdg.zzj();
            if (zzj == null) {
                Log.e("FirebaseInstanceId", "Token retrieval failed: null");
                return false;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Token successfully retrieved");
            }
            if (zzi != null && (zzi == null || zzj.equals(zzi.zzbn))) {
                return true;
            }
            Context context = getContext();
            Intent intent = new Intent("com.google.firebase.messaging.NEW_TOKEN");
            intent.putExtra("token", zzj);
            zzau.zzc(context, intent);
            zzau.zzb(context, new Intent("com.google.firebase.iid.TOKEN_REFRESH"));
            return true;
        } catch (IOException e2) {
            e = e2;
            str = "FirebaseInstanceId";
            zzj = "Token retrieval failed: ";
            valueOf = String.valueOf(e.getMessage());
            Log.e(str, valueOf.length() == 0 ? zzj.concat(valueOf) : new String(zzj));
            return false;
        } catch (SecurityException e3) {
            e = e3;
            str = "FirebaseInstanceId";
            zzj = "Token retrieval failed: ";
            valueOf = String.valueOf(e.getMessage());
            if (valueOf.length() == 0) {
            }
            Log.e(str, valueOf.length() == 0 ? zzj.concat(valueOf) : new String(zzj));
            return false;
        }
    }

    final Context getContext() {
        return this.zzdg.zzg().getApplicationContext();
    }

    final boolean zzan() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
