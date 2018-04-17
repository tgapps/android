package org.telegram.messenger;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.TL_updates;

public class GcmPushListenerService extends FirebaseMessagingService {
    public static final int NOTIFICATION_ID = 1;

    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        final Map data = message.getData();
        final long time = message.getSentTime();
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("GCM received data: ");
            stringBuilder.append(data);
            stringBuilder.append(" from: ");
            stringBuilder.append(from);
            FileLog.d(stringBuilder.toString());
        }
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                ApplicationLoader.postInitApplication();
                Utilities.stageQueue.postRunnable(new Runnable() {

                    class AnonymousClass1 implements Runnable {
                        final /* synthetic */ int val$accountFinal;
                        final /* synthetic */ TL_updates val$updates;

                        AnonymousClass1(int i, TL_updates tL_updates) {
                            this.val$accountFinal = i;
                            this.val$updates = tL_updates;
                        }

                        public void run() {
                            MessagesController.getInstance(this.val$accountFinal).processUpdates(this.val$updates, false);
                        }
                    }

                    public void run() {
                        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.GcmPushListenerService.1.1.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
                        /*
                        r0 = this;
                        r1 = r75;
                        r2 = -1;
                        r3 = 0;
                        r4 = r3;
                        r6 = org.telegram.messenger.GcmPushListenerService.AnonymousClass1.this;	 Catch:{ Throwable -> 0x1494 }
                        r6 = r1;	 Catch:{ Throwable -> 0x1494 }
                        r7 = "p";	 Catch:{ Throwable -> 0x1494 }
                        r6 = r6.get(r7);	 Catch:{ Throwable -> 0x1494 }
                        r7 = r6 instanceof java.lang.String;	 Catch:{ Throwable -> 0x1494 }
                        if (r7 != 0) goto L_0x0020;
                    L_0x0013:
                        r3 = org.telegram.messenger.GcmPushListenerService.AnonymousClass1.this;	 Catch:{ Throwable -> 0x001b }
                        r3 = org.telegram.messenger.GcmPushListenerService.this;	 Catch:{ Throwable -> 0x001b }
                        r3.onDecryptError();	 Catch:{ Throwable -> 0x001b }
                        return;
                    L_0x001b:
                        r0 = move-exception;
                    L_0x001c:
                        r3 = r2;
                        r2 = r0;
                        goto L_0x149c;
                    L_0x0020:
                        r7 = r6;	 Catch:{ Throwable -> 0x1494 }
                        r7 = (java.lang.String) r7;	 Catch:{ Throwable -> 0x1494 }
                        r8 = 8;	 Catch:{ Throwable -> 0x1494 }
                        r7 = android.util.Base64.decode(r7, r8);	 Catch:{ Throwable -> 0x1494 }
                        r9 = new org.telegram.tgnet.NativeByteBuffer;	 Catch:{ Throwable -> 0x1494 }
                        r10 = r7.length;	 Catch:{ Throwable -> 0x1494 }
                        r9.<init>(r10);	 Catch:{ Throwable -> 0x1494 }
                        r9.writeBytes(r7);	 Catch:{ Throwable -> 0x1494 }
                        r10 = 0;	 Catch:{ Throwable -> 0x1494 }
                        r9.position(r10);	 Catch:{ Throwable -> 0x1494 }
                        r11 = org.telegram.messenger.SharedConfig.pushAuthKeyId;	 Catch:{ Throwable -> 0x1494 }
                        if (r11 != 0) goto L_0x004b;
                    L_0x003a:
                        r11 = new byte[r8];	 Catch:{ Throwable -> 0x001b }
                        org.telegram.messenger.SharedConfig.pushAuthKeyId = r11;	 Catch:{ Throwable -> 0x001b }
                        r11 = org.telegram.messenger.SharedConfig.pushAuthKey;	 Catch:{ Throwable -> 0x001b }
                        r11 = org.telegram.messenger.Utilities.computeSHA1(r11);	 Catch:{ Throwable -> 0x001b }
                        r12 = r11.length;	 Catch:{ Throwable -> 0x001b }
                        r12 = r12 - r8;	 Catch:{ Throwable -> 0x001b }
                        r13 = org.telegram.messenger.SharedConfig.pushAuthKeyId;	 Catch:{ Throwable -> 0x001b }
                        java.lang.System.arraycopy(r11, r12, r13, r10, r8);	 Catch:{ Throwable -> 0x001b }
                    L_0x004b:
                        r11 = new byte[r8];	 Catch:{ Throwable -> 0x1494 }
                        r12 = 1;	 Catch:{ Throwable -> 0x1494 }
                        r9.readBytes(r11, r12);	 Catch:{ Throwable -> 0x1494 }
                        r13 = org.telegram.messenger.SharedConfig.pushAuthKeyId;	 Catch:{ Throwable -> 0x1494 }
                        r13 = java.util.Arrays.equals(r13, r11);	 Catch:{ Throwable -> 0x1494 }
                        if (r13 != 0) goto L_0x0061;
                    L_0x0059:
                        r3 = org.telegram.messenger.GcmPushListenerService.AnonymousClass1.this;	 Catch:{ Throwable -> 0x001b }
                        r3 = org.telegram.messenger.GcmPushListenerService.this;	 Catch:{ Throwable -> 0x001b }
                        r3.onDecryptError();	 Catch:{ Throwable -> 0x001b }
                        return;
                    L_0x0061:
                        r13 = 16;
                        r13 = new byte[r13];	 Catch:{ Throwable -> 0x1494 }
                        r9.readBytes(r13, r12);	 Catch:{ Throwable -> 0x1494 }
                        r14 = org.telegram.messenger.SharedConfig.pushAuthKey;	 Catch:{ Throwable -> 0x1494 }
                        r15 = 2;	 Catch:{ Throwable -> 0x1494 }
                        r14 = org.telegram.messenger.MessageKeyData.generateMessageKeyData(r14, r13, r12, r15);	 Catch:{ Throwable -> 0x1494 }
                        r5 = r9.buffer;	 Catch:{ Throwable -> 0x1494 }
                        r3 = r14.aesKey;	 Catch:{ Throwable -> 0x1494 }
                        r15 = r14.aesIv;	 Catch:{ Throwable -> 0x1494 }
                        r19 = 0;	 Catch:{ Throwable -> 0x1494 }
                        r20 = 0;	 Catch:{ Throwable -> 0x1494 }
                        r21 = 24;	 Catch:{ Throwable -> 0x1494 }
                        r12 = r7.length;	 Catch:{ Throwable -> 0x1494 }
                        r22 = r12 + -24;	 Catch:{ Throwable -> 0x1494 }
                        r16 = r5;	 Catch:{ Throwable -> 0x1494 }
                        r17 = r3;	 Catch:{ Throwable -> 0x1494 }
                        r18 = r15;	 Catch:{ Throwable -> 0x1494 }
                        org.telegram.messenger.Utilities.aesIgeEncryption(r16, r17, r18, r19, r20, r21, r22);	 Catch:{ Throwable -> 0x1494 }
                        r25 = org.telegram.messenger.SharedConfig.pushAuthKey;	 Catch:{ Throwable -> 0x1494 }
                        r26 = 96;	 Catch:{ Throwable -> 0x1494 }
                        r27 = 32;	 Catch:{ Throwable -> 0x1494 }
                        r3 = r9.buffer;	 Catch:{ Throwable -> 0x1494 }
                        r29 = 24;	 Catch:{ Throwable -> 0x1494 }
                        r5 = r9.buffer;	 Catch:{ Throwable -> 0x1494 }
                        r30 = r5.limit();	 Catch:{ Throwable -> 0x1494 }
                        r28 = r3;	 Catch:{ Throwable -> 0x1494 }
                        r3 = org.telegram.messenger.Utilities.computeSHA256(r25, r26, r27, r28, r29, r30);	 Catch:{ Throwable -> 0x1494 }
                        r5 = org.telegram.messenger.Utilities.arraysEquals(r13, r10, r3, r8);	 Catch:{ Throwable -> 0x1494 }
                        if (r5 != 0) goto L_0x00ab;
                    L_0x00a3:
                        r5 = org.telegram.messenger.GcmPushListenerService.AnonymousClass1.this;	 Catch:{ Throwable -> 0x001b }
                        r5 = org.telegram.messenger.GcmPushListenerService.this;	 Catch:{ Throwable -> 0x001b }
                        r5.onDecryptError();	 Catch:{ Throwable -> 0x001b }
                        return;
                    L_0x00ab:
                        r5 = 1;
                        r12 = r9.readInt32(r5);	 Catch:{ Throwable -> 0x1494 }
                        r15 = new byte[r12];	 Catch:{ Throwable -> 0x1494 }
                        r9.readBytes(r15, r5);	 Catch:{ Throwable -> 0x1494 }
                        r5 = new java.lang.String;	 Catch:{ Throwable -> 0x1494 }
                        r8 = "UTF-8";	 Catch:{ Throwable -> 0x1494 }
                        r5.<init>(r15, r8);	 Catch:{ Throwable -> 0x1494 }
                        r8 = new org.json.JSONObject;	 Catch:{ Throwable -> 0x1494 }
                        r8.<init>(r5);	 Catch:{ Throwable -> 0x1494 }
                        r10 = "custom";	 Catch:{ Throwable -> 0x1494 }
                        r10 = r8.getJSONObject(r10);	 Catch:{ Throwable -> 0x1494 }
                        r31 = r2;
                        r2 = "user_id";	 Catch:{ Throwable -> 0x148d }
                        r2 = r8.has(r2);	 Catch:{ Throwable -> 0x148d }
                        if (r2 == 0) goto L_0x00de;
                    L_0x00d1:
                        r2 = "user_id";	 Catch:{ Throwable -> 0x00d8 }
                        r2 = r8.get(r2);	 Catch:{ Throwable -> 0x00d8 }
                        goto L_0x00df;	 Catch:{ Throwable -> 0x00d8 }
                    L_0x00d8:
                        r0 = move-exception;	 Catch:{ Throwable -> 0x00d8 }
                        r2 = r0;	 Catch:{ Throwable -> 0x00d8 }
                        r3 = r31;	 Catch:{ Throwable -> 0x00d8 }
                        goto L_0x149c;	 Catch:{ Throwable -> 0x00d8 }
                    L_0x00de:
                        r2 = 0;	 Catch:{ Throwable -> 0x00d8 }
                    L_0x00df:
                        if (r2 != 0) goto L_0x00ee;	 Catch:{ Throwable -> 0x00d8 }
                    L_0x00e1:
                        r32 = r3;	 Catch:{ Throwable -> 0x00d8 }
                        r3 = org.telegram.messenger.UserConfig.selectedAccount;	 Catch:{ Throwable -> 0x00d8 }
                        r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Throwable -> 0x00d8 }
                        r3 = r3.getClientUserId();	 Catch:{ Throwable -> 0x00d8 }
                    L_0x00ed:
                        goto L_0x0116;
                    L_0x00ee:
                        r32 = r3;
                        r3 = r2 instanceof java.lang.Integer;	 Catch:{ Throwable -> 0x148d }
                        if (r3 == 0) goto L_0x00fc;
                    L_0x00f4:
                        r3 = r2;	 Catch:{ Throwable -> 0x00d8 }
                        r3 = (java.lang.Integer) r3;	 Catch:{ Throwable -> 0x00d8 }
                        r3 = r3.intValue();	 Catch:{ Throwable -> 0x00d8 }
                        goto L_0x00ed;
                    L_0x00fc:
                        r3 = r2 instanceof java.lang.String;	 Catch:{ Throwable -> 0x148d }
                        if (r3 == 0) goto L_0x010c;
                    L_0x0100:
                        r3 = r2;	 Catch:{ Throwable -> 0x00d8 }
                        r3 = (java.lang.String) r3;	 Catch:{ Throwable -> 0x00d8 }
                        r3 = org.telegram.messenger.Utilities.parseInt(r3);	 Catch:{ Throwable -> 0x00d8 }
                        r3 = r3.intValue();	 Catch:{ Throwable -> 0x00d8 }
                        goto L_0x00ed;
                    L_0x010c:
                        r3 = org.telegram.messenger.UserConfig.selectedAccount;	 Catch:{ Throwable -> 0x148d }
                        r3 = org.telegram.messenger.UserConfig.getInstance(r3);	 Catch:{ Throwable -> 0x148d }
                        r3 = r3.getClientUserId();	 Catch:{ Throwable -> 0x148d }
                    L_0x0116:
                        r16 = org.telegram.messenger.UserConfig.selectedAccount;	 Catch:{ Throwable -> 0x148d }
                        r17 = 0;
                    L_0x011a:
                        r33 = r17;
                        r34 = r2;
                        r2 = 3;
                        r35 = r4;
                        r4 = r33;
                        if (r4 >= r2) goto L_0x0141;
                    L_0x0125:
                        r2 = org.telegram.messenger.UserConfig.getInstance(r4);	 Catch:{ Throwable -> 0x0139 }
                        r2 = r2.getClientUserId();	 Catch:{ Throwable -> 0x0139 }
                        if (r2 != r3) goto L_0x0132;
                    L_0x012f:
                        r16 = r4;
                        goto L_0x0141;
                    L_0x0132:
                        r17 = r4 + 1;
                        r2 = r34;
                        r4 = r35;
                        goto L_0x011a;
                    L_0x0139:
                        r0 = move-exception;
                        r2 = r0;
                        r3 = r31;
                        r4 = r35;
                        goto L_0x149c;
                    L_0x0141:
                        r2 = r16;
                        r4 = r16;
                        r44 = r3;
                        r3 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Throwable -> 0x1487 }
                        r3 = r3.isClientActivated();	 Catch:{ Throwable -> 0x1487 }
                        if (r3 != 0) goto L_0x0152;	 Catch:{ Throwable -> 0x1487 }
                    L_0x0151:
                        return;	 Catch:{ Throwable -> 0x1487 }
                    L_0x0152:
                        r3 = "loc_key";	 Catch:{ Throwable -> 0x1487 }
                        r3 = r8.has(r3);	 Catch:{ Throwable -> 0x1487 }
                        if (r3 == 0) goto L_0x0161;	 Catch:{ Throwable -> 0x1487 }
                    L_0x015a:
                        r3 = "loc_key";	 Catch:{ Throwable -> 0x1487 }
                        r3 = r8.getString(r3);	 Catch:{ Throwable -> 0x1487 }
                        goto L_0x0163;	 Catch:{ Throwable -> 0x1487 }
                    L_0x0161:
                        r3 = "";	 Catch:{ Throwable -> 0x1487 }
                    L_0x0163:
                        r45 = r5;
                        r5 = org.telegram.messenger.GcmPushListenerService.AnonymousClass1.this;	 Catch:{ Throwable -> 0x147f }
                        r5 = r1;	 Catch:{ Throwable -> 0x147f }
                        r46 = r6;	 Catch:{ Throwable -> 0x147f }
                        r6 = "google.sent_time";	 Catch:{ Throwable -> 0x147f }
                        r5 = r5.get(r6);	 Catch:{ Throwable -> 0x147f }
                        r6 = r3.hashCode();	 Catch:{ Throwable -> 0x147f }
                        r47 = r5;
                        r5 = -920689527; // 0xffffffffc91f6489 float:-652872.56 double:NaN;
                        if (r6 == r5) goto L_0x0190;
                    L_0x017c:
                        r5 = 633004703; // 0x25bae29f float:3.241942E-16 double:3.127458774E-315;
                        if (r6 == r5) goto L_0x0182;
                    L_0x0181:
                        goto L_0x019a;
                    L_0x0182:
                        r5 = "MESSAGE_ANNOUNCEMENT";	 Catch:{ Throwable -> 0x018c }
                        r5 = r3.equals(r5);	 Catch:{ Throwable -> 0x018c }
                        if (r5 == 0) goto L_0x019a;
                    L_0x018a:
                        r5 = 1;
                        goto L_0x019b;
                    L_0x018c:
                        r0 = move-exception;
                        r4 = r3;
                        goto L_0x001c;
                    L_0x0190:
                        r5 = "DC_UPDATE";	 Catch:{ Throwable -> 0x147f }
                        r5 = r3.equals(r5);	 Catch:{ Throwable -> 0x147f }
                        if (r5 == 0) goto L_0x019a;	 Catch:{ Throwable -> 0x147f }
                    L_0x0198:
                        r5 = 0;	 Catch:{ Throwable -> 0x147f }
                        goto L_0x019b;	 Catch:{ Throwable -> 0x147f }
                    L_0x019a:
                        r5 = -1;	 Catch:{ Throwable -> 0x147f }
                    L_0x019b:
                        switch(r5) {
                            case 0: goto L_0x01f1;
                            case 1: goto L_0x01a8;
                            default: goto L_0x019e;
                        };	 Catch:{ Throwable -> 0x147f }
                    L_0x019e:
                        r48 = r7;	 Catch:{ Throwable -> 0x147f }
                        r51 = r9;	 Catch:{ Throwable -> 0x147f }
                        r5 = 0;	 Catch:{ Throwable -> 0x147f }
                        r7 = "channel_id";	 Catch:{ Throwable -> 0x147f }
                        goto L_0x0229;
                    L_0x01a8:
                        r5 = new org.telegram.tgnet.TLRPC$TL_updateServiceNotification;	 Catch:{ Throwable -> 0x018c }
                        r5.<init>();	 Catch:{ Throwable -> 0x018c }
                        r6 = 0;	 Catch:{ Throwable -> 0x018c }
                        r5.popup = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2;	 Catch:{ Throwable -> 0x018c }
                        r5.flags = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.GcmPushListenerService.AnonymousClass1.this;	 Catch:{ Throwable -> 0x018c }
                        r48 = r7;	 Catch:{ Throwable -> 0x018c }
                        r6 = r2;	 Catch:{ Throwable -> 0x018c }
                        r17 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Throwable -> 0x018c }
                        r6 = r6 / r17;	 Catch:{ Throwable -> 0x018c }
                        r6 = (int) r6;	 Catch:{ Throwable -> 0x018c }
                        r5.inbox_date = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = "message";	 Catch:{ Throwable -> 0x018c }
                        r6 = r8.getString(r6);	 Catch:{ Throwable -> 0x018c }
                        r5.message = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = "announcement";	 Catch:{ Throwable -> 0x018c }
                        r5.type = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = new org.telegram.tgnet.TLRPC$TL_messageMediaEmpty;	 Catch:{ Throwable -> 0x018c }
                        r6.<init>();	 Catch:{ Throwable -> 0x018c }
                        r5.media = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = new org.telegram.tgnet.TLRPC$TL_updates;	 Catch:{ Throwable -> 0x018c }
                        r6.<init>();	 Catch:{ Throwable -> 0x018c }
                        r7 = r6.updates;	 Catch:{ Throwable -> 0x018c }
                        r7.add(r5);	 Catch:{ Throwable -> 0x018c }
                        r7 = org.telegram.messenger.Utilities.stageQueue;	 Catch:{ Throwable -> 0x018c }
                        r49 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = new org.telegram.messenger.GcmPushListenerService$1$1$1;	 Catch:{ Throwable -> 0x018c }
                        r5.<init>(r4, r6);	 Catch:{ Throwable -> 0x018c }
                        r7.postRunnable(r5);	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);	 Catch:{ Throwable -> 0x018c }
                        r5.resumeNetworkMaybe();	 Catch:{ Throwable -> 0x018c }
                        return;	 Catch:{ Throwable -> 0x018c }
                    L_0x01f1:
                        r48 = r7;	 Catch:{ Throwable -> 0x018c }
                        r5 = "dc";	 Catch:{ Throwable -> 0x018c }
                        r5 = r10.getInt(r5);	 Catch:{ Throwable -> 0x018c }
                        r6 = "addr";	 Catch:{ Throwable -> 0x018c }
                        r6 = r10.getString(r6);	 Catch:{ Throwable -> 0x018c }
                        r7 = ":";	 Catch:{ Throwable -> 0x018c }
                        r7 = r6.split(r7);	 Catch:{ Throwable -> 0x018c }
                        r50 = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = r7.length;	 Catch:{ Throwable -> 0x018c }
                        r51 = r9;	 Catch:{ Throwable -> 0x018c }
                        r9 = 2;	 Catch:{ Throwable -> 0x018c }
                        if (r6 == r9) goto L_0x020e;	 Catch:{ Throwable -> 0x018c }
                    L_0x020d:
                        return;	 Catch:{ Throwable -> 0x018c }
                    L_0x020e:
                        r6 = 0;	 Catch:{ Throwable -> 0x018c }
                        r6 = r7[r6];	 Catch:{ Throwable -> 0x018c }
                        r9 = 1;	 Catch:{ Throwable -> 0x018c }
                        r9 = r7[r9];	 Catch:{ Throwable -> 0x018c }
                        r9 = java.lang.Integer.parseInt(r9);	 Catch:{ Throwable -> 0x018c }
                        r52 = r7;	 Catch:{ Throwable -> 0x018c }
                        r7 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);	 Catch:{ Throwable -> 0x018c }
                        r7.applyDatacenterAddress(r5, r6, r9);	 Catch:{ Throwable -> 0x018c }
                        r7 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);	 Catch:{ Throwable -> 0x018c }
                        r7.resumeNetworkMaybe();	 Catch:{ Throwable -> 0x018c }
                        return;
                    L_0x0229:
                        r7 = r10.has(r7);	 Catch:{ Throwable -> 0x147f }
                        if (r7 == 0) goto L_0x0238;
                    L_0x022f:
                        r7 = "channel_id";	 Catch:{ Throwable -> 0x018c }
                        r7 = r10.getInt(r7);	 Catch:{ Throwable -> 0x018c }
                        r9 = -r7;
                        r5 = (long) r9;
                        goto L_0x0239;
                    L_0x0238:
                        r7 = 0;
                    L_0x0239:
                        r9 = "from_id";	 Catch:{ Throwable -> 0x147f }
                        r9 = r10.has(r9);	 Catch:{ Throwable -> 0x147f }
                        if (r9 == 0) goto L_0x0249;
                    L_0x0241:
                        r9 = "from_id";	 Catch:{ Throwable -> 0x018c }
                        r9 = r10.getInt(r9);	 Catch:{ Throwable -> 0x018c }
                        r5 = (long) r9;
                        goto L_0x024a;
                    L_0x0249:
                        r9 = 0;
                    L_0x024a:
                        r53 = r5;
                        r5 = "chat_id";	 Catch:{ Throwable -> 0x147f }
                        r5 = r10.has(r5);	 Catch:{ Throwable -> 0x147f }
                        if (r5 == 0) goto L_0x025f;
                    L_0x0254:
                        r5 = "chat_id";	 Catch:{ Throwable -> 0x018c }
                        r5 = r10.getInt(r5);	 Catch:{ Throwable -> 0x018c }
                        r6 = -r5;
                        r55 = r5;
                        r5 = (long) r6;
                        goto L_0x0263;
                    L_0x025f:
                        r5 = r53;
                        r55 = 0;
                    L_0x0263:
                        r56 = r55;
                        r17 = 0;
                        r19 = (r5 > r17 ? 1 : (r5 == r17 ? 0 : -1));
                        if (r19 != 0) goto L_0x026c;
                    L_0x026b:
                        return;
                    L_0x026c:
                        r57 = r11;
                        r11 = "badge";	 Catch:{ Throwable -> 0x147f }
                        r11 = r8.has(r11);	 Catch:{ Throwable -> 0x147f }
                        if (r11 == 0) goto L_0x027d;
                    L_0x0276:
                        r11 = "badge";	 Catch:{ Throwable -> 0x018c }
                        r11 = r8.getInt(r11);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x027e;
                    L_0x027d:
                        r11 = 0;
                    L_0x027e:
                        if (r11 == 0) goto L_0x13ed;
                    L_0x0280:
                        r58 = r11;
                        r11 = "msg_id";	 Catch:{ Throwable -> 0x147f }
                        r11 = r10.getInt(r11);	 Catch:{ Throwable -> 0x147f }
                        r59 = r12;	 Catch:{ Throwable -> 0x147f }
                        r12 = org.telegram.messenger.MessagesController.getInstance(r2);	 Catch:{ Throwable -> 0x147f }
                        r12 = r12.dialogs_read_inbox_max;	 Catch:{ Throwable -> 0x147f }
                        r60 = r13;	 Catch:{ Throwable -> 0x147f }
                        r13 = java.lang.Long.valueOf(r5);	 Catch:{ Throwable -> 0x147f }
                        r12 = r12.get(r13);	 Catch:{ Throwable -> 0x147f }
                        r12 = (java.lang.Integer) r12;	 Catch:{ Throwable -> 0x147f }
                        if (r12 != 0) goto L_0x02bd;
                    L_0x029e:
                        r13 = org.telegram.messenger.MessagesStorage.getInstance(r2);	 Catch:{ Throwable -> 0x018c }
                        r61 = r12;	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r13 = r13.getDialogReadMax(r12, r5);	 Catch:{ Throwable -> 0x018c }
                        r12 = java.lang.Integer.valueOf(r13);	 Catch:{ Throwable -> 0x018c }
                        r13 = org.telegram.messenger.MessagesController.getInstance(r4);	 Catch:{ Throwable -> 0x018c }
                        r13 = r13.dialogs_read_inbox_max;	 Catch:{ Throwable -> 0x018c }
                        r62 = r14;	 Catch:{ Throwable -> 0x018c }
                        r14 = java.lang.Long.valueOf(r5);	 Catch:{ Throwable -> 0x018c }
                        r13.put(r14, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x02c1;
                    L_0x02bd:
                        r61 = r12;
                        r62 = r14;
                    L_0x02c1:
                        r13 = r12.intValue();	 Catch:{ Throwable -> 0x147f }
                        if (r11 > r13) goto L_0x02c8;	 Catch:{ Throwable -> 0x147f }
                    L_0x02c7:
                        return;	 Catch:{ Throwable -> 0x147f }
                    L_0x02c8:
                        r13 = "chat_from_id";	 Catch:{ Throwable -> 0x147f }
                        r13 = r10.has(r13);	 Catch:{ Throwable -> 0x147f }
                        if (r13 == 0) goto L_0x02d7;
                    L_0x02d0:
                        r13 = "chat_from_id";	 Catch:{ Throwable -> 0x018c }
                        r13 = r10.getInt(r13);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x02d8;
                    L_0x02d7:
                        r13 = 0;
                    L_0x02d8:
                        r14 = "mention";	 Catch:{ Throwable -> 0x147f }
                        r14 = r10.has(r14);	 Catch:{ Throwable -> 0x147f }
                        if (r14 == 0) goto L_0x02ea;
                    L_0x02e0:
                        r14 = "mention";	 Catch:{ Throwable -> 0x018c }
                        r14 = r10.getInt(r14);	 Catch:{ Throwable -> 0x018c }
                        if (r14 == 0) goto L_0x02ea;
                    L_0x02e8:
                        r14 = 1;
                        goto L_0x02eb;
                    L_0x02ea:
                        r14 = 0;
                    L_0x02eb:
                        r63 = r12;
                        r12 = "loc_args";	 Catch:{ Throwable -> 0x147f }
                        r12 = r8.has(r12);	 Catch:{ Throwable -> 0x147f }
                        if (r12 == 0) goto L_0x0321;
                    L_0x02f5:
                        r12 = "loc_args";	 Catch:{ Throwable -> 0x018c }
                        r12 = r8.getJSONArray(r12);	 Catch:{ Throwable -> 0x018c }
                        r64 = r8;	 Catch:{ Throwable -> 0x018c }
                        r8 = r12.length();	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.String[r8];	 Catch:{ Throwable -> 0x018c }
                        r17 = 0;	 Catch:{ Throwable -> 0x018c }
                        r65 = r17;	 Catch:{ Throwable -> 0x018c }
                        r66 = r15;	 Catch:{ Throwable -> 0x018c }
                        r15 = r8.length;	 Catch:{ Throwable -> 0x018c }
                        r67 = r4;	 Catch:{ Throwable -> 0x018c }
                        r4 = r65;	 Catch:{ Throwable -> 0x018c }
                        if (r4 >= r15) goto L_0x031d;	 Catch:{ Throwable -> 0x018c }
                    L_0x0310:
                        r15 = r12.getString(r4);	 Catch:{ Throwable -> 0x018c }
                        r8[r4] = r15;	 Catch:{ Throwable -> 0x018c }
                        r17 = r4 + 1;
                        r15 = r66;
                        r4 = r67;
                        goto L_0x0305;
                        r23 = r8;
                        goto L_0x0329;
                    L_0x0321:
                        r67 = r4;
                        r64 = r8;
                        r66 = r15;
                        r23 = 0;
                        r4 = r23;
                        r8 = 0;
                        r12 = 0;
                        r15 = 0;
                        r17 = r4[r15];	 Catch:{ Throwable -> 0x147f }
                        r15 = r17;	 Catch:{ Throwable -> 0x147f }
                        r17 = 0;	 Catch:{ Throwable -> 0x147f }
                        r18 = 0;	 Catch:{ Throwable -> 0x147f }
                        r19 = 0;	 Catch:{ Throwable -> 0x147f }
                        r20 = 0;	 Catch:{ Throwable -> 0x147f }
                        r21 = 0;	 Catch:{ Throwable -> 0x147f }
                        r68 = r8;	 Catch:{ Throwable -> 0x147f }
                        r8 = "CHAT_";	 Catch:{ Throwable -> 0x147f }
                        r8 = r3.startsWith(r8);	 Catch:{ Throwable -> 0x147f }
                        if (r8 == 0) goto L_0x0357;
                        if (r7 == 0) goto L_0x034a;
                        r8 = 1;
                        goto L_0x034b;
                        r8 = 0;
                        r19 = r8;
                        r8 = r15;
                        r17 = 1;
                        r22 = r4[r17];	 Catch:{ Throwable -> 0x018c }
                        r15 = r22;
                        r17 = r8;
                        goto L_0x0374;
                        r8 = "PINNED_";	 Catch:{ Throwable -> 0x147f }
                        r8 = r3.startsWith(r8);	 Catch:{ Throwable -> 0x147f }
                        if (r8 == 0) goto L_0x0369;	 Catch:{ Throwable -> 0x147f }
                        if (r13 == 0) goto L_0x0363;	 Catch:{ Throwable -> 0x147f }
                        r8 = 1;	 Catch:{ Throwable -> 0x147f }
                        goto L_0x0364;	 Catch:{ Throwable -> 0x147f }
                        r8 = 0;	 Catch:{ Throwable -> 0x147f }
                        r19 = r8;	 Catch:{ Throwable -> 0x147f }
                        r20 = 1;	 Catch:{ Throwable -> 0x147f }
                        goto L_0x0374;	 Catch:{ Throwable -> 0x147f }
                        r8 = "CHANNEL_";	 Catch:{ Throwable -> 0x147f }
                        r8 = r3.startsWith(r8);	 Catch:{ Throwable -> 0x147f }
                        if (r8 == 0) goto L_0x0374;	 Catch:{ Throwable -> 0x147f }
                        r8 = 1;	 Catch:{ Throwable -> 0x147f }
                        r21 = r8;	 Catch:{ Throwable -> 0x147f }
                        r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x147f }
                        if (r8 == 0) goto L_0x039f;
                        r8 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c }
                        r8.<init>();	 Catch:{ Throwable -> 0x018c }
                        r69 = r12;	 Catch:{ Throwable -> 0x018c }
                        r12 = "GCM received message notification ";	 Catch:{ Throwable -> 0x018c }
                        r8.append(r12);	 Catch:{ Throwable -> 0x018c }
                        r8.append(r3);	 Catch:{ Throwable -> 0x018c }
                        r12 = " for dialogId = ";	 Catch:{ Throwable -> 0x018c }
                        r8.append(r12);	 Catch:{ Throwable -> 0x018c }
                        r8.append(r5);	 Catch:{ Throwable -> 0x018c }
                        r12 = " mid = ";	 Catch:{ Throwable -> 0x018c }
                        r8.append(r12);	 Catch:{ Throwable -> 0x018c }
                        r8.append(r11);	 Catch:{ Throwable -> 0x018c }
                        r8 = r8.toString();	 Catch:{ Throwable -> 0x018c }
                        org.telegram.messenger.FileLog.d(r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x03a1;
                        r69 = r12;
                        r8 = r3.hashCode();	 Catch:{ Throwable -> 0x147f }
                        switch(r8) {
                            case -2091498420: goto L_0x0796;
                            case -2053872415: goto L_0x078b;
                            case -2039746363: goto L_0x0780;
                            case -1979538588: goto L_0x0775;
                            case -1979536003: goto L_0x076a;
                            case -1979535888: goto L_0x075f;
                            case -1969004705: goto L_0x0754;
                            case -1946699248: goto L_0x0749;
                            case -1528047021: goto L_0x073e;
                            case -1493579426: goto L_0x0732;
                            case -1480102982: goto L_0x0727;
                            case -1478041834: goto L_0x071c;
                            case -1474543101: goto L_0x0711;
                            case -1465695932: goto L_0x0705;
                            case -1374906292: goto L_0x06f9;
                            case -1372940586: goto L_0x06ed;
                            case -1264245338: goto L_0x06e1;
                            case -1236086700: goto L_0x06d5;
                            case -1236077786: goto L_0x06c9;
                            case -1235686303: goto L_0x06bd;
                            case -1198046100: goto L_0x06b2;
                            case -1124254527: goto L_0x06a6;
                            case -1085137927: goto L_0x069a;
                            case -1084746444: goto L_0x068e;
                            case -819729482: goto L_0x0682;
                            case -772141857: goto L_0x0676;
                            case -638310039: goto L_0x066a;
                            case -589196239: goto L_0x065e;
                            case -589193654: goto L_0x0652;
                            case -589193539: goto L_0x0646;
                            case -440169325: goto L_0x063a;
                            case -412748110: goto L_0x062e;
                            case -228518075: goto L_0x0622;
                            case -213586509: goto L_0x0616;
                            case -115582002: goto L_0x060a;
                            case -112621464: goto L_0x05fe;
                            case -108522133: goto L_0x05f2;
                            case -107572034: goto L_0x05e7;
                            case -40534265: goto L_0x05db;
                            case 65254746: goto L_0x05cf;
                            case 141040782: goto L_0x05c3;
                            case 309993049: goto L_0x05b7;
                            case 309995634: goto L_0x05ab;
                            case 309995749: goto L_0x059f;
                            case 320532812: goto L_0x0593;
                            case 328933854: goto L_0x0587;
                            case 331340546: goto L_0x057b;
                            case 344816990: goto L_0x056f;
                            case 346878138: goto L_0x0563;
                            case 350376871: goto L_0x0557;
                            case 615714517: goto L_0x054c;
                            case 715508879: goto L_0x0540;
                            case 728985323: goto L_0x0534;
                            case 731046471: goto L_0x0528;
                            case 734545204: goto L_0x051c;
                            case 802032552: goto L_0x0510;
                            case 991498806: goto L_0x0504;
                            case 1019917311: goto L_0x04f8;
                            case 1019926225: goto L_0x04ec;
                            case 1020317708: goto L_0x04e0;
                            case 1060349560: goto L_0x04d4;
                            case 1060358474: goto L_0x04c8;
                            case 1060749957: goto L_0x04bd;
                            case 1073049781: goto L_0x04b1;
                            case 1078101399: goto L_0x04a5;
                            case 1110103437: goto L_0x0499;
                            case 1160762272: goto L_0x048d;
                            case 1172918249: goto L_0x0481;
                            case 1281128640: goto L_0x0475;
                            case 1281131225: goto L_0x0469;
                            case 1281131340: goto L_0x045d;
                            case 1310789062: goto L_0x0452;
                            case 1361447897: goto L_0x0446;
                            case 1498266155: goto L_0x043a;
                            case 1547988151: goto L_0x042e;
                            case 1561464595: goto L_0x0422;
                            case 1563525743: goto L_0x0416;
                            case 1567024476: goto L_0x040a;
                            case 1810705077: goto L_0x03fe;
                            case 1815177512: goto L_0x03f2;
                            case 1963241394: goto L_0x03e6;
                            case 2014789757: goto L_0x03da;
                            case 2022049433: goto L_0x03ce;
                            case 2048733346: goto L_0x03c2;
                            case 2099392181: goto L_0x03b6;
                            case 2140162142: goto L_0x03aa;
                            default: goto L_0x03a8;
                        };
                        goto L_0x07a1;
                        r8 = "CHAT_MESSAGE_GEOLIVE";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 46;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_PHOTOS";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 34;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_NOTEXT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 21;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_CONTACT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 71;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_PHOTO_EDITED";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 52;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "LOCKED_MESSAGE";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 83;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGES";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 35;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_INVOICE";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 16;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_VIDEO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 39;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_ROUND";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 40;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_PHOTO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 38;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_AUDIO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 43;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PHONE_CALL_MISSED";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 85;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_PHOTOS";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 18;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_NOTEXT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_GIF";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 14;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_GEO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 12;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_DOC";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 8;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_GEOLIVE";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 30;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_PHOTOS";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 61;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_NOTEXT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 37;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_TITLE_EDITED";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 51;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_NOTEXT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 64;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_TEXT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_GAME";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 15;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_FWDS";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 17;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_TEXT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 36;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_GAME";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 48;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_FWDS";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 60;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_GEOLIVE";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 73;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_CONTACT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 11;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_VIDEO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 66;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_ROUND";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 67;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_PHOTO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 65;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_AUDIO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 70;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_PHOTO_SECRET";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_VIDEO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 23;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_ROUND";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 24;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_PHOTO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 22;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_AUDIO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 27;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_STICKER";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 42;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGES";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 19;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_GIF";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 47;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_GEO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 45;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_DOC";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 41;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_LEFT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 57;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_ADD_YOU";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 54;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_DELETE_MEMBER";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 55;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_SCREENSHOT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "AUTH_REGION";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 79;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CONTACT_JOINED";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 77;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_INVOICE";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 49;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "ENCRYPTION_REQUEST";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 80;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_GEOLIVE";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 13;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_DELETE_YOU";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 56;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "AUTH_UNKNOWN";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 78;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_GIF";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 76;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_GEO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 72;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_DOC";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 68;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_STICKER";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 26;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PHONE_CALL_REQUEST";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 84;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_STICKER";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 69;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_TEXT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 63;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_GAME";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 74;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGE_CONTACT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 44;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_VIDEO_SECRET";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 5;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_TEXT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 20;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_GAME";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 32;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_FWDS";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 33;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "PINNED_INVOICE";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 75;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_RETURNED";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 58;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "ENCRYPTED_MESSAGE";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 82;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "ENCRYPTION_ACCEPT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 81;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_VIDEO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 4;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_ROUND";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 7;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_PHOTO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_AUDIO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 10;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_MESSAGES";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 62;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_JOINED";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 59;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_ADD_MEMBER";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 53;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_GIF";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 31;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_GEO";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 29;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_DOC";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 25;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "MESSAGE_STICKER";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 9;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHAT_CREATED";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;	 Catch:{ Throwable -> 0x018c }
                        r8 = 50;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x07a2;	 Catch:{ Throwable -> 0x018c }
                        r8 = "CHANNEL_MESSAGE_CONTACT";	 Catch:{ Throwable -> 0x018c }
                        r8 = r3.equals(r8);	 Catch:{ Throwable -> 0x018c }
                        if (r8 == 0) goto L_0x07a1;
                        r8 = 28;
                        goto L_0x07a2;
                        r8 = -1;
                        r12 = 2131493043; // 0x7f0c00b3 float:1.8609555E38 double:1.053097487E-314;
                        switch(r8) {
                            case 0: goto L_0x1317;
                            case 1: goto L_0x12fd;
                            case 2: goto L_0x12df;
                            case 3: goto L_0x12c1;
                            case 4: goto L_0x12a3;
                            case 5: goto L_0x1284;
                            case 6: goto L_0x126e;
                            case 7: goto L_0x124f;
                            case 8: goto L_0x1230;
                            case 9: goto L_0x11ee;
                            case 10: goto L_0x11cf;
                            case 11: goto L_0x11b0;
                            case 12: goto L_0x1191;
                            case 13: goto L_0x1172;
                            case 14: goto L_0x1153;
                            case 15: goto L_0x1134;
                            case 16: goto L_0x1110;
                            case 17: goto L_0x10e2;
                            case 18: goto L_0x10b8;
                            case 19: goto L_0x108e;
                            case 20: goto L_0x1071;
                            case 21: goto L_0x1059;
                            case 22: goto L_0x103a;
                            case 23: goto L_0x101b;
                            case 24: goto L_0x0ffc;
                            case 25: goto L_0x0fdd;
                            case 26: goto L_0x0f9b;
                            case 27: goto L_0x0f7c;
                            case 28: goto L_0x0f5d;
                            case 29: goto L_0x0f3e;
                            case 30: goto L_0x0f1f;
                            case 31: goto L_0x0f00;
                            case 32: goto L_0x0ee1;
                            case 33: goto L_0x0eb2;
                            case 34: goto L_0x0e87;
                            case 35: goto L_0x0e5c;
                            case 36: goto L_0x0e39;
                            case 37: goto L_0x0e1c;
                            case 38: goto L_0x0df8;
                            case 39: goto L_0x0dd5;
                            case 40: goto L_0x0db1;
                            case 41: goto L_0x0d8d;
                            case 42: goto L_0x0d41;
                            case 43: goto L_0x0d1d;
                            case 44: goto L_0x0cf9;
                            case 45: goto L_0x0cd5;
                            case 46: goto L_0x0cb1;
                            case 47: goto L_0x0c8d;
                            case 48: goto L_0x0c64;
                            case 49: goto L_0x0c3b;
                            case 50: goto L_0x0c21;
                            case 51: goto L_0x0c07;
                            case 52: goto L_0x0bed;
                            case 53: goto L_0x0bce;
                            case 54: goto L_0x0bb4;
                            case 55: goto L_0x0b9a;
                            case 56: goto L_0x0b80;
                            case 57: goto L_0x0b66;
                            case 58: goto L_0x0b4c;
                            case 59: goto L_0x0b32;
                            case 60: goto L_0x0b02;
                            case 61: goto L_0x0ad2;
                            case 62: goto L_0x0aa2;
                            case 63: goto L_0x0a6a;
                            case 64: goto L_0x0a3b;
                            case 65: goto L_0x0a0d;
                            case 66: goto L_0x09df;
                            case 67: goto L_0x09b0;
                            case 68: goto L_0x0981;
                            case 69: goto L_0x0905;
                            case 70: goto L_0x08d6;
                            case 71: goto L_0x08a7;
                            case 72: goto L_0x0878;
                            case 73: goto L_0x0849;
                            case 74: goto L_0x081a;
                            case 75: goto L_0x07eb;
                            case 76: goto L_0x07b8;
                            case 77: goto L_0x07b3;
                            case 78: goto L_0x07b2;
                            case 79: goto L_0x07b1;
                            case 80: goto L_0x07b0;
                            case 81: goto L_0x07b0;
                            case 82: goto L_0x07b0;
                            case 83: goto L_0x07b0;
                            case 84: goto L_0x07af;
                            case 85: goto L_0x07ae;
                            default: goto L_0x07a8;
                        };
                        r70 = r5;
                        r5 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x147f }
                        goto L_0x1333;
                        goto L_0x07b4;
                        goto L_0x07b4;
                        goto L_0x07b4;
                        goto L_0x07b4;
                        goto L_0x07b4;
                        r70 = r5;
                        goto L_0x1349;
                        if (r13 == 0) goto L_0x07d6;
                        r8 = "NotificationActionPinnedGif";	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r12];	 Catch:{ Throwable -> 0x018c }
                        r22 = 0;	 Catch:{ Throwable -> 0x018c }
                        r23 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r12[r22] = r23;	 Catch:{ Throwable -> 0x018c }
                        r22 = 1;	 Catch:{ Throwable -> 0x018c }
                        r23 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r12[r22] = r23;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = 2131493930; // 0x7f0c042a float:1.8611354E38 double:1.053097925E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r8, r5, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedGifChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493931; // 0x7f0c042b float:1.8611356E38 double:1.0530979256E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x0807;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedInvoice";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493932; // 0x7f0c042c float:1.8611358E38 double:1.053097926E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedInvoiceChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493933; // 0x7f0c042d float:1.861136E38 double:1.0530979266E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x0836;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedGame";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493924; // 0x7f0c0424 float:1.8611342E38 double:1.053097922E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedGameChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493925; // 0x7f0c0425 float:1.8611344E38 double:1.0530979227E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x0865;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedGeoLive";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493928; // 0x7f0c0428 float:1.861135E38 double:1.053097924E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedGeoLiveChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493929; // 0x7f0c0429 float:1.8611352E38 double:1.0530979246E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x0894;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedGeo";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493926; // 0x7f0c0426 float:1.8611346E38 double:1.053097923E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedGeoChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493927; // 0x7f0c0427 float:1.8611348E38 double:1.0530979236E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x08c3;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedContact";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493920; // 0x7f0c0420 float:1.8611334E38 double:1.05309792E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedContactChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493921; // 0x7f0c0421 float:1.8611336E38 double:1.0530979207E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x08f2;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedVoice";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493950; // 0x7f0c043e float:1.8611395E38 double:1.053097935E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedVoiceChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493951; // 0x7f0c043f float:1.8611397E38 double:1.0530979355E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x094a;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4.length;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2;	 Catch:{ Throwable -> 0x018c }
                        if (r5 <= r6) goto L_0x0932;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4[r6];	 Catch:{ Throwable -> 0x018c }
                        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ Throwable -> 0x018c }
                        if (r5 != 0) goto L_0x0932;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedStickerEmoji";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493944; // 0x7f0c0438 float:1.8611382E38 double:1.053097932E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedSticker";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493942; // 0x7f0c0436 float:1.8611378E38 double:1.053097931E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4.length;	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        if (r5 <= r6) goto L_0x096e;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4[r6];	 Catch:{ Throwable -> 0x018c }
                        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ Throwable -> 0x018c }
                        if (r5 != 0) goto L_0x096e;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedStickerEmojiChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493945; // 0x7f0c0439 float:1.8611384E38 double:1.0530979325E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedStickerChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493943; // 0x7f0c0437 float:1.861138E38 double:1.0530979316E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x099d;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedFile";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493922; // 0x7f0c0422 float:1.8611338E38 double:1.053097921E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedFileChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493923; // 0x7f0c0423 float:1.861134E38 double:1.0530979217E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x09cc;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedRound";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493940; // 0x7f0c0434 float:1.8611374E38 double:1.05309793E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedRoundChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493941; // 0x7f0c0435 float:1.8611376E38 double:1.0530979306E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x09fa;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedVideo";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493948; // 0x7f0c043c float:1.861139E38 double:1.053097934E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedVideoChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493949; // 0x7f0c043d float:1.8611393E38 double:1.0530979345E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x0a28;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedPhoto";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493938; // 0x7f0c0432 float:1.861137E38 double:1.053097929E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedPhotoChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493939; // 0x7f0c0433 float:1.8611372E38 double:1.0530979296E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x0a57;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedNoText";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493936; // 0x7f0c0430 float:1.8611366E38 double:1.053097928E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedNoTextChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493937; // 0x7f0c0431 float:1.8611368E38 double:1.0530979286E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        if (r13 == 0) goto L_0x0a8a;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedText";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493946; // 0x7f0c043a float:1.8611386E38 double:1.053097933E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0a55;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationActionPinnedTextChannel";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493947; // 0x7f0c043b float:1.8611389E38 double:1.0530979335E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationGroupFew";	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = "messages";	 Catch:{ Throwable -> 0x018c }
                        r22 = 2;	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.intValue();	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.formatPluralString(r12, r6);	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493959; // 0x7f0c0447 float:1.8611413E38 double:1.0530979395E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x110c;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationGroupFew";	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = "Photos";	 Catch:{ Throwable -> 0x018c }
                        r22 = 2;	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.intValue();	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.formatPluralString(r12, r6);	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493959; // 0x7f0c0447 float:1.8611413E38 double:1.0530979395E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x110c;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationGroupForwardedFew";	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = "messages";	 Catch:{ Throwable -> 0x018c }
                        r22 = 2;	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.intValue();	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.formatPluralString(r12, r6);	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493960; // 0x7f0c0448 float:1.8611415E38 double:1.05309794E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x110c;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationGroupAddSelfMega";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493958; // 0x7f0c0446 float:1.861141E38 double:1.053097939E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationGroupAddSelf";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493957; // 0x7f0c0445 float:1.8611409E38 double:1.0530979385E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationGroupLeftMember";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493963; // 0x7f0c044b float:1.861142E38 double:1.0530979414E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationGroupKickYou";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493962; // 0x7f0c044a float:1.8611419E38 double:1.053097941E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationGroupKickMember";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493961; // 0x7f0c0449 float:1.8611417E38 double:1.0530979404E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationInvitedToGroup";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493964; // 0x7f0c044c float:1.8611423E38 double:1.053097942E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationGroupAddMember";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493956; // 0x7f0c0444 float:1.8611407E38 double:1.053097938E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationEditedGroupPhoto";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493955; // 0x7f0c0443 float:1.8611405E38 double:1.0530979375E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationEditedGroupName";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493954; // 0x7f0c0442 float:1.8611403E38 double:1.053097937E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationInvitedToGroup";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493964; // 0x7f0c044c float:1.8611423E38 double:1.053097942E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupInvoice";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493978; // 0x7f0c045a float:1.8611451E38 double:1.053097949E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "PaymentInvoice";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131494102; // 0x7f0c04d6 float:1.8611703E38 double:1.05309801E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupGame";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493976; // 0x7f0c0458 float:1.8611447E38 double:1.053097948E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachGame";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493027; // 0x7f0c00a3 float:1.8609523E38 double:1.053097479E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupGif";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493977; // 0x7f0c0459 float:1.861145E38 double:1.0530979484E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachGif";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493028; // 0x7f0c00a4 float:1.8609525E38 double:1.0530974795E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupLiveLocation";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493979; // 0x7f0c045b float:1.8611453E38 double:1.0530979493E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachLiveLocation";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493031; // 0x7f0c00a7 float:1.860953E38 double:1.053097481E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupMap";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493980; // 0x7f0c045c float:1.8611455E38 double:1.05309795E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachLocation";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493033; // 0x7f0c00a9 float:1.8609535E38 double:1.053097482E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupContact";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493974; // 0x7f0c0456 float:1.8611443E38 double:1.053097947E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachContact";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493025; // 0x7f0c00a1 float:1.8609518E38 double:1.053097478E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupAudio";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493973; // 0x7f0c0455 float:1.8611441E38 double:1.0530979464E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachAudio";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493023; // 0x7f0c009f float:1.8609514E38 double:1.053097477E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4.length;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2;	 Catch:{ Throwable -> 0x018c }
                        if (r5 <= r6) goto L_0x0d6b;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4[r6];	 Catch:{ Throwable -> 0x018c }
                        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ Throwable -> 0x018c }
                        if (r5 != 0) goto L_0x0d6b;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupStickerEmoji";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493986; // 0x7f0c0462 float:1.8611468E38 double:1.053097953E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0d81;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupSticker";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493985; // 0x7f0c0461 float:1.8611466E38 double:1.0530979523E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachSticker";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493040; // 0x7f0c00b0 float:1.8609549E38 double:1.0530974854E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupDocument";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493975; // 0x7f0c0457 float:1.8611445E38 double:1.0530979474E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachDocument";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493026; // 0x7f0c00a2 float:1.860952E38 double:1.0530974785E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupRound";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493984; // 0x7f0c0460 float:1.8611464E38 double:1.053097952E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachRound";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493039; // 0x7f0c00af float:1.8609547E38 double:1.053097485E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupVideo";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493988; // 0x7f0c0464 float:1.8611472E38 double:1.053097954E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r22 = 0;	 Catch:{ Throwable -> 0x018c }
                        r23 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r8[r22] = r23;	 Catch:{ Throwable -> 0x018c }
                        r22 = 1;	 Catch:{ Throwable -> 0x018c }
                        r23 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r8[r22] = r23;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachVideo";	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupPhoto";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493983; // 0x7f0c045f float:1.8611462E38 double:1.0530979513E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachPhoto";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493037; // 0x7f0c00ad float:1.8609543E38 double:1.053097484E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupNoText";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493982; // 0x7f0c045e float:1.861146E38 double:1.053097951E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "";	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGroupText";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493987; // 0x7f0c0463 float:1.861147E38 double:1.0530979533E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 3;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 2;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        r8 = r4[r6];	 Catch:{ Throwable -> 0x018c }
                        r12 = r8;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageFew";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = "messages";	 Catch:{ Throwable -> 0x018c }
                        r22 = 1;	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.intValue();	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.formatPluralString(r12, r6);	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493182; // 0x7f0c013e float:1.8609837E38 double:1.0530975556E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x110c;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageFew";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = "Photos";	 Catch:{ Throwable -> 0x018c }
                        r22 = 1;	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.intValue();	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.formatPluralString(r12, r6);	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493182; // 0x7f0c013e float:1.8609837E38 double:1.0530975556E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x110c;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageFew";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = "ForwardedMessageCount";	 Catch:{ Throwable -> 0x018c }
                        r22 = 1;	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.intValue();	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.formatPluralString(r12, r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.toLowerCase();	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493182; // 0x7f0c013e float:1.8609837E38 double:1.0530975556E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x110c;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGame";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493971; // 0x7f0c0453 float:1.8611437E38 double:1.0530979454E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachGame";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493027; // 0x7f0c00a3 float:1.8609523E38 double:1.053097479E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageGIF";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493183; // 0x7f0c013f float:1.8609839E38 double:1.053097556E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachGif";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493028; // 0x7f0c00a4 float:1.8609525E38 double:1.0530974795E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageLiveLocation";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493184; // 0x7f0c0140 float:1.860984E38 double:1.0530975566E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachLiveLocation";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493031; // 0x7f0c00a7 float:1.860953E38 double:1.053097481E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageMap";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493185; // 0x7f0c0141 float:1.8609843E38 double:1.053097557E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachLocation";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493033; // 0x7f0c00a9 float:1.8609535E38 double:1.053097482E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageContact";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493180; // 0x7f0c013c float:1.8609833E38 double:1.0530975546E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachContact";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493025; // 0x7f0c00a1 float:1.8609518E38 double:1.053097478E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageAudio";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493179; // 0x7f0c013b float:1.860983E38 double:1.053097554E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachAudio";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493023; // 0x7f0c009f float:1.8609514E38 double:1.053097477E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4.length;	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        if (r5 <= r6) goto L_0x0fc0;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4[r6];	 Catch:{ Throwable -> 0x018c }
                        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ Throwable -> 0x018c }
                        if (r5 != 0) goto L_0x0fc0;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageStickerEmoji";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493191; // 0x7f0c0147 float:1.8609855E38 double:1.05309756E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x0fd1;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageSticker";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493190; // 0x7f0c0146 float:1.8609853E38 double:1.0530975595E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachSticker";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493040; // 0x7f0c00b0 float:1.8609549E38 double:1.0530974854E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageDocument";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493181; // 0x7f0c013d float:1.8609835E38 double:1.053097555E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachDocument";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493026; // 0x7f0c00a2 float:1.860952E38 double:1.0530974785E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageRound";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493189; // 0x7f0c0145 float:1.8609851E38 double:1.053097559E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachRound";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493039; // 0x7f0c00af float:1.8609547E38 double:1.053097485E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageVideo";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493192; // 0x7f0c0148 float:1.8609857E38 double:1.0530975605E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachVideo";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493043; // 0x7f0c00b3 float:1.8609555E38 double:1.053097487E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessagePhoto";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493188; // 0x7f0c0144 float:1.860985E38 double:1.0530975585E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachPhoto";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493037; // 0x7f0c00ad float:1.8609543E38 double:1.053097484E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ChannelMessageNoText";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493187; // 0x7f0c0143 float:1.8609847E38 double:1.053097558E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "";	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageText";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageFew";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = "messages";	 Catch:{ Throwable -> 0x018c }
                        r22 = 1;	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.intValue();	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.formatPluralString(r12, r6);	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493969; // 0x7f0c0451 float:1.8611433E38 double:1.0530979444E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x110c;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageFew";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = "Photos";	 Catch:{ Throwable -> 0x018c }
                        r22 = 1;	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.intValue();	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.formatPluralString(r12, r6);	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493969; // 0x7f0c0451 float:1.8611433E38 double:1.0530979444E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x110c;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageForwardFew";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = "messages";	 Catch:{ Throwable -> 0x018c }
                        r22 = 1;	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r22];	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.Utilities.parseInt(r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = r6.intValue();	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.formatPluralString(r12, r6);	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r6;	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493970; // 0x7f0c0452 float:1.8611435E38 double:1.053097945E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        r18 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageInvoice";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493989; // 0x7f0c0465 float:1.8611474E38 double:1.0530979543E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = "PaymentInvoice";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131494102; // 0x7f0c04d6 float:1.8611703E38 double:1.05309801E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGame";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493971; // 0x7f0c0453 float:1.8611437E38 double:1.0530979454E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachGame";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493027; // 0x7f0c00a3 float:1.8609523E38 double:1.053097479E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageGif";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493972; // 0x7f0c0454 float:1.861144E38 double:1.053097946E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachGif";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493028; // 0x7f0c00a4 float:1.8609525E38 double:1.0530974795E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageLiveLocation";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493990; // 0x7f0c0466 float:1.8611476E38 double:1.053097955E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachLiveLocation";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493031; // 0x7f0c00a7 float:1.860953E38 double:1.053097481E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageMap";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493991; // 0x7f0c0467 float:1.8611478E38 double:1.0530979553E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachLocation";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493033; // 0x7f0c00a9 float:1.8609535E38 double:1.053097482E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageContact";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493967; // 0x7f0c044f float:1.861143E38 double:1.0530979434E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachContact";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493025; // 0x7f0c00a1 float:1.8609518E38 double:1.053097478E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageAudio";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493966; // 0x7f0c044e float:1.8611427E38 double:1.053097943E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachAudio";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493023; // 0x7f0c009f float:1.8609514E38 double:1.053097477E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4.length;	 Catch:{ Throwable -> 0x018c }
                        r6 = 1;	 Catch:{ Throwable -> 0x018c }
                        if (r5 <= r6) goto L_0x1213;	 Catch:{ Throwable -> 0x018c }
                        r5 = r4[r6];	 Catch:{ Throwable -> 0x018c }
                        r5 = android.text.TextUtils.isEmpty(r5);	 Catch:{ Throwable -> 0x018c }
                        if (r5 != 0) goto L_0x1213;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageStickerEmoji";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493999; // 0x7f0c046f float:1.8611494E38 double:1.053097959E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1224;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageSticker";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493998; // 0x7f0c046e float:1.8611492E38 double:1.0530979587E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachSticker";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493040; // 0x7f0c00b0 float:1.8609549E38 double:1.0530974854E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageDocument";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493968; // 0x7f0c0450 float:1.8611431E38 double:1.053097944E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachDocument";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493026; // 0x7f0c00a2 float:1.860952E38 double:1.0530974785E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageRound";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493995; // 0x7f0c046b float:1.8611486E38 double:1.053097957E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachRound";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493039; // 0x7f0c00af float:1.8609547E38 double:1.053097485E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "ActionTakeScreenshoot";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131492907; // 0x7f0c002b float:1.860928E38 double:1.0530974197E-314;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);	 Catch:{ Throwable -> 0x018c }
                        r6 = "un1";	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r8 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r5 = r5.replace(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageSDVideo";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493997; // 0x7f0c046d float:1.861149E38 double:1.053097958E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachVideo";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493043; // 0x7f0c00b3 float:1.8609555E38 double:1.053097487E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageVideo";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131494001; // 0x7f0c0471 float:1.8611498E38 double:1.05309796E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachVideo";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493043; // 0x7f0c00b3 float:1.8609555E38 double:1.053097487E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageSDPhoto";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493996; // 0x7f0c046c float:1.8611488E38 double:1.0530979577E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachPhoto";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493037; // 0x7f0c00ad float:1.8609543E38 double:1.053097484E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessagePhoto";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493994; // 0x7f0c046a float:1.8611484E38 double:1.0530979568E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "AttachPhoto";	 Catch:{ Throwable -> 0x018c }
                        r8 = 2131493037; // 0x7f0c00ad float:1.8609543E38 double:1.053097484E-314;	 Catch:{ Throwable -> 0x018c }
                        r6 = org.telegram.messenger.LocaleController.getString(r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageNoText";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131493993; // 0x7f0c0469 float:1.8611482E38 double:1.0530979563E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 1;	 Catch:{ Throwable -> 0x018c }
                        r12 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r8 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r8];	 Catch:{ Throwable -> 0x018c }
                        r12[r8] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r12);	 Catch:{ Throwable -> 0x018c }
                        r6 = "";	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        r69 = r12;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x134b;	 Catch:{ Throwable -> 0x018c }
                        r70 = r5;	 Catch:{ Throwable -> 0x018c }
                        r5 = "NotificationMessageText";	 Catch:{ Throwable -> 0x018c }
                        r6 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;	 Catch:{ Throwable -> 0x018c }
                        r8 = 2;	 Catch:{ Throwable -> 0x018c }
                        r8 = new java.lang.Object[r8];	 Catch:{ Throwable -> 0x018c }
                        r12 = 0;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r12 = 1;	 Catch:{ Throwable -> 0x018c }
                        r22 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r8[r12] = r22;	 Catch:{ Throwable -> 0x018c }
                        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r8);	 Catch:{ Throwable -> 0x018c }
                        r6 = r4[r12];	 Catch:{ Throwable -> 0x018c }
                        r12 = r6;	 Catch:{ Throwable -> 0x018c }
                        goto L_0x1314;	 Catch:{ Throwable -> 0x018c }
                        if (r5 == 0) goto L_0x1349;	 Catch:{ Throwable -> 0x018c }
                        r5 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x018c }
                        r5.<init>();	 Catch:{ Throwable -> 0x018c }
                        r6 = "unhandled loc_key = ";	 Catch:{ Throwable -> 0x018c }
                        r5.append(r6);	 Catch:{ Throwable -> 0x018c }
                        r5.append(r3);	 Catch:{ Throwable -> 0x018c }
                        r5 = r5.toString();	 Catch:{ Throwable -> 0x018c }
                        org.telegram.messenger.FileLog.w(r5);	 Catch:{ Throwable -> 0x018c }
                        r5 = r68;
                        if (r5 == 0) goto L_0x13e3;
                        r6 = new org.telegram.tgnet.TLRPC$TL_message;	 Catch:{ Throwable -> 0x147f }
                        r6.<init>();	 Catch:{ Throwable -> 0x147f }
                        r6.id = r11;	 Catch:{ Throwable -> 0x147f }
                        if (r69 == 0) goto L_0x1359;	 Catch:{ Throwable -> 0x147f }
                        r8 = r69;	 Catch:{ Throwable -> 0x147f }
                        goto L_0x135a;	 Catch:{ Throwable -> 0x147f }
                        r8 = r5;	 Catch:{ Throwable -> 0x147f }
                        r6.message = r8;	 Catch:{ Throwable -> 0x147f }
                        r8 = org.telegram.messenger.GcmPushListenerService.AnonymousClass1.this;	 Catch:{ Throwable -> 0x147f }
                        r72 = r3;
                        r73 = r4;
                        r3 = r2;	 Catch:{ Throwable -> 0x1479 }
                        r23 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Throwable -> 0x1479 }
                        r3 = r3 / r23;	 Catch:{ Throwable -> 0x1479 }
                        r3 = (int) r3;	 Catch:{ Throwable -> 0x1479 }
                        r6.date = r3;	 Catch:{ Throwable -> 0x1479 }
                        if (r20 == 0) goto L_0x1374;	 Catch:{ Throwable -> 0x1479 }
                        r3 = new org.telegram.tgnet.TLRPC$TL_messageActionPinMessage;	 Catch:{ Throwable -> 0x1479 }
                        r3.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r6.action = r3;	 Catch:{ Throwable -> 0x1479 }
                        if (r19 == 0) goto L_0x137d;	 Catch:{ Throwable -> 0x1479 }
                        r3 = r6.flags;	 Catch:{ Throwable -> 0x1479 }
                        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;	 Catch:{ Throwable -> 0x1479 }
                        r3 = r3 | r4;	 Catch:{ Throwable -> 0x1479 }
                        r6.flags = r3;	 Catch:{ Throwable -> 0x1479 }
                        if (r7 == 0) goto L_0x1393;	 Catch:{ Throwable -> 0x1479 }
                        r3 = new org.telegram.tgnet.TLRPC$TL_peerChannel;	 Catch:{ Throwable -> 0x1479 }
                        r3.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r6.to_id = r3;	 Catch:{ Throwable -> 0x1479 }
                        r3 = r6.to_id;	 Catch:{ Throwable -> 0x1479 }
                        r3.channel_id = r7;	 Catch:{ Throwable -> 0x1479 }
                        r3 = -r7;	 Catch:{ Throwable -> 0x1479 }
                        r3 = (long) r3;	 Catch:{ Throwable -> 0x1479 }
                        r6.dialog_id = r3;	 Catch:{ Throwable -> 0x1479 }
                        r74 = r11;	 Catch:{ Throwable -> 0x1479 }
                        r3 = r56;	 Catch:{ Throwable -> 0x1479 }
                        goto L_0x13b9;	 Catch:{ Throwable -> 0x1479 }
                        r3 = r56;	 Catch:{ Throwable -> 0x1479 }
                        if (r3 == 0) goto L_0x13a9;	 Catch:{ Throwable -> 0x1479 }
                        r4 = new org.telegram.tgnet.TLRPC$TL_peerChat;	 Catch:{ Throwable -> 0x1479 }
                        r4.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r6.to_id = r4;	 Catch:{ Throwable -> 0x1479 }
                        r4 = r6.to_id;	 Catch:{ Throwable -> 0x1479 }
                        r4.chat_id = r3;	 Catch:{ Throwable -> 0x1479 }
                        r4 = -r3;	 Catch:{ Throwable -> 0x1479 }
                        r74 = r11;	 Catch:{ Throwable -> 0x1479 }
                        r11 = (long) r4;	 Catch:{ Throwable -> 0x1479 }
                        r6.dialog_id = r11;	 Catch:{ Throwable -> 0x1479 }
                        goto L_0x13b9;	 Catch:{ Throwable -> 0x1479 }
                        r74 = r11;	 Catch:{ Throwable -> 0x1479 }
                        r4 = new org.telegram.tgnet.TLRPC$TL_peerUser;	 Catch:{ Throwable -> 0x1479 }
                        r4.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r6.to_id = r4;	 Catch:{ Throwable -> 0x1479 }
                        r4 = r6.to_id;	 Catch:{ Throwable -> 0x1479 }
                        r4.user_id = r9;	 Catch:{ Throwable -> 0x1479 }
                        r11 = (long) r9;	 Catch:{ Throwable -> 0x1479 }
                        r6.dialog_id = r11;	 Catch:{ Throwable -> 0x1479 }
                        r6.from_id = r13;	 Catch:{ Throwable -> 0x1479 }
                        r6.mentioned = r14;	 Catch:{ Throwable -> 0x1479 }
                        r4 = new org.telegram.messenger.MessageObject;	 Catch:{ Throwable -> 0x1479 }
                        r36 = r4;	 Catch:{ Throwable -> 0x1479 }
                        r37 = r2;	 Catch:{ Throwable -> 0x1479 }
                        r38 = r6;	 Catch:{ Throwable -> 0x1479 }
                        r39 = r5;	 Catch:{ Throwable -> 0x1479 }
                        r40 = r15;	 Catch:{ Throwable -> 0x1479 }
                        r41 = r17;	 Catch:{ Throwable -> 0x1479 }
                        r42 = r18;	 Catch:{ Throwable -> 0x1479 }
                        r43 = r21;	 Catch:{ Throwable -> 0x1479 }
                        r36.<init>(r37, r38, r39, r40, r41, r42, r43);	 Catch:{ Throwable -> 0x1479 }
                        r8 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x1479 }
                        r8.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r8.add(r4);	 Catch:{ Throwable -> 0x1479 }
                        r11 = org.telegram.messenger.NotificationsController.getInstance(r2);	 Catch:{ Throwable -> 0x1479 }
                        r12 = 1;	 Catch:{ Throwable -> 0x1479 }
                        r11.processNewMessages(r8, r12, r12);	 Catch:{ Throwable -> 0x1479 }
                        goto L_0x13e7;	 Catch:{ Throwable -> 0x1479 }
                        r72 = r3;	 Catch:{ Throwable -> 0x1479 }
                        r3 = r56;	 Catch:{ Throwable -> 0x1479 }
                        r6 = r67;	 Catch:{ Throwable -> 0x1479 }
                        r11 = r70;	 Catch:{ Throwable -> 0x1479 }
                        goto L_0x146e;	 Catch:{ Throwable -> 0x1479 }
                    L_0x13ed:
                        r72 = r3;	 Catch:{ Throwable -> 0x1479 }
                        r67 = r4;	 Catch:{ Throwable -> 0x1479 }
                        r70 = r5;	 Catch:{ Throwable -> 0x1479 }
                        r64 = r8;	 Catch:{ Throwable -> 0x1479 }
                        r58 = r11;	 Catch:{ Throwable -> 0x1479 }
                        r59 = r12;	 Catch:{ Throwable -> 0x1479 }
                        r60 = r13;	 Catch:{ Throwable -> 0x1479 }
                        r62 = r14;	 Catch:{ Throwable -> 0x1479 }
                        r66 = r15;	 Catch:{ Throwable -> 0x1479 }
                        r3 = r56;	 Catch:{ Throwable -> 0x1479 }
                        r4 = "max_id";	 Catch:{ Throwable -> 0x1479 }
                        r4 = r10.getInt(r4);	 Catch:{ Throwable -> 0x1479 }
                        r5 = new java.util.ArrayList;	 Catch:{ Throwable -> 0x1479 }
                        r5.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Throwable -> 0x1479 }
                        if (r6 == 0) goto L_0x142f;	 Catch:{ Throwable -> 0x1479 }
                        r6 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x1479 }
                        r6.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r8 = "GCM received read notification max_id = ";	 Catch:{ Throwable -> 0x1479 }
                        r6.append(r8);	 Catch:{ Throwable -> 0x1479 }
                        r6.append(r4);	 Catch:{ Throwable -> 0x1479 }
                        r8 = " for dialogId = ";	 Catch:{ Throwable -> 0x1479 }
                        r6.append(r8);	 Catch:{ Throwable -> 0x1479 }
                        r11 = r70;	 Catch:{ Throwable -> 0x1479 }
                        r6.append(r11);	 Catch:{ Throwable -> 0x1479 }
                        r6 = r6.toString();	 Catch:{ Throwable -> 0x1479 }
                        org.telegram.messenger.FileLog.d(r6);	 Catch:{ Throwable -> 0x1479 }
                        goto L_0x1431;	 Catch:{ Throwable -> 0x1479 }
                        r11 = r70;	 Catch:{ Throwable -> 0x1479 }
                        if (r7 == 0) goto L_0x1440;	 Catch:{ Throwable -> 0x1479 }
                        r6 = new org.telegram.tgnet.TLRPC$TL_updateReadChannelInbox;	 Catch:{ Throwable -> 0x1479 }
                        r6.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r6.channel_id = r7;	 Catch:{ Throwable -> 0x1479 }
                        r6.max_id = r4;	 Catch:{ Throwable -> 0x1479 }
                        r5.add(r6);	 Catch:{ Throwable -> 0x1479 }
                        goto L_0x1463;	 Catch:{ Throwable -> 0x1479 }
                        r6 = new org.telegram.tgnet.TLRPC$TL_updateReadHistoryInbox;	 Catch:{ Throwable -> 0x1479 }
                        r6.<init>();	 Catch:{ Throwable -> 0x1479 }
                        if (r9 == 0) goto L_0x1453;	 Catch:{ Throwable -> 0x1479 }
                        r8 = new org.telegram.tgnet.TLRPC$TL_peerUser;	 Catch:{ Throwable -> 0x1479 }
                        r8.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r6.peer = r8;	 Catch:{ Throwable -> 0x1479 }
                        r8 = r6.peer;	 Catch:{ Throwable -> 0x1479 }
                        r8.user_id = r9;	 Catch:{ Throwable -> 0x1479 }
                        goto L_0x145e;	 Catch:{ Throwable -> 0x1479 }
                        r8 = new org.telegram.tgnet.TLRPC$TL_peerChat;	 Catch:{ Throwable -> 0x1479 }
                        r8.<init>();	 Catch:{ Throwable -> 0x1479 }
                        r6.peer = r8;	 Catch:{ Throwable -> 0x1479 }
                        r8 = r6.peer;	 Catch:{ Throwable -> 0x1479 }
                        r8.chat_id = r3;	 Catch:{ Throwable -> 0x1479 }
                        r6.max_id = r4;	 Catch:{ Throwable -> 0x1479 }
                        r5.add(r6);	 Catch:{ Throwable -> 0x1479 }
                        r6 = r67;	 Catch:{ Throwable -> 0x1479 }
                        r8 = org.telegram.messenger.MessagesController.getInstance(r6);	 Catch:{ Throwable -> 0x1479 }
                        r13 = 0;	 Catch:{ Throwable -> 0x1479 }
                        r14 = 0;	 Catch:{ Throwable -> 0x1479 }
                        r8.processUpdateArray(r5, r14, r14, r13);	 Catch:{ Throwable -> 0x1479 }
                        org.telegram.tgnet.ConnectionsManager.onInternalPushReceived(r2);	 Catch:{ Throwable -> 0x1479 }
                        r4 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);	 Catch:{ Throwable -> 0x1479 }
                        r4.resumeNetworkMaybe();	 Catch:{ Throwable -> 0x1479 }
                        goto L_0x14cf;
                    L_0x1479:
                        r0 = move-exception;
                        r3 = r2;
                        r4 = r72;
                        goto L_0x001d;
                    L_0x147f:
                        r0 = move-exception;
                        r72 = r3;
                        r3 = r2;
                        r4 = r72;
                        r2 = r0;
                        goto L_0x149c;
                    L_0x1487:
                        r0 = move-exception;
                        r3 = r2;
                        r4 = r35;
                        goto L_0x001d;
                    L_0x148d:
                        r0 = move-exception;
                        r35 = r4;
                        r2 = r0;
                        r3 = r31;
                        goto L_0x149c;
                    L_0x1494:
                        r0 = move-exception;
                        r31 = r2;
                        r35 = r4;
                        r2 = r0;
                        r3 = r31;
                    L_0x149c:
                        r5 = -1;
                        if (r3 == r5) goto L_0x14aa;
                        org.telegram.tgnet.ConnectionsManager.onInternalPushReceived(r3);
                        r5 = org.telegram.tgnet.ConnectionsManager.getInstance(r3);
                        r5.resumeNetworkMaybe();
                        goto L_0x14b1;
                        r5 = org.telegram.messenger.GcmPushListenerService.AnonymousClass1.this;
                        r5 = org.telegram.messenger.GcmPushListenerService.this;
                        r5.onDecryptError();
                        r5 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
                        if (r5 == 0) goto L_0x14c9;
                        r5 = new java.lang.StringBuilder;
                        r5.<init>();
                        r6 = "error in loc_key = ";
                        r5.append(r6);
                        r5.append(r4);
                        r5 = r5.toString();
                        org.telegram.messenger.FileLog.e(r5);
                        org.telegram.messenger.FileLog.e(r2);
                        r2 = r3;
                        r72 = r4;
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.GcmPushListenerService.1.1.run():void");
                    }
                });
            }
        });
    }

    private void onDecryptError() {
        for (int a = 0; a < 3; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                ConnectionsManager.onInternalPushReceived(a);
                ConnectionsManager.getInstance(a).resumeNetworkMaybe();
            }
        }
    }
}
