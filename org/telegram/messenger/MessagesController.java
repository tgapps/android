package org.telegram.messenger;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.widget.Toast;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import org.telegram.SQLite.SQLiteCursor;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.DefaultLoadControl;
import org.telegram.messenger.exoplayer2.DefaultRenderersFactory;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.SerializedData;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInfo;
import org.telegram.tgnet.TLRPC.ChannelParticipant;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ChatParticipant;
import org.telegram.tgnet.TLRPC.ChatParticipants;
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
import org.telegram.tgnet.TLRPC.PhoneCall;
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
import org.telegram.tgnet.TLRPC.TL_channels_editPhoto;
import org.telegram.tgnet.TLRPC.TL_channels_editTitle;
import org.telegram.tgnet.TLRPC.TL_channels_getFullChannel;
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
import org.telegram.tgnet.TLRPC.TL_contactLinkContact;
import org.telegram.tgnet.TLRPC.TL_contacts_block;
import org.telegram.tgnet.TLRPC.TL_contacts_getBlocked;
import org.telegram.tgnet.TLRPC.TL_contacts_resolveUsername;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;
import org.telegram.tgnet.TLRPC.TL_contacts_unblock;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_disabledFeature;
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
import org.telegram.tgnet.TLRPC.TL_inputChatPhotoEmpty;
import org.telegram.tgnet.TLRPC.TL_inputChatUploadedPhoto;
import org.telegram.tgnet.TLRPC.TL_inputDocument;
import org.telegram.tgnet.TLRPC.TL_inputEncryptedChat;
import org.telegram.tgnet.TLRPC.TL_inputMessagesFilterChatPhotos;
import org.telegram.tgnet.TLRPC.TL_inputPeerChannel;
import org.telegram.tgnet.TLRPC.TL_inputPeerChat;
import org.telegram.tgnet.TLRPC.TL_inputPeerEmpty;
import org.telegram.tgnet.TLRPC.TL_inputPeerUser;
import org.telegram.tgnet.TLRPC.TL_inputPhoneCall;
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
import org.telegram.tgnet.TLRPC.TL_messageActionUserJoined;
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
import org.telegram.tgnet.TLRPC.TL_messages_editChatPhoto;
import org.telegram.tgnet.TLRPC.TL_messages_editChatTitle;
import org.telegram.tgnet.TLRPC.TL_messages_getDialogs;
import org.telegram.tgnet.TLRPC.TL_messages_getFullChat;
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
import org.telegram.tgnet.TLRPC.TL_notifyPeer;
import org.telegram.tgnet.TLRPC.TL_peerChannel;
import org.telegram.tgnet.TLRPC.TL_peerChat;
import org.telegram.tgnet.TLRPC.TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC.TL_peerNotifySettingsEmpty;
import org.telegram.tgnet.TLRPC.TL_peerSettings;
import org.telegram.tgnet.TLRPC.TL_peerUser;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
import org.telegram.tgnet.TLRPC.TL_phoneCallRequested;
import org.telegram.tgnet.TLRPC.TL_phone_discardCall;
import org.telegram.tgnet.TLRPC.TL_photoEmpty;
import org.telegram.tgnet.TLRPC.TL_photos_deletePhotos;
import org.telegram.tgnet.TLRPC.TL_photos_getUserPhotos;
import org.telegram.tgnet.TLRPC.TL_photos_photo;
import org.telegram.tgnet.TLRPC.TL_photos_photos;
import org.telegram.tgnet.TLRPC.TL_photos_updateProfilePhoto;
import org.telegram.tgnet.TLRPC.TL_photos_uploadProfilePhoto;
import org.telegram.tgnet.TLRPC.TL_privacyKeyChatInvite;
import org.telegram.tgnet.TLRPC.TL_privacyKeyPhoneCall;
import org.telegram.tgnet.TLRPC.TL_privacyKeyStatusTimestamp;
import org.telegram.tgnet.TLRPC.TL_replyKeyboardHide;
import org.telegram.tgnet.TLRPC.TL_sendMessageCancelAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageGamePlayAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageRecordAudioAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageRecordRoundAction;
import org.telegram.tgnet.TLRPC.TL_sendMessageRecordVideoAction;
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
import org.telegram.tgnet.TLRPC.TL_updateChatAdmins;
import org.telegram.tgnet.TLRPC.TL_updateChatParticipantAdd;
import org.telegram.tgnet.TLRPC.TL_updateChatParticipantAdmin;
import org.telegram.tgnet.TLRPC.TL_updateChatParticipantDelete;
import org.telegram.tgnet.TLRPC.TL_updateChatParticipants;
import org.telegram.tgnet.TLRPC.TL_updateChatUserTyping;
import org.telegram.tgnet.TLRPC.TL_updateConfig;
import org.telegram.tgnet.TLRPC.TL_updateContactLink;
import org.telegram.tgnet.TLRPC.TL_updateContactRegistered;
import org.telegram.tgnet.TLRPC.TL_updateContactsReset;
import org.telegram.tgnet.TLRPC.TL_updateDcOptions;
import org.telegram.tgnet.TLRPC.TL_updateDeleteChannelMessages;
import org.telegram.tgnet.TLRPC.TL_updateDeleteMessages;
import org.telegram.tgnet.TLRPC.TL_updateDialogPinned;
import org.telegram.tgnet.TLRPC.TL_updateDraftMessage;
import org.telegram.tgnet.TLRPC.TL_updateEditChannelMessage;
import org.telegram.tgnet.TLRPC.TL_updateEditMessage;
import org.telegram.tgnet.TLRPC.TL_updateEncryptedChatTyping;
import org.telegram.tgnet.TLRPC.TL_updateEncryptedMessagesRead;
import org.telegram.tgnet.TLRPC.TL_updateEncryption;
import org.telegram.tgnet.TLRPC.TL_updateFavedStickers;
import org.telegram.tgnet.TLRPC.TL_updateGroupCall;
import org.telegram.tgnet.TLRPC.TL_updateGroupCallParticipant;
import org.telegram.tgnet.TLRPC.TL_updateLangPack;
import org.telegram.tgnet.TLRPC.TL_updateLangPackTooLong;
import org.telegram.tgnet.TLRPC.TL_updateMessageID;
import org.telegram.tgnet.TLRPC.TL_updateNewChannelMessage;
import org.telegram.tgnet.TLRPC.TL_updateNewEncryptedMessage;
import org.telegram.tgnet.TLRPC.TL_updateNewGeoChatMessage;
import org.telegram.tgnet.TLRPC.TL_updateNewMessage;
import org.telegram.tgnet.TLRPC.TL_updateNewStickerSet;
import org.telegram.tgnet.TLRPC.TL_updateNotifySettings;
import org.telegram.tgnet.TLRPC.TL_updatePhoneCall;
import org.telegram.tgnet.TLRPC.TL_updatePinnedDialogs;
import org.telegram.tgnet.TLRPC.TL_updatePrivacy;
import org.telegram.tgnet.TLRPC.TL_updateReadChannelInbox;
import org.telegram.tgnet.TLRPC.TL_updateReadChannelOutbox;
import org.telegram.tgnet.TLRPC.TL_updateReadFeaturedStickers;
import org.telegram.tgnet.TLRPC.TL_updateReadHistoryInbox;
import org.telegram.tgnet.TLRPC.TL_updateReadHistoryOutbox;
import org.telegram.tgnet.TLRPC.TL_updateReadMessagesContents;
import org.telegram.tgnet.TLRPC.TL_updateRecentStickers;
import org.telegram.tgnet.TLRPC.TL_updateSavedGifs;
import org.telegram.tgnet.TLRPC.TL_updateServiceNotification;
import org.telegram.tgnet.TLRPC.TL_updateShort;
import org.telegram.tgnet.TLRPC.TL_updateShortChatMessage;
import org.telegram.tgnet.TLRPC.TL_updateShortMessage;
import org.telegram.tgnet.TLRPC.TL_updateStickerSets;
import org.telegram.tgnet.TLRPC.TL_updateStickerSetsOrder;
import org.telegram.tgnet.TLRPC.TL_updateUserBlocked;
import org.telegram.tgnet.TLRPC.TL_updateUserName;
import org.telegram.tgnet.TLRPC.TL_updateUserPhone;
import org.telegram.tgnet.TLRPC.TL_updateUserPhoto;
import org.telegram.tgnet.TLRPC.TL_updateUserStatus;
import org.telegram.tgnet.TLRPC.TL_updateUserTyping;
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
import org.telegram.tgnet.TLRPC.TL_user;
import org.telegram.tgnet.TLRPC.TL_userForeign_old2;
import org.telegram.tgnet.TLRPC.TL_userFull;
import org.telegram.tgnet.TLRPC.TL_userProfilePhoto;
import org.telegram.tgnet.TLRPC.TL_userProfilePhotoEmpty;
import org.telegram.tgnet.TLRPC.TL_userStatusLastMonth;
import org.telegram.tgnet.TLRPC.TL_userStatusLastWeek;
import org.telegram.tgnet.TLRPC.TL_userStatusRecently;
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
import org.telegram.tgnet.TLRPC.WebPage;
import org.telegram.tgnet.TLRPC.contacts_Blocked;
import org.telegram.tgnet.TLRPC.messages_Dialogs;
import org.telegram.tgnet.TLRPC.messages_Messages;
import org.telegram.tgnet.TLRPC.photos_Photos;
import org.telegram.tgnet.TLRPC.updates_ChannelDifference;
import org.telegram.tgnet.TLRPC.updates_Difference;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.BaseFragment;
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
    public static final int UPDATE_MASK_NAME = 1;
    public static final int UPDATE_MASK_NEW_MESSAGE = 2048;
    public static final int UPDATE_MASK_PHONE = 1024;
    public static final int UPDATE_MASK_READ_DIALOG_MESSAGE = 256;
    public static final int UPDATE_MASK_SELECT_DIALOG = 512;
    public static final int UPDATE_MASK_SEND_STATE = 4096;
    public static final int UPDATE_MASK_STATUS = 4;
    public static final int UPDATE_MASK_USER_PHONE = 128;
    public static final int UPDATE_MASK_USER_PRINT = 64;
    public ArrayList<Integer> blockedUsers = new ArrayList();
    public int callConnectTimeout = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS;
    public int callPacketTimeout = 10000;
    public int callReceiveTimeout = 20000;
    public int callRingTimeout = 90000;
    private HashMap<Integer, ArrayList<Integer>> channelAdmins = new HashMap();
    private SparseArray<ArrayList<Integer>> channelViewsToSend = new SparseArray();
    private HashMap<Integer, Integer> channelsPts = new HashMap();
    private ConcurrentHashMap<Integer, Chat> chats = new ConcurrentHashMap(100, 1.0f, 2);
    private HashMap<Integer, Boolean> checkingLastMessagesDialogs = new HashMap();
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
    public HashMap<Long, MessageObject> dialogMessage = new HashMap();
    public HashMap<Integer, MessageObject> dialogMessagesByIds = new HashMap();
    public HashMap<Long, MessageObject> dialogMessagesByRandomIds = new HashMap();
    public ArrayList<TL_dialog> dialogs = new ArrayList();
    public boolean dialogsEndReached;
    public ArrayList<TL_dialog> dialogsForward = new ArrayList();
    public ArrayList<TL_dialog> dialogsGroupsOnly = new ArrayList();
    public ArrayList<TL_dialog> dialogsServerOnly = new ArrayList();
    public ConcurrentHashMap<Long, TL_dialog> dialogs_dict = new ConcurrentHashMap(100, 1.0f, 2);
    public ConcurrentHashMap<Long, Integer> dialogs_read_inbox_max = new ConcurrentHashMap(100, 1.0f, 2);
    public ConcurrentHashMap<Long, Integer> dialogs_read_outbox_max = new ConcurrentHashMap(100, 1.0f, 2);
    private ArrayList<TL_disabledFeature> disabledFeatures = new ArrayList();
    private SharedPreferences emojiPreferences;
    public boolean enableJoined = true;
    private ConcurrentHashMap<Integer, EncryptedChat> encryptedChats = new ConcurrentHashMap(10, 1.0f, 2);
    private HashMap<Integer, ExportedChatInvite> exportedChats = new HashMap();
    public boolean firstGettingTask;
    private HashMap<Integer, TL_userFull> fullUsers = new HashMap();
    private boolean getDifferenceFirstSync = true;
    public boolean gettingDifference;
    private HashMap<Integer, Boolean> gettingDifferenceChannels = new HashMap();
    private boolean gettingNewDeleteTask;
    private HashMap<Integer, Boolean> gettingUnknownChannels = new HashMap();
    public int groupBigSize;
    public ArrayList<RecentMeUrl> hintDialogs = new ArrayList();
    private String installReferer;
    private ArrayList<Integer> joiningToChannels = new ArrayList();
    private int lastPrintingStringCount;
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
    private HashMap<Long, Boolean> loadingPeerSettings = new HashMap();
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
    public HashMap<Long, CharSequence> printingStrings = new HashMap();
    public HashMap<Long, Integer> printingStringsTypes = new HashMap();
    public ConcurrentHashMap<Long, ArrayList<PrintingUser>> printingUsers = new ConcurrentHashMap(20, 1.0f, 2);
    public int ratingDecay;
    public boolean registeringForPush;
    private HashMap<Long, ArrayList<Integer>> reloadingMessages = new HashMap();
    private HashMap<String, ArrayList<MessageObject>> reloadingWebpages = new HashMap();
    private HashMap<Long, ArrayList<MessageObject>> reloadingWebpagesPending = new HashMap();
    private messages_Dialogs resetDialogsAll;
    private TL_messages_peerDialogs resetDialogsPinned;
    private boolean resetingDialogs;
    public int secretWebpagePreview = 2;
    public HashMap<Integer, HashMap<Long, Boolean>> sendingTypings = new HashMap();
    public boolean serverDialogsEndReached;
    private SparseIntArray shortPollChannels = new SparseIntArray();
    private int statusRequest;
    private int statusSettingState;
    private final Comparator<Update> updatesComparator = new Comparator<Update>() {
        public int compare(Update lhs, Update rhs) {
            int ltype = MessagesController.this.getUpdateType(lhs);
            int rtype = MessagesController.this.getUpdateType(rhs);
            if (ltype != rtype) {
                return AndroidUtilities.compare(ltype, rtype);
            }
            if (ltype == 0) {
                return AndroidUtilities.compare(lhs.pts, rhs.pts);
            }
            if (ltype == 1) {
                return AndroidUtilities.compare(lhs.qts, rhs.qts);
            }
            if (ltype != 2) {
                return 0;
            }
            int lChannel = MessagesController.this.getUpdateChannelId(lhs);
            int rChannel = MessagesController.this.getUpdateChannelId(rhs);
            if (lChannel == rChannel) {
                return AndroidUtilities.compare(lhs.pts, rhs.pts);
            }
            return AndroidUtilities.compare(lChannel, rChannel);
        }
    };
    private HashMap<Integer, ArrayList<Updates>> updatesQueueChannels = new HashMap();
    private ArrayList<Updates> updatesQueuePts = new ArrayList();
    private ArrayList<Updates> updatesQueueQts = new ArrayList();
    private ArrayList<Updates> updatesQueueSeq = new ArrayList();
    private HashMap<Integer, Long> updatesStartWaitTimeChannels = new HashMap();
    private long updatesStartWaitTimePts;
    private long updatesStartWaitTimeQts;
    private long updatesStartWaitTimeSeq;
    public boolean updatingState;
    private String uploadingAvatar;
    private ConcurrentHashMap<Integer, User> users = new ConcurrentHashMap(100, 1.0f, 2);

    public static class PrintingUser {
        public SendMessageAction action;
        public long lastTime;
        public int userId;
    }

    private class UserActionUpdatesPts extends Updates {
        private UserActionUpdatesPts() {
        }
    }

    private class UserActionUpdatesSeq extends Updates {
        private UserActionUpdatesSeq() {
        }
    }

    public static MessagesController getInstance(int num) {
        MessagesController localInstance = Instance[num];
        if (localInstance == null) {
            synchronized (MessagesController.class) {
                try {
                    localInstance = Instance[num];
                    if (localInstance == null) {
                        MessagesController[] messagesControllerArr = Instance;
                        MessagesController localInstance2 = new MessagesController(num);
                        try {
                            messagesControllerArr[num] = localInstance2;
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

    public static MessagesController getAccountInstance() {
        return getInstance(UserConfig.selectedAccount);
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
            this.notificationsPreferences = ApplicationLoader.applicationContext.getSharedPreferences("Notifications" + this.currentAccount, 0);
            this.mainPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig" + this.currentAccount, 0);
            this.emojiPreferences = ApplicationLoader.applicationContext.getSharedPreferences("emoji" + this.currentAccount, 0);
        }
        this.enableJoined = this.notificationsPreferences.getBoolean("EnableContactJoined", true);
        this.secretWebpagePreview = this.mainPreferences.getInt("secretWebpage2", 2);
        this.maxGroupCount = this.mainPreferences.getInt("maxGroupCount", Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.maxMegagroupCount = this.mainPreferences.getInt("maxMegagroupCount", 10000);
        this.maxRecentGifsCount = this.mainPreferences.getInt("maxRecentGifsCount", Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.maxRecentStickersCount = this.mainPreferences.getInt("maxRecentStickersCount", 30);
        this.maxFaveStickersCount = this.mainPreferences.getInt("maxFaveStickersCount", 5);
        this.maxEditTime = this.mainPreferences.getInt("maxEditTime", 3600);
        this.groupBigSize = this.mainPreferences.getInt("groupBigSize", 10);
        this.ratingDecay = this.mainPreferences.getInt("ratingDecay", 2419200);
        this.linkPrefix = this.mainPreferences.getString("linkPrefix", "t.me");
        this.callReceiveTimeout = this.mainPreferences.getInt("callReceiveTimeout", 20000);
        this.callRingTimeout = this.mainPreferences.getInt("callRingTimeout", 90000);
        this.callConnectTimeout = this.mainPreferences.getInt("callConnectTimeout", DefaultLoadControl.DEFAULT_MAX_BUFFER_MS);
        this.callPacketTimeout = this.mainPreferences.getInt("callPacketTimeout", 10000);
        this.maxPinnedDialogsCount = this.mainPreferences.getInt("maxPinnedDialogsCount", 5);
        this.installReferer = this.mainPreferences.getString("installReferer", null);
        this.defaultP2pContacts = this.mainPreferences.getBoolean("defaultP2pContacts", false);
        String disabledFeaturesString = this.mainPreferences.getString("disabledFeatures", null);
        if (disabledFeaturesString != null && disabledFeaturesString.length() != 0) {
            try {
                byte[] bytes = Base64.decode(disabledFeaturesString, 0);
                if (bytes != null) {
                    SerializedData data = new SerializedData(bytes);
                    int count = data.readInt32(false);
                    for (int a = 0; a < count; a++) {
                        TL_disabledFeature feature = TL_disabledFeature.TLdeserialize(data, data.readInt32(false), false);
                        if (!(feature == null || feature.feature == null || feature.description == null)) {
                            this.disabledFeatures.add(feature);
                        }
                    }
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public void updateConfig(final TL_config config) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                LocaleController.getInstance().loadRemoteLanguages(MessagesController.this.currentAccount);
                MessagesController.this.maxMegagroupCount = config.megagroup_size_max;
                MessagesController.this.maxGroupCount = config.chat_size_max;
                MessagesController.this.groupBigSize = config.chat_big_size;
                MessagesController.this.disabledFeatures = config.disabled_features;
                MessagesController.this.maxEditTime = config.edit_time_limit;
                MessagesController.this.ratingDecay = config.rating_e_decay;
                MessagesController.this.maxRecentGifsCount = config.saved_gifs_limit;
                MessagesController.this.maxRecentStickersCount = config.stickers_recent_limit;
                MessagesController.this.maxFaveStickersCount = config.stickers_faved_limit;
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
                Editor editor = MessagesController.this.mainPreferences.edit();
                editor.putInt("maxGroupCount", MessagesController.this.maxGroupCount);
                editor.putInt("maxMegagroupCount", MessagesController.this.maxMegagroupCount);
                editor.putInt("groupBigSize", MessagesController.this.groupBigSize);
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
                try {
                    SerializedData data = new SerializedData();
                    data.writeInt32(MessagesController.this.disabledFeatures.size());
                    Iterator it = MessagesController.this.disabledFeatures.iterator();
                    while (it.hasNext()) {
                        ((TL_disabledFeature) it.next()).serializeToStream(data);
                    }
                    String string = Base64.encodeToString(data.toByteArray(), 0);
                    if (string.length() != 0) {
                        editor.putString("disabledFeatures", string);
                    }
                } catch (Throwable e) {
                    editor.remove("disabledFeatures");
                    FileLog.e(e);
                }
                editor.commit();
            }
        });
    }

    public static boolean isFeatureEnabled(String feature, BaseFragment fragment) {
        MessagesController messagesController = getInstance(UserConfig.selectedAccount);
        if (feature == null || feature.length() == 0 || messagesController.disabledFeatures.isEmpty() || fragment == null) {
            return true;
        }
        Iterator it = messagesController.disabledFeatures.iterator();
        while (it.hasNext()) {
            TL_disabledFeature disabledFeature = (TL_disabledFeature) it.next();
            if (disabledFeature.feature.equals(feature)) {
                if (fragment.getParentActivity() != null) {
                    Builder builder = new Builder(fragment.getParentActivity());
                    builder.setTitle("Oops!");
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                    builder.setMessage(disabledFeature.description);
                    fragment.showDialog(builder.create());
                }
                return false;
            }
        }
        return true;
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
        if (user.id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
            return new TL_inputUserSelf();
        }
        InputUser inputUser = new TL_inputUser();
        inputUser.user_id = user.id;
        inputUser.access_hash = user.access_hash;
        return inputUser;
    }

    public InputUser getInputUser(int user_id) {
        return getInputUser(getInstance(UserConfig.selectedAccount).getUser(Integer.valueOf(user_id)));
    }

    public static InputChannel getInputChannel(Chat chat) {
        if (!(chat instanceof TL_channel) && !(chat instanceof TL_channelForbidden)) {
            return new TL_inputChannelEmpty();
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
        InputPeer inputPeer;
        if (id < 0) {
            Chat chat = getChat(Integer.valueOf(-id));
            if (ChatObject.isChannel(chat)) {
                inputPeer = new TL_inputPeerChannel();
                inputPeer.channel_id = -id;
                inputPeer.access_hash = chat.access_hash;
                return inputPeer;
            }
            inputPeer = new TL_inputPeerChat();
            inputPeer.chat_id = -id;
            return inputPeer;
        }
        User user = getUser(Integer.valueOf(id));
        inputPeer = new TL_inputPeerUser();
        inputPeer.user_id = id;
        if (user == null) {
            return inputPeer;
        }
        inputPeer.access_hash = user.access_hash;
        return inputPeer;
    }

    public Peer getPeer(int id) {
        Peer inputPeer;
        if (id < 0) {
            Chat chat = getChat(Integer.valueOf(-id));
            if ((chat instanceof TL_channel) || (chat instanceof TL_channelForbidden)) {
                inputPeer = new TL_peerChannel();
                inputPeer.channel_id = -id;
                return inputPeer;
            }
            inputPeer = new TL_peerChat();
            inputPeer.chat_id = -id;
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
            MessageObject obj = (MessageObject) this.dialogMessage.get(did);
            if (obj != null && (obj.getId() == msgId.intValue() || obj.messageOwner.local_id == msgId.intValue())) {
                obj.messageOwner.id = newMsgId.intValue();
                obj.messageOwner.send_state = 0;
            }
            TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(did);
            if (dialog != null && dialog.top_message == msgId.intValue()) {
                dialog.top_message = newMsgId.intValue();
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            }
            obj = (MessageObject) this.dialogMessagesByIds.remove(msgId);
            if (obj != null) {
                this.dialogMessagesByIds.put(newMsgId, obj);
            }
        } else if (id == NotificationCenter.updateMessageMedia) {
            Message message = args[0];
            MessageObject existMessageObject = (MessageObject) this.dialogMessagesByIds.get(Integer.valueOf(message.id));
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
        if (username == null || username.length() == 0) {
            return null;
        }
        return (TLObject) this.objectsByUsernames.get(username.toLowerCase());
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
        Semaphore semaphore = new Semaphore(0);
        ArrayList<TLObject> result = new ArrayList();
        MessagesStorage.getInstance(this.currentAccount).getEncryptedChat(chat_id, semaphore, result);
        try {
            semaphore.acquire();
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
        return (ExportedChatInvite) this.exportedChats.get(Integer.valueOf(chat_id));
    }

    public boolean putUser(User user, boolean fromCache) {
        if (user == null) {
            return false;
        }
        if (!fromCache || user.id / 1000 == 333 || user.id == 777000) {
            fromCache = false;
        } else {
            fromCache = true;
        }
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
                return false;
            } else if (fromCache) {
                return false;
            } else {
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
                    return false;
                }
                oldUser.flags &= -33;
                oldUser.photo = null;
                return false;
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
            return false;
        } else if (!oldUser.min) {
            return false;
        } else {
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
            return false;
        }
    }

    public void putUsers(ArrayList<User> users, boolean fromCache) {
        if (users != null && !users.isEmpty()) {
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
                if (chat.min) {
                    if (oldChat == null) {
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
                } else if (!fromCache) {
                    if (oldChat != null) {
                        int newFlags;
                        if (chat.version != oldChat.version) {
                            this.loadedFullChats.remove(Integer.valueOf(chat.id));
                        }
                        if (oldChat.participants_count != 0 && chat.participants_count == 0) {
                            chat.participants_count = oldChat.participants_count;
                            chat.flags |= 131072;
                        }
                        int oldFlags = oldChat.banned_rights != null ? oldChat.banned_rights.flags : 0;
                        if (chat.banned_rights != null) {
                            newFlags = chat.banned_rights.flags;
                        } else {
                            newFlags = 0;
                        }
                        if (oldFlags != newFlags) {
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
                        chat.flags |= 131072;
                    }
                    this.chats.put(Integer.valueOf(chat.id), chat);
                }
            }
        }
    }

    public void putChats(ArrayList<Chat> chats, boolean fromCache) {
        if (chats != null && !chats.isEmpty()) {
            int count = chats.size();
            for (int a = 0; a < count; a++) {
                putChat((Chat) chats.get(a), fromCache);
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
        if (encryptedChats != null && !encryptedChats.isEmpty()) {
            int count = encryptedChats.size();
            for (int a = 0; a < count; a++) {
                putEncryptedChat((EncryptedChat) encryptedChats.get(a), fromCache);
            }
        }
    }

    public TL_userFull getUserFull(int uid) {
        return (TL_userFull) this.fullUsers.get(Integer.valueOf(uid));
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
            InputPeer inputPeer;
            if (dialogs != null) {
                for (int a = 0; a < dialogs.size(); a++) {
                    inputPeer = getInputPeer((int) ((TL_dialog) dialogs.get(a)).id);
                    if (!(inputPeer instanceof TL_inputPeerChannel) || inputPeer.access_hash != 0) {
                        req.peers.add(inputPeer);
                    }
                }
            } else {
                inputPeer = getInputPeer((int) did);
                if (!(inputPeer instanceof TL_inputPeerChannel) || inputPeer.access_hash != 0) {
                    req.peers.add(inputPeer);
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
        ArrayList<Integer> array = (ArrayList) this.channelAdmins.get(Integer.valueOf(chatId));
        return array != null && array.indexOf(Integer.valueOf(uid)) >= 0;
    }

    public void loadChannelAdmins(final int chatId, boolean cache) {
        if (this.loadingChannelAdmins.indexOfKey(chatId) < 0) {
            this.loadingChannelAdmins.put(chatId, 0);
            if (cache) {
                MessagesStorage.getInstance(this.currentAccount).loadChannelAdmins(chatId);
                return;
            }
            TL_channels_getParticipants req = new TL_channels_getParticipants();
            ArrayList<Integer> array = (ArrayList) this.channelAdmins.get(Integer.valueOf(chatId));
            if (array != null) {
                long acc = 0;
                for (int a = 0; a < array.size(); a++) {
                    acc = (((20261 * acc) + 2147483648L) + ((long) ((Integer) array.get(a)).intValue())) % 2147483648L;
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

    public void processLoadedChannelAdmins(final ArrayList<Integer> array, final int chatId, final boolean cache) {
        Collections.sort(array);
        if (!cache) {
            MessagesStorage.getInstance(this.currentAccount).putChannelAdmins(chatId, array);
        }
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                MessagesController.this.loadingChannelAdmins.delete(chatId);
                MessagesController.this.channelAdmins.put(Integer.valueOf(chatId), array);
                if (cache) {
                    MessagesController.this.loadChannelAdmins(chatId, false);
                }
            }
        });
    }

    public void loadFullChat(int chat_id, int classGuid, boolean force) {
        boolean loaded = this.loadedFullChats.contains(Integer.valueOf(chat_id));
        if (!this.loadingFullChats.contains(Integer.valueOf(chat_id))) {
            if (force || !loaded) {
                TLObject request;
                this.loadingFullChats.add(Integer.valueOf(chat_id));
                final long dialog_id = (long) (-chat_id);
                final Chat chat = getChat(Integer.valueOf(chat_id));
                TLObject req;
                if (ChatObject.isChannel(chat)) {
                    req = new TL_channels_getFullChannel();
                    req.channel = getInputChannel(chat);
                    request = req;
                    if (chat.megagroup) {
                        loadChannelAdmins(chat_id, !loaded);
                    }
                } else {
                    req = new TL_messages_getFullChat();
                    req.chat_id = chat_id;
                    request = req;
                    if (this.dialogs_read_inbox_max.get(Long.valueOf(dialog_id)) == null || this.dialogs_read_outbox_max.get(Long.valueOf(dialog_id)) == null) {
                        reloadDialogsReadValue(null, dialog_id);
                    }
                }
                final int i = chat_id;
                final int i2 = classGuid;
                int reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new RequestDelegate() {
                    public void run(TLObject response, final TL_error error) {
                        if (error == null) {
                            final TL_messages_chatFull res = (TL_messages_chatFull) response;
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, res.chats, true, true);
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).updateChatInfo(res.full_chat, false);
                            if (ChatObject.isChannel(chat)) {
                                ArrayList<Update> arrayList;
                                Integer value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(dialog_id));
                                if (value == null) {
                                    value = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, dialog_id));
                                }
                                MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(dialog_id), Integer.valueOf(Math.max(res.full_chat.read_inbox_max_id, value.intValue())));
                                if (value.intValue() == 0) {
                                    arrayList = new ArrayList();
                                    TL_updateReadChannelInbox update = new TL_updateReadChannelInbox();
                                    update.channel_id = i;
                                    update.max_id = res.full_chat.read_inbox_max_id;
                                    arrayList.add(update);
                                    MessagesController.this.processUpdateArray(arrayList, null, null, false);
                                }
                                value = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(dialog_id));
                                if (value == null) {
                                    value = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, dialog_id));
                                }
                                MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(dialog_id), Integer.valueOf(Math.max(res.full_chat.read_outbox_max_id, value.intValue())));
                                if (value.intValue() == 0) {
                                    arrayList = new ArrayList();
                                    TL_updateReadChannelOutbox update2 = new TL_updateReadChannelOutbox();
                                    update2.channel_id = i;
                                    update2.max_id = res.full_chat.read_outbox_max_id;
                                    arrayList.add(update2);
                                    MessagesController.this.processUpdateArray(arrayList, null, null, false);
                                }
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.applyDialogNotificationsSettings((long) (-i), res.full_chat.notify_settings);
                                    for (int a = 0; a < res.full_chat.bot_info.size(); a++) {
                                        DataQuery.getInstance(MessagesController.this.currentAccount).putBotInfo((BotInfo) res.full_chat.bot_info.get(a));
                                    }
                                    MessagesController.this.exportedChats.put(Integer.valueOf(i), res.full_chat.exported_invite);
                                    MessagesController.this.loadingFullChats.remove(Integer.valueOf(i));
                                    MessagesController.this.loadedFullChats.add(Integer.valueOf(i));
                                    if (!res.chats.isEmpty()) {
                                        ((Chat) res.chats.get(0)).address = res.full_chat.about;
                                    }
                                    MessagesController.this.putUsers(res.users, false);
                                    MessagesController.this.putChats(res.chats, false);
                                    if (res.full_chat.stickerset != null) {
                                        DataQuery.getInstance(MessagesController.this.currentAccount).getGroupStickerSetById(res.full_chat.stickerset);
                                    }
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, res.full_chat, Integer.valueOf(i2), Boolean.valueOf(false), null);
                                }
                            });
                            return;
                        }
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                MessagesController.this.checkChannelError(error.text, i);
                                MessagesController.this.loadingFullChats.remove(Integer.valueOf(i));
                            }
                        });
                    }
                });
                if (classGuid != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(reqId, classGuid);
                }
            }
        }
    }

    public void loadFullUser(final User user, final int classGuid, boolean force) {
        if (user != null && !this.loadingFullUsers.contains(Integer.valueOf(user.id))) {
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
                                    MessagesController.this.fullUsers.put(Integer.valueOf(user.id), userFull);
                                    MessagesController.this.loadingFullUsers.remove(Integer.valueOf(user.id));
                                    MessagesController.this.loadedFullUsers.add(Integer.valueOf(user.id));
                                    String names = user.first_name + user.last_name + user.username;
                                    ArrayList<User> users = new ArrayList();
                                    users.add(userFull.user);
                                    MessagesController.this.putUsers(users, false);
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(users, null, false, true);
                                    if (!(names == null || names.equals(userFull.user.first_name + userFull.user.last_name + userFull.user.username))) {
                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(1));
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
            final ArrayList<Integer> result = new ArrayList();
            final Chat chat = ChatObject.getChatByDialog(dialog_id, this.currentAccount);
            TLObject req;
            if (ChatObject.isChannel(chat)) {
                req = new TL_channels_getMessages();
                req.channel = getInputChannel(chat);
                req.id = result;
                request = req;
            } else {
                req = new TL_messages_getMessages();
                req.id = result;
                request = req;
            }
            ArrayList<Integer> arrayList = (ArrayList) this.reloadingMessages.get(Long.valueOf(dialog_id));
            for (int a = 0; a < mids.size(); a++) {
                Integer mid = (Integer) mids.get(a);
                if (arrayList == null || !arrayList.contains(mid)) {
                    result.add(mid);
                }
            }
            if (!result.isEmpty()) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    this.reloadingMessages.put(Long.valueOf(dialog_id), arrayList);
                }
                arrayList.addAll(result);
                final long j = dialog_id;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            int a;
                            messages_Messages messagesRes = (messages_Messages) response;
                            HashMap<Integer, User> usersLocal = new HashMap();
                            for (a = 0; a < messagesRes.users.size(); a++) {
                                User u = (User) messagesRes.users.get(a);
                                usersLocal.put(Integer.valueOf(u.id), u);
                            }
                            HashMap<Integer, Chat> chatsLocal = new HashMap();
                            for (a = 0; a < messagesRes.chats.size(); a++) {
                                Chat c = (Chat) messagesRes.chats.get(a);
                                chatsLocal.put(Integer.valueOf(c.id), c);
                            }
                            Integer inboxValue = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(j));
                            if (inboxValue == null) {
                                inboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, j));
                                MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(j), inboxValue);
                            }
                            Integer outboxValue = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(j));
                            if (outboxValue == null) {
                                outboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, j));
                                MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(j), outboxValue);
                            }
                            ArrayList<MessageObject> objects = new ArrayList();
                            for (a = 0; a < messagesRes.messages.size(); a++) {
                                Integer num;
                                Message message = (Message) messagesRes.messages.get(a);
                                if (chat != null && chat.megagroup) {
                                    message.flags |= Integer.MIN_VALUE;
                                }
                                message.dialog_id = j;
                                if (message.out) {
                                    num = outboxValue;
                                } else {
                                    num = inboxValue;
                                }
                                message.unread = num.intValue() < message.id;
                                objects.add(new MessageObject(MessagesController.this.currentAccount, message, usersLocal, chatsLocal, true));
                            }
                            ImageLoader.saveMessagesThumbs(messagesRes.messages);
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(messagesRes, j, -1, 0, false);
                            final ArrayList<MessageObject> arrayList = objects;
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    ArrayList<Integer> arrayList = (ArrayList) MessagesController.this.reloadingMessages.get(Long.valueOf(j));
                                    if (arrayList != null) {
                                        arrayList.removeAll(result);
                                        if (arrayList.isEmpty()) {
                                            MessagesController.this.reloadingMessages.remove(Long.valueOf(j));
                                        }
                                    }
                                    MessageObject dialogObj = (MessageObject) MessagesController.this.dialogMessage.get(Long.valueOf(j));
                                    if (dialogObj != null) {
                                        int a = 0;
                                        while (a < arrayList.size()) {
                                            MessageObject obj = (MessageObject) arrayList.get(a);
                                            if (dialogObj == null || dialogObj.getId() != obj.getId()) {
                                                a++;
                                            } else {
                                                MessagesController.this.dialogMessage.put(Long.valueOf(j), obj);
                                                if (obj.messageOwner.to_id.channel_id == 0) {
                                                    obj = (MessageObject) MessagesController.this.dialogMessagesByIds.remove(Integer.valueOf(obj.getId()));
                                                    if (obj != null) {
                                                        MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(obj.getId()), obj);
                                                    }
                                                }
                                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                            }
                                        }
                                    }
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(j), arrayList);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    public void hideReportSpam(long dialogId, User currentUser, Chat currentChat) {
        if (currentUser != null || currentChat != null) {
            Editor editor = this.notificationsPreferences.edit();
            editor.putInt("spam3_" + dialogId, 1);
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
            editor.putInt("spam3_" + dialogId, 1);
            editor.commit();
            if (((int) dialogId) != 0) {
                TL_messages_reportSpam req = new TL_messages_reportSpam();
                if (currentChat != null) {
                    req.peer = getInputPeer(-currentChat.id);
                } else if (currentUser != null) {
                    req.peer = getInputPeer(currentUser.id);
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                }, 2);
            } else if (currentEncryptedChat != null && currentEncryptedChat.access_hash != 0) {
                TL_messages_reportEncryptedSpam req2 = new TL_messages_reportEncryptedSpam();
                req2.peer = new TL_inputEncryptedChat();
                req2.peer.chat_id = currentEncryptedChat.id;
                req2.peer.access_hash = currentEncryptedChat.access_hash;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                }, 2);
            }
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
            if (!this.loadingPeerSettings.containsKey(Long.valueOf(dialogId))) {
                this.loadingPeerSettings.put(Long.valueOf(dialogId), Boolean.valueOf(true));
                FileLog.d("request spam button for " + dialogId);
                if (this.notificationsPreferences.getInt("spam3_" + dialogId, 0) == 1) {
                    FileLog.d("spam button already hidden for " + dialogId);
                } else if (this.notificationsPreferences.getBoolean("spam_" + dialogId, false)) {
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
                                    MessagesController.this.loadingPeerSettings.remove(Long.valueOf(dialogId));
                                    Editor editor = MessagesController.this.notificationsPreferences.edit();
                                    editor.remove("spam_" + dialogId);
                                    editor.putInt("spam3_" + dialogId, 1);
                                    editor.commit();
                                }
                            });
                        }
                    });
                } else {
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
                                    MessagesController.this.loadingPeerSettings.remove(Long.valueOf(dialogId));
                                    if (response != null) {
                                        TL_peerSettings res = response;
                                        Editor editor = MessagesController.this.notificationsPreferences.edit();
                                        if (res.report_spam) {
                                            FileLog.d("show spam button for " + dialogId);
                                            editor.putInt("spam3_" + dialogId, 2);
                                            editor.commit();
                                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.peerSettingsDidLoaded, Long.valueOf(dialogId));
                                            return;
                                        }
                                        FileLog.d("don't show spam button for " + dialogId);
                                        editor.putInt("spam3_" + dialogId, 1);
                                        editor.commit();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }
    }

    protected void processNewChannelDifferenceParams(int pts, int pts_count, int channelId) {
        FileLog.e("processNewChannelDifferenceParams pts = " + pts + " pts_count = " + pts_count + " channeldId = " + channelId);
        if (DialogObject.isChannel((TL_dialog) this.dialogs_dict.get(Long.valueOf((long) (-channelId))))) {
            Integer channelPts = (Integer) this.channelsPts.get(Integer.valueOf(channelId));
            if (channelPts == null) {
                channelPts = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(channelId));
                if (channelPts.intValue() == 0) {
                    channelPts = Integer.valueOf(1);
                }
                this.channelsPts.put(Integer.valueOf(channelId), channelPts);
            }
            if (channelPts.intValue() + pts_count == pts) {
                FileLog.e("APPLY CHANNEL PTS");
                this.channelsPts.put(Integer.valueOf(channelId), Integer.valueOf(pts));
                MessagesStorage.getInstance(this.currentAccount).saveChannelPts(channelId, pts);
            } else if (channelPts.intValue() != pts) {
                Long updatesStartWaitTime = (Long) this.updatesStartWaitTimeChannels.get(Integer.valueOf(channelId));
                Boolean gettingDifferenceChannel = (Boolean) this.gettingDifferenceChannels.get(Integer.valueOf(channelId));
                if (gettingDifferenceChannel == null) {
                    gettingDifferenceChannel = Boolean.valueOf(false);
                }
                if (gettingDifferenceChannel.booleanValue() || updatesStartWaitTime == null || Math.abs(System.currentTimeMillis() - updatesStartWaitTime.longValue()) <= 1500) {
                    FileLog.e("ADD CHANNEL UPDATE TO QUEUE pts = " + pts + " pts_count = " + pts_count);
                    if (updatesStartWaitTime == null) {
                        this.updatesStartWaitTimeChannels.put(Integer.valueOf(channelId), Long.valueOf(System.currentTimeMillis()));
                    }
                    UserActionUpdatesPts updates = new UserActionUpdatesPts();
                    updates.pts = pts;
                    updates.pts_count = pts_count;
                    updates.chat_id = channelId;
                    ArrayList<Updates> arrayList = (ArrayList) this.updatesQueueChannels.get(Integer.valueOf(channelId));
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        this.updatesQueueChannels.put(Integer.valueOf(channelId), arrayList);
                    }
                    arrayList.add(updates);
                    return;
                }
                getChannelDifference(channelId);
            }
        }
    }

    protected void processNewDifferenceParams(int seq, int pts, int date, int pts_count) {
        FileLog.e("processNewDifferenceParams seq = " + seq + " pts = " + pts + " date = " + date + " pts_count = " + pts_count);
        if (pts != -1) {
            if (MessagesStorage.getInstance(this.currentAccount).lastPtsValue + pts_count == pts) {
                FileLog.e("APPLY PTS");
                MessagesStorage.getInstance(this.currentAccount).lastPtsValue = pts;
                MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).lastSeqValue, MessagesStorage.getInstance(this.currentAccount).lastPtsValue, MessagesStorage.getInstance(this.currentAccount).lastDateValue, MessagesStorage.getInstance(this.currentAccount).lastQtsValue);
            } else if (MessagesStorage.getInstance(this.currentAccount).lastPtsValue != pts) {
                if (this.gettingDifference || this.updatesStartWaitTimePts == 0 || Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimePts) <= 1500) {
                    FileLog.e("ADD UPDATE TO QUEUE pts = " + pts + " pts_count = " + pts_count);
                    if (this.updatesStartWaitTimePts == 0) {
                        this.updatesStartWaitTimePts = System.currentTimeMillis();
                    }
                    UserActionUpdatesPts updates = new UserActionUpdatesPts();
                    updates.pts = pts;
                    updates.pts_count = pts_count;
                    this.updatesQueuePts.add(updates);
                } else {
                    getDifference();
                }
            }
        }
        if (seq == -1) {
            return;
        }
        if (MessagesStorage.getInstance(this.currentAccount).lastSeqValue + 1 == seq) {
            FileLog.e("APPLY SEQ");
            MessagesStorage.getInstance(this.currentAccount).lastSeqValue = seq;
            if (date != -1) {
                MessagesStorage.getInstance(this.currentAccount).lastDateValue = date;
            }
            MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).lastSeqValue, MessagesStorage.getInstance(this.currentAccount).lastPtsValue, MessagesStorage.getInstance(this.currentAccount).lastDateValue, MessagesStorage.getInstance(this.currentAccount).lastQtsValue);
        } else if (MessagesStorage.getInstance(this.currentAccount).lastSeqValue == seq) {
        } else {
            if (this.gettingDifference || this.updatesStartWaitTimeSeq == 0 || Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimeSeq) <= 1500) {
                FileLog.e("ADD UPDATE TO QUEUE seq = " + seq);
                if (this.updatesStartWaitTimeSeq == 0) {
                    this.updatesStartWaitTimeSeq = System.currentTimeMillis();
                }
                UserActionUpdatesSeq updates2 = new UserActionUpdatesSeq();
                updates2.seq = seq;
                this.updatesQueueSeq.add(updates2);
                return;
            }
            getDifference();
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
        if (this.currentDeletingTaskMids == null) {
            return false;
        }
        if (!runnable && (this.currentDeletingTaskTime == 0 || this.currentDeletingTaskTime > currentServerTime)) {
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
        if (fromCache) {
            MessagesStorage.getInstance(this.currentAccount).getDialogPhotos(did, count, max_id, classGuid);
        } else if (did > 0) {
            User user = getUser(Integer.valueOf(did));
            if (user != null) {
                TL_photos_getUserPhotos req = new TL_photos_getUserPhotos();
                req.limit = count;
                req.offset = 0;
                req.max_id = (long) ((int) max_id);
                req.user_id = getInputUser(user);
                r4 = did;
                r5 = count;
                r6 = max_id;
                r8 = classGuid;
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            MessagesController.this.processLoadedUserPhotos((photos_Photos) response, r4, r5, r6, false, r8);
                        }
                    }
                }), classGuid);
            }
        } else if (did < 0) {
            TL_messages_search req2 = new TL_messages_search();
            req2.filter = new TL_inputMessagesFilterChatPhotos();
            req2.limit = count;
            req2.offset_id = (int) max_id;
            req2.q = TtmlNode.ANONYMOUS_REGION_ID;
            req2.peer = getInputPeer(did);
            r4 = did;
            r5 = count;
            r6 = max_id;
            r8 = classGuid;
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        messages_Messages messages = (messages_Messages) response;
                        TL_photos_photos res = new TL_photos_photos();
                        res.count = messages.count;
                        res.users.addAll(messages.users);
                        for (int a = 0; a < messages.messages.size(); a++) {
                            Message message = (Message) messages.messages.get(a);
                            if (!(message.action == null || message.action.photo == null)) {
                                res.photos.add(message.action.photo);
                            }
                        }
                        MessagesController.this.processLoadedUserPhotos(res, r4, r5, r6, false, r8);
                    }
                }
            }), classGuid);
        }
    }

    public void blockUser(int user_id) {
        final User user = getUser(Integer.valueOf(user_id));
        if (user != null && !this.blockedUsers.contains(Integer.valueOf(user_id))) {
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

    public void setUserBannedRole(int chatId, User user, TL_channelBannedRights rights, boolean isMegagroup, BaseFragment parentFragment) {
        if (user != null && rights != null) {
            final TL_channels_editBanned req = new TL_channels_editBanned();
            req.channel = getInputChannel(chatId);
            req.user_id = getInputUser(user);
            req.banned_rights = rights;
            final int i = chatId;
            final BaseFragment baseFragment = parentFragment;
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
                            boolean z = true;
                            int access$000 = MessagesController.this.currentAccount;
                            TL_error tL_error = error;
                            BaseFragment baseFragment = baseFragment;
                            TLObject tLObject = req;
                            Object[] objArr = new Object[1];
                            if (z) {
                                z = false;
                            }
                            objArr[0] = Boolean.valueOf(z);
                            AlertsCreator.processError(access$000, tL_error, baseFragment, tLObject, objArr);
                        }
                    });
                }
            });
        }
    }

    public void setUserAdminRole(int chatId, User user, TL_channelAdminRights rights, boolean isMegagroup, BaseFragment parentFragment) {
        if (user != null && rights != null) {
            final TL_channels_editAdmin req = new TL_channels_editAdmin();
            req.channel = getInputChannel(chatId);
            req.user_id = getInputUser(user);
            req.admin_rights = rights;
            final int i = chatId;
            final BaseFragment baseFragment = parentFragment;
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
                            boolean z = true;
                            int access$000 = MessagesController.this.currentAccount;
                            TL_error tL_error = error;
                            BaseFragment baseFragment = baseFragment;
                            TLObject tLObject = req;
                            Object[] objArr = new Object[1];
                            if (z) {
                                z = false;
                            }
                            objArr[0] = Boolean.valueOf(z);
                            AlertsCreator.processError(access$000, tL_error, baseFragment, tLObject, objArr);
                        }
                    });
                }
            });
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
        if (UserConfig.getInstance(this.currentAccount).isClientActivated() && !this.loadingBlockedUsers) {
            this.loadingBlockedUsers = true;
            if (cache) {
                MessagesStorage.getInstance(this.currentAccount).getBlockedUsers();
                return;
            }
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
                return;
            }
            return;
        }
        TL_photos_deletePhotos req2 = new TL_photos_deletePhotos();
        req2.id.add(photo);
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
            }
        });
    }

    public void processLoadedUserPhotos(photos_Photos res, int did, int count, long max_id, boolean fromCache, int classGuid) {
        if (!fromCache) {
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(res.users, null, true, true);
            MessagesStorage.getInstance(this.currentAccount).putDialogPhotos(did, res);
        } else if (res == null || res.photos.isEmpty()) {
            loadDialogPhotos(did, count, max_id, false, classGuid);
            return;
        }
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
            this.uploadingAvatar = FileLoader.getDirectory(4) + "/" + bigPhoto.location.volume_id + "_" + bigPhoto.location.local_id + ".jpg";
            FileLoader.getInstance(this.currentAccount).uploadFile(this.uploadingAvatar, false, true, 16777216);
        }
    }

    public void markChannelDialogMessageAsDeleted(ArrayList<Integer> messages, int channelId) {
        MessageObject obj = (MessageObject) this.dialogMessage.get(Long.valueOf((long) (-channelId)));
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
        TL_channels_deleteMessages req;
        long newTaskId;
        NativeByteBuffer data;
        Throwable e;
        final int i;
        if ((messages != null && !messages.isEmpty()) || taskRequest != null) {
            ArrayList<Integer> toSend = null;
            if (taskId == 0) {
                int a;
                if (channelId == 0) {
                    for (a = 0; a < messages.size(); a++) {
                        MessageObject obj = (MessageObject) this.dialogMessagesByIds.get((Integer) messages.get(a));
                        if (obj != null) {
                            obj.deleted = true;
                        }
                    }
                } else {
                    markChannelDialogMessageAsDeleted(messages, channelId);
                }
                toSend = new ArrayList();
                for (a = 0; a < messages.size(); a++) {
                    Integer mid = (Integer) messages.get(a);
                    if (mid.intValue() > 0) {
                        toSend.add(mid);
                    }
                }
                MessagesStorage.getInstance(this.currentAccount).markMessagesAsDeleted((ArrayList) messages, true, channelId);
                MessagesStorage.getInstance(this.currentAccount).updateDialogsWithDeletedMessages(messages, null, true, channelId);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, messages, Integer.valueOf(channelId));
            }
            NativeByteBuffer data2;
            if (channelId != 0) {
                if (taskRequest != null) {
                    req = (TL_channels_deleteMessages) taskRequest;
                    newTaskId = taskId;
                } else {
                    req = new TL_channels_deleteMessages();
                    req.id = toSend;
                    req.channel = getInputChannel(channelId);
                    data2 = null;
                    try {
                        data = new NativeByteBuffer(req.getObjectSize() + 8);
                        try {
                            data.writeInt32(7);
                            data.writeInt32(channelId);
                            req.serializeToStream(data);
                            data2 = data;
                        } catch (Exception e2) {
                            e = e2;
                            data2 = data;
                            FileLog.e(e);
                            newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data2);
                            i = channelId;
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
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
                            return;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        FileLog.e(e);
                        newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data2);
                        i = channelId;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
                        return;
                    }
                    newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data2);
                }
                i = channelId;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
                return;
            }
            TL_messages_deleteMessages req2;
            if (!(randoms == null || encryptedChat == null || randoms.isEmpty())) {
                SecretChatHelper.getInstance(this.currentAccount).sendMessagesDeleteMessage(encryptedChat, randoms, null);
            }
            if (taskRequest != null) {
                req2 = (TL_messages_deleteMessages) taskRequest;
                newTaskId = taskId;
            } else {
                req2 = new TL_messages_deleteMessages();
                req2.id = toSend;
                req2.revoke = forAll;
                data2 = null;
                try {
                    data = new NativeByteBuffer(req2.getObjectSize() + 8);
                    try {
                        data.writeInt32(7);
                        data.writeInt32(channelId);
                        req2.serializeToStream(data);
                        data2 = data;
                    } catch (Exception e4) {
                        e = e4;
                        data2 = data;
                        FileLog.e(e);
                        newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data2);
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
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
                } catch (Exception e5) {
                    e = e5;
                    FileLog.e(e);
                    newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data2);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, /* anonymous class already generated */);
                }
                newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data2);
            }
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, /* anonymous class already generated */);
        }
    }

    public void pinChannelMessage(Chat chat, int id, boolean notify) {
        TL_channels_updatePinnedMessage req = new TL_channels_updatePinnedMessage();
        req.channel = getInputChannel(chat);
        req.id = id;
        req.silent = !notify;
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
        int lower_part = (int) did;
        int high_id = (int) (did >> 32);
        int max_id_delete = max_id;
        if (onlyHistory == 2) {
            MessagesStorage.getInstance(this.currentAccount).deleteDialog(did, onlyHistory);
            return;
        }
        if (onlyHistory == 0 || onlyHistory == 3) {
            DataQuery.getInstance(this.currentAccount).uninstallShortcut(did);
        }
        if (first) {
            final long j;
            MessagesStorage.getInstance(this.currentAccount).deleteDialog(did, onlyHistory);
            TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(Long.valueOf(did));
            if (dialog != null) {
                if (max_id_delete == 0) {
                    max_id_delete = Math.max(0, dialog.top_message);
                }
                if (onlyHistory == 0 || onlyHistory == 3) {
                    this.dialogs.remove(dialog);
                    if (this.dialogsServerOnly.remove(dialog) && DialogObject.isChannel(dialog)) {
                        j = did;
                        Utilities.stageQueue.postRunnable(new Runnable() {
                            public void run() {
                                MessagesController.this.channelsPts.remove(Integer.valueOf(-((int) j)));
                                MessagesController.this.shortPollChannels.delete(-((int) j));
                                MessagesController.this.needShortPollChannels.delete(-((int) j));
                            }
                        });
                    }
                    this.dialogsGroupsOnly.remove(dialog);
                    this.dialogs_dict.remove(Long.valueOf(did));
                    this.dialogs_read_inbox_max.remove(Long.valueOf(did));
                    this.dialogs_read_outbox_max.remove(Long.valueOf(did));
                    this.nextDialogsCacheOffset--;
                } else {
                    dialog.unread_count = 0;
                }
                MessageObject object = (MessageObject) this.dialogMessage.remove(Long.valueOf(dialog.id));
                int lastMessageId;
                if (object != null) {
                    lastMessageId = object.getId();
                    this.dialogMessagesByIds.remove(Integer.valueOf(object.getId()));
                } else {
                    lastMessageId = dialog.top_message;
                    object = (MessageObject) this.dialogMessagesByIds.remove(Integer.valueOf(dialog.top_message));
                }
                if (!(object == null || object.messageOwner.random_id == 0)) {
                    this.dialogMessagesByRandomIds.remove(Long.valueOf(object.messageOwner.random_id));
                }
                if (onlyHistory != 1 || lower_part == 0 || lastMessageId <= 0) {
                    dialog.top_message = 0;
                } else {
                    Message message = new TL_messageService();
                    message.id = dialog.top_message;
                    message.out = ((long) UserConfig.getInstance(this.currentAccount).getClientUserId()) == did;
                    message.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
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
                    MessageObject messageObject = new MessageObject(this.currentAccount, message, null, this.createdDialogIds.contains(Long.valueOf(message.dialog_id)));
                    ArrayList<MessageObject> objArr = new ArrayList();
                    objArr.add(messageObject);
                    ArrayList arr = new ArrayList();
                    arr.add(message);
                    updateInterfaceWithMessages(did, objArr);
                    MessagesStorage.getInstance(this.currentAccount).putMessages(arr, false, true, false, 0);
                }
            }
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.removeAllMessagesFromDialog, Long.valueOf(did), Boolean.valueOf(false));
            j = did;
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                public void run() {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.getInstance(MessagesController.this.currentAccount).removeNotificationsForDialog(j);
                        }
                    });
                }
            });
        }
        if (high_id != 1 && onlyHistory != 3) {
            if (lower_part != 0) {
                InputPeer peer = getInputPeer(lower_part);
                if (peer == null) {
                    return;
                }
                TLObject req;
                if (!(peer instanceof TL_inputPeerChannel)) {
                    int i;
                    req = new TL_messages_deleteHistory();
                    req.peer = peer;
                    if (onlyHistory == 0) {
                        i = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    } else {
                        i = max_id_delete;
                    }
                    req.max_id = i;
                    req.just_clear = onlyHistory != 0;
                    final int max_id_delete_final = max_id_delete;
                    final long j2 = did;
                    final int i2 = onlyHistory;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            if (error == null) {
                                TL_messages_affectedHistory res = (TL_messages_affectedHistory) response;
                                if (res.offset > 0) {
                                    MessagesController.this.deleteDialog(j2, false, i2, max_id_delete_final);
                                }
                                MessagesController.this.processNewDifferenceParams(-1, res.pts, -1, res.pts_count);
                            }
                        }
                    }, 64);
                } else if (onlyHistory != 0) {
                    req = new TL_channels_deleteHistory();
                    req.channel = new TL_inputChannel();
                    req.channel.channel_id = peer.channel_id;
                    req.channel.access_hash = peer.access_hash;
                    req.max_id = max_id_delete > 0 ? max_id_delete : ConnectionsManager.DEFAULT_DATACENTER_ID;
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                        }
                    }, 64);
                }
            } else if (onlyHistory == 1) {
                SecretChatHelper.getInstance(this.currentAccount).sendClearHistoryMessage(getEncryptedChat(Integer.valueOf(high_id)), null);
            } else {
                SecretChatHelper.getInstance(this.currentAccount).declineSecretChat(high_id);
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
        if (!this.loadingFullParticipants.contains(chat_id) && !this.loadedFullParticipants.contains(chat_id)) {
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

    public void loadChatInfo(int chat_id, Semaphore semaphore, boolean force) {
        MessagesStorage.getInstance(this.currentAccount).loadChatInfo(chat_id, semaphore, force, false);
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
        int key;
        long currentTime = System.currentTimeMillis();
        checkDeletingTask(false);
        if (UserConfig.getInstance(this.currentAccount).isClientActivated()) {
            TL_account_updateStatus req;
            if (ConnectionsManager.getInstance(this.currentAccount).getPauseTime() == 0 && ApplicationLoader.isScreenOn && !ApplicationLoader.mainInterfacePausedStageQueue) {
                if (ApplicationLoader.mainInterfacePausedStageQueueTime != 0 && Math.abs(ApplicationLoader.mainInterfacePausedStageQueueTime - System.currentTimeMillis()) > 1000 && this.statusSettingState != 1 && (this.lastStatusUpdateTime == 0 || Math.abs(System.currentTimeMillis() - this.lastStatusUpdateTime) >= 55000 || this.offlineSent)) {
                    this.statusSettingState = 1;
                    if (this.statusRequest != 0) {
                        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.statusRequest, true);
                    }
                    req = new TL_account_updateStatus();
                    req.offline = false;
                    this.statusRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
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
            } else if (!(this.statusSettingState == 2 || this.offlineSent || Math.abs(System.currentTimeMillis() - ConnectionsManager.getInstance(this.currentAccount).getPauseTime()) < 2000)) {
                this.statusSettingState = 2;
                if (this.statusRequest != 0) {
                    ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.statusRequest, true);
                }
                req = new TL_account_updateStatus();
                req.offline = true;
                this.statusRequest = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
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
            if (!this.updatesQueueChannels.isEmpty()) {
                ArrayList<Integer> keys = new ArrayList(this.updatesQueueChannels.keySet());
                for (a = 0; a < keys.size(); a++) {
                    key = ((Integer) keys.get(a)).intValue();
                    Long updatesStartWaitTime = (Long) this.updatesStartWaitTimeChannels.get(Integer.valueOf(key));
                    if (updatesStartWaitTime != null && updatesStartWaitTime.longValue() + 1500 < currentTime) {
                        FileLog.e("QUEUE CHANNEL " + key + " UPDATES WAIT TIMEOUT - CHECK QUEUE");
                        processChannelsUpdatesQueue(key, 0);
                    }
                }
            }
            a = 0;
            while (a < 3) {
                if (getUpdatesStartTime(a) != 0 && getUpdatesStartTime(a) + 1500 < currentTime) {
                    FileLog.e(a + " QUEUE UPDATES WAIT TIMEOUT - CHECK QUEUE");
                    processUpdatesQueue(a, 0);
                }
                a++;
            }
        }
        if (this.channelViewsToSend.size() != 0 && Math.abs(System.currentTimeMillis() - this.lastViewsCheckTime) >= DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS) {
            this.lastViewsCheckTime = System.currentTimeMillis();
            a = 0;
            while (a < this.channelViewsToSend.size()) {
                key = this.channelViewsToSend.keyAt(a);
                final TL_messages_getMessagesViews req2 = new TL_messages_getMessagesViews();
                req2.peer = getInputPeer(key);
                req2.id = (ArrayList) this.channelViewsToSend.get(key);
                req2.increment = a == 0;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            Vector vector = (Vector) response;
                            final SparseArray<SparseIntArray> channelViews = new SparseArray();
                            SparseIntArray array = (SparseIntArray) channelViews.get(key);
                            if (array == null) {
                                array = new SparseIntArray();
                                channelViews.put(key, array);
                            }
                            int a = 0;
                            while (a < req2.id.size() && a < vector.objects.size()) {
                                array.put(((Integer) req2.id.get(a)).intValue(), ((Integer) vector.objects.get(a)).intValue());
                                a++;
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
            this.channelViewsToSend.clear();
        }
        if (!this.onlinePrivacy.isEmpty()) {
            ArrayList<Integer> toRemove = null;
            int currentServerTime = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            for (Entry<Integer, Integer> entry : this.onlinePrivacy.entrySet()) {
                if (((Integer) entry.getValue()).intValue() < currentServerTime - 30) {
                    if (toRemove == null) {
                        toRemove = new ArrayList();
                    }
                    toRemove.add(entry.getKey());
                }
            }
            if (toRemove != null) {
                Iterator it = toRemove.iterator();
                while (it.hasNext()) {
                    this.onlinePrivacy.remove((Integer) it.next());
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(4));
                    }
                });
            }
        }
        if (this.shortPollChannels.size() != 0) {
            for (a = 0; a < this.shortPollChannels.size(); a++) {
                key = this.shortPollChannels.keyAt(a);
                if (((long) this.shortPollChannels.get(key)) < System.currentTimeMillis() / 1000) {
                    this.shortPollChannels.delete(key);
                    if (this.needShortPollChannels.indexOfKey(key) >= 0) {
                        getChannelDifference(key);
                    }
                }
            }
        }
        if (!(this.printingUsers.isEmpty() && this.lastPrintingStringCount == this.printingUsers.size())) {
            boolean updated = false;
            ArrayList<Long> keys2 = new ArrayList(this.printingUsers.keySet());
            int b = 0;
            while (b < keys2.size()) {
                Long key2 = (Long) keys2.get(b);
                ArrayList<PrintingUser> arr = (ArrayList) this.printingUsers.get(key2);
                a = 0;
                while (a < arr.size()) {
                    int timeToRemove;
                    PrintingUser user = (PrintingUser) arr.get(a);
                    if (user.action instanceof TL_sendMessageGamePlayAction) {
                        timeToRemove = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS;
                    } else {
                        timeToRemove = 5900;
                    }
                    if (user.lastTime + ((long) timeToRemove) < currentTime) {
                        updated = true;
                        arr.remove(user);
                        a--;
                    }
                    a++;
                }
                if (arr.isEmpty()) {
                    this.printingUsers.remove(key2);
                    keys2.remove(b);
                    b--;
                }
                b++;
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
        LocationController.getInstance(this.currentAccount).update();
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

    private void updatePrintingStrings() {
        final HashMap<Long, CharSequence> newPrintingStrings = new HashMap();
        final HashMap<Long, Integer> newPrintingStringsTypes = new HashMap();
        ArrayList<Long> keys = new ArrayList(this.printingUsers.keySet());
        for (Entry<Long, ArrayList<PrintingUser>> entry : this.printingUsers.entrySet()) {
            long key = ((Long) entry.getKey()).longValue();
            ArrayList<PrintingUser> arr = (ArrayList) entry.getValue();
            int lower_id = (int) key;
            User user;
            if (lower_id > 0 || lower_id == 0 || arr.size() == 1) {
                PrintingUser pu = (PrintingUser) arr.get(0);
                if (getUser(Integer.valueOf(pu.userId)) != null) {
                    if (pu.action instanceof TL_sendMessageRecordAudioAction) {
                        if (lower_id < 0) {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("IsRecordingAudio", R.string.IsRecordingAudio, getUserNameForTyping(user)));
                        } else {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.getString("RecordingAudio", R.string.RecordingAudio));
                        }
                        newPrintingStringsTypes.put(Long.valueOf(key), Integer.valueOf(1));
                    } else if ((pu.action instanceof TL_sendMessageRecordRoundAction) || (pu.action instanceof TL_sendMessageUploadRoundAction)) {
                        if (lower_id < 0) {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("IsRecordingRound", R.string.IsRecordingRound, getUserNameForTyping(user)));
                        } else {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.getString("RecordingRound", R.string.RecordingRound));
                        }
                        newPrintingStringsTypes.put(Long.valueOf(key), Integer.valueOf(4));
                    } else if (pu.action instanceof TL_sendMessageUploadAudioAction) {
                        if (lower_id < 0) {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("IsSendingAudio", R.string.IsSendingAudio, getUserNameForTyping(user)));
                        } else {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.getString("SendingAudio", R.string.SendingAudio));
                        }
                        newPrintingStringsTypes.put(Long.valueOf(key), Integer.valueOf(2));
                    } else if ((pu.action instanceof TL_sendMessageUploadVideoAction) || (pu.action instanceof TL_sendMessageRecordVideoAction)) {
                        if (lower_id < 0) {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("IsSendingVideo", R.string.IsSendingVideo, getUserNameForTyping(user)));
                        } else {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.getString("SendingVideoStatus", R.string.SendingVideoStatus));
                        }
                        newPrintingStringsTypes.put(Long.valueOf(key), Integer.valueOf(2));
                    } else if (pu.action instanceof TL_sendMessageUploadDocumentAction) {
                        if (lower_id < 0) {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("IsSendingFile", R.string.IsSendingFile, getUserNameForTyping(user)));
                        } else {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.getString("SendingFile", R.string.SendingFile));
                        }
                        newPrintingStringsTypes.put(Long.valueOf(key), Integer.valueOf(2));
                    } else if (pu.action instanceof TL_sendMessageUploadPhotoAction) {
                        if (lower_id < 0) {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("IsSendingPhoto", R.string.IsSendingPhoto, getUserNameForTyping(user)));
                        } else {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.getString("SendingPhoto", R.string.SendingPhoto));
                        }
                        newPrintingStringsTypes.put(Long.valueOf(key), Integer.valueOf(2));
                    } else if (pu.action instanceof TL_sendMessageGamePlayAction) {
                        if (lower_id < 0) {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("IsSendingGame", R.string.IsSendingGame, getUserNameForTyping(user)));
                        } else {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.getString("SendingGame", R.string.SendingGame));
                        }
                        newPrintingStringsTypes.put(Long.valueOf(key), Integer.valueOf(3));
                    } else {
                        if (lower_id < 0) {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("IsTypingGroup", R.string.IsTypingGroup, getUserNameForTyping(user)));
                        } else {
                            newPrintingStrings.put(Long.valueOf(key), LocaleController.getString("Typing", R.string.Typing));
                        }
                        newPrintingStringsTypes.put(Long.valueOf(key), Integer.valueOf(0));
                    }
                }
            } else {
                int count = 0;
                StringBuilder label = new StringBuilder();
                Iterator it = arr.iterator();
                while (it.hasNext()) {
                    user = getUser(Integer.valueOf(((PrintingUser) it.next()).userId));
                    if (user != null) {
                        if (label.length() != 0) {
                            label.append(", ");
                        }
                        label.append(getUserNameForTyping(user));
                        count++;
                    }
                    if (count == 2) {
                        break;
                    }
                }
                if (label.length() != 0) {
                    if (count == 1) {
                        newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("IsTypingGroup", R.string.IsTypingGroup, label.toString()));
                    } else if (arr.size() > 2) {
                        String plural = LocaleController.getPluralString("AndMoreTypingGroup", arr.size() - 2);
                        newPrintingStrings.put(Long.valueOf(key), String.format(plural, new Object[]{label.toString(), Integer.valueOf(arr.size() - 2)}));
                    } else {
                        newPrintingStrings.put(Long.valueOf(key), LocaleController.formatString("AreTypingGroup", R.string.AreTypingGroup, label.toString()));
                    }
                    newPrintingStringsTypes.put(Long.valueOf(key), Integer.valueOf(0));
                }
            }
        }
        this.lastPrintingStringCount = newPrintingStrings.size();
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                MessagesController.this.printingStrings = newPrintingStrings;
                MessagesController.this.printingStringsTypes = newPrintingStringsTypes;
            }
        });
    }

    public void cancelTyping(int action, long dialog_id) {
        HashMap<Long, Boolean> typings = (HashMap) this.sendingTypings.get(Integer.valueOf(action));
        if (typings != null) {
            typings.remove(Long.valueOf(dialog_id));
        }
    }

    public void sendTyping(final long dialog_id, final int action, int classGuid) {
        if (dialog_id != 0) {
            HashMap<Long, Boolean> typings = (HashMap) this.sendingTypings.get(Integer.valueOf(action));
            if (typings == null || typings.get(Long.valueOf(dialog_id)) == null) {
                if (typings == null) {
                    typings = new HashMap();
                    this.sendingTypings.put(Integer.valueOf(action), typings);
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
                            }
                            typings.put(Long.valueOf(dialog_id), Boolean.valueOf(true));
                            reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                                public void run(TLObject response, TL_error error) {
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            HashMap<Long, Boolean> typings = (HashMap) MessagesController.this.sendingTypings.get(Integer.valueOf(action));
                                            if (typings != null) {
                                                typings.remove(Long.valueOf(dialog_id));
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
                        typings.put(Long.valueOf(dialog_id), Boolean.valueOf(true));
                        reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                            public void run(TLObject response, TL_error error) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        HashMap<Long, Boolean> typings = (HashMap) MessagesController.this.sendingTypings.get(Integer.valueOf(action));
                                        if (typings != null) {
                                            typings.remove(Long.valueOf(dialog_id));
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
        FileLog.e("load messages in chat " + dialog_id + " count " + count + " max_id " + max_id + " cache " + fromCache + " mindate = " + midDate + " guid " + classGuid + " load_type " + load_type + " last_message_id " + last_message_id + " index " + loadIndex + " firstUnread " + first_unread + " unread_count " + unread_count + " last_date " + last_date + " queryFromServer " + queryFromServer);
        int lower_part = (int) dialog_id;
        if (fromCache || lower_part == 0) {
            MessagesStorage.getInstance(this.currentAccount).getMessages(dialog_id, count, max_id, offset_date, midDate, classGuid, load_type, isChannel, loadIndex);
            return;
        }
        TLObject req = new TL_messages_getHistory();
        req.peer = getInputPeer(lower_part);
        if (load_type == 4) {
            req.add_offset = (-count) + 5;
        } else if (load_type == 3) {
            req.add_offset = (-count) / 2;
        } else if (load_type == 1) {
            req.add_offset = (-count) - 1;
        } else if (load_type == 2 && max_id != 0) {
            req.add_offset = (-count) + 6;
        } else if (lower_part < 0 && max_id != 0 && ChatObject.isChannel(getChat(Integer.valueOf(-lower_part)))) {
            req.add_offset = -1;
            req.limit++;
        }
        req.limit = count;
        req.offset_id = max_id;
        req.offset_date = offset_date;
        final int i = count;
        final int i2 = max_id;
        final int i3 = offset_date;
        final long j = dialog_id;
        final int i4 = classGuid;
        final int i5 = first_unread;
        final int i6 = last_message_id;
        final int i7 = unread_count;
        final int i8 = last_date;
        final int i9 = load_type;
        final boolean z = isChannel;
        final int i10 = loadIndex;
        final boolean z2 = queryFromServer;
        final int i11 = mentionsCount;
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (response != null) {
                    messages_Messages res = (messages_Messages) response;
                    if (res.messages.size() > i) {
                        res.messages.remove(0);
                    }
                    int mid = i2;
                    if (i3 != 0 && !res.messages.isEmpty()) {
                        mid = ((Message) res.messages.get(res.messages.size() - 1)).id;
                        for (int a = res.messages.size() - 1; a >= 0; a--) {
                            Message message = (Message) res.messages.get(a);
                            if (message.date > i3) {
                                mid = message.id;
                                break;
                            }
                        }
                    }
                    MessagesController.this.processLoadedMessages(res, j, i, mid, i3, false, i4, i5, i6, i7, i8, i9, z, false, i10, z2, i11);
                }
            }
        }), classGuid);
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
                                int a;
                                if (response instanceof TL_messageMediaWebPage) {
                                    TL_messageMediaWebPage media = response;
                                    if ((media.webpage instanceof TL_webPage) || (media.webpage instanceof TL_webPageEmpty)) {
                                        for (a = 0; a < arrayList.size(); a++) {
                                            ((MessageObject) arrayList.get(a)).messageOwner.media.webpage = media.webpage;
                                            if (a == 0) {
                                                ImageLoader.saveMessageThumbs(((MessageObject) arrayList.get(a)).messageOwner);
                                            }
                                            messagesRes.messages.add(((MessageObject) arrayList.get(a)).messageOwner);
                                        }
                                    } else {
                                        MessagesController.this.reloadingWebpagesPending.put(Long.valueOf(media.webpage.id), arrayList);
                                    }
                                } else {
                                    for (a = 0; a < arrayList.size(); a++) {
                                        ((MessageObject) arrayList.get(a)).messageOwner.media.webpage = new TL_webPageEmpty();
                                        messagesRes.messages.add(((MessageObject) arrayList.get(a)).messageOwner);
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
        FileLog.e("processLoadedMessages size " + messagesRes.messages.size() + " in chat " + dialog_id + " count " + count + " max_id " + max_id + " cache " + isCache + " guid " + classGuid + " load_type " + load_type + " last_message_id " + last_message_id + " isChannel " + isChannel + " index " + loadIndex + " firstUnread " + first_unread + " unread_count " + unread_count + " last_date " + last_date + " queryFromServer " + queryFromServer);
        final messages_Messages org_telegram_tgnet_TLRPC_messages_Messages = messagesRes;
        final long j = dialog_id;
        final boolean z = isCache;
        final int i = count;
        final int i2 = load_type;
        final boolean z2 = queryFromServer;
        final int i3 = first_unread;
        final int i4 = max_id;
        final int i5 = offset_date;
        final int i6 = classGuid;
        final int i7 = last_message_id;
        final boolean z3 = isChannel;
        final int i8 = loadIndex;
        final int i9 = unread_count;
        final int i10 = last_date;
        final int i11 = mentionsCount;
        final boolean z4 = isEnd;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                int a;
                boolean createDialog = false;
                boolean isMegagroup = false;
                if (org_telegram_tgnet_TLRPC_messages_Messages instanceof TL_messages_channelMessages) {
                    int channelId = -((int) j);
                    if (((Integer) MessagesController.this.channelsPts.get(Integer.valueOf(channelId))) == null && Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getChannelPtsSync(channelId)).intValue() == 0) {
                        MessagesController.this.channelsPts.put(Integer.valueOf(channelId), Integer.valueOf(org_telegram_tgnet_TLRPC_messages_Messages.pts));
                        createDialog = true;
                        if (MessagesController.this.needShortPollChannels.indexOfKey(channelId) < 0 || MessagesController.this.shortPollChannels.indexOfKey(channelId) >= 0) {
                            MessagesController.this.getChannelDifference(channelId);
                        } else {
                            MessagesController.this.getChannelDifference(channelId, 2, 0, null);
                        }
                    }
                    for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Messages.chats.size(); a++) {
                        Chat chat = (Chat) org_telegram_tgnet_TLRPC_messages_Messages.chats.get(a);
                        if (chat.id == channelId) {
                            isMegagroup = chat.megagroup;
                            break;
                        }
                    }
                }
                int lower_id = (int) j;
                int high_id = (int) (j >> 32);
                if (!z) {
                    ImageLoader.saveMessagesThumbs(org_telegram_tgnet_TLRPC_messages_Messages.messages);
                }
                if (high_id == 1 || lower_id == 0 || !z || org_telegram_tgnet_TLRPC_messages_Messages.messages.size() != 0) {
                    Message message;
                    AbstractMap usersDict = new HashMap();
                    AbstractMap chatsDict = new HashMap();
                    for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Messages.users.size(); a++) {
                        User u = (User) org_telegram_tgnet_TLRPC_messages_Messages.users.get(a);
                        usersDict.put(Integer.valueOf(u.id), u);
                    }
                    for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Messages.chats.size(); a++) {
                        Chat c = (Chat) org_telegram_tgnet_TLRPC_messages_Messages.chats.get(a);
                        chatsDict.put(Integer.valueOf(c.id), c);
                    }
                    int size = org_telegram_tgnet_TLRPC_messages_Messages.messages.size();
                    if (!z) {
                        Integer inboxValue = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(j));
                        if (inboxValue == null) {
                            inboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, j));
                            MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(j), inboxValue);
                        }
                        Integer outboxValue = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(j));
                        if (outboxValue == null) {
                            outboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, j));
                            MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(j), outboxValue);
                        }
                        for (a = 0; a < size; a++) {
                            message = (Message) org_telegram_tgnet_TLRPC_messages_Messages.messages.get(a);
                            if (isMegagroup) {
                                message.flags |= Integer.MIN_VALUE;
                            }
                            if (message.action instanceof TL_messageActionChatDeleteUser) {
                                User user = (User) usersDict.get(Integer.valueOf(message.action.user_id));
                                if (user != null && user.bot) {
                                    message.reply_markup = new TL_replyKeyboardHide();
                                    message.flags |= 64;
                                }
                            }
                            if ((message.action instanceof TL_messageActionChatMigrateTo) || (message.action instanceof TL_messageActionChannelCreate)) {
                                message.unread = false;
                                message.media_unread = false;
                            } else {
                                message.unread = (message.out ? outboxValue : inboxValue).intValue() < message.id;
                            }
                        }
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(org_telegram_tgnet_TLRPC_messages_Messages, j, i2, i4, createDialog);
                    }
                    ArrayList<MessageObject> objects = new ArrayList();
                    ArrayList<Integer> messagesToReload = new ArrayList();
                    HashMap<String, ArrayList<MessageObject>> webpagesToReload = new HashMap();
                    for (a = 0; a < size; a++) {
                        message = (Message) org_telegram_tgnet_TLRPC_messages_Messages.messages.get(a);
                        message.dialog_id = j;
                        MessageObject messageObject = new MessageObject(MessagesController.this.currentAccount, message, usersDict, chatsDict, true);
                        objects.add(messageObject);
                        if (z) {
                            if (message.media instanceof TL_messageMediaUnsupported) {
                                if (message.media.bytes != null && (message.media.bytes.length == 0 || (message.media.bytes.length == 1 && message.media.bytes[0] < (byte) 74))) {
                                    messagesToReload.add(Integer.valueOf(message.id));
                                }
                            } else if (message.media instanceof TL_messageMediaWebPage) {
                                if ((message.media.webpage instanceof TL_webPagePending) && message.media.webpage.date <= ConnectionsManager.getInstance(MessagesController.this.currentAccount).getCurrentTime()) {
                                    messagesToReload.add(Integer.valueOf(message.id));
                                } else if (message.media.webpage instanceof TL_webPageUrlPending) {
                                    ArrayList<MessageObject> arrayList = (ArrayList) webpagesToReload.get(message.media.webpage.url);
                                    if (arrayList == null) {
                                        arrayList = new ArrayList();
                                        webpagesToReload.put(message.media.webpage.url, arrayList);
                                    }
                                    arrayList.add(messageObject);
                                }
                            }
                        }
                    }
                    final ArrayList<MessageObject> arrayList2 = objects;
                    final ArrayList<Integer> arrayList3 = messagesToReload;
                    final HashMap<String, ArrayList<MessageObject>> hashMap = webpagesToReload;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            MessagesController.this.putUsers(org_telegram_tgnet_TLRPC_messages_Messages.users, z);
                            MessagesController.this.putChats(org_telegram_tgnet_TLRPC_messages_Messages.chats, z);
                            int first_unread_final = ConnectionsManager.DEFAULT_DATACENTER_ID;
                            if (z2 && i2 == 2) {
                                for (int a = 0; a < org_telegram_tgnet_TLRPC_messages_Messages.messages.size(); a++) {
                                    Message message = (Message) org_telegram_tgnet_TLRPC_messages_Messages.messages.get(a);
                                    if (!message.out && message.id > i3 && message.id < first_unread_final) {
                                        first_unread_final = message.id;
                                    }
                                }
                            }
                            if (first_unread_final == ConnectionsManager.DEFAULT_DATACENTER_ID) {
                                first_unread_final = i3;
                            }
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesDidLoaded, Long.valueOf(j), Integer.valueOf(i), arrayList2, Boolean.valueOf(z), Integer.valueOf(first_unread_final), Integer.valueOf(i7), Integer.valueOf(i9), Integer.valueOf(i10), Integer.valueOf(i2), Boolean.valueOf(z4), Integer.valueOf(i6), Integer.valueOf(i8), Integer.valueOf(i4), Integer.valueOf(i11));
                            if (!arrayList3.isEmpty()) {
                                MessagesController.this.reloadMessages(arrayList3, j);
                            }
                            if (!hashMap.isEmpty()) {
                                MessagesController.this.reloadWebPages(j, hashMap);
                            }
                        }
                    });
                    return;
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        MessagesController messagesController = MessagesController.this;
                        long j = j;
                        int i = i;
                        int i2 = (i2 == 2 && z2) ? i3 : i4;
                        messagesController.loadMessages(j, i, i2, i5, false, 0, i6, i2, i7, z3, i8, i3, i9, i10, z2, i11);
                    }
                });
            }
        });
    }

    public void loadHintDialogs() {
        if (this.hintDialogs.isEmpty() && !TextUtils.isEmpty(this.installReferer)) {
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

    public void loadDialogs(int offset, final int count, boolean fromCache) {
        if (!this.loadingDialogs && !this.resetingDialogs) {
            this.loadingDialogs = true;
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            FileLog.e("load cacheOffset = " + offset + " count = " + count + " cache = " + fromCache);
            if (fromCache) {
                MessagesStorage.getInstance(this.currentAccount).getDialogs(offset == 0 ? 0 : this.nextDialogsCacheOffset, count);
                return;
            }
            TL_messages_getDialogs req = new TL_messages_getDialogs();
            req.limit = count;
            req.exclude_pinned = true;
            if (UserConfig.getInstance(this.currentAccount).dialogsLoadOffsetId == -1) {
                boolean found = false;
                for (int a = this.dialogs.size() - 1; a >= 0; a--) {
                    TL_dialog dialog = (TL_dialog) this.dialogs.get(a);
                    if (!dialog.pinned) {
                        int high_id = (int) (dialog.id >> 32);
                        if (!(((int) dialog.id) == 0 || high_id == 1 || dialog.top_message <= 0)) {
                            MessageObject message = (MessageObject) this.dialogMessage.get(Long.valueOf(dialog.id));
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
                                }
                                req.offset_peer = getInputPeer(id);
                                found = true;
                                if (!found) {
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

    public void forceResetDialogs() {
        resetDialogs(true, MessagesStorage.getInstance(this.currentAccount).lastSeqValue, MessagesStorage.getInstance(this.currentAccount).lastPtsValue, MessagesStorage.getInstance(this.currentAccount).lastDateValue, MessagesStorage.getInstance(this.currentAccount).lastQtsValue);
    }

    private void resetDialogs(boolean query, int seq, int newPts, int date, int qts) {
        if (query) {
            if (!this.resetingDialogs) {
                this.resetingDialogs = true;
                final int i = seq;
                final int i2 = newPts;
                final int i3 = date;
                final int i4 = qts;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_messages_getPinnedDialogs(), new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (response != null) {
                            MessagesController.this.resetDialogsPinned = (TL_messages_peerDialogs) response;
                            MessagesController.this.resetDialogs(false, i, i2, i3, i4);
                        }
                    }
                });
                TLObject req2 = new TL_messages_getDialogs();
                req2.limit = 100;
                req2.exclude_pinned = true;
                req2.offset_peer = new TL_inputPeerEmpty();
                i = seq;
                i2 = newPts;
                i3 = date;
                i4 = qts;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req2, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (error == null) {
                            MessagesController.this.resetDialogsAll = (messages_Dialogs) response;
                            MessagesController.this.resetDialogs(false, i, i2, i3, i4);
                        }
                    }
                });
            }
        } else if (this.resetDialogsPinned != null && this.resetDialogsAll != null) {
            int a;
            Message message;
            Chat chat;
            Integer value;
            int messagesCount = this.resetDialogsAll.messages.size();
            int dialogsCount = this.resetDialogsAll.dialogs.size();
            this.resetDialogsAll.dialogs.addAll(this.resetDialogsPinned.dialogs);
            this.resetDialogsAll.messages.addAll(this.resetDialogsPinned.messages);
            this.resetDialogsAll.users.addAll(this.resetDialogsPinned.users);
            this.resetDialogsAll.chats.addAll(this.resetDialogsPinned.chats);
            HashMap<Long, TL_dialog> new_dialogs_dict = new HashMap();
            HashMap<Long, MessageObject> new_dialogMessage = new HashMap();
            HashMap<Integer, User> usersDict = new HashMap();
            HashMap<Integer, Chat> chatsDict = new HashMap();
            for (a = 0; a < this.resetDialogsAll.users.size(); a++) {
                User u = (User) this.resetDialogsAll.users.get(a);
                usersDict.put(Integer.valueOf(u.id), u);
            }
            for (a = 0; a < this.resetDialogsAll.chats.size(); a++) {
                Chat c = (Chat) this.resetDialogsAll.chats.get(a);
                chatsDict.put(Integer.valueOf(c.id), c);
            }
            Message lastMessage = null;
            for (a = 0; a < this.resetDialogsAll.messages.size(); a++) {
                message = (Message) this.resetDialogsAll.messages.get(a);
                if (a < messagesCount && (lastMessage == null || message.date < lastMessage.date)) {
                    lastMessage = message;
                }
                MessageObject messageObject;
                if (message.to_id.channel_id != 0) {
                    chat = (Chat) chatsDict.get(Integer.valueOf(message.to_id.channel_id));
                    if (chat == null || !chat.left) {
                        if (chat != null && chat.megagroup) {
                            message.flags |= Integer.MIN_VALUE;
                        }
                        messageObject = new MessageObject(this.currentAccount, message, usersDict, chatsDict, false);
                        new_dialogMessage.put(Long.valueOf(messageObject.getDialogId()), messageObject);
                    }
                } else {
                    if (message.to_id.chat_id != 0) {
                        chat = (Chat) chatsDict.get(Integer.valueOf(message.to_id.chat_id));
                        if (!(chat == null || chat.migrated_to == null)) {
                        }
                    }
                    messageObject = new MessageObject(this.currentAccount, message, usersDict, chatsDict, false);
                    new_dialogMessage.put(Long.valueOf(messageObject.getDialogId()), messageObject);
                }
            }
            for (a = 0; a < this.resetDialogsAll.dialogs.size(); a++) {
                TL_dialog d = (TL_dialog) this.resetDialogsAll.dialogs.get(a);
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
                        MessageObject mess = (MessageObject) new_dialogMessage.get(Long.valueOf(d.id));
                        if (mess != null) {
                            d.last_message_date = mess.messageOwner.date;
                        }
                    }
                    if (DialogObject.isChannel(d)) {
                        chat = (Chat) chatsDict.get(Integer.valueOf(-((int) d.id)));
                        if (chat == null || !chat.left) {
                            this.channelsPts.put(Integer.valueOf(-((int) d.id)), Integer.valueOf(d.pts));
                        }
                    } else if (((int) d.id) < 0) {
                        chat = (Chat) chatsDict.get(Integer.valueOf(-((int) d.id)));
                        if (!(chat == null || chat.migrated_to == null)) {
                        }
                    }
                    new_dialogs_dict.put(Long.valueOf(d.id), d);
                    value = (Integer) this.dialogs_read_inbox_max.get(Long.valueOf(d.id));
                    if (value == null) {
                        value = Integer.valueOf(0);
                    }
                    this.dialogs_read_inbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_inbox_max_id)));
                    value = (Integer) this.dialogs_read_outbox_max.get(Long.valueOf(d.id));
                    if (value == null) {
                        value = Integer.valueOf(0);
                    }
                    this.dialogs_read_outbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_outbox_max_id)));
                }
            }
            ImageLoader.saveMessagesThumbs(this.resetDialogsAll.messages);
            for (a = 0; a < this.resetDialogsAll.messages.size(); a++) {
                message = (Message) this.resetDialogsAll.messages.get(a);
                if (message.action instanceof TL_messageActionChatDeleteUser) {
                    User user = (User) usersDict.get(Integer.valueOf(message.action.user_id));
                    if (user != null && user.bot) {
                        message.reply_markup = new TL_replyKeyboardHide();
                        message.flags |= 64;
                    }
                }
                if ((message.action instanceof TL_messageActionChatMigrateTo) || (message.action instanceof TL_messageActionChannelCreate)) {
                    message.unread = false;
                    message.media_unread = false;
                } else {
                    boolean z;
                    ConcurrentHashMap<Long, Integer> read_max = message.out ? this.dialogs_read_outbox_max : this.dialogs_read_inbox_max;
                    value = (Integer) read_max.get(Long.valueOf(message.dialog_id));
                    if (value == null) {
                        value = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                        read_max.put(Long.valueOf(message.dialog_id), value);
                    }
                    if (value.intValue() < message.id) {
                        z = true;
                    } else {
                        z = false;
                    }
                    message.unread = z;
                }
            }
            MessagesStorage.getInstance(this.currentAccount).resetDialogs(this.resetDialogsAll, messagesCount, seq, newPts, date, qts, new_dialogs_dict, new_dialogMessage, lastMessage, dialogsCount);
            this.resetDialogsPinned = null;
            this.resetDialogsAll = null;
        }
    }

    protected void completeDialogsReset(messages_Dialogs dialogsRes, int messagesCount, int seq, int newPts, int date, int qts, HashMap<Long, TL_dialog> new_dialogs_dict, HashMap<Long, MessageObject> new_dialogMessage, Message lastMessage) {
        final int i = newPts;
        final int i2 = date;
        final int i3 = qts;
        final messages_Dialogs org_telegram_tgnet_TLRPC_messages_Dialogs = dialogsRes;
        final HashMap<Long, TL_dialog> hashMap = new_dialogs_dict;
        final HashMap<Long, MessageObject> hashMap2 = new_dialogMessage;
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                MessagesController.this.gettingDifference = false;
                MessagesStorage.getInstance(MessagesController.this.currentAccount).lastPtsValue = i;
                MessagesStorage.getInstance(MessagesController.this.currentAccount).lastDateValue = i2;
                MessagesStorage.getInstance(MessagesController.this.currentAccount).lastQtsValue = i3;
                MessagesController.this.getDifference();
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        MessageObject messageObject;
                        MessagesController.this.resetingDialogs = false;
                        MessagesController.this.applyDialogsNotificationsSettings(org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs);
                        if (!UserConfig.getInstance(MessagesController.this.currentAccount).draftsLoaded) {
                            DataQuery.getInstance(MessagesController.this.currentAccount).loadDrafts();
                        }
                        MessagesController.this.putUsers(org_telegram_tgnet_TLRPC_messages_Dialogs.users, false);
                        MessagesController.this.putChats(org_telegram_tgnet_TLRPC_messages_Dialogs.chats, false);
                        for (int a = 0; a < MessagesController.this.dialogs.size(); a++) {
                            TL_dialog oldDialog = (TL_dialog) MessagesController.this.dialogs.get(a);
                            if (((int) oldDialog.id) != 0) {
                                MessagesController.this.dialogs_dict.remove(Long.valueOf(oldDialog.id));
                                messageObject = (MessageObject) MessagesController.this.dialogMessage.remove(Long.valueOf(oldDialog.id));
                                if (messageObject != null) {
                                    MessagesController.this.dialogMessagesByIds.remove(Integer.valueOf(messageObject.getId()));
                                    if (messageObject.messageOwner.random_id != 0) {
                                        MessagesController.this.dialogMessagesByRandomIds.remove(Long.valueOf(messageObject.messageOwner.random_id));
                                    }
                                }
                            }
                        }
                        for (Entry<Long, TL_dialog> pair : hashMap.entrySet()) {
                            Long key = (Long) pair.getKey();
                            TL_dialog value = (TL_dialog) pair.getValue();
                            if (value.draft instanceof TL_draftMessage) {
                                DataQuery.getInstance(MessagesController.this.currentAccount).saveDraft(value.id, value.draft, null, false);
                            }
                            MessagesController.this.dialogs_dict.put(key, value);
                            messageObject = (MessageObject) hashMap2.get(Long.valueOf(value.id));
                            MessagesController.this.dialogMessage.put(key, messageObject);
                            if (messageObject != null && messageObject.messageOwner.to_id.channel_id == 0) {
                                MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(messageObject.getId()), messageObject);
                                if (messageObject.messageOwner.random_id != 0) {
                                    MessagesController.this.dialogMessagesByRandomIds.put(Long.valueOf(messageObject.messageOwner.random_id), messageObject);
                                }
                            }
                        }
                        MessagesController.this.dialogs.clear();
                        MessagesController.this.dialogs.addAll(MessagesController.this.dialogs_dict.values());
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
        if (!this.migratingDialogs && offset != -1) {
            this.migratingDialogs = true;
            TL_messages_getDialogs req = new TL_messages_getDialogs();
            req.exclude_pinned = true;
            req.limit = 100;
            req.offset_id = offset;
            req.offset_date = offsetDate;
            FileLog.e("start migrate with id " + offset + " date " + LocaleController.getInstance().formatterStats.format(((long) offsetDate) * 1000));
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
                                try {
                                    int a;
                                    Message message;
                                    int offsetId;
                                    TL_dialog dialog;
                                    UserConfig instance = UserConfig.getInstance(MessagesController.this.currentAccount);
                                    instance.totalDialogsLoadCount += dialogsRes.dialogs.size();
                                    Message lastMessage = null;
                                    for (a = 0; a < dialogsRes.messages.size(); a++) {
                                        message = (Message) dialogsRes.messages.get(a);
                                        FileLog.e("search migrate id " + message.id + " date " + LocaleController.getInstance().formatterStats.format(((long) message.date) * 1000));
                                        if (lastMessage == null || message.date < lastMessage.date) {
                                            lastMessage = message;
                                        }
                                    }
                                    FileLog.e("migrate step with id " + lastMessage.id + " date " + LocaleController.getInstance().formatterStats.format(((long) lastMessage.date) * 1000));
                                    if (dialogsRes.dialogs.size() >= 100) {
                                        offsetId = lastMessage.id;
                                    } else {
                                        FileLog.e("migrate stop due to not 100 dialogs");
                                        UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetDate = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetDate;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetUserId;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChatId;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChannelId;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetAccess = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetAccess;
                                        offsetId = -1;
                                    }
                                    StringBuilder stringBuilder = new StringBuilder(dialogsRes.dialogs.size() * 12);
                                    HashMap<Long, TL_dialog> dialogHashMap = new HashMap();
                                    for (a = 0; a < dialogsRes.dialogs.size(); a++) {
                                        dialog = (TL_dialog) dialogsRes.dialogs.get(a);
                                        if (dialog.peer.channel_id != 0) {
                                            dialog.id = (long) (-dialog.peer.channel_id);
                                        } else if (dialog.peer.chat_id != 0) {
                                            dialog.id = (long) (-dialog.peer.chat_id);
                                        } else {
                                            dialog.id = (long) dialog.peer.user_id;
                                        }
                                        if (stringBuilder.length() > 0) {
                                            stringBuilder.append(",");
                                        }
                                        stringBuilder.append(dialog.id);
                                        dialogHashMap.put(Long.valueOf(dialog.id), dialog);
                                    }
                                    SQLiteCursor cursor = MessagesStorage.getInstance(MessagesController.this.currentAccount).getDatabase().queryFinalized(String.format(Locale.US, "SELECT did FROM dialogs WHERE did IN (%s)", new Object[]{stringBuilder.toString()}), new Object[0]);
                                    while (cursor.next()) {
                                        long did = cursor.longValue(0);
                                        dialog = (TL_dialog) dialogHashMap.remove(Long.valueOf(did));
                                        if (dialog != null) {
                                            dialogsRes.dialogs.remove(dialog);
                                            a = 0;
                                            while (a < dialogsRes.messages.size()) {
                                                message = (Message) dialogsRes.messages.get(a);
                                                if (MessageObject.getDialogId(message) == did) {
                                                    dialogsRes.messages.remove(a);
                                                    a--;
                                                    if (message.id == dialog.top_message) {
                                                        dialog.top_message = 0;
                                                        break;
                                                    }
                                                }
                                                a++;
                                            }
                                        }
                                    }
                                    cursor.dispose();
                                    FileLog.e("migrate found missing dialogs " + dialogsRes.dialogs.size());
                                    cursor = MessagesStorage.getInstance(MessagesController.this.currentAccount).getDatabase().queryFinalized("SELECT min(date) FROM dialogs WHERE date != 0 AND did >> 32 IN (0, -1)", new Object[0]);
                                    if (cursor.next()) {
                                        int date = Math.max(1441062000, cursor.intValue(0));
                                        a = 0;
                                        while (a < dialogsRes.messages.size()) {
                                            message = (Message) dialogsRes.messages.get(a);
                                            if (message.date < date) {
                                                if (offset != -1) {
                                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetId;
                                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetDate = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetDate;
                                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetUserId;
                                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChatId;
                                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChannelId;
                                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetAccess = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetAccess;
                                                    offsetId = -1;
                                                    FileLog.e("migrate stop due to reached loaded dialogs " + LocaleController.getInstance().formatterStats.format(((long) date) * 1000));
                                                }
                                                dialogsRes.messages.remove(a);
                                                a--;
                                                dialog = (TL_dialog) dialogHashMap.remove(Long.valueOf(MessageObject.getDialogId(message)));
                                                if (dialog != null) {
                                                    dialogsRes.dialogs.remove(dialog);
                                                }
                                            }
                                            a++;
                                        }
                                        if (!(lastMessage == null || lastMessage.date >= date || offset == -1)) {
                                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetId;
                                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetDate = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetDate;
                                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetUserId;
                                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChatId;
                                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChannelId;
                                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetAccess = UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetAccess;
                                            offsetId = -1;
                                            FileLog.e("migrate stop due to reached loaded dialogs " + LocaleController.getInstance().formatterStats.format(((long) date) * 1000));
                                        }
                                    }
                                    cursor.dispose();
                                    UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetDate = lastMessage.date;
                                    Chat chat;
                                    if (lastMessage.to_id.channel_id != 0) {
                                        UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChannelId = lastMessage.to_id.channel_id;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChatId = 0;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetUserId = 0;
                                        for (a = 0; a < dialogsRes.chats.size(); a++) {
                                            chat = (Chat) dialogsRes.chats.get(a);
                                            if (chat.id == UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChannelId) {
                                                UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetAccess = chat.access_hash;
                                                break;
                                            }
                                        }
                                    } else if (lastMessage.to_id.chat_id != 0) {
                                        UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChatId = lastMessage.to_id.chat_id;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChannelId = 0;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetUserId = 0;
                                        for (a = 0; a < dialogsRes.chats.size(); a++) {
                                            chat = (Chat) dialogsRes.chats.get(a);
                                            if (chat.id == UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChatId) {
                                                UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetAccess = chat.access_hash;
                                                break;
                                            }
                                        }
                                    } else if (lastMessage.to_id.user_id != 0) {
                                        UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetUserId = lastMessage.to_id.user_id;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChatId = 0;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChannelId = 0;
                                        for (a = 0; a < dialogsRes.users.size(); a++) {
                                            User user = (User) dialogsRes.users.get(a);
                                            if (user.id == UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetUserId) {
                                                UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetAccess = user.access_hash;
                                                break;
                                            }
                                        }
                                    }
                                    MessagesController.this.processLoadedDialogs(dialogsRes, null, offsetId, 0, 0, false, true, false);
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.this.migratingDialogs = false;
                                        }
                                    });
                                }
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
                if (!MessagesController.this.firstGettingTask) {
                    MessagesController.this.getNewDeleteTask(null, 0);
                    MessagesController.this.firstGettingTask = true;
                }
                FileLog.e("loaded loadType " + i + " count " + org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size());
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
                User user;
                Integer value;
                final HashMap<Long, TL_dialog> new_dialogs_dict = new HashMap();
                final HashMap<Long, MessageObject> new_dialogMessage = new HashMap();
                HashMap<Integer, User> usersDict = new HashMap();
                HashMap<Integer, Chat> chatsDict = new HashMap();
                for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.users.size(); a++) {
                    User u = (User) org_telegram_tgnet_TLRPC_messages_Dialogs.users.get(a);
                    usersDict.put(Integer.valueOf(u.id), u);
                }
                for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.chats.size(); a++) {
                    Chat c = (Chat) org_telegram_tgnet_TLRPC_messages_Dialogs.chats.get(a);
                    chatsDict.put(Integer.valueOf(c.id), c);
                }
                if (i == 1) {
                    MessagesController.this.nextDialogsCacheOffset = i3 + i2;
                }
                Message lastMessage = null;
                for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.messages.size(); a++) {
                    Chat chat;
                    Message message = (Message) org_telegram_tgnet_TLRPC_messages_Dialogs.messages.get(a);
                    if (lastMessage == null || message.date < lastMessage.date) {
                        lastMessage = message;
                    }
                    MessageObject messageObject;
                    if (message.to_id.channel_id != 0) {
                        chat = (Chat) chatsDict.get(Integer.valueOf(message.to_id.channel_id));
                        if (chat == null || !chat.left) {
                            if (chat != null && chat.megagroup) {
                                message.flags |= Integer.MIN_VALUE;
                            }
                            messageObject = new MessageObject(MessagesController.this.currentAccount, message, usersDict, chatsDict, false);
                            new_dialogMessage.put(Long.valueOf(messageObject.getDialogId()), messageObject);
                        }
                    } else {
                        if (message.to_id.chat_id != 0) {
                            chat = (Chat) chatsDict.get(Integer.valueOf(message.to_id.chat_id));
                            if (!(chat == null || chat.migrated_to == null)) {
                            }
                        }
                        messageObject = new MessageObject(MessagesController.this.currentAccount, message, usersDict, chatsDict, false);
                        new_dialogMessage.put(Long.valueOf(messageObject.getDialogId()), messageObject);
                    }
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
                                chat = (Chat) org_telegram_tgnet_TLRPC_messages_Dialogs.chats.get(a);
                                if (chat.id == UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId) {
                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetAccess = chat.access_hash;
                                    break;
                                }
                            }
                        } else if (lastMessage.to_id.chat_id != 0) {
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId = lastMessage.to_id.chat_id;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId = 0;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId = 0;
                            for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.chats.size(); a++) {
                                chat = (Chat) org_telegram_tgnet_TLRPC_messages_Dialogs.chats.get(a);
                                if (chat.id == UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId) {
                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetAccess = chat.access_hash;
                                    break;
                                }
                            }
                        } else if (lastMessage.to_id.user_id != 0) {
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId = lastMessage.to_id.user_id;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChatId = 0;
                            UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetChannelId = 0;
                            for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.users.size(); a++) {
                                user = (User) org_telegram_tgnet_TLRPC_messages_Dialogs.users.get(a);
                                if (user.id == UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetUserId) {
                                    UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetAccess = user.access_hash;
                                    break;
                                }
                            }
                        }
                    }
                    UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
                }
                final ArrayList<TL_dialog> dialogsToReload = new ArrayList();
                for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size(); a++) {
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
                            MessageObject mess = (MessageObject) new_dialogMessage.get(Long.valueOf(d.id));
                            if (mess != null) {
                                d.last_message_date = mess.messageOwner.date;
                            }
                        }
                        boolean allowCheck = true;
                        if (DialogObject.isChannel(d)) {
                            chat = (Chat) chatsDict.get(Integer.valueOf(-((int) d.id)));
                            if (chat != null) {
                                if (!chat.megagroup) {
                                    allowCheck = false;
                                }
                                if (chat.left) {
                                }
                            }
                            MessagesController.this.channelsPts.put(Integer.valueOf(-((int) d.id)), Integer.valueOf(d.pts));
                        } else if (((int) d.id) < 0) {
                            chat = (Chat) chatsDict.get(Integer.valueOf(-((int) d.id)));
                            if (!(chat == null || chat.migrated_to == null)) {
                            }
                        }
                        new_dialogs_dict.put(Long.valueOf(d.id), d);
                        if (allowCheck && i == 1 && ((d.read_outbox_max_id == 0 || d.read_inbox_max_id == 0) && d.top_message != 0)) {
                            dialogsToReload.add(d);
                        }
                        value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(d.id));
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
                }
                if (i != 1) {
                    ImageLoader.saveMessagesThumbs(org_telegram_tgnet_TLRPC_messages_Dialogs.messages);
                    for (a = 0; a < org_telegram_tgnet_TLRPC_messages_Dialogs.messages.size(); a++) {
                        message = (Message) org_telegram_tgnet_TLRPC_messages_Dialogs.messages.get(a);
                        if (message.action instanceof TL_messageActionChatDeleteUser) {
                            user = (User) usersDict.get(Integer.valueOf(message.action.user_id));
                            if (user != null && user.bot) {
                                message.reply_markup = new TL_replyKeyboardHide();
                                message.flags |= 64;
                            }
                        }
                        if ((message.action instanceof TL_messageActionChatMigrateTo) || (message.action instanceof TL_messageActionChannelCreate)) {
                            message.unread = false;
                            message.media_unread = false;
                        } else {
                            boolean z;
                            ConcurrentHashMap<Long, Integer> read_max = message.out ? MessagesController.this.dialogs_read_outbox_max : MessagesController.this.dialogs_read_inbox_max;
                            value = (Integer) read_max.get(Long.valueOf(message.dialog_id));
                            if (value == null) {
                                value = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                                read_max.put(Long.valueOf(message.dialog_id), value);
                            }
                            if (value.intValue() < message.id) {
                                z = true;
                            } else {
                                z = false;
                            }
                            message.unread = z;
                        }
                    }
                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putDialogs(org_telegram_tgnet_TLRPC_messages_Dialogs, false);
                }
                if (i == 2) {
                    chat = (Chat) org_telegram_tgnet_TLRPC_messages_Dialogs.chats.get(0);
                    MessagesController.this.getChannelDifference(chat.id);
                    MessagesController.this.checkChannelInviter(chat.id);
                }
                final HashMap<Integer, Chat> hashMap = chatsDict;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        if (i != 1) {
                            MessagesController.this.applyDialogsNotificationsSettings(org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs);
                            if (!UserConfig.getInstance(MessagesController.this.currentAccount).draftsLoaded) {
                                DataQuery.getInstance(MessagesController.this.currentAccount).loadDrafts();
                            }
                        }
                        MessagesController.this.putUsers(org_telegram_tgnet_TLRPC_messages_Dialogs.users, i == 1);
                        MessagesController.this.putChats(org_telegram_tgnet_TLRPC_messages_Dialogs.chats, i == 1);
                        if (arrayList != null) {
                            for (int a = 0; a < arrayList.size(); a++) {
                                EncryptedChat encryptedChat = (EncryptedChat) arrayList.get(a);
                                if ((encryptedChat instanceof TL_encryptedChat) && AndroidUtilities.getMyLayerVersion(encryptedChat.layer) < 73) {
                                    SecretChatHelper.getInstance(MessagesController.this.currentAccount).sendNotifyLayerMessage(encryptedChat, null);
                                }
                                MessagesController.this.putEncryptedChat(encryptedChat, true);
                            }
                        }
                        if (!z3) {
                            MessagesController.this.loadingDialogs = false;
                        }
                        boolean added = false;
                        int lastDialogDate = (!z3 || MessagesController.this.dialogs.isEmpty()) ? 0 : ((TL_dialog) MessagesController.this.dialogs.get(MessagesController.this.dialogs.size() - 1)).last_message_date;
                        for (Entry<Long, TL_dialog> pair : new_dialogs_dict.entrySet()) {
                            Long key = (Long) pair.getKey();
                            TL_dialog value = (TL_dialog) pair.getValue();
                            if (!z3 || lastDialogDate == 0 || value.last_message_date >= lastDialogDate) {
                                TL_dialog currentDialog = (TL_dialog) MessagesController.this.dialogs_dict.get(key);
                                if (i != 1 && (value.draft instanceof TL_draftMessage)) {
                                    DataQuery.getInstance(MessagesController.this.currentAccount).saveDraft(value.id, value.draft, null, false);
                                }
                                MessageObject messageObject;
                                if (currentDialog == null) {
                                    added = true;
                                    MessagesController.this.dialogs_dict.put(key, value);
                                    messageObject = (MessageObject) new_dialogMessage.get(Long.valueOf(value.id));
                                    MessagesController.this.dialogMessage.put(key, messageObject);
                                    if (messageObject != null && messageObject.messageOwner.to_id.channel_id == 0) {
                                        MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(messageObject.getId()), messageObject);
                                        if (messageObject.messageOwner.random_id != 0) {
                                            MessagesController.this.dialogMessagesByRandomIds.put(Long.valueOf(messageObject.messageOwner.random_id), messageObject);
                                        }
                                    }
                                } else {
                                    if (i != 1) {
                                        currentDialog.notify_settings = value.notify_settings;
                                    }
                                    currentDialog.pinned = value.pinned;
                                    currentDialog.pinnedNum = value.pinnedNum;
                                    MessageObject oldMsg = (MessageObject) MessagesController.this.dialogMessage.get(key);
                                    if ((oldMsg == null || !oldMsg.deleted) && oldMsg != null && currentDialog.top_message <= 0) {
                                        MessageObject newMsg = (MessageObject) new_dialogMessage.get(Long.valueOf(value.id));
                                        if (oldMsg.deleted || newMsg == null || newMsg.messageOwner.date > oldMsg.messageOwner.date) {
                                            MessagesController.this.dialogs_dict.put(key, value);
                                            MessagesController.this.dialogMessage.put(key, newMsg);
                                            if (newMsg != null && newMsg.messageOwner.to_id.channel_id == 0) {
                                                MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(newMsg.getId()), newMsg);
                                                if (!(newMsg == null || newMsg.messageOwner.random_id == 0)) {
                                                    MessagesController.this.dialogMessagesByRandomIds.put(Long.valueOf(newMsg.messageOwner.random_id), newMsg);
                                                }
                                            }
                                            MessagesController.this.dialogMessagesByIds.remove(Integer.valueOf(oldMsg.getId()));
                                            if (oldMsg.messageOwner.random_id != 0) {
                                                MessagesController.this.dialogMessagesByRandomIds.remove(Long.valueOf(oldMsg.messageOwner.random_id));
                                            }
                                        }
                                    } else if (value.top_message >= currentDialog.top_message) {
                                        MessagesController.this.dialogs_dict.put(key, value);
                                        messageObject = (MessageObject) new_dialogMessage.get(Long.valueOf(value.id));
                                        MessagesController.this.dialogMessage.put(key, messageObject);
                                        if (messageObject != null && messageObject.messageOwner.to_id.channel_id == 0) {
                                            MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(messageObject.getId()), messageObject);
                                            if (!(messageObject == null || messageObject.messageOwner.random_id == 0)) {
                                                MessagesController.this.dialogMessagesByRandomIds.put(Long.valueOf(messageObject.messageOwner.random_id), messageObject);
                                            }
                                        }
                                        if (oldMsg != null) {
                                            MessagesController.this.dialogMessagesByIds.remove(Integer.valueOf(oldMsg.getId()));
                                            if (oldMsg.messageOwner.random_id != 0) {
                                                MessagesController.this.dialogMessagesByRandomIds.remove(Long.valueOf(oldMsg.messageOwner.random_id));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        MessagesController.this.dialogs.clear();
                        MessagesController.this.dialogs.addAll(MessagesController.this.dialogs_dict.values());
                        MessagesController.this.sortDialogs(z3 ? hashMap : null);
                        if (!(i == 2 || z3)) {
                            MessagesController messagesController = MessagesController.this;
                            boolean z = (org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size() == 0 || org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size() != i2) && i == 0;
                            messagesController.dialogsEndReached = z;
                            if (!z2) {
                                messagesController = MessagesController.this;
                                z = (org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size() == 0 || org_telegram_tgnet_TLRPC_messages_Dialogs.dialogs.size() != i2) && i == 0;
                                messagesController.serverDialogsEndReached = z;
                            }
                        }
                        if (!(z2 || z3 || UserConfig.getInstance(MessagesController.this.currentAccount).totalDialogsLoadCount >= 400 || UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId == -1 || UserConfig.getInstance(MessagesController.this.currentAccount).dialogsLoadOffsetId == ConnectionsManager.DEFAULT_DATACENTER_ID)) {
                            MessagesController.this.loadDialogs(0, 100, false);
                        }
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                        if (z3) {
                            UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetId = i3;
                            UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
                            MessagesController.this.migratingDialogs = false;
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.needReloadRecentDialogsSearch, new Object[0]);
                        } else {
                            MessagesController.this.generateUpdateMessage();
                            if (!added && i == 1) {
                                MessagesController.this.loadDialogs(0, i2, false);
                            }
                        }
                        MessagesController.this.migrateDialogs(UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetId, UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetDate, UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetUserId, UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChatId, UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetChannelId, UserConfig.getInstance(MessagesController.this.currentAccount).migrateOffsetAccess);
                        if (!dialogsToReload.isEmpty()) {
                            MessagesController.this.reloadDialogsReadValue(dialogsToReload, 0);
                        }
                    }
                });
            }
        });
    }

    private void applyDialogNotificationsSettings(long dialog_id, PeerNotifySettings notify_settings) {
        int currentValue = this.notificationsPreferences.getInt("notify2_" + dialog_id, 0);
        int currentValue2 = this.notificationsPreferences.getInt("notifyuntil_" + dialog_id, 0);
        Editor editor = this.notificationsPreferences.edit();
        boolean updated = false;
        TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(Long.valueOf(dialog_id));
        if (dialog != null) {
            dialog.notify_settings = notify_settings;
        }
        editor.putBoolean("silent_" + dialog_id, notify_settings.silent);
        if (notify_settings.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
            int until = 0;
            if (notify_settings.mute_until <= ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 31536000) {
                if (!(currentValue == 3 && currentValue2 == notify_settings.mute_until)) {
                    updated = true;
                    editor.putInt("notify2_" + dialog_id, 3);
                    editor.putInt("notifyuntil_" + dialog_id, notify_settings.mute_until);
                    if (dialog != null) {
                        dialog.notify_settings.mute_until = 0;
                    }
                }
                until = notify_settings.mute_until;
            } else if (currentValue != 2) {
                updated = true;
                editor.putInt("notify2_" + dialog_id, 2);
                if (dialog != null) {
                    dialog.notify_settings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
                }
            }
            MessagesStorage.getInstance(this.currentAccount).setDialogFlags(dialog_id, (((long) until) << 32) | 1);
            NotificationsController.getInstance(this.currentAccount).removeNotificationsForDialog(dialog_id);
        } else {
            if (!(currentValue == 0 || currentValue == 1)) {
                updated = true;
                if (dialog != null) {
                    dialog.notify_settings.mute_until = 0;
                }
                editor.remove("notify2_" + dialog_id);
            }
            MessagesStorage.getInstance(this.currentAccount).setDialogFlags(dialog_id, 0);
        }
        editor.commit();
        if (updated) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
        }
    }

    private void applyDialogsNotificationsSettings(ArrayList<TL_dialog> dialogs) {
        Editor editor = null;
        for (int a = 0; a < dialogs.size(); a++) {
            TL_dialog dialog = (TL_dialog) dialogs.get(a);
            if (dialog.peer != null && (dialog.notify_settings instanceof TL_peerNotifySettings)) {
                int dialog_id;
                if (editor == null) {
                    editor = this.notificationsPreferences.edit();
                }
                if (dialog.peer.user_id != 0) {
                    dialog_id = dialog.peer.user_id;
                } else if (dialog.peer.chat_id != 0) {
                    dialog_id = -dialog.peer.chat_id;
                } else {
                    dialog_id = -dialog.peer.channel_id;
                }
                editor.putBoolean("silent_" + dialog_id, dialog.notify_settings.silent);
                if (dialog.notify_settings.mute_until == 0) {
                    editor.remove("notify2_" + dialog_id);
                } else if (dialog.notify_settings.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() + 31536000) {
                    editor.putInt("notify2_" + dialog_id, 2);
                    dialog.notify_settings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
                } else {
                    editor.putInt("notify2_" + dialog_id, 3);
                    editor.putInt("notifyuntil_" + dialog_id, dialog.notify_settings.mute_until);
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

    public void processDialogsUpdateRead(final HashMap<Long, Integer> dialogsToUpdate, final HashMap<Long, Integer> dialogsMentionsToUpdate) {
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                TL_dialog currentDialog;
                if (dialogsToUpdate != null) {
                    for (Entry<Long, Integer> entry : dialogsToUpdate.entrySet()) {
                        currentDialog = (TL_dialog) MessagesController.this.dialogs_dict.get((Long) entry.getKey());
                        if (currentDialog != null) {
                            currentDialog.unread_count = ((Integer) entry.getValue()).intValue();
                        }
                    }
                }
                if (dialogsMentionsToUpdate != null) {
                    for (Entry<Long, Integer> entry2 : dialogsMentionsToUpdate.entrySet()) {
                        currentDialog = (TL_dialog) MessagesController.this.dialogs_dict.get((Long) entry2.getKey());
                        if (currentDialog != null) {
                            currentDialog.unread_mentions_count = ((Integer) entry2.getValue()).intValue();
                            if (MessagesController.this.createdDialogMainThreadIds.contains(Long.valueOf(currentDialog.id))) {
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateMentionsCount, Long.valueOf(currentDialog.id), Integer.valueOf(currentDialog.unread_mentions_count));
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
        Throwable e;
        final int lower_id = (int) dialog.id;
        if (lower_id != 0 && !this.checkingLastMessagesDialogs.containsKey(Integer.valueOf(lower_id))) {
            InputPeer inputPeer;
            TL_messages_getHistory req = new TL_messages_getHistory();
            if (peer == null) {
                inputPeer = getInputPeer(lower_id);
            } else {
                inputPeer = peer;
            }
            req.peer = inputPeer;
            if (req.peer != null && !(req.peer instanceof TL_inputPeerChannel)) {
                long newTaskId;
                final TL_dialog tL_dialog;
                req.limit = 1;
                this.checkingLastMessagesDialogs.put(Integer.valueOf(lower_id), Boolean.valueOf(true));
                if (taskId == 0) {
                    NativeByteBuffer data = null;
                    try {
                        NativeByteBuffer data2 = new NativeByteBuffer(req.peer.getObjectSize() + 48);
                        try {
                            data2.writeInt32(8);
                            data2.writeInt64(dialog.id);
                            data2.writeInt32(dialog.top_message);
                            data2.writeInt32(dialog.read_inbox_max_id);
                            data2.writeInt32(dialog.read_outbox_max_id);
                            data2.writeInt32(dialog.unread_count);
                            data2.writeInt32(dialog.last_message_date);
                            data2.writeInt32(dialog.pts);
                            data2.writeInt32(dialog.flags);
                            data2.writeBool(dialog.pinned);
                            data2.writeInt32(dialog.pinnedNum);
                            data2.writeInt32(dialog.unread_mentions_count);
                            peer.serializeToStream(data2);
                            data = data2;
                        } catch (Exception e2) {
                            e = e2;
                            data = data2;
                            FileLog.e(e);
                            newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                            tL_dialog = dialog;
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                                public void run(TLObject response, TL_error error) {
                                    if (response != null) {
                                        messages_Messages res = (messages_Messages) response;
                                        if (res.messages.isEmpty()) {
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                    TL_dialog currentDialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(tL_dialog.id));
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
                                            MessagesController.this.checkingLastMessagesDialogs.remove(Integer.valueOf(lower_id));
                                        }
                                    });
                                }
                            });
                        }
                    } catch (Exception e3) {
                        e = e3;
                        FileLog.e(e);
                        newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                        tL_dialog = dialog;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
                    }
                    newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                } else {
                    newTaskId = taskId;
                }
                tL_dialog = dialog;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
            }
        }
    }

    public void processDialogsUpdate(final messages_Dialogs dialogsRes, ArrayList<EncryptedChat> arrayList) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                int a;
                final HashMap<Long, TL_dialog> new_dialogs_dict = new HashMap();
                final HashMap<Long, MessageObject> new_dialogMessage = new HashMap();
                HashMap<Integer, User> usersDict = new HashMap();
                HashMap<Integer, Chat> chatsDict = new HashMap();
                final HashMap<Long, Integer> dialogsToUpdate = new HashMap();
                for (a = 0; a < dialogsRes.users.size(); a++) {
                    User u = (User) dialogsRes.users.get(a);
                    usersDict.put(Integer.valueOf(u.id), u);
                }
                for (a = 0; a < dialogsRes.chats.size(); a++) {
                    Chat c = (Chat) dialogsRes.chats.get(a);
                    chatsDict.put(Integer.valueOf(c.id), c);
                }
                for (a = 0; a < dialogsRes.messages.size(); a++) {
                    Chat chat;
                    Message message = (Message) dialogsRes.messages.get(a);
                    MessageObject messageObject;
                    if (message.to_id.channel_id != 0) {
                        chat = (Chat) chatsDict.get(Integer.valueOf(message.to_id.channel_id));
                        if (chat != null && chat.left) {
                        }
                        messageObject = new MessageObject(MessagesController.this.currentAccount, message, usersDict, chatsDict, false);
                        new_dialogMessage.put(Long.valueOf(messageObject.getDialogId()), messageObject);
                    } else {
                        if (message.to_id.chat_id != 0) {
                            chat = (Chat) chatsDict.get(Integer.valueOf(message.to_id.chat_id));
                            if (!(chat == null || chat.migrated_to == null)) {
                            }
                        }
                        messageObject = new MessageObject(MessagesController.this.currentAccount, message, usersDict, chatsDict, false);
                        new_dialogMessage.put(Long.valueOf(messageObject.getDialogId()), messageObject);
                    }
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
                    MessageObject mess;
                    Integer value;
                    if (DialogObject.isChannel(d)) {
                        chat = (Chat) chatsDict.get(Integer.valueOf(-((int) d.id)));
                        if (chat != null && chat.left) {
                        }
                        if (d.last_message_date == 0) {
                            mess = (MessageObject) new_dialogMessage.get(Long.valueOf(d.id));
                            if (mess != null) {
                                d.last_message_date = mess.messageOwner.date;
                            }
                        }
                        new_dialogs_dict.put(Long.valueOf(d.id), d);
                        dialogsToUpdate.put(Long.valueOf(d.id), Integer.valueOf(d.unread_count));
                        value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(d.id));
                        if (value == null) {
                            value = Integer.valueOf(0);
                        }
                        MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_inbox_max_id)));
                        value = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(d.id));
                        if (value == null) {
                            value = Integer.valueOf(0);
                        }
                        MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_outbox_max_id)));
                    } else {
                        if (((int) d.id) < 0) {
                            chat = (Chat) chatsDict.get(Integer.valueOf(-((int) d.id)));
                            if (!(chat == null || chat.migrated_to == null)) {
                            }
                        }
                        if (d.last_message_date == 0) {
                            mess = (MessageObject) new_dialogMessage.get(Long.valueOf(d.id));
                            if (mess != null) {
                                d.last_message_date = mess.messageOwner.date;
                            }
                        }
                        new_dialogs_dict.put(Long.valueOf(d.id), d);
                        dialogsToUpdate.put(Long.valueOf(d.id), Integer.valueOf(d.unread_count));
                        value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(d.id));
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
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        MessagesController.this.putUsers(dialogsRes.users, true);
                        MessagesController.this.putChats(dialogsRes.chats, true);
                        for (Entry<Long, TL_dialog> pair : new_dialogs_dict.entrySet()) {
                            Long key = (Long) pair.getKey();
                            TL_dialog value = (TL_dialog) pair.getValue();
                            TL_dialog currentDialog = (TL_dialog) MessagesController.this.dialogs_dict.get(key);
                            MessageObject messageObject;
                            if (currentDialog == null) {
                                MessagesController messagesController = MessagesController.this;
                                messagesController.nextDialogsCacheOffset++;
                                MessagesController.this.dialogs_dict.put(key, value);
                                messageObject = (MessageObject) new_dialogMessage.get(Long.valueOf(value.id));
                                MessagesController.this.dialogMessage.put(key, messageObject);
                                if (messageObject != null && messageObject.messageOwner.to_id.channel_id == 0) {
                                    MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(messageObject.getId()), messageObject);
                                    if (messageObject.messageOwner.random_id != 0) {
                                        MessagesController.this.dialogMessagesByRandomIds.put(Long.valueOf(messageObject.messageOwner.random_id), messageObject);
                                    }
                                }
                            } else {
                                currentDialog.unread_count = value.unread_count;
                                if (currentDialog.unread_mentions_count != value.unread_mentions_count) {
                                    currentDialog.unread_mentions_count = value.unread_mentions_count;
                                    if (MessagesController.this.createdDialogMainThreadIds.contains(Long.valueOf(currentDialog.id))) {
                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateMentionsCount, Long.valueOf(currentDialog.id), Integer.valueOf(currentDialog.unread_mentions_count));
                                    }
                                }
                                MessageObject oldMsg = (MessageObject) MessagesController.this.dialogMessage.get(key);
                                if (oldMsg != null && currentDialog.top_message <= 0) {
                                    MessageObject newMsg = (MessageObject) new_dialogMessage.get(Long.valueOf(value.id));
                                    if (oldMsg.deleted || newMsg == null || newMsg.messageOwner.date > oldMsg.messageOwner.date) {
                                        MessagesController.this.dialogs_dict.put(key, value);
                                        MessagesController.this.dialogMessage.put(key, newMsg);
                                        if (newMsg != null && newMsg.messageOwner.to_id.channel_id == 0) {
                                            MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(newMsg.getId()), newMsg);
                                            if (newMsg.messageOwner.random_id != 0) {
                                                MessagesController.this.dialogMessagesByRandomIds.put(Long.valueOf(newMsg.messageOwner.random_id), newMsg);
                                            }
                                        }
                                        MessagesController.this.dialogMessagesByIds.remove(Integer.valueOf(oldMsg.getId()));
                                        if (oldMsg.messageOwner.random_id != 0) {
                                            MessagesController.this.dialogMessagesByRandomIds.remove(Long.valueOf(oldMsg.messageOwner.random_id));
                                        }
                                    }
                                } else if ((oldMsg != null && oldMsg.deleted) || value.top_message > currentDialog.top_message) {
                                    MessagesController.this.dialogs_dict.put(key, value);
                                    messageObject = (MessageObject) new_dialogMessage.get(Long.valueOf(value.id));
                                    MessagesController.this.dialogMessage.put(key, messageObject);
                                    if (messageObject != null && messageObject.messageOwner.to_id.channel_id == 0) {
                                        MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(messageObject.getId()), messageObject);
                                        if (messageObject.messageOwner.random_id != 0) {
                                            MessagesController.this.dialogMessagesByRandomIds.put(Long.valueOf(messageObject.messageOwner.random_id), messageObject);
                                        }
                                    }
                                    if (oldMsg != null) {
                                        MessagesController.this.dialogMessagesByIds.remove(Integer.valueOf(oldMsg.getId()));
                                        if (oldMsg.messageOwner.random_id != 0) {
                                            MessagesController.this.dialogMessagesByRandomIds.remove(Long.valueOf(oldMsg.messageOwner.random_id));
                                        }
                                    }
                                    if (messageObject == null) {
                                        MessagesController.this.checkLastDialogMessage(value, null, 0);
                                    }
                                }
                            }
                        }
                        MessagesController.this.dialogs.clear();
                        MessagesController.this.dialogs.addAll(MessagesController.this.dialogs_dict.values());
                        MessagesController.this.sortDialogs(null);
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                        NotificationsController.getInstance(MessagesController.this.currentAccount).processDialogsUpdateRead(dialogsToUpdate);
                    }
                });
            }
        });
    }

    public void addToViewsQueue(final Message message) {
        Utilities.stageQueue.postRunnable(new Runnable() {
            public void run() {
                int peer;
                if (message.to_id.channel_id != 0) {
                    peer = -message.to_id.channel_id;
                } else if (message.to_id.chat_id != 0) {
                    peer = -message.to_id.chat_id;
                } else {
                    peer = message.to_id.user_id;
                }
                ArrayList<Integer> ids = (ArrayList) MessagesController.this.channelViewsToSend.get(peer);
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
                return;
            }
            return;
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
        if (mid != 0 && ttl > 0) {
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
                return;
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
    }

    public void markMessageAsRead(long dialog_id, long random_id, int ttl) {
        if (random_id != 0 && dialog_id != 0) {
            if (ttl > 0 || ttl == Integer.MIN_VALUE) {
                int high_id = (int) (dialog_id >> 32);
                if (((int) dialog_id) == 0) {
                    EncryptedChat chat = getEncryptedChat(Integer.valueOf(high_id));
                    if (chat != null) {
                        ArrayList<Long> random_ids = new ArrayList();
                        random_ids.add(Long.valueOf(random_id));
                        SecretChatHelper.getInstance(this.currentAccount).sendMessagesReadMessage(chat, random_ids, null);
                        if (ttl > 0) {
                            int time = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                            MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(chat.id, time, time, 0, random_ids);
                        }
                    }
                }
            }
        }
    }

    public void markDialogAsRead(long dialog_id, int max_id, int max_positive_id, int max_date, boolean was, boolean popup) {
        int lower_part = (int) dialog_id;
        int high_id = (int) (dialog_id >> 32);
        TLObject req;
        final long j;
        if (lower_part != 0) {
            if (max_positive_id != 0 && high_id != 1) {
                InputPeer inputPeer = getInputPeer(lower_part);
                long messageId = (long) max_positive_id;
                TLObject request;
                if (inputPeer instanceof TL_inputPeerChannel) {
                    request = new TL_channels_readHistory();
                    request.channel = getInputChannel(-lower_part);
                    request.max_id = max_positive_id;
                    req = request;
                    messageId |= ((long) (-lower_part)) << 32;
                } else {
                    request = new TL_messages_readHistory();
                    request.peer = inputPeer;
                    request.max_id = max_positive_id;
                    req = request;
                }
                Integer value = (Integer) this.dialogs_read_inbox_max.get(Long.valueOf(dialog_id));
                if (value == null) {
                    value = Integer.valueOf(0);
                }
                this.dialogs_read_inbox_max.put(Long.valueOf(dialog_id), Integer.valueOf(Math.max(value.intValue(), max_positive_id)));
                MessagesStorage.getInstance(this.currentAccount).processPendingRead(dialog_id, messageId, max_date);
                j = dialog_id;
                final boolean z = popup;
                final int i = max_positive_id;
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                    public void run() {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(j));
                                if (dialog != null) {
                                    dialog.unread_count = 0;
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(256));
                                }
                                if (z) {
                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processReadMessages(null, j, 0, i, true);
                                    HashMap<Long, Integer> dialogsToUpdate = new HashMap();
                                    dialogsToUpdate.put(Long.valueOf(j), Integer.valueOf(-1));
                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processDialogsUpdateRead(dialogsToUpdate);
                                    return;
                                }
                                NotificationsController.getInstance(MessagesController.this.currentAccount).processReadMessages(null, j, 0, i, false);
                                dialogsToUpdate = new HashMap();
                                dialogsToUpdate.put(Long.valueOf(j), Integer.valueOf(0));
                                NotificationsController.getInstance(MessagesController.this.currentAccount).processDialogsUpdateRead(dialogsToUpdate);
                            }
                        });
                    }
                });
                if (max_positive_id != Integer.MAX_VALUE) {
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            if (error == null && (response instanceof TL_messages_affectedMessages)) {
                                TL_messages_affectedMessages res = (TL_messages_affectedMessages) response;
                                MessagesController.this.processNewDifferenceParams(-1, res.pts, -1, res.pts_count);
                            }
                        }
                    });
                }
            }
        } else if (max_date != 0) {
            EncryptedChat chat = getEncryptedChat(Integer.valueOf(high_id));
            if (chat.auth_key != null && chat.auth_key.length > 1 && (chat instanceof TL_encryptedChat)) {
                req = new TL_messages_readEncryptedHistory();
                req.peer = new TL_inputEncryptedChat();
                req.peer.chat_id = chat.id;
                req.peer.access_hash = chat.access_hash;
                req.max_date = max_date;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                    }
                });
            }
            MessagesStorage.getInstance(this.currentAccount).processPendingRead(dialog_id, (long) max_id, max_date);
            j = dialog_id;
            final int i2 = max_date;
            final boolean z2 = popup;
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                public void run() {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.getInstance(MessagesController.this.currentAccount).processReadMessages(null, j, i2, 0, z2);
                            TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(j));
                            if (dialog != null) {
                                dialog.unread_count = 0;
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(256));
                            }
                            HashMap<Long, Integer> dialogsToUpdate = new HashMap();
                            dialogsToUpdate.put(Long.valueOf(j), Integer.valueOf(0));
                            NotificationsController.getInstance(MessagesController.this.currentAccount).processDialogsUpdateRead(dialogsToUpdate);
                        }
                    });
                }
            });
            if (chat.ttl > 0 && was) {
                int serverTime = Math.max(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime(), max_date);
                MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(chat.id, serverTime, serverTime, 0, null);
            }
        }
    }

    public int createChat(String title, ArrayList<Integer> selectedContacts, String about, int type, BaseFragment fragment) {
        int a;
        if (type == 1) {
            TL_chat chat = new TL_chat();
            chat.id = UserConfig.getInstance(this.currentAccount).lastBroadcastId;
            chat.title = title;
            chat.photo = new TL_chatPhotoEmpty();
            chat.participants_count = selectedContacts.size();
            chat.date = (int) (System.currentTimeMillis() / 1000);
            chat.version = 1;
            UserConfig instance = UserConfig.getInstance(this.currentAccount);
            instance.lastBroadcastId--;
            putChat(chat, false);
            ArrayList<Chat> chatsArrays = new ArrayList();
            chatsArrays.add(chat);
            MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, chatsArrays, true, true);
            TL_chatFull chatFull = new TL_chatFull();
            chatFull.id = chat.id;
            chatFull.chat_photo = new TL_photoEmpty();
            chatFull.notify_settings = new TL_peerNotifySettingsEmpty();
            chatFull.exported_invite = new TL_chatInviteEmpty();
            chatFull.participants = new TL_chatParticipants();
            chatFull.participants.chat_id = chat.id;
            chatFull.participants.admin_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
            chatFull.participants.version = 1;
            for (a = 0; a < selectedContacts.size(); a++) {
                TL_chatParticipant participant = new TL_chatParticipant();
                participant.user_id = ((Integer) selectedContacts.get(a)).intValue();
                participant.inviter_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                participant.date = (int) (System.currentTimeMillis() / 1000);
                chatFull.participants.participants.add(participant);
            }
            MessagesStorage.getInstance(this.currentAccount).updateChatInfo(chatFull, false);
            TL_messageService newMsg = new TL_messageService();
            newMsg.action = new TL_messageActionCreatedBroadcastList();
            int newMessageId = UserConfig.getInstance(this.currentAccount).getNewMessageId();
            newMsg.id = newMessageId;
            newMsg.local_id = newMessageId;
            newMsg.from_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
            newMsg.dialog_id = AndroidUtilities.makeBroadcastId(chat.id);
            newMsg.to_id = new TL_peerChat();
            newMsg.to_id.chat_id = chat.id;
            newMsg.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
            newMsg.random_id = 0;
            newMsg.flags |= 256;
            UserConfig.getInstance(this.currentAccount).saveConfig(false);
            MessageObject newMsgObj = new MessageObject(this.currentAccount, newMsg, this.users, true);
            newMsgObj.messageOwner.send_state = 0;
            ArrayList<MessageObject> objArr = new ArrayList();
            objArr.add(newMsgObj);
            ArrayList arr = new ArrayList();
            arr.add(newMsg);
            MessagesStorage.getInstance(this.currentAccount).putMessages(arr, false, true, false, 0);
            updateInterfaceWithMessages(newMsg.dialog_id, objArr);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatDidCreated, Integer.valueOf(chat.id));
            return 0;
        } else if (type == 0) {
            req = new TL_messages_createChat();
            req.title = title;
            for (a = 0; a < selectedContacts.size(); a++) {
                User user = getUser((Integer) selectedContacts.get(a));
                if (user != null) {
                    req.users.add(getInputUser(user));
                }
            }
            r1 = fragment;
            r2 = req;
            return ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, final TL_error error) {
                    if (error != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                AlertsCreator.processError(MessagesController.this.currentAccount, error, r1, r2, new Object[0]);
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
        } else if (type != 2 && type != 4) {
            return 0;
        } else {
            req = new TL_channels_createChannel();
            req.title = title;
            req.about = about;
            if (type == 4) {
                req.megagroup = true;
            } else {
                req.broadcast = true;
            }
            r1 = fragment;
            r2 = req;
            return ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, final TL_error error) {
                    if (error != null) {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                AlertsCreator.processError(MessagesController.this.currentAccount, error, r1, r2, new Object[0]);
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
        if (users != null && !users.isEmpty()) {
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
        if (user != null) {
            if (chat_id > 0) {
                TLObject request;
                final boolean isChannel = ChatObject.isChannel(chat_id, this.currentAccount);
                final boolean isMegagroup = isChannel && getChat(Integer.valueOf(chat_id)).megagroup;
                final InputUser inputUser = getInputUser(user);
                TLObject req;
                if (botHash != null && (!isChannel || isMegagroup)) {
                    req = new TL_messages_startBot();
                    req.bot = inputUser;
                    if (isChannel) {
                        req.peer = getInputPeer(-chat_id);
                    } else {
                        req.peer = new TL_inputPeerChat();
                        req.peer.chat_id = chat_id;
                    }
                    req.start_param = botHash;
                    req.random_id = Utilities.random.nextLong();
                    request = req;
                } else if (!isChannel) {
                    req = new TL_messages_addChatUser();
                    req.chat_id = chat_id;
                    req.fwd_limit = count_fwd;
                    req.user_id = inputUser;
                    request = req;
                } else if (!(inputUser instanceof TL_inputUserSelf)) {
                    req = new TL_channels_inviteToChannel();
                    req.channel = getInputChannel(chat_id);
                    req.users.add(inputUser);
                    request = req;
                } else if (!this.joiningToChannels.contains(Integer.valueOf(chat_id))) {
                    req = new TL_channels_joinChannel();
                    req.channel = getInputChannel(chat_id);
                    request = req;
                    this.joiningToChannels.add(Integer.valueOf(chat_id));
                } else {
                    return;
                }
                final int i = chat_id;
                final BaseFragment baseFragment = fragment;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new RequestDelegate() {
                    public void run(TLObject response, final TL_error error) {
                        if (isChannel && (inputUser instanceof TL_inputUserSelf)) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.joiningToChannels.remove(Integer.valueOf(i));
                                }
                            });
                        }
                        if (error != null) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    boolean z = true;
                                    int access$000 = MessagesController.this.currentAccount;
                                    TL_error tL_error = error;
                                    BaseFragment baseFragment = baseFragment;
                                    TLObject tLObject = request;
                                    Object[] objArr = new Object[1];
                                    if (!isChannel || isMegagroup) {
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
                        if (isChannel) {
                            if (!hasJoinMessage && (inputUser instanceof TL_inputUserSelf)) {
                                MessagesController.this.generateJoinMessage(i, true);
                            }
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.loadFullChat(i, 0, true);
                                }
                            }, 1000);
                        }
                        if (isChannel && (inputUser instanceof TL_inputUserSelf)) {
                            MessagesStorage.getInstance(MessagesController.this.currentAccount).updateDialogsWithDeletedMessages(new ArrayList(), null, true, i);
                        }
                    }
                });
            } else if (info instanceof TL_chatFull) {
                int a = 0;
                while (a < info.participants.participants.size()) {
                    if (((ChatParticipant) info.participants.participants.get(a)).user_id != user.id) {
                        a++;
                    } else {
                        return;
                    }
                }
                Chat chat = getChat(Integer.valueOf(chat_id));
                chat.participants_count++;
                ArrayList<Chat> chatArrayList = new ArrayList();
                chatArrayList.add(chat);
                MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, chatArrayList, true, true);
                TL_chatParticipant newPart = new TL_chatParticipant();
                newPart.user_id = user.id;
                newPart.inviter_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                newPart.date = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                info.participants.participants.add(0, newPart);
                MessagesStorage.getInstance(this.currentAccount).updateChatInfo(info, true);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, info, Integer.valueOf(0), Boolean.valueOf(false), null);
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(32));
            }
        }
    }

    public void deleteUserFromChat(int chat_id, User user, ChatFull info) {
        deleteUserFromChat(chat_id, user, info, false);
    }

    public void deleteUserFromChat(int chat_id, User user, ChatFull info, boolean forceDelete) {
        if (user != null) {
            Chat chat;
            if (chat_id > 0) {
                TLObject request;
                final InputUser inputUser = getInputUser(user);
                chat = getChat(Integer.valueOf(chat_id));
                final boolean isChannel = ChatObject.isChannel(chat);
                TLObject req;
                if (!isChannel) {
                    req = new TL_messages_deleteChatUser();
                    req.chat_id = chat_id;
                    req.user_id = getInputUser(user);
                    request = req;
                } else if (!(inputUser instanceof TL_inputUserSelf)) {
                    req = new TL_channels_editBanned();
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
                    req = new TL_channels_deleteChannel();
                    req.channel = getInputChannel(chat);
                    request = req;
                } else {
                    req = new TL_channels_leaveChannel();
                    req.channel = getInputChannel(chat);
                    request = req;
                }
                final User user2 = user;
                final int i = chat_id;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (user2.id == UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.deleteDialog((long) (-i), 0);
                                }
                            });
                        }
                        if (error == null) {
                            MessagesController.this.processUpdates((Updates) response, false);
                            if (isChannel && !(inputUser instanceof TL_inputUserSelf)) {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        MessagesController.this.loadFullChat(i, 0, true);
                                    }
                                }, 1000);
                            }
                        }
                    }
                }, 64);
            } else if (info instanceof TL_chatFull) {
                chat = getChat(Integer.valueOf(chat_id));
                chat.participants_count--;
                ArrayList<Chat> chatArrayList = new ArrayList();
                chatArrayList.add(chat);
                MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(null, chatArrayList, true, true);
                boolean changed = false;
                for (int a = 0; a < info.participants.participants.size(); a++) {
                    if (((ChatParticipant) info.participants.participants.get(a)).user_id == user.id) {
                        info.participants.participants.remove(a);
                        changed = true;
                        break;
                    }
                }
                if (changed) {
                    MessagesStorage.getInstance(this.currentAccount).updateChatInfo(info, true);
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.chatInfoDidLoaded, info, Integer.valueOf(0), Boolean.valueOf(false), null);
                }
                NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(32));
            }
        }
    }

    public void changeChatTitle(int chat_id, String title) {
        if (chat_id > 0) {
            TLObject request;
            TLObject req;
            if (ChatObject.isChannel(chat_id, this.currentAccount)) {
                req = new TL_channels_editTitle();
                req.channel = getInputChannel(chat_id);
                req.title = title;
                request = req;
            } else {
                req = new TL_messages_editChatTitle();
                req.chat_id = chat_id;
                req.title = title;
                request = req;
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

    public void changeChatAvatar(int chat_id, InputFile uploadedAvatar) {
        TLObject request;
        TLObject req;
        if (ChatObject.isChannel(chat_id, this.currentAccount)) {
            req = new TL_channels_editPhoto();
            req.channel = getInputChannel(chat_id);
            if (uploadedAvatar != null) {
                req.photo = new TL_inputChatUploadedPhoto();
                req.photo.file = uploadedAvatar;
            } else {
                req.photo = new TL_inputChatPhotoEmpty();
            }
            request = req;
        } else {
            req = new TL_messages_editChatPhoto();
            req.chat_id = chat_id;
            if (uploadedAvatar != null) {
                req.photo = new TL_inputChatUploadedPhoto();
                req.photo.file = uploadedAvatar;
            } else {
                req.photo = new TL_inputChatPhotoEmpty();
            }
            request = req;
        }
        ConnectionsManager.getInstance(this.currentAccount).sendRequest(request, new RequestDelegate() {
            public void run(TLObject response, TL_error error) {
                if (error == null) {
                    MessagesController.this.processUpdates((Updates) response, false);
                }
            }
        }, 64);
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
        if (!BuildVars.DEBUG_VERSION && SharedConfig.lastUpdateVersion != null && !SharedConfig.lastUpdateVersion.equals(BuildVars.BUILD_VERSION_STRING)) {
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

    public void registerForPush(final String regid) {
        if (regid != null && regid.length() != 0 && !this.registeringForPush && UserConfig.getInstance(this.currentAccount).getClientUserId() != 0) {
            if (!UserConfig.getInstance(this.currentAccount).registeredForPush || !regid.equals(SharedConfig.pushString)) {
                this.registeringForPush = true;
                TL_account_registerDevice req = new TL_account_registerDevice();
                req.token_type = 2;
                req.token = regid;
                for (int a = 0; a < 3; a++) {
                    UserConfig userConfig = UserConfig.getInstance(a);
                    if (a != this.currentAccount && userConfig.isClientActivated()) {
                        req.other_uids.add(Integer.valueOf(userConfig.getClientUserId()));
                    }
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(TLObject response, TL_error error) {
                        if (response instanceof TL_boolTrue) {
                            FileLog.e("account " + MessagesController.this.currentAccount + " registered for push");
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

    public void loadCurrentState() {
        if (!this.updatingState) {
            this.updatingState = true;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_updates_getState(), new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    MessagesController.this.updatingState = false;
                    if (error == null) {
                        TL_updates_state res = (TL_updates_state) response;
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).lastDateValue = res.date;
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).lastPtsValue = res.pts;
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).lastSeqValue = res.seq;
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).lastQtsValue = res.qts;
                        for (int a = 0; a < 3; a++) {
                            MessagesController.this.processUpdatesQueue(a, 2);
                        }
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).saveDiffParams(MessagesStorage.getInstance(MessagesController.this.currentAccount).lastSeqValue, MessagesStorage.getInstance(MessagesController.this.currentAccount).lastPtsValue, MessagesStorage.getInstance(MessagesController.this.currentAccount).lastDateValue, MessagesStorage.getInstance(MessagesController.this.currentAccount).lastQtsValue);
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
            if (MessagesStorage.getInstance(this.currentAccount).lastSeqValue + 1 == seq || MessagesStorage.getInstance(this.currentAccount).lastSeqValue == seq) {
                return 0;
            }
            if (MessagesStorage.getInstance(this.currentAccount).lastSeqValue >= seq) {
                return 2;
            }
            return 1;
        } else if (type == 1) {
            if (updates.pts <= MessagesStorage.getInstance(this.currentAccount).lastPtsValue) {
                return 2;
            }
            if (MessagesStorage.getInstance(this.currentAccount).lastPtsValue + updates.pts_count == updates.pts) {
                return 0;
            }
            return 1;
        } else if (type != 2) {
            return 0;
        } else {
            if (updates.pts <= MessagesStorage.getInstance(this.currentAccount).lastQtsValue) {
                return 2;
            }
            if (MessagesStorage.getInstance(this.currentAccount).lastQtsValue + updates.updates.size() == updates.pts) {
                return 0;
            }
            return 1;
        }
    }

    private void processChannelsUpdatesQueue(int channelId, int state) {
        ArrayList<Updates> updatesQueue = (ArrayList) this.updatesQueueChannels.get(Integer.valueOf(channelId));
        if (updatesQueue != null) {
            Integer channelPts = (Integer) this.channelsPts.get(Integer.valueOf(channelId));
            if (updatesQueue.isEmpty() || channelPts == null) {
                this.updatesQueueChannels.remove(Integer.valueOf(channelId));
                return;
            }
            Collections.sort(updatesQueue, new Comparator<Updates>() {
                public int compare(Updates updates, Updates updates2) {
                    return AndroidUtilities.compare(updates.pts, updates2.pts);
                }
            });
            boolean anyProceed = false;
            if (state == 2) {
                this.channelsPts.put(Integer.valueOf(channelId), Integer.valueOf(((Updates) updatesQueue.get(0)).pts));
            }
            int a = 0;
            while (updatesQueue.size() > 0) {
                int updateState;
                Updates updates = (Updates) updatesQueue.get(a);
                if (updates.pts <= channelPts.intValue()) {
                    updateState = 2;
                } else if (channelPts.intValue() + updates.pts_count == updates.pts) {
                    updateState = 0;
                } else {
                    updateState = 1;
                }
                if (updateState == 0) {
                    processUpdates(updates, true);
                    anyProceed = true;
                    updatesQueue.remove(a);
                    a--;
                } else if (updateState == 1) {
                    Long updatesStartWaitTime = (Long) this.updatesStartWaitTimeChannels.get(Integer.valueOf(channelId));
                    if (updatesStartWaitTime == null || (!anyProceed && Math.abs(System.currentTimeMillis() - updatesStartWaitTime.longValue()) > 1500)) {
                        FileLog.e("HOLE IN CHANNEL " + channelId + " UPDATES QUEUE - getChannelDifference ");
                        this.updatesStartWaitTimeChannels.remove(Integer.valueOf(channelId));
                        this.updatesQueueChannels.remove(Integer.valueOf(channelId));
                        getChannelDifference(channelId);
                        return;
                    }
                    FileLog.e("HOLE IN CHANNEL " + channelId + " UPDATES QUEUE - will wait more time");
                    if (anyProceed) {
                        this.updatesStartWaitTimeChannels.put(Integer.valueOf(channelId), Long.valueOf(System.currentTimeMillis()));
                        return;
                    }
                    return;
                } else {
                    updatesQueue.remove(a);
                    a--;
                }
                a++;
            }
            this.updatesQueueChannels.remove(Integer.valueOf(channelId));
            this.updatesStartWaitTimeChannels.remove(Integer.valueOf(channelId));
            FileLog.e("UPDATES CHANNEL " + channelId + " QUEUE PROCEED - OK");
        }
    }

    private void processUpdatesQueue(int type, int state) {
        ArrayList<Updates> updatesQueue = null;
        if (type == 0) {
            updatesQueue = this.updatesQueueSeq;
            Collections.sort(updatesQueue, new Comparator<Updates>() {
                public int compare(Updates updates, Updates updates2) {
                    return AndroidUtilities.compare(MessagesController.this.getUpdateSeq(updates), MessagesController.this.getUpdateSeq(updates2));
                }
            });
        } else if (type == 1) {
            updatesQueue = this.updatesQueuePts;
            Collections.sort(updatesQueue, new Comparator<Updates>() {
                public int compare(Updates updates, Updates updates2) {
                    return AndroidUtilities.compare(updates.pts, updates2.pts);
                }
            });
        } else if (type == 2) {
            updatesQueue = this.updatesQueueQts;
            Collections.sort(updatesQueue, new Comparator<Updates>() {
                public int compare(Updates updates, Updates updates2) {
                    return AndroidUtilities.compare(updates.pts, updates2.pts);
                }
            });
        }
        if (!(updatesQueue == null || updatesQueue.isEmpty())) {
            Updates updates;
            boolean anyProceed = false;
            if (state == 2) {
                updates = (Updates) updatesQueue.get(0);
                if (type == 0) {
                    MessagesStorage.getInstance(this.currentAccount).lastSeqValue = getUpdateSeq(updates);
                } else if (type == 1) {
                    MessagesStorage.getInstance(this.currentAccount).lastPtsValue = updates.pts;
                } else {
                    MessagesStorage.getInstance(this.currentAccount).lastQtsValue = updates.pts;
                }
            }
            int a = 0;
            while (updatesQueue.size() > 0) {
                updates = (Updates) updatesQueue.get(a);
                int updateState = isValidUpdate(updates, type);
                if (updateState == 0) {
                    processUpdates(updates, true);
                    anyProceed = true;
                    updatesQueue.remove(a);
                    a--;
                } else if (updateState != 1) {
                    updatesQueue.remove(a);
                    a--;
                } else if (getUpdatesStartTime(type) == 0 || (!anyProceed && Math.abs(System.currentTimeMillis() - getUpdatesStartTime(type)) > 1500)) {
                    FileLog.e("HOLE IN UPDATES QUEUE - getDifference");
                    setUpdatesStartTime(type, 0);
                    updatesQueue.clear();
                    getDifference();
                    return;
                } else {
                    FileLog.e("HOLE IN UPDATES QUEUE - will wait more time");
                    if (anyProceed) {
                        setUpdatesStartTime(type, System.currentTimeMillis());
                        return;
                    }
                    return;
                }
                a++;
            }
            updatesQueue.clear();
            FileLog.e("UPDATES QUEUE PROCEED - OK");
        }
        setUpdatesStartTime(type, 0);
    }

    protected void loadUnknownChannel(final Chat channel, long taskId) {
        Throwable e;
        long newTaskId;
        if ((channel instanceof TL_channel) && !this.gettingUnknownChannels.containsKey(Integer.valueOf(channel.id))) {
            if (channel.access_hash != 0) {
                TL_inputPeerChannel inputPeer = new TL_inputPeerChannel();
                inputPeer.channel_id = channel.id;
                inputPeer.access_hash = channel.access_hash;
                this.gettingUnknownChannels.put(Integer.valueOf(channel.id), Boolean.valueOf(true));
                TL_messages_getPeerDialogs req = new TL_messages_getPeerDialogs();
                req.peers.add(inputPeer);
                if (taskId == 0) {
                    NativeByteBuffer data = null;
                    try {
                        NativeByteBuffer data2 = new NativeByteBuffer(channel.getObjectSize() + 4);
                        try {
                            data2.writeInt32(0);
                            channel.serializeToStream(data2);
                            data = data2;
                        } catch (Exception e2) {
                            e = e2;
                            data = data2;
                            FileLog.e(e);
                            newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                                public void run(TLObject response, TL_error error) {
                                    if (response != null) {
                                        TL_messages_peerDialogs res = (TL_messages_peerDialogs) response;
                                        if (!(res.dialogs.isEmpty() || res.chats.isEmpty())) {
                                            TL_messages_dialogs dialogs = new TL_messages_dialogs();
                                            dialogs.dialogs.addAll(res.dialogs);
                                            dialogs.messages.addAll(res.messages);
                                            dialogs.users.addAll(res.users);
                                            dialogs.chats.addAll(res.chats);
                                            MessagesController.this.processLoadedDialogs(dialogs, null, 0, 1, 2, false, false, false);
                                        }
                                    }
                                    if (newTaskId != 0) {
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                                    }
                                    MessagesController.this.gettingUnknownChannels.remove(Integer.valueOf(channel.id));
                                }
                            });
                        }
                    } catch (Exception e3) {
                        e = e3;
                        FileLog.e(e);
                        newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
                    }
                    newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                } else {
                    newTaskId = taskId;
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
            } else if (taskId != 0) {
                MessagesStorage.getInstance(this.currentAccount).removePendingTask(taskId);
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
        return id / 1000 == 777 || id == 333000 || id == 4240000 || id == 4240000 || id == 4244000 || id == 4245000 || id == 4246000 || id == 410000 || id == 420000 || id == 431000 || id == 431415000 || id == 434000 || id == 4243000 || id == 439000 || id == 449000 || id == 450000 || id == 452000 || id == 454000 || id == 4254000 || id == 455000 || id == 460000 || id == 470000 || id == 479000 || id == 796000 || id == 482000 || id == 490000 || id == 496000 || id == 497000 || id == 498000 || id == 4298000;
    }

    protected void getChannelDifference(int channelId, int newDialogType, long taskId, InputChannel inputChannel) {
        Integer channelPts;
        Throwable e;
        long newTaskId;
        TL_updates_getChannelDifference req;
        final int i;
        final int i2;
        Boolean gettingDifferenceChannel = (Boolean) this.gettingDifferenceChannels.get(Integer.valueOf(channelId));
        if (gettingDifferenceChannel == null) {
            gettingDifferenceChannel = Boolean.valueOf(false);
        }
        if (!gettingDifferenceChannel.booleanValue()) {
            int limit = 100;
            if (newDialogType != 1) {
                channelPts = (Integer) this.channelsPts.get(Integer.valueOf(channelId));
                if (channelPts == null) {
                    channelPts = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(channelId));
                    if (channelPts.intValue() != 0) {
                        this.channelsPts.put(Integer.valueOf(channelId), channelPts);
                    }
                    if (channelPts.intValue() == 0 && (newDialogType == 2 || newDialogType == 3)) {
                        return;
                    }
                }
                if (channelPts.intValue() == 0) {
                    return;
                }
            } else if (((Integer) this.channelsPts.get(Integer.valueOf(channelId))) == null) {
                channelPts = Integer.valueOf(1);
                limit = 1;
            } else {
                return;
            }
            if (inputChannel == null) {
                Chat chat = getChat(Integer.valueOf(channelId));
                if (chat == null) {
                    chat = MessagesStorage.getInstance(this.currentAccount).getChatSync(channelId);
                    if (chat != null) {
                        putChat(chat, true);
                    }
                }
                inputChannel = getInputChannel(chat);
            }
            if (inputChannel != null && inputChannel.access_hash != 0) {
                if (taskId == 0) {
                    NativeByteBuffer data = null;
                    try {
                        NativeByteBuffer data2 = new NativeByteBuffer(inputChannel.getObjectSize() + 12);
                        try {
                            data2.writeInt32(6);
                            data2.writeInt32(channelId);
                            data2.writeInt32(newDialogType);
                            inputChannel.serializeToStream(data2);
                            data = data2;
                        } catch (Exception e2) {
                            e = e2;
                            data = data2;
                            FileLog.e(e);
                            newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                            this.gettingDifferenceChannels.put(Integer.valueOf(channelId), Boolean.valueOf(true));
                            req = new TL_updates_getChannelDifference();
                            req.channel = inputChannel;
                            req.filter = new TL_channelMessagesFilterEmpty();
                            req.pts = channelPts.intValue();
                            req.limit = limit;
                            req.force = newDialogType == 3;
                            FileLog.e("start getChannelDifference with pts = " + channelPts + " channelId = " + channelId);
                            i = channelId;
                            i2 = newDialogType;
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                                public void run(TLObject response, TL_error error) {
                                    if (error == null) {
                                        int a;
                                        final updates_ChannelDifference res = (updates_ChannelDifference) response;
                                        final HashMap<Integer, User> usersDict = new HashMap();
                                        for (a = 0; a < res.users.size(); a++) {
                                            User user = (User) res.users.get(a);
                                            usersDict.put(Integer.valueOf(user.id), user);
                                        }
                                        Chat channel = null;
                                        for (a = 0; a < res.chats.size(); a++) {
                                            Chat chat = (Chat) res.chats.get(a);
                                            if (chat.id == i) {
                                                channel = chat;
                                                break;
                                            }
                                        }
                                        final Chat channelFinal = channel;
                                        final ArrayList<TL_updateMessageID> msgUpdates = new ArrayList();
                                        if (!res.other_updates.isEmpty()) {
                                            a = 0;
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
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                            public void run() {
                                                if (!msgUpdates.isEmpty()) {
                                                    final HashMap<Integer, long[]> corrected = new HashMap();
                                                    Iterator it = msgUpdates.iterator();
                                                    while (it.hasNext()) {
                                                        TL_updateMessageID update = (TL_updateMessageID) it.next();
                                                        long[] ids = MessagesStorage.getInstance(MessagesController.this.currentAccount).updateMessageStateAndId(update.random_id, null, update.id, 0, false, i);
                                                        if (ids != null) {
                                                            corrected.put(Integer.valueOf(update.id), ids);
                                                        }
                                                    }
                                                    if (!corrected.isEmpty()) {
                                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                                            public void run() {
                                                                for (Entry<Integer, long[]> entry : corrected.entrySet()) {
                                                                    Integer newId = (Integer) entry.getKey();
                                                                    SendMessagesHelper.getInstance(MessagesController.this.currentAccount).processSentMessage(Integer.valueOf((int) ((long[]) entry.getValue())[1]).intValue());
                                                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, oldId, newId, null, Long.valueOf(ids[0]));
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                                Utilities.stageQueue.postRunnable(new Runnable() {
                                                    public void run() {
                                                        long dialog_id;
                                                        Integer inboxValue;
                                                        Integer outboxValue;
                                                        int a;
                                                        Message message;
                                                        Integer num;
                                                        boolean z;
                                                        if ((res instanceof TL_updates_channelDifference) || (res instanceof TL_updates_channelDifferenceEmpty)) {
                                                            if (!res.new_messages.isEmpty()) {
                                                                final HashMap<Long, ArrayList<MessageObject>> messages = new HashMap();
                                                                ImageLoader.saveMessagesThumbs(res.new_messages);
                                                                final ArrayList<MessageObject> pushMessages = new ArrayList();
                                                                dialog_id = (long) (-i);
                                                                inboxValue = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(dialog_id));
                                                                if (inboxValue == null) {
                                                                    inboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, dialog_id));
                                                                    MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(dialog_id), inboxValue);
                                                                }
                                                                outboxValue = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(dialog_id));
                                                                if (outboxValue == null) {
                                                                    outboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, dialog_id));
                                                                    MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(dialog_id), outboxValue);
                                                                }
                                                                for (a = 0; a < res.new_messages.size(); a++) {
                                                                    MessageObject obj;
                                                                    long uid;
                                                                    ArrayList<MessageObject> arr;
                                                                    message = (Message) res.new_messages.get(a);
                                                                    if (channelFinal == null || !channelFinal.left) {
                                                                        if (message.out) {
                                                                            num = outboxValue;
                                                                        } else {
                                                                            num = inboxValue;
                                                                        }
                                                                        if (num.intValue() < message.id && !(message.action instanceof TL_messageActionChannelCreate)) {
                                                                            z = true;
                                                                            message.unread = z;
                                                                            if (channelFinal != null && channelFinal.megagroup) {
                                                                                message.flags |= Integer.MIN_VALUE;
                                                                            }
                                                                            obj = new MessageObject(MessagesController.this.currentAccount, message, usersDict, MessagesController.this.createdDialogIds.contains(Long.valueOf(dialog_id)));
                                                                            if (!obj.isOut() && obj.isUnread()) {
                                                                                pushMessages.add(obj);
                                                                            }
                                                                            uid = (long) (-i);
                                                                            arr = (ArrayList) messages.get(Long.valueOf(uid));
                                                                            if (arr == null) {
                                                                                arr = new ArrayList();
                                                                                messages.put(Long.valueOf(uid), arr);
                                                                            }
                                                                            arr.add(obj);
                                                                        }
                                                                    }
                                                                    z = false;
                                                                    message.unread = z;
                                                                    message.flags |= Integer.MIN_VALUE;
                                                                    obj = new MessageObject(MessagesController.this.currentAccount, message, usersDict, MessagesController.this.createdDialogIds.contains(Long.valueOf(dialog_id)));
                                                                    pushMessages.add(obj);
                                                                    uid = (long) (-i);
                                                                    arr = (ArrayList) messages.get(Long.valueOf(uid));
                                                                    if (arr == null) {
                                                                        arr = new ArrayList();
                                                                        messages.put(Long.valueOf(uid), arr);
                                                                    }
                                                                    arr.add(obj);
                                                                }
                                                                AndroidUtilities.runOnUIThread(new Runnable() {
                                                                    public void run() {
                                                                        for (Entry<Long, ArrayList<MessageObject>> pair : messages.entrySet()) {
                                                                            ArrayList<MessageObject> value = (ArrayList) pair.getValue();
                                                                            MessagesController.this.updateInterfaceWithMessages(((Long) pair.getKey()).longValue(), value);
                                                                        }
                                                                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                                                    }
                                                                });
                                                                MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                                                    public void run() {
                                                                        if (!pushMessages.isEmpty()) {
                                                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                                                public void run() {
                                                                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(pushMessages, true);
                                                                                }
                                                                            });
                                                                        }
                                                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(res.new_messages, true, false, false, DownloadController.getInstance(MessagesController.this.currentAccount).getAutodownloadMask());
                                                                    }
                                                                });
                                                            }
                                                            if (!res.other_updates.isEmpty()) {
                                                                MessagesController.this.processUpdateArray(res.other_updates, res.users, res.chats, true);
                                                            }
                                                            MessagesController.this.processChannelsUpdatesQueue(i, 1);
                                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).saveChannelPts(i, res.pts);
                                                        } else if (res instanceof TL_updates_channelDifferenceTooLong) {
                                                            dialog_id = (long) (-i);
                                                            inboxValue = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(dialog_id));
                                                            if (inboxValue == null) {
                                                                inboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(false, dialog_id));
                                                                MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(dialog_id), inboxValue);
                                                            }
                                                            outboxValue = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(dialog_id));
                                                            if (outboxValue == null) {
                                                                outboxValue = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(true, dialog_id));
                                                                MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(dialog_id), outboxValue);
                                                            }
                                                            for (a = 0; a < res.messages.size(); a++) {
                                                                message = (Message) res.messages.get(a);
                                                                message.dialog_id = (long) (-i);
                                                                if (!(message.action instanceof TL_messageActionChannelCreate) && (channelFinal == null || !channelFinal.left)) {
                                                                    if (message.out) {
                                                                        num = outboxValue;
                                                                    } else {
                                                                        num = inboxValue;
                                                                    }
                                                                    if (num.intValue() < message.id) {
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
                                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).overwriteChannel(i, (TL_updates_channelDifferenceTooLong) res, i2);
                                                        }
                                                        MessagesController.this.gettingDifferenceChannels.remove(Integer.valueOf(i));
                                                        MessagesController.this.channelsPts.put(Integer.valueOf(i), Integer.valueOf(res.pts));
                                                        if ((res.flags & 2) != 0) {
                                                            MessagesController.this.shortPollChannels.put(i, ((int) (System.currentTimeMillis() / 1000)) + res.timeout);
                                                        }
                                                        if (!res.isFinal) {
                                                            MessagesController.this.getChannelDifference(i);
                                                        }
                                                        FileLog.e("received channel difference with pts = " + res.pts + " channelId = " + i);
                                                        FileLog.e("new_messages = " + res.new_messages.size() + " messages = " + res.messages.size() + " users = " + res.users.size() + " chats = " + res.chats.size() + " other updates = " + res.other_updates.size());
                                                        if (newTaskId != 0) {
                                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                        return;
                                    }
                                    final TL_error tL_error = error;
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.this.checkChannelError(tL_error.text, i);
                                        }
                                    });
                                    MessagesController.this.gettingDifferenceChannels.remove(Integer.valueOf(i));
                                    if (newTaskId != 0) {
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                                    }
                                }
                            });
                        }
                    } catch (Exception e3) {
                        e = e3;
                        FileLog.e(e);
                        newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                        this.gettingDifferenceChannels.put(Integer.valueOf(channelId), Boolean.valueOf(true));
                        req = new TL_updates_getChannelDifference();
                        req.channel = inputChannel;
                        req.filter = new TL_channelMessagesFilterEmpty();
                        req.pts = channelPts.intValue();
                        req.limit = limit;
                        if (newDialogType == 3) {
                        }
                        req.force = newDialogType == 3;
                        FileLog.e("start getChannelDifference with pts = " + channelPts + " channelId = " + channelId);
                        i = channelId;
                        i2 = newDialogType;
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
                    }
                    newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                } else {
                    newTaskId = taskId;
                }
                this.gettingDifferenceChannels.put(Integer.valueOf(channelId), Boolean.valueOf(true));
                req = new TL_updates_getChannelDifference();
                req.channel = inputChannel;
                req.filter = new TL_channelMessagesFilterEmpty();
                req.pts = channelPts.intValue();
                req.limit = limit;
                if (newDialogType == 3) {
                }
                req.force = newDialogType == 3;
                FileLog.e("start getChannelDifference with pts = " + channelPts + " channelId = " + channelId);
                i = channelId;
                i2 = newDialogType;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
            } else if (taskId != 0) {
                MessagesStorage.getInstance(this.currentAccount).removePendingTask(taskId);
            }
        }
    }

    private void checkChannelError(String text, int channelId) {
        int i = -1;
        switch (text.hashCode()) {
            case -1809401834:
                if (text.equals("USER_BANNED_IN_CHANNEL")) {
                    i = 2;
                    break;
                }
                break;
            case -795226617:
                if (text.equals("CHANNEL_PRIVATE")) {
                    i = 0;
                    break;
                }
                break;
            case -471086771:
                if (text.equals("CHANNEL_PUBLIC_GROUP_NA")) {
                    i = 1;
                    break;
                }
                break;
        }
        switch (i) {
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
        getDifference(MessagesStorage.getInstance(this.currentAccount).lastPtsValue, MessagesStorage.getInstance(this.currentAccount).lastDateValue, MessagesStorage.getInstance(this.currentAccount).lastQtsValue, false);
    }

    public void getDifference(int pts, final int date, final int qts, boolean slice) {
        registerForPush(SharedConfig.pushString);
        if (MessagesStorage.getInstance(this.currentAccount).lastPtsValue == 0) {
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
            FileLog.e("start getDifference with date = " + date + " pts = " + pts + " qts = " + qts);
            ConnectionsManager.getInstance(this.currentAccount).setIsUpdating(true);
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (error == null) {
                        final updates_Difference res = (updates_Difference) response;
                        if (res instanceof TL_updates_differenceTooLong) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    MessagesController.this.loadedFullUsers.clear();
                                    MessagesController.this.loadedFullChats.clear();
                                    MessagesController.this.resetDialogs(true, MessagesStorage.getInstance(MessagesController.this.currentAccount).lastSeqValue, res.pts, date, qts);
                                }
                            });
                            return;
                        }
                        int a;
                        if (res instanceof TL_updates_differenceSlice) {
                            MessagesController.this.getDifference(res.intermediate_state.pts, res.intermediate_state.date, res.intermediate_state.qts, true);
                        }
                        final HashMap<Integer, User> usersDict = new HashMap();
                        final HashMap<Integer, Chat> chatsDict = new HashMap();
                        for (a = 0; a < res.users.size(); a++) {
                            User user = (User) res.users.get(a);
                            usersDict.put(Integer.valueOf(user.id), user);
                        }
                        for (a = 0; a < res.chats.size(); a++) {
                            Chat chat = (Chat) res.chats.get(a);
                            chatsDict.put(Integer.valueOf(chat.id), chat);
                        }
                        final ArrayList<TL_updateMessageID> msgUpdates = new ArrayList();
                        if (!res.other_updates.isEmpty()) {
                            a = 0;
                            while (a < res.other_updates.size()) {
                                Update upd = (Update) res.other_updates.get(a);
                                if (upd instanceof TL_updateMessageID) {
                                    msgUpdates.add((TL_updateMessageID) upd);
                                    res.other_updates.remove(a);
                                    a--;
                                } else if (MessagesController.this.getUpdateType(upd) == 2) {
                                    int channelId = MessagesController.this.getUpdateChannelId(upd);
                                    Integer channelPts = (Integer) MessagesController.this.channelsPts.get(Integer.valueOf(channelId));
                                    if (channelPts == null) {
                                        channelPts = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getChannelPtsSync(channelId));
                                        if (channelPts.intValue() != 0) {
                                            MessagesController.this.channelsPts.put(Integer.valueOf(channelId), channelPts);
                                        }
                                    }
                                    if (channelPts.intValue() != 0 && upd.pts <= channelPts.intValue()) {
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
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                            public void run() {
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, res.chats, true, false);
                                if (!msgUpdates.isEmpty()) {
                                    final HashMap<Integer, long[]> corrected = new HashMap();
                                    for (int a = 0; a < msgUpdates.size(); a++) {
                                        TL_updateMessageID update = (TL_updateMessageID) msgUpdates.get(a);
                                        long[] ids = MessagesStorage.getInstance(MessagesController.this.currentAccount).updateMessageStateAndId(update.random_id, null, update.id, 0, false, 0);
                                        if (ids != null) {
                                            corrected.put(Integer.valueOf(update.id), ids);
                                        }
                                    }
                                    if (!corrected.isEmpty()) {
                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                            public void run() {
                                                for (Entry<Integer, long[]> entry : corrected.entrySet()) {
                                                    Integer newId = (Integer) entry.getKey();
                                                    SendMessagesHelper.getInstance(MessagesController.this.currentAccount).processSentMessage(Integer.valueOf((int) ((long[]) entry.getValue())[1]).intValue());
                                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messageReceivedByServer, oldId, newId, null, Long.valueOf(ids[0]));
                                                }
                                            }
                                        });
                                    }
                                }
                                Utilities.stageQueue.postRunnable(new Runnable() {
                                    public void run() {
                                        int a;
                                        if (!(res.new_messages.isEmpty() && res.new_encrypted_messages.isEmpty())) {
                                            final HashMap<Long, ArrayList<MessageObject>> messages = new HashMap();
                                            for (int b = 0; b < res.new_encrypted_messages.size(); b++) {
                                                ArrayList<Message> decryptedMessages = SecretChatHelper.getInstance(MessagesController.this.currentAccount).decryptMessage((EncryptedMessage) res.new_encrypted_messages.get(b));
                                                if (!(decryptedMessages == null || decryptedMessages.isEmpty())) {
                                                    res.new_messages.addAll(decryptedMessages);
                                                }
                                            }
                                            ImageLoader.saveMessagesThumbs(res.new_messages);
                                            final ArrayList<MessageObject> pushMessages = new ArrayList();
                                            int clientUserId = UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId();
                                            for (a = 0; a < res.new_messages.size(); a++) {
                                                Message message = (Message) res.new_messages.get(a);
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
                                                        User user = (User) usersDict.get(Integer.valueOf(message.action.user_id));
                                                        if (user != null && user.bot) {
                                                            message.reply_markup = new TL_replyKeyboardHide();
                                                            message.flags |= 64;
                                                        }
                                                    }
                                                    if ((message.action instanceof TL_messageActionChatMigrateTo) || (message.action instanceof TL_messageActionChannelCreate)) {
                                                        message.unread = false;
                                                        message.media_unread = false;
                                                    } else {
                                                        boolean z;
                                                        ConcurrentHashMap<Long, Integer> read_max = message.out ? MessagesController.this.dialogs_read_outbox_max : MessagesController.this.dialogs_read_inbox_max;
                                                        Integer value = (Integer) read_max.get(Long.valueOf(message.dialog_id));
                                                        if (value == null) {
                                                            value = Integer.valueOf(MessagesStorage.getInstance(MessagesController.this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                                                            read_max.put(Long.valueOf(message.dialog_id), value);
                                                        }
                                                        if (value.intValue() < message.id) {
                                                            z = true;
                                                        } else {
                                                            z = false;
                                                        }
                                                        message.unread = z;
                                                    }
                                                }
                                                if (message.dialog_id == ((long) clientUserId)) {
                                                    message.unread = false;
                                                    message.media_unread = false;
                                                    message.out = true;
                                                }
                                                MessageObject obj = new MessageObject(MessagesController.this.currentAccount, message, usersDict, chatsDict, MessagesController.this.createdDialogIds.contains(Long.valueOf(message.dialog_id)));
                                                if (!obj.isOut() && obj.isUnread()) {
                                                    pushMessages.add(obj);
                                                }
                                                ArrayList<MessageObject> arr = (ArrayList) messages.get(Long.valueOf(message.dialog_id));
                                                if (arr == null) {
                                                    arr = new ArrayList();
                                                    messages.put(Long.valueOf(message.dialog_id), arr);
                                                }
                                                arr.add(obj);
                                            }
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                    for (Entry<Long, ArrayList<MessageObject>> pair : messages.entrySet()) {
                                                        ArrayList<MessageObject> value = (ArrayList) pair.getValue();
                                                        MessagesController.this.updateInterfaceWithMessages(((Long) pair.getKey()).longValue(), value);
                                                    }
                                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                                }
                                            });
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                                public void run() {
                                                    if (!pushMessages.isEmpty()) {
                                                        AndroidUtilities.runOnUIThread(new Runnable() {
                                                            public void run() {
                                                                NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(pushMessages, !(res instanceof TL_updates_differenceSlice));
                                                            }
                                                        });
                                                    }
                                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(res.new_messages, true, false, false, DownloadController.getInstance(MessagesController.this.currentAccount).getAutodownloadMask());
                                                }
                                            });
                                            SecretChatHelper.getInstance(MessagesController.this.currentAccount).processPendingEncMessages();
                                        }
                                        if (!res.other_updates.isEmpty()) {
                                            MessagesController.this.processUpdateArray(res.other_updates, res.users, res.chats, true);
                                        }
                                        if (res instanceof TL_updates_difference) {
                                            MessagesController.this.gettingDifference = false;
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).lastSeqValue = res.state.seq;
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).lastDateValue = res.state.date;
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).lastPtsValue = res.state.pts;
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).lastQtsValue = res.state.qts;
                                            ConnectionsManager.getInstance(MessagesController.this.currentAccount).setIsUpdating(false);
                                            for (a = 0; a < 3; a++) {
                                                MessagesController.this.processUpdatesQueue(a, 1);
                                            }
                                        } else if (res instanceof TL_updates_differenceSlice) {
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).lastDateValue = res.intermediate_state.date;
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).lastPtsValue = res.intermediate_state.pts;
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).lastQtsValue = res.intermediate_state.qts;
                                        } else if (res instanceof TL_updates_differenceEmpty) {
                                            MessagesController.this.gettingDifference = false;
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).lastSeqValue = res.seq;
                                            MessagesStorage.getInstance(MessagesController.this.currentAccount).lastDateValue = res.date;
                                            ConnectionsManager.getInstance(MessagesController.this.currentAccount).setIsUpdating(false);
                                            for (a = 0; a < 3; a++) {
                                                MessagesController.this.processUpdatesQueue(a, 1);
                                            }
                                        }
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).saveDiffParams(MessagesStorage.getInstance(MessagesController.this.currentAccount).lastSeqValue, MessagesStorage.getInstance(MessagesController.this.currentAccount).lastPtsValue, MessagesStorage.getInstance(MessagesController.this.currentAccount).lastDateValue, MessagesStorage.getInstance(MessagesController.this.currentAccount).lastQtsValue);
                                        FileLog.e("received difference with date = " + MessagesStorage.getInstance(MessagesController.this.currentAccount).lastDateValue + " pts = " + MessagesStorage.getInstance(MessagesController.this.currentAccount).lastPtsValue + " seq = " + MessagesStorage.getInstance(MessagesController.this.currentAccount).lastSeqValue + " messages = " + res.new_messages.size() + " users = " + res.users.size() + " chats = " + res.chats.size() + " other updates = " + res.other_updates.size());
                                    }
                                });
                            }
                        });
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
            if ((!secret || lower_id == 0) && ((secret || lower_id != 0) && dialog.pinned)) {
                count++;
            }
        }
        return count < this.maxPinnedDialogsCount;
    }

    public boolean pinDialog(long did, boolean pin, InputPeer peer, long taskId) {
        Throwable e;
        long newTaskId;
        int lower_id = (int) did;
        TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(Long.valueOf(did));
        if (dialog != null && dialog.pinned != pin) {
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
                req.peer = peer;
                if (taskId == 0) {
                    NativeByteBuffer data = null;
                    try {
                        NativeByteBuffer data2 = new NativeByteBuffer(peer.getObjectSize() + 16);
                        try {
                            data2.writeInt32(1);
                            data2.writeInt64(did);
                            data2.writeBool(pin);
                            peer.serializeToStream(data2);
                            data = data2;
                        } catch (Exception e2) {
                            e = e2;
                            data = data2;
                            FileLog.e(e);
                            newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                                public void run(TLObject response, TL_error error) {
                                    if (newTaskId != 0) {
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).removePendingTask(newTaskId);
                                    }
                                }
                            });
                            MessagesStorage.getInstance(this.currentAccount).setDialogPinned(did, dialog.pinnedNum);
                            return true;
                        }
                    } catch (Exception e3) {
                        e = e3;
                        FileLog.e(e);
                        newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                        ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
                        MessagesStorage.getInstance(this.currentAccount).setDialogPinned(did, dialog.pinnedNum);
                        return true;
                    }
                    newTaskId = MessagesStorage.getInstance(this.currentAccount).createPendingTask(data);
                } else {
                    newTaskId = taskId;
                }
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, /* anonymous class already generated */);
            }
            MessagesStorage.getInstance(this.currentAccount).setDialogPinned(did, dialog.pinnedNum);
            return true;
        } else if (dialog != null) {
            return true;
        } else {
            return false;
        }
    }

    public void loadPinnedDialogs(final long newDialogId, final ArrayList<Long> order) {
        if (!UserConfig.getInstance(this.currentAccount).pinnedDialogsLoaded) {
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(new TL_messages_getPinnedDialogs(), new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                    if (response != null) {
                        int a;
                        Chat chat;
                        final TL_messages_peerDialogs res = (TL_messages_peerDialogs) response;
                        final TL_messages_dialogs toCache = new TL_messages_dialogs();
                        toCache.users.addAll(res.users);
                        toCache.chats.addAll(res.chats);
                        toCache.dialogs.addAll(res.dialogs);
                        toCache.messages.addAll(res.messages);
                        final HashMap<Long, MessageObject> new_dialogMessage = new HashMap();
                        HashMap<Integer, User> usersDict = new HashMap();
                        HashMap<Integer, Chat> chatsDict = new HashMap();
                        final ArrayList<Long> newPinnedOrder = new ArrayList();
                        for (a = 0; a < res.users.size(); a++) {
                            User u = (User) res.users.get(a);
                            usersDict.put(Integer.valueOf(u.id), u);
                        }
                        for (a = 0; a < res.chats.size(); a++) {
                            Chat c = (Chat) res.chats.get(a);
                            chatsDict.put(Integer.valueOf(c.id), c);
                        }
                        for (a = 0; a < res.messages.size(); a++) {
                            Message message = (Message) res.messages.get(a);
                            MessageObject messageObject;
                            if (message.to_id.channel_id != 0) {
                                chat = (Chat) chatsDict.get(Integer.valueOf(message.to_id.channel_id));
                                if (chat != null && chat.left) {
                                }
                                messageObject = new MessageObject(MessagesController.this.currentAccount, message, usersDict, chatsDict, false);
                                new_dialogMessage.put(Long.valueOf(messageObject.getDialogId()), messageObject);
                            } else {
                                if (message.to_id.chat_id != 0) {
                                    chat = (Chat) chatsDict.get(Integer.valueOf(message.to_id.chat_id));
                                    if (!(chat == null || chat.migrated_to == null)) {
                                    }
                                }
                                messageObject = new MessageObject(MessagesController.this.currentAccount, message, usersDict, chatsDict, false);
                                new_dialogMessage.put(Long.valueOf(messageObject.getDialogId()), messageObject);
                            }
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
                            newPinnedOrder.add(Long.valueOf(d.id));
                            MessageObject mess;
                            Integer value;
                            if (DialogObject.isChannel(d)) {
                                chat = (Chat) chatsDict.get(Integer.valueOf(-((int) d.id)));
                                if (chat != null && chat.left) {
                                }
                                if (d.last_message_date == 0) {
                                    mess = (MessageObject) new_dialogMessage.get(Long.valueOf(d.id));
                                    if (mess != null) {
                                        d.last_message_date = mess.messageOwner.date;
                                    }
                                }
                                value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(d.id));
                                if (value == null) {
                                    value = Integer.valueOf(0);
                                }
                                MessagesController.this.dialogs_read_inbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_inbox_max_id)));
                                value = (Integer) MessagesController.this.dialogs_read_outbox_max.get(Long.valueOf(d.id));
                                if (value == null) {
                                    value = Integer.valueOf(0);
                                }
                                MessagesController.this.dialogs_read_outbox_max.put(Long.valueOf(d.id), Integer.valueOf(Math.max(value.intValue(), d.read_outbox_max_id)));
                            } else {
                                if (((int) d.id) < 0) {
                                    chat = (Chat) chatsDict.get(Integer.valueOf(-((int) d.id)));
                                    if (!(chat == null || chat.migrated_to == null)) {
                                    }
                                }
                                if (d.last_message_date == 0) {
                                    mess = (MessageObject) new_dialogMessage.get(Long.valueOf(d.id));
                                    if (mess != null) {
                                        d.last_message_date = mess.messageOwner.date;
                                    }
                                }
                                value = (Integer) MessagesController.this.dialogs_read_inbox_max.get(Long.valueOf(d.id));
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
                        }
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                            public void run() {
                                AndroidUtilities.runOnUIThread(new Runnable() {
                                    public void run() {
                                        int a;
                                        MessagesController.this.applyDialogsNotificationsSettings(res.dialogs);
                                        boolean changed = false;
                                        boolean added = false;
                                        int maxPinnedNum = 0;
                                        HashMap<Long, Integer> oldPinnedDialogNums = new HashMap();
                                        ArrayList<Long> oldPinnedOrder = new ArrayList();
                                        for (a = 0; a < MessagesController.this.dialogs.size(); a++) {
                                            TL_dialog dialog = (TL_dialog) MessagesController.this.dialogs.get(a);
                                            if (((int) dialog.id) != 0) {
                                                if (!dialog.pinned) {
                                                    break;
                                                }
                                                maxPinnedNum = Math.max(dialog.pinnedNum, maxPinnedNum);
                                                oldPinnedDialogNums.put(Long.valueOf(dialog.id), Integer.valueOf(dialog.pinnedNum));
                                                oldPinnedOrder.add(Long.valueOf(dialog.id));
                                                dialog.pinned = false;
                                                dialog.pinnedNum = 0;
                                                changed = true;
                                            }
                                        }
                                        ArrayList<Long> pinnedDialogs = new ArrayList();
                                        ArrayList<Long> orderArrayList = order != null ? order : newPinnedOrder;
                                        if (orderArrayList.size() < oldPinnedOrder.size()) {
                                            orderArrayList.add(Long.valueOf(0));
                                        }
                                        while (oldPinnedOrder.size() < orderArrayList.size()) {
                                            oldPinnedOrder.add(0, Long.valueOf(0));
                                        }
                                        if (!res.dialogs.isEmpty()) {
                                            MessagesController.this.putUsers(res.users, false);
                                            MessagesController.this.putChats(res.chats, false);
                                            for (a = 0; a < res.dialogs.size(); a++) {
                                                dialog = (TL_dialog) res.dialogs.get(a);
                                                Integer oldNum;
                                                if (newDialogId != 0) {
                                                    oldNum = (Integer) oldPinnedDialogNums.get(Long.valueOf(dialog.id));
                                                    if (oldNum != null) {
                                                        dialog.pinnedNum = oldNum.intValue();
                                                    }
                                                } else {
                                                    int oldIdx = oldPinnedOrder.indexOf(Long.valueOf(dialog.id));
                                                    int newIdx = orderArrayList.indexOf(Long.valueOf(dialog.id));
                                                    if (!(oldIdx == -1 || newIdx == -1)) {
                                                        if (oldIdx == newIdx) {
                                                            oldNum = (Integer) oldPinnedDialogNums.get(Long.valueOf(dialog.id));
                                                            if (oldNum != null) {
                                                                dialog.pinnedNum = oldNum.intValue();
                                                            }
                                                        } else {
                                                            oldNum = (Integer) oldPinnedDialogNums.get(Long.valueOf(((Long) oldPinnedOrder.get(newIdx)).longValue()));
                                                            if (oldNum != null) {
                                                                dialog.pinnedNum = oldNum.intValue();
                                                            }
                                                        }
                                                    }
                                                }
                                                if (dialog.pinnedNum == 0) {
                                                    dialog.pinnedNum = (res.dialogs.size() - a) + maxPinnedNum;
                                                }
                                                pinnedDialogs.add(Long.valueOf(dialog.id));
                                                TL_dialog d = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(dialog.id));
                                                if (d != null) {
                                                    d.pinned = true;
                                                    d.pinnedNum = dialog.pinnedNum;
                                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).setDialogPinned(dialog.id, dialog.pinnedNum);
                                                } else {
                                                    added = true;
                                                    MessagesController.this.dialogs_dict.put(Long.valueOf(dialog.id), dialog);
                                                    MessageObject messageObject = (MessageObject) new_dialogMessage.get(Long.valueOf(dialog.id));
                                                    MessagesController.this.dialogMessage.put(Long.valueOf(dialog.id), messageObject);
                                                    if (messageObject != null && messageObject.messageOwner.to_id.channel_id == 0) {
                                                        MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(messageObject.getId()), messageObject);
                                                        if (messageObject.messageOwner.random_id != 0) {
                                                            MessagesController.this.dialogMessagesByRandomIds.put(Long.valueOf(messageObject.messageOwner.random_id), messageObject);
                                                        }
                                                    }
                                                }
                                                changed = true;
                                            }
                                        }
                                        if (changed) {
                                            if (added) {
                                                MessagesController.this.dialogs.clear();
                                                MessagesController.this.dialogs.addAll(MessagesController.this.dialogs_dict.values());
                                            }
                                            MessagesController.this.sortDialogs(null);
                                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                        }
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).unpinAllDialogsExceptNew(pinnedDialogs);
                                        MessagesStorage.getInstance(MessagesController.this.currentAccount).putDialogs(toCache, true);
                                        UserConfig.getInstance(MessagesController.this.currentAccount).pinnedDialogsLoaded = true;
                                        UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
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
                ArrayList messagesArr = new ArrayList();
                messagesArr.add(message);
                pushMessages.add(new MessageObject(this.currentAccount, message, null, true));
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                    public void run() {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(pushMessages, true);
                            }
                        });
                    }
                });
                MessagesStorage.getInstance(this.currentAccount).putMessages(messagesArr, true, true, false, 0);
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
                if (chat != null && ChatObject.isChannel(chat_id, MessagesController.this.currentAccount) && !chat.creator) {
                    TL_channels_getParticipant req = new TL_channels_getParticipant();
                    req.channel = MessagesController.this.getInputChannel(chat_id);
                    req.user_id = new TL_inputUserSelf();
                    ConnectionsManager.getInstance(MessagesController.this.currentAccount).sendRequest(req, new RequestDelegate() {
                        public void run(TLObject response, TL_error error) {
                            final TL_channels_channelParticipant res = (TL_channels_channelParticipant) response;
                            if (res != null && (res.participant instanceof TL_channelParticipantSelf) && res.participant.inviter_id != UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()) {
                                if (!chat.megagroup || !MessagesStorage.getInstance(MessagesController.this.currentAccount).isMigratedChat(chat.id)) {
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.this.putUsers(res.users, false);
                                        }
                                    });
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, null, true, true);
                                    TL_messageService message = new TL_messageService();
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
                                    UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
                                    final ArrayList<MessageObject> pushMessages = new ArrayList();
                                    ArrayList messagesArr = new ArrayList();
                                    ConcurrentHashMap<Integer, User> usersDict = new ConcurrentHashMap();
                                    for (int a = 0; a < res.users.size(); a++) {
                                        User user = (User) res.users.get(a);
                                        usersDict.put(Integer.valueOf(user.id), user);
                                    }
                                    messagesArr.add(message);
                                    pushMessages.add(new MessageObject(MessagesController.this.currentAccount, message, usersDict, true));
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                                        public void run() {
                                            AndroidUtilities.runOnUIThread(new Runnable() {
                                                public void run() {
                                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(pushMessages, true);
                                                }
                                            });
                                        }
                                    });
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(messagesArr, true, true, false, 0);
                                    AndroidUtilities.runOnUIThread(new Runnable() {
                                        public void run() {
                                            MessagesController.this.updateInterfaceWithMessages((long) (-chat_id), pushMessages);
                                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    private int getUpdateType(Update update) {
        if ((update instanceof TL_updateNewMessage) || (update instanceof TL_updateReadMessagesContents) || (update instanceof TL_updateReadHistoryInbox) || (update instanceof TL_updateReadHistoryOutbox) || (update instanceof TL_updateDeleteMessages) || (update instanceof TL_updateWebPage) || (update instanceof TL_updateEditMessage)) {
            return 0;
        }
        if (update instanceof TL_updateNewEncryptedMessage) {
            return 1;
        }
        if ((update instanceof TL_updateNewChannelMessage) || (update instanceof TL_updateDeleteChannelMessages) || (update instanceof TL_updateEditChannelMessage) || (update instanceof TL_updateChannelWebPage)) {
            return 2;
        }
        return 3;
    }

    private int getUpdateChannelId(Update update) {
        if (update instanceof TL_updateNewChannelMessage) {
            return ((TL_updateNewChannelMessage) update).message.to_id.channel_id;
        }
        if (update instanceof TL_updateEditChannelMessage) {
            return ((TL_updateEditChannelMessage) update).message.to_id.channel_id;
        }
        return update.channel_id;
    }

    public void processUpdates(Updates updates, boolean fromQueue) {
        int a;
        ArrayList<Integer> needGetChannelsDiff = null;
        boolean needGetDiff = false;
        boolean needReceivedQueue = false;
        boolean updateStatus = false;
        if (updates instanceof TL_updateShort) {
            ArrayList<Update> arr = new ArrayList();
            arr.add(updates.update);
            processUpdateArray(arr, null, null, false);
        } else if ((updates instanceof TL_updateShortChatMessage) || (updates instanceof TL_updateShortMessage)) {
            int user_id;
            boolean missingData;
            if (updates instanceof TL_updateShortChatMessage) {
                user_id = updates.from_id;
            } else {
                user_id = updates.user_id;
            }
            User user = getUser(Integer.valueOf(user_id));
            User user2 = null;
            User user3 = null;
            Chat channel = null;
            if (user == null || user.min) {
                user = MessagesStorage.getInstance(this.currentAccount).getUserSync(user_id);
                if (user != null && user.min) {
                    user = null;
                }
                putUser(user, true);
            }
            boolean needFwdUser = false;
            if (updates.fwd_from != null) {
                if (updates.fwd_from.from_id != 0) {
                    user2 = getUser(Integer.valueOf(updates.fwd_from.from_id));
                    if (user2 == null) {
                        user2 = MessagesStorage.getInstance(this.currentAccount).getUserSync(updates.fwd_from.from_id);
                        putUser(user2, true);
                    }
                    needFwdUser = true;
                }
                if (updates.fwd_from.channel_id != 0) {
                    channel = getChat(Integer.valueOf(updates.fwd_from.channel_id));
                    if (channel == null) {
                        channel = MessagesStorage.getInstance(this.currentAccount).getChatSync(updates.fwd_from.channel_id);
                        putChat(channel, true);
                    }
                    needFwdUser = true;
                }
            }
            boolean needBotUser = false;
            if (updates.via_bot_id != 0) {
                user3 = getUser(Integer.valueOf(updates.via_bot_id));
                if (user3 == null) {
                    user3 = MessagesStorage.getInstance(this.currentAccount).getUserSync(updates.via_bot_id);
                    putUser(user3, true);
                }
                needBotUser = true;
            }
            if (updates instanceof TL_updateShortMessage) {
                missingData = user == null || ((needFwdUser && user2 == null && channel == null) || (needBotUser && user3 == null));
            } else {
                chat = getChat(Integer.valueOf(updates.chat_id));
                if (chat == null) {
                    chat = MessagesStorage.getInstance(this.currentAccount).getChatSync(updates.chat_id);
                    putChat(chat, true);
                }
                missingData = chat == null || user == null || ((needFwdUser && user2 == null && channel == null) || (needBotUser && user3 == null));
            }
            if (!missingData && !updates.entities.isEmpty()) {
                for (a = 0; a < updates.entities.size(); a++) {
                    MessageEntity entity = (MessageEntity) updates.entities.get(a);
                    if (entity instanceof TL_messageEntityMentionName) {
                        int uid = ((TL_messageEntityMentionName) entity).user_id;
                        User entityUser = getUser(Integer.valueOf(uid));
                        if (entityUser == null || entityUser.min) {
                            entityUser = MessagesStorage.getInstance(this.currentAccount).getUserSync(uid);
                            if (entityUser != null && entityUser.min) {
                                entityUser = null;
                            }
                            if (entityUser == null) {
                                missingData = true;
                                break;
                            }
                            putUser(user, true);
                        }
                    }
                }
            }
            if (!(user == null || user.status == null || user.status.expires > 0)) {
                this.onlinePrivacy.put(Integer.valueOf(user.id), Integer.valueOf(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()));
                updateStatus = true;
            }
            if (missingData) {
                needGetDiff = true;
            } else if (MessagesStorage.getInstance(this.currentAccount).lastPtsValue + updates.pts_count == updates.pts) {
                Message message = new TL_message();
                message.id = updates.id;
                int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                if (updates instanceof TL_updateShortMessage) {
                    if (updates.out) {
                        message.from_id = clientUserId;
                    } else {
                        message.from_id = user_id;
                    }
                    message.to_id = new TL_peerUser();
                    message.to_id.user_id = user_id;
                    message.dialog_id = (long) user_id;
                } else {
                    message.from_id = user_id;
                    message.to_id = new TL_peerChat();
                    message.to_id.chat_id = updates.chat_id;
                    message.dialog_id = (long) (-updates.chat_id);
                }
                message.fwd_from = updates.fwd_from;
                message.silent = updates.silent;
                message.out = updates.out;
                message.mentioned = updates.mentioned;
                message.media_unread = updates.media_unread;
                message.entities = updates.entities;
                message.message = updates.message;
                message.date = updates.date;
                message.via_bot_id = updates.via_bot_id;
                message.flags = updates.flags | 256;
                message.reply_to_msg_id = updates.reply_to_msg_id;
                message.media = new TL_messageMediaEmpty();
                ConcurrentHashMap<Long, Integer> read_max = message.out ? this.dialogs_read_outbox_max : this.dialogs_read_inbox_max;
                Integer value = (Integer) read_max.get(Long.valueOf(message.dialog_id));
                if (value == null) {
                    value = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                    read_max.put(Long.valueOf(message.dialog_id), value);
                }
                message.unread = value.intValue() < message.id;
                if (message.dialog_id == ((long) clientUserId)) {
                    message.unread = false;
                    message.media_unread = false;
                    message.out = true;
                }
                MessagesStorage.getInstance(this.currentAccount).lastPtsValue = updates.pts;
                MessageObject messageObject = new MessageObject(this.currentAccount, message, null, this.createdDialogIds.contains(Long.valueOf(message.dialog_id)));
                ArrayList<MessageObject> objArr = new ArrayList();
                objArr.add(messageObject);
                ArrayList arr2 = new ArrayList();
                arr2.add(message);
                boolean printUpdate;
                final boolean z;
                final ArrayList<MessageObject> arrayList;
                if (updates instanceof TL_updateShortMessage) {
                    printUpdate = !updates.out && updatePrintingUsersWithNewMessages((long) updates.user_id, objArr);
                    if (printUpdate) {
                        updatePrintingStrings();
                    }
                    z = printUpdate;
                    final int i = user_id;
                    arrayList = objArr;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (z) {
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(64));
                            }
                            MessagesController.this.updateInterfaceWithMessages((long) i, arrayList);
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                        }
                    });
                } else {
                    printUpdate = updatePrintingUsersWithNewMessages((long) (-updates.chat_id), objArr);
                    if (printUpdate) {
                        updatePrintingStrings();
                    }
                    z = printUpdate;
                    final Updates updates2 = updates;
                    arrayList = objArr;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            if (z) {
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(64));
                            }
                            MessagesController.this.updateInterfaceWithMessages((long) (-updates2.chat_id), arrayList);
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                        }
                    });
                }
                if (!messageObject.isOut()) {
                    final ArrayList<MessageObject> arrayList2 = objArr;
                    MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                        public void run() {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(arrayList2, true);
                                }
                            });
                        }
                    });
                }
                MessagesStorage.getInstance(this.currentAccount).putMessages(arr2, false, true, false, 0);
            } else if (MessagesStorage.getInstance(this.currentAccount).lastPtsValue != updates.pts) {
                FileLog.e("need get diff short message, pts: " + MessagesStorage.getInstance(this.currentAccount).lastPtsValue + " " + updates.pts + " count = " + updates.pts_count);
                if (this.gettingDifference || this.updatesStartWaitTimePts == 0 || Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimePts) <= 1500) {
                    if (this.updatesStartWaitTimePts == 0) {
                        this.updatesStartWaitTimePts = System.currentTimeMillis();
                    }
                    FileLog.e("add to queue");
                    this.updatesQueuePts.add(updates);
                } else {
                    needGetDiff = true;
                }
            }
        } else if ((updates instanceof TL_updatesCombined) || (updates instanceof TL_updates)) {
            Update update;
            int channelId;
            HashMap<Integer, Chat> minChannels = null;
            for (a = 0; a < updates.chats.size(); a++) {
                chat = (Chat) updates.chats.get(a);
                if ((chat instanceof TL_channel) && chat.min) {
                    Chat existChat = getChat(Integer.valueOf(chat.id));
                    if (existChat == null || existChat.min) {
                        Chat cacheChat = MessagesStorage.getInstance(this.currentAccount).getChatSync(updates.chat_id);
                        putChat(cacheChat, true);
                        existChat = cacheChat;
                    }
                    if (existChat == null || existChat.min) {
                        if (minChannels == null) {
                            minChannels = new HashMap();
                        }
                        minChannels.put(Integer.valueOf(chat.id), chat);
                    }
                }
            }
            if (minChannels != null) {
                for (a = 0; a < updates.updates.size(); a++) {
                    update = (Update) updates.updates.get(a);
                    if (update instanceof TL_updateNewChannelMessage) {
                        channelId = ((TL_updateNewChannelMessage) update).message.to_id.channel_id;
                        if (minChannels.containsKey(Integer.valueOf(channelId))) {
                            FileLog.e("need get diff because of min channel " + channelId);
                            needGetDiff = true;
                            break;
                        }
                    }
                }
            }
            if (!needGetDiff) {
                MessagesStorage.getInstance(this.currentAccount).putUsersAndChats(updates.users, updates.chats, true, true);
                Collections.sort(updates.updates, this.updatesComparator);
                a = 0;
                while (updates.updates.size() > 0) {
                    update = (Update) updates.updates.get(a);
                    TL_updates updatesNew;
                    int b;
                    Update update2;
                    if (getUpdateType(update) != 0) {
                        if (getUpdateType(update) != 1) {
                            if (getUpdateType(update) != 2) {
                                break;
                            }
                            channelId = getUpdateChannelId(update);
                            boolean skipUpdate = false;
                            Integer channelPts = (Integer) this.channelsPts.get(Integer.valueOf(channelId));
                            if (channelPts == null) {
                                channelPts = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(channelId));
                                if (channelPts.intValue() == 0) {
                                    for (int c = 0; c < updates.chats.size(); c++) {
                                        chat = (Chat) updates.chats.get(c);
                                        if (chat.id == channelId) {
                                            loadUnknownChannel(chat, 0);
                                            skipUpdate = true;
                                            break;
                                        }
                                    }
                                } else {
                                    this.channelsPts.put(Integer.valueOf(channelId), channelPts);
                                }
                            }
                            updatesNew = new TL_updates();
                            updatesNew.updates.add(update);
                            updatesNew.pts = update.pts;
                            updatesNew.pts_count = update.pts_count;
                            for (b = a + 1; b < updates.updates.size(); b = (b - 1) + 1) {
                                update2 = (Update) updates.updates.get(b);
                                if (getUpdateType(update2) != 2 || channelId != getUpdateChannelId(update2) || updatesNew.pts + update2.pts_count != update2.pts) {
                                    break;
                                }
                                updatesNew.updates.add(update2);
                                updatesNew.pts = update2.pts;
                                updatesNew.pts_count += update2.pts_count;
                                updates.updates.remove(b);
                            }
                            if (skipUpdate) {
                                FileLog.e("need load unknown channel = " + channelId);
                            } else if (channelPts.intValue() + updatesNew.pts_count == updatesNew.pts) {
                                if (processUpdateArray(updatesNew.updates, updates.users, updates.chats, false)) {
                                    this.channelsPts.put(Integer.valueOf(channelId), Integer.valueOf(updatesNew.pts));
                                    MessagesStorage.getInstance(this.currentAccount).saveChannelPts(channelId, updatesNew.pts);
                                } else {
                                    FileLog.e("need get channel diff inner TL_updates, channel_id = " + channelId);
                                    if (needGetChannelsDiff == null) {
                                        needGetChannelsDiff = new ArrayList();
                                    } else {
                                        if (!needGetChannelsDiff.contains(Integer.valueOf(channelId))) {
                                            needGetChannelsDiff.add(Integer.valueOf(channelId));
                                        }
                                    }
                                }
                            } else if (channelPts.intValue() != updatesNew.pts) {
                                FileLog.e(update + " need get channel diff, pts: " + channelPts + " " + updatesNew.pts + " count = " + updatesNew.pts_count + " channelId = " + channelId);
                                Long updatesStartWaitTime = (Long) this.updatesStartWaitTimeChannels.get(Integer.valueOf(channelId));
                                Boolean gettingDifferenceChannel = (Boolean) this.gettingDifferenceChannels.get(Integer.valueOf(channelId));
                                if (gettingDifferenceChannel == null) {
                                    gettingDifferenceChannel = Boolean.valueOf(false);
                                }
                                if (gettingDifferenceChannel.booleanValue() || updatesStartWaitTime == null || Math.abs(System.currentTimeMillis() - updatesStartWaitTime.longValue()) <= 1500) {
                                    if (updatesStartWaitTime == null) {
                                        this.updatesStartWaitTimeChannels.put(Integer.valueOf(channelId), Long.valueOf(System.currentTimeMillis()));
                                    }
                                    FileLog.e("add to queue");
                                    ArrayList<Updates> arrayList3 = (ArrayList) this.updatesQueueChannels.get(Integer.valueOf(channelId));
                                    if (arrayList3 == null) {
                                        arrayList3 = new ArrayList();
                                        this.updatesQueueChannels.put(Integer.valueOf(channelId), arrayList3);
                                    }
                                    arrayList3.add(updatesNew);
                                } else if (needGetChannelsDiff == null) {
                                    needGetChannelsDiff = new ArrayList();
                                } else {
                                    if (!needGetChannelsDiff.contains(Integer.valueOf(channelId))) {
                                        needGetChannelsDiff.add(Integer.valueOf(channelId));
                                    }
                                }
                            }
                        } else {
                            updatesNew = new TL_updates();
                            updatesNew.updates.add(update);
                            updatesNew.pts = update.qts;
                            for (b = a + 1; b < updates.updates.size(); b = (b - 1) + 1) {
                                update2 = (Update) updates.updates.get(b);
                                if (getUpdateType(update2) != 1 || updatesNew.pts + 1 != update2.qts) {
                                    break;
                                }
                                updatesNew.updates.add(update2);
                                updatesNew.pts = update2.qts;
                                updates.updates.remove(b);
                            }
                            if (MessagesStorage.getInstance(this.currentAccount).lastQtsValue == 0 || MessagesStorage.getInstance(this.currentAccount).lastQtsValue + updatesNew.updates.size() == updatesNew.pts) {
                                processUpdateArray(updatesNew.updates, updates.users, updates.chats, false);
                                MessagesStorage.getInstance(this.currentAccount).lastQtsValue = updatesNew.pts;
                                needReceivedQueue = true;
                            } else if (MessagesStorage.getInstance(this.currentAccount).lastPtsValue != updatesNew.pts) {
                                FileLog.e(update + " need get diff, qts: " + MessagesStorage.getInstance(this.currentAccount).lastQtsValue + " " + updatesNew.pts);
                                if (this.gettingDifference || this.updatesStartWaitTimeQts == 0 || (this.updatesStartWaitTimeQts != 0 && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimeQts) <= 1500)) {
                                    if (this.updatesStartWaitTimeQts == 0) {
                                        this.updatesStartWaitTimeQts = System.currentTimeMillis();
                                    }
                                    FileLog.e("add to queue");
                                    this.updatesQueueQts.add(updatesNew);
                                } else {
                                    needGetDiff = true;
                                }
                            }
                        }
                    } else {
                        updatesNew = new TL_updates();
                        updatesNew.updates.add(update);
                        updatesNew.pts = update.pts;
                        updatesNew.pts_count = update.pts_count;
                        for (b = a + 1; b < updates.updates.size(); b = (b - 1) + 1) {
                            update2 = (Update) updates.updates.get(b);
                            if (getUpdateType(update2) != 0 || updatesNew.pts + update2.pts_count != update2.pts) {
                                break;
                            }
                            updatesNew.updates.add(update2);
                            updatesNew.pts = update2.pts;
                            updatesNew.pts_count += update2.pts_count;
                            updates.updates.remove(b);
                        }
                        if (MessagesStorage.getInstance(this.currentAccount).lastPtsValue + updatesNew.pts_count == updatesNew.pts) {
                            if (processUpdateArray(updatesNew.updates, updates.users, updates.chats, false)) {
                                MessagesStorage.getInstance(this.currentAccount).lastPtsValue = updatesNew.pts;
                            } else {
                                FileLog.e("need get diff inner TL_updates, pts: " + MessagesStorage.getInstance(this.currentAccount).lastPtsValue + " " + updates.seq);
                                needGetDiff = true;
                            }
                        } else if (MessagesStorage.getInstance(this.currentAccount).lastPtsValue != updatesNew.pts) {
                            FileLog.e(update + " need get diff, pts: " + MessagesStorage.getInstance(this.currentAccount).lastPtsValue + " " + updatesNew.pts + " count = " + updatesNew.pts_count);
                            if (this.gettingDifference || this.updatesStartWaitTimePts == 0 || (this.updatesStartWaitTimePts != 0 && Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimePts) <= 1500)) {
                                if (this.updatesStartWaitTimePts == 0) {
                                    this.updatesStartWaitTimePts = System.currentTimeMillis();
                                }
                                FileLog.e("add to queue");
                                this.updatesQueuePts.add(updatesNew);
                            } else {
                                needGetDiff = true;
                            }
                        }
                    }
                    updates.updates.remove(a);
                    a = (a - 1) + 1;
                }
                boolean processUpdate = updates instanceof TL_updatesCombined ? MessagesStorage.getInstance(this.currentAccount).lastSeqValue + 1 == updates.seq_start || MessagesStorage.getInstance(this.currentAccount).lastSeqValue == updates.seq_start : MessagesStorage.getInstance(this.currentAccount).lastSeqValue + 1 == updates.seq || updates.seq == 0 || updates.seq == MessagesStorage.getInstance(this.currentAccount).lastSeqValue;
                if (processUpdate) {
                    processUpdateArray(updates.updates, updates.users, updates.chats, false);
                    if (updates.seq != 0) {
                        if (updates.date != 0) {
                            MessagesStorage.getInstance(this.currentAccount).lastDateValue = updates.date;
                        }
                        MessagesStorage.getInstance(this.currentAccount).lastSeqValue = updates.seq;
                    }
                } else {
                    if (updates instanceof TL_updatesCombined) {
                        FileLog.e("need get diff TL_updatesCombined, seq: " + MessagesStorage.getInstance(this.currentAccount).lastSeqValue + " " + updates.seq_start);
                    } else {
                        FileLog.e("need get diff TL_updates, seq: " + MessagesStorage.getInstance(this.currentAccount).lastSeqValue + " " + updates.seq);
                    }
                    if (this.gettingDifference || this.updatesStartWaitTimeSeq == 0 || Math.abs(System.currentTimeMillis() - this.updatesStartWaitTimeSeq) <= 1500) {
                        if (this.updatesStartWaitTimeSeq == 0) {
                            this.updatesStartWaitTimeSeq = System.currentTimeMillis();
                        }
                        FileLog.e("add TL_updates/Combined to queue");
                        this.updatesQueueSeq.add(updates);
                    } else {
                        needGetDiff = true;
                    }
                }
            }
        } else if (updates instanceof TL_updatesTooLong) {
            FileLog.e("need get diff TL_updatesTooLong");
            needGetDiff = true;
        } else if (updates instanceof UserActionUpdatesSeq) {
            MessagesStorage.getInstance(this.currentAccount).lastSeqValue = updates.seq;
        } else if (updates instanceof UserActionUpdatesPts) {
            if (updates.chat_id != 0) {
                this.channelsPts.put(Integer.valueOf(updates.chat_id), Integer.valueOf(updates.pts));
                MessagesStorage.getInstance(this.currentAccount).saveChannelPts(updates.chat_id, updates.pts);
            } else {
                MessagesStorage.getInstance(this.currentAccount).lastPtsValue = updates.pts;
            }
        }
        SecretChatHelper.getInstance(this.currentAccount).processPendingEncMessages();
        if (!fromQueue) {
            ArrayList<Integer> arrayList4 = new ArrayList(this.updatesQueueChannels.keySet());
            for (a = 0; a < arrayList4.size(); a++) {
                Integer key = (Integer) arrayList4.get(a);
                if (needGetChannelsDiff == null || !needGetChannelsDiff.contains(key)) {
                    processChannelsUpdatesQueue(key.intValue(), 0);
                } else {
                    getChannelDifference(key.intValue());
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
            TLObject req = new TL_messages_receivedQueue();
            req.max_qts = MessagesStorage.getInstance(this.currentAccount).lastQtsValue;
            ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                public void run(TLObject response, TL_error error) {
                }
            });
        }
        if (updateStatus) {
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(4));
                }
            });
        }
        MessagesStorage.getInstance(this.currentAccount).saveDiffParams(MessagesStorage.getInstance(this.currentAccount).lastSeqValue, MessagesStorage.getInstance(this.currentAccount).lastPtsValue, MessagesStorage.getInstance(this.currentAccount).lastDateValue, MessagesStorage.getInstance(this.currentAccount).lastQtsValue);
    }

    public boolean processUpdateArray(ArrayList<Update> updates, ArrayList<User> usersArr, ArrayList<Chat> chatsArr, boolean fromGetDifference) {
        if (updates.isEmpty()) {
            if (!(usersArr == null && chatsArr == null)) {
                final ArrayList<User> arrayList = usersArr;
                final ArrayList<Chat> arrayList2 = chatsArr;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        MessagesController.this.putUsers(arrayList, false);
                        MessagesController.this.putChats(arrayList2, false);
                    }
                });
            }
            return true;
        }
        AbstractMap usersDict;
        int a;
        AbstractMap chatsDict;
        long currentTime = System.currentTimeMillis();
        final HashMap<Long, ArrayList<MessageObject>> messages = new HashMap();
        HashMap<Long, WebPage> webPages = new HashMap();
        ArrayList<MessageObject> pushMessages = new ArrayList();
        ArrayList<Message> messagesArr = new ArrayList();
        final HashMap<Long, ArrayList<MessageObject>> editingMessages = new HashMap();
        final SparseArray<SparseIntArray> channelViews = new SparseArray();
        final SparseArray<Long> markAsReadMessagesInbox = new SparseArray();
        final SparseArray<Long> markAsReadMessagesOutbox = new SparseArray();
        final ArrayList<Long> markAsReadMessages = new ArrayList();
        final HashMap<Integer, Integer> markAsReadEncrypted = new HashMap();
        final SparseArray<ArrayList<Integer>> deletedMessages = new SparseArray();
        final SparseArray<Integer> clearHistoryMessages = new SparseArray();
        boolean printChanged = false;
        final ArrayList<ChatParticipants> chatInfoToUpdate = new ArrayList();
        ArrayList<Update> updatesOnMainThread = new ArrayList();
        ArrayList<TL_updateEncryptedMessagesRead> tasks = new ArrayList();
        final ArrayList<Integer> contactsIds = new ArrayList();
        boolean checkForUsers = true;
        if (usersArr != null) {
            usersDict = new ConcurrentHashMap();
            for (a = 0; a < usersArr.size(); a++) {
                User user = (User) usersArr.get(a);
                usersDict.put(Integer.valueOf(user.id), user);
            }
        } else {
            checkForUsers = false;
            usersDict = this.users;
        }
        if (chatsArr != null) {
            chatsDict = new ConcurrentHashMap();
            for (a = 0; a < chatsArr.size(); a++) {
                Chat chat = (Chat) chatsArr.get(a);
                chatsDict.put(Integer.valueOf(chat.id), chat);
            }
        } else {
            checkForUsers = false;
            chatsDict = this.chats;
        }
        if (fromGetDifference) {
            checkForUsers = false;
        }
        if (!(usersArr == null && chatsArr == null)) {
            arrayList = usersArr;
            arrayList2 = chatsArr;
            AndroidUtilities.runOnUIThread(new Runnable() {
                public void run() {
                    MessagesController.this.putUsers(arrayList, false);
                    MessagesController.this.putChats(arrayList2, false);
                }
            });
        }
        int interfaceUpdateMask = 0;
        for (int c = 0; c < updates.size(); c++) {
            Iterator it;
            Update update = (Update) updates.get(c);
            FileLog.d("process update " + update);
            Message message;
            int user_id;
            int count;
            MessageEntity entity;
            int clientUserId;
            ConcurrentHashMap<Long, Integer> read_max;
            Integer value;
            MessageObject obj;
            ArrayList<MessageObject> arr;
            if ((update instanceof TL_updateNewMessage) || (update instanceof TL_updateNewChannelMessage)) {
                if (update instanceof TL_updateNewMessage) {
                    message = ((TL_updateNewMessage) update).message;
                } else {
                    message = ((TL_updateNewChannelMessage) update).message;
                    if (BuildVars.DEBUG_VERSION) {
                        FileLog.d(update + " channelId = " + message.to_id.channel_id);
                    }
                    if (!message.out && message.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        message.out = true;
                    }
                }
                chat = null;
                int chat_id = 0;
                user_id = 0;
                if (message.to_id.channel_id != 0) {
                    chat_id = message.to_id.channel_id;
                } else if (message.to_id.chat_id != 0) {
                    chat_id = message.to_id.chat_id;
                } else if (message.to_id.user_id != 0) {
                    user_id = message.to_id.user_id;
                }
                if (chat_id != 0) {
                    chat = (Chat) chatsDict.get(Integer.valueOf(chat_id));
                    if (chat == null) {
                        chat = getChat(Integer.valueOf(chat_id));
                    }
                    if (chat == null) {
                        chat = MessagesStorage.getInstance(this.currentAccount).getChatSync(chat_id);
                        putChat(chat, true);
                    }
                }
                if (checkForUsers) {
                    if (chat_id == 0 || chat != null) {
                        count = message.entities.size() + 3;
                        for (a = 0; a < count; a++) {
                            boolean allowMin = false;
                            if (a != 0) {
                                if (a == 1) {
                                    user_id = message.from_id;
                                    if (message.post) {
                                        allowMin = true;
                                    }
                                } else if (a == 2) {
                                    user_id = message.fwd_from != null ? message.fwd_from.from_id : 0;
                                } else {
                                    entity = (MessageEntity) message.entities.get(a - 3);
                                    user_id = entity instanceof TL_messageEntityMentionName ? ((TL_messageEntityMentionName) entity).user_id : 0;
                                }
                            }
                            if (user_id > 0) {
                                user = (User) usersDict.get(Integer.valueOf(user_id));
                                if (user == null || (!allowMin && user.min)) {
                                    user = getUser(Integer.valueOf(user_id));
                                }
                                if (user == null || (!allowMin && user.min)) {
                                    user = MessagesStorage.getInstance(this.currentAccount).getUserSync(user_id);
                                    if (!(user == null || allowMin || !user.min)) {
                                        user = null;
                                    }
                                    putUser(user, true);
                                }
                                if (user == null) {
                                    FileLog.d("not found user " + user_id);
                                    return false;
                                } else if (a == 1 && user.status != null && user.status.expires <= 0) {
                                    this.onlinePrivacy.put(Integer.valueOf(user_id), Integer.valueOf(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()));
                                    interfaceUpdateMask |= 4;
                                }
                            }
                        }
                    } else {
                        FileLog.d("not found chat " + chat_id);
                        return false;
                    }
                }
                if (chat != null && chat.megagroup) {
                    message.flags |= Integer.MIN_VALUE;
                }
                if (message.action instanceof TL_messageActionChatDeleteUser) {
                    user = (User) usersDict.get(Integer.valueOf(message.action.user_id));
                    if (user != null && user.bot) {
                        message.reply_markup = new TL_replyKeyboardHide();
                        message.flags |= 64;
                    } else if (message.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId() && message.action.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                    }
                }
                messagesArr.add(message);
                ImageLoader.saveMessageThumbs(message);
                clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                if (message.to_id.chat_id != 0) {
                    message.dialog_id = (long) (-message.to_id.chat_id);
                } else if (message.to_id.channel_id != 0) {
                    message.dialog_id = (long) (-message.to_id.channel_id);
                } else {
                    if (message.to_id.user_id == clientUserId) {
                        message.to_id.user_id = message.from_id;
                    }
                    message.dialog_id = (long) message.to_id.user_id;
                }
                read_max = message.out ? this.dialogs_read_outbox_max : this.dialogs_read_inbox_max;
                value = (Integer) read_max.get(Long.valueOf(message.dialog_id));
                if (value == null) {
                    value = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                    read_max.put(Long.valueOf(message.dialog_id), value);
                }
                boolean z = value.intValue() < message.id && !((chat != null && ChatObject.isNotInChat(chat)) || (message.action instanceof TL_messageActionChatMigrateTo) || (message.action instanceof TL_messageActionChannelCreate));
                message.unread = z;
                if (message.dialog_id == ((long) clientUserId)) {
                    message.unread = false;
                    message.media_unread = false;
                    message.out = true;
                }
                obj = new MessageObject(this.currentAccount, message, usersDict, chatsDict, this.createdDialogIds.contains(Long.valueOf(message.dialog_id)));
                if (obj.type == 11) {
                    interfaceUpdateMask |= 8;
                } else if (obj.type == 10) {
                    interfaceUpdateMask |= 16;
                }
                arr = (ArrayList) messages.get(Long.valueOf(message.dialog_id));
                if (arr == null) {
                    arr = new ArrayList();
                    messages.put(Long.valueOf(message.dialog_id), arr);
                }
                arr.add(obj);
                if (!obj.isOut() && obj.isUnread()) {
                    pushMessages.add(obj);
                }
            } else if (update instanceof TL_updateReadMessagesContents) {
                for (a = 0; a < update.messages.size(); a++) {
                    markAsReadMessages.add(Long.valueOf((long) ((Integer) update.messages.get(a)).intValue()));
                }
            } else if (update instanceof TL_updateChannelReadMessagesContents) {
                for (a = 0; a < update.messages.size(); a++) {
                    markAsReadMessages.add(Long.valueOf(((long) ((Integer) update.messages.get(a)).intValue()) | (((long) update.channel_id) << 32)));
                }
            } else if ((update instanceof TL_updateReadHistoryInbox) || (update instanceof TL_updateReadHistoryOutbox)) {
                Peer peer;
                if (update instanceof TL_updateReadHistoryInbox) {
                    peer = ((TL_updateReadHistoryInbox) update).peer;
                    if (peer.chat_id != 0) {
                        markAsReadMessagesInbox.put(-peer.chat_id, Long.valueOf((long) update.max_id));
                        dialog_id = (long) (-peer.chat_id);
                    } else {
                        markAsReadMessagesInbox.put(peer.user_id, Long.valueOf((long) update.max_id));
                        dialog_id = (long) peer.user_id;
                    }
                    read_max = this.dialogs_read_inbox_max;
                } else {
                    peer = ((TL_updateReadHistoryOutbox) update).peer;
                    if (peer.chat_id != 0) {
                        markAsReadMessagesOutbox.put(-peer.chat_id, Long.valueOf((long) update.max_id));
                        dialog_id = (long) (-peer.chat_id);
                    } else {
                        markAsReadMessagesOutbox.put(peer.user_id, Long.valueOf((long) update.max_id));
                        dialog_id = (long) peer.user_id;
                    }
                    read_max = this.dialogs_read_outbox_max;
                }
                value = (Integer) read_max.get(Long.valueOf(dialog_id));
                if (value == null) {
                    value = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(update instanceof TL_updateReadHistoryOutbox, dialog_id));
                }
                read_max.put(Long.valueOf(dialog_id), Integer.valueOf(Math.max(value.intValue(), update.max_id)));
            } else if (update instanceof TL_updateDeleteMessages) {
                arrayList = (ArrayList) deletedMessages.get(0);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    deletedMessages.put(0, arrayList);
                }
                arrayList.addAll(update.messages);
            } else if ((update instanceof TL_updateUserTyping) || (update instanceof TL_updateChatUserTyping)) {
                if (update.user_id != UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                    uid = (long) (-update.chat_id);
                    if (uid == 0) {
                        uid = (long) update.user_id;
                    }
                    arr = (ArrayList) this.printingUsers.get(Long.valueOf(uid));
                    if (!(update.action instanceof TL_sendMessageCancelAction)) {
                        if (arr == null) {
                            arr = new ArrayList();
                            this.printingUsers.put(Long.valueOf(uid), arr);
                        }
                        exist = false;
                        it = arr.iterator();
                        while (it.hasNext()) {
                            u = (PrintingUser) it.next();
                            if (u.userId == update.user_id) {
                                exist = true;
                                u.lastTime = currentTime;
                                if (u.action.getClass() != update.action.getClass()) {
                                    printChanged = true;
                                }
                                u.action = update.action;
                                if (!exist) {
                                    newUser = new PrintingUser();
                                    newUser.userId = update.user_id;
                                    newUser.lastTime = currentTime;
                                    newUser.action = update.action;
                                    arr.add(newUser);
                                    printChanged = true;
                                }
                            }
                        }
                        if (exist) {
                            newUser = new PrintingUser();
                            newUser.userId = update.user_id;
                            newUser.lastTime = currentTime;
                            newUser.action = update.action;
                            arr.add(newUser);
                            printChanged = true;
                        }
                    } else if (arr != null) {
                        for (a = 0; a < arr.size(); a++) {
                            if (((PrintingUser) arr.get(a)).userId == update.user_id) {
                                arr.remove(a);
                                printChanged = true;
                                break;
                            }
                        }
                        if (arr.isEmpty()) {
                            this.printingUsers.remove(Long.valueOf(uid));
                        }
                    }
                    this.onlinePrivacy.put(Integer.valueOf(update.user_id), Integer.valueOf(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()));
                }
            } else if (update instanceof TL_updateChatParticipants) {
                interfaceUpdateMask |= 32;
                chatInfoToUpdate.add(update.participants);
            } else if (update instanceof TL_updateUserStatus) {
                interfaceUpdateMask |= 4;
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateUserName) {
                interfaceUpdateMask |= 1;
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateUserPhoto) {
                interfaceUpdateMask |= 2;
                MessagesStorage.getInstance(this.currentAccount).clearUserPhotos(update.user_id);
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateUserPhone) {
                interfaceUpdateMask |= 1024;
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateContactRegistered) {
                if (this.enableJoined && usersDict.containsKey(Integer.valueOf(update.user_id)) && !MessagesStorage.getInstance(this.currentAccount).isDialogHasMessages((long) update.user_id)) {
                    TL_messageService newMessage = new TL_messageService();
                    newMessage.action = new TL_messageActionUserJoined();
                    r5 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                    newMessage.id = r5;
                    newMessage.local_id = r5;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                    newMessage.unread = false;
                    newMessage.flags = 256;
                    newMessage.date = update.date;
                    newMessage.from_id = update.user_id;
                    newMessage.to_id = new TL_peerUser();
                    newMessage.to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    newMessage.dialog_id = (long) update.user_id;
                    messagesArr.add(newMessage);
                    r9 = new MessageObject(this.currentAccount, newMessage, usersDict, chatsDict, this.createdDialogIds.contains(Long.valueOf(newMessage.dialog_id)));
                    arr = (ArrayList) messages.get(Long.valueOf(newMessage.dialog_id));
                    if (arr == null) {
                        arr = new ArrayList();
                        messages.put(Long.valueOf(newMessage.dialog_id), arr);
                    }
                    arr.add(r9);
                }
            } else if (update instanceof TL_updateContactLink) {
                int idx;
                if (update.my_link instanceof TL_contactLinkContact) {
                    idx = contactsIds.indexOf(Integer.valueOf(-update.user_id));
                    if (idx != -1) {
                        contactsIds.remove(idx);
                    }
                    if (!contactsIds.contains(Integer.valueOf(update.user_id))) {
                        contactsIds.add(Integer.valueOf(update.user_id));
                    }
                } else {
                    idx = contactsIds.indexOf(Integer.valueOf(update.user_id));
                    if (idx != -1) {
                        contactsIds.remove(idx);
                    }
                    if (!contactsIds.contains(Integer.valueOf(update.user_id))) {
                        contactsIds.add(Integer.valueOf(-update.user_id));
                    }
                }
            } else if (update instanceof TL_updateNewGeoChatMessage) {
                continue;
            } else if (update instanceof TL_updateNewEncryptedMessage) {
                ArrayList<Message> decryptedMessages = SecretChatHelper.getInstance(this.currentAccount).decryptMessage(((TL_updateNewEncryptedMessage) update).message);
                if (!(decryptedMessages == null || decryptedMessages.isEmpty())) {
                    uid = ((long) ((TL_updateNewEncryptedMessage) update).message.chat_id) << 32;
                    arr = (ArrayList) messages.get(Long.valueOf(uid));
                    if (arr == null) {
                        arr = new ArrayList();
                        messages.put(Long.valueOf(uid), arr);
                    }
                    for (a = 0; a < decryptedMessages.size(); a++) {
                        message = (Message) decryptedMessages.get(a);
                        ImageLoader.saveMessageThumbs(message);
                        messagesArr.add(message);
                        obj = new MessageObject(this.currentAccount, message, usersDict, chatsDict, this.createdDialogIds.contains(Long.valueOf(uid)));
                        arr.add(obj);
                        pushMessages.add(obj);
                    }
                }
            } else if (update instanceof TL_updateEncryptedChatTyping) {
                EncryptedChat encryptedChat = getEncryptedChatDB(update.chat_id, true);
                if (encryptedChat != null) {
                    update.user_id = encryptedChat.user_id;
                    uid = ((long) update.chat_id) << 32;
                    arr = (ArrayList) this.printingUsers.get(Long.valueOf(uid));
                    if (arr == null) {
                        arr = new ArrayList();
                        this.printingUsers.put(Long.valueOf(uid), arr);
                    }
                    exist = false;
                    it = arr.iterator();
                    while (it.hasNext()) {
                        u = (PrintingUser) it.next();
                        if (u.userId == update.user_id) {
                            exist = true;
                            u.lastTime = currentTime;
                            u.action = new TL_sendMessageTypingAction();
                            break;
                        }
                    }
                    if (!exist) {
                        newUser = new PrintingUser();
                        newUser.userId = update.user_id;
                        newUser.lastTime = currentTime;
                        newUser.action = new TL_sendMessageTypingAction();
                        arr.add(newUser);
                        printChanged = true;
                    }
                    this.onlinePrivacy.put(Integer.valueOf(update.user_id), Integer.valueOf(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()));
                }
            } else if (update instanceof TL_updateEncryptedMessagesRead) {
                markAsReadEncrypted.put(Integer.valueOf(update.chat_id), Integer.valueOf(Math.max(update.max_date, update.date)));
                tasks.add((TL_updateEncryptedMessagesRead) update);
            } else if (update instanceof TL_updateChatParticipantAdd) {
                MessagesStorage.getInstance(this.currentAccount).updateChatInfo(update.chat_id, update.user_id, 0, update.inviter_id, update.version);
            } else if (update instanceof TL_updateChatParticipantDelete) {
                MessagesStorage.getInstance(this.currentAccount).updateChatInfo(update.chat_id, update.user_id, 1, 0, update.version);
            } else if ((update instanceof TL_updateDcOptions) || (update instanceof TL_updateConfig)) {
                ConnectionsManager.getInstance(this.currentAccount).updateDcSettings();
            } else if (update instanceof TL_updateEncryption) {
                SecretChatHelper.getInstance(this.currentAccount).processUpdateEncryption((TL_updateEncryption) update, usersDict);
            } else if (update instanceof TL_updateUserBlocked) {
                TL_updateUserBlocked finalUpdate = (TL_updateUserBlocked) update;
                if (finalUpdate.blocked) {
                    ArrayList<Integer> ids = new ArrayList();
                    ids.add(Integer.valueOf(finalUpdate.user_id));
                    MessagesStorage.getInstance(this.currentAccount).putBlockedUsers(ids, false);
                } else {
                    MessagesStorage.getInstance(this.currentAccount).deleteBlockedUser(finalUpdate.user_id);
                }
                final TL_updateUserBlocked tL_updateUserBlocked = finalUpdate;
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                    public void run() {
                        AndroidUtilities.runOnUIThread(new Runnable() {
                            public void run() {
                                if (!tL_updateUserBlocked.blocked) {
                                    MessagesController.this.blockedUsers.remove(Integer.valueOf(tL_updateUserBlocked.user_id));
                                } else if (!MessagesController.this.blockedUsers.contains(Integer.valueOf(tL_updateUserBlocked.user_id))) {
                                    MessagesController.this.blockedUsers.add(Integer.valueOf(tL_updateUserBlocked.user_id));
                                }
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.blockedUsersDidLoaded, new Object[0]);
                            }
                        });
                    }
                });
            } else if (update instanceof TL_updateNotifySettings) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateServiceNotification) {
                TL_updateServiceNotification notification = (TL_updateServiceNotification) update;
                if (notification.popup && notification.message != null && notification.message.length() > 0) {
                    final TL_updateServiceNotification tL_updateServiceNotification = notification;
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.needShowAlert, Integer.valueOf(2), tL_updateServiceNotification.message);
                        }
                    });
                }
                if ((notification.flags & 2) != 0) {
                    TL_message newMessage2 = new TL_message();
                    r5 = UserConfig.getInstance(this.currentAccount).getNewMessageId();
                    newMessage2.id = r5;
                    newMessage2.local_id = r5;
                    UserConfig.getInstance(this.currentAccount).saveConfig(false);
                    newMessage2.unread = true;
                    newMessage2.flags = 256;
                    if (notification.inbox_date != 0) {
                        newMessage2.date = notification.inbox_date;
                    } else {
                        newMessage2.date = (int) (System.currentTimeMillis() / 1000);
                    }
                    newMessage2.from_id = 777000;
                    newMessage2.to_id = new TL_peerUser();
                    newMessage2.to_id.user_id = UserConfig.getInstance(this.currentAccount).getClientUserId();
                    newMessage2.dialog_id = 777000;
                    if (update.media != null) {
                        newMessage2.media = update.media;
                        newMessage2.flags |= 512;
                    }
                    newMessage2.message = notification.message;
                    if (notification.entities != null) {
                        newMessage2.entities = notification.entities;
                    }
                    messagesArr.add(newMessage2);
                    r9 = new MessageObject(this.currentAccount, newMessage2, usersDict, chatsDict, this.createdDialogIds.contains(Long.valueOf(newMessage2.dialog_id)));
                    arr = (ArrayList) messages.get(Long.valueOf(newMessage2.dialog_id));
                    if (arr == null) {
                        arr = new ArrayList();
                        messages.put(Long.valueOf(newMessage2.dialog_id), arr);
                    }
                    arr.add(r9);
                    pushMessages.add(r9);
                }
            } else if (update instanceof TL_updateDialogPinned) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updatePinnedDialogs) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updatePrivacy) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateWebPage) {
                webPages.put(Long.valueOf(update.webpage.id), update.webpage);
            } else if (update instanceof TL_updateChannelWebPage) {
                webPages.put(Long.valueOf(update.webpage.id), update.webpage);
            } else if (update instanceof TL_updateChannelTooLong) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d(update + " channelId = " + update.channel_id);
                }
                Integer channelPts = (Integer) this.channelsPts.get(Integer.valueOf(update.channel_id));
                if (channelPts == null) {
                    channelPts = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getChannelPtsSync(update.channel_id));
                    if (channelPts.intValue() == 0) {
                        chat = (Chat) chatsDict.get(Integer.valueOf(update.channel_id));
                        if (chat == null || chat.min) {
                            chat = getChat(Integer.valueOf(update.channel_id));
                        }
                        if (chat == null || chat.min) {
                            chat = MessagesStorage.getInstance(this.currentAccount).getChatSync(update.channel_id);
                            putChat(chat, true);
                        }
                        if (!(chat == null || chat.min)) {
                            loadUnknownChannel(chat, 0);
                        }
                    } else {
                        this.channelsPts.put(Integer.valueOf(update.channel_id), channelPts);
                    }
                }
                if (channelPts.intValue() != 0) {
                    if ((update.flags & 1) == 0) {
                        getChannelDifference(update.channel_id);
                    } else if (update.pts > channelPts.intValue()) {
                        getChannelDifference(update.channel_id);
                    }
                }
            } else if ((update instanceof TL_updateReadChannelInbox) || (update instanceof TL_updateReadChannelOutbox)) {
                long message_id = ((long) update.max_id) | (((long) update.channel_id) << 32);
                dialog_id = (long) (-update.channel_id);
                if (update instanceof TL_updateReadChannelInbox) {
                    read_max = this.dialogs_read_inbox_max;
                    markAsReadMessagesInbox.put(-update.channel_id, Long.valueOf(message_id));
                } else {
                    read_max = this.dialogs_read_outbox_max;
                    markAsReadMessagesOutbox.put(-update.channel_id, Long.valueOf(message_id));
                }
                value = (Integer) read_max.get(Long.valueOf(dialog_id));
                if (value == null) {
                    value = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(update instanceof TL_updateReadChannelOutbox, dialog_id));
                }
                read_max.put(Long.valueOf(dialog_id), Integer.valueOf(Math.max(value.intValue(), update.max_id)));
            } else if (update instanceof TL_updateDeleteChannelMessages) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d(update + " channelId = " + update.channel_id);
                }
                arrayList = (ArrayList) deletedMessages.get(update.channel_id);
                if (arrayList == null) {
                    arrayList = new ArrayList();
                    deletedMessages.put(update.channel_id, arrayList);
                }
                arrayList.addAll(update.messages);
            } else if (update instanceof TL_updateChannel) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d(update + " channelId = " + update.channel_id);
                }
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateChannelMessageViews) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d(update + " channelId = " + update.channel_id);
                }
                TL_updateChannelMessageViews updateChannelMessageViews = (TL_updateChannelMessageViews) update;
                SparseIntArray array = (SparseIntArray) channelViews.get(update.channel_id);
                if (array == null) {
                    array = new SparseIntArray();
                    channelViews.put(update.channel_id, array);
                }
                array.put(updateChannelMessageViews.id, update.views);
            } else if (update instanceof TL_updateChatParticipantAdmin) {
                MessagesStorage.getInstance(this.currentAccount).updateChatInfo(update.chat_id, update.user_id, 2, update.is_admin ? 1 : 0, update.version);
            } else if (update instanceof TL_updateChatAdmins) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateStickerSets) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateStickerSetsOrder) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateNewStickerSet) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateDraftMessage) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateSavedGifs) {
                updatesOnMainThread.add(update);
            } else if ((update instanceof TL_updateEditChannelMessage) || (update instanceof TL_updateEditMessage)) {
                clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
                if (update instanceof TL_updateEditChannelMessage) {
                    message = ((TL_updateEditChannelMessage) update).message;
                    chat = (Chat) chatsDict.get(Integer.valueOf(message.to_id.channel_id));
                    if (chat == null) {
                        chat = getChat(Integer.valueOf(message.to_id.channel_id));
                    }
                    if (chat == null) {
                        chat = MessagesStorage.getInstance(this.currentAccount).getChatSync(message.to_id.channel_id);
                        putChat(chat, true);
                    }
                    if (chat != null && chat.megagroup) {
                        message.flags |= Integer.MIN_VALUE;
                    }
                } else {
                    message = ((TL_updateEditMessage) update).message;
                    if (message.dialog_id == ((long) clientUserId)) {
                        message.unread = false;
                        message.media_unread = false;
                        message.out = true;
                    }
                }
                if (!message.out && message.from_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                    message.out = true;
                }
                if (!fromGetDifference) {
                    count = message.entities.size();
                    for (a = 0; a < count; a++) {
                        entity = (MessageEntity) message.entities.get(a);
                        if (entity instanceof TL_messageEntityMentionName) {
                            user_id = ((TL_messageEntityMentionName) entity).user_id;
                            user = (User) usersDict.get(Integer.valueOf(user_id));
                            if (user == null || user.min) {
                                user = getUser(Integer.valueOf(user_id));
                            }
                            if (user == null || user.min) {
                                user = MessagesStorage.getInstance(this.currentAccount).getUserSync(user_id);
                                if (user != null && user.min) {
                                    user = null;
                                }
                                putUser(user, true);
                            }
                            if (user == null) {
                                return false;
                            }
                        }
                    }
                }
                if (message.to_id.chat_id != 0) {
                    message.dialog_id = (long) (-message.to_id.chat_id);
                } else if (message.to_id.channel_id != 0) {
                    message.dialog_id = (long) (-message.to_id.channel_id);
                } else {
                    if (message.to_id.user_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                        message.to_id.user_id = message.from_id;
                    }
                    message.dialog_id = (long) message.to_id.user_id;
                }
                read_max = message.out ? this.dialogs_read_outbox_max : this.dialogs_read_inbox_max;
                value = (Integer) read_max.get(Long.valueOf(message.dialog_id));
                if (value == null) {
                    value = Integer.valueOf(MessagesStorage.getInstance(this.currentAccount).getDialogReadMax(message.out, message.dialog_id));
                    read_max.put(Long.valueOf(message.dialog_id), value);
                }
                message.unread = value.intValue() < message.id;
                if (message.dialog_id == ((long) clientUserId)) {
                    message.out = true;
                    message.unread = false;
                    message.media_unread = false;
                }
                if (message.out && (message.message == null || message.message.length() == 0)) {
                    message.message = "-1";
                    message.attachPath = TtmlNode.ANONYMOUS_REGION_ID;
                }
                ImageLoader.saveMessageThumbs(message);
                obj = new MessageObject(this.currentAccount, message, usersDict, chatsDict, this.createdDialogIds.contains(Long.valueOf(message.dialog_id)));
                arr = (ArrayList) editingMessages.get(Long.valueOf(message.dialog_id));
                if (arr == null) {
                    arr = new ArrayList();
                    editingMessages.put(Long.valueOf(message.dialog_id), arr);
                }
                arr.add(obj);
            } else if (update instanceof TL_updateChannelPinnedMessage) {
                if (BuildVars.DEBUG_VERSION) {
                    FileLog.d(update + " channelId = " + update.channel_id);
                }
                MessagesStorage.getInstance(this.currentAccount).updateChannelPinnedMessage(update.channel_id, ((TL_updateChannelPinnedMessage) update).id);
            } else if (update instanceof TL_updateReadFeaturedStickers) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updatePhoneCall) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateLangPack) {
                LocaleController.getInstance().saveRemoteLocaleStrings(update.difference, this.currentAccount);
            } else if (update instanceof TL_updateLangPackTooLong) {
                LocaleController.getInstance().reloadCurrentRemoteLocale(this.currentAccount);
            } else if (update instanceof TL_updateFavedStickers) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateContactsReset) {
                updatesOnMainThread.add(update);
            } else if (update instanceof TL_updateChannelAvailableMessages) {
                Integer currentValue = (Integer) clearHistoryMessages.get(update.channel_id);
                if (currentValue == null || currentValue.intValue() < update.available_min_id) {
                    clearHistoryMessages.put(update.channel_id, Integer.valueOf(update.available_min_id));
                }
            }
        }
        if (!messages.isEmpty()) {
            for (Entry<Long, ArrayList<MessageObject>> pair : messages.entrySet()) {
                if (updatePrintingUsersWithNewMessages(((Long) pair.getKey()).longValue(), (ArrayList) pair.getValue())) {
                    printChanged = true;
                }
            }
        }
        if (printChanged) {
            updatePrintingStrings();
        }
        int interfaceUpdateMaskFinal = interfaceUpdateMask;
        final boolean printChangedArg = printChanged;
        if (!contactsIds.isEmpty()) {
            ContactsController.getInstance(this.currentAccount).processContactsUpdates(contactsIds, usersDict);
        }
        if (!pushMessages.isEmpty()) {
            final ArrayList<MessageObject> arrayList3 = pushMessages;
            MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                public void run() {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        public void run() {
                            NotificationsController.getInstance(MessagesController.this.currentAccount).processNewMessages(arrayList3, true);
                        }
                    });
                }
            });
        }
        if (!messagesArr.isEmpty()) {
            StatsController.getInstance(this.currentAccount).incrementReceivedItemsCount(ConnectionsManager.getCurrentNetworkType(), 1, messagesArr.size());
            MessagesStorage.getInstance(this.currentAccount).putMessages((ArrayList) messagesArr, true, true, false, DownloadController.getInstance(this.currentAccount).getAutodownloadMask());
        }
        if (!editingMessages.isEmpty()) {
            for (Entry<Long, ArrayList<MessageObject>> pair2 : editingMessages.entrySet()) {
                messages_Messages messagesRes = new TL_messages_messages();
                ArrayList<MessageObject> messageObjects = (ArrayList) pair2.getValue();
                for (a = 0; a < messageObjects.size(); a++) {
                    messagesRes.messages.add(((MessageObject) messageObjects.get(a)).messageOwner);
                }
                MessagesStorage.getInstance(this.currentAccount).putMessages(messagesRes, ((Long) pair2.getKey()).longValue(), -2, 0, false);
            }
        }
        if (channelViews.size() != 0) {
            MessagesStorage.getInstance(this.currentAccount).putChannelViews(channelViews, true);
        }
        final int i = interfaceUpdateMaskFinal;
        final ArrayList<Update> arrayList4 = updatesOnMainThread;
        final HashMap<Long, WebPage> hashMap = webPages;
        AndroidUtilities.runOnUIThread(new Runnable() {
            public void run() {
                int a;
                long dialog_id;
                ArrayList<MessageObject> arrayList;
                int updateMask = i;
                boolean hasDraftUpdates = false;
                if (!arrayList4.isEmpty()) {
                    ArrayList<User> dbUsers = new ArrayList();
                    ArrayList<User> dbUsersStatus = new ArrayList();
                    Editor editor = null;
                    for (a = 0; a < arrayList4.size(); a++) {
                        Update update = (Update) arrayList4.get(a);
                        User toDbUser = new TL_user();
                        toDbUser.id = update.user_id;
                        User currentUser = MessagesController.this.getUser(Integer.valueOf(update.user_id));
                        if (update instanceof TL_updatePrivacy) {
                            if (update.key instanceof TL_privacyKeyStatusTimestamp) {
                                ContactsController.getInstance(MessagesController.this.currentAccount).setPrivacyRules(update.rules, 0);
                            } else if (update.key instanceof TL_privacyKeyChatInvite) {
                                ContactsController.getInstance(MessagesController.this.currentAccount).setPrivacyRules(update.rules, 1);
                            } else if (update.key instanceof TL_privacyKeyPhoneCall) {
                                ContactsController.getInstance(MessagesController.this.currentAccount).setPrivacyRules(update.rules, 2);
                            }
                        } else if (update instanceof TL_updateUserStatus) {
                            if (update.status instanceof TL_userStatusRecently) {
                                update.status.expires = -100;
                            } else if (update.status instanceof TL_userStatusLastWeek) {
                                update.status.expires = -101;
                            } else if (update.status instanceof TL_userStatusLastMonth) {
                                update.status.expires = -102;
                            }
                            if (currentUser != null) {
                                currentUser.id = update.user_id;
                                currentUser.status = update.status;
                            }
                            toDbUser.status = update.status;
                            dbUsersStatus.add(toDbUser);
                            if (update.user_id == UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()) {
                                NotificationsController.getInstance(MessagesController.this.currentAccount).setLastOnlineFromOtherDevice(update.status.expires);
                            }
                        } else if (update instanceof TL_updateUserName) {
                            if (currentUser != null) {
                                if (!UserObject.isContact(currentUser)) {
                                    currentUser.first_name = update.first_name;
                                    currentUser.last_name = update.last_name;
                                }
                                if (currentUser.username != null && currentUser.username.length() > 0) {
                                    MessagesController.this.objectsByUsernames.remove(currentUser.username);
                                }
                                if (update.username != null && update.username.length() > 0) {
                                    MessagesController.this.objectsByUsernames.put(update.username, currentUser);
                                }
                                currentUser.username = update.username;
                            }
                            toDbUser.first_name = update.first_name;
                            toDbUser.last_name = update.last_name;
                            toDbUser.username = update.username;
                            dbUsers.add(toDbUser);
                        } else if (update instanceof TL_updateDialogPinned) {
                            TL_updateDialogPinned updateDialogPinned = (TL_updateDialogPinned) update;
                            if (updateDialogPinned.peer instanceof TL_peerUser) {
                                did = (long) updateDialogPinned.peer.user_id;
                            } else if (updateDialogPinned.peer instanceof TL_peerChat) {
                                did = (long) (-updateDialogPinned.peer.chat_id);
                            } else {
                                did = (long) (-updateDialogPinned.peer.channel_id);
                            }
                            if (!MessagesController.this.pinDialog(did, updateDialogPinned.pinned, null, -1)) {
                                UserConfig.getInstance(MessagesController.this.currentAccount).pinnedDialogsLoaded = false;
                                UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
                                MessagesController.this.loadPinnedDialogs(did, null);
                            }
                        } else if (update instanceof TL_updatePinnedDialogs) {
                            ArrayList<Long> order;
                            UserConfig.getInstance(MessagesController.this.currentAccount).pinnedDialogsLoaded = false;
                            UserConfig.getInstance(MessagesController.this.currentAccount).saveConfig(false);
                            if ((update.flags & 1) != 0) {
                                order = new ArrayList();
                                ArrayList<Peer> peers = ((TL_updatePinnedDialogs) update).order;
                                for (int b = 0; b < peers.size(); b++) {
                                    peer = (Peer) peers.get(b);
                                    if (peer.user_id != 0) {
                                        did = (long) peer.user_id;
                                    } else if (peer.chat_id != 0) {
                                        did = (long) (-peer.chat_id);
                                    } else {
                                        did = (long) (-peer.channel_id);
                                    }
                                    order.add(Long.valueOf(did));
                                }
                            } else {
                                order = null;
                            }
                            MessagesController.this.loadPinnedDialogs(0, order);
                        } else if (update instanceof TL_updateUserPhoto) {
                            if (currentUser != null) {
                                currentUser.photo = update.photo;
                            }
                            toDbUser.photo = update.photo;
                            dbUsers.add(toDbUser);
                        } else if (update instanceof TL_updateUserPhone) {
                            if (currentUser != null) {
                                currentUser.phone = update.phone;
                                final User user = currentUser;
                                Utilities.phoneBookQueue.postRunnable(new Runnable() {
                                    public void run() {
                                        ContactsController.getInstance(MessagesController.this.currentAccount).addContactToPhoneBook(user, true);
                                    }
                                });
                            }
                            toDbUser.phone = update.phone;
                            dbUsers.add(toDbUser);
                        } else if (update instanceof TL_updateNotifySettings) {
                            TL_updateNotifySettings updateNotifySettings = (TL_updateNotifySettings) update;
                            if ((update.notify_settings instanceof TL_peerNotifySettings) && (updateNotifySettings.peer instanceof TL_notifyPeer)) {
                                if (editor == null) {
                                    editor = MessagesController.this.notificationsPreferences.edit();
                                }
                                if (updateNotifySettings.peer.peer.user_id != 0) {
                                    dialog_id = (long) updateNotifySettings.peer.peer.user_id;
                                } else if (updateNotifySettings.peer.peer.chat_id != 0) {
                                    dialog_id = (long) (-updateNotifySettings.peer.peer.chat_id);
                                } else {
                                    dialog_id = (long) (-updateNotifySettings.peer.peer.channel_id);
                                }
                                dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(dialog_id));
                                if (dialog != null) {
                                    dialog.notify_settings = update.notify_settings;
                                }
                                editor.putBoolean("silent_" + dialog_id, update.notify_settings.silent);
                                int currentTime = ConnectionsManager.getInstance(MessagesController.this.currentAccount).getCurrentTime();
                                if (update.notify_settings.mute_until > currentTime) {
                                    int until = 0;
                                    if (update.notify_settings.mute_until > 31536000 + currentTime) {
                                        editor.putInt("notify2_" + dialog_id, 2);
                                        if (dialog != null) {
                                            dialog.notify_settings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
                                        }
                                    } else {
                                        until = update.notify_settings.mute_until;
                                        editor.putInt("notify2_" + dialog_id, 3);
                                        editor.putInt("notifyuntil_" + dialog_id, update.notify_settings.mute_until);
                                        if (dialog != null) {
                                            dialog.notify_settings.mute_until = until;
                                        }
                                    }
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).setDialogFlags(dialog_id, (((long) until) << 32) | 1);
                                    NotificationsController.getInstance(MessagesController.this.currentAccount).removeNotificationsForDialog(dialog_id);
                                } else {
                                    if (dialog != null) {
                                        dialog.notify_settings.mute_until = 0;
                                    }
                                    editor.remove("notify2_" + dialog_id);
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).setDialogFlags(dialog_id, 0);
                                }
                            }
                        } else if (update instanceof TL_updateChannel) {
                            dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(-((long) update.channel_id)));
                            Chat chat = MessagesController.this.getChat(Integer.valueOf(update.channel_id));
                            if (chat != null) {
                                if (dialog == null && (chat instanceof TL_channel) && !chat.left) {
                                    final Update update2 = update;
                                    Utilities.stageQueue.postRunnable(new Runnable() {
                                        public void run() {
                                            MessagesController.this.getChannelDifference(update2.channel_id, 1, 0, null);
                                        }
                                    });
                                } else if (chat.left && dialog != null) {
                                    MessagesController.this.deleteDialog(dialog.id, 0);
                                }
                            }
                            updateMask |= MessagesController.UPDATE_MASK_CHANNEL;
                            MessagesController.this.loadFullChat(update.channel_id, 0, true);
                        } else if (update instanceof TL_updateChatAdmins) {
                            updateMask |= MessagesController.UPDATE_MASK_CHAT_ADMINS;
                        } else if (update instanceof TL_updateStickerSets) {
                            DataQuery.getInstance(MessagesController.this.currentAccount).loadStickers(update.masks ? 1 : 0, false, true);
                        } else if (update instanceof TL_updateStickerSetsOrder) {
                            DataQuery.getInstance(MessagesController.this.currentAccount).reorderStickers(update.masks ? 1 : 0, ((TL_updateStickerSetsOrder) update).order);
                        } else if (update instanceof TL_updateFavedStickers) {
                            DataQuery.getInstance(MessagesController.this.currentAccount).loadRecents(2, false, false, true);
                        } else if (update instanceof TL_updateContactsReset) {
                            ContactsController.getInstance(MessagesController.this.currentAccount).forceImportContacts();
                        } else if (update instanceof TL_updateNewStickerSet) {
                            DataQuery.getInstance(MessagesController.this.currentAccount).addNewStickerSet(update.stickerset);
                        } else if (update instanceof TL_updateSavedGifs) {
                            MessagesController.this.emojiPreferences.edit().putLong("lastGifLoadTime", 0).commit();
                        } else if (update instanceof TL_updateRecentStickers) {
                            MessagesController.this.emojiPreferences.edit().putLong("lastStickersLoadTime", 0).commit();
                        } else if (update instanceof TL_updateDraftMessage) {
                            hasDraftUpdates = true;
                            peer = ((TL_updateDraftMessage) update).peer;
                            if (peer.user_id != 0) {
                                did = (long) peer.user_id;
                            } else if (peer.channel_id != 0) {
                                did = (long) (-peer.channel_id);
                            } else {
                                did = (long) (-peer.chat_id);
                            }
                            DataQuery.getInstance(MessagesController.this.currentAccount).saveDraft(did, update.draft, null, true);
                        } else if (update instanceof TL_updateReadFeaturedStickers) {
                            DataQuery.getInstance(MessagesController.this.currentAccount).markFaturedStickersAsRead(false);
                        } else if (update instanceof TL_updatePhoneCall) {
                            PhoneCall call = ((TL_updatePhoneCall) update).phone_call;
                            VoIPService svc = VoIPService.getSharedInstance();
                            if (BuildVars.DEBUG_VERSION) {
                                FileLog.d("Received call in update: " + call);
                                FileLog.d("call id " + call.id);
                            }
                            if (call instanceof TL_phoneCallRequested) {
                                if (call.date + (MessagesController.this.callRingTimeout / 1000) >= ConnectionsManager.getInstance(MessagesController.this.currentAccount).getCurrentTime()) {
                                    TelephonyManager tm = (TelephonyManager) ApplicationLoader.applicationContext.getSystemService("phone");
                                    if (svc == null && VoIPService.callIShouldHavePutIntoIntent == null && tm.getCallState() == 0) {
                                        if (BuildVars.DEBUG_VERSION) {
                                            FileLog.d("Starting service for call " + call.id);
                                        }
                                        VoIPService.callIShouldHavePutIntoIntent = call;
                                        Intent intent = new Intent(ApplicationLoader.applicationContext, VoIPService.class);
                                        intent.putExtra("is_outgoing", false);
                                        intent.putExtra("user_id", call.participant_id == UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId() ? call.admin_id : call.participant_id);
                                        intent.putExtra("account", MessagesController.this.currentAccount);
                                        try {
                                            if (VERSION.SDK_INT >= 26) {
                                                ApplicationLoader.applicationContext.startForegroundService(intent);
                                            } else {
                                                ApplicationLoader.applicationContext.startService(intent);
                                            }
                                        } catch (Throwable e) {
                                            FileLog.e(e);
                                        }
                                    } else {
                                        if (BuildVars.DEBUG_VERSION) {
                                            FileLog.d("Auto-declining call " + call.id + " because there's already active one");
                                        }
                                        TLObject req = new TL_phone_discardCall();
                                        req.peer = new TL_inputPhoneCall();
                                        req.peer.access_hash = call.access_hash;
                                        req.peer.id = call.id;
                                        req.reason = new TL_phoneCallDiscardReasonBusy();
                                        ConnectionsManager.getInstance(MessagesController.this.currentAccount).sendRequest(req, new RequestDelegate() {
                                            public void run(TLObject response, TL_error error) {
                                                if (response != null) {
                                                    MessagesController.this.processUpdates((Updates) response, false);
                                                }
                                            }
                                        });
                                    }
                                } else if (BuildVars.DEBUG_VERSION) {
                                    FileLog.d("ignoring too old call");
                                }
                            } else if (svc != null && call != null) {
                                svc.onCallUpdated(call);
                            } else if (VoIPService.callIShouldHavePutIntoIntent != null) {
                                FileLog.d("Updated the call while the service is starting");
                                if (call.id == VoIPService.callIShouldHavePutIntoIntent.id) {
                                    VoIPService.callIShouldHavePutIntoIntent = call;
                                }
                            }
                        } else if (!(update instanceof TL_updateGroupCall) && (update instanceof TL_updateGroupCallParticipant)) {
                        }
                    }
                    if (editor != null) {
                        editor.commit();
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.notificationsSettingsUpdated, new Object[0]);
                    }
                    MessagesStorage.getInstance(MessagesController.this.currentAccount).updateUsers(dbUsersStatus, true, true, true);
                    MessagesStorage.getInstance(MessagesController.this.currentAccount).updateUsers(dbUsers, false, true, true);
                }
                if (!hashMap.isEmpty()) {
                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.didReceivedWebpagesInUpdates, hashMap);
                    for (Entry<Long, WebPage> entry : hashMap.entrySet()) {
                        arrayList = (ArrayList) MessagesController.this.reloadingWebpagesPending.remove(entry.getKey());
                        if (arrayList != null) {
                            WebPage webpage = (WebPage) entry.getValue();
                            ArrayList messagesArr = new ArrayList();
                            dialog_id = 0;
                            if ((webpage instanceof TL_webPage) || (webpage instanceof TL_webPageEmpty)) {
                                for (a = 0; a < arrayList.size(); a++) {
                                    ((MessageObject) arrayList.get(a)).messageOwner.media.webpage = webpage;
                                    if (a == 0) {
                                        dialog_id = ((MessageObject) arrayList.get(a)).getDialogId();
                                        ImageLoader.saveMessageThumbs(((MessageObject) arrayList.get(a)).messageOwner);
                                    }
                                    messagesArr.add(((MessageObject) arrayList.get(a)).messageOwner);
                                }
                            } else {
                                MessagesController.this.reloadingWebpagesPending.put(Long.valueOf(webpage.id), arrayList);
                            }
                            if (!messagesArr.isEmpty()) {
                                MessagesStorage.getInstance(MessagesController.this.currentAccount).putMessages(messagesArr, true, true, false, DownloadController.getInstance(MessagesController.this.currentAccount).getAutodownloadMask());
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, Long.valueOf(dialog_id), arrayList);
                            }
                        }
                    }
                }
                boolean updateDialogs = false;
                if (!messages.isEmpty()) {
                    for (Entry<Long, ArrayList<MessageObject>> entry2 : messages.entrySet()) {
                        MessagesController.this.updateInterfaceWithMessages(((Long) entry2.getKey()).longValue(), (ArrayList) entry2.getValue());
                    }
                    updateDialogs = true;
                } else if (hasDraftUpdates) {
                    MessagesController.this.sortDialogs(null);
                    updateDialogs = true;
                }
                if (!editingMessages.isEmpty()) {
                    for (Entry<Long, ArrayList<MessageObject>> pair : editingMessages.entrySet()) {
                        Long dialog_id2 = (Long) pair.getKey();
                        arrayList = (ArrayList) pair.getValue();
                        MessageObject oldObject = (MessageObject) MessagesController.this.dialogMessage.get(dialog_id2);
                        if (oldObject != null) {
                            a = 0;
                            while (a < arrayList.size()) {
                                MessageObject newMessage = (MessageObject) arrayList.get(a);
                                if (oldObject.getId() == newMessage.getId()) {
                                    MessagesController.this.dialogMessage.put(dialog_id2, newMessage);
                                    if (newMessage.messageOwner.to_id != null && newMessage.messageOwner.to_id.channel_id == 0) {
                                        MessagesController.this.dialogMessagesByIds.put(Integer.valueOf(newMessage.getId()), newMessage);
                                    }
                                    updateDialogs = true;
                                } else {
                                    a++;
                                }
                            }
                        }
                        DataQuery.getInstance(MessagesController.this.currentAccount).loadReplyMessagesForMessages(arrayList, dialog_id2.longValue());
                        NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.replaceMessagesObjects, dialog_id2, arrayList);
                    }
                }
                if (updateDialogs) {
                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.dialogsNeedReload, new Object[0]);
                }
                if (printChangedArg) {
                    updateMask |= 64;
                }
                if (!contactsIds.isEmpty()) {
                    updateMask = (updateMask | 1) | 128;
                }
                if (!chatInfoToUpdate.isEmpty()) {
                    for (a = 0; a < chatInfoToUpdate.size(); a++) {
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).updateChatParticipants((ChatParticipants) chatInfoToUpdate.get(a));
                    }
                }
                if (channelViews.size() != 0) {
                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.didUpdatedMessagesViews, channelViews);
                }
                if (updateMask != 0) {
                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(updateMask));
                }
            }
        });
        MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
            public void run() {
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        int b;
                        int key;
                        MessageObject obj;
                        int a;
                        int updateMask = 0;
                        if (!(markAsReadMessagesInbox.size() == 0 && markAsReadMessagesOutbox.size() == 0)) {
                            int messageId;
                            TL_dialog dialog;
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesRead, markAsReadMessagesInbox, markAsReadMessagesOutbox);
                            NotificationsController.getInstance(MessagesController.this.currentAccount).processReadMessages(markAsReadMessagesInbox, 0, 0, 0, false);
                            if (markAsReadMessagesInbox.size() != 0) {
                                Editor editor = MessagesController.this.notificationsPreferences.edit();
                                for (b = 0; b < markAsReadMessagesInbox.size(); b++) {
                                    key = markAsReadMessagesInbox.keyAt(b);
                                    messageId = (int) ((Long) markAsReadMessagesInbox.get(key)).longValue();
                                    dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf((long) key));
                                    if (dialog != null && dialog.top_message > 0 && dialog.top_message <= messageId) {
                                        obj = (MessageObject) MessagesController.this.dialogMessage.get(Long.valueOf(dialog.id));
                                        if (!(obj == null || obj.isOut())) {
                                            obj.setIsRead();
                                            updateMask |= 256;
                                        }
                                    }
                                    if (key != UserConfig.getInstance(MessagesController.this.currentAccount).getClientUserId()) {
                                        editor.remove("diditem" + key);
                                        editor.remove("diditemo" + key);
                                    }
                                }
                                editor.commit();
                            }
                            for (b = 0; b < markAsReadMessagesOutbox.size(); b++) {
                                key = markAsReadMessagesOutbox.keyAt(b);
                                messageId = (int) ((Long) markAsReadMessagesOutbox.get(key)).longValue();
                                dialog = (TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf((long) key));
                                if (dialog != null && dialog.top_message > 0 && dialog.top_message <= messageId) {
                                    obj = (MessageObject) MessagesController.this.dialogMessage.get(Long.valueOf(dialog.id));
                                    if (obj != null && obj.isOut()) {
                                        obj.setIsRead();
                                        updateMask |= 256;
                                    }
                                }
                            }
                        }
                        if (!markAsReadEncrypted.isEmpty()) {
                            for (Entry<Integer, Integer> entry : markAsReadEncrypted.entrySet()) {
                                NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesReadEncrypted, entry.getKey(), entry.getValue());
                                long dialog_id = ((long) ((Integer) entry.getKey()).intValue()) << 32;
                                if (((TL_dialog) MessagesController.this.dialogs_dict.get(Long.valueOf(dialog_id))) != null) {
                                    MessageObject message = (MessageObject) MessagesController.this.dialogMessage.get(Long.valueOf(dialog_id));
                                    if (message != null && message.messageOwner.date <= ((Integer) entry.getValue()).intValue()) {
                                        message.setIsRead();
                                        updateMask |= 256;
                                    }
                                }
                            }
                        }
                        if (!markAsReadMessages.isEmpty()) {
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesReadContent, markAsReadMessages);
                        }
                        if (deletedMessages.size() != 0) {
                            for (a = 0; a < deletedMessages.size(); a++) {
                                key = deletedMessages.keyAt(a);
                                ArrayList<Integer> arrayList = (ArrayList) deletedMessages.get(key);
                                if (arrayList != null) {
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.messagesDeleted, arrayList, Integer.valueOf(key));
                                    if (key == 0) {
                                        for (b = 0; b < arrayList.size(); b++) {
                                            obj = (MessageObject) MessagesController.this.dialogMessagesByIds.get((Integer) arrayList.get(b));
                                            if (obj != null) {
                                                obj.deleted = true;
                                            }
                                        }
                                    } else {
                                        obj = (MessageObject) MessagesController.this.dialogMessage.get(Long.valueOf((long) (-key)));
                                        if (obj != null) {
                                            for (b = 0; b < arrayList.size(); b++) {
                                                if (obj.getId() == ((Integer) arrayList.get(b)).intValue()) {
                                                    obj.deleted = true;
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            NotificationsController.getInstance(MessagesController.this.currentAccount).removeDeletedMessagesFromNotifications(deletedMessages);
                        }
                        if (clearHistoryMessages.size() != 0) {
                            for (a = 0; a < clearHistoryMessages.size(); a++) {
                                key = clearHistoryMessages.keyAt(a);
                                Integer id = (Integer) clearHistoryMessages.get(key);
                                if (id != null) {
                                    long did = (long) (-key);
                                    NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.historyCleared, Long.valueOf(did), id);
                                    obj = (MessageObject) MessagesController.this.dialogMessage.get(Long.valueOf(did));
                                    if (obj != null && obj.getId() <= id.intValue()) {
                                        obj.deleted = true;
                                        break;
                                    }
                                }
                            }
                            NotificationsController.getInstance(MessagesController.this.currentAccount).removeDeletedHisoryFromNotifications(clearHistoryMessages);
                        }
                        if (updateMask != 0) {
                            NotificationCenter.getInstance(MessagesController.this.currentAccount).postNotificationName(NotificationCenter.updateInterfaces, Integer.valueOf(updateMask));
                        }
                    }
                });
            }
        });
        if (!webPages.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).putWebPages(webPages);
        }
        if (!(markAsReadMessagesInbox.size() == 0 && markAsReadMessagesOutbox.size() == 0 && markAsReadEncrypted.isEmpty() && markAsReadMessages.isEmpty())) {
            if (!(markAsReadMessagesInbox.size() == 0 && markAsReadMessages.isEmpty())) {
                MessagesStorage.getInstance(this.currentAccount).updateDialogsWithReadMessages(markAsReadMessagesInbox, markAsReadMessagesOutbox, markAsReadMessages, true);
            }
            MessagesStorage.getInstance(this.currentAccount).markMessagesAsRead(markAsReadMessagesInbox, markAsReadMessagesOutbox, markAsReadEncrypted, true);
        }
        if (!markAsReadMessages.isEmpty()) {
            MessagesStorage.getInstance(this.currentAccount).markMessagesContentAsRead(markAsReadMessages, ConnectionsManager.getInstance(this.currentAccount).getCurrentTime());
        }
        if (deletedMessages.size() != 0) {
            for (a = 0; a < deletedMessages.size(); a++) {
                int key = deletedMessages.keyAt(a);
                final ArrayList<Integer> arrayList5 = (ArrayList) deletedMessages.get(key);
                final int i2 = key;
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).updateDialogsWithDeletedMessages(arrayList5, MessagesStorage.getInstance(MessagesController.this.currentAccount).markMessagesAsDeleted(arrayList5, false, i2), false, i2);
                    }
                });
            }
        }
        if (clearHistoryMessages.size() != 0) {
            for (a = 0; a < clearHistoryMessages.size(); a++) {
                key = clearHistoryMessages.keyAt(a);
                final int i3 = key;
                final Integer num = (Integer) clearHistoryMessages.get(key);
                MessagesStorage.getInstance(this.currentAccount).getStorageQueue().postRunnable(new Runnable() {
                    public void run() {
                        MessagesStorage.getInstance(MessagesController.this.currentAccount).updateDialogsWithDeletedMessages(new ArrayList(), MessagesStorage.getInstance(MessagesController.this.currentAccount).markMessagesAsDeleted(i3, num.intValue(), false), false, i3);
                    }
                });
            }
        }
        if (!tasks.isEmpty()) {
            for (a = 0; a < tasks.size(); a++) {
                TL_updateEncryptedMessagesRead update2 = (TL_updateEncryptedMessagesRead) tasks.get(a);
                MessagesStorage.getInstance(this.currentAccount).createTaskForSecretChat(update2.chat_id, update2.max_date, update2.date, 1, null);
            }
        }
        return true;
    }

    private boolean isNotifySettingsMuted(PeerNotifySettings settings) {
        return (settings instanceof TL_peerNotifySettings) && settings.mute_until > ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
    }

    public boolean isDialogMuted(long dialog_id) {
        int mute_type = this.notificationsPreferences.getInt("notify2_" + dialog_id, 0);
        if (mute_type == 2) {
            return true;
        }
        if (mute_type != 3 || this.notificationsPreferences.getInt("notifyuntil_" + dialog_id, 0) < ConnectionsManager.getInstance(this.currentAccount).getCurrentTime()) {
            return false;
        }
        return true;
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
                int a = 0;
                while (a < arr.size()) {
                    if (messagesUsers.contains(Integer.valueOf(((PrintingUser) arr.get(a)).userId))) {
                        arr.remove(a);
                        a--;
                        if (arr.isEmpty()) {
                            this.printingUsers.remove(Long.valueOf(uid));
                        }
                        changed = true;
                    }
                    a++;
                }
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
        if (messages != null && !messages.isEmpty()) {
            boolean isEncryptedChat = ((int) uid) == 0;
            MessageObject lastMessage = null;
            int channelId = 0;
            boolean updateRating = false;
            for (int a = 0; a < messages.size(); a++) {
                MessageObject message = (MessageObject) messages.get(a);
                if (lastMessage == null || ((!isEncryptedChat && message.getId() > lastMessage.getId()) || (((isEncryptedChat || (message.getId() < 0 && lastMessage.getId() < 0)) && message.getId() < lastMessage.getId()) || message.messageOwner.date > lastMessage.messageOwner.date))) {
                    lastMessage = message;
                    if (message.messageOwner.to_id.channel_id != 0) {
                        channelId = message.messageOwner.to_id.channel_id;
                    }
                }
                if (!(!message.isOut() || message.isSending() || message.isForwarded())) {
                    if (message.isNewGif()) {
                        DataQuery.getInstance(this.currentAccount).addRecentGif(message.messageOwner.media.document, message.messageOwner.date);
                    } else if (message.isSticker()) {
                        DataQuery.getInstance(this.currentAccount).addRecentSticker(0, message.messageOwner.media.document, message.messageOwner.date, false);
                    }
                }
                if (message.isOut() && message.isSent()) {
                    updateRating = true;
                }
            }
            DataQuery.getInstance(this.currentAccount).loadReplyMessagesForMessages(messages, uid);
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.didReceivedNewMessages, Long.valueOf(uid), messages);
            if (lastMessage != null) {
                TL_dialog dialog = (TL_dialog) this.dialogs_dict.get(Long.valueOf(uid));
                MessageObject object;
                if (!(lastMessage.messageOwner.action instanceof TL_messageActionChatMigrateTo)) {
                    boolean changed = false;
                    if (dialog == null) {
                        if (!isBroadcast) {
                            Chat chat = getChat(Integer.valueOf(channelId));
                            if (channelId != 0 && chat == null) {
                                return;
                            }
                            if (chat == null || !chat.left) {
                                dialog = new TL_dialog();
                                dialog.id = uid;
                                dialog.unread_count = 0;
                                dialog.top_message = lastMessage.getId();
                                dialog.last_message_date = lastMessage.messageOwner.date;
                                dialog.flags = ChatObject.isChannel(chat) ? 1 : 0;
                                this.dialogs_dict.put(Long.valueOf(uid), dialog);
                                this.dialogs.add(dialog);
                                this.dialogMessage.put(Long.valueOf(uid), lastMessage);
                                if (lastMessage.messageOwner.to_id.channel_id == 0) {
                                    this.dialogMessagesByIds.put(Integer.valueOf(lastMessage.getId()), lastMessage);
                                    if (lastMessage.messageOwner.random_id != 0) {
                                        this.dialogMessagesByRandomIds.put(Long.valueOf(lastMessage.messageOwner.random_id), lastMessage);
                                    }
                                }
                                this.nextDialogsCacheOffset++;
                                changed = true;
                            } else {
                                return;
                            }
                        }
                    } else if ((dialog.top_message > 0 && lastMessage.getId() > 0 && lastMessage.getId() > dialog.top_message) || ((dialog.top_message < 0 && lastMessage.getId() < 0 && lastMessage.getId() < dialog.top_message) || !this.dialogMessage.containsKey(Long.valueOf(uid)) || dialog.top_message < 0 || dialog.last_message_date <= lastMessage.messageOwner.date)) {
                        object = (MessageObject) this.dialogMessagesByIds.remove(Integer.valueOf(dialog.top_message));
                        if (!(object == null || object.messageOwner.random_id == 0)) {
                            this.dialogMessagesByRandomIds.remove(Long.valueOf(object.messageOwner.random_id));
                        }
                        dialog.top_message = lastMessage.getId();
                        if (!isBroadcast) {
                            dialog.last_message_date = lastMessage.messageOwner.date;
                            changed = true;
                        }
                        this.dialogMessage.put(Long.valueOf(uid), lastMessage);
                        if (lastMessage.messageOwner.to_id.channel_id == 0) {
                            this.dialogMessagesByIds.put(Integer.valueOf(lastMessage.getId()), lastMessage);
                            if (lastMessage.messageOwner.random_id != 0) {
                                this.dialogMessagesByRandomIds.put(Long.valueOf(lastMessage.messageOwner.random_id), lastMessage);
                            }
                        }
                    }
                    if (changed) {
                        sortDialogs(null);
                    }
                    if (updateRating) {
                        DataQuery.getInstance(this.currentAccount).increasePeerRaiting(uid);
                    }
                } else if (dialog != null) {
                    this.dialogs.remove(dialog);
                    this.dialogsServerOnly.remove(dialog);
                    this.dialogsGroupsOnly.remove(dialog);
                    this.dialogs_dict.remove(Long.valueOf(dialog.id));
                    this.dialogs_read_inbox_max.remove(Long.valueOf(dialog.id));
                    this.dialogs_read_outbox_max.remove(Long.valueOf(dialog.id));
                    this.nextDialogsCacheOffset--;
                    this.dialogMessage.remove(Long.valueOf(dialog.id));
                    object = (MessageObject) this.dialogMessagesByIds.remove(Integer.valueOf(dialog.top_message));
                    if (!(object == null || object.messageOwner.random_id == 0)) {
                        this.dialogMessagesByRandomIds.remove(Long.valueOf(object.messageOwner.random_id));
                    }
                    dialog.top_message = 0;
                    NotificationsController.getInstance(this.currentAccount).removeNotificationsForDialog(dialog.id);
                    NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.needReloadRecentDialogsSearch, new Object[0]);
                }
            }
        }
    }

    public void sortDialogs(HashMap<Integer, Chat> chatsDict) {
        this.dialogsServerOnly.clear();
        this.dialogsGroupsOnly.clear();
        this.dialogsForward.clear();
        boolean selfAdded = false;
        int selfId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        Collections.sort(this.dialogs, this.dialogComparator);
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
                    if (chat != null && ((chat.megagroup && chat.admin_rights != null && chat.admin_rights.post_messages) || chat.creator)) {
                        this.dialogsGroupsOnly.add(d);
                    }
                } else if (lower_id < 0) {
                    if (chatsDict != null) {
                        chat = (Chat) chatsDict.get(Integer.valueOf(-lower_id));
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
                TL_dialog dialog = new TL_dialog();
                dialog.id = (long) user.id;
                dialog.notify_settings = new TL_peerNotifySettings();
                dialog.peer = new TL_peerUser();
                dialog.peer.user_id = user.id;
                this.dialogsForward.add(0, dialog);
            }
        }
    }

    private static String getRestrictionReason(String reason) {
        if (reason == null || reason.length() == 0) {
            return null;
        }
        int index = reason.indexOf(": ");
        if (index <= 0) {
            return null;
        }
        String type = reason.substring(0, index);
        if (type.contains("-all") || type.contains("-android")) {
            return reason.substring(index + 2);
        }
        return null;
    }

    private static void showCantOpenAlert(BaseFragment fragment, String reason) {
        if (fragment != null && fragment.getParentActivity() != null) {
            Builder builder = new Builder(fragment.getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            builder.setMessage(reason);
            fragment.showDialog(builder.create());
        }
    }

    public boolean checkCanOpenChat(Bundle bundle, BaseFragment fragment) {
        return checkCanOpenChat(bundle, fragment, null);
    }

    public boolean checkCanOpenChat(Bundle bundle, BaseFragment fragment, MessageObject originalMessage) {
        if (bundle == null || fragment == null) {
            return true;
        }
        User user = null;
        Chat chat = null;
        int user_id = bundle.getInt("user_id", 0);
        int chat_id = bundle.getInt("chat_id", 0);
        int messageId = bundle.getInt("message_id", 0);
        if (user_id != 0) {
            user = getUser(Integer.valueOf(user_id));
        } else if (chat_id != 0) {
            chat = getChat(Integer.valueOf(chat_id));
        }
        if (user == null && chat == null) {
            return true;
        }
        String reason = null;
        if (chat != null) {
            reason = getRestrictionReason(chat.restriction_reason);
        } else if (user != null) {
            reason = getRestrictionReason(user.restriction_reason);
        }
        if (reason != null) {
            showCantOpenAlert(fragment, reason);
            return false;
        }
        if (!(messageId == 0 || originalMessage == null || chat == null || chat.access_hash != 0)) {
            int did = (int) originalMessage.getDialogId();
            if (did != 0) {
                TLObject req;
                final AlertDialog progressDialog = new AlertDialog(fragment.getParentActivity(), 1);
                progressDialog.setMessage(LocaleController.getString("Loading", R.string.Loading));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                if (did < 0) {
                    chat = getChat(Integer.valueOf(-did));
                }
                TLObject request;
                if (did > 0 || !ChatObject.isChannel(chat)) {
                    request = new TL_messages_getMessages();
                    request.id.add(Integer.valueOf(originalMessage.getId()));
                    req = request;
                } else {
                    chat = getChat(Integer.valueOf(-did));
                    request = new TL_channels_getMessages();
                    request.channel = getInputChannel(chat);
                    request.id.add(Integer.valueOf(originalMessage.getId()));
                    req = request;
                }
                final BaseFragment baseFragment = fragment;
                final Bundle bundle2 = bundle;
                final int reqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new RequestDelegate() {
                    public void run(final TLObject response, TL_error error) {
                        if (response != null) {
                            AndroidUtilities.runOnUIThread(new Runnable() {
                                public void run() {
                                    try {
                                        progressDialog.dismiss();
                                    } catch (Throwable e) {
                                        FileLog.e(e);
                                    }
                                    messages_Messages res = response;
                                    MessagesController.this.putUsers(res.users, false);
                                    MessagesController.this.putChats(res.chats, false);
                                    MessagesStorage.getInstance(MessagesController.this.currentAccount).putUsersAndChats(res.users, res.chats, true, true);
                                    baseFragment.presentFragment(new ChatActivity(bundle2), true);
                                }
                            });
                        }
                    }
                });
                baseFragment = fragment;
                progressDialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectionsManager.getInstance(MessagesController.this.currentAccount).cancelRequest(reqId, true);
                        try {
                            dialog.dismiss();
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        if (baseFragment != null) {
                            baseFragment.setVisibleDialog(null);
                        }
                    }
                });
                fragment.setVisibleDialog(progressDialog);
                progressDialog.show();
                return false;
            }
        }
        return true;
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
                return;
            }
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

    public void openByUserName(String username, final BaseFragment fragment, final int type) {
        if (username != null && fragment != null) {
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