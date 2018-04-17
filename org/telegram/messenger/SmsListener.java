package org.telegram.messenger;

import android.content.BroadcastReceiver;

public class SmsListener extends BroadcastReceiver {
    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onReceive(android.content.Context r11, android.content.Intent r12) {
        /*
        r10 = this;
        r0 = 0;
        r1 = r12.getAction();
        r2 = "android.provider.Telephony.SMS_RECEIVED";
        r1 = r1.equals(r2);
        if (r1 != 0) goto L_0x001a;
    L_0x000d:
        r1 = r12.getAction();
        r2 = "android.provider.Telephony.NEW_OUTGOING_SMS";
        r1 = r1.equals(r2);
        r0 = r1;
        if (r1 == 0) goto L_0x008d;
    L_0x001a:
        r1 = org.telegram.messenger.AndroidUtilities.isWaitingForSms();
        if (r1 != 0) goto L_0x0021;
    L_0x0020:
        return;
    L_0x0021:
        r1 = r12.getExtras();
        if (r1 == 0) goto L_0x008d;
    L_0x0027:
        r2 = "pdus";
        r2 = r1.get(r2);	 Catch:{ Throwable -> 0x0089 }
        r2 = (java.lang.Object[]) r2;	 Catch:{ Throwable -> 0x0089 }
        r3 = r2.length;	 Catch:{ Throwable -> 0x0089 }
        r3 = new android.telephony.SmsMessage[r3];	 Catch:{ Throwable -> 0x0089 }
        r4 = "";
        r5 = 0;
        r6 = r4;
        r4 = r5;
    L_0x0037:
        r7 = r3.length;	 Catch:{ Throwable -> 0x0089 }
        if (r4 >= r7) goto L_0x005d;
    L_0x003a:
        r7 = r2[r4];	 Catch:{ Throwable -> 0x0089 }
        r7 = (byte[]) r7;	 Catch:{ Throwable -> 0x0089 }
        r7 = android.telephony.SmsMessage.createFromPdu(r7);	 Catch:{ Throwable -> 0x0089 }
        r3[r4] = r7;	 Catch:{ Throwable -> 0x0089 }
        r7 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x0089 }
        r7.<init>();	 Catch:{ Throwable -> 0x0089 }
        r7.append(r6);	 Catch:{ Throwable -> 0x0089 }
        r8 = r3[r4];	 Catch:{ Throwable -> 0x0089 }
        r8 = r8.getMessageBody();	 Catch:{ Throwable -> 0x0089 }
        r7.append(r8);	 Catch:{ Throwable -> 0x0089 }
        r7 = r7.toString();	 Catch:{ Throwable -> 0x0089 }
        r6 = r7;
        r4 = r4 + 1;
        goto L_0x0037;
    L_0x005d:
        if (r0 == 0) goto L_0x0060;
    L_0x005f:
        goto L_0x0088;
    L_0x0060:
        r4 = "[0-9]+";
        r4 = java.util.regex.Pattern.compile(r4);	 Catch:{ Throwable -> 0x0084 }
        r7 = r4.matcher(r6);	 Catch:{ Throwable -> 0x0084 }
        r8 = r7.find();	 Catch:{ Throwable -> 0x0084 }
        if (r8 == 0) goto L_0x0083;
    L_0x0070:
        r5 = r7.group(r5);	 Catch:{ Throwable -> 0x0084 }
        r8 = r5.length();	 Catch:{ Throwable -> 0x0084 }
        r9 = 3;
        if (r8 < r9) goto L_0x0083;
    L_0x007b:
        r8 = new org.telegram.messenger.SmsListener$1;	 Catch:{ Throwable -> 0x0084 }
        r8.<init>(r7);	 Catch:{ Throwable -> 0x0084 }
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r8);	 Catch:{ Throwable -> 0x0084 }
    L_0x0083:
        goto L_0x0088;
    L_0x0084:
        r4 = move-exception;
        org.telegram.messenger.FileLog.e(r4);	 Catch:{ Throwable -> 0x0089 }
    L_0x0088:
        goto L_0x008d;
    L_0x0089:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
    L_0x008d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.SmsListener.onReceive(android.content.Context, android.content.Intent):void");
    }
}
