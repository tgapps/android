package com.google.firebase.iid;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import android.util.Log;

public class FirebaseInstanceIdService extends zzb {
    @VisibleForTesting
    private static Object zzckB = new Object();
    @VisibleForTesting
    private static boolean zzckC = false;
    private boolean zzckD = false;

    static class zza extends BroadcastReceiver {
        @Nullable
        private static BroadcastReceiver receiver;
        private int zzckE;

        private zza(int i) {
            this.zzckE = i;
        }

        static synchronized void zzl(Context context, int i) {
            synchronized (zza.class) {
                if (receiver == null) {
                    receiver = new zza(i);
                    context.getApplicationContext().registerReceiver(receiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                }
            }
        }

        public void onReceive(Context context, Intent intent) {
            synchronized (zza.class) {
                if (receiver != this) {
                } else if (FirebaseInstanceIdService.zzbJ(context)) {
                    if (Log.isLoggable("FirebaseInstanceId", 3)) {
                        Log.d("FirebaseInstanceId", "connectivity changed. starting background sync.");
                    }
                    context.getApplicationContext().unregisterReceiver(this);
                    receiver = null;
                    zzq.zzJX().zze(context, FirebaseInstanceIdService.zzbZ(this.zzckE));
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void zza(android.content.Context r2, com.google.firebase.iid.FirebaseInstanceId r3) {
        /*
        r1 = zzckB;
        monitor-enter(r1);
        r0 = zzckC;	 Catch:{ all -> 0x0026 }
        if (r0 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
    L_0x0008:
        return;
    L_0x0009:
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
        r0 = r3.zzJQ();
        if (r0 == 0) goto L_0x0022;
    L_0x0010:
        r1 = com.google.firebase.iid.zzj.zzbgW;
        r0 = r0.zzhp(r1);
        if (r0 != 0) goto L_0x0022;
    L_0x0018:
        r0 = com.google.firebase.iid.FirebaseInstanceId.zzJS();
        r0 = r0.zzJV();
        if (r0 == 0) goto L_0x0008;
    L_0x0022:
        zzbI(r2);
        goto L_0x0008;
    L_0x0026:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0026 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.FirebaseInstanceIdService.zza(android.content.Context, com.google.firebase.iid.FirebaseInstanceId):void");
    }

    private final void zza(Intent intent, String str) {
        int i = 28800;
        boolean zzbJ = zzbJ(this);
        int intExtra = intent == null ? 10 : intent.getIntExtra("next_retry_delay_in_seconds", 0);
        if (intExtra < 10 && !zzbJ) {
            i = 30;
        } else if (intExtra < 10) {
            i = 10;
        } else if (intExtra <= 28800) {
            i = intExtra;
        }
        Log.d("FirebaseInstanceId", new StringBuilder(String.valueOf(str).length() + 47).append("background sync failed: ").append(str).append(", retry in ").append(i).append("s").toString());
        synchronized (zzckB) {
            ((AlarmManager) getSystemService("alarm")).set(3, SystemClock.elapsedRealtime() + ((long) (i * 1000)), zzq.zza(this, 0, zzbZ(i << 1), 134217728));
            zzckC = true;
        }
        if (!zzbJ) {
            if (this.zzckD) {
                Log.d("FirebaseInstanceId", "device not connected. Connectivity change received registered");
            }
            zza.zzl(this, i);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void zza(android.content.Intent r9, boolean r10, boolean r11) {
        /*
        r8 = this;
        r2 = 1;
        r1 = 0;
        r3 = zzckB;
        monitor-enter(r3);
        r0 = 0;
        zzckC = r0;	 Catch:{ all -> 0x0010 }
        monitor-exit(r3);	 Catch:{ all -> 0x0010 }
        r0 = com.google.firebase.iid.zzl.zzbd(r8);
        if (r0 != 0) goto L_0x0013;
    L_0x000f:
        return;
    L_0x0010:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0010 }
        throw r0;
    L_0x0013:
        r0 = com.google.firebase.iid.FirebaseInstanceId.getInstance();
        r3 = r0.zzJQ();
        if (r3 == 0) goto L_0x0025;
    L_0x001d:
        r4 = com.google.firebase.iid.zzj.zzbgW;
        r4 = r3.zzhp(r4);
        if (r4 == 0) goto L_0x0068;
    L_0x0025:
        r1 = r0.zzJR();	 Catch:{ IOException -> 0x004d, SecurityException -> 0x005d }
        if (r1 == 0) goto L_0x0056;
    L_0x002b:
        r2 = r8.zzckD;	 Catch:{ IOException -> 0x004d, SecurityException -> 0x005d }
        if (r2 == 0) goto L_0x0038;
    L_0x002f:
        r2 = "FirebaseInstanceId";
        r4 = "get master token succeeded";
        android.util.Log.d(r2, r4);	 Catch:{ IOException -> 0x004d, SecurityException -> 0x005d }
    L_0x0038:
        zza(r8, r0);	 Catch:{ IOException -> 0x004d, SecurityException -> 0x005d }
        if (r11 != 0) goto L_0x0049;
    L_0x003d:
        if (r3 == 0) goto L_0x0049;
    L_0x003f:
        if (r3 == 0) goto L_0x000f;
    L_0x0041:
        r0 = r3.zzbPJ;	 Catch:{ IOException -> 0x004d, SecurityException -> 0x005d }
        r0 = r1.equals(r0);	 Catch:{ IOException -> 0x004d, SecurityException -> 0x005d }
        if (r0 != 0) goto L_0x000f;
    L_0x0049:
        r8.onTokenRefresh();	 Catch:{ IOException -> 0x004d, SecurityException -> 0x005d }
        goto L_0x000f;
    L_0x004d:
        r0 = move-exception;
        r0 = r0.getMessage();
        r8.zza(r9, r0);
        goto L_0x000f;
    L_0x0056:
        r0 = "returned token is null";
        r8.zza(r9, r0);	 Catch:{ IOException -> 0x004d, SecurityException -> 0x005d }
        goto L_0x000f;
    L_0x005d:
        r0 = move-exception;
        r1 = "FirebaseInstanceId";
        r2 = "Unable to get master token";
        android.util.Log.e(r1, r2, r0);
        goto L_0x000f;
    L_0x0068:
        r4 = com.google.firebase.iid.FirebaseInstanceId.zzJS();
        r0 = r4.zzJV();
        r3 = r0;
    L_0x0071:
        if (r3 == 0) goto L_0x00e0;
    L_0x0073:
        r0 = "!";
        r0 = r3.split(r0);
        r5 = r0.length;
        r6 = 2;
        if (r5 != r6) goto L_0x008d;
    L_0x007e:
        r5 = r0[r1];
        r6 = r0[r2];
        r0 = -1;
        r7 = r5.hashCode();	 Catch:{ IOException -> 0x00c1 }
        switch(r7) {
            case 83: goto L_0x0096;
            case 84: goto L_0x008a;
            case 85: goto L_0x00a1;
            default: goto L_0x008a;
        };
    L_0x008a:
        switch(r0) {
            case 0: goto L_0x00ac;
            case 1: goto L_0x00cb;
            default: goto L_0x008d;
        };
    L_0x008d:
        r4.zzhj(r3);
        r0 = r4.zzJV();
        r3 = r0;
        goto L_0x0071;
    L_0x0096:
        r7 = "S";
        r5 = r5.equals(r7);	 Catch:{ IOException -> 0x00c1 }
        if (r5 == 0) goto L_0x008a;
    L_0x009f:
        r0 = r1;
        goto L_0x008a;
    L_0x00a1:
        r7 = "U";
        r5 = r5.equals(r7);	 Catch:{ IOException -> 0x00c1 }
        if (r5 == 0) goto L_0x008a;
    L_0x00aa:
        r0 = r2;
        goto L_0x008a;
    L_0x00ac:
        r0 = com.google.firebase.iid.FirebaseInstanceId.getInstance();	 Catch:{ IOException -> 0x00c1 }
        r0.zzhg(r6);	 Catch:{ IOException -> 0x00c1 }
        r0 = r8.zzckD;	 Catch:{ IOException -> 0x00c1 }
        if (r0 == 0) goto L_0x008d;
    L_0x00b7:
        r0 = "FirebaseInstanceId";
        r5 = "subscribe operation succeeded";
        android.util.Log.d(r0, r5);	 Catch:{ IOException -> 0x00c1 }
        goto L_0x008d;
    L_0x00c1:
        r0 = move-exception;
        r0 = r0.getMessage();
        r8.zza(r9, r0);
        goto L_0x000f;
    L_0x00cb:
        r0 = com.google.firebase.iid.FirebaseInstanceId.getInstance();	 Catch:{ IOException -> 0x00c1 }
        r0.zzhh(r6);	 Catch:{ IOException -> 0x00c1 }
        r0 = r8.zzckD;	 Catch:{ IOException -> 0x00c1 }
        if (r0 == 0) goto L_0x008d;
    L_0x00d6:
        r0 = "FirebaseInstanceId";
        r5 = "unsubscribe operation succeeded";
        android.util.Log.d(r0, r5);	 Catch:{ IOException -> 0x00c1 }
        goto L_0x008d;
    L_0x00e0:
        r0 = "FirebaseInstanceId";
        r1 = "topic sync succeeded";
        android.util.Log.d(r0, r1);
        goto L_0x000f;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.FirebaseInstanceIdService.zza(android.content.Intent, boolean, boolean):void");
    }

    static void zzbI(Context context) {
        if (zzl.zzbd(context) != null) {
            synchronized (zzckB) {
                if (!zzckC) {
                    zzq.zzJX().zze(context, zzbZ(0));
                    zzckC = true;
                }
            }
        }
    }

    private static boolean zzbJ(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static Intent zzbZ(int i) {
        Intent intent = new Intent("ACTION_TOKEN_REFRESH_RETRY");
        intent.putExtra("next_retry_delay_in_seconds", i);
        return intent;
    }

    private final zzj zzhi(String str) {
        if (str == null) {
            return zzj.zzb(this, null);
        }
        Bundle bundle = new Bundle();
        bundle.putString("subtype", str);
        return zzj.zzb(this, bundle);
    }

    private static String zzp(Intent intent) {
        String stringExtra = intent.getStringExtra("subtype");
        return stringExtra == null ? "" : stringExtra;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleIntent(android.content.Intent r10) {
        /*
        r9 = this;
        r1 = 0;
        r8 = 1;
        r0 = r10.getAction();
        if (r0 != 0) goto L_0x000b;
    L_0x0008:
        r0 = "";
    L_0x000b:
        r2 = -1;
        r3 = r0.hashCode();
        switch(r3) {
            case -1737547627: goto L_0x009b;
            default: goto L_0x0013;
        };
    L_0x0013:
        r0 = r2;
    L_0x0014:
        switch(r0) {
            case 0: goto L_0x00a7;
            default: goto L_0x0017;
        };
    L_0x0017:
        r0 = zzp(r10);
        r2 = r9.zzhi(r0);
        r3 = "CMD";
        r3 = r10.getStringExtra(r3);
        r4 = r9.zzckD;
        if (r4 == 0) goto L_0x007e;
    L_0x002a:
        r4 = "FirebaseInstanceId";
        r5 = r10.getExtras();
        r5 = java.lang.String.valueOf(r5);
        r6 = java.lang.String.valueOf(r0);
        r6 = r6.length();
        r6 = r6 + 18;
        r7 = java.lang.String.valueOf(r3);
        r7 = r7.length();
        r6 = r6 + r7;
        r7 = java.lang.String.valueOf(r5);
        r7 = r7.length();
        r6 = r6 + r7;
        r7 = new java.lang.StringBuilder;
        r7.<init>(r6);
        r6 = "Service command ";
        r6 = r7.append(r6);
        r6 = r6.append(r0);
        r7 = " ";
        r6 = r6.append(r7);
        r6 = r6.append(r3);
        r7 = " ";
        r6 = r6.append(r7);
        r5 = r6.append(r5);
        r5 = r5.toString();
        android.util.Log.d(r4, r5);
    L_0x007e:
        r4 = "unregistered";
        r4 = r10.getStringExtra(r4);
        if (r4 == 0) goto L_0x00ab;
    L_0x0087:
        r1 = com.google.firebase.iid.zzj.zzJT();
        if (r0 != 0) goto L_0x0090;
    L_0x008d:
        r0 = "";
    L_0x0090:
        r1.zzdr(r0);
        r0 = com.google.firebase.iid.zzj.zzJU();
        r0.zzi(r10);
    L_0x009a:
        return;
    L_0x009b:
        r3 = "ACTION_TOKEN_REFRESH_RETRY";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x0013;
    L_0x00a4:
        r0 = r1;
        goto L_0x0014;
    L_0x00a7:
        r9.zza(r10, r1, r1);
        goto L_0x009a;
    L_0x00ab:
        r4 = "gcm.googleapis.com/refresh";
        r5 = "from";
        r5 = r10.getStringExtra(r5);
        r4 = r4.equals(r5);
        if (r4 == 0) goto L_0x00c6;
    L_0x00bb:
        r2 = com.google.firebase.iid.zzj.zzJT();
        r2.zzdr(r0);
        r9.zza(r10, r1, r8);
        goto L_0x009a;
    L_0x00c6:
        r4 = "RST";
        r4 = r4.equals(r3);
        if (r4 == 0) goto L_0x00d6;
    L_0x00cf:
        r2.zzvL();
        r9.zza(r10, r8, r8);
        goto L_0x009a;
    L_0x00d6:
        r4 = "RST_FULL";
        r4 = r4.equals(r3);
        if (r4 == 0) goto L_0x00f7;
    L_0x00df:
        r0 = com.google.firebase.iid.zzj.zzJT();
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x009a;
    L_0x00e9:
        r2.zzvL();
        r0 = com.google.firebase.iid.zzj.zzJT();
        r0.zzvP();
        r9.zza(r10, r8, r8);
        goto L_0x009a;
    L_0x00f7:
        r2 = "SYNC";
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x010b;
    L_0x0100:
        r2 = com.google.firebase.iid.zzj.zzJT();
        r2.zzdr(r0);
        r9.zza(r10, r1, r8);
        goto L_0x009a;
    L_0x010b:
        r0 = "PING";
        r0 = r0.equals(r3);
        if (r0 == 0) goto L_0x009a;
    L_0x0114:
        r0 = r10.getExtras();
        r1 = com.google.firebase.iid.zzl.zzbd(r9);
        if (r1 != 0) goto L_0x0129;
    L_0x011e:
        r0 = "FirebaseInstanceId";
        r1 = "Unable to respond to ping due to missing target package";
        android.util.Log.w(r0, r1);
        goto L_0x009a;
    L_0x0129:
        r2 = new android.content.Intent;
        r3 = "com.google.android.gcm.intent.SEND";
        r2.<init>(r3);
        r2.setPackage(r1);
        r2.putExtras(r0);
        com.google.firebase.iid.zzl.zzd(r9, r2);
        r0 = "google.to";
        r1 = "google.com/iid";
        r2.putExtra(r0, r1);
        r0 = "google.message_id";
        r1 = com.google.firebase.iid.zzl.zzvO();
        r2.putExtra(r0, r1);
        r0 = "com.google.android.gtalkservice.permission.GTALK_SERVICE";
        r9.sendOrderedBroadcast(r2, r0);
        goto L_0x009a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.iid.FirebaseInstanceIdService.handleIntent(android.content.Intent):void");
    }

    @WorkerThread
    public void onTokenRefresh() {
    }

    protected final Intent zzn(Intent intent) {
        return (Intent) zzq.zzJX().zzckP.poll();
    }

    public final boolean zzo(Intent intent) {
        this.zzckD = Log.isLoggable("FirebaseInstanceId", 3);
        if (intent.getStringExtra("error") == null && intent.getStringExtra("registration_id") == null) {
            return false;
        }
        String zzp = zzp(intent);
        if (this.zzckD) {
            String str = "FirebaseInstanceId";
            String str2 = "Register result in service ";
            String valueOf = String.valueOf(zzp);
            Log.d(str, valueOf.length() != 0 ? str2.concat(valueOf) : new String(str2));
        }
        zzhi(zzp);
        zzj.zzJU().zzi(intent);
        return true;
    }
}
