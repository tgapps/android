package com.google.firebase.messaging;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.firebase.iid.zzau;
import com.google.firebase.iid.zzb;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class FirebaseMessagingService extends zzb {
    private static final Queue<String> zzdo = new ArrayDeque(10);

    public void onMessageReceived(RemoteMessage remoteMessage) {
    }

    public void onDeletedMessages() {
    }

    public void onMessageSent(String str) {
    }

    public void onSendError(String str, Exception exception) {
    }

    public void onNewToken(String str) {
    }

    protected final Intent zzb(Intent intent) {
        return zzau.zzah().zzai();
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
        if (MessagingAnalytics.shouldUploadMetrics(intent)) {
            MessagingAnalytics.logNotificationOpen(intent);
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
        if (r1 != 0) goto L_0x001a;
    L_0x0011:
        r1 = "com.google.firebase.messaging.RECEIVE_DIRECT_BOOT";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x0198;
    L_0x001a:
        r0 = "google.message_id";
        r1 = r10.getStringExtra(r0);
        r0 = android.text.TextUtils.isEmpty(r1);
        if (r0 == 0) goto L_0x006c;
    L_0x0027:
        r0 = 0;
        r0 = com.google.android.gms.tasks.Tasks.forResult(r0);
    L_0x002c:
        r3 = android.text.TextUtils.isEmpty(r1);
        if (r3 == 0) goto L_0x0080;
    L_0x0032:
        r1 = r2;
    L_0x0033:
        if (r1 != 0) goto L_0x0064;
    L_0x0035:
        r1 = "message_type";
        r1 = r10.getStringExtra(r1);
        if (r1 != 0) goto L_0x0041;
    L_0x003e:
        r1 = "gcm";
    L_0x0041:
        r3 = -1;
        r7 = r1.hashCode();
        switch(r7) {
            case -2062414158: goto L_0x00d2;
            case 102161: goto L_0x00c7;
            case 814694033: goto L_0x00ea;
            case 814800675: goto L_0x00de;
            default: goto L_0x0049;
        };
    L_0x0049:
        r2 = r3;
    L_0x004a:
        switch(r2) {
            case 0: goto L_0x00f6;
            case 1: goto L_0x0134;
            case 2: goto L_0x0139;
            case 3: goto L_0x0145;
            default: goto L_0x004d;
        };
    L_0x004d:
        r2 = "FirebaseMessaging";
        r3 = "Received message with unknown type: ";
        r1 = java.lang.String.valueOf(r1);
        r4 = r1.length();
        if (r4 == 0) goto L_0x0166;
    L_0x005d:
        r1 = r3.concat(r1);
    L_0x0061:
        android.util.Log.w(r2, r1);
    L_0x0064:
        r2 = 1;
        r1 = java.util.concurrent.TimeUnit.SECONDS;	 Catch:{ ExecutionException -> 0x01e4, InterruptedException -> 0x016d, TimeoutException -> 0x01e6 }
        com.google.android.gms.tasks.Tasks.await(r0, r2, r1);	 Catch:{ ExecutionException -> 0x01e4, InterruptedException -> 0x016d, TimeoutException -> 0x01e6 }
    L_0x006b:
        return;
    L_0x006c:
        r0 = new android.os.Bundle;
        r0.<init>();
        r3 = "google.message_id";
        r0.putString(r3, r1);
        r3 = com.google.firebase.iid.zzaa.zzc(r9);
        r0 = r3.zza(r5, r0);
        goto L_0x002c;
    L_0x0080:
        r3 = zzdo;
        r3 = r3.contains(r1);
        if (r3 == 0) goto L_0x00b0;
    L_0x0088:
        r3 = "FirebaseMessaging";
        r3 = android.util.Log.isLoggable(r3, r6);
        if (r3 == 0) goto L_0x00a8;
    L_0x0091:
        r3 = "FirebaseMessaging";
        r7 = "Received duplicate message: ";
        r1 = java.lang.String.valueOf(r1);
        r8 = r1.length();
        if (r8 == 0) goto L_0x00aa;
    L_0x00a1:
        r1 = r7.concat(r1);
    L_0x00a5:
        android.util.Log.d(r3, r1);
    L_0x00a8:
        r1 = r4;
        goto L_0x0033;
    L_0x00aa:
        r1 = new java.lang.String;
        r1.<init>(r7);
        goto L_0x00a5;
    L_0x00b0:
        r3 = zzdo;
        r3 = r3.size();
        r7 = 10;
        if (r3 < r7) goto L_0x00bf;
    L_0x00ba:
        r3 = zzdo;
        r3.remove();
    L_0x00bf:
        r3 = zzdo;
        r3.add(r1);
        r1 = r2;
        goto L_0x0033;
    L_0x00c7:
        r4 = "gcm";
        r4 = r1.equals(r4);
        if (r4 == 0) goto L_0x0049;
    L_0x00d0:
        goto L_0x004a;
    L_0x00d2:
        r2 = "deleted_messages";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0049;
    L_0x00db:
        r2 = r4;
        goto L_0x004a;
    L_0x00de:
        r2 = "send_event";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0049;
    L_0x00e7:
        r2 = r5;
        goto L_0x004a;
    L_0x00ea:
        r2 = "send_error";
        r2 = r1.equals(r2);
        if (r2 == 0) goto L_0x0049;
    L_0x00f3:
        r2 = r6;
        goto L_0x004a;
    L_0x00f6:
        r1 = com.google.firebase.messaging.MessagingAnalytics.shouldUploadMetrics(r10);
        if (r1 == 0) goto L_0x00ff;
    L_0x00fc:
        com.google.firebase.messaging.MessagingAnalytics.logNotificationReceived(r10);
    L_0x00ff:
        r1 = r10.getExtras();
        if (r1 != 0) goto L_0x010a;
    L_0x0105:
        r1 = new android.os.Bundle;
        r1.<init>();
    L_0x010a:
        r2 = "android.support.content.wakelockid";
        r1.remove(r2);
        r2 = com.google.firebase.messaging.zza.zzf(r1);
        if (r2 == 0) goto L_0x012a;
    L_0x0116:
        r2 = new com.google.firebase.messaging.zza;
        r2.<init>(r9);
        r2 = r2.zzh(r1);
        if (r2 != 0) goto L_0x0064;
    L_0x0121:
        r2 = com.google.firebase.messaging.MessagingAnalytics.shouldUploadMetrics(r10);
        if (r2 == 0) goto L_0x012a;
    L_0x0127:
        com.google.firebase.messaging.MessagingAnalytics.logNotificationForeground(r10);
    L_0x012a:
        r2 = new com.google.firebase.messaging.RemoteMessage;
        r2.<init>(r1);
        r9.onMessageReceived(r2);
        goto L_0x0064;
    L_0x0134:
        r9.onDeletedMessages();
        goto L_0x0064;
    L_0x0139:
        r1 = "google.message_id";
        r1 = r10.getStringExtra(r1);
        r9.onMessageSent(r1);
        goto L_0x0064;
    L_0x0145:
        r1 = "google.message_id";
        r1 = r10.getStringExtra(r1);
        if (r1 != 0) goto L_0x0155;
    L_0x014e:
        r1 = "message_id";
        r1 = r10.getStringExtra(r1);
    L_0x0155:
        r2 = new com.google.firebase.messaging.SendException;
        r3 = "error";
        r3 = r10.getStringExtra(r3);
        r2.<init>(r3);
        r9.onSendError(r1, r2);
        goto L_0x0064;
    L_0x0166:
        r1 = new java.lang.String;
        r1.<init>(r3);
        goto L_0x0061;
    L_0x016d:
        r0 = move-exception;
    L_0x016e:
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
        goto L_0x006b;
    L_0x0198:
        r1 = "com.google.firebase.messaging.NOTIFICATION_DISMISS";
        r1 = r1.equals(r0);
        if (r1 == 0) goto L_0x01ac;
    L_0x01a1:
        r0 = com.google.firebase.messaging.MessagingAnalytics.shouldUploadMetrics(r10);
        if (r0 == 0) goto L_0x006b;
    L_0x01a7:
        com.google.firebase.messaging.MessagingAnalytics.logNotificationDismiss(r10);
        goto L_0x006b;
    L_0x01ac:
        r1 = "com.google.firebase.messaging.NEW_TOKEN";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x01c1;
    L_0x01b5:
        r0 = "token";
        r0 = r10.getStringExtra(r0);
        r9.onNewToken(r0);
        goto L_0x006b;
    L_0x01c1:
        r1 = "FirebaseMessaging";
        r2 = "Unknown intent action: ";
        r0 = r10.getAction();
        r0 = java.lang.String.valueOf(r0);
        r3 = r0.length();
        if (r3 == 0) goto L_0x01de;
    L_0x01d5:
        r0 = r2.concat(r0);
    L_0x01d9:
        android.util.Log.d(r1, r0);
        goto L_0x006b;
    L_0x01de:
        r0 = new java.lang.String;
        r0.<init>(r2);
        goto L_0x01d9;
    L_0x01e4:
        r0 = move-exception;
        goto L_0x016e;
    L_0x01e6:
        r0 = move-exception;
        goto L_0x016e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.firebase.messaging.FirebaseMessagingService.zzd(android.content.Intent):void");
    }

    static void zzj(Bundle bundle) {
        Iterator it = bundle.keySet().iterator();
        while (it.hasNext()) {
            String str = (String) it.next();
            if (str != null && str.startsWith("google.c.")) {
                it.remove();
            }
        }
    }
}
