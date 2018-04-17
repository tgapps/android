package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompat.CarExtender;
import android.support.v4.app.NotificationCompat.CarExtender.UnreadConversation;
import android.support.v4.app.NotificationCompat.MessagingStyle;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.DefaultLoadControl;
import org.telegram.messenger.exoplayer2.upstream.DataSchemeDataSource;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.TL_account_updateNotifySettings;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputNotifyPeer;
import org.telegram.tgnet.TLRPC.TL_inputPeerNotifySettings;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC.TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC.TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.LaunchActivity;
import org.telegram.ui.PopupNotificationActivity;

public class NotificationsController {
    public static final String EXTRA_VOICE_REPLY = "extra_voice_reply";
    private static volatile NotificationsController[] Instance = new NotificationsController[3];
    public static final String OTHER_NOTIFICATIONS_CHANNEL = "Other3";
    protected static AudioManager audioManager = ((AudioManager) ApplicationLoader.applicationContext.getSystemService(MimeTypes.BASE_TYPE_AUDIO));
    public static long lastNoDataNotificationTime;
    private static NotificationManagerCompat notificationManager;
    private static DispatchQueue notificationsQueue = new DispatchQueue("notificationsQueue");
    private static NotificationManager systemNotificationManager;
    private AlarmManager alarmManager;
    private int currentAccount;
    private ArrayList<MessageObject> delayedPushMessages = new ArrayList();
    private boolean inChatSoundEnabled = true;
    private int lastBadgeCount = -1;
    private int lastButtonId = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS;
    private boolean lastNotificationIsNoData;
    private int lastOnlineFromOtherDevice = 0;
    private long lastSoundOutPlay;
    private long lastSoundPlay;
    private LongSparseArray<Integer> lastWearNotifiedMessageId = new LongSparseArray();
    private String launcherClassName;
    private Runnable notificationDelayRunnable;
    private WakeLock notificationDelayWakelock;
    private String notificationGroup;
    private int notificationId;
    private boolean notifyCheck = false;
    private long opened_dialog_id = 0;
    private int personal_count = 0;
    public ArrayList<MessageObject> popupMessages = new ArrayList();
    public ArrayList<MessageObject> popupReplyMessages = new ArrayList();
    private LongSparseArray<Integer> pushDialogs = new LongSparseArray();
    private LongSparseArray<Integer> pushDialogsOverrideMention = new LongSparseArray();
    private ArrayList<MessageObject> pushMessages = new ArrayList();
    private LongSparseArray<MessageObject> pushMessagesDict = new LongSparseArray();
    public boolean showBadgeNumber;
    private LongSparseArray<Point> smartNotificationsDialogs = new LongSparseArray();
    private int soundIn;
    private boolean soundInLoaded;
    private int soundOut;
    private boolean soundOutLoaded;
    private SoundPool soundPool;
    private int soundRecord;
    private boolean soundRecordLoaded;
    private int total_unread_count = 0;
    private LongSparseArray<Integer> wearNotificationsIds = new LongSparseArray();

    class AnonymousClass1NotificationHolder {
        int id;
        Notification notification;

        AnonymousClass1NotificationHolder(int i, Notification n) {
            this.id = i;
            this.notification = n;
        }

        void call() {
            NotificationsController.notificationManager.notify(this.id, this.notification);
        }
    }

