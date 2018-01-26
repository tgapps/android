package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
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
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v4.app.NotificationCompat.BigTextStyle;
import android.support.v4.app.NotificationCompat.Builder;
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
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.PhoneCallDiscardReason;
import org.telegram.tgnet.TLRPC.TL_account_updateNotifySettings;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_inputNotifyPeer;
import org.telegram.tgnet.TLRPC.TL_inputPeerNotifySettings;
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
    private static NotificationManagerCompat notificationManager;
    private static DispatchQueue notificationsQueue = new DispatchQueue("notificationsQueue");
    private static NotificationManager systemNotificationManager;
    private AlarmManager alarmManager;
    private int currentAccount;
    private ArrayList<MessageObject> delayedPushMessages = new ArrayList();
    private boolean inChatSoundEnabled = true;
    private int lastBadgeCount = -1;
    private boolean lastNotificationIsNoData;
    private int lastOnlineFromOtherDevice = 0;
    private long lastSoundOutPlay;
    private long lastSoundPlay;
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

    protected void showSingleBackgroundNotification() {
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                try {
                    if (ApplicationLoader.mainInterfacePaused) {
                        SharedPreferences preferences = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount);
                        boolean notifyDisabled = false;
                        int needVibrate = 0;
                        String choosenSoundPath = null;
                        int ledColor = -16776961;
                        int priority = 0;
                        if (!preferences.getBoolean("EnableAll", true)) {
                            notifyDisabled = true;
                        }
                        String defaultPath = System.DEFAULT_NOTIFICATION_URI.getPath();
                        if (!notifyDisabled) {
                            choosenSoundPath = null;
                            boolean vibrateOnlyIfSilent = false;
                            if (choosenSoundPath != null && choosenSoundPath.equals(defaultPath)) {
                                choosenSoundPath = null;
                            } else if (choosenSoundPath == null) {
                                choosenSoundPath = preferences.getString("GlobalSoundPath", defaultPath);
                            }
                            needVibrate = preferences.getInt("vibrate_messages", 0);
                            priority = preferences.getInt("priority_group", 1);
                            ledColor = preferences.getInt("MessagesLed", -16776961);
                            if (needVibrate == 4) {
                                vibrateOnlyIfSilent = true;
                                needVibrate = 0;
                            }
                            if ((needVibrate == 2 && (0 == 1 || 0 == 3)) || ((needVibrate != 2 && 0 == 2) || !(null == null || 0 == 4))) {
                                needVibrate = 0;
                            }
                            if (vibrateOnlyIfSilent && needVibrate != 2) {
                                try {
                                    int mode = NotificationsController.audioManager.getRingerMode();
                                    if (!(mode == 0 || mode == 1)) {
                                        needVibrate = 2;
                                    }
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                            }
                        }
                        Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                        intent.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                        intent.setFlags(32768);
                        PendingIntent contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824);
                        String name = LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
                        Builder mBuilder = new Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setAutoCancel(true).setNumber(NotificationsController.this.total_unread_count).setContentIntent(contentIntent).setGroup(NotificationsController.this.notificationGroup).setGroupSummary(true).setColor(-13851168);
                        long[] vibrationPattern = null;
                        int importance = 0;
                        Uri sound = null;
                        mBuilder.setCategory("msg");
                        String lastMessage = LocaleController.getString("BackgroundRestricted", R.string.BackgroundRestricted);
                        mBuilder.setContentText(lastMessage);
                        mBuilder.setStyle(new BigTextStyle().bigText(lastMessage));
                        if (priority == 0) {
                            mBuilder.setPriority(0);
                            if (VERSION.SDK_INT >= 26) {
                                importance = 3;
                            }
                        } else if (priority == 1) {
                            mBuilder.setPriority(1);
                            if (VERSION.SDK_INT >= 26) {
                                importance = 4;
                            }
                        } else if (priority == 2) {
                            mBuilder.setPriority(2);
                            if (VERSION.SDK_INT >= 26) {
                                importance = 5;
                            }
                        }
                        if (notifyDisabled) {
                            vibrationPattern = new long[]{0, 0};
                            mBuilder.setVibrate(vibrationPattern);
                        } else {
                            if (lastMessage.length() > 100) {
                                lastMessage = lastMessage.substring(0, 100).replace('\n', ' ').trim() + "...";
                            }
                            mBuilder.setTicker(lastMessage);
                            if (!(choosenSoundPath == null || choosenSoundPath.equals("NoSound"))) {
                                if (VERSION.SDK_INT >= 26) {
                                    sound = choosenSoundPath.equals(defaultPath) ? System.DEFAULT_NOTIFICATION_URI : Uri.parse(choosenSoundPath);
                                } else if (choosenSoundPath.equals(defaultPath)) {
                                    mBuilder.setSound(System.DEFAULT_NOTIFICATION_URI, 5);
                                } else {
                                    mBuilder.setSound(Uri.parse(choosenSoundPath), 5);
                                }
                            }
                            if (ledColor != 0) {
                                mBuilder.setLights(ledColor, 1000, 1000);
                            }
                            if (needVibrate == 2 || MediaController.getInstance().isRecordingAudio()) {
                                vibrationPattern = new long[]{0, 0};
                                mBuilder.setVibrate(vibrationPattern);
                            } else if (needVibrate == 1) {
                                vibrationPattern = new long[]{0, 100, 0, 100};
                                mBuilder.setVibrate(vibrationPattern);
                            } else if (needVibrate == 0 || needVibrate == 4) {
                                mBuilder.setDefaults(2);
                                vibrationPattern = new long[0];
                            } else if (needVibrate == 3) {
                                vibrationPattern = new long[]{0, 1000};
                                mBuilder.setVibrate(vibrationPattern);
                            }
                        }
                        if (VERSION.SDK_INT >= 26) {
                            mBuilder.setChannelId(NotificationsController.this.validateChannelId(0, name, vibrationPattern, ledColor, sound, importance, notifyDisabled));
                        }
                        NotificationsController.this.lastNotificationIsNoData = true;
                        NotificationsController.notificationManager.notify(NotificationsController.this.notificationId, mBuilder.build());
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        });
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
        final ArrayList<MessageObject> popupArray = this.popupMessages.isEmpty() ? null : new ArrayList(this.popupMessages);
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
                            if (popupArray != null) {
                                popupArray.remove(messageObject);
                            }
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
                        if (!(popupArray == null || !NotificationsController.this.pushMessages.isEmpty() || popupArray.isEmpty())) {
                            popupArray.clear();
                        }
                    }
                }
                if (popupArray != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.this.popupMessages = popupArray;
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
        final ArrayList<MessageObject> popupArray = this.popupMessages.isEmpty() ? null : new ArrayList(this.popupMessages);
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
                            if (popupArray != null) {
                                popupArray.remove(messageObject);
                            }
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
                        if (!(popupArray == null || !NotificationsController.this.pushMessages.isEmpty() || popupArray.isEmpty())) {
                            popupArray.clear();
                        }
                    }
                }
                if (popupArray != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.this.popupMessages = popupArray;
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
        final ArrayList<MessageObject> popupArray = this.popupMessages.isEmpty() ? null : new ArrayList(this.popupMessages);
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
                int oldCount = popupArray != null ? popupArray.size() : 0;
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
                                if (popupArray != null) {
                                    popupArray.remove(messageObject);
                                }
                                mid = (long) messageObject.messageOwner.id;
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
                    if (!(popupArray == null || !NotificationsController.this.pushMessages.isEmpty() || popupArray.isEmpty())) {
                        popupArray.clear();
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
                                if (popupArray != null) {
                                    popupArray.remove(messageObject);
                                }
                                mid = (long) messageObject.messageOwner.id;
                                if (messageObject.messageOwner.to_id.channel_id != 0) {
                                    mid |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
                                }
                                NotificationsController.this.pushMessagesDict.remove(mid);
                                a--;
                            }
                        }
                        a++;
                    }
                    if (!(popupArray == null || !NotificationsController.this.pushMessages.isEmpty() || popupArray.isEmpty())) {
                        popupArray.clear();
                    }
                }
                if (popupArray != null && oldCount != popupArray.size()) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.this.popupMessages = popupArray;
                            NotificationCenter.getInstance(NotificationsController.this.currentAccount).postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
                        }
                    });
                }
            }
        });
    }

    public void processNewMessages(final ArrayList<MessageObject> messageObjects, final boolean isLast) {
        if (!messageObjects.isEmpty()) {
            final ArrayList<MessageObject> popupArray = new ArrayList(this.popupMessages);
            notificationsQueue.postRunnable(new Runnable() {
                public void run() {
                    boolean added = false;
                    int oldCount = popupArray.size();
                    LongSparseArray<Boolean> settingsCache = new LongSparseArray();
                    SharedPreferences preferences = MessagesController.getNotificationsSettings(NotificationsController.this.currentAccount);
                    boolean allowPinned = preferences.getBoolean("PinnedMessages", true);
                    int popup = 0;
                    for (int a = 0; a < messageObjects.size(); a++) {
                        MessageObject messageObject = (MessageObject) messageObjects.get(a);
                        long mid = (long) messageObject.messageOwner.id;
                        if (messageObject.messageOwner.to_id.channel_id != 0) {
                            mid |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
                        }
                        if (NotificationsController.this.pushMessagesDict.indexOfKey(mid) < 0) {
                            long dialog_id = messageObject.getDialogId();
                            long original_dialog_id = dialog_id;
                            if (dialog_id == NotificationsController.this.opened_dialog_id && ApplicationLoader.isScreenOn) {
                                NotificationsController.this.playInChatSound();
                            } else {
                                if (messageObject.messageOwner.mentioned) {
                                    if (allowPinned || !(messageObject.messageOwner.action instanceof TL_messageActionPinMessage)) {
                                        dialog_id = (long) messageObject.messageOwner.from_id;
                                    }
                                }
                                if (NotificationsController.this.isPersonalMessage(messageObject)) {
                                    NotificationsController.this.personal_count = NotificationsController.this.personal_count + 1;
                                }
                                added = true;
                                Boolean value = (Boolean) settingsCache.get(dialog_id);
                                int lower_id = (int) dialog_id;
                                boolean isChat = lower_id < 0;
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
                                if (value == null) {
                                    int notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                                    boolean z = notifyOverride != 2 && ((preferences.getBoolean("EnableAll", true) && (!isChat || preferences.getBoolean("EnableGroup", true))) || notifyOverride != 0);
                                    value = Boolean.valueOf(z);
                                    settingsCache.put(dialog_id, value);
                                }
                                if (!(popup == 0 || messageObject.messageOwner.to_id.channel_id == 0 || messageObject.isMegagroup())) {
                                    popup = 0;
                                }
                                if (value.booleanValue()) {
                                    if (popup != 0) {
                                        popupArray.add(0, messageObject);
                                    }
                                    NotificationsController.this.delayedPushMessages.add(messageObject);
                                    NotificationsController.this.pushMessages.add(0, messageObject);
                                    NotificationsController.this.pushMessagesDict.put(mid, messageObject);
                                    if (original_dialog_id != dialog_id) {
                                        NotificationsController.this.pushDialogsOverrideMention.put(original_dialog_id, Integer.valueOf(1));
                                    }
                                }
                            }
                        }
                    }
                    if (added) {
                        NotificationsController.this.notifyCheck = isLast;
                    }
                    if (!popupArray.isEmpty() && oldCount != popupArray.size() && !AndroidUtilities.needShowPasscode(false)) {
                        final int i = popup;
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationsController.this.popupMessages = popupArray;
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
                }
            });
        }
    }

    public int getTotalUnreadCount() {
        return this.total_unread_count;
    }

    public void processDialogsUpdateRead(final LongSparseArray<Integer> dialogsToUpdate) {
        final ArrayList<MessageObject> popupArray = this.popupMessages.isEmpty() ? null : new ArrayList(this.popupMessages);
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
                                long mid = (long) messageObject.messageOwner.id;
                                if (messageObject.messageOwner.to_id.channel_id != 0) {
                                    mid |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
                                }
                                NotificationsController.this.pushMessagesDict.remove(mid);
                                if (popupArray != null) {
                                    popupArray.remove(messageObject);
                                }
                            }
                            a++;
                        }
                        if (!(popupArray == null || !NotificationsController.this.pushMessages.isEmpty() || popupArray.isEmpty())) {
                            popupArray.clear();
                        }
                    } else if (canAddValue) {
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + newCount.intValue();
                        NotificationsController.this.pushDialogs.put(dialog_id, newCount);
                    }
                }
                if (popupArray != null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.this.popupMessages = popupArray;
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
                Boolean value;
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
                            value = (Boolean) settingsCache.get(dialog_id);
                            if (value == null) {
                                int notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                                boolean z = notifyOverride != 2 && ((preferences.getBoolean("EnableAll", true) && (((int) dialog_id) >= 0 || preferences.getBoolean("EnableGroup", true))) || notifyOverride != 0);
                                value = Boolean.valueOf(z);
                                settingsCache.put(dialog_id, value);
                            }
                            if (value.booleanValue() && !(dialog_id == NotificationsController.this.opened_dialog_id && ApplicationLoader.isScreenOn)) {
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
                    value = (Boolean) settingsCache.get(dialog_id);
                    if (value == null) {
                        notifyOverride = NotificationsController.this.getNotifyOverride(preferences, dialog_id);
                        Integer override = (Integer) NotificationsController.this.pushDialogsOverrideMention.get(dialog_id);
                        if (override != null && override.intValue() == 1) {
                            NotificationsController.this.pushDialogsOverrideMention.put(dialog_id, Integer.valueOf(0));
                            notifyOverride = 1;
                        }
                        z = notifyOverride != 2 && ((preferences.getBoolean("EnableAll", true) && (((int) dialog_id) >= 0 || preferences.getBoolean("EnableGroup", true))) || notifyOverride != 0);
                        value = Boolean.valueOf(z);
                        settingsCache.put(dialog_id, value);
                    }
                    if (value.booleanValue()) {
                        int count = ((Integer) dialogs.valueAt(a)).intValue();
                        NotificationsController.this.pushDialogs.put(dialog_id, Integer.valueOf(count));
                        NotificationsController.this.total_unread_count = NotificationsController.this.total_unread_count + count;
                    }
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        if (NotificationsController.this.total_unread_count == 0) {
                            NotificationsController.this.popupMessages.clear();
                            NotificationCenter.getInstance(NotificationsController.this.currentAccount).postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
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
        setBadge(getTotalAllUnreadCount());
    }

    private void setBadge(final int count) {
        notificationsQueue.postRunnable(new Runnable() {
            public void run() {
                if (NotificationsController.this.lastBadgeCount != count) {
                    NotificationsController.this.lastBadgeCount = count;
                    NotificationBadge.applyCount(count);
                }
            }
        });
    }

    private String getStringForMessage(MessageObject messageObject, boolean shortMessage, boolean[] text) {
        User user;
        Chat chat;
        long dialog_id = messageObject.messageOwner.dialog_id;
        int chat_id = messageObject.messageOwner.to_id.chat_id != 0 ? messageObject.messageOwner.to_id.chat_id : messageObject.messageOwner.to_id.channel_id;
        int from_id = messageObject.messageOwner.to_id.user_id;
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
        if (((int) dialog_id) == 0 || AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
            return LocaleController.getString("YouHaveNewMessage", R.string.YouHaveNewMessage);
        }
        String msg;
        if (chat_id == 0 && from_id != 0) {
            if (!MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewAll", true)) {
                return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, name);
            } else if (messageObject.messageOwner instanceof TL_messageService) {
                if (messageObject.messageOwner.action instanceof TL_messageActionUserJoined) {
                    return LocaleController.formatString("NotificationContactJoined", R.string.NotificationContactJoined, name);
                } else if (messageObject.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto) {
                    return LocaleController.formatString("NotificationContactNewPhoto", R.string.NotificationContactNewPhoto, name);
                } else if (messageObject.messageOwner.action instanceof TL_messageActionLoginUnknownLocation) {
                    String date = LocaleController.formatString("formatDateAtTime", R.string.formatDateAtTime, LocaleController.getInstance().formatterYear.format(((long) messageObject.messageOwner.date) * 1000), LocaleController.getInstance().formatterDay.format(((long) messageObject.messageOwner.date) * 1000));
                    return LocaleController.formatString("NotificationUnrecognizedDevice", R.string.NotificationUnrecognizedDevice, UserConfig.getInstance(this.currentAccount).getCurrentUser().first_name, date, messageObject.messageOwner.action.title, messageObject.messageOwner.action.address);
                } else if ((messageObject.messageOwner.action instanceof TL_messageActionGameScore) || (messageObject.messageOwner.action instanceof TL_messageActionPaymentSent)) {
                    return messageObject.messageText.toString();
                } else {
                    if (!(messageObject.messageOwner.action instanceof TL_messageActionPhoneCall)) {
                        return null;
                    }
                    PhoneCallDiscardReason reason = messageObject.messageOwner.action.reason;
                    if (messageObject.isOut() || !(reason instanceof TL_phoneCallDiscardReasonMissed)) {
                        return null;
                    }
                    return LocaleController.getString("CallMessageIncomingMissed", R.string.CallMessageIncomingMissed);
                }
            } else if (messageObject.isMediaEmpty()) {
                if (shortMessage) {
                    return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, name);
                } else if (messageObject.messageOwner.message == null || messageObject.messageOwner.message.length() == 0) {
                    return LocaleController.formatString("NotificationMessageNoText", R.string.NotificationMessageNoText, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, messageObject.messageOwner.message);
                    text[0] = true;
                    return msg;
                }
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                if (!shortMessage && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "🖼 " + messageObject.messageOwner.message);
                    text[0] = true;
                    return msg;
                } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                    return LocaleController.formatString("NotificationMessageSDPhoto", R.string.NotificationMessageSDPhoto, name);
                } else {
                    return LocaleController.formatString("NotificationMessagePhoto", R.string.NotificationMessagePhoto, name);
                }
            } else if (messageObject.isVideo()) {
                if (!shortMessage && VERSION.SDK_INT >= 19 && !TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "📹 " + messageObject.messageOwner.message);
                    text[0] = true;
                    return msg;
                } else if (messageObject.messageOwner.media.ttl_seconds != 0) {
                    return LocaleController.formatString("NotificationMessageSDVideo", R.string.NotificationMessageSDVideo, name);
                } else {
                    return LocaleController.formatString("NotificationMessageVideo", R.string.NotificationMessageVideo, name);
                }
            } else if (messageObject.isGame()) {
                return LocaleController.formatString("NotificationMessageGame", R.string.NotificationMessageGame, name, messageObject.messageOwner.media.game.title);
            } else if (messageObject.isVoice()) {
                return LocaleController.formatString("NotificationMessageAudio", R.string.NotificationMessageAudio, name);
            } else if (messageObject.isRoundVideo()) {
                return LocaleController.formatString("NotificationMessageRound", R.string.NotificationMessageRound, name);
            } else if (messageObject.isMusic()) {
                return LocaleController.formatString("NotificationMessageMusic", R.string.NotificationMessageMusic, name);
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaContact) {
                return LocaleController.formatString("NotificationMessageContact", R.string.NotificationMessageContact, name);
            } else if ((messageObject.messageOwner.media instanceof TL_messageMediaGeo) || (messageObject.messageOwner.media instanceof TL_messageMediaVenue)) {
                return LocaleController.formatString("NotificationMessageMap", R.string.NotificationMessageMap, name);
            } else if (messageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                return LocaleController.formatString("NotificationMessageLiveLocation", R.string.NotificationMessageLiveLocation, name);
            } else if (!(messageObject.messageOwner.media instanceof TL_messageMediaDocument)) {
                return null;
            } else {
                if (messageObject.isSticker()) {
                    if (messageObject.getStickerEmoji() != null) {
                        return LocaleController.formatString("NotificationMessageStickerEmoji", R.string.NotificationMessageStickerEmoji, name, messageObject.getStickerEmoji());
                    }
                    return LocaleController.formatString("NotificationMessageSticker", R.string.NotificationMessageSticker, name);
                } else if (messageObject.isGif()) {
                    if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return LocaleController.formatString("NotificationMessageGif", R.string.NotificationMessageGif, name);
                    }
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "🎬 " + messageObject.messageOwner.message);
                    text[0] = true;
                    return msg;
                } else if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                    return LocaleController.formatString("NotificationMessageDocument", R.string.NotificationMessageDocument, name);
                } else {
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "📎 " + messageObject.messageOwner.message);
                    text[0] = true;
                    return msg;
                }
            }
        } else if (chat_id == 0) {
            return null;
        } else {
            if (MessagesController.getNotificationsSettings(this.currentAccount).getBoolean("EnablePreviewGroup", true)) {
                if (messageObject.messageOwner instanceof TL_messageService) {
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
                            return LocaleController.formatString("NotificationGroupAddMember", R.string.NotificationGroupAddMember, name, chat.title, stringBuilder.toString());
                        } else if (messageObject.messageOwner.to_id.channel_id != 0 && !chat.megagroup) {
                            return LocaleController.formatString("ChannelAddedByNotification", R.string.ChannelAddedByNotification, name, chat.title);
                        } else if (singleUserId == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            return LocaleController.formatString("NotificationInvitedToGroup", R.string.NotificationInvitedToGroup, name, chat.title);
                        } else {
                            User u2 = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(singleUserId));
                            if (u2 == null) {
                                return null;
                            }
                            if (from_id != u2.id) {
                                return LocaleController.formatString("NotificationGroupAddMember", R.string.NotificationGroupAddMember, name, chat.title, UserObject.getUserName(u2));
                            } else if (chat.megagroup) {
                                return LocaleController.formatString("NotificationGroupAddSelfMega", R.string.NotificationGroupAddSelfMega, name, chat.title);
                            } else {
                                return LocaleController.formatString("NotificationGroupAddSelf", R.string.NotificationGroupAddSelf, name, chat.title);
                            }
                        }
                    } else if (messageObject.messageOwner.action instanceof TL_messageActionChatJoinedByLink) {
                        return LocaleController.formatString("NotificationInvitedToGroupByLink", R.string.NotificationInvitedToGroupByLink, name, chat.title);
                    } else if (messageObject.messageOwner.action instanceof TL_messageActionChatEditTitle) {
                        return LocaleController.formatString("NotificationEditedGroupName", R.string.NotificationEditedGroupName, name, messageObject.messageOwner.action.title);
                    } else if ((messageObject.messageOwner.action instanceof TL_messageActionChatEditPhoto) || (messageObject.messageOwner.action instanceof TL_messageActionChatDeletePhoto)) {
                        if (messageObject.messageOwner.to_id.channel_id == 0 || chat.megagroup) {
                            return LocaleController.formatString("NotificationEditedGroupPhoto", R.string.NotificationEditedGroupPhoto, name, chat.title);
                        }
                        return LocaleController.formatString("ChannelPhotoEditNotification", R.string.ChannelPhotoEditNotification, chat.title);
                    } else if (messageObject.messageOwner.action instanceof TL_messageActionChatDeleteUser) {
                        if (messageObject.messageOwner.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            return LocaleController.formatString("NotificationGroupKickYou", R.string.NotificationGroupKickYou, name, chat.title);
                        } else if (messageObject.messageOwner.action.user_id == from_id) {
                            return LocaleController.formatString("NotificationGroupLeftMember", R.string.NotificationGroupLeftMember, name, chat.title);
                        } else {
                            if (MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.action.user_id)) == null) {
                                return null;
                            }
                            return LocaleController.formatString("NotificationGroupKickMember", R.string.NotificationGroupKickMember, name, chat.title, UserObject.getUserName(MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.action.user_id))));
                        }
                    } else if (messageObject.messageOwner.action instanceof TL_messageActionChatCreate) {
                        return messageObject.messageText.toString();
                    } else {
                        if (messageObject.messageOwner.action instanceof TL_messageActionChannelCreate) {
                            return messageObject.messageText.toString();
                        }
                        if (messageObject.messageOwner.action instanceof TL_messageActionChatMigrateTo) {
                            return LocaleController.formatString("ActionMigrateFromGroupNotify", R.string.ActionMigrateFromGroupNotify, chat.title);
                        } else if (messageObject.messageOwner.action instanceof TL_messageActionChannelMigrateFrom) {
                            return LocaleController.formatString("ActionMigrateFromGroupNotify", R.string.ActionMigrateFromGroupNotify, messageObject.messageOwner.action.title);
                        } else if (messageObject.messageOwner.action instanceof TL_messageActionScreenshotTaken) {
                            return messageObject.messageText.toString();
                        } else {
                            if (messageObject.messageOwner.action instanceof TL_messageActionPinMessage) {
                                MessageObject object;
                                String message;
                                CharSequence message2;
                                if (chat == null || !chat.megagroup) {
                                    if (messageObject.replyMessageObject == null) {
                                        return LocaleController.formatString("NotificationActionPinnedNoTextChannel", R.string.NotificationActionPinnedNoTextChannel, chat.title);
                                    }
                                    object = messageObject.replyMessageObject;
                                    if (object.isMusic()) {
                                        return LocaleController.formatString("NotificationActionPinnedMusicChannel", R.string.NotificationActionPinnedMusicChannel, chat.title);
                                    } else if (object.isVideo()) {
                                        if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedVideoChannel", R.string.NotificationActionPinnedVideoChannel, chat.title);
                                        }
                                        message = "📹 " + object.messageOwner.message;
                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message);
                                    } else if (object.isGif()) {
                                        if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedGifChannel", R.string.NotificationActionPinnedGifChannel, chat.title);
                                        }
                                        message = "🎬 " + object.messageOwner.message;
                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message);
                                    } else if (object.isVoice()) {
                                        return LocaleController.formatString("NotificationActionPinnedVoiceChannel", R.string.NotificationActionPinnedVoiceChannel, chat.title);
                                    } else if (object.isRoundVideo()) {
                                        return LocaleController.formatString("NotificationActionPinnedRoundChannel", R.string.NotificationActionPinnedRoundChannel, chat.title);
                                    } else if (object.isSticker()) {
                                        if (object.getStickerEmoji() != null) {
                                            return LocaleController.formatString("NotificationActionPinnedStickerEmojiChannel", R.string.NotificationActionPinnedStickerEmojiChannel, chat.title, object.getStickerEmoji());
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedStickerChannel", R.string.NotificationActionPinnedStickerChannel, chat.title);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaDocument) {
                                        if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedFileChannel", R.string.NotificationActionPinnedFileChannel, chat.title);
                                        }
                                        message = "📎 " + object.messageOwner.message;
                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message);
                                    } else if ((object.messageOwner.media instanceof TL_messageMediaGeo) || (object.messageOwner.media instanceof TL_messageMediaVenue)) {
                                        return LocaleController.formatString("NotificationActionPinnedGeoChannel", R.string.NotificationActionPinnedGeoChannel, chat.title);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaGeoLive) {
                                        return LocaleController.formatString("NotificationActionPinnedGeoLiveChannel", R.string.NotificationActionPinnedGeoLiveChannel, chat.title);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaContact) {
                                        return LocaleController.formatString("NotificationActionPinnedContactChannel", R.string.NotificationActionPinnedContactChannel, chat.title);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaPhoto) {
                                        if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedPhotoChannel", R.string.NotificationActionPinnedPhotoChannel, chat.title);
                                        }
                                        message = "🖼 " + object.messageOwner.message;
                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaGame) {
                                        return LocaleController.formatString("NotificationActionPinnedGameChannel", R.string.NotificationActionPinnedGameChannel, chat.title);
                                    } else if (object.messageText == null || object.messageText.length() <= 0) {
                                        return LocaleController.formatString("NotificationActionPinnedNoTextChannel", R.string.NotificationActionPinnedNoTextChannel, chat.title);
                                    } else {
                                        message2 = object.messageText;
                                        if (message2.length() > 20) {
                                            message2 = message2.subSequence(0, 20) + "...";
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedTextChannel", R.string.NotificationActionPinnedTextChannel, chat.title, message2);
                                    }
                                } else if (messageObject.replyMessageObject == null) {
                                    return LocaleController.formatString("NotificationActionPinnedNoText", R.string.NotificationActionPinnedNoText, name, chat.title);
                                } else {
                                    object = messageObject.replyMessageObject;
                                    if (object.isMusic()) {
                                        return LocaleController.formatString("NotificationActionPinnedMusic", R.string.NotificationActionPinnedMusic, name, chat.title);
                                    } else if (object.isVideo()) {
                                        if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedVideo", R.string.NotificationActionPinnedVideo, name, chat.title);
                                        }
                                        message = "📹 " + object.messageOwner.message;
                                        return LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message, chat.title);
                                    } else if (object.isGif()) {
                                        if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedGif", R.string.NotificationActionPinnedGif, name, chat.title);
                                        }
                                        message = "🎬 " + object.messageOwner.message;
                                        return LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message, chat.title);
                                    } else if (object.isVoice()) {
                                        return LocaleController.formatString("NotificationActionPinnedVoice", R.string.NotificationActionPinnedVoice, name, chat.title);
                                    } else if (object.isRoundVideo()) {
                                        return LocaleController.formatString("NotificationActionPinnedRound", R.string.NotificationActionPinnedRound, name, chat.title);
                                    } else if (object.isSticker()) {
                                        if (object.getStickerEmoji() != null) {
                                            return LocaleController.formatString("NotificationActionPinnedStickerEmoji", R.string.NotificationActionPinnedStickerEmoji, name, chat.title, object.getStickerEmoji());
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedSticker", R.string.NotificationActionPinnedSticker, name, chat.title);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaDocument) {
                                        if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedFile", R.string.NotificationActionPinnedFile, name, chat.title);
                                        }
                                        message = "📎 " + object.messageOwner.message;
                                        return LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message, chat.title);
                                    } else if ((object.messageOwner.media instanceof TL_messageMediaGeo) || (object.messageOwner.media instanceof TL_messageMediaVenue)) {
                                        return LocaleController.formatString("NotificationActionPinnedGeo", R.string.NotificationActionPinnedGeo, name, chat.title);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaGeoLive) {
                                        return LocaleController.formatString("NotificationActionPinnedGeoLive", R.string.NotificationActionPinnedGeoLive, name, chat.title);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaContact) {
                                        return LocaleController.formatString("NotificationActionPinnedContact", R.string.NotificationActionPinnedContact, name, chat.title);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaPhoto) {
                                        if (VERSION.SDK_INT < 19 || TextUtils.isEmpty(object.messageOwner.message)) {
                                            return LocaleController.formatString("NotificationActionPinnedPhoto", R.string.NotificationActionPinnedPhoto, name, chat.title);
                                        }
                                        message = "🖼 " + object.messageOwner.message;
                                        return LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message, chat.title);
                                    } else if (object.messageOwner.media instanceof TL_messageMediaGame) {
                                        return LocaleController.formatString("NotificationActionPinnedGame", R.string.NotificationActionPinnedGame, name, chat.title);
                                    } else if (object.messageText == null || object.messageText.length() <= 0) {
                                        return LocaleController.formatString("NotificationActionPinnedNoText", R.string.NotificationActionPinnedNoText, name, chat.title);
                                    } else {
                                        message2 = object.messageText;
                                        if (message2.length() > 20) {
                                            message2 = message2.subSequence(0, 20) + "...";
                                        }
                                        return LocaleController.formatString("NotificationActionPinnedText", R.string.NotificationActionPinnedText, name, message2, chat.title);
                                    }
                                }
                            } else if (messageObject.messageOwner.action instanceof TL_messageActionGameScore) {
                                return messageObject.messageText.toString();
                            } else {
                                return null;
                            }
                        }
                    }
                } else if (!ChatObject.isChannel(chat) || chat.megagroup) {
                    if (messageObject.isMediaEmpty()) {
                        if (shortMessage || messageObject.messageOwner.message == null || messageObject.messageOwner.message.length() == 0) {
                            return LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, name, chat.title);
                        }
                        return LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, messageObject.messageOwner.message);
                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                        if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return LocaleController.formatString("NotificationMessageGroupPhoto", R.string.NotificationMessageGroupPhoto, name, chat.title);
                        }
                        return LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, "🖼 " + messageObject.messageOwner.message);
                    } else if (messageObject.isVideo()) {
                        if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return LocaleController.formatString("NotificationMessageGroupVideo", R.string.NotificationMessageGroupVideo, name, chat.title);
                        }
                        return LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, "📹 " + messageObject.messageOwner.message);
                    } else if (messageObject.isVoice()) {
                        return LocaleController.formatString("NotificationMessageGroupAudio", R.string.NotificationMessageGroupAudio, name, chat.title);
                    } else if (messageObject.isRoundVideo()) {
                        return LocaleController.formatString("NotificationMessageGroupRound", R.string.NotificationMessageGroupRound, name, chat.title);
                    } else if (messageObject.isMusic()) {
                        return LocaleController.formatString("NotificationMessageGroupMusic", R.string.NotificationMessageGroupMusic, name, chat.title);
                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaContact) {
                        return LocaleController.formatString("NotificationMessageGroupContact", R.string.NotificationMessageGroupContact, name, chat.title);
                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaGame) {
                        return LocaleController.formatString("NotificationMessageGroupGame", R.string.NotificationMessageGroupGame, name, chat.title, messageObject.messageOwner.media.game.title);
                    } else if ((messageObject.messageOwner.media instanceof TL_messageMediaGeo) || (messageObject.messageOwner.media instanceof TL_messageMediaVenue)) {
                        return LocaleController.formatString("NotificationMessageGroupMap", R.string.NotificationMessageGroupMap, name, chat.title);
                    } else if (messageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                        return LocaleController.formatString("NotificationMessageGroupLiveLocation", R.string.NotificationMessageGroupLiveLocation, name, chat.title);
                    } else if (!(messageObject.messageOwner.media instanceof TL_messageMediaDocument)) {
                        return null;
                    } else {
                        if (messageObject.isSticker()) {
                            if (messageObject.getStickerEmoji() != null) {
                                return LocaleController.formatString("NotificationMessageGroupStickerEmoji", R.string.NotificationMessageGroupStickerEmoji, name, chat.title, messageObject.getStickerEmoji());
                            }
                            return LocaleController.formatString("NotificationMessageGroupSticker", R.string.NotificationMessageGroupSticker, name, chat.title);
                        } else if (messageObject.isGif()) {
                            if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                                return LocaleController.formatString("NotificationMessageGroupGif", R.string.NotificationMessageGroupGif, name, chat.title);
                            }
                            return LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, "🎬 " + messageObject.messageOwner.message);
                        } else if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return LocaleController.formatString("NotificationMessageGroupDocument", R.string.NotificationMessageGroupDocument, name, chat.title);
                        } else {
                            return LocaleController.formatString("NotificationMessageGroupText", R.string.NotificationMessageGroupText, name, chat.title, "📎 " + messageObject.messageOwner.message);
                        }
                    }
                } else if (messageObject.isMediaEmpty()) {
                    if (shortMessage || messageObject.messageOwner.message == null || messageObject.messageOwner.message.length() == 0) {
                        return LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, name);
                    }
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, messageObject.messageOwner.message);
                    text[0] = true;
                    return msg;
                } else if (messageObject.messageOwner.media instanceof TL_messageMediaPhoto) {
                    if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return LocaleController.formatString("ChannelMessagePhoto", R.string.ChannelMessagePhoto, name);
                    }
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "🖼 " + messageObject.messageOwner.message);
                    text[0] = true;
                    return msg;
                } else if (messageObject.isVideo()) {
                    if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return LocaleController.formatString("ChannelMessageVideo", R.string.ChannelMessageVideo, name);
                    }
                    msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "📹 " + messageObject.messageOwner.message);
                    text[0] = true;
                    return msg;
                } else if (messageObject.isVoice()) {
                    return LocaleController.formatString("ChannelMessageAudio", R.string.ChannelMessageAudio, name);
                } else if (messageObject.isRoundVideo()) {
                    return LocaleController.formatString("ChannelMessageRound", R.string.ChannelMessageRound, name);
                } else if (messageObject.isMusic()) {
                    return LocaleController.formatString("ChannelMessageMusic", R.string.ChannelMessageMusic, name);
                } else if (messageObject.messageOwner.media instanceof TL_messageMediaContact) {
                    return LocaleController.formatString("ChannelMessageContact", R.string.ChannelMessageContact, name);
                } else if ((messageObject.messageOwner.media instanceof TL_messageMediaGeo) || (messageObject.messageOwner.media instanceof TL_messageMediaVenue)) {
                    return LocaleController.formatString("ChannelMessageMap", R.string.ChannelMessageMap, name);
                } else if (messageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                    return LocaleController.formatString("ChannelMessageLiveLocation", R.string.ChannelMessageLiveLocation, name);
                } else if (!(messageObject.messageOwner.media instanceof TL_messageMediaDocument)) {
                    return null;
                } else {
                    if (messageObject.isSticker()) {
                        if (messageObject.getStickerEmoji() != null) {
                            return LocaleController.formatString("ChannelMessageStickerEmoji", R.string.ChannelMessageStickerEmoji, name, messageObject.getStickerEmoji());
                        }
                        return LocaleController.formatString("ChannelMessageSticker", R.string.ChannelMessageSticker, name);
                    } else if (messageObject.isGif()) {
                        if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                            return LocaleController.formatString("ChannelMessageGIF", R.string.ChannelMessageGIF, name);
                        }
                        msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "🎬 " + messageObject.messageOwner.message);
                        text[0] = true;
                        return msg;
                    } else if (shortMessage || VERSION.SDK_INT < 19 || TextUtils.isEmpty(messageObject.messageOwner.message)) {
                        return LocaleController.formatString("ChannelMessageDocument", R.string.ChannelMessageDocument, name);
                    } else {
                        msg = LocaleController.formatString("NotificationMessageText", R.string.NotificationMessageText, name, "📎 " + messageObject.messageOwner.message);
                        text[0] = true;
                        return msg;
                    }
                }
            } else if (!ChatObject.isChannel(chat) || chat.megagroup) {
                return LocaleController.formatString("NotificationMessageGroupNoText", R.string.NotificationMessageGroupNoText, name, chat.title);
            } else {
                return LocaleController.formatString("ChannelMessageNoText", R.string.ChannelMessageNoText, name, chat.title);
            }
        }
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
            for (int a = 0; a < this.wearNotificationsIds.size(); a++) {
                notificationManager.cancel(((Integer) this.wearNotificationsIds.valueAt(a)).intValue());
            }
            this.wearNotificationsIds.clear();
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    NotificationCenter.getInstance(NotificationsController.this.currentAccount).postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
                }
            });
        } catch (Throwable e) {
            FileLog.e(e);
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

    @TargetApi(26)
    private String validateChannelId(long dialogId, String name, long[] vibrationPattern, int ledColor, Uri sound, int importance, boolean silent) {
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
            systemNotificationManager.deleteNotificationChannel(channelId);
            channelId = null;
        }
        if (channelId == null) {
            channelId = this.currentAccount + "channel" + dialogId + "_" + Utilities.random.nextLong();
            NotificationChannel notificationChannel = new NotificationChannel(channelId, name, importance);
            if (ledColor != 0) {
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(ledColor);
            }
            if (vibrationPattern != null) {
                notificationChannel.enableVibration(true);
                if (vibrationPattern.length > 0) {
                    notificationChannel.setVibrationPattern(vibrationPattern);
                }
            }
            if (sound != null) {
                AudioAttributes.Builder builder = new AudioAttributes.Builder();
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
    private void showOrUpdateNotification(boolean r73) {
        /*
        r72 = this;
        r0 = r72;
        r5 = r0.currentAccount;
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);
        r5 = r5.isClientActivated();
        if (r5 == 0) goto L_0x0018;
    L_0x000e:
        r0 = r72;
        r5 = r0.pushMessages;
        r5 = r5.isEmpty();
        if (r5 == 0) goto L_0x001c;
    L_0x0018:
        r72.dismissNotification();
    L_0x001b:
        return;
    L_0x001c:
        r0 = r72;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.tgnet.ConnectionsManager.getInstance(r5);	 Catch:{ Exception -> 0x0052 }
        r5.resumeNetworkMaybe();	 Catch:{ Exception -> 0x0052 }
        r0 = r72;
        r5 = r0.pushMessages;	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r37 = r5.get(r13);	 Catch:{ Exception -> 0x0052 }
        r37 = (org.telegram.messenger.MessageObject) r37;	 Catch:{ Exception -> 0x0052 }
        r0 = r72;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r54 = org.telegram.messenger.MessagesController.getNotificationsSettings(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = "dismissDate";
        r13 = 0;
        r0 = r54;
        r23 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r0 = r37;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.date;	 Catch:{ Exception -> 0x0052 }
        r0 = r23;
        if (r5 > r0) goto L_0x0057;
    L_0x004e:
        r72.dismissNotification();	 Catch:{ Exception -> 0x0052 }
        goto L_0x001b;
    L_0x0052:
        r25 = move-exception;
        org.telegram.messenger.FileLog.e(r25);
        goto L_0x001b;
    L_0x0057:
        r6 = r37.getDialogId();	 Catch:{ Exception -> 0x0052 }
        r52 = r6;
        r0 = r37;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.mentioned;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x006e;
    L_0x0065:
        r0 = r37;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.from_id;	 Catch:{ Exception -> 0x0052 }
        r0 = (long) r5;	 Catch:{ Exception -> 0x0052 }
        r52 = r0;
    L_0x006e:
        r42 = r37.getId();	 Catch:{ Exception -> 0x0052 }
        r0 = r37;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.to_id;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.chat_id;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0660;
    L_0x007c:
        r0 = r37;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.to_id;	 Catch:{ Exception -> 0x0052 }
        r15 = r5.chat_id;	 Catch:{ Exception -> 0x0052 }
    L_0x0084:
        r0 = r37;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.to_id;	 Catch:{ Exception -> 0x0052 }
        r0 = r5.user_id;	 Catch:{ Exception -> 0x0052 }
        r63 = r0;
        if (r63 != 0) goto L_0x066a;
    L_0x0090:
        r0 = r37;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r0 = r5.from_id;	 Catch:{ Exception -> 0x0052 }
        r63 = r0;
    L_0x0098:
        r0 = r72;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);	 Catch:{ Exception -> 0x0052 }
        r13 = java.lang.Integer.valueOf(r63);	 Catch:{ Exception -> 0x0052 }
        r62 = r5.getUser(r13);	 Catch:{ Exception -> 0x0052 }
        r14 = 0;
        if (r15 == 0) goto L_0x00bb;
    L_0x00ab:
        r0 = r72;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.messenger.MessagesController.getInstance(r5);	 Catch:{ Exception -> 0x0052 }
        r13 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x0052 }
        r14 = r5.getChat(r13);	 Catch:{ Exception -> 0x0052 }
    L_0x00bb:
        r51 = 0;
        r47 = 0;
        r45 = 0;
        r16 = 0;
        r10 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
        r30 = 0;
        r55 = 0;
        r0 = r72;
        r1 = r54;
        r2 = r52;
        r49 = r0.getNotifyOverride(r1, r2);	 Catch:{ Exception -> 0x0052 }
        if (r73 == 0) goto L_0x00f7;
    L_0x00d6:
        r5 = 2;
        r0 = r49;
        if (r0 == r5) goto L_0x00f7;
    L_0x00db:
        r5 = "EnableAll";
        r13 = 1;
        r0 = r54;
        r5 = r0.getBoolean(r5, r13);	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x00f5;
    L_0x00e7:
        if (r15 == 0) goto L_0x00f9;
    L_0x00e9:
        r5 = "EnableGroup";
        r13 = 1;
        r0 = r54;
        r5 = r0.getBoolean(r5, r13);	 Catch:{ Exception -> 0x0052 }
        if (r5 != 0) goto L_0x00f9;
    L_0x00f5:
        if (r49 != 0) goto L_0x00f9;
    L_0x00f7:
        r47 = 1;
    L_0x00f9:
        if (r47 != 0) goto L_0x017f;
    L_0x00fb:
        r5 = (r6 > r52 ? 1 : (r6 == r52 ? 0 : -1));
        if (r5 != 0) goto L_0x017f;
    L_0x00ff:
        if (r14 == 0) goto L_0x017f;
    L_0x0101:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "custom_";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r0 = r54;
        r5 = r0.getBoolean(r5, r13);	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0684;
    L_0x011e:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "smart_max_count_";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = 2;
        r0 = r54;
        r48 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "smart_delay_";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        r0 = r54;
        r46 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
    L_0x0155:
        if (r48 == 0) goto L_0x017f;
    L_0x0157:
        r0 = r72;
        r5 = r0.smartNotificationsDialogs;	 Catch:{ Exception -> 0x0052 }
        r22 = r5.get(r6);	 Catch:{ Exception -> 0x0052 }
        r22 = (android.graphics.Point) r22;	 Catch:{ Exception -> 0x0052 }
        if (r22 != 0) goto L_0x068a;
    L_0x0163:
        r22 = new android.graphics.Point;	 Catch:{ Exception -> 0x0052 }
        r5 = 1;
        r66 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0052 }
        r68 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r66 = r66 / r68;
        r0 = r66;
        r13 = (int) r0;	 Catch:{ Exception -> 0x0052 }
        r0 = r22;
        r0.<init>(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r0 = r72;
        r5 = r0.smartNotificationsDialogs;	 Catch:{ Exception -> 0x0052 }
        r0 = r22;
        r5.put(r6, r0);	 Catch:{ Exception -> 0x0052 }
    L_0x017f:
        r5 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x0052 }
        r20 = r5.getPath();	 Catch:{ Exception -> 0x0052 }
        if (r47 != 0) goto L_0x02e0;
    L_0x0187:
        r5 = "EnableInAppSounds";
        r13 = 1;
        r0 = r54;
        r32 = r0.getBoolean(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "EnableInAppVibrate";
        r13 = 1;
        r0 = r54;
        r33 = r0.getBoolean(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "EnableInAppPreview";
        r13 = 1;
        r0 = r54;
        r30 = r0.getBoolean(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "EnableInAppPriority";
        r13 = 0;
        r0 = r54;
        r31 = r0.getBoolean(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "custom_";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r0 = r54;
        r19 = r0.getBoolean(r5, r13);	 Catch:{ Exception -> 0x0052 }
        if (r19 == 0) goto L_0x06d8;
    L_0x01cc:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "vibrate_";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r0 = r54;
        r65 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "priority_";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = 3;
        r0 = r54;
        r56 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "sound_path_";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r0 = r54;
        r16 = r0.getString(r5, r13);	 Catch:{ Exception -> 0x0052 }
    L_0x021d:
        r64 = 0;
        if (r15 == 0) goto L_0x06ef;
    L_0x0221:
        if (r16 == 0) goto L_0x06e0;
    L_0x0223:
        r0 = r16;
        r1 = r20;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x06e0;
    L_0x022d:
        r16 = 0;
    L_0x022f:
        r5 = "vibrate_group";
        r13 = 0;
        r0 = r54;
        r45 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "priority_group";
        r13 = 1;
        r0 = r54;
        r55 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "GroupLed";
        r13 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
        r0 = r54;
        r10 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
    L_0x024f:
        if (r19 == 0) goto L_0x0288;
    L_0x0251:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "color_";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r0 = r54;
        r5 = r0.contains(r5);	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0288;
    L_0x026d:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "color_";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r6);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r0 = r54;
        r10 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
    L_0x0288:
        r5 = 3;
        r0 = r56;
        if (r0 == r5) goto L_0x028f;
    L_0x028d:
        r55 = r56;
    L_0x028f:
        r5 = 4;
        r0 = r45;
        if (r0 != r5) goto L_0x0298;
    L_0x0294:
        r64 = 1;
        r45 = 0;
    L_0x0298:
        r5 = 2;
        r0 = r45;
        if (r0 != r5) goto L_0x02a7;
    L_0x029d:
        r5 = 1;
        r0 = r65;
        if (r0 == r5) goto L_0x02b8;
    L_0x02a2:
        r5 = 3;
        r0 = r65;
        if (r0 == r5) goto L_0x02b8;
    L_0x02a7:
        r5 = 2;
        r0 = r45;
        if (r0 == r5) goto L_0x02b1;
    L_0x02ac:
        r5 = 2;
        r0 = r65;
        if (r0 == r5) goto L_0x02b8;
    L_0x02b1:
        if (r65 == 0) goto L_0x02ba;
    L_0x02b3:
        r5 = 4;
        r0 = r65;
        if (r0 == r5) goto L_0x02ba;
    L_0x02b8:
        r45 = r65;
    L_0x02ba:
        r5 = org.telegram.messenger.ApplicationLoader.mainInterfacePaused;	 Catch:{ Exception -> 0x0052 }
        if (r5 != 0) goto L_0x02ca;
    L_0x02be:
        if (r32 != 0) goto L_0x02c2;
    L_0x02c0:
        r16 = 0;
    L_0x02c2:
        if (r33 != 0) goto L_0x02c6;
    L_0x02c4:
        r45 = 2;
    L_0x02c6:
        if (r31 != 0) goto L_0x072f;
    L_0x02c8:
        r55 = 0;
    L_0x02ca:
        if (r64 == 0) goto L_0x02e0;
    L_0x02cc:
        r5 = 2;
        r0 = r45;
        if (r0 == r5) goto L_0x02e0;
    L_0x02d1:
        r5 = audioManager;	 Catch:{ Exception -> 0x0738 }
        r43 = r5.getRingerMode();	 Catch:{ Exception -> 0x0738 }
        if (r43 == 0) goto L_0x02e0;
    L_0x02d9:
        r5 = 1;
        r0 = r43;
        if (r0 == r5) goto L_0x02e0;
    L_0x02de:
        r45 = 2;
    L_0x02e0:
        r35 = new android.content.Intent;	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0052 }
        r13 = org.telegram.ui.LaunchActivity.class;
        r0 = r35;
        r0.<init>(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "com.tmessages.openchat";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r66 = java.lang.Math.random();	 Catch:{ Exception -> 0x0052 }
        r0 = r66;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0052 }
        r13 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r0 = r35;
        r0.setAction(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r0 = r35;
        r0.setFlags(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = (int) r6;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x07b5;
    L_0x031c:
        r0 = r72;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 != r13) goto L_0x0331;
    L_0x0327:
        if (r15 == 0) goto L_0x073e;
    L_0x0329:
        r5 = "chatId";
        r0 = r35;
        r0.putExtra(r5, r15);	 Catch:{ Exception -> 0x0052 }
    L_0x0331:
        r5 = 0;
        r5 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r5);	 Catch:{ Exception -> 0x0052 }
        if (r5 != 0) goto L_0x033c;
    L_0x0338:
        r5 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x074c;
    L_0x033c:
        r51 = 0;
    L_0x033e:
        r5 = "currentAccount";
        r0 = r72;
        r13 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r0 = r35;
        r0.putExtra(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r66 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r0 = r35;
        r1 = r66;
        r17 = android.app.PendingIntent.getActivity(r5, r13, r0, r1);	 Catch:{ Exception -> 0x0052 }
        r57 = 1;
        if (r14 == 0) goto L_0x07d1;
    L_0x035b:
        r8 = r14.title;	 Catch:{ Exception -> 0x0052 }
    L_0x035d:
        r5 = (int) r6;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0376;
    L_0x0360:
        r0 = r72;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 > r13) goto L_0x0376;
    L_0x036b:
        r5 = 0;
        r5 = org.telegram.messenger.AndroidUtilities.needShowPasscode(r5);	 Catch:{ Exception -> 0x0052 }
        if (r5 != 0) goto L_0x0376;
    L_0x0372:
        r5 = org.telegram.messenger.SharedConfig.isWaitingForPasscodeEnter;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x07d7;
    L_0x0376:
        r5 = "AppName";
        r13 = 2131492979; // 0x7f0c0073 float:1.8609425E38 double:1.0530974553E-314;
        r44 = org.telegram.messenger.LocaleController.getString(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r57 = 0;
    L_0x0382:
        r5 = org.telegram.messenger.UserConfig.getActivatedAccountsCount();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 <= r13) goto L_0x0801;
    L_0x0389:
        r0 = r72;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 != r13) goto L_0x07db;
    L_0x0394:
        r0 = r72;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.getCurrentUser();	 Catch:{ Exception -> 0x0052 }
        r21 = org.telegram.messenger.UserObject.getFirstName(r5);	 Catch:{ Exception -> 0x0052 }
    L_0x03a4:
        r0 = r72;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 != r13) goto L_0x03b5;
    L_0x03af:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0052 }
        r13 = 23;
        if (r5 >= r13) goto L_0x03e2;
    L_0x03b5:
        r0 = r72;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 != r13) goto L_0x0806;
    L_0x03c0:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r0 = r21;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0052 }
        r13 = "NewMessages";
        r0 = r72;
        r0 = r0.total_unread_count;	 Catch:{ Exception -> 0x0052 }
        r66 = r0;
        r0 = r66;
        r13 = org.telegram.messenger.LocaleController.formatPluralString(r13, r0);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r21 = r5.toString();	 Catch:{ Exception -> 0x0052 }
    L_0x03e2:
        r5 = new android.support.v4.app.NotificationCompat$Builder;	 Catch:{ Exception -> 0x0052 }
        r13 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0052 }
        r5.<init>(r13);	 Catch:{ Exception -> 0x0052 }
        r0 = r44;
        r5 = r5.setContentTitle(r0);	 Catch:{ Exception -> 0x0052 }
        r13 = 2131165529; // 0x7f070159 float:1.7945278E38 double:1.0529356735E-314;
        r5 = r5.setSmallIcon(r13);	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        r5 = r5.setAutoCancel(r13);	 Catch:{ Exception -> 0x0052 }
        r0 = r72;
        r13 = r0.total_unread_count;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.setNumber(r13);	 Catch:{ Exception -> 0x0052 }
        r0 = r17;
        r5 = r5.setContentIntent(r0);	 Catch:{ Exception -> 0x0052 }
        r0 = r72;
        r13 = r0.notificationGroup;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.setGroup(r13);	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        r5 = r5.setGroupSummary(r13);	 Catch:{ Exception -> 0x0052 }
        r13 = -13851168; // 0xffffffffff2ca5e0 float:-2.2948849E38 double:NaN;
        r39 = r5.setColor(r13);	 Catch:{ Exception -> 0x0052 }
        r9 = 0;
        r12 = 0;
        r11 = 0;
        r5 = "msg";
        r0 = r39;
        r0.setCategory(r5);	 Catch:{ Exception -> 0x0052 }
        if (r14 != 0) goto L_0x0459;
    L_0x042a:
        if (r62 == 0) goto L_0x0459;
    L_0x042c:
        r0 = r62;
        r5 = r0.phone;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0459;
    L_0x0432:
        r0 = r62;
        r5 = r0.phone;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.length();	 Catch:{ Exception -> 0x0052 }
        if (r5 <= 0) goto L_0x0459;
    L_0x043c:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = "tel:+";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r0 = r62;
        r13 = r0.phone;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r0.addPerson(r5);	 Catch:{ Exception -> 0x0052 }
    L_0x0459:
        r60 = 2;
        r36 = 0;
        r27 = 0;
        r0 = r72;
        r5 = r0.pushMessages;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 != r13) goto L_0x08a2;
    L_0x046a:
        r0 = r72;
        r5 = r0.pushMessages;	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r41 = r5.get(r13);	 Catch:{ Exception -> 0x0052 }
        r41 = (org.telegram.messenger.MessageObject) r41;	 Catch:{ Exception -> 0x0052 }
        r5 = 1;
        r0 = new boolean[r5];	 Catch:{ Exception -> 0x0052 }
        r61 = r0;
        r5 = 0;
        r0 = r72;
        r1 = r41;
        r2 = r61;
        r36 = r0.getStringForMessage(r1, r5, r2);	 Catch:{ Exception -> 0x0052 }
        r40 = r36;
        r0 = r41;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.silent;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0857;
    L_0x048f:
        r60 = 1;
    L_0x0491:
        if (r40 == 0) goto L_0x001b;
    L_0x0493:
        if (r57 == 0) goto L_0x04b6;
    L_0x0495:
        if (r14 == 0) goto L_0x085b;
    L_0x0497:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = " @ ";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r0 = r44;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = "";
        r0 = r40;
        r40 = r0.replace(r5, r13);	 Catch:{ Exception -> 0x0052 }
    L_0x04b6:
        r39.setContentText(r40);	 Catch:{ Exception -> 0x0052 }
        r5 = new android.support.v4.app.NotificationCompat$BigTextStyle;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r0 = r40;
        r5 = r5.bigText(r0);	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r0.setStyle(r5);	 Catch:{ Exception -> 0x0052 }
    L_0x04c9:
        r24 = new android.content.Intent;	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0052 }
        r13 = org.telegram.messenger.NotificationDismissReceiver.class;
        r0 = r24;
        r0.<init>(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "messageDate";
        r0 = r37;
        r13 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r13 = r13.date;	 Catch:{ Exception -> 0x0052 }
        r0 = r24;
        r0.putExtra(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "currentAccount";
        r0 = r72;
        r13 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r0 = r24;
        r0.putExtra(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        r66 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r0 = r24;
        r1 = r66;
        r5 = android.app.PendingIntent.getBroadcast(r5, r13, r0, r1);	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r0.setDeleteIntent(r5);	 Catch:{ Exception -> 0x0052 }
        if (r51 == 0) goto L_0x051d;
    L_0x0502:
        r5 = org.telegram.messenger.ImageLoader.getInstance();	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r66 = "50_50";
        r0 = r51;
        r1 = r66;
        r29 = r5.getImageFromMemory(r0, r13, r1);	 Catch:{ Exception -> 0x0052 }
        if (r29 == 0) goto L_0x0995;
    L_0x0514:
        r5 = r29.getBitmap();	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r0.setLargeIcon(r5);	 Catch:{ Exception -> 0x0052 }
    L_0x051d:
        if (r73 == 0) goto L_0x0524;
    L_0x051f:
        r5 = 1;
        r0 = r60;
        if (r0 != r5) goto L_0x09d7;
    L_0x0524:
        r5 = -1;
        r0 = r39;
        r0.setPriority(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0052 }
        r13 = 26;
        if (r5 < r13) goto L_0x0531;
    L_0x0530:
        r12 = 2;
    L_0x0531:
        r5 = 1;
        r0 = r60;
        if (r0 == r5) goto L_0x0a6c;
    L_0x0536:
        if (r47 != 0) goto L_0x0a6c;
    L_0x0538:
        r5 = org.telegram.messenger.ApplicationLoader.mainInterfacePaused;	 Catch:{ Exception -> 0x0052 }
        if (r5 != 0) goto L_0x053e;
    L_0x053c:
        if (r30 == 0) goto L_0x057c;
    L_0x053e:
        r5 = r36.length();	 Catch:{ Exception -> 0x0052 }
        r13 = 100;
        if (r5 <= r13) goto L_0x0575;
    L_0x0546:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = 0;
        r66 = 100;
        r0 = r36;
        r1 = r66;
        r13 = r0.substring(r13, r1);	 Catch:{ Exception -> 0x0052 }
        r66 = 10;
        r67 = 32;
        r0 = r66;
        r1 = r67;
        r13 = r13.replace(r0, r1);	 Catch:{ Exception -> 0x0052 }
        r13 = r13.trim();	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r13 = "...";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r36 = r5.toString();	 Catch:{ Exception -> 0x0052 }
    L_0x0575:
        r0 = r39;
        r1 = r36;
        r0.setTicker(r1);	 Catch:{ Exception -> 0x0052 }
    L_0x057c:
        r5 = org.telegram.messenger.MediaController.getInstance();	 Catch:{ Exception -> 0x0052 }
        r5 = r5.isRecordingAudio();	 Catch:{ Exception -> 0x0052 }
        if (r5 != 0) goto L_0x05a5;
    L_0x0586:
        if (r16 == 0) goto L_0x05a5;
    L_0x0588:
        r5 = "NoSound";
        r0 = r16;
        r5 = r0.equals(r5);	 Catch:{ Exception -> 0x0052 }
        if (r5 != 0) goto L_0x05a5;
    L_0x0593:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0052 }
        r13 = 26;
        if (r5 < r13) goto L_0x0a16;
    L_0x0599:
        r0 = r16;
        r1 = r20;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0a10;
    L_0x05a3:
        r11 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x0052 }
    L_0x05a5:
        if (r10 == 0) goto L_0x05b0;
    L_0x05a7:
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r13 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = r39;
        r0.setLights(r10, r5, r13);	 Catch:{ Exception -> 0x0052 }
    L_0x05b0:
        r5 = 2;
        r0 = r45;
        if (r0 == r5) goto L_0x05bf;
    L_0x05b5:
        r5 = org.telegram.messenger.MediaController.getInstance();	 Catch:{ Exception -> 0x0052 }
        r5 = r5.isRecordingAudio();	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0a36;
    L_0x05bf:
        r5 = 2;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0052 }
        r9 = {0, 0};	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r0.setVibrate(r9);	 Catch:{ Exception -> 0x0052 }
    L_0x05ca:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0052 }
        r13 = 24;
        if (r5 >= r13) goto L_0x0623;
    L_0x05d0:
        r5 = org.telegram.messenger.SharedConfig.passcodeHash;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.length();	 Catch:{ Exception -> 0x0052 }
        if (r5 != 0) goto L_0x0623;
    L_0x05d8:
        r5 = r72.hasMessagesToReply();	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0623;
    L_0x05de:
        r58 = new android.content.Intent;	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0052 }
        r13 = org.telegram.messenger.PopupReplyReceiver.class;
        r0 = r58;
        r0.<init>(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "currentAccount";
        r0 = r72;
        r13 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r0 = r58;
        r0.putExtra(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0052 }
        r13 = 19;
        if (r5 > r13) goto L_0x0a79;
    L_0x05fb:
        r5 = 2131165347; // 0x7f0700a3 float:1.7944909E38 double:1.0529355836E-314;
        r13 = "Reply";
        r66 = 2131494184; // 0x7f0c0528 float:1.861187E38 double:1.0530980506E-314;
        r0 = r66;
        r13 = org.telegram.messenger.LocaleController.getString(r13, r0);	 Catch:{ Exception -> 0x0052 }
        r66 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0052 }
        r67 = 2;
        r68 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r0 = r66;
        r1 = r67;
        r2 = r58;
        r3 = r68;
        r66 = android.app.PendingIntent.getBroadcast(r0, r1, r2, r3);	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r1 = r66;
        r0.addAction(r5, r13, r1);	 Catch:{ Exception -> 0x0052 }
    L_0x0623:
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0052 }
        r13 = 26;
        if (r5 < r13) goto L_0x063c;
    L_0x0629:
        r5 = 1;
        r0 = r60;
        if (r0 == r5) goto L_0x0630;
    L_0x062e:
        if (r47 == 0) goto L_0x0aa3;
    L_0x0630:
        r13 = 1;
    L_0x0631:
        r5 = r72;
        r5 = r5.validateChannelId(r6, r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r0.setChannelId(r5);	 Catch:{ Exception -> 0x0052 }
    L_0x063c:
        r5 = notificationManager;	 Catch:{ Exception -> 0x0052 }
        r0 = r72;
        r13 = r0.notificationId;	 Catch:{ Exception -> 0x0052 }
        r66 = r39.build();	 Catch:{ Exception -> 0x0052 }
        r0 = r66;
        r5.notify(r13, r0);	 Catch:{ Exception -> 0x0052 }
        r5 = 0;
        r0 = r72;
        r0.lastNotificationIsNoData = r5;	 Catch:{ Exception -> 0x0052 }
        r0 = r72;
        r1 = r39;
        r2 = r73;
        r3 = r21;
        r0.showExtraNotifications(r1, r2, r3);	 Catch:{ Exception -> 0x0052 }
        r72.scheduleNotificationRepeat();	 Catch:{ Exception -> 0x0052 }
        goto L_0x001b;
    L_0x0660:
        r0 = r37;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.to_id;	 Catch:{ Exception -> 0x0052 }
        r15 = r5.channel_id;	 Catch:{ Exception -> 0x0052 }
        goto L_0x0084;
    L_0x066a:
        r0 = r72;
        r5 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.getClientUserId();	 Catch:{ Exception -> 0x0052 }
        r0 = r63;
        if (r0 != r5) goto L_0x0098;
    L_0x067a:
        r0 = r37;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r0 = r5.from_id;	 Catch:{ Exception -> 0x0052 }
        r63 = r0;
        goto L_0x0098;
    L_0x0684:
        r48 = 2;
        r46 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        goto L_0x0155;
    L_0x068a:
        r0 = r22;
        r0 = r0.y;	 Catch:{ Exception -> 0x0052 }
        r38 = r0;
        r5 = r38 + r46;
        r0 = (long) r5;	 Catch:{ Exception -> 0x0052 }
        r66 = r0;
        r68 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0052 }
        r70 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r68 = r68 / r70;
        r5 = (r66 > r68 ? 1 : (r66 == r68 ? 0 : -1));
        if (r5 >= 0) goto L_0x06b4;
    L_0x06a1:
        r5 = 1;
        r66 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0052 }
        r68 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r66 = r66 / r68;
        r0 = r66;
        r13 = (int) r0;	 Catch:{ Exception -> 0x0052 }
        r0 = r22;
        r0.set(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x017f;
    L_0x06b4:
        r0 = r22;
        r0 = r0.x;	 Catch:{ Exception -> 0x0052 }
        r18 = r0;
        r0 = r18;
        r1 = r48;
        if (r0 >= r1) goto L_0x06d4;
    L_0x06c0:
        r5 = r18 + 1;
        r66 = java.lang.System.currentTimeMillis();	 Catch:{ Exception -> 0x0052 }
        r68 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r66 = r66 / r68;
        r0 = r66;
        r13 = (int) r0;	 Catch:{ Exception -> 0x0052 }
        r0 = r22;
        r0.set(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x017f;
    L_0x06d4:
        r47 = 1;
        goto L_0x017f;
    L_0x06d8:
        r65 = 0;
        r56 = 3;
        r16 = 0;
        goto L_0x021d;
    L_0x06e0:
        if (r16 != 0) goto L_0x022f;
    L_0x06e2:
        r5 = "GroupSoundPath";
        r0 = r54;
        r1 = r20;
        r16 = r0.getString(r5, r1);	 Catch:{ Exception -> 0x0052 }
        goto L_0x022f;
    L_0x06ef:
        if (r63 == 0) goto L_0x024f;
    L_0x06f1:
        if (r16 == 0) goto L_0x0721;
    L_0x06f3:
        r0 = r16;
        r1 = r20;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0721;
    L_0x06fd:
        r16 = 0;
    L_0x06ff:
        r5 = "vibrate_messages";
        r13 = 0;
        r0 = r54;
        r45 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "priority_group";
        r13 = 1;
        r0 = r54;
        r55 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = "MessagesLed";
        r13 = -16776961; // 0xffffffffff0000ff float:-1.7014636E38 double:NaN;
        r0 = r54;
        r10 = r0.getInt(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x024f;
    L_0x0721:
        if (r16 != 0) goto L_0x06ff;
    L_0x0723:
        r5 = "GlobalSoundPath";
        r0 = r54;
        r1 = r20;
        r16 = r0.getString(r5, r1);	 Catch:{ Exception -> 0x0052 }
        goto L_0x06ff;
    L_0x072f:
        r5 = 2;
        r0 = r55;
        if (r0 != r5) goto L_0x02ca;
    L_0x0734:
        r55 = 1;
        goto L_0x02ca;
    L_0x0738:
        r25 = move-exception;
        org.telegram.messenger.FileLog.e(r25);	 Catch:{ Exception -> 0x0052 }
        goto L_0x02e0;
    L_0x073e:
        if (r63 == 0) goto L_0x0331;
    L_0x0740:
        r5 = "userId";
        r0 = r35;
        r1 = r63;
        r0.putExtra(r5, r1);	 Catch:{ Exception -> 0x0052 }
        goto L_0x0331;
    L_0x074c:
        r0 = r72;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 != r13) goto L_0x033e;
    L_0x0757:
        if (r14 == 0) goto L_0x0781;
    L_0x0759:
        r5 = r14.photo;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x033e;
    L_0x075d:
        r5 = r14.photo;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x033e;
    L_0x0763:
        r5 = r14.photo;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0052 }
        r0 = r5.volume_id;	 Catch:{ Exception -> 0x0052 }
        r66 = r0;
        r68 = 0;
        r5 = (r66 > r68 ? 1 : (r66 == r68 ? 0 : -1));
        if (r5 == 0) goto L_0x033e;
    L_0x0771:
        r5 = r14.photo;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.local_id;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x033e;
    L_0x0779:
        r5 = r14.photo;	 Catch:{ Exception -> 0x0052 }
        r0 = r5.photo_small;	 Catch:{ Exception -> 0x0052 }
        r51 = r0;
        goto L_0x033e;
    L_0x0781:
        if (r62 == 0) goto L_0x033e;
    L_0x0783:
        r0 = r62;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x033e;
    L_0x0789:
        r0 = r62;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x033e;
    L_0x0791:
        r0 = r62;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0052 }
        r0 = r5.volume_id;	 Catch:{ Exception -> 0x0052 }
        r66 = r0;
        r68 = 0;
        r5 = (r66 > r68 ? 1 : (r66 == r68 ? 0 : -1));
        if (r5 == 0) goto L_0x033e;
    L_0x07a1:
        r0 = r62;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.photo_small;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.local_id;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x033e;
    L_0x07ab:
        r0 = r62;
        r5 = r0.photo;	 Catch:{ Exception -> 0x0052 }
        r0 = r5.photo_small;	 Catch:{ Exception -> 0x0052 }
        r51 = r0;
        goto L_0x033e;
    L_0x07b5:
        r0 = r72;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 != r13) goto L_0x033e;
    L_0x07c0:
        r5 = "encId";
        r13 = 32;
        r66 = r6 >> r13;
        r0 = r66;
        r13 = (int) r0;	 Catch:{ Exception -> 0x0052 }
        r0 = r35;
        r0.putExtra(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x033e;
    L_0x07d1:
        r8 = org.telegram.messenger.UserObject.getUserName(r62);	 Catch:{ Exception -> 0x0052 }
        goto L_0x035d;
    L_0x07d7:
        r44 = r8;
        goto L_0x0382;
    L_0x07db:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r0 = r72;
        r13 = r0.currentAccount;	 Catch:{ Exception -> 0x0052 }
        r13 = org.telegram.messenger.UserConfig.getInstance(r13);	 Catch:{ Exception -> 0x0052 }
        r13 = r13.getCurrentUser();	 Catch:{ Exception -> 0x0052 }
        r13 = org.telegram.messenger.UserObject.getFirstName(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r13 = "・";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r21 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        goto L_0x03a4;
    L_0x0801:
        r21 = "";
        goto L_0x03a4;
    L_0x0806:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r0 = r21;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0052 }
        r13 = "NotificationMessagesPeopleDisplayOrder";
        r66 = 2131493966; // 0x7f0c044e float:1.8611427E38 double:1.053097943E-314;
        r67 = 2;
        r0 = r67;
        r0 = new java.lang.Object[r0];	 Catch:{ Exception -> 0x0052 }
        r67 = r0;
        r68 = 0;
        r69 = "NewMessages";
        r0 = r72;
        r0 = r0.total_unread_count;	 Catch:{ Exception -> 0x0052 }
        r70 = r0;
        r69 = org.telegram.messenger.LocaleController.formatPluralString(r69, r70);	 Catch:{ Exception -> 0x0052 }
        r67[r68] = r69;	 Catch:{ Exception -> 0x0052 }
        r68 = 1;
        r69 = "FromChats";
        r0 = r72;
        r0 = r0.pushDialogs;	 Catch:{ Exception -> 0x0052 }
        r70 = r0;
        r70 = r70.size();	 Catch:{ Exception -> 0x0052 }
        r69 = org.telegram.messenger.LocaleController.formatPluralString(r69, r70);	 Catch:{ Exception -> 0x0052 }
        r67[r68] = r69;	 Catch:{ Exception -> 0x0052 }
        r0 = r66;
        r1 = r67;
        r13 = org.telegram.messenger.LocaleController.formatString(r13, r0, r1);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r21 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        goto L_0x03e2;
    L_0x0857:
        r60 = 0;
        goto L_0x0491;
    L_0x085b:
        r5 = 0;
        r5 = r61[r5];	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0881;
    L_0x0860:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r0 = r44;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0052 }
        r13 = ": ";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = "";
        r0 = r40;
        r40 = r0.replace(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x04b6;
    L_0x0881:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r0 = r44;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0052 }
        r13 = " ";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = "";
        r0 = r40;
        r40 = r0.replace(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x04b6;
    L_0x08a2:
        r0 = r39;
        r1 = r21;
        r0.setContentText(r1);	 Catch:{ Exception -> 0x0052 }
        r34 = new android.support.v4.app.NotificationCompat$InboxStyle;	 Catch:{ Exception -> 0x0052 }
        r34.<init>();	 Catch:{ Exception -> 0x0052 }
        r0 = r34;
        r1 = r44;
        r0.setBigContentTitle(r1);	 Catch:{ Exception -> 0x0052 }
        r5 = 10;
        r0 = r72;
        r13 = r0.pushMessages;	 Catch:{ Exception -> 0x0052 }
        r13 = r13.size();	 Catch:{ Exception -> 0x0052 }
        r18 = java.lang.Math.min(r5, r13);	 Catch:{ Exception -> 0x0052 }
        r5 = 1;
        r0 = new boolean[r5];	 Catch:{ Exception -> 0x0052 }
        r61 = r0;
        r28 = 0;
    L_0x08ca:
        r0 = r28;
        r1 = r18;
        if (r0 >= r1) goto L_0x0985;
    L_0x08d0:
        r0 = r72;
        r5 = r0.pushMessages;	 Catch:{ Exception -> 0x0052 }
        r0 = r28;
        r41 = r5.get(r0);	 Catch:{ Exception -> 0x0052 }
        r41 = (org.telegram.messenger.MessageObject) r41;	 Catch:{ Exception -> 0x0052 }
        r5 = 0;
        r0 = r72;
        r1 = r41;
        r2 = r61;
        r40 = r0.getStringForMessage(r1, r5, r2);	 Catch:{ Exception -> 0x0052 }
        if (r40 == 0) goto L_0x08f3;
    L_0x08e9:
        r0 = r41;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.date;	 Catch:{ Exception -> 0x0052 }
        r0 = r23;
        if (r5 > r0) goto L_0x08f6;
    L_0x08f3:
        r28 = r28 + 1;
        goto L_0x08ca;
    L_0x08f6:
        r5 = 2;
        r0 = r60;
        if (r0 != r5) goto L_0x0907;
    L_0x08fb:
        r36 = r40;
        r0 = r41;
        r5 = r0.messageOwner;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.silent;	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x093d;
    L_0x0905:
        r60 = 1;
    L_0x0907:
        r0 = r72;
        r5 = r0.pushDialogs;	 Catch:{ Exception -> 0x0052 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0052 }
        r13 = 1;
        if (r5 != r13) goto L_0x0935;
    L_0x0912:
        if (r57 == 0) goto L_0x0935;
    L_0x0914:
        if (r14 == 0) goto L_0x0940;
    L_0x0916:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r13 = " @ ";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r0 = r44;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = "";
        r0 = r40;
        r40 = r0.replace(r5, r13);	 Catch:{ Exception -> 0x0052 }
    L_0x0935:
        r0 = r34;
        r1 = r40;
        r0.addLine(r1);	 Catch:{ Exception -> 0x0052 }
        goto L_0x08f3;
    L_0x093d:
        r60 = 0;
        goto L_0x0907;
    L_0x0940:
        r5 = 0;
        r5 = r61[r5];	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0965;
    L_0x0945:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r0 = r44;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0052 }
        r13 = ": ";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = "";
        r0 = r40;
        r40 = r0.replace(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x0935;
    L_0x0965:
        r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0052 }
        r5.<init>();	 Catch:{ Exception -> 0x0052 }
        r0 = r44;
        r5 = r5.append(r0);	 Catch:{ Exception -> 0x0052 }
        r13 = " ";
        r5 = r5.append(r13);	 Catch:{ Exception -> 0x0052 }
        r5 = r5.toString();	 Catch:{ Exception -> 0x0052 }
        r13 = "";
        r0 = r40;
        r40 = r0.replace(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x0935;
    L_0x0985:
        r0 = r34;
        r1 = r21;
        r0.setSummaryText(r1);	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r1 = r34;
        r0.setStyle(r1);	 Catch:{ Exception -> 0x0052 }
        goto L_0x04c9;
    L_0x0995:
        r5 = 1;
        r0 = r51;
        r26 = org.telegram.messenger.FileLoader.getPathToAttach(r0, r5);	 Catch:{ Throwable -> 0x09d0 }
        r5 = r26.exists();	 Catch:{ Throwable -> 0x09d0 }
        if (r5 == 0) goto L_0x051d;
    L_0x09a2:
        r5 = 1126170624; // 0x43200000 float:160.0 double:5.564022167E-315;
        r13 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);	 Catch:{ Throwable -> 0x09d0 }
        r13 = (float) r13;	 Catch:{ Throwable -> 0x09d0 }
        r59 = r5 / r13;
        r50 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x09d0 }
        r50.<init>();	 Catch:{ Throwable -> 0x09d0 }
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r5 = (r59 > r5 ? 1 : (r59 == r5 ? 0 : -1));
        if (r5 >= 0) goto L_0x09d3;
    L_0x09b8:
        r5 = 1;
    L_0x09b9:
        r0 = r50;
        r0.inSampleSize = r5;	 Catch:{ Throwable -> 0x09d0 }
        r5 = r26.getAbsolutePath();	 Catch:{ Throwable -> 0x09d0 }
        r0 = r50;
        r4 = android.graphics.BitmapFactory.decodeFile(r5, r0);	 Catch:{ Throwable -> 0x09d0 }
        if (r4 == 0) goto L_0x051d;
    L_0x09c9:
        r0 = r39;
        r0.setLargeIcon(r4);	 Catch:{ Throwable -> 0x09d0 }
        goto L_0x051d;
    L_0x09d0:
        r5 = move-exception;
        goto L_0x051d;
    L_0x09d3:
        r0 = r59;
        r5 = (int) r0;
        goto L_0x09b9;
    L_0x09d7:
        if (r55 != 0) goto L_0x09e8;
    L_0x09d9:
        r5 = 0;
        r0 = r39;
        r0.setPriority(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0052 }
        r13 = 26;
        if (r5 < r13) goto L_0x0531;
    L_0x09e5:
        r12 = 3;
        goto L_0x0531;
    L_0x09e8:
        r5 = 1;
        r0 = r55;
        if (r0 != r5) goto L_0x09fc;
    L_0x09ed:
        r5 = 1;
        r0 = r39;
        r0.setPriority(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0052 }
        r13 = 26;
        if (r5 < r13) goto L_0x0531;
    L_0x09f9:
        r12 = 4;
        goto L_0x0531;
    L_0x09fc:
        r5 = 2;
        r0 = r55;
        if (r0 != r5) goto L_0x0531;
    L_0x0a01:
        r5 = 2;
        r0 = r39;
        r0.setPriority(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x0052 }
        r13 = 26;
        if (r5 < r13) goto L_0x0531;
    L_0x0a0d:
        r12 = 5;
        goto L_0x0531;
    L_0x0a10:
        r11 = android.net.Uri.parse(r16);	 Catch:{ Exception -> 0x0052 }
        goto L_0x05a5;
    L_0x0a16:
        r0 = r16;
        r1 = r20;
        r5 = r0.equals(r1);	 Catch:{ Exception -> 0x0052 }
        if (r5 == 0) goto L_0x0a2a;
    L_0x0a20:
        r5 = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI;	 Catch:{ Exception -> 0x0052 }
        r13 = 5;
        r0 = r39;
        r0.setSound(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x05a5;
    L_0x0a2a:
        r5 = android.net.Uri.parse(r16);	 Catch:{ Exception -> 0x0052 }
        r13 = 5;
        r0 = r39;
        r0.setSound(r5, r13);	 Catch:{ Exception -> 0x0052 }
        goto L_0x05a5;
    L_0x0a36:
        r5 = 1;
        r0 = r45;
        if (r0 != r5) goto L_0x0a48;
    L_0x0a3b:
        r5 = 4;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0052 }
        r9 = {0, 100, 0, 100};	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r0.setVibrate(r9);	 Catch:{ Exception -> 0x0052 }
        goto L_0x05ca;
    L_0x0a48:
        if (r45 == 0) goto L_0x0a4f;
    L_0x0a4a:
        r5 = 4;
        r0 = r45;
        if (r0 != r5) goto L_0x0a5a;
    L_0x0a4f:
        r5 = 2;
        r0 = r39;
        r0.setDefaults(r5);	 Catch:{ Exception -> 0x0052 }
        r5 = 0;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0052 }
        goto L_0x05ca;
    L_0x0a5a:
        r5 = 3;
        r0 = r45;
        if (r0 != r5) goto L_0x05ca;
    L_0x0a5f:
        r5 = 2;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0052 }
        r9 = {0, 1000};	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r0.setVibrate(r9);	 Catch:{ Exception -> 0x0052 }
        goto L_0x05ca;
    L_0x0a6c:
        r5 = 2;
        r9 = new long[r5];	 Catch:{ Exception -> 0x0052 }
        r9 = {0, 0};	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r0.setVibrate(r9);	 Catch:{ Exception -> 0x0052 }
        goto L_0x05ca;
    L_0x0a79:
        r5 = 2131165346; // 0x7f0700a2 float:1.7944907E38 double:1.052935583E-314;
        r13 = "Reply";
        r66 = 2131494184; // 0x7f0c0528 float:1.861187E38 double:1.0530980506E-314;
        r0 = r66;
        r13 = org.telegram.messenger.LocaleController.getString(r13, r0);	 Catch:{ Exception -> 0x0052 }
        r66 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0052 }
        r67 = 2;
        r68 = 134217728; // 0x8000000 float:3.85186E-34 double:6.63123685E-316;
        r0 = r66;
        r1 = r67;
        r2 = r58;
        r3 = r68;
        r66 = android.app.PendingIntent.getBroadcast(r0, r1, r2, r3);	 Catch:{ Exception -> 0x0052 }
        r0 = r39;
        r1 = r66;
        r0.addAction(r5, r13, r1);	 Catch:{ Exception -> 0x0052 }
        goto L_0x0623;
    L_0x0aa3:
        r13 = 0;
        goto L_0x0631;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.NotificationsController.showOrUpdateNotification(boolean):void");
    }

    @SuppressLint({"InlinedApi"})
    private void showExtraNotifications(Builder notificationBuilder, boolean notifyAboutLast, String summary) {
        if (VERSION.SDK_INT >= 18) {
            int a;
            MessageObject messageObject;
            long dialog_id;
            ArrayList<Long> sortedDialogs = new ArrayList();
            LongSparseArray<ArrayList<MessageObject>> messagesByDialogs = new LongSparseArray();
            for (a = 0; a < this.pushMessages.size(); a++) {
                messageObject = (MessageObject) this.pushMessages.get(a);
                dialog_id = messageObject.getDialogId();
                if (((int) dialog_id) != 0) {
                    ArrayList<MessageObject> arrayList = (ArrayList) messagesByDialogs.get(dialog_id);
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        messagesByDialogs.put(dialog_id, arrayList);
                        sortedDialogs.add(0, Long.valueOf(dialog_id));
                    }
                    arrayList.add(messageObject);
                }
            }
            LongSparseArray<Integer> oldIdsWear = this.wearNotificationsIds.clone();
            this.wearNotificationsIds.clear();
            for (int b = 0; b < sortedDialogs.size(); b++) {
                dialog_id = ((Long) sortedDialogs.get(b)).longValue();
                ArrayList<MessageObject> messageObjects = (ArrayList) messagesByDialogs.get(dialog_id);
                int max_id = ((MessageObject) messageObjects.get(0)).getId();
                int max_date = ((MessageObject) messageObjects.get(0)).messageOwner.date;
                Chat chat = null;
                User user = null;
                TLObject photoPath;
                String chatName;
                String name;
                Integer notificationId;
                UnreadConversation.Builder unreadConvBuilder;
                Intent intent;
                Action wearReplyAction;
                PendingIntent replyPendingIntent;
                RemoteInput remoteInputWear;
                String replyToString;
                Integer count;
                Style messagingStyle;
                StringBuilder text;
                boolean[] isText;
                String message;
                PendingIntent contentIntent;
                WearableExtender wearableExtender;
                String dismissalID;
                WearableExtender summaryExtender;
                long date;
                Builder builder;
                BitmapDrawable img;
                File file;
                float scaleFactor;
                Options options;
                int i;
                Bitmap bitmap;
                if (dialog_id > 0) {
                    user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf((int) dialog_id));
                    if (user == null) {
                    }
                    photoPath = null;
                    if (chat == null) {
                        chatName = chat.title;
                    } else {
                        chatName = UserObject.getUserName(user);
                    }
                    if (!AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter) {
                        name = LocaleController.getString("AppName", R.string.AppName);
                    } else {
                        name = chatName;
                        if (chat != null) {
                            if (!(chat.photo == null || chat.photo.photo_small == null || chat.photo.photo_small.volume_id == 0 || chat.photo.photo_small.local_id == 0)) {
                                photoPath = chat.photo.photo_small;
                            }
                        } else if (!(user.photo == null || user.photo.photo_small == null || user.photo.photo_small.volume_id == 0 || user.photo.photo_small.local_id == 0)) {
                            photoPath = user.photo.photo_small;
                        }
                    }
                    notificationId = (Integer) oldIdsWear.get(dialog_id);
                    if (notificationId != null) {
                        notificationId = Integer.valueOf((int) dialog_id);
                    } else {
                        oldIdsWear.remove(dialog_id);
                    }
                    unreadConvBuilder = new UnreadConversation.Builder(name).setLatestTimestamp(((long) max_date) * 1000);
                    intent = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent.addFlags(32);
                    intent.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent.putExtra("dialog_id", dialog_id);
                    intent.putExtra("max_id", max_id);
                    intent.putExtra("currentAccount", this.currentAccount);
                    unreadConvBuilder.setReadPendingIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, notificationId.intValue(), intent, 134217728));
                    wearReplyAction = null;
                    if (!((ChatObject.isChannel(chat) && (chat == null || !chat.megagroup)) || AndroidUtilities.needShowPasscode(false) || SharedConfig.isWaitingForPasscodeEnter)) {
                        intent = new Intent(ApplicationLoader.applicationContext, AutoMessageReplyReceiver.class);
                        intent.addFlags(32);
                        intent.setAction("org.telegram.messenger.ACTION_MESSAGE_REPLY");
                        intent.putExtra("dialog_id", dialog_id);
                        intent.putExtra("max_id", max_id);
                        intent.putExtra("currentAccount", this.currentAccount);
                        unreadConvBuilder.setReplyAction(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, notificationId.intValue(), intent, 134217728), new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build());
                        intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                        intent.putExtra("dialog_id", dialog_id);
                        intent.putExtra("max_id", max_id);
                        intent.putExtra("currentAccount", this.currentAccount);
                        replyPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, notificationId.intValue(), intent, 134217728);
                        remoteInputWear = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                        if (chat == null) {
                            replyToString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, name);
                        } else {
                            replyToString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, name);
                        }
                        wearReplyAction = new Action.Builder(R.drawable.ic_reply_icon, replyToString, replyPendingIntent).setAllowGeneratedReplies(true).addRemoteInput(remoteInputWear).build();
                    }
                    count = (Integer) this.pushDialogs.get(dialog_id);
                    if (count == null) {
                        count = Integer.valueOf(0);
                    }
                    messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID).setConversationTitle(String.format("%1$s (%2$s)", new Object[]{name, LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()))}));
                    text = new StringBuilder();
                    isText = new boolean[1];
                    for (a = messageObjects.size() - 1; a >= 0; a--) {
                        messageObject = (MessageObject) messageObjects.get(a);
                        message = getStringForMessage(messageObject, false, isText);
                        if (message != null) {
                            if (chat != null) {
                                message = message.replace(" @ " + name, TtmlNode.ANONYMOUS_REGION_ID);
                            } else if (isText[0]) {
                                message = message.replace(name + " ", TtmlNode.ANONYMOUS_REGION_ID);
                            } else {
                                message = message.replace(name + ": ", TtmlNode.ANONYMOUS_REGION_ID);
                            }
                            if (text.length() > 0) {
                                text.append("\n\n");
                            }
                            text.append(message);
                            unreadConvBuilder.addMessage(message);
                            messagingStyle.addMessage(message, ((long) messageObject.messageOwner.date) * 1000, null);
                        }
                    }
                    intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent.setFlags(32768);
                    if (chat != null) {
                        intent.putExtra("chatId", chat.id);
                    } else if (user != null) {
                        intent.putExtra("userId", user.id);
                    }
                    intent.putExtra("currentAccount", this.currentAccount);
                    contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824);
                    wearableExtender = new WearableExtender();
                    if (wearReplyAction != null) {
                        wearableExtender.addAction(wearReplyAction);
                    }
                    dismissalID = null;
                    if (chat != null) {
                        dismissalID = "tgchat" + chat.id + "_" + max_id;
                    } else if (user != null) {
                        dismissalID = "tguser" + user.id + "_" + max_id;
                    }
                    wearableExtender.setDismissalId(dismissalID);
                    summaryExtender = new WearableExtender();
                    summaryExtender.setDismissalId("summary_" + dismissalID);
                    notificationBuilder.extend(summaryExtender);
                    date = ((long) ((MessageObject) messageObjects.get(0)).messageOwner.date) * 1000;
                    builder = new Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(this.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setStyle(messagingStyle).setContentIntent(contentIntent).extend(wearableExtender).setSortKey(TtmlNode.ANONYMOUS_REGION_ID + (Long.MAX_VALUE - date)).extend(new CarExtender().setUnreadConversation(unreadConvBuilder.build())).setCategory("msg");
                    if (this.pushDialogs.size() == 1) {
                        builder.setSubText(summary);
                    }
                    if (photoPath != null) {
                        img = ImageLoader.getInstance().getImageFromMemory(photoPath, null, "50_50");
                        if (img == null) {
                            builder.setLargeIcon(img.getBitmap());
                        } else {
                            try {
                                file = FileLoader.getPathToAttach(photoPath, true);
                                if (file.exists()) {
                                    scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                    options = new Options();
                                    if (scaleFactor >= 1.0f) {
                                        i = 1;
                                    } else {
                                        i = (int) scaleFactor;
                                    }
                                    options.inSampleSize = i;
                                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                                    if (bitmap != null) {
                                        builder.setLargeIcon(bitmap);
                                    }
                                }
                            } catch (Throwable th) {
                            }
                        }
                    }
                    if (chat == null && user != null && user.phone != null && user.phone.length() > 0) {
                        builder.addPerson("tel:+" + user.phone);
                    }
                    if (VERSION.SDK_INT >= 26) {
                        builder.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                    }
                    notificationManager.notify(notificationId.intValue(), builder.build());
                    this.wearNotificationsIds.put(dialog_id, notificationId);
                } else {
                    chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-((int) dialog_id)));
                    if (chat == null) {
                    }
                    photoPath = null;
                    if (chat == null) {
                        chatName = UserObject.getUserName(user);
                    } else {
                        chatName = chat.title;
                    }
                    if (AndroidUtilities.needShowPasscode(false)) {
                    }
                    name = LocaleController.getString("AppName", R.string.AppName);
                    notificationId = (Integer) oldIdsWear.get(dialog_id);
                    if (notificationId != null) {
                        oldIdsWear.remove(dialog_id);
                    } else {
                        notificationId = Integer.valueOf((int) dialog_id);
                    }
                    unreadConvBuilder = new UnreadConversation.Builder(name).setLatestTimestamp(((long) max_date) * 1000);
                    intent = new Intent(ApplicationLoader.applicationContext, AutoMessageHeardReceiver.class);
                    intent.addFlags(32);
                    intent.setAction("org.telegram.messenger.ACTION_MESSAGE_HEARD");
                    intent.putExtra("dialog_id", dialog_id);
                    intent.putExtra("max_id", max_id);
                    intent.putExtra("currentAccount", this.currentAccount);
                    unreadConvBuilder.setReadPendingIntent(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, notificationId.intValue(), intent, 134217728));
                    wearReplyAction = null;
                    intent = new Intent(ApplicationLoader.applicationContext, AutoMessageReplyReceiver.class);
                    intent.addFlags(32);
                    intent.setAction("org.telegram.messenger.ACTION_MESSAGE_REPLY");
                    intent.putExtra("dialog_id", dialog_id);
                    intent.putExtra("max_id", max_id);
                    intent.putExtra("currentAccount", this.currentAccount);
                    unreadConvBuilder.setReplyAction(PendingIntent.getBroadcast(ApplicationLoader.applicationContext, notificationId.intValue(), intent, 134217728), new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build());
                    intent = new Intent(ApplicationLoader.applicationContext, WearReplyReceiver.class);
                    intent.putExtra("dialog_id", dialog_id);
                    intent.putExtra("max_id", max_id);
                    intent.putExtra("currentAccount", this.currentAccount);
                    replyPendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, notificationId.intValue(), intent, 134217728);
                    remoteInputWear = new RemoteInput.Builder(EXTRA_VOICE_REPLY).setLabel(LocaleController.getString("Reply", R.string.Reply)).build();
                    if (chat == null) {
                        replyToString = LocaleController.formatString("ReplyToUser", R.string.ReplyToUser, name);
                    } else {
                        replyToString = LocaleController.formatString("ReplyToGroup", R.string.ReplyToGroup, name);
                    }
                    wearReplyAction = new Action.Builder(R.drawable.ic_reply_icon, replyToString, replyPendingIntent).setAllowGeneratedReplies(true).addRemoteInput(remoteInputWear).build();
                    count = (Integer) this.pushDialogs.get(dialog_id);
                    if (count == null) {
                        count = Integer.valueOf(0);
                    }
                    messagingStyle = new MessagingStyle(TtmlNode.ANONYMOUS_REGION_ID).setConversationTitle(String.format("%1$s (%2$s)", new Object[]{name, LocaleController.formatPluralString("NewMessages", Math.max(count.intValue(), messageObjects.size()))}));
                    text = new StringBuilder();
                    isText = new boolean[1];
                    for (a = messageObjects.size() - 1; a >= 0; a--) {
                        messageObject = (MessageObject) messageObjects.get(a);
                        message = getStringForMessage(messageObject, false, isText);
                        if (message != null) {
                            if (chat != null) {
                                message = message.replace(" @ " + name, TtmlNode.ANONYMOUS_REGION_ID);
                            } else if (isText[0]) {
                                message = message.replace(name + " ", TtmlNode.ANONYMOUS_REGION_ID);
                            } else {
                                message = message.replace(name + ": ", TtmlNode.ANONYMOUS_REGION_ID);
                            }
                            if (text.length() > 0) {
                                text.append("\n\n");
                            }
                            text.append(message);
                            unreadConvBuilder.addMessage(message);
                            messagingStyle.addMessage(message, ((long) messageObject.messageOwner.date) * 1000, null);
                        }
                    }
                    intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
                    intent.setAction("com.tmessages.openchat" + Math.random() + ConnectionsManager.DEFAULT_DATACENTER_ID);
                    intent.setFlags(32768);
                    if (chat != null) {
                        intent.putExtra("chatId", chat.id);
                    } else if (user != null) {
                        intent.putExtra("userId", user.id);
                    }
                    intent.putExtra("currentAccount", this.currentAccount);
                    contentIntent = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 1073741824);
                    wearableExtender = new WearableExtender();
                    if (wearReplyAction != null) {
                        wearableExtender.addAction(wearReplyAction);
                    }
                    dismissalID = null;
                    if (chat != null) {
                        dismissalID = "tgchat" + chat.id + "_" + max_id;
                    } else if (user != null) {
                        dismissalID = "tguser" + user.id + "_" + max_id;
                    }
                    wearableExtender.setDismissalId(dismissalID);
                    summaryExtender = new WearableExtender();
                    summaryExtender.setDismissalId("summary_" + dismissalID);
                    notificationBuilder.extend(summaryExtender);
                    date = ((long) ((MessageObject) messageObjects.get(0)).messageOwner.date) * 1000;
                    builder = new Builder(ApplicationLoader.applicationContext).setContentTitle(name).setSmallIcon(R.drawable.notification).setGroup(this.notificationGroup).setContentText(text.toString()).setAutoCancel(true).setNumber(messageObjects.size()).setColor(-13851168).setGroupSummary(false).setWhen(date).setStyle(messagingStyle).setContentIntent(contentIntent).extend(wearableExtender).setSortKey(TtmlNode.ANONYMOUS_REGION_ID + (Long.MAX_VALUE - date)).extend(new CarExtender().setUnreadConversation(unreadConvBuilder.build())).setCategory("msg");
                    if (this.pushDialogs.size() == 1) {
                        builder.setSubText(summary);
                    }
                    if (photoPath != null) {
                        img = ImageLoader.getInstance().getImageFromMemory(photoPath, null, "50_50");
                        if (img == null) {
                            file = FileLoader.getPathToAttach(photoPath, true);
                            if (file.exists()) {
                                scaleFactor = 160.0f / ((float) AndroidUtilities.dp(50.0f));
                                options = new Options();
                                if (scaleFactor >= 1.0f) {
                                    i = (int) scaleFactor;
                                } else {
                                    i = 1;
                                }
                                options.inSampleSize = i;
                                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
                                if (bitmap != null) {
                                    builder.setLargeIcon(bitmap);
                                }
                            }
                        } else {
                            builder.setLargeIcon(img.getBitmap());
                        }
                    }
                    builder.addPerson("tel:+" + user.phone);
                    if (VERSION.SDK_INT >= 26) {
                        builder.setChannelId(OTHER_NOTIFICATIONS_CHANNEL);
                    }
                    notificationManager.notify(notificationId.intValue(), builder.build());
                    this.wearNotificationsIds.put(dialog_id, notificationId);
                }
            }
            for (a = 0; a < oldIdsWear.size(); a++) {
                notificationManager.cancel(((Integer) oldIdsWear.valueAt(a)).intValue());
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
