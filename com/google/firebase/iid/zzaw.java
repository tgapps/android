package com.google.firebase.iid;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import java.io.IOException;

final class zzaw implements Runnable {
    private final zzal zzak;
    private final zzay zzan;
    private final long zzcy;
    private final WakeLock zzcz = ((PowerManager) getContext().getSystemService("power")).newWakeLock(1, "fiid-sync");
    private final FirebaseInstanceId zzda;

    zzaw(FirebaseInstanceId firebaseInstanceId, zzal com_google_firebase_iid_zzal, zzay com_google_firebase_iid_zzay, long j) {
        this.zzda = firebaseInstanceId;
        this.zzak = com_google_firebase_iid_zzal;
        this.zzan = com_google_firebase_iid_zzay;
        this.zzcy = j;
        this.zzcz.setReferenceCounted(false);
    }

    private final boolean zzal() {
        try {
            if (!this.zzda.zzn()) {
                this.zzda.zzo();
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
        String zzj;
        Exception e;
        String str;
        String valueOf;
        zzav zzi = this.zzda.zzi();
        if (zzi != null && !zzi.zzj(this.zzak.zzac())) {
            return true;
        }
        try {
            zzj = this.zzda.zzj();
            if (zzj == null) {
                Log.e("FirebaseInstanceId", "Token retrieval failed: null");
                return false;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                Log.d("FirebaseInstanceId", "Token successfully retrieved");
            }
            if (zzi != null && (zzi == null || zzj.equals(zzi.zzbh))) {
                return true;
            }
            Context context = getContext();
            Intent intent = new Intent("com.google.firebase.messaging.NEW_TOKEN");
            intent.putExtra("token", zzj);
            zzat.zzc(context, intent);
            zzat.zzb(context, new Intent("com.google.firebase.iid.TOKEN_REFRESH"));
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
        return this.zzda.zzg().getApplicationContext();
    }

    public final void run() {
        this.zzcz.acquire();
        try {
            this.zzda.zza(true);
            if (!this.zzda.zzm()) {
                this.zzda.zza(false);
            } else if (zzan()) {
                if (zzal() && zzam() && this.zzan.zza(this.zzda)) {
                    this.zzda.zza(false);
                } else {
                    this.zzda.zza(this.zzcy);
                }
                this.zzcz.release();
            } else {
                new zzax(this).zzao();
                this.zzcz.release();
            }
        } finally {
            this.zzcz.release();
        }
    }

    final boolean zzan() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
