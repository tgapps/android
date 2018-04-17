package org.telegram.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.widget.Toast;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.DefaultLoadControl;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;
import org.telegram.messenger.exoplayer2.trackselection.AdaptiveTrackSelection;
import org.telegram.messenger.support.SparseLongArray;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInfo;
import org.telegram.tgnet.TLRPC.ChannelParticipant;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ChatParticipant;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DraftMessage;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.EncryptedMessage;
import org.telegram.tgnet.TLRPC.ExportedChatInvite;
import org.telegram.tgnet.TLRPC.InputChannel;
import org.telegram.tgnet.TLRPC.InputFile;
import org.telegram.tgnet.TLRPC.InputPeer;
import org.telegram.tgnet.TLRPC.InputPhoto;
import org.telegram.tgnet.TLRPC.InputUser;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageEntity;
import org.telegram.tgnet.TLRPC.Peer;
import org.telegram.tgnet.TLRPC.PeerNotifySettings;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.RecentMeUrl;
import org.telegram.tgnet.TLRPC.SendMessageAction;
import org.telegram.tgnet.TLRPC.TL_account_registerDevice;
import org.telegram.tgnet.TLRPC.TL_account_unregisterDevice;
import org.telegram.tgnet.TLRPC.TL_account_updateStatus;
import org.telegram.tgnet.TLRPC.TL_auth_logOut;
import org.telegram.tgnet.TLRPC.TL_boolTrue;
import org.telegram.tgnet.TLRPC.TL_botInfo;
import org.telegram.tgnet.TLRPC.TL_channel;
import org.telegram.tgnet.TLRPC.TL_channelAdminRights;
import org.telegram.tgnet.TLRPC.TL_channelBannedRights;
import org.telegram.tgnet.TLRPC.TL_channelForbidden;
import org.telegram.tgnet.TLRPC.TL_channelMessagesFilterEmpty;
import org.telegram.tgnet.TLRPC.TL_channelParticipantSelf;
import org.telegram.tgnet.TLRPC.TL_channelParticipantsAdmins;
import org.telegram.tgnet.TLRPC.TL_channelParticipantsRecent;
import org.telegram.tgnet.TLRPC.TL_channels_channelParticipant;
import org.telegram.tgnet.TLRPC.TL_channels_channelParticipants;
import org.telegram.tgnet.TLRPC.TL_channels_createChannel;
import org.telegram.tgnet.TLRPC.TL_channels_deleteChannel;
import org.telegram.tgnet.TLRPC.TL_channels_deleteHistory;
import org.telegram.tgnet.TLRPC.TL_channels_deleteMessages;
import org.telegram.tgnet.TLRPC.TL_channels_deleteUserHistory;
import org.telegram.tgnet.TLRPC.TL_channels_editAbout;
import org.telegram.tgnet.TLRPC.TL_channels_editAdmin;
import org.telegram.tgnet.TLRPC.TL_channels_editBanned;
import org.telegram.tgnet.TLRPC.TL_channels_editTitle;
import org.telegram.tgnet.TLRPC.TL_channels_getMessages;
import org.telegram.tgnet.TLRPC.TL_channels_getParticipant;
import org.telegram.tgnet.TLRPC.TL_channels_getParticipants;
import org.telegram.tgnet.TLRPC.TL_channels_inviteToChannel;
import org.telegram.tgnet.TLRPC.TL_channels_joinChannel;
import org.telegram.tgnet.TLRPC.TL_channels_leaveChannel;
import org.telegram.tgnet.TLRPC.TL_channels_readHistory;
import org.telegram.tgnet.TLRPC.TL_channels_readMessageContents;
import org.telegram.tgnet.TLRPC.TL_channels_toggleInvites;
import org.telegram.tgnet.TLRPC.TL_channels_togglePreHistoryHidden;
import org.telegram.tgnet.TLRPC.TL_channels_toggleSignatures;
import org.telegram.tgnet.TLRPC.TL_channels_updatePinnedMessage;
import org.telegram.tgnet.TLRPC.TL_channels_updateUsername;
import org.telegram.tgnet.TLRPC.TL_chat;
import org.telegram.tgnet.TLRPC.TL_chatFull;
import org.telegram.tgnet.TLRPC.TL_chatInviteEmpty;
import org.telegram.tgnet.TLRPC.TL_chatParticipant;
import org.telegram.tgnet.TLRPC.TL_chatParticipants;
import org.telegram.tgnet.TLRPC.TL_chatPhotoEmpty;
import org.telegram.tgnet.TLRPC.TL_config;
import org.telegram.tgnet.TLRPC.TL_contactBlocked;
import org.telegram.tgnet.TLRPC.TL_contacts_block;
import org.telegram.tgnet.TLRPC.TL_contacts_getBlocked;
import org.telegram.tgnet.TLRPC.TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_contacts_unblock;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_documentEmpty;
import org.telegram.tgnet.TLRPC.TL_draftMessage;
import org.telegram.tgnet.TLRPC.TL_encryptedChat;
import org.telegram.tgnet.TLRPC.TL_encryptedChatRequested;
import org.telegram.tgnet.TLRPC.TL_encryptedChatWaiting;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_help_getAppChangelog;
import org.telegram.tgnet.TLRPC.TL_help_getRecentMeUrls;
import org.telegram.tgnet.TLRPC.TL_help_recentMeUrls;
import org.telegram.tgnet.TLRPC.TL_inputChannel;
import org.telegram.tgnet.TLRPC.TL_inputChannelEmpty;
import org.telegram.tgnet.TLRPC.TL_inputDialogPeer;
import org.telegram.tgnet.TLRPC.TL_inputDocument;
import org.telegram.tgnet.TLRPC.TL_inputEncryptedChat;
import org.telegram.tgnet.TLRPC.TL_inputMessagesFilterChatPhotos;
import org.telegram.tgnet.TLRPC.TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC.TL_inputPeerChat;
import org.telegram.tgnet.TLRPC.TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC.TL_inputPeerUser;
import org.telegram.tgnet.TLRPC.TL_inputPhotoEmpty;
import org.telegram.tgnet.TLRPC.TL_inputUser;
import org.telegram.tgnet.TLRPC.TL_inputUserEmpty;
import org.telegram.tgnet.TLRPC.TL_inputUserSelf;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageActionChannelCreate;
import org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser;
import org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
import org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo;
import org.telegram.tgnet.TLRPC.TL_messageActionCreatedBroadcastList;
import org.telegram.tgnet.TLRPC.TL_messageActionHistoryClear;
import org.telegram.tgnet.TLRPC.TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_messageMediaUnsupported;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_messageService;
import org.telegram.tgnet.TLRPC.TL_messages_addChatUser;
import org.telegram.tgnet.TLRPC.TL_messages_affectedHistory;
import org.telegram.tgnet.TLRPC.TL_messages_affectedMessages;
import org.telegram.tgnet.TLRPC.TL_messages_channelMessages;
import org.telegram.tgnet.TLRPC.TL_messages_chatFull;
import org.telegram.tgnet.TLRPC.TL_messages_createChat;
import org.telegram.tgnet.TLRPC.TL_messages_deleteChatUser;
import org.telegram.tgnet.TLRPC.TL_messages_deleteHistory;
import org.telegram.tgnet.TLRPC.TL_messages_deleteMessages;
import org.telegram.tgnet.TLRPC.TL_messages_dialogs;
import org.telegram.tgnet.TLRPC.TL_messages_editChatAdmin;
import org.telegram.tgnet.TLRPC.TL_messages_editChatTitle;
import org.telegram.tgnet.TLRPC.TL_messages_getDialogs;
import org.telegram.tgnet.TLRPC.TL_messages_getHistory;
import org.telegram.tgnet.TLRPC.TL_messages_getMessages;
import org.telegram.tgnet.TLRPC.TL_messages_getMessagesViews;
import org.telegram.tgnet.TLRPC.TL_messages_getPeerDialogs;
import org.telegram.tgnet.TLRPC.TL_messages_getPeerSettings;
import org.telegram.tgnet.TLRPC.TL_messages_getPinnedDialogs;
import org.telegram.tgnet.TLRPC.TL_messages_getUnreadMentions;
import org.telegram.tgnet.TLRPC.TL_messages_getWebPagePreview;
import org.telegram.tgnet.TLRPC.TL_messages_hideReportSpam;
import org.telegram.tgnet.TLRPC.TL_messages_messages;
import org.telegram.tgnet.TLRPC.TL_messages_migrateChat;
import org.telegram.tgnet.TLRPC.TL_messages_peerDialogs;
import org.telegram.tgnet.TLRPC.TL_messages_readEncryptedHistory;
import org.telegram.tgnet.TLRPC.TL_messages_readHistory;
import org.telegram.tgnet.TLRPC.TL_messages_readMessageContents;
import org.telegram.tgnet.TLRPC.TL_messages_receivedQueue;
import org.telegram.tgnet.TLRPC.TL_messages_reportEncryptedSpam;
import org.telegram.tgnet.TLRPC.TL_messages_reportSpam;
import org.telegram.tgnet.TLRPC.TL_messages_saveGif;
import org.telegram.tgnet.TLRPC.TL_messages_saveRecentSticker;
import org.telegram.tgnet.TLRPC.TL_messages_search;
import org.telegram.tgnet.TLRPC.TL_messages_setEncryptedTyping;
import org.telegram.tgnet.TLRPC.TL_messages_setTyping;
import org.telegram.tgnet.TLRPC.TL_messages_startBot;
import org.telegram.tgnet.TLRPC.TL_messages_toggleChatAdmins;
import org.telegram.tgnet.TLRPC.TL_messages_toggleDialogPin;
import org.telegram.tgnet.TLRPC.TL_peerChannel;
import org.telegram.tgnet.TLRPC.TL_peerChat;
import org.telegram.tgnet.TLRPC.TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC.TL_peerNotifySettingsEmpty;
import org.telegram.tgnet.TLRPC.TL_peerSettings;
import org.telegram.tgnet.TLRPC.TL_peerUser;
import org.telegram.tgnet.TLRPC.TL_photoEmpty;
import org.telegram.tgnet.TLRPC.TL_photos_deletePhotos;
import org.telegram.tgnet.TLRPC.TL_photos_getUserPhotos;
import org.telegram.tgnet.TLRPC.TL_photos_photo;
import org.telegram.tgnet.TLRPC.TL_photos_photos;
import org.telegram.tgnet.TLRPC.TL_photos_updateProfilePhoto;
import org.telegram.tgnet.TLRPC.TL_photos_uploadProfilePhoto;
import org.telegram.tgnet.TLRPC.TL_replyKeyboardHide;
import org.telegram.tgnet.TLRPC.TL_sendMessageCancelAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageGamePlayAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageRecordAudioAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageRecordRoundAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageTypingAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageUploadAudioAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageUploadDocumentAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageUploadPhotoAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageUploadRoundAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageUploadVideoAction;
import org.telegram.tgnet.TLRPC.TL_updateChannel;
import org.telegram.tgnet.TLRPC.TL_updateChannelAvailableMessages;
import org.telegram.tgnet.TLRPC.TL_updateChannelMessageViews;
import org.telegram.tgnet.TLRPC.TL_updateChannelPinnedMessage;
import org.telegram.tgnet.TLRPC.TL_updateChannelReadMessagesContents;
import org.telegram.tgnet.TLRPC.TL_updateChannelTooLong;
import org.telegram.tgnet.TLRPC.TL_updateChannelWebPage;
import org.telegram.tgnet.TLRPC.TL_updateDeleteChannelMessages;
import org.telegram.tgnet.TLRPC.TL_updateDeleteMessages;
import org.telegram.tgnet.TLRPC.TL_updateEditChannelMessage;
import org.telegram.tgnet.TLRPC.TL_updateEditMessage;
import org.telegram.tgnet.TLRPC.TL_updateMessageID;
import org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage;
import org.telegram.tgnet.TLRPC.TL_updateNewEncryptedMessage;
import org.telegram.tgnet.TLRPC.TL_updateNewMessage;
import org.telegram.tgnet.TLRPC.TL_updateReadChannelInbox;
import org.telegram.tgnet.TLRPC.TL_updateReadChannelOutbox;
import org.telegram.tgnet.TLRPC.TL_updateReadHistoryInbox;
import org.telegram.tgnet.TLRPC.TL_updateReadHistoryOutbox;
import org.telegram.tgnet.TLRPC.TL_updateReadMessagesContents;
import org.telegram.tgnet.TLRPC.TL_updateServiceNotification;
import org.telegram.tgnet.TLRPC.TL_updateShort;
import org.telegram.tgnet.TLRPC.TL_updateShortChatMessage;
import org.telegram.tgnet.TLRPC.TL_updateShortMessage;
import org.telegram.tgnet.TLRPC.TL_updateUserBlocked;
import org.telegram.tgnet.TLRPC.TL_updateWebPage;
import org.telegram.tgnet.TLRPC.TL_updates;
import org.telegram.tgnet.TLRPC.TL_updatesCombined;
import org.telegram.tgnet.TLRPC.TL_updatesTooLong;
import org.telegram.tgnet.TLRPC.TL_updates_channelDifference;
import org.telegram.tgnet.TLRPC.TL_updates_channelDifferenceEmpty;
import org.telegram.tgnet.TLRPC.TL_updates_channelDifferenceTooLong;
import org.telegram.tgnet.TLRPC.TL_updates_difference;
import org.telegram.tgnet.TLRPC.TL_updates_differenceEmpty;
import org.telegram.tgnet.TLRPC.TL_updates_differenceSlice;
import org.telegram.tgnet.TLRPC.TL_updates_differenceTooLong;
import org.telegram.tgnet.TLRPC.TL_updates_getChannelDifference;
import org.telegram.tgnet.TLRPC.TL_updates_getDifference;
import org.telegram.tgnet.TLRPC.TL_updates_getState;
import org.telegram.tgnet.TLRPC.TL_updates_state;
import org.telegram.tgnet.TLRPC.TL_userForeign_old2;
import org.telegram.tgnet.TLRPC.TL_userFull;
import org.telegram.tgnet.TLRPC.TL_userProfilePhoto;
import org.telegram.tgnet.TLRPC.TL_userProfilePhotoEmpty;
import org.telegram.tgnet.TLRPC.TL_users_getFullUser;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.TL_webPageEmpty;
import org.telegram.tgnet.TLRPC.TL_webPagePending;
import org.telegram.tgnet.TLRPC.TL_webPageUrlPending;
import org.telegram.tgnet.TLRPC.Update;
import org.telegram.tgnet.TLRPC.Updates;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.UserProfilePhoto;
import org.telegram.tgnet.TLRPC.Vector;
import org.telegram.tgnet.TLRPC.contacts_Blocked;
import org.telegram.tgnet.TLRPC.messages_Dialogs;
import org.telegram.tgnet.TLRPC.messages_Messages;
import org.telegram.tgnet.TLRPC.photos_Photos;
import org.telegram.tgnet.TLRPC.updates_ChannelDifference;
import org.telegram.tgnet.TLRPC.updates_Difference;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.ProfileActivity;

public class MessagesController implements NotificationCenterDelegate {
    private static volatile MessagesController[] Instance = new MessagesController[3];
    public static final int UPDATE_MASK_ALL = 1535;
    public static final int UPDATE_MASK_AVATAR = 2;
    public static final int UPDATE_MASK_CHANNEL = 8192;
    public static final int UPDATE_MASK_CHAT_ADMINS = 16384;
    public static final int UPDATE_MASK_CHAT_AVATAR = 8;
    public static final int UPDATE_MASK_CHAT_MEMBERS = 32;
    public static final int UPDATE_MASK_CHAT_NAME = 16;
    public static final int UPDATE_MASK_MESSAGE_TEXT = 32768;
    public static final int UPDATE_MASK_NAME = 1;
    public static final int UPDATE_MASK_NEW_MESSAGE = 2048;
    public static final int UPDATE_MASK_PHONE = 1024;
    public static final int UPDATE_MASK_READ_DIALOG_MESSAGE = 256;
    public static final int UPDATE_MASK_SELECT_DIALOG = 512;
    public static final int UPDATE_MASK_SEND_STATE = 4096;
    public static final int UPDATE_MASK_STATUS = 4;
    public static final int UPDATE_MASK_USER_PHONE = 128;
    public static final int UPDATE_MASK_USER_PRINT = 64;
    private static volatile long lastThemeCheckTime;
    public ArrayList<Integer> blockedUsers = new ArrayList();
    public int callConnectTimeout = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS;
    public int callPacketTimeout = 10000;
    public int callReceiveTimeout = 20000;
    public int callRingTimeout = 90000;
    public boolean canRevokePmInbox;
    private SparseArray<ArrayList<Integer>> channelAdmins = new SparseArray();
    private SparseArray<ArrayList<Integer>> channelViewsToSend = new SparseArray();
    private SparseIntArray channelsPts = new SparseIntArray();
    private ConcurrentHashMap<Integer, Chat> chats = new ConcurrentHashMap(100, 1.0f, 2);
    private SparseBooleanArray checkingLastMessagesDialogs = new SparseBooleanArray();
    private ArrayList<Long> createdDialogIds = new ArrayList();
    private ArrayList<Long> createdDialogMainThreadIds = new ArrayList();
    private int currentAccount;
    private Runnable currentDeleteTaskRunnable;
    private int currentDeletingTaskChannelId;
    private ArrayList<Integer> currentDeletingTaskMids;
    private int currentDeletingTaskTime;
    public boolean defaultP2pContacts = false;
    private final Comparator<TL_dialog> dialogComparator = new Comparator<TL_dialog>() {
        public int compare(TL_dialog dialog1, TL_dialog dialog2) {
            if (!dialog1.pinned && dialog2.pinned) {
                return 1;
            }
            if (dialog1.pinned && !dialog2.pinned) {
                return -1;
            }
            if (!dialog1.pinned || !dialog2.pinned) {
                DraftMessage draftMessage = DataQuery.getInstance(MessagesController.this.currentAccount).getDraft(dialog1.id);
                int date1 = (draftMessage == null || draftMessage.date < dialog1.last_message_date) ? dialog1.last_message_date : draftMessage.date;
                draftMessage = DataQuery.getInstance(MessagesController.this.currentAccount).getDraft(dialog2.id);
                int date2 = (draftMessage == null || draftMessage.date < dialog2.last_message_date) ? dialog2.last_message_date : draftMessage.date;
                if (date1 < date2) {
                    return 1;
                }
                if (date1 > date2) {
                    return -1;
                }
                return 0;
            } else if (dialog1.pinnedNum < dialog2.pinnedNum) {
                return 1;
            } else {
                if (dialog1.pinnedNum > dialog2.pinnedNum) {
                    return -1;
                }
                return 0;
            }
        }
    };
    public LongSparseArray<MessageObject> dialogMessage = new LongSparseArray();
    public SparseArray<MessageObject> dialogMessagesByIds = new SparseArray();
    public LongSparseArray<MessageObject> dialogMessagesByRandomIds = new LongSparseArray();
    public ArrayList<TL_dialog> dialogs = new ArrayList();
    public boolean dialogsEndReached;
    public ArrayList<TL_dialog> dialogsForward = new ArrayList();
    public ArrayList<TL_dialog> dialogsGroupsOnly = new ArrayList();
    public ArrayList<TL_dialog> dialogsServerOnly = new ArrayList();
    public LongSparseArray<TL_dialog> dialogs_dict = new LongSparseArray();
    public ConcurrentHashMap<Long, Integer> dialogs_read_inbox_max = new ConcurrentHashMap(100, 1.0f, 2);
    public ConcurrentHashMap<Long, Integer> dialogs_read_outbox_max = new ConcurrentHashMap(100, 1.0f, 2);
    private SharedPreferences emojiPreferences;
    public boolean enableJoined = true;
    private ConcurrentHashMap<Integer, EncryptedChat> encryptedChats = new ConcurrentHashMap(10, 1.0f, 2);
    private SparseArray<ExportedChatInvite> exportedChats = new SparseArray();
    public boolean firstGettingTask;
    private SparseArray<TL_userFull> fullUsers = new SparseArray();
    private boolean getDifferenceFirstSync = true;
    public boolean gettingDifference;
    private SparseBooleanArray gettingDifferenceChannels = new SparseBooleanArray();
    private boolean gettingNewDeleteTask;
    private SparseBooleanArray gettingUnknownChannels = new SparseBooleanArray();
    public ArrayList<RecentMeUrl> hintDialogs = new ArrayList();
    private String installReferer;
    private ArrayList<Integer> joiningToChannels = new ArrayList();
    private int lastPrintingStringCount;
    private long lastPushRegisterSendTime;
    private long lastStatusUpdateTime;
    private long lastViewsCheckTime;
    public String linkPrefix = "t.me";
    private ArrayList<Integer> loadedFullChats = new ArrayList();
    private ArrayList<Integer> loadedFullParticipants = new ArrayList();
    private ArrayList<Integer> loadedFullUsers = new ArrayList();
    public boolean loadingBlockedUsers = false;
    private SparseIntArray loadingChannelAdmins = new SparseIntArray();
    public boolean loadingDialogs;
    private ArrayList<Integer> loadingFullChats = new ArrayList();
    private ArrayList<Integer> loadingFullParticipants = new ArrayList();
    private ArrayList<Integer> loadingFullUsers = new ArrayList();
    private LongSparseArray<Boolean> loadingPeerSettings = new LongSparseArray();
    private SharedPreferences mainPreferences;
    public int maxBroadcastCount = 100;
    public int maxEditTime = 172800;
    public int maxFaveStickersCount = 5;
    public int maxGroupCount = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
    public int maxMegagroupCount = 10000;
    public int maxPinnedDialogsCount = 5;
    public int maxRecentGifsCount = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
    public int maxRecentStickersCount = 30;
    private boolean migratingDialogs;
    public int minGroupConvertSize = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
    private SparseIntArray needShortPollChannels = new SparseIntArray();
    public int nextDialogsCacheOffset;
    private SharedPreferences notificationsPreferences;
    private ConcurrentHashMap<String, TLObject> objectsByUsernames = new ConcurrentHashMap(100, 1.0f, 2);
    private boolean offlineSent;
    public ConcurrentHashMap<Integer, Integer> onlinePrivacy = new ConcurrentHashMap(20, 1.0f, 2);
    public boolean preloadFeaturedStickers;
    public LongSparseArray<CharSequence> printingStrings = new LongSparseArray();
    public LongSparseArray<Integer> printingStringsTypes = new LongSparseArray();
    public ConcurrentHashMap<Long, ArrayList<PrintingUser>> printingUsers = new ConcurrentHashMap(20, 1.0f, 2);
    public int ratingDecay;
    private ArrayList<ReadTask> readTasks = new ArrayList();
    private LongSparseArray<ReadTask> readTasksMap = new LongSparseArray();
    public boolean registeringForPush;
    private LongSparseArray<ArrayList<Integer>> reloadingMessages = new LongSparseArray();
    private HashMap<String, ArrayList<MessageObject>> reloadingWebpages = new HashMap();
    private LongSparseArray<ArrayList<MessageObject>> reloadingWebpagesPending = new LongSparseArray();
    private messages_Dialogs resetDialogsAll;
    private TL_messages_peerDialogs resetDialogsPinned;
    private boolean resetingDialogs;
    public int revokeTimeLimit = 172800;
    public int revokeTimePmLimit = 172800;
    public int secretWebpagePreview = 2;
    public SparseArray<LongSparseArray<Boolean>> sendingTypings = new SparseArray();
    public boolean serverDialogsEndReached;
    private SparseIntArray shortPollChannels = new SparseIntArray();
    private int statusRequest;
    private int statusSettingState;
    private Runnable themeCheckRunnable = new Runnable() {
        public void run() {
            Theme.checkAutoNightThemeConditions();
        }
    };
    private final Comparator<Update> updatesComparator = new Comparator<Update>() {
        public int compare(Update lhs, Update rhs) {
            int ltype = MessagesController.this.getUpdateType(lhs);
            int rtype = MessagesController.this.getUpdateType(rhs);
            if (ltype != rtype) {
                return AndroidUtilities.compare(ltype, rtype);
            }
            if (ltype == 0) {
                return AndroidUtilities.compare(MessagesController.getUpdatePts(lhs), MessagesController.getUpdatePts(rhs));
            }
            if (ltype == 1) {
                return AndroidUtilities.compare(MessagesController.getUpdateQts(lhs), MessagesController.getUpdateQts(rhs));
            }
            if (ltype != 2) {
                return 0;
            }
            int lChannel = MessagesController.getUpdateChannelId(lhs);
            int rChannel = MessagesController.getUpdateChannelId(rhs);
            if (lChannel == rChannel) {
                return AndroidUtilities.compare(MessagesController.getUpdatePts(lhs), MessagesController.getUpdatePts(rhs));
            }
            return AndroidUtilities.compare(lChannel, rChannel);
        }
    };
    private SparseArray<ArrayList<Updates>> updatesQueueChannels = new SparseArray();
    private ArrayList<Updates> updatesQueuePts = new ArrayList();
    private ArrayList<Updates> updatesQueueQts = new ArrayList();
    private ArrayList<Updates> updatesQueueSeq = new ArrayList();
    private SparseLongArray updatesStartWaitTimeChannels = new SparseLongArray();
    private long updatesStartWaitTimePts;
    private long updatesStartWaitTimeQts;
    private long updatesStartWaitTimeSeq;
    public boolean updatingState;
    private String uploadingAvatar;
    private ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap(100, 1.0f, 2);

    class AnonymousClass127 implements Runnable {
        final /* synthetic */ ArrayList val$chatsArr;
        final /* synthetic */ ArrayList val$usersArr;

        AnonymousClass127(ArrayList arrayList, ArrayList arrayList2) {
            this.val$usersArr = arrayList;
            this.val$chatsArr = arrayList2;
        }

        public void run() {
            MessagesController.this.putUsers(this.val$usersArr, false);
            MessagesController.this.putChats(this.val$chatsArr, false);
        }
    }

    class AnonymousClass128 implements Runnable {
        final /* synthetic */ ArrayList val$chatsArr;
        final /* synthetic */ ArrayList val$usersArr;

        AnonymousClass128(ArrayList arrayList, ArrayList arrayList2) {
            this.val$usersArr = arrayList;
            this.val$chatsArr = arrayList2;
        }

        public void run() {
            MessagesController.this.putUsers(this.val$usersArr, false);
            MessagesController.this.putChats(this.val$chatsArr, false);
        }
    }

    class AnonymousClass129 implements Runnable {
        final /* synthetic */ TL_updateUserBlocked val$finalUpdate;

        AnonymousClass129(TL_updateUserBlocked tL_updateUserBlocked) {
            this.val$finalUpdate = tL_updateUserBlocked;
        }

        public void run() {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    if (!AnonymousClass129.this.val$finalUpdate.blocked) {
                        MessagesController.this.blockedUsers.remove(Integer.valueOf(AnonymousClass129.this.val$finalUpdate.user_id));
                    } else if (!MessagesController.this.blockedUsers.contains(Integer.valueOf(AnonymousClass129.this.val$finalUpdate.user_id))) {
                        MessagesController.this.blockedUsers.add(Integer.valueOf(AnonymousClass129.this.val$finalUpdate.user_id));
                    }
                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoaded, new Object[0]);
                }
            });
        }
    }

    class AnonymousClass130 implements Runnable {
        final /* synthetic */ TL_updateServiceNotification val$update;

        AnonymousClass130(TL_updateServiceNotification tL_updateServiceNotification) {
            this.val$update = tL_updateServiceNotification;
        }

        public void run() {
            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.needShowAlert, Integer.valueOf(2), this.val$update.message);
        }
    }

    class AnonymousClass131 implements Runnable {
        final /* synthetic */ ArrayList val$pushMessagesFinal;

        AnonymousClass131(ArrayList arrayList) {
            this.val$pushMessagesFinal = arrayList;
        }

        public void run() {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(AnonymousClass131.this.val$pushMessagesFinal, true, false);
                }
            });
        }
    }

    class AnonymousClass132 implements Runnable {
        final /* synthetic */ SparseArray val$channelViewsFinal;
        final /* synthetic */ ArrayList val$chatInfoToUpdateFinal;
        final /* synthetic */ ArrayList val$contactsIdsFinal;
        final /* synthetic */ LongSparseArray val$editingMessagesFinal;
        final /* synthetic */ int val$interfaceUpdateMaskFinal;
        final /* synthetic */ LongSparseArray val$messagesFinal;
        final /* synthetic */ boolean val$printChangedArg;
        final /* synthetic */ ArrayList val$updatesOnMainThreadFinal;
        final /* synthetic */ LongSparseArray val$webPagesFinal;

        class AnonymousClass1 implements Runnable {
            final /* synthetic */ User val$currentUser;

            AnonymousClass1(User user) {
                this.val$currentUser = user;
            }

            public void run() {
                ContactsController.getInstance(MessagesController.this.currentAccount).addContactToPhoneBook(this.val$currentUser, true);
            }
        }

        class AnonymousClass2 implements Runnable {
            final /* synthetic */ TL_updateChannel val$update;

            AnonymousClass2(TL_updateChannel tL_updateChannel) {
                this.val$update = tL_updateChannel;
            }

            public void run() {
                MessagesController.this.getChannelDifference(this.val$update.channel_id, 1, 0, null);
            }
        }

        public void run() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.132.run():void
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
            r1 = r34;
            r2 = r1.val$interfaceUpdateMaskFinal;
            r3 = 0;
            r4 = r1.val$updatesOnMainThreadFinal;
            r6 = 2;
            r7 = 0;
            r8 = 1;
            if (r4 == 0) goto L_0x06ff;
        L_0x000c:
            r4 = new java.util.ArrayList;
            r4.<init>();
            r9 = new java.util.ArrayList;
            r9.<init>();
            r10 = 0;
            r11 = 0;
            r12 = r1.val$updatesOnMainThreadFinal;
            r12 = r12.size();
            if (r11 >= r12) goto L_0x06c5;
        L_0x0020:
            r13 = r1.val$updatesOnMainThreadFinal;
            r13 = r13.get(r11);
            r13 = (org.telegram.tgnet.TLRPC.Update) r13;
            r14 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updatePrivacy;
            if (r14 == 0) goto L_0x0079;
        L_0x002c:
            r14 = r13;
            r14 = (org.telegram.tgnet.TLRPC.TL_updatePrivacy) r14;
            r15 = r14.key;
            r15 = r15 instanceof org.telegram.tgnet.TLRPC.TL_privacyKeyStatusTimestamp;
            if (r15 == 0) goto L_0x0045;
        L_0x0035:
            r15 = org.telegram.messenger.MessagesController.this;
            r15 = r15.currentAccount;
            r15 = org.telegram.messenger.ContactsController.getInstance(r15);
            r5 = r14.rules;
            r15.setPrivacyRules(r5, r7);
            goto L_0x0070;
        L_0x0045:
            r5 = r14.key;
            r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_privacyKeyChatInvite;
            if (r5 == 0) goto L_0x005b;
            r5 = org.telegram.messenger.MessagesController.this;
            r5 = r5.currentAccount;
            r5 = org.telegram.messenger.ContactsController.getInstance(r5);
            r15 = r14.rules;
            r5.setPrivacyRules(r15, r8);
            goto L_0x0070;
            r5 = r14.key;
            r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_privacyKeyPhoneCall;
            if (r5 == 0) goto L_0x0070;
            r5 = org.telegram.messenger.MessagesController.this;
            r5 = r5.currentAccount;
            r5 = org.telegram.messenger.ContactsController.getInstance(r5);
            r15 = r14.rules;
            r5.setPrivacyRules(r15, r6);
            r24 = r3;
            r29 = r4;
            r28 = r12;
            goto L_0x06b8;
        L_0x0079:
            r5 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateUserStatus;
            if (r5 == 0) goto L_0x00f0;
            r5 = r13;
            r5 = (org.telegram.tgnet.TLRPC.TL_updateUserStatus) r5;
            r14 = org.telegram.messenger.MessagesController.this;
            r15 = r5.user_id;
            r15 = java.lang.Integer.valueOf(r15);
            r14 = r14.getUser(r15);
            r15 = r5.status;
            r15 = r15 instanceof org.telegram.tgnet.TLRPC.TL_userStatusRecently;
            if (r15 == 0) goto L_0x0099;
            r15 = r5.status;
            r6 = -100;
            r15.expires = r6;
            goto L_0x00b2;
            r6 = r5.status;
            r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_userStatusLastWeek;
            if (r6 == 0) goto L_0x00a6;
            r6 = r5.status;
            r15 = -101; // 0xffffffffffffff9b float:NaN double:NaN;
            r6.expires = r15;
            goto L_0x00b2;
            r6 = r5.status;
            r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_userStatusLastMonth;
            if (r6 == 0) goto L_0x00b2;
            r6 = r5.status;
            r15 = -102; // 0xffffffffffffff9a float:NaN double:NaN;
            r6.expires = r15;
            if (r14 == 0) goto L_0x00bc;
            r6 = r5.user_id;
            r14.id = r6;
            r6 = r5.status;
            r14.status = r6;
            r6 = new org.telegram.tgnet.TLRPC$TL_user;
            r6.<init>();
            r15 = r5.user_id;
            r6.id = r15;
            r15 = r5.status;
            r6.status = r15;
            r9.add(r6);
            r15 = r5.user_id;
            r8 = org.telegram.messenger.MessagesController.this;
            r8 = r8.currentAccount;
            r8 = org.telegram.messenger.UserConfig.getInstance(r8);
            r8 = r8.getClientUserId();
            if (r15 != r8) goto L_0x00ef;
            r8 = org.telegram.messenger.MessagesController.this;
            r8 = r8.currentAccount;
            r8 = org.telegram.messenger.NotificationsController.getInstance(r8);
            r15 = r5.status;
            r15 = r15.expires;
            r8.setLastOnlineFromOtherDevice(r15);
            goto L_0x0071;
            r5 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateUserName;
            if (r5 == 0) goto L_0x0157;
            r5 = r13;
            r5 = (org.telegram.tgnet.TLRPC.TL_updateUserName) r5;
            r6 = org.telegram.messenger.MessagesController.this;
            r8 = r5.user_id;
            r8 = java.lang.Integer.valueOf(r8);
            r6 = r6.getUser(r8);
            if (r6 == 0) goto L_0x013d;
            r8 = org.telegram.messenger.UserObject.isContact(r6);
            if (r8 != 0) goto L_0x0113;
            r8 = r5.first_name;
            r6.first_name = r8;
            r8 = r5.last_name;
            r6.last_name = r8;
            r8 = r6.username;
            r8 = android.text.TextUtils.isEmpty(r8);
            if (r8 != 0) goto L_0x0126;
            r8 = org.telegram.messenger.MessagesController.this;
            r8 = r8.objectsByUsernames;
            r14 = r6.username;
            r8.remove(r14);
            r8 = r5.username;
            r8 = android.text.TextUtils.isEmpty(r8);
            if (r8 == 0) goto L_0x0139;
            r8 = org.telegram.messenger.MessagesController.this;
            r8 = r8.objectsByUsernames;
            r14 = r5.username;
            r8.put(r14, r6);
            r8 = r5.username;
            r6.username = r8;
            r8 = new org.telegram.tgnet.TLRPC$TL_user;
            r8.<init>();
            r14 = r5.user_id;
            r8.id = r14;
            r14 = r5.first_name;
            r8.first_name = r14;
            r14 = r5.last_name;
            r8.last_name = r14;
            r14 = r5.username;
            r8.username = r14;
            r4.add(r8);
            goto L_0x0071;
            r5 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateDialogPinned;
            if (r5 == 0) goto L_0x01b7;
            r5 = r13;
            r5 = (org.telegram.tgnet.TLRPC.TL_updateDialogPinned) r5;
            r6 = r5.peer;
            r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_dialogPeer;
            if (r6 == 0) goto L_0x0180;
            r6 = r5.peer;
            r6 = (org.telegram.tgnet.TLRPC.TL_dialogPeer) r6;
            r6 = r6.peer;
            r8 = r6 instanceof org.telegram.tgnet.TLRPC.TL_peerUser;
            if (r8 == 0) goto L_0x0172;
            r8 = r6.user_id;
            r14 = (long) r8;
            goto L_0x017f;
            r8 = r6 instanceof org.telegram.tgnet.TLRPC.TL_peerChat;
            if (r8 == 0) goto L_0x017b;
            r8 = r6.chat_id;
            r8 = -r8;
            r14 = (long) r8;
            goto L_0x0171;
            r8 = r6.channel_id;
            r8 = -r8;
            r14 = (long) r8;
            goto L_0x0182;
            r14 = 0;
            r6 = org.telegram.messenger.MessagesController.this;
            r8 = r5.pinned;
            r20 = 0;
            r21 = -1;
            r16 = r6;
            r17 = r14;
            r19 = r8;
            r6 = r16.pinDialog(r17, r19, r20, r21);
            if (r6 != 0) goto L_0x01b5;
            r6 = org.telegram.messenger.MessagesController.this;
            r6 = r6.currentAccount;
            r6 = org.telegram.messenger.UserConfig.getInstance(r6);
            r6.pinnedDialogsLoaded = r7;
            r6 = org.telegram.messenger.MessagesController.this;
            r6 = r6.currentAccount;
            r6 = org.telegram.messenger.UserConfig.getInstance(r6);
            r6.saveConfig(r7);
            r6 = org.telegram.messenger.MessagesController.this;
            r8 = 0;
            r6.loadPinnedDialogs(r14, r8);
            goto L_0x0071;
            r5 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updatePinnedDialogs;
            if (r5 == 0) goto L_0x0252;
            r5 = r13;
            r5 = (org.telegram.tgnet.TLRPC.TL_updatePinnedDialogs) r5;
            r6 = org.telegram.messenger.MessagesController.this;
            r6 = r6.currentAccount;
            r6 = org.telegram.messenger.UserConfig.getInstance(r6);
            r6.pinnedDialogsLoaded = r7;
            r6 = org.telegram.messenger.MessagesController.this;
            r6 = r6.currentAccount;
            r6 = org.telegram.messenger.UserConfig.getInstance(r6);
            r6.saveConfig(r7);
            r6 = r5.flags;
            r8 = 1;
            r6 = r6 & r8;
            if (r6 == 0) goto L_0x0243;
            r6 = new java.util.ArrayList;
            r6.<init>();
            r8 = r13;
            r8 = (org.telegram.tgnet.TLRPC.TL_updatePinnedDialogs) r8;
            r8 = r8.order;
            r16 = 0;
            r17 = r8.size();
            r7 = r16;
            r23 = r17;
            r14 = r23;
            if (r7 >= r14) goto L_0x023e;
            r15 = r8.get(r7);
            r15 = (org.telegram.tgnet.TLRPC.DialogPeer) r15;
            r24 = r3;
            r3 = r15 instanceof org.telegram.tgnet.TLRPC.TL_dialogPeer;
            if (r3 == 0) goto L_0x0226;
            r3 = r15;
            r3 = (org.telegram.tgnet.TLRPC.TL_dialogPeer) r3;
            r3 = r3.peer;
            r25 = r5;
            r5 = r3.user_id;
            if (r5 == 0) goto L_0x0214;
            r5 = r3.user_id;
            r26 = r14;
            r27 = r15;
            r14 = (long) r5;
            goto L_0x0225;
            r26 = r14;
            r27 = r15;
            r5 = r3.chat_id;
            if (r5 == 0) goto L_0x0221;
            r5 = r3.chat_id;
            r5 = -r5;
            r14 = (long) r5;
            goto L_0x0225;
            r5 = r3.channel_id;
            r5 = -r5;
            r14 = (long) r5;
            goto L_0x022e;
            r25 = r5;
            r26 = r14;
            r27 = r15;
            r14 = 0;
            r3 = java.lang.Long.valueOf(r14);
            r6.add(r3);
            r7 = r7 + 1;
            r3 = r24;
            r5 = r25;
            r17 = r26;
            goto L_0x01ef;
            r24 = r3;
            r25 = r5;
            goto L_0x0248;
            r24 = r3;
            r25 = r5;
            r6 = 0;
            r3 = r6;
            r5 = org.telegram.messenger.MessagesController.this;
            r6 = 0;
            r5.loadPinnedDialogs(r6, r3);
            goto L_0x0073;
            r24 = r3;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateUserPhoto;
            if (r3 == 0) goto L_0x027f;
            r3 = r13;
            r3 = (org.telegram.tgnet.TLRPC.TL_updateUserPhoto) r3;
            r5 = org.telegram.messenger.MessagesController.this;
            r6 = r3.user_id;
            r6 = java.lang.Integer.valueOf(r6);
            r5 = r5.getUser(r6);
            if (r5 == 0) goto L_0x026d;
            r6 = r3.photo;
            r5.photo = r6;
            r6 = new org.telegram.tgnet.TLRPC$TL_user;
            r6.<init>();
            r7 = r3.user_id;
            r6.id = r7;
            r7 = r3.photo;
            r6.photo = r7;
            r4.add(r6);
            goto L_0x0073;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateUserPhone;
            if (r3 == 0) goto L_0x02b4;
            r3 = r13;
            r3 = (org.telegram.tgnet.TLRPC.TL_updateUserPhone) r3;
            r5 = org.telegram.messenger.MessagesController.this;
            r6 = r3.user_id;
            r6 = java.lang.Integer.valueOf(r6);
            r5 = r5.getUser(r6);
            if (r5 == 0) goto L_0x02a2;
            r6 = r3.phone;
            r5.phone = r6;
            r6 = org.telegram.messenger.Utilities.phoneBookQueue;
            r7 = new org.telegram.messenger.MessagesController$132$1;
            r7.<init>(r5);
            r6.postRunnable(r7);
            r6 = new org.telegram.tgnet.TLRPC$TL_user;
            r6.<init>();
            r7 = r3.user_id;
            r6.id = r7;
            r7 = r3.phone;
            r6.phone = r7;
            r4.add(r6);
            goto L_0x0073;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateNotifySettings;
            if (r3 == 0) goto L_0x03fe;
            r3 = r13;
            r3 = (org.telegram.tgnet.TLRPC.TL_updateNotifySettings) r3;
            r5 = r3.notify_settings;
            r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_peerNotifySettings;
            if (r5 == 0) goto L_0x03f6;
            r5 = r3.peer;
            r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_notifyPeer;
            if (r5 == 0) goto L_0x03f6;
            if (r10 != 0) goto L_0x02d3;
            r5 = org.telegram.messenger.MessagesController.this;
            r5 = r5.notificationsPreferences;
            r10 = r5.edit();
            r5 = r3.peer;
            r5 = r5.peer;
            r5 = r5.user_id;
            if (r5 == 0) goto L_0x02e3;
            r5 = r3.peer;
            r5 = r5.peer;
            r5 = r5.user_id;
            r5 = (long) r5;
            goto L_0x02fc;
            r5 = r3.peer;
            r5 = r5.peer;
            r5 = r5.chat_id;
            if (r5 == 0) goto L_0x02f4;
            r5 = r3.peer;
            r5 = r5.peer;
            r5 = r5.chat_id;
            r5 = -r5;
            r5 = (long) r5;
            goto L_0x02e2;
            r5 = r3.peer;
            r5 = r5.peer;
            r5 = r5.channel_id;
            r5 = -r5;
            r5 = (long) r5;
            r7 = org.telegram.messenger.MessagesController.this;
            r7 = r7.dialogs_dict;
            r7 = r7.get(r5);
            r7 = (org.telegram.tgnet.TLRPC.TL_dialog) r7;
            if (r7 == 0) goto L_0x030c;
            r8 = r3.notify_settings;
            r7.notify_settings = r8;
            r8 = new java.lang.StringBuilder;
            r8.<init>();
            r14 = "silent_";
            r8.append(r14);
            r8.append(r5);
            r8 = r8.toString();
            r14 = r3.notify_settings;
            r14 = r14.silent;
            r10.putBoolean(r8, r14);
            r8 = org.telegram.messenger.MessagesController.this;
            r8 = r8.currentAccount;
            r8 = org.telegram.tgnet.ConnectionsManager.getInstance(r8);
            r8 = r8.getCurrentTime();
            r14 = r3.notify_settings;
            r14 = r14.mute_until;
            if (r14 <= r8) goto L_0x03c5;
            r14 = 0;
            r15 = r3.notify_settings;
            r15 = r15.mute_until;
            r16 = 31536000; // 0x1e13380 float:8.2725845E-38 double:1.5580854E-316;
            r28 = r12;
            r12 = r8 + r16;
            if (r15 <= r12) goto L_0x0365;
            r12 = new java.lang.StringBuilder;
            r12.<init>();
            r15 = "notify2_";
            r12.append(r15);
            r12.append(r5);
            r12 = r12.toString();
            r15 = 2;
            r10.putInt(r12, r15);
            if (r7 == 0) goto L_0x039c;
            r12 = r7.notify_settings;
            r15 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
            r12.mute_until = r15;
            goto L_0x039c;
            r12 = r3.notify_settings;
            r14 = r12.mute_until;
            r12 = new java.lang.StringBuilder;
            r12.<init>();
            r15 = "notify2_";
            r12.append(r15);
            r12.append(r5);
            r12 = r12.toString();
            r15 = 3;
            r10.putInt(r12, r15);
            r12 = new java.lang.StringBuilder;
            r12.<init>();
            r15 = "notifyuntil_";
            r12.append(r15);
            r12.append(r5);
            r12 = r12.toString();
            r15 = r3.notify_settings;
            r15 = r15.mute_until;
            r10.putInt(r12, r15);
            if (r7 == 0) goto L_0x039c;
            r12 = r7.notify_settings;
            r12.mute_until = r14;
            r12 = org.telegram.messenger.MessagesController.this;
            r12 = r12.currentAccount;
            r12 = org.telegram.messenger.MessagesStorage.getInstance(r12);
            r30 = r3;
            r29 = r4;
            r3 = (long) r14;
            r15 = 32;
            r3 = r3 << r15;
            r16 = 1;
            r31 = r14;
            r14 = r3 | r16;
            r12.setDialogFlags(r5, r14);
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.NotificationsController.getInstance(r3);
            r3.removeNotificationsForDialog(r5);
            goto L_0x03fa;
            r30 = r3;
            r29 = r4;
            r28 = r12;
            if (r7 == 0) goto L_0x03d2;
            r3 = r7.notify_settings;
            r4 = 0;
            r3.mute_until = r4;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "notify2_";
            r3.append(r4);
            r3.append(r5);
            r3 = r3.toString();
            r10.remove(r3);
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
            r14 = 0;
            r3.setDialogFlags(r5, r14);
            goto L_0x03fa;
            r29 = r4;
            r28 = r12;
            r3 = r24;
            goto L_0x06ba;
            r29 = r4;
            r28 = r12;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateChannel;
            if (r3 == 0) goto L_0x0455;
            r3 = r13;
            r3 = (org.telegram.tgnet.TLRPC.TL_updateChannel) r3;
            r4 = org.telegram.messenger.MessagesController.this;
            r4 = r4.dialogs_dict;
            r5 = r3.channel_id;
            r5 = (long) r5;
            r5 = -r5;
            r4 = r4.get(r5);
            r4 = (org.telegram.tgnet.TLRPC.TL_dialog) r4;
            r5 = org.telegram.messenger.MessagesController.this;
            r6 = r3.channel_id;
            r6 = java.lang.Integer.valueOf(r6);
            r5 = r5.getChat(r6);
            if (r5 == 0) goto L_0x0449;
            if (r4 != 0) goto L_0x043a;
            r6 = r5 instanceof org.telegram.tgnet.TLRPC.TL_channel;
            if (r6 == 0) goto L_0x043a;
            r6 = r5.left;
            if (r6 != 0) goto L_0x043a;
            r6 = org.telegram.messenger.Utilities.stageQueue;
            r7 = new org.telegram.messenger.MessagesController$132$2;
            r7.<init>(r3);
            r6.postRunnable(r7);
            goto L_0x0449;
            r6 = r5.left;
            if (r6 == 0) goto L_0x0449;
            if (r4 == 0) goto L_0x0449;
            r6 = org.telegram.messenger.MessagesController.this;
            r7 = r4.id;
            r12 = 0;
            r6.deleteDialog(r7, r12);
            goto L_0x044a;
            r12 = 0;
            r2 = r2 | 8192;
            r6 = org.telegram.messenger.MessagesController.this;
            r7 = r3.channel_id;
            r8 = 1;
            r6.loadFullChat(r7, r12, r8);
            goto L_0x03fa;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateChatAdmins;
            if (r3 == 0) goto L_0x045c;
            r2 = r2 | 16384;
            goto L_0x03fa;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateStickerSets;
            if (r3 == 0) goto L_0x0474;
            r3 = r13;
            r3 = (org.telegram.tgnet.TLRPC.TL_updateStickerSets) r3;
            r4 = org.telegram.messenger.MessagesController.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.DataQuery.getInstance(r4);
            r5 = 1;
            r6 = 0;
            r4.loadStickers(r6, r6, r5);
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateStickerSetsOrder;
            if (r3 == 0) goto L_0x0491;
            r3 = r13;
            r3 = (org.telegram.tgnet.TLRPC.TL_updateStickerSetsOrder) r3;
            r4 = org.telegram.messenger.MessagesController.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.DataQuery.getInstance(r4);
            r5 = r3.masks;
            r6 = r13;
            r6 = (org.telegram.tgnet.TLRPC.TL_updateStickerSetsOrder) r6;
            r6 = r6.order;
            r4.reorderStickers(r5, r6);
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateFavedStickers;
            if (r3 == 0) goto L_0x04a7;
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.DataQuery.getInstance(r3);
            r4 = 1;
            r5 = 0;
            r6 = 2;
            r3.loadRecents(r6, r5, r5, r4);
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateContactsReset;
            if (r3 == 0) goto L_0x04ba;
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.ContactsController.getInstance(r3);
            r3.forceImportContacts();
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateNewStickerSet;
            if (r3 == 0) goto L_0x04d2;
            r3 = r13;
            r3 = (org.telegram.tgnet.TLRPC.TL_updateNewStickerSet) r3;
            r4 = org.telegram.messenger.MessagesController.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.DataQuery.getInstance(r4);
            r5 = r3.stickerset;
            r4.addNewStickerSet(r5);
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateSavedGifs;
            if (r3 == 0) goto L_0x04ed;
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.emojiPreferences;
            r3 = r3.edit();
            r4 = "lastGifLoadTime";
            r5 = 0;
            r4 = r3.putLong(r4, r5);
            r4.commit();
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateRecentStickers;
            if (r3 == 0) goto L_0x0508;
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.emojiPreferences;
            r3 = r3.edit();
            r4 = "lastStickersLoadTime";
            r5 = 0;
            r4 = r3.putLong(r4, r5);
            r4.commit();
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateDraftMessage;
            if (r3 == 0) goto L_0x0546;
            r3 = r13;
            r3 = (org.telegram.tgnet.TLRPC.TL_updateDraftMessage) r3;
            r4 = 1;
            r5 = r13;
            r5 = (org.telegram.tgnet.TLRPC.TL_updateDraftMessage) r5;
            r5 = r5.peer;
            r6 = r5.user_id;
            if (r6 == 0) goto L_0x051f;
            r6 = r5.user_id;
            r6 = (long) r6;
            r17 = r6;
            goto L_0x052d;
            r6 = r5.channel_id;
            if (r6 == 0) goto L_0x0528;
            r6 = r5.channel_id;
            r6 = -r6;
            r6 = (long) r6;
            goto L_0x051c;
            r6 = r5.chat_id;
            r6 = -r6;
            r6 = (long) r6;
            goto L_0x051c;
            r6 = org.telegram.messenger.MessagesController.this;
            r6 = r6.currentAccount;
            r16 = org.telegram.messenger.DataQuery.getInstance(r6);
            r6 = r3.draft;
            r20 = 0;
            r21 = 1;
            r19 = r6;
            r16.saveDraft(r17, r19, r20, r21);
            r3 = r4;
            goto L_0x06ba;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateReadFeaturedStickers;
            if (r3 == 0) goto L_0x055a;
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.DataQuery.getInstance(r3);
            r4 = 0;
            r3.markFaturedStickersAsRead(r4);
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updatePhoneCall;
            if (r3 == 0) goto L_0x06b1;
            r3 = r13;
            r3 = (org.telegram.tgnet.TLRPC.TL_updatePhoneCall) r3;
            r4 = r3.phone_call;
            r5 = org.telegram.messenger.voip.VoIPService.getSharedInstance();
            r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r6 == 0) goto L_0x0595;
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r7 = "Received call in update: ";
            r6.append(r7);
            r6.append(r4);
            r6 = r6.toString();
            org.telegram.messenger.FileLog.d(r6);
            r6 = new java.lang.StringBuilder;
            r6.<init>();
            r7 = "call id ";
            r6.append(r7);
            r7 = r4.id;
            r6.append(r7);
            r6 = r6.toString();
            org.telegram.messenger.FileLog.d(r6);
            r6 = r4 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallRequested;
            if (r6 == 0) goto L_0x068f;
            r6 = r4.date;
            r7 = org.telegram.messenger.MessagesController.this;
            r7 = r7.callRingTimeout;
            r7 = r7 / 1000;
            r6 = r6 + r7;
            r7 = org.telegram.messenger.MessagesController.this;
            r7 = r7.currentAccount;
            r7 = org.telegram.tgnet.ConnectionsManager.getInstance(r7);
            r7 = r7.getCurrentTime();
            if (r6 >= r7) goto L_0x05bd;
            r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r6 == 0) goto L_0x06b8;
            r6 = "ignoring too old call";
            org.telegram.messenger.FileLog.d(r6);
            goto L_0x06b8;
            r6 = org.telegram.messenger.ApplicationLoader.applicationContext;
            r7 = "phone";
            r6 = r6.getSystemService(r7);
            r6 = (android.telephony.TelephonyManager) r6;
            if (r5 != 0) goto L_0x063e;
            r7 = org.telegram.messenger.voip.VoIPService.callIShouldHavePutIntoIntent;
            if (r7 != 0) goto L_0x063e;
            r7 = r6.getCallState();
            if (r7 == 0) goto L_0x05d4;
            goto L_0x063e;
            r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r7 == 0) goto L_0x05ee;
            r7 = new java.lang.StringBuilder;
            r7.<init>();
            r8 = "Starting service for call ";
            r7.append(r8);
            r14 = r4.id;
            r7.append(r14);
            r7 = r7.toString();
            org.telegram.messenger.FileLog.d(r7);
            org.telegram.messenger.voip.VoIPService.callIShouldHavePutIntoIntent = r4;
            r7 = new android.content.Intent;
            r8 = org.telegram.messenger.ApplicationLoader.applicationContext;
            r12 = org.telegram.messenger.voip.VoIPService.class;
            r7.<init>(r8, r12);
            r8 = "is_outgoing";
            r12 = 0;
            r7.putExtra(r8, r12);
            r8 = "user_id";
            r12 = r4.participant_id;
            r14 = org.telegram.messenger.MessagesController.this;
            r14 = r14.currentAccount;
            r14 = org.telegram.messenger.UserConfig.getInstance(r14);
            r14 = r14.getClientUserId();
            if (r12 != r14) goto L_0x0616;
            r12 = r4.admin_id;
            goto L_0x0618;
            r12 = r4.participant_id;
            r7.putExtra(r8, r12);
            r8 = "account";
            r12 = org.telegram.messenger.MessagesController.this;
            r12 = r12.currentAccount;
            r7.putExtra(r8, r12);
            r8 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x0638 }
            r12 = 26;	 Catch:{ Throwable -> 0x0638 }
            if (r8 < r12) goto L_0x0632;	 Catch:{ Throwable -> 0x0638 }
            r8 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0638 }
            r8.startForegroundService(r7);	 Catch:{ Throwable -> 0x0638 }
            goto L_0x0637;	 Catch:{ Throwable -> 0x0638 }
            r8 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x0638 }
            r8.startService(r7);	 Catch:{ Throwable -> 0x0638 }
            goto L_0x063d;
        L_0x0638:
            r0 = move-exception;
            r8 = r0;
            org.telegram.messenger.FileLog.e(r8);
            goto L_0x06b0;
            r7 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r7 == 0) goto L_0x065d;
            r7 = new java.lang.StringBuilder;
            r7.<init>();
            r8 = "Auto-declining call ";
            r7.append(r8);
            r14 = r4.id;
            r7.append(r14);
            r8 = " because there's already active one";
            r7.append(r8);
            r7 = r7.toString();
            org.telegram.messenger.FileLog.d(r7);
            r7 = new org.telegram.tgnet.TLRPC$TL_phone_discardCall;
            r7.<init>();
            r8 = new org.telegram.tgnet.TLRPC$TL_inputPhoneCall;
            r8.<init>();
            r7.peer = r8;
            r8 = r7.peer;
            r14 = r4.access_hash;
            r8.access_hash = r14;
            r8 = r7.peer;
            r14 = r4.id;
            r8.id = r14;
            r8 = new org.telegram.tgnet.TLRPC$TL_phoneCallDiscardReasonBusy;
            r8.<init>();
            r7.reason = r8;
            r8 = org.telegram.messenger.MessagesController.this;
            r8 = r8.currentAccount;
            r8 = org.telegram.tgnet.ConnectionsManager.getInstance(r8);
            r12 = new org.telegram.messenger.MessagesController$132$3;
            r12.<init>();
            r8.sendRequest(r7, r12);
            goto L_0x06b8;
            if (r5 == 0) goto L_0x0697;
            if (r4 == 0) goto L_0x0697;
            r5.onCallUpdated(r4);
            goto L_0x06b0;
            r6 = org.telegram.messenger.voip.VoIPService.callIShouldHavePutIntoIntent;
            if (r6 == 0) goto L_0x06b0;
            r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
            if (r6 == 0) goto L_0x06a4;
            r6 = "Updated the call while the service is starting";
            org.telegram.messenger.FileLog.d(r6);
            r6 = r4.id;
            r8 = org.telegram.messenger.voip.VoIPService.callIShouldHavePutIntoIntent;
            r14 = r8.id;
            r8 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1));
            if (r8 != 0) goto L_0x06b0;
            org.telegram.messenger.voip.VoIPService.callIShouldHavePutIntoIntent = r4;
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateGroupCall;
            if (r3 == 0) goto L_0x06b6;
            goto L_0x06b8;
            r3 = r13 instanceof org.telegram.tgnet.TLRPC.TL_updateGroupCallParticipant;
            r3 = r24;
            r11 = r11 + 1;
            r12 = r28;
            r4 = r29;
            r6 = 2;
            r7 = 0;
            r8 = 1;
            goto L_0x001e;
        L_0x06c5:
            r24 = r3;
            r29 = r4;
            if (r10 == 0) goto L_0x06e0;
            r10.commit();
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.NotificationCenter.getInstance(r3);
            r4 = org.telegram.messenger.NotificationCenter.notificationsSettingsUpdated;
            r5 = 0;
            r6 = new java.lang.Object[r5];
            r3.postNotificationName(r4, r6);
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
            r4 = 1;
            r3.updateUsers(r9, r4, r4, r4);
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
            r5 = r29;
            r6 = 0;
            r3.updateUsers(r5, r6, r4, r4);
            goto L_0x0702;
        L_0x06ff:
            r4 = r8;
            r24 = r3;
            r3 = r1.val$webPagesFinal;
            if (r3 == 0) goto L_0x07ee;
            r3 = org.telegram.messenger.MessagesController.this;
            r3 = r3.currentAccount;
            r3 = org.telegram.messenger.NotificationCenter.getInstance(r3);
            r5 = org.telegram.messenger.NotificationCenter.didReceivedWebpagesInUpdates;
            r6 = new java.lang.Object[r4];
            r4 = r1.val$webPagesFinal;
            r7 = 0;
            r6[r7] = r4;
            r3.postNotificationName(r5, r6);
            r3 = 0;
            r4 = r1.val$webPagesFinal;
            r4 = r4.size();
            if (r3 >= r4) goto L_0x07ee;
            r5 = r1.val$webPagesFinal;
            r5 = r5.keyAt(r3);
            r7 = org.telegram.messenger.MessagesController.this;
            r7 = r7.reloadingWebpagesPending;
            r7 = r7.get(r5);
            r7 = (java.util.ArrayList) r7;
            r8 = org.telegram.messenger.MessagesController.this;
            r8 = r8.reloadingWebpagesPending;
            r8.remove(r5);
            if (r7 == 0) goto L_0x07ea;
            r8 = r1.val$webPagesFinal;
            r8 = r8.valueAt(r3);
            r8 = (org.telegram.tgnet.TLRPC.WebPage) r8;
            r9 = new java.util.ArrayList;
            r9.<init>();
            r10 = 0;
            r12 = r8 instanceof org.telegram.tgnet.TLRPC.TL_webPage;
            if (r12 != 0) goto L_0x0767;
            r12 = r8 instanceof org.telegram.tgnet.TLRPC.TL_webPageEmpty;
            if (r12 == 0) goto L_0x075a;
            goto L_0x0767;
            r12 = org.telegram.messenger.MessagesController.this;
            r12 = r12.reloadingWebpagesPending;
            r13 = r8.id;
            r12.put(r13, r7);
            r14 = r10;
            goto L_0x079f;
            r12 = 0;
            r13 = r7.size();
            if (r12 >= r13) goto L_0x0765;
            r14 = r7.get(r12);
            r14 = (org.telegram.messenger.MessageObject) r14;
            r14 = r14.messageOwner;
            r14 = r14.media;
            r14.webpage = r8;
            if (r12 != 0) goto L_0x0791;
            r14 = r7.get(r12);
            r14 = (org.telegram.messenger.MessageObject) r14;
            r10 = r14.getDialogId();
            r14 = r7.get(r12);
            r14 = (org.telegram.messenger.MessageObject) r14;
            r14 = r14.messageOwner;
            org.telegram.messenger.ImageLoader.saveMessageThumbs(r14);
            r14 = r7.get(r12);
            r14 = (org.telegram.messenger.MessageObject) r14;
            r14 = r14.messageOwner;
            r9.add(r14);
            r12 = r12 + 1;
            goto L_0x076c;
            r10 = r9.isEmpty();
            if (r10 != 0) goto L_0x07ea;
            r10 = org.telegram.messenger.MessagesController.this;
            r10 = r10.currentAccount;
            r10 = org.telegram.messenger.MessagesStorage.getInstance(r10);
            r12 = 1;
            r13 = 1;
            r16 = 0;
            r11 = org.telegram.messenger.MessagesController.this;
            r11 = r11.currentAccount;
            r11 = org.telegram.messenger.DownloadController.getInstance(r11);
            r17 = r11.getAutodownloadMask();
            r11 = r9;
            r32 = r14;
            r14 = r16;
            r15 = r17;
            r10.putMessages(r11, r12, r13, r14, r15);
            r10 = org.telegram.messenger.MessagesController.this;
            r10 = r10.currentAccount;
            r10 = org.telegram.messenger.NotificationCenter.getInstance(r10);
            r11 = org.telegram.messenger.NotificationCenter.replaceMessagesObjects;
            r12 = 2;
            r13 = new java.lang.Object[r12];
            r14 = r32;
            r12 = java.lang.Long.valueOf(r14);
            r16 = 0;
            r13[r16] = r12;
            r12 = 1;
            r13[r12] = r7;
            r10.postNotificationName(r11, r13);
            r3 = r3 + 1;
            goto L_0x0723;
            r3 = 0;
            r4 = r1.val$messagesFinal;
            if (r4 == 0) goto L_0x0814;
            r4 = 0;
            r5 = r1.val$messagesFinal;
            r5 = r5.size();
            if (r4 >= r5) goto L_0x0812;
            r6 = r1.val$messagesFinal;
            r6 = r6.keyAt(r4);
            r8 = r1.val$messagesFinal;
            r8 = r8.valueAt(r4);
            r8 = (java.util.ArrayList) r8;
            r9 = org.telegram.messenger.MessagesController.this;
            r9.updateInterfaceWithMessages(r6, r8);
            r4 = r4 + 1;
            goto L_0x07fa;
            r3 = 1;
            goto L_0x081d;
            if (r24 == 0) goto L_0x081d;
            r4 = org.telegram.messenger.MessagesController.this;
            r5 = 0;
            r4.sortDialogs(r5);
            r3 = 1;
            r4 = r1.val$editingMessagesFinal;
            if (r4 == 0) goto L_0x08ad;
            r4 = 0;
            r5 = r1.val$editingMessagesFinal;
            r5 = r5.size();
            if (r4 >= r5) goto L_0x08ad;
            r6 = r1.val$editingMessagesFinal;
            r6 = r6.keyAt(r4);
            r8 = r1.val$editingMessagesFinal;
            r8 = r8.valueAt(r4);
            r8 = (java.util.ArrayList) r8;
            r9 = org.telegram.messenger.MessagesController.this;
            r9 = r9.dialogMessage;
            r9 = r9.get(r6);
            r9 = (org.telegram.messenger.MessageObject) r9;
            if (r9 == 0) goto L_0x0880;
            r10 = 0;
            r11 = r8.size();
            if (r10 >= r11) goto L_0x0880;
            r12 = r8.get(r10);
            r12 = (org.telegram.messenger.MessageObject) r12;
            r13 = r9.getId();
            r14 = r12.getId();
            if (r13 != r14) goto L_0x087d;
            r13 = org.telegram.messenger.MessagesController.this;
            r13 = r13.dialogMessage;
            r13.put(r6, r12);
            r13 = r12.messageOwner;
            r13 = r13.to_id;
            if (r13 == 0) goto L_0x087b;
            r13 = r12.messageOwner;
            r13 = r13.to_id;
            r13 = r13.channel_id;
            if (r13 != 0) goto L_0x087b;
            r13 = org.telegram.messenger.MessagesController.this;
            r13 = r13.dialogMessagesByIds;
            r14 = r12.getId();
            r13.put(r14, r12);
            r3 = 1;
            goto L_0x0880;
            r10 = r10 + 1;
            goto L_0x0849;
            r10 = org.telegram.messenger.MessagesController.this;
            r10 = r10.currentAccount;
            r10 = org.telegram.messenger.DataQuery.getInstance(r10);
            r10.loadReplyMessagesForMessages(r8, r6);
            r10 = org.telegram.messenger.MessagesController.this;
            r10 = r10.currentAccount;
            r10 = org.telegram.messenger.NotificationCenter.getInstance(r10);
            r11 = org.telegram.messenger.NotificationCenter.replaceMessagesObjects;
            r12 = 2;
            r13 = new java.lang.Object[r12];
            r14 = java.lang.Long.valueOf(r6);
            r15 = 0;
            r13[r15] = r14;
            r14 = 1;
            r13[r14] = r8;
            r10.postNotificationName(r11, r13);
            r4 = r4 + 1;
            goto L_0x0828;
            if (r3 == 0) goto L_0x08c1;
            r4 = org.telegram.messenger.MessagesController.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
            r5 = org.telegram.messenger.NotificationCenter.dialogsNeedReload;
            r6 = 0;
            r7 = new java.lang.Object[r6];
            r4.postNotificationName(r5, r7);
            r4 = r1.val$printChangedArg;
            if (r4 == 0) goto L_0x08c7;
            r2 = r2 | 64;
            r4 = r1.val$contactsIdsFinal;
            if (r4 == 0) goto L_0x08cf;
            r2 = r2 | 1;
            r2 = r2 | 128;
            r4 = r1.val$chatInfoToUpdateFinal;
            if (r4 == 0) goto L_0x08f4;
            r4 = 0;
            r5 = r1.val$chatInfoToUpdateFinal;
            r5 = r5.size();
            if (r4 >= r5) goto L_0x08f4;
            r6 = r1.val$chatInfoToUpdateFinal;
            r6 = r6.get(r4);
            r6 = (org.telegram.tgnet.TLRPC.ChatParticipants) r6;
            r7 = org.telegram.messenger.MessagesController.this;
            r7 = r7.currentAccount;
            r7 = org.telegram.messenger.MessagesStorage.getInstance(r7);
            r7.updateChatParticipants(r6);
            r4 = r4 + 1;
            goto L_0x08da;
            r4 = r1.val$channelViewsFinal;
            if (r4 == 0) goto L_0x090f;
            r4 = org.telegram.messenger.MessagesController.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
            r5 = org.telegram.messenger.NotificationCenter.didUpdatedMessagesViews;
            r6 = 1;
            r7 = new java.lang.Object[r6];
            r6 = r1.val$channelViewsFinal;
            r8 = 0;
            r7[r8] = r6;
            r4.postNotificationName(r5, r7);
            if (r2 == 0) goto L_0x092a;
            r4 = org.telegram.messenger.MessagesController.this;
            r4 = r4.currentAccount;
            r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
            r5 = org.telegram.messenger.NotificationCenter.updateInterfaces;
            r6 = 1;
            r6 = new java.lang.Object[r6];
            r7 = java.lang.Integer.valueOf(r2);
            r8 = 0;
            r6[r8] = r7;
            r4.postNotificationName(r5, r6);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.132.run():void");
        }

        AnonymousClass132(int i, ArrayList arrayList, LongSparseArray longSparseArray, LongSparseArray longSparseArray2, LongSparseArray longSparseArray3, boolean z, ArrayList arrayList2, ArrayList arrayList3, SparseArray sparseArray) {
            this.val$interfaceUpdateMaskFinal = i;
            this.val$updatesOnMainThreadFinal = arrayList;
            this.val$webPagesFinal = longSparseArray;
            this.val$messagesFinal = longSparseArray2;
            this.val$editingMessagesFinal = longSparseArray3;
            this.val$printChangedArg = z;
            this.val$contactsIdsFinal = arrayList2;
            this.val$chatInfoToUpdateFinal = arrayList3;
            this.val$channelViewsFinal = sparseArray;
        }
    }

    class AnonymousClass133 implements Runnable {
        final /* synthetic */ SparseIntArray val$clearHistoryMessagesFinal;
        final /* synthetic */ SparseArray val$deletedMessagesFinal;
        final /* synthetic */ SparseIntArray val$markAsReadEncryptedFinal;
        final /* synthetic */ ArrayList val$markAsReadMessagesFinal;
        final /* synthetic */ SparseLongArray val$markAsReadMessagesInboxFinal;
        final /* synthetic */ SparseLongArray val$markAsReadMessagesOutboxFinal;

        AnonymousClass133(SparseLongArray sparseLongArray, SparseLongArray sparseLongArray2, SparseIntArray sparseIntArray, ArrayList arrayList, SparseArray sparseArray, SparseIntArray sparseIntArray2) {
            this.val$markAsReadMessagesInboxFinal = sparseLongArray;
            this.val$markAsReadMessagesOutboxFinal = sparseLongArray2;
            this.val$markAsReadEncryptedFinal = sparseIntArray;
            this.val$markAsReadMessagesFinal = arrayList;
            this.val$deletedMessagesFinal = sparseArray;
            this.val$clearHistoryMessagesFinal = sparseIntArray2;
        }

        public void run() {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    int size;
                    int b;
                    int key;
                    MessageObject obj;
                    int b2;
                    long dialog_id;
                    MessageObject message;
                    int updateMask = 0;
                    if (!(AnonymousClass133.this.val$markAsReadMessagesInboxFinal == null && AnonymousClass133.this.val$markAsReadMessagesOutboxFinal == null)) {
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesRead, AnonymousClass133.this.val$markAsReadMessagesInboxFinal, AnonymousClass133.this.val$markAsReadMessagesOutboxFinal);
                        if (AnonymousClass133.this.val$markAsReadMessagesInboxFinal != null) {
                            NotificationsController.getInstance(MessagesController.this.currentAccount).processReadMessages(AnonymousClass133.this.val$markAsReadMessagesInboxFinal, 0, 0, 0, false);
                            Editor editor = MessagesController.this.notificationsPreferences.edit();
                            size = AnonymousClass133.this.val$markAsReadMessagesInboxFinal.size();
                            for (b = 0; b < size; b++) {
                                key = AnonymousClass133.this.val$markAsReadMessagesInboxFinal.keyAt(b);
                                int messageId = (int) AnonymousClass133.this.val$markAsReadMessagesInboxFinal.valueAt(b);
                                TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs_dict.get((long) key);
                                if (dialog != null && dialog.top_message > 0 && dialog.top_message <= messageId) {
                                    obj = (MessageObject) MessagesController.this.dialogMessage.get(dialog.id);
                                    if (!(obj == null || obj.isOut())) {
                                        obj.setIsRead();
                                        updateMask |= 256;
                                    }
                                }
                                if (key != UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("diditem");
                                    stringBuilder.append(key);
                                    editor.remove(stringBuilder.toString());
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("diditemo");
                                    stringBuilder.append(key);
                                    editor.remove(stringBuilder.toString());
                                }
                            }
                            editor.commit();
                        }
                        if (AnonymousClass133.this.val$markAsReadMessagesOutboxFinal != null) {
                            b = AnonymousClass133.this.val$markAsReadMessagesOutboxFinal.size();
                            for (b2 = 0; b2 < b; b2++) {
                                key = (int) AnonymousClass133.this.val$markAsReadMessagesOutboxFinal.valueAt(b2);
                                TL_dialog dialog2 = (TL_dialog) MessagesController.this.dialogs_dict.get((long) AnonymousClass133.this.val$markAsReadMessagesOutboxFinal.keyAt(b2));
                                if (dialog2 != null && dialog2.top_message > 0 && dialog2.top_message <= key) {
                                    MessageObject obj2 = (MessageObject) MessagesController.this.dialogMessage.get(dialog2.id);
                                    if (obj2 != null && obj2.isOut()) {
                                        obj2.setIsRead();
                                        updateMask |= 256;
                                    }
                                }
                            }
                        }
                    }
                    if (AnonymousClass133.this.val$markAsReadEncryptedFinal != null) {
                        b = AnonymousClass133.this.val$markAsReadEncryptedFinal.size();
                        for (b2 = 0; b2 < b; b2++) {
                            size = AnonymousClass133.this.val$markAsReadEncryptedFinal.keyAt(b2);
                            key = AnonymousClass133.this.val$markAsReadEncryptedFinal.valueAt(b2);
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesReadEncrypted, Integer.valueOf(size), Integer.valueOf(key));
                            dialog_id = ((long) size) << 32;
                            if (((TL_dialog) MessagesController.this.dialogs_dict.get(dialog_id)) != null) {
                                message = (MessageObject) MessagesController.this.dialogMessage.get(dialog_id);
                                if (message != null && message.messageOwner.date <= key) {
                                    message.setIsRead();
                                    updateMask |= 256;
                                }
                            }
                        }
                    }
                    if (AnonymousClass133.this.val$markAsReadMessagesFinal != null) {
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, AnonymousClass133.this.val$markAsReadMessagesFinal);
                    }
                    if (AnonymousClass133.this.val$deletedMessagesFinal != null) {
                        b = AnonymousClass133.this.val$deletedMessagesFinal.size();
                        for (b2 = 0; b2 < b; b2++) {
                            size = AnonymousClass133.this.val$deletedMessagesFinal.keyAt(b2);
                            ArrayList<Integer> arrayList = (ArrayList) AnonymousClass133.this.val$deletedMessagesFinal.valueAt(b2);
                            if (arrayList != null) {
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, arrayList, Integer.valueOf(size));
                                int size2;
                                if (size == 0) {
                                    size2 = arrayList.size();
                                    for (messageId = 0; messageId < size2; messageId++) {
                                        message = (MessageObject) MessagesController.this.dialogMessagesByIds.get(((Integer) arrayList.get(messageId)).intValue());
                                        if (message != null) {
                                            message.deleted = true;
                                        }
                                    }
                                } else {
                                    MessageObject obj3 = (MessageObject) MessagesController.this.dialogMessage.get((long) (-size));
                                    if (obj3 != null) {
                                        int size22 = arrayList.size();
                                        for (size2 = 0; size2 < size22; size2++) {
                                            if (obj3.getId() == ((Integer) arrayList.get(size2)).intValue()) {
                                                obj3.deleted = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        NotificationsController.getInstance(MessagesController.this.currentAccount).removeDeletedMessagesFromNotifications(AnonymousClass133.this.val$deletedMessagesFinal);
                    }
                    if (AnonymousClass133.this.val$clearHistoryMessagesFinal != null) {
                        b = AnonymousClass133.this.val$clearHistoryMessagesFinal.size();
                        for (b2 = 0; b2 < b; b2++) {
                            size = AnonymousClass133.this.val$clearHistoryMessagesFinal.keyAt(b2);
                            key = AnonymousClass133.this.val$clearHistoryMessagesFinal.valueAt(b2);
                            dialog_id = (long) (-size);
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.historyCleared, Long.valueOf(dialog_id), Integer.valueOf(key));
                            obj = (MessageObject) MessagesController.this.dialogMessage.get(dialog_id);
                            if (obj != null && obj.getId() <= key) {
                                obj.deleted = true;
                                break;
                            }
                        }
                        NotificationsController.getInstance(MessagesController.this.currentAccount).removeDeletedHisoryFromNotifications(AnonymousClass133.this.val$clearHistoryMessagesFinal);
                    }
                    if (updateMask != 0) {
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(updateMask));
                    }
                }
            });
        }
    }

    class AnonymousClass134 implements Runnable {
        final /* synthetic */ ArrayList val$arrayList;
        final /* synthetic */ int val$key;

        AnonymousClass134(ArrayList arrayList, int i) {
            this.val$arrayList = arrayList;
            this.val$key = i;
        }

        public void run() {
            MessagesStorage.getInstance(MessagesController.this.currentAccount).updateDialogsWithDeletedMessages(this.val$arrayList, MessagesStorage.getInstance(MessagesController.this.currentAccount).markMessagesAsDeleted(this.val$arrayList, false, this.val$key), false, this.val$key);
        }
    }

    class AnonymousClass135 implements Runnable {
        final /* synthetic */ int val$id;
        final /* synthetic */ int val$key;

        AnonymousClass135(int i, int i2) {
            this.val$key = i;
            this.val$id = i2;
        }

        public void run() {
            MessagesStorage.getInstance(MessagesController.this.currentAccount).updateDialogsWithDeletedMessages(new ArrayList(), MessagesStorage.getInstance(MessagesController.this.currentAccount).markMessagesAsDeleted(this.val$key, this.val$id, false), false, this.val$key);
        }
    }

    class AnonymousClass137 implements OnClickListener {
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ int val$reqId;

        AnonymousClass137(int i, BaseFragment baseFragment) {
            this.val$reqId = i;
            this.val$fragment = baseFragment;
        }

        public void onClick(DialogInterface dialog, int which) {
            ConnectionsManager.getInstance(MessagesController.this.currentAccount).cancelRequest(this.val$reqId, true);
            try {
                dialog.dismiss();
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (this.val$fragment != null) {
                this.val$fragment.setVisibleDialog(null);
            }
        }
    }

    class AnonymousClass56 implements Runnable {
        final /* synthetic */ LongSparseArray val$newPrintingStrings;
        final /* synthetic */ LongSparseArray val$newPrintingStringsTypes;

        AnonymousClass56(LongSparseArray longSparseArray, LongSparseArray longSparseArray2) {
            this.val$newPrintingStrings = longSparseArray;
            this.val$newPrintingStringsTypes = longSparseArray2;
        }

        public void run() {
            MessagesController.this.printingStrings = this.val$newPrintingStrings;
            MessagesController.this.printingStringsTypes = this.val$newPrintingStringsTypes;
        }
    }

    public static class PrintingUser {
        public SendMessageAction action;
        public long lastTime;
        public int userId;
    }

    private class ReadTask {
        public long dialogId;
        public int maxDate;
        public int maxId;
        public long sendRequestTime;

        private ReadTask() {
        }
    }

    class AnonymousClass136 implements RequestDelegate {
        final /* synthetic */ Bundle val$bundle;
        final /* synthetic */ BaseFragment val$fragment;
        final /* synthetic */ AlertDialog val$progressDialog;

        AnonymousClass136(AlertDialog alertDialog, BaseFragment baseFragment, Bundle bundle) {
            this.val$progressDialog = alertDialog;
            this.val$fragment = baseFragment;
            this.val$bundle = bundle;
        }

        public void run(final TLObject response, TL_error error) {
            if (response != null) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        try {
                            AnonymousClass136.this.val$progressDialog.dismiss();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        messages_Messages res = response;
                        MessagesController.this.putUsers(res.users, false);
                        MessagesController.this.putChats(res.chats, false);
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, res.chats, true, true);
                        AnonymousClass136.this.val$fragment.presentFragment(new ChatActivity(AnonymousClass136.this.val$bundle), true);
                    }
                });
            }
        }
    }

    class AnonymousClass15 implements RequestDelegate {
        final /* synthetic */ Chat val$chat;
        final /* synthetic */ int val$chat_id;
        final /* synthetic */ int val$classGuid;
        final /* synthetic */ long val$dialog_id;

        AnonymousClass15(Chat chat, long j, int i, int i2) {
            this.val$chat = chat;
            this.val$dialog_id = j;
            this.val$chat_id = i;
            this.val$classGuid = i2;
        }

        public void run(TLObject response, final TL_error error) {
            if (error == null) {
                final TL_messages_chatFull res = (TL_messages_chatFull) response;
                MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, res.chats, true, true);
                MessagesStorage.getInstance(MessagesController.this.currentAccount).updateChatInfo(res.full_chat, false);
                if (ChatObject.isChannel(this.val$chat)) {
                    ArrayList<Update> arrayList;
                    Integer value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(this.val$dialog_id));
                    if (value == null) {
                        value = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, this.val$dialog_id));
                    }
                    MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(this.val$dialog_id), Integer.valueOf(Math.max(res.full_chat.read_inbox_max_id, value.intValue())));
                    if (value.intValue() == 0) {
                        arrayList = new ArrayList();
                        TL_updateReadChannelInbox update = new TL_updateReadChannelInbox();
                        update.channel_id = this.val$chat_id;
                        update.max_id = res.full_chat.read_inbox_max_id;
                        arrayList.add(update);
                        MessagesController.this.processUpdateArray(arrayList, null, null, false);
                    }
                    value = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(this.val$dialog_id));
                    if (value == null) {
                        value = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, this.val$dialog_id));
                    }
                    MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(this.val$dialog_id), Integer.valueOf(Math.max(res.full_chat.read_outbox_max_id, value.intValue())));
                    if (value.intValue() == 0) {
                        arrayList = new ArrayList();
                        TL_updateReadChannelOutbox update2 = new TL_updateReadChannelOutbox();
                        update2.channel_id = this.val$chat_id;
                        update2.max_id = res.full_chat.read_outbox_max_id;
                        arrayList.add(update2);
                        MessagesController.this.processUpdateArray(arrayList, null, null, false);
                    }
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        MessagesController.this.applyDialogNotificationsSettings((long) (-AnonymousClass15.this.val$chat_id), res.full_chat.notify_settings);
                        for (int a = 0; a < res.full_chat.bot_info.size(); a++) {
                            DataQuery.getInstance(MessagesController.this.currentAccount).putBotInfo((BotInfo) res.full_chat.bot_info.get(a));
                        }
                        MessagesController.this.exportedChats.put(AnonymousClass15.this.val$chat_id, res.full_chat.exported_invite);
                        MessagesController.this.loadingFullChats.remove(Integer.valueOf(AnonymousClass15.this.val$chat_id));
                        MessagesController.this.loadedFullChats.add(Integer.valueOf(AnonymousClass15.this.val$chat_id));
                        MessagesController.this.putUsers(res.users, false);
                        MessagesController.this.putChats(res.chats, false);
                        if (res.full_chat.stickerset != null) {
                            DataQuery.getInstance(MessagesController.this.currentAccount).getGroupStickerSetById(res.full_chat.stickerset);
                        }
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, res.full_chat, Integer.valueOf(AnonymousClass15.this.val$classGuid), Boolean.valueOf(false), null);
                    }
                });
                return;
            }
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    MessagesController.this.checkChannelError(error.text, AnonymousClass15.this.val$chat_id);
                    MessagesController.this.loadingFullChats.remove(Integer.valueOf(AnonymousClass15.this.val$chat_id));
                }
            });
        }
    }

    private class UserActionUpdatesPts extends Updates {
        private UserActionUpdatesPts() {
        }
    }

    private class UserActionUpdatesSeq extends Updates {
        private UserActionUpdatesSeq() {
        }
    }

    private void processChannelsUpdatesQueue(int r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.processChannelsUpdatesQueue(int, int):void
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
        r0 = r13.updatesQueueChannels;
        r0 = r0.get(r14);
        r0 = (java.util.ArrayList) r0;
        if (r0 != 0) goto L_0x000b;
    L_0x000a:
        return;
    L_0x000b:
        r1 = r13.channelsPts;
        r1 = r1.get(r14);
        r2 = r0.isEmpty();
        if (r2 != 0) goto L_0x0106;
    L_0x0017:
        if (r1 != 0) goto L_0x001b;
    L_0x0019:
        goto L_0x0106;
    L_0x001b:
        r2 = new org.telegram.messenger.MessagesController$109;
        r2.<init>();
        java.util.Collections.sort(r0, r2);
        r2 = 0;
        r3 = 2;
        r4 = 0;
        if (r15 != r3) goto L_0x0035;
    L_0x0028:
        r3 = r13.channelsPts;
        r5 = r0.get(r4);
        r5 = (org.telegram.tgnet.TLRPC.Updates) r5;
        r5 = r5.pts;
        r3.put(r14, r5);
        r3 = r4;
        r4 = r0.size();
        if (r3 >= r4) goto L_0x00de;
        r4 = r0.get(r3);
        r4 = (org.telegram.tgnet.TLRPC.Updates) r4;
        r5 = r4.pts;
        r6 = 1;
        if (r5 > r1) goto L_0x004a;
        r5 = 2;
        goto L_0x0054;
        r5 = r4.pts_count;
        r5 = r5 + r1;
        r7 = r4.pts;
        if (r5 != r7) goto L_0x0053;
        r5 = 0;
        goto L_0x0049;
        r5 = r6;
        if (r5 != 0) goto L_0x0061;
        r13.processUpdates(r4, r6);
        r2 = 1;
        r0.remove(r3);
        r3 = r3 + -1;
        goto L_0x00da;
        if (r5 != r6) goto L_0x00d5;
        r6 = r13.updatesStartWaitTimeChannels;
        r6 = r6.get(r14);
        r8 = 0;
        r10 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r10 == 0) goto L_0x00aa;
        if (r2 != 0) goto L_0x0081;
        r8 = java.lang.System.currentTimeMillis();
        r10 = r8 - r6;
        r8 = java.lang.Math.abs(r10);
        r10 = 1500; // 0x5dc float:2.102E-42 double:7.41E-321;
        r12 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r12 > 0) goto L_0x00aa;
        r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r8 == 0) goto L_0x009e;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "HOLE IN CHANNEL ";
        r8.append(r9);
        r8.append(r14);
        r9 = " UPDATES QUEUE - will wait more time";
        r8.append(r9);
        r8 = r8.toString();
        org.telegram.messenger.FileLog.d(r8);
        if (r2 == 0) goto L_0x00a9;
        r8 = r13.updatesStartWaitTimeChannels;
        r9 = java.lang.System.currentTimeMillis();
        r8.put(r14, r9);
        return;
        r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r8 == 0) goto L_0x00c7;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "HOLE IN CHANNEL ";
        r8.append(r9);
        r8.append(r14);
        r9 = " UPDATES QUEUE - getChannelDifference ";
        r8.append(r9);
        r8 = r8.toString();
        org.telegram.messenger.FileLog.d(r8);
        r8 = r13.updatesStartWaitTimeChannels;
        r8.delete(r14);
        r8 = r13.updatesQueueChannels;
        r8.remove(r14);
        r13.getChannelDifference(r14);
        return;
        r0.remove(r3);
        r3 = r3 + -1;
        r4 = r3 + 1;
        goto L_0x0036;
        r3 = r13.updatesQueueChannels;
        r3.remove(r14);
        r3 = r13.updatesStartWaitTimeChannels;
        r3.delete(r14);
        r3 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r3 == 0) goto L_0x0105;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "UPDATES CHANNEL ";
        r3.append(r4);
        r3.append(r14);
        r4 = " QUEUE PROCEED - OK";
        r3.append(r4);
        r3 = r3.toString();
        org.telegram.messenger.FileLog.d(r3);
        return;
    L_0x0106:
        r2 = r13.updatesQueueChannels;
        r2.remove(r14);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.processChannelsUpdatesQueue(int, int):void");
    }

    private void processUpdatesQueue(int r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.processUpdatesQueue(int, int):void
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
        r0 = r16;
        r1 = r17;
        r2 = 0;
        r3 = 2;
        r4 = 1;
        if (r1 != 0) goto L_0x0014;
    L_0x0009:
        r2 = r0.updatesQueueSeq;
        r5 = new org.telegram.messenger.MessagesController$110;
        r5.<init>();
        java.util.Collections.sort(r2, r5);
        goto L_0x002d;
    L_0x0014:
        if (r1 != r4) goto L_0x0021;
    L_0x0016:
        r2 = r0.updatesQueuePts;
        r5 = new org.telegram.messenger.MessagesController$111;
        r5.<init>();
        java.util.Collections.sort(r2, r5);
        goto L_0x002d;
    L_0x0021:
        if (r1 != r3) goto L_0x002d;
    L_0x0023:
        r2 = r0.updatesQueueQts;
        r5 = new org.telegram.messenger.MessagesController$112;
        r5.<init>();
        java.util.Collections.sort(r2, r5);
    L_0x002d:
        r5 = 0;
        if (r2 == 0) goto L_0x00e9;
    L_0x0031:
        r7 = r2.isEmpty();
        if (r7 != 0) goto L_0x00e9;
    L_0x0037:
        r7 = 0;
        r8 = 0;
        r9 = r18;
        if (r9 != r3) goto L_0x006c;
    L_0x003d:
        r3 = r2.get(r8);
        r3 = (org.telegram.tgnet.TLRPC.Updates) r3;
        if (r1 != 0) goto L_0x0053;
        r10 = r0.currentAccount;
        r10 = org.telegram.messenger.MessagesStorage.getInstance(r10);
        r11 = r0.getUpdateSeq(r3);
        r10.setLastSeqValue(r11);
        goto L_0x006c;
        if (r1 != r4) goto L_0x0061;
        r10 = r0.currentAccount;
        r10 = org.telegram.messenger.MessagesStorage.getInstance(r10);
        r11 = r3.pts;
        r10.setLastPtsValue(r11);
        goto L_0x006c;
        r10 = r0.currentAccount;
        r10 = org.telegram.messenger.MessagesStorage.getInstance(r10);
        r11 = r3.pts;
        r10.setLastQtsValue(r11);
        r3 = r8;
        r8 = r2.size();
        if (r3 >= r8) goto L_0x00dc;
        r8 = r2.get(r3);
        r8 = (org.telegram.tgnet.TLRPC.Updates) r8;
        r10 = r0.isValidUpdate(r8, r1);
        if (r10 != 0) goto L_0x008a;
        r0.processUpdates(r8, r4);
        r7 = 1;
        r2.remove(r3);
        r3 = r3 + -1;
        goto L_0x00d7;
        if (r10 != r4) goto L_0x00d2;
        r11 = r16.getUpdatesStartTime(r17);
        r4 = (r11 > r5 ? 1 : (r11 == r5 ? 0 : -1));
        if (r4 == 0) goto L_0x00bd;
        if (r7 != 0) goto L_0x00aa;
        r11 = java.lang.System.currentTimeMillis();
        r13 = r16.getUpdatesStartTime(r17);
        r5 = r11 - r13;
        r4 = java.lang.Math.abs(r5);
        r11 = 1500; // 0x5dc float:2.102E-42 double:7.41E-321;
        r6 = (r4 > r11 ? 1 : (r4 == r11 ? 0 : -1));
        if (r6 > 0) goto L_0x00bd;
        r4 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r4 == 0) goto L_0x00b3;
        r4 = "HOLE IN UPDATES QUEUE - will wait more time";
        org.telegram.messenger.FileLog.d(r4);
        if (r7 == 0) goto L_0x00bc;
        r4 = java.lang.System.currentTimeMillis();
        r0.setUpdatesStartTime(r1, r4);
        return;
        r4 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r4 == 0) goto L_0x00c6;
        r4 = "HOLE IN UPDATES QUEUE - getDifference";
        org.telegram.messenger.FileLog.d(r4);
        r4 = 0;
        r0.setUpdatesStartTime(r1, r4);
        r2.clear();
        r16.getDifference();
        return;
        r2.remove(r3);
        r3 = r3 + -1;
        r8 = r3 + 1;
        r5 = 0;
        goto L_0x006d;
        r2.clear();
        r3 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r3 == 0) goto L_0x00eb;
        r3 = "UPDATES QUEUE PROCEED - OK";
        org.telegram.messenger.FileLog.d(r3);
        goto L_0x00eb;
    L_0x00e9:
        r9 = r18;
        r3 = 0;
        r0.setUpdatesStartTime(r1, r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.processUpdatesQueue(int, int):void");
    }

    private void updatePrintingStrings() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.updatePrintingStrings():void
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
        r1 = new android.util.LongSparseArray;
        r1.<init>();
        r2 = new android.util.LongSparseArray;
        r2.<init>();
        r3 = new java.util.ArrayList;
        r4 = r0.printingUsers;
        r4 = r4.keySet();
        r3.<init>(r4);
        r4 = r0.printingUsers;
        r4 = r4.entrySet();
        r4 = r4.iterator();
        r5 = r4.hasNext();
        if (r5 == 0) goto L_0x02d0;
    L_0x0027:
        r5 = r4.next();
        r5 = (java.util.Map.Entry) r5;
        r6 = r5.getKey();
        r6 = (java.lang.Long) r6;
        r6 = r6.longValue();
        r8 = r5.getValue();
        r8 = (java.util.ArrayList) r8;
        r9 = (int) r6;
        r13 = 1;
        if (r9 > 0) goto L_0x0104;
    L_0x0041:
        if (r9 == 0) goto L_0x0104;
    L_0x0043:
        r14 = r8.size();
        if (r14 != r13) goto L_0x004e;
    L_0x0049:
        r18 = r3;
        r3 = 0;
        goto L_0x0107;
    L_0x004e:
        r14 = 0;
        r15 = new java.lang.StringBuilder;
        r15.<init>();
        r10 = r8.iterator();
        r17 = r10.hasNext();
        if (r17 == 0) goto L_0x008d;
    L_0x005e:
        r17 = r10.next();
        r12 = r17;
        r12 = (org.telegram.messenger.MessagesController.PrintingUser) r12;
        r13 = r12.userId;
        r13 = java.lang.Integer.valueOf(r13);
        r13 = r0.getUser(r13);
        if (r13 == 0) goto L_0x0086;
    L_0x0072:
        r17 = r15.length();
        if (r17 == 0) goto L_0x007d;
    L_0x0078:
        r11 = ", ";
        r15.append(r11);
    L_0x007d:
        r11 = r0.getUserNameForTyping(r13);
        r15.append(r11);
        r14 = r14 + 1;
    L_0x0086:
        r11 = 2;
        if (r14 != r11) goto L_0x008a;
    L_0x0089:
        goto L_0x008d;
        r13 = 1;
        goto L_0x0058;
    L_0x008d:
        r10 = r15.length();
        if (r10 == 0) goto L_0x0100;
        r10 = 1;
        if (r14 != r10) goto L_0x00af;
        r11 = "IsTypingGroup";
        r10 = new java.lang.Object[r10];
        r12 = r15.toString();
        r13 = 0;
        r10[r13] = r12;
        r12 = 2131493708; // 0x7f0c034c float:1.8610904E38 double:1.0530978154E-314;
        r10 = org.telegram.messenger.LocaleController.formatString(r11, r12, r10);
        r1.put(r6, r10);
        r18 = r3;
        r3 = 0;
        goto L_0x00f7;
        r10 = r8.size();
        r11 = 2;
        if (r10 <= r11) goto L_0x00df;
        r10 = "AndMoreTypingGroup";
        r12 = r8.size();
        r12 = r12 - r11;
        r10 = org.telegram.messenger.LocaleController.getPluralString(r10, r12);
        r12 = new java.lang.Object[r11];
        r13 = r15.toString();
        r16 = 0;
        r12[r16] = r13;
        r13 = r8.size();
        r13 = r13 - r11;
        r11 = java.lang.Integer.valueOf(r13);
        r13 = 1;
        r12[r13] = r11;
        r11 = java.lang.String.format(r10, r12);
        r1.put(r6, r11);
        goto L_0x00ab;
        r13 = 1;
        r10 = "AreTypingGroup";
        r11 = 2131492997; // 0x7f0c0085 float:1.8609462E38 double:1.053097464E-314;
        r12 = new java.lang.Object[r13];
        r13 = r15.toString();
        r18 = r3;
        r3 = 0;
        r12[r3] = r13;
        r10 = org.telegram.messenger.LocaleController.formatString(r10, r11, r12);
        r1.put(r6, r10);
        r3 = java.lang.Integer.valueOf(r3);
        r2.put(r6, r3);
        goto L_0x02ce;
        r18 = r3;
        goto L_0x02ce;
    L_0x0104:
        r18 = r3;
        r3 = 0;
    L_0x0107:
        r10 = r8.get(r3);
        r3 = r10;
        r3 = (org.telegram.messenger.MessagesController.PrintingUser) r3;
        r10 = r3.userId;
        r10 = java.lang.Integer.valueOf(r10);
        r10 = r0.getUser(r10);
        if (r10 != 0) goto L_0x011f;
        r3 = r18;
        goto L_0x0021;
        r11 = r3.action;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageRecordAudioAction;
        if (r11 == 0) goto L_0x0154;
        if (r9 >= 0) goto L_0x013e;
        r11 = "IsRecordingAudio";
        r12 = 2131493700; // 0x7f0c0344 float:1.8610888E38 double:1.0530978115E-314;
        r13 = 1;
        r14 = new java.lang.Object[r13];
        r13 = r0.getUserNameForTyping(r10);
        r15 = 0;
        r14[r15] = r13;
        r11 = org.telegram.messenger.LocaleController.formatString(r11, r12, r14);
        r1.put(r6, r11);
        goto L_0x014a;
        r11 = "RecordingAudio";
        r12 = 2131494225; // 0x7f0c0551 float:1.8611952E38 double:1.053098071E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r1.put(r6, r11);
        r11 = 1;
        r11 = java.lang.Integer.valueOf(r11);
        r2.put(r6, r11);
        goto L_0x02cd;
        r11 = r3.action;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageRecordRoundAction;
        if (r11 != 0) goto L_0x02a0;
        r11 = r3.action;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageUploadRoundAction;
        if (r11 == 0) goto L_0x0162;
        goto L_0x02a0;
        r11 = r3.action;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageUploadAudioAction;
        if (r11 == 0) goto L_0x0197;
        if (r9 >= 0) goto L_0x0181;
        r11 = "IsSendingAudio";
        r12 = 2131493702; // 0x7f0c0346 float:1.8610892E38 double:1.0530978125E-314;
        r13 = 1;
        r13 = new java.lang.Object[r13];
        r14 = r0.getUserNameForTyping(r10);
        r15 = 0;
        r13[r15] = r14;
        r11 = org.telegram.messenger.LocaleController.formatString(r11, r12, r13);
        r1.put(r6, r11);
        goto L_0x018d;
        r11 = "SendingAudio";
        r12 = 2131494354; // 0x7f0c05d2 float:1.8612214E38 double:1.0530981346E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r1.put(r6, r11);
        r11 = 2;
        r11 = java.lang.Integer.valueOf(r11);
        r2.put(r6, r11);
        goto L_0x02cd;
        r11 = r3.action;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageUploadVideoAction;
        if (r11 != 0) goto L_0x0272;
        r11 = r3.action;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageRecordVideoAction;
        if (r11 == 0) goto L_0x01a5;
        goto L_0x0272;
        r11 = r3.action;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageUploadDocumentAction;
        if (r11 == 0) goto L_0x01da;
        if (r9 >= 0) goto L_0x01c4;
        r11 = "IsSendingFile";
        r12 = 2131493703; // 0x7f0c0347 float:1.8610894E38 double:1.053097813E-314;
        r13 = 1;
        r13 = new java.lang.Object[r13];
        r14 = r0.getUserNameForTyping(r10);
        r15 = 0;
        r13[r15] = r14;
        r11 = org.telegram.messenger.LocaleController.formatString(r11, r12, r13);
        r1.put(r6, r11);
        goto L_0x01d0;
        r11 = "SendingFile";
        r12 = 2131494355; // 0x7f0c05d3 float:1.8612216E38 double:1.053098135E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r1.put(r6, r11);
        r11 = 2;
        r11 = java.lang.Integer.valueOf(r11);
        r2.put(r6, r11);
        goto L_0x02cd;
        r11 = r3.action;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageUploadPhotoAction;
        if (r11 == 0) goto L_0x020f;
        if (r9 >= 0) goto L_0x01f9;
        r11 = "IsSendingPhoto";
        r12 = 2131493705; // 0x7f0c0349 float:1.8610898E38 double:1.053097814E-314;
        r13 = 1;
        r13 = new java.lang.Object[r13];
        r14 = r0.getUserNameForTyping(r10);
        r15 = 0;
        r13[r15] = r14;
        r11 = org.telegram.messenger.LocaleController.formatString(r11, r12, r13);
        r1.put(r6, r11);
        goto L_0x0205;
        r11 = "SendingPhoto";
        r12 = 2131494358; // 0x7f0c05d6 float:1.8612222E38 double:1.0530981366E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r1.put(r6, r11);
        r11 = 2;
        r11 = java.lang.Integer.valueOf(r11);
        r2.put(r6, r11);
        goto L_0x02cd;
        r11 = r3.action;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageGamePlayAction;
        if (r11 == 0) goto L_0x0244;
        if (r9 >= 0) goto L_0x022e;
        r11 = "IsSendingGame";
        r12 = 2131493704; // 0x7f0c0348 float:1.8610896E38 double:1.0530978135E-314;
        r13 = 1;
        r13 = new java.lang.Object[r13];
        r14 = r0.getUserNameForTyping(r10);
        r15 = 0;
        r13[r15] = r14;
        r11 = org.telegram.messenger.LocaleController.formatString(r11, r12, r13);
        r1.put(r6, r11);
        goto L_0x023a;
        r11 = "SendingGame";
        r12 = 2131494356; // 0x7f0c05d4 float:1.8612218E38 double:1.0530981356E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r1.put(r6, r11);
        r11 = 3;
        r11 = java.lang.Integer.valueOf(r11);
        r2.put(r6, r11);
        goto L_0x02cd;
        if (r9 >= 0) goto L_0x025d;
        r11 = "IsTypingGroup";
        r12 = 1;
        r12 = new java.lang.Object[r12];
        r13 = r0.getUserNameForTyping(r10);
        r14 = 0;
        r12[r14] = r13;
        r13 = 2131493708; // 0x7f0c034c float:1.8610904E38 double:1.0530978154E-314;
        r11 = org.telegram.messenger.LocaleController.formatString(r11, r13, r12);
        r1.put(r6, r11);
        goto L_0x026a;
        r14 = 0;
        r11 = "Typing";
        r12 = 2131494503; // 0x7f0c0667 float:1.8612516E38 double:1.053098208E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r1.put(r6, r11);
        r11 = java.lang.Integer.valueOf(r14);
        r2.put(r6, r11);
        goto L_0x02cd;
        r14 = 0;
        if (r9 >= 0) goto L_0x028b;
        r11 = "IsSendingVideo";
        r12 = 2131493706; // 0x7f0c034a float:1.86109E38 double:1.0530978145E-314;
        r13 = 1;
        r13 = new java.lang.Object[r13];
        r15 = r0.getUserNameForTyping(r10);
        r13[r14] = r15;
        r11 = org.telegram.messenger.LocaleController.formatString(r11, r12, r13);
        r1.put(r6, r11);
        goto L_0x0297;
        r11 = "SendingVideoStatus";
        r12 = 2131494360; // 0x7f0c05d8 float:1.8612226E38 double:1.0530981376E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r1.put(r6, r11);
        r11 = 2;
        r11 = java.lang.Integer.valueOf(r11);
        r2.put(r6, r11);
        goto L_0x02cd;
        if (r9 >= 0) goto L_0x02b9;
        r11 = "IsRecordingRound";
        r12 = 2131493701; // 0x7f0c0345 float:1.861089E38 double:1.053097812E-314;
        r13 = 1;
        r13 = new java.lang.Object[r13];
        r14 = r0.getUserNameForTyping(r10);
        r15 = 0;
        r13[r15] = r14;
        r11 = org.telegram.messenger.LocaleController.formatString(r11, r12, r13);
        r1.put(r6, r11);
        goto L_0x02c5;
        r11 = "RecordingRound";
        r12 = 2131494226; // 0x7f0c0552 float:1.8611954E38 double:1.0530980714E-314;
        r11 = org.telegram.messenger.LocaleController.getString(r11, r12);
        r1.put(r6, r11);
        r11 = 4;
        r11 = java.lang.Integer.valueOf(r11);
        r2.put(r6, r11);
        goto L_0x011b;
    L_0x02d0:
        r18 = r3;
        r3 = r1.size();
        r0.lastPrintingStringCount = r3;
        r3 = new org.telegram.messenger.MessagesController$56;
        r3.<init>(r1, r2);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.updatePrintingStrings():void");
    }

    public void changeChatAvatar(int r1, org.telegram.tgnet.TLRPC.InputFile r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.changeChatAvatar(int, org.telegram.tgnet.TLRPC$InputFile):void
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
        r0 = r4.currentAccount;
        r0 = org.telegram.messenger.ChatObject.isChannel(r5, r0);
        if (r0 == 0) goto L_0x002a;
    L_0x0008:
        r0 = new org.telegram.tgnet.TLRPC$TL_channels_editPhoto;
        r0.<init>();
        r1 = r4.getInputChannel(r5);
        r0.channel = r1;
        if (r6 == 0) goto L_0x0021;
    L_0x0015:
        r1 = new org.telegram.tgnet.TLRPC$TL_inputChatUploadedPhoto;
        r1.<init>();
        r0.photo = r1;
        r1 = r0.photo;
        r1.file = r6;
        goto L_0x0028;
    L_0x0021:
        r1 = new org.telegram.tgnet.TLRPC$TL_inputChatPhotoEmpty;
        r1.<init>();
        r0.photo = r1;
        goto L_0x0047;
    L_0x002a:
        r0 = new org.telegram.tgnet.TLRPC$TL_messages_editChatPhoto;
        r0.<init>();
        r0.chat_id = r5;
        if (r6 == 0) goto L_0x003f;
        r1 = new org.telegram.tgnet.TLRPC$TL_inputChatUploadedPhoto;
        r1.<init>();
        r0.photo = r1;
        r1 = r0.photo;
        r1.file = r6;
        goto L_0x0046;
        r1 = new org.telegram.tgnet.TLRPC$TL_inputChatPhotoEmpty;
        r1.<init>();
        r0.photo = r1;
        r1 = r4.currentAccount;
        r1 = org.telegram.tgnet.ConnectionsManager.getInstance(r1);
        r2 = new org.telegram.messenger.MessagesController$103;
        r2.<init>();
        r3 = 64;
        r1.sendRequest(r0, r2, r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.changeChatAvatar(int, org.telegram.tgnet.TLRPC$InputFile):void");
    }

    public boolean checkCanOpenChat(android.os.Bundle r1, org.telegram.ui.ActionBar.BaseFragment r2, org.telegram.messenger.MessageObject r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.checkCanOpenChat(android.os.Bundle, org.telegram.ui.ActionBar.BaseFragment, org.telegram.messenger.MessageObject):boolean
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
        r0 = r18;
        r1 = r19;
        r2 = r20;
        r3 = 1;
        if (r1 == 0) goto L_0x00fb;
    L_0x0009:
        if (r2 != 0) goto L_0x000d;
    L_0x000b:
        goto L_0x00fb;
    L_0x000d:
        r4 = 0;
        r5 = 0;
        r6 = "user_id";
        r7 = 0;
        r6 = r1.getInt(r6, r7);
        r8 = "chat_id";
        r8 = r1.getInt(r8, r7);
        r9 = "message_id";
        r9 = r1.getInt(r9, r7);
        if (r6 == 0) goto L_0x002d;
    L_0x0024:
        r10 = java.lang.Integer.valueOf(r6);
        r4 = r0.getUser(r10);
        goto L_0x0037;
    L_0x002d:
        if (r8 == 0) goto L_0x0037;
    L_0x002f:
        r10 = java.lang.Integer.valueOf(r8);
        r5 = r0.getChat(r10);
    L_0x0037:
        if (r4 != 0) goto L_0x003c;
    L_0x0039:
        if (r5 != 0) goto L_0x003c;
    L_0x003b:
        return r3;
    L_0x003c:
        r10 = 0;
        if (r5 == 0) goto L_0x0046;
    L_0x003f:
        r11 = r5.restriction_reason;
        r10 = getRestrictionReason(r11);
        goto L_0x004e;
    L_0x0046:
        if (r4 == 0) goto L_0x004e;
    L_0x0048:
        r11 = r4.restriction_reason;
        r10 = getRestrictionReason(r11);
    L_0x004e:
        if (r10 == 0) goto L_0x0054;
    L_0x0050:
        showCantOpenAlert(r2, r10);
        return r7;
    L_0x0054:
        if (r9 == 0) goto L_0x00fa;
    L_0x0056:
        if (r21 == 0) goto L_0x00fa;
    L_0x0058:
        if (r5 == 0) goto L_0x00fa;
    L_0x005a:
        r12 = r5.access_hash;
        r14 = 0;
        r16 = (r12 > r14 ? 1 : (r12 == r14 ? 0 : -1));
        if (r16 != 0) goto L_0x00fa;
    L_0x0062:
        r12 = r21.getDialogId();
        r12 = (int) r12;
        if (r12 == 0) goto L_0x00fa;
    L_0x0069:
        r13 = new org.telegram.ui.ActionBar.AlertDialog;
        r14 = r20.getParentActivity();
        r13.<init>(r14, r3);
        r3 = r13;
        r13 = "Loading";
        r14 = 2131493762; // 0x7f0c0382 float:1.8611013E38 double:1.053097842E-314;
        r13 = org.telegram.messenger.LocaleController.getString(r13, r14);
        r3.setMessage(r13);
        r3.setCanceledOnTouchOutside(r7);
        r3.setCancelable(r7);
        if (r12 >= 0) goto L_0x0090;
    L_0x0087:
        r13 = -r12;
        r13 = java.lang.Integer.valueOf(r13);
        r5 = r0.getChat(r13);
    L_0x0090:
        if (r12 > 0) goto L_0x00bc;
    L_0x0092:
        r13 = org.telegram.messenger.ChatObject.isChannel(r5);
        if (r13 != 0) goto L_0x0099;
    L_0x0098:
        goto L_0x00bc;
    L_0x0099:
        r13 = -r12;
        r13 = java.lang.Integer.valueOf(r13);
        r5 = r0.getChat(r13);
        r13 = new org.telegram.tgnet.TLRPC$TL_channels_getMessages;
        r13.<init>();
        r14 = getInputChannel(r5);
        r13.channel = r14;
        r14 = r13.id;
        r15 = r21.getId();
        r15 = java.lang.Integer.valueOf(r15);
        r14.add(r15);
        goto L_0x00d0;
    L_0x00bc:
        r13 = new org.telegram.tgnet.TLRPC$TL_messages_getMessages;
        r13.<init>();
        r14 = r13.id;
        r15 = r21.getId();
        r15 = java.lang.Integer.valueOf(r15);
        r14.add(r15);
        r14 = r0.currentAccount;
        r14 = org.telegram.tgnet.ConnectionsManager.getInstance(r14);
        r15 = new org.telegram.messenger.MessagesController$136;
        r15.<init>(r3, r2, r1);
        r14 = r14.sendRequest(r13, r15);
        r7 = "Cancel";
        r15 = 2131493127; // 0x7f0c0107 float:1.8609725E38 double:1.0530975284E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r15);
        r15 = new org.telegram.messenger.MessagesController$137;
        r15.<init>(r14, r2);
        r0 = -2;
        r3.setButton(r0, r7, r15);
        r2.setVisibleDialog(r3);
        r3.show();
        r0 = 0;
        return r0;
    L_0x00fa:
        return r3;
    L_0x00fb:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.checkCanOpenChat(android.os.Bundle, org.telegram.ui.ActionBar.BaseFragment, org.telegram.messenger.MessageObject):boolean");
    }

    public void loadFullChat(int r1, int r2, boolean r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.loadFullChat(int, int, boolean):void
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
        r7 = r17;
        r8 = r18;
        r9 = r19;
        r0 = r7.loadedFullChats;
        r1 = java.lang.Integer.valueOf(r18);
        r10 = r0.contains(r1);
        r0 = r7.loadingFullChats;
        r1 = java.lang.Integer.valueOf(r18);
        r0 = r0.contains(r1);
        if (r0 != 0) goto L_0x009d;
    L_0x001c:
        if (r20 != 0) goto L_0x0023;
    L_0x001e:
        if (r10 == 0) goto L_0x0023;
    L_0x0020:
        r15 = r10;
        goto L_0x009e;
    L_0x0023:
        r0 = r7.loadingFullChats;
        r1 = java.lang.Integer.valueOf(r18);
        r0.add(r1);
        r0 = -r8;
        r11 = (long) r0;
        r0 = java.lang.Integer.valueOf(r18);
        r13 = r7.getChat(r0);
        r0 = org.telegram.messenger.ChatObject.isChannel(r13);
        if (r0 == 0) goto L_0x0054;
    L_0x003c:
        r0 = new org.telegram.tgnet.TLRPC$TL_channels_getFullChannel;
        r0.<init>();
        r1 = getInputChannel(r13);
        r0.channel = r1;
        r1 = r0;
        r2 = r13.megagroup;
        if (r2 == 0) goto L_0x0051;
    L_0x004c:
        r2 = r10 ^ 1;
        r7.loadChannelAdmins(r8, r2);
        r14 = r1;
        goto L_0x0079;
    L_0x0054:
        r0 = new org.telegram.tgnet.TLRPC$TL_messages_getFullChat;
        r0.<init>();
        r0.chat_id = r8;
        r1 = r0;
        r2 = r7.dialogs_read_inbox_max;
        r3 = java.lang.Long.valueOf(r11);
        r2 = r2.get(r3);
        if (r2 == 0) goto L_0x0074;
        r2 = r7.dialogs_read_outbox_max;
        r3 = java.lang.Long.valueOf(r11);
        r2 = r2.get(r3);
        if (r2 != 0) goto L_0x0052;
        r2 = 0;
        r7.reloadDialogsReadValue(r2, r11);
        goto L_0x0052;
        r0 = r7.currentAccount;
        r6 = org.telegram.tgnet.ConnectionsManager.getInstance(r0);
        r5 = new org.telegram.messenger.MessagesController$15;
        r0 = r5;
        r1 = r7;
        r2 = r13;
        r3 = r11;
        r15 = r10;
        r10 = r5;
        r5 = r8;
        r8 = r6;
        r6 = r9;
        r0.<init>(r2, r3, r5, r6);
        r0 = r8.sendRequest(r14, r10);
        if (r9 == 0) goto L_0x009c;
        r1 = r7.currentAccount;
        r1 = org.telegram.tgnet.ConnectionsManager.getInstance(r1);
        r1.bindRequestToGuid(r0, r9);
        return;
    L_0x009d:
        r15 = r10;
    L_0x009e:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.loadFullChat(int, int, boolean):void");
    }

    public boolean processUpdateArray(java.util.ArrayList<org.telegram.tgnet.TLRPC.Update> r1, java.util.ArrayList<org.telegram.tgnet.TLRPC.User> r2, java.util.ArrayList<org.telegram.tgnet.TLRPC.Chat> r3, boolean r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.processUpdateArray(java.util.ArrayList, java.util.ArrayList, java.util.ArrayList, boolean):boolean
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
        r12 = r122;
        r11 = r124;
        r15 = r125;
        r0 = r123.isEmpty();
        r14 = 1;
        if (r0 == 0) goto L_0x001a;
    L_0x000d:
        if (r11 != 0) goto L_0x0011;
    L_0x000f:
        if (r15 == 0) goto L_0x0019;
    L_0x0011:
        r0 = new org.telegram.messenger.MessagesController$127;
        r0.<init>(r11, r15);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r0);
    L_0x0019:
        return r14;
    L_0x001a:
        r0 = java.lang.System.currentTimeMillis();
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r13 = 0;
        r16 = 0;
        r17 = 0;
        r18 = 0;
        r19 = 0;
        r20 = 0;
        r21 = 0;
        r22 = 0;
        r23 = 1;
        if (r11 == 0) goto L_0x0072;
    L_0x003a:
        r14 = new java.util.concurrent.ConcurrentHashMap;
        r14.<init>();
        r25 = 0;
        r26 = r124.size();
        r27 = r2;
        r2 = r25;
    L_0x0049:
        r28 = r26;
        r29 = r3;
        r3 = r28;
        if (r2 >= r3) goto L_0x006f;
    L_0x0051:
        r25 = r11.get(r2);
        r30 = r3;
        r3 = r25;
        r3 = (org.telegram.tgnet.TLRPC.User) r3;
        r31 = r4;
        r4 = r3.id;
        r4 = java.lang.Integer.valueOf(r4);
        r14.put(r4, r3);
        r2 = r2 + 1;
        r3 = r29;
        r26 = r30;
        r4 = r31;
        goto L_0x0049;
    L_0x006f:
        r31 = r4;
        goto L_0x007c;
    L_0x0072:
        r27 = r2;
        r29 = r3;
        r31 = r4;
        r23 = 0;
        r14 = r12.users;
    L_0x007c:
        if (r15 == 0) goto L_0x00a9;
    L_0x007e:
        r2 = new java.util.concurrent.ConcurrentHashMap;
        r2.<init>();
        r3 = 0;
        r4 = r125.size();
    L_0x0088:
        if (r3 >= r4) goto L_0x00a6;
    L_0x008a:
        r25 = r15.get(r3);
        r38 = r4;
        r4 = r25;
        r4 = (org.telegram.tgnet.TLRPC.Chat) r4;
        r39 = r5;
        r5 = r4.id;
        r5 = java.lang.Integer.valueOf(r5);
        r2.put(r5, r4);
        r3 = r3 + 1;
        r4 = r38;
        r5 = r39;
        goto L_0x0088;
    L_0x00a6:
        r39 = r5;
        goto L_0x00af;
    L_0x00a9:
        r39 = r5;
        r23 = 0;
        r2 = r12.chats;
    L_0x00af:
        r3 = r2;
        if (r126 == 0) goto L_0x00b4;
    L_0x00b2:
        r23 = 0;
    L_0x00b4:
        if (r11 != 0) goto L_0x00b8;
    L_0x00b6:
        if (r15 == 0) goto L_0x00c0;
    L_0x00b8:
        r2 = new org.telegram.messenger.MessagesController$128;
        r2.<init>(r11, r15);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
    L_0x00c0:
        r2 = 0;
        r4 = 0;
        r5 = r123.size();
        r40 = r7;
        r41 = r8;
        r11 = r10;
        r10 = r13;
        r15 = r16;
        r7 = r17;
        r43 = r18;
        r44 = r21;
        r13 = r29;
        r42 = r31;
        r8 = r2;
        r2 = r9;
        r9 = r22;
        if (r4 >= r5) goto L_0x1343;
    L_0x00de:
        r45 = r5;
        r5 = r123;
        r16 = r5.get(r4);
        r5 = r16;
        r5 = (org.telegram.tgnet.TLRPC.Update) r5;
        r16 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r16 == 0) goto L_0x0107;
    L_0x00ee:
        r46 = r4;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r47 = r15;
        r15 = "process update ";
        r4.append(r15);
        r4.append(r5);
        r4 = r4.toString();
        org.telegram.messenger.FileLog.d(r4);
        goto L_0x010b;
    L_0x0107:
        r46 = r4;
        r47 = r15;
    L_0x010b:
        r4 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateNewMessage;
        r15 = 0;
        if (r4 != 0) goto L_0x0ff4;
    L_0x0110:
        r4 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage;
        if (r4 == 0) goto L_0x012a;
    L_0x0114:
        r53 = r2;
        r80 = r7;
        r66 = r8;
        r57 = r9;
        r58 = r10;
        r76 = r11;
        r55 = r13;
        r15 = r43;
        r13 = r44;
        r9 = r0;
        r8 = r3;
        goto L_0x1008;
    L_0x012a:
        r4 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateReadMessagesContents;
        if (r4 == 0) goto L_0x0173;
    L_0x012e:
        r4 = r5;
        r4 = (org.telegram.tgnet.TLRPC.TL_updateReadMessagesContents) r4;
        if (r10 != 0) goto L_0x0139;
    L_0x0133:
        r15 = new java.util.ArrayList;
        r15.<init>();
        r10 = r15;
    L_0x0139:
        r15 = 0;
        r48 = r15;
        r15 = r4.messages;
        r15 = r15.size();
        r49 = r0;
        r0 = r48;
    L_0x0146:
        if (r0 >= r15) goto L_0x0167;
    L_0x0148:
        r1 = r4.messages;
        r1 = r1.get(r0);
        r1 = (java.lang.Integer) r1;
        r1 = r1.intValue();
        r51 = r3;
        r52 = r4;
        r3 = (long) r1;
        r1 = java.lang.Long.valueOf(r3);
        r10.add(r1);
        r0 = r0 + 1;
        r3 = r51;
        r4 = r52;
        goto L_0x0146;
    L_0x0167:
        r51 = r3;
        r66 = r8;
        r15 = r47;
        r88 = r49;
        r8 = r51;
        goto L_0x1338;
    L_0x0173:
        r49 = r0;
        r51 = r3;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChannelReadMessagesContents;
        if (r0 == 0) goto L_0x01d6;
    L_0x017b:
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChannelReadMessagesContents) r0;
        if (r10 != 0) goto L_0x0186;
    L_0x0180:
        r3 = new java.util.ArrayList;
        r3.<init>();
        r10 = r3;
    L_0x0186:
        r3 = 0;
        r4 = r0.messages;
        r4 = r4.size();
    L_0x018d:
        if (r3 >= r4) goto L_0x01c2;
    L_0x018f:
        r15 = r0.messages;
        r15 = r15.get(r3);
        r15 = (java.lang.Integer) r15;
        r15 = r15.intValue();
        r53 = r2;
        r1 = (long) r15;
        r15 = r0.channel_id;
        r55 = r13;
        r54 = r14;
        r13 = (long) r15;
        r15 = 32;
        r13 = r13 << r15;
        r56 = r8;
        r57 = r9;
        r8 = r1 | r13;
        r1 = java.lang.Long.valueOf(r8);
        r10.add(r1);
        r3 = r3 + 1;
        r2 = r53;
        r14 = r54;
        r13 = r55;
        r8 = r56;
        r9 = r57;
        goto L_0x018d;
    L_0x01c2:
        r53 = r2;
        r56 = r8;
        r57 = r9;
        r55 = r13;
        r54 = r14;
        r15 = r47;
        r88 = r49;
        r8 = r51;
        r66 = r56;
        goto L_0x1338;
    L_0x01d6:
        r53 = r2;
        r56 = r8;
        r57 = r9;
        r55 = r13;
        r54 = r14;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateReadHistoryInbox;
        if (r0 == 0) goto L_0x025d;
    L_0x01e4:
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateReadHistoryInbox) r0;
        if (r53 != 0) goto L_0x01f0;
    L_0x01e9:
        r1 = new org.telegram.messenger.support.SparseLongArray;
        r1.<init>();
        r2 = r1;
        goto L_0x01f2;
    L_0x01f0:
        r2 = r53;
    L_0x01f2:
        r1 = r0.peer;
        r1 = r1.chat_id;
        if (r1 == 0) goto L_0x020a;
    L_0x01f8:
        r1 = r0.peer;
        r1 = r1.chat_id;
        r1 = -r1;
        r3 = r0.max_id;
        r3 = (long) r3;
        r2.put(r1, r3);
        r1 = r0.peer;
        r1 = r1.chat_id;
        r1 = -r1;
        r3 = (long) r1;
        goto L_0x0219;
    L_0x020a:
        r1 = r0.peer;
        r1 = r1.user_id;
        r3 = r0.max_id;
        r3 = (long) r3;
        r2.put(r1, r3);
        r1 = r0.peer;
        r1 = r1.user_id;
        r3 = (long) r1;
    L_0x0219:
        r1 = r12.dialogs_read_inbox_max;
        r8 = java.lang.Long.valueOf(r3);
        r1 = r1.get(r8);
        r1 = (java.lang.Integer) r1;
        if (r1 != 0) goto L_0x0235;
    L_0x0227:
        r8 = r12.currentAccount;
        r8 = org.telegram.messenger.MessagesStorage.getInstance(r8);
        r8 = r8.getDialogReadMax(r15, r3);
        r1 = java.lang.Integer.valueOf(r8);
    L_0x0235:
        r8 = r12.dialogs_read_inbox_max;
        r9 = java.lang.Long.valueOf(r3);
        r13 = r1.intValue();
        r14 = r0.max_id;
        r13 = java.lang.Math.max(r13, r14);
        r13 = java.lang.Integer.valueOf(r13);
        r8.put(r9, r13);
        r15 = r47;
        r88 = r49;
        r8 = r51;
    L_0x0253:
        r14 = r54;
        r13 = r55;
        r66 = r56;
    L_0x0259:
        r9 = r57;
        goto L_0x1338;
    L_0x025d:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateReadHistoryOutbox;
        if (r0 == 0) goto L_0x02c8;
    L_0x0261:
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateReadHistoryOutbox) r0;
        if (r11 != 0) goto L_0x026c;
    L_0x0266:
        r1 = new org.telegram.messenger.support.SparseLongArray;
        r1.<init>();
        r11 = r1;
    L_0x026c:
        r1 = r0.peer;
        r1 = r1.chat_id;
        if (r1 == 0) goto L_0x0284;
    L_0x0272:
        r1 = r0.peer;
        r1 = r1.chat_id;
        r1 = -r1;
        r2 = r0.max_id;
        r2 = (long) r2;
        r11.put(r1, r2);
        r1 = r0.peer;
        r1 = r1.chat_id;
        r1 = -r1;
        r1 = (long) r1;
        goto L_0x0293;
    L_0x0284:
        r1 = r0.peer;
        r1 = r1.user_id;
        r2 = r0.max_id;
        r2 = (long) r2;
        r11.put(r1, r2);
        r1 = r0.peer;
        r1 = r1.user_id;
        r1 = (long) r1;
    L_0x0293:
        r3 = r12.dialogs_read_outbox_max;
        r4 = java.lang.Long.valueOf(r1);
        r3 = r3.get(r4);
        r3 = (java.lang.Integer) r3;
        if (r3 != 0) goto L_0x02b0;
    L_0x02a1:
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r8 = 1;
        r4 = r4.getDialogReadMax(r8, r1);
        r3 = java.lang.Integer.valueOf(r4);
    L_0x02b0:
        r4 = r12.dialogs_read_outbox_max;
        r8 = java.lang.Long.valueOf(r1);
        r9 = r3.intValue();
        r13 = r0.max_id;
        r9 = java.lang.Math.max(r9, r13);
        r9 = java.lang.Integer.valueOf(r9);
        r4.put(r8, r9);
        goto L_0x02ee;
    L_0x02c8:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateDeleteMessages;
        if (r0 == 0) goto L_0x02f8;
    L_0x02cc:
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateDeleteMessages) r0;
        if (r7 != 0) goto L_0x02d7;
    L_0x02d1:
        r1 = new android.util.SparseArray;
        r1.<init>();
        r7 = r1;
    L_0x02d7:
        r1 = r7.get(r15);
        r1 = (java.util.ArrayList) r1;
        if (r1 != 0) goto L_0x02e8;
    L_0x02df:
        r2 = new java.util.ArrayList;
        r2.<init>();
        r1 = r2;
        r7.put(r15, r1);
    L_0x02e8:
        r2 = r0.messages;
        r1.addAll(r2);
    L_0x02ee:
        r15 = r47;
        r88 = r49;
        r8 = r51;
        r2 = r53;
        goto L_0x0253;
    L_0x02f8:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateUserTyping;
        r1 = 0;
        if (r0 != 0) goto L_0x0ec5;
    L_0x02fe:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChatUserTyping;
        if (r0 == 0) goto L_0x0314;
    L_0x0302:
        r58 = r10;
        r76 = r11;
        r15 = r43;
        r13 = r44;
        r68 = r49;
        r8 = r51;
        r14 = r54;
        r66 = r56;
        goto L_0x0ed5;
    L_0x0314:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChatParticipants;
        if (r0 == 0) goto L_0x0341;
    L_0x0318:
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChatParticipants) r0;
        r8 = r56 | 32;
        if (r19 != 0) goto L_0x0327;
    L_0x031f:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r19 = r1;
        goto L_0x0329;
    L_0x0327:
        r1 = r19;
    L_0x0329:
        r2 = r0.participants;
        r1.add(r2);
        r19 = r1;
    L_0x0331:
        r66 = r8;
        r15 = r47;
        r88 = r49;
        r8 = r51;
        r2 = r53;
        r14 = r54;
        r13 = r55;
        goto L_0x0259;
    L_0x0341:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateUserStatus;
        if (r0 == 0) goto L_0x0359;
    L_0x0345:
        r8 = r56 | 4;
        if (r20 != 0) goto L_0x0351;
    L_0x0349:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0353;
    L_0x0351:
        r0 = r20;
    L_0x0353:
        r0.add(r5);
    L_0x0356:
        r20 = r0;
        goto L_0x0331;
    L_0x0359:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateUserName;
        if (r0 == 0) goto L_0x036f;
    L_0x035d:
        r8 = r56 | 1;
        if (r20 != 0) goto L_0x0369;
    L_0x0361:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x036b;
    L_0x0369:
        r0 = r20;
    L_0x036b:
        r0.add(r5);
        goto L_0x0356;
    L_0x036f:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateUserPhoto;
        if (r0 == 0) goto L_0x0396;
    L_0x0373:
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateUserPhoto) r0;
        r8 = r56 | 2;
        r1 = r12.currentAccount;
        r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
        r2 = r0.user_id;
        r1.clearUserPhotos(r2);
        if (r20 != 0) goto L_0x038d;
    L_0x0385:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r20 = r1;
        goto L_0x038f;
    L_0x038d:
        r1 = r20;
    L_0x038f:
        r1.add(r5);
        r20 = r1;
        goto L_0x0331;
    L_0x0396:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateUserPhone;
        if (r0 == 0) goto L_0x03ae;
    L_0x039a:
        r0 = r56;
        r8 = r0 | 1024;
        if (r20 != 0) goto L_0x03a8;
    L_0x03a0:
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x03aa;
    L_0x03a8:
        r0 = r20;
    L_0x03aa:
        r0.add(r5);
        goto L_0x0356;
    L_0x03ae:
        r0 = r56;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateContactRegistered;
        r4 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        if (r3 == 0) goto L_0x048a;
    L_0x03b6:
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateContactRegistered) r1;
        r2 = r12.enableJoined;
        if (r2 == 0) goto L_0x0474;
    L_0x03bd:
        r2 = r1.user_id;
        r2 = java.lang.Integer.valueOf(r2);
        r14 = r54;
        r2 = r14.containsKey(r2);
        if (r2 == 0) goto L_0x0471;
    L_0x03cb:
        r2 = r12.currentAccount;
        r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);
        r3 = r1.user_id;
        r8 = (long) r3;
        r2 = r2.isDialogHasMessages(r8);
        if (r2 != 0) goto L_0x0471;
    L_0x03da:
        r2 = new org.telegram.tgnet.TLRPC$TL_messageService;
        r2.<init>();
        r3 = new org.telegram.tgnet.TLRPC$TL_messageActionUserJoined;
        r3.<init>();
        r2.action = r3;
        r3 = r12.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3 = r3.getNewMessageId();
        r2.id = r3;
        r2.local_id = r3;
        r3 = r12.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3.saveConfig(r15);
        r2.unread = r15;
        r2.flags = r4;
        r3 = r1.date;
        r2.date = r3;
        r3 = r1.user_id;
        r2.from_id = r3;
        r3 = new org.telegram.tgnet.TLRPC$TL_peerUser;
        r3.<init>();
        r2.to_id = r3;
        r3 = r2.to_id;
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.getClientUserId();
        r3.user_id = r4;
        r3 = r1.user_id;
        r3 = (long) r3;
        r2.dialog_id = r3;
        if (r6 != 0) goto L_0x042b;
    L_0x0425:
        r3 = new java.util.ArrayList;
        r3.<init>();
        r6 = r3;
    L_0x042b:
        r6.add(r2);
        r3 = new org.telegram.messenger.MessageObject;
        r4 = r12.currentAccount;
        r8 = r12.createdDialogIds;
        r58 = r10;
        r9 = r2.dialog_id;
        r9 = java.lang.Long.valueOf(r9);
        r37 = r8.contains(r9);
        r32 = r3;
        r33 = r4;
        r34 = r2;
        r35 = r14;
        r36 = r51;
        r32.<init>(r33, r34, r35, r36, r37);
        if (r55 != 0) goto L_0x0456;
    L_0x044f:
        r4 = new android.util.LongSparseArray;
        r4.<init>();
        r13 = r4;
        goto L_0x0458;
    L_0x0456:
        r13 = r55;
    L_0x0458:
        r8 = r2.dialog_id;
        r4 = r13.get(r8);
        r4 = (java.util.ArrayList) r4;
        if (r4 != 0) goto L_0x046d;
    L_0x0462:
        r8 = new java.util.ArrayList;
        r8.<init>();
        r4 = r8;
        r8 = r2.dialog_id;
        r13.put(r8, r4);
    L_0x046d:
        r4.add(r3);
        goto L_0x047a;
    L_0x0471:
        r58 = r10;
        goto L_0x0478;
    L_0x0474:
        r58 = r10;
        r14 = r54;
    L_0x0478:
        r13 = r55;
    L_0x047a:
        r66 = r0;
        r15 = r47;
        r88 = r49;
        r8 = r51;
        r2 = r53;
        r9 = r57;
        r10 = r58;
        goto L_0x1338;
    L_0x048a:
        r58 = r10;
        r14 = r54;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateContactLink;
        if (r3 == 0) goto L_0x0500;
    L_0x0492:
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateContactLink) r1;
        if (r57 != 0) goto L_0x049e;
    L_0x0497:
        r2 = new java.util.ArrayList;
        r2.<init>();
        r9 = r2;
        goto L_0x04a0;
    L_0x049e:
        r9 = r57;
    L_0x04a0:
        r2 = r1.my_link;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_contactLinkContact;
        r3 = -1;
        if (r2 == 0) goto L_0x04cd;
    L_0x04a7:
        r2 = r1.user_id;
        r2 = -r2;
        r2 = java.lang.Integer.valueOf(r2);
        r2 = r9.indexOf(r2);
        if (r2 == r3) goto L_0x04b7;
    L_0x04b4:
        r9.remove(r2);
    L_0x04b7:
        r3 = r1.user_id;
        r3 = java.lang.Integer.valueOf(r3);
        r3 = r9.contains(r3);
        if (r3 != 0) goto L_0x04cc;
    L_0x04c3:
        r3 = r1.user_id;
        r3 = java.lang.Integer.valueOf(r3);
        r9.add(r3);
    L_0x04cc:
        goto L_0x04f2;
    L_0x04cd:
        r2 = r1.user_id;
        r2 = java.lang.Integer.valueOf(r2);
        r2 = r9.indexOf(r2);
        if (r2 == r3) goto L_0x04dc;
        r9.remove(r2);
        r3 = r1.user_id;
        r3 = java.lang.Integer.valueOf(r3);
        r3 = r9.contains(r3);
        if (r3 != 0) goto L_0x04f2;
        r3 = r1.user_id;
        r3 = -r3;
        r3 = java.lang.Integer.valueOf(r3);
        r9.add(r3);
        r66 = r0;
        r15 = r47;
        r88 = r49;
        r8 = r51;
        r2 = r53;
        r13 = r55;
        goto L_0x0486;
    L_0x0500:
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateNewEncryptedMessage;
        if (r3 == 0) goto L_0x05a4;
        r1 = r12.currentAccount;
        r1 = org.telegram.messenger.SecretChatHelper.getInstance(r1);
        r2 = r5;
        r2 = (org.telegram.tgnet.TLRPC.TL_updateNewEncryptedMessage) r2;
        r2 = r2.message;
        r1 = r1.decryptMessage(r2);
        if (r1 == 0) goto L_0x05a0;
        r2 = r1.isEmpty();
        if (r2 != 0) goto L_0x05a0;
        r2 = r5;
        r2 = (org.telegram.tgnet.TLRPC.TL_updateNewEncryptedMessage) r2;
        r2 = r2.message;
        r2 = r2.chat_id;
        r3 = (long) r2;
        r8 = 32;
        r3 = r3 << r8;
        if (r55 != 0) goto L_0x052f;
        r8 = new android.util.LongSparseArray;
        r8.<init>();
        r13 = r8;
        goto L_0x0531;
        r13 = r55;
        r8 = r13.get(r3);
        r8 = (java.util.ArrayList) r8;
        if (r8 != 0) goto L_0x0542;
        r9 = new java.util.ArrayList;
        r9.<init>();
        r8 = r9;
        r13.put(r3, r8);
        r9 = 0;
        r10 = r1.size();
        if (r9 >= r10) goto L_0x05a2;
        r15 = r1.get(r9);
        r15 = (org.telegram.tgnet.TLRPC.Message) r15;
        org.telegram.messenger.ImageLoader.saveMessageThumbs(r15);
        if (r6 != 0) goto L_0x055d;
        r59 = r1;
        r1 = new java.util.ArrayList;
        r1.<init>();
        r6 = r1;
        goto L_0x055f;
        r59 = r1;
        r6.add(r15);
        r1 = new org.telegram.messenger.MessageObject;
        r60 = r2;
        r2 = r12.currentAccount;
        r61 = r6;
        r6 = r12.createdDialogIds;
        r62 = r10;
        r10 = java.lang.Long.valueOf(r3);
        r37 = r6.contains(r10);
        r32 = r1;
        r33 = r2;
        r34 = r15;
        r35 = r14;
        r36 = r51;
        r32.<init>(r33, r34, r35, r36, r37);
        r8.add(r1);
        if (r39 != 0) goto L_0x058e;
        r2 = new java.util.ArrayList;
        r2.<init>();
        goto L_0x0590;
        r2 = r39;
        r2.add(r1);
        r9 = r9 + 1;
        r39 = r2;
        r1 = r59;
        r2 = r60;
        r6 = r61;
        r10 = r62;
        goto L_0x0547;
        r13 = r55;
        goto L_0x047a;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateEncryptedChatTyping;
        if (r3 == 0) goto L_0x064f;
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateEncryptedChatTyping) r1;
        r2 = r1.chat_id;
        r3 = 1;
        r2 = r12.getEncryptedChatDB(r2, r3);
        if (r2 == 0) goto L_0x063f;
        r3 = r1.chat_id;
        r3 = (long) r3;
        r8 = 32;
        r3 = r3 << r8;
        r8 = r12.printingUsers;
        r9 = java.lang.Long.valueOf(r3);
        r8 = r8.get(r9);
        r8 = (java.util.ArrayList) r8;
        if (r8 != 0) goto L_0x05d7;
        r9 = new java.util.ArrayList;
        r9.<init>();
        r8 = r9;
        r9 = r12.printingUsers;
        r10 = java.lang.Long.valueOf(r3);
        r9.put(r10, r8);
        r9 = 0;
        r10 = 0;
        r13 = r8.size();
        if (r10 >= r13) goto L_0x0605;
        r15 = r8.get(r10);
        r15 = (org.telegram.messenger.MessagesController.PrintingUser) r15;
        r63 = r1;
        r1 = r15.userId;
        r64 = r3;
        r3 = r2.user_id;
        if (r1 != r3) goto L_0x05fc;
        r9 = 1;
        r3 = r49;
        r15.lastTime = r3;
        r1 = new org.telegram.tgnet.TLRPC$TL_sendMessageTypingAction;
        r1.<init>();
        r15.action = r1;
        goto L_0x060b;
        r3 = r49;
        r10 = r10 + 1;
        r1 = r63;
        r3 = r64;
        goto L_0x05dd;
        r63 = r1;
        r64 = r3;
        r3 = r49;
        if (r9 != 0) goto L_0x0625;
        r1 = new org.telegram.messenger.MessagesController$PrintingUser;
        r1.<init>();
        r10 = r2.user_id;
        r1.userId = r10;
        r1.lastTime = r3;
        r10 = new org.telegram.tgnet.TLRPC$TL_sendMessageTypingAction;
        r10.<init>();
        r1.action = r10;
        r8.add(r1);
        r1 = 1;
        r27 = r1;
        r1 = r12.onlinePrivacy;
        r10 = r2.user_id;
        r10 = java.lang.Integer.valueOf(r10);
        r13 = r12.currentAccount;
        r13 = org.telegram.tgnet.ConnectionsManager.getInstance(r13);
        r13 = r13.getCurrentTime();
        r13 = java.lang.Integer.valueOf(r13);
        r1.put(r10, r13);
        goto L_0x0641;
        r3 = r49;
        r66 = r0;
        r88 = r3;
        r15 = r47;
        r8 = r51;
        r2 = r53;
        r13 = r55;
        goto L_0x0484;
        r8 = r49;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateEncryptedMessagesRead;
        if (r3 == 0) goto L_0x068b;
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateEncryptedMessagesRead) r1;
        if (r47 != 0) goto L_0x0661;
        r2 = new android.util.SparseIntArray;
        r2.<init>();
        r15 = r2;
        goto L_0x0663;
        r15 = r47;
        r2 = r1.chat_id;
        r3 = r1.max_date;
        r4 = r1.date;
        r3 = java.lang.Math.max(r3, r4);
        r15.put(r2, r3);
        r13 = r44;
        if (r13 != 0) goto L_0x067d;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r44 = r2;
        r13 = r44;
        r2 = r5;
        r2 = (org.telegram.tgnet.TLRPC.TL_updateEncryptedMessagesRead) r2;
        r13.add(r2);
        r66 = r0;
        r88 = r8;
        r44 = r13;
        goto L_0x0647;
        r13 = r44;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChatParticipantAdd;
        if (r3 == 0) goto L_0x06bc;
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateChatParticipantAdd) r1;
        r2 = r12.currentAccount;
        r28 = org.telegram.messenger.MessagesStorage.getInstance(r2);
        r2 = r1.chat_id;
        r3 = r1.user_id;
        r31 = 0;
        r4 = r1.inviter_id;
        r10 = r1.version;
        r29 = r2;
        r30 = r3;
        r32 = r4;
        r33 = r10;
        r28.updateChatInfo(r29, r30, r31, r32, r33);
        r66 = r0;
        r68 = r8;
        r76 = r11;
        r15 = r43;
        r8 = r51;
        goto L_0x0eb1;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChatParticipantDelete;
        if (r3 == 0) goto L_0x06dd;
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateChatParticipantDelete) r1;
        r2 = r12.currentAccount;
        r28 = org.telegram.messenger.MessagesStorage.getInstance(r2);
        r2 = r1.chat_id;
        r3 = r1.user_id;
        r31 = 1;
        r32 = 0;
        r4 = r1.version;
        r29 = r2;
        r30 = r3;
        r33 = r4;
        r28.updateChatInfo(r29, r30, r31, r32, r33);
        goto L_0x06b0;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateDcOptions;
        if (r3 != 0) goto L_0x0e9e;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateConfig;
        if (r3 == 0) goto L_0x06f1;
        r66 = r0;
        r68 = r8;
        r76 = r11;
        r15 = r43;
        r8 = r51;
        goto L_0x0ea8;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateEncryption;
        if (r3 == 0) goto L_0x0702;
        r1 = r12.currentAccount;
        r1 = org.telegram.messenger.SecretChatHelper.getInstance(r1);
        r2 = r5;
        r2 = (org.telegram.tgnet.TLRPC.TL_updateEncryption) r2;
        r1.processUpdateEncryption(r2, r14);
        goto L_0x06b0;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateUserBlocked;
        if (r3 == 0) goto L_0x0744;
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateUserBlocked) r1;
        r2 = r1.blocked;
        if (r2 == 0) goto L_0x0725;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = r1.user_id;
        r3 = java.lang.Integer.valueOf(r3);
        r2.add(r3);
        r3 = r12.currentAccount;
        r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
        r3.putBlockedUsers(r2, r15);
        goto L_0x0730;
        r2 = r12.currentAccount;
        r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);
        r3 = r1.user_id;
        r2.deleteBlockedUser(r3);
        r2 = r12.currentAccount;
        r2 = org.telegram.messenger.MessagesStorage.getInstance(r2);
        r2 = r2.getStorageQueue();
        r3 = new org.telegram.messenger.MessagesController$129;
        r3.<init>(r1);
        r2.postRunnable(r3);
        goto L_0x06b0;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateNotifySettings;
        if (r3 == 0) goto L_0x0761;
        if (r20 != 0) goto L_0x0752;
        r1 = new java.util.ArrayList;
        r1.<init>();
        r20 = r1;
        goto L_0x0754;
        r1 = r20;
        r1.add(r5);
        r66 = r0;
        r20 = r1;
        r88 = r8;
        r44 = r13;
        goto L_0x0645;
        r3 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateServiceNotification;
        if (r3 == 0) goto L_0x0873;
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateServiceNotification) r1;
        r2 = r1.popup;
        if (r2 == 0) goto L_0x0780;
        r2 = r1.message;
        if (r2 == 0) goto L_0x0780;
        r2 = r1.message;
        r2 = r2.length();
        if (r2 <= 0) goto L_0x0780;
        r2 = new org.telegram.messenger.MessagesController$130;
        r2.<init>(r1);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r2);
        r2 = r1.flags;
        r3 = 2;
        r2 = r2 & r3;
        if (r2 == 0) goto L_0x0859;
        r2 = new org.telegram.tgnet.TLRPC$TL_message;
        r2.<init>();
        r3 = r12.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3 = r3.getNewMessageId();
        r2.id = r3;
        r2.local_id = r3;
        r3 = r12.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3.saveConfig(r15);
        r3 = 1;
        r2.unread = r3;
        r2.flags = r4;
        r3 = r1.inbox_date;
        if (r3 == 0) goto L_0x07b0;
        r3 = r1.inbox_date;
        r2.date = r3;
        goto L_0x07ba;
        r3 = java.lang.System.currentTimeMillis();
        r15 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r3 = r3 / r15;
        r3 = (int) r3;
        r2.date = r3;
        r3 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
        r2.from_id = r3;
        r3 = new org.telegram.tgnet.TLRPC$TL_peerUser;
        r3.<init>();
        r2.to_id = r3;
        r3 = r2.to_id;
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.getClientUserId();
        r3.user_id = r4;
        r3 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
        r2.dialog_id = r3;
        r3 = r1.media;
        if (r3 == 0) goto L_0x07e7;
        r3 = r1.media;
        r2.media = r3;
        r3 = r2.flags;
        r3 = r3 | 512;
        r2.flags = r3;
        r3 = r1.message;
        r2.message = r3;
        r3 = r1.entities;
        if (r3 == 0) goto L_0x07f3;
        r3 = r1.entities;
        r2.entities = r3;
        if (r6 != 0) goto L_0x07fb;
        r3 = new java.util.ArrayList;
        r3.<init>();
        r6 = r3;
        r6.add(r2);
        r3 = new org.telegram.messenger.MessageObject;
        r4 = r12.currentAccount;
        r10 = r12.createdDialogIds;
        r66 = r0;
        r67 = r1;
        r0 = r2.dialog_id;
        r0 = java.lang.Long.valueOf(r0);
        r37 = r10.contains(r0);
        r32 = r3;
        r33 = r4;
        r34 = r2;
        r35 = r14;
        r36 = r51;
        r32.<init>(r33, r34, r35, r36, r37);
        r0 = r3;
        if (r55 != 0) goto L_0x0828;
        r1 = new android.util.LongSparseArray;
        r1.<init>();
        goto L_0x082a;
        r1 = r55;
        r3 = r2.dialog_id;
        r3 = r1.get(r3);
        r3 = (java.util.ArrayList) r3;
        if (r3 != 0) goto L_0x0842;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r3 = r4;
        r68 = r8;
        r8 = r2.dialog_id;
        r1.put(r8, r3);
        goto L_0x0844;
        r68 = r8;
        r3.add(r0);
        if (r39 != 0) goto L_0x0851;
        r4 = new java.util.ArrayList;
        r4.<init>();
        r39 = r4;
        goto L_0x0853;
        r4 = r39;
        r4.add(r0);
        r55 = r1;
        goto L_0x085f;
        r66 = r0;
        r68 = r8;
        r4 = r39;
        r39 = r4;
        r44 = r13;
        r15 = r47;
        r8 = r51;
        r2 = r53;
        r13 = r55;
        r9 = r57;
        r10 = r58;
        r88 = r68;
        goto L_0x1338;
        r66 = r0;
        r68 = r8;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateDialogPinned;
        if (r0 == 0) goto L_0x088d;
        if (r20 != 0) goto L_0x0885;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0887;
        r0 = r20;
        r0.add(r5);
        r20 = r0;
        goto L_0x0861;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updatePinnedDialogs;
        if (r0 == 0) goto L_0x08a1;
        if (r20 != 0) goto L_0x089b;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x089d;
        r0 = r20;
        r0.add(r5);
        goto L_0x088a;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updatePrivacy;
        if (r0 == 0) goto L_0x08b5;
        if (r20 != 0) goto L_0x08af;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x08b1;
        r0 = r20;
        r0.add(r5);
        goto L_0x088a;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateWebPage;
        if (r0 == 0) goto L_0x08d5;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateWebPage) r0;
        if (r42 != 0) goto L_0x08c6;
        r1 = new android.util.LongSparseArray;
        r1.<init>();
        r42 = r1;
        goto L_0x08c8;
        r1 = r42;
        r2 = r0.webpage;
        r2 = r2.id;
        r4 = r0.webpage;
        r1.put(r2, r4);
        r42 = r1;
        goto L_0x0861;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChannelWebPage;
        if (r0 == 0) goto L_0x08f2;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChannelWebPage) r0;
        if (r42 != 0) goto L_0x08e6;
        r1 = new android.util.LongSparseArray;
        r1.<init>();
        r42 = r1;
        goto L_0x08e8;
        r1 = r42;
        r2 = r0.webpage;
        r2 = r2.id;
        r4 = r0.webpage;
        r1.put(r2, r4);
        goto L_0x08d2;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChannelTooLong;
        if (r0 == 0) goto L_0x0996;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChannelTooLong) r0;
        r3 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r3 == 0) goto L_0x0916;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r5);
        r4 = " channelId = ";
        r3.append(r4);
        r4 = r0.channel_id;
        r3.append(r4);
        r3 = r3.toString();
        org.telegram.messenger.FileLog.d(r3);
        r3 = r12.channelsPts;
        r4 = r0.channel_id;
        r3 = r3.get(r4);
        if (r3 != 0) goto L_0x0976;
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r8 = r0.channel_id;
        r3 = r4.getChannelPtsSync(r8);
        if (r3 != 0) goto L_0x096c;
        r4 = r0.channel_id;
        r4 = java.lang.Integer.valueOf(r4);
        r8 = r51;
        r4 = r8.get(r4);
        r4 = (org.telegram.tgnet.TLRPC.Chat) r4;
        if (r4 == 0) goto L_0x0942;
        r9 = r4.min;
        if (r9 == 0) goto L_0x094c;
        r9 = r0.channel_id;
        r9 = java.lang.Integer.valueOf(r9);
        r4 = r12.getChat(r9);
        if (r4 == 0) goto L_0x0952;
        r9 = r4.min;
        if (r9 == 0) goto L_0x0962;
        r9 = r12.currentAccount;
        r9 = org.telegram.messenger.MessagesStorage.getInstance(r9);
        r10 = r0.channel_id;
        r4 = r9.getChatSync(r10);
        r9 = 1;
        r12.putChat(r4, r9);
        if (r4 == 0) goto L_0x096b;
        r9 = r4.min;
        if (r9 != 0) goto L_0x096b;
        r12.loadUnknownChannel(r4, r1);
        goto L_0x0978;
        r8 = r51;
        r1 = r12.channelsPts;
        r2 = r0.channel_id;
        r1.put(r2, r3);
        goto L_0x0978;
        r8 = r51;
        if (r3 == 0) goto L_0x098f;
        r1 = r0.flags;
        r2 = 1;
        r1 = r1 & r2;
        if (r1 == 0) goto L_0x098a;
        r1 = r0.pts;
        if (r1 <= r3) goto L_0x098f;
        r1 = r0.channel_id;
        r12.getChannelDifference(r1);
        goto L_0x098f;
        r1 = r0.channel_id;
        r12.getChannelDifference(r1);
        r76 = r11;
        r15 = r43;
        goto L_0x0eb1;
        r8 = r51;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateReadChannelInbox;
        if (r0 == 0) goto L_0x0a0a;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateReadChannelInbox) r0;
        r1 = r0.max_id;
        r1 = (long) r1;
        r3 = r0.channel_id;
        r3 = (long) r3;
        r9 = 32;
        r3 = r3 << r9;
        r9 = r1 | r3;
        r1 = r0.channel_id;
        r1 = -r1;
        r1 = (long) r1;
        if (r53 != 0) goto L_0x09b6;
        r3 = new org.telegram.messenger.support.SparseLongArray;
        r3.<init>();
        goto L_0x09b8;
        r3 = r53;
        r4 = r0.channel_id;
        r4 = -r4;
        r3.put(r4, r9);
        r4 = r12.dialogs_read_inbox_max;
        r15 = java.lang.Long.valueOf(r1);
        r4 = r4.get(r15);
        r4 = (java.lang.Integer) r4;
        if (r4 != 0) goto L_0x09de;
        r15 = r12.currentAccount;
        r15 = org.telegram.messenger.MessagesStorage.getInstance(r15);
        r70 = r3;
        r3 = 0;
        r3 = r15.getDialogReadMax(r3, r1);
        r4 = java.lang.Integer.valueOf(r3);
        goto L_0x09e0;
        r70 = r3;
        r3 = r12.dialogs_read_inbox_max;
        r15 = java.lang.Long.valueOf(r1);
        r71 = r1;
        r1 = r4.intValue();
        r2 = r0.max_id;
        r1 = java.lang.Math.max(r1, r2);
        r1 = java.lang.Integer.valueOf(r1);
        r3.put(r15, r1);
        r44 = r13;
        r15 = r47;
        r13 = r55;
        r9 = r57;
        r10 = r58;
        r88 = r68;
        r2 = r70;
        goto L_0x1338;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateReadChannelOutbox;
        if (r0 == 0) goto L_0x0a6b;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateReadChannelOutbox) r0;
        r1 = r0.max_id;
        r1 = (long) r1;
        r3 = r0.channel_id;
        r3 = (long) r3;
        r9 = 32;
        r3 = r3 << r9;
        r9 = r1 | r3;
        r1 = r0.channel_id;
        r1 = -r1;
        r1 = (long) r1;
        if (r11 != 0) goto L_0x0a28;
        r3 = new org.telegram.messenger.support.SparseLongArray;
        r3.<init>();
        r11 = r3;
        r3 = r0.channel_id;
        r3 = -r3;
        r11.put(r3, r9);
        r3 = r12.dialogs_read_outbox_max;
        r4 = java.lang.Long.valueOf(r1);
        r3 = r3.get(r4);
        r3 = (java.lang.Integer) r3;
        if (r3 != 0) goto L_0x0a4b;
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r15 = 1;
        r4 = r4.getDialogReadMax(r15, r1);
        r3 = java.lang.Integer.valueOf(r4);
        r4 = r12.dialogs_read_outbox_max;
        r15 = java.lang.Long.valueOf(r1);
        r73 = r1;
        r1 = r3.intValue();
        r2 = r0.max_id;
        r1 = java.lang.Math.max(r1, r2);
        r1 = java.lang.Integer.valueOf(r1);
        r4.put(r15, r1);
        r44 = r13;
        r15 = r47;
        goto L_0x0867;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateDeleteChannelMessages;
        if (r0 == 0) goto L_0x0ab2;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateDeleteChannelMessages) r0;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x0a8f;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r5);
        r2 = " channelId = ";
        r1.append(r2);
        r2 = r0.channel_id;
        r1.append(r2);
        r1 = r1.toString();
        org.telegram.messenger.FileLog.d(r1);
        if (r7 != 0) goto L_0x0a97;
        r1 = new android.util.SparseArray;
        r1.<init>();
        r7 = r1;
        r1 = r0.channel_id;
        r1 = r7.get(r1);
        r1 = (java.util.ArrayList) r1;
        if (r1 != 0) goto L_0x0aac;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r1 = r2;
        r2 = r0.channel_id;
        r7.put(r2, r1);
        r2 = r0.messages;
        r1.addAll(r2);
        goto L_0x0a65;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChannel;
        if (r0 == 0) goto L_0x0ae9;
        r0 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r0 == 0) goto L_0x0ad6;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChannel) r0;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r5);
        r2 = " channelId = ";
        r1.append(r2);
        r2 = r0.channel_id;
        r1.append(r2);
        r1 = r1.toString();
        org.telegram.messenger.FileLog.d(r1);
        if (r20 != 0) goto L_0x0ae0;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0ae2;
        r0 = r20;
        r0.add(r5);
        r20 = r0;
        goto L_0x0a65;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChannelMessageViews;
        if (r0 == 0) goto L_0x0b3a;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChannelMessageViews) r0;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x0b0d;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r5);
        r2 = " channelId = ";
        r1.append(r2);
        r2 = r0.channel_id;
        r1.append(r2);
        r1 = r1.toString();
        org.telegram.messenger.FileLog.d(r1);
        if (r41 != 0) goto L_0x0b17;
        r1 = new android.util.SparseArray;
        r1.<init>();
        r41 = r1;
        goto L_0x0b19;
        r1 = r41;
        r2 = r0.channel_id;
        r2 = r1.get(r2);
        r2 = (android.util.SparseIntArray) r2;
        if (r2 != 0) goto L_0x0b2e;
        r3 = new android.util.SparseIntArray;
        r3.<init>();
        r2 = r3;
        r3 = r0.channel_id;
        r1.put(r3, r2);
        r3 = r0.id;
        r4 = r0.views;
        r2.put(r3, r4);
        r41 = r1;
        goto L_0x0a65;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChatParticipantAdmin;
        if (r0 == 0) goto L_0x0b5e;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChatParticipantAdmin) r0;
        r1 = r12.currentAccount;
        r28 = org.telegram.messenger.MessagesStorage.getInstance(r1);
        r1 = r0.chat_id;
        r2 = r0.user_id;
        r31 = 2;
        r3 = r0.is_admin;
        r4 = r0.version;
        r29 = r1;
        r30 = r2;
        r32 = r3;
        r33 = r4;
        r28.updateChatInfo(r29, r30, r31, r32, r33);
        goto L_0x0990;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChatAdmins;
        if (r0 == 0) goto L_0x0b73;
        if (r20 != 0) goto L_0x0b6c;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0b6e;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateStickerSets;
        if (r0 == 0) goto L_0x0b88;
        if (r20 != 0) goto L_0x0b81;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0b83;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateStickerSetsOrder;
        if (r0 == 0) goto L_0x0b9d;
        if (r20 != 0) goto L_0x0b96;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0b98;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateNewStickerSet;
        if (r0 == 0) goto L_0x0bb2;
        if (r20 != 0) goto L_0x0bab;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0bad;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateDraftMessage;
        if (r0 == 0) goto L_0x0bc7;
        if (r20 != 0) goto L_0x0bc0;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0bc2;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateSavedGifs;
        if (r0 == 0) goto L_0x0bdc;
        if (r20 != 0) goto L_0x0bd5;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0bd7;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateEditChannelMessage;
        if (r0 != 0) goto L_0x0cc5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateEditMessage;
        if (r0 == 0) goto L_0x0be8;
        r15 = r43;
        goto L_0x0cc7;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChannelPinnedMessage;
        if (r0 == 0) goto L_0x0c1b;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChannelPinnedMessage) r0;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x0c0c;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r5);
        r2 = " channelId = ";
        r1.append(r2);
        r2 = r0.channel_id;
        r1.append(r2);
        r1 = r1.toString();
        org.telegram.messenger.FileLog.d(r1);
        r1 = r12.currentAccount;
        r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
        r2 = r0.channel_id;
        r3 = r0.id;
        r1.updateChannelPinnedMessage(r2, r3);
        goto L_0x0990;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateReadFeaturedStickers;
        if (r0 == 0) goto L_0x0c30;
        if (r20 != 0) goto L_0x0c29;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0c2b;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updatePhoneCall;
        if (r0 == 0) goto L_0x0c45;
        if (r20 != 0) goto L_0x0c3e;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0c40;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateLangPack;
        if (r0 == 0) goto L_0x0c59;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateLangPack) r0;
        r1 = org.telegram.messenger.LocaleController.getInstance();
        r2 = r0.difference;
        r3 = r12.currentAccount;
        r1.saveRemoteLocaleStrings(r2, r3);
        goto L_0x0990;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateLangPackTooLong;
        if (r0 == 0) goto L_0x0c68;
        r0 = org.telegram.messenger.LocaleController.getInstance();
        r1 = r12.currentAccount;
        r0.reloadCurrentRemoteLocale(r1);
        goto L_0x0990;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateFavedStickers;
        if (r0 == 0) goto L_0x0c7d;
        if (r20 != 0) goto L_0x0c76;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0c78;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateContactsReset;
        if (r0 == 0) goto L_0x0c92;
        if (r20 != 0) goto L_0x0c8b;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r20 = r0;
        goto L_0x0c8d;
        r0 = r20;
        r0.add(r5);
        goto L_0x0ae5;
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateChannelAvailableMessages;
        if (r0 == 0) goto L_0x0cbf;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChannelAvailableMessages) r0;
        r15 = r43;
        if (r15 != 0) goto L_0x0ca6;
        r1 = new android.util.SparseIntArray;
        r1.<init>();
        r43 = r1;
        r15 = r43;
        r1 = r0.channel_id;
        r1 = r15.get(r1);
        if (r1 == 0) goto L_0x0cb2;
        r2 = r0.available_min_id;
        if (r1 >= r2) goto L_0x0cb9;
        r2 = r0.channel_id;
        r3 = r0.available_min_id;
        r15.put(r2, r3);
        r44 = r13;
        r43 = r15;
        goto L_0x0a67;
        r15 = r43;
        r76 = r11;
        goto L_0x0eb1;
        r15 = r43;
        r0 = r12.currentAccount;
        r0 = org.telegram.messenger.UserConfig.getInstance(r0);
        r0 = r0.getClientUserId();
        r1 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateEditChannelMessage;
        if (r1 == 0) goto L_0x0d18;
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateEditChannelMessage) r1;
        r1 = r1.message;
        r2 = r1.to_id;
        r2 = r2.channel_id;
        r2 = java.lang.Integer.valueOf(r2);
        r2 = r8.get(r2);
        r2 = (org.telegram.tgnet.TLRPC.Chat) r2;
        if (r2 != 0) goto L_0x0cf6;
        r3 = r1.to_id;
        r3 = r3.channel_id;
        r3 = java.lang.Integer.valueOf(r3);
        r2 = r12.getChat(r3);
        if (r2 != 0) goto L_0x0d0a;
        r3 = r12.currentAccount;
        r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
        r4 = r1.to_id;
        r4 = r4.channel_id;
        r2 = r3.getChatSync(r4);
        r3 = 1;
        r12.putChat(r2, r3);
        if (r2 == 0) goto L_0x0d17;
        r3 = r2.megagroup;
        if (r3 == 0) goto L_0x0d17;
        r3 = r1.flags;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r3 = r3 | r4;
        r1.flags = r3;
        goto L_0x0d2c;
        r1 = r5;
        r1 = (org.telegram.tgnet.TLRPC.TL_updateEditMessage) r1;
        r1 = r1.message;
        r2 = r1.dialog_id;
        r9 = (long) r0;
        r4 = (r2 > r9 ? 1 : (r2 == r9 ? 0 : -1));
        if (r4 != 0) goto L_0x0d2c;
        r2 = 0;
        r1.unread = r2;
        r1.media_unread = r2;
        r2 = 1;
        r1.out = r2;
        r2 = r1.out;
        if (r2 != 0) goto L_0x0d41;
        r2 = r1.from_id;
        r3 = r12.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3 = r3.getClientUserId();
        if (r2 != r3) goto L_0x0d41;
        r2 = 1;
        r1.out = r2;
        if (r126 != 0) goto L_0x0da1;
        r2 = 0;
        r3 = r1.entities;
        r3 = r3.size();
        if (r2 >= r3) goto L_0x0da1;
        r4 = r1.entities;
        r4 = r4.get(r2);
        r4 = (org.telegram.tgnet.TLRPC.MessageEntity) r4;
        r9 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageEntityMentionName;
        if (r9 == 0) goto L_0x0d9a;
        r9 = r4;
        r9 = (org.telegram.tgnet.TLRPC.TL_messageEntityMentionName) r9;
        r9 = r9.user_id;
        r10 = java.lang.Integer.valueOf(r9);
        r10 = r14.get(r10);
        r10 = (org.telegram.tgnet.TLRPC.User) r10;
        if (r10 == 0) goto L_0x0d70;
        r75 = r3;
        r3 = r10.min;
        if (r3 == 0) goto L_0x0d7a;
        goto L_0x0d72;
        r75 = r3;
        r3 = java.lang.Integer.valueOf(r9);
        r10 = r12.getUser(r3);
        if (r10 == 0) goto L_0x0d80;
        r3 = r10.min;
        if (r3 == 0) goto L_0x0d96;
        r3 = r12.currentAccount;
        r3 = org.telegram.messenger.MessagesStorage.getInstance(r3);
        r3 = r3.getUserSync(r9);
        if (r3 == 0) goto L_0x0d91;
        r10 = r3.min;
        if (r10 == 0) goto L_0x0d91;
        r3 = 0;
        r10 = r3;
        r3 = 1;
        r12.putUser(r10, r3);
        if (r10 != 0) goto L_0x0d9c;
        r3 = 0;
        return r3;
        r75 = r3;
        r2 = r2 + 1;
        r3 = r75;
        goto L_0x0d4a;
        r2 = r1.to_id;
        r2 = r2.chat_id;
        if (r2 == 0) goto L_0x0db0;
        r2 = r1.to_id;
        r2 = r2.chat_id;
        r2 = -r2;
        r2 = (long) r2;
        r1.dialog_id = r2;
        goto L_0x0ddc;
        r2 = r1.to_id;
        r2 = r2.channel_id;
        if (r2 == 0) goto L_0x0dbf;
        r2 = r1.to_id;
        r2 = r2.channel_id;
        r2 = -r2;
        r2 = (long) r2;
        r1.dialog_id = r2;
        goto L_0x0ddc;
        r2 = r1.to_id;
        r2 = r2.user_id;
        r3 = r12.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3 = r3.getClientUserId();
        if (r2 != r3) goto L_0x0dd5;
        r2 = r1.to_id;
        r3 = r1.from_id;
        r2.user_id = r3;
        r2 = r1.to_id;
        r2 = r2.user_id;
        r2 = (long) r2;
        r1.dialog_id = r2;
        r2 = r1.out;
        if (r2 == 0) goto L_0x0de3;
        r2 = r12.dialogs_read_outbox_max;
        goto L_0x0de5;
        r2 = r12.dialogs_read_inbox_max;
        r3 = r1.dialog_id;
        r3 = java.lang.Long.valueOf(r3);
        r3 = r2.get(r3);
        r3 = (java.lang.Integer) r3;
        if (r3 != 0) goto L_0x0e11;
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r9 = r1.out;
        r76 = r11;
        r10 = r1.dialog_id;
        r4 = r4.getDialogReadMax(r9, r10);
        r3 = java.lang.Integer.valueOf(r4);
        r9 = r1.dialog_id;
        r4 = java.lang.Long.valueOf(r9);
        r2.put(r4, r3);
        goto L_0x0e13;
        r76 = r11;
        r4 = r3.intValue();
        r9 = r1.id;
        if (r4 >= r9) goto L_0x0e1d;
        r4 = 1;
        goto L_0x0e1e;
        r4 = 0;
        r1.unread = r4;
        r9 = r1.dialog_id;
        r77 = r2;
        r78 = r3;
        r2 = (long) r0;
        r4 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1));
        if (r4 != 0) goto L_0x0e33;
        r2 = 1;
        r1.out = r2;
        r2 = 0;
        r1.unread = r2;
        r1.media_unread = r2;
        r2 = r1.out;
        if (r2 == 0) goto L_0x0e43;
        r2 = r1.message;
        if (r2 != 0) goto L_0x0e43;
        r2 = "";
        r1.message = r2;
        r2 = "";
        r1.attachPath = r2;
        org.telegram.messenger.ImageLoader.saveMessageThumbs(r1);
        r2 = new org.telegram.messenger.MessageObject;
        r3 = r12.currentAccount;
        r4 = r12.createdDialogIds;
        r9 = r1.dialog_id;
        r9 = java.lang.Long.valueOf(r9);
        r37 = r4.contains(r9);
        r32 = r2;
        r33 = r3;
        r34 = r1;
        r35 = r14;
        r36 = r8;
        r32.<init>(r33, r34, r35, r36, r37);
        if (r40 != 0) goto L_0x0e6d;
        r3 = new android.util.LongSparseArray;
        r3.<init>();
        r40 = r3;
        goto L_0x0e6f;
        r3 = r40;
        r9 = r1.dialog_id;
        r4 = r3.get(r9);
        r4 = (java.util.ArrayList) r4;
        if (r4 != 0) goto L_0x0e84;
        r9 = new java.util.ArrayList;
        r9.<init>();
        r4 = r9;
        r9 = r1.dialog_id;
        r3.put(r9, r4);
        r4.add(r2);
        r40 = r3;
        r44 = r13;
        r43 = r15;
        r15 = r47;
        r2 = r53;
        r13 = r55;
        r9 = r57;
        r10 = r58;
        r88 = r68;
        r11 = r76;
        goto L_0x1338;
        r66 = r0;
        r68 = r8;
        r76 = r11;
        r15 = r43;
        r8 = r51;
        r0 = r12.currentAccount;
        r0 = org.telegram.tgnet.ConnectionsManager.getInstance(r0);
        r0.updateDcSettings();
        r44 = r13;
        r43 = r15;
        r15 = r47;
        r2 = r53;
        r13 = r55;
        r9 = r57;
        r10 = r58;
        r88 = r68;
        r11 = r76;
        goto L_0x1338;
    L_0x0ec5:
        r58 = r10;
        r76 = r11;
        r15 = r43;
        r13 = r44;
        r68 = r49;
        r8 = r51;
        r14 = r54;
        r66 = r56;
    L_0x0ed5:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateUserTyping;
        if (r0 == 0) goto L_0x0ee8;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateUserTyping) r0;
        r3 = r0.user_id;
        r4 = r0.action;
        r0 = 0;
        r121 = r4;
        r4 = r0;
        r0 = r121;
        goto L_0x0ef6;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateChatUserTyping) r0;
        r3 = r0.chat_id;
        r4 = r0.user_id;
        r0 = r0.action;
        r121 = r4;
        r4 = r3;
        r3 = r121;
        r9 = r12.currentAccount;
        r9 = org.telegram.messenger.UserConfig.getInstance(r9);
        r9 = r9.getClientUserId();
        if (r3 == r9) goto L_0x0fda;
        r9 = -r4;
        r9 = (long) r9;
        r11 = (r9 > r1 ? 1 : (r9 == r1 ? 0 : -1));
        if (r11 != 0) goto L_0x0f09;
        r9 = (long) r3;
        r1 = r12.printingUsers;
        r2 = java.lang.Long.valueOf(r9);
        r1 = r1.get(r2);
        r1 = (java.util.ArrayList) r1;
        r2 = r0 instanceof org.telegram.tgnet.TLRPC.TL_sendMessageCancelAction;
        if (r2 == 0) goto L_0x0f61;
        if (r1 == 0) goto L_0x0f58;
        r2 = 0;
        r11 = r1.size();
        if (r2 >= r11) goto L_0x0f3f;
        r16 = r1.get(r2);
        r79 = r4;
        r4 = r16;
        r4 = (org.telegram.messenger.MessagesController.PrintingUser) r4;
        r80 = r7;
        r7 = r4.userId;
        if (r7 != r3) goto L_0x0f38;
        r1.remove(r2);
        r27 = 1;
        goto L_0x0f43;
        r2 = r2 + 1;
        r4 = r79;
        r7 = r80;
        goto L_0x0f20;
        r79 = r4;
        r80 = r7;
        r2 = r1.isEmpty();
        if (r2 == 0) goto L_0x0f52;
        r2 = r12.printingUsers;
        r4 = java.lang.Long.valueOf(r9);
        r2.remove(r4);
        r81 = r9;
        r9 = r68;
        goto L_0x0fc2;
        r79 = r4;
        r80 = r7;
        r81 = r9;
        r9 = r68;
        goto L_0x0fc2;
        r79 = r4;
        r80 = r7;
        if (r1 != 0) goto L_0x0f76;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r1 = r2;
        r2 = r12.printingUsers;
        r4 = java.lang.Long.valueOf(r9);
        r2.put(r4, r1);
        r2 = 0;
        r4 = r1.iterator();
        r7 = r4.hasNext();
        if (r7 == 0) goto L_0x0fab;
        r7 = r4.next();
        r7 = (org.telegram.messenger.MessagesController.PrintingUser) r7;
        r11 = r7.userId;
        if (r11 != r3) goto L_0x0fa4;
        r2 = 1;
        r81 = r9;
        r9 = r68;
        r7.lastTime = r9;
        r4 = r7.action;
        r4 = r4.getClass();
        r11 = r0.getClass();
        if (r4 == r11) goto L_0x0fa1;
        r4 = 1;
        r27 = r4;
        r7.action = r0;
        goto L_0x0faf;
        r81 = r9;
        r9 = r68;
        r9 = r81;
        goto L_0x0f7b;
        r81 = r9;
        r9 = r68;
        if (r2 != 0) goto L_0x0fc2;
        r4 = new org.telegram.messenger.MessagesController$PrintingUser;
        r4.<init>();
        r4.userId = r3;
        r4.lastTime = r9;
        r4.action = r0;
        r1.add(r4);
        r2 = 1;
        r27 = r2;
        r2 = r12.onlinePrivacy;
        r4 = java.lang.Integer.valueOf(r3);
        r7 = r12.currentAccount;
        r7 = org.telegram.tgnet.ConnectionsManager.getInstance(r7);
        r7 = r7.getCurrentTime();
        r7 = java.lang.Integer.valueOf(r7);
        r2.put(r4, r7);
        goto L_0x0fde;
        r80 = r7;
        r9 = r68;
        r88 = r9;
        r44 = r13;
        r43 = r15;
        r15 = r47;
        r2 = r53;
        r13 = r55;
        r9 = r57;
        r10 = r58;
        r11 = r76;
        r7 = r80;
        goto L_0x1338;
    L_0x0ff4:
        r53 = r2;
        r80 = r7;
        r66 = r8;
        r57 = r9;
        r58 = r10;
        r76 = r11;
        r55 = r13;
        r15 = r43;
        r13 = r44;
        r9 = r0;
        r8 = r3;
    L_0x1008:
        r0 = r5 instanceof org.telegram.tgnet.TLRPC.TL_updateNewMessage;
        if (r0 == 0) goto L_0x1012;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateNewMessage) r0;
        r0 = r0.message;
        goto L_0x104b;
        r0 = r5;
        r0 = (org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage) r0;
        r0 = r0.message;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x1036;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r5);
        r2 = " channelId = ";
        r1.append(r2);
        r2 = r0.to_id;
        r2 = r2.channel_id;
        r1.append(r2);
        r1 = r1.toString();
        org.telegram.messenger.FileLog.d(r1);
        r1 = r0.out;
        if (r1 != 0) goto L_0x104b;
        r1 = r0.from_id;
        r2 = r12.currentAccount;
        r2 = org.telegram.messenger.UserConfig.getInstance(r2);
        r2 = r2.getClientUserId();
        if (r1 != r2) goto L_0x104b;
        r1 = 1;
        r0.out = r1;
        r1 = 0;
        r2 = 0;
        r3 = 0;
        r4 = r0.to_id;
        r4 = r4.channel_id;
        if (r4 == 0) goto L_0x1059;
        r4 = r0.to_id;
        r2 = r4.channel_id;
        goto L_0x106e;
        r4 = r0.to_id;
        r4 = r4.chat_id;
        if (r4 == 0) goto L_0x1064;
        r4 = r0.to_id;
        r2 = r4.chat_id;
        goto L_0x106e;
        r4 = r0.to_id;
        r4 = r4.user_id;
        if (r4 == 0) goto L_0x106e;
        r4 = r0.to_id;
        r3 = r4.user_id;
        if (r2 == 0) goto L_0x1095;
        r4 = java.lang.Integer.valueOf(r2);
        r4 = r8.get(r4);
        r1 = r4;
        r1 = (org.telegram.tgnet.TLRPC.Chat) r1;
        if (r1 != 0) goto L_0x1085;
        r4 = java.lang.Integer.valueOf(r2);
        r1 = r12.getChat(r4);
        if (r1 != 0) goto L_0x1095;
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r1 = r4.getChatSync(r2);
        r4 = 1;
        r12.putChat(r1, r4);
        if (r23 == 0) goto L_0x11a3;
        if (r2 == 0) goto L_0x10b5;
        if (r1 != 0) goto L_0x10b5;
        r4 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r4 == 0) goto L_0x10b3;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r7 = "not found chat ";
        r4.append(r7);
        r4.append(r2);
        r4 = r4.toString();
        org.telegram.messenger.FileLog.d(r4);
        r4 = 0;
        return r4;
        r4 = r0.entities;
        r4 = r4.size();
        r7 = 3;
        r7 = r7 + r4;
        r4 = r3;
        r3 = 0;
        if (r3 >= r7) goto L_0x119c;
        r11 = 0;
        if (r3 == 0) goto L_0x10f9;
        r83 = r2;
        r2 = 1;
        if (r3 != r2) goto L_0x10d2;
        r2 = r0.from_id;
        r4 = r0.post;
        if (r4 == 0) goto L_0x10d0;
        r11 = 1;
        r4 = r2;
        goto L_0x10fd;
        r2 = 2;
        if (r3 != r2) goto L_0x10e0;
        r2 = r0.fwd_from;
        if (r2 == 0) goto L_0x10de;
        r2 = r0.fwd_from;
        r2 = r2.from_id;
        goto L_0x10df;
        r2 = 0;
        goto L_0x10d0;
        r2 = r0.entities;
        r84 = r4;
        r4 = r3 + -3;
        r2 = r2.get(r4);
        r2 = (org.telegram.tgnet.TLRPC.MessageEntity) r2;
        r4 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageEntityMentionName;
        if (r4 == 0) goto L_0x10f6;
        r4 = r2;
        r4 = (org.telegram.tgnet.TLRPC.TL_messageEntityMentionName) r4;
        r4 = r4.user_id;
        goto L_0x10f7;
        r4 = 0;
        r2 = r4;
        goto L_0x10fd;
        r83 = r2;
        r84 = r4;
        if (r4 <= 0) goto L_0x118e;
        r2 = java.lang.Integer.valueOf(r4);
        r2 = r14.get(r2);
        r2 = (org.telegram.tgnet.TLRPC.User) r2;
        if (r2 == 0) goto L_0x1117;
        if (r11 != 0) goto L_0x1114;
        r85 = r5;
        r5 = r2.min;
        if (r5 == 0) goto L_0x1121;
        goto L_0x1119;
        r85 = r5;
        goto L_0x1121;
        r85 = r5;
        r5 = java.lang.Integer.valueOf(r4);
        r2 = r12.getUser(r5);
        if (r2 == 0) goto L_0x1129;
        if (r11 != 0) goto L_0x1140;
        r5 = r2.min;
        if (r5 == 0) goto L_0x1140;
        r5 = r12.currentAccount;
        r5 = org.telegram.messenger.MessagesStorage.getInstance(r5);
        r2 = r5.getUserSync(r4);
        if (r2 == 0) goto L_0x113c;
        if (r11 != 0) goto L_0x113c;
        r5 = r2.min;
        if (r5 == 0) goto L_0x113c;
        r2 = 0;
        r5 = 1;
        r12.putUser(r2, r5);
        if (r2 != 0) goto L_0x1161;
        r5 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r5 == 0) goto L_0x115d;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r86 = r7;
        r7 = "not found user ";
        r5.append(r7);
        r5.append(r4);
        r5 = r5.toString();
        org.telegram.messenger.FileLog.d(r5);
        goto L_0x115f;
        r86 = r7;
        r5 = 0;
        return r5;
        r86 = r7;
        r5 = 1;
        if (r3 != r5) goto L_0x1192;
        r5 = r2.status;
        if (r5 == 0) goto L_0x1192;
        r5 = r2.status;
        r5 = r5.expires;
        if (r5 > 0) goto L_0x1192;
        r5 = r12.onlinePrivacy;
        r7 = java.lang.Integer.valueOf(r4);
        r87 = r2;
        r2 = r12.currentAccount;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);
        r2 = r2.getCurrentTime();
        r2 = java.lang.Integer.valueOf(r2);
        r5.put(r7, r2);
        r2 = r66 | 4;
        r66 = r2;
        goto L_0x1192;
        r85 = r5;
        r86 = r7;
        r3 = r3 + 1;
        r2 = r83;
        r5 = r85;
        r7 = r86;
        goto L_0x10bf;
        r83 = r2;
        r84 = r4;
        r85 = r5;
        goto L_0x11a9;
        r83 = r2;
        r85 = r5;
        r84 = r3;
        if (r1 == 0) goto L_0x11b6;
        r2 = r1.megagroup;
        if (r2 == 0) goto L_0x11b6;
        r2 = r0.flags;
        r3 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r2 = r2 | r3;
        r0.flags = r2;
        r2 = r0.action;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
        if (r2 == 0) goto L_0x11fe;
        r2 = r0.action;
        r2 = r2.user_id;
        r2 = java.lang.Integer.valueOf(r2);
        r2 = r14.get(r2);
        r2 = (org.telegram.tgnet.TLRPC.User) r2;
        if (r2 == 0) goto L_0x11de;
        r3 = r2.bot;
        if (r3 == 0) goto L_0x11de;
        r3 = new org.telegram.tgnet.TLRPC$TL_replyKeyboardHide;
        r3.<init>();
        r0.reply_markup = r3;
        r3 = r0.flags;
        r3 = r3 | 64;
        r0.flags = r3;
        goto L_0x11fe;
        r3 = r0.from_id;
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.getClientUserId();
        if (r3 != r4) goto L_0x11fe;
        r3 = r0.action;
        r3 = r3.user_id;
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r4 = r4.getClientUserId();
        if (r3 != r4) goto L_0x11fe;
        goto L_0x0fde;
        if (r6 != 0) goto L_0x1206;
        r2 = new java.util.ArrayList;
        r2.<init>();
        r6 = r2;
        r6.add(r0);
        org.telegram.messenger.ImageLoader.saveMessageThumbs(r0);
        r2 = r12.currentAccount;
        r2 = org.telegram.messenger.UserConfig.getInstance(r2);
        r2 = r2.getClientUserId();
        r3 = r0.to_id;
        r3 = r3.chat_id;
        if (r3 == 0) goto L_0x1225;
        r3 = r0.to_id;
        r3 = r3.chat_id;
        r3 = -r3;
        r3 = (long) r3;
        r0.dialog_id = r3;
        goto L_0x1247;
        r3 = r0.to_id;
        r3 = r3.channel_id;
        if (r3 == 0) goto L_0x1234;
        r3 = r0.to_id;
        r3 = r3.channel_id;
        r3 = -r3;
        r3 = (long) r3;
        r0.dialog_id = r3;
        goto L_0x1247;
        r3 = r0.to_id;
        r3 = r3.user_id;
        if (r3 != r2) goto L_0x1240;
        r3 = r0.to_id;
        r4 = r0.from_id;
        r3.user_id = r4;
        r3 = r0.to_id;
        r3 = r3.user_id;
        r3 = (long) r3;
        r0.dialog_id = r3;
        r3 = r0.out;
        if (r3 == 0) goto L_0x124e;
        r3 = r12.dialogs_read_outbox_max;
        goto L_0x1250;
        r3 = r12.dialogs_read_inbox_max;
        r4 = r0.dialog_id;
        r4 = java.lang.Long.valueOf(r4);
        r4 = r3.get(r4);
        r4 = (java.lang.Integer) r4;
        if (r4 != 0) goto L_0x127c;
        r5 = r12.currentAccount;
        r5 = org.telegram.messenger.MessagesStorage.getInstance(r5);
        r7 = r0.out;
        r88 = r9;
        r9 = r0.dialog_id;
        r5 = r5.getDialogReadMax(r7, r9);
        r4 = java.lang.Integer.valueOf(r5);
        r9 = r0.dialog_id;
        r5 = java.lang.Long.valueOf(r9);
        r3.put(r5, r4);
        goto L_0x127e;
        r88 = r9;
        r5 = r4.intValue();
        r7 = r0.id;
        if (r5 >= r7) goto L_0x129c;
        if (r1 == 0) goto L_0x128e;
        r5 = org.telegram.messenger.ChatObject.isNotInChat(r1);
        if (r5 != 0) goto L_0x129c;
        r5 = r0.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo;
        if (r5 != 0) goto L_0x129c;
        r5 = r0.action;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChannelCreate;
        if (r5 != 0) goto L_0x129c;
        r5 = 1;
        goto L_0x129d;
        r5 = 0;
        r0.unread = r5;
        r9 = r0.dialog_id;
        r90 = r3;
        r91 = r4;
        r3 = (long) r2;
        r5 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r5 != 0) goto L_0x12b2;
        r3 = 0;
        r0.unread = r3;
        r0.media_unread = r3;
        r3 = 1;
        r0.out = r3;
        r3 = new org.telegram.messenger.MessageObject;
        r4 = r12.currentAccount;
        r5 = r12.createdDialogIds;
        r9 = r0.dialog_id;
        r7 = java.lang.Long.valueOf(r9);
        r37 = r5.contains(r7);
        r32 = r3;
        r33 = r4;
        r34 = r0;
        r35 = r14;
        r36 = r8;
        r32.<init>(r33, r34, r35, r36, r37);
        r4 = r3.type;
        r5 = 11;
        if (r4 != r5) goto L_0x12d8;
        r4 = r66 | 8;
        goto L_0x12e3;
        r4 = r3.type;
        r5 = 10;
        if (r4 != r5) goto L_0x12e1;
        r4 = r66 | 16;
        goto L_0x12d7;
        r4 = r66;
        if (r55 != 0) goto L_0x12eb;
        r5 = new android.util.LongSparseArray;
        r5.<init>();
        goto L_0x12ed;
        r5 = r55;
        r9 = r0.dialog_id;
        r7 = r5.get(r9);
        r7 = (java.util.ArrayList) r7;
        if (r7 != 0) goto L_0x1302;
        r9 = new java.util.ArrayList;
        r9.<init>();
        r7 = r9;
        r9 = r0.dialog_id;
        r5.put(r9, r7);
        r7.add(r3);
        r9 = r3.isOut();
        if (r9 != 0) goto L_0x1321;
        r9 = r3.isUnread();
        if (r9 == 0) goto L_0x1321;
        if (r39 != 0) goto L_0x131b;
        r9 = new java.util.ArrayList;
        r9.<init>();
        r39 = r9;
        goto L_0x131d;
        r9 = r39;
        r9.add(r3);
        goto L_0x1323;
        r9 = r39;
        r66 = r4;
        r39 = r9;
        r44 = r13;
        r43 = r15;
        r15 = r47;
        r2 = r53;
        r9 = r57;
        r10 = r58;
        r11 = r76;
        r7 = r80;
        r13 = r5;
    L_0x1338:
        r4 = r46 + 1;
        r3 = r8;
        r5 = r45;
        r8 = r66;
        r0 = r88;
        goto L_0x00dc;
    L_0x1343:
        r88 = r0;
        r53 = r2;
        r80 = r7;
        r66 = r8;
        r57 = r9;
        r58 = r10;
        r76 = r11;
        r55 = r13;
        r47 = r15;
        r15 = r43;
        r13 = r44;
        r8 = r3;
        if (r55 == 0) goto L_0x137b;
        r0 = 0;
        r11 = r55;
        r1 = r11.size();
        if (r0 >= r1) goto L_0x137d;
        r2 = r11.keyAt(r0);
        r4 = r11.valueAt(r0);
        r4 = (java.util.ArrayList) r4;
        r5 = r12.updatePrintingUsersWithNewMessages(r2, r4);
        if (r5 == 0) goto L_0x1378;
        r2 = 1;
        r27 = r2;
        r0 = r0 + 1;
        goto L_0x1363;
        r11 = r55;
        if (r27 == 0) goto L_0x1382;
        r122.updatePrintingStrings();
        r1 = r53;
        r2 = r66;
        r0 = r40;
        r3 = r42;
        r9 = r80;
        r7 = r27;
        if (r57 == 0) goto L_0x139c;
        r4 = r12.currentAccount;
        r4 = org.telegram.messenger.ContactsController.getInstance(r4);
        r5 = r57;
        r4.processContactsUpdates(r5, r14);
        goto L_0x139e;
        r5 = r57;
        if (r39 == 0) goto L_0x13b7;
        r4 = r39;
        r10 = r12.currentAccount;
        r10 = org.telegram.messenger.MessagesStorage.getInstance(r10);
        r10 = r10.getStorageQueue();
        r92 = r1;
        r1 = new org.telegram.messenger.MessagesController$131;
        r1.<init>(r4);
        r10.postRunnable(r1);
        goto L_0x13b9;
        r92 = r1;
        if (r6 == 0) goto L_0x13eb;
        r1 = r12.currentAccount;
        r1 = org.telegram.messenger.StatsController.getInstance(r1);
        r4 = org.telegram.tgnet.ConnectionsManager.getCurrentNetworkType();
        r10 = r6.size();
        r93 = r5;
        r5 = 1;
        r1.incrementReceivedItemsCount(r4, r5, r10);
        r1 = r12.currentAccount;
        r28 = org.telegram.messenger.MessagesStorage.getInstance(r1);
        r30 = 1;
        r31 = 1;
        r32 = 0;
        r1 = r12.currentAccount;
        r1 = org.telegram.messenger.DownloadController.getInstance(r1);
        r33 = r1.getAutodownloadMask();
        r29 = r6;
        r28.putMessages(r29, r30, r31, r32, r33);
        goto L_0x13ed;
        r93 = r5;
        if (r0 == 0) goto L_0x144d;
        r1 = 0;
        r4 = r0.size();
        if (r1 >= r4) goto L_0x144d;
        r5 = new org.telegram.tgnet.TLRPC$TL_messages_messages;
        r5.<init>();
        r10 = r0.valueAt(r1);
        r10 = (java.util.ArrayList) r10;
        r16 = 0;
        r17 = r10.size();
        r94 = r4;
        r4 = r16;
        r95 = r17;
        r96 = r6;
        r6 = r95;
        if (r4 >= r6) goto L_0x142f;
        r97 = r6;
        r6 = r5.messages;
        r16 = r10.get(r4);
        r98 = r8;
        r8 = r16;
        r8 = (org.telegram.messenger.MessageObject) r8;
        r8 = r8.messageOwner;
        r6.add(r8);
        r4 = r4 + 1;
        r6 = r96;
        r17 = r97;
        r8 = r98;
        goto L_0x140b;
        r98 = r8;
        r4 = r12.currentAccount;
        r28 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r30 = r0.keyAt(r1);
        r32 = -2;
        r33 = 0;
        r34 = 0;
        r29 = r5;
        r28.putMessages(r29, r30, r32, r33, r34);
        r1 = r1 + 1;
        r4 = r94;
        r6 = r96;
        goto L_0x13f4;
        r96 = r6;
        r98 = r8;
        if (r41 == 0) goto L_0x1460;
        r1 = r12.currentAccount;
        r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
        r8 = r41;
        r5 = 1;
        r1.putChannelViews(r8, r5);
        goto L_0x1463;
        r8 = r41;
        r5 = 1;
        r21 = r96;
        r6 = r0;
        r1 = r58;
        r16 = r88;
        r10 = r8;
        r4 = r3;
        r18 = r5;
        r22 = r93;
        r5 = r11;
        r99 = r9;
        r9 = r19;
        r41 = r8;
        r24 = r98;
        r8 = r22;
        r100 = r3;
        r3 = r20;
        r101 = r11;
        r11 = new org.telegram.messenger.MessagesController$132;
        r40 = r0;
        r25 = r16;
        r0 = r11;
        r102 = r1;
        r103 = r92;
        r1 = r12;
        r0.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r11);
        r0 = r13;
        r29 = r101;
        r13 = r103;
        r1 = r14;
        r11 = r18;
        r14 = r76;
        r16 = r102;
        r105 = r15;
        r104 = r47;
        r15 = r104;
        r17 = r99;
        r18 = r105;
        r11 = r12.currentAccount;
        r11 = org.telegram.messenger.MessagesStorage.getInstance(r11);
        r11 = r11.getStorageQueue();
        r106 = r1;
        r1 = new org.telegram.messenger.MessagesController$133;
        r107 = r2;
        r108 = r3;
        r109 = r4;
        r3 = r11;
        r2 = r76;
        r4 = 1;
        r11 = r1;
        r12 = r122;
        r11.<init>(r13, r14, r15, r16, r17, r18);
        r3.postRunnable(r1);
        r1 = r100;
        if (r1 == 0) goto L_0x14da;
        r3 = r122;
        r11 = r3.currentAccount;
        r11 = org.telegram.messenger.MessagesStorage.getInstance(r11);
        r11.putWebPages(r1);
        goto L_0x14dc;
        r3 = r122;
        r11 = r103;
        if (r11 != 0) goto L_0x14f3;
        if (r2 != 0) goto L_0x14f3;
        r12 = r104;
        if (r12 != 0) goto L_0x14f0;
        r4 = r102;
        if (r4 == 0) goto L_0x14eb;
        goto L_0x14f7;
        r110 = r1;
        r111 = r5;
        goto L_0x1519;
        r4 = r102;
        goto L_0x14f7;
        r4 = r102;
        r12 = r104;
        if (r11 != 0) goto L_0x1502;
        if (r4 == 0) goto L_0x14fc;
        goto L_0x1502;
        r110 = r1;
        r111 = r5;
        r5 = 1;
        goto L_0x1510;
        r110 = r1;
        r1 = r3.currentAccount;
        r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
        r111 = r5;
        r5 = 1;
        r1.updateDialogsWithReadMessages(r11, r2, r4, r5);
        r1 = r3.currentAccount;
        r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
        r1.markMessagesAsRead(r11, r2, r12, r5);
        if (r4 == 0) goto L_0x152e;
        r1 = r3.currentAccount;
        r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
        r5 = r3.currentAccount;
        r5 = org.telegram.tgnet.ConnectionsManager.getInstance(r5);
        r5 = r5.getCurrentTime();
        r1.markMessagesContentAsRead(r4, r5);
        r1 = r99;
        if (r1 == 0) goto L_0x1579;
        r5 = 0;
        r28 = r1.size();
        r112 = r28;
        r113 = r2;
        r2 = r112;
        if (r5 >= r2) goto L_0x1572;
        r114 = r2;
        r2 = r1.keyAt(r5);
        r28 = r1.valueAt(r5);
        r115 = r1;
        r1 = r28;
        r1 = (java.util.ArrayList) r1;
        r116 = r4;
        r4 = r3.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r4 = r4.getStorageQueue();
        r117 = r6;
        r6 = new org.telegram.messenger.MessagesController$134;
        r6.<init>(r1, r2);
        r4.postRunnable(r6);
        r5 = r5 + 1;
        r2 = r113;
        r28 = r114;
        r1 = r115;
        r4 = r116;
        r6 = r117;
        goto L_0x1537;
        r115 = r1;
        r116 = r4;
        r117 = r6;
        goto L_0x1581;
        r115 = r1;
        r113 = r2;
        r116 = r4;
        r117 = r6;
        r1 = r105;
        if (r1 == 0) goto L_0x15b1;
        r2 = 0;
        r4 = r1.size();
        if (r2 >= r4) goto L_0x15b1;
        r5 = r1.keyAt(r2);
        r6 = r1.valueAt(r2);
        r118 = r1;
        r1 = r3.currentAccount;
        r1 = org.telegram.messenger.MessagesStorage.getInstance(r1);
        r1 = r1.getStorageQueue();
        r119 = r4;
        r4 = new org.telegram.messenger.MessagesController$135;
        r4.<init>(r5, r6);
        r1.postRunnable(r4);
        r2 = r2 + 1;
        r1 = r118;
        r4 = r119;
        goto L_0x158a;
        r118 = r1;
        if (r0 == 0) goto L_0x15e2;
        r1 = 0;
        r2 = r0.size();
        if (r1 >= r2) goto L_0x15e2;
        r4 = r0.get(r1);
        r4 = (org.telegram.tgnet.TLRPC.TL_updateEncryptedMessagesRead) r4;
        r5 = r3.currentAccount;
        r30 = org.telegram.messenger.MessagesStorage.getInstance(r5);
        r5 = r4.chat_id;
        r6 = r4.max_date;
        r120 = r0;
        r0 = r4.date;
        r34 = 1;
        r35 = 0;
        r31 = r5;
        r32 = r6;
        r33 = r0;
        r30.createTaskForSecretChat(r31, r32, r33, r34, r35);
        r1 = r1 + 1;
        r0 = r120;
        goto L_0x15ba;
        r120 = r0;
        r0 = 1;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.processUpdateArray(java.util.ArrayList, java.util.ArrayList, java.util.ArrayList, boolean):boolean");
    }

    public static MessagesController getInstance(int num) {
        MessagesController localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (MessagesController.class) {
                localInstance = Instance[num];
                if (localInstance == null) {
                    MessagesController[] messagesControllerArr = Instance;
                    MessagesController messagesController = new MessagesController(num);
                    localInstance = messagesController;
                    messagesControllerArr[num] = messagesController;
                }
            }
        }
        return localInstance;
    }

    public static SharedPreferences getNotificationsSettings(int account) {
        return getInstance(account).notificationsPreferences;
    }

    public static SharedPreferences getGlobalNotificationsSettings() {
        return getInstance(0).notificationsPreferences;
    }

    public static SharedPreferences getMainSettings(int account) {
        return getInstance(account).mainPreferences;
    }

    public static SharedPreferences getGlobalMainSettings() {
        return getInstance(0).mainPreferences;
    }

    public static SharedPreferences getEmojiSettings(int account) {
        return getInstance(account).emojiPreferences;
    }

    public static SharedPreferences getGlobalEmojiSettings() {
        return getInstance(0).emojiPreferences;
    }

    public MessagesController(int num) {
        this.currentAccount = num;
        ImageLoader.getInstance();
        MessagesStorage.getInstance(this.currentAccount);
        LocationController.getInstance(this.currentAccount);
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                MessagesController messagesController = MessagesController.getInstance(MessagesController.this.currentAccount);
                NotificationCenter.getInstance(MessagesController.this.currentAccount).addObserver(messagesController, NotificationCenter.FileDidUpload);
                NotificationCenter.getInstance(MessagesController.this.currentAccount).addObserver(messagesController, NotificationCenter.FileDidFailUpload);
                NotificationCenter.getInstance(MessagesController.this.currentAccount).addObserver(messagesController, NotificationCenter.FileDidLoaded);
                NotificationCenter.getInstance(MessagesController.this.currentAccount).addObserver(messagesController, NotificationCenter.FileDidFailedLoad);
                NotificationCenter.getInstance(MessagesController.this.currentAccount).addObserver(messagesController, NotificationCenter.messageReceivedByServer);
                NotificationCenter.getInstance(MessagesController.this.currentAccount).addObserver(messagesController, NotificationCenter.updateMessageMedia);
            }
        });
        addSupportUser();
        if (this.currentAccount == 0) {
            this.notificationsPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications", 0);
            this.mainPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", 0);
            this.emojiPreferences = ApplicationLoader.applicationContext.getSharedPreferences("emoji", 0);
        } else {
            Context context = ApplicationLoader.applicationContext;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Notifications");
            stringBuilder.append(this.currentAccount);
            this.notificationsPreferences = context.getSharedPreferences(stringBuilder.toString(), 0);
            context = ApplicationLoader.applicationContext;
            stringBuilder = new StringBuilder();
            stringBuilder.append("mainconfig");
            stringBuilder.append(this.currentAccount);
            this.mainPreferences = context.getSharedPreferences(stringBuilder.toString(), 0);
            context = ApplicationLoader.applicationContext;
            stringBuilder = new StringBuilder();
            stringBuilder.append("emoji");
            stringBuilder.append(this.currentAccount);
            this.emojiPreferences = context.getSharedPreferences(stringBuilder.toString(), 0);
        }
        this.enableJoined = this.notificationsPreferences.getBoolean("EnableContactJoined", true);
        this.secretWebpagePreview = this.mainPreferences.getInt("secretWebpage2", 2);
        this.maxGroupCount = this.mainPreferences.getInt("maxGroupCount", Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.maxMegagroupCount = this.mainPreferences.getInt("maxMegagroupCount", 10000);
        this.maxRecentGifsCount = this.mainPreferences.getInt("maxRecentGifsCount", Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.maxRecentStickersCount = this.mainPreferences.getInt("maxRecentStickersCount", 30);
        this.maxFaveStickersCount = this.mainPreferences.getInt("maxFaveStickersCount", 5);
        this.maxEditTime = this.mainPreferences.getInt("maxEditTime", 3600);
        this.ratingDecay = this.mainPreferences.getInt("ratingDecay", 2419200);
        this.linkPrefix = this.mainPreferences.getString("linkPrefix", "t.me");
        this.callReceiveTimeout = this.mainPreferences.getInt("callReceiveTimeout", 20000);
        this.callRingTimeout = this.mainPreferences.getInt("callRingTimeout", 90000);
        this.callConnectTimeout = this.mainPreferences.getInt("callConnectTimeout", DefaultLoadControl.DEFAULT_MAX_BUFFER_MS);
        this.callPacketTimeout = this.mainPreferences.getInt("callPacketTimeout", 10000);
        this.maxPinnedDialogsCount = this.mainPreferences.getInt("maxPinnedDialogsCount", 5);
        this.installReferer = this.mainPreferences.getString("installReferer", null);
        this.defaultP2pContacts = this.mainPreferences.getBoolean("defaultP2pContacts", false);
        this.revokeTimeLimit = this.mainPreferences.getInt("revokeTimeLimit", this.revokeTimeLimit);
        this.revokeTimePmLimit = this.mainPreferences.getInt("revokeTimePmLimit", this.revokeTimePmLimit);
        this.canRevokePmInbox = this.mainPreferences.getBoolean("canRevokePmInbox", this.canRevokePmInbox);
        this.preloadFeaturedStickers = this.mainPreferences.getBoolean("preloadFeaturedStickers", false);
    }

    public void updateConfig(final TL_config config) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                LocaleController.getInstance().loadRemoteLanguages(MessagesController.this.currentAccount);
                MessagesController.this.maxMegagroupCount = config.megagroup_size_max;
                MessagesController.this.maxGroupCount = config.chat_size_max;
                MessagesController.this.maxEditTime = config.edit_time_limit;
                MessagesController.this.ratingDecay = config.rating_e_decay;
                MessagesController.this.maxRecentGifsCount = config.saved_gifs_limit;
                MessagesController.this.maxRecentStickersCount = config.stickers_recent_limit;
                MessagesController.this.maxFaveStickersCount = config.stickers_faved_limit;
                MessagesController.this.revokeTimeLimit = config.revoke_time_limit;
                MessagesController.this.revokeTimePmLimit = config.revoke_pm_time_limit;
                MessagesController.this.canRevokePmInbox = config.revoke_pm_inbox;
                MessagesController.this.linkPrefix = config.me_url_prefix;
                if (MessagesController.this.linkPrefix.endsWith("/")) {
                    MessagesController.this.linkPrefix = MessagesController.this.linkPrefix.substring(0, MessagesController.this.linkPrefix.length() - 1);
                }
                if (MessagesController.this.linkPrefix.startsWith("https://")) {
                    MessagesController.this.linkPrefix = MessagesController.this.linkPrefix.substring(8);
                } else if (MessagesController.this.linkPrefix.startsWith("http://")) {
                    MessagesController.this.linkPrefix = MessagesController.this.linkPrefix.substring(7);
                }
                MessagesController.this.callReceiveTimeout = config.call_receive_timeout_ms;
                MessagesController.this.callRingTimeout = config.call_ring_timeout_ms;
                MessagesController.this.callConnectTimeout = config.call_connect_timeout_ms;
                MessagesController.this.callPacketTimeout = config.call_packet_timeout_ms;
                MessagesController.this.maxPinnedDialogsCount = config.pinned_dialogs_count_max;
                MessagesController.this.defaultP2pContacts = config.default_p2p_contacts;
                MessagesController.this.preloadFeaturedStickers = config.preload_featured_stickers;
                Editor editor = MessagesController.this.mainPreferences.edit();
                editor.putInt("maxGroupCount", MessagesController.this.maxGroupCount);
                editor.putInt("maxMegagroupCount", MessagesController.this.maxMegagroupCount);
                editor.putInt("maxEditTime", MessagesController.this.maxEditTime);
                editor.putInt("ratingDecay", MessagesController.this.ratingDecay);
                editor.putInt("maxRecentGifsCount", MessagesController.this.maxRecentGifsCount);
                editor.putInt("maxRecentStickersCount", MessagesController.this.maxRecentStickersCount);
                editor.putInt("maxFaveStickersCount", MessagesController.this.maxFaveStickersCount);
                editor.putInt("callReceiveTimeout", MessagesController.this.callReceiveTimeout);
                editor.putInt("callRingTimeout", MessagesController.this.callRingTimeout);
                editor.putInt("callConnectTimeout", MessagesController.this.callConnectTimeout);
                editor.putInt("callPacketTimeout", MessagesController.this.callPacketTimeout);
                editor.putString("linkPrefix", MessagesController.this.linkPrefix);
                editor.putInt("maxPinnedDialogsCount", MessagesController.this.maxPinnedDialogsCount);
                editor.putBoolean("defaultP2pContacts", MessagesController.this.defaultP2pContacts);
                editor.putBoolean("preloadFeaturedStickers", MessagesController.this.preloadFeaturedStickers);
                editor.putInt("revokeTimeLimit", MessagesController.this.revokeTimeLimit);
                editor.putInt("revokeTimePmLimit", MessagesController.this.revokeTimePmLimit);
                editor.putBoolean("canRevokePmInbox", MessagesController.this.canRevokePmInbox);
                editor.commit();
                LocaleController.getInstance().checkUpdateForCurrentRemoteLocale(MessagesController.this.currentAccount, config.lang_pack_version);
            }
        });
    }

    public void addSupportUser() {
        TL_userForeign_old2 user = new TL_userForeign_old2();
        user.phone = "333";
        user.id = 333000;
        user.first_name = "Telegram";
        user.last_name = TtmlNode.ANONYMOUS_REGION_ID;
        user.status = null;
        user.photo = new TL_userProfilePhotoEmpty();
        putUser(user, true);
        user = new TL_userForeign_old2();
        user.phone = "42777";
        user.id = 777000;
        user.first_name = "Telegram";
        user.last_name = "Notifications";
        user.status = null;
        user.photo = new TL_userProfilePhotoEmpty();
        putUser(user, true);
    }

    public InputUser getInputUser(User user) {
        if (user == null) {
            return new TL_inputUserEmpty();
        }
        InputUser inputUser;
        if (user.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            inputUser = new TL_inputUserSelf();
        } else {
            inputUser = new TL_inputUser();
            inputUser.user_id = user.id;
            inputUser.access_hash = user.access_hash;
        }
        return inputUser;
    }

    public InputUser getInputUser(int user_id) {
        return getInputUser(getInstance(UserConfig.selectedAccount).getUser(Integer.valueOf(user_id)));
    }

    public static InputChannel getInputChannel(Chat chat) {
        if (!(chat instanceof TL_channel)) {
            if (!(chat instanceof TL_channelForbidden)) {
                return new TL_inputChannelEmpty();
            }
        }
        InputChannel inputChat = new TL_inputChannel();
        inputChat.channel_id = chat.id;
        inputChat.access_hash = chat.access_hash;
        return inputChat;
    }

    public InputChannel getInputChannel(int chatId) {
        return getInputChannel(getChat(Integer.valueOf(chatId)));
    }

    public InputPeer getInputPeer(int id) {
        InputPeer tL_inputPeerChannel;
        if (id < 0) {
            Chat chat = getChat(Integer.valueOf(-id));
            if (ChatObject.isChannel(chat)) {
                tL_inputPeerChannel = new TL_inputPeerChannel();
                tL_inputPeerChannel.channel_id = -id;
                tL_inputPeerChannel.access_hash = chat.access_hash;
            } else {
                tL_inputPeerChannel = new TL_inputPeerChat();
                tL_inputPeerChannel.chat_id = -id;
            }
        } else {
            User user = getUser(Integer.valueOf(id));
            tL_inputPeerChannel = new TL_inputPeerUser();
            tL_inputPeerChannel.user_id = id;
            if (user != null) {
                tL_inputPeerChannel.access_hash = user.access_hash;
            }
        }
        return tL_inputPeerChannel;
    }

    public Peer getPeer(int id) {
        Peer inputPeer;
        if (id < 0) {
            Chat chat = getChat(Integer.valueOf(-id));
            if (!(chat instanceof TL_channel)) {
                if (!(chat instanceof TL_channelForbidden)) {
                    inputPeer = new TL_peerChat();
                    inputPeer.chat_id = -id;
                    return inputPeer;
                }
            }
            inputPeer = new TL_peerChannel();
            inputPeer.channel_id = -id;
            return inputPeer;
        }
        User user = getUser(Integer.valueOf(id));
        inputPeer = new TL_peerUser();
        inputPeer.user_id = id;
        return inputPeer;
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        String location;
        if (id == NotificationCenter.FileDidUpload) {
            location = args[0];
            InputFile file = args[1];
            if (this.uploadingAvatar != null && this.uploadingAvatar.equals(location)) {
                TL_photos_uploadProfilePhoto req = new TL_photos_uploadProfilePhoto();
                req.file = file;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            User user = MessagesController.this.getUser(Integer.valueOf(UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()));
                            if (user == null) {
                                user = UserConfig.getInstance(MessagesController.this.currentAccount).getCurrentUser();
                                MessagesController.this.putUser(user, true);
                            } else {
                                UserConfig.getInstance(MessagesController.this.currentAccount).setCurrentUser(user);
                            }
                            if (user != null) {
                                TL_photos_photo photo = (TL_photos_photo) response;
                                ArrayList<PhotoSize> sizes = photo.photo.sizes;
                                PhotoSize smallSize = FileLoader.getClosestPhotoSizeWithSize(sizes, 100);
                                PhotoSize bigSize = FileLoader.getClosestPhotoSizeWithSize(sizes, 1000);
                                user.photo = new TL_userProfilePhoto();
                                user.photo.photo_id = photo.photo.id;
                                if (smallSize != null) {
                                    user.photo.photo_small = smallSize.location;
                                }
                                if (bigSize != null) {
                                    user.photo.photo_big = bigSize.location;
                                } else if (smallSize != null) {
                                    user.photo.photo_small = smallSize.location;
                                }
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).clearUserPhotos(user.id);
                                ArrayList<User> users = new ArrayList();
                                users.add(user);
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(users, null, false, true);
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(2));
                                        UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(true);
                                    }
                                });
                            }
                        }
                    }
                });
            }
        } else if (id == NotificationCenter.FileDidFailUpload) {
            location = (String) args[0];
            if (this.uploadingAvatar != null && this.uploadingAvatar.equals(location)) {
                this.uploadingAvatar = null;
            }
        } else if (id == NotificationCenter.messageReceivedByServer) {
            Integer msgId = args[0];
            Integer newMsgId = args[1];
            Long did = args[3];
            MessageObject obj = (MessageObject) this.dialogMessage.get(did.longValue());
            if (obj != null && (obj.getId() == msgId.intValue() || obj.messageOwner.local_id == msgId.intValue())) {
                obj.messageOwner.id = newMsgId.intValue();
                obj.messageOwner.send_state = 0;
            }
            TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(did.longValue());
            if (dialog != null && dialog.top_message == msgId.intValue()) {
                dialog.top_message = newMsgId.intValue();
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            MessageObject obj2 = (MessageObject) this.dialogMessagesByIds.get(msgId.intValue());
            this.dialogMessagesByIds.remove(msgId.intValue());
            if (obj2 != null) {
                this.dialogMessagesByIds.put(newMsgId.intValue(), obj2);
            }
        } else if (id == NotificationCenter.updateMessageMedia) {
            Message message = args[0];
            MessageObject existMessageObject = (MessageObject) this.dialogMessagesByIds.get(message.id);
            if (existMessageObject != null) {
                existMessageObject.messageOwner.media = message.media;
                if (message.media.ttl_seconds == 0) {
                    return;
                }
                if ((message.media.photo instanceof TL_photoEmpty) || (message.media.document instanceof TL_documentEmpty)) {
                    existMessageObject.setType();
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
                }
            }
        }
    }

    public void cleanup() {
        ContactsController.getInstance(this.currentAccount).cleanup();
        MediaController.getInstance().cleanup();
        NotificationsController.getInstance(this.currentAccount).cleanup();
        SendMessagesHelper.getInstance(this.currentAccount).cleanup();
        SecretChatHelper.getInstance(this.currentAccount).cleanup();
        LocationController.getInstance(this.currentAccount).cleanup();
        DataQuery.getInstance(this.currentAccount).cleanup();
        DialogsActivity.dialogsLoaded[this.currentAccount] = false;
        this.reloadingWebpages.clear();
        this.reloadingWebpagesPending.clear();
        this.dialogs_dict.clear();
        this.dialogs_read_inbox_max.clear();
        this.dialogs_read_outbox_max.clear();
        this.exportedChats.clear();
        this.fullUsers.clear();
        this.dialogs.clear();
        this.joiningToChannels.clear();
        this.channelViewsToSend.clear();
        this.dialogsServerOnly.clear();
        this.dialogsForward.clear();
        this.dialogsGroupsOnly.clear();
        this.dialogMessagesByIds.clear();
        this.dialogMessagesByRandomIds.clear();
        this.channelAdmins.clear();
        this.loadingChannelAdmins.clear();
        this.users.clear();
        this.objectsByUsernames.clear();
        this.chats.clear();
        this.dialogMessage.clear();
        this.printingUsers.clear();
        this.printingStrings.clear();
        this.printingStringsTypes.clear();
        this.onlinePrivacy.clear();
        this.loadingPeerSettings.clear();
        this.lastPrintingStringCount = 0;
        this.nextDialogsCacheOffset = 0;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.this.readTasks.clear();
                MessagesController.this.readTasksMap.clear();
                MessagesController.this.updatesQueueSeq.clear();
                MessagesController.this.updatesQueuePts.clear();
                MessagesController.this.updatesQueueQts.clear();
                MessagesController.this.gettingUnknownChannels.clear();
                MessagesController.this.updatesStartWaitTimeSeq = 0;
                MessagesController.this.updatesStartWaitTimePts = 0;
                MessagesController.this.updatesStartWaitTimeQts = 0;
                MessagesController.this.createdDialogIds.clear();
                MessagesController.this.gettingDifference = false;
                MessagesController.this.resetDialogsPinned = null;
                MessagesController.this.resetDialogsAll = null;
            }
        });
        this.createdDialogMainThreadIds.clear();
        this.blockedUsers.clear();
        this.sendingTypings.clear();
        this.loadingFullUsers.clear();
        this.loadedFullUsers.clear();
        this.reloadingMessages.clear();
        this.loadingFullChats.clear();
        this.loadingFullParticipants.clear();
        this.loadedFullParticipants.clear();
        this.loadedFullChats.clear();
        this.currentDeletingTaskTime = 0;
        this.currentDeletingTaskMids = null;
        this.currentDeletingTaskChannelId = 0;
        this.gettingNewDeleteTask = false;
        this.loadingDialogs = false;
        this.dialogsEndReached = false;
        this.serverDialogsEndReached = false;
        this.loadingBlockedUsers = false;
        this.firstGettingTask = false;
        this.updatingState = false;
        this.resetingDialogs = false;
        this.lastStatusUpdateTime = 0;
        this.offlineSent = false;
        this.registeringForPush = false;
        this.getDifferenceFirstSync = true;
        this.uploadingAvatar = null;
        this.statusRequest = 0;
        this.statusSettingState = 0;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                ConnectionsManager.getInstance(MessagesController.this.currentAccount).setIsUpdating(false);
                MessagesController.this.updatesQueueChannels.clear();
                MessagesController.this.updatesStartWaitTimeChannels.clear();
                MessagesController.this.gettingDifferenceChannels.clear();
                MessagesController.this.channelsPts.clear();
                MessagesController.this.shortPollChannels.clear();
                MessagesController.this.needShortPollChannels.clear();
            }
        });
        if (this.currentDeleteTaskRunnable != null) {
            Utilities.stageQueue.cancelRunnable(this.currentDeleteTaskRunnable);
            this.currentDeleteTaskRunnable = null;
        }
        addSupportUser();
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
    }

    public User getUser(Integer id) {
        return (User) this.users.get(id);
    }

    public TLObject getUserOrChat(String username) {
        if (username != null) {
            if (username.length() != 0) {
                return (TLObject) this.objectsByUsernames.get(username.toLowerCase());
            }
        }
        return null;
    }

    public ConcurrentHashMap<Integer, User> getUsers() {
        return this.users;
    }

    public Chat getChat(Integer id) {
        return (Chat) this.chats.get(id);
    }

    public EncryptedChat getEncryptedChat(Integer id) {
        return (EncryptedChat) this.encryptedChats.get(id);
    }

    public EncryptedChat getEncryptedChatDB(int chat_id, boolean created) {
        EncryptedChat chat = (EncryptedChat) this.encryptedChats.get(Integer.valueOf(chat_id));
        if (chat != null) {
            if (!created) {
                return chat;
            }
            if (!((chat instanceof TL_encryptedChatWaiting) || (chat instanceof TL_encryptedChatRequested))) {
                return chat;
            }
        }
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ArrayList<TLObject> result = new ArrayList();
        MessagesStorage.getInstance(this.currentAccount).getEncryptedChat(chat_id, countDownLatch, result);
        try {
            countDownLatch.await();
        } catch (Throwable e) {
            FileLog.e(e);
        }
        if (result.size() != 2) {
            return chat;
        }
        chat = (EncryptedChat) result.get(0);
        User user = (User) result.get(1);
        putEncryptedChat(chat, false);
        putUser(user, true);
        return chat;
    }

    public boolean isDialogCreated(long dialog_id) {
        return this.createdDialogMainThreadIds.contains(Long.valueOf(dialog_id));
    }

    public void setLastCreatedDialogId(final long dialog_id, final boolean set) {
        if (set) {
            this.createdDialogMainThreadIds.add(Long.valueOf(dialog_id));
        } else {
            this.createdDialogMainThreadIds.remove(Long.valueOf(dialog_id));
        }
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                if (set) {
                    MessagesController.this.createdDialogIds.add(Long.valueOf(dialog_id));
                } else {
                    MessagesController.this.createdDialogIds.remove(Long.valueOf(dialog_id));
                }
            }
        });
    }

    public ExportedChatInvite getExportedInvite(int chat_id) {
        return (ExportedChatInvite) this.exportedChats.get(chat_id);
    }

    public boolean putUser(User user, boolean fromCache) {
        if (user == null) {
            return false;
        }
        boolean z = (!fromCache || user.id / 1000 == 333 || user.id == 777000) ? false : true;
        fromCache = z;
        User oldUser = (User) this.users.get(Integer.valueOf(user.id));
        if (oldUser == user) {
            return false;
        }
        if (!(oldUser == null || TextUtils.isEmpty(oldUser.username))) {
            this.objectsByUsernames.remove(oldUser.username.toLowerCase());
        }
        if (!TextUtils.isEmpty(user.username)) {
            this.objectsByUsernames.put(user.username.toLowerCase(), user);
        }
        if (user.min) {
            if (oldUser == null) {
                this.users.put(Integer.valueOf(user.id), user);
            } else if (!fromCache) {
                if (user.bot) {
                    if (user.username != null) {
                        oldUser.username = user.username;
                        oldUser.flags |= 8;
                    } else {
                        oldUser.flags &= -9;
                        oldUser.username = null;
                    }
                }
                if (user.photo != null) {
                    oldUser.photo = user.photo;
                    oldUser.flags |= 32;
                } else {
                    oldUser.flags &= -33;
                    oldUser.photo = null;
                }
            }
        } else if (!fromCache) {
            this.users.put(Integer.valueOf(user.id), user);
            if (user.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                UserConfig.getInstance(this.currentAccount).setCurrentUser(user);
                UserConfig.getInstance(this.currentAccount).saveConfig(true);
            }
            if (oldUser == null || user.status == null || oldUser.status == null || user.status.expires == oldUser.status.expires) {
                return false;
            }
            return true;
        } else if (oldUser == null) {
            this.users.put(Integer.valueOf(user.id), user);
        } else if (oldUser.min) {
            user.min = false;
            if (oldUser.bot) {
                if (oldUser.username != null) {
                    user.username = oldUser.username;
                    user.flags |= 8;
                } else {
                    user.flags &= -9;
                    user.username = null;
                }
            }
            if (oldUser.photo != null) {
                user.photo = oldUser.photo;
                user.flags |= 32;
            } else {
                user.flags &= -33;
                user.photo = null;
            }
            this.users.put(Integer.valueOf(user.id), user);
        }
        return false;
    }

    public void putUsers(ArrayList<User> users, boolean fromCache) {
        if (users != null) {
            if (!users.isEmpty()) {
                boolean updateStatus = false;
                int count = users.size();
                for (int a = 0; a < count; a++) {
                    if (putUser((User) users.get(a), fromCache)) {
                        updateStatus = true;
                    }
                }
                if (updateStatus) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(4));
                        }
                    });
                }
            }
        }
    }

    public void putChat(final Chat chat, boolean fromCache) {
        if (chat != null) {
            Chat oldChat = (Chat) this.chats.get(Integer.valueOf(chat.id));
            if (oldChat != chat) {
                if (!(oldChat == null || TextUtils.isEmpty(oldChat.username))) {
                    this.objectsByUsernames.remove(oldChat.username.toLowerCase());
                }
                if (!TextUtils.isEmpty(chat.username)) {
                    this.objectsByUsernames.put(chat.username.toLowerCase(), chat);
                }
                if (!chat.min) {
                    boolean z = false;
                    if (!fromCache) {
                        if (oldChat != null) {
                            if (chat.version != oldChat.version) {
                                this.loadedFullChats.remove(Integer.valueOf(chat.id));
                            }
                            if (oldChat.participants_count != 0 && chat.participants_count == 0) {
                                chat.participants_count = oldChat.participants_count;
                                chat.flags = 131072 | chat.flags;
                            }
                            boolean oldFlags = oldChat.banned_rights != null ? oldChat.banned_rights.flags : false;
                            if (chat.banned_rights != null) {
                                z = chat.banned_rights.flags;
                            }
                            if (oldFlags != z) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.channelRightsUpdated, chat);
                                    }
                                });
                            }
                        }
                        this.chats.put(Integer.valueOf(chat.id), chat);
                    } else if (oldChat == null) {
                        this.chats.put(Integer.valueOf(chat.id), chat);
                    } else if (oldChat.min) {
                        chat.min = false;
                        chat.title = oldChat.title;
                        chat.photo = oldChat.photo;
                        chat.broadcast = oldChat.broadcast;
                        chat.verified = oldChat.verified;
                        chat.megagroup = oldChat.megagroup;
                        chat.democracy = oldChat.democracy;
                        if (oldChat.username != null) {
                            chat.username = oldChat.username;
                            chat.flags |= 64;
                        } else {
                            chat.flags &= -65;
                            chat.username = null;
                        }
                        if (oldChat.participants_count != 0 && chat.participants_count == 0) {
                            chat.participants_count = oldChat.participants_count;
                            chat.flags = 131072 | chat.flags;
                        }
                        this.chats.put(Integer.valueOf(chat.id), chat);
                    }
                } else if (oldChat == null) {
                    this.chats.put(Integer.valueOf(chat.id), chat);
                } else if (!fromCache) {
                    oldChat.title = chat.title;
                    oldChat.photo = chat.photo;
                    oldChat.broadcast = chat.broadcast;
                    oldChat.verified = chat.verified;
                    oldChat.megagroup = chat.megagroup;
                    oldChat.democracy = chat.democracy;
                    if (chat.username != null) {
                        oldChat.username = chat.username;
                        oldChat.flags |= 64;
                    } else {
                        oldChat.flags &= -65;
                        oldChat.username = null;
                    }
                    if (chat.participants_count != 0) {
                        oldChat.participants_count = chat.participants_count;
                    }
                }
            }
        }
    }

    public void putChats(ArrayList<Chat> chats, boolean fromCache) {
        if (chats != null) {
            if (!chats.isEmpty()) {
                int count = chats.size();
                for (int a = 0; a < count; a++) {
                    putChat((Chat) chats.get(a), fromCache);
                }
            }
        }
    }

    public void setReferer(String referer) {
        if (referer != null) {
            this.installReferer = referer;
            this.mainPreferences.edit().putString("installReferer", referer).commit();
        }
    }

    public void putEncryptedChat(EncryptedChat encryptedChat, boolean fromCache) {
        if (encryptedChat != null) {
            if (fromCache) {
                this.encryptedChats.putIfAbsent(Integer.valueOf(encryptedChat.id), encryptedChat);
            } else {
                this.encryptedChats.put(Integer.valueOf(encryptedChat.id), encryptedChat);
            }
        }
    }

    public void putEncryptedChats(ArrayList<EncryptedChat> encryptedChats, boolean fromCache) {
        if (encryptedChats != null) {
            if (!encryptedChats.isEmpty()) {
                int count = encryptedChats.size();
                for (int a = 0; a < count; a++) {
                    putEncryptedChat((EncryptedChat) encryptedChats.get(a), fromCache);
                }
            }
        }
    }

    public TL_userFull getUserFull(int uid) {
        return (TL_userFull) this.fullUsers.get(uid);
    }

    public void cancelLoadFullUser(int uid) {
        this.loadingFullUsers.remove(Integer.valueOf(uid));
    }

    public void cancelLoadFullChat(int cid) {
        this.loadingFullChats.remove(Integer.valueOf(cid));
    }

    protected void clearFullUsers() {
        this.loadedFullUsers.clear();
        this.loadedFullChats.clear();
    }

    private void reloadDialogsReadValue(ArrayList<TL_dialog> dialogs, long did) {
        if (did != 0 || (dialogs != null && !dialogs.isEmpty())) {
            TL_messages_getPeerDialogs req = new TL_messages_getPeerDialogs();
            if (dialogs != null) {
                for (int a = 0; a < dialogs.size(); a++) {
                    InputPeer inputPeer = getInputPeer((int) ((TL_dialog) dialogs.get(a)).id);
                    if (!(inputPeer instanceof TL_inputPeerChannel) || inputPeer.access_hash != 0) {
                        TL_inputDialogPeer inputDialogPeer = new TL_inputDialogPeer();
                        inputDialogPeer.peer = inputPeer;
                        req.peers.add(inputDialogPeer);
                    }
                }
            } else {
                InputPeer inputPeer2 = getInputPeer((int) did);
                if (!(inputPeer2 instanceof TL_inputPeerChannel) || inputPeer2.access_hash != 0) {
                    TL_inputDialogPeer inputDialogPeer2 = new TL_inputDialogPeer();
                    inputDialogPeer2.peer = inputPeer2;
                    req.peers.add(inputDialogPeer2);
                } else {
                    return;
                }
            }
            if (!req.peers.isEmpty()) {
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (response != null) {
                            TL_messages_peerDialogs res = (TL_messages_peerDialogs) response;
                            ArrayList<Update> arrayList = new ArrayList();
                            for (int a = 0; a < res.dialogs.size(); a++) {
                                TL_dialog dialog = (TL_dialog) res.dialogs.get(a);
                                if (dialog.read_inbox_max_id == 0) {
                                    dialog.read_inbox_max_id = 1;
                                }
                                if (dialog.read_outbox_max_id == 0) {
                                    dialog.read_outbox_max_id = 1;
                                }
                                if (dialog.id == 0 && dialog.peer != null) {
                                    if (dialog.peer.user_id != 0) {
                                        dialog.id = (long) dialog.peer.user_id;
                                    } else if (dialog.peer.chat_id != 0) {
                                        dialog.id = (long) (-dialog.peer.chat_id);
                                    } else if (dialog.peer.channel_id != 0) {
                                        dialog.id = (long) (-dialog.peer.channel_id);
                                    }
                                }
                                Integer value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(dialog.id));
                                if (value == null) {
                                    value = Integer.valueOf(0);
                                }
                                MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(dialog.id), Integer.valueOf(Math.max(dialog.read_inbox_max_id, value.intValue())));
                                if (value.intValue() == 0) {
                                    if (dialog.peer.channel_id != 0) {
                                        TL_updateReadChannelInbox update = new TL_updateReadChannelInbox();
                                        update.channel_id = dialog.peer.channel_id;
                                        update.max_id = dialog.read_inbox_max_id;
                                        arrayList.add(update);
                                    } else {
                                        TL_updateReadHistoryInbox update2 = new TL_updateReadHistoryInbox();
                                        update2.peer = dialog.peer;
                                        update2.max_id = dialog.read_inbox_max_id;
                                        arrayList.add(update2);
                                    }
                                }
                                value = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(dialog.id));
                                if (value == null) {
                                    value = Integer.valueOf(0);
                                }
                                MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(dialog.id), Integer.valueOf(Math.max(dialog.read_outbox_max_id, value.intValue())));
                                if (value.intValue() == 0) {
                                    if (dialog.peer.channel_id != 0) {
                                        TL_updateReadChannelOutbox update3 = new TL_updateReadChannelOutbox();
                                        update3.channel_id = dialog.peer.channel_id;
                                        update3.max_id = dialog.read_outbox_max_id;
                                        arrayList.add(update3);
                                    } else {
                                        TL_updateReadHistoryOutbox update4 = new TL_updateReadHistoryOutbox();
                                        update4.peer = dialog.peer;
                                        update4.max_id = dialog.read_outbox_max_id;
                                        arrayList.add(update4);
                                    }
                                }
                            }
                            if (!arrayList.isEmpty()) {
                                MessagesController.this.processUpdateArray(arrayList, null, null, false);
                            }
                        }
                    }
                });
            }
        }
    }

    public boolean isChannelAdmin(int chatId, int uid) {
        ArrayList<Integer> array = (ArrayList) this.channelAdmins.get(chatId);
        return array != null && array.indexOf(Integer.valueOf(uid)) >= 0;
    }

    public void loadChannelAdmins(final int chatId, boolean cache) {
        if (this.loadingChannelAdmins.indexOfKey(chatId) < 0) {
            int a = 0;
            this.loadingChannelAdmins.put(chatId, 0);
            if (cache) {
                MessagesStorage.getInstance(this.currentAccount).loadChannelAdmins(chatId);
            } else {
                TL_channels_getParticipants req = new TL_channels_getParticipants();
                ArrayList<Integer> array = (ArrayList) this.channelAdmins.get(chatId);
                if (array != null) {
                    long acc = 0;
                    while (a < array.size()) {
                        acc = (((20261 * acc) + 2147483648L) + ((long) ((Integer) array.get(a)).intValue())) % 2147483648L;
                        a++;
                    }
                    req.hash = (int) acc;
                }
                req.channel = getInputChannel(chatId);
                req.limit = 100;
                req.filter = new TL_channelParticipantsAdmins();
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (response instanceof TL_channels_channelParticipants) {
                            TL_channels_channelParticipants participants = (TL_channels_channelParticipants) response;
                            ArrayList<Integer> array = new ArrayList(participants.participants.size());
                            for (int a = 0; a < participants.participants.size(); a++) {
                                array.add(Integer.valueOf(((ChannelParticipant) participants.participants.get(a)).user_id));
                            }
                            MessagesController.this.processLoadedChannelAdmins(array, chatId, false);
                        }
                    }
                });
            }
        }
    }

    public void processLoadedChannelAdmins(final ArrayList<Integer> array, final int chatId, final boolean cache) {
        Collections.sort(array);
        if (!cache) {
            MessagesStorage.getInstance(this.currentAccount).putChannelAdmins(chatId, array);
        }
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                MessagesController.this.loadingChannelAdmins.delete(chatId);
                MessagesController.this.channelAdmins.put(chatId, array);
                if (cache) {
                    MessagesController.this.loadChannelAdmins(chatId, false);
                }
            }
        });
    }

    public void loadFullUser(final User user, final int classGuid, boolean force) {
        if (!(user == null || this.loadingFullUsers.contains(Integer.valueOf(user.id)))) {
            if (force || !this.loadedFullUsers.contains(Integer.valueOf(user.id))) {
                this.loadingFullUsers.add(Integer.valueOf(user.id));
                TL_users_getFullUser req = new TL_users_getFullUser();
                req.id = getInputUser(user);
                long dialog_id = (long) user.id;
                if (this.dialogs_read_inbox_max.get(Long.valueOf(dialog_id)) == null || this.dialogs_read_outbox_max.get(Long.valueOf(dialog_id)) == null) {
                    reloadDialogsReadValue(null, dialog_id);
                }
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, TL_error error) {
                        if (error == null) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    TL_userFull userFull = response;
                                    MessagesController.this.applyDialogNotificationsSettings((long) user.id, userFull.notify_settings);
                                    if (userFull.bot_info instanceof TL_botInfo) {
                                        DataQuery.getInstance(MessagesController.this.currentAccount).putBotInfo(userFull.bot_info);
                                    }
                                    MessagesController.this.fullUsers.put(user.id, userFull);
                                    MessagesController.this.loadingFullUsers.remove(Integer.valueOf(user.id));
                                    MessagesController.this.loadedFullUsers.add(Integer.valueOf(user.id));
                                    String names = new StringBuilder();
                                    names.append(user.first_name);
                                    names.append(user.last_name);
                                    names.append(user.username);
                                    names = names.toString();
                                    ArrayList<User> users = new ArrayList();
                                    users.add(userFull.user);
                                    MessagesController.this.putUsers(users, false);
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(users, null, false, true);
                                    if (names != null) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append(userFull.user.first_name);
                                        stringBuilder.append(userFull.user.last_name);
                                        stringBuilder.append(userFull.user.username);
                                        if (!names.equals(stringBuilder.toString())) {
                                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(1));
                                        }
                                    }
                                    if (userFull.bot_info instanceof TL_botInfo) {
                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.botInfoDidLoaded, userFull.bot_info, Integer.valueOf(classGuid));
                                    }
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.userInfoDidLoaded, Integer.valueOf(user.id), userFull);
                                }
                            });
                        } else {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.loadingFullUsers.remove(Integer.valueOf(user.id));
                                }
                            });
                        }
                    }
                }), classGuid);
            }
        }
    }

    private void reloadMessages(ArrayList<Integer> mids, long dialog_id) {
        if (!mids.isEmpty()) {
            TLObject request;
            ArrayList<Integer> result = new ArrayList();
            Chat chat = ChatObject.getChatByDialog(dialog_id, this.currentAccount);
            if (ChatObject.isChannel(chat)) {
                request = new TL_channels_getMessages();
                request.channel = getInputChannel(chat);
                request.id = result;
            } else {
                request = new TL_messages_getMessages();
                request.id = result;
            }
            TLObject request2 = request;
            ArrayList<Integer> arrayList = (ArrayList) this.reloadingMessages.get(dialog_id);
            for (int a = 0; a < mids.size(); a++) {
                Integer mid = (Integer) mids.get(a);
                if (arrayList == null || !arrayList.contains(mid)) {
                    result.add(mid);
                }
            }
            if (!result.isEmpty()) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    this.reloadingMessages.put(dialog_id, arrayList);
                }
                arrayList.addAll(result);
                final long j = dialog_id;
                final Chat chat2 = chat;
                final ArrayList<Integer> arrayList2 = result;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(request2, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        AnonymousClass17 anonymousClass17 = this;
                        if (error == null) {
                            int a;
                            messages_Messages messagesRes = (messages_Messages) response;
                            SparseArray<User> usersLocal = new SparseArray();
                            boolean z = false;
                            for (a = 0; a < messagesRes.users.size(); a++) {
                                User u = (User) messagesRes.users.get(a);
                                usersLocal.put(u.id, u);
                            }
                            SparseArray<Chat> chatsLocal = new SparseArray();
                            for (a = 0; a < messagesRes.chats.size(); a++) {
                                Chat c = (Chat) messagesRes.chats.get(a);
                                chatsLocal.put(c.id, c);
                            }
                            Integer inboxValue = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(j));
                            if (inboxValue == null) {
                                inboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, j));
                                MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(j), inboxValue);
                            }
                            Integer inboxValue2 = inboxValue;
                            inboxValue = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(j));
                            boolean z2 = true;
                            if (inboxValue == null) {
                                inboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, j));
                                MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(j), inboxValue);
                            }
                            Integer outboxValue = inboxValue;
                            ArrayList<MessageObject> objects = new ArrayList();
                            a = 0;
                            while (true) {
                                int a2 = a;
                                ArrayList<MessageObject> objects2;
                                if (a2 < messagesRes.messages.size()) {
                                    Message message = (Message) messagesRes.messages.get(a2);
                                    if (chat2 != null && chat2.megagroup) {
                                        message.flags |= Integer.MIN_VALUE;
                                    }
                                    message.dialog_id = j;
                                    message.unread = (message.out ? outboxValue : inboxValue2).intValue() < message.id ? z2 : z;
                                    MessageObject messageObject = r2;
                                    int a3 = a2;
                                    objects2 = objects;
                                    MessageObject messageObject2 = new MessageObject(MessagesController.this.currentAccount, message, (SparseArray) usersLocal, (SparseArray) chatsLocal, true);
                                    objects2.add(messageObject);
                                    a = a3 + 1;
                                    objects = objects2;
                                    z = false;
                                    z2 = true;
                                } else {
                                    objects2 = objects;
                                    ImageLoader.saveMessagesThumbs(messagesRes.messages);
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(messagesRes, j, -1, 0, false);
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            ArrayList<Integer> arrayList = (ArrayList) MessagesController.this.reloadingMessages.get(j);
                                            if (arrayList != null) {
                                                arrayList.removeAll(arrayList2);
                                                if (arrayList.isEmpty()) {
                                                    MessagesController.this.reloadingMessages.remove(j);
                                                }
                                            }
                                            MessageObject dialogObj = (MessageObject) MessagesController.this.dialogMessage.get(j);
                                            if (dialogObj != null) {
                                                int a = 0;
                                                while (a < objects2.size()) {
                                                    MessageObject obj = (MessageObject) objects2.get(a);
                                                    if (dialogObj == null || dialogObj.getId() != obj.getId()) {
                                                        a++;
                                                    } else {
                                                        MessagesController.this.dialogMessage.put(j, obj);
                                                        if (obj.messageOwner.to_id.channel_id == 0) {
                                                            MessageObject obj2 = (MessageObject) MessagesController.this.dialogMessagesByIds.get(obj.getId());
                                                            MessagesController.this.dialogMessagesByIds.remove(obj.getId());
                                                            if (obj2 != null) {
                                                                MessagesController.this.dialogMessagesByIds.put(obj2.getId(), obj2);
                                                            }
                                                        }
                                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                                    }
                                                }
                                            }
                                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(j), objects2);
                                        }
                                    });
                                    return;
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    public void hideReportSpam(long dialogId, User currentUser, Chat currentChat) {
        if (currentUser != null || currentChat != null) {
            Editor editor = this.notificationsPreferences.edit();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("spam3_");
            stringBuilder.append(dialogId);
            editor.putInt(stringBuilder.toString(), 1);
            editor.commit();
            if (((int) dialogId) != 0) {
                TL_messages_hideReportSpam req = new TL_messages_hideReportSpam();
                if (currentUser != null) {
                    req.peer = getInputPeer(currentUser.id);
                } else if (currentChat != null) {
                    req.peer = getInputPeer(-currentChat.id);
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                });
            }
        }
    }

    public void reportSpam(long dialogId, User currentUser, Chat currentChat, EncryptedChat currentEncryptedChat) {
        if (currentUser != null || currentChat != null || currentEncryptedChat != null) {
            Editor editor = this.notificationsPreferences.edit();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("spam3_");
            stringBuilder.append(dialogId);
            editor.putInt(stringBuilder.toString(), 1);
            editor.commit();
            if (((int) dialogId) == 0) {
                if (currentEncryptedChat != null) {
                    if (currentEncryptedChat.access_hash != 0) {
                        TL_messages_reportEncryptedSpam req = new TL_messages_reportEncryptedSpam();
                        req.peer = new TL_inputEncryptedChat();
                        req.peer.chat_id = currentEncryptedChat.id;
                        req.peer.access_hash = currentEncryptedChat.access_hash;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                            public void run(TLObject response, TL_error error) {
                            }
                        }, 2);
                    }
                }
                return;
            }
            TL_messages_reportSpam req2 = new TL_messages_reportSpam();
            if (currentChat != null) {
                req2.peer = getInputPeer(-currentChat.id);
            } else if (currentUser != null) {
                req2.peer = getInputPeer(currentUser.id);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            }, 2);
        }
    }

    public void loadPeerSettings(User currentUser, Chat currentChat) {
        if (currentUser != null || currentChat != null) {
            long dialogId;
            if (currentUser != null) {
                dialogId = (long) currentUser.id;
            } else {
                dialogId = (long) (-currentChat.id);
            }
            if (this.loadingPeerSettings.indexOfKey(dialogId) < 0) {
                StringBuilder stringBuilder;
                this.loadingPeerSettings.put(dialogId, Boolean.valueOf(true));
                if (BuildVars.LOGS_ENABLED) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("request spam button for ");
                    stringBuilder.append(dialogId);
                    FileLog.d(stringBuilder.toString());
                }
                SharedPreferences sharedPreferences = this.notificationsPreferences;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("spam3_");
                stringBuilder2.append(dialogId);
                if (sharedPreferences.getInt(stringBuilder2.toString(), 0) == 1) {
                    if (BuildVars.LOGS_ENABLED) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("spam button already hidden for ");
                        stringBuilder.append(dialogId);
                        FileLog.d(stringBuilder.toString());
                    }
                    return;
                }
                boolean hidden = this.notificationsPreferences;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("spam_");
                stringBuilder3.append(dialogId);
                if (hidden.getBoolean(stringBuilder3.toString(), false)) {
                    TL_messages_hideReportSpam req = new TL_messages_hideReportSpam();
                    if (currentUser != null) {
                        req.peer = getInputPeer(currentUser.id);
                    } else if (currentChat != null) {
                        req.peer = getInputPeer(-currentChat.id);
                    }
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.loadingPeerSettings.remove(dialogId);
                                    Editor editor = MessagesController.this.notificationsPreferences.edit();
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("spam_");
                                    stringBuilder.append(dialogId);
                                    editor.remove(stringBuilder.toString());
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("spam3_");
                                    stringBuilder.append(dialogId);
                                    editor.putInt(stringBuilder.toString(), 1);
                                    editor.commit();
                                }
                            });
                        }
                    });
                    return;
                }
                TL_messages_getPeerSettings req2 = new TL_messages_getPeerSettings();
                if (currentUser != null) {
                    req2.peer = getInputPeer(currentUser.id);
                } else if (currentChat != null) {
                    req2.peer = getInputPeer(-currentChat.id);
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                    public void run(final TLObject response, TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                MessagesController.this.loadingPeerSettings.remove(dialogId);
                                if (response != null) {
                                    TL_peerSettings res = response;
                                    Editor editor = MessagesController.this.notificationsPreferences.edit();
                                    StringBuilder stringBuilder;
                                    if (res.report_spam) {
                                        if (BuildVars.LOGS_ENABLED) {
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append("show spam button for ");
                                            stringBuilder.append(dialogId);
                                            FileLog.d(stringBuilder.toString());
                                        }
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("spam3_");
                                        stringBuilder.append(dialogId);
                                        editor.putInt(stringBuilder.toString(), 2);
                                        editor.commit();
                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.peerSettingsDidLoaded, Long.valueOf(dialogId));
                                        return;
                                    }
                                    if (BuildVars.LOGS_ENABLED) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("don't show spam button for ");
                                        stringBuilder.append(dialogId);
                                        FileLog.d(stringBuilder.toString());
                                    }
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("spam3_");
                                    stringBuilder.append(dialogId);
                                    editor.putInt(stringBuilder.toString(), 1);
                                    editor.commit();
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    protected void processNewChannelDifferenceParams(int pts, int pts_count, int channelId) {
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("processNewChannelDifferenceParams pts = ");
            stringBuilder.append(pts);
            stringBuilder.append(" pts_count = ");
            stringBuilder.append(pts_count);
            stringBuilder.append(" channeldId = ");
            stringBuilder.append(channelId);
            FileLog.d(stringBuilder.toString());
        }
        int channelPts = this.channelsPts.get(channelId);
        if (channelPts == 0) {
            channelPts = MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(channelId);
            if (channelPts == 0) {
                channelPts = 1;
            }
            this.channelsPts.put(channelId, channelPts);
        }
        if (channelPts + pts_count == pts) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("APPLY CHANNEL PTS");
            }
            this.channelsPts.put(channelId, pts);
            MessagesStorage.getInstance(this.currentAccount).saveChannelPts(channelId, pts);
        } else if (channelPts != pts) {
            long updatesStartWaitTime = this.updatesStartWaitTimeChannels.get(channelId);
            if (!(this.gettingDifferenceChannels.get(channelId) || updatesStartWaitTime == 0)) {
                if (Math.abs(System.currentTimeMillis() - updatesStartWaitTime) > 1500) {
                    getChannelDifference(channelId);
                    return;
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("ADD CHANNEL UPDATE TO QUEUE pts = ");
                stringBuilder2.append(pts);
                stringBuilder2.append(" pts_count = ");
                stringBuilder2.append(pts_count);
                FileLog.d(stringBuilder2.toString());
            }
            if (updatesStartWaitTime == 0) {
                this.updatesStartWaitTimeChannels.put(channelId, System.currentTimeMillis());
            }
            UserActionUpdatesPts updates = new UserActionUpdatesPts();
            updates.pts = pts;
            updates.pts_count = pts_count;
            updates.chat_id = channelId;
            ArrayList<Updates> arrayList = (ArrayList) this.updatesQueueChannels.get(channelId);
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.updatesQueueChannels.put(channelId, arrayList);
            }
            arrayList.add(updates);
        }
    }

    protected void processNewDifferenceParams(int seq, int pts, int date, int pts_count) {
        MessagesController messagesController = this;
        int i = seq;
        int i2 = pts;
        int i3 = date;
        int i4 = pts_count;
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("processNewDifferenceParams seq = ");
            stringBuilder.append(i);
            stringBuilder.append(" pts = ");
            stringBuilder.append(i2);
            stringBuilder.append(" date = ");
            stringBuilder.append(i3);
            stringBuilder.append(" pts_count = ");
            stringBuilder.append(i4);
            FileLog.d(stringBuilder.toString());
        }
        if (i2 != -1) {
            if (MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue() + i4 == i2) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("APPLY PTS");
                }
                MessagesStorage.getInstance(messagesController.currentAccount).setLastPtsValue(i2);
                MessagesStorage.getInstance(messagesController.currentAccount).saveDiffParams(MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastDateValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue());
            } else if (MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue() != i2) {
                if (!(messagesController.gettingDifference || messagesController.updatesStartWaitTimePts == 0)) {
                    if (Math.abs(System.currentTimeMillis() - messagesController.updatesStartWaitTimePts) > 1500) {
                        getDifference();
                    }
                }
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("ADD UPDATE TO QUEUE pts = ");
                    stringBuilder2.append(i2);
                    stringBuilder2.append(" pts_count = ");
                    stringBuilder2.append(i4);
                    FileLog.d(stringBuilder2.toString());
                }
                if (messagesController.updatesStartWaitTimePts == 0) {
                    messagesController.updatesStartWaitTimePts = System.currentTimeMillis();
                }
                UserActionUpdatesPts updates = new UserActionUpdatesPts();
                updates.pts = i2;
                updates.pts_count = i4;
                messagesController.updatesQueuePts.add(updates);
            }
        }
        if (i == -1) {
            return;
        }
        if (MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue() + 1 == i) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.d("APPLY SEQ");
            }
            MessagesStorage.getInstance(messagesController.currentAccount).setLastSeqValue(i);
            if (i3 != -1) {
                MessagesStorage.getInstance(messagesController.currentAccount).setLastDateValue(i3);
            }
            MessagesStorage.getInstance(messagesController.currentAccount).saveDiffParams(MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastDateValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue());
        } else if (MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue() != i) {
            if (!(messagesController.gettingDifference || messagesController.updatesStartWaitTimeSeq == 0)) {
                if (Math.abs(System.currentTimeMillis() - messagesController.updatesStartWaitTimeSeq) > 1500) {
                    getDifference();
                    return;
                }
            }
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("ADD UPDATE TO QUEUE seq = ");
                stringBuilder3.append(i);
                FileLog.d(stringBuilder3.toString());
            }
            if (messagesController.updatesStartWaitTimeSeq == 0) {
                messagesController.updatesStartWaitTimeSeq = System.currentTimeMillis();
            }
            UserActionUpdatesSeq updates2 = new UserActionUpdatesSeq();
            updates2.seq = i;
            messagesController.updatesQueueSeq.add(updates2);
        }
    }

    public void didAddedNewTask(final int minDate, final SparseArray<ArrayList<Long>> mids) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                if ((MessagesController.this.currentDeletingTaskMids == null && !MessagesController.this.gettingNewDeleteTask) || (MessagesController.this.currentDeletingTaskTime != 0 && minDate < MessagesController.this.currentDeletingTaskTime)) {
                    MessagesController.this.getNewDeleteTask(null, 0);
                }
            }
        });
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.didCreatedNewDeleteTask, mids);
            }
        });
    }

    public void getNewDeleteTask(final ArrayList<Integer> oldTask, final int channelId) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.this.gettingNewDeleteTask = true;
                MessagesStorage.getInstance(MessagesController.this.currentAccount).getNewTask(oldTask, channelId);
            }
        });
    }

    private boolean checkDeletingTask(boolean runnable) {
        int currentServerTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        if (this.currentDeletingTaskMids == null || (!runnable && (this.currentDeletingTaskTime == 0 || this.currentDeletingTaskTime > currentServerTime))) {
            return false;
        }
        this.currentDeletingTaskTime = 0;
        if (!(this.currentDeleteTaskRunnable == null || runnable)) {
            Utilities.stageQueue.cancelRunnable(this.currentDeleteTaskRunnable);
        }
        this.currentDeleteTaskRunnable = null;
        final ArrayList<Integer> mids = new ArrayList(this.currentDeletingTaskMids);
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                if (mids.isEmpty() || ((Integer) mids.get(0)).intValue() <= 0) {
                    MessagesController.this.deleteMessages(mids, null, null, 0, false);
                } else {
                    MessagesStorage.getInstance(MessagesController.this.currentAccount).emptyMessagesMedia(mids);
                }
                Utilities.stageQueue.postRunnable(new Runnable() {
                    public void run() {
                        MessagesController.this.getNewDeleteTask(mids, MessagesController.this.currentDeletingTaskChannelId);
                        MessagesController.this.currentDeletingTaskTime = 0;
                        MessagesController.this.currentDeletingTaskMids = null;
                    }
                });
            }
        });
        return true;
    }

    public void processLoadedDeleteTask(final int taskTime, final ArrayList<Integer> messages, int channelId) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.this.gettingNewDeleteTask = false;
                if (messages != null) {
                    MessagesController.this.currentDeletingTaskTime = taskTime;
                    MessagesController.this.currentDeletingTaskMids = messages;
                    if (MessagesController.this.currentDeleteTaskRunnable != null) {
                        Utilities.stageQueue.cancelRunnable(MessagesController.this.currentDeleteTaskRunnable);
                        MessagesController.this.currentDeleteTaskRunnable = null;
                    }
                    if (!MessagesController.this.checkDeletingTask(false)) {
                        MessagesController.this.currentDeleteTaskRunnable = new Runnable() {
                            public void run() {
                                MessagesController.this.checkDeletingTask(true);
                            }
                        };
                        Utilities.stageQueue.postRunnable(MessagesController.this.currentDeleteTaskRunnable, ((long) Math.abs(ConnectionsManager.getInstance(MessagesController.this.currentAccount).getCurrentTime() - MessagesController.this.currentDeletingTaskTime)) * 1000);
                        return;
                    }
                    return;
                }
                MessagesController.this.currentDeletingTaskTime = 0;
                MessagesController.this.currentDeletingTaskMids = null;
            }
        });
    }

    public void loadDialogPhotos(int did, int count, long max_id, boolean fromCache, int classGuid) {
        MessagesController messagesController = this;
        int i = count;
        long j = max_id;
        int i2 = classGuid;
        if (fromCache) {
            MessagesStorage.getInstance(messagesController.currentAccount).getDialogPhotos(did, i, j, i2);
        } else if (did > 0) {
            User user = getUser(Integer.valueOf(did));
            if (user != null) {
                TL_photos_getUserPhotos req = new TL_photos_getUserPhotos();
                req.limit = i;
                req.offset = 0;
                req.max_id = (long) ((int) j);
                req.user_id = getInputUser(user);
                r2 = did;
                r3 = i;
                AnonymousClass28 anonymousClass28 = r0;
                r4 = j;
                ConnectionsManager instance = ConnectionsManager.getInstance(messagesController.currentAccount);
                r6 = i2;
                AnonymousClass28 anonymousClass282 = new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            MessagesController.this.processLoadedUserPhotos((photos_Photos) response, r2, r3, r4, false, r6);
                        }
                    }
                };
                ConnectionsManager.getInstance(messagesController.currentAccount).bindRequestToGuid(instance.sendRequest(req, anonymousClass28), i2);
            }
        } else if (did < 0) {
            TL_messages_search req2 = new TL_messages_search();
            req2.filter = new TL_inputMessagesFilterChatPhotos();
            req2.limit = i;
            req2.offset_id = (int) j;
            req2.q = TtmlNode.ANONYMOUS_REGION_ID;
            req2.peer = getInputPeer(did);
            r2 = did;
            r3 = i;
            r4 = j;
            r6 = i2;
            ConnectionsManager.getInstance(messagesController.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req2, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        messages_Messages messages = (messages_Messages) response;
                        photos_Photos res = new TL_photos_photos();
                        res.count = messages.count;
                        res.users.addAll(messages.users);
                        for (int a = 0; a < messages.messages.size(); a++) {
                            Message message = (Message) messages.messages.get(a);
                            if (message.action != null) {
                                if (message.action.photo != null) {
                                    res.photos.add(message.action.photo);
                                }
                            }
                        }
                        MessagesController.this.processLoadedUserPhotos(res, r2, r3, r4, false, r6);
                    }
                }
            }), i2);
        }
    }

    public void blockUser(int user_id) {
        final User user = getUser(Integer.valueOf(user_id));
        if (user != null) {
            if (!this.blockedUsers.contains(Integer.valueOf(user_id))) {
                this.blockedUsers.add(Integer.valueOf(user_id));
                if (user.bot) {
                    DataQuery.getInstance(this.currentAccount).removeInline(user_id);
                } else {
                    DataQuery.getInstance(this.currentAccount).removePeer(user_id);
                }
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoaded, new Object[0]);
                TL_contacts_block req = new TL_contacts_block();
                req.id = getInputUser(user);
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            ArrayList<Integer> ids = new ArrayList();
                            ids.add(Integer.valueOf(user.id));
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).putBlockedUsers(ids, false);
                        }
                    }
                });
            }
        }
    }

    public void setUserBannedRole(int chatId, User user, TL_channelBannedRights rights, boolean isMegagroup, BaseFragment parentFragment) {
        if (user != null) {
            if (rights != null) {
                TL_channels_editBanned req = new TL_channels_editBanned();
                req.channel = getInputChannel(chatId);
                req.user_id = getInputUser(user);
                req.banned_rights = rights;
                final int i = chatId;
                final BaseFragment baseFragment = parentFragment;
                final TL_channels_editBanned tL_channels_editBanned = req;
                final boolean z = isMegagroup;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, final TL_error error) {
                        if (error == null) {
                            MessagesController.this.processUpdates((Updates) response, false);
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.loadFullChat(i, 0, true);
                                }
                            }, 1000);
                            return;
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                AlertsCreator.processError(MessagesController.this.currentAccount, error, baseFragment, tL_channels_editBanned, Boolean.valueOf(true ^ z));
                            }
                        });
                    }
                });
            }
        }
    }

    public void setUserAdminRole(int chatId, User user, TL_channelAdminRights rights, boolean isMegagroup, BaseFragment parentFragment) {
        if (user != null) {
            if (rights != null) {
                TL_channels_editAdmin req = new TL_channels_editAdmin();
                req.channel = getInputChannel(chatId);
                req.user_id = getInputUser(user);
                req.admin_rights = rights;
                final int i = chatId;
                final BaseFragment baseFragment = parentFragment;
                final TL_channels_editAdmin tL_channels_editAdmin = req;
                final boolean z = isMegagroup;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, final TL_error error) {
                        if (error == null) {
                            MessagesController.this.processUpdates((Updates) response, false);
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.loadFullChat(i, 0, true);
                                }
                            }, 1000);
                            return;
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                AlertsCreator.processError(MessagesController.this.currentAccount, error, baseFragment, tL_channels_editAdmin, Boolean.valueOf(true ^ z));
                            }
                        });
                    }
                });
            }
        }
    }

    public void unblockUser(int user_id) {
        TL_contacts_unblock req = new TL_contacts_unblock();
        final User user = getUser(Integer.valueOf(user_id));
        if (user != null) {
            this.blockedUsers.remove(Integer.valueOf(user.id));
            req.id = getInputUser(user);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoaded, new Object[0]);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    MessagesStorage.getInstance(MessagesController.this.currentAccount).deleteBlockedUser(user.id);
                }
            });
        }
    }

    public void getBlockedUsers(boolean cache) {
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            if (!this.loadingBlockedUsers) {
                this.loadingBlockedUsers = true;
                if (cache) {
                    MessagesStorage.getInstance(this.currentAccount).getBlockedUsers();
                } else {
                    TL_contacts_getBlocked req = new TL_contacts_getBlocked();
                    req.offset = 0;
                    req.limit = Callback.DEFAULT_DRAG_ANIMATION_DURATION;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            ArrayList<Integer> blocked = new ArrayList();
                            ArrayList<User> users = null;
                            if (error == null) {
                                contacts_Blocked res = (contacts_Blocked) response;
                                Iterator it = res.blocked.iterator();
                                while (it.hasNext()) {
                                    blocked.add(Integer.valueOf(((TL_contactBlocked) it.next()).user_id));
                                }
                                users = res.users;
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, null, true, true);
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).putBlockedUsers(blocked, true);
                            }
                            MessagesController.this.processLoadedBlockedUsers(blocked, users, false);
                        }
                    });
                }
            }
        }
    }

    public void processLoadedBlockedUsers(final ArrayList<Integer> ids, final ArrayList<User> users, final boolean cache) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                if (users != null) {
                    MessagesController.this.putUsers(users, cache);
                }
                MessagesController.this.loadingBlockedUsers = false;
                if (ids.isEmpty() && cache && !UserConfig.getInstance(MessagesController.this.currentAccount).blockedUsersLoaded) {
                    MessagesController.this.getBlockedUsers(false);
                    return;
                }
                if (!cache) {
                    UserConfig.getInstance(MessagesController.this.currentAccount).blockedUsersLoaded = true;
                    UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
                }
                MessagesController.this.blockedUsers = ids;
                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoaded, new Object[0]);
            }
        });
    }

    public void deleteUserPhoto(InputPhoto photo) {
        if (photo == null) {
            TL_photos_updateProfilePhoto req = new TL_photos_updateProfilePhoto();
            req.id = new TL_inputPhotoEmpty();
            UserConfig.getInstance(this.currentAccount).getCurrentUser().photo = new TL_userProfilePhotoEmpty();
            User user = getUser(Integer.valueOf(UserConfig.getInstance(this.currentAccount).getClientUserId()));
            if (user == null) {
                user = UserConfig.getInstance(this.currentAccount).getCurrentUser();
            }
            if (user != null) {
                user.photo = UserConfig.getInstance(this.currentAccount).getCurrentUser().photo;
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(UPDATE_MASK_ALL));
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            User user = MessagesController.this.getUser(Integer.valueOf(UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()));
                            if (user == null) {
                                user = UserConfig.getInstance(MessagesController.this.currentAccount).getCurrentUser();
                                MessagesController.this.putUser(user, false);
                            } else {
                                UserConfig.getInstance(MessagesController.this.currentAccount).setCurrentUser(user);
                            }
                            if (user != null) {
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).clearUserPhotos(user.id);
                                ArrayList<User> users = new ArrayList();
                                users.add(user);
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(users, null, false, true);
                                user.photo = (UserProfilePhoto) response;
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.mainUserInfoChanged, new Object[0]);
                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_ALL));
                                        UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(true);
                                    }
                                });
                            }
                        }
                    }
                });
            } else {
                return;
            }
        }
        TL_photos_deletePhotos req2 = new TL_photos_deletePhotos();
        req2.id.add(photo);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
            }
        });
    }

    public void processLoadedUserPhotos(photos_Photos res, int did, int count, long max_id, boolean fromCache, int classGuid) {
        if (fromCache) {
            if (res != null) {
                if (res.photos.isEmpty()) {
                }
            }
            loadDialogPhotos(did, count, max_id, false, classGuid);
            return;
        }
        MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(res.users, null, true, true);
        MessagesStorage.getInstance(this.currentAccount).putDialogPhotos(did, res);
        final photos_Photos org_telegram_tgnet_TLRPC_photos_Photos = res;
        final boolean z = fromCache;
        final int i = did;
        final int i2 = count;
        final int i3 = classGuid;
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                MessagesController.this.putUsers(org_telegram_tgnet_TLRPC_photos_Photos.users, z);
                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogPhotosLoaded, Integer.valueOf(i), Integer.valueOf(i2), Boolean.valueOf(z), Integer.valueOf(i3), org_telegram_tgnet_TLRPC_photos_Photos.photos);
            }
        });
    }

    public void uploadAndApplyUserAvatar(PhotoSize bigPhoto) {
        if (bigPhoto != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(FileLoader.getDirectory(4));
            stringBuilder.append("/");
            stringBuilder.append(bigPhoto.location.volume_id);
            stringBuilder.append("_");
            stringBuilder.append(bigPhoto.location.local_id);
            stringBuilder.append(".jpg");
            this.uploadingAvatar = stringBuilder.toString();
            FileLoader.getInstance(this.currentAccount).uploadFile(this.uploadingAvatar, false, true, 16777216);
        }
    }

    public void markChannelDialogMessageAsDeleted(ArrayList<Integer> messages, int channelId) {
        MessageObject obj = (MessageObject) this.dialogMessage.get((long) (-channelId));
        if (obj != null) {
            for (int a = 0; a < messages.size(); a++) {
                if (obj.getId() == ((Integer) messages.get(a)).intValue()) {
                    obj.deleted = true;
                    return;
                }
            }
        }
    }

    public void deleteMessages(ArrayList<Integer> messages, ArrayList<Long> randoms, EncryptedChat encryptedChat, int channelId, boolean forAll) {
        deleteMessages(messages, randoms, encryptedChat, channelId, forAll, 0, null);
    }

    public void deleteMessages(ArrayList<Integer> messages, ArrayList<Long> randoms, EncryptedChat encryptedChat, int channelId, boolean forAll, long taskId, TLObject taskRequest) {
        NativeByteBuffer data;
        MessagesController messagesController = this;
        ArrayList arrayList = messages;
        ArrayList<Long> arrayList2 = randoms;
        EncryptedChat encryptedChat2 = encryptedChat;
        final int i = channelId;
        if ((arrayList != null && !messages.isEmpty()) || taskRequest != null) {
            ArrayList<Integer> toSend = null;
            NativeByteBuffer data2 = null;
            if (taskId == 0) {
                if (i == 0) {
                    for (int a = 0; a < messages.size(); a++) {
                        MessageObject obj = (MessageObject) messagesController.dialogMessagesByIds.get(((Integer) arrayList.get(a)).intValue());
                        if (obj != null) {
                            obj.deleted = true;
                        }
                    }
                } else {
                    markChannelDialogMessageAsDeleted(arrayList, i);
                }
                toSend = new ArrayList();
                for (int a2 = 0; a2 < messages.size(); a2++) {
                    Integer mid = (Integer) arrayList.get(a2);
                    if (mid.intValue() > 0) {
                        toSend.add(mid);
                    }
                }
                MessagesStorage.getInstance(messagesController.currentAccount).markMessagesAsDeleted(arrayList, true, i);
                MessagesStorage.getInstance(messagesController.currentAccount).updateDialogsWithDeletedMessages(arrayList, null, true, i);
                NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, arrayList, Integer.valueOf(channelId));
            }
            long newTaskId;
            boolean z;
            if (i != 0) {
                TL_channels_deleteMessages req;
                if (taskRequest != null) {
                    req = (TL_channels_deleteMessages) taskRequest;
                    newTaskId = taskId;
                } else {
                    req = new TL_channels_deleteMessages();
                    req.id = toSend;
                    req.channel = getInputChannel(i);
                    try {
                        newTaskId = new NativeByteBuffer(8 + req.getObjectSize());
                        newTaskId.writeInt32(7);
                        newTaskId.writeInt32(i);
                        req.serializeToStream(newTaskId);
                    } catch (Throwable e) {
                        data = data2;
                        FileLog.e(e);
                        newTaskId = data;
                    }
                    newTaskId = MessagesStorage.getInstance(messagesController.currentAccount).createPendingTask(newTaskId);
                }
                ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            TL_messages_affectedMessages res = (TL_messages_affectedMessages) response;
                            MessagesController.this.processNewChannelDifferenceParams(res.pts, res.pts_count, i);
                        }
                        if (newTaskId != 0) {
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                        }
                    }
                });
                z = forAll;
            } else {
                TL_messages_deleteMessages req2;
                if (!(arrayList2 == null || encryptedChat2 == null || randoms.isEmpty())) {
                    SecretChatHelper.getInstance(messagesController.currentAccount).sendMessagesDeleteMessage(encryptedChat2, arrayList2, null);
                }
                if (taskRequest != null) {
                    z = forAll;
                    req2 = (TL_messages_deleteMessages) taskRequest;
                    newTaskId = taskId;
                } else {
                    req2 = new TL_messages_deleteMessages();
                    req2.id = toSend;
                    req2.revoke = forAll;
                    try {
                        newTaskId = new NativeByteBuffer(8 + req2.getObjectSize());
                        newTaskId.writeInt32(7);
                        newTaskId.writeInt32(i);
                        req2.serializeToStream(newTaskId);
                    } catch (Throwable e2) {
                        data = data2;
                        FileLog.e(e2);
                        newTaskId = data;
                    }
                    newTaskId = MessagesStorage.getInstance(messagesController.currentAccount).createPendingTask(newTaskId);
                }
                ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req2, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            TL_messages_affectedMessages res = (TL_messages_affectedMessages) response;
                            MessagesController.this.processNewDifferenceParams(-1, res.pts, -1, res.pts_count);
                        }
                        if (newTaskId != 0) {
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                        }
                    }
                });
            }
        }
    }

    public void pinChannelMessage(Chat chat, int id, boolean notify) {
        TL_channels_updatePinnedMessage req = new TL_channels_updatePinnedMessage();
        req.channel = getInputChannel(chat);
        req.id = id;
        req.silent = notify ^ 1;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    MessagesController.this.processUpdates((Updates) response, false);
                }
            }
        });
    }

    public void deleteUserChannelHistory(final Chat chat, final User user, int offset) {
        if (offset == 0) {
            MessagesStorage.getInstance(this.currentAccount).deleteUserChannelHistory(chat.id, user.id);
        }
        TL_channels_deleteUserHistory req = new TL_channels_deleteUserHistory();
        req.channel = getInputChannel(chat);
        req.user_id = getInputUser(user);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    TL_messages_affectedHistory res = (TL_messages_affectedHistory) response;
                    if (res.offset > 0) {
                        MessagesController.this.deleteUserChannelHistory(chat, user, res.offset);
                    }
                    MessagesController.this.processNewChannelDifferenceParams(res.pts, res.pts_count, chat.id);
                }
            }
        });
    }

    public void deleteDialog(long did, int onlyHistory) {
        deleteDialog(did, true, onlyHistory, 0);
    }

    private void deleteDialog(long did, boolean first, int onlyHistory, int max_id) {
        MessagesController messagesController = this;
        final long j = did;
        int i = onlyHistory;
        int lower_part = (int) j;
        int high_id = (int) (j >> 32);
        int max_id_delete = max_id;
        if (i == 2) {
            MessagesStorage.getInstance(messagesController.currentAccount).deleteDialog(j, i);
            return;
        }
        int lastMessageId;
        boolean z;
        if (i == 0 || i == 3) {
            DataQuery.getInstance(messagesController.currentAccount).uninstallShortcut(j);
        }
        if (first) {
            MessagesStorage.getInstance(messagesController.currentAccount).deleteDialog(j, i);
            TL_dialog dialog = (TL_dialog) messagesController.dialogs_dict.get(j);
            TL_dialog tL_dialog;
            if (dialog != null) {
                MessageObject object;
                if (max_id_delete == 0) {
                    max_id_delete = Math.max(0, dialog.top_message);
                }
                if (i != 0) {
                    if (i != 3) {
                        dialog.unread_count = 0;
                        object = (MessageObject) messagesController.dialogMessage.get(dialog.id);
                        messagesController.dialogMessage.remove(dialog.id);
                        if (object == null) {
                            lastMessageId = object.getId();
                            messagesController.dialogMessagesByIds.remove(object.getId());
                        } else {
                            lastMessageId = dialog.top_message;
                            object = (MessageObject) messagesController.dialogMessagesByIds.get(dialog.top_message);
                            messagesController.dialogMessagesByIds.remove(dialog.top_message);
                        }
                        if (!(object == null || object.messageOwner.random_id == 0)) {
                            messagesController.dialogMessagesByRandomIds.remove(object.messageOwner.random_id);
                        }
                        if (i == 1 || lower_part == 0 || lastMessageId <= 0) {
                            z = false;
                            dialog.top_message = 0;
                        } else {
                            TL_messageService message = new TL_messageService();
                            message.id = dialog.top_message;
                            message.out = ((long) UserConfig.getInstance(messagesController.currentAccount).getClientUserId()) == j;
                            message.from_id = UserConfig.getInstance(messagesController.currentAccount).getClientUserId();
                            message.flags |= 256;
                            message.action = new TL_messageActionHistoryClear();
                            message.date = dialog.last_message_date;
                            if (lower_part > 0) {
                                message.to_id = new TL_peerUser();
                                message.to_id.user_id = lower_part;
                            } else if (ChatObject.isChannel(getChat(Integer.valueOf(-lower_part)))) {
                                message.to_id = new TL_peerChannel();
                                message.to_id.channel_id = -lower_part;
                            } else {
                                message.to_id = new TL_peerChat();
                                message.to_id.chat_id = -lower_part;
                            }
                            TL_dialog dialog2 = dialog;
                            MessageObject obj = new MessageObject(messagesController.currentAccount, message, messagesController.createdDialogIds.contains(Long.valueOf(message.dialog_id)));
                            ArrayList<MessageObject> objArr = new ArrayList();
                            objArr.add(obj);
                            dialog = new ArrayList();
                            dialog.add(message);
                            updateInterfaceWithMessages(j, objArr);
                            MessagesStorage.getInstance(messagesController.currentAccount).putMessages((ArrayList) dialog, false, true, false, 0);
                            tL_dialog = dialog2;
                            z = false;
                        }
                    }
                }
                messagesController.dialogs.remove(dialog);
                if (messagesController.dialogsServerOnly.remove(dialog) && DialogObject.isChannel(dialog)) {
                    Utilities.stageQueue.postRunnable(new Runnable() {
                        public void run() {
                            MessagesController.this.channelsPts.delete(-((int) j));
                            MessagesController.this.shortPollChannels.delete(-((int) j));
                            MessagesController.this.needShortPollChannels.delete(-((int) j));
                        }
                    });
                }
                messagesController.dialogsGroupsOnly.remove(dialog);
                messagesController.dialogs_dict.remove(j);
                messagesController.dialogs_read_inbox_max.remove(Long.valueOf(did));
                messagesController.dialogs_read_outbox_max.remove(Long.valueOf(did));
                messagesController.nextDialogsCacheOffset--;
                object = (MessageObject) messagesController.dialogMessage.get(dialog.id);
                messagesController.dialogMessage.remove(dialog.id);
                if (object == null) {
                    lastMessageId = dialog.top_message;
                    object = (MessageObject) messagesController.dialogMessagesByIds.get(dialog.top_message);
                    messagesController.dialogMessagesByIds.remove(dialog.top_message);
                } else {
                    lastMessageId = object.getId();
                    messagesController.dialogMessagesByIds.remove(object.getId());
                }
                messagesController.dialogMessagesByRandomIds.remove(object.messageOwner.random_id);
                if (i == 1) {
                }
                z = false;
                dialog.top_message = 0;
            } else {
                z = false;
                tL_dialog = dialog;
            }
            NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[z]);
            NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.removeAllMessagesFromDialog, Long.valueOf(did), Boolean.valueOf(z));
            MessagesStorage.getInstance(messagesController.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                public void run() {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.getInstance(MessagesController.this.currentAccount).removeNotificationsForDialog(j);
                        }
                    });
                }
            });
        } else {
            z = false;
        }
        int max_id_delete2 = max_id_delete;
        if (high_id == 1) {
        } else if (i == 3) {
            r24 = lower_part;
        } else {
            if (lower_part != 0) {
                InputPeer peer = getInputPeer(lower_part);
                if (peer != null) {
                    boolean z2 = peer instanceof TL_inputPeerChannel;
                    lastMessageId = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    if (!z2) {
                        TL_messages_deleteHistory req = new TL_messages_deleteHistory();
                        req.peer = peer;
                        if (i != 0) {
                            lastMessageId = max_id_delete2;
                        }
                        req.max_id = lastMessageId;
                        if (i != 0) {
                            z = true;
                        }
                        req.just_clear = z;
                        final int max_id_delete_final = max_id_delete2;
                        ConnectionsManager instance = ConnectionsManager.getInstance(messagesController.currentAccount);
                        AnonymousClass46 anonymousClass46 = r0;
                        final long j2 = j;
                        TL_messages_deleteHistory req2 = req;
                        req = i;
                        AnonymousClass46 anonymousClass462 = new RequestDelegate() {
                            public void run(TLObject response, TL_error error) {
                                if (error == null) {
                                    TL_messages_affectedHistory res = (TL_messages_affectedHistory) response;
                                    if (res.offset > 0) {
                                        MessagesController.this.deleteDialog(j2, false, req, max_id_delete_final);
                                    }
                                    MessagesController.this.processNewDifferenceParams(-1, res.pts, -1, res.pts_count);
                                }
                            }
                        };
                        instance.sendRequest(req2, anonymousClass46, 64);
                    } else if (i != 0) {
                        TL_channels_deleteHistory req3 = new TL_channels_deleteHistory();
                        req3.channel = new TL_inputChannel();
                        req3.channel.channel_id = peer.channel_id;
                        req3.channel.access_hash = peer.access_hash;
                        if (max_id_delete2 > 0) {
                            lastMessageId = max_id_delete2;
                        }
                        req3.max_id = lastMessageId;
                        ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req3, new RequestDelegate() {
                            public void run(TLObject response, TL_error error) {
                            }
                        }, 64);
                        r24 = lower_part;
                    } else {
                        return;
                    }
                }
                return;
            }
            if (i == 1) {
                SecretChatHelper.getInstance(messagesController.currentAccount).sendClearHistoryMessage(getEncryptedChat(Integer.valueOf(high_id)), null);
            } else {
                SecretChatHelper.getInstance(messagesController.currentAccount).declineSecretChat(high_id);
            }
        }
    }

    public void saveGif(Document document) {
        TL_messages_saveGif req = new TL_messages_saveGif();
        req.id = new TL_inputDocument();
        req.id.id = document.id;
        req.id.access_hash = document.access_hash;
        req.unsave = false;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
            }
        });
    }

    public void saveRecentSticker(Document document, boolean asMask) {
        TL_messages_saveRecentSticker req = new TL_messages_saveRecentSticker();
        req.id = new TL_inputDocument();
        req.id.id = document.id;
        req.id.access_hash = document.access_hash;
        req.unsave = false;
        req.attached = asMask;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
            }
        });
    }

    public void loadChannelParticipants(final Integer chat_id) {
        if (!this.loadingFullParticipants.contains(chat_id)) {
            if (!this.loadedFullParticipants.contains(chat_id)) {
                this.loadingFullParticipants.add(chat_id);
                TL_channels_getParticipants req = new TL_channels_getParticipants();
                req.channel = getInputChannel(chat_id.intValue());
                req.filter = new TL_channelParticipantsRecent();
                req.offset = 0;
                req.limit = 32;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, final TL_error error) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (error == null) {
                                    TL_channels_channelParticipants res = response;
                                    MessagesController.this.putUsers(res.users, false);
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, null, true, true);
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).updateChannelUsers(chat_id.intValue(), res.participants);
                                    MessagesController.this.loadedFullParticipants.add(chat_id);
                                }
                                MessagesController.this.loadingFullParticipants.remove(chat_id);
                            }
                        });
                    }
                });
            }
        }
    }

    public void loadChatInfo(int chat_id, CountDownLatch countDownLatch, boolean force) {
        MessagesStorage.getInstance(this.currentAccount).loadChatInfo(chat_id, countDownLatch, force, false);
    }

    public void processChatInfo(int chat_id, ChatFull info, ArrayList<User> usersArr, boolean fromCache, boolean force, boolean byChannelUsers, MessageObject pinnedMessageObject) {
        if (fromCache && chat_id > 0 && !byChannelUsers) {
            loadFullChat(chat_id, 0, force);
        }
        if (info != null) {
            final ArrayList<User> arrayList = usersArr;
            final boolean z = fromCache;
            final ChatFull chatFull = info;
            final boolean z2 = byChannelUsers;
            final MessageObject messageObject = pinnedMessageObject;
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    MessagesController.this.putUsers(arrayList, z);
                    if (chatFull.stickerset != null) {
                        DataQuery.getInstance(MessagesController.this.currentAccount).getGroupStickerSetById(chatFull.stickerset);
                    }
                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, chatFull, Integer.valueOf(0), Boolean.valueOf(z2), messageObject);
                }
            });
        }
    }

    public void updateTimerProc() {
        int a;
        long currentTime = System.currentTimeMillis();
        boolean z = false;
        checkDeletingTask(false);
        checkReadTasks();
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            TL_account_updateStatus req;
            if (ConnectionsManager.getInstance(r0.currentAccount).getPauseTime() == 0 && ApplicationLoader.isScreenOn && !ApplicationLoader.mainInterfacePausedStageQueue) {
                if (ApplicationLoader.mainInterfacePausedStageQueueTime != 0 && Math.abs(ApplicationLoader.mainInterfacePausedStageQueueTime - System.currentTimeMillis()) > 1000 && r0.statusSettingState != 1 && (r0.lastStatusUpdateTime == 0 || Math.abs(System.currentTimeMillis() - r0.lastStatusUpdateTime) >= 55000 || r0.offlineSent)) {
                    r0.statusSettingState = 1;
                    if (r0.statusRequest != 0) {
                        ConnectionsManager.getInstance(r0.currentAccount).cancelRequest(r0.statusRequest, true);
                    }
                    req = new TL_account_updateStatus();
                    req.offline = false;
                    r0.statusRequest = ConnectionsManager.getInstance(r0.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            if (error == null) {
                                MessagesController.this.lastStatusUpdateTime = System.currentTimeMillis();
                                MessagesController.this.offlineSent = false;
                                MessagesController.this.statusSettingState = 0;
                            } else if (MessagesController.this.lastStatusUpdateTime != 0) {
                                MessagesController.this.lastStatusUpdateTime = MessagesController.this.lastStatusUpdateTime + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
                            }
                            MessagesController.this.statusRequest = 0;
                        }
                    });
                }
            } else if (!(r0.statusSettingState == 2 || r0.offlineSent || Math.abs(System.currentTimeMillis() - ConnectionsManager.getInstance(r0.currentAccount).getPauseTime()) < AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS)) {
                r0.statusSettingState = 2;
                if (r0.statusRequest != 0) {
                    ConnectionsManager.getInstance(r0.currentAccount).cancelRequest(r0.statusRequest, true);
                }
                req = new TL_account_updateStatus();
                req.offline = true;
                r0.statusRequest = ConnectionsManager.getInstance(r0.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            MessagesController.this.offlineSent = true;
                        } else if (MessagesController.this.lastStatusUpdateTime != 0) {
                            MessagesController.this.lastStatusUpdateTime = MessagesController.this.lastStatusUpdateTime + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
                        }
                        MessagesController.this.statusRequest = 0;
                    }
                });
            }
            if (r0.updatesQueueChannels.size() != 0) {
                for (a = 0; a < r0.updatesQueueChannels.size(); a++) {
                    int key = r0.updatesQueueChannels.keyAt(a);
                    if (r0.updatesStartWaitTimeChannels.valueAt(a) + 1500 < currentTime) {
                        if (BuildVars.LOGS_ENABLED) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("QUEUE CHANNEL ");
                            stringBuilder.append(key);
                            stringBuilder.append(" UPDATES WAIT TIMEOUT - CHECK QUEUE");
                            FileLog.d(stringBuilder.toString());
                        }
                        processChannelsUpdatesQueue(key, 0);
                    }
                }
            }
            a = 0;
            while (a < 3) {
                if (getUpdatesStartTime(a) != 0 && getUpdatesStartTime(a) + 1500 < currentTime) {
                    if (BuildVars.LOGS_ENABLED) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(a);
                        stringBuilder2.append(" QUEUE UPDATES WAIT TIMEOUT - CHECK QUEUE");
                        FileLog.d(stringBuilder2.toString());
                    }
                    processUpdatesQueue(a, 0);
                }
                a++;
            }
        }
        if (r0.channelViewsToSend.size() != 0 && Math.abs(System.currentTimeMillis() - r0.lastViewsCheckTime) >= DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) {
            r0.lastViewsCheckTime = System.currentTimeMillis();
            a = 0;
            while (a < r0.channelViewsToSend.size()) {
                final int key2 = r0.channelViewsToSend.keyAt(a);
                final TL_messages_getMessagesViews req2 = new TL_messages_getMessagesViews();
                req2.peer = getInputPeer(key2);
                req2.id = (ArrayList) r0.channelViewsToSend.valueAt(a);
                req2.increment = a == 0;
                ConnectionsManager.getInstance(r0.currentAccount).sendRequest(req2, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            Vector vector = (Vector) response;
                            final SparseArray<SparseIntArray> channelViews = new SparseArray();
                            SparseIntArray array = (SparseIntArray) channelViews.get(key2);
                            if (array == null) {
                                array = new SparseIntArray();
                                channelViews.put(key2, array);
                            }
                            for (int a = 0; a < req2.id.size(); a++) {
                                if (a >= vector.objects.size()) {
                                    break;
                                }
                                array.put(((Integer) req2.id.get(a)).intValue(), ((Integer) vector.objects.get(a)).intValue());
                            }
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).putChannelViews(channelViews, req2.peer instanceof TL_inputPeerChannel);
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.didUpdatedMessagesViews, channelViews);
                                }
                            });
                        }
                    }
                });
                a++;
            }
            r0.channelViewsToSend.clear();
        }
        if (!r0.onlinePrivacy.isEmpty()) {
            Iterator it;
            ArrayList<Integer> toRemove = null;
            key2 = ConnectionsManager.getInstance(r0.currentAccount).getCurrentTime();
            for (Entry<Integer, Integer> entry : r0.onlinePrivacy.entrySet()) {
                if (((Integer) entry.getValue()).intValue() < key2 - 30) {
                    if (toRemove == null) {
                        toRemove = new ArrayList();
                    }
                    toRemove.add(entry.getKey());
                }
            }
            if (toRemove != null) {
                it = toRemove.iterator();
                while (it.hasNext()) {
                    r0.onlinePrivacy.remove((Integer) it.next());
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(4));
                    }
                });
            }
        }
        if (r0.shortPollChannels.size() != 0) {
            for (a = 0; a < r0.shortPollChannels.size(); a++) {
                key2 = r0.shortPollChannels.keyAt(a);
                if (((long) r0.shortPollChannels.valueAt(a)) < System.currentTimeMillis() / 1000) {
                    r0.shortPollChannels.delete(key2);
                    if (r0.needShortPollChannels.indexOfKey(key2) >= 0) {
                        getChannelDifference(key2);
                    }
                }
            }
        }
        if (!(r0.printingUsers.isEmpty() && r0.lastPrintingStringCount == r0.printingUsers.size())) {
            ArrayList<Long> keys = new ArrayList(r0.printingUsers.keySet());
            boolean updated = false;
            a = 0;
            while (a < keys.size()) {
                int b;
                long key3 = ((Long) keys.get(a)).longValue();
                ArrayList<PrintingUser> arr = (ArrayList) r0.printingUsers.get(Long.valueOf(key3));
                if (arr != null) {
                    boolean updated2 = updated;
                    int a2 = z;
                    while (a2 < arr.size()) {
                        int timeToRemove;
                        PrintingUser user = (PrintingUser) arr.get(a2);
                        if (user.action instanceof TL_sendMessageGamePlayAction) {
                            timeToRemove = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS;
                        } else {
                            timeToRemove = 5900;
                        }
                        b = a;
                        if (user.lastTime + ((long) timeToRemove) < currentTime) {
                            arr.remove(user);
                            a2--;
                            updated2 = true;
                        }
                        a2++;
                        a = b;
                    }
                    b = a;
                    updated = updated2;
                } else {
                    b = a;
                }
                if (arr != null) {
                    if (!arr.isEmpty()) {
                        a = b;
                        a++;
                        z = false;
                    }
                }
                r0.printingUsers.remove(Long.valueOf(key3));
                int b2 = b;
                keys.remove(b2);
                a = b2 - 1;
                a++;
                z = false;
            }
            updatePrintingStrings();
            if (updated) {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(64));
                    }
                });
            }
        }
        if (Theme.selectedAutoNightType == 1 && Math.abs(currentTime - lastThemeCheckTime) >= 60) {
            AndroidUtilities.runOnUIThread(r0.themeCheckRunnable);
            lastThemeCheckTime = currentTime;
        }
        if (r0.lastPushRegisterSendTime != 0 && Math.abs(SystemClock.uptimeMillis() - r0.lastPushRegisterSendTime) >= 10800000) {
            GcmInstanceIDListenerService.sendRegistrationToServer(SharedConfig.pushString);
        }
        LocationController.getInstance(r0.currentAccount).update();
    }

    private String getUserNameForTyping(User user) {
        if (user == null) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        if (user.first_name != null && user.first_name.length() > 0) {
            return user.first_name;
        }
        if (user.last_name == null || user.last_name.length() <= 0) {
            return TtmlNode.ANONYMOUS_REGION_ID;
        }
        return user.last_name;
    }

    public void cancelTyping(int action, long dialog_id) {
        LongSparseArray<Boolean> typings = (LongSparseArray) this.sendingTypings.get(action);
        if (typings != null) {
            typings.remove(dialog_id);
        }
    }

    public void sendTyping(final long dialog_id, final int action, int classGuid) {
        if (dialog_id != 0) {
            LongSparseArray<Boolean> typings = (LongSparseArray) this.sendingTypings.get(action);
            if (typings == null || typings.get(dialog_id) == null) {
                if (typings == null) {
                    typings = new LongSparseArray();
                    this.sendingTypings.put(action, typings);
                }
                int lower_part = (int) dialog_id;
                int high_id = (int) (dialog_id >> 32);
                int reqId;
                if (lower_part != 0) {
                    if (high_id != 1) {
                        TL_messages_setTyping req = new TL_messages_setTyping();
                        req.peer = getInputPeer(lower_part);
                        if (req.peer instanceof TL_inputPeerChannel) {
                            Chat chat = getChat(Integer.valueOf(req.peer.channel_id));
                            if (chat == null || !chat.megagroup) {
                                return;
                            }
                        }
                        if (req.peer != null) {
                            if (action == 0) {
                                req.action = new TL_sendMessageTypingAction();
                            } else if (action == 1) {
                                req.action = new TL_sendMessageRecordAudioAction();
                            } else if (action == 2) {
                                req.action = new TL_sendMessageCancelAction();
                            } else if (action == 3) {
                                req.action = new TL_sendMessageUploadDocumentAction();
                            } else if (action == 4) {
                                req.action = new TL_sendMessageUploadPhotoAction();
                            } else if (action == 5) {
                                req.action = new TL_sendMessageUploadVideoAction();
                            } else if (action == 6) {
                                req.action = new TL_sendMessageGamePlayAction();
                            } else if (action == 7) {
                                req.action = new TL_sendMessageRecordRoundAction();
                            } else if (action == 8) {
                                req.action = new TL_sendMessageUploadRoundAction();
                            } else if (action == 9) {
                                req.action = new TL_sendMessageUploadAudioAction();
                            }
                            typings.put(dialog_id, Boolean.valueOf(true));
                            reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                                public void run(TLObject response, TL_error error) {
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            LongSparseArray<Boolean> typings = (LongSparseArray) MessagesController.this.sendingTypings.get(action);
                                            if (typings != null) {
                                                typings.remove(dialog_id);
                                            }
                                        }
                                    });
                                }
                            }, 2);
                            if (classGuid != 0) {
                                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(reqId, classGuid);
                            }
                        }
                    }
                } else if (action == 0) {
                    EncryptedChat chat2 = getEncryptedChat(Integer.valueOf(high_id));
                    if (chat2.auth_key != null && chat2.auth_key.length > 1 && (chat2 instanceof TL_encryptedChat)) {
                        TL_messages_setEncryptedTyping req2 = new TL_messages_setEncryptedTyping();
                        req2.peer = new TL_inputEncryptedChat();
                        req2.peer.chat_id = chat2.id;
                        req2.peer.access_hash = chat2.access_hash;
                        req2.typing = true;
                        typings.put(dialog_id, Boolean.valueOf(true));
                        reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                            public void run(TLObject response, TL_error error) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        LongSparseArray<Boolean> typings = (LongSparseArray) MessagesController.this.sendingTypings.get(action);
                                        if (typings != null) {
                                            typings.remove(dialog_id);
                                        }
                                    }
                                });
                            }
                        }, 2);
                        if (classGuid != 0) {
                            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(reqId, classGuid);
                        }
                    }
                }
            }
        }
    }

    public void loadMessages(long dialog_id, int count, int max_id, int offset_date, boolean fromCache, int midDate, int classGuid, int load_type, int last_message_id, boolean isChannel, int loadIndex) {
        loadMessages(dialog_id, count, max_id, offset_date, fromCache, midDate, classGuid, load_type, last_message_id, isChannel, loadIndex, 0, 0, 0, false, 0);
    }

    public void loadMessages(long dialog_id, int count, int max_id, int offset_date, boolean fromCache, int midDate, int classGuid, int load_type, int last_message_id, boolean isChannel, int loadIndex, int first_unread, int unread_count, int last_date, boolean queryFromServer, int mentionsCount) {
        loadMessagesInternal(dialog_id, count, max_id, offset_date, fromCache, midDate, classGuid, load_type, last_message_id, isChannel, loadIndex, first_unread, unread_count, last_date, queryFromServer, mentionsCount, true);
    }

    private void loadMessagesInternal(long dialog_id, int count, int max_id, int offset_date, boolean fromCache, int midDate, int classGuid, int load_type, int last_message_id, boolean isChannel, int loadIndex, int first_unread, int unread_count, int last_date, boolean queryFromServer, int mentionsCount, boolean loadDialog) {
        MessagesController messagesController;
        MessagesController messagesController2 = this;
        long j = dialog_id;
        int i = count;
        int i2 = max_id;
        boolean z = fromCache;
        int i3 = classGuid;
        int i4 = load_type;
        int i5 = last_message_id;
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("load messages in chat ");
            stringBuilder.append(j);
            stringBuilder.append(" count ");
            stringBuilder.append(i);
            stringBuilder.append(" max_id ");
            stringBuilder.append(i2);
            stringBuilder.append(" cache ");
            stringBuilder.append(z);
            stringBuilder.append(" mindate = ");
            stringBuilder.append(midDate);
            stringBuilder.append(" guid ");
            stringBuilder.append(i3);
            stringBuilder.append(" load_type ");
            stringBuilder.append(i4);
            stringBuilder.append(" last_message_id ");
            stringBuilder.append(i5);
            stringBuilder.append(" index ");
            stringBuilder.append(loadIndex);
            stringBuilder.append(" firstUnread ");
            stringBuilder.append(first_unread);
            stringBuilder.append(" unread_count ");
            stringBuilder.append(unread_count);
            stringBuilder.append(" last_date ");
            stringBuilder.append(last_date);
            stringBuilder.append(" queryFromServer ");
            stringBuilder.append(queryFromServer);
            FileLog.d(stringBuilder.toString());
        } else {
            int i6 = midDate;
            int i7 = loadIndex;
            int i8 = first_unread;
            int i9 = unread_count;
            int i10 = last_date;
            boolean z2 = queryFromServer;
        }
        int lower_part = (int) j;
        if (z) {
            i = i3;
            messagesController = messagesController2;
        } else if (lower_part == 0) {
            r17 = lower_part;
            i = i3;
            messagesController = messagesController2;
        } else {
            int lower_part2;
            TL_messages_getHistory req;
            int lower_part3;
            int i11;
            final long j2;
            AnonymousClass60 anonymousClass60;
            ConnectionsManager instance;
            final boolean z3;
            final int i12;
            TL_messages_getHistory req2;
            final boolean z4;
            final int i13;
            r17 = lower_part;
            if (loadDialog) {
                if (i4 != 3) {
                    if (i4 != 2) {
                        lower_part2 = r17;
                        req = new TL_messages_getHistory();
                        lower_part3 = lower_part2;
                        req.peer = getInputPeer(lower_part3);
                        i = load_type;
                        if (i != 4) {
                            i2 = count;
                            req.add_offset = (-i2) + 5;
                        } else {
                            i2 = count;
                            if (i == 3) {
                                req.add_offset = (-i2) / 2;
                            } else if (i != 1) {
                                req.add_offset = (-i2) - 1;
                            } else {
                                if (i != 2) {
                                    i11 = max_id;
                                    if (i11 != 0) {
                                        req.add_offset = (-i2) + 6;
                                        req.limit = i2;
                                        req.offset_id = i11;
                                        i3 = offset_date;
                                        req.offset_date = i3;
                                        i9 = i2;
                                        i10 = i11;
                                        i8 = i3;
                                        j2 = dialog_id;
                                        anonymousClass60 = lower_part;
                                        i5 = classGuid;
                                        instance = ConnectionsManager.getInstance(r14.currentAccount);
                                        i4 = first_unread;
                                        i3 = last_message_id;
                                        i11 = unread_count;
                                        i2 = last_date;
                                        i = load_type;
                                        r17 = lower_part3;
                                        z3 = isChannel;
                                        i12 = loadIndex;
                                        req2 = req;
                                        z4 = queryFromServer;
                                        i13 = mentionsCount;
                                        lower_part = new RequestDelegate() {
                                            public void run(TLObject response, TL_error error) {
                                                AnonymousClass60 anonymousClass60 = this;
                                                if (response != null) {
                                                    messages_Messages res = (messages_Messages) response;
                                                    if (res.messages.size() > i9) {
                                                        res.messages.remove(0);
                                                    }
                                                    int mid = i10;
                                                    if (i8 != 0 && !res.messages.isEmpty()) {
                                                        mid = ((Message) res.messages.get(res.messages.size() - 1)).id;
                                                        for (int a = res.messages.size() - 1; a >= 0; a--) {
                                                            Message message = (Message) res.messages.get(a);
                                                            if (message.date > i8) {
                                                                mid = message.id;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                    int mid2 = mid;
                                                    MessagesController messagesController = MessagesController.this;
                                                    long j = j2;
                                                    int i = i9;
                                                    int i2 = i8;
                                                    int i3 = i5;
                                                    int i4 = i4;
                                                    int i5 = i3;
                                                    int i6 = i11;
                                                    int i7 = i2;
                                                    int i8 = i;
                                                    boolean z = z3;
                                                    int i9 = i12;
                                                    boolean z2 = z;
                                                    int i10 = i8;
                                                    int i11 = i9;
                                                    messagesController.processLoadedMessages(res, j, i, mid2, i2, false, i3, i4, i5, i6, i7, i10, z2, false, i11, z4, i13);
                                                }
                                            }
                                        };
                                        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(instance.sendRequest(req2, anonymousClass60), classGuid);
                                    }
                                }
                                i11 = max_id;
                                if (lower_part3 < 0 && r10 != 0 && ChatObject.isChannel(getChat(Integer.valueOf(-lower_part3)))) {
                                    req.add_offset = -1;
                                    req.limit++;
                                }
                                req.limit = i2;
                                req.offset_id = i11;
                                i3 = offset_date;
                                req.offset_date = i3;
                                i9 = i2;
                                i10 = i11;
                                i8 = i3;
                                j2 = dialog_id;
                                anonymousClass60 = lower_part;
                                i5 = classGuid;
                                instance = ConnectionsManager.getInstance(r14.currentAccount);
                                i4 = first_unread;
                                i3 = last_message_id;
                                i11 = unread_count;
                                i2 = last_date;
                                i = load_type;
                                r17 = lower_part3;
                                z3 = isChannel;
                                i12 = loadIndex;
                                req2 = req;
                                z4 = queryFromServer;
                                i13 = mentionsCount;
                                lower_part = /* anonymous class already generated */;
                                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(instance.sendRequest(req2, anonymousClass60), classGuid);
                            }
                        }
                        i11 = max_id;
                        req.limit = i2;
                        req.offset_id = i11;
                        i3 = offset_date;
                        req.offset_date = i3;
                        i9 = i2;
                        i10 = i11;
                        i8 = i3;
                        j2 = dialog_id;
                        anonymousClass60 = lower_part;
                        i5 = classGuid;
                        instance = ConnectionsManager.getInstance(r14.currentAccount);
                        i4 = first_unread;
                        i3 = last_message_id;
                        i11 = unread_count;
                        i2 = last_date;
                        i = load_type;
                        r17 = lower_part3;
                        z3 = isChannel;
                        i12 = loadIndex;
                        req2 = req;
                        z4 = queryFromServer;
                        i13 = mentionsCount;
                        lower_part = /* anonymous class already generated */;
                        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(instance.sendRequest(req2, anonymousClass60), classGuid);
                    }
                }
                if (i5 == 0) {
                    lower_part = new TL_messages_getPeerDialogs();
                    InputPeer inputPeer = getInputPeer((int) j);
                    TL_inputDialogPeer inputDialogPeer = new TL_inputDialogPeer();
                    inputDialogPeer.peer = inputPeer;
                    InputPeer inputPeer2 = inputPeer;
                    lower_part.peers.add(inputDialogPeer);
                    TL_messages_getPeerDialogs req3 = lower_part;
                    ConnectionsManager instance2 = ConnectionsManager.getInstance(messagesController2.currentAccount);
                    r17 = inputDialogPeer;
                    final long j3 = j;
                    i8 = i;
                    i7 = i2;
                    i6 = offset_date;
                    i5 = midDate;
                    i4 = i3;
                    i3 = load_type;
                    AnonymousClass59 anonymousClass59 = lower_part;
                    z = isChannel;
                    i2 = loadIndex;
                    i = first_unread;
                    lower_part3 = last_date;
                    final boolean z5 = queryFromServer;
                    lower_part = new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            AnonymousClass59 anonymousClass59 = this;
                            if (response != null) {
                                TL_messages_peerDialogs res = (TL_messages_peerDialogs) response;
                                if (!res.dialogs.isEmpty()) {
                                    TL_dialog dialog = (TL_dialog) res.dialogs.get(0);
                                    if (dialog.top_message != 0) {
                                        TL_messages_dialogs dialogs = new TL_messages_dialogs();
                                        dialogs.chats = res.chats;
                                        dialogs.users = res.users;
                                        dialogs.dialogs = res.dialogs;
                                        dialogs.messages = res.messages;
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putDialogs(dialogs, false);
                                    }
                                    MessagesController messagesController = MessagesController.this;
                                    long j = j3;
                                    int i = i8;
                                    int i2 = i7;
                                    int i3 = i6;
                                    int i4 = i5;
                                    int i5 = i4;
                                    int i6 = i3;
                                    int i7 = dialog.top_message;
                                    boolean z = z;
                                    int i8 = i2;
                                    int i9 = i;
                                    int i10 = dialog.unread_count;
                                    int i11 = lower_part3;
                                    int i12 = i6;
                                    int i13 = i7;
                                    boolean z2 = z;
                                    messagesController.loadMessagesInternal(j, i, i2, i3, false, i4, i5, i12, i13, z2, i8, i9, i10, i11, z5, dialog.unread_mentions_count, false);
                                }
                            }
                        }
                    };
                    instance2.sendRequest(req3, anonymousClass59);
                    return;
                }
            }
            lower_part2 = r17;
            req = new TL_messages_getHistory();
            lower_part3 = lower_part2;
            req.peer = getInputPeer(lower_part3);
            i = load_type;
            if (i != 4) {
                i2 = count;
                if (i == 3) {
                    req.add_offset = (-i2) / 2;
                } else if (i != 1) {
                    if (i != 2) {
                        i11 = max_id;
                    } else {
                        i11 = max_id;
                        if (i11 != 0) {
                            req.add_offset = (-i2) + 6;
                            req.limit = i2;
                            req.offset_id = i11;
                            i3 = offset_date;
                            req.offset_date = i3;
                            i9 = i2;
                            i10 = i11;
                            i8 = i3;
                            j2 = dialog_id;
                            anonymousClass60 = lower_part;
                            i5 = classGuid;
                            instance = ConnectionsManager.getInstance(r14.currentAccount);
                            i4 = first_unread;
                            i3 = last_message_id;
                            i11 = unread_count;
                            i2 = last_date;
                            i = load_type;
                            r17 = lower_part3;
                            z3 = isChannel;
                            i12 = loadIndex;
                            req2 = req;
                            z4 = queryFromServer;
                            i13 = mentionsCount;
                            lower_part = /* anonymous class already generated */;
                            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(instance.sendRequest(req2, anonymousClass60), classGuid);
                        }
                    }
                    req.add_offset = -1;
                    req.limit++;
                    req.limit = i2;
                    req.offset_id = i11;
                    i3 = offset_date;
                    req.offset_date = i3;
                    i9 = i2;
                    i10 = i11;
                    i8 = i3;
                    j2 = dialog_id;
                    anonymousClass60 = lower_part;
                    i5 = classGuid;
                    instance = ConnectionsManager.getInstance(r14.currentAccount);
                    i4 = first_unread;
                    i3 = last_message_id;
                    i11 = unread_count;
                    i2 = last_date;
                    i = load_type;
                    r17 = lower_part3;
                    z3 = isChannel;
                    i12 = loadIndex;
                    req2 = req;
                    z4 = queryFromServer;
                    i13 = mentionsCount;
                    lower_part = /* anonymous class already generated */;
                    ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(instance.sendRequest(req2, anonymousClass60), classGuid);
                } else {
                    req.add_offset = (-i2) - 1;
                }
            } else {
                i2 = count;
                req.add_offset = (-i2) + 5;
            }
            i11 = max_id;
            req.limit = i2;
            req.offset_id = i11;
            i3 = offset_date;
            req.offset_date = i3;
            i9 = i2;
            i10 = i11;
            i8 = i3;
            j2 = dialog_id;
            anonymousClass60 = lower_part;
            i5 = classGuid;
            instance = ConnectionsManager.getInstance(r14.currentAccount);
            i4 = first_unread;
            i3 = last_message_id;
            i11 = unread_count;
            i2 = last_date;
            i = load_type;
            r17 = lower_part3;
            z3 = isChannel;
            i12 = loadIndex;
            req2 = req;
            z4 = queryFromServer;
            i13 = mentionsCount;
            lower_part = /* anonymous class already generated */;
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(instance.sendRequest(req2, anonymousClass60), classGuid);
        }
        MessagesStorage.getInstance(messagesController.currentAccount).getMessages(dialog_id, count, max_id, offset_date, midDate, i, load_type, isChannel, loadIndex);
    }

    public void reloadWebPages(final long dialog_id, HashMap<String, ArrayList<MessageObject>> webpagesToReload) {
        for (Entry<String, ArrayList<MessageObject>> entry : webpagesToReload.entrySet()) {
            final String url = (String) entry.getKey();
            ArrayList<MessageObject> messages = (ArrayList) entry.getValue();
            ArrayList<MessageObject> arrayList = (ArrayList) this.reloadingWebpages.get(url);
            if (arrayList == null) {
                arrayList = new ArrayList();
                this.reloadingWebpages.put(url, arrayList);
            }
            arrayList.addAll(messages);
            TL_messages_getWebPagePreview req = new TL_messages_getWebPagePreview();
            req.message = url;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(final TLObject response, TL_error error) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            ArrayList<MessageObject> arrayList = (ArrayList) MessagesController.this.reloadingWebpages.remove(url);
                            if (arrayList != null) {
                                messages_Messages messagesRes = new TL_messages_messages();
                                if (response instanceof TL_messageMediaWebPage) {
                                    TL_messageMediaWebPage media = response;
                                    if (!(media.webpage instanceof TL_webPage)) {
                                        if (!(media.webpage instanceof TL_webPageEmpty)) {
                                            MessagesController.this.reloadingWebpagesPending.put(media.webpage.id, arrayList);
                                        }
                                    }
                                    for (int a = 0; a < arrayList.size(); a++) {
                                        ((MessageObject) arrayList.get(a)).messageOwner.media.webpage = media.webpage;
                                        if (a == 0) {
                                            ImageLoader.saveMessageThumbs(((MessageObject) arrayList.get(a)).messageOwner);
                                        }
                                        messagesRes.messages.add(((MessageObject) arrayList.get(a)).messageOwner);
                                    }
                                } else {
                                    for (int a2 = 0; a2 < arrayList.size(); a2++) {
                                        ((MessageObject) arrayList.get(a2)).messageOwner.media.webpage = new TL_webPageEmpty();
                                        messagesRes.messages.add(((MessageObject) arrayList.get(a2)).messageOwner);
                                    }
                                }
                                if (!messagesRes.messages.isEmpty()) {
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(messagesRes, dialog_id, -2, 0, false);
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(dialog_id), arrayList);
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    public void processLoadedMessages(messages_Messages messagesRes, long dialog_id, int count, int max_id, int offset_date, boolean isCache, int classGuid, int first_unread, int last_message_id, int unread_count, int last_date, int load_type, boolean isChannel, boolean isEnd, int loadIndex, boolean queryFromServer, int mentionsCount) {
        messages_Messages org_telegram_tgnet_TLRPC_messages_Messages;
        long j;
        int i;
        boolean z;
        int i2;
        int i3;
        int i4;
        boolean z2;
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("processLoadedMessages size ");
            org_telegram_tgnet_TLRPC_messages_Messages = messagesRes;
            stringBuilder.append(org_telegram_tgnet_TLRPC_messages_Messages.messages.size());
            stringBuilder.append(" in chat ");
            j = dialog_id;
            stringBuilder.append(j);
            stringBuilder.append(" count ");
            i = count;
            stringBuilder.append(i);
            stringBuilder.append(" max_id ");
            stringBuilder.append(max_id);
            stringBuilder.append(" cache ");
            z = isCache;
            stringBuilder.append(z);
            stringBuilder.append(" guid ");
            stringBuilder.append(classGuid);
            stringBuilder.append(" load_type ");
            stringBuilder.append(load_type);
            stringBuilder.append(" last_message_id ");
            stringBuilder.append(last_message_id);
            stringBuilder.append(" isChannel ");
            stringBuilder.append(isChannel);
            stringBuilder.append(" index ");
            stringBuilder.append(loadIndex);
            stringBuilder.append(" firstUnread ");
            stringBuilder.append(first_unread);
            stringBuilder.append(" unread_count ");
            stringBuilder.append(unread_count);
            stringBuilder.append(" last_date ");
            stringBuilder.append(last_date);
            stringBuilder.append(" queryFromServer ");
            stringBuilder.append(queryFromServer);
            FileLog.d(stringBuilder.toString());
        } else {
            org_telegram_tgnet_TLRPC_messages_Messages = messagesRes;
            j = dialog_id;
            i = count;
            i2 = max_id;
            z = isCache;
            int i5 = classGuid;
            int i6 = first_unread;
            i3 = last_message_id;
            int i7 = unread_count;
            int i8 = last_date;
            i4 = load_type;
            z2 = isChannel;
            int i9 = loadIndex;
            boolean z3 = queryFromServer;
        }
        final messages_Messages org_telegram_tgnet_TLRPC_messages_Messages2 = org_telegram_tgnet_TLRPC_messages_Messages;
        final long j2 = j;
        z2 = z;
        i3 = i;
        i4 = load_type;
        final boolean z4 = queryFromServer;
        final int i10 = first_unread;
        i2 = max_id;
        i = offset_date;
        final int i11 = classGuid;
        final int i12 = last_message_id;
        final boolean z5 = isChannel;
        final int i13 = loadIndex;
        final int i14 = unread_count;
        final int i15 = last_date;
        final int i16 = mentionsCount;
        final boolean z6 = isEnd;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                int channelId;
                int channelPts;
                boolean createDialog = false;
                boolean isMegagroup = false;
                if (org_telegram_tgnet_TLRPC_messages_Messages2 instanceof TL_messages_channelMessages) {
                    channelId = -((int) j2);
                    channelPts = MessagesController.this.channelsPts.get(channelId);
                    if (channelPts != 0) {
                    } else if (MessagesStorage.getInstance(MessagesController.this.currentAccount).getChannelPtsSync(channelId) == 0) {
                        MessagesController.this.channelsPts.put(channelId, org_telegram_tgnet_TLRPC_messages_Messages2.pts);
                        createDialog = true;
                        if (MessagesController.this.needShortPollChannels.indexOfKey(channelId) < 0 || MessagesController.this.shortPollChannels.indexOfKey(channelId) >= 0) {
                            MessagesController.this.getChannelDifference(channelId);
                        } else {
                            MessagesController.this.getChannelDifference(channelId, 2, 0, null);
                        }
                    }
                    for (channelPts = 0; channelPts < org_telegram_tgnet_TLRPC_messages_Messages2.chats.size(); channelPts++) {
                        Chat chat = (Chat) org_telegram_tgnet_TLRPC_messages_Messages2.chats.get(channelPts);
                        if (chat.id == channelId) {
                            isMegagroup = chat.megagroup;
                            break;
                        }
                    }
                }
                channelId = (int) j2;
                channelPts = (int) (j2 >> 32);
                if (!z2) {
                    ImageLoader.saveMessagesThumbs(org_telegram_tgnet_TLRPC_messages_Messages2.messages);
                }
                if (channelPts == 1 || channelId == 0 || !z2 || org_telegram_tgnet_TLRPC_messages_Messages2.messages.size() != 0) {
                    int a;
                    SparseArray<User> usersDict = new SparseArray();
                    SparseArray<Chat> chatsDict = new SparseArray();
                    for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Messages2.users.size(); a++) {
                        User u = (User) org_telegram_tgnet_TLRPC_messages_Messages2.users.get(a);
                        usersDict.put(u.id, u);
                    }
                    for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Messages2.chats.size(); a++) {
                        Chat c = (Chat) org_telegram_tgnet_TLRPC_messages_Messages2.chats.get(a);
                        chatsDict.put(c.id, c);
                    }
                    int size = org_telegram_tgnet_TLRPC_messages_Messages2.messages.size();
                    if (!z2) {
                        Integer inboxValue = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(j2));
                        if (inboxValue == null) {
                            inboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, j2));
                            MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(j2), inboxValue);
                        }
                        Integer outboxValue = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(j2));
                        if (outboxValue == null) {
                            outboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, j2));
                            MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(j2), outboxValue);
                        }
                        for (int a2 = 0; a2 < size; a2++) {
                            Message message = (Message) org_telegram_tgnet_TLRPC_messages_Messages2.messages.get(a2);
                            if (isMegagroup) {
                                message.flags |= Integer.MIN_VALUE;
                            }
                            if (message.action instanceof TL_messageActionChatDeleteUser) {
                                User user = (User) usersDict.get(message.action.user_id);
                                if (user != null && user.bot) {
                                    message.reply_markup = new TL_replyKeyboardHide();
                                    message.flags |= 64;
                                }
                            }
                            if (!(message.action instanceof TL_messageActionChatMigrateTo)) {
                                if (!(message.action instanceof TL_messageActionChannelCreate)) {
                                    message.unread = (message.out ? outboxValue : inboxValue).intValue() < message.id;
                                }
                            }
                            message.unread = false;
                            message.media_unread = false;
                        }
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(org_telegram_tgnet_TLRPC_messages_Messages2, j2, i4, i2, createDialog);
                    }
                    final ArrayList<MessageObject> objects = new ArrayList();
                    final ArrayList<Integer> messagesToReload = new ArrayList();
                    final HashMap<String, ArrayList<MessageObject>> webpagesToReload = new HashMap();
                    a = 0;
                    while (true) {
                        int a3 = a;
                        int size2;
                        SparseArray<Chat> chatsDict2;
                        if (a3 < size) {
                            Message message2 = (Message) org_telegram_tgnet_TLRPC_messages_Messages2.messages.get(a3);
                            message2.dialog_id = j2;
                            size2 = size;
                            chatsDict2 = chatsDict;
                            boolean createDialog2 = createDialog;
                            createDialog = message2;
                            int a4 = a3;
                            MessageObject messageObject = new MessageObject(MessagesController.this.currentAccount, message2, (SparseArray) usersDict, (SparseArray) chatsDict2, true);
                            objects.add(messageObject);
                            if (z2) {
                                if (createDialog.media instanceof TL_messageMediaUnsupported) {
                                    if (createDialog.media.bytes != null) {
                                        if (createDialog.media.bytes.length != 0) {
                                            if (createDialog.media.bytes.length == 1) {
                                                if (createDialog.media.bytes[0] < (byte) 76) {
                                                }
                                            }
                                        }
                                        messagesToReload.add(Integer.valueOf(createDialog.id));
                                    }
                                } else if (createDialog.media instanceof TL_messageMediaWebPage) {
                                    if ((createDialog.media.webpage instanceof TL_webPagePending) && createDialog.media.webpage.date <= ConnectionsManager.getInstance(MessagesController.this.currentAccount).getCurrentTime()) {
                                        messagesToReload.add(Integer.valueOf(createDialog.id));
                                    } else if (createDialog.media.webpage instanceof TL_webPageUrlPending) {
                                        ArrayList<MessageObject> arrayList = (ArrayList) webpagesToReload.get(createDialog.media.webpage.url);
                                        if (arrayList == null) {
                                            arrayList = new ArrayList();
                                            webpagesToReload.put(createDialog.media.webpage.url, arrayList);
                                        }
                                        arrayList.add(messageObject);
                                    }
                                }
                                a = a4 + 1;
                                size = size2;
                                chatsDict = chatsDict2;
                                createDialog = createDialog2;
                            }
                            a = a4 + 1;
                            size = size2;
                            chatsDict = chatsDict2;
                            createDialog = createDialog2;
                        } else {
                            size2 = size;
                            chatsDict2 = chatsDict;
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.putUsers(org_telegram_tgnet_TLRPC_messages_Messages2.users, z2);
                                    MessagesController.this.putChats(org_telegram_tgnet_TLRPC_messages_Messages2.chats, z2);
                                    int first_unread_final = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                    if (z4 && i4 == 2) {
                                        int first_unread_final2 = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                        for (first_unread_final = 0; first_unread_final < org_telegram_tgnet_TLRPC_messages_Messages2.messages.size(); first_unread_final++) {
                                            Message message = (Message) org_telegram_tgnet_TLRPC_messages_Messages2.messages.get(first_unread_final);
                                            if (!message.out && message.id > i10 && message.id < first_unread_final2) {
                                                first_unread_final2 = message.id;
                                            }
                                        }
                                        first_unread_final = first_unread_final2;
                                    }
                                    if (first_unread_final == ConnectionsManager.DEFAULT_DATACENTER_ID) {
                                        first_unread_final = i10;
                                    }
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesDidLoaded, Long.valueOf(j2), Integer.valueOf(i3), objects, Boolean.valueOf(z2), Integer.valueOf(first_unread_final), Integer.valueOf(i12), Integer.valueOf(i14), Integer.valueOf(i15), Integer.valueOf(i4), Boolean.valueOf(z6), Integer.valueOf(i11), Integer.valueOf(i13), Integer.valueOf(i2), Integer.valueOf(i16));
                                    if (!messagesToReload.isEmpty()) {
                                        MessagesController.this.reloadMessages(messagesToReload, j2);
                                    }
                                    if (!webpagesToReload.isEmpty()) {
                                        MessagesController.this.reloadWebPages(j2, webpagesToReload);
                                    }
                                }
                            });
                            return;
                        }
                    }
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        MessagesController messagesController = MessagesController.this;
                        long j = j2;
                        int i = i3;
                        int i2 = (i4 == 2 && z4) ? i10 : i2;
                        int i3 = i2;
                        int i4 = i;
                        int i5 = i11;
                        int i6 = i4;
                        int i7 = i12;
                        boolean z = z5;
                        int i8 = i13;
                        int i9 = i10;
                        i2 = i14;
                        int i10 = i2;
                        messagesController.loadMessages(j, i, i3, i4, false, 0, i5, i6, i7, z, i8, i9, i10, i15, z4, i16);
                    }
                });
            }
        });
    }

    public void loadHintDialogs() {
        if (this.hintDialogs.isEmpty()) {
            if (!TextUtils.isEmpty(this.installReferer)) {
                TL_help_getRecentMeUrls req = new TL_help_getRecentMeUrls();
                req.referer = this.installReferer;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, TL_error error) {
                        if (error == null) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    TL_help_recentMeUrls res = response;
                                    MessagesController.this.putUsers(res.users, false);
                                    MessagesController.this.putChats(res.chats, false);
                                    MessagesController.this.hintDialogs.clear();
                                    MessagesController.this.hintDialogs.addAll(res.urls);
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    public void loadDialogs(int offset, final int count, boolean fromCache) {
        if (!this.loadingDialogs) {
            if (!this.resetingDialogs) {
                this.loadingDialogs = true;
                int i = 0;
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("load cacheOffset = ");
                    stringBuilder.append(offset);
                    stringBuilder.append(" count = ");
                    stringBuilder.append(count);
                    stringBuilder.append(" cache = ");
                    stringBuilder.append(fromCache);
                    FileLog.d(stringBuilder.toString());
                }
                if (fromCache) {
                    MessagesStorage instance = MessagesStorage.getInstance(this.currentAccount);
                    if (offset != 0) {
                        i = this.nextDialogsCacheOffset;
                    }
                    instance.getDialogs(i, count);
                } else {
                    TL_messages_getDialogs req = new TL_messages_getDialogs();
                    req.limit = count;
                    req.exclude_pinned = true;
                    if (UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetId == -1) {
                        boolean found = false;
                        for (i = this.dialogs.size() - 1; i >= 0; i--) {
                            TL_dialog dialog = (TL_dialog) this.dialogs.get(i);
                            if (!dialog.pinned) {
                                int high_id = (int) (dialog.id >> 32);
                                if (!(((int) dialog.id) == 0 || high_id == 1 || dialog.top_message <= 0)) {
                                    MessageObject message = (MessageObject) this.dialogMessage.get(dialog.id);
                                    if (message != null && message.getId() > 0) {
                                        int id;
                                        req.offset_date = message.messageOwner.date;
                                        req.offset_id = message.messageOwner.id;
                                        if (message.messageOwner.to_id.channel_id != 0) {
                                            id = -message.messageOwner.to_id.channel_id;
                                        } else if (message.messageOwner.to_id.chat_id != 0) {
                                            id = -message.messageOwner.to_id.chat_id;
                                        } else {
                                            id = message.messageOwner.to_id.user_id;
                                            req.offset_peer = getInputPeer(id);
                                            found = true;
                                            if (!found) {
                                                req.offset_peer = new TL_inputPeerEmpty();
                                            }
                                        }
                                        req.offset_peer = getInputPeer(id);
                                        found = true;
                                        if (found) {
                                            req.offset_peer = new TL_inputPeerEmpty();
                                        }
                                    }
                                }
                            }
                        }
                        if (found) {
                            req.offset_peer = new TL_inputPeerEmpty();
                        }
                    } else if (UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetId == ConnectionsManager.DEFAULT_DATACENTER_ID) {
                        this.dialogsEndReached = true;
                        this.serverDialogsEndReached = true;
                        this.loadingDialogs = false;
                        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                        return;
                    } else {
                        req.offset_id = UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetId;
                        req.offset_date = UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetDate;
                        if (req.offset_id == 0) {
                            req.offset_peer = new TL_inputPeerEmpty();
                        } else {
                            if (UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetChannelId != 0) {
                                req.offset_peer = new TL_inputPeerChannel();
                                req.offset_peer.channel_id = UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetChannelId;
                            } else if (UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetUserId != 0) {
                                req.offset_peer = new TL_inputPeerUser();
                                req.offset_peer.user_id = UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetUserId;
                            } else {
                                req.offset_peer = new TL_inputPeerChat();
                                req.offset_peer.chat_id = UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetChatId;
                            }
                            req.offset_peer.access_hash = UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetAccess;
                        }
                    }
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            if (error == null) {
                                MessagesController.this.processLoadedDialogs((messages_Dialogs) response, null, 0, count, 0, false, false, false);
                            }
                        }
                    });
                }
            }
        }
    }

    public void forceResetDialogs() {
        resetDialogs(true, MessagesStorage.getInstance(this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue());
    }

    private void resetDialogs(boolean query, int seq, int newPts, int date, int qts) {
        MessagesController messagesController = this;
        if (query) {
            if (!messagesController.resetingDialogs) {
                messagesController.resetingDialogs = true;
                final int i = seq;
                final int i2 = newPts;
                final int i3 = date;
                final int i4 = qts;
                ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(new TL_messages_getPinnedDialogs(), new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (response != null) {
                            MessagesController.this.resetDialogsPinned = (TL_messages_peerDialogs) response;
                            MessagesController.this.resetDialogs(false, i, i2, i3, i4);
                        }
                    }
                });
                TL_messages_getDialogs req2 = new TL_messages_getDialogs();
                req2.limit = 100;
                req2.exclude_pinned = true;
                req2.offset_peer = new TL_inputPeerEmpty();
                ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req2, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            MessagesController.this.resetDialogsAll = (messages_Dialogs) response;
                            MessagesController.this.resetDialogs(false, i, i2, i3, i4);
                        }
                    }
                });
            }
        } else if (!(messagesController.resetDialogsPinned == null || messagesController.resetDialogsAll == null)) {
            int a;
            SparseArray<Chat> chatsDict;
            int messagesCount = messagesController.resetDialogsAll.messages.size();
            int dialogsCount = messagesController.resetDialogsAll.dialogs.size();
            messagesController.resetDialogsAll.dialogs.addAll(messagesController.resetDialogsPinned.dialogs);
            messagesController.resetDialogsAll.messages.addAll(messagesController.resetDialogsPinned.messages);
            messagesController.resetDialogsAll.users.addAll(messagesController.resetDialogsPinned.users);
            messagesController.resetDialogsAll.chats.addAll(messagesController.resetDialogsPinned.chats);
            LongSparseArray<TL_dialog> new_dialogs_dict = new LongSparseArray();
            LongSparseArray<MessageObject> new_dialogMessage = new LongSparseArray();
            SparseArray<User> usersDict = new SparseArray();
            SparseArray<Chat> chatsDict2 = new SparseArray();
            for (a = 0; a < messagesController.resetDialogsAll.users.size(); a++) {
                User u = (User) messagesController.resetDialogsAll.users.get(a);
                usersDict.put(u.id, u);
            }
            for (a = 0; a < messagesController.resetDialogsAll.chats.size(); a++) {
                Chat c = (Chat) messagesController.resetDialogsAll.chats.get(a);
                chatsDict2.put(c.id, c);
            }
            Message lastMessage = null;
            a = 0;
            while (true) {
                int a2 = a;
                if (a2 >= messagesController.resetDialogsAll.messages.size()) {
                    break;
                }
                int a3;
                Message message = (Message) messagesController.resetDialogsAll.messages.get(a2);
                if (a2 < messagesCount && (lastMessage == null || message.date < lastMessage.date)) {
                    lastMessage = message;
                }
                Message lastMessage2 = lastMessage;
                Chat chat;
                MessageObject messageObject;
                if (message.to_id.channel_id != 0) {
                    chat = (Chat) chatsDict2.get(message.to_id.channel_id);
                    if (chat == null || !chat.left) {
                        if (chat != null && chat.megagroup) {
                            message.flags |= Integer.MIN_VALUE;
                        }
                        a3 = a2;
                        messageObject = new MessageObject(messagesController.currentAccount, message, (SparseArray) usersDict, (SparseArray) chatsDict2, false);
                        new_dialogMessage.put(messageObject.getDialogId(), messageObject);
                        a = a3 + 1;
                        lastMessage = lastMessage2;
                    }
                } else {
                    if (message.to_id.chat_id != 0) {
                        chat = (Chat) chatsDict2.get(message.to_id.chat_id);
                        if (!(chat == null || chat.migrated_to == null)) {
                        }
                    }
                    a3 = a2;
                    messageObject = new MessageObject(messagesController.currentAccount, message, (SparseArray) usersDict, (SparseArray) chatsDict2, false);
                    new_dialogMessage.put(messageObject.getDialogId(), messageObject);
                    a = a3 + 1;
                    lastMessage = lastMessage2;
                }
                a3 = a2;
                a = a3 + 1;
                lastMessage = lastMessage2;
            }
            for (a = 0; a < messagesController.resetDialogsAll.dialogs.size(); a++) {
                TL_dialog d = (TL_dialog) messagesController.resetDialogsAll.dialogs.get(a);
                if (d.id == 0 && d.peer != null) {
                    if (d.peer.user_id != 0) {
                        d.id = (long) d.peer.user_id;
                    } else if (d.peer.chat_id != 0) {
                        d.id = (long) (-d.peer.chat_id);
                    } else if (d.peer.channel_id != 0) {
                        d.id = (long) (-d.peer.channel_id);
                    }
                }
                if (d.id != 0) {
                    if (d.last_message_date == 0) {
                        MessageObject mess = (MessageObject) new_dialogMessage.get(d.id);
                        if (mess != null) {
                            d.last_message_date = mess.messageOwner.date;
                        }
                    }
                    if (DialogObject.isChannel(d)) {
                        Chat chat2 = (Chat) chatsDict2.get(-((int) d.id));
                        if (chat2 == null || !chat2.left) {
                            messagesController.channelsPts.put(-((int) d.id), d.pts);
                        }
                    } else if (((int) d.id) < 0) {
                        Chat chat3 = (Chat) chatsDict2.get(-((int) d.id));
                        if (!(chat3 == null || chat3.migrated_to == null)) {
                        }
                    }
                    new_dialogs_dict.put(d.id, d);
                    Integer value = (Integer) messagesController.dialogs_read_inbox_max.get(Long.valueOf(d.id));
                    if (value == null) {
                        value = Integer.valueOf(0);
                    }
                    messagesController.dialogs_read_inbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_inbox_max_id)));
                    value = (Integer) messagesController.dialogs_read_outbox_max.get(Long.valueOf(d.id));
                    if (value == null) {
                        value = Integer.valueOf(0);
                    }
                    messagesController.dialogs_read_outbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_outbox_max_id)));
                }
            }
            ImageLoader.saveMessagesThumbs(messagesController.resetDialogsAll.messages);
            int a4 = 0;
            while (a4 < messagesController.resetDialogsAll.messages.size()) {
                SparseArray<User> usersDict2;
                Message message2 = (Message) messagesController.resetDialogsAll.messages.get(a4);
                if (message2.action instanceof TL_messageActionChatDeleteUser) {
                    User user = (User) usersDict.get(message2.action.user_id);
                    if (user != null && user.bot) {
                        message2.reply_markup = new TL_replyKeyboardHide();
                        message2.flags |= 64;
                    }
                }
                if (message2.action instanceof TL_messageActionChatMigrateTo) {
                    usersDict2 = usersDict;
                    chatsDict = chatsDict2;
                } else if (message2.action instanceof TL_messageActionChannelCreate) {
                    usersDict2 = usersDict;
                    chatsDict = chatsDict2;
                } else {
                    ConcurrentHashMap<Long, Integer> read_max = message2.out ? messagesController.dialogs_read_outbox_max : messagesController.dialogs_read_inbox_max;
                    Integer value2 = (Integer) read_max.get(Long.valueOf(message2.dialog_id));
                    if (value2 == null) {
                        usersDict2 = usersDict;
                        chatsDict = chatsDict2;
                        value2 = Integer.valueOf(MessagesStorage.getInstance(messagesController.currentAccount).getDialogReadMax(message2.out, message2.dialog_id));
                        read_max.put(Long.valueOf(message2.dialog_id), value2);
                    } else {
                        usersDict2 = usersDict;
                        chatsDict = chatsDict2;
                    }
                    message2.unread = value2.intValue() < message2.id;
                    a4++;
                    usersDict = usersDict2;
                    chatsDict2 = chatsDict;
                }
                message2.unread = false;
                message2.media_unread = false;
                a4++;
                usersDict = usersDict2;
                chatsDict2 = chatsDict;
            }
            chatsDict = chatsDict2;
            MessagesStorage.getInstance(messagesController.currentAccount).resetDialogs(messagesController.resetDialogsAll, messagesCount, seq, newPts, date, qts, new_dialogs_dict, new_dialogMessage, lastMessage, dialogsCount);
            messagesController.resetDialogsPinned = null;
            messagesController.resetDialogsAll = null;
        }
    }

    protected void completeDialogsReset(messages_Dialogs dialogsRes, int messagesCount, int seq, int newPts, int date, int qts, LongSparseArray<TL_dialog> new_dialogs_dict, LongSparseArray<MessageObject> new_dialogMessage, Message lastMessage) {
        final int i = newPts;
        final int i2 = date;
        final int i3 = qts;
        final messages_Dialogs org_telegram_tgnet_TLRPC_messages_Dialogs = dialogsRes;
        final LongSparseArray<TL_dialog> longSparseArray = new_dialogs_dict;
        final LongSparseArray<MessageObject> longSparseArray2 = new_dialogMessage;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.this.gettingDifference = false;
                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastPtsValue(i);
                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastDateValue(i2);
                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastQtsValue(i3);
                MessagesController.this.getDifference();
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        int a;
                        MessagesController.this.resetingDialogs = false;
                        MessagesController.this.applyDialogsNotificationsSettings(org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs);
                        if (!UserConfig.getInstance(MessagesController.this.currentAccount).draftsLoaded) {
                            DataQuery.getInstance(MessagesController.this.currentAccount).loadDrafts();
                        }
                        MessagesController.this.putUsers(org_telegram_tgnet_TLRPC_messages_Dialogs.users, false);
                        MessagesController.this.putChats(org_telegram_tgnet_TLRPC_messages_Dialogs.chats, false);
                        for (a = 0; a < MessagesController.this.dialogs.size(); a++) {
                            TL_dialog oldDialog = (TL_dialog) MessagesController.this.dialogs.get(a);
                            if (((int) oldDialog.id) != 0) {
                                MessagesController.this.dialogs_dict.remove(oldDialog.id);
                                MessageObject messageObject = (MessageObject) MessagesController.this.dialogMessage.get(oldDialog.id);
                                MessagesController.this.dialogMessage.remove(oldDialog.id);
                                if (messageObject != null) {
                                    MessagesController.this.dialogMessagesByIds.remove(messageObject.getId());
                                    if (messageObject.messageOwner.random_id != 0) {
                                        MessagesController.this.dialogMessagesByRandomIds.remove(messageObject.messageOwner.random_id);
                                    }
                                }
                            }
                        }
                        for (a = 0; a < longSparseArray.size(); a++) {
                            long key = longSparseArray.keyAt(a);
                            oldDialog = (TL_dialog) longSparseArray.valueAt(a);
                            if (oldDialog.draft instanceof TL_draftMessage) {
                                DataQuery.getInstance(MessagesController.this.currentAccount).saveDraft(oldDialog.id, oldDialog.draft, null, false);
                            }
                            MessagesController.this.dialogs_dict.put(key, oldDialog);
                            MessageObject messageObject2 = (MessageObject) longSparseArray2.get(oldDialog.id);
                            MessagesController.this.dialogMessage.put(key, messageObject2);
                            if (messageObject2 != null && messageObject2.messageOwner.to_id.channel_id == 0) {
                                MessagesController.this.dialogMessagesByIds.put(messageObject2.getId(), messageObject2);
                                if (messageObject2.messageOwner.random_id != 0) {
                                    MessagesController.this.dialogMessagesByRandomIds.put(messageObject2.messageOwner.random_id, messageObject2);
                                }
                            }
                        }
                        MessagesController.this.dialogs.clear();
                        int size = MessagesController.this.dialogs_dict.size();
                        for (a = 0; a < size; a++) {
                            MessagesController.this.dialogs.add(MessagesController.this.dialogs_dict.valueAt(a));
                        }
                        MessagesController.this.sortDialogs(null);
                        MessagesController.this.dialogsEndReached = true;
                        MessagesController.this.serverDialogsEndReached = false;
                        if (!(UserConfig.getInstance(MessagesController.this.currentAccount).totalDialogsLoadCount >= 400 || UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId == -1 || UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId == ConnectionsManager.DEFAULT_DATACENTER_ID)) {
                            MessagesController.this.loadDialogs(0, 100, false);
                        }
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                    }
                });
            }
        });
    }

    private void migrateDialogs(final int offset, int offsetDate, int offsetUser, int offsetChat, int offsetChannel, long accessPeer) {
        if (!this.migratingDialogs) {
            if (offset != -1) {
                this.migratingDialogs = true;
                TL_messages_getDialogs req = new TL_messages_getDialogs();
                req.exclude_pinned = true;
                req.limit = 100;
                req.offset_id = offset;
                req.offset_date = offsetDate;
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("start migrate with id ");
                    stringBuilder.append(offset);
                    stringBuilder.append(" date ");
                    stringBuilder.append(LocaleController.getInstance().formatterStats.format(((long) offsetDate) * 1000));
                    FileLog.d(stringBuilder.toString());
                }
                if (offset == 0) {
                    req.offset_peer = new TL_inputPeerEmpty();
                } else {
                    if (offsetChannel != 0) {
                        req.offset_peer = new TL_inputPeerChannel();
                        req.offset_peer.channel_id = offsetChannel;
                    } else if (offsetUser != 0) {
                        req.offset_peer = new TL_inputPeerUser();
                        req.offset_peer.user_id = offsetUser;
                    } else {
                        req.offset_peer = new TL_inputPeerChat();
                        req.offset_peer.chat_id = offsetChat;
                    }
                    req.offset_peer.access_hash = accessPeer;
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            final messages_Dialogs dialogsRes = (messages_Dialogs) response;
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                public void run() {
                                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.68.1.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                                    r1 = r21;
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r3 = r2.totalDialogsLoadCount;	 Catch:{ Exception -> 0x05eb }
                                    r4 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r4 = r4.dialogs;	 Catch:{ Exception -> 0x05eb }
                                    r4 = r4.size();	 Catch:{ Exception -> 0x05eb }
                                    r3 = r3 + r4;	 Catch:{ Exception -> 0x05eb }
                                    r2.totalDialogsLoadCount = r3;	 Catch:{ Exception -> 0x05eb }
                                    r2 = 0;	 Catch:{ Exception -> 0x05eb }
                                    r3 = 0;	 Catch:{ Exception -> 0x05eb }
                                    r4 = r2;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r3;	 Catch:{ Exception -> 0x05eb }
                                L_0x001f:
                                    r5 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.messages;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.size();	 Catch:{ Exception -> 0x05eb }
                                    r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Exception -> 0x05eb }
                                    if (r2 >= r5) goto L_0x0071;	 Catch:{ Exception -> 0x05eb }
                                L_0x002b:
                                    r5 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.messages;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.get(r2);	 Catch:{ Exception -> 0x05eb }
                                    r5 = (org.telegram.tgnet.TLRPC.Message) r5;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x05eb }
                                    if (r8 == 0) goto L_0x0065;	 Catch:{ Exception -> 0x05eb }
                                L_0x0039:
                                    r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x05eb }
                                    r8.<init>();	 Catch:{ Exception -> 0x05eb }
                                    r9 = "search migrate id ";	 Catch:{ Exception -> 0x05eb }
                                    r8.append(r9);	 Catch:{ Exception -> 0x05eb }
                                    r9 = r5.id;	 Catch:{ Exception -> 0x05eb }
                                    r8.append(r9);	 Catch:{ Exception -> 0x05eb }
                                    r9 = " date ";	 Catch:{ Exception -> 0x05eb }
                                    r8.append(r9);	 Catch:{ Exception -> 0x05eb }
                                    r9 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x05eb }
                                    r9 = r9.formatterStats;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r5.date;	 Catch:{ Exception -> 0x05eb }
                                    r10 = (long) r10;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10 * r6;	 Catch:{ Exception -> 0x05eb }
                                    r6 = r9.format(r10);	 Catch:{ Exception -> 0x05eb }
                                    r8.append(r6);	 Catch:{ Exception -> 0x05eb }
                                    r6 = r8.toString();	 Catch:{ Exception -> 0x05eb }
                                    org.telegram.messenger.FileLog.d(r6);	 Catch:{ Exception -> 0x05eb }
                                L_0x0065:
                                    if (r4 == 0) goto L_0x006d;	 Catch:{ Exception -> 0x05eb }
                                L_0x0067:
                                    r6 = r5.date;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r4.date;	 Catch:{ Exception -> 0x05eb }
                                    if (r6 >= r7) goto L_0x006e;	 Catch:{ Exception -> 0x05eb }
                                L_0x006d:
                                    r4 = r5;	 Catch:{ Exception -> 0x05eb }
                                L_0x006e:
                                    r2 = r2 + 1;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x001f;	 Catch:{ Exception -> 0x05eb }
                                L_0x0071:
                                    r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x05eb }
                                    if (r2 == 0) goto L_0x00a1;	 Catch:{ Exception -> 0x05eb }
                                L_0x0075:
                                    r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x05eb }
                                    r2.<init>();	 Catch:{ Exception -> 0x05eb }
                                    r5 = "migrate step with id ";	 Catch:{ Exception -> 0x05eb }
                                    r2.append(r5);	 Catch:{ Exception -> 0x05eb }
                                    r5 = r4.id;	 Catch:{ Exception -> 0x05eb }
                                    r2.append(r5);	 Catch:{ Exception -> 0x05eb }
                                    r5 = " date ";	 Catch:{ Exception -> 0x05eb }
                                    r2.append(r5);	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.formatterStats;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r4.date;	 Catch:{ Exception -> 0x05eb }
                                    r8 = (long) r8;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r8 * r6;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.format(r8);	 Catch:{ Exception -> 0x05eb }
                                    r2.append(r5);	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.toString();	 Catch:{ Exception -> 0x05eb }
                                    org.telegram.messenger.FileLog.d(r2);	 Catch:{ Exception -> 0x05eb }
                                L_0x00a1:
                                    r2 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.dialogs;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.size();	 Catch:{ Exception -> 0x05eb }
                                    r5 = 100;	 Catch:{ Exception -> 0x05eb }
                                    if (r2 < r5) goto L_0x00b1;	 Catch:{ Exception -> 0x05eb }
                                L_0x00ad:
                                    r2 = r4.id;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x0158;	 Catch:{ Exception -> 0x05eb }
                                L_0x00b1:
                                    r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x05eb }
                                    if (r2 == 0) goto L_0x00ba;	 Catch:{ Exception -> 0x05eb }
                                L_0x00b5:
                                    r2 = "migrate stop due to not 100 dialogs";	 Catch:{ Exception -> 0x05eb }
                                    org.telegram.messenger.FileLog.d(r2);	 Catch:{ Exception -> 0x05eb }
                                L_0x00ba:
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r5 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetId = r5;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.migrateOffsetDate;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetDate = r5;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.migrateOffsetUserId;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetUserId = r5;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.migrateOffsetChatId;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetChatId = r5;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.migrateOffsetChannelId;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetChannelId = r5;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r5 = r5.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r5 = org.telegram.messenger.UserConfig.getInstance(r5);	 Catch:{ Exception -> 0x05eb }
                                    r9 = r5.migrateOffsetAccess;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetAccess = r9;	 Catch:{ Exception -> 0x05eb }
                                    r2 = -1;	 Catch:{ Exception -> 0x05eb }
                                L_0x0158:
                                    r5 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x05eb }
                                    r9 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r9 = r9.dialogs;	 Catch:{ Exception -> 0x05eb }
                                    r9 = r9.size();	 Catch:{ Exception -> 0x05eb }
                                    r9 = r9 * 12;	 Catch:{ Exception -> 0x05eb }
                                    r5.<init>(r9);	 Catch:{ Exception -> 0x05eb }
                                    r9 = new android.util.LongSparseArray;	 Catch:{ Exception -> 0x05eb }
                                    r9.<init>();	 Catch:{ Exception -> 0x05eb }
                                    r10 = r3;	 Catch:{ Exception -> 0x05eb }
                                L_0x016d:
                                    r11 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.dialogs;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.size();	 Catch:{ Exception -> 0x05eb }
                                    if (r10 >= r11) goto L_0x01be;	 Catch:{ Exception -> 0x05eb }
                                L_0x0177:
                                    r11 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.dialogs;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.get(r10);	 Catch:{ Exception -> 0x05eb }
                                    r11 = (org.telegram.tgnet.TLRPC.TL_dialog) r11;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r11.peer;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.channel_id;	 Catch:{ Exception -> 0x05eb }
                                    if (r12 == 0) goto L_0x0190;	 Catch:{ Exception -> 0x05eb }
                                L_0x0187:
                                    r12 = r11.peer;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.channel_id;	 Catch:{ Exception -> 0x05eb }
                                    r12 = -r12;	 Catch:{ Exception -> 0x05eb }
                                    r12 = (long) r12;	 Catch:{ Exception -> 0x05eb }
                                    r11.id = r12;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x01a6;	 Catch:{ Exception -> 0x05eb }
                                L_0x0190:
                                    r12 = r11.peer;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.chat_id;	 Catch:{ Exception -> 0x05eb }
                                    if (r12 == 0) goto L_0x019f;	 Catch:{ Exception -> 0x05eb }
                                L_0x0196:
                                    r12 = r11.peer;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.chat_id;	 Catch:{ Exception -> 0x05eb }
                                    r12 = -r12;	 Catch:{ Exception -> 0x05eb }
                                    r12 = (long) r12;	 Catch:{ Exception -> 0x05eb }
                                    r11.id = r12;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x01a6;	 Catch:{ Exception -> 0x05eb }
                                L_0x019f:
                                    r12 = r11.peer;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.user_id;	 Catch:{ Exception -> 0x05eb }
                                    r12 = (long) r12;	 Catch:{ Exception -> 0x05eb }
                                    r11.id = r12;	 Catch:{ Exception -> 0x05eb }
                                L_0x01a6:
                                    r12 = r5.length();	 Catch:{ Exception -> 0x05eb }
                                    if (r12 <= 0) goto L_0x01b1;	 Catch:{ Exception -> 0x05eb }
                                L_0x01ac:
                                    r12 = ",";	 Catch:{ Exception -> 0x05eb }
                                    r5.append(r12);	 Catch:{ Exception -> 0x05eb }
                                L_0x01b1:
                                    r12 = r11.id;	 Catch:{ Exception -> 0x05eb }
                                    r5.append(r12);	 Catch:{ Exception -> 0x05eb }
                                    r12 = r11.id;	 Catch:{ Exception -> 0x05eb }
                                    r9.put(r12, r11);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10 + 1;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x016d;	 Catch:{ Exception -> 0x05eb }
                                L_0x01be:
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesStorage.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.getDatabase();	 Catch:{ Exception -> 0x05eb }
                                    r11 = java.util.Locale.US;	 Catch:{ Exception -> 0x05eb }
                                    r12 = "SELECT did FROM dialogs WHERE did IN (%s)";	 Catch:{ Exception -> 0x05eb }
                                    r13 = 1;	 Catch:{ Exception -> 0x05eb }
                                    r14 = new java.lang.Object[r13];	 Catch:{ Exception -> 0x05eb }
                                    r15 = r5.toString();	 Catch:{ Exception -> 0x05eb }
                                    r14[r3] = r15;	 Catch:{ Exception -> 0x05eb }
                                    r11 = java.lang.String.format(r11, r12, r14);	 Catch:{ Exception -> 0x05eb }
                                    r12 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.queryFinalized(r11, r12);	 Catch:{ Exception -> 0x05eb }
                                    r11 = r10.next();	 Catch:{ Exception -> 0x05eb }
                                    if (r11 == 0) goto L_0x0239;	 Catch:{ Exception -> 0x05eb }
                                L_0x01eb:
                                    r11 = r10.longValue(r3);	 Catch:{ Exception -> 0x05eb }
                                    r14 = r9.get(r11);	 Catch:{ Exception -> 0x05eb }
                                    r14 = (org.telegram.tgnet.TLRPC.TL_dialog) r14;	 Catch:{ Exception -> 0x05eb }
                                    r9.remove(r11);	 Catch:{ Exception -> 0x05eb }
                                    if (r14 == 0) goto L_0x0235;	 Catch:{ Exception -> 0x05eb }
                                L_0x01fa:
                                    r15 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r15 = r15.dialogs;	 Catch:{ Exception -> 0x05eb }
                                    r15.remove(r14);	 Catch:{ Exception -> 0x05eb }
                                    r15 = r3;	 Catch:{ Exception -> 0x05eb }
                                    r6 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r6 = r6.messages;	 Catch:{ Exception -> 0x05eb }
                                    r6 = r6.size();	 Catch:{ Exception -> 0x05eb }
                                    if (r15 >= r6) goto L_0x0235;	 Catch:{ Exception -> 0x05eb }
                                    r6 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r6 = r6.messages;	 Catch:{ Exception -> 0x05eb }
                                    r6 = r6.get(r15);	 Catch:{ Exception -> 0x05eb }
                                    r6 = (org.telegram.tgnet.TLRPC.Message) r6;	 Catch:{ Exception -> 0x05eb }
                                    r18 = org.telegram.messenger.MessageObject.getDialogId(r6);	 Catch:{ Exception -> 0x05eb }
                                    r7 = (r18 > r11 ? 1 : (r18 == r11 ? 0 : -1));	 Catch:{ Exception -> 0x05eb }
                                    if (r7 == 0) goto L_0x021f;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x0231;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.messages;	 Catch:{ Exception -> 0x05eb }
                                    r7.remove(r15);	 Catch:{ Exception -> 0x05eb }
                                    r15 = r15 + -1;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r6.id;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r14.top_message;	 Catch:{ Exception -> 0x05eb }
                                    if (r7 != r8) goto L_0x0231;	 Catch:{ Exception -> 0x05eb }
                                    r14.top_message = r3;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x0235;	 Catch:{ Exception -> 0x05eb }
                                    r15 = r15 + r13;	 Catch:{ Exception -> 0x05eb }
                                    r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x0202;	 Catch:{ Exception -> 0x05eb }
                                    r6 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x01e5;	 Catch:{ Exception -> 0x05eb }
                                L_0x0239:
                                    r10.dispose();	 Catch:{ Exception -> 0x05eb }
                                    r6 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x05eb }
                                    if (r6 == 0) goto L_0x025c;	 Catch:{ Exception -> 0x05eb }
                                    r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x05eb }
                                    r6.<init>();	 Catch:{ Exception -> 0x05eb }
                                    r7 = "migrate found missing dialogs ";	 Catch:{ Exception -> 0x05eb }
                                    r6.append(r7);	 Catch:{ Exception -> 0x05eb }
                                    r7 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.dialogs;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.size();	 Catch:{ Exception -> 0x05eb }
                                    r6.append(r7);	 Catch:{ Exception -> 0x05eb }
                                    r6 = r6.toString();	 Catch:{ Exception -> 0x05eb }
                                    org.telegram.messenger.FileLog.d(r6);	 Catch:{ Exception -> 0x05eb }
                                    r6 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r6 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r6 = r6.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r6 = org.telegram.messenger.MessagesStorage.getInstance(r6);	 Catch:{ Exception -> 0x05eb }
                                    r6 = r6.getDatabase();	 Catch:{ Exception -> 0x05eb }
                                    r7 = "SELECT min(date) FROM dialogs WHERE date != 0 AND did >> 32 IN (0, -1)";	 Catch:{ Exception -> 0x05eb }
                                    r8 = new java.lang.Object[r3];	 Catch:{ Exception -> 0x05eb }
                                    r6 = r6.queryFinalized(r7, r8);	 Catch:{ Exception -> 0x05eb }
                                    r7 = r6.next();	 Catch:{ Exception -> 0x05eb }
                                    if (r7 == 0) goto L_0x0476;	 Catch:{ Exception -> 0x05eb }
                                    r7 = 1441062000; // 0x55e4dc70 float:3.14544279E13 double:7.119792277E-315;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r6.intValue(r3);	 Catch:{ Exception -> 0x05eb }
                                    r7 = java.lang.Math.max(r7, r8);	 Catch:{ Exception -> 0x05eb }
                                    r8 = r2;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r3;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.messages;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.size();	 Catch:{ Exception -> 0x05eb }
                                    if (r2 >= r10) goto L_0x0398;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.messages;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.get(r2);	 Catch:{ Exception -> 0x05eb }
                                    r10 = (org.telegram.tgnet.TLRPC.Message) r10;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r10.date;	 Catch:{ Exception -> 0x05eb }
                                    if (r11 >= r7) goto L_0x0395;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r8;	 Catch:{ Exception -> 0x05eb }
                                    r12 = -1;	 Catch:{ Exception -> 0x05eb }
                                    if (r11 == r12) goto L_0x0376;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.migrateOffsetId;	 Catch:{ Exception -> 0x05eb }
                                    r11.dialogsLoadOffsetId = r12;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.migrateOffsetDate;	 Catch:{ Exception -> 0x05eb }
                                    r11.dialogsLoadOffsetDate = r12;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.migrateOffsetUserId;	 Catch:{ Exception -> 0x05eb }
                                    r11.dialogsLoadOffsetUserId = r12;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.migrateOffsetChatId;	 Catch:{ Exception -> 0x05eb }
                                    r11.dialogsLoadOffsetChatId = r12;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.migrateOffsetChannelId;	 Catch:{ Exception -> 0x05eb }
                                    r11.dialogsLoadOffsetChannelId = r12;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.UserConfig.getInstance(r11);	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.UserConfig.getInstance(r12);	 Catch:{ Exception -> 0x05eb }
                                    r14 = r12.migrateOffsetAccess;	 Catch:{ Exception -> 0x05eb }
                                    r11.dialogsLoadOffsetAccess = r14;	 Catch:{ Exception -> 0x05eb }
                                    r8 = -1;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x05eb }
                                    if (r11 == 0) goto L_0x0376;	 Catch:{ Exception -> 0x05eb }
                                    r11 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x05eb }
                                    r11.<init>();	 Catch:{ Exception -> 0x05eb }
                                    r12 = "migrate stop due to reached loaded dialogs ";	 Catch:{ Exception -> 0x05eb }
                                    r11.append(r12);	 Catch:{ Exception -> 0x05eb }
                                    r12 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.formatterStats;	 Catch:{ Exception -> 0x05eb }
                                    r14 = (long) r7;	 Catch:{ Exception -> 0x05eb }
                                    r16 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Exception -> 0x05eb }
                                    r14 = r14 * r16;	 Catch:{ Exception -> 0x05eb }
                                    r12 = r12.format(r14);	 Catch:{ Exception -> 0x05eb }
                                    r11.append(r12);	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.toString();	 Catch:{ Exception -> 0x05eb }
                                    org.telegram.messenger.FileLog.d(r11);	 Catch:{ Exception -> 0x05eb }
                                    r11 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11.messages;	 Catch:{ Exception -> 0x05eb }
                                    r11.remove(r2);	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2 + -1;	 Catch:{ Exception -> 0x05eb }
                                    r11 = org.telegram.messenger.MessageObject.getDialogId(r10);	 Catch:{ Exception -> 0x05eb }
                                    r14 = r9.get(r11);	 Catch:{ Exception -> 0x05eb }
                                    r14 = (org.telegram.tgnet.TLRPC.TL_dialog) r14;	 Catch:{ Exception -> 0x05eb }
                                    r9.remove(r11);	 Catch:{ Exception -> 0x05eb }
                                    if (r14 == 0) goto L_0x0395;	 Catch:{ Exception -> 0x05eb }
                                    r15 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r15 = r15.dialogs;	 Catch:{ Exception -> 0x05eb }
                                    r15.remove(r14);	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2 + r13;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x0287;	 Catch:{ Exception -> 0x05eb }
                                    if (r4 == 0) goto L_0x0475;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r4.date;	 Catch:{ Exception -> 0x05eb }
                                    if (r2 >= r7) goto L_0x0475;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r8;	 Catch:{ Exception -> 0x05eb }
                                    r10 = -1;	 Catch:{ Exception -> 0x05eb }
                                    if (r2 == r10) goto L_0x0475;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.migrateOffsetId;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetId = r10;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.migrateOffsetDate;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetDate = r10;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.migrateOffsetUserId;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetUserId = r10;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.migrateOffsetChatId;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetChatId = r10;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.migrateOffsetChannelId;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetChannelId = r10;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r2.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r2 = org.telegram.messenger.UserConfig.getInstance(r2);	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.migrateOffsetAccess;	 Catch:{ Exception -> 0x05eb }
                                    r2.dialogsLoadOffsetAccess = r10;	 Catch:{ Exception -> 0x05eb }
                                    r2 = -1;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x05eb }
                                    if (r8 == 0) goto L_0x0476;	 Catch:{ Exception -> 0x05eb }
                                    r8 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x05eb }
                                    r8.<init>();	 Catch:{ Exception -> 0x05eb }
                                    r10 = "migrate stop due to reached loaded dialogs ";	 Catch:{ Exception -> 0x05eb }
                                    r8.append(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.LocaleController.getInstance();	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.formatterStats;	 Catch:{ Exception -> 0x05eb }
                                    r11 = (long) r7;	 Catch:{ Exception -> 0x05eb }
                                    r13 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r11 * r13;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.format(r11);	 Catch:{ Exception -> 0x05eb }
                                    r8.append(r10);	 Catch:{ Exception -> 0x05eb }
                                    r8 = r8.toString();	 Catch:{ Exception -> 0x05eb }
                                    org.telegram.messenger.FileLog.d(r8);	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x0476;	 Catch:{ Exception -> 0x05eb }
                                    r2 = r8;	 Catch:{ Exception -> 0x05eb }
                                    r6.dispose();	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r8 = r4.date;	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetDate = r8;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r4.to_id;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.channel_id;	 Catch:{ Exception -> 0x05eb }
                                    if (r7 == 0) goto L_0x04f9;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r8 = r4.to_id;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r8.channel_id;	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetChannelId = r8;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetChatId = r3;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetUserId = r3;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.chats;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.size();	 Catch:{ Exception -> 0x05eb }
                                    if (r3 >= r7) goto L_0x05d7;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.chats;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.get(r3);	 Catch:{ Exception -> 0x05eb }
                                    r7 = (org.telegram.tgnet.TLRPC.Chat) r7;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r7.id;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.migrateOffsetChannelId;	 Catch:{ Exception -> 0x05eb }
                                    if (r8 != r10) goto L_0x04f6;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r8.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.UserConfig.getInstance(r8);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r7.access_hash;	 Catch:{ Exception -> 0x05eb }
                                    r8.migrateOffsetAccess = r10;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x05d7;	 Catch:{ Exception -> 0x05eb }
                                    r3 = r3 + 1;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x04be;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r4.to_id;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.chat_id;	 Catch:{ Exception -> 0x05eb }
                                    if (r7 == 0) goto L_0x0568;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r8 = r4.to_id;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r8.chat_id;	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetChatId = r8;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetChannelId = r3;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetUserId = r3;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.chats;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.size();	 Catch:{ Exception -> 0x05eb }
                                    if (r3 >= r7) goto L_0x05d7;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.chats;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.get(r3);	 Catch:{ Exception -> 0x05eb }
                                    r7 = (org.telegram.tgnet.TLRPC.Chat) r7;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r7.id;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.migrateOffsetChatId;	 Catch:{ Exception -> 0x05eb }
                                    if (r8 != r10) goto L_0x0565;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r8.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.UserConfig.getInstance(r8);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r7.access_hash;	 Catch:{ Exception -> 0x05eb }
                                    r8.migrateOffsetAccess = r10;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x05d7;	 Catch:{ Exception -> 0x05eb }
                                    r3 = r3 + 1;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x052e;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r4.to_id;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.user_id;	 Catch:{ Exception -> 0x05eb }
                                    if (r7 == 0) goto L_0x05d7;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r8 = r4.to_id;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r8.user_id;	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetUserId = r8;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetChatId = r3;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r7 = org.telegram.messenger.UserConfig.getInstance(r7);	 Catch:{ Exception -> 0x05eb }
                                    r7.migrateOffsetChannelId = r3;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.users;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.size();	 Catch:{ Exception -> 0x05eb }
                                    if (r3 >= r7) goto L_0x05d7;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.users;	 Catch:{ Exception -> 0x05eb }
                                    r7 = r7.get(r3);	 Catch:{ Exception -> 0x05eb }
                                    r7 = (org.telegram.tgnet.TLRPC.User) r7;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r7.id;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.UserConfig.getInstance(r10);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r10.migrateOffsetUserId;	 Catch:{ Exception -> 0x05eb }
                                    if (r8 != r10) goto L_0x05d4;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r8 = r8.currentAccount;	 Catch:{ Exception -> 0x05eb }
                                    r8 = org.telegram.messenger.UserConfig.getInstance(r8);	 Catch:{ Exception -> 0x05eb }
                                    r10 = r7.access_hash;	 Catch:{ Exception -> 0x05eb }
                                    r8.migrateOffsetAccess = r10;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x05d7;	 Catch:{ Exception -> 0x05eb }
                                    r3 = r3 + 1;	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x059d;	 Catch:{ Exception -> 0x05eb }
                                    r3 = org.telegram.messenger.MessagesController.AnonymousClass68.this;	 Catch:{ Exception -> 0x05eb }
                                    r10 = org.telegram.messenger.MessagesController.this;	 Catch:{ Exception -> 0x05eb }
                                    r11 = r0;	 Catch:{ Exception -> 0x05eb }
                                    r12 = 0;	 Catch:{ Exception -> 0x05eb }
                                    r14 = 0;	 Catch:{ Exception -> 0x05eb }
                                    r15 = 0;	 Catch:{ Exception -> 0x05eb }
                                    r16 = 0;	 Catch:{ Exception -> 0x05eb }
                                    r17 = 1;	 Catch:{ Exception -> 0x05eb }
                                    r18 = 0;	 Catch:{ Exception -> 0x05eb }
                                    r13 = r2;	 Catch:{ Exception -> 0x05eb }
                                    r10.processLoadedDialogs(r11, r12, r13, r14, r15, r16, r17, r18);	 Catch:{ Exception -> 0x05eb }
                                    goto L_0x05f8;
                                L_0x05eb:
                                    r0 = move-exception;
                                    r2 = r0;
                                    org.telegram.messenger.FileLog.e(r2);
                                    r3 = new org.telegram.messenger.MessagesController$68$1$1;
                                    r3.<init>();
                                    org.telegram.messenger.AndroidUtilities.runOnUIThread(r3);
                                    return;
                                    */
                                    throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.68.1.run():void");
                                }
                            });
                            return;
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                MessagesController.this.migratingDialogs = false;
                            }
                        });
                    }
                });
            }
        }
    }

    public void processLoadedDialogs(messages_Dialogs dialogsRes, ArrayList<EncryptedChat> encChats, int offset, int count, int loadType, boolean resetEnd, boolean migrate, boolean fromCache) {
        final int i = loadType;
        final messages_Dialogs org_telegram_tgnet_TLRPC_messages_Dialogs = dialogsRes;
        final boolean z = resetEnd;
        final int i2 = count;
        final int i3 = offset;
        final boolean z2 = fromCache;
        final boolean z3 = migrate;
        final ArrayList<EncryptedChat> arrayList = encChats;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                boolean z = true;
                boolean z2 = false;
                if (!MessagesController.this.firstGettingTask) {
                    MessagesController.this.getNewDeleteTask(null, 0);
                    MessagesController.this.firstGettingTask = true;
                }
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("loaded loadType ");
                    stringBuilder.append(i);
                    stringBuilder.append(" count ");
                    stringBuilder.append(org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size());
                    FileLog.d(stringBuilder.toString());
                }
                if (i == 1 && org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size() == 0) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            MessagesController.this.putUsers(org_telegram_tgnet_TLRPC_messages_Dialogs.users, true);
                            MessagesController.this.loadingDialogs = false;
                            if (z) {
                                MessagesController.this.dialogsEndReached = false;
                                MessagesController.this.serverDialogsEndReached = false;
                            } else if (UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId == ConnectionsManager.DEFAULT_DATACENTER_ID) {
                                MessagesController.this.dialogsEndReached = true;
                                MessagesController.this.serverDialogsEndReached = true;
                            } else {
                                MessagesController.this.loadDialogs(0, i2, false);
                            }
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                        }
                    });
                    return;
                }
                int a;
                int i;
                LongSparseArray<TL_dialog> new_dialogs_dict = new LongSparseArray();
                LongSparseArray<MessageObject> new_dialogMessage = new LongSparseArray();
                SparseArray<User> usersDict = new SparseArray();
                SparseArray<Chat> chatsDict = new SparseArray();
                for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.users.size(); a++) {
                    User u = (User) org_telegram_tgnet_TLRPC_messages_Dialogs.users.get(a);
                    usersDict.put(u.id, u);
                }
                for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.chats.size(); a++) {
                    Chat c = (Chat) org_telegram_tgnet_TLRPC_messages_Dialogs.chats.get(a);
                    chatsDict.put(c.id, c);
                }
                if (i == 1) {
                    MessagesController.this.nextDialogsCacheOffset = i3 + i2;
                }
                Message lastMessage = null;
                a = 0;
                while (a < org_telegram_tgnet_TLRPC_messages_Dialogs.messages.size()) {
                    Message lastMessage2;
                    Chat chat;
                    MessageObject messageObject;
                    Message message = (Message) org_telegram_tgnet_TLRPC_messages_Dialogs.messages.get(a);
                    if (lastMessage != null) {
                        if (message.date >= lastMessage.date) {
                            lastMessage2 = lastMessage;
                            if (message.to_id.channel_id != 0) {
                                chat = (Chat) chatsDict.get(message.to_id.channel_id);
                                if (chat == null && chat.left) {
                                    a++;
                                    lastMessage = lastMessage2;
                                } else if (chat != null && chat.megagroup) {
                                    message.flags |= Integer.MIN_VALUE;
                                }
                            } else if (message.to_id.chat_id != 0) {
                                chat = (Chat) chatsDict.get(message.to_id.chat_id);
                                if (!(chat == null || chat.migrated_to == null)) {
                                    a++;
                                    lastMessage = lastMessage2;
                                }
                            }
                            messageObject = new MessageObject(MessagesController.this.currentAccount, message, (SparseArray) usersDict, (SparseArray) chatsDict, false);
                            new_dialogMessage.put(messageObject.getDialogId(), messageObject);
                            a++;
                            lastMessage = lastMessage2;
                        }
                    }
                    lastMessage2 = message;
                    if (message.to_id.channel_id != 0) {
                        chat = (Chat) chatsDict.get(message.to_id.channel_id);
                        if (chat == null) {
                        }
                        message.flags |= Integer.MIN_VALUE;
                    } else if (message.to_id.chat_id != 0) {
                        chat = (Chat) chatsDict.get(message.to_id.chat_id);
                        a++;
                        lastMessage = lastMessage2;
                    }
                    messageObject = new MessageObject(MessagesController.this.currentAccount, message, (SparseArray) usersDict, (SparseArray) chatsDict, false);
                    new_dialogMessage.put(messageObject.getDialogId(), messageObject);
                    a++;
                    lastMessage = lastMessage2;
                }
                if (!(z2 || z3 || UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId == -1 || i != 0)) {
                    if (lastMessage == null || lastMessage.id == UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId) {
                        UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    } else {
                        UserConfig instance = UserConfig.getInstance(MessagesController.this.currentAccount);
                        instance.totalDialogsLoadCount += org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size();
                        UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId = lastMessage.id;
                        UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetDate = lastMessage.date;
                        if (lastMessage.to_id.channel_id != 0) {
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId = lastMessage.to_id.channel_id;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId = 0;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId = 0;
                            for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.chats.size(); a++) {
                                c = (Chat) org_telegram_tgnet_TLRPC_messages_Dialogs.chats.get(a);
                                if (c.id == UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId) {
                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetAccess = c.access_hash;
                                    break;
                                }
                            }
                        } else if (lastMessage.to_id.chat_id != 0) {
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId = lastMessage.to_id.chat_id;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId = 0;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId = 0;
                            for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.chats.size(); a++) {
                                c = (Chat) org_telegram_tgnet_TLRPC_messages_Dialogs.chats.get(a);
                                if (c.id == UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId) {
                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetAccess = c.access_hash;
                                    break;
                                }
                            }
                        } else if (lastMessage.to_id.user_id != 0) {
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId = lastMessage.to_id.user_id;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId = 0;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId = 0;
                            for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.users.size(); a++) {
                                u = (User) org_telegram_tgnet_TLRPC_messages_Dialogs.users.get(a);
                                if (u.id == UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId) {
                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetAccess = u.access_hash;
                                    break;
                                }
                            }
                        }
                    }
                    UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
                }
                ArrayList<TL_dialog> dialogsToReload = new ArrayList();
                a = 0;
                while (a < org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size()) {
                    Integer value;
                    TL_dialog d = (TL_dialog) org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.get(a);
                    if (d.id == 0 && d.peer != null) {
                        if (d.peer.user_id != 0) {
                            d.id = (long) d.peer.user_id;
                        } else if (d.peer.chat_id != 0) {
                            d.id = (long) (-d.peer.chat_id);
                        } else if (d.peer.channel_id != 0) {
                            d.id = (long) (-d.peer.channel_id);
                        }
                    }
                    if (d.id != 0) {
                        if (d.last_message_date == 0) {
                            MessageObject mess = (MessageObject) new_dialogMessage.get(d.id);
                            if (mess != null) {
                                d.last_message_date = mess.messageOwner.date;
                            }
                        }
                        boolean allowCheck = true;
                        Chat chat2;
                        if (DialogObject.isChannel(d)) {
                            chat2 = (Chat) chatsDict.get(-((int) d.id));
                            if (chat2 != null) {
                                if (!chat2.megagroup) {
                                    allowCheck = false;
                                }
                                if (chat2.left) {
                                }
                            }
                            MessagesController.this.channelsPts.put(-((int) d.id), d.pts);
                        } else if (((int) d.id) < 0) {
                            chat2 = (Chat) chatsDict.get(-((int) d.id));
                            if (!(chat2 == null || chat2.migrated_to == null)) {
                            }
                        }
                        new_dialogs_dict.put(d.id, d);
                        if (allowCheck && i == r1 && ((d.read_outbox_max_id == 0 || d.read_inbox_max_id == 0) && d.top_message != 0)) {
                            dialogsToReload.add(d);
                        }
                        value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(d.id));
                        if (value == null) {
                            value = Integer.valueOf(0);
                        }
                        MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_inbox_max_id)));
                        Integer value2 = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(d.id));
                        if (value2 == null) {
                            value2 = Integer.valueOf(0);
                        }
                        MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value2.intValue(), d.read_outbox_max_id)));
                    }
                    a++;
                    z = true;
                }
                if (i != 1) {
                    ImageLoader.saveMessagesThumbs(org_telegram_tgnet_TLRPC_messages_Dialogs.messages);
                    a = 0;
                    while (a < org_telegram_tgnet_TLRPC_messages_Dialogs.messages.size()) {
                        message = (Message) org_telegram_tgnet_TLRPC_messages_Dialogs.messages.get(a);
                        if (message.action instanceof TL_messageActionChatDeleteUser) {
                            User user = (User) usersDict.get(message.action.user_id);
                            if (user != null && user.bot) {
                                message.reply_markup = new TL_replyKeyboardHide();
                                message.flags |= 64;
                            }
                        }
                        if (!(message.action instanceof TL_messageActionChatMigrateTo)) {
                            if (!(message.action instanceof TL_messageActionChannelCreate)) {
                                ConcurrentHashMap<Long, Integer> read_max = message.out ? MessagesController.this.dialogs_read_outbox_max : MessagesController.this.dialogs_read_inbox_max;
                                value = (Integer) read_max.get(Long.valueOf(message.dialog_id));
                                if (value == null) {
                                    value = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                                    read_max.put(Long.valueOf(message.dialog_id), value);
                                }
                                message.unread = value.intValue() < message.id;
                                z = false;
                                a++;
                                z2 = z;
                            }
                        }
                        z = false;
                        message.unread = false;
                        message.media_unread = false;
                        a++;
                        z2 = z;
                    }
                    i = z2;
                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putDialogs(org_telegram_tgnet_TLRPC_messages_Dialogs, i);
                } else {
                    i = 0;
                }
                if (i == 2) {
                    Chat chat3 = (Chat) org_telegram_tgnet_TLRPC_messages_Dialogs.chats.get(i);
                    MessagesController.this.getChannelDifference(chat3.id);
                    MessagesController.this.checkChannelInviter(chat3.id);
                }
                final LongSparseArray<TL_dialog> longSparseArray = new_dialogs_dict;
                final LongSparseArray<MessageObject> longSparseArray2 = new_dialogMessage;
                final SparseArray<Chat> sparseArray = chatsDict;
                final ArrayList<TL_dialog> chatsDict2 = dialogsToReload;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.69.2.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                        r0 = r21;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r3;
                        r2 = 1;
                        if (r1 == r2) goto L_0x0035;
                    L_0x0009:
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r4;
                        r3 = r3.dialogs;
                        r1.applyDialogsNotificationsSettings(r3);
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r1 = r1.draftsLoaded;
                        if (r1 != 0) goto L_0x0035;
                    L_0x0026:
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.DataQuery.getInstance(r1);
                        r1.loadDrafts();
                    L_0x0035:
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r4;
                        r3 = r3.users;
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = r3;
                        r5 = 0;
                        if (r4 != r2) goto L_0x0048;
                    L_0x0046:
                        r4 = r2;
                        goto L_0x0049;
                    L_0x0048:
                        r4 = r5;
                    L_0x0049:
                        r1.putUsers(r3, r4);
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r4;
                        r3 = r3.chats;
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = r3;
                        if (r4 != r2) goto L_0x005e;
                    L_0x005c:
                        r4 = r2;
                        goto L_0x005f;
                    L_0x005e:
                        r4 = r5;
                    L_0x005f:
                        r1.putChats(r3, r4);
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r10;
                        r3 = 0;
                        if (r1 == 0) goto L_0x00a5;
                    L_0x0069:
                        r1 = r5;
                    L_0x006a:
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = r10;
                        r4 = r4.size();
                        if (r1 >= r4) goto L_0x00a5;
                    L_0x0074:
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = r10;
                        r4 = r4.get(r1);
                        r4 = (org.telegram.tgnet.TLRPC.EncryptedChat) r4;
                        r6 = r4 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChat;
                        if (r6 == 0) goto L_0x009b;
                    L_0x0082:
                        r6 = r4.layer;
                        r6 = org.telegram.messenger.AndroidUtilities.getMyLayerVersion(r6);
                        r7 = 73;
                        if (r6 >= r7) goto L_0x009b;
                    L_0x008c:
                        r6 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r6 = org.telegram.messenger.MessagesController.this;
                        r6 = r6.currentAccount;
                        r6 = org.telegram.messenger.SecretChatHelper.getInstance(r6);
                        r6.sendNotifyLayerMessage(r4, r3);
                    L_0x009b:
                        r6 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r6 = org.telegram.messenger.MessagesController.this;
                        r6.putEncryptedChat(r4, r2);
                        r1 = r1 + 1;
                        goto L_0x006a;
                    L_0x00a5:
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r9;
                        if (r1 != 0) goto L_0x00b1;
                    L_0x00ab:
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1.loadingDialogs = r5;
                    L_0x00b1:
                        r1 = 0;
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = r9;
                        if (r4 == 0) goto L_0x00de;
                    L_0x00b8:
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = org.telegram.messenger.MessagesController.this;
                        r4 = r4.dialogs;
                        r4 = r4.isEmpty();
                        if (r4 != 0) goto L_0x00de;
                    L_0x00c4:
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = org.telegram.messenger.MessagesController.this;
                        r4 = r4.dialogs;
                        r6 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r6 = org.telegram.messenger.MessagesController.this;
                        r6 = r6.dialogs;
                        r6 = r6.size();
                        r6 = r6 - r2;
                        r4 = r4.get(r6);
                        r4 = (org.telegram.tgnet.TLRPC.TL_dialog) r4;
                        r4 = r4.last_message_date;
                        goto L_0x00df;
                    L_0x00de:
                        r4 = r5;
                    L_0x00df:
                        r6 = r1;
                        r1 = r5;
                        r7 = r2;
                        r7 = r7.size();
                        r8 = 0;
                        if (r1 >= r7) goto L_0x02b5;
                    L_0x00eb:
                        r7 = r2;
                        r10 = r7.keyAt(r1);
                        r7 = r2;
                        r7 = r7.valueAt(r1);
                        r7 = (org.telegram.tgnet.TLRPC.TL_dialog) r7;
                        r12 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r12 = r9;
                        if (r12 == 0) goto L_0x010a;
                    L_0x00ff:
                        if (r4 == 0) goto L_0x010a;
                    L_0x0101:
                        r12 = r7.last_message_date;
                        if (r12 >= r4) goto L_0x010a;
                    L_0x0106:
                        r20 = r4;
                        goto L_0x02ae;
                    L_0x010a:
                        r12 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r12 = org.telegram.messenger.MessagesController.this;
                        r12 = r12.dialogs_dict;
                        r12 = r12.get(r10);
                        r12 = (org.telegram.tgnet.TLRPC.TL_dialog) r12;
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = r3;
                        if (r13 == r2) goto L_0x013f;
                    L_0x011c:
                        r13 = r7.draft;
                        r13 = r13 instanceof org.telegram.tgnet.TLRPC.TL_draftMessage;
                        if (r13 == 0) goto L_0x013f;
                    L_0x0122:
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.currentAccount;
                        r14 = org.telegram.messenger.DataQuery.getInstance(r13);
                        r20 = r4;
                        r3 = r7.id;
                        r13 = r7.draft;
                        r18 = 0;
                        r19 = 0;
                        r15 = r3;
                        r17 = r13;
                        r14.saveDraft(r15, r17, r18, r19);
                        goto L_0x0141;
                    L_0x013f:
                        r20 = r4;
                    L_0x0141:
                        if (r12 != 0) goto L_0x0190;
                    L_0x0143:
                        r3 = 1;
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = org.telegram.messenger.MessagesController.this;
                        r4 = r4.dialogs_dict;
                        r4.put(r10, r7);
                        r4 = r3;
                        r13 = r7.id;
                        r4 = r4.get(r13);
                        r4 = (org.telegram.messenger.MessageObject) r4;
                        r6 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r6 = org.telegram.messenger.MessagesController.this;
                        r6 = r6.dialogMessage;
                        r6.put(r10, r4);
                        if (r4 == 0) goto L_0x018c;
                    L_0x0162:
                        r6 = r4.messageOwner;
                        r6 = r6.to_id;
                        r6 = r6.channel_id;
                        if (r6 != 0) goto L_0x018c;
                        r6 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r6 = org.telegram.messenger.MessagesController.this;
                        r6 = r6.dialogMessagesByIds;
                        r13 = r4.getId();
                        r6.put(r13, r4);
                        r6 = r4.messageOwner;
                        r13 = r6.random_id;
                        r6 = (r13 > r8 ? 1 : (r13 == r8 ? 0 : -1));
                        if (r6 == 0) goto L_0x018c;
                        r6 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r6 = org.telegram.messenger.MessagesController.this;
                        r6 = r6.dialogMessagesByRandomIds;
                        r8 = r4.messageOwner;
                        r8 = r8.random_id;
                        r6.put(r8, r4);
                        r6 = r3;
                        goto L_0x02ae;
                    L_0x0190:
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r3;
                        if (r3 == r2) goto L_0x019a;
                        r3 = r7.notify_settings;
                        r12.notify_settings = r3;
                        r3 = r7.pinned;
                        r12.pinned = r3;
                        r3 = r7.pinnedNum;
                        r12.pinnedNum = r3;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = org.telegram.messenger.MessagesController.this;
                        r3 = r3.dialogMessage;
                        r3 = r3.get(r10);
                        r3 = (org.telegram.messenger.MessageObject) r3;
                        if (r3 == 0) goto L_0x01b4;
                        r4 = r3.deleted;
                        if (r4 != 0) goto L_0x0239;
                        if (r3 == 0) goto L_0x0239;
                        r4 = r12.top_message;
                        if (r4 <= 0) goto L_0x01bc;
                        goto L_0x0239;
                        r4 = r3;
                        r13 = r7.id;
                        r4 = r4.get(r13);
                        r4 = (org.telegram.messenger.MessageObject) r4;
                        r13 = r3.deleted;
                        if (r13 != 0) goto L_0x01d6;
                        if (r4 == 0) goto L_0x01d6;
                        r13 = r4.messageOwner;
                        r13 = r13.date;
                        r14 = r3.messageOwner;
                        r14 = r14.date;
                        if (r13 <= r14) goto L_0x02ae;
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.dialogs_dict;
                        r13.put(r10, r7);
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.dialogMessage;
                        r13.put(r10, r4);
                        if (r4 == 0) goto L_0x0216;
                        r13 = r4.messageOwner;
                        r13 = r13.to_id;
                        r13 = r13.channel_id;
                        if (r13 != 0) goto L_0x0216;
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.dialogMessagesByIds;
                        r14 = r4.getId();
                        r13.put(r14, r4);
                        if (r4 == 0) goto L_0x0216;
                        r13 = r4.messageOwner;
                        r13 = r13.random_id;
                        r15 = (r13 > r8 ? 1 : (r13 == r8 ? 0 : -1));
                        if (r15 == 0) goto L_0x0216;
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.dialogMessagesByRandomIds;
                        r14 = r4.messageOwner;
                        r14 = r14.random_id;
                        r13.put(r14, r4);
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.dialogMessagesByIds;
                        r14 = r3.getId();
                        r13.remove(r14);
                        r13 = r3.messageOwner;
                        r13 = r13.random_id;
                        r15 = (r13 > r8 ? 1 : (r13 == r8 ? 0 : -1));
                        if (r15 == 0) goto L_0x02ae;
                        r8 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r8 = org.telegram.messenger.MessagesController.this;
                        r8 = r8.dialogMessagesByRandomIds;
                        r9 = r3.messageOwner;
                        r13 = r9.random_id;
                        r8.remove(r13);
                        goto L_0x02ae;
                        r4 = r7.top_message;
                        r13 = r12.top_message;
                        if (r4 < r13) goto L_0x02ae;
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = org.telegram.messenger.MessagesController.this;
                        r4 = r4.dialogs_dict;
                        r4.put(r10, r7);
                        r4 = r3;
                        r13 = r7.id;
                        r4 = r4.get(r13);
                        r4 = (org.telegram.messenger.MessageObject) r4;
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.dialogMessage;
                        r13.put(r10, r4);
                        if (r4 == 0) goto L_0x0289;
                        r13 = r4.messageOwner;
                        r13 = r13.to_id;
                        r13 = r13.channel_id;
                        if (r13 != 0) goto L_0x0289;
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.dialogMessagesByIds;
                        r14 = r4.getId();
                        r13.put(r14, r4);
                        if (r4 == 0) goto L_0x0289;
                        r13 = r4.messageOwner;
                        r13 = r13.random_id;
                        r15 = (r13 > r8 ? 1 : (r13 == r8 ? 0 : -1));
                        if (r15 == 0) goto L_0x0289;
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.dialogMessagesByRandomIds;
                        r14 = r4.messageOwner;
                        r14 = r14.random_id;
                        r13.put(r14, r4);
                        if (r3 == 0) goto L_0x02ad;
                        r13 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r13 = org.telegram.messenger.MessagesController.this;
                        r13 = r13.dialogMessagesByIds;
                        r14 = r3.getId();
                        r13.remove(r14);
                        r13 = r3.messageOwner;
                        r13 = r13.random_id;
                        r15 = (r13 > r8 ? 1 : (r13 == r8 ? 0 : -1));
                        if (r15 == 0) goto L_0x02ad;
                        r8 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r8 = org.telegram.messenger.MessagesController.this;
                        r8 = r8.dialogMessagesByRandomIds;
                        r9 = r3.messageOwner;
                        r13 = r9.random_id;
                        r8.remove(r13);
                    L_0x02ae:
                        r1 = r1 + 1;
                        r4 = r20;
                        r3 = 0;
                        goto L_0x00e1;
                    L_0x02b5:
                        r20 = r4;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.dialogs;
                        r1.clear();
                        r1 = 0;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = org.telegram.messenger.MessagesController.this;
                        r3 = r3.dialogs_dict;
                        r3 = r3.size();
                        if (r1 >= r3) goto L_0x02e3;
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = org.telegram.messenger.MessagesController.this;
                        r4 = r4.dialogs;
                        r7 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r7 = org.telegram.messenger.MessagesController.this;
                        r7 = r7.dialogs_dict;
                        r7 = r7.valueAt(r1);
                        r4.add(r7);
                        r1 = r1 + 1;
                        goto L_0x02cb;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r9;
                        if (r3 == 0) goto L_0x02f0;
                        r3 = r4;
                        goto L_0x02f1;
                        r3 = 0;
                        r1.sortDialogs(r3);
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r3;
                        r3 = 2;
                        if (r1 == r3) goto L_0x035d;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r9;
                        if (r1 != 0) goto L_0x035d;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r4;
                        r3 = r3.dialogs;
                        r3 = r3.size();
                        if (r3 == 0) goto L_0x0321;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r4;
                        r3 = r3.dialogs;
                        r3 = r3.size();
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = r6;
                        if (r3 == r4) goto L_0x0329;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r3;
                        if (r3 != 0) goto L_0x0329;
                        r3 = r2;
                        goto L_0x032a;
                        r3 = r5;
                        r1.dialogsEndReached = r3;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r8;
                        if (r1 != 0) goto L_0x035d;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r4;
                        r3 = r3.dialogs;
                        r3 = r3.size();
                        if (r3 == 0) goto L_0x0352;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r4;
                        r3 = r3.dialogs;
                        r3 = r3.size();
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r4 = r6;
                        if (r3 == r4) goto L_0x035a;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r3 = r3;
                        if (r3 != 0) goto L_0x035a;
                        r3 = r2;
                        goto L_0x035b;
                        r3 = r5;
                        r1.serverDialogsEndReached = r3;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r8;
                        if (r1 != 0) goto L_0x03a8;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r9;
                        if (r1 != 0) goto L_0x03a8;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r1 = r1.totalDialogsLoadCount;
                        r3 = 400; // 0x190 float:5.6E-43 double:1.976E-321;
                        if (r1 >= r3) goto L_0x03a8;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r1 = r1.dialogsLoadOffsetId;
                        r3 = -1;
                        if (r1 == r3) goto L_0x03a8;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r1 = r1.dialogsLoadOffsetId;
                        r3 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
                        if (r1 == r3) goto L_0x03a8;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r3 = 100;
                        r1.loadDialogs(r5, r3, r5);
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
                        r3 = org.telegram.messenger.NotificationCenter.dialogsNeedReload;
                        r4 = new java.lang.Object[r5];
                        r1.postNotificationName(r3, r4);
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r9;
                        if (r1 == 0) goto L_0x03fd;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r2 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r2 = r7;
                        r1.migrateOffsetId = r2;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r1.saveConfig(r5);
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1.migratingDialogs = r5;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
                        r2 = org.telegram.messenger.NotificationCenter.needReloadRecentDialogsSearch;
                        r3 = new java.lang.Object[r5];
                        r1.postNotificationName(r2, r3);
                        goto L_0x0417;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1.generateUpdateMessage();
                        if (r6 != 0) goto L_0x0417;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = r3;
                        if (r1 != r2) goto L_0x0417;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r2 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r2 = r6;
                        r1.loadDialogs(r5, r2, r5);
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r10 = org.telegram.messenger.MessagesController.this;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r11 = r1.migrateOffsetId;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r12 = r1.migrateOffsetDate;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r13 = r1.migrateOffsetUserId;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r14 = r1.migrateOffsetChatId;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r15 = r1.migrateOffsetChannelId;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.UserConfig.getInstance(r1);
                        r1 = r1.migrateOffsetAccess;
                        r16 = r1;
                        r10.migrateDialogs(r11, r12, r13, r14, r15, r16);
                        r1 = r5;
                        r1 = r1.isEmpty();
                        if (r1 != 0) goto L_0x0485;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass69.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r2 = r5;
                        r1.reloadDialogsReadValue(r2, r8);
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.69.2.run():void");
                    }
                });
            }
        });
    }

    private void applyDialogNotificationsSettings(long dialog_id, PeerNotifySettings notify_settings) {
        long j = dialog_id;
        PeerNotifySettings peerNotifySettings = notify_settings;
        int currentValue = this.notificationsPreferences;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("notify2_");
        stringBuilder.append(j);
        currentValue = currentValue.getInt(stringBuilder.toString(), 0);
        int currentValue2 = this.notificationsPreferences;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("notifyuntil_");
        stringBuilder2.append(j);
        currentValue2 = currentValue2.getInt(stringBuilder2.toString(), 0);
        Editor editor = this.notificationsPreferences.edit();
        boolean updated = false;
        TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(j);
        if (dialog != null) {
            dialog.notify_settings = peerNotifySettings;
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("silent_");
        stringBuilder3.append(j);
        editor.putBoolean(stringBuilder3.toString(), peerNotifySettings.silent);
        Editor editor2;
        if (peerNotifySettings.mute_until > ConnectionsManager.getInstance(r0.currentAccount).getCurrentTime()) {
            int until = 0;
            StringBuilder stringBuilder4;
            if (peerNotifySettings.mute_until <= ConnectionsManager.getInstance(r0.currentAccount).getCurrentTime() + 31536000) {
                if (!(currentValue == 3 && currentValue2 == peerNotifySettings.mute_until)) {
                    updated = true;
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("notify2_");
                    stringBuilder4.append(j);
                    editor.putInt(stringBuilder4.toString(), 3);
                    StringBuilder stringBuilder5 = new StringBuilder();
                    stringBuilder5.append("notifyuntil_");
                    stringBuilder5.append(j);
                    editor.putInt(stringBuilder5.toString(), peerNotifySettings.mute_until);
                    if (dialog != null) {
                        dialog.notify_settings.mute_until = 0;
                    }
                }
                until = peerNotifySettings.mute_until;
            } else if (currentValue != 2) {
                updated = true;
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append("notify2_");
                stringBuilder4.append(j);
                editor.putInt(stringBuilder4.toString(), 2);
                if (dialog != null) {
                    dialog.notify_settings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
                }
            }
            editor2 = editor;
            MessagesStorage.getInstance(r0.currentAccount).setDialogFlags(j, (((long) until) << 32) | 1);
            NotificationsController.getInstance(r0.currentAccount).removeNotificationsForDialog(j);
            editor = editor2;
        } else {
            editor2 = editor;
            if (currentValue == 0 || currentValue == 1) {
                editor = editor2;
            } else {
                updated = true;
                if (dialog != null) {
                    dialog.notify_settings.mute_until = 0;
                }
                StringBuilder stringBuilder6 = new StringBuilder();
                stringBuilder6.append("notify2_");
                stringBuilder6.append(j);
                editor = editor2;
                editor.remove(stringBuilder6.toString());
            }
            MessagesStorage.getInstance(r0.currentAccount).setDialogFlags(j, 0);
        }
        editor.commit();
        if (updated) {
            NotificationCenter.getInstance(r0.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        }
    }

    private void applyDialogsNotificationsSettings(ArrayList<TL_dialog> dialogs) {
        Editor editor = null;
        for (int a = 0; a < dialogs.size(); a++) {
            TL_dialog dialog = (TL_dialog) dialogs.get(a);
            if (dialog.peer != null && (dialog.notify_settings instanceof TL_peerNotifySettings)) {
                int dialog_id;
                StringBuilder stringBuilder;
                if (editor == null) {
                    editor = this.notificationsPreferences.edit();
                }
                if (dialog.peer.user_id != 0) {
                    dialog_id = dialog.peer.user_id;
                } else if (dialog.peer.chat_id != 0) {
                    dialog_id = -dialog.peer.chat_id;
                } else {
                    dialog_id = -dialog.peer.channel_id;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("silent_");
                    stringBuilder.append(dialog_id);
                    editor.putBoolean(stringBuilder.toString(), dialog.notify_settings.silent);
                    if (dialog.notify_settings.mute_until != 0) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("notify2_");
                        stringBuilder.append(dialog_id);
                        editor.remove(stringBuilder.toString());
                    } else if (dialog.notify_settings.mute_until <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 31536000) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("notify2_");
                        stringBuilder.append(dialog_id);
                        editor.putInt(stringBuilder.toString(), 2);
                        dialog.notify_settings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    } else {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("notify2_");
                        stringBuilder.append(dialog_id);
                        editor.putInt(stringBuilder.toString(), 3);
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("notifyuntil_");
                        stringBuilder.append(dialog_id);
                        editor.putInt(stringBuilder.toString(), dialog.notify_settings.mute_until);
                    }
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("silent_");
                stringBuilder.append(dialog_id);
                editor.putBoolean(stringBuilder.toString(), dialog.notify_settings.silent);
                if (dialog.notify_settings.mute_until != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("notify2_");
                    stringBuilder.append(dialog_id);
                    editor.remove(stringBuilder.toString());
                } else if (dialog.notify_settings.mute_until <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 31536000) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("notify2_");
                    stringBuilder.append(dialog_id);
                    editor.putInt(stringBuilder.toString(), 3);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("notifyuntil_");
                    stringBuilder.append(dialog_id);
                    editor.putInt(stringBuilder.toString(), dialog.notify_settings.mute_until);
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("notify2_");
                    stringBuilder.append(dialog_id);
                    editor.putInt(stringBuilder.toString(), 2);
                    dialog.notify_settings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
                }
            }
        }
        if (editor != null) {
            editor.commit();
        }
    }

    public void reloadMentionsCountForChannels(final ArrayList<Integer> arrayList) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                for (int a = 0; a < arrayList.size(); a++) {
                    final long dialog_id = (long) (-((Integer) arrayList.get(a)).intValue());
                    TL_messages_getUnreadMentions req = new TL_messages_getUnreadMentions();
                    req.peer = MessagesController.this.getInputPeer((int) dialog_id);
                    req.limit = 1;
                    ConnectionsManager.getInstance(MessagesController.this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(final TLObject response, TL_error error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    messages_Messages res = response;
                                    if (res != null) {
                                        int newCount;
                                        if (res.count != 0) {
                                            newCount = res.count;
                                        } else {
                                            newCount = res.messages.size();
                                        }
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).resetMentionsCount(dialog_id, newCount);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void processDialogsUpdateRead(final LongSparseArray<Integer> dialogsToUpdate, final LongSparseArray<Integer> dialogsMentionsToUpdate) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                int a;
                if (dialogsToUpdate != null) {
                    for (a = 0; a < dialogsToUpdate.size(); a++) {
                        TL_dialog currentDialog = (TL_dialog) MessagesController.this.dialogs_dict.get(dialogsToUpdate.keyAt(a));
                        if (currentDialog != null) {
                            currentDialog.unread_count = ((Integer) dialogsToUpdate.valueAt(a)).intValue();
                        }
                    }
                }
                if (dialogsMentionsToUpdate != null) {
                    for (a = 0; a < dialogsMentionsToUpdate.size(); a++) {
                        TL_dialog currentDialog2 = (TL_dialog) MessagesController.this.dialogs_dict.get(dialogsMentionsToUpdate.keyAt(a));
                        if (currentDialog2 != null) {
                            currentDialog2.unread_mentions_count = ((Integer) dialogsMentionsToUpdate.valueAt(a)).intValue();
                            if (MessagesController.this.createdDialogMainThreadIds.contains(Long.valueOf(currentDialog2.id))) {
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateMentionsCount, Long.valueOf(currentDialog2.id), Integer.valueOf(currentDialog2.unread_mentions_count));
                            }
                        }
                    }
                }
                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(256));
                if (dialogsToUpdate != null) {
                    NotificationsController.getInstance(MessagesController.this.currentAccount).processDialogsUpdateRead(dialogsToUpdate);
                }
            }
        });
    }

    protected void checkLastDialogMessage(TL_dialog dialog, InputPeer peer, long taskId) {
        int lower_id = (int) dialog.id;
        if (lower_id != 0) {
            if (this.checkingLastMessagesDialogs.indexOfKey(lower_id) < 0) {
                TL_messages_getHistory req = new TL_messages_getHistory();
                req.peer = peer == null ? getInputPeer(lower_id) : peer;
                if (req.peer != null) {
                    if (!(req.peer instanceof TL_inputPeerChannel)) {
                        long newTaskId;
                        req.limit = 1;
                        this.checkingLastMessagesDialogs.put(lower_id, true);
                        if (taskId == 0) {
                            long data = null;
                            try {
                                data = new NativeByteBuffer(48 + req.peer.getObjectSize());
                                data.writeInt32(8);
                                data.writeInt64(dialog.id);
                                data.writeInt32(dialog.top_message);
                                data.writeInt32(dialog.read_inbox_max_id);
                                data.writeInt32(dialog.read_outbox_max_id);
                                data.writeInt32(dialog.unread_count);
                                data.writeInt32(dialog.last_message_date);
                                data.writeInt32(dialog.pts);
                                data.writeInt32(dialog.flags);
                                data.writeBool(dialog.pinned);
                                data.writeInt32(dialog.pinnedNum);
                                data.writeInt32(dialog.unread_mentions_count);
                                peer.serializeToStream(data);
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                            newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                        } else {
                            newTaskId = taskId;
                        }
                        final TL_dialog tL_dialog = dialog;
                        final int i = lower_id;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                            public void run(TLObject response, TL_error error) {
                                if (response != null) {
                                    messages_Messages res = (messages_Messages) response;
                                    if (res.messages.isEmpty()) {
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                TL_dialog currentDialog = (TL_dialog) MessagesController.this.dialogs_dict.get(tL_dialog.id);
                                                if (currentDialog != null && currentDialog.top_message == 0) {
                                                    MessagesController.this.deleteDialog(tL_dialog.id, 3);
                                                }
                                            }
                                        });
                                    } else {
                                        TL_messages_dialogs dialogs = new TL_messages_dialogs();
                                        Message newMessage = (Message) res.messages.get(0);
                                        TL_dialog newDialog = new TL_dialog();
                                        newDialog.flags = tL_dialog.flags;
                                        newDialog.top_message = newMessage.id;
                                        newDialog.last_message_date = newMessage.date;
                                        newDialog.notify_settings = tL_dialog.notify_settings;
                                        newDialog.pts = tL_dialog.pts;
                                        newDialog.unread_count = tL_dialog.unread_count;
                                        newDialog.unread_mentions_count = tL_dialog.unread_mentions_count;
                                        newDialog.read_inbox_max_id = tL_dialog.read_inbox_max_id;
                                        newDialog.read_outbox_max_id = tL_dialog.read_outbox_max_id;
                                        newDialog.pinned = tL_dialog.pinned;
                                        newDialog.pinnedNum = tL_dialog.pinnedNum;
                                        long j = tL_dialog.id;
                                        newDialog.id = j;
                                        newMessage.dialog_id = j;
                                        dialogs.users.addAll(res.users);
                                        dialogs.chats.addAll(res.chats);
                                        dialogs.dialogs.add(newDialog);
                                        dialogs.messages.addAll(res.messages);
                                        dialogs.count = 1;
                                        MessagesController.this.processDialogsUpdate(dialogs, null);
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(res.messages, true, true, false, DownloadController.getInstance(MessagesController.this.currentAccount).getAutodownloadMask(), true);
                                    }
                                }
                                if (newTaskId != 0) {
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                                }
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        MessagesController.this.checkingLastMessagesDialogs.delete(i);
                                    }
                                });
                            }
                        });
                    }
                }
            }
        }
    }

    public void processDialogsUpdate(final messages_Dialogs dialogsRes, ArrayList<EncryptedChat> arrayList) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                int a;
                final LongSparseArray<TL_dialog> new_dialogs_dict = new LongSparseArray();
                final LongSparseArray<MessageObject> new_dialogMessage = new LongSparseArray();
                SparseArray<User> usersDict = new SparseArray(dialogsRes.users.size());
                SparseArray<Chat> chatsDict = new SparseArray(dialogsRes.chats.size());
                final LongSparseArray<Integer> dialogsToUpdate = new LongSparseArray();
                for (a = 0; a < dialogsRes.users.size(); a++) {
                    User u = (User) dialogsRes.users.get(a);
                    usersDict.put(u.id, u);
                }
                for (a = 0; a < dialogsRes.chats.size(); a++) {
                    Chat c = (Chat) dialogsRes.chats.get(a);
                    chatsDict.put(c.id, c);
                }
                a = 0;
                while (true) {
                    int a2 = a;
                    if (a2 >= dialogsRes.messages.size()) {
                        break;
                    }
                    Message message = (Message) dialogsRes.messages.get(a2);
                    Chat chat;
                    if (message.to_id.channel_id != 0) {
                        chat = (Chat) chatsDict.get(message.to_id.channel_id);
                        if (chat != null && chat.left) {
                            a = a2 + 1;
                        }
                    } else if (message.to_id.chat_id != 0) {
                        chat = (Chat) chatsDict.get(message.to_id.chat_id);
                        if (!(chat == null || chat.migrated_to == null)) {
                            a = a2 + 1;
                        }
                    }
                    MessageObject messageObject = new MessageObject(MessagesController.this.currentAccount, message, (SparseArray) usersDict, (SparseArray) chatsDict, false);
                    new_dialogMessage.put(messageObject.getDialogId(), messageObject);
                    a = a2 + 1;
                }
                for (a = 0; a < dialogsRes.dialogs.size(); a++) {
                    TL_dialog d = (TL_dialog) dialogsRes.dialogs.get(a);
                    if (d.id == 0) {
                        if (d.peer.user_id != 0) {
                            d.id = (long) d.peer.user_id;
                        } else if (d.peer.chat_id != 0) {
                            d.id = (long) (-d.peer.chat_id);
                        } else if (d.peer.channel_id != 0) {
                            d.id = (long) (-d.peer.channel_id);
                        }
                    }
                    Chat chat2;
                    if (DialogObject.isChannel(d)) {
                        chat2 = (Chat) chatsDict.get(-((int) d.id));
                        if (chat2 != null && chat2.left) {
                        }
                    } else if (((int) d.id) < 0) {
                        chat2 = (Chat) chatsDict.get(-((int) d.id));
                        if (!(chat2 == null || chat2.migrated_to == null)) {
                        }
                    }
                    if (d.last_message_date == 0) {
                        MessageObject mess = (MessageObject) new_dialogMessage.get(d.id);
                        if (mess != null) {
                            d.last_message_date = mess.messageOwner.date;
                        }
                    }
                    new_dialogs_dict.put(d.id, d);
                    dialogsToUpdate.put(d.id, Integer.valueOf(d.unread_count));
                    Integer value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(d.id));
                    if (value == null) {
                        value = Integer.valueOf(0);
                    }
                    MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_inbox_max_id)));
                    value = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(d.id));
                    if (value == null) {
                        value = Integer.valueOf(0);
                    }
                    MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_outbox_max_id)));
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.73.1.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                        r0 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r0 = org.telegram.messenger.MessagesController.this;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r1 = r3;
                        r1 = r1.users;
                        r2 = 1;
                        r0.putUsers(r1, r2);
                        r0 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r0 = org.telegram.messenger.MessagesController.this;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r1 = r3;
                        r1 = r1.chats;
                        r0.putChats(r1, r2);
                        r0 = 0;
                        r1 = r0;
                        r3 = r0;
                        r3 = r3.size();
                        r4 = 0;
                        if (r1 >= r3) goto L_0x01f3;
                    L_0x0026:
                        r3 = r0;
                        r5 = r3.keyAt(r1);
                        r3 = r0;
                        r3 = r3.valueAt(r1);
                        r3 = (org.telegram.tgnet.TLRPC.TL_dialog) r3;
                        r7 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r7 = org.telegram.messenger.MessagesController.this;
                        r7 = r7.dialogs_dict;
                        r7 = r7.get(r5);
                        r7 = (org.telegram.tgnet.TLRPC.TL_dialog) r7;
                        r8 = 0;
                        if (r7 != 0) goto L_0x0097;
                    L_0x0044:
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r4 = org.telegram.messenger.MessagesController.this;
                        r10 = r4.nextDialogsCacheOffset;
                        r10 = r10 + r2;
                        r4.nextDialogsCacheOffset = r10;
                        r4 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r4 = org.telegram.messenger.MessagesController.this;
                        r4 = r4.dialogs_dict;
                        r4.put(r5, r3);
                        r4 = r1;
                        r10 = r3.id;
                        r4 = r4.get(r10);
                        r4 = (org.telegram.messenger.MessageObject) r4;
                        r10 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r10 = org.telegram.messenger.MessagesController.this;
                        r10 = r10.dialogMessage;
                        r10.put(r5, r4);
                        if (r4 == 0) goto L_0x0095;
                    L_0x006b:
                        r10 = r4.messageOwner;
                        r10 = r10.to_id;
                        r10 = r10.channel_id;
                        if (r10 != 0) goto L_0x0095;
                    L_0x0073:
                        r10 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r10 = org.telegram.messenger.MessagesController.this;
                        r10 = r10.dialogMessagesByIds;
                        r11 = r4.getId();
                        r10.put(r11, r4);
                        r10 = r4.messageOwner;
                        r10 = r10.random_id;
                        r12 = (r10 > r8 ? 1 : (r10 == r8 ? 0 : -1));
                        if (r12 == 0) goto L_0x0095;
                    L_0x0088:
                        r8 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r8 = org.telegram.messenger.MessagesController.this;
                        r8 = r8.dialogMessagesByRandomIds;
                        r9 = r4.messageOwner;
                        r9 = r9.random_id;
                        r8.put(r9, r4);
                    L_0x0095:
                        goto L_0x01ef;
                    L_0x0097:
                        r10 = r3.unread_count;
                        r7.unread_count = r10;
                        r10 = r7.unread_mentions_count;
                        r11 = r3.unread_mentions_count;
                        if (r10 == r11) goto L_0x00dd;
                    L_0x00a1:
                        r10 = r3.unread_mentions_count;
                        r7.unread_mentions_count = r10;
                        r10 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r10 = org.telegram.messenger.MessagesController.this;
                        r10 = r10.createdDialogMainThreadIds;
                        r11 = r7.id;
                        r11 = java.lang.Long.valueOf(r11);
                        r10 = r10.contains(r11);
                        if (r10 == 0) goto L_0x00dd;
                    L_0x00b9:
                        r10 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r10 = org.telegram.messenger.MessagesController.this;
                        r10 = r10.currentAccount;
                        r10 = org.telegram.messenger.NotificationCenter.getInstance(r10);
                        r11 = org.telegram.messenger.NotificationCenter.updateMentionsCount;
                        r12 = 2;
                        r12 = new java.lang.Object[r12];
                        r13 = r7.id;
                        r13 = java.lang.Long.valueOf(r13);
                        r12[r0] = r13;
                        r13 = r7.unread_mentions_count;
                        r13 = java.lang.Integer.valueOf(r13);
                        r12[r2] = r13;
                        r10.postNotificationName(r11, r12);
                    L_0x00dd:
                        r10 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r10 = org.telegram.messenger.MessagesController.this;
                        r10 = r10.dialogMessage;
                        r10 = r10.get(r5);
                        r10 = (org.telegram.messenger.MessageObject) r10;
                        if (r10 == 0) goto L_0x016d;
                    L_0x00eb:
                        r11 = r7.top_message;
                        if (r11 <= 0) goto L_0x00f1;
                    L_0x00ef:
                        goto L_0x016d;
                    L_0x00f1:
                        r4 = r1;
                        r11 = r3.id;
                        r4 = r4.get(r11);
                        r4 = (org.telegram.messenger.MessageObject) r4;
                        r11 = r10.deleted;
                        if (r11 != 0) goto L_0x010b;
                    L_0x00ff:
                        if (r4 == 0) goto L_0x010b;
                    L_0x0101:
                        r11 = r4.messageOwner;
                        r11 = r11.date;
                        r12 = r10.messageOwner;
                        r12 = r12.date;
                        if (r11 <= r12) goto L_0x01ef;
                    L_0x010b:
                        r11 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r11 = org.telegram.messenger.MessagesController.this;
                        r11 = r11.dialogs_dict;
                        r11.put(r5, r3);
                        r11 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r11 = org.telegram.messenger.MessagesController.this;
                        r11 = r11.dialogMessage;
                        r11.put(r5, r4);
                        if (r4 == 0) goto L_0x0149;
                    L_0x011f:
                        r11 = r4.messageOwner;
                        r11 = r11.to_id;
                        r11 = r11.channel_id;
                        if (r11 != 0) goto L_0x0149;
                    L_0x0127:
                        r11 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r11 = org.telegram.messenger.MessagesController.this;
                        r11 = r11.dialogMessagesByIds;
                        r12 = r4.getId();
                        r11.put(r12, r4);
                        r11 = r4.messageOwner;
                        r11 = r11.random_id;
                        r13 = (r11 > r8 ? 1 : (r11 == r8 ? 0 : -1));
                        if (r13 == 0) goto L_0x0149;
                    L_0x013c:
                        r11 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r11 = org.telegram.messenger.MessagesController.this;
                        r11 = r11.dialogMessagesByRandomIds;
                        r12 = r4.messageOwner;
                        r12 = r12.random_id;
                        r11.put(r12, r4);
                    L_0x0149:
                        r11 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r11 = org.telegram.messenger.MessagesController.this;
                        r11 = r11.dialogMessagesByIds;
                        r12 = r10.getId();
                        r11.remove(r12);
                        r11 = r10.messageOwner;
                        r11 = r11.random_id;
                        r13 = (r11 > r8 ? 1 : (r11 == r8 ? 0 : -1));
                        if (r13 == 0) goto L_0x01ef;
                    L_0x015e:
                        r8 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r8 = org.telegram.messenger.MessagesController.this;
                        r8 = r8.dialogMessagesByRandomIds;
                        r9 = r10.messageOwner;
                        r11 = r9.random_id;
                        r8.remove(r11);
                        goto L_0x01ef;
                    L_0x016d:
                        if (r10 == 0) goto L_0x0173;
                    L_0x016f:
                        r11 = r10.deleted;
                        if (r11 != 0) goto L_0x0179;
                    L_0x0173:
                        r11 = r3.top_message;
                        r12 = r7.top_message;
                        if (r11 <= r12) goto L_0x01ef;
                    L_0x0179:
                        r11 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r11 = org.telegram.messenger.MessagesController.this;
                        r11 = r11.dialogs_dict;
                        r11.put(r5, r3);
                        r11 = r1;
                        r12 = r3.id;
                        r11 = r11.get(r12);
                        r11 = (org.telegram.messenger.MessageObject) r11;
                        r12 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r12 = org.telegram.messenger.MessagesController.this;
                        r12 = r12.dialogMessage;
                        r12.put(r5, r11);
                        if (r11 == 0) goto L_0x01c1;
                    L_0x0197:
                        r12 = r11.messageOwner;
                        r12 = r12.to_id;
                        r12 = r12.channel_id;
                        if (r12 != 0) goto L_0x01c1;
                    L_0x019f:
                        r12 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r12 = org.telegram.messenger.MessagesController.this;
                        r12 = r12.dialogMessagesByIds;
                        r13 = r11.getId();
                        r12.put(r13, r11);
                        r12 = r11.messageOwner;
                        r12 = r12.random_id;
                        r14 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1));
                        if (r14 == 0) goto L_0x01c1;
                    L_0x01b4:
                        r12 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r12 = org.telegram.messenger.MessagesController.this;
                        r12 = r12.dialogMessagesByRandomIds;
                        r13 = r11.messageOwner;
                        r13 = r13.random_id;
                        r12.put(r13, r11);
                    L_0x01c1:
                        if (r10 == 0) goto L_0x01e5;
                    L_0x01c3:
                        r12 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r12 = org.telegram.messenger.MessagesController.this;
                        r12 = r12.dialogMessagesByIds;
                        r13 = r10.getId();
                        r12.remove(r13);
                        r12 = r10.messageOwner;
                        r12 = r12.random_id;
                        r14 = (r12 > r8 ? 1 : (r12 == r8 ? 0 : -1));
                        if (r14 == 0) goto L_0x01e5;
                    L_0x01d8:
                        r12 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r12 = org.telegram.messenger.MessagesController.this;
                        r12 = r12.dialogMessagesByRandomIds;
                        r13 = r10.messageOwner;
                        r13 = r13.random_id;
                        r12.remove(r13);
                    L_0x01e5:
                        if (r11 != 0) goto L_0x01ee;
                    L_0x01e7:
                        r12 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r12 = org.telegram.messenger.MessagesController.this;
                        r12.checkLastDialogMessage(r3, r4, r8);
                    L_0x01ef:
                        r1 = r1 + 1;
                        goto L_0x001d;
                    L_0x01f3:
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.dialogs;
                        r1.clear();
                        r1 = 0;
                        r2 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r2 = org.telegram.messenger.MessagesController.this;
                        r2 = r2.dialogs_dict;
                        r2 = r2.size();
                        if (r1 >= r2) goto L_0x021f;
                        r3 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r3 = org.telegram.messenger.MessagesController.this;
                        r3 = r3.dialogs;
                        r5 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r5 = org.telegram.messenger.MessagesController.this;
                        r5 = r5.dialogs_dict;
                        r5 = r5.valueAt(r1);
                        r3.add(r5);
                        r1 = r1 + 1;
                        goto L_0x0207;
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1.sortDialogs(r4);
                        r1 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r1 = org.telegram.messenger.MessagesController.this;
                        r1 = r1.currentAccount;
                        r1 = org.telegram.messenger.NotificationCenter.getInstance(r1);
                        r2 = org.telegram.messenger.NotificationCenter.dialogsNeedReload;
                        r0 = new java.lang.Object[r0];
                        r1.postNotificationName(r2, r0);
                        r0 = org.telegram.messenger.MessagesController.AnonymousClass73.this;
                        r0 = org.telegram.messenger.MessagesController.this;
                        r0 = r0.currentAccount;
                        r0 = org.telegram.messenger.NotificationsController.getInstance(r0);
                        r1 = r10;
                        r0.processDialogsUpdateRead(r1);
                        return;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.73.1.run():void");
                    }
                });
            }
        });
    }

    public void addToViewsQueue(final Message message) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                int peer;
                ArrayList<Integer> ids;
                if (message.to_id.channel_id != 0) {
                    peer = -message.to_id.channel_id;
                } else if (message.to_id.chat_id != 0) {
                    peer = -message.to_id.chat_id;
                } else {
                    peer = message.to_id.user_id;
                    ids = (ArrayList) MessagesController.this.channelViewsToSend.get(peer);
                    if (ids == null) {
                        ids = new ArrayList();
                        MessagesController.this.channelViewsToSend.put(peer, ids);
                    }
                    if (!ids.contains(Integer.valueOf(message.id))) {
                        ids.add(Integer.valueOf(message.id));
                    }
                }
                ids = (ArrayList) MessagesController.this.channelViewsToSend.get(peer);
                if (ids == null) {
                    ids = new ArrayList();
                    MessagesController.this.channelViewsToSend.put(peer, ids);
                }
                if (!ids.contains(Integer.valueOf(message.id))) {
                    ids.add(Integer.valueOf(message.id));
                }
            }
        });
    }

    public void markMessageContentAsRead(MessageObject messageObject) {
        ArrayList<Long> arrayList = new ArrayList();
        long messageId = (long) messageObject.getId();
        if (messageObject.messageOwner.to_id.channel_id != 0) {
            messageId |= ((long) messageObject.messageOwner.to_id.channel_id) << 32;
        }
        if (messageObject.messageOwner.mentioned) {
            MessagesStorage.getInstance(this.currentAccount).markMentionMessageAsRead(messageObject.getId(), messageObject.messageOwner.to_id.channel_id, messageObject.getDialogId());
        }
        arrayList.add(Long.valueOf(messageId));
        MessagesStorage.getInstance(this.currentAccount).markMessagesContentAsRead(arrayList, 0);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, arrayList);
        if (messageObject.getId() < 0) {
            markMessageAsRead(messageObject.getDialogId(), messageObject.messageOwner.random_id, Integer.MIN_VALUE);
        } else if (messageObject.messageOwner.to_id.channel_id != 0) {
            TL_channels_readMessageContents req = new TL_channels_readMessageContents();
            req.channel = getInputChannel(messageObject.messageOwner.to_id.channel_id);
            if (req.channel != null) {
                req.id.add(Integer.valueOf(messageObject.getId()));
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                });
            }
        } else {
            TL_messages_readMessageContents req2 = new TL_messages_readMessageContents();
            req2.id.add(Integer.valueOf(messageObject.getId()));
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        TL_messages_affectedMessages res = (TL_messages_affectedMessages) response;
                        MessagesController.this.processNewDifferenceParams(-1, res.pts, -1, res.pts_count);
                    }
                }
            });
        }
    }

    public void markMentionMessageAsRead(int mid, int channelId, long did) {
        MessagesStorage.getInstance(this.currentAccount).markMentionMessageAsRead(mid, channelId, did);
        if (channelId != 0) {
            TL_channels_readMessageContents req = new TL_channels_readMessageContents();
            req.channel = getInputChannel(channelId);
            if (req.channel != null) {
                req.id.add(Integer.valueOf(mid));
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                });
            } else {
                return;
            }
        }
        TL_messages_readMessageContents req2 = new TL_messages_readMessageContents();
        req2.id.add(Integer.valueOf(mid));
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    TL_messages_affectedMessages res = (TL_messages_affectedMessages) response;
                    MessagesController.this.processNewDifferenceParams(-1, res.pts, -1, res.pts_count);
                }
            }
        });
    }

    public void markMessageAsRead(int mid, int channelId, int ttl) {
        if (mid != 0) {
            if (ttl > 0) {
                int time = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                MessagesStorage.getInstance(this.currentAccount).createTaskForMid(mid, channelId, time, time, ttl, false);
                if (channelId != 0) {
                    TL_channels_readMessageContents req = new TL_channels_readMessageContents();
                    req.channel = getInputChannel(channelId);
                    req.id.add(Integer.valueOf(mid));
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                        }
                    });
                } else {
                    TL_messages_readMessageContents req2 = new TL_messages_readMessageContents();
                    req2.id.add(Integer.valueOf(mid));
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            if (error == null) {
                                TL_messages_affectedMessages res = (TL_messages_affectedMessages) response;
                                MessagesController.this.processNewDifferenceParams(-1, res.pts, -1, res.pts_count);
                            }
                        }
                    });
                }
            }
        }
    }

    public void markMessageAsRead(long dialog_id, long random_id, int ttl) {
        MessagesController messagesController = this;
        long j = dialog_id;
        int i = ttl;
        if (!(random_id == 0 || j == 0)) {
            if (i > 0 || i == Integer.MIN_VALUE) {
                int high_id = (int) (j >> 32);
                if (((int) j) == 0) {
                    EncryptedChat chat = getEncryptedChat(Integer.valueOf(high_id));
                    if (chat != null) {
                        ArrayList<Long> random_ids = new ArrayList();
                        random_ids.add(Long.valueOf(random_id));
                        SecretChatHelper.getInstance(messagesController.currentAccount).sendMessagesReadMessage(chat, random_ids, null);
                        if (i > 0) {
                            int time = ConnectionsManager.getInstance(messagesController.currentAccount).getCurrentTime();
                            MessagesStorage.getInstance(messagesController.currentAccount).createTaskForSecretChat(chat.id, time, time, 0, random_ids);
                        }
                    }
                }
            }
        }
    }

    private void completeReadTask(ReadTask task) {
        int lower_part = (int) task.dialogId;
        int high_id = (int) (task.dialogId >> 32);
        if (lower_part != 0) {
            TLObject req;
            InputPeer inputPeer = getInputPeer(lower_part);
            if (inputPeer instanceof TL_inputPeerChannel) {
                req = new TL_channels_readHistory();
                req.channel = getInputChannel(-lower_part);
                req.max_id = task.maxId;
            } else {
                req = new TL_messages_readHistory();
                req.peer = inputPeer;
                req.max_id = task.maxId;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null && (response instanceof TL_messages_affectedMessages)) {
                        TL_messages_affectedMessages res = (TL_messages_affectedMessages) response;
                        MessagesController.this.processNewDifferenceParams(-1, res.pts, -1, res.pts_count);
                    }
                }
            });
            return;
        }
        EncryptedChat chat = getEncryptedChat(Integer.valueOf(high_id));
        if (chat.auth_key != null && chat.auth_key.length > 1 && (chat instanceof TL_encryptedChat)) {
            TL_messages_readEncryptedHistory req2 = new TL_messages_readEncryptedHistory();
            req2.peer = new TL_inputEncryptedChat();
            req2.peer.chat_id = chat.id;
            req2.peer.access_hash = chat.access_hash;
            req2.max_date = task.maxDate;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            });
        }
    }

    private void checkReadTasks() {
        long time = SystemClock.uptimeMillis();
        int a = 0;
        int size = this.readTasks.size();
        while (a < size) {
            ReadTask task = (ReadTask) this.readTasks.get(a);
            if (task.sendRequestTime <= time) {
                completeReadTask(task);
                this.readTasks.remove(a);
                this.readTasksMap.remove(task.dialogId);
                a--;
                size--;
            }
            a++;
        }
    }

    public void markDialogAsReadNow(final long dialogId) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                ReadTask currentReadTask = (ReadTask) MessagesController.this.readTasksMap.get(dialogId);
                if (currentReadTask != null) {
                    MessagesController.this.completeReadTask(currentReadTask);
                    MessagesController.this.readTasks.remove(currentReadTask);
                    MessagesController.this.readTasksMap.remove(dialogId);
                }
            }
        });
    }

    public void markDialogAsRead(long dialogId, int maxPositiveId, int maxNegativeId, int maxDate, boolean popup, int countDiff, boolean readNow) {
        MessagesController messagesController = this;
        long j = dialogId;
        final int i = maxPositiveId;
        int i2 = maxNegativeId;
        int i3 = maxDate;
        int lower_part = (int) j;
        int high_id = (int) (j >> 32);
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        if (lower_part != 0) {
            if (i == 0) {
                i4 = i3;
                i5 = lower_part;
                i6 = i2;
                i7 = i;
            } else if (high_id == 1) {
                i8 = high_id;
                i4 = i3;
                i5 = lower_part;
                i6 = i2;
                i7 = i;
            } else {
                long minMessageId;
                boolean isChannel;
                long maxMessageId;
                long maxMessageId2 = (long) i;
                long minMessageId2 = (long) i2;
                if (lower_part >= 0 || !ChatObject.isChannel(getChat(Integer.valueOf(-lower_part)))) {
                    minMessageId = minMessageId2;
                    isChannel = false;
                    maxMessageId = maxMessageId2;
                } else {
                    boolean isChannel2 = false;
                    isChannel = true;
                    minMessageId = minMessageId2 | (((long) (-lower_part)) << 32);
                    maxMessageId = maxMessageId2 | (((long) (-lower_part)) << true);
                }
                Integer value = (Integer) messagesController.dialogs_read_inbox_max.get(Long.valueOf(dialogId));
                if (value == null) {
                    value = Integer.valueOf(0);
                }
                messagesController.dialogs_read_inbox_max.put(Long.valueOf(dialogId), Integer.valueOf(Math.max(value.intValue(), i)));
                boolean z = false;
                MessagesStorage.getInstance(messagesController.currentAccount).processPendingRead(j, maxMessageId, minMessageId, i3, isChannel);
                i8 = high_id;
                i4 = i3;
                final long j2 = j;
                i6 = i2;
                i2 = countDiff;
                i7 = i;
                final boolean z2 = popup;
                MessagesStorage.getInstance(messagesController.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                    public void run() {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(j2);
                                if (dialog != null) {
                                    if (i2 != 0) {
                                        if (i < dialog.top_message) {
                                            dialog.unread_count = Math.max(dialog.unread_count - i2, 0);
                                            if (i != Integer.MIN_VALUE && dialog.unread_count > dialog.top_message - i) {
                                                dialog.unread_count = dialog.top_message - i;
                                            }
                                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(256));
                                        }
                                    }
                                    dialog.unread_count = 0;
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(256));
                                }
                                if (z2) {
                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processReadMessages(null, j2, 0, i, true);
                                    LongSparseArray<Integer> dialogsToUpdate = new LongSparseArray(1);
                                    dialogsToUpdate.put(j2, Integer.valueOf(-1));
                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processDialogsUpdateRead(dialogsToUpdate);
                                    return;
                                }
                                NotificationsController.getInstance(MessagesController.this.currentAccount).processReadMessages(null, j2, 0, i, false);
                                dialogsToUpdate = new LongSparseArray(1);
                                dialogsToUpdate.put(j2, Integer.valueOf(0));
                                NotificationsController.getInstance(MessagesController.this.currentAccount).processDialogsUpdateRead(dialogsToUpdate);
                            }
                        });
                    }
                });
                if (i7 != Integer.MAX_VALUE) {
                    z = true;
                }
                boolean createReadTask = z;
                int i9 = i8;
                int i10 = i4;
            }
            return;
        }
        i8 = high_id;
        i4 = i3;
        i5 = lower_part;
        i6 = i2;
        i7 = i;
        if (i4 != 0) {
            createReadTask = true;
            EncryptedChat chat = getEncryptedChat(Integer.valueOf(i8));
            int i11 = i4;
            MessagesStorage.getInstance(messagesController.currentAccount).processPendingRead(dialogId, (long) i7, (long) i6, i11, false);
            j2 = dialogId;
            i2 = i11;
            final boolean z3 = popup;
            i10 = i11;
            i11 = countDiff;
            EncryptedChat chat2 = chat;
            chat = maxNegativeId;
            MessagesStorage.getInstance(messagesController.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                public void run() {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.getInstance(MessagesController.this.currentAccount).processReadMessages(null, j2, i2, 0, z3);
                            TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(j2);
                            if (dialog != null) {
                                if (i11 != 0) {
                                    if (chat > dialog.top_message) {
                                        dialog.unread_count = Math.max(dialog.unread_count - i11, 0);
                                        if (chat != ConnectionsManager.DEFAULT_DATACENTER_ID && dialog.unread_count > chat - dialog.top_message) {
                                            dialog.unread_count = chat - dialog.top_message;
                                        }
                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(256));
                                    }
                                }
                                dialog.unread_count = 0;
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(256));
                            }
                            LongSparseArray<Integer> dialogsToUpdate = new LongSparseArray(1);
                            dialogsToUpdate.put(j2, Integer.valueOf(0));
                            NotificationsController.getInstance(MessagesController.this.currentAccount).processDialogsUpdateRead(dialogsToUpdate);
                        }
                    });
                }
            });
            if (chat2.ttl > 0) {
                int serverTime = Math.max(ConnectionsManager.getInstance(messagesController.currentAccount).getCurrentTime(), i10);
                MessagesStorage.getInstance(messagesController.currentAccount).createTaskForSecretChat(chat2.id, serverTime, serverTime, 0, null);
            }
        } else {
            return;
        }
        if (createReadTask) {
            j2 = dialogId;
            final boolean z4 = readNow;
            i = i10;
            i11 = maxPositiveId;
            Utilities.stageQueue.postRunnable(new Runnable() {
                public void run() {
                    ReadTask currentReadTask = (ReadTask) MessagesController.this.readTasksMap.get(j2);
                    if (currentReadTask == null) {
                        currentReadTask = new ReadTask();
                        currentReadTask.dialogId = j2;
                        currentReadTask.sendRequestTime = SystemClock.uptimeMillis() + DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS;
                        if (!z4) {
                            MessagesController.this.readTasksMap.put(j2, currentReadTask);
                            MessagesController.this.readTasks.add(currentReadTask);
                        }
                    }
                    currentReadTask.maxDate = i;
                    currentReadTask.maxId = i11;
                    if (z4) {
                        MessagesController.this.completeReadTask(currentReadTask);
                    }
                }
            });
        }
    }

    public int createChat(String title, ArrayList<Integer> selectedContacts, String about, int type, BaseFragment fragment) {
        MessagesController messagesController = this;
        String str = title;
        ArrayList<Integer> arrayList = selectedContacts;
        int i = type;
        final BaseFragment baseFragment = fragment;
        int a = 0;
        if (i == 1) {
            TL_chat chat = new TL_chat();
            chat.id = UserConfig.getInstance(messagesController.currentAccount).lastBroadcastId;
            chat.title = str;
            chat.photo = new TL_chatPhotoEmpty();
            chat.participants_count = selectedContacts.size();
            chat.date = (int) (System.currentTimeMillis() / 1000);
            chat.version = 1;
            UserConfig instance = UserConfig.getInstance(messagesController.currentAccount);
            instance.lastBroadcastId--;
            putChat(chat, false);
            ArrayList<Chat> chatsArrays = new ArrayList();
            chatsArrays.add(chat);
            MessagesStorage.getInstance(messagesController.currentAccount).putUsersAndChats(null, chatsArrays, true, true);
            TL_chatFull chatFull = new TL_chatFull();
            chatFull.id = chat.id;
            chatFull.chat_photo = new TL_photoEmpty();
            chatFull.notify_settings = new TL_peerNotifySettingsEmpty();
            chatFull.exported_invite = new TL_chatInviteEmpty();
            chatFull.participants = new TL_chatParticipants();
            chatFull.participants.chat_id = chat.id;
            chatFull.participants.admin_id = UserConfig.getInstance(messagesController.currentAccount).getClientUserId();
            chatFull.participants.version = 1;
            for (int a2 = 0; a2 < selectedContacts.size(); a2++) {
                TL_chatParticipant participant = new TL_chatParticipant();
                participant.user_id = ((Integer) arrayList.get(a2)).intValue();
                participant.inviter_id = UserConfig.getInstance(messagesController.currentAccount).getClientUserId();
                participant.date = (int) (System.currentTimeMillis() / 1000);
                chatFull.participants.participants.add(participant);
            }
            MessagesStorage.getInstance(messagesController.currentAccount).updateChatInfo(chatFull, false);
            Message newMsg = new TL_messageService();
            newMsg.action = new TL_messageActionCreatedBroadcastList();
            int newMessageId = UserConfig.getInstance(messagesController.currentAccount).getNewMessageId();
            newMsg.id = newMessageId;
            newMsg.local_id = newMessageId;
            newMsg.from_id = UserConfig.getInstance(messagesController.currentAccount).getClientUserId();
            newMsg.dialog_id = AndroidUtilities.makeBroadcastId(chat.id);
            newMsg.to_id = new TL_peerChat();
            newMsg.to_id.chat_id = chat.id;
            newMsg.date = ConnectionsManager.getInstance(messagesController.currentAccount).getCurrentTime();
            newMsg.random_id = 0;
            newMsg.flags |= 256;
            UserConfig.getInstance(messagesController.currentAccount).saveConfig(false);
            MessageObject newMsgObj = new MessageObject(messagesController.currentAccount, newMsg, messagesController.users, true);
            newMsgObj.messageOwner.send_state = 0;
            ArrayList<MessageObject> objArr = new ArrayList();
            objArr.add(newMsgObj);
            ArrayList<Message> arr = new ArrayList();
            arr.add(newMsg);
            MessagesStorage.getInstance(messagesController.currentAccount).putMessages((ArrayList) arr, false, true, false, 0);
            updateInterfaceWithMessages(newMsg.dialog_id, objArr);
            NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            NotificationCenter instance2 = NotificationCenter.getInstance(messagesController.currentAccount);
            int i2 = NotificationCenter.chatDidCreated;
            Object[] objArr2 = new Object[1];
            objArr2[0] = Integer.valueOf(chat.id);
            instance2.postNotificationName(i2, objArr2);
            return 0;
        } else if (i == 0) {
            final TL_messages_createChat req = new TL_messages_createChat();
            req.title = str;
            while (a < selectedContacts.size()) {
                User user = getUser((Integer) arrayList.get(a));
                if (user != null) {
                    req.users.add(getInputUser(user));
                }
                a++;
            }
            return ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, final TL_error error) {
                    if (error != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                AlertsCreator.processError(MessagesController.this.currentAccount, error, baseFragment, req, new Object[0]);
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatDidFailCreate, new Object[0]);
                            }
                        });
                        return;
                    }
                    final Updates updates = (Updates) response;
                    MessagesController.this.processUpdates(updates, false);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            MessagesController.this.putUsers(updates.users, false);
                            MessagesController.this.putChats(updates.chats, false);
                            if (updates.chats == null || updates.chats.isEmpty()) {
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatDidFailCreate, new Object[0]);
                                return;
                            }
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatDidCreated, Integer.valueOf(((Chat) updates.chats.get(0)).id));
                        }
                    });
                }
            }, 2);
        } else {
            if (i != 2) {
                if (i != 4) {
                    return 0;
                }
            }
            final TL_channels_createChannel req2 = new TL_channels_createChannel();
            req2.title = str;
            req2.about = about;
            if (i == 4) {
                req2.megagroup = true;
            } else {
                req2.broadcast = true;
            }
            return ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req2, new RequestDelegate() {
                public void run(TLObject response, final TL_error error) {
                    if (error != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                AlertsCreator.processError(MessagesController.this.currentAccount, error, baseFragment, req2, new Object[0]);
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatDidFailCreate, new Object[0]);
                            }
                        });
                        return;
                    }
                    final Updates updates = (Updates) response;
                    MessagesController.this.processUpdates(updates, false);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            MessagesController.this.putUsers(updates.users, false);
                            MessagesController.this.putChats(updates.chats, false);
                            if (updates.chats == null || updates.chats.isEmpty()) {
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatDidFailCreate, new Object[0]);
                                return;
                            }
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatDidCreated, Integer.valueOf(((Chat) updates.chats.get(0)).id));
                        }
                    });
                }
            }, 2);
        }
    }

    public void convertToMegaGroup(final Context context, int chat_id) {
        TL_messages_migrateChat req = new TL_messages_migrateChat();
        req.chat_id = chat_id;
        final AlertDialog progressDialog = new AlertDialog(context, 1);
        progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        final int reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (!((Activity) context).isFinishing()) {
                                try {
                                    progressDialog.dismiss();
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                            }
                        }
                    });
                    Updates updates = (Updates) response;
                    MessagesController.this.processUpdates((Updates) response, false);
                    return;
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        if (!((Activity) context).isFinishing()) {
                            try {
                                progressDialog.dismiss();
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                            Builder builder = new Builder(context);
                            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                            builder.setMessage(LocaleController.getString("ErrorOccurred", R.string.ErrorOccurred));
                            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                            builder.show().setCanceledOnTouchOutside(true);
                        }
                    }
                });
            }
        });
        progressDialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ConnectionsManager.getInstance(MessagesController.this.currentAccount).cancelRequest(reqId, true);
                try {
                    dialog.dismiss();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        });
        try {
            progressDialog.show();
        } catch (Exception e) {
        }
    }

    public void addUsersToChannel(int chat_id, ArrayList<InputUser> users, final BaseFragment fragment) {
        if (users != null) {
            if (!users.isEmpty()) {
                final TL_channels_inviteToChannel req = new TL_channels_inviteToChannel();
                req.channel = getInputChannel(chat_id);
                req.users = users;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, final TL_error error) {
                        if (error != null) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    AlertsCreator.processError(MessagesController.this.currentAccount, error, fragment, req, Boolean.valueOf(true));
                                }
                            });
                        } else {
                            MessagesController.this.processUpdates((Updates) response, false);
                        }
                    }
                });
            }
        }
    }

    public void toogleChannelInvites(int chat_id, boolean enabled) {
        TL_channels_toggleInvites req = new TL_channels_toggleInvites();
        req.channel = getInputChannel(chat_id);
        req.enabled = enabled;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (response != null) {
                    MessagesController.this.processUpdates((Updates) response, false);
                }
            }
        }, 64);
    }

    public void toogleChannelSignatures(int chat_id, boolean enabled) {
        TL_channels_toggleSignatures req = new TL_channels_toggleSignatures();
        req.channel = getInputChannel(chat_id);
        req.enabled = enabled;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (response != null) {
                    MessagesController.this.processUpdates((Updates) response, false);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_CHANNEL));
                        }
                    });
                }
            }
        }, 64);
    }

    public void toogleChannelInvitesHistory(int chat_id, boolean enabled) {
        TL_channels_togglePreHistoryHidden req = new TL_channels_togglePreHistoryHidden();
        req.channel = getInputChannel(chat_id);
        req.enabled = enabled;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (response != null) {
                    MessagesController.this.processUpdates((Updates) response, false);
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_CHANNEL));
                        }
                    });
                }
            }
        }, 64);
    }

    public void updateChannelAbout(int chat_id, final String about, final ChatFull info) {
        if (info != null) {
            TL_channels_editAbout req = new TL_channels_editAbout();
            req.channel = getInputChannel(chat_id);
            req.about = about;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (response instanceof TL_boolTrue) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                info.about = about;
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).updateChatInfo(info, false);
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, info, Integer.valueOf(0), Boolean.valueOf(false), null);
                            }
                        });
                    }
                }
            }, 64);
        }
    }

    public void updateChannelUserName(final int chat_id, final String userName) {
        TL_channels_updateUsername req = new TL_channels_updateUsername();
        req.channel = getInputChannel(chat_id);
        req.username = userName;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (response instanceof TL_boolTrue) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            Chat chat = MessagesController.this.getChat(Integer.valueOf(chat_id));
                            if (userName.length() != 0) {
                                chat.flags |= 64;
                            } else {
                                chat.flags &= -65;
                            }
                            chat.username = userName;
                            ArrayList<Chat> arrayList = new ArrayList();
                            arrayList.add(chat);
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(null, arrayList, true, true);
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(MessagesController.UPDATE_MASK_CHANNEL));
                        }
                    });
                }
            }
        }, 64);
    }

    public void sendBotStart(User user, String botHash) {
        if (user != null) {
            TL_messages_startBot req = new TL_messages_startBot();
            req.bot = getInputUser(user);
            req.peer = getInputPeer(user.id);
            req.start_param = botHash;
            req.random_id = Utilities.random.nextLong();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        MessagesController.this.processUpdates((Updates) response, false);
                    }
                }
            });
        }
    }

    public void toggleAdminMode(final int chat_id, boolean enabled) {
        TL_messages_toggleChatAdmins req = new TL_messages_toggleChatAdmins();
        req.chat_id = chat_id;
        req.enabled = enabled;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    MessagesController.this.processUpdates((Updates) response, false);
                    MessagesController.this.loadFullChat(chat_id, 0, true);
                }
            }
        });
    }

    public void toggleUserAdmin(int chat_id, int user_id, boolean admin) {
        TL_messages_editChatAdmin req = new TL_messages_editChatAdmin();
        req.chat_id = chat_id;
        req.user_id = getInputUser(user_id);
        req.is_admin = admin;
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
            }
        });
    }

    public void addUserToChat(int chat_id, User user, ChatFull info, int count_fwd, String botHash, BaseFragment fragment) {
        MessagesController messagesController = this;
        int i = chat_id;
        User user2 = user;
        ChatFull chatFull = info;
        String str = botHash;
        if (user2 != null) {
            boolean z = false;
            if (i > 0) {
                TLObject tL_messages_startBot;
                int i2;
                final TLObject tLObject;
                final boolean z2;
                final InputUser inputUser;
                AnonymousClass100 anonymousClass100;
                AnonymousClass100 anonymousClass1002;
                final int i3;
                ConnectionsManager instance;
                final BaseFragment baseFragment;
                TLObject request;
                final boolean z3;
                boolean isChannel = ChatObject.isChannel(i, messagesController.currentAccount);
                if (isChannel && getChat(Integer.valueOf(chat_id)).megagroup) {
                    z = true;
                }
                boolean isMegagroup = z;
                InputUser inputUser2 = getInputUser(user2);
                if (str != null) {
                    if (!isChannel || isMegagroup) {
                        tL_messages_startBot = new TL_messages_startBot();
                        tL_messages_startBot.bot = inputUser2;
                        if (isChannel) {
                            tL_messages_startBot.peer = getInputPeer(-i);
                        } else {
                            tL_messages_startBot.peer = new TL_inputPeerChat();
                            tL_messages_startBot.peer.chat_id = i;
                        }
                        tL_messages_startBot.start_param = str;
                        tL_messages_startBot.random_id = Utilities.random.nextLong();
                        i2 = count_fwd;
                        tLObject = tL_messages_startBot;
                        z2 = isChannel;
                        inputUser = inputUser2;
                        anonymousClass100 = anonymousClass1002;
                        i3 = i;
                        instance = ConnectionsManager.getInstance(messagesController.currentAccount);
                        baseFragment = fragment;
                        request = tLObject;
                        z3 = isMegagroup;
                        anonymousClass1002 = new RequestDelegate() {
                            public void run(TLObject response, final TL_error error) {
                                if (z2 && (inputUser instanceof TL_inputUserSelf)) {
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.this.joiningToChannels.remove(Integer.valueOf(i3));
                                        }
                                    });
                                }
                                if (error != null) {
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            int access$000 = MessagesController.this.currentAccount;
                                            TL_error tL_error = error;
                                            BaseFragment baseFragment = baseFragment;
                                            TLObject tLObject = tLObject;
                                            boolean z = true;
                                            Object[] objArr = new Object[1];
                                            if (!z2 || z3) {
                                                z = false;
                                            }
                                            objArr[0] = Boolean.valueOf(z);
                                            AlertsCreator.processError(access$000, tL_error, baseFragment, tLObject, objArr);
                                        }
                                    });
                                    return;
                                }
                                boolean hasJoinMessage = false;
                                Updates updates = (Updates) response;
                                for (int a = 0; a < updates.updates.size(); a++) {
                                    Update update = (Update) updates.updates.get(a);
                                    if ((update instanceof TL_updateNewChannelMessage) && (((TL_updateNewChannelMessage) update).message.action instanceof TL_messageActionChatAddUser)) {
                                        hasJoinMessage = true;
                                        break;
                                    }
                                }
                                MessagesController.this.processUpdates(updates, false);
                                if (z2) {
                                    if (!hasJoinMessage && (inputUser instanceof TL_inputUserSelf)) {
                                        MessagesController.this.generateJoinMessage(i3, true);
                                    }
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.this.loadFullChat(i3, 0, true);
                                        }
                                    }, 1000);
                                }
                                if (z2 && (inputUser instanceof TL_inputUserSelf)) {
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).updateDialogsWithDeletedMessages(new ArrayList(), null, true, i3);
                                }
                            }
                        };
                        instance.sendRequest(request, anonymousClass100);
                    }
                }
                if (isChannel) {
                    if (!(inputUser2 instanceof TL_inputUserSelf)) {
                        tL_messages_startBot = new TL_channels_inviteToChannel();
                        tL_messages_startBot.channel = getInputChannel(chat_id);
                        tL_messages_startBot.users.add(inputUser2);
                    } else if (!messagesController.joiningToChannels.contains(Integer.valueOf(chat_id))) {
                        tL_messages_startBot = new TL_channels_joinChannel();
                        tL_messages_startBot.channel = getInputChannel(chat_id);
                        TLObject request2 = tL_messages_startBot;
                        messagesController.joiningToChannels.add(Integer.valueOf(chat_id));
                    } else {
                        return;
                    }
                    i2 = count_fwd;
                    tLObject = tL_messages_startBot;
                    z2 = isChannel;
                    inputUser = inputUser2;
                    anonymousClass100 = anonymousClass1002;
                    i3 = i;
                    instance = ConnectionsManager.getInstance(messagesController.currentAccount);
                    baseFragment = fragment;
                    request = tLObject;
                    z3 = isMegagroup;
                    anonymousClass1002 = /* anonymous class already generated */;
                    instance.sendRequest(request, anonymousClass100);
                } else {
                    tL_messages_startBot = new TL_messages_addChatUser();
                    tL_messages_startBot.chat_id = i;
                    tL_messages_startBot.fwd_limit = count_fwd;
                    tL_messages_startBot.user_id = inputUser2;
                    tLObject = tL_messages_startBot;
                    z2 = isChannel;
                    inputUser = inputUser2;
                    anonymousClass100 = anonymousClass1002;
                    i3 = i;
                    instance = ConnectionsManager.getInstance(messagesController.currentAccount);
                    baseFragment = fragment;
                    request = tLObject;
                    z3 = isMegagroup;
                    anonymousClass1002 = /* anonymous class already generated */;
                    instance.sendRequest(request, anonymousClass100);
                }
            } else if (chatFull instanceof TL_chatFull) {
                int a = 0;
                while (a < chatFull.participants.participants.size()) {
                    if (((ChatParticipant) chatFull.participants.participants.get(a)).user_id != user2.id) {
                        a++;
                    } else {
                        return;
                    }
                }
                Chat chat = getChat(Integer.valueOf(chat_id));
                chat.participants_count++;
                ArrayList<Chat> chatArrayList = new ArrayList();
                chatArrayList.add(chat);
                MessagesStorage.getInstance(messagesController.currentAccount).putUsersAndChats(null, chatArrayList, true, true);
                TL_chatParticipant newPart = new TL_chatParticipant();
                newPart.user_id = user2.id;
                newPart.inviter_id = UserConfig.getInstance(messagesController.currentAccount).getClientUserId();
                newPart.date = ConnectionsManager.getInstance(messagesController.currentAccount).getCurrentTime();
                chatFull.participants.participants.add(0, newPart);
                MessagesStorage.getInstance(messagesController.currentAccount).updateChatInfo(chatFull, true);
                NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, chatFull, Integer.valueOf(0), Boolean.valueOf(false), null);
                NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(32));
            }
        }
    }

    public void deleteUserFromChat(int chat_id, User user, ChatFull info) {
        deleteUserFromChat(chat_id, user, info, false);
    }

    public void deleteUserFromChat(int chat_id, User user, ChatFull info, boolean forceDelete) {
        MessagesController messagesController = this;
        int i = chat_id;
        User user2 = user;
        ChatFull chatFull = info;
        if (user2 != null) {
            if (i > 0) {
                TLObject request;
                InputUser inputUser = getInputUser(user2);
                Chat chat = getChat(Integer.valueOf(chat_id));
                boolean isChannel = ChatObject.isChannel(chat);
                if (!isChannel) {
                    request = new TL_messages_deleteChatUser();
                    request.chat_id = i;
                    request.user_id = getInputUser(user2);
                } else if (!(inputUser instanceof TL_inputUserSelf)) {
                    TLObject req = new TL_channels_editBanned();
                    req.channel = getInputChannel(chat);
                    req.user_id = inputUser;
                    req.banned_rights = new TL_channelBannedRights();
                    req.banned_rights.view_messages = true;
                    req.banned_rights.send_media = true;
                    req.banned_rights.send_messages = true;
                    req.banned_rights.send_stickers = true;
                    req.banned_rights.send_gifs = true;
                    req.banned_rights.send_games = true;
                    req.banned_rights.send_inline = true;
                    req.banned_rights.embed_links = true;
                    request = req;
                } else if (chat.creator && forceDelete) {
                    request = new TL_channels_deleteChannel();
                    request.channel = getInputChannel(chat);
                } else {
                    request = new TL_channels_leaveChannel();
                    request.channel = getInputChannel(chat);
                }
                TLObject request2 = request;
                ConnectionsManager instance = ConnectionsManager.getInstance(messagesController.currentAccount);
                final User user3 = user2;
                final int i2 = i;
                final boolean z = isChannel;
                AnonymousClass101 anonymousClass101 = r0;
                final InputUser inputUser2 = inputUser;
                AnonymousClass101 anonymousClass1012 = new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (user3.id == UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.deleteDialog((long) (-i2), 0);
                                }
                            });
                        }
                        if (error == null) {
                            MessagesController.this.processUpdates((Updates) response, false);
                            if (z && !(inputUser2 instanceof TL_inputUserSelf)) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        MessagesController.this.loadFullChat(i2, 0, true);
                                    }
                                }, 1000);
                            }
                        }
                    }
                };
                instance.sendRequest(request2, anonymousClass101, 64);
            } else if (chatFull instanceof TL_chatFull) {
                Chat chat2 = getChat(Integer.valueOf(chat_id));
                chat2.participants_count--;
                ArrayList<Chat> chatArrayList = new ArrayList();
                chatArrayList.add(chat2);
                MessagesStorage.getInstance(messagesController.currentAccount).putUsersAndChats(null, chatArrayList, true, true);
                boolean changed = false;
                for (int a = 0; a < chatFull.participants.participants.size(); a++) {
                    if (((ChatParticipant) chatFull.participants.participants.get(a)).user_id == user2.id) {
                        chatFull.participants.participants.remove(a);
                        changed = true;
                        break;
                    }
                }
                if (changed) {
                    MessagesStorage.getInstance(messagesController.currentAccount).updateChatInfo(chatFull, true);
                    NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, chatFull, Integer.valueOf(0), Boolean.valueOf(false), null);
                }
                NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(32));
            }
        }
    }

    public void changeChatTitle(int chat_id, String title) {
        if (chat_id > 0) {
            TLObject request;
            if (ChatObject.isChannel(chat_id, this.currentAccount)) {
                request = new TL_channels_editTitle();
                request.channel = getInputChannel(chat_id);
                request.title = title;
            } else {
                request = new TL_messages_editChatTitle();
                request.chat_id = chat_id;
                request.title = title;
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        MessagesController.this.processUpdates((Updates) response, false);
                    }
                }
            }, 64);
            return;
        }
        Chat chat = getChat(Integer.valueOf(chat_id));
        chat.title = title;
        ArrayList<Chat> chatArrayList = new ArrayList();
        chatArrayList.add(chat);
        MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, chatArrayList, true, true);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(16));
    }

    public void unregistedPush() {
        if (UserConfig.getInstance(this.currentAccount).registeredForPush && SharedConfig.pushString.length() == 0) {
            TL_account_unregisterDevice req = new TL_account_unregisterDevice();
            req.token = SharedConfig.pushString;
            req.token_type = 2;
            for (int a = 0; a < 3; a++) {
                UserConfig userConfig = UserConfig.getInstance(a);
                if (a != this.currentAccount && userConfig.isClientActivated()) {
                    req.other_uids.add(Integer.valueOf(userConfig.getClientUserId()));
                }
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            });
        }
    }

    public void performLogout(boolean byUser) {
        this.notificationsPreferences.edit().clear().commit();
        this.emojiPreferences.edit().putLong("lastGifLoadTime", 0).putLong("lastStickersLoadTime", 0).putLong("lastStickersLoadTimeMask", 0).putLong("lastStickersLoadTimeFavs", 0).commit();
        this.mainPreferences.edit().remove("gifhint").commit();
        if (byUser) {
            unregistedPush();
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_auth_logOut(), new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    ConnectionsManager.getInstance(MessagesController.this.currentAccount).cleanup();
                }
            });
        } else {
            ConnectionsManager.getInstance(this.currentAccount).cleanup();
        }
        UserConfig.getInstance(this.currentAccount).clearConfig();
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.appDidLogout, new Object[0]);
        MessagesStorage.getInstance(this.currentAccount).cleanup(false);
        cleanup();
        ContactsController.getInstance(this.currentAccount).deleteUnknownAppAccounts();
    }

    public void generateUpdateMessage() {
        if (!(BuildVars.DEBUG_VERSION || SharedConfig.lastUpdateVersion == null)) {
            if (!SharedConfig.lastUpdateVersion.equals(BuildVars.BUILD_VERSION_STRING)) {
                TL_help_getAppChangelog req = new TL_help_getAppChangelog();
                req.prev_app_version = SharedConfig.lastUpdateVersion;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            SharedConfig.lastUpdateVersion = BuildVars.BUILD_VERSION_STRING;
                            SharedConfig.saveConfig();
                        }
                        if (response instanceof Updates) {
                            MessagesController.this.processUpdates((Updates) response, false);
                        }
                    }
                });
            }
        }
    }

    public void registerForPush(final String regid) {
        if (!(TextUtils.isEmpty(regid) || this.registeringForPush)) {
            if (UserConfig.getInstance(this.currentAccount).getClientUserId() != 0) {
                if (!UserConfig.getInstance(this.currentAccount).registeredForPush || !regid.equals(SharedConfig.pushString)) {
                    this.registeringForPush = true;
                    this.lastPushRegisterSendTime = SystemClock.uptimeMillis();
                    if (SharedConfig.pushAuthKey == null) {
                        SharedConfig.pushAuthKey = new byte[256];
                        Utilities.random.nextBytes(SharedConfig.pushAuthKey);
                        SharedConfig.saveConfig();
                    }
                    TL_account_registerDevice req = new TL_account_registerDevice();
                    req.token_type = 2;
                    req.token = regid;
                    req.secret = SharedConfig.pushAuthKey;
                    for (int a = 0; a < 3; a++) {
                        UserConfig userConfig = UserConfig.getInstance(a);
                        if (a != this.currentAccount && userConfig.isClientActivated()) {
                            req.other_uids.add(Integer.valueOf(userConfig.getClientUserId()));
                        }
                    }
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            if (response instanceof TL_boolTrue) {
                                if (BuildVars.LOGS_ENABLED) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("account ");
                                    stringBuilder.append(MessagesController.this.currentAccount);
                                    stringBuilder.append(" registered for push");
                                    FileLog.d(stringBuilder.toString());
                                }
                                UserConfig.getInstance(MessagesController.this.currentAccount).registeredForPush = true;
                                SharedConfig.pushString = regid;
                                UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.registeringForPush = false;
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    public void loadCurrentState() {
        if (!this.updatingState) {
            this.updatingState = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_updates_getState(), new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    int a = 0;
                    MessagesController.this.updatingState = false;
                    if (error == null) {
                        TL_updates_state res = (TL_updates_state) response;
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastDateValue(res.date);
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastPtsValue(res.pts);
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastSeqValue(res.seq);
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastQtsValue(res.qts);
                        while (a < 3) {
                            MessagesController.this.processUpdatesQueue(a, 2);
                            a++;
                        }
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).saveDiffParams(MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastQtsValue());
                    } else if (error.code != 401) {
                        MessagesController.this.loadCurrentState();
                    }
                }
            });
        }
    }

    private int getUpdateSeq(Updates updates) {
        if (updates instanceof TL_updatesCombined) {
            return updates.seq_start;
        }
        return updates.seq;
    }

    private void setUpdatesStartTime(int type, long time) {
        if (type == 0) {
            this.updatesStartWaitTimeSeq = time;
        } else if (type == 1) {
            this.updatesStartWaitTimePts = time;
        } else if (type == 2) {
            this.updatesStartWaitTimeQts = time;
        }
    }

    public long getUpdatesStartTime(int type) {
        if (type == 0) {
            return this.updatesStartWaitTimeSeq;
        }
        if (type == 1) {
            return this.updatesStartWaitTimePts;
        }
        if (type == 2) {
            return this.updatesStartWaitTimeQts;
        }
        return 0;
    }

    private int isValidUpdate(Updates updates, int type) {
        if (type == 0) {
            int seq = getUpdateSeq(updates);
            if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() + 1 != seq) {
                if (MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() != seq) {
                    return MessagesStorage.getInstance(this.currentAccount).getLastSeqValue() < seq ? 1 : 2;
                }
            }
            return 0;
        } else if (type == 1) {
            if (updates.pts <= MessagesStorage.getInstance(this.currentAccount).getLastPtsValue()) {
                return 2;
            }
            return MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() + updates.pts_count == updates.pts ? 0 : 1;
        } else if (type != 2) {
            return 0;
        } else {
            if (updates.pts <= MessagesStorage.getInstance(this.currentAccount).getLastQtsValue()) {
                return 2;
            }
            return MessagesStorage.getInstance(this.currentAccount).getLastQtsValue() + updates.updates.size() == updates.pts ? 0 : 1;
        }
    }

    protected void loadUnknownChannel(final Chat channel, long taskId) {
        if (channel instanceof TL_channel) {
            if (this.gettingUnknownChannels.indexOfKey(channel.id) < 0) {
                if (channel.access_hash == 0) {
                    if (taskId != 0) {
                        MessagesStorage.getInstance(this.currentAccount).removePendingTask(taskId);
                    }
                    return;
                }
                long data;
                TL_inputPeerChannel inputPeer = new TL_inputPeerChannel();
                inputPeer.channel_id = channel.id;
                inputPeer.access_hash = channel.access_hash;
                this.gettingUnknownChannels.put(channel.id, true);
                TL_messages_getPeerDialogs req = new TL_messages_getPeerDialogs();
                TL_inputDialogPeer inputDialogPeer = new TL_inputDialogPeer();
                inputDialogPeer.peer = inputPeer;
                req.peers.add(inputDialogPeer);
                if (taskId == 0) {
                    data = null;
                    try {
                        data = new NativeByteBuffer(4 + channel.getObjectSize());
                        data.writeInt32(0);
                        channel.serializeToStream(data);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                    data = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                } else {
                    data = taskId;
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (response != null) {
                            TL_messages_peerDialogs res = (TL_messages_peerDialogs) response;
                            if (!(res.dialogs.isEmpty() || res.chats.isEmpty())) {
                                messages_Dialogs dialogs = new TL_messages_dialogs();
                                dialogs.dialogs.addAll(res.dialogs);
                                dialogs.messages.addAll(res.messages);
                                dialogs.users.addAll(res.users);
                                dialogs.chats.addAll(res.chats);
                                MessagesController.this.processLoadedDialogs(dialogs, null, 0, 1, 2, false, false, false);
                            }
                        }
                        if (data != 0) {
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(data);
                        }
                        MessagesController.this.gettingUnknownChannels.delete(channel.id);
                    }
                });
            }
        }
    }

    public void startShortPoll(final int channelId, final boolean stop) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                if (stop) {
                    MessagesController.this.needShortPollChannels.delete(channelId);
                    return;
                }
                MessagesController.this.needShortPollChannels.put(channelId, 0);
                if (MessagesController.this.shortPollChannels.indexOfKey(channelId) < 0) {
                    MessagesController.this.getChannelDifference(channelId, 3, 0, null);
                }
            }
        });
    }

    private void getChannelDifference(int channelId) {
        getChannelDifference(channelId, 0, 0, null);
    }

    public static boolean isSupportId(int id) {
        if (!(id / 1000 == 777 || id == 333000 || id == 4240000 || id == 4240000 || id == 4244000 || id == 4245000 || id == 4246000 || id == 410000 || id == 420000 || id == 431000 || id == 431415000 || id == 434000 || id == 4243000 || id == 439000 || id == 449000 || id == 450000 || id == 452000 || id == 454000 || id == 4254000 || id == 455000 || id == 460000 || id == 470000 || id == 479000 || id == 796000 || id == 482000 || id == 490000 || id == 496000 || id == 497000 || id == 498000)) {
            if (id != 4298000) {
                return false;
            }
        }
        return true;
    }

    protected void getChannelDifference(int channelId, int newDialogType, long taskId, InputChannel inputChannel) {
        int i = channelId;
        int i2 = newDialogType;
        long j = taskId;
        boolean gettingDifferenceChannel = this.gettingDifferenceChannels.get(i);
        if (!gettingDifferenceChannel) {
            int channelPts;
            InputChannel inputChannel2;
            int limit = 100;
            if (i2 != 1) {
                channelPts = r7.channelsPts.get(i);
                if (channelPts == 0) {
                    channelPts = MessagesStorage.getInstance(r7.currentAccount).getChannelPtsSync(i);
                    if (channelPts != 0) {
                        r7.channelsPts.put(i, channelPts);
                    }
                    if (channelPts == 0 && (i2 == 2 || i2 == 3)) {
                        return;
                    }
                }
                if (channelPts == 0) {
                    return;
                }
            } else if (r7.channelsPts.get(i) == 0) {
                channelPts = 1;
                limit = 1;
            } else {
                return;
            }
            int limit2 = limit;
            int channelPts2 = channelPts;
            if (inputChannel == null) {
                Chat chat = getChat(Integer.valueOf(channelId));
                if (chat == null) {
                    chat = MessagesStorage.getInstance(r7.currentAccount).getChatSync(i);
                    if (chat != null) {
                        putChat(chat, true);
                    }
                }
                inputChannel2 = getInputChannel(chat);
            } else {
                inputChannel2 = inputChannel;
            }
            int i3;
            if (inputChannel2 == null) {
                i3 = limit2;
            } else if (inputChannel2.access_hash == 0) {
                boolean z = gettingDifferenceChannel;
                i3 = limit2;
            } else {
                long newTaskId;
                if (j == 0) {
                    long newTaskId2;
                    NativeByteBuffer data = null;
                    try {
                        newTaskId2 = new NativeByteBuffer(12 + inputChannel2.getObjectSize());
                        newTaskId2.writeInt32(6);
                        newTaskId2.writeInt32(i);
                        newTaskId2.writeInt32(i2);
                        inputChannel2.serializeToStream(newTaskId2);
                    } catch (Throwable e) {
                        NativeByteBuffer data2 = data;
                        FileLog.e(e);
                        newTaskId2 = data2;
                    }
                    newTaskId = MessagesStorage.getInstance(r7.currentAccount).createPendingTask(newTaskId2);
                } else {
                    newTaskId = j;
                }
                boolean z2 = true;
                r7.gettingDifferenceChannels.put(i, true);
                TL_updates_getChannelDifference req = new TL_updates_getChannelDifference();
                req.channel = inputChannel2;
                req.filter = new TL_channelMessagesFilterEmpty();
                req.pts = channelPts2;
                req.limit = limit2;
                if (i2 == 3) {
                    z2 = false;
                }
                req.force = z2;
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("start getChannelDifference with pts = ");
                    stringBuilder.append(channelPts2);
                    stringBuilder.append(" channelId = ");
                    stringBuilder.append(i);
                    FileLog.d(stringBuilder.toString());
                }
                AnonymousClass115 anonymousClass115 = r1;
                ConnectionsManager instance = ConnectionsManager.getInstance(r7.currentAccount);
                final int i4 = i;
                TL_updates_getChannelDifference req2 = req;
                req = i2;
                AnonymousClass115 anonymousClass1152 = new RequestDelegate() {
                    public void run(TLObject response, final TL_error error) {
                        if (error == null) {
                            final updates_ChannelDifference res = (updates_ChannelDifference) response;
                            SparseArray<User> usersDict = new SparseArray();
                            int a = 0;
                            for (int a2 = 0; a2 < res.users.size(); a2++) {
                                User user = (User) res.users.get(a2);
                                usersDict.put(user.id, user);
                            }
                            Chat channel = null;
                            for (int a3 = 0; a3 < res.chats.size(); a3++) {
                                Chat chat = (Chat) res.chats.get(a3);
                                if (chat.id == i4) {
                                    channel = chat;
                                    break;
                                }
                            }
                            final Chat channelFinal = channel;
                            ArrayList<TL_updateMessageID> msgUpdates = new ArrayList();
                            if (!res.other_updates.isEmpty()) {
                                while (a < res.other_updates.size()) {
                                    Update upd = (Update) res.other_updates.get(a);
                                    if (upd instanceof TL_updateMessageID) {
                                        msgUpdates.add((TL_updateMessageID) upd);
                                        res.other_updates.remove(a);
                                        a--;
                                    }
                                    a++;
                                }
                            }
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, res.chats, true, true);
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.putUsers(res.users, false);
                                    MessagesController.this.putChats(res.chats, false);
                                }
                            });
                            final ArrayList<TL_updateMessageID> arrayList = msgUpdates;
                            final updates_ChannelDifference org_telegram_tgnet_TLRPC_updates_ChannelDifference = res;
                            final SparseArray<User> sparseArray = usersDict;
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                public void run() {
                                    if (!arrayList.isEmpty()) {
                                        final SparseArray<long[]> corrected = new SparseArray();
                                        Iterator it = arrayList.iterator();
                                        while (it.hasNext()) {
                                            TL_updateMessageID update = (TL_updateMessageID) it.next();
                                            long[] ids = MessagesStorage.getInstance(MessagesController.this.currentAccount).updateMessageStateAndId(update.random_id, null, update.id, 0, false, i4);
                                            if (ids != null) {
                                                corrected.put(update.id, ids);
                                            }
                                        }
                                        if (corrected.size() != 0) {
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                    for (int a = 0; a < corrected.size(); a++) {
                                                        int newId = corrected.keyAt(a);
                                                        SendMessagesHelper.getInstance(MessagesController.this.currentAccount).processSentMessage((int) ((long[]) corrected.valueAt(a))[1]);
                                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newId), null, Long.valueOf(ids[0]));
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    Utilities.stageQueue.postRunnable(new Runnable() {
                                        public void run() {
                                            int i = Integer.MIN_VALUE;
                                            if (!(org_telegram_tgnet_TLRPC_updates_ChannelDifference instanceof TL_updates_channelDifference)) {
                                                if (!(org_telegram_tgnet_TLRPC_updates_ChannelDifference instanceof TL_updates_channelDifferenceEmpty)) {
                                                    if (org_telegram_tgnet_TLRPC_updates_ChannelDifference instanceof TL_updates_channelDifferenceTooLong) {
                                                        long dialog_id = (long) (-i4);
                                                        Integer inboxValue = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(dialog_id));
                                                        if (inboxValue == null) {
                                                            inboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, dialog_id));
                                                            MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(dialog_id), inboxValue);
                                                        }
                                                        Integer outboxValue = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(dialog_id));
                                                        if (outboxValue == null) {
                                                            outboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, dialog_id));
                                                            MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(dialog_id), outboxValue);
                                                        }
                                                        for (int a = 0; a < org_telegram_tgnet_TLRPC_updates_ChannelDifference.messages.size(); a++) {
                                                            boolean z;
                                                            Message message = (Message) org_telegram_tgnet_TLRPC_updates_ChannelDifference.messages.get(a);
                                                            message.dialog_id = (long) (-i4);
                                                            if (!(message.action instanceof TL_messageActionChannelCreate) && (channelFinal == null || !channelFinal.left)) {
                                                                if ((message.out ? outboxValue : inboxValue).intValue() < message.id) {
                                                                    z = true;
                                                                    message.unread = z;
                                                                    if (channelFinal != null && channelFinal.megagroup) {
                                                                        message.flags |= Integer.MIN_VALUE;
                                                                    }
                                                                }
                                                            }
                                                            z = false;
                                                            message.unread = z;
                                                            message.flags |= Integer.MIN_VALUE;
                                                        }
                                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).overwriteChannel(i4, (TL_updates_channelDifferenceTooLong) org_telegram_tgnet_TLRPC_updates_ChannelDifference, req);
                                                    }
                                                    MessagesController.this.gettingDifferenceChannels.delete(i4);
                                                    MessagesController.this.channelsPts.put(i4, org_telegram_tgnet_TLRPC_updates_ChannelDifference.pts);
                                                    if ((org_telegram_tgnet_TLRPC_updates_ChannelDifference.flags & 2) != 0) {
                                                        MessagesController.this.shortPollChannels.put(i4, ((int) (System.currentTimeMillis() / 1000)) + org_telegram_tgnet_TLRPC_updates_ChannelDifference.timeout);
                                                    }
                                                    if (!org_telegram_tgnet_TLRPC_updates_ChannelDifference.isFinal) {
                                                        MessagesController.this.getChannelDifference(i4);
                                                    }
                                                    if (BuildVars.LOGS_ENABLED) {
                                                        StringBuilder stringBuilder = new StringBuilder();
                                                        stringBuilder.append("received channel difference with pts = ");
                                                        stringBuilder.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.pts);
                                                        stringBuilder.append(" channelId = ");
                                                        stringBuilder.append(i4);
                                                        FileLog.d(stringBuilder.toString());
                                                        stringBuilder = new StringBuilder();
                                                        stringBuilder.append("new_messages = ");
                                                        stringBuilder.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.new_messages.size());
                                                        stringBuilder.append(" messages = ");
                                                        stringBuilder.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.messages.size());
                                                        stringBuilder.append(" users = ");
                                                        stringBuilder.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.users.size());
                                                        stringBuilder.append(" chats = ");
                                                        stringBuilder.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.chats.size());
                                                        stringBuilder.append(" other updates = ");
                                                        stringBuilder.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.other_updates.size());
                                                        FileLog.d(stringBuilder.toString());
                                                    }
                                                    if (newTaskId != 0) {
                                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                                                    }
                                                }
                                            }
                                            if (!org_telegram_tgnet_TLRPC_updates_ChannelDifference.new_messages.isEmpty()) {
                                                final LongSparseArray<ArrayList<MessageObject>> messages = new LongSparseArray();
                                                ImageLoader.saveMessagesThumbs(org_telegram_tgnet_TLRPC_updates_ChannelDifference.new_messages);
                                                final ArrayList<MessageObject> pushMessages = new ArrayList();
                                                long dialog_id2 = (long) (-i4);
                                                Integer inboxValue2 = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(dialog_id2));
                                                if (inboxValue2 == null) {
                                                    inboxValue2 = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, dialog_id2));
                                                    MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(dialog_id2), inboxValue2);
                                                }
                                                Integer outboxValue2 = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(dialog_id2));
                                                if (outboxValue2 == null) {
                                                    outboxValue2 = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, dialog_id2));
                                                    MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(dialog_id2), outboxValue2);
                                                }
                                                int a2 = 0;
                                                while (a2 < org_telegram_tgnet_TLRPC_updates_ChannelDifference.new_messages.size()) {
                                                    boolean z2;
                                                    MessageObject obj;
                                                    long uid;
                                                    ArrayList<MessageObject> arr;
                                                    Message message2 = (Message) org_telegram_tgnet_TLRPC_updates_ChannelDifference.new_messages.get(a2);
                                                    if (channelFinal == null || !channelFinal.left) {
                                                        if ((message2.out ? outboxValue2 : inboxValue2).intValue() < message2.id && !(message2.action instanceof TL_messageActionChannelCreate)) {
                                                            z2 = true;
                                                            message2.unread = z2;
                                                            if (channelFinal != null && channelFinal.megagroup) {
                                                                message2.flags |= i;
                                                            }
                                                            obj = new MessageObject(MessagesController.this.currentAccount, message2, sparseArray, MessagesController.this.createdDialogIds.contains(Long.valueOf(dialog_id2)));
                                                            if (!obj.isOut() && obj.isUnread()) {
                                                                pushMessages.add(obj);
                                                            }
                                                            uid = (long) (-i4);
                                                            arr = (ArrayList) messages.get(uid);
                                                            if (arr == null) {
                                                                arr = new ArrayList();
                                                                messages.put(uid, arr);
                                                            }
                                                            arr.add(obj);
                                                            a2++;
                                                            i = Integer.MIN_VALUE;
                                                        }
                                                    }
                                                    z2 = false;
                                                    message2.unread = z2;
                                                    message2.flags |= i;
                                                    obj = new MessageObject(MessagesController.this.currentAccount, message2, sparseArray, MessagesController.this.createdDialogIds.contains(Long.valueOf(dialog_id2)));
                                                    pushMessages.add(obj);
                                                    uid = (long) (-i4);
                                                    arr = (ArrayList) messages.get(uid);
                                                    if (arr == null) {
                                                        arr = new ArrayList();
                                                        messages.put(uid, arr);
                                                    }
                                                    arr.add(obj);
                                                    a2++;
                                                    i = Integer.MIN_VALUE;
                                                }
                                                AndroidUtilities.runOnUIThread(new Runnable() {
                                                    public void run() {
                                                        for (int a = 0; a < messages.size(); a++) {
                                                            MessagesController.this.updateInterfaceWithMessages(messages.keyAt(a), (ArrayList) messages.valueAt(a));
                                                        }
                                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                                    }
                                                });
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                                    public void run() {
                                                        if (!pushMessages.isEmpty()) {
                                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                                public void run() {
                                                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(pushMessages, true, false);
                                                                }
                                                            });
                                                        }
                                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(org_telegram_tgnet_TLRPC_updates_ChannelDifference.new_messages, true, false, false, DownloadController.getInstance(MessagesController.this.currentAccount).getAutodownloadMask());
                                                    }
                                                });
                                            }
                                            if (!org_telegram_tgnet_TLRPC_updates_ChannelDifference.other_updates.isEmpty()) {
                                                MessagesController.this.processUpdateArray(org_telegram_tgnet_TLRPC_updates_ChannelDifference.other_updates, org_telegram_tgnet_TLRPC_updates_ChannelDifference.users, org_telegram_tgnet_TLRPC_updates_ChannelDifference.chats, true);
                                            }
                                            MessagesController.this.processChannelsUpdatesQueue(i4, 1);
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).saveChannelPts(i4, org_telegram_tgnet_TLRPC_updates_ChannelDifference.pts);
                                            MessagesController.this.gettingDifferenceChannels.delete(i4);
                                            MessagesController.this.channelsPts.put(i4, org_telegram_tgnet_TLRPC_updates_ChannelDifference.pts);
                                            if ((org_telegram_tgnet_TLRPC_updates_ChannelDifference.flags & 2) != 0) {
                                                MessagesController.this.shortPollChannels.put(i4, ((int) (System.currentTimeMillis() / 1000)) + org_telegram_tgnet_TLRPC_updates_ChannelDifference.timeout);
                                            }
                                            if (org_telegram_tgnet_TLRPC_updates_ChannelDifference.isFinal) {
                                                MessagesController.this.getChannelDifference(i4);
                                            }
                                            if (BuildVars.LOGS_ENABLED) {
                                                StringBuilder stringBuilder2 = new StringBuilder();
                                                stringBuilder2.append("received channel difference with pts = ");
                                                stringBuilder2.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.pts);
                                                stringBuilder2.append(" channelId = ");
                                                stringBuilder2.append(i4);
                                                FileLog.d(stringBuilder2.toString());
                                                stringBuilder2 = new StringBuilder();
                                                stringBuilder2.append("new_messages = ");
                                                stringBuilder2.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.new_messages.size());
                                                stringBuilder2.append(" messages = ");
                                                stringBuilder2.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.messages.size());
                                                stringBuilder2.append(" users = ");
                                                stringBuilder2.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.users.size());
                                                stringBuilder2.append(" chats = ");
                                                stringBuilder2.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.chats.size());
                                                stringBuilder2.append(" other updates = ");
                                                stringBuilder2.append(org_telegram_tgnet_TLRPC_updates_ChannelDifference.other_updates.size());
                                                FileLog.d(stringBuilder2.toString());
                                            }
                                            if (newTaskId != 0) {
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                                            }
                                        }
                                    });
                                }
                            });
                            return;
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                MessagesController.this.checkChannelError(error.text, i4);
                            }
                        });
                        MessagesController.this.gettingDifferenceChannels.delete(i4);
                        if (newTaskId != 0) {
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                        }
                    }
                };
                instance.sendRequest(req2, anonymousClass115);
                return;
            }
            if (j != 0) {
                MessagesStorage.getInstance(r7.currentAccount).removePendingTask(j);
            }
        }
    }

    private void checkChannelError(String text, int channelId) {
        int hashCode = text.hashCode();
        if (hashCode != -1809401834) {
            if (hashCode != -795226617) {
                if (hashCode == -471086771) {
                    if (text.equals("CHANNEL_PUBLIC_GROUP_NA")) {
                        hashCode = 1;
                        switch (hashCode) {
                            case 0:
                                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(0));
                                return;
                            case 1:
                                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(1));
                                return;
                            case 2:
                                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(2));
                                return;
                            default:
                                return;
                        }
                    }
                }
            } else if (text.equals("CHANNEL_PRIVATE")) {
                hashCode = 0;
                switch (hashCode) {
                    case 0:
                        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(0));
                        return;
                    case 1:
                        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(1));
                        return;
                    case 2:
                        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(2));
                        return;
                    default:
                        return;
                }
            }
        } else if (text.equals("USER_BANNED_IN_CHANNEL")) {
            hashCode = 2;
            switch (hashCode) {
                case 0:
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(0));
                    return;
                case 1:
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(1));
                    return;
                case 2:
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(2));
                    return;
                default:
                    return;
            }
        }
        hashCode = -1;
        switch (hashCode) {
            case 0:
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(0));
                return;
            case 1:
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(1));
                return;
            case 2:
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoCantLoad, Integer.valueOf(channelId), Integer.valueOf(2));
                return;
            default:
                return;
        }
    }

    public void getDifference() {
        getDifference(MessagesStorage.getInstance(this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(this.currentAccount).getLastQtsValue(), false);
    }

    public void getDifference(int pts, final int date, final int qts, boolean slice) {
        registerForPush(SharedConfig.pushString);
        if (MessagesStorage.getInstance(this.currentAccount).getLastPtsValue() == 0) {
            loadCurrentState();
        } else if (slice || !this.gettingDifference) {
            this.gettingDifference = true;
            TL_updates_getDifference req = new TL_updates_getDifference();
            req.pts = pts;
            req.date = date;
            req.qts = qts;
            if (this.getDifferenceFirstSync) {
                req.flags |= 1;
                if (ConnectionsManager.isConnectedOrConnectingToWiFi()) {
                    req.pts_total_limit = DefaultLoadControl.DEFAULT_BUFFER_FOR_PLAYBACK_AFTER_REBUFFER_MS;
                } else {
                    req.pts_total_limit = 1000;
                }
                this.getDifferenceFirstSync = false;
            }
            if (req.date == 0) {
                req.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            }
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("start getDifference with date = ");
                stringBuilder.append(date);
                stringBuilder.append(" pts = ");
                stringBuilder.append(pts);
                stringBuilder.append(" qts = ");
                stringBuilder.append(qts);
                FileLog.d(stringBuilder.toString());
            }
            ConnectionsManager.getInstance(this.currentAccount).setIsUpdating(true);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    int a = 0;
                    if (error == null) {
                        final updates_Difference res = (updates_Difference) response;
                        if (res instanceof TL_updates_differenceTooLong) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.loadedFullUsers.clear();
                                    MessagesController.this.loadedFullChats.clear();
                                    MessagesController.this.resetDialogs(true, MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastSeqValue(), res.pts, date, qts);
                                }
                            });
                        } else {
                            int a2;
                            if (res instanceof TL_updates_differenceSlice) {
                                MessagesController.this.getDifference(res.intermediate_state.pts, res.intermediate_state.date, res.intermediate_state.qts, true);
                            }
                            SparseArray<User> usersDict = new SparseArray();
                            SparseArray<Chat> chatsDict = new SparseArray();
                            for (a2 = 0; a2 < res.users.size(); a2++) {
                                User user = (User) res.users.get(a2);
                                usersDict.put(user.id, user);
                            }
                            for (a2 = 0; a2 < res.chats.size(); a2++) {
                                Chat chat = (Chat) res.chats.get(a2);
                                chatsDict.put(chat.id, chat);
                            }
                            ArrayList<TL_updateMessageID> msgUpdates = new ArrayList();
                            if (!res.other_updates.isEmpty()) {
                                while (a < res.other_updates.size()) {
                                    Update upd = (Update) res.other_updates.get(a);
                                    if (upd instanceof TL_updateMessageID) {
                                        msgUpdates.add((TL_updateMessageID) upd);
                                        res.other_updates.remove(a);
                                        a--;
                                    } else if (MessagesController.this.getUpdateType(upd) == 2) {
                                        int channelId = MessagesController.getUpdateChannelId(upd);
                                        int channelPts = MessagesController.this.channelsPts.get(channelId);
                                        if (channelPts == 0) {
                                            channelPts = MessagesStorage.getInstance(MessagesController.this.currentAccount).getChannelPtsSync(channelId);
                                            if (channelPts != 0) {
                                                MessagesController.this.channelsPts.put(channelId, channelPts);
                                            }
                                        }
                                        if (channelPts != 0 && MessagesController.getUpdatePts(upd) <= channelPts) {
                                            res.other_updates.remove(a);
                                            a--;
                                        }
                                    }
                                    a++;
                                }
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.loadedFullUsers.clear();
                                    MessagesController.this.loadedFullChats.clear();
                                    MessagesController.this.putUsers(res.users, false);
                                    MessagesController.this.putChats(res.chats, false);
                                }
                            });
                            final updates_Difference org_telegram_tgnet_TLRPC_updates_Difference = res;
                            final ArrayList<TL_updateMessageID> arrayList = msgUpdates;
                            final SparseArray<User> sparseArray = usersDict;
                            final SparseArray<Chat> sparseArray2 = chatsDict;
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                public void run() {
                                    int a = 0;
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(org_telegram_tgnet_TLRPC_updates_Difference.users, org_telegram_tgnet_TLRPC_updates_Difference.chats, true, false);
                                    if (!arrayList.isEmpty()) {
                                        final SparseArray<long[]> corrected = new SparseArray();
                                        while (true) {
                                            int a2 = a;
                                            if (a2 >= arrayList.size()) {
                                                break;
                                            }
                                            TL_updateMessageID update = (TL_updateMessageID) arrayList.get(a2);
                                            long[] ids = MessagesStorage.getInstance(MessagesController.this.currentAccount).updateMessageStateAndId(update.random_id, null, update.id, 0, false, 0);
                                            if (ids != null) {
                                                corrected.put(update.id, ids);
                                            }
                                            a = a2 + 1;
                                        }
                                        if (corrected.size() != 0) {
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                    for (int a = 0; a < corrected.size(); a++) {
                                                        int newId = corrected.keyAt(a);
                                                        SendMessagesHelper.getInstance(MessagesController.this.currentAccount).processSentMessage((int) ((long[]) corrected.valueAt(a))[1]);
                                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, Integer.valueOf(oldId), Integer.valueOf(newId), null, Long.valueOf(ids[0]));
                                                    }
                                                }
                                            });
                                        }
                                    }
                                    Utilities.stageQueue.postRunnable(new Runnable() {
                                        public void run() {
                                            boolean z = true;
                                            boolean z2 = false;
                                            if (!(org_telegram_tgnet_TLRPC_updates_Difference.new_messages.isEmpty() && org_telegram_tgnet_TLRPC_updates_Difference.new_encrypted_messages.isEmpty())) {
                                                final LongSparseArray<ArrayList<MessageObject>> messages = new LongSparseArray();
                                                for (int b = 0; b < org_telegram_tgnet_TLRPC_updates_Difference.new_encrypted_messages.size(); b++) {
                                                    ArrayList<Message> decryptedMessages = SecretChatHelper.getInstance(MessagesController.this.currentAccount).decryptMessage((EncryptedMessage) org_telegram_tgnet_TLRPC_updates_Difference.new_encrypted_messages.get(b));
                                                    if (!(decryptedMessages == null || decryptedMessages.isEmpty())) {
                                                        org_telegram_tgnet_TLRPC_updates_Difference.new_messages.addAll(decryptedMessages);
                                                    }
                                                }
                                                ImageLoader.saveMessagesThumbs(org_telegram_tgnet_TLRPC_updates_Difference.new_messages);
                                                final ArrayList<MessageObject> pushMessages = new ArrayList();
                                                int clientUserId = UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId();
                                                int a = 0;
                                                while (a < org_telegram_tgnet_TLRPC_updates_Difference.new_messages.size()) {
                                                    Message message = (Message) org_telegram_tgnet_TLRPC_updates_Difference.new_messages.get(a);
                                                    if (message.dialog_id == 0) {
                                                        if (message.to_id.chat_id != 0) {
                                                            message.dialog_id = (long) (-message.to_id.chat_id);
                                                        } else {
                                                            if (message.to_id.user_id == UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()) {
                                                                message.to_id.user_id = message.from_id;
                                                            }
                                                            message.dialog_id = (long) message.to_id.user_id;
                                                        }
                                                    }
                                                    if (((int) message.dialog_id) != 0) {
                                                        if (message.action instanceof TL_messageActionChatDeleteUser) {
                                                            User user = (User) sparseArray.get(message.action.user_id);
                                                            if (user != null && user.bot) {
                                                                message.reply_markup = new TL_replyKeyboardHide();
                                                                message.flags |= 64;
                                                            }
                                                        }
                                                        if (!(message.action instanceof TL_messageActionChatMigrateTo)) {
                                                            if (!(message.action instanceof TL_messageActionChannelCreate)) {
                                                                ConcurrentHashMap<Long, Integer> read_max = message.out ? MessagesController.this.dialogs_read_outbox_max : MessagesController.this.dialogs_read_inbox_max;
                                                                Integer value = (Integer) read_max.get(Long.valueOf(message.dialog_id));
                                                                if (value == null) {
                                                                    value = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                                                                    read_max.put(Long.valueOf(message.dialog_id), value);
                                                                }
                                                                message.unread = value.intValue() < message.id ? z : z2;
                                                            }
                                                        }
                                                        message.unread = z2;
                                                        message.media_unread = z2;
                                                    }
                                                    if (message.dialog_id == ((long) clientUserId)) {
                                                        message.unread = z2;
                                                        message.media_unread = z2;
                                                        message.out = z;
                                                    }
                                                    MessageObject obj = new MessageObject(MessagesController.this.currentAccount, message, sparseArray, sparseArray2, MessagesController.this.createdDialogIds.contains(Long.valueOf(message.dialog_id)));
                                                    if (!obj.isOut() && obj.isUnread()) {
                                                        pushMessages.add(obj);
                                                    }
                                                    ArrayList<MessageObject> arr = (ArrayList) messages.get(message.dialog_id);
                                                    if (arr == null) {
                                                        arr = new ArrayList();
                                                        messages.put(message.dialog_id, arr);
                                                    }
                                                    arr.add(obj);
                                                    a++;
                                                    z = true;
                                                    z2 = false;
                                                }
                                                AndroidUtilities.runOnUIThread(new Runnable() {
                                                    public void run() {
                                                        for (int a = 0; a < messages.size(); a++) {
                                                            MessagesController.this.updateInterfaceWithMessages(messages.keyAt(a), (ArrayList) messages.valueAt(a));
                                                        }
                                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                                    }
                                                });
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                                    public void run() {
                                                        if (!pushMessages.isEmpty()) {
                                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                                public void run() {
                                                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(pushMessages, !(org_telegram_tgnet_TLRPC_updates_Difference instanceof TL_updates_differenceSlice), false);
                                                                }
                                                            });
                                                        }
                                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(org_telegram_tgnet_TLRPC_updates_Difference.new_messages, true, false, false, DownloadController.getInstance(MessagesController.this.currentAccount).getAutodownloadMask());
                                                    }
                                                });
                                                SecretChatHelper.getInstance(MessagesController.this.currentAccount).processPendingEncMessages();
                                            }
                                            if (!org_telegram_tgnet_TLRPC_updates_Difference.other_updates.isEmpty()) {
                                                MessagesController.this.processUpdateArray(org_telegram_tgnet_TLRPC_updates_Difference.other_updates, org_telegram_tgnet_TLRPC_updates_Difference.users, org_telegram_tgnet_TLRPC_updates_Difference.chats, true);
                                            }
                                            int a2;
                                            if (org_telegram_tgnet_TLRPC_updates_Difference instanceof TL_updates_difference) {
                                                MessagesController.this.gettingDifference = false;
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastSeqValue(org_telegram_tgnet_TLRPC_updates_Difference.state.seq);
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastDateValue(org_telegram_tgnet_TLRPC_updates_Difference.state.date);
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastPtsValue(org_telegram_tgnet_TLRPC_updates_Difference.state.pts);
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastQtsValue(org_telegram_tgnet_TLRPC_updates_Difference.state.qts);
                                                ConnectionsManager.getInstance(MessagesController.this.currentAccount).setIsUpdating(false);
                                                int a3 = 0;
                                                while (true) {
                                                    a2 = a3;
                                                    if (a2 >= 3) {
                                                        break;
                                                    }
                                                    MessagesController.this.processUpdatesQueue(a2, 1);
                                                    a3 = a2 + 1;
                                                }
                                            } else if (org_telegram_tgnet_TLRPC_updates_Difference instanceof TL_updates_differenceSlice) {
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastDateValue(org_telegram_tgnet_TLRPC_updates_Difference.intermediate_state.date);
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastPtsValue(org_telegram_tgnet_TLRPC_updates_Difference.intermediate_state.pts);
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastQtsValue(org_telegram_tgnet_TLRPC_updates_Difference.intermediate_state.qts);
                                            } else if (org_telegram_tgnet_TLRPC_updates_Difference instanceof TL_updates_differenceEmpty) {
                                                MessagesController.this.gettingDifference = false;
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastSeqValue(org_telegram_tgnet_TLRPC_updates_Difference.seq);
                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).setLastDateValue(org_telegram_tgnet_TLRPC_updates_Difference.date);
                                                int a4 = 0;
                                                ConnectionsManager.getInstance(MessagesController.this.currentAccount).setIsUpdating(false);
                                                while (true) {
                                                    a2 = a4;
                                                    if (a2 >= 3) {
                                                        break;
                                                    }
                                                    MessagesController.this.processUpdatesQueue(a2, 1);
                                                    a4 = a2 + 1;
                                                }
                                            }
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).saveDiffParams(MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastDateValue(), MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastQtsValue());
                                            if (BuildVars.LOGS_ENABLED) {
                                                StringBuilder stringBuilder = new StringBuilder();
                                                stringBuilder.append("received difference with date = ");
                                                stringBuilder.append(MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastDateValue());
                                                stringBuilder.append(" pts = ");
                                                stringBuilder.append(MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastPtsValue());
                                                stringBuilder.append(" seq = ");
                                                stringBuilder.append(MessagesStorage.getInstance(MessagesController.this.currentAccount).getLastSeqValue());
                                                stringBuilder.append(" messages = ");
                                                stringBuilder.append(org_telegram_tgnet_TLRPC_updates_Difference.new_messages.size());
                                                stringBuilder.append(" users = ");
                                                stringBuilder.append(org_telegram_tgnet_TLRPC_updates_Difference.users.size());
                                                stringBuilder.append(" chats = ");
                                                stringBuilder.append(org_telegram_tgnet_TLRPC_updates_Difference.chats.size());
                                                stringBuilder.append(" other updates = ");
                                                stringBuilder.append(org_telegram_tgnet_TLRPC_updates_Difference.other_updates.size());
                                                FileLog.d(stringBuilder.toString());
                                            }
                                        }
                                    });
                                }
                            });
                        }
                        return;
                    }
                    MessagesController.this.gettingDifference = false;
                    ConnectionsManager.getInstance(MessagesController.this.currentAccount).setIsUpdating(false);
                }
            });
        }
    }

    public boolean canPinDialog(boolean secret) {
        int count = 0;
        for (int a = 0; a < this.dialogs.size(); a++) {
            TL_dialog dialog = (TL_dialog) this.dialogs.get(a);
            int lower_id = (int) dialog.id;
            if (!secret || lower_id == 0) {
                if (secret || lower_id != 0) {
                    if (dialog.pinned) {
                        count++;
                    }
                }
            }
        }
        if (count < this.maxPinnedDialogsCount) {
            return true;
        }
        return false;
    }

    public boolean pinDialog(long did, boolean pin, InputPeer peer, long taskId) {
        int lower_id = (int) did;
        TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(did);
        boolean z = false;
        if (dialog != null) {
            if (dialog.pinned != pin) {
                dialog.pinned = pin;
                if (pin) {
                    int maxPinnedNum = 0;
                    for (int a = 0; a < this.dialogs.size(); a++) {
                        TL_dialog d = (TL_dialog) this.dialogs.get(a);
                        if (!d.pinned) {
                            break;
                        }
                        maxPinnedNum = Math.max(d.pinnedNum, maxPinnedNum);
                    }
                    dialog.pinnedNum = maxPinnedNum + 1;
                } else {
                    dialog.pinnedNum = 0;
                }
                NativeByteBuffer data = null;
                sortDialogs(null);
                if (!pin && this.dialogs.get(this.dialogs.size() - 1) == dialog) {
                    this.dialogs.remove(this.dialogs.size() - 1);
                }
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                if (!(lower_id == 0 || taskId == -1)) {
                    TL_messages_toggleDialogPin req = new TL_messages_toggleDialogPin();
                    req.pinned = pin;
                    if (peer == null) {
                        peer = getInputPeer(lower_id);
                    }
                    if (peer instanceof TL_inputPeerEmpty) {
                        return false;
                    }
                    long newTaskId;
                    TL_inputDialogPeer inputDialogPeer = new TL_inputDialogPeer();
                    inputDialogPeer.peer = peer;
                    req.peer = inputDialogPeer;
                    if (taskId == 0) {
                        try {
                            data = new NativeByteBuffer(16 + peer.getObjectSize());
                            data.writeInt32(1);
                            data.writeInt64(did);
                            data.writeBool(pin);
                            peer.serializeToStream(data);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                    } else {
                        newTaskId = taskId;
                    }
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            if (newTaskId != 0) {
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                            }
                        }
                    });
                }
                MessagesStorage.getInstance(this.currentAccount).setDialogPinned(did, dialog.pinnedNum);
                return true;
            }
        }
        if (dialog != null) {
            z = true;
        }
        return z;
    }

    public void loadPinnedDialogs(final long newDialogId, final ArrayList<Long> order) {
        if (!UserConfig.getInstance(this.currentAccount).pinnedDialogsLoaded) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_messages_getPinnedDialogs(), new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    AnonymousClass118 anonymousClass118 = this;
                    if (response != null) {
                        int a;
                        Chat chat;
                        MessageObject messageObject;
                        TL_messages_peerDialogs res = (TL_messages_peerDialogs) response;
                        TL_messages_dialogs toCache = new TL_messages_dialogs();
                        toCache.users.addAll(res.users);
                        toCache.chats.addAll(res.chats);
                        toCache.dialogs.addAll(res.dialogs);
                        toCache.messages.addAll(res.messages);
                        LongSparseArray<MessageObject> new_dialogMessage = new LongSparseArray();
                        SparseArray<User> usersDict = new SparseArray();
                        SparseArray<Chat> chatsDict = new SparseArray();
                        final ArrayList<Long> arrayList = new ArrayList();
                        for (a = 0; a < res.users.size(); a++) {
                            User u = (User) res.users.get(a);
                            usersDict.put(u.id, u);
                        }
                        for (a = 0; a < res.chats.size(); a++) {
                            Chat c = (Chat) res.chats.get(a);
                            chatsDict.put(c.id, c);
                        }
                        for (a = 0; a < res.messages.size(); a++) {
                            Message message = (Message) res.messages.get(a);
                            if (message.to_id.channel_id != 0) {
                                chat = (Chat) chatsDict.get(message.to_id.channel_id);
                                if (chat != null && chat.left) {
                                }
                            } else if (message.to_id.chat_id != 0) {
                                chat = (Chat) chatsDict.get(message.to_id.chat_id);
                                if (!(chat == null || chat.migrated_to == null)) {
                                }
                            }
                            messageObject = new MessageObject(MessagesController.this.currentAccount, message, (SparseArray) usersDict, (SparseArray) chatsDict, false);
                            new_dialogMessage.put(messageObject.getDialogId(), messageObject);
                        }
                        for (a = 0; a < res.dialogs.size(); a++) {
                            TL_dialog d = (TL_dialog) res.dialogs.get(a);
                            if (d.id == 0) {
                                if (d.peer.user_id != 0) {
                                    d.id = (long) d.peer.user_id;
                                } else if (d.peer.chat_id != 0) {
                                    d.id = (long) (-d.peer.chat_id);
                                } else if (d.peer.channel_id != 0) {
                                    d.id = (long) (-d.peer.channel_id);
                                }
                            }
                            arrayList.add(Long.valueOf(d.id));
                            if (DialogObject.isChannel(d)) {
                                chat = (Chat) chatsDict.get(-((int) d.id));
                                if (chat != null && chat.left) {
                                }
                            } else if (((int) d.id) < 0) {
                                chat = (Chat) chatsDict.get(-((int) d.id));
                                if (!(chat == null || chat.migrated_to == null)) {
                                }
                            }
                            if (d.last_message_date == 0) {
                                messageObject = (MessageObject) new_dialogMessage.get(d.id);
                                if (messageObject != null) {
                                    d.last_message_date = messageObject.messageOwner.date;
                                }
                            }
                            Integer value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(d.id));
                            if (value == null) {
                                value = Integer.valueOf(0);
                            }
                            MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_inbox_max_id)));
                            value = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(d.id));
                            if (value == null) {
                                value = Integer.valueOf(0);
                            }
                            MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_outbox_max_id)));
                        }
                        final TL_messages_peerDialogs tL_messages_peerDialogs = res;
                        ArrayList<Long> newPinnedOrder = arrayList;
                        final LongSparseArray<MessageObject> longSparseArray = new_dialogMessage;
                        final TL_messages_dialogs usersDict2 = toCache;
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                            public void run() {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.MessagesController.118.1.1.run():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
                                        r0 = r19;
                                        r1 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r1 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r1 = org.telegram.messenger.MessagesController.this;
                                        r2 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r2 = r2;
                                        r2 = r2.dialogs;
                                        r1.applyDialogsNotificationsSettings(r2);
                                        r1 = 0;
                                        r2 = 0;
                                        r3 = 0;
                                        r4 = new android.util.LongSparseArray;
                                        r4.<init>();
                                        r5 = new java.util.ArrayList;
                                        r5.<init>();
                                        r6 = 0;
                                        r7 = r1;
                                        r1 = r6;
                                    L_0x0021:
                                        r8 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r8 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r8 = org.telegram.messenger.MessagesController.this;
                                        r8 = r8.dialogs;
                                        r8 = r8.size();
                                        if (r1 >= r8) goto L_0x006a;
                                    L_0x002f:
                                        r8 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r8 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r8 = org.telegram.messenger.MessagesController.this;
                                        r8 = r8.dialogs;
                                        r8 = r8.get(r1);
                                        r8 = (org.telegram.tgnet.TLRPC.TL_dialog) r8;
                                        r9 = r8.id;
                                        r9 = (int) r9;
                                        if (r9 != 0) goto L_0x0043;
                                    L_0x0042:
                                        goto L_0x0067;
                                    L_0x0043:
                                        r9 = r8.pinned;
                                        if (r9 != 0) goto L_0x0048;
                                    L_0x0047:
                                        goto L_0x006a;
                                    L_0x0048:
                                        r9 = r8.pinnedNum;
                                        r3 = java.lang.Math.max(r9, r3);
                                        r9 = r8.id;
                                        r11 = r8.pinnedNum;
                                        r11 = java.lang.Integer.valueOf(r11);
                                        r4.put(r9, r11);
                                        r9 = r8.id;
                                        r9 = java.lang.Long.valueOf(r9);
                                        r5.add(r9);
                                        r8.pinned = r6;
                                        r8.pinnedNum = r6;
                                        r7 = 1;
                                    L_0x0067:
                                        r1 = r1 + 1;
                                        goto L_0x0021;
                                    L_0x006a:
                                        r1 = new java.util.ArrayList;
                                        r1.<init>();
                                        r8 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r8 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r8 = r6;
                                        if (r8 == 0) goto L_0x007e;
                                    L_0x0077:
                                        r8 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r8 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r8 = r6;
                                        goto L_0x0082;
                                    L_0x007e:
                                        r8 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r8 = r3;
                                    L_0x0082:
                                        r9 = r8.size();
                                        r10 = r5.size();
                                        r11 = 0;
                                        if (r9 >= r10) goto L_0x0095;
                                    L_0x008e:
                                        r9 = java.lang.Long.valueOf(r11);
                                        r8.add(r9);
                                    L_0x0095:
                                        r9 = r5.size();
                                        r10 = r8.size();
                                        if (r9 >= r10) goto L_0x00a7;
                                    L_0x009f:
                                        r9 = java.lang.Long.valueOf(r11);
                                        r5.add(r6, r9);
                                        goto L_0x0095;
                                    L_0x00a7:
                                        r9 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r9 = r2;
                                        r9 = r9.dialogs;
                                        r9 = r9.isEmpty();
                                        r10 = 1;
                                        if (r9 != 0) goto L_0x01fe;
                                    L_0x00b4:
                                        r9 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r9 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r9 = org.telegram.messenger.MessagesController.this;
                                        r13 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r13 = r2;
                                        r13 = r13.users;
                                        r9.putUsers(r13, r6);
                                        r9 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r9 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r9 = org.telegram.messenger.MessagesController.this;
                                        r13 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r13 = r2;
                                        r13 = r13.chats;
                                        r9.putChats(r13, r6);
                                        r9 = r2;
                                        r2 = r6;
                                        r13 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r13 = r2;
                                        r13 = r13.dialogs;
                                        r13 = r13.size();
                                        if (r2 >= r13) goto L_0x01fa;
                                    L_0x00e0:
                                        r13 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r13 = r2;
                                        r13 = r13.dialogs;
                                        r13 = r13.get(r2);
                                        r13 = (org.telegram.tgnet.TLRPC.TL_dialog) r13;
                                        r14 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r14 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r14 = r4;
                                        r16 = (r14 > r11 ? 1 : (r14 == r11 ? 0 : -1));
                                        if (r16 == 0) goto L_0x010a;
                                    L_0x00f6:
                                        r14 = r13.id;
                                        r14 = r4.get(r14);
                                        r14 = (java.lang.Integer) r14;
                                        if (r14 == 0) goto L_0x0106;
                                    L_0x0100:
                                        r15 = r14.intValue();
                                        r13.pinnedNum = r15;
                                        r17 = r7;
                                        goto L_0x0150;
                                    L_0x010a:
                                        r14 = r13.id;
                                        r14 = java.lang.Long.valueOf(r14);
                                        r14 = r5.indexOf(r14);
                                        r17 = r7;
                                        r6 = r13.id;
                                        r6 = java.lang.Long.valueOf(r6);
                                        r6 = r8.indexOf(r6);
                                        r7 = -1;
                                        if (r14 == r7) goto L_0x0150;
                                        if (r6 == r7) goto L_0x0150;
                                        if (r14 != r6) goto L_0x0138;
                                        r11 = r13.id;
                                        r7 = r4.get(r11);
                                        r7 = (java.lang.Integer) r7;
                                        if (r7 == 0) goto L_0x0137;
                                        r11 = r7.intValue();
                                        r13.pinnedNum = r11;
                                        goto L_0x0150;
                                        r7 = r5.get(r6);
                                        r7 = (java.lang.Long) r7;
                                        r11 = r7.longValue();
                                        r7 = r4.get(r11);
                                        r7 = (java.lang.Integer) r7;
                                        if (r7 == 0) goto L_0x0150;
                                        r15 = r7.intValue();
                                        r13.pinnedNum = r15;
                                        r6 = r13.pinnedNum;
                                        if (r6 != 0) goto L_0x0162;
                                        r6 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r6 = r2;
                                        r6 = r6.dialogs;
                                        r6 = r6.size();
                                        r6 = r6 - r2;
                                        r6 = r6 + r3;
                                        r13.pinnedNum = r6;
                                        r6 = r13.id;
                                        r6 = java.lang.Long.valueOf(r6);
                                        r1.add(r6);
                                        r6 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r6 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r6 = org.telegram.messenger.MessagesController.this;
                                        r6 = r6.dialogs_dict;
                                        r11 = r13.id;
                                        r6 = r6.get(r11);
                                        r6 = (org.telegram.tgnet.TLRPC.TL_dialog) r6;
                                        if (r6 == 0) goto L_0x0199;
                                        r6.pinned = r10;
                                        r7 = r13.pinnedNum;
                                        r6.pinnedNum = r7;
                                        r7 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r7 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r7 = org.telegram.messenger.MessagesController.this;
                                        r7 = r7.currentAccount;
                                        r7 = org.telegram.messenger.MessagesStorage.getInstance(r7);
                                        r11 = r13.id;
                                        r14 = r13.pinnedNum;
                                        r7.setDialogPinned(r11, r14);
                                        goto L_0x01f2;
                                        r9 = 1;
                                        r7 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r7 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r7 = org.telegram.messenger.MessagesController.this;
                                        r7 = r7.dialogs_dict;
                                        r11 = r13.id;
                                        r7.put(r11, r13);
                                        r7 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r7 = r4;
                                        r11 = r13.id;
                                        r7 = r7.get(r11);
                                        r7 = (org.telegram.messenger.MessageObject) r7;
                                        r11 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r11 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r11 = org.telegram.messenger.MessagesController.this;
                                        r11 = r11.dialogMessage;
                                        r14 = r13.id;
                                        r11.put(r14, r7);
                                        if (r7 == 0) goto L_0x01f2;
                                        r11 = r7.messageOwner;
                                        r11 = r11.to_id;
                                        r11 = r11.channel_id;
                                        if (r11 != 0) goto L_0x01f2;
                                        r11 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r11 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r11 = org.telegram.messenger.MessagesController.this;
                                        r11 = r11.dialogMessagesByIds;
                                        r12 = r7.getId();
                                        r11.put(r12, r7);
                                        r11 = r7.messageOwner;
                                        r11 = r11.random_id;
                                        r14 = 0;
                                        r16 = (r11 > r14 ? 1 : (r11 == r14 ? 0 : -1));
                                        if (r16 == 0) goto L_0x01f2;
                                        r11 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r11 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r11 = org.telegram.messenger.MessagesController.this;
                                        r11 = r11.dialogMessagesByRandomIds;
                                        r12 = r7.messageOwner;
                                        r14 = r12.random_id;
                                        r11.put(r14, r7);
                                        r7 = 1;
                                        r2 = r2 + 1;
                                        r6 = 0;
                                        r11 = 0;
                                        goto L_0x00d4;
                                    L_0x01fa:
                                        r17 = r7;
                                        r2 = r9;
                                        goto L_0x0200;
                                    L_0x01fe:
                                        r17 = r7;
                                        if (r17 == 0) goto L_0x0258;
                                        if (r2 == 0) goto L_0x0238;
                                        r6 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r6 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r6 = org.telegram.messenger.MessagesController.this;
                                        r6 = r6.dialogs;
                                        r6.clear();
                                        r6 = 0;
                                        r7 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r7 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r7 = org.telegram.messenger.MessagesController.this;
                                        r7 = r7.dialogs_dict;
                                        r7 = r7.size();
                                        if (r6 >= r7) goto L_0x0238;
                                        r9 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r9 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r9 = org.telegram.messenger.MessagesController.this;
                                        r9 = r9.dialogs;
                                        r11 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r11 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r11 = org.telegram.messenger.MessagesController.this;
                                        r11 = r11.dialogs_dict;
                                        r11 = r11.valueAt(r6);
                                        r9.add(r11);
                                        r6 = r6 + 1;
                                        goto L_0x021c;
                                        r6 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r6 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r6 = org.telegram.messenger.MessagesController.this;
                                        r7 = 0;
                                        r6.sortDialogs(r7);
                                        r6 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r6 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r6 = org.telegram.messenger.MessagesController.this;
                                        r6 = r6.currentAccount;
                                        r6 = org.telegram.messenger.NotificationCenter.getInstance(r6);
                                        r7 = org.telegram.messenger.NotificationCenter.dialogsNeedReload;
                                        r9 = 0;
                                        r11 = new java.lang.Object[r9];
                                        r6.postNotificationName(r7, r11);
                                        r6 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r6 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r6 = org.telegram.messenger.MessagesController.this;
                                        r6 = r6.currentAccount;
                                        r6 = org.telegram.messenger.MessagesStorage.getInstance(r6);
                                        r6.unpinAllDialogsExceptNew(r1);
                                        r6 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r6 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r6 = org.telegram.messenger.MessagesController.this;
                                        r6 = r6.currentAccount;
                                        r6 = org.telegram.messenger.MessagesStorage.getInstance(r6);
                                        r7 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r7 = r5;
                                        r6.putDialogs(r7, r10);
                                        r6 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r6 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r6 = org.telegram.messenger.MessagesController.this;
                                        r6 = r6.currentAccount;
                                        r6 = org.telegram.messenger.UserConfig.getInstance(r6);
                                        r6.pinnedDialogsLoaded = r10;
                                        r6 = org.telegram.messenger.MessagesController.118.AnonymousClass1.this;
                                        r6 = org.telegram.messenger.MessagesController.AnonymousClass118.this;
                                        r6 = org.telegram.messenger.MessagesController.this;
                                        r6 = r6.currentAccount;
                                        r6 = org.telegram.messenger.UserConfig.getInstance(r6);
                                        r7 = 0;
                                        r6.saveConfig(r7);
                                        return;
                                        */
                                        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.MessagesController.118.1.1.run():void");
                                    }
                                });
                            }
                        });
                    }
                }
            });
        }
    }

    public void generateJoinMessage(final int chat_id, boolean ignoreLeft) {
        Chat chat = getChat(Integer.valueOf(chat_id));
        if (chat != null && ChatObject.isChannel(chat_id, this.currentAccount)) {
            if ((!chat.left && !chat.kicked) || ignoreLeft) {
                TL_messageService message = new TL_messageService();
                message.flags = 256;
                int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                message.id = newMessageId;
                message.local_id = newMessageId;
                message.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                message.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                message.to_id = new TL_peerChannel();
                message.to_id.channel_id = chat_id;
                message.dialog_id = (long) (-chat_id);
                message.post = true;
                message.action = new TL_messageActionChatAddUser();
                message.action.users.add(Integer.valueOf(UserConfig.getInstance(this.currentAccount).getClientUserId()));
                if (chat.megagroup) {
                    message.flags |= Integer.MIN_VALUE;
                }
                UserConfig.getInstance(this.currentAccount).saveConfig(false);
                final ArrayList<MessageObject> pushMessages = new ArrayList();
                ArrayList<Message> messagesArr = new ArrayList();
                messagesArr.add(message);
                pushMessages.add(new MessageObject(this.currentAccount, message, true));
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                    public void run() {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(pushMessages, true, false);
                            }
                        });
                    }
                });
                MessagesStorage.getInstance(this.currentAccount).putMessages((ArrayList) messagesArr, true, true, false, 0);
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        MessagesController.this.updateInterfaceWithMessages((long) (-chat_id), pushMessages);
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                    }
                });
            }
        }
    }

    public void checkChannelInviter(final int chat_id) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                final Chat chat = MessagesController.this.getChat(Integer.valueOf(chat_id));
                if (chat != null && ChatObject.isChannel(chat_id, MessagesController.this.currentAccount)) {
                    if (!chat.creator) {
                        TL_channels_getParticipant req = new TL_channels_getParticipant();
                        req.channel = MessagesController.this.getInputChannel(chat_id);
                        req.user_id = new TL_inputUserSelf();
                        ConnectionsManager.getInstance(MessagesController.this.currentAccount).sendRequest(req, new RequestDelegate() {
                            public void run(TLObject response, TL_error error) {
                                final TL_channels_channelParticipant res = (TL_channels_channelParticipant) response;
                                if (res != null && (res.participant instanceof TL_channelParticipantSelf) && res.participant.inviter_id != UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId() && (!chat.megagroup || !MessagesStorage.getInstance(MessagesController.this.currentAccount).isMigratedChat(chat.id))) {
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.this.putUsers(res.users, false);
                                        }
                                    });
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, null, true, true);
                                    Message message = new TL_messageService();
                                    message.media_unread = true;
                                    message.unread = true;
                                    message.flags = 256;
                                    message.post = true;
                                    if (chat.megagroup) {
                                        message.flags |= Integer.MIN_VALUE;
                                    }
                                    int newMessageId = UserConfig.getInstance(MessagesController.this.currentAccount).getNewMessageId();
                                    message.id = newMessageId;
                                    message.local_id = newMessageId;
                                    message.date = res.participant.date;
                                    message.action = new TL_messageActionChatAddUser();
                                    message.from_id = res.participant.inviter_id;
                                    message.action.users.add(Integer.valueOf(UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()));
                                    message.to_id = new TL_peerChannel();
                                    message.to_id.channel_id = chat_id;
                                    message.dialog_id = (long) (-chat_id);
                                    int a = 0;
                                    UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
                                    final ArrayList<MessageObject> pushMessages = new ArrayList();
                                    ArrayList<Message> messagesArr = new ArrayList();
                                    AbstractMap usersDict = new ConcurrentHashMap();
                                    while (a < res.users.size()) {
                                        User user = (User) res.users.get(a);
                                        usersDict.put(Integer.valueOf(user.id), user);
                                        a++;
                                    }
                                    messagesArr.add(message);
                                    pushMessages.add(new MessageObject(MessagesController.this.currentAccount, message, usersDict, true));
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                        public void run() {
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(pushMessages, true, false);
                                                }
                                            });
                                        }
                                    });
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages((ArrayList) messagesArr, true, true, false, 0);
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.this.updateInterfaceWithMessages((long) (-chat_id), pushMessages);
                                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private int getUpdateType(Update update) {
        if (!((update instanceof TL_updateNewMessage) || (update instanceof TL_updateReadMessagesContents) || (update instanceof TL_updateReadHistoryInbox) || (update instanceof TL_updateReadHistoryOutbox) || (update instanceof TL_updateDeleteMessages) || (update instanceof TL_updateWebPage))) {
            if (!(update instanceof TL_updateEditMessage)) {
                if (update instanceof TL_updateNewEncryptedMessage) {
                    return 1;
                }
                if (!((update instanceof TL_updateNewChannelMessage) || (update instanceof TL_updateDeleteChannelMessages) || (update instanceof TL_updateEditChannelMessage))) {
                    if (!(update instanceof TL_updateChannelWebPage)) {
                        return 3;
                    }
                }
                return 2;
            }
        }
        return 0;
    }

    private static int getUpdatePts(Update update) {
        if (update instanceof TL_updateDeleteMessages) {
            return ((TL_updateDeleteMessages) update).pts;
        }
        if (update instanceof TL_updateNewChannelMessage) {
            return ((TL_updateNewChannelMessage) update).pts;
        }
        if (update instanceof TL_updateReadHistoryOutbox) {
            return ((TL_updateReadHistoryOutbox) update).pts;
        }
        if (update instanceof TL_updateNewMessage) {
            return ((TL_updateNewMessage) update).pts;
        }
        if (update instanceof TL_updateEditMessage) {
            return ((TL_updateEditMessage) update).pts;
        }
        if (update instanceof TL_updateWebPage) {
            return ((TL_updateWebPage) update).pts;
        }
        if (update instanceof TL_updateReadHistoryInbox) {
            return ((TL_updateReadHistoryInbox) update).pts;
        }
        if (update instanceof TL_updateChannelWebPage) {
            return ((TL_updateChannelWebPage) update).pts;
        }
        if (update instanceof TL_updateDeleteChannelMessages) {
            return ((TL_updateDeleteChannelMessages) update).pts;
        }
        if (update instanceof TL_updateEditChannelMessage) {
            return ((TL_updateEditChannelMessage) update).pts;
        }
        if (update instanceof TL_updateReadMessagesContents) {
            return ((TL_updateReadMessagesContents) update).pts;
        }
        if (update instanceof TL_updateChannelTooLong) {
            return ((TL_updateChannelTooLong) update).pts;
        }
        return 0;
    }

    private static int getUpdatePtsCount(Update update) {
        if (update instanceof TL_updateDeleteMessages) {
            return ((TL_updateDeleteMessages) update).pts_count;
        }
        if (update instanceof TL_updateNewChannelMessage) {
            return ((TL_updateNewChannelMessage) update).pts_count;
        }
        if (update instanceof TL_updateReadHistoryOutbox) {
            return ((TL_updateReadHistoryOutbox) update).pts_count;
        }
        if (update instanceof TL_updateNewMessage) {
            return ((TL_updateNewMessage) update).pts_count;
        }
        if (update instanceof TL_updateEditMessage) {
            return ((TL_updateEditMessage) update).pts_count;
        }
        if (update instanceof TL_updateWebPage) {
            return ((TL_updateWebPage) update).pts_count;
        }
        if (update instanceof TL_updateReadHistoryInbox) {
            return ((TL_updateReadHistoryInbox) update).pts_count;
        }
        if (update instanceof TL_updateChannelWebPage) {
            return ((TL_updateChannelWebPage) update).pts_count;
        }
        if (update instanceof TL_updateDeleteChannelMessages) {
            return ((TL_updateDeleteChannelMessages) update).pts_count;
        }
        if (update instanceof TL_updateEditChannelMessage) {
            return ((TL_updateEditChannelMessage) update).pts_count;
        }
        if (update instanceof TL_updateReadMessagesContents) {
            return ((TL_updateReadMessagesContents) update).pts_count;
        }
        return 0;
    }

    private static int getUpdateQts(Update update) {
        if (update instanceof TL_updateNewEncryptedMessage) {
            return ((TL_updateNewEncryptedMessage) update).qts;
        }
        return 0;
    }

    private static int getUpdateChannelId(Update update) {
        if (update instanceof TL_updateNewChannelMessage) {
            return ((TL_updateNewChannelMessage) update).message.to_id.channel_id;
        }
        if (update instanceof TL_updateEditChannelMessage) {
            return ((TL_updateEditChannelMessage) update).message.to_id.channel_id;
        }
        if (update instanceof TL_updateReadChannelOutbox) {
            return ((TL_updateReadChannelOutbox) update).channel_id;
        }
        if (update instanceof TL_updateChannelMessageViews) {
            return ((TL_updateChannelMessageViews) update).channel_id;
        }
        if (update instanceof TL_updateChannelTooLong) {
            return ((TL_updateChannelTooLong) update).channel_id;
        }
        if (update instanceof TL_updateChannelPinnedMessage) {
            return ((TL_updateChannelPinnedMessage) update).channel_id;
        }
        if (update instanceof TL_updateChannelReadMessagesContents) {
            return ((TL_updateChannelReadMessagesContents) update).channel_id;
        }
        if (update instanceof TL_updateChannelAvailableMessages) {
            return ((TL_updateChannelAvailableMessages) update).channel_id;
        }
        if (update instanceof TL_updateChannel) {
            return ((TL_updateChannel) update).channel_id;
        }
        if (update instanceof TL_updateChannelWebPage) {
            return ((TL_updateChannelWebPage) update).channel_id;
        }
        if (update instanceof TL_updateDeleteChannelMessages) {
            return ((TL_updateDeleteChannelMessages) update).channel_id;
        }
        if (update instanceof TL_updateReadChannelInbox) {
            return ((TL_updateReadChannelInbox) update).channel_id;
        }
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("trying to get unknown update channel_id for ");
            stringBuilder.append(update);
            FileLog.e(stringBuilder.toString());
        }
        return 0;
    }

    public void processUpdates(Updates updates, boolean fromQueue) {
        boolean z;
        int a;
        int channelId;
        TL_messages_receivedQueue req;
        MessagesController messagesController = this;
        final Updates updates2 = updates;
        ArrayList<Integer> needGetChannelsDiff = null;
        boolean needGetDiff = false;
        boolean needReceivedQueue = false;
        boolean updateStatus = false;
        if (updates2 instanceof TL_updateShort) {
            ArrayList<Update> arr = new ArrayList();
            arr.add(updates2.update);
            processUpdateArray(arr, null, null, false);
        } else {
            int i;
            boolean skipUpdate;
            int b;
            int pts2;
            boolean z2;
            ArrayList<Integer> needGetChannelsDiff2;
            boolean needGetDiff2;
            boolean needReceivedQueue2;
            int i2 = 1;
            if (updates2 instanceof TL_updateShortChatMessage) {
                z = false;
            } else if (updates2 instanceof TL_updateShortMessage) {
                z = false;
            } else {
                Update update;
                int channelId2;
                if (!(updates2 instanceof TL_updatesCombined)) {
                    if (!(updates2 instanceof TL_updates)) {
                        if (updates2 instanceof TL_updatesTooLong) {
                            if (BuildVars.LOGS_ENABLED) {
                                FileLog.d("need get diff TL_updatesTooLong");
                            }
                            needGetDiff = true;
                        } else if (updates2 instanceof UserActionUpdatesSeq) {
                            MessagesStorage.getInstance(messagesController.currentAccount).setLastSeqValue(updates2.seq);
                        } else if (updates2 instanceof UserActionUpdatesPts) {
                            if (updates2.chat_id != 0) {
                                messagesController.channelsPts.put(updates2.chat_id, updates2.pts);
                                MessagesStorage.getInstance(messagesController.currentAccount).saveChannelPts(updates2.chat_id, updates2.pts);
                            } else {
                                MessagesStorage.getInstance(messagesController.currentAccount).setLastPtsValue(updates2.pts);
                            }
                        }
                    }
                }
                SparseArray<Chat> minChannels = null;
                for (a = 0; a < updates2.chats.size(); a++) {
                    Chat chat = (Chat) updates2.chats.get(a);
                    if ((chat instanceof TL_channel) && chat.min) {
                        Chat existChat = getChat(Integer.valueOf(chat.id));
                        if (existChat == null || existChat.min) {
                            Chat cacheChat = MessagesStorage.getInstance(messagesController.currentAccount).getChatSync(updates2.chat_id);
                            putChat(cacheChat, true);
                            existChat = cacheChat;
                        }
                        if (existChat == null || existChat.min) {
                            if (minChannels == null) {
                                minChannels = new SparseArray();
                            }
                            minChannels.put(chat.id, chat);
                        }
                    }
                }
                if (minChannels != null) {
                    for (a = 0; a < updates2.updates.size(); a++) {
                        update = (Update) updates2.updates.get(a);
                        if (update instanceof TL_updateNewChannelMessage) {
                            channelId2 = ((TL_updateNewChannelMessage) update).message.to_id.channel_id;
                            if (minChannels.indexOfKey(channelId2) >= 0) {
                                if (BuildVars.LOGS_ENABLED) {
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("need get diff because of min channel ");
                                    stringBuilder.append(channelId2);
                                    FileLog.d(stringBuilder.toString());
                                }
                                needGetDiff = true;
                            }
                        }
                    }
                }
                if (needGetDiff) {
                    z = false;
                } else {
                    SparseArray<Chat> minChannels2;
                    boolean needGetDiff3;
                    boolean needReceivedQueue3;
                    StringBuilder stringBuilder2;
                    boolean processUpdate;
                    MessagesStorage.getInstance(messagesController.currentAccount).putUsersAndChats(updates2.users, updates2.chats, true, true);
                    Collections.sort(updates2.updates, messagesController.updatesComparator);
                    ArrayList<Integer> needGetChannelsDiff3 = null;
                    int a2 = 0;
                    while (a2 < updates2.updates.size()) {
                        update = (Update) updates2.updates.get(a2);
                        if (getUpdateType(update) != 0) {
                            minChannels2 = minChannels;
                            int channelPts;
                            if (getUpdateType(update) != 1) {
                                if (getUpdateType(update) != 2) {
                                    needGetDiff3 = needGetDiff;
                                    needReceivedQueue3 = needReceivedQueue;
                                    z = updateStatus;
                                    i = 1;
                                    break;
                                }
                                channelId = getUpdateChannelId(update);
                                skipUpdate = false;
                                channelPts = messagesController.channelsPts.get(channelId);
                                if (channelPts == 0) {
                                    channelPts = MessagesStorage.getInstance(messagesController.currentAccount).getChannelPtsSync(channelId);
                                    if (channelPts == 0) {
                                        for (i2 = 0; i2 < updates2.chats.size(); i2++) {
                                            Chat chat2 = (Chat) updates2.chats.get(i2);
                                            if (chat2.id == channelId) {
                                                loadUnknownChannel(chat2, 0);
                                                skipUpdate = true;
                                                break;
                                            }
                                        }
                                    } else {
                                        messagesController.channelsPts.put(channelId, channelPts);
                                    }
                                }
                                TL_updates updatesNew = new TL_updates();
                                updatesNew.updates.add(update);
                                updatesNew.pts = getUpdatePts(update);
                                updatesNew.pts_count = getUpdatePtsCount(update);
                                b = a2 + 1;
                                while (b < updates2.updates.size()) {
                                    Update update2 = (Update) updates2.updates.get(b);
                                    pts2 = getUpdatePts(update2);
                                    int count2 = getUpdatePtsCount(update2);
                                    needGetDiff3 = needGetDiff;
                                    if (!getUpdateType(update2) || channelId != getUpdateChannelId(update2) || updatesNew.pts + count2 != pts2) {
                                        break;
                                    }
                                    updatesNew.updates.add(update2);
                                    updatesNew.pts = pts2;
                                    updatesNew.pts_count += count2;
                                    updates2.updates.remove(b);
                                    b = (b - 1) + 1;
                                    needGetDiff = needGetDiff3;
                                }
                                needGetDiff3 = needGetDiff;
                                if (skipUpdate) {
                                    needReceivedQueue3 = needReceivedQueue;
                                    z = updateStatus;
                                    if (BuildVars.LOGS_ENABLED) {
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("need load unknown channel = ");
                                        stringBuilder2.append(channelId);
                                        FileLog.d(stringBuilder2.toString());
                                    }
                                } else if (updatesNew.pts_count + channelPts == updatesNew.pts) {
                                    if (processUpdateArray(updatesNew.updates, updates2.users, updates2.chats, false)) {
                                        messagesController.channelsPts.put(channelId, updatesNew.pts);
                                        MessagesStorage.getInstance(messagesController.currentAccount).saveChannelPts(channelId, updatesNew.pts);
                                    } else {
                                        if (BuildVars.LOGS_ENABLED) {
                                            stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append("need get channel diff inner TL_updates, channel_id = ");
                                            stringBuilder2.append(channelId);
                                            FileLog.d(stringBuilder2.toString());
                                        }
                                        if (needGetChannelsDiff3 == null) {
                                            needGetChannelsDiff3 = new ArrayList();
                                            z = updateStatus;
                                            needGetDiff = needGetDiff3;
                                            updates2.updates.remove(a2);
                                            a2 = (a2 - 1) + 1;
                                            i2 = 1;
                                            minChannels = minChannels2;
                                            updateStatus = z;
                                        } else if (!needGetChannelsDiff3.contains(Integer.valueOf(channelId))) {
                                            needGetChannelsDiff3.add(Integer.valueOf(channelId));
                                        }
                                    }
                                    needReceivedQueue3 = needReceivedQueue;
                                    z = updateStatus;
                                } else if (channelPts != updatesNew.pts) {
                                    if (BuildVars.LOGS_ENABLED) {
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append(update);
                                        stringBuilder2.append(" need get channel diff, pts: ");
                                        stringBuilder2.append(channelPts);
                                        stringBuilder2.append(" ");
                                        stringBuilder2.append(updatesNew.pts);
                                        stringBuilder2.append(" count = ");
                                        stringBuilder2.append(updatesNew.pts_count);
                                        stringBuilder2.append(" channelId = ");
                                        stringBuilder2.append(channelId);
                                        FileLog.d(stringBuilder2.toString());
                                    }
                                    long updatesStartWaitTime = messagesController.updatesStartWaitTimeChannels.get(channelId);
                                    needGetDiff = messagesController.gettingDifferenceChannels.get(channelId);
                                    if (needGetDiff || updatesStartWaitTime == 0) {
                                        boolean z3 = needGetDiff;
                                        needReceivedQueue3 = needReceivedQueue;
                                    } else {
                                        needReceivedQueue3 = needReceivedQueue;
                                        if (Math.abs(System.currentTimeMillis() - updatesStartWaitTime) > 1500) {
                                            if (needGetChannelsDiff3 == null) {
                                                needGetChannelsDiff3 = new ArrayList();
                                            } else if (!needGetChannelsDiff3.contains(Integer.valueOf(channelId))) {
                                                needGetChannelsDiff3.add(Integer.valueOf(channelId));
                                            }
                                            z = updateStatus;
                                        }
                                    }
                                    if (updatesStartWaitTime == 0) {
                                        z = updateStatus;
                                        messagesController.updatesStartWaitTimeChannels.put(channelId, System.currentTimeMillis());
                                    } else {
                                        z = updateStatus;
                                    }
                                    if (BuildVars.LOGS_ENABLED) {
                                        FileLog.d("add to queue");
                                    }
                                    ArrayList<Updates> arrayList = (ArrayList) messagesController.updatesQueueChannels.get(channelId);
                                    if (arrayList == null) {
                                        arrayList = new ArrayList();
                                        messagesController.updatesQueueChannels.put(channelId, arrayList);
                                    }
                                    arrayList.add(updatesNew);
                                } else {
                                    needReceivedQueue3 = needReceivedQueue;
                                    z = updateStatus;
                                }
                                needGetDiff = needGetDiff3;
                                needReceivedQueue = needReceivedQueue3;
                                updates2.updates.remove(a2);
                                a2 = (a2 - 1) + 1;
                                i2 = 1;
                                minChannels = minChannels2;
                                updateStatus = z;
                            } else {
                                TL_updates updatesNew2 = new TL_updates();
                                updatesNew2.updates.add(update);
                                updatesNew2.pts = getUpdateQts(update);
                                for (int b2 = a2 + 1; b2 < updates2.updates.size(); b2 = (b2 - 1) + 1) {
                                    Update update22 = (Update) updates2.updates.get(b2);
                                    channelPts = getUpdateQts(update22);
                                    if (getUpdateType(update22) != 1 || updatesNew2.pts + 1 != channelPts) {
                                        break;
                                    }
                                    updatesNew2.updates.add(update22);
                                    updatesNew2.pts = channelPts;
                                    updates2.updates.remove(b2);
                                }
                                if (MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue() != 0) {
                                    if (MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue() + updatesNew2.updates.size() != updatesNew2.pts) {
                                        if (MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue() != updatesNew2.pts) {
                                            if (BuildVars.LOGS_ENABLED) {
                                                StringBuilder stringBuilder3 = new StringBuilder();
                                                stringBuilder3.append(update);
                                                stringBuilder3.append(" need get diff, qts: ");
                                                stringBuilder3.append(MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue());
                                                stringBuilder3.append(" ");
                                                stringBuilder3.append(updatesNew2.pts);
                                                FileLog.d(stringBuilder3.toString());
                                            }
                                            if (!(messagesController.gettingDifference || messagesController.updatesStartWaitTimeQts == 0)) {
                                                if (messagesController.updatesStartWaitTimeQts == 0 || Math.abs(System.currentTimeMillis() - messagesController.updatesStartWaitTimeQts) > 1500) {
                                                    needGetDiff = true;
                                                }
                                            }
                                            if (messagesController.updatesStartWaitTimeQts == 0) {
                                                messagesController.updatesStartWaitTimeQts = System.currentTimeMillis();
                                            }
                                            if (BuildVars.LOGS_ENABLED) {
                                                FileLog.d("add to queue");
                                            }
                                            messagesController.updatesQueueQts.add(updatesNew2);
                                        }
                                    }
                                }
                                processUpdateArray(updatesNew2.updates, updates2.users, updates2.chats, false);
                                MessagesStorage.getInstance(messagesController.currentAccount).setLastQtsValue(updatesNew2.pts);
                                needReceivedQueue = true;
                            }
                        } else {
                            TL_updates updatesNew3 = new TL_updates();
                            updatesNew3.updates.add(update);
                            updatesNew3.pts = getUpdatePts(update);
                            updatesNew3.pts_count = getUpdatePtsCount(update);
                            for (int b3 = a2 + 1; b3 < updates2.updates.size(); b3 = (b3 - 1) + i2) {
                                Update update23 = (Update) updates2.updates.get(b3);
                                channelId2 = getUpdatePts(update23);
                                pts2 = getUpdatePtsCount(update23);
                                if (getUpdateType(update23) != 0 || updatesNew3.pts + pts2 != channelId2) {
                                    break;
                                }
                                updatesNew3.updates.add(update23);
                                updatesNew3.pts = channelId2;
                                updatesNew3.pts_count += pts2;
                                updates2.updates.remove(b3);
                            }
                            StringBuilder stringBuilder4;
                            if (MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue() + updatesNew3.pts_count == updatesNew3.pts) {
                                if (processUpdateArray(updatesNew3.updates, updates2.users, updates2.chats, false)) {
                                    MessagesStorage.getInstance(messagesController.currentAccount).setLastPtsValue(updatesNew3.pts);
                                } else {
                                    if (BuildVars.LOGS_ENABLED) {
                                        stringBuilder4 = new StringBuilder();
                                        stringBuilder4.append("need get diff inner TL_updates, pts: ");
                                        stringBuilder4.append(MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue());
                                        stringBuilder4.append(" ");
                                        stringBuilder4.append(updates2.seq);
                                        FileLog.d(stringBuilder4.toString());
                                    }
                                    needGetDiff = true;
                                }
                                minChannels2 = minChannels;
                            } else if (MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue() != updatesNew3.pts) {
                                if (BuildVars.LOGS_ENABLED) {
                                    stringBuilder4 = new StringBuilder();
                                    stringBuilder4.append(update);
                                    stringBuilder4.append(" need get diff, pts: ");
                                    stringBuilder4.append(MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue());
                                    stringBuilder4.append(" ");
                                    stringBuilder4.append(updatesNew3.pts);
                                    stringBuilder4.append(" count = ");
                                    stringBuilder4.append(updatesNew3.pts_count);
                                    FileLog.d(stringBuilder4.toString());
                                }
                                if (messagesController.gettingDifference || messagesController.updatesStartWaitTimePts == 0) {
                                    minChannels2 = minChannels;
                                } else {
                                    if (messagesController.updatesStartWaitTimePts != 0) {
                                        minChannels2 = minChannels;
                                        if (Math.abs(System.currentTimeMillis() - messagesController.updatesStartWaitTimePts) <= 1500) {
                                        }
                                    } else {
                                        minChannels2 = minChannels;
                                    }
                                    needGetDiff = true;
                                }
                                if (messagesController.updatesStartWaitTimePts == 0) {
                                    messagesController.updatesStartWaitTimePts = System.currentTimeMillis();
                                }
                                if (BuildVars.LOGS_ENABLED) {
                                    FileLog.d("add to queue");
                                }
                                messagesController.updatesQueuePts.add(updatesNew3);
                            } else {
                                minChannels2 = minChannels;
                            }
                        }
                        z = updateStatus;
                        updates2.updates.remove(a2);
                        a2 = (a2 - 1) + 1;
                        i2 = 1;
                        minChannels = minChannels2;
                        updateStatus = z;
                    }
                    needGetDiff3 = needGetDiff;
                    needReceivedQueue3 = needReceivedQueue;
                    z = updateStatus;
                    i = i2;
                    minChannels2 = minChannels;
                    if (updates2 instanceof TL_updatesCombined) {
                        boolean z4;
                        if (MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue() + i != updates2.seq_start) {
                            if (MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue() != updates2.seq_start) {
                                z4 = false;
                                processUpdate = z4;
                            }
                        }
                        z4 = true;
                        processUpdate = z4;
                    } else {
                        if (!(MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue() + 1 == updates2.seq || updates2.seq == 0)) {
                            if (updates2.seq != MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue()) {
                                processUpdate = false;
                            }
                        }
                        processUpdate = true;
                    }
                    if (processUpdate) {
                        processUpdateArray(updates2.updates, updates2.users, updates2.chats, false);
                        if (updates2.seq != 0) {
                            if (updates2.date != 0) {
                                MessagesStorage.getInstance(messagesController.currentAccount).setLastDateValue(updates2.date);
                            }
                            MessagesStorage.getInstance(messagesController.currentAccount).setLastSeqValue(updates2.seq);
                        }
                    } else {
                        if (BuildVars.LOGS_ENABLED) {
                            if (updates2 instanceof TL_updatesCombined) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("need get diff TL_updatesCombined, seq: ");
                                stringBuilder2.append(MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue());
                                stringBuilder2.append(" ");
                                stringBuilder2.append(updates2.seq_start);
                                FileLog.d(stringBuilder2.toString());
                            } else {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("need get diff TL_updates, seq: ");
                                stringBuilder2.append(MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue());
                                stringBuilder2.append(" ");
                                stringBuilder2.append(updates2.seq);
                                FileLog.d(stringBuilder2.toString());
                            }
                        }
                        if (!(messagesController.gettingDifference || messagesController.updatesStartWaitTimeSeq == 0)) {
                            if (Math.abs(System.currentTimeMillis() - messagesController.updatesStartWaitTimeSeq) > 1500) {
                                needGetDiff = true;
                                needGetChannelsDiff = needGetChannelsDiff3;
                                needReceivedQueue = needReceivedQueue3;
                            }
                        }
                        if (messagesController.updatesStartWaitTimeSeq == 0) {
                            messagesController.updatesStartWaitTimeSeq = System.currentTimeMillis();
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("add TL_updates/Combined to queue");
                        }
                        messagesController.updatesQueueSeq.add(updates2);
                    }
                    needGetChannelsDiff = needGetChannelsDiff3;
                    needGetDiff = needGetDiff3;
                    needReceivedQueue = needReceivedQueue3;
                }
                SecretChatHelper.getInstance(messagesController.currentAccount).processPendingEncMessages();
                if (!fromQueue) {
                    for (a = 0; a < messagesController.updatesQueueChannels.size(); a++) {
                        channelId = messagesController.updatesQueueChannels.keyAt(a);
                        if (needGetChannelsDiff == null && needGetChannelsDiff.contains(Integer.valueOf(channelId))) {
                            getChannelDifference(channelId);
                        } else {
                            processChannelsUpdatesQueue(channelId, 0);
                        }
                    }
                    if (needGetDiff) {
                        getDifference();
                    } else {
                        for (a = 0; a < 3; a++) {
                            processUpdatesQueue(a, 0);
                        }
                    }
                }
                if (needReceivedQueue) {
                    req = new TL_messages_receivedQueue();
                    req.max_qts = MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue();
                    ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                        }
                    });
                }
                if (z) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(4));
                        }
                    });
                }
                MessagesStorage.getInstance(messagesController.currentAccount).saveDiffParams(MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastDateValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue());
            }
            i = updates2 instanceof TL_updateShortChatMessage ? updates2.from_id : updates2.user_id;
            User user = getUser(Integer.valueOf(i));
            User user2 = null;
            User user3 = null;
            Chat channel = null;
            if (user == null || user.min) {
                user = MessagesStorage.getInstance(messagesController.currentAccount).getUserSync(i);
                if (user != null && user.min) {
                    user = null;
                }
                putUser(user, true);
            }
            skipUpdate = false;
            if (updates2.fwd_from != null) {
                if (updates2.fwd_from.from_id != 0) {
                    user2 = getUser(Integer.valueOf(updates2.fwd_from.from_id));
                    if (user2 == null) {
                        user2 = MessagesStorage.getInstance(messagesController.currentAccount).getUserSync(updates2.fwd_from.from_id);
                        putUser(user2, true);
                    }
                    skipUpdate = true;
                }
                if (updates2.fwd_from.channel_id != 0) {
                    channel = getChat(Integer.valueOf(updates2.fwd_from.channel_id));
                    if (channel == null) {
                        channel = MessagesStorage.getInstance(messagesController.currentAccount).getChatSync(updates2.fwd_from.channel_id);
                        putChat(channel, true);
                    }
                    skipUpdate = true;
                }
            }
            boolean needBotUser = false;
            if (updates2.via_bot_id != 0) {
                user3 = getUser(Integer.valueOf(updates2.via_bot_id));
                if (user3 == null) {
                    user3 = MessagesStorage.getInstance(messagesController.currentAccount).getUserSync(updates2.via_bot_id);
                    putUser(user3, true);
                }
                needBotUser = true;
            }
            if (updates2 instanceof TL_updateShortMessage) {
                if (!(user == null || (skipUpdate && user2 == null && channel == null))) {
                    if (!needBotUser || user3 != null) {
                        z2 = false;
                    }
                }
                z2 = true;
            } else {
                boolean z5;
                Chat chat3 = getChat(Integer.valueOf(updates2.chat_id));
                if (chat3 == null) {
                    chat3 = MessagesStorage.getInstance(messagesController.currentAccount).getChatSync(updates2.chat_id);
                    putChat(chat3, true);
                }
                if (!(chat3 == null || user == null || (skipUpdate && user2 == null && channel == null))) {
                    if (!needBotUser || user3 != null) {
                        z5 = false;
                        z2 = z5;
                    }
                }
                z5 = true;
                z2 = z5;
            }
            if (!(z2 || updates2.entities.isEmpty())) {
                b = 0;
                while (b < updates2.entities.size()) {
                    MessageEntity entity = (MessageEntity) updates2.entities.get(b);
                    if (entity instanceof TL_messageEntityMentionName) {
                        pts2 = ((TL_messageEntityMentionName) entity).user_id;
                        needGetChannelsDiff2 = needGetChannelsDiff;
                        needGetChannelsDiff = getUser(Integer.valueOf(pts2));
                        if (needGetChannelsDiff != null) {
                            needGetDiff2 = needGetDiff;
                            if (!needGetChannelsDiff.min) {
                                continue;
                            }
                        } else {
                            needGetDiff2 = needGetDiff;
                        }
                        needGetChannelsDiff = MessagesStorage.getInstance(messagesController.currentAccount).getUserSync(pts2);
                        if (needGetChannelsDiff != null && needGetChannelsDiff.min) {
                            needGetChannelsDiff = null;
                        }
                        if (needGetChannelsDiff == null) {
                            z2 = true;
                            break;
                        }
                        putUser(user, true);
                    } else {
                        needGetChannelsDiff2 = needGetChannelsDiff;
                        needGetDiff2 = needGetDiff;
                    }
                    b++;
                    needGetChannelsDiff = needGetChannelsDiff2;
                    needGetDiff = needGetDiff2;
                }
            }
            needGetChannelsDiff2 = needGetChannelsDiff;
            needGetDiff2 = needGetDiff;
            if (!(user == null || user.status == null || user.status.expires > 0)) {
                messagesController.onlinePrivacy.put(Integer.valueOf(user.id), Integer.valueOf(ConnectionsManager.getInstance(messagesController.currentAccount).getCurrentTime()));
                z = true;
            }
            if (z2) {
                needGetDiff = true;
                needReceivedQueue2 = false;
            } else {
                User user4;
                User user5;
                if (MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue() + updates2.pts_count == updates2.pts) {
                    TL_message message = new TL_message();
                    message.id = updates2.id;
                    int clientUserId = UserConfig.getInstance(messagesController.currentAccount).getClientUserId();
                    if (updates2 instanceof TL_updateShortMessage) {
                        if (updates2.out) {
                            message.from_id = clientUserId;
                        } else {
                            message.from_id = i;
                        }
                        message.to_id = new TL_peerUser();
                        message.to_id.user_id = i;
                        message.dialog_id = (long) i;
                    } else {
                        message.from_id = i;
                        message.to_id = new TL_peerChat();
                        message.to_id.chat_id = updates2.chat_id;
                        message.dialog_id = (long) (-updates2.chat_id);
                    }
                    message.fwd_from = updates2.fwd_from;
                    message.silent = updates2.silent;
                    message.out = updates2.out;
                    message.mentioned = updates2.mentioned;
                    message.media_unread = updates2.media_unread;
                    message.entities = updates2.entities;
                    message.message = updates2.message;
                    message.date = updates2.date;
                    message.via_bot_id = updates2.via_bot_id;
                    message.flags = updates2.flags | 256;
                    message.reply_to_msg_id = updates2.reply_to_msg_id;
                    message.media = new TL_messageMediaEmpty();
                    ConcurrentHashMap<Long, Integer> read_max = message.out ? messagesController.dialogs_read_outbox_max : messagesController.dialogs_read_inbox_max;
                    Integer value = (Integer) read_max.get(Long.valueOf(message.dialog_id));
                    if (value == null) {
                        needReceivedQueue2 = false;
                        value = Integer.valueOf(MessagesStorage.getInstance(messagesController.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                        read_max.put(Long.valueOf(message.dialog_id), value);
                    } else {
                        needReceivedQueue2 = false;
                        user4 = user;
                        user5 = user2;
                    }
                    message.unread = value.intValue() < message.id;
                    if (message.dialog_id == ((long) clientUserId)) {
                        message.unread = false;
                        message.media_unread = false;
                        message.out = true;
                    }
                    MessagesStorage.getInstance(messagesController.currentAccount).setLastPtsValue(updates2.pts);
                    needReceivedQueue = new MessageObject(messagesController.currentAccount, message, messagesController.createdDialogIds.contains(Long.valueOf(message.dialog_id)));
                    user = new ArrayList();
                    user.add(needReceivedQueue);
                    user2 = new ArrayList();
                    user2.add(message);
                    final boolean printUpdate;
                    if (updates2 instanceof TL_updateShortMessage) {
                        boolean z6 = !updates2.out && updatePrintingUsersWithNewMessages((long) updates2.user_id, user);
                        printUpdate = z6;
                        if (printUpdate) {
                            updatePrintingStrings();
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (printUpdate) {
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(64));
                                }
                                MessagesController.this.updateInterfaceWithMessages((long) i, user);
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                            }
                        });
                    } else {
                        printUpdate = updatePrintingUsersWithNewMessages((long) (-updates2.chat_id), user);
                        if (printUpdate) {
                            updatePrintingStrings();
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (printUpdate) {
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(64));
                                }
                                MessagesController.this.updateInterfaceWithMessages((long) (-updates2.chat_id), user);
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                            }
                        });
                    }
                    if (!needReceivedQueue.isOut()) {
                        MessagesStorage.getInstance(messagesController.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                            public void run() {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(user, true, false);
                                    }
                                });
                            }
                        });
                    }
                    MessagesStorage.getInstance(messagesController.currentAccount).putMessages((ArrayList) user2, false, true, false, 0);
                } else {
                    needReceivedQueue2 = false;
                    user4 = user;
                    user5 = user2;
                    User user6 = user3;
                    Chat chat4 = channel;
                    boolean z7 = skipUpdate;
                    if (MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue() != updates2.pts) {
                        if (BuildVars.LOGS_ENABLED) {
                            StringBuilder stringBuilder5 = new StringBuilder();
                            stringBuilder5.append("need get diff short message, pts: ");
                            stringBuilder5.append(MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue());
                            stringBuilder5.append(" ");
                            stringBuilder5.append(updates2.pts);
                            stringBuilder5.append(" count = ");
                            stringBuilder5.append(updates2.pts_count);
                            FileLog.d(stringBuilder5.toString());
                        }
                        if (!(messagesController.gettingDifference || messagesController.updatesStartWaitTimePts == 0)) {
                            if (Math.abs(System.currentTimeMillis() - messagesController.updatesStartWaitTimePts) > 1500) {
                                needGetDiff = true;
                            }
                        }
                        if (messagesController.updatesStartWaitTimePts == 0) {
                            messagesController.updatesStartWaitTimePts = System.currentTimeMillis();
                        }
                        if (BuildVars.LOGS_ENABLED) {
                            FileLog.d("add to queue");
                        }
                        messagesController.updatesQueuePts.add(updates2);
                    }
                }
                needGetDiff = needGetDiff2;
            }
            needGetChannelsDiff = needGetChannelsDiff2;
            needReceivedQueue = needReceivedQueue2;
            SecretChatHelper.getInstance(messagesController.currentAccount).processPendingEncMessages();
            if (fromQueue) {
                for (a = 0; a < messagesController.updatesQueueChannels.size(); a++) {
                    channelId = messagesController.updatesQueueChannels.keyAt(a);
                    if (needGetChannelsDiff == null) {
                    }
                    processChannelsUpdatesQueue(channelId, 0);
                }
                if (needGetDiff) {
                    for (a = 0; a < 3; a++) {
                        processUpdatesQueue(a, 0);
                    }
                } else {
                    getDifference();
                }
            }
            if (needReceivedQueue) {
                req = new TL_messages_receivedQueue();
                req.max_qts = MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue();
                ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req, /* anonymous class already generated */);
            }
            if (z) {
                AndroidUtilities.runOnUIThread(/* anonymous class already generated */);
            }
            MessagesStorage.getInstance(messagesController.currentAccount).saveDiffParams(MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastDateValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue());
        }
        z = false;
        SecretChatHelper.getInstance(messagesController.currentAccount).processPendingEncMessages();
        if (fromQueue) {
            for (a = 0; a < messagesController.updatesQueueChannels.size(); a++) {
                channelId = messagesController.updatesQueueChannels.keyAt(a);
                if (needGetChannelsDiff == null) {
                }
                processChannelsUpdatesQueue(channelId, 0);
            }
            if (needGetDiff) {
                getDifference();
            } else {
                for (a = 0; a < 3; a++) {
                    processUpdatesQueue(a, 0);
                }
            }
        }
        if (needReceivedQueue) {
            req = new TL_messages_receivedQueue();
            req.max_qts = MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue();
            ConnectionsManager.getInstance(messagesController.currentAccount).sendRequest(req, /* anonymous class already generated */);
        }
        if (z) {
            AndroidUtilities.runOnUIThread(/* anonymous class already generated */);
        }
        MessagesStorage.getInstance(messagesController.currentAccount).saveDiffParams(MessagesStorage.getInstance(messagesController.currentAccount).getLastSeqValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastPtsValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastDateValue(), MessagesStorage.getInstance(messagesController.currentAccount).getLastQtsValue());
    }

    private boolean isNotifySettingsMuted(PeerNotifySettings settings) {
        return (settings instanceof TL_peerNotifySettings) && settings.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
    }

    public boolean isDialogMuted(long dialog_id) {
        int mute_type = this.notificationsPreferences;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("notify2_");
        stringBuilder.append(dialog_id);
        mute_type = mute_type.getInt(stringBuilder.toString(), 0);
        if (mute_type == 2) {
            return true;
        }
        if (mute_type == 3) {
            int mute_until = this.notificationsPreferences;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("notifyuntil_");
            stringBuilder2.append(dialog_id);
            if (mute_until.getInt(stringBuilder2.toString(), 0) >= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
                return true;
            }
        }
        return false;
    }

    private boolean updatePrintingUsersWithNewMessages(long uid, ArrayList<MessageObject> messages) {
        if (uid > 0) {
            if (((ArrayList) this.printingUsers.get(Long.valueOf(uid))) != null) {
                this.printingUsers.remove(Long.valueOf(uid));
                return true;
            }
        } else if (uid < 0) {
            ArrayList<Integer> messagesUsers = new ArrayList();
            Iterator it = messages.iterator();
            while (it.hasNext()) {
                MessageObject message = (MessageObject) it.next();
                if (!messagesUsers.contains(Integer.valueOf(message.messageOwner.from_id))) {
                    messagesUsers.add(Integer.valueOf(message.messageOwner.from_id));
                }
            }
            ArrayList<PrintingUser> arr = (ArrayList) this.printingUsers.get(Long.valueOf(uid));
            boolean changed = false;
            if (arr != null) {
                boolean changed2 = false;
                int a = 0;
                while (a < arr.size()) {
                    if (messagesUsers.contains(Integer.valueOf(((PrintingUser) arr.get(a)).userId))) {
                        arr.remove(a);
                        a--;
                        if (arr.isEmpty()) {
                            this.printingUsers.remove(Long.valueOf(uid));
                        }
                        changed2 = true;
                    }
                    a++;
                }
                changed = changed2;
            }
            if (changed) {
                return true;
            }
        }
        return false;
    }

    protected void updateInterfaceWithMessages(long uid, ArrayList<MessageObject> messages) {
        updateInterfaceWithMessages(uid, messages, false);
    }

    protected void updateInterfaceWithMessages(long uid, ArrayList<MessageObject> messages, boolean isBroadcast) {
        MessagesController messagesController = this;
        long j = uid;
        ArrayList<MessageObject> arrayList = messages;
        if (arrayList != null) {
            if (!messages.isEmpty()) {
                boolean isEncryptedChat = ((int) j) == 0;
                boolean updateRating = false;
                int channelId = 0;
                MessageObject lastMessage = null;
                for (int a = 0; a < messages.size(); a++) {
                    MessageObject message = (MessageObject) arrayList.get(a);
                    if (lastMessage == null || ((!isEncryptedChat && message.getId() > lastMessage.getId()) || (((isEncryptedChat || (message.getId() < 0 && lastMessage.getId() < 0)) && message.getId() < lastMessage.getId()) || message.messageOwner.date > lastMessage.messageOwner.date))) {
                        lastMessage = message;
                        if (message.messageOwner.to_id.channel_id != 0) {
                            channelId = message.messageOwner.to_id.channel_id;
                        }
                    }
                    if (!(!message.isOut() || message.isSending() || message.isForwarded())) {
                        if (message.isNewGif()) {
                            DataQuery.getInstance(messagesController.currentAccount).addRecentGif(message.messageOwner.media.document, message.messageOwner.date);
                        } else if (message.isSticker()) {
                            DataQuery.getInstance(messagesController.currentAccount).addRecentSticker(0, message.messageOwner.media.document, message.messageOwner.date, false);
                        }
                    }
                    if (message.isOut() && message.isSent()) {
                        updateRating = true;
                    }
                }
                DataQuery.getInstance(messagesController.currentAccount).loadReplyMessagesForMessages(arrayList, j);
                NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.didReceivedNewMessages, Long.valueOf(uid), arrayList);
                if (lastMessage != null) {
                    TL_dialog dialog = (TL_dialog) messagesController.dialogs_dict.get(j);
                    MessageObject object;
                    if (lastMessage.messageOwner.action instanceof TL_messageActionChatMigrateTo) {
                        if (dialog != null) {
                            messagesController.dialogs.remove(dialog);
                            messagesController.dialogsServerOnly.remove(dialog);
                            messagesController.dialogsGroupsOnly.remove(dialog);
                            messagesController.dialogs_dict.remove(dialog.id);
                            messagesController.dialogs_read_inbox_max.remove(Long.valueOf(dialog.id));
                            messagesController.dialogs_read_outbox_max.remove(Long.valueOf(dialog.id));
                            messagesController.nextDialogsCacheOffset--;
                            messagesController.dialogMessage.remove(dialog.id);
                            object = (MessageObject) messagesController.dialogMessagesByIds.get(dialog.top_message);
                            messagesController.dialogMessagesByIds.remove(dialog.top_message);
                            if (!(object == null || object.messageOwner.random_id == 0)) {
                                messagesController.dialogMessagesByRandomIds.remove(object.messageOwner.random_id);
                            }
                            dialog.top_message = 0;
                            NotificationsController.getInstance(messagesController.currentAccount).removeNotificationsForDialog(dialog.id);
                            NotificationCenter.getInstance(messagesController.currentAccount).postNotificationName(NotificationCenter.needReloadRecentDialogsSearch, new Object[0]);
                        }
                        return;
                    }
                    boolean changed = false;
                    if (dialog == null) {
                        if (!isBroadcast) {
                            Chat chat = getChat(Integer.valueOf(channelId));
                            if ((channelId == 0 || chat != null) && (chat == null || !chat.left)) {
                                dialog = new TL_dialog();
                                dialog.id = j;
                                dialog.unread_count = 0;
                                dialog.top_message = lastMessage.getId();
                                dialog.last_message_date = lastMessage.messageOwner.date;
                                dialog.flags = ChatObject.isChannel(chat);
                                messagesController.dialogs_dict.put(j, dialog);
                                messagesController.dialogs.add(dialog);
                                messagesController.dialogMessage.put(j, lastMessage);
                                if (lastMessage.messageOwner.to_id.channel_id == 0) {
                                    messagesController.dialogMessagesByIds.put(lastMessage.getId(), lastMessage);
                                    if (lastMessage.messageOwner.random_id != 0) {
                                        messagesController.dialogMessagesByRandomIds.put(lastMessage.messageOwner.random_id, lastMessage);
                                    }
                                }
                                messagesController.nextDialogsCacheOffset++;
                                changed = true;
                            } else {
                                return;
                            }
                        }
                    } else if ((dialog.top_message > 0 && lastMessage.getId() > 0 && lastMessage.getId() > dialog.top_message) || ((dialog.top_message < 0 && lastMessage.getId() < 0 && lastMessage.getId() < dialog.top_message) || messagesController.dialogMessage.indexOfKey(j) < 0 || dialog.top_message < 0 || dialog.last_message_date <= lastMessage.messageOwner.date)) {
                        object = (MessageObject) messagesController.dialogMessagesByIds.get(dialog.top_message);
                        messagesController.dialogMessagesByIds.remove(dialog.top_message);
                        if (!(object == null || object.messageOwner.random_id == 0)) {
                            messagesController.dialogMessagesByRandomIds.remove(object.messageOwner.random_id);
                        }
                        dialog.top_message = lastMessage.getId();
                        if (!isBroadcast) {
                            dialog.last_message_date = lastMessage.messageOwner.date;
                            changed = true;
                        }
                        messagesController.dialogMessage.put(j, lastMessage);
                        if (lastMessage.messageOwner.to_id.channel_id == 0) {
                            messagesController.dialogMessagesByIds.put(lastMessage.getId(), lastMessage);
                            if (lastMessage.messageOwner.random_id != 0) {
                                messagesController.dialogMessagesByRandomIds.put(lastMessage.messageOwner.random_id, lastMessage);
                            }
                        }
                    }
                    if (changed) {
                        sortDialogs(null);
                    }
                    if (updateRating) {
                        DataQuery.getInstance(messagesController.currentAccount).increasePeerRaiting(j);
                    }
                }
            }
        }
    }

    public void sortDialogs(SparseArray<Chat> chatsDict) {
        this.dialogsServerOnly.clear();
        this.dialogsGroupsOnly.clear();
        this.dialogsForward.clear();
        int selfId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        Collections.sort(this.dialogs, this.dialogComparator);
        boolean selfAdded = false;
        int a = 0;
        while (a < this.dialogs.size()) {
            TL_dialog d = (TL_dialog) this.dialogs.get(a);
            int high_id = (int) (d.id >> 32);
            int lower_id = (int) d.id;
            if (lower_id == selfId) {
                this.dialogsForward.add(0, d);
                selfAdded = true;
            } else {
                this.dialogsForward.add(d);
            }
            if (!(lower_id == 0 || high_id == 1)) {
                this.dialogsServerOnly.add(d);
                Chat chat;
                if (DialogObject.isChannel(d)) {
                    chat = getChat(Integer.valueOf(-lower_id));
                    if (chat != null && ((chat.megagroup && chat.admin_rights != null && (chat.admin_rights.post_messages || chat.admin_rights.add_admins)) || chat.creator)) {
                        this.dialogsGroupsOnly.add(d);
                    }
                } else if (lower_id < 0) {
                    if (chatsDict != null) {
                        chat = (Chat) chatsDict.get(-lower_id);
                        if (!(chat == null || chat.migrated_to == null)) {
                            this.dialogs.remove(a);
                            a--;
                        }
                    }
                    this.dialogsGroupsOnly.add(d);
                }
            }
            a++;
        }
        if (!selfAdded) {
            User user = UserConfig.getInstance(this.currentAccount).getCurrentUser();
            if (user != null) {
                d = new TL_dialog();
                d.id = (long) user.id;
                d.notify_settings = new TL_peerNotifySettings();
                d.peer = new TL_peerUser();
                d.peer.user_id = user.id;
                this.dialogsForward.add(0, d);
            }
        }
    }

    private static String getRestrictionReason(String reason) {
        if (reason != null) {
            if (reason.length() != 0) {
                int index = reason.indexOf(": ");
                if (index > 0) {
                    String type = reason.substring(null, index);
                    if (type.contains("-all") || type.contains("-android")) {
                        return reason.substring(index + 2);
                    }
                }
                return null;
            }
        }
        return null;
    }

    private static void showCantOpenAlert(BaseFragment fragment, String reason) {
        if (fragment != null) {
            if (fragment.getParentActivity() != null) {
                Builder builder = new Builder(fragment.getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                builder.setMessage(reason);
                fragment.showDialog(builder.create());
            }
        }
    }

    public boolean checkCanOpenChat(Bundle bundle, BaseFragment fragment) {
        return checkCanOpenChat(bundle, fragment, null);
    }

    public static void openChatOrProfileWith(User user, Chat chat, BaseFragment fragment, int type, boolean closeLast) {
        if ((user != null || chat != null) && fragment != null) {
            String reason = null;
            if (chat != null) {
                reason = getRestrictionReason(chat.restriction_reason);
            } else if (user != null) {
                reason = getRestrictionReason(user.restriction_reason);
                if (user.bot) {
                    type = 1;
                    closeLast = true;
                }
            }
            if (reason != null) {
                showCantOpenAlert(fragment, reason);
            } else {
                Bundle args = new Bundle();
                if (chat != null) {
                    args.putInt("chat_id", chat.id);
                } else {
                    args.putInt("user_id", user.id);
                }
                if (type == 0) {
                    fragment.presentFragment(new ProfileActivity(args));
                } else if (type == 2) {
                    fragment.presentFragment(new ChatActivity(args), true, true);
                } else {
                    fragment.presentFragment(new ChatActivity(args), closeLast);
                }
            }
        }
    }

    public void openByUserName(String username, final BaseFragment fragment, final int type) {
        if (username != null) {
            if (fragment != null) {
                TLObject object = getUserOrChat(username);
                User user = null;
                Chat chat = null;
                if (object instanceof User) {
                    user = (User) object;
                    if (user.min) {
                        user = null;
                    }
                } else if (object instanceof Chat) {
                    chat = (Chat) object;
                    if (chat.min) {
                        chat = null;
                    }
                }
                if (user != null) {
                    openChatOrProfileWith(user, null, fragment, type, false);
                } else if (chat != null) {
                    openChatOrProfileWith(null, chat, fragment, 1, false);
                } else if (fragment.getParentActivity() != null) {
                    final AlertDialog[] progressDialog = new AlertDialog[]{new AlertDialog(fragment.getParentActivity(), 1)};
                    TL_contacts_resolveUsername req = new TL_contacts_resolveUsername();
                    req.username = username;
                    final int reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(final TLObject response, final TL_error error) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    try {
                                        progressDialog[0].dismiss();
                                    } catch (Exception e) {
                                    }
                                    progressDialog[0] = null;
                                    fragment.setVisibleDialog(null);
                                    if (error == null) {
                                        TL_contacts_resolvedPeer res = response;
                                        MessagesController.this.putUsers(res.users, false);
                                        MessagesController.this.putChats(res.chats, false);
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, res.chats, false, true);
                                        if (!res.chats.isEmpty()) {
                                            MessagesController.openChatOrProfileWith(null, (Chat) res.chats.get(0), fragment, 1, false);
                                        } else if (!res.users.isEmpty()) {
                                            MessagesController.openChatOrProfileWith((User) res.users.get(0), null, fragment, type, false);
                                        }
                                    } else if (fragment != null && fragment.getParentActivity() != null) {
                                        try {
                                            Toast.makeText(fragment.getParentActivity(), LocaleController.getString("NoUsernameFound", R.string.NoUsernameFound), 0).show();
                                        } catch (Throwable e2) {
                                            FileLog.e(e2);
                                        }
                                    }
                                }
                            });
                        }
                    });
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (progressDialog[0] != null) {
                                progressDialog[0].setMessage(LocaleController.getString("Loading", R.string.Loading));
                                progressDialog[0].setCanceledOnTouchOutside(false);
                                progressDialog[0].setCancelable(false);
                                progressDialog[0].setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ConnectionsManager.getInstance(MessagesController.this.currentAccount).cancelRequest(reqId, true);
                                        try {
                                            dialog.dismiss();
                                        } catch (Throwable e) {
                                            FileLog.e(e);
                                        }
                                    }
                                });
                                fragment.showDialog(progressDialog[0]);
                            }
                        }
                    }, 500);
                }
            }
        }
    }
}