    private java.lang.String getStringForMessage(org.telegram.messenger.MessageObject r1, boolean r2, boolean[] r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.NotificationsController.getStringForMessage(org.telegram.messenger.MessageObject, boolean, boolean[]):java.lang.String
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r19;
        r1 = r20;
        r2 = 0;
        r3 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r2);
        r4 = 2131494657; // 0x7f0c0701 float:1.8612829E38 double:1.0530982843E-314;
        if (r3 != 0) goto L_0x12cf;
    L_0x000e:
        r3 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;
        if (r3 == 0) goto L_0x0014;
    L_0x0012:
        goto L_0x12cf;
    L_0x0014:
        r3 = r1.messageOwner;
        r5 = r3.dialog_id;
        r3 = r1.messageOwner;
        r3 = r3.to_id;
        r3 = r3.chat_id;
        if (r3 == 0) goto L_0x0027;
    L_0x0020:
        r3 = r1.messageOwner;
        r3 = r3.to_id;
        r3 = r3.chat_id;
        goto L_0x002d;
    L_0x0027:
        r3 = r1.messageOwner;
        r3 = r3.to_id;
        r3 = r3.channel_id;
    L_0x002d:
        r7 = r1.messageOwner;
        r7 = r7.to_id;
        r7 = r7.user_id;
        r8 = r20.isFcmMessage();
        r9 = 2131493982; // 0x7f0c045e float:1.861146E38 double:1.053097951E-314;
        r10 = 2131493187; // 0x7f0c0143 float:1.8609847E38 double:1.053097558E-314;
        r11 = 2131493993; // 0x7f0c0469 float:1.8611482E38 double:1.0530979563E-314;
        r12 = 2;
        r13 = 1;
        if (r8 == 0) goto L_0x00a7;
    L_0x0044:
        if (r3 != 0) goto L_0x0064;
    L_0x0046:
        if (r7 == 0) goto L_0x0064;
    L_0x0048:
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getNotificationsSettings(r4);
        r8 = "EnablePreviewAll";
        r8 = r4.getBoolean(r8, r13);
        if (r8 != 0) goto L_0x0063;
    L_0x0056:
        r8 = "NotificationMessageNoText";
        r9 = new java.lang.Object[r13];
        r10 = r1.localName;
        r9[r2] = r10;
        r2 = org.telegram.messenger.LocaleController.formatString(r8, r11, r9);
        return r2;
    L_0x0063:
        goto L_0x00a0;
    L_0x0064:
        if (r3 == 0) goto L_0x00a0;
    L_0x0066:
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getNotificationsSettings(r4);
        r8 = "EnablePreviewGroup";
        r8 = r4.getBoolean(r8, r13);
        if (r8 != 0) goto L_0x00a0;
    L_0x0074:
        r8 = r20.isMegagroup();
        if (r8 != 0) goto L_0x008f;
    L_0x007a:
        r8 = r1.messageOwner;
        r8 = r8.to_id;
        r8 = r8.channel_id;
        if (r8 == 0) goto L_0x008f;
    L_0x0082:
        r8 = "ChannelMessageNoText";
        r9 = new java.lang.Object[r13];
        r11 = r1.localName;
        r9[r2] = r11;
        r2 = org.telegram.messenger.LocaleController.formatString(r8, r10, r9);
        return r2;
    L_0x008f:
        r8 = "NotificationMessageGroupNoText";
        r10 = new java.lang.Object[r12];
        r11 = r1.localUserName;
        r10[r2] = r11;
        r2 = r1.localName;
        r10[r13] = r2;
        r2 = org.telegram.messenger.LocaleController.formatString(r8, r9, r10);
        return r2;
    L_0x00a0:
        r22[r2] = r13;
        r2 = r1.messageText;
        r2 = (java.lang.String) r2;
        return r2;
    L_0x00a7:
        if (r7 != 0) goto L_0x00bd;
    L_0x00a9:
        r8 = r20.isFromUser();
        if (r8 != 0) goto L_0x00b8;
    L_0x00af:
        r8 = r20.getId();
        if (r8 >= 0) goto L_0x00b6;
    L_0x00b5:
        goto L_0x00b8;
    L_0x00b6:
        r7 = -r3;
        goto L_0x00cd;
    L_0x00b8:
        r8 = r1.messageOwner;
        r7 = r8.from_id;
        goto L_0x00cd;
    L_0x00bd:
        r8 = r0.currentAccount;
        r8 = org.telegram.messenger.UserConfig.getInstance(r8);
        r8 = r8.getClientUserId();
        if (r7 != r8) goto L_0x00cd;
    L_0x00c9:
        r8 = r1.messageOwner;
        r7 = r8.from_id;
    L_0x00cd:
        r14 = 0;
        r8 = (r5 > r14 ? 1 : (r5 == r14 ? 0 : -1));
        if (r8 != 0) goto L_0x00db;
    L_0x00d3:
        if (r3 == 0) goto L_0x00d8;
    L_0x00d5:
        r8 = -r3;
        r5 = (long) r8;
        goto L_0x00db;
    L_0x00d8:
        if (r7 == 0) goto L_0x00db;
    L_0x00da:
        r5 = (long) r7;
    L_0x00db:
        r8 = 0;
        if (r7 <= 0) goto L_0x00f3;
    L_0x00de:
        r9 = r0.currentAccount;
        r9 = org.telegram.messenger.MessagesController.getInstance(r9);
        r10 = java.lang.Integer.valueOf(r7);
        r9 = r9.getUser(r10);
        if (r9 == 0) goto L_0x00f2;
    L_0x00ee:
        r8 = org.telegram.messenger.UserObject.getUserName(r9);
    L_0x00f2:
        goto L_0x0106;
    L_0x00f3:
        r9 = r0.currentAccount;
        r9 = org.telegram.messenger.MessagesController.getInstance(r9);
        r10 = -r7;
        r10 = java.lang.Integer.valueOf(r10);
        r9 = r9.getChat(r10);
        if (r9 == 0) goto L_0x0106;
    L_0x0104:
        r8 = r9.title;
    L_0x0106:
        r9 = 0;
        if (r8 != 0) goto L_0x010a;
    L_0x0109:
        return r9;
    L_0x010a:
        r10 = 0;
        if (r3 == 0) goto L_0x011e;
    L_0x010d:
        r11 = r0.currentAccount;
        r11 = org.telegram.messenger.MessagesController.getInstance(r11);
        r12 = java.lang.Integer.valueOf(r3);
        r10 = r11.getChat(r12);
        if (r10 != 0) goto L_0x011e;
    L_0x011d:
        return r9;
    L_0x011e:
        r11 = 0;
        r12 = (int) r5;
        if (r12 != 0) goto L_0x012c;
    L_0x0122:
        r2 = "YouHaveNewMessage";
        r11 = org.telegram.messenger.LocaleController.getString(r2, r4);
        r16 = r5;
        goto L_0x12ce;
    L_0x012c:
        if (r3 != 0) goto L_0x04f4;
    L_0x012e:
        if (r7 == 0) goto L_0x04f4;
    L_0x0130:
        r9 = r0.currentAccount;
        r9 = org.telegram.messenger.MessagesController.getNotificationsSettings(r9);
        r4 = "EnablePreviewAll";
        r4 = r9.getBoolean(r4, r13);
        if (r4 == 0) goto L_0x04e2;
    L_0x013e:
        r4 = r1.messageOwner;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageService;
        if (r4 == 0) goto L_0x0221;
    L_0x0144:
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionUserJoined;
        if (r4 == 0) goto L_0x015d;
    L_0x014c:
        r4 = "NotificationContactJoined";
        r12 = 2131493952; // 0x7f0c0440 float:1.8611399E38 double:1.053097936E-314;
        r13 = new java.lang.Object[r13];
        r13[r2] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r12, r13);
    L_0x0159:
        r16 = r5;
        goto L_0x04f2;
    L_0x015d:
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionUserUpdatedPhoto;
        if (r4 == 0) goto L_0x0173;
    L_0x0165:
        r4 = "NotificationContactNewPhoto";
        r12 = 2131493953; // 0x7f0c0441 float:1.86114E38 double:1.0530979365E-314;
        r13 = new java.lang.Object[r13];
        r13[r2] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r12, r13);
        goto L_0x0159;
    L_0x0173:
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionLoginUnknownLocation;
        if (r4 == 0) goto L_0x01e3;
    L_0x017b:
        r4 = "formatDateAtTime";
        r12 = 2;
        r13 = new java.lang.Object[r12];
        r12 = org.telegram.messenger.LocaleController.getInstance();
        r12 = r12.formatterYear;
        r2 = r1.messageOwner;
        r2 = r2.date;
        r16 = r5;
        r5 = (long) r2;
        r14 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r5 = r5 * r14;
        r2 = r12.format(r5);
        r5 = 0;
        r13[r5] = r2;
        r2 = org.telegram.messenger.LocaleController.getInstance();
        r2 = r2.formatterDay;
        r5 = r1.messageOwner;
        r5 = r5.date;
        r5 = (long) r5;
        r5 = r5 * r14;
        r2 = r2.format(r5);
        r5 = 1;
        r13[r5] = r2;
        r2 = 2131494696; // 0x7f0c0728 float:1.8612908E38 double:1.0530983036E-314;
        r2 = org.telegram.messenger.LocaleController.formatString(r4, r2, r13);
        r4 = "NotificationUnrecognizedDevice";
        r5 = 2131494003; // 0x7f0c0473 float:1.8611502E38 double:1.053097961E-314;
        r6 = 4;
        r6 = new java.lang.Object[r6];
        r12 = r0.currentAccount;
        r12 = org.telegram.messenger.UserConfig.getInstance(r12);
        r12 = r12.getCurrentUser();
        r12 = r12.first_name;
        r13 = 0;
        r6[r13] = r12;
        r12 = 1;
        r6[r12] = r2;
        r12 = r1.messageOwner;
        r12 = r12.action;
        r12 = r12.title;
        r13 = 2;
        r6[r13] = r12;
        r12 = r1.messageOwner;
        r12 = r12.action;
        r12 = r12.address;
        r13 = 3;
        r6[r13] = r12;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x04f2;
    L_0x01e3:
        r16 = r5;
        r2 = r1.messageOwner;
        r2 = r2.action;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGameScore;
        if (r2 != 0) goto L_0x0219;
    L_0x01ed:
        r2 = r1.messageOwner;
        r2 = r2.action;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPaymentSent;
        if (r2 == 0) goto L_0x01f6;
    L_0x01f5:
        goto L_0x0219;
    L_0x01f6:
        r2 = r1.messageOwner;
        r2 = r2.action;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall;
        if (r2 == 0) goto L_0x04f2;
    L_0x01fe:
        r2 = r1.messageOwner;
        r2 = r2.action;
        r2 = r2.reason;
        r4 = r20.isOut();
        if (r4 != 0) goto L_0x0217;
    L_0x020a:
        r4 = r2 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
        if (r4 == 0) goto L_0x0217;
    L_0x020e:
        r4 = "CallMessageIncomingMissed";
        r5 = 2131493111; // 0x7f0c00f7 float:1.8609693E38 double:1.0530975205E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r4, r5);
    L_0x0217:
        goto L_0x04f2;
    L_0x0219:
        r2 = r1.messageText;
        r11 = r2.toString();
        goto L_0x04f2;
    L_0x0221:
        r16 = r5;
        r2 = r20.isMediaEmpty();
        if (r2 == 0) goto L_0x0271;
    L_0x0229:
        if (r21 != 0) goto L_0x0260;
    L_0x022b:
        r2 = r1.messageOwner;
        r2 = r2.message;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 != 0) goto L_0x024f;
    L_0x0235:
        r2 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r6 = r1.messageOwner;
        r6 = r6.message;
        r12 = 1;
        r4[r12] = r6;
        r6 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r6, r4);
        r22[r5] = r12;
        goto L_0x04f2;
    L_0x024f:
        r5 = 0;
        r12 = 1;
        r2 = "NotificationMessageNoText";
        r4 = new java.lang.Object[r12];
        r4[r5] = r8;
        r6 = 2131493993; // 0x7f0c0469 float:1.8611482E38 double:1.0530979563E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r6, r4);
        goto L_0x04f2;
    L_0x0260:
        r5 = 0;
        r6 = 2131493993; // 0x7f0c0469 float:1.8611482E38 double:1.0530979563E-314;
        r12 = 1;
        r2 = "NotificationMessageNoText";
        r4 = new java.lang.Object[r12];
        r4[r5] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r6, r4);
        goto L_0x04f2;
    L_0x0271:
        r2 = r1.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r2 == 0) goto L_0x02e1;
    L_0x0279:
        if (r21 != 0) goto L_0x02b7;
    L_0x027b:
        r2 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r2 < r4) goto L_0x02b7;
    L_0x0281:
        r2 = r1.messageOwner;
        r2 = r2.message;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 != 0) goto L_0x02b7;
    L_0x028b:
        r2 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "🖼 ";
        r5.append(r6);
        r6 = r1.messageOwner;
        r6 = r6.message;
        r5.append(r6);
        r5 = r5.toString();
        r6 = 1;
        r4[r6] = r5;
        r5 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r5, r4);
        r2 = 0;
        r22[r2] = r6;
        goto L_0x04f2;
    L_0x02b7:
        r2 = r1.messageOwner;
        r2 = r2.media;
        r2 = r2.ttl_seconds;
        if (r2 == 0) goto L_0x02d0;
    L_0x02bf:
        r2 = "NotificationMessageSDPhoto";
        r4 = 2131493996; // 0x7f0c046c float:1.8611488E38 double:1.0530979577E-314;
        r5 = 1;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x02d0:
        r5 = 1;
        r6 = 0;
        r2 = "NotificationMessagePhoto";
        r4 = 2131493994; // 0x7f0c046a float:1.8611484E38 double:1.0530979568E-314;
        r5 = new java.lang.Object[r5];
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x02e1:
        r2 = r20.isVideo();
        if (r2 == 0) goto L_0x034f;
    L_0x02e7:
        if (r21 != 0) goto L_0x0325;
    L_0x02e9:
        r2 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r2 < r4) goto L_0x0325;
    L_0x02ef:
        r2 = r1.messageOwner;
        r2 = r2.message;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 != 0) goto L_0x0325;
    L_0x02f9:
        r2 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "📹 ";
        r5.append(r6);
        r6 = r1.messageOwner;
        r6 = r6.message;
        r5.append(r6);
        r5 = r5.toString();
        r6 = 1;
        r4[r6] = r5;
        r5 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r5, r4);
        r2 = 0;
        r22[r2] = r6;
        goto L_0x04f2;
    L_0x0325:
        r2 = r1.messageOwner;
        r2 = r2.media;
        r2 = r2.ttl_seconds;
        if (r2 == 0) goto L_0x033e;
    L_0x032d:
        r2 = "NotificationMessageSDVideo";
        r4 = 2131493997; // 0x7f0c046d float:1.861149E38 double:1.053097958E-314;
        r5 = 1;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x033e:
        r5 = 1;
        r6 = 0;
        r2 = "NotificationMessageVideo";
        r4 = 2131494001; // 0x7f0c0471 float:1.8611498E38 double:1.05309796E-314;
        r5 = new java.lang.Object[r5];
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x034f:
        r6 = 0;
        r2 = r20.isGame();
        if (r2 == 0) goto L_0x0371;
    L_0x0356:
        r2 = "NotificationMessageGame";
        r4 = 2131493971; // 0x7f0c0453 float:1.8611437E38 double:1.0530979454E-314;
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r5[r6] = r8;
        r6 = r1.messageOwner;
        r6 = r6.media;
        r6 = r6.game;
        r6 = r6.title;
        r12 = 1;
        r5[r12] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x0371:
        r12 = 1;
        r2 = r20.isVoice();
        if (r2 == 0) goto L_0x0388;
    L_0x0378:
        r2 = "NotificationMessageAudio";
        r4 = 2131493966; // 0x7f0c044e float:1.8611427E38 double:1.053097943E-314;
        r5 = new java.lang.Object[r12];
        r6 = 0;
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x0388:
        r6 = 0;
        r2 = r20.isRoundVideo();
        if (r2 == 0) goto L_0x039e;
    L_0x038f:
        r2 = "NotificationMessageRound";
        r4 = 2131493995; // 0x7f0c046b float:1.8611486E38 double:1.053097957E-314;
        r5 = new java.lang.Object[r12];
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x039e:
        r2 = r20.isMusic();
        if (r2 == 0) goto L_0x03b3;
    L_0x03a4:
        r2 = "NotificationMessageMusic";
        r4 = 2131493992; // 0x7f0c0468 float:1.861148E38 double:1.053097956E-314;
        r5 = new java.lang.Object[r12];
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x03b3:
        r2 = r1.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact;
        if (r2 == 0) goto L_0x03cc;
    L_0x03bb:
        r2 = "NotificationMessageContact";
        r4 = 2131493967; // 0x7f0c044f float:1.861143E38 double:1.0530979434E-314;
        r5 = 1;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x03cc:
        r2 = r1.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
        if (r2 != 0) goto L_0x04d2;
    L_0x03d4:
        r2 = r1.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
        if (r2 == 0) goto L_0x03de;
    L_0x03dc:
        goto L_0x04d2;
    L_0x03de:
        r2 = r1.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r2 == 0) goto L_0x03f7;
    L_0x03e6:
        r2 = "NotificationMessageLiveLocation";
        r4 = 2131493990; // 0x7f0c0466 float:1.8611476E38 double:1.053097955E-314;
        r5 = 1;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r4, r5);
        goto L_0x04f2;
    L_0x03f7:
        r2 = r1.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r2 == 0) goto L_0x04f2;
    L_0x03ff:
        r2 = r20.isSticker();
        if (r2 == 0) goto L_0x0431;
    L_0x0405:
        r2 = r20.getStickerEmoji();
        if (r2 == 0) goto L_0x041f;
    L_0x040b:
        r4 = "NotificationMessageStickerEmoji";
        r5 = 2131493999; // 0x7f0c046f float:1.8611494E38 double:1.053097959E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r12 = 0;
        r6[r12] = r8;
        r13 = 1;
        r6[r13] = r2;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        r11 = r4;
        goto L_0x042f;
    L_0x041f:
        r12 = 0;
        r13 = 1;
        r4 = "NotificationMessageSticker";
        r5 = 2131493998; // 0x7f0c046e float:1.8611492E38 double:1.0530979587E-314;
        r6 = new java.lang.Object[r13];
        r6[r12] = r8;
        r2 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        r11 = r2;
    L_0x042f:
        goto L_0x04f2;
    L_0x0431:
        r2 = r20.isGif();
        if (r2 == 0) goto L_0x0485;
    L_0x0437:
        if (r21 != 0) goto L_0x0475;
    L_0x0439:
        r2 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r2 < r4) goto L_0x0475;
    L_0x043f:
        r2 = r1.messageOwner;
        r2 = r2.message;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 != 0) goto L_0x0475;
    L_0x0449:
        r2 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "🎬 ";
        r5.append(r6);
        r6 = r1.messageOwner;
        r6 = r6.message;
        r5.append(r6);
        r5 = r5.toString();
        r6 = 1;
        r4[r6] = r5;
        r5 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r5, r4);
        r2 = 0;
        r22[r2] = r6;
        goto L_0x04f2;
    L_0x0475:
        r2 = 0;
        r6 = 1;
        r4 = "NotificationMessageGif";
        r5 = 2131493972; // 0x7f0c0454 float:1.861144E38 double:1.053097946E-314;
        r6 = new java.lang.Object[r6];
        r6[r2] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x04f2;
    L_0x0485:
        if (r21 != 0) goto L_0x04c2;
    L_0x0487:
        r2 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r2 < r4) goto L_0x04c2;
    L_0x048d:
        r2 = r1.messageOwner;
        r2 = r2.message;
        r2 = android.text.TextUtils.isEmpty(r2);
        if (r2 != 0) goto L_0x04c2;
    L_0x0497:
        r2 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "📎 ";
        r5.append(r6);
        r6 = r1.messageOwner;
        r6 = r6.message;
        r5.append(r6);
        r5 = r5.toString();
        r6 = 1;
        r4[r6] = r5;
        r5 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r2, r5, r4);
        r2 = 0;
        r22[r2] = r6;
        goto L_0x04f2;
    L_0x04c2:
        r2 = 0;
        r6 = 1;
        r4 = "NotificationMessageDocument";
        r5 = 2131493968; // 0x7f0c0450 float:1.8611431E38 double:1.053097944E-314;
        r6 = new java.lang.Object[r6];
        r6[r2] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x04f2;
    L_0x04d2:
        r2 = 0;
        r6 = 1;
        r4 = "NotificationMessageMap";
        r5 = 2131493991; // 0x7f0c0467 float:1.8611478E38 double:1.0530979553E-314;
        r6 = new java.lang.Object[r6];
        r6[r2] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x04f2;
    L_0x04e2:
        r16 = r5;
        r6 = r13;
        r4 = "NotificationMessageNoText";
        r5 = new java.lang.Object[r6];
        r5[r2] = r8;
        r2 = 2131493993; // 0x7f0c0469 float:1.8611482E38 double:1.0530979563E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r2, r5);
    L_0x04f2:
        goto L_0x12ce;
    L_0x04f4:
        r16 = r5;
        if (r3 == 0) goto L_0x12ce;
    L_0x04f8:
        r2 = r0.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getNotificationsSettings(r2);
        r4 = "EnablePreviewGroup";
        r5 = 1;
        r4 = r2.getBoolean(r4, r5);
        if (r4 == 0) goto L_0x12a1;
    L_0x0507:
        r4 = r1.messageOwner;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageService;
        if (r4 == 0) goto L_0x0d67;
    L_0x050d:
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser;
        if (r4 == 0) goto L_0x0642;
    L_0x0515:
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4.user_id;
        if (r4 != 0) goto L_0x053b;
    L_0x051d:
        r5 = r1.messageOwner;
        r5 = r5.action;
        r5 = r5.users;
        r5 = r5.size();
        r6 = 1;
        if (r5 != r6) goto L_0x053b;
    L_0x052a:
        r5 = r1.messageOwner;
        r5 = r5.action;
        r5 = r5.users;
        r6 = 0;
        r5 = r5.get(r6);
        r5 = (java.lang.Integer) r5;
        r4 = r5.intValue();
    L_0x053b:
        if (r4 == 0) goto L_0x05e3;
    L_0x053d:
        r5 = r1.messageOwner;
        r5 = r5.to_id;
        r5 = r5.channel_id;
        if (r5 == 0) goto L_0x0560;
    L_0x0545:
        r5 = r10.megagroup;
        if (r5 != 0) goto L_0x0560;
    L_0x0549:
        r5 = "ChannelAddedByNotification";
        r6 = 2131493148; // 0x7f0c011c float:1.8609768E38 double:1.053097539E-314;
        r9 = 2;
        r9 = new java.lang.Object[r9];
        r12 = 0;
        r9[r12] = r8;
        r12 = r10.title;
        r13 = 1;
        r9[r13] = r12;
        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r9);
    L_0x055d:
        r11 = r5;
        goto L_0x0640;
    L_0x0560:
        r5 = r0.currentAccount;
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);
        r5 = r5.getClientUserId();
        if (r4 != r5) goto L_0x0581;
    L_0x056c:
        r5 = "NotificationInvitedToGroup";
        r6 = 2131493964; // 0x7f0c044c float:1.8611423E38 double:1.053097942E-314;
        r9 = 2;
        r9 = new java.lang.Object[r9];
        r12 = 0;
        r9[r12] = r8;
        r12 = r10.title;
        r13 = 1;
        r9[r13] = r12;
        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r9);
        goto L_0x055d;
    L_0x0581:
        r5 = r0.currentAccount;
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);
        r6 = java.lang.Integer.valueOf(r4);
        r5 = r5.getUser(r6);
        if (r5 != 0) goto L_0x0593;
    L_0x0591:
        r6 = 0;
        return r6;
    L_0x0593:
        r6 = r5.id;
        if (r7 != r6) goto L_0x05c5;
    L_0x0597:
        r6 = r10.megagroup;
        if (r6 == 0) goto L_0x05b0;
    L_0x059b:
        r6 = "NotificationGroupAddSelfMega";
        r9 = 2131493958; // 0x7f0c0446 float:1.861141E38 double:1.053097939E-314;
        r12 = 2;
        r12 = new java.lang.Object[r12];
        r13 = 0;
        r12[r13] = r8;
        r13 = r10.title;
        r14 = 1;
        r12[r14] = r13;
        r6 = org.telegram.messenger.LocaleController.formatString(r6, r9, r12);
        goto L_0x05e0;
    L_0x05b0:
        r12 = 2;
        r13 = 0;
        r14 = 1;
        r6 = "NotificationGroupAddSelf";
        r9 = 2131493957; // 0x7f0c0445 float:1.8611409E38 double:1.0530979385E-314;
        r12 = new java.lang.Object[r12];
        r12[r13] = r8;
        r13 = r10.title;
        r12[r14] = r13;
        r6 = org.telegram.messenger.LocaleController.formatString(r6, r9, r12);
        goto L_0x05af;
    L_0x05c5:
        r13 = 0;
        r14 = 1;
        r6 = "NotificationGroupAddMember";
        r9 = 2131493956; // 0x7f0c0444 float:1.8611407E38 double:1.053097938E-314;
        r12 = 3;
        r12 = new java.lang.Object[r12];
        r12[r13] = r8;
        r13 = r10.title;
        r12[r14] = r13;
        r13 = org.telegram.messenger.UserObject.getUserName(r5);
        r14 = 2;
        r12[r14] = r13;
        r6 = org.telegram.messenger.LocaleController.formatString(r6, r9, r12);
        r11 = r6;
        goto L_0x0640;
    L_0x05e3:
        r5 = new java.lang.StringBuilder;
        r6 = "";
        r5.<init>(r6);
        r6 = 0;
        r9 = r1.messageOwner;
        r9 = r9.action;
        r9 = r9.users;
        r9 = r9.size();
        if (r6 >= r9) goto L_0x0624;
        r9 = r0.currentAccount;
        r9 = org.telegram.messenger.MessagesController.getInstance(r9);
        r12 = r1.messageOwner;
        r12 = r12.action;
        r12 = r12.users;
        r12 = r12.get(r6);
        r12 = (java.lang.Integer) r12;
        r9 = r9.getUser(r12);
        if (r9 == 0) goto L_0x0621;
        r12 = org.telegram.messenger.UserObject.getUserName(r9);
        r13 = r5.length();
        if (r13 == 0) goto L_0x061e;
        r13 = ", ";
        r5.append(r13);
        r5.append(r12);
        r6 = r6 + 1;
        goto L_0x05eb;
        r6 = "NotificationGroupAddMember";
        r9 = 2131493956; // 0x7f0c0444 float:1.8611407E38 double:1.053097938E-314;
        r12 = 3;
        r12 = new java.lang.Object[r12];
        r13 = 0;
        r12[r13] = r8;
        r13 = r10.title;
        r14 = 1;
        r12[r14] = r13;
        r13 = r5.toString();
        r14 = 2;
        r12[r14] = r13;
        r4 = org.telegram.messenger.LocaleController.formatString(r6, r9, r12);
        r11 = r4;
    L_0x0640:
        goto L_0x12ce;
    L_0x0642:
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatJoinedByLink;
        if (r4 == 0) goto L_0x0660;
        r4 = "NotificationInvitedToGroupByLink";
        r5 = 2131493965; // 0x7f0c044d float:1.8611425E38 double:1.0530979424E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatEditTitle;
        if (r4 == 0) goto L_0x0682;
        r4 = "NotificationEditedGroupName";
        r5 = 2131493954; // 0x7f0c0442 float:1.8611403E38 double:1.053097937E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r1.messageOwner;
        r9 = r9.action;
        r9 = r9.title;
        r12 = 1;
        r6[r12] = r9;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatEditPhoto;
        if (r4 != 0) goto L_0x0d32;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeletePhoto;
        if (r4 == 0) goto L_0x0694;
        goto L_0x0d32;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
        if (r4 == 0) goto L_0x0717;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4.user_id;
        r5 = r0.currentAccount;
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);
        r5 = r5.getClientUserId();
        if (r4 != r5) goto L_0x06c4;
        r4 = "NotificationGroupKickYou";
        r5 = 2131493962; // 0x7f0c044a float:1.8611419E38 double:1.053097941E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4.user_id;
        if (r4 != r7) goto L_0x06e2;
        r4 = "NotificationGroupLeftMember";
        r5 = 2131493963; // 0x7f0c044b float:1.861142E38 double:1.0530979414E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r5 = r1.messageOwner;
        r5 = r5.action;
        r5 = r5.user_id;
        r5 = java.lang.Integer.valueOf(r5);
        r4 = r4.getUser(r5);
        if (r4 != 0) goto L_0x06fa;
        r5 = 0;
        return r5;
        r5 = "NotificationGroupKickMember";
        r6 = 2131493961; // 0x7f0c0449 float:1.8611417E38 double:1.0530979404E-314;
        r9 = 3;
        r9 = new java.lang.Object[r9];
        r12 = 0;
        r9[r12] = r8;
        r12 = r10.title;
        r13 = 1;
        r9[r13] = r12;
        r12 = org.telegram.messenger.UserObject.getUserName(r4);
        r13 = 2;
        r9[r13] = r12;
        r11 = org.telegram.messenger.LocaleController.formatString(r5, r6, r9);
        goto L_0x12ce;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatCreate;
        if (r4 == 0) goto L_0x0727;
        r4 = r1.messageText;
        r11 = r4.toString();
        goto L_0x12ce;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelCreate;
        if (r4 == 0) goto L_0x0737;
        r4 = r1.messageText;
        r11 = r4.toString();
        goto L_0x12ce;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo;
        if (r4 == 0) goto L_0x0752;
        r4 = "ActionMigrateFromGroupNotify";
        r5 = 2131492891; // 0x7f0c001b float:1.8609247E38 double:1.053097412E-314;
        r6 = 1;
        r6 = new java.lang.Object[r6];
        r9 = r10.title;
        r12 = 0;
        r6[r12] = r9;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelMigrateFrom;
        if (r4 == 0) goto L_0x0771;
        r4 = "ActionMigrateFromGroupNotify";
        r5 = 2131492891; // 0x7f0c001b float:1.8609247E38 double:1.053097412E-314;
        r6 = 1;
        r6 = new java.lang.Object[r6];
        r9 = r1.messageOwner;
        r9 = r9.action;
        r9 = r9.title;
        r12 = 0;
        r6[r12] = r9;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionScreenshotTaken;
        if (r4 == 0) goto L_0x0781;
        r4 = r1.messageText;
        r11 = r4.toString();
        goto L_0x12ce;
        r4 = r1.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPinMessage;
        if (r4 == 0) goto L_0x0d22;
        r4 = 20;
        if (r10 == 0) goto L_0x0a77;
        r5 = r10.megagroup;
        if (r5 == 0) goto L_0x0a77;
        r5 = r1.replyMessageObject;
        if (r5 != 0) goto L_0x07ab;
        r4 = "NotificationActionPinnedNoText";
        r5 = 2131493936; // 0x7f0c0430 float:1.8611366E38 double:1.053097928E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r5 = r1.replyMessageObject;
        r6 = r5.isMusic();
        if (r6 == 0) goto L_0x07ca;
        r4 = "NotificationActionPinnedMusic";
        r6 = 2131493934; // 0x7f0c042e float:1.8611362E38 double:1.053097927E-314;
        r9 = 2;
        r9 = new java.lang.Object[r9];
        r12 = 0;
        r9[r12] = r8;
        r12 = r10.title;
        r13 = 1;
        r9[r13] = r12;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r6, r9);
        r11 = r4;
        goto L_0x0a75;
        r6 = r5.isVideo();
        r9 = 2131493946; // 0x7f0c043a float:1.8611386E38 double:1.053097933E-314;
        if (r6 == 0) goto L_0x0824;
        r4 = android.os.Build.VERSION.SDK_INT;
        r6 = 19;
        if (r4 < r6) goto L_0x080d;
        r4 = r5.messageOwner;
        r4 = r4.message;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x080d;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r6 = "📹 ";
        r4.append(r6);
        r6 = r5.messageOwner;
        r6 = r6.message;
        r4.append(r6);
        r4 = r4.toString();
        r6 = "NotificationActionPinnedText";
        r12 = 3;
        r12 = new java.lang.Object[r12];
        r13 = 0;
        r12[r13] = r8;
        r13 = 1;
        r12[r13] = r4;
        r13 = r10.title;
        r0 = 2;
        r12[r0] = r13;
        r0 = org.telegram.messenger.LocaleController.formatString(r6, r9, r12);
        goto L_0x0821;
        r0 = 2;
        r4 = "NotificationActionPinnedVideo";
        r6 = 2131493948; // 0x7f0c043c float:1.861139E38 double:1.053097934E-314;
        r0 = new java.lang.Object[r0];
        r9 = 0;
        r0[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r0[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r4, r6, r0);
        r11 = r0;
        goto L_0x0a75;
        r0 = r5.isGif();
        if (r0 == 0) goto L_0x0879;
        r0 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r0 < r4) goto L_0x0864;
        r0 = r5.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x0864;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r4 = "🎬 ";
        r0.append(r4);
        r4 = r5.messageOwner;
        r4 = r4.message;
        r0.append(r4);
        r0 = r0.toString();
        r4 = "NotificationActionPinnedText";
        r6 = 3;
        r6 = new java.lang.Object[r6];
        r12 = 0;
        r6[r12] = r8;
        r12 = 1;
        r6[r12] = r0;
        r12 = r10.title;
        r13 = 2;
        r6[r13] = r12;
        r0 = org.telegram.messenger.LocaleController.formatString(r4, r9, r6);
        goto L_0x0821;
        r13 = 2;
        r0 = "NotificationActionPinnedGif";
        r4 = 2131493930; // 0x7f0c042a float:1.8611354E38 double:1.053097925E-314;
        r6 = new java.lang.Object[r13];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        r0 = r5.isVoice();
        if (r0 == 0) goto L_0x0894;
        r0 = "NotificationActionPinnedVoice";
        r4 = 2131493950; // 0x7f0c043e float:1.8611395E38 double:1.053097935E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        r0 = r5.isRoundVideo();
        if (r0 == 0) goto L_0x08b0;
        r0 = "NotificationActionPinnedRound";
        r4 = 2131493940; // 0x7f0c0434 float:1.8611374E38 double:1.05309793E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        r0 = r5.isSticker();
        if (r0 == 0) goto L_0x08ea;
        r0 = r5.getStickerEmoji();
        if (r0 == 0) goto L_0x08d4;
        r4 = "NotificationActionPinnedStickerEmoji";
        r6 = 2131493944; // 0x7f0c0438 float:1.8611382E38 double:1.053097932E-314;
        r9 = 3;
        r9 = new java.lang.Object[r9];
        r12 = 0;
        r9[r12] = r8;
        r12 = r10.title;
        r13 = 1;
        r9[r13] = r12;
        r12 = 2;
        r9[r12] = r0;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r6, r9);
        goto L_0x08e8;
        r4 = 2;
        r12 = 0;
        r13 = 1;
        r6 = "NotificationActionPinnedSticker";
        r9 = 2131493942; // 0x7f0c0436 float:1.8611378E38 double:1.053097931E-314;
        r4 = new java.lang.Object[r4];
        r4[r12] = r8;
        r12 = r10.title;
        r4[r13] = r12;
        r4 = org.telegram.messenger.LocaleController.formatString(r6, r9, r4);
        goto L_0x07c7;
        r0 = r5.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r0 == 0) goto L_0x0943;
        r0 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r0 < r4) goto L_0x092d;
        r0 = r5.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x092d;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r4 = "📎 ";
        r0.append(r4);
        r4 = r5.messageOwner;
        r4 = r4.message;
        r0.append(r4);
        r0 = r0.toString();
        r4 = "NotificationActionPinnedText";
        r6 = 3;
        r6 = new java.lang.Object[r6];
        r12 = 0;
        r6[r12] = r8;
        r12 = 1;
        r6[r12] = r0;
        r12 = r10.title;
        r13 = 2;
        r6[r13] = r12;
        r0 = org.telegram.messenger.LocaleController.formatString(r4, r9, r6);
        goto L_0x0821;
        r13 = 2;
        r0 = "NotificationActionPinnedFile";
        r4 = 2131493922; // 0x7f0c0422 float:1.8611338E38 double:1.053097921E-314;
        r6 = new java.lang.Object[r13];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        r0 = r5.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
        if (r0 != 0) goto L_0x0a5f;
        r0 = r5.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
        if (r0 == 0) goto L_0x0955;
        goto L_0x0a5f;
        r0 = r5.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r0 == 0) goto L_0x0973;
        r0 = "NotificationActionPinnedGeoLive";
        r4 = 2131493928; // 0x7f0c0428 float:1.861135E38 double:1.053097924E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        r0 = r5.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact;
        if (r0 == 0) goto L_0x0991;
        r0 = "NotificationActionPinnedContact";
        r4 = 2131493920; // 0x7f0c0420 float:1.8611334E38 double:1.05309792E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        r0 = r5.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r0 == 0) goto L_0x09ea;
        r0 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r0 < r4) goto L_0x09d4;
        r0 = r5.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x09d4;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r4 = "🖼 ";
        r0.append(r4);
        r4 = r5.messageOwner;
        r4 = r4.message;
        r0.append(r4);
        r0 = r0.toString();
        r4 = "NotificationActionPinnedText";
        r6 = 3;
        r6 = new java.lang.Object[r6];
        r12 = 0;
        r6[r12] = r8;
        r12 = 1;
        r6[r12] = r0;
        r12 = r10.title;
        r13 = 2;
        r6[r13] = r12;
        r0 = org.telegram.messenger.LocaleController.formatString(r4, r9, r6);
        goto L_0x0821;
        r13 = 2;
        r0 = "NotificationActionPinnedPhoto";
        r4 = 2131493938; // 0x7f0c0432 float:1.861137E38 double:1.053097929E-314;
        r6 = new java.lang.Object[r13];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        r0 = r5.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r0 == 0) goto L_0x0a08;
        r0 = "NotificationActionPinnedGame";
        r4 = 2131493924; // 0x7f0c0424 float:1.8611342E38 double:1.053097922E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        r0 = r5.messageText;
        if (r0 == 0) goto L_0x0a49;
        r0 = r5.messageText;
        r0 = r0.length();
        if (r0 <= 0) goto L_0x0a49;
        r0 = r5.messageText;
        r6 = r0.length();
        if (r6 <= r4) goto L_0x0a33;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r12 = 0;
        r4 = r0.subSequence(r12, r4);
        r6.append(r4);
        r4 = "...";
        r6.append(r4);
        r0 = r6.toString();
        goto L_0x0a34;
        r12 = 0;
        r4 = "NotificationActionPinnedText";
        r6 = 3;
        r6 = new java.lang.Object[r6];
        r6[r12] = r8;
        r12 = 1;
        r6[r12] = r0;
        r12 = r10.title;
        r13 = 2;
        r6[r13] = r12;
        r0 = org.telegram.messenger.LocaleController.formatString(r4, r9, r6);
        goto L_0x0821;
        r13 = 2;
        r0 = "NotificationActionPinnedNoText";
        r4 = 2131493936; // 0x7f0c0430 float:1.8611366E38 double:1.053097928E-314;
        r6 = new java.lang.Object[r13];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        r9 = 0;
        r12 = 1;
        r13 = 2;
        r0 = "NotificationActionPinnedGeo";
        r4 = 2131493926; // 0x7f0c0426 float:1.8611346E38 double:1.053097923E-314;
        r6 = new java.lang.Object[r13];
        r6[r9] = r8;
        r9 = r10.title;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r0, r4, r6);
        goto L_0x0821;
        goto L_0x12ce;
        r0 = r1.replyMessageObject;
        if (r0 != 0) goto L_0x0a8e;
        r0 = "NotificationActionPinnedNoTextChannel";
        r4 = 2131493937; // 0x7f0c0431 float:1.8611368E38 double:1.0530979286E-314;
        r5 = 1;
        r5 = new java.lang.Object[r5];
        r6 = r10.title;
        r9 = 0;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r1.replyMessageObject;
        r5 = r0.isMusic();
        if (r5 == 0) goto L_0x0aaa;
        r4 = "NotificationActionPinnedMusicChannel";
        r5 = 2131493935; // 0x7f0c042f float:1.8611364E38 double:1.0530979276E-314;
        r6 = 1;
        r6 = new java.lang.Object[r6];
        r9 = r10.title;
        r12 = 0;
        r6[r12] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        r11 = r4;
        goto L_0x0d20;
        r5 = r0.isVideo();
        r6 = 2131493947; // 0x7f0c043b float:1.8611389E38 double:1.0530979335E-314;
        if (r5 == 0) goto L_0x0afc;
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 19;
        if (r4 < r5) goto L_0x0aea;
        r4 = r0.messageOwner;
        r4 = r4.message;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x0aea;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "📹 ";
        r4.append(r5);
        r5 = r0.messageOwner;
        r5 = r5.message;
        r4.append(r5);
        r4 = r4.toString();
        r5 = "NotificationActionPinnedTextChannel";
        r9 = 2;
        r9 = new java.lang.Object[r9];
        r12 = r10.title;
        r13 = 0;
        r9[r13] = r12;
        r12 = 1;
        r9[r12] = r4;
        r4 = org.telegram.messenger.LocaleController.formatString(r5, r6, r9);
        goto L_0x0aa7;
        r12 = 1;
        r4 = "NotificationActionPinnedVideoChannel";
        r5 = 2131493949; // 0x7f0c043d float:1.8611393E38 double:1.0530979345E-314;
        r6 = new java.lang.Object[r12];
        r9 = r10.title;
        r12 = 0;
        r6[r12] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        r5 = r0.isGif();
        if (r5 == 0) goto L_0x0b4d;
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 19;
        if (r4 < r5) goto L_0x0b3a;
        r4 = r0.messageOwner;
        r4 = r4.message;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x0b3a;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "🎬 ";
        r4.append(r5);
        r5 = r0.messageOwner;
        r5 = r5.message;
        r4.append(r5);
        r4 = r4.toString();
        r5 = "NotificationActionPinnedTextChannel";
        r9 = 2;
        r9 = new java.lang.Object[r9];
        r12 = r10.title;
        r13 = 0;
        r9[r13] = r12;
        r12 = 1;
        r9[r12] = r4;
        r4 = org.telegram.messenger.LocaleController.formatString(r5, r6, r9);
        goto L_0x0aa7;
        r12 = 1;
        r4 = "NotificationActionPinnedGifChannel";
        r5 = 2131493931; // 0x7f0c042b float:1.8611356E38 double:1.0530979256E-314;
        r6 = new java.lang.Object[r12];
        r9 = r10.title;
        r13 = 0;
        r6[r13] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        r12 = 1;
        r13 = 0;
        r5 = r0.isVoice();
        if (r5 == 0) goto L_0x0b66;
        r4 = "NotificationActionPinnedVoiceChannel";
        r5 = 2131493951; // 0x7f0c043f float:1.8611397E38 double:1.0530979355E-314;
        r6 = new java.lang.Object[r12];
        r9 = r10.title;
        r6[r13] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        r5 = r0.isRoundVideo();
        if (r5 == 0) goto L_0x0b7d;
        r4 = "NotificationActionPinnedRoundChannel";
        r5 = 2131493941; // 0x7f0c0435 float:1.8611376E38 double:1.0530979306E-314;
        r6 = new java.lang.Object[r12];
        r9 = r10.title;
        r6[r13] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        r5 = r0.isSticker();
        if (r5 == 0) goto L_0x0bb3;
        r4 = r0.getStickerEmoji();
        if (r4 == 0) goto L_0x0b9e;
        r5 = "NotificationActionPinnedStickerEmojiChannel";
        r6 = 2131493945; // 0x7f0c0439 float:1.8611384E38 double:1.0530979325E-314;
        r9 = 2;
        r9 = new java.lang.Object[r9];
        r12 = r10.title;
        r13 = 0;
        r9[r13] = r12;
        r12 = 1;
        r9[r12] = r4;
        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r9);
        goto L_0x0baf;
        r12 = 1;
        r13 = 0;
        r5 = "NotificationActionPinnedStickerChannel";
        r6 = 2131493943; // 0x7f0c0437 float:1.861138E38 double:1.0530979316E-314;
        r9 = new java.lang.Object[r12];
        r12 = r10.title;
        r9[r13] = r12;
        r5 = org.telegram.messenger.LocaleController.formatString(r5, r6, r9);
        r11 = r5;
        goto L_0x0d20;
        r5 = r0.messageOwner;
        r5 = r5.media;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r5 == 0) goto L_0x0c06;
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 19;
        if (r4 < r5) goto L_0x0bf3;
        r4 = r0.messageOwner;
        r4 = r4.message;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x0bf3;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "📎 ";
        r4.append(r5);
        r5 = r0.messageOwner;
        r5 = r5.message;
        r4.append(r5);
        r4 = r4.toString();
        r5 = "NotificationActionPinnedTextChannel";
        r9 = 2;
        r9 = new java.lang.Object[r9];
        r12 = r10.title;
        r13 = 0;
        r9[r13] = r12;
        r12 = 1;
        r9[r12] = r4;
        r4 = org.telegram.messenger.LocaleController.formatString(r5, r6, r9);
        goto L_0x0aa7;
        r12 = 1;
        r4 = "NotificationActionPinnedFileChannel";
        r5 = 2131493923; // 0x7f0c0423 float:1.861134E38 double:1.0530979217E-314;
        r6 = new java.lang.Object[r12];
        r9 = r10.title;
        r12 = 0;
        r6[r12] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        r5 = r0.messageOwner;
        r5 = r5.media;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
        if (r5 != 0) goto L_0x0d0d;
        r5 = r0.messageOwner;
        r5 = r5.media;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
        if (r5 == 0) goto L_0x0c18;
        goto L_0x0d0d;
        r5 = r0.messageOwner;
        r5 = r5.media;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r5 == 0) goto L_0x0c33;
        r4 = "NotificationActionPinnedGeoLiveChannel";
        r5 = 2131493929; // 0x7f0c0429 float:1.8611352E38 double:1.0530979246E-314;
        r6 = 1;
        r6 = new java.lang.Object[r6];
        r9 = r10.title;
        r12 = 0;
        r6[r12] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        r5 = r0.messageOwner;
        r5 = r5.media;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact;
        if (r5 == 0) goto L_0x0c4e;
        r4 = "NotificationActionPinnedContactChannel";
        r5 = 2131493921; // 0x7f0c0421 float:1.8611336E38 double:1.0530979207E-314;
        r6 = 1;
        r6 = new java.lang.Object[r6];
        r9 = r10.title;
        r12 = 0;
        r6[r12] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        r5 = r0.messageOwner;
        r5 = r5.media;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r5 == 0) goto L_0x0ca1;
        r4 = android.os.Build.VERSION.SDK_INT;
        r5 = 19;
        if (r4 < r5) goto L_0x0c8e;
        r4 = r0.messageOwner;
        r4 = r4.message;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x0c8e;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "🖼 ";
        r4.append(r5);
        r5 = r0.messageOwner;
        r5 = r5.message;
        r4.append(r5);
        r4 = r4.toString();
        r5 = "NotificationActionPinnedTextChannel";
        r9 = 2;
        r9 = new java.lang.Object[r9];
        r12 = r10.title;
        r13 = 0;
        r9[r13] = r12;
        r12 = 1;
        r9[r12] = r4;
        r4 = org.telegram.messenger.LocaleController.formatString(r5, r6, r9);
        goto L_0x0aa7;
        r12 = 1;
        r4 = "NotificationActionPinnedPhotoChannel";
        r5 = 2131493939; // 0x7f0c0433 float:1.8611372E38 double:1.0530979296E-314;
        r6 = new java.lang.Object[r12];
        r9 = r10.title;
        r12 = 0;
        r6[r12] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        r5 = r0.messageOwner;
        r5 = r5.media;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r5 == 0) goto L_0x0cbc;
        r4 = "NotificationActionPinnedGameChannel";
        r5 = 2131493925; // 0x7f0c0425 float:1.8611344E38 double:1.0530979227E-314;
        r6 = 1;
        r6 = new java.lang.Object[r6];
        r9 = r10.title;
        r12 = 0;
        r6[r12] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        r5 = r0.messageText;
        if (r5 == 0) goto L_0x0cfa;
        r5 = r0.messageText;
        r5 = r5.length();
        if (r5 <= 0) goto L_0x0cfa;
        r5 = r0.messageText;
        r9 = r5.length();
        if (r9 <= r4) goto L_0x0ce7;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r12 = 0;
        r4 = r5.subSequence(r12, r4);
        r9.append(r4);
        r4 = "...";
        r9.append(r4);
        r5 = r9.toString();
        goto L_0x0ce8;
        r12 = 0;
        r4 = "NotificationActionPinnedTextChannel";
        r9 = 2;
        r9 = new java.lang.Object[r9];
        r13 = r10.title;
        r9[r12] = r13;
        r12 = 1;
        r9[r12] = r5;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r6, r9);
        goto L_0x0aa7;
        r12 = 1;
        r4 = "NotificationActionPinnedNoTextChannel";
        r5 = 2131493937; // 0x7f0c0431 float:1.8611368E38 double:1.0530979286E-314;
        r6 = new java.lang.Object[r12];
        r9 = r10.title;
        r13 = 0;
        r6[r13] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        r11 = r0;
        goto L_0x0d20;
        r12 = 1;
        r13 = 0;
        r4 = "NotificationActionPinnedGeoChannel";
        r5 = 2131493927; // 0x7f0c0427 float:1.8611348E38 double:1.0530979236E-314;
        r6 = new java.lang.Object[r12];
        r9 = r10.title;
        r6[r13] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x0aa7;
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.action;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGameScore;
        if (r0 == 0) goto L_0x12ce;
        r0 = r1.messageText;
        r11 = r0.toString();
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.to_id;
        r0 = r0.channel_id;
        if (r0 == 0) goto L_0x0d51;
        r0 = r10.megagroup;
        if (r0 != 0) goto L_0x0d51;
        r0 = "ChannelPhotoEditNotification";
        r4 = 2131493196; // 0x7f0c014c float:1.8609865E38 double:1.0530975625E-314;
        r5 = 1;
        r5 = new java.lang.Object[r5];
        r6 = r10.title;
        r9 = 0;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r9 = 0;
        r0 = "NotificationEditedGroupPhoto";
        r4 = 2131493955; // 0x7f0c0443 float:1.8611405E38 double:1.0530979375E-314;
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r5[r9] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
    L_0x0d67:
        r0 = org.telegram.messenger.ChatObject.isChannel(r10);
        if (r0 == 0) goto L_0x0fd4;
        r0 = r10.megagroup;
        if (r0 != 0) goto L_0x0fd4;
        r0 = r20.isMediaEmpty();
        if (r0 == 0) goto L_0x0db4;
        if (r21 != 0) goto L_0x0da3;
        r0 = r1.messageOwner;
        r0 = r0.message;
        if (r0 == 0) goto L_0x0da3;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = r0.length();
        if (r0 == 0) goto L_0x0da3;
        r0 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r6 = r1.messageOwner;
        r6 = r6.message;
        r9 = 1;
        r4[r9] = r6;
        r6 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r6, r4);
        r22[r5] = r9;
        goto L_0x12ce;
        r5 = 0;
        r9 = 1;
        r0 = "ChannelMessageNoText";
        r4 = new java.lang.Object[r9];
        r4[r5] = r8;
        r5 = 2131493187; // 0x7f0c0143 float:1.8609847E38 double:1.053097558E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r5, r4);
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r0 == 0) goto L_0x0e0b;
        if (r21 != 0) goto L_0x0dfa;
        r0 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r0 < r4) goto L_0x0dfa;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x0dfa;
        r0 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "🖼 ";
        r5.append(r6);
        r6 = r1.messageOwner;
        r6 = r6.message;
        r5.append(r6);
        r5 = r5.toString();
        r6 = 1;
        r4[r6] = r5;
        r5 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r5, r4);
        r0 = 0;
        r22[r0] = r6;
        goto L_0x12ce;
        r0 = 0;
        r6 = 1;
        r4 = "ChannelMessagePhoto";
        r5 = 2131493188; // 0x7f0c0144 float:1.860985E38 double:1.0530975585E-314;
        r6 = new java.lang.Object[r6];
        r6[r0] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r0 = r20.isVideo();
        if (r0 == 0) goto L_0x0e60;
        if (r21 != 0) goto L_0x0e4f;
        r0 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r0 < r4) goto L_0x0e4f;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x0e4f;
        r0 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "📹 ";
        r5.append(r6);
        r6 = r1.messageOwner;
        r6 = r6.message;
        r5.append(r6);
        r5 = r5.toString();
        r6 = 1;
        r4[r6] = r5;
        r5 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r5, r4);
        r0 = 0;
        r22[r0] = r6;
        goto L_0x12ce;
        r0 = 0;
        r6 = 1;
        r4 = "ChannelMessageVideo";
        r5 = 2131493192; // 0x7f0c0148 float:1.8609857E38 double:1.0530975605E-314;
        r6 = new java.lang.Object[r6];
        r6[r0] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r0 = 0;
        r6 = 1;
        r4 = r20.isVoice();
        if (r4 == 0) goto L_0x0e77;
        r4 = "ChannelMessageAudio";
        r5 = 2131493179; // 0x7f0c013b float:1.860983E38 double:1.053097554E-314;
        r6 = new java.lang.Object[r6];
        r6[r0] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r4 = r20.isRoundVideo();
        if (r4 == 0) goto L_0x0e8c;
        r4 = "ChannelMessageRound";
        r5 = 2131493189; // 0x7f0c0145 float:1.8609851E38 double:1.053097559E-314;
        r6 = new java.lang.Object[r6];
        r6[r0] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r4 = r20.isMusic();
        if (r4 == 0) goto L_0x0ea1;
        r4 = "ChannelMessageMusic";
        r5 = 2131493186; // 0x7f0c0142 float:1.8609845E38 double:1.0530975575E-314;
        r6 = new java.lang.Object[r6];
        r6[r0] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact;
        if (r0 == 0) goto L_0x0eba;
        r0 = "ChannelMessageContact";
        r4 = 2131493180; // 0x7f0c013c float:1.8609833E38 double:1.0530975546E-314;
        r5 = 1;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
        if (r0 != 0) goto L_0x0fc3;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
        if (r0 == 0) goto L_0x0ecc;
        goto L_0x0fc3;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r0 == 0) goto L_0x0ee5;
        r0 = "ChannelMessageLiveLocation";
        r4 = 2131493184; // 0x7f0c0140 float:1.860984E38 double:1.0530975566E-314;
        r5 = 1;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r0 == 0) goto L_0x12ce;
        r0 = r20.isSticker();
        if (r0 == 0) goto L_0x0f1f;
        r0 = r20.getStickerEmoji();
        if (r0 == 0) goto L_0x0f0d;
        r4 = "ChannelMessageStickerEmoji";
        r5 = 2131493191; // 0x7f0c0147 float:1.8609855E38 double:1.05309756E-314;
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r12 = 1;
        r6[r12] = r0;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        r11 = r4;
        goto L_0x0f1d;
        r9 = 0;
        r12 = 1;
        r4 = "ChannelMessageSticker";
        r5 = 2131493190; // 0x7f0c0146 float:1.8609853E38 double:1.0530975595E-314;
        r6 = new java.lang.Object[r12];
        r6[r9] = r8;
        r0 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        r11 = r0;
        goto L_0x12ce;
        r0 = r20.isGif();
        if (r0 == 0) goto L_0x0f74;
        if (r21 != 0) goto L_0x0f63;
        r0 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r0 < r4) goto L_0x0f63;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x0f63;
        r0 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "🎬 ";
        r5.append(r6);
        r6 = r1.messageOwner;
        r6 = r6.message;
        r5.append(r6);
        r5 = r5.toString();
        r6 = 1;
        r4[r6] = r5;
        r5 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r5, r4);
        r0 = 0;
        r22[r0] = r6;
        goto L_0x12ce;
        r0 = 0;
        r6 = 1;
        r4 = "ChannelMessageGIF";
        r5 = 2131493183; // 0x7f0c013f float:1.8609839E38 double:1.053097556E-314;
        r6 = new java.lang.Object[r6];
        r6[r0] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        if (r21 != 0) goto L_0x0fb2;
        r0 = android.os.Build.VERSION.SDK_INT;
        r4 = 19;
        if (r0 < r4) goto L_0x0fb2;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x0fb2;
        r0 = "NotificationMessageText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r5 = 0;
        r4[r5] = r8;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r6 = "📎 ";
        r5.append(r6);
        r6 = r1.messageOwner;
        r6 = r6.message;
        r5.append(r6);
        r5 = r5.toString();
        r6 = 1;
        r4[r6] = r5;
        r5 = 2131494000; // 0x7f0c0470 float:1.8611496E38 double:1.0530979597E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r5, r4);
        r0 = 0;
        r22[r0] = r6;
        goto L_0x12ce;
        r0 = 0;
        r6 = 1;
        r4 = "ChannelMessageDocument";
        r5 = 2131493181; // 0x7f0c013d float:1.8609835E38 double:1.053097555E-314;
        r6 = new java.lang.Object[r6];
        r6[r0] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r0 = 0;
        r6 = 1;
        r4 = "ChannelMessageMap";
        r5 = 2131493185; // 0x7f0c0141 float:1.8609843E38 double:1.053097557E-314;
        r6 = new java.lang.Object[r6];
        r6[r0] = r8;
        r11 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        goto L_0x12ce;
        r0 = r20.isMediaEmpty();
        r4 = 2131493987; // 0x7f0c0463 float:1.861147E38 double:1.0530979533E-314;
        if (r0 == 0) goto L_0x101f;
        if (r21 != 0) goto L_0x1009;
        r0 = r1.messageOwner;
        r0 = r0.message;
        if (r0 == 0) goto L_0x1009;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = r0.length();
        if (r0 == 0) goto L_0x1009;
        r0 = "NotificationMessageGroupText";
        r5 = 3;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r6 = r1.messageOwner;
        r6 = r6.message;
        r9 = 2;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r9 = 2;
        r0 = "NotificationMessageGroupNoText";
        r4 = new java.lang.Object[r9];
        r5 = 0;
        r4[r5] = r8;
        r5 = r10.title;
        r6 = 1;
        r4[r6] = r5;
        r5 = 2131493982; // 0x7f0c045e float:1.861146E38 double:1.053097951E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r5, r4);
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r0 == 0) goto L_0x107a;
        if (r21 != 0) goto L_0x1064;
        r0 = android.os.Build.VERSION.SDK_INT;
        r5 = 19;
        if (r0 < r5) goto L_0x1064;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x1064;
        r0 = "NotificationMessageGroupText";
        r5 = 3;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r9 = "🖼 ";
        r6.append(r9);
        r9 = r1.messageOwner;
        r9 = r9.message;
        r6.append(r9);
        r6 = r6.toString();
        r9 = 2;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r9 = 2;
        r0 = "NotificationMessageGroupPhoto";
        r4 = 2131493983; // 0x7f0c045f float:1.8611462E38 double:1.0530979513E-314;
        r5 = new java.lang.Object[r9];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r20.isVideo();
        if (r0 == 0) goto L_0x10d3;
        if (r21 != 0) goto L_0x10bd;
        r0 = android.os.Build.VERSION.SDK_INT;
        r5 = 19;
        if (r0 < r5) goto L_0x10bd;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x10bd;
        r0 = "NotificationMessageGroupText";
        r5 = 3;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r9 = "📹 ";
        r6.append(r9);
        r9 = r1.messageOwner;
        r9 = r9.message;
        r6.append(r9);
        r6 = r6.toString();
        r9 = 2;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r9 = 2;
        r0 = "NotificationMessageGroupVideo";
        r4 = 2131493988; // 0x7f0c0464 float:1.8611472E38 double:1.053097954E-314;
        r5 = new java.lang.Object[r9];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r20.isVoice();
        if (r0 == 0) goto L_0x10ef;
        r0 = "NotificationMessageGroupAudio";
        r4 = 2131493973; // 0x7f0c0455 float:1.8611441E38 double:1.0530979464E-314;
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r20.isRoundVideo();
        if (r0 == 0) goto L_0x110b;
        r0 = "NotificationMessageGroupRound";
        r4 = 2131493984; // 0x7f0c0460 float:1.8611464E38 double:1.053097952E-314;
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r20.isMusic();
        if (r0 == 0) goto L_0x1127;
        r0 = "NotificationMessageGroupMusic";
        r4 = 2131493981; // 0x7f0c045d float:1.8611457E38 double:1.0530979503E-314;
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaContact;
        if (r0 == 0) goto L_0x1145;
        r0 = "NotificationMessageGroupContact";
        r4 = 2131493974; // 0x7f0c0456 float:1.8611443E38 double:1.053097947E-314;
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r0 == 0) goto L_0x116e;
        r0 = "NotificationMessageGroupGame";
        r4 = 2131493976; // 0x7f0c0458 float:1.8611447E38 double:1.053097948E-314;
        r5 = 3;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r6 = r1.messageOwner;
        r6 = r6.media;
        r6 = r6.game;
        r6 = r6.title;
        r9 = 2;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
        if (r0 != 0) goto L_0x128c;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
        if (r0 == 0) goto L_0x1180;
        goto L_0x128c;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r0 == 0) goto L_0x119e;
        r0 = "NotificationMessageGroupLiveLocation";
        r4 = 2131493979; // 0x7f0c045b float:1.8611453E38 double:1.0530979493E-314;
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r0 = r1.messageOwner;
        r0 = r0.media;
        r0 = r0 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r0 == 0) goto L_0x12ce;
        r0 = r20.isSticker();
        if (r0 == 0) goto L_0x11e2;
        r0 = r20.getStickerEmoji();
        if (r0 == 0) goto L_0x11cb;
        r4 = "NotificationMessageGroupStickerEmoji";
        r5 = 2131493986; // 0x7f0c0462 float:1.8611468E38 double:1.053097953E-314;
        r6 = 3;
        r6 = new java.lang.Object[r6];
        r9 = 0;
        r6[r9] = r8;
        r9 = r10.title;
        r12 = 1;
        r6[r12] = r9;
        r13 = 2;
        r6[r13] = r0;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        r11 = r4;
        goto L_0x11e0;
        r9 = 0;
        r12 = 1;
        r13 = 2;
        r4 = "NotificationMessageGroupSticker";
        r5 = 2131493985; // 0x7f0c0461 float:1.8611466E38 double:1.0530979523E-314;
        r6 = new java.lang.Object[r13];
        r6[r9] = r8;
        r9 = r10.title;
        r6[r12] = r9;
        r0 = org.telegram.messenger.LocaleController.formatString(r4, r5, r6);
        r11 = r0;
        goto L_0x12ce;
        r0 = r20.isGif();
        if (r0 == 0) goto L_0x123b;
        if (r21 != 0) goto L_0x1225;
        r0 = android.os.Build.VERSION.SDK_INT;
        r5 = 19;
        if (r0 < r5) goto L_0x1225;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x1225;
        r0 = "NotificationMessageGroupText";
        r5 = 3;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r9 = "🎬 ";
        r6.append(r9);
        r9 = r1.messageOwner;
        r9 = r9.message;
        r6.append(r9);
        r6 = r6.toString();
        r9 = 2;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r9 = 2;
        r0 = "NotificationMessageGroupGif";
        r4 = 2131493977; // 0x7f0c0459 float:1.861145E38 double:1.0530979484E-314;
        r5 = new java.lang.Object[r9];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        if (r21 != 0) goto L_0x1277;
        r0 = android.os.Build.VERSION.SDK_INT;
        r5 = 19;
        if (r0 < r5) goto L_0x1277;
        r0 = r1.messageOwner;
        r0 = r0.message;
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x1277;
        r0 = "NotificationMessageGroupText";
        r5 = 3;
        r5 = new java.lang.Object[r5];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r9 = 1;
        r5[r9] = r6;
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r9 = "📎 ";
        r6.append(r9);
        r9 = r1.messageOwner;
        r9 = r9.message;
        r6.append(r9);
        r6 = r6.toString();
        r9 = 2;
        r5[r9] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r9 = 2;
        r0 = "NotificationMessageGroupDocument";
        r4 = 2131493975; // 0x7f0c0457 float:1.8611445E38 double:1.0530979474E-314;
        r5 = new java.lang.Object[r9];
        r6 = 0;
        r5[r6] = r8;
        r6 = r10.title;
        r12 = 1;
        r5[r12] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
        r6 = 0;
        r9 = 2;
        r12 = 1;
        r0 = "NotificationMessageGroupMap";
        r4 = 2131493980; // 0x7f0c045c float:1.8611455E38 double:1.05309795E-314;
        r5 = new java.lang.Object[r9];
        r5[r6] = r8;
        r6 = r10.title;
        r5[r12] = r6;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r4, r5);
        goto L_0x12ce;
    L_0x12a1:
        r12 = 1;
        r0 = org.telegram.messenger.ChatObject.isChannel(r10);
        if (r0 == 0) goto L_0x12bb;
        r0 = r10.megagroup;
        if (r0 != 0) goto L_0x12bb;
        r0 = "ChannelMessageNoText";
        r4 = new java.lang.Object[r12];
        r5 = 0;
        r4[r5] = r8;
        r5 = 2131493187; // 0x7f0c0143 float:1.8609847E38 double:1.053097558E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r5, r4);
        goto L_0x12ce;
        r5 = 0;
        r0 = "NotificationMessageGroupNoText";
        r4 = 2;
        r4 = new java.lang.Object[r4];
        r4[r5] = r8;
        r5 = r10.title;
        r4[r12] = r5;
        r5 = 2131493982; // 0x7f0c045e float:1.861146E38 double:1.053097951E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r0, r5, r4);
    L_0x12ce:
        return r11;
    L_0x12cf:
        r0 = "YouHaveNewMessage";
        r0 = org.telegram.messenger.LocaleController.getString(r0, r4);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationsController.getStringForMessage(org.telegram.messenger.MessageObject, boolean, boolean[]):java.lang.String");
    }

    static {
        notificationManager = null;
        systemNotificationManager = null;
        if (VERSION.SDK_INT >= 26 && ApplicationLoader.applicationContext != null) {
            notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
            systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService("notification");
            NotificationChannel notificationChannel = new NotificationChannel(OTHER_NOTIFICATIONS_CHANNEL, "Other", 3);
            notificationChannel.enableLights(false);
            notificationChannel.enableVibration(false);
            notificationChannel.setSound(null, null);
            systemNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public static NotificationsController getInstance(int num) {
        NotificationsController localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (NotificationsController.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    NotificationsController[] notificationsControllerArr = Instance;
                    NotificationsController notificationsController = new NotificationsController(num);
                    localInstance = notificationsController;
                    notificationsControllerArr[num] = notificationsController;
                }
            }
        }
        return localInstance;
    }

    public NotificationsController(int instance) {
        this.currentAccount = instance;
        this.notificationId = this.currentAccount + 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("messages");
        stringBuilder.append(this.currentAccount == 0 ? TtmlNode.ANONYMOUS_REGION_ID : Integer.valueOf(this.currentAccount));
        this.notificationGroup = stringBuilder.toString();
        SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
        this.inChatSoundEnabled = preferences.getBoolean("EnableInChatSound", true);
        this.showBadgeNumber = preferences.getBoolean("badgeNumber", true);
        notificationManager = NotificationManagerCompat.from(ApplicationLoader.applicationContext);
        systemNotificationManager = (NotificationManager) ApplicationLoader.applicationContext.getSystemService("notification");
        try {
            audioManager = (AudioManager) ApplicationLoader.applicationContext.getSystemService(MimeTypes.BASE_TYPE_AUDIO);
        } catch (Throwable e) {
            FileLog.e(e);
        }
        try {
            this.alarmManager = (AlarmManager) ApplicationLoader.applicationContext.getSystemService("alarm");
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        try {
            this.notificationDelayWakelock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(1, "lock");
            this.notificationDelayWakelock.setReferenceCounted(false);
        } catch (Throwable e3) {
            FileLog.e(e3);
        }
        this.notificationDelayRunnable = new Runnable() {
            public void run() {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("delay reached");
                }
                if (!NotificationsController.this.delayedPushMessages.isEmpty()) {
                    NotificationsController.this.showOrUpdateNotification(true);
                    NotificationsController.this.delayedPushMessages.clear();
                } else if (NotificationsController.this.lastNotificationIsNoData) {
                    NotificationsController.notificationManager.cancel(NotificationsController.this.notificationId);
                }
                try {
                    if (NotificationsController.this.notificationDelayWakelock.isHeld()) {
                        NotificationsController.this.notificationDelayWakelock.release();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        };
    }

    public void cleanup() {
        this.popupMessages.clear();
        this.popupReplyMessages.clear();
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                NotificationsController.this.opened_dialog_id = 0;
                int a = 0;
                NotificationsController.this.total_unread_count = 0;
                NotificationsController.this.personal_count = 0;
                NotificationsController.this.pushMessages.clear();
                NotificationsController.this.pushMessagesDict.clear();
                NotificationsController.this.pushDialogs.clear();
                NotificationsController.this.wearNotificationsIds.clear();
                NotificationsController.this.lastWearNotifiedMessageId.clear();
                NotificationsController.this.delayedPushMessages.clear();
                NotificationsController.this.notifyCheck = false;
                NotificationsController.this.lastBadgeCount = 0;
                try {
                    if (NotificationsController.this.notificationDelayWakelock.isHeld()) {
                        NotificationsController.this.notificationDelayWakelock.release();
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                NotificationsController.this.setBadge(NotificationsController.this.getTotalAllUnreadCount());
                Editor editor = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount).edit();
                editor.clear();
                editor.commit();
                if (VERSION.SDK_INT >= 26) {
                    try {
                        String keyStart = new StringBuilder();
                        keyStart.append(NotificationsController.this.currentAccount);
                        keyStart.append("channel");
                        keyStart = keyStart.toString();
                        List<NotificationChannel> list = NotificationsController.systemNotificationManager.getNotificationChannels();
                        int count = list.size();
                        while (a < count) {
                            String id = ((NotificationChannel) list.get(a)).getId();
                            if (id.startsWith(keyStart)) {
                                NotificationsController.systemNotificationManager.deleteNotificationChannel(id);
                            }
                            a++;
                        }
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                    }
                }
            }
        });
    }

    public void setInChatSoundEnabled(boolean value) {
        this.inChatSoundEnabled = value;
    }

    public void setOpenedDialogId(final long dialog_id) {
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                NotificationsController.this.opened_dialog_id = dialog_id;
            }
        });
    }

    public void setLastOnlineFromOtherDevice(final int time) {
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("set last online from other device = ");
                    stringBuilder.append(time);
                    FileLog.d(stringBuilder.toString());
                }
                NotificationsController.this.lastOnlineFromOtherDevice = time;
            }
        });
    }

    public void removeNotificationsForDialog(long did) {
        getInstance(this.currentAccount).processReadMessages(null, did, 0, ConnectionsManager.DEFAULT_DATACENTER_ID, false);
        LongSparseArray<Integer> dialogsToUpdate = new LongSparseArray();
        dialogsToUpdate.put(did, Integer.valueOf(0));
        getInstance(this.currentAccount).processDialogsUpdateRead(dialogsToUpdate);
    }

    public boolean hasMessagesToReply() {
        for (int a = 0; a < this.pushMessages.size(); a++) {
            MessageObject messageObject = (MessageObject) this.pushMessages.get(a);
            long dialog_id = messageObject.getDialogId();
            if (!((messageObject.messageOwner.mentioned && (messageObject.messageOwner.action instanceof TL_messageActionPinMessage)) || ((int) dialog_id) == 0)) {
                if (messageObject.messageOwner.to_id.channel_id == 0 || messageObject.isMegagroup()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected void forceShowPopupForReply() {
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                final ArrayList<MessageObject> popupArray = new ArrayList();
                for (int a = 0; a < NotificationsController.this.pushMessages.size(); a++) {
                    MessageObject messageObject = (MessageObject) NotificationsController.this.pushMessages.get(a);
                    long dialog_id = messageObject.getDialogId();
                    if (!((messageObject.messageOwner.mentioned && (messageObject.messageOwner.action instanceof TL_messageActionPinMessage)) || ((int) dialog_id) == 0)) {
                        if (messageObject.messageOwner.to_id.channel_id == 0 || messageObject.isMegagroup()) {
                            popupArray.add(0, messageObject);
                        }
                    }
                }
                if (!popupArray.isEmpty() && !AndroidUtilities.needShowPasscode(false)) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.this.popupReplyMessages = popupArray;
                            Intent popupIntent = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
                            popupIntent.putExtra("force", true);
                            popupIntent.putExtra("currentAccount", NotificationsController.this.currentAccount);
                            popupIntent.setFlags(268763140);
                            ApplicationLoader.applicationContext.startActivity(popupIntent);
                            ApplicationLoader.applicationContext.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                        }
                    });
                }
            }
        });
    }

    public void removeDeletedMessagesFromNotifications(final SparseArray<ArrayList<Integer>> deletedMessages) {
        final ArrayList<MessageObject> popupArrayRemove = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                int old_unread_count = NotificationsController.this.total_unread_count;
                SharedPreferences preferences = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount);
                int i = 0;
                int a = 0;
                while (a < deletedMessages.size()) {
                    int a2;
                    long dialog_id;
                    int key = deletedMessages.keyAt(a);
                    long dialog_id2 = (long) (-key);
                    ArrayList<Integer> mids = (ArrayList) deletedMessages.get(key);
                    Integer currentCount = (Integer) NotificationsController.this.pushDialogs.get(dialog_id2);
                    if (currentCount == null) {
                        currentCount = Integer.valueOf(i);
                    }
                    Integer newCount = currentCount;
                    int b = i;
                    while (b < mids.size()) {
                        a2 = a;
                        dialog_id = dialog_id2;
                        long mid = ((long) ((Integer) mids.get(b)).intValue()) | (((long) key) << 32);
                        MessageObject messageObject = (MessageObject) NotificationsController.this.pushMessagesDict.get(mid);
                        if (messageObject != null) {
                            NotificationsController.this.pushMessagesDict.remove(mid);
                            NotificationsController.this.delayedPushMessages.remove(messageObject);
                            NotificationsController.this.pushMessages.remove(messageObject);
                            if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                NotificationsController.this.personal_count = NotificationsController.this.personal_count - 1;
                            }
                            popupArrayRemove.add(messageObject);
                            newCount = Integer.valueOf(newCount.intValue() - 1);
                        }
                        b++;
                        a = a2;
                        dialog_id2 = dialog_id;
                    }
                    a2 = a;
                    dialog_id = dialog_id2;
                    if (newCount.intValue() <= 0) {
                        newCount = Integer.valueOf(0);
                        mid = dialog_id;
                        NotificationsController.this.smartNotificationsDialogs.remove(mid);
                    } else {
                        mid = dialog_id;
                    }
                    if (!newCount.equals(currentCount)) {
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count - currentCount.intValue();
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + newCount.intValue();
                        NotificationsController.this.pushDialogs.put(mid, newCount);
                    }
                    if (newCount.intValue() == 0) {
                        NotificationsController.this.pushDialogs.remove(mid);
                        NotificationsController.this.pushDialogsOverrideMention.remove(mid);
                    }
                    a = a2 + 1;
                    i = 0;
                }
                boolean z = true;
                if (!popupArrayRemove.isEmpty()) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            int size = popupArrayRemove.size();
                            for (int a = 0; a < size; a++) {
                                NotificationsController.this.popupMessages.remove(popupArrayRemove.get(a));
                            }
                        }
                    });
                }
                if (old_unread_count != NotificationsController.this.total_unread_count) {
                    if (NotificationsController.this.notifyCheck) {
                        NotificationsController notificationsController = NotificationsController.this;
                        if (NotificationsController.this.lastOnlineFromOtherDevice <= ConnectionsManager.getInstance(NotificationsController.this.currentAccount).getCurrentTime()) {
                            z = false;
                        }
                        notificationsController.scheduleNotificationDelay(z);
                    } else {
                        NotificationsController.this.delayedPushMessages.clear();
                        NotificationsController.this.showOrUpdateNotification(NotificationsController.this.notifyCheck);
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(NotificationsController.this.currentAccount));
                        }
                    });
                }
                NotificationsController.this.notifyCheck = false;
                if (NotificationsController.this.showBadgeNumber) {
                    NotificationsController.this.setBadge(NotificationsController.this.getTotalAllUnreadCount());
                }
            }
        });
    }

    public void removeDeletedHisoryFromNotifications(final SparseIntArray deletedMessages) {
        final ArrayList<MessageObject> popupArrayRemove = new ArrayList(0);
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                int old_unread_count = NotificationsController.this.total_unread_count;
                SharedPreferences preferences = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount);
                int i = 0;
                int a = 0;
                while (true) {
                    boolean z = true;
                    if (a >= deletedMessages.size()) {
                        break;
                    }
                    int a2;
                    int key = deletedMessages.keyAt(a);
                    long dialog_id = (long) (-key);
                    int id = deletedMessages.get(key);
                    Integer currentCount = (Integer) NotificationsController.this.pushDialogs.get(dialog_id);
                    if (currentCount == null) {
                        currentCount = Integer.valueOf(i);
                    }
                    Integer newCount = currentCount;
                    int c = i;
                    while (c < NotificationsController.this.pushMessages.size()) {
                        MessageObject messageObject = (MessageObject) NotificationsController.this.pushMessages.get(c);
                        if (messageObject.getDialogId() != dialog_id || messageObject.getId() > id) {
                            a2 = a;
                        } else {
                            a2 = a;
                            NotificationsController.this.pushMessagesDict.remove(messageObject.getIdWithChannel());
                            NotificationsController.this.delayedPushMessages.remove(messageObject);
                            NotificationsController.this.pushMessages.remove(messageObject);
                            c--;
                            if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                NotificationsController.this.personal_count = NotificationsController.this.personal_count - 1;
                            }
                            popupArrayRemove.add(messageObject);
                            newCount = Integer.valueOf(newCount.intValue() - 1);
                        }
                        c++;
                        a = a2;
                    }
                    a2 = a;
                    if (newCount.intValue() <= 0) {
                        newCount = Integer.valueOf(0);
                        NotificationsController.this.smartNotificationsDialogs.remove(dialog_id);
                    }
                    if (!newCount.equals(currentCount)) {
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count - currentCount.intValue();
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + newCount.intValue();
                        NotificationsController.this.pushDialogs.put(dialog_id, newCount);
                    }
                    if (newCount.intValue() == 0) {
                        NotificationsController.this.pushDialogs.remove(dialog_id);
                        NotificationsController.this.pushDialogsOverrideMention.remove(dialog_id);
                    }
                    a = a2 + 1;
                    i = 0;
                }
                if (popupArrayRemove.isEmpty()) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            int size = popupArrayRemove.size();
                            for (int a = 0; a < size; a++) {
                                NotificationsController.this.popupMessages.remove(popupArrayRemove.get(a));
                            }
                        }
                    });
                }
                if (old_unread_count != NotificationsController.this.total_unread_count) {
                    if (NotificationsController.this.notifyCheck) {
                        NotificationsController notificationsController = NotificationsController.this;
                        if (NotificationsController.this.lastOnlineFromOtherDevice <= ConnectionsManager.getInstance(NotificationsController.this.currentAccount).getCurrentTime()) {
                            z = false;
                        }
                        notificationsController.scheduleNotificationDelay(z);
                    } else {
                        NotificationsController.this.delayedPushMessages.clear();
                        NotificationsController.this.showOrUpdateNotification(NotificationsController.this.notifyCheck);
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(NotificationsController.this.currentAccount));
                        }
                    });
                }
                NotificationsController.this.notifyCheck = false;
                if (NotificationsController.this.showBadgeNumber) {
                    NotificationsController.this.setBadge(NotificationsController.this.getTotalAllUnreadCount());
                }
            }
        });
    }

    public void processReadMessages(SparseLongArray inbox, long dialog_id, int max_date, int max_id, boolean isPopup) {
        final ArrayList<MessageObject> popupArrayRemove = new ArrayList(0);
        final SparseLongArray sparseLongArray = inbox;
        final long j = dialog_id;
        final int i = max_id;
        final int i2 = max_date;
        final boolean z = isPopup;
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.NotificationsController.8.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
                /*
                r0 = this;
                r0 = r2;
                r1 = 32;
                r2 = 0;
                if (r0 == 0) goto L_0x0093;
            L_0x0007:
                r0 = r2;
            L_0x0008:
                r3 = r2;
                r3 = r3.size();
                if (r0 >= r3) goto L_0x0093;
            L_0x0010:
                r3 = r2;
                r3 = r3.keyAt(r0);
                r4 = r2;
                r4 = r4.get(r3);
                r6 = r2;
            L_0x001d:
                r7 = org.telegram.messenger.NotificationsController.this;
                r7 = r7.pushMessages;
                r7 = r7.size();
                if (r6 >= r7) goto L_0x008f;
            L_0x0029:
                r7 = org.telegram.messenger.NotificationsController.this;
                r7 = r7.pushMessages;
                r7 = r7.get(r6);
                r7 = (org.telegram.messenger.MessageObject) r7;
                r8 = r7.getDialogId();
                r10 = (long) r3;
                r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
                if (r12 != 0) goto L_0x008c;
            L_0x003e:
                r8 = r7.getId();
                r9 = (int) r4;
                if (r8 > r9) goto L_0x008c;
            L_0x0045:
                r8 = org.telegram.messenger.NotificationsController.this;
                r8 = r8.isPersonalMessage(r7);
                if (r8 == 0) goto L_0x0052;
            L_0x004d:
                r8 = org.telegram.messenger.NotificationsController.this;
                r8.personal_count = r8.personal_count - 1;
            L_0x0052:
                r8 = r3;
                r8.add(r7);
                r8 = r7.getId();
                r8 = (long) r8;
                r10 = r7.messageOwner;
                r10 = r10.to_id;
                r10 = r10.channel_id;
                if (r10 == 0) goto L_0x006f;
            L_0x0064:
                r10 = r7.messageOwner;
                r10 = r10.to_id;
                r10 = r10.channel_id;
                r10 = (long) r10;
                r10 = r10 << r1;
                r12 = r8 | r10;
                r8 = r12;
            L_0x006f:
                r10 = org.telegram.messenger.NotificationsController.this;
                r10 = r10.pushMessagesDict;
                r10.remove(r8);
                r10 = org.telegram.messenger.NotificationsController.this;
                r10 = r10.delayedPushMessages;
                r10.remove(r7);
                r10 = org.telegram.messenger.NotificationsController.this;
                r10 = r10.pushMessages;
                r10.remove(r6);
                r6 = r6 + -1;
            L_0x008c:
                r6 = r6 + 1;
                goto L_0x001d;
            L_0x008f:
                r0 = r0 + 1;
                goto L_0x0008;
            L_0x0093:
                r3 = r4;
                r5 = 0;
                r0 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
                if (r0 == 0) goto L_0x0142;
            L_0x009b:
                r0 = r6;
                if (r0 != 0) goto L_0x00a3;
            L_0x009f:
                r0 = r7;
                if (r0 == 0) goto L_0x0142;
                r0 = r2;
                r2 = org.telegram.messenger.NotificationsController.this;
                r2 = r2.pushMessages;
                r2 = r2.size();
                if (r0 >= r2) goto L_0x0142;
                r2 = org.telegram.messenger.NotificationsController.this;
                r2 = r2.pushMessages;
                r2 = r2.get(r0);
                r2 = (org.telegram.messenger.MessageObject) r2;
                r3 = r2.getDialogId();
                r5 = r4;
                r7 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
                if (r7 != 0) goto L_0x013e;
                r3 = 0;
                r4 = r7;
                if (r4 == 0) goto L_0x00d6;
                r4 = r2.messageOwner;
                r4 = r4.date;
                r5 = r7;
                if (r4 > r5) goto L_0x00f5;
                r3 = 1;
                goto L_0x00f5;
                r4 = r8;
                if (r4 != 0) goto L_0x00e8;
                r4 = r2.getId();
                r5 = r6;
                if (r4 <= r5) goto L_0x00e6;
                r4 = r6;
                if (r4 >= 0) goto L_0x00f5;
                r3 = 1;
                goto L_0x00f5;
                r4 = r2.getId();
                r5 = r6;
                if (r4 == r5) goto L_0x00f4;
                r4 = r6;
                if (r4 >= 0) goto L_0x00f5;
                r3 = 1;
                if (r3 == 0) goto L_0x013e;
                r4 = org.telegram.messenger.NotificationsController.this;
                r4 = r4.isPersonalMessage(r2);
                if (r4 == 0) goto L_0x0104;
                r4 = org.telegram.messenger.NotificationsController.this;
                r4.personal_count = r4.personal_count - 1;
                r4 = org.telegram.messenger.NotificationsController.this;
                r4 = r4.pushMessages;
                r4.remove(r0);
                r4 = org.telegram.messenger.NotificationsController.this;
                r4 = r4.delayedPushMessages;
                r4.remove(r2);
                r4 = r3;
                r4.add(r2);
                r4 = r2.getId();
                r4 = (long) r4;
                r6 = r2.messageOwner;
                r6 = r6.to_id;
                r6 = r6.channel_id;
                if (r6 == 0) goto L_0x0133;
                r6 = r2.messageOwner;
                r6 = r6.to_id;
                r6 = r6.channel_id;
                r6 = (long) r6;
                r6 = r6 << r1;
                r8 = r4 | r6;
                r4 = r8;
                r6 = org.telegram.messenger.NotificationsController.this;
                r6 = r6.pushMessagesDict;
                r6.remove(r4);
                r0 = r0 + -1;
                r2 = r0 + 1;
                goto L_0x00a4;
            L_0x0142:
                r0 = r3;
                r0 = r0.isEmpty();
                if (r0 != 0) goto L_0x0152;
                r0 = new org.telegram.messenger.NotificationsController$8$1;
                r0.<init>();
                org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationsController.8.run():void");
            }
        });
    }

    public void processNewMessages(ArrayList<MessageObject> messageObjects, boolean isLast, boolean isFcm) {
        if (!messageObjects.isEmpty()) {
            final ArrayList<MessageObject> popupArrayAdd = new ArrayList(0);
            final ArrayList<MessageObject> arrayList = messageObjects;
            final boolean z = isFcm;
            final boolean z2 = isLast;
            notificationsQueue.postRunnable(new Runnable() {
                public void run() {
                    boolean z;
                    LongSparseArray<Boolean> settingsCache = new LongSparseArray();
                    SharedPreferences preferences = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount);
                    boolean z2 = true;
                    boolean allowPinned = preferences.getBoolean("PinnedMessages", true);
                    int popup = 0;
                    boolean added = false;
                    int a = 0;
                    while (a < arrayList.size()) {
                        int index;
                        LongSparseArray<Boolean> longSparseArray;
                        MessageObject messageObject = (MessageObject) arrayList.get(a);
                        long mid = (long) messageObject.getId();
                        if (messageObject.messageOwner.to_id.channel_id != 0) {
                            mid |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
                        }
                        MessageObject oldMessageObject = (MessageObject) NotificationsController.this.pushMessagesDict.get(mid);
                        int i;
                        if (oldMessageObject == null) {
                            long dialog_id = messageObject.getDialogId();
                            long original_dialog_id = dialog_id;
                            if (dialog_id != NotificationsController.this.opened_dialog_id || !ApplicationLoader.isScreenOn) {
                                boolean added2;
                                if (messageObject.messageOwner.mentioned) {
                                    if (allowPinned || !(messageObject.messageOwner.action instanceof TL_messageActionPinMessage)) {
                                        dialog_id = (long) messageObject.messageOwner.from_id;
                                    }
                                }
                                if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                    NotificationsController.this.personal_count = NotificationsController.this.personal_count + 1;
                                }
                                int lower_id = (int) dialog_id;
                                boolean isChat = lower_id < 0 ? z2 : false;
                                index = settingsCache.indexOfKey(dialog_id);
                                if (index >= 0) {
                                    z = allowPinned;
                                    int i2 = index;
                                    allowPinned = ((Boolean) settingsCache.valueAt(index)).booleanValue();
                                } else {
                                    boolean notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                                    z = allowPinned;
                                    if (!notifyOverride) {
                                        if ((preferences.getBoolean("EnableAll", true) && (!isChat || preferences.getBoolean("EnableGroup", true))) || notifyOverride) {
                                            allowPinned = true;
                                            settingsCache.put(dialog_id, Boolean.valueOf(allowPinned));
                                        }
                                    }
                                    allowPinned = false;
                                    settingsCache.put(dialog_id, Boolean.valueOf(allowPinned));
                                }
                                if (lower_id != 0) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("custom_");
                                    stringBuilder.append(dialog_id);
                                    if (preferences.getBoolean(stringBuilder.toString(), false)) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("popup_");
                                        stringBuilder.append(dialog_id);
                                        index = preferences.getInt(stringBuilder.toString(), 0);
                                    } else {
                                        index = 0;
                                    }
                                    popup = index;
                                    if (popup == 0) {
                                        popup = preferences.getInt(((int) dialog_id) < 0 ? "popupGroup" : "popupAll", 0);
                                    } else if (popup == 1) {
                                        popup = 3;
                                    } else if (popup == 2) {
                                        popup = 0;
                                    }
                                }
                                if (!(popup == 0 || messageObject.messageOwner.to_id.channel_id == 0 || messageObject.isMegagroup())) {
                                    popup = 0;
                                }
                                if (allowPinned) {
                                    if (popup != 0) {
                                        i = 0;
                                        popupArrayAdd.add(0, messageObject);
                                    } else {
                                        i = 0;
                                    }
                                    NotificationsController.this.delayedPushMessages.add(messageObject);
                                    NotificationsController.this.pushMessages.add(i, messageObject);
                                    NotificationsController.this.pushMessagesDict.put(mid, messageObject);
                                    long original_dialog_id2 = original_dialog_id;
                                    if (original_dialog_id2 != dialog_id) {
                                        longSparseArray = settingsCache;
                                        added2 = true;
                                        NotificationsController.this.pushDialogsOverrideMention.put(original_dialog_id2, Integer.valueOf(1));
                                        added = added2;
                                        a++;
                                        allowPinned = z;
                                        settingsCache = longSparseArray;
                                        z2 = true;
                                    }
                                }
                                longSparseArray = settingsCache;
                                added2 = true;
                                added = added2;
                                a++;
                                allowPinned = z;
                                settingsCache = longSparseArray;
                                z2 = true;
                            } else if (!z) {
                                NotificationsController.this.playInChatSound();
                            }
                        } else if (oldMessageObject.isFcmMessage()) {
                            NotificationsController.this.pushMessagesDict.put(mid, messageObject);
                            i = NotificationsController.this.pushMessages.indexOf(oldMessageObject);
                            if (i >= 0) {
                                NotificationsController.this.pushMessages.set(i, messageObject);
                            }
                        }
                        longSparseArray = settingsCache;
                        z = allowPinned;
                        a++;
                        allowPinned = z;
                        settingsCache = longSparseArray;
                        z2 = true;
                    }
                    z = allowPinned;
                    if (added) {
                        NotificationsController.this.notifyCheck = z2;
                    }
                    if (!(popupArrayAdd.isEmpty() || AndroidUtilities.needShowPasscode(false))) {
                        a = popup;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationsController.this.popupMessages.addAll(0, popupArrayAdd);
                                if (!ApplicationLoader.mainInterfacePaused && (ApplicationLoader.isScreenOn || SharedConfig.isWaitingForPasscodeEnter)) {
                                    return;
                                }
                                if (a == 3 || ((a == 1 && ApplicationLoader.isScreenOn) || (a == 2 && !ApplicationLoader.isScreenOn))) {
                                    Intent popupIntent = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
                                    popupIntent.setFlags(268763140);
                                    ApplicationLoader.applicationContext.startActivity(popupIntent);
                                }
                            }
                        });
                    }
                    if (added && z) {
                        boolean z3;
                        Integer newCount;
                        long dialog_id2 = ((MessageObject) arrayList.get(0)).getDialogId();
                        int old_unread_count = NotificationsController.this.total_unread_count;
                        index = NotificationsController.this.getNotifyOverride(preferences, dialog_id2);
                        if (NotificationsController.this.notifyCheck) {
                            Integer override = (Integer) NotificationsController.this.pushDialogsOverrideMention.get(dialog_id2);
                            if (override != null && override.intValue() == 1) {
                                NotificationsController.this.pushDialogsOverrideMention.put(dialog_id2, Integer.valueOf(0));
                                index = 1;
                            }
                        }
                        boolean canAddValue = index != 2 && ((preferences.getBoolean("EnableAll", true) && (((int) dialog_id2) >= 0 || preferences.getBoolean("EnableGroup", true))) || index != 0);
                        Integer currentCount = (Integer) NotificationsController.this.pushDialogs.get(dialog_id2);
                        if (currentCount != null) {
                            z3 = true;
                            newCount = currentCount.intValue() + 1;
                        } else {
                            z3 = true;
                            newCount = 1;
                        }
                        newCount = Integer.valueOf(newCount);
                        if (canAddValue) {
                            if (currentCount != null) {
                                NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count - currentCount.intValue();
                            }
                            NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + newCount.intValue();
                            NotificationsController.this.pushDialogs.put(dialog_id2, newCount);
                        }
                        if (old_unread_count != NotificationsController.this.total_unread_count) {
                            if (NotificationsController.this.notifyCheck) {
                                NotificationsController notificationsController = NotificationsController.this;
                                if (NotificationsController.this.lastOnlineFromOtherDevice <= ConnectionsManager.getInstance(NotificationsController.this.currentAccount).getCurrentTime()) {
                                    z3 = false;
                                }
                                notificationsController.scheduleNotificationDelay(z3);
                            } else {
                                NotificationsController.this.delayedPushMessages.clear();
                                NotificationsController.this.showOrUpdateNotification(NotificationsController.this.notifyCheck);
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(NotificationsController.this.currentAccount));
                                }
                            });
                        }
                        NotificationsController.this.notifyCheck = false;
                        if (NotificationsController.this.showBadgeNumber) {
                            NotificationsController.this.setBadge(NotificationsController.this.getTotalAllUnreadCount());
                        }
                    }
                }
            });
        }
    }

    public int getTotalUnreadCount() {
        return this.total_unread_count;
    }

    public void processDialogsUpdateRead(final LongSparseArray<Integer> dialogsToUpdate) {
        final ArrayList<MessageObject> popupArrayToRemove = new ArrayList();
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                int old_unread_count = NotificationsController.this.total_unread_count;
                SharedPreferences preferences = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount);
                boolean z = false;
                int b = 0;
                while (b < dialogsToUpdate.size()) {
                    long dialog_id = dialogsToUpdate.keyAt(b);
                    int notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                    if (NotificationsController.this.notifyCheck) {
                        Integer override = (Integer) NotificationsController.this.pushDialogsOverrideMention.get(dialog_id);
                        if (override != null && override.intValue() == 1) {
                            NotificationsController.this.pushDialogsOverrideMention.put(dialog_id, Integer.valueOf(z));
                            notifyOverride = 1;
                        }
                    }
                    boolean canAddValue = (notifyOverride == 2 || ((!preferences.getBoolean("EnableAll", true) || (((int) dialog_id) < 0 && !preferences.getBoolean("EnableGroup", true))) && notifyOverride == 0)) ? z : true;
                    Integer currentCount = (Integer) NotificationsController.this.pushDialogs.get(dialog_id);
                    Integer newCount = (Integer) dialogsToUpdate.get(dialog_id);
                    if (newCount.intValue() == 0) {
                        NotificationsController.this.smartNotificationsDialogs.remove(dialog_id);
                    }
                    if (newCount.intValue() < 0) {
                        if (currentCount == null) {
                            b++;
                            z = false;
                        } else {
                            newCount = Integer.valueOf(currentCount.intValue() + newCount.intValue());
                        }
                    }
                    if ((canAddValue || newCount.intValue() == 0) && currentCount != null) {
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count - currentCount.intValue();
                    }
                    long j;
                    if (newCount.intValue() == 0) {
                        NotificationsController.this.pushDialogs.remove(dialog_id);
                        NotificationsController.this.pushDialogsOverrideMention.remove(dialog_id);
                        int a = z;
                        while (a < NotificationsController.this.pushMessages.size()) {
                            MessageObject messageObject = (MessageObject) NotificationsController.this.pushMessages.get(a);
                            if (messageObject.getDialogId() == dialog_id) {
                                if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                    NotificationsController.this.personal_count = NotificationsController.this.personal_count - 1;
                                }
                                NotificationsController.this.pushMessages.remove(a);
                                a--;
                                NotificationsController.this.delayedPushMessages.remove(messageObject);
                                long mid = (long) messageObject.getId();
                                if (messageObject.messageOwner.to_id.channel_id != 0) {
                                    j = dialog_id;
                                    mid |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
                                } else {
                                    j = dialog_id;
                                }
                                NotificationsController.this.pushMessagesDict.remove(mid);
                                popupArrayToRemove.add(messageObject);
                            } else {
                                j = dialog_id;
                            }
                            a++;
                            int i = 1;
                            dialog_id = j;
                        }
                    } else {
                        j = dialog_id;
                        if (canAddValue) {
                            NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + newCount.intValue();
                            NotificationsController.this.pushDialogs.put(j, newCount);
                        }
                    }
                    b++;
                    z = false;
                }
                z = true;
                if (!popupArrayToRemove.isEmpty()) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            int size = popupArrayToRemove.size();
                            for (int a = 0; a < size; a++) {
                                NotificationsController.this.popupMessages.remove(popupArrayToRemove.get(a));
                            }
                        }
                    });
                }
                if (old_unread_count != NotificationsController.this.total_unread_count) {
                    if (NotificationsController.this.notifyCheck) {
                        NotificationsController notificationsController = NotificationsController.this;
                        if (NotificationsController.this.lastOnlineFromOtherDevice <= ConnectionsManager.getInstance(NotificationsController.this.currentAccount).getCurrentTime()) {
                            z = false;
                        }
                        notificationsController.scheduleNotificationDelay(z);
                    } else {
                        NotificationsController.this.delayedPushMessages.clear();
                        NotificationsController.this.showOrUpdateNotification(NotificationsController.this.notifyCheck);
                    }
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(NotificationsController.this.currentAccount));
                        }
                    });
                }
                NotificationsController.this.notifyCheck = false;
                if (NotificationsController.this.showBadgeNumber) {
                    NotificationsController.this.setBadge(NotificationsController.this.getTotalAllUnreadCount());
                }
            }
        });
    }

    public void processLoadedUnreadMessages(final LongSparseArray<Integer> dialogs, final ArrayList<Message> messages, ArrayList<User> users, ArrayList<Chat> chats, ArrayList<EncryptedChat> encryptedChats) {
        MessagesController.getInstance(this.currentAccount).putUsers(users, true);
        MessagesController.getInstance(this.currentAccount).putChats(chats, true);
        MessagesController.getInstance(this.currentAccount).putEncryptedChats(encryptedChats, true);
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                boolean z;
                NotificationsController.this.pushDialogs.clear();
                NotificationsController.this.pushMessages.clear();
                NotificationsController.this.pushMessagesDict.clear();
                boolean z2 = false;
                NotificationsController.this.total_unread_count = 0;
                NotificationsController.this.personal_count = 0;
                SharedPreferences preferences = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount);
                LongSparseArray<Boolean> settingsCache = new LongSparseArray();
                int i = 2;
                boolean z3 = true;
                if (messages != null) {
                    int a = 0;
                    while (a < messages.size()) {
                        Message message = (Message) messages.get(a);
                        long mid = (long) message.id;
                        if (message.to_id.channel_id != 0) {
                            mid |= ((long) message.to_id.channel_id) << 32;
                        }
                        if (NotificationsController.this.pushMessagesDict.indexOfKey(mid) < 0) {
                            MessageObject messageObject = new MessageObject(NotificationsController.this.currentAccount, message, z2);
                            if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                NotificationsController.this.personal_count = NotificationsController.this.personal_count + 1;
                            }
                            long dialog_id = messageObject.getDialogId();
                            long original_dialog_id = dialog_id;
                            if (messageObject.messageOwner.mentioned) {
                                dialog_id = (long) messageObject.messageOwner.from_id;
                            }
                            int index = settingsCache.indexOfKey(dialog_id);
                            if (index >= 0) {
                                z2 = ((Boolean) settingsCache.valueAt(index)).booleanValue();
                            } else {
                                int notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                                boolean value = (notifyOverride == i || ((!preferences.getBoolean("EnableAll", z3) || (((int) dialog_id) < 0 && !preferences.getBoolean("EnableGroup", z3))) && notifyOverride == 0)) ? false : z3;
                                settingsCache.put(dialog_id, Boolean.valueOf(value));
                                z2 = value;
                            }
                            if (z2) {
                                if (dialog_id != NotificationsController.this.opened_dialog_id || !ApplicationLoader.isScreenOn) {
                                    NotificationsController.this.pushMessagesDict.put(mid, messageObject);
                                    NotificationsController.this.pushMessages.add(0, messageObject);
                                    if (original_dialog_id != dialog_id) {
                                        NotificationsController.this.pushDialogsOverrideMention.put(original_dialog_id, Integer.valueOf(1));
                                    }
                                }
                            }
                        }
                        a++;
                        z2 = false;
                        i = 2;
                        z3 = true;
                    }
                }
                for (notifyOverride = 0; notifyOverride < dialogs.size(); notifyOverride++) {
                    boolean value2;
                    long dialog_id2 = dialogs.keyAt(notifyOverride);
                    int index2 = settingsCache.indexOfKey(dialog_id2);
                    if (index2 >= 0) {
                        value2 = ((Boolean) settingsCache.valueAt(index2)).booleanValue();
                    } else {
                        boolean value3;
                        int notifyOverride2 = NotificationsController.this.getNotifyOverride(preferences, dialog_id2);
                        Integer override = (Integer) NotificationsController.this.pushDialogsOverrideMention.get(dialog_id2);
                        if (override == null || override.intValue() != 1) {
                            z = false;
                        } else {
                            z = false;
                            NotificationsController.this.pushDialogsOverrideMention.put(dialog_id2, Integer.valueOf(0));
                            notifyOverride2 = 1;
                        }
                        if (notifyOverride2 != 2) {
                            if ((preferences.getBoolean("EnableAll", true) && (((int) dialog_id2) >= 0 || preferences.getBoolean("EnableGroup", true))) || notifyOverride2 != 0) {
                                value3 = true;
                                settingsCache.put(dialog_id2, Boolean.valueOf(value3));
                                value2 = value3;
                            }
                        }
                        value3 = z;
                        settingsCache.put(dialog_id2, Boolean.valueOf(value3));
                        value2 = value3;
                    }
                    if (value2) {
                        int count = ((Integer) dialogs.valueAt(notifyOverride)).intValue();
                        NotificationsController.this.pushDialogs.put(dialog_id2, Integer.valueOf(count));
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + count;
                    }
                }
                z = false;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        if (NotificationsController.this.total_unread_count == 0) {
                            NotificationsController.this.popupMessages.clear();
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
                        }
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(NotificationsController.this.currentAccount));
                    }
                });
                NotificationsController notificationsController = NotificationsController.this;
                if (SystemClock.uptimeMillis() / 1000 < 60) {
                    z = true;
                }
                notificationsController.showOrUpdateNotification(z);
                if (NotificationsController.this.showBadgeNumber) {
                    NotificationsController.this.setBadge(NotificationsController.this.getTotalAllUnreadCount());
                }
            }
        });
    }

    private int getTotalAllUnreadCount() {
        int count = 0;
        for (int a = 0; a < 3; a++) {
            if (UserConfig.getInstance(a).isClientActivated()) {
                NotificationsController controller = getInstance(a);
                if (controller.showBadgeNumber) {
                    count += controller.total_unread_count;
                }
            }
        }
        return count;
    }

    public void setBadgeEnabled(boolean enabled) {
        this.showBadgeNumber = enabled;
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                NotificationsController.this.setBadge(NotificationsController.this.getTotalAllUnreadCount());
            }
        });
    }

    private void setBadge(int count) {
        if (this.lastBadgeCount != count) {
            this.lastBadgeCount = count;
            NotificationBadge.applyCount(count);
        }
    }

    private String getShortStringForMessage(MessageObject msg) {
        if (!(msg.isMediaEmpty() || TextUtils.isEmpty(msg.messageOwner.message))) {
            StringBuilder stringBuilder;
            if (msg.messageOwner.media instanceof TL_messageMediaPhoto) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("🖼 ");
                stringBuilder.append(msg.messageOwner.message);
                return stringBuilder.toString();
            } else if (msg.isVideo()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("📹 ");
                stringBuilder.append(msg.messageOwner.message);
                return stringBuilder.toString();
            } else if (msg.messageOwner.media instanceof TL_messageMediaDocument) {
                if (msg.isGif()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("🎬 ");
                    stringBuilder.append(msg.messageOwner.message);
                    return stringBuilder.toString();
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("📎 ");
                stringBuilder.append(msg.messageOwner.message);
                return stringBuilder.toString();
            }
        }
        return msg.messageText.toString();
    }

    private void scheduleNotificationRepeat() {
        try {
            Intent intent = new Intent(ApplicationLoader.applicationContext, NotificationRepeat.class);
            intent.putExtra("currentAccount", this.currentAccount);
            PendingIntent pintent = PendingIntent.getService(ApplicationLoader.applicationContext, 0, intent, 0);
            int minutes = MessagesController.getNotificationsSettings(this.currentAccount).getInt("repeat_messages", 60);
            if (minutes <= 0 || this.personal_count <= 0) {
                this.alarmManager.cancel(pintent);
            } else {
                this.alarmManager.set(2, SystemClock.elapsedRealtime() + ((long) ((minutes * 60) * 1000)), pintent);
            }
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private boolean isPersonalMessage(MessageObject messageObject) {
        return messageObject.messageOwner.to_id != null && messageObject.messageOwner.to_id.chat_id == 0 && messageObject.messageOwner.to_id.channel_id == 0 && (messageObject.messageOwner.action == null || (messageObject.messageOwner.action instanceof TL_messageActionEmpty));
    }

    private int getNotifyOverride(SharedPreferences preferences, long dialog_id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("notify2_");
        stringBuilder.append(dialog_id);
        int notifyOverride = preferences.getInt(stringBuilder.toString(), 0);
        if (notifyOverride != 3) {
            return notifyOverride;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("notifyuntil_");
        stringBuilder2.append(dialog_id);
        if (preferences.getInt(stringBuilder2.toString(), 0) >= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
            return 2;
        }
        return notifyOverride;
    }

    private void dismissNotification() {
        int a = 0;
        try {
            this.lastNotificationIsNoData = false;
            notificationManager.cancel(this.notificationId);
            this.pushMessages.clear();
            this.pushMessagesDict.clear();
            this.lastWearNotifiedMessageId.clear();
            while (a < this.wearNotificationsIds.size()) {
                notificationManager.cancel(((Integer) this.wearNotificationsIds.valueAt(a)).intValue());
                a++;
            }
            this.wearNotificationsIds.clear();
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
                }
            });
            if (WearDataLayerListenerService.isWatchConnected()) {
                try {
                    JSONObject o = new JSONObject();
                    o.put(TtmlNode.ATTR_ID, UserConfig.getInstance(this.currentAccount).getClientUserId());
                    o.put("cancel_all", true);
                    WearDataLayerListenerService.sendMessageToWatch("/notify", o.toString().getBytes(C.UTF8_NAME), "remote_notifications");
                } catch (JSONException e) {
                }
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
    }

    private void playInChatSound() {
        if (this.inChatSoundEnabled) {
            if (!MediaController.getInstance().isRecordingAudio()) {
                try {
                    if (audioManager.getRingerMode() != 0) {
                        try {
                            if (getNotifyOverride(MessagesController.getNotificationsSettings(this.currentAccount), this.opened_dialog_id) != 2) {
                                notificationsQueue.postRunnable(new Runnable() {
                                    public void run() {
                                        if (Math.abs(System.currentTimeMillis() - NotificationsController.this.lastSoundPlay) > 500) {
                                            try {
                                                if (NotificationsController.this.soundPool == null) {
                                                    NotificationsController.this.soundPool = new SoundPool(3, 1, 0);
                                                    NotificationsController.this.soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
                                                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                                                            if (status == 0) {
                                                                try {
                                                                    soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
                                                                } catch (Throwable e) {
                                                                    FileLog.e(e);
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                                if (NotificationsController.this.soundIn == 0 && !NotificationsController.this.soundInLoaded) {
                                                    NotificationsController.this.soundInLoaded = true;
                                                    NotificationsController.this.soundIn = NotificationsController.this.soundPool.load(ApplicationLoader.applicationContext, R.raw.sound_in, 1);
                                                }
                                                if (NotificationsController.this.soundIn != 0) {
                                                    try {
                                                        NotificationsController.this.soundPool.play(NotificationsController.this.soundIn, 1.0f, 1.0f, 1, 0, 1.0f);
                                                    } catch (Throwable e) {
                                                        FileLog.e(e);
                                                    }
                                                }
                                            } catch (Throwable e2) {
                                                FileLog.e(e2);
                                            }
                                        }
                                    }
                                });
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        }
    }

    private void scheduleNotificationDelay(boolean onlineReason) {
        try {
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("delay notification start, onlineReason = ");
                stringBuilder.append(onlineReason);
                FileLog.d(stringBuilder.toString());
            }
            this.notificationDelayWakelock.acquire(10000);
            AndroidUtilities.cancelRunOnUIThread(this.notificationDelayRunnable);
            AndroidUtilities.runOnUIThread(this.notificationDelayRunnable, (long) (onlineReason ? 3000 : 1000));
        } catch (Throwable e) {
            FileLog.e(e);
            showOrUpdateNotification(this.notifyCheck);
        }
    }

    protected void repeatNotificationMaybe() {
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                int hour = Calendar.getInstance().get(11);
                if (hour < 11 || hour > 22) {
                    NotificationsController.this.scheduleNotificationRepeat();
                    return;
                }
                NotificationsController.notificationManager.cancel(NotificationsController.this.notificationId);
                NotificationsController.this.showOrUpdateNotification(true);
            }
        });
    }

    private boolean isEmptyVibration(long[] pattern) {
        if (pattern != null) {
            if (pattern.length != 0) {
                for (int a = 0; a < pattern.length; a++) {
                    if (pattern[0] != 0) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @TargetApi(26)
    private String validateChannelId(long dialogId, String name, long[] vibrationPattern, int ledColor, Uri sound, int importance, long[] configVibrationPattern, Uri configSound, int configImportance) {
        long j = dialogId;
        long[] jArr = vibrationPattern;
        int i = ledColor;
        Uri uri = sound;
        int i2 = importance;
        SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
        String key = new StringBuilder();
        key.append("org.telegram.key");
        key.append(j);
        key = key.toString();
        String channelId = preferences.getString(key, null);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(key);
        stringBuilder.append("_s");
        String settings = preferences.getString(stringBuilder.toString(), null);
        StringBuilder newSettings = new StringBuilder();
        int a = 0;
        while (a < jArr.length) {
            newSettings.append(jArr[a]);
            a++;
            j = dialogId;
        }
        newSettings.append(i);
        if (uri != null) {
            newSettings.append(sound.toString());
        }
        newSettings.append(i2);
        String newSettingsHash = Utilities.MD5(newSettings.toString());
        if (!(channelId == null || settings.equals(newSettingsHash))) {
            if (false) {
                Editor putString = preferences.edit().putString(key, channelId);
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(key);
                stringBuilder2.append("_s");
                putString.putString(stringBuilder2.toString(), newSettingsHash).commit();
            } else {
                systemNotificationManager.deleteNotificationChannel(channelId);
                channelId = null;
            }
        }
        if (channelId == null) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(r0.currentAccount);
            stringBuilder3.append("channel");
            stringBuilder3.append(dialogId);
            stringBuilder3.append("_");
            stringBuilder3.append(Utilities.random.nextLong());
            channelId = stringBuilder3.toString();
            NotificationChannel notificationChannel = new NotificationChannel(channelId, name, i2);
            if (i != 0) {
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(i);
            }
            if (isEmptyVibration(jArr)) {
                notificationChannel.enableVibration(false);
            } else {
                notificationChannel.enableVibration(true);
                if (jArr != null && jArr.length > 0) {
                    notificationChannel.setVibrationPattern(jArr);
                }
            }
            if (uri != null) {
                Builder builder = new Builder();
                builder.setContentType(4);
                builder.setUsage(5);
                notificationChannel.setSound(uri, builder.build());
            } else {
                notificationChannel.setSound(null, null);
            }
            systemNotificationManager.createNotificationChannel(notificationChannel);
            Editor putString2 = preferences.edit().putString(key, channelId);
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(key);
            stringBuilder4.append("_s");
            putString2.putString(stringBuilder4.toString(), newSettingsHash).commit();
            return channelId;
        }
        String str = name;
        long j2 = dialogId;
        return channelId;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showOrUpdateNotification(boolean r104) {
        /*
        r103 = this;
        r12 = r103;
        r13 = r104;
        r1 = r12.currentAccount;
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
        r1 = r1.isClientActivated();
        if (r1 == 0) goto L_0x0c2f;
    L_0x0010:
        r1 = r12.pushMessages;
        r1 = r1.isEmpty();
        if (r1 == 0) goto L_0x001a;
    L_0x0018:
        goto L_0x0c2f;
    L_0x001a:
        r1 = r12.currentAccount;	 Catch:{ Exception -> 0x0c29 }
        r1 = org.telegram.tgnet.ConnectionsManager.getInstance(r1);	 Catch:{ Exception -> 0x0c29 }
        r1.resumeNetworkMaybe();	 Catch:{ Exception -> 0x0c29 }
        r1 = r12.pushMessages;	 Catch:{ Exception -> 0x0c29 }
        r14 = 0;
        r1 = r1.get(r14);	 Catch:{ Exception -> 0x0c29 }
        r1 = (org.telegram.messenger.MessageObject) r1;	 Catch:{ Exception -> 0x0c29 }
        r15 = r1;
        r1 = r12.currentAccount;	 Catch:{ Exception -> 0x0c29 }
        r1 = org.telegram.messenger.MessagesController.getNotificationsSettings(r1);	 Catch:{ Exception -> 0x0c29 }
        r11 = r1;
        r1 = "dismissDate";
        r1 = r11.getInt(r1, r14);	 Catch:{ Exception -> 0x0c29 }
        r10 = r1;
        r1 = r15.messageOwner;	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.date;	 Catch:{ Exception -> 0x0c29 }
        if (r1 > r10) goto L_0x0045;
    L_0x0041:
        r103.dismissNotification();	 Catch:{ Exception -> 0x0c29 }
        return;
    L_0x0045:
        r1 = r15.getDialogId();	 Catch:{ Exception -> 0x0c29 }
        r8 = r1;
        r3 = r15.messageOwner;	 Catch:{ Exception -> 0x0c29 }
        r3 = r3.mentioned;	 Catch:{ Exception -> 0x0c29 }
        if (r3 == 0) goto L_0x0056;
    L_0x0051:
        r3 = r15.messageOwner;	 Catch:{ Exception -> 0x0c29 }
        r3 = r3.from_id;	 Catch:{ Exception -> 0x0c29 }
        r1 = (long) r3;	 Catch:{ Exception -> 0x0c29 }
    L_0x0056:
        r6 = r1;
        r1 = r15.getId();	 Catch:{ Exception -> 0x0c29 }
        r16 = r1;
        r1 = r15.messageOwner;	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.to_id;	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.chat_id;	 Catch:{ Exception -> 0x0c29 }
        if (r1 == 0) goto L_0x006c;
    L_0x0065:
        r1 = r15.messageOwner;	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.to_id;	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.chat_id;	 Catch:{ Exception -> 0x0c29 }
        goto L_0x0072;
    L_0x006c:
        r1 = r15.messageOwner;	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.to_id;	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.channel_id;	 Catch:{ Exception -> 0x0c29 }
    L_0x0072:
        r5 = r1;
        r1 = r15.messageOwner;	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.to_id;	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.user_id;	 Catch:{ Exception -> 0x0c29 }
        if (r1 != 0) goto L_0x0081;
    L_0x007b:
        r2 = r15.messageOwner;	 Catch:{ Exception -> 0x0c29 }
        r2 = r2.from_id;	 Catch:{ Exception -> 0x0c29 }
        r1 = r2;
        goto L_0x0094;
    L_0x0081:
        r2 = r12.currentAccount;	 Catch:{ Exception -> 0x0c29 }
        r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x0c29 }
        r2 = r2.getClientUserId();	 Catch:{ Exception -> 0x0c29 }
        if (r1 != r2) goto L_0x0093;
    L_0x008d:
        r2 = r15.messageOwner;	 Catch:{ Exception -> 0x0c29 }
        r2 = r2.from_id;	 Catch:{ Exception -> 0x0c29 }
        r1 = r2;
        goto L_0x0094;
    L_0x0093:
        r2 = r1;
    L_0x0094:
        r1 = r12.currentAccount;	 Catch:{ Exception -> 0x0c29 }
        r1 = org.telegram.messenger.MessagesController.getInstance(r1);	 Catch:{ Exception -> 0x0c29 }
        r3 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x0c29 }
        r1 = r1.getUser(r3);	 Catch:{ Exception -> 0x0c29 }
        r3 = r1;
        r1 = 0;
        if (r5 == 0) goto L_0x00b5;
    L_0x00a6:
        r4 = r12.currentAccount;	 Catch:{ Exception -> 0x0c29 }
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);	 Catch:{ Exception -> 0x0c29 }
        r14 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x0c29 }
        r4 = r4.getChat(r14);	 Catch:{ Exception -> 0x0c29 }
        r1 = r4;
    L_0x00b5:
        r14 = r1;
        r1 = 0;
        r4 = 0;
        r17 = 0;
        r18 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
        r19 = 0;
        r20 = r12.getNotifyOverride(r11, r6);	 Catch:{ Exception -> 0x0c29 }
        r21 = r20;
        r22 = r1;
        if (r13 == 0) goto L_0x00ef;
    L_0x00c9:
        r25 = r4;
        r1 = r21;
        r4 = 2;
        if (r1 == r4) goto L_0x00f3;
    L_0x00d0:
        r4 = "EnableAll";
        r13 = 1;
        r4 = r11.getBoolean(r4, r13);	 Catch:{ Exception -> 0x00e9 }
        if (r4 == 0) goto L_0x00e3;
    L_0x00d9:
        if (r5 == 0) goto L_0x00e6;
    L_0x00db:
        r4 = "EnableGroup";
        r4 = r11.getBoolean(r4, r13);	 Catch:{ Exception -> 0x00e9 }
        if (r4 != 0) goto L_0x00e6;
    L_0x00e3:
        if (r1 != 0) goto L_0x00e6;
    L_0x00e5:
        goto L_0x00f3;
    L_0x00e6:
        r4 = r25;
        goto L_0x00f4;
    L_0x00e9:
        r0 = move-exception;
        r1 = r0;
        r13 = r104;
        goto L_0x0c2b;
    L_0x00ef:
        r25 = r4;
        r1 = r21;
    L_0x00f3:
        r4 = 1;
    L_0x00f4:
        r20 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r4 != 0) goto L_0x01be;
    L_0x00f8:
        r13 = (r8 > r6 ? 1 : (r8 == r6 ? 0 : -1));
        if (r13 != 0) goto L_0x01be;
    L_0x00fc:
        if (r14 == 0) goto L_0x01be;
    L_0x00fe:
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r13.<init>();	 Catch:{ Exception -> 0x00e9 }
        r27 = r1;
        r1 = "custom_";
        r13.append(r1);	 Catch:{ Exception -> 0x00e9 }
        r13.append(r8);	 Catch:{ Exception -> 0x00e9 }
        r1 = r13.toString();	 Catch:{ Exception -> 0x00e9 }
        r13 = 0;
        r1 = r11.getBoolean(r1, r13);	 Catch:{ Exception -> 0x00e9 }
        if (r1 == 0) goto L_0x014b;
    L_0x0118:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r1.<init>();	 Catch:{ Exception -> 0x00e9 }
        r13 = "smart_max_count_";
        r1.append(r13);	 Catch:{ Exception -> 0x00e9 }
        r1.append(r8);	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00e9 }
        r13 = 2;
        r1 = r11.getInt(r1, r13);	 Catch:{ Exception -> 0x00e9 }
        r13 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r13.<init>();	 Catch:{ Exception -> 0x00e9 }
        r29 = r1;
        r1 = "smart_delay_";
        r13.append(r1);	 Catch:{ Exception -> 0x00e9 }
        r13.append(r8);	 Catch:{ Exception -> 0x00e9 }
        r1 = r13.toString();	 Catch:{ Exception -> 0x00e9 }
        r13 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        r1 = r11.getInt(r1, r13);	 Catch:{ Exception -> 0x00e9 }
        r13 = r1;
        r1 = r29;
        goto L_0x014f;
    L_0x014b:
        r13 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        r1 = 2;
    L_0x014f:
        if (r1 == 0) goto L_0x01b5;
    L_0x0151:
        r30 = r4;
        r4 = r12.smartNotificationsDialogs;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.get(r8);	 Catch:{ Exception -> 0x00e9 }
        r4 = (android.graphics.Point) r4;	 Catch:{ Exception -> 0x00e9 }
        if (r4 != 0) goto L_0x0177;
    L_0x015d:
        r31 = r6;
        r6 = new android.graphics.Point;	 Catch:{ Exception -> 0x00e9 }
        r28 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00e9 }
        r34 = r14;
        r33 = r15;
        r14 = r28 / r20;
        r7 = (int) r14;	 Catch:{ Exception -> 0x00e9 }
        r14 = 1;
        r6.<init>(r14, r7);	 Catch:{ Exception -> 0x00e9 }
        r4 = r6;
        r6 = r12.smartNotificationsDialogs;	 Catch:{ Exception -> 0x00e9 }
        r6.put(r8, r4);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x01c8;
    L_0x0177:
        r31 = r6;
        r34 = r14;
        r33 = r15;
        r6 = r4.y;	 Catch:{ Exception -> 0x00e9 }
        r7 = r6 + r13;
        r14 = (long) r7;	 Catch:{ Exception -> 0x00e9 }
        r28 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00e9 }
        r28 = r28 / r20;
        r7 = (r14 > r28 ? 1 : (r14 == r28 ? 0 : -1));
        if (r7 >= 0) goto L_0x0198;
    L_0x018c:
        r14 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00e9 }
        r14 = r14 / r20;
        r7 = (int) r14;	 Catch:{ Exception -> 0x00e9 }
        r14 = 1;
        r4.set(r14, r7);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x01c8;
    L_0x0198:
        r7 = r4.x;	 Catch:{ Exception -> 0x00e9 }
        if (r7 >= r1) goto L_0x01ad;
    L_0x019c:
        r14 = r7 + 1;
        r28 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x00e9 }
        r35 = r6;
        r36 = r7;
        r6 = r28 / r20;
        r6 = (int) r6;	 Catch:{ Exception -> 0x00e9 }
        r4.set(r14, r6);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x01c8;
    L_0x01ad:
        r35 = r6;
        r36 = r7;
        r4 = 1;
        r30 = r4;
        goto L_0x01c8;
    L_0x01b5:
        r30 = r4;
        r31 = r6;
        r34 = r14;
        r33 = r15;
        goto L_0x01c8;
    L_0x01be:
        r27 = r1;
        r30 = r4;
        r31 = r6;
        r34 = r14;
        r33 = r15;
    L_0x01c8:
        r1 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x0c25 }
        r1 = r1.getPath();	 Catch:{ Exception -> 0x0c25 }
        r13 = r1;
        r1 = "EnableInAppSounds";
        r4 = 1;
        r1 = r11.getBoolean(r1, r4);	 Catch:{ Exception -> 0x0c25 }
        r14 = r1;
        r1 = "EnableInAppVibrate";
        r1 = r11.getBoolean(r1, r4);	 Catch:{ Exception -> 0x0c25 }
        r15 = r1;
        r1 = "EnableInAppPreview";
        r1 = r11.getBoolean(r1, r4);	 Catch:{ Exception -> 0x0c25 }
        r25 = r1;
        r1 = "EnableInAppPriority";
        r4 = 0;
        r1 = r11.getBoolean(r1, r4);	 Catch:{ Exception -> 0x0c25 }
        r28 = r1;
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0c25 }
        r1.<init>();	 Catch:{ Exception -> 0x0c25 }
        r4 = "custom_";
        r1.append(r4);	 Catch:{ Exception -> 0x0c25 }
        r1.append(r8);	 Catch:{ Exception -> 0x0c25 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x0c25 }
        r4 = 0;
        r1 = r11.getBoolean(r1, r4);	 Catch:{ Exception -> 0x0c25 }
        r29 = r1;
        r7 = 3;
        if (r1 == 0) goto L_0x024e;
    L_0x020a:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r1.<init>();	 Catch:{ Exception -> 0x00e9 }
        r4 = "vibrate_";
        r1.append(r4);	 Catch:{ Exception -> 0x00e9 }
        r1.append(r8);	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00e9 }
        r4 = 0;
        r1 = r11.getInt(r1, r4);	 Catch:{ Exception -> 0x00e9 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r4.<init>();	 Catch:{ Exception -> 0x00e9 }
        r6 = "priority_";
        r4.append(r6);	 Catch:{ Exception -> 0x00e9 }
        r4.append(r8);	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x00e9 }
        r4 = r11.getInt(r4, r7);	 Catch:{ Exception -> 0x00e9 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r6.<init>();	 Catch:{ Exception -> 0x00e9 }
        r7 = "sound_path_";
        r6.append(r7);	 Catch:{ Exception -> 0x00e9 }
        r6.append(r8);	 Catch:{ Exception -> 0x00e9 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x00e9 }
        r7 = 0;
        r6 = r11.getString(r6, r7);	 Catch:{ Exception -> 0x00e9 }
        r7 = r1;
        r1 = r4;
        goto L_0x0253;
    L_0x024e:
        r1 = 0;
        r4 = 3;
        r7 = r1;
        r1 = r4;
        r6 = 0;
    L_0x0253:
        r4 = r6;
        r6 = 0;
        r39 = r6;
        if (r5 == 0) goto L_0x0290;
    L_0x0259:
        if (r4 == 0) goto L_0x0263;
    L_0x025b:
        r35 = r4.equals(r13);	 Catch:{ Exception -> 0x00e9 }
        if (r35 == 0) goto L_0x0263;
    L_0x0261:
        r4 = 0;
        goto L_0x026c;
    L_0x0263:
        if (r4 != 0) goto L_0x026c;
    L_0x0265:
        r6 = "GroupSoundPath";
        r6 = r11.getString(r6, r13);	 Catch:{ Exception -> 0x00e9 }
        r4 = r6;
    L_0x026c:
        r6 = "vibrate_group";
        r41 = r4;
        r4 = 0;
        r6 = r11.getInt(r6, r4);	 Catch:{ Exception -> 0x00e9 }
        r17 = r6;
        r4 = "priority_group";
        r6 = 1;
        r4 = r11.getInt(r4, r6);	 Catch:{ Exception -> 0x00e9 }
        r19 = r4;
        r4 = "GroupLed";
        r6 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
        r4 = r11.getInt(r4, r6);	 Catch:{ Exception -> 0x00e9 }
        r18 = r4;
        r6 = r17;
        r4 = r41;
        goto L_0x02cb;
    L_0x0290:
        if (r2 == 0) goto L_0x02c9;
    L_0x0292:
        if (r4 == 0) goto L_0x029c;
    L_0x0294:
        r6 = r4.equals(r13);	 Catch:{ Exception -> 0x00e9 }
        if (r6 == 0) goto L_0x029c;
    L_0x029a:
        r4 = 0;
        goto L_0x02a5;
    L_0x029c:
        if (r4 != 0) goto L_0x02a5;
    L_0x029e:
        r6 = "GlobalSoundPath";
        r6 = r11.getString(r6, r13);	 Catch:{ Exception -> 0x00e9 }
        r4 = r6;
    L_0x02a5:
        r6 = "vibrate_messages";
        r42 = r4;
        r4 = 0;
        r6 = r11.getInt(r6, r4);	 Catch:{ Exception -> 0x00e9 }
        r17 = r6;
        r4 = "priority_group";
        r6 = 1;
        r4 = r11.getInt(r4, r6);	 Catch:{ Exception -> 0x00e9 }
        r19 = r4;
        r4 = "MessagesLed";
        r6 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
        r4 = r11.getInt(r4, r6);	 Catch:{ Exception -> 0x00e9 }
        r18 = r4;
        r6 = r17;
        r4 = r42;
        goto L_0x02cb;
    L_0x02c9:
        r6 = r17;
    L_0x02cb:
        if (r29 == 0) goto L_0x0301;
    L_0x02cd:
        r43 = r4;
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r4.<init>();	 Catch:{ Exception -> 0x00e9 }
        r44 = r10;
        r10 = "color_";
        r4.append(r10);	 Catch:{ Exception -> 0x00e9 }
        r4.append(r8);	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x00e9 }
        r4 = r11.contains(r4);	 Catch:{ Exception -> 0x00e9 }
        if (r4 == 0) goto L_0x0305;
    L_0x02e8:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r4.<init>();	 Catch:{ Exception -> 0x00e9 }
        r10 = "color_";
        r4.append(r10);	 Catch:{ Exception -> 0x00e9 }
        r4.append(r8);	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x00e9 }
        r10 = 0;
        r4 = r11.getInt(r4, r10);	 Catch:{ Exception -> 0x00e9 }
        r18 = r4;
        goto L_0x0305;
    L_0x0301:
        r43 = r4;
        r44 = r10;
    L_0x0305:
        r4 = 3;
        if (r1 == r4) goto L_0x030a;
    L_0x0308:
        r19 = r1;
    L_0x030a:
        r4 = r19;
        r10 = 4;
        if (r6 != r10) goto L_0x0313;
    L_0x030f:
        r17 = 1;
        r6 = 0;
        goto L_0x0315;
    L_0x0313:
        r17 = r39;
    L_0x0315:
        r10 = 2;
        if (r6 != r10) goto L_0x031e;
    L_0x0318:
        r10 = 1;
        if (r7 == r10) goto L_0x0328;
    L_0x031b:
        r10 = 3;
        if (r7 == r10) goto L_0x0328;
    L_0x031e:
        r10 = 2;
        if (r6 == r10) goto L_0x0323;
    L_0x0321:
        if (r7 == r10) goto L_0x0328;
    L_0x0323:
        if (r7 == 0) goto L_0x0329;
    L_0x0325:
        r10 = 4;
        if (r7 == r10) goto L_0x0329;
    L_0x0328:
        r6 = r7;
    L_0x0329:
        r10 = org.telegram.messenger.ApplicationLoader.mainInterfacePaused;	 Catch:{ Exception -> 0x0c25 }
        if (r10 != 0) goto L_0x0341;
    L_0x032d:
        if (r14 != 0) goto L_0x0332;
    L_0x032f:
        r10 = 0;
        r43 = r10;
    L_0x0332:
        if (r15 != 0) goto L_0x0335;
    L_0x0334:
        r6 = 2;
    L_0x0335:
        if (r28 != 0) goto L_0x033c;
    L_0x0337:
        r4 = 0;
        r47 = r43;
        r10 = 2;
        goto L_0x0344;
    L_0x033c:
        r10 = 2;
        if (r4 != r10) goto L_0x0342;
    L_0x033f:
        r4 = 1;
        goto L_0x0342;
    L_0x0341:
        r10 = 2;
    L_0x0342:
        r47 = r43;
    L_0x0344:
        if (r17 == 0) goto L_0x0363;
    L_0x0346:
        if (r6 == r10) goto L_0x0363;
    L_0x0348:
        r10 = audioManager;	 Catch:{ Exception -> 0x035b }
        r10 = r10.getRingerMode();	 Catch:{ Exception -> 0x035b }
        if (r10 == 0) goto L_0x0358;
    L_0x0350:
        r48 = r1;
        r1 = 1;
        if (r10 == r1) goto L_0x035a;
    L_0x0355:
        r1 = 2;
        r6 = r1;
        goto L_0x035a;
    L_0x0358:
        r48 = r1;
    L_0x035a:
        goto L_0x0365;
    L_0x035b:
        r0 = move-exception;
        r48 = r1;
        r1 = r0;
        org.telegram.messenger.FileLog.e(r1);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x0365;
    L_0x0363:
        r48 = r1;
    L_0x0365:
        r1 = 0;
        r10 = 0;
        r19 = 0;
        r49 = r1;
        r1 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0c25 }
        r50 = r7;
        r7 = 26;
        if (r1 < r7) goto L_0x03db;
    L_0x0373:
        r1 = 2;
        if (r6 != r1) goto L_0x037d;
    L_0x0376:
        r7 = new long[r1];	 Catch:{ Exception -> 0x00e9 }
        r7 = {0, 0};	 Catch:{ Exception -> 0x00e9 }
        r10 = r7;
        goto L_0x039d;
    L_0x037d:
        r1 = 1;
        if (r6 != r1) goto L_0x0388;
    L_0x0380:
        r1 = 4;
        r7 = new long[r1];	 Catch:{ Exception -> 0x00e9 }
        r7 = {0, 100, 0, 100};	 Catch:{ Exception -> 0x00e9 }
        r10 = r7;
        goto L_0x039d;
    L_0x0388:
        if (r6 == 0) goto L_0x0399;
    L_0x038a:
        r1 = 4;
        if (r6 != r1) goto L_0x038e;
    L_0x038d:
        goto L_0x0399;
    L_0x038e:
        r1 = 3;
        if (r6 != r1) goto L_0x039d;
    L_0x0391:
        r1 = 2;
        r7 = new long[r1];	 Catch:{ Exception -> 0x00e9 }
        r7 = {0, 1000};	 Catch:{ Exception -> 0x00e9 }
        r10 = r7;
        goto L_0x039d;
    L_0x0399:
        r1 = 0;
        r7 = new long[r1];	 Catch:{ Exception -> 0x00e9 }
        r10 = r7;
    L_0x039d:
        r1 = r47;
        if (r1 == 0) goto L_0x03b7;
    L_0x03a1:
        r7 = "NoSound";
        r7 = r1.equals(r7);	 Catch:{ Exception -> 0x00e9 }
        if (r7 != 0) goto L_0x03b7;
    L_0x03a9:
        r7 = r1.equals(r13);	 Catch:{ Exception -> 0x00e9 }
        if (r7 == 0) goto L_0x03b2;
    L_0x03af:
        r7 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x00e9 }
    L_0x03b1:
        goto L_0x03b9;
    L_0x03b2:
        r7 = android.net.Uri.parse(r1);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x03b1;
    L_0x03b7:
        r7 = r49;
    L_0x03b9:
        if (r4 != 0) goto L_0x03c4;
    L_0x03bb:
        r19 = 3;
        r51 = r1;
    L_0x03bf:
        r49 = r7;
        r35 = r10;
        goto L_0x03df;
    L_0x03c4:
        r51 = r1;
        r1 = 1;
        if (r4 == r1) goto L_0x03d7;
    L_0x03c9:
        r1 = 2;
        if (r4 != r1) goto L_0x03cd;
    L_0x03cc:
        goto L_0x03d7;
    L_0x03cd:
        r1 = 4;
        if (r4 != r1) goto L_0x03d2;
    L_0x03d0:
        r1 = 1;
        goto L_0x03d8;
    L_0x03d2:
        r1 = 5;
        if (r4 != r1) goto L_0x03bf;
    L_0x03d5:
        r1 = 2;
        goto L_0x03d8;
    L_0x03d7:
        r1 = 4;
    L_0x03d8:
        r19 = r1;
        goto L_0x03bf;
    L_0x03db:
        r51 = r47;
        r35 = r10;
    L_0x03df:
        if (r30 == 0) goto L_0x03eb;
    L_0x03e1:
        r6 = 0;
        r4 = 0;
        r1 = 0;
        r47 = 0;
        r7 = r1;
        r10 = r6;
        r1 = r47;
        goto L_0x03f0;
    L_0x03eb:
        r10 = r6;
        r7 = r18;
        r1 = r51;
    L_0x03f0:
        r6 = r4;
        r4 = new android.content.Intent;	 Catch:{ Exception -> 0x0c25 }
        r52 = r11;
        r11 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0c25 }
        r53 = r14;
        r14 = org.telegram.ui.LaunchActivity.class;
        r4.<init>(r11, r14);	 Catch:{ Exception -> 0x0c25 }
        r14 = r4;
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0c25 }
        r4.<init>();	 Catch:{ Exception -> 0x0c25 }
        r11 = "com.tmessages.openchat";
        r4.append(r11);	 Catch:{ Exception -> 0x0c25 }
        r54 = r10;
        r10 = java.lang.Math.random();	 Catch:{ Exception -> 0x0c25 }
        r4.append(r10);	 Catch:{ Exception -> 0x0c25 }
        r10 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r4.append(r10);	 Catch:{ Exception -> 0x0c25 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0c25 }
        r14.setAction(r4);	 Catch:{ Exception -> 0x0c25 }
        r4 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r14.setFlags(r4);	 Catch:{ Exception -> 0x0c25 }
        r4 = (int) r8;
        if (r4 == 0) goto L_0x04ed;
    L_0x0428:
        r4 = r12.pushDialogs;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.size();	 Catch:{ Exception -> 0x00e9 }
        r11 = 1;
        if (r4 != r11) goto L_0x0440;
    L_0x0431:
        if (r5 == 0) goto L_0x0439;
    L_0x0433:
        r4 = "chatId";
        r14.putExtra(r4, r5);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x0440;
    L_0x0439:
        if (r2 == 0) goto L_0x0440;
    L_0x043b:
        r4 = "userId";
        r14.putExtra(r4, r2);	 Catch:{ Exception -> 0x00e9 }
    L_0x0440:
        r4 = 0;
        r11 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r4);	 Catch:{ Exception -> 0x00e9 }
        if (r11 != 0) goto L_0x04e0;
    L_0x0447:
        r4 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;	 Catch:{ Exception -> 0x00e9 }
        if (r4 == 0) goto L_0x0453;
    L_0x044b:
        r57 = r1;
        r56 = r2;
        r11 = r34;
        goto L_0x04e6;
    L_0x0453:
        r4 = r12.pushDialogs;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.size();	 Catch:{ Exception -> 0x00e9 }
        r11 = 1;
        if (r4 != r11) goto L_0x04d5;
    L_0x045c:
        r39 = 0;
        if (r34 == 0) goto L_0x04a3;
    L_0x0460:
        r11 = r34;
        r4 = r11.photo;	 Catch:{ Exception -> 0x00e9 }
        if (r4 == 0) goto L_0x0499;
    L_0x0466:
        r4 = r11.photo;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.photo_small;	 Catch:{ Exception -> 0x00e9 }
        if (r4 == 0) goto L_0x0499;
    L_0x046c:
        r4 = r11.photo;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.photo_small;	 Catch:{ Exception -> 0x00e9 }
        r55 = r11;
        r10 = r4.volume_id;	 Catch:{ Exception -> 0x00e9 }
        r4 = (r10 > r39 ? 1 : (r10 == r39 ? 0 : -1));
        if (r4 == 0) goto L_0x048d;
    L_0x0478:
        r11 = r55;
        r4 = r11.photo;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.photo_small;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.local_id;	 Catch:{ Exception -> 0x00e9 }
        if (r4 == 0) goto L_0x0499;
    L_0x0482:
        r4 = r11.photo;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.photo_small;	 Catch:{ Exception -> 0x00e9 }
        r57 = r1;
        r56 = r2;
        r10 = r4;
        goto L_0x04e8;
    L_0x048d:
        r11 = r55;
        r57 = r1;
        r56 = r2;
        r59 = r6;
        r58 = r7;
        goto L_0x050f;
    L_0x0499:
        r57 = r1;
        r56 = r2;
    L_0x049d:
        r59 = r6;
        r58 = r7;
        goto L_0x050f;
    L_0x04a3:
        r11 = r34;
        if (r3 == 0) goto L_0x04cc;
    L_0x04a7:
        r4 = r3.photo;	 Catch:{ Exception -> 0x00e9 }
        if (r4 == 0) goto L_0x04cc;
    L_0x04ab:
        r4 = r3.photo;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.photo_small;	 Catch:{ Exception -> 0x00e9 }
        if (r4 == 0) goto L_0x04cc;
    L_0x04b1:
        r4 = r3.photo;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.photo_small;	 Catch:{ Exception -> 0x00e9 }
        r57 = r1;
        r56 = r2;
        r1 = r4.volume_id;	 Catch:{ Exception -> 0x00e9 }
        r4 = (r1 > r39 ? 1 : (r1 == r39 ? 0 : -1));
        if (r4 == 0) goto L_0x049d;
    L_0x04bf:
        r1 = r3.photo;	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.photo_small;	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.local_id;	 Catch:{ Exception -> 0x00e9 }
        if (r1 == 0) goto L_0x049d;
    L_0x04c7:
        r1 = r3.photo;	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.photo_small;	 Catch:{ Exception -> 0x00e9 }
        goto L_0x04e7;
    L_0x04cc:
        r57 = r1;
        r56 = r2;
        r59 = r6;
        r58 = r7;
        goto L_0x050f;
    L_0x04d5:
        r57 = r1;
        r56 = r2;
        r11 = r34;
        r59 = r6;
        r58 = r7;
        goto L_0x050f;
    L_0x04e0:
        r57 = r1;
        r56 = r2;
        r11 = r34;
    L_0x04e6:
        r1 = 0;
    L_0x04e7:
        r10 = r1;
    L_0x04e8:
        r59 = r6;
        r58 = r7;
        goto L_0x0511;
    L_0x04ed:
        r57 = r1;
        r56 = r2;
        r11 = r34;
        r1 = r12.pushDialogs;	 Catch:{ Exception -> 0x0c25 }
        r1 = r1.size();	 Catch:{ Exception -> 0x0c25 }
        r2 = 1;
        if (r1 != r2) goto L_0x050b;
    L_0x04fc:
        r1 = "encId";
        r59 = r6;
        r58 = r7;
        r2 = 32;
        r6 = r8 >> r2;
        r2 = (int) r6;	 Catch:{ Exception -> 0x00e9 }
        r14.putExtra(r1, r2);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x050f;
    L_0x050b:
        r59 = r6;
        r58 = r7;
    L_0x050f:
        r10 = r22;
    L_0x0511:
        r1 = "currentAccount";
        r2 = r12.currentAccount;	 Catch:{ Exception -> 0x0c25 }
        r14.putExtra(r1, r2);	 Catch:{ Exception -> 0x0c25 }
        r1 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0c25 }
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = 0;
        r1 = android.app.PendingIntent.getActivity(r1, r4, r14, r2);	 Catch:{ Exception -> 0x0c25 }
        r7 = r1;
        r1 = 1;
        if (r5 == 0) goto L_0x0527;
    L_0x0525:
        if (r11 == 0) goto L_0x0529;
    L_0x0527:
        if (r3 != 0) goto L_0x0534;
    L_0x0529:
        r6 = r33;
        r2 = r6.isFcmMessage();	 Catch:{ Exception -> 0x00e9 }
        if (r2 == 0) goto L_0x0536;
    L_0x0531:
        r2 = r6.localName;	 Catch:{ Exception -> 0x00e9 }
        goto L_0x053a;
    L_0x0534:
        r6 = r33;
    L_0x0536:
        if (r11 == 0) goto L_0x053c;
    L_0x0538:
        r2 = r11.title;	 Catch:{ Exception -> 0x00e9 }
    L_0x053a:
        r4 = r2;
        goto L_0x0541;
    L_0x053c:
        r2 = org.telegram.messenger.UserObject.getUserName(r3);	 Catch:{ Exception -> 0x0c25 }
        goto L_0x053a;
    L_0x0541:
        r2 = (int) r8;
        if (r2 == 0) goto L_0x055d;
    L_0x0544:
        r2 = r12.pushDialogs;	 Catch:{ Exception -> 0x00e9 }
        r2 = r2.size();	 Catch:{ Exception -> 0x00e9 }
        r60 = r1;
        r1 = 1;
        if (r2 > r1) goto L_0x055f;
    L_0x054f:
        r1 = 0;
        r2 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r1);	 Catch:{ Exception -> 0x00e9 }
        if (r2 != 0) goto L_0x055f;
    L_0x0556:
        r1 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;	 Catch:{ Exception -> 0x00e9 }
        if (r1 == 0) goto L_0x055b;
    L_0x055a:
        goto L_0x055f;
    L_0x055b:
        r1 = r4;
        goto L_0x056b;
    L_0x055d:
        r60 = r1;
    L_0x055f:
        r1 = "AppName";
        r2 = 2131492981; // 0x7f0c0075 float:1.860943E38 double:1.0530974563E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r1, r2);	 Catch:{ Exception -> 0x0c25 }
        r2 = 0;
        r60 = r2;
    L_0x056b:
        r2 = r1;
        r1 = org.telegram.messenger.UserConfig.getActivatedAccountsCount();	 Catch:{ Exception -> 0x0c25 }
        r61 = r5;
        r5 = 1;
        if (r1 <= r5) goto L_0x05ac;
    L_0x0575:
        r1 = r12.pushDialogs;	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.size();	 Catch:{ Exception -> 0x00e9 }
        if (r1 != r5) goto L_0x058c;
    L_0x057d:
        r1 = r12.currentAccount;	 Catch:{ Exception -> 0x00e9 }
        r1 = org.telegram.messenger.UserConfig.getInstance(r1);	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.getCurrentUser();	 Catch:{ Exception -> 0x00e9 }
        r1 = org.telegram.messenger.UserObject.getFirstName(r1);	 Catch:{ Exception -> 0x00e9 }
    L_0x058b:
        goto L_0x05ae;
    L_0x058c:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r1.<init>();	 Catch:{ Exception -> 0x00e9 }
        r5 = r12.currentAccount;	 Catch:{ Exception -> 0x00e9 }
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x00e9 }
        r5 = r5.getCurrentUser();	 Catch:{ Exception -> 0x00e9 }
        r5 = org.telegram.messenger.UserObject.getFirstName(r5);	 Catch:{ Exception -> 0x00e9 }
        r1.append(r5);	 Catch:{ Exception -> 0x00e9 }
        r5 = "・";
        r1.append(r5);	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00e9 }
        goto L_0x058b;
    L_0x05ac:
        r1 = "";
    L_0x05ae:
        r5 = r12.pushDialogs;	 Catch:{ Exception -> 0x0c25 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0c25 }
        r62 = r14;
        r14 = 1;
        if (r5 != r14) goto L_0x05c6;
    L_0x05b9:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x00e9 }
        r14 = 23;
        if (r5 >= r14) goto L_0x05c0;
    L_0x05bf:
        goto L_0x05c6;
    L_0x05c0:
        r14 = r1;
        r65 = r4;
        r63 = r15;
        goto L_0x0629;
    L_0x05c6:
        r5 = r12.pushDialogs;	 Catch:{ Exception -> 0x0c25 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0c25 }
        r14 = 1;
        if (r5 != r14) goto L_0x05ed;
    L_0x05cf:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r5.<init>();	 Catch:{ Exception -> 0x00e9 }
        r5.append(r1);	 Catch:{ Exception -> 0x00e9 }
        r14 = "NewMessages";
        r63 = r15;
        r15 = r12.total_unread_count;	 Catch:{ Exception -> 0x00e9 }
        r14 = org.telegram.messenger.LocaleController.formatPluralString(r14, r15);	 Catch:{ Exception -> 0x00e9 }
        r5.append(r14);	 Catch:{ Exception -> 0x00e9 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x00e9 }
        r1 = r5;
        r14 = r1;
        r65 = r4;
        goto L_0x0629;
    L_0x05ed:
        r63 = r15;
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0c25 }
        r5.<init>();	 Catch:{ Exception -> 0x0c25 }
        r5.append(r1);	 Catch:{ Exception -> 0x0c25 }
        r14 = "NotificationMessagesPeopleDisplayOrder";
        r64 = r1;
        r15 = 2;
        r1 = new java.lang.Object[r15];	 Catch:{ Exception -> 0x0c25 }
        r15 = "NewMessages";
        r65 = r4;
        r4 = r12.total_unread_count;	 Catch:{ Exception -> 0x0c25 }
        r4 = org.telegram.messenger.LocaleController.formatPluralString(r15, r4);	 Catch:{ Exception -> 0x0c25 }
        r15 = 0;
        r1[r15] = r4;	 Catch:{ Exception -> 0x0c25 }
        r4 = "FromChats";
        r15 = r12.pushDialogs;	 Catch:{ Exception -> 0x0c25 }
        r15 = r15.size();	 Catch:{ Exception -> 0x0c25 }
        r4 = org.telegram.messenger.LocaleController.formatPluralString(r4, r15);	 Catch:{ Exception -> 0x0c25 }
        r15 = 1;
        r1[r15] = r4;	 Catch:{ Exception -> 0x0c25 }
        r4 = 2131494002; // 0x7f0c0472 float:1.86115E38 double:1.0530979607E-314;
        r1 = org.telegram.messenger.LocaleController.formatString(r14, r4, r1);	 Catch:{ Exception -> 0x0c25 }
        r5.append(r1);	 Catch:{ Exception -> 0x0c25 }
        r1 = r5.toString();	 Catch:{ Exception -> 0x0c25 }
        r14 = r1;
    L_0x0629:
        r1 = new android.support.v4.app.NotificationCompat$Builder;	 Catch:{ Exception -> 0x0c25 }
        r4 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0c25 }
        r1.<init>(r4);	 Catch:{ Exception -> 0x0c25 }
        r1 = r1.setContentTitle(r2);	 Catch:{ Exception -> 0x0c25 }
        r4 = 2131165543; // 0x7f070167 float:1.7945306E38 double:1.0529356804E-314;
        r1 = r1.setSmallIcon(r4);	 Catch:{ Exception -> 0x0c25 }
        r4 = 1;
        r1 = r1.setAutoCancel(r4);	 Catch:{ Exception -> 0x0c25 }
        r4 = r12.total_unread_count;	 Catch:{ Exception -> 0x0c25 }
        r1 = r1.setNumber(r4);	 Catch:{ Exception -> 0x0c25 }
        r1 = r1.setContentIntent(r7);	 Catch:{ Exception -> 0x0c25 }
        r4 = r12.notificationGroup;	 Catch:{ Exception -> 0x0c25 }
        r1 = r1.setGroup(r4);	 Catch:{ Exception -> 0x0c25 }
        r4 = 1;
        r1 = r1.setGroupSummary(r4);	 Catch:{ Exception -> 0x0c25 }
        r1 = r1.setShowWhen(r4);	 Catch:{ Exception -> 0x0c25 }
        r4 = r6.messageOwner;	 Catch:{ Exception -> 0x0c25 }
        r4 = r4.date;	 Catch:{ Exception -> 0x0c25 }
        r4 = (long) r4;	 Catch:{ Exception -> 0x0c25 }
        r4 = r4 * r20;
        r1 = r1.setWhen(r4);	 Catch:{ Exception -> 0x0c25 }
        r4 = -13851168; // 0xffffffffff2ca5e0 float:-2.2948849E38 double:NaN;
        r1 = r1.setColor(r4);	 Catch:{ Exception -> 0x0c25 }
        r15 = r1;
        r1 = 0;
        r4 = 0;
        r5 = 0;
        r66 = r1;
        r1 = "msg";
        r15.setCategory(r1);	 Catch:{ Exception -> 0x0c25 }
        if (r11 != 0) goto L_0x069f;
    L_0x0678:
        if (r3 == 0) goto L_0x069f;
    L_0x067a:
        r1 = r3.phone;	 Catch:{ Exception -> 0x00e9 }
        if (r1 == 0) goto L_0x069f;
    L_0x067e:
        r1 = r3.phone;	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.length();	 Catch:{ Exception -> 0x00e9 }
        if (r1 <= 0) goto L_0x069f;
    L_0x0686:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r1.<init>();	 Catch:{ Exception -> 0x00e9 }
        r67 = r4;
        r4 = "tel:+";
        r1.append(r4);	 Catch:{ Exception -> 0x00e9 }
        r4 = r3.phone;	 Catch:{ Exception -> 0x00e9 }
        r1.append(r4);	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00e9 }
        r15.addPerson(r1);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x06a1;
    L_0x069f:
        r67 = r4;
    L_0x06a1:
        r1 = 2;
        r4 = 0;
        r18 = 0;
        r68 = r1;
        r1 = r12.pushMessages;	 Catch:{ Exception -> 0x0c25 }
        r1 = r1.size();	 Catch:{ Exception -> 0x0c25 }
        r69 = r3;
        r3 = 1;
        if (r1 != r3) goto L_0x075a;
    L_0x06b2:
        r1 = r12.pushMessages;	 Catch:{ Exception -> 0x00e9 }
        r3 = 0;
        r1 = r1.get(r3);	 Catch:{ Exception -> 0x00e9 }
        r1 = (org.telegram.messenger.MessageObject) r1;	 Catch:{ Exception -> 0x00e9 }
        r70 = r4;
        r3 = 1;
        r4 = new boolean[r3];	 Catch:{ Exception -> 0x00e9 }
        r3 = r4;
        r4 = 0;
        r20 = r12.getStringForMessage(r1, r4, r3);	 Catch:{ Exception -> 0x00e9 }
        r4 = r20;
        r71 = r20;
        r72 = r4;
        r4 = r1.messageOwner;	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.silent;	 Catch:{ Exception -> 0x00e9 }
        r73 = r1;
        r1 = r71;
        if (r1 != 0) goto L_0x06d7;
    L_0x06d6:
        return;
    L_0x06d7:
        if (r60 == 0) goto L_0x0738;
    L_0x06d9:
        if (r11 == 0) goto L_0x06fb;
    L_0x06db:
        r74 = r4;
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r4.<init>();	 Catch:{ Exception -> 0x00e9 }
        r75 = r5;
        r5 = " @ ";
        r4.append(r5);	 Catch:{ Exception -> 0x00e9 }
        r4.append(r2);	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x00e9 }
        r5 = "";
        r4 = r1.replace(r4, r5);	 Catch:{ Exception -> 0x00e9 }
        r71 = r4;
    L_0x06f8:
        r1 = r71;
        goto L_0x073c;
    L_0x06fb:
        r74 = r4;
        r75 = r5;
        r4 = 0;
        r5 = r3[r4];	 Catch:{ Exception -> 0x00e9 }
        if (r5 == 0) goto L_0x071e;
    L_0x0704:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r4.<init>();	 Catch:{ Exception -> 0x00e9 }
        r4.append(r2);	 Catch:{ Exception -> 0x00e9 }
        r5 = ": ";
        r4.append(r5);	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x00e9 }
        r5 = "";
        r4 = r1.replace(r4, r5);	 Catch:{ Exception -> 0x00e9 }
        r71 = r4;
        goto L_0x06f8;
    L_0x071e:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r4.<init>();	 Catch:{ Exception -> 0x00e9 }
        r4.append(r2);	 Catch:{ Exception -> 0x00e9 }
        r5 = " ";
        r4.append(r5);	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x00e9 }
        r5 = "";
        r4 = r1.replace(r4, r5);	 Catch:{ Exception -> 0x00e9 }
        r71 = r4;
        goto L_0x06f8;
    L_0x0738:
        r74 = r4;
        r75 = r5;
    L_0x073c:
        r15.setContentText(r1);	 Catch:{ Exception -> 0x00e9 }
        r4 = new android.support.v4.app.NotificationCompat$BigTextStyle;	 Catch:{ Exception -> 0x00e9 }
        r4.<init>();	 Catch:{ Exception -> 0x00e9 }
        r4 = r4.bigText(r1);	 Catch:{ Exception -> 0x00e9 }
        r15.setStyle(r4);	 Catch:{ Exception -> 0x00e9 }
        r76 = r7;
        r78 = r8;
        r80 = r13;
        r13 = r44;
        r1 = r72;
        r4 = r74;
        goto L_0x082d;
    L_0x075a:
        r70 = r4;
        r75 = r5;
        r15.setContentText(r14);	 Catch:{ Exception -> 0x0c25 }
        r1 = new android.support.v4.app.NotificationCompat$InboxStyle;	 Catch:{ Exception -> 0x0c25 }
        r1.<init>();	 Catch:{ Exception -> 0x0c25 }
        r1.setBigContentTitle(r2);	 Catch:{ Exception -> 0x0c25 }
        r3 = r12.pushMessages;	 Catch:{ Exception -> 0x0c25 }
        r3 = r3.size();	 Catch:{ Exception -> 0x0c25 }
        r4 = 10;
        r3 = java.lang.Math.min(r4, r3);	 Catch:{ Exception -> 0x0c25 }
        r4 = 1;
        r5 = new boolean[r4];	 Catch:{ Exception -> 0x0c25 }
        r4 = r5;
        r76 = r7;
        r7 = r68;
        r5 = 0;
    L_0x077e:
        if (r5 >= r3) goto L_0x081c;
    L_0x0780:
        r77 = r3;
        r3 = r12.pushMessages;	 Catch:{ Exception -> 0x00e9 }
        r3 = r3.get(r5);	 Catch:{ Exception -> 0x00e9 }
        r3 = (org.telegram.messenger.MessageObject) r3;	 Catch:{ Exception -> 0x00e9 }
        r78 = r8;
        r8 = 0;
        r9 = r12.getStringForMessage(r3, r8, r4);	 Catch:{ Exception -> 0x00e9 }
        r8 = r9;
        if (r8 == 0) goto L_0x080c;
    L_0x0794:
        r9 = r3.messageOwner;	 Catch:{ Exception -> 0x00e9 }
        r9 = r9.date;	 Catch:{ Exception -> 0x00e9 }
        r80 = r13;
        r13 = r44;
        if (r9 > r13) goto L_0x07a0;
    L_0x079e:
        goto L_0x0810;
    L_0x07a0:
        r9 = 2;
        if (r7 != r9) goto L_0x07aa;
    L_0x07a3:
        r70 = r8;
        r9 = r3.messageOwner;	 Catch:{ Exception -> 0x00e9 }
        r9 = r9.silent;	 Catch:{ Exception -> 0x00e9 }
        r7 = r9;
    L_0x07aa:
        r9 = r12.pushDialogs;	 Catch:{ Exception -> 0x00e9 }
        r9 = r9.size();	 Catch:{ Exception -> 0x00e9 }
        r81 = r3;
        r3 = 1;
        if (r9 != r3) goto L_0x0808;
    L_0x07b5:
        if (r60 == 0) goto L_0x0808;
    L_0x07b7:
        if (r11 == 0) goto L_0x07d2;
    L_0x07b9:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r3.<init>();	 Catch:{ Exception -> 0x00e9 }
        r9 = " @ ";
        r3.append(r9);	 Catch:{ Exception -> 0x00e9 }
        r3.append(r2);	 Catch:{ Exception -> 0x00e9 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x00e9 }
        r9 = "";
        r3 = r8.replace(r3, r9);	 Catch:{ Exception -> 0x00e9 }
        r8 = r3;
        goto L_0x0808;
    L_0x07d2:
        r3 = 0;
        r9 = r4[r3];	 Catch:{ Exception -> 0x00e9 }
        if (r9 == 0) goto L_0x07f0;
    L_0x07d7:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r3.<init>();	 Catch:{ Exception -> 0x00e9 }
        r3.append(r2);	 Catch:{ Exception -> 0x00e9 }
        r9 = ": ";
        r3.append(r9);	 Catch:{ Exception -> 0x00e9 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x00e9 }
        r9 = "";
        r3 = r8.replace(r3, r9);	 Catch:{ Exception -> 0x00e9 }
        r8 = r3;
        goto L_0x0808;
    L_0x07f0:
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e9 }
        r3.<init>();	 Catch:{ Exception -> 0x00e9 }
        r3.append(r2);	 Catch:{ Exception -> 0x00e9 }
        r9 = " ";
        r3.append(r9);	 Catch:{ Exception -> 0x00e9 }
        r3 = r3.toString();	 Catch:{ Exception -> 0x00e9 }
        r9 = "";
        r3 = r8.replace(r3, r9);	 Catch:{ Exception -> 0x00e9 }
        r8 = r3;
    L_0x0808:
        r1.addLine(r8);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x0810;
    L_0x080c:
        r80 = r13;
        r13 = r44;
    L_0x0810:
        r5 = r5 + 1;
        r44 = r13;
        r3 = r77;
        r8 = r78;
        r13 = r80;
        goto L_0x077e;
    L_0x081c:
        r77 = r3;
        r78 = r8;
        r80 = r13;
        r13 = r44;
        r1.setSummaryText(r14);	 Catch:{ Exception -> 0x0c25 }
        r15.setStyle(r1);	 Catch:{ Exception -> 0x0c25 }
        r4 = r7;
        r1 = r70;
    L_0x082d:
        r3 = new android.content.Intent;	 Catch:{ Exception -> 0x0c25 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0c25 }
        r7 = org.telegram.messenger.NotificationDismissReceiver.class;
        r3.<init>(r5, r7);	 Catch:{ Exception -> 0x0c25 }
        r9 = r3;
        r3 = "messageDate";
        r5 = r6.messageOwner;	 Catch:{ Exception -> 0x0c25 }
        r5 = r5.date;	 Catch:{ Exception -> 0x0c25 }
        r9.putExtra(r3, r5);	 Catch:{ Exception -> 0x0c25 }
        r3 = "currentAccount";
        r5 = r12.currentAccount;	 Catch:{ Exception -> 0x0c25 }
        r9.putExtra(r3, r5);	 Catch:{ Exception -> 0x0c25 }
        r3 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0c25 }
        r5 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r7 = 1;
        r3 = android.app.PendingIntent.getBroadcast(r3, r7, r9, r5);	 Catch:{ Exception -> 0x0c25 }
        r15.setDeleteIntent(r3);	 Catch:{ Exception -> 0x0c25 }
        if (r10 == 0) goto L_0x08ad;
    L_0x0855:
        r3 = org.telegram.messenger.ImageLoader.getInstance();	 Catch:{ Exception -> 0x00e9 }
        r7 = "50_50";
        r8 = 0;
        r3 = r3.getImageFromMemory(r10, r8, r7);	 Catch:{ Exception -> 0x00e9 }
        if (r3 == 0) goto L_0x086c;
    L_0x0862:
        r7 = r3.getBitmap();	 Catch:{ Exception -> 0x00e9 }
        r15.setLargeIcon(r7);	 Catch:{ Exception -> 0x00e9 }
        r82 = r2;
        goto L_0x08af;
    L_0x086c:
        r7 = 1;
        r8 = org.telegram.messenger.FileLoader.getPathToAttach(r10, r7);	 Catch:{ Throwable -> 0x08a9 }
        r7 = r8;
        r8 = r7.exists();	 Catch:{ Throwable -> 0x08a9 }
        if (r8 == 0) goto L_0x08a6;
    L_0x0878:
        r8 = 1126170624; // 0x43200000 float:160.0 double:5.564022167E-315;
        r5 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);	 Catch:{ Throwable -> 0x08a9 }
        r5 = (float) r5;	 Catch:{ Throwable -> 0x08a9 }
        r8 = r8 / r5;
        r5 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x08a9 }
        r5.<init>();	 Catch:{ Throwable -> 0x08a9 }
        r20 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r20 = (r8 > r20 ? 1 : (r8 == r20 ? 0 : -1));
        if (r20 >= 0) goto L_0x0891;
    L_0x088d:
        r82 = r2;
        r2 = 1;
        goto L_0x0894;
    L_0x0891:
        r82 = r2;
        r2 = (int) r8;
    L_0x0894:
        r5.inSampleSize = r2;	 Catch:{ Throwable -> 0x08a4 }
        r2 = r7.getAbsolutePath();	 Catch:{ Throwable -> 0x08a4 }
        r2 = android.graphics.BitmapFactory.decodeFile(r2, r5);	 Catch:{ Throwable -> 0x08a4 }
        if (r2 == 0) goto L_0x08a8;
    L_0x08a0:
        r15.setLargeIcon(r2);	 Catch:{ Throwable -> 0x08a4 }
        goto L_0x08a8;
    L_0x08a4:
        r0 = move-exception;
        goto L_0x08ac;
    L_0x08a6:
        r82 = r2;
    L_0x08a8:
        goto L_0x08af;
    L_0x08a9:
        r0 = move-exception;
        r82 = r2;
    L_0x08ac:
        goto L_0x08af;
    L_0x08ad:
        r82 = r2;
    L_0x08af:
        r8 = r104;
        if (r8 == 0) goto L_0x0903;
    L_0x08b3:
        r2 = 1;
        if (r4 != r2) goto L_0x08b9;
    L_0x08b6:
        r7 = r59;
        goto L_0x0905;
    L_0x08b9:
        if (r59 != 0) goto L_0x08d0;
    L_0x08bb:
        r2 = 0;
        r15.setPriority(r2);	 Catch:{ Exception -> 0x08cb }
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x08cb }
        r3 = 26;
        if (r2 < r3) goto L_0x08c8;
    L_0x08c5:
        r2 = 3;
        r67 = r2;
    L_0x08c8:
        r7 = r59;
        goto L_0x0912;
    L_0x08cb:
        r0 = move-exception;
        r1 = r0;
        r13 = r8;
        goto L_0x0c2b;
    L_0x08d0:
        r7 = r59;
        r2 = 1;
        if (r7 == r2) goto L_0x08f7;
    L_0x08d5:
        r2 = 2;
        if (r7 != r2) goto L_0x08d9;
    L_0x08d8:
        goto L_0x08f7;
    L_0x08d9:
        r2 = 4;
        if (r7 != r2) goto L_0x08e8;
    L_0x08dc:
        r2 = -2;
        r15.setPriority(r2);	 Catch:{ Exception -> 0x08cb }
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x08cb }
        r3 = 26;
        if (r2 < r3) goto L_0x0912;
    L_0x08e6:
        r2 = 1;
        goto L_0x0910;
    L_0x08e8:
        r2 = 5;
        if (r7 != r2) goto L_0x0912;
    L_0x08eb:
        r2 = -1;
        r15.setPriority(r2);	 Catch:{ Exception -> 0x08cb }
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x08cb }
        r3 = 26;
        if (r2 < r3) goto L_0x0912;
    L_0x08f5:
        r2 = 2;
        goto L_0x0910;
    L_0x08f7:
        r2 = 1;
        r15.setPriority(r2);	 Catch:{ Exception -> 0x08cb }
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x08cb }
        r3 = 26;
        if (r2 < r3) goto L_0x0912;
    L_0x0901:
        r2 = 4;
        goto L_0x0910;
    L_0x0903:
        r7 = r59;
    L_0x0905:
        r2 = -1;
        r15.setPriority(r2);	 Catch:{ Exception -> 0x0c22 }
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0c22 }
        r3 = 26;
        if (r2 < r3) goto L_0x0912;
    L_0x090f:
        r2 = 2;
    L_0x0910:
        r67 = r2;
    L_0x0912:
        r2 = 1;
        if (r4 == r2) goto L_0x0a18;
    L_0x0915:
        if (r30 != 0) goto L_0x0a18;
    L_0x0917:
        r2 = org.telegram.messenger.ApplicationLoader.mainInterfacePaused;	 Catch:{ Exception -> 0x08cb }
        if (r2 != 0) goto L_0x091d;
    L_0x091b:
        if (r25 == 0) goto L_0x0951;
    L_0x091d:
        r2 = r1.length();	 Catch:{ Exception -> 0x08cb }
        r3 = 100;
        if (r2 <= r3) goto L_0x094c;
    L_0x0925:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x08cb }
        r2.<init>();	 Catch:{ Exception -> 0x08cb }
        r3 = 100;
        r5 = 0;
        r3 = r1.substring(r5, r3);	 Catch:{ Exception -> 0x08cb }
        r83 = r1;
        r1 = 10;
        r5 = 32;
        r1 = r3.replace(r1, r5);	 Catch:{ Exception -> 0x08cb }
        r1 = r1.trim();	 Catch:{ Exception -> 0x08cb }
        r2.append(r1);	 Catch:{ Exception -> 0x08cb }
        r1 = "...";
        r2.append(r1);	 Catch:{ Exception -> 0x08cb }
        r1 = r2.toString();	 Catch:{ Exception -> 0x08cb }
        goto L_0x094e;
    L_0x094c:
        r83 = r1;
    L_0x094e:
        r15.setTicker(r1);	 Catch:{ Exception -> 0x08cb }
    L_0x0951:
        r2 = org.telegram.messenger.MediaController.getInstance();	 Catch:{ Exception -> 0x08cb }
        r2 = r2.isRecordingAudio();	 Catch:{ Exception -> 0x08cb }
        if (r2 != 0) goto L_0x09a0;
    L_0x095b:
        if (r57 == 0) goto L_0x09a0;
    L_0x095d:
        r2 = "NoSound";
        r3 = r57;
        r2 = r3.equals(r2);	 Catch:{ Exception -> 0x08cb }
        if (r2 != 0) goto L_0x099b;
    L_0x0967:
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x08cb }
        r5 = 26;
        if (r2 < r5) goto L_0x097f;
    L_0x096d:
        r5 = r80;
        r2 = r3.equals(r5);	 Catch:{ Exception -> 0x08cb }
        if (r2 == 0) goto L_0x097a;
    L_0x0975:
        r2 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x08cb }
    L_0x0977:
        r84 = r1;
        goto L_0x09a8;
    L_0x097a:
        r2 = android.net.Uri.parse(r3);	 Catch:{ Exception -> 0x08cb }
        goto L_0x0977;
    L_0x097f:
        r5 = r80;
        r2 = r3.equals(r5);	 Catch:{ Exception -> 0x08cb }
        if (r2 == 0) goto L_0x0990;
    L_0x0987:
        r2 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x08cb }
        r84 = r1;
        r1 = 5;
        r15.setSound(r2, r1);	 Catch:{ Exception -> 0x08cb }
        goto L_0x09a6;
    L_0x0990:
        r84 = r1;
        r1 = 5;
        r2 = android.net.Uri.parse(r3);	 Catch:{ Exception -> 0x08cb }
        r15.setSound(r2, r1);	 Catch:{ Exception -> 0x08cb }
        goto L_0x09a6;
    L_0x099b:
        r84 = r1;
        r5 = r80;
        goto L_0x09a6;
    L_0x09a0:
        r84 = r1;
        r3 = r57;
        r5 = r80;
    L_0x09a6:
        r2 = r75;
    L_0x09a8:
        if (r58 == 0) goto L_0x09b8;
    L_0x09aa:
        r1 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r85 = r2;
        r2 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r86 = r4;
        r4 = r58;
        r15.setLights(r4, r1, r2);	 Catch:{ Exception -> 0x08cb }
        goto L_0x09be;
    L_0x09b8:
        r85 = r2;
        r86 = r4;
        r4 = r58;
    L_0x09be:
        r2 = r54;
        r1 = 2;
        if (r2 == r1) goto L_0x0a09;
    L_0x09c3:
        r1 = org.telegram.messenger.MediaController.getInstance();	 Catch:{ Exception -> 0x08cb }
        r1 = r1.isRecordingAudio();	 Catch:{ Exception -> 0x08cb }
        if (r1 == 0) goto L_0x09d0;
    L_0x09cd:
        r87 = r2;
        goto L_0x0a0b;
    L_0x09d0:
        r1 = 1;
        if (r2 != r1) goto L_0x09e1;
    L_0x09d3:
        r1 = 4;
        r1 = new long[r1];	 Catch:{ Exception -> 0x08cb }
        r1 = {0, 100, 0, 100};	 Catch:{ Exception -> 0x08cb }
        r20 = r1;
        r15.setVibrate(r1);	 Catch:{ Exception -> 0x08cb }
        r87 = r2;
        goto L_0x0a34;
    L_0x09e1:
        if (r2 == 0) goto L_0x09fe;
    L_0x09e3:
        r1 = 4;
        if (r2 != r1) goto L_0x09e9;
    L_0x09e6:
        r87 = r2;
        goto L_0x0a00;
    L_0x09e9:
        r1 = 3;
        if (r2 != r1) goto L_0x09f9;
    L_0x09ec:
        r87 = r2;
        r1 = 2;
        r2 = new long[r1];	 Catch:{ Exception -> 0x08cb }
        r2 = {0, 1000};	 Catch:{ Exception -> 0x08cb }
        r1 = r2;
        r15.setVibrate(r2);	 Catch:{ Exception -> 0x08cb }
        goto L_0x0a15;
    L_0x09f9:
        r87 = r2;
        r20 = r66;
        goto L_0x0a34;
    L_0x09fe:
        r87 = r2;
    L_0x0a00:
        r1 = 2;
        r15.setDefaults(r1);	 Catch:{ Exception -> 0x08cb }
        r1 = 0;
        r2 = new long[r1];	 Catch:{ Exception -> 0x08cb }
        r1 = r2;
        goto L_0x0a15;
    L_0x0a09:
        r87 = r2;
    L_0x0a0b:
        r1 = 2;
        r2 = new long[r1];	 Catch:{ Exception -> 0x08cb }
        r2 = {0, 0};	 Catch:{ Exception -> 0x08cb }
        r1 = r2;
        r15.setVibrate(r2);	 Catch:{ Exception -> 0x08cb }
    L_0x0a15:
        r20 = r1;
        goto L_0x0a34;
    L_0x0a18:
        r83 = r1;
        r86 = r4;
        r87 = r54;
        r3 = r57;
        r4 = r58;
        r5 = r80;
        r1 = 2;
        r2 = new long[r1];	 Catch:{ Exception -> 0x0c22 }
        r2 = {0, 0};	 Catch:{ Exception -> 0x0c22 }
        r1 = r2;
        r15.setVibrate(r2);	 Catch:{ Exception -> 0x0c22 }
        r20 = r1;
        r85 = r75;
        r84 = r83;
    L_0x0a34:
        r1 = 0;
        r2 = 0;
        r21 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r2);	 Catch:{ Exception -> 0x0c22 }
        if (r21 != 0) goto L_0x0b2b;
    L_0x0a3c:
        r2 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;	 Catch:{ Exception -> 0x08cb }
        if (r2 != 0) goto L_0x0b2b;
    L_0x0a40:
        r21 = r6.getDialogId();	 Catch:{ Exception -> 0x08cb }
        r33 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
        r2 = (r21 > r33 ? 1 : (r21 == r33 ? 0 : -1));
        if (r2 != 0) goto L_0x0b2b;
    L_0x0a4b:
        r2 = r6.messageOwner;	 Catch:{ Exception -> 0x08cb }
        r2 = r2.reply_markup;	 Catch:{ Exception -> 0x08cb }
        if (r2 == 0) goto L_0x0b2b;
    L_0x0a51:
        r2 = r6.messageOwner;	 Catch:{ Exception -> 0x08cb }
        r2 = r2.reply_markup;	 Catch:{ Exception -> 0x08cb }
        r2 = r2.rows;	 Catch:{ Exception -> 0x08cb }
        r21 = 0;
        r22 = r2.size();	 Catch:{ Exception -> 0x08cb }
        r102 = r21;
        r21 = r1;
        r1 = r102;
    L_0x0a63:
        r88 = r22;
        r89 = r3;
        r3 = r88;
        if (r1 >= r3) goto L_0x0b1e;
    L_0x0a6b:
        r22 = r2.get(r1);	 Catch:{ Exception -> 0x08cb }
        r22 = (org.telegram.tgnet.TLRPC.TL_keyboardButtonRow) r22;	 Catch:{ Exception -> 0x08cb }
        r90 = r22;
        r22 = 0;
        r91 = r2;
        r92 = r3;
        r2 = r90;
        r3 = r2.buttons;	 Catch:{ Exception -> 0x08cb }
        r3 = r3.size();	 Catch:{ Exception -> 0x08cb }
        r93 = r5;
        r5 = r22;
    L_0x0a85:
        if (r5 >= r3) goto L_0x0b04;
    L_0x0a87:
        r94 = r3;
        r3 = r2.buttons;	 Catch:{ Exception -> 0x08cb }
        r3 = r3.get(r5);	 Catch:{ Exception -> 0x08cb }
        r3 = (org.telegram.tgnet.TLRPC.KeyboardButton) r3;	 Catch:{ Exception -> 0x08cb }
        r95 = r2;
        r2 = r3 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;	 Catch:{ Exception -> 0x08cb }
        if (r2 == 0) goto L_0x0ae7;
    L_0x0a97:
        r2 = new android.content.Intent;	 Catch:{ Exception -> 0x08cb }
        r96 = r7;
        r7 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x08cb }
        r8 = org.telegram.messenger.NotificationCallbackReceiver.class;
        r2.<init>(r7, r8);	 Catch:{ Exception -> 0x00e9 }
        r7 = "currentAccount";
        r8 = r12.currentAccount;	 Catch:{ Exception -> 0x00e9 }
        r2.putExtra(r7, r8);	 Catch:{ Exception -> 0x00e9 }
        r7 = "did";
        r97 = r9;
        r8 = r78;
        r2.putExtra(r7, r8);	 Catch:{ Exception -> 0x00e9 }
        r7 = r3.data;	 Catch:{ Exception -> 0x00e9 }
        if (r7 == 0) goto L_0x0ac0;
    L_0x0ab6:
        r7 = "data";
        r98 = r10;
        r10 = r3.data;	 Catch:{ Exception -> 0x00e9 }
        r2.putExtra(r7, r10);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x0ac2;
    L_0x0ac0:
        r98 = r10;
    L_0x0ac2:
        r7 = "mid";
        r10 = r6.getId();	 Catch:{ Exception -> 0x00e9 }
        r2.putExtra(r7, r10);	 Catch:{ Exception -> 0x00e9 }
        r7 = r3.text;	 Catch:{ Exception -> 0x00e9 }
        r10 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x00e9 }
        r99 = r3;
        r3 = r12.lastButtonId;	 Catch:{ Exception -> 0x00e9 }
        r100 = r6;
        r6 = r3 + 1;
        r12.lastButtonId = r6;	 Catch:{ Exception -> 0x00e9 }
        r6 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r3 = android.app.PendingIntent.getBroadcast(r10, r3, r2, r6);	 Catch:{ Exception -> 0x00e9 }
        r6 = 0;
        r15.addAction(r6, r7, r3);	 Catch:{ Exception -> 0x00e9 }
        r2 = 1;
        r21 = r2;
        goto L_0x0af1;
    L_0x0ae7:
        r100 = r6;
        r96 = r7;
        r97 = r9;
        r98 = r10;
        r8 = r78;
    L_0x0af1:
        r5 = r5 + 1;
        r78 = r8;
        r3 = r94;
        r2 = r95;
        r7 = r96;
        r9 = r97;
        r10 = r98;
        r6 = r100;
        r8 = r104;
        goto L_0x0a85;
    L_0x0b04:
        r100 = r6;
        r96 = r7;
        r97 = r9;
        r98 = r10;
        r8 = r78;
        r1 = r1 + 1;
        r3 = r89;
        r2 = r91;
        r22 = r92;
        r5 = r93;
        r9 = r97;
        r8 = r104;
        goto L_0x0a63;
    L_0x0b1e:
        r93 = r5;
        r100 = r6;
        r96 = r7;
        r97 = r9;
        r98 = r10;
        r8 = r78;
        goto L_0x0b3b;
    L_0x0b2b:
        r89 = r3;
        r93 = r5;
        r100 = r6;
        r96 = r7;
        r97 = r9;
        r98 = r10;
        r8 = r78;
        r21 = r1;
    L_0x0b3b:
        if (r21 != 0) goto L_0x0b98;
    L_0x0b3d:
        r1 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x00e9 }
        r2 = 24;
        if (r1 >= r2) goto L_0x0b98;
    L_0x0b43:
        r1 = org.telegram.messenger.SharedConfig.passcodeHash;	 Catch:{ Exception -> 0x00e9 }
        r1 = r1.length();	 Catch:{ Exception -> 0x00e9 }
        if (r1 != 0) goto L_0x0b98;
    L_0x0b4b:
        r1 = r103.hasMessagesToReply();	 Catch:{ Exception -> 0x00e9 }
        if (r1 == 0) goto L_0x0b98;
    L_0x0b51:
        r1 = new android.content.Intent;	 Catch:{ Exception -> 0x00e9 }
        r2 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x00e9 }
        r3 = org.telegram.messenger.PopupReplyReceiver.class;
        r1.<init>(r2, r3);	 Catch:{ Exception -> 0x00e9 }
        r2 = "currentAccount";
        r3 = r12.currentAccount;	 Catch:{ Exception -> 0x00e9 }
        r1.putExtra(r2, r3);	 Catch:{ Exception -> 0x00e9 }
        r2 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x00e9 }
        r3 = 19;
        if (r2 > r3) goto L_0x0b80;
    L_0x0b67:
        r2 = 2131165355; // 0x7f0700ab float:1.7944925E38 double:1.0529355875E-314;
        r3 = "Reply";
        r5 = 2131494235; // 0x7f0c055b float:1.8611973E38 double:1.053098076E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r5);	 Catch:{ Exception -> 0x00e9 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x00e9 }
        r6 = 2;
        r7 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r5 = android.app.PendingIntent.getBroadcast(r5, r6, r1, r7);	 Catch:{ Exception -> 0x00e9 }
        r15.addAction(r2, r3, r5);	 Catch:{ Exception -> 0x00e9 }
        goto L_0x0b98;
    L_0x0b80:
        r2 = 2131165354; // 0x7f0700aa float:1.7944923E38 double:1.052935587E-314;
        r3 = "Reply";
        r5 = 2131494235; // 0x7f0c055b float:1.8611973E38 double:1.053098076E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r5);	 Catch:{ Exception -> 0x00e9 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x00e9 }
        r6 = 2;
        r7 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r5 = android.app.PendingIntent.getBroadcast(r5, r6, r1, r7);	 Catch:{ Exception -> 0x00e9 }
        r15.addAction(r2, r3, r5);	 Catch:{ Exception -> 0x00e9 }
    L_0x0b98:
        r1 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0c25 }
        r2 = 26;
        if (r1 < r2) goto L_0x0be8;
    L_0x0b9e:
        r22 = r27;
        r23 = r48;
        r47 = r89;
        r1 = r12;
        r24 = r56;
        r26 = r69;
        r27 = r82;
        r10 = r87;
        r2 = r8;
        r33 = r61;
        r34 = r93;
        r5 = r20;
        r36 = r31;
        r32 = r96;
        r31 = r100;
        r6 = r4;
        r39 = r4;
        r38 = r50;
        r40 = r76;
        r7 = r85;
        r41 = r8;
        r4 = r104;
        r8 = r67;
        r43 = r97;
        r9 = r35;
        r44 = r10;
        r45 = r98;
        r10 = r49;
        r48 = r11;
        r46 = r52;
        r11 = r19;
        r101 = r13;
        r74 = r86;
        r13 = r4;
        r4 = r65;
        r1 = r1.validateChannelId(r2, r4, r5, r6, r7, r8, r9, r10, r11);	 Catch:{ Exception -> 0x0c29 }
        r15.setChannelId(r1);	 Catch:{ Exception -> 0x0c29 }
        goto L_0x0c18;
    L_0x0be8:
        r39 = r4;
        r41 = r8;
        r101 = r13;
        r22 = r27;
        r36 = r31;
        r23 = r48;
        r38 = r50;
        r46 = r52;
        r24 = r56;
        r33 = r61;
        r4 = r65;
        r26 = r69;
        r40 = r76;
        r27 = r82;
        r74 = r86;
        r44 = r87;
        r47 = r89;
        r34 = r93;
        r32 = r96;
        r43 = r97;
        r45 = r98;
        r31 = r100;
        r13 = r104;
        r48 = r11;
    L_0x0c18:
        r12.showExtraNotifications(r15, r13, r14);	 Catch:{ Exception -> 0x0c29 }
        r1 = 0;
        r12.lastNotificationIsNoData = r1;	 Catch:{ Exception -> 0x0c29 }
        r103.scheduleNotificationRepeat();	 Catch:{ Exception -> 0x0c29 }
        goto L_0x0c2e;
    L_0x0c22:
        r0 = move-exception;
        r13 = r8;
        goto L_0x0c2a;
    L_0x0c25:
        r0 = move-exception;
        r13 = r104;
        goto L_0x0c2a;
    L_0x0c29:
        r0 = move-exception;
    L_0x0c2a:
        r1 = r0;
    L_0x0c2b:
        org.telegram.messenger.FileLog.e(r1);
    L_0x0c2e:
        return;
    L_0x0c2f:
        r103.dismissNotification();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationsController.showOrUpdateNotification(boolean):void");
    }

    @SuppressLint({"InlinedApi"})
    private void showExtraNotifications(NotificationCompat.Builder notificationBuilder, boolean notifyAboutLast, String summary) {
        ArrayList<Long> sortedDialogs;
        LongSparseArray<ArrayList<MessageObject>> messagesByDialogs;
        LongSparseArray<Integer> oldIdsWear;
        Notification mainNotification;
        JSONArray serializedNotifications;
        int b;
        JSONArray serializedNotifications2;
        ArrayList<AnonymousClass1NotificationHolder> holders;
        int max_date;
        boolean[] zArr;
        JSONArray serializedMsgs;
        String name;
        NotificationCompat.Builder showWhen;
        User user;
        Integer internalId;
        User user2;
        NotificationCompat.Builder builder;
        Integer num;
        String str;
        JSONArray builder2;
        NotificationsController notificationsController = this;
        Notification mainNotification2 = notificationBuilder.build();
        if (VERSION.SDK_INT < 18) {
            notificationManager.notify(notificationsController.notificationId, mainNotification2);
            return;
        }
        int a;
        int i;
        int a2;
        ArrayList<Long> sortedDialogs2 = new ArrayList();
        LongSparseArray<ArrayList<MessageObject>> messagesByDialogs2 = new LongSparseArray();
        int i2 = 0;
        for (a = 0; a < notificationsController.pushMessages.size(); a++) {
            MessageObject messageObject = (MessageObject) notificationsController.pushMessages.get(a);
            long dialog_id = messageObject.getDialogId();
            ArrayList<MessageObject> arrayList = (ArrayList) messagesByDialogs2.get(dialog_id);
            if (arrayList == null) {
                arrayList = new ArrayList();
                messagesByDialogs2.put(dialog_id, arrayList);
                sortedDialogs2.add(0, Long.valueOf(dialog_id));
            }
            arrayList.add(messageObject);
        }
        LongSparseArray<Integer> oldIdsWear2 = notificationsController.wearNotificationsIds.clone();
        notificationsController.wearNotificationsIds.clear();
        ArrayList<AnonymousClass1NotificationHolder> holders2 = new ArrayList();
        JSONArray serializedNotifications3 = null;
        if (WearDataLayerListenerService.isWatchConnected()) {
            serializedNotifications3 = new JSONArray();
        }
        int b2 = 0;
        int size = sortedDialogs2.size();
        while (b2 < size) {
            long dialog_id2 = ((Long) sortedDialogs2.get(b2)).longValue();
            ArrayList<MessageObject> messageObjects = (ArrayList) messagesByDialogs2.get(dialog_id2);
            int max_id = ((MessageObject) messageObjects.get(i2)).getId();
            int lowerId = (int) dialog_id2;
            sortedDialogs = sortedDialogs2;
            messagesByDialogs = messagesByDialogs2;
            int highId = (int) (dialog_id2 >> 32);
            Integer internalId2 = (Integer) oldIdsWear2.get(dialog_id2);
            if (internalId2 != null) {
                oldIdsWear2.remove(dialog_id2);
            } else if (lowerId != 0) {
                internalId2 = Integer.valueOf(lowerId);
            } else {
                internalId2 = Integer.valueOf(highId);
            }
            JSONObject serializedChat = null;
            if (serializedNotifications3 != null) {
                serializedChat = new JSONObject();
            }
            MessageObject lastMessageObject = (MessageObject) messageObjects.get(0);
            int size2 = size;
            size = lastMessageObject.messageOwner.date;
            Chat chat = null;
            User user3 = null;
            boolean isChannel = false;
            boolean isSupergroup = false;
            FileLocation fileLocation = null;
            oldIdsWear = oldIdsWear2;
            boolean z;
            MessageObject messageObject2;
            String name2;
            User user4;
            boolean z2;
            FileLocation photoPath;
            ArrayList<AnonymousClass1NotificationHolder> holders3;
            JSONObject serializedChat2;
            int highId2;
            Intent msgHeardIntent;
            User user5;
            PendingIntent msgHeardPendingIntent;
            Intent intent;
            Integer internalId3;
            PendingIntent pendingIntent;
            boolean canReply;
            Action action;
            Action wearReplyAction;
            Integer count;
            MessagingStyle messagingStyle;
            Object[] objArr;
            FileLocation photoPath2;
            MessagingStyle messagingStyle2;
            StringBuilder text;
            boolean[] isText;
            JSONArray serializedMsgs2;
            ArrayList<TL_keyboardButtonRow> rows;
            int rowsMid;
            ArrayList<TL_keyboardButtonRow> rows2;
            ArrayList<TL_keyboardButtonRow> rows3;
            MessageObject messageObject3;
            String name3;
            ArrayList<MessageObject> messageObjects2;
            String message;
            Action action2;
            int i3;
            StringBuilder stringBuilder;
            String nameToReplace;
            User sender;
            Intent intent2;
            StringBuilder stringBuilder2;
            int highId3;
            PendingIntent contentIntent;
            WearableExtender wearableExtender;
            StringBuilder stringBuilder3;
            Intent intent3;
            StringBuilder stringBuilder4;
            String dismissalID;
            WearableExtender summaryExtender;
            long date;
            int max_id2;
            String str2;
            Notification unreadConvBuilder;
            File file;
            float scaleFactor;
            MessagingStyle img;
            Bitmap bitmap;
            WearableExtender wearableExtender2;
            long j;
            int i4;
            ArrayList<TL_keyboardButtonRow> arrayList2;
            JSONObject jSONObject;
            boolean z3;
            MessagingStyle messagingStyle3;
            if (lowerId != 0) {
                z = true;
                if (lowerId > 0) {
                    mainNotification = mainNotification2;
                    mainNotification2 = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(lowerId));
                    if (mainNotification2 != null) {
                        String name4 = UserObject.getUserName(mainNotification2);
                        if (mainNotification2.photo == null || mainNotification2.photo.photo_small == null) {
                            serializedNotifications = serializedNotifications3;
                            b = b2;
                        } else {
                            serializedNotifications = serializedNotifications3;
                            b = b2;
                            if (!(mainNotification2.photo.photo_small.volume_id == null || mainNotification2.photo.photo_small.local_id == 0)) {
                                messageObject2 = lastMessageObject;
                                fileLocation = mainNotification2.photo.photo_small;
                                name2 = name4;
                            }
                        }
                        name2 = name4;
                    } else if (lastMessageObject.isFcmMessage()) {
                        name2 = lastMessageObject.localName;
                        messageObject2 = lastMessageObject;
                        serializedNotifications = serializedNotifications3;
                        b = b2;
                    } else {
                        serializedNotifications2 = serializedNotifications3;
                        b = b2;
                        holders = holders2;
                        b2 = b + 1;
                        holders2 = holders;
                        messagesByDialogs2 = messagesByDialogs;
                        size = size2;
                        oldIdsWear2 = oldIdsWear;
                        mainNotification2 = mainNotification;
                        i2 = 0;
                        serializedNotifications3 = serializedNotifications2;
                        sortedDialogs2 = sortedDialogs;
                    }
                    user4 = mainNotification2;
                    mainNotification2 = serializedChat;
                } else {
                    mainNotification = mainNotification2;
                    serializedNotifications = serializedNotifications3;
                    b = b2;
                    mainNotification2 = MessagesController.getInstance(notificationsController.currentAccount).getChat(Integer.valueOf(-lowerId));
                    if (mainNotification2 != null) {
                        boolean isSupergroup2;
                        boolean isSupergroup3 = mainNotification2.megagroup;
                        z2 = ChatObject.isChannel(mainNotification2) && !mainNotification2.megagroup;
                        isChannel = z2;
                        serializedNotifications3 = mainNotification2.title;
                        if (mainNotification2.photo == null || mainNotification2.photo.photo_small == null) {
                            isSupergroup2 = isSupergroup3;
                        } else {
                            messageObject2 = lastMessageObject;
                            isSupergroup2 = isSupergroup3;
                            if (!(mainNotification2.photo.photo_small.volume_id == null || mainNotification2.photo.photo_small.local_id == null)) {
                                chat = mainNotification2;
                                fileLocation = mainNotification2.photo.photo_small;
                                name2 = serializedNotifications3;
                                mainNotification2 = serializedChat;
                                user4 = null;
                                isSupergroup = isSupergroup2;
                            }
                        }
                        chat = mainNotification2;
                        name2 = serializedNotifications3;
                        mainNotification2 = serializedChat;
                        user4 = null;
                        isSupergroup = isSupergroup2;
                    } else if (lastMessageObject.isFcmMessage()) {
                        isSupergroup = lastMessageObject.isMegagroup();
                        name2 = lastMessageObject.localName;
                        chat = mainNotification2;
                        messageObject2 = lastMessageObject;
                        isChannel = lastMessageObject.localChannel;
                        mainNotification2 = serializedChat;
                        user4 = user3;
                    }
                }
                if (!AndroidUtilities.needShowPasscode(false)) {
                    if (SharedConfig.isWaitingForPasscodeEnter) {
                    }
                    photoPath = fileLocation;
                    z2 = z;
                    holders3 = holders2;
                    serializedChat2 = mainNotification2;
                    highId2 = highId;
                    mainNotification2 = new UnreadConversation.Builder(name2).setLatestTimestamp(((long) size) * 1000);
                    max_date = size;
                    msgHeardIntent = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    msgHeardIntent.addFlags(32);
                    msgHeardIntent.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    msgHeardIntent.putExtra("dialog_id", dialog_id2);
                    msgHeardIntent.putExtra("max_id", max_id);
                    msgHeardIntent.putExtra("currentAccount", notificationsController.currentAccount);
                    user5 = user4;
                    msgHeardPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent, 134217728);
                    mainNotification2.setReadPendingIntent(msgHeardPendingIntent);
                    if (isChannel) {
                        if (!isSupergroup) {
                            intent = msgHeardIntent;
                            internalId3 = internalId2;
                            pendingIntent = msgHeardPendingIntent;
                            canReply = z2;
                            action = null;
                            wearReplyAction = action;
                            count = (Integer) notificationsController.pushDialogs.get(dialog_id2);
                            if (count == null) {
                                i = 0;
                                count = Integer.valueOf(0);
                            } else {
                                i = 0;
                            }
                            messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID);
                            objArr = new Object[2];
                            objArr[i] = name2;
                            photoPath2 = photoPath;
                            objArr[1] = LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()));
                            messagingStyle2 = messagingStyle.setConversationTitle(String.format("%1$s (%2$s)", objArr));
                            text = new StringBuilder();
                            isText = new boolean[1];
                            serializedMsgs2 = null;
                            if (serializedChat2 != null) {
                                rows = null;
                                serializedMsgs2 = new JSONArray();
                            } else {
                                rows = null;
                            }
                            i = messageObjects.size() - 1;
                            rowsMid = 0;
                            rows2 = rows;
                            while (true) {
                                rows3 = rows2;
                                if (i < 0) {
                                    break;
                                }
                                messageObject3 = (MessageObject) messageObjects.get(i);
                                name3 = name2;
                                messageObjects2 = messageObjects;
                                message = getStringForMessage(messageObject3, null, isText);
                                name2 = messageObject3.isFcmMessage() != null ? messageObject3.localName : name3;
                                if (message == null) {
                                    zArr = isText;
                                    action2 = wearReplyAction;
                                    i3 = max_id;
                                } else {
                                    if (lowerId < 0) {
                                        i3 = max_id;
                                        stringBuilder = new StringBuilder();
                                        action2 = wearReplyAction;
                                        stringBuilder.append(" @ ");
                                        stringBuilder.append(name2);
                                        wearReplyAction = message.replace(stringBuilder.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    } else {
                                        action2 = wearReplyAction;
                                        i3 = max_id;
                                        if (isText[null]) {
                                            wearReplyAction = new StringBuilder();
                                            wearReplyAction.append(name2);
                                            wearReplyAction.append(": ");
                                            wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                        } else {
                                            wearReplyAction = new StringBuilder();
                                            wearReplyAction.append(name2);
                                            wearReplyAction.append(" ");
                                            wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                        }
                                    }
                                    if (text.length() > 0) {
                                        text.append("\n\n");
                                    }
                                    text.append(wearReplyAction);
                                    mainNotification2.addMessage(wearReplyAction);
                                    nameToReplace = name2;
                                    messagingStyle2.addMessage(wearReplyAction, ((long) messageObject3.messageOwner.date) * 1000, null);
                                    if (serializedMsgs2 != null) {
                                        try {
                                            name2 = new JSONObject();
                                            name2.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject3));
                                            name2.put("date", messageObject3.messageOwner.date);
                                            if (messageObject3.isFromUser() && lowerId < 0) {
                                                sender = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(messageObject3.getFromId()));
                                                if (sender != null) {
                                                    zArr = isText;
                                                    try {
                                                        name2.put("fname", sender.first_name);
                                                        name2.put("lname", sender.last_name);
                                                        serializedMsgs2.put(name2);
                                                    } catch (JSONException e) {
                                                        name2 = messageObject3.messageOwner.reply_markup.rows;
                                                        isText = messageObject3.getId();
                                                        rows2 = name2;
                                                        rowsMid = isText;
                                                        i--;
                                                        name2 = name3;
                                                        messageObjects = messageObjects2;
                                                        max_id = i3;
                                                        wearReplyAction = action2;
                                                        isText = zArr;
                                                    }
                                                }
                                            }
                                            zArr = isText;
                                            serializedMsgs2.put(name2);
                                        } catch (JSONException e2) {
                                            zArr = isText;
                                            name2 = messageObject3.messageOwner.reply_markup.rows;
                                            isText = messageObject3.getId();
                                            rows2 = name2;
                                            rowsMid = isText;
                                            i--;
                                            name2 = name3;
                                            messageObjects = messageObjects2;
                                            max_id = i3;
                                            wearReplyAction = action2;
                                            isText = zArr;
                                        }
                                    } else {
                                        zArr = isText;
                                    }
                                    if (dialog_id2 == 777000 && messageObject3.messageOwner.reply_markup != null) {
                                        name2 = messageObject3.messageOwner.reply_markup.rows;
                                        isText = messageObject3.getId();
                                        rows2 = name2;
                                        rowsMid = isText;
                                        i--;
                                        name2 = name3;
                                        messageObjects = messageObjects2;
                                        max_id = i3;
                                        wearReplyAction = action2;
                                        isText = zArr;
                                    }
                                }
                                rows2 = rows3;
                                i--;
                                name2 = name3;
                                messageObjects = messageObjects2;
                                max_id = i3;
                                wearReplyAction = action2;
                                isText = zArr;
                            }
                            name3 = name2;
                            zArr = isText;
                            action2 = wearReplyAction;
                            messageObjects2 = messageObjects;
                            i3 = max_id;
                            intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("com.tmessages.openchat");
                            stringBuilder2.append(Math.random());
                            stringBuilder2.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent2.setAction(stringBuilder2.toString());
                            intent2.setFlags(32768);
                            if (lowerId != 0) {
                                if (lowerId > 0) {
                                    intent2.putExtra("userId", lowerId);
                                } else {
                                    intent2.putExtra("chatId", -lowerId);
                                }
                                highId3 = highId2;
                            } else {
                                highId3 = highId2;
                                intent2.putExtra("encId", highId3);
                            }
                            intent2.putExtra("currentAccount", notificationsController.currentAccount);
                            contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                            wearableExtender = new WearableExtender();
                            if (action2 != null) {
                                wearReplyAction = action2;
                                wearableExtender.addAction(wearReplyAction);
                            } else {
                                wearReplyAction = action2;
                            }
                            if (lowerId == 0) {
                                max_id = i3;
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("tgenc");
                                stringBuilder3.append(highId3);
                                stringBuilder3.append("_");
                                stringBuilder3.append(max_id);
                                message = stringBuilder3.toString();
                            } else if (lowerId > 0) {
                                message = new StringBuilder();
                                message.append("tguser");
                                message.append(lowerId);
                                message.append("_");
                                max_id = i3;
                                message.append(max_id);
                                message = message.toString();
                                intent3 = intent2;
                            } else {
                                max_id = i3;
                                stringBuilder4 = new StringBuilder();
                                stringBuilder4.append("tgchat");
                                stringBuilder4.append(-lowerId);
                                stringBuilder4.append("_");
                                stringBuilder4.append(max_id);
                                message = stringBuilder4.toString();
                            }
                            dismissalID = message;
                            wearableExtender.setDismissalId(dismissalID);
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("tgaccount");
                            stringBuilder4.append(UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                            wearableExtender.setBridgeTag(stringBuilder4.toString());
                            summaryExtender = new WearableExtender();
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("summary_");
                            stringBuilder4.append(dismissalID);
                            summaryExtender.setDismissalId(stringBuilder4.toString());
                            notificationBuilder.extend(summaryExtender);
                            messageObjects = messageObjects2;
                            serializedMsgs = serializedMsgs2;
                            date = ((long) ((MessageObject) messageObjects.get(null)).messageOwner.date) * 1000;
                            name = name3;
                            max_id2 = max_id;
                            showWhen = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(notificationsController.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true);
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("sdid_");
                            stringBuilder.append(dialog_id2);
                            showWhen = showWhen.setShortcutId(stringBuilder.toString()).setGroupAlertBehavior(1).setStyle(messagingStyle2).setContentIntent(contentIntent).extend(wearableExtender);
                            text = new StringBuilder();
                            text.append(TtmlNode.ANONYMOUS_REGION_ID);
                            text.append(Long.MAX_VALUE - date);
                            showWhen = showWhen.setSortKey(text.toString()).extend(new CarExtender().setUnreadConversation(mainNotification2.build())).setCategory("msg");
                            if (notificationsController.pushDialogs.size() == 1 || TextUtils.isEmpty(summary)) {
                                str2 = summary;
                            } else {
                                showWhen.setSubText(summary);
                            }
                            if (lowerId == 0) {
                                showWhen.setLocalOnly(true);
                            }
                            if (photoPath2 != null) {
                                unreadConvBuilder = mainNotification2;
                                mainNotification2 = photoPath2;
                                messagingStyle2 = ImageLoader.getInstance().getImageFromMemory(mainNotification2, null, "50_50");
                                if (messagingStyle2 != null) {
                                    showWhen.setLargeIcon(messagingStyle2.getBitmap());
                                } else {
                                    try {
                                        file = FileLoader.getPathToAttach(mainNotification2, true);
                                        if (file.exists()) {
                                            scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                            img = messagingStyle2;
                                            try {
                                                messagingStyle2 = new Options();
                                                messagingStyle2.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), messagingStyle2);
                                                if (bitmap != null) {
                                                    showWhen.setLargeIcon(bitmap);
                                                }
                                            } catch (Throwable th) {
                                                if (!AndroidUtilities.needShowPasscode(false)) {
                                                }
                                                wearableExtender2 = wearableExtender;
                                                j = date;
                                                i4 = rowsMid;
                                                arrayList2 = rows3;
                                                if (chat == null) {
                                                }
                                                user = user5;
                                                if (VERSION.SDK_INT >= 26) {
                                                    showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                                                }
                                                internalId = internalId3;
                                                holders = holders3;
                                                holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                                                notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                                                if (serializedChat2 != null) {
                                                    user2 = user;
                                                    builder = showWhen;
                                                    num = internalId;
                                                    str = name;
                                                    serializedNotifications2 = serializedNotifications;
                                                    jSONObject = serializedChat2;
                                                    size = max_date;
                                                    z3 = canReply;
                                                    builder2 = serializedMsgs;
                                                    max_id = max_id2;
                                                } else {
                                                    try {
                                                        jSONObject = serializedChat2;
                                                        try {
                                                            jSONObject.put("reply", canReply);
                                                            jSONObject.put("name", name);
                                                        } catch (JSONException e3) {
                                                            user2 = user;
                                                            builder = showWhen;
                                                            num = internalId;
                                                            str = name;
                                                            serializedNotifications2 = serializedNotifications;
                                                            size = max_date;
                                                            builder2 = serializedMsgs;
                                                            max_id = max_id2;
                                                            b2 = b + 1;
                                                            holders2 = holders;
                                                            messagesByDialogs2 = messagesByDialogs;
                                                            size = size2;
                                                            oldIdsWear2 = oldIdsWear;
                                                            mainNotification2 = mainNotification;
                                                            i2 = 0;
                                                            serializedNotifications3 = serializedNotifications2;
                                                            sortedDialogs2 = sortedDialogs;
                                                        }
                                                        try {
                                                            jSONObject.put("max_id", max_id2);
                                                            try {
                                                                jSONObject.put("max_date", max_date);
                                                                try {
                                                                    jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                                                                    if (mainNotification2 != null) {
                                                                        num = internalId;
                                                                        str = name;
                                                                    } else {
                                                                        try {
                                                                            user = "photo";
                                                                            text = new StringBuilder();
                                                                            try {
                                                                                text.append(mainNotification2.dc_id);
                                                                                text.append("_");
                                                                                try {
                                                                                    text.append(mainNotification2.volume_id);
                                                                                    text.append("_");
                                                                                    text.append(mainNotification2.secret);
                                                                                    jSONObject.put(user, text.toString());
                                                                                } catch (JSONException e4) {
                                                                                    serializedNotifications2 = serializedNotifications;
                                                                                    builder2 = serializedMsgs;
                                                                                    b2 = b + 1;
                                                                                    holders2 = holders;
                                                                                    messagesByDialogs2 = messagesByDialogs;
                                                                                    size = size2;
                                                                                    oldIdsWear2 = oldIdsWear;
                                                                                    mainNotification2 = mainNotification;
                                                                                    i2 = 0;
                                                                                    serializedNotifications3 = serializedNotifications2;
                                                                                    sortedDialogs2 = sortedDialogs;
                                                                                }
                                                                            } catch (JSONException e5) {
                                                                                num = internalId;
                                                                                str = name;
                                                                                serializedNotifications2 = serializedNotifications;
                                                                                builder2 = serializedMsgs;
                                                                                b2 = b + 1;
                                                                                holders2 = holders;
                                                                                messagesByDialogs2 = messagesByDialogs;
                                                                                size = size2;
                                                                                oldIdsWear2 = oldIdsWear;
                                                                                mainNotification2 = mainNotification;
                                                                                i2 = 0;
                                                                                serializedNotifications3 = serializedNotifications2;
                                                                                sortedDialogs2 = sortedDialogs;
                                                                            }
                                                                        } catch (JSONException e6) {
                                                                            builder = showWhen;
                                                                            num = internalId;
                                                                            str = name;
                                                                            serializedNotifications2 = serializedNotifications;
                                                                            builder2 = serializedMsgs;
                                                                            b2 = b + 1;
                                                                            holders2 = holders;
                                                                            messagesByDialogs2 = messagesByDialogs;
                                                                            size = size2;
                                                                            oldIdsWear2 = oldIdsWear;
                                                                            mainNotification2 = mainNotification;
                                                                            i2 = 0;
                                                                            serializedNotifications3 = serializedNotifications2;
                                                                            sortedDialogs2 = sortedDialogs;
                                                                        }
                                                                    }
                                                                    if (serializedMsgs != null) {
                                                                        builder2 = serializedMsgs;
                                                                    } else {
                                                                        try {
                                                                        } catch (JSONException e7) {
                                                                            builder2 = serializedMsgs;
                                                                            serializedNotifications2 = serializedNotifications;
                                                                            b2 = b + 1;
                                                                            holders2 = holders;
                                                                            messagesByDialogs2 = messagesByDialogs;
                                                                            size = size2;
                                                                            oldIdsWear2 = oldIdsWear;
                                                                            mainNotification2 = mainNotification;
                                                                            i2 = 0;
                                                                            serializedNotifications3 = serializedNotifications2;
                                                                            sortedDialogs2 = sortedDialogs;
                                                                        }
                                                                        try {
                                                                            jSONObject.put("msgs", serializedMsgs);
                                                                        } catch (JSONException e8) {
                                                                            serializedNotifications2 = serializedNotifications;
                                                                            b2 = b + 1;
                                                                            holders2 = holders;
                                                                            messagesByDialogs2 = messagesByDialogs;
                                                                            size = size2;
                                                                            oldIdsWear2 = oldIdsWear;
                                                                            mainNotification2 = mainNotification;
                                                                            i2 = 0;
                                                                            serializedNotifications3 = serializedNotifications2;
                                                                            sortedDialogs2 = sortedDialogs;
                                                                        }
                                                                    }
                                                                    if (lowerId <= 0) {
                                                                        jSONObject.put("type", "user");
                                                                    } else if (lowerId < 0) {
                                                                        if (!isChannel) {
                                                                            if (isSupergroup) {
                                                                                jSONObject.put("type", "group");
                                                                            }
                                                                        }
                                                                        jSONObject.put("type", "channel");
                                                                    }
                                                                    serializedNotifications2 = serializedNotifications;
                                                                } catch (JSONException e9) {
                                                                    builder = showWhen;
                                                                    num = internalId;
                                                                    str = name;
                                                                    serializedNotifications2 = serializedNotifications;
                                                                    builder2 = serializedMsgs;
                                                                    b2 = b + 1;
                                                                    holders2 = holders;
                                                                    messagesByDialogs2 = messagesByDialogs;
                                                                    size = size2;
                                                                    oldIdsWear2 = oldIdsWear;
                                                                    mainNotification2 = mainNotification;
                                                                    i2 = 0;
                                                                    serializedNotifications3 = serializedNotifications2;
                                                                    sortedDialogs2 = sortedDialogs;
                                                                }
                                                            } catch (JSONException e10) {
                                                                user2 = user;
                                                                builder = showWhen;
                                                                num = internalId;
                                                                str = name;
                                                                serializedNotifications2 = serializedNotifications;
                                                                builder2 = serializedMsgs;
                                                                b2 = b + 1;
                                                                holders2 = holders;
                                                                messagesByDialogs2 = messagesByDialogs;
                                                                size = size2;
                                                                oldIdsWear2 = oldIdsWear;
                                                                mainNotification2 = mainNotification;
                                                                i2 = 0;
                                                                serializedNotifications3 = serializedNotifications2;
                                                                sortedDialogs2 = sortedDialogs;
                                                            }
                                                        } catch (JSONException e11) {
                                                            user2 = user;
                                                            builder = showWhen;
                                                            num = internalId;
                                                            str = name;
                                                            serializedNotifications2 = serializedNotifications;
                                                            size = max_date;
                                                            builder2 = serializedMsgs;
                                                            b2 = b + 1;
                                                            holders2 = holders;
                                                            messagesByDialogs2 = messagesByDialogs;
                                                            size = size2;
                                                            oldIdsWear2 = oldIdsWear;
                                                            mainNotification2 = mainNotification;
                                                            i2 = 0;
                                                            serializedNotifications3 = serializedNotifications2;
                                                            sortedDialogs2 = sortedDialogs;
                                                        }
                                                        try {
                                                            serializedNotifications2.put(jSONObject);
                                                        } catch (JSONException e12) {
                                                            b2 = b + 1;
                                                            holders2 = holders;
                                                            messagesByDialogs2 = messagesByDialogs;
                                                            size = size2;
                                                            oldIdsWear2 = oldIdsWear;
                                                            mainNotification2 = mainNotification;
                                                            i2 = 0;
                                                            serializedNotifications3 = serializedNotifications2;
                                                            sortedDialogs2 = sortedDialogs;
                                                        }
                                                    } catch (JSONException e13) {
                                                        user2 = user;
                                                        builder = showWhen;
                                                        num = internalId;
                                                        str = name;
                                                        serializedNotifications2 = serializedNotifications;
                                                        jSONObject = serializedChat2;
                                                        size = max_date;
                                                        z3 = canReply;
                                                        builder2 = serializedMsgs;
                                                        max_id = max_id2;
                                                        b2 = b + 1;
                                                        holders2 = holders;
                                                        messagesByDialogs2 = messagesByDialogs;
                                                        size = size2;
                                                        oldIdsWear2 = oldIdsWear;
                                                        mainNotification2 = mainNotification;
                                                        i2 = 0;
                                                        serializedNotifications3 = serializedNotifications2;
                                                        sortedDialogs2 = sortedDialogs;
                                                    }
                                                }
                                                b2 = b + 1;
                                                holders2 = holders;
                                                messagesByDialogs2 = messagesByDialogs;
                                                size = size2;
                                                oldIdsWear2 = oldIdsWear;
                                                mainNotification2 = mainNotification;
                                                i2 = 0;
                                                serializedNotifications3 = serializedNotifications2;
                                                sortedDialogs2 = sortedDialogs;
                                            }
                                        } else {
                                            img = messagingStyle2;
                                        }
                                    } catch (Throwable th2) {
                                        img = messagingStyle2;
                                        if (AndroidUtilities.needShowPasscode(false)) {
                                        }
                                        wearableExtender2 = wearableExtender;
                                        j = date;
                                        i4 = rowsMid;
                                        arrayList2 = rows3;
                                        if (chat == null) {
                                        }
                                        user = user5;
                                        if (VERSION.SDK_INT >= 26) {
                                            showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                                        }
                                        internalId = internalId3;
                                        holders = holders3;
                                        holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                                        notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                                        if (serializedChat2 != null) {
                                            jSONObject = serializedChat2;
                                            jSONObject.put("reply", canReply);
                                            jSONObject.put("name", name);
                                            jSONObject.put("max_id", max_id2);
                                            jSONObject.put("max_date", max_date);
                                            jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                                            if (mainNotification2 != null) {
                                                user = "photo";
                                                text = new StringBuilder();
                                                text.append(mainNotification2.dc_id);
                                                text.append("_");
                                                text.append(mainNotification2.volume_id);
                                                text.append("_");
                                                text.append(mainNotification2.secret);
                                                jSONObject.put(user, text.toString());
                                            } else {
                                                num = internalId;
                                                str = name;
                                            }
                                            if (serializedMsgs != null) {
                                                jSONObject.put("msgs", serializedMsgs);
                                            } else {
                                                builder2 = serializedMsgs;
                                            }
                                            if (lowerId <= 0) {
                                                jSONObject.put("type", "user");
                                            } else if (lowerId < 0) {
                                                if (isChannel) {
                                                    if (isSupergroup) {
                                                        jSONObject.put("type", "group");
                                                    }
                                                }
                                                jSONObject.put("type", "channel");
                                            }
                                            serializedNotifications2 = serializedNotifications;
                                            serializedNotifications2.put(jSONObject);
                                        } else {
                                            user2 = user;
                                            builder = showWhen;
                                            num = internalId;
                                            str = name;
                                            serializedNotifications2 = serializedNotifications;
                                            jSONObject = serializedChat2;
                                            size = max_date;
                                            z3 = canReply;
                                            builder2 = serializedMsgs;
                                            max_id = max_id2;
                                        }
                                        b2 = b + 1;
                                        holders2 = holders;
                                        messagesByDialogs2 = messagesByDialogs;
                                        size = size2;
                                        oldIdsWear2 = oldIdsWear;
                                        mainNotification2 = mainNotification;
                                        i2 = 0;
                                        serializedNotifications3 = serializedNotifications2;
                                        sortedDialogs2 = sortedDialogs;
                                    }
                                }
                            } else {
                                unreadConvBuilder = mainNotification2;
                                messagingStyle3 = messagingStyle2;
                                mainNotification2 = photoPath2;
                            }
                            if (AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter || rows3 == null) {
                                wearableExtender2 = wearableExtender;
                                j = date;
                                i4 = rowsMid;
                                arrayList2 = rows3;
                            } else {
                                PendingIntent contentIntent2;
                                highId = 0;
                                ArrayList<TL_keyboardButtonRow> rows4 = rows3;
                                int rc = rows4.size();
                                while (highId < rc) {
                                    TL_keyboardButtonRow row = (TL_keyboardButtonRow) rows4.get(highId);
                                    arrayList2 = rows4;
                                    rows4 = row.buttons.size();
                                    contentIntent2 = contentIntent;
                                    a = 0;
                                    while (a < rows4) {
                                        int rc2;
                                        int cc = rows4;
                                        KeyboardButton rows5 = (KeyboardButton) row.buttons.get(a);
                                        wearableExtender2 = wearableExtender;
                                        if ((rows5 instanceof TL_keyboardButtonCallback) != null) {
                                            j = date;
                                            wearableExtender = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                                            wearableExtender.putExtra("currentAccount", notificationsController.currentAccount);
                                            wearableExtender.putExtra("did", dialog_id2);
                                            if (rows5.data != null) {
                                                wearableExtender.putExtra(DataSchemeDataSource.SCHEME_DATA, rows5.data);
                                            }
                                            size = rowsMid;
                                            wearableExtender.putExtra("mid", size);
                                            CharSequence charSequence = rows5.text;
                                            KeyboardButton button = rows5;
                                            rows4 = ApplicationLoader.applicationContext;
                                            i4 = size;
                                            size = notificationsController.lastButtonId;
                                            rc2 = rc;
                                            notificationsController.lastButtonId = size + 1;
                                            showWhen.addAction(0, charSequence, PendingIntent.getBroadcast(rows4, size, wearableExtender, 134217728));
                                        } else {
                                            j = date;
                                            rc2 = rc;
                                            i4 = rowsMid;
                                        }
                                        a++;
                                        rows4 = cc;
                                        wearableExtender = wearableExtender2;
                                        date = j;
                                        rowsMid = i4;
                                        rc = rc2;
                                    }
                                    i4 = rowsMid;
                                    highId++;
                                    rows4 = arrayList2;
                                    contentIntent = contentIntent2;
                                    date = date;
                                    rc = rc;
                                }
                                contentIntent2 = contentIntent;
                                wearableExtender2 = wearableExtender;
                                j = date;
                                i4 = rowsMid;
                            }
                            if (chat == null || user5 == null) {
                                user = user5;
                            } else {
                                user = user5;
                                if (user.phone != null && user.phone.length() > 0) {
                                    text = new StringBuilder();
                                    text.append("tel:+");
                                    text.append(user.phone);
                                    showWhen.addPerson(text.toString());
                                }
                            }
                            if (VERSION.SDK_INT >= 26) {
                                showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                            }
                            internalId = internalId3;
                            holders = holders3;
                            holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                            notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                            if (serializedChat2 != null) {
                                jSONObject = serializedChat2;
                                jSONObject.put("reply", canReply);
                                jSONObject.put("name", name);
                                jSONObject.put("max_id", max_id2);
                                jSONObject.put("max_date", max_date);
                                jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                                if (mainNotification2 != null) {
                                    user = "photo";
                                    text = new StringBuilder();
                                    text.append(mainNotification2.dc_id);
                                    text.append("_");
                                    text.append(mainNotification2.volume_id);
                                    text.append("_");
                                    text.append(mainNotification2.secret);
                                    jSONObject.put(user, text.toString());
                                } else {
                                    num = internalId;
                                    str = name;
                                }
                                if (serializedMsgs != null) {
                                    jSONObject.put("msgs", serializedMsgs);
                                } else {
                                    builder2 = serializedMsgs;
                                }
                                if (lowerId <= 0) {
                                    jSONObject.put("type", "user");
                                } else if (lowerId < 0) {
                                    if (isChannel) {
                                        if (isSupergroup) {
                                            jSONObject.put("type", "group");
                                        }
                                    }
                                    jSONObject.put("type", "channel");
                                }
                                serializedNotifications2 = serializedNotifications;
                                serializedNotifications2.put(jSONObject);
                            } else {
                                user2 = user;
                                builder = showWhen;
                                num = internalId;
                                str = name;
                                serializedNotifications2 = serializedNotifications;
                                jSONObject = serializedChat2;
                                size = max_date;
                                z3 = canReply;
                                builder2 = serializedMsgs;
                                max_id = max_id2;
                            }
                            b2 = b + 1;
                            holders2 = holders;
                            messagesByDialogs2 = messagesByDialogs;
                            size = size2;
                            oldIdsWear2 = oldIdsWear;
                            mainNotification2 = mainNotification;
                            i2 = 0;
                            serializedNotifications3 = serializedNotifications2;
                            sortedDialogs2 = sortedDialogs;
                        }
                    }
                    if (z2 || SharedConfig.isWaitingForPasscodeEnter) {
                        internalId3 = internalId2;
                        pendingIntent = msgHeardPendingIntent;
                        canReply = z2;
                        action = null;
                        wearReplyAction = action;
                        count = (Integer) notificationsController.pushDialogs.get(dialog_id2);
                        if (count == null) {
                            i = 0;
                        } else {
                            i = 0;
                            count = Integer.valueOf(0);
                        }
                        messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID);
                        objArr = new Object[2];
                        objArr[i] = name2;
                        photoPath2 = photoPath;
                        objArr[1] = LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()));
                        messagingStyle2 = messagingStyle.setConversationTitle(String.format("%1$s (%2$s)", objArr));
                        text = new StringBuilder();
                        isText = new boolean[1];
                        serializedMsgs2 = null;
                        if (serializedChat2 != null) {
                            rows = null;
                        } else {
                            rows = null;
                            serializedMsgs2 = new JSONArray();
                        }
                        i = messageObjects.size() - 1;
                        rowsMid = 0;
                        rows2 = rows;
                        while (true) {
                            rows3 = rows2;
                            if (i < 0) {
                                break;
                            }
                            messageObject3 = (MessageObject) messageObjects.get(i);
                            name3 = name2;
                            messageObjects2 = messageObjects;
                            message = getStringForMessage(messageObject3, null, isText);
                            if (messageObject3.isFcmMessage() != null) {
                            }
                            if (message == null) {
                                if (lowerId < 0) {
                                    action2 = wearReplyAction;
                                    i3 = max_id;
                                    if (isText[null]) {
                                        wearReplyAction = new StringBuilder();
                                        wearReplyAction.append(name2);
                                        wearReplyAction.append(" ");
                                        wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    } else {
                                        wearReplyAction = new StringBuilder();
                                        wearReplyAction.append(name2);
                                        wearReplyAction.append(": ");
                                        wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    }
                                } else {
                                    i3 = max_id;
                                    stringBuilder = new StringBuilder();
                                    action2 = wearReplyAction;
                                    stringBuilder.append(" @ ");
                                    stringBuilder.append(name2);
                                    wearReplyAction = message.replace(stringBuilder.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                }
                                if (text.length() > 0) {
                                    text.append("\n\n");
                                }
                                text.append(wearReplyAction);
                                mainNotification2.addMessage(wearReplyAction);
                                nameToReplace = name2;
                                messagingStyle2.addMessage(wearReplyAction, ((long) messageObject3.messageOwner.date) * 1000, null);
                                if (serializedMsgs2 != null) {
                                    zArr = isText;
                                } else {
                                    name2 = new JSONObject();
                                    name2.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject3));
                                    name2.put("date", messageObject3.messageOwner.date);
                                    sender = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(messageObject3.getFromId()));
                                    if (sender != null) {
                                        zArr = isText;
                                        name2.put("fname", sender.first_name);
                                        name2.put("lname", sender.last_name);
                                        serializedMsgs2.put(name2);
                                    }
                                    zArr = isText;
                                    serializedMsgs2.put(name2);
                                }
                                name2 = messageObject3.messageOwner.reply_markup.rows;
                                isText = messageObject3.getId();
                                rows2 = name2;
                                rowsMid = isText;
                                i--;
                                name2 = name3;
                                messageObjects = messageObjects2;
                                max_id = i3;
                                wearReplyAction = action2;
                                isText = zArr;
                            } else {
                                zArr = isText;
                                action2 = wearReplyAction;
                                i3 = max_id;
                            }
                            rows2 = rows3;
                            i--;
                            name2 = name3;
                            messageObjects = messageObjects2;
                            max_id = i3;
                            wearReplyAction = action2;
                            isText = zArr;
                        }
                        name3 = name2;
                        zArr = isText;
                        action2 = wearReplyAction;
                        messageObjects2 = messageObjects;
                        i3 = max_id;
                        intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("com.tmessages.openchat");
                        stringBuilder2.append(Math.random());
                        stringBuilder2.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent2.setAction(stringBuilder2.toString());
                        intent2.setFlags(32768);
                        if (lowerId != 0) {
                            highId3 = highId2;
                            intent2.putExtra("encId", highId3);
                        } else {
                            if (lowerId > 0) {
                                intent2.putExtra("chatId", -lowerId);
                            } else {
                                intent2.putExtra("userId", lowerId);
                            }
                            highId3 = highId2;
                        }
                        intent2.putExtra("currentAccount", notificationsController.currentAccount);
                        contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                        wearableExtender = new WearableExtender();
                        if (action2 != null) {
                            wearReplyAction = action2;
                        } else {
                            wearReplyAction = action2;
                            wearableExtender.addAction(wearReplyAction);
                        }
                        if (lowerId == 0) {
                            max_id = i3;
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("tgenc");
                            stringBuilder3.append(highId3);
                            stringBuilder3.append("_");
                            stringBuilder3.append(max_id);
                            message = stringBuilder3.toString();
                        } else if (lowerId > 0) {
                            max_id = i3;
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("tgchat");
                            stringBuilder4.append(-lowerId);
                            stringBuilder4.append("_");
                            stringBuilder4.append(max_id);
                            message = stringBuilder4.toString();
                        } else {
                            message = new StringBuilder();
                            message.append("tguser");
                            message.append(lowerId);
                            message.append("_");
                            max_id = i3;
                            message.append(max_id);
                            message = message.toString();
                            intent3 = intent2;
                        }
                        dismissalID = message;
                        wearableExtender.setDismissalId(dismissalID);
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("tgaccount");
                        stringBuilder4.append(UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                        wearableExtender.setBridgeTag(stringBuilder4.toString());
                        summaryExtender = new WearableExtender();
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("summary_");
                        stringBuilder4.append(dismissalID);
                        summaryExtender.setDismissalId(stringBuilder4.toString());
                        notificationBuilder.extend(summaryExtender);
                        messageObjects = messageObjects2;
                        serializedMsgs = serializedMsgs2;
                        date = ((long) ((MessageObject) messageObjects.get(null)).messageOwner.date) * 1000;
                        name = name3;
                        max_id2 = max_id;
                        showWhen = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(notificationsController.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("sdid_");
                        stringBuilder.append(dialog_id2);
                        showWhen = showWhen.setShortcutId(stringBuilder.toString()).setGroupAlertBehavior(1).setStyle(messagingStyle2).setContentIntent(contentIntent).extend(wearableExtender);
                        text = new StringBuilder();
                        text.append(TtmlNode.ANONYMOUS_REGION_ID);
                        text.append(Long.MAX_VALUE - date);
                        showWhen = showWhen.setSortKey(text.toString()).extend(new CarExtender().setUnreadConversation(mainNotification2.build())).setCategory("msg");
                        if (notificationsController.pushDialogs.size() == 1) {
                        }
                        str2 = summary;
                        if (lowerId == 0) {
                            showWhen.setLocalOnly(true);
                        }
                        if (photoPath2 != null) {
                            unreadConvBuilder = mainNotification2;
                            messagingStyle3 = messagingStyle2;
                            mainNotification2 = photoPath2;
                        } else {
                            unreadConvBuilder = mainNotification2;
                            mainNotification2 = photoPath2;
                            messagingStyle2 = ImageLoader.getInstance().getImageFromMemory(mainNotification2, null, "50_50");
                            if (messagingStyle2 != null) {
                                file = FileLoader.getPathToAttach(mainNotification2, true);
                                if (file.exists()) {
                                    img = messagingStyle2;
                                } else {
                                    scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                    img = messagingStyle2;
                                    messagingStyle2 = new Options();
                                    if (scaleFactor < 1.0f) {
                                    }
                                    messagingStyle2.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), messagingStyle2);
                                    if (bitmap != null) {
                                        showWhen.setLargeIcon(bitmap);
                                    }
                                }
                            } else {
                                showWhen.setLargeIcon(messagingStyle2.getBitmap());
                            }
                        }
                        if (AndroidUtilities.needShowPasscode(false)) {
                        }
                        wearableExtender2 = wearableExtender;
                        j = date;
                        i4 = rowsMid;
                        arrayList2 = rows3;
                        if (chat == null) {
                        }
                        user = user5;
                        if (VERSION.SDK_INT >= 26) {
                            showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                        }
                        internalId = internalId3;
                        holders = holders3;
                        holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                        notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                        if (serializedChat2 != null) {
                            user2 = user;
                            builder = showWhen;
                            num = internalId;
                            str = name;
                            serializedNotifications2 = serializedNotifications;
                            jSONObject = serializedChat2;
                            size = max_date;
                            z3 = canReply;
                            builder2 = serializedMsgs;
                            max_id = max_id2;
                        } else {
                            jSONObject = serializedChat2;
                            jSONObject.put("reply", canReply);
                            jSONObject.put("name", name);
                            jSONObject.put("max_id", max_id2);
                            jSONObject.put("max_date", max_date);
                            jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                            if (mainNotification2 != null) {
                                num = internalId;
                                str = name;
                            } else {
                                user = "photo";
                                text = new StringBuilder();
                                text.append(mainNotification2.dc_id);
                                text.append("_");
                                text.append(mainNotification2.volume_id);
                                text.append("_");
                                text.append(mainNotification2.secret);
                                jSONObject.put(user, text.toString());
                            }
                            if (serializedMsgs != null) {
                                builder2 = serializedMsgs;
                            } else {
                                jSONObject.put("msgs", serializedMsgs);
                            }
                            if (lowerId <= 0) {
                                jSONObject.put("type", "user");
                            } else if (lowerId < 0) {
                                if (isChannel) {
                                    if (isSupergroup) {
                                        jSONObject.put("type", "group");
                                    }
                                }
                                jSONObject.put("type", "channel");
                            }
                            serializedNotifications2 = serializedNotifications;
                            serializedNotifications2.put(jSONObject);
                        }
                        b2 = b + 1;
                        holders2 = holders;
                        messagesByDialogs2 = messagesByDialogs;
                        size = size2;
                        oldIdsWear2 = oldIdsWear;
                        mainNotification2 = mainNotification;
                        i2 = 0;
                        serializedNotifications3 = serializedNotifications2;
                        sortedDialogs2 = sortedDialogs;
                    } else {
                        boolean z4;
                        msgHeardIntent = new Intent(ApplicationLoader.applicationContext, AutoMessageReplyReceiver.class);
                        msgHeardIntent.addFlags(32);
                        msgHeardIntent.setAction("org.telegram.messenger.ACTION_MESSAGE_REPLY");
                        msgHeardIntent.putExtra("dialog_id", dialog_id2);
                        msgHeardIntent.putExtra("max_id", max_id);
                        msgHeardIntent.putExtra("currentAccount", notificationsController.currentAccount);
                        action = null;
                        PendingIntent msgReplyPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent, 134217728);
                        Intent msgReplyIntent = msgHeardIntent;
                        RemoteInput remoteInputAuto = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                        mainNotification2.setReplyAction(msgReplyPendingIntent, remoteInputAuto);
                        msgHeardIntent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                        msgHeardIntent.putExtra("dialog_id", dialog_id2);
                        msgHeardIntent.putExtra("max_id", max_id);
                        msgHeardIntent.putExtra("currentAccount", notificationsController.currentAccount);
                        msgReplyPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent, 134217728);
                        Intent replyIntent = msgHeardIntent;
                        msgHeardIntent = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                        if (lowerId < 0) {
                            canReply = z2;
                            name = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, name2);
                            internalId3 = internalId2;
                            z4 = true;
                        } else {
                            canReply = z2;
                            internalId3 = internalId2;
                            z4 = true;
                            name = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, name2);
                        }
                        wearReplyAction = new Action.Builder(R.drawable.ic_reply_icon, name, msgReplyPendingIntent).setAllowGeneratedReplies(z4).addRemoteInput(msgHeardIntent).build();
                        count = (Integer) notificationsController.pushDialogs.get(dialog_id2);
                        if (count == null) {
                            i = 0;
                            count = Integer.valueOf(0);
                        } else {
                            i = 0;
                        }
                        messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID);
                        objArr = new Object[2];
                        objArr[i] = name2;
                        photoPath2 = photoPath;
                        objArr[1] = LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()));
                        messagingStyle2 = messagingStyle.setConversationTitle(String.format("%1$s (%2$s)", objArr));
                        text = new StringBuilder();
                        isText = new boolean[1];
                        serializedMsgs2 = null;
                        if (serializedChat2 != null) {
                            rows = null;
                            serializedMsgs2 = new JSONArray();
                        } else {
                            rows = null;
                        }
                        i = messageObjects.size() - 1;
                        rowsMid = 0;
                        rows2 = rows;
                        while (true) {
                            rows3 = rows2;
                            if (i < 0) {
                                break;
                            }
                            messageObject3 = (MessageObject) messageObjects.get(i);
                            name3 = name2;
                            messageObjects2 = messageObjects;
                            message = getStringForMessage(messageObject3, null, isText);
                            if (messageObject3.isFcmMessage() != null) {
                            }
                            if (message == null) {
                                zArr = isText;
                                action2 = wearReplyAction;
                                i3 = max_id;
                            } else {
                                if (lowerId < 0) {
                                    i3 = max_id;
                                    stringBuilder = new StringBuilder();
                                    action2 = wearReplyAction;
                                    stringBuilder.append(" @ ");
                                    stringBuilder.append(name2);
                                    wearReplyAction = message.replace(stringBuilder.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                } else {
                                    action2 = wearReplyAction;
                                    i3 = max_id;
                                    if (isText[null]) {
                                        wearReplyAction = new StringBuilder();
                                        wearReplyAction.append(name2);
                                        wearReplyAction.append(": ");
                                        wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    } else {
                                        wearReplyAction = new StringBuilder();
                                        wearReplyAction.append(name2);
                                        wearReplyAction.append(" ");
                                        wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    }
                                }
                                if (text.length() > 0) {
                                    text.append("\n\n");
                                }
                                text.append(wearReplyAction);
                                mainNotification2.addMessage(wearReplyAction);
                                nameToReplace = name2;
                                messagingStyle2.addMessage(wearReplyAction, ((long) messageObject3.messageOwner.date) * 1000, null);
                                if (serializedMsgs2 != null) {
                                    name2 = new JSONObject();
                                    name2.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject3));
                                    name2.put("date", messageObject3.messageOwner.date);
                                    sender = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(messageObject3.getFromId()));
                                    if (sender != null) {
                                        zArr = isText;
                                        name2.put("fname", sender.first_name);
                                        name2.put("lname", sender.last_name);
                                        serializedMsgs2.put(name2);
                                    }
                                    zArr = isText;
                                    serializedMsgs2.put(name2);
                                } else {
                                    zArr = isText;
                                }
                                name2 = messageObject3.messageOwner.reply_markup.rows;
                                isText = messageObject3.getId();
                                rows2 = name2;
                                rowsMid = isText;
                                i--;
                                name2 = name3;
                                messageObjects = messageObjects2;
                                max_id = i3;
                                wearReplyAction = action2;
                                isText = zArr;
                            }
                            rows2 = rows3;
                            i--;
                            name2 = name3;
                            messageObjects = messageObjects2;
                            max_id = i3;
                            wearReplyAction = action2;
                            isText = zArr;
                        }
                        name3 = name2;
                        zArr = isText;
                        action2 = wearReplyAction;
                        messageObjects2 = messageObjects;
                        i3 = max_id;
                        intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("com.tmessages.openchat");
                        stringBuilder2.append(Math.random());
                        stringBuilder2.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent2.setAction(stringBuilder2.toString());
                        intent2.setFlags(32768);
                        if (lowerId != 0) {
                            if (lowerId > 0) {
                                intent2.putExtra("userId", lowerId);
                            } else {
                                intent2.putExtra("chatId", -lowerId);
                            }
                            highId3 = highId2;
                        } else {
                            highId3 = highId2;
                            intent2.putExtra("encId", highId3);
                        }
                        intent2.putExtra("currentAccount", notificationsController.currentAccount);
                        contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                        wearableExtender = new WearableExtender();
                        if (action2 != null) {
                            wearReplyAction = action2;
                            wearableExtender.addAction(wearReplyAction);
                        } else {
                            wearReplyAction = action2;
                        }
                        if (lowerId == 0) {
                            max_id = i3;
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("tgenc");
                            stringBuilder3.append(highId3);
                            stringBuilder3.append("_");
                            stringBuilder3.append(max_id);
                            message = stringBuilder3.toString();
                        } else if (lowerId > 0) {
                            message = new StringBuilder();
                            message.append("tguser");
                            message.append(lowerId);
                            message.append("_");
                            max_id = i3;
                            message.append(max_id);
                            message = message.toString();
                            intent3 = intent2;
                        } else {
                            max_id = i3;
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("tgchat");
                            stringBuilder4.append(-lowerId);
                            stringBuilder4.append("_");
                            stringBuilder4.append(max_id);
                            message = stringBuilder4.toString();
                        }
                        dismissalID = message;
                        wearableExtender.setDismissalId(dismissalID);
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("tgaccount");
                        stringBuilder4.append(UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                        wearableExtender.setBridgeTag(stringBuilder4.toString());
                        summaryExtender = new WearableExtender();
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("summary_");
                        stringBuilder4.append(dismissalID);
                        summaryExtender.setDismissalId(stringBuilder4.toString());
                        notificationBuilder.extend(summaryExtender);
                        messageObjects = messageObjects2;
                        serializedMsgs = serializedMsgs2;
                        date = ((long) ((MessageObject) messageObjects.get(null)).messageOwner.date) * 1000;
                        name = name3;
                        max_id2 = max_id;
                        showWhen = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(notificationsController.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("sdid_");
                        stringBuilder.append(dialog_id2);
                        showWhen = showWhen.setShortcutId(stringBuilder.toString()).setGroupAlertBehavior(1).setStyle(messagingStyle2).setContentIntent(contentIntent).extend(wearableExtender);
                        text = new StringBuilder();
                        text.append(TtmlNode.ANONYMOUS_REGION_ID);
                        text.append(Long.MAX_VALUE - date);
                        showWhen = showWhen.setSortKey(text.toString()).extend(new CarExtender().setUnreadConversation(mainNotification2.build())).setCategory("msg");
                        if (notificationsController.pushDialogs.size() == 1) {
                        }
                        str2 = summary;
                        if (lowerId == 0) {
                            showWhen.setLocalOnly(true);
                        }
                        if (photoPath2 != null) {
                            unreadConvBuilder = mainNotification2;
                            mainNotification2 = photoPath2;
                            messagingStyle2 = ImageLoader.getInstance().getImageFromMemory(mainNotification2, null, "50_50");
                            if (messagingStyle2 != null) {
                                showWhen.setLargeIcon(messagingStyle2.getBitmap());
                            } else {
                                file = FileLoader.getPathToAttach(mainNotification2, true);
                                if (file.exists()) {
                                    scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                    img = messagingStyle2;
                                    messagingStyle2 = new Options();
                                    if (scaleFactor < 1.0f) {
                                    }
                                    messagingStyle2.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), messagingStyle2);
                                    if (bitmap != null) {
                                        showWhen.setLargeIcon(bitmap);
                                    }
                                } else {
                                    img = messagingStyle2;
                                }
                            }
                        } else {
                            unreadConvBuilder = mainNotification2;
                            messagingStyle3 = messagingStyle2;
                            mainNotification2 = photoPath2;
                        }
                        if (AndroidUtilities.needShowPasscode(false)) {
                        }
                        wearableExtender2 = wearableExtender;
                        j = date;
                        i4 = rowsMid;
                        arrayList2 = rows3;
                        if (chat == null) {
                        }
                        user = user5;
                        if (VERSION.SDK_INT >= 26) {
                            showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                        }
                        internalId = internalId3;
                        holders = holders3;
                        holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                        notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                        if (serializedChat2 != null) {
                            jSONObject = serializedChat2;
                            jSONObject.put("reply", canReply);
                            jSONObject.put("name", name);
                            jSONObject.put("max_id", max_id2);
                            jSONObject.put("max_date", max_date);
                            jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                            if (mainNotification2 != null) {
                                user = "photo";
                                text = new StringBuilder();
                                text.append(mainNotification2.dc_id);
                                text.append("_");
                                text.append(mainNotification2.volume_id);
                                text.append("_");
                                text.append(mainNotification2.secret);
                                jSONObject.put(user, text.toString());
                            } else {
                                num = internalId;
                                str = name;
                            }
                            if (serializedMsgs != null) {
                                jSONObject.put("msgs", serializedMsgs);
                            } else {
                                builder2 = serializedMsgs;
                            }
                            if (lowerId <= 0) {
                                jSONObject.put("type", "user");
                            } else if (lowerId < 0) {
                                if (isChannel) {
                                    if (isSupergroup) {
                                        jSONObject.put("type", "group");
                                    }
                                }
                                jSONObject.put("type", "channel");
                            }
                            serializedNotifications2 = serializedNotifications;
                            serializedNotifications2.put(jSONObject);
                        } else {
                            user2 = user;
                            builder = showWhen;
                            num = internalId;
                            str = name;
                            serializedNotifications2 = serializedNotifications;
                            jSONObject = serializedChat2;
                            size = max_date;
                            z3 = canReply;
                            builder2 = serializedMsgs;
                            max_id = max_id2;
                        }
                        b2 = b + 1;
                        holders2 = holders;
                        messagesByDialogs2 = messagesByDialogs;
                        size = size2;
                        oldIdsWear2 = oldIdsWear;
                        mainNotification2 = mainNotification;
                        i2 = 0;
                        serializedNotifications3 = serializedNotifications2;
                        sortedDialogs2 = sortedDialogs;
                    }
                }
                name2 = LocaleController.getString("AppName", R.string.AppName);
                fileLocation = null;
                z = false;
                photoPath = fileLocation;
                z2 = z;
                holders3 = holders2;
                serializedChat2 = mainNotification2;
                highId2 = highId;
                mainNotification2 = new UnreadConversation.Builder(name2).setLatestTimestamp(((long) size) * 1000);
                max_date = size;
                msgHeardIntent = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                msgHeardIntent.addFlags(32);
                msgHeardIntent.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                msgHeardIntent.putExtra("dialog_id", dialog_id2);
                msgHeardIntent.putExtra("max_id", max_id);
                msgHeardIntent.putExtra("currentAccount", notificationsController.currentAccount);
                user5 = user4;
                msgHeardPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent, 134217728);
                mainNotification2.setReadPendingIntent(msgHeardPendingIntent);
                if (isChannel) {
                    if (!isSupergroup) {
                        intent = msgHeardIntent;
                        internalId3 = internalId2;
                        pendingIntent = msgHeardPendingIntent;
                        canReply = z2;
                        action = null;
                        wearReplyAction = action;
                        count = (Integer) notificationsController.pushDialogs.get(dialog_id2);
                        if (count == null) {
                            i = 0;
                        } else {
                            i = 0;
                            count = Integer.valueOf(0);
                        }
                        messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID);
                        objArr = new Object[2];
                        objArr[i] = name2;
                        photoPath2 = photoPath;
                        objArr[1] = LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()));
                        messagingStyle2 = messagingStyle.setConversationTitle(String.format("%1$s (%2$s)", objArr));
                        text = new StringBuilder();
                        isText = new boolean[1];
                        serializedMsgs2 = null;
                        if (serializedChat2 != null) {
                            rows = null;
                        } else {
                            rows = null;
                            serializedMsgs2 = new JSONArray();
                        }
                        i = messageObjects.size() - 1;
                        rowsMid = 0;
                        rows2 = rows;
                        while (true) {
                            rows3 = rows2;
                            if (i < 0) {
                                break;
                            }
                            messageObject3 = (MessageObject) messageObjects.get(i);
                            name3 = name2;
                            messageObjects2 = messageObjects;
                            message = getStringForMessage(messageObject3, null, isText);
                            if (messageObject3.isFcmMessage() != null) {
                            }
                            if (message == null) {
                                if (lowerId < 0) {
                                    action2 = wearReplyAction;
                                    i3 = max_id;
                                    if (isText[null]) {
                                        wearReplyAction = new StringBuilder();
                                        wearReplyAction.append(name2);
                                        wearReplyAction.append(" ");
                                        wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    } else {
                                        wearReplyAction = new StringBuilder();
                                        wearReplyAction.append(name2);
                                        wearReplyAction.append(": ");
                                        wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    }
                                } else {
                                    i3 = max_id;
                                    stringBuilder = new StringBuilder();
                                    action2 = wearReplyAction;
                                    stringBuilder.append(" @ ");
                                    stringBuilder.append(name2);
                                    wearReplyAction = message.replace(stringBuilder.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                }
                                if (text.length() > 0) {
                                    text.append("\n\n");
                                }
                                text.append(wearReplyAction);
                                mainNotification2.addMessage(wearReplyAction);
                                nameToReplace = name2;
                                messagingStyle2.addMessage(wearReplyAction, ((long) messageObject3.messageOwner.date) * 1000, null);
                                if (serializedMsgs2 != null) {
                                    zArr = isText;
                                } else {
                                    name2 = new JSONObject();
                                    name2.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject3));
                                    name2.put("date", messageObject3.messageOwner.date);
                                    sender = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(messageObject3.getFromId()));
                                    if (sender != null) {
                                        zArr = isText;
                                        name2.put("fname", sender.first_name);
                                        name2.put("lname", sender.last_name);
                                        serializedMsgs2.put(name2);
                                    }
                                    zArr = isText;
                                    serializedMsgs2.put(name2);
                                }
                                name2 = messageObject3.messageOwner.reply_markup.rows;
                                isText = messageObject3.getId();
                                rows2 = name2;
                                rowsMid = isText;
                                i--;
                                name2 = name3;
                                messageObjects = messageObjects2;
                                max_id = i3;
                                wearReplyAction = action2;
                                isText = zArr;
                            } else {
                                zArr = isText;
                                action2 = wearReplyAction;
                                i3 = max_id;
                            }
                            rows2 = rows3;
                            i--;
                            name2 = name3;
                            messageObjects = messageObjects2;
                            max_id = i3;
                            wearReplyAction = action2;
                            isText = zArr;
                        }
                        name3 = name2;
                        zArr = isText;
                        action2 = wearReplyAction;
                        messageObjects2 = messageObjects;
                        i3 = max_id;
                        intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("com.tmessages.openchat");
                        stringBuilder2.append(Math.random());
                        stringBuilder2.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent2.setAction(stringBuilder2.toString());
                        intent2.setFlags(32768);
                        if (lowerId != 0) {
                            highId3 = highId2;
                            intent2.putExtra("encId", highId3);
                        } else {
                            if (lowerId > 0) {
                                intent2.putExtra("chatId", -lowerId);
                            } else {
                                intent2.putExtra("userId", lowerId);
                            }
                            highId3 = highId2;
                        }
                        intent2.putExtra("currentAccount", notificationsController.currentAccount);
                        contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                        wearableExtender = new WearableExtender();
                        if (action2 != null) {
                            wearReplyAction = action2;
                        } else {
                            wearReplyAction = action2;
                            wearableExtender.addAction(wearReplyAction);
                        }
                        if (lowerId == 0) {
                            max_id = i3;
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("tgenc");
                            stringBuilder3.append(highId3);
                            stringBuilder3.append("_");
                            stringBuilder3.append(max_id);
                            message = stringBuilder3.toString();
                        } else if (lowerId > 0) {
                            max_id = i3;
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("tgchat");
                            stringBuilder4.append(-lowerId);
                            stringBuilder4.append("_");
                            stringBuilder4.append(max_id);
                            message = stringBuilder4.toString();
                        } else {
                            message = new StringBuilder();
                            message.append("tguser");
                            message.append(lowerId);
                            message.append("_");
                            max_id = i3;
                            message.append(max_id);
                            message = message.toString();
                            intent3 = intent2;
                        }
                        dismissalID = message;
                        wearableExtender.setDismissalId(dismissalID);
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("tgaccount");
                        stringBuilder4.append(UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                        wearableExtender.setBridgeTag(stringBuilder4.toString());
                        summaryExtender = new WearableExtender();
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("summary_");
                        stringBuilder4.append(dismissalID);
                        summaryExtender.setDismissalId(stringBuilder4.toString());
                        notificationBuilder.extend(summaryExtender);
                        messageObjects = messageObjects2;
                        serializedMsgs = serializedMsgs2;
                        date = ((long) ((MessageObject) messageObjects.get(null)).messageOwner.date) * 1000;
                        name = name3;
                        max_id2 = max_id;
                        showWhen = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(notificationsController.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("sdid_");
                        stringBuilder.append(dialog_id2);
                        showWhen = showWhen.setShortcutId(stringBuilder.toString()).setGroupAlertBehavior(1).setStyle(messagingStyle2).setContentIntent(contentIntent).extend(wearableExtender);
                        text = new StringBuilder();
                        text.append(TtmlNode.ANONYMOUS_REGION_ID);
                        text.append(Long.MAX_VALUE - date);
                        showWhen = showWhen.setSortKey(text.toString()).extend(new CarExtender().setUnreadConversation(mainNotification2.build())).setCategory("msg");
                        if (notificationsController.pushDialogs.size() == 1) {
                        }
                        str2 = summary;
                        if (lowerId == 0) {
                            showWhen.setLocalOnly(true);
                        }
                        if (photoPath2 != null) {
                            unreadConvBuilder = mainNotification2;
                            messagingStyle3 = messagingStyle2;
                            mainNotification2 = photoPath2;
                        } else {
                            unreadConvBuilder = mainNotification2;
                            mainNotification2 = photoPath2;
                            messagingStyle2 = ImageLoader.getInstance().getImageFromMemory(mainNotification2, null, "50_50");
                            if (messagingStyle2 != null) {
                                file = FileLoader.getPathToAttach(mainNotification2, true);
                                if (file.exists()) {
                                    img = messagingStyle2;
                                } else {
                                    scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                    img = messagingStyle2;
                                    messagingStyle2 = new Options();
                                    if (scaleFactor < 1.0f) {
                                    }
                                    messagingStyle2.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), messagingStyle2);
                                    if (bitmap != null) {
                                        showWhen.setLargeIcon(bitmap);
                                    }
                                }
                            } else {
                                showWhen.setLargeIcon(messagingStyle2.getBitmap());
                            }
                        }
                        if (AndroidUtilities.needShowPasscode(false)) {
                        }
                        wearableExtender2 = wearableExtender;
                        j = date;
                        i4 = rowsMid;
                        arrayList2 = rows3;
                        if (chat == null) {
                        }
                        user = user5;
                        if (VERSION.SDK_INT >= 26) {
                            showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                        }
                        internalId = internalId3;
                        holders = holders3;
                        holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                        notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                        if (serializedChat2 != null) {
                            user2 = user;
                            builder = showWhen;
                            num = internalId;
                            str = name;
                            serializedNotifications2 = serializedNotifications;
                            jSONObject = serializedChat2;
                            size = max_date;
                            z3 = canReply;
                            builder2 = serializedMsgs;
                            max_id = max_id2;
                        } else {
                            jSONObject = serializedChat2;
                            jSONObject.put("reply", canReply);
                            jSONObject.put("name", name);
                            jSONObject.put("max_id", max_id2);
                            jSONObject.put("max_date", max_date);
                            jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                            if (mainNotification2 != null) {
                                num = internalId;
                                str = name;
                            } else {
                                user = "photo";
                                text = new StringBuilder();
                                text.append(mainNotification2.dc_id);
                                text.append("_");
                                text.append(mainNotification2.volume_id);
                                text.append("_");
                                text.append(mainNotification2.secret);
                                jSONObject.put(user, text.toString());
                            }
                            if (serializedMsgs != null) {
                                builder2 = serializedMsgs;
                            } else {
                                jSONObject.put("msgs", serializedMsgs);
                            }
                            if (lowerId <= 0) {
                                jSONObject.put("type", "user");
                            } else if (lowerId < 0) {
                                if (isChannel) {
                                    if (isSupergroup) {
                                        jSONObject.put("type", "group");
                                    }
                                }
                                jSONObject.put("type", "channel");
                            }
                            serializedNotifications2 = serializedNotifications;
                            serializedNotifications2.put(jSONObject);
                        }
                        b2 = b + 1;
                        holders2 = holders;
                        messagesByDialogs2 = messagesByDialogs;
                        size = size2;
                        oldIdsWear2 = oldIdsWear;
                        mainNotification2 = mainNotification;
                        i2 = 0;
                        serializedNotifications3 = serializedNotifications2;
                        sortedDialogs2 = sortedDialogs;
                    }
                }
                if (z2) {
                }
                internalId3 = internalId2;
                pendingIntent = msgHeardPendingIntent;
                canReply = z2;
                action = null;
                wearReplyAction = action;
                count = (Integer) notificationsController.pushDialogs.get(dialog_id2);
                if (count == null) {
                    i = 0;
                    count = Integer.valueOf(0);
                } else {
                    i = 0;
                }
                messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID);
                objArr = new Object[2];
                objArr[i] = name2;
                photoPath2 = photoPath;
                objArr[1] = LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()));
                messagingStyle2 = messagingStyle.setConversationTitle(String.format("%1$s (%2$s)", objArr));
                text = new StringBuilder();
                isText = new boolean[1];
                serializedMsgs2 = null;
                if (serializedChat2 != null) {
                    rows = null;
                    serializedMsgs2 = new JSONArray();
                } else {
                    rows = null;
                }
                i = messageObjects.size() - 1;
                rowsMid = 0;
                rows2 = rows;
                while (true) {
                    rows3 = rows2;
                    if (i < 0) {
                        break;
                    }
                    messageObject3 = (MessageObject) messageObjects.get(i);
                    name3 = name2;
                    messageObjects2 = messageObjects;
                    message = getStringForMessage(messageObject3, null, isText);
                    if (messageObject3.isFcmMessage() != null) {
                    }
                    if (message == null) {
                        zArr = isText;
                        action2 = wearReplyAction;
                        i3 = max_id;
                    } else {
                        if (lowerId < 0) {
                            i3 = max_id;
                            stringBuilder = new StringBuilder();
                            action2 = wearReplyAction;
                            stringBuilder.append(" @ ");
                            stringBuilder.append(name2);
                            wearReplyAction = message.replace(stringBuilder.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                        } else {
                            action2 = wearReplyAction;
                            i3 = max_id;
                            if (isText[null]) {
                                wearReplyAction = new StringBuilder();
                                wearReplyAction.append(name2);
                                wearReplyAction.append(": ");
                                wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                            } else {
                                wearReplyAction = new StringBuilder();
                                wearReplyAction.append(name2);
                                wearReplyAction.append(" ");
                                wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                            }
                        }
                        if (text.length() > 0) {
                            text.append("\n\n");
                        }
                        text.append(wearReplyAction);
                        mainNotification2.addMessage(wearReplyAction);
                        nameToReplace = name2;
                        messagingStyle2.addMessage(wearReplyAction, ((long) messageObject3.messageOwner.date) * 1000, null);
                        if (serializedMsgs2 != null) {
                            name2 = new JSONObject();
                            name2.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject3));
                            name2.put("date", messageObject3.messageOwner.date);
                            sender = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(messageObject3.getFromId()));
                            if (sender != null) {
                                zArr = isText;
                                name2.put("fname", sender.first_name);
                                name2.put("lname", sender.last_name);
                                serializedMsgs2.put(name2);
                            }
                            zArr = isText;
                            serializedMsgs2.put(name2);
                        } else {
                            zArr = isText;
                        }
                        name2 = messageObject3.messageOwner.reply_markup.rows;
                        isText = messageObject3.getId();
                        rows2 = name2;
                        rowsMid = isText;
                        i--;
                        name2 = name3;
                        messageObjects = messageObjects2;
                        max_id = i3;
                        wearReplyAction = action2;
                        isText = zArr;
                    }
                    rows2 = rows3;
                    i--;
                    name2 = name3;
                    messageObjects = messageObjects2;
                    max_id = i3;
                    wearReplyAction = action2;
                    isText = zArr;
                }
                name3 = name2;
                zArr = isText;
                action2 = wearReplyAction;
                messageObjects2 = messageObjects;
                i3 = max_id;
                intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("com.tmessages.openchat");
                stringBuilder2.append(Math.random());
                stringBuilder2.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                intent2.setAction(stringBuilder2.toString());
                intent2.setFlags(32768);
                if (lowerId != 0) {
                    if (lowerId > 0) {
                        intent2.putExtra("userId", lowerId);
                    } else {
                        intent2.putExtra("chatId", -lowerId);
                    }
                    highId3 = highId2;
                } else {
                    highId3 = highId2;
                    intent2.putExtra("encId", highId3);
                }
                intent2.putExtra("currentAccount", notificationsController.currentAccount);
                contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                wearableExtender = new WearableExtender();
                if (action2 != null) {
                    wearReplyAction = action2;
                    wearableExtender.addAction(wearReplyAction);
                } else {
                    wearReplyAction = action2;
                }
                if (lowerId == 0) {
                    max_id = i3;
                    stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("tgenc");
                    stringBuilder3.append(highId3);
                    stringBuilder3.append("_");
                    stringBuilder3.append(max_id);
                    message = stringBuilder3.toString();
                } else if (lowerId > 0) {
                    message = new StringBuilder();
                    message.append("tguser");
                    message.append(lowerId);
                    message.append("_");
                    max_id = i3;
                    message.append(max_id);
                    message = message.toString();
                    intent3 = intent2;
                } else {
                    max_id = i3;
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("tgchat");
                    stringBuilder4.append(-lowerId);
                    stringBuilder4.append("_");
                    stringBuilder4.append(max_id);
                    message = stringBuilder4.toString();
                }
                dismissalID = message;
                wearableExtender.setDismissalId(dismissalID);
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append("tgaccount");
                stringBuilder4.append(UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                wearableExtender.setBridgeTag(stringBuilder4.toString());
                summaryExtender = new WearableExtender();
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append("summary_");
                stringBuilder4.append(dismissalID);
                summaryExtender.setDismissalId(stringBuilder4.toString());
                notificationBuilder.extend(summaryExtender);
                messageObjects = messageObjects2;
                serializedMsgs = serializedMsgs2;
                date = ((long) ((MessageObject) messageObjects.get(null)).messageOwner.date) * 1000;
                name = name3;
                max_id2 = max_id;
                showWhen = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(notificationsController.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true);
                stringBuilder = new StringBuilder();
                stringBuilder.append("sdid_");
                stringBuilder.append(dialog_id2);
                showWhen = showWhen.setShortcutId(stringBuilder.toString()).setGroupAlertBehavior(1).setStyle(messagingStyle2).setContentIntent(contentIntent).extend(wearableExtender);
                text = new StringBuilder();
                text.append(TtmlNode.ANONYMOUS_REGION_ID);
                text.append(Long.MAX_VALUE - date);
                showWhen = showWhen.setSortKey(text.toString()).extend(new CarExtender().setUnreadConversation(mainNotification2.build())).setCategory("msg");
                if (notificationsController.pushDialogs.size() == 1) {
                }
                str2 = summary;
                if (lowerId == 0) {
                    showWhen.setLocalOnly(true);
                }
                if (photoPath2 != null) {
                    unreadConvBuilder = mainNotification2;
                    mainNotification2 = photoPath2;
                    messagingStyle2 = ImageLoader.getInstance().getImageFromMemory(mainNotification2, null, "50_50");
                    if (messagingStyle2 != null) {
                        showWhen.setLargeIcon(messagingStyle2.getBitmap());
                    } else {
                        file = FileLoader.getPathToAttach(mainNotification2, true);
                        if (file.exists()) {
                            scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                            img = messagingStyle2;
                            messagingStyle2 = new Options();
                            if (scaleFactor < 1.0f) {
                            }
                            messagingStyle2.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), messagingStyle2);
                            if (bitmap != null) {
                                showWhen.setLargeIcon(bitmap);
                            }
                        } else {
                            img = messagingStyle2;
                        }
                    }
                } else {
                    unreadConvBuilder = mainNotification2;
                    messagingStyle3 = messagingStyle2;
                    mainNotification2 = photoPath2;
                }
                if (AndroidUtilities.needShowPasscode(false)) {
                }
                wearableExtender2 = wearableExtender;
                j = date;
                i4 = rowsMid;
                arrayList2 = rows3;
                if (chat == null) {
                }
                user = user5;
                if (VERSION.SDK_INT >= 26) {
                    showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                }
                internalId = internalId3;
                holders = holders3;
                holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                if (serializedChat2 != null) {
                    jSONObject = serializedChat2;
                    jSONObject.put("reply", canReply);
                    jSONObject.put("name", name);
                    jSONObject.put("max_id", max_id2);
                    jSONObject.put("max_date", max_date);
                    jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                    if (mainNotification2 != null) {
                        user = "photo";
                        text = new StringBuilder();
                        text.append(mainNotification2.dc_id);
                        text.append("_");
                        text.append(mainNotification2.volume_id);
                        text.append("_");
                        text.append(mainNotification2.secret);
                        jSONObject.put(user, text.toString());
                    } else {
                        num = internalId;
                        str = name;
                    }
                    if (serializedMsgs != null) {
                        jSONObject.put("msgs", serializedMsgs);
                    } else {
                        builder2 = serializedMsgs;
                    }
                    if (lowerId <= 0) {
                        jSONObject.put("type", "user");
                    } else if (lowerId < 0) {
                        if (isChannel) {
                            if (isSupergroup) {
                                jSONObject.put("type", "group");
                            }
                        }
                        jSONObject.put("type", "channel");
                    }
                    serializedNotifications2 = serializedNotifications;
                    serializedNotifications2.put(jSONObject);
                } else {
                    user2 = user;
                    builder = showWhen;
                    num = internalId;
                    str = name;
                    serializedNotifications2 = serializedNotifications;
                    jSONObject = serializedChat2;
                    size = max_date;
                    z3 = canReply;
                    builder2 = serializedMsgs;
                    max_id = max_id2;
                }
                b2 = b + 1;
                holders2 = holders;
                messagesByDialogs2 = messagesByDialogs;
                size = size2;
                oldIdsWear2 = oldIdsWear;
                mainNotification2 = mainNotification;
                i2 = 0;
                serializedNotifications3 = serializedNotifications2;
                sortedDialogs2 = sortedDialogs;
            } else {
                mainNotification = mainNotification2;
                messageObject2 = lastMessageObject;
                serializedNotifications = serializedNotifications3;
                b = b2;
                z = false;
                mainNotification2 = MessagesController.getInstance(notificationsController.currentAccount).getEncryptedChat(Integer.valueOf(highId));
                if (mainNotification2 != null) {
                    user3 = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(mainNotification2.user_id));
                    if (user3 != null) {
                        name2 = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                        fileLocation = null;
                        serializedChat = null;
                        mainNotification2 = serializedChat;
                        user4 = user3;
                        if (AndroidUtilities.needShowPasscode(false)) {
                            if (SharedConfig.isWaitingForPasscodeEnter) {
                            }
                            photoPath = fileLocation;
                            z2 = z;
                            holders3 = holders2;
                            serializedChat2 = mainNotification2;
                            highId2 = highId;
                            mainNotification2 = new UnreadConversation.Builder(name2).setLatestTimestamp(((long) size) * 1000);
                            max_date = size;
                            msgHeardIntent = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                            msgHeardIntent.addFlags(32);
                            msgHeardIntent.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                            msgHeardIntent.putExtra("dialog_id", dialog_id2);
                            msgHeardIntent.putExtra("max_id", max_id);
                            msgHeardIntent.putExtra("currentAccount", notificationsController.currentAccount);
                            user5 = user4;
                            msgHeardPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent, 134217728);
                            mainNotification2.setReadPendingIntent(msgHeardPendingIntent);
                            if (isChannel) {
                                if (!isSupergroup) {
                                    intent = msgHeardIntent;
                                    internalId3 = internalId2;
                                    pendingIntent = msgHeardPendingIntent;
                                    canReply = z2;
                                    action = null;
                                    wearReplyAction = action;
                                    count = (Integer) notificationsController.pushDialogs.get(dialog_id2);
                                    if (count == null) {
                                        i = 0;
                                    } else {
                                        i = 0;
                                        count = Integer.valueOf(0);
                                    }
                                    messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID);
                                    objArr = new Object[2];
                                    objArr[i] = name2;
                                    photoPath2 = photoPath;
                                    objArr[1] = LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()));
                                    messagingStyle2 = messagingStyle.setConversationTitle(String.format("%1$s (%2$s)", objArr));
                                    text = new StringBuilder();
                                    isText = new boolean[1];
                                    serializedMsgs2 = null;
                                    if (serializedChat2 != null) {
                                        rows = null;
                                    } else {
                                        rows = null;
                                        serializedMsgs2 = new JSONArray();
                                    }
                                    i = messageObjects.size() - 1;
                                    rowsMid = 0;
                                    rows2 = rows;
                                    while (true) {
                                        rows3 = rows2;
                                        if (i < 0) {
                                            break;
                                        }
                                        messageObject3 = (MessageObject) messageObjects.get(i);
                                        name3 = name2;
                                        messageObjects2 = messageObjects;
                                        message = getStringForMessage(messageObject3, null, isText);
                                        if (messageObject3.isFcmMessage() != null) {
                                        }
                                        if (message == null) {
                                            if (lowerId < 0) {
                                                action2 = wearReplyAction;
                                                i3 = max_id;
                                                if (isText[null]) {
                                                    wearReplyAction = new StringBuilder();
                                                    wearReplyAction.append(name2);
                                                    wearReplyAction.append(" ");
                                                    wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                                } else {
                                                    wearReplyAction = new StringBuilder();
                                                    wearReplyAction.append(name2);
                                                    wearReplyAction.append(": ");
                                                    wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                                }
                                            } else {
                                                i3 = max_id;
                                                stringBuilder = new StringBuilder();
                                                action2 = wearReplyAction;
                                                stringBuilder.append(" @ ");
                                                stringBuilder.append(name2);
                                                wearReplyAction = message.replace(stringBuilder.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                            }
                                            if (text.length() > 0) {
                                                text.append("\n\n");
                                            }
                                            text.append(wearReplyAction);
                                            mainNotification2.addMessage(wearReplyAction);
                                            nameToReplace = name2;
                                            messagingStyle2.addMessage(wearReplyAction, ((long) messageObject3.messageOwner.date) * 1000, null);
                                            if (serializedMsgs2 != null) {
                                                zArr = isText;
                                            } else {
                                                name2 = new JSONObject();
                                                name2.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject3));
                                                name2.put("date", messageObject3.messageOwner.date);
                                                sender = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(messageObject3.getFromId()));
                                                if (sender != null) {
                                                    zArr = isText;
                                                    name2.put("fname", sender.first_name);
                                                    name2.put("lname", sender.last_name);
                                                    serializedMsgs2.put(name2);
                                                }
                                                zArr = isText;
                                                serializedMsgs2.put(name2);
                                            }
                                            name2 = messageObject3.messageOwner.reply_markup.rows;
                                            isText = messageObject3.getId();
                                            rows2 = name2;
                                            rowsMid = isText;
                                            i--;
                                            name2 = name3;
                                            messageObjects = messageObjects2;
                                            max_id = i3;
                                            wearReplyAction = action2;
                                            isText = zArr;
                                        } else {
                                            zArr = isText;
                                            action2 = wearReplyAction;
                                            i3 = max_id;
                                        }
                                        rows2 = rows3;
                                        i--;
                                        name2 = name3;
                                        messageObjects = messageObjects2;
                                        max_id = i3;
                                        wearReplyAction = action2;
                                        isText = zArr;
                                    }
                                    name3 = name2;
                                    zArr = isText;
                                    action2 = wearReplyAction;
                                    messageObjects2 = messageObjects;
                                    i3 = max_id;
                                    intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append("com.tmessages.openchat");
                                    stringBuilder2.append(Math.random());
                                    stringBuilder2.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                    intent2.setAction(stringBuilder2.toString());
                                    intent2.setFlags(32768);
                                    if (lowerId != 0) {
                                        highId3 = highId2;
                                        intent2.putExtra("encId", highId3);
                                    } else {
                                        if (lowerId > 0) {
                                            intent2.putExtra("chatId", -lowerId);
                                        } else {
                                            intent2.putExtra("userId", lowerId);
                                        }
                                        highId3 = highId2;
                                    }
                                    intent2.putExtra("currentAccount", notificationsController.currentAccount);
                                    contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                                    wearableExtender = new WearableExtender();
                                    if (action2 != null) {
                                        wearReplyAction = action2;
                                    } else {
                                        wearReplyAction = action2;
                                        wearableExtender.addAction(wearReplyAction);
                                    }
                                    if (lowerId == 0) {
                                        max_id = i3;
                                        stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append("tgenc");
                                        stringBuilder3.append(highId3);
                                        stringBuilder3.append("_");
                                        stringBuilder3.append(max_id);
                                        message = stringBuilder3.toString();
                                    } else if (lowerId > 0) {
                                        max_id = i3;
                                        stringBuilder4 = new StringBuilder();
                                        stringBuilder4.append("tgchat");
                                        stringBuilder4.append(-lowerId);
                                        stringBuilder4.append("_");
                                        stringBuilder4.append(max_id);
                                        message = stringBuilder4.toString();
                                    } else {
                                        message = new StringBuilder();
                                        message.append("tguser");
                                        message.append(lowerId);
                                        message.append("_");
                                        max_id = i3;
                                        message.append(max_id);
                                        message = message.toString();
                                        intent3 = intent2;
                                    }
                                    dismissalID = message;
                                    wearableExtender.setDismissalId(dismissalID);
                                    stringBuilder4 = new StringBuilder();
                                    stringBuilder4.append("tgaccount");
                                    stringBuilder4.append(UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                                    wearableExtender.setBridgeTag(stringBuilder4.toString());
                                    summaryExtender = new WearableExtender();
                                    stringBuilder4 = new StringBuilder();
                                    stringBuilder4.append("summary_");
                                    stringBuilder4.append(dismissalID);
                                    summaryExtender.setDismissalId(stringBuilder4.toString());
                                    notificationBuilder.extend(summaryExtender);
                                    messageObjects = messageObjects2;
                                    serializedMsgs = serializedMsgs2;
                                    date = ((long) ((MessageObject) messageObjects.get(null)).messageOwner.date) * 1000;
                                    name = name3;
                                    max_id2 = max_id;
                                    showWhen = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(notificationsController.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true);
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("sdid_");
                                    stringBuilder.append(dialog_id2);
                                    showWhen = showWhen.setShortcutId(stringBuilder.toString()).setGroupAlertBehavior(1).setStyle(messagingStyle2).setContentIntent(contentIntent).extend(wearableExtender);
                                    text = new StringBuilder();
                                    text.append(TtmlNode.ANONYMOUS_REGION_ID);
                                    text.append(Long.MAX_VALUE - date);
                                    showWhen = showWhen.setSortKey(text.toString()).extend(new CarExtender().setUnreadConversation(mainNotification2.build())).setCategory("msg");
                                    if (notificationsController.pushDialogs.size() == 1) {
                                    }
                                    str2 = summary;
                                    if (lowerId == 0) {
                                        showWhen.setLocalOnly(true);
                                    }
                                    if (photoPath2 != null) {
                                        unreadConvBuilder = mainNotification2;
                                        messagingStyle3 = messagingStyle2;
                                        mainNotification2 = photoPath2;
                                    } else {
                                        unreadConvBuilder = mainNotification2;
                                        mainNotification2 = photoPath2;
                                        messagingStyle2 = ImageLoader.getInstance().getImageFromMemory(mainNotification2, null, "50_50");
                                        if (messagingStyle2 != null) {
                                            file = FileLoader.getPathToAttach(mainNotification2, true);
                                            if (file.exists()) {
                                                img = messagingStyle2;
                                            } else {
                                                scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                                img = messagingStyle2;
                                                messagingStyle2 = new Options();
                                                if (scaleFactor < 1.0f) {
                                                }
                                                messagingStyle2.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), messagingStyle2);
                                                if (bitmap != null) {
                                                    showWhen.setLargeIcon(bitmap);
                                                }
                                            }
                                        } else {
                                            showWhen.setLargeIcon(messagingStyle2.getBitmap());
                                        }
                                    }
                                    if (AndroidUtilities.needShowPasscode(false)) {
                                    }
                                    wearableExtender2 = wearableExtender;
                                    j = date;
                                    i4 = rowsMid;
                                    arrayList2 = rows3;
                                    if (chat == null) {
                                    }
                                    user = user5;
                                    if (VERSION.SDK_INT >= 26) {
                                        showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                                    }
                                    internalId = internalId3;
                                    holders = holders3;
                                    holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                                    notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                                    if (serializedChat2 != null) {
                                        user2 = user;
                                        builder = showWhen;
                                        num = internalId;
                                        str = name;
                                        serializedNotifications2 = serializedNotifications;
                                        jSONObject = serializedChat2;
                                        size = max_date;
                                        z3 = canReply;
                                        builder2 = serializedMsgs;
                                        max_id = max_id2;
                                    } else {
                                        jSONObject = serializedChat2;
                                        jSONObject.put("reply", canReply);
                                        jSONObject.put("name", name);
                                        jSONObject.put("max_id", max_id2);
                                        jSONObject.put("max_date", max_date);
                                        jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                                        if (mainNotification2 != null) {
                                            num = internalId;
                                            str = name;
                                        } else {
                                            user = "photo";
                                            text = new StringBuilder();
                                            text.append(mainNotification2.dc_id);
                                            text.append("_");
                                            text.append(mainNotification2.volume_id);
                                            text.append("_");
                                            text.append(mainNotification2.secret);
                                            jSONObject.put(user, text.toString());
                                        }
                                        if (serializedMsgs != null) {
                                            builder2 = serializedMsgs;
                                        } else {
                                            jSONObject.put("msgs", serializedMsgs);
                                        }
                                        if (lowerId <= 0) {
                                            jSONObject.put("type", "user");
                                        } else if (lowerId < 0) {
                                            if (isChannel) {
                                                if (isSupergroup) {
                                                    jSONObject.put("type", "group");
                                                }
                                            }
                                            jSONObject.put("type", "channel");
                                        }
                                        serializedNotifications2 = serializedNotifications;
                                        serializedNotifications2.put(jSONObject);
                                    }
                                    b2 = b + 1;
                                    holders2 = holders;
                                    messagesByDialogs2 = messagesByDialogs;
                                    size = size2;
                                    oldIdsWear2 = oldIdsWear;
                                    mainNotification2 = mainNotification;
                                    i2 = 0;
                                    serializedNotifications3 = serializedNotifications2;
                                    sortedDialogs2 = sortedDialogs;
                                }
                            }
                            if (z2) {
                            }
                            internalId3 = internalId2;
                            pendingIntent = msgHeardPendingIntent;
                            canReply = z2;
                            action = null;
                            wearReplyAction = action;
                            count = (Integer) notificationsController.pushDialogs.get(dialog_id2);
                            if (count == null) {
                                i = 0;
                                count = Integer.valueOf(0);
                            } else {
                                i = 0;
                            }
                            messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID);
                            objArr = new Object[2];
                            objArr[i] = name2;
                            photoPath2 = photoPath;
                            objArr[1] = LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()));
                            messagingStyle2 = messagingStyle.setConversationTitle(String.format("%1$s (%2$s)", objArr));
                            text = new StringBuilder();
                            isText = new boolean[1];
                            serializedMsgs2 = null;
                            if (serializedChat2 != null) {
                                rows = null;
                                serializedMsgs2 = new JSONArray();
                            } else {
                                rows = null;
                            }
                            i = messageObjects.size() - 1;
                            rowsMid = 0;
                            rows2 = rows;
                            while (true) {
                                rows3 = rows2;
                                if (i < 0) {
                                    break;
                                }
                                messageObject3 = (MessageObject) messageObjects.get(i);
                                name3 = name2;
                                messageObjects2 = messageObjects;
                                message = getStringForMessage(messageObject3, null, isText);
                                if (messageObject3.isFcmMessage() != null) {
                                }
                                if (message == null) {
                                    zArr = isText;
                                    action2 = wearReplyAction;
                                    i3 = max_id;
                                } else {
                                    if (lowerId < 0) {
                                        i3 = max_id;
                                        stringBuilder = new StringBuilder();
                                        action2 = wearReplyAction;
                                        stringBuilder.append(" @ ");
                                        stringBuilder.append(name2);
                                        wearReplyAction = message.replace(stringBuilder.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    } else {
                                        action2 = wearReplyAction;
                                        i3 = max_id;
                                        if (isText[null]) {
                                            wearReplyAction = new StringBuilder();
                                            wearReplyAction.append(name2);
                                            wearReplyAction.append(": ");
                                            wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                        } else {
                                            wearReplyAction = new StringBuilder();
                                            wearReplyAction.append(name2);
                                            wearReplyAction.append(" ");
                                            wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                        }
                                    }
                                    if (text.length() > 0) {
                                        text.append("\n\n");
                                    }
                                    text.append(wearReplyAction);
                                    mainNotification2.addMessage(wearReplyAction);
                                    nameToReplace = name2;
                                    messagingStyle2.addMessage(wearReplyAction, ((long) messageObject3.messageOwner.date) * 1000, null);
                                    if (serializedMsgs2 != null) {
                                        name2 = new JSONObject();
                                        name2.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject3));
                                        name2.put("date", messageObject3.messageOwner.date);
                                        sender = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(messageObject3.getFromId()));
                                        if (sender != null) {
                                            zArr = isText;
                                            name2.put("fname", sender.first_name);
                                            name2.put("lname", sender.last_name);
                                            serializedMsgs2.put(name2);
                                        }
                                        zArr = isText;
                                        serializedMsgs2.put(name2);
                                    } else {
                                        zArr = isText;
                                    }
                                    name2 = messageObject3.messageOwner.reply_markup.rows;
                                    isText = messageObject3.getId();
                                    rows2 = name2;
                                    rowsMid = isText;
                                    i--;
                                    name2 = name3;
                                    messageObjects = messageObjects2;
                                    max_id = i3;
                                    wearReplyAction = action2;
                                    isText = zArr;
                                }
                                rows2 = rows3;
                                i--;
                                name2 = name3;
                                messageObjects = messageObjects2;
                                max_id = i3;
                                wearReplyAction = action2;
                                isText = zArr;
                            }
                            name3 = name2;
                            zArr = isText;
                            action2 = wearReplyAction;
                            messageObjects2 = messageObjects;
                            i3 = max_id;
                            intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append("com.tmessages.openchat");
                            stringBuilder2.append(Math.random());
                            stringBuilder2.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                            intent2.setAction(stringBuilder2.toString());
                            intent2.setFlags(32768);
                            if (lowerId != 0) {
                                if (lowerId > 0) {
                                    intent2.putExtra("userId", lowerId);
                                } else {
                                    intent2.putExtra("chatId", -lowerId);
                                }
                                highId3 = highId2;
                            } else {
                                highId3 = highId2;
                                intent2.putExtra("encId", highId3);
                            }
                            intent2.putExtra("currentAccount", notificationsController.currentAccount);
                            contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                            wearableExtender = new WearableExtender();
                            if (action2 != null) {
                                wearReplyAction = action2;
                                wearableExtender.addAction(wearReplyAction);
                            } else {
                                wearReplyAction = action2;
                            }
                            if (lowerId == 0) {
                                max_id = i3;
                                stringBuilder3 = new StringBuilder();
                                stringBuilder3.append("tgenc");
                                stringBuilder3.append(highId3);
                                stringBuilder3.append("_");
                                stringBuilder3.append(max_id);
                                message = stringBuilder3.toString();
                            } else if (lowerId > 0) {
                                message = new StringBuilder();
                                message.append("tguser");
                                message.append(lowerId);
                                message.append("_");
                                max_id = i3;
                                message.append(max_id);
                                message = message.toString();
                                intent3 = intent2;
                            } else {
                                max_id = i3;
                                stringBuilder4 = new StringBuilder();
                                stringBuilder4.append("tgchat");
                                stringBuilder4.append(-lowerId);
                                stringBuilder4.append("_");
                                stringBuilder4.append(max_id);
                                message = stringBuilder4.toString();
                            }
                            dismissalID = message;
                            wearableExtender.setDismissalId(dismissalID);
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("tgaccount");
                            stringBuilder4.append(UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                            wearableExtender.setBridgeTag(stringBuilder4.toString());
                            summaryExtender = new WearableExtender();
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("summary_");
                            stringBuilder4.append(dismissalID);
                            summaryExtender.setDismissalId(stringBuilder4.toString());
                            notificationBuilder.extend(summaryExtender);
                            messageObjects = messageObjects2;
                            serializedMsgs = serializedMsgs2;
                            date = ((long) ((MessageObject) messageObjects.get(null)).messageOwner.date) * 1000;
                            name = name3;
                            max_id2 = max_id;
                            showWhen = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(notificationsController.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true);
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("sdid_");
                            stringBuilder.append(dialog_id2);
                            showWhen = showWhen.setShortcutId(stringBuilder.toString()).setGroupAlertBehavior(1).setStyle(messagingStyle2).setContentIntent(contentIntent).extend(wearableExtender);
                            text = new StringBuilder();
                            text.append(TtmlNode.ANONYMOUS_REGION_ID);
                            text.append(Long.MAX_VALUE - date);
                            showWhen = showWhen.setSortKey(text.toString()).extend(new CarExtender().setUnreadConversation(mainNotification2.build())).setCategory("msg");
                            if (notificationsController.pushDialogs.size() == 1) {
                            }
                            str2 = summary;
                            if (lowerId == 0) {
                                showWhen.setLocalOnly(true);
                            }
                            if (photoPath2 != null) {
                                unreadConvBuilder = mainNotification2;
                                mainNotification2 = photoPath2;
                                messagingStyle2 = ImageLoader.getInstance().getImageFromMemory(mainNotification2, null, "50_50");
                                if (messagingStyle2 != null) {
                                    showWhen.setLargeIcon(messagingStyle2.getBitmap());
                                } else {
                                    file = FileLoader.getPathToAttach(mainNotification2, true);
                                    if (file.exists()) {
                                        scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                        img = messagingStyle2;
                                        messagingStyle2 = new Options();
                                        if (scaleFactor < 1.0f) {
                                        }
                                        messagingStyle2.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), messagingStyle2);
                                        if (bitmap != null) {
                                            showWhen.setLargeIcon(bitmap);
                                        }
                                    } else {
                                        img = messagingStyle2;
                                    }
                                }
                            } else {
                                unreadConvBuilder = mainNotification2;
                                messagingStyle3 = messagingStyle2;
                                mainNotification2 = photoPath2;
                            }
                            if (AndroidUtilities.needShowPasscode(false)) {
                            }
                            wearableExtender2 = wearableExtender;
                            j = date;
                            i4 = rowsMid;
                            arrayList2 = rows3;
                            if (chat == null) {
                            }
                            user = user5;
                            if (VERSION.SDK_INT >= 26) {
                                showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                            }
                            internalId = internalId3;
                            holders = holders3;
                            holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                            notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                            if (serializedChat2 != null) {
                                jSONObject = serializedChat2;
                                jSONObject.put("reply", canReply);
                                jSONObject.put("name", name);
                                jSONObject.put("max_id", max_id2);
                                jSONObject.put("max_date", max_date);
                                jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                                if (mainNotification2 != null) {
                                    user = "photo";
                                    text = new StringBuilder();
                                    text.append(mainNotification2.dc_id);
                                    text.append("_");
                                    text.append(mainNotification2.volume_id);
                                    text.append("_");
                                    text.append(mainNotification2.secret);
                                    jSONObject.put(user, text.toString());
                                } else {
                                    num = internalId;
                                    str = name;
                                }
                                if (serializedMsgs != null) {
                                    jSONObject.put("msgs", serializedMsgs);
                                } else {
                                    builder2 = serializedMsgs;
                                }
                                if (lowerId <= 0) {
                                    jSONObject.put("type", "user");
                                } else if (lowerId < 0) {
                                    if (isChannel) {
                                        if (isSupergroup) {
                                            jSONObject.put("type", "group");
                                        }
                                    }
                                    jSONObject.put("type", "channel");
                                }
                                serializedNotifications2 = serializedNotifications;
                                serializedNotifications2.put(jSONObject);
                            } else {
                                user2 = user;
                                builder = showWhen;
                                num = internalId;
                                str = name;
                                serializedNotifications2 = serializedNotifications;
                                jSONObject = serializedChat2;
                                size = max_date;
                                z3 = canReply;
                                builder2 = serializedMsgs;
                                max_id = max_id2;
                            }
                            b2 = b + 1;
                            holders2 = holders;
                            messagesByDialogs2 = messagesByDialogs;
                            size = size2;
                            oldIdsWear2 = oldIdsWear;
                            mainNotification2 = mainNotification;
                            i2 = 0;
                            serializedNotifications3 = serializedNotifications2;
                            sortedDialogs2 = sortedDialogs;
                        }
                        name2 = LocaleController.getString("AppName", R.string.AppName);
                        fileLocation = null;
                        z = false;
                        photoPath = fileLocation;
                        z2 = z;
                        holders3 = holders2;
                        serializedChat2 = mainNotification2;
                        highId2 = highId;
                        mainNotification2 = new UnreadConversation.Builder(name2).setLatestTimestamp(((long) size) * 1000);
                        max_date = size;
                        msgHeardIntent = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                        msgHeardIntent.addFlags(32);
                        msgHeardIntent.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                        msgHeardIntent.putExtra("dialog_id", dialog_id2);
                        msgHeardIntent.putExtra("max_id", max_id);
                        msgHeardIntent.putExtra("currentAccount", notificationsController.currentAccount);
                        user5 = user4;
                        msgHeardPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId2.intValue(), msgHeardIntent, 134217728);
                        mainNotification2.setReadPendingIntent(msgHeardPendingIntent);
                        if (isChannel) {
                            if (!isSupergroup) {
                                intent = msgHeardIntent;
                                internalId3 = internalId2;
                                pendingIntent = msgHeardPendingIntent;
                                canReply = z2;
                                action = null;
                                wearReplyAction = action;
                                count = (Integer) notificationsController.pushDialogs.get(dialog_id2);
                                if (count == null) {
                                    i = 0;
                                } else {
                                    i = 0;
                                    count = Integer.valueOf(0);
                                }
                                messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID);
                                objArr = new Object[2];
                                objArr[i] = name2;
                                photoPath2 = photoPath;
                                objArr[1] = LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()));
                                messagingStyle2 = messagingStyle.setConversationTitle(String.format("%1$s (%2$s)", objArr));
                                text = new StringBuilder();
                                isText = new boolean[1];
                                serializedMsgs2 = null;
                                if (serializedChat2 != null) {
                                    rows = null;
                                } else {
                                    rows = null;
                                    serializedMsgs2 = new JSONArray();
                                }
                                i = messageObjects.size() - 1;
                                rowsMid = 0;
                                rows2 = rows;
                                while (true) {
                                    rows3 = rows2;
                                    if (i < 0) {
                                        break;
                                    }
                                    messageObject3 = (MessageObject) messageObjects.get(i);
                                    name3 = name2;
                                    messageObjects2 = messageObjects;
                                    message = getStringForMessage(messageObject3, null, isText);
                                    if (messageObject3.isFcmMessage() != null) {
                                    }
                                    if (message == null) {
                                        if (lowerId < 0) {
                                            action2 = wearReplyAction;
                                            i3 = max_id;
                                            if (isText[null]) {
                                                wearReplyAction = new StringBuilder();
                                                wearReplyAction.append(name2);
                                                wearReplyAction.append(" ");
                                                wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                            } else {
                                                wearReplyAction = new StringBuilder();
                                                wearReplyAction.append(name2);
                                                wearReplyAction.append(": ");
                                                wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                            }
                                        } else {
                                            i3 = max_id;
                                            stringBuilder = new StringBuilder();
                                            action2 = wearReplyAction;
                                            stringBuilder.append(" @ ");
                                            stringBuilder.append(name2);
                                            wearReplyAction = message.replace(stringBuilder.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                        }
                                        if (text.length() > 0) {
                                            text.append("\n\n");
                                        }
                                        text.append(wearReplyAction);
                                        mainNotification2.addMessage(wearReplyAction);
                                        nameToReplace = name2;
                                        messagingStyle2.addMessage(wearReplyAction, ((long) messageObject3.messageOwner.date) * 1000, null);
                                        if (serializedMsgs2 != null) {
                                            zArr = isText;
                                        } else {
                                            name2 = new JSONObject();
                                            name2.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject3));
                                            name2.put("date", messageObject3.messageOwner.date);
                                            sender = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(messageObject3.getFromId()));
                                            if (sender != null) {
                                                zArr = isText;
                                                name2.put("fname", sender.first_name);
                                                name2.put("lname", sender.last_name);
                                                serializedMsgs2.put(name2);
                                            }
                                            zArr = isText;
                                            serializedMsgs2.put(name2);
                                        }
                                        name2 = messageObject3.messageOwner.reply_markup.rows;
                                        isText = messageObject3.getId();
                                        rows2 = name2;
                                        rowsMid = isText;
                                        i--;
                                        name2 = name3;
                                        messageObjects = messageObjects2;
                                        max_id = i3;
                                        wearReplyAction = action2;
                                        isText = zArr;
                                    } else {
                                        zArr = isText;
                                        action2 = wearReplyAction;
                                        i3 = max_id;
                                    }
                                    rows2 = rows3;
                                    i--;
                                    name2 = name3;
                                    messageObjects = messageObjects2;
                                    max_id = i3;
                                    wearReplyAction = action2;
                                    isText = zArr;
                                }
                                name3 = name2;
                                zArr = isText;
                                action2 = wearReplyAction;
                                messageObjects2 = messageObjects;
                                i3 = max_id;
                                intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("com.tmessages.openchat");
                                stringBuilder2.append(Math.random());
                                stringBuilder2.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                                intent2.setAction(stringBuilder2.toString());
                                intent2.setFlags(32768);
                                if (lowerId != 0) {
                                    highId3 = highId2;
                                    intent2.putExtra("encId", highId3);
                                } else {
                                    if (lowerId > 0) {
                                        intent2.putExtra("chatId", -lowerId);
                                    } else {
                                        intent2.putExtra("userId", lowerId);
                                    }
                                    highId3 = highId2;
                                }
                                intent2.putExtra("currentAccount", notificationsController.currentAccount);
                                contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                                wearableExtender = new WearableExtender();
                                if (action2 != null) {
                                    wearReplyAction = action2;
                                } else {
                                    wearReplyAction = action2;
                                    wearableExtender.addAction(wearReplyAction);
                                }
                                if (lowerId == 0) {
                                    max_id = i3;
                                    stringBuilder3 = new StringBuilder();
                                    stringBuilder3.append("tgenc");
                                    stringBuilder3.append(highId3);
                                    stringBuilder3.append("_");
                                    stringBuilder3.append(max_id);
                                    message = stringBuilder3.toString();
                                } else if (lowerId > 0) {
                                    max_id = i3;
                                    stringBuilder4 = new StringBuilder();
                                    stringBuilder4.append("tgchat");
                                    stringBuilder4.append(-lowerId);
                                    stringBuilder4.append("_");
                                    stringBuilder4.append(max_id);
                                    message = stringBuilder4.toString();
                                } else {
                                    message = new StringBuilder();
                                    message.append("tguser");
                                    message.append(lowerId);
                                    message.append("_");
                                    max_id = i3;
                                    message.append(max_id);
                                    message = message.toString();
                                    intent3 = intent2;
                                }
                                dismissalID = message;
                                wearableExtender.setDismissalId(dismissalID);
                                stringBuilder4 = new StringBuilder();
                                stringBuilder4.append("tgaccount");
                                stringBuilder4.append(UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                                wearableExtender.setBridgeTag(stringBuilder4.toString());
                                summaryExtender = new WearableExtender();
                                stringBuilder4 = new StringBuilder();
                                stringBuilder4.append("summary_");
                                stringBuilder4.append(dismissalID);
                                summaryExtender.setDismissalId(stringBuilder4.toString());
                                notificationBuilder.extend(summaryExtender);
                                messageObjects = messageObjects2;
                                serializedMsgs = serializedMsgs2;
                                date = ((long) ((MessageObject) messageObjects.get(null)).messageOwner.date) * 1000;
                                name = name3;
                                max_id2 = max_id;
                                showWhen = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(notificationsController.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true);
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("sdid_");
                                stringBuilder.append(dialog_id2);
                                showWhen = showWhen.setShortcutId(stringBuilder.toString()).setGroupAlertBehavior(1).setStyle(messagingStyle2).setContentIntent(contentIntent).extend(wearableExtender);
                                text = new StringBuilder();
                                text.append(TtmlNode.ANONYMOUS_REGION_ID);
                                text.append(Long.MAX_VALUE - date);
                                showWhen = showWhen.setSortKey(text.toString()).extend(new CarExtender().setUnreadConversation(mainNotification2.build())).setCategory("msg");
                                if (notificationsController.pushDialogs.size() == 1) {
                                }
                                str2 = summary;
                                if (lowerId == 0) {
                                    showWhen.setLocalOnly(true);
                                }
                                if (photoPath2 != null) {
                                    unreadConvBuilder = mainNotification2;
                                    messagingStyle3 = messagingStyle2;
                                    mainNotification2 = photoPath2;
                                } else {
                                    unreadConvBuilder = mainNotification2;
                                    mainNotification2 = photoPath2;
                                    messagingStyle2 = ImageLoader.getInstance().getImageFromMemory(mainNotification2, null, "50_50");
                                    if (messagingStyle2 != null) {
                                        file = FileLoader.getPathToAttach(mainNotification2, true);
                                        if (file.exists()) {
                                            img = messagingStyle2;
                                        } else {
                                            scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                            img = messagingStyle2;
                                            messagingStyle2 = new Options();
                                            if (scaleFactor < 1.0f) {
                                            }
                                            messagingStyle2.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), messagingStyle2);
                                            if (bitmap != null) {
                                                showWhen.setLargeIcon(bitmap);
                                            }
                                        }
                                    } else {
                                        showWhen.setLargeIcon(messagingStyle2.getBitmap());
                                    }
                                }
                                if (AndroidUtilities.needShowPasscode(false)) {
                                }
                                wearableExtender2 = wearableExtender;
                                j = date;
                                i4 = rowsMid;
                                arrayList2 = rows3;
                                if (chat == null) {
                                }
                                user = user5;
                                if (VERSION.SDK_INT >= 26) {
                                    showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                                }
                                internalId = internalId3;
                                holders = holders3;
                                holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                                notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                                if (serializedChat2 != null) {
                                    user2 = user;
                                    builder = showWhen;
                                    num = internalId;
                                    str = name;
                                    serializedNotifications2 = serializedNotifications;
                                    jSONObject = serializedChat2;
                                    size = max_date;
                                    z3 = canReply;
                                    builder2 = serializedMsgs;
                                    max_id = max_id2;
                                } else {
                                    jSONObject = serializedChat2;
                                    jSONObject.put("reply", canReply);
                                    jSONObject.put("name", name);
                                    jSONObject.put("max_id", max_id2);
                                    jSONObject.put("max_date", max_date);
                                    jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                                    if (mainNotification2 != null) {
                                        num = internalId;
                                        str = name;
                                    } else {
                                        user = "photo";
                                        text = new StringBuilder();
                                        text.append(mainNotification2.dc_id);
                                        text.append("_");
                                        text.append(mainNotification2.volume_id);
                                        text.append("_");
                                        text.append(mainNotification2.secret);
                                        jSONObject.put(user, text.toString());
                                    }
                                    if (serializedMsgs != null) {
                                        builder2 = serializedMsgs;
                                    } else {
                                        jSONObject.put("msgs", serializedMsgs);
                                    }
                                    if (lowerId <= 0) {
                                        jSONObject.put("type", "user");
                                    } else if (lowerId < 0) {
                                        if (isChannel) {
                                            if (isSupergroup) {
                                                jSONObject.put("type", "group");
                                            }
                                        }
                                        jSONObject.put("type", "channel");
                                    }
                                    serializedNotifications2 = serializedNotifications;
                                    serializedNotifications2.put(jSONObject);
                                }
                                b2 = b + 1;
                                holders2 = holders;
                                messagesByDialogs2 = messagesByDialogs;
                                size = size2;
                                oldIdsWear2 = oldIdsWear;
                                mainNotification2 = mainNotification;
                                i2 = 0;
                                serializedNotifications3 = serializedNotifications2;
                                sortedDialogs2 = sortedDialogs;
                            }
                        }
                        if (z2) {
                        }
                        internalId3 = internalId2;
                        pendingIntent = msgHeardPendingIntent;
                        canReply = z2;
                        action = null;
                        wearReplyAction = action;
                        count = (Integer) notificationsController.pushDialogs.get(dialog_id2);
                        if (count == null) {
                            i = 0;
                            count = Integer.valueOf(0);
                        } else {
                            i = 0;
                        }
                        messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID);
                        objArr = new Object[2];
                        objArr[i] = name2;
                        photoPath2 = photoPath;
                        objArr[1] = LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()));
                        messagingStyle2 = messagingStyle.setConversationTitle(String.format("%1$s (%2$s)", objArr));
                        text = new StringBuilder();
                        isText = new boolean[1];
                        serializedMsgs2 = null;
                        if (serializedChat2 != null) {
                            rows = null;
                            serializedMsgs2 = new JSONArray();
                        } else {
                            rows = null;
                        }
                        i = messageObjects.size() - 1;
                        rowsMid = 0;
                        rows2 = rows;
                        while (true) {
                            rows3 = rows2;
                            if (i < 0) {
                                break;
                            }
                            messageObject3 = (MessageObject) messageObjects.get(i);
                            name3 = name2;
                            messageObjects2 = messageObjects;
                            message = getStringForMessage(messageObject3, null, isText);
                            if (messageObject3.isFcmMessage() != null) {
                            }
                            if (message == null) {
                                zArr = isText;
                                action2 = wearReplyAction;
                                i3 = max_id;
                            } else {
                                if (lowerId < 0) {
                                    i3 = max_id;
                                    stringBuilder = new StringBuilder();
                                    action2 = wearReplyAction;
                                    stringBuilder.append(" @ ");
                                    stringBuilder.append(name2);
                                    wearReplyAction = message.replace(stringBuilder.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                } else {
                                    action2 = wearReplyAction;
                                    i3 = max_id;
                                    if (isText[null]) {
                                        wearReplyAction = new StringBuilder();
                                        wearReplyAction.append(name2);
                                        wearReplyAction.append(": ");
                                        wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    } else {
                                        wearReplyAction = new StringBuilder();
                                        wearReplyAction.append(name2);
                                        wearReplyAction.append(" ");
                                        wearReplyAction = message.replace(wearReplyAction.toString(), TtmlNode.ANONYMOUS_REGION_ID);
                                    }
                                }
                                if (text.length() > 0) {
                                    text.append("\n\n");
                                }
                                text.append(wearReplyAction);
                                mainNotification2.addMessage(wearReplyAction);
                                nameToReplace = name2;
                                messagingStyle2.addMessage(wearReplyAction, ((long) messageObject3.messageOwner.date) * 1000, null);
                                if (serializedMsgs2 != null) {
                                    name2 = new JSONObject();
                                    name2.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject3));
                                    name2.put("date", messageObject3.messageOwner.date);
                                    sender = MessagesController.getInstance(notificationsController.currentAccount).getUser(Integer.valueOf(messageObject3.getFromId()));
                                    if (sender != null) {
                                        zArr = isText;
                                        name2.put("fname", sender.first_name);
                                        name2.put("lname", sender.last_name);
                                        serializedMsgs2.put(name2);
                                    }
                                    zArr = isText;
                                    serializedMsgs2.put(name2);
                                } else {
                                    zArr = isText;
                                }
                                name2 = messageObject3.messageOwner.reply_markup.rows;
                                isText = messageObject3.getId();
                                rows2 = name2;
                                rowsMid = isText;
                                i--;
                                name2 = name3;
                                messageObjects = messageObjects2;
                                max_id = i3;
                                wearReplyAction = action2;
                                isText = zArr;
                            }
                            rows2 = rows3;
                            i--;
                            name2 = name3;
                            messageObjects = messageObjects2;
                            max_id = i3;
                            wearReplyAction = action2;
                            isText = zArr;
                        }
                        name3 = name2;
                        zArr = isText;
                        action2 = wearReplyAction;
                        messageObjects2 = messageObjects;
                        i3 = max_id;
                        intent2 = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("com.tmessages.openchat");
                        stringBuilder2.append(Math.random());
                        stringBuilder2.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent2.setAction(stringBuilder2.toString());
                        intent2.setFlags(32768);
                        if (lowerId != 0) {
                            if (lowerId > 0) {
                                intent2.putExtra("userId", lowerId);
                            } else {
                                intent2.putExtra("chatId", -lowerId);
                            }
                            highId3 = highId2;
                        } else {
                            highId3 = highId2;
                            intent2.putExtra("encId", highId3);
                        }
                        intent2.putExtra("currentAccount", notificationsController.currentAccount);
                        contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent2, 1073741824);
                        wearableExtender = new WearableExtender();
                        if (action2 != null) {
                            wearReplyAction = action2;
                            wearableExtender.addAction(wearReplyAction);
                        } else {
                            wearReplyAction = action2;
                        }
                        if (lowerId == 0) {
                            max_id = i3;
                            stringBuilder3 = new StringBuilder();
                            stringBuilder3.append("tgenc");
                            stringBuilder3.append(highId3);
                            stringBuilder3.append("_");
                            stringBuilder3.append(max_id);
                            message = stringBuilder3.toString();
                        } else if (lowerId > 0) {
                            message = new StringBuilder();
                            message.append("tguser");
                            message.append(lowerId);
                            message.append("_");
                            max_id = i3;
                            message.append(max_id);
                            message = message.toString();
                            intent3 = intent2;
                        } else {
                            max_id = i3;
                            stringBuilder4 = new StringBuilder();
                            stringBuilder4.append("tgchat");
                            stringBuilder4.append(-lowerId);
                            stringBuilder4.append("_");
                            stringBuilder4.append(max_id);
                            message = stringBuilder4.toString();
                        }
                        dismissalID = message;
                        wearableExtender.setDismissalId(dismissalID);
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("tgaccount");
                        stringBuilder4.append(UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                        wearableExtender.setBridgeTag(stringBuilder4.toString());
                        summaryExtender = new WearableExtender();
                        stringBuilder4 = new StringBuilder();
                        stringBuilder4.append("summary_");
                        stringBuilder4.append(dismissalID);
                        summaryExtender.setDismissalId(stringBuilder4.toString());
                        notificationBuilder.extend(summaryExtender);
                        messageObjects = messageObjects2;
                        serializedMsgs = serializedMsgs2;
                        date = ((long) ((MessageObject) messageObjects.get(null)).messageOwner.date) * 1000;
                        name = name3;
                        max_id2 = max_id;
                        showWhen = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(notificationsController.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("sdid_");
                        stringBuilder.append(dialog_id2);
                        showWhen = showWhen.setShortcutId(stringBuilder.toString()).setGroupAlertBehavior(1).setStyle(messagingStyle2).setContentIntent(contentIntent).extend(wearableExtender);
                        text = new StringBuilder();
                        text.append(TtmlNode.ANONYMOUS_REGION_ID);
                        text.append(Long.MAX_VALUE - date);
                        showWhen = showWhen.setSortKey(text.toString()).extend(new CarExtender().setUnreadConversation(mainNotification2.build())).setCategory("msg");
                        if (notificationsController.pushDialogs.size() == 1) {
                        }
                        str2 = summary;
                        if (lowerId == 0) {
                            showWhen.setLocalOnly(true);
                        }
                        if (photoPath2 != null) {
                            unreadConvBuilder = mainNotification2;
                            mainNotification2 = photoPath2;
                            messagingStyle2 = ImageLoader.getInstance().getImageFromMemory(mainNotification2, null, "50_50");
                            if (messagingStyle2 != null) {
                                showWhen.setLargeIcon(messagingStyle2.getBitmap());
                            } else {
                                file = FileLoader.getPathToAttach(mainNotification2, true);
                                if (file.exists()) {
                                    scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                    img = messagingStyle2;
                                    messagingStyle2 = new Options();
                                    if (scaleFactor < 1.0f) {
                                    }
                                    messagingStyle2.inSampleSize = scaleFactor < 1.0f ? 1 : (int) scaleFactor;
                                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), messagingStyle2);
                                    if (bitmap != null) {
                                        showWhen.setLargeIcon(bitmap);
                                    }
                                } else {
                                    img = messagingStyle2;
                                }
                            }
                        } else {
                            unreadConvBuilder = mainNotification2;
                            messagingStyle3 = messagingStyle2;
                            mainNotification2 = photoPath2;
                        }
                        if (AndroidUtilities.needShowPasscode(false)) {
                        }
                        wearableExtender2 = wearableExtender;
                        j = date;
                        i4 = rowsMid;
                        arrayList2 = rows3;
                        if (chat == null) {
                        }
                        user = user5;
                        if (VERSION.SDK_INT >= 26) {
                            showWhen.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                        }
                        internalId = internalId3;
                        holders = holders3;
                        holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), showWhen.build()));
                        notificationsController.wearNotificationsIds.put(dialog_id2, internalId);
                        if (serializedChat2 != null) {
                            jSONObject = serializedChat2;
                            jSONObject.put("reply", canReply);
                            jSONObject.put("name", name);
                            jSONObject.put("max_id", max_id2);
                            jSONObject.put("max_date", max_date);
                            jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                            if (mainNotification2 != null) {
                                user = "photo";
                                text = new StringBuilder();
                                text.append(mainNotification2.dc_id);
                                text.append("_");
                                text.append(mainNotification2.volume_id);
                                text.append("_");
                                text.append(mainNotification2.secret);
                                jSONObject.put(user, text.toString());
                            } else {
                                num = internalId;
                                str = name;
                            }
                            if (serializedMsgs != null) {
                                jSONObject.put("msgs", serializedMsgs);
                            } else {
                                builder2 = serializedMsgs;
                            }
                            if (lowerId <= 0) {
                                jSONObject.put("type", "user");
                            } else if (lowerId < 0) {
                                if (isChannel) {
                                    if (isSupergroup) {
                                        jSONObject.put("type", "group");
                                    }
                                }
                                jSONObject.put("type", "channel");
                            }
                            serializedNotifications2 = serializedNotifications;
                            serializedNotifications2.put(jSONObject);
                        } else {
                            user2 = user;
                            builder = showWhen;
                            num = internalId;
                            str = name;
                            serializedNotifications2 = serializedNotifications;
                            jSONObject = serializedChat2;
                            size = max_date;
                            z3 = canReply;
                            builder2 = serializedMsgs;
                            max_id = max_id2;
                        }
                        b2 = b + 1;
                        holders2 = holders;
                        messagesByDialogs2 = messagesByDialogs;
                        size = size2;
                        oldIdsWear2 = oldIdsWear;
                        mainNotification2 = mainNotification;
                        i2 = 0;
                        serializedNotifications3 = serializedNotifications2;
                        sortedDialogs2 = sortedDialogs;
                    }
                }
            }
            holders = holders2;
            serializedNotifications2 = serializedNotifications;
            b2 = b + 1;
            holders2 = holders;
            messagesByDialogs2 = messagesByDialogs;
            size = size2;
            oldIdsWear2 = oldIdsWear;
            mainNotification2 = mainNotification;
            i2 = 0;
            serializedNotifications3 = serializedNotifications2;
            sortedDialogs2 = sortedDialogs;
        }
        sortedDialogs = sortedDialogs2;
        messagesByDialogs = messagesByDialogs2;
        oldIdsWear = oldIdsWear2;
        serializedNotifications2 = serializedNotifications3;
        holders = holders2;
        notificationManager.notify(notificationsController.notificationId, mainNotification2);
        i = holders.size();
        for (a2 = 0; a2 < i; a2++) {
            ((AnonymousClass1NotificationHolder) holders.get(a2)).call();
        }
        int a3 = 0;
        while (true) {
            a2 = a3;
            LongSparseArray<Integer> oldIdsWear3 = oldIdsWear;
            if (a2 >= oldIdsWear3.size()) {
                break;
            }
            notificationManager.cancel(((Integer) oldIdsWear3.valueAt(a2)).intValue());
            a3 = a2 + 1;
            oldIdsWear = oldIdsWear3;
        }
        if (serializedNotifications2 != null) {
            try {
                JSONObject s = new JSONObject();
                s.put(TtmlNode.ATTR_ID, UserConfig.getInstance(notificationsController.currentAccount).getClientUserId());
                s.put("n", serializedNotifications2);
                WearDataLayerListenerService.sendMessageToWatch("/notify", s.toString().getBytes(C.UTF8_NAME), "remote_notifications");
            } catch (Exception e14) {
            }
        }
    }

    public void playOutChatSound() {
        if (this.inChatSoundEnabled) {
            if (!MediaController.getInstance().isRecordingAudio()) {
                try {
                    if (audioManager.getRingerMode() != 0) {
                        notificationsQueue.postRunnable(new Runnable() {
                            public void run() {
                                try {
                                    if (Math.abs(System.currentTimeMillis() - NotificationsController.this.lastSoundOutPlay) > 100) {
                                        NotificationsController.this.lastSoundOutPlay = System.currentTimeMillis();
                                        if (NotificationsController.this.soundPool == null) {
                                            NotificationsController.this.soundPool = new SoundPool(3, 1, 0);
                                            NotificationsController.this.soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
                                                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                                                    if (status == 0) {
                                                        try {
                                                            soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
                                                        } catch (Throwable e) {
                                                            FileLog.e(e);
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                        if (NotificationsController.this.soundOut == 0 && !NotificationsController.this.soundOutLoaded) {
                                            NotificationsController.this.soundOutLoaded = true;
                                            NotificationsController.this.soundOut = NotificationsController.this.soundPool.load(ApplicationLoader.applicationContext, R.raw.sound_out, 1);
                                        }
                                        if (NotificationsController.this.soundOut != 0) {
                                            try {
                                                NotificationsController.this.soundPool.play(NotificationsController.this.soundOut, 1.0f, 1.0f, 1, 0, 1.0f);
                                            } catch (Throwable e) {
                                                FileLog.e(e);
                                            }
                                        }
                                    }
                                } catch (Throwable e2) {
                                    FileLog.e(e2);
                                }
                            }
                        });
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public void updateServerNotificationsSettings(long dialog_id) {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        if (((int) dialog_id) != 0) {
            TL_inputPeerNotifySettings tL_inputPeerNotifySettings;
            StringBuilder stringBuilder;
            SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
            TL_account_updateNotifySettings req = new TL_account_updateNotifySettings();
            req.settings = new TL_inputPeerNotifySettings();
            req.settings.sound = "default";
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("notify2_");
            stringBuilder2.append(dialog_id);
            int mute_type = preferences.getInt(stringBuilder2.toString(), 0);
            if (mute_type == 3) {
                tL_inputPeerNotifySettings = req.settings;
                stringBuilder = new StringBuilder();
                stringBuilder.append("notifyuntil_");
                stringBuilder.append(dialog_id);
                tL_inputPeerNotifySettings.mute_until = preferences.getInt(stringBuilder.toString(), 0);
            } else {
                req.settings.mute_until = mute_type != 2 ? 0 : ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            tL_inputPeerNotifySettings = req.settings;
            stringBuilder = new StringBuilder();
            stringBuilder.append("preview_");
            stringBuilder.append(dialog_id);
            tL_inputPeerNotifySettings.show_previews = preferences.getBoolean(stringBuilder.toString(), true);
            tL_inputPeerNotifySettings = req.settings;
            stringBuilder = new StringBuilder();
            stringBuilder.append("silent_");
            stringBuilder.append(dialog_id);
            tL_inputPeerNotifySettings.silent = preferences.getBoolean(stringBuilder.toString(), false);
            req.peer = new TL_inputNotifyPeer();
            ((TL_inputNotifyPeer) req.peer).peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int) dialog_id);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            });
        }
    }
}
