package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.iid.zzat;
import com.google.firebase.iid.zzb;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class FirebaseMessagingService extends zzb {
    private static final Queue<String> zzdl = new ArrayDeque(10);

    static void zzj(Bundle bundle) {
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str != null && str.startsWith("google.c.")) {
                it.remove();
            }
        }
    }

    static boolean zzk(Bundle bundle) {
        return bundle == null ? false : "1".equals(bundle.getString("google.c.a.e"));
    }

    public void onDeletedMessages() {
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    public void onMessageSent(String str) {
    }

    public void onNewToken(String str) {
    }

    public void onSendError(String str, Exception exception) {
    }

    protected final Intent zzb(Intent intent) {
        return zzat.zzah().zzai();
    }

    public final boolean zzc(Intent intent) {
        if (!"com.google.firebase.messaging.NOTIFICATION_OPEN".equals(intent.getAction())) {
            return false;
        }
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("pending_intent");
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (CanceledException e) {
                Log.e("FirebaseMessaging", "Notification pending intent canceled");
            }
        }
        if (zzk(intent.getExtras())) {
            zzb.zzf(this, intent);
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zzd(android.content.Intent r10) {
        /*
        r9 = this;
        r6 = 3;
        r5 = 2;
        r4 = 1;
        r2 = 0;
        r0 = r10.getAction();
        r1 = "com.google.android.c2dm.intent.RECEIVE";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x0192;
    L_0x0011:
        r0 = "google.message_id";
        r1 = r10.getStringExtra(r0);
        r0 = android.text.TextUtils.isEmpty(r1);
        if (r0 == 0) goto L_0x0063;
    L_0x001e:
        r0 = 0;
        r0 = com.google.android.gms.tasks.Tasks.forResult(r0);
    L_0x0023:
        r3 = android.text.TextUtils.isEmpty(r1);
        if (r3 == 0) goto L_0x0077;
    L_0x0029:
        r1 = r2;
    L_0x002a:
        if (r1 != 0) goto L_0x005b;
    L_0x002c:
        r1 = "message_type";
        r1 = r10.getStringExtra(r1);
        if (r1 != 0) goto L_0x0038;
    L_0x0035:
        r1 = "gcm";
    L_0x0038:
        r3 = -1;
        r7 = r1.hashCode();
        switch(r7) {
            case -2062414158: goto L_0x00c9;
            case 102161: goto L_0x00be;
            case 814694033: goto L_0x00e1;
            case 814800675: goto L_0x00d5;
            default: goto L_0x0040;
        };
    L_0x0040:
        r2 = r3;
    L_0x0041:
        switch(r2) {
            case 0: goto L_0x00ed;
            case 1: goto L_0x012e;
            case 2: goto L_0x0133;
            case 3: goto L_0x013f;
            default: goto L_0x0044;
        };
    L_0x0044:
        r2 = "FirebaseMessaging";
        r3 = "Received message with unknown type: ";
        r1 = java.lang.String.valueOf(r1);
        r4 = r1.length();
        if (r4 == 0) goto L_0x0160;
    L_0x0054:
        r1 = r3.concat(r1);
    L_0x0058:
        android.util.Log.w(r2, r1);
    L_0x005b:
        r2 = 1;
        r1 = java.util.concurrent.TimeUnit.SECONDS;	 Catch:{ ExecutionException -> 0x01e2, InterruptedException -> 0x0167, TimeoutException -> 0x01e4 }
        com.google.android.gms.tasks.Tasks.await(r0, r2, r1);	 Catch:{ ExecutionException -> 0x01e2, InterruptedException -> 0x0167, TimeoutException -> 0x01e4 }
    L_0x0062:
        return;
    L_0x0063:
        r0 = new android.os.Bundle;
        r0.<init>();
        r3 = "google.message_id";
        r0.putString(r3, r1);
        r3 = com.google.firebase.iid.zzz.zzc(r9);
        r0 = r3.zza(r5, r0);
        goto L_0x0023;
    L_0x0077:
        r3 = zzdl;
        r3 = r3.contains(r1);
        if (r3 == 0) goto L_0x00a7;
    L_0x007f:
        r3 = "FirebaseMessaging";
        r3 = android.util.Log.isLoggable(r3, r6);
        if (r3 == 0) goto L_0x009f;
    L_0x0088:
        r3 = "FirebaseMessaging";
        r7 = "Received duplicate message: ";
        r1 = java.lang.String.valueOf(r1);
        r8 = r1.length();
        if (r8 == 0) goto L_0x00a1;
    L_0x0098:
        r1 = r7.concat(r1);
    L_0x009c:
        android.util.Log.d(r3, r1);
    L_0x009f:
        r1 = r4;
        goto L_0x002a;
    L_0x00a1:
        r1 = new java.lang.String;
        r1.<init>(r7);
        goto L_0x009c;
    L_0x00a7:
        r3 = zzdl;
        r3 = r3.size();
        r7 = 10;
        if (r3 < r7) goto L_0x00b6;
    L_0x00b1:
        r3 = zzdl;
        r3.remove();
    L_0x00b6:
        r3 = zzdl;
        r3.add(r1);
        r1 = r2;
        goto L_0x002a;
    L_0x00be:
        r4 = "gcm";
        r4 = r1.equals(r4);
        if (r4 == 0) goto L_0x0040;
    L_0x00c7:
        goto L_0x0041;
    L_0x00c9:
        r2 = "deleted_messages";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0040;
    L_0x00d2:
        r2 = r4;
        goto L_0x0041;
    L_0x00d5:
        r2 = "send_event";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0040;
    L_0x00de:
        r2 = r5;
        goto L_0x0041;
    L_0x00e1:
        r2 = "send_error";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0040;
    L_0x00ea:
        r2 = r6;
        goto L_0x0041;
    L_0x00ed:
        r1 = r10.getExtras();
        r1 = zzk(r1);
        if (r1 == 0) goto L_0x00fa;
    L_0x00f7:
        com.google.firebase.messaging.zzb.zze(r9, r10);
    L_0x00fa:
        r1 = r10.getExtras();
        if (r1 != 0) goto L_0x0105;
    L_0x0100:
        r1 = new android.os.Bundle;
        r1.<init>();
    L_0x0105:
        r2 = "android.support.content.wakelockid";
        r1.remove(r2);
        r2 = com.google.firebase.messaging.zza.zzf(r1);
        if (r2 == 0) goto L_0x0124;
    L_0x0111:
        r2 = com.google.firebase.messaging.zza.zzd(r9);
        r2 = r2.zzh(r1);
        if (r2 != 0) goto L_0x005b;
    L_0x011b:
        r2 = zzk(r1);
        if (r2 == 0) goto L_0x0124;
    L_0x0121:
        com.google.firebase.messaging.zzb.zzh(r9, r10);
    L_0x0124:
        r2 = new com.google.firebase.messaging.RemoteMessage;
        r2.<init>(r1);
        r9.onMessageReceived(r2);
        goto L_0x005b;
    L_0x012e:
        r9.onDeletedMessages();
        goto L_0x005b;
    L_0x0133:
        r1 = "google.message_id";
        r1 = r10.getStringExtra(r1);
        r9.onMessageSent(r1);
        goto L_0x005b;
    L_0x013f:
        r1 = "google.message_id";
        r1 = r10.getStringExtra(r1);
        if (r1 != 0) goto L_0x014f;
    L_0x0148:
        r1 = "message_id";
        r1 = r10.getStringExtra(r1);
    L_0x014f:
        r2 = new com.google.firebase.messaging.SendException;
        r3 = "error";
        r3 = r10.getStringExtra(r3);
        r2.<init>(r3);
        r9.onSendError(r1, r2);
        goto L_0x005b;
    L_0x0160:
        r1 = new java.lang.String;
        r1.<init>(r3);
        goto L_0x0058;
    L_0x0167:
        r0 = move-exception;
    L_0x0168:
        r1 = "FirebaseMessaging";
        r0 = java.lang.String.valueOf(r0);
        r2 = java.lang.String.valueOf(r0);
        r2 = r2.length();
        r2 = r2 + 20;
        r3 = new java.lang.StringBuilder;
        r3.<init>(r2);
        r2 = "Message ack failed: ";
        r2 = r3.append(r2);
        r0 = r2.append(r0);
        r0 = r0.toString();
        android.util.Log.w(r1, r0);
        goto L_0x0062;
    L_0x0192:
        r1 = "com.google.firebase.messaging.NOTIFICATION_DISMISS";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x01aa;
    L_0x019b:
        r0 = r10.getExtras();
        r0 = zzk(r0);
        if (r0 == 0) goto L_0x0062;
    L_0x01a5:
        com.google.firebase.messaging.zzb.zzg(r9, r10);
        goto L_0x0062;
    L_0x01aa:
        r1 = "com.google.firebase.messaging.NEW_TOKEN";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x01bf;
    L_0x01b3:
        r0 = "token";
        r0 = r10.getStringExtra(r0);
        r9.onNewToken(r0);
        goto L_0x0062;
    L_0x01bf:
        r1 = "FirebaseMessaging";
        r2 = "Unknown intent action: ";
        r0 = r10.getAction();
        r0 = java.lang.String.valueOf(r0);
        r3 = r0.length();
        if (r3 == 0) goto L_0x01dc;
    L_0x01d3:
        r0 = r2.concat(r0);
    L_0x01d7:
        android.util.Log.d(r1, r0);
        goto L_0x0062;
    L_0x01dc:
        r0 = new java.lang.String;
        r0.<init>(r2);
        goto L_0x01d7;
    L_0x01e2:
        r0 = move-exception;
        goto L_0x0168;
    L_0x01e4:
        r0 = move-exception;
        goto L_0x0168;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.FirebaseMessagingService.zzd(android.content.Intent):void");
    }
}
