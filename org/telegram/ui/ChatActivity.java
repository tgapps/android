package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.extractor.ts.PsExtractor;
import com.google.android.exoplayer2.extractor.ts.TsExtractor;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.util.MimeTypes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.EmojiSuggestion;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.ImageReceiver.BitmapHolder;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MediaController.PhotoEntry;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessageObject.GroupedMessagePosition;
import org.telegram.messenger.MessageObject.GroupedMessages;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SecretChatHelper;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SendMessagesHelper.SendingMediaInfo;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.VideoEditedInfo;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.messenger.support.widget.GridLayoutManager.SpanSizeLookup;
import org.telegram.messenger.support.widget.GridLayoutManagerFixed;
import org.telegram.messenger.support.widget.LinearLayoutManager;
import org.telegram.messenger.support.widget.LinearSmoothScrollerMiddle;
import org.telegram.messenger.support.widget.RecyclerView;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;
import org.telegram.messenger.support.widget.RecyclerView.ItemDecoration;
import org.telegram.messenger.support.widget.RecyclerView.LayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.LayoutParams;
import org.telegram.messenger.support.widget.RecyclerView.OnScrollListener;
import org.telegram.messenger.support.widget.RecyclerView.State;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;
import org.telegram.messenger.support.widget.helper.ItemTouchHelper.Callback;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInfo;
import org.telegram.tgnet.TLRPC.BotInlineResult;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;
import org.telegram.tgnet.TLRPC.ChatParticipant;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.DraftMessage;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.InputStickerSet;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.Message;
import org.telegram.tgnet.TLRPC.MessageEntity;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_botCommand;
import org.telegram.tgnet.TLRPC.TL_channelForbidden;
import org.telegram.tgnet.TLRPC.TL_channelFull;
import org.telegram.tgnet.TLRPC.TL_channelParticipantAdmin;
import org.telegram.tgnet.TLRPC.TL_channelParticipantCreator;
import org.telegram.tgnet.TLRPC.TL_channels_channelParticipant;
import org.telegram.tgnet.TLRPC.TL_channels_exportMessageLink;
import org.telegram.tgnet.TLRPC.TL_channels_getParticipant;
import org.telegram.tgnet.TLRPC.TL_channels_reportSpam;
import org.telegram.tgnet.TLRPC.TL_chatForbidden;
import org.telegram.tgnet.TLRPC.TL_chatFull;
import org.telegram.tgnet.TLRPC.TL_chatParticipantsForbidden;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_encryptedChat;
import org.telegram.tgnet.TLRPC.TL_encryptedChatDiscarded;
import org.telegram.tgnet.TLRPC.TL_encryptedChatRequested;
import org.telegram.tgnet.TLRPC.TL_encryptedChatWaiting;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_exportedMessageLink;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_game;
import org.telegram.tgnet.TLRPC.TL_inlineBotSwitchPM;
import org.telegram.tgnet.TLRPC.TL_inputMessageEntityMentionName;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetID;
import org.telegram.tgnet.TLRPC.TL_inputStickerSetShortName;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonSwitchInline;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonUrl;
import org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
import org.telegram.tgnet.TLRPC.TL_messageActionEmpty;
import org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall;
import org.telegram.tgnet.TLRPC.TL_messageActionPinMessage;
import org.telegram.tgnet.TLRPC.TL_messageActionSecureValuesSent;
import org.telegram.tgnet.TLRPC.TL_messageEntityBold;
import org.telegram.tgnet.TLRPC.TL_messageEntityCode;
import org.telegram.tgnet.TLRPC.TL_messageEntityItalic;
import org.telegram.tgnet.TLRPC.TL_messageEntityMentionName;
import org.telegram.tgnet.TLRPC.TL_messageEntityPre;
import org.telegram.tgnet.TLRPC.TL_messageEntityTextUrl;
import org.telegram.tgnet.TLRPC.TL_messageMediaGame;
import org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_messages_getMessageEditData;
import org.telegram.tgnet.TLRPC.TL_messages_getUnreadMentions;
import org.telegram.tgnet.TLRPC.TL_messages_getWebPagePreview;
import org.telegram.tgnet.TLRPC.TL_peerNotifySettings;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
import org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC.TL_replyInlineMarkup;
import org.telegram.tgnet.TLRPC.TL_replyKeyboardForceReply;
import org.telegram.tgnet.TLRPC.TL_userFull;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.TL_webPagePending;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WebPage;
import org.telegram.tgnet.TLRPC.messages_Messages;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarLayout;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.ActionBarMenuItem.ActionBarMenuItemSearchListener;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BackDrawable;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.BottomSheet.Builder;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.Theme.ThemeInfo;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.ActionBar.ThemeDescription.ThemeDescriptionDelegate;
import org.telegram.ui.Adapters.MentionsAdapter;
import org.telegram.ui.Adapters.MentionsAdapter.MentionsAdapterDelegate;
import org.telegram.ui.Adapters.StickersAdapter;
import org.telegram.ui.Cells.BotHelpCell;
import org.telegram.ui.Cells.BotSwitchCell;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatActionCell.ChatActionCellDelegate;
import org.telegram.ui.Cells.ChatLoadingCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Cells.ChatMessageCell.ChatMessageCellDelegate;
import org.telegram.ui.Cells.ChatUnreadCell;
import org.telegram.ui.Cells.CheckBoxCell;
import org.telegram.ui.Cells.ContextLinkCell;
import org.telegram.ui.Cells.MentionCell;
import org.telegram.ui.Cells.StickerCell;
import org.telegram.ui.Components.AlertsCreator;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate;
import org.telegram.ui.Components.ChatAttachAlert;
import org.telegram.ui.Components.ChatAttachAlert.ChatAttachViewDelegate;
import org.telegram.ui.Components.ChatAvatarContainer;
import org.telegram.ui.Components.ChatBigEmptyView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.CorrectlyMeasuringTextView;
import org.telegram.ui.Components.EmbedBottomSheet;
import org.telegram.ui.Components.EmojiView;
import org.telegram.ui.Components.ExtendedGridLayoutManager;
import org.telegram.ui.Components.FragmentContextView;
import org.telegram.ui.Components.InstantCameraView;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.PipRoundVideoView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.RecyclerListView.Holder;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListener;
import org.telegram.ui.Components.RecyclerListView.OnItemClickListenerExtended;
import org.telegram.ui.Components.RecyclerListView.OnItemLongClickListenerExtended;
import org.telegram.ui.Components.ShareAlert;
import org.telegram.ui.Components.Size;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StickersAlert;
import org.telegram.ui.Components.StickersAlert.StickersAlertDelegate;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.Components.URLSpanReplacement;
import org.telegram.ui.Components.URLSpanUserMention;
import org.telegram.ui.Components.voip.VoIPHelper;
import org.telegram.ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.ui.DocumentSelectActivity.DocumentSelectActivityDelegate;
import org.telegram.ui.LocationActivity.LocationActivityDelegate;
import org.telegram.ui.PhotoAlbumPickerActivity.PhotoAlbumPickerActivityDelegate;
import org.telegram.ui.PhotoViewer.EmptyPhotoViewerProvider;
import org.telegram.ui.PhotoViewer.PhotoViewerProvider;
import org.telegram.ui.PhotoViewer.PlaceProviderObject;
import org.telegram.ui.StickerPreviewViewer.StickerPreviewViewerDelegate;

public class ChatActivity extends BaseFragment implements NotificationCenterDelegate, DialogsActivityDelegate, LocationActivityDelegate {
    private static final int add_shortcut = 24;
    private static final int attach_audio = 3;
    private static final int attach_contact = 5;
    private static final int attach_document = 4;
    private static final int attach_gallery = 1;
    private static final int attach_location = 6;
    private static final int attach_photo = 0;
    private static final int attach_video = 2;
    private static final int bot_help = 30;
    private static final int bot_settings = 31;
    private static final int call = 32;
    private static final int chat_enc_timer = 13;
    private static final int chat_menu_attach = 14;
    private static final int clear_history = 15;
    private static final int copy = 10;
    private static final int delete = 12;
    private static final int delete_chat = 16;
    private static final int edit = 23;
    private static final int forward = 11;
    private static final int id_chat_compose_panel = 1000;
    private static final int mute = 18;
    private static final int reply = 19;
    private static final int report = 21;
    private static final int search = 40;
    private static final int share_contact = 17;
    private static final int star = 22;
    private static final int text_bold = 50;
    private static final int text_italic = 51;
    private static final int text_link = 53;
    private static final int text_mono = 52;
    private static final int text_regular = 54;
    private ArrayList<View> actionModeViews = new ArrayList();
    private TextView addContactItem;
    private TextView addToContactsButton;
    private TextView alertNameTextView;
    private TextView alertTextView;
    private FrameLayout alertView;
    private AnimatorSet alertViewAnimator;
    private boolean allowContextBotPanel;
    private boolean allowContextBotPanelSecond = true;
    private boolean allowStickersPanel;
    private ArrayList<MessageObject> animatingMessageObjects = new ArrayList();
    private Paint aspectPaint;
    private Path aspectPath;
    private AspectRatioFrameLayout aspectRatioFrameLayout;
    private ActionBarMenuItem attachItem;
    private ChatAvatarContainer avatarContainer;
    private ChatBigEmptyView bigEmptyView;
    private MessageObject botButtons;
    private PhotoViewerProvider botContextProvider = new EmptyPhotoViewerProvider() {
        public PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, FileLocation fileLocation, int index) {
            PlaceProviderObject placeProviderObject = null;
            int i = 0;
            if (index >= 0 && index < ChatActivity.this.botContextResults.size()) {
                int count = ChatActivity.this.mentionListView.getChildCount();
                BotInlineResult result = ChatActivity.this.botContextResults.get(index);
                int a = 0;
                while (a < count) {
                    ImageReceiver imageReceiver = null;
                    View view = ChatActivity.this.mentionListView.getChildAt(a);
                    if (view instanceof ContextLinkCell) {
                        ContextLinkCell cell = (ContextLinkCell) view;
                        if (cell.getResult() == result) {
                            imageReceiver = cell.getPhotoImage();
                        }
                    }
                    if (imageReceiver != null) {
                        int[] coords = new int[2];
                        view.getLocationInWindow(coords);
                        placeProviderObject = new PlaceProviderObject();
                        placeProviderObject.viewX = coords[0];
                        int i2 = coords[1];
                        if (VERSION.SDK_INT < 21) {
                            i = AndroidUtilities.statusBarHeight;
                        }
                        placeProviderObject.viewY = i2 - i;
                        placeProviderObject.parentView = ChatActivity.this.mentionListView;
                        placeProviderObject.imageReceiver = imageReceiver;
                        placeProviderObject.thumb = imageReceiver.getBitmapSafe();
                        placeProviderObject.radius = imageReceiver.getRoundRadius();
                    } else {
                        a++;
                    }
                }
            }
            return placeProviderObject;
        }

        public void sendButtonPressed(int index, VideoEditedInfo videoEditedInfo) {
            if (index >= 0 && index < ChatActivity.this.botContextResults.size()) {
                ChatActivity.this.sendBotInlineResult((BotInlineResult) ChatActivity.this.botContextResults.get(index));
            }
        }
    };
    private ArrayList<Object> botContextResults;
    private SparseArray<BotInfo> botInfo = new SparseArray();
    private MessageObject botReplyButtons;
    private String botUser;
    private int botsCount;
    private FrameLayout bottomOverlay;
    private FrameLayout bottomOverlayChat;
    private TextView bottomOverlayChatText;
    private TextView bottomOverlayText;
    private boolean[] cacheEndReached = new boolean[2];
    private int canEditMessagesCount;
    private int cantDeleteMessagesCount;
    protected ChatActivityEnterView chatActivityEnterView;
    private ChatActivityAdapter chatAdapter;
    private ChatAttachAlert chatAttachAlert;
    private long chatEnterTime;
    private GridLayoutManagerFixed chatLayoutManager;
    private long chatLeaveTime;
    private RecyclerListView chatListView;
    private boolean chatListViewIgnoreLayout;
    private ArrayList<ChatMessageCell> chatMessageCellsCache = new ArrayList();
    private boolean checkTextureViewPosition;
    private Dialog closeChatDialog;
    private ImageView closePinned;
    private ImageView closeReportSpam;
    private SizeNotifierFrameLayout contentView;
    private int createUnreadMessageAfterId;
    private boolean createUnreadMessageAfterIdLoading;
    protected Chat currentChat;
    protected EncryptedChat currentEncryptedChat;
    private boolean currentFloatingDateOnScreen;
    private boolean currentFloatingTopIsNotMessage;
    private String currentPicturePath;
    protected User currentUser;
    private long dialog_id;
    private ChatMessageCell drawLaterRoundProgressCell;
    private int editTextEnd;
    private ActionBarMenuItem editTextItem;
    private int editTextStart;
    private MessageObject editingMessageObject;
    private int editingMessageObjectReqId;
    private View emojiButtonRed;
    private TextView emptyView;
    private FrameLayout emptyViewContainer;
    private boolean[] endReached = new boolean[2];
    private boolean first = true;
    private boolean firstLoading = true;
    boolean firstOpen = true;
    private boolean firstUnreadSent = false;
    private int first_unread_id;
    private boolean fixPaddingsInLayout;
    private AnimatorSet floatingDateAnimation;
    private ChatActionCell floatingDateView;
    private boolean forceScrollToTop;
    private boolean[] forwardEndReached = new boolean[]{true, true};
    private MessageObject forwardingMessage;
    private GroupedMessages forwardingMessageGroup;
    private ArrayList<MessageObject> forwardingMessages;
    private ArrayList<CharSequence> foundUrls;
    private WebPage foundWebPage;
    private FragmentContextView fragmentContextView;
    private FragmentContextView fragmentLocationContextView;
    private TextView gifHintTextView;
    private boolean globalIgnoreLayout;
    private LongSparseArray<GroupedMessages> groupedMessagesMap = new LongSparseArray();
    private boolean hasAllMentionsLocal;
    private boolean hasBotsCommands;
    private boolean hasUnfavedSelected;
    private ActionBarMenuItem headerItem;
    private Runnable hideAlertViewRunnable;
    private int highlightMessageId = ConnectionsManager.DEFAULT_DATACENTER_ID;
    private boolean ignoreAttachOnPause;
    protected ChatFull info;
    private long inlineReturn;
    private InstantCameraView instantCameraView;
    private boolean isBroadcast;
    private int lastLoadIndex;
    private int last_message_id = 0;
    private int linkSearchRequestId;
    private boolean loading;
    private boolean loadingForward;
    private boolean loadingFromOldPosition;
    private int loadingPinnedMessage;
    private int loadsCount;
    private boolean locationAlertShown;
    private int[] maxDate = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE};
    private int[] maxMessageId = new int[]{ConnectionsManager.DEFAULT_DATACENTER_ID, ConnectionsManager.DEFAULT_DATACENTER_ID};
    private TextView mediaBanTooltip;
    private FrameLayout mentionContainer;
    private ExtendedGridLayoutManager mentionGridLayoutManager;
    private LinearLayoutManager mentionLayoutManager;
    private AnimatorSet mentionListAnimation;
    private RecyclerListView mentionListView;
    private boolean mentionListViewIgnoreLayout;
    private boolean mentionListViewIsScrolling;
    private int mentionListViewLastViewPosition;
    private int mentionListViewLastViewTop;
    private int mentionListViewScrollOffsetY;
    private FrameLayout mentiondownButton;
    private ObjectAnimator mentiondownButtonAnimation;
    private TextView mentiondownButtonCounter;
    private ImageView mentiondownButtonImage;
    private MentionsAdapter mentionsAdapter;
    private OnItemClickListener mentionsOnItemClickListener;
    private long mergeDialogId;
    protected ArrayList<MessageObject> messages = new ArrayList();
    private HashMap<String, ArrayList<MessageObject>> messagesByDays = new HashMap();
    private SparseArray<MessageObject>[] messagesDict = new SparseArray[]{new SparseArray(), new SparseArray()};
    private int[] minDate = new int[2];
    private int[] minMessageId = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE};
    private TextView muteItem;
    private MessageObject needAnimateToMessage;
    private boolean needSelectFromMessageId;
    private int newMentionsCount;
    private int newUnreadMessageCount;
    OnItemClickListenerExtended onItemClickListener = new OnItemClickListenerExtended() {
        public void onItemClick(View view, int position, float x, float y) {
            ChatActivity.this.wasManualScroll = true;
            if (ChatActivity.this.actionBar.isActionModeShowed()) {
                boolean outside = false;
                if (view instanceof ChatMessageCell) {
                    if (((ChatMessageCell) view).isInsideBackground(x, y)) {
                        outside = false;
                    } else {
                        outside = true;
                    }
                }
                ChatActivity.this.processRowSelect(view, outside);
                return;
            }
            ChatActivity.this.createMenu(view, true, false);
        }
    };
    OnItemLongClickListenerExtended onItemLongClickListener = new OnItemLongClickListenerExtended() {
        public boolean onItemClick(View view, int position, float x, float y) {
            ChatActivity.this.wasManualScroll = true;
            if (ChatActivity.this.actionBar.isActionModeShowed()) {
                boolean outside = false;
                if (view instanceof ChatMessageCell) {
                    if (((ChatMessageCell) view).isInsideBackground(x, y)) {
                        outside = false;
                    } else {
                        outside = true;
                    }
                }
                ChatActivity.this.processRowSelect(view, outside);
            } else {
                ChatActivity.this.createMenu(view, false, true);
            }
            return true;
        }

        public void onLongClickRelease() {
        }

        public void onMove(float dx, float dy) {
        }
    };
    private boolean openAnimationEnded;
    private boolean openSearchKeyboard;
    private View overlayView;
    private FrameLayout pagedownButton;
    private AnimatorSet pagedownButtonAnimation;
    private TextView pagedownButtonCounter;
    private ImageView pagedownButtonImage;
    private boolean pagedownButtonShowedByScroll;
    private boolean paused = true;
    private boolean pausedOnLastMessage;
    private String pendingLinkSearchString;
    private Runnable pendingWebPageTimeoutRunnable;
    private PhotoViewerProvider photoViewerProvider = new EmptyPhotoViewerProvider() {
        public PlaceProviderObject getPlaceForPhoto(MessageObject messageObject, FileLocation fileLocation, int index) {
            int count = ChatActivity.this.chatListView.getChildCount();
            for (int a = 0; a < count; a++) {
                ImageReceiver imageReceiver = null;
                View view = ChatActivity.this.chatListView.getChildAt(a);
                MessageObject message;
                if (view instanceof ChatMessageCell) {
                    if (messageObject != null) {
                        ChatMessageCell cell = (ChatMessageCell) view;
                        message = cell.getMessageObject();
                        if (message != null && message.getId() == messageObject.getId()) {
                            imageReceiver = cell.getPhotoImage();
                        }
                    }
                } else if (view instanceof ChatActionCell) {
                    ChatActionCell cell2 = (ChatActionCell) view;
                    message = cell2.getMessageObject();
                    if (message != null) {
                        if (messageObject != null) {
                            if (message.getId() == messageObject.getId()) {
                                imageReceiver = cell2.getPhotoImage();
                            }
                        } else if (fileLocation != null && message.photoThumbs != null) {
                            for (int b = 0; b < message.photoThumbs.size(); b++) {
                                PhotoSize photoSize = (PhotoSize) message.photoThumbs.get(b);
                                if (photoSize.location.volume_id == fileLocation.volume_id && photoSize.location.local_id == fileLocation.local_id) {
                                    imageReceiver = cell2.getPhotoImage();
                                    break;
                                }
                            }
                        }
                    }
                }
                if (imageReceiver != null) {
                    int[] coords = new int[2];
                    view.getLocationInWindow(coords);
                    PlaceProviderObject object = new PlaceProviderObject();
                    object.viewX = coords[0];
                    object.viewY = coords[1] - (VERSION.SDK_INT >= 21 ? 0 : AndroidUtilities.statusBarHeight);
                    object.parentView = ChatActivity.this.chatListView;
                    object.imageReceiver = imageReceiver;
                    object.thumb = imageReceiver.getBitmapSafe();
                    object.radius = imageReceiver.getRoundRadius();
                    if ((view instanceof ChatActionCell) && ChatActivity.this.currentChat != null) {
                        object.dialogId = -ChatActivity.this.currentChat.id;
                    }
                    if ((ChatActivity.this.pinnedMessageView == null || ChatActivity.this.pinnedMessageView.getTag() != null) && (ChatActivity.this.reportSpamView == null || ChatActivity.this.reportSpamView.getTag() != null)) {
                        return object;
                    }
                    object.clipTopAddition = AndroidUtilities.dp(48.0f);
                    return object;
                }
            }
            return null;
        }
    };
    private FileLocation pinnedImageLocation;
    private View pinnedLineView;
    private BackupImageView pinnedMessageImageView;
    private SimpleTextView pinnedMessageNameTextView;
    private MessageObject pinnedMessageObject;
    private SimpleTextView pinnedMessageTextView;
    private FrameLayout pinnedMessageView;
    private AnimatorSet pinnedMessageViewAnimator;
    private int prevSetUnreadCount = Integer.MIN_VALUE;
    private RadialProgressView progressBar;
    private FrameLayout progressView;
    private View progressView2;
    private AnimatorSet replyButtonAnimation;
    private ImageView replyCloseImageView;
    private ImageView replyIconImageView;
    private FileLocation replyImageLocation;
    private BackupImageView replyImageView;
    private View replyLineView;
    private SimpleTextView replyNameTextView;
    private SimpleTextView replyObjectTextView;
    private MessageObject replyingMessageObject;
    private TextView reportSpamButton;
    private FrameLayout reportSpamContainer;
    private LinearLayout reportSpamView;
    private AnimatorSet reportSpamViewAnimator;
    private int returnToLoadIndex;
    private int returnToMessageId;
    private FrameLayout roundVideoContainer;
    private AnimatorSet runningAnimation;
    private MessageObject scrollToMessage;
    private int scrollToMessagePosition = -10000;
    private int scrollToOffsetOnRecreate = 0;
    private int scrollToPositionOnRecreate = -1;
    private boolean scrollToTopOnResume;
    private boolean scrollToTopUnReadOnResume;
    private boolean scrollingFloatingDate;
    private ImageView searchCalendarButton;
    private FrameLayout searchContainer;
    private SimpleTextView searchCountText;
    private ImageView searchDownButton;
    private ActionBarMenuItem searchItem;
    private ImageView searchUpButton;
    private ImageView searchUserButton;
    private boolean searchingForUser;
    private User searchingUserMessages;
    private SparseArray<MessageObject>[] selectedMessagesCanCopyIds = new SparseArray[]{new SparseArray(), new SparseArray()};
    private SparseArray<MessageObject>[] selectedMessagesCanStarIds = new SparseArray[]{new SparseArray(), new SparseArray()};
    private NumberTextView selectedMessagesCountTextView;
    private SparseArray<MessageObject>[] selectedMessagesIds = new SparseArray[]{new SparseArray(), new SparseArray()};
    private MessageObject selectedObject;
    private GroupedMessages selectedObjectGroup;
    private int startLoadFromMessageId;
    private int startLoadFromMessageOffset = ConnectionsManager.DEFAULT_DATACENTER_ID;
    private boolean startReplyOnTextChange;
    private String startVideoEdit;
    private StickersAdapter stickersAdapter;
    private RecyclerListView stickersListView;
    private OnItemClickListener stickersOnItemClickListener;
    private FrameLayout stickersPanel;
    private ImageView stickersPanelArrow;
    private View timeItem2;
    private int topViewWasVisible;
    private MessageObject unreadMessageObject;
    private boolean userBlocked = false;
    private TextureView videoTextureView;
    private AnimatorSet voiceHintAnimation;
    private Runnable voiceHintHideRunnable;
    private TextView voiceHintTextView;
    private Runnable waitingForCharaterEnterRunnable;
    private ArrayList<Integer> waitingForLoad = new ArrayList();
    private boolean waitingForReplyMessageLoad;
    private boolean wasManualScroll;
    private boolean wasPaused;

    public class ChatActivityAdapter extends Adapter {
        private int botInfoRow = -1;
        private boolean isBot;
        private int loadingDownRow;
        private int loadingUpRow;
        private Context mContext;
        private int messagesEndRow;
        private int messagesStartRow;
        private int rowCount;

        public ChatActivityAdapter(Context context) {
            this.mContext = context;
            boolean z = ChatActivity.this.currentUser != null && ChatActivity.this.currentUser.bot;
            this.isBot = z;
        }

        public void updateRows() {
            int i;
            this.rowCount = 0;
            if (ChatActivity.this.messages.isEmpty()) {
                this.loadingUpRow = -1;
                this.loadingDownRow = -1;
                this.messagesStartRow = -1;
                this.messagesEndRow = -1;
            } else {
                if (ChatActivity.this.forwardEndReached[0] && (ChatActivity.this.mergeDialogId == 0 || ChatActivity.this.forwardEndReached[1])) {
                    this.loadingDownRow = -1;
                } else {
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.loadingDownRow = i;
                }
                this.messagesStartRow = this.rowCount;
                this.rowCount += ChatActivity.this.messages.size();
                this.messagesEndRow = this.rowCount;
                if (ChatActivity.this.endReached[0] && (ChatActivity.this.mergeDialogId == 0 || ChatActivity.this.endReached[1])) {
                    this.loadingUpRow = -1;
                } else {
                    i = this.rowCount;
                    this.rowCount = i + 1;
                    this.loadingUpRow = i;
                }
            }
            if (ChatActivity.this.currentUser == null || !ChatActivity.this.currentUser.bot) {
                this.botInfoRow = -1;
                return;
            }
            i = this.rowCount;
            this.rowCount = i + 1;
            this.botInfoRow = i;
        }

        public int getItemCount() {
            return this.rowCount;
        }

        public long getItemId(int i) {
            return -1;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == 0) {
                if (ChatActivity.this.chatMessageCellsCache.isEmpty()) {
                    view = new ChatMessageCell(this.mContext);
                } else {
                    view = (View) ChatActivity.this.chatMessageCellsCache.get(0);
                    ChatActivity.this.chatMessageCellsCache.remove(0);
                }
                ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                chatMessageCell.setDelegate(new ChatMessageCellDelegate() {
                    public void didPressedShare(ChatMessageCell cell) {
                        if (ChatActivity.this.getParentActivity() != null) {
                            if (ChatActivity.this.chatActivityEnterView != null) {
                                ChatActivity.this.chatActivityEnterView.closeKeyboard();
                            }
                            MessageObject messageObject = cell.getMessageObject();
                            if (!UserObject.isUserSelf(ChatActivity.this.currentUser) || messageObject.messageOwner.fwd_from.saved_from_peer == null) {
                                ArrayList<MessageObject> arrayList = null;
                                if (messageObject.getGroupId() != 0) {
                                    GroupedMessages groupedMessages = (GroupedMessages) ChatActivity.this.groupedMessagesMap.get(messageObject.getGroupId());
                                    if (groupedMessages != null) {
                                        arrayList = groupedMessages.messages;
                                    }
                                }
                                if (arrayList == null) {
                                    arrayList = new ArrayList();
                                    arrayList.add(messageObject);
                                }
                                ChatActivity chatActivity = ChatActivity.this;
                                Context access$20500 = ChatActivityAdapter.this.mContext;
                                boolean z = ChatObject.isChannel(ChatActivity.this.currentChat) && !ChatActivity.this.currentChat.megagroup && ChatActivity.this.currentChat.username != null && ChatActivity.this.currentChat.username.length() > 0;
                                chatActivity.showDialog(new ShareAlert(access$20500, arrayList, null, z, null, false));
                                return;
                            }
                            Bundle args = new Bundle();
                            if (messageObject.messageOwner.fwd_from.saved_from_peer.channel_id != 0) {
                                args.putInt("chat_id", messageObject.messageOwner.fwd_from.saved_from_peer.channel_id);
                            } else if (messageObject.messageOwner.fwd_from.saved_from_peer.chat_id != 0) {
                                args.putInt("chat_id", messageObject.messageOwner.fwd_from.saved_from_peer.chat_id);
                            } else if (messageObject.messageOwner.fwd_from.saved_from_peer.user_id != 0) {
                                args.putInt("user_id", messageObject.messageOwner.fwd_from.saved_from_peer.user_id);
                            }
                            args.putInt("message_id", messageObject.messageOwner.fwd_from.saved_from_msg_id);
                            if (MessagesController.getInstance(ChatActivity.this.currentAccount).checkCanOpenChat(args, ChatActivity.this)) {
                                ChatActivity.this.presentFragment(new ChatActivity(args));
                            }
                        }
                    }

                    public boolean needPlayMessage(MessageObject messageObject) {
                        if (!messageObject.isVoice() && !messageObject.isRoundVideo()) {
                            return messageObject.isMusic() ? MediaController.getInstance().setPlaylist(ChatActivity.this.messages, messageObject) : false;
                        } else {
                            boolean result = MediaController.getInstance().playMessage(messageObject);
                            MediaController.getInstance().setVoiceMessagesPlaylist(result ? ChatActivity.this.createVoiceMessagesPlaylist(messageObject, false) : null, false);
                            return result;
                        }
                    }

                    public void didPressedChannelAvatar(ChatMessageCell cell, Chat chat, int postId) {
                        if (ChatActivity.this.actionBar.isActionModeShowed()) {
                            ChatActivity.this.processRowSelect(cell, true);
                        } else if (chat != null && chat != ChatActivity.this.currentChat) {
                            Bundle args = new Bundle();
                            args.putInt("chat_id", chat.id);
                            if (postId != 0) {
                                args.putInt("message_id", postId);
                            }
                            if (MessagesController.getInstance(ChatActivity.this.currentAccount).checkCanOpenChat(args, ChatActivity.this, cell.getMessageObject())) {
                                ChatActivity.this.presentFragment(new ChatActivity(args), true);
                            }
                        }
                    }

                    public void didPressedOther(ChatMessageCell cell) {
                        if (cell.getMessageObject().type != 16) {
                            ChatActivity.this.createMenu(cell, true, false, false);
                        } else if (ChatActivity.this.currentUser != null) {
                            VoIPHelper.startCall(ChatActivity.this.currentUser, ChatActivity.this.getParentActivity(), MessagesController.getInstance(ChatActivity.this.currentAccount).getUserFull(ChatActivity.this.currentUser.id));
                        }
                    }

                    public void didPressedUserAvatar(ChatMessageCell cell, User user) {
                        boolean z = true;
                        if (ChatActivity.this.actionBar.isActionModeShowed()) {
                            ChatActivity.this.processRowSelect(cell, true);
                        } else if (user != null && user.id != UserConfig.getInstance(ChatActivity.this.currentAccount).getClientUserId()) {
                            Bundle args = new Bundle();
                            args.putInt("user_id", user.id);
                            ProfileActivity fragment = new ProfileActivity(args);
                            if (ChatActivity.this.currentUser == null || ChatActivity.this.currentUser.id != user.id) {
                                z = false;
                            }
                            fragment.setPlayProfileAnimation(z);
                            ChatActivity.this.presentFragment(fragment);
                        }
                    }

                    public void didPressedBotButton(ChatMessageCell cell, KeyboardButton button) {
                        if (ChatActivity.this.getParentActivity() == null) {
                            return;
                        }
                        if (ChatActivity.this.bottomOverlayChat.getVisibility() != 0 || (button instanceof TL_keyboardButtonSwitchInline) || (button instanceof TL_keyboardButtonCallback) || (button instanceof TL_keyboardButtonGame) || (button instanceof TL_keyboardButtonUrl) || (button instanceof TL_keyboardButtonBuy)) {
                            ChatActivity.this.chatActivityEnterView.didPressedBotButton(button, cell.getMessageObject(), cell.getMessageObject());
                        }
                    }

                    public void didPressedCancelSendButton(ChatMessageCell cell) {
                        MessageObject message = cell.getMessageObject();
                        if (message.messageOwner.send_state != 0) {
                            SendMessagesHelper.getInstance(ChatActivity.this.currentAccount).cancelSendingMessage(message);
                        }
                    }

                    public void didLongPressed(ChatMessageCell cell) {
                        ChatActivity.this.createMenu(cell, false, false);
                    }

                    public boolean canPerformActions() {
                        return (ChatActivity.this.actionBar == null || ChatActivity.this.actionBar.isActionModeShowed()) ? false : true;
                    }

                    public void didPressedUrl(MessageObject messageObject, CharacterStyle url, boolean longPress) {
                        if (url != null) {
                            if (url instanceof URLSpanMono) {
                                ((URLSpanMono) url).copyToClipboard();
                                Toast.makeText(ChatActivity.this.getParentActivity(), LocaleController.getString("TextCopied", R.string.TextCopied), 0).show();
                            } else if (url instanceof URLSpanUserMention) {
                                User user = MessagesController.getInstance(ChatActivity.this.currentAccount).getUser(Utilities.parseInt(((URLSpanUserMention) url).getURL()));
                                if (user != null) {
                                    MessagesController.openChatOrProfileWith(user, null, ChatActivity.this, 0, false);
                                }
                            } else if (url instanceof URLSpanNoUnderline) {
                                String str = ((URLSpanNoUnderline) url).getURL();
                                if (str.startsWith("@")) {
                                    MessagesController.getInstance(ChatActivity.this.currentAccount).openByUserName(str.substring(1), ChatActivity.this, 0);
                                } else if (str.startsWith("#") || str.startsWith("$")) {
                                    if (ChatObject.isChannel(ChatActivity.this.currentChat)) {
                                        ChatActivity.this.openSearchWithText(str);
                                        return;
                                    }
                                    DialogsActivity fragment = new DialogsActivity(null);
                                    fragment.setSearchString(str);
                                    ChatActivity.this.presentFragment(fragment);
                                } else if (str.startsWith("/") && URLSpanBotCommand.enabled) {
                                    ChatActivityEnterView chatActivityEnterView = ChatActivity.this.chatActivityEnterView;
                                    boolean z = ChatActivity.this.currentChat != null && ChatActivity.this.currentChat.megagroup;
                                    chatActivityEnterView.setCommand(messageObject, str, longPress, z);
                                    if (!longPress && ChatActivity.this.chatActivityEnterView.getFieldText() == null) {
                                        ChatActivity.this.hideFieldPanel();
                                    }
                                }
                            } else {
                                String urlFinal = ((URLSpan) url).getURL();
                                if (longPress) {
                                    Builder builder = new Builder(ChatActivity.this.getParentActivity());
                                    builder.setTitle(urlFinal);
                                    builder.setItems(new CharSequence[]{LocaleController.getString("Open", R.string.Open), LocaleController.getString("Copy", R.string.Copy)}, new ChatActivity$ChatActivityAdapter$1$$Lambda$0(this, urlFinal));
                                    ChatActivity.this.showDialog(builder.create());
                                } else if ((url instanceof URLSpanReplacement) && (urlFinal == null || !urlFinal.startsWith("mailto:"))) {
                                    ChatActivity.this.showOpenUrlAlert(urlFinal, true);
                                } else if (url instanceof URLSpan) {
                                    if (!(!(messageObject.messageOwner.media instanceof TL_messageMediaWebPage) || messageObject.messageOwner.media.webpage == null || messageObject.messageOwner.media.webpage.cached_page == null)) {
                                        String lowerUrl = urlFinal.toLowerCase();
                                        String lowerUrl2 = messageObject.messageOwner.media.webpage.url.toLowerCase();
                                        if ((lowerUrl.contains("telegra.ph") || lowerUrl.contains("t.me/iv")) && (lowerUrl.contains(lowerUrl2) || lowerUrl2.contains(lowerUrl))) {
                                            ArticleViewer.getInstance().setParentActivity(ChatActivity.this.getParentActivity(), ChatActivity.this);
                                            ArticleViewer.getInstance().open(messageObject);
                                            return;
                                        }
                                    }
                                    Browser.openUrl(ChatActivity.this.getParentActivity(), urlFinal, ChatActivity.this.inlineReturn == 0);
                                } else if (url instanceof ClickableSpan) {
                                    ((ClickableSpan) url).onClick(ChatActivity.this.fragmentView);
                                }
                            }
                        }
                    }

                    final /* synthetic */ void lambda$didPressedUrl$0$ChatActivity$ChatActivityAdapter$1(String urlFinal, DialogInterface dialog, int which) {
                        boolean z = true;
                        if (which == 0) {
                            Context parentActivity = ChatActivity.this.getParentActivity();
                            if (ChatActivity.this.inlineReturn != 0) {
                                z = false;
                            }
                            Browser.openUrl(parentActivity, urlFinal, z, false);
                        } else if (which == 1) {
                            String url1 = urlFinal;
                            if (url1.startsWith("mailto:")) {
                                url1 = url1.substring(7);
                            } else if (url1.startsWith("tel:")) {
                                url1 = url1.substring(4);
                            }
                            AndroidUtilities.addToClipboard(url1);
                        }
                    }

                    public void needOpenWebView(String url, String title, String description, String originalUrl, int w, int h) {
                        try {
                            EmbedBottomSheet.show(ChatActivityAdapter.this.mContext, title, description, originalUrl, url, w, h);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }

                    public void didPressedReplyMessage(ChatMessageCell cell, int id) {
                        int i;
                        MessageObject messageObject = cell.getMessageObject();
                        ChatActivity chatActivity = ChatActivity.this;
                        int id2 = messageObject.getId();
                        if (messageObject.getDialogId() == ChatActivity.this.mergeDialogId) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        chatActivity.scrollToMessageId(id, id2, true, i, false);
                    }

                    public void didPressedViaBot(ChatMessageCell cell, String username) {
                        if (ChatActivity.this.bottomOverlayChat != null && ChatActivity.this.bottomOverlayChat.getVisibility() == 0) {
                            return;
                        }
                        if ((ChatActivity.this.bottomOverlay == null || ChatActivity.this.bottomOverlay.getVisibility() != 0) && ChatActivity.this.chatActivityEnterView != null && username != null && username.length() > 0) {
                            ChatActivity.this.chatActivityEnterView.setFieldText("@" + username + " ");
                            ChatActivity.this.chatActivityEnterView.openKeyboard();
                        }
                    }

                    public void didPressedImage(ChatMessageCell cell) {
                        MessageObject message = cell.getMessageObject();
                        if (message.isSendError()) {
                            ChatActivity.this.createMenu(cell, false, false);
                        } else if (!message.isSending()) {
                            if (message.needDrawBluredPreview()) {
                                if (ChatActivity.this.sendSecretMessageRead(message)) {
                                    cell.invalidate();
                                }
                                SecretMediaViewer.getInstance().setParentActivity(ChatActivity.this.getParentActivity());
                                SecretMediaViewer.getInstance().openMedia(message, ChatActivity.this.photoViewerProvider);
                            } else if (message.type == 13) {
                                ChatActivity chatActivity = ChatActivity.this;
                                Context parentActivity = ChatActivity.this.getParentActivity();
                                BaseFragment baseFragment = ChatActivity.this;
                                InputStickerSet inputStickerSet = message.getInputStickerSet();
                                StickersAlertDelegate stickersAlertDelegate = (ChatActivity.this.bottomOverlayChat.getVisibility() == 0 || !ChatObject.canSendStickers(ChatActivity.this.currentChat)) ? null : ChatActivity.this.chatActivityEnterView;
                                chatActivity.showDialog(new StickersAlert(parentActivity, baseFragment, inputStickerSet, null, stickersAlertDelegate));
                            } else if (message.isVideo() || message.type == 1 || ((message.type == 0 && !message.isWebpageDocument()) || message.isGif())) {
                                if (message.isVideo()) {
                                    ChatActivity.this.sendSecretMessageRead(message);
                                }
                                PhotoViewer.getInstance().setParentActivity(ChatActivity.this.getParentActivity());
                                if (PhotoViewer.getInstance().openPhoto(message, message.type != 0 ? ChatActivity.this.dialog_id : 0, message.type != 0 ? ChatActivity.this.mergeDialogId : 0, ChatActivity.this.photoViewerProvider)) {
                                    PhotoViewer.getInstance().setParentChatActivity(ChatActivity.this);
                                }
                            } else if (message.type == 3) {
                                ChatActivity.this.sendSecretMessageRead(message);
                                f = null;
                                try {
                                    if (!(message.messageOwner.attachPath == null || message.messageOwner.attachPath.length() == 0)) {
                                        f = new File(message.messageOwner.attachPath);
                                    }
                                    if (f == null || !f.exists()) {
                                        f = FileLoader.getPathToMessage(message.messageOwner);
                                    }
                                    Intent intent = new Intent("android.intent.action.VIEW");
                                    if (VERSION.SDK_INT >= 24) {
                                        intent.setFlags(1);
                                        intent.setDataAndType(FileProvider.getUriForFile(ChatActivity.this.getParentActivity(), "org.telegram.messenger.beta.provider", f), MimeTypes.VIDEO_MP4);
                                    } else {
                                        intent.setDataAndType(Uri.fromFile(f), MimeTypes.VIDEO_MP4);
                                    }
                                    ChatActivity.this.getParentActivity().startActivityForResult(intent, 500);
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                    ChatActivity.this.alertUserOpenError(message);
                                }
                            } else if (message.type == 4) {
                                if (!AndroidUtilities.isGoogleMapsInstalled(ChatActivity.this)) {
                                    return;
                                }
                                LocationActivity fragment;
                                if (message.isLiveLocation()) {
                                    fragment = new LocationActivity(2);
                                    fragment.setMessageObject(message);
                                    fragment.setDelegate(ChatActivity.this);
                                    ChatActivity.this.presentFragment(fragment);
                                    return;
                                }
                                fragment = new LocationActivity(ChatActivity.this.currentEncryptedChat == null ? 3 : 0);
                                fragment.setMessageObject(message);
                                fragment.setDelegate(ChatActivity.this);
                                ChatActivity.this.presentFragment(fragment);
                            } else if (message.type == 9 || message.type == 0) {
                                if (message.getDocumentName().toLowerCase().endsWith("attheme")) {
                                    File locFile = null;
                                    if (!(message.messageOwner.attachPath == null || message.messageOwner.attachPath.length() == 0)) {
                                        f = new File(message.messageOwner.attachPath);
                                        if (f.exists()) {
                                            locFile = f;
                                        }
                                    }
                                    if (locFile == null) {
                                        f = FileLoader.getPathToMessage(message.messageOwner);
                                        if (f.exists()) {
                                            locFile = f;
                                        }
                                    }
                                    if (ChatActivity.this.chatLayoutManager != null) {
                                        int lastPosition = ChatActivity.this.chatLayoutManager.findFirstVisibleItemPosition();
                                        if (lastPosition != 0) {
                                            ChatActivity.this.scrollToPositionOnRecreate = lastPosition;
                                            Holder holder = (Holder) ChatActivity.this.chatListView.findViewHolderForAdapterPosition(ChatActivity.this.scrollToPositionOnRecreate);
                                            if (holder != null) {
                                                ChatActivity.this.scrollToOffsetOnRecreate = (ChatActivity.this.chatListView.getMeasuredHeight() - holder.itemView.getBottom()) - ChatActivity.this.chatListView.getPaddingBottom();
                                            } else {
                                                ChatActivity.this.scrollToPositionOnRecreate = -1;
                                            }
                                        } else {
                                            ChatActivity.this.scrollToPositionOnRecreate = -1;
                                        }
                                    }
                                    ThemeInfo themeInfo = Theme.applyThemeFile(locFile, message.getDocumentName(), true);
                                    if (themeInfo != null) {
                                        ChatActivity.this.presentFragment(new ThemePreviewActivity(locFile, themeInfo));
                                        return;
                                    }
                                    ChatActivity.this.scrollToPositionOnRecreate = -1;
                                }
                                try {
                                    AndroidUtilities.openForView(message, ChatActivity.this.getParentActivity());
                                } catch (Throwable e2) {
                                    FileLog.e(e2);
                                    ChatActivity.this.alertUserOpenError(message);
                                }
                            }
                        }
                    }

                    public void didPressedInstantButton(ChatMessageCell cell, int type) {
                        MessageObject messageObject = cell.getMessageObject();
                        if (type == 0) {
                            if (messageObject.messageOwner.media != null && messageObject.messageOwner.media.webpage != null && messageObject.messageOwner.media.webpage.cached_page != null) {
                                ArticleViewer.getInstance().setParentActivity(ChatActivity.this.getParentActivity(), ChatActivity.this);
                                ArticleViewer.getInstance().open(messageObject);
                            }
                        } else if (type == 5) {
                            ChatActivity.this.openVCard(messageObject.messageOwner.media.vcard, messageObject.messageOwner.media.first_name, messageObject.messageOwner.media.last_name);
                        } else if (messageObject.messageOwner.media != null && messageObject.messageOwner.media.webpage != null) {
                            Browser.openUrl(ChatActivity.this.getParentActivity(), messageObject.messageOwner.media.webpage.url);
                        }
                    }

                    public boolean isChatAdminCell(int uid) {
                        if (ChatObject.isChannel(ChatActivity.this.currentChat) && ChatActivity.this.currentChat.megagroup) {
                            return MessagesController.getInstance(ChatActivity.this.currentAccount).isChannelAdmin(ChatActivity.this.currentChat.id, uid);
                        }
                        return false;
                    }
                });
                if (ChatActivity.this.currentEncryptedChat == null) {
                    chatMessageCell.setAllowAssistant(true);
                }
            } else if (viewType == 1) {
                view = new ChatActionCell(this.mContext);
                ((ChatActionCell) view).setDelegate(new ChatActionCellDelegate() {
                    public void didClickedImage(ChatActionCell cell) {
                        MessageObject message = cell.getMessageObject();
                        PhotoViewer.getInstance().setParentActivity(ChatActivity.this.getParentActivity());
                        PhotoSize photoSize = FileLoader.getClosestPhotoSizeWithSize(message.photoThumbs, 640);
                        if (photoSize != null) {
                            PhotoViewer.getInstance().openPhoto(photoSize.location, ChatActivity.this.photoViewerProvider);
                            return;
                        }
                        PhotoViewer.getInstance().openPhoto(message, 0, 0, ChatActivity.this.photoViewerProvider);
                    }

                    public void didLongPressed(ChatActionCell cell) {
                        ChatActivity.this.createMenu(cell, false, false);
                    }

                    public void needOpenUserProfile(int uid) {
                        boolean z = true;
                        Bundle args;
                        if (uid < 0) {
                            args = new Bundle();
                            args.putInt("chat_id", -uid);
                            if (MessagesController.getInstance(ChatActivity.this.currentAccount).checkCanOpenChat(args, ChatActivity.this)) {
                                ChatActivity.this.presentFragment(new ChatActivity(args), true);
                            }
                        } else if (uid != UserConfig.getInstance(ChatActivity.this.currentAccount).getClientUserId()) {
                            args = new Bundle();
                            args.putInt("user_id", uid);
                            if (ChatActivity.this.currentEncryptedChat != null && uid == ChatActivity.this.currentUser.id) {
                                args.putLong("dialog_id", ChatActivity.this.dialog_id);
                            }
                            ProfileActivity fragment = new ProfileActivity(args);
                            if (ChatActivity.this.currentUser == null || ChatActivity.this.currentUser.id != uid) {
                                z = false;
                            }
                            fragment.setPlayProfileAnimation(z);
                            ChatActivity.this.presentFragment(fragment);
                        }
                    }

                    public void didPressedReplyMessage(ChatActionCell cell, int id) {
                        int i;
                        MessageObject messageObject = cell.getMessageObject();
                        ChatActivity chatActivity = ChatActivity.this;
                        int id2 = messageObject.getId();
                        if (messageObject.getDialogId() == ChatActivity.this.mergeDialogId) {
                            i = 1;
                        } else {
                            i = 0;
                        }
                        chatActivity.scrollToMessageId(id, id2, true, i, false);
                    }

                    public void didPressedBotButton(MessageObject messageObject, KeyboardButton button) {
                        if (ChatActivity.this.getParentActivity() == null) {
                            return;
                        }
                        if (ChatActivity.this.bottomOverlayChat.getVisibility() != 0 || (button instanceof TL_keyboardButtonSwitchInline) || (button instanceof TL_keyboardButtonCallback) || (button instanceof TL_keyboardButtonGame) || (button instanceof TL_keyboardButtonUrl) || (button instanceof TL_keyboardButtonBuy)) {
                            ChatActivity.this.chatActivityEnterView.didPressedBotButton(button, messageObject, messageObject);
                        }
                    }
                });
            } else if (viewType == 2) {
                view = new ChatUnreadCell(this.mContext);
            } else if (viewType == 3) {
                view = new BotHelpCell(this.mContext);
                ((BotHelpCell) view).setDelegate(new ChatActivity$ChatActivityAdapter$$Lambda$0(this));
            } else if (viewType == 4) {
                view = new ChatLoadingCell(this.mContext);
            }
            view.setLayoutParams(new LayoutParams(-1, -2));
            return new Holder(view);
        }

        final /* synthetic */ void lambda$onCreateViewHolder$0$ChatActivity$ChatActivityAdapter(String url) {
            if (url.startsWith("@")) {
                MessagesController.getInstance(ChatActivity.this.currentAccount).openByUserName(url.substring(1), ChatActivity.this, 0);
            } else if (url.startsWith("#") || url.startsWith("$")) {
                DialogsActivity fragment = new DialogsActivity(null);
                fragment.setSearchString(url);
                ChatActivity.this.presentFragment(fragment);
            } else if (url.startsWith("/")) {
                ChatActivity.this.chatActivityEnterView.setCommand(null, url, false, false);
                if (ChatActivity.this.chatActivityEnterView.getFieldText() == null) {
                    ChatActivity.this.hideFieldPanel();
                }
            }
        }

        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position == this.botInfoRow) {
                String str;
                BotHelpCell helpView = holder.itemView;
                if (ChatActivity.this.botInfo.size() != 0) {
                    str = ((BotInfo) ChatActivity.this.botInfo.get(ChatActivity.this.currentUser.id)).description;
                } else {
                    str = null;
                }
                helpView.setText(str);
                return;
            }
            if (position == this.loadingDownRow || position == this.loadingUpRow) {
                holder.itemView.setProgressVisible(ChatActivity.this.loadsCount > 1);
                return;
            }
            if (position >= this.messagesStartRow && position < this.messagesEndRow) {
                MessageObject message = (MessageObject) ChatActivity.this.messages.get(position - this.messagesStartRow);
                View view = holder.itemView;
                if (view instanceof ChatMessageCell) {
                    int prevPosition;
                    int nextPosition;
                    int index;
                    final ChatMessageCell messageCell = (ChatMessageCell) view;
                    boolean z = ChatActivity.this.currentChat != null || UserObject.isUserSelf(ChatActivity.this.currentUser);
                    messageCell.isChat = z;
                    boolean pinnedBottom = false;
                    boolean pinnedTop = false;
                    GroupedMessages groupedMessages = ChatActivity.this.getValidGroupedMessage(message);
                    if (groupedMessages != null) {
                        GroupedMessagePosition pos = (GroupedMessagePosition) groupedMessages.positions.get(message);
                        if (pos != null) {
                            if ((pos.flags & 4) != 0) {
                                prevPosition = (groupedMessages.posArray.indexOf(pos) + position) + 1;
                            } else {
                                pinnedTop = true;
                                prevPosition = -100;
                            }
                            if ((pos.flags & 8) != 0) {
                                nextPosition = (position - groupedMessages.posArray.size()) + groupedMessages.posArray.indexOf(pos);
                            } else {
                                pinnedBottom = true;
                                nextPosition = -100;
                            }
                        } else {
                            prevPosition = -100;
                            nextPosition = -100;
                        }
                    } else {
                        nextPosition = position - 1;
                        prevPosition = position + 1;
                    }
                    int nextType = getItemViewType(nextPosition);
                    int prevType = getItemViewType(prevPosition);
                    if (!(message.messageOwner.reply_markup instanceof TL_replyInlineMarkup) && nextType == holder.getItemViewType()) {
                        MessageObject nextMessage = (MessageObject) ChatActivity.this.messages.get(nextPosition - this.messagesStartRow);
                        pinnedBottom = nextMessage.isOutOwner() == message.isOutOwner() && Math.abs(nextMessage.messageOwner.date - message.messageOwner.date) <= 300;
                        if (pinnedBottom) {
                            if (ChatActivity.this.currentChat != null) {
                                pinnedBottom = nextMessage.messageOwner.from_id == message.messageOwner.from_id;
                            } else if (UserObject.isUserSelf(ChatActivity.this.currentUser)) {
                                pinnedBottom = nextMessage.getFromId() == message.getFromId();
                            }
                        }
                    }
                    if (prevType == holder.getItemViewType()) {
                        MessageObject prevMessage = (MessageObject) ChatActivity.this.messages.get(prevPosition - this.messagesStartRow);
                        pinnedTop = !(prevMessage.messageOwner.reply_markup instanceof TL_replyInlineMarkup) && prevMessage.isOutOwner() == message.isOutOwner() && Math.abs(prevMessage.messageOwner.date - message.messageOwner.date) <= 300;
                        if (pinnedTop) {
                            if (ChatActivity.this.currentChat != null) {
                                pinnedTop = prevMessage.messageOwner.from_id == message.messageOwner.from_id;
                            } else if (UserObject.isUserSelf(ChatActivity.this.currentUser)) {
                                pinnedTop = prevMessage.getFromId() == message.getFromId();
                            }
                        }
                    }
                    messageCell.setMessageObject(message, groupedMessages, pinnedBottom, pinnedTop);
                    if ((view instanceof ChatMessageCell) && DownloadController.getInstance(ChatActivity.this.currentAccount).canDownloadMedia(message)) {
                        ((ChatMessageCell) view).downloadAudioIfNeed();
                    }
                    z = ChatActivity.this.highlightMessageId != Integer.MAX_VALUE && message.getId() == ChatActivity.this.highlightMessageId;
                    messageCell.setHighlighted(z);
                    if (ChatActivity.this.searchContainer != null && ChatActivity.this.searchContainer.getVisibility() == 0) {
                        if (DataQuery.getInstance(ChatActivity.this.currentAccount).isMessageFound(message.getId(), message.getDialogId() == ChatActivity.this.mergeDialogId) && DataQuery.getInstance(ChatActivity.this.currentAccount).getLastSearchQuery() != null) {
                            messageCell.setHighlightedText(DataQuery.getInstance(ChatActivity.this.currentAccount).getLastSearchQuery());
                            index = ChatActivity.this.animatingMessageObjects.indexOf(message);
                            if (index != -1) {
                                ChatActivity.this.animatingMessageObjects.remove(index);
                                messageCell.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                                    public boolean onPreDraw() {
                                        PipRoundVideoView pipRoundVideoView = PipRoundVideoView.getInstance();
                                        if (pipRoundVideoView != null) {
                                            pipRoundVideoView.showTemporary(true);
                                        }
                                        messageCell.getViewTreeObserver().removeOnPreDrawListener(this);
                                        ImageReceiver imageReceiver = messageCell.getPhotoImage();
                                        float scale = ((float) imageReceiver.getImageWidth()) / ChatActivity.this.instantCameraView.getCameraRect().width;
                                        int[] position = new int[2];
                                        messageCell.setAlpha(0.0f);
                                        messageCell.getLocationOnScreen(position);
                                        position[0] = position[0] + imageReceiver.getImageX();
                                        position[1] = position[1] + imageReceiver.getImageY();
                                        final View cameraContainer = ChatActivity.this.instantCameraView.getCameraContainer();
                                        cameraContainer.setPivotX(0.0f);
                                        cameraContainer.setPivotY(0.0f);
                                        AnimatorSet animatorSet = new AnimatorSet();
                                        r8 = new Animator[8];
                                        r8[0] = ObjectAnimator.ofFloat(ChatActivity.this.instantCameraView, "alpha", new float[]{0.0f});
                                        r8[1] = ObjectAnimator.ofFloat(cameraContainer, "scaleX", new float[]{scale});
                                        r8[2] = ObjectAnimator.ofFloat(cameraContainer, "scaleY", new float[]{scale});
                                        r8[3] = ObjectAnimator.ofFloat(cameraContainer, "translationX", new float[]{((float) position[0]) - rect.x});
                                        r8[4] = ObjectAnimator.ofFloat(cameraContainer, "translationY", new float[]{((float) position[1]) - rect.y});
                                        r8[5] = ObjectAnimator.ofFloat(ChatActivity.this.instantCameraView.getSwitchButtonView(), "alpha", new float[]{0.0f});
                                        r8[6] = ObjectAnimator.ofInt(ChatActivity.this.instantCameraView.getPaint(), "alpha", new int[]{0});
                                        r8[7] = ObjectAnimator.ofFloat(ChatActivity.this.instantCameraView.getMuteImageView(), "alpha", new float[]{0.0f});
                                        animatorSet.playTogether(r8);
                                        animatorSet.setDuration(180);
                                        animatorSet.setInterpolator(new DecelerateInterpolator());
                                        animatorSet.addListener(new AnimatorListenerAdapter() {
                                            public void onAnimationEnd(Animator animation) {
                                                AnimatorSet animatorSet = new AnimatorSet();
                                                r1 = new Animator[2];
                                                r1[0] = ObjectAnimator.ofFloat(cameraContainer, "alpha", new float[]{0.0f});
                                                r1[1] = ObjectAnimator.ofFloat(messageCell, "alpha", new float[]{1.0f});
                                                animatorSet.playTogether(r1);
                                                animatorSet.setDuration(100);
                                                animatorSet.setInterpolator(new DecelerateInterpolator());
                                                animatorSet.addListener(new AnimatorListenerAdapter() {
                                                    public void onAnimationEnd(Animator animation) {
                                                        ChatActivity.this.instantCameraView.hideCamera(true);
                                                        ChatActivity.this.instantCameraView.setVisibility(4);
                                                    }
                                                });
                                                animatorSet.start();
                                            }
                                        });
                                        animatorSet.start();
                                        return true;
                                    }
                                });
                            }
                        }
                    }
                    messageCell.setHighlightedText(null);
                    index = ChatActivity.this.animatingMessageObjects.indexOf(message);
                    if (index != -1) {
                        ChatActivity.this.animatingMessageObjects.remove(index);
                        messageCell.getViewTreeObserver().addOnPreDrawListener(/* anonymous class already generated */);
                    }
                } else if (view instanceof ChatActionCell) {
                    ChatActionCell actionCell = (ChatActionCell) view;
                    actionCell.setMessageObject(message);
                    actionCell.setAlpha(1.0f);
                } else if (view instanceof ChatUnreadCell) {
                    ((ChatUnreadCell) view).setText(LocaleController.getString("UnreadMessages", R.string.UnreadMessages));
                    if (ChatActivity.this.createUnreadMessageAfterId != 0) {
                        ChatActivity.this.createUnreadMessageAfterId = 0;
                    }
                }
                if (message != null && message.messageOwner != null && message.messageOwner.media_unread && message.messageOwner.mentioned) {
                    if (!(ChatActivity.this.inPreviewMode || message.isVoice() || message.isRoundVideo())) {
                        ChatActivity.this.newMentionsCount = ChatActivity.this.newMentionsCount - 1;
                        if (ChatActivity.this.newMentionsCount <= 0) {
                            ChatActivity.this.newMentionsCount = 0;
                            ChatActivity.this.hasAllMentionsLocal = true;
                            ChatActivity.this.showMentiondownButton(false, true);
                        } else {
                            ChatActivity.this.mentiondownButtonCounter.setText(String.format("%d", new Object[]{Integer.valueOf(ChatActivity.this.newMentionsCount)}));
                        }
                        MessagesController.getInstance(ChatActivity.this.currentAccount).markMentionMessageAsRead(message.getId(), ChatObject.isChannel(ChatActivity.this.currentChat) ? ChatActivity.this.currentChat.id : 0, ChatActivity.this.dialog_id);
                        message.setContentIsRead();
                    }
                    if (!(view instanceof ChatMessageCell)) {
                        return;
                    }
                    if (ChatActivity.this.inPreviewMode) {
                        ((ChatMessageCell) view).setHighlighted(true);
                    } else {
                        ((ChatMessageCell) view).setHighlightedAnimated();
                    }
                }
            }
        }

        public int getItemViewType(int position) {
            if (position >= this.messagesStartRow && position < this.messagesEndRow) {
                return ((MessageObject) ChatActivity.this.messages.get(position - this.messagesStartRow)).contentType;
            }
            if (position == this.botInfoRow) {
                return 3;
            }
            return 4;
        }

        public void onViewAttachedToWindow(ViewHolder holder) {
            boolean z = true;
            if (holder.itemView instanceof ChatMessageCell) {
                boolean z2;
                boolean z3;
                final ChatMessageCell messageCell = holder.itemView;
                MessageObject message = messageCell.getMessageObject();
                boolean selected = false;
                boolean disableSelection = false;
                if (ChatActivity.this.actionBar.isActionModeShowed()) {
                    MessageObject messageObject;
                    int idx;
                    if (ChatActivity.this.chatActivityEnterView != null) {
                        messageObject = ChatActivity.this.chatActivityEnterView.getEditingMessageObject();
                    } else {
                        messageObject = null;
                    }
                    if (message.getDialogId() == ChatActivity.this.dialog_id) {
                        idx = 0;
                    } else {
                        idx = 1;
                    }
                    if (messageObject == message || ChatActivity.this.selectedMessagesIds[idx].indexOfKey(message.getId()) >= 0) {
                        ChatActivity.this.setCellSelectionBackground(message, messageCell, idx);
                        selected = true;
                    } else {
                        messageCell.setBackgroundDrawable(null);
                    }
                    disableSelection = true;
                } else {
                    messageCell.setBackgroundDrawable(null);
                }
                if (disableSelection) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                if (disableSelection && selected) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                messageCell.setCheckPressed(z2, z3);
                messageCell.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                    public boolean onPreDraw() {
                        messageCell.getViewTreeObserver().removeOnPreDrawListener(this);
                        int height = ChatActivity.this.chatListView.getMeasuredHeight();
                        int top = messageCell.getTop();
                        int bottom = messageCell.getBottom();
                        int viewTop = top >= 0 ? 0 : -top;
                        int viewBottom = messageCell.getMeasuredHeight();
                        if (viewBottom > height) {
                            viewBottom = viewTop + height;
                        }
                        messageCell.setVisiblePart(viewTop, viewBottom - viewTop);
                        return true;
                    }
                });
                if (!ChatActivity.this.inPreviewMode || !messageCell.isHighlighted()) {
                    if (ChatActivity.this.highlightMessageId == ConnectionsManager.DEFAULT_DATACENTER_ID || messageCell.getMessageObject().getId() != ChatActivity.this.highlightMessageId) {
                        z = false;
                    }
                    messageCell.setHighlighted(z);
                }
            }
        }

        public void updateRowAtPosition(int index) {
            if (ChatActivity.this.chatLayoutManager != null) {
                int lastVisibleItem = -1;
                if (!(ChatActivity.this.wasManualScroll || ChatActivity.this.unreadMessageObject == null)) {
                    int pos = ChatActivity.this.messages.indexOf(ChatActivity.this.unreadMessageObject);
                    if (pos >= 0) {
                        lastVisibleItem = this.messagesStartRow + pos;
                    }
                }
                notifyItemChanged(index);
                if (lastVisibleItem != -1) {
                    ChatActivity.this.chatLayoutManager.scrollToPositionWithOffset(lastVisibleItem, ((ChatActivity.this.chatListView.getMeasuredHeight() - ChatActivity.this.chatListView.getPaddingBottom()) - ChatActivity.this.chatListView.getPaddingTop()) - AndroidUtilities.dp(29.0f));
                }
            }
        }

        public void updateRowWithMessageObject(MessageObject messageObject, boolean allowInPlace) {
            if (allowInPlace) {
                int count = ChatActivity.this.chatListView.getChildCount();
                for (int a = 0; a < count; a++) {
                    View child = ChatActivity.this.chatListView.getChildAt(a);
                    if (child instanceof ChatMessageCell) {
                        ChatMessageCell cell = (ChatMessageCell) child;
                        if (cell.getMessageObject() == messageObject) {
                            cell.setMessageObject(messageObject, cell.getCurrentMessagesGroup(), cell.isPinnedBottom(), cell.isPinnedTop());
                            return;
                        }
                    }
                }
            }
            int index = ChatActivity.this.messages.indexOf(messageObject);
            if (index != -1) {
                updateRowAtPosition(this.messagesStartRow + index);
            }
        }

        public void notifyDataSetChanged() {
            updateRows();
            try {
                super.notifyDataSetChanged();
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }

        public void notifyItemChanged(int position) {
            updateRows();
            try {
                super.notifyItemChanged(position);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }

        public void notifyItemRangeChanged(int positionStart, int itemCount) {
            updateRows();
            try {
                super.notifyItemRangeChanged(positionStart, itemCount);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }

        public void notifyItemInserted(int position) {
            updateRows();
            try {
                super.notifyItemInserted(position);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }

        public void notifyItemMoved(int fromPosition, int toPosition) {
            updateRows();
            try {
                super.notifyItemMoved(fromPosition, toPosition);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }

        public void notifyItemRangeInserted(int positionStart, int itemCount) {
            updateRows();
            try {
                super.notifyItemRangeInserted(positionStart, itemCount);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }

        public void notifyItemRemoved(int position) {
            updateRows();
            try {
                super.notifyItemRemoved(position);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }

        public void notifyItemRangeRemoved(int positionStart, int itemCount) {
            updateRows();
            try {
                super.notifyItemRangeRemoved(positionStart, itemCount);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    final /* bridge */ /* synthetic */ void bridge$lambda$0$ChatActivity() {
        hideVoiceHint();
    }

    final /* bridge */ /* synthetic */ void bridge$lambda$1$ChatActivity() {
        inlineUpdate1();
    }

    final /* bridge */ /* synthetic */ void bridge$lambda$2$ChatActivity() {
        inlineUpdate2();
    }

    final /* bridge */ /* synthetic */ void bridge$lambda$3$ChatActivity() {
        checkListViewPaddingsInternal();
    }

    final /* bridge */ /* synthetic */ void bridge$lambda$4$ChatActivity() {
        checkEditTimer();
    }

    public ChatActivity(Bundle args) {
        super(args);
    }

    public boolean onFragmentCreate() {
        CountDownLatch countDownLatch;
        int chatId = this.arguments.getInt("chat_id", 0);
        int userId = this.arguments.getInt("user_id", 0);
        int encId = this.arguments.getInt("enc_id", 0);
        this.inlineReturn = this.arguments.getLong("inline_return", 0);
        String inlineQuery = this.arguments.getString("inline_query");
        this.startLoadFromMessageId = this.arguments.getInt("message_id", 0);
        int migrated_to = this.arguments.getInt("migrated_to", 0);
        this.scrollToTopOnResume = this.arguments.getBoolean("scrollToTopOnResume", false);
        MessagesStorage messagesStorage;
        if (chatId != 0) {
            this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(chatId));
            if (this.currentChat == null) {
                countDownLatch = new CountDownLatch(1);
                messagesStorage = MessagesStorage.getInstance(this.currentAccount);
                messagesStorage.getStorageQueue().postRunnable(new ChatActivity$$Lambda$0(this, messagesStorage, chatId, countDownLatch));
                try {
                    countDownLatch.await();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                if (this.currentChat == null) {
                    return false;
                }
                MessagesController.getInstance(this.currentAccount).putChat(this.currentChat, true);
            }
            if (chatId > 0) {
                this.dialog_id = (long) (-chatId);
            } else {
                this.isBroadcast = true;
                this.dialog_id = AndroidUtilities.makeBroadcastId(chatId);
            }
            if (ChatObject.isChannel(this.currentChat)) {
                MessagesController.getInstance(this.currentAccount).startShortPoll(chatId, false);
            }
        } else if (userId != 0) {
            this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(userId));
            if (this.currentUser == null) {
                messagesStorage = MessagesStorage.getInstance(this.currentAccount);
                countDownLatch = new CountDownLatch(1);
                messagesStorage.getStorageQueue().postRunnable(new ChatActivity$$Lambda$1(this, messagesStorage, userId, countDownLatch));
                try {
                    countDownLatch.await();
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
                if (this.currentUser == null) {
                    return false;
                }
                MessagesController.getInstance(this.currentAccount).putUser(this.currentUser, true);
            }
            this.dialog_id = (long) userId;
            this.botUser = this.arguments.getString("botUser");
            if (inlineQuery != null) {
                MessagesController.getInstance(this.currentAccount).sendBotStart(this.currentUser, inlineQuery);
            }
        } else if (encId == 0) {
            return false;
        } else {
            this.currentEncryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(encId));
            messagesStorage = MessagesStorage.getInstance(this.currentAccount);
            if (this.currentEncryptedChat == null) {
                countDownLatch = new CountDownLatch(1);
                messagesStorage.getStorageQueue().postRunnable(new ChatActivity$$Lambda$2(this, messagesStorage, encId, countDownLatch));
                try {
                    countDownLatch.await();
                } catch (Throwable e22) {
                    FileLog.e(e22);
                }
                if (this.currentEncryptedChat == null) {
                    return false;
                }
                MessagesController.getInstance(this.currentAccount).putEncryptedChat(this.currentEncryptedChat, true);
            }
            this.currentUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.currentEncryptedChat.user_id));
            if (this.currentUser == null) {
                countDownLatch = new CountDownLatch(1);
                messagesStorage.getStorageQueue().postRunnable(new ChatActivity$$Lambda$3(this, messagesStorage, countDownLatch));
                try {
                    countDownLatch.await();
                } catch (Throwable e222) {
                    FileLog.e(e222);
                }
                if (this.currentUser == null) {
                    return false;
                }
                MessagesController.getInstance(this.currentAccount).putUser(this.currentUser, true);
            }
            this.dialog_id = ((long) encId) << 32;
            int[] iArr = this.maxMessageId;
            this.maxMessageId[1] = Integer.MIN_VALUE;
            iArr[0] = Integer.MIN_VALUE;
            iArr = this.minMessageId;
            this.minMessageId[1] = ConnectionsManager.DEFAULT_DATACENTER_ID;
            iArr[0] = ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
        if (this.currentUser != null) {
            MediaController.getInstance().startMediaObserver();
        }
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesDidLoaded);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.emojiDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didReceivedNewMessages);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesRead);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.historyCleared);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messageReceivedByServer);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messageReceivedByAck);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messageSendError);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.contactsDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.encryptedChatUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesReadEncrypted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.removeAllMessagesFromDialog);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.screenshotTook);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.blockedUsersDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileNewChunkAvailable);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didCreatedNewDeleteTask);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagePlayingDidStarted);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateMessageMedia);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.replaceMessagesObjects);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.notificationsSettingsUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didLoadedReplyMessages);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didReceivedWebpages);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didReceivedWebpagesInUpdates);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.messagesReadContent);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.botInfoDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.botKeyboardDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatSearchResultsAvailable);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatSearchResultsLoading);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatedMessagesViews);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.chatInfoCantLoad);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didLoadedPinnedMessage);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.peerSettingsDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.newDraftReceived);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.userInfoDidLoaded);
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.channelRightsUpdated);
        NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.updateMentionsCount);
        super.onFragmentCreate();
        if (this.currentEncryptedChat == null && !this.isBroadcast) {
            DataQuery.getInstance(this.currentAccount).loadBotKeyboard(this.dialog_id);
        }
        this.loading = true;
        MessagesController.getInstance(this.currentAccount).loadPeerSettings(this.currentUser, this.currentChat);
        MessagesController.getInstance(this.currentAccount).setLastCreatedDialogId(this.dialog_id, true);
        if (this.startLoadFromMessageId == 0) {
            SharedPreferences sharedPreferences = MessagesController.getNotificationsSettings(this.currentAccount);
            int messageId = sharedPreferences.getInt("diditem" + this.dialog_id, 0);
            if (messageId != 0) {
                this.wasManualScroll = true;
                this.loadingFromOldPosition = true;
                this.startLoadFromMessageOffset = sharedPreferences.getInt("diditemo" + this.dialog_id, 0);
                this.startLoadFromMessageId = messageId;
            }
        } else {
            this.needSelectFromMessageId = true;
        }
        MessagesController instance;
        long j;
        int i;
        int i2;
        boolean isChannel;
        int i3;
        if (this.startLoadFromMessageId != 0) {
            this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
            int i4;
            if (migrated_to != 0) {
                this.mergeDialogId = (long) migrated_to;
                instance = MessagesController.getInstance(this.currentAccount);
                j = this.mergeDialogId;
                i = this.loadingFromOldPosition ? 50 : AndroidUtilities.isTablet() ? 30 : 20;
                i4 = this.startLoadFromMessageId;
                i2 = this.classGuid;
                isChannel = ChatObject.isChannel(this.currentChat);
                i3 = this.lastLoadIndex;
                this.lastLoadIndex = i3 + 1;
                instance.loadMessages(j, i, i4, 0, true, 0, i2, 3, 0, isChannel, i3);
            } else {
                instance = MessagesController.getInstance(this.currentAccount);
                j = this.dialog_id;
                i = this.loadingFromOldPosition ? 50 : AndroidUtilities.isTablet() ? 30 : 20;
                i4 = this.startLoadFromMessageId;
                i2 = this.classGuid;
                isChannel = ChatObject.isChannel(this.currentChat);
                i3 = this.lastLoadIndex;
                this.lastLoadIndex = i3 + 1;
                instance.loadMessages(j, i, i4, 0, true, 0, i2, 3, 0, isChannel, i3);
            }
        } else {
            this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
            instance = MessagesController.getInstance(this.currentAccount);
            j = this.dialog_id;
            i = AndroidUtilities.isTablet() ? 30 : 20;
            i2 = this.classGuid;
            isChannel = ChatObject.isChannel(this.currentChat);
            i3 = this.lastLoadIndex;
            this.lastLoadIndex = i3 + 1;
            instance.loadMessages(j, i, 0, 0, true, 0, i2, 2, 0, isChannel, i3);
        }
        if (this.currentChat != null) {
            CountDownLatch countDownLatch2 = null;
            if (this.isBroadcast) {
                countDownLatch = new CountDownLatch(1);
            }
            MessagesController.getInstance(this.currentAccount).loadChatInfo(this.currentChat.id, countDownLatch2, ChatObject.isChannel(this.currentChat));
            if (this.isBroadcast && countDownLatch2 != null) {
                try {
                    countDownLatch2.await();
                } catch (Throwable e2222) {
                    FileLog.e(e2222);
                }
            }
        }
        if (userId != 0 && this.currentUser.bot) {
            DataQuery.getInstance(this.currentAccount).loadBotInfo(userId, true, this.classGuid);
        } else if (this.info instanceof TL_chatFull) {
            for (int a = 0; a < this.info.participants.participants.size(); a++) {
                User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(((ChatParticipant) this.info.participants.participants.get(a)).user_id));
                if (user != null && user.bot) {
                    DataQuery.getInstance(this.currentAccount).loadBotInfo(user.id, true, this.classGuid);
                }
            }
        }
        if (this.currentUser != null) {
            this.userBlocked = MessagesController.getInstance(this.currentAccount).blockedUsers.indexOfKey(this.currentUser.id) >= 0;
        }
        if (AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.openedChatChanged, Long.valueOf(this.dialog_id), Boolean.valueOf(false));
        }
        if (!(this.currentEncryptedChat == null || AndroidUtilities.getMyLayerVersion(this.currentEncryptedChat.layer) == 73)) {
            SecretChatHelper.getInstance(this.currentAccount).sendNotifyLayerMessage(this.currentEncryptedChat, null);
        }
        return true;
    }

    final /* synthetic */ void lambda$onFragmentCreate$0$ChatActivity(MessagesStorage messagesStorage, int chatId, CountDownLatch countDownLatch) {
        this.currentChat = messagesStorage.getChat(chatId);
        countDownLatch.countDown();
    }

    final /* synthetic */ void lambda$onFragmentCreate$1$ChatActivity(MessagesStorage messagesStorage, int userId, CountDownLatch countDownLatch) {
        this.currentUser = messagesStorage.getUser(userId);
        countDownLatch.countDown();
    }

    final /* synthetic */ void lambda$onFragmentCreate$2$ChatActivity(MessagesStorage messagesStorage, int encId, CountDownLatch countDownLatch) {
        this.currentEncryptedChat = messagesStorage.getEncryptedChat(encId);
        countDownLatch.countDown();
    }

    final /* synthetic */ void lambda$onFragmentCreate$3$ChatActivity(MessagesStorage messagesStorage, CountDownLatch countDownLatch) {
        this.currentUser = messagesStorage.getUser(this.currentEncryptedChat.user_id);
        countDownLatch.countDown();
    }

    public void onFragmentDestroy() {
        super.onFragmentDestroy();
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.onDestroy();
        }
        if (this.mentionsAdapter != null) {
            this.mentionsAdapter.onDestroy();
        }
        if (this.chatAttachAlert != null) {
            this.chatAttachAlert.dismissInternal();
        }
        MessagesController.getInstance(this.currentAccount).setLastCreatedDialogId(this.dialog_id, false);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDidLoaded);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateInterfaces);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didReceivedNewMessages);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.closeChats);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesRead);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesDeleted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.historyCleared);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messageReceivedByServer);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messageReceivedByAck);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messageSendError);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.encryptedChatUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesReadEncrypted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.removeAllMessagesFromDialog);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.contactsDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidReset);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.screenshotTook);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.blockedUsersDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileNewChunkAvailable);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didCreatedNewDeleteTask);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingDidStarted);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateMessageMedia);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.replaceMessagesObjects);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.notificationsSettingsUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didLoadedReplyMessages);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didReceivedWebpages);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didReceivedWebpagesInUpdates);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagesReadContent);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.botInfoDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.botKeyboardDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatSearchResultsAvailable);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatSearchResultsLoading);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.messagePlayingPlayStateChanged);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatedMessagesViews);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.chatInfoCantLoad);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didLoadedPinnedMessage);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.peerSettingsDidLoaded);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.newDraftReceived);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.userInfoDidLoaded);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.didSetNewWallpapper);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.channelRightsUpdated);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.updateMentionsCount);
        if (AndroidUtilities.isTablet()) {
            NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.openedChatChanged, Long.valueOf(this.dialog_id), Boolean.valueOf(true));
        }
        if (this.currentUser != null) {
            MediaController.getInstance().stopMediaObserver();
        }
        if (this.currentEncryptedChat != null) {
            try {
                if (VERSION.SDK_INT >= 23 && (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture)) {
                    MediaController.getInstance().setFlagSecure(this, false);
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
        if (this.currentUser != null) {
            MessagesController.getInstance(this.currentAccount).cancelLoadFullUser(this.currentUser.id);
        }
        AndroidUtilities.removeAdjustResize(getParentActivity(), this.classGuid);
        if (this.stickersAdapter != null) {
            this.stickersAdapter.onDestroy();
        }
        if (this.chatAttachAlert != null) {
            this.chatAttachAlert.onDestroy();
        }
        AndroidUtilities.unlockOrientation(getParentActivity());
        if (ChatObject.isChannel(this.currentChat)) {
            MessagesController.getInstance(this.currentAccount).startShortPoll(this.currentChat.id, true);
        }
    }

    public View createView(Context context) {
        int a;
        CharSequence oldMessage;
        boolean z;
        View fragmentContextView;
        boolean z2;
        if (this.chatMessageCellsCache.isEmpty()) {
            for (a = 0; a < 8; a++) {
                this.chatMessageCellsCache.add(new ChatMessageCell(context));
            }
        }
        for (a = 1; a >= 0; a--) {
            this.selectedMessagesIds[a].clear();
            this.selectedMessagesCanCopyIds[a].clear();
            this.selectedMessagesCanStarIds[a].clear();
        }
        this.cantDeleteMessagesCount = 0;
        this.canEditMessagesCount = 0;
        this.roundVideoContainer = null;
        this.hasOwnBackground = true;
        if (this.chatAttachAlert != null) {
            try {
                if (this.chatAttachAlert.isShowing()) {
                    this.chatAttachAlert.dismiss();
                }
            } catch (Exception e) {
            }
            this.chatAttachAlert.onDestroy();
            this.chatAttachAlert = null;
        }
        Theme.createChatResources(context, false);
        this.actionBar.setAddToContainer(false);
        if (this.inPreviewMode) {
            this.actionBar.setBackButtonDrawable(null);
        } else {
            this.actionBar.setBackButtonDrawable(new BackDrawable(false));
        }
        this.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                int a;
                if (id == -1) {
                    if (ChatActivity.this.actionBar.isActionModeShowed()) {
                        for (a = 1; a >= 0; a--) {
                            ChatActivity.this.selectedMessagesIds[a].clear();
                            ChatActivity.this.selectedMessagesCanCopyIds[a].clear();
                            ChatActivity.this.selectedMessagesCanStarIds[a].clear();
                        }
                        ChatActivity.this.cantDeleteMessagesCount = 0;
                        ChatActivity.this.canEditMessagesCount = 0;
                        ChatActivity.this.actionBar.hideActionMode();
                        ChatActivity.this.updatePinnedMessageView(true);
                        ChatActivity.this.updateVisibleRows();
                        return;
                    }
                    ChatActivity.this.finishFragment();
                } else if (id == 10) {
                    String str = TtmlNode.ANONYMOUS_REGION_ID;
                    int previousUid = 0;
                    for (a = 1; a >= 0; a--) {
                        ids = new ArrayList();
                        for (b = 0; b < ChatActivity.this.selectedMessagesCanCopyIds[a].size(); b++) {
                            ids.add(Integer.valueOf(ChatActivity.this.selectedMessagesCanCopyIds[a].keyAt(b)));
                        }
                        if (ChatActivity.this.currentEncryptedChat == null) {
                            Collections.sort(ids);
                        } else {
                            Collections.sort(ids, Collections.reverseOrder());
                        }
                        for (b = 0; b < ids.size(); b++) {
                            messageObject = (MessageObject) ChatActivity.this.selectedMessagesCanCopyIds[a].get(((Integer) ids.get(b)).intValue());
                            if (str.length() != 0) {
                                str = str + "\n\n";
                            }
                            str = str + ChatActivity.this.getMessageContent(messageObject, previousUid, true);
                            previousUid = messageObject.messageOwner.from_id;
                        }
                    }
                    if (str.length() != 0) {
                        AndroidUtilities.addToClipboard(str);
                    }
                    for (a = 1; a >= 0; a--) {
                        ChatActivity.this.selectedMessagesIds[a].clear();
                        ChatActivity.this.selectedMessagesCanCopyIds[a].clear();
                        ChatActivity.this.selectedMessagesCanStarIds[a].clear();
                    }
                    ChatActivity.this.cantDeleteMessagesCount = 0;
                    ChatActivity.this.canEditMessagesCount = 0;
                    ChatActivity.this.actionBar.hideActionMode();
                    ChatActivity.this.updatePinnedMessageView(true);
                    ChatActivity.this.updateVisibleRows();
                } else if (id == 12) {
                    if (ChatActivity.this.getParentActivity() != null) {
                        ChatActivity.this.createDeleteMessagesAlert(null, null);
                    }
                } else if (id == 11) {
                    args = new Bundle();
                    args.putBoolean("onlySelect", true);
                    args.putInt("dialogsType", 3);
                    BaseFragment dialogsActivity = new DialogsActivity(args);
                    dialogsActivity.setDelegate(ChatActivity.this);
                    ChatActivity.this.presentFragment(dialogsActivity);
                } else if (id == 13) {
                    if (ChatActivity.this.getParentActivity() != null) {
                        ChatActivity.this.showDialog(AlertsCreator.createTTLAlert(ChatActivity.this.getParentActivity(), ChatActivity.this.currentEncryptedChat).create());
                    }
                } else if (id == 15 || id == 16) {
                    if (ChatActivity.this.getParentActivity() != null) {
                        boolean isChat = ((int) ChatActivity.this.dialog_id) < 0 && ((int) (ChatActivity.this.dialog_id >> 32)) != 1;
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this.getParentActivity());
                        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                        if (id == 15) {
                            builder.setMessage(LocaleController.getString("AreYouSureClearHistory", R.string.AreYouSureClearHistory));
                        } else if (isChat) {
                            builder.setMessage(LocaleController.getString("AreYouSureDeleteAndExit", R.string.AreYouSureDeleteAndExit));
                        } else {
                            builder.setMessage(LocaleController.getString("AreYouSureDeleteThisChat", R.string.AreYouSureDeleteThisChat));
                        }
                        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$5$$Lambda$0(this, id, isChat));
                        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                        ChatActivity.this.showDialog(builder.create());
                    }
                } else if (id == 17) {
                    if (ChatActivity.this.currentUser != null && ChatActivity.this.getParentActivity() != null) {
                        if (ChatActivity.this.currentUser.phone == null || ChatActivity.this.currentUser.phone.length() == 0) {
                            ChatActivity.this.shareMyContact(ChatActivity.this.replyingMessageObject);
                            return;
                        }
                        args = new Bundle();
                        args.putInt("user_id", ChatActivity.this.currentUser.id);
                        args.putBoolean("addContact", true);
                        ChatActivity.this.presentFragment(new ContactAddActivity(args));
                    }
                } else if (id == 18) {
                    ChatActivity.this.toggleMute(false);
                } else if (id == 24) {
                    try {
                        DataQuery.getInstance(ChatActivity.this.currentAccount).installShortcut((long) ChatActivity.this.currentUser.id);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                } else if (id == 21) {
                    ChatActivity.this.showDialog(AlertsCreator.createReportAlert(ChatActivity.this.getParentActivity(), ChatActivity.this.dialog_id, 0, ChatActivity.this));
                } else if (id == 19) {
                    messageObject = null;
                    a = 1;
                    while (a >= 0) {
                        if (messageObject == null && ChatActivity.this.selectedMessagesIds[a].size() == 1) {
                            ids = new ArrayList();
                            for (b = 0; b < ChatActivity.this.selectedMessagesIds[a].size(); b++) {
                                ids.add(Integer.valueOf(ChatActivity.this.selectedMessagesIds[a].keyAt(b)));
                            }
                            messageObject = (MessageObject) ChatActivity.this.messagesDict[a].get(((Integer) ids.get(0)).intValue());
                        }
                        ChatActivity.this.selectedMessagesIds[a].clear();
                        ChatActivity.this.selectedMessagesCanCopyIds[a].clear();
                        ChatActivity.this.selectedMessagesCanStarIds[a].clear();
                        a--;
                    }
                    if (messageObject != null && (messageObject.messageOwner.id > 0 || (messageObject.messageOwner.id < 0 && ChatActivity.this.currentEncryptedChat != null))) {
                        ChatActivity.this.showFieldPanelForReply(true, messageObject);
                    }
                    ChatActivity.this.cantDeleteMessagesCount = 0;
                    ChatActivity.this.canEditMessagesCount = 0;
                    ChatActivity.this.actionBar.hideActionMode();
                    ChatActivity.this.updatePinnedMessageView(true);
                    ChatActivity.this.updateVisibleRows();
                } else if (id == 22) {
                    for (a = 0; a < 2; a++) {
                        for (b = 0; b < ChatActivity.this.selectedMessagesCanStarIds[a].size(); b++) {
                            DataQuery.getInstance(ChatActivity.this.currentAccount).addRecentSticker(2, ((MessageObject) ChatActivity.this.selectedMessagesCanStarIds[a].valueAt(b)).getDocument(), (int) (System.currentTimeMillis() / 1000), !ChatActivity.this.hasUnfavedSelected);
                        }
                    }
                    for (a = 1; a >= 0; a--) {
                        ChatActivity.this.selectedMessagesIds[a].clear();
                        ChatActivity.this.selectedMessagesCanCopyIds[a].clear();
                        ChatActivity.this.selectedMessagesCanStarIds[a].clear();
                    }
                    ChatActivity.this.cantDeleteMessagesCount = 0;
                    ChatActivity.this.canEditMessagesCount = 0;
                    ChatActivity.this.actionBar.hideActionMode();
                    ChatActivity.this.updatePinnedMessageView(true);
                    ChatActivity.this.updateVisibleRows();
                } else if (id == 23) {
                    messageObject = null;
                    a = 1;
                    while (a >= 0) {
                        if (messageObject == null && ChatActivity.this.selectedMessagesIds[a].size() == 1) {
                            ids = new ArrayList();
                            for (b = 0; b < ChatActivity.this.selectedMessagesIds[a].size(); b++) {
                                ids.add(Integer.valueOf(ChatActivity.this.selectedMessagesIds[a].keyAt(b)));
                            }
                            messageObject = (MessageObject) ChatActivity.this.messagesDict[a].get(((Integer) ids.get(0)).intValue());
                        }
                        ChatActivity.this.selectedMessagesIds[a].clear();
                        ChatActivity.this.selectedMessagesCanCopyIds[a].clear();
                        ChatActivity.this.selectedMessagesCanStarIds[a].clear();
                        a--;
                    }
                    ChatActivity.this.startReplyOnTextChange = false;
                    ChatActivity.this.startEditingMessageObject(messageObject);
                    ChatActivity.this.cantDeleteMessagesCount = 0;
                    ChatActivity.this.canEditMessagesCount = 0;
                    ChatActivity.this.actionBar.hideActionMode();
                    ChatActivity.this.updatePinnedMessageView(true);
                    ChatActivity.this.updateVisibleRows();
                } else if (id == 14) {
                    if (ChatActivity.this.chatAttachAlert != null) {
                        ChatActivity.this.chatAttachAlert.setEditingMessageObject(null);
                    }
                    ChatActivity.this.openAttachMenu();
                } else if (id == 30) {
                    SendMessagesHelper.getInstance(ChatActivity.this.currentAccount).sendMessage("/help", ChatActivity.this.dialog_id, null, null, false, null, null, null);
                } else if (id == 31) {
                    SendMessagesHelper.getInstance(ChatActivity.this.currentAccount).sendMessage("/settings", ChatActivity.this.dialog_id, null, null, false, null, null, null);
                } else if (id == ChatActivity.search) {
                    ChatActivity.this.openSearchWithText(null);
                } else if (id == 32) {
                    if (ChatActivity.this.currentUser != null && ChatActivity.this.getParentActivity() != null) {
                        VoIPHelper.startCall(ChatActivity.this.currentUser, ChatActivity.this.getParentActivity(), MessagesController.getInstance(ChatActivity.this.currentAccount).getUserFull(ChatActivity.this.currentUser.id));
                    }
                } else if (id == 50) {
                    if (ChatActivity.this.chatActivityEnterView != null) {
                        ChatActivity.this.chatActivityEnterView.getEditField().setSelectionOverride(ChatActivity.this.editTextStart, ChatActivity.this.editTextEnd);
                        ChatActivity.this.chatActivityEnterView.getEditField().makeSelectedBold();
                    }
                } else if (id == 51) {
                    if (ChatActivity.this.chatActivityEnterView != null) {
                        ChatActivity.this.chatActivityEnterView.getEditField().setSelectionOverride(ChatActivity.this.editTextStart, ChatActivity.this.editTextEnd);
                        ChatActivity.this.chatActivityEnterView.getEditField().makeSelectedItalic();
                    }
                } else if (id == 52) {
                    if (ChatActivity.this.chatActivityEnterView != null) {
                        ChatActivity.this.chatActivityEnterView.getEditField().setSelectionOverride(ChatActivity.this.editTextStart, ChatActivity.this.editTextEnd);
                        ChatActivity.this.chatActivityEnterView.getEditField().makeSelectedMono();
                    }
                } else if (id == 53) {
                    if (ChatActivity.this.chatActivityEnterView != null) {
                        ChatActivity.this.chatActivityEnterView.getEditField().setSelectionOverride(ChatActivity.this.editTextStart, ChatActivity.this.editTextEnd);
                        ChatActivity.this.chatActivityEnterView.getEditField().makeSelectedUrl();
                    }
                } else if (id == 54 && ChatActivity.this.chatActivityEnterView != null) {
                    ChatActivity.this.chatActivityEnterView.getEditField().setSelectionOverride(ChatActivity.this.editTextStart, ChatActivity.this.editTextEnd);
                    ChatActivity.this.chatActivityEnterView.getEditField().makeSelectedRegular();
                }
            }

            final /* synthetic */ void lambda$onItemClick$0$ChatActivity$5(int id, boolean isChat, DialogInterface dialogInterface, int i) {
                if (id != 15) {
                    if (!isChat) {
                        MessagesController.getInstance(ChatActivity.this.currentAccount).deleteDialog(ChatActivity.this.dialog_id, 0);
                    } else if (ChatObject.isNotInChat(ChatActivity.this.currentChat)) {
                        MessagesController.getInstance(ChatActivity.this.currentAccount).deleteDialog(ChatActivity.this.dialog_id, 0);
                    } else {
                        MessagesController.getInstance(ChatActivity.this.currentAccount).deleteUserFromChat((int) (-ChatActivity.this.dialog_id), MessagesController.getInstance(ChatActivity.this.currentAccount).getUser(Integer.valueOf(UserConfig.getInstance(ChatActivity.this.currentAccount).getClientUserId())), null);
                    }
                    ChatActivity.this.finishFragment();
                    return;
                }
                if (!(!ChatObject.isChannel(ChatActivity.this.currentChat) || ChatActivity.this.info == null || ChatActivity.this.info.pinned_msg_id == 0)) {
                    MessagesController.getNotificationsSettings(ChatActivity.this.currentAccount).edit().putInt("pin_" + ChatActivity.this.dialog_id, ChatActivity.this.info.pinned_msg_id).commit();
                    ChatActivity.this.updatePinnedMessageView(true);
                }
                MessagesController.getInstance(ChatActivity.this.currentAccount).deleteDialog(ChatActivity.this.dialog_id, 1);
            }
        });
        this.avatarContainer = new ChatAvatarContainer(context, this, this.currentEncryptedChat != null);
        if (this.inPreviewMode) {
            this.avatarContainer.setOccupyStatusBar(false);
        }
        this.actionBar.addView(this.avatarContainer, 0, LayoutHelper.createFrame(-2, -1.0f, 51, !this.inPreviewMode ? 56.0f : 0.0f, 0.0f, 40.0f, 0.0f));
        if (!(this.currentChat == null || ChatObject.isChannel(this.currentChat))) {
            int count = this.currentChat.participants_count;
            if (this.info != null) {
                count = this.info.participants.participants.size();
            }
            if (count == 0 || this.currentChat.deactivated || this.currentChat.left || (this.currentChat instanceof TL_chatForbidden) || (this.info != null && (this.info.participants instanceof TL_chatParticipantsForbidden))) {
                this.avatarContainer.setEnabled(false);
            }
        }
        ActionBarMenu menu = this.actionBar.createMenu();
        if (this.currentEncryptedChat == null && !this.isBroadcast) {
            this.searchItem = menu.addItem(0, (int) R.drawable.ic_ab_search).setIsSearchField(true).setActionBarMenuItemSearchListener(new ActionBarMenuItemSearchListener() {
                boolean searchWas;

                public void onSearchCollapse() {
                    ChatActivity.this.searchCalendarButton.setVisibility(0);
                    if (ChatActivity.this.searchUserButton != null) {
                        ChatActivity.this.searchUserButton.setVisibility(0);
                    }
                    if (ChatActivity.this.searchingForUser) {
                        ChatActivity.this.mentionsAdapter.searchUsernameOrHashtag(null, 0, null, false);
                        ChatActivity.this.searchingForUser = false;
                    }
                    ChatActivity.this.mentionLayoutManager.setReverseLayout(false);
                    ChatActivity.this.mentionsAdapter.setSearchingMentions(false);
                    ChatActivity.this.searchingUserMessages = null;
                    ChatActivity.this.searchItem.getSearchField().setHint(LocaleController.getString("Search", R.string.Search));
                    ChatActivity.this.searchItem.setSearchFieldCaption(null);
                    ChatActivity.this.avatarContainer.setVisibility(0);
                    if (ChatActivity.this.editTextItem.getTag() != null) {
                        if (ChatActivity.this.headerItem != null) {
                            ChatActivity.this.headerItem.setVisibility(8);
                            ChatActivity.this.editTextItem.setVisibility(0);
                            ChatActivity.this.attachItem.setVisibility(8);
                        }
                    } else if (ChatActivity.this.chatActivityEnterView.hasText()) {
                        if (ChatActivity.this.headerItem != null) {
                            ChatActivity.this.headerItem.setVisibility(8);
                            ChatActivity.this.editTextItem.setVisibility(8);
                            ChatActivity.this.attachItem.setVisibility(0);
                        }
                    } else if (ChatActivity.this.headerItem != null) {
                        ChatActivity.this.headerItem.setVisibility(0);
                        ChatActivity.this.editTextItem.setVisibility(8);
                        ChatActivity.this.attachItem.setVisibility(8);
                    }
                    ChatActivity.this.searchItem.setVisibility(8);
                    ChatActivity.this.highlightMessageId = ConnectionsManager.DEFAULT_DATACENTER_ID;
                    ChatActivity.this.updateVisibleRows();
                    if (this.searchWas) {
                        ChatActivity.this.scrollToLastMessage(false);
                    }
                    ChatActivity.this.updateBottomOverlay();
                    ChatActivity.this.updatePinnedMessageView(true);
                }

                public void onSearchExpand() {
                    if (ChatActivity.this.openSearchKeyboard) {
                        AndroidUtilities.runOnUIThread(new ChatActivity$6$$Lambda$0(this), 300);
                    }
                }

                final /* synthetic */ void lambda$onSearchExpand$0$ChatActivity$6() {
                    this.searchWas = false;
                    ChatActivity.this.searchItem.getSearchField().requestFocus();
                    AndroidUtilities.showKeyboard(ChatActivity.this.searchItem.getSearchField());
                }

                public void onSearchPressed(EditText editText) {
                    this.searchWas = true;
                    ChatActivity.this.updateSearchButtons(0, 0, -1);
                    DataQuery.getInstance(ChatActivity.this.currentAccount).searchMessagesInChat(editText.getText().toString(), ChatActivity.this.dialog_id, ChatActivity.this.mergeDialogId, ChatActivity.this.classGuid, 0, ChatActivity.this.searchingUserMessages);
                }

                public void onTextChanged(EditText editText) {
                    if (ChatActivity.this.searchingForUser) {
                        ChatActivity.this.mentionsAdapter.searchUsernameOrHashtag("@" + editText.getText().toString(), 0, ChatActivity.this.messages, true);
                    } else if (!ChatActivity.this.searchingForUser && ChatActivity.this.searchingUserMessages == null && ChatActivity.this.searchUserButton != null && TextUtils.equals(editText.getText(), LocaleController.getString("SearchFrom", R.string.SearchFrom))) {
                        ChatActivity.this.searchUserButton.callOnClick();
                    }
                }

                public void onCaptionCleared() {
                    if (ChatActivity.this.searchingUserMessages != null) {
                        ChatActivity.this.searchUserButton.callOnClick();
                        return;
                    }
                    if (ChatActivity.this.searchingForUser) {
                        ChatActivity.this.mentionsAdapter.searchUsernameOrHashtag(null, 0, null, false);
                        ChatActivity.this.searchingForUser = false;
                    }
                    ChatActivity.this.searchItem.getSearchField().setHint(LocaleController.getString("Search", R.string.Search));
                    ChatActivity.this.searchCalendarButton.setVisibility(0);
                    ChatActivity.this.searchUserButton.setVisibility(0);
                    ChatActivity.this.searchingUserMessages = null;
                }
            });
            this.searchItem.getSearchField().setHint(LocaleController.getString("Search", R.string.Search));
            this.searchItem.setVisibility(8);
        }
        this.headerItem = menu.addItem(0, (int) R.drawable.ic_ab_other);
        if (this.currentUser != null) {
            this.headerItem.addSubItem(32, LocaleController.getString("Call", R.string.Call));
            TL_userFull userFull = MessagesController.getInstance(this.currentAccount).getUserFull(this.currentUser.id);
            if (userFull == null || !userFull.phone_calls_available) {
                this.headerItem.hideSubItem(32);
            } else {
                this.headerItem.showSubItem(32);
            }
        }
        this.editTextItem = menu.addItem(0, (int) R.drawable.ic_ab_other);
        this.editTextItem.setTag(null);
        this.editTextItem.setVisibility(8);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString("Bold", R.string.Bold));
        spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0, spannableStringBuilder.length(), 33);
        this.editTextItem.addSubItem(50, spannableStringBuilder);
        spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString("Italic", R.string.Italic));
        spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf")), 0, spannableStringBuilder.length(), 33);
        this.editTextItem.addSubItem(51, spannableStringBuilder);
        spannableStringBuilder = new SpannableStringBuilder(LocaleController.getString("Mono", R.string.Mono));
        spannableStringBuilder.setSpan(new TypefaceSpan(Typeface.MONOSPACE), 0, spannableStringBuilder.length(), 33);
        this.editTextItem.addSubItem(52, spannableStringBuilder);
        this.editTextItem.addSubItem(53, LocaleController.getString("CreateLink", R.string.CreateLink));
        this.editTextItem.addSubItem(54, LocaleController.getString("Regular", R.string.Regular));
        if (this.searchItem != null) {
            this.headerItem.addSubItem(search, LocaleController.getString("Search", R.string.Search));
        }
        if (ChatObject.isChannel(this.currentChat) && !this.currentChat.creator && (!this.currentChat.megagroup || (this.currentChat.username != null && this.currentChat.username.length() > 0))) {
            this.headerItem.addSubItem(21, LocaleController.getString("ReportChat", R.string.ReportChat));
        }
        if (this.currentUser != null) {
            this.addContactItem = this.headerItem.addSubItem(17, TtmlNode.ANONYMOUS_REGION_ID);
        }
        if (this.currentEncryptedChat != null) {
            this.timeItem2 = this.headerItem.addSubItem(13, LocaleController.getString("SetTimer", R.string.SetTimer));
        }
        if (!ChatObject.isChannel(this.currentChat) || (this.currentChat != null && this.currentChat.megagroup && TextUtils.isEmpty(this.currentChat.username))) {
            this.headerItem.addSubItem(15, LocaleController.getString("ClearHistory", R.string.ClearHistory));
        }
        if (!ChatObject.isChannel(this.currentChat)) {
            if (this.currentChat == null || this.isBroadcast) {
                this.headerItem.addSubItem(16, LocaleController.getString("DeleteChatUser", R.string.DeleteChatUser));
            } else {
                this.headerItem.addSubItem(16, LocaleController.getString("DeleteAndExit", R.string.DeleteAndExit));
            }
        }
        if (this.currentUser == null || !this.currentUser.self) {
            this.muteItem = this.headerItem.addSubItem(18, null);
        } else if (this.currentUser.self) {
            this.headerItem.addSubItem(24, LocaleController.getString("AddShortcut", R.string.AddShortcut));
        }
        if (this.currentUser != null && this.currentEncryptedChat == null && this.currentUser.bot) {
            this.headerItem.addSubItem(31, LocaleController.getString("BotSettings", R.string.BotSettings));
            this.headerItem.addSubItem(30, LocaleController.getString("BotHelp", R.string.BotHelp));
            updateBotButtons();
        }
        updateTitle();
        this.avatarContainer.updateOnlineCount();
        this.avatarContainer.updateSubtitle();
        updateTitleIcons();
        this.attachItem = menu.addItem(14, (int) R.drawable.ic_ab_other).setOverrideMenuClick(true).setAllowCloseAnimation(false);
        this.attachItem.setVisibility(8);
        this.actionModeViews.clear();
        if (this.inPreviewMode) {
            this.headerItem.setAlpha(0.0f);
            this.attachItem.setAlpha(0.0f);
        }
        ActionBarMenu actionMode = this.actionBar.createActionMode();
        this.selectedMessagesCountTextView = new NumberTextView(actionMode.getContext());
        this.selectedMessagesCountTextView.setTextSize(18);
        this.selectedMessagesCountTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.selectedMessagesCountTextView.setTextColor(Theme.getColor(Theme.key_actionBarActionModeDefaultIcon));
        actionMode.addView(this.selectedMessagesCountTextView, LayoutHelper.createLinear(0, -1, 1.0f, 65, 0, 0, 0));
        this.selectedMessagesCountTextView.setOnTouchListener(ChatActivity$$Lambda$4.$instance);
        if (this.currentEncryptedChat == null) {
            this.actionModeViews.add(actionMode.addItemWithWidth(23, R.drawable.group_edit, AndroidUtilities.dp(54.0f)));
            if (!this.isBroadcast) {
                this.actionModeViews.add(actionMode.addItemWithWidth(19, R.drawable.ic_ab_reply, AndroidUtilities.dp(54.0f)));
            }
            this.actionModeViews.add(actionMode.addItemWithWidth(22, R.drawable.ic_ab_fave, AndroidUtilities.dp(54.0f)));
            this.actionModeViews.add(actionMode.addItemWithWidth(10, R.drawable.ic_ab_copy, AndroidUtilities.dp(54.0f)));
            this.actionModeViews.add(actionMode.addItemWithWidth(11, R.drawable.ic_ab_forward, AndroidUtilities.dp(54.0f)));
            this.actionModeViews.add(actionMode.addItemWithWidth(12, R.drawable.ic_ab_delete, AndroidUtilities.dp(54.0f)));
        } else {
            this.actionModeViews.add(actionMode.addItemWithWidth(23, R.drawable.group_edit, AndroidUtilities.dp(54.0f)));
            this.actionModeViews.add(actionMode.addItemWithWidth(19, R.drawable.ic_ab_reply, AndroidUtilities.dp(54.0f)));
            this.actionModeViews.add(actionMode.addItemWithWidth(22, R.drawable.ic_ab_fave, AndroidUtilities.dp(54.0f)));
            this.actionModeViews.add(actionMode.addItemWithWidth(10, R.drawable.ic_ab_copy, AndroidUtilities.dp(54.0f)));
            this.actionModeViews.add(actionMode.addItemWithWidth(12, R.drawable.ic_ab_delete, AndroidUtilities.dp(54.0f)));
        }
        ActionBarMenuItem item = actionMode.getItem(23);
        int i = (this.canEditMessagesCount == 1 && this.selectedMessagesIds[0].size() + this.selectedMessagesIds[1].size() == 1) ? 0 : 8;
        item.setVisibility(i);
        actionMode.getItem(10).setVisibility(this.selectedMessagesCanCopyIds[0].size() + this.selectedMessagesCanCopyIds[1].size() != 0 ? 0 : 8);
        actionMode.getItem(22).setVisibility(this.selectedMessagesCanStarIds[0].size() + this.selectedMessagesCanStarIds[1].size() != 0 ? 0 : 8);
        actionMode.getItem(12).setVisibility(this.cantDeleteMessagesCount == 0 ? 0 : 8);
        checkActionBarMenu();
        this.fragmentView = new SizeNotifierFrameLayout(context) {
            int inputFieldHeight = 0;

            protected void onAttachedToWindow() {
                super.onAttachedToWindow();
                MessageObject messageObject = MediaController.getInstance().getPlayingMessageObject();
                if (messageObject != null && messageObject.isRoundVideo() && messageObject.eventId == 0 && messageObject.getDialogId() == ChatActivity.this.dialog_id) {
                    MediaController.getInstance().setTextureView(ChatActivity.this.createTextureView(false), ChatActivity.this.aspectRatioFrameLayout, ChatActivity.this.roundVideoContainer, true);
                }
            }

            protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
                boolean result;
                MessageObject messageObject = MediaController.getInstance().getPlayingMessageObject();
                boolean isRoundVideo = messageObject != null && messageObject.eventId == 0 && messageObject.isRoundVideo();
                if (!isRoundVideo || child != ChatActivity.this.roundVideoContainer) {
                    result = super.drawChild(canvas, child, drawingTime);
                    if (isRoundVideo && child == ChatActivity.this.chatListView && messageObject.type != 5 && ChatActivity.this.roundVideoContainer != null) {
                        super.drawChild(canvas, ChatActivity.this.roundVideoContainer, drawingTime);
                        if (ChatActivity.this.drawLaterRoundProgressCell != null) {
                            canvas.save();
                            canvas.translate(ChatActivity.this.drawLaterRoundProgressCell.getX(), (float) (ChatActivity.this.drawLaterRoundProgressCell.getTop() + ChatActivity.this.chatListView.getTop()));
                            ChatActivity.this.drawLaterRoundProgressCell.drawRoundProgress(canvas);
                            canvas.restore();
                        }
                    }
                } else if (messageObject.type == 5) {
                    if (Theme.chat_roundVideoShadow != null && ChatActivity.this.aspectRatioFrameLayout.isDrawingReady()) {
                        int x = ((int) child.getX()) - AndroidUtilities.dp(3.0f);
                        int y = ((int) child.getY()) - AndroidUtilities.dp(2.0f);
                        Theme.chat_roundVideoShadow.setAlpha(255);
                        Theme.chat_roundVideoShadow.setBounds(x, y, (AndroidUtilities.roundMessageSize + x) + AndroidUtilities.dp(6.0f), (AndroidUtilities.roundMessageSize + y) + AndroidUtilities.dp(6.0f));
                        Theme.chat_roundVideoShadow.draw(canvas);
                    }
                    result = super.drawChild(canvas, child, drawingTime);
                } else {
                    result = false;
                }
                if (child == ChatActivity.this.actionBar && ChatActivity.this.parentLayout != null) {
                    int i;
                    ActionBarLayout access$7800 = ChatActivity.this.parentLayout;
                    if (ChatActivity.this.actionBar.getVisibility() == 0) {
                        int measuredHeight = ChatActivity.this.actionBar.getMeasuredHeight();
                        i = (!ChatActivity.this.inPreviewMode || VERSION.SDK_INT < 21) ? 0 : AndroidUtilities.statusBarHeight;
                        i += measuredHeight;
                    } else {
                        i = 0;
                    }
                    access$7800.drawHeaderShadow(canvas, i);
                }
                return result;
            }

            protected boolean isActionBarVisible() {
                return ChatActivity.this.actionBar.getVisibility() == 0;
            }

            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                int allHeight = MeasureSpec.getSize(heightMeasureSpec);
                int heightSize = allHeight;
                setMeasuredDimension(widthSize, heightSize);
                heightSize -= getPaddingTop();
                measureChildWithMargins(ChatActivity.this.actionBar, widthMeasureSpec, 0, heightMeasureSpec, 0);
                int actionBarHeight = ChatActivity.this.actionBar.getMeasuredHeight();
                if (ChatActivity.this.actionBar.getVisibility() == 0) {
                    heightSize -= actionBarHeight;
                }
                if (getKeyboardHeight() <= AndroidUtilities.dp(20.0f) && !AndroidUtilities.isInMultiwindow) {
                    heightSize -= ChatActivity.this.chatActivityEnterView.getEmojiPadding();
                    allHeight -= ChatActivity.this.chatActivityEnterView.getEmojiPadding();
                }
                int childCount = getChildCount();
                measureChildWithMargins(ChatActivity.this.chatActivityEnterView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                if (ChatActivity.this.inPreviewMode) {
                    this.inputFieldHeight = 0;
                } else {
                    this.inputFieldHeight = ChatActivity.this.chatActivityEnterView.getMeasuredHeight();
                }
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    if (!(child == null || child.getVisibility() == 8 || child == ChatActivity.this.chatActivityEnterView || child == ChatActivity.this.actionBar)) {
                        if (child == ChatActivity.this.chatListView || child == ChatActivity.this.progressView) {
                            int contentWidthSpec = MeasureSpec.makeMeasureSpec(widthSize, 1073741824);
                            int dp = AndroidUtilities.dp(10.0f);
                            int i2 = heightSize - this.inputFieldHeight;
                            int i3 = (!ChatActivity.this.inPreviewMode || VERSION.SDK_INT < 21) ? 0 : AndroidUtilities.statusBarHeight;
                            child.measure(contentWidthSpec, MeasureSpec.makeMeasureSpec(Math.max(dp, AndroidUtilities.dp((float) ((ChatActivity.this.chatActivityEnterView.isTopViewVisible() ? 48 : 0) + 2)) + (i2 - i3)), 1073741824));
                        } else if (child == ChatActivity.this.instantCameraView || child == ChatActivity.this.overlayView) {
                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec((allHeight - this.inputFieldHeight) + AndroidUtilities.dp(3.0f), 1073741824));
                        } else if (child == ChatActivity.this.emptyViewContainer) {
                            child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(heightSize, 1073741824));
                        } else if (ChatActivity.this.chatActivityEnterView.isPopupView(child)) {
                            if (!AndroidUtilities.isInMultiwindow) {
                                height = child.getLayoutParams().height;
                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                            } else if (AndroidUtilities.isTablet()) {
                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(Math.min(AndroidUtilities.dp(320.0f), (((heightSize - this.inputFieldHeight) + actionBarHeight) - AndroidUtilities.statusBarHeight) + getPaddingTop()), 1073741824));
                            } else {
                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec((((heightSize - this.inputFieldHeight) + actionBarHeight) - AndroidUtilities.statusBarHeight) + getPaddingTop(), 1073741824));
                            }
                        } else if (child == ChatActivity.this.mentionContainer) {
                            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ChatActivity.this.mentionContainer.getLayoutParams();
                            if (ChatActivity.this.mentionsAdapter.isBannedInline()) {
                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(heightSize, Integer.MIN_VALUE));
                            } else {
                                ChatActivity.this.mentionListViewIgnoreLayout = true;
                                int maxHeight;
                                int padding;
                                if (ChatActivity.this.mentionsAdapter.isBotContext() && ChatActivity.this.mentionsAdapter.isMediaLayout()) {
                                    maxHeight = ChatActivity.this.mentionGridLayoutManager.getRowsCount(widthSize) * 102;
                                    if (ChatActivity.this.mentionsAdapter.isBotContext() && ChatActivity.this.mentionsAdapter.getBotContextSwitch() != null) {
                                        maxHeight += 34;
                                    }
                                    height = (heightSize - ChatActivity.this.chatActivityEnterView.getMeasuredHeight()) + (maxHeight != 0 ? AndroidUtilities.dp(2.0f) : 0);
                                    padding = Math.max(0, height - AndroidUtilities.dp(Math.min((float) maxHeight, 122.399994f)));
                                    if (ChatActivity.this.mentionLayoutManager.getReverseLayout()) {
                                        ChatActivity.this.mentionListView.setPadding(0, 0, 0, padding);
                                    } else {
                                        ChatActivity.this.mentionListView.setPadding(0, padding, 0, 0);
                                    }
                                } else {
                                    int size = ChatActivity.this.mentionsAdapter.getItemCount();
                                    maxHeight = 0;
                                    if (ChatActivity.this.mentionsAdapter.isBotContext()) {
                                        if (ChatActivity.this.mentionsAdapter.getBotContextSwitch() != null) {
                                            maxHeight = 0 + 36;
                                            size--;
                                        }
                                        maxHeight += size * 68;
                                    } else {
                                        maxHeight = 0 + (size * 36);
                                    }
                                    height = (heightSize - ChatActivity.this.chatActivityEnterView.getMeasuredHeight()) + (maxHeight != 0 ? AndroidUtilities.dp(2.0f) : 0);
                                    padding = Math.max(0, height - AndroidUtilities.dp(Math.min((float) maxHeight, 122.399994f)));
                                    if (ChatActivity.this.mentionLayoutManager.getReverseLayout()) {
                                        ChatActivity.this.mentionListView.setPadding(0, 0, 0, padding);
                                    } else {
                                        ChatActivity.this.mentionListView.setPadding(0, padding, 0, 0);
                                    }
                                }
                                layoutParams.height = height;
                                layoutParams.topMargin = 0;
                                ChatActivity.this.mentionListViewIgnoreLayout = false;
                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(layoutParams.height, 1073741824));
                            }
                        } else {
                            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                        }
                    }
                }
                if (ChatActivity.this.fixPaddingsInLayout) {
                    ChatActivity.this.globalIgnoreLayout = true;
                    ChatActivity.this.checkListViewPaddingsInternal();
                    ChatActivity.this.fixPaddingsInLayout = false;
                    ChatActivity.this.globalIgnoreLayout = false;
                }
                if (ChatActivity.this.scrollToPositionOnRecreate != -1) {
                    AndroidUtilities.runOnUIThread(new ChatActivity$7$$Lambda$0(this, ChatActivity.this.scrollToPositionOnRecreate));
                    ChatActivity.this.globalIgnoreLayout = true;
                    ChatActivity.this.scrollToPositionOnRecreate = -1;
                    ChatActivity.this.globalIgnoreLayout = false;
                }
            }

            final /* synthetic */ void lambda$onMeasure$0$ChatActivity$7(int scrollTo) {
                ChatActivity.this.chatLayoutManager.scrollToPositionWithOffset(scrollTo, ChatActivity.this.scrollToOffsetOnRecreate);
            }

            public void requestLayout() {
                if (!ChatActivity.this.globalIgnoreLayout) {
                    super.requestLayout();
                }
            }

            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                int count = getChildCount();
                int paddingBottom = (getKeyboardHeight() > AndroidUtilities.dp(20.0f) || AndroidUtilities.isInMultiwindow) ? 0 : ChatActivity.this.chatActivityEnterView.getEmojiPadding();
                setBottomClip(paddingBottom);
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() != 8) {
                        int childLeft;
                        int childTop;
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
                        int width = child.getMeasuredWidth();
                        int height = child.getMeasuredHeight();
                        int gravity = lp.gravity;
                        if (gravity == -1) {
                            gravity = 51;
                        }
                        int verticalGravity = gravity & 112;
                        switch ((gravity & 7) & 7) {
                            case 1:
                                childLeft = ((((r - l) - width) / 2) + lp.leftMargin) - lp.rightMargin;
                                break;
                            case 5:
                                childLeft = (r - width) - lp.rightMargin;
                                break;
                            default:
                                childLeft = lp.leftMargin;
                                break;
                        }
                        switch (verticalGravity) {
                            case 16:
                                childTop = (((((b - paddingBottom) - t) - height) / 2) + lp.topMargin) - lp.bottomMargin;
                                break;
                            case 48:
                                childTop = lp.topMargin + getPaddingTop();
                                if (child != ChatActivity.this.actionBar && ChatActivity.this.actionBar.getVisibility() == 0) {
                                    childTop += ChatActivity.this.actionBar.getMeasuredHeight();
                                    if (ChatActivity.this.inPreviewMode && VERSION.SDK_INT >= 21) {
                                        childTop += AndroidUtilities.statusBarHeight;
                                        break;
                                    }
                                }
                            case 80:
                                childTop = (((b - paddingBottom) - t) - height) - lp.bottomMargin;
                                break;
                            default:
                                childTop = lp.topMargin;
                                break;
                        }
                        if (child == ChatActivity.this.mentionContainer) {
                            childTop -= ChatActivity.this.chatActivityEnterView.getMeasuredHeight() - AndroidUtilities.dp(2.0f);
                        } else if (child == ChatActivity.this.pagedownButton) {
                            if (!ChatActivity.this.inPreviewMode) {
                                childTop -= ChatActivity.this.chatActivityEnterView.getMeasuredHeight();
                            }
                        } else if (child == ChatActivity.this.mentiondownButton) {
                            if (!ChatActivity.this.inPreviewMode) {
                                childTop -= ChatActivity.this.chatActivityEnterView.getMeasuredHeight();
                            }
                        } else if (child == ChatActivity.this.emptyViewContainer) {
                            childTop -= (this.inputFieldHeight / 2) - (ChatActivity.this.actionBar.getVisibility() == 0 ? ChatActivity.this.actionBar.getMeasuredHeight() / 2 : 0);
                        } else if (ChatActivity.this.chatActivityEnterView.isPopupView(child)) {
                            if (AndroidUtilities.isInMultiwindow) {
                                childTop = (ChatActivity.this.chatActivityEnterView.getTop() - child.getMeasuredHeight()) + AndroidUtilities.dp(1.0f);
                            } else {
                                childTop = ChatActivity.this.chatActivityEnterView.getBottom();
                            }
                        } else if (child == ChatActivity.this.gifHintTextView || child == ChatActivity.this.voiceHintTextView || child == ChatActivity.this.mediaBanTooltip) {
                            childTop -= this.inputFieldHeight;
                        } else if (child == ChatActivity.this.chatListView || child == ChatActivity.this.progressView) {
                            if (ChatActivity.this.chatActivityEnterView.isTopViewVisible()) {
                                childTop -= AndroidUtilities.dp(48.0f);
                            }
                        } else if (child == ChatActivity.this.actionBar) {
                            if (ChatActivity.this.inPreviewMode && VERSION.SDK_INT >= 21) {
                                childTop += AndroidUtilities.statusBarHeight;
                            }
                            childTop -= getPaddingTop();
                        } else if (child == ChatActivity.this.roundVideoContainer) {
                            childTop = ChatActivity.this.actionBar.getMeasuredHeight();
                        } else if (child == ChatActivity.this.instantCameraView || child == ChatActivity.this.overlayView) {
                            childTop = 0;
                        }
                        child.layout(childLeft, childTop, childLeft + width, childTop + height);
                    }
                }
                ChatActivity.this.updateMessagesVisisblePart(true);
                notifyHeightChanged();
            }
        };
        this.contentView = (SizeNotifierFrameLayout) this.fragmentView;
        this.contentView.setBackgroundImage(Theme.getCachedWallpaper());
        this.emptyViewContainer = new FrameLayout(context);
        this.emptyViewContainer.setVisibility(4);
        this.contentView.addView(this.emptyViewContainer, LayoutHelper.createFrame(-1, -2, 17));
        this.emptyViewContainer.setOnTouchListener(ChatActivity$$Lambda$5.$instance);
        if (this.currentEncryptedChat != null) {
            this.bigEmptyView = new ChatBigEmptyView(context, true);
            if (this.currentEncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                this.bigEmptyView.setSecretText(LocaleController.formatString("EncryptedPlaceholderTitleOutgoing", R.string.EncryptedPlaceholderTitleOutgoing, UserObject.getFirstName(this.currentUser)));
            } else {
                this.bigEmptyView.setSecretText(LocaleController.formatString("EncryptedPlaceholderTitleIncoming", R.string.EncryptedPlaceholderTitleIncoming, UserObject.getFirstName(this.currentUser)));
            }
            this.emptyViewContainer.addView(this.bigEmptyView, new FrameLayout.LayoutParams(-2, -2, 17));
        } else if (this.currentUser == null || !this.currentUser.self) {
            this.emptyView = new TextView(context);
            if (this.currentUser == null || this.currentUser.id == 777000 || this.currentUser.id == 429000 || this.currentUser.id == 4244000 || !MessagesController.isSupportId(this.currentUser.id)) {
                this.emptyView.setText(LocaleController.getString("NoMessages", R.string.NoMessages));
            } else {
                this.emptyView.setText(LocaleController.getString("GotAQuestion", R.string.GotAQuestion));
            }
            this.emptyView.setTextSize(1, 14.0f);
            this.emptyView.setGravity(17);
            this.emptyView.setTextColor(Theme.getColor(Theme.key_chat_serviceText));
            this.emptyView.setBackgroundResource(R.drawable.system);
            this.emptyView.getBackground().setColorFilter(Theme.colorFilter);
            this.emptyView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.emptyView.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(2.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(3.0f));
            this.emptyViewContainer.addView(this.emptyView, new FrameLayout.LayoutParams(-2, -2, 17));
        } else {
            this.bigEmptyView = new ChatBigEmptyView(context, false);
            this.emptyViewContainer.addView(this.bigEmptyView, new FrameLayout.LayoutParams(-2, -2, 17));
        }
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.onDestroy();
            if (this.chatActivityEnterView.isEditingMessage()) {
                oldMessage = null;
            } else {
                oldMessage = this.chatActivityEnterView.getFieldText();
            }
        } else {
            oldMessage = null;
        }
        if (this.mentionsAdapter != null) {
            this.mentionsAdapter.onDestroy();
        }
        this.chatListView = new RecyclerListView(context) {
            ArrayList<ChatMessageCell> drawCaptionAfter = new ArrayList();
            ArrayList<ChatMessageCell> drawNamesAfter = new ArrayList();
            ArrayList<ChatMessageCell> drawTimeAfter = new ArrayList();
            private float endedTrackingX;
            private long lastReplyButtonAnimationTime;
            private long lastTrackingAnimationTime;
            private boolean maybeStartTracking;
            private float replyButtonProgress;
            private boolean slideAnimationInProgress;
            private ChatMessageCell slidingView;
            private boolean startedTracking;
            private int startedTrackingPointerId;
            private int startedTrackingX;
            private int startedTrackingY;
            private float trackAnimationProgress;
            private boolean wasTrackingVibrate;

            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                super.onLayout(changed, l, t, r, b);
                ChatActivity.this.forceScrollToTop = false;
                if (ChatActivity.this.chatAdapter.isBot) {
                    int childCount = getChildCount();
                    for (int a = 0; a < childCount; a++) {
                        View child = getChildAt(a);
                        if (child instanceof BotHelpCell) {
                            int top = ((b - t) / 2) - (child.getMeasuredHeight() / 2);
                            if (child.getTop() > top) {
                                child.layout(0, top, r - l, child.getMeasuredHeight() + top);
                                return;
                            }
                            return;
                        }
                    }
                }
            }

            private void setGroupTranslationX(ChatMessageCell view, float dx) {
                GroupedMessages group = view.getCurrentMessagesGroup();
                if (group != null) {
                    int count = getChildCount();
                    for (int a = 0; a < count; a++) {
                        View child = getChildAt(a);
                        if (child != this && (child instanceof ChatMessageCell)) {
                            ChatMessageCell cell = (ChatMessageCell) child;
                            if (cell.getCurrentMessagesGroup() == group) {
                                cell.setTranslationX(dx);
                                cell.invalidate();
                            }
                        }
                    }
                    invalidate();
                }
            }

            public boolean onInterceptTouchEvent(MotionEvent e) {
                boolean result = super.onInterceptTouchEvent(e);
                if (!ChatActivity.this.actionBar.isActionModeShowed()) {
                    processTouchEvent(e);
                }
                return result;
            }

            private void drawReplyButton(Canvas canvas) {
                if (this.slidingView != null) {
                    float scale;
                    int alpha;
                    float translationX = this.slidingView.getTranslationX();
                    long newTime = System.currentTimeMillis();
                    long dt = Math.min(17, newTime - this.lastReplyButtonAnimationTime);
                    this.lastReplyButtonAnimationTime = newTime;
                    boolean showing = translationX <= ((float) (-AndroidUtilities.dp(50.0f)));
                    if (showing) {
                        if (this.replyButtonProgress < 1.0f) {
                            this.replyButtonProgress += ((float) dt) / 180.0f;
                            if (this.replyButtonProgress > 1.0f) {
                                this.replyButtonProgress = 1.0f;
                            } else {
                                invalidate();
                            }
                        }
                    } else if (this.replyButtonProgress > 0.0f) {
                        this.replyButtonProgress -= ((float) dt) / 180.0f;
                        if (this.replyButtonProgress < 0.0f) {
                            this.replyButtonProgress = 0.0f;
                        } else {
                            invalidate();
                        }
                    }
                    if (showing) {
                        if (this.replyButtonProgress <= 0.8f) {
                            scale = 1.2f * (this.replyButtonProgress / 0.8f);
                        } else {
                            scale = 1.2f - (0.2f * ((this.replyButtonProgress - 0.8f) / 0.2f));
                        }
                        alpha = (int) Math.min(255.0f, 255.0f * (this.replyButtonProgress / 0.8f));
                    } else {
                        scale = this.replyButtonProgress;
                        alpha = (int) Math.min(255.0f, 255.0f * this.replyButtonProgress);
                    }
                    Theme.chat_shareDrawable.setAlpha(alpha);
                    Theme.chat_replyIconDrawable.setAlpha(alpha);
                    float x = ((float) getMeasuredWidth()) + (this.slidingView.getTranslationX() / 2.0f);
                    float y = (float) (this.slidingView.getTop() + (this.slidingView.getMeasuredHeight() / 2));
                    Theme.chat_shareDrawable.setColorFilter(Theme.colorFilter);
                    Theme.chat_shareDrawable.setBounds((int) (x - (((float) AndroidUtilities.dp(16.0f)) * scale)), (int) (y - (((float) AndroidUtilities.dp(16.0f)) * scale)), (int) ((((float) AndroidUtilities.dp(16.0f)) * scale) + x), (int) ((((float) AndroidUtilities.dp(16.0f)) * scale) + y));
                    Theme.chat_shareDrawable.draw(canvas);
                    Theme.chat_replyIconDrawable.setBounds((int) (x - (((float) AndroidUtilities.dp(10.0f)) * scale)), (int) (y - (((float) AndroidUtilities.dp(8.0f)) * scale)), (int) ((((float) AndroidUtilities.dp(10.0f)) * scale) + x), (int) ((((float) AndroidUtilities.dp(6.0f)) * scale) + y));
                    Theme.chat_replyIconDrawable.draw(canvas);
                    Theme.chat_shareDrawable.setAlpha(255);
                    Theme.chat_replyIconDrawable.setAlpha(255);
                }
            }

            private void processTouchEvent(MotionEvent e) {
                if (e.getAction() == 0 && !this.startedTracking && !this.maybeStartTracking) {
                    View view = getPressedChildView();
                    if (view instanceof ChatMessageCell) {
                        this.slidingView = (ChatMessageCell) view;
                        MessageObject message = this.slidingView.getMessageObject();
                        if ((ChatActivity.this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(ChatActivity.this.currentEncryptedChat.layer) >= 46) && (!(ChatActivity.this.getMessageType(message) == 1 && (message.getDialogId() == ChatActivity.this.mergeDialogId || message.needDrawBluredPreview())) && ((ChatActivity.this.currentEncryptedChat != null || message.getId() >= 0) && ((ChatActivity.this.bottomOverlayChat == null || ChatActivity.this.bottomOverlayChat.getVisibility() != 0) && !ChatActivity.this.isBroadcast && (ChatActivity.this.currentChat == null || (!ChatObject.isNotInChat(ChatActivity.this.currentChat) && ((!ChatObject.isChannel(ChatActivity.this.currentChat) || ChatObject.canPost(ChatActivity.this.currentChat) || ChatActivity.this.currentChat.megagroup) && ChatObject.canSendMessages(ChatActivity.this.currentChat)))))))) {
                            this.startedTrackingPointerId = e.getPointerId(0);
                            this.maybeStartTracking = true;
                            this.startedTrackingX = (int) e.getX();
                            this.startedTrackingY = (int) e.getY();
                            return;
                        }
                        this.slidingView = null;
                    }
                } else if (this.slidingView != null && e.getAction() == 2 && e.getPointerId(0) == this.startedTrackingPointerId) {
                    int dx = Math.max(AndroidUtilities.dp(-80.0f), Math.min(0, (int) (e.getX() - ((float) this.startedTrackingX))));
                    int dy = Math.abs(((int) e.getY()) - this.startedTrackingY);
                    if (getScrollState() == 0 && this.maybeStartTracking && !this.startedTracking && ((float) dx) <= (-AndroidUtilities.getPixelsInCM(0.4f, true)) && Math.abs(dx) / 3 > dy) {
                        MotionEvent event = MotionEvent.obtain(0, 0, 3, 0.0f, 0.0f, 0);
                        this.slidingView.onTouchEvent(event);
                        super.onInterceptTouchEvent(event);
                        event.recycle();
                        ChatActivity.this.chatLayoutManager.setCanScrollVertically(false);
                        this.maybeStartTracking = false;
                        this.startedTracking = true;
                        this.startedTrackingX = (int) e.getX();
                        if (getParent() != null) {
                            getParent().requestDisallowInterceptTouchEvent(true);
                        }
                    } else if (this.startedTracking) {
                        if (Math.abs(dx) < AndroidUtilities.dp(50.0f)) {
                            this.wasTrackingVibrate = false;
                        } else if (!this.wasTrackingVibrate) {
                            try {
                                performHapticFeedback(3, 2);
                            } catch (Exception e2) {
                            }
                            this.wasTrackingVibrate = true;
                        }
                        this.slidingView.setTranslationX((float) dx);
                        if (this.slidingView.getMessageObject().isRoundVideo()) {
                            ChatActivity.this.updateTextureViewPosition();
                        }
                        setGroupTranslationX(this.slidingView, (float) dx);
                        invalidate();
                    }
                } else if (this.slidingView != null && e.getPointerId(0) == this.startedTrackingPointerId) {
                    if (e.getAction() == 3 || e.getAction() == 1 || e.getAction() == 6) {
                        if (Math.abs(this.slidingView.getTranslationX()) >= ((float) AndroidUtilities.dp(50.0f))) {
                            ChatActivity.this.showFieldPanelForReply(true, this.slidingView.getMessageObject());
                        }
                        this.endedTrackingX = this.slidingView.getTranslationX();
                        this.lastTrackingAnimationTime = System.currentTimeMillis();
                        this.trackAnimationProgress = 0.0f;
                        invalidate();
                        this.maybeStartTracking = false;
                        this.startedTracking = false;
                        ChatActivity.this.chatLayoutManager.setCanScrollVertically(true);
                    }
                }
            }

            public boolean onTouchEvent(MotionEvent e) {
                boolean result = super.onTouchEvent(e);
                if (ChatActivity.this.actionBar.isActionModeShowed()) {
                    return result;
                }
                processTouchEvent(e);
                boolean z = this.startedTracking || result;
                return z;
            }

            public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                super.requestDisallowInterceptTouchEvent(disallowIntercept);
                if (this.slidingView != null) {
                    this.endedTrackingX = this.slidingView.getTranslationX();
                    this.lastTrackingAnimationTime = System.currentTimeMillis();
                    this.trackAnimationProgress = 0.0f;
                    invalidate();
                    this.maybeStartTracking = false;
                    this.startedTracking = false;
                    ChatActivity.this.chatLayoutManager.setCanScrollVertically(true);
                }
            }

            protected void onChildPressed(View child, boolean pressed) {
                super.onChildPressed(child, pressed);
                if (child instanceof ChatMessageCell) {
                    GroupedMessages groupedMessages = ((ChatMessageCell) child).getCurrentMessagesGroup();
                    if (groupedMessages != null) {
                        int count = getChildCount();
                        for (int a = 0; a < count; a++) {
                            View item = getChildAt(a);
                            if (item != child && (item instanceof ChatMessageCell)) {
                                ChatMessageCell cell = (ChatMessageCell) item;
                                if (((ChatMessageCell) item).getCurrentMessagesGroup() == groupedMessages) {
                                    cell.setPressed(pressed);
                                }
                            }
                        }
                    }
                }
            }

            public void requestLayout() {
                if (!ChatActivity.this.chatListViewIgnoreLayout && !ChatActivity.this.globalIgnoreLayout) {
                    super.requestLayout();
                }
            }

            public void onDraw(Canvas c) {
                super.onDraw(c);
                if (this.slidingView != null) {
                    float translationX = this.slidingView.getTranslationX();
                    if (!(this.maybeStartTracking || this.startedTracking || this.endedTrackingX == 0.0f || translationX == 0.0f)) {
                        long newTime = System.currentTimeMillis();
                        this.trackAnimationProgress += ((float) (newTime - this.lastTrackingAnimationTime)) / 180.0f;
                        if (this.trackAnimationProgress > 1.0f) {
                            this.trackAnimationProgress = 1.0f;
                        }
                        this.lastTrackingAnimationTime = newTime;
                        translationX = this.endedTrackingX * (1.0f - AndroidUtilities.decelerateInterpolator.getInterpolation(this.trackAnimationProgress));
                        if (translationX == 0.0f) {
                            this.endedTrackingX = 0.0f;
                        }
                        setGroupTranslationX(this.slidingView, translationX);
                        this.slidingView.setTranslationX(translationX);
                        if (this.slidingView.getMessageObject().isRoundVideo()) {
                            ChatActivity.this.updateTextureViewPosition();
                        }
                        invalidate();
                    }
                    drawReplyButton(c);
                }
            }

            protected void dispatchDraw(Canvas canvas) {
                ChatActivity.this.drawLaterRoundProgressCell = null;
                super.dispatchDraw(canvas);
            }

            public boolean drawChild(Canvas canvas, View child, long drawingTime) {
                ChatMessageCell cell;
                GroupedMessagePosition position;
                int a;
                int size;
                int clipLeft = 0;
                int clipBottom = 0;
                if (child instanceof ChatMessageCell) {
                    cell = (ChatMessageCell) child;
                    position = cell.getCurrentPosition();
                    GroupedMessages group = cell.getCurrentMessagesGroup();
                    if (position != null) {
                        if (position.pw != position.spanSize && position.spanSize == ChatActivity.id_chat_compose_panel && position.siblingHeights == null && group.hasSibling) {
                            clipLeft = cell.getBackgroundDrawableLeft();
                        } else if (position.siblingHeights != null) {
                            clipBottom = child.getBottom() - AndroidUtilities.dp((float) ((cell.isPinnedBottom() ? 1 : 0) + 1));
                        }
                    }
                    if (cell.needDelayRoundProgressDraw()) {
                        ChatActivity.this.drawLaterRoundProgressCell = cell;
                    }
                }
                if (clipLeft != 0) {
                    canvas.save();
                    canvas.clipRect(((float) clipLeft) + child.getTranslationX(), (float) child.getTop(), ((float) child.getRight()) + child.getTranslationX(), (float) child.getBottom());
                } else if (clipBottom != 0) {
                    canvas.save();
                    canvas.clipRect(((float) child.getLeft()) + child.getTranslationX(), (float) child.getTop(), ((float) child.getRight()) + child.getTranslationX(), (float) clipBottom);
                }
                boolean result = super.drawChild(canvas, child, drawingTime);
                if (!(clipLeft == 0 && clipBottom == 0)) {
                    canvas.restore();
                }
                int num = 0;
                int count = getChildCount();
                for (a = 0; a < count; a++) {
                    if (getChildAt(a) == child) {
                        num = a;
                        break;
                    }
                }
                if (num == count - 1) {
                    size = this.drawTimeAfter.size();
                    if (size > 0) {
                        for (a = 0; a < size; a++) {
                            cell = (ChatMessageCell) this.drawTimeAfter.get(a);
                            canvas.save();
                            canvas.translate(((float) cell.getLeft()) + cell.getTranslationX(), (float) cell.getTop());
                            cell.drawTimeLayout(canvas);
                            canvas.restore();
                        }
                        this.drawTimeAfter.clear();
                    }
                    size = this.drawNamesAfter.size();
                    if (size > 0) {
                        for (a = 0; a < size; a++) {
                            cell = (ChatMessageCell) this.drawNamesAfter.get(a);
                            canvas.save();
                            canvas.translate(((float) cell.getLeft()) + cell.getTranslationX(), (float) cell.getTop());
                            cell.drawNamesLayout(canvas);
                            canvas.restore();
                        }
                        this.drawNamesAfter.clear();
                    }
                    size = this.drawCaptionAfter.size();
                    if (size > 0) {
                        for (a = 0; a < size; a++) {
                            cell = (ChatMessageCell) this.drawCaptionAfter.get(a);
                            if (cell.getCurrentPosition() != null) {
                                canvas.save();
                                canvas.translate(((float) cell.getLeft()) + cell.getTranslationX(), (float) cell.getTop());
                                cell.drawCaptionLayout(canvas, (cell.getCurrentPosition().flags & 1) == 0);
                                canvas.restore();
                            }
                        }
                        this.drawCaptionAfter.clear();
                    }
                }
                if (child instanceof ChatMessageCell) {
                    ImageReceiver imageReceiver;
                    ChatMessageCell chatMessageCell = (ChatMessageCell) child;
                    position = chatMessageCell.getCurrentPosition();
                    if (position != null) {
                        if (position.last || (position.minX == (byte) 0 && position.minY == (byte) 0)) {
                            if (num == count - 1) {
                                canvas.save();
                                canvas.translate(((float) chatMessageCell.getLeft()) + chatMessageCell.getTranslationX(), (float) chatMessageCell.getTop());
                                if (position.last) {
                                    chatMessageCell.drawTimeLayout(canvas);
                                }
                                if (position.minX == (byte) 0 && position.minY == (byte) 0) {
                                    chatMessageCell.drawNamesLayout(canvas);
                                }
                                canvas.restore();
                            } else {
                                if (position.last) {
                                    this.drawTimeAfter.add(chatMessageCell);
                                }
                                if (position.minX == (byte) 0 && position.minY == (byte) 0 && chatMessageCell.hasNameLayout()) {
                                    this.drawNamesAfter.add(chatMessageCell);
                                }
                            }
                        }
                        if (num == count - 1) {
                            canvas.save();
                            canvas.translate(((float) chatMessageCell.getLeft()) + chatMessageCell.getTranslationX(), (float) chatMessageCell.getTop());
                            if (chatMessageCell.hasCaptionLayout() && (position.flags & 8) != 0) {
                                chatMessageCell.drawCaptionLayout(canvas, (position.flags & 1) == 0);
                            }
                            canvas.restore();
                        } else if (chatMessageCell.hasCaptionLayout() && (position.flags & 8) != 0) {
                            this.drawCaptionAfter.add(chatMessageCell);
                        }
                    }
                    MessageObject message = chatMessageCell.getMessageObject();
                    if (ChatActivity.this.roundVideoContainer != null && message.isRoundVideo() && MediaController.getInstance().isPlayingMessage(message)) {
                        imageReceiver = chatMessageCell.getPhotoImage();
                        float newX = ((float) imageReceiver.getImageX()) + chatMessageCell.getTranslationX();
                        float newY = (float) ((ChatActivity.this.inPreviewMode ? AndroidUtilities.statusBarHeight : 0) + (((ChatActivity.this.fragmentView.getPaddingTop() + chatMessageCell.getTop()) + imageReceiver.getImageY()) - (ChatActivity.this.chatActivityEnterView.isTopViewVisible() ? AndroidUtilities.dp(48.0f) : 0)));
                        if (!(ChatActivity.this.roundVideoContainer.getTranslationX() == newX && ChatActivity.this.roundVideoContainer.getTranslationY() == newY)) {
                            ChatActivity.this.roundVideoContainer.setTranslationX(newX);
                            ChatActivity.this.roundVideoContainer.setTranslationY(newY);
                            ChatActivity.this.fragmentView.invalidate();
                            ChatActivity.this.roundVideoContainer.invalidate();
                        }
                    }
                    imageReceiver = chatMessageCell.getAvatarImage();
                    if (imageReceiver != null) {
                        ViewHolder holder;
                        int p;
                        int idx;
                        GroupedMessages groupedMessages = ChatActivity.this.getValidGroupedMessage(message);
                        int top = child.getTop();
                        if (chatMessageCell.isPinnedBottom()) {
                            holder = ChatActivity.this.chatListView.getChildViewHolder(child);
                            if (holder != null) {
                                int nextPosition;
                                p = holder.getAdapterPosition();
                                if (groupedMessages == null || position == null) {
                                    nextPosition = p - 1;
                                } else {
                                    idx = groupedMessages.posArray.indexOf(position);
                                    size = groupedMessages.posArray.size();
                                    if ((position.flags & 8) != 0) {
                                        nextPosition = (p - size) + idx;
                                    } else {
                                        nextPosition = p - 1;
                                        a = idx + 1;
                                        while (idx < size && ((GroupedMessagePosition) groupedMessages.posArray.get(a)).minY <= position.maxY) {
                                            nextPosition--;
                                            a++;
                                        }
                                    }
                                }
                                if (ChatActivity.this.chatListView.findViewHolderForAdapterPosition(nextPosition) != null) {
                                    imageReceiver.setImageY(-AndroidUtilities.dp(1000.0f));
                                    imageReceiver.draw(canvas);
                                }
                            }
                        }
                        float tx = chatMessageCell.getTranslationX();
                        int y = child.getTop() + chatMessageCell.getLayoutHeight();
                        int maxY = ChatActivity.this.chatListView.getMeasuredHeight() - ChatActivity.this.chatListView.getPaddingBottom();
                        if (y > maxY) {
                            y = maxY;
                        }
                        if (chatMessageCell.isPinnedTop()) {
                            holder = ChatActivity.this.chatListView.getChildViewHolder(child);
                            if (holder != null) {
                                int tries = 0;
                                while (tries < 20) {
                                    int prevPosition;
                                    tries++;
                                    p = holder.getAdapterPosition();
                                    if (groupedMessages != null && position != null) {
                                        idx = groupedMessages.posArray.indexOf(position);
                                        if (idx < 0) {
                                            break;
                                        }
                                        size = groupedMessages.posArray.size();
                                        if ((position.flags & 4) != 0) {
                                            prevPosition = (p + idx) + 1;
                                        } else {
                                            prevPosition = p + 1;
                                            a = idx - 1;
                                            while (idx >= 0 && ((GroupedMessagePosition) groupedMessages.posArray.get(a)).maxY >= position.minY) {
                                                prevPosition++;
                                                a--;
                                            }
                                        }
                                    } else {
                                        prevPosition = p + 1;
                                    }
                                    holder = ChatActivity.this.chatListView.findViewHolderForAdapterPosition(prevPosition);
                                    if (holder == null) {
                                        break;
                                    }
                                    top = holder.itemView.getTop();
                                    if (y - AndroidUtilities.dp(48.0f) < holder.itemView.getBottom()) {
                                        tx = Math.min(holder.itemView.getTranslationX(), tx);
                                    }
                                    if (holder.itemView instanceof ChatMessageCell) {
                                        if (!((ChatMessageCell) holder.itemView).isPinnedTop()) {
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                        }
                        if (y - AndroidUtilities.dp(48.0f) < top) {
                            y = top + AndroidUtilities.dp(48.0f);
                        }
                        if (tx != 0.0f) {
                            canvas.save();
                            canvas.translate(tx, 0.0f);
                        }
                        imageReceiver.setImageY(y - AndroidUtilities.dp(44.0f));
                        imageReceiver.draw(canvas);
                        if (tx != 0.0f) {
                            canvas.restore();
                        }
                    }
                }
                return result;
            }
        };
        this.chatListView.setTag(Integer.valueOf(1));
        this.chatListView.setVerticalScrollBarEnabled(true);
        RecyclerListView recyclerListView = this.chatListView;
        Adapter chatActivityAdapter = new ChatActivityAdapter(context);
        this.chatAdapter = chatActivityAdapter;
        recyclerListView.setAdapter(chatActivityAdapter);
        this.chatListView.setClipToPadding(false);
        this.chatListView.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(3.0f));
        this.chatListView.setItemAnimator(null);
        this.chatListView.setLayoutAnimation(null);
        this.chatLayoutManager = new GridLayoutManagerFixed(context, id_chat_compose_panel, 1, true) {
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }

            public void smoothScrollToPosition(RecyclerView recyclerView, State state, int position) {
                LinearSmoothScrollerMiddle linearSmoothScroller = new LinearSmoothScrollerMiddle(recyclerView.getContext());
                linearSmoothScroller.setTargetPosition(position);
                startSmoothScroll(linearSmoothScroller);
            }

            public boolean shouldLayoutChildFromOpositeSide(View child) {
                if (!(child instanceof ChatMessageCell) || ((ChatMessageCell) child).getMessageObject().isOutOwner()) {
                    return false;
                }
                return true;
            }

            protected boolean hasSiblingChild(int position) {
                if (position < ChatActivity.this.chatAdapter.messagesStartRow || position >= ChatActivity.this.chatAdapter.messagesEndRow) {
                    return false;
                }
                int index = position - ChatActivity.this.chatAdapter.messagesStartRow;
                if (index < 0 || index >= ChatActivity.this.messages.size()) {
                    return false;
                }
                MessageObject message = (MessageObject) ChatActivity.this.messages.get(index);
                GroupedMessages group = ChatActivity.this.getValidGroupedMessage(message);
                if (group == null) {
                    return false;
                }
                GroupedMessagePosition pos = (GroupedMessagePosition) group.positions.get(message);
                if (pos.minX == pos.maxX || pos.minY != pos.maxY || pos.minY == (byte) 0) {
                    return false;
                }
                int count = group.posArray.size();
                for (int a = 0; a < count; a++) {
                    GroupedMessagePosition p = (GroupedMessagePosition) group.posArray.get(a);
                    if (p != pos && p.minY <= pos.minY && p.maxY >= pos.minY) {
                        return true;
                    }
                }
                return false;
            }
        };
        this.chatLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            public int getSpanSize(int position) {
                if (position >= ChatActivity.this.chatAdapter.messagesStartRow && position < ChatActivity.this.chatAdapter.messagesEndRow) {
                    int idx = position - ChatActivity.this.chatAdapter.messagesStartRow;
                    if (idx >= 0 && idx < ChatActivity.this.messages.size()) {
                        MessageObject message = (MessageObject) ChatActivity.this.messages.get(idx);
                        GroupedMessages groupedMessages = ChatActivity.this.getValidGroupedMessage(message);
                        if (groupedMessages != null) {
                            return ((GroupedMessagePosition) groupedMessages.positions.get(message)).spanSize;
                        }
                    }
                }
                return ChatActivity.id_chat_compose_panel;
            }
        });
        this.chatListView.setLayoutManager(this.chatLayoutManager);
        this.chatListView.addItemDecoration(new ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                outRect.bottom = 0;
                if (view instanceof ChatMessageCell) {
                    ChatMessageCell cell = (ChatMessageCell) view;
                    GroupedMessages group = cell.getCurrentMessagesGroup();
                    if (group != null) {
                        GroupedMessagePosition position = cell.getCurrentPosition();
                        if (position != null && position.siblingHeights != null) {
                            int a;
                            float maxHeight = ((float) Math.max(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y)) * 0.5f;
                            int h = cell.getCaptionHeight();
                            for (float f : position.siblingHeights) {
                                h += (int) Math.ceil((double) (f * maxHeight));
                            }
                            h += (position.maxY - position.minY) * AndroidUtilities.dp2(11.0f);
                            int count = group.posArray.size();
                            for (a = 0; a < count; a++) {
                                GroupedMessagePosition pos = (GroupedMessagePosition) group.posArray.get(a);
                                if (pos.minY == position.minY && ((pos.minX != position.minX || pos.maxX != position.maxX || pos.minY != position.minY || pos.maxY != position.maxY) && pos.minY == position.minY)) {
                                    h -= ((int) Math.ceil((double) (pos.ph * maxHeight))) - AndroidUtilities.dp(4.0f);
                                    break;
                                }
                            }
                            outRect.bottom = -h;
                        }
                    }
                }
            }
        });
        this.contentView.addView(this.chatListView, LayoutHelper.createFrame(-1, -1.0f));
        this.chatListView.setOnItemLongClickListener(this.onItemLongClickListener);
        this.chatListView.setOnItemClickListener(this.onItemClickListener);
        this.chatListView.setOnScrollListener(new OnScrollListener() {
            private final int scrollValue = AndroidUtilities.dp(100.0f);
            private float totalDy = 0.0f;

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == 2) {
                    ChatActivity.this.wasManualScroll = true;
                } else if (newState == 1) {
                    ChatActivity.this.wasManualScroll = true;
                    ChatActivity.this.scrollingFloatingDate = true;
                    ChatActivity.this.checkTextureViewPosition = true;
                } else if (newState == 0) {
                    ChatActivity.this.scrollingFloatingDate = false;
                    ChatActivity.this.checkTextureViewPosition = false;
                    ChatActivity.this.hideFloatingDateView(true);
                }
            }

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                ChatActivity.this.chatListView.invalidate();
                if (!(ChatActivity.this.wasManualScroll || dy == 0)) {
                    ChatActivity.this.wasManualScroll = true;
                }
                if (!(dy == 0 || !ChatActivity.this.scrollingFloatingDate || ChatActivity.this.currentFloatingTopIsNotMessage)) {
                    if (ChatActivity.this.highlightMessageId != ConnectionsManager.DEFAULT_DATACENTER_ID) {
                        ChatActivity.this.highlightMessageId = ConnectionsManager.DEFAULT_DATACENTER_ID;
                        ChatActivity.this.updateVisibleRows();
                    }
                    if (ChatActivity.this.floatingDateView.getTag() == null) {
                        if (ChatActivity.this.floatingDateAnimation != null) {
                            ChatActivity.this.floatingDateAnimation.cancel();
                        }
                        ChatActivity.this.floatingDateView.setTag(Integer.valueOf(1));
                        ChatActivity.this.floatingDateAnimation = new AnimatorSet();
                        ChatActivity.this.floatingDateAnimation.setDuration(150);
                        AnimatorSet access$13700 = ChatActivity.this.floatingDateAnimation;
                        Animator[] animatorArr = new Animator[1];
                        animatorArr[0] = ObjectAnimator.ofFloat(ChatActivity.this.floatingDateView, "alpha", new float[]{1.0f});
                        access$13700.playTogether(animatorArr);
                        ChatActivity.this.floatingDateAnimation.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                if (animation.equals(ChatActivity.this.floatingDateAnimation)) {
                                    ChatActivity.this.floatingDateAnimation = null;
                                }
                            }
                        });
                        ChatActivity.this.floatingDateAnimation.start();
                    }
                }
                ChatActivity.this.checkScrollForLoad(true);
                int firstVisibleItem = ChatActivity.this.chatLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem != -1) {
                    int totalItemCount = ChatActivity.this.chatAdapter.getItemCount();
                    if (firstVisibleItem == 0 && ChatActivity.this.forwardEndReached[0]) {
                        ChatActivity.this.showPagedownButton(false, true);
                    } else if (dy > 0) {
                        if (ChatActivity.this.pagedownButton.getTag() == null) {
                            this.totalDy += (float) dy;
                            if (this.totalDy > ((float) this.scrollValue)) {
                                this.totalDy = 0.0f;
                                ChatActivity.this.showPagedownButton(true, true);
                                ChatActivity.this.pagedownButtonShowedByScroll = true;
                            }
                        }
                    } else if (ChatActivity.this.pagedownButtonShowedByScroll && ChatActivity.this.pagedownButton.getTag() != null) {
                        this.totalDy += (float) dy;
                        if (this.totalDy < ((float) (-this.scrollValue))) {
                            ChatActivity.this.showPagedownButton(false, true);
                            this.totalDy = 0.0f;
                        }
                    }
                }
                ChatActivity.this.updateMessagesVisisblePart(true);
            }
        });
        this.progressView = new FrameLayout(context);
        this.progressView.setVisibility(4);
        this.contentView.addView(this.progressView, LayoutHelper.createFrame(-1, -1, 51));
        this.progressView2 = new View(context);
        this.progressView2.setBackgroundResource(R.drawable.system_loader);
        this.progressView2.getBackground().setColorFilter(Theme.colorFilter);
        this.progressView.addView(this.progressView2, LayoutHelper.createFrame(36, 36, 17));
        this.progressBar = new RadialProgressView(context);
        this.progressBar.setSize(AndroidUtilities.dp(28.0f));
        this.progressBar.setProgressColor(Theme.getColor(Theme.key_chat_serviceText));
        this.progressView.addView(this.progressBar, LayoutHelper.createFrame(32, 32, 17));
        this.floatingDateView = new ChatActionCell(context);
        this.floatingDateView.setAlpha(0.0f);
        this.contentView.addView(this.floatingDateView, LayoutHelper.createFrame(-2, -2.0f, 49, 0.0f, 4.0f, 0.0f, 0.0f));
        this.floatingDateView.setOnClickListener(new ChatActivity$$Lambda$6(this));
        if (ChatObject.isChannel(this.currentChat)) {
            this.pinnedMessageView = new FrameLayout(context);
            this.pinnedMessageView.setTag(Integer.valueOf(1));
            this.pinnedMessageView.setTranslationY((float) (-AndroidUtilities.dp(50.0f)));
            this.pinnedMessageView.setVisibility(8);
            this.pinnedMessageView.setBackgroundResource(R.drawable.blockpanel);
            this.pinnedMessageView.getBackground().setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_topPanelBackground), Mode.MULTIPLY));
            this.contentView.addView(this.pinnedMessageView, LayoutHelper.createFrame(-1, 50, 51));
            this.pinnedMessageView.setOnClickListener(new ChatActivity$$Lambda$7(this));
            this.pinnedLineView = new View(context);
            this.pinnedLineView.setBackgroundColor(Theme.getColor(Theme.key_chat_topPanelLine));
            this.pinnedMessageView.addView(this.pinnedLineView, LayoutHelper.createFrame(2, 32.0f, 51, 8.0f, 8.0f, 0.0f, 0.0f));
            this.pinnedMessageImageView = new BackupImageView(context);
            this.pinnedMessageView.addView(this.pinnedMessageImageView, LayoutHelper.createFrame(32, 32.0f, 51, 17.0f, 8.0f, 0.0f, 0.0f));
            this.pinnedMessageNameTextView = new SimpleTextView(context);
            this.pinnedMessageNameTextView.setTextSize(14);
            this.pinnedMessageNameTextView.setTextColor(Theme.getColor(Theme.key_chat_topPanelTitle));
            this.pinnedMessageNameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
            this.pinnedMessageView.addView(this.pinnedMessageNameTextView, LayoutHelper.createFrame(-1, (float) AndroidUtilities.dp(18.0f), 51, 18.0f, 7.3f, 52.0f, 0.0f));
            this.pinnedMessageTextView = new SimpleTextView(context);
            this.pinnedMessageTextView.setTextSize(14);
            this.pinnedMessageTextView.setTextColor(Theme.getColor(Theme.key_chat_topPanelMessage));
            this.pinnedMessageView.addView(this.pinnedMessageTextView, LayoutHelper.createFrame(-1, (float) AndroidUtilities.dp(18.0f), 51, 18.0f, 25.3f, 52.0f, 0.0f));
            this.closePinned = new ImageView(context);
            this.closePinned.setImageResource(R.drawable.miniplayer_close);
            this.closePinned.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_topPanelClose), Mode.MULTIPLY));
            this.closePinned.setScaleType(ScaleType.CENTER);
            this.pinnedMessageView.addView(this.closePinned, LayoutHelper.createFrame(48, 48, 53));
            this.closePinned.setOnClickListener(new ChatActivity$$Lambda$8(this));
        }
        this.reportSpamView = new LinearLayout(context);
        this.reportSpamView.setTag(Integer.valueOf(1));
        this.reportSpamView.setTranslationY((float) (-AndroidUtilities.dp(50.0f)));
        this.reportSpamView.setVisibility(8);
        this.reportSpamView.setBackgroundResource(R.drawable.blockpanel);
        this.reportSpamView.getBackground().setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_topPanelBackground), Mode.MULTIPLY));
        this.contentView.addView(this.reportSpamView, LayoutHelper.createFrame(-1, 50, 51));
        this.addToContactsButton = new TextView(context);
        this.addToContactsButton.setTextColor(Theme.getColor(Theme.key_chat_addContact));
        this.addToContactsButton.setVisibility(8);
        this.addToContactsButton.setTextSize(1, 14.0f);
        this.addToContactsButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.addToContactsButton.setSingleLine(true);
        this.addToContactsButton.setMaxLines(1);
        this.addToContactsButton.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
        this.addToContactsButton.setGravity(17);
        this.addToContactsButton.setText(LocaleController.getString("AddContactChat", R.string.AddContactChat));
        this.reportSpamView.addView(this.addToContactsButton, LayoutHelper.createLinear(-1, -1, 0.5f, 51, 0, 0, 0, AndroidUtilities.dp(1.0f)));
        this.addToContactsButton.setOnClickListener(new ChatActivity$$Lambda$9(this));
        this.reportSpamContainer = new FrameLayout(context);
        this.reportSpamView.addView(this.reportSpamContainer, LayoutHelper.createLinear(-1, -1, 1.0f, 51, 0, 0, 0, AndroidUtilities.dp(1.0f)));
        this.reportSpamButton = new TextView(context);
        this.reportSpamButton.setTextColor(Theme.getColor(Theme.key_chat_reportSpam));
        this.reportSpamButton.setTextSize(1, 14.0f);
        this.reportSpamButton.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.reportSpamButton.setSingleLine(true);
        this.reportSpamButton.setMaxLines(1);
        if (this.currentChat != null) {
            this.reportSpamButton.setText(LocaleController.getString("ReportSpamAndLeave", R.string.ReportSpamAndLeave));
        } else {
            this.reportSpamButton.setText(LocaleController.getString("ReportSpam", R.string.ReportSpam));
        }
        this.reportSpamButton.setGravity(17);
        this.reportSpamButton.setPadding(AndroidUtilities.dp(50.0f), 0, AndroidUtilities.dp(50.0f), 0);
        this.reportSpamContainer.addView(this.reportSpamButton, LayoutHelper.createFrame(-1, -1, 51));
        this.reportSpamButton.setOnClickListener(new ChatActivity$$Lambda$10(this));
        this.closeReportSpam = new ImageView(context);
        this.closeReportSpam.setImageResource(R.drawable.miniplayer_close);
        this.closeReportSpam.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_topPanelClose), Mode.MULTIPLY));
        this.closeReportSpam.setScaleType(ScaleType.CENTER);
        this.reportSpamContainer.addView(this.closeReportSpam, LayoutHelper.createFrame(48, 48, 53));
        this.closeReportSpam.setOnClickListener(new ChatActivity$$Lambda$11(this));
        this.alertView = new FrameLayout(context);
        this.alertView.setTag(Integer.valueOf(1));
        this.alertView.setTranslationY((float) (-AndroidUtilities.dp(50.0f)));
        this.alertView.setVisibility(8);
        this.alertView.setBackgroundResource(R.drawable.blockpanel);
        this.alertView.getBackground().setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_topPanelBackground), Mode.MULTIPLY));
        this.contentView.addView(this.alertView, LayoutHelper.createFrame(-1, 50, 51));
        this.alertNameTextView = new TextView(context);
        this.alertNameTextView.setTextSize(1, 14.0f);
        this.alertNameTextView.setTextColor(Theme.getColor(Theme.key_chat_topPanelTitle));
        this.alertNameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.alertNameTextView.setSingleLine(true);
        this.alertNameTextView.setEllipsize(TruncateAt.END);
        this.alertNameTextView.setMaxLines(1);
        this.alertView.addView(this.alertNameTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 8.0f, 5.0f, 8.0f, 0.0f));
        this.alertTextView = new TextView(context);
        this.alertTextView.setTextSize(1, 14.0f);
        this.alertTextView.setTextColor(Theme.getColor(Theme.key_chat_topPanelMessage));
        this.alertTextView.setSingleLine(true);
        this.alertTextView.setEllipsize(TruncateAt.END);
        this.alertTextView.setMaxLines(1);
        this.alertView.addView(this.alertTextView, LayoutHelper.createFrame(-2, -2.0f, 51, 8.0f, 23.0f, 8.0f, 0.0f));
        this.pagedownButton = new FrameLayout(context);
        this.pagedownButton.setVisibility(4);
        this.contentView.addView(this.pagedownButton, LayoutHelper.createFrame(66, 59.0f, 85, 0.0f, 0.0f, -3.0f, 5.0f));
        this.pagedownButton.setOnClickListener(new ChatActivity$$Lambda$12(this));
        this.mentiondownButton = new FrameLayout(context);
        this.mentiondownButton.setVisibility(4);
        this.contentView.addView(this.mentiondownButton, LayoutHelper.createFrame(46, 59.0f, 85, 0.0f, 0.0f, 7.0f, 5.0f));
        this.mentiondownButton.setOnClickListener(new OnClickListener() {
            private void loadLastUnreadMention() {
                ChatActivity.this.wasManualScroll = true;
                if (ChatActivity.this.hasAllMentionsLocal) {
                    MessagesStorage.getInstance(ChatActivity.this.currentAccount).getUnreadMention(ChatActivity.this.dialog_id, new ChatActivity$13$$Lambda$0(this));
                    return;
                }
                MessagesStorage messagesStorage = MessagesStorage.getInstance(ChatActivity.this.currentAccount);
                TL_messages_getUnreadMentions req = new TL_messages_getUnreadMentions();
                req.peer = MessagesController.getInstance(ChatActivity.this.currentAccount).getInputPeer((int) ChatActivity.this.dialog_id);
                req.limit = 1;
                req.add_offset = ChatActivity.this.newMentionsCount - 1;
                ConnectionsManager.getInstance(ChatActivity.this.currentAccount).sendRequest(req, new ChatActivity$13$$Lambda$1(this, messagesStorage));
            }

            final /* synthetic */ void lambda$loadLastUnreadMention$0$ChatActivity$13(int param) {
                if (param == 0) {
                    ChatActivity.this.hasAllMentionsLocal = false;
                    loadLastUnreadMention();
                    return;
                }
                ChatActivity.this.scrollToMessageId(param, 0, false, 0, false);
            }

            final /* synthetic */ void lambda$loadLastUnreadMention$2$ChatActivity$13(MessagesStorage messagesStorage, TLObject response, TL_error error) {
                AndroidUtilities.runOnUIThread(new ChatActivity$13$$Lambda$2(this, response, error, messagesStorage));
            }

            final /* synthetic */ void lambda$null$1$ChatActivity$13(TLObject response, TL_error error, MessagesStorage messagesStorage) {
                messages_Messages res = (messages_Messages) response;
                if (error != null || res.messages.isEmpty()) {
                    if (res != null) {
                        ChatActivity.this.newMentionsCount = res.count;
                    } else {
                        ChatActivity.this.newMentionsCount = 0;
                    }
                    messagesStorage.resetMentionsCount(ChatActivity.this.dialog_id, ChatActivity.this.newMentionsCount);
                    if (ChatActivity.this.newMentionsCount == 0) {
                        ChatActivity.this.hasAllMentionsLocal = true;
                        ChatActivity.this.showMentiondownButton(false, true);
                        return;
                    }
                    ChatActivity.this.mentiondownButtonCounter.setText(String.format("%d", new Object[]{Integer.valueOf(ChatActivity.this.newMentionsCount)}));
                    loadLastUnreadMention();
                    return;
                }
                int id = ((Message) res.messages.get(0)).id;
                long mid = (long) id;
                if (ChatObject.isChannel(ChatActivity.this.currentChat)) {
                    mid |= ((long) ChatActivity.this.currentChat.id) << 32;
                }
                MessageObject object = (MessageObject) ChatActivity.this.messagesDict[0].get(id);
                messagesStorage.markMessageAsMention(mid);
                if (object != null) {
                    object.messageOwner.media_unread = true;
                    object.messageOwner.mentioned = true;
                }
                ChatActivity.this.scrollToMessageId(id, 0, false, 0, false);
            }

            public void onClick(View view) {
                loadLastUnreadMention();
            }
        });
        this.mentiondownButton.setOnLongClickListener(new ChatActivity$$Lambda$13(this));
        if (!this.isBroadcast) {
            this.mentionContainer = new FrameLayout(context) {
                public void onDraw(Canvas canvas) {
                    if (ChatActivity.this.mentionListView.getChildCount() > 0) {
                        int top;
                        if (ChatActivity.this.mentionLayoutManager.getReverseLayout()) {
                            top = ChatActivity.this.mentionListViewScrollOffsetY + AndroidUtilities.dp(2.0f);
                            Theme.chat_composeShadowDrawable.setBounds(0, top + Theme.chat_composeShadowDrawable.getIntrinsicHeight(), getMeasuredWidth(), top);
                            Theme.chat_composeShadowDrawable.draw(canvas);
                            canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) top, Theme.chat_composeBackgroundPaint);
                            return;
                        }
                        if (ChatActivity.this.mentionsAdapter.isBotContext() && ChatActivity.this.mentionsAdapter.isMediaLayout() && ChatActivity.this.mentionsAdapter.getBotContextSwitch() == null) {
                            top = ChatActivity.this.mentionListViewScrollOffsetY - AndroidUtilities.dp(4.0f);
                        } else {
                            top = ChatActivity.this.mentionListViewScrollOffsetY - AndroidUtilities.dp(2.0f);
                        }
                        int bottom = top + Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                        Theme.chat_composeShadowDrawable.setBounds(0, top, getMeasuredWidth(), bottom);
                        Theme.chat_composeShadowDrawable.draw(canvas);
                        canvas.drawRect(0.0f, (float) bottom, (float) getMeasuredWidth(), (float) getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
                    }
                }

                public void requestLayout() {
                    if (!ChatActivity.this.mentionListViewIgnoreLayout) {
                        super.requestLayout();
                    }
                }
            };
            this.mentionContainer.setVisibility(8);
            this.mentionContainer.setWillNotDraw(false);
            this.contentView.addView(this.mentionContainer, LayoutHelper.createFrame(-1, 110, 83));
            this.mentionListView = new RecyclerListView(context) {
                private int lastHeight;
                private int lastWidth;

                public boolean onInterceptTouchEvent(MotionEvent event) {
                    if (ChatActivity.this.mentionLayoutManager.getReverseLayout()) {
                        if (!(ChatActivity.this.mentionListViewIsScrolling || ChatActivity.this.mentionListViewScrollOffsetY == 0 || event.getY() <= ((float) ChatActivity.this.mentionListViewScrollOffsetY))) {
                            return false;
                        }
                    } else if (!(ChatActivity.this.mentionListViewIsScrolling || ChatActivity.this.mentionListViewScrollOffsetY == 0 || event.getY() >= ((float) ChatActivity.this.mentionListViewScrollOffsetY))) {
                        return false;
                    }
                    boolean result = StickerPreviewViewer.getInstance().onInterceptTouchEvent(event, ChatActivity.this.mentionListView, 0, null);
                    if (super.onInterceptTouchEvent(event) || result) {
                        return true;
                    }
                    return false;
                }

                public boolean onTouchEvent(MotionEvent event) {
                    if (ChatActivity.this.mentionLayoutManager.getReverseLayout()) {
                        if (!(ChatActivity.this.mentionListViewIsScrolling || ChatActivity.this.mentionListViewScrollOffsetY == 0 || event.getY() <= ((float) ChatActivity.this.mentionListViewScrollOffsetY))) {
                            return false;
                        }
                    } else if (!(ChatActivity.this.mentionListViewIsScrolling || ChatActivity.this.mentionListViewScrollOffsetY == 0 || event.getY() >= ((float) ChatActivity.this.mentionListViewScrollOffsetY))) {
                        return false;
                    }
                    return super.onTouchEvent(event);
                }

                public void requestLayout() {
                    if (!ChatActivity.this.mentionListViewIgnoreLayout) {
                        super.requestLayout();
                    }
                }

                protected void onLayout(boolean changed, int l, int t, int r, int b) {
                    int width = r - l;
                    int height = b - t;
                    int newPosition = -1;
                    int newTop = 0;
                    if (!(ChatActivity.this.mentionLayoutManager.getReverseLayout() || ChatActivity.this.mentionListView == null || ChatActivity.this.mentionListViewLastViewPosition < 0 || width != this.lastWidth || height - this.lastHeight == 0)) {
                        newPosition = ChatActivity.this.mentionListViewLastViewPosition;
                        newTop = ((ChatActivity.this.mentionListViewLastViewTop + height) - this.lastHeight) - getPaddingTop();
                    }
                    super.onLayout(changed, l, t, r, b);
                    if (newPosition != -1) {
                        ChatActivity.this.mentionListViewIgnoreLayout = true;
                        if (ChatActivity.this.mentionsAdapter.isBotContext() && ChatActivity.this.mentionsAdapter.isMediaLayout()) {
                            ChatActivity.this.mentionGridLayoutManager.scrollToPositionWithOffset(newPosition, newTop);
                        } else {
                            ChatActivity.this.mentionLayoutManager.scrollToPositionWithOffset(newPosition, newTop);
                        }
                        super.onLayout(false, l, t, r, b);
                        ChatActivity.this.mentionListViewIgnoreLayout = false;
                    }
                    this.lastHeight = height;
                    this.lastWidth = width;
                    ChatActivity.this.mentionListViewUpdateLayout();
                }
            };
            this.mentionListView.setOnTouchListener(new ChatActivity$$Lambda$14(this));
            this.mentionListView.setTag(Integer.valueOf(2));
            this.mentionLayoutManager = new LinearLayoutManager(context) {
                public boolean supportsPredictiveItemAnimations() {
                    return false;
                }
            };
            this.mentionLayoutManager.setOrientation(1);
            this.mentionGridLayoutManager = new ExtendedGridLayoutManager(context, 100) {
                private Size size = new Size();

                protected Size getSizeForItem(int i) {
                    float f = 100.0f;
                    if (ChatActivity.this.mentionsAdapter.getBotContextSwitch() != null) {
                        i++;
                    }
                    this.size.width = 0.0f;
                    this.size.height = 0.0f;
                    BotInlineResult object = ChatActivity.this.mentionsAdapter.getItem(i);
                    if (object instanceof BotInlineResult) {
                        BotInlineResult inlineResult = object;
                        int b;
                        DocumentAttribute attribute;
                        if (inlineResult.document != null) {
                            float f2;
                            Size size = this.size;
                            if (inlineResult.document.thumb != null) {
                                f2 = (float) inlineResult.document.thumb.w;
                            } else {
                                f2 = 100.0f;
                            }
                            size.width = f2;
                            Size size2 = this.size;
                            if (inlineResult.document.thumb != null) {
                                f = (float) inlineResult.document.thumb.h;
                            }
                            size2.height = f;
                            for (b = 0; b < inlineResult.document.attributes.size(); b++) {
                                attribute = (DocumentAttribute) inlineResult.document.attributes.get(b);
                                if ((attribute instanceof TL_documentAttributeImageSize) || (attribute instanceof TL_documentAttributeVideo)) {
                                    this.size.width = (float) attribute.w;
                                    this.size.height = (float) attribute.h;
                                    break;
                                }
                            }
                        } else if (inlineResult.content != null) {
                            for (b = 0; b < inlineResult.content.attributes.size(); b++) {
                                attribute = (DocumentAttribute) inlineResult.content.attributes.get(b);
                                if ((attribute instanceof TL_documentAttributeImageSize) || (attribute instanceof TL_documentAttributeVideo)) {
                                    this.size.width = (float) attribute.w;
                                    this.size.height = (float) attribute.h;
                                    break;
                                }
                            }
                        } else if (inlineResult.thumb != null) {
                            for (b = 0; b < inlineResult.thumb.attributes.size(); b++) {
                                attribute = (DocumentAttribute) inlineResult.thumb.attributes.get(b);
                                if ((attribute instanceof TL_documentAttributeImageSize) || (attribute instanceof TL_documentAttributeVideo)) {
                                    this.size.width = (float) attribute.w;
                                    this.size.height = (float) attribute.h;
                                    break;
                                }
                            }
                        } else if (inlineResult.photo != null) {
                            PhotoSize photoSize = FileLoader.getClosestPhotoSizeWithSize(inlineResult.photo.sizes, AndroidUtilities.photoSize.intValue());
                            if (photoSize != null) {
                                this.size.width = (float) photoSize.w;
                                this.size.height = (float) photoSize.h;
                            }
                        }
                    }
                    return this.size;
                }

                protected int getFlowItemCount() {
                    if (ChatActivity.this.mentionsAdapter.getBotContextSwitch() != null) {
                        return getItemCount() - 1;
                    }
                    return super.getFlowItemCount();
                }
            };
            this.mentionGridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
                public int getSpanSize(int position) {
                    if (ChatActivity.this.mentionsAdapter.getItem(position) instanceof TL_inlineBotSwitchPM) {
                        return 100;
                    }
                    if (ChatActivity.this.mentionsAdapter.getBotContextSwitch() != null) {
                        position--;
                    }
                    return ChatActivity.this.mentionGridLayoutManager.getSpanSizeForItem(position);
                }
            });
            this.mentionListView.addItemDecoration(new ItemDecoration() {
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
                    int i = 0;
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                    if (parent.getLayoutManager() == ChatActivity.this.mentionGridLayoutManager) {
                        int position = parent.getChildAdapterPosition(view);
                        if (ChatActivity.this.mentionsAdapter.getBotContextSwitch() == null) {
                            outRect.top = AndroidUtilities.dp(2.0f);
                        } else if (position != 0) {
                            position--;
                            if (!ChatActivity.this.mentionGridLayoutManager.isFirstRow(position)) {
                                outRect.top = AndroidUtilities.dp(2.0f);
                            }
                        } else {
                            return;
                        }
                        if (!ChatActivity.this.mentionGridLayoutManager.isLastInRow(position)) {
                            i = AndroidUtilities.dp(2.0f);
                        }
                        outRect.right = i;
                    }
                }
            });
            this.mentionListView.setItemAnimator(null);
            this.mentionListView.setLayoutAnimation(null);
            this.mentionListView.setClipToPadding(false);
            this.mentionListView.setLayoutManager(this.mentionLayoutManager);
            this.mentionListView.setOverScrollMode(2);
            this.mentionContainer.addView(this.mentionListView, LayoutHelper.createFrame(-1, -1.0f));
            recyclerListView = this.mentionListView;
            chatActivityAdapter = new MentionsAdapter(context, false, this.dialog_id, new MentionsAdapterDelegate() {
                public void needChangePanelVisibility(boolean show) {
                    if (ChatActivity.this.mentionsAdapter.isBotContext() && ChatActivity.this.mentionsAdapter.isMediaLayout()) {
                        ChatActivity.this.mentionListView.setLayoutManager(ChatActivity.this.mentionGridLayoutManager);
                    } else {
                        ChatActivity.this.mentionListView.setLayoutManager(ChatActivity.this.mentionLayoutManager);
                    }
                    if (show && ChatActivity.this.bottomOverlay.getVisibility() == 0) {
                        show = false;
                    }
                    if (show) {
                        if (ChatActivity.this.mentionListAnimation != null) {
                            ChatActivity.this.mentionListAnimation.cancel();
                            ChatActivity.this.mentionListAnimation = null;
                        }
                        if (ChatActivity.this.mentionContainer.getVisibility() == 0) {
                            ChatActivity.this.mentionContainer.setAlpha(1.0f);
                            return;
                        }
                        if (ChatActivity.this.mentionsAdapter.isBotContext() && ChatActivity.this.mentionsAdapter.isMediaLayout()) {
                            ChatActivity.this.mentionGridLayoutManager.scrollToPositionWithOffset(0, 10000);
                        } else {
                            ChatActivity.this.mentionLayoutManager.scrollToPositionWithOffset(0, 10000);
                        }
                        if (ChatActivity.this.allowStickersPanel && (!ChatActivity.this.mentionsAdapter.isBotContext() || ChatActivity.this.allowContextBotPanel || ChatActivity.this.allowContextBotPanelSecond)) {
                            if (ChatActivity.this.currentEncryptedChat != null && ChatActivity.this.mentionsAdapter.isBotContext()) {
                                SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                                if (!preferences.getBoolean("secretbot", false)) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this.getParentActivity());
                                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                    builder.setMessage(LocaleController.getString("SecretChatContextBotAlert", R.string.SecretChatContextBotAlert));
                                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                                    ChatActivity.this.showDialog(builder.create());
                                    preferences.edit().putBoolean("secretbot", true).commit();
                                }
                            }
                            ChatActivity.this.mentionContainer.setVisibility(0);
                            ChatActivity.this.mentionContainer.setTag(null);
                            ChatActivity.this.mentionListAnimation = new AnimatorSet();
                            ChatActivity.this.mentionListAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(ChatActivity.this.mentionContainer, "alpha", new float[]{0.0f, 1.0f})});
                            ChatActivity.this.mentionListAnimation.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationEnd(Animator animation) {
                                    if (ChatActivity.this.mentionListAnimation != null && ChatActivity.this.mentionListAnimation.equals(animation)) {
                                        ChatActivity.this.mentionListAnimation = null;
                                    }
                                }

                                public void onAnimationCancel(Animator animation) {
                                    if (ChatActivity.this.mentionListAnimation != null && ChatActivity.this.mentionListAnimation.equals(animation)) {
                                        ChatActivity.this.mentionListAnimation = null;
                                    }
                                }
                            });
                            ChatActivity.this.mentionListAnimation.setDuration(200);
                            ChatActivity.this.mentionListAnimation.start();
                            return;
                        }
                        ChatActivity.this.mentionContainer.setAlpha(1.0f);
                        ChatActivity.this.mentionContainer.setVisibility(4);
                        return;
                    }
                    if (ChatActivity.this.mentionListAnimation != null) {
                        ChatActivity.this.mentionListAnimation.cancel();
                        ChatActivity.this.mentionListAnimation = null;
                    }
                    if (ChatActivity.this.mentionContainer.getVisibility() == 8) {
                        return;
                    }
                    if (ChatActivity.this.allowStickersPanel) {
                        ChatActivity.this.mentionListAnimation = new AnimatorSet();
                        AnimatorSet access$15600 = ChatActivity.this.mentionListAnimation;
                        Animator[] animatorArr = new Animator[1];
                        animatorArr[0] = ObjectAnimator.ofFloat(ChatActivity.this.mentionContainer, "alpha", new float[]{0.0f});
                        access$15600.playTogether(animatorArr);
                        ChatActivity.this.mentionListAnimation.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                if (ChatActivity.this.mentionListAnimation != null && ChatActivity.this.mentionListAnimation.equals(animation)) {
                                    ChatActivity.this.mentionContainer.setVisibility(8);
                                    ChatActivity.this.mentionContainer.setTag(null);
                                    ChatActivity.this.mentionListAnimation = null;
                                }
                            }

                            public void onAnimationCancel(Animator animation) {
                                if (ChatActivity.this.mentionListAnimation != null && ChatActivity.this.mentionListAnimation.equals(animation)) {
                                    ChatActivity.this.mentionListAnimation = null;
                                }
                            }
                        });
                        ChatActivity.this.mentionListAnimation.setDuration(200);
                        ChatActivity.this.mentionListAnimation.start();
                        return;
                    }
                    ChatActivity.this.mentionContainer.setTag(null);
                    ChatActivity.this.mentionContainer.setVisibility(8);
                }

                public void onContextSearch(boolean searching) {
                    if (ChatActivity.this.chatActivityEnterView != null) {
                        ChatActivity.this.chatActivityEnterView.setCaption(ChatActivity.this.mentionsAdapter.getBotCaption());
                        ChatActivity.this.chatActivityEnterView.showContextProgress(searching);
                    }
                }

                public void onContextClick(BotInlineResult result) {
                    if (ChatActivity.this.getParentActivity() != null && result.content != null) {
                        if (result.type.equals(MimeTypes.BASE_TYPE_VIDEO) || result.type.equals("web_player_video")) {
                            int[] size = MessageObject.getInlineResultWidthAndHeight(result);
                            EmbedBottomSheet.show(ChatActivity.this.getParentActivity(), result.title != null ? result.title : TtmlNode.ANONYMOUS_REGION_ID, result.description, result.content.url, result.content.url, size[0], size[1]);
                            return;
                        }
                        Browser.openUrl(ChatActivity.this.getParentActivity(), result.content.url);
                    }
                }
            });
            this.mentionsAdapter = chatActivityAdapter;
            recyclerListView.setAdapter(chatActivityAdapter);
            if (!ChatObject.isChannel(this.currentChat) || (this.currentChat != null && this.currentChat.megagroup)) {
                this.mentionsAdapter.setBotInfo(this.botInfo);
            }
            this.mentionsAdapter.setParentFragment(this);
            this.mentionsAdapter.setChatInfo(this.info);
            this.mentionsAdapter.setNeedUsernames(this.currentChat != null);
            MentionsAdapter mentionsAdapter = this.mentionsAdapter;
            z = this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(this.currentEncryptedChat.layer) >= 46;
            mentionsAdapter.setNeedBotContext(z);
            this.mentionsAdapter.setBotsCount(this.currentChat != null ? this.botsCount : 1);
            recyclerListView = this.mentionListView;
            OnItemClickListener chatActivity$$Lambda$15 = new ChatActivity$$Lambda$15(this);
            this.mentionsOnItemClickListener = chatActivity$$Lambda$15;
            recyclerListView.setOnItemClickListener(chatActivity$$Lambda$15);
            this.mentionListView.setOnItemLongClickListener(new ChatActivity$$Lambda$16(this));
            this.mentionListView.setOnScrollListener(new OnScrollListener() {
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    boolean z = true;
                    ChatActivity chatActivity = ChatActivity.this;
                    if (newState != 1) {
                        z = false;
                    }
                    chatActivity.mentionListViewIsScrolling = z;
                }

                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    int lastVisibleItem;
                    int visibleItemCount;
                    if (ChatActivity.this.mentionsAdapter.isBotContext() && ChatActivity.this.mentionsAdapter.isMediaLayout()) {
                        lastVisibleItem = ChatActivity.this.mentionGridLayoutManager.findLastVisibleItemPosition();
                    } else {
                        lastVisibleItem = ChatActivity.this.mentionLayoutManager.findLastVisibleItemPosition();
                    }
                    if (lastVisibleItem == -1) {
                        visibleItemCount = 0;
                    } else {
                        visibleItemCount = lastVisibleItem;
                    }
                    if (visibleItemCount > 0 && lastVisibleItem > ChatActivity.this.mentionsAdapter.getItemCount() - 5) {
                        ChatActivity.this.mentionsAdapter.searchForContextBotForNextOffset();
                    }
                    ChatActivity.this.mentionListViewUpdateLayout();
                }
            });
        }
        this.pagedownButtonImage = new ImageView(context);
        this.pagedownButtonImage.setImageResource(R.drawable.pagedown);
        this.pagedownButtonImage.setScaleType(ScaleType.CENTER);
        this.pagedownButtonImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_goDownButtonIcon), Mode.MULTIPLY));
        this.pagedownButtonImage.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
        Drawable drawable = Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(Theme.key_chat_goDownButton));
        Drawable shadowDrawable = context.getResources().getDrawable(R.drawable.pagedown_shadow).mutate();
        shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_goDownButtonShadow), Mode.MULTIPLY));
        Drawable combinedDrawable = new CombinedDrawable(shadowDrawable, drawable, 0, 0);
        combinedDrawable.setIconSize(AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f));
        this.pagedownButtonImage.setBackgroundDrawable(combinedDrawable);
        this.pagedownButton.addView(this.pagedownButtonImage, LayoutHelper.createFrame(46, 46, 81));
        this.pagedownButtonCounter = new TextView(context);
        this.pagedownButtonCounter.setVisibility(4);
        this.pagedownButtonCounter.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.pagedownButtonCounter.setTextSize(1, 13.0f);
        this.pagedownButtonCounter.setTextColor(Theme.getColor(Theme.key_chat_goDownButtonCounter));
        this.pagedownButtonCounter.setGravity(17);
        this.pagedownButtonCounter.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(11.5f), Theme.getColor(Theme.key_chat_goDownButtonCounterBackground)));
        this.pagedownButtonCounter.setMinWidth(AndroidUtilities.dp(23.0f));
        this.pagedownButtonCounter.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(1.0f));
        this.pagedownButton.addView(this.pagedownButtonCounter, LayoutHelper.createFrame(-2, 23, 49));
        this.mentiondownButtonImage = new ImageView(context);
        this.mentiondownButtonImage.setImageResource(R.drawable.mentionbutton);
        this.mentiondownButtonImage.setScaleType(ScaleType.CENTER);
        this.mentiondownButtonImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_goDownButtonIcon), Mode.MULTIPLY));
        this.mentiondownButtonImage.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
        drawable = Theme.createCircleDrawable(AndroidUtilities.dp(42.0f), Theme.getColor(Theme.key_chat_goDownButton));
        shadowDrawable = context.getResources().getDrawable(R.drawable.pagedown_shadow).mutate();
        shadowDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_goDownButtonShadow), Mode.MULTIPLY));
        combinedDrawable = new CombinedDrawable(shadowDrawable, drawable, 0, 0);
        combinedDrawable.setIconSize(AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f));
        this.mentiondownButtonImage.setBackgroundDrawable(combinedDrawable);
        this.mentiondownButton.addView(this.mentiondownButtonImage, LayoutHelper.createFrame(46, 46, 83));
        this.mentiondownButtonCounter = new TextView(context);
        this.mentiondownButtonCounter.setVisibility(4);
        this.mentiondownButtonCounter.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.mentiondownButtonCounter.setTextSize(1, 13.0f);
        this.mentiondownButtonCounter.setTextColor(Theme.getColor(Theme.key_chat_goDownButtonCounter));
        this.mentiondownButtonCounter.setGravity(17);
        this.mentiondownButtonCounter.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(11.5f), Theme.getColor(Theme.key_chat_goDownButtonCounterBackground)));
        this.mentiondownButtonCounter.setMinWidth(AndroidUtilities.dp(23.0f));
        this.mentiondownButtonCounter.setPadding(AndroidUtilities.dp(8.0f), 0, AndroidUtilities.dp(8.0f), AndroidUtilities.dp(1.0f));
        this.mentiondownButton.addView(this.mentiondownButtonCounter, LayoutHelper.createFrame(-2, 23, 49));
        if (!AndroidUtilities.isTablet() || AndroidUtilities.isSmallTablet()) {
            SizeNotifierFrameLayout sizeNotifierFrameLayout = this.contentView;
            fragmentContextView = new FragmentContextView(context, this, true);
            this.fragmentLocationContextView = fragmentContextView;
            sizeNotifierFrameLayout.addView(fragmentContextView, LayoutHelper.createFrame(-1, 39.0f, 51, 0.0f, -36.0f, 0.0f, 0.0f));
            sizeNotifierFrameLayout = this.contentView;
            fragmentContextView = new FragmentContextView(context, this, false);
            this.fragmentContextView = fragmentContextView;
            sizeNotifierFrameLayout.addView(fragmentContextView, LayoutHelper.createFrame(-1, 39.0f, 51, 0.0f, -36.0f, 0.0f, 0.0f));
            this.fragmentContextView.setAdditionalContextView(this.fragmentLocationContextView);
            this.fragmentLocationContextView.setAdditionalContextView(this.fragmentContextView);
        }
        this.contentView.addView(this.actionBar);
        this.overlayView = new View(context);
        this.overlayView.setOnTouchListener(new ChatActivity$$Lambda$17(this));
        this.contentView.addView(this.overlayView, LayoutHelper.createFrame(-1, -1, 51));
        this.overlayView.setVisibility(8);
        this.instantCameraView = new InstantCameraView(context, this);
        this.contentView.addView(this.instantCameraView, LayoutHelper.createFrame(-1, -1, 51));
        this.chatActivityEnterView = new ChatActivityEnterView(getParentActivity(), this.contentView, this, true);
        this.chatActivityEnterView.setDialogId(this.dialog_id, this.currentAccount);
        this.chatActivityEnterView.setId(id_chat_compose_panel);
        this.chatActivityEnterView.setBotsCount(this.botsCount, this.hasBotsCommands);
        ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
        z = this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(this.currentEncryptedChat.layer) >= 23;
        if (this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(this.currentEncryptedChat.layer) >= 46) {
            z2 = true;
        } else {
            z2 = false;
        }
        chatActivityEnterView.setAllowStickersAndGifs(z, z2);
        if (this.inPreviewMode) {
            this.chatActivityEnterView.setVisibility(4);
        }
        this.contentView.addView(this.chatActivityEnterView, this.contentView.getChildCount() - 1, LayoutHelper.createFrame(-1, -2, 83));
        this.chatActivityEnterView.setDelegate(new ChatActivityEnterViewDelegate() {
            public void onMessageSend(CharSequence message) {
                ChatActivity.this.moveScrollToLastMessage();
                ChatActivity.this.hideFieldPanel();
                if (ChatActivity.this.mentionsAdapter != null) {
                    ChatActivity.this.mentionsAdapter.addHashtagsFromMessage(message);
                }
            }

            public void onSwitchRecordMode(boolean video) {
                ChatActivity.this.showVoiceHint(false, video);
            }

            public void onPreAudioVideoRecord() {
                ChatActivity.this.showVoiceHint(true, false);
            }

            public void onTextSelectionChanged(int start, int end) {
                if (ChatActivity.this.editTextItem != null) {
                    if (end - start > 0) {
                        if (ChatActivity.this.editTextItem.getTag() == null) {
                            ChatActivity.this.editTextItem.setTag(Integer.valueOf(1));
                            ChatActivity.this.editTextItem.setVisibility(0);
                            ChatActivity.this.headerItem.setVisibility(8);
                            ChatActivity.this.attachItem.setVisibility(8);
                        }
                        ChatActivity.this.editTextStart = start;
                        ChatActivity.this.editTextEnd = end;
                    } else if (ChatActivity.this.editTextItem.getTag() != null) {
                        ChatActivity.this.editTextItem.setTag(null);
                        ChatActivity.this.editTextItem.setVisibility(8);
                        if (ChatActivity.this.chatActivityEnterView.hasText()) {
                            ChatActivity.this.headerItem.setVisibility(8);
                            ChatActivity.this.attachItem.setVisibility(0);
                            return;
                        }
                        ChatActivity.this.headerItem.setVisibility(0);
                        ChatActivity.this.attachItem.setVisibility(8);
                    }
                }
            }

            public void onTextChanged(final CharSequence text, boolean bigChange) {
                if (ChatActivity.this.startReplyOnTextChange && text.length() > 0) {
                    ChatActivity.this.actionBar.getActionBarMenuOnItemClick().onItemClick(19);
                    ChatActivity.this.startReplyOnTextChange = false;
                }
                MediaController instance = MediaController.getInstance();
                boolean z = !TextUtils.isEmpty(text) || ChatActivity.this.chatActivityEnterView.isEditingMessage();
                instance.setInputFieldHasText(z);
                if (!(ChatActivity.this.stickersAdapter == null || ChatActivity.this.chatActivityEnterView.isEditingMessage() || !ChatObject.canSendStickers(ChatActivity.this.currentChat))) {
                    ChatActivity.this.stickersAdapter.loadStikersForEmoji(text);
                }
                if (ChatActivity.this.mentionsAdapter != null) {
                    ChatActivity.this.mentionsAdapter.searchUsernameOrHashtag(text.toString(), ChatActivity.this.chatActivityEnterView.getCursorPosition(), ChatActivity.this.messages, false);
                }
                if (ChatActivity.this.waitingForCharaterEnterRunnable != null) {
                    AndroidUtilities.cancelRunOnUIThread(ChatActivity.this.waitingForCharaterEnterRunnable);
                    ChatActivity.this.waitingForCharaterEnterRunnable = null;
                }
                if (!ChatObject.canSendEmbed(ChatActivity.this.currentChat) || !ChatActivity.this.chatActivityEnterView.isMessageWebPageSearchEnabled()) {
                    return;
                }
                if (!ChatActivity.this.chatActivityEnterView.isEditingMessage() || !ChatActivity.this.chatActivityEnterView.isEditingCaption()) {
                    if (bigChange) {
                        ChatActivity.this.searchLinks(text, true);
                        return;
                    }
                    ChatActivity.this.waitingForCharaterEnterRunnable = new Runnable() {
                        public void run() {
                            if (this == ChatActivity.this.waitingForCharaterEnterRunnable) {
                                ChatActivity.this.searchLinks(text, false);
                                ChatActivity.this.waitingForCharaterEnterRunnable = null;
                            }
                        }
                    };
                    AndroidUtilities.runOnUIThread(ChatActivity.this.waitingForCharaterEnterRunnable, AndroidUtilities.WEB_URL == null ? 3000 : 1000);
                }
            }

            public void onTextSpansChanged(CharSequence text) {
                ChatActivity.this.searchLinks(text, true);
            }

            public void needSendTyping() {
                MessagesController.getInstance(ChatActivity.this.currentAccount).sendTyping(ChatActivity.this.dialog_id, 0, ChatActivity.this.classGuid);
            }

            public void onAttachButtonHidden() {
                if (!ChatActivity.this.actionBar.isSearchFieldVisible() && ChatActivity.this.headerItem != null) {
                    ChatActivity.this.headerItem.setVisibility(8);
                    ChatActivity.this.editTextItem.setVisibility(8);
                    ChatActivity.this.attachItem.setVisibility(0);
                }
            }

            public void onAttachButtonShow() {
                if (!ChatActivity.this.actionBar.isSearchFieldVisible() && ChatActivity.this.headerItem != null) {
                    ChatActivity.this.headerItem.setVisibility(0);
                    ChatActivity.this.editTextItem.setVisibility(8);
                    ChatActivity.this.attachItem.setVisibility(8);
                }
            }

            public void onMessageEditEnd(boolean loading) {
                if (!loading) {
                    MentionsAdapter access$5300 = ChatActivity.this.mentionsAdapter;
                    boolean z = ChatActivity.this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(ChatActivity.this.currentEncryptedChat.layer) >= 46;
                    access$5300.setNeedBotContext(z);
                    ChatActivity.this.chatListView.setOnItemLongClickListener(ChatActivity.this.onItemLongClickListener);
                    ChatActivity.this.chatListView.setOnItemClickListener(ChatActivity.this.onItemClickListener);
                    ChatActivity.this.chatListView.setClickable(true);
                    ChatActivity.this.chatListView.setLongClickable(true);
                    if (ChatActivity.this.editingMessageObject != null) {
                        ChatActivity.this.hideFieldPanel();
                    }
                    ChatActivityEnterView chatActivityEnterView = ChatActivity.this.chatActivityEnterView;
                    if (ChatActivity.this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(ChatActivity.this.currentEncryptedChat.layer) >= 23) {
                        z = true;
                    } else {
                        z = false;
                    }
                    boolean z2 = ChatActivity.this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(ChatActivity.this.currentEncryptedChat.layer) >= 46;
                    chatActivityEnterView.setAllowStickersAndGifs(z, z2);
                    if (ChatActivity.this.editingMessageObjectReqId != 0) {
                        ConnectionsManager.getInstance(ChatActivity.this.currentAccount).cancelRequest(ChatActivity.this.editingMessageObjectReqId, true);
                        ChatActivity.this.editingMessageObjectReqId = 0;
                    }
                    ChatActivity.this.updatePinnedMessageView(true);
                    ChatActivity.this.updateBottomOverlay();
                    ChatActivity.this.updateVisibleRows();
                }
            }

            public void onWindowSizeChanged(int size) {
                boolean z = true;
                if (size < AndroidUtilities.dp(72.0f) + ActionBar.getCurrentActionBarHeight()) {
                    ChatActivity.this.allowStickersPanel = false;
                    if (ChatActivity.this.stickersPanel.getVisibility() == 0) {
                        ChatActivity.this.stickersPanel.setVisibility(4);
                    }
                    if (ChatActivity.this.mentionContainer != null && ChatActivity.this.mentionContainer.getVisibility() == 0) {
                        ChatActivity.this.mentionContainer.setVisibility(4);
                    }
                } else {
                    ChatActivity.this.allowStickersPanel = true;
                    if (ChatActivity.this.stickersPanel.getVisibility() == 4) {
                        ChatActivity.this.stickersPanel.setVisibility(0);
                    }
                    if (ChatActivity.this.mentionContainer != null && ChatActivity.this.mentionContainer.getVisibility() == 4 && (!ChatActivity.this.mentionsAdapter.isBotContext() || ChatActivity.this.allowContextBotPanel || ChatActivity.this.allowContextBotPanelSecond)) {
                        ChatActivity.this.mentionContainer.setVisibility(0);
                        ChatActivity.this.mentionContainer.setTag(null);
                    }
                }
                ChatActivity chatActivity = ChatActivity.this;
                if (ChatActivity.this.chatActivityEnterView.isPopupShowing()) {
                    z = false;
                }
                chatActivity.allowContextBotPanel = z;
                ChatActivity.this.checkContextBotPanel();
            }

            public void onStickersTab(boolean opened) {
                if (ChatActivity.this.emojiButtonRed != null) {
                    ChatActivity.this.emojiButtonRed.setVisibility(8);
                }
                ChatActivity.this.allowContextBotPanelSecond = !opened;
                ChatActivity.this.checkContextBotPanel();
            }

            public void didPressedAttachButton() {
                if (ChatActivity.this.chatAttachAlert != null) {
                    ChatActivity.this.chatAttachAlert.setEditingMessageObject(null);
                }
                ChatActivity.this.openAttachMenu();
            }

            public void needStartRecordVideo(int state) {
                if (ChatActivity.this.instantCameraView == null) {
                    return;
                }
                if (state == 0) {
                    ChatActivity.this.instantCameraView.showCamera();
                } else if (state == 1 || state == 3 || state == 4) {
                    ChatActivity.this.instantCameraView.send(state);
                } else if (state == 2) {
                    ChatActivity.this.instantCameraView.cancel();
                }
            }

            public void needChangeVideoPreviewState(int state, float seekProgress) {
                if (ChatActivity.this.instantCameraView != null) {
                    ChatActivity.this.instantCameraView.changeVideoPreviewState(state, seekProgress);
                }
            }

            public void needStartRecordAudio(int state) {
                int visibility = state == 0 ? 8 : 0;
                if (ChatActivity.this.overlayView.getVisibility() != visibility) {
                    ChatActivity.this.overlayView.setVisibility(visibility);
                }
            }

            public void needShowMediaBanHint() {
                ChatActivity.this.showMediaBannedHint();
            }

            public void onStickersExpandedChange() {
                ChatActivity.this.checkRaiseSensors();
            }
        });
        fragmentContextView = new FrameLayout(context) {
            public void setTranslationY(float translationY) {
                super.setTranslationY(translationY);
                if (ChatActivity.this.chatActivityEnterView != null) {
                    ChatActivity.this.chatActivityEnterView.invalidate();
                }
                if (getVisibility() != 8) {
                    int height = getLayoutParams().height;
                    if (ChatActivity.this.chatListView != null) {
                        ChatActivity.this.chatListView.setTranslationY(translationY);
                    }
                    if (ChatActivity.this.progressView != null) {
                        ChatActivity.this.progressView.setTranslationY(translationY);
                    }
                    if (ChatActivity.this.mentionContainer != null) {
                        ChatActivity.this.mentionContainer.setTranslationY(translationY);
                    }
                    if (ChatActivity.this.pagedownButton != null) {
                        ChatActivity.this.pagedownButton.setTranslationY(translationY);
                    }
                    if (ChatActivity.this.mentiondownButton != null) {
                        FrameLayout access$10300 = ChatActivity.this.mentiondownButton;
                        if (ChatActivity.this.pagedownButton.getVisibility() == 0) {
                            translationY -= (float) AndroidUtilities.dp(72.0f);
                        }
                        access$10300.setTranslationY(translationY);
                    }
                }
            }

            public boolean hasOverlappingRendering() {
                return false;
            }

            public void setVisibility(int visibility) {
                float f = 0.0f;
                super.setVisibility(visibility);
                if (visibility == 8) {
                    FrameLayout access$10100;
                    if (ChatActivity.this.chatListView != null) {
                        ChatActivity.this.chatListView.setTranslationY(0.0f);
                    }
                    if (ChatActivity.this.progressView != null) {
                        ChatActivity.this.progressView.setTranslationY(0.0f);
                    }
                    if (ChatActivity.this.mentionContainer != null) {
                        ChatActivity.this.mentionContainer.setTranslationY(0.0f);
                    }
                    if (ChatActivity.this.pagedownButton != null) {
                        access$10100 = ChatActivity.this.pagedownButton;
                        if (ChatActivity.this.pagedownButton.getTag() == null) {
                            f = (float) AndroidUtilities.dp(100.0f);
                        }
                        access$10100.setTranslationY(f);
                    }
                    if (ChatActivity.this.mentiondownButton != null) {
                        access$10100 = ChatActivity.this.mentiondownButton;
                        if (ChatActivity.this.mentiondownButton.getTag() == null) {
                            f = (float) AndroidUtilities.dp(100.0f);
                        } else {
                            f = (float) (ChatActivity.this.pagedownButton.getVisibility() == 0 ? -AndroidUtilities.dp(72.0f) : 0);
                        }
                        access$10100.setTranslationY(f);
                    }
                }
            }
        };
        this.chatActivityEnterView.addTopView(fragmentContextView, 48);
        fragmentContextView.setOnClickListener(new ChatActivity$$Lambda$18(this));
        this.replyLineView = new View(context);
        this.replyLineView.setBackgroundColor(Theme.getColor(Theme.key_chat_replyPanelLine));
        fragmentContextView.addView(this.replyLineView, LayoutHelper.createFrame(-1, 1, 83));
        this.replyIconImageView = new ImageView(context);
        this.replyIconImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_replyPanelIcons), Mode.MULTIPLY));
        this.replyIconImageView.setScaleType(ScaleType.CENTER);
        fragmentContextView.addView(this.replyIconImageView, LayoutHelper.createFrame(52, 46, 51));
        this.replyCloseImageView = new ImageView(context);
        this.replyCloseImageView.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_replyPanelClose), Mode.MULTIPLY));
        this.replyCloseImageView.setImageResource(R.drawable.msg_panel_clear);
        this.replyCloseImageView.setScaleType(ScaleType.CENTER);
        fragmentContextView.addView(this.replyCloseImageView, LayoutHelper.createFrame(52, 46.0f, 53, 0.0f, 0.5f, 0.0f, 0.0f));
        this.replyCloseImageView.setOnClickListener(new ChatActivity$$Lambda$19(this));
        this.replyNameTextView = new SimpleTextView(context);
        this.replyNameTextView.setTextSize(14);
        this.replyNameTextView.setTextColor(Theme.getColor(Theme.key_chat_replyPanelName));
        this.replyNameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        fragmentContextView.addView(this.replyNameTextView, LayoutHelper.createFrame(-1, 18.0f, 51, 52.0f, 6.0f, 52.0f, 0.0f));
        this.replyObjectTextView = new SimpleTextView(context);
        this.replyObjectTextView.setTextSize(14);
        this.replyObjectTextView.setTextColor(Theme.getColor(Theme.key_chat_replyPanelMessage));
        fragmentContextView.addView(this.replyObjectTextView, LayoutHelper.createFrame(-1, 18.0f, 51, 52.0f, 24.0f, 52.0f, 0.0f));
        this.replyImageView = new BackupImageView(context);
        fragmentContextView.addView(this.replyImageView, LayoutHelper.createFrame(34, 34.0f, 51, 52.0f, 6.0f, 0.0f, 0.0f));
        this.stickersPanel = new FrameLayout(context);
        this.stickersPanel.setVisibility(8);
        this.contentView.addView(this.stickersPanel, LayoutHelper.createFrame(-2, 81.5f, 83, 0.0f, 0.0f, 0.0f, 38.0f));
        StickerPreviewViewerDelegate anonymousClass24 = new StickerPreviewViewerDelegate() {
            public void sendSticker(Document sticker) {
            }

            public boolean needSend() {
                return false;
            }

            public void openSet(InputStickerSet set) {
                if (set != null && ChatActivity.this.getParentActivity() != null) {
                    TL_inputStickerSetID inputStickerSet = new TL_inputStickerSetID();
                    inputStickerSet.access_hash = set.access_hash;
                    inputStickerSet.id = set.id;
                    ChatActivity.this.showDialog(new StickersAlert(ChatActivity.this.getParentActivity(), ChatActivity.this, inputStickerSet, null, ChatActivity.this.chatActivityEnterView));
                }
            }
        };
        final StickerPreviewViewerDelegate stickerPreviewViewerDelegate = anonymousClass24;
        this.stickersListView = new RecyclerListView(context) {
            public boolean onInterceptTouchEvent(MotionEvent event) {
                boolean result = StickerPreviewViewer.getInstance().onInterceptTouchEvent(event, ChatActivity.this.stickersListView, 0, stickerPreviewViewerDelegate);
                if (super.onInterceptTouchEvent(event) || result) {
                    return true;
                }
                return false;
            }
        };
        this.stickersListView.setTag(Integer.valueOf(3));
        this.stickersListView.setOnTouchListener(new ChatActivity$$Lambda$20(this, anonymousClass24));
        this.stickersListView.setDisallowInterceptTouchEvents(true);
        LayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(0);
        this.stickersListView.setLayoutManager(linearLayoutManager);
        this.stickersListView.setClipToPadding(false);
        this.stickersListView.setOverScrollMode(2);
        this.stickersPanel.addView(this.stickersListView, LayoutHelper.createFrame(-1, 78.0f));
        initStickers();
        this.stickersPanelArrow = new ImageView(context);
        this.stickersPanelArrow.setImageResource(R.drawable.stickers_back_arrow);
        this.stickersPanelArrow.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_stickersHintPanel), Mode.MULTIPLY));
        this.stickersPanel.addView(this.stickersPanelArrow, LayoutHelper.createFrame(-2, -2.0f, 83, 53.0f, 0.0f, 0.0f, 0.0f));
        this.searchContainer = new FrameLayout(context) {
            public void onDraw(Canvas canvas) {
                int bottom = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                Theme.chat_composeShadowDrawable.setBounds(0, 0, getMeasuredWidth(), bottom);
                Theme.chat_composeShadowDrawable.draw(canvas);
                canvas.drawRect(0.0f, (float) bottom, (float) getMeasuredWidth(), (float) getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
            }
        };
        this.searchContainer.setOnTouchListener(ChatActivity$$Lambda$21.$instance);
        this.searchContainer.setWillNotDraw(false);
        this.searchContainer.setVisibility(4);
        this.searchContainer.setFocusable(true);
        this.searchContainer.setFocusableInTouchMode(true);
        this.searchContainer.setClickable(true);
        this.searchContainer.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
        this.searchUpButton = new ImageView(context);
        this.searchUpButton.setScaleType(ScaleType.CENTER);
        this.searchUpButton.setImageResource(R.drawable.search_up);
        this.searchUpButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_searchPanelIcons), Mode.MULTIPLY));
        this.searchContainer.addView(this.searchUpButton, LayoutHelper.createFrame(48, 48.0f, 53, 0.0f, 0.0f, 48.0f, 0.0f));
        this.searchUpButton.setOnClickListener(new ChatActivity$$Lambda$22(this));
        this.searchDownButton = new ImageView(context);
        this.searchDownButton.setScaleType(ScaleType.CENTER);
        this.searchDownButton.setImageResource(R.drawable.search_down);
        this.searchDownButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_searchPanelIcons), Mode.MULTIPLY));
        this.searchContainer.addView(this.searchDownButton, LayoutHelper.createFrame(48, 48.0f, 53, 0.0f, 0.0f, 0.0f, 0.0f));
        this.searchDownButton.setOnClickListener(new ChatActivity$$Lambda$23(this));
        if (this.currentChat != null && (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup)) {
            this.searchUserButton = new ImageView(context);
            this.searchUserButton.setScaleType(ScaleType.CENTER);
            this.searchUserButton.setImageResource(R.drawable.usersearch);
            this.searchUserButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_searchPanelIcons), Mode.MULTIPLY));
            this.searchContainer.addView(this.searchUserButton, LayoutHelper.createFrame(48, 48.0f, 51, 48.0f, 0.0f, 0.0f, 0.0f));
            this.searchUserButton.setOnClickListener(new ChatActivity$$Lambda$24(this));
        }
        this.searchCalendarButton = new ImageView(context);
        this.searchCalendarButton.setScaleType(ScaleType.CENTER);
        this.searchCalendarButton.setImageResource(R.drawable.search_calendar);
        this.searchCalendarButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_searchPanelIcons), Mode.MULTIPLY));
        this.searchContainer.addView(this.searchCalendarButton, LayoutHelper.createFrame(48, 48, 51));
        this.searchCalendarButton.setOnClickListener(new ChatActivity$$Lambda$25(this));
        this.searchCountText = new SimpleTextView(context);
        this.searchCountText.setTextColor(Theme.getColor(Theme.key_chat_searchPanelText));
        this.searchCountText.setTextSize(15);
        this.searchCountText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.searchCountText.setGravity(5);
        this.searchContainer.addView(this.searchCountText, LayoutHelper.createFrame(-2, -2.0f, 21, 0.0f, 0.0f, 108.0f, 0.0f));
        this.bottomOverlay = new FrameLayout(context) {
            public void onDraw(Canvas canvas) {
                int bottom = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                Theme.chat_composeShadowDrawable.setBounds(0, 0, getMeasuredWidth(), bottom);
                Theme.chat_composeShadowDrawable.draw(canvas);
                canvas.drawRect(0.0f, (float) bottom, (float) getMeasuredWidth(), (float) getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
            }
        };
        this.bottomOverlay.setWillNotDraw(false);
        this.bottomOverlay.setVisibility(4);
        this.bottomOverlay.setFocusable(true);
        this.bottomOverlay.setFocusableInTouchMode(true);
        this.bottomOverlay.setClickable(true);
        this.bottomOverlay.setPadding(0, AndroidUtilities.dp(2.0f), 0, 0);
        this.contentView.addView(this.bottomOverlay, LayoutHelper.createFrame(-1, 51, 80));
        this.bottomOverlayText = new TextView(context);
        this.bottomOverlayText.setTextSize(1, 14.0f);
        this.bottomOverlayText.setGravity(17);
        this.bottomOverlayText.setMaxLines(2);
        this.bottomOverlayText.setEllipsize(TruncateAt.END);
        this.bottomOverlayText.setLineSpacing((float) AndroidUtilities.dp(2.0f), 1.0f);
        this.bottomOverlayText.setTextColor(Theme.getColor(Theme.key_chat_secretChatStatusText));
        this.bottomOverlay.addView(this.bottomOverlayText, LayoutHelper.createFrame(-2, -2.0f, 17, 14.0f, 0.0f, 14.0f, 0.0f));
        this.bottomOverlayChat = new FrameLayout(context) {
            public void onDraw(Canvas canvas) {
                int bottom = Theme.chat_composeShadowDrawable.getIntrinsicHeight();
                Theme.chat_composeShadowDrawable.setBounds(0, 0, getMeasuredWidth(), bottom);
                Theme.chat_composeShadowDrawable.draw(canvas);
                canvas.drawRect(0.0f, (float) bottom, (float) getMeasuredWidth(), (float) getMeasuredHeight(), Theme.chat_composeBackgroundPaint);
            }
        };
        this.bottomOverlayChat.setWillNotDraw(false);
        this.bottomOverlayChat.setPadding(0, AndroidUtilities.dp(3.0f), 0, 0);
        this.bottomOverlayChat.setVisibility(4);
        this.contentView.addView(this.bottomOverlayChat, LayoutHelper.createFrame(-1, 51, 80));
        this.bottomOverlayChat.setOnClickListener(new ChatActivity$$Lambda$26(this));
        this.bottomOverlayChatText = new TextView(context);
        this.bottomOverlayChatText.setTextSize(1, 15.0f);
        this.bottomOverlayChatText.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.bottomOverlayChatText.setTextColor(Theme.getColor(Theme.key_chat_fieldOverlayText));
        this.bottomOverlayChat.addView(this.bottomOverlayChatText, LayoutHelper.createFrame(-2, -2, 17));
        this.contentView.addView(this.searchContainer, LayoutHelper.createFrame(-1, 51, 80));
        this.chatAdapter.updateRows();
        if (this.loading && this.messages.isEmpty()) {
            this.progressView.setVisibility(this.chatAdapter.botInfoRow == -1 ? 0 : 4);
            this.chatListView.setEmptyView(null);
        } else {
            this.progressView.setVisibility(4);
            this.chatListView.setEmptyView(this.emptyViewContainer);
        }
        checkBotKeyboard();
        updateContactStatus();
        updateBottomOverlay();
        updateSecretStatus();
        updateSpamView();
        updatePinnedMessageView(true);
        try {
            if (this.currentEncryptedChat != null && VERSION.SDK_INT >= 23 && (SharedConfig.passcodeHash.length() == 0 || SharedConfig.allowScreenCapture)) {
                MediaController.getInstance().setFlagSecure(this, true);
            }
        } catch (Throwable e2) {
            FileLog.e(e2);
        }
        if (oldMessage != null) {
            this.chatActivityEnterView.setFieldText(oldMessage);
        }
        fixLayoutInternal();
        return this.fragmentView;
    }

    final /* synthetic */ void lambda$createView$6$ChatActivity(View view) {
        if (this.floatingDateView.getAlpha() != 0.0f) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(((long) this.floatingDateView.getCustomDate()) * 1000);
            int year = calendar.get(1);
            int monthOfYear = calendar.get(2);
            int dayOfMonth = calendar.get(5);
            calendar.clear();
            calendar.set(year, monthOfYear, dayOfMonth);
            jumpToDate((int) (calendar.getTime().getTime() / 1000));
        }
    }

    final /* synthetic */ void lambda$createView$7$ChatActivity(View v) {
        this.wasManualScroll = true;
        scrollToMessageId(this.info.pinned_msg_id, 0, true, 0, false);
    }

    final /* synthetic */ void lambda$createView$9$ChatActivity(View v) {
        if (getParentActivity() != null) {
            if (this.currentChat.creator || (this.currentChat.admin_rights != null && ((this.currentChat.megagroup && this.currentChat.admin_rights.pin_messages) || (!this.currentChat.megagroup && this.currentChat.admin_rights.edit_messages)))) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setMessage(LocaleController.getString("UnpinMessageAlert", R.string.UnpinMessageAlert));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$84(this));
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                showDialog(builder.create());
                return;
            }
            MessagesController.getNotificationsSettings(this.currentAccount).edit().putInt("pin_" + this.dialog_id, this.info.pinned_msg_id).commit();
            updatePinnedMessageView(true);
        }
    }

    final /* synthetic */ void lambda$null$8$ChatActivity(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).pinChannelMessage(this.currentChat, 0, false);
    }

    final /* synthetic */ void lambda$createView$10$ChatActivity(View v) {
        Bundle args = new Bundle();
        args.putInt("user_id", this.currentUser.id);
        args.putBoolean("addContact", true);
        presentFragment(new ContactAddActivity(args));
    }

    final /* synthetic */ void lambda$createView$12$ChatActivity(View v) {
        if (getParentActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            if (ChatObject.isChannel(this.currentChat) && !this.currentChat.megagroup) {
                builder.setMessage(LocaleController.getString("ReportSpamAlertChannel", R.string.ReportSpamAlertChannel));
            } else if (this.currentChat != null) {
                builder.setMessage(LocaleController.getString("ReportSpamAlertGroup", R.string.ReportSpamAlertGroup));
            } else {
                builder.setMessage(LocaleController.getString("ReportSpamAlert", R.string.ReportSpamAlert));
            }
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$83(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder.create());
        }
    }

    final /* synthetic */ void lambda$null$11$ChatActivity(DialogInterface dialogInterface, int i) {
        if (this.currentUser != null) {
            MessagesController.getInstance(this.currentAccount).blockUser(this.currentUser.id);
        }
        MessagesController.getInstance(this.currentAccount).reportSpam(this.dialog_id, this.currentUser, this.currentChat, this.currentEncryptedChat);
        updateSpamView();
        if (this.currentChat == null) {
            MessagesController.getInstance(this.currentAccount).deleteDialog(this.dialog_id, 0);
        } else if (ChatObject.isNotInChat(this.currentChat)) {
            MessagesController.getInstance(this.currentAccount).deleteDialog(this.dialog_id, 0);
        } else {
            MessagesController.getInstance(this.currentAccount).deleteUserFromChat((int) (-this.dialog_id), MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(UserConfig.getInstance(this.currentAccount).getClientUserId())), null);
        }
        finishFragment();
    }

    final /* synthetic */ void lambda$createView$13$ChatActivity(View v) {
        MessagesController.getInstance(this.currentAccount).hideReportSpam(this.dialog_id, this.currentUser, this.currentChat);
        updateSpamView();
    }

    final /* synthetic */ void lambda$createView$14$ChatActivity(View view) {
        this.wasManualScroll = true;
        this.checkTextureViewPosition = true;
        if (this.createUnreadMessageAfterId != 0) {
            scrollToMessageId(this.createUnreadMessageAfterId, 0, false, this.returnToLoadIndex, false);
        } else if (this.returnToMessageId > 0) {
            scrollToMessageId(this.returnToMessageId, 0, true, this.returnToLoadIndex, false);
        } else {
            scrollToLastMessage(true);
        }
    }

    final /* synthetic */ boolean lambda$createView$15$ChatActivity(View view) {
        for (int a = 0; a < this.messages.size(); a++) {
            MessageObject messageObject = (MessageObject) this.messages.get(a);
            if (messageObject.messageOwner.mentioned && !messageObject.isContentUnread()) {
                messageObject.setContentIsRead();
            }
        }
        this.newMentionsCount = 0;
        MessagesController.getInstance(this.currentAccount).markMentionsAsRead(this.dialog_id);
        this.hasAllMentionsLocal = true;
        showMentiondownButton(false, true);
        return true;
    }

    final /* synthetic */ boolean lambda$createView$16$ChatActivity(View v, MotionEvent event) {
        return StickerPreviewViewer.getInstance().onTouch(event, this.mentionListView, 0, this.mentionsOnItemClickListener, null);
    }

    final /* synthetic */ void lambda$createView$17$ChatActivity(View view, int position) {
        if (!this.mentionsAdapter.isBannedInline()) {
            TLObject object = this.mentionsAdapter.getItem(position);
            int start = this.mentionsAdapter.getResultStartPosition();
            int len = this.mentionsAdapter.getResultLength();
            if (object instanceof User) {
                Spannable spannableString;
                if (this.searchingForUser && this.searchContainer.getVisibility() == 0) {
                    this.searchingUserMessages = (User) object;
                    if (this.searchingUserMessages != null) {
                        String name = this.searchingUserMessages.first_name;
                        if (TextUtils.isEmpty(name)) {
                            name = this.searchingUserMessages.last_name;
                        }
                        this.searchingForUser = false;
                        String from = LocaleController.getString("SearchFrom", R.string.SearchFrom);
                        spannableString = new SpannableString(from + " " + name);
                        spannableString.setSpan(new ForegroundColorSpan(Theme.getColor(Theme.key_actionBarDefaultSubtitle)), from.length() + 1, spannableString.length(), 33);
                        this.searchItem.setSearchFieldCaption(spannableString);
                        this.mentionsAdapter.searchUsernameOrHashtag(null, 0, null, false);
                        this.searchItem.getSearchField().setHint(null);
                        this.searchItem.clearSearchText();
                        DataQuery.getInstance(this.currentAccount).searchMessagesInChat(TtmlNode.ANONYMOUS_REGION_ID, this.dialog_id, this.mergeDialogId, this.classGuid, 0, this.searchingUserMessages);
                        return;
                    }
                    return;
                }
                User user = (User) object;
                if (user == null) {
                    return;
                }
                if (user.username != null) {
                    this.chatActivityEnterView.replaceWithText(start, len, "@" + user.username + " ", false);
                    return;
                }
                spannableString = new SpannableString(UserObject.getFirstName(user, false) + " ");
                spannableString.setSpan(new URLSpanUserMention(TtmlNode.ANONYMOUS_REGION_ID + user.id, 1), 0, spannableString.length(), 33);
                this.chatActivityEnterView.replaceWithText(start, len, spannableString, false);
            } else if (object instanceof String) {
                if (this.mentionsAdapter.isBotCommands()) {
                    SendMessagesHelper.getInstance(this.currentAccount).sendMessage((String) object, this.dialog_id, this.replyingMessageObject, null, false, null, null, null);
                    this.chatActivityEnterView.setFieldText(TtmlNode.ANONYMOUS_REGION_ID);
                    hideFieldPanel();
                    return;
                }
                this.chatActivityEnterView.replaceWithText(start, len, object + " ", false);
            } else if (object instanceof BotInlineResult) {
                if (this.chatActivityEnterView.getFieldText() != null) {
                    BotInlineResult result = (BotInlineResult) object;
                    if ((!result.type.equals("photo") || (result.photo == null && result.content == null)) && ((!result.type.equals("gif") || (result.document == null && result.content == null)) && (!result.type.equals(MimeTypes.BASE_TYPE_VIDEO) || result.document == null))) {
                        sendBotInlineResult(result);
                        return;
                    }
                    ArrayList<Object> arrayList = new ArrayList(this.mentionsAdapter.getSearchResultBotContext());
                    this.botContextResults = arrayList;
                    PhotoViewer.getInstance().setParentActivity(getParentActivity());
                    PhotoViewer.getInstance().openPhotoForSelect(arrayList, this.mentionsAdapter.getItemPosition(position), 3, this.botContextProvider, null);
                }
            } else if (object instanceof TL_inlineBotSwitchPM) {
                processInlineBotContextPM((TL_inlineBotSwitchPM) object);
            } else if (object instanceof EmojiSuggestion) {
                String code = ((EmojiSuggestion) object).emoji;
                this.chatActivityEnterView.addEmojiToRecent(code);
                this.chatActivityEnterView.replaceWithText(start, len, code, true);
            }
        }
    }

    final /* synthetic */ boolean lambda$createView$19$ChatActivity(View view, int position) {
        boolean z = false;
        if (getParentActivity() == null || !this.mentionsAdapter.isLongClickEnabled()) {
            return false;
        }
        Object object = this.mentionsAdapter.getItem(position);
        if (!(object instanceof String)) {
            return false;
        }
        if (!this.mentionsAdapter.isBotCommands()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(LocaleController.getString("ClearSearch", R.string.ClearSearch));
            builder.setPositiveButton(LocaleController.getString("ClearButton", R.string.ClearButton).toUpperCase(), new ChatActivity$$Lambda$82(this));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder.create());
            return true;
        } else if (!URLSpanBotCommand.enabled) {
            return false;
        } else {
            this.chatActivityEnterView.setFieldText(TtmlNode.ANONYMOUS_REGION_ID);
            ChatActivityEnterView chatActivityEnterView = this.chatActivityEnterView;
            String str = (String) object;
            if (this.currentChat != null && this.currentChat.megagroup) {
                z = true;
            }
            chatActivityEnterView.setCommand(null, str, true, z);
            return true;
        }
    }

    final /* synthetic */ void lambda$null$18$ChatActivity(DialogInterface dialogInterface, int i) {
        this.mentionsAdapter.clearRecentHashtags();
    }

    final /* synthetic */ boolean lambda$createView$20$ChatActivity(View v, MotionEvent event) {
        if (event.getAction() == 0) {
            checkRecordLocked();
        }
        this.overlayView.getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }

    final /* synthetic */ void lambda$createView$21$ChatActivity(View v) {
        if (this.replyingMessageObject != null) {
            scrollToMessageId(this.replyingMessageObject.getId(), 0, true, 0, false);
        } else if (this.editingMessageObject != null && this.editingMessageObject.canEditMedia() && this.editingMessageObjectReqId == 0) {
            if (this.chatAttachAlert == null) {
                createChatAttachView();
            }
            this.chatAttachAlert.setEditingMessageObject(this.editingMessageObject);
            openAttachMenu();
        }
    }

    final /* synthetic */ void lambda$createView$22$ChatActivity(View v) {
        if (this.forwardingMessages != null) {
            this.forwardingMessages.clear();
        }
        showFieldPanel(false, null, null, null, this.foundWebPage, true);
    }

    final /* synthetic */ boolean lambda$createView$23$ChatActivity(StickerPreviewViewerDelegate stickerPreviewViewerDelegate, View v, MotionEvent event) {
        return StickerPreviewViewer.getInstance().onTouch(event, this.stickersListView, 0, this.stickersOnItemClickListener, stickerPreviewViewerDelegate);
    }

    final /* synthetic */ void lambda$createView$25$ChatActivity(View view) {
        DataQuery.getInstance(this.currentAccount).searchMessagesInChat(null, this.dialog_id, this.mergeDialogId, this.classGuid, 1, this.searchingUserMessages);
    }

    final /* synthetic */ void lambda$createView$26$ChatActivity(View view) {
        DataQuery.getInstance(this.currentAccount).searchMessagesInChat(null, this.dialog_id, this.mergeDialogId, this.classGuid, 2, this.searchingUserMessages);
    }

    final /* synthetic */ void lambda$createView$27$ChatActivity(View view) {
        this.mentionLayoutManager.setReverseLayout(true);
        this.mentionsAdapter.setSearchingMentions(true);
        this.searchCalendarButton.setVisibility(8);
        this.searchUserButton.setVisibility(8);
        this.searchingForUser = true;
        this.searchingUserMessages = null;
        this.searchItem.getSearchField().setHint(LocaleController.getString("SearchMembers", R.string.SearchMembers));
        this.searchItem.setSearchFieldCaption(LocaleController.getString("SearchFrom", R.string.SearchFrom));
        AndroidUtilities.showKeyboard(this.searchItem.getSearchField());
        this.searchItem.clearSearchText();
    }

    final /* synthetic */ void lambda$createView$31$ChatActivity(View view) {
        if (getParentActivity() != null) {
            AndroidUtilities.hideKeyboard(this.searchItem.getSearchField());
            Calendar calendar = Calendar.getInstance();
            try {
                DatePickerDialog dialog = new DatePickerDialog(getParentActivity(), new ChatActivity$$Lambda$79(this), calendar.get(1), calendar.get(2), calendar.get(5));
                DatePicker datePicker = dialog.getDatePicker();
                datePicker.setMinDate(1375315200000L);
                datePicker.setMaxDate(System.currentTimeMillis());
                dialog.setButton(-1, LocaleController.getString("JumpToDate", R.string.JumpToDate), dialog);
                dialog.setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), ChatActivity$$Lambda$80.$instance);
                if (VERSION.SDK_INT >= 21) {
                    dialog.setOnShowListener(new ChatActivity$$Lambda$81(datePicker));
                }
                showDialog(dialog);
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    final /* synthetic */ void lambda$null$28$ChatActivity(DatePicker view1, int year1, int month, int dayOfMonth1) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.clear();
        calendar1.set(year1, month, dayOfMonth1);
        int date = (int) (calendar1.getTime().getTime() / 1000);
        clearChatData();
        this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
        MessagesController instance = MessagesController.getInstance(this.currentAccount);
        long j = this.dialog_id;
        int i = this.classGuid;
        boolean isChannel = ChatObject.isChannel(this.currentChat);
        int i2 = this.lastLoadIndex;
        this.lastLoadIndex = i2 + 1;
        instance.loadMessages(j, 30, 0, date, true, 0, i, 4, 0, isChannel, i2);
    }

    static final /* synthetic */ void lambda$null$29$ChatActivity(DialogInterface dialog1, int which) {
    }

    static final /* synthetic */ void lambda$null$30$ChatActivity(DatePicker datePicker, DialogInterface dialog12) {
        int count = datePicker.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = datePicker.getChildAt(a);
            ViewGroup.LayoutParams layoutParams = child.getLayoutParams();
            layoutParams.width = -1;
            child.setLayoutParams(layoutParams);
        }
    }

    final /* synthetic */ void lambda$createView$34$ChatActivity(View view) {
        if (getParentActivity() != null) {
            AlertDialog.Builder builder = null;
            if (this.currentUser == null || !this.userBlocked) {
                if (this.currentUser != null && this.currentUser.bot && this.botUser != null) {
                    if (this.botUser.length() != 0) {
                        MessagesController.getInstance(this.currentAccount).sendBotStart(this.currentUser, this.botUser);
                    } else {
                        SendMessagesHelper.getInstance(this.currentAccount).sendMessage("/start", this.dialog_id, null, null, false, null, null, null);
                    }
                    this.botUser = null;
                    updateBottomOverlay();
                } else if (!ChatObject.isChannel(this.currentChat) || (this.currentChat instanceof TL_channelForbidden)) {
                    builder = new AlertDialog.Builder(getParentActivity());
                    builder.setMessage(LocaleController.getString("AreYouSureDeleteThisChat", R.string.AreYouSureDeleteThisChat));
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$78(this));
                } else if (ChatObject.isNotInChat(this.currentChat)) {
                    MessagesController.getInstance(this.currentAccount).addUserToChat(this.currentChat.id, UserConfig.getInstance(this.currentAccount).getCurrentUser(), null, 0, null, this);
                    NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.closeSearchByActiveAction, new Object[0]);
                } else {
                    toggleMute(true);
                }
            } else if (this.currentUser.bot) {
                String botUserLast = this.botUser;
                this.botUser = null;
                MessagesController.getInstance(this.currentAccount).unblockUser(this.currentUser.id);
                if (botUserLast == null || botUserLast.length() == 0) {
                    SendMessagesHelper.getInstance(this.currentAccount).sendMessage("/start", this.dialog_id, null, null, false, null, null, null);
                } else {
                    MessagesController.getInstance(this.currentAccount).sendBotStart(this.currentUser, botUserLast);
                }
            } else {
                builder = new AlertDialog.Builder(getParentActivity());
                builder.setMessage(LocaleController.getString("AreYouSureUnblockContact", R.string.AreYouSureUnblockContact));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$77(this));
            }
            if (builder != null) {
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                showDialog(builder.create());
            }
        }
    }

    final /* synthetic */ void lambda$null$32$ChatActivity(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).unblockUser(this.currentUser.id);
    }

    final /* synthetic */ void lambda$null$33$ChatActivity(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).deleteDialog(this.dialog_id, 0);
        finishFragment();
    }

    private TextureView createTextureView(boolean add) {
        if (this.parentLayout == null) {
            return null;
        }
        if (this.roundVideoContainer == null) {
            if (VERSION.SDK_INT >= 21) {
                this.roundVideoContainer = new FrameLayout(getParentActivity()) {
                    public void setTranslationY(float translationY) {
                        super.setTranslationY(translationY);
                        ChatActivity.this.contentView.invalidate();
                    }
                };
                this.roundVideoContainer.setOutlineProvider(new ViewOutlineProvider() {
                    @TargetApi(21)
                    public void getOutline(View view, Outline outline) {
                        outline.setOval(0, 0, AndroidUtilities.roundMessageSize, AndroidUtilities.roundMessageSize);
                    }
                });
                this.roundVideoContainer.setClipToOutline(true);
            } else {
                this.roundVideoContainer = new FrameLayout(getParentActivity()) {
                    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
                        super.onSizeChanged(w, h, oldw, oldh);
                        ChatActivity.this.aspectPath.reset();
                        ChatActivity.this.aspectPath.addCircle((float) (w / 2), (float) (h / 2), (float) (w / 2), Direction.CW);
                        ChatActivity.this.aspectPath.toggleInverseFillType();
                    }

                    public void setTranslationY(float translationY) {
                        super.setTranslationY(translationY);
                        ChatActivity.this.contentView.invalidate();
                    }

                    public void setVisibility(int visibility) {
                        super.setVisibility(visibility);
                        if (visibility == 0) {
                            setLayerType(2, null);
                        }
                    }

                    protected void dispatchDraw(Canvas canvas) {
                        super.dispatchDraw(canvas);
                        canvas.drawPath(ChatActivity.this.aspectPath, ChatActivity.this.aspectPaint);
                    }
                };
                this.aspectPath = new Path();
                this.aspectPaint = new Paint(1);
                this.aspectPaint.setColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
                this.aspectPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
            }
            this.roundVideoContainer.setWillNotDraw(false);
            this.roundVideoContainer.setVisibility(4);
            this.aspectRatioFrameLayout = new AspectRatioFrameLayout(getParentActivity());
            this.aspectRatioFrameLayout.setBackgroundColor(0);
            if (add) {
                this.roundVideoContainer.addView(this.aspectRatioFrameLayout, LayoutHelper.createFrame(-1, -1.0f));
            }
            this.videoTextureView = new TextureView(getParentActivity());
            this.videoTextureView.setOpaque(false);
            this.aspectRatioFrameLayout.addView(this.videoTextureView, LayoutHelper.createFrame(-1, -1.0f));
        }
        ViewGroup parent = (ViewGroup) this.roundVideoContainer.getParent();
        if (!(parent == null || parent == this.contentView)) {
            parent.removeView(this.roundVideoContainer);
            parent = null;
        }
        if (parent == null) {
            this.contentView.addView(this.roundVideoContainer, 1, new FrameLayout.LayoutParams(AndroidUtilities.roundMessageSize, AndroidUtilities.roundMessageSize));
        }
        this.roundVideoContainer.setVisibility(4);
        this.aspectRatioFrameLayout.setDrawingReady(false);
        return this.videoTextureView;
    }

    private void destroyTextureView() {
        if (this.roundVideoContainer != null && this.roundVideoContainer.getParent() != null) {
            this.contentView.removeView(this.roundVideoContainer);
            this.aspectRatioFrameLayout.setDrawingReady(false);
            this.roundVideoContainer.setVisibility(4);
            if (VERSION.SDK_INT < 21) {
                this.roundVideoContainer.setLayerType(0, null);
            }
        }
    }

    private void sendBotInlineResult(BotInlineResult result) {
        int uid = this.mentionsAdapter.getContextBotId();
        HashMap<String, String> params = new HashMap();
        params.put(TtmlNode.ATTR_ID, result.id);
        params.put("query_id", TtmlNode.ANONYMOUS_REGION_ID + result.query_id);
        params.put("bot", TtmlNode.ANONYMOUS_REGION_ID + uid);
        params.put("bot_name", this.mentionsAdapter.getContextBotName());
        SendMessagesHelper.prepareSendingBotContextResult(result, params, this.dialog_id, this.replyingMessageObject);
        this.chatActivityEnterView.setFieldText(TtmlNode.ANONYMOUS_REGION_ID);
        hideFieldPanel();
        DataQuery.getInstance(this.currentAccount).increaseInlineRaiting(uid);
    }

    private void mentionListViewUpdateLayout() {
        if (this.mentionListView.getChildCount() <= 0) {
            this.mentionListViewScrollOffsetY = 0;
            this.mentionListViewLastViewPosition = -1;
            return;
        }
        View child = this.mentionListView.getChildAt(this.mentionListView.getChildCount() - 1);
        Holder holder = (Holder) this.mentionListView.findContainingViewHolder(child);
        int newOffset;
        if (this.mentionLayoutManager.getReverseLayout()) {
            if (holder != null) {
                this.mentionListViewLastViewPosition = holder.getAdapterPosition();
                this.mentionListViewLastViewTop = child.getBottom();
            } else {
                this.mentionListViewLastViewPosition = -1;
            }
            child = this.mentionListView.getChildAt(0);
            holder = (Holder) this.mentionListView.findContainingViewHolder(child);
            newOffset = (child.getBottom() >= this.mentionListView.getMeasuredHeight() || holder == null || holder.getAdapterPosition() != 0) ? this.mentionListView.getMeasuredHeight() : child.getBottom();
            if (this.mentionListViewScrollOffsetY != newOffset) {
                RecyclerListView recyclerListView = this.mentionListView;
                this.mentionListViewScrollOffsetY = newOffset;
                recyclerListView.setBottomGlowOffset(newOffset);
                this.mentionListView.setTopGlowOffset(0);
                this.mentionListView.invalidate();
                this.mentionContainer.invalidate();
                return;
            }
            return;
        }
        if (holder != null) {
            this.mentionListViewLastViewPosition = holder.getAdapterPosition();
            this.mentionListViewLastViewTop = child.getTop();
        } else {
            this.mentionListViewLastViewPosition = -1;
        }
        child = this.mentionListView.getChildAt(0);
        holder = (Holder) this.mentionListView.findContainingViewHolder(child);
        if (child.getTop() <= 0 || holder == null || holder.getAdapterPosition() != 0) {
            newOffset = 0;
        } else {
            newOffset = child.getTop();
        }
        if (this.mentionListViewScrollOffsetY != newOffset) {
            recyclerListView = this.mentionListView;
            this.mentionListViewScrollOffsetY = newOffset;
            recyclerListView.setTopGlowOffset(newOffset);
            this.mentionListView.setBottomGlowOffset(0);
            this.mentionListView.invalidate();
            this.mentionContainer.invalidate();
        }
    }

    private void checkBotCommands() {
        boolean z = true;
        URLSpanBotCommand.enabled = false;
        if (this.currentUser != null && this.currentUser.bot) {
            URLSpanBotCommand.enabled = true;
        } else if (this.info instanceof TL_chatFull) {
            int a = 0;
            while (a < this.info.participants.participants.size()) {
                User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(((ChatParticipant) this.info.participants.participants.get(a)).user_id));
                if (user == null || !user.bot) {
                    a++;
                } else {
                    URLSpanBotCommand.enabled = true;
                    return;
                }
            }
        } else if (this.info instanceof TL_channelFull) {
            if (this.info.bot_info.isEmpty() || this.currentChat == null || !this.currentChat.megagroup) {
                z = false;
            }
            URLSpanBotCommand.enabled = z;
        }
    }

    private GroupedMessages getValidGroupedMessage(MessageObject message) {
        if (message.getGroupId() == 0) {
            return null;
        }
        GroupedMessages groupedMessages = (GroupedMessages) this.groupedMessagesMap.get(message.getGroupId());
        if (groupedMessages == null) {
            return groupedMessages;
        }
        if (groupedMessages.messages.size() <= 1 || groupedMessages.positions.get(message) == null) {
            return null;
        }
        return groupedMessages;
    }

    private void jumpToDate(int date) {
        if (!this.messages.isEmpty()) {
            MessageObject lastMessage = (MessageObject) this.messages.get(this.messages.size() - 1);
            if (((MessageObject) this.messages.get(0)).messageOwner.date >= date && lastMessage.messageOwner.date <= date) {
                int a = this.messages.size() - 1;
                while (a >= 0) {
                    MessageObject message = (MessageObject) this.messages.get(a);
                    if (message.messageOwner.date < date || message.getId() == 0) {
                        a--;
                    } else {
                        scrollToMessageId(message.getId(), 0, false, message.getDialogId() == this.mergeDialogId ? 1 : 0, false);
                        return;
                    }
                }
            } else if (((int) this.dialog_id) != 0) {
                clearChatData();
                this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
                MessagesController instance = MessagesController.getInstance(this.currentAccount);
                long j = this.dialog_id;
                int i = this.classGuid;
                boolean isChannel = ChatObject.isChannel(this.currentChat);
                int i2 = this.lastLoadIndex;
                this.lastLoadIndex = i2 + 1;
                instance.loadMessages(j, 30, 0, date, true, 0, i, 4, 0, isChannel, i2);
                this.floatingDateView.setAlpha(0.0f);
                this.floatingDateView.setTag(null);
            }
        }
    }

    public void processInlineBotContextPM(TL_inlineBotSwitchPM object) {
        if (object != null) {
            User user = this.mentionsAdapter.getContextBotUser();
            if (user != null) {
                this.chatActivityEnterView.setFieldText(TtmlNode.ANONYMOUS_REGION_ID);
                if (this.dialog_id == ((long) user.id)) {
                    this.inlineReturn = this.dialog_id;
                    MessagesController.getInstance(this.currentAccount).sendBotStart(this.currentUser, object.start_param);
                    return;
                }
                Bundle args = new Bundle();
                args.putInt("user_id", user.id);
                args.putString("inline_query", object.start_param);
                args.putLong("inline_return", this.dialog_id);
                if (MessagesController.getInstance(this.currentAccount).checkCanOpenChat(args, this)) {
                    presentFragment(new ChatActivity(args));
                }
            }
        }
    }

    private void createChatAttachView() {
        if (getParentActivity() != null && this.chatAttachAlert == null) {
            this.chatAttachAlert = new ChatAttachAlert(getParentActivity(), this);
            this.chatAttachAlert.setDelegate(new ChatAttachViewDelegate() {
                public void didPressedButton(int button) {
                    boolean z = false;
                    if (ChatActivity.this.getParentActivity() != null && ChatActivity.this.chatAttachAlert != null) {
                        if (ChatActivity.this.chatAttachAlert != null) {
                            ChatActivity.this.editingMessageObject = ChatActivity.this.chatAttachAlert.getEditingMessageObject();
                        } else {
                            ChatActivity.this.editingMessageObject = null;
                        }
                        if (button == 8 || button == 7 || (button == 4 && !ChatActivity.this.chatAttachAlert.getSelectedPhotos().isEmpty())) {
                            if (button != 8) {
                                ChatActivity.this.chatAttachAlert.dismiss();
                            }
                            HashMap<Object, Object> selectedPhotos = ChatActivity.this.chatAttachAlert.getSelectedPhotos();
                            ArrayList<Object> selectedPhotosOrder = ChatActivity.this.chatAttachAlert.getSelectedPhotosOrder();
                            if (!selectedPhotos.isEmpty()) {
                                ArrayList<SendingMediaInfo> photos = new ArrayList();
                                for (int a = 0; a < selectedPhotosOrder.size(); a++) {
                                    String charSequence;
                                    ArrayList arrayList;
                                    PhotoEntry photoEntry = (PhotoEntry) selectedPhotos.get(selectedPhotosOrder.get(a));
                                    SendingMediaInfo info = new SendingMediaInfo();
                                    if (photoEntry.imagePath != null) {
                                        info.path = photoEntry.imagePath;
                                    } else if (photoEntry.path != null) {
                                        info.path = photoEntry.path;
                                    }
                                    info.isVideo = photoEntry.isVideo;
                                    if (photoEntry.caption != null) {
                                        charSequence = photoEntry.caption.toString();
                                    } else {
                                        charSequence = null;
                                    }
                                    info.caption = charSequence;
                                    info.entities = photoEntry.entities;
                                    if (photoEntry.stickers.isEmpty()) {
                                        arrayList = null;
                                    } else {
                                        arrayList = new ArrayList(photoEntry.stickers);
                                    }
                                    info.masks = arrayList;
                                    info.ttl = photoEntry.ttl;
                                    info.videoEditedInfo = photoEntry.editedInfo;
                                    photos.add(info);
                                    photoEntry.reset();
                                }
                                ChatActivity.this.fillEditingMediaWithCaption(((SendingMediaInfo) photos.get(0)).caption, ((SendingMediaInfo) photos.get(0)).entities);
                                long access$2300 = ChatActivity.this.dialog_id;
                                MessageObject access$2400 = ChatActivity.this.replyingMessageObject;
                                if (button == 4) {
                                    z = true;
                                }
                                SendMessagesHelper.prepareSendingMedia(photos, access$2300, access$2400, null, z, SharedConfig.groupPhotosEnabled, ChatActivity.this.editingMessageObject);
                                ChatActivity.this.hideFieldPanel();
                                DataQuery.getInstance(ChatActivity.this.currentAccount).cleanDraft(ChatActivity.this.dialog_id, true);
                                return;
                            }
                            return;
                        }
                        if (ChatActivity.this.chatAttachAlert != null) {
                            ChatActivity.this.chatAttachAlert.dismissWithButtonClick(button);
                        }
                        ChatActivity.this.processSelectedAttach(button);
                    }
                }

                public View getRevealView() {
                    return ChatActivity.this.chatActivityEnterView.getAttachButton();
                }

                public void didSelectBot(User user) {
                    if (ChatActivity.this.chatActivityEnterView != null && !TextUtils.isEmpty(user.username)) {
                        ChatActivity.this.chatActivityEnterView.setFieldText("@" + user.username + " ");
                        ChatActivity.this.chatActivityEnterView.openKeyboard();
                    }
                }

                public void onCameraOpened() {
                    ChatActivity.this.chatActivityEnterView.closeKeyboard();
                }

                public boolean allowGroupPhotos() {
                    return ChatActivity.this.allowGroupPhotos();
                }
            });
        }
    }

    public long getDialogId() {
        return this.dialog_id;
    }

    public void setBotUser(String value) {
        if (this.inlineReturn != 0) {
            MessagesController.getInstance(this.currentAccount).sendBotStart(this.currentUser, value);
            return;
        }
        this.botUser = value;
        updateBottomOverlay();
    }

    public boolean playFirstUnreadVoiceMessage() {
        if (this.chatActivityEnterView != null && this.chatActivityEnterView.isRecordingAudioVideo()) {
            return true;
        }
        for (int a = this.messages.size() - 1; a >= 0; a--) {
            MessageObject messageObject = (MessageObject) this.messages.get(a);
            if ((messageObject.isVoice() || messageObject.isRoundVideo()) && messageObject.isContentUnread() && !messageObject.isOut()) {
                MediaController.getInstance().setVoiceMessagesPlaylist(MediaController.getInstance().playMessage(messageObject) ? createVoiceMessagesPlaylist(messageObject, true) : null, true);
                return true;
            }
        }
        if (VERSION.SDK_INT < 23 || getParentActivity() == null || getParentActivity().checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
            return false;
        }
        getParentActivity().requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 3);
        return true;
    }

    private void initStickers() {
        if (this.chatActivityEnterView != null && getParentActivity() != null && this.stickersAdapter == null) {
            if (this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(this.currentEncryptedChat.layer) >= 23) {
                if (this.stickersAdapter != null) {
                    this.stickersAdapter.onDestroy();
                }
                this.stickersListView.setPadding(AndroidUtilities.dp(18.0f), 0, AndroidUtilities.dp(18.0f), 0);
                RecyclerListView recyclerListView = this.stickersListView;
                Adapter stickersAdapter = new StickersAdapter(getParentActivity(), new ChatActivity$$Lambda$27(this));
                this.stickersAdapter = stickersAdapter;
                recyclerListView.setAdapter(stickersAdapter);
                recyclerListView = this.stickersListView;
                OnItemClickListener chatActivity$$Lambda$28 = new ChatActivity$$Lambda$28(this);
                this.stickersOnItemClickListener = chatActivity$$Lambda$28;
                recyclerListView.setOnItemClickListener(chatActivity$$Lambda$28);
            }
        }
    }

    final /* synthetic */ void lambda$initStickers$35$ChatActivity(final boolean show) {
        float f = 1.0f;
        if (!show || this.stickersPanel.getVisibility() != 0) {
            if (show || this.stickersPanel.getVisibility() != 8) {
                if (show) {
                    this.stickersListView.scrollToPosition(0);
                    this.stickersPanel.setVisibility(this.allowStickersPanel ? 0 : 4);
                }
                if (this.runningAnimation != null) {
                    this.runningAnimation.cancel();
                    this.runningAnimation = null;
                }
                if (this.stickersPanel.getVisibility() != 4) {
                    float f2;
                    this.runningAnimation = new AnimatorSet();
                    AnimatorSet animatorSet = this.runningAnimation;
                    Animator[] animatorArr = new Animator[1];
                    FrameLayout frameLayout = this.stickersPanel;
                    String str = "alpha";
                    float[] fArr = new float[2];
                    if (show) {
                        f2 = 0.0f;
                    } else {
                        f2 = 1.0f;
                    }
                    fArr[0] = f2;
                    if (!show) {
                        f = 0.0f;
                    }
                    fArr[1] = f;
                    animatorArr[0] = ObjectAnimator.ofFloat(frameLayout, str, fArr);
                    animatorSet.playTogether(animatorArr);
                    this.runningAnimation.setDuration(150);
                    this.runningAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (ChatActivity.this.runningAnimation != null && ChatActivity.this.runningAnimation.equals(animation)) {
                                if (!show) {
                                    ChatActivity.this.stickersAdapter.clearStickers();
                                    ChatActivity.this.stickersPanel.setVisibility(8);
                                    if (StickerPreviewViewer.getInstance().isVisible()) {
                                        StickerPreviewViewer.getInstance().close();
                                    }
                                    StickerPreviewViewer.getInstance().reset();
                                }
                                ChatActivity.this.runningAnimation = null;
                            }
                        }

                        public void onAnimationCancel(Animator animation) {
                            if (ChatActivity.this.runningAnimation != null && ChatActivity.this.runningAnimation.equals(animation)) {
                                ChatActivity.this.runningAnimation = null;
                            }
                        }
                    });
                    this.runningAnimation.start();
                } else if (!show) {
                    this.stickersPanel.setVisibility(8);
                }
            }
        }
    }

    final /* synthetic */ void lambda$initStickers$36$ChatActivity(View view, int position) {
        Document document = this.stickersAdapter.getItem(position);
        if (document instanceof TL_document) {
            SendMessagesHelper.getInstance(this.currentAccount).sendSticker(document, this.dialog_id, this.replyingMessageObject);
            hideFieldPanel();
            this.chatActivityEnterView.addStickerToRecent(document);
        }
        this.chatActivityEnterView.setFieldText(TtmlNode.ANONYMOUS_REGION_ID);
    }

    public void shareMyContact(MessageObject messageObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("ShareYouPhoneNumberTitle", R.string.ShareYouPhoneNumberTitle));
        if (this.currentUser == null) {
            builder.setMessage(LocaleController.getString("AreYouSureShareMyContactInfo", R.string.AreYouSureShareMyContactInfo));
        } else if (this.currentUser.bot) {
            builder.setMessage(LocaleController.getString("AreYouSureShareMyContactInfoBot", R.string.AreYouSureShareMyContactInfoBot));
        } else {
            builder.setMessage(AndroidUtilities.replaceTags(LocaleController.formatString("AreYouSureShareMyContactInfoUser", R.string.AreYouSureShareMyContactInfoUser, PhoneFormat.getInstance().format("+" + UserConfig.getInstance(this.currentAccount).getCurrentUser().phone), ContactsController.formatName(this.currentUser.first_name, this.currentUser.last_name))));
        }
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$29(this, messageObject));
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    final /* synthetic */ void lambda$shareMyContact$37$ChatActivity(MessageObject messageObject, DialogInterface dialogInterface, int i) {
        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(UserConfig.getInstance(this.currentAccount).getCurrentUser(), this.dialog_id, messageObject, null, null);
        moveScrollToLastMessage();
        hideFieldPanel();
    }

    private void hideVoiceHint() {
        this.voiceHintAnimation = new AnimatorSet();
        AnimatorSet animatorSet = this.voiceHintAnimation;
        Animator[] animatorArr = new Animator[1];
        animatorArr[0] = ObjectAnimator.ofFloat(this.voiceHintTextView, "alpha", new float[]{0.0f});
        animatorSet.playTogether(animatorArr);
        this.voiceHintAnimation.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (animation.equals(ChatActivity.this.voiceHintAnimation)) {
                    ChatActivity.this.voiceHintAnimation = null;
                    ChatActivity.this.voiceHintHideRunnable = null;
                    if (ChatActivity.this.voiceHintTextView != null) {
                        ChatActivity.this.voiceHintTextView.setVisibility(8);
                    }
                }
            }

            public void onAnimationCancel(Animator animation) {
                if (animation.equals(ChatActivity.this.voiceHintAnimation)) {
                    ChatActivity.this.voiceHintHideRunnable = null;
                    ChatActivity.this.voiceHintHideRunnable = null;
                }
            }
        });
        this.voiceHintAnimation.setDuration(300);
        this.voiceHintAnimation.start();
    }

    private void showVoiceHint(boolean hide, boolean video) {
        if (getParentActivity() != null && this.fragmentView != null) {
            if (!hide || this.voiceHintTextView != null) {
                if (this.voiceHintTextView == null) {
                    SizeNotifierFrameLayout frameLayout = this.fragmentView;
                    int index = frameLayout.indexOfChild(this.chatActivityEnterView);
                    if (index != -1) {
                        this.voiceHintTextView = new TextView(getParentActivity());
                        this.voiceHintTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), Theme.getColor(Theme.key_chat_gifSaveHintBackground)));
                        this.voiceHintTextView.setTextColor(Theme.getColor(Theme.key_chat_gifSaveHintText));
                        this.voiceHintTextView.setTextSize(1, 14.0f);
                        this.voiceHintTextView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f));
                        this.voiceHintTextView.setGravity(16);
                        this.voiceHintTextView.setAlpha(0.0f);
                        frameLayout.addView(this.voiceHintTextView, index + 1, LayoutHelper.createFrame(-2, -2.0f, 85, 5.0f, 0.0f, 5.0f, 3.0f));
                    } else {
                        return;
                    }
                }
                if (hide) {
                    if (this.voiceHintAnimation != null) {
                        this.voiceHintAnimation.cancel();
                        this.voiceHintAnimation = null;
                    }
                    AndroidUtilities.cancelRunOnUIThread(this.voiceHintHideRunnable);
                    this.voiceHintHideRunnable = null;
                    hideVoiceHint();
                    return;
                }
                this.voiceHintTextView.setText(video ? LocaleController.getString("HoldToVideo", R.string.HoldToVideo) : LocaleController.getString("HoldToAudio", R.string.HoldToAudio));
                if (this.voiceHintHideRunnable != null) {
                    if (this.voiceHintAnimation != null) {
                        this.voiceHintAnimation.cancel();
                        this.voiceHintAnimation = null;
                    } else {
                        AndroidUtilities.cancelRunOnUIThread(this.voiceHintHideRunnable);
                        Runnable chatActivity$$Lambda$30 = new ChatActivity$$Lambda$30(this);
                        this.voiceHintHideRunnable = chatActivity$$Lambda$30;
                        AndroidUtilities.runOnUIThread(chatActivity$$Lambda$30, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                        return;
                    }
                } else if (this.voiceHintAnimation != null) {
                    return;
                }
                this.voiceHintTextView.setVisibility(0);
                this.voiceHintAnimation = new AnimatorSet();
                AnimatorSet animatorSet = this.voiceHintAnimation;
                Animator[] animatorArr = new Animator[1];
                animatorArr[0] = ObjectAnimator.ofFloat(this.voiceHintTextView, "alpha", new float[]{1.0f});
                animatorSet.playTogether(animatorArr);
                this.voiceHintAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (animation.equals(ChatActivity.this.voiceHintAnimation)) {
                            ChatActivity.this.voiceHintAnimation = null;
                            AndroidUtilities.runOnUIThread(ChatActivity.this.voiceHintHideRunnable = new ChatActivity$35$$Lambda$0(this), AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                        }
                    }

                    final /* synthetic */ void lambda$onAnimationEnd$0$ChatActivity$35() {
                        ChatActivity.this.hideVoiceHint();
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (animation.equals(ChatActivity.this.voiceHintAnimation)) {
                            ChatActivity.this.voiceHintAnimation = null;
                        }
                    }
                });
                this.voiceHintAnimation.setDuration(300);
                this.voiceHintAnimation.start();
            }
        }
    }

    private void showMediaBannedHint() {
        if (getParentActivity() != null && this.currentChat != null && this.currentChat.banned_rights != null && this.fragmentView != null) {
            if (this.mediaBanTooltip == null || this.mediaBanTooltip.getVisibility() != 0) {
                SizeNotifierFrameLayout frameLayout = this.fragmentView;
                int index = frameLayout.indexOfChild(this.chatActivityEnterView);
                if (index != -1) {
                    if (this.mediaBanTooltip == null) {
                        this.mediaBanTooltip = new CorrectlyMeasuringTextView(getParentActivity());
                        this.mediaBanTooltip.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), Theme.getColor(Theme.key_chat_gifSaveHintBackground)));
                        this.mediaBanTooltip.setTextColor(Theme.getColor(Theme.key_chat_gifSaveHintText));
                        this.mediaBanTooltip.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f));
                        this.mediaBanTooltip.setGravity(16);
                        this.mediaBanTooltip.setTextSize(1, 14.0f);
                        frameLayout.addView(this.mediaBanTooltip, index + 1, LayoutHelper.createFrame(-2, -2.0f, 85, 30.0f, 0.0f, 5.0f, 3.0f));
                    }
                    if (AndroidUtilities.isBannedForever(this.currentChat.banned_rights.until_date)) {
                        this.mediaBanTooltip.setText(LocaleController.getString("AttachMediaRestrictedForever", R.string.AttachMediaRestrictedForever));
                    } else {
                        this.mediaBanTooltip.setText(LocaleController.formatString("AttachMediaRestricted", R.string.AttachMediaRestricted, LocaleController.formatDateForBan((long) this.currentChat.banned_rights.until_date)));
                    }
                    this.mediaBanTooltip.setVisibility(0);
                    AnimatorSet AnimatorSet = new AnimatorSet();
                    AnimatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.mediaBanTooltip, "alpha", new float[]{0.0f, 1.0f})});
                    AnimatorSet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            AndroidUtilities.runOnUIThread(new ChatActivity$36$$Lambda$0(this), DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
                        }

                        final /* synthetic */ void lambda$onAnimationEnd$0$ChatActivity$36() {
                            if (ChatActivity.this.mediaBanTooltip != null) {
                                AnimatorSet AnimatorSet = new AnimatorSet();
                                Animator[] animatorArr = new Animator[1];
                                animatorArr[0] = ObjectAnimator.ofFloat(ChatActivity.this.mediaBanTooltip, "alpha", new float[]{0.0f});
                                AnimatorSet.playTogether(animatorArr);
                                AnimatorSet.addListener(new AnimatorListenerAdapter() {
                                    public void onAnimationEnd(Animator animation) {
                                        if (ChatActivity.this.mediaBanTooltip != null) {
                                            ChatActivity.this.mediaBanTooltip.setVisibility(8);
                                        }
                                    }
                                });
                                AnimatorSet.setDuration(300);
                                AnimatorSet.start();
                            }
                        }
                    });
                    AnimatorSet.setDuration(300);
                    AnimatorSet.start();
                }
            }
        }
    }

    private void showGifHint() {
        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
        if (!preferences.getBoolean("gifhint", false)) {
            preferences.edit().putBoolean("gifhint", true).commit();
            if (getParentActivity() != null && this.fragmentView != null && this.gifHintTextView == null) {
                if (this.allowContextBotPanelSecond) {
                    SizeNotifierFrameLayout frameLayout = this.fragmentView;
                    int index = frameLayout.indexOfChild(this.chatActivityEnterView);
                    if (index != -1) {
                        this.chatActivityEnterView.setOpenGifsTabFirst();
                        this.emojiButtonRed = new View(getParentActivity());
                        this.emojiButtonRed.setBackgroundResource(R.drawable.redcircle);
                        frameLayout.addView(this.emojiButtonRed, index + 1, LayoutHelper.createFrame(10, 10.0f, 83, 30.0f, 0.0f, 0.0f, 27.0f));
                        this.gifHintTextView = new TextView(getParentActivity());
                        this.gifHintTextView.setBackgroundDrawable(Theme.createRoundRectDrawable(AndroidUtilities.dp(3.0f), Theme.getColor(Theme.key_chat_gifSaveHintBackground)));
                        this.gifHintTextView.setTextColor(Theme.getColor(Theme.key_chat_gifSaveHintText));
                        this.gifHintTextView.setTextSize(1, 14.0f);
                        this.gifHintTextView.setPadding(AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f), AndroidUtilities.dp(8.0f), AndroidUtilities.dp(7.0f));
                        this.gifHintTextView.setText(LocaleController.getString("TapHereGifs", R.string.TapHereGifs));
                        this.gifHintTextView.setGravity(16);
                        frameLayout.addView(this.gifHintTextView, index + 1, LayoutHelper.createFrame(-2, -2.0f, 83, 5.0f, 0.0f, 5.0f, 3.0f));
                        AnimatorSet AnimatorSet = new AnimatorSet();
                        AnimatorSet.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.gifHintTextView, "alpha", new float[]{0.0f, 1.0f}), ObjectAnimator.ofFloat(this.emojiButtonRed, "alpha", new float[]{0.0f, 1.0f})});
                        AnimatorSet.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                AndroidUtilities.runOnUIThread(new ChatActivity$37$$Lambda$0(this), AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
                            }

                            final /* synthetic */ void lambda$onAnimationEnd$0$ChatActivity$37() {
                                if (ChatActivity.this.gifHintTextView != null) {
                                    AnimatorSet AnimatorSet = new AnimatorSet();
                                    Animator[] animatorArr = new Animator[1];
                                    animatorArr[0] = ObjectAnimator.ofFloat(ChatActivity.this.gifHintTextView, "alpha", new float[]{0.0f});
                                    AnimatorSet.playTogether(animatorArr);
                                    AnimatorSet.addListener(new AnimatorListenerAdapter() {
                                        public void onAnimationEnd(Animator animation) {
                                            if (ChatActivity.this.gifHintTextView != null) {
                                                ChatActivity.this.gifHintTextView.setVisibility(8);
                                            }
                                        }
                                    });
                                    AnimatorSet.setDuration(300);
                                    AnimatorSet.start();
                                }
                            }
                        });
                        AnimatorSet.setDuration(300);
                        AnimatorSet.start();
                    }
                } else if (this.chatActivityEnterView != null) {
                    this.chatActivityEnterView.setOpenGifsTabFirst();
                }
            }
        }
    }

    private void openAttachMenu() {
        if (getParentActivity() != null) {
            createChatAttachView();
            this.chatAttachAlert.loadGalleryPhotos();
            if (VERSION.SDK_INT == 21 || VERSION.SDK_INT == 22) {
                this.chatActivityEnterView.closeKeyboard();
            }
            this.chatAttachAlert.init();
            showDialog(this.chatAttachAlert);
        }
    }

    private void checkContextBotPanel() {
        if (!this.allowStickersPanel || this.mentionsAdapter == null || !this.mentionsAdapter.isBotContext()) {
            return;
        }
        if (this.allowContextBotPanel || this.allowContextBotPanelSecond) {
            if (this.mentionContainer.getVisibility() == 4 || this.mentionContainer.getTag() != null) {
                if (this.mentionListAnimation != null) {
                    this.mentionListAnimation.cancel();
                }
                this.mentionContainer.setTag(null);
                this.mentionContainer.setVisibility(0);
                this.mentionListAnimation = new AnimatorSet();
                this.mentionListAnimation.playTogether(new Animator[]{ObjectAnimator.ofFloat(this.mentionContainer, "alpha", new float[]{0.0f, 1.0f})});
                this.mentionListAnimation.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (ChatActivity.this.mentionListAnimation != null && ChatActivity.this.mentionListAnimation.equals(animation)) {
                            ChatActivity.this.mentionListAnimation = null;
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (ChatActivity.this.mentionListAnimation != null && ChatActivity.this.mentionListAnimation.equals(animation)) {
                            ChatActivity.this.mentionListAnimation = null;
                        }
                    }
                });
                this.mentionListAnimation.setDuration(200);
                this.mentionListAnimation.start();
            }
        } else if (this.mentionContainer.getVisibility() == 0 && this.mentionContainer.getTag() == null) {
            if (this.mentionListAnimation != null) {
                this.mentionListAnimation.cancel();
            }
            this.mentionContainer.setTag(Integer.valueOf(1));
            this.mentionListAnimation = new AnimatorSet();
            AnimatorSet animatorSet = this.mentionListAnimation;
            Animator[] animatorArr = new Animator[1];
            animatorArr[0] = ObjectAnimator.ofFloat(this.mentionContainer, "alpha", new float[]{0.0f});
            animatorSet.playTogether(animatorArr);
            this.mentionListAnimation.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (ChatActivity.this.mentionListAnimation != null && ChatActivity.this.mentionListAnimation.equals(animation)) {
                        ChatActivity.this.mentionContainer.setVisibility(4);
                        ChatActivity.this.mentionListAnimation = null;
                    }
                }

                public void onAnimationCancel(Animator animation) {
                    if (ChatActivity.this.mentionListAnimation != null && ChatActivity.this.mentionListAnimation.equals(animation)) {
                        ChatActivity.this.mentionListAnimation = null;
                    }
                }
            });
            this.mentionListAnimation.setDuration(200);
            this.mentionListAnimation.start();
        }
    }

    private void hideFloatingDateView(boolean animated) {
        if (this.floatingDateView.getTag() != null && !this.currentFloatingDateOnScreen) {
            if (!this.scrollingFloatingDate || this.currentFloatingTopIsNotMessage) {
                this.floatingDateView.setTag(null);
                if (animated) {
                    this.floatingDateAnimation = new AnimatorSet();
                    this.floatingDateAnimation.setDuration(150);
                    AnimatorSet animatorSet = this.floatingDateAnimation;
                    Animator[] animatorArr = new Animator[1];
                    animatorArr[0] = ObjectAnimator.ofFloat(this.floatingDateView, "alpha", new float[]{0.0f});
                    animatorSet.playTogether(animatorArr);
                    this.floatingDateAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (animation.equals(ChatActivity.this.floatingDateAnimation)) {
                                ChatActivity.this.floatingDateAnimation = null;
                            }
                        }
                    });
                    this.floatingDateAnimation.setStartDelay(500);
                    this.floatingDateAnimation.start();
                    return;
                }
                if (this.floatingDateAnimation != null) {
                    this.floatingDateAnimation.cancel();
                    this.floatingDateAnimation = null;
                }
                this.floatingDateView.setAlpha(0.0f);
            }
        }
    }

    protected void onRemoveFromParent() {
        MediaController.getInstance().setTextureView(this.videoTextureView, null, null, false);
    }

    protected void setIgnoreAttachOnPause(boolean value) {
        this.ignoreAttachOnPause = value;
    }

    private void checkScrollForLoad(boolean scroll) {
        if (this.chatLayoutManager != null && !this.paused) {
            int firstVisibleItem = this.chatLayoutManager.findFirstVisibleItemPosition();
            int visibleItemCount = firstVisibleItem == -1 ? 0 : Math.abs(this.chatLayoutManager.findLastVisibleItemPosition() - firstVisibleItem) + 1;
            if (visibleItemCount > 0 || this.currentEncryptedChat != null) {
                int checkLoadCount;
                MessagesController instance;
                long j;
                int i;
                int i2;
                int i3;
                boolean isChannel;
                int i4;
                int totalItemCount = this.chatAdapter.getItemCount();
                if (scroll) {
                    checkLoadCount = 25;
                } else {
                    checkLoadCount = 5;
                }
                if ((totalItemCount - firstVisibleItem) - visibleItemCount <= checkLoadCount && !this.loading) {
                    boolean z;
                    if (!this.endReached[0]) {
                        this.loading = true;
                        this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
                        if (this.messagesByDays.size() != 0) {
                            instance = MessagesController.getInstance(this.currentAccount);
                            j = this.dialog_id;
                            i = this.maxMessageId[0];
                            z = !this.cacheEndReached[0];
                            i2 = this.minDate[0];
                            i3 = this.classGuid;
                            isChannel = ChatObject.isChannel(this.currentChat);
                            i4 = this.lastLoadIndex;
                            this.lastLoadIndex = i4 + 1;
                            instance.loadMessages(j, 50, i, 0, z, i2, i3, 0, 0, isChannel, i4);
                        } else {
                            instance = MessagesController.getInstance(this.currentAccount);
                            j = this.dialog_id;
                            z = !this.cacheEndReached[0];
                            i2 = this.minDate[0];
                            i3 = this.classGuid;
                            isChannel = ChatObject.isChannel(this.currentChat);
                            i4 = this.lastLoadIndex;
                            this.lastLoadIndex = i4 + 1;
                            instance.loadMessages(j, 50, 0, 0, z, i2, i3, 0, 0, isChannel, i4);
                        }
                    } else if (!(this.mergeDialogId == 0 || this.endReached[1])) {
                        this.loading = true;
                        this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
                        instance = MessagesController.getInstance(this.currentAccount);
                        j = this.mergeDialogId;
                        i = this.maxMessageId[1];
                        z = !this.cacheEndReached[1];
                        i2 = this.minDate[1];
                        i3 = this.classGuid;
                        isChannel = ChatObject.isChannel(this.currentChat);
                        i4 = this.lastLoadIndex;
                        this.lastLoadIndex = i4 + 1;
                        instance.loadMessages(j, 50, i, 0, z, i2, i3, 0, 0, isChannel, i4);
                    }
                }
                if (visibleItemCount > 0 && !this.loadingForward && firstVisibleItem <= 10) {
                    if (this.mergeDialogId != 0 && !this.forwardEndReached[1]) {
                        this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
                        instance = MessagesController.getInstance(this.currentAccount);
                        j = this.mergeDialogId;
                        i = this.minMessageId[1];
                        i2 = this.maxDate[1];
                        i3 = this.classGuid;
                        isChannel = ChatObject.isChannel(this.currentChat);
                        i4 = this.lastLoadIndex;
                        this.lastLoadIndex = i4 + 1;
                        instance.loadMessages(j, 50, i, 0, true, i2, i3, 1, 0, isChannel, i4);
                        this.loadingForward = true;
                    } else if (!this.forwardEndReached[0]) {
                        this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
                        instance = MessagesController.getInstance(this.currentAccount);
                        j = this.dialog_id;
                        i = this.minMessageId[0];
                        i2 = this.maxDate[0];
                        i3 = this.classGuid;
                        isChannel = ChatObject.isChannel(this.currentChat);
                        i4 = this.lastLoadIndex;
                        this.lastLoadIndex = i4 + 1;
                        instance.loadMessages(j, 50, i, 0, true, i2, i3, 1, 0, isChannel, i4);
                        this.loadingForward = true;
                    }
                }
            }
        }
    }

    private void processSelectedAttach(int which) {
        int i = 1;
        if (which == 0) {
            if (VERSION.SDK_INT < 23 || getParentActivity().checkSelfPermission("android.permission.CAMERA") == 0) {
                try {
                    Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File image = AndroidUtilities.generatePicturePath();
                    if (image != null) {
                        if (VERSION.SDK_INT >= 24) {
                            takePictureIntent.putExtra("output", FileProvider.getUriForFile(getParentActivity(), "org.telegram.messenger.beta.provider", image));
                            takePictureIntent.addFlags(2);
                            takePictureIntent.addFlags(1);
                        } else {
                            takePictureIntent.putExtra("output", Uri.fromFile(image));
                        }
                        this.currentPicturePath = image.getAbsolutePath();
                    }
                    startActivityForResult(takePictureIntent, 0);
                    return;
                } catch (Throwable e) {
                    FileLog.e(e);
                    return;
                }
            }
            getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 19);
        } else if (which == 1) {
            if (VERSION.SDK_INT < 23 || getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
                boolean allowGifs;
                if (ChatObject.isChannel(this.currentChat) && this.currentChat.banned_rights != null && this.currentChat.banned_rights.send_gifs) {
                    allowGifs = false;
                } else {
                    allowGifs = this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(this.currentEncryptedChat.layer) >= 46;
                }
                PhotoAlbumPickerActivity fragment = new PhotoAlbumPickerActivity(false, allowGifs, true, this);
                if (this.editingMessageObject == null) {
                    i = 100;
                }
                fragment.setMaxSelectedPhotos(i);
                fragment.setDelegate(new PhotoAlbumPickerActivityDelegate() {
                    public void didSelectPhotos(ArrayList<SendingMediaInfo> photos) {
                        if (!photos.isEmpty()) {
                            ChatActivity.this.fillEditingMediaWithCaption(((SendingMediaInfo) photos.get(0)).caption, ((SendingMediaInfo) photos.get(0)).entities);
                            SendMessagesHelper.prepareSendingMedia(photos, ChatActivity.this.dialog_id, ChatActivity.this.replyingMessageObject, null, false, SharedConfig.groupPhotosEnabled, ChatActivity.this.editingMessageObject);
                            ChatActivity.this.hideFieldPanel();
                            DataQuery.getInstance(ChatActivity.this.currentAccount).cleanDraft(ChatActivity.this.dialog_id, true);
                        }
                    }

                    public void startPhotoSelectActivity() {
                        try {
                            Intent videoPickerIntent = new Intent();
                            videoPickerIntent.setType("video/*");
                            videoPickerIntent.setAction("android.intent.action.GET_CONTENT");
                            videoPickerIntent.putExtra("android.intent.extra.sizeLimit", 1610612736);
                            Intent photoPickerIntent = new Intent("android.intent.action.PICK");
                            photoPickerIntent.setType("image/*");
                            Intent chooserIntent = Intent.createChooser(photoPickerIntent, null);
                            chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", new Intent[]{videoPickerIntent});
                            ChatActivity.this.startActivityForResult(chooserIntent, 1);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
                presentFragment(fragment);
                return;
            }
            getParentActivity().requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
        } else if (which == 2) {
            if (VERSION.SDK_INT < 23 || getParentActivity().checkSelfPermission("android.permission.CAMERA") == 0) {
                try {
                    Intent takeVideoIntent = new Intent("android.media.action.VIDEO_CAPTURE");
                    File video = AndroidUtilities.generateVideoPath();
                    if (video != null) {
                        if (VERSION.SDK_INT >= 24) {
                            takeVideoIntent.putExtra("output", FileProvider.getUriForFile(getParentActivity(), "org.telegram.messenger.beta.provider", video));
                            takeVideoIntent.addFlags(2);
                            takeVideoIntent.addFlags(1);
                        } else if (VERSION.SDK_INT >= 18) {
                            takeVideoIntent.putExtra("output", Uri.fromFile(video));
                        }
                        takeVideoIntent.putExtra("android.intent.extra.sizeLimit", 1610612736);
                        this.currentPicturePath = video.getAbsolutePath();
                    }
                    startActivityForResult(takeVideoIntent, 2);
                    return;
                } catch (Throwable e2) {
                    FileLog.e(e2);
                    return;
                }
            }
            getParentActivity().requestPermissions(new String[]{"android.permission.CAMERA"}, 20);
        } else if (which == 6) {
            if (AndroidUtilities.isGoogleMapsInstalled(this)) {
                if (this.currentEncryptedChat != null) {
                    i = 0;
                }
                LocationActivity fragment2 = new LocationActivity(i);
                fragment2.setDialogId(this.dialog_id);
                fragment2.setDelegate(this);
                presentFragment(fragment2);
            }
        } else if (which == 4) {
            if (VERSION.SDK_INT < 23 || getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
                DocumentSelectActivity fragment3 = new DocumentSelectActivity();
                if (this.editingMessageObject == null) {
                    i = -1;
                }
                fragment3.setMaxSelectedFiles(i);
                fragment3.setDelegate(new DocumentSelectActivityDelegate() {
                    public void didSelectFiles(DocumentSelectActivity activity, ArrayList<String> files) {
                        activity.finishFragment();
                        ChatActivity.this.fillEditingMediaWithCaption(null, null);
                        SendMessagesHelper.prepareSendingDocuments(files, files, null, null, ChatActivity.this.dialog_id, ChatActivity.this.replyingMessageObject, null, ChatActivity.this.editingMessageObject);
                        ChatActivity.this.hideFieldPanel();
                        DataQuery.getInstance(ChatActivity.this.currentAccount).cleanDraft(ChatActivity.this.dialog_id, true);
                    }

                    public void startDocumentSelectActivity() {
                        try {
                            Intent photoPickerIntent = new Intent("android.intent.action.GET_CONTENT");
                            if (VERSION.SDK_INT >= 18) {
                                photoPickerIntent.putExtra("android.intent.extra.ALLOW_MULTIPLE", true);
                            }
                            photoPickerIntent.setType("*/*");
                            ChatActivity.this.startActivityForResult(photoPickerIntent, 21);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                    }
                });
                presentFragment(fragment3);
                return;
            }
            getParentActivity().requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
        } else if (which == 3) {
            if (VERSION.SDK_INT < 23 || getParentActivity().checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == 0) {
                AudioSelectActivity fragment4 = new AudioSelectActivity();
                fragment4.setDelegate(new ChatActivity$$Lambda$31(this));
                presentFragment(fragment4);
                return;
            }
            getParentActivity().requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 4);
        } else if (which != 5) {
        } else {
            if (VERSION.SDK_INT < 23 || getParentActivity().checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
                PhonebookSelectActivity activity = new PhonebookSelectActivity();
                activity.setDelegate(new ChatActivity$$Lambda$32(this));
                presentFragment(activity);
                return;
            }
            getParentActivity().requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 5);
        }
    }

    final /* synthetic */ void lambda$processSelectedAttach$38$ChatActivity(ArrayList audios) {
        fillEditingMediaWithCaption(null, null);
        SendMessagesHelper.prepareSendingAudioDocuments(audios, this.dialog_id, this.replyingMessageObject, this.editingMessageObject);
        hideFieldPanel();
        DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, true);
    }

    final /* synthetic */ void lambda$processSelectedAttach$39$ChatActivity(User user) {
        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(user, this.dialog_id, this.replyingMessageObject, null, null);
        hideFieldPanel();
        DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, true);
    }

    public boolean dismissDialogOnPause(Dialog dialog) {
        return dialog != this.chatAttachAlert && super.dismissDialogOnPause(dialog);
    }

    private void searchLinks(CharSequence charSequence, boolean force) {
        if (this.currentEncryptedChat == null || (MessagesController.getInstance(this.currentAccount).secretWebpagePreview != 0 && AndroidUtilities.getPeerLayerVersion(this.currentEncryptedChat.layer) >= 46)) {
            if (force && this.foundWebPage != null) {
                if (this.foundWebPage.url != null) {
                    int index = TextUtils.indexOf(charSequence, this.foundWebPage.url);
                    char lastChar = '\u0000';
                    boolean lenEqual = false;
                    if (index != -1) {
                        if (this.foundWebPage.url.length() + index == charSequence.length()) {
                            lenEqual = true;
                        } else {
                            lenEqual = false;
                        }
                        if (lenEqual) {
                            lastChar = '\u0000';
                        } else {
                            lastChar = charSequence.charAt(this.foundWebPage.url.length() + index);
                        }
                    } else if (this.foundWebPage.display_url != null) {
                        index = TextUtils.indexOf(charSequence, this.foundWebPage.display_url);
                        if (index == -1 || this.foundWebPage.display_url.length() + index != charSequence.length()) {
                            lenEqual = false;
                        } else {
                            lenEqual = true;
                        }
                        if (index == -1 || lenEqual) {
                            lastChar = '\u0000';
                        } else {
                            lastChar = charSequence.charAt(this.foundWebPage.display_url.length() + index);
                        }
                    }
                    if (index != -1 && (lenEqual || lastChar == ' ' || lastChar == ',' || lastChar == '.' || lastChar == '!' || lastChar == '/')) {
                        return;
                    }
                }
                this.pendingLinkSearchString = null;
                this.foundUrls = null;
                showFieldPanelForWebPage(false, this.foundWebPage, false);
            }
            Utilities.searchQueue.postRunnable(new ChatActivity$$Lambda$33(this, charSequence, MessagesController.getInstance(this.currentAccount), force));
        }
    }

    final /* synthetic */ void lambda$searchLinks$46$ChatActivity(CharSequence charSequence, MessagesController messagesController, boolean force) {
        ArrayList<CharSequence> urls;
        Throwable e;
        int a;
        boolean clear;
        CharSequence textToCheck;
        TL_messages_getWebPagePreview req;
        if (this.linkSearchRequestId != 0) {
            ConnectionsManager.getInstance(this.currentAccount).cancelRequest(this.linkSearchRequestId, true);
            this.linkSearchRequestId = 0;
        }
        Matcher m = AndroidUtilities.WEB_URL.matcher(charSequence);
        ArrayList<CharSequence> urls2 = null;
        while (m.find()) {
            try {
                if (m.start() > 0) {
                    if (charSequence.charAt(m.start() - 1) == '@') {
                        continue;
                    }
                }
                if (urls2 == null) {
                    urls = new ArrayList();
                } else {
                    urls = urls2;
                }
                try {
                    urls.add(charSequence.subSequence(m.start(), m.end()));
                    urls2 = urls;
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Exception e3) {
                e = e3;
                urls = urls2;
            }
        }
        if (charSequence instanceof Spannable) {
            URLSpanReplacement[] spans = (URLSpanReplacement[]) ((Spannable) charSequence).getSpans(0, charSequence.length(), URLSpanReplacement.class);
            if (spans != null && spans.length > 0) {
                if (urls2 == null) {
                    urls = new ArrayList();
                } else {
                    urls = urls2;
                }
                for (URLSpanReplacement url : spans) {
                    urls.add(url.getURL());
                }
                if (!(urls == null || this.foundUrls == null || urls.size() != this.foundUrls.size())) {
                    clear = true;
                    for (a = 0; a < urls.size(); a++) {
                        if (!TextUtils.equals((CharSequence) urls.get(a), (CharSequence) this.foundUrls.get(a))) {
                            clear = false;
                        }
                    }
                    if (clear) {
                        return;
                    }
                }
                this.foundUrls = urls;
                if (urls != null) {
                    AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$71(this));
                    return;
                }
                textToCheck = TextUtils.join(" ", urls);
                if (this.currentEncryptedChat == null && messagesController.secretWebpagePreview == 2) {
                    AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$73(this, messagesController, charSequence, force));
                    return;
                }
                req = new TL_messages_getWebPagePreview();
                if (textToCheck instanceof String) {
                    req.message = (String) textToCheck;
                } else {
                    req.message = textToCheck.toString();
                }
                this.linkSearchRequestId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new ChatActivity$$Lambda$74(this, req));
                ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(this.linkSearchRequestId, this.classGuid);
            }
        }
        urls = urls2;
        clear = true;
        for (a = 0; a < urls.size(); a++) {
            if (!TextUtils.equals((CharSequence) urls.get(a), (CharSequence) this.foundUrls.get(a))) {
                clear = false;
            }
        }
        if (clear) {
            return;
        }
        this.foundUrls = urls;
        if (urls != null) {
            textToCheck = TextUtils.join(" ", urls);
            if (this.currentEncryptedChat == null) {
            }
            req = new TL_messages_getWebPagePreview();
            if (textToCheck instanceof String) {
                req.message = textToCheck.toString();
            } else {
                req.message = (String) textToCheck;
            }
            this.linkSearchRequestId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new ChatActivity$$Lambda$74(this, req));
            ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(this.linkSearchRequestId, this.classGuid);
        }
        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$71(this));
        return;
        FileLog.e(e);
        String text = charSequence.toString().toLowerCase();
        if (charSequence.length() < 13 || !(text.contains("http://") || text.contains("https://"))) {
            AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$72(this));
            return;
        }
        textToCheck = charSequence;
        if (this.currentEncryptedChat == null) {
        }
        req = new TL_messages_getWebPagePreview();
        if (textToCheck instanceof String) {
            req.message = (String) textToCheck;
        } else {
            req.message = textToCheck.toString();
        }
        this.linkSearchRequestId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new ChatActivity$$Lambda$74(this, req));
        ConnectionsManager.getInstance(this.currentAccount).bindRequestToGuid(this.linkSearchRequestId, this.classGuid);
    }

    final /* synthetic */ void lambda$null$40$ChatActivity() {
        if (this.foundWebPage != null) {
            showFieldPanelForWebPage(false, this.foundWebPage, false);
            this.foundWebPage = null;
        }
    }

    final /* synthetic */ void lambda$null$41$ChatActivity() {
        if (this.foundWebPage != null) {
            showFieldPanelForWebPage(false, this.foundWebPage, false);
            this.foundWebPage = null;
        }
    }

    final /* synthetic */ void lambda$null$43$ChatActivity(MessagesController messagesController, CharSequence charSequence, boolean force) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$76(this, messagesController, charSequence, force));
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        builder.setMessage(LocaleController.getString("SecretLinkPreviewAlert", R.string.SecretLinkPreviewAlert));
        showDialog(builder.create());
        messagesController.secretWebpagePreview = 0;
        MessagesController.getGlobalMainSettings().edit().putInt("secretWebpage2", messagesController.secretWebpagePreview).commit();
    }

    final /* synthetic */ void lambda$null$42$ChatActivity(MessagesController messagesController, CharSequence charSequence, boolean force, DialogInterface dialog, int which) {
        messagesController.secretWebpagePreview = 1;
        MessagesController.getGlobalMainSettings().edit().putInt("secretWebpage2", MessagesController.getInstance(this.currentAccount).secretWebpagePreview).commit();
        this.foundUrls = null;
        searchLinks(charSequence, force);
    }

    final /* synthetic */ void lambda$null$45$ChatActivity(TL_messages_getWebPagePreview req, TLObject response, TL_error error) {
        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$75(this, error, response, req));
    }

    final /* synthetic */ void lambda$null$44$ChatActivity(TL_error error, TLObject response, TL_messages_getWebPagePreview req) {
        this.linkSearchRequestId = 0;
        if (error != null) {
            return;
        }
        if (response instanceof TL_messageMediaWebPage) {
            this.foundWebPage = ((TL_messageMediaWebPage) response).webpage;
            if ((this.foundWebPage instanceof TL_webPage) || (this.foundWebPage instanceof TL_webPagePending)) {
                if (this.foundWebPage instanceof TL_webPagePending) {
                    this.pendingLinkSearchString = req.message;
                }
                if (this.currentEncryptedChat != null && (this.foundWebPage instanceof TL_webPagePending)) {
                    this.foundWebPage.url = req.message;
                }
                showFieldPanelForWebPage(true, this.foundWebPage, false);
            } else if (this.foundWebPage != null) {
                showFieldPanelForWebPage(false, this.foundWebPage, false);
                this.foundWebPage = null;
            }
        } else if (this.foundWebPage != null) {
            showFieldPanelForWebPage(false, this.foundWebPage, false);
            this.foundWebPage = null;
        }
    }

    private void forwardMessages(ArrayList<MessageObject> arrayList, boolean fromMyName) {
        if (arrayList != null && !arrayList.isEmpty()) {
            if (fromMyName) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    SendMessagesHelper.getInstance(this.currentAccount).processForwardFromMyName((MessageObject) it.next(), this.dialog_id);
                }
                return;
            }
            AlertsCreator.showSendMediaAlert(SendMessagesHelper.getInstance(this.currentAccount).sendMessage(arrayList, this.dialog_id), this);
        }
    }

    private void checkBotKeyboard() {
        if (this.chatActivityEnterView != null && this.botButtons != null && !this.userBlocked) {
            if (!(this.botButtons.messageOwner.reply_markup instanceof TL_replyKeyboardForceReply)) {
                if (this.replyingMessageObject != null && this.botReplyButtons == this.replyingMessageObject) {
                    this.botReplyButtons = null;
                    hideFieldPanel();
                }
                this.chatActivityEnterView.setButtons(this.botButtons);
            } else if (MessagesController.getMainSettings(this.currentAccount).getInt("answered_" + this.dialog_id, 0) == this.botButtons.getId()) {
            } else {
                if (this.replyingMessageObject == null || this.chatActivityEnterView.getFieldText() == null) {
                    this.botReplyButtons = this.botButtons;
                    this.chatActivityEnterView.setButtons(this.botButtons);
                    showFieldPanelForReply(true, this.botButtons);
                }
            }
        }
    }

    public void hideFieldPanel() {
        showFieldPanel(false, null, null, null, null, false);
    }

    public void showFieldPanelForWebPage(boolean show, WebPage webPage, boolean cancel) {
        showFieldPanel(show, null, null, null, webPage, cancel);
    }

    public void showFieldPanelForForward(boolean show, ArrayList<MessageObject> messageObjectsToForward) {
        showFieldPanel(show, null, null, messageObjectsToForward, null, false);
    }

    public void showFieldPanelForReply(boolean show, MessageObject messageObjectToReply) {
        showFieldPanel(show, messageObjectToReply, null, null, null, false);
    }

    public void showFieldPanelForEdit(boolean show, MessageObject messageObjectToEdit) {
        showFieldPanel(show, null, messageObjectToEdit, null, null, false);
    }

    public void showFieldPanel(boolean show, MessageObject messageObjectToReply, MessageObject messageObjectToEdit, ArrayList<MessageObject> messageObjectsToForward, WebPage webPage, boolean cancel) {
        if (this.chatActivityEnterView != null) {
            if (show) {
                if (messageObjectToReply != null || messageObjectsToForward != null || messageObjectToEdit != null || webPage != null) {
                    MessageObject thumbMediaMessageObject;
                    if (this.searchItem != null && this.actionBar.isSearchFieldVisible()) {
                        this.actionBar.closeSearchField(false);
                        this.chatActivityEnterView.setFieldFocused();
                        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$34(this), 100);
                    }
                    boolean openKeyboard = false;
                    if (!(messageObjectToReply == null || messageObjectToReply.getDialogId() == this.dialog_id)) {
                        messageObjectsToForward = new ArrayList();
                        messageObjectsToForward.add(messageObjectToReply);
                        messageObjectToReply = null;
                        openKeyboard = true;
                    }
                    String mess;
                    if (messageObjectToEdit != null) {
                        this.forwardingMessages = null;
                        this.replyingMessageObject = null;
                        this.editingMessageObject = messageObjectToEdit;
                        this.chatActivityEnterView.setReplyingMessageObject(null);
                        this.chatActivityEnterView.setEditingMessageObject(messageObjectToEdit, !messageObjectToEdit.isMediaEmpty());
                        if (this.foundWebPage == null) {
                            this.chatActivityEnterView.setForceShowSendButton(false, false);
                            this.replyIconImageView.setImageResource(R.drawable.group_edit);
                            if (messageObjectToEdit.isMediaEmpty()) {
                                this.replyNameTextView.setText(LocaleController.getString("EditMessage", R.string.EditMessage));
                            } else {
                                this.replyNameTextView.setText(LocaleController.getString("EditCaption", R.string.EditCaption));
                            }
                            if (messageObjectToEdit.canEditMedia()) {
                                this.replyObjectTextView.setText(LocaleController.getString("EditMessageMedia", R.string.EditMessageMedia));
                            } else if (messageObjectToEdit.messageText != null) {
                                mess = messageObjectToEdit.messageText.toString();
                                if (mess.length() > 150) {
                                    mess = mess.substring(0, 150);
                                }
                                this.replyObjectTextView.setText(Emoji.replaceEmoji(mess.replace('\n', ' '), this.replyObjectTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                            }
                        } else {
                            return;
                        }
                    } else if (messageObjectToReply != null) {
                        this.forwardingMessages = null;
                        this.editingMessageObject = null;
                        this.replyingMessageObject = messageObjectToReply;
                        this.chatActivityEnterView.setReplyingMessageObject(messageObjectToReply);
                        this.chatActivityEnterView.setEditingMessageObject(null, false);
                        if (this.foundWebPage == null) {
                            this.chatActivityEnterView.setForceShowSendButton(false, false);
                            if (messageObjectToReply.isFromUser()) {
                                user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObjectToReply.messageOwner.from_id));
                                if (user != null) {
                                    name = UserObject.getUserName(user);
                                } else {
                                    return;
                                }
                            }
                            chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(messageObjectToReply.messageOwner.to_id.channel_id));
                            if (chat != null) {
                                name = chat.title;
                            } else {
                                return;
                            }
                            this.replyIconImageView.setImageResource(R.drawable.msg_panel_reply);
                            this.replyNameTextView.setText(name);
                            if (messageObjectToReply.messageOwner.media instanceof TL_messageMediaGame) {
                                this.replyObjectTextView.setText(Emoji.replaceEmoji(messageObjectToReply.messageOwner.media.game.title, this.replyObjectTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                            } else if (messageObjectToReply.messageText != null) {
                                mess = messageObjectToReply.messageText.toString();
                                if (mess.length() > 150) {
                                    mess = mess.substring(0, 150);
                                }
                                this.replyObjectTextView.setText(Emoji.replaceEmoji(mess.replace('\n', ' '), this.replyObjectTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                            }
                        } else {
                            return;
                        }
                    } else if (messageObjectsToForward == null) {
                        this.replyIconImageView.setImageResource(R.drawable.msg_panel_link);
                        if (webPage instanceof TL_webPagePending) {
                            this.replyNameTextView.setText(LocaleController.getString("GettingLinkInfo", R.string.GettingLinkInfo));
                            this.replyObjectTextView.setText(this.pendingLinkSearchString);
                        } else {
                            if (webPage.site_name != null) {
                                this.replyNameTextView.setText(webPage.site_name);
                            } else if (webPage.title != null) {
                                this.replyNameTextView.setText(webPage.title);
                            } else {
                                this.replyNameTextView.setText(LocaleController.getString("LinkPreview", R.string.LinkPreview));
                            }
                            if (webPage.title != null) {
                                this.replyObjectTextView.setText(webPage.title);
                            } else if (webPage.description != null) {
                                this.replyObjectTextView.setText(webPage.description);
                            } else if (webPage.author != null) {
                                this.replyObjectTextView.setText(webPage.author);
                            } else {
                                this.replyObjectTextView.setText(webPage.display_url);
                            }
                            this.chatActivityEnterView.setWebPage(webPage, true);
                        }
                    } else if (!messageObjectsToForward.isEmpty()) {
                        this.replyingMessageObject = null;
                        this.editingMessageObject = null;
                        this.chatActivityEnterView.setReplyingMessageObject(null);
                        this.chatActivityEnterView.setEditingMessageObject(null, false);
                        this.forwardingMessages = messageObjectsToForward;
                        if (this.foundWebPage == null) {
                            int a;
                            Integer uid;
                            this.chatActivityEnterView.setForceShowSendButton(true, false);
                            ArrayList<Integer> uids = new ArrayList();
                            this.replyIconImageView.setImageResource(R.drawable.msg_panel_forward);
                            MessageObject object = (MessageObject) messageObjectsToForward.get(0);
                            if (object.isFromUser()) {
                                uids.add(Integer.valueOf(object.messageOwner.from_id));
                            } else {
                                uids.add(Integer.valueOf(-object.messageOwner.to_id.channel_id));
                            }
                            int type = ((MessageObject) messageObjectsToForward.get(0)).type;
                            for (a = 1; a < messageObjectsToForward.size(); a++) {
                                object = (MessageObject) messageObjectsToForward.get(a);
                                if (object.isFromUser()) {
                                    uid = Integer.valueOf(object.messageOwner.from_id);
                                } else {
                                    uid = Integer.valueOf(-object.messageOwner.to_id.channel_id);
                                }
                                if (!uids.contains(uid)) {
                                    uids.add(uid);
                                }
                                if (((MessageObject) messageObjectsToForward.get(a)).type != type) {
                                    type = -1;
                                }
                            }
                            StringBuilder userNames = new StringBuilder();
                            for (a = 0; a < uids.size(); a++) {
                                uid = (Integer) uids.get(a);
                                chat = null;
                                user = null;
                                if (uid.intValue() > 0) {
                                    user = MessagesController.getInstance(this.currentAccount).getUser(uid);
                                } else {
                                    chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-uid.intValue()));
                                }
                                if (user != null || chat != null) {
                                    if (uids.size() != 1) {
                                        if (uids.size() != 2 && userNames.length() != 0) {
                                            userNames.append(" ");
                                            userNames.append(LocaleController.formatPluralString("AndOther", uids.size() - 1));
                                            break;
                                        }
                                        if (userNames.length() > 0) {
                                            userNames.append(", ");
                                        }
                                        if (user == null) {
                                            userNames.append(chat.title);
                                        } else if (!TextUtils.isEmpty(user.first_name)) {
                                            userNames.append(user.first_name);
                                        } else if (TextUtils.isEmpty(user.last_name)) {
                                            userNames.append(" ");
                                        } else {
                                            userNames.append(user.last_name);
                                        }
                                    } else if (user != null) {
                                        userNames.append(UserObject.getUserName(user));
                                    } else {
                                        userNames.append(chat.title);
                                    }
                                }
                            }
                            this.replyNameTextView.setText(userNames);
                            if (type == -1 || type == 0 || type == 10 || type == 11) {
                                if (messageObjectsToForward.size() != 1 || ((MessageObject) messageObjectsToForward.get(0)).messageText == null) {
                                    this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedMessageCount", messageObjectsToForward.size()));
                                } else {
                                    MessageObject messageObject = (MessageObject) messageObjectsToForward.get(0);
                                    if (messageObject.messageOwner.media instanceof TL_messageMediaGame) {
                                        this.replyObjectTextView.setText(Emoji.replaceEmoji(messageObject.messageOwner.media.game.title, this.replyObjectTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                                    } else {
                                        mess = messageObject.messageText.toString();
                                        if (mess.length() > 150) {
                                            mess = mess.substring(0, 150);
                                        }
                                        this.replyObjectTextView.setText(Emoji.replaceEmoji(mess.replace('\n', ' '), this.replyObjectTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                                    }
                                }
                            } else if (type == 1) {
                                this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedPhoto", messageObjectsToForward.size()));
                                if (messageObjectsToForward.size() == 1) {
                                    messageObjectToReply = (MessageObject) messageObjectsToForward.get(0);
                                }
                            } else if (type == 4) {
                                this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedLocation", messageObjectsToForward.size()));
                            } else if (type == 3) {
                                this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedVideo", messageObjectsToForward.size()));
                                if (messageObjectsToForward.size() == 1) {
                                    messageObjectToReply = (MessageObject) messageObjectsToForward.get(0);
                                }
                            } else if (type == 12) {
                                this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedContact", messageObjectsToForward.size()));
                            } else if (type == 2) {
                                this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedAudio", messageObjectsToForward.size()));
                            } else if (type == 5) {
                                this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedRound", messageObjectsToForward.size()));
                            } else if (type == 14) {
                                this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedMusic", messageObjectsToForward.size()));
                            } else if (type == 13) {
                                this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedSticker", messageObjectsToForward.size()));
                            } else if (type == 8 || type == 9) {
                                if (messageObjectsToForward.size() != 1) {
                                    this.replyObjectTextView.setText(LocaleController.formatPluralString("ForwardedFile", messageObjectsToForward.size()));
                                } else if (type == 8) {
                                    this.replyObjectTextView.setText(LocaleController.getString("AttachGif", R.string.AttachGif));
                                } else {
                                    name = FileLoader.getDocumentFileName(((MessageObject) messageObjectsToForward.get(0)).getDocument());
                                    if (name.length() != 0) {
                                        this.replyObjectTextView.setText(name);
                                    }
                                    messageObjectToReply = (MessageObject) messageObjectsToForward.get(0);
                                }
                            }
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    if (messageObjectToReply != null) {
                        thumbMediaMessageObject = messageObjectToReply;
                    } else if (messageObjectToEdit != null) {
                        thumbMediaMessageObject = messageObjectToEdit;
                    } else {
                        thumbMediaMessageObject = null;
                    }
                    FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) this.replyNameTextView.getLayoutParams();
                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.replyObjectTextView.getLayoutParams();
                    PhotoSize photoSize = null;
                    if (thumbMediaMessageObject != null) {
                        photoSize = FileLoader.getClosestPhotoSizeWithSize(thumbMediaMessageObject.photoThumbs2, 80);
                        if (photoSize == null) {
                            photoSize = FileLoader.getClosestPhotoSizeWithSize(thumbMediaMessageObject.photoThumbs, 80);
                        }
                    }
                    int dp;
                    if (photoSize == null || (photoSize instanceof TL_photoSizeEmpty) || (photoSize.location instanceof TL_fileLocationUnavailable) || thumbMediaMessageObject.type == 13 || (thumbMediaMessageObject != null && thumbMediaMessageObject.isSecretMedia())) {
                        this.replyImageView.setImageBitmap(null);
                        this.replyImageLocation = null;
                        this.replyImageView.setVisibility(4);
                        dp = AndroidUtilities.dp(52.0f);
                        layoutParams2.leftMargin = dp;
                        layoutParams1.leftMargin = dp;
                    } else {
                        if (thumbMediaMessageObject == null || !thumbMediaMessageObject.isRoundVideo()) {
                            this.replyImageView.setRoundRadius(0);
                        } else {
                            this.replyImageView.setRoundRadius(AndroidUtilities.dp(17.0f));
                        }
                        this.replyImageLocation = photoSize.location;
                        this.replyImageView.setImage(this.replyImageLocation, "50_50", (Drawable) null);
                        this.replyImageView.setVisibility(0);
                        dp = AndroidUtilities.dp(96.0f);
                        layoutParams2.leftMargin = dp;
                        layoutParams1.leftMargin = dp;
                    }
                    this.replyNameTextView.setLayoutParams(layoutParams1);
                    this.replyObjectTextView.setLayoutParams(layoutParams2);
                    this.chatActivityEnterView.showTopView(false, openKeyboard);
                }
            } else if (this.replyingMessageObject != null || this.forwardingMessages != null || this.foundWebPage != null || this.editingMessageObject != null) {
                if (this.replyingMessageObject != null && (this.replyingMessageObject.messageOwner.reply_markup instanceof TL_replyKeyboardForceReply)) {
                    MessagesController.getMainSettings(this.currentAccount).edit().putInt("answered_" + this.dialog_id, this.replyingMessageObject.getId()).commit();
                }
                if (this.foundWebPage != null) {
                    this.foundWebPage = null;
                    this.chatActivityEnterView.setWebPage(null, !cancel);
                    if (!(webPage == null || (this.replyingMessageObject == null && this.forwardingMessages == null && this.editingMessageObject == null))) {
                        showFieldPanel(true, this.replyingMessageObject, this.editingMessageObject, this.forwardingMessages, null, false);
                        return;
                    }
                }
                if (this.forwardingMessages != null) {
                    forwardMessages(this.forwardingMessages, false);
                }
                this.chatActivityEnterView.setForceShowSendButton(false, false);
                this.chatActivityEnterView.hideTopView(false);
                this.chatActivityEnterView.setReplyingMessageObject(null);
                this.chatActivityEnterView.setEditingMessageObject(null, false);
                this.replyingMessageObject = null;
                this.editingMessageObject = null;
                this.forwardingMessages = null;
                this.replyImageLocation = null;
            }
        }
    }

    final /* synthetic */ void lambda$showFieldPanel$47$ChatActivity() {
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.openKeyboard();
        }
    }

    private void moveScrollToLastMessage() {
        if (this.chatListView != null && !this.messages.isEmpty()) {
            this.chatLayoutManager.scrollToPositionWithOffset(0, 0);
        }
    }

    private boolean sendSecretMessageRead(MessageObject messageObject) {
        int i = 0;
        if (messageObject == null || messageObject.isOut() || !messageObject.isSecretMedia() || messageObject.messageOwner.destroyTime != 0 || messageObject.messageOwner.ttl <= 0) {
            return false;
        }
        if (this.currentEncryptedChat != null) {
            MessagesController.getInstance(this.currentAccount).markMessageAsRead(this.dialog_id, messageObject.messageOwner.random_id, messageObject.messageOwner.ttl);
        } else {
            MessagesController instance = MessagesController.getInstance(this.currentAccount);
            int id = messageObject.getId();
            if (ChatObject.isChannel(this.currentChat)) {
                i = this.currentChat.id;
            }
            instance.markMessageAsRead(id, i, messageObject.messageOwner.ttl);
        }
        messageObject.messageOwner.destroyTime = messageObject.messageOwner.ttl + ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
        return true;
    }

    private void clearChatData() {
        this.messages.clear();
        this.messagesByDays.clear();
        this.waitingForLoad.clear();
        this.groupedMessagesMap.clear();
        this.progressView.setVisibility(this.chatAdapter.botInfoRow == -1 ? 0 : 4);
        this.chatListView.setEmptyView(null);
        for (int a = 0; a < 2; a++) {
            this.messagesDict[a].clear();
            if (this.currentEncryptedChat == null) {
                this.maxMessageId[a] = ConnectionsManager.DEFAULT_DATACENTER_ID;
                this.minMessageId[a] = Integer.MIN_VALUE;
            } else {
                this.maxMessageId[a] = Integer.MIN_VALUE;
                this.minMessageId[a] = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            this.maxDate[a] = Integer.MIN_VALUE;
            this.minDate[a] = 0;
            this.endReached[a] = false;
            this.cacheEndReached[a] = false;
            this.forwardEndReached[a] = true;
        }
        this.first = true;
        this.firstLoading = true;
        this.loading = true;
        this.loadingForward = false;
        this.waitingForReplyMessageLoad = false;
        this.startLoadFromMessageId = 0;
        this.last_message_id = 0;
        this.unreadMessageObject = null;
        this.createUnreadMessageAfterId = 0;
        this.createUnreadMessageAfterIdLoading = false;
        this.needSelectFromMessageId = false;
        this.chatAdapter.notifyDataSetChanged();
    }

    private void scrollToLastMessage(boolean pagedown) {
        if (!this.forwardEndReached[0] || this.first_unread_id != 0 || this.startLoadFromMessageId != 0) {
            clearChatData();
            this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
            MessagesController instance = MessagesController.getInstance(this.currentAccount);
            long j = this.dialog_id;
            int i = this.classGuid;
            boolean isChannel = ChatObject.isChannel(this.currentChat);
            int i2 = this.lastLoadIndex;
            this.lastLoadIndex = i2 + 1;
            instance.loadMessages(j, 30, 0, 0, true, 0, i, 0, 0, isChannel, i2);
        } else if (pagedown && this.chatLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
            showPagedownButton(false, true);
            this.highlightMessageId = ConnectionsManager.DEFAULT_DATACENTER_ID;
            updateVisibleRows();
        } else {
            this.chatLayoutManager.scrollToPositionWithOffset(0, 0);
        }
    }

    private void updateTextureViewPosition() {
        if (this.fragmentView != null && !this.paused) {
            MessageObject messageObject;
            boolean foundTextureViewMessage = false;
            int count = this.chatListView.getChildCount();
            int additionalTop = this.chatActivityEnterView.isTopViewVisible() ? AndroidUtilities.dp(48.0f) : 0;
            for (int a = 0; a < count; a++) {
                View view = this.chatListView.getChildAt(a);
                if (view instanceof ChatMessageCell) {
                    ChatMessageCell messageCell = (ChatMessageCell) view;
                    messageObject = messageCell.getMessageObject();
                    if (this.roundVideoContainer != null && messageObject.isRoundVideo() && MediaController.getInstance().isPlayingMessage(messageObject)) {
                        ImageReceiver imageReceiver = messageCell.getPhotoImage();
                        this.roundVideoContainer.setTranslationX(((float) imageReceiver.getImageX()) + messageCell.getTranslationX());
                        this.roundVideoContainer.setTranslationY((float) ((this.inPreviewMode ? AndroidUtilities.statusBarHeight : 0) + (((this.fragmentView.getPaddingTop() + messageCell.getTop()) + imageReceiver.getImageY()) - additionalTop)));
                        this.fragmentView.invalidate();
                        this.roundVideoContainer.invalidate();
                        foundTextureViewMessage = true;
                        if (this.roundVideoContainer != null) {
                            messageObject = MediaController.getInstance().getPlayingMessageObject();
                            if (messageObject == null || messageObject.eventId != 0) {
                            }
                            if (foundTextureViewMessage) {
                                MediaController.getInstance().setCurrentRoundVisible(true);
                                scrollToMessageId(messageObject.getId(), 0, false, 0, true);
                                return;
                            }
                            this.roundVideoContainer.setTranslationY((float) ((-AndroidUtilities.roundMessageSize) - 100));
                            this.fragmentView.invalidate();
                            if (messageObject != null && messageObject.isRoundVideo()) {
                                if (this.checkTextureViewPosition || PipRoundVideoView.getInstance() != null) {
                                    MediaController.getInstance().setCurrentRoundVisible(false);
                                    return;
                                } else {
                                    scrollToMessageId(messageObject.getId(), 0, false, 0, true);
                                    return;
                                }
                            }
                            return;
                        }
                    }
                }
            }
            if (this.roundVideoContainer != null) {
                messageObject = MediaController.getInstance().getPlayingMessageObject();
                if (messageObject == null) {
                }
            }
        }
    }

    private void updateMessagesVisisblePart(boolean inLayout) {
        if (this.chatListView != null) {
            int a;
            MessageObject messageObject;
            int id;
            int count = this.chatListView.getChildCount();
            int additionalTop = this.chatActivityEnterView.isTopViewVisible() ? AndroidUtilities.dp(48.0f) : 0;
            int height = this.chatListView.getMeasuredHeight();
            int minPositionHolder = ConnectionsManager.DEFAULT_DATACENTER_ID;
            int minPositionDateHolder = ConnectionsManager.DEFAULT_DATACENTER_ID;
            View minDateChild = null;
            View minChild = null;
            View minMessageChild = null;
            boolean foundTextureViewMessage = false;
            int maxPositiveUnreadId = Integer.MIN_VALUE;
            int maxNegativeUnreadId = ConnectionsManager.DEFAULT_DATACENTER_ID;
            int maxUnreadDate = Integer.MIN_VALUE;
            if (this.currentEncryptedChat != null) {
            }
            for (a = 0; a < count; a++) {
                View view = this.chatListView.getChildAt(a);
                messageObject = null;
                if (view instanceof ChatMessageCell) {
                    int viewTop;
                    ChatMessageCell messageCell = (ChatMessageCell) view;
                    int top = messageCell.getTop();
                    int bottom = messageCell.getBottom();
                    if (top >= 0) {
                        viewTop = 0;
                    } else {
                        viewTop = -top;
                    }
                    int viewBottom = messageCell.getMeasuredHeight();
                    if (viewBottom > height) {
                        viewBottom = viewTop + height;
                    }
                    messageCell.setVisiblePart(viewTop, viewBottom - viewTop);
                    messageObject = messageCell.getMessageObject();
                    if (this.roundVideoContainer != null && messageObject.isRoundVideo() && MediaController.getInstance().isPlayingMessage(messageObject)) {
                        ImageReceiver imageReceiver = messageCell.getPhotoImage();
                        this.roundVideoContainer.setTranslationX(((float) imageReceiver.getImageX()) + messageCell.getTranslationX());
                        this.roundVideoContainer.setTranslationY((float) ((this.inPreviewMode ? AndroidUtilities.statusBarHeight : 0) + (((this.fragmentView.getPaddingTop() + top) + imageReceiver.getImageY()) - additionalTop)));
                        this.fragmentView.invalidate();
                        this.roundVideoContainer.invalidate();
                        foundTextureViewMessage = true;
                    }
                } else if (view instanceof ChatActionCell) {
                    messageObject = ((ChatActionCell) view).getMessageObject();
                }
                if (!(messageObject == null || messageObject.isOut() || !messageObject.isUnread())) {
                    id = messageObject.getId();
                    if (id > 0) {
                        maxPositiveUnreadId = Math.max(maxPositiveUnreadId, messageObject.getId());
                    }
                    if (id < 0) {
                        maxNegativeUnreadId = Math.min(maxNegativeUnreadId, messageObject.getId());
                    }
                    maxUnreadDate = Math.max(maxUnreadDate, messageObject.messageOwner.date);
                }
                if (view.getBottom() > this.chatListView.getPaddingTop()) {
                    int position = view.getBottom();
                    if (position < minPositionHolder) {
                        minPositionHolder = position;
                        if ((view instanceof ChatMessageCell) || (view instanceof ChatActionCell)) {
                            minMessageChild = view;
                        }
                        minChild = view;
                    }
                    if ((view instanceof ChatActionCell) && ((ChatActionCell) view).getMessageObject().isDateObject) {
                        if (view.getAlpha() != 1.0f) {
                            view.setAlpha(1.0f);
                        }
                        if (position < minPositionDateHolder) {
                            minPositionDateHolder = position;
                            minDateChild = view;
                        }
                    }
                }
            }
            if (this.roundVideoContainer != null) {
                if (foundTextureViewMessage) {
                    MediaController.getInstance().setCurrentRoundVisible(true);
                } else {
                    this.roundVideoContainer.setTranslationY((float) ((-AndroidUtilities.roundMessageSize) - 100));
                    this.fragmentView.invalidate();
                    messageObject = MediaController.getInstance().getPlayingMessageObject();
                    if (messageObject != null && messageObject.isRoundVideo() && messageObject.eventId == 0 && this.checkTextureViewPosition) {
                        MediaController.getInstance().setCurrentRoundVisible(false);
                    }
                }
            }
            if (minMessageChild != null) {
                if (minMessageChild instanceof ChatMessageCell) {
                    messageObject = ((ChatMessageCell) minMessageChild).getMessageObject();
                } else {
                    messageObject = ((ChatActionCell) minMessageChild).getMessageObject();
                }
                this.floatingDateView.setCustomDate(messageObject.messageOwner.date);
            }
            this.currentFloatingDateOnScreen = false;
            boolean z = ((minChild instanceof ChatMessageCell) || (minChild instanceof ChatActionCell)) ? false : true;
            this.currentFloatingTopIsNotMessage = z;
            if (minDateChild != null) {
                if (minDateChild.getTop() > this.chatListView.getPaddingTop() || this.currentFloatingTopIsNotMessage) {
                    if (minDateChild.getAlpha() != 1.0f) {
                        minDateChild.setAlpha(1.0f);
                    }
                    if (this.currentFloatingTopIsNotMessage) {
                        z = false;
                    } else {
                        z = true;
                    }
                    hideFloatingDateView(z);
                } else {
                    if (minDateChild.getAlpha() != 0.0f) {
                        minDateChild.setAlpha(0.0f);
                    }
                    if (this.floatingDateAnimation != null) {
                        this.floatingDateAnimation.cancel();
                        this.floatingDateAnimation = null;
                    }
                    if (this.floatingDateView.getTag() == null) {
                        this.floatingDateView.setTag(Integer.valueOf(1));
                    }
                    if (this.floatingDateView.getAlpha() != 1.0f) {
                        this.floatingDateView.setAlpha(1.0f);
                    }
                    this.currentFloatingDateOnScreen = true;
                }
                int offset = minDateChild.getBottom() - this.chatListView.getPaddingTop();
                if (offset <= this.floatingDateView.getMeasuredHeight() || offset >= this.floatingDateView.getMeasuredHeight() * 2) {
                    this.floatingDateView.setTranslationY(0.0f);
                } else {
                    this.floatingDateView.setTranslationY((float) (((-this.floatingDateView.getMeasuredHeight()) * 2) + offset));
                }
            } else {
                hideFloatingDateView(true);
                this.floatingDateView.setTranslationY(0.0f);
            }
            if (!this.firstLoading && !this.paused && !this.inPreviewMode) {
                if (maxPositiveUnreadId != Integer.MIN_VALUE || maxNegativeUnreadId != ConnectionsManager.DEFAULT_DATACENTER_ID) {
                    int counterDicrement = 0;
                    for (a = 0; a < this.messages.size(); a++) {
                        messageObject = (MessageObject) this.messages.get(a);
                        id = messageObject.getId();
                        if (maxPositiveUnreadId != Integer.MIN_VALUE && id > 0 && id <= maxPositiveUnreadId && messageObject.isUnread()) {
                            messageObject.setIsRead();
                            counterDicrement++;
                        }
                        if (maxNegativeUnreadId != ConnectionsManager.DEFAULT_DATACENTER_ID && id < 0 && id >= maxNegativeUnreadId && messageObject.isUnread()) {
                            messageObject.setIsRead();
                            counterDicrement++;
                        }
                    }
                    if ((this.forwardEndReached[0] && maxPositiveUnreadId == this.minMessageId[0]) || maxNegativeUnreadId == this.minMessageId[0]) {
                        this.newUnreadMessageCount = 0;
                    } else {
                        this.newUnreadMessageCount -= counterDicrement;
                        if (this.newUnreadMessageCount < 0) {
                            this.newUnreadMessageCount = 0;
                        }
                    }
                    if (inLayout) {
                        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$35(this));
                    } else {
                        inlineUpdate1();
                    }
                    MessagesController instance = MessagesController.getInstance(this.currentAccount);
                    long j = this.dialog_id;
                    boolean z2 = maxPositiveUnreadId == this.minMessageId[0] || maxNegativeUnreadId == this.minMessageId[0];
                    instance.markDialogAsRead(j, maxPositiveUnreadId, maxNegativeUnreadId, maxUnreadDate, false, counterDicrement, z2);
                    this.firstUnreadSent = true;
                } else if (!this.firstUnreadSent) {
                    this.newUnreadMessageCount = 0;
                    if (inLayout) {
                        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$36(this));
                    } else {
                        inlineUpdate2();
                    }
                    if (this.chatLayoutManager.findFirstVisibleItemPosition() == 0) {
                        MessagesController.getInstance(this.currentAccount).markDialogAsRead(this.dialog_id, this.minMessageId[0], this.minMessageId[0], this.maxDate[0], false, 0, true);
                        this.firstUnreadSent = true;
                    }
                }
            }
        }
    }

    private void inlineUpdate1() {
        if (this.prevSetUnreadCount != this.newUnreadMessageCount) {
            this.prevSetUnreadCount = this.newUnreadMessageCount;
            this.pagedownButtonCounter.setText(String.format("%d", new Object[]{Integer.valueOf(this.newUnreadMessageCount)}));
        }
        if (this.newUnreadMessageCount <= 0) {
            if (this.pagedownButtonCounter.getVisibility() != 4) {
                this.pagedownButtonCounter.setVisibility(4);
            }
        } else if (this.pagedownButtonCounter.getVisibility() != 0) {
            this.pagedownButtonCounter.setVisibility(0);
        }
    }

    private void inlineUpdate2() {
        if (this.prevSetUnreadCount != this.newUnreadMessageCount) {
            this.prevSetUnreadCount = this.newUnreadMessageCount;
            this.pagedownButtonCounter.setText(String.format("%d", new Object[]{Integer.valueOf(this.newUnreadMessageCount)}));
        }
        if (this.pagedownButtonCounter.getVisibility() != 4) {
            this.pagedownButtonCounter.setVisibility(4);
        }
    }

    private void toggleMute(boolean instant) {
        Editor editor;
        TL_dialog dialog;
        if (MessagesController.getInstance(this.currentAccount).isDialogMuted(this.dialog_id)) {
            editor = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            editor.putInt("notify2_" + this.dialog_id, 0);
            MessagesStorage.getInstance(this.currentAccount).setDialogFlags(this.dialog_id, 0);
            editor.commit();
            dialog = (TL_dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialog_id);
            if (dialog != null) {
                dialog.notify_settings = new TL_peerNotifySettings();
            }
            NotificationsController.getInstance(this.currentAccount).updateServerNotificationsSettings(this.dialog_id);
        } else if (instant) {
            editor = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            editor.putInt("notify2_" + this.dialog_id, 2);
            MessagesStorage.getInstance(this.currentAccount).setDialogFlags(this.dialog_id, 1);
            editor.commit();
            dialog = (TL_dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialog_id);
            if (dialog != null) {
                dialog.notify_settings = new TL_peerNotifySettings();
                dialog.notify_settings.mute_until = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            NotificationsController.getInstance(this.currentAccount).updateServerNotificationsSettings(this.dialog_id);
            NotificationsController.getInstance(this.currentAccount).removeNotificationsForDialog(this.dialog_id);
        } else {
            showDialog(AlertsCreator.createMuteAlert(getParentActivity(), this.dialog_id));
        }
    }

    private int getScrollOffsetForMessage(MessageObject object) {
        int offset = ConnectionsManager.DEFAULT_DATACENTER_ID;
        GroupedMessages groupedMessages = getValidGroupedMessage(object);
        if (groupedMessages != null) {
            float itemHeight;
            GroupedMessagePosition currentPosition = (GroupedMessagePosition) groupedMessages.positions.get(object);
            float maxH = ((float) Math.max(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y)) * 0.5f;
            if (currentPosition.siblingHeights != null) {
                itemHeight = currentPosition.siblingHeights[0];
            } else {
                itemHeight = currentPosition.ph;
            }
            float totalHeight = 0.0f;
            float moveDiff = 0.0f;
            SparseBooleanArray array = new SparseBooleanArray();
            for (int a = 0; a < groupedMessages.posArray.size(); a++) {
                GroupedMessagePosition pos = (GroupedMessagePosition) groupedMessages.posArray.get(a);
                if (array.indexOfKey(pos.minY) < 0 && pos.siblingHeights == null) {
                    array.put(pos.minY, true);
                    if (pos.minY < currentPosition.minY) {
                        moveDiff -= pos.ph;
                    } else if (pos.minY > currentPosition.minY) {
                        moveDiff += pos.ph;
                    }
                    totalHeight += pos.ph;
                }
            }
            if (Math.abs(totalHeight - itemHeight) < 0.02f) {
                offset = ((((int) (((float) this.chatListView.getMeasuredHeight()) - (totalHeight * maxH))) / 2) - this.chatListView.getPaddingTop()) - AndroidUtilities.dp(7.0f);
            } else {
                offset = ((((int) (((float) this.chatListView.getMeasuredHeight()) - ((itemHeight + moveDiff) * maxH))) / 2) - this.chatListView.getPaddingTop()) - AndroidUtilities.dp(7.0f);
            }
        }
        if (offset == ConnectionsManager.DEFAULT_DATACENTER_ID) {
            offset = (this.chatListView.getMeasuredHeight() - object.getApproximateHeight()) / 2;
        }
        return Math.max(0, offset);
    }

    public void scrollToMessageId(int id, int fromMessageId, boolean select, int loadIndex, boolean smooth) {
        this.wasManualScroll = true;
        MessageObject object = (MessageObject) this.messagesDict[loadIndex].get(id);
        boolean query = false;
        if (object == null) {
            query = true;
        } else if (this.messages.indexOf(object) != -1) {
            if (select) {
                this.highlightMessageId = id;
            } else {
                this.highlightMessageId = ConnectionsManager.DEFAULT_DATACENTER_ID;
            }
            int yOffset = getScrollOffsetForMessage(object);
            if (smooth) {
                if (this.messages.get(this.messages.size() - 1) == object) {
                    this.chatListView.smoothScrollToPosition(this.chatAdapter.getItemCount() - 1);
                } else {
                    this.chatListView.smoothScrollToPosition(this.chatAdapter.messagesStartRow + this.messages.indexOf(object));
                }
            } else if (this.messages.get(this.messages.size() - 1) == object) {
                this.chatLayoutManager.scrollToPositionWithOffset(this.chatAdapter.getItemCount() - 1, yOffset, false);
            } else {
                this.chatLayoutManager.scrollToPositionWithOffset(this.chatAdapter.messagesStartRow + this.messages.indexOf(object), yOffset, false);
            }
            updateVisibleRows();
            boolean found = false;
            int count = this.chatListView.getChildCount();
            for (int a = 0; a < count; a++) {
                View view = this.chatListView.getChildAt(a);
                MessageObject messageObject;
                if (view instanceof ChatMessageCell) {
                    messageObject = ((ChatMessageCell) view).getMessageObject();
                    if (messageObject != null && messageObject.getId() == object.getId()) {
                        found = true;
                        break;
                    }
                } else if (view instanceof ChatActionCell) {
                    messageObject = ((ChatActionCell) view).getMessageObject();
                    if (messageObject != null && messageObject.getId() == object.getId()) {
                        found = true;
                        break;
                    }
                } else {
                    continue;
                }
            }
            if (!found) {
                showPagedownButton(true, true);
            }
        } else {
            query = true;
        }
        if (query) {
            if (this.currentEncryptedChat == null || MessagesStorage.getInstance(this.currentAccount).checkMessageId(this.dialog_id, this.startLoadFromMessageId)) {
                this.waitingForLoad.clear();
                this.waitingForReplyMessageLoad = true;
                this.highlightMessageId = ConnectionsManager.DEFAULT_DATACENTER_ID;
                this.scrollToMessagePosition = -10000;
                this.startLoadFromMessageId = id;
                if (id == this.createUnreadMessageAfterId) {
                    this.createUnreadMessageAfterIdLoading = true;
                }
                this.waitingForLoad.add(Integer.valueOf(this.lastLoadIndex));
                MessagesController instance = MessagesController.getInstance(this.currentAccount);
                long j = loadIndex == 0 ? this.dialog_id : this.mergeDialogId;
                int i = AndroidUtilities.isTablet() ? 30 : 20;
                int i2 = this.startLoadFromMessageId;
                int i3 = this.classGuid;
                boolean isChannel = ChatObject.isChannel(this.currentChat);
                int i4 = this.lastLoadIndex;
                this.lastLoadIndex = i4 + 1;
                instance.loadMessages(j, i, i2, 0, true, 0, i3, 3, 0, isChannel, i4);
            } else {
                return;
            }
        }
        this.returnToMessageId = fromMessageId;
        this.returnToLoadIndex = loadIndex;
        this.needSelectFromMessageId = select;
    }

    private void showPagedownButton(boolean show, boolean animated) {
        if (this.pagedownButton != null) {
            AnimatorSet animatorSet;
            Animator[] animatorArr;
            if (show) {
                this.pagedownButtonShowedByScroll = false;
                if (this.pagedownButton.getTag() == null) {
                    if (this.pagedownButtonAnimation != null) {
                        this.pagedownButtonAnimation.cancel();
                        this.pagedownButtonAnimation = null;
                    }
                    if (animated) {
                        if (this.pagedownButton.getTranslationY() == 0.0f) {
                            this.pagedownButton.setTranslationY((float) AndroidUtilities.dp(100.0f));
                        }
                        this.pagedownButton.setVisibility(0);
                        this.pagedownButton.setTag(Integer.valueOf(1));
                        this.pagedownButtonAnimation = new AnimatorSet();
                        if (this.mentiondownButton.getVisibility() == 0) {
                            animatorSet = this.pagedownButtonAnimation;
                            animatorArr = new Animator[2];
                            animatorArr[0] = ObjectAnimator.ofFloat(this.pagedownButton, "translationY", new float[]{0.0f});
                            animatorArr[1] = ObjectAnimator.ofFloat(this.mentiondownButton, "translationY", new float[]{(float) (-AndroidUtilities.dp(72.0f))});
                            animatorSet.playTogether(animatorArr);
                        } else {
                            animatorSet = this.pagedownButtonAnimation;
                            animatorArr = new Animator[1];
                            animatorArr[0] = ObjectAnimator.ofFloat(this.pagedownButton, "translationY", new float[]{0.0f});
                            animatorSet.playTogether(animatorArr);
                        }
                        this.pagedownButtonAnimation.setDuration(200);
                        this.pagedownButtonAnimation.start();
                        return;
                    }
                    this.pagedownButton.setVisibility(0);
                    return;
                }
                return;
            }
            this.returnToMessageId = 0;
            this.newUnreadMessageCount = 0;
            if (this.pagedownButton.getTag() != null) {
                this.pagedownButton.setTag(null);
                if (this.pagedownButtonAnimation != null) {
                    this.pagedownButtonAnimation.cancel();
                    this.pagedownButtonAnimation = null;
                }
                if (animated) {
                    this.pagedownButtonAnimation = new AnimatorSet();
                    if (this.mentiondownButton.getVisibility() == 0) {
                        animatorSet = this.pagedownButtonAnimation;
                        animatorArr = new Animator[2];
                        animatorArr[0] = ObjectAnimator.ofFloat(this.pagedownButton, "translationY", new float[]{(float) AndroidUtilities.dp(100.0f)});
                        animatorArr[1] = ObjectAnimator.ofFloat(this.mentiondownButton, "translationY", new float[]{0.0f});
                        animatorSet.playTogether(animatorArr);
                    } else {
                        animatorSet = this.pagedownButtonAnimation;
                        animatorArr = new Animator[1];
                        animatorArr[0] = ObjectAnimator.ofFloat(this.pagedownButton, "translationY", new float[]{(float) AndroidUtilities.dp(100.0f)});
                        animatorSet.playTogether(animatorArr);
                    }
                    this.pagedownButtonAnimation.setDuration(200);
                    this.pagedownButtonAnimation.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            ChatActivity.this.pagedownButtonCounter.setVisibility(4);
                            ChatActivity.this.pagedownButton.setVisibility(4);
                        }
                    });
                    this.pagedownButtonAnimation.start();
                    return;
                }
                this.pagedownButton.setVisibility(4);
            }
        }
    }

    private void showMentiondownButton(boolean show, boolean animated) {
        if (this.mentiondownButton != null) {
            if (!show) {
                this.returnToMessageId = 0;
                if (this.mentiondownButton.getTag() != null) {
                    this.mentiondownButton.setTag(null);
                    if (this.mentiondownButtonAnimation != null) {
                        this.mentiondownButtonAnimation.cancel();
                        this.mentiondownButtonAnimation = null;
                    }
                    if (animated) {
                        if (this.pagedownButton.getVisibility() == 0) {
                            this.mentiondownButtonAnimation = ObjectAnimator.ofFloat(this.mentiondownButton, "alpha", new float[]{1.0f, 0.0f}).setDuration(200);
                        } else {
                            this.mentiondownButtonAnimation = ObjectAnimator.ofFloat(this.mentiondownButton, "translationY", new float[]{(float) AndroidUtilities.dp(100.0f)}).setDuration(200);
                        }
                        this.mentiondownButtonAnimation.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                ChatActivity.this.mentiondownButtonCounter.setVisibility(4);
                                ChatActivity.this.mentiondownButton.setVisibility(4);
                            }
                        });
                        this.mentiondownButtonAnimation.start();
                        return;
                    }
                    this.mentiondownButton.setVisibility(4);
                }
            } else if (this.mentiondownButton.getTag() == null) {
                if (this.mentiondownButtonAnimation != null) {
                    this.mentiondownButtonAnimation.cancel();
                    this.mentiondownButtonAnimation = null;
                }
                if (animated) {
                    this.mentiondownButton.setVisibility(0);
                    this.mentiondownButton.setTag(Integer.valueOf(1));
                    if (this.pagedownButton.getVisibility() == 0) {
                        this.mentiondownButton.setTranslationY((float) (-AndroidUtilities.dp(72.0f)));
                        this.mentiondownButtonAnimation = ObjectAnimator.ofFloat(this.mentiondownButton, "alpha", new float[]{0.0f, 1.0f}).setDuration(200);
                    } else {
                        if (this.mentiondownButton.getTranslationY() == 0.0f) {
                            this.mentiondownButton.setTranslationY((float) AndroidUtilities.dp(100.0f));
                        }
                        this.mentiondownButtonAnimation = ObjectAnimator.ofFloat(this.mentiondownButton, "translationY", new float[]{0.0f}).setDuration(200);
                    }
                    this.mentiondownButtonAnimation.start();
                    return;
                }
                this.mentiondownButton.setVisibility(0);
            }
        }
    }

    private void updateSecretStatus() {
        if (this.bottomOverlay != null) {
            boolean hideKeyboard = false;
            if (ChatObject.isChannel(this.currentChat) && this.currentChat.banned_rights != null && this.currentChat.banned_rights.send_messages) {
                if (AndroidUtilities.isBannedForever(this.currentChat.banned_rights.until_date)) {
                    this.bottomOverlayText.setText(LocaleController.getString("SendMessageRestrictedForever", R.string.SendMessageRestrictedForever));
                } else {
                    this.bottomOverlayText.setText(LocaleController.formatString("SendMessageRestricted", R.string.SendMessageRestricted, LocaleController.formatDateForBan((long) this.currentChat.banned_rights.until_date)));
                }
                this.bottomOverlay.setVisibility(0);
                if (this.mentionListAnimation != null) {
                    this.mentionListAnimation.cancel();
                    this.mentionListAnimation = null;
                }
                this.mentionContainer.setVisibility(8);
                this.mentionContainer.setTag(null);
                hideKeyboard = true;
            } else if (this.currentEncryptedChat == null || this.bigEmptyView == null) {
                this.bottomOverlay.setVisibility(4);
                return;
            } else {
                if (this.currentEncryptedChat instanceof TL_encryptedChatRequested) {
                    this.bottomOverlayText.setText(LocaleController.getString("EncryptionProcessing", R.string.EncryptionProcessing));
                    this.bottomOverlay.setVisibility(0);
                    hideKeyboard = true;
                } else if (this.currentEncryptedChat instanceof TL_encryptedChatWaiting) {
                    this.bottomOverlayText.setText(AndroidUtilities.replaceTags(LocaleController.formatString("AwaitingEncryption", R.string.AwaitingEncryption, "<b>" + this.currentUser.first_name + "</b>")));
                    this.bottomOverlay.setVisibility(0);
                    hideKeyboard = true;
                } else if (this.currentEncryptedChat instanceof TL_encryptedChatDiscarded) {
                    this.bottomOverlayText.setText(LocaleController.getString("EncryptionRejected", R.string.EncryptionRejected));
                    this.bottomOverlay.setVisibility(0);
                    this.chatActivityEnterView.setFieldText(TtmlNode.ANONYMOUS_REGION_ID);
                    DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, false);
                    hideKeyboard = true;
                } else if (this.currentEncryptedChat instanceof TL_encryptedChat) {
                    this.bottomOverlay.setVisibility(4);
                }
                checkRaiseSensors();
                checkActionBarMenu();
            }
            if (hideKeyboard) {
                this.chatActivityEnterView.hidePopup(false);
                if (getParentActivity() != null) {
                    AndroidUtilities.hideKeyboard(getParentActivity().getCurrentFocus());
                }
            }
        }
    }

    public void onRequestPermissionsResultFragment(int requestCode, String[] permissions, int[] grantResults) {
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
        }
        if (this.mentionsAdapter != null) {
            this.mentionsAdapter.onRequestPermissionsResultFragment(requestCode, permissions, grantResults);
        }
        if (requestCode == 17 && this.chatAttachAlert != null) {
            this.chatAttachAlert.checkCamera(false);
        } else if (requestCode == 21) {
            if (getParentActivity() != null && grantResults != null && grantResults.length != 0 && grantResults[0] != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder.setMessage(LocaleController.getString("PermissionNoAudioVideo", R.string.PermissionNoAudioVideo));
                builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", R.string.PermissionOpenSettings), new ChatActivity$$Lambda$37(this));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                builder.show();
            }
        } else if (requestCode == 19 && grantResults != null && grantResults.length > 0 && grantResults[0] == 0) {
            processSelectedAttach(0);
        } else if (requestCode == 20 && grantResults != null && grantResults.length > 0 && grantResults[0] == 0) {
            processSelectedAttach(2);
        } else if (requestCode == 101 && this.currentUser != null) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                VoIPHelper.permissionDenied(getParentActivity(), null);
            } else {
                VoIPHelper.startCall(this.currentUser, getParentActivity(), MessagesController.getInstance(this.currentAccount).getUserFull(this.currentUser.id));
            }
        }
    }

    final /* synthetic */ void lambda$onRequestPermissionsResultFragment$48$ChatActivity(DialogInterface dialog, int which) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            getParentActivity().startActivity(intent);
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void checkActionBarMenu() {
        if ((this.currentEncryptedChat == null || (this.currentEncryptedChat instanceof TL_encryptedChat)) && ((this.currentChat == null || !ChatObject.isNotInChat(this.currentChat)) && (this.currentUser == null || !UserObject.isDeleted(this.currentUser)))) {
            if (this.timeItem2 != null) {
                this.timeItem2.setVisibility(0);
            }
            if (this.avatarContainer != null) {
                this.avatarContainer.showTimeItem();
            }
        } else {
            if (this.timeItem2 != null) {
                this.timeItem2.setVisibility(8);
            }
            if (this.avatarContainer != null) {
                this.avatarContainer.hideTimeItem();
            }
        }
        if (!(this.avatarContainer == null || this.currentEncryptedChat == null)) {
            this.avatarContainer.setTime(this.currentEncryptedChat.ttl);
        }
        checkAndUpdateAvatar();
    }

    private int getMessageType(MessageObject messageObject) {
        if (messageObject == null) {
            return -1;
        }
        InputStickerSet inputStickerSet;
        boolean canSave;
        String mime;
        if (this.currentEncryptedChat == null) {
            boolean isBroadcastError;
            if (this.isBroadcast && messageObject.getId() <= 0 && messageObject.isSendError()) {
                isBroadcastError = true;
            } else {
                isBroadcastError = false;
            }
            if (messageObject.isEditing()) {
                return -1;
            }
            if ((this.isBroadcast || messageObject.getId() > 0 || !messageObject.isOut()) && !isBroadcastError) {
                if (messageObject.type == 6) {
                    return -1;
                }
                if (messageObject.type == 10 || messageObject.type == 11 || messageObject.type == 16) {
                    if (messageObject.getId() == 0) {
                        return -1;
                    }
                    return 1;
                } else if (messageObject.isVoice()) {
                    return 2;
                } else {
                    if (messageObject.isSticker()) {
                        inputStickerSet = messageObject.getInputStickerSet();
                        if (inputStickerSet instanceof TL_inputStickerSetID) {
                            if (!DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(inputStickerSet.id)) {
                                return 7;
                            }
                        } else if ((inputStickerSet instanceof TL_inputStickerSetShortName) && !DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(inputStickerSet.short_name)) {
                            return 7;
                        }
                        return 9;
                    }
                    if ((!messageObject.isRoundVideo() || (messageObject.isRoundVideo() && BuildVars.DEBUG_VERSION)) && ((messageObject.messageOwner.media instanceof TL_messageMediaPhoto) || messageObject.getDocument() != null || messageObject.isMusic() || messageObject.isVideo())) {
                        canSave = false;
                        if (!TextUtils.isEmpty(messageObject.messageOwner.attachPath) && new File(messageObject.messageOwner.attachPath).exists()) {
                            canSave = true;
                        }
                        if (!canSave && FileLoader.getPathToMessage(messageObject.messageOwner).exists()) {
                            canSave = true;
                        }
                        if (canSave) {
                            if (messageObject.getDocument() != null) {
                                mime = messageObject.getDocument().mime_type;
                                if (mime != null) {
                                    if (messageObject.getDocumentName().toLowerCase().endsWith("attheme")) {
                                        return 10;
                                    }
                                    if (mime.endsWith("/xml")) {
                                        return 5;
                                    }
                                    if (mime.endsWith("/png") || mime.endsWith("/jpg") || mime.endsWith("/jpeg")) {
                                        return 6;
                                    }
                                }
                            }
                            return 4;
                        }
                    } else if (messageObject.type == 12) {
                        return 8;
                    } else {
                        if (messageObject.isMediaEmpty()) {
                            return 3;
                        }
                    }
                    return 2;
                }
            } else if (!messageObject.isSendError()) {
                return -1;
            } else {
                if (messageObject.isMediaEmpty()) {
                    return 20;
                }
                return 0;
            }
        } else if (messageObject.isSending()) {
            return -1;
        } else {
            if (messageObject.type == 6) {
                return -1;
            }
            if (messageObject.isSendError()) {
                if (messageObject.isMediaEmpty()) {
                    return 20;
                }
                return 0;
            } else if (messageObject.type == 10 || messageObject.type == 11) {
                if (messageObject.getId() == 0 || messageObject.isSending()) {
                    return -1;
                }
                return 1;
            } else if (messageObject.isVoice()) {
                return 2;
            } else {
                if (messageObject.isSticker()) {
                    inputStickerSet = messageObject.getInputStickerSet();
                    if ((inputStickerSet instanceof TL_inputStickerSetShortName) && !DataQuery.getInstance(this.currentAccount).isStickerPackInstalled(inputStickerSet.short_name)) {
                        return 7;
                    }
                } else if (!messageObject.isRoundVideo() && ((messageObject.messageOwner.media instanceof TL_messageMediaPhoto) || messageObject.getDocument() != null || messageObject.isMusic() || messageObject.isVideo())) {
                    canSave = false;
                    if (!TextUtils.isEmpty(messageObject.messageOwner.attachPath) && new File(messageObject.messageOwner.attachPath).exists()) {
                        canSave = true;
                    }
                    if (!canSave && FileLoader.getPathToMessage(messageObject.messageOwner).exists()) {
                        canSave = true;
                    }
                    if (canSave) {
                        if (messageObject.getDocument() != null) {
                            mime = messageObject.getDocument().mime_type;
                            if (mime != null && mime.endsWith("text/xml")) {
                                return 5;
                            }
                        }
                        if (messageObject.messageOwner.ttl <= 0) {
                            return 4;
                        }
                    }
                } else if (messageObject.type == 12) {
                    return 8;
                } else {
                    if (messageObject.isMediaEmpty()) {
                        return 3;
                    }
                }
                return 2;
            }
        }
    }

    private void addToSelectedMessages(MessageObject messageObject, boolean outside) {
        addToSelectedMessages(messageObject, outside, true);
    }

    private void addToSelectedMessages(MessageObject messageObject, boolean outside, boolean last) {
        int a;
        if (messageObject != null) {
            int index = messageObject.getDialogId() == this.dialog_id ? 0 : 1;
            GroupedMessages groupedMessages;
            if (outside && messageObject.getGroupId() != 0) {
                boolean hasUnselected = false;
                groupedMessages = (GroupedMessages) this.groupedMessagesMap.get(messageObject.getGroupId());
                if (groupedMessages != null) {
                    int lastNum = 0;
                    for (a = 0; a < groupedMessages.messages.size(); a++) {
                        if (this.selectedMessagesIds[index].indexOfKey(((MessageObject) groupedMessages.messages.get(a)).getId()) < 0) {
                            hasUnselected = true;
                            lastNum = a;
                        }
                    }
                    a = 0;
                    while (a < groupedMessages.messages.size()) {
                        MessageObject message = (MessageObject) groupedMessages.messages.get(a);
                        if (!hasUnselected) {
                            addToSelectedMessages(message, false, a == groupedMessages.messages.size() + -1);
                        } else if (this.selectedMessagesIds[index].indexOfKey(message.getId()) < 0) {
                            addToSelectedMessages(message, false, a == lastNum);
                        }
                        a++;
                    }
                    return;
                }
                return;
            } else if (this.selectedMessagesIds[index].indexOfKey(messageObject.getId()) >= 0) {
                this.selectedMessagesIds[index].remove(messageObject.getId());
                if (messageObject.type == 0 || messageObject.caption != null) {
                    this.selectedMessagesCanCopyIds[index].remove(messageObject.getId());
                }
                if (messageObject.isSticker()) {
                    this.selectedMessagesCanStarIds[index].remove(messageObject.getId());
                }
                if (messageObject.canEditMessage(this.currentChat) && messageObject.getGroupId() != 0) {
                    groupedMessages = (GroupedMessages) this.groupedMessagesMap.get(messageObject.getGroupId());
                    if (groupedMessages != null && groupedMessages.messages.size() > 1) {
                        this.canEditMessagesCount--;
                    }
                }
                if (!messageObject.canDeleteMessage(this.currentChat)) {
                    this.cantDeleteMessagesCount--;
                }
            } else if (this.selectedMessagesIds[0].size() + this.selectedMessagesIds[1].size() < 100) {
                this.selectedMessagesIds[index].put(messageObject.getId(), messageObject);
                if (messageObject.type == 0 || messageObject.caption != null) {
                    this.selectedMessagesCanCopyIds[index].put(messageObject.getId(), messageObject);
                }
                if (messageObject.isSticker()) {
                    this.selectedMessagesCanStarIds[index].put(messageObject.getId(), messageObject);
                }
                if (messageObject.canEditMessage(this.currentChat) && messageObject.getGroupId() != 0) {
                    groupedMessages = (GroupedMessages) this.groupedMessagesMap.get(messageObject.getGroupId());
                    if (groupedMessages != null && groupedMessages.messages.size() > 1) {
                        this.canEditMessagesCount++;
                    }
                }
                if (!messageObject.canDeleteMessage(this.currentChat)) {
                    this.cantDeleteMessagesCount++;
                }
            } else {
                return;
            }
        }
        if (last && this.actionBar.isActionModeShowed()) {
            int selectedCount = this.selectedMessagesIds[0].size() + this.selectedMessagesIds[1].size();
            if (selectedCount == 0) {
                this.actionBar.hideActionMode();
                updatePinnedMessageView(true);
                this.startReplyOnTextChange = false;
                return;
            }
            ActionBarMenuItem copyItem = this.actionBar.createActionMode().getItem(10);
            ActionBarMenuItem starItem = this.actionBar.createActionMode().getItem(22);
            ActionBarMenuItem editItem = this.actionBar.createActionMode().getItem(23);
            ActionBarMenuItem replyItem = this.actionBar.createActionMode().getItem(19);
            int copyVisible = copyItem.getVisibility();
            int starVisible = starItem.getVisibility();
            copyItem.setVisibility(this.selectedMessagesCanCopyIds[0].size() + this.selectedMessagesCanCopyIds[1].size() != 0 ? 0 : 8);
            int i = (DataQuery.getInstance(this.currentAccount).canAddStickerToFavorites() && this.selectedMessagesCanStarIds[0].size() + this.selectedMessagesCanStarIds[1].size() == selectedCount) ? 0 : 8;
            starItem.setVisibility(i);
            int newCopyVisible = copyItem.getVisibility();
            int newStarVisible = starItem.getVisibility();
            this.actionBar.createActionMode().getItem(12).setVisibility(this.cantDeleteMessagesCount == 0 ? 0 : 8);
            if (editItem != null) {
                i = (this.canEditMessagesCount == 1 && this.selectedMessagesIds[0].size() + this.selectedMessagesIds[1].size() == 1) ? 0 : 8;
                editItem.setVisibility(i);
            }
            this.hasUnfavedSelected = false;
            for (a = 0; a < 2; a++) {
                for (int b = 0; b < this.selectedMessagesCanStarIds[a].size(); b++) {
                    if (!DataQuery.getInstance(this.currentAccount).isStickerInFavorites(((MessageObject) this.selectedMessagesCanStarIds[a].valueAt(b)).getDocument())) {
                        this.hasUnfavedSelected = true;
                        break;
                    }
                }
                if (this.hasUnfavedSelected) {
                    break;
                }
            }
            starItem.setIcon(this.hasUnfavedSelected ? R.drawable.ic_ab_fave : R.drawable.ic_ab_unfave);
            if (replyItem != null) {
                boolean allowChatActions = true;
                if ((this.currentEncryptedChat != null && AndroidUtilities.getPeerLayerVersion(this.currentEncryptedChat.layer) < 46) || this.isBroadcast || ((this.bottomOverlayChat != null && this.bottomOverlayChat.getVisibility() == 0) || (this.currentChat != null && (ChatObject.isNotInChat(this.currentChat) || !((!ChatObject.isChannel(this.currentChat) || ChatObject.canPost(this.currentChat) || this.currentChat.megagroup) && ChatObject.canSendMessages(this.currentChat)))))) {
                    allowChatActions = false;
                }
                int newVisibility = (allowChatActions && this.selectedMessagesIds[0].size() + this.selectedMessagesIds[1].size() == 1) ? 0 : 8;
                boolean z = newVisibility == 0 && !this.chatActivityEnterView.hasText();
                this.startReplyOnTextChange = z;
                if (replyItem.getVisibility() != newVisibility) {
                    if (this.replyButtonAnimation != null) {
                        this.replyButtonAnimation.cancel();
                    }
                    if (copyVisible == newCopyVisible && starVisible == newStarVisible) {
                        this.replyButtonAnimation = new AnimatorSet();
                        replyItem.setPivotX((float) AndroidUtilities.dp(54.0f));
                        editItem.setPivotX((float) AndroidUtilities.dp(54.0f));
                        AnimatorSet animatorSet;
                        Animator[] animatorArr;
                        if (newVisibility == 0) {
                            replyItem.setVisibility(newVisibility);
                            animatorSet = this.replyButtonAnimation;
                            animatorArr = new Animator[4];
                            animatorArr[0] = ObjectAnimator.ofFloat(replyItem, "alpha", new float[]{1.0f});
                            animatorArr[1] = ObjectAnimator.ofFloat(replyItem, "scaleX", new float[]{1.0f});
                            animatorArr[2] = ObjectAnimator.ofFloat(editItem, "alpha", new float[]{1.0f});
                            animatorArr[3] = ObjectAnimator.ofFloat(editItem, "scaleX", new float[]{1.0f});
                            animatorSet.playTogether(animatorArr);
                        } else {
                            animatorSet = this.replyButtonAnimation;
                            animatorArr = new Animator[4];
                            animatorArr[0] = ObjectAnimator.ofFloat(replyItem, "alpha", new float[]{0.0f});
                            animatorArr[1] = ObjectAnimator.ofFloat(replyItem, "scaleX", new float[]{0.0f});
                            animatorArr[2] = ObjectAnimator.ofFloat(editItem, "alpha", new float[]{0.0f});
                            animatorArr[3] = ObjectAnimator.ofFloat(editItem, "scaleX", new float[]{0.0f});
                            animatorSet.playTogether(animatorArr);
                        }
                        this.replyButtonAnimation.setDuration(100);
                        final int i2 = newVisibility;
                        final ActionBarMenuItem actionBarMenuItem = replyItem;
                        this.replyButtonAnimation.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                if (ChatActivity.this.replyButtonAnimation != null && ChatActivity.this.replyButtonAnimation.equals(animation) && i2 == 8) {
                                    actionBarMenuItem.setVisibility(8);
                                }
                            }

                            public void onAnimationCancel(Animator animation) {
                                if (ChatActivity.this.replyButtonAnimation != null && ChatActivity.this.replyButtonAnimation.equals(animation)) {
                                    ChatActivity.this.replyButtonAnimation = null;
                                }
                            }
                        });
                        this.replyButtonAnimation.start();
                        return;
                    }
                    if (newVisibility == 0) {
                        replyItem.setAlpha(1.0f);
                        replyItem.setScaleX(1.0f);
                    } else {
                        replyItem.setAlpha(0.0f);
                        replyItem.setScaleX(0.0f);
                    }
                    replyItem.setVisibility(newVisibility);
                }
            }
        }
    }

    private void processRowSelect(View view, boolean outside) {
        MessageObject message = null;
        if (view instanceof ChatMessageCell) {
            message = ((ChatMessageCell) view).getMessageObject();
        } else if (view instanceof ChatActionCell) {
            message = ((ChatActionCell) view).getMessageObject();
        }
        int type = getMessageType(message);
        if (type >= 2 && type != 20) {
            addToSelectedMessages(message, outside);
            updateActionModeTitle();
            updateVisibleRows();
        }
    }

    private void updateActionModeTitle() {
        if (!this.actionBar.isActionModeShowed()) {
            return;
        }
        if (this.selectedMessagesIds[0].size() != 0 || this.selectedMessagesIds[1].size() != 0) {
            this.selectedMessagesCountTextView.setNumber(this.selectedMessagesIds[0].size() + this.selectedMessagesIds[1].size(), true);
        }
    }

    private void updateTitle() {
        if (this.avatarContainer != null) {
            if (this.currentChat != null) {
                this.avatarContainer.setTitle(this.currentChat.title);
            } else if (this.currentUser == null) {
            } else {
                if (this.currentUser.self) {
                    this.avatarContainer.setTitle(LocaleController.getString("SavedMessages", R.string.SavedMessages));
                } else if (MessagesController.isSupportId(this.currentUser.id) || ContactsController.getInstance(this.currentAccount).contactsDict.get(Integer.valueOf(this.currentUser.id)) != null || (ContactsController.getInstance(this.currentAccount).contactsDict.size() == 0 && ContactsController.getInstance(this.currentAccount).isLoadingContacts())) {
                    this.avatarContainer.setTitle(UserObject.getUserName(this.currentUser));
                } else if (TextUtils.isEmpty(this.currentUser.phone)) {
                    this.avatarContainer.setTitle(UserObject.getUserName(this.currentUser));
                } else {
                    this.avatarContainer.setTitle(PhoneFormat.getInstance().format("+" + this.currentUser.phone));
                }
            }
        }
    }

    private void updateBotButtons() {
        if (this.headerItem != null && this.currentUser != null && this.currentEncryptedChat == null && this.currentUser.bot) {
            boolean hasHelp = false;
            boolean hasSettings = false;
            if (this.botInfo.size() != 0) {
                for (int b = 0; b < this.botInfo.size(); b++) {
                    BotInfo info = (BotInfo) this.botInfo.valueAt(b);
                    for (int a = 0; a < info.commands.size(); a++) {
                        TL_botCommand command = (TL_botCommand) info.commands.get(a);
                        if (command.command.toLowerCase().equals("help")) {
                            hasHelp = true;
                        } else if (command.command.toLowerCase().equals("settings")) {
                            hasSettings = true;
                        }
                        if (hasSettings && hasHelp) {
                            break;
                        }
                    }
                }
            }
            if (hasHelp) {
                this.headerItem.showSubItem(30);
            } else {
                this.headerItem.hideSubItem(30);
            }
            if (hasSettings) {
                this.headerItem.showSubItem(31);
            } else {
                this.headerItem.hideSubItem(31);
            }
        }
    }

    private void updateTitleIcons() {
        Drawable drawable = null;
        if (this.avatarContainer != null) {
            Drawable rightIcon;
            if (MessagesController.getInstance(this.currentAccount).isDialogMuted(this.dialog_id)) {
                rightIcon = Theme.chat_muteIconDrawable;
            } else {
                rightIcon = null;
            }
            ChatAvatarContainer chatAvatarContainer = this.avatarContainer;
            if (this.currentEncryptedChat != null) {
                drawable = Theme.chat_lockIconDrawable;
            }
            chatAvatarContainer.setTitleIcons(drawable, rightIcon);
            if (this.muteItem == null) {
                return;
            }
            if (rightIcon != null) {
                this.muteItem.setText(LocaleController.getString("UnmuteNotifications", R.string.UnmuteNotifications));
            } else {
                this.muteItem.setText(LocaleController.getString("MuteNotifications", R.string.MuteNotifications));
            }
        }
    }

    private void checkAndUpdateAvatar() {
        if (this.currentUser != null) {
            User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.currentUser.id));
            if (user != null) {
                this.currentUser = user;
            } else {
                return;
            }
        } else if (this.currentChat != null) {
            Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.currentChat.id));
            if (chat != null) {
                this.currentChat = chat;
            } else {
                return;
            }
        }
        if (this.avatarContainer != null) {
            this.avatarContainer.checkAndUpdateAvatar();
        }
    }

    public void openVideoEditor(String videoPath, String caption) {
        if (getParentActivity() != null) {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(videoPath, 1);
            PhotoViewer.getInstance().setParentActivity(getParentActivity());
            final ArrayList<Object> cameraPhoto = new ArrayList();
            PhotoEntry entry = new PhotoEntry(0, 0, 0, videoPath, 0, true);
            entry.caption = caption;
            cameraPhoto.add(entry);
            final Bitmap bitmap = thumb;
            PhotoViewer.getInstance().openPhotoForSelect(cameraPhoto, 0, 2, new EmptyPhotoViewerProvider() {
                public BitmapHolder getThumbForPhoto(MessageObject messageObject, FileLocation fileLocation, int index) {
                    return new BitmapHolder(bitmap, null);
                }

                public void sendButtonPressed(int index, VideoEditedInfo videoEditedInfo) {
                    ChatActivity.this.sendMedia((PhotoEntry) cameraPhoto.get(0), videoEditedInfo);
                }

                public boolean canScrollAway() {
                    return false;
                }
            }, this);
            return;
        }
        fillEditingMediaWithCaption(caption, null);
        SendMessagesHelper.prepareSendingVideo(videoPath, 0, 0, 0, 0, null, this.dialog_id, this.replyingMessageObject, null, null, 0, this.editingMessageObject);
        hideFieldPanel();
        DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, true);
    }

    private void showAttachmentError() {
        if (getParentActivity() != null) {
            Toast.makeText(getParentActivity(), LocaleController.getString("UnsupportedAttachment", R.string.UnsupportedAttachment), 0).show();
        }
    }

    private void fillEditingMediaWithCaption(CharSequence caption, ArrayList<MessageEntity> entities) {
        if (this.editingMessageObject != null) {
            if (!TextUtils.isEmpty(caption)) {
                this.editingMessageObject.editingMessage = caption;
                this.editingMessageObject.editingMessageEntities = entities;
            } else if (this.chatActivityEnterView != null) {
                this.editingMessageObject.editingMessage = this.chatActivityEnterView.getFieldText();
                if (this.editingMessageObject.editingMessage == null && !TextUtils.isEmpty(this.editingMessageObject.messageOwner.message)) {
                    this.editingMessageObject.editingMessage = TtmlNode.ANONYMOUS_REGION_ID;
                }
            }
        }
    }

    private void sendUriAsDocument(Uri uri) {
        if (uri != null) {
            String extractUriFrom = uri.toString();
            if (extractUriFrom.contains("com.google.android.apps.photos.contentprovider")) {
                try {
                    String firstExtraction = extractUriFrom.split("/1/")[1];
                    int index = firstExtraction.indexOf("/ACTUAL");
                    if (index != -1) {
                        uri = Uri.parse(URLDecoder.decode(firstExtraction.substring(0, index), C.UTF8_NAME));
                    }
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
            String tempPath = AndroidUtilities.getPath(uri);
            String originalPath = tempPath;
            if (tempPath == null) {
                originalPath = uri.toString();
                tempPath = MediaController.copyFileToCache(uri, "file");
            }
            if (tempPath == null) {
                showAttachmentError();
                return;
            }
            fillEditingMediaWithCaption(null, null);
            SendMessagesHelper.prepareSendingDocument(tempPath, originalPath, null, null, this.dialog_id, this.replyingMessageObject, null, this.editingMessageObject);
            hideFieldPanel();
        }
    }

    public void onActivityResultFragment(int requestCode, int resultCode, Intent data) {
        if (resultCode != -1) {
            return;
        }
        if (requestCode == 0 || requestCode == 2) {
            createChatAttachView();
            if (this.chatAttachAlert != null) {
                this.chatAttachAlert.onActivityResultFragment(requestCode, data, this.currentPicturePath);
            }
            this.currentPicturePath = null;
        } else if (requestCode == 1) {
            if (data == null || data.getData() == null) {
                showAttachmentError();
                return;
            }
            Uri uri = data.getData();
            if (uri.toString().contains(MimeTypes.BASE_TYPE_VIDEO)) {
                String videoPath = null;
                try {
                    videoPath = AndroidUtilities.getPath(uri);
                } catch (Throwable e) {
                    FileLog.e(e);
                }
                if (videoPath == null) {
                    showAttachmentError();
                }
                if (this.paused) {
                    this.startVideoEdit = videoPath;
                } else {
                    openVideoEditor(videoPath, null);
                }
            } else {
                fillEditingMediaWithCaption(null, null);
                SendMessagesHelper.prepareSendingPhoto(null, uri, this.dialog_id, this.replyingMessageObject, null, null, null, null, 0, this.editingMessageObject);
            }
            hideFieldPanel();
            DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, true);
        } else if (requestCode != 21) {
        } else {
            if (data == null) {
                showAttachmentError();
                return;
            }
            if (data.getData() != null) {
                sendUriAsDocument(data.getData());
            } else if (data.getClipData() != null) {
                ClipData clipData = data.getClipData();
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    sendUriAsDocument(clipData.getItemAt(i).getUri());
                }
            } else {
                showAttachmentError();
            }
            hideFieldPanel();
            DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, true);
        }
    }

    public void saveSelfArgs(Bundle args) {
        if (this.currentPicturePath != null) {
            args.putString("path", this.currentPicturePath);
        }
    }

    public void restoreSelfArgs(Bundle args) {
        this.currentPicturePath = args.getString("path");
    }

    private void removeUnreadPlane(boolean scrollToEnd) {
        if (this.unreadMessageObject != null) {
            if (scrollToEnd) {
                boolean[] zArr = this.forwardEndReached;
                this.forwardEndReached[1] = true;
                zArr[0] = true;
                this.first_unread_id = 0;
                this.last_message_id = 0;
            }
            this.createUnreadMessageAfterId = 0;
            this.createUnreadMessageAfterIdLoading = false;
            removeMessageObject(this.unreadMessageObject);
            this.unreadMessageObject = null;
        }
    }

    public boolean processSendingText(String text) {
        return this.chatActivityEnterView.processSendingText(text);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void didReceivedNotification(int r162, int r163, java.lang.Object... r164) {
        /*
        r161 = this;
        r4 = org.telegram.messenger.NotificationCenter.messagesDidLoaded;
        r0 = r162;
        if (r0 != r4) goto L_0x0d63;
    L_0x0006:
        r4 = 10;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r71 = r4.intValue();
        r0 = r161;
        r4 = r0.classGuid;
        r0 = r71;
        if (r0 != r4) goto L_0x0067;
    L_0x0018:
        r0 = r161;
        r4 = r0.openAnimationEnded;
        if (r4 != 0) goto L_0x0040;
    L_0x001e:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.NotificationCenter.getInstance(r4);
        r6 = 4;
        r6 = new int[r6];
        r7 = 0;
        r8 = org.telegram.messenger.NotificationCenter.chatInfoDidLoaded;
        r6[r7] = r8;
        r7 = 1;
        r8 = org.telegram.messenger.NotificationCenter.dialogsNeedReload;
        r6[r7] = r8;
        r7 = 2;
        r8 = org.telegram.messenger.NotificationCenter.closeChats;
        r6[r7] = r8;
        r7 = 3;
        r8 = org.telegram.messenger.NotificationCenter.botKeyboardDidLoaded;
        r6[r7] = r8;
        r4.setAllowedNotificationsDutingAnimation(r6);
    L_0x0040:
        r4 = 11;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r133 = r4.intValue();
        r0 = r161;
        r4 = r0.waitingForLoad;
        r6 = java.lang.Integer.valueOf(r133);
        r78 = r4.indexOf(r6);
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r50 = r4.getClientUserId();
        r4 = -1;
        r0 = r78;
        if (r0 != r4) goto L_0x0068;
    L_0x0067:
        return;
    L_0x0068:
        r0 = r161;
        r4 = r0.waitingForLoad;
        r0 = r78;
        r4.remove(r0);
        r4 = 2;
        r98 = r164[r4];
        r98 = (java.util.ArrayList) r98;
        r44 = 0;
        r0 = r161;
        r4 = r0.waitingForReplyMessageLoad;
        if (r4 == 0) goto L_0x010f;
    L_0x007e:
        r0 = r161;
        r4 = r0.createUnreadMessageAfterIdLoading;
        if (r4 != 0) goto L_0x00e2;
    L_0x0084:
        r68 = 0;
        r18 = 0;
    L_0x0088:
        r4 = r98.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x00a6;
    L_0x0090:
        r0 = r98;
        r1 = r18;
        r118 = r0.get(r1);
        r118 = (org.telegram.messenger.MessageObject) r118;
        r4 = r118.getId();
        r0 = r161;
        r6 = r0.startLoadFromMessageId;
        if (r4 != r6) goto L_0x00ae;
    L_0x00a4:
        r68 = 1;
    L_0x00a6:
        if (r68 != 0) goto L_0x00e2;
    L_0x00a8:
        r4 = 0;
        r0 = r161;
        r0.startLoadFromMessageId = r4;
        goto L_0x0067;
    L_0x00ae:
        r4 = r18 + 1;
        r6 = r98.size();
        if (r4 >= r6) goto L_0x00df;
    L_0x00b6:
        r4 = r18 + 1;
        r0 = r98;
        r119 = r0.get(r4);
        r119 = (org.telegram.messenger.MessageObject) r119;
        r4 = r118.getId();
        r0 = r161;
        r6 = r0.startLoadFromMessageId;
        if (r4 < r6) goto L_0x00df;
    L_0x00ca:
        r4 = r119.getId();
        r0 = r161;
        r6 = r0.startLoadFromMessageId;
        if (r4 >= r6) goto L_0x00df;
    L_0x00d4:
        r4 = r118.getId();
        r0 = r161;
        r0.startLoadFromMessageId = r4;
        r68 = 1;
        goto L_0x00a6;
    L_0x00df:
        r18 = r18 + 1;
        goto L_0x0088;
    L_0x00e2:
        r0 = r161;
        r0 = r0.startLoadFromMessageId;
        r142 = r0;
        r0 = r161;
        r0 = r0.needSelectFromMessageId;
        r109 = r0;
        r0 = r161;
        r0 = r0.createUnreadMessageAfterId;
        r145 = r0;
        r0 = r161;
        r0 = r0.createUnreadMessageAfterIdLoading;
        r44 = r0;
        r161.clearChatData();
        r0 = r145;
        r1 = r161;
        r1.createUnreadMessageAfterId = r0;
        r0 = r142;
        r1 = r161;
        r1.startLoadFromMessageId = r0;
        r0 = r109;
        r1 = r161;
        r1.needSelectFromMessageId = r0;
    L_0x010f:
        r0 = r161;
        r4 = r0.loadsCount;
        r4 = r4 + 1;
        r0 = r161;
        r0.loadsCount = r4;
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x02a7;
    L_0x012a:
        r90 = 0;
    L_0x012c:
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r43 = r4.intValue();
        r4 = 3;
        r4 = r164[r4];
        r4 = (java.lang.Boolean) r4;
        r82 = r4.booleanValue();
        r4 = 4;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r67 = r4.intValue();
        r4 = 7;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r89 = r4.intValue();
        r4 = 8;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r91 = r4.intValue();
        r4 = 12;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r92 = r4.intValue();
        r4 = 13;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r93 = r4.intValue();
        if (r93 >= 0) goto L_0x02ab;
    L_0x0170:
        r93 = r93 * -1;
        r4 = 0;
        r0 = r161;
        r0.hasAllMentionsLocal = r4;
    L_0x0177:
        r4 = 4;
        r0 = r91;
        if (r0 != r4) goto L_0x01b6;
    L_0x017c:
        r0 = r92;
        r1 = r161;
        r1.startLoadFromMessageId = r0;
        r4 = r98.size();
        r18 = r4 + -1;
    L_0x0188:
        if (r18 <= 0) goto L_0x01b6;
    L_0x018a:
        r0 = r98;
        r1 = r18;
        r118 = r0.get(r1);
        r118 = (org.telegram.messenger.MessageObject) r118;
        r0 = r118;
        r4 = r0.type;
        if (r4 >= 0) goto L_0x02b8;
    L_0x019a:
        r4 = r118.getId();
        r0 = r161;
        r6 = r0.startLoadFromMessageId;
        if (r4 != r6) goto L_0x02b8;
    L_0x01a4:
        r4 = r18 + -1;
        r0 = r98;
        r4 = r0.get(r4);
        r4 = (org.telegram.messenger.MessageObject) r4;
        r4 = r4.getId();
        r0 = r161;
        r0.startLoadFromMessageId = r4;
    L_0x01b6:
        r146 = 0;
        if (r67 == 0) goto L_0x02cd;
    L_0x01ba:
        r4 = 5;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r4 = r4.intValue();
        r0 = r161;
        r0.last_message_id = r4;
        r4 = 3;
        r0 = r91;
        if (r0 != r4) goto L_0x02bc;
    L_0x01cc:
        r0 = r161;
        r4 = r0.loadingFromOldPosition;
        if (r4 == 0) goto L_0x01e8;
    L_0x01d2:
        r4 = 6;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r146 = r4.intValue();
        if (r146 == 0) goto L_0x01e3;
    L_0x01dd:
        r0 = r67;
        r1 = r161;
        r1.createUnreadMessageAfterId = r0;
    L_0x01e3:
        r4 = 0;
        r0 = r161;
        r0.loadingFromOldPosition = r4;
    L_0x01e8:
        r4 = 0;
        r0 = r161;
        r0.first_unread_id = r4;
    L_0x01ed:
        r115 = 0;
        if (r91 == 0) goto L_0x0204;
    L_0x01f1:
        r0 = r161;
        r4 = r0.startLoadFromMessageId;
        if (r4 != 0) goto L_0x01fd;
    L_0x01f7:
        r0 = r161;
        r4 = r0.last_message_id;
        if (r4 == 0) goto L_0x0204;
    L_0x01fd:
        r0 = r161;
        r4 = r0.forwardEndReached;
        r6 = 0;
        r4[r90] = r6;
    L_0x0204:
        r4 = 1;
        r0 = r91;
        if (r0 == r4) goto L_0x020e;
    L_0x0209:
        r4 = 3;
        r0 = r91;
        if (r0 != r4) goto L_0x0232;
    L_0x020e:
        r4 = 1;
        r0 = r90;
        if (r0 != r4) goto L_0x0232;
    L_0x0213:
        r0 = r161;
        r4 = r0.endReached;
        r6 = 0;
        r0 = r161;
        r7 = r0.cacheEndReached;
        r8 = 0;
        r9 = 1;
        r7[r8] = r9;
        r4[r6] = r9;
        r0 = r161;
        r4 = r0.forwardEndReached;
        r6 = 0;
        r7 = 0;
        r4[r6] = r7;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = 0;
        r7 = 0;
        r4[r6] = r7;
    L_0x0232:
        r0 = r161;
        r4 = r0.loadsCount;
        r6 = 1;
        if (r4 != r6) goto L_0x024b;
    L_0x0239:
        r4 = r98.size();
        r6 = 20;
        if (r4 <= r6) goto L_0x024b;
    L_0x0241:
        r0 = r161;
        r4 = r0.loadsCount;
        r4 = r4 + 1;
        r0 = r161;
        r0.loadsCount = r4;
    L_0x024b:
        r0 = r161;
        r4 = r0.firstLoading;
        if (r4 == 0) goto L_0x030d;
    L_0x0251:
        r0 = r161;
        r4 = r0.forwardEndReached;
        r4 = r4[r90];
        if (r4 != 0) goto L_0x02fe;
    L_0x0259:
        r0 = r161;
        r4 = r0.messages;
        r4.clear();
        r0 = r161;
        r4 = r0.messagesByDays;
        r4.clear();
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r4.clear();
        r18 = 0;
    L_0x0270:
        r4 = 2;
        r0 = r18;
        if (r0 >= r4) goto L_0x02fe;
    L_0x0275:
        r0 = r161;
        r4 = r0.messagesDict;
        r4 = r4[r18];
        r4.clear();
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x02ec;
    L_0x0284:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r4[r18] = r6;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4[r18] = r6;
    L_0x0295:
        r0 = r161;
        r4 = r0.maxDate;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4[r18] = r6;
        r0 = r161;
        r4 = r0.minDate;
        r6 = 0;
        r4[r18] = r6;
        r18 = r18 + 1;
        goto L_0x0270;
    L_0x02a7:
        r90 = 1;
        goto L_0x012c;
    L_0x02ab:
        r0 = r161;
        r4 = r0.first;
        if (r4 == 0) goto L_0x0177;
    L_0x02b1:
        r4 = 1;
        r0 = r161;
        r0.hasAllMentionsLocal = r4;
        goto L_0x0177;
    L_0x02b8:
        r18 = r18 + -1;
        goto L_0x0188;
    L_0x02bc:
        r0 = r67;
        r1 = r161;
        r1.first_unread_id = r0;
        r4 = 6;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r146 = r4.intValue();
        goto L_0x01ed;
    L_0x02cd:
        r0 = r161;
        r4 = r0.startLoadFromMessageId;
        if (r4 == 0) goto L_0x01ed;
    L_0x02d3:
        r4 = 3;
        r0 = r91;
        if (r0 == r4) goto L_0x02dd;
    L_0x02d8:
        r4 = 4;
        r0 = r91;
        if (r0 != r4) goto L_0x01ed;
    L_0x02dd:
        r4 = 5;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r4 = r4.intValue();
        r0 = r161;
        r0.last_message_id = r4;
        goto L_0x01ed;
    L_0x02ec:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4[r18] = r6;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r4[r18] = r6;
        goto L_0x0295;
    L_0x02fe:
        r4 = 0;
        r0 = r161;
        r0.firstLoading = r4;
        r4 = new org.telegram.ui.ChatActivity$$Lambda$38;
        r0 = r161;
        r4.<init>(r0);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
    L_0x030d:
        r4 = 1;
        r0 = r91;
        if (r0 != r4) goto L_0x0315;
    L_0x0312:
        java.util.Collections.reverse(r98);
    L_0x0315:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x032c;
    L_0x031b:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DataQuery.getInstance(r4);
        r0 = r161;
        r6 = r0.dialog_id;
        r0 = r98;
        r4.loadReplyMessagesForMessages(r0, r6);
    L_0x032c:
        r19 = 0;
        r4 = 2;
        r0 = r91;
        if (r0 != r4) goto L_0x0343;
    L_0x0333:
        r4 = r98.isEmpty();
        if (r4 == 0) goto L_0x0343;
    L_0x0339:
        if (r82 != 0) goto L_0x0343;
    L_0x033b:
        r0 = r161;
        r4 = r0.forwardEndReached;
        r6 = 0;
        r7 = 1;
        r4[r6] = r7;
    L_0x0343:
        r112 = 0;
        r35 = 0;
        r96 = org.telegram.messenger.MediaController.getInstance();
        r18 = 0;
    L_0x034d:
        r4 = r98.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x08a5;
    L_0x0355:
        r0 = r98;
        r1 = r18;
        r118 = r0.get(r1);
        r118 = (org.telegram.messenger.MessageObject) r118;
        r4 = r118.getApproximateHeight();
        r19 = r19 + r4;
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x0395;
    L_0x036b:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.self;
        if (r4 == 0) goto L_0x037a;
    L_0x0373:
        r0 = r118;
        r4 = r0.messageOwner;
        r6 = 1;
        r4.out = r6;
    L_0x037a:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.bot;
        if (r4 == 0) goto L_0x0388;
    L_0x0382:
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x0392;
    L_0x0388:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.id;
        r0 = r50;
        if (r4 != r0) goto L_0x0395;
    L_0x0392:
        r118.setIsRead();
    L_0x0395:
        r0 = r161;
        r4 = r0.messagesDict;
        r4 = r4[r90];
        r6 = r118.getId();
        r4 = r4.indexOfKey(r6);
        if (r4 < 0) goto L_0x03a8;
    L_0x03a5:
        r18 = r18 + 1;
        goto L_0x034d;
    L_0x03a8:
        r4 = r161.isSecretChat();
        if (r4 == 0) goto L_0x03b5;
    L_0x03ae:
        r0 = r161;
        r1 = r118;
        r0.checkSecretMessageForLocation(r1);
    L_0x03b5:
        r0 = r96;
        r1 = r118;
        r4 = r0.isPlayingMessage(r1);
        if (r4 == 0) goto L_0x03db;
    L_0x03bf:
        r128 = r96.getPlayingMessageObject();
        r0 = r128;
        r4 = r0.audioProgress;
        r0 = r118;
        r0.audioProgress = r4;
        r0 = r128;
        r4 = r0.audioProgressSec;
        r0 = r118;
        r0.audioProgressSec = r4;
        r0 = r128;
        r4 = r0.audioPlayerDuration;
        r0 = r118;
        r0.audioPlayerDuration = r4;
    L_0x03db:
        if (r90 != 0) goto L_0x03fc;
    L_0x03dd:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x03fc;
    L_0x03e7:
        r4 = r118.getId();
        r6 = 1;
        if (r4 != r6) goto L_0x03fc;
    L_0x03ee:
        r0 = r161;
        r4 = r0.endReached;
        r6 = 1;
        r4[r90] = r6;
        r0 = r161;
        r4 = r0.cacheEndReached;
        r6 = 1;
        r4[r90] = r6;
    L_0x03fc:
        r4 = r118.getId();
        if (r4 <= 0) goto L_0x074e;
    L_0x0402:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = r118.getId();
        r0 = r161;
        r7 = r0.maxMessageId;
        r7 = r7[r90];
        r6 = java.lang.Math.min(r6, r7);
        r4[r90] = r6;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = r118.getId();
        r0 = r161;
        r7 = r0.minMessageId;
        r7 = r7[r90];
        r6 = java.lang.Math.max(r6, r7);
        r4[r90] = r6;
    L_0x042a:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.date;
        if (r4 == 0) goto L_0x046a;
    L_0x0432:
        r0 = r161;
        r4 = r0.maxDate;
        r0 = r161;
        r6 = r0.maxDate;
        r6 = r6[r90];
        r0 = r118;
        r7 = r0.messageOwner;
        r7 = r7.date;
        r6 = java.lang.Math.max(r6, r7);
        r4[r90] = r6;
        r0 = r161;
        r4 = r0.minDate;
        r4 = r4[r90];
        if (r4 == 0) goto L_0x045e;
    L_0x0450:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.date;
        r0 = r161;
        r6 = r0.minDate;
        r6 = r6[r90];
        if (r4 >= r6) goto L_0x046a;
    L_0x045e:
        r0 = r161;
        r4 = r0.minDate;
        r0 = r118;
        r6 = r0.messageOwner;
        r6 = r6.date;
        r4[r90] = r6;
    L_0x046a:
        r4 = r118.getId();
        r0 = r161;
        r6 = r0.last_message_id;
        if (r4 != r6) goto L_0x047b;
    L_0x0474:
        r0 = r161;
        r4 = r0.forwardEndReached;
        r6 = 1;
        r4[r90] = r6;
    L_0x047b:
        r0 = r118;
        r4 = r0.type;
        if (r4 < 0) goto L_0x03a5;
    L_0x0481:
        r4 = 1;
        r0 = r90;
        if (r0 != r4) goto L_0x0490;
    L_0x0486:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo;
        if (r4 != 0) goto L_0x03a5;
    L_0x0490:
        r0 = r161;
        r4 = r0.needAnimateToMessage;
        if (r4 == 0) goto L_0x04c5;
    L_0x0496:
        r0 = r161;
        r4 = r0.needAnimateToMessage;
        r4 = r4.getId();
        r6 = r118.getId();
        if (r4 != r6) goto L_0x04c5;
    L_0x04a4:
        r4 = r118.getId();
        if (r4 >= 0) goto L_0x04c5;
    L_0x04aa:
        r0 = r118;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x04c5;
    L_0x04b1:
        r0 = r161;
        r0 = r0.needAnimateToMessage;
        r118 = r0;
        r0 = r161;
        r4 = r0.animatingMessageObjects;
        r0 = r118;
        r4.add(r0);
        r4 = 0;
        r0 = r161;
        r0.needAnimateToMessage = r4;
    L_0x04c5:
        r0 = r161;
        r4 = r0.messagesDict;
        r4 = r4[r90];
        r6 = r118.getId();
        r0 = r118;
        r4.put(r6, r0);
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r118;
        r6 = r0.dateKey;
        r55 = r4.get(r6);
        r55 = (java.util.ArrayList) r55;
        if (r55 != 0) goto L_0x0548;
    L_0x04e4:
        r55 = new java.util.ArrayList;
        r55.<init>();
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r118;
        r6 = r0.dateKey;
        r0 = r55;
        r4.put(r6, r0);
        r52 = new org.telegram.tgnet.TLRPC$TL_message;
        r52.<init>();
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.date;
        r6 = (long) r4;
        r4 = org.telegram.messenger.LocaleController.formatDateChat(r6);
        r0 = r52;
        r0.message = r4;
        r4 = 0;
        r0 = r52;
        r0.id = r4;
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.date;
        r0 = r52;
        r0.date = r4;
        r53 = new org.telegram.messenger.MessageObject;
        r0 = r161;
        r4 = r0.currentAccount;
        r6 = 0;
        r0 = r53;
        r1 = r52;
        r0.<init>(r4, r1, r6);
        r4 = 10;
        r0 = r53;
        r0.type = r4;
        r4 = 1;
        r0 = r53;
        r0.contentType = r4;
        r4 = 1;
        r0 = r53;
        r0.isDateObject = r4;
        r4 = 1;
        r0 = r91;
        if (r0 != r4) goto L_0x077e;
    L_0x053c:
        r0 = r161;
        r4 = r0.messages;
        r6 = 0;
        r0 = r53;
        r4.add(r6, r0);
    L_0x0546:
        r115 = r115 + 1;
    L_0x0548:
        r4 = r118.hasValidGroupId();
        if (r4 == 0) goto L_0x07e7;
    L_0x054e:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r6 = r118.getGroupIdForUse();
        r69 = r4.get(r6);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
        if (r69 == 0) goto L_0x05a5;
    L_0x055e:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.size();
        r6 = 1;
        if (r4 <= r6) goto L_0x05a5;
    L_0x0569:
        r4 = 1;
        r0 = r91;
        if (r0 != r4) goto L_0x0789;
    L_0x056e:
        r0 = r161;
        r4 = r0.messages;
        r6 = 0;
        r132 = r4.get(r6);
        r132 = (org.telegram.messenger.MessageObject) r132;
    L_0x0579:
        r6 = r132.getGroupIdForUse();
        r8 = r118.getGroupIdForUse();
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x079f;
    L_0x0585:
        r0 = r132;
        r6 = r0.localGroupId;
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x05a5;
    L_0x058f:
        r0 = r132;
        r6 = r0.localGroupId;
        r0 = r118;
        r0.localGroupId = r6;
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r0 = r132;
        r6 = r0.localGroupId;
        r69 = r4.get(r6);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
    L_0x05a5:
        if (r69 != 0) goto L_0x07b9;
    L_0x05a7:
        r69 = new org.telegram.messenger.MessageObject$GroupedMessages;
        r69.<init>();
        r6 = r118.getGroupId();
        r0 = r69;
        r0.groupId = r6;
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r0 = r69;
        r6 = r0.groupId;
        r0 = r69;
        r4.put(r6, r0);
    L_0x05c1:
        if (r112 != 0) goto L_0x05c8;
    L_0x05c3:
        r112 = new android.util.LongSparseArray;
        r112.<init>();
    L_0x05c8:
        r0 = r69;
        r6 = r0.groupId;
        r0 = r112;
        r1 = r69;
        r0.put(r6, r1);
        r4 = 1;
        r0 = r91;
        if (r0 != r4) goto L_0x07db;
    L_0x05d8:
        r0 = r69;
        r4 = r0.messages;
        r0 = r118;
        r4.add(r0);
    L_0x05e1:
        r115 = r115 + 1;
        r0 = r55;
        r1 = r118;
        r0.add(r1);
        r4 = 1;
        r0 = r91;
        if (r0 != r4) goto L_0x0801;
    L_0x05ef:
        r0 = r161;
        r4 = r0.messages;
        r6 = 0;
        r0 = r118;
        r4.add(r6, r0);
    L_0x05f9:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x081a;
    L_0x05ff:
        r0 = r161;
        r4 = r0.createUnreadMessageAfterId;
        if (r4 == 0) goto L_0x0816;
    L_0x0605:
        r4 = 1;
        r0 = r91;
        if (r0 == r4) goto L_0x0816;
    L_0x060a:
        r4 = r18 + 1;
        r6 = r98.size();
        if (r4 >= r6) goto L_0x0816;
    L_0x0612:
        r4 = r18 + 1;
        r0 = r98;
        r131 = r0.get(r4);
        r131 = (org.telegram.messenger.MessageObject) r131;
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x062c;
    L_0x0622:
        r4 = r131.getId();
        r0 = r161;
        r6 = r0.createUnreadMessageAfterId;
        if (r4 < r6) goto L_0x062e;
    L_0x062c:
        r131 = 0;
    L_0x062e:
        r4 = 2;
        r0 = r91;
        if (r0 != r4) goto L_0x084b;
    L_0x0633:
        r4 = r118.getId();
        r0 = r161;
        r6 = r0.first_unread_id;
        if (r4 != r6) goto L_0x084b;
    L_0x063d:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.y;
        r4 = r4 / 2;
        r0 = r19;
        if (r0 > r4) goto L_0x0650;
    L_0x0647:
        r0 = r161;
        r4 = r0.forwardEndReached;
        r6 = 0;
        r4 = r4[r6];
        if (r4 != 0) goto L_0x06a2;
    L_0x0650:
        r52 = new org.telegram.tgnet.TLRPC$TL_message;
        r52.<init>();
        r4 = "";
        r0 = r52;
        r0.message = r4;
        r4 = 0;
        r0 = r52;
        r0.id = r4;
        r53 = new org.telegram.messenger.MessageObject;
        r0 = r161;
        r4 = r0.currentAccount;
        r6 = 0;
        r0 = r53;
        r1 = r52;
        r0.<init>(r4, r1, r6);
        r4 = 6;
        r0 = r53;
        r0.type = r4;
        r4 = 2;
        r0 = r53;
        r0.contentType = r4;
        r0 = r161;
        r4 = r0.messages;
        r0 = r161;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -1;
        r0 = r53;
        r4.add(r6, r0);
        r0 = r53;
        r1 = r161;
        r1.unreadMessageObject = r0;
        r0 = r161;
        r4 = r0.unreadMessageObject;
        r0 = r161;
        r0.scrollToMessage = r4;
        r4 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
        r0 = r161;
        r0.scrollToMessagePosition = r4;
        r115 = r115 + 1;
    L_0x06a2:
        r4 = 2;
        r0 = r91;
        if (r0 == r4) goto L_0x03a5;
    L_0x06a7:
        r0 = r161;
        r4 = r0.unreadMessageObject;
        if (r4 != 0) goto L_0x03a5;
    L_0x06ad:
        r0 = r161;
        r4 = r0.createUnreadMessageAfterId;
        if (r4 == 0) goto L_0x03a5;
    L_0x06b3:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x06c9;
    L_0x06b9:
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x06c9;
    L_0x06bf:
        r4 = r118.getId();
        r0 = r161;
        r6 = r0.createUnreadMessageAfterId;
        if (r4 >= r6) goto L_0x06df;
    L_0x06c9:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x03a5;
    L_0x06cf:
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x03a5;
    L_0x06d5:
        r4 = r118.getId();
        r0 = r161;
        r6 = r0.createUnreadMessageAfterId;
        if (r4 > r6) goto L_0x03a5;
    L_0x06df:
        r4 = 1;
        r0 = r91;
        if (r0 == r4) goto L_0x06f4;
    L_0x06e4:
        if (r131 != 0) goto L_0x06f4;
    L_0x06e6:
        if (r131 != 0) goto L_0x03a5;
    L_0x06e8:
        if (r44 == 0) goto L_0x03a5;
    L_0x06ea:
        r4 = r98.size();
        r4 = r4 + -1;
        r0 = r18;
        if (r0 != r4) goto L_0x03a5;
    L_0x06f4:
        r52 = new org.telegram.tgnet.TLRPC$TL_message;
        r52.<init>();
        r4 = "";
        r0 = r52;
        r0.message = r4;
        r4 = 0;
        r0 = r52;
        r0.id = r4;
        r53 = new org.telegram.messenger.MessageObject;
        r0 = r161;
        r4 = r0.currentAccount;
        r6 = 0;
        r0 = r53;
        r1 = r52;
        r0.<init>(r4, r1, r6);
        r4 = 6;
        r0 = r53;
        r0.type = r4;
        r4 = 2;
        r0 = r53;
        r0.contentType = r4;
        r4 = 1;
        r0 = r91;
        if (r0 != r4) goto L_0x0890;
    L_0x0722:
        r0 = r161;
        r4 = r0.messages;
        r6 = 1;
        r0 = r53;
        r4.add(r6, r0);
    L_0x072c:
        r0 = r53;
        r1 = r161;
        r1.unreadMessageObject = r0;
        r4 = 3;
        r0 = r91;
        if (r0 != r4) goto L_0x074a;
    L_0x0737:
        r0 = r161;
        r4 = r0.unreadMessageObject;
        r0 = r161;
        r0.scrollToMessage = r4;
        r4 = 0;
        r0 = r161;
        r0.startLoadFromMessageId = r4;
        r4 = -9000; // 0xffffffffffffdcd8 float:NaN double:NaN;
        r0 = r161;
        r0.scrollToMessagePosition = r4;
    L_0x074a:
        r115 = r115 + 1;
        goto L_0x03a5;
    L_0x074e:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x042a;
    L_0x0754:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = r118.getId();
        r0 = r161;
        r7 = r0.maxMessageId;
        r7 = r7[r90];
        r6 = java.lang.Math.max(r6, r7);
        r4[r90] = r6;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = r118.getId();
        r0 = r161;
        r7 = r0.minMessageId;
        r7 = r7[r90];
        r6 = java.lang.Math.min(r6, r7);
        r4[r90] = r6;
        goto L_0x042a;
    L_0x077e:
        r0 = r161;
        r4 = r0.messages;
        r0 = r53;
        r4.add(r0);
        goto L_0x0546;
    L_0x0789:
        r0 = r161;
        r4 = r0.messages;
        r0 = r161;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -2;
        r132 = r4.get(r6);
        r132 = (org.telegram.messenger.MessageObject) r132;
        goto L_0x0579;
    L_0x079f:
        r6 = r132.getGroupIdForUse();
        r8 = r118.getGroupIdForUse();
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x05a5;
    L_0x07ab:
        r4 = org.telegram.messenger.Utilities.random;
        r6 = r4.nextLong();
        r0 = r118;
        r0.localGroupId = r6;
        r69 = 0;
        goto L_0x05a5;
    L_0x07b9:
        if (r112 == 0) goto L_0x07c7;
    L_0x07bb:
        r6 = r118.getGroupId();
        r0 = r112;
        r4 = r0.indexOfKey(r6);
        if (r4 >= 0) goto L_0x05c1;
    L_0x07c7:
        if (r35 != 0) goto L_0x07ce;
    L_0x07c9:
        r35 = new android.util.LongSparseArray;
        r35.<init>();
    L_0x07ce:
        r6 = r118.getGroupId();
        r0 = r35;
        r1 = r69;
        r0.put(r6, r1);
        goto L_0x05c1;
    L_0x07db:
        r0 = r69;
        r4 = r0.messages;
        r6 = 0;
        r0 = r118;
        r4.add(r6, r0);
        goto L_0x05e1;
    L_0x07e7:
        r6 = r118.getGroupIdForUse();
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x05e1;
    L_0x07f1:
        r0 = r118;
        r4 = r0.messageOwner;
        r6 = 0;
        r4.grouped_id = r6;
        r6 = 0;
        r0 = r118;
        r0.localSentGroupId = r6;
        goto L_0x05e1;
    L_0x0801:
        r0 = r161;
        r4 = r0.messages;
        r0 = r161;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -1;
        r0 = r118;
        r4.add(r6, r0);
        goto L_0x05f9;
    L_0x0816:
        r131 = 0;
        goto L_0x062e;
    L_0x081a:
        r0 = r161;
        r4 = r0.createUnreadMessageAfterId;
        if (r4 == 0) goto L_0x0847;
    L_0x0820:
        r4 = 1;
        r0 = r91;
        if (r0 == r4) goto L_0x0847;
    L_0x0825:
        r4 = r18 + -1;
        if (r4 < 0) goto L_0x0847;
    L_0x0829:
        r4 = r18 + -1;
        r0 = r98;
        r131 = r0.get(r4);
        r131 = (org.telegram.messenger.MessageObject) r131;
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x0843;
    L_0x0839:
        r4 = r131.getId();
        r0 = r161;
        r6 = r0.createUnreadMessageAfterId;
        if (r4 < r6) goto L_0x062e;
    L_0x0843:
        r131 = 0;
        goto L_0x062e;
    L_0x0847:
        r131 = 0;
        goto L_0x062e;
    L_0x084b:
        r4 = 3;
        r0 = r91;
        if (r0 == r4) goto L_0x0855;
    L_0x0850:
        r4 = 4;
        r0 = r91;
        if (r0 != r4) goto L_0x06a2;
    L_0x0855:
        r4 = r118.getId();
        r0 = r161;
        r6 = r0.startLoadFromMessageId;
        if (r4 != r6) goto L_0x06a2;
    L_0x085f:
        r0 = r161;
        r4 = r0.needSelectFromMessageId;
        if (r4 == 0) goto L_0x0888;
    L_0x0865:
        r4 = r118.getId();
        r0 = r161;
        r0.highlightMessageId = r4;
    L_0x086d:
        r0 = r118;
        r1 = r161;
        r1.scrollToMessage = r0;
        r4 = 0;
        r0 = r161;
        r0.startLoadFromMessageId = r4;
        r0 = r161;
        r4 = r0.scrollToMessagePosition;
        r6 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
        if (r4 != r6) goto L_0x06a2;
    L_0x0880:
        r4 = -9000; // 0xffffffffffffdcd8 float:NaN double:NaN;
        r0 = r161;
        r0.scrollToMessagePosition = r4;
        goto L_0x06a2;
    L_0x0888:
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r0 = r161;
        r0.highlightMessageId = r4;
        goto L_0x086d;
    L_0x0890:
        r0 = r161;
        r4 = r0.messages;
        r0 = r161;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -1;
        r0 = r53;
        r4.add(r6, r0);
        goto L_0x072c;
    L_0x08a5:
        if (r44 == 0) goto L_0x08ac;
    L_0x08a7:
        r4 = 0;
        r0 = r161;
        r0.createUnreadMessageAfterId = r4;
    L_0x08ac:
        if (r91 != 0) goto L_0x08ba;
    L_0x08ae:
        if (r115 != 0) goto L_0x08ba;
    L_0x08b0:
        r0 = r161;
        r4 = r0.loadsCount;
        r4 = r4 + -1;
        r0 = r161;
        r0.loadsCount = r4;
    L_0x08ba:
        if (r112 == 0) goto L_0x0927;
    L_0x08bc:
        r18 = 0;
    L_0x08be:
        r4 = r112.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x0927;
    L_0x08c6:
        r0 = r112;
        r1 = r18;
        r69 = r0.valueAt(r1);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
        r69.calculate();
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x0924;
    L_0x08d9:
        if (r35 == 0) goto L_0x0924;
    L_0x08db:
        r0 = r112;
        r1 = r18;
        r6 = r0.keyAt(r1);
        r0 = r35;
        r4 = r0.indexOfKey(r6);
        if (r4 < 0) goto L_0x0924;
    L_0x08eb:
        r0 = r69;
        r4 = r0.messages;
        r0 = r69;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -1;
        r102 = r4.get(r6);
        r102 = (org.telegram.messenger.MessageObject) r102;
        r0 = r161;
        r4 = r0.messages;
        r0 = r102;
        r76 = r4.indexOf(r0);
        if (r76 < 0) goto L_0x0924;
    L_0x090b:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.messagesStartRow;
        r6 = r6 + r76;
        r0 = r69;
        r7 = r0.messages;
        r7 = r7.size();
        r4.notifyItemRangeChanged(r6, r7);
    L_0x0924:
        r18 = r18 + 1;
        goto L_0x08be;
    L_0x0927:
        r0 = r161;
        r4 = r0.forwardEndReached;
        r4 = r4[r90];
        if (r4 == 0) goto L_0x0943;
    L_0x092f:
        r4 = 1;
        r0 = r90;
        if (r0 == r4) goto L_0x0943;
    L_0x0934:
        r4 = 0;
        r0 = r161;
        r0.first_unread_id = r4;
        r4 = 0;
        r0 = r161;
        r0.last_message_id = r4;
        r4 = 0;
        r0 = r161;
        r0.createUnreadMessageAfterId = r4;
    L_0x0943:
        r4 = 1;
        r0 = r91;
        if (r0 != r4) goto L_0x0a66;
    L_0x0948:
        r138 = 0;
        r4 = r98.size();
        r0 = r43;
        if (r4 == r0) goto L_0x0993;
    L_0x0952:
        if (r82 == 0) goto L_0x0962;
    L_0x0954:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x0962;
    L_0x095a:
        r0 = r161;
        r4 = r0.forwardEndReached;
        r4 = r4[r90];
        if (r4 == 0) goto L_0x0993;
    L_0x0962:
        r0 = r161;
        r4 = r0.forwardEndReached;
        r6 = 1;
        r4[r90] = r6;
        r4 = 1;
        r0 = r90;
        if (r0 == r4) goto L_0x098e;
    L_0x096e:
        r4 = 0;
        r0 = r161;
        r0.first_unread_id = r4;
        r4 = 0;
        r0 = r161;
        r0.last_message_id = r4;
        r4 = 0;
        r0 = r161;
        r0.createUnreadMessageAfterId = r4;
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.loadingDownRow;
        r4.notifyItemRemoved(r6);
        r138 = r138 + 1;
    L_0x098e:
        r4 = 0;
        r0 = r161;
        r0.startLoadFromMessageId = r4;
    L_0x0993:
        if (r115 <= 0) goto L_0x09cb;
    L_0x0995:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r63 = r4.findFirstVisibleItemPosition();
        if (r63 != 0) goto L_0x09a1;
    L_0x099f:
        r63 = r63 + 1;
    L_0x09a1:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r0 = r63;
        r66 = r4.findViewByPosition(r0);
        if (r66 != 0) goto L_0x0a4d;
    L_0x09ad:
        r143 = 0;
    L_0x09af:
        r0 = r161;
        r4 = r0.chatAdapter;
        r6 = 1;
        r0 = r115;
        r4.notifyItemRangeInserted(r6, r0);
        r4 = -1;
        r0 = r63;
        if (r0 == r4) goto L_0x09cb;
    L_0x09be:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r6 = r63 + r115;
        r6 = r6 - r138;
        r0 = r143;
        r4.scrollToPositionWithOffset(r6, r0);
    L_0x09cb:
        r4 = 0;
        r0 = r161;
        r0.loadingForward = r4;
    L_0x09d0:
        r0 = r161;
        r4 = r0.first;
        if (r4 == 0) goto L_0x09e5;
    L_0x09d6:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.size();
        if (r4 <= 0) goto L_0x09e5;
    L_0x09e0:
        r4 = 0;
        r0 = r161;
        r0.first = r4;
    L_0x09e5:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x0a13;
    L_0x09ef:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x0a13;
    L_0x09f5:
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x0a13;
    L_0x09fb:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.bot;
        if (r4 == 0) goto L_0x0a13;
    L_0x0a03:
        r0 = r161;
        r4 = r0.botUser;
        if (r4 != 0) goto L_0x0a13;
    L_0x0a09:
        r4 = "";
        r0 = r161;
        r0.botUser = r4;
        r161.updateBottomOverlay();
    L_0x0a13:
        if (r115 != 0) goto L_0x0d53;
    L_0x0a15:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x0d53;
    L_0x0a1b:
        r0 = r161;
        r4 = r0.endReached;
        r6 = 0;
        r4 = r4[r6];
        if (r4 != 0) goto L_0x0d53;
    L_0x0a24:
        r4 = 1;
        r0 = r161;
        r0.first = r4;
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x0a37;
    L_0x0a2f:
        r0 = r161;
        r4 = r0.chatListView;
        r6 = 0;
        r4.setEmptyView(r6);
    L_0x0a37:
        r0 = r161;
        r4 = r0.emptyViewContainer;
        if (r4 == 0) goto L_0x0a45;
    L_0x0a3d:
        r0 = r161;
        r4 = r0.emptyViewContainer;
        r6 = 4;
        r4.setVisibility(r6);
    L_0x0a45:
        r4 = 0;
        r0 = r161;
        r0.checkScrollForLoad(r4);
        goto L_0x0067;
    L_0x0a4d:
        r0 = r161;
        r4 = r0.chatListView;
        r4 = r4.getMeasuredHeight();
        r6 = r66.getBottom();
        r4 = r4 - r6;
        r0 = r161;
        r6 = r0.chatListView;
        r6 = r6.getPaddingBottom();
        r143 = r4 - r6;
        goto L_0x09af;
    L_0x0a66:
        r4 = r98.size();
        r0 = r43;
        if (r4 >= r0) goto L_0x0a99;
    L_0x0a6e:
        r4 = 3;
        r0 = r91;
        if (r0 == r4) goto L_0x0a99;
    L_0x0a73:
        r4 = 4;
        r0 = r91;
        if (r0 == r4) goto L_0x0a99;
    L_0x0a78:
        if (r82 == 0) goto L_0x0bf7;
    L_0x0a7a:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x0a86;
    L_0x0a80:
        r0 = r161;
        r4 = r0.isBroadcast;
        if (r4 == 0) goto L_0x0a8d;
    L_0x0a86:
        r0 = r161;
        r4 = r0.endReached;
        r6 = 1;
        r4[r90] = r6;
    L_0x0a8d:
        r4 = 2;
        r0 = r91;
        if (r0 == r4) goto L_0x0a99;
    L_0x0a92:
        r0 = r161;
        r4 = r0.cacheEndReached;
        r6 = 1;
        r4[r90] = r6;
    L_0x0a99:
        r4 = 0;
        r0 = r161;
        r0.loading = r4;
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x0d41;
    L_0x0aa4:
        r0 = r161;
        r4 = r0.first;
        if (r4 != 0) goto L_0x0ab6;
    L_0x0aaa:
        r0 = r161;
        r4 = r0.scrollToTopOnResume;
        if (r4 != 0) goto L_0x0ab6;
    L_0x0ab0:
        r0 = r161;
        r4 = r0.forceScrollToTop;
        if (r4 == 0) goto L_0x0c6f;
    L_0x0ab6:
        r4 = 0;
        r0 = r161;
        r0.forceScrollToTop = r4;
        r0 = r161;
        r4 = r0.chatAdapter;
        r4.notifyDataSetChanged();
        r0 = r161;
        r4 = r0.scrollToMessage;
        if (r4 == 0) goto L_0x0c6a;
    L_0x0ac8:
        r28 = 1;
        r0 = r161;
        r4 = r0.startLoadFromMessageOffset;
        r6 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r4 == r6) goto L_0x0c15;
    L_0x0ad3:
        r0 = r161;
        r4 = r0.startLoadFromMessageOffset;
        r4 = -r4;
        r0 = r161;
        r6 = r0.chatListView;
        r6 = r6.getPaddingBottom();
        r160 = r4 - r6;
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r0 = r161;
        r0.startLoadFromMessageOffset = r4;
    L_0x0ae9:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x0b36;
    L_0x0af3:
        r0 = r161;
        r4 = r0.messages;
        r0 = r161;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -1;
        r4 = r4.get(r6);
        r0 = r161;
        r6 = r0.scrollToMessage;
        if (r4 == r6) goto L_0x0b23;
    L_0x0b0b:
        r0 = r161;
        r4 = r0.messages;
        r0 = r161;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -2;
        r4 = r4.get(r6);
        r0 = r161;
        r6 = r0.scrollToMessage;
        if (r4 != r6) goto L_0x0c48;
    L_0x0b23:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.loadingUpRow;
        r0 = r160;
        r1 = r28;
        r4.scrollToPositionWithOffset(r6, r0, r1);
    L_0x0b36:
        r0 = r161;
        r4 = r0.chatListView;
        r4.invalidate();
        r0 = r161;
        r4 = r0.scrollToMessagePosition;
        r6 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
        if (r4 == r6) goto L_0x0b4d;
    L_0x0b45:
        r0 = r161;
        r4 = r0.scrollToMessagePosition;
        r6 = -9000; // 0xffffffffffffdcd8 float:NaN double:NaN;
        if (r4 != r6) goto L_0x0b8e;
    L_0x0b4d:
        r4 = 1;
        r6 = 1;
        r0 = r161;
        r0.showPagedownButton(r4, r6);
        if (r146 == 0) goto L_0x0b8e;
    L_0x0b56:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = 0;
        r4.setVisibility(r6);
        r0 = r161;
        r4 = r0.prevSetUnreadCount;
        r0 = r161;
        r6 = r0.newUnreadMessageCount;
        if (r4 == r6) goto L_0x0b8e;
    L_0x0b68:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = "%d";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r146;
        r1 = r161;
        r1.newUnreadMessageCount = r0;
        r9 = java.lang.Integer.valueOf(r146);
        r7[r8] = r9;
        r6 = java.lang.String.format(r6, r7);
        r4.setText(r6);
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        r0 = r161;
        r0.prevSetUnreadCount = r4;
    L_0x0b8e:
        r4 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
        r0 = r161;
        r0.scrollToMessagePosition = r4;
        r4 = 0;
        r0 = r161;
        r0.scrollToMessage = r4;
    L_0x0b99:
        if (r93 == 0) goto L_0x0bc8;
    L_0x0b9b:
        r4 = 1;
        r6 = 1;
        r0 = r161;
        r0.showMentiondownButton(r4, r6);
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        r6 = 0;
        r4.setVisibility(r6);
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        r6 = "%d";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r93;
        r1 = r161;
        r1.newMentionsCount = r0;
        r9 = java.lang.Integer.valueOf(r93);
        r7[r8] = r9;
        r6 = java.lang.String.format(r6, r7);
        r4.setText(r6);
    L_0x0bc8:
        r0 = r161;
        r4 = r0.paused;
        if (r4 == 0) goto L_0x0bde;
    L_0x0bce:
        r4 = 1;
        r0 = r161;
        r0.scrollToTopOnResume = r4;
        r0 = r161;
        r4 = r0.scrollToMessage;
        if (r4 == 0) goto L_0x0bde;
    L_0x0bd9:
        r4 = 1;
        r0 = r161;
        r0.scrollToTopUnReadOnResume = r4;
    L_0x0bde:
        r0 = r161;
        r4 = r0.first;
        if (r4 == 0) goto L_0x09d0;
    L_0x0be4:
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x09d0;
    L_0x0bea:
        r0 = r161;
        r4 = r0.chatListView;
        r0 = r161;
        r6 = r0.emptyViewContainer;
        r4.setEmptyView(r6);
        goto L_0x09d0;
    L_0x0bf7:
        r4 = 2;
        r0 = r91;
        if (r0 != r4) goto L_0x0c0c;
    L_0x0bfc:
        r4 = r98.size();
        if (r4 != 0) goto L_0x0a99;
    L_0x0c02:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x0a99;
    L_0x0c0c:
        r0 = r161;
        r4 = r0.endReached;
        r6 = 1;
        r4[r90] = r6;
        goto L_0x0a99;
    L_0x0c15:
        r0 = r161;
        r4 = r0.scrollToMessagePosition;
        r6 = -9000; // 0xffffffffffffdcd8 float:NaN double:NaN;
        if (r4 != r6) goto L_0x0c2b;
    L_0x0c1d:
        r0 = r161;
        r4 = r0.scrollToMessage;
        r0 = r161;
        r160 = r0.getScrollOffsetForMessage(r4);
        r28 = 0;
        goto L_0x0ae9;
    L_0x0c2b:
        r0 = r161;
        r4 = r0.scrollToMessagePosition;
        r6 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
        if (r4 != r6) goto L_0x0c40;
    L_0x0c33:
        r4 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = -r4;
        r160 = r0;
        r28 = 0;
        goto L_0x0ae9;
    L_0x0c40:
        r0 = r161;
        r0 = r0.scrollToMessagePosition;
        r160 = r0;
        goto L_0x0ae9;
    L_0x0c48:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.messagesStartRow;
        r0 = r161;
        r7 = r0.messages;
        r0 = r161;
        r8 = r0.scrollToMessage;
        r7 = r7.indexOf(r8);
        r6 = r6 + r7;
        r0 = r160;
        r1 = r28;
        r4.scrollToPositionWithOffset(r6, r0, r1);
        goto L_0x0b36;
    L_0x0c6a:
        r161.moveScrollToLastMessage();
        goto L_0x0b99;
    L_0x0c6f:
        if (r115 == 0) goto L_0x0d17;
    L_0x0c71:
        r61 = 0;
        r0 = r161;
        r4 = r0.endReached;
        r4 = r4[r90];
        if (r4 == 0) goto L_0x0ca7;
    L_0x0c7b:
        if (r90 != 0) goto L_0x0c87;
    L_0x0c7d:
        r0 = r161;
        r6 = r0.mergeDialogId;
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x0c8c;
    L_0x0c87:
        r4 = 1;
        r0 = r90;
        if (r0 != r4) goto L_0x0ca7;
    L_0x0c8c:
        r61 = 1;
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.loadingUpRow;
        r6 = r6 + -1;
        r7 = 2;
        r4.notifyItemRangeChanged(r6, r7);
        r0 = r161;
        r4 = r0.chatAdapter;
        r4.updateRows();
    L_0x0ca7:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r63 = r4.findFirstVisibleItemPosition();
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r0 = r63;
        r66 = r4.findViewByPosition(r0);
        if (r66 != 0) goto L_0x0cfb;
    L_0x0cbb:
        r143 = 0;
    L_0x0cbd:
        if (r61 == 0) goto L_0x0d13;
    L_0x0cbf:
        r4 = 1;
    L_0x0cc0:
        r4 = r115 - r4;
        if (r4 <= 0) goto L_0x0ce9;
    L_0x0cc4:
        r0 = r161;
        r4 = r0.chatAdapter;
        r81 = r4.messagesEndRow;
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.loadingUpRow;
        r4.notifyItemChanged(r6);
        r0 = r161;
        r6 = r0.chatAdapter;
        if (r61 == 0) goto L_0x0d15;
    L_0x0ce1:
        r4 = 1;
    L_0x0ce2:
        r4 = r115 - r4;
        r0 = r81;
        r6.notifyItemRangeInserted(r0, r4);
    L_0x0ce9:
        r4 = -1;
        r0 = r63;
        if (r0 == r4) goto L_0x0bc8;
    L_0x0cee:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r0 = r63;
        r1 = r143;
        r4.scrollToPositionWithOffset(r0, r1);
        goto L_0x0bc8;
    L_0x0cfb:
        r0 = r161;
        r4 = r0.chatListView;
        r4 = r4.getMeasuredHeight();
        r6 = r66.getBottom();
        r4 = r4 - r6;
        r0 = r161;
        r6 = r0.chatListView;
        r6 = r6.getPaddingBottom();
        r143 = r4 - r6;
        goto L_0x0cbd;
    L_0x0d13:
        r4 = 0;
        goto L_0x0cc0;
    L_0x0d15:
        r4 = 0;
        goto L_0x0ce2;
    L_0x0d17:
        r0 = r161;
        r4 = r0.endReached;
        r4 = r4[r90];
        if (r4 == 0) goto L_0x0bc8;
    L_0x0d1f:
        if (r90 != 0) goto L_0x0d2b;
    L_0x0d21:
        r0 = r161;
        r6 = r0.mergeDialogId;
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x0d30;
    L_0x0d2b:
        r4 = 1;
        r0 = r90;
        if (r0 != r4) goto L_0x0bc8;
    L_0x0d30:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.loadingUpRow;
        r4.notifyItemRemoved(r6);
        goto L_0x0bc8;
    L_0x0d41:
        r4 = 1;
        r0 = r161;
        r0.scrollToTopOnResume = r4;
        r0 = r161;
        r4 = r0.scrollToMessage;
        if (r4 == 0) goto L_0x09d0;
    L_0x0d4c:
        r4 = 1;
        r0 = r161;
        r0.scrollToTopUnReadOnResume = r4;
        goto L_0x09d0;
    L_0x0d53:
        r0 = r161;
        r4 = r0.progressView;
        if (r4 == 0) goto L_0x0a45;
    L_0x0d59:
        r0 = r161;
        r4 = r0.progressView;
        r6 = 4;
        r4.setVisibility(r6);
        goto L_0x0a45;
    L_0x0d63:
        r4 = org.telegram.messenger.NotificationCenter.emojiDidLoaded;
        r0 = r162;
        if (r0 != r4) goto L_0x0dac;
    L_0x0d69:
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x0d76;
    L_0x0d6f:
        r0 = r161;
        r4 = r0.chatListView;
        r4.invalidateViews();
    L_0x0d76:
        r0 = r161;
        r4 = r0.replyObjectTextView;
        if (r4 == 0) goto L_0x0d83;
    L_0x0d7c:
        r0 = r161;
        r4 = r0.replyObjectTextView;
        r4.invalidate();
    L_0x0d83:
        r0 = r161;
        r4 = r0.alertTextView;
        if (r4 == 0) goto L_0x0d90;
    L_0x0d89:
        r0 = r161;
        r4 = r0.alertTextView;
        r4.invalidate();
    L_0x0d90:
        r0 = r161;
        r4 = r0.pinnedMessageTextView;
        if (r4 == 0) goto L_0x0d9d;
    L_0x0d96:
        r0 = r161;
        r4 = r0.pinnedMessageTextView;
        r4.invalidate();
    L_0x0d9d:
        r0 = r161;
        r4 = r0.mentionListView;
        if (r4 == 0) goto L_0x0067;
    L_0x0da3:
        r0 = r161;
        r4 = r0.mentionListView;
        r4.invalidateViews();
        goto L_0x0067;
    L_0x0dac:
        r4 = org.telegram.messenger.NotificationCenter.updateInterfaces;
        r0 = r162;
        if (r0 != r4) goto L_0x0ea9;
    L_0x0db2:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r149 = r4.intValue();
        r4 = r149 & 1;
        if (r4 != 0) goto L_0x0dc3;
    L_0x0dbf:
        r4 = r149 & 16;
        if (r4 == 0) goto L_0x0dea;
    L_0x0dc3:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x0e83;
    L_0x0dc9:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.currentChat;
        r6 = r6.id;
        r6 = java.lang.Integer.valueOf(r6);
        r39 = r4.getChat(r6);
        if (r39 == 0) goto L_0x0de7;
    L_0x0de1:
        r0 = r39;
        r1 = r161;
        r1.currentChat = r0;
    L_0x0de7:
        r161.updateTitle();
    L_0x0dea:
        r150 = 0;
        r4 = r149 & 32;
        if (r4 != 0) goto L_0x0df4;
    L_0x0df0:
        r4 = r149 & 4;
        if (r4 == 0) goto L_0x0e09;
    L_0x0df4:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x0e07;
    L_0x0dfa:
        r0 = r161;
        r4 = r0.avatarContainer;
        if (r4 == 0) goto L_0x0e07;
    L_0x0e00:
        r0 = r161;
        r4 = r0.avatarContainer;
        r4.updateOnlineCount();
    L_0x0e07:
        r150 = 1;
    L_0x0e09:
        r4 = r149 & 2;
        if (r4 != 0) goto L_0x0e15;
    L_0x0e0d:
        r4 = r149 & 8;
        if (r4 != 0) goto L_0x0e15;
    L_0x0e11:
        r4 = r149 & 1;
        if (r4 == 0) goto L_0x0e1b;
    L_0x0e15:
        r161.checkAndUpdateAvatar();
        r161.updateVisibleRows();
    L_0x0e1b:
        r4 = r149 & 64;
        if (r4 == 0) goto L_0x0e21;
    L_0x0e1f:
        r150 = 1;
    L_0x0e21:
        r0 = r149;
        r4 = r0 & 8192;
        if (r4 == 0) goto L_0x0e69;
    L_0x0e27:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x0e69;
    L_0x0e31:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.currentChat;
        r6 = r6.id;
        r6 = java.lang.Integer.valueOf(r6);
        r39 = r4.getChat(r6);
        if (r39 == 0) goto L_0x0067;
    L_0x0e49:
        r0 = r39;
        r1 = r161;
        r1.currentChat = r0;
        r150 = 1;
        r161.updateBottomOverlay();
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x0e69;
    L_0x0e5a:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r0 = r161;
        r6 = r0.dialog_id;
        r0 = r161;
        r8 = r0.currentAccount;
        r4.setDialogId(r6, r8);
    L_0x0e69:
        r0 = r161;
        r4 = r0.avatarContainer;
        if (r4 == 0) goto L_0x0e78;
    L_0x0e6f:
        if (r150 == 0) goto L_0x0e78;
    L_0x0e71:
        r0 = r161;
        r4 = r0.avatarContainer;
        r4.updateSubtitle();
    L_0x0e78:
        r0 = r149;
        r4 = r0 & 128;
        if (r4 == 0) goto L_0x0067;
    L_0x0e7e:
        r161.updateContactStatus();
        goto L_0x0067;
    L_0x0e83:
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x0de7;
    L_0x0e89:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.currentUser;
        r6 = r6.id;
        r6 = java.lang.Integer.valueOf(r6);
        r155 = r4.getUser(r6);
        if (r155 == 0) goto L_0x0de7;
    L_0x0ea1:
        r0 = r155;
        r1 = r161;
        r1.currentUser = r0;
        goto L_0x0de7;
    L_0x0ea9:
        r4 = org.telegram.messenger.NotificationCenter.didReceivedNewMessages;
        r0 = r162;
        if (r0 != r4) goto L_0x1aac;
    L_0x0eaf:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x0ec0:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.UserConfig.getInstance(r4);
        r50 = r4.getClientUserId();
        r147 = 0;
        r72 = 0;
        r4 = 1;
        r21 = r164[r4];
        r21 = (java.util.ArrayList) r21;
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x0f92;
    L_0x0edb:
        r4 = r21.size();
        r6 = 1;
        if (r4 != r6) goto L_0x0f92;
    L_0x0ee2:
        r4 = 0;
        r0 = r21;
        r118 = r0.get(r4);
        r118 = (org.telegram.messenger.MessageObject) r118;
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x0f92;
    L_0x0ef1:
        r4 = r118.isOut();
        if (r4 == 0) goto L_0x0f92;
    L_0x0ef7:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        if (r4 == 0) goto L_0x0f92;
    L_0x0eff:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
        if (r4 == 0) goto L_0x0f92;
    L_0x0f09:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4.encryptedAction;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
        if (r4 == 0) goto L_0x0f92;
    L_0x0f15:
        r4 = r161.getParentActivity();
        if (r4 == 0) goto L_0x0f92;
    L_0x0f1b:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        r4 = r4.layer;
        r4 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r4);
        r6 = 17;
        if (r4 >= r6) goto L_0x0f92;
    L_0x0f29:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        r4 = r4.ttl;
        if (r4 <= 0) goto L_0x0f92;
    L_0x0f31:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        r4 = r4.ttl;
        r6 = 60;
        if (r4 > r6) goto L_0x0f92;
    L_0x0f3b:
        r29 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
        r4 = r161.getParentActivity();
        r0 = r29;
        r0.<init>(r4);
        r4 = "AppName";
        r6 = 2131492999; // 0x7f0c0087 float:1.8609466E38 double:1.053097465E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r0 = r29;
        r0.setTitle(r4);
        r4 = "OK";
        r6 = 2131494093; // 0x7f0c04cd float:1.8611685E38 double:1.0530980057E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r6 = 0;
        r0 = r29;
        r0.setPositiveButton(r4, r6);
        r4 = "CompatibilityChat";
        r6 = 2131493306; // 0x7f0c01ba float:1.8610088E38 double:1.053097617E-314;
        r7 = 2;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r161;
        r9 = r0.currentUser;
        r9 = r9.first_name;
        r7[r8] = r9;
        r8 = 1;
        r0 = r161;
        r9 = r0.currentUser;
        r9 = r9.first_name;
        r7[r8] = r9;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r6, r7);
        r0 = r29;
        r0.setMessage(r4);
        r4 = r29.create();
        r0 = r161;
        r0.showDialog(r4);
    L_0x0f92:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 != 0) goto L_0x0fa2;
    L_0x0f98:
        r0 = r161;
        r6 = r0.inlineReturn;
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x1118;
    L_0x0fa2:
        r117 = 0;
        r18 = 0;
    L_0x0fa6:
        r4 = r21.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x1118;
    L_0x0fae:
        r0 = r21;
        r1 = r18;
        r102 = r0.get(r1);
        r102 = (org.telegram.messenger.MessageObject) r102;
        if (r117 != 0) goto L_0x0fce;
    L_0x0fba:
        r4 = r102.isOut();
        if (r4 == 0) goto L_0x0fce;
    L_0x0fc0:
        r117 = 1;
        r4 = org.telegram.messenger.NotificationCenter.getGlobalInstance();
        r6 = org.telegram.messenger.NotificationCenter.closeSearchByActiveAction;
        r7 = 0;
        r7 = new java.lang.Object[r7];
        r4.postNotificationName(r6, r7);
    L_0x0fce:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x10b5;
    L_0x0fd4:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
        if (r4 == 0) goto L_0x0fea;
    L_0x0fde:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4.user_id;
        r0 = r50;
        if (r4 == r0) goto L_0x1006;
    L_0x0fea:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser;
        if (r4 == 0) goto L_0x103b;
    L_0x0ff4:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4.users;
        r6 = java.lang.Integer.valueOf(r50);
        r4 = r4.contains(r6);
        if (r4 == 0) goto L_0x103b;
    L_0x1006:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.currentChat;
        r6 = r6.id;
        r6 = java.lang.Integer.valueOf(r6);
        r110 = r4.getChat(r6);
        if (r110 == 0) goto L_0x1037;
    L_0x101e:
        r0 = r110;
        r1 = r161;
        r1.currentChat = r0;
        r161.checkActionBarMenu();
        r161.updateBottomOverlay();
        r0 = r161;
        r4 = r0.avatarContainer;
        if (r4 == 0) goto L_0x1037;
    L_0x1030:
        r0 = r161;
        r4 = r0.avatarContainer;
        r4.updateSubtitle();
    L_0x1037:
        r18 = r18 + 1;
        goto L_0x0fa6;
    L_0x103b:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.reply_to_msg_id;
        if (r4 == 0) goto L_0x1037;
    L_0x1043:
        r0 = r102;
        r4 = r0.replyMessageObject;
        if (r4 != 0) goto L_0x1037;
    L_0x1049:
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r0 = r102;
        r6 = r0.messageOwner;
        r6 = r6.reply_to_msg_id;
        r4 = r4.get(r6);
        r4 = (org.telegram.messenger.MessageObject) r4;
        r0 = r102;
        r0.replyMessageObject = r4;
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPinMessage;
        if (r4 == 0) goto L_0x1093;
    L_0x106a:
        r4 = 0;
        r6 = 0;
        r0 = r102;
        r0.generatePinMessageText(r4, r6);
    L_0x1071:
        r4 = r102.isMegagroup();
        if (r4 == 0) goto L_0x1037;
    L_0x1077:
        r0 = r102;
        r4 = r0.replyMessageObject;
        if (r4 == 0) goto L_0x1037;
    L_0x107d:
        r0 = r102;
        r4 = r0.replyMessageObject;
        r4 = r4.messageOwner;
        if (r4 == 0) goto L_0x1037;
    L_0x1085:
        r0 = r102;
        r4 = r0.replyMessageObject;
        r4 = r4.messageOwner;
        r6 = r4.flags;
        r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r6 = r6 | r7;
        r4.flags = r6;
        goto L_0x1037;
    L_0x1093:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGameScore;
        if (r4 == 0) goto L_0x10a4;
    L_0x109d:
        r4 = 0;
        r0 = r102;
        r0.generateGameMessageText(r4);
        goto L_0x1071;
    L_0x10a4:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPaymentSent;
        if (r4 == 0) goto L_0x1071;
    L_0x10ae:
        r4 = 0;
        r0 = r102;
        r0.generatePaymentSentMessageText(r4);
        goto L_0x1071;
    L_0x10b5:
        r0 = r161;
        r6 = r0.inlineReturn;
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x1037;
    L_0x10bf:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.reply_markup;
        if (r4 == 0) goto L_0x1037;
    L_0x10c7:
        r26 = 0;
    L_0x10c9:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.reply_markup;
        r4 = r4.rows;
        r4 = r4.size();
        r0 = r26;
        if (r0 >= r4) goto L_0x1037;
    L_0x10d9:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.reply_markup;
        r4 = r4.rows;
        r0 = r26;
        r137 = r4.get(r0);
        r137 = (org.telegram.tgnet.TLRPC.TL_keyboardButtonRow) r137;
        r32 = 0;
    L_0x10eb:
        r0 = r137;
        r4 = r0.buttons;
        r4 = r4.size();
        r0 = r32;
        if (r0 >= r4) goto L_0x1112;
    L_0x10f7:
        r0 = r137;
        r4 = r0.buttons;
        r0 = r32;
        r31 = r4.get(r0);
        r31 = (org.telegram.tgnet.TLRPC.KeyboardButton) r31;
        r0 = r31;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonSwitchInline;
        if (r4 == 0) goto L_0x1115;
    L_0x1109:
        r31 = (org.telegram.tgnet.TLRPC.TL_keyboardButtonSwitchInline) r31;
        r0 = r161;
        r1 = r31;
        r0.processSwitchButton(r1);
    L_0x1112:
        r26 = r26 + 1;
        goto L_0x10c9;
    L_0x1115:
        r32 = r32 + 1;
        goto L_0x10eb;
    L_0x1118:
        r135 = 0;
        r0 = r161;
        r4 = r0.forwardEndReached;
        r6 = 0;
        r4 = r4[r6];
        if (r4 != 0) goto L_0x13b2;
    L_0x1123:
        r46 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r48 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x1130;
    L_0x112d:
        r48 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
    L_0x1130:
        r18 = 0;
    L_0x1132:
        r4 = r21.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x12f5;
    L_0x113a:
        r0 = r21;
        r1 = r18;
        r118 = r0.get(r1);
        r118 = (org.telegram.messenger.MessageObject) r118;
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x1165;
    L_0x114a:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.bot;
        if (r4 == 0) goto L_0x1158;
    L_0x1152:
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x1162;
    L_0x1158:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.id;
        r0 = r50;
        if (r4 != r0) goto L_0x1165;
    L_0x1162:
        r118.setIsRead();
    L_0x1165:
        r0 = r161;
        r4 = r0.avatarContainer;
        if (r4 == 0) goto L_0x11a2;
    L_0x116b:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x11a2;
    L_0x1171:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        if (r4 == 0) goto L_0x11a2;
    L_0x1179:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
        if (r4 == 0) goto L_0x11a2;
    L_0x1183:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4.encryptedAction;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
        if (r4 == 0) goto L_0x11a2;
    L_0x118f:
        r0 = r161;
        r6 = r0.avatarContainer;
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4.encryptedAction;
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL) r4;
        r4 = r4.ttl_seconds;
        r6.setTime(r4);
    L_0x11a2:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo;
        if (r4 == 0) goto L_0x1206;
    L_0x11ac:
        r30 = new android.os.Bundle;
        r30.<init>();
        r4 = "chat_id";
        r0 = r118;
        r6 = r0.messageOwner;
        r6 = r6.action;
        r6 = r6.channel_id;
        r0 = r30;
        r0.putInt(r4, r6);
        r0 = r161;
        r4 = r0.parentLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        if (r4 <= 0) goto L_0x1203;
    L_0x11cd:
        r0 = r161;
        r4 = r0.parentLayout;
        r4 = r4.fragmentsStack;
        r0 = r161;
        r6 = r0.parentLayout;
        r6 = r6.fragmentsStack;
        r6 = r6.size();
        r6 = r6 + -1;
        r4 = r4.get(r6);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r85 = r4;
    L_0x11e7:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r0 = r4.channel_id;
        r38 = r0;
        r4 = new org.telegram.ui.ChatActivity$$Lambda$39;
        r0 = r161;
        r1 = r85;
        r2 = r30;
        r3 = r38;
        r4.<init>(r0, r1, r2, r3);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
        goto L_0x0067;
    L_0x1203:
        r85 = 0;
        goto L_0x11e7;
    L_0x1206:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x122a;
    L_0x120c:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.megagroup;
        if (r4 == 0) goto L_0x122a;
    L_0x1214:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser;
        if (r4 != 0) goto L_0x1228;
    L_0x121e:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
        if (r4 == 0) goto L_0x122a;
    L_0x1228:
        r135 = 1;
    L_0x122a:
        if (r18 != 0) goto L_0x1241;
    L_0x122c:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.id;
        if (r4 >= 0) goto L_0x1241;
    L_0x1234:
        r0 = r118;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x1241;
    L_0x123b:
        r0 = r118;
        r1 = r161;
        r1.needAnimateToMessage = r0;
    L_0x1241:
        r4 = r118.isOut();
        if (r4 == 0) goto L_0x1255;
    L_0x1247:
        r4 = r118.isSending();
        if (r4 == 0) goto L_0x1255;
    L_0x124d:
        r4 = 0;
        r0 = r161;
        r0.scrollToLastMessage(r4);
        goto L_0x0067;
    L_0x1255:
        r0 = r118;
        r4 = r0.type;
        if (r4 < 0) goto L_0x126c;
    L_0x125b:
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r118.getId();
        r4 = r4.indexOfKey(r6);
        if (r4 < 0) goto L_0x1270;
    L_0x126c:
        r18 = r18 + 1;
        goto L_0x1132;
    L_0x1270:
        r118.checkLayout();
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.date;
        r0 = r46;
        r46 = java.lang.Math.max(r0, r4);
        r4 = r118.getId();
        if (r4 <= 0) goto L_0x12d4;
    L_0x1285:
        r4 = r118.getId();
        r0 = r48;
        r48 = java.lang.Math.max(r4, r0);
        r0 = r161;
        r4 = r0.last_message_id;
        r6 = r118.getId();
        r4 = java.lang.Math.max(r4, r6);
        r0 = r161;
        r0.last_message_id = r4;
    L_0x129f:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.mentioned;
        if (r4 == 0) goto L_0x12b7;
    L_0x12a7:
        r4 = r118.isContentUnread();
        if (r4 == 0) goto L_0x12b7;
    L_0x12ad:
        r0 = r161;
        r4 = r0.newMentionsCount;
        r4 = r4 + 1;
        r0 = r161;
        r0.newMentionsCount = r4;
    L_0x12b7:
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        r4 = r4 + 1;
        r0 = r161;
        r0.newUnreadMessageCount = r4;
        r0 = r118;
        r4 = r0.type;
        r6 = 10;
        if (r4 == r6) goto L_0x12d1;
    L_0x12c9:
        r0 = r118;
        r4 = r0.type;
        r6 = 11;
        if (r4 != r6) goto L_0x126c;
    L_0x12d1:
        r147 = 1;
        goto L_0x126c;
    L_0x12d4:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x129f;
    L_0x12da:
        r4 = r118.getId();
        r0 = r48;
        r48 = java.lang.Math.min(r4, r0);
        r0 = r161;
        r4 = r0.last_message_id;
        r6 = r118.getId();
        r4 = java.lang.Math.min(r4, r6);
        r0 = r161;
        r0.last_message_id = r4;
        goto L_0x129f;
    L_0x12f5:
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        if (r4 == 0) goto L_0x1337;
    L_0x12fb:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        if (r4 == 0) goto L_0x1337;
    L_0x1301:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = 0;
        r4.setVisibility(r6);
        r0 = r161;
        r4 = r0.prevSetUnreadCount;
        r0 = r161;
        r6 = r0.newUnreadMessageCount;
        if (r4 == r6) goto L_0x1337;
    L_0x1313:
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        r0 = r161;
        r0.prevSetUnreadCount = r4;
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = "%d";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r161;
        r9 = r0.newUnreadMessageCount;
        r9 = java.lang.Integer.valueOf(r9);
        r7[r8] = r9;
        r6 = java.lang.String.format(r6, r7);
        r4.setText(r6);
    L_0x1337:
        r0 = r161;
        r4 = r0.newMentionsCount;
        if (r4 == 0) goto L_0x136e;
    L_0x133d:
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        if (r4 == 0) goto L_0x136e;
    L_0x1343:
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        r6 = 0;
        r4.setVisibility(r6);
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        r6 = "%d";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r161;
        r9 = r0.newMentionsCount;
        r9 = java.lang.Integer.valueOf(r9);
        r7[r8] = r9;
        r6 = java.lang.String.format(r6, r7);
        r4.setText(r6);
        r4 = 1;
        r6 = 1;
        r0 = r161;
        r0.showMentiondownButton(r4, r6);
    L_0x136e:
        r161.updateVisibleRows();
    L_0x1371:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x1393;
    L_0x137b:
        r0 = r161;
        r4 = r0.botUser;
        if (r4 == 0) goto L_0x1393;
    L_0x1381:
        r0 = r161;
        r4 = r0.botUser;
        r4 = r4.length();
        if (r4 != 0) goto L_0x1393;
    L_0x138b:
        r4 = 0;
        r0 = r161;
        r0.botUser = r4;
        r161.updateBottomOverlay();
    L_0x1393:
        if (r147 == 0) goto L_0x139b;
    L_0x1395:
        r161.updateTitle();
        r161.checkAndUpdateAvatar();
    L_0x139b:
        if (r135 == 0) goto L_0x0067;
    L_0x139d:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.currentChat;
        r6 = r6.id;
        r7 = 0;
        r8 = 1;
        r4.loadFullChat(r6, r7, r8);
        goto L_0x0067;
    L_0x13b2:
        r112 = 0;
        r159 = 0;
        r4 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r4 == 0) goto L_0x13e4;
    L_0x13ba:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r6 = "received new messages ";
        r4 = r4.append(r6);
        r6 = r21.size();
        r4 = r4.append(r6);
        r6 = " in dialog ";
        r4 = r4.append(r6);
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = r4.append(r6);
        r4 = r4.toString();
        org.telegram.messenger.FileLog.d(r4);
    L_0x13e4:
        r18 = 0;
    L_0x13e6:
        r4 = r21.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x1933;
    L_0x13ee:
        r127 = -1;
        r0 = r21;
        r1 = r18;
        r118 = r0.get(r1);
        r118 = (org.telegram.messenger.MessageObject) r118;
        r4 = r161.isSecretChat();
        if (r4 == 0) goto L_0x1407;
    L_0x1400:
        r0 = r161;
        r1 = r118;
        r0.checkSecretMessageForLocation(r1);
    L_0x1407:
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x1428;
    L_0x140d:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.bot;
        if (r4 == 0) goto L_0x141b;
    L_0x1415:
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x1425;
    L_0x141b:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.id;
        r0 = r50;
        if (r4 != r0) goto L_0x1428;
    L_0x1425:
        r118.setIsRead();
    L_0x1428:
        r0 = r161;
        r4 = r0.avatarContainer;
        if (r4 == 0) goto L_0x1465;
    L_0x142e:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x1465;
    L_0x1434:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        if (r4 == 0) goto L_0x1465;
    L_0x143c:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageEncryptedAction;
        if (r4 == 0) goto L_0x1465;
    L_0x1446:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4.encryptedAction;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL;
        if (r4 == 0) goto L_0x1465;
    L_0x1452:
        r0 = r161;
        r6 = r0.avatarContainer;
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4.encryptedAction;
        r4 = (org.telegram.tgnet.TLRPC.TL_decryptedMessageActionSetMessageTTL) r4;
        r4 = r4.ttl_seconds;
        r6.setTime(r4);
    L_0x1465:
        r0 = r118;
        r4 = r0.type;
        if (r4 < 0) goto L_0x147c;
    L_0x146b:
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r118.getId();
        r4 = r4.indexOfKey(r6);
        if (r4 < 0) goto L_0x1480;
    L_0x147c:
        r18 = r18 + 1;
        goto L_0x13e6;
    L_0x1480:
        if (r18 != 0) goto L_0x149a;
    L_0x1482:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.id;
        if (r4 >= 0) goto L_0x149a;
    L_0x148a:
        r0 = r118;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x149a;
    L_0x1491:
        r0 = r161;
        r4 = r0.animatingMessageObjects;
        r0 = r118;
        r4.add(r0);
    L_0x149a:
        r4 = r118.hasValidGroupId();
        if (r4 == 0) goto L_0x1601;
    L_0x14a0:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r6 = r118.getGroupId();
        r69 = r4.get(r6);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
        if (r69 != 0) goto L_0x14ca;
    L_0x14b0:
        r69 = new org.telegram.messenger.MessageObject$GroupedMessages;
        r69.<init>();
        r6 = r118.getGroupId();
        r0 = r69;
        r0.groupId = r6;
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r0 = r69;
        r6 = r0.groupId;
        r0 = r69;
        r4.put(r6, r0);
    L_0x14ca:
        if (r112 != 0) goto L_0x14d1;
    L_0x14cc:
        r112 = new android.util.LongSparseArray;
        r112.<init>();
    L_0x14d1:
        r0 = r69;
        r6 = r0.groupId;
        r0 = r112;
        r1 = r69;
        r0.put(r6, r1);
        r0 = r69;
        r4 = r0.messages;
        r0 = r118;
        r4.add(r0);
    L_0x14e5:
        if (r69 == 0) goto L_0x1516;
    L_0x14e7:
        r0 = r69;
        r4 = r0.messages;
        r139 = r4.size();
        r4 = 1;
        r0 = r139;
        if (r0 <= r4) goto L_0x1605;
    L_0x14f4:
        r0 = r69;
        r4 = r0.messages;
        r0 = r69;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -2;
        r4 = r4.get(r6);
        r4 = (org.telegram.messenger.MessageObject) r4;
        r102 = r4;
    L_0x150a:
        if (r102 == 0) goto L_0x1516;
    L_0x150c:
        r0 = r161;
        r4 = r0.messages;
        r0 = r102;
        r127 = r4.indexOf(r0);
    L_0x1516:
        r4 = -1;
        r0 = r127;
        if (r0 != r4) goto L_0x152f;
    L_0x151b:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.id;
        if (r4 < 0) goto L_0x152d;
    L_0x1523:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x1609;
    L_0x152d:
        r127 = 0;
    L_0x152f:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x1583;
    L_0x1535:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r4 == 0) goto L_0x1583;
    L_0x153f:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_webPageUrlPending;
        if (r4 == 0) goto L_0x1583;
    L_0x154b:
        if (r159 != 0) goto L_0x1552;
    L_0x154d:
        r159 = new java.util.HashMap;
        r159.<init>();
    L_0x1552:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4.url;
        r0 = r159;
        r24 = r0.get(r4);
        r24 = (java.util.ArrayList) r24;
        if (r24 != 0) goto L_0x157c;
    L_0x1566:
        r24 = new java.util.ArrayList;
        r24.<init>();
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4.url;
        r0 = r159;
        r1 = r24;
        r0.put(r4, r1);
    L_0x157c:
        r0 = r24;
        r1 = r118;
        r0.add(r1);
    L_0x1583:
        r118.checkLayout();
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatMigrateTo;
        if (r4 == 0) goto L_0x16ca;
    L_0x1590:
        r30 = new android.os.Bundle;
        r30.<init>();
        r4 = "chat_id";
        r0 = r118;
        r6 = r0.messageOwner;
        r6 = r6.action;
        r6 = r6.channel_id;
        r0 = r30;
        r0.putInt(r4, r6);
        r0 = r161;
        r4 = r0.parentLayout;
        r4 = r4.fragmentsStack;
        r4 = r4.size();
        if (r4 <= 0) goto L_0x16c6;
    L_0x15b1:
        r0 = r161;
        r4 = r0.parentLayout;
        r4 = r4.fragmentsStack;
        r0 = r161;
        r6 = r0.parentLayout;
        r6 = r6.fragmentsStack;
        r6 = r6.size();
        r6 = r6 + -1;
        r4 = r4.get(r6);
        r4 = (org.telegram.ui.ActionBar.BaseFragment) r4;
        r85 = r4;
    L_0x15cb:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r0 = r4.channel_id;
        r38 = r0;
        r4 = new org.telegram.ui.ChatActivity$$Lambda$40;
        r0 = r161;
        r1 = r85;
        r2 = r30;
        r3 = r38;
        r4.<init>(r0, r1, r2, r3);
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);
        if (r112 == 0) goto L_0x0067;
    L_0x15e7:
        r26 = 0;
    L_0x15e9:
        r4 = r112.size();
        r0 = r26;
        if (r0 >= r4) goto L_0x0067;
    L_0x15f1:
        r0 = r112;
        r1 = r26;
        r4 = r0.valueAt(r1);
        r4 = (org.telegram.messenger.MessageObject.GroupedMessages) r4;
        r4.calculate();
        r26 = r26 + 1;
        goto L_0x15e9;
    L_0x1601:
        r69 = 0;
        goto L_0x14e5;
    L_0x1605:
        r102 = 0;
        goto L_0x150a;
    L_0x1609:
        r0 = r161;
        r4 = r0.messages;
        r139 = r4.size();
        r26 = 0;
    L_0x1613:
        r0 = r26;
        r1 = r139;
        if (r0 >= r1) goto L_0x1689;
    L_0x1619:
        r0 = r161;
        r4 = r0.messages;
        r0 = r26;
        r87 = r4.get(r0);
        r87 = (org.telegram.messenger.MessageObject) r87;
        r0 = r87;
        r4 = r0.type;
        if (r4 < 0) goto L_0x16c2;
    L_0x162b:
        r0 = r87;
        r4 = r0.messageOwner;
        r4 = r4.date;
        if (r4 <= 0) goto L_0x16c2;
    L_0x1633:
        r0 = r87;
        r4 = r0.messageOwner;
        r4 = r4.id;
        if (r4 <= 0) goto L_0x1651;
    L_0x163b:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.id;
        if (r4 <= 0) goto L_0x1651;
    L_0x1643:
        r0 = r87;
        r4 = r0.messageOwner;
        r4 = r4.id;
        r0 = r118;
        r6 = r0.messageOwner;
        r6 = r6.id;
        if (r4 < r6) goto L_0x165f;
    L_0x1651:
        r0 = r87;
        r4 = r0.messageOwner;
        r4 = r4.date;
        r0 = r118;
        r6 = r0.messageOwner;
        r6 = r6.date;
        if (r4 >= r6) goto L_0x16c2;
    L_0x165f:
        r6 = r87.getGroupId();
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x16a4;
    L_0x1669:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r6 = r87.getGroupId();
        r86 = r4.get(r6);
        r86 = (org.telegram.messenger.MessageObject.GroupedMessages) r86;
        if (r86 == 0) goto L_0x1685;
    L_0x1679:
        r0 = r86;
        r4 = r0.messages;
        r4 = r4.size();
        if (r4 != 0) goto L_0x1685;
    L_0x1683:
        r86 = 0;
    L_0x1685:
        if (r86 != 0) goto L_0x16a7;
    L_0x1687:
        r127 = r26;
    L_0x1689:
        r4 = -1;
        r0 = r127;
        if (r0 == r4) goto L_0x169a;
    L_0x168e:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.size();
        r0 = r127;
        if (r0 <= r4) goto L_0x152f;
    L_0x169a:
        r0 = r161;
        r4 = r0.messages;
        r127 = r4.size();
        goto L_0x152f;
    L_0x16a4:
        r86 = 0;
        goto L_0x1685;
    L_0x16a7:
        r0 = r161;
        r4 = r0.messages;
        r0 = r86;
        r6 = r0.messages;
        r0 = r86;
        r7 = r0.messages;
        r7 = r7.size();
        r7 = r7 + -1;
        r6 = r6.get(r7);
        r127 = r4.indexOf(r6);
        goto L_0x1689;
    L_0x16c2:
        r26 = r26 + 1;
        goto L_0x1613;
    L_0x16c6:
        r85 = 0;
        goto L_0x15cb;
    L_0x16ca:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x16ee;
    L_0x16d0:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.megagroup;
        if (r4 == 0) goto L_0x16ee;
    L_0x16d8:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatAddUser;
        if (r4 != 0) goto L_0x16ec;
    L_0x16e2:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionChatDeleteUser;
        if (r4 == 0) goto L_0x16ee;
    L_0x16ec:
        r135 = 1;
    L_0x16ee:
        r0 = r161;
        r4 = r0.minDate;
        r6 = 0;
        r4 = r4[r6];
        if (r4 == 0) goto L_0x1706;
    L_0x16f7:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.date;
        r0 = r161;
        r6 = r0.minDate;
        r7 = 0;
        r6 = r6[r7];
        if (r4 >= r6) goto L_0x1713;
    L_0x1706:
        r0 = r161;
        r4 = r0.minDate;
        r6 = 0;
        r0 = r118;
        r7 = r0.messageOwner;
        r7 = r7.date;
        r4[r6] = r7;
    L_0x1713:
        r4 = r118.isOut();
        if (r4 == 0) goto L_0x1721;
    L_0x1719:
        r4 = 1;
        r0 = r161;
        r0.removeUnreadPlane(r4);
        r72 = 1;
    L_0x1721:
        r4 = r118.getId();
        if (r4 <= 0) goto L_0x18ff;
    L_0x1727:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = 0;
        r7 = r118.getId();
        r0 = r161;
        r8 = r0.maxMessageId;
        r9 = 0;
        r8 = r8[r9];
        r7 = java.lang.Math.min(r7, r8);
        r4[r6] = r7;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = 0;
        r7 = r118.getId();
        r0 = r161;
        r8 = r0.minMessageId;
        r9 = 0;
        r8 = r8[r9];
        r7 = java.lang.Math.max(r7, r8);
        r4[r6] = r7;
    L_0x1753:
        r0 = r161;
        r4 = r0.maxDate;
        r6 = 0;
        r0 = r161;
        r7 = r0.maxDate;
        r8 = 0;
        r7 = r7[r8];
        r0 = r118;
        r8 = r0.messageOwner;
        r8 = r8.date;
        r7 = java.lang.Math.max(r7, r8);
        r4[r6] = r7;
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r118.getId();
        r0 = r118;
        r4.put(r6, r0);
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r118;
        r6 = r0.dateKey;
        r55 = r4.get(r6);
        r55 = (java.util.ArrayList) r55;
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.size();
        r0 = r127;
        if (r0 <= r4) goto L_0x179d;
    L_0x1795:
        r0 = r161;
        r4 = r0.messages;
        r127 = r4.size();
    L_0x179d:
        if (r55 != 0) goto L_0x180c;
    L_0x179f:
        r55 = new java.util.ArrayList;
        r55.<init>();
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r118;
        r6 = r0.dateKey;
        r0 = r55;
        r4.put(r6, r0);
        r52 = new org.telegram.tgnet.TLRPC$TL_message;
        r52.<init>();
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.date;
        r6 = (long) r4;
        r4 = org.telegram.messenger.LocaleController.formatDateChat(r6);
        r0 = r52;
        r0.message = r4;
        r4 = 0;
        r0 = r52;
        r0.id = r4;
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.date;
        r0 = r52;
        r0.date = r4;
        r53 = new org.telegram.messenger.MessageObject;
        r0 = r161;
        r4 = r0.currentAccount;
        r6 = 0;
        r0 = r53;
        r1 = r52;
        r0.<init>(r4, r1, r6);
        r4 = 10;
        r0 = r53;
        r0.type = r4;
        r4 = 1;
        r0 = r53;
        r0.contentType = r4;
        r4 = 1;
        r0 = r53;
        r0.isDateObject = r4;
        r0 = r161;
        r4 = r0.messages;
        r0 = r127;
        r1 = r53;
        r4.add(r0, r1);
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x180c;
    L_0x1803:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r127;
        r4.notifyItemInserted(r0);
    L_0x180c:
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x1898;
    L_0x1812:
        r0 = r161;
        r4 = r0.paused;
        if (r4 == 0) goto L_0x1898;
    L_0x1818:
        if (r127 != 0) goto L_0x1898;
    L_0x181a:
        r0 = r161;
        r4 = r0.scrollToTopUnReadOnResume;
        if (r4 != 0) goto L_0x1838;
    L_0x1820:
        r0 = r161;
        r4 = r0.unreadMessageObject;
        if (r4 == 0) goto L_0x1838;
    L_0x1826:
        r0 = r161;
        r4 = r0.unreadMessageObject;
        r0 = r161;
        r0.removeMessageObject(r4);
        if (r127 <= 0) goto L_0x1833;
    L_0x1831:
        r127 = r127 + -1;
    L_0x1833:
        r4 = 0;
        r0 = r161;
        r0.unreadMessageObject = r4;
    L_0x1838:
        r0 = r161;
        r4 = r0.unreadMessageObject;
        if (r4 != 0) goto L_0x1898;
    L_0x183e:
        r52 = new org.telegram.tgnet.TLRPC$TL_message;
        r52.<init>();
        r4 = "";
        r0 = r52;
        r0.message = r4;
        r4 = 0;
        r0 = r52;
        r0.id = r4;
        r53 = new org.telegram.messenger.MessageObject;
        r0 = r161;
        r4 = r0.currentAccount;
        r6 = 0;
        r0 = r53;
        r1 = r52;
        r0.<init>(r4, r1, r6);
        r4 = 6;
        r0 = r53;
        r0.type = r4;
        r4 = 2;
        r0 = r53;
        r0.contentType = r4;
        r0 = r161;
        r4 = r0.messages;
        r6 = 0;
        r0 = r53;
        r4.add(r6, r0);
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x187f;
    L_0x1877:
        r0 = r161;
        r4 = r0.chatAdapter;
        r6 = 0;
        r4.notifyItemInserted(r6);
    L_0x187f:
        r0 = r53;
        r1 = r161;
        r1.unreadMessageObject = r0;
        r0 = r161;
        r4 = r0.unreadMessageObject;
        r0 = r161;
        r0.scrollToMessage = r4;
        r4 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
        r0 = r161;
        r0.scrollToMessagePosition = r4;
        r4 = 1;
        r0 = r161;
        r0.scrollToTopUnReadOnResume = r4;
    L_0x1898:
        r4 = 0;
        r0 = r55;
        r1 = r118;
        r0.add(r4, r1);
        r0 = r161;
        r4 = r0.messages;
        r0 = r127;
        r1 = r118;
        r4.add(r0, r1);
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x18c3;
    L_0x18b1:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r127;
        r4.notifyItemChanged(r0);
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r127;
        r4.notifyItemInserted(r0);
    L_0x18c3:
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x18e1;
    L_0x18c9:
        r0 = r118;
        r4 = r0.messageOwner;
        r4 = r4.mentioned;
        if (r4 == 0) goto L_0x18e1;
    L_0x18d1:
        r4 = r118.isContentUnread();
        if (r4 == 0) goto L_0x18e1;
    L_0x18d7:
        r0 = r161;
        r4 = r0.newMentionsCount;
        r4 = r4 + 1;
        r0 = r161;
        r0.newMentionsCount = r4;
    L_0x18e1:
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        r4 = r4 + 1;
        r0 = r161;
        r0.newUnreadMessageCount = r4;
        r0 = r118;
        r4 = r0.type;
        r6 = 10;
        if (r4 == r6) goto L_0x18fb;
    L_0x18f3:
        r0 = r118;
        r4 = r0.type;
        r6 = 11;
        if (r4 != r6) goto L_0x147c;
    L_0x18fb:
        r147 = 1;
        goto L_0x147c;
    L_0x18ff:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x1753;
    L_0x1905:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = 0;
        r7 = r118.getId();
        r0 = r161;
        r8 = r0.maxMessageId;
        r9 = 0;
        r8 = r8[r9];
        r7 = java.lang.Math.max(r7, r8);
        r4[r6] = r7;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = 0;
        r7 = r118.getId();
        r0 = r161;
        r8 = r0.minMessageId;
        r9 = 0;
        r8 = r8[r9];
        r7 = java.lang.Math.min(r7, r8);
        r4[r6] = r7;
        goto L_0x1753;
    L_0x1933:
        if (r159 == 0) goto L_0x1946;
    L_0x1935:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.dialog_id;
        r0 = r159;
        r4.reloadWebPages(r6, r0);
    L_0x1946:
        if (r112 == 0) goto L_0x19a3;
    L_0x1948:
        r18 = 0;
    L_0x194a:
        r4 = r112.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x19a3;
    L_0x1952:
        r0 = r112;
        r1 = r18;
        r69 = r0.valueAt(r1);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
        r0 = r69;
        r4 = r0.posArray;
        r121 = r4.size();
        r69.calculate();
        r0 = r69;
        r4 = r0.posArray;
        r111 = r4.size();
        r4 = r111 - r121;
        if (r4 <= 0) goto L_0x19a0;
    L_0x1973:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x19a0;
    L_0x1979:
        r0 = r161;
        r4 = r0.messages;
        r0 = r69;
        r6 = r0.messages;
        r0 = r69;
        r7 = r0.messages;
        r7 = r7.size();
        r7 = r7 + -1;
        r6 = r6.get(r7);
        r78 = r4.indexOf(r6);
        if (r78 < 0) goto L_0x19a0;
    L_0x1995:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r78;
        r1 = r111;
        r4.notifyItemRangeChanged(r0, r1);
    L_0x19a0:
        r18 = r18 + 1;
        goto L_0x194a;
    L_0x19a3:
        r0 = r161;
        r4 = r0.progressView;
        if (r4 == 0) goto L_0x19b1;
    L_0x19a9:
        r0 = r161;
        r4 = r0.progressView;
        r6 = 4;
        r4.setVisibility(r6);
    L_0x19b1:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 != 0) goto L_0x19bc;
    L_0x19b7:
        r4 = 1;
        r0 = r161;
        r0.scrollToTopOnResume = r4;
    L_0x19bc:
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x1aa5;
    L_0x19c2:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x1aa5;
    L_0x19c8:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r88 = r4.findFirstVisibleItemPosition();
        r4 = -1;
        r0 = r88;
        if (r0 != r4) goto L_0x19d7;
    L_0x19d5:
        r88 = 0;
    L_0x19d7:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r0 = r88;
        r42 = r4.findViewByPosition(r0);
        if (r42 == 0) goto L_0x1a4e;
    L_0x19e3:
        r4 = r42.getBottom();
        r0 = r161;
        r6 = r0.chatListView;
        r6 = r6.getMeasuredHeight();
        r58 = r4 - r6;
    L_0x19f1:
        if (r88 != 0) goto L_0x19fd;
    L_0x19f3:
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r58;
        if (r0 <= r4) goto L_0x19ff;
    L_0x19fd:
        if (r72 == 0) goto L_0x1a5a;
    L_0x19ff:
        r4 = 0;
        r0 = r161;
        r0.newUnreadMessageCount = r4;
        r0 = r161;
        r4 = r0.firstLoading;
        if (r4 != 0) goto L_0x1a15;
    L_0x1a0a:
        r0 = r161;
        r4 = r0.paused;
        if (r4 == 0) goto L_0x1a51;
    L_0x1a10:
        r4 = 1;
        r0 = r161;
        r0.scrollToTopOnResume = r4;
    L_0x1a15:
        r0 = r161;
        r4 = r0.newMentionsCount;
        if (r4 == 0) goto L_0x1371;
    L_0x1a1b:
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        if (r4 == 0) goto L_0x1371;
    L_0x1a21:
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        r6 = 0;
        r4.setVisibility(r6);
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        r6 = "%d";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r161;
        r9 = r0.newMentionsCount;
        r9 = java.lang.Integer.valueOf(r9);
        r7[r8] = r9;
        r6 = java.lang.String.format(r6, r7);
        r4.setText(r6);
        r4 = 1;
        r6 = 1;
        r0 = r161;
        r0.showMentiondownButton(r4, r6);
        goto L_0x1371;
    L_0x1a4e:
        r58 = 0;
        goto L_0x19f1;
    L_0x1a51:
        r4 = 1;
        r0 = r161;
        r0.forceScrollToTop = r4;
        r161.moveScrollToLastMessage();
        goto L_0x1a15;
    L_0x1a5a:
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        if (r4 == 0) goto L_0x1a9c;
    L_0x1a60:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        if (r4 == 0) goto L_0x1a9c;
    L_0x1a66:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = 0;
        r4.setVisibility(r6);
        r0 = r161;
        r4 = r0.prevSetUnreadCount;
        r0 = r161;
        r6 = r0.newUnreadMessageCount;
        if (r4 == r6) goto L_0x1a9c;
    L_0x1a78:
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        r0 = r161;
        r0.prevSetUnreadCount = r4;
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = "%d";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r161;
        r9 = r0.newUnreadMessageCount;
        r9 = java.lang.Integer.valueOf(r9);
        r7[r8] = r9;
        r6 = java.lang.String.format(r6, r7);
        r4.setText(r6);
    L_0x1a9c:
        r4 = 1;
        r6 = 1;
        r0 = r161;
        r0.showPagedownButton(r4, r6);
        goto L_0x1a15;
    L_0x1aa5:
        r4 = 1;
        r0 = r161;
        r0.scrollToTopOnResume = r4;
        goto L_0x1371;
    L_0x1aac:
        r4 = org.telegram.messenger.NotificationCenter.closeChats;
        r0 = r162;
        if (r0 != r4) goto L_0x1ad4;
    L_0x1ab2:
        if (r164 == 0) goto L_0x1acf;
    L_0x1ab4:
        r0 = r164;
        r4 = r0.length;
        if (r4 <= 0) goto L_0x1acf;
    L_0x1ab9:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x1aca:
        r161.finishFragment();
        goto L_0x0067;
    L_0x1acf:
        r161.removeSelfFromStack();
        goto L_0x0067;
    L_0x1ad4:
        r4 = org.telegram.messenger.NotificationCenter.messagesRead;
        r0 = r162;
        if (r0 != r4) goto L_0x1c3f;
    L_0x1ada:
        r4 = 0;
        r77 = r164[r4];
        r77 = (org.telegram.messenger.support.SparseLongArray) r77;
        r4 = 1;
        r125 = r164[r4];
        r125 = (org.telegram.messenger.support.SparseLongArray) r125;
        r151 = 0;
        if (r77 == 0) goto L_0x1b4f;
    L_0x1ae8:
        r26 = 0;
        r139 = r77.size();
    L_0x1aee:
        r0 = r26;
        r1 = r139;
        if (r0 >= r1) goto L_0x1b4f;
    L_0x1af4:
        r0 = r77;
        r1 = r26;
        r83 = r0.keyAt(r1);
        r0 = r77;
        r1 = r83;
        r100 = r0.get(r1);
        r0 = r83;
        r6 = (long) r0;
        r0 = r161;
        r8 = r0.dialog_id;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x1b12;
    L_0x1b0f:
        r26 = r26 + 1;
        goto L_0x1aee;
    L_0x1b12:
        r18 = 0;
        r0 = r161;
        r4 = r0.messages;
        r140 = r4.size();
    L_0x1b1c:
        r0 = r18;
        r1 = r140;
        if (r0 >= r1) goto L_0x1b49;
    L_0x1b22:
        r0 = r161;
        r4 = r0.messages;
        r0 = r18;
        r118 = r4.get(r0);
        r118 = (org.telegram.messenger.MessageObject) r118;
        r4 = r118.isOut();
        if (r4 != 0) goto L_0x1be5;
    L_0x1b34:
        r4 = r118.getId();
        if (r4 <= 0) goto L_0x1be5;
    L_0x1b3a:
        r4 = r118.getId();
        r0 = r100;
        r6 = (int) r0;
        if (r4 > r6) goto L_0x1be5;
    L_0x1b43:
        r4 = r118.isUnread();
        if (r4 != 0) goto L_0x1bd6;
    L_0x1b49:
        r4 = 0;
        r0 = r161;
        r0.removeUnreadPlane(r4);
    L_0x1b4f:
        if (r151 == 0) goto L_0x1ba9;
    L_0x1b51:
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        if (r4 >= 0) goto L_0x1b5c;
    L_0x1b57:
        r4 = 0;
        r0 = r161;
        r0.newUnreadMessageCount = r4;
    L_0x1b5c:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        if (r4 == 0) goto L_0x1ba9;
    L_0x1b62:
        r0 = r161;
        r4 = r0.prevSetUnreadCount;
        r0 = r161;
        r6 = r0.newUnreadMessageCount;
        if (r4 == r6) goto L_0x1b90;
    L_0x1b6c:
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        r0 = r161;
        r0.prevSetUnreadCount = r4;
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = "%d";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r161;
        r9 = r0.newUnreadMessageCount;
        r9 = java.lang.Integer.valueOf(r9);
        r7[r8] = r9;
        r6 = java.lang.String.format(r6, r7);
        r4.setText(r6);
    L_0x1b90:
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        if (r4 > 0) goto L_0x1be9;
    L_0x1b96:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r4 = r4.getVisibility();
        r6 = 4;
        if (r4 == r6) goto L_0x1ba9;
    L_0x1ba1:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = 4;
        r4.setVisibility(r6);
    L_0x1ba9:
        if (r125 == 0) goto L_0x1c30;
    L_0x1bab:
        r26 = 0;
        r139 = r125.size();
    L_0x1bb1:
        r0 = r26;
        r1 = r139;
        if (r0 >= r1) goto L_0x1c30;
    L_0x1bb7:
        r0 = r125;
        r1 = r26;
        r83 = r0.keyAt(r1);
        r0 = r125;
        r1 = r83;
        r6 = r0.get(r1);
        r5 = (int) r6;
        r0 = r83;
        r6 = (long) r0;
        r0 = r161;
        r8 = r0.dialog_id;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x1bfc;
    L_0x1bd3:
        r26 = r26 + 1;
        goto L_0x1bb1;
    L_0x1bd6:
        r118.setIsRead();
        r151 = 1;
        r0 = r161;
        r4 = r0.newUnreadMessageCount;
        r4 = r4 + -1;
        r0 = r161;
        r0.newUnreadMessageCount = r4;
    L_0x1be5:
        r18 = r18 + 1;
        goto L_0x1b1c;
    L_0x1be9:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r4 = r4.getVisibility();
        if (r4 == 0) goto L_0x1ba9;
    L_0x1bf3:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = 0;
        r4.setVisibility(r6);
        goto L_0x1ba9;
    L_0x1bfc:
        r18 = 0;
        r0 = r161;
        r4 = r0.messages;
        r140 = r4.size();
    L_0x1c06:
        r0 = r18;
        r1 = r140;
        if (r0 >= r1) goto L_0x1c30;
    L_0x1c0c:
        r0 = r161;
        r4 = r0.messages;
        r0 = r18;
        r118 = r4.get(r0);
        r118 = (org.telegram.messenger.MessageObject) r118;
        r4 = r118.isOut();
        if (r4 == 0) goto L_0x1c3c;
    L_0x1c1e:
        r4 = r118.getId();
        if (r4 <= 0) goto L_0x1c3c;
    L_0x1c24:
        r4 = r118.getId();
        if (r4 > r5) goto L_0x1c3c;
    L_0x1c2a:
        r4 = r118.isUnread();
        if (r4 != 0) goto L_0x1c37;
    L_0x1c30:
        if (r151 == 0) goto L_0x0067;
    L_0x1c32:
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x1c37:
        r118.setIsRead();
        r151 = 1;
    L_0x1c3c:
        r18 = r18 + 1;
        goto L_0x1c06;
    L_0x1c3f:
        r4 = org.telegram.messenger.NotificationCenter.historyCleared;
        r0 = r162;
        if (r0 != r4) goto L_0x1e62;
    L_0x1c45:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x1c56:
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r95 = r4.intValue();
        r151 = 0;
        r26 = 0;
    L_0x1c63:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.size();
        r0 = r26;
        if (r0 >= r4) goto L_0x1d1a;
    L_0x1c6f:
        r0 = r161;
        r4 = r0.messages;
        r0 = r26;
        r118 = r4.get(r0);
        r118 = (org.telegram.messenger.MessageObject) r118;
        r106 = r118.getId();
        if (r106 <= 0) goto L_0x1c87;
    L_0x1c81:
        r0 = r106;
        r1 = r95;
        if (r0 <= r1) goto L_0x1c8a;
    L_0x1c87:
        r26 = r26 + 1;
        goto L_0x1c63;
    L_0x1c8a:
        r0 = r161;
        r4 = r0.info;
        if (r4 == 0) goto L_0x1cbe;
    L_0x1c90:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.pinned_msg_id;
        r0 = r106;
        if (r4 != r0) goto L_0x1cbe;
    L_0x1c9a:
        r4 = 0;
        r0 = r161;
        r0.pinnedMessageObject = r4;
        r0 = r161;
        r4 = r0.info;
        r6 = 0;
        r4.pinned_msg_id = r6;
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r0 = r161;
        r6 = r0.info;
        r6 = r6.id;
        r7 = 0;
        r4.updateChannelPinnedMessage(r6, r7);
        r4 = 1;
        r0 = r161;
        r0.updatePinnedMessageView(r4);
    L_0x1cbe:
        r0 = r161;
        r4 = r0.messages;
        r0 = r26;
        r4.remove(r0);
        r26 = r26 + -1;
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r0 = r106;
        r4.remove(r0);
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r118;
        r6 = r0.dateKey;
        r54 = r4.get(r6);
        r54 = (java.util.ArrayList) r54;
        if (r54 == 0) goto L_0x1d16;
    L_0x1ce5:
        r0 = r54;
        r1 = r118;
        r0.remove(r1);
        r4 = r54.isEmpty();
        if (r4 == 0) goto L_0x1d16;
    L_0x1cf2:
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r118;
        r6 = r0.dateKey;
        r4.remove(r6);
        if (r26 < 0) goto L_0x1d16;
    L_0x1cff:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.size();
        r0 = r26;
        if (r0 >= r4) goto L_0x1d16;
    L_0x1d0b:
        r0 = r161;
        r4 = r0.messages;
        r0 = r26;
        r4.remove(r0);
        r26 = r26 + -1;
    L_0x1d16:
        r151 = 1;
        goto L_0x1c87;
    L_0x1d1a:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x1de7;
    L_0x1d24:
        r0 = r161;
        r4 = r0.endReached;
        r6 = 0;
        r4 = r4[r6];
        if (r4 != 0) goto L_0x1e23;
    L_0x1d2d:
        r0 = r161;
        r4 = r0.loading;
        if (r4 != 0) goto L_0x1e23;
    L_0x1d33:
        r0 = r161;
        r4 = r0.progressView;
        if (r4 == 0) goto L_0x1d41;
    L_0x1d39:
        r0 = r161;
        r4 = r0.progressView;
        r6 = 4;
        r4.setVisibility(r6);
    L_0x1d41:
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x1d4f;
    L_0x1d47:
        r0 = r161;
        r4 = r0.chatListView;
        r6 = 0;
        r4.setEmptyView(r6);
    L_0x1d4f:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x1dfe;
    L_0x1d55:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = 0;
        r0 = r161;
        r7 = r0.maxMessageId;
        r8 = 1;
        r9 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r7[r8] = r9;
        r4[r6] = r9;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = 0;
        r0 = r161;
        r7 = r0.minMessageId;
        r8 = 1;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r7[r8] = r9;
        r4[r6] = r9;
    L_0x1d76:
        r0 = r161;
        r4 = r0.maxDate;
        r6 = 0;
        r0 = r161;
        r7 = r0.maxDate;
        r8 = 1;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r7[r8] = r9;
        r4[r6] = r9;
        r0 = r161;
        r4 = r0.minDate;
        r6 = 0;
        r0 = r161;
        r7 = r0.minDate;
        r8 = 1;
        r9 = 0;
        r7[r8] = r9;
        r4[r6] = r9;
        r0 = r161;
        r4 = r0.waitingForLoad;
        r0 = r161;
        r6 = r0.lastLoadIndex;
        r6 = java.lang.Integer.valueOf(r6);
        r4.add(r6);
        r0 = r161;
        r4 = r0.currentAccount;
        r5 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.dialog_id;
        r8 = 30;
        r9 = 0;
        r10 = 0;
        r0 = r161;
        r4 = r0.cacheEndReached;
        r11 = 0;
        r4 = r4[r11];
        if (r4 != 0) goto L_0x1e21;
    L_0x1dbd:
        r11 = 1;
    L_0x1dbe:
        r0 = r161;
        r4 = r0.minDate;
        r12 = 0;
        r12 = r4[r12];
        r0 = r161;
        r13 = r0.classGuid;
        r14 = 0;
        r15 = 0;
        r0 = r161;
        r4 = r0.currentChat;
        r16 = org.telegram.messenger.ChatObject.isChannel(r4);
        r0 = r161;
        r0 = r0.lastLoadIndex;
        r17 = r0;
        r4 = r17 + 1;
        r0 = r161;
        r0.lastLoadIndex = r4;
        r5.loadMessages(r6, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);
        r4 = 1;
        r0 = r161;
        r0.loading = r4;
    L_0x1de7:
        if (r151 == 0) goto L_0x0067;
    L_0x1de9:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x0067;
    L_0x1def:
        r4 = 1;
        r0 = r161;
        r0.removeUnreadPlane(r4);
        r0 = r161;
        r4 = r0.chatAdapter;
        r4.notifyDataSetChanged();
        goto L_0x0067;
    L_0x1dfe:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = 0;
        r0 = r161;
        r7 = r0.maxMessageId;
        r8 = 1;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r7[r8] = r9;
        r4[r6] = r9;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = 0;
        r0 = r161;
        r7 = r0.minMessageId;
        r8 = 1;
        r9 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r7[r8] = r9;
        r4[r6] = r9;
        goto L_0x1d76;
    L_0x1e21:
        r11 = 0;
        goto L_0x1dbe;
    L_0x1e23:
        r0 = r161;
        r4 = r0.botButtons;
        if (r4 == 0) goto L_0x1e3d;
    L_0x1e29:
        r4 = 0;
        r0 = r161;
        r0.botButtons = r4;
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x1e3d;
    L_0x1e34:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r6 = 0;
        r7 = 0;
        r4.setButtons(r6, r7);
    L_0x1e3d:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x1de7;
    L_0x1e43:
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x1de7;
    L_0x1e49:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.bot;
        if (r4 == 0) goto L_0x1de7;
    L_0x1e51:
        r0 = r161;
        r4 = r0.botUser;
        if (r4 != 0) goto L_0x1de7;
    L_0x1e57:
        r4 = "";
        r0 = r161;
        r0.botUser = r4;
        r161.updateBottomOverlay();
        goto L_0x1de7;
    L_0x1e62:
        r4 = org.telegram.messenger.NotificationCenter.messagesDeleted;
        r0 = r162;
        if (r0 != r4) goto L_0x2242;
    L_0x1e68:
        r4 = 0;
        r94 = r164[r4];
        r94 = (java.util.ArrayList) r94;
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r36 = r4.intValue();
        r90 = 0;
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x1fc8;
    L_0x1e82:
        if (r36 != 0) goto L_0x1fba;
    L_0x1e84:
        r0 = r161;
        r6 = r0.mergeDialogId;
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x1fba;
    L_0x1e8e:
        r90 = 1;
    L_0x1e90:
        r151 = 0;
        r112 = 0;
        r139 = r94.size();
        r153 = 0;
        r154 = 0;
        r18 = 0;
    L_0x1e9e:
        r0 = r18;
        r1 = r139;
        if (r0 >= r1) goto L_0x1fd0;
    L_0x1ea4:
        r0 = r94;
        r1 = r18;
        r75 = r0.get(r1);
        r75 = (java.lang.Integer) r75;
        r0 = r161;
        r4 = r0.messagesDict;
        r4 = r4[r90];
        r6 = r75.intValue();
        r118 = r4.get(r6);
        r118 = (org.telegram.messenger.MessageObject) r118;
        if (r90 != 0) goto L_0x1ef2;
    L_0x1ec0:
        r0 = r161;
        r4 = r0.info;
        if (r4 == 0) goto L_0x1ef2;
    L_0x1ec6:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.pinned_msg_id;
        r6 = r75.intValue();
        if (r4 != r6) goto L_0x1ef2;
    L_0x1ed2:
        r4 = 0;
        r0 = r161;
        r0.pinnedMessageObject = r4;
        r0 = r161;
        r4 = r0.info;
        r6 = 0;
        r4.pinned_msg_id = r6;
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesStorage.getInstance(r4);
        r6 = 0;
        r0 = r36;
        r4.updateChannelPinnedMessage(r0, r6);
        r4 = 1;
        r0 = r161;
        r0.updatePinnedMessageView(r4);
    L_0x1ef2:
        if (r118 == 0) goto L_0x1fb6;
    L_0x1ef4:
        r0 = r161;
        r4 = r0.messages;
        r0 = r118;
        r78 = r4.indexOf(r0);
        r4 = -1;
        r0 = r78;
        if (r0 == r4) goto L_0x1fb6;
    L_0x1f03:
        r0 = r161;
        r4 = r0.selectedMessagesIds;
        r4 = r4[r90];
        r6 = r75.intValue();
        r4 = r4.indexOfKey(r6);
        if (r4 < 0) goto L_0x1f27;
    L_0x1f13:
        r153 = 1;
        r4 = 0;
        r6 = r139 + -1;
        r0 = r18;
        if (r0 != r6) goto L_0x1fcc;
    L_0x1f1c:
        r154 = 1;
    L_0x1f1e:
        r0 = r161;
        r1 = r118;
        r2 = r154;
        r0.addToSelectedMessages(r1, r4, r2);
    L_0x1f27:
        r0 = r161;
        r4 = r0.messages;
        r0 = r78;
        r136 = r4.remove(r0);
        r136 = (org.telegram.messenger.MessageObject) r136;
        r6 = r136.getGroupId();
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x1f68;
    L_0x1f3d:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r6 = r136.getGroupId();
        r69 = r4.get(r6);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
        if (r69 == 0) goto L_0x1f68;
    L_0x1f4d:
        if (r112 != 0) goto L_0x1f54;
    L_0x1f4f:
        r112 = new android.util.LongSparseArray;
        r112.<init>();
    L_0x1f54:
        r0 = r69;
        r6 = r0.groupId;
        r0 = r112;
        r1 = r69;
        r0.put(r6, r1);
        r0 = r69;
        r4 = r0.messages;
        r0 = r118;
        r4.remove(r0);
    L_0x1f68:
        r0 = r161;
        r4 = r0.messagesDict;
        r4 = r4[r90];
        r6 = r75.intValue();
        r4.remove(r6);
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r118;
        r6 = r0.dateKey;
        r54 = r4.get(r6);
        r54 = (java.util.ArrayList) r54;
        if (r54 == 0) goto L_0x1fb4;
    L_0x1f85:
        r0 = r54;
        r1 = r118;
        r0.remove(r1);
        r4 = r54.isEmpty();
        if (r4 == 0) goto L_0x1fb4;
    L_0x1f92:
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r118;
        r6 = r0.dateKey;
        r4.remove(r6);
        if (r78 < 0) goto L_0x1fb4;
    L_0x1f9f:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.size();
        r0 = r78;
        if (r0 >= r4) goto L_0x1fb4;
    L_0x1fab:
        r0 = r161;
        r4 = r0.messages;
        r0 = r78;
        r4.remove(r0);
    L_0x1fb4:
        r151 = 1;
    L_0x1fb6:
        r18 = r18 + 1;
        goto L_0x1e9e;
    L_0x1fba:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.id;
        r0 = r36;
        if (r0 != r4) goto L_0x0067;
    L_0x1fc4:
        r90 = 0;
        goto L_0x1e90;
    L_0x1fc8:
        if (r36 == 0) goto L_0x1e90;
    L_0x1fca:
        goto L_0x0067;
    L_0x1fcc:
        r154 = 0;
        goto L_0x1f1e;
    L_0x1fd0:
        if (r153 == 0) goto L_0x1fdc;
    L_0x1fd2:
        if (r154 != 0) goto L_0x1fdc;
    L_0x1fd4:
        r4 = 0;
        r6 = 0;
        r7 = 1;
        r0 = r161;
        r0.addToSelectedMessages(r4, r6, r7);
    L_0x1fdc:
        if (r112 == 0) goto L_0x204d;
    L_0x1fde:
        r18 = 0;
    L_0x1fe0:
        r4 = r112.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x204d;
    L_0x1fe8:
        r0 = r112;
        r1 = r18;
        r69 = r0.valueAt(r1);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
        r0 = r69;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x200a;
    L_0x1ffc:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r0 = r69;
        r6 = r0.groupId;
        r4.remove(r6);
    L_0x2007:
        r18 = r18 + 1;
        goto L_0x1fe0;
    L_0x200a:
        r69.calculate();
        r0 = r69;
        r4 = r0.messages;
        r0 = r69;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -1;
        r102 = r4.get(r6);
        r102 = (org.telegram.messenger.MessageObject) r102;
        r0 = r161;
        r4 = r0.messages;
        r0 = r102;
        r78 = r4.indexOf(r0);
        if (r78 < 0) goto L_0x2007;
    L_0x202d:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x2007;
    L_0x2033:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.messagesStartRow;
        r6 = r6 + r78;
        r0 = r69;
        r7 = r0.messages;
        r7 = r7.size();
        r4.notifyItemRangeChanged(r6, r7);
        goto L_0x2007;
    L_0x204d:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x211a;
    L_0x2057:
        r0 = r161;
        r4 = r0.endReached;
        r6 = 0;
        r4 = r4[r6];
        if (r4 != 0) goto L_0x218d;
    L_0x2060:
        r0 = r161;
        r4 = r0.loading;
        if (r4 != 0) goto L_0x218d;
    L_0x2066:
        r0 = r161;
        r4 = r0.progressView;
        if (r4 == 0) goto L_0x2074;
    L_0x206c:
        r0 = r161;
        r4 = r0.progressView;
        r6 = 4;
        r4.setVisibility(r6);
    L_0x2074:
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x2082;
    L_0x207a:
        r0 = r161;
        r4 = r0.chatListView;
        r6 = 0;
        r4.setEmptyView(r6);
    L_0x2082:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x2167;
    L_0x2088:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = 0;
        r0 = r161;
        r7 = r0.maxMessageId;
        r8 = 1;
        r9 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r7[r8] = r9;
        r4[r6] = r9;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = 0;
        r0 = r161;
        r7 = r0.minMessageId;
        r8 = 1;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r7[r8] = r9;
        r4[r6] = r9;
    L_0x20a9:
        r0 = r161;
        r4 = r0.maxDate;
        r6 = 0;
        r0 = r161;
        r7 = r0.maxDate;
        r8 = 1;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r7[r8] = r9;
        r4[r6] = r9;
        r0 = r161;
        r4 = r0.minDate;
        r6 = 0;
        r0 = r161;
        r7 = r0.minDate;
        r8 = 1;
        r9 = 0;
        r7[r8] = r9;
        r4[r6] = r9;
        r0 = r161;
        r4 = r0.waitingForLoad;
        r0 = r161;
        r6 = r0.lastLoadIndex;
        r6 = java.lang.Integer.valueOf(r6);
        r4.add(r6);
        r0 = r161;
        r4 = r0.currentAccount;
        r5 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.dialog_id;
        r8 = 30;
        r9 = 0;
        r10 = 0;
        r0 = r161;
        r4 = r0.cacheEndReached;
        r11 = 0;
        r4 = r4[r11];
        if (r4 != 0) goto L_0x218a;
    L_0x20f0:
        r11 = 1;
    L_0x20f1:
        r0 = r161;
        r4 = r0.minDate;
        r12 = 0;
        r12 = r4[r12];
        r0 = r161;
        r13 = r0.classGuid;
        r14 = 0;
        r15 = 0;
        r0 = r161;
        r4 = r0.currentChat;
        r16 = org.telegram.messenger.ChatObject.isChannel(r4);
        r0 = r161;
        r0 = r0.lastLoadIndex;
        r17 = r0;
        r4 = r17 + 1;
        r0 = r161;
        r0.lastLoadIndex = r4;
        r5.loadMessages(r6, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);
        r4 = 1;
        r0 = r161;
        r0.loading = r4;
    L_0x211a:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x0067;
    L_0x2120:
        if (r151 == 0) goto L_0x2215;
    L_0x2122:
        r4 = 0;
        r0 = r161;
        r0.removeUnreadPlane(r4);
        r0 = r161;
        r4 = r0.chatListView;
        r43 = r4.getChildCount();
        r130 = -1;
        r28 = 0;
        r18 = 0;
    L_0x2136:
        r0 = r18;
        r1 = r43;
        if (r0 >= r1) goto L_0x21eb;
    L_0x213c:
        r0 = r161;
        r4 = r0.chatListView;
        r0 = r18;
        r42 = r4.getChildAt(r0);
        r102 = 0;
        r0 = r42;
        r4 = r0 instanceof org.telegram.ui.Cells.ChatMessageCell;
        if (r4 == 0) goto L_0x21cd;
    L_0x214e:
        r4 = r42;
        r4 = (org.telegram.ui.Cells.ChatMessageCell) r4;
        r102 = r4.getMessageObject();
    L_0x2156:
        if (r102 == 0) goto L_0x2164;
    L_0x2158:
        r0 = r161;
        r4 = r0.messages;
        r0 = r102;
        r76 = r4.indexOf(r0);
        if (r76 >= 0) goto L_0x21dd;
    L_0x2164:
        r18 = r18 + 1;
        goto L_0x2136;
    L_0x2167:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = 0;
        r0 = r161;
        r7 = r0.maxMessageId;
        r8 = 1;
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r7[r8] = r9;
        r4[r6] = r9;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = 0;
        r0 = r161;
        r7 = r0.minMessageId;
        r8 = 1;
        r9 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r7[r8] = r9;
        r4[r6] = r9;
        goto L_0x20a9;
    L_0x218a:
        r11 = 0;
        goto L_0x20f1;
    L_0x218d:
        r0 = r161;
        r4 = r0.botButtons;
        if (r4 == 0) goto L_0x21a7;
    L_0x2193:
        r4 = 0;
        r0 = r161;
        r0.botButtons = r4;
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x21a7;
    L_0x219e:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r6 = 0;
        r7 = 0;
        r4.setButtons(r6, r7);
    L_0x21a7:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x211a;
    L_0x21ad:
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x211a;
    L_0x21b3:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.bot;
        if (r4 == 0) goto L_0x211a;
    L_0x21bb:
        r0 = r161;
        r4 = r0.botUser;
        if (r4 != 0) goto L_0x211a;
    L_0x21c1:
        r4 = "";
        r0 = r161;
        r0.botUser = r4;
        r161.updateBottomOverlay();
        goto L_0x211a;
    L_0x21cd:
        r0 = r42;
        r4 = r0 instanceof org.telegram.ui.Cells.ChatActionCell;
        if (r4 == 0) goto L_0x2156;
    L_0x21d3:
        r4 = r42;
        r4 = (org.telegram.ui.Cells.ChatActionCell) r4;
        r102 = r4.getMessageObject();
        goto L_0x2156;
    L_0x21dd:
        r0 = r161;
        r4 = r0.chatAdapter;
        r4 = r4.messagesStartRow;
        r130 = r4 + r76;
        r28 = r42.getBottom();
    L_0x21eb:
        r0 = r161;
        r4 = r0.chatAdapter;
        r4.notifyDataSetChanged();
        r4 = -1;
        r0 = r130;
        if (r0 == r4) goto L_0x0067;
    L_0x21f7:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r0 = r161;
        r6 = r0.chatListView;
        r6 = r6.getMeasuredHeight();
        r6 = r6 - r28;
        r0 = r161;
        r7 = r0.chatListView;
        r7 = r7.getPaddingBottom();
        r6 = r6 - r7;
        r0 = r130;
        r4.scrollToPositionWithOffset(r0, r6);
        goto L_0x0067;
    L_0x2215:
        r4 = 0;
        r0 = r161;
        r0.first_unread_id = r4;
        r4 = 0;
        r0 = r161;
        r0.last_message_id = r4;
        r4 = 0;
        r0 = r161;
        r0.createUnreadMessageAfterId = r4;
        r0 = r161;
        r4 = r0.unreadMessageObject;
        r0 = r161;
        r0.removeMessageObject(r4);
        r4 = 0;
        r0 = r161;
        r0.unreadMessageObject = r4;
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        if (r4 == 0) goto L_0x0067;
    L_0x2238:
        r0 = r161;
        r4 = r0.pagedownButtonCounter;
        r6 = 4;
        r4.setVisibility(r6);
        goto L_0x0067;
    L_0x2242:
        r4 = org.telegram.messenger.NotificationCenter.messageReceivedByServer;
        r0 = r162;
        if (r0 != r4) goto L_0x2481;
    L_0x2248:
        r4 = 0;
        r108 = r164[r4];
        r108 = (java.lang.Integer) r108;
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r108.intValue();
        r118 = r4.get(r6);
        r118 = (org.telegram.messenger.MessageObject) r118;
        if (r118 == 0) goto L_0x0067;
    L_0x2260:
        r4 = 1;
        r113 = r164[r4];
        r113 = (java.lang.Integer) r113;
        r0 = r113;
        r1 = r108;
        r4 = r0.equals(r1);
        if (r4 != 0) goto L_0x2300;
    L_0x226f:
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r113.intValue();
        r4 = r4.indexOfKey(r6);
        if (r4 < 0) goto L_0x2300;
    L_0x2280:
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r108.intValue();
        r136 = r4.get(r6);
        r136 = (org.telegram.messenger.MessageObject) r136;
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r108.intValue();
        r4.remove(r6);
        if (r136 == 0) goto L_0x0067;
    L_0x22a1:
        r0 = r161;
        r4 = r0.messages;
        r0 = r136;
        r78 = r4.indexOf(r0);
        r0 = r161;
        r4 = r0.messages;
        r0 = r78;
        r4.remove(r0);
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r136;
        r6 = r0.dateKey;
        r54 = r4.get(r6);
        r54 = (java.util.ArrayList) r54;
        r0 = r54;
        r1 = r118;
        r0.remove(r1);
        r4 = r54.isEmpty();
        if (r4 == 0) goto L_0x22f1;
    L_0x22cf:
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r118;
        r6 = r0.dateKey;
        r4.remove(r6);
        if (r78 < 0) goto L_0x22f1;
    L_0x22dc:
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.size();
        r0 = r78;
        if (r0 >= r4) goto L_0x22f1;
    L_0x22e8:
        r0 = r161;
        r4 = r0.messages;
        r0 = r78;
        r4.remove(r0);
    L_0x22f1:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x0067;
    L_0x22f7:
        r0 = r161;
        r4 = r0.chatAdapter;
        r4.notifyDataSetChanged();
        goto L_0x0067;
    L_0x2300:
        r4 = 2;
        r114 = r164[r4];
        r114 = (org.telegram.tgnet.TLRPC.Message) r114;
        r0 = r164;
        r4 = r0.length;
        r6 = 4;
        if (r4 < r6) goto L_0x246b;
    L_0x230b:
        r4 = 4;
        r70 = r164[r4];
        r70 = (java.lang.Long) r70;
    L_0x2310:
        r97 = 0;
        r152 = 0;
        if (r114 == 0) goto L_0x23dc;
    L_0x2316:
        r4 = r118.isForwarded();	 Catch:{ Exception -> 0x247b }
        if (r4 == 0) goto L_0x2473;
    L_0x231c:
        r0 = r118;
        r4 = r0.messageOwner;	 Catch:{ Exception -> 0x247b }
        r4 = r4.reply_markup;	 Catch:{ Exception -> 0x247b }
        if (r4 != 0) goto L_0x232a;
    L_0x2324:
        r0 = r114;
        r4 = r0.reply_markup;	 Catch:{ Exception -> 0x247b }
        if (r4 != 0) goto L_0x233a;
    L_0x232a:
        r0 = r118;
        r4 = r0.messageOwner;	 Catch:{ Exception -> 0x247b }
        r4 = r4.message;	 Catch:{ Exception -> 0x247b }
        r0 = r114;
        r6 = r0.message;	 Catch:{ Exception -> 0x247b }
        r4 = r4.equals(r6);	 Catch:{ Exception -> 0x247b }
        if (r4 != 0) goto L_0x2473;
    L_0x233a:
        r152 = 1;
    L_0x233c:
        if (r152 != 0) goto L_0x237b;
    L_0x233e:
        r0 = r118;
        r4 = r0.messageOwner;	 Catch:{ Exception -> 0x247b }
        r4 = r4.params;	 Catch:{ Exception -> 0x247b }
        if (r4 == 0) goto L_0x2355;
    L_0x2346:
        r0 = r118;
        r4 = r0.messageOwner;	 Catch:{ Exception -> 0x247b }
        r4 = r4.params;	 Catch:{ Exception -> 0x247b }
        r6 = "query_id";
        r4 = r4.containsKey(r6);	 Catch:{ Exception -> 0x247b }
        if (r4 != 0) goto L_0x237b;
    L_0x2355:
        r0 = r114;
        r4 = r0.media;	 Catch:{ Exception -> 0x247b }
        if (r4 == 0) goto L_0x2477;
    L_0x235b:
        r0 = r118;
        r4 = r0.messageOwner;	 Catch:{ Exception -> 0x247b }
        r4 = r4.media;	 Catch:{ Exception -> 0x247b }
        if (r4 == 0) goto L_0x2477;
    L_0x2363:
        r0 = r114;
        r4 = r0.media;	 Catch:{ Exception -> 0x247b }
        r4 = r4.getClass();	 Catch:{ Exception -> 0x247b }
        r0 = r118;
        r6 = r0.messageOwner;	 Catch:{ Exception -> 0x247b }
        r6 = r6.media;	 Catch:{ Exception -> 0x247b }
        r6 = r6.getClass();	 Catch:{ Exception -> 0x247b }
        r4 = r4.equals(r6);	 Catch:{ Exception -> 0x247b }
        if (r4 != 0) goto L_0x2477;
    L_0x237b:
        r97 = 1;
    L_0x237d:
        r6 = r118.getGroupId();
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x23c2;
    L_0x2387:
        r0 = r114;
        r6 = r0.grouped_id;
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x23c2;
    L_0x2391:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r6 = r118.getGroupId();
        r122 = r4.get(r6);
        r122 = (org.telegram.messenger.MessageObject.GroupedMessages) r122;
        if (r122 == 0) goto L_0x23ae;
    L_0x23a1:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r0 = r114;
        r6 = r0.grouped_id;
        r0 = r122;
        r4.put(r6, r0);
    L_0x23ae:
        r0 = r118;
        r4 = r0.messageOwner;
        r6 = r4.grouped_id;
        r0 = r118;
        r0.localSentGroupId = r6;
        r0 = r118;
        r4 = r0.messageOwner;
        r6 = r70.longValue();
        r4.grouped_id = r6;
    L_0x23c2:
        r0 = r114;
        r1 = r118;
        r1.messageOwner = r0;
        r4 = 1;
        r0 = r118;
        r0.generateThumbs(r4);
        r118.setType();
        r0 = r114;
        r4 = r0.media;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r4 == 0) goto L_0x23dc;
    L_0x23d9:
        r118.applyNewText();
    L_0x23dc:
        if (r152 == 0) goto L_0x23e1;
    L_0x23de:
        r118.measureInlineBotButtons();
    L_0x23e1:
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r108.intValue();
        r4.remove(r6);
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r113.intValue();
        r0 = r118;
        r4.put(r6, r0);
        r0 = r118;
        r4 = r0.messageOwner;
        r6 = r113.intValue();
        r4.id = r6;
        r0 = r118;
        r4 = r0.messageOwner;
        r6 = 0;
        r4.send_state = r6;
        r0 = r97;
        r1 = r118;
        r1.forceUpdate = r0;
        r98 = new java.util.ArrayList;
        r98.<init>();
        r0 = r98;
        r1 = r118;
        r0.add(r1);
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x2439;
    L_0x2428:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DataQuery.getInstance(r4);
        r0 = r161;
        r6 = r0.dialog_id;
        r0 = r98;
        r4.loadReplyMessagesForMessages(r0, r6);
    L_0x2439:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x2449;
    L_0x243f:
        r0 = r161;
        r4 = r0.chatAdapter;
        r6 = 1;
        r0 = r118;
        r4.updateRowWithMessageObject(r0, r6);
    L_0x2449:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        if (r4 == 0) goto L_0x245e;
    L_0x244f:
        if (r97 == 0) goto L_0x245e;
    L_0x2451:
        r0 = r161;
        r4 = r0.chatLayoutManager;
        r4 = r4.findFirstVisibleItemPosition();
        if (r4 != 0) goto L_0x245e;
    L_0x245b:
        r161.moveScrollToLastMessage();
    L_0x245e:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.NotificationsController.getInstance(r4);
        r4.playOutChatSound();
        goto L_0x0067;
    L_0x246b:
        r6 = 0;
        r70 = java.lang.Long.valueOf(r6);
        goto L_0x2310;
    L_0x2473:
        r152 = 0;
        goto L_0x233c;
    L_0x2477:
        r97 = 0;
        goto L_0x237d;
    L_0x247b:
        r59 = move-exception;
        org.telegram.messenger.FileLog.e(r59);
        goto L_0x237d;
    L_0x2481:
        r4 = org.telegram.messenger.NotificationCenter.messageReceivedByAck;
        r0 = r162;
        if (r0 != r4) goto L_0x24b8;
    L_0x2487:
        r4 = 0;
        r108 = r164[r4];
        r108 = (java.lang.Integer) r108;
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r108.intValue();
        r118 = r4.get(r6);
        r118 = (org.telegram.messenger.MessageObject) r118;
        if (r118 == 0) goto L_0x0067;
    L_0x249f:
        r0 = r118;
        r4 = r0.messageOwner;
        r6 = 0;
        r4.send_state = r6;
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x0067;
    L_0x24ac:
        r0 = r161;
        r4 = r0.chatAdapter;
        r6 = 1;
        r0 = r118;
        r4.updateRowWithMessageObject(r0, r6);
        goto L_0x0067;
    L_0x24b8:
        r4 = org.telegram.messenger.NotificationCenter.messageSendError;
        r0 = r162;
        if (r0 != r4) goto L_0x24e2;
    L_0x24be:
        r4 = 0;
        r108 = r164[r4];
        r108 = (java.lang.Integer) r108;
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r108.intValue();
        r118 = r4.get(r6);
        r118 = (org.telegram.messenger.MessageObject) r118;
        if (r118 == 0) goto L_0x0067;
    L_0x24d6:
        r0 = r118;
        r4 = r0.messageOwner;
        r6 = 2;
        r4.send_state = r6;
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x24e2:
        r4 = org.telegram.messenger.NotificationCenter.chatInfoDidLoaded;
        r0 = r162;
        if (r0 != r4) goto L_0x27b7;
    L_0x24e8:
        r4 = 0;
        r40 = r164[r4];
        r40 = (org.telegram.tgnet.TLRPC.ChatFull) r40;
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x0067;
    L_0x24f3:
        r0 = r40;
        r4 = r0.id;
        r0 = r161;
        r6 = r0.currentChat;
        r6 = r6.id;
        if (r4 != r6) goto L_0x0067;
    L_0x24ff:
        r0 = r40;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_channelFull;
        if (r4 == 0) goto L_0x2580;
    L_0x2505:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.megagroup;
        if (r4 == 0) goto L_0x256a;
    L_0x250d:
        r84 = 0;
        r0 = r40;
        r4 = r0.participants;
        if (r4 == 0) goto L_0x253e;
    L_0x2515:
        r18 = 0;
    L_0x2517:
        r0 = r40;
        r4 = r0.participants;
        r4 = r4.participants;
        r4 = r4.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x253e;
    L_0x2525:
        r0 = r40;
        r4 = r0.participants;
        r4 = r4.participants;
        r0 = r18;
        r4 = r4.get(r0);
        r4 = (org.telegram.tgnet.TLRPC.ChatParticipant) r4;
        r4 = r4.date;
        r0 = r84;
        r84 = java.lang.Math.max(r4, r0);
        r18 = r18 + 1;
        goto L_0x2517;
    L_0x253e:
        if (r84 == 0) goto L_0x2555;
    L_0x2540:
        r6 = java.lang.System.currentTimeMillis();
        r8 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r6 = r6 / r8;
        r0 = r84;
        r8 = (long) r0;
        r6 = r6 - r8;
        r6 = java.lang.Math.abs(r6);
        r8 = 3600; // 0xe10 float:5.045E-42 double:1.7786E-320;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 <= 0) goto L_0x256a;
    L_0x2555:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.currentChat;
        r6 = r6.id;
        r6 = java.lang.Integer.valueOf(r6);
        r4.loadChannelParticipants(r6);
    L_0x256a:
        r0 = r40;
        r4 = r0.participants;
        if (r4 != 0) goto L_0x2580;
    L_0x2570:
        r0 = r161;
        r4 = r0.info;
        if (r4 == 0) goto L_0x2580;
    L_0x2576:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.participants;
        r0 = r40;
        r0.participants = r4;
    L_0x2580:
        r0 = r40;
        r1 = r161;
        r1.info = r0;
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x2597;
    L_0x258c:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r0 = r161;
        r6 = r0.info;
        r4.setChatInfo(r6);
    L_0x2597:
        r0 = r161;
        r4 = r0.mentionsAdapter;
        if (r4 == 0) goto L_0x25a8;
    L_0x259d:
        r0 = r161;
        r4 = r0.mentionsAdapter;
        r0 = r161;
        r6 = r0.info;
        r4.setChatInfo(r6);
    L_0x25a8:
        r4 = 3;
        r4 = r164[r4];
        r4 = r4 instanceof org.telegram.messenger.MessageObject;
        if (r4 == 0) goto L_0x2665;
    L_0x25af:
        r4 = 3;
        r4 = r164[r4];
        r4 = (org.telegram.messenger.MessageObject) r4;
        r0 = r161;
        r0.pinnedMessageObject = r4;
        r4 = 0;
        r0 = r161;
        r0.updatePinnedMessageView(r4);
    L_0x25be:
        r0 = r161;
        r4 = r0.avatarContainer;
        if (r4 == 0) goto L_0x25d2;
    L_0x25c4:
        r0 = r161;
        r4 = r0.avatarContainer;
        r4.updateOnlineCount();
        r0 = r161;
        r4 = r0.avatarContainer;
        r4.updateSubtitle();
    L_0x25d2:
        r0 = r161;
        r4 = r0.isBroadcast;
        if (r4 == 0) goto L_0x25e7;
    L_0x25d8:
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.SendMessagesHelper.getInstance(r4);
        r0 = r161;
        r6 = r0.info;
        r4.setCurrentChatInfo(r6);
    L_0x25e7:
        r0 = r161;
        r4 = r0.info;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_chatFull;
        if (r4 == 0) goto L_0x26e4;
    L_0x25ef:
        r4 = 0;
        r0 = r161;
        r0.hasBotsCommands = r4;
        r0 = r161;
        r4 = r0.botInfo;
        r4.clear();
        r4 = 0;
        r0 = r161;
        r0.botsCount = r4;
        r4 = 0;
        org.telegram.ui.Components.URLSpanBotCommand.enabled = r4;
        r18 = 0;
    L_0x2605:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.participants;
        r4 = r4.participants;
        r4 = r4.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x266d;
    L_0x2615:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.participants;
        r4 = r4.participants;
        r0 = r18;
        r126 = r4.get(r0);
        r126 = (org.telegram.tgnet.TLRPC.ChatParticipant) r126;
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r126;
        r6 = r0.user_id;
        r6 = java.lang.Integer.valueOf(r6);
        r155 = r4.getUser(r6);
        if (r155 == 0) goto L_0x2662;
    L_0x263b:
        r0 = r155;
        r4 = r0.bot;
        if (r4 == 0) goto L_0x2662;
    L_0x2641:
        r4 = 1;
        org.telegram.ui.Components.URLSpanBotCommand.enabled = r4;
        r0 = r161;
        r4 = r0.botsCount;
        r4 = r4 + 1;
        r0 = r161;
        r0.botsCount = r4;
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DataQuery.getInstance(r4);
        r0 = r155;
        r6 = r0.id;
        r7 = 1;
        r0 = r161;
        r8 = r0.classGuid;
        r4.loadBotInfo(r6, r7, r8);
    L_0x2662:
        r18 = r18 + 1;
        goto L_0x2605;
    L_0x2665:
        r4 = 1;
        r0 = r161;
        r0.updatePinnedMessageView(r4);
        goto L_0x25be;
    L_0x266d:
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x267a;
    L_0x2673:
        r0 = r161;
        r4 = r0.chatListView;
        r4.invalidateViews();
    L_0x267a:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x268f;
    L_0x2680:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r0 = r161;
        r6 = r0.botsCount;
        r0 = r161;
        r7 = r0.hasBotsCommands;
        r4.setBotsCount(r6, r7);
    L_0x268f:
        r0 = r161;
        r4 = r0.mentionsAdapter;
        if (r4 == 0) goto L_0x26a0;
    L_0x2695:
        r0 = r161;
        r4 = r0.mentionsAdapter;
        r0 = r161;
        r6 = r0.botsCount;
        r4.setBotsCount(r6);
    L_0x26a0:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x0067;
    L_0x26aa:
        r0 = r161;
        r6 = r0.mergeDialogId;
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x26b4:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.migrated_from_chat_id;
        if (r4 == 0) goto L_0x0067;
    L_0x26bc:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.migrated_from_chat_id;
        r4 = -r4;
        r6 = (long) r4;
        r0 = r161;
        r0.mergeDialogId = r6;
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = 1;
        r0 = r161;
        r7 = r0.info;
        r7 = r7.migrated_from_max_id;
        r4[r6] = r7;
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x0067;
    L_0x26db:
        r0 = r161;
        r4 = r0.chatAdapter;
        r4.notifyDataSetChanged();
        goto L_0x0067;
    L_0x26e4:
        r0 = r161;
        r4 = r0.info;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_channelFull;
        if (r4 == 0) goto L_0x267a;
    L_0x26ec:
        r4 = 0;
        r0 = r161;
        r0.hasBotsCommands = r4;
        r0 = r161;
        r4 = r0.botInfo;
        r4.clear();
        r4 = 0;
        r0 = r161;
        r0.botsCount = r4;
        r0 = r161;
        r4 = r0.info;
        r4 = r4.bot_info;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x277d;
    L_0x2709:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x277d;
    L_0x270f:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.megagroup;
        if (r4 == 0) goto L_0x277d;
    L_0x2717:
        r4 = 1;
    L_0x2718:
        org.telegram.ui.Components.URLSpanBotCommand.enabled = r4;
        r0 = r161;
        r4 = r0.info;
        r4 = r4.bot_info;
        r4 = r4.size();
        r0 = r161;
        r0.botsCount = r4;
        r18 = 0;
    L_0x272a:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.bot_info;
        r4 = r4.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x277f;
    L_0x2738:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.bot_info;
        r0 = r18;
        r27 = r4.get(r0);
        r27 = (org.telegram.tgnet.TLRPC.BotInfo) r27;
        r0 = r27;
        r4 = r0.commands;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x276d;
    L_0x2750:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x2768;
    L_0x275a:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x276d;
    L_0x2760:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.megagroup;
        if (r4 == 0) goto L_0x276d;
    L_0x2768:
        r4 = 1;
        r0 = r161;
        r0.hasBotsCommands = r4;
    L_0x276d:
        r0 = r161;
        r4 = r0.botInfo;
        r0 = r27;
        r6 = r0.user_id;
        r0 = r27;
        r4.put(r6, r0);
        r18 = r18 + 1;
        goto L_0x272a;
    L_0x277d:
        r4 = 0;
        goto L_0x2718;
    L_0x277f:
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x278c;
    L_0x2785:
        r0 = r161;
        r4 = r0.chatListView;
        r4.invalidateViews();
    L_0x278c:
        r0 = r161;
        r4 = r0.mentionsAdapter;
        if (r4 == 0) goto L_0x267a;
    L_0x2792:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x27aa;
    L_0x279c:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x267a;
    L_0x27a2:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.megagroup;
        if (r4 == 0) goto L_0x267a;
    L_0x27aa:
        r0 = r161;
        r4 = r0.mentionsAdapter;
        r0 = r161;
        r6 = r0.botInfo;
        r4.setBotInfo(r6);
        goto L_0x267a;
    L_0x27b7:
        r4 = org.telegram.messenger.NotificationCenter.chatInfoCantLoad;
        r0 = r162;
        if (r0 != r4) goto L_0x287f;
    L_0x27bd:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r41 = r4.intValue();
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x0067;
    L_0x27cc:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.id;
        r0 = r41;
        if (r4 != r0) goto L_0x0067;
    L_0x27d6:
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r134 = r4.intValue();
        r4 = r161.getParentActivity();
        if (r4 == 0) goto L_0x0067;
    L_0x27e5:
        r0 = r161;
        r4 = r0.closeChatDialog;
        if (r4 != 0) goto L_0x0067;
    L_0x27eb:
        r29 = new org.telegram.ui.ActionBar.AlertDialog$Builder;
        r4 = r161.getParentActivity();
        r0 = r29;
        r0.<init>(r4);
        r4 = "AppName";
        r6 = 2131492999; // 0x7f0c0087 float:1.8609466E38 double:1.053097465E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r0 = r29;
        r0.setTitle(r4);
        if (r134 != 0) goto L_0x2855;
    L_0x2807:
        r4 = "ChannelCantOpenPrivate";
        r6 = 2131493184; // 0x7f0c0140 float:1.860984E38 double:1.0530975566E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r0 = r29;
        r0.setMessage(r4);
    L_0x2816:
        r4 = "OK";
        r6 = 2131494093; // 0x7f0c04cd float:1.8611685E38 double:1.0530980057E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r6 = 0;
        r0 = r29;
        r0.setPositiveButton(r4, r6);
        r4 = r29.create();
        r0 = r161;
        r0.closeChatDialog = r4;
        r0 = r161;
        r0.showDialog(r4);
        r4 = 0;
        r0 = r161;
        r0.loading = r4;
        r0 = r161;
        r4 = r0.progressView;
        if (r4 == 0) goto L_0x2846;
    L_0x283e:
        r0 = r161;
        r4 = r0.progressView;
        r6 = 4;
        r4.setVisibility(r6);
    L_0x2846:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x0067;
    L_0x284c:
        r0 = r161;
        r4 = r0.chatAdapter;
        r4.notifyDataSetChanged();
        goto L_0x0067;
    L_0x2855:
        r4 = 1;
        r0 = r134;
        if (r0 != r4) goto L_0x286a;
    L_0x285a:
        r4 = "ChannelCantOpenNa";
        r6 = 2131493183; // 0x7f0c013f float:1.8609839E38 double:1.053097556E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r0 = r29;
        r0.setMessage(r4);
        goto L_0x2816;
    L_0x286a:
        r4 = 2;
        r0 = r134;
        if (r0 != r4) goto L_0x2816;
    L_0x286f:
        r4 = "ChannelCantOpenBanned";
        r6 = 2131493182; // 0x7f0c013e float:1.8609837E38 double:1.0530975556E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r0 = r29;
        r0.setMessage(r4);
        goto L_0x2816;
    L_0x287f:
        r4 = org.telegram.messenger.NotificationCenter.contactsDidLoaded;
        r0 = r162;
        if (r0 != r4) goto L_0x28a0;
    L_0x2885:
        r161.updateContactStatus();
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x2891;
    L_0x288e:
        r161.updateSpamView();
    L_0x2891:
        r0 = r161;
        r4 = r0.avatarContainer;
        if (r4 == 0) goto L_0x0067;
    L_0x2897:
        r0 = r161;
        r4 = r0.avatarContainer;
        r4.updateSubtitle();
        goto L_0x0067;
    L_0x28a0:
        r4 = org.telegram.messenger.NotificationCenter.encryptedChatUpdated;
        r0 = r162;
        if (r0 != r4) goto L_0x293e;
    L_0x28a6:
        r4 = 0;
        r39 = r164[r4];
        r39 = (org.telegram.tgnet.TLRPC.EncryptedChat) r39;
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x0067;
    L_0x28b1:
        r0 = r39;
        r4 = r0.id;
        r0 = r161;
        r6 = r0.currentEncryptedChat;
        r6 = r6.id;
        if (r4 != r6) goto L_0x0067;
    L_0x28bd:
        r0 = r39;
        r1 = r161;
        r1.currentEncryptedChat = r0;
        r161.updateContactStatus();
        r161.updateSecretStatus();
        r161.initStickers();
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x290a;
    L_0x28d2:
        r0 = r161;
        r7 = r0.chatActivityEnterView;
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x28ea;
    L_0x28dc:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        r4 = r4.layer;
        r4 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r4);
        r6 = 23;
        if (r4 < r6) goto L_0x2938;
    L_0x28ea:
        r4 = 1;
    L_0x28eb:
        r0 = r161;
        r6 = r0.currentEncryptedChat;
        if (r6 == 0) goto L_0x28ff;
    L_0x28f1:
        r0 = r161;
        r6 = r0.currentEncryptedChat;
        r6 = r6.layer;
        r6 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r6);
        r8 = 46;
        if (r6 < r8) goto L_0x293a;
    L_0x28ff:
        r6 = 1;
    L_0x2900:
        r7.setAllowStickersAndGifs(r4, r6);
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r4.checkRoundVideo();
    L_0x290a:
        r0 = r161;
        r4 = r0.mentionsAdapter;
        if (r4 == 0) goto L_0x0067;
    L_0x2910:
        r0 = r161;
        r6 = r0.mentionsAdapter;
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r4 = r4.isEditingMessage();
        if (r4 != 0) goto L_0x293c;
    L_0x291e:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x2932;
    L_0x2924:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        r4 = r4.layer;
        r4 = org.telegram.messenger.AndroidUtilities.getPeerLayerVersion(r4);
        r7 = 46;
        if (r4 < r7) goto L_0x293c;
    L_0x2932:
        r4 = 1;
    L_0x2933:
        r6.setNeedBotContext(r4);
        goto L_0x0067;
    L_0x2938:
        r4 = 0;
        goto L_0x28eb;
    L_0x293a:
        r6 = 0;
        goto L_0x2900;
    L_0x293c:
        r4 = 0;
        goto L_0x2933;
    L_0x293e:
        r4 = org.telegram.messenger.NotificationCenter.messagesReadEncrypted;
        r0 = r162;
        if (r0 != r4) goto L_0x29a1;
    L_0x2944:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r60 = r4.intValue();
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 == 0) goto L_0x0067;
    L_0x2953:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        r4 = r4.id;
        r0 = r60;
        if (r4 != r0) goto L_0x0067;
    L_0x295d:
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r51 = r4.intValue();
        r0 = r161;
        r4 = r0.messages;
        r4 = r4.iterator();
    L_0x296e:
        r6 = r4.hasNext();
        if (r6 == 0) goto L_0x298c;
    L_0x2974:
        r118 = r4.next();
        r118 = (org.telegram.messenger.MessageObject) r118;
        r6 = r118.isOut();
        if (r6 == 0) goto L_0x296e;
    L_0x2980:
        r6 = r118.isOut();
        if (r6 == 0) goto L_0x2991;
    L_0x2986:
        r6 = r118.isUnread();
        if (r6 != 0) goto L_0x2991;
    L_0x298c:
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x2991:
        r0 = r118;
        r6 = r0.messageOwner;
        r6 = r6.date;
        r6 = r6 + -1;
        r0 = r51;
        if (r6 > r0) goto L_0x296e;
    L_0x299d:
        r118.setIsRead();
        goto L_0x296e;
    L_0x29a1:
        r4 = org.telegram.messenger.NotificationCenter.removeAllMessagesFromDialog;
        r0 = r162;
        if (r0 != r4) goto L_0x2b62;
    L_0x29a7:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r6 > r56 ? 1 : (r6 == r56 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x29b8:
        r0 = r161;
        r4 = r0.messages;
        r4.clear();
        r0 = r161;
        r4 = r0.waitingForLoad;
        r4.clear();
        r0 = r161;
        r4 = r0.messagesByDays;
        r4.clear();
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r4.clear();
        r18 = 1;
    L_0x29d6:
        if (r18 < 0) goto L_0x2a37;
    L_0x29d8:
        r0 = r161;
        r4 = r0.messagesDict;
        r4 = r4[r18];
        r4.clear();
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x2a25;
    L_0x29e7:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r4[r18] = r6;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4[r18] = r6;
    L_0x29f8:
        r0 = r161;
        r4 = r0.maxDate;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4[r18] = r6;
        r0 = r161;
        r4 = r0.minDate;
        r6 = 0;
        r4[r18] = r6;
        r0 = r161;
        r4 = r0.selectedMessagesIds;
        r4 = r4[r18];
        r4.clear();
        r0 = r161;
        r4 = r0.selectedMessagesCanCopyIds;
        r4 = r4[r18];
        r4.clear();
        r0 = r161;
        r4 = r0.selectedMessagesCanStarIds;
        r4 = r4[r18];
        r4.clear();
        r18 = r18 + -1;
        goto L_0x29d6;
    L_0x2a25:
        r0 = r161;
        r4 = r0.maxMessageId;
        r6 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r4[r18] = r6;
        r0 = r161;
        r4 = r0.minMessageId;
        r6 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r4[r18] = r6;
        goto L_0x29f8;
    L_0x2a37:
        r4 = 0;
        r0 = r161;
        r0.cantDeleteMessagesCount = r4;
        r4 = 0;
        r0 = r161;
        r0.canEditMessagesCount = r4;
        r0 = r161;
        r4 = r0.actionBar;
        r4.hideActionMode();
        r4 = 1;
        r0 = r161;
        r0.updatePinnedMessageView(r4);
        r0 = r161;
        r4 = r0.botButtons;
        if (r4 == 0) goto L_0x2a68;
    L_0x2a54:
        r4 = 0;
        r0 = r161;
        r0.botButtons = r4;
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x2a68;
    L_0x2a5f:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r6 = 0;
        r7 = 0;
        r4.setButtons(r6, r7);
    L_0x2a68:
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Boolean) r4;
        r4 = r4.booleanValue();
        if (r4 == 0) goto L_0x2b48;
    L_0x2a73:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x2a94;
    L_0x2a79:
        r0 = r161;
        r6 = r0.progressView;
        r0 = r161;
        r4 = r0.chatAdapter;
        r4 = r4.botInfoRow;
        r7 = -1;
        if (r4 != r7) goto L_0x2ab3;
    L_0x2a88:
        r4 = 0;
    L_0x2a89:
        r6.setVisibility(r4);
        r0 = r161;
        r4 = r0.chatListView;
        r6 = 0;
        r4.setEmptyView(r6);
    L_0x2a94:
        r18 = 0;
    L_0x2a96:
        r4 = 2;
        r0 = r18;
        if (r0 >= r4) goto L_0x2ab5;
    L_0x2a9b:
        r0 = r161;
        r4 = r0.endReached;
        r6 = 0;
        r4[r18] = r6;
        r0 = r161;
        r4 = r0.cacheEndReached;
        r6 = 0;
        r4[r18] = r6;
        r0 = r161;
        r4 = r0.forwardEndReached;
        r6 = 1;
        r4[r18] = r6;
        r18 = r18 + 1;
        goto L_0x2a96;
    L_0x2ab3:
        r4 = 4;
        goto L_0x2a89;
    L_0x2ab5:
        r4 = 1;
        r0 = r161;
        r0.first = r4;
        r4 = 1;
        r0 = r161;
        r0.firstLoading = r4;
        r4 = 1;
        r0 = r161;
        r0.loading = r4;
        r4 = 0;
        r0 = r161;
        r0.startLoadFromMessageId = r4;
        r4 = 0;
        r0 = r161;
        r0.needSelectFromMessageId = r4;
        r0 = r161;
        r4 = r0.waitingForLoad;
        r0 = r161;
        r6 = r0.lastLoadIndex;
        r6 = java.lang.Integer.valueOf(r6);
        r4.add(r6);
        r0 = r161;
        r4 = r0.currentAccount;
        r5 = org.telegram.messenger.MessagesController.getInstance(r4);
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x2b45;
    L_0x2aef:
        r8 = 30;
    L_0x2af1:
        r9 = 0;
        r10 = 0;
        r11 = 1;
        r12 = 0;
        r0 = r161;
        r13 = r0.classGuid;
        r14 = 2;
        r15 = 0;
        r0 = r161;
        r4 = r0.currentChat;
        r16 = org.telegram.messenger.ChatObject.isChannel(r4);
        r0 = r161;
        r0 = r0.lastLoadIndex;
        r17 = r0;
        r4 = r17 + 1;
        r0 = r161;
        r0.lastLoadIndex = r4;
        r5.loadMessages(r6, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);
    L_0x2b12:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x2b1f;
    L_0x2b18:
        r0 = r161;
        r4 = r0.chatAdapter;
        r4.notifyDataSetChanged();
    L_0x2b1f:
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x0067;
    L_0x2b25:
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x0067;
    L_0x2b2b:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.bot;
        if (r4 == 0) goto L_0x0067;
    L_0x2b33:
        r0 = r161;
        r4 = r0.botUser;
        if (r4 != 0) goto L_0x0067;
    L_0x2b39:
        r4 = "";
        r0 = r161;
        r0.botUser = r4;
        r161.updateBottomOverlay();
        goto L_0x0067;
    L_0x2b45:
        r8 = 20;
        goto L_0x2af1;
    L_0x2b48:
        r0 = r161;
        r4 = r0.progressView;
        if (r4 == 0) goto L_0x2b12;
    L_0x2b4e:
        r0 = r161;
        r4 = r0.progressView;
        r6 = 4;
        r4.setVisibility(r6);
        r0 = r161;
        r4 = r0.chatListView;
        r0 = r161;
        r6 = r0.emptyViewContainer;
        r4.setEmptyView(r6);
        goto L_0x2b12;
    L_0x2b62:
        r4 = org.telegram.messenger.NotificationCenter.screenshotTook;
        r0 = r162;
        if (r0 != r4) goto L_0x2b6d;
    L_0x2b68:
        r161.updateInformationForScreenshotDetector();
        goto L_0x0067;
    L_0x2b6d:
        r4 = org.telegram.messenger.NotificationCenter.blockedUsersDidLoaded;
        r0 = r162;
        if (r0 != r4) goto L_0x2ba9;
    L_0x2b73:
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x0067;
    L_0x2b79:
        r0 = r161;
        r0 = r0.userBlocked;
        r124 = r0;
        r0 = r161;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r4 = r4.blockedUsers;
        r0 = r161;
        r6 = r0.currentUser;
        r6 = r6.id;
        r4 = r4.indexOfKey(r6);
        if (r4 < 0) goto L_0x2ba7;
    L_0x2b95:
        r4 = 1;
    L_0x2b96:
        r0 = r161;
        r0.userBlocked = r4;
        r0 = r161;
        r4 = r0.userBlocked;
        r0 = r124;
        if (r0 == r4) goto L_0x0067;
    L_0x2ba2:
        r161.updateBottomOverlay();
        goto L_0x0067;
    L_0x2ba7:
        r4 = 0;
        goto L_0x2b96;
    L_0x2ba9:
        r4 = org.telegram.messenger.NotificationCenter.FileNewChunkAvailable;
        r0 = r162;
        if (r0 != r4) goto L_0x2bfe;
    L_0x2baf:
        r4 = 0;
        r102 = r164[r4];
        r102 = (org.telegram.messenger.MessageObject) r102;
        r4 = 3;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r64 = r4.longValue();
        r6 = 0;
        r4 = (r64 > r6 ? 1 : (r64 == r6 ? 0 : -1));
        if (r4 == 0) goto L_0x0067;
    L_0x2bc3:
        r0 = r161;
        r6 = r0.dialog_id;
        r8 = r102.getDialogId();
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x2bcf:
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r6 = r102.getId();
        r49 = r4.get(r6);
        r49 = (org.telegram.messenger.MessageObject) r49;
        if (r49 == 0) goto L_0x0067;
    L_0x2be2:
        r0 = r49;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        if (r4 == 0) goto L_0x0067;
    L_0x2bec:
        r0 = r49;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r0 = r64;
        r6 = (int) r0;
        r4.size = r6;
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x2bfe:
        r4 = org.telegram.messenger.NotificationCenter.didCreatedNewDeleteTask;
        r0 = r162;
        if (r0 != r4) goto L_0x2c8b;
    L_0x2c04:
        r4 = 0;
        r105 = r164[r4];
        r105 = (android.util.SparseArray) r105;
        r34 = 0;
        r74 = 0;
    L_0x2c0d:
        r4 = r105.size();
        r0 = r74;
        if (r0 >= r4) goto L_0x2c84;
    L_0x2c15:
        r0 = r105;
        r1 = r74;
        r83 = r0.keyAt(r1);
        r0 = r105;
        r1 = r83;
        r20 = r0.get(r1);
        r20 = (java.util.ArrayList) r20;
        r18 = 0;
    L_0x2c29:
        r4 = r20.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x2c81;
    L_0x2c31:
        r0 = r20;
        r1 = r18;
        r4 = r0.get(r1);
        r4 = (java.lang.Long) r4;
        r106 = r4.longValue();
        if (r18 != 0) goto L_0x2c60;
    L_0x2c41:
        r4 = 32;
        r6 = r106 >> r4;
        r0 = (int) r6;
        r36 = r0;
        if (r36 >= 0) goto L_0x2c4c;
    L_0x2c4a:
        r36 = 0;
    L_0x2c4c:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x2c7f;
    L_0x2c56:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.id;
    L_0x2c5c:
        r0 = r36;
        if (r0 != r4) goto L_0x0067;
    L_0x2c60:
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r0 = r106;
        r6 = (int) r0;
        r102 = r4.get(r6);
        r102 = (org.telegram.messenger.MessageObject) r102;
        if (r102 == 0) goto L_0x2c7c;
    L_0x2c72:
        r0 = r102;
        r4 = r0.messageOwner;
        r0 = r83;
        r4.destroyTime = r0;
        r34 = 1;
    L_0x2c7c:
        r18 = r18 + 1;
        goto L_0x2c29;
    L_0x2c7f:
        r4 = 0;
        goto L_0x2c5c;
    L_0x2c81:
        r74 = r74 + 1;
        goto L_0x2c0d;
    L_0x2c84:
        if (r34 == 0) goto L_0x0067;
    L_0x2c86:
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x2c8b:
        r4 = org.telegram.messenger.NotificationCenter.messagePlayingDidStarted;
        r0 = r162;
        if (r0 != r4) goto L_0x2d84;
    L_0x2c91:
        r4 = 0;
        r102 = r164[r4];
        r102 = (org.telegram.messenger.MessageObject) r102;
        r0 = r102;
        r6 = r0.eventId;
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x2ca0:
        r0 = r161;
        r1 = r102;
        r0.sendSecretMessageRead(r1);
        r4 = r102.isRoundVideo();
        if (r4 == 0) goto L_0x2cd7;
    L_0x2cad:
        r0 = r161;
        r4 = r0.fragmentView;
        if (r4 == 0) goto L_0x2cd7;
    L_0x2cb3:
        r0 = r161;
        r4 = r0.fragmentView;
        r4 = r4.getParent();
        if (r4 == 0) goto L_0x2cd7;
    L_0x2cbd:
        r4 = org.telegram.messenger.MediaController.getInstance();
        r6 = 1;
        r0 = r161;
        r6 = r0.createTextureView(r6);
        r0 = r161;
        r7 = r0.aspectRatioFrameLayout;
        r0 = r161;
        r8 = r0.roundVideoContainer;
        r9 = 1;
        r4.setTextureView(r6, r7, r8, r9);
        r161.updateTextureViewPosition();
    L_0x2cd7:
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x0067;
    L_0x2cdd:
        r0 = r161;
        r4 = r0.chatListView;
        r43 = r4.getChildCount();
        r18 = 0;
    L_0x2ce7:
        r0 = r18;
        r1 = r43;
        if (r0 >= r1) goto L_0x2d45;
    L_0x2ced:
        r0 = r161;
        r4 = r0.chatListView;
        r0 = r18;
        r157 = r4.getChildAt(r0);
        r0 = r157;
        r4 = r0 instanceof org.telegram.ui.Cells.ChatMessageCell;
        if (r4 == 0) goto L_0x2d1a;
    L_0x2cfd:
        r33 = r157;
        r33 = (org.telegram.ui.Cells.ChatMessageCell) r33;
        r103 = r33.getMessageObject();
        if (r103 == 0) goto L_0x2d1a;
    L_0x2d07:
        r4 = r103.isVoice();
        if (r4 != 0) goto L_0x2d13;
    L_0x2d0d:
        r4 = r103.isMusic();
        if (r4 == 0) goto L_0x2d1d;
    L_0x2d13:
        r4 = 0;
        r6 = 0;
        r0 = r33;
        r0.updateButtonState(r4, r6);
    L_0x2d1a:
        r18 = r18 + 1;
        goto L_0x2ce7;
    L_0x2d1d:
        r4 = r103.isRoundVideo();
        if (r4 == 0) goto L_0x2d1a;
    L_0x2d23:
        r4 = 0;
        r0 = r33;
        r0.checkRoundVideoPlayback(r4);
        r4 = org.telegram.messenger.MediaController.getInstance();
        r0 = r103;
        r4 = r4.isPlayingMessage(r0);
        if (r4 != 0) goto L_0x2d1a;
    L_0x2d35:
        r0 = r103;
        r4 = r0.audioProgress;
        r6 = 0;
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 == 0) goto L_0x2d1a;
    L_0x2d3e:
        r103.resetPlayingProgress();
        r33.invalidate();
        goto L_0x2d1a;
    L_0x2d45:
        r0 = r161;
        r4 = r0.mentionListView;
        r43 = r4.getChildCount();
        r18 = 0;
    L_0x2d4f:
        r0 = r18;
        r1 = r43;
        if (r0 >= r1) goto L_0x0067;
    L_0x2d55:
        r0 = r161;
        r4 = r0.mentionListView;
        r0 = r18;
        r157 = r4.getChildAt(r0);
        r0 = r157;
        r4 = r0 instanceof org.telegram.ui.Cells.ContextLinkCell;
        if (r4 == 0) goto L_0x2d81;
    L_0x2d65:
        r33 = r157;
        r33 = (org.telegram.ui.Cells.ContextLinkCell) r33;
        r103 = r33.getMessageObject();
        if (r103 == 0) goto L_0x2d81;
    L_0x2d6f:
        r4 = r103.isVoice();
        if (r4 != 0) goto L_0x2d7b;
    L_0x2d75:
        r4 = r103.isMusic();
        if (r4 == 0) goto L_0x2d81;
    L_0x2d7b:
        r4 = 0;
        r0 = r33;
        r0.updateButtonState(r4);
    L_0x2d81:
        r18 = r18 + 1;
        goto L_0x2d4f;
    L_0x2d84:
        r4 = org.telegram.messenger.NotificationCenter.messagePlayingDidReset;
        r0 = r162;
        if (r0 == r4) goto L_0x2d90;
    L_0x2d8a:
        r4 = org.telegram.messenger.NotificationCenter.messagePlayingPlayStateChanged;
        r0 = r162;
        if (r0 != r4) goto L_0x2e37;
    L_0x2d90:
        r4 = org.telegram.messenger.NotificationCenter.messagePlayingDidReset;
        r0 = r162;
        if (r0 != r4) goto L_0x2d99;
    L_0x2d96:
        r161.destroyTextureView();
    L_0x2d99:
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x0067;
    L_0x2d9f:
        r0 = r161;
        r4 = r0.chatListView;
        r43 = r4.getChildCount();
        r18 = 0;
    L_0x2da9:
        r0 = r18;
        r1 = r43;
        if (r0 >= r1) goto L_0x2df8;
    L_0x2daf:
        r0 = r161;
        r4 = r0.chatListView;
        r0 = r18;
        r157 = r4.getChildAt(r0);
        r0 = r157;
        r4 = r0 instanceof org.telegram.ui.Cells.ChatMessageCell;
        if (r4 == 0) goto L_0x2ddc;
    L_0x2dbf:
        r33 = r157;
        r33 = (org.telegram.ui.Cells.ChatMessageCell) r33;
        r102 = r33.getMessageObject();
        if (r102 == 0) goto L_0x2ddc;
    L_0x2dc9:
        r4 = r102.isVoice();
        if (r4 != 0) goto L_0x2dd5;
    L_0x2dcf:
        r4 = r102.isMusic();
        if (r4 == 0) goto L_0x2ddf;
    L_0x2dd5:
        r4 = 0;
        r6 = 0;
        r0 = r33;
        r0.updateButtonState(r4, r6);
    L_0x2ddc:
        r18 = r18 + 1;
        goto L_0x2da9;
    L_0x2ddf:
        r4 = r102.isRoundVideo();
        if (r4 == 0) goto L_0x2ddc;
    L_0x2de5:
        r4 = org.telegram.messenger.MediaController.getInstance();
        r0 = r102;
        r4 = r4.isPlayingMessage(r0);
        if (r4 != 0) goto L_0x2ddc;
    L_0x2df1:
        r4 = 1;
        r0 = r33;
        r0.checkRoundVideoPlayback(r4);
        goto L_0x2ddc;
    L_0x2df8:
        r0 = r161;
        r4 = r0.mentionListView;
        r43 = r4.getChildCount();
        r18 = 0;
    L_0x2e02:
        r0 = r18;
        r1 = r43;
        if (r0 >= r1) goto L_0x0067;
    L_0x2e08:
        r0 = r161;
        r4 = r0.mentionListView;
        r0 = r18;
        r157 = r4.getChildAt(r0);
        r0 = r157;
        r4 = r0 instanceof org.telegram.ui.Cells.ContextLinkCell;
        if (r4 == 0) goto L_0x2e34;
    L_0x2e18:
        r33 = r157;
        r33 = (org.telegram.ui.Cells.ContextLinkCell) r33;
        r102 = r33.getMessageObject();
        if (r102 == 0) goto L_0x2e34;
    L_0x2e22:
        r4 = r102.isVoice();
        if (r4 != 0) goto L_0x2e2e;
    L_0x2e28:
        r4 = r102.isMusic();
        if (r4 == 0) goto L_0x2e34;
    L_0x2e2e:
        r4 = 0;
        r0 = r33;
        r0.updateButtonState(r4);
    L_0x2e34:
        r18 = r18 + 1;
        goto L_0x2e02;
    L_0x2e37:
        r4 = org.telegram.messenger.NotificationCenter.messagePlayingProgressDidChanged;
        r0 = r162;
        if (r0 != r4) goto L_0x2eb5;
    L_0x2e3d:
        r4 = 0;
        r106 = r164[r4];
        r106 = (java.lang.Integer) r106;
        r0 = r161;
        r4 = r0.chatListView;
        if (r4 == 0) goto L_0x0067;
    L_0x2e48:
        r0 = r161;
        r4 = r0.chatListView;
        r43 = r4.getChildCount();
        r18 = 0;
    L_0x2e52:
        r0 = r18;
        r1 = r43;
        if (r0 >= r1) goto L_0x0067;
    L_0x2e58:
        r0 = r161;
        r4 = r0.chatListView;
        r0 = r18;
        r157 = r4.getChildAt(r0);
        r0 = r157;
        r4 = r0 instanceof org.telegram.ui.Cells.ChatMessageCell;
        if (r4 == 0) goto L_0x2eb2;
    L_0x2e68:
        r33 = r157;
        r33 = (org.telegram.ui.Cells.ChatMessageCell) r33;
        r129 = r33.getMessageObject();
        if (r129 == 0) goto L_0x2eb2;
    L_0x2e72:
        r4 = r129.getId();
        r6 = r106.intValue();
        if (r4 != r6) goto L_0x2eb2;
    L_0x2e7c:
        r4 = org.telegram.messenger.MediaController.getInstance();
        r128 = r4.getPlayingMessageObject();
        if (r128 == 0) goto L_0x0067;
    L_0x2e86:
        r0 = r128;
        r4 = r0.audioProgress;
        r0 = r129;
        r0.audioProgress = r4;
        r0 = r128;
        r4 = r0.audioProgressSec;
        r0 = r129;
        r0.audioProgressSec = r4;
        r0 = r128;
        r4 = r0.audioPlayerDuration;
        r0 = r129;
        r0.audioPlayerDuration = r4;
        r33.updatePlayingMessageProgress();
        r0 = r161;
        r4 = r0.drawLaterRoundProgressCell;
        r0 = r33;
        if (r4 != r0) goto L_0x0067;
    L_0x2ea9:
        r0 = r161;
        r4 = r0.fragmentView;
        r4.invalidate();
        goto L_0x0067;
    L_0x2eb2:
        r18 = r18 + 1;
        goto L_0x2e52;
    L_0x2eb5:
        r4 = org.telegram.messenger.NotificationCenter.updateMessageMedia;
        r0 = r162;
        if (r0 != r4) goto L_0x3042;
    L_0x2ebb:
        r4 = 0;
        r99 = r164[r4];
        r99 = (org.telegram.tgnet.TLRPC.Message) r99;
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r0 = r99;
        r6 = r0.id;
        r62 = r4.get(r6);
        r62 = (org.telegram.messenger.MessageObject) r62;
        if (r62 == 0) goto L_0x0067;
    L_0x2ed3:
        r0 = r62;
        r4 = r0.messageOwner;
        r0 = r99;
        r6 = r0.media;
        r4.media = r6;
        r0 = r62;
        r4 = r0.messageOwner;
        r0 = r99;
        r6 = r0.attachPath;
        r4.attachPath = r6;
        r4 = 0;
        r0 = r62;
        r0.generateThumbs(r4);
        r6 = r62.getGroupId();
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x2fd3;
    L_0x2ef7:
        r0 = r62;
        r4 = r0.photoThumbs;
        if (r4 == 0) goto L_0x2f07;
    L_0x2efd:
        r0 = r62;
        r4 = r0.photoThumbs;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x2fd3;
    L_0x2f07:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r6 = r62.getGroupId();
        r69 = r4.get(r6);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
        if (r69 == 0) goto L_0x2fd3;
    L_0x2f17:
        r0 = r69;
        r4 = r0.messages;
        r0 = r62;
        r76 = r4.indexOf(r0);
        if (r76 < 0) goto L_0x2fd3;
    L_0x2f23:
        r0 = r69;
        r4 = r0.messages;
        r148 = r4.size();
        r102 = 0;
        if (r76 <= 0) goto L_0x2fb5;
    L_0x2f2f:
        r0 = r69;
        r4 = r0.messages;
        r4 = r4.size();
        r4 = r4 + -1;
        r0 = r76;
        if (r0 >= r4) goto L_0x2fb5;
    L_0x2f3d:
        r141 = new org.telegram.messenger.MessageObject$GroupedMessages;
        r141.<init>();
        r4 = org.telegram.messenger.Utilities.random;
        r6 = r4.nextLong();
        r0 = r141;
        r0.groupId = r6;
        r0 = r141;
        r4 = r0.messages;
        r0 = r69;
        r6 = r0.messages;
        r7 = r76 + 1;
        r0 = r69;
        r8 = r0.messages;
        r8 = r8.size();
        r6 = r6.subList(r7, r8);
        r4.addAll(r6);
        r26 = 0;
    L_0x2f67:
        r0 = r141;
        r4 = r0.messages;
        r4 = r4.size();
        r0 = r26;
        if (r0 >= r4) goto L_0x2f91;
    L_0x2f73:
        r0 = r141;
        r4 = r0.messages;
        r0 = r26;
        r4 = r4.get(r0);
        r4 = (org.telegram.messenger.MessageObject) r4;
        r0 = r141;
        r6 = r0.groupId;
        r4.localGroupId = r6;
        r0 = r69;
        r4 = r0.messages;
        r6 = r76 + 1;
        r4.remove(r6);
        r26 = r26 + 1;
        goto L_0x2f67;
    L_0x2f91:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r0 = r141;
        r6 = r0.groupId;
        r0 = r141;
        r4.put(r6, r0);
        r0 = r141;
        r4 = r0.messages;
        r0 = r141;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -1;
        r102 = r4.get(r6);
        r102 = (org.telegram.messenger.MessageObject) r102;
        r141.calculate();
    L_0x2fb5:
        r0 = r69;
        r4 = r0.messages;
        r0 = r76;
        r4.remove(r0);
        r0 = r69;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x2ffe;
    L_0x2fc8:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r0 = r69;
        r6 = r0.groupId;
        r4.remove(r6);
    L_0x2fd3:
        r0 = r99;
        r4 = r0.media;
        r4 = r4.ttl_seconds;
        if (r4 == 0) goto L_0x303d;
    L_0x2fdb:
        r0 = r99;
        r4 = r0.media;
        r4 = r4.photo;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_photoEmpty;
        if (r4 != 0) goto L_0x2fef;
    L_0x2fe5:
        r0 = r99;
        r4 = r0.media;
        r4 = r4.document;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_documentEmpty;
        if (r4 == 0) goto L_0x303d;
    L_0x2fef:
        r62.setType();
        r0 = r161;
        r4 = r0.chatAdapter;
        r6 = 0;
        r0 = r62;
        r4.updateRowWithMessageObject(r0, r6);
        goto L_0x0067;
    L_0x2ffe:
        if (r102 != 0) goto L_0x3014;
    L_0x3000:
        r0 = r69;
        r4 = r0.messages;
        r0 = r69;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -1;
        r102 = r4.get(r6);
        r102 = (org.telegram.messenger.MessageObject) r102;
    L_0x3014:
        r69.calculate();
        r0 = r161;
        r4 = r0.messages;
        r0 = r102;
        r78 = r4.indexOf(r0);
        if (r78 < 0) goto L_0x2fd3;
    L_0x3023:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x2fd3;
    L_0x3029:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.messagesStartRow;
        r6 = r6 + r78;
        r0 = r148;
        r4.notifyItemRangeChanged(r6, r0);
        goto L_0x2fd3;
    L_0x303d:
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x3042:
        r4 = org.telegram.messenger.NotificationCenter.replaceMessagesObjects;
        r0 = r162;
        if (r0 != r4) goto L_0x3384;
    L_0x3048:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 == 0) goto L_0x3061;
    L_0x3059:
        r0 = r161;
        r6 = r0.mergeDialogId;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x3061:
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x3206;
    L_0x3069:
        r90 = 0;
    L_0x306b:
        r4 = 1;
        r104 = r164[r4];
        r104 = (java.util.ArrayList) r104;
        r112 = 0;
        r18 = 0;
    L_0x3074:
        r4 = r104.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x3313;
    L_0x307c:
        r0 = r104;
        r1 = r18;
        r102 = r0.get(r1);
        r102 = (org.telegram.messenger.MessageObject) r102;
        r0 = r161;
        r4 = r0.messagesDict;
        r4 = r4[r90];
        r6 = r102.getId();
        r120 = r4.get(r6);
        r120 = (org.telegram.messenger.MessageObject) r120;
        r0 = r161;
        r4 = r0.pinnedMessageObject;
        if (r4 == 0) goto L_0x30b6;
    L_0x309c:
        r0 = r161;
        r4 = r0.pinnedMessageObject;
        r4 = r4.getId();
        r6 = r102.getId();
        if (r4 != r6) goto L_0x30b6;
    L_0x30aa:
        r0 = r102;
        r1 = r161;
        r1.pinnedMessageObject = r0;
        r4 = 1;
        r0 = r161;
        r0.updatePinnedMessageView(r4);
    L_0x30b6:
        if (r120 == 0) goto L_0x3284;
    L_0x30b8:
        r0 = r102;
        r4 = r0.type;
        if (r4 < 0) goto L_0x3221;
    L_0x30be:
        r0 = r120;
        r4 = r0.replyMessageObject;
        if (r4 == 0) goto L_0x30dc;
    L_0x30c4:
        r0 = r120;
        r4 = r0.replyMessageObject;
        r0 = r102;
        r0.replyMessageObject = r4;
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionGameScore;
        if (r4 == 0) goto L_0x320a;
    L_0x30d6:
        r4 = 0;
        r0 = r102;
        r0.generateGameMessageText(r4);
    L_0x30dc:
        r4 = r120.isEditing();
        if (r4 != 0) goto L_0x310c;
    L_0x30e2:
        r4 = r120.getFileName();
        r6 = r102.getFileName();
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x321c;
    L_0x30f0:
        r0 = r102;
        r4 = r0.messageOwner;
        r0 = r120;
        r6 = r0.messageOwner;
        r6 = r6.attachPath;
        r4.attachPath = r6;
        r0 = r120;
        r4 = r0.attachPathExists;
        r0 = r102;
        r0.attachPathExists = r4;
        r0 = r120;
        r4 = r0.mediaExists;
        r0 = r102;
        r0.mediaExists = r4;
    L_0x310c:
        r0 = r161;
        r4 = r0.messagesDict;
        r4 = r4[r90];
        r6 = r120.getId();
        r0 = r102;
        r4.put(r6, r0);
    L_0x311b:
        r0 = r161;
        r4 = r0.messages;
        r0 = r120;
        r78 = r4.indexOf(r0);
        if (r78 < 0) goto L_0x3284;
    L_0x3127:
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r120;
        r6 = r0.dateKey;
        r54 = r4.get(r6);
        r54 = (java.util.ArrayList) r54;
        r79 = -1;
        if (r54 == 0) goto L_0x3141;
    L_0x3139:
        r0 = r54;
        r1 = r120;
        r79 = r0.indexOf(r1);
    L_0x3141:
        r6 = r120.getGroupId();
        r8 = 0;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x3251;
    L_0x314b:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r6 = r120.getGroupId();
        r69 = r4.get(r6);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
        if (r69 == 0) goto L_0x3251;
    L_0x315b:
        r0 = r69;
        r4 = r0.messages;
        r0 = r120;
        r76 = r4.indexOf(r0);
        if (r76 < 0) goto L_0x3251;
    L_0x3167:
        r6 = r120.getGroupId();
        r8 = r102.getGroupId();
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x3180;
    L_0x3173:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r6 = r102.getGroupId();
        r0 = r69;
        r4.put(r6, r0);
    L_0x3180:
        r0 = r102;
        r4 = r0.photoThumbs;
        if (r4 == 0) goto L_0x3190;
    L_0x3186:
        r0 = r102;
        r4 = r0.photoThumbs;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x3288;
    L_0x3190:
        if (r112 != 0) goto L_0x3197;
    L_0x3192:
        r112 = new android.util.LongSparseArray;
        r112.<init>();
    L_0x3197:
        r0 = r69;
        r6 = r0.groupId;
        r0 = r112;
        r1 = r69;
        r0.put(r6, r1);
        if (r76 <= 0) goto L_0x3248;
    L_0x31a4:
        r0 = r69;
        r4 = r0.messages;
        r4 = r4.size();
        r4 = r4 + -1;
        r0 = r76;
        if (r0 >= r4) goto L_0x3248;
    L_0x31b2:
        r141 = new org.telegram.messenger.MessageObject$GroupedMessages;
        r141.<init>();
        r4 = org.telegram.messenger.Utilities.random;
        r6 = r4.nextLong();
        r0 = r141;
        r0.groupId = r6;
        r0 = r141;
        r4 = r0.messages;
        r0 = r69;
        r6 = r0.messages;
        r7 = r76 + 1;
        r0 = r69;
        r8 = r0.messages;
        r8 = r8.size();
        r6 = r6.subList(r7, r8);
        r4.addAll(r6);
        r26 = 0;
    L_0x31dc:
        r0 = r141;
        r4 = r0.messages;
        r4 = r4.size();
        r0 = r26;
        if (r0 >= r4) goto L_0x3230;
    L_0x31e8:
        r0 = r141;
        r4 = r0.messages;
        r0 = r26;
        r4 = r4.get(r0);
        r4 = (org.telegram.messenger.MessageObject) r4;
        r0 = r141;
        r6 = r0.groupId;
        r4.localGroupId = r6;
        r0 = r69;
        r4 = r0.messages;
        r6 = r76 + 1;
        r4.remove(r6);
        r26 = r26 + 1;
        goto L_0x31dc;
    L_0x3206:
        r90 = 1;
        goto L_0x306b;
    L_0x320a:
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionPaymentSent;
        if (r4 == 0) goto L_0x30dc;
    L_0x3214:
        r4 = 0;
        r0 = r102;
        r0.generatePaymentSentMessageText(r4);
        goto L_0x30dc;
    L_0x321c:
        r102.checkMediaExistance();
        goto L_0x310c;
    L_0x3221:
        r0 = r161;
        r4 = r0.messagesDict;
        r4 = r4[r90];
        r6 = r120.getId();
        r4.remove(r6);
        goto L_0x311b;
    L_0x3230:
        r0 = r141;
        r6 = r0.groupId;
        r0 = r112;
        r1 = r141;
        r0.put(r6, r1);
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r0 = r141;
        r6 = r0.groupId;
        r0 = r141;
        r4.put(r6, r0);
    L_0x3248:
        r0 = r69;
        r4 = r0.messages;
        r0 = r76;
        r4.remove(r0);
    L_0x3251:
        r0 = r102;
        r4 = r0.type;
        if (r4 < 0) goto L_0x32bf;
    L_0x3257:
        r0 = r161;
        r4 = r0.messages;
        r0 = r78;
        r1 = r102;
        r4.set(r0, r1);
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x3279;
    L_0x3268:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.messagesStartRow;
        r6 = r6 + r78;
        r4.updateRowAtPosition(r6);
    L_0x3279:
        if (r79 < 0) goto L_0x3284;
    L_0x327b:
        r0 = r54;
        r1 = r79;
        r2 = r102;
        r0.set(r1, r2);
    L_0x3284:
        r18 = r18 + 1;
        goto L_0x3074;
    L_0x3288:
        r0 = r69;
        r4 = r0.messages;
        r0 = r76;
        r1 = r102;
        r4.set(r0, r1);
        r0 = r69;
        r4 = r0.positions;
        r0 = r120;
        r123 = r4.remove(r0);
        r123 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r123;
        if (r123 == 0) goto L_0x32ac;
    L_0x32a1:
        r0 = r69;
        r4 = r0.positions;
        r0 = r102;
        r1 = r123;
        r4.put(r0, r1);
    L_0x32ac:
        if (r112 != 0) goto L_0x32b3;
    L_0x32ae:
        r112 = new android.util.LongSparseArray;
        r112.<init>();
    L_0x32b3:
        r0 = r69;
        r6 = r0.groupId;
        r0 = r112;
        r1 = r69;
        r0.put(r6, r1);
        goto L_0x3251;
    L_0x32bf:
        r0 = r161;
        r4 = r0.messages;
        r0 = r78;
        r4.remove(r0);
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x32df;
    L_0x32ce:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.messagesStartRow;
        r6 = r6 + r78;
        r4.notifyItemRemoved(r6);
    L_0x32df:
        if (r79 < 0) goto L_0x3284;
    L_0x32e1:
        r0 = r54;
        r1 = r79;
        r0.remove(r1);
        r4 = r54.isEmpty();
        if (r4 == 0) goto L_0x3284;
    L_0x32ee:
        r0 = r161;
        r4 = r0.messagesByDays;
        r0 = r120;
        r6 = r0.dateKey;
        r4.remove(r6);
        r0 = r161;
        r4 = r0.messages;
        r0 = r78;
        r4.remove(r0);
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.messagesStartRow;
        r4.notifyItemRemoved(r6);
        goto L_0x3284;
    L_0x3313:
        if (r112 == 0) goto L_0x0067;
    L_0x3315:
        r26 = 0;
    L_0x3317:
        r4 = r112.size();
        r0 = r26;
        if (r0 >= r4) goto L_0x0067;
    L_0x331f:
        r0 = r112;
        r1 = r26;
        r69 = r0.valueAt(r1);
        r69 = (org.telegram.messenger.MessageObject.GroupedMessages) r69;
        r0 = r69;
        r4 = r0.messages;
        r4 = r4.isEmpty();
        if (r4 == 0) goto L_0x3341;
    L_0x3333:
        r0 = r161;
        r4 = r0.groupedMessagesMap;
        r0 = r69;
        r6 = r0.groupId;
        r4.remove(r6);
    L_0x333e:
        r26 = r26 + 1;
        goto L_0x3317;
    L_0x3341:
        r69.calculate();
        r0 = r69;
        r4 = r0.messages;
        r0 = r69;
        r6 = r0.messages;
        r6 = r6.size();
        r6 = r6 + -1;
        r102 = r4.get(r6);
        r102 = (org.telegram.messenger.MessageObject) r102;
        r0 = r161;
        r4 = r0.messages;
        r0 = r102;
        r78 = r4.indexOf(r0);
        if (r78 < 0) goto L_0x333e;
    L_0x3364:
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x333e;
    L_0x336a:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.messagesStartRow;
        r6 = r6 + r78;
        r0 = r69;
        r7 = r0.messages;
        r7 = r7.size();
        r4.notifyItemRangeChanged(r6, r7);
        goto L_0x333e;
    L_0x3384:
        r4 = org.telegram.messenger.NotificationCenter.notificationsSettingsUpdated;
        r0 = r162;
        if (r0 != r4) goto L_0x339c;
    L_0x338a:
        r161.updateTitleIcons();
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x0067;
    L_0x3397:
        r161.updateBottomOverlay();
        goto L_0x0067;
    L_0x339c:
        r4 = org.telegram.messenger.NotificationCenter.didLoadedReplyMessages;
        r0 = r162;
        if (r0 != r4) goto L_0x33b8;
    L_0x33a2:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x33b3:
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x33b8:
        r4 = org.telegram.messenger.NotificationCenter.didLoadedPinnedMessage;
        r0 = r162;
        if (r0 != r4) goto L_0x33f4;
    L_0x33be:
        r4 = 0;
        r99 = r164[r4];
        r99 = (org.telegram.messenger.MessageObject) r99;
        r6 = r99.getDialogId();
        r0 = r161;
        r8 = r0.dialog_id;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x33cf:
        r0 = r161;
        r4 = r0.info;
        if (r4 == 0) goto L_0x0067;
    L_0x33d5:
        r0 = r161;
        r4 = r0.info;
        r4 = r4.pinned_msg_id;
        r6 = r99.getId();
        if (r4 != r6) goto L_0x0067;
    L_0x33e1:
        r0 = r99;
        r1 = r161;
        r1.pinnedMessageObject = r0;
        r4 = 0;
        r0 = r161;
        r0.loadingPinnedMessage = r4;
        r4 = 1;
        r0 = r161;
        r0.updatePinnedMessageView(r4);
        goto L_0x0067;
    L_0x33f4:
        r4 = org.telegram.messenger.NotificationCenter.didReceivedWebpages;
        r0 = r162;
        if (r0 != r4) goto L_0x3472;
    L_0x33fa:
        r4 = 0;
        r25 = r164[r4];
        r25 = (java.util.ArrayList) r25;
        r151 = 0;
        r18 = 0;
    L_0x3403:
        r4 = r25.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x346b;
    L_0x340b:
        r0 = r25;
        r1 = r18;
        r99 = r0.get(r1);
        r99 = (org.telegram.tgnet.TLRPC.Message) r99;
        r56 = org.telegram.messenger.MessageObject.getDialogId(r99);
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 == 0) goto L_0x342c;
    L_0x3421:
        r0 = r161;
        r6 = r0.mergeDialogId;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 == 0) goto L_0x342c;
    L_0x3429:
        r18 = r18 + 1;
        goto L_0x3403;
    L_0x342c:
        r0 = r161;
        r6 = r0.messagesDict;
        r0 = r161;
        r8 = r0.dialog_id;
        r4 = (r56 > r8 ? 1 : (r56 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x3469;
    L_0x3438:
        r4 = 0;
    L_0x3439:
        r4 = r6[r4];
        r0 = r99;
        r6 = r0.id;
        r47 = r4.get(r6);
        r47 = (org.telegram.messenger.MessageObject) r47;
        if (r47 == 0) goto L_0x3429;
    L_0x3447:
        r0 = r47;
        r4 = r0.messageOwner;
        r6 = new org.telegram.tgnet.TLRPC$TL_messageMediaWebPage;
        r6.<init>();
        r4.media = r6;
        r0 = r47;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r0 = r99;
        r6 = r0.media;
        r6 = r6.webpage;
        r4.webpage = r6;
        r4 = 1;
        r0 = r47;
        r0.generateThumbs(r4);
        r151 = 1;
        goto L_0x3429;
    L_0x3469:
        r4 = 1;
        goto L_0x3439;
    L_0x346b:
        if (r151 == 0) goto L_0x0067;
    L_0x346d:
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x3472:
        r4 = org.telegram.messenger.NotificationCenter.didReceivedWebpagesInUpdates;
        r0 = r162;
        if (r0 != r4) goto L_0x34bb;
    L_0x3478:
        r0 = r161;
        r4 = r0.foundWebPage;
        if (r4 == 0) goto L_0x0067;
    L_0x347e:
        r4 = 0;
        r73 = r164[r4];
        r73 = (android.util.LongSparseArray) r73;
        r18 = 0;
    L_0x3485:
        r4 = r73.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x0067;
    L_0x348d:
        r0 = r73;
        r1 = r18;
        r158 = r0.valueAt(r1);
        r158 = (org.telegram.tgnet.TLRPC.WebPage) r158;
        r0 = r158;
        r6 = r0.id;
        r0 = r161;
        r4 = r0.foundWebPage;
        r8 = r4.id;
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x34b8;
    L_0x34a5:
        r0 = r158;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_webPageEmpty;
        if (r4 != 0) goto L_0x34b6;
    L_0x34ab:
        r4 = 1;
    L_0x34ac:
        r6 = 0;
        r0 = r161;
        r1 = r158;
        r0.showFieldPanelForWebPage(r4, r1, r6);
        goto L_0x0067;
    L_0x34b6:
        r4 = 0;
        goto L_0x34ac;
    L_0x34b8:
        r18 = r18 + 1;
        goto L_0x3485;
    L_0x34bb:
        r4 = org.telegram.messenger.NotificationCenter.messagesReadContent;
        r0 = r162;
        if (r0 != r4) goto L_0x356e;
    L_0x34c1:
        r4 = 0;
        r23 = r164[r4];
        r23 = (java.util.ArrayList) r23;
        r151 = 0;
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x3506;
    L_0x34d2:
        r0 = r161;
        r4 = r0.currentChat;
        r0 = r4.id;
        r45 = r0;
    L_0x34da:
        r18 = 0;
    L_0x34dc:
        r4 = r23.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x3567;
    L_0x34e4:
        r0 = r23;
        r1 = r18;
        r4 = r0.get(r1);
        r4 = (java.lang.Long) r4;
        r106 = r4.longValue();
        r4 = 32;
        r6 = r106 >> r4;
        r0 = (int) r6;
        r36 = r0;
        if (r36 >= 0) goto L_0x34fd;
    L_0x34fb:
        r36 = 0;
    L_0x34fd:
        r0 = r36;
        r1 = r45;
        if (r0 == r1) goto L_0x3509;
    L_0x3503:
        r18 = r18 + 1;
        goto L_0x34dc;
    L_0x3506:
        r45 = 0;
        goto L_0x34da;
    L_0x3509:
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r0 = r106;
        r6 = (int) r0;
        r47 = r4.get(r6);
        r47 = (org.telegram.messenger.MessageObject) r47;
        if (r47 == 0) goto L_0x3503;
    L_0x351b:
        r47.setContentIsRead();
        r151 = 1;
        r0 = r47;
        r4 = r0.messageOwner;
        r4 = r4.mentioned;
        if (r4 == 0) goto L_0x3503;
    L_0x3528:
        r0 = r161;
        r4 = r0.newMentionsCount;
        r4 = r4 + -1;
        r0 = r161;
        r0.newMentionsCount = r4;
        r0 = r161;
        r4 = r0.newMentionsCount;
        if (r4 > 0) goto L_0x354a;
    L_0x3538:
        r4 = 0;
        r0 = r161;
        r0.newMentionsCount = r4;
        r4 = 1;
        r0 = r161;
        r0.hasAllMentionsLocal = r4;
        r4 = 0;
        r6 = 1;
        r0 = r161;
        r0.showMentiondownButton(r4, r6);
        goto L_0x3503;
    L_0x354a:
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        r6 = "%d";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r161;
        r9 = r0.newMentionsCount;
        r9 = java.lang.Integer.valueOf(r9);
        r7[r8] = r9;
        r6 = java.lang.String.format(r6, r7);
        r4.setText(r6);
        goto L_0x3503;
    L_0x3567:
        if (r151 == 0) goto L_0x0067;
    L_0x3569:
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x356e:
        r4 = org.telegram.messenger.NotificationCenter.botInfoDidLoaded;
        r0 = r162;
        if (r0 != r4) goto L_0x360e;
    L_0x3574:
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r71 = r4.intValue();
        r0 = r161;
        r4 = r0.classGuid;
        r0 = r71;
        if (r4 != r0) goto L_0x0067;
    L_0x3585:
        r4 = 0;
        r80 = r164[r4];
        r80 = (org.telegram.tgnet.TLRPC.BotInfo) r80;
        r0 = r161;
        r4 = r0.currentEncryptedChat;
        if (r4 != 0) goto L_0x3609;
    L_0x3590:
        r0 = r80;
        r4 = r0.commands;
        r4 = r4.isEmpty();
        if (r4 != 0) goto L_0x35a9;
    L_0x359a:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 != 0) goto L_0x35a9;
    L_0x35a4:
        r4 = 1;
        r0 = r161;
        r0.hasBotsCommands = r4;
    L_0x35a9:
        r0 = r161;
        r4 = r0.botInfo;
        r0 = r80;
        r6 = r0.user_id;
        r0 = r80;
        r4.put(r6, r0);
        r0 = r161;
        r4 = r0.chatAdapter;
        if (r4 == 0) goto L_0x35cb;
    L_0x35bc:
        r0 = r161;
        r4 = r0.chatAdapter;
        r0 = r161;
        r6 = r0.chatAdapter;
        r6 = r6.botInfoRow;
        r4.notifyItemChanged(r6);
    L_0x35cb:
        r0 = r161;
        r4 = r0.mentionsAdapter;
        if (r4 == 0) goto L_0x35f4;
    L_0x35d1:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x35e9;
    L_0x35db:
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x35f4;
    L_0x35e1:
        r0 = r161;
        r4 = r0.currentChat;
        r4 = r4.megagroup;
        if (r4 == 0) goto L_0x35f4;
    L_0x35e9:
        r0 = r161;
        r4 = r0.mentionsAdapter;
        r0 = r161;
        r6 = r0.botInfo;
        r4.setBotInfo(r6);
    L_0x35f4:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x3609;
    L_0x35fa:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r0 = r161;
        r6 = r0.botsCount;
        r0 = r161;
        r7 = r0.hasBotsCommands;
        r4.setBotsCount(r6, r7);
    L_0x3609:
        r161.updateBotButtons();
        goto L_0x0067;
    L_0x360e:
        r4 = org.telegram.messenger.NotificationCenter.botKeyboardDidLoaded;
        r0 = r162;
        if (r0 != r4) goto L_0x3677;
    L_0x3614:
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r8 = r4.longValue();
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x3625:
        r4 = 0;
        r99 = r164[r4];
        r99 = (org.telegram.tgnet.TLRPC.Message) r99;
        if (r99 == 0) goto L_0x3647;
    L_0x362c:
        r0 = r161;
        r4 = r0.userBlocked;
        if (r4 != 0) goto L_0x3647;
    L_0x3632:
        r4 = new org.telegram.messenger.MessageObject;
        r0 = r161;
        r6 = r0.currentAccount;
        r7 = 0;
        r0 = r99;
        r4.<init>(r6, r0, r7);
        r0 = r161;
        r0.botButtons = r4;
        r161.checkBotKeyboard();
        goto L_0x0067;
    L_0x3647:
        r4 = 0;
        r0 = r161;
        r0.botButtons = r4;
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x0067;
    L_0x3652:
        r0 = r161;
        r4 = r0.replyingMessageObject;
        if (r4 == 0) goto L_0x366a;
    L_0x3658:
        r0 = r161;
        r4 = r0.botReplyButtons;
        r0 = r161;
        r6 = r0.replyingMessageObject;
        if (r4 != r6) goto L_0x366a;
    L_0x3662:
        r4 = 0;
        r0 = r161;
        r0.botReplyButtons = r4;
        r161.hideFieldPanel();
    L_0x366a:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r0 = r161;
        r6 = r0.botButtons;
        r4.setButtons(r6);
        goto L_0x0067;
    L_0x3677:
        r4 = org.telegram.messenger.NotificationCenter.chatSearchResultsAvailable;
        r0 = r162;
        if (r0 != r4) goto L_0x36e3;
    L_0x367d:
        r0 = r161;
        r6 = r0.classGuid;
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r4 = r4.intValue();
        if (r6 != r4) goto L_0x0067;
    L_0x368c:
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r5 = r4.intValue();
        r4 = 3;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        if (r5 == 0) goto L_0x36b1;
    L_0x36a0:
        r6 = 0;
        r7 = 1;
        r0 = r161;
        r8 = r0.dialog_id;
        r4 = (r56 > r8 ? 1 : (r56 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x36e1;
    L_0x36aa:
        r8 = 0;
    L_0x36ab:
        r9 = 0;
        r4 = r161;
        r4.scrollToMessageId(r5, r6, r7, r8, r9);
    L_0x36b1:
        r4 = 2;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r6 = r4.intValue();
        r4 = 4;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r7 = r4.intValue();
        r4 = 5;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r4 = r4.intValue();
        r0 = r161;
        r0.updateSearchButtons(r6, r7, r4);
        r0 = r161;
        r4 = r0.searchItem;
        if (r4 == 0) goto L_0x0067;
    L_0x36d7:
        r0 = r161;
        r4 = r0.searchItem;
        r6 = 0;
        r4.setShowSearchProgress(r6);
        goto L_0x0067;
    L_0x36e1:
        r8 = 1;
        goto L_0x36ab;
    L_0x36e3:
        r4 = org.telegram.messenger.NotificationCenter.chatSearchResultsLoading;
        r0 = r162;
        if (r0 != r4) goto L_0x3708;
    L_0x36e9:
        r0 = r161;
        r6 = r0.classGuid;
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r4 = r4.intValue();
        if (r6 != r4) goto L_0x0067;
    L_0x36f8:
        r0 = r161;
        r4 = r0.searchItem;
        if (r4 == 0) goto L_0x0067;
    L_0x36fe:
        r0 = r161;
        r4 = r0.searchItem;
        r6 = 1;
        r4.setShowSearchProgress(r6);
        goto L_0x0067;
    L_0x3708:
        r4 = org.telegram.messenger.NotificationCenter.didUpdatedMessagesViews;
        r0 = r162;
        if (r0 != r4) goto L_0x3769;
    L_0x370e:
        r4 = 0;
        r37 = r164[r4];
        r37 = (android.util.SparseArray) r37;
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (int) r6;
        r0 = r37;
        r22 = r0.get(r4);
        r22 = (android.util.SparseIntArray) r22;
        if (r22 == 0) goto L_0x0067;
    L_0x3722:
        r151 = 0;
        r18 = 0;
    L_0x3726:
        r4 = r22.size();
        r0 = r18;
        if (r0 >= r4) goto L_0x3762;
    L_0x372e:
        r0 = r22;
        r1 = r18;
        r5 = r0.keyAt(r1);
        r0 = r161;
        r4 = r0.messagesDict;
        r6 = 0;
        r4 = r4[r6];
        r102 = r4.get(r5);
        r102 = (org.telegram.messenger.MessageObject) r102;
        if (r102 == 0) goto L_0x375f;
    L_0x3745:
        r0 = r22;
        r116 = r0.get(r5);
        r0 = r102;
        r4 = r0.messageOwner;
        r4 = r4.views;
        r0 = r116;
        if (r0 <= r4) goto L_0x375f;
    L_0x3755:
        r0 = r102;
        r4 = r0.messageOwner;
        r0 = r116;
        r4.views = r0;
        r151 = 1;
    L_0x375f:
        r18 = r18 + 1;
        goto L_0x3726;
    L_0x3762:
        if (r151 == 0) goto L_0x0067;
    L_0x3764:
        r161.updateVisibleRows();
        goto L_0x0067;
    L_0x3769:
        r4 = org.telegram.messenger.NotificationCenter.peerSettingsDidLoaded;
        r0 = r162;
        if (r0 != r4) goto L_0x3785;
    L_0x376f:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x3780:
        r161.updateSpamView();
        goto L_0x0067;
    L_0x3785:
        r4 = org.telegram.messenger.NotificationCenter.newDraftReceived;
        r0 = r162;
        if (r0 != r4) goto L_0x37a4;
    L_0x378b:
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r56 = r4.longValue();
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = (r56 > r6 ? 1 : (r56 == r6 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x379c:
        r4 = 1;
        r0 = r161;
        r0.applyDraftMaybe(r4);
        goto L_0x0067;
    L_0x37a4:
        r4 = org.telegram.messenger.NotificationCenter.userInfoDidLoaded;
        r0 = r162;
        if (r0 != r4) goto L_0x37e8;
    L_0x37aa:
        r4 = 0;
        r144 = r164[r4];
        r144 = (java.lang.Integer) r144;
        r0 = r161;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x0067;
    L_0x37b5:
        r0 = r161;
        r4 = r0.currentUser;
        r4 = r4.id;
        r6 = r144.intValue();
        if (r4 != r6) goto L_0x0067;
    L_0x37c1:
        r4 = 1;
        r156 = r164[r4];
        r156 = (org.telegram.tgnet.TLRPC.TL_userFull) r156;
        r0 = r161;
        r4 = r0.headerItem;
        if (r4 == 0) goto L_0x0067;
    L_0x37cc:
        r0 = r156;
        r4 = r0.phone_calls_available;
        if (r4 == 0) goto L_0x37dd;
    L_0x37d2:
        r0 = r161;
        r4 = r0.headerItem;
        r6 = 32;
        r4.showSubItem(r6);
        goto L_0x0067;
    L_0x37dd:
        r0 = r161;
        r4 = r0.headerItem;
        r6 = 32;
        r4.hideSubItem(r6);
        goto L_0x0067;
    L_0x37e8:
        r4 = org.telegram.messenger.NotificationCenter.didSetNewWallpapper;
        r0 = r162;
        if (r0 != r4) goto L_0x383d;
    L_0x37ee:
        r0 = r161;
        r4 = r0.fragmentView;
        if (r4 == 0) goto L_0x0067;
    L_0x37f4:
        r0 = r161;
        r4 = r0.fragmentView;
        r4 = (org.telegram.ui.Components.SizeNotifierFrameLayout) r4;
        r6 = org.telegram.ui.ActionBar.Theme.getCachedWallpaper();
        r4.setBackgroundImage(r6);
        r0 = r161;
        r4 = r0.progressView2;
        r4 = r4.getBackground();
        r6 = org.telegram.ui.ActionBar.Theme.colorFilter;
        r4.setColorFilter(r6);
        r0 = r161;
        r4 = r0.emptyView;
        if (r4 == 0) goto L_0x3821;
    L_0x3814:
        r0 = r161;
        r4 = r0.emptyView;
        r4 = r4.getBackground();
        r6 = org.telegram.ui.ActionBar.Theme.colorFilter;
        r4.setColorFilter(r6);
    L_0x3821:
        r0 = r161;
        r4 = r0.bigEmptyView;
        if (r4 == 0) goto L_0x3834;
    L_0x3827:
        r0 = r161;
        r4 = r0.bigEmptyView;
        r4 = r4.getBackground();
        r6 = org.telegram.ui.ActionBar.Theme.colorFilter;
        r4.setColorFilter(r6);
    L_0x3834:
        r0 = r161;
        r4 = r0.chatListView;
        r4.invalidateViews();
        goto L_0x0067;
    L_0x383d:
        r4 = org.telegram.messenger.NotificationCenter.channelRightsUpdated;
        r0 = r162;
        if (r0 != r4) goto L_0x3875;
    L_0x3843:
        r4 = 0;
        r39 = r164[r4];
        r39 = (org.telegram.tgnet.TLRPC.Chat) r39;
        r0 = r161;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x0067;
    L_0x384e:
        r0 = r39;
        r4 = r0.id;
        r0 = r161;
        r6 = r0.currentChat;
        r6 = r6.id;
        if (r4 != r6) goto L_0x0067;
    L_0x385a:
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        if (r4 == 0) goto L_0x0067;
    L_0x3860:
        r0 = r39;
        r1 = r161;
        r1.currentChat = r0;
        r0 = r161;
        r4 = r0.chatActivityEnterView;
        r4.checkChannelRights();
        r161.checkRaiseSensors();
        r161.updateSecretStatus();
        goto L_0x0067;
    L_0x3875:
        r4 = org.telegram.messenger.NotificationCenter.updateMentionsCount;
        r0 = r162;
        if (r0 != r4) goto L_0x0067;
    L_0x387b:
        r0 = r161;
        r6 = r0.dialog_id;
        r4 = 0;
        r4 = r164[r4];
        r4 = (java.lang.Long) r4;
        r8 = r4.longValue();
        r4 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x0067;
    L_0x388c:
        r4 = 1;
        r4 = r164[r4];
        r4 = (java.lang.Integer) r4;
        r43 = r4.intValue();
        r0 = r161;
        r4 = r0.newMentionsCount;
        r0 = r43;
        if (r4 <= r0) goto L_0x0067;
    L_0x389d:
        r0 = r43;
        r1 = r161;
        r1.newMentionsCount = r0;
        r0 = r161;
        r4 = r0.newMentionsCount;
        if (r4 > 0) goto L_0x38bc;
    L_0x38a9:
        r4 = 0;
        r0 = r161;
        r0.newMentionsCount = r4;
        r4 = 1;
        r0 = r161;
        r0.hasAllMentionsLocal = r4;
        r4 = 0;
        r6 = 1;
        r0 = r161;
        r0.showMentiondownButton(r4, r6);
        goto L_0x0067;
    L_0x38bc:
        r0 = r161;
        r4 = r0.mentiondownButtonCounter;
        r6 = "%d";
        r7 = 1;
        r7 = new java.lang.Object[r7];
        r8 = 0;
        r0 = r161;
        r9 = r0.newMentionsCount;
        r9 = java.lang.Integer.valueOf(r9);
        r7[r8] = r9;
        r6 = java.lang.String.format(r6, r7);
        r4.setText(r6);
        goto L_0x0067;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ChatActivity.didReceivedNotification(int, int, java.lang.Object[]):void");
    }

    final /* synthetic */ void lambda$didReceivedNotification$49$ChatActivity() {
        if (this.parentLayout != null) {
            this.parentLayout.resumeDelayedFragmentAnimation();
        }
    }

    final /* synthetic */ void lambda$didReceivedNotification$51$ChatActivity(BaseFragment lastFragment, Bundle bundle, int channel_id) {
        ActionBarLayout parentLayout = this.parentLayout;
        if (lastFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(lastFragment, NotificationCenter.closeChats);
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        parentLayout.presentFragment(new ChatActivity(bundle), true);
        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$70(this, channel_id), 1000);
    }

    final /* synthetic */ void lambda$null$50$ChatActivity(int channel_id) {
        MessagesController.getInstance(this.currentAccount).loadFullChat(channel_id, 0, true);
    }

    final /* synthetic */ void lambda$didReceivedNotification$53$ChatActivity(BaseFragment lastFragment, Bundle bundle, int channel_id) {
        ActionBarLayout parentLayout = this.parentLayout;
        if (lastFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(lastFragment, NotificationCenter.closeChats);
        }
        NotificationCenter.getInstance(this.currentAccount).postNotificationName(NotificationCenter.closeChats, new Object[0]);
        parentLayout.presentFragment(new ChatActivity(bundle), true);
        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$69(this, channel_id), 1000);
    }

    final /* synthetic */ void lambda$null$52$ChatActivity(int channel_id) {
        MessagesController.getInstance(this.currentAccount).loadFullChat(channel_id, 0, true);
    }

    private void checkSecretMessageForLocation(MessageObject messageObject) {
        if (messageObject.type == 4 && !this.locationAlertShown && !SharedConfig.isSecretMapPreviewSet()) {
            this.locationAlertShown = true;
            AlertsCreator.showSecretLocationAlert(getParentActivity(), this.currentAccount, new ChatActivity$$Lambda$41(this), true);
        }
    }

    final /* synthetic */ void lambda$checkSecretMessageForLocation$54$ChatActivity() {
        int count = this.chatListView.getChildCount();
        for (int a = 0; a < count; a++) {
            View view = this.chatListView.getChildAt(a);
            if (view instanceof ChatMessageCell) {
                ChatMessageCell cell = (ChatMessageCell) view;
                if (cell.getMessageObject().type == 4) {
                    cell.forceResetMessageObject();
                }
            }
        }
    }

    public boolean processSwitchButton(TL_keyboardButtonSwitchInline button) {
        if (this.inlineReturn == 0 || button.same_peer || this.parentLayout == null) {
            return false;
        }
        String query = "@" + this.currentUser.username + " " + button.query;
        if (this.inlineReturn == this.dialog_id) {
            this.inlineReturn = 0;
            this.chatActivityEnterView.setFieldText(query);
        } else {
            DataQuery.getInstance(this.currentAccount).saveDraft(this.inlineReturn, query, null, null, false);
            if (this.parentLayout.fragmentsStack.size() > 1) {
                BaseFragment prevFragment = (BaseFragment) this.parentLayout.fragmentsStack.get(this.parentLayout.fragmentsStack.size() - 2);
                if ((prevFragment instanceof ChatActivity) && ((ChatActivity) prevFragment).dialog_id == this.inlineReturn) {
                    finishFragment();
                } else {
                    Bundle bundle = new Bundle();
                    int lower_part = (int) this.inlineReturn;
                    int high_part = (int) (this.inlineReturn >> 32);
                    if (lower_part == 0) {
                        bundle.putInt("enc_id", high_part);
                    } else if (lower_part > 0) {
                        bundle.putInt("user_id", lower_part);
                    } else if (lower_part < 0) {
                        bundle.putInt("chat_id", -lower_part);
                    }
                    presentFragment(new ChatActivity(bundle), true);
                }
            }
        }
        return true;
    }

    private void updateSearchButtons(int mask, int num, int count) {
        float f = 1.0f;
        if (this.searchUpButton != null) {
            boolean z;
            float f2;
            this.searchUpButton.setEnabled((mask & 1) != 0);
            ImageView imageView = this.searchDownButton;
            if ((mask & 2) != 0) {
                z = true;
            } else {
                z = false;
            }
            imageView.setEnabled(z);
            imageView = this.searchUpButton;
            if (this.searchUpButton.isEnabled()) {
                f2 = 1.0f;
            } else {
                f2 = 0.5f;
            }
            imageView.setAlpha(f2);
            ImageView imageView2 = this.searchDownButton;
            if (!this.searchDownButton.isEnabled()) {
                f = 0.5f;
            }
            imageView2.setAlpha(f);
            if (count < 0) {
                this.searchCountText.setText(TtmlNode.ANONYMOUS_REGION_ID);
            } else if (count == 0) {
                this.searchCountText.setText(LocaleController.getString("NoResult", R.string.NoResult));
            } else {
                this.searchCountText.setText(LocaleController.formatString("Of", R.string.Of, Integer.valueOf(num + 1), Integer.valueOf(count)));
            }
        }
    }

    public boolean needDelayOpenAnimation() {
        return this.firstLoading;
    }

    public void onTransitionAnimationStart(boolean isOpen, boolean backward) {
        NotificationCenter.getInstance(this.currentAccount).setAllowedNotificationsDutingAnimation(new int[]{NotificationCenter.chatInfoDidLoaded, NotificationCenter.dialogsNeedReload, NotificationCenter.closeChats, NotificationCenter.messagesDidLoaded, NotificationCenter.botKeyboardDidLoaded});
        NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(true);
        if (isOpen) {
            this.openAnimationEnded = false;
        }
    }

    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        NotificationCenter.getInstance(this.currentAccount).setAnimationInProgress(false);
        if (isOpen) {
            this.openAnimationEnded = true;
            if (this.currentUser != null) {
                MessagesController.getInstance(this.currentAccount).loadFullUser(this.currentUser, this.classGuid, false);
            }
            if (VERSION.SDK_INT >= 21) {
                createChatAttachView();
            }
            if (this.chatActivityEnterView.hasRecordVideo() && !this.chatActivityEnterView.isSendButtonVisible()) {
                boolean isChannel = false;
                if (this.currentChat != null) {
                    if (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup) {
                        isChannel = false;
                    } else {
                        isChannel = true;
                    }
                }
                SharedPreferences preferences = MessagesController.getGlobalMainSettings();
                String key = isChannel ? "needShowRoundHintChannel" : "needShowRoundHint";
                if (preferences.getBoolean(key, true) && Utilities.random.nextFloat() < 0.2f) {
                    showVoiceHint(false, this.chatActivityEnterView.isInVideoMode());
                    preferences.edit().putBoolean(key, false).commit();
                }
            }
        }
    }

    protected void onDialogDismiss(Dialog dialog) {
        if (this.closeChatDialog != null && dialog == this.closeChatDialog) {
            MessagesController.getInstance(this.currentAccount).deleteDialog(this.dialog_id, 0);
            if (this.parentLayout == null || this.parentLayout.fragmentsStack.isEmpty() || this.parentLayout.fragmentsStack.get(this.parentLayout.fragmentsStack.size() - 1) == this) {
                finishFragment();
                return;
            }
            BaseFragment fragment = (BaseFragment) this.parentLayout.fragmentsStack.get(this.parentLayout.fragmentsStack.size() - 1);
            removeSelfFromStack();
            fragment.finishFragment();
        }
    }

    public boolean extendActionMode(Menu menu) {
        if (!PhotoViewer.hasInstance() || !PhotoViewer.getInstance().isVisible() ? this.chatActivityEnterView.getSelectionLength() == 0 || menu.findItem(16908321) == null : PhotoViewer.getInstance().getSelectiongLength() == 0 || menu.findItem(16908321) == null) {
            if (VERSION.SDK_INT >= 23) {
                menu.removeItem(16908341);
            }
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(LocaleController.getString("Bold", R.string.Bold));
            stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), 0, stringBuilder.length(), 33);
            menu.add(R.id.menu_groupbolditalic, R.id.menu_bold, 6, stringBuilder);
            stringBuilder = new SpannableStringBuilder(LocaleController.getString("Italic", R.string.Italic));
            stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf")), 0, stringBuilder.length(), 33);
            menu.add(R.id.menu_groupbolditalic, R.id.menu_italic, 7, stringBuilder);
            stringBuilder = new SpannableStringBuilder(LocaleController.getString("Mono", R.string.Mono));
            stringBuilder.setSpan(new TypefaceSpan(Typeface.MONOSPACE), 0, stringBuilder.length(), 33);
            menu.add(R.id.menu_groupbolditalic, R.id.menu_mono, 8, stringBuilder);
            menu.add(R.id.menu_groupbolditalic, R.id.menu_link, 9, LocaleController.getString("CreateLink", R.string.CreateLink));
            menu.add(R.id.menu_groupbolditalic, R.id.menu_regular, 10, LocaleController.getString("Regular", R.string.Regular));
        }
        return true;
    }

    private void updateBottomOverlay() {
        if (this.bottomOverlayChatText != null) {
            if (this.currentChat != null) {
                if (!ChatObject.isChannel(this.currentChat) || (this.currentChat instanceof TL_channelForbidden)) {
                    this.bottomOverlayChatText.setText(LocaleController.getString("DeleteThisGroup", R.string.DeleteThisGroup));
                } else if (ChatObject.isNotInChat(this.currentChat)) {
                    this.bottomOverlayChatText.setText(LocaleController.getString("ChannelJoin", R.string.ChannelJoin));
                } else if (MessagesController.getInstance(this.currentAccount).isDialogMuted(this.dialog_id)) {
                    this.bottomOverlayChatText.setText(LocaleController.getString("ChannelUnmute", R.string.ChannelUnmute));
                } else {
                    this.bottomOverlayChatText.setText(LocaleController.getString("ChannelMute", R.string.ChannelMute));
                }
            } else if (this.userBlocked) {
                if (this.currentUser.bot) {
                    this.bottomOverlayChatText.setText(LocaleController.getString("BotUnblock", R.string.BotUnblock));
                } else {
                    this.bottomOverlayChatText.setText(LocaleController.getString("Unblock", R.string.Unblock));
                }
                if (this.botButtons != null) {
                    this.botButtons = null;
                    if (this.chatActivityEnterView != null) {
                        if (this.replyingMessageObject != null && this.botReplyButtons == this.replyingMessageObject) {
                            this.botReplyButtons = null;
                            hideFieldPanel();
                        }
                        this.chatActivityEnterView.setButtons(this.botButtons, false);
                    }
                }
            } else if (this.botUser == null || !this.currentUser.bot) {
                this.bottomOverlayChatText.setText(LocaleController.getString("DeleteThisChat", R.string.DeleteThisChat));
            } else {
                this.bottomOverlayChatText.setText(LocaleController.getString("BotStart", R.string.BotStart));
                this.chatActivityEnterView.hidePopup(false);
                if (getParentActivity() != null) {
                    AndroidUtilities.hideKeyboard(getParentActivity().getCurrentFocus());
                }
            }
            if (this.inPreviewMode) {
                this.searchContainer.setVisibility(4);
                this.bottomOverlayChat.setVisibility(4);
                this.chatActivityEnterView.setFieldFocused(false);
                this.chatActivityEnterView.setVisibility(4);
            } else if (this.searchItem == null || this.searchItem.getVisibility() != 0) {
                this.searchContainer.setVisibility(4);
                if ((this.currentChat == null || (!ChatObject.isNotInChat(this.currentChat) && ChatObject.canWriteToChat(this.currentChat))) && (this.currentUser == null || !(UserObject.isDeleted(this.currentUser) || this.userBlocked))) {
                    if (this.botUser == null || !this.currentUser.bot) {
                        this.chatActivityEnterView.setVisibility(0);
                        this.bottomOverlayChat.setVisibility(4);
                    } else {
                        this.bottomOverlayChat.setVisibility(0);
                        this.chatActivityEnterView.setVisibility(4);
                    }
                    if (this.muteItem != null) {
                        this.muteItem.setVisibility(0);
                    }
                } else {
                    if (this.chatActivityEnterView.isEditingMessage()) {
                        this.chatActivityEnterView.setVisibility(0);
                        this.bottomOverlayChat.setVisibility(4);
                        this.chatActivityEnterView.setFieldFocused();
                        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$42(this), 100);
                    } else {
                        this.bottomOverlayChat.setVisibility(0);
                        this.chatActivityEnterView.setFieldFocused(false);
                        this.chatActivityEnterView.setVisibility(4);
                        this.chatActivityEnterView.closeKeyboard();
                    }
                    if (this.muteItem != null) {
                        this.muteItem.setVisibility(8);
                    }
                    this.attachItem.setVisibility(8);
                    this.editTextItem.setVisibility(8);
                    this.headerItem.setVisibility(0);
                }
                if (this.topViewWasVisible == 1) {
                    this.chatActivityEnterView.showTopView(false, false);
                    this.topViewWasVisible = 0;
                }
            } else {
                this.searchContainer.setVisibility(0);
                this.bottomOverlayChat.setVisibility(4);
                this.chatActivityEnterView.setFieldFocused(false);
                this.chatActivityEnterView.setVisibility(4);
                if (this.chatActivityEnterView.isTopViewVisible()) {
                    this.topViewWasVisible = 1;
                    this.chatActivityEnterView.hideTopView(false);
                } else {
                    this.topViewWasVisible = 2;
                }
            }
            checkRaiseSensors();
        }
    }

    final /* synthetic */ void lambda$updateBottomOverlay$55$ChatActivity() {
        this.chatActivityEnterView.openKeyboard();
    }

    public void showAlert(String name, String message) {
        if (this.alertView != null && name != null && message != null) {
            if (this.alertView.getTag() != null) {
                this.alertView.setTag(null);
                if (this.alertViewAnimator != null) {
                    this.alertViewAnimator.cancel();
                    this.alertViewAnimator = null;
                }
                this.alertView.setVisibility(0);
                this.alertViewAnimator = new AnimatorSet();
                AnimatorSet animatorSet = this.alertViewAnimator;
                Animator[] animatorArr = new Animator[1];
                animatorArr[0] = ObjectAnimator.ofFloat(this.alertView, "translationY", new float[]{0.0f});
                animatorSet.playTogether(animatorArr);
                this.alertViewAnimator.setDuration(200);
                this.alertViewAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (ChatActivity.this.alertViewAnimator != null && ChatActivity.this.alertViewAnimator.equals(animation)) {
                            ChatActivity.this.alertViewAnimator = null;
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (ChatActivity.this.alertViewAnimator != null && ChatActivity.this.alertViewAnimator.equals(animation)) {
                            ChatActivity.this.alertViewAnimator = null;
                        }
                    }
                });
                this.alertViewAnimator.start();
            }
            this.alertNameTextView.setText(name);
            this.alertTextView.setText(Emoji.replaceEmoji(message.replace('\n', ' '), this.alertTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
            if (this.hideAlertViewRunnable != null) {
                AndroidUtilities.cancelRunOnUIThread(this.hideAlertViewRunnable);
            }
            Runnable anonymousClass48 = new Runnable() {
                public void run() {
                    if (ChatActivity.this.hideAlertViewRunnable == this && ChatActivity.this.alertView.getTag() == null) {
                        ChatActivity.this.alertView.setTag(Integer.valueOf(1));
                        if (ChatActivity.this.alertViewAnimator != null) {
                            ChatActivity.this.alertViewAnimator.cancel();
                            ChatActivity.this.alertViewAnimator = null;
                        }
                        ChatActivity.this.alertViewAnimator = new AnimatorSet();
                        AnimatorSet access$19500 = ChatActivity.this.alertViewAnimator;
                        Animator[] animatorArr = new Animator[1];
                        animatorArr[0] = ObjectAnimator.ofFloat(ChatActivity.this.alertView, "translationY", new float[]{(float) (-AndroidUtilities.dp(50.0f))});
                        access$19500.playTogether(animatorArr);
                        ChatActivity.this.alertViewAnimator.setDuration(200);
                        ChatActivity.this.alertViewAnimator.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                if (ChatActivity.this.alertViewAnimator != null && ChatActivity.this.alertViewAnimator.equals(animation)) {
                                    ChatActivity.this.alertView.setVisibility(8);
                                    ChatActivity.this.alertViewAnimator = null;
                                }
                            }

                            public void onAnimationCancel(Animator animation) {
                                if (ChatActivity.this.alertViewAnimator != null && ChatActivity.this.alertViewAnimator.equals(animation)) {
                                    ChatActivity.this.alertViewAnimator = null;
                                }
                            }
                        });
                        ChatActivity.this.alertViewAnimator.start();
                    }
                }
            };
            this.hideAlertViewRunnable = anonymousClass48;
            AndroidUtilities.runOnUIThread(anonymousClass48, 3000);
        }
    }

    private void hidePinnedMessageView(boolean animated) {
        if (this.pinnedMessageView.getTag() == null) {
            this.pinnedMessageView.setTag(Integer.valueOf(1));
            if (this.pinnedMessageViewAnimator != null) {
                this.pinnedMessageViewAnimator.cancel();
                this.pinnedMessageViewAnimator = null;
            }
            if (animated) {
                this.pinnedMessageViewAnimator = new AnimatorSet();
                AnimatorSet animatorSet = this.pinnedMessageViewAnimator;
                Animator[] animatorArr = new Animator[1];
                animatorArr[0] = ObjectAnimator.ofFloat(this.pinnedMessageView, "translationY", new float[]{(float) (-AndroidUtilities.dp(50.0f))});
                animatorSet.playTogether(animatorArr);
                this.pinnedMessageViewAnimator.setDuration(200);
                this.pinnedMessageViewAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (ChatActivity.this.pinnedMessageViewAnimator != null && ChatActivity.this.pinnedMessageViewAnimator.equals(animation)) {
                            ChatActivity.this.pinnedMessageView.setVisibility(8);
                            ChatActivity.this.pinnedMessageViewAnimator = null;
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (ChatActivity.this.pinnedMessageViewAnimator != null && ChatActivity.this.pinnedMessageViewAnimator.equals(animation)) {
                            ChatActivity.this.pinnedMessageViewAnimator = null;
                        }
                    }
                });
                this.pinnedMessageViewAnimator.start();
                return;
            }
            this.pinnedMessageView.setTranslationY((float) (-AndroidUtilities.dp(50.0f)));
            this.pinnedMessageView.setVisibility(8);
        }
    }

    private void updatePinnedMessageView(boolean animated) {
        if (this.pinnedMessageView != null) {
            if (this.info != null) {
                if (!(this.pinnedMessageObject == null || this.info.pinned_msg_id == this.pinnedMessageObject.getId())) {
                    this.pinnedMessageObject = null;
                }
                if (this.info.pinned_msg_id != 0 && this.pinnedMessageObject == null) {
                    this.pinnedMessageObject = (MessageObject) this.messagesDict[0].get(this.info.pinned_msg_id);
                }
            }
            SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
            if (this.info == null || this.info.pinned_msg_id == 0 || this.info.pinned_msg_id == preferences.getInt("pin_" + this.dialog_id, 0) || (this.actionBar != null && (this.actionBar.isActionModeShowed() || this.actionBar.isSearchFieldVisible()))) {
                hidePinnedMessageView(animated);
            } else if (this.pinnedMessageObject != null) {
                if (this.pinnedMessageView.getTag() != null) {
                    this.pinnedMessageView.setTag(null);
                    if (this.pinnedMessageViewAnimator != null) {
                        this.pinnedMessageViewAnimator.cancel();
                        this.pinnedMessageViewAnimator = null;
                    }
                    if (animated) {
                        this.pinnedMessageView.setVisibility(0);
                        this.pinnedMessageViewAnimator = new AnimatorSet();
                        AnimatorSet animatorSet = this.pinnedMessageViewAnimator;
                        Animator[] animatorArr = new Animator[1];
                        animatorArr[0] = ObjectAnimator.ofFloat(this.pinnedMessageView, "translationY", new float[]{0.0f});
                        animatorSet.playTogether(animatorArr);
                        this.pinnedMessageViewAnimator.setDuration(200);
                        this.pinnedMessageViewAnimator.addListener(new AnimatorListenerAdapter() {
                            public void onAnimationEnd(Animator animation) {
                                if (ChatActivity.this.pinnedMessageViewAnimator != null && ChatActivity.this.pinnedMessageViewAnimator.equals(animation)) {
                                    ChatActivity.this.pinnedMessageViewAnimator = null;
                                }
                            }

                            public void onAnimationCancel(Animator animation) {
                                if (ChatActivity.this.pinnedMessageViewAnimator != null && ChatActivity.this.pinnedMessageViewAnimator.equals(animation)) {
                                    ChatActivity.this.pinnedMessageViewAnimator = null;
                                }
                            }
                        });
                        this.pinnedMessageViewAnimator.start();
                    } else {
                        this.pinnedMessageView.setTranslationY(0.0f);
                        this.pinnedMessageView.setVisibility(0);
                    }
                }
                FrameLayout.LayoutParams layoutParams1 = (FrameLayout.LayoutParams) this.pinnedMessageNameTextView.getLayoutParams();
                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.pinnedMessageTextView.getLayoutParams();
                PhotoSize photoSize = FileLoader.getClosestPhotoSizeWithSize(this.pinnedMessageObject.photoThumbs2, AndroidUtilities.dp(50.0f));
                if (photoSize == null) {
                    photoSize = FileLoader.getClosestPhotoSizeWithSize(this.pinnedMessageObject.photoThumbs, AndroidUtilities.dp(50.0f));
                }
                int dp;
                if (photoSize == null || (photoSize instanceof TL_photoSizeEmpty) || (photoSize.location instanceof TL_fileLocationUnavailable) || this.pinnedMessageObject.type == 13) {
                    this.pinnedMessageImageView.setImageBitmap(null);
                    this.pinnedImageLocation = null;
                    this.pinnedMessageImageView.setVisibility(4);
                    dp = AndroidUtilities.dp(18.0f);
                    layoutParams2.leftMargin = dp;
                    layoutParams1.leftMargin = dp;
                } else {
                    if (this.pinnedMessageObject.isRoundVideo()) {
                        this.pinnedMessageImageView.setRoundRadius(AndroidUtilities.dp(16.0f));
                    } else {
                        this.pinnedMessageImageView.setRoundRadius(0);
                    }
                    this.pinnedImageLocation = photoSize.location;
                    this.pinnedMessageImageView.setImage(this.pinnedImageLocation, "50_50", (Drawable) null);
                    this.pinnedMessageImageView.setVisibility(0);
                    dp = AndroidUtilities.dp(55.0f);
                    layoutParams2.leftMargin = dp;
                    layoutParams1.leftMargin = dp;
                }
                this.pinnedMessageNameTextView.setLayoutParams(layoutParams1);
                this.pinnedMessageTextView.setLayoutParams(layoutParams2);
                this.pinnedMessageNameTextView.setText(LocaleController.getString("PinnedMessage", R.string.PinnedMessage));
                if (this.pinnedMessageObject.type == 14) {
                    this.pinnedMessageTextView.setText(String.format("%s - %s", new Object[]{this.pinnedMessageObject.getMusicAuthor(), this.pinnedMessageObject.getMusicTitle()}));
                } else if (this.pinnedMessageObject.messageOwner.media instanceof TL_messageMediaGame) {
                    this.pinnedMessageTextView.setText(Emoji.replaceEmoji(this.pinnedMessageObject.messageOwner.media.game.title, this.pinnedMessageTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                } else if (this.pinnedMessageObject.messageText != null) {
                    String mess = this.pinnedMessageObject.messageText.toString();
                    if (mess.length() > 150) {
                        mess = mess.substring(0, 150);
                    }
                    this.pinnedMessageTextView.setText(Emoji.replaceEmoji(mess.replace('\n', ' '), this.pinnedMessageTextView.getPaint().getFontMetricsInt(), AndroidUtilities.dp(14.0f), false));
                }
            } else {
                this.pinnedImageLocation = null;
                hidePinnedMessageView(animated);
                if (this.loadingPinnedMessage != this.info.pinned_msg_id) {
                    this.loadingPinnedMessage = this.info.pinned_msg_id;
                    DataQuery.getInstance(this.currentAccount).loadPinnedMessage(this.currentChat.id, this.info.pinned_msg_id, true);
                }
            }
            checkListViewPaddings();
        }
    }

    private void updateSpamView() {
        if (this.reportSpamView != null) {
            boolean show;
            SharedPreferences preferences = MessagesController.getNotificationsSettings(this.currentAccount);
            if (this.currentEncryptedChat != null) {
                if (this.currentEncryptedChat.admin_id == UserConfig.getInstance(this.currentAccount).getClientUserId() || ContactsController.getInstance(this.currentAccount).isLoadingContacts() || ContactsController.getInstance(this.currentAccount).contactsDict.get(Integer.valueOf(this.currentUser.id)) != null) {
                    show = false;
                } else {
                    show = true;
                }
                if (show && preferences.getInt("spam3_" + this.dialog_id, 0) == 1) {
                    show = false;
                }
            } else {
                show = preferences.getInt(new StringBuilder().append("spam3_").append(this.dialog_id).toString(), 0) == 2;
            }
            AnimatorSet animatorSet;
            Animator[] animatorArr;
            if (show) {
                if (this.reportSpamView.getTag() != null) {
                    if (BuildVars.LOGS_ENABLED) {
                        FileLog.d("show spam button");
                    }
                    this.reportSpamView.setTag(null);
                    this.reportSpamView.setVisibility(0);
                    if (this.reportSpamViewAnimator != null) {
                        this.reportSpamViewAnimator.cancel();
                    }
                    this.reportSpamViewAnimator = new AnimatorSet();
                    animatorSet = this.reportSpamViewAnimator;
                    animatorArr = new Animator[1];
                    animatorArr[0] = ObjectAnimator.ofFloat(this.reportSpamView, "translationY", new float[]{0.0f});
                    animatorSet.playTogether(animatorArr);
                    this.reportSpamViewAnimator.setDuration(200);
                    this.reportSpamViewAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (ChatActivity.this.reportSpamViewAnimator != null && ChatActivity.this.reportSpamViewAnimator.equals(animation)) {
                                ChatActivity.this.reportSpamViewAnimator = null;
                            }
                        }

                        public void onAnimationCancel(Animator animation) {
                            if (ChatActivity.this.reportSpamViewAnimator != null && ChatActivity.this.reportSpamViewAnimator.equals(animation)) {
                                ChatActivity.this.reportSpamViewAnimator = null;
                            }
                        }
                    });
                    this.reportSpamViewAnimator.start();
                }
            } else if (this.reportSpamView.getTag() == null) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.d("hide spam button");
                }
                this.reportSpamView.setTag(Integer.valueOf(1));
                if (this.reportSpamViewAnimator != null) {
                    this.reportSpamViewAnimator.cancel();
                }
                this.reportSpamViewAnimator = new AnimatorSet();
                animatorSet = this.reportSpamViewAnimator;
                animatorArr = new Animator[1];
                animatorArr[0] = ObjectAnimator.ofFloat(this.reportSpamView, "translationY", new float[]{(float) (-AndroidUtilities.dp(50.0f))});
                animatorSet.playTogether(animatorArr);
                this.reportSpamViewAnimator.setDuration(200);
                this.reportSpamViewAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        if (ChatActivity.this.reportSpamViewAnimator != null && ChatActivity.this.reportSpamViewAnimator.equals(animation)) {
                            ChatActivity.this.reportSpamView.setVisibility(8);
                            ChatActivity.this.reportSpamViewAnimator = null;
                        }
                    }

                    public void onAnimationCancel(Animator animation) {
                        if (ChatActivity.this.reportSpamViewAnimator != null && ChatActivity.this.reportSpamViewAnimator.equals(animation)) {
                            ChatActivity.this.reportSpamViewAnimator = null;
                        }
                    }
                });
                this.reportSpamViewAnimator.start();
            }
            checkListViewPaddings();
        } else if (BuildVars.LOGS_ENABLED) {
            FileLog.d("no spam view found");
        }
    }

    private void updateContactStatus() {
        if (this.addContactItem != null) {
            if (this.currentUser == null) {
                this.addContactItem.setVisibility(8);
            } else {
                User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.currentUser.id));
                if (user != null) {
                    this.currentUser = user;
                }
                if ((this.currentEncryptedChat != null && !(this.currentEncryptedChat instanceof TL_encryptedChat)) || MessagesController.isSupportId(this.currentUser.id) || UserObject.isDeleted(this.currentUser) || ContactsController.getInstance(this.currentAccount).isLoadingContacts() || (!TextUtils.isEmpty(this.currentUser.phone) && ContactsController.getInstance(this.currentAccount).contactsDict.get(Integer.valueOf(this.currentUser.id)) != null && (ContactsController.getInstance(this.currentAccount).contactsDict.size() != 0 || !ContactsController.getInstance(this.currentAccount).isLoadingContacts()))) {
                    this.addContactItem.setVisibility(8);
                } else {
                    this.addContactItem.setVisibility(0);
                    if (TextUtils.isEmpty(this.currentUser.phone)) {
                        this.addContactItem.setText(LocaleController.getString("ShareMyContactInfo", R.string.ShareMyContactInfo));
                        this.addToContactsButton.setVisibility(8);
                        this.reportSpamButton.setPadding(AndroidUtilities.dp(50.0f), 0, AndroidUtilities.dp(50.0f), 0);
                        this.reportSpamContainer.setLayoutParams(LayoutHelper.createLinear(-1, -1, 1.0f, 51, 0, 0, 0, AndroidUtilities.dp(1.0f)));
                    } else {
                        this.addContactItem.setText(LocaleController.getString("AddToContacts", R.string.AddToContacts));
                        this.reportSpamButton.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(50.0f), 0);
                        this.addToContactsButton.setVisibility(0);
                        this.reportSpamContainer.setLayoutParams(LayoutHelper.createLinear(-1, -1, 0.5f, 51, 0, 0, 0, AndroidUtilities.dp(1.0f)));
                    }
                }
            }
            checkListViewPaddings();
        }
    }

    private void checkListViewPaddingsInternal() {
        if (this.chatLayoutManager != null) {
            try {
                int firstVisPos = this.chatLayoutManager.findFirstVisibleItemPosition();
                int lastVisPos = -1;
                if (!(this.wasManualScroll || this.unreadMessageObject == null)) {
                    int pos = this.messages.indexOf(this.unreadMessageObject);
                    if (pos >= 0) {
                        lastVisPos = pos + this.chatAdapter.messagesStartRow;
                        firstVisPos = -1;
                    }
                }
                int top = 0;
                if (firstVisPos != -1) {
                    View firstVisView = this.chatLayoutManager.findViewByPosition(firstVisPos);
                    top = firstVisView == null ? 0 : (this.chatListView.getMeasuredHeight() - firstVisView.getBottom()) - this.chatListView.getPaddingBottom();
                }
                FrameLayout.LayoutParams layoutParams;
                if (this.chatListView.getPaddingTop() != AndroidUtilities.dp(52.0f) && ((this.pinnedMessageView != null && this.pinnedMessageView.getTag() == null) || (this.reportSpamView != null && this.reportSpamView.getTag() == null))) {
                    this.chatListView.setPadding(0, AndroidUtilities.dp(52.0f), 0, AndroidUtilities.dp(3.0f));
                    layoutParams = (FrameLayout.LayoutParams) this.floatingDateView.getLayoutParams();
                    layoutParams.topMargin = AndroidUtilities.dp(52.0f);
                    this.floatingDateView.setLayoutParams(layoutParams);
                    this.chatListView.setTopGlowOffset(AndroidUtilities.dp(48.0f));
                } else if (this.chatListView.getPaddingTop() == AndroidUtilities.dp(4.0f) || ((this.pinnedMessageView != null && this.pinnedMessageView.getTag() == null) || (this.reportSpamView != null && this.reportSpamView.getTag() == null))) {
                    firstVisPos = -1;
                } else {
                    this.chatListView.setPadding(0, AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(3.0f));
                    layoutParams = (FrameLayout.LayoutParams) this.floatingDateView.getLayoutParams();
                    layoutParams.topMargin = AndroidUtilities.dp(4.0f);
                    this.floatingDateView.setLayoutParams(layoutParams);
                    this.chatListView.setTopGlowOffset(0);
                }
                if (firstVisPos != -1) {
                    this.chatLayoutManager.scrollToPositionWithOffset(firstVisPos, top);
                } else if (lastVisPos != -1) {
                    this.chatLayoutManager.scrollToPositionWithOffset(lastVisPos, ((this.chatListView.getMeasuredHeight() - this.chatListView.getPaddingBottom()) - this.chatListView.getPaddingTop()) - AndroidUtilities.dp(29.0f));
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    private void checkListViewPaddings() {
        if (this.wasManualScroll || this.unreadMessageObject == null) {
            AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$43(this));
        } else if (this.messages.indexOf(this.unreadMessageObject) >= 0) {
            this.fixPaddingsInLayout = true;
            if (this.fragmentView != null) {
                this.fragmentView.requestLayout();
            }
        }
    }

    private void checkRaiseSensors() {
        if (this.chatActivityEnterView != null && this.chatActivityEnterView.isStickersExpanded()) {
            MediaController.getInstance().setAllowStartRecord(false);
        } else if (ChatObject.isChannel(this.currentChat) && this.currentChat.banned_rights != null && this.currentChat.banned_rights.send_media) {
            MediaController.getInstance().setAllowStartRecord(false);
        } else if (ApplicationLoader.mainInterfacePaused || ((this.bottomOverlayChat != null && this.bottomOverlayChat.getVisibility() == 0) || ((this.bottomOverlay != null && this.bottomOverlay.getVisibility() == 0) || (this.searchContainer != null && this.searchContainer.getVisibility() == 0)))) {
            MediaController.getInstance().setAllowStartRecord(false);
        } else {
            MediaController.getInstance().setAllowStartRecord(true);
        }
    }

    public void dismissCurrentDialig() {
        if (this.chatAttachAlert == null || this.visibleDialog != this.chatAttachAlert) {
            super.dismissCurrentDialig();
            return;
        }
        this.chatAttachAlert.closeCamera(false);
        this.chatAttachAlert.dismissInternal();
        this.chatAttachAlert.hideCamera(true);
    }

    protected void setInPreviewMode(boolean value) {
        super.setInPreviewMode(value);
        if (this.avatarContainer != null) {
            this.avatarContainer.setOccupyStatusBar(!value);
            this.avatarContainer.setLayoutParams(LayoutHelper.createFrame(-2, -1.0f, 51, !value ? 56.0f : 0.0f, 0.0f, 40.0f, 0.0f));
        }
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.setVisibility(!value ? 0 : 4);
        }
        if (this.actionBar != null) {
            this.actionBar.setBackButtonDrawable(!value ? new BackDrawable(false) : null);
            this.headerItem.setAlpha(!value ? 1.0f : 0.0f);
            this.attachItem.setAlpha(!value ? 1.0f : 0.0f);
        }
        if (this.chatListView != null) {
            int count = this.chatListView.getChildCount();
            for (int a = 0; a < count; a++) {
                View view = this.chatListView.getChildAt(a);
                MessageObject message = null;
                if (view instanceof ChatMessageCell) {
                    message = ((ChatMessageCell) view).getMessageObject();
                } else if (view instanceof ChatActionCell) {
                    message = ((ChatActionCell) view).getMessageObject();
                }
                if (message != null && message.messageOwner != null && message.messageOwner.media_unread && message.messageOwner.mentioned) {
                    if (!(message.isVoice() || message.isRoundVideo())) {
                        this.newMentionsCount--;
                        if (this.newMentionsCount <= 0) {
                            this.newMentionsCount = 0;
                            this.hasAllMentionsLocal = true;
                            showMentiondownButton(false, true);
                        } else {
                            this.mentiondownButtonCounter.setText(String.format("%d", new Object[]{Integer.valueOf(this.newMentionsCount)}));
                        }
                        MessagesController.getInstance(this.currentAccount).markMentionMessageAsRead(message.getId(), ChatObject.isChannel(this.currentChat) ? this.currentChat.id : 0, this.dialog_id);
                        message.setContentIsRead();
                    }
                    if (view instanceof ChatMessageCell) {
                        ((ChatMessageCell) view).setHighlighted(false);
                        ((ChatMessageCell) view).setHighlightedAnimated();
                    }
                }
            }
        }
        updateBottomOverlay();
    }

    public void onResume() {
        super.onResume();
        AndroidUtilities.requestAdjustResize(getParentActivity(), this.classGuid);
        MediaController.getInstance().startRaiseToEarSensors(this);
        checkRaiseSensors();
        if (this.chatAttachAlert != null) {
            this.chatAttachAlert.onResume();
        }
        if (this.firstOpen && MessagesController.getInstance(this.currentAccount).isProxyDialog(this.dialog_id)) {
            SharedPreferences preferences = MessagesController.getGlobalNotificationsSettings();
            if (preferences.getLong("proxychannel", 0) != this.dialog_id) {
                preferences.edit().putLong("proxychannel", this.dialog_id).commit();
                AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                builder.setMessage(LocaleController.getString("UseProxySponsorInfo", R.string.UseProxySponsorInfo));
                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                showDialog(builder.create());
            }
        }
        checkActionBarMenu();
        if (!(this.replyImageLocation == null || this.replyImageView == null)) {
            this.replyImageView.setImage(this.replyImageLocation, "50_50", (Drawable) null);
        }
        if (!(this.pinnedImageLocation == null || this.pinnedMessageImageView == null)) {
            this.pinnedMessageImageView.setImage(this.pinnedImageLocation, "50_50", (Drawable) null);
        }
        NotificationsController.getInstance(this.currentAccount).setOpenedDialogId(this.dialog_id);
        MessagesController.getInstance(this.currentAccount).setLastVisibleDialogId(this.dialog_id, true);
        if (this.scrollToTopOnResume) {
            if (!this.scrollToTopUnReadOnResume || this.scrollToMessage == null) {
                moveScrollToLastMessage();
            } else if (this.chatListView != null) {
                int yOffset;
                boolean bottom = true;
                if (this.scrollToMessagePosition == -9000) {
                    yOffset = getScrollOffsetForMessage(this.scrollToMessage);
                    bottom = false;
                } else if (this.scrollToMessagePosition == -10000) {
                    yOffset = -AndroidUtilities.dp(11.0f);
                    bottom = false;
                } else {
                    yOffset = this.scrollToMessagePosition;
                }
                this.chatLayoutManager.scrollToPositionWithOffset(this.chatAdapter.messagesStartRow + this.messages.indexOf(this.scrollToMessage), yOffset, bottom);
            }
            this.scrollToTopUnReadOnResume = false;
            this.scrollToTopOnResume = false;
            this.scrollToMessage = null;
        }
        this.paused = false;
        this.pausedOnLastMessage = false;
        checkScrollForLoad(false);
        if (this.wasPaused) {
            this.wasPaused = false;
            if (this.chatAdapter != null) {
                this.chatAdapter.notifyDataSetChanged();
            }
        }
        fixLayout();
        applyDraftMaybe(false);
        if (!(this.bottomOverlayChat == null || this.bottomOverlayChat.getVisibility() == 0)) {
            this.chatActivityEnterView.setFieldFocused(true);
        }
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.onResume();
        }
        if (this.currentUser != null) {
            this.chatEnterTime = System.currentTimeMillis();
            this.chatLeaveTime = 0;
        }
        if (this.startVideoEdit != null) {
            AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$44(this));
        }
        if (this.chatListView != null && (this.chatActivityEnterView == null || !this.chatActivityEnterView.isEditingMessage())) {
            this.chatListView.setOnItemLongClickListener(this.onItemLongClickListener);
            this.chatListView.setOnItemClickListener(this.onItemClickListener);
            this.chatListView.setLongClickable(true);
        }
        checkBotCommands();
    }

    final /* synthetic */ void lambda$onResume$56$ChatActivity() {
        openVideoEditor(this.startVideoEdit, null);
        this.startVideoEdit = null;
    }

    public void onPause() {
        boolean z;
        super.onPause();
        MessagesController.getInstance(this.currentAccount).markDialogAsReadNow(this.dialog_id);
        MediaController.getInstance().stopRaiseToEarSensors(this, true);
        this.paused = true;
        this.wasPaused = true;
        NotificationsController.getInstance(this.currentAccount).setOpenedDialogId(0);
        MessagesController.getInstance(this.currentAccount).setLastVisibleDialogId(this.dialog_id, false);
        CharSequence draftMessage = null;
        MessageObject replyMessage = null;
        boolean searchWebpage = true;
        if (!(this.ignoreAttachOnPause || this.chatActivityEnterView == null || this.bottomOverlayChat.getVisibility() == 0)) {
            this.chatActivityEnterView.onPause();
            replyMessage = this.replyingMessageObject;
            if (!this.chatActivityEnterView.isEditingMessage()) {
                CharSequence text = AndroidUtilities.getTrimmedString(this.chatActivityEnterView.getFieldText());
                if (!TextUtils.isEmpty(text)) {
                    if (!TextUtils.equals(text, "@gif")) {
                        draftMessage = text;
                    }
                }
            }
            searchWebpage = this.chatActivityEnterView.isMessageWebPageSearchEnabled();
            this.chatActivityEnterView.setFieldFocused(false);
        }
        if (this.chatAttachAlert != null) {
            if (this.ignoreAttachOnPause) {
                this.ignoreAttachOnPause = false;
            } else {
                this.chatAttachAlert.onPause();
            }
        }
        CharSequence[] message = new CharSequence[]{draftMessage};
        ArrayList<MessageEntity> entities = DataQuery.getInstance(this.currentAccount).getEntities(message);
        DataQuery instance = DataQuery.getInstance(this.currentAccount);
        long j = this.dialog_id;
        CharSequence charSequence = message[0];
        Message message2 = replyMessage != null ? replyMessage.messageOwner : null;
        if (searchWebpage) {
            z = false;
        } else {
            z = true;
        }
        instance.saveDraft(j, charSequence, entities, message2, z);
        MessagesController.getInstance(this.currentAccount).cancelTyping(0, this.dialog_id);
        if (!this.pausedOnLastMessage) {
            Editor editor = MessagesController.getNotificationsSettings(this.currentAccount).edit();
            int messageId = 0;
            int offset = 0;
            if (this.chatLayoutManager != null) {
                int position = this.chatLayoutManager.findFirstVisibleItemPosition();
                if (position != 0) {
                    Holder holder = (Holder) this.chatListView.findViewHolderForAdapterPosition(position);
                    if (holder != null) {
                        int mid = 0;
                        if (holder.itemView instanceof ChatMessageCell) {
                            mid = ((ChatMessageCell) holder.itemView).getMessageObject().getId();
                        } else if (holder.itemView instanceof ChatActionCell) {
                            mid = ((ChatActionCell) holder.itemView).getMessageObject().getId();
                        }
                        if (mid == 0) {
                            holder = (Holder) this.chatListView.findViewHolderForAdapterPosition(position + 1);
                        }
                        boolean ignore = false;
                        for (int a = position - 1; a >= this.chatAdapter.messagesStartRow; a--) {
                            int num = a - this.chatAdapter.messagesStartRow;
                            if (num >= 0 && num < this.messages.size()) {
                                MessageObject messageObject = (MessageObject) this.messages.get(num);
                                if (messageObject.getId() != 0) {
                                    if (!messageObject.isOut() && messageObject.isUnread()) {
                                        ignore = true;
                                        messageId = 0;
                                    }
                                    if (!(holder == null || ignore)) {
                                        if (holder.itemView instanceof ChatMessageCell) {
                                            messageId = ((ChatMessageCell) holder.itemView).getMessageObject().getId();
                                        } else if (holder.itemView instanceof ChatActionCell) {
                                            messageId = ((ChatActionCell) holder.itemView).getMessageObject().getId();
                                        }
                                        if ((messageId > 0 || this.currentEncryptedChat != null) && (messageId >= 0 || this.currentEncryptedChat == null)) {
                                            messageId = 0;
                                        } else {
                                            offset = holder.itemView.getBottom() - this.chatListView.getMeasuredHeight();
                                            if (BuildVars.LOGS_ENABLED) {
                                                FileLog.d("save offset = " + offset + " for mid " + messageId);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (holder.itemView instanceof ChatMessageCell) {
                            messageId = ((ChatMessageCell) holder.itemView).getMessageObject().getId();
                        } else if (holder.itemView instanceof ChatActionCell) {
                            messageId = ((ChatActionCell) holder.itemView).getMessageObject().getId();
                        }
                        if (messageId > 0) {
                        }
                        messageId = 0;
                    }
                }
            }
            if (messageId != 0) {
                editor.putInt("diditem" + this.dialog_id, messageId);
                editor.putInt("diditemo" + this.dialog_id, offset);
            } else {
                this.pausedOnLastMessage = true;
                editor.remove("diditem" + this.dialog_id);
                editor.remove("diditemo" + this.dialog_id);
            }
            editor.commit();
        }
        if (this.currentUser != null) {
            this.chatLeaveTime = System.currentTimeMillis();
            updateInformationForScreenshotDetector();
        }
    }

    private void applyDraftMaybe(boolean canClear) {
        if (this.chatActivityEnterView != null) {
            DraftMessage draftMessage = DataQuery.getInstance(this.currentAccount).getDraft(this.dialog_id);
            Message draftReplyMessage = (draftMessage == null || draftMessage.reply_to_msg_id == 0) ? null : DataQuery.getInstance(this.currentAccount).getDraftMessage(this.dialog_id);
            if (this.chatActivityEnterView.getFieldText() == null) {
                if (draftMessage != null) {
                    CharSequence message;
                    this.chatActivityEnterView.setWebPage(null, !draftMessage.no_webpage);
                    if (draftMessage.entities.isEmpty()) {
                        message = draftMessage.message;
                    } else {
                        SpannableStringBuilder stringBuilder = SpannableStringBuilder.valueOf(draftMessage.message);
                        DataQuery.sortEntities(draftMessage.entities);
                        int addToOffset = 0;
                        for (int a = 0; a < draftMessage.entities.size(); a++) {
                            MessageEntity entity = (MessageEntity) draftMessage.entities.get(a);
                            if ((entity instanceof TL_inputMessageEntityMentionName) || (entity instanceof TL_messageEntityMentionName)) {
                                int user_id;
                                if (entity instanceof TL_inputMessageEntityMentionName) {
                                    user_id = ((TL_inputMessageEntityMentionName) entity).user_id.user_id;
                                } else {
                                    user_id = ((TL_messageEntityMentionName) entity).user_id;
                                }
                                if ((entity.offset + addToOffset) + entity.length < stringBuilder.length() && stringBuilder.charAt((entity.offset + addToOffset) + entity.length) == ' ') {
                                    entity.length++;
                                }
                                stringBuilder.setSpan(new URLSpanUserMention(TtmlNode.ANONYMOUS_REGION_ID + user_id, 1), entity.offset + addToOffset, (entity.offset + addToOffset) + entity.length, 33);
                            } else if (entity instanceof TL_messageEntityCode) {
                                stringBuilder.insert((entity.offset + entity.length) + addToOffset, "`");
                                stringBuilder.insert(entity.offset + addToOffset, "`");
                                addToOffset += 2;
                            } else if (entity instanceof TL_messageEntityPre) {
                                stringBuilder.insert((entity.offset + entity.length) + addToOffset, "```");
                                stringBuilder.insert(entity.offset + addToOffset, "```");
                                addToOffset += 6;
                            } else if (entity instanceof TL_messageEntityBold) {
                                stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), entity.offset + addToOffset, (entity.offset + entity.length) + addToOffset, 33);
                            } else if (entity instanceof TL_messageEntityItalic) {
                                stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/ritalic.ttf")), entity.offset + addToOffset, (entity.offset + entity.length) + addToOffset, 33);
                            } else if (entity instanceof TL_messageEntityTextUrl) {
                                stringBuilder.setSpan(new URLSpanReplacement(entity.url), entity.offset + addToOffset, (entity.offset + entity.length) + addToOffset, 33);
                            }
                        }
                        message = stringBuilder;
                    }
                    this.chatActivityEnterView.setFieldText(message);
                    if (getArguments().getBoolean("hasUrl", false)) {
                        this.chatActivityEnterView.setSelection(draftMessage.message.indexOf(10) + 1);
                        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$45(this), 700);
                    }
                }
            } else if (canClear && draftMessage == null) {
                this.chatActivityEnterView.setFieldText(TtmlNode.ANONYMOUS_REGION_ID);
                hideFieldPanel();
            }
            if (this.replyingMessageObject == null && draftReplyMessage != null) {
                this.replyingMessageObject = new MessageObject(this.currentAccount, draftReplyMessage, MessagesController.getInstance(this.currentAccount).getUsers(), false);
                showFieldPanelForReply(true, this.replyingMessageObject);
            }
        }
    }

    final /* synthetic */ void lambda$applyDraftMaybe$57$ChatActivity() {
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.setFieldFocused(true);
            this.chatActivityEnterView.openKeyboard();
        }
    }

    private void updateInformationForScreenshotDetector() {
        if (this.currentUser != null) {
            if (this.currentEncryptedChat != null) {
                ArrayList<Long> visibleMessages = new ArrayList();
                if (this.chatListView != null) {
                    int count = this.chatListView.getChildCount();
                    for (int a = 0; a < count; a++) {
                        View view = this.chatListView.getChildAt(a);
                        MessageObject object = null;
                        if (view instanceof ChatMessageCell) {
                            object = ((ChatMessageCell) view).getMessageObject();
                        }
                        if (!(object == null || object.getId() >= 0 || object.messageOwner.random_id == 0)) {
                            visibleMessages.add(Long.valueOf(object.messageOwner.random_id));
                        }
                    }
                }
                MediaController.getInstance().setLastVisibleMessageIds(this.currentAccount, this.chatEnterTime, this.chatLeaveTime, this.currentUser, this.currentEncryptedChat, visibleMessages, 0);
                return;
            }
            SecretMediaViewer viewer = SecretMediaViewer.getInstance();
            MessageObject messageObject = viewer.getCurrentMessageObject();
            if (messageObject != null && !messageObject.isOut()) {
                MediaController.getInstance().setLastVisibleMessageIds(this.currentAccount, viewer.getOpenTime(), viewer.getCloseTime(), this.currentUser, null, null, messageObject.getId());
            }
        }
    }

    private boolean fixLayoutInternal() {
        if (AndroidUtilities.isTablet() || ApplicationLoader.applicationContext.getResources().getConfiguration().orientation != 2) {
            this.selectedMessagesCountTextView.setTextSize(20);
        } else {
            this.selectedMessagesCountTextView.setTextSize(18);
        }
        HashMap<Long, GroupedMessages> newGroups = null;
        int count = this.chatListView.getChildCount();
        for (int a = 0; a < count; a++) {
            View child = this.chatListView.getChildAt(a);
            if (child instanceof ChatMessageCell) {
                GroupedMessages groupedMessages = ((ChatMessageCell) child).getCurrentMessagesGroup();
                if (groupedMessages != null && groupedMessages.hasSibling) {
                    if (newGroups == null) {
                        newGroups = new HashMap();
                    }
                    if (!newGroups.containsKey(Long.valueOf(groupedMessages.groupId))) {
                        newGroups.put(Long.valueOf(groupedMessages.groupId), groupedMessages);
                        int idx = this.messages.indexOf((MessageObject) groupedMessages.messages.get(groupedMessages.messages.size() - 1));
                        if (idx >= 0) {
                            this.chatAdapter.notifyItemRangeChanged(this.chatAdapter.messagesStartRow + idx, groupedMessages.messages.size());
                        }
                    }
                }
            }
        }
        if (!AndroidUtilities.isTablet()) {
            return true;
        }
        if (AndroidUtilities.isSmallTablet() && ApplicationLoader.applicationContext.getResources().getConfiguration().orientation == 1) {
            this.actionBar.setBackButtonDrawable(new BackDrawable(false));
            if (this.fragmentContextView != null && this.fragmentContextView.getParent() == null) {
                ((ViewGroup) this.fragmentView).addView(this.fragmentContextView, LayoutHelper.createFrame(-1, 39.0f, 51, 0.0f, -36.0f, 0.0f, 0.0f));
            }
        } else {
            ActionBar actionBar = this.actionBar;
            boolean z = this.parentLayout == null || this.parentLayout.fragmentsStack.isEmpty() || this.parentLayout.fragmentsStack.get(0) == this || this.parentLayout.fragmentsStack.size() == 1;
            actionBar.setBackButtonDrawable(new BackDrawable(z));
            if (!(this.fragmentContextView == null || this.fragmentContextView.getParent() == null)) {
                this.fragmentView.setPadding(0, 0, 0, 0);
                ((ViewGroup) this.fragmentView).removeView(this.fragmentContextView);
            }
        }
        return false;
    }

    private void fixLayout() {
        if (this.avatarContainer != null) {
            this.avatarContainer.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (ChatActivity.this.avatarContainer != null) {
                        ChatActivity.this.avatarContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    return ChatActivity.this.fixLayoutInternal();
                }
            });
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        fixLayout();
        if (this.visibleDialog instanceof DatePickerDialog) {
            this.visibleDialog.dismiss();
        }
    }

    private void createDeleteMessagesAlert(MessageObject finalSelectedObject, GroupedMessages selectedGroup) {
        createDeleteMessagesAlert(finalSelectedObject, selectedGroup, 1);
    }

    private void createDeleteMessagesAlert(MessageObject finalSelectedObject, GroupedMessages finalSelectedGroup, int loadParticipant) {
        if (getParentActivity() != null) {
            int count;
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            if (finalSelectedGroup != null) {
                count = finalSelectedGroup.messages.size();
            } else if (finalSelectedObject != null) {
                count = 1;
            } else {
                count = this.selectedMessagesIds[0].size() + this.selectedMessagesIds[1].size();
            }
            builder.setMessage(LocaleController.formatString("AreYouSureDeleteMessages", R.string.AreYouSureDeleteMessages, LocaleController.formatPluralString("messages", count)));
            builder.setTitle(LocaleController.getString("Message", R.string.Message));
            boolean[] checks = new boolean[3];
            boolean[] deleteForAll = new boolean[1];
            User user = null;
            boolean canRevokeInbox = this.currentUser != null && MessagesController.getInstance(this.currentAccount).canRevokePmInbox;
            int revokeTimeLimit;
            if (this.currentUser != null) {
                revokeTimeLimit = MessagesController.getInstance(this.currentAccount).revokeTimePmLimit;
            } else {
                revokeTimeLimit = MessagesController.getInstance(this.currentAccount).revokeTimeLimit;
            }
            boolean z;
            int currentDate;
            int a;
            int b;
            MessageObject msg;
            boolean exit;
            View frameLayout;
            if (this.currentChat != null && this.currentChat.megagroup) {
                z = false;
                boolean canBan = ChatObject.canBlockUsers(this.currentChat);
                currentDate = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                if (finalSelectedObject != null) {
                    if (finalSelectedObject.messageOwner.action == null || (finalSelectedObject.messageOwner.action instanceof TL_messageActionEmpty) || (finalSelectedObject.messageOwner.action instanceof TL_messageActionChatDeleteUser)) {
                        user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(finalSelectedObject.messageOwner.from_id));
                    }
                    if (!finalSelectedObject.isSendError() && finalSelectedObject.getDialogId() == this.mergeDialogId && ((finalSelectedObject.messageOwner.action == null || (finalSelectedObject.messageOwner.action instanceof TL_messageActionEmpty)) && finalSelectedObject.isOut() && currentDate - finalSelectedObject.messageOwner.date <= revokeTimeLimit)) {
                        z = true;
                    } else {
                        z = false;
                    }
                } else {
                    int from_id = -1;
                    for (a = 1; a >= 0; a--) {
                        for (b = 0; b < this.selectedMessagesIds[a].size(); b++) {
                            msg = (MessageObject) this.selectedMessagesIds[a].valueAt(b);
                            if (from_id == -1) {
                                from_id = msg.messageOwner.from_id;
                            }
                            if (from_id < 0 || from_id != msg.messageOwner.from_id) {
                                from_id = -2;
                                break;
                            }
                        }
                        if (from_id == -2) {
                            break;
                        }
                    }
                    exit = false;
                    for (a = 1; a >= 0; a--) {
                        for (b = 0; b < this.selectedMessagesIds[a].size(); b++) {
                            msg = (MessageObject) this.selectedMessagesIds[a].valueAt(b);
                            if (a != 1) {
                                if (a == 0 && !msg.isOut()) {
                                    z = false;
                                    exit = true;
                                    break;
                                }
                            } else if (!msg.isOut() || msg.messageOwner.action != null) {
                                z = false;
                                exit = true;
                                break;
                            } else if (currentDate - msg.messageOwner.date <= 172800) {
                                z = true;
                            }
                        }
                        if (exit) {
                            break;
                        }
                    }
                    if (from_id != -1) {
                        user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(from_id));
                    }
                }
                if (user == null || user.id == UserConfig.getInstance(this.currentAccount).getClientUserId() || loadParticipant == 2) {
                    if (z) {
                        frameLayout = new FrameLayout(getParentActivity());
                        frameLayout = new CheckBoxCell(getParentActivity(), 1);
                        frameLayout.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        if (this.currentChat != null) {
                            frameLayout.setText(LocaleController.getString("DeleteForAll", R.string.DeleteForAll), TtmlNode.ANONYMOUS_REGION_ID, false, false);
                        } else {
                            frameLayout.setText(LocaleController.formatString("DeleteForUser", R.string.DeleteForUser, UserObject.getFirstName(this.currentUser)), TtmlNode.ANONYMOUS_REGION_ID, false, false);
                        }
                        frameLayout.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                        frameLayout.addView(frameLayout, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                        frameLayout.setOnClickListener(new ChatActivity$$Lambda$49(deleteForAll));
                        builder.setView(frameLayout);
                    } else {
                        user = null;
                    }
                } else if (loadParticipant != 1 || this.currentChat.creator) {
                    frameLayout = new FrameLayout(getParentActivity());
                    int num = 0;
                    a = 0;
                    while (a < 3) {
                        if (canBan || a != 0) {
                            int dp;
                            int dp2;
                            frameLayout = new CheckBoxCell(getParentActivity(), 1);
                            frameLayout.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                            frameLayout.setTag(Integer.valueOf(a));
                            if (a == 0) {
                                frameLayout.setText(LocaleController.getString("DeleteBanUser", R.string.DeleteBanUser), TtmlNode.ANONYMOUS_REGION_ID, false, false);
                            } else if (a == 1) {
                                frameLayout.setText(LocaleController.getString("DeleteReportSpam", R.string.DeleteReportSpam), TtmlNode.ANONYMOUS_REGION_ID, false, false);
                            } else if (a == 2) {
                                frameLayout.setText(LocaleController.formatString("DeleteAllFrom", R.string.DeleteAllFrom, ContactsController.formatName(user.first_name, user.last_name)), TtmlNode.ANONYMOUS_REGION_ID, false, false);
                            }
                            if (LocaleController.isRTL) {
                                dp = AndroidUtilities.dp(16.0f);
                            } else {
                                dp = AndroidUtilities.dp(8.0f);
                            }
                            if (LocaleController.isRTL) {
                                dp2 = AndroidUtilities.dp(8.0f);
                            } else {
                                dp2 = AndroidUtilities.dp(16.0f);
                            }
                            frameLayout.setPadding(dp, 0, dp2, 0);
                            frameLayout.addView(frameLayout, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, (float) (num * 48), 0.0f, 0.0f));
                            frameLayout.setOnClickListener(new ChatActivity$$Lambda$48(checks));
                            num++;
                        }
                        a++;
                    }
                    builder.setView(frameLayout);
                } else {
                    AlertDialog[] progressDialog = new AlertDialog[]{new AlertDialog(getParentActivity(), 1)};
                    TLObject req = new TL_channels_getParticipant();
                    req.channel = MessagesController.getInputChannel(this.currentChat);
                    req.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(user);
                    int requestId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new ChatActivity$$Lambda$46(this, progressDialog, finalSelectedObject, finalSelectedGroup));
                    if (requestId != 0) {
                        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$47(this, progressDialog, requestId), 1000);
                        return;
                    }
                    return;
                }
            } else if (!ChatObject.isChannel(this.currentChat) && this.currentEncryptedChat == null) {
                z = false;
                currentDate = ConnectionsManager.getInstance(this.currentAccount).getCurrentTime();
                if ((this.currentUser != null && this.currentUser.id != UserConfig.getInstance(this.currentAccount).getClientUserId() && !this.currentUser.bot) || this.currentChat != null) {
                    if (finalSelectedObject == null) {
                        exit = false;
                        for (a = 1; a >= 0; a--) {
                            for (b = 0; b < this.selectedMessagesIds[a].size(); b++) {
                                msg = (MessageObject) this.selectedMessagesIds[a].valueAt(b);
                                if (msg.messageOwner.action == null) {
                                    if (!msg.isOut() && !canRevokeInbox && (this.currentChat == null || (!this.currentChat.creator && (!this.currentChat.admin || !this.currentChat.admins_enabled)))) {
                                        exit = true;
                                        z = false;
                                        break;
                                    } else if (!z && currentDate - msg.messageOwner.date <= revokeTimeLimit) {
                                        z = true;
                                    }
                                }
                            }
                            if (exit) {
                                break;
                            }
                        }
                    } else if (finalSelectedObject.isSendError() || (!(finalSelectedObject.messageOwner.action == null || (finalSelectedObject.messageOwner.action instanceof TL_messageActionEmpty)) || (!(finalSelectedObject.isOut() || canRevokeInbox || (this.currentChat != null && (this.currentChat.creator || (this.currentChat.admin && this.currentChat.admins_enabled)))) || currentDate - finalSelectedObject.messageOwner.date > revokeTimeLimit))) {
                        z = false;
                    } else {
                        z = true;
                    }
                }
                if (z) {
                    frameLayout = new FrameLayout(getParentActivity());
                    frameLayout = new CheckBoxCell(getParentActivity(), 1);
                    frameLayout.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                    if (this.currentChat != null) {
                        frameLayout.setText(LocaleController.getString("DeleteForAll", R.string.DeleteForAll), TtmlNode.ANONYMOUS_REGION_ID, false, false);
                    } else {
                        frameLayout.setText(LocaleController.formatString("DeleteForUser", R.string.DeleteForUser, UserObject.getFirstName(this.currentUser)), TtmlNode.ANONYMOUS_REGION_ID, false, false);
                    }
                    frameLayout.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(16.0f) : AndroidUtilities.dp(8.0f), 0, LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : AndroidUtilities.dp(16.0f), 0);
                    frameLayout.addView(frameLayout, LayoutHelper.createFrame(-1, 48.0f, 51, 0.0f, 0.0f, 0.0f, 0.0f));
                    frameLayout.setOnClickListener(new ChatActivity$$Lambda$50(deleteForAll));
                    builder.setView(frameLayout);
                }
            }
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$51(this, finalSelectedObject, finalSelectedGroup, deleteForAll, user, checks));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder.create());
        }
    }

    final /* synthetic */ void lambda$createDeleteMessagesAlert$59$ChatActivity(AlertDialog[] progressDialog, MessageObject finalSelectedObject, GroupedMessages finalSelectedGroup, TLObject response, TL_error error) {
        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$68(this, progressDialog, response, finalSelectedObject, finalSelectedGroup));
    }

    final /* synthetic */ void lambda$null$58$ChatActivity(AlertDialog[] progressDialog, TLObject response, MessageObject finalSelectedObject, GroupedMessages finalSelectedGroup) {
        try {
            progressDialog[0].dismiss();
        } catch (Throwable th) {
        }
        progressDialog[0] = null;
        int loadType = 2;
        if (response != null) {
            TL_channels_channelParticipant participant = (TL_channels_channelParticipant) response;
            if (!((participant.participant instanceof TL_channelParticipantAdmin) || (participant.participant instanceof TL_channelParticipantCreator))) {
                loadType = 0;
            }
        }
        createDeleteMessagesAlert(finalSelectedObject, finalSelectedGroup, loadType);
    }

    final /* synthetic */ void lambda$createDeleteMessagesAlert$61$ChatActivity(AlertDialog[] progressDialog, int reqId) {
        if (progressDialog[0] != null) {
            progressDialog[0].setMessage(LocaleController.getString("Loading", R.string.Loading));
            progressDialog[0].setCanceledOnTouchOutside(false);
            progressDialog[0].setCancelable(false);
            progressDialog[0].setButton(-2, LocaleController.getString("Cancel", R.string.Cancel), new ChatActivity$$Lambda$67(this, reqId));
            showDialog(progressDialog[0]);
        }
    }

    final /* synthetic */ void lambda$null$60$ChatActivity(int reqId, DialogInterface dialog, int which) {
        ConnectionsManager.getInstance(this.currentAccount).cancelRequest(reqId, true);
        try {
            dialog.dismiss();
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    static final /* synthetic */ void lambda$createDeleteMessagesAlert$62$ChatActivity(boolean[] checks, View v) {
        if (v.isEnabled()) {
            CheckBoxCell cell13 = (CheckBoxCell) v;
            Integer num1 = (Integer) cell13.getTag();
            checks[num1.intValue()] = !checks[num1.intValue()];
            cell13.setChecked(checks[num1.intValue()], true);
        }
    }

    static final /* synthetic */ void lambda$createDeleteMessagesAlert$63$ChatActivity(boolean[] deleteForAll, View v) {
        boolean z;
        CheckBoxCell cell12 = (CheckBoxCell) v;
        if (deleteForAll[0]) {
            z = false;
        } else {
            z = true;
        }
        deleteForAll[0] = z;
        cell12.setChecked(deleteForAll[0], true);
    }

    static final /* synthetic */ void lambda$createDeleteMessagesAlert$64$ChatActivity(boolean[] deleteForAll, View v) {
        boolean z;
        CheckBoxCell cell1 = (CheckBoxCell) v;
        if (deleteForAll[0]) {
            z = false;
        } else {
            z = true;
        }
        deleteForAll[0] = z;
        cell1.setChecked(deleteForAll[0], true);
    }

    final /* synthetic */ void lambda$createDeleteMessagesAlert$66$ChatActivity(MessageObject finalSelectedObject, GroupedMessages finalSelectedGroup, boolean[] deleteForAll, User userFinal, boolean[] checks, DialogInterface dialogInterface, int i) {
        ArrayList<Integer> ids = null;
        ArrayList<Long> random_ids;
        int a;
        if (finalSelectedObject != null) {
            ids = new ArrayList();
            random_ids = null;
            if (finalSelectedGroup != null) {
                for (a = 0; a < finalSelectedGroup.messages.size(); a++) {
                    MessageObject messageObject = (MessageObject) finalSelectedGroup.messages.get(a);
                    ids.add(Integer.valueOf(messageObject.getId()));
                    if (!(this.currentEncryptedChat == null || messageObject.messageOwner.random_id == 0 || messageObject.type == 10)) {
                        if (random_ids == null) {
                            random_ids = new ArrayList();
                        }
                        random_ids.add(Long.valueOf(messageObject.messageOwner.random_id));
                    }
                }
            } else {
                ids.add(Integer.valueOf(finalSelectedObject.getId()));
                if (!(this.currentEncryptedChat == null || finalSelectedObject.messageOwner.random_id == 0 || finalSelectedObject.type == 10)) {
                    random_ids = new ArrayList();
                    random_ids.add(Long.valueOf(finalSelectedObject.messageOwner.random_id));
                }
            }
            MessagesController.getInstance(this.currentAccount).deleteMessages(ids, random_ids, this.currentEncryptedChat, finalSelectedObject.messageOwner.to_id.channel_id, deleteForAll[0]);
        } else {
            for (a = 1; a >= 0; a--) {
                int b;
                MessageObject msg;
                ids = new ArrayList();
                for (b = 0; b < this.selectedMessagesIds[a].size(); b++) {
                    ids.add(Integer.valueOf(this.selectedMessagesIds[a].keyAt(b)));
                }
                random_ids = null;
                int channelId = 0;
                if (!ids.isEmpty()) {
                    msg = (MessageObject) this.selectedMessagesIds[a].get(((Integer) ids.get(0)).intValue());
                    if (null == null && msg.messageOwner.to_id.channel_id != 0) {
                        channelId = msg.messageOwner.to_id.channel_id;
                    }
                }
                if (this.currentEncryptedChat != null) {
                    random_ids = new ArrayList();
                    for (b = 0; b < this.selectedMessagesIds[a].size(); b++) {
                        msg = (MessageObject) this.selectedMessagesIds[a].valueAt(b);
                        if (!(msg.messageOwner.random_id == 0 || msg.type == 10)) {
                            random_ids.add(Long.valueOf(msg.messageOwner.random_id));
                        }
                    }
                }
                MessagesController.getInstance(this.currentAccount).deleteMessages(ids, random_ids, this.currentEncryptedChat, channelId, deleteForAll[0]);
            }
            this.actionBar.hideActionMode();
            updatePinnedMessageView(true);
        }
        if (userFinal != null) {
            if (checks[0]) {
                MessagesController.getInstance(this.currentAccount).deleteUserFromChat(this.currentChat.id, userFinal, this.info);
            }
            if (checks[1]) {
                TL_channels_reportSpam req = new TL_channels_reportSpam();
                req.channel = MessagesController.getInputChannel(this.currentChat);
                req.user_id = MessagesController.getInstance(this.currentAccount).getInputUser(userFinal);
                req.id = ids;
                ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, ChatActivity$$Lambda$66.$instance);
            }
            if (checks[2]) {
                MessagesController.getInstance(this.currentAccount).deleteUserChannelHistory(this.currentChat, userFinal, 0);
            }
        }
    }

    static final /* synthetic */ void lambda$null$65$ChatActivity(TLObject response, TL_error error) {
    }

    private void createMenu(View v, boolean single, boolean listView) {
        createMenu(v, single, listView, true);
    }

    private void createMenu(View v, boolean single, boolean listView, boolean searchGroup) {
        if (!this.actionBar.isActionModeShowed()) {
            MessageObject message = null;
            if (v instanceof ChatMessageCell) {
                message = ((ChatMessageCell) v).getMessageObject();
            } else if (v instanceof ChatActionCell) {
                message = ((ChatActionCell) v).getMessageObject();
            }
            if (message != null) {
                int type = getMessageType(message);
                if (single && (message.messageOwner.action instanceof TL_messageActionPinMessage)) {
                    scrollToMessageId(message.messageOwner.reply_to_msg_id, message.messageOwner.id, true, 0, false);
                    return;
                }
                int a;
                GroupedMessages groupedMessages;
                boolean allowEdit;
                ArrayList<CharSequence> items;
                ArrayList<Integer> options;
                TL_messageActionPhoneCall call;
                Object string;
                User user;
                AlertDialog.Builder builder;
                this.selectedObject = null;
                this.selectedObjectGroup = null;
                this.forwardingMessage = null;
                this.forwardingMessageGroup = null;
                for (a = 1; a >= 0; a--) {
                    this.selectedMessagesCanCopyIds[a].clear();
                    this.selectedMessagesCanStarIds[a].clear();
                    this.selectedMessagesIds[a].clear();
                }
                this.cantDeleteMessagesCount = 0;
                this.canEditMessagesCount = 0;
                this.actionBar.hideActionMode();
                updatePinnedMessageView(true);
                if (searchGroup) {
                    groupedMessages = getValidGroupedMessage(message);
                } else {
                    groupedMessages = null;
                }
                boolean allowChatActions = true;
                boolean allowPin = message.getDialogId() != this.mergeDialogId && message.getId() > 0 && ChatObject.isChannel(this.currentChat) && ((this.currentChat.creator || (this.currentChat.admin_rights != null && ((this.currentChat.megagroup && this.currentChat.admin_rights.pin_messages) || (!this.currentChat.megagroup && this.currentChat.admin_rights.edit_messages)))) && (message.messageOwner.action == null || (message.messageOwner.action instanceof TL_messageActionEmpty)));
                boolean allowUnpin = message.getDialogId() != this.mergeDialogId && this.info != null && this.info.pinned_msg_id == message.getId() && (this.currentChat.creator || (this.currentChat.admin_rights != null && ((this.currentChat.megagroup && this.currentChat.admin_rights.pin_messages) || (!this.currentChat.megagroup && this.currentChat.admin_rights.edit_messages))));
                if (groupedMessages == null) {
                    if (!(!message.canEditMessage(this.currentChat) || this.chatActivityEnterView.hasAudioToSend() || message.getDialogId() == this.mergeDialogId)) {
                        allowEdit = true;
                        if ((this.currentEncryptedChat != null && AndroidUtilities.getPeerLayerVersion(this.currentEncryptedChat.layer) < 46) || ((type == 1 && (message.getDialogId() == this.mergeDialogId || message.needDrawBluredPreview())) || (message.messageOwner.action instanceof TL_messageActionSecureValuesSent) || ((this.currentEncryptedChat == null && message.getId() < 0) || ((this.bottomOverlayChat != null && this.bottomOverlayChat.getVisibility() == 0) || this.isBroadcast || (this.currentChat != null && (ChatObject.isNotInChat(this.currentChat) || !((!ChatObject.isChannel(this.currentChat) || ChatObject.canPost(this.currentChat) || this.currentChat.megagroup) && ChatObject.canSendMessages(this.currentChat)))))))) {
                            allowChatActions = false;
                        }
                        if (single && type >= 2 && type != 20 && !message.needDrawBluredPreview() && !message.isLiveLocation()) {
                            ActionBarMenu actionMode = this.actionBar.createActionMode();
                            View item = actionMode.getItem(11);
                            if (item != null) {
                                item.setVisibility(0);
                            }
                            item = actionMode.getItem(12);
                            if (item != null) {
                                item.setVisibility(0);
                            }
                            this.actionBar.showActionMode();
                            updatePinnedMessageView(true);
                            AnimatorSet animatorSet = new AnimatorSet();
                            ArrayList<Animator> animators = new ArrayList();
                            for (a = 0; a < this.actionModeViews.size(); a++) {
                                View view = (View) this.actionModeViews.get(a);
                                view.setPivotY((float) (ActionBar.getCurrentActionBarHeight() / 2));
                                AndroidUtilities.clearDrawableAnimation(view);
                                animators.add(ObjectAnimator.ofFloat(view, "scaleY", new float[]{0.1f, 1.0f}));
                            }
                            animatorSet.playTogether(animators);
                            animatorSet.setDuration(250);
                            animatorSet.start();
                            addToSelectedMessages(message, listView);
                            this.selectedMessagesCountTextView.setNumber(this.selectedMessagesIds[0].size() + this.selectedMessagesIds[1].size(), false);
                            updateVisibleRows();
                            return;
                        } else if (getParentActivity() != null) {
                            items = new ArrayList();
                            options = new ArrayList();
                            if (type >= 0 || (type == -1 && single && ((message.isSending() || message.isEditing()) && this.currentEncryptedChat == null))) {
                                this.selectedObject = message;
                                this.selectedObjectGroup = groupedMessages;
                                if (type == -1) {
                                    items.add(LocaleController.getString("CancelSending", R.string.CancelSending));
                                    options.add(Integer.valueOf(24));
                                } else if (type == 0) {
                                    items.add(LocaleController.getString("Retry", R.string.Retry));
                                    options.add(Integer.valueOf(0));
                                    items.add(LocaleController.getString("Delete", R.string.Delete));
                                    options.add(Integer.valueOf(1));
                                } else if (type == 1) {
                                    if (this.currentChat != null || this.isBroadcast) {
                                        if (message.messageOwner.action != null && (message.messageOwner.action instanceof TL_messageActionPhoneCall)) {
                                            call = (TL_messageActionPhoneCall) message.messageOwner.action;
                                            if ((!(call.reason instanceof TL_phoneCallDiscardReasonMissed) || (call.reason instanceof TL_phoneCallDiscardReasonBusy)) && !message.isOutOwner()) {
                                                string = LocaleController.getString("CallBack", R.string.CallBack);
                                            } else {
                                                string = LocaleController.getString("CallAgain", R.string.CallAgain);
                                            }
                                            items.add(string);
                                            options.add(Integer.valueOf(18));
                                            if (VoIPHelper.canRateCall(call)) {
                                                items.add(LocaleController.getString("CallMessageReportProblem", R.string.CallMessageReportProblem));
                                                options.add(Integer.valueOf(19));
                                            }
                                        }
                                        if (single && this.selectedObject.getId() > 0 && allowChatActions) {
                                            items.add(LocaleController.getString("Reply", R.string.Reply));
                                            options.add(Integer.valueOf(8));
                                        }
                                        if (message.canDeleteMessage(this.currentChat)) {
                                            items.add(LocaleController.getString("Delete", R.string.Delete));
                                            options.add(Integer.valueOf(1));
                                        }
                                    } else {
                                        if (allowChatActions) {
                                            items.add(LocaleController.getString("Reply", R.string.Reply));
                                            options.add(Integer.valueOf(8));
                                        }
                                        if (allowUnpin) {
                                            items.add(LocaleController.getString("UnpinMessage", R.string.UnpinMessage));
                                            options.add(Integer.valueOf(14));
                                        } else if (allowPin) {
                                            items.add(LocaleController.getString("PinMessage", R.string.PinMessage));
                                            options.add(Integer.valueOf(13));
                                        }
                                        if (allowEdit) {
                                            items.add(LocaleController.getString("Edit", R.string.Edit));
                                            options.add(Integer.valueOf(12));
                                        }
                                        if (this.selectedObject.contentType == 0 && this.selectedObject.getId() > 0 && !this.selectedObject.isOut() && (ChatObject.isChannel(this.currentChat) || (this.currentUser != null && this.currentUser.bot))) {
                                            items.add(LocaleController.getString("ReportChat", R.string.ReportChat));
                                            options.add(Integer.valueOf(23));
                                        }
                                        if (message.canDeleteMessage(this.currentChat)) {
                                            items.add(LocaleController.getString("Delete", R.string.Delete));
                                            options.add(Integer.valueOf(1));
                                        }
                                    }
                                } else if (type == 20) {
                                    items.add(LocaleController.getString("Retry", R.string.Retry));
                                    options.add(Integer.valueOf(0));
                                    items.add(LocaleController.getString("Copy", R.string.Copy));
                                    options.add(Integer.valueOf(3));
                                    items.add(LocaleController.getString("Delete", R.string.Delete));
                                    options.add(Integer.valueOf(1));
                                } else if (this.currentEncryptedChat != null) {
                                    if (allowChatActions) {
                                        items.add(LocaleController.getString("Reply", R.string.Reply));
                                        options.add(Integer.valueOf(8));
                                    }
                                    if (this.selectedObject.type == 0 || this.selectedObject.caption != null) {
                                        items.add(LocaleController.getString("Copy", R.string.Copy));
                                        options.add(Integer.valueOf(3));
                                    }
                                    if (ChatObject.isChannel(this.currentChat) && this.currentChat.megagroup && !TextUtils.isEmpty(this.currentChat.username) && ChatObject.hasAdminRights(this.currentChat)) {
                                        items.add(LocaleController.getString("CopyLink", R.string.CopyLink));
                                        options.add(Integer.valueOf(22));
                                    }
                                    if (type == 3) {
                                        if ((this.selectedObject.messageOwner.media instanceof TL_messageMediaWebPage) && MessageObject.isNewGifDocument(this.selectedObject.messageOwner.media.webpage.document)) {
                                            items.add(LocaleController.getString("SaveToGIFs", R.string.SaveToGIFs));
                                            options.add(Integer.valueOf(11));
                                        }
                                    } else if (type != 4) {
                                        if (this.selectedObject.isVideo()) {
                                            if (this.selectedObject.isMusic()) {
                                                items.add(LocaleController.getString("SaveToMusic", R.string.SaveToMusic));
                                                options.add(Integer.valueOf(10));
                                                items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                                options.add(Integer.valueOf(6));
                                            } else if (this.selectedObject.getDocument() != null) {
                                                if (MessageObject.isNewGifDocument(this.selectedObject.getDocument())) {
                                                    items.add(LocaleController.getString("SaveToGIFs", R.string.SaveToGIFs));
                                                    options.add(Integer.valueOf(11));
                                                }
                                                items.add(LocaleController.getString("SaveToDownloads", R.string.SaveToDownloads));
                                                options.add(Integer.valueOf(10));
                                                items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                                options.add(Integer.valueOf(6));
                                            } else if (!this.selectedObject.needDrawBluredPreview()) {
                                                items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                                                options.add(Integer.valueOf(4));
                                            }
                                        } else if (!this.selectedObject.needDrawBluredPreview()) {
                                            items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                                            options.add(Integer.valueOf(4));
                                            items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                            options.add(Integer.valueOf(6));
                                        }
                                    } else if (type == 5) {
                                        items.add(LocaleController.getString("ApplyLocalizationFile", R.string.ApplyLocalizationFile));
                                        options.add(Integer.valueOf(5));
                                        items.add(LocaleController.getString("SaveToDownloads", R.string.SaveToDownloads));
                                        options.add(Integer.valueOf(10));
                                        items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                        options.add(Integer.valueOf(6));
                                    } else if (type == 10) {
                                        items.add(LocaleController.getString("ApplyThemeFile", R.string.ApplyThemeFile));
                                        options.add(Integer.valueOf(5));
                                        items.add(LocaleController.getString("SaveToDownloads", R.string.SaveToDownloads));
                                        options.add(Integer.valueOf(10));
                                        items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                        options.add(Integer.valueOf(6));
                                    } else if (type == 6) {
                                        items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                                        options.add(Integer.valueOf(7));
                                        items.add(LocaleController.getString("SaveToDownloads", R.string.SaveToDownloads));
                                        options.add(Integer.valueOf(10));
                                        items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                        options.add(Integer.valueOf(6));
                                    } else if (type != 7) {
                                        if (this.selectedObject.isMask()) {
                                            items.add(LocaleController.getString("AddToStickers", R.string.AddToStickers));
                                            options.add(Integer.valueOf(9));
                                            if (!DataQuery.getInstance(this.currentAccount).isStickerInFavorites(this.selectedObject.getDocument())) {
                                                items.add(LocaleController.getString("DeleteFromFavorites", R.string.DeleteFromFavorites));
                                                options.add(Integer.valueOf(21));
                                            } else if (DataQuery.getInstance(this.currentAccount).canAddStickerToFavorites()) {
                                                items.add(LocaleController.getString("AddToFavorites", R.string.AddToFavorites));
                                                options.add(Integer.valueOf(20));
                                            }
                                        } else {
                                            items.add(LocaleController.getString("AddToMasks", R.string.AddToMasks));
                                            options.add(Integer.valueOf(9));
                                        }
                                    } else if (type == 8) {
                                        user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.selectedObject.messageOwner.media.user_id));
                                        if (!(user == null || user.id == UserConfig.getInstance(this.currentAccount).getClientUserId() || ContactsController.getInstance(this.currentAccount).contactsDict.get(Integer.valueOf(user.id)) != null)) {
                                            items.add(LocaleController.getString("AddContactTitle", R.string.AddContactTitle));
                                            options.add(Integer.valueOf(15));
                                        }
                                        if (!TextUtils.isEmpty(this.selectedObject.messageOwner.media.phone_number)) {
                                            items.add(LocaleController.getString("Copy", R.string.Copy));
                                            options.add(Integer.valueOf(16));
                                            items.add(LocaleController.getString("Call", R.string.Call));
                                            options.add(Integer.valueOf(17));
                                        }
                                    } else if (type == 9) {
                                        if (DataQuery.getInstance(this.currentAccount).isStickerInFavorites(this.selectedObject.getDocument())) {
                                            items.add(LocaleController.getString("AddToFavorites", R.string.AddToFavorites));
                                            options.add(Integer.valueOf(20));
                                        } else {
                                            items.add(LocaleController.getString("DeleteFromFavorites", R.string.DeleteFromFavorites));
                                            options.add(Integer.valueOf(21));
                                        }
                                    }
                                    if (!(this.selectedObject.needDrawBluredPreview() || this.selectedObject.isLiveLocation())) {
                                        items.add(LocaleController.getString("Forward", R.string.Forward));
                                        options.add(Integer.valueOf(2));
                                    }
                                    if (allowUnpin) {
                                        items.add(LocaleController.getString("UnpinMessage", R.string.UnpinMessage));
                                        options.add(Integer.valueOf(14));
                                    } else if (allowPin) {
                                        items.add(LocaleController.getString("PinMessage", R.string.PinMessage));
                                        options.add(Integer.valueOf(13));
                                    }
                                    if (allowEdit) {
                                        items.add(LocaleController.getString("Edit", R.string.Edit));
                                        options.add(Integer.valueOf(12));
                                    }
                                    if (this.selectedObject.contentType == 0 && this.selectedObject.getId() > 0 && !this.selectedObject.isOut() && (ChatObject.isChannel(this.currentChat) || (this.currentUser != null && this.currentUser.bot))) {
                                        items.add(LocaleController.getString("ReportChat", R.string.ReportChat));
                                        options.add(Integer.valueOf(23));
                                    }
                                    if (message.canDeleteMessage(this.currentChat)) {
                                        items.add(LocaleController.getString("Delete", R.string.Delete));
                                        options.add(Integer.valueOf(1));
                                    }
                                } else {
                                    if (allowChatActions) {
                                        items.add(LocaleController.getString("Reply", R.string.Reply));
                                        options.add(Integer.valueOf(8));
                                    }
                                    if (this.selectedObject.type == 0 || this.selectedObject.caption != null) {
                                        items.add(LocaleController.getString("Copy", R.string.Copy));
                                        options.add(Integer.valueOf(3));
                                    }
                                    if (type != 4) {
                                        if (this.selectedObject.isVideo()) {
                                            items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                                            options.add(Integer.valueOf(4));
                                            items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                            options.add(Integer.valueOf(6));
                                        } else if (this.selectedObject.isMusic()) {
                                            items.add(LocaleController.getString("SaveToMusic", R.string.SaveToMusic));
                                            options.add(Integer.valueOf(10));
                                            items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                            options.add(Integer.valueOf(6));
                                        } else if (!this.selectedObject.isVideo() || this.selectedObject.getDocument() == null) {
                                            items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                                            options.add(Integer.valueOf(4));
                                        } else {
                                            items.add(LocaleController.getString("SaveToDownloads", R.string.SaveToDownloads));
                                            options.add(Integer.valueOf(10));
                                            items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                            options.add(Integer.valueOf(6));
                                        }
                                    } else if (type == 5) {
                                        items.add(LocaleController.getString("ApplyLocalizationFile", R.string.ApplyLocalizationFile));
                                        options.add(Integer.valueOf(5));
                                    } else if (type == 10) {
                                        items.add(LocaleController.getString("ApplyThemeFile", R.string.ApplyThemeFile));
                                        options.add(Integer.valueOf(5));
                                    } else if (type == 7) {
                                        items.add(LocaleController.getString("AddToStickers", R.string.AddToStickers));
                                        options.add(Integer.valueOf(9));
                                    } else if (type == 8) {
                                        user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.selectedObject.messageOwner.media.user_id));
                                        if (!(user == null || user.id == UserConfig.getInstance(this.currentAccount).getClientUserId() || ContactsController.getInstance(this.currentAccount).contactsDict.get(Integer.valueOf(user.id)) != null)) {
                                            items.add(LocaleController.getString("AddContactTitle", R.string.AddContactTitle));
                                            options.add(Integer.valueOf(15));
                                        }
                                        if (!TextUtils.isEmpty(this.selectedObject.messageOwner.media.phone_number)) {
                                            items.add(LocaleController.getString("Copy", R.string.Copy));
                                            options.add(Integer.valueOf(16));
                                            items.add(LocaleController.getString("Call", R.string.Call));
                                            options.add(Integer.valueOf(17));
                                        }
                                    }
                                    items.add(LocaleController.getString("Delete", R.string.Delete));
                                    options.add(Integer.valueOf(1));
                                }
                            }
                            if (!options.isEmpty()) {
                                builder = new AlertDialog.Builder(getParentActivity());
                                builder.setItems((CharSequence[]) items.toArray(new CharSequence[items.size()]), new ChatActivity$$Lambda$52(this, options));
                                builder.setTitle(LocaleController.getString("Message", R.string.Message));
                                showDialog(builder.create());
                            }
                        }
                    }
                }
                allowEdit = false;
                allowChatActions = false;
                if (single) {
                }
                if (getParentActivity() != null) {
                    items = new ArrayList();
                    options = new ArrayList();
                    this.selectedObject = message;
                    this.selectedObjectGroup = groupedMessages;
                    if (type == -1) {
                        items.add(LocaleController.getString("CancelSending", R.string.CancelSending));
                        options.add(Integer.valueOf(24));
                    } else if (type == 0) {
                        items.add(LocaleController.getString("Retry", R.string.Retry));
                        options.add(Integer.valueOf(0));
                        items.add(LocaleController.getString("Delete", R.string.Delete));
                        options.add(Integer.valueOf(1));
                    } else if (type == 1) {
                        if (this.currentChat != null) {
                        }
                        call = (TL_messageActionPhoneCall) message.messageOwner.action;
                        if (call.reason instanceof TL_phoneCallDiscardReasonMissed) {
                        }
                        string = LocaleController.getString("CallBack", R.string.CallBack);
                        items.add(string);
                        options.add(Integer.valueOf(18));
                        if (VoIPHelper.canRateCall(call)) {
                            items.add(LocaleController.getString("CallMessageReportProblem", R.string.CallMessageReportProblem));
                            options.add(Integer.valueOf(19));
                        }
                        items.add(LocaleController.getString("Reply", R.string.Reply));
                        options.add(Integer.valueOf(8));
                        if (message.canDeleteMessage(this.currentChat)) {
                            items.add(LocaleController.getString("Delete", R.string.Delete));
                            options.add(Integer.valueOf(1));
                        }
                    } else if (type == 20) {
                        items.add(LocaleController.getString("Retry", R.string.Retry));
                        options.add(Integer.valueOf(0));
                        items.add(LocaleController.getString("Copy", R.string.Copy));
                        options.add(Integer.valueOf(3));
                        items.add(LocaleController.getString("Delete", R.string.Delete));
                        options.add(Integer.valueOf(1));
                    } else if (this.currentEncryptedChat != null) {
                        if (allowChatActions) {
                            items.add(LocaleController.getString("Reply", R.string.Reply));
                            options.add(Integer.valueOf(8));
                        }
                        items.add(LocaleController.getString("Copy", R.string.Copy));
                        options.add(Integer.valueOf(3));
                        if (type != 4) {
                            if (type == 5) {
                                items.add(LocaleController.getString("ApplyLocalizationFile", R.string.ApplyLocalizationFile));
                                options.add(Integer.valueOf(5));
                            } else if (type == 10) {
                                items.add(LocaleController.getString("ApplyThemeFile", R.string.ApplyThemeFile));
                                options.add(Integer.valueOf(5));
                            } else if (type == 7) {
                                items.add(LocaleController.getString("AddToStickers", R.string.AddToStickers));
                                options.add(Integer.valueOf(9));
                            } else if (type == 8) {
                                user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.selectedObject.messageOwner.media.user_id));
                                items.add(LocaleController.getString("AddContactTitle", R.string.AddContactTitle));
                                options.add(Integer.valueOf(15));
                                if (TextUtils.isEmpty(this.selectedObject.messageOwner.media.phone_number)) {
                                    items.add(LocaleController.getString("Copy", R.string.Copy));
                                    options.add(Integer.valueOf(16));
                                    items.add(LocaleController.getString("Call", R.string.Call));
                                    options.add(Integer.valueOf(17));
                                }
                            }
                        } else if (this.selectedObject.isVideo()) {
                            items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                            options.add(Integer.valueOf(4));
                            items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                            options.add(Integer.valueOf(6));
                        } else if (this.selectedObject.isMusic()) {
                            items.add(LocaleController.getString("SaveToMusic", R.string.SaveToMusic));
                            options.add(Integer.valueOf(10));
                            items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                            options.add(Integer.valueOf(6));
                        } else {
                            if (this.selectedObject.isVideo()) {
                            }
                            items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                            options.add(Integer.valueOf(4));
                        }
                        items.add(LocaleController.getString("Delete", R.string.Delete));
                        options.add(Integer.valueOf(1));
                    } else {
                        if (allowChatActions) {
                            items.add(LocaleController.getString("Reply", R.string.Reply));
                            options.add(Integer.valueOf(8));
                        }
                        items.add(LocaleController.getString("Copy", R.string.Copy));
                        options.add(Integer.valueOf(3));
                        items.add(LocaleController.getString("CopyLink", R.string.CopyLink));
                        options.add(Integer.valueOf(22));
                        if (type == 3) {
                            items.add(LocaleController.getString("SaveToGIFs", R.string.SaveToGIFs));
                            options.add(Integer.valueOf(11));
                        } else if (type != 4) {
                            if (type == 5) {
                                items.add(LocaleController.getString("ApplyLocalizationFile", R.string.ApplyLocalizationFile));
                                options.add(Integer.valueOf(5));
                                items.add(LocaleController.getString("SaveToDownloads", R.string.SaveToDownloads));
                                options.add(Integer.valueOf(10));
                                items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                options.add(Integer.valueOf(6));
                            } else if (type == 10) {
                                items.add(LocaleController.getString("ApplyThemeFile", R.string.ApplyThemeFile));
                                options.add(Integer.valueOf(5));
                                items.add(LocaleController.getString("SaveToDownloads", R.string.SaveToDownloads));
                                options.add(Integer.valueOf(10));
                                items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                options.add(Integer.valueOf(6));
                            } else if (type == 6) {
                                items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                                options.add(Integer.valueOf(7));
                                items.add(LocaleController.getString("SaveToDownloads", R.string.SaveToDownloads));
                                options.add(Integer.valueOf(10));
                                items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                options.add(Integer.valueOf(6));
                            } else if (type != 7) {
                                if (type == 8) {
                                    user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.selectedObject.messageOwner.media.user_id));
                                    items.add(LocaleController.getString("AddContactTitle", R.string.AddContactTitle));
                                    options.add(Integer.valueOf(15));
                                    if (TextUtils.isEmpty(this.selectedObject.messageOwner.media.phone_number)) {
                                        items.add(LocaleController.getString("Copy", R.string.Copy));
                                        options.add(Integer.valueOf(16));
                                        items.add(LocaleController.getString("Call", R.string.Call));
                                        options.add(Integer.valueOf(17));
                                    }
                                } else if (type == 9) {
                                    if (DataQuery.getInstance(this.currentAccount).isStickerInFavorites(this.selectedObject.getDocument())) {
                                        items.add(LocaleController.getString("DeleteFromFavorites", R.string.DeleteFromFavorites));
                                        options.add(Integer.valueOf(21));
                                    } else {
                                        items.add(LocaleController.getString("AddToFavorites", R.string.AddToFavorites));
                                        options.add(Integer.valueOf(20));
                                    }
                                }
                            } else if (this.selectedObject.isMask()) {
                                items.add(LocaleController.getString("AddToStickers", R.string.AddToStickers));
                                options.add(Integer.valueOf(9));
                                if (!DataQuery.getInstance(this.currentAccount).isStickerInFavorites(this.selectedObject.getDocument())) {
                                    items.add(LocaleController.getString("DeleteFromFavorites", R.string.DeleteFromFavorites));
                                    options.add(Integer.valueOf(21));
                                } else if (DataQuery.getInstance(this.currentAccount).canAddStickerToFavorites()) {
                                    items.add(LocaleController.getString("AddToFavorites", R.string.AddToFavorites));
                                    options.add(Integer.valueOf(20));
                                }
                            } else {
                                items.add(LocaleController.getString("AddToMasks", R.string.AddToMasks));
                                options.add(Integer.valueOf(9));
                            }
                        } else if (this.selectedObject.isVideo()) {
                            if (this.selectedObject.isMusic()) {
                                items.add(LocaleController.getString("SaveToMusic", R.string.SaveToMusic));
                                options.add(Integer.valueOf(10));
                                items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                options.add(Integer.valueOf(6));
                            } else if (this.selectedObject.getDocument() != null) {
                                if (MessageObject.isNewGifDocument(this.selectedObject.getDocument())) {
                                    items.add(LocaleController.getString("SaveToGIFs", R.string.SaveToGIFs));
                                    options.add(Integer.valueOf(11));
                                }
                                items.add(LocaleController.getString("SaveToDownloads", R.string.SaveToDownloads));
                                options.add(Integer.valueOf(10));
                                items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                                options.add(Integer.valueOf(6));
                            } else if (this.selectedObject.needDrawBluredPreview()) {
                                items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                                options.add(Integer.valueOf(4));
                            }
                        } else if (this.selectedObject.needDrawBluredPreview()) {
                            items.add(LocaleController.getString("SaveToGallery", R.string.SaveToGallery));
                            options.add(Integer.valueOf(4));
                            items.add(LocaleController.getString("ShareFile", R.string.ShareFile));
                            options.add(Integer.valueOf(6));
                        }
                        items.add(LocaleController.getString("Forward", R.string.Forward));
                        options.add(Integer.valueOf(2));
                        if (allowUnpin) {
                            items.add(LocaleController.getString("UnpinMessage", R.string.UnpinMessage));
                            options.add(Integer.valueOf(14));
                        } else if (allowPin) {
                            items.add(LocaleController.getString("PinMessage", R.string.PinMessage));
                            options.add(Integer.valueOf(13));
                        }
                        if (allowEdit) {
                            items.add(LocaleController.getString("Edit", R.string.Edit));
                            options.add(Integer.valueOf(12));
                        }
                        items.add(LocaleController.getString("ReportChat", R.string.ReportChat));
                        options.add(Integer.valueOf(23));
                        if (message.canDeleteMessage(this.currentChat)) {
                            items.add(LocaleController.getString("Delete", R.string.Delete));
                            options.add(Integer.valueOf(1));
                        }
                    }
                    if (!options.isEmpty()) {
                        builder = new AlertDialog.Builder(getParentActivity());
                        builder.setItems((CharSequence[]) items.toArray(new CharSequence[items.size()]), new ChatActivity$$Lambda$52(this, options));
                        builder.setTitle(LocaleController.getString("Message", R.string.Message));
                        showDialog(builder.create());
                    }
                }
            }
        }
    }

    final /* synthetic */ void lambda$createMenu$67$ChatActivity(ArrayList options, DialogInterface dialogInterface, int i) {
        if (this.selectedObject != null && i >= 0 && i < options.size()) {
            processSelectedOption(((Integer) options.get(i)).intValue());
        }
    }

    private void startEditingMessageObject(MessageObject messageObject) {
        if (messageObject != null && getParentActivity() != null) {
            if (this.searchItem != null && this.actionBar.isSearchFieldVisible()) {
                this.actionBar.closeSearchField();
                this.chatActivityEnterView.setFieldFocused();
            }
            this.mentionsAdapter.setNeedBotContext(false);
            this.chatListView.setOnItemLongClickListener((OnItemLongClickListenerExtended) null);
            this.chatListView.setOnItemClickListener((OnItemClickListenerExtended) null);
            this.chatListView.setClickable(false);
            this.chatListView.setLongClickable(false);
            this.chatActivityEnterView.setVisibility(0);
            showFieldPanelForEdit(true, messageObject);
            updateBottomOverlay();
            checkEditTimer();
            this.chatActivityEnterView.setAllowStickersAndGifs(false, false);
            updatePinnedMessageView(true);
            updateVisibleRows();
            TL_messages_getMessageEditData req = new TL_messages_getMessageEditData();
            req.peer = MessagesController.getInstance(this.currentAccount).getInputPeer((int) this.dialog_id);
            req.id = messageObject.getId();
            this.editingMessageObjectReqId = ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, new ChatActivity$$Lambda$53(this));
        }
    }

    final /* synthetic */ void lambda$startEditingMessageObject$69$ChatActivity(TLObject response, TL_error error) {
        AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$65(this, response));
    }

    final /* synthetic */ void lambda$null$68$ChatActivity(TLObject response) {
        this.editingMessageObjectReqId = 0;
        if (response == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(LocaleController.getString("EditMessageError", R.string.EditMessageError));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            showDialog(builder.create());
            if (this.chatActivityEnterView != null) {
                this.chatActivityEnterView.setEditingMessageObject(null, false);
                hideFieldPanel();
            }
        } else if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.showEditDoneProgress(false, true);
        }
    }

    private String getMessageContent(MessageObject messageObject, int previousUid, boolean name) {
        String str = TtmlNode.ANONYMOUS_REGION_ID;
        if (name && previousUid != messageObject.messageOwner.from_id) {
            if (messageObject.messageOwner.from_id > 0) {
                User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.from_id));
                if (user != null) {
                    str = ContactsController.formatName(user.first_name, user.last_name) + ":\n";
                }
            } else if (messageObject.messageOwner.from_id < 0) {
                Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-messageObject.messageOwner.from_id));
                if (chat != null) {
                    str = chat.title + ":\n";
                }
            }
        }
        if (messageObject.type == 0 && messageObject.messageOwner.message != null) {
            return str + messageObject.messageOwner.message;
        }
        if (messageObject.messageOwner.media == null || messageObject.messageOwner.message == null) {
            return str + messageObject.messageText;
        }
        return str + messageObject.messageOwner.message;
    }

    private void saveMessageToGallery(MessageObject messageObject) {
        String path = messageObject.messageOwner.attachPath;
        if (!(TextUtils.isEmpty(path) || new File(path).exists())) {
            path = null;
        }
        if (TextUtils.isEmpty(path)) {
            path = FileLoader.getPathToMessage(messageObject.messageOwner).toString();
        }
        MediaController.saveFile(path, getParentActivity(), messageObject.isVideo() ? 1 : 0, null, null);
    }

    private void processSelectedOption(int option) {
        File file;
        Intent intent;
        if (this.selectedObject != null && getParentActivity() != null) {
            int a;
            Bundle args;
            AlertDialog.Builder builder;
            String path;
            switch (option) {
                case 0:
                    if (this.selectedObjectGroup == null) {
                        if (SendMessagesHelper.getInstance(this.currentAccount).retrySendMessage(this.selectedObject, false)) {
                            updateVisibleRows();
                            moveScrollToLastMessage();
                            break;
                        }
                    }
                    boolean success = true;
                    for (a = 0; a < this.selectedObjectGroup.messages.size(); a++) {
                        if (!SendMessagesHelper.getInstance(this.currentAccount).retrySendMessage((MessageObject) this.selectedObjectGroup.messages.get(a), false)) {
                            success = false;
                        }
                    }
                    if (success) {
                        moveScrollToLastMessage();
                        break;
                    }
                    break;
                case 1:
                    if (getParentActivity() != null) {
                        createDeleteMessagesAlert(this.selectedObject, this.selectedObjectGroup);
                        break;
                    }
                    this.selectedObject = null;
                    this.selectedObjectGroup = null;
                    return;
                case 2:
                    this.forwardingMessage = this.selectedObject;
                    this.forwardingMessageGroup = this.selectedObjectGroup;
                    args = new Bundle();
                    args.putBoolean("onlySelect", true);
                    args.putInt("dialogsType", 3);
                    BaseFragment dialogsActivity = new DialogsActivity(args);
                    dialogsActivity.setDelegate(this);
                    presentFragment(dialogsActivity);
                    break;
                case 3:
                    AndroidUtilities.addToClipboard(getMessageContent(this.selectedObject, 0, false));
                    break;
                case 4:
                    if (VERSION.SDK_INT < 23 || getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                        if (this.selectedObjectGroup == null) {
                            saveMessageToGallery(this.selectedObject);
                            break;
                        }
                        for (a = 0; a < this.selectedObjectGroup.messages.size(); a++) {
                            saveMessageToGallery((MessageObject) this.selectedObjectGroup.messages.get(a));
                        }
                        break;
                    }
                    getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                    this.selectedObject = null;
                    this.selectedObjectGroup = null;
                    return;
                case 5:
                    File locFile = null;
                    if (!TextUtils.isEmpty(this.selectedObject.messageOwner.attachPath)) {
                        file = new File(this.selectedObject.messageOwner.attachPath);
                        if (file.exists()) {
                            locFile = file;
                        }
                    }
                    if (locFile == null) {
                        File f = FileLoader.getPathToMessage(this.selectedObject.messageOwner);
                        if (f.exists()) {
                            locFile = f;
                        }
                    }
                    if (locFile != null) {
                        if (!locFile.getName().toLowerCase().endsWith("attheme")) {
                            if (!LocaleController.getInstance().applyLanguageFile(locFile, this.currentAccount)) {
                                if (getParentActivity() != null) {
                                    builder = new AlertDialog.Builder(getParentActivity());
                                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                    builder.setMessage(LocaleController.getString("IncorrectLocalization", R.string.IncorrectLocalization));
                                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                                    showDialog(builder.create());
                                    break;
                                }
                                this.selectedObject = null;
                                this.selectedObjectGroup = null;
                                return;
                            }
                            presentFragment(new LanguageSelectActivity());
                            break;
                        }
                        if (this.chatLayoutManager != null) {
                            int lastPosition = this.chatLayoutManager.findFirstVisibleItemPosition();
                            if (lastPosition != 0) {
                                this.scrollToPositionOnRecreate = lastPosition;
                                Holder holder = (Holder) this.chatListView.findViewHolderForAdapterPosition(this.scrollToPositionOnRecreate);
                                if (holder != null) {
                                    this.scrollToOffsetOnRecreate = (this.chatListView.getMeasuredHeight() - holder.itemView.getBottom()) - this.chatListView.getPaddingBottom();
                                } else {
                                    this.scrollToPositionOnRecreate = -1;
                                }
                            } else {
                                this.scrollToPositionOnRecreate = -1;
                            }
                        }
                        ThemeInfo themeInfo = Theme.applyThemeFile(locFile, this.selectedObject.getDocumentName(), true);
                        if (themeInfo == null) {
                            this.scrollToPositionOnRecreate = -1;
                            if (getParentActivity() != null) {
                                builder = new AlertDialog.Builder(getParentActivity());
                                builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                                builder.setMessage(LocaleController.getString("IncorrectTheme", R.string.IncorrectTheme));
                                builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
                                showDialog(builder.create());
                                break;
                            }
                            this.selectedObject = null;
                            this.selectedObjectGroup = null;
                            return;
                        }
                        presentFragment(new ThemePreviewActivity(locFile, themeInfo));
                        break;
                    }
                    break;
                case 6:
                    path = this.selectedObject.messageOwner.attachPath;
                    if (!(path == null || path.length() <= 0 || new File(path).exists())) {
                        path = null;
                    }
                    if (path == null || path.length() == 0) {
                        path = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
                    }
                    intent = new Intent("android.intent.action.SEND");
                    intent.setType(this.selectedObject.getDocument().mime_type);
                    file = new File(path);
                    if (VERSION.SDK_INT >= 24) {
                        try {
                            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(getParentActivity(), "org.telegram.messenger.beta.provider", file));
                            intent.setFlags(1);
                        } catch (Exception e) {
                            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                        }
                    } else {
                        intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
                    }
                    try {
                        getParentActivity().startActivityForResult(Intent.createChooser(intent, LocaleController.getString("ShareFile", R.string.ShareFile)), 500);
                        break;
                    } catch (Throwable th) {
                        break;
                    }
                case 7:
                    path = this.selectedObject.messageOwner.attachPath;
                    if (!(path == null || path.length() <= 0 || new File(path).exists())) {
                        path = null;
                    }
                    if (path == null || path.length() == 0) {
                        path = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
                    }
                    if (VERSION.SDK_INT < 23 || getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                        MediaController.saveFile(path, getParentActivity(), 0, null, null);
                        break;
                    }
                    getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                    this.selectedObject = null;
                    this.selectedObjectGroup = null;
                    return;
                    break;
                case 8:
                    showFieldPanelForReply(true, this.selectedObject);
                    break;
                case 9:
                    Context parentActivity = getParentActivity();
                    InputStickerSet inputStickerSet = this.selectedObject.getInputStickerSet();
                    StickersAlertDelegate stickersAlertDelegate = (this.bottomOverlayChat.getVisibility() == 0 || !ChatObject.canSendStickers(this.currentChat)) ? null : this.chatActivityEnterView;
                    showDialog(new StickersAlert(parentActivity, this, inputStickerSet, null, stickersAlertDelegate));
                    break;
                case 10:
                    if (VERSION.SDK_INT < 23 || getParentActivity().checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                        String fileName = FileLoader.getDocumentFileName(this.selectedObject.getDocument());
                        if (TextUtils.isEmpty(fileName)) {
                            fileName = this.selectedObject.getFileName();
                        }
                        path = this.selectedObject.messageOwner.attachPath;
                        if (!(path == null || path.length() <= 0 || new File(path).exists())) {
                            path = null;
                        }
                        if (path == null || path.length() == 0) {
                            path = FileLoader.getPathToMessage(this.selectedObject.messageOwner).toString();
                        }
                        MediaController.saveFile(path, getParentActivity(), this.selectedObject.isMusic() ? 3 : 2, fileName, this.selectedObject.getDocument() != null ? this.selectedObject.getDocument().mime_type : TtmlNode.ANONYMOUS_REGION_ID);
                        break;
                    }
                    getParentActivity().requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 4);
                    this.selectedObject = null;
                    this.selectedObjectGroup = null;
                    return;
                    break;
                case 11:
                    Document document = this.selectedObject.getDocument();
                    MessagesController.getInstance(this.currentAccount).saveGif(document);
                    showGifHint();
                    this.chatActivityEnterView.addRecentGif(document);
                    break;
                case 12:
                    startEditingMessageObject(this.selectedObject);
                    this.selectedObject = null;
                    this.selectedObjectGroup = null;
                    break;
                case 13:
                    boolean[] checks;
                    int mid = this.selectedObject.getId();
                    builder = new AlertDialog.Builder(getParentActivity());
                    if (ChatObject.isChannel(this.currentChat) && this.currentChat.megagroup) {
                        builder.setMessage(LocaleController.getString("PinMessageAlert", R.string.PinMessageAlert));
                        checks = new boolean[]{true};
                        View frameLayout = new FrameLayout(getParentActivity());
                        CheckBoxCell cell = new CheckBoxCell(getParentActivity(), 1);
                        cell.setBackgroundDrawable(Theme.getSelectorDrawable(false));
                        cell.setText(LocaleController.getString("PinNotify", R.string.PinNotify), TtmlNode.ANONYMOUS_REGION_ID, true, false);
                        cell.setPadding(LocaleController.isRTL ? AndroidUtilities.dp(8.0f) : 0, 0, LocaleController.isRTL ? 0 : AndroidUtilities.dp(8.0f), 0);
                        frameLayout.addView(cell, LayoutHelper.createFrame(-1, 48.0f, 51, 8.0f, 0.0f, 8.0f, 0.0f));
                        cell.setOnClickListener(new ChatActivity$$Lambda$54(checks));
                        builder.setView(frameLayout);
                    } else {
                        builder.setMessage(LocaleController.getString("PinMessageAlertChannel", R.string.PinMessageAlertChannel));
                        checks = new boolean[]{false};
                    }
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$55(this, mid, checks));
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    showDialog(builder.create());
                    break;
                case 14:
                    builder = new AlertDialog.Builder(getParentActivity());
                    builder.setMessage(LocaleController.getString("UnpinMessageAlert", R.string.UnpinMessageAlert));
                    builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$56(this));
                    builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
                    builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
                    showDialog(builder.create());
                    break;
                case 15:
                    args = new Bundle();
                    args.putInt("user_id", this.selectedObject.messageOwner.media.user_id);
                    args.putString("phone", this.selectedObject.messageOwner.media.phone_number);
                    args.putBoolean("addContact", true);
                    presentFragment(new ContactAddActivity(args));
                    break;
                case 16:
                    AndroidUtilities.addToClipboard(this.selectedObject.messageOwner.media.phone_number);
                    break;
                case 17:
                    try {
                        intent = new Intent("android.intent.action.DIAL", Uri.parse("tel:" + this.selectedObject.messageOwner.media.phone_number));
                        intent.addFlags(C.ENCODING_PCM_MU_LAW);
                        getParentActivity().startActivityForResult(intent, 500);
                        break;
                    } catch (Throwable e2) {
                        FileLog.e(e2);
                        break;
                    }
                case 18:
                    if (this.currentUser != null) {
                        VoIPHelper.startCall(this.currentUser, getParentActivity(), MessagesController.getInstance(this.currentAccount).getUserFull(this.currentUser.id));
                        break;
                    }
                    break;
                case 19:
                    VoIPHelper.showRateAlert(getParentActivity(), (TL_messageActionPhoneCall) this.selectedObject.messageOwner.action);
                    break;
                case 20:
                    DataQuery.getInstance(this.currentAccount).addRecentSticker(2, this.selectedObject.getDocument(), (int) (System.currentTimeMillis() / 1000), false);
                    break;
                case 21:
                    DataQuery.getInstance(this.currentAccount).addRecentSticker(2, this.selectedObject.getDocument(), (int) (System.currentTimeMillis() / 1000), true);
                    break;
                case 22:
                    TLObject req = new TL_channels_exportMessageLink();
                    req.id = this.selectedObject.getId();
                    req.channel = MessagesController.getInputChannel(this.currentChat);
                    ConnectionsManager.getInstance(this.currentAccount).sendRequest(req, ChatActivity$$Lambda$57.$instance);
                    break;
                case 23:
                    showDialog(AlertsCreator.createReportAlert(getParentActivity(), this.dialog_id, this.selectedObject.getId(), this));
                    break;
                case 24:
                    if (!this.selectedObject.isEditing() && (!this.selectedObject.isSending() || this.selectedObjectGroup != null)) {
                        if (this.selectedObject.isSending() && this.selectedObjectGroup != null) {
                            for (a = 0; a < this.selectedObjectGroup.messages.size(); a++) {
                                SendMessagesHelper.getInstance(this.currentAccount).cancelSendingMessage(new ArrayList(this.selectedObjectGroup.messages));
                            }
                            break;
                        }
                    }
                    SendMessagesHelper.getInstance(this.currentAccount).cancelSendingMessage(this.selectedObject);
                    break;
                    break;
            }
            this.selectedObject = null;
            this.selectedObjectGroup = null;
        }
    }

    static final /* synthetic */ void lambda$processSelectedOption$70$ChatActivity(boolean[] checks, View v) {
        boolean z;
        CheckBoxCell cell1 = (CheckBoxCell) v;
        if (checks[0]) {
            z = false;
        } else {
            z = true;
        }
        checks[0] = z;
        cell1.setChecked(checks[0], true);
    }

    final /* synthetic */ void lambda$processSelectedOption$71$ChatActivity(int mid, boolean[] checks, DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).pinChannelMessage(this.currentChat, mid, checks[0]);
    }

    final /* synthetic */ void lambda$processSelectedOption$72$ChatActivity(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.currentAccount).pinChannelMessage(this.currentChat, 0, false);
    }

    static final /* synthetic */ void lambda$null$73$ChatActivity(TLObject response) {
        if (response != null) {
            try {
                ((ClipboardManager) ApplicationLoader.applicationContext.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", ((TL_exportedMessageLink) response).link));
                Toast.makeText(ApplicationLoader.applicationContext, LocaleController.getString("LinkCopied", R.string.LinkCopied), 0).show();
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public void didSelectDialogs(DialogsActivity fragment, ArrayList<Long> dids, CharSequence message, boolean param) {
        if (this.forwardingMessage != null || this.selectedMessagesIds[0].size() != 0 || this.selectedMessagesIds[1].size() != 0) {
            int a;
            ArrayList<MessageObject> fmessages = new ArrayList();
            if (this.forwardingMessage != null) {
                if (this.forwardingMessageGroup != null) {
                    fmessages.addAll(this.forwardingMessageGroup.messages);
                } else {
                    fmessages.add(this.forwardingMessage);
                }
                this.forwardingMessage = null;
                this.forwardingMessageGroup = null;
            } else {
                for (a = 1; a >= 0; a--) {
                    int b;
                    ArrayList<Integer> ids = new ArrayList();
                    for (b = 0; b < this.selectedMessagesIds[a].size(); b++) {
                        ids.add(Integer.valueOf(this.selectedMessagesIds[a].keyAt(b)));
                    }
                    Collections.sort(ids);
                    for (b = 0; b < ids.size(); b++) {
                        Integer id = (Integer) ids.get(b);
                        MessageObject messageObject = (MessageObject) this.selectedMessagesIds[a].get(id.intValue());
                        if (messageObject != null && id.intValue() > 0) {
                            fmessages.add(messageObject);
                        }
                    }
                    this.selectedMessagesCanCopyIds[a].clear();
                    this.selectedMessagesCanStarIds[a].clear();
                    this.selectedMessagesIds[a].clear();
                }
                this.cantDeleteMessagesCount = 0;
                this.canEditMessagesCount = 0;
                this.actionBar.hideActionMode();
                updatePinnedMessageView(true);
            }
            long did;
            if (dids.size() > 1 || ((Long) dids.get(0)).longValue() == ((long) UserConfig.getInstance(this.currentAccount).getClientUserId()) || message != null) {
                for (a = 0; a < dids.size(); a++) {
                    did = ((Long) dids.get(a)).longValue();
                    if (message != null) {
                        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(message.toString(), did, null, null, true, null, null, null);
                    }
                    SendMessagesHelper.getInstance(this.currentAccount).sendMessage(fmessages, did);
                }
                fragment.finishFragment();
                return;
            }
            did = ((Long) dids.get(0)).longValue();
            if (did != this.dialog_id) {
                int lower_part = (int) did;
                int high_part = (int) (did >> 32);
                Bundle args = new Bundle();
                args.putBoolean("scrollToTopOnResume", this.scrollToTopOnResume);
                if (lower_part == 0) {
                    args.putInt("enc_id", high_part);
                } else if (lower_part > 0) {
                    args.putInt("user_id", lower_part);
                } else if (lower_part < 0) {
                    args.putInt("chat_id", -lower_part);
                }
                if (lower_part == 0 || MessagesController.getInstance(this.currentAccount).checkCanOpenChat(args, fragment)) {
                    ChatActivity chatActivity = new ChatActivity(args);
                    if (presentFragment(chatActivity, true)) {
                        chatActivity.showFieldPanelForForward(true, fmessages);
                        if (!AndroidUtilities.isTablet()) {
                            removeSelfFromStack();
                            return;
                        }
                        return;
                    }
                    fragment.finishFragment();
                    return;
                }
                return;
            }
            fragment.finishFragment();
            moveScrollToLastMessage();
            showFieldPanelForForward(true, fmessages);
            if (AndroidUtilities.isTablet()) {
                this.actionBar.hideActionMode();
                updatePinnedMessageView(true);
            }
            updateVisibleRows();
        }
    }

    public boolean checkRecordLocked() {
        if (this.chatActivityEnterView == null || !this.chatActivityEnterView.isRecordLocked()) {
            return false;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        if (this.chatActivityEnterView.isInVideoMode()) {
            builder.setTitle(LocaleController.getString("DiscardVideoMessageTitle", R.string.DiscardVideoMessageTitle));
            builder.setMessage(LocaleController.getString("DiscardVideoMessageDescription", R.string.DiscardVideoMessageDescription));
        } else {
            builder.setTitle(LocaleController.getString("DiscardVoiceMessageTitle", R.string.DiscardVoiceMessageTitle));
            builder.setMessage(LocaleController.getString("DiscardVoiceMessageDescription", R.string.DiscardVoiceMessageDescription));
        }
        builder.setPositiveButton(LocaleController.getString("DiscardVoiceMessageAction", R.string.DiscardVoiceMessageAction), new ChatActivity$$Lambda$58(this));
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
        return true;
    }

    final /* synthetic */ void lambda$checkRecordLocked$75$ChatActivity(DialogInterface dialog, int which) {
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.cancelRecordingAudioVideo();
        }
    }

    public boolean onBackPressed() {
        if (checkRecordLocked()) {
            return false;
        }
        if (this.actionBar != null && this.actionBar.isActionModeShowed()) {
            for (int a = 1; a >= 0; a--) {
                this.selectedMessagesIds[a].clear();
                this.selectedMessagesCanCopyIds[a].clear();
                this.selectedMessagesCanStarIds[a].clear();
            }
            this.actionBar.hideActionMode();
            updatePinnedMessageView(true);
            this.cantDeleteMessagesCount = 0;
            this.canEditMessagesCount = 0;
            updateVisibleRows();
            return false;
        } else if (this.chatActivityEnterView == null || !this.chatActivityEnterView.isPopupShowing()) {
            return true;
        } else {
            this.chatActivityEnterView.hidePopup(true);
            return false;
        }
    }

    private void updateVisibleRows() {
        if (this.chatListView != null) {
            int lastVisibleItem = -1;
            if (!(this.wasManualScroll || this.unreadMessageObject == null || this.chatListView.getMeasuredHeight() == 0)) {
                int pos = this.messages.indexOf(this.unreadMessageObject);
                if (pos >= 0) {
                    lastVisibleItem = this.chatAdapter.messagesStartRow + pos;
                }
            }
            int count = this.chatListView.getChildCount();
            MessageObject editingMessageObject = this.chatActivityEnterView != null ? this.chatActivityEnterView.getEditingMessageObject() : null;
            for (int a = 0; a < count; a++) {
                View view = this.chatListView.getChildAt(a);
                if (view instanceof ChatMessageCell) {
                    ChatMessageCell cell = (ChatMessageCell) view;
                    MessageObject messageObject = cell.getMessageObject();
                    boolean disableSelection = false;
                    boolean selected = false;
                    if (this.actionBar.isActionModeShowed()) {
                        int idx = messageObject.getDialogId() == this.dialog_id ? 0 : 1;
                        if (messageObject == editingMessageObject || this.selectedMessagesIds[idx].indexOfKey(messageObject.getId()) >= 0) {
                            setCellSelectionBackground(messageObject, cell, idx);
                            selected = true;
                        } else {
                            view.setBackgroundDrawable(null);
                        }
                        disableSelection = true;
                    } else {
                        view.setBackgroundDrawable(null);
                    }
                    cell.setMessageObject(cell.getMessageObject(), cell.getCurrentMessagesGroup(), cell.isPinnedBottom(), cell.isPinnedTop());
                    boolean z = !disableSelection;
                    boolean z2 = disableSelection && selected;
                    cell.setCheckPressed(z, z2);
                    z2 = (this.highlightMessageId == ConnectionsManager.DEFAULT_DATACENTER_ID || messageObject == null || messageObject.getId() != this.highlightMessageId) ? false : true;
                    cell.setHighlighted(z2);
                    if (this.searchContainer != null && this.searchContainer.getVisibility() == 0) {
                        if (DataQuery.getInstance(this.currentAccount).isMessageFound(messageObject.getId(), messageObject.getDialogId() == this.mergeDialogId) && DataQuery.getInstance(this.currentAccount).getLastSearchQuery() != null) {
                            cell.setHighlightedText(DataQuery.getInstance(this.currentAccount).getLastSearchQuery());
                        }
                    }
                    cell.setHighlightedText(null);
                } else if (view instanceof ChatActionCell) {
                    ChatActionCell cell2 = (ChatActionCell) view;
                    cell2.setMessageObject(cell2.getMessageObject());
                }
            }
            this.chatListView.invalidate();
            if (lastVisibleItem != -1) {
                this.chatLayoutManager.scrollToPositionWithOffset(lastVisibleItem, ((this.chatListView.getMeasuredHeight() - this.chatListView.getPaddingBottom()) - this.chatListView.getPaddingTop()) - AndroidUtilities.dp(29.0f));
            }
        }
    }

    private void checkEditTimer() {
        if (this.chatActivityEnterView != null) {
            MessageObject messageObject = this.chatActivityEnterView.getEditingMessageObject();
            if (messageObject == null) {
                return;
            }
            if (this.currentUser == null || !this.currentUser.self) {
                int dt = messageObject.canEditMessageAnytime(this.currentChat) ? 360 : (MessagesController.getInstance(this.currentAccount).maxEditTime + 300) - Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - messageObject.messageOwner.date);
                if (dt > 0) {
                    if (dt <= 300) {
                        SimpleTextView simpleTextView = this.replyObjectTextView;
                        Object[] objArr = new Object[1];
                        objArr[0] = String.format("%d:%02d", new Object[]{Integer.valueOf(dt / 60), Integer.valueOf(dt % 60)});
                        simpleTextView.setText(LocaleController.formatString("TimeToEdit", R.string.TimeToEdit, objArr));
                    }
                    AndroidUtilities.runOnUIThread(new ChatActivity$$Lambda$59(this), 1000);
                    return;
                }
                this.chatActivityEnterView.onEditTimeExpired();
                this.replyObjectTextView.setText(LocaleController.formatString("TimeToEditExpired", R.string.TimeToEditExpired, new Object[0]));
            }
        }
    }

    private ArrayList<MessageObject> createVoiceMessagesPlaylist(MessageObject startMessageObject, boolean playingUnreadMedia) {
        ArrayList<MessageObject> messageObjects = new ArrayList();
        messageObjects.add(startMessageObject);
        int messageId = startMessageObject.getId();
        long startDialogId = startMessageObject.getDialogId();
        if (messageId != 0) {
            for (int a = this.messages.size() - 1; a >= 0; a--) {
                MessageObject messageObject = (MessageObject) this.messages.get(a);
                if ((messageObject.getDialogId() != this.mergeDialogId || startMessageObject.getDialogId() == this.mergeDialogId) && (((this.currentEncryptedChat == null && messageObject.getId() > messageId) || (this.currentEncryptedChat != null && messageObject.getId() < messageId)) && ((messageObject.isVoice() || messageObject.isRoundVideo()) && (!playingUnreadMedia || (messageObject.isContentUnread() && !messageObject.isOut()))))) {
                    messageObjects.add(messageObject);
                }
            }
        }
        return messageObjects;
    }

    private void alertUserOpenError(MessageObject message) {
        if (getParentActivity() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            if (message.type == 3) {
                builder.setMessage(LocaleController.getString("NoPlayerInstalled", R.string.NoPlayerInstalled));
            } else {
                builder.setMessage(LocaleController.formatString("NoHandleAppInstalled", R.string.NoHandleAppInstalled, message.getDocument().mime_type));
            }
            showDialog(builder.create());
        }
    }

    private void openSearchWithText(String text) {
        if (!this.actionBar.isSearchFieldVisible()) {
            this.avatarContainer.setVisibility(8);
            this.headerItem.setVisibility(8);
            this.attachItem.setVisibility(8);
            this.editTextItem.setVisibility(8);
            this.searchItem.setVisibility(0);
            updateSearchButtons(0, 0, -1);
            updateBottomOverlay();
        }
        this.openSearchKeyboard = text == null;
        this.searchItem.openSearch(this.openSearchKeyboard);
        if (text != null) {
            this.searchItem.getSearchField().setText(text);
            this.searchItem.getSearchField().setSelection(this.searchItem.getSearchField().length());
            DataQuery.getInstance(this.currentAccount).searchMessagesInChat(text, this.dialog_id, this.mergeDialogId, this.classGuid, 0, this.searchingUserMessages);
        }
        updatePinnedMessageView(true);
    }

    public void didSelectLocation(MessageMedia location, int live) {
        SendMessagesHelper.getInstance(this.currentAccount).sendMessage(location, this.dialog_id, this.replyingMessageObject, null, null);
        moveScrollToLastMessage();
        if (live == 1) {
            hideFieldPanel();
            DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, true);
        }
        if (this.paused) {
            this.scrollToTopOnResume = true;
        }
    }

    public boolean isEditingMessageMedia() {
        return (this.chatAttachAlert == null || this.chatAttachAlert.getEditingMessageObject() == null) ? false : true;
    }

    public boolean isSecretChat() {
        return this.currentEncryptedChat != null;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }

    public Chat getCurrentChat() {
        return this.currentChat;
    }

    public boolean allowGroupPhotos() {
        return !isEditingMessageMedia() && (this.currentEncryptedChat == null || AndroidUtilities.getPeerLayerVersion(this.currentEncryptedChat.layer) >= 73);
    }

    public EncryptedChat getCurrentEncryptedChat() {
        return this.currentEncryptedChat;
    }

    public ChatFull getCurrentChatInfo() {
        return this.info;
    }

    public void sendMedia(PhotoEntry photoEntry, VideoEditedInfo videoEditedInfo) {
        if (photoEntry != null) {
            fillEditingMediaWithCaption(photoEntry.caption, photoEntry.entities);
            if (photoEntry.isVideo) {
                if (videoEditedInfo != null) {
                    SendMessagesHelper.prepareSendingVideo(photoEntry.path, videoEditedInfo.estimatedSize, videoEditedInfo.estimatedDuration, videoEditedInfo.resultWidth, videoEditedInfo.resultHeight, videoEditedInfo, this.dialog_id, this.replyingMessageObject, photoEntry.caption, photoEntry.entities, photoEntry.ttl, this.editingMessageObject);
                } else {
                    SendMessagesHelper.prepareSendingVideo(photoEntry.path, 0, 0, 0, 0, null, this.dialog_id, this.replyingMessageObject, photoEntry.caption, photoEntry.entities, photoEntry.ttl, this.editingMessageObject);
                }
                hideFieldPanel();
                DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, true);
            } else if (photoEntry.imagePath != null) {
                SendMessagesHelper.prepareSendingPhoto(photoEntry.imagePath, null, this.dialog_id, this.replyingMessageObject, photoEntry.caption, photoEntry.entities, photoEntry.stickers, null, photoEntry.ttl, this.editingMessageObject);
                hideFieldPanel();
                DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, true);
            } else if (photoEntry.path != null) {
                SendMessagesHelper.prepareSendingPhoto(photoEntry.path, null, this.dialog_id, this.replyingMessageObject, photoEntry.caption, photoEntry.entities, photoEntry.stickers, null, photoEntry.ttl, this.editingMessageObject);
                hideFieldPanel();
                DataQuery.getInstance(this.currentAccount).cleanDraft(this.dialog_id, true);
            }
        }
    }

    public void showOpenGameAlert(TL_game game, MessageObject messageObject, String urlStr, boolean ask, int uid) {
        User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(uid));
        if (ask) {
            String name;
            AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            if (user != null) {
                name = ContactsController.formatName(user.first_name, user.last_name);
            } else {
                name = TtmlNode.ANONYMOUS_REGION_ID;
            }
            builder.setMessage(LocaleController.formatString("BotPermissionGameAlert", R.string.BotPermissionGameAlert, name));
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), new ChatActivity$$Lambda$60(this, game, messageObject, urlStr, uid));
            builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
            showDialog(builder.create());
        } else if (VERSION.SDK_INT < 21 || AndroidUtilities.isTablet() || !WebviewActivity.supportWebview()) {
            Activity parentActivity = getParentActivity();
            r2 = game.short_name;
            String str = (user == null || user.username == null) ? TtmlNode.ANONYMOUS_REGION_ID : user.username;
            WebviewActivity.openGameInBrowser(urlStr, messageObject, parentActivity, r2, str);
        } else if (this.parentLayout.fragmentsStack.get(this.parentLayout.fragmentsStack.size() - 1) == this) {
            r2 = (user == null || TextUtils.isEmpty(user.username)) ? TtmlNode.ANONYMOUS_REGION_ID : user.username;
            presentFragment(new WebviewActivity(urlStr, r2, game.title, game.short_name, messageObject));
        }
    }

    final /* synthetic */ void lambda$showOpenGameAlert$76$ChatActivity(TL_game game, MessageObject messageObject, String urlStr, int uid, DialogInterface dialogInterface, int i) {
        showOpenGameAlert(game, messageObject, urlStr, false, uid);
        MessagesController.getNotificationsSettings(this.currentAccount).edit().putBoolean("askgame_" + uid, false).commit();
    }

    public void showOpenUrlAlert(String url, boolean ask) {
        boolean z = true;
        if (Browser.isInternalUrl(url, null) || !ask) {
            Context parentActivity = getParentActivity();
            if (this.inlineReturn != 0) {
                z = false;
            }
            Browser.openUrl(parentActivity, url, z);
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getParentActivity());
        builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
        builder.setMessage(LocaleController.formatString("OpenUrlAlert", R.string.OpenUrlAlert, url));
        builder.setPositiveButton(LocaleController.getString("Open", R.string.Open), new ChatActivity$$Lambda$61(this, url));
        builder.setNegativeButton(LocaleController.getString("Cancel", R.string.Cancel), null);
        showDialog(builder.create());
    }

    final /* synthetic */ void lambda$showOpenUrlAlert$77$ChatActivity(String url, DialogInterface dialogInterface, int i) {
        Browser.openUrl(getParentActivity(), url, this.inlineReturn == 0);
    }

    private void removeMessageObject(MessageObject messageObject) {
        int index = this.messages.indexOf(messageObject);
        if (index != -1) {
            this.messages.remove(index);
            if (this.chatAdapter != null) {
                this.chatAdapter.notifyItemRemoved(this.chatAdapter.messagesStartRow + index);
            }
        }
    }

    public void openVCard(String vcard, String first_name, String last_name) {
        try {
            File f = new File(FileLoader.getDirectory(4), "sharing/");
            f.mkdirs();
            File f2 = new File(f, "vcard.vcf");
            BufferedWriter writer = new BufferedWriter(new FileWriter(f2));
            writer.write(vcard);
            writer.close();
            presentFragment(new PhonebookShareActivity(null, null, f2, ContactsController.formatName(first_name, last_name)));
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    private void setCellSelectionBackground(MessageObject message, ChatMessageCell messageCell, int idx) {
        GroupedMessages groupedMessages = getValidGroupedMessage(message);
        if (groupedMessages != null) {
            boolean hasUnselected = false;
            for (int a = 0; a < groupedMessages.messages.size(); a++) {
                if (this.selectedMessagesIds[idx].indexOfKey(((MessageObject) groupedMessages.messages.get(a)).getId()) < 0) {
                    hasUnselected = true;
                    break;
                }
            }
            if (!hasUnselected) {
                groupedMessages = null;
            }
        }
        if (groupedMessages == null) {
            messageCell.setBackgroundColor(Theme.getColor(Theme.key_chat_selectedBackground));
        } else {
            messageCell.setBackground(null);
        }
    }

    public ThemeDescription[] getThemeDescriptions() {
        ThemeDescriptionDelegate selectedBackgroundDelegate = new ChatActivity$$Lambda$62(this);
        ThemeDescriptionDelegate attachAlertDelegate = new ChatActivity$$Lambda$63(this);
        ThemeDescription[] themeDescriptionArr = new ThemeDescription[371];
        themeDescriptionArr[0] = new ThemeDescription(this.fragmentView, 0, null, null, null, null, Theme.key_chat_wallpaper);
        themeDescriptionArr[1] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[2] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[3] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[4] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[5] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null, null, null, Theme.key_actionBarDefaultSubmenuBackground);
        themeDescriptionArr[6] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null, null, Theme.key_actionBarDefaultSubmenuItem);
        themeDescriptionArr[7] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[8] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault);
        themeDescriptionArr[9] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon);
        themeDescriptionArr[10] = new ThemeDescription(this.avatarContainer != null ? this.avatarContainer.getTitleTextView() : null, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle);
        themeDescriptionArr[11] = new ThemeDescription(this.avatarContainer != null ? this.avatarContainer.getSubtitleTextView() : null, ThemeDescription.FLAG_TEXTCOLOR, null, new Paint[]{Theme.chat_statusPaint, Theme.chat_statusRecordPaint}, null, null, Theme.key_actionBarDefaultSubtitle, null);
        themeDescriptionArr[12] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector);
        themeDescriptionArr[13] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCH, null, null, null, null, Theme.key_actionBarDefaultSearch);
        themeDescriptionArr[14] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_SEARCHPLACEHOLDER, null, null, null, null, Theme.key_actionBarDefaultSearchPlaceholder);
        themeDescriptionArr[15] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarActionModeDefaultIcon);
        themeDescriptionArr[16] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_BACKGROUND, null, null, null, null, Theme.key_actionBarActionModeDefault);
        themeDescriptionArr[17] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_TOPBACKGROUND, null, null, null, null, Theme.key_actionBarActionModeDefaultTop);
        themeDescriptionArr[18] = new ThemeDescription(this.actionBar, ThemeDescription.FLAG_AB_AM_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarActionModeDefaultSelector);
        themeDescriptionArr[19] = new ThemeDescription(this.selectedMessagesCountTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_actionBarActionModeDefaultIcon);
        themeDescriptionArr[20] = new ThemeDescription(this.avatarContainer != null ? this.avatarContainer.getTitleTextView() : null, 0, null, null, new Drawable[]{Theme.chat_muteIconDrawable}, null, Theme.key_chat_muteIcon);
        themeDescriptionArr[21] = new ThemeDescription(this.avatarContainer != null ? this.avatarContainer.getTitleTextView() : null, 0, null, null, new Drawable[]{Theme.chat_lockIconDrawable}, null, Theme.key_chat_lockIcon);
        themeDescriptionArr[22] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.avatar_photoDrawable, Theme.avatar_broadcastDrawable, Theme.avatar_savedDrawable}, null, Theme.key_avatar_text);
        themeDescriptionArr[23] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_backgroundRed);
        themeDescriptionArr[24] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_backgroundOrange);
        themeDescriptionArr[25] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_backgroundViolet);
        themeDescriptionArr[26] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_backgroundGreen);
        themeDescriptionArr[27] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_backgroundCyan);
        themeDescriptionArr[28] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_backgroundBlue);
        themeDescriptionArr[29] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_backgroundPink);
        themeDescriptionArr[30] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_nameInMessageRed);
        themeDescriptionArr[31] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_nameInMessageOrange);
        themeDescriptionArr[32] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_nameInMessageViolet);
        themeDescriptionArr[33] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_nameInMessageGreen);
        themeDescriptionArr[34] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_nameInMessageCyan);
        themeDescriptionArr[35] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_nameInMessageBlue);
        themeDescriptionArr[36] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_avatar_nameInMessagePink);
        themeDescriptionArr[37] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class, BotHelpCell.class}, null, new Drawable[]{Theme.chat_msgInDrawable, Theme.chat_msgInMediaDrawable}, null, Theme.key_chat_inBubble);
        themeDescriptionArr[38] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInSelectedDrawable, Theme.chat_msgInMediaSelectedDrawable}, null, Theme.key_chat_inBubbleSelected);
        themeDescriptionArr[39] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class, BotHelpCell.class}, null, new Drawable[]{Theme.chat_msgInShadowDrawable, Theme.chat_msgInMediaShadowDrawable}, null, Theme.key_chat_inBubbleShadow);
        themeDescriptionArr[search] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutDrawable, Theme.chat_msgOutMediaDrawable}, null, Theme.key_chat_outBubble);
        themeDescriptionArr[41] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutSelectedDrawable, Theme.chat_msgOutMediaSelectedDrawable}, null, Theme.key_chat_outBubbleSelected);
        themeDescriptionArr[42] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutShadowDrawable, Theme.chat_msgOutMediaShadowDrawable}, null, Theme.key_chat_outBubbleShadow);
        themeDescriptionArr[43] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatActionCell.class}, Theme.chat_actionTextPaint, null, null, Theme.key_chat_serviceText);
        themeDescriptionArr[44] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{ChatActionCell.class}, Theme.chat_actionTextPaint, null, null, Theme.key_chat_serviceLink);
        themeDescriptionArr[45] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_shareIconDrawable, Theme.chat_replyIconDrawable, Theme.chat_botInlineDrawable, Theme.chat_botLinkDrawalbe, Theme.chat_goIconDrawable}, null, Theme.key_chat_serviceIcon);
        themeDescriptionArr[46] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class, ChatActionCell.class}, null, null, null, Theme.key_chat_serviceBackground);
        themeDescriptionArr[47] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class, ChatActionCell.class}, null, null, null, Theme.key_chat_serviceBackgroundSelected);
        themeDescriptionArr[48] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class, BotHelpCell.class}, null, null, null, Theme.key_chat_messageTextIn);
        themeDescriptionArr[49] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_messageTextOut);
        themeDescriptionArr[50] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{ChatMessageCell.class, BotHelpCell.class}, null, null, null, Theme.key_chat_messageLinkIn, null);
        themeDescriptionArr[51] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_LINKCOLOR, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_messageLinkOut, null);
        themeDescriptionArr[52] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckDrawable, Theme.chat_msgOutHalfCheckDrawable}, null, Theme.key_chat_outSentCheck);
        themeDescriptionArr[53] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCheckSelectedDrawable, Theme.chat_msgOutHalfCheckSelectedDrawable}, null, Theme.key_chat_outSentCheckSelected);
        themeDescriptionArr[54] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutClockDrawable}, null, Theme.key_chat_outSentClock);
        themeDescriptionArr[55] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutSelectedClockDrawable}, null, Theme.key_chat_outSentClockSelected);
        themeDescriptionArr[56] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInClockDrawable}, null, Theme.key_chat_inSentClock);
        themeDescriptionArr[57] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInSelectedClockDrawable}, null, Theme.key_chat_inSentClockSelected);
        themeDescriptionArr[58] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgMediaCheckDrawable, Theme.chat_msgMediaHalfCheckDrawable}, null, Theme.key_chat_mediaSentCheck);
        themeDescriptionArr[59] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgStickerHalfCheckDrawable, Theme.chat_msgStickerCheckDrawable, Theme.chat_msgStickerClockDrawable, Theme.chat_msgStickerViewsDrawable}, null, Theme.key_chat_serviceText);
        themeDescriptionArr[60] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgMediaClockDrawable}, null, Theme.key_chat_mediaSentClock);
        themeDescriptionArr[61] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutViewsDrawable}, null, Theme.key_chat_outViews);
        themeDescriptionArr[62] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutViewsSelectedDrawable}, null, Theme.key_chat_outViewsSelected);
        themeDescriptionArr[63] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInViewsDrawable}, null, Theme.key_chat_inViews);
        themeDescriptionArr[64] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInViewsSelectedDrawable}, null, Theme.key_chat_inViewsSelected);
        themeDescriptionArr[65] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgMediaViewsDrawable}, null, Theme.key_chat_mediaViews);
        themeDescriptionArr[66] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutMenuDrawable}, null, Theme.key_chat_outMenu);
        themeDescriptionArr[67] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutMenuSelectedDrawable}, null, Theme.key_chat_outMenuSelected);
        themeDescriptionArr[68] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInMenuDrawable}, null, Theme.key_chat_inMenu);
        themeDescriptionArr[69] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInMenuSelectedDrawable}, null, Theme.key_chat_inMenuSelected);
        themeDescriptionArr[70] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgMediaMenuDrawable}, null, Theme.key_chat_mediaMenu);
        themeDescriptionArr[71] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutInstantDrawable, Theme.chat_msgOutCallDrawable}, null, Theme.key_chat_outInstant);
        themeDescriptionArr[72] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgOutCallSelectedDrawable}, null, Theme.key_chat_outInstantSelected);
        themeDescriptionArr[73] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInInstantDrawable, Theme.chat_msgInCallDrawable}, null, Theme.key_chat_inInstant);
        themeDescriptionArr[74] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgInCallSelectedDrawable}, null, Theme.key_chat_inInstantSelected);
        themeDescriptionArr[75] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgCallUpRedDrawable, Theme.chat_msgCallDownRedDrawable}, null, Theme.key_calls_callReceivedRedIcon);
        themeDescriptionArr[76] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgCallUpGreenDrawable, Theme.chat_msgCallDownGreenDrawable}, null, Theme.key_calls_callReceivedGreenIcon);
        themeDescriptionArr[77] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, Theme.chat_msgErrorPaint, null, null, Theme.key_chat_sentError);
        themeDescriptionArr[78] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_msgErrorDrawable}, null, Theme.key_chat_sentErrorIcon);
        themeDescriptionArr[79] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, selectedBackgroundDelegate, Theme.key_chat_selectedBackground);
        themeDescriptionArr[80] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, Theme.chat_durationPaint, null, null, Theme.key_chat_previewDurationText);
        themeDescriptionArr[81] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, Theme.chat_gamePaint, null, null, Theme.key_chat_previewGameText);
        themeDescriptionArr[82] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inPreviewInstantText);
        themeDescriptionArr[83] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outPreviewInstantText);
        themeDescriptionArr[84] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inPreviewInstantSelectedText);
        themeDescriptionArr[85] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outPreviewInstantSelectedText);
        themeDescriptionArr[86] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, Theme.chat_deleteProgressPaint, null, null, Theme.key_chat_secretTimeText);
        themeDescriptionArr[87] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_stickerNameText);
        themeDescriptionArr[88] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, Theme.chat_botButtonPaint, null, null, Theme.key_chat_botButtonText);
        themeDescriptionArr[89] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, Theme.chat_botProgressPaint, null, null, Theme.key_chat_botProgress);
        themeDescriptionArr[90] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, Theme.chat_timeBackgroundPaint, null, null, Theme.key_chat_mediaTimeBackground);
        themeDescriptionArr[91] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inForwardedNameText);
        themeDescriptionArr[92] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outForwardedNameText);
        themeDescriptionArr[93] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inViaBotNameText);
        themeDescriptionArr[94] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outViaBotNameText);
        themeDescriptionArr[95] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_stickerViaBotNameText);
        themeDescriptionArr[96] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inReplyLine);
        themeDescriptionArr[97] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outReplyLine);
        themeDescriptionArr[98] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_stickerReplyLine);
        themeDescriptionArr[99] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inReplyNameText);
        themeDescriptionArr[100] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outReplyNameText);
        themeDescriptionArr[101] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_stickerReplyNameText);
        themeDescriptionArr[102] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inReplyMessageText);
        themeDescriptionArr[103] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outReplyMessageText);
        themeDescriptionArr[104] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inReplyMediaMessageText);
        themeDescriptionArr[105] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outReplyMediaMessageText);
        themeDescriptionArr[106] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inReplyMediaMessageSelectedText);
        themeDescriptionArr[107] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outReplyMediaMessageSelectedText);
        themeDescriptionArr[108] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_stickerReplyMessageText);
        themeDescriptionArr[109] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inPreviewLine);
        themeDescriptionArr[110] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outPreviewLine);
        themeDescriptionArr[111] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inSiteNameText);
        themeDescriptionArr[112] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outSiteNameText);
        themeDescriptionArr[113] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inContactNameText);
        themeDescriptionArr[114] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outContactNameText);
        themeDescriptionArr[115] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inContactPhoneText);
        themeDescriptionArr[116] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inContactPhoneSelectedText);
        themeDescriptionArr[117] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_outContactPhoneText");
        themeDescriptionArr[118] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, "chat_outContactPhoneText");
        themeDescriptionArr[119] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_mediaProgress);
        themeDescriptionArr[120] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioProgress);
        themeDescriptionArr[121] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioProgress);
        themeDescriptionArr[122] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioSelectedProgress);
        themeDescriptionArr[123] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioSelectedProgress);
        themeDescriptionArr[124] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_mediaTimeText);
        themeDescriptionArr[125] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inTimeText);
        themeDescriptionArr[126] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outTimeText);
        themeDescriptionArr[127] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inTimeSelectedText);
        themeDescriptionArr[128] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_adminText);
        themeDescriptionArr[TsExtractor.TS_STREAM_TYPE_AC3] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_adminSelectedText);
        themeDescriptionArr[TsExtractor.TS_STREAM_TYPE_HDMV_DTS] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outTimeSelectedText);
        themeDescriptionArr[131] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioPerfomerText);
        themeDescriptionArr[132] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioPerfomerSelectedText);
        themeDescriptionArr[133] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioPerfomerText);
        themeDescriptionArr[TsExtractor.TS_STREAM_TYPE_SPLICE_INFO] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioPerfomerSelectedText);
        themeDescriptionArr[TsExtractor.TS_STREAM_TYPE_E_AC3] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioTitleText);
        themeDescriptionArr[136] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioTitleText);
        themeDescriptionArr[137] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioDurationText);
        themeDescriptionArr[TsExtractor.TS_STREAM_TYPE_DTS] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioDurationText);
        themeDescriptionArr[139] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioDurationSelectedText);
        themeDescriptionArr[140] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioDurationSelectedText);
        themeDescriptionArr[141] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioSeekbar);
        themeDescriptionArr[142] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioSeekbar);
        themeDescriptionArr[143] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioSeekbarSelected);
        themeDescriptionArr[144] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioSeekbarSelected);
        themeDescriptionArr[145] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioSeekbarFill);
        themeDescriptionArr[146] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inAudioCacheSeekbar);
        themeDescriptionArr[147] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioSeekbarFill);
        themeDescriptionArr[148] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outAudioCacheSeekbar);
        themeDescriptionArr[149] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inVoiceSeekbar);
        themeDescriptionArr[150] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outVoiceSeekbar);
        themeDescriptionArr[151] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inVoiceSeekbarSelected);
        themeDescriptionArr[152] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outVoiceSeekbarSelected);
        themeDescriptionArr[153] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inVoiceSeekbarFill);
        themeDescriptionArr[154] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outVoiceSeekbarFill);
        themeDescriptionArr[155] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inFileProgress);
        themeDescriptionArr[156] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outFileProgress);
        themeDescriptionArr[157] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inFileProgressSelected);
        themeDescriptionArr[158] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outFileProgressSelected);
        themeDescriptionArr[159] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inFileNameText);
        themeDescriptionArr[160] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outFileNameText);
        themeDescriptionArr[161] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inFileInfoText);
        themeDescriptionArr[162] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outFileInfoText);
        themeDescriptionArr[163] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inFileInfoSelectedText);
        themeDescriptionArr[164] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outFileInfoSelectedText);
        themeDescriptionArr[165] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inFileBackground);
        themeDescriptionArr[166] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outFileBackground);
        themeDescriptionArr[167] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inFileBackgroundSelected);
        themeDescriptionArr[168] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outFileBackgroundSelected);
        themeDescriptionArr[169] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inVenueInfoText);
        themeDescriptionArr[170] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outVenueInfoText);
        themeDescriptionArr[171] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_inVenueInfoSelectedText);
        themeDescriptionArr[172] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_outVenueInfoSelectedText);
        themeDescriptionArr[173] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, null, null, Theme.key_chat_mediaInfoText);
        themeDescriptionArr[174] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, Theme.chat_urlPaint, null, null, Theme.key_chat_linkSelectBackground);
        themeDescriptionArr[175] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, Theme.chat_textSearchSelectionPaint, null, null, Theme.key_chat_textSelectBackground);
        themeDescriptionArr[176] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_fileStatesDrawable[0][0], Theme.chat_fileStatesDrawable[1][0], Theme.chat_fileStatesDrawable[2][0], Theme.chat_fileStatesDrawable[3][0], Theme.chat_fileStatesDrawable[4][0]}, null, Theme.key_chat_outLoader);
        themeDescriptionArr[177] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_fileStatesDrawable[0][0], Theme.chat_fileStatesDrawable[1][0], Theme.chat_fileStatesDrawable[2][0], Theme.chat_fileStatesDrawable[3][0], Theme.chat_fileStatesDrawable[4][0]}, null, Theme.key_chat_outMediaIcon);
        themeDescriptionArr[178] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_fileStatesDrawable[0][1], Theme.chat_fileStatesDrawable[1][1], Theme.chat_fileStatesDrawable[2][1], Theme.chat_fileStatesDrawable[3][1], Theme.chat_fileStatesDrawable[4][1]}, null, Theme.key_chat_outLoaderSelected);
        themeDescriptionArr[179] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_fileStatesDrawable[0][1], Theme.chat_fileStatesDrawable[1][1], Theme.chat_fileStatesDrawable[2][1], Theme.chat_fileStatesDrawable[3][1], Theme.chat_fileStatesDrawable[4][1]}, null, Theme.key_chat_outMediaIconSelected);
        themeDescriptionArr[180] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_fileStatesDrawable[5][0], Theme.chat_fileStatesDrawable[6][0], Theme.chat_fileStatesDrawable[7][0], Theme.chat_fileStatesDrawable[8][0], Theme.chat_fileStatesDrawable[9][0]}, null, Theme.key_chat_inLoader);
        themeDescriptionArr[181] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_fileStatesDrawable[5][0], Theme.chat_fileStatesDrawable[6][0], Theme.chat_fileStatesDrawable[7][0], Theme.chat_fileStatesDrawable[8][0], Theme.chat_fileStatesDrawable[9][0]}, null, Theme.key_chat_inMediaIcon);
        themeDescriptionArr[182] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_fileStatesDrawable[5][1], Theme.chat_fileStatesDrawable[6][1], Theme.chat_fileStatesDrawable[7][1], Theme.chat_fileStatesDrawable[8][1], Theme.chat_fileStatesDrawable[9][1]}, null, Theme.key_chat_inLoaderSelected);
        themeDescriptionArr[183] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_fileStatesDrawable[5][1], Theme.chat_fileStatesDrawable[6][1], Theme.chat_fileStatesDrawable[7][1], Theme.chat_fileStatesDrawable[8][1], Theme.chat_fileStatesDrawable[9][1]}, null, Theme.key_chat_inMediaIconSelected);
        themeDescriptionArr[184] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[0][0], Theme.chat_photoStatesDrawables[1][0], Theme.chat_photoStatesDrawables[2][0], Theme.chat_photoStatesDrawables[3][0]}, null, Theme.key_chat_mediaLoaderPhoto);
        themeDescriptionArr[185] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[0][0], Theme.chat_photoStatesDrawables[1][0], Theme.chat_photoStatesDrawables[2][0], Theme.chat_photoStatesDrawables[3][0]}, null, Theme.key_chat_mediaLoaderPhotoIcon);
        themeDescriptionArr[186] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[0][1], Theme.chat_photoStatesDrawables[1][1], Theme.chat_photoStatesDrawables[2][1], Theme.chat_photoStatesDrawables[3][1]}, null, Theme.key_chat_mediaLoaderPhotoSelected);
        themeDescriptionArr[187] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[0][1], Theme.chat_photoStatesDrawables[1][1], Theme.chat_photoStatesDrawables[2][1], Theme.chat_photoStatesDrawables[3][1]}, null, Theme.key_chat_mediaLoaderPhotoIconSelected);
        themeDescriptionArr[TsExtractor.TS_PACKET_SIZE] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[7][0], Theme.chat_photoStatesDrawables[8][0]}, null, Theme.key_chat_outLoaderPhoto);
        themeDescriptionArr[PsExtractor.PRIVATE_STREAM_1] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[7][0], Theme.chat_photoStatesDrawables[8][0]}, null, Theme.key_chat_outLoaderPhotoIcon);
        themeDescriptionArr[190] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[7][1], Theme.chat_photoStatesDrawables[8][1]}, null, Theme.key_chat_outLoaderPhotoSelected);
        themeDescriptionArr[191] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[7][1], Theme.chat_photoStatesDrawables[8][1]}, null, Theme.key_chat_outLoaderPhotoIconSelected);
        themeDescriptionArr[PsExtractor.AUDIO_STREAM] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[10][0], Theme.chat_photoStatesDrawables[11][0]}, null, Theme.key_chat_inLoaderPhoto);
        themeDescriptionArr[193] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[10][0], Theme.chat_photoStatesDrawables[11][0]}, null, Theme.key_chat_inLoaderPhotoIcon);
        themeDescriptionArr[194] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[10][1], Theme.chat_photoStatesDrawables[11][1]}, null, Theme.key_chat_inLoaderPhotoSelected);
        themeDescriptionArr[195] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[10][1], Theme.chat_photoStatesDrawables[11][1]}, null, Theme.key_chat_inLoaderPhotoIconSelected);
        themeDescriptionArr[196] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[9][0]}, null, Theme.key_chat_outFileIcon);
        themeDescriptionArr[197] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[9][1]}, null, Theme.key_chat_outFileSelectedIcon);
        themeDescriptionArr[198] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[12][0]}, null, Theme.key_chat_inFileIcon);
        themeDescriptionArr[199] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_photoStatesDrawables[12][1]}, null, Theme.key_chat_inFileSelectedIcon);
        themeDescriptionArr[Callback.DEFAULT_DRAG_ANIMATION_DURATION] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_contactDrawable[0]}, null, Theme.key_chat_inContactBackground);
        themeDescriptionArr[201] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_contactDrawable[0]}, null, Theme.key_chat_inContactIcon);
        themeDescriptionArr[202] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_contactDrawable[1]}, null, Theme.key_chat_outContactBackground);
        themeDescriptionArr[203] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_contactDrawable[1]}, null, Theme.key_chat_outContactIcon);
        themeDescriptionArr[204] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_locationDrawable[0]}, null, Theme.key_chat_inLocationBackground);
        themeDescriptionArr[205] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_locationDrawable[0]}, null, Theme.key_chat_inLocationIcon);
        themeDescriptionArr[206] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_locationDrawable[1]}, null, Theme.key_chat_outLocationBackground);
        themeDescriptionArr[207] = new ThemeDescription(this.chatListView, 0, new Class[]{ChatMessageCell.class}, null, new Drawable[]{Theme.chat_locationDrawable[1]}, null, Theme.key_chat_outLocationIcon);
        themeDescriptionArr[208] = new ThemeDescription(this.mentionContainer, 0, null, Theme.chat_composeBackgroundPaint, null, null, Theme.key_chat_messagePanelBackground);
        themeDescriptionArr[209] = new ThemeDescription(this.mentionContainer, 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, Theme.key_chat_messagePanelShadow);
        themeDescriptionArr[210] = new ThemeDescription(this.searchContainer, 0, null, Theme.chat_composeBackgroundPaint, null, null, Theme.key_chat_messagePanelBackground);
        themeDescriptionArr[211] = new ThemeDescription(this.searchContainer, 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, Theme.key_chat_messagePanelShadow);
        themeDescriptionArr[212] = new ThemeDescription(this.bottomOverlay, 0, null, Theme.chat_composeBackgroundPaint, null, null, Theme.key_chat_messagePanelBackground);
        themeDescriptionArr[213] = new ThemeDescription(this.bottomOverlay, 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, Theme.key_chat_messagePanelShadow);
        themeDescriptionArr[214] = new ThemeDescription(this.bottomOverlayChat, 0, null, Theme.chat_composeBackgroundPaint, null, null, Theme.key_chat_messagePanelBackground);
        themeDescriptionArr[215] = new ThemeDescription(this.bottomOverlayChat, 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, Theme.key_chat_messagePanelShadow);
        themeDescriptionArr[216] = new ThemeDescription(this.chatActivityEnterView, 0, null, Theme.chat_composeBackgroundPaint, null, null, Theme.key_chat_messagePanelBackground);
        themeDescriptionArr[217] = new ThemeDescription(this.chatActivityEnterView, 0, null, null, new Drawable[]{Theme.chat_composeShadowDrawable}, null, Theme.key_chat_messagePanelShadow);
        themeDescriptionArr[218] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_BACKGROUND, new Class[]{ChatActivityEnterView.class}, new String[]{"audioVideoButtonContainer"}, null, null, null, Theme.key_chat_messagePanelBackground);
        themeDescriptionArr[219] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"messageEditText"}, null, null, null, Theme.key_chat_messagePanelText);
        themeDescriptionArr[220] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"recordSendText"}, null, null, null, Theme.key_chat_fieldOverlayText);
        themeDescriptionArr[221] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_HINTTEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"messageEditText"}, null, null, null, Theme.key_chat_messagePanelHint);
        themeDescriptionArr[222] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"sendButton"}, null, null, null, Theme.key_chat_messagePanelSend);
        themeDescriptionArr[223] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"emojiButton"}, null, null, null, Theme.key_chat_messagePanelIcons);
        themeDescriptionArr[224] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"botButton"}, null, null, null, Theme.key_chat_messagePanelIcons);
        themeDescriptionArr[225] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"notifyButton"}, null, null, null, Theme.key_chat_messagePanelIcons);
        themeDescriptionArr[226] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"attachButton"}, null, null, null, Theme.key_chat_messagePanelIcons);
        themeDescriptionArr[227] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"audioSendButton"}, null, null, null, Theme.key_chat_messagePanelIcons);
        themeDescriptionArr[228] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"videoSendButton"}, null, null, null, Theme.key_chat_messagePanelIcons);
        themeDescriptionArr[229] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"doneButtonImage"}, null, null, null, Theme.key_chat_editDoneIcon);
        themeDescriptionArr[230] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_BACKGROUND, new Class[]{ChatActivityEnterView.class}, new String[]{"recordedAudioPanel"}, null, null, null, Theme.key_chat_messagePanelBackground);
        themeDescriptionArr[231] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"micDrawable"}, null, null, null, Theme.key_chat_messagePanelVoicePressed);
        themeDescriptionArr[232] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"cameraDrawable"}, null, null, null, Theme.key_chat_messagePanelVoicePressed);
        themeDescriptionArr[233] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"sendDrawable"}, null, null, null, Theme.key_chat_messagePanelVoicePressed);
        themeDescriptionArr[234] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"lockDrawable"}, null, null, null, Theme.key_chat_messagePanelVoiceLock);
        themeDescriptionArr[235] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"lockTopDrawable"}, null, null, null, Theme.key_chat_messagePanelVoiceLock);
        themeDescriptionArr[236] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"lockArrowDrawable"}, null, null, null, Theme.key_chat_messagePanelVoiceLock);
        themeDescriptionArr[237] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"lockBackgroundDrawable"}, null, null, null, Theme.key_chat_messagePanelVoiceLockBackground);
        themeDescriptionArr[238] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"lockShadowDrawable"}, null, null, null, Theme.key_chat_messagePanelVoiceLockShadow);
        themeDescriptionArr[239] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"recordDeleteImageView"}, null, null, null, Theme.key_chat_messagePanelVoiceDelete);
        themeDescriptionArr[PsExtractor.VIDEO_STREAM_MASK] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{ChatActivityEnterView.class}, new String[]{"recordedAudioBackground"}, null, null, null, Theme.key_chat_recordedVoiceBackground);
        themeDescriptionArr[241] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"recordTimeText"}, null, null, null, Theme.key_chat_recordTime);
        themeDescriptionArr[242] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_BACKGROUND, new Class[]{ChatActivityEnterView.class}, new String[]{"recordTimeContainer"}, null, null, null, Theme.key_chat_messagePanelBackground);
        themeDescriptionArr[243] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"recordCancelText"}, null, null, null, Theme.key_chat_recordVoiceCancel);
        themeDescriptionArr[244] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_BACKGROUND, new Class[]{ChatActivityEnterView.class}, new String[]{"recordPanel"}, null, null, null, Theme.key_chat_messagePanelBackground);
        themeDescriptionArr[245] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"recordedAudioTimeTextView"}, null, null, null, Theme.key_chat_messagePanelVoiceDuration);
        themeDescriptionArr[246] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"recordCancelImage"}, null, null, null, Theme.key_chat_recordVoiceCancel);
        themeDescriptionArr[247] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"doneButtonProgress"}, null, null, null, Theme.key_contextProgressInner1);
        themeDescriptionArr[248] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"doneButtonProgress"}, null, null, null, Theme.key_contextProgressOuter1);
        themeDescriptionArr[249] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ChatActivityEnterView.class}, new String[]{"cancelBotButton"}, null, null, null, Theme.key_chat_messagePanelCancelInlineBot);
        themeDescriptionArr[Callback.DEFAULT_SWIPE_ANIMATION_DURATION] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"redDotPaint"}, null, null, null, Theme.key_chat_recordedVoiceDot);
        themeDescriptionArr[251] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"paint"}, null, null, null, Theme.key_chat_messagePanelVoiceBackground);
        themeDescriptionArr[252] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"paintRecord"}, null, null, null, Theme.key_chat_messagePanelVoiceShadow);
        themeDescriptionArr[253] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"seekBarWaveform"}, null, null, null, Theme.key_chat_recordedVoiceProgress);
        themeDescriptionArr[254] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"seekBarWaveform"}, null, null, null, Theme.key_chat_recordedVoiceProgressInner);
        themeDescriptionArr[255] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"playDrawable"}, null, null, null, Theme.key_chat_recordedVoicePlayPause);
        themeDescriptionArr[256] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"pauseDrawable"}, null, null, null, Theme.key_chat_recordedVoicePlayPause);
        themeDescriptionArr[257] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{ChatActivityEnterView.class}, new String[]{"playDrawable"}, null, null, null, Theme.key_chat_recordedVoicePlayPausePressed);
        themeDescriptionArr[258] = new ThemeDescription(this.chatActivityEnterView, ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, new Class[]{ChatActivityEnterView.class}, new String[]{"pauseDrawable"}, null, null, null, Theme.key_chat_recordedVoicePlayPausePressed);
        themeDescriptionArr[259] = new ThemeDescription(this.chatActivityEnterView, 0, new Class[]{ChatActivityEnterView.class}, new String[]{"dotPaint"}, null, null, null, Theme.key_chat_emojiPanelNewTrending);
        themeDescriptionArr[260] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelBackground);
        themeDescriptionArr[261] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelShadowLine);
        themeDescriptionArr[262] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelEmptyText);
        themeDescriptionArr[263] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelIcon);
        themeDescriptionArr[264] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelIconSelected);
        themeDescriptionArr[265] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelStickerPackSelector);
        themeDescriptionArr[266] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelIconSelector);
        themeDescriptionArr[267] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelBackspace);
        themeDescriptionArr[268] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelTrendingTitle);
        themeDescriptionArr[269] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelTrendingDescription);
        themeDescriptionArr[270] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelBadgeText);
        themeDescriptionArr[271] = new ThemeDescription(this.chatActivityEnterView != null ? this.chatActivityEnterView.getEmojiView() : this.chatActivityEnterView, 0, new Class[]{EmojiView.class}, null, null, null, selectedBackgroundDelegate, Theme.key_chat_emojiPanelBadgeBackground);
        themeDescriptionArr[272] = new ThemeDescription(null, 0, null, null, null, null, Theme.key_chat_botKeyboardButtonText);
        themeDescriptionArr[273] = new ThemeDescription(null, 0, null, null, null, null, Theme.key_chat_botKeyboardButtonBackground);
        themeDescriptionArr[274] = new ThemeDescription(null, 0, null, null, null, null, Theme.key_chat_botKeyboardButtonBackgroundPressed);
        themeDescriptionArr[275] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, null, null, null, Theme.key_inappPlayerBackground);
        themeDescriptionArr[276] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"playButton"}, null, null, null, Theme.key_inappPlayerPlayPause);
        themeDescriptionArr[277] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, null, null, null, Theme.key_inappPlayerTitle);
        themeDescriptionArr[278] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_FASTSCROLL, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, null, null, null, Theme.key_inappPlayerPerformer);
        themeDescriptionArr[279] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{FragmentContextView.class}, new String[]{"closeButton"}, null, null, null, Theme.key_inappPlayerClose);
        themeDescriptionArr[280] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_BACKGROUND | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"frameLayout"}, null, null, null, Theme.key_returnToCallBackground);
        themeDescriptionArr[281] = new ThemeDescription(this.fragmentView, ThemeDescription.FLAG_TEXTCOLOR | ThemeDescription.FLAG_CHECKTAG, new Class[]{FragmentContextView.class}, new String[]{"titleTextView"}, null, null, null, Theme.key_returnToCallText);
        themeDescriptionArr[282] = new ThemeDescription(this.pinnedLineView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_chat_topPanelLine);
        themeDescriptionArr[283] = new ThemeDescription(this.pinnedMessageNameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_topPanelTitle);
        themeDescriptionArr[284] = new ThemeDescription(this.pinnedMessageTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_topPanelMessage);
        themeDescriptionArr[285] = new ThemeDescription(this.alertNameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_topPanelTitle);
        themeDescriptionArr[286] = new ThemeDescription(this.alertTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_topPanelMessage);
        themeDescriptionArr[287] = new ThemeDescription(this.closePinned, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_topPanelClose);
        themeDescriptionArr[288] = new ThemeDescription(this.closeReportSpam, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_topPanelClose);
        themeDescriptionArr[289] = new ThemeDescription(this.reportSpamView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chat_topPanelBackground);
        themeDescriptionArr[290] = new ThemeDescription(this.alertView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chat_topPanelBackground);
        themeDescriptionArr[291] = new ThemeDescription(this.pinnedMessageView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chat_topPanelBackground);
        themeDescriptionArr[292] = new ThemeDescription(this.addToContactsButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_addContact);
        themeDescriptionArr[293] = new ThemeDescription(this.reportSpamButton, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_reportSpam);
        themeDescriptionArr[294] = new ThemeDescription(this.replyLineView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_chat_replyPanelLine);
        themeDescriptionArr[295] = new ThemeDescription(this.replyNameTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_replyPanelName);
        themeDescriptionArr[296] = new ThemeDescription(this.replyObjectTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_replyPanelMessage);
        themeDescriptionArr[297] = new ThemeDescription(this.replyIconImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_replyPanelIcons);
        themeDescriptionArr[298] = new ThemeDescription(this.replyCloseImageView, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_replyPanelClose);
        themeDescriptionArr[299] = new ThemeDescription(this.searchUpButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_searchPanelIcons);
        themeDescriptionArr[300] = new ThemeDescription(this.searchDownButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_searchPanelIcons);
        themeDescriptionArr[301] = new ThemeDescription(this.searchCalendarButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_searchPanelIcons);
        themeDescriptionArr[302] = new ThemeDescription(this.searchUserButton, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_searchPanelIcons);
        themeDescriptionArr[303] = new ThemeDescription(this.searchCountText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_searchPanelText);
        themeDescriptionArr[304] = new ThemeDescription(this.bottomOverlayText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_secretChatStatusText);
        themeDescriptionArr[305] = new ThemeDescription(this.bottomOverlayChatText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_fieldOverlayText);
        themeDescriptionArr[306] = new ThemeDescription(this.bigEmptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_serviceText);
        themeDescriptionArr[307] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_serviceText);
        themeDescriptionArr[308] = new ThemeDescription(this.progressBar, ThemeDescription.FLAG_PROGRESSBAR, null, null, null, null, Theme.key_chat_serviceText);
        themeDescriptionArr[309] = new ThemeDescription(this.stickersPanelArrow, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_stickersHintPanel);
        themeDescriptionArr[310] = new ThemeDescription(this.stickersListView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{StickerCell.class}, null, null, null, Theme.key_chat_stickersHintPanel);
        themeDescriptionArr[311] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_USEBACKGROUNDDRAWABLE, new Class[]{ChatUnreadCell.class}, new String[]{"backgroundLayout"}, null, null, null, Theme.key_chat_unreadMessagesStartBackground);
        themeDescriptionArr[312] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_IMAGECOLOR, new Class[]{ChatUnreadCell.class}, new String[]{"imageView"}, null, null, null, Theme.key_chat_unreadMessagesStartArrowIcon);
        themeDescriptionArr[313] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{ChatUnreadCell.class}, new String[]{"textView"}, null, null, null, Theme.key_chat_unreadMessagesStartText);
        themeDescriptionArr[314] = new ThemeDescription(this.progressView2, ThemeDescription.FLAG_SERVICEBACKGROUND, null, null, null, null, Theme.key_chat_serviceBackground);
        themeDescriptionArr[315] = new ThemeDescription(this.emptyView, ThemeDescription.FLAG_SERVICEBACKGROUND, null, null, null, null, Theme.key_chat_serviceBackground);
        themeDescriptionArr[316] = new ThemeDescription(this.bigEmptyView, ThemeDescription.FLAG_SERVICEBACKGROUND, null, null, null, null, Theme.key_chat_serviceBackground);
        themeDescriptionArr[317] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_SERVICEBACKGROUND, new Class[]{ChatLoadingCell.class}, new String[]{"textView"}, null, null, null, Theme.key_chat_serviceBackground);
        themeDescriptionArr[318] = new ThemeDescription(this.chatListView, ThemeDescription.FLAG_PROGRESSBAR, new Class[]{ChatLoadingCell.class}, new String[]{"textView"}, null, null, null, Theme.key_chat_serviceText);
        themeDescriptionArr[319] = new ThemeDescription(this.mentionListView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{BotSwitchCell.class}, new String[]{"textView"}, null, null, null, Theme.key_chat_botSwitchToInlineText);
        themeDescriptionArr[320] = new ThemeDescription(this.mentionListView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{MentionCell.class}, new String[]{"nameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[321] = new ThemeDescription(this.mentionListView, ThemeDescription.FLAG_TEXTCOLOR, new Class[]{MentionCell.class}, new String[]{"usernameTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText3);
        themeDescriptionArr[322] = new ThemeDescription(this.mentionListView, 0, new Class[]{ContextLinkCell.class}, null, new Drawable[]{Theme.chat_inlineResultFile, Theme.chat_inlineResultAudio, Theme.chat_inlineResultLocation}, null, Theme.key_chat_inlineResultIcon);
        themeDescriptionArr[323] = new ThemeDescription(this.mentionListView, 0, new Class[]{ContextLinkCell.class}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2);
        themeDescriptionArr[324] = new ThemeDescription(this.mentionListView, 0, new Class[]{ContextLinkCell.class}, null, null, null, Theme.key_windowBackgroundWhiteLinkText);
        themeDescriptionArr[325] = new ThemeDescription(this.mentionListView, 0, new Class[]{ContextLinkCell.class}, null, null, null, Theme.key_windowBackgroundWhiteBlackText);
        themeDescriptionArr[326] = new ThemeDescription(this.mentionListView, 0, new Class[]{ContextLinkCell.class}, null, null, null, Theme.key_chat_inAudioProgress);
        themeDescriptionArr[327] = new ThemeDescription(this.mentionListView, 0, new Class[]{ContextLinkCell.class}, null, null, null, Theme.key_chat_inAudioSelectedProgress);
        themeDescriptionArr[328] = new ThemeDescription(this.mentionListView, 0, new Class[]{ContextLinkCell.class}, null, null, null, Theme.key_divider);
        themeDescriptionArr[329] = new ThemeDescription(this.gifHintTextView, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chat_gifSaveHintBackground);
        themeDescriptionArr[330] = new ThemeDescription(this.gifHintTextView, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_gifSaveHintText);
        themeDescriptionArr[331] = new ThemeDescription(this.pagedownButtonCounter, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chat_goDownButtonCounterBackground);
        themeDescriptionArr[332] = new ThemeDescription(this.pagedownButtonCounter, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_goDownButtonCounter);
        themeDescriptionArr[333] = new ThemeDescription(this.pagedownButtonImage, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chat_goDownButton);
        themeDescriptionArr[334] = new ThemeDescription(this.pagedownButtonImage, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_chat_goDownButtonShadow);
        themeDescriptionArr[335] = new ThemeDescription(this.pagedownButtonImage, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_goDownButtonIcon);
        themeDescriptionArr[336] = new ThemeDescription(this.mentiondownButtonCounter, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chat_goDownButtonCounterBackground);
        themeDescriptionArr[337] = new ThemeDescription(this.mentiondownButtonCounter, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_chat_goDownButtonCounter);
        themeDescriptionArr[338] = new ThemeDescription(this.mentiondownButtonImage, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_chat_goDownButton);
        themeDescriptionArr[339] = new ThemeDescription(this.mentiondownButtonImage, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_chat_goDownButtonShadow);
        themeDescriptionArr[340] = new ThemeDescription(this.mentiondownButtonImage, ThemeDescription.FLAG_IMAGECOLOR, null, null, null, null, Theme.key_chat_goDownButtonIcon);
        themeDescriptionArr[341] = new ThemeDescription(this.avatarContainer != null ? this.avatarContainer.getTimeItem() : null, 0, null, null, null, null, Theme.key_chat_secretTimerBackground);
        themeDescriptionArr[342] = new ThemeDescription(this.avatarContainer != null ? this.avatarContainer.getTimeItem() : null, 0, null, null, null, null, Theme.key_chat_secretTimerText);
        themeDescriptionArr[343] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[0]}, null, Theme.key_chat_attachCameraIcon1);
        themeDescriptionArr[344] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[0]}, null, Theme.key_chat_attachCameraIcon2);
        themeDescriptionArr[345] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[0]}, null, Theme.key_chat_attachCameraIcon3);
        themeDescriptionArr[346] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[0]}, null, Theme.key_chat_attachCameraIcon4);
        themeDescriptionArr[347] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[0]}, null, Theme.key_chat_attachCameraIcon5);
        themeDescriptionArr[348] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[0]}, null, Theme.key_chat_attachCameraIcon6);
        themeDescriptionArr[349] = new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[1]}, null, Theme.key_chat_attachGalleryBackground);
        themeDescriptionArr[350] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[1]}, null, Theme.key_chat_attachGalleryIcon);
        themeDescriptionArr[351] = new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[2]}, null, Theme.key_chat_attachVideoBackground);
        themeDescriptionArr[352] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[2]}, null, Theme.key_chat_attachVideoIcon);
        themeDescriptionArr[353] = new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[3]}, null, Theme.key_chat_attachAudioBackground);
        themeDescriptionArr[354] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[3]}, null, Theme.key_chat_attachAudioIcon);
        themeDescriptionArr[355] = new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[4]}, null, Theme.key_chat_attachFileBackground);
        themeDescriptionArr[356] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[4]}, null, Theme.key_chat_attachFileIcon);
        themeDescriptionArr[357] = new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[5]}, null, Theme.key_chat_attachContactBackground);
        themeDescriptionArr[358] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[5]}, null, Theme.key_chat_attachContactIcon);
        themeDescriptionArr[359] = new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[6]}, null, Theme.key_chat_attachLocationBackground);
        themeDescriptionArr[360] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[6]}, null, Theme.key_chat_attachLocationIcon);
        themeDescriptionArr[361] = new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[7]}, null, Theme.key_chat_attachHideBackground);
        themeDescriptionArr[362] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[7]}, null, Theme.key_chat_attachHideIcon);
        themeDescriptionArr[363] = new ThemeDescription(null, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[8]}, null, Theme.key_chat_attachSendBackground);
        themeDescriptionArr[364] = new ThemeDescription(null, 0, null, null, new Drawable[]{Theme.chat_attachButtonDrawables[8]}, null, Theme.key_chat_attachSendIcon);
        themeDescriptionArr[365] = new ThemeDescription(null, 0, null, null, null, attachAlertDelegate, Theme.key_dialogBackground);
        themeDescriptionArr[366] = new ThemeDescription(null, 0, null, null, null, attachAlertDelegate, Theme.key_dialogBackgroundGray);
        themeDescriptionArr[367] = new ThemeDescription(null, 0, null, null, null, attachAlertDelegate, Theme.key_dialogTextGray2);
        themeDescriptionArr[368] = new ThemeDescription(null, 0, null, null, null, attachAlertDelegate, Theme.key_dialogScrollGlow);
        themeDescriptionArr[369] = new ThemeDescription(null, 0, null, null, null, attachAlertDelegate, Theme.key_dialogGrayLine);
        themeDescriptionArr[370] = new ThemeDescription(null, 0, null, null, null, attachAlertDelegate, Theme.key_dialogCameraIcon);
        return themeDescriptionArr;
    }

    final /* synthetic */ void lambda$getThemeDescriptions$78$ChatActivity() {
        updateVisibleRows();
        if (this.chatActivityEnterView != null && this.chatActivityEnterView.getEmojiView() != null) {
            this.chatActivityEnterView.getEmojiView().updateUIColors();
        }
    }

    final /* synthetic */ void lambda$getThemeDescriptions$79$ChatActivity() {
        if (this.chatAttachAlert != null) {
            this.chatAttachAlert.checkColors();
        }
    }
}
