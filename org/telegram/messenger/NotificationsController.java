package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
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
import android.support.v4.app.NotificationCompat.Style;
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
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.PhoneCallDiscardReason;
import org.telegram.tgnet.TLRPC.TL_account_updateNotifySettings;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputNotifyPeer;
import org.telegram.tgnet.TLRPC.TL_inputPeerNotifySettings;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC.TL_messageActionChannelCreate;
import org.telegram.tgnet.TLRPC.TL_messageActionChannelMigrateFrom;
import org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser;
import org.telegram.tgnet.TLRPC.TL_messageActionChatCreate;
import org.telegram.tgnet.TLRPC.TL_messageActionChatDeletePhoto;
import org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
import org.telegram.tgnet.TLRPC.TL_messageActionChatEditPhoto;
import org.telegram.tgnet.TLRPC.TL_messageActionChatEditTitle;
import org.telegram.tgnet.TLRPC.TL_messageActionChatJoinedByLink;
import org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo;
import org.telegram.tgnet.TLRPC.TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC.TL_messageActionGameScore;
import org.telegram.tgnet.TLRPC.TL_messageActionLoginUnknownLocation;
import org.telegram.tgnet.TLRPC.TL_messageActionPaymentSent;
import org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC.TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC.TL_messageActionScreenshotTaken;
import org.telegram.tgnet.TLRPC.TL_messageActionUserJoined;
import org.telegram.tgnet.TLRPC.TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaContact;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_messageMediaGame;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_messageService;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
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
                try {
                    localInstance = Instance[num];
                    if (localInstance == null) {
                        NotificationsController[] notificationsControllerArr = Instance;
                        NotificationsController localInstance2 = new NotificationsController(num);
                        try {
                            notificationsControllerArr[num] = localInstance2;
                            localInstance = localInstance2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            localInstance = localInstance2;
                            throw th2;
                        }
                    }
                } catch (Throwable th3) {
                    th2 = th3;
                    throw th2;
                }
            }
        }
        return localInstance;
    }

    public NotificationsController(int instance) {
        this.currentAccount = instance;
        this.notificationId = this.currentAccount + 1;
        this.notificationGroup = "messages" + (this.currentAccount == 0 ? TtmlNode.ANONYMOUS_REGION_ID : Integer.valueOf(this.currentAccount));
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
        } catch (Throwable e22) {
            FileLog.e(e22);
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
                        String keyStart = NotificationsController.this.currentAccount + "channel";
                        List<NotificationChannel> list = NotificationsController.systemNotificationManager.getNotificationChannels();
                        int count = list.size();
                        for (int a = 0; a < count; a++) {
                            String id = ((NotificationChannel) list.get(a)).getId();
                            if (id.startsWith(keyStart)) {
                                NotificationsController.systemNotificationManager.deleteNotificationChannel(id);
                            }
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
                    FileLog.d("set last online from other device = " + time);
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
            if ((!messageObject.messageOwner.mentioned || !(messageObject.messageOwner.action instanceof TL_messageActionPinMessage)) && ((int) dialog_id) != 0 && (messageObject.messageOwner.to_id.channel_id == 0 || messageObject.isMegagroup())) {
                return true;
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
                    if (!((messageObject.messageOwner.mentioned && (messageObject.messageOwner.action instanceof TL_messageActionPinMessage)) || ((int) dialog_id) == 0 || (messageObject.messageOwner.to_id.channel_id != 0 && !messageObject.isMegagroup()))) {
                        popupArray.add(0, messageObject);
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
                for (int a = 0; a < deletedMessages.size(); a++) {
                    int key = deletedMessages.keyAt(a);
                    long dialog_id = (long) (-key);
                    ArrayList<Integer> mids = (ArrayList) deletedMessages.get(key);
                    Integer currentCount = (Integer) NotificationsController.this.pushDialogs.get(dialog_id);
                    if (currentCount == null) {
                        currentCount = Integer.valueOf(0);
                    }
                    Integer newCount = currentCount;
                    for (int b = 0; b < mids.size(); b++) {
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
                    }
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
                }
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
                        NotificationsController.this.scheduleNotificationDelay(NotificationsController.this.lastOnlineFromOtherDevice > ConnectionsManager.getInstance(NotificationsController.this.currentAccount).getCurrentTime());
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
                for (int a = 0; a < deletedMessages.size(); a++) {
                    int key = deletedMessages.keyAt(a);
                    long dialog_id = (long) (-key);
                    int id = deletedMessages.get(key);
                    Integer currentCount = (Integer) NotificationsController.this.pushDialogs.get(dialog_id);
                    if (currentCount == null) {
                        currentCount = Integer.valueOf(0);
                    }
                    Integer newCount = currentCount;
                    int c = 0;
                    while (c < NotificationsController.this.pushMessages.size()) {
                        MessageObject messageObject = (MessageObject) NotificationsController.this.pushMessages.get(c);
                        if (messageObject.getDialogId() == dialog_id && messageObject.getId() <= id) {
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
                    }
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
                        NotificationsController.this.scheduleNotificationDelay(NotificationsController.this.lastOnlineFromOtherDevice > ConnectionsManager.getInstance(NotificationsController.this.currentAccount).getCurrentTime());
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
                int a;
                MessageObject messageObject;
                long mid;
                if (sparseLongArray != null) {
                    for (int b = 0; b < sparseLongArray.size(); b++) {
                        int key = sparseLongArray.keyAt(b);
                        long messageId = sparseLongArray.get(key);
                        a = 0;
                        while (a < NotificationsController.this.pushMessages.size()) {
                            messageObject = (MessageObject) NotificationsController.this.pushMessages.get(a);
                            if (messageObject.getDialogId() == ((long) key) && messageObject.getId() <= ((int) messageId)) {
                                if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                    NotificationsController.this.personal_count = NotificationsController.this.personal_count - 1;
                                }
                                popupArrayRemove.add(messageObject);
                                mid = (long) messageObject.getId();
                                if (messageObject.messageOwner.to_id.channel_id != 0) {
                                    mid |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
                                }
                                NotificationsController.this.pushMessagesDict.remove(mid);
                                NotificationsController.this.delayedPushMessages.remove(messageObject);
                                NotificationsController.this.pushMessages.remove(a);
                                a--;
                            }
                            a++;
                        }
                    }
                }
                if (!(j == 0 || (i == 0 && i2 == 0))) {
                    a = 0;
                    while (a < NotificationsController.this.pushMessages.size()) {
                        messageObject = (MessageObject) NotificationsController.this.pushMessages.get(a);
                        if (messageObject.getDialogId() == j) {
                            boolean remove = false;
                            if (i2 != 0) {
                                if (messageObject.messageOwner.date <= i2) {
                                    remove = true;
                                }
                            } else if (z) {
                                if (messageObject.getId() == i || i < 0) {
                                    remove = true;
                                }
                            } else if (messageObject.getId() <= i || i < 0) {
                                remove = true;
                            }
                            if (remove) {
                                if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                    NotificationsController.this.personal_count = NotificationsController.this.personal_count - 1;
                                }
                                NotificationsController.this.pushMessages.remove(a);
                                NotificationsController.this.delayedPushMessages.remove(messageObject);
                                popupArrayRemove.add(messageObject);
                                mid = (long) messageObject.getId();
                                if (messageObject.messageOwner.to_id.channel_id != 0) {
                                    mid |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
                                }
                                NotificationsController.this.pushMessagesDict.remove(mid);
                                a--;
                            }
                        }
                        a++;
                    }
                }
                if (!popupArrayRemove.isEmpty()) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            int size = popupArrayRemove.size();
                            for (int a = 0; a < size; a++) {
                                NotificationsController.this.popupMessages.remove(popupArrayRemove.get(a));
                            }
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
                        }
                    });
                }
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
                    long dialog_id;
                    boolean added = false;
                    LongSparseArray<Boolean> settingsCache = new LongSparseArray();
                    SharedPreferences preferences = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount);
                    boolean allowPinned = preferences.getBoolean("PinnedMessages", true);
                    int popup = 0;
                    for (int a = 0; a < arrayList.size(); a++) {
                        MessageObject messageObject = (MessageObject) arrayList.get(a);
                        long mid = (long) messageObject.getId();
                        if (messageObject.messageOwner.to_id.channel_id != 0) {
                            mid |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
                        }
                        MessageObject oldMessageObject = (MessageObject) NotificationsController.this.pushMessagesDict.get(mid);
                        if (oldMessageObject == null) {
                            dialog_id = messageObject.getDialogId();
                            long original_dialog_id = dialog_id;
                            if (dialog_id != NotificationsController.this.opened_dialog_id || !ApplicationLoader.isScreenOn) {
                                boolean value;
                                if (messageObject.messageOwner.mentioned) {
                                    if (allowPinned || !(messageObject.messageOwner.action instanceof TL_messageActionPinMessage)) {
                                        dialog_id = (long) messageObject.messageOwner.from_id;
                                    }
                                }
                                if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                    NotificationsController.this.personal_count = NotificationsController.this.personal_count + 1;
                                }
                                added = true;
                                int lower_id = (int) dialog_id;
                                boolean isChat = lower_id < 0;
                                int index = settingsCache.indexOfKey(dialog_id);
                                if (index >= 0) {
                                    value = ((Boolean) settingsCache.valueAt(index)).booleanValue();
                                } else {
                                    int notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                                    value = notifyOverride != 2 && ((preferences.getBoolean("EnableAll", true) && (!isChat || preferences.getBoolean("EnableGroup", true))) || notifyOverride != 0);
                                    settingsCache.put(dialog_id, Boolean.valueOf(value));
                                }
                                if (lower_id != 0) {
                                    if (preferences.getBoolean("custom_" + dialog_id, false)) {
                                        popup = preferences.getInt("popup_" + dialog_id, 0);
                                    } else {
                                        popup = 0;
                                    }
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
                                if (value) {
                                    if (popup != 0) {
                                        popupArrayAdd.add(0, messageObject);
                                    }
                                    NotificationsController.this.delayedPushMessages.add(messageObject);
                                    NotificationsController.this.pushMessages.add(0, messageObject);
                                    NotificationsController.this.pushMessagesDict.put(mid, messageObject);
                                    if (original_dialog_id != dialog_id) {
                                        NotificationsController.this.pushDialogsOverrideMention.put(original_dialog_id, Integer.valueOf(1));
                                    }
                                }
                            } else if (!z) {
                                NotificationsController.this.playInChatSound();
                            }
                        } else if (oldMessageObject.isFcmMessage()) {
                            NotificationsController.this.pushMessagesDict.put(mid, messageObject);
                            int idxOld = NotificationsController.this.pushMessages.indexOf(oldMessageObject);
                            if (idxOld >= 0) {
                                NotificationsController.this.pushMessages.set(idxOld, messageObject);
                            }
                        }
                    }
                    if (added) {
                        NotificationsController.this.notifyCheck = z2;
                    }
                    if (!(popupArrayAdd.isEmpty() || AndroidUtilities.needShowPasscode(false))) {
                        final int i = popup;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationsController.this.popupMessages.addAll(0, popupArrayAdd);
                                if (!ApplicationLoader.mainInterfacePaused && (ApplicationLoader.isScreenOn || SharedConfig.isWaitingForPasscodeEnter)) {
                                    return;
                                }
                                if (i == 3 || ((i == 1 && ApplicationLoader.isScreenOn) || (i == 2 && !ApplicationLoader.isScreenOn))) {
                                    Intent popupIntent = new Intent(ApplicationLoader.applicationContext, PopupNotificationActivity.class);
                                    popupIntent.setFlags(268763140);
                                    ApplicationLoader.applicationContext.startActivity(popupIntent);
                                }
                            }
                        });
                    }
                    if (added && z) {
                        dialog_id = ((MessageObject) arrayList.get(0)).getDialogId();
                        int old_unread_count = NotificationsController.this.total_unread_count;
                        notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                        if (NotificationsController.this.notifyCheck) {
                            Integer override = (Integer) NotificationsController.this.pushDialogsOverrideMention.get(dialog_id);
                            if (override != null && override.intValue() == 1) {
                                NotificationsController.this.pushDialogsOverrideMention.put(dialog_id, Integer.valueOf(0));
                                notifyOverride = 1;
                            }
                        }
                        boolean canAddValue = notifyOverride != 2 && ((preferences.getBoolean("EnableAll", true) && (((int) dialog_id) >= 0 || preferences.getBoolean("EnableGroup", true))) || notifyOverride != 0);
                        Integer currentCount = (Integer) NotificationsController.this.pushDialogs.get(dialog_id);
                        Integer newCount = Integer.valueOf(currentCount != null ? currentCount.intValue() + 1 : 1);
                        if (canAddValue) {
                            if (currentCount != null) {
                                NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count - currentCount.intValue();
                            }
                            NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + newCount.intValue();
                            NotificationsController.this.pushDialogs.put(dialog_id, newCount);
                        }
                        if (old_unread_count != NotificationsController.this.total_unread_count) {
                            if (NotificationsController.this.notifyCheck) {
                                NotificationsController.this.scheduleNotificationDelay(NotificationsController.this.lastOnlineFromOtherDevice > ConnectionsManager.getInstance(NotificationsController.this.currentAccount).getCurrentTime());
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
                for (int b = 0; b < dialogsToUpdate.size(); b++) {
                    long dialog_id = dialogsToUpdate.keyAt(b);
                    int notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                    if (NotificationsController.this.notifyCheck) {
                        Integer override = (Integer) NotificationsController.this.pushDialogsOverrideMention.get(dialog_id);
                        if (override != null && override.intValue() == 1) {
                            NotificationsController.this.pushDialogsOverrideMention.put(dialog_id, Integer.valueOf(0));
                            notifyOverride = 1;
                        }
                    }
                    boolean canAddValue = notifyOverride != 2 && ((preferences.getBoolean("EnableAll", true) && (((int) dialog_id) >= 0 || preferences.getBoolean("EnableGroup", true))) || notifyOverride != 0);
                    Integer currentCount = (Integer) NotificationsController.this.pushDialogs.get(dialog_id);
                    Integer newCount = (Integer) dialogsToUpdate.get(dialog_id);
                    if (newCount.intValue() == 0) {
                        NotificationsController.this.smartNotificationsDialogs.remove(dialog_id);
                    }
                    if (newCount.intValue() < 0) {
                        if (currentCount == null) {
                        } else {
                            newCount = Integer.valueOf(currentCount.intValue() + newCount.intValue());
                        }
                    }
                    if ((canAddValue || newCount.intValue() == 0) && currentCount != null) {
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count - currentCount.intValue();
                    }
                    if (newCount.intValue() == 0) {
                        NotificationsController.this.pushDialogs.remove(dialog_id);
                        NotificationsController.this.pushDialogsOverrideMention.remove(dialog_id);
                        int a = 0;
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
                                    mid |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
                                }
                                NotificationsController.this.pushMessagesDict.remove(mid);
                                popupArrayToRemove.add(messageObject);
                            }
                            a++;
                        }
                    } else if (canAddValue) {
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + newCount.intValue();
                        NotificationsController.this.pushDialogs.put(dialog_id, newCount);
                    }
                }
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
                        NotificationsController.this.scheduleNotificationDelay(NotificationsController.this.lastOnlineFromOtherDevice > ConnectionsManager.getInstance(NotificationsController.this.currentAccount).getCurrentTime());
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
                int a;
                long dialog_id;
                int index;
                boolean value;
                NotificationsController.this.pushDialogs.clear();
                NotificationsController.this.pushMessages.clear();
                NotificationsController.this.pushMessagesDict.clear();
                NotificationsController.this.total_unread_count = 0;
                NotificationsController.this.personal_count = 0;
                SharedPreferences preferences = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount);
                LongSparseArray<Boolean> settingsCache = new LongSparseArray();
                if (messages != null) {
                    for (a = 0; a < messages.size(); a++) {
                        Message message = (Message) messages.get(a);
                        long mid = (long) message.id;
                        if (message.to_id.channel_id != 0) {
                            mid |= ((long) message.to_id.channel_id) << 32;
                        }
                        if (NotificationsController.this.pushMessagesDict.indexOfKey(mid) < 0) {
                            MessageObject messageObject = new MessageObject(NotificationsController.this.currentAccount, message, false);
                            if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                NotificationsController.this.personal_count = NotificationsController.this.personal_count + 1;
                            }
                            dialog_id = messageObject.getDialogId();
                            long original_dialog_id = dialog_id;
                            if (messageObject.messageOwner.mentioned) {
                                dialog_id = (long) messageObject.messageOwner.from_id;
                            }
                            index = settingsCache.indexOfKey(dialog_id);
                            if (index >= 0) {
                                value = ((Boolean) settingsCache.valueAt(index)).booleanValue();
                            } else {
                                int notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                                value = notifyOverride != 2 && ((preferences.getBoolean("EnableAll", true) && (((int) dialog_id) >= 0 || preferences.getBoolean("EnableGroup", true))) || notifyOverride != 0);
                                settingsCache.put(dialog_id, Boolean.valueOf(value));
                            }
                            if (value && !(dialog_id == NotificationsController.this.opened_dialog_id && ApplicationLoader.isScreenOn)) {
                                NotificationsController.this.pushMessagesDict.put(mid, messageObject);
                                NotificationsController.this.pushMessages.add(0, messageObject);
                                if (original_dialog_id != dialog_id) {
                                    NotificationsController.this.pushDialogsOverrideMention.put(original_dialog_id, Integer.valueOf(1));
                                }
                            }
                        }
                    }
                }
                for (a = 0; a < dialogs.size(); a++) {
                    dialog_id = dialogs.keyAt(a);
                    index = settingsCache.indexOfKey(dialog_id);
                    if (index >= 0) {
                        value = ((Boolean) settingsCache.valueAt(index)).booleanValue();
                    } else {
                        notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                        Integer override = (Integer) NotificationsController.this.pushDialogsOverrideMention.get(dialog_id);
                        if (override != null && override.intValue() == 1) {
                            NotificationsController.this.pushDialogsOverrideMention.put(dialog_id, Integer.valueOf(0));
                            notifyOverride = 1;
                        }
                        value = notifyOverride != 2 && ((preferences.getBoolean("EnableAll", true) && (((int) dialog_id) >= 0 || preferences.getBoolean("EnableGroup", true))) || notifyOverride != 0);
                        settingsCache.put(dialog_id, Boolean.valueOf(value));
                    }
                    if (value) {
                        int count = ((Integer) dialogs.valueAt(a)).intValue();
                        NotificationsController.this.pushDialogs.put(dialog_id, Integer.valueOf(count));
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + count;
                    }
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        if (NotificationsController.this.total_unread_count == 0) {
                            NotificationsController.this.popupMessages.clear();
                            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
                        }
                        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.notificationsCountUpdated, Integer.valueOf(NotificationsController.this.currentAccount));
                    }
                });
                NotificationsController.this.showOrUpdateNotification(SystemClock.uptimeMillis() / 1000 < 60);
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
            if (msg.messageOwner.media instanceof TL_messageMediaPhoto) {
                return "🖼 " + msg.messageOwner.message;
            }
            if (msg.isVideo()) {
                return "📹 " + msg.messageOwner.message;
            }
            if (msg.messageOwner.media instanceof TL_messageMediaDocument) {
                if (msg.isGif()) {
                    return "🎬 " + msg.messageOwner.message;
                }
                return "📎 " + msg.messageOwner.message;
            }
        }
        return msg.messageText.toString();
    }

    private String getStringForMessage(MessageObject messageObject, boolean shortMessage, boolean[] text) {
        if (AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
        }
        long dialog_id = messageObject.messageOwner.dialog_id;
        int chat_id = messageObject.messageOwner.to_id.chat_id != 0 ? messageObject.messageOwner.to_id.chat_id : messageObject.messageOwner.to_id.channel_id;
        int from_id = messageObject.messageOwner.to_id.user_id;
        if (messageObject.isFcmMessage()) {
            if (chat_id == 0 && from_id != 0) {
                if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewAll", true)) {
                    return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, messageObject.localName);
                }
            } else if (chat_id != 0) {
                if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewGroup", true)) {
                    if (messageObject.isMegagroup() || messageObject.messageOwner.to_id.channel_id == 0) {
                        return LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, messageObject.localUserName, messageObject.localName);
                    }
                    return LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, messageObject.localName);
                }
            }
            text[0] = true;
            return (String) messageObject.messageText;
        }
        User user;
        Chat chat;
        if (from_id == 0) {
            if (messageObject.isFromUser() || messageObject.getId() < 0) {
                from_id = messageObject.messageOwner.from_id;
            } else {
                from_id = -chat_id;
            }
        } else if (from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            from_id = messageObject.messageOwner.from_id;
        }
        if (dialog_id == 0) {
            if (chat_id != 0) {
                dialog_id = (long) (-chat_id);
            } else if (from_id != 0) {
                dialog_id = (long) from_id;
            }
        }
        String name = null;
        if (from_id > 0) {
            user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(from_id));
            if (user != null) {
                name = UserObject.getUserName(user);
            }
        } else {
            chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-from_id));
            if (chat != null) {
                name = chat.title;
            }
        }
        if (name == null) {
            return null;
        }
        chat = null;
        if (chat_id != 0) {
            chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(chat_id));
            if (chat == null) {
                return null;
            }
        }
        String msg = null;
        if (((int) dialog_id) == 0) {
            msg = LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
        } else if (chat_id == 0 && from_id != 0) {
            if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewAll", true)) {
                msg = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, name);
            } else if (messageObject.messageOwner instanceof TL_messageService) {
                if (messageObject.messageOwner.action instanceof TL_messageActionUserJoined) {
                    msg = LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, name);
                } else if (messageObject.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto) {
                    msg = LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, name);
                } else if (messageObject.messageOwner.action instanceof TL_messageActionLoginUnknownLocation) {
                    String date = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(((long) messageObject.messageOwner.date) * 1000), LocaleController.getInstance().formatterDay.format(((long) messageObject.messageOwner.date) * 1000));
                    msg = LocaleController.formatString("NotificationUnrecognizedDevice", R.string.NotificationUnrecognizedDevice, UserConfig.getInstance(this.currentAccount).getCurrentUser().first_name, date, messageObject.messageOwner.action.title, messageObject.messageOwner.action.address);
                } else if ((messageObject.messageOwner.action instanceof TL_messageActionGameScore) || (messageObject.messageOwner.action instanceof TL_messageActionPaymentSent)) {
                    msg = messageObject.messageText.toString();
                } else if (messageObject.messageOwner.action instanceof TL_messageActionPhoneCall) {
                    PhoneCallDiscardReason reason = messageObject.messageOwner.action.reason;
                    if (!messageObject.isOut() && (reason instanceof TL_phoneCallDiscardReasonMissed)) {
                        msg = LocaleController.getString("CallMessageIncomingMissed", R.string.CallMessageIncomingMissed);
                    }
                }
            } else if (messageObject.isMediaEmpty()) {
                if (shortMessage) {
                    msg = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, name);
                } else if (TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    msg = LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, messageObject.messageOwner.message);
                    text[0] = true;
                }
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    msg = messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, name) : LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "🖼 " + messageObject.messageOwner.message);
                    text[0] = true;
                }
            } else if (messageObject.isVideo()) {
                if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    msg = messageObject.messageOwner.media.ttl_seconds != 0 ? LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, name) : LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "📹 " + messageObject.messageOwner.message);
                    text[0] = true;
                }
            } else if (messageObject.isGame()) {
                msg = LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, name, messageObject.messageOwner.media.game.title);
            } else if (messageObject.isVoice()) {
                msg = LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, name);
            } else if (messageObject.isRoundVideo()) {
                msg = LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, name);
            } else if (messageObject.isMusic()) {
                msg = LocaleController.formatString("NotificationMessageMusic", R.string.NotificationMessageMusic, name);
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaContact) {
                msg = LocaleController.formatString("NotificationMessageContact", R.string.NotificationMessageContact, name);
            } else if ((messageObject.messageOwner.media instanceof TL_messageMediaGeo) || (messageObject.messageOwner.media instanceof TL_messageMediaVenue)) {
                msg = LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, name);
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                msg = LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, name);
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaDocument) {
                if (messageObject.isSticker()) {
                    msg = messageObject.getStickerEmoji() != null ? LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, name, messageObject.getStickerEmoji()) : LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, name);
                } else if (messageObject.isGif()) {
                    if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        msg = LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, name);
                    } else {
                        msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "🎬 " + messageObject.messageOwner.message);
                        text[0] = true;
                    }
                } else if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    msg = LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "📎 " + messageObject.messageOwner.message);
                    text[0] = true;
                }
            }
        } else if (chat_id != 0) {
            if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewGroup", true)) {
                msg = (!ChatObject.isChannel(chat) || chat.megagroup) ? LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, name, chat.title) : LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, name);
            } else if (messageObject.messageOwner instanceof TL_messageService) {
                if (messageObject.messageOwner.action instanceof TL_messageActionChatAddUser) {
                    int singleUserId = messageObject.messageOwner.action.user_id;
                    if (singleUserId == 0 && messageObject.messageOwner.action.users.size() == 1) {
                        singleUserId = ((Integer) messageObject.messageOwner.action.users.get(0)).intValue();
                    }
                    if (singleUserId == 0) {
                        StringBuilder stringBuilder = new StringBuilder(TtmlNode.ANONYMOUS_REGION_ID);
                        for (int a = 0; a < messageObject.messageOwner.action.users.size(); a++) {
                            user = MessagesController.getInstance(this.currentAccount).getUser((Integer) messageObject.messageOwner.action.users.get(a));
                            if (user != null) {
                                String name2 = UserObject.getUserName(user);
                                if (stringBuilder.length() != 0) {
                                    stringBuilder.append(", ");
                                }
                                stringBuilder.append(name2);
                            }
                        }
                        msg = LocaleController.formatString("NotificationGroupAddMember", R.string.NotificationGroupAddMember, name, chat.title, stringBuilder.toString());
                    } else if (messageObject.messageOwner.to_id.channel_id != 0 && !chat.megagroup) {
                        msg = LocaleController.formatString("ChannelAddedByNotification", R.string.ChannelAddedByNotification, name, chat.title);
                    } else if (singleUserId == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        msg = LocaleController.formatString("NotificationInvitedToGroup", R.string.NotificationInvitedToGroup, name, chat.title);
                    } else {
                        User u2 = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(singleUserId));
                        if (u2 == null) {
                            return null;
                        }
                        msg = from_id == u2.id ? chat.megagroup ? LocaleController.formatString("NotificationGroupAddSelfMega", R.string.NotificationGroupAddSelfMega, name, chat.title) : LocaleController.formatString("NotificationGroupAddSelf", R.string.NotificationGroupAddSelf, name, chat.title) : LocaleController.formatString("NotificationGroupAddMember", R.string.NotificationGroupAddMember, name, chat.title, UserObject.getUserName(u2));
                    }
                } else if (messageObject.messageOwner.action instanceof TL_messageActionChatJoinedByLink) {
                    msg = LocaleController.formatString("NotificationInvitedToGroupByLink", R.string.NotificationInvitedToGroupByLink, name, chat.title);
                } else if (messageObject.messageOwner.action instanceof TL_messageActionChatEditTitle) {
                    msg = LocaleController.formatString("NotificationEditedGroupName", R.string.NotificationEditedGroupName, name, messageObject.messageOwner.action.title);
                } else if ((messageObject.messageOwner.action instanceof TL_messageActionChatEditPhoto) || (messageObject.messageOwner.action instanceof TL_messageActionChatDeletePhoto)) {
                    msg = (messageObject.messageOwner.to_id.channel_id == 0 || chat.megagroup) ? LocaleController.formatString("NotificationEditedGroupPhoto", R.string.NotificationEditedGroupPhoto, name, chat.title) : LocaleController.formatString("ChannelPhotoEditNotification", R.string.ChannelPhotoEditNotification, chat.title);
                } else if (messageObject.messageOwner.action instanceof TL_messageActionChatDeleteUser) {
                    if (messageObject.messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        msg = LocaleController.formatString("NotificationGroupKickYou", R.string.NotificationGroupKickYou, name, chat.title);
                    } else if (messageObject.messageOwner.action.user_id == from_id) {
                        msg = LocaleController.formatString("NotificationGroupLeftMember", R.string.NotificationGroupLeftMember, name, chat.title);
                    } else {
                        if (MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.action.user_id)) == null) {
                            return null;
                        }
                        msg = LocaleController.formatString("NotificationGroupKickMember", R.string.NotificationGroupKickMember, name, chat.title, UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.action.user_id))));
                    }
                } else if (messageObject.messageOwner.action instanceof TL_messageActionChatCreate) {
                    msg = messageObject.messageText.toString();
                } else if (messageObject.messageOwner.action instanceof TL_messageActionChannelCreate) {
                    msg = messageObject.messageText.toString();
                } else if (messageObject.messageOwner.action instanceof TL_messageActionChatMigrateTo) {
                    msg = LocaleController.formatString("ActionMigrateFromGroupNotify", R.string.ActionMigrateFromGroupNotify, chat.title);
                } else if (messageObject.messageOwner.action instanceof TL_messageActionChannelMigrateFrom) {
                    msg = LocaleController.formatString("ActionMigrateFromGroupNotify", R.string.ActionMigrateFromGroupNotify, messageObject.messageOwner.action.title);
                } else if (messageObject.messageOwner.action instanceof TL_messageActionScreenshotTaken) {
                    msg = messageObject.messageText.toString();
                } else if (messageObject.messageOwner.action instanceof TL_messageActionPinMessage) {
                    MessageObject object;
                    String message;
                    CharSequence message2;
                    if (chat == null || !chat.megagroup) {
                        if (messageObject.replyMessageObject == null) {
                            msg = LocaleController.formatString("NotificationActionPinnedNoTextChannel", R.string.NotificationActionPinnedNoTextChannel, chat.title);
                        } else {
                            object = messageObject.replyMessageObject;
                            if (object.isMusic()) {
                                msg = LocaleController.formatString("NotificationActionPinnedMusicChannel", R.string.NotificationActionPinnedMusicChannel, chat.title);
                            } else if (object.isVideo()) {
                                if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                    msg = LocaleController.formatString("NotificationActionPinnedVideoChannel", R.string.NotificationActionPinnedVideoChannel, chat.title);
                                } else {
                                    message = "📹 " + object.messageOwner.message;
                                    msg = LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message);
                                }
                            } else if (object.isGif()) {
                                if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                    msg = LocaleController.formatString("NotificationActionPinnedGifChannel", R.string.NotificationActionPinnedGifChannel, chat.title);
                                } else {
                                    message = "🎬 " + object.messageOwner.message;
                                    msg = LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message);
                                }
                            } else if (object.isVoice()) {
                                msg = LocaleController.formatString("NotificationActionPinnedVoiceChannel", R.string.NotificationActionPinnedVoiceChannel, chat.title);
                            } else if (object.isRoundVideo()) {
                                msg = LocaleController.formatString("NotificationActionPinnedRoundChannel", R.string.NotificationActionPinnedRoundChannel, chat.title);
                            } else if (object.isSticker()) {
                                msg = object.getStickerEmoji() != null ? LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", R.string.NotificationActionPinnedStickerEmojiChannel, chat.title, object.getStickerEmoji()) : LocaleController.formatString("NotificationActionPinnedStickerChannel", R.string.NotificationActionPinnedStickerChannel, chat.title);
                            } else if (object.messageOwner.media instanceof TL_messageMediaDocument) {
                                if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                    msg = LocaleController.formatString("NotificationActionPinnedFileChannel", R.string.NotificationActionPinnedFileChannel, chat.title);
                                } else {
                                    message = "📎 " + object.messageOwner.message;
                                    msg = LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message);
                                }
                            } else if ((object.messageOwner.media instanceof TL_messageMediaGeo) || (object.messageOwner.media instanceof TL_messageMediaVenue)) {
                                msg = LocaleController.formatString("NotificationActionPinnedGeoChannel", R.string.NotificationActionPinnedGeoChannel, chat.title);
                            } else if (object.messageOwner.media instanceof TL_messageMediaGeoLive) {
                                msg = LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", R.string.NotificationActionPinnedGeoLiveChannel, chat.title);
                            } else if (object.messageOwner.media instanceof TL_messageMediaContact) {
                                msg = LocaleController.formatString("NotificationActionPinnedContactChannel", R.string.NotificationActionPinnedContactChannel, chat.title);
                            } else if (object.messageOwner.media instanceof TL_messageMediaPhoto) {
                                if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                    msg = LocaleController.formatString("NotificationActionPinnedPhotoChannel", R.string.NotificationActionPinnedPhotoChannel, chat.title);
                                } else {
                                    message = "🖼 " + object.messageOwner.message;
                                    msg = LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message);
                                }
                            } else if (object.messageOwner.media instanceof TL_messageMediaGame) {
                                msg = LocaleController.formatString("NotificationActionPinnedGameChannel", R.string.NotificationActionPinnedGameChannel, chat.title);
                            } else if (object.messageText == null || object.messageText.length() <= 0) {
                                msg = LocaleController.formatString("NotificationActionPinnedNoTextChannel", R.string.NotificationActionPinnedNoTextChannel, chat.title);
                            } else {
                                message2 = object.messageText;
                                if (message2.length() > 20) {
                                    message2 = message2.subSequence(0, 20) + "...";
                                }
                                msg = LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message2);
                            }
                        }
                    } else if (messageObject.replyMessageObject == null) {
                        msg = LocaleController.formatString("NotificationActionPinnedNoText", R.string.NotificationActionPinnedNoText, name, chat.title);
                    } else {
                        object = messageObject.replyMessageObject;
                        if (object.isMusic()) {
                            msg = LocaleController.formatString("NotificationActionPinnedMusic", R.string.NotificationActionPinnedMusic, name, chat.title);
                        } else if (object.isVideo()) {
                            if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                msg = LocaleController.formatString("NotificationActionPinnedVideo", R.string.NotificationActionPinnedVideo, name, chat.title);
                            } else {
                                message = "📹 " + object.messageOwner.message;
                                msg = LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message, chat.title);
                            }
                        } else if (object.isGif()) {
                            if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                msg = LocaleController.formatString("NotificationActionPinnedGif", R.string.NotificationActionPinnedGif, name, chat.title);
                            } else {
                                message = "🎬 " + object.messageOwner.message;
                                msg = LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message, chat.title);
                            }
                        } else if (object.isVoice()) {
                            msg = LocaleController.formatString("NotificationActionPinnedVoice", R.string.NotificationActionPinnedVoice, name, chat.title);
                        } else if (object.isRoundVideo()) {
                            msg = LocaleController.formatString("NotificationActionPinnedRound", R.string.NotificationActionPinnedRound, name, chat.title);
                        } else if (object.isSticker()) {
                            msg = object.getStickerEmoji() != null ? LocaleController.formatString("NotificationActionPinnedStickerEmoji", R.string.NotificationActionPinnedStickerEmoji, name, chat.title, object.getStickerEmoji()) : LocaleController.formatString("NotificationActionPinnedSticker", R.string.NotificationActionPinnedSticker, name, chat.title);
                        } else if (object.messageOwner.media instanceof TL_messageMediaDocument) {
                            if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                msg = LocaleController.formatString("NotificationActionPinnedFile", R.string.NotificationActionPinnedFile, name, chat.title);
                            } else {
                                message = "📎 " + object.messageOwner.message;
                                msg = LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message, chat.title);
                            }
                        } else if ((object.messageOwner.media instanceof TL_messageMediaGeo) || (object.messageOwner.media instanceof TL_messageMediaVenue)) {
                            msg = LocaleController.formatString("NotificationActionPinnedGeo", R.string.NotificationActionPinnedGeo, name, chat.title);
                        } else if (object.messageOwner.media instanceof TL_messageMediaGeoLive) {
                            msg = LocaleController.formatString("NotificationActionPinnedGeoLive", R.string.NotificationActionPinnedGeoLive, name, chat.title);
                        } else if (object.messageOwner.media instanceof TL_messageMediaContact) {
                            msg = LocaleController.formatString("NotificationActionPinnedContact", R.string.NotificationActionPinnedContact, name, chat.title);
                        } else if (object.messageOwner.media instanceof TL_messageMediaPhoto) {
                            if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                msg = LocaleController.formatString("NotificationActionPinnedPhoto", R.string.NotificationActionPinnedPhoto, name, chat.title);
                            } else {
                                message = "🖼 " + object.messageOwner.message;
                                msg = LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message, chat.title);
                            }
                        } else if (object.messageOwner.media instanceof TL_messageMediaGame) {
                            msg = LocaleController.formatString("NotificationActionPinnedGame", R.string.NotificationActionPinnedGame, name, chat.title);
                        } else if (object.messageText == null || object.messageText.length() <= 0) {
                            msg = LocaleController.formatString("NotificationActionPinnedNoText", R.string.NotificationActionPinnedNoText, name, chat.title);
                        } else {
                            message2 = object.messageText;
                            if (message2.length() > 20) {
                                message2 = message2.subSequence(0, 20) + "...";
                            }
                            msg = LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message2, chat.title);
                        }
                    }
                } else if (messageObject.messageOwner.action instanceof TL_messageActionGameScore) {
                    msg = messageObject.messageText.toString();
                }
            } else if (!ChatObject.isChannel(chat) || chat.megagroup) {
                if (messageObject.isMediaEmpty()) {
                    msg = (shortMessage || messageObject.messageOwner.message == null || messageObject.messageOwner.message.length() == 0) ? LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, name, chat.title) : LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, messageObject.messageOwner.message);
                } else if (messageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                    msg = (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) ? LocaleController.formatString("NotificationMessageGroupPhoto", R.string.NotificationMessageGroupPhoto, name, chat.title) : LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, "🖼 " + messageObject.messageOwner.message);
                } else if (messageObject.isVideo()) {
                    msg = (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) ? LocaleController.formatString("NotificationMessageGroupVideo", R.string.NotificationMessageGroupVideo, name, chat.title) : LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, "📹 " + messageObject.messageOwner.message);
                } else if (messageObject.isVoice()) {
                    msg = LocaleController.formatString("NotificationMessageGroupAudio", R.string.NotificationMessageGroupAudio, name, chat.title);
                } else if (messageObject.isRoundVideo()) {
                    msg = LocaleController.formatString("NotificationMessageGroupRound", R.string.NotificationMessageGroupRound, name, chat.title);
                } else if (messageObject.isMusic()) {
                    msg = LocaleController.formatString("NotificationMessageGroupMusic", R.string.NotificationMessageGroupMusic, name, chat.title);
                } else if (messageObject.messageOwner.media instanceof TL_messageMediaContact) {
                    msg = LocaleController.formatString("NotificationMessageGroupContact", R.string.NotificationMessageGroupContact, name, chat.title);
                } else if (messageObject.messageOwner.media instanceof TL_messageMediaGame) {
                    msg = LocaleController.formatString("NotificationMessageGroupGame", R.string.NotificationMessageGroupGame, name, chat.title, messageObject.messageOwner.media.game.title);
                } else if ((messageObject.messageOwner.media instanceof TL_messageMediaGeo) || (messageObject.messageOwner.media instanceof TL_messageMediaVenue)) {
                    msg = LocaleController.formatString("NotificationMessageGroupMap", R.string.NotificationMessageGroupMap, name, chat.title);
                } else if (messageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                    msg = LocaleController.formatString("NotificationMessageGroupLiveLocation", R.string.NotificationMessageGroupLiveLocation, name, chat.title);
                } else if (messageObject.messageOwner.media instanceof TL_messageMediaDocument) {
                    if (messageObject.isSticker()) {
                        msg = messageObject.getStickerEmoji() != null ? LocaleController.formatString("NotificationMessageGroupStickerEmoji", R.string.NotificationMessageGroupStickerEmoji, name, chat.title, messageObject.getStickerEmoji()) : LocaleController.formatString("NotificationMessageGroupSticker", R.string.NotificationMessageGroupSticker, name, chat.title);
                    } else {
                        msg = messageObject.isGif() ? (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) ? LocaleController.formatString("NotificationMessageGroupGif", R.string.NotificationMessageGroupGif, name, chat.title) : LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, "🎬 " + messageObject.messageOwner.message) : (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) ? LocaleController.formatString("NotificationMessageGroupDocument", R.string.NotificationMessageGroupDocument, name, chat.title) : LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, "📎 " + messageObject.messageOwner.message);
                    }
                }
            } else if (messageObject.isMediaEmpty()) {
                if (shortMessage || messageObject.messageOwner.message == null || messageObject.messageOwner.message.length() == 0) {
                    msg = LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, messageObject.messageOwner.message);
                    text[0] = true;
                }
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    msg = LocaleController.formatString("ChannelMessagePhoto", R.string.ChannelMessagePhoto, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "🖼 " + messageObject.messageOwner.message);
                    text[0] = true;
                }
            } else if (messageObject.isVideo()) {
                if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    msg = LocaleController.formatString("ChannelMessageVideo", R.string.ChannelMessageVideo, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "📹 " + messageObject.messageOwner.message);
                    text[0] = true;
                }
            } else if (messageObject.isVoice()) {
                msg = LocaleController.formatString("ChannelMessageAudio", R.string.ChannelMessageAudio, name);
            } else if (messageObject.isRoundVideo()) {
                msg = LocaleController.formatString("ChannelMessageRound", R.string.ChannelMessageRound, name);
            } else if (messageObject.isMusic()) {
                msg = LocaleController.formatString("ChannelMessageMusic", R.string.ChannelMessageMusic, name);
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaContact) {
                msg = LocaleController.formatString("ChannelMessageContact", R.string.ChannelMessageContact, name);
            } else if ((messageObject.messageOwner.media instanceof TL_messageMediaGeo) || (messageObject.messageOwner.media instanceof TL_messageMediaVenue)) {
                msg = LocaleController.formatString("ChannelMessageMap", R.string.ChannelMessageMap, name);
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                msg = LocaleController.formatString("ChannelMessageLiveLocation", R.string.ChannelMessageLiveLocation, name);
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaDocument) {
                if (messageObject.isSticker()) {
                    msg = messageObject.getStickerEmoji() != null ? LocaleController.formatString("ChannelMessageStickerEmoji", R.string.ChannelMessageStickerEmoji, name, messageObject.getStickerEmoji()) : LocaleController.formatString("ChannelMessageSticker", R.string.ChannelMessageSticker, name);
                } else if (messageObject.isGif()) {
                    if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        msg = LocaleController.formatString("ChannelMessageGIF", R.string.ChannelMessageGIF, name);
                    } else {
                        msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "🎬 " + messageObject.messageOwner.message);
                        text[0] = true;
                    }
                } else if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    msg = LocaleController.formatString("ChannelMessageDocument", R.string.ChannelMessageDocument, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "📎 " + messageObject.messageOwner.message);
                    text[0] = true;
                }
            }
        }
        return msg;
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
        int notifyOverride = preferences.getInt("notify2_" + dialog_id, 0);
        if (notifyOverride != 3 || preferences.getInt("notifyuntil_" + dialog_id, 0) < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
            return notifyOverride;
        }
        return 2;
    }

    private void dismissNotification() {
        try {
            this.lastNotificationIsNoData = false;
            notificationManager.cancel(this.notificationId);
            this.pushMessages.clear();
            this.pushMessagesDict.clear();
            this.lastWearNotifiedMessageId.clear();
            for (int a = 0; a < this.wearNotificationsIds.size(); a++) {
                notificationManager.cancel(((Integer) this.wearNotificationsIds.valueAt(a)).intValue());
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
        if (this.inChatSoundEnabled && !MediaController.getInstance().isRecordingAudio()) {
            try {
                if (audioManager.getRingerMode() == 0) {
                    return;
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
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
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
        }
    }

    private void scheduleNotificationDelay(boolean onlineReason) {
        try {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("delay notification start, onlineReason = " + onlineReason);
            }
            this.notificationDelayWakelock.acquire(10000);
            notificationsQueue.cancelRunnable(this.notificationDelayRunnable);
            notificationsQueue.postRunnable(this.notificationDelayRunnable, (long) (onlineReason ? 3000 : 1000));
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
        if (pattern == null || pattern.length == 0) {
            return false;
        }
        for (int a = 0; a < pattern.length; a++) {
            if (pattern[0] != 0) {
                return false;
            }
        }
        return true;
    }

    @TargetApi(26)
    private String validateChannelId(long dialogId, String name, long[] vibrationPattern, int ledColor, Uri sound, int importance, long[] configVibrationPattern, Uri configSound, int configImportance) {
        SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
        String key = "org.telegram.key" + dialogId;
        String channelId = preferences.getString(key, null);
        String settings = preferences.getString(key + "_s", null);
        StringBuilder newSettings = new StringBuilder();
        for (long append : vibrationPattern) {
            newSettings.append(append);
        }
        newSettings.append(ledColor);
        if (sound != null) {
            newSettings.append(sound.toString());
        }
        newSettings.append(importance);
        String newSettingsHash = Utilities.MD5(newSettings.toString());
        if (!(channelId == null || settings.equals(newSettingsHash))) {
            if (false) {
                preferences.edit().putString(key, channelId).putString(key + "_s", newSettingsHash).commit();
            } else {
                systemNotificationManager.deleteNotificationChannel(channelId);
                channelId = null;
            }
        }
        if (channelId == null) {
            channelId = this.currentAccount + "channel" + dialogId + "_" + Utilities.random.nextLong();
            NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
            if (ledColor != 0) {
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(ledColor);
            }
            if (isEmptyVibration(vibrationPattern)) {
                notificationChannel.enableVibration(false);
            } else {
                notificationChannel.enableVibration(true);
                if (vibrationPattern != null && vibrationPattern.length > 0) {
                    notificationChannel.setVibrationPattern(vibrationPattern);
                }
            }
            if (sound != null) {
                Builder builder = new Builder();
                builder.setContentType(4);
                builder.setUsage(5);
                notificationChannel.setSound(sound, builder.build());
            } else {
                notificationChannel.setSound(null, null);
            }
            systemNotificationManager.createNotificationChannel(notificationChannel);
            preferences.edit().putString(key, channelId).putString(key + "_s", newSettingsHash).commit();
        }
        return channelId;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showOrUpdateNotification(boolean r85) {
        /*
        r84 = this;
        r0 = r84;
        r5 = r0.currentAccount;
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);
        r5 = r5.isClientActivated();
        if (r5 == 0) goto L_0x0018;
    L_0x000e:
        r0 = r84;
        r5 = r0.pushMessages;
        r5 = r5.isEmpty();
        if (r5 == 0) goto L_0x001c;
    L_0x0018:
        r84.dismissNotification();
    L_0x001b:
        return;
    L_0x001c:
        r0 = r84;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.tgnet.ConnectionsManager.getInstance(r5);	 Catch:{ Exception -> 0x0058 }
        r5.resumeNetworkMaybe();	 Catch:{ Exception -> 0x0058 }
        r0 = r84;
        r5 = r0.pushMessages;	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r0 = r77;
        r44 = r5.get(r0);	 Catch:{ Exception -> 0x0058 }
        r44 = (org.telegram.messenger.MessageObject) r44;	 Catch:{ Exception -> 0x0058 }
        r0 = r84;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r61 = org.telegram.messenger.MessagesController.getNotificationsSettings(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = "dismissDate";
        r77 = 0;
        r0 = r61;
        r1 = r77;
        r29 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.date;	 Catch:{ Exception -> 0x0058 }
        r0 = r29;
        if (r5 > r0) goto L_0x005d;
    L_0x0054:
        r84.dismissNotification();	 Catch:{ Exception -> 0x0058 }
        goto L_0x001b;
    L_0x0058:
        r31 = move-exception;
        org.telegram.messenger.FileLog.e(r31);
        goto L_0x001b;
    L_0x005d:
        r6 = r44.getDialogId();	 Catch:{ Exception -> 0x0058 }
        r58 = r6;
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.mentioned;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0074;
    L_0x006b:
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.from_id;	 Catch:{ Exception -> 0x0058 }
        r0 = (long) r5;	 Catch:{ Exception -> 0x0058 }
        r58 = r0;
    L_0x0074:
        r49 = r44.getId();	 Catch:{ Exception -> 0x0058 }
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.to_id;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.chat_id;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x07c4;
    L_0x0082:
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.to_id;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.chat_id;	 Catch:{ Exception -> 0x0058 }
        r21 = r0;
    L_0x008c:
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.to_id;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.user_id;	 Catch:{ Exception -> 0x0058 }
        r74 = r0;
        if (r74 != 0) goto L_0x07d0;
    L_0x0098:
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.from_id;	 Catch:{ Exception -> 0x0058 }
        r74 = r0;
    L_0x00a0:
        r0 = r84;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);	 Catch:{ Exception -> 0x0058 }
        r77 = java.lang.Integer.valueOf(r74);	 Catch:{ Exception -> 0x0058 }
        r0 = r77;
        r73 = r5.getUser(r0);	 Catch:{ Exception -> 0x0058 }
        r20 = 0;
        if (r21 == 0) goto L_0x00c8;
    L_0x00b6:
        r0 = r84;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);	 Catch:{ Exception -> 0x0058 }
        r77 = java.lang.Integer.valueOf(r21);	 Catch:{ Exception -> 0x0058 }
        r0 = r77;
        r20 = r5.getChat(r0);	 Catch:{ Exception -> 0x0058 }
    L_0x00c8:
        r60 = 0;
        r54 = 0;
        r52 = 0;
        r10 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
        r62 = 0;
        r0 = r84;
        r1 = r61;
        r2 = r58;
        r56 = r0.getNotifyOverride(r1, r2);	 Catch:{ Exception -> 0x0058 }
        if (r85 == 0) goto L_0x0106;
    L_0x00df:
        r5 = 2;
        r0 = r56;
        if (r0 == r5) goto L_0x0106;
    L_0x00e4:
        r5 = "EnableAll";
        r77 = 1;
        r0 = r61;
        r1 = r77;
        r5 = r0.getBoolean(r5, r1);	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0104;
    L_0x00f3:
        if (r21 == 0) goto L_0x0108;
    L_0x00f5:
        r5 = "EnableGroup";
        r77 = 1;
        r0 = r61;
        r1 = r77;
        r5 = r0.getBoolean(r5, r1);	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x0108;
    L_0x0104:
        if (r56 != 0) goto L_0x0108;
    L_0x0106:
        r54 = 1;
    L_0x0108:
        if (r54 != 0) goto L_0x01a0;
    L_0x010a:
        r5 = (r6 > r58 ? 1 : (r6 == r58 ? 0 : -1));
        if (r5 != 0) goto L_0x01a0;
    L_0x010e:
        if (r20 == 0) goto L_0x01a0;
    L_0x0110:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "custom_";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r0 = r61;
        r1 = r77;
        r5 = r0.getBoolean(r5, r1);	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x07ea;
    L_0x0132:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "smart_max_count_";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = 2;
        r0 = r61;
        r1 = r77;
        r55 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "smart_delay_";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        r0 = r61;
        r1 = r77;
        r53 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
    L_0x0172:
        if (r55 == 0) goto L_0x01a0;
    L_0x0174:
        r0 = r84;
        r5 = r0.smartNotificationsDialogs;	 Catch:{ Exception -> 0x0058 }
        r28 = r5.get(r6);	 Catch:{ Exception -> 0x0058 }
        r28 = (android.graphics.Point) r28;	 Catch:{ Exception -> 0x0058 }
        if (r28 != 0) goto L_0x07f0;
    L_0x0180:
        r28 = new android.graphics.Point;	 Catch:{ Exception -> 0x0058 }
        r5 = 1;
        r78 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0058 }
        r80 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r78 = r78 / r80;
        r0 = r78;
        r0 = (int) r0;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r28;
        r1 = r77;
        r0.<init>(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r0 = r84;
        r5 = r0.smartNotificationsDialogs;	 Catch:{ Exception -> 0x0058 }
        r0 = r28;
        r5.put(r6, r0);	 Catch:{ Exception -> 0x0058 }
    L_0x01a0:
        r5 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x0058 }
        r26 = r5.getPath();	 Catch:{ Exception -> 0x0058 }
        r5 = "EnableInAppSounds";
        r77 = 1;
        r0 = r61;
        r1 = r77;
        r39 = r0.getBoolean(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "EnableInAppVibrate";
        r77 = 1;
        r0 = r61;
        r1 = r77;
        r40 = r0.getBoolean(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "EnableInAppPreview";
        r77 = 1;
        r0 = r61;
        r1 = r77;
        r37 = r0.getBoolean(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "EnableInAppPriority";
        r77 = 0;
        r0 = r61;
        r1 = r77;
        r38 = r0.getBoolean(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "custom_";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r0 = r61;
        r1 = r77;
        r25 = r0.getBoolean(r5, r1);	 Catch:{ Exception -> 0x0058 }
        if (r25 == 0) goto L_0x0846;
    L_0x01fc:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "vibrate_";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r0 = r61;
        r1 = r77;
        r76 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "priority_";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = 3;
        r0 = r61;
        r1 = r77;
        r63 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "sound_path_";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r0 = r61;
        r1 = r77;
        r22 = r0.getString(r5, r1);	 Catch:{ Exception -> 0x0058 }
    L_0x025c:
        r75 = 0;
        if (r21 == 0) goto L_0x085d;
    L_0x0260:
        if (r22 == 0) goto L_0x084e;
    L_0x0262:
        r0 = r22;
        r1 = r26;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x084e;
    L_0x026c:
        r22 = 0;
    L_0x026e:
        r5 = "vibrate_group";
        r77 = 0;
        r0 = r61;
        r1 = r77;
        r52 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "priority_group";
        r77 = 1;
        r0 = r61;
        r1 = r77;
        r62 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "GroupLed";
        r77 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
        r0 = r61;
        r1 = r77;
        r10 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
    L_0x0296:
        if (r25 == 0) goto L_0x02d6;
    L_0x0298:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "color_";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r0 = r61;
        r5 = r0.contains(r5);	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x02d6;
    L_0x02b6:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "color_";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r0 = r61;
        r1 = r77;
        r10 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
    L_0x02d6:
        r5 = 3;
        r0 = r63;
        if (r0 == r5) goto L_0x02dd;
    L_0x02db:
        r62 = r63;
    L_0x02dd:
        r5 = 4;
        r0 = r52;
        if (r0 != r5) goto L_0x02e6;
    L_0x02e2:
        r75 = 1;
        r52 = 0;
    L_0x02e6:
        r5 = 2;
        r0 = r52;
        if (r0 != r5) goto L_0x02f5;
    L_0x02eb:
        r5 = 1;
        r0 = r76;
        if (r0 == r5) goto L_0x0306;
    L_0x02f0:
        r5 = 3;
        r0 = r76;
        if (r0 == r5) goto L_0x0306;
    L_0x02f5:
        r5 = 2;
        r0 = r52;
        if (r0 == r5) goto L_0x02ff;
    L_0x02fa:
        r5 = 2;
        r0 = r76;
        if (r0 == r5) goto L_0x0306;
    L_0x02ff:
        if (r76 == 0) goto L_0x0308;
    L_0x0301:
        r5 = 4;
        r0 = r76;
        if (r0 == r5) goto L_0x0308;
    L_0x0306:
        r52 = r76;
    L_0x0308:
        r5 = org.telegram.messenger.ApplicationLoader.mainInterfacePaused;	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x0318;
    L_0x030c:
        if (r39 != 0) goto L_0x0310;
    L_0x030e:
        r22 = 0;
    L_0x0310:
        if (r40 != 0) goto L_0x0314;
    L_0x0312:
        r52 = 2;
    L_0x0314:
        if (r38 != 0) goto L_0x08a5;
    L_0x0316:
        r62 = 0;
    L_0x0318:
        if (r75 == 0) goto L_0x032e;
    L_0x031a:
        r5 = 2;
        r0 = r52;
        if (r0 == r5) goto L_0x032e;
    L_0x031f:
        r5 = audioManager;	 Catch:{ Exception -> 0x08ae }
        r50 = r5.getRingerMode();	 Catch:{ Exception -> 0x08ae }
        if (r50 == 0) goto L_0x032e;
    L_0x0327:
        r5 = 1;
        r0 = r50;
        if (r0 == r5) goto L_0x032e;
    L_0x032c:
        r52 = 2;
    L_0x032e:
        r14 = 0;
        r13 = 0;
        r15 = 0;
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 26;
        r0 = r77;
        if (r5 < r0) goto L_0x0360;
    L_0x0339:
        r5 = 2;
        r0 = r52;
        if (r0 != r5) goto L_0x08b4;
    L_0x033e:
        r5 = 2;
        r13 = new long[r5];	 Catch:{ Exception -> 0x0058 }
        r13 = {0, 0};	 Catch:{ Exception -> 0x0058 }
    L_0x0344:
        if (r22 == 0) goto L_0x035d;
    L_0x0346:
        r5 = "NoSound";
        r0 = r22;
        r5 = r0.equals(r5);	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x035d;
    L_0x0351:
        r0 = r22;
        r1 = r26;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x08da;
    L_0x035b:
        r14 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x0058 }
    L_0x035d:
        if (r62 != 0) goto L_0x08e0;
    L_0x035f:
        r15 = 3;
    L_0x0360:
        if (r54 == 0) goto L_0x0369;
    L_0x0362:
        r52 = 0;
        r62 = 0;
        r10 = 0;
        r22 = 0;
    L_0x0369:
        r42 = new android.content.Intent;	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r77 = org.telegram.ui.LaunchActivity.class;
        r0 = r42;
        r1 = r77;
        r0.<init>(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "com.tmessages.openchat";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r78 = java.lang.Math.random();	 Catch:{ Exception -> 0x0058 }
        r0 = r78;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r0 = r42;
        r0.setAction(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r0 = r42;
        r0.setFlags(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = (int) r6;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0981;
    L_0x03ab:
        r0 = r84;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 != r0) goto L_0x03c5;
    L_0x03b9:
        if (r21 == 0) goto L_0x08fd;
    L_0x03bb:
        r5 = "chatId";
        r0 = r42;
        r1 = r21;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
    L_0x03c5:
        r5 = 0;
        r5 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r5);	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x03d0;
    L_0x03cc:
        r5 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x090b;
    L_0x03d0:
        r60 = 0;
    L_0x03d2:
        r5 = "currentAccount";
        r0 = r84;
        r0 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r42;
        r1 = r77;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r78 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r77;
        r1 = r42;
        r2 = r78;
        r23 = android.app.PendingIntent.getActivity(r5, r0, r1, r2);	 Catch:{ Exception -> 0x0058 }
        r64 = 1;
        if (r21 == 0) goto L_0x03f8;
    L_0x03f6:
        if (r20 == 0) goto L_0x03fa;
    L_0x03f8:
        if (r73 != 0) goto L_0x09a4;
    L_0x03fa:
        r5 = r44.isFcmMessage();	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x09a4;
    L_0x0400:
        r0 = r44;
        r8 = r0.localName;	 Catch:{ Exception -> 0x0058 }
    L_0x0404:
        r5 = (int) r6;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0420;
    L_0x0407:
        r0 = r84;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 > r0) goto L_0x0420;
    L_0x0415:
        r5 = 0;
        r5 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r5);	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x0420;
    L_0x041c:
        r5 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x09b2;
    L_0x0420:
        r5 = "AppName";
        r77 = 2131492982; // 0x7f0c0076 float:1.8609431E38 double:1.053097457E-314;
        r0 = r77;
        r51 = org.telegram.messenger.LocaleController.getString(r5, r0);	 Catch:{ Exception -> 0x0058 }
        r64 = 0;
    L_0x042e:
        r5 = org.telegram.messenger.UserConfig.getActivatedAccountsCount();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 <= r0) goto L_0x09e2;
    L_0x0438:
        r0 = r84;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 != r0) goto L_0x09b6;
    L_0x0446:
        r0 = r84;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.getCurrentUser();	 Catch:{ Exception -> 0x0058 }
        r27 = org.telegram.messenger.UserObject.getFirstName(r5);	 Catch:{ Exception -> 0x0058 }
    L_0x0456:
        r0 = r84;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 != r0) goto L_0x046c;
    L_0x0464:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 23;
        r0 = r77;
        if (r5 >= r0) goto L_0x049c;
    L_0x046c:
        r0 = r84;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 != r0) goto L_0x09e7;
    L_0x047a:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r0 = r27;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = "NewMessages";
        r0 = r84;
        r0 = r0.total_unread_count;	 Catch:{ Exception -> 0x0058 }
        r78 = r0;
        r77 = org.telegram.messenger.LocaleController.formatPluralString(r77, r78);	 Catch:{ Exception -> 0x0058 }
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r27 = r5.toString();	 Catch:{ Exception -> 0x0058 }
    L_0x049c:
        r5 = new android.support.v4.app.NotificationCompat$Builder;	 Catch:{ Exception -> 0x0058 }
        r77 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r0 = r77;
        r5.<init>(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r51;
        r5 = r5.setContentTitle(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = 2131165543; // 0x7f070167 float:1.7945306E38 double:1.0529356804E-314;
        r0 = r77;
        r5 = r5.setSmallIcon(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        r5 = r5.setAutoCancel(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r84;
        r0 = r0.total_unread_count;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r77;
        r5 = r5.setNumber(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r23;
        r5 = r5.setContentIntent(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r84;
        r0 = r0.notificationGroup;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r77;
        r5 = r5.setGroup(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        r5 = r5.setGroupSummary(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        r5 = r5.setShowWhen(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r44;
        r0 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r77;
        r0 = r0.date;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r77;
        r0 = (long) r0;	 Catch:{ Exception -> 0x0058 }
        r78 = r0;
        r80 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r78 = r78 * r80;
        r0 = r78;
        r5 = r5.setWhen(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = -13851168; // 0xffffffffff2ca5e0 float:-2.2948849E38 double:NaN;
        r0 = r77;
        r46 = r5.setColor(r0);	 Catch:{ Exception -> 0x0058 }
        r9 = 0;
        r12 = 0;
        r11 = 0;
        r5 = "msg";
        r0 = r46;
        r0.setCategory(r5);	 Catch:{ Exception -> 0x0058 }
        if (r20 != 0) goto L_0x0550;
    L_0x051b:
        if (r73 == 0) goto L_0x0550;
    L_0x051d:
        r0 = r73;
        r5 = r0.phone;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0550;
    L_0x0523:
        r0 = r73;
        r5 = r0.phone;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.length();	 Catch:{ Exception -> 0x0058 }
        if (r5 <= 0) goto L_0x0550;
    L_0x052d:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = "tel:+";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r73;
        r0 = r0.phone;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r0.addPerson(r5);	 Catch:{ Exception -> 0x0058 }
    L_0x0550:
        r69 = 2;
        r43 = 0;
        r34 = 0;
        r0 = r84;
        r5 = r0.pushMessages;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 != r0) goto L_0x0a89;
    L_0x0564:
        r0 = r84;
        r5 = r0.pushMessages;	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r0 = r77;
        r48 = r5.get(r0);	 Catch:{ Exception -> 0x0058 }
        r48 = (org.telegram.messenger.MessageObject) r48;	 Catch:{ Exception -> 0x0058 }
        r5 = 1;
        r0 = new boolean[r5];	 Catch:{ Exception -> 0x0058 }
        r72 = r0;
        r5 = 0;
        r0 = r84;
        r1 = r48;
        r2 = r72;
        r43 = r0.getStringForMessage(r1, r5, r2);	 Catch:{ Exception -> 0x0058 }
        r47 = r43;
        r0 = r48;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.silent;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0a36;
    L_0x058c:
        r69 = 1;
    L_0x058e:
        if (r47 == 0) goto L_0x001b;
    L_0x0590:
        if (r64 == 0) goto L_0x05b7;
    L_0x0592:
        if (r20 == 0) goto L_0x0a3a;
    L_0x0594:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = " @ ";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r51;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = "";
        r0 = r47;
        r1 = r77;
        r47 = r0.replace(r5, r1);	 Catch:{ Exception -> 0x0058 }
    L_0x05b7:
        r46.setContentText(r47);	 Catch:{ Exception -> 0x0058 }
        r5 = new android.support.v4.app.NotificationCompat$BigTextStyle;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r0 = r47;
        r5 = r5.bigText(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r0.setStyle(r5);	 Catch:{ Exception -> 0x0058 }
    L_0x05ca:
        r30 = new android.content.Intent;	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r77 = org.telegram.messenger.NotificationDismissReceiver.class;
        r0 = r30;
        r1 = r77;
        r0.<init>(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "messageDate";
        r0 = r44;
        r0 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r77;
        r0 = r0.date;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r30;
        r1 = r77;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "currentAccount";
        r0 = r84;
        r0 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r30;
        r1 = r77;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r78 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r0 = r77;
        r1 = r30;
        r2 = r78;
        r5 = android.app.PendingIntent.getBroadcast(r5, r0, r1, r2);	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r0.setDeleteIntent(r5);	 Catch:{ Exception -> 0x0058 }
        if (r60 == 0) goto L_0x0632;
    L_0x0614:
        r5 = org.telegram.messenger.ImageLoader.getInstance();	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r78 = "50_50";
        r0 = r60;
        r1 = r77;
        r2 = r78;
        r36 = r5.getImageFromMemory(r0, r1, r2);	 Catch:{ Exception -> 0x0058 }
        if (r36 == 0) goto L_0x0b8f;
    L_0x0629:
        r5 = r36.getBitmap();	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r0.setLargeIcon(r5);	 Catch:{ Exception -> 0x0058 }
    L_0x0632:
        if (r85 == 0) goto L_0x0639;
    L_0x0634:
        r5 = 1;
        r0 = r69;
        if (r0 != r5) goto L_0x0bd7;
    L_0x0639:
        r5 = -1;
        r0 = r46;
        r0.setPriority(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 26;
        r0 = r77;
        if (r5 < r0) goto L_0x0648;
    L_0x0647:
        r12 = 2;
    L_0x0648:
        r5 = 1;
        r0 = r69;
        if (r0 == r5) goto L_0x0c93;
    L_0x064d:
        if (r54 != 0) goto L_0x0c93;
    L_0x064f:
        r5 = org.telegram.messenger.ApplicationLoader.mainInterfacePaused;	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x0655;
    L_0x0653:
        if (r37 == 0) goto L_0x0698;
    L_0x0655:
        r5 = r43.length();	 Catch:{ Exception -> 0x0058 }
        r77 = 100;
        r0 = r77;
        if (r5 <= r0) goto L_0x0691;
    L_0x065f:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = 0;
        r78 = 100;
        r0 = r43;
        r1 = r77;
        r2 = r78;
        r77 = r0.substring(r1, r2);	 Catch:{ Exception -> 0x0058 }
        r78 = 10;
        r79 = 32;
        r77 = r77.replace(r78, r79);	 Catch:{ Exception -> 0x0058 }
        r77 = r77.trim();	 Catch:{ Exception -> 0x0058 }
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = "...";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r43 = r5.toString();	 Catch:{ Exception -> 0x0058 }
    L_0x0691:
        r0 = r46;
        r1 = r43;
        r0.setTicker(r1);	 Catch:{ Exception -> 0x0058 }
    L_0x0698:
        r5 = org.telegram.messenger.MediaController.getInstance();	 Catch:{ Exception -> 0x0058 }
        r5 = r5.isRecordingAudio();	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x06c3;
    L_0x06a2:
        if (r22 == 0) goto L_0x06c3;
    L_0x06a4:
        r5 = "NoSound";
        r0 = r22;
        r5 = r0.equals(r5);	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x06c3;
    L_0x06af:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 26;
        r0 = r77;
        if (r5 < r0) goto L_0x0c37;
    L_0x06b7:
        r0 = r22;
        r1 = r26;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0c31;
    L_0x06c1:
        r11 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x0058 }
    L_0x06c3:
        if (r10 == 0) goto L_0x06d0;
    L_0x06c5:
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r77 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = r46;
        r1 = r77;
        r0.setLights(r10, r5, r1);	 Catch:{ Exception -> 0x0058 }
    L_0x06d0:
        r5 = 2;
        r0 = r52;
        if (r0 == r5) goto L_0x06df;
    L_0x06d5:
        r5 = org.telegram.messenger.MediaController.getInstance();	 Catch:{ Exception -> 0x0058 }
        r5 = r5.isRecordingAudio();	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0c5d;
    L_0x06df:
        r5 = 2;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0058 }
        r9 = {0, 0};	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r0.setVibrate(r9);	 Catch:{ Exception -> 0x0058 }
    L_0x06ea:
        r33 = 0;
        r5 = 0;
        r5 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r5);	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x0ca4;
    L_0x06f3:
        r5 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x0ca4;
    L_0x06f7:
        r78 = r44.getDialogId();	 Catch:{ Exception -> 0x0058 }
        r80 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
        r5 = (r78 > r80 ? 1 : (r78 == r80 ? 0 : -1));
        if (r5 != 0) goto L_0x0ca4;
    L_0x0702:
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.reply_markup;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0ca4;
    L_0x070a:
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.reply_markup;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.rows;	 Catch:{ Exception -> 0x0058 }
        r67 = r0;
        r4 = 0;
        r70 = r67.size();	 Catch:{ Exception -> 0x0058 }
    L_0x0719:
        r0 = r70;
        if (r4 >= r0) goto L_0x0ca4;
    L_0x071d:
        r0 = r67;
        r66 = r0.get(r4);	 Catch:{ Exception -> 0x0058 }
        r66 = (org.telegram.tgnet.TLRPC.TL_keyboardButtonRow) r66;	 Catch:{ Exception -> 0x0058 }
        r16 = 0;
        r0 = r66;
        r5 = r0.buttons;	 Catch:{ Exception -> 0x0058 }
        r71 = r5.size();	 Catch:{ Exception -> 0x0058 }
    L_0x072f:
        r0 = r16;
        r1 = r71;
        if (r0 >= r1) goto L_0x0ca0;
    L_0x0735:
        r0 = r66;
        r5 = r0.buttons;	 Catch:{ Exception -> 0x0058 }
        r0 = r16;
        r18 = r5.get(r0);	 Catch:{ Exception -> 0x0058 }
        r18 = (org.telegram.tgnet.TLRPC.KeyboardButton) r18;	 Catch:{ Exception -> 0x0058 }
        r0 = r18;
        r5 = r0 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x07c0;
    L_0x0747:
        r19 = new android.content.Intent;	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r77 = org.telegram.messenger.NotificationCallbackReceiver.class;
        r0 = r19;
        r1 = r77;
        r0.<init>(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "currentAccount";
        r0 = r84;
        r0 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r19;
        r1 = r77;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "did";
        r0 = r19;
        r0.putExtra(r5, r6);	 Catch:{ Exception -> 0x0058 }
        r0 = r18;
        r5 = r0.data;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0782;
    L_0x0772:
        r5 = "data";
        r0 = r18;
        r0 = r0.data;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r19;
        r1 = r77;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
    L_0x0782:
        r5 = "mid";
        r77 = r44.getId();	 Catch:{ Exception -> 0x0058 }
        r0 = r19;
        r1 = r77;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = 0;
        r0 = r18;
        r0 = r0.text;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r78 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r0 = r84;
        r0 = r0.lastButtonId;	 Catch:{ Exception -> 0x0058 }
        r79 = r0;
        r80 = r79 + 1;
        r0 = r80;
        r1 = r84;
        r1.lastButtonId = r0;	 Catch:{ Exception -> 0x0058 }
        r80 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r0 = r78;
        r1 = r79;
        r2 = r19;
        r3 = r80;
        r78 = android.app.PendingIntent.getBroadcast(r0, r1, r2, r3);	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r1 = r77;
        r2 = r78;
        r0.addAction(r5, r1, r2);	 Catch:{ Exception -> 0x0058 }
        r33 = 1;
    L_0x07c0:
        r16 = r16 + 1;
        goto L_0x072f;
    L_0x07c4:
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.to_id;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.channel_id;	 Catch:{ Exception -> 0x0058 }
        r21 = r0;
        goto L_0x008c;
    L_0x07d0:
        r0 = r84;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.getClientUserId();	 Catch:{ Exception -> 0x0058 }
        r0 = r74;
        if (r0 != r5) goto L_0x00a0;
    L_0x07e0:
        r0 = r44;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.from_id;	 Catch:{ Exception -> 0x0058 }
        r74 = r0;
        goto L_0x00a0;
    L_0x07ea:
        r55 = 2;
        r53 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        goto L_0x0172;
    L_0x07f0:
        r0 = r28;
        r0 = r0.y;	 Catch:{ Exception -> 0x0058 }
        r45 = r0;
        r5 = r45 + r53;
        r0 = (long) r5;	 Catch:{ Exception -> 0x0058 }
        r78 = r0;
        r80 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0058 }
        r82 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r80 = r80 / r82;
        r5 = (r78 > r80 ? 1 : (r78 == r80 ? 0 : -1));
        if (r5 >= 0) goto L_0x081e;
    L_0x0807:
        r5 = 1;
        r78 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0058 }
        r80 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r78 = r78 / r80;
        r0 = r78;
        r0 = (int) r0;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r28;
        r1 = r77;
        r0.set(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x01a0;
    L_0x081e:
        r0 = r28;
        r0 = r0.x;	 Catch:{ Exception -> 0x0058 }
        r24 = r0;
        r0 = r24;
        r1 = r55;
        if (r0 >= r1) goto L_0x0842;
    L_0x082a:
        r5 = r24 + 1;
        r78 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0058 }
        r80 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r78 = r78 / r80;
        r0 = r78;
        r0 = (int) r0;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r28;
        r1 = r77;
        r0.set(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x01a0;
    L_0x0842:
        r54 = 1;
        goto L_0x01a0;
    L_0x0846:
        r76 = 0;
        r63 = 3;
        r22 = 0;
        goto L_0x025c;
    L_0x084e:
        if (r22 != 0) goto L_0x026e;
    L_0x0850:
        r5 = "GroupSoundPath";
        r0 = r61;
        r1 = r26;
        r22 = r0.getString(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x026e;
    L_0x085d:
        if (r74 == 0) goto L_0x0296;
    L_0x085f:
        if (r22 == 0) goto L_0x0897;
    L_0x0861:
        r0 = r22;
        r1 = r26;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0897;
    L_0x086b:
        r22 = 0;
    L_0x086d:
        r5 = "vibrate_messages";
        r77 = 0;
        r0 = r61;
        r1 = r77;
        r52 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "priority_group";
        r77 = 1;
        r0 = r61;
        r1 = r77;
        r62 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "MessagesLed";
        r77 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
        r0 = r61;
        r1 = r77;
        r10 = r0.getInt(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x0296;
    L_0x0897:
        if (r22 != 0) goto L_0x086d;
    L_0x0899:
        r5 = "GlobalSoundPath";
        r0 = r61;
        r1 = r26;
        r22 = r0.getString(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x086d;
    L_0x08a5:
        r5 = 2;
        r0 = r62;
        if (r0 != r5) goto L_0x0318;
    L_0x08aa:
        r62 = 1;
        goto L_0x0318;
    L_0x08ae:
        r31 = move-exception;
        org.telegram.messenger.FileLog.e(r31);	 Catch:{ Exception -> 0x0058 }
        goto L_0x032e;
    L_0x08b4:
        r5 = 1;
        r0 = r52;
        if (r0 != r5) goto L_0x08c1;
    L_0x08b9:
        r5 = 4;
        r13 = new long[r5];	 Catch:{ Exception -> 0x0058 }
        r13 = {0, 100, 0, 100};	 Catch:{ Exception -> 0x0058 }
        goto L_0x0344;
    L_0x08c1:
        if (r52 == 0) goto L_0x08c8;
    L_0x08c3:
        r5 = 4;
        r0 = r52;
        if (r0 != r5) goto L_0x08cd;
    L_0x08c8:
        r5 = 0;
        r13 = new long[r5];	 Catch:{ Exception -> 0x0058 }
        goto L_0x0344;
    L_0x08cd:
        r5 = 3;
        r0 = r52;
        if (r0 != r5) goto L_0x0344;
    L_0x08d2:
        r5 = 2;
        r13 = new long[r5];	 Catch:{ Exception -> 0x0058 }
        r13 = {0, 1000};	 Catch:{ Exception -> 0x0058 }
        goto L_0x0344;
    L_0x08da:
        r14 = android.net.Uri.parse(r22);	 Catch:{ Exception -> 0x0058 }
        goto L_0x035d;
    L_0x08e0:
        r5 = 1;
        r0 = r62;
        if (r0 == r5) goto L_0x08ea;
    L_0x08e5:
        r5 = 2;
        r0 = r62;
        if (r0 != r5) goto L_0x08ed;
    L_0x08ea:
        r15 = 4;
        goto L_0x0360;
    L_0x08ed:
        r5 = 4;
        r0 = r62;
        if (r0 != r5) goto L_0x08f5;
    L_0x08f2:
        r15 = 1;
        goto L_0x0360;
    L_0x08f5:
        r5 = 5;
        r0 = r62;
        if (r0 != r5) goto L_0x0360;
    L_0x08fa:
        r15 = 2;
        goto L_0x0360;
    L_0x08fd:
        if (r74 == 0) goto L_0x03c5;
    L_0x08ff:
        r5 = "userId";
        r0 = r42;
        r1 = r74;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x03c5;
    L_0x090b:
        r0 = r84;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 != r0) goto L_0x03d2;
    L_0x0919:
        if (r20 == 0) goto L_0x094d;
    L_0x091b:
        r0 = r20;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x03d2;
    L_0x0921:
        r0 = r20;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x03d2;
    L_0x0929:
        r0 = r20;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.volume_id;	 Catch:{ Exception -> 0x0058 }
        r78 = r0;
        r80 = 0;
        r5 = (r78 > r80 ? 1 : (r78 == r80 ? 0 : -1));
        if (r5 == 0) goto L_0x03d2;
    L_0x0939:
        r0 = r20;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.local_id;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x03d2;
    L_0x0943:
        r0 = r20;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.photo_small;	 Catch:{ Exception -> 0x0058 }
        r60 = r0;
        goto L_0x03d2;
    L_0x094d:
        if (r73 == 0) goto L_0x03d2;
    L_0x094f:
        r0 = r73;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x03d2;
    L_0x0955:
        r0 = r73;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x03d2;
    L_0x095d:
        r0 = r73;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.volume_id;	 Catch:{ Exception -> 0x0058 }
        r78 = r0;
        r80 = 0;
        r5 = (r78 > r80 ? 1 : (r78 == r80 ? 0 : -1));
        if (r5 == 0) goto L_0x03d2;
    L_0x096d:
        r0 = r73;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.local_id;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x03d2;
    L_0x0977:
        r0 = r73;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0058 }
        r0 = r5.photo_small;	 Catch:{ Exception -> 0x0058 }
        r60 = r0;
        goto L_0x03d2;
    L_0x0981:
        r0 = r84;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 != r0) goto L_0x03d2;
    L_0x098f:
        r5 = "encId";
        r77 = 32;
        r78 = r6 >> r77;
        r0 = r78;
        r0 = (int) r0;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r42;
        r1 = r77;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x03d2;
    L_0x09a4:
        if (r20 == 0) goto L_0x09ac;
    L_0x09a6:
        r0 = r20;
        r8 = r0.title;	 Catch:{ Exception -> 0x0058 }
        goto L_0x0404;
    L_0x09ac:
        r8 = org.telegram.messenger.UserObject.getUserName(r73);	 Catch:{ Exception -> 0x0058 }
        goto L_0x0404;
    L_0x09b2:
        r51 = r8;
        goto L_0x042e;
    L_0x09b6:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r0 = r84;
        r0 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r77 = org.telegram.messenger.UserConfig.getInstance(r77);	 Catch:{ Exception -> 0x0058 }
        r77 = r77.getCurrentUser();	 Catch:{ Exception -> 0x0058 }
        r77 = org.telegram.messenger.UserObject.getFirstName(r77);	 Catch:{ Exception -> 0x0058 }
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = "・";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r27 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        goto L_0x0456;
    L_0x09e2:
        r27 = "";
        goto L_0x0456;
    L_0x09e7:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r0 = r27;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = "NotificationMessagesPeopleDisplayOrder";
        r78 = 2131494003; // 0x7f0c0473 float:1.8611502E38 double:1.053097961E-314;
        r79 = 2;
        r0 = r79;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x0058 }
        r79 = r0;
        r80 = 0;
        r81 = "NewMessages";
        r0 = r84;
        r0 = r0.total_unread_count;	 Catch:{ Exception -> 0x0058 }
        r82 = r0;
        r81 = org.telegram.messenger.LocaleController.formatPluralString(r81, r82);	 Catch:{ Exception -> 0x0058 }
        r79[r80] = r81;	 Catch:{ Exception -> 0x0058 }
        r80 = 1;
        r81 = "FromChats";
        r0 = r84;
        r0 = r0.pushDialogs;	 Catch:{ Exception -> 0x0058 }
        r82 = r0;
        r82 = r82.size();	 Catch:{ Exception -> 0x0058 }
        r81 = org.telegram.messenger.LocaleController.formatPluralString(r81, r82);	 Catch:{ Exception -> 0x0058 }
        r79[r80] = r81;	 Catch:{ Exception -> 0x0058 }
        r77 = org.telegram.messenger.LocaleController.formatString(r77, r78, r79);	 Catch:{ Exception -> 0x0058 }
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r27 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        goto L_0x049c;
    L_0x0a36:
        r69 = 0;
        goto L_0x058e;
    L_0x0a3a:
        r5 = 0;
        r5 = r72[r5];	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0a64;
    L_0x0a3f:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r0 = r51;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = ": ";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = "";
        r0 = r47;
        r1 = r77;
        r47 = r0.replace(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x05b7;
    L_0x0a64:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r0 = r51;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = " ";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = "";
        r0 = r47;
        r1 = r77;
        r47 = r0.replace(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x05b7;
    L_0x0a89:
        r0 = r46;
        r1 = r27;
        r0.setContentText(r1);	 Catch:{ Exception -> 0x0058 }
        r41 = new android.support.v4.app.NotificationCompat$InboxStyle;	 Catch:{ Exception -> 0x0058 }
        r41.<init>();	 Catch:{ Exception -> 0x0058 }
        r0 = r41;
        r1 = r51;
        r0.setBigContentTitle(r1);	 Catch:{ Exception -> 0x0058 }
        r5 = 10;
        r0 = r84;
        r0 = r0.pushMessages;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r77 = r77.size();	 Catch:{ Exception -> 0x0058 }
        r0 = r77;
        r24 = java.lang.Math.min(r5, r0);	 Catch:{ Exception -> 0x0058 }
        r5 = 1;
        r0 = new boolean[r5];	 Catch:{ Exception -> 0x0058 }
        r72 = r0;
        r35 = 0;
    L_0x0ab5:
        r0 = r35;
        r1 = r24;
        if (r0 >= r1) goto L_0x0b7f;
    L_0x0abb:
        r0 = r84;
        r5 = r0.pushMessages;	 Catch:{ Exception -> 0x0058 }
        r0 = r35;
        r48 = r5.get(r0);	 Catch:{ Exception -> 0x0058 }
        r48 = (org.telegram.messenger.MessageObject) r48;	 Catch:{ Exception -> 0x0058 }
        r5 = 0;
        r0 = r84;
        r1 = r48;
        r2 = r72;
        r47 = r0.getStringForMessage(r1, r5, r2);	 Catch:{ Exception -> 0x0058 }
        if (r47 == 0) goto L_0x0ade;
    L_0x0ad4:
        r0 = r48;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.date;	 Catch:{ Exception -> 0x0058 }
        r0 = r29;
        if (r5 > r0) goto L_0x0ae1;
    L_0x0ade:
        r35 = r35 + 1;
        goto L_0x0ab5;
    L_0x0ae1:
        r5 = 2;
        r0 = r69;
        if (r0 != r5) goto L_0x0af2;
    L_0x0ae6:
        r43 = r47;
        r0 = r48;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.silent;	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0b2f;
    L_0x0af0:
        r69 = 1;
    L_0x0af2:
        r0 = r84;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0058 }
        r77 = 1;
        r0 = r77;
        if (r5 != r0) goto L_0x0b27;
    L_0x0b00:
        if (r64 == 0) goto L_0x0b27;
    L_0x0b02:
        if (r20 == 0) goto L_0x0b32;
    L_0x0b04:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r77 = " @ ";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r0 = r51;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = "";
        r0 = r47;
        r1 = r77;
        r47 = r0.replace(r5, r1);	 Catch:{ Exception -> 0x0058 }
    L_0x0b27:
        r0 = r41;
        r1 = r47;
        r0.addLine(r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x0ade;
    L_0x0b2f:
        r69 = 0;
        goto L_0x0af2;
    L_0x0b32:
        r5 = 0;
        r5 = r72[r5];	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0b5b;
    L_0x0b37:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r0 = r51;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = ": ";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = "";
        r0 = r47;
        r1 = r77;
        r47 = r0.replace(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x0b27;
    L_0x0b5b:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0058 }
        r5.<init>();	 Catch:{ Exception -> 0x0058 }
        r0 = r51;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r77 = " ";
        r0 = r77;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0058 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0058 }
        r77 = "";
        r0 = r47;
        r1 = r77;
        r47 = r0.replace(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x0b27;
    L_0x0b7f:
        r0 = r41;
        r1 = r27;
        r0.setSummaryText(r1);	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r1 = r41;
        r0.setStyle(r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x05ca;
    L_0x0b8f:
        r5 = 1;
        r0 = r60;
        r32 = org.telegram.messenger.FileLoader.getPathToAttach(r0, r5);	 Catch:{ Throwable -> 0x0bd0 }
        r5 = r32.exists();	 Catch:{ Throwable -> 0x0bd0 }
        if (r5 == 0) goto L_0x0632;
    L_0x0b9c:
        r5 = 1126170624; // 0x43200000 float:160.0 double:5.564022167E-315;
        r77 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r77 = org.telegram.messenger.AndroidUtilities.dp(r77);	 Catch:{ Throwable -> 0x0bd0 }
        r0 = r77;
        r0 = (float) r0;	 Catch:{ Throwable -> 0x0bd0 }
        r77 = r0;
        r68 = r5 / r77;
        r57 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x0bd0 }
        r57.<init>();	 Catch:{ Throwable -> 0x0bd0 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r5 = (r68 > r5 ? 1 : (r68 == r5 ? 0 : -1));
        if (r5 >= 0) goto L_0x0bd3;
    L_0x0bb6:
        r5 = 1;
    L_0x0bb7:
        r0 = r57;
        r0.inSampleSize = r5;	 Catch:{ Throwable -> 0x0bd0 }
        r5 = r32.getAbsolutePath();	 Catch:{ Throwable -> 0x0bd0 }
        r0 = r57;
        r17 = android.graphics.BitmapFactory.decodeFile(r5, r0);	 Catch:{ Throwable -> 0x0bd0 }
        if (r17 == 0) goto L_0x0632;
    L_0x0bc7:
        r0 = r46;
        r1 = r17;
        r0.setLargeIcon(r1);	 Catch:{ Throwable -> 0x0bd0 }
        goto L_0x0632;
    L_0x0bd0:
        r5 = move-exception;
        goto L_0x0632;
    L_0x0bd3:
        r0 = r68;
        r5 = (int) r0;
        goto L_0x0bb7;
    L_0x0bd7:
        if (r62 != 0) goto L_0x0bea;
    L_0x0bd9:
        r5 = 0;
        r0 = r46;
        r0.setPriority(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 26;
        r0 = r77;
        if (r5 < r0) goto L_0x0648;
    L_0x0be7:
        r12 = 3;
        goto L_0x0648;
    L_0x0bea:
        r5 = 1;
        r0 = r62;
        if (r0 == r5) goto L_0x0bf4;
    L_0x0bef:
        r5 = 2;
        r0 = r62;
        if (r0 != r5) goto L_0x0c05;
    L_0x0bf4:
        r5 = 1;
        r0 = r46;
        r0.setPriority(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 26;
        r0 = r77;
        if (r5 < r0) goto L_0x0648;
    L_0x0c02:
        r12 = 4;
        goto L_0x0648;
    L_0x0c05:
        r5 = 4;
        r0 = r62;
        if (r0 != r5) goto L_0x0c1b;
    L_0x0c0a:
        r5 = -2;
        r0 = r46;
        r0.setPriority(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 26;
        r0 = r77;
        if (r5 < r0) goto L_0x0648;
    L_0x0c18:
        r12 = 1;
        goto L_0x0648;
    L_0x0c1b:
        r5 = 5;
        r0 = r62;
        if (r0 != r5) goto L_0x0648;
    L_0x0c20:
        r5 = -1;
        r0 = r46;
        r0.setPriority(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 26;
        r0 = r77;
        if (r5 < r0) goto L_0x0648;
    L_0x0c2e:
        r12 = 2;
        goto L_0x0648;
    L_0x0c31:
        r11 = android.net.Uri.parse(r22);	 Catch:{ Exception -> 0x0058 }
        goto L_0x06c3;
    L_0x0c37:
        r0 = r22;
        r1 = r26;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0c4e;
    L_0x0c41:
        r5 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x0058 }
        r77 = 5;
        r0 = r46;
        r1 = r77;
        r0.setSound(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x06c3;
    L_0x0c4e:
        r5 = android.net.Uri.parse(r22);	 Catch:{ Exception -> 0x0058 }
        r77 = 5;
        r0 = r46;
        r1 = r77;
        r0.setSound(r5, r1);	 Catch:{ Exception -> 0x0058 }
        goto L_0x06c3;
    L_0x0c5d:
        r5 = 1;
        r0 = r52;
        if (r0 != r5) goto L_0x0c6f;
    L_0x0c62:
        r5 = 4;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0058 }
        r9 = {0, 100, 0, 100};	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r0.setVibrate(r9);	 Catch:{ Exception -> 0x0058 }
        goto L_0x06ea;
    L_0x0c6f:
        if (r52 == 0) goto L_0x0c76;
    L_0x0c71:
        r5 = 4;
        r0 = r52;
        if (r0 != r5) goto L_0x0c81;
    L_0x0c76:
        r5 = 2;
        r0 = r46;
        r0.setDefaults(r5);	 Catch:{ Exception -> 0x0058 }
        r5 = 0;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0058 }
        goto L_0x06ea;
    L_0x0c81:
        r5 = 3;
        r0 = r52;
        if (r0 != r5) goto L_0x06ea;
    L_0x0c86:
        r5 = 2;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0058 }
        r9 = {0, 1000};	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r0.setVibrate(r9);	 Catch:{ Exception -> 0x0058 }
        goto L_0x06ea;
    L_0x0c93:
        r5 = 2;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0058 }
        r9 = {0, 0};	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r0.setVibrate(r9);	 Catch:{ Exception -> 0x0058 }
        goto L_0x06ea;
    L_0x0ca0:
        r4 = r4 + 1;
        goto L_0x0719;
    L_0x0ca4:
        if (r33 != 0) goto L_0x0d09;
    L_0x0ca6:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 24;
        r0 = r77;
        if (r5 >= r0) goto L_0x0d09;
    L_0x0cae:
        r5 = org.telegram.messenger.SharedConfig.passcodeHash;	 Catch:{ Exception -> 0x0058 }
        r5 = r5.length();	 Catch:{ Exception -> 0x0058 }
        if (r5 != 0) goto L_0x0d09;
    L_0x0cb6:
        r5 = r84.hasMessagesToReply();	 Catch:{ Exception -> 0x0058 }
        if (r5 == 0) goto L_0x0d09;
    L_0x0cbc:
        r65 = new android.content.Intent;	 Catch:{ Exception -> 0x0058 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r77 = org.telegram.messenger.PopupReplyReceiver.class;
        r0 = r65;
        r1 = r77;
        r0.<init>(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = "currentAccount";
        r0 = r84;
        r0 = r0.currentAccount;	 Catch:{ Exception -> 0x0058 }
        r77 = r0;
        r0 = r65;
        r1 = r77;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0058 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 19;
        r0 = r77;
        if (r5 > r0) goto L_0x0d31;
    L_0x0ce1:
        r5 = 2131165355; // 0x7f0700ab float:1.7944925E38 double:1.0529355875E-314;
        r77 = "Reply";
        r78 = 2131494239; // 0x7f0c055f float:1.861198E38 double:1.053098078E-314;
        r77 = org.telegram.messenger.LocaleController.getString(r77, r78);	 Catch:{ Exception -> 0x0058 }
        r78 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r79 = 2;
        r80 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r0 = r78;
        r1 = r79;
        r2 = r65;
        r3 = r80;
        r78 = android.app.PendingIntent.getBroadcast(r0, r1, r2, r3);	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r1 = r77;
        r2 = r78;
        r0.addAction(r5, r1, r2);	 Catch:{ Exception -> 0x0058 }
    L_0x0d09:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0058 }
        r77 = 26;
        r0 = r77;
        if (r5 < r0) goto L_0x0d1c;
    L_0x0d11:
        r5 = r84;
        r5 = r5.validateChannelId(r6, r8, r9, r10, r11, r12, r13, r14, r15);	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r0.setChannelId(r5);	 Catch:{ Exception -> 0x0058 }
    L_0x0d1c:
        r0 = r84;
        r1 = r46;
        r2 = r85;
        r3 = r27;
        r0.showExtraNotifications(r1, r2, r3);	 Catch:{ Exception -> 0x0058 }
        r5 = 0;
        r0 = r84;
        r0.lastNotificationIsNoData = r5;	 Catch:{ Exception -> 0x0058 }
        r84.scheduleNotificationRepeat();	 Catch:{ Exception -> 0x0058 }
        goto L_0x001b;
    L_0x0d31:
        r5 = 2131165354; // 0x7f0700aa float:1.7944923E38 double:1.052935587E-314;
        r77 = "Reply";
        r78 = 2131494239; // 0x7f0c055f float:1.861198E38 double:1.053098078E-314;
        r77 = org.telegram.messenger.LocaleController.getString(r77, r78);	 Catch:{ Exception -> 0x0058 }
        r78 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0058 }
        r79 = 2;
        r80 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r0 = r78;
        r1 = r79;
        r2 = r65;
        r3 = r80;
        r78 = android.app.PendingIntent.getBroadcast(r0, r1, r2, r3);	 Catch:{ Exception -> 0x0058 }
        r0 = r46;
        r1 = r77;
        r2 = r78;
        r0.addAction(r5, r1, r2);	 Catch:{ Exception -> 0x0058 }
        goto L_0x0d09;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationsController.showOrUpdateNotification(boolean):void");
    }

    @SuppressLint({"InlinedApi"})
    private void showExtraNotifications(NotificationCompat.Builder notificationBuilder, boolean notifyAboutLast, String summary) {
        Notification mainNotification = notificationBuilder.build();
        if (VERSION.SDK_INT < 18) {
            notificationManager.notify(this.notificationId, mainNotification);
            return;
        }
        int a;
        ArrayList<Long> sortedDialogs = new ArrayList();
        LongSparseArray<ArrayList<MessageObject>> messagesByDialogs = new LongSparseArray();
        for (a = 0; a < this.pushMessages.size(); a++) {
            MessageObject messageObject = (MessageObject) this.pushMessages.get(a);
            long dialog_id = messageObject.getDialogId();
            ArrayList<MessageObject> arrayList = (ArrayList) messagesByDialogs.get(dialog_id);
            if (arrayList == null) {
                arrayList = new ArrayList();
                messagesByDialogs.put(dialog_id, arrayList);
                sortedDialogs.add(0, Long.valueOf(dialog_id));
            }
            arrayList.add(messageObject);
        }
        LongSparseArray<Integer> oldIdsWear = this.wearNotificationsIds.clone();
        this.wearNotificationsIds.clear();
        ArrayList<AnonymousClass1NotificationHolder> holders = new ArrayList();
        JSONArray serializedNotifications = null;
        if (WearDataLayerListenerService.isWatchConnected()) {
            serializedNotifications = new JSONArray();
        }
        int size = sortedDialogs.size();
        for (int b = 0; b < size; b++) {
            boolean canReply;
            String name;
            String dismissalID;
            dialog_id = ((Long) sortedDialogs.get(b)).longValue();
            ArrayList<MessageObject> messageObjects = (ArrayList) messagesByDialogs.get(dialog_id);
            int max_id = ((MessageObject) messageObjects.get(0)).getId();
            int lowerId = (int) dialog_id;
            int highId = (int) (dialog_id >> 32);
            Integer internalId = (Integer) oldIdsWear.get(dialog_id);
            if (internalId != null) {
                oldIdsWear.remove(dialog_id);
            } else if (lowerId != 0) {
                internalId = Integer.valueOf(lowerId);
            } else {
                internalId = Integer.valueOf(highId);
            }
            JSONObject jSONObject = null;
            if (serializedNotifications != null) {
                jSONObject = new JSONObject();
            }
            MessageObject lastMessageObject = (MessageObject) messageObjects.get(0);
            int max_date = lastMessageObject.messageOwner.date;
            Chat chat = null;
            User user = null;
            boolean isChannel = false;
            boolean isSupergroup = false;
            TLObject photoPath = null;
            if (lowerId != 0) {
                canReply = true;
                if (lowerId > 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(lowerId));
                    if (user != null) {
                        name = UserObject.getUserName(user);
                        if (!(user.photo == null || user.photo.photo_small == null || user.photo.photo_small.volume_id == 0 || user.photo.photo_small.local_id == 0)) {
                            photoPath = user.photo.photo_small;
                        }
                    } else if (lastMessageObject.isFcmMessage()) {
                        name = lastMessageObject.localName;
                    } else {
                    }
                } else {
                    chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-lowerId));
                    if (chat != null) {
                        isSupergroup = chat.megagroup;
                        isChannel = ChatObject.isChannel(chat) && !chat.megagroup;
                        name = chat.title;
                        if (!(chat.photo == null || chat.photo.photo_small == null || chat.photo.photo_small.volume_id == 0 || chat.photo.photo_small.local_id == 0)) {
                            photoPath = chat.photo.photo_small;
                        }
                    } else if (lastMessageObject.isFcmMessage()) {
                        isSupergroup = lastMessageObject.isMegagroup();
                        name = lastMessageObject.localName;
                        isChannel = lastMessageObject.localChannel;
                    } else {
                    }
                }
            } else {
                canReply = false;
                EncryptedChat encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(highId));
                if (encryptedChat != null) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(encryptedChat.user_id));
                    if (user != null) {
                        name = LocaleController.getString("SecretChatName", R.string.SecretChatName);
                        photoPath = null;
                        jSONObject = null;
                    }
                }
            }
            if (AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
                name = LocaleController.getString("AppName", R.string.AppName);
                photoPath = null;
                canReply = false;
            }
            UnreadConversation.Builder unreadConvBuilder = new UnreadConversation.Builder(name).setLatestTimestamp(((long) max_date) * 1000);
            Intent intent = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
            intent.addFlags(32);
            intent.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
            intent.putExtra("dialog_id", dialog_id);
            intent.putExtra("max_id", max_id);
            intent.putExtra("currentAccount", this.currentAccount);
            unreadConvBuilder.setReadPendingIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId.intValue(), intent, 134217728));
            Action wearReplyAction = null;
            if ((!isChannel || isSupergroup) && canReply && !SharedConfig.isWaitingForPasscodeEnter) {
                String replyToString;
                intent = new Intent(ApplicationLoader.applicationContext, AutoMessageReplyReceiver.class);
                intent.addFlags(32);
                intent.setAction("org.telegram.messenger.ACTION_MESSAGE_REPLY");
                intent.putExtra("dialog_id", dialog_id);
                intent.putExtra("max_id", max_id);
                intent.putExtra("currentAccount", this.currentAccount);
                unreadConvBuilder.setReplyAction(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId.intValue(), intent, 134217728), new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build());
                intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                intent.putExtra("dialog_id", dialog_id);
                intent.putExtra("max_id", max_id);
                intent.putExtra("currentAccount", this.currentAccount);
                PendingIntent replyPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, internalId.intValue(), intent, 134217728);
                RemoteInput remoteInputWear = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                if (lowerId < 0) {
                    replyToString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, name);
                } else {
                    replyToString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, name);
                }
                wearReplyAction = new Action.Builder(R.drawable.ic_reply_icon, replyToString, replyPendingIntent).setAllowGeneratedReplies(true).addRemoteInput(remoteInputWear).build();
            }
            Integer count = (Integer) this.pushDialogs.get(dialog_id);
            if (count == null) {
                count = Integer.valueOf(0);
            }
            Style messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID).setConversationTitle(String.format("%1$s (%2$s)", new Object[]{name, LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()))}));
            StringBuilder text = new StringBuilder();
            boolean[] isText = new boolean[1];
            ArrayList<TL_keyboardButtonRow> rows = null;
            int rowsMid = 0;
            JSONArray serializedMsgs = null;
            if (jSONObject != null) {
                serializedMsgs = new JSONArray();
            }
            for (a = messageObjects.size() - 1; a >= 0; a--) {
                String nameToReplace;
                messageObject = (MessageObject) messageObjects.get(a);
                String message = getStringForMessage(messageObject, false, isText);
                if (messageObject.isFcmMessage()) {
                    nameToReplace = messageObject.localName;
                } else {
                    nameToReplace = name;
                }
                if (message != null) {
                    if (lowerId < 0) {
                        message = message.replace(" @ " + nameToReplace, TtmlNode.ANONYMOUS_REGION_ID);
                    } else if (isText[0]) {
                        message = message.replace(nameToReplace + ": ", TtmlNode.ANONYMOUS_REGION_ID);
                    } else {
                        message = message.replace(nameToReplace + " ", TtmlNode.ANONYMOUS_REGION_ID);
                    }
                    if (text.length() > 0) {
                        text.append("\n\n");
                    }
                    text.append(message);
                    unreadConvBuilder.addMessage(message);
                    messagingStyle.addMessage(message, ((long) messageObject.messageOwner.date) * 1000, null);
                    if (serializedMsgs != null) {
                        try {
                            JSONObject jmsg = new JSONObject();
                            jmsg.put(MimeTypes.BASE_TYPE_TEXT, getShortStringForMessage(messageObject));
                            jmsg.put("date", messageObject.messageOwner.date);
                            if (messageObject.isFromUser() && lowerId < 0) {
                                User sender = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.getFromId()));
                                if (sender != null) {
                                    jmsg.put("fname", sender.first_name);
                                    jmsg.put("lname", sender.last_name);
                                }
                            }
                            serializedMsgs.put(jmsg);
                        } catch (JSONException e) {
                        }
                    }
                    if (dialog_id == 777000 && messageObject.messageOwner.reply_markup != null) {
                        rows = messageObject.messageOwner.reply_markup.rows;
                        rowsMid = messageObject.getId();
                    }
                }
            }
            intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            intent.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent.setFlags(32768);
            if (lowerId == 0) {
                intent.putExtra("encId", highId);
            } else if (lowerId > 0) {
                intent.putExtra("userId", lowerId);
            } else {
                intent.putExtra("chatId", -lowerId);
            }
            intent.putExtra("currentAccount", this.currentAccount);
            PendingIntent contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824);
            WearableExtender wearableExtender = new WearableExtender();
            if (wearReplyAction != null) {
                wearableExtender.addAction(wearReplyAction);
            }
            if (lowerId == 0) {
                dismissalID = "tgenc" + highId + "_" + max_id;
            } else if (lowerId > 0) {
                dismissalID = "tguser" + lowerId + "_" + max_id;
            } else {
                dismissalID = "tgchat" + (-lowerId) + "_" + max_id;
            }
            wearableExtender.setDismissalId(dismissalID);
            wearableExtender.setBridgeTag("tgaccount" + UserConfig.getInstance(this.currentAccount).getClientUserId());
            WearableExtender summaryExtender = new WearableExtender();
            summaryExtender.setDismissalId("summary_" + dismissalID);
            notificationBuilder.extend(summaryExtender);
            long date = ((long) ((MessageObject) messageObjects.get(0)).messageOwner.date) * 1000;
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(this.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setShowWhen(true).setShortcutId("sdid_" + dialog_id).setGroupAlertBehavior(1).setStyle(messagingStyle).setContentIntent(contentIntent).extend(wearableExtender).setSortKey(TtmlNode.ANONYMOUS_REGION_ID + (Long.MAX_VALUE - date)).extend(new CarExtender().setUnreadConversation(unreadConvBuilder.build())).setCategory("msg");
            if (this.pushDialogs.size() == 1 && !TextUtils.isEmpty(summary)) {
                builder.setSubText(summary);
            }
            if (lowerId == 0) {
                builder.setLocalOnly(true);
            }
            if (photoPath != null) {
                BitmapDrawable img = ImageLoader.getInstance().getImageFromMemory(photoPath, null, "50_50");
                if (img != null) {
                    builder.setLargeIcon(img.getBitmap());
                } else {
                    try {
                        File file = FileLoader.getPathToAttach(photoPath, true);
                        if (file.exists()) {
                            int i;
                            float scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                            Options options = new Options();
                            if (scaleFactor < 1.0f) {
                                i = 1;
                            } else {
                                i = (int) scaleFactor;
                            }
                            options.inSampleSize = i;
                            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                            if (bitmap != null) {
                                builder.setLargeIcon(bitmap);
                            }
                        }
                    } catch (Throwable th) {
                    }
                }
            }
            if (!(AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter || rows == null)) {
                int rc = rows.size();
                for (int r = 0; r < rc; r++) {
                    TL_keyboardButtonRow row = (TL_keyboardButtonRow) rows.get(r);
                    int cc = row.buttons.size();
                    for (int c = 0; c < cc; c++) {
                        KeyboardButton button = (KeyboardButton) row.buttons.get(c);
                        if (button instanceof TL_keyboardButtonCallback) {
                            Intent callbackIntent = new Intent(ApplicationLoader.applicationContext, NotificationCallbackReceiver.class);
                            callbackIntent.putExtra("currentAccount", this.currentAccount);
                            callbackIntent.putExtra("did", dialog_id);
                            if (button.data != null) {
                                callbackIntent.putExtra(DataSchemeDataSource.SCHEME_DATA, button.data);
                            }
                            callbackIntent.putExtra("mid", rowsMid);
                            String str = button.text;
                            Context context = ApplicationLoader.applicationContext;
                            int i2 = this.lastButtonId;
                            this.lastButtonId = i2 + 1;
                            builder.addAction(0, str, PendingIntent.getBroadcast(context, i2, callbackIntent, 134217728));
                        }
                    }
                }
            }
            if (chat == null && user != null && user.phone != null && user.phone.length() > 0) {
                builder.addPerson("tel:+" + user.phone);
            }
            if (VERSION.SDK_INT >= 26) {
                builder.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
            }
            holders.add(new AnonymousClass1NotificationHolder(internalId.intValue(), builder.build()));
            this.wearNotificationsIds.put(dialog_id, internalId);
            if (jSONObject != null) {
                try {
                    jSONObject.put("reply", canReply);
                    jSONObject.put("name", name);
                    jSONObject.put("max_id", max_id);
                    jSONObject.put("max_date", max_date);
                    jSONObject.put(TtmlNode.ATTR_ID, Math.abs(lowerId));
                    if (photoPath != null) {
                        jSONObject.put("photo", photoPath.dc_id + "_" + photoPath.volume_id + "_" + photoPath.secret);
                    }
                    if (serializedMsgs != null) {
                        jSONObject.put("msgs", serializedMsgs);
                    }
                    if (lowerId > 0) {
                        jSONObject.put("type", "user");
                    } else if (lowerId < 0) {
                        if (isChannel || isSupergroup) {
                            jSONObject.put("type", "channel");
                        } else {
                            jSONObject.put("type", "group");
                        }
                    }
                    serializedNotifications.put(jSONObject);
                } catch (JSONException e2) {
                }
            }
        }
        notificationManager.notify(this.notificationId, mainNotification);
        size = holders.size();
        for (a = 0; a < size; a++) {
            ((AnonymousClass1NotificationHolder) holders.get(a)).call();
        }
        for (a = 0; a < oldIdsWear.size(); a++) {
            notificationManager.cancel(((Integer) oldIdsWear.valueAt(a)).intValue());
        }
        if (serializedNotifications != null) {
            try {
                JSONObject s = new JSONObject();
                s.put(TtmlNode.ATTR_ID, UserConfig.getInstance(this.currentAccount).getClientUserId());
                s.put("n", serializedNotifications);
                WearDataLayerListenerService.sendMessageToWatch("/notify", s.toString().getBytes(C.UTF8_NAME), "remote_notifications");
            } catch (Exception e3) {
            }
        }
    }

    public void playOutChatSound() {
        if (this.inChatSoundEnabled && !MediaController.getInstance().isRecordingAudio()) {
            try {
                if (audioManager.getRingerMode() == 0) {
                    return;
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
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
    }

    public void updateServerNotificationsSettings(long dialog_id) {
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        if (((int) dialog_id) != 0) {
            SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
            TL_account_updateNotifySettings req = new TL_account_updateNotifySettings();
            req.settings = new TL_inputPeerNotifySettings();
            req.settings.sound = "default";
            int mute_type = preferences.getInt("notify2_" + dialog_id, 0);
            if (mute_type == 3) {
                req.settings.mute_until = preferences.getInt("notifyuntil_" + dialog_id, 0);
            } else {
                req.settings.mute_until = mute_type != 2 ? 0 : ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            req.settings.show_previews = preferences.getBoolean("preview_" + dialog_id, true);
            req.settings.silent = preferences.getBoolean("silent_" + dialog_id, false);
            req.peer = new TL_inputNotifyPeer();
            ((TL_inputNotifyPeer) req.peer).peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int) dialog_id);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            });
        }
    }
}
