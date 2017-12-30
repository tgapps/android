package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Layout.Alignment;
import android.text.Layout.Directions;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.StateSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewStructure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import net.hockeyapp.android.UpdateFragment;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.DownloadController.FileDownloadProgressListener;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.ImageReceiver.ImageReceiverDelegate;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessageObject.GroupedMessagePosition;
import org.telegram.messenger.MessageObject.GroupedMessages;
import org.telegram.messenger.MessageObject.TextLayoutBlock;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.MessageFwdHeader;
import org.telegram.tgnet.TLRPC.PhoneCallDiscardReason;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonRequestGeoLocation;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonSwitchInline;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonUrl;
import org.telegram.tgnet.TLRPC.TL_messageMediaContact;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_messageMediaGame;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
import org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
import org.telegram.tgnet.TLRPC.TL_photoSize;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WebPage;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.RoundVideoPlayingDrawable;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.SeekBar.SeekBarDelegate;
import org.telegram.ui.Components.SeekBarWaveform;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.TypefaceSpan;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.SecretMediaViewer;

public class ChatMessageCell extends BaseCell implements FileDownloadProgressListener, ImageReceiverDelegate, SeekBarDelegate {
    private static final int DOCUMENT_ATTACH_TYPE_AUDIO = 3;
    private static final int DOCUMENT_ATTACH_TYPE_DOCUMENT = 1;
    private static final int DOCUMENT_ATTACH_TYPE_GIF = 2;
    private static final int DOCUMENT_ATTACH_TYPE_MUSIC = 5;
    private static final int DOCUMENT_ATTACH_TYPE_NONE = 0;
    private static final int DOCUMENT_ATTACH_TYPE_ROUND = 7;
    private static final int DOCUMENT_ATTACH_TYPE_STICKER = 6;
    private static final int DOCUMENT_ATTACH_TYPE_VIDEO = 4;
    private int TAG;
    private StaticLayout adminLayout;
    private boolean allowAssistant;
    private StaticLayout authorLayout;
    private int authorX;
    private int availableTimeWidth;
    private AvatarDrawable avatarDrawable;
    private ImageReceiver avatarImage = new ImageReceiver();
    private boolean avatarPressed;
    private int backgroundDrawableLeft;
    private int backgroundDrawableRight;
    private int backgroundWidth = 100;
    private ArrayList<BotButton> botButtons = new ArrayList();
    private HashMap<String, BotButton> botButtonsByData = new HashMap();
    private HashMap<String, BotButton> botButtonsByPosition = new HashMap();
    private String botButtonsLayout;
    private int buttonPressed;
    private int buttonState;
    private int buttonX;
    private int buttonY;
    private boolean cancelLoading;
    private int captionHeight;
    private StaticLayout captionLayout;
    private int captionX;
    private int captionY;
    private AvatarDrawable contactAvatarDrawable;
    private float controlsAlpha = 1.0f;
    private int currentAccount = UserConfig.selectedAccount;
    private Drawable currentBackgroundDrawable;
    private Chat currentChat;
    private Chat currentForwardChannel;
    private String currentForwardNameString;
    private User currentForwardUser;
    private MessageObject currentMessageObject;
    private GroupedMessages currentMessagesGroup;
    private String currentNameString;
    private FileLocation currentPhoto;
    private String currentPhotoFilter;
    private String currentPhotoFilterThumb;
    private PhotoSize currentPhotoObject;
    private PhotoSize currentPhotoObjectThumb;
    private GroupedMessagePosition currentPosition;
    private FileLocation currentReplyPhoto;
    private String currentTimeString;
    private String currentUrl;
    private User currentUser;
    private User currentViaBotUser;
    private String currentViewsString;
    private ChatMessageCellDelegate delegate;
    private RectF deleteProgressRect = new RectF();
    private StaticLayout descriptionLayout;
    private int descriptionX;
    private int descriptionY;
    private boolean disallowLongPress;
    private StaticLayout docTitleLayout;
    private int docTitleOffsetX;
    private Document documentAttach;
    private int documentAttachType;
    private boolean drawBackground = true;
    private boolean drawForwardedName;
    private boolean drawImageButton;
    private boolean drawInstantView;
    private int drawInstantViewType;
    private boolean drawJoinChannelView;
    private boolean drawJoinGroupView;
    private boolean drawName;
    private boolean drawNameLayout;
    private boolean drawPhotoImage;
    private boolean drawPinnedBottom;
    private boolean drawPinnedTop;
    private boolean drawRadialCheckBackground;
    private boolean drawShareButton;
    private boolean drawTime = true;
    private boolean drwaShareGoIcon;
    private StaticLayout durationLayout;
    private int durationWidth;
    private int firstVisibleBlockNum;
    private boolean forceNotDrawTime;
    private boolean forwardBotPressed;
    private boolean forwardName;
    private float[] forwardNameOffsetX = new float[2];
    private boolean forwardNamePressed;
    private int forwardNameX;
    private int forwardNameY;
    private StaticLayout[] forwardedNameLayout = new StaticLayout[2];
    private int forwardedNameWidth;
    private boolean fullyDraw;
    private boolean gamePreviewPressed;
    private boolean groupPhotoInvisible;
    private boolean hasGamePreview;
    private boolean hasInvoicePreview;
    private boolean hasLinkPreview;
    private boolean hasNewLineForTime;
    private boolean hasOldCaptionPreview;
    private int highlightProgress;
    private boolean imagePressed;
    private boolean inLayout;
    private StaticLayout infoLayout;
    private int infoWidth;
    private boolean instantButtonPressed;
    private boolean instantPressed;
    private int instantTextX;
    private StaticLayout instantViewLayout;
    private Drawable instantViewSelectorDrawable;
    private int instantWidth;
    private Runnable invalidateRunnable = new Runnable() {
        public void run() {
            ChatMessageCell.this.checkLocationExpired();
            if (ChatMessageCell.this.locationExpired) {
                ChatMessageCell.this.invalidate();
                ChatMessageCell.this.scheduledInvalidate = false;
                return;
            }
            ChatMessageCell.this.invalidate(((int) ChatMessageCell.this.rect.left) - 5, ((int) ChatMessageCell.this.rect.top) - 5, ((int) ChatMessageCell.this.rect.right) + 5, ((int) ChatMessageCell.this.rect.bottom) + 5);
            if (ChatMessageCell.this.scheduledInvalidate) {
                AndroidUtilities.runOnUIThread(ChatMessageCell.this.invalidateRunnable, 1000);
            }
        }
    };
    private boolean isAvatarVisible;
    public boolean isChat;
    private boolean isCheckPressed = true;
    private boolean isHighlighted;
    private boolean isHighlightedAnimated;
    private boolean isPressed;
    private boolean isSmallImage;
    private int keyboardHeight;
    private long lastControlsAlphaChangeTime;
    private int lastDeleteDate;
    private int lastHeight;
    private long lastHighlightProgressTime;
    private int lastSendState;
    private int lastTime;
    private int lastViewsCount;
    private int lastVisibleBlockNum;
    private int layoutHeight;
    private int layoutWidth;
    private int linkBlockNum;
    private int linkPreviewHeight;
    private boolean linkPreviewPressed;
    private int linkSelectionBlockNum;
    private boolean locationExpired;
    private ImageReceiver locationImageReceiver;
    private boolean mediaBackground;
    private int mediaOffsetY;
    private boolean mediaWasInvisible;
    private StaticLayout nameLayout;
    private float nameOffsetX;
    private int nameWidth;
    private float nameX;
    private float nameY;
    private int namesOffset;
    private boolean needNewVisiblePart;
    private boolean needReplyImage;
    private boolean otherPressed;
    private int otherX;
    private int otherY;
    private StaticLayout performerLayout;
    private int performerX;
    private ImageReceiver photoImage;
    private boolean photoNotSet;
    private StaticLayout photosCountLayout;
    private int photosCountWidth;
    private boolean pinnedBottom;
    private boolean pinnedTop;
    private int pressedBotButton;
    private CharacterStyle pressedLink;
    private int pressedLinkType;
    private int[] pressedState = new int[]{16842910, 16842919};
    private RadialProgress radialProgress;
    private RectF rect = new RectF();
    private ImageReceiver replyImageReceiver;
    private StaticLayout replyNameLayout;
    private float replyNameOffset;
    private int replyNameWidth;
    private boolean replyPressed;
    private int replyStartX;
    private int replyStartY;
    private StaticLayout replyTextLayout;
    private float replyTextOffset;
    private int replyTextWidth;
    private RoundVideoPlayingDrawable roundVideoPlayingDrawable;
    private boolean scheduledInvalidate;
    private Rect scrollRect = new Rect();
    private SeekBar seekBar;
    private SeekBarWaveform seekBarWaveform;
    private int seekBarX;
    private int seekBarY;
    private boolean sharePressed;
    private int shareStartX;
    private int shareStartY;
    private StaticLayout siteNameLayout;
    private StaticLayout songLayout;
    private int songX;
    private int substractBackgroundHeight;
    private int textX;
    private int textY;
    private float timeAlpha = 1.0f;
    private int timeAudioX;
    private StaticLayout timeLayout;
    private int timeTextWidth;
    private boolean timeWasInvisible;
    private int timeWidth;
    private int timeWidthAudio;
    private int timeX;
    private StaticLayout titleLayout;
    private int titleX;
    private long totalChangeTime;
    private int totalHeight;
    private int totalVisibleBlocksCount;
    private ArrayList<LinkPath> urlPath = new ArrayList();
    private ArrayList<LinkPath> urlPathCache = new ArrayList();
    private ArrayList<LinkPath> urlPathSelection = new ArrayList();
    private boolean useSeekBarWaweform;
    private int viaNameWidth;
    private int viaWidth;
    private StaticLayout videoInfoLayout;
    private StaticLayout viewsLayout;
    private int viewsTextWidth;
    private boolean wasLayout;
    private int widthBeforeNewTimeLine;
    private int widthForButtons;

    private class BotButton {
        private int angle;
        private KeyboardButton button;
        private int height;
        private long lastUpdateTime;
        private float progressAlpha;
        private StaticLayout title;
        private int width;
        private int x;
        private int y;

        private BotButton() {
        }
    }

    public interface ChatMessageCellDelegate {
        boolean canPerformActions();

        void didLongPressed(ChatMessageCell chatMessageCell);

        void didPressedBotButton(ChatMessageCell chatMessageCell, KeyboardButton keyboardButton);

        void didPressedCancelSendButton(ChatMessageCell chatMessageCell);

        void didPressedChannelAvatar(ChatMessageCell chatMessageCell, Chat chat, int i);

        void didPressedImage(ChatMessageCell chatMessageCell);

        void didPressedInstantButton(ChatMessageCell chatMessageCell, int i);

        void didPressedOther(ChatMessageCell chatMessageCell);

        void didPressedReplyMessage(ChatMessageCell chatMessageCell, int i);

        void didPressedShare(ChatMessageCell chatMessageCell);

        void didPressedUrl(MessageObject messageObject, CharacterStyle characterStyle, boolean z);

        void didPressedUserAvatar(ChatMessageCell chatMessageCell, User user);

        void didPressedViaBot(ChatMessageCell chatMessageCell, String str);

        boolean isChatAdminCell(int i);

        void needOpenWebView(String str, String str2, String str3, String str4, int i, int i2);

        boolean needPlayMessage(MessageObject messageObject);
    }

    public ChatMessageCell(Context context) {
        super(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(21.0f));
        this.avatarDrawable = new AvatarDrawable();
        this.replyImageReceiver = new ImageReceiver(this);
        this.locationImageReceiver = new ImageReceiver(this);
        this.locationImageReceiver.setRoundRadius(AndroidUtilities.dp(26.1f));
        this.TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
        this.contactAvatarDrawable = new AvatarDrawable();
        this.photoImage = new ImageReceiver(this);
        this.photoImage.setDelegate(this);
        this.radialProgress = new RadialProgress(this);
        this.seekBar = new SeekBar(context);
        this.seekBar.setDelegate(this);
        this.seekBarWaveform = new SeekBarWaveform(context);
        this.seekBarWaveform.setDelegate(this);
        this.seekBarWaveform.setParentView(this);
        this.roundVideoPlayingDrawable = new RoundVideoPlayingDrawable(this);
    }

    private void resetPressedLink(int type) {
        if (this.pressedLink == null) {
            return;
        }
        if (this.pressedLinkType == type || type == -1) {
            resetUrlPaths(false);
            this.pressedLink = null;
            this.pressedLinkType = -1;
            invalidate();
        }
    }

    private void resetUrlPaths(boolean text) {
        if (text) {
            if (!this.urlPathSelection.isEmpty()) {
                this.urlPathCache.addAll(this.urlPathSelection);
                this.urlPathSelection.clear();
            }
        } else if (!this.urlPath.isEmpty()) {
            this.urlPathCache.addAll(this.urlPath);
            this.urlPath.clear();
        }
    }

    private LinkPath obtainNewUrlPath(boolean text) {
        LinkPath linkPath;
        if (this.urlPathCache.isEmpty()) {
            linkPath = new LinkPath();
        } else {
            linkPath = (LinkPath) this.urlPathCache.get(0);
            this.urlPathCache.remove(0);
        }
        if (text) {
            this.urlPathSelection.add(linkPath);
        } else {
            this.urlPath.add(linkPath);
        }
        return linkPath;
    }

    private boolean checkTextBlockMotionEvent(MotionEvent event) {
        if (this.currentMessageObject.type != 0 || this.currentMessageObject.textLayoutBlocks == null || this.currentMessageObject.textLayoutBlocks.isEmpty() || !(this.currentMessageObject.messageText instanceof Spannable)) {
            return false;
        }
        if (event.getAction() == 0 || (event.getAction() == 1 && this.pressedLinkType == 1)) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (x < this.textX || y < this.textY || x > this.textX + this.currentMessageObject.textWidth || y > this.textY + this.currentMessageObject.textHeight) {
                resetPressedLink(1);
            } else {
                y -= this.textY;
                int blockNum = 0;
                int a = 0;
                while (a < this.currentMessageObject.textLayoutBlocks.size() && ((TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(a)).textYOffset <= ((float) y)) {
                    blockNum = a;
                    a++;
                }
                try {
                    TextLayoutBlock block = (TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(blockNum);
                    x = (int) (((float) x) - (((float) this.textX) - (block.isRtl() ? this.currentMessageObject.textXOffset : 0.0f)));
                    int line = block.textLayout.getLineForVertical((int) (((float) y) - block.textYOffset));
                    int off = block.textLayout.getOffsetForHorizontal(line, (float) x);
                    float left = block.textLayout.getLineLeft(line);
                    if (left <= ((float) x) && block.textLayout.getLineWidth(line) + left >= ((float) x)) {
                        Spannable buffer = this.currentMessageObject.messageText;
                        CharacterStyle[] link = (CharacterStyle[]) buffer.getSpans(off, off, ClickableSpan.class);
                        boolean isMono = false;
                        if (link == null || link.length == 0) {
                            link = (CharacterStyle[]) buffer.getSpans(off, off, URLSpanMono.class);
                            isMono = true;
                        }
                        boolean ignore = false;
                        if (link.length == 0 || !(link.length == 0 || !(link[0] instanceof URLSpanBotCommand) || URLSpanBotCommand.enabled)) {
                            ignore = true;
                        }
                        if (!ignore) {
                            if (event.getAction() == 0) {
                                this.pressedLink = link[0];
                                this.linkBlockNum = blockNum;
                                this.pressedLinkType = 1;
                                resetUrlPaths(false);
                                try {
                                    TextLayoutBlock nextBlock;
                                    CharacterStyle[] nextLink;
                                    Path path = obtainNewUrlPath(false);
                                    int start = buffer.getSpanStart(this.pressedLink);
                                    int end = buffer.getSpanEnd(this.pressedLink);
                                    path.setCurrentLayout(block.textLayout, start, 0.0f);
                                    block.textLayout.getSelectionPath(start, end, path);
                                    if (end >= block.charactersEnd) {
                                        a = blockNum + 1;
                                        while (a < this.currentMessageObject.textLayoutBlocks.size()) {
                                            nextBlock = (TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(a);
                                            nextLink = (CharacterStyle[]) buffer.getSpans(nextBlock.charactersOffset, nextBlock.charactersOffset, isMono ? URLSpanMono.class : ClickableSpan.class);
                                            if (nextLink != null && nextLink.length != 0 && nextLink[0] == this.pressedLink) {
                                                path = obtainNewUrlPath(false);
                                                path.setCurrentLayout(nextBlock.textLayout, 0, nextBlock.textYOffset - block.textYOffset);
                                                nextBlock.textLayout.getSelectionPath(0, end, path);
                                                if (end < nextBlock.charactersEnd - 1) {
                                                    break;
                                                }
                                                a++;
                                            } else {
                                                break;
                                            }
                                        }
                                    }
                                    if (start <= block.charactersOffset) {
                                        int offsetY = 0;
                                        a = blockNum - 1;
                                        while (a >= 0) {
                                            nextBlock = (TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(a);
                                            nextLink = (CharacterStyle[]) buffer.getSpans(nextBlock.charactersEnd - 1, nextBlock.charactersEnd - 1, isMono ? URLSpanMono.class : ClickableSpan.class);
                                            if (nextLink != null && nextLink.length != 0) {
                                                if (nextLink[0] == this.pressedLink) {
                                                    path = obtainNewUrlPath(false);
                                                    start = buffer.getSpanStart(this.pressedLink);
                                                    offsetY -= nextBlock.height;
                                                    path.setCurrentLayout(nextBlock.textLayout, start, (float) offsetY);
                                                    nextBlock.textLayout.getSelectionPath(start, buffer.getSpanEnd(this.pressedLink), path);
                                                    if (start <= nextBlock.charactersOffset) {
                                                        a--;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } catch (Throwable e) {
                                    FileLog.e(e);
                                }
                                invalidate();
                                return true;
                            }
                            if (link[0] == this.pressedLink) {
                                this.delegate.didPressedUrl(this.currentMessageObject, this.pressedLink, false);
                                resetPressedLink(1);
                                return true;
                            }
                        }
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        }
        return false;
    }

    private boolean checkCaptionMotionEvent(MotionEvent event) {
        if (!(this.currentMessageObject.caption instanceof Spannable) || this.captionLayout == null) {
            return false;
        }
        if (event.getAction() == 0 || ((this.linkPreviewPressed || this.pressedLink != null) && event.getAction() == 1)) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (x < this.captionX || x > this.captionX + this.backgroundWidth || y < this.captionY || y > this.captionY + this.captionHeight) {
                resetPressedLink(3);
            } else if (event.getAction() == 0) {
                try {
                    x -= this.captionX;
                    int line = this.captionLayout.getLineForVertical(y - this.captionY);
                    int off = this.captionLayout.getOffsetForHorizontal(line, (float) x);
                    float left = this.captionLayout.getLineLeft(line);
                    if (left <= ((float) x) && this.captionLayout.getLineWidth(line) + left >= ((float) x)) {
                        Spannable buffer = this.currentMessageObject.caption;
                        ClickableSpan[] link = (ClickableSpan[]) buffer.getSpans(off, off, ClickableSpan.class);
                        boolean ignore = false;
                        if (link.length == 0 || !(link.length == 0 || !(link[0] instanceof URLSpanBotCommand) || URLSpanBotCommand.enabled)) {
                            ignore = true;
                        }
                        if (!ignore) {
                            this.pressedLink = link[0];
                            this.pressedLinkType = 3;
                            resetUrlPaths(false);
                            try {
                                LinkPath path = obtainNewUrlPath(false);
                                int start = buffer.getSpanStart(this.pressedLink);
                                path.setCurrentLayout(this.captionLayout, start, 0.0f);
                                this.captionLayout.getSelectionPath(start, buffer.getSpanEnd(this.pressedLink), path);
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                            invalidate();
                            return true;
                        }
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            } else if (this.pressedLinkType == 3) {
                this.delegate.didPressedUrl(this.currentMessageObject, this.pressedLink, false);
                resetPressedLink(3);
                return true;
            }
        }
        return false;
    }

    private boolean checkGameMotionEvent(MotionEvent event) {
        if (!this.hasGamePreview) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == 0) {
            if (this.drawPhotoImage && this.photoImage.isInsideImage((float) x, (float) y)) {
                this.gamePreviewPressed = true;
                return true;
            } else if (this.descriptionLayout != null && y >= this.descriptionY) {
                try {
                    x -= (this.textX + AndroidUtilities.dp(10.0f)) + this.descriptionX;
                    int line = this.descriptionLayout.getLineForVertical(y - this.descriptionY);
                    int off = this.descriptionLayout.getOffsetForHorizontal(line, (float) x);
                    float left = this.descriptionLayout.getLineLeft(line);
                    if (left <= ((float) x) && this.descriptionLayout.getLineWidth(line) + left >= ((float) x)) {
                        Spannable buffer = this.currentMessageObject.linkDescription;
                        ClickableSpan[] link = (ClickableSpan[]) buffer.getSpans(off, off, ClickableSpan.class);
                        boolean ignore = false;
                        if (link.length == 0 || !(link.length == 0 || !(link[0] instanceof URLSpanBotCommand) || URLSpanBotCommand.enabled)) {
                            ignore = true;
                        }
                        if (!ignore) {
                            this.pressedLink = link[0];
                            this.linkBlockNum = -10;
                            this.pressedLinkType = 2;
                            resetUrlPaths(false);
                            try {
                                LinkPath path = obtainNewUrlPath(false);
                                int start = buffer.getSpanStart(this.pressedLink);
                                path.setCurrentLayout(this.descriptionLayout, start, 0.0f);
                                this.descriptionLayout.getSelectionPath(start, buffer.getSpanEnd(this.pressedLink), path);
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                            invalidate();
                            return true;
                        }
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        } else if (event.getAction() == 1) {
            if (this.pressedLinkType != 2 && !this.gamePreviewPressed) {
                resetPressedLink(2);
            } else if (this.pressedLink != null) {
                if (this.pressedLink instanceof URLSpan) {
                    Browser.openUrl(getContext(), ((URLSpan) this.pressedLink).getURL());
                } else if (this.pressedLink instanceof ClickableSpan) {
                    ((ClickableSpan) this.pressedLink).onClick(this);
                }
                resetPressedLink(2);
            } else {
                this.gamePreviewPressed = false;
                for (int a = 0; a < this.botButtons.size(); a++) {
                    BotButton button = (BotButton) this.botButtons.get(a);
                    if (button.button instanceof TL_keyboardButtonGame) {
                        playSoundEffect(0);
                        this.delegate.didPressedBotButton(this, button.button);
                        invalidate();
                        break;
                    }
                }
                resetPressedLink(2);
                return true;
            }
        }
        return false;
    }

    private boolean checkLinkPreviewMotionEvent(MotionEvent event) {
        if (this.currentMessageObject.type != 0 || !this.hasLinkPreview) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (x >= this.textX && x <= this.textX + this.backgroundWidth && y >= this.textY + this.currentMessageObject.textHeight) {
            if (y <= AndroidUtilities.dp((float) ((this.drawInstantView ? 46 : 0) + 8)) + (this.linkPreviewHeight + (this.textY + this.currentMessageObject.textHeight))) {
                WebPage webPage;
                if (event.getAction() == 0) {
                    if (this.descriptionLayout != null && y >= this.descriptionY) {
                        try {
                            int checkX = x - ((this.textX + AndroidUtilities.dp(10.0f)) + this.descriptionX);
                            int checkY = y - this.descriptionY;
                            if (checkY <= this.descriptionLayout.getHeight()) {
                                int line = this.descriptionLayout.getLineForVertical(checkY);
                                int off = this.descriptionLayout.getOffsetForHorizontal(line, (float) checkX);
                                float left = this.descriptionLayout.getLineLeft(line);
                                if (left <= ((float) checkX) && this.descriptionLayout.getLineWidth(line) + left >= ((float) checkX)) {
                                    Spannable buffer = this.currentMessageObject.linkDescription;
                                    ClickableSpan[] link = (ClickableSpan[]) buffer.getSpans(off, off, ClickableSpan.class);
                                    boolean ignore = false;
                                    if (link.length == 0 || !(link.length == 0 || !(link[0] instanceof URLSpanBotCommand) || URLSpanBotCommand.enabled)) {
                                        ignore = true;
                                    }
                                    if (!ignore) {
                                        this.pressedLink = link[0];
                                        this.linkBlockNum = -10;
                                        this.pressedLinkType = 2;
                                        resetUrlPaths(false);
                                        try {
                                            Path path = obtainNewUrlPath(false);
                                            int start = buffer.getSpanStart(this.pressedLink);
                                            path.setCurrentLayout(this.descriptionLayout, start, 0.0f);
                                            this.descriptionLayout.getSelectionPath(start, buffer.getSpanEnd(this.pressedLink), path);
                                        } catch (Throwable e) {
                                            FileLog.e(e);
                                        }
                                        invalidate();
                                        return true;
                                    }
                                }
                            }
                        } catch (Throwable e2) {
                            FileLog.e(e2);
                        }
                    }
                    if (this.pressedLink == null) {
                        if (this.drawPhotoImage && this.drawImageButton && this.buttonState != -1 && x >= this.buttonX && x <= this.buttonX + AndroidUtilities.dp(48.0f) && y >= this.buttonY && y <= this.buttonY + AndroidUtilities.dp(48.0f)) {
                            this.buttonPressed = 1;
                            return true;
                        } else if (this.drawInstantView) {
                            this.instantPressed = true;
                            if (VERSION.SDK_INT >= 21 && this.instantViewSelectorDrawable != null && this.instantViewSelectorDrawable.getBounds().contains(x, y)) {
                                this.instantViewSelectorDrawable.setState(this.pressedState);
                                this.instantViewSelectorDrawable.setHotspot((float) x, (float) y);
                                this.instantButtonPressed = true;
                            }
                            invalidate();
                            return true;
                        } else if (this.documentAttachType != 1 && this.drawPhotoImage && this.photoImage.isInsideImage((float) x, (float) y)) {
                            this.linkPreviewPressed = true;
                            webPage = this.currentMessageObject.messageOwner.media.webpage;
                            if (this.documentAttachType != 2 || this.buttonState != -1 || !SharedConfig.autoplayGifs || (this.photoImage.getAnimation() != null && TextUtils.isEmpty(webPage.embed_url))) {
                                return true;
                            }
                            this.linkPreviewPressed = false;
                            return false;
                        }
                    }
                } else if (event.getAction() == 1) {
                    if (this.instantPressed) {
                        if (this.delegate != null) {
                            this.delegate.didPressedInstantButton(this, this.drawInstantViewType);
                        }
                        playSoundEffect(0);
                        if (VERSION.SDK_INT >= 21 && this.instantViewSelectorDrawable != null) {
                            this.instantViewSelectorDrawable.setState(StateSet.NOTHING);
                        }
                        this.instantButtonPressed = false;
                        this.instantPressed = false;
                        invalidate();
                    } else if (this.pressedLinkType != 2 && this.buttonPressed == 0 && !this.linkPreviewPressed) {
                        resetPressedLink(2);
                    } else if (this.buttonPressed != 0) {
                        this.buttonPressed = 0;
                        playSoundEffect(0);
                        didPressedButton(false);
                        invalidate();
                    } else if (this.pressedLink != null) {
                        if (this.pressedLink instanceof URLSpan) {
                            Browser.openUrl(getContext(), ((URLSpan) this.pressedLink).getURL());
                        } else if (this.pressedLink instanceof ClickableSpan) {
                            ((ClickableSpan) this.pressedLink).onClick(this);
                        }
                        resetPressedLink(2);
                    } else {
                        if (this.documentAttachType == 7) {
                            if (!MediaController.getInstance().isPlayingMessage(this.currentMessageObject) || MediaController.getInstance().isMessagePaused()) {
                                this.delegate.needPlayMessage(this.currentMessageObject);
                            } else {
                                MediaController.getInstance().pauseMessage(this.currentMessageObject);
                            }
                        } else if (this.documentAttachType != 2 || !this.drawImageButton) {
                            webPage = this.currentMessageObject.messageOwner.media.webpage;
                            if (webPage != null && !TextUtils.isEmpty(webPage.embed_url)) {
                                this.delegate.needOpenWebView(webPage.embed_url, webPage.site_name, webPage.title, webPage.url, webPage.embed_width, webPage.embed_height);
                            } else if (this.buttonState == -1 || this.buttonState == 3) {
                                this.delegate.didPressedImage(this);
                                playSoundEffect(0);
                            } else if (webPage != null) {
                                Browser.openUrl(getContext(), webPage.url);
                            }
                        } else if (this.buttonState == -1) {
                            if (SharedConfig.autoplayGifs) {
                                this.delegate.didPressedImage(this);
                            } else {
                                this.buttonState = 2;
                                this.currentMessageObject.gifState = 1.0f;
                                this.photoImage.setAllowStartAnimation(false);
                                this.photoImage.stopAnimation();
                                this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                                invalidate();
                                playSoundEffect(0);
                            }
                        } else if (this.buttonState == 2 || this.buttonState == 0) {
                            didPressedButton(false);
                            playSoundEffect(0);
                        }
                        resetPressedLink(2);
                        return true;
                    }
                } else if (event.getAction() == 2 && this.instantButtonPressed && VERSION.SDK_INT >= 21 && this.instantViewSelectorDrawable != null) {
                    this.instantViewSelectorDrawable.setHotspot((float) x, (float) y);
                }
            }
        }
        return false;
    }

    private boolean checkOtherButtonMotionEvent(MotionEvent event) {
        boolean allow;
        if (this.currentMessageObject.type == 16) {
            allow = true;
        } else {
            allow = false;
        }
        if (!allow) {
            if ((this.documentAttachType != 1 && this.currentMessageObject.type != 12 && this.documentAttachType != 5 && this.documentAttachType != 4 && this.documentAttachType != 2 && this.currentMessageObject.type != 8) || this.hasGamePreview || this.hasInvoicePreview) {
                allow = false;
            } else {
                allow = true;
            }
        }
        if (!allow) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        boolean result = false;
        if (event.getAction() == 0) {
            if (this.currentMessageObject.type == 16) {
                if (x >= this.otherX && x <= this.otherX + AndroidUtilities.dp(235.0f) && y >= this.otherY - AndroidUtilities.dp(14.0f) && y <= this.otherY + AndroidUtilities.dp(50.0f)) {
                    this.otherPressed = true;
                    result = true;
                    invalidate();
                }
            } else if (x >= this.otherX - AndroidUtilities.dp(20.0f) && x <= this.otherX + AndroidUtilities.dp(20.0f) && y >= this.otherY - AndroidUtilities.dp(4.0f) && y <= this.otherY + AndroidUtilities.dp(30.0f)) {
                this.otherPressed = true;
                result = true;
                invalidate();
            }
        } else if (event.getAction() == 1 && this.otherPressed) {
            this.otherPressed = false;
            playSoundEffect(0);
            this.delegate.didPressedOther(this);
            invalidate();
            result = true;
        }
        return result;
    }

    private boolean checkPhotoImageMotionEvent(MotionEvent event) {
        if (!this.drawPhotoImage && this.documentAttachType != 1) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        boolean result = false;
        if (event.getAction() == 0) {
            if (this.buttonState != -1 && x >= this.buttonX && x <= this.buttonX + AndroidUtilities.dp(48.0f) && y >= this.buttonY && y <= this.buttonY + AndroidUtilities.dp(48.0f)) {
                this.buttonPressed = 1;
                invalidate();
                result = true;
            } else if (this.documentAttachType == 1) {
                if (x >= this.photoImage.getImageX() && x <= (this.photoImage.getImageX() + this.backgroundWidth) - AndroidUtilities.dp(50.0f) && y >= this.photoImage.getImageY() && y <= this.photoImage.getImageY() + this.photoImage.getImageHeight()) {
                    this.imagePressed = true;
                    result = true;
                }
            } else if (!(this.currentMessageObject.type == 13 && this.currentMessageObject.getInputStickerSet() == null)) {
                if (x >= this.photoImage.getImageX() && x <= this.photoImage.getImageX() + this.backgroundWidth && y >= this.photoImage.getImageY() && y <= this.photoImage.getImageY() + this.photoImage.getImageHeight()) {
                    this.imagePressed = true;
                    result = true;
                }
                if (this.currentMessageObject.type == 12 && MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.currentMessageObject.messageOwner.media.user_id)) == null) {
                    this.imagePressed = false;
                    result = false;
                }
            }
            if (!this.imagePressed) {
                return result;
            }
            if (this.currentMessageObject.isSendError()) {
                this.imagePressed = false;
                return false;
            } else if (this.currentMessageObject.type == 8 && this.buttonState == -1 && SharedConfig.autoplayGifs && this.photoImage.getAnimation() == null) {
                this.imagePressed = false;
                return false;
            } else if (this.currentMessageObject.type != 5 || this.buttonState == -1) {
                return result;
            } else {
                this.imagePressed = false;
                return false;
            }
        } else if (event.getAction() != 1) {
            return false;
        } else {
            if (this.buttonPressed == 1) {
                this.buttonPressed = 0;
                playSoundEffect(0);
                didPressedButton(false);
                updateRadialProgressBackground(true);
                invalidate();
                return false;
            } else if (!this.imagePressed) {
                return false;
            } else {
                this.imagePressed = false;
                if (this.buttonState == -1 || this.buttonState == 2 || this.buttonState == 3) {
                    playSoundEffect(0);
                    didClickedImage();
                } else if (this.buttonState == 0 && this.documentAttachType == 1) {
                    playSoundEffect(0);
                    didPressedButton(false);
                }
                invalidate();
                return false;
            }
        }
    }

    private boolean checkAudioMotionEvent(MotionEvent event) {
        if (this.documentAttachType != 3 && this.documentAttachType != 5) {
            return false;
        }
        boolean result;
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (this.useSeekBarWaweform) {
            result = this.seekBarWaveform.onTouch(event.getAction(), (event.getX() - ((float) this.seekBarX)) - ((float) AndroidUtilities.dp(13.0f)), event.getY() - ((float) this.seekBarY));
        } else {
            result = this.seekBar.onTouch(event.getAction(), event.getX() - ((float) this.seekBarX), event.getY() - ((float) this.seekBarY));
        }
        if (result) {
            if (!this.useSeekBarWaweform && event.getAction() == 0) {
                getParent().requestDisallowInterceptTouchEvent(true);
            } else if (this.useSeekBarWaweform && !this.seekBarWaveform.isStartDraging() && event.getAction() == 1) {
                didPressedButton(true);
            }
            this.disallowLongPress = true;
            invalidate();
            return result;
        }
        boolean area;
        int side = AndroidUtilities.dp(36.0f);
        if (this.buttonState == 0 || this.buttonState == 1 || this.buttonState == 2) {
            if (x >= this.buttonX - AndroidUtilities.dp(12.0f) && x <= (this.buttonX - AndroidUtilities.dp(12.0f)) + this.backgroundWidth) {
                if (y >= (this.drawInstantView ? this.buttonY : this.namesOffset + this.mediaOffsetY)) {
                    if (y <= (this.drawInstantView ? this.buttonY + side : (this.namesOffset + this.mediaOffsetY) + AndroidUtilities.dp(82.0f))) {
                        area = true;
                    }
                }
            }
            area = false;
        } else {
            area = x >= this.buttonX && x <= this.buttonX + side && y >= this.buttonY && y <= this.buttonY + side;
        }
        if (event.getAction() == 0) {
            if (!area) {
                return result;
            }
            this.buttonPressed = 1;
            invalidate();
            updateRadialProgressBackground(true);
            return true;
        } else if (this.buttonPressed == 0) {
            return result;
        } else {
            if (event.getAction() == 1) {
                this.buttonPressed = 0;
                playSoundEffect(0);
                didPressedButton(true);
                invalidate();
            } else if (event.getAction() == 3) {
                this.buttonPressed = 0;
                invalidate();
            } else if (event.getAction() == 2 && !area) {
                this.buttonPressed = 0;
                invalidate();
            }
            updateRadialProgressBackground(true);
            return result;
        }
    }

    private boolean checkBotButtonMotionEvent(MotionEvent event) {
        if (this.botButtons.isEmpty() || this.currentMessageObject.eventId != 0) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        if (event.getAction() == 0) {
            int addX;
            if (this.currentMessageObject.isOutOwner()) {
                addX = (getMeasuredWidth() - this.widthForButtons) - AndroidUtilities.dp(10.0f);
            } else {
                addX = this.backgroundDrawableLeft + AndroidUtilities.dp(this.mediaBackground ? 1.0f : 7.0f);
            }
            int a = 0;
            while (a < this.botButtons.size()) {
                BotButton button = (BotButton) this.botButtons.get(a);
                int y2 = (button.y + this.layoutHeight) - AndroidUtilities.dp(2.0f);
                if (x < button.x + addX || x > (button.x + addX) + button.width || y < y2 || y > button.height + y2) {
                    a++;
                } else {
                    this.pressedBotButton = a;
                    invalidate();
                    return true;
                }
            }
            return false;
        } else if (event.getAction() != 1 || this.pressedBotButton == -1) {
            return false;
        } else {
            playSoundEffect(0);
            this.delegate.didPressedBotButton(this, ((BotButton) this.botButtons.get(this.pressedBotButton)).button);
            this.pressedBotButton = -1;
            invalidate();
            return false;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.currentMessageObject == null || !this.delegate.canPerformActions()) {
            return super.onTouchEvent(event);
        }
        this.disallowLongPress = false;
        boolean result = checkTextBlockMotionEvent(event);
        if (!result) {
            result = checkOtherButtonMotionEvent(event);
        }
        if (!result) {
            result = checkCaptionMotionEvent(event);
        }
        if (!result) {
            result = checkAudioMotionEvent(event);
        }
        if (!result) {
            result = checkLinkPreviewMotionEvent(event);
        }
        if (!result) {
            result = checkGameMotionEvent(event);
        }
        if (!result) {
            result = checkPhotoImageMotionEvent(event);
        }
        if (!result) {
            result = checkBotButtonMotionEvent(event);
        }
        if (event.getAction() == 3) {
            this.buttonPressed = 0;
            this.pressedBotButton = -1;
            this.linkPreviewPressed = false;
            this.otherPressed = false;
            this.imagePressed = false;
            this.gamePreviewPressed = false;
            this.instantButtonPressed = false;
            this.instantPressed = false;
            if (VERSION.SDK_INT >= 21 && this.instantViewSelectorDrawable != null) {
                this.instantViewSelectorDrawable.setState(StateSet.NOTHING);
            }
            result = false;
            resetPressedLink(-1);
        }
        if (!this.disallowLongPress && result && event.getAction() == 0) {
            startCheckLongPress();
        }
        if (!(event.getAction() == 0 || event.getAction() == 2)) {
            cancelCheckLongPress();
        }
        if (result) {
            return result;
        }
        float x = event.getX();
        float y = event.getY();
        int replyEnd;
        if (event.getAction() != 0) {
            if (event.getAction() != 2) {
                cancelCheckLongPress();
            }
            if (this.avatarPressed) {
                if (event.getAction() == 1) {
                    this.avatarPressed = false;
                    playSoundEffect(0);
                    if (this.delegate == null) {
                        return result;
                    }
                    if (this.currentUser != null) {
                        this.delegate.didPressedUserAvatar(this, this.currentUser);
                        return result;
                    } else if (this.currentChat == null) {
                        return result;
                    } else {
                        this.delegate.didPressedChannelAvatar(this, this.currentChat, 0);
                        return result;
                    }
                } else if (event.getAction() == 3) {
                    this.avatarPressed = false;
                    return result;
                } else if (event.getAction() != 2 || !this.isAvatarVisible || this.avatarImage.isInsideImage(x, ((float) getTop()) + y)) {
                    return result;
                } else {
                    this.avatarPressed = false;
                    return result;
                }
            } else if (this.forwardNamePressed) {
                if (event.getAction() == 1) {
                    this.forwardNamePressed = false;
                    playSoundEffect(0);
                    if (this.delegate == null) {
                        return result;
                    }
                    if (this.currentForwardChannel != null) {
                        this.delegate.didPressedChannelAvatar(this, this.currentForwardChannel, this.currentMessageObject.messageOwner.fwd_from.channel_post);
                        return result;
                    } else if (this.currentForwardUser == null) {
                        return result;
                    } else {
                        this.delegate.didPressedUserAvatar(this, this.currentForwardUser);
                        return result;
                    }
                } else if (event.getAction() == 3) {
                    this.forwardNamePressed = false;
                    return result;
                } else if (event.getAction() != 2) {
                    return result;
                } else {
                    if (x >= ((float) this.forwardNameX) && x <= ((float) (this.forwardNameX + this.forwardedNameWidth)) && y >= ((float) this.forwardNameY) && y <= ((float) (this.forwardNameY + AndroidUtilities.dp(32.0f)))) {
                        return result;
                    }
                    this.forwardNamePressed = false;
                    return result;
                }
            } else if (this.forwardBotPressed) {
                if (event.getAction() == 1) {
                    this.forwardBotPressed = false;
                    playSoundEffect(0);
                    if (this.delegate == null) {
                        return result;
                    }
                    this.delegate.didPressedViaBot(this, this.currentViaBotUser != null ? this.currentViaBotUser.username : this.currentMessageObject.messageOwner.via_bot_name);
                    return result;
                } else if (event.getAction() == 3) {
                    this.forwardBotPressed = false;
                    return result;
                } else if (event.getAction() != 2) {
                    return result;
                } else {
                    if (!this.drawForwardedName || this.forwardedNameLayout[0] == null) {
                        if (x >= this.nameX + ((float) this.viaNameWidth) && x <= (this.nameX + ((float) this.viaNameWidth)) + ((float) this.viaWidth) && y >= this.nameY - ((float) AndroidUtilities.dp(4.0f)) && y <= this.nameY + ((float) AndroidUtilities.dp(20.0f))) {
                            return result;
                        }
                        this.forwardBotPressed = false;
                        return result;
                    } else if (x >= ((float) this.forwardNameX) && x <= ((float) (this.forwardNameX + this.forwardedNameWidth)) && y >= ((float) this.forwardNameY) && y <= ((float) (this.forwardNameY + AndroidUtilities.dp(32.0f)))) {
                        return result;
                    } else {
                        this.forwardBotPressed = false;
                        return result;
                    }
                }
            } else if (this.replyPressed) {
                if (event.getAction() == 1) {
                    this.replyPressed = false;
                    playSoundEffect(0);
                    if (this.delegate == null) {
                        return result;
                    }
                    this.delegate.didPressedReplyMessage(this, this.currentMessageObject.messageOwner.reply_to_msg_id);
                    return result;
                } else if (event.getAction() == 3) {
                    this.replyPressed = false;
                    return result;
                } else if (event.getAction() != 2) {
                    return result;
                } else {
                    if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                        replyEnd = this.replyStartX + Math.max(this.replyNameWidth, this.replyTextWidth);
                    } else {
                        replyEnd = this.replyStartX + this.backgroundDrawableRight;
                    }
                    if (x >= ((float) this.replyStartX) && x <= ((float) replyEnd) && y >= ((float) this.replyStartY) && y <= ((float) (this.replyStartY + AndroidUtilities.dp(35.0f)))) {
                        return result;
                    }
                    this.replyPressed = false;
                    return result;
                }
            } else if (!this.sharePressed) {
                return result;
            } else {
                if (event.getAction() == 1) {
                    this.sharePressed = false;
                    playSoundEffect(0);
                    if (this.delegate != null) {
                        this.delegate.didPressedShare(this);
                    }
                } else if (event.getAction() == 3) {
                    this.sharePressed = false;
                } else if (event.getAction() == 2 && (x < ((float) this.shareStartX) || x > ((float) (this.shareStartX + AndroidUtilities.dp(40.0f))) || y < ((float) this.shareStartY) || y > ((float) (this.shareStartY + AndroidUtilities.dp(32.0f))))) {
                    this.sharePressed = false;
                }
                invalidate();
                return result;
            }
        } else if (this.delegate != null && !this.delegate.canPerformActions()) {
            return result;
        } else {
            if (this.isAvatarVisible && this.avatarImage.isInsideImage(x, ((float) getTop()) + y)) {
                this.avatarPressed = true;
                result = true;
            } else if (this.drawForwardedName && this.forwardedNameLayout[0] != null && x >= ((float) this.forwardNameX) && x <= ((float) (this.forwardNameX + this.forwardedNameWidth)) && y >= ((float) this.forwardNameY) && y <= ((float) (this.forwardNameY + AndroidUtilities.dp(32.0f)))) {
                if (this.viaWidth == 0 || x < ((float) ((this.forwardNameX + this.viaNameWidth) + AndroidUtilities.dp(4.0f)))) {
                    this.forwardNamePressed = true;
                } else {
                    this.forwardBotPressed = true;
                }
                result = true;
            } else if (this.drawNameLayout && this.nameLayout != null && this.viaWidth != 0 && x >= this.nameX + ((float) this.viaNameWidth) && x <= (this.nameX + ((float) this.viaNameWidth)) + ((float) this.viaWidth) && y >= this.nameY - ((float) AndroidUtilities.dp(4.0f)) && y <= this.nameY + ((float) AndroidUtilities.dp(20.0f))) {
                this.forwardBotPressed = true;
                result = true;
            } else if (this.drawShareButton && x >= ((float) this.shareStartX) && x <= ((float) (this.shareStartX + AndroidUtilities.dp(40.0f))) && y >= ((float) this.shareStartY) && y <= ((float) (this.shareStartY + AndroidUtilities.dp(32.0f)))) {
                this.sharePressed = true;
                result = true;
                invalidate();
            } else if (this.replyNameLayout != null) {
                if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                    replyEnd = this.replyStartX + Math.max(this.replyNameWidth, this.replyTextWidth);
                } else {
                    replyEnd = this.replyStartX + this.backgroundDrawableRight;
                }
                if (x >= ((float) this.replyStartX) && x <= ((float) replyEnd) && y >= ((float) this.replyStartY) && y <= ((float) (this.replyStartY + AndroidUtilities.dp(35.0f)))) {
                    this.replyPressed = true;
                    result = true;
                }
            }
            if (!result) {
                return result;
            }
            startCheckLongPress();
            return result;
        }
    }

    public void updatePlayingMessageProgress() {
        if (this.currentMessageObject != null) {
            int duration;
            int a;
            DocumentAttribute attribute;
            String timeString;
            if (this.currentMessageObject.isRoundVideo()) {
                duration = 0;
                Document document = this.currentMessageObject.getDocument();
                for (a = 0; a < document.attributes.size(); a++) {
                    attribute = (DocumentAttribute) document.attributes.get(a);
                    if (attribute instanceof TL_documentAttributeVideo) {
                        duration = attribute.duration;
                        break;
                    }
                }
                if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                    duration = Math.max(0, duration - this.currentMessageObject.audioProgressSec);
                }
                if (this.lastTime != duration) {
                    this.lastTime = duration;
                    timeString = String.format("%02d:%02d", new Object[]{Integer.valueOf(duration / 60), Integer.valueOf(duration % 60)});
                    this.timeWidthAudio = (int) Math.ceil((double) Theme.chat_timePaint.measureText(timeString));
                    this.durationLayout = new StaticLayout(timeString, Theme.chat_timePaint, this.timeWidthAudio, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    invalidate();
                }
            } else if (this.documentAttach != null) {
                if (this.useSeekBarWaweform) {
                    if (!this.seekBarWaveform.isDragging()) {
                        this.seekBarWaveform.setProgress(this.currentMessageObject.audioProgress);
                    }
                } else if (!this.seekBar.isDragging()) {
                    this.seekBar.setProgress(this.currentMessageObject.audioProgress);
                }
                duration = 0;
                if (this.documentAttachType == 3) {
                    if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                        duration = this.currentMessageObject.audioProgressSec;
                    } else {
                        for (a = 0; a < this.documentAttach.attributes.size(); a++) {
                            attribute = (DocumentAttribute) this.documentAttach.attributes.get(a);
                            if (attribute instanceof TL_documentAttributeAudio) {
                                duration = attribute.duration;
                                break;
                            }
                        }
                    }
                    if (this.lastTime != duration) {
                        this.lastTime = duration;
                        timeString = String.format("%02d:%02d", new Object[]{Integer.valueOf(duration / 60), Integer.valueOf(duration % 60)});
                        this.timeWidthAudio = (int) Math.ceil((double) Theme.chat_audioTimePaint.measureText(timeString));
                        this.durationLayout = new StaticLayout(timeString, Theme.chat_audioTimePaint, this.timeWidthAudio, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                } else {
                    int currentProgress = 0;
                    for (a = 0; a < this.documentAttach.attributes.size(); a++) {
                        attribute = (DocumentAttribute) this.documentAttach.attributes.get(a);
                        if (attribute instanceof TL_documentAttributeAudio) {
                            duration = attribute.duration;
                            break;
                        }
                    }
                    if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                        currentProgress = this.currentMessageObject.audioProgressSec;
                    }
                    if (this.lastTime != currentProgress) {
                        this.lastTime = currentProgress;
                        timeString = String.format("%d:%02d / %d:%02d", new Object[]{Integer.valueOf(currentProgress / 60), Integer.valueOf(currentProgress % 60), Integer.valueOf(duration / 60), Integer.valueOf(duration % 60)});
                        this.durationLayout = new StaticLayout(timeString, Theme.chat_audioTimePaint, (int) Math.ceil((double) Theme.chat_audioTimePaint.measureText(timeString)), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    }
                }
                invalidate();
            }
        }
    }

    public void downloadAudioIfNeed() {
        if (this.documentAttachType == 3 && this.buttonState == 2) {
            FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, true, 0);
            this.buttonState = 4;
            this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
        }
    }

    public void setFullyDraw(boolean draw) {
        this.fullyDraw = draw;
    }

    public void setVisiblePart(int position, int height) {
        if (this.currentMessageObject != null && this.currentMessageObject.textLayoutBlocks != null) {
            position -= this.textY;
            int newFirst = -1;
            int newLast = -1;
            int newCount = 0;
            int startBlock = 0;
            int a = 0;
            while (a < this.currentMessageObject.textLayoutBlocks.size() && ((TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(a)).textYOffset <= ((float) position)) {
                startBlock = a;
                a++;
            }
            for (a = startBlock; a < this.currentMessageObject.textLayoutBlocks.size(); a++) {
                TextLayoutBlock block = (TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(a);
                float y = block.textYOffset;
                if (intersect(y, ((float) block.height) + y, (float) position, (float) (position + height))) {
                    if (newFirst == -1) {
                        newFirst = a;
                    }
                    newLast = a;
                    newCount++;
                } else if (y > ((float) position)) {
                    break;
                }
            }
            if (this.lastVisibleBlockNum != newLast || this.firstVisibleBlockNum != newFirst || this.totalVisibleBlocksCount != newCount) {
                this.lastVisibleBlockNum = newLast;
                this.firstVisibleBlockNum = newFirst;
                this.totalVisibleBlocksCount = newCount;
                invalidate();
            }
        }
    }

    private boolean intersect(float left1, float right1, float left2, float right2) {
        if (left1 <= left2) {
            if (right1 >= left2) {
                return true;
            }
            return false;
        } else if (left1 > right2) {
            return false;
        } else {
            return true;
        }
    }

    public static StaticLayout generateStaticLayout(CharSequence text, TextPaint paint, int maxWidth, int smallWidth, int linesCount, int maxLines) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
        int addedChars = 0;
        StaticLayout layout = new StaticLayout(text, paint, smallWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int a = 0;
        while (a < linesCount) {
            Directions directions = layout.getLineDirections(a);
            if (layout.getLineLeft(a) != 0.0f || layout.isRtlCharAt(layout.getLineStart(a)) || layout.isRtlCharAt(layout.getLineEnd(a))) {
                maxWidth = smallWidth;
            }
            int pos = layout.getLineEnd(a);
            if (pos != text.length()) {
                pos--;
                if (stringBuilder.charAt(pos + addedChars) == ' ') {
                    stringBuilder.replace(pos + addedChars, (pos + addedChars) + 1, "\n");
                } else {
                    if (stringBuilder.charAt(pos + addedChars) != '\n') {
                        stringBuilder.insert(pos + addedChars, "\n");
                        addedChars++;
                    }
                }
                if (a == layout.getLineCount() - 1 || a == maxLines - 1) {
                    break;
                }
                a++;
            } else {
                break;
            }
        }
        return StaticLayoutEx.createStaticLayout(stringBuilder, paint, maxWidth, Alignment.ALIGN_NORMAL, 1.0f, (float) AndroidUtilities.dp(1.0f), false, TruncateAt.END, maxWidth, maxLines);
    }

    private void didClickedImage() {
        if (this.currentMessageObject.type == 1 || this.currentMessageObject.type == 13) {
            if (this.buttonState == -1) {
                this.delegate.didPressedImage(this);
            } else if (this.buttonState == 0) {
                didPressedButton(false);
            }
        } else if (this.currentMessageObject.type == 12) {
            this.delegate.didPressedUserAvatar(this, MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.currentMessageObject.messageOwner.media.user_id)));
        } else if (this.currentMessageObject.type == 5) {
            if (!MediaController.getInstance().isPlayingMessage(this.currentMessageObject) || MediaController.getInstance().isMessagePaused()) {
                this.delegate.needPlayMessage(this.currentMessageObject);
            } else {
                MediaController.getInstance().pauseMessage(this.currentMessageObject);
            }
        } else if (this.currentMessageObject.type == 8) {
            if (this.buttonState == -1) {
                if (SharedConfig.autoplayGifs) {
                    this.delegate.didPressedImage(this);
                    return;
                }
                this.buttonState = 2;
                this.currentMessageObject.gifState = 1.0f;
                this.photoImage.setAllowStartAnimation(false);
                this.photoImage.stopAnimation();
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                invalidate();
            } else if (this.buttonState == 2 || this.buttonState == 0) {
                didPressedButton(false);
            }
        } else if (this.documentAttachType == 4) {
            if (this.buttonState == -1) {
                this.delegate.didPressedImage(this);
            } else if (this.buttonState == 0 || this.buttonState == 3) {
                didPressedButton(false);
            }
        } else if (this.currentMessageObject.type == 4) {
            this.delegate.didPressedImage(this);
        } else if (this.documentAttachType == 1) {
            if (this.buttonState == -1) {
                this.delegate.didPressedImage(this);
            }
        } else if (this.documentAttachType == 2) {
            if (this.buttonState == -1) {
                WebPage webPage = this.currentMessageObject.messageOwner.media.webpage;
                if (webPage == null) {
                    return;
                }
                if (webPage.embed_url == null || webPage.embed_url.length() == 0) {
                    Browser.openUrl(getContext(), webPage.url);
                } else {
                    this.delegate.needOpenWebView(webPage.embed_url, webPage.site_name, webPage.description, webPage.url, webPage.embed_width, webPage.embed_height);
                }
            }
        } else if (this.hasInvoicePreview && this.buttonState == -1) {
            this.delegate.didPressedImage(this);
        }
    }

    private void updateSecretTimeText(MessageObject messageObject) {
        if (messageObject != null && messageObject.isSecretPhoto()) {
            String str = messageObject.getSecretTimeString();
            if (str != null) {
                this.infoWidth = (int) Math.ceil((double) Theme.chat_infoPaint.measureText(str));
                this.infoLayout = new StaticLayout(TextUtils.ellipsize(str, Theme.chat_infoPaint, (float) this.infoWidth, TruncateAt.END), Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                invalidate();
            }
        }
    }

    private boolean isPhotoDataChanged(MessageObject object) {
        if (object.type == 0 || object.type == 14) {
            return false;
        }
        if (object.type == 4) {
            if (this.currentUrl == null) {
                return true;
            }
            String url;
            double lat = object.messageOwner.media.geo.lat;
            double lon = object.messageOwner.media.geo._long;
            if (object.messageOwner.media instanceof TL_messageMediaGeoLive) {
                int photoWidth = this.backgroundWidth - AndroidUtilities.dp(21.0f);
                int photoHeight = AndroidUtilities.dp(195.0f);
                double rad = ((double) 268435456) / 3.141592653589793d;
                lat = ((1.5707963267948966d - (2.0d * Math.atan(Math.exp((((double) (Math.round(((double) 268435456) - ((Math.log((1.0d + Math.sin((3.141592653589793d * lat) / 180.0d)) / (1.0d - Math.sin((3.141592653589793d * lat) / 180.0d))) * rad) / 2.0d)) - ((long) (AndroidUtilities.dp(10.3f) << 6)))) - ((double) 268435456)) / rad)))) * 180.0d) / 3.141592653589793d;
                url = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=%dx%d&maptype=roadmap&scale=%d&sensor=false", new Object[]{Double.valueOf(lat), Double.valueOf(lon), Integer.valueOf((int) (((float) photoWidth) / AndroidUtilities.density)), Integer.valueOf((int) (((float) photoHeight) / AndroidUtilities.density)), Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)))});
            } else if (TextUtils.isEmpty(object.messageOwner.media.title)) {
                url = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=200x100&maptype=roadmap&scale=%d&markers=color:red|size:mid|%f,%f&sensor=false", new Object[]{Double.valueOf(lat), Double.valueOf(lon), Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density))), Double.valueOf(lat), Double.valueOf(lon)});
            } else {
                url = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:mid|%f,%f&sensor=false", new Object[]{Double.valueOf(lat), Double.valueOf(lon), Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density))), Double.valueOf(lat), Double.valueOf(lon)});
            }
            if (!url.equals(this.currentUrl)) {
                return true;
            }
        } else if (this.currentPhotoObject == null || (this.currentPhotoObject.location instanceof TL_fileLocationUnavailable)) {
            return true;
        } else {
            if (this.currentMessageObject != null && this.photoNotSet && FileLoader.getPathToMessage(this.currentMessageObject.messageOwner).exists()) {
                return true;
            }
        }
        return false;
    }

    private boolean isUserDataChanged() {
        boolean z = false;
        if (this.currentMessageObject != null && !this.hasLinkPreview && this.currentMessageObject.messageOwner.media != null && (this.currentMessageObject.messageOwner.media.webpage instanceof TL_webPage)) {
            return true;
        }
        if (this.currentMessageObject == null || (this.currentUser == null && this.currentChat == null)) {
            return false;
        }
        if (this.lastSendState != this.currentMessageObject.messageOwner.send_state || this.lastDeleteDate != this.currentMessageObject.messageOwner.destroyTime || this.lastViewsCount != this.currentMessageObject.messageOwner.views) {
            return true;
        }
        updateCurrentUserAndChat();
        FileLocation newPhoto = null;
        if (this.isAvatarVisible) {
            if (this.currentUser != null && this.currentUser.photo != null) {
                newPhoto = this.currentUser.photo.photo_small;
            } else if (!(this.currentChat == null || this.currentChat.photo == null)) {
                newPhoto = this.currentChat.photo.photo_small;
            }
        }
        if (this.replyTextLayout == null && this.currentMessageObject.replyMessageObject != null) {
            return true;
        }
        if (this.currentPhoto == null && newPhoto != null) {
            return true;
        }
        if (this.currentPhoto != null && newPhoto == null) {
            return true;
        }
        if (this.currentPhoto != null && newPhoto != null && (this.currentPhoto.local_id != newPhoto.local_id || this.currentPhoto.volume_id != newPhoto.volume_id)) {
            return true;
        }
        FileLocation newReplyPhoto = null;
        if (this.replyNameLayout != null) {
            PhotoSize photoSize = FileLoader.getClosestPhotoSizeWithSize(this.currentMessageObject.replyMessageObject.photoThumbs, 80);
            if (!(photoSize == null || this.currentMessageObject.replyMessageObject.type == 13)) {
                newReplyPhoto = photoSize.location;
            }
        }
        if (this.currentReplyPhoto == null && newReplyPhoto != null) {
            return true;
        }
        String newNameString = null;
        if (this.drawName && this.isChat && !this.currentMessageObject.isOutOwner()) {
            if (this.currentUser != null) {
                newNameString = UserObject.getUserName(this.currentUser);
            } else if (this.currentChat != null) {
                newNameString = this.currentChat.title;
            }
        }
        if (this.currentNameString == null && newNameString != null) {
            return true;
        }
        if (this.currentNameString != null && newNameString == null) {
            return true;
        }
        if (this.currentNameString != null && newNameString != null && !this.currentNameString.equals(newNameString)) {
            return true;
        }
        if (!this.drawForwardedName) {
            return false;
        }
        newNameString = this.currentMessageObject.getForwardedName();
        if ((this.currentForwardNameString == null && newNameString != null) || ((this.currentForwardNameString != null && newNameString == null) || !(this.currentForwardNameString == null || newNameString == null || this.currentForwardNameString.equals(newNameString)))) {
            z = true;
        }
        return z;
    }

    public ImageReceiver getPhotoImage() {
        return this.photoImage;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.avatarImage.onDetachedFromWindow();
        this.replyImageReceiver.onDetachedFromWindow();
        this.locationImageReceiver.onDetachedFromWindow();
        this.photoImage.onDetachedFromWindow();
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
        this.avatarImage.setParentView((View) getParent());
        this.replyImageReceiver.onAttachedToWindow();
        this.locationImageReceiver.onAttachedToWindow();
        if (!this.drawPhotoImage) {
            updateButtonState(false);
        } else if (this.photoImage.onAttachedToWindow()) {
            updateButtonState(false);
        }
        if (this.currentMessageObject != null && this.currentMessageObject.isRoundVideo()) {
            checkRoundVideoPlayback(true);
        }
    }

    public void checkRoundVideoPlayback(boolean allowStart) {
        if (allowStart) {
            allowStart = MediaController.getInstance().getPlayingMessageObject() == null;
        }
        this.photoImage.setAllowStartAnimation(allowStart);
        if (allowStart) {
            this.photoImage.startAnimation();
        } else {
            this.photoImage.stopAnimation();
        }
    }

    protected void onLongPress() {
        if (this.pressedLink instanceof URLSpanMono) {
            this.delegate.didPressedUrl(this.currentMessageObject, this.pressedLink, true);
            return;
        }
        if (this.pressedLink instanceof URLSpanNoUnderline) {
            if (this.pressedLink.getURL().startsWith("/")) {
                this.delegate.didPressedUrl(this.currentMessageObject, this.pressedLink, true);
                return;
            }
        } else if (this.pressedLink instanceof URLSpan) {
            this.delegate.didPressedUrl(this.currentMessageObject, this.pressedLink, true);
            return;
        }
        resetPressedLink(-1);
        if (!(this.buttonPressed == 0 && this.pressedBotButton == -1)) {
            this.buttonPressed = 0;
            this.pressedBotButton = -1;
            invalidate();
        }
        if (this.instantPressed) {
            this.instantButtonPressed = false;
            this.instantPressed = false;
            if (VERSION.SDK_INT >= 21 && this.instantViewSelectorDrawable != null) {
                this.instantViewSelectorDrawable.setState(StateSet.NOTHING);
            }
            invalidate();
        }
        if (this.delegate != null) {
            this.delegate.didLongPressed(this);
        }
    }

    public void setCheckPressed(boolean value, boolean pressed) {
        this.isCheckPressed = value;
        this.isPressed = pressed;
        updateRadialProgressBackground(true);
        if (this.useSeekBarWaweform) {
            this.seekBarWaveform.setSelected(isDrawSelectedBackground());
        } else {
            this.seekBar.setSelected(isDrawSelectedBackground());
        }
        invalidate();
    }

    public void setHighlightedAnimated() {
        this.isHighlightedAnimated = true;
        this.highlightProgress = 1000;
        this.lastHighlightProgressTime = System.currentTimeMillis();
        invalidate();
    }

    public void setHighlighted(boolean value) {
        if (this.isHighlighted != value) {
            this.isHighlighted = value;
            if (!this.isHighlighted) {
                this.lastHighlightProgressTime = System.currentTimeMillis();
                this.isHighlightedAnimated = true;
                this.highlightProgress = 300;
            }
            updateRadialProgressBackground(true);
            if (this.useSeekBarWaweform) {
                this.seekBarWaveform.setSelected(isDrawSelectedBackground());
            } else {
                this.seekBar.setSelected(isDrawSelectedBackground());
            }
            invalidate();
        }
    }

    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        updateRadialProgressBackground(true);
        if (this.useSeekBarWaweform) {
            this.seekBarWaveform.setSelected(isDrawSelectedBackground());
        } else {
            this.seekBar.setSelected(isDrawSelectedBackground());
        }
        invalidate();
    }

    private void updateRadialProgressBackground(boolean swap) {
        if (!this.drawRadialCheckBackground && swap) {
            this.radialProgress.swapBackground(getDrawableForCurrentState());
        }
    }

    public void onSeekBarDrag(float progress) {
        if (this.currentMessageObject != null) {
            this.currentMessageObject.audioProgress = progress;
            MediaController.getInstance().seekToProgress(this.currentMessageObject, progress);
        }
    }

    private void updateWaveform() {
        if (this.currentMessageObject != null && this.documentAttachType == 3) {
            for (int a = 0; a < this.documentAttach.attributes.size(); a++) {
                DocumentAttribute attribute = (DocumentAttribute) this.documentAttach.attributes.get(a);
                if (attribute instanceof TL_documentAttributeAudio) {
                    if (attribute.waveform == null || attribute.waveform.length == 0) {
                        MediaController.getInstance().generateWaveform(this.currentMessageObject);
                    }
                    this.useSeekBarWaweform = attribute.waveform != null;
                    this.seekBarWaveform.setWaveform(attribute.waveform);
                    return;
                }
            }
        }
    }

    private int createDocumentLayout(int maxWidth, MessageObject messageObject) {
        if (messageObject.type == 0) {
            this.documentAttach = messageObject.messageOwner.media.webpage.document;
        } else {
            this.documentAttach = messageObject.messageOwner.media.document;
        }
        if (this.documentAttach == null) {
            return 0;
        }
        int duration;
        int a;
        DocumentAttribute attribute;
        if (MessageObject.isVoiceDocument(this.documentAttach)) {
            this.documentAttachType = 3;
            duration = 0;
            for (a = 0; a < this.documentAttach.attributes.size(); a++) {
                attribute = (DocumentAttribute) this.documentAttach.attributes.get(a);
                if (attribute instanceof TL_documentAttributeAudio) {
                    duration = attribute.duration;
                    break;
                }
            }
            this.widthBeforeNewTimeLine = (maxWidth - AndroidUtilities.dp(94.0f)) - ((int) Math.ceil((double) Theme.chat_audioTimePaint.measureText("00:00")));
            this.availableTimeWidth = maxWidth - AndroidUtilities.dp(18.0f);
            measureTime(messageObject);
            int minSize = AndroidUtilities.dp(174.0f) + this.timeWidth;
            if (!this.hasLinkPreview) {
                this.backgroundWidth = Math.min(maxWidth, (AndroidUtilities.dp(10.0f) * duration) + minSize);
            }
            this.seekBarWaveform.setMessageObject(messageObject);
            return 0;
        } else if (MessageObject.isMusicDocument(this.documentAttach)) {
            this.documentAttachType = 5;
            maxWidth -= AndroidUtilities.dp(86.0f);
            this.songLayout = new StaticLayout(TextUtils.ellipsize(messageObject.getMusicTitle().replace('\n', ' '), Theme.chat_audioTitlePaint, (float) (maxWidth - AndroidUtilities.dp(12.0f)), TruncateAt.END), Theme.chat_audioTitlePaint, maxWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (this.songLayout.getLineCount() > 0) {
                this.songX = -((int) Math.ceil((double) this.songLayout.getLineLeft(0)));
            }
            this.performerLayout = new StaticLayout(TextUtils.ellipsize(messageObject.getMusicAuthor().replace('\n', ' '), Theme.chat_audioPerformerPaint, (float) maxWidth, TruncateAt.END), Theme.chat_audioPerformerPaint, maxWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (this.performerLayout.getLineCount() > 0) {
                this.performerX = -((int) Math.ceil((double) this.performerLayout.getLineLeft(0)));
            }
            duration = 0;
            for (a = 0; a < this.documentAttach.attributes.size(); a++) {
                attribute = (DocumentAttribute) this.documentAttach.attributes.get(a);
                if (attribute instanceof TL_documentAttributeAudio) {
                    duration = attribute.duration;
                    break;
                }
            }
            int durationWidth = (int) Math.ceil((double) Theme.chat_audioTimePaint.measureText(String.format("%d:%02d / %d:%02d", new Object[]{Integer.valueOf(duration / 60), Integer.valueOf(duration % 60), Integer.valueOf(duration / 60), Integer.valueOf(duration % 60)})));
            this.widthBeforeNewTimeLine = (this.backgroundWidth - AndroidUtilities.dp(86.0f)) - durationWidth;
            this.availableTimeWidth = this.backgroundWidth - AndroidUtilities.dp(28.0f);
            return durationWidth;
        } else if (MessageObject.isVideoDocument(this.documentAttach)) {
            this.documentAttachType = 4;
            if (!messageObject.isSecretPhoto()) {
                duration = 0;
                for (a = 0; a < this.documentAttach.attributes.size(); a++) {
                    attribute = (DocumentAttribute) this.documentAttach.attributes.get(a);
                    if (attribute instanceof TL_documentAttributeVideo) {
                        duration = attribute.duration;
                        break;
                    }
                }
                int seconds = duration - ((duration / 60) * 60);
                str = String.format("%d:%02d, %s", new Object[]{Integer.valueOf(duration / 60), Integer.valueOf(seconds), AndroidUtilities.formatFileSize((long) this.documentAttach.size)});
                this.infoWidth = (int) Math.ceil((double) Theme.chat_infoPaint.measureText(str));
                this.infoLayout = new StaticLayout(str, Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            return 0;
        } else {
            int width;
            boolean z = (this.documentAttach.mime_type != null && this.documentAttach.mime_type.toLowerCase().startsWith("image/")) || ((this.documentAttach.thumb instanceof TL_photoSize) && !(this.documentAttach.thumb.location instanceof TL_fileLocationUnavailable));
            this.drawPhotoImage = z;
            if (!this.drawPhotoImage) {
                maxWidth += AndroidUtilities.dp(30.0f);
            }
            this.documentAttachType = 1;
            String name = FileLoader.getDocumentFileName(this.documentAttach);
            if (name == null || name.length() == 0) {
                name = LocaleController.getString("AttachDocument", R.string.AttachDocument);
            }
            this.docTitleLayout = StaticLayoutEx.createStaticLayout(name, Theme.chat_docNamePaint, maxWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TruncateAt.MIDDLE, maxWidth, this.drawPhotoImage ? 2 : 1);
            this.docTitleOffsetX = Integer.MIN_VALUE;
            if (this.docTitleLayout == null || this.docTitleLayout.getLineCount() <= 0) {
                width = maxWidth;
                this.docTitleOffsetX = 0;
            } else {
                int maxLineWidth = 0;
                for (a = 0; a < this.docTitleLayout.getLineCount(); a++) {
                    maxLineWidth = Math.max(maxLineWidth, (int) Math.ceil((double) this.docTitleLayout.getLineWidth(a)));
                    this.docTitleOffsetX = Math.max(this.docTitleOffsetX, (int) Math.ceil((double) (-this.docTitleLayout.getLineLeft(a))));
                }
                width = Math.min(maxWidth, maxLineWidth);
            }
            str = AndroidUtilities.formatFileSize((long) this.documentAttach.size) + " " + FileLoader.getDocumentExtension(this.documentAttach);
            this.infoWidth = Math.min(maxWidth - AndroidUtilities.dp(30.0f), (int) Math.ceil((double) Theme.chat_infoPaint.measureText(str)));
            CharSequence str2 = TextUtils.ellipsize(str, Theme.chat_infoPaint, (float) this.infoWidth, TruncateAt.END);
            try {
                if (this.infoWidth < 0) {
                    this.infoWidth = AndroidUtilities.dp(10.0f);
                }
                this.infoLayout = new StaticLayout(str2, Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (this.drawPhotoImage) {
                this.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(messageObject.photoThumbs, AndroidUtilities.getPhotoSize());
                this.photoImage.setNeedsQualityThumb(true);
                this.photoImage.setShouldGenerateQualityThumb(true);
                this.photoImage.setParentMessageObject(messageObject);
                if (this.currentPhotoObject != null) {
                    this.currentPhotoFilter = "86_86_b";
                    this.photoImage.setImage(null, null, null, null, this.currentPhotoObject.location, this.currentPhotoFilter, 0, null, 1);
                } else {
                    this.photoImage.setImageBitmap((BitmapDrawable) null);
                }
            }
            return width;
        }
    }

    private void calcBackgroundWidth(int maxWidth, int timeMore, int maxChildWidth) {
        if (this.hasLinkPreview || this.hasOldCaptionPreview || this.hasGamePreview || this.hasInvoicePreview || maxWidth - this.currentMessageObject.lastLineWidth < timeMore || this.currentMessageObject.hasRtl) {
            this.totalHeight += AndroidUtilities.dp(14.0f);
            this.hasNewLineForTime = true;
            this.backgroundWidth = Math.max(maxChildWidth, this.currentMessageObject.lastLineWidth) + AndroidUtilities.dp(31.0f);
            this.backgroundWidth = Math.max(this.backgroundWidth, this.timeWidth + AndroidUtilities.dp(31.0f));
            return;
        }
        int diff = maxChildWidth - this.currentMessageObject.lastLineWidth;
        if (diff < 0 || diff > timeMore) {
            this.backgroundWidth = Math.max(maxChildWidth, this.currentMessageObject.lastLineWidth + timeMore) + AndroidUtilities.dp(31.0f);
        } else {
            this.backgroundWidth = ((maxChildWidth + timeMore) - diff) + AndroidUtilities.dp(31.0f);
        }
    }

    public void setHighlightedText(String text) {
        if (this.currentMessageObject.messageOwner.message != null && this.currentMessageObject != null && this.currentMessageObject.type == 0 && !TextUtils.isEmpty(this.currentMessageObject.messageText) && text != null) {
            int start = TextUtils.indexOf(this.currentMessageObject.messageOwner.message.toLowerCase(), text.toLowerCase());
            if (start != -1) {
                int end = start + text.length();
                int c = 0;
                while (c < this.currentMessageObject.textLayoutBlocks.size()) {
                    TextLayoutBlock block = (TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(c);
                    if (start < block.charactersOffset || start >= block.charactersOffset + block.textLayout.getText().length()) {
                        c++;
                    } else {
                        this.linkSelectionBlockNum = c;
                        resetUrlPaths(true);
                        try {
                            LinkPath path = obtainNewUrlPath(true);
                            int length = block.textLayout.getText().length();
                            path.setCurrentLayout(block.textLayout, start, 0.0f);
                            block.textLayout.getSelectionPath(start, end - block.charactersOffset, path);
                            if (end >= block.charactersOffset + length) {
                                for (int a = c + 1; a < this.currentMessageObject.textLayoutBlocks.size(); a++) {
                                    TextLayoutBlock nextBlock = (TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(a);
                                    length = nextBlock.textLayout.getText().length();
                                    path = obtainNewUrlPath(true);
                                    path.setCurrentLayout(nextBlock.textLayout, 0, (float) nextBlock.height);
                                    nextBlock.textLayout.getSelectionPath(0, end - nextBlock.charactersOffset, path);
                                    if (end < (block.charactersOffset + length) - 1) {
                                        break;
                                    }
                                }
                            }
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        invalidate();
                        return;
                    }
                }
            } else if (!this.urlPathSelection.isEmpty()) {
                this.linkSelectionBlockNum = -1;
                resetUrlPaths(true);
                invalidate();
            }
        } else if (!this.urlPathSelection.isEmpty()) {
            this.linkSelectionBlockNum = -1;
            resetUrlPaths(true);
            invalidate();
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.instantViewSelectorDrawable;
    }

    private boolean isCurrentLocationTimeExpired(MessageObject messageObject) {
        if (this.currentMessageObject.messageOwner.media.period % 60 == 0) {
            if (Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - messageObject.messageOwner.date) > messageObject.messageOwner.media.period) {
                return true;
            }
            return false;
        } else if (Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - messageObject.messageOwner.date) <= messageObject.messageOwner.media.period - 5) {
            return false;
        } else {
            return true;
        }
    }

    private void checkLocationExpired() {
        if (this.currentMessageObject != null) {
            boolean newExpired = isCurrentLocationTimeExpired(this.currentMessageObject);
            if (newExpired != this.locationExpired) {
                this.locationExpired = newExpired;
                if (this.locationExpired) {
                    MessageObject messageObject = this.currentMessageObject;
                    this.currentMessageObject = null;
                    setMessageObject(messageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
                    return;
                }
                AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000);
                this.scheduledInvalidate = true;
                this.docTitleLayout = new StaticLayout(LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation), Theme.chat_locationTitlePaint, this.backgroundWidth - AndroidUtilities.dp(91.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setMessageObject(org.telegram.messenger.MessageObject r147, org.telegram.messenger.MessageObject.GroupedMessages r148, boolean r149, boolean r150) {
        /*
        r146 = this;
        r4 = r147.checkLayout();
        if (r4 != 0) goto L_0x0016;
    L_0x0006:
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 == 0) goto L_0x001b;
    L_0x000c:
        r0 = r146;
        r4 = r0.lastHeight;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        if (r4 == r6) goto L_0x001b;
    L_0x0016:
        r4 = 0;
        r0 = r146;
        r0.currentMessageObject = r4;
    L_0x001b:
        r0 = r146;
        r4 = r0.currentMessageObject;
        if (r4 == 0) goto L_0x002f;
    L_0x0021:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.getId();
        r6 = r147.getId();
        if (r4 == r6) goto L_0x07eb;
    L_0x002f:
        r101 = 1;
    L_0x0031:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r0 = r147;
        if (r4 != r0) goto L_0x003f;
    L_0x0039:
        r0 = r147;
        r4 = r0.forceUpdate;
        if (r4 == 0) goto L_0x07ef;
    L_0x003f:
        r100 = 1;
    L_0x0041:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r0 = r147;
        if (r4 != r0) goto L_0x07f3;
    L_0x0049:
        r4 = r146.isUserDataChanged();
        if (r4 != 0) goto L_0x0055;
    L_0x004f:
        r0 = r146;
        r4 = r0.photoNotSet;
        if (r4 == 0) goto L_0x07f3;
    L_0x0055:
        r64 = 1;
    L_0x0057:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        r0 = r148;
        if (r0 == r4) goto L_0x07f7;
    L_0x005f:
        r75 = 1;
    L_0x0061:
        if (r75 != 0) goto L_0x008a;
    L_0x0063:
        if (r148 == 0) goto L_0x008a;
    L_0x0065:
        r0 = r148;
        r4 = r0.messages;
        r4 = r4.size();
        r6 = 1;
        if (r4 <= r6) goto L_0x07fb;
    L_0x0070:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        r4 = r4.positions;
        r0 = r146;
        r6 = r0.currentMessageObject;
        r103 = r4.get(r6);
        r103 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r103;
    L_0x0080:
        r0 = r146;
        r4 = r0.currentPosition;
        r0 = r103;
        if (r0 == r4) goto L_0x07ff;
    L_0x0088:
        r75 = 1;
    L_0x008a:
        if (r100 != 0) goto L_0x00a6;
    L_0x008c:
        if (r64 != 0) goto L_0x00a6;
    L_0x008e:
        if (r75 != 0) goto L_0x00a6;
    L_0x0090:
        r4 = r146.isPhotoDataChanged(r147);
        if (r4 != 0) goto L_0x00a6;
    L_0x0096:
        r0 = r146;
        r4 = r0.pinnedBottom;
        r0 = r149;
        if (r4 != r0) goto L_0x00a6;
    L_0x009e:
        r0 = r146;
        r4 = r0.pinnedTop;
        r0 = r150;
        if (r4 == r0) goto L_0x37f6;
    L_0x00a6:
        r0 = r149;
        r1 = r146;
        r1.pinnedBottom = r0;
        r0 = r150;
        r1 = r146;
        r1.pinnedTop = r0;
        r4 = -2;
        r0 = r146;
        r0.lastTime = r4;
        r4 = 0;
        r0 = r146;
        r0.isHighlightedAnimated = r4;
        r4 = -1;
        r0 = r146;
        r0.widthBeforeNewTimeLine = r4;
        r0 = r147;
        r1 = r146;
        r1.currentMessageObject = r0;
        r0 = r148;
        r1 = r146;
        r1.currentMessagesGroup = r0;
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        if (r4 == 0) goto L_0x0803;
    L_0x00d3:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        r4 = r4.posArray;
        r4 = r4.size();
        r6 = 1;
        if (r4 <= r6) goto L_0x0803;
    L_0x00e0:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        r4 = r4.positions;
        r0 = r146;
        r6 = r0.currentMessageObject;
        r4 = r4.get(r6);
        r4 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r4;
        r0 = r146;
        r0.currentPosition = r4;
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 != 0) goto L_0x00ff;
    L_0x00fa:
        r4 = 0;
        r0 = r146;
        r0.currentMessagesGroup = r4;
    L_0x00ff:
        r0 = r146;
        r4 = r0.pinnedTop;
        if (r4 == 0) goto L_0x080f;
    L_0x0105:
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 == 0) goto L_0x0115;
    L_0x010b:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.flags;
        r4 = r4 & 4;
        if (r4 == 0) goto L_0x080f;
    L_0x0115:
        r4 = 1;
    L_0x0116:
        r0 = r146;
        r0.drawPinnedTop = r4;
        r0 = r146;
        r4 = r0.pinnedBottom;
        if (r4 == 0) goto L_0x0812;
    L_0x0120:
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 == 0) goto L_0x0130;
    L_0x0126:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.flags;
        r4 = r4 & 8;
        if (r4 == 0) goto L_0x0812;
    L_0x0130:
        r4 = 1;
    L_0x0131:
        r0 = r146;
        r0.drawPinnedBottom = r4;
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r4.setCrossfadeWithOldImage(r6);
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.send_state;
        r0 = r146;
        r0.lastSendState = r4;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.destroyTime;
        r0 = r146;
        r0.lastDeleteDate = r4;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.views;
        r0 = r146;
        r0.lastViewsCount = r4;
        r4 = 0;
        r0 = r146;
        r0.isPressed = r4;
        r4 = 0;
        r0 = r146;
        r0.gamePreviewPressed = r4;
        r4 = 1;
        r0 = r146;
        r0.isCheckPressed = r4;
        r4 = 0;
        r0 = r146;
        r0.hasNewLineForTime = r4;
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x0815;
    L_0x0175:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x0815;
    L_0x017b:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x0815;
    L_0x0181:
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 == 0) goto L_0x018f;
    L_0x0187:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.edge;
        if (r4 == 0) goto L_0x0815;
    L_0x018f:
        r4 = 1;
    L_0x0190:
        r0 = r146;
        r0.isAvatarVisible = r4;
        r4 = 0;
        r0 = r146;
        r0.wasLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.drwaShareGoIcon = r4;
        r4 = 0;
        r0 = r146;
        r0.groupPhotoInvisible = r4;
        r4 = r146.checkNeedDrawShareButton(r147);
        r0 = r146;
        r0.drawShareButton = r4;
        r4 = 0;
        r0 = r146;
        r0.replyNameLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.adminLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.replyTextLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.replyNameWidth = r4;
        r4 = 0;
        r0 = r146;
        r0.replyTextWidth = r4;
        r4 = 0;
        r0 = r146;
        r0.viaWidth = r4;
        r4 = 0;
        r0 = r146;
        r0.viaNameWidth = r4;
        r4 = 0;
        r0 = r146;
        r0.currentReplyPhoto = r4;
        r4 = 0;
        r0 = r146;
        r0.currentUser = r4;
        r4 = 0;
        r0 = r146;
        r0.currentChat = r4;
        r4 = 0;
        r0 = r146;
        r0.currentViaBotUser = r4;
        r4 = 0;
        r0 = r146;
        r0.drawNameLayout = r4;
        r0 = r146;
        r4 = r0.scheduledInvalidate;
        if (r4 == 0) goto L_0x01f9;
    L_0x01ed:
        r0 = r146;
        r4 = r0.invalidateRunnable;
        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r4);
        r4 = 0;
        r0 = r146;
        r0.scheduledInvalidate = r4;
    L_0x01f9:
        r4 = -1;
        r0 = r146;
        r0.resetPressedLink(r4);
        r4 = 0;
        r0 = r147;
        r0.forceUpdate = r4;
        r4 = 0;
        r0 = r146;
        r0.drawPhotoImage = r4;
        r4 = 0;
        r0 = r146;
        r0.hasLinkPreview = r4;
        r4 = 0;
        r0 = r146;
        r0.hasOldCaptionPreview = r4;
        r4 = 0;
        r0 = r146;
        r0.hasGamePreview = r4;
        r4 = 0;
        r0 = r146;
        r0.hasInvoicePreview = r4;
        r4 = 0;
        r0 = r146;
        r0.instantButtonPressed = r4;
        r0 = r146;
        r0.instantPressed = r4;
        r4 = android.os.Build.VERSION.SDK_INT;
        r6 = 21;
        if (r4 < r6) goto L_0x0244;
    L_0x022c:
        r0 = r146;
        r4 = r0.instantViewSelectorDrawable;
        if (r4 == 0) goto L_0x0244;
    L_0x0232:
        r0 = r146;
        r4 = r0.instantViewSelectorDrawable;
        r6 = 0;
        r8 = 0;
        r4.setVisible(r6, r8);
        r0 = r146;
        r4 = r0.instantViewSelectorDrawable;
        r6 = android.util.StateSet.NOTHING;
        r4.setState(r6);
    L_0x0244:
        r4 = 0;
        r0 = r146;
        r0.linkPreviewPressed = r4;
        r4 = 0;
        r0 = r146;
        r0.buttonPressed = r4;
        r4 = -1;
        r0 = r146;
        r0.pressedBotButton = r4;
        r4 = 0;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        r4 = 0;
        r0 = r146;
        r0.mediaOffsetY = r4;
        r4 = 0;
        r0 = r146;
        r0.documentAttachType = r4;
        r4 = 0;
        r0 = r146;
        r0.documentAttach = r4;
        r4 = 0;
        r0 = r146;
        r0.descriptionLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.titleLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.videoInfoLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.photosCountLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.siteNameLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.authorLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.captionLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.docTitleLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.drawImageButton = r4;
        r4 = 0;
        r0 = r146;
        r0.currentPhotoObject = r4;
        r4 = 0;
        r0 = r146;
        r0.currentPhotoObjectThumb = r4;
        r4 = 0;
        r0 = r146;
        r0.currentPhotoFilter = r4;
        r4 = 0;
        r0 = r146;
        r0.infoLayout = r4;
        r4 = 0;
        r0 = r146;
        r0.cancelLoading = r4;
        r4 = -1;
        r0 = r146;
        r0.buttonState = r4;
        r4 = 0;
        r0 = r146;
        r0.currentUrl = r4;
        r4 = 0;
        r0 = r146;
        r0.photoNotSet = r4;
        r4 = 1;
        r0 = r146;
        r0.drawBackground = r4;
        r4 = 0;
        r0 = r146;
        r0.drawName = r4;
        r4 = 0;
        r0 = r146;
        r0.useSeekBarWaweform = r4;
        r4 = 0;
        r0 = r146;
        r0.drawInstantView = r4;
        r4 = 0;
        r0 = r146;
        r0.drawInstantViewType = r4;
        r4 = 0;
        r0 = r146;
        r0.drawForwardedName = r4;
        r4 = 0;
        r0 = r146;
        r0.mediaBackground = r4;
        r57 = 0;
        r4 = 0;
        r0 = r146;
        r0.availableTimeWidth = r4;
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r4.setForceLoading(r6);
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r4.setNeedsQualityThumb(r6);
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r4.setShouldGenerateQualityThumb(r6);
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r4.setAllowDecodeSingleFrame(r6);
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r4.setParentMessageObject(r6);
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4.setRoundRadius(r6);
        if (r100 == 0) goto L_0x032c;
    L_0x031d:
        r4 = 0;
        r0 = r146;
        r0.firstVisibleBlockNum = r4;
        r4 = 0;
        r0 = r146;
        r0.lastVisibleBlockNum = r4;
        r4 = 1;
        r0 = r146;
        r0.needNewVisiblePart = r4;
    L_0x032c:
        r0 = r147;
        r4 = r0.type;
        if (r4 != 0) goto L_0x1c02;
    L_0x0332:
        r4 = 1;
        r0 = r146;
        r0.drawForwardedName = r4;
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x083d;
    L_0x033d:
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x0818;
    L_0x0343:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x0818;
    L_0x0349:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x0818;
    L_0x034f:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r6 = 1123287040; // 0x42f40000 float:122.0 double:5.54977537E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r26 = r4 - r6;
        r4 = 1;
        r0 = r146;
        r0.drawName = r4;
    L_0x0360:
        r0 = r26;
        r1 = r146;
        r1.availableTimeWidth = r0;
        r146.measureTime(r147);
        r0 = r146;
        r4 = r0.timeWidth;
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r132 = r4 + r6;
        r4 = r147.isOutOwner();
        if (r4 == 0) goto L_0x0383;
    L_0x037b:
        r4 = 1101266944; // 0x41a40000 float:20.5 double:5.44098164E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r132 = r132 + r4;
    L_0x0383:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r4 == 0) goto L_0x0897;
    L_0x038d:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.game;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_game;
        if (r4 == 0) goto L_0x0897;
    L_0x0399:
        r4 = 1;
    L_0x039a:
        r0 = r146;
        r0.hasGamePreview = r4;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        r0 = r146;
        r0.hasInvoicePreview = r4;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r4 == 0) goto L_0x089a;
    L_0x03b4:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_webPage;
        if (r4 == 0) goto L_0x089a;
    L_0x03c0:
        r4 = 1;
    L_0x03c1:
        r0 = r146;
        r0.hasLinkPreview = r4;
        r0 = r146;
        r4 = r0.hasLinkPreview;
        if (r4 == 0) goto L_0x089d;
    L_0x03cb:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4.cached_page;
        if (r4 == 0) goto L_0x089d;
    L_0x03d7:
        r4 = 1;
    L_0x03d8:
        r0 = r146;
        r0.drawInstantView = r4;
        r126 = 0;
        r0 = r146;
        r4 = r0.hasLinkPreview;
        if (r4 == 0) goto L_0x08a0;
    L_0x03e4:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r0 = r4.site_name;
        r125 = r0;
    L_0x03f0:
        r0 = r146;
        r4 = r0.hasLinkPreview;
        if (r4 == 0) goto L_0x08a4;
    L_0x03f6:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r0 = r4.type;
        r142 = r0;
    L_0x0402:
        r0 = r146;
        r4 = r0.drawInstantView;
        if (r4 != 0) goto L_0x08d6;
    L_0x0408:
        r4 = "telegram_channel";
        r0 = r142;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x08a8;
    L_0x0413:
        r4 = 1;
        r0 = r146;
        r0.drawInstantView = r4;
        r4 = 1;
        r0 = r146;
        r0.drawInstantViewType = r4;
    L_0x041d:
        r4 = android.os.Build.VERSION.SDK_INT;
        r6 = 21;
        if (r4 < r6) goto L_0x0492;
    L_0x0423:
        r0 = r146;
        r4 = r0.drawInstantView;
        if (r4 == 0) goto L_0x0492;
    L_0x0429:
        r0 = r146;
        r4 = r0.instantViewSelectorDrawable;
        if (r4 != 0) goto L_0x09c4;
    L_0x042f:
        r92 = new android.graphics.Paint;
        r4 = 1;
        r0 = r92;
        r0.<init>(r4);
        r4 = -1;
        r0 = r92;
        r0.setColor(r4);
        r89 = new org.telegram.ui.Cells.ChatMessageCell$2;
        r0 = r89;
        r1 = r146;
        r2 = r92;
        r0.<init>(r2);
        r59 = new android.content.res.ColorStateList;
        r4 = 1;
        r6 = new int[r4][];
        r4 = 0;
        r8 = android.util.StateSet.WILD_CARD;
        r6[r4] = r8;
        r4 = 1;
        r8 = new int[r4];
        r9 = 0;
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x09bf;
    L_0x0460:
        r4 = "chat_outPreviewInstantText";
    L_0x0463:
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r10 = 1610612735; // 0x5fffffff float:3.6893486E19 double:7.95748421E-315;
        r4 = r4 & r10;
        r8[r9] = r4;
        r0 = r59;
        r0.<init>(r6, r8);
        r4 = new android.graphics.drawable.RippleDrawable;
        r6 = 0;
        r0 = r59;
        r1 = r89;
        r4.<init>(r0, r6, r1);
        r0 = r146;
        r0.instantViewSelectorDrawable = r4;
        r0 = r146;
        r4 = r0.instantViewSelectorDrawable;
        r0 = r146;
        r4.setCallback(r0);
    L_0x0489:
        r0 = r146;
        r4 = r0.instantViewSelectorDrawable;
        r6 = 1;
        r8 = 0;
        r4.setVisible(r6, r8);
    L_0x0492:
        r0 = r26;
        r1 = r146;
        r1.backgroundWidth = r0;
        r0 = r146;
        r4 = r0.hasLinkPreview;
        if (r4 != 0) goto L_0x04b4;
    L_0x049e:
        r0 = r146;
        r4 = r0.hasGamePreview;
        if (r4 != 0) goto L_0x04b4;
    L_0x04a4:
        r0 = r146;
        r4 = r0.hasInvoicePreview;
        if (r4 != 0) goto L_0x04b4;
    L_0x04aa:
        r0 = r147;
        r4 = r0.lastLineWidth;
        r4 = r26 - r4;
        r0 = r132;
        if (r4 >= r0) goto L_0x09e7;
    L_0x04b4:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r0 = r147;
        r6 = r0.lastLineWidth;
        r4 = java.lang.Math.max(r4, r6);
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.backgroundWidth = r4;
        r0 = r146;
        r4 = r0.backgroundWidth;
        r0 = r146;
        r6 = r0.timeWidth;
        r8 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r6 = r6 + r8;
        r4 = java.lang.Math.max(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x04e2:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.availableTimeWidth = r4;
        r146.setMessageObjectInternal(r147);
        r0 = r147;
        r6 = r0.textWidth;
        r0 = r146;
        r4 = r0.hasGamePreview;
        if (r4 != 0) goto L_0x0504;
    L_0x04fe:
        r0 = r146;
        r4 = r0.hasInvoicePreview;
        if (r4 == 0) goto L_0x0a29;
    L_0x0504:
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
    L_0x050a:
        r4 = r4 + r6;
        r0 = r146;
        r0.backgroundWidth = r4;
        r0 = r147;
        r4 = r0.textHeight;
        r6 = 1100742656; // 0x419c0000 float:19.5 double:5.43839131E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r6 = r0.namesOffset;
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.drawPinnedTop;
        if (r4 == 0) goto L_0x0538;
    L_0x0529:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.namesOffset = r4;
    L_0x0538:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r0 = r146;
        r6 = r0.nameWidth;
        r95 = java.lang.Math.max(r4, r6);
        r0 = r146;
        r4 = r0.forwardedNameWidth;
        r0 = r95;
        r95 = java.lang.Math.max(r0, r4);
        r0 = r146;
        r4 = r0.replyNameWidth;
        r0 = r95;
        r95 = java.lang.Math.max(r0, r4);
        r0 = r146;
        r4 = r0.replyTextWidth;
        r0 = r95;
        r95 = java.lang.Math.max(r0, r4);
        r98 = 0;
        r0 = r146;
        r4 = r0.hasLinkPreview;
        if (r4 != 0) goto L_0x0576;
    L_0x056a:
        r0 = r146;
        r4 = r0.hasGamePreview;
        if (r4 != 0) goto L_0x0576;
    L_0x0570:
        r0 = r146;
        r4 = r0.hasInvoicePreview;
        if (r4 == 0) goto L_0x1beb;
    L_0x0576:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x0a3a;
    L_0x057c:
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x0a2c;
    L_0x0582:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x0a2c;
    L_0x0588:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.isOut();
        if (r4 != 0) goto L_0x0a2c;
    L_0x0592:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r6 = 1124335616; // 0x43040000 float:132.0 double:5.554956023E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r87 = r4 - r6;
    L_0x059e:
        r0 = r146;
        r4 = r0.drawShareButton;
        if (r4 == 0) goto L_0x05ac;
    L_0x05a4:
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r87 = r87 - r4;
    L_0x05ac:
        r0 = r146;
        r4 = r0.hasLinkPreview;
        if (r4 == 0) goto L_0x0a83;
    L_0x05b2:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r0 = r4.webpage;
        r141 = r0;
        r141 = (org.telegram.tgnet.TLRPC.TL_webPage) r141;
        r0 = r141;
        r7 = r0.site_name;
        r0 = r141;
        r0 = r0.title;
        r134 = r0;
        r0 = r141;
        r0 = r0.author;
        r47 = r0;
        r0 = r141;
        r0 = r0.description;
        r65 = r0;
        r0 = r141;
        r0 = r0.photo;
        r110 = r0;
        r14 = 0;
        r0 = r141;
        r0 = r0.document;
        r67 = r0;
        r0 = r141;
        r0 = r0.type;
        r136 = r0;
        r0 = r141;
        r0 = r0.duration;
        r68 = r0;
        if (r7 == 0) goto L_0x060e;
    L_0x05ef:
        if (r110 == 0) goto L_0x060e;
    L_0x05f1:
        r4 = r7.toLowerCase();
        r6 = "instagram";
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x060e;
    L_0x05fe:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.y;
        r4 = r4 / 3;
        r0 = r146;
        r6 = r0.currentMessageObject;
        r6 = r6.textWidth;
        r87 = java.lang.Math.max(r4, r6);
    L_0x060e:
        if (r126 != 0) goto L_0x0a7c;
    L_0x0610:
        r0 = r146;
        r4 = r0.drawInstantView;
        if (r4 != 0) goto L_0x0a7c;
    L_0x0616:
        if (r67 != 0) goto L_0x0a7c;
    L_0x0618:
        if (r136 == 0) goto L_0x0a7c;
    L_0x061a:
        r4 = "app";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x063b;
    L_0x0625:
        r4 = "profile";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x063b;
    L_0x0630:
        r4 = "article";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0a7c;
    L_0x063b:
        r127 = 1;
    L_0x063d:
        if (r126 != 0) goto L_0x0a80;
    L_0x063f:
        r0 = r146;
        r4 = r0.drawInstantView;
        if (r4 != 0) goto L_0x0a80;
    L_0x0645:
        if (r67 != 0) goto L_0x0a80;
    L_0x0647:
        if (r65 == 0) goto L_0x0a80;
    L_0x0649:
        if (r136 == 0) goto L_0x0a80;
    L_0x064b:
        r4 = "app";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x066c;
    L_0x0656:
        r4 = "profile";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x066c;
    L_0x0661:
        r4 = "article";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0a80;
    L_0x066c:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.photoThumbs;
        if (r4 == 0) goto L_0x0a80;
    L_0x0674:
        r4 = 1;
    L_0x0675:
        r0 = r146;
        r0.isSmallImage = r4;
        r140 = r14;
    L_0x067b:
        r0 = r146;
        r4 = r0.hasInvoicePreview;
        if (r4 == 0) goto L_0x0af7;
    L_0x0681:
        r42 = 0;
    L_0x0683:
        r119 = 3;
        r44 = 0;
        r87 = r87 - r42;
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.photoThumbs;
        if (r4 != 0) goto L_0x069b;
    L_0x0691:
        if (r110 == 0) goto L_0x069b;
    L_0x0693:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r6 = 1;
        r4.generateThumbs(r6);
    L_0x069b:
        if (r7 == 0) goto L_0x0704;
    L_0x069d:
        r4 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x0aff }
        r4 = r4.measureText(r7);	 Catch:{ Exception -> 0x0aff }
        r8 = (double) r4;	 Catch:{ Exception -> 0x0aff }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x0aff }
        r0 = (int) r8;	 Catch:{ Exception -> 0x0aff }
        r143 = r0;
        r6 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0aff }
        r8 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x0aff }
        r0 = r143;
        r1 = r87;
        r9 = java.lang.Math.min(r0, r1);	 Catch:{ Exception -> 0x0aff }
        r10 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0aff }
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r12 = 0;
        r13 = 0;
        r6.<init>(r7, r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x0aff }
        r0 = r146;
        r0.siteNameLayout = r6;	 Catch:{ Exception -> 0x0aff }
        r0 = r146;
        r4 = r0.siteNameLayout;	 Catch:{ Exception -> 0x0aff }
        r0 = r146;
        r6 = r0.siteNameLayout;	 Catch:{ Exception -> 0x0aff }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x0aff }
        r6 = r6 + -1;
        r79 = r4.getLineBottom(r6);	 Catch:{ Exception -> 0x0aff }
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x0aff }
        r4 = r4 + r79;
        r0 = r146;
        r0.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x0aff }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x0aff }
        r4 = r4 + r79;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x0aff }
        r44 = r44 + r79;
        r0 = r146;
        r4 = r0.siteNameLayout;	 Catch:{ Exception -> 0x0aff }
        r143 = r4.getWidth();	 Catch:{ Exception -> 0x0aff }
        r4 = r143 + r42;
        r0 = r95;
        r95 = java.lang.Math.max(r0, r4);	 Catch:{ Exception -> 0x0aff }
        r4 = r143 + r42;
        r0 = r98;
        r98 = java.lang.Math.max(r0, r4);	 Catch:{ Exception -> 0x0aff }
    L_0x0704:
        r135 = 0;
        if (r134 == 0) goto L_0x0b43;
    L_0x0708:
        r4 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r0 = r146;
        r0.titleX = r4;	 Catch:{ Exception -> 0x384f }
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x384f }
        if (r4 == 0) goto L_0x0733;
    L_0x0715:
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x384f }
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x384f }
        r4 = r4 + r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x384f }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x384f }
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x384f }
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x384f }
    L_0x0733:
        r118 = 0;
        r0 = r146;
        r4 = r0.isSmallImage;	 Catch:{ Exception -> 0x384f }
        if (r4 == 0) goto L_0x073d;
    L_0x073b:
        if (r65 != 0) goto L_0x0b05;
    L_0x073d:
        r9 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x384f }
        r11 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x384f }
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x384f }
        r13 = (float) r4;	 Catch:{ Exception -> 0x384f }
        r14 = 0;
        r15 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x384f }
        r17 = 4;
        r8 = r134;
        r10 = r87;
        r16 = r87;
        r4 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);	 Catch:{ Exception -> 0x384f }
        r0 = r146;
        r0.titleLayout = r4;	 Catch:{ Exception -> 0x384f }
        r12 = r119;
    L_0x075f:
        r0 = r146;
        r4 = r0.titleLayout;	 Catch:{ Exception -> 0x0b3d }
        r0 = r146;
        r6 = r0.titleLayout;	 Catch:{ Exception -> 0x0b3d }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x0b3d }
        r6 = r6 + -1;
        r79 = r4.getLineBottom(r6);	 Catch:{ Exception -> 0x0b3d }
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x0b3d }
        r4 = r4 + r79;
        r0 = r146;
        r0.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x0b3d }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x0b3d }
        r4 = r4 + r79;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x0b3d }
        r58 = 1;
        r41 = 0;
    L_0x0789:
        r0 = r146;
        r4 = r0.titleLayout;	 Catch:{ Exception -> 0x0b3d }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x0b3d }
        r0 = r41;
        if (r0 >= r4) goto L_0x0cb7;
    L_0x0795:
        r0 = r146;
        r4 = r0.titleLayout;	 Catch:{ Exception -> 0x0b3d }
        r0 = r41;
        r4 = r4.getLineLeft(r0);	 Catch:{ Exception -> 0x0b3d }
        r0 = (int) r4;	 Catch:{ Exception -> 0x0b3d }
        r86 = r0;
        if (r86 == 0) goto L_0x07a6;
    L_0x07a4:
        r135 = 1;
    L_0x07a6:
        r0 = r146;
        r4 = r0.titleX;	 Catch:{ Exception -> 0x0b3d }
        r6 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r4 != r6) goto L_0x0b2c;
    L_0x07af:
        r0 = r86;
        r4 = -r0;
        r0 = r146;
        r0.titleX = r4;	 Catch:{ Exception -> 0x0b3d }
    L_0x07b6:
        if (r86 == 0) goto L_0x0ca3;
    L_0x07b8:
        r0 = r146;
        r4 = r0.titleLayout;	 Catch:{ Exception -> 0x0b3d }
        r4 = r4.getWidth();	 Catch:{ Exception -> 0x0b3d }
        r143 = r4 - r86;
    L_0x07c2:
        r0 = r41;
        r1 = r118;
        if (r0 < r1) goto L_0x07d0;
    L_0x07c8:
        if (r86 == 0) goto L_0x07d8;
    L_0x07ca:
        r0 = r146;
        r4 = r0.isSmallImage;	 Catch:{ Exception -> 0x0b3d }
        if (r4 == 0) goto L_0x07d8;
    L_0x07d0:
        r4 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x0b3d }
        r143 = r143 + r4;
    L_0x07d8:
        r4 = r143 + r42;
        r0 = r95;
        r95 = java.lang.Math.max(r0, r4);	 Catch:{ Exception -> 0x0b3d }
        r4 = r143 + r42;
        r0 = r98;
        r98 = java.lang.Math.max(r0, r4);	 Catch:{ Exception -> 0x0b3d }
        r41 = r41 + 1;
        goto L_0x0789;
    L_0x07eb:
        r101 = 0;
        goto L_0x0031;
    L_0x07ef:
        r100 = 0;
        goto L_0x0041;
    L_0x07f3:
        r64 = 0;
        goto L_0x0057;
    L_0x07f7:
        r75 = 0;
        goto L_0x0061;
    L_0x07fb:
        r103 = 0;
        goto L_0x0080;
    L_0x07ff:
        r75 = 0;
        goto L_0x008a;
    L_0x0803:
        r4 = 0;
        r0 = r146;
        r0.currentMessagesGroup = r4;
        r4 = 0;
        r0 = r146;
        r0.currentPosition = r4;
        goto L_0x00ff;
    L_0x080f:
        r4 = 0;
        goto L_0x0116;
    L_0x0812:
        r4 = 0;
        goto L_0x0131;
    L_0x0815:
        r4 = 0;
        goto L_0x0190;
    L_0x0818:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.to_id;
        r4 = r4.channel_id;
        if (r4 == 0) goto L_0x083b;
    L_0x0822:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x083b;
    L_0x0828:
        r4 = 1;
    L_0x0829:
        r0 = r146;
        r0.drawName = r4;
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r6 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r26 = r4 - r6;
        goto L_0x0360;
    L_0x083b:
        r4 = 0;
        goto L_0x0829;
    L_0x083d:
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x086a;
    L_0x0843:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x086a;
    L_0x0849:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x086a;
    L_0x084f:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r4 = java.lang.Math.min(r4, r6);
        r6 = 1123287040; // 0x42f40000 float:122.0 double:5.54977537E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r26 = r4 - r6;
        r4 = 1;
        r0 = r146;
        r0.drawName = r4;
        goto L_0x0360;
    L_0x086a:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r4 = java.lang.Math.min(r4, r6);
        r6 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r26 = r4 - r6;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.to_id;
        r4 = r4.channel_id;
        if (r4 == 0) goto L_0x0895;
    L_0x0888:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x0895;
    L_0x088e:
        r4 = 1;
    L_0x088f:
        r0 = r146;
        r0.drawName = r4;
        goto L_0x0360;
    L_0x0895:
        r4 = 0;
        goto L_0x088f;
    L_0x0897:
        r4 = 0;
        goto L_0x039a;
    L_0x089a:
        r4 = 0;
        goto L_0x03c1;
    L_0x089d:
        r4 = 0;
        goto L_0x03d8;
    L_0x08a0:
        r125 = 0;
        goto L_0x03f0;
    L_0x08a4:
        r142 = 0;
        goto L_0x0402;
    L_0x08a8:
        r4 = "telegram_megagroup";
        r0 = r142;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x08bf;
    L_0x08b3:
        r4 = 1;
        r0 = r146;
        r0.drawInstantView = r4;
        r4 = 2;
        r0 = r146;
        r0.drawInstantViewType = r4;
        goto L_0x041d;
    L_0x08bf:
        r4 = "telegram_message";
        r0 = r142;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x041d;
    L_0x08ca:
        r4 = 1;
        r0 = r146;
        r0.drawInstantView = r4;
        r4 = 3;
        r0 = r146;
        r0.drawInstantViewType = r4;
        goto L_0x041d;
    L_0x08d6:
        if (r125 == 0) goto L_0x041d;
    L_0x08d8:
        r125 = r125.toLowerCase();
        r4 = "instagram";
        r0 = r125;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x08fd;
    L_0x08e7:
        r4 = "twitter";
        r0 = r125;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x08fd;
    L_0x08f2:
        r4 = "telegram_album";
        r0 = r142;
        r4 = r4.equals(r0);
        if (r4 == 0) goto L_0x041d;
    L_0x08fd:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4.cached_page;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_pageFull;
        if (r4 == 0) goto L_0x041d;
    L_0x090b:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4.photo;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_photo;
        if (r4 != 0) goto L_0x0929;
    L_0x0919:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4.document;
        r4 = org.telegram.messenger.MessageObject.isVideoDocument(r4);
        if (r4 == 0) goto L_0x041d;
    L_0x0929:
        r4 = 0;
        r0 = r146;
        r0.drawInstantView = r4;
        r126 = 1;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4.cached_page;
        r0 = r4.blocks;
        r52 = r0;
        r60 = 1;
        r41 = 0;
    L_0x0942:
        r4 = r52.size();
        r0 = r41;
        if (r0 >= r4) goto L_0x097c;
    L_0x094a:
        r0 = r52;
        r1 = r41;
        r51 = r0.get(r1);
        r51 = (org.telegram.tgnet.TLRPC.PageBlock) r51;
        r0 = r51;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow;
        if (r4 == 0) goto L_0x0969;
    L_0x095a:
        r50 = r51;
        r50 = (org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow) r50;
        r0 = r50;
        r4 = r0.items;
        r60 = r4.size();
    L_0x0966:
        r41 = r41 + 1;
        goto L_0x0942;
    L_0x0969:
        r0 = r51;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockCollage;
        if (r4 == 0) goto L_0x0966;
    L_0x096f:
        r50 = r51;
        r50 = (org.telegram.tgnet.TLRPC.TL_pageBlockCollage) r50;
        r0 = r50;
        r4 = r0.items;
        r60 = r4.size();
        goto L_0x0966;
    L_0x097c:
        r4 = "Of";
        r6 = 2131493978; // 0x7f0c045a float:1.8611451E38 double:1.053097949E-314;
        r8 = 2;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r10 = 1;
        r10 = java.lang.Integer.valueOf(r10);
        r8[r9] = r10;
        r9 = 1;
        r10 = java.lang.Integer.valueOf(r60);
        r8[r9] = r10;
        r5 = org.telegram.messenger.LocaleController.formatString(r4, r6, r8);
        r4 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r4 = r4.measureText(r5);
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
        r4 = (int) r8;
        r0 = r146;
        r0.photosCountWidth = r4;
        r4 = new android.text.StaticLayout;
        r6 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r0 = r146;
        r7 = r0.photosCountWidth;
        r8 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r10 = 0;
        r11 = 0;
        r4.<init>(r5, r6, r7, r8, r9, r10, r11);
        r0 = r146;
        r0.photosCountLayout = r4;
        goto L_0x041d;
    L_0x09bf:
        r4 = "chat_inPreviewInstantText";
        goto L_0x0463;
    L_0x09c4:
        r0 = r146;
        r6 = r0.instantViewSelectorDrawable;
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x09e3;
    L_0x09d2:
        r4 = "chat_outPreviewInstantText";
    L_0x09d5:
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r8 = 1610612735; // 0x5fffffff float:3.6893486E19 double:7.95748421E-315;
        r4 = r4 & r8;
        r8 = 1;
        org.telegram.ui.ActionBar.Theme.setSelectorDrawableColor(r6, r4, r8);
        goto L_0x0489;
    L_0x09e3:
        r4 = "chat_inPreviewInstantText";
        goto L_0x09d5;
    L_0x09e7:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r0 = r147;
        r6 = r0.lastLineWidth;
        r66 = r4 - r6;
        if (r66 < 0) goto L_0x0a0e;
    L_0x09f3:
        r0 = r66;
        r1 = r132;
        if (r0 > r1) goto L_0x0a0e;
    L_0x09f9:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r4 = r4 + r132;
        r4 = r4 - r66;
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x04e2;
    L_0x0a0e:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r0 = r147;
        r6 = r0.lastLineWidth;
        r6 = r6 + r132;
        r4 = java.lang.Math.max(r4, r6);
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x04e2;
    L_0x0a29:
        r4 = 0;
        goto L_0x050a;
    L_0x0a2c:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r6 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r87 = r4 - r6;
        goto L_0x059e;
    L_0x0a3a:
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x0a66;
    L_0x0a40:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x0a66;
    L_0x0a46:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 != 0) goto L_0x0a66;
    L_0x0a50:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r4 = java.lang.Math.min(r4, r6);
        r6 = 1124335616; // 0x43040000 float:132.0 double:5.554956023E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r87 = r4 - r6;
        goto L_0x059e;
    L_0x0a66:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r4 = java.lang.Math.min(r4, r6);
        r6 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r87 = r4 - r6;
        goto L_0x059e;
    L_0x0a7c:
        r127 = 0;
        goto L_0x063d;
    L_0x0a80:
        r4 = 0;
        goto L_0x0675;
    L_0x0a83:
        r0 = r146;
        r4 = r0.hasInvoicePreview;
        if (r4 == 0) goto L_0x0ab5;
    L_0x0a89:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r7 = r4.title;
        r134 = 0;
        r65 = 0;
        r110 = 0;
        r47 = 0;
        r67 = 0;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = (org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) r4;
        r14 = r4.photo;
        r68 = 0;
        r136 = "invoice";
        r4 = 0;
        r0 = r146;
        r0.isSmallImage = r4;
        r127 = 0;
        r140 = r14;
        goto L_0x067b;
    L_0x0ab5:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r0 = r4.game;
        r74 = r0;
        r0 = r74;
        r7 = r0.title;
        r134 = 0;
        r14 = 0;
        r0 = r147;
        r4 = r0.messageText;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x0af4;
    L_0x0ad0:
        r0 = r74;
        r0 = r0.description;
        r65 = r0;
    L_0x0ad6:
        r0 = r74;
        r0 = r0.photo;
        r110 = r0;
        r47 = 0;
        r0 = r74;
        r0 = r0.document;
        r67 = r0;
        r68 = 0;
        r136 = "game";
        r4 = 0;
        r0 = r146;
        r0.isSmallImage = r4;
        r127 = 0;
        r140 = r14;
        goto L_0x067b;
    L_0x0af4:
        r65 = 0;
        goto L_0x0ad6;
    L_0x0af7:
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r42 = org.telegram.messenger.AndroidUtilities.dp(r4);
        goto L_0x0683;
    L_0x0aff:
        r70 = move-exception;
        org.telegram.messenger.FileLog.e(r70);
        goto L_0x0704;
    L_0x0b05:
        r118 = r119;
        r9 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x384f }
        r4 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x384f }
        r11 = r87 - r4;
        r13 = 4;
        r8 = r134;
        r10 = r87;
        r12 = r119;
        r4 = generateStaticLayout(r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x384f }
        r0 = r146;
        r0.titleLayout = r4;	 Catch:{ Exception -> 0x384f }
        r0 = r146;
        r4 = r0.titleLayout;	 Catch:{ Exception -> 0x384f }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x384f }
        r12 = r119 - r4;
        goto L_0x075f;
    L_0x0b2c:
        r0 = r146;
        r4 = r0.titleX;	 Catch:{ Exception -> 0x0b3d }
        r0 = r86;
        r6 = -r0;
        r4 = java.lang.Math.max(r4, r6);	 Catch:{ Exception -> 0x0b3d }
        r0 = r146;
        r0.titleX = r4;	 Catch:{ Exception -> 0x0b3d }
        goto L_0x07b6;
    L_0x0b3d:
        r70 = move-exception;
    L_0x0b3e:
        org.telegram.messenger.FileLog.e(r70);
        r119 = r12;
    L_0x0b43:
        r48 = 0;
        if (r47 == 0) goto L_0x3864;
    L_0x0b47:
        if (r134 != 0) goto L_0x3864;
    L_0x0b49:
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x0cf3 }
        if (r4 == 0) goto L_0x0b6d;
    L_0x0b4f:
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x0cf3 }
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x0cf3 }
        r4 = r4 + r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x0cf3 }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x0cf3 }
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x0cf3 }
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x0cf3 }
    L_0x0b6d:
        r4 = 3;
        r0 = r119;
        if (r0 != r4) goto L_0x0cbb;
    L_0x0b72:
        r0 = r146;
        r4 = r0.isSmallImage;	 Catch:{ Exception -> 0x0cf3 }
        if (r4 == 0) goto L_0x0b7a;
    L_0x0b78:
        if (r65 != 0) goto L_0x0cbb;
    L_0x0b7a:
        r8 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0cf3 }
        r10 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x0cf3 }
        r12 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0cf3 }
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r14 = 0;
        r15 = 0;
        r9 = r47;
        r11 = r87;
        r8.<init>(r9, r10, r11, r12, r13, r14, r15);	 Catch:{ Exception -> 0x0cf3 }
        r0 = r146;
        r0.authorLayout = r8;	 Catch:{ Exception -> 0x0cf3 }
        r12 = r119;
    L_0x0b91:
        r0 = r146;
        r4 = r0.authorLayout;	 Catch:{ Exception -> 0x384c }
        r0 = r146;
        r6 = r0.authorLayout;	 Catch:{ Exception -> 0x384c }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x384c }
        r6 = r6 + -1;
        r79 = r4.getLineBottom(r6);	 Catch:{ Exception -> 0x384c }
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x384c }
        r4 = r4 + r79;
        r0 = r146;
        r0.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x384c }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x384c }
        r4 = r4 + r79;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x384c }
        r0 = r146;
        r4 = r0.authorLayout;	 Catch:{ Exception -> 0x384c }
        r6 = 0;
        r4 = r4.getLineLeft(r6);	 Catch:{ Exception -> 0x384c }
        r0 = (int) r4;	 Catch:{ Exception -> 0x384c }
        r86 = r0;
        r0 = r86;
        r4 = -r0;
        r0 = r146;
        r0.authorX = r4;	 Catch:{ Exception -> 0x384c }
        if (r86 == 0) goto L_0x0ce0;
    L_0x0bcc:
        r0 = r146;
        r4 = r0.authorLayout;	 Catch:{ Exception -> 0x384c }
        r4 = r4.getWidth();	 Catch:{ Exception -> 0x384c }
        r143 = r4 - r86;
        r48 = 1;
    L_0x0bd8:
        r4 = r143 + r42;
        r0 = r95;
        r95 = java.lang.Math.max(r0, r4);	 Catch:{ Exception -> 0x384c }
        r4 = r143 + r42;
        r0 = r98;
        r98 = java.lang.Math.max(r0, r4);	 Catch:{ Exception -> 0x384c }
    L_0x0be8:
        if (r65 == 0) goto L_0x0d1c;
    L_0x0bea:
        r4 = 0;
        r0 = r146;
        r0.descriptionX = r4;	 Catch:{ Exception -> 0x0d18 }
        r0 = r146;
        r4 = r0.currentMessageObject;	 Catch:{ Exception -> 0x0d18 }
        r4.generateLinkDescription();	 Catch:{ Exception -> 0x0d18 }
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x0d18 }
        if (r4 == 0) goto L_0x0c1a;
    L_0x0bfc:
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x0d18 }
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x0d18 }
        r4 = r4 + r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x0d18 }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x0d18 }
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x0d18 }
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x0d18 }
    L_0x0c1a:
        r118 = 0;
        r4 = 3;
        if (r12 != r4) goto L_0x0cfb;
    L_0x0c1f:
        r0 = r146;
        r4 = r0.isSmallImage;	 Catch:{ Exception -> 0x0d18 }
        if (r4 != 0) goto L_0x0cfb;
    L_0x0c25:
        r0 = r147;
        r8 = r0.linkDescription;	 Catch:{ Exception -> 0x0d18 }
        r9 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x0d18 }
        r11 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0d18 }
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x0d18 }
        r13 = (float) r4;	 Catch:{ Exception -> 0x0d18 }
        r14 = 0;
        r15 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x0d18 }
        r17 = 6;
        r10 = r87;
        r16 = r87;
        r4 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r8, r9, r10, r11, r12, r13, r14, r15, r16, r17);	 Catch:{ Exception -> 0x0d18 }
        r0 = r146;
        r0.descriptionLayout = r4;	 Catch:{ Exception -> 0x0d18 }
    L_0x0c47:
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x0d18 }
        r0 = r146;
        r6 = r0.descriptionLayout;	 Catch:{ Exception -> 0x0d18 }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x0d18 }
        r6 = r6 + -1;
        r79 = r4.getLineBottom(r6);	 Catch:{ Exception -> 0x0d18 }
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x0d18 }
        r4 = r4 + r79;
        r0 = r146;
        r0.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x0d18 }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x0d18 }
        r4 = r4 + r79;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x0d18 }
        r78 = 0;
        r41 = 0;
    L_0x0c71:
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x0d18 }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x0d18 }
        r0 = r41;
        if (r0 >= r4) goto L_0x13dc;
    L_0x0c7d:
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x0d18 }
        r0 = r41;
        r4 = r4.getLineLeft(r0);	 Catch:{ Exception -> 0x0d18 }
        r8 = (double) r4;	 Catch:{ Exception -> 0x0d18 }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x0d18 }
        r0 = (int) r8;	 Catch:{ Exception -> 0x0d18 }
        r86 = r0;
        if (r86 == 0) goto L_0x0ca0;
    L_0x0c91:
        r78 = 1;
        r0 = r146;
        r4 = r0.descriptionX;	 Catch:{ Exception -> 0x0d18 }
        if (r4 != 0) goto L_0x13cb;
    L_0x0c99:
        r0 = r86;
        r4 = -r0;
        r0 = r146;
        r0.descriptionX = r4;	 Catch:{ Exception -> 0x0d18 }
    L_0x0ca0:
        r41 = r41 + 1;
        goto L_0x0c71;
    L_0x0ca3:
        r0 = r146;
        r4 = r0.titleLayout;	 Catch:{ Exception -> 0x0b3d }
        r0 = r41;
        r4 = r4.getLineWidth(r0);	 Catch:{ Exception -> 0x0b3d }
        r8 = (double) r4;	 Catch:{ Exception -> 0x0b3d }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x0b3d }
        r0 = (int) r8;
        r143 = r0;
        goto L_0x07c2;
    L_0x0cb7:
        r119 = r12;
        goto L_0x0b43;
    L_0x0cbb:
        r9 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x0cf3 }
        r4 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x0cf3 }
        r11 = r87 - r4;
        r13 = 1;
        r8 = r47;
        r10 = r87;
        r12 = r119;
        r4 = generateStaticLayout(r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x0cf3 }
        r0 = r146;
        r0.authorLayout = r4;	 Catch:{ Exception -> 0x0cf3 }
        r0 = r146;
        r4 = r0.authorLayout;	 Catch:{ Exception -> 0x0cf3 }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x0cf3 }
        r12 = r119 - r4;
        goto L_0x0b91;
    L_0x0ce0:
        r0 = r146;
        r4 = r0.authorLayout;	 Catch:{ Exception -> 0x384c }
        r6 = 0;
        r4 = r4.getLineWidth(r6);	 Catch:{ Exception -> 0x384c }
        r8 = (double) r4;	 Catch:{ Exception -> 0x384c }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x384c }
        r0 = (int) r8;
        r143 = r0;
        goto L_0x0bd8;
    L_0x0cf3:
        r70 = move-exception;
        r12 = r119;
    L_0x0cf6:
        org.telegram.messenger.FileLog.e(r70);
        goto L_0x0be8;
    L_0x0cfb:
        r118 = r12;
        r0 = r147;
        r8 = r0.linkDescription;	 Catch:{ Exception -> 0x0d18 }
        r9 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x0d18 }
        r4 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x0d18 }
        r11 = r87 - r4;
        r13 = 6;
        r10 = r87;
        r4 = generateStaticLayout(r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x0d18 }
        r0 = r146;
        r0.descriptionLayout = r4;	 Catch:{ Exception -> 0x0d18 }
        goto L_0x0c47;
    L_0x0d18:
        r70 = move-exception;
        org.telegram.messenger.FileLog.e(r70);
    L_0x0d1c:
        if (r127 == 0) goto L_0x0d3c;
    L_0x0d1e:
        r0 = r146;
        r4 = r0.descriptionLayout;
        if (r4 == 0) goto L_0x0d35;
    L_0x0d24:
        r0 = r146;
        r4 = r0.descriptionLayout;
        if (r4 == 0) goto L_0x0d3c;
    L_0x0d2a:
        r0 = r146;
        r4 = r0.descriptionLayout;
        r4 = r4.getLineCount();
        r6 = 1;
        if (r4 != r6) goto L_0x0d3c;
    L_0x0d35:
        r127 = 0;
        r4 = 0;
        r0 = r146;
        r0.isSmallImage = r4;
    L_0x0d3c:
        if (r127 == 0) goto L_0x147a;
    L_0x0d3e:
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r97 = org.telegram.messenger.AndroidUtilities.dp(r4);
    L_0x0d44:
        if (r67 == 0) goto L_0x17c6;
    L_0x0d46:
        r4 = org.telegram.messenger.MessageObject.isGifDocument(r67);
        if (r4 == 0) goto L_0x1485;
    L_0x0d4c:
        r4 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r4 != 0) goto L_0x0d56;
    L_0x0d50:
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r147;
        r0.gifState = r4;
    L_0x0d56:
        r0 = r146;
        r6 = r0.photoImage;
        r0 = r147;
        r4 = r0.gifState;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x147e;
    L_0x0d64:
        r4 = 1;
    L_0x0d65:
        r6.setAllowStartAnimation(r4);
        r0 = r67;
        r4 = r0.thumb;
        r0 = r146;
        r0.currentPhotoObject = r4;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x0de2;
    L_0x0d76:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        if (r4 == 0) goto L_0x0d86;
    L_0x0d7e:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.h;
        if (r4 != 0) goto L_0x0de2;
    L_0x0d86:
        r41 = 0;
    L_0x0d88:
        r0 = r67;
        r4 = r0.attributes;
        r4 = r4.size();
        r0 = r41;
        if (r0 >= r4) goto L_0x0dc0;
    L_0x0d94:
        r0 = r67;
        r4 = r0.attributes;
        r0 = r41;
        r46 = r4.get(r0);
        r46 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r46;
        r0 = r46;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r4 != 0) goto L_0x0dac;
    L_0x0da6:
        r0 = r46;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r4 == 0) goto L_0x1481;
    L_0x0dac:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r46;
        r6 = r0.w;
        r4.w = r6;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r46;
        r6 = r0.h;
        r4.h = r6;
    L_0x0dc0:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        if (r4 == 0) goto L_0x0dd0;
    L_0x0dc8:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.h;
        if (r4 != 0) goto L_0x0de2;
    L_0x0dd0:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r146;
        r6 = r0.currentPhotoObject;
        r8 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r6.h = r8;
        r4.w = r8;
    L_0x0de2:
        r4 = 2;
        r0 = r146;
        r0.documentAttachType = r4;
        r14 = r140;
    L_0x0de9:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 5;
        if (r4 == r6) goto L_0x10e9;
    L_0x0df0:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 3;
        if (r4 == r6) goto L_0x10e9;
    L_0x0df7:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 1;
        if (r4 == r6) goto L_0x10e9;
    L_0x0dfe:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 != 0) goto L_0x0e06;
    L_0x0e04:
        if (r14 == 0) goto L_0x1b4a;
    L_0x0e06:
        if (r136 == 0) goto L_0x1836;
    L_0x0e08:
        r4 = "photo";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x0e37;
    L_0x0e13:
        r4 = "document";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x0e25;
    L_0x0e1e:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 6;
        if (r4 != r6) goto L_0x0e37;
    L_0x0e25:
        r4 = "gif";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 != 0) goto L_0x0e37;
    L_0x0e30:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 4;
        if (r4 != r6) goto L_0x1836;
    L_0x0e37:
        r4 = 1;
    L_0x0e38:
        r0 = r146;
        r0.drawImageButton = r4;
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        if (r4 == 0) goto L_0x0e60;
    L_0x0e42:
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
    L_0x0e60:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 6;
        if (r4 != r6) goto L_0x1846;
    L_0x0e67:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1839;
    L_0x0e6d:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = (float) r4;
        r6 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r4 = r4 * r6;
        r0 = (int) r4;
        r97 = r0;
    L_0x0e78:
        r0 = r146;
        r4 = r0.hasInvoicePreview;
        if (r4 == 0) goto L_0x1859;
    L_0x0e7e:
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
    L_0x0e84:
        r4 = r97 - r4;
        r4 = r4 + r42;
        r0 = r95;
        r95 = java.lang.Math.max(r0, r4);
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x185c;
    L_0x0e94:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r6 = -1;
        r4.size = r6;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        if (r4 == 0) goto L_0x0ea8;
    L_0x0ea1:
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        r6 = -1;
        r4.size = r6;
    L_0x0ea8:
        if (r127 != 0) goto L_0x0eb1;
    L_0x0eaa:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 7;
        if (r4 != r6) goto L_0x1861;
    L_0x0eb1:
        r79 = r97;
        r143 = r97;
    L_0x0eb5:
        r0 = r146;
        r4 = r0.isSmallImage;
        if (r4 == 0) goto L_0x18fa;
    L_0x0ebb:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 + r44;
        r0 = r146;
        r6 = r0.linkPreviewHeight;
        if (r4 <= r6) goto L_0x0ef2;
    L_0x0ec9:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = r6 + r44;
        r0 = r146;
        r8 = r0.linkPreviewHeight;
        r6 = r6 - r8;
        r8 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r6 = r6 + r8;
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 + r44;
        r0 = r146;
        r0.linkPreviewHeight = r4;
    L_0x0ef2:
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        r6 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;
    L_0x0f01:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r8 = 0;
        r0 = r143;
        r1 = r79;
        r4.setImageCoords(r6, r8, r0, r1);
        r4 = java.util.Locale.US;
        r6 = "%d_%d";
        r8 = 2;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r10 = java.lang.Integer.valueOf(r143);
        r8[r9] = r10;
        r9 = 1;
        r10 = java.lang.Integer.valueOf(r79);
        r8[r9] = r10;
        r4 = java.lang.String.format(r4, r6, r8);
        r0 = r146;
        r0.currentPhotoFilter = r4;
        r4 = java.util.Locale.US;
        r6 = "%d_%d_b";
        r8 = 2;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r10 = java.lang.Integer.valueOf(r143);
        r8[r9] = r10;
        r9 = 1;
        r10 = java.lang.Integer.valueOf(r79);
        r8[r9] = r10;
        r4 = java.lang.String.format(r4, r6, r8);
        r0 = r146;
        r0.currentPhotoFilterThumb = r4;
        if (r14 == 0) goto L_0x1917;
    L_0x0f4c:
        r0 = r146;
        r13 = r0.photoImage;
        r15 = 0;
        r0 = r146;
        r0 = r0.currentPhotoFilter;
        r16 = r0;
        r17 = 0;
        r18 = 0;
        r19 = "b1";
        r0 = r14.size;
        r20 = r0;
        r21 = 0;
        r22 = 1;
        r13.setImage(r14, r15, r16, r17, r18, r19, r20, r21, r22);
    L_0x0f69:
        r4 = 1;
        r0 = r146;
        r0.drawPhotoImage = r4;
        if (r136 == 0) goto L_0x1b09;
    L_0x0f70:
        r4 = "video";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x1b09;
    L_0x0f7b:
        if (r68 == 0) goto L_0x1b09;
    L_0x0f7d:
        r102 = r68 / 60;
        r4 = r102 * 60;
        r124 = r68 - r4;
        r4 = "%d:%02d";
        r6 = 2;
        r6 = new java.lang.Object[r6];
        r8 = 0;
        r9 = java.lang.Integer.valueOf(r102);
        r6[r8] = r9;
        r8 = 1;
        r9 = java.lang.Integer.valueOf(r124);
        r6[r8] = r9;
        r5 = java.lang.String.format(r4, r6);
        r4 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r4 = r4.measureText(r5);
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
        r4 = (int) r8;
        r0 = r146;
        r0.durationWidth = r4;
        r15 = new android.text.StaticLayout;
        r17 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r0 = r146;
        r0 = r0.durationWidth;
        r18 = r0;
        r19 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r20 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r21 = 0;
        r22 = 0;
        r16 = r5;
        r15.<init>(r16, r17, r18, r19, r20, r21, r22);
        r0 = r146;
        r0.videoInfoLayout = r15;
    L_0x0fc6:
        r0 = r146;
        r4 = r0.hasInvoicePreview;
        if (r4 == 0) goto L_0x10af;
    L_0x0fcc:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.flags;
        r4 = r4 & 4;
        if (r4 == 0) goto L_0x1b74;
    L_0x0fd8:
        r4 = "PaymentReceipt";
        r6 = 2131494063; // 0x7f0c04af float:1.8611624E38 double:1.053097991E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r5 = r4.toUpperCase();
    L_0x0fe6:
        r4 = org.telegram.messenger.LocaleController.getInstance();
        r0 = r147;
        r6 = r0.messageOwner;
        r6 = r6.media;
        r8 = r6.total_amount;
        r0 = r147;
        r6 = r0.messageOwner;
        r6 = r6.media;
        r6 = r6.currency;
        r115 = r4.formatCurrencyString(r8, r6);
        r16 = new android.text.SpannableStringBuilder;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r115;
        r4 = r4.append(r0);
        r6 = " ";
        r4 = r4.append(r6);
        r4 = r4.append(r5);
        r4 = r4.toString();
        r0 = r16;
        r0.<init>(r4);
        r4 = new org.telegram.ui.Components.TypefaceSpan;
        r6 = "fonts/rmedium.ttf";
        r6 = org.telegram.messenger.AndroidUtilities.getTypeface(r6);
        r4.<init>(r6);
        r6 = 0;
        r8 = r115.length();
        r9 = 33;
        r0 = r16;
        r0.setSpan(r4, r6, r8, r9);
        r4 = org.telegram.ui.ActionBar.Theme.chat_shipmentPaint;
        r6 = 0;
        r8 = r16.length();
        r0 = r16;
        r4 = r4.measureText(r0, r6, r8);
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
        r4 = (int) r8;
        r0 = r146;
        r0.durationWidth = r4;
        r15 = new android.text.StaticLayout;
        r17 = org.telegram.ui.ActionBar.Theme.chat_shipmentPaint;
        r0 = r146;
        r4 = r0.durationWidth;
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r18 = r4 + r6;
        r19 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r20 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r21 = 0;
        r22 = 0;
        r15.<init>(r16, r17, r18, r19, r20, r21, r22);
        r0 = r146;
        r0.videoInfoLayout = r15;
        r0 = r146;
        r4 = r0.drawPhotoImage;
        if (r4 != 0) goto L_0x10af;
    L_0x1073:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.durationWidth;
        r0 = r146;
        r6 = r0.timeWidth;
        r4 = r4 + r6;
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r26;
        if (r4 <= r0) goto L_0x1b9e;
    L_0x1096:
        r0 = r146;
        r4 = r0.durationWidth;
        r0 = r95;
        r95 = java.lang.Math.max(r4, r0);
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
    L_0x10af:
        r0 = r146;
        r4 = r0.hasGamePreview;
        if (r4 == 0) goto L_0x10de;
    L_0x10b5:
        r0 = r147;
        r4 = r0.textHeight;
        if (r4 == 0) goto L_0x10de;
    L_0x10bb:
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        r0 = r147;
        r6 = r0.textHeight;
        r8 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r6 = r6 + r8;
        r4 = r4 + r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
    L_0x10de:
        r0 = r146;
        r1 = r26;
        r2 = r132;
        r3 = r95;
        r0.calcBackgroundWidth(r1, r2, r3);
    L_0x10e9:
        r0 = r146;
        r4 = r0.drawInstantView;
        if (r4 == 0) goto L_0x11a0;
    L_0x10ef:
        r4 = 1107558400; // 0x42040000 float:33.0 double:5.47206556E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r146;
        r0.instantWidth = r4;
        r0 = r146;
        r4 = r0.drawInstantViewType;
        r6 = 1;
        if (r4 != r6) goto L_0x1bb6;
    L_0x1100:
        r4 = "OpenChannel";
        r6 = 2131493990; // 0x7f0c0466 float:1.8611476E38 double:1.053097955E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r4, r6);
    L_0x110a:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1117126656; // 0x42960000 float:75.0 double:5.51933903E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r20 = r4 - r6;
        r17 = new android.text.StaticLayout;
        r4 = org.telegram.ui.ActionBar.Theme.chat_instantViewPaint;
        r0 = r20;
        r6 = (float) r0;
        r8 = android.text.TextUtils.TruncateAt.END;
        r18 = android.text.TextUtils.ellipsize(r5, r4, r6, r8);
        r19 = org.telegram.ui.ActionBar.Theme.chat_instantViewPaint;
        r21 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r22 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r23 = 0;
        r24 = 0;
        r17.<init>(r18, r19, r20, r21, r22, r23, r24);
        r0 = r17;
        r1 = r146;
        r1.instantViewLayout = r0;
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1107820544; // 0x42080000 float:34.0 double:5.473360725E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.instantWidth = r4;
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1110966272; // 0x42380000 float:46.0 double:5.488902687E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.instantViewLayout;
        if (r4 == 0) goto L_0x11a0;
    L_0x115a:
        r0 = r146;
        r4 = r0.instantViewLayout;
        r4 = r4.getLineCount();
        if (r4 <= 0) goto L_0x11a0;
    L_0x1164:
        r0 = r146;
        r4 = r0.instantWidth;
        r8 = (double) r4;
        r0 = r146;
        r4 = r0.instantViewLayout;
        r6 = 0;
        r4 = r4.getLineWidth(r6);
        r10 = (double) r4;
        r10 = java.lang.Math.ceil(r10);
        r8 = r8 - r10;
        r4 = (int) r8;
        r6 = r4 / 2;
        r0 = r146;
        r4 = r0.drawInstantViewType;
        if (r4 != 0) goto L_0x1be8;
    L_0x1181:
        r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
    L_0x1187:
        r4 = r4 + r6;
        r0 = r146;
        r0.instantTextX = r4;
        r0 = r146;
        r4 = r0.instantTextX;
        r0 = r146;
        r6 = r0.instantViewLayout;
        r8 = 0;
        r6 = r6.getLineLeft(r8);
        r6 = -r6;
        r6 = (int) r6;
        r4 = r4 + r6;
        r0 = r146;
        r0.instantTextX = r4;
    L_0x11a0:
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 != 0) goto L_0x3481;
    L_0x11a6:
        r0 = r146;
        r4 = r0.captionLayout;
        if (r4 != 0) goto L_0x3481;
    L_0x11ac:
        r0 = r147;
        r4 = r0.caption;
        if (r4 == 0) goto L_0x3481;
    L_0x11b2:
        r0 = r147;
        r4 = r0.type;
        r6 = 13;
        if (r4 == r6) goto L_0x3481;
    L_0x11ba:
        r0 = r146;
        r4 = r0.backgroundWidth;	 Catch:{ Exception -> 0x3478 }
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x3478 }
        r143 = r4 - r6;
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x3478 }
        r6 = 24;
        if (r4 < r6) goto L_0x3453;
    L_0x11cc:
        r0 = r147;
        r4 = r0.caption;	 Catch:{ Exception -> 0x3478 }
        r6 = 0;
        r0 = r147;
        r8 = r0.caption;	 Catch:{ Exception -> 0x3478 }
        r8 = r8.length();	 Catch:{ Exception -> 0x3478 }
        r9 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x3478 }
        r10 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x3478 }
        r10 = r143 - r10;
        r4 = android.text.StaticLayout.Builder.obtain(r4, r6, r8, r9, r10);	 Catch:{ Exception -> 0x3478 }
        r6 = 1;
        r4 = r4.setBreakStrategy(r6);	 Catch:{ Exception -> 0x3478 }
        r6 = 0;
        r4 = r4.setHyphenationFrequency(r6);	 Catch:{ Exception -> 0x3478 }
        r6 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x3478 }
        r4 = r4.setAlignment(r6);	 Catch:{ Exception -> 0x3478 }
        r4 = r4.build();	 Catch:{ Exception -> 0x3478 }
        r0 = r146;
        r0.captionLayout = r4;	 Catch:{ Exception -> 0x3478 }
    L_0x11ff:
        r0 = r146;
        r4 = r0.captionLayout;	 Catch:{ Exception -> 0x3478 }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x3478 }
        if (r4 <= 0) goto L_0x1293;
    L_0x1209:
        r0 = r146;
        r6 = r0.timeWidth;	 Catch:{ Exception -> 0x3478 }
        r4 = r147.isOutOwner();	 Catch:{ Exception -> 0x3478 }
        if (r4 == 0) goto L_0x347e;
    L_0x1213:
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x3478 }
    L_0x1219:
        r133 = r6 + r4;
        r0 = r146;
        r4 = r0.captionLayout;	 Catch:{ Exception -> 0x3478 }
        r4 = r4.getHeight();	 Catch:{ Exception -> 0x3478 }
        r0 = r146;
        r0.captionHeight = r4;	 Catch:{ Exception -> 0x3478 }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x3478 }
        r0 = r146;
        r6 = r0.captionHeight;	 Catch:{ Exception -> 0x3478 }
        r8 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);	 Catch:{ Exception -> 0x3478 }
        r6 = r6 + r8;
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x3478 }
        r0 = r146;
        r4 = r0.captionLayout;	 Catch:{ Exception -> 0x3478 }
        r0 = r146;
        r6 = r0.captionLayout;	 Catch:{ Exception -> 0x3478 }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x3478 }
        r6 = r6 + -1;
        r4 = r4.getLineWidth(r6);	 Catch:{ Exception -> 0x3478 }
        r0 = r146;
        r6 = r0.captionLayout;	 Catch:{ Exception -> 0x3478 }
        r0 = r146;
        r8 = r0.captionLayout;	 Catch:{ Exception -> 0x3478 }
        r8 = r8.getLineCount();	 Catch:{ Exception -> 0x3478 }
        r8 = r8 + -1;
        r6 = r6.getLineLeft(r8);	 Catch:{ Exception -> 0x3478 }
        r82 = r4 + r6;
        r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x3478 }
        r4 = r143 - r4;
        r4 = (float) r4;	 Catch:{ Exception -> 0x3478 }
        r4 = r4 - r82;
        r0 = r133;
        r6 = (float) r0;	 Catch:{ Exception -> 0x3478 }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 >= 0) goto L_0x1293;
    L_0x1273:
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x3478 }
        r6 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x3478 }
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x3478 }
        r0 = r146;
        r4 = r0.captionHeight;	 Catch:{ Exception -> 0x3478 }
        r6 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x3478 }
        r4 = r4 + r6;
        r0 = r146;
        r0.captionHeight = r4;	 Catch:{ Exception -> 0x3478 }
        r57 = 2;
    L_0x1293:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r8 = r4.eventId;
        r10 = 0;
        r4 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r4 == 0) goto L_0x34f7;
    L_0x129f:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.isMediaEmpty();
        if (r4 != 0) goto L_0x34f7;
    L_0x12a9:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        if (r4 == 0) goto L_0x34f7;
    L_0x12b5:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1109655552; // 0x42240000 float:41.0 double:5.48242687E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r87 = r4 - r6;
        r4 = 1;
        r0 = r146;
        r0.hasOldCaptionPreview = r4;
        r4 = 0;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.media;
        r0 = r4.webpage;
        r141 = r0;
        r4 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x34a8 }
        r0 = r141;
        r6 = r0.site_name;	 Catch:{ Exception -> 0x34a8 }
        r4 = r4.measureText(r6);	 Catch:{ Exception -> 0x34a8 }
        r8 = (double) r4;	 Catch:{ Exception -> 0x34a8 }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x34a8 }
        r0 = (int) r8;	 Catch:{ Exception -> 0x34a8 }
        r143 = r0;
        r31 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x34a8 }
        r0 = r141;
        r0 = r0.site_name;	 Catch:{ Exception -> 0x34a8 }
        r32 = r0;
        r33 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x34a8 }
        r0 = r143;
        r1 = r87;
        r34 = java.lang.Math.min(r0, r1);	 Catch:{ Exception -> 0x34a8 }
        r35 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x34a8 }
        r36 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r37 = 0;
        r38 = 0;
        r31.<init>(r32, r33, r34, r35, r36, r37, r38);	 Catch:{ Exception -> 0x34a8 }
        r0 = r31;
        r1 = r146;
        r1.siteNameLayout = r0;	 Catch:{ Exception -> 0x34a8 }
        r0 = r146;
        r4 = r0.siteNameLayout;	 Catch:{ Exception -> 0x34a8 }
        r0 = r146;
        r6 = r0.siteNameLayout;	 Catch:{ Exception -> 0x34a8 }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x34a8 }
        r6 = r6 + -1;
        r79 = r4.getLineBottom(r6);	 Catch:{ Exception -> 0x34a8 }
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x34a8 }
        r4 = r4 + r79;
        r0 = r146;
        r0.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x34a8 }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x34a8 }
        r4 = r4 + r79;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x34a8 }
    L_0x1332:
        r4 = 0;
        r0 = r146;
        r0.descriptionX = r4;	 Catch:{ Exception -> 0x34bf }
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x34bf }
        if (r4 == 0) goto L_0x134c;
    L_0x133d:
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x34bf }
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x34bf }
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x34bf }
    L_0x134c:
        r0 = r141;
        r0 = r0.description;	 Catch:{ Exception -> 0x34bf }
        r31 = r0;
        r32 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x34bf }
        r34 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x34bf }
        r35 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x34bf }
        r0 = (float) r4;	 Catch:{ Exception -> 0x34bf }
        r36 = r0;
        r37 = 0;
        r38 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x34bf }
        r40 = 6;
        r33 = r87;
        r39 = r87;
        r4 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r31, r32, r33, r34, r35, r36, r37, r38, r39, r40);	 Catch:{ Exception -> 0x34bf }
        r0 = r146;
        r0.descriptionLayout = r4;	 Catch:{ Exception -> 0x34bf }
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x34bf }
        r0 = r146;
        r6 = r0.descriptionLayout;	 Catch:{ Exception -> 0x34bf }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x34bf }
        r6 = r6 + -1;
        r79 = r4.getLineBottom(r6);	 Catch:{ Exception -> 0x34bf }
        r0 = r146;
        r4 = r0.linkPreviewHeight;	 Catch:{ Exception -> 0x34bf }
        r4 = r4 + r79;
        r0 = r146;
        r0.linkPreviewHeight = r4;	 Catch:{ Exception -> 0x34bf }
        r0 = r146;
        r4 = r0.totalHeight;	 Catch:{ Exception -> 0x34bf }
        r4 = r4 + r79;
        r0 = r146;
        r0.totalHeight = r4;	 Catch:{ Exception -> 0x34bf }
        r41 = 0;
    L_0x139b:
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x34bf }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x34bf }
        r0 = r41;
        if (r0 >= r4) goto L_0x34c3;
    L_0x13a7:
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x34bf }
        r0 = r41;
        r4 = r4.getLineLeft(r0);	 Catch:{ Exception -> 0x34bf }
        r8 = (double) r4;	 Catch:{ Exception -> 0x34bf }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x34bf }
        r0 = (int) r8;	 Catch:{ Exception -> 0x34bf }
        r86 = r0;
        if (r86 == 0) goto L_0x13c8;
    L_0x13bb:
        r0 = r146;
        r4 = r0.descriptionX;	 Catch:{ Exception -> 0x34bf }
        if (r4 != 0) goto L_0x34ae;
    L_0x13c1:
        r0 = r86;
        r4 = -r0;
        r0 = r146;
        r0.descriptionX = r4;	 Catch:{ Exception -> 0x34bf }
    L_0x13c8:
        r41 = r41 + 1;
        goto L_0x139b;
    L_0x13cb:
        r0 = r146;
        r4 = r0.descriptionX;	 Catch:{ Exception -> 0x0d18 }
        r0 = r86;
        r6 = -r0;
        r4 = java.lang.Math.max(r4, r6);	 Catch:{ Exception -> 0x0d18 }
        r0 = r146;
        r0.descriptionX = r4;	 Catch:{ Exception -> 0x0d18 }
        goto L_0x0ca0;
    L_0x13dc:
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x0d18 }
        r129 = r4.getWidth();	 Catch:{ Exception -> 0x0d18 }
        r41 = 0;
    L_0x13e6:
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x0d18 }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x0d18 }
        r0 = r41;
        if (r0 >= r4) goto L_0x0d1c;
    L_0x13f2:
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x0d18 }
        r0 = r41;
        r4 = r4.getLineLeft(r0);	 Catch:{ Exception -> 0x0d18 }
        r8 = (double) r4;	 Catch:{ Exception -> 0x0d18 }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x0d18 }
        r0 = (int) r8;	 Catch:{ Exception -> 0x0d18 }
        r86 = r0;
        if (r86 != 0) goto L_0x1411;
    L_0x1406:
        r0 = r146;
        r4 = r0.descriptionX;	 Catch:{ Exception -> 0x0d18 }
        if (r4 == 0) goto L_0x1411;
    L_0x140c:
        r4 = 0;
        r0 = r146;
        r0.descriptionX = r4;	 Catch:{ Exception -> 0x0d18 }
    L_0x1411:
        if (r86 == 0) goto L_0x145e;
    L_0x1413:
        r143 = r129 - r86;
    L_0x1415:
        r0 = r41;
        r1 = r118;
        if (r0 < r1) goto L_0x1425;
    L_0x141b:
        if (r118 == 0) goto L_0x142d;
    L_0x141d:
        if (r86 == 0) goto L_0x142d;
    L_0x141f:
        r0 = r146;
        r4 = r0.isSmallImage;	 Catch:{ Exception -> 0x0d18 }
        if (r4 == 0) goto L_0x142d;
    L_0x1425:
        r4 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x0d18 }
        r143 = r143 + r4;
    L_0x142d:
        r4 = r143 + r42;
        r0 = r98;
        if (r0 >= r4) goto L_0x1453;
    L_0x1433:
        if (r135 == 0) goto L_0x1442;
    L_0x1435:
        r0 = r146;
        r4 = r0.titleX;	 Catch:{ Exception -> 0x0d18 }
        r6 = r143 + r42;
        r6 = r6 - r98;
        r4 = r4 + r6;
        r0 = r146;
        r0.titleX = r4;	 Catch:{ Exception -> 0x0d18 }
    L_0x1442:
        if (r48 == 0) goto L_0x1451;
    L_0x1444:
        r0 = r146;
        r4 = r0.authorX;	 Catch:{ Exception -> 0x0d18 }
        r6 = r143 + r42;
        r6 = r6 - r98;
        r4 = r4 + r6;
        r0 = r146;
        r0.authorX = r4;	 Catch:{ Exception -> 0x0d18 }
    L_0x1451:
        r98 = r143 + r42;
    L_0x1453:
        r4 = r143 + r42;
        r0 = r95;
        r95 = java.lang.Math.max(r0, r4);	 Catch:{ Exception -> 0x0d18 }
        r41 = r41 + 1;
        goto L_0x13e6;
    L_0x145e:
        if (r78 == 0) goto L_0x1463;
    L_0x1460:
        r143 = r129;
        goto L_0x1415;
    L_0x1463:
        r0 = r146;
        r4 = r0.descriptionLayout;	 Catch:{ Exception -> 0x0d18 }
        r0 = r41;
        r4 = r4.getLineWidth(r0);	 Catch:{ Exception -> 0x0d18 }
        r8 = (double) r4;	 Catch:{ Exception -> 0x0d18 }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x0d18 }
        r4 = (int) r8;	 Catch:{ Exception -> 0x0d18 }
        r0 = r129;
        r143 = java.lang.Math.min(r4, r0);	 Catch:{ Exception -> 0x0d18 }
        goto L_0x1415;
    L_0x147a:
        r97 = r87;
        goto L_0x0d44;
    L_0x147e:
        r4 = 0;
        goto L_0x0d65;
    L_0x1481:
        r41 = r41 + 1;
        goto L_0x0d88;
    L_0x1485:
        r4 = org.telegram.messenger.MessageObject.isVideoDocument(r67);
        if (r4 == 0) goto L_0x150e;
    L_0x148b:
        r0 = r67;
        r4 = r0.thumb;
        r0 = r146;
        r0.currentPhotoObject = r4;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x14ff;
    L_0x1499:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        if (r4 == 0) goto L_0x14a9;
    L_0x14a1:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.h;
        if (r4 != 0) goto L_0x14ff;
    L_0x14a9:
        r41 = 0;
    L_0x14ab:
        r0 = r67;
        r4 = r0.attributes;
        r4 = r4.size();
        r0 = r41;
        if (r0 >= r4) goto L_0x14dd;
    L_0x14b7:
        r0 = r67;
        r4 = r0.attributes;
        r0 = r41;
        r46 = r4.get(r0);
        r46 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r46;
        r0 = r46;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r4 == 0) goto L_0x150b;
    L_0x14c9:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r46;
        r6 = r0.w;
        r4.w = r6;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r46;
        r6 = r0.h;
        r4.h = r6;
    L_0x14dd:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        if (r4 == 0) goto L_0x14ed;
    L_0x14e5:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.h;
        if (r4 != 0) goto L_0x14ff;
    L_0x14ed:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r146;
        r6 = r0.currentPhotoObject;
        r8 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r6.h = r8;
        r4.w = r8;
    L_0x14ff:
        r4 = 0;
        r0 = r146;
        r1 = r147;
        r0.createDocumentLayout(r4, r1);
        r14 = r140;
        goto L_0x0de9;
    L_0x150b:
        r41 = r41 + 1;
        goto L_0x14ab;
    L_0x150e:
        r4 = org.telegram.messenger.MessageObject.isStickerDocument(r67);
        if (r4 == 0) goto L_0x159a;
    L_0x1514:
        r0 = r67;
        r4 = r0.thumb;
        r0 = r146;
        r0.currentPhotoObject = r4;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x1588;
    L_0x1522:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        if (r4 == 0) goto L_0x1532;
    L_0x152a:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.h;
        if (r4 != 0) goto L_0x1588;
    L_0x1532:
        r41 = 0;
    L_0x1534:
        r0 = r67;
        r4 = r0.attributes;
        r4 = r4.size();
        r0 = r41;
        if (r0 >= r4) goto L_0x1566;
    L_0x1540:
        r0 = r67;
        r4 = r0.attributes;
        r0 = r41;
        r46 = r4.get(r0);
        r46 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r46;
        r0 = r46;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r4 == 0) goto L_0x1597;
    L_0x1552:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r46;
        r6 = r0.w;
        r4.w = r6;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r46;
        r6 = r0.h;
        r4.h = r6;
    L_0x1566:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        if (r4 == 0) goto L_0x1576;
    L_0x156e:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.h;
        if (r4 != 0) goto L_0x1588;
    L_0x1576:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r146;
        r6 = r0.currentPhotoObject;
        r8 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r6.h = r8;
        r4.w = r8;
    L_0x1588:
        r0 = r67;
        r1 = r146;
        r1.documentAttach = r0;
        r4 = 6;
        r0 = r146;
        r0.documentAttachType = r4;
        r14 = r140;
        goto L_0x0de9;
    L_0x1597:
        r41 = r41 + 1;
        goto L_0x1534;
    L_0x159a:
        r4 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r67);
        if (r4 == 0) goto L_0x15b7;
    L_0x15a0:
        r0 = r67;
        r4 = r0.thumb;
        r0 = r146;
        r0.currentPhotoObject = r4;
        r0 = r67;
        r1 = r146;
        r1.documentAttach = r0;
        r4 = 7;
        r0 = r146;
        r0.documentAttachType = r4;
        r14 = r140;
        goto L_0x0de9;
    L_0x15b7:
        r0 = r146;
        r1 = r26;
        r2 = r132;
        r3 = r95;
        r0.calcBackgroundWidth(r1, r2, r3);
        r4 = org.telegram.messenger.MessageObject.isStickerDocument(r67);
        if (r4 != 0) goto L_0x3860;
    L_0x15c8:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = r6 + r26;
        if (r4 >= r6) goto L_0x15e2;
    L_0x15d6:
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 + r26;
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x15e2:
        r4 = org.telegram.messenger.MessageObject.isVoiceDocument(r67);
        if (r4 == 0) goto L_0x163d;
    L_0x15e8:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r1 = r147;
        r0.createDocumentLayout(r4, r1);
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.textHeight;
        r6 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r6 = r0.linkPreviewHeight;
        r4 = r4 + r6;
        r0 = r146;
        r0.mediaOffsetY = r4;
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        r6 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        r0 = r146;
        r1 = r26;
        r2 = r132;
        r3 = r95;
        r0.calcBackgroundWidth(r1, r2, r3);
        r14 = r140;
        goto L_0x0de9;
    L_0x163d:
        r4 = org.telegram.messenger.MessageObject.isMusicDocument(r67);
        if (r4 == 0) goto L_0x170e;
    L_0x1643:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r1 = r147;
        r69 = r0.createDocumentLayout(r4, r1);
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.textHeight;
        r6 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r6 = r0.linkPreviewHeight;
        r4 = r4 + r6;
        r0 = r146;
        r0.mediaOffsetY = r4;
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        r6 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        r4 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r26 = r26 - r4;
        r4 = r69 + r42;
        r6 = 1119617024; // 0x42bc0000 float:94.0 double:5.53164308E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r95;
        r95 = java.lang.Math.max(r0, r4);
        r0 = r146;
        r4 = r0.songLayout;
        if (r4 == 0) goto L_0x16d0;
    L_0x16a7:
        r0 = r146;
        r4 = r0.songLayout;
        r4 = r4.getLineCount();
        if (r4 <= 0) goto L_0x16d0;
    L_0x16b1:
        r0 = r95;
        r4 = (float) r0;
        r0 = r146;
        r6 = r0.songLayout;
        r8 = 0;
        r6 = r6.getLineWidth(r8);
        r0 = r42;
        r8 = (float) r0;
        r6 = r6 + r8;
        r8 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = (float) r8;
        r6 = r6 + r8;
        r4 = java.lang.Math.max(r4, r6);
        r0 = (int) r4;
        r95 = r0;
    L_0x16d0:
        r0 = r146;
        r4 = r0.performerLayout;
        if (r4 == 0) goto L_0x16ff;
    L_0x16d6:
        r0 = r146;
        r4 = r0.performerLayout;
        r4 = r4.getLineCount();
        if (r4 <= 0) goto L_0x16ff;
    L_0x16e0:
        r0 = r95;
        r4 = (float) r0;
        r0 = r146;
        r6 = r0.performerLayout;
        r8 = 0;
        r6 = r6.getLineWidth(r8);
        r0 = r42;
        r8 = (float) r0;
        r6 = r6 + r8;
        r8 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = (float) r8;
        r6 = r6 + r8;
        r4 = java.lang.Math.max(r4, r6);
        r0 = (int) r4;
        r95 = r0;
    L_0x16ff:
        r0 = r146;
        r1 = r26;
        r2 = r132;
        r3 = r95;
        r0.calcBackgroundWidth(r1, r2, r3);
        r14 = r140;
        goto L_0x0de9;
    L_0x170e:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1126694912; // 0x43280000 float:168.0 double:5.566612494E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r1 = r147;
        r0.createDocumentLayout(r4, r1);
        r4 = 1;
        r0 = r146;
        r0.drawImageButton = r4;
        r0 = r146;
        r4 = r0.drawPhotoImage;
        if (r4 == 0) goto L_0x176a;
    L_0x172b:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        r6 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r0 = r146;
        r8 = r0.totalHeight;
        r0 = r146;
        r9 = r0.namesOffset;
        r8 = r8 + r9;
        r9 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r10 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r4.setImageCoords(r6, r8, r9, r10);
        r14 = r140;
        goto L_0x0de9;
    L_0x176a:
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.textHeight;
        r6 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r6 = r0.linkPreviewHeight;
        r4 = r4 + r6;
        r0 = r146;
        r0.mediaOffsetY = r4;
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r0 = r146;
        r8 = r0.totalHeight;
        r0 = r146;
        r9 = r0.namesOffset;
        r8 = r8 + r9;
        r9 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 - r9;
        r9 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r10 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r4.setImageCoords(r6, r8, r9, r10);
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        r6 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        r14 = r140;
        goto L_0x0de9;
    L_0x17c6:
        if (r110 == 0) goto L_0x181f;
    L_0x17c8:
        if (r136 == 0) goto L_0x1818;
    L_0x17ca:
        r4 = "photo";
        r0 = r136;
        r4 = r0.equals(r4);
        if (r4 == 0) goto L_0x1818;
    L_0x17d5:
        r4 = 1;
    L_0x17d6:
        r0 = r146;
        r0.drawImageButton = r4;
        r0 = r147;
        r8 = r0.photoThumbs;
        r0 = r146;
        r4 = r0.drawImageButton;
        if (r4 == 0) goto L_0x181a;
    L_0x17e4:
        r4 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
    L_0x17e8:
        r0 = r146;
        r6 = r0.drawImageButton;
        if (r6 != 0) goto L_0x181d;
    L_0x17ee:
        r6 = 1;
    L_0x17ef:
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r8, r4, r6);
        r0 = r146;
        r0.currentPhotoObject = r4;
        r0 = r147;
        r4 = r0.photoThumbs;
        r6 = 80;
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r6);
        r0 = r146;
        r0.currentPhotoObjectThumb = r4;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        r0 = r146;
        r6 = r0.currentPhotoObject;
        if (r4 != r6) goto L_0x3860;
    L_0x180f:
        r4 = 0;
        r0 = r146;
        r0.currentPhotoObjectThumb = r4;
        r14 = r140;
        goto L_0x0de9;
    L_0x1818:
        r4 = 0;
        goto L_0x17d6;
    L_0x181a:
        r4 = r97;
        goto L_0x17e8;
    L_0x181d:
        r6 = 0;
        goto L_0x17ef;
    L_0x181f:
        if (r140 == 0) goto L_0x3860;
    L_0x1821:
        r0 = r140;
        r4 = r0.mime_type;
        r6 = "image/";
        r4 = r4.startsWith(r6);
        if (r4 != 0) goto L_0x385c;
    L_0x182e:
        r14 = 0;
    L_0x182f:
        r4 = 0;
        r0 = r146;
        r0.drawImageButton = r4;
        goto L_0x0de9;
    L_0x1836:
        r4 = 0;
        goto L_0x0e38;
    L_0x1839:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r4 = (float) r4;
        r6 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r4 = r4 * r6;
        r0 = (int) r4;
        r97 = r0;
        goto L_0x0e78;
    L_0x1846:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 7;
        if (r4 != r6) goto L_0x0e78;
    L_0x184d:
        r97 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1;
        r4.setAllowDecodeSingleFrame(r6);
        goto L_0x0e78;
    L_0x1859:
        r4 = 0;
        goto L_0x0e84;
    L_0x185c:
        r4 = -1;
        r14.size = r4;
        goto L_0x0ea8;
    L_0x1861:
        r0 = r146;
        r4 = r0.hasGamePreview;
        if (r4 != 0) goto L_0x186d;
    L_0x1867:
        r0 = r146;
        r4 = r0.hasInvoicePreview;
        if (r4 == 0) goto L_0x1891;
    L_0x186d:
        r143 = 640; // 0x280 float:8.97E-43 double:3.16E-321;
        r79 = 360; // 0x168 float:5.04E-43 double:1.78E-321;
        r0 = r143;
        r4 = (float) r0;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = r97 - r6;
        r6 = (float) r6;
        r122 = r4 / r6;
        r0 = r143;
        r4 = (float) r0;
        r4 = r4 / r122;
        r0 = (int) r4;
        r143 = r0;
        r0 = r79;
        r4 = (float) r0;
        r4 = r4 / r122;
        r0 = (int) r4;
        r79 = r0;
        goto L_0x0eb5;
    L_0x1891:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.w;
        r143 = r0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.h;
        r79 = r0;
        r0 = r143;
        r4 = (float) r0;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = r97 - r6;
        r6 = (float) r6;
        r122 = r4 / r6;
        r0 = r143;
        r4 = (float) r0;
        r4 = r4 / r122;
        r0 = (int) r4;
        r143 = r0;
        r0 = r79;
        r4 = (float) r0;
        r4 = r4 / r122;
        r0 = (int) r4;
        r79 = r0;
        if (r7 == 0) goto L_0x18d6;
    L_0x18c1:
        if (r7 == 0) goto L_0x18e8;
    L_0x18c3:
        r4 = r7.toLowerCase();
        r6 = "instagram";
        r4 = r4.equals(r6);
        if (r4 != 0) goto L_0x18e8;
    L_0x18d0:
        r0 = r146;
        r4 = r0.documentAttachType;
        if (r4 != 0) goto L_0x18e8;
    L_0x18d6:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.y;
        r4 = r4 / 3;
        r0 = r79;
        if (r0 <= r4) goto L_0x0eb5;
    L_0x18e0:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.y;
        r79 = r4 / 3;
        goto L_0x0eb5;
    L_0x18e8:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.y;
        r4 = r4 / 2;
        r0 = r79;
        if (r0 <= r4) goto L_0x0eb5;
    L_0x18f2:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.y;
        r79 = r4 / 2;
        goto L_0x0eb5;
    L_0x18fa:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = r6 + r79;
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        r4 = r4 + r79;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        goto L_0x0f01;
    L_0x1917:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 6;
        if (r4 != r6) goto L_0x1958;
    L_0x191e:
        r0 = r146;
        r15 = r0.photoImage;
        r0 = r146;
        r0 = r0.documentAttach;
        r16 = r0;
        r17 = 0;
        r0 = r146;
        r0 = r0.currentPhotoFilter;
        r18 = r0;
        r19 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x1955;
    L_0x1938:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r20 = r0;
    L_0x1940:
        r21 = "b1";
        r0 = r146;
        r4 = r0.documentAttach;
        r0 = r4.size;
        r22 = r0;
        r23 = "webp";
        r24 = 1;
        r15.setImage(r16, r17, r18, r19, r20, r21, r22, r23, r24);
        goto L_0x0f69;
    L_0x1955:
        r20 = 0;
        goto L_0x1940;
    L_0x1958:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 4;
        if (r4 != r6) goto L_0x1980;
    L_0x195f:
        r0 = r146;
        r15 = r0.photoImage;
        r16 = 0;
        r17 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r18 = r0;
        r0 = r146;
        r0 = r0.currentPhotoFilter;
        r19 = r0;
        r20 = 0;
        r21 = 0;
        r22 = 0;
        r15.setImage(r16, r17, r18, r19, r20, r21, r22);
        goto L_0x0f69;
    L_0x1980:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 2;
        if (r4 == r6) goto L_0x198e;
    L_0x1987:
        r0 = r146;
        r4 = r0.documentAttachType;
        r6 = 7;
        if (r4 != r6) goto L_0x1a4c;
    L_0x198e:
        r71 = org.telegram.messenger.FileLoader.getAttachFileName(r67);
        r49 = 0;
        r4 = org.telegram.messenger.MessageObject.isNewGifDocument(r67);
        if (r4 == 0) goto L_0x19f8;
    L_0x199a:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r0 = r146;
        r6 = r0.currentMessageObject;
        r49 = r4.canDownloadMedia(r6);
    L_0x19aa:
        r4 = r147.isSending();
        if (r4 != 0) goto L_0x1a1d;
    L_0x19b0:
        r0 = r147;
        r4 = r0.mediaExists;
        if (r4 != 0) goto L_0x19c8;
    L_0x19b6:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.FileLoader.getInstance(r4);
        r0 = r71;
        r4 = r4.isLoadingFile(r0);
        if (r4 != 0) goto L_0x19c8;
    L_0x19c6:
        if (r49 == 0) goto L_0x1a1d;
    L_0x19c8:
        r4 = 0;
        r0 = r146;
        r0.photoNotSet = r4;
        r0 = r146;
        r15 = r0.photoImage;
        r17 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x1a1a;
    L_0x19d9:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r18 = r0;
    L_0x19e1:
        r0 = r146;
        r0 = r0.currentPhotoFilterThumb;
        r19 = r0;
        r0 = r67;
        r0 = r0.size;
        r20 = r0;
        r21 = 0;
        r22 = 0;
        r16 = r67;
        r15.setImage(r16, r17, r18, r19, r20, r21, r22);
        goto L_0x0f69;
    L_0x19f8:
        r4 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r67);
        if (r4 == 0) goto L_0x19aa;
    L_0x19fe:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r6 = r6 / 2;
        r4.setRoundRadius(r6);
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r0 = r146;
        r6 = r0.currentMessageObject;
        r49 = r4.canDownloadMedia(r6);
        goto L_0x19aa;
    L_0x1a1a:
        r18 = 0;
        goto L_0x19e1;
    L_0x1a1d:
        r4 = 1;
        r0 = r146;
        r0.photoNotSet = r4;
        r0 = r146;
        r15 = r0.photoImage;
        r16 = 0;
        r17 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x1a49;
    L_0x1a30:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r18 = r0;
    L_0x1a38:
        r0 = r146;
        r0 = r0.currentPhotoFilterThumb;
        r19 = r0;
        r20 = 0;
        r21 = 0;
        r22 = 0;
        r15.setImage(r16, r17, r18, r19, r20, r21, r22);
        goto L_0x0f69;
    L_0x1a49:
        r18 = 0;
        goto L_0x1a38;
    L_0x1a4c:
        r0 = r147;
        r0 = r0.mediaExists;
        r111 = r0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r71 = org.telegram.messenger.FileLoader.getAttachFileName(r4);
        r0 = r146;
        r4 = r0.hasGamePreview;
        if (r4 != 0) goto L_0x1a84;
    L_0x1a60:
        if (r111 != 0) goto L_0x1a84;
    L_0x1a62:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r0 = r146;
        r6 = r0.currentMessageObject;
        r4 = r4.canDownloadMedia(r6);
        if (r4 != 0) goto L_0x1a84;
    L_0x1a74:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.FileLoader.getInstance(r4);
        r0 = r71;
        r4 = r4.isLoadingFile(r0);
        if (r4 == 0) goto L_0x1abd;
    L_0x1a84:
        r4 = 0;
        r0 = r146;
        r0.photoNotSet = r4;
        r0 = r146;
        r15 = r0.photoImage;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r16 = r0;
        r0 = r146;
        r0 = r0.currentPhotoFilter;
        r17 = r0;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        if (r4 == 0) goto L_0x1aba;
    L_0x1aa1:
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        r0 = r4.location;
        r18 = r0;
    L_0x1aa9:
        r0 = r146;
        r0 = r0.currentPhotoFilterThumb;
        r19 = r0;
        r20 = 0;
        r21 = 0;
        r22 = 0;
        r15.setImage(r16, r17, r18, r19, r20, r21, r22);
        goto L_0x0f69;
    L_0x1aba:
        r18 = 0;
        goto L_0x1aa9;
    L_0x1abd:
        r4 = 1;
        r0 = r146;
        r0.photoNotSet = r4;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        if (r4 == 0) goto L_0x1afd;
    L_0x1ac8:
        r0 = r146;
        r15 = r0.photoImage;
        r16 = 0;
        r17 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        r0 = r4.location;
        r18 = r0;
        r4 = java.util.Locale.US;
        r6 = "%d_%d_b";
        r8 = 2;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r10 = java.lang.Integer.valueOf(r143);
        r8[r9] = r10;
        r9 = 1;
        r10 = java.lang.Integer.valueOf(r79);
        r8[r9] = r10;
        r19 = java.lang.String.format(r4, r6, r8);
        r20 = 0;
        r21 = 0;
        r22 = 0;
        r15.setImage(r16, r17, r18, r19, r20, r21, r22);
        goto L_0x0f69;
    L_0x1afd:
        r0 = r146;
        r6 = r0.photoImage;
        r4 = 0;
        r4 = (android.graphics.drawable.Drawable) r4;
        r6.setImageBitmap(r4);
        goto L_0x0f69;
    L_0x1b09:
        r0 = r146;
        r4 = r0.hasGamePreview;
        if (r4 == 0) goto L_0x0fc6;
    L_0x1b0f:
        r4 = "AttachGame";
        r6 = 2131493023; // 0x7f0c009f float:1.8609514E38 double:1.053097477E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r5 = r4.toUpperCase();
        r4 = org.telegram.ui.ActionBar.Theme.chat_gamePaint;
        r4 = r4.measureText(r5);
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
        r4 = (int) r8;
        r0 = r146;
        r0.durationWidth = r4;
        r15 = new android.text.StaticLayout;
        r17 = org.telegram.ui.ActionBar.Theme.chat_gamePaint;
        r0 = r146;
        r0 = r0.durationWidth;
        r18 = r0;
        r19 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r20 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r21 = 0;
        r22 = 0;
        r16 = r5;
        r15.<init>(r16, r17, r18, r19, r20, r21, r22);
        r0 = r146;
        r0.videoInfoLayout = r15;
        goto L_0x0fc6;
    L_0x1b4a:
        r0 = r146;
        r6 = r0.photoImage;
        r4 = 0;
        r4 = (android.graphics.drawable.Drawable) r4;
        r6.setImageBitmap(r4);
        r0 = r146;
        r4 = r0.linkPreviewHeight;
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.linkPreviewHeight = r4;
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        goto L_0x0fc6;
    L_0x1b74:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.test;
        if (r4 == 0) goto L_0x1b8e;
    L_0x1b7e:
        r4 = "PaymentTestInvoice";
        r6 = 2131494081; // 0x7f0c04c1 float:1.861166E38 double:1.0530979997E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r5 = r4.toUpperCase();
        goto L_0x0fe6;
    L_0x1b8e:
        r4 = "PaymentInvoice";
        r6 = 2131494050; // 0x7f0c04a2 float:1.8611597E38 double:1.0530979844E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r5 = r4.toUpperCase();
        goto L_0x0fe6;
    L_0x1b9e:
        r0 = r146;
        r4 = r0.durationWidth;
        r0 = r146;
        r6 = r0.timeWidth;
        r4 = r4 + r6;
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r95;
        r95 = java.lang.Math.max(r4, r0);
        goto L_0x10af;
    L_0x1bb6:
        r0 = r146;
        r4 = r0.drawInstantViewType;
        r6 = 2;
        if (r4 != r6) goto L_0x1bc9;
    L_0x1bbd:
        r4 = "OpenGroup";
        r6 = 2131493991; // 0x7f0c0467 float:1.8611478E38 double:1.0530979553E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r4, r6);
        goto L_0x110a;
    L_0x1bc9:
        r0 = r146;
        r4 = r0.drawInstantViewType;
        r6 = 3;
        if (r4 != r6) goto L_0x1bdc;
    L_0x1bd0:
        r4 = "OpenMessage";
        r6 = 2131493994; // 0x7f0c046a float:1.8611484E38 double:1.0530979568E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r4, r6);
        goto L_0x110a;
    L_0x1bdc:
        r4 = "InstantView";
        r6 = 2131493646; // 0x7f0c030e float:1.8610778E38 double:1.053097785E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r4, r6);
        goto L_0x110a;
    L_0x1be8:
        r4 = 0;
        goto L_0x1187;
    L_0x1beb:
        r0 = r146;
        r6 = r0.photoImage;
        r4 = 0;
        r4 = (android.graphics.drawable.Drawable) r4;
        r6.setImageBitmap(r4);
        r0 = r146;
        r1 = r26;
        r2 = r132;
        r3 = r95;
        r0.calcBackgroundWidth(r1, r2, r3);
        goto L_0x11a0;
    L_0x1c02:
        r0 = r147;
        r4 = r0.type;
        r6 = 16;
        if (r4 != r6) goto L_0x1dbc;
    L_0x1c0a:
        r4 = 0;
        r0 = r146;
        r0.drawName = r4;
        r4 = 0;
        r0 = r146;
        r0.drawForwardedName = r4;
        r4 = 0;
        r0 = r146;
        r0.drawPhotoImage = r4;
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1d51;
    L_0x1c1f:
        r6 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x1d4d;
    L_0x1c29:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x1d4d;
    L_0x1c2f:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x1d4d;
    L_0x1c35:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x1c37:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x1c4b:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.availableTimeWidth = r4;
        r4 = r146.getMaxNameWidth();
        r6 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r26 = r4 - r6;
        if (r26 >= 0) goto L_0x3858;
    L_0x1c68:
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r26 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r99 = r26;
    L_0x1c70:
        r4 = org.telegram.messenger.LocaleController.getInstance();
        r4 = r4.formatterDay;
        r0 = r147;
        r6 = r0.messageOwner;
        r6 = r6.date;
        r8 = (long) r6;
        r10 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r8 = r8 * r10;
        r130 = r4.format(r8);
        r0 = r147;
        r4 = r0.messageOwner;
        r0 = r4.action;
        r56 = r0;
        r56 = (org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall) r56;
        r0 = r56;
        r4 = r0.reason;
        r0 = r4 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
        r80 = r0;
        r4 = r147.isOutOwner();
        if (r4 == 0) goto L_0x1d8e;
    L_0x1c9c:
        if (r80 == 0) goto L_0x1d82;
    L_0x1c9e:
        r4 = "CallMessageOutgoingMissed";
        r6 = 2131493095; // 0x7f0c00e7 float:1.860966E38 double:1.0530975126E-314;
        r128 = org.telegram.messenger.LocaleController.getString(r4, r6);
    L_0x1ca8:
        r0 = r56;
        r4 = r0.duration;
        if (r4 <= 0) goto L_0x1cd0;
    L_0x1cae:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r130;
        r4 = r4.append(r0);
        r6 = ", ";
        r4 = r4.append(r6);
        r0 = r56;
        r6 = r0.duration;
        r6 = org.telegram.messenger.LocaleController.formatCallDuration(r6);
        r4 = r4.append(r6);
        r130 = r4.toString();
    L_0x1cd0:
        r21 = new android.text.StaticLayout;
        r4 = org.telegram.ui.ActionBar.Theme.chat_audioTitlePaint;
        r0 = r99;
        r6 = (float) r0;
        r8 = android.text.TextUtils.TruncateAt.END;
        r0 = r128;
        r22 = android.text.TextUtils.ellipsize(r0, r4, r6, r8);
        r23 = org.telegram.ui.ActionBar.Theme.chat_audioTitlePaint;
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r24 = r99 + r4;
        r25 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r26 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r27 = 0;
        r28 = 0;
        r21.<init>(r22, r23, r24, r25, r26, r27, r28);
        r0 = r21;
        r1 = r146;
        r1.titleLayout = r0;
        r21 = new android.text.StaticLayout;
        r4 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r0 = r99;
        r6 = (float) r0;
        r8 = android.text.TextUtils.TruncateAt.END;
        r0 = r130;
        r22 = android.text.TextUtils.ellipsize(r0, r4, r6, r8);
        r23 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r24 = r99 + r4;
        r25 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r26 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r27 = 0;
        r28 = 0;
        r21.<init>(r22, r23, r24, r25, r26, r27, r28);
        r0 = r21;
        r1 = r146;
        r1.docTitleLayout = r0;
        r146.setMessageObjectInternal(r147);
        r4 = 1115815936; // 0x42820000 float:65.0 double:5.51286321E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r146;
        r6 = r0.namesOffset;
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.drawPinnedTop;
        if (r4 == 0) goto L_0x11a0;
    L_0x1d3c:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.namesOffset = r4;
        goto L_0x11a0;
    L_0x1d4d:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x1c37;
    L_0x1d51:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r4.x;
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x1d7f;
    L_0x1d5b:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x1d7f;
    L_0x1d61:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x1d7f;
    L_0x1d67:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x1d69:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x1c4b;
    L_0x1d7f:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x1d69;
    L_0x1d82:
        r4 = "CallMessageOutgoing";
        r6 = 2131493094; // 0x7f0c00e6 float:1.8609658E38 double:1.053097512E-314;
        r128 = org.telegram.messenger.LocaleController.getString(r4, r6);
        goto L_0x1ca8;
    L_0x1d8e:
        if (r80 == 0) goto L_0x1d9c;
    L_0x1d90:
        r4 = "CallMessageIncomingMissed";
        r6 = 2131493093; // 0x7f0c00e5 float:1.8609656E38 double:1.0530975116E-314;
        r128 = org.telegram.messenger.LocaleController.getString(r4, r6);
        goto L_0x1ca8;
    L_0x1d9c:
        r0 = r56;
        r4 = r0.reason;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
        if (r4 == 0) goto L_0x1db0;
    L_0x1da4:
        r4 = "CallMessageIncomingDeclined";
        r6 = 2131493092; // 0x7f0c00e4 float:1.8609654E38 double:1.053097511E-314;
        r128 = org.telegram.messenger.LocaleController.getString(r4, r6);
        goto L_0x1ca8;
    L_0x1db0:
        r4 = "CallMessageIncoming";
        r6 = 2131493091; // 0x7f0c00e3 float:1.8609652E38 double:1.0530975106E-314;
        r128 = org.telegram.messenger.LocaleController.getString(r4, r6);
        goto L_0x1ca8;
    L_0x1dbc:
        r0 = r147;
        r4 = r0.type;
        r6 = 12;
        if (r4 != r6) goto L_0x201e;
    L_0x1dc4:
        r4 = 0;
        r0 = r146;
        r0.drawName = r4;
        r4 = 1;
        r0 = r146;
        r0.drawForwardedName = r4;
        r4 = 1;
        r0 = r146;
        r0.drawPhotoImage = r4;
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4.setRoundRadius(r6);
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x1fb3;
    L_0x1de6:
        r6 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x1faf;
    L_0x1df0:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x1faf;
    L_0x1df6:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x1faf;
    L_0x1dfc:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x1dfe:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x1e12:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.availableTimeWidth = r4;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r0 = r4.user_id;
        r137 = r0;
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r6 = java.lang.Integer.valueOf(r137);
        r138 = r4.getUser(r6);
        r4 = r146.getMaxNameWidth();
        r6 = 1121714176; // 0x42dc0000 float:110.0 double:5.54200439E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r26 = r4 - r6;
        if (r26 >= 0) goto L_0x3854;
    L_0x1e49:
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r26 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r99 = r26;
    L_0x1e51:
        r22 = 0;
        if (r138 == 0) goto L_0x1e6c;
    L_0x1e55:
        r0 = r138;
        r4 = r0.photo;
        if (r4 == 0) goto L_0x1e63;
    L_0x1e5b:
        r0 = r138;
        r4 = r0.photo;
        r0 = r4.photo_small;
        r22 = r0;
    L_0x1e63:
        r0 = r146;
        r4 = r0.contactAvatarDrawable;
        r0 = r138;
        r4.setInfo(r0);
    L_0x1e6c:
        r0 = r146;
        r0 = r0.photoImage;
        r21 = r0;
        r23 = "50_50";
        if (r138 == 0) goto L_0x1fe4;
    L_0x1e77:
        r0 = r146;
        r0 = r0.contactAvatarDrawable;
        r24 = r0;
    L_0x1e7d:
        r25 = 0;
        r26 = 0;
        r21.setImage(r22, r23, r24, r25, r26);
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r0 = r4.phone_number;
        r109 = r0;
        if (r109 == 0) goto L_0x1ff3;
    L_0x1e90:
        r4 = r109.length();
        if (r4 == 0) goto L_0x1ff3;
    L_0x1e96:
        r4 = org.telegram.PhoneFormat.PhoneFormat.getInstance();
        r0 = r109;
        r109 = r4.format(r0);
    L_0x1ea0:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.first_name;
        r0 = r147;
        r6 = r0.messageOwner;
        r6 = r6.media;
        r6 = r6.last_name;
        r4 = org.telegram.messenger.ContactsController.formatName(r4, r6);
        r6 = 10;
        r8 = 32;
        r62 = r4.replace(r6, r8);
        r4 = r62.length();
        if (r4 != 0) goto L_0x1ec4;
    L_0x1ec2:
        r62 = r109;
    L_0x1ec4:
        r23 = new android.text.StaticLayout;
        r4 = org.telegram.ui.ActionBar.Theme.chat_contactNamePaint;
        r0 = r99;
        r6 = (float) r0;
        r8 = android.text.TextUtils.TruncateAt.END;
        r0 = r62;
        r24 = android.text.TextUtils.ellipsize(r0, r4, r6, r8);
        r25 = org.telegram.ui.ActionBar.Theme.chat_contactNamePaint;
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r26 = r99 + r4;
        r27 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 0;
        r30 = 0;
        r23.<init>(r24, r25, r26, r27, r28, r29, r30);
        r0 = r23;
        r1 = r146;
        r1.titleLayout = r0;
        r23 = new android.text.StaticLayout;
        r4 = 10;
        r6 = 32;
        r0 = r109;
        r4 = r0.replace(r4, r6);
        r6 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r0 = r99;
        r8 = (float) r0;
        r9 = android.text.TextUtils.TruncateAt.END;
        r24 = android.text.TextUtils.ellipsize(r4, r6, r8, r9);
        r25 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r26 = r99 + r4;
        r27 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 0;
        r30 = 0;
        r23.<init>(r24, r25, r26, r27, r28, r29, r30);
        r0 = r23;
        r1 = r146;
        r1.docTitleLayout = r0;
        r146.setMessageObjectInternal(r147);
        r0 = r146;
        r4 = r0.drawForwardedName;
        if (r4 == 0) goto L_0x1fff;
    L_0x1f29:
        r4 = r147.needDrawForwarded();
        if (r4 == 0) goto L_0x1fff;
    L_0x1f2f:
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 == 0) goto L_0x1f3d;
    L_0x1f35:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.minY;
        if (r4 != 0) goto L_0x1fff;
    L_0x1f3d:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.namesOffset = r4;
    L_0x1f4c:
        r4 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r146;
        r6 = r0.namesOffset;
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.drawPinnedTop;
        if (r4 == 0) goto L_0x1f70;
    L_0x1f61:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.namesOffset = r4;
    L_0x1f70:
        r0 = r146;
        r4 = r0.docTitleLayout;
        r4 = r4.getLineCount();
        if (r4 <= 0) goto L_0x11a0;
    L_0x1f7a:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1121714176; // 0x42dc0000 float:110.0 double:5.54200439E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r6 = r0.docTitleLayout;
        r8 = 0;
        r6 = r6.getLineWidth(r8);
        r8 = (double) r6;
        r8 = java.lang.Math.ceil(r8);
        r6 = (int) r8;
        r131 = r4 - r6;
        r0 = r146;
        r4 = r0.timeWidth;
        r0 = r131;
        if (r0 >= r4) goto L_0x11a0;
    L_0x1f9e:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        goto L_0x11a0;
    L_0x1faf:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x1dfe;
    L_0x1fb3:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r4.x;
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x1fe1;
    L_0x1fbd:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x1fe1;
    L_0x1fc3:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x1fe1;
    L_0x1fc9:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x1fcb:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x1e12;
    L_0x1fe1:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x1fcb;
    L_0x1fe4:
        r6 = org.telegram.ui.ActionBar.Theme.chat_contactDrawable;
        r4 = r147.isOutOwner();
        if (r4 == 0) goto L_0x1ff1;
    L_0x1fec:
        r4 = 1;
    L_0x1fed:
        r24 = r6[r4];
        goto L_0x1e7d;
    L_0x1ff1:
        r4 = 0;
        goto L_0x1fed;
    L_0x1ff3:
        r4 = "NumberUnknown";
        r6 = 2131493976; // 0x7f0c0458 float:1.8611447E38 double:1.053097948E-314;
        r109 = org.telegram.messenger.LocaleController.getString(r4, r6);
        goto L_0x1ea0;
    L_0x1fff:
        r0 = r146;
        r4 = r0.drawNameLayout;
        if (r4 == 0) goto L_0x1f4c;
    L_0x2005:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.reply_to_msg_id;
        if (r4 != 0) goto L_0x1f4c;
    L_0x200d:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.namesOffset = r4;
        goto L_0x1f4c;
    L_0x201e:
        r0 = r147;
        r4 = r0.type;
        r6 = 2;
        if (r4 != r6) goto L_0x20c3;
    L_0x2025:
        r4 = 1;
        r0 = r146;
        r0.drawForwardedName = r4;
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x2093;
    L_0x2030:
        r6 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x2090;
    L_0x203a:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x2090;
    L_0x2040:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x2090;
    L_0x2046:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x2048:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x205c:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r0 = r146;
        r1 = r147;
        r0.createDocumentLayout(r4, r1);
        r146.setMessageObjectInternal(r147);
        r4 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r146;
        r6 = r0.namesOffset;
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.drawPinnedTop;
        if (r4 == 0) goto L_0x11a0;
    L_0x207f:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.namesOffset = r4;
        goto L_0x11a0;
    L_0x2090:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x2048;
    L_0x2093:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r4.x;
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x20c0;
    L_0x209d:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x20c0;
    L_0x20a3:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x20c0;
    L_0x20a9:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x20ab:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x205c;
    L_0x20c0:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x20ab;
    L_0x20c3:
        r0 = r147;
        r4 = r0.type;
        r6 = 14;
        if (r4 != r6) goto L_0x2164;
    L_0x20cb:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x2134;
    L_0x20d1:
        r6 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x2131;
    L_0x20db:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x2131;
    L_0x20e1:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x2131;
    L_0x20e7:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x20e9:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x20fd:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r0 = r146;
        r1 = r147;
        r0.createDocumentLayout(r4, r1);
        r146.setMessageObjectInternal(r147);
        r4 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r146;
        r6 = r0.namesOffset;
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.drawPinnedTop;
        if (r4 == 0) goto L_0x11a0;
    L_0x2120:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.namesOffset = r4;
        goto L_0x11a0;
    L_0x2131:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x20e9;
    L_0x2134:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r4.x;
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x2161;
    L_0x213e:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x2161;
    L_0x2144:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x2161;
    L_0x214a:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x214c:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x20fd;
    L_0x2161:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x214c;
    L_0x2164:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.fwd_from;
        if (r4 == 0) goto L_0x23ab;
    L_0x216c:
        r0 = r147;
        r4 = r0.type;
        r6 = 13;
        if (r4 == r6) goto L_0x23ab;
    L_0x2174:
        r4 = 1;
    L_0x2175:
        r0 = r146;
        r0.drawForwardedName = r4;
        r0 = r147;
        r4 = r0.type;
        r6 = 9;
        if (r4 == r6) goto L_0x23ae;
    L_0x2181:
        r4 = 1;
    L_0x2182:
        r0 = r146;
        r0.mediaBackground = r4;
        r4 = 1;
        r0 = r146;
        r0.drawImageButton = r4;
        r4 = 1;
        r0 = r146;
        r0.drawPhotoImage = r4;
        r113 = 0;
        r112 = 0;
        r43 = 0;
        r0 = r147;
        r4 = r0.gifState;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 == 0) goto L_0x21b9;
    L_0x21a0:
        r4 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r4 != 0) goto L_0x21b9;
    L_0x21a4:
        r0 = r147;
        r4 = r0.type;
        r6 = 8;
        if (r4 == r6) goto L_0x21b3;
    L_0x21ac:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x21b9;
    L_0x21b3:
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r0 = r147;
        r0.gifState = r4;
    L_0x21b9:
        r4 = r147.isRoundVideo();
        if (r4 == 0) goto L_0x23b4;
    L_0x21bf:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1;
        r4.setAllowDecodeSingleFrame(r6);
        r0 = r146;
        r6 = r0.photoImage;
        r4 = org.telegram.messenger.MediaController.getInstance();
        r4 = r4.getPlayingMessageObject();
        if (r4 != 0) goto L_0x23b1;
    L_0x21d5:
        r4 = 1;
    L_0x21d6:
        r6.setAllowStartAnimation(r4);
    L_0x21d9:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = r147.isSecretPhoto();
        r4.setForcePreview(r6);
        r0 = r147;
        r4 = r0.type;
        r6 = 9;
        if (r4 != r6) goto L_0x2421;
    L_0x21ec:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x23cd;
    L_0x21f2:
        r6 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x23c9;
    L_0x21fc:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x23c9;
    L_0x2202:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x23c9;
    L_0x2208:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x220a:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x221e:
        r4 = r146.checkNeedDrawShareButton(r147);
        if (r4 == 0) goto L_0x2233;
    L_0x2224:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x2233:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1124728832; // 0x430a0000 float:138.0 double:5.55689877E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r26 = r4 - r6;
        r0 = r146;
        r1 = r26;
        r2 = r147;
        r0.createDocumentLayout(r1, r2);
        r0 = r147;
        r4 = r0.caption;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x225a;
    L_0x2252:
        r4 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r26 = r26 + r4;
    L_0x225a:
        r0 = r146;
        r4 = r0.drawPhotoImage;
        if (r4 == 0) goto L_0x23fe;
    L_0x2260:
        r4 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r113 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r112 = org.telegram.messenger.AndroidUtilities.dp(r4);
    L_0x226c:
        r0 = r26;
        r1 = r146;
        r1.availableTimeWidth = r0;
        r0 = r146;
        r4 = r0.drawPhotoImage;
        if (r4 != 0) goto L_0x22bb;
    L_0x2278:
        r0 = r147;
        r4 = r0.caption;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x22bb;
    L_0x2282:
        r0 = r146;
        r4 = r0.infoLayout;
        r4 = r4.getLineCount();
        if (r4 <= 0) goto L_0x22bb;
    L_0x228c:
        r146.measureTime(r147);
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1123287040; // 0x42f40000 float:122.0 double:5.54977537E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r6 = r0.infoLayout;
        r8 = 0;
        r6 = r6.getLineWidth(r8);
        r8 = (double) r6;
        r8 = java.lang.Math.ceil(r8);
        r6 = (int) r8;
        r131 = r4 - r6;
        r0 = r146;
        r4 = r0.timeWidth;
        r0 = r131;
        if (r0 >= r4) goto L_0x22bb;
    L_0x22b3:
        r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r112 = r112 + r4;
    L_0x22bb:
        r146.setMessageObjectInternal(r147);
        r0 = r146;
        r4 = r0.drawForwardedName;
        if (r4 == 0) goto L_0x3434;
    L_0x22c4:
        r4 = r147.needDrawForwarded();
        if (r4 == 0) goto L_0x3434;
    L_0x22ca:
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 == 0) goto L_0x22d8;
    L_0x22d0:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.minY;
        if (r4 != 0) goto L_0x3434;
    L_0x22d8:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 == r6) goto L_0x22ee;
    L_0x22df:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.namesOffset = r4;
    L_0x22ee:
        r4 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 + r112;
        r0 = r146;
        r6 = r0.namesOffset;
        r4 = r4 + r6;
        r4 = r4 + r43;
        r0 = r146;
        r0.totalHeight = r4;
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 == 0) goto L_0x2320;
    L_0x2307:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.flags;
        r4 = r4 & 8;
        if (r4 != 0) goto L_0x2320;
    L_0x2311:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.totalHeight = r4;
    L_0x2320:
        r45 = 0;
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 == 0) goto L_0x2378;
    L_0x2328:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.flags;
        r4 = r4 & 2;
        if (r4 != 0) goto L_0x233a;
    L_0x2332:
        r4 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r113 = r113 + r4;
    L_0x233a:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.flags;
        r4 = r4 & 1;
        if (r4 != 0) goto L_0x234c;
    L_0x2344:
        r4 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r113 = r113 + r4;
    L_0x234c:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.flags;
        r4 = r4 & 4;
        if (r4 != 0) goto L_0x2366;
    L_0x2356:
        r4 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r112 = r112 + r4;
        r4 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r45 = r45 - r4;
    L_0x2366:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.flags;
        r4 = r4 & 8;
        if (r4 != 0) goto L_0x2378;
    L_0x2370:
        r4 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r112 = r112 + r4;
    L_0x2378:
        r0 = r146;
        r4 = r0.drawPinnedTop;
        if (r4 == 0) goto L_0x238d;
    L_0x237e:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.namesOffset = r4;
    L_0x238d:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 0;
        r8 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r0 = r146;
        r9 = r0.namesOffset;
        r8 = r8 + r9;
        r8 = r8 + r45;
        r0 = r113;
        r1 = r112;
        r4.setImageCoords(r6, r8, r0, r1);
        r146.invalidate();
        goto L_0x11a0;
    L_0x23ab:
        r4 = 0;
        goto L_0x2175;
    L_0x23ae:
        r4 = 0;
        goto L_0x2182;
    L_0x23b1:
        r4 = 0;
        goto L_0x21d6;
    L_0x23b4:
        r0 = r146;
        r6 = r0.photoImage;
        r0 = r147;
        r4 = r0.gifState;
        r8 = 0;
        r4 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1));
        if (r4 != 0) goto L_0x23c7;
    L_0x23c1:
        r4 = 1;
    L_0x23c2:
        r6.setAllowStartAnimation(r4);
        goto L_0x21d9;
    L_0x23c7:
        r4 = 0;
        goto L_0x23c2;
    L_0x23c9:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x220a;
    L_0x23cd:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r4.x;
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x23fb;
    L_0x23d7:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x23fb;
    L_0x23dd:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x23fb;
    L_0x23e3:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x23e5:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x221e;
    L_0x23fb:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x23e5;
    L_0x23fe:
        r4 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r113 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r112 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r147;
        r4 = r0.caption;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x241e;
    L_0x2414:
        r4 = 1112276992; // 0x424c0000 float:51.0 double:5.495378504E-315;
    L_0x2416:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r26 = r26 + r4;
        goto L_0x226c;
    L_0x241e:
        r4 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        goto L_0x2416;
    L_0x2421:
        r0 = r147;
        r4 = r0.type;
        r6 = 4;
        if (r4 != r6) goto L_0x28af;
    L_0x2428:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.geo;
        r0 = r4.lat;
        r84 = r0;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.geo;
        r0 = r4._long;
        r90 = r0;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r4 == 0) goto L_0x26e1;
    L_0x244a:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x2670;
    L_0x2450:
        r6 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x266c;
    L_0x245a:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x266c;
    L_0x2460:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x266c;
    L_0x2466:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x2468:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x247d:
        r4 = r146.checkNeedDrawShareButton(r147);
        if (r4 == 0) goto L_0x2492;
    L_0x2483:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x2492:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1108606976; // 0x42140000 float:37.0 double:5.477246216E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r26 = r4 - r6;
        r0 = r26;
        r1 = r146;
        r1.availableTimeWidth = r0;
        r4 = 1113063424; // 0x42580000 float:54.0 double:5.499263994E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r26 = r26 - r4;
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r113 = r4 - r6;
        r4 = 1128464384; // 0x43430000 float:195.0 double:5.575354847E-315;
        r112 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r105 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r0 = r105;
        r8 = (double) r0;
        r10 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r116 = r8 / r10;
        r0 = r105;
        r8 = (double) r0;
        r10 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r18 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r18 = r18 * r84;
        r24 = 4640537203540230144; // 0x4066800000000000 float:0.0 double:180.0;
        r18 = r18 / r24;
        r18 = java.lang.Math.sin(r18);
        r10 = r10 + r18;
        r18 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r24 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r24 = r24 * r84;
        r34 = 4640537203540230144; // 0x4066800000000000 float:0.0 double:180.0;
        r24 = r24 / r34;
        r24 = java.lang.Math.sin(r24);
        r18 = r18 - r24;
        r10 = r10 / r18;
        r10 = java.lang.Math.log(r10);
        r10 = r10 * r116;
        r18 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
        r10 = r10 / r18;
        r8 = r8 - r10;
        r8 = java.lang.Math.round(r8);
        r4 = 1092930765; // 0x4124cccd float:10.3 double:5.399795443E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 << 6;
        r10 = (long) r4;
        r8 = r8 - r10;
        r0 = (double) r8;
        r144 = r0;
        r8 = 4609753056924675352; // 0x3ff921fb54442d18 float:3.37028055E12 double:1.5707963267948966;
        r10 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
        r0 = r105;
        r0 = (double) r0;
        r18 = r0;
        r18 = r144 - r18;
        r18 = r18 / r116;
        r18 = java.lang.Math.exp(r18);
        r18 = java.lang.Math.atan(r18);
        r10 = r10 * r18;
        r8 = r8 - r10;
        r10 = 4640537203540230144; // 0x4066800000000000 float:0.0 double:180.0;
        r8 = r8 * r10;
        r10 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r84 = r8 / r10;
        r4 = java.util.Locale.US;
        r6 = "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=%dx%d&maptype=roadmap&scale=%d&sensor=false";
        r8 = 5;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r10 = java.lang.Double.valueOf(r84);
        r8[r9] = r10;
        r9 = 1;
        r10 = java.lang.Double.valueOf(r90);
        r8[r9] = r10;
        r9 = 2;
        r0 = r113;
        r10 = (float) r0;
        r11 = org.telegram.messenger.AndroidUtilities.density;
        r10 = r10 / r11;
        r10 = (int) r10;
        r10 = java.lang.Integer.valueOf(r10);
        r8[r9] = r10;
        r9 = 3;
        r0 = r112;
        r10 = (float) r0;
        r11 = org.telegram.messenger.AndroidUtilities.density;
        r10 = r10 / r11;
        r10 = (int) r10;
        r10 = java.lang.Integer.valueOf(r10);
        r8[r9] = r10;
        r9 = 4;
        r10 = 2;
        r11 = org.telegram.messenger.AndroidUtilities.density;
        r0 = (double) r11;
        r18 = r0;
        r18 = java.lang.Math.ceil(r18);
        r0 = r18;
        r11 = (int) r0;
        r10 = java.lang.Math.min(r10, r11);
        r10 = java.lang.Integer.valueOf(r10);
        r8[r9] = r10;
        r4 = java.lang.String.format(r4, r6, r8);
        r0 = r146;
        r0.currentUrl = r4;
        r4 = r146.isCurrentLocationTimeExpired(r147);
        r0 = r146;
        r0.locationExpired = r4;
        if (r4 != 0) goto L_0x26a2;
    L_0x259c:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1;
        r4.setCrossfadeWithOldImage(r6);
        r4 = 0;
        r0 = r146;
        r0.mediaBackground = r4;
        r4 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r43 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r146;
        r4 = r0.invalidateRunnable;
        r8 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4, r8);
        r4 = 1;
        r0 = r146;
        r0.scheduledInvalidate = r4;
    L_0x25bd:
        r23 = new android.text.StaticLayout;
        r4 = "AttachLiveLocation";
        r6 = 2131493027; // 0x7f0c00a3 float:1.8609523E38 double:1.053097479E-314;
        r24 = org.telegram.messenger.LocaleController.getString(r4, r6);
        r25 = org.telegram.ui.ActionBar.Theme.chat_locationTitlePaint;
        r27 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 0;
        r30 = 0;
        r23.<init>(r24, r25, r26, r27, r28, r29, r30);
        r0 = r23;
        r1 = r146;
        r1.docTitleLayout = r0;
        r22 = 0;
        r146.updateCurrentUserAndChat();
        r0 = r146;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x26b3;
    L_0x25e7:
        r0 = r146;
        r4 = r0.currentUser;
        r4 = r4.photo;
        if (r4 == 0) goto L_0x25f9;
    L_0x25ef:
        r0 = r146;
        r4 = r0.currentUser;
        r4 = r4.photo;
        r0 = r4.photo_small;
        r22 = r0;
    L_0x25f9:
        r0 = r146;
        r4 = r0.contactAvatarDrawable;
        r0 = r146;
        r6 = r0.currentUser;
        r4.setInfo(r6);
    L_0x2604:
        r0 = r146;
        r0 = r0.locationImageReceiver;
        r27 = r0;
        r29 = "50_50";
        r0 = r146;
        r0 = r0.contactAvatarDrawable;
        r30 = r0;
        r31 = 0;
        r32 = 0;
        r28 = r22;
        r27.setImage(r28, r29, r30, r31, r32);
        r23 = new android.text.StaticLayout;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.edit_date;
        if (r4 == 0) goto L_0x26d8;
    L_0x2626:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.edit_date;
        r8 = (long) r4;
    L_0x262d:
        r24 = org.telegram.messenger.LocaleController.formatLocationUpdateDate(r8);
        r25 = org.telegram.ui.ActionBar.Theme.chat_locationAddressPaint;
        r27 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 0;
        r30 = 0;
        r23.<init>(r24, r25, r26, r27, r28, r29, r30);
        r0 = r23;
        r1 = r146;
        r1.infoLayout = r0;
    L_0x2644:
        r0 = r146;
        r4 = r0.currentUrl;
        if (r4 == 0) goto L_0x22bb;
    L_0x264a:
        r0 = r146;
        r0 = r0.photoImage;
        r27 = r0;
        r0 = r146;
        r0 = r0.currentUrl;
        r28 = r0;
        r29 = 0;
        r6 = org.telegram.ui.ActionBar.Theme.chat_locationDrawable;
        r4 = r147.isOutOwner();
        if (r4 == 0) goto L_0x28ac;
    L_0x2660:
        r4 = 1;
    L_0x2661:
        r30 = r6[r4];
        r31 = 0;
        r32 = 0;
        r27.setImage(r28, r29, r30, r31, r32);
        goto L_0x22bb;
    L_0x266c:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x2468;
    L_0x2670:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r4.x;
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x269f;
    L_0x267a:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x269f;
    L_0x2680:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x269f;
    L_0x2686:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x2688:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x247d;
    L_0x269f:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x2688;
    L_0x26a2:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x25bd;
    L_0x26b3:
        r0 = r146;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x2604;
    L_0x26b9:
        r0 = r146;
        r4 = r0.currentChat;
        r4 = r4.photo;
        if (r4 == 0) goto L_0x26cb;
    L_0x26c1:
        r0 = r146;
        r4 = r0.currentChat;
        r4 = r4.photo;
        r0 = r4.photo_small;
        r22 = r0;
    L_0x26cb:
        r0 = r146;
        r4 = r0.contactAvatarDrawable;
        r0 = r146;
        r6 = r0.currentChat;
        r4.setInfo(r6);
        goto L_0x2604;
    L_0x26d8:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.date;
        r8 = (long) r4;
        goto L_0x262d;
    L_0x26e1:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.title;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x2844;
    L_0x26ef:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x280c;
    L_0x26f5:
        r6 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x2808;
    L_0x26ff:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x2808;
    L_0x2705:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x2808;
    L_0x270b:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x270d:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x2721:
        r4 = r146.checkNeedDrawShareButton(r147);
        if (r4 == 0) goto L_0x2736;
    L_0x2727:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x2736:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1123418112; // 0x42f60000 float:123.0 double:5.55042295E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r26 = r4 - r6;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r0 = r4.title;
        r24 = r0;
        r25 = org.telegram.ui.ActionBar.Theme.chat_locationTitlePaint;
        r27 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 0;
        r30 = 0;
        r31 = android.text.TextUtils.TruncateAt.END;
        r33 = 2;
        r32 = r26;
        r4 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r24, r25, r26, r27, r28, r29, r30, r31, r32, r33);
        r0 = r146;
        r0.docTitleLayout = r4;
        r0 = r146;
        r4 = r0.docTitleLayout;
        r83 = r4.getLineCount();
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.address;
        if (r4 == 0) goto L_0x283d;
    L_0x2776:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.address;
        r4 = r4.length();
        if (r4 <= 0) goto L_0x283d;
    L_0x2784:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r0 = r4.address;
        r24 = r0;
        r25 = org.telegram.ui.ActionBar.Theme.chat_locationAddressPaint;
        r27 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 0;
        r30 = 0;
        r31 = android.text.TextUtils.TruncateAt.END;
        r4 = 3;
        r6 = 3 - r83;
        r33 = java.lang.Math.min(r4, r6);
        r32 = r26;
        r4 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r24, r25, r26, r27, r28, r29, r30, r31, r32, r33);
        r0 = r146;
        r0.infoLayout = r4;
    L_0x27ab:
        r4 = 0;
        r0 = r146;
        r0.mediaBackground = r4;
        r0 = r26;
        r1 = r146;
        r1.availableTimeWidth = r0;
        r4 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r113 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r112 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = java.util.Locale.US;
        r6 = "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:mid|%f,%f&sensor=false";
        r8 = 5;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r10 = java.lang.Double.valueOf(r84);
        r8[r9] = r10;
        r9 = 1;
        r10 = java.lang.Double.valueOf(r90);
        r8[r9] = r10;
        r9 = 2;
        r10 = 2;
        r11 = org.telegram.messenger.AndroidUtilities.density;
        r0 = (double) r11;
        r18 = r0;
        r18 = java.lang.Math.ceil(r18);
        r0 = r18;
        r11 = (int) r0;
        r10 = java.lang.Math.min(r10, r11);
        r10 = java.lang.Integer.valueOf(r10);
        r8[r9] = r10;
        r9 = 3;
        r10 = java.lang.Double.valueOf(r84);
        r8[r9] = r10;
        r9 = 4;
        r10 = java.lang.Double.valueOf(r90);
        r8[r9] = r10;
        r4 = java.lang.String.format(r4, r6, r8);
        r0 = r146;
        r0.currentUrl = r4;
        goto L_0x2644;
    L_0x2808:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x270d;
    L_0x280c:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r4.x;
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x283a;
    L_0x2816:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x283a;
    L_0x281c:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x283a;
    L_0x2822:
        r4 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
    L_0x2824:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = java.lang.Math.min(r4, r6);
        r0 = r146;
        r0.backgroundWidth = r4;
        goto L_0x2721;
    L_0x283a:
        r4 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        goto L_0x2824;
    L_0x283d:
        r4 = 0;
        r0 = r146;
        r0.infoLayout = r4;
        goto L_0x27ab;
    L_0x2844:
        r4 = 1127874560; // 0x433a0000 float:186.0 double:5.57244073E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r146;
        r0.availableTimeWidth = r4;
        r4 = 1128792064; // 0x43480000 float:200.0 double:5.5769738E-315;
        r113 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r112 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 + r113;
        r0 = r146;
        r0.backgroundWidth = r4;
        r4 = java.util.Locale.US;
        r6 = "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=200x100&maptype=roadmap&scale=%d&markers=color:red|size:mid|%f,%f&sensor=false";
        r8 = 5;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r10 = java.lang.Double.valueOf(r84);
        r8[r9] = r10;
        r9 = 1;
        r10 = java.lang.Double.valueOf(r90);
        r8[r9] = r10;
        r9 = 2;
        r10 = 2;
        r11 = org.telegram.messenger.AndroidUtilities.density;
        r0 = (double) r11;
        r18 = r0;
        r18 = java.lang.Math.ceil(r18);
        r0 = r18;
        r11 = (int) r0;
        r10 = java.lang.Math.min(r10, r11);
        r10 = java.lang.Integer.valueOf(r10);
        r8[r9] = r10;
        r9 = 3;
        r10 = java.lang.Double.valueOf(r84);
        r8[r9] = r10;
        r9 = 4;
        r10 = java.lang.Double.valueOf(r90);
        r8[r9] = r10;
        r4 = java.lang.String.format(r4, r6, r8);
        r0 = r146;
        r0.currentUrl = r4;
        goto L_0x2644;
    L_0x28ac:
        r4 = 0;
        goto L_0x2661;
    L_0x28af:
        r0 = r147;
        r4 = r0.type;
        r6 = 13;
        if (r4 != r6) goto L_0x2a46;
    L_0x28b7:
        r4 = 0;
        r0 = r146;
        r0.drawBackground = r4;
        r41 = 0;
    L_0x28be:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r4 = r4.attributes;
        r4 = r4.size();
        r0 = r41;
        if (r0 >= r4) goto L_0x28f4;
    L_0x28d0:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r4 = r4.attributes;
        r0 = r41;
        r46 = r4.get(r0);
        r46 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r46;
        r0 = r46;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r4 == 0) goto L_0x29c2;
    L_0x28e8:
        r0 = r46;
        r0 = r0.w;
        r113 = r0;
        r0 = r46;
        r0 = r0.h;
        r112 = r0;
    L_0x28f4:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x29c6;
    L_0x28fa:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = (float) r4;
        r6 = 1053609165; // 0x3ecccccd float:0.4 double:5.205520926E-315;
        r26 = r4 * r6;
        r96 = r26;
    L_0x2906:
        if (r113 != 0) goto L_0x2915;
    L_0x2908:
        r0 = r96;
        r0 = (int) r0;
        r112 = r0;
        r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r113 = r112 + r4;
    L_0x2915:
        r0 = r112;
        r4 = (float) r0;
        r0 = r113;
        r6 = (float) r0;
        r6 = r26 / r6;
        r4 = r4 * r6;
        r0 = (int) r4;
        r112 = r0;
        r0 = r26;
        r0 = (int) r0;
        r113 = r0;
        r0 = r112;
        r4 = (float) r0;
        r4 = (r4 > r96 ? 1 : (r4 == r96 ? 0 : -1));
        if (r4 <= 0) goto L_0x293e;
    L_0x292d:
        r0 = r113;
        r4 = (float) r0;
        r0 = r112;
        r6 = (float) r0;
        r6 = r96 / r6;
        r4 = r4 * r6;
        r0 = (int) r4;
        r113 = r0;
        r0 = r96;
        r0 = (int) r0;
        r112 = r0;
    L_0x293e:
        r4 = 6;
        r0 = r146;
        r0.documentAttachType = r4;
        r4 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r113 - r4;
        r0 = r146;
        r0.availableTimeWidth = r4;
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 + r113;
        r0 = r146;
        r0.backgroundWidth = r4;
        r0 = r147;
        r4 = r0.photoThumbs;
        r6 = 80;
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r6);
        r0 = r146;
        r0.currentPhotoObjectThumb = r4;
        r0 = r147;
        r4 = r0.attachPathExists;
        if (r4 == 0) goto L_0x29de;
    L_0x296f:
        r0 = r146;
        r0 = r0.photoImage;
        r27 = r0;
        r28 = 0;
        r0 = r147;
        r4 = r0.messageOwner;
        r0 = r4.attachPath;
        r29 = r0;
        r4 = java.util.Locale.US;
        r6 = "%d_%d";
        r8 = 2;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r10 = java.lang.Integer.valueOf(r113);
        r8[r9] = r10;
        r9 = 1;
        r10 = java.lang.Integer.valueOf(r112);
        r8[r9] = r10;
        r30 = java.lang.String.format(r4, r6, r8);
        r31 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        if (r4 == 0) goto L_0x29db;
    L_0x29a1:
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        r0 = r4.location;
        r32 = r0;
    L_0x29a9:
        r33 = "b1";
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r0 = r4.size;
        r34 = r0;
        r35 = "webp";
        r36 = 1;
        r27.setImage(r28, r29, r30, r31, r32, r33, r34, r35, r36);
        goto L_0x22bb;
    L_0x29c2:
        r41 = r41 + 1;
        goto L_0x28be;
    L_0x29c6:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r4 = java.lang.Math.min(r4, r6);
        r4 = (float) r4;
        r6 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r26 = r4 * r6;
        r96 = r26;
        goto L_0x2906;
    L_0x29db:
        r32 = 0;
        goto L_0x29a9;
    L_0x29de:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r8 = r4.id;
        r10 = 0;
        r4 = (r8 > r10 ? 1 : (r8 == r10 ? 0 : -1));
        if (r4 == 0) goto L_0x22bb;
    L_0x29ee:
        r0 = r146;
        r0 = r0.photoImage;
        r27 = r0;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r0 = r4.document;
        r28 = r0;
        r29 = 0;
        r4 = java.util.Locale.US;
        r6 = "%d_%d";
        r8 = 2;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r10 = java.lang.Integer.valueOf(r113);
        r8[r9] = r10;
        r9 = 1;
        r10 = java.lang.Integer.valueOf(r112);
        r8[r9] = r10;
        r30 = java.lang.String.format(r4, r6, r8);
        r31 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        if (r4 == 0) goto L_0x2a43;
    L_0x2a22:
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        r0 = r4.location;
        r32 = r0;
    L_0x2a2a:
        r33 = "b1";
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r0 = r4.size;
        r34 = r0;
        r35 = "webp";
        r36 = 1;
        r27.setImage(r28, r29, r30, r31, r32, r33, r34, r35, r36);
        goto L_0x22bb;
    L_0x2a43:
        r32 = 0;
        goto L_0x2a2a;
    L_0x2a46:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x2c33;
    L_0x2a4d:
        r113 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r97 = r113;
    L_0x2a51:
        r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r112 = r113 + r4;
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 == r6) goto L_0x2a76;
    L_0x2a60:
        r4 = r146.checkNeedDrawShareButton(r147);
        if (r4 == 0) goto L_0x2a76;
    L_0x2a66:
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r97 = r97 - r4;
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r113 = r113 - r4;
    L_0x2a76:
        r4 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r0 = r113;
        if (r0 <= r4) goto L_0x2a82;
    L_0x2a7e:
        r113 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
    L_0x2a82:
        r4 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r0 = r112;
        if (r0 <= r4) goto L_0x2a8e;
    L_0x2a8a:
        r112 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
    L_0x2a8e:
        r0 = r147;
        r4 = r0.type;
        r6 = 1;
        if (r4 != r6) goto L_0x2c61;
    L_0x2a95:
        r146.updateSecretTimeText(r147);
        r0 = r147;
        r4 = r0.photoThumbs;
        r6 = 80;
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r6);
        r0 = r146;
        r0.currentPhotoObjectThumb = r4;
    L_0x2aa6:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        if (r4 != 0) goto L_0x2ab7;
    L_0x2aac:
        r0 = r147;
        r4 = r0.caption;
        if (r4 == 0) goto L_0x2ab7;
    L_0x2ab2:
        r4 = 0;
        r0 = r146;
        r0.mediaBackground = r4;
    L_0x2ab7:
        r0 = r147;
        r4 = r0.photoThumbs;
        r6 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r6);
        r0 = r146;
        r0.currentPhotoObject = r4;
        r139 = 0;
        r76 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x2ae0;
    L_0x2ad1:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r146;
        r6 = r0.currentPhotoObjectThumb;
        if (r4 != r6) goto L_0x2ae0;
    L_0x2adb:
        r4 = 0;
        r0 = r146;
        r0.currentPhotoObjectThumb = r4;
    L_0x2ae0:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x2b34;
    L_0x2ae6:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        r4 = (float) r4;
        r0 = r113;
        r6 = (float) r0;
        r122 = r4 / r6;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        r4 = (float) r4;
        r4 = r4 / r122;
        r0 = (int) r4;
        r139 = r0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.h;
        r4 = (float) r4;
        r4 = r4 / r122;
        r0 = (int) r4;
        r76 = r0;
        if (r139 != 0) goto L_0x2b12;
    L_0x2b0c:
        r4 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r139 = org.telegram.messenger.AndroidUtilities.dp(r4);
    L_0x2b12:
        if (r76 != 0) goto L_0x2b1a;
    L_0x2b14:
        r4 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r76 = org.telegram.messenger.AndroidUtilities.dp(r4);
    L_0x2b1a:
        r0 = r76;
        r1 = r112;
        if (r0 <= r1) goto L_0x2d21;
    L_0x2b20:
        r0 = r76;
        r0 = (float) r0;
        r123 = r0;
        r76 = r112;
        r0 = r76;
        r4 = (float) r0;
        r123 = r123 / r4;
        r0 = r139;
        r4 = (float) r0;
        r4 = r4 / r123;
        r0 = (int) r4;
        r139 = r0;
    L_0x2b34:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x2b3f;
    L_0x2b3b:
        r76 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r139 = r76;
    L_0x2b3f:
        if (r139 == 0) goto L_0x2b43;
    L_0x2b41:
        if (r76 != 0) goto L_0x2bb5;
    L_0x2b43:
        r0 = r147;
        r4 = r0.type;
        r6 = 8;
        if (r4 != r6) goto L_0x2bb5;
    L_0x2b4b:
        r41 = 0;
    L_0x2b4d:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r4 = r4.attributes;
        r4 = r4.size();
        r0 = r41;
        if (r0 >= r4) goto L_0x2bb5;
    L_0x2b5f:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r4 = r4.attributes;
        r0 = r41;
        r46 = r4.get(r0);
        r46 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r46;
        r0 = r46;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r4 != 0) goto L_0x2b7d;
    L_0x2b77:
        r0 = r46;
        r4 = r0 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r4 == 0) goto L_0x2d8f;
    L_0x2b7d:
        r0 = r46;
        r4 = r0.w;
        r4 = (float) r4;
        r0 = r113;
        r6 = (float) r0;
        r122 = r4 / r6;
        r0 = r46;
        r4 = r0.w;
        r4 = (float) r4;
        r4 = r4 / r122;
        r0 = (int) r4;
        r139 = r0;
        r0 = r46;
        r4 = r0.h;
        r4 = (float) r4;
        r4 = r4 / r122;
        r0 = (int) r4;
        r76 = r0;
        r0 = r76;
        r1 = r112;
        if (r0 <= r1) goto L_0x2d5b;
    L_0x2ba1:
        r0 = r76;
        r0 = (float) r0;
        r123 = r0;
        r76 = r112;
        r0 = r76;
        r4 = (float) r0;
        r123 = r123 / r4;
        r0 = r139;
        r4 = (float) r0;
        r4 = r4 / r123;
        r0 = (int) r4;
        r139 = r0;
    L_0x2bb5:
        if (r139 == 0) goto L_0x2bb9;
    L_0x2bb7:
        if (r76 != 0) goto L_0x2bc1;
    L_0x2bb9:
        r4 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r76 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r139 = r76;
    L_0x2bc1:
        r0 = r147;
        r4 = r0.type;
        r6 = 3;
        if (r4 != r6) goto L_0x2be3;
    L_0x2bc8:
        r0 = r146;
        r4 = r0.infoWidth;
        r6 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r139;
        if (r0 >= r4) goto L_0x2be3;
    L_0x2bd7:
        r0 = r146;
        r4 = r0.infoWidth;
        r6 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r139 = r4 + r6;
    L_0x2be3:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        if (r4 == 0) goto L_0x2ec1;
    L_0x2be9:
        r72 = 0;
        r63 = r146.getGroupPhotosWidth();
        r41 = 0;
    L_0x2bf1:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        r4 = r4.posArray;
        r4 = r4.size();
        r0 = r41;
        if (r0 >= r4) goto L_0x2d93;
    L_0x2bff:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        r4 = r4.posArray;
        r0 = r41;
        r114 = r4.get(r0);
        r114 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r114;
        r0 = r114;
        r4 = r0.minY;
        if (r4 != 0) goto L_0x2d93;
    L_0x2c13:
        r0 = r72;
        r8 = (double) r0;
        r0 = r114;
        r4 = r0.pw;
        r0 = r114;
        r6 = r0.leftSpanOffset;
        r4 = r4 + r6;
        r4 = (float) r4;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 / r6;
        r0 = r63;
        r6 = (float) r0;
        r4 = r4 * r6;
        r10 = (double) r4;
        r10 = java.lang.Math.ceil(r10);
        r8 = r8 + r10;
        r0 = (int) r8;
        r72 = r0;
        r41 = r41 + 1;
        goto L_0x2bf1;
    L_0x2c33:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x2c49;
    L_0x2c39:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = (float) r4;
        r6 = 1060320051; // 0x3f333333 float:0.7 double:5.23867711E-315;
        r4 = r4 * r6;
        r0 = (int) r4;
        r113 = r0;
        r97 = r113;
        goto L_0x2a51;
    L_0x2c49:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r4 = java.lang.Math.min(r4, r6);
        r4 = (float) r4;
        r6 = 1060320051; // 0x3f333333 float:0.7 double:5.23867711E-315;
        r4 = r4 * r6;
        r0 = (int) r4;
        r113 = r0;
        r97 = r113;
        goto L_0x2a51;
    L_0x2c61:
        r0 = r147;
        r4 = r0.type;
        r6 = 3;
        if (r4 != r6) goto L_0x2c94;
    L_0x2c68:
        r4 = 0;
        r0 = r146;
        r1 = r147;
        r0.createDocumentLayout(r4, r1);
        r146.updateSecretTimeText(r147);
        r4 = r147.isSecretPhoto();
        if (r4 != 0) goto L_0x2c89;
    L_0x2c79:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1;
        r4.setNeedsQualityThumb(r6);
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1;
        r4.setShouldGenerateQualityThumb(r6);
    L_0x2c89:
        r0 = r146;
        r4 = r0.photoImage;
        r0 = r147;
        r4.setParentMessageObject(r0);
        goto L_0x2aa6;
    L_0x2c94:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x2cbc;
    L_0x2c9b:
        r4 = r147.isSecretPhoto();
        if (r4 != 0) goto L_0x2cb1;
    L_0x2ca1:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1;
        r4.setNeedsQualityThumb(r6);
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1;
        r4.setShouldGenerateQualityThumb(r6);
    L_0x2cb1:
        r0 = r146;
        r4 = r0.photoImage;
        r0 = r147;
        r4.setParentMessageObject(r0);
        goto L_0x2aa6;
    L_0x2cbc:
        r0 = r147;
        r4 = r0.type;
        r6 = 8;
        if (r4 != r6) goto L_0x2aa6;
    L_0x2cc4:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r4 = r4.size;
        r8 = (long) r4;
        r5 = org.telegram.messenger.AndroidUtilities.formatFileSize(r8);
        r4 = org.telegram.ui.ActionBar.Theme.chat_infoPaint;
        r4 = r4.measureText(r5);
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
        r4 = (int) r8;
        r0 = r146;
        r0.infoWidth = r4;
        r27 = new android.text.StaticLayout;
        r29 = org.telegram.ui.ActionBar.Theme.chat_infoPaint;
        r0 = r146;
        r0 = r0.infoWidth;
        r30 = r0;
        r31 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r32 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r33 = 0;
        r34 = 0;
        r28 = r5;
        r27.<init>(r28, r29, r30, r31, r32, r33, r34);
        r0 = r27;
        r1 = r146;
        r1.infoLayout = r0;
        r4 = r147.isSecretPhoto();
        if (r4 != 0) goto L_0x2d16;
    L_0x2d06:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1;
        r4.setNeedsQualityThumb(r6);
        r0 = r146;
        r4 = r0.photoImage;
        r6 = 1;
        r4.setShouldGenerateQualityThumb(r6);
    L_0x2d16:
        r0 = r146;
        r4 = r0.photoImage;
        r0 = r147;
        r4.setParentMessageObject(r0);
        goto L_0x2aa6;
    L_0x2d21:
        r4 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r76;
        if (r0 >= r4) goto L_0x2b34;
    L_0x2d2b:
        r4 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r76 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.h;
        r4 = (float) r4;
        r0 = r76;
        r6 = (float) r0;
        r77 = r4 / r6;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        r4 = (float) r4;
        r4 = r4 / r77;
        r0 = r113;
        r6 = (float) r0;
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 >= 0) goto L_0x2b34;
    L_0x2d4d:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.w;
        r4 = (float) r4;
        r4 = r4 / r77;
        r0 = (int) r4;
        r139 = r0;
        goto L_0x2b34;
    L_0x2d5b:
        r4 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r76;
        if (r0 >= r4) goto L_0x2bb5;
    L_0x2d65:
        r4 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r76 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r46;
        r4 = r0.h;
        r4 = (float) r4;
        r0 = r76;
        r6 = (float) r0;
        r77 = r4 / r6;
        r0 = r46;
        r4 = r0.w;
        r4 = (float) r4;
        r4 = r4 / r77;
        r0 = r113;
        r6 = (float) r0;
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 >= 0) goto L_0x2bb5;
    L_0x2d83:
        r0 = r46;
        r4 = r0.w;
        r4 = (float) r4;
        r4 = r4 / r77;
        r0 = (int) r4;
        r139 = r0;
        goto L_0x2bb5;
    L_0x2d8f:
        r41 = r41 + 1;
        goto L_0x2b4d;
    L_0x2d93:
        r4 = 1108082688; // 0x420c0000 float:35.0 double:5.47465589E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r72 - r4;
        r0 = r146;
        r0.availableTimeWidth = r4;
    L_0x2d9f:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x2dca;
    L_0x2da6:
        r0 = r146;
        r4 = r0.availableTimeWidth;
        r8 = (double) r4;
        r4 = org.telegram.ui.ActionBar.Theme.chat_audioTimePaint;
        r6 = "00:00";
        r4 = r4.measureText(r6);
        r10 = (double) r4;
        r10 = java.lang.Math.ceil(r10);
        r4 = 1104150528; // 0x41d00000 float:26.0 double:5.455228437E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = (double) r4;
        r18 = r0;
        r10 = r10 + r18;
        r8 = r8 - r10;
        r4 = (int) r8;
        r0 = r146;
        r0.availableTimeWidth = r4;
    L_0x2dca:
        r146.measureTime(r147);
        r0 = r146;
        r6 = r0.timeWidth;
        r4 = r147.isOutOwner();
        if (r4 == 0) goto L_0x2ecf;
    L_0x2dd7:
        r4 = 20;
    L_0x2dd9:
        r4 = r4 + 14;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r133 = r6 + r4;
        r0 = r139;
        r1 = r133;
        if (r0 >= r1) goto L_0x2dea;
    L_0x2de8:
        r139 = r133;
    L_0x2dea:
        r4 = r147.isRoundVideo();
        if (r4 == 0) goto L_0x2ed2;
    L_0x2df0:
        r0 = r139;
        r1 = r76;
        r76 = java.lang.Math.min(r0, r1);
        r139 = r76;
        r4 = 0;
        r0 = r146;
        r0.drawBackground = r4;
        r0 = r146;
        r4 = r0.photoImage;
        r6 = r139 / 2;
        r4.setRoundRadius(r6);
    L_0x2e08:
        r28 = 0;
        r30 = 0;
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        if (r4 == 0) goto L_0x3160;
    L_0x2e12:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r4 = java.lang.Math.max(r4, r6);
        r4 = (float) r4;
        r6 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r96 = r4 * r6;
        r63 = r146.getGroupPhotosWidth();
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.pw;
        r4 = (float) r4;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 / r6;
        r0 = r63;
        r6 = (float) r0;
        r4 = r4 * r6;
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
        r0 = (int) r8;
        r139 = r0;
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.minY;
        if (r4 == 0) goto L_0x2f59;
    L_0x2e45:
        r4 = r147.isOutOwner();
        if (r4 == 0) goto L_0x2e55;
    L_0x2e4b:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.flags;
        r4 = r4 & 1;
        if (r4 != 0) goto L_0x2e65;
    L_0x2e55:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x2f59;
    L_0x2e5b:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.flags;
        r4 = r4 & 2;
        if (r4 == 0) goto L_0x2f59;
    L_0x2e65:
        r72 = 0;
        r61 = 0;
        r41 = 0;
    L_0x2e6b:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        r4 = r4.posArray;
        r4 = r4.size();
        r0 = r41;
        if (r0 >= r4) goto L_0x2f55;
    L_0x2e79:
        r0 = r146;
        r4 = r0.currentMessagesGroup;
        r4 = r4.posArray;
        r0 = r41;
        r114 = r4.get(r0);
        r114 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r114;
        r0 = r114;
        r4 = r0.minY;
        if (r4 != 0) goto L_0x2f07;
    L_0x2e8d:
        r0 = r72;
        r10 = (double) r0;
        r0 = r114;
        r4 = r0.pw;
        r4 = (float) r4;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 / r6;
        r0 = r63;
        r6 = (float) r0;
        r4 = r4 * r6;
        r8 = (double) r4;
        r18 = java.lang.Math.ceil(r8);
        r0 = r114;
        r4 = r0.leftSpanOffset;
        if (r4 == 0) goto L_0x2f04;
    L_0x2ea7:
        r0 = r114;
        r4 = r0.leftSpanOffset;
        r4 = (float) r4;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 / r6;
        r0 = r63;
        r6 = (float) r0;
        r4 = r4 * r6;
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
    L_0x2eb8:
        r8 = r8 + r18;
        r8 = r8 + r10;
        r0 = (int) r8;
        r72 = r0;
    L_0x2ebe:
        r41 = r41 + 1;
        goto L_0x2e6b;
    L_0x2ec1:
        r4 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r97 - r4;
        r0 = r146;
        r0.availableTimeWidth = r4;
        goto L_0x2d9f;
    L_0x2ecf:
        r4 = 0;
        goto L_0x2dd9;
    L_0x2ed2:
        r4 = r147.isSecretPhoto();
        if (r4 == 0) goto L_0x2e08;
    L_0x2ed8:
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x2eed;
    L_0x2ede:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = (float) r4;
        r6 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r4 = r4 * r6;
        r0 = (int) r4;
        r76 = r0;
        r139 = r76;
        goto L_0x2e08;
    L_0x2eed:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r4 = java.lang.Math.min(r4, r6);
        r4 = (float) r4;
        r6 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r4 = r4 * r6;
        r0 = (int) r4;
        r76 = r0;
        r139 = r76;
        goto L_0x2e08;
    L_0x2f04:
        r8 = 0;
        goto L_0x2eb8;
    L_0x2f07:
        r0 = r114;
        r4 = r0.minY;
        r0 = r146;
        r6 = r0.currentPosition;
        r6 = r6.minY;
        if (r4 != r6) goto L_0x2f49;
    L_0x2f13:
        r0 = r61;
        r10 = (double) r0;
        r0 = r114;
        r4 = r0.pw;
        r4 = (float) r4;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 / r6;
        r0 = r63;
        r6 = (float) r0;
        r4 = r4 * r6;
        r8 = (double) r4;
        r18 = java.lang.Math.ceil(r8);
        r0 = r114;
        r4 = r0.leftSpanOffset;
        if (r4 == 0) goto L_0x2f46;
    L_0x2f2d:
        r0 = r114;
        r4 = r0.leftSpanOffset;
        r4 = (float) r4;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r4 = r4 / r6;
        r0 = r63;
        r6 = (float) r0;
        r4 = r4 * r6;
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
    L_0x2f3e:
        r8 = r8 + r18;
        r8 = r8 + r10;
        r0 = (int) r8;
        r61 = r0;
        goto L_0x2ebe;
    L_0x2f46:
        r8 = 0;
        goto L_0x2f3e;
    L_0x2f49:
        r0 = r114;
        r4 = r0.minY;
        r0 = r146;
        r6 = r0.currentPosition;
        r6 = r6.minY;
        if (r4 <= r6) goto L_0x2ebe;
    L_0x2f55:
        r4 = r72 - r61;
        r139 = r139 + r4;
    L_0x2f59:
        r4 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r139 = r139 - r4;
        r0 = r146;
        r4 = r0.isAvatarVisible;
        if (r4 == 0) goto L_0x2f6f;
    L_0x2f67:
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r139 = r139 - r4;
    L_0x2f6f:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.siblingHeights;
        if (r4 == 0) goto L_0x314e;
    L_0x2f77:
        r76 = 0;
        r41 = 0;
    L_0x2f7b:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.siblingHeights;
        r4 = r4.length;
        r0 = r41;
        if (r0 >= r4) goto L_0x2f9b;
    L_0x2f86:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.siblingHeights;
        r4 = r4[r41];
        r4 = r4 * r96;
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
        r4 = (int) r8;
        r76 = r76 + r4;
        r41 = r41 + 1;
        goto L_0x2f7b;
    L_0x2f9b:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.maxY;
        r0 = r146;
        r6 = r0.currentPosition;
        r6 = r6.minY;
        r4 = r4 - r6;
        r6 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 * r6;
        r76 = r76 + r4;
    L_0x2fb1:
        r0 = r139;
        r1 = r146;
        r1.backgroundWidth = r0;
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r139 = r139 - r4;
        r113 = r139;
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.edge;
        if (r4 != 0) goto L_0x2fd1;
    L_0x2fc9:
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r113 = r113 + r4;
    L_0x2fd1:
        r112 = r76;
    L_0x2fd3:
        if (r28 == 0) goto L_0x3067;
    L_0x2fd5:
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x31ac }
        r6 = 24;
        if (r4 < r6) goto L_0x3195;
    L_0x2fdb:
        r4 = 0;
        r6 = r28.length();	 Catch:{ Exception -> 0x31ac }
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x31ac }
        r0 = r28;
        r1 = r30;
        r4 = android.text.StaticLayout.Builder.obtain(r0, r4, r6, r8, r1);	 Catch:{ Exception -> 0x31ac }
        r6 = 1;
        r4 = r4.setBreakStrategy(r6);	 Catch:{ Exception -> 0x31ac }
        r6 = 0;
        r4 = r4.setHyphenationFrequency(r6);	 Catch:{ Exception -> 0x31ac }
        r6 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x31ac }
        r4 = r4.setAlignment(r6);	 Catch:{ Exception -> 0x31ac }
        r4 = r4.build();	 Catch:{ Exception -> 0x31ac }
        r0 = r146;
        r0.captionLayout = r4;	 Catch:{ Exception -> 0x31ac }
    L_0x3002:
        r0 = r146;
        r4 = r0.captionLayout;	 Catch:{ Exception -> 0x31ac }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x31ac }
        if (r4 <= 0) goto L_0x3067;
    L_0x300c:
        r0 = r146;
        r4 = r0.captionLayout;	 Catch:{ Exception -> 0x31ac }
        r4 = r4.getHeight();	 Catch:{ Exception -> 0x31ac }
        r0 = r146;
        r0.captionHeight = r4;	 Catch:{ Exception -> 0x31ac }
        r0 = r146;
        r4 = r0.captionHeight;	 Catch:{ Exception -> 0x31ac }
        r6 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x31ac }
        r4 = r4 + r6;
        r43 = r43 + r4;
        r0 = r146;
        r4 = r0.captionLayout;	 Catch:{ Exception -> 0x31ac }
        r0 = r146;
        r6 = r0.captionLayout;	 Catch:{ Exception -> 0x31ac }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x31ac }
        r6 = r6 + -1;
        r4 = r4.getLineWidth(r6);	 Catch:{ Exception -> 0x31ac }
        r0 = r146;
        r6 = r0.captionLayout;	 Catch:{ Exception -> 0x31ac }
        r0 = r146;
        r8 = r0.captionLayout;	 Catch:{ Exception -> 0x31ac }
        r8 = r8.getLineCount();	 Catch:{ Exception -> 0x31ac }
        r8 = r8 + -1;
        r6 = r6.getLineLeft(r8);	 Catch:{ Exception -> 0x31ac }
        r82 = r4 + r6;
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x31ac }
        r4 = r4 + r30;
        r4 = (float) r4;	 Catch:{ Exception -> 0x31ac }
        r4 = r4 - r82;
        r0 = r133;
        r6 = (float) r0;	 Catch:{ Exception -> 0x31ac }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 >= 0) goto L_0x3067;
    L_0x305d:
        r4 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x31ac }
        r43 = r43 + r4;
        r57 = 1;
    L_0x3067:
        r4 = java.util.Locale.US;
        r6 = "%d_%d";
        r8 = 2;
        r8 = new java.lang.Object[r8];
        r9 = 0;
        r0 = r139;
        r10 = (float) r0;
        r11 = org.telegram.messenger.AndroidUtilities.density;
        r10 = r10 / r11;
        r10 = (int) r10;
        r10 = java.lang.Integer.valueOf(r10);
        r8[r9] = r10;
        r9 = 1;
        r0 = r76;
        r10 = (float) r0;
        r11 = org.telegram.messenger.AndroidUtilities.density;
        r10 = r10 / r11;
        r10 = (int) r10;
        r10 = java.lang.Integer.valueOf(r10);
        r8[r9] = r10;
        r4 = java.lang.String.format(r4, r6, r8);
        r0 = r146;
        r0.currentPhotoFilterThumb = r4;
        r0 = r146;
        r0.currentPhotoFilter = r4;
        r0 = r147;
        r4 = r0.photoThumbs;
        if (r4 == 0) goto L_0x30a8;
    L_0x309d:
        r0 = r147;
        r4 = r0.photoThumbs;
        r4 = r4.size();
        r6 = 1;
        if (r4 > r6) goto L_0x30be;
    L_0x30a8:
        r0 = r147;
        r4 = r0.type;
        r6 = 3;
        if (r4 == r6) goto L_0x30be;
    L_0x30af:
        r0 = r147;
        r4 = r0.type;
        r6 = 8;
        if (r4 == r6) goto L_0x30be;
    L_0x30b7:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x30fc;
    L_0x30be:
        r4 = r147.isSecretPhoto();
        if (r4 == 0) goto L_0x31b2;
    L_0x30c4:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r146;
        r6 = r0.currentPhotoFilter;
        r4 = r4.append(r6);
        r6 = "_b2";
        r4 = r4.append(r6);
        r4 = r4.toString();
        r0 = r146;
        r0.currentPhotoFilter = r4;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r146;
        r6 = r0.currentPhotoFilterThumb;
        r4 = r4.append(r6);
        r6 = "_b2";
        r4 = r4.append(r6);
        r4 = r4.toString();
        r0 = r146;
        r0.currentPhotoFilterThumb = r4;
    L_0x30fc:
        r104 = 0;
        r0 = r147;
        r4 = r0.type;
        r6 = 3;
        if (r4 == r6) goto L_0x3114;
    L_0x3105:
        r0 = r147;
        r4 = r0.type;
        r6 = 8;
        if (r4 == r6) goto L_0x3114;
    L_0x310d:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x3116;
    L_0x3114:
        r104 = 1;
    L_0x3116:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x312d;
    L_0x311c:
        if (r104 != 0) goto L_0x312d;
    L_0x311e:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r4 = r4.size;
        if (r4 != 0) goto L_0x312d;
    L_0x3126:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r6 = -1;
        r4.size = r6;
    L_0x312d:
        r0 = r147;
        r4 = r0.type;
        r6 = 1;
        if (r4 != r6) goto L_0x32bb;
    L_0x3134:
        r0 = r147;
        r4 = r0.useCustomPhoto;
        if (r4 == 0) goto L_0x31d0;
    L_0x313a:
        r0 = r146;
        r4 = r0.photoImage;
        r6 = r146.getResources();
        r8 = 2131165651; // 0x7f0701d3 float:1.7945525E38 double:1.052935734E-314;
        r6 = r6.getDrawable(r8);
        r4.setImageBitmap(r6);
        goto L_0x22bb;
    L_0x314e:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.ph;
        r4 = r4 * r96;
        r8 = (double) r4;
        r8 = java.lang.Math.ceil(r8);
        r0 = (int) r8;
        r76 = r0;
        goto L_0x2fb1;
    L_0x3160:
        r113 = r139;
        r112 = r76;
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 + r139;
        r0 = r146;
        r0.backgroundWidth = r4;
        r0 = r146;
        r4 = r0.mediaBackground;
        if (r4 != 0) goto L_0x3185;
    L_0x3176:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r6 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.backgroundWidth = r4;
    L_0x3185:
        r0 = r147;
        r0 = r0.caption;
        r28 = r0;
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r30 = r113 - r4;
        goto L_0x2fd3;
    L_0x3195:
        r27 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x31ac }
        r29 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x31ac }
        r31 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x31ac }
        r32 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r33 = 0;
        r34 = 0;
        r27.<init>(r28, r29, r30, r31, r32, r33, r34);	 Catch:{ Exception -> 0x31ac }
        r0 = r27;
        r1 = r146;
        r1.captionLayout = r0;	 Catch:{ Exception -> 0x31ac }
        goto L_0x3002;
    L_0x31ac:
        r70 = move-exception;
        org.telegram.messenger.FileLog.e(r70);
        goto L_0x3067;
    L_0x31b2:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r146;
        r6 = r0.currentPhotoFilterThumb;
        r4 = r4.append(r6);
        r6 = "_b";
        r4 = r4.append(r6);
        r4 = r4.toString();
        r0 = r146;
        r0.currentPhotoFilterThumb = r4;
        goto L_0x30fc;
    L_0x31d0:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x32af;
    L_0x31d6:
        r111 = 1;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r71 = org.telegram.messenger.FileLoader.getAttachFileName(r4);
        r0 = r147;
        r4 = r0.mediaExists;
        if (r4 == 0) goto L_0x3256;
    L_0x31e6:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r0 = r146;
        r4.removeLoadingFileObserver(r0);
    L_0x31f3:
        if (r111 != 0) goto L_0x3217;
    L_0x31f5:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r0 = r146;
        r6 = r0.currentMessageObject;
        r4 = r4.canDownloadMedia(r6);
        if (r4 != 0) goto L_0x3217;
    L_0x3207:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.FileLoader.getInstance(r4);
        r0 = r71;
        r4 = r4.isLoadingFile(r0);
        if (r4 == 0) goto L_0x3268;
    L_0x3217:
        r0 = r146;
        r0 = r0.photoImage;
        r31 = r0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r32 = r0;
        r0 = r146;
        r0 = r0.currentPhotoFilter;
        r33 = r0;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        if (r4 == 0) goto L_0x3259;
    L_0x3231:
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        r0 = r4.location;
        r34 = r0;
    L_0x3239:
        r0 = r146;
        r0 = r0.currentPhotoFilterThumb;
        r35 = r0;
        if (r104 == 0) goto L_0x325c;
    L_0x3241:
        r36 = 0;
    L_0x3243:
        r37 = 0;
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.shouldEncryptPhotoOrVideo();
        if (r4 == 0) goto L_0x3265;
    L_0x324f:
        r38 = 2;
    L_0x3251:
        r31.setImage(r32, r33, r34, r35, r36, r37, r38);
        goto L_0x22bb;
    L_0x3256:
        r111 = 0;
        goto L_0x31f3;
    L_0x3259:
        r34 = 0;
        goto L_0x3239;
    L_0x325c:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.size;
        r36 = r0;
        goto L_0x3243;
    L_0x3265:
        r38 = 0;
        goto L_0x3251;
    L_0x3268:
        r4 = 1;
        r0 = r146;
        r0.photoNotSet = r4;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        if (r4 == 0) goto L_0x32a3;
    L_0x3273:
        r0 = r146;
        r0 = r0.photoImage;
        r31 = r0;
        r32 = 0;
        r33 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObjectThumb;
        r0 = r4.location;
        r34 = r0;
        r0 = r146;
        r0 = r0.currentPhotoFilterThumb;
        r35 = r0;
        r36 = 0;
        r37 = 0;
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.shouldEncryptPhotoOrVideo();
        if (r4 == 0) goto L_0x32a0;
    L_0x3299:
        r38 = 2;
    L_0x329b:
        r31.setImage(r32, r33, r34, r35, r36, r37, r38);
        goto L_0x22bb;
    L_0x32a0:
        r38 = 0;
        goto L_0x329b;
    L_0x32a3:
        r0 = r146;
        r6 = r0.photoImage;
        r4 = 0;
        r4 = (android.graphics.drawable.Drawable) r4;
        r6.setImageBitmap(r4);
        goto L_0x22bb;
    L_0x32af:
        r0 = r146;
        r6 = r0.photoImage;
        r4 = 0;
        r4 = (android.graphics.drawable.BitmapDrawable) r4;
        r6.setImageBitmap(r4);
        goto L_0x22bb;
    L_0x32bb:
        r0 = r147;
        r4 = r0.type;
        r6 = 8;
        if (r4 == r6) goto L_0x32ca;
    L_0x32c3:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x33fb;
    L_0x32ca:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r71 = org.telegram.messenger.FileLoader.getAttachFileName(r4);
        r88 = 0;
        r0 = r147;
        r4 = r0.attachPathExists;
        if (r4 == 0) goto L_0x335f;
    L_0x32de:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r0 = r146;
        r4.removeLoadingFileObserver(r0);
        r88 = 1;
    L_0x32ed:
        r49 = 0;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r4 = org.telegram.messenger.MessageObject.isNewGifDocument(r4);
        if (r4 == 0) goto L_0x3368;
    L_0x32fd:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r0 = r146;
        r6 = r0.currentMessageObject;
        r49 = r4.canDownloadMedia(r6);
    L_0x330d:
        r4 = r147.isSending();
        if (r4 != 0) goto L_0x33ca;
    L_0x3313:
        if (r88 != 0) goto L_0x3327;
    L_0x3315:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.FileLoader.getInstance(r4);
        r0 = r71;
        r4 = r4.isLoadingFile(r0);
        if (r4 != 0) goto L_0x3327;
    L_0x3325:
        if (r49 == 0) goto L_0x33ca;
    L_0x3327:
        r4 = 1;
        r0 = r88;
        if (r0 != r4) goto L_0x338c;
    L_0x332c:
        r0 = r146;
        r0 = r0.photoImage;
        r31 = r0;
        r32 = 0;
        r4 = r147.isSendError();
        if (r4 == 0) goto L_0x3380;
    L_0x333a:
        r33 = 0;
    L_0x333c:
        r34 = 0;
        r35 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x3389;
    L_0x3346:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r36 = r0;
    L_0x334e:
        r0 = r146;
        r0 = r0.currentPhotoFilterThumb;
        r37 = r0;
        r38 = 0;
        r39 = 0;
        r40 = 0;
        r31.setImage(r32, r33, r34, r35, r36, r37, r38, r39, r40);
        goto L_0x22bb;
    L_0x335f:
        r0 = r147;
        r4 = r0.mediaExists;
        if (r4 == 0) goto L_0x32ed;
    L_0x3365:
        r88 = 2;
        goto L_0x32ed;
    L_0x3368:
        r0 = r147;
        r4 = r0.type;
        r6 = 5;
        if (r4 != r6) goto L_0x330d;
    L_0x336f:
        r0 = r146;
        r4 = r0.currentAccount;
        r4 = org.telegram.messenger.DownloadController.getInstance(r4);
        r0 = r146;
        r6 = r0.currentMessageObject;
        r49 = r4.canDownloadMedia(r6);
        goto L_0x330d;
    L_0x3380:
        r0 = r147;
        r4 = r0.messageOwner;
        r0 = r4.attachPath;
        r33 = r0;
        goto L_0x333c;
    L_0x3389:
        r36 = 0;
        goto L_0x334e;
    L_0x338c:
        r0 = r146;
        r0 = r0.photoImage;
        r31 = r0;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r0 = r4.document;
        r32 = r0;
        r33 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x33c7;
    L_0x33a4:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r34 = r0;
    L_0x33ac:
        r0 = r146;
        r0 = r0.currentPhotoFilterThumb;
        r35 = r0;
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r0 = r4.size;
        r36 = r0;
        r37 = 0;
        r38 = 0;
        r31.setImage(r32, r33, r34, r35, r36, r37, r38);
        goto L_0x22bb;
    L_0x33c7:
        r34 = 0;
        goto L_0x33ac;
    L_0x33ca:
        r4 = 1;
        r0 = r146;
        r0.photoNotSet = r4;
        r0 = r146;
        r0 = r0.photoImage;
        r31 = r0;
        r32 = 0;
        r33 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x33f8;
    L_0x33df:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r34 = r0;
    L_0x33e7:
        r0 = r146;
        r0 = r0.currentPhotoFilterThumb;
        r35 = r0;
        r36 = 0;
        r37 = 0;
        r38 = 0;
        r31.setImage(r32, r33, r34, r35, r36, r37, r38);
        goto L_0x22bb;
    L_0x33f8:
        r34 = 0;
        goto L_0x33e7;
    L_0x33fb:
        r0 = r146;
        r0 = r0.photoImage;
        r31 = r0;
        r32 = 0;
        r33 = 0;
        r0 = r146;
        r4 = r0.currentPhotoObject;
        if (r4 == 0) goto L_0x342e;
    L_0x340b:
        r0 = r146;
        r4 = r0.currentPhotoObject;
        r0 = r4.location;
        r34 = r0;
    L_0x3413:
        r0 = r146;
        r0 = r0.currentPhotoFilterThumb;
        r35 = r0;
        r36 = 0;
        r37 = 0;
        r0 = r146;
        r4 = r0.currentMessageObject;
        r4 = r4.shouldEncryptPhotoOrVideo();
        if (r4 == 0) goto L_0x3431;
    L_0x3427:
        r38 = 2;
    L_0x3429:
        r31.setImage(r32, r33, r34, r35, r36, r37, r38);
        goto L_0x22bb;
    L_0x342e:
        r34 = 0;
        goto L_0x3413;
    L_0x3431:
        r38 = 0;
        goto L_0x3429;
    L_0x3434:
        r0 = r146;
        r4 = r0.drawNameLayout;
        if (r4 == 0) goto L_0x22ee;
    L_0x343a:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.reply_to_msg_id;
        if (r4 != 0) goto L_0x22ee;
    L_0x3442:
        r0 = r146;
        r4 = r0.namesOffset;
        r6 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.namesOffset = r4;
        goto L_0x22ee;
    L_0x3453:
        r31 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x3478 }
        r0 = r147;
        r0 = r0.caption;	 Catch:{ Exception -> 0x3478 }
        r32 = r0;
        r33 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x3478 }
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x3478 }
        r34 = r143 - r4;
        r35 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x3478 }
        r36 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r37 = 0;
        r38 = 0;
        r31.<init>(r32, r33, r34, r35, r36, r37, r38);	 Catch:{ Exception -> 0x3478 }
        r0 = r31;
        r1 = r146;
        r1.captionLayout = r0;	 Catch:{ Exception -> 0x3478 }
        goto L_0x11ff;
    L_0x3478:
        r70 = move-exception;
        org.telegram.messenger.FileLog.e(r70);
        goto L_0x1293;
    L_0x347e:
        r4 = 0;
        goto L_0x1219;
    L_0x3481:
        r0 = r146;
        r4 = r0.widthBeforeNewTimeLine;
        r6 = -1;
        if (r4 == r6) goto L_0x1293;
    L_0x3488:
        r0 = r146;
        r4 = r0.availableTimeWidth;
        r0 = r146;
        r6 = r0.widthBeforeNewTimeLine;
        r4 = r4 - r6;
        r0 = r146;
        r6 = r0.timeWidth;
        if (r4 >= r6) goto L_0x1293;
    L_0x3497:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        goto L_0x1293;
    L_0x34a8:
        r70 = move-exception;
        org.telegram.messenger.FileLog.e(r70);
        goto L_0x1332;
    L_0x34ae:
        r0 = r146;
        r4 = r0.descriptionX;	 Catch:{ Exception -> 0x34bf }
        r0 = r86;
        r6 = -r0;
        r4 = java.lang.Math.max(r4, r6);	 Catch:{ Exception -> 0x34bf }
        r0 = r146;
        r0.descriptionX = r4;	 Catch:{ Exception -> 0x34bf }
        goto L_0x13c8;
    L_0x34bf:
        r70 = move-exception;
        org.telegram.messenger.FileLog.e(r70);
    L_0x34c3:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.totalHeight = r4;
        if (r57 == 0) goto L_0x34f7;
    L_0x34d4:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.totalHeight = r4;
        r4 = 2;
        r0 = r57;
        if (r0 != r4) goto L_0x34f7;
    L_0x34e8:
        r0 = r146;
        r4 = r0.captionHeight;
        r6 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.captionHeight = r4;
    L_0x34f7:
        r0 = r146;
        r4 = r0.botButtons;
        r4.clear();
        if (r101 == 0) goto L_0x3513;
    L_0x3500:
        r0 = r146;
        r4 = r0.botButtonsByData;
        r4.clear();
        r0 = r146;
        r4 = r0.botButtonsByPosition;
        r4.clear();
        r4 = 0;
        r0 = r146;
        r0.botButtonsLayout = r4;
    L_0x3513:
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 != 0) goto L_0x3801;
    L_0x3519:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.reply_markup;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_replyInlineMarkup;
        if (r4 == 0) goto L_0x3801;
    L_0x3523:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.reply_markup;
        r4 = r4.rows;
        r121 = r4.size();
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 * r121;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r146;
        r0.keyboardHeight = r4;
        r0 = r146;
        r0.substractBackgroundHeight = r4;
        r0 = r146;
        r4 = r0.backgroundWidth;
        r0 = r146;
        r0.widthForButtons = r4;
        r73 = 0;
        r0 = r147;
        r4 = r0.wantedBotKeyboardWidth;
        r0 = r146;
        r6 = r0.widthForButtons;
        if (r4 <= r6) goto L_0x3599;
    L_0x355a:
        r0 = r146;
        r4 = r0.isChat;
        if (r4 == 0) goto L_0x35fb;
    L_0x3560:
        r4 = r147.needDrawAvatar();
        if (r4 == 0) goto L_0x35fb;
    L_0x3566:
        r4 = r147.isOutOwner();
        if (r4 != 0) goto L_0x35fb;
    L_0x356c:
        r4 = 1115160576; // 0x42780000 float:62.0 double:5.5096253E-315;
    L_0x356e:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = -r4;
        r93 = r0;
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x35ff;
    L_0x357b:
        r4 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r93 = r93 + r4;
    L_0x3581:
        r0 = r146;
        r4 = r0.backgroundWidth;
        r0 = r147;
        r6 = r0.wantedBotKeyboardWidth;
        r0 = r93;
        r6 = java.lang.Math.min(r6, r0);
        r4 = java.lang.Math.max(r4, r6);
        r0 = r146;
        r0.widthForButtons = r4;
        r73 = 1;
    L_0x3599:
        r94 = 0;
        r107 = new java.util.HashMap;
        r0 = r146;
        r4 = r0.botButtonsByData;
        r0 = r107;
        r0.<init>(r4);
        r0 = r147;
        r4 = r0.botButtonsLayout;
        if (r4 == 0) goto L_0x360f;
    L_0x35ac:
        r0 = r146;
        r4 = r0.botButtonsLayout;
        if (r4 == 0) goto L_0x360f;
    L_0x35b2:
        r0 = r146;
        r4 = r0.botButtonsLayout;
        r0 = r147;
        r6 = r0.botButtonsLayout;
        r6 = r6.toString();
        r4 = r4.equals(r6);
        if (r4 == 0) goto L_0x360f;
    L_0x35c4:
        r108 = new java.util.HashMap;
        r0 = r146;
        r4 = r0.botButtonsByPosition;
        r0 = r108;
        r0.<init>(r4);
    L_0x35cf:
        r0 = r146;
        r4 = r0.botButtonsByData;
        r4.clear();
        r41 = 0;
    L_0x35d8:
        r0 = r41;
        r1 = r121;
        if (r0 >= r1) goto L_0x37b7;
    L_0x35de:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.reply_markup;
        r4 = r4.rows;
        r0 = r41;
        r120 = r4.get(r0);
        r120 = (org.telegram.tgnet.TLRPC.TL_keyboardButtonRow) r120;
        r0 = r120;
        r4 = r0.buttons;
        r55 = r4.size();
        if (r55 != 0) goto L_0x3624;
    L_0x35f8:
        r41 = r41 + 1;
        goto L_0x35d8;
    L_0x35fb:
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        goto L_0x356e;
    L_0x35ff:
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r4 = java.lang.Math.min(r4, r6);
        r93 = r93 + r4;
        goto L_0x3581;
    L_0x360f:
        r0 = r147;
        r4 = r0.botButtonsLayout;
        if (r4 == 0) goto L_0x3621;
    L_0x3615:
        r0 = r147;
        r4 = r0.botButtonsLayout;
        r4 = r4.toString();
        r0 = r146;
        r0.botButtonsLayout = r4;
    L_0x3621:
        r108 = 0;
        goto L_0x35cf;
    L_0x3624:
        r0 = r146;
        r4 = r0.widthForButtons;
        r6 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r8 = r55 + -1;
        r6 = r6 * r8;
        r6 = r4 - r6;
        if (r73 != 0) goto L_0x3770;
    L_0x3635:
        r0 = r146;
        r4 = r0.mediaBackground;
        if (r4 == 0) goto L_0x3770;
    L_0x363b:
        r4 = 0;
    L_0x363c:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r6 - r4;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r54 = r4 / r55;
        r50 = 0;
    L_0x364d:
        r0 = r120;
        r4 = r0.buttons;
        r4 = r4.size();
        r0 = r50;
        if (r0 >= r4) goto L_0x35f8;
    L_0x3659:
        r53 = new org.telegram.ui.Cells.ChatMessageCell$BotButton;
        r4 = 0;
        r0 = r53;
        r1 = r146;
        r0.<init>();
        r0 = r120;
        r4 = r0.buttons;
        r0 = r50;
        r4 = r4.get(r0);
        r4 = (org.telegram.tgnet.TLRPC.KeyboardButton) r4;
        r0 = r53;
        r0.button = r4;
        r4 = r53.button;
        r4 = r4.data;
        r81 = org.telegram.messenger.Utilities.bytesToHex(r4);
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r0 = r41;
        r4 = r4.append(r0);
        r6 = "";
        r4 = r4.append(r6);
        r0 = r50;
        r4 = r4.append(r0);
        r114 = r4.toString();
        if (r108 == 0) goto L_0x3774;
    L_0x369c:
        r0 = r108;
        r1 = r114;
        r106 = r0.get(r1);
        r106 = (org.telegram.ui.Cells.ChatMessageCell.BotButton) r106;
    L_0x36a6:
        if (r106 == 0) goto L_0x3780;
    L_0x36a8:
        r4 = r106.progressAlpha;
        r0 = r53;
        r0.progressAlpha = r4;
        r4 = r106.angle;
        r0 = r53;
        r0.angle = r4;
        r8 = r106.lastUpdateTime;
        r0 = r53;
        r0.lastUpdateTime = r8;
    L_0x36c3:
        r0 = r146;
        r4 = r0.botButtonsByData;
        r0 = r81;
        r1 = r53;
        r4.put(r0, r1);
        r0 = r146;
        r4 = r0.botButtonsByPosition;
        r0 = r114;
        r1 = r53;
        r4.put(r0, r1);
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 + r54;
        r4 = r4 * r50;
        r0 = r53;
        r0.x = r4;
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 * r41;
        r6 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r0 = r53;
        r0.y = r4;
        r53.width = r54;
        r4 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r53;
        r0.height = r4;
        r4 = r53.button;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
        if (r4 == 0) goto L_0x378b;
    L_0x3712:
        r0 = r147;
        r4 = r0.messageOwner;
        r4 = r4.media;
        r4 = r4.flags;
        r4 = r4 & 4;
        if (r4 == 0) goto L_0x378b;
    L_0x371e:
        r4 = "PaymentReceipt";
        r6 = 2131494063; // 0x7f0c04af float:1.8611624E38 double:1.053097991E-314;
        r32 = org.telegram.messenger.LocaleController.getString(r4, r6);
    L_0x3728:
        r31 = new android.text.StaticLayout;
        r33 = org.telegram.ui.ActionBar.Theme.chat_botButtonPaint;
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r34 = r54 - r4;
        r35 = android.text.Layout.Alignment.ALIGN_CENTER;
        r36 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r37 = 0;
        r38 = 0;
        r31.<init>(r32, r33, r34, r35, r36, r37, r38);
        r0 = r53;
        r1 = r31;
        r0.title = r1;
        r0 = r146;
        r4 = r0.botButtons;
        r0 = r53;
        r4.add(r0);
        r0 = r120;
        r4 = r0.buttons;
        r4 = r4.size();
        r4 = r4 + -1;
        r0 = r50;
        if (r0 != r4) goto L_0x376c;
    L_0x375d:
        r4 = r53.x;
        r6 = r53.width;
        r4 = r4 + r6;
        r0 = r94;
        r94 = java.lang.Math.max(r0, r4);
    L_0x376c:
        r50 = r50 + 1;
        goto L_0x364d;
    L_0x3770:
        r4 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        goto L_0x363c;
    L_0x3774:
        r0 = r107;
        r1 = r81;
        r106 = r0.get(r1);
        r106 = (org.telegram.ui.Cells.ChatMessageCell.BotButton) r106;
        goto L_0x36a6;
    L_0x3780:
        r8 = java.lang.System.currentTimeMillis();
        r0 = r53;
        r0.lastUpdateTime = r8;
        goto L_0x36c3;
    L_0x378b:
        r4 = r53.button;
        r4 = r4.text;
        r6 = org.telegram.ui.ActionBar.Theme.chat_botButtonPaint;
        r6 = r6.getFontMetricsInt();
        r8 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r9 = 0;
        r32 = org.telegram.messenger.Emoji.replaceEmoji(r4, r6, r8, r9);
        r4 = org.telegram.ui.ActionBar.Theme.chat_botButtonPaint;
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = r54 - r6;
        r6 = (float) r6;
        r8 = android.text.TextUtils.TruncateAt.END;
        r0 = r32;
        r32 = android.text.TextUtils.ellipsize(r0, r4, r6, r8);
        goto L_0x3728;
    L_0x37b7:
        r0 = r94;
        r1 = r146;
        r1.widthForButtons = r0;
    L_0x37bd:
        r0 = r146;
        r4 = r0.drawPinnedBottom;
        if (r4 == 0) goto L_0x380c;
    L_0x37c3:
        r0 = r146;
        r4 = r0.drawPinnedTop;
        if (r4 == 0) goto L_0x380c;
    L_0x37c9:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.totalHeight = r4;
    L_0x37d8:
        r0 = r147;
        r4 = r0.type;
        r6 = 13;
        if (r4 != r6) goto L_0x37f6;
    L_0x37e0:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        if (r4 >= r6) goto L_0x37f6;
    L_0x37ec:
        r4 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r146;
        r0.totalHeight = r4;
    L_0x37f6:
        r146.updateWaveform();
        r0 = r146;
        r1 = r64;
        r0.updateButtonState(r1);
        return;
    L_0x3801:
        r4 = 0;
        r0 = r146;
        r0.substractBackgroundHeight = r4;
        r4 = 0;
        r0 = r146;
        r0.keyboardHeight = r4;
        goto L_0x37bd;
    L_0x380c:
        r0 = r146;
        r4 = r0.drawPinnedBottom;
        if (r4 == 0) goto L_0x3822;
    L_0x3812:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.totalHeight = r4;
        goto L_0x37d8;
    L_0x3822:
        r0 = r146;
        r4 = r0.drawPinnedTop;
        if (r4 == 0) goto L_0x37d8;
    L_0x3828:
        r0 = r146;
        r4 = r0.pinnedBottom;
        if (r4 == 0) goto L_0x37d8;
    L_0x382e:
        r0 = r146;
        r4 = r0.currentPosition;
        if (r4 == 0) goto L_0x37d8;
    L_0x3834:
        r0 = r146;
        r4 = r0.currentPosition;
        r4 = r4.siblingHeights;
        if (r4 != 0) goto L_0x37d8;
    L_0x383c:
        r0 = r146;
        r4 = r0.totalHeight;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 - r6;
        r0 = r146;
        r0.totalHeight = r4;
        goto L_0x37d8;
    L_0x384c:
        r70 = move-exception;
        goto L_0x0cf6;
    L_0x384f:
        r70 = move-exception;
        r12 = r119;
        goto L_0x0b3e;
    L_0x3854:
        r99 = r26;
        goto L_0x1e51;
    L_0x3858:
        r99 = r26;
        goto L_0x1c70;
    L_0x385c:
        r14 = r140;
        goto L_0x182f;
    L_0x3860:
        r14 = r140;
        goto L_0x0de9;
    L_0x3864:
        r12 = r119;
        goto L_0x0be8;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatMessageCell.setMessageObject(org.telegram.messenger.MessageObject, org.telegram.messenger.MessageObject$GroupedMessages, boolean, boolean):void");
    }

    public void requestLayout() {
        if (!this.inLayout) {
            super.requestLayout();
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.currentMessageObject != null && (this.currentMessageObject.checkLayout() || !(this.currentPosition == null || this.lastHeight == AndroidUtilities.displaySize.y))) {
            this.inLayout = true;
            MessageObject messageObject = this.currentMessageObject;
            this.currentMessageObject = null;
            setMessageObject(messageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
            this.inLayout = false;
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), this.totalHeight + this.keyboardHeight);
        this.lastHeight = AndroidUtilities.displaySize.y;
    }

    private int getGroupPhotosWidth() {
        if (AndroidUtilities.isInMultiwindow || !AndroidUtilities.isTablet() || (AndroidUtilities.isSmallTablet() && getResources().getConfiguration().orientation != 2)) {
            return AndroidUtilities.displaySize.x;
        }
        int leftWidth = (AndroidUtilities.displaySize.x / 100) * 35;
        if (leftWidth < AndroidUtilities.dp(320.0f)) {
            leftWidth = AndroidUtilities.dp(320.0f);
        }
        return AndroidUtilities.displaySize.x - leftWidth;
    }

    @SuppressLint({"DrawAllocation"})
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.currentMessageObject != null) {
            if (changed || !this.wasLayout) {
                this.layoutWidth = getMeasuredWidth();
                this.layoutHeight = getMeasuredHeight() - this.substractBackgroundHeight;
                if (this.timeTextWidth < 0) {
                    this.timeTextWidth = AndroidUtilities.dp(10.0f);
                }
                this.timeLayout = new StaticLayout(this.currentTimeString, Theme.chat_timePaint, this.timeTextWidth + AndroidUtilities.dp(100.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                if (this.mediaBackground) {
                    if (this.currentMessageObject.isOutOwner()) {
                        this.timeX = (this.layoutWidth - this.timeWidth) - AndroidUtilities.dp(42.0f);
                    } else {
                        this.timeX = (this.isAvatarVisible ? AndroidUtilities.dp(48.0f) : 0) + ((this.backgroundWidth - AndroidUtilities.dp(4.0f)) - this.timeWidth);
                        if (!(this.currentPosition == null || this.currentPosition.leftSpanOffset == 0)) {
                            this.timeX += (int) Math.ceil((double) ((((float) this.currentPosition.leftSpanOffset) / 1000.0f) * ((float) getGroupPhotosWidth())));
                        }
                    }
                } else if (this.currentMessageObject.isOutOwner()) {
                    this.timeX = (this.layoutWidth - this.timeWidth) - AndroidUtilities.dp(38.5f);
                } else {
                    int dp;
                    int dp2 = (this.backgroundWidth - AndroidUtilities.dp(9.0f)) - this.timeWidth;
                    if (this.isAvatarVisible) {
                        dp = AndroidUtilities.dp(48.0f);
                    } else {
                        dp = 0;
                    }
                    this.timeX = dp + dp2;
                }
                if ((this.currentMessageObject.messageOwner.flags & 1024) != 0) {
                    this.viewsLayout = new StaticLayout(this.currentViewsString, Theme.chat_timePaint, this.viewsTextWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                } else {
                    this.viewsLayout = null;
                }
                if (this.isAvatarVisible) {
                    this.avatarImage.setImageCoords(AndroidUtilities.dp(6.0f), this.avatarImage.getImageY(), AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f));
                }
                this.wasLayout = true;
            }
            if (this.currentMessageObject.type == 0) {
                this.textY = AndroidUtilities.dp(10.0f) + this.namesOffset;
            }
            if (this.currentMessageObject.isRoundVideo()) {
                updatePlayingMessageProgress();
            }
            if (this.documentAttachType == 3) {
                if (this.currentMessageObject.isOutOwner()) {
                    this.seekBarX = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(57.0f);
                    this.buttonX = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(14.0f);
                    this.timeAudioX = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(67.0f);
                } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
                    this.seekBarX = AndroidUtilities.dp(114.0f);
                    this.buttonX = AndroidUtilities.dp(71.0f);
                    this.timeAudioX = AndroidUtilities.dp(124.0f);
                } else {
                    this.seekBarX = AndroidUtilities.dp(66.0f);
                    this.buttonX = AndroidUtilities.dp(23.0f);
                    this.timeAudioX = AndroidUtilities.dp(76.0f);
                }
                if (this.hasLinkPreview) {
                    this.seekBarX += AndroidUtilities.dp(10.0f);
                    this.buttonX += AndroidUtilities.dp(10.0f);
                    this.timeAudioX += AndroidUtilities.dp(10.0f);
                }
                this.seekBarWaveform.setSize(this.backgroundWidth - AndroidUtilities.dp((float) ((this.hasLinkPreview ? 10 : 0) + 92)), AndroidUtilities.dp(30.0f));
                this.seekBar.setSize(this.backgroundWidth - AndroidUtilities.dp((float) ((this.hasLinkPreview ? 10 : 0) + 72)), AndroidUtilities.dp(30.0f));
                this.seekBarY = (AndroidUtilities.dp(13.0f) + this.namesOffset) + this.mediaOffsetY;
                this.buttonY = (AndroidUtilities.dp(13.0f) + this.namesOffset) + this.mediaOffsetY;
                this.radialProgress.setProgressRect(this.buttonX, this.buttonY, this.buttonX + AndroidUtilities.dp(44.0f), this.buttonY + AndroidUtilities.dp(44.0f));
                updatePlayingMessageProgress();
            } else if (this.documentAttachType == 5) {
                if (this.currentMessageObject.isOutOwner()) {
                    this.seekBarX = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(56.0f);
                    this.buttonX = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(14.0f);
                    this.timeAudioX = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(67.0f);
                } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
                    this.seekBarX = AndroidUtilities.dp(113.0f);
                    this.buttonX = AndroidUtilities.dp(71.0f);
                    this.timeAudioX = AndroidUtilities.dp(124.0f);
                } else {
                    this.seekBarX = AndroidUtilities.dp(65.0f);
                    this.buttonX = AndroidUtilities.dp(23.0f);
                    this.timeAudioX = AndroidUtilities.dp(76.0f);
                }
                if (this.hasLinkPreview) {
                    this.seekBarX += AndroidUtilities.dp(10.0f);
                    this.buttonX += AndroidUtilities.dp(10.0f);
                    this.timeAudioX += AndroidUtilities.dp(10.0f);
                }
                this.seekBar.setSize(this.backgroundWidth - AndroidUtilities.dp((float) ((this.hasLinkPreview ? 10 : 0) + 65)), AndroidUtilities.dp(30.0f));
                this.seekBarY = (AndroidUtilities.dp(29.0f) + this.namesOffset) + this.mediaOffsetY;
                this.buttonY = (AndroidUtilities.dp(13.0f) + this.namesOffset) + this.mediaOffsetY;
                this.radialProgress.setProgressRect(this.buttonX, this.buttonY, this.buttonX + AndroidUtilities.dp(44.0f), this.buttonY + AndroidUtilities.dp(44.0f));
                updatePlayingMessageProgress();
            } else if (this.documentAttachType == 1 && !this.drawPhotoImage) {
                if (this.currentMessageObject.isOutOwner()) {
                    this.buttonX = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(14.0f);
                } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
                    this.buttonX = AndroidUtilities.dp(71.0f);
                } else {
                    this.buttonX = AndroidUtilities.dp(23.0f);
                }
                if (this.hasLinkPreview) {
                    this.buttonX += AndroidUtilities.dp(10.0f);
                }
                this.buttonY = (AndroidUtilities.dp(13.0f) + this.namesOffset) + this.mediaOffsetY;
                this.radialProgress.setProgressRect(this.buttonX, this.buttonY, this.buttonX + AndroidUtilities.dp(44.0f), this.buttonY + AndroidUtilities.dp(44.0f));
                this.photoImage.setImageCoords(this.buttonX - AndroidUtilities.dp(10.0f), this.buttonY - AndroidUtilities.dp(10.0f), this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
            } else if (this.currentMessageObject.type == 12) {
                if (this.currentMessageObject.isOutOwner()) {
                    x = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(14.0f);
                } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
                    x = AndroidUtilities.dp(72.0f);
                } else {
                    x = AndroidUtilities.dp(23.0f);
                }
                this.photoImage.setImageCoords(x, AndroidUtilities.dp(13.0f) + this.namesOffset, AndroidUtilities.dp(44.0f), AndroidUtilities.dp(44.0f));
            } else {
                if (this.currentMessageObject.type == 0 && (this.hasLinkPreview || this.hasGamePreview || this.hasInvoicePreview)) {
                    int linkX;
                    if (this.hasGamePreview) {
                        linkX = this.textX - AndroidUtilities.dp(10.0f);
                    } else if (this.hasInvoicePreview) {
                        linkX = this.textX + AndroidUtilities.dp(1.0f);
                    } else {
                        linkX = this.textX + AndroidUtilities.dp(1.0f);
                    }
                    if (this.isSmallImage) {
                        x = (this.backgroundWidth + linkX) - AndroidUtilities.dp(81.0f);
                    } else {
                        x = linkX + (this.hasInvoicePreview ? -AndroidUtilities.dp(6.3f) : AndroidUtilities.dp(10.0f));
                    }
                } else if (!this.currentMessageObject.isOutOwner()) {
                    if (this.isChat && this.isAvatarVisible) {
                        x = AndroidUtilities.dp(63.0f);
                    } else {
                        x = AndroidUtilities.dp(15.0f);
                    }
                    if (!(this.currentPosition == null || this.currentPosition.edge)) {
                        x -= AndroidUtilities.dp(10.0f);
                    }
                } else if (this.mediaBackground) {
                    x = (this.layoutWidth - this.backgroundWidth) - AndroidUtilities.dp(3.0f);
                } else {
                    x = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(6.0f);
                }
                if (this.currentPosition != null) {
                    if ((this.currentPosition.flags & 1) == 0) {
                        x -= AndroidUtilities.dp(4.0f);
                    }
                    if (this.currentPosition.leftSpanOffset != 0) {
                        x += (int) Math.ceil((double) ((((float) this.currentPosition.leftSpanOffset) / 1000.0f) * ((float) getGroupPhotosWidth())));
                    }
                }
                this.photoImage.setImageCoords(x, this.photoImage.getImageY(), this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
                this.buttonX = (int) (((float) x) + (((float) (this.photoImage.getImageWidth() - AndroidUtilities.dp(48.0f))) / 2.0f));
                this.buttonY = ((int) (((float) AndroidUtilities.dp(7.0f)) + (((float) (this.photoImage.getImageHeight() - AndroidUtilities.dp(48.0f))) / 2.0f))) + this.namesOffset;
                this.radialProgress.setProgressRect(this.buttonX, this.buttonY, this.buttonX + AndroidUtilities.dp(48.0f), this.buttonY + AndroidUtilities.dp(48.0f));
                this.deleteProgressRect.set((float) (this.buttonX + AndroidUtilities.dp(3.0f)), (float) (this.buttonY + AndroidUtilities.dp(3.0f)), (float) (this.buttonX + AndroidUtilities.dp(45.0f)), (float) (this.buttonY + AndroidUtilities.dp(45.0f)));
            }
        }
    }

    private void drawContent(Canvas canvas) {
        int a;
        int startY;
        int linkX;
        int linkPreviewY;
        int x;
        int y;
        float progress;
        int x1;
        int y1;
        RadialProgress radialProgress;
        String str;
        if (this.needNewVisiblePart && this.currentMessageObject.type == 0) {
            getLocalVisibleRect(this.scrollRect);
            setVisiblePart(this.scrollRect.top, this.scrollRect.bottom - this.scrollRect.top);
            this.needNewVisiblePart = false;
        }
        this.forceNotDrawTime = this.currentMessagesGroup != null;
        ImageReceiver imageReceiver = this.photoImage;
        int i = isDrawSelectedBackground() ? this.currentPosition != null ? 2 : 1 : 0;
        imageReceiver.setPressed(i);
        imageReceiver = this.photoImage;
        boolean z = (PhotoViewer.getInstance().isShowingImage(this.currentMessageObject) || SecretMediaViewer.getInstance().isShowingImage(this.currentMessageObject)) ? false : true;
        imageReceiver.setVisible(z, false);
        if (!this.photoImage.getVisible()) {
            this.mediaWasInvisible = true;
            this.timeWasInvisible = true;
        } else if (this.groupPhotoInvisible) {
            this.timeWasInvisible = true;
        } else if (this.mediaWasInvisible || this.timeWasInvisible) {
            if (this.mediaWasInvisible) {
                this.controlsAlpha = 0.0f;
                this.mediaWasInvisible = false;
            }
            if (this.timeWasInvisible) {
                this.timeAlpha = 0.0f;
                this.timeWasInvisible = false;
            }
            this.lastControlsAlphaChangeTime = System.currentTimeMillis();
            this.totalChangeTime = 0;
        }
        this.radialProgress.setHideCurrentDrawable(false);
        this.radialProgress.setProgressColor(Theme.getColor(Theme.key_chat_mediaProgress));
        boolean imageDrawn = false;
        if (this.currentMessageObject.type == 0) {
            int b;
            if (this.currentMessageObject.isOutOwner()) {
                this.textX = this.currentBackgroundDrawable.getBounds().left + AndroidUtilities.dp(11.0f);
            } else {
                int i2 = this.currentBackgroundDrawable.getBounds().left;
                float f = (this.mediaBackground || !this.drawPinnedBottom) ? 17.0f : 11.0f;
                this.textX = AndroidUtilities.dp(f) + i2;
            }
            if (this.hasGamePreview) {
                this.textX += AndroidUtilities.dp(11.0f);
                this.textY = AndroidUtilities.dp(14.0f) + this.namesOffset;
                if (this.siteNameLayout != null) {
                    this.textY += this.siteNameLayout.getLineBottom(this.siteNameLayout.getLineCount() - 1);
                }
            } else if (this.hasInvoicePreview) {
                this.textY = AndroidUtilities.dp(14.0f) + this.namesOffset;
                if (this.siteNameLayout != null) {
                    this.textY += this.siteNameLayout.getLineBottom(this.siteNameLayout.getLineCount() - 1);
                }
            } else {
                this.textY = AndroidUtilities.dp(10.0f) + this.namesOffset;
            }
            if (!(this.currentMessageObject.textXOffset == 0.0f || this.replyNameLayout == null)) {
                int diff = (this.backgroundWidth - AndroidUtilities.dp(31.0f)) - this.currentMessageObject.textWidth;
                if (!this.hasNewLineForTime) {
                    diff -= AndroidUtilities.dp((float) ((this.currentMessageObject.isOutOwner() ? 20 : 0) + 4)) + this.timeWidth;
                }
                if (diff > 0) {
                    this.textX += diff;
                }
            }
            if (!(this.currentMessageObject.textLayoutBlocks == null || this.currentMessageObject.textLayoutBlocks.isEmpty())) {
                if (this.fullyDraw) {
                    this.firstVisibleBlockNum = 0;
                    this.lastVisibleBlockNum = this.currentMessageObject.textLayoutBlocks.size();
                }
                if (this.firstVisibleBlockNum >= 0) {
                    a = this.firstVisibleBlockNum;
                    while (a <= this.lastVisibleBlockNum && a < this.currentMessageObject.textLayoutBlocks.size()) {
                        TextLayoutBlock block = (TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(a);
                        canvas.save();
                        canvas.translate((float) (this.textX - (block.isRtl() ? (int) Math.ceil((double) this.currentMessageObject.textXOffset) : 0)), ((float) this.textY) + block.textYOffset);
                        if (this.pressedLink != null && a == this.linkBlockNum) {
                            for (b = 0; b < this.urlPath.size(); b++) {
                                canvas.drawPath((Path) this.urlPath.get(b), Theme.chat_urlPaint);
                            }
                        }
                        if (a == this.linkSelectionBlockNum && !this.urlPathSelection.isEmpty()) {
                            for (b = 0; b < this.urlPathSelection.size(); b++) {
                                canvas.drawPath((Path) this.urlPathSelection.get(b), Theme.chat_textSearchSelectionPaint);
                            }
                        }
                        try {
                            block.textLayout.draw(canvas);
                        } catch (Throwable e) {
                            FileLog.e(e);
                        }
                        canvas.restore();
                        a++;
                    }
                }
            }
            if (this.hasLinkPreview || this.hasGamePreview || this.hasInvoicePreview) {
                int size;
                if (this.hasGamePreview) {
                    startY = AndroidUtilities.dp(14.0f) + this.namesOffset;
                    linkX = this.textX - AndroidUtilities.dp(10.0f);
                } else if (this.hasInvoicePreview) {
                    startY = AndroidUtilities.dp(14.0f) + this.namesOffset;
                    linkX = this.textX + AndroidUtilities.dp(1.0f);
                } else {
                    startY = (this.textY + this.currentMessageObject.textHeight) + AndroidUtilities.dp(8.0f);
                    linkX = this.textX + AndroidUtilities.dp(1.0f);
                }
                linkPreviewY = startY;
                int smallImageStartY = 0;
                if (!this.hasInvoicePreview) {
                    Theme.chat_replyLinePaint.setColor(Theme.getColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outPreviewLine : Theme.key_chat_inPreviewLine));
                    canvas.drawRect((float) linkX, (float) (linkPreviewY - AndroidUtilities.dp(3.0f)), (float) (AndroidUtilities.dp(2.0f) + linkX), (float) ((this.linkPreviewHeight + linkPreviewY) + AndroidUtilities.dp(3.0f)), Theme.chat_replyLinePaint);
                }
                if (this.siteNameLayout != null) {
                    Theme.chat_replyNamePaint.setColor(Theme.getColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outSiteNameText : Theme.key_chat_inSiteNameText));
                    canvas.save();
                    if (this.hasInvoicePreview) {
                        i = 0;
                    } else {
                        i = AndroidUtilities.dp(10.0f);
                    }
                    canvas.translate((float) (i + linkX), (float) (linkPreviewY - AndroidUtilities.dp(3.0f)));
                    this.siteNameLayout.draw(canvas);
                    canvas.restore();
                    linkPreviewY += this.siteNameLayout.getLineBottom(this.siteNameLayout.getLineCount() - 1);
                }
                if ((this.hasGamePreview || this.hasInvoicePreview) && this.currentMessageObject.textHeight != 0) {
                    startY += this.currentMessageObject.textHeight + AndroidUtilities.dp(4.0f);
                    linkPreviewY += this.currentMessageObject.textHeight + AndroidUtilities.dp(4.0f);
                }
                if (this.drawPhotoImage && this.drawInstantView) {
                    if (linkPreviewY != startY) {
                        linkPreviewY += AndroidUtilities.dp(2.0f);
                    }
                    this.photoImage.setImageCoords(AndroidUtilities.dp(10.0f) + linkX, linkPreviewY, this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
                    if (this.drawImageButton) {
                        size = AndroidUtilities.dp(48.0f);
                        this.buttonX = (int) (((float) this.photoImage.getImageX()) + (((float) (this.photoImage.getImageWidth() - size)) / 2.0f));
                        this.buttonY = (int) (((float) this.photoImage.getImageY()) + (((float) (this.photoImage.getImageHeight() - size)) / 2.0f));
                        this.radialProgress.setProgressRect(this.buttonX, this.buttonY, this.buttonX + size, this.buttonY + size);
                    }
                    imageDrawn = this.photoImage.draw(canvas);
                    linkPreviewY += this.photoImage.getImageHeight() + AndroidUtilities.dp(6.0f);
                }
                if (this.currentMessageObject.isOutOwner()) {
                    Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
                    Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
                } else {
                    Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
                    Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
                }
                if (this.titleLayout != null) {
                    if (linkPreviewY != startY) {
                        linkPreviewY += AndroidUtilities.dp(2.0f);
                    }
                    smallImageStartY = linkPreviewY - AndroidUtilities.dp(1.0f);
                    canvas.save();
                    canvas.translate((float) ((AndroidUtilities.dp(10.0f) + linkX) + this.titleX), (float) (linkPreviewY - AndroidUtilities.dp(3.0f)));
                    this.titleLayout.draw(canvas);
                    canvas.restore();
                    linkPreviewY += this.titleLayout.getLineBottom(this.titleLayout.getLineCount() - 1);
                }
                if (this.authorLayout != null) {
                    if (linkPreviewY != startY) {
                        linkPreviewY += AndroidUtilities.dp(2.0f);
                    }
                    if (smallImageStartY == 0) {
                        smallImageStartY = linkPreviewY - AndroidUtilities.dp(1.0f);
                    }
                    canvas.save();
                    canvas.translate((float) ((AndroidUtilities.dp(10.0f) + linkX) + this.authorX), (float) (linkPreviewY - AndroidUtilities.dp(3.0f)));
                    this.authorLayout.draw(canvas);
                    canvas.restore();
                    linkPreviewY += this.authorLayout.getLineBottom(this.authorLayout.getLineCount() - 1);
                }
                if (this.descriptionLayout != null) {
                    if (linkPreviewY != startY) {
                        linkPreviewY += AndroidUtilities.dp(2.0f);
                    }
                    if (smallImageStartY == 0) {
                        smallImageStartY = linkPreviewY - AndroidUtilities.dp(1.0f);
                    }
                    this.descriptionY = linkPreviewY - AndroidUtilities.dp(3.0f);
                    canvas.save();
                    canvas.translate((float) (((this.hasInvoicePreview ? 0 : AndroidUtilities.dp(10.0f)) + linkX) + this.descriptionX), (float) this.descriptionY);
                    if (this.pressedLink != null && this.linkBlockNum == -10) {
                        for (b = 0; b < this.urlPath.size(); b++) {
                            canvas.drawPath((Path) this.urlPath.get(b), Theme.chat_urlPaint);
                        }
                    }
                    this.descriptionLayout.draw(canvas);
                    canvas.restore();
                    linkPreviewY += this.descriptionLayout.getLineBottom(this.descriptionLayout.getLineCount() - 1);
                }
                if (this.drawPhotoImage && !this.drawInstantView) {
                    if (linkPreviewY != startY) {
                        linkPreviewY += AndroidUtilities.dp(2.0f);
                    }
                    if (this.isSmallImage) {
                        this.photoImage.setImageCoords((this.backgroundWidth + linkX) - AndroidUtilities.dp(81.0f), smallImageStartY, this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
                    } else {
                        imageReceiver = this.photoImage;
                        if (this.hasInvoicePreview) {
                            i = -AndroidUtilities.dp(6.3f);
                        } else {
                            i = AndroidUtilities.dp(10.0f);
                        }
                        imageReceiver.setImageCoords(i + linkX, linkPreviewY, this.photoImage.getImageWidth(), this.photoImage.getImageHeight());
                        if (this.drawImageButton) {
                            size = AndroidUtilities.dp(48.0f);
                            this.buttonX = (int) (((float) this.photoImage.getImageX()) + (((float) (this.photoImage.getImageWidth() - size)) / 2.0f));
                            this.buttonY = (int) (((float) this.photoImage.getImageY()) + (((float) (this.photoImage.getImageHeight() - size)) / 2.0f));
                            this.radialProgress.setProgressRect(this.buttonX, this.buttonY, this.buttonX + size, this.buttonY + size);
                        }
                    }
                    if (this.currentMessageObject.isRoundVideo() && MediaController.getInstance().isPlayingMessage(this.currentMessageObject) && MediaController.getInstance().isRoundVideoDrawingReady()) {
                        imageDrawn = true;
                        this.drawTime = true;
                    } else {
                        imageDrawn = this.photoImage.draw(canvas);
                    }
                }
                if (this.photosCountLayout != null && this.photoImage.getVisible()) {
                    x = ((this.photoImage.getImageX() + this.photoImage.getImageWidth()) - AndroidUtilities.dp(8.0f)) - this.photosCountWidth;
                    y = (this.photoImage.getImageY() + this.photoImage.getImageHeight()) - AndroidUtilities.dp(19.0f);
                    this.rect.set((float) (x - AndroidUtilities.dp(4.0f)), (float) (y - AndroidUtilities.dp(1.5f)), (float) ((this.photosCountWidth + x) + AndroidUtilities.dp(4.0f)), (float) (AndroidUtilities.dp(14.5f) + y));
                    int oldAlpha = Theme.chat_timeBackgroundPaint.getAlpha();
                    Theme.chat_timeBackgroundPaint.setAlpha((int) (((float) oldAlpha) * this.controlsAlpha));
                    Theme.chat_durationPaint.setAlpha((int) (255.0f * this.controlsAlpha));
                    canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                    Theme.chat_timeBackgroundPaint.setAlpha(oldAlpha);
                    canvas.save();
                    canvas.translate((float) x, (float) y);
                    this.photosCountLayout.draw(canvas);
                    canvas.restore();
                    Theme.chat_durationPaint.setAlpha(255);
                }
                if (this.videoInfoLayout != null && (!this.drawPhotoImage || this.photoImage.getVisible())) {
                    if (!this.hasGamePreview && !this.hasInvoicePreview) {
                        x = ((this.photoImage.getImageX() + this.photoImage.getImageWidth()) - AndroidUtilities.dp(8.0f)) - this.durationWidth;
                        y = (this.photoImage.getImageY() + this.photoImage.getImageHeight()) - AndroidUtilities.dp(19.0f);
                        this.rect.set((float) (x - AndroidUtilities.dp(4.0f)), (float) (y - AndroidUtilities.dp(1.5f)), (float) ((this.durationWidth + x) + AndroidUtilities.dp(4.0f)), (float) (AndroidUtilities.dp(14.5f) + y));
                        canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                    } else if (this.drawPhotoImage) {
                        x = this.photoImage.getImageX() + AndroidUtilities.dp(8.5f);
                        y = this.photoImage.getImageY() + AndroidUtilities.dp(6.0f);
                        this.rect.set((float) (x - AndroidUtilities.dp(4.0f)), (float) (y - AndroidUtilities.dp(1.5f)), (float) ((this.durationWidth + x) + AndroidUtilities.dp(4.0f)), (float) (AndroidUtilities.dp(16.5f) + y));
                        canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                    } else {
                        x = linkX;
                        y = linkPreviewY;
                    }
                    canvas.save();
                    canvas.translate((float) x, (float) y);
                    if (this.hasInvoicePreview) {
                        if (this.drawPhotoImage) {
                            Theme.chat_shipmentPaint.setColor(Theme.getColor(Theme.key_chat_previewGameText));
                        } else if (this.currentMessageObject.isOutOwner()) {
                            Theme.chat_shipmentPaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
                        } else {
                            Theme.chat_shipmentPaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
                        }
                    }
                    this.videoInfoLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.drawInstantView) {
                    Drawable instantDrawable;
                    int instantY = (this.linkPreviewHeight + startY) + AndroidUtilities.dp(10.0f);
                    Paint backPaint = Theme.chat_instantViewRectPaint;
                    if (this.currentMessageObject.isOutOwner()) {
                        instantDrawable = Theme.chat_msgOutInstantDrawable;
                        Theme.chat_instantViewPaint.setColor(Theme.getColor(Theme.key_chat_outPreviewInstantText));
                        backPaint.setColor(Theme.getColor(Theme.key_chat_outPreviewInstantText));
                    } else {
                        instantDrawable = Theme.chat_msgInInstantDrawable;
                        Theme.chat_instantViewPaint.setColor(Theme.getColor(Theme.key_chat_inPreviewInstantText));
                        backPaint.setColor(Theme.getColor(Theme.key_chat_inPreviewInstantText));
                    }
                    if (VERSION.SDK_INT >= 21) {
                        this.instantViewSelectorDrawable.setBounds(linkX, instantY, this.instantWidth + linkX, AndroidUtilities.dp(36.0f) + instantY);
                        this.instantViewSelectorDrawable.draw(canvas);
                    }
                    this.rect.set((float) linkX, (float) instantY, (float) (this.instantWidth + linkX), (float) (AndroidUtilities.dp(36.0f) + instantY));
                    canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(6.0f), (float) AndroidUtilities.dp(6.0f), backPaint);
                    if (this.drawInstantViewType == 0) {
                        BaseCell.setDrawableBounds(instantDrawable, (this.instantTextX + linkX) - AndroidUtilities.dp(15.0f), AndroidUtilities.dp(11.5f) + instantY, AndroidUtilities.dp(9.0f), AndroidUtilities.dp(13.0f));
                        instantDrawable.draw(canvas);
                    }
                    if (this.instantViewLayout != null) {
                        canvas.save();
                        canvas.translate((float) (this.instantTextX + linkX), (float) (AndroidUtilities.dp(10.5f) + instantY));
                        this.instantViewLayout.draw(canvas);
                        canvas.restore();
                    }
                }
            }
            this.drawTime = true;
        } else if (this.drawPhotoImage) {
            if (this.currentMessageObject.isRoundVideo() && MediaController.getInstance().isPlayingMessage(this.currentMessageObject) && MediaController.getInstance().isRoundVideoDrawingReady()) {
                imageDrawn = true;
                this.drawTime = true;
            } else {
                if (this.currentMessageObject.type == 5 && Theme.chat_roundVideoShadow != null) {
                    x = this.photoImage.getImageX() - AndroidUtilities.dp(3.0f);
                    y = this.photoImage.getImageY() - AndroidUtilities.dp(2.0f);
                    Theme.chat_roundVideoShadow.setAlpha((int) (this.photoImage.getCurrentAlpha() * 255.0f));
                    Theme.chat_roundVideoShadow.setBounds(x, y, (AndroidUtilities.roundMessageSize + x) + AndroidUtilities.dp(6.0f), (AndroidUtilities.roundMessageSize + y) + AndroidUtilities.dp(6.0f));
                    Theme.chat_roundVideoShadow.draw(canvas);
                }
                imageDrawn = this.photoImage.draw(canvas);
                boolean drawTimeOld = this.drawTime;
                this.drawTime = this.photoImage.getVisible();
                if (!(this.currentPosition == null || drawTimeOld == this.drawTime)) {
                    ViewGroup viewGroup = (ViewGroup) getParent();
                    if (viewGroup != null) {
                        if (this.currentPosition.last) {
                            viewGroup.invalidate();
                        } else {
                            int count = viewGroup.getChildCount();
                            for (a = 0; a < count; a++) {
                                View child = viewGroup.getChildAt(a);
                                if (child != this && (child instanceof ChatMessageCell)) {
                                    ChatMessageCell cell = (ChatMessageCell) child;
                                    if (cell.getCurrentMessagesGroup() == this.currentMessagesGroup) {
                                        GroupedMessagePosition position = cell.getCurrentPosition();
                                        if (position.last && position.maxY == this.currentPosition.maxY && (cell.timeX - AndroidUtilities.dp(4.0f)) + cell.getLeft() < getRight()) {
                                            cell.groupPhotoInvisible = !this.drawTime;
                                            cell.invalidate();
                                            viewGroup.invalidate();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.buttonState == -1 && this.currentMessageObject.isSecretPhoto() && !MediaController.getInstance().isPlayingMessage(this.currentMessageObject) && this.photoImage.getVisible()) {
            int drawable = 4;
            if (this.currentMessageObject.messageOwner.destroyTime != 0) {
                if (this.currentMessageObject.isOutOwner()) {
                    drawable = 6;
                } else {
                    drawable = 5;
                }
            }
            BaseCell.setDrawableBounds(Theme.chat_photoStatesDrawables[drawable][this.buttonPressed], this.buttonX, this.buttonY);
            Theme.chat_photoStatesDrawables[drawable][this.buttonPressed].setAlpha((int) ((255.0f * (1.0f - this.radialProgress.getAlpha())) * this.controlsAlpha));
            Theme.chat_photoStatesDrawables[drawable][this.buttonPressed].draw(canvas);
            if (this.currentMessageObject.messageOwner.destroyTime != 0) {
                if (!this.currentMessageObject.isOutOwner()) {
                    progress = ((float) Math.max(0, (((long) this.currentMessageObject.messageOwner.destroyTime) * 1000) - (System.currentTimeMillis() + ((long) (ConnectionsManager.getInstance(this.currentAccount).getTimeDifference() * 1000))))) / (((float) this.currentMessageObject.messageOwner.ttl) * 1000.0f);
                    Theme.chat_deleteProgressPaint.setAlpha((int) (255.0f * this.controlsAlpha));
                    canvas.drawArc(this.deleteProgressRect, -90.0f, -360.0f * progress, true, Theme.chat_deleteProgressPaint);
                    if (progress != 0.0f) {
                        int offset = AndroidUtilities.dp(2.0f);
                        invalidate(((int) this.deleteProgressRect.left) - offset, ((int) this.deleteProgressRect.top) - offset, ((int) this.deleteProgressRect.right) + (offset * 2), ((int) this.deleteProgressRect.bottom) + (offset * 2));
                    }
                }
                updateSecretTimeText(this.currentMessageObject);
            }
        }
        if (this.documentAttachType == 2 || this.currentMessageObject.type == 8) {
            if (!(!this.photoImage.getVisible() || this.hasGamePreview || this.currentMessageObject.isSecretPhoto())) {
                oldAlpha = ((BitmapDrawable) Theme.chat_msgMediaMenuDrawable).getPaint().getAlpha();
                Theme.chat_msgMediaMenuDrawable.setAlpha((int) (((float) oldAlpha) * this.controlsAlpha));
                Drawable drawable2 = Theme.chat_msgMediaMenuDrawable;
                i2 = (this.photoImage.getImageX() + this.photoImage.getImageWidth()) - AndroidUtilities.dp(14.0f);
                this.otherX = i2;
                int imageY = this.photoImage.getImageY() + AndroidUtilities.dp(8.1f);
                this.otherY = imageY;
                BaseCell.setDrawableBounds(drawable2, i2, imageY);
                Theme.chat_msgMediaMenuDrawable.draw(canvas);
                Theme.chat_msgMediaMenuDrawable.setAlpha(oldAlpha);
            }
        } else if (this.documentAttachType == 7 || this.currentMessageObject.type == 5) {
            if (this.durationLayout != null) {
                boolean playing = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (playing) {
                    this.rect.set(((float) this.photoImage.getImageX()) + AndroidUtilities.dpf2(1.5f), ((float) this.photoImage.getImageY()) + AndroidUtilities.dpf2(1.5f), ((float) this.photoImage.getImageX2()) - AndroidUtilities.dpf2(1.5f), ((float) this.photoImage.getImageY2()) - AndroidUtilities.dpf2(1.5f));
                    canvas.drawArc(this.rect, -90.0f, this.currentMessageObject.audioProgress * 360.0f, false, Theme.chat_radialProgressPaint);
                }
                if (this.documentAttachType == 7) {
                    x1 = this.backgroundDrawableLeft + AndroidUtilities.dp(this.currentMessageObject.isOutOwner() ? 12.0f : 18.0f);
                    i2 = this.layoutHeight;
                    if (this.drawPinnedBottom) {
                        i = 2;
                    } else {
                        i = 0;
                    }
                    y1 = (i2 - AndroidUtilities.dp(6.3f - ((float) i))) - this.timeLayout.getHeight();
                } else {
                    x1 = this.backgroundDrawableLeft + AndroidUtilities.dp(8.0f);
                    y1 = this.layoutHeight - AndroidUtilities.dp(28.0f);
                    this.rect.set((float) x1, (float) y1, (float) ((this.timeWidthAudio + x1) + AndroidUtilities.dp(22.0f)), (float) (AndroidUtilities.dp(17.0f) + y1));
                    canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(4.0f), Theme.chat_actionBackgroundPaint);
                    if (playing || !this.currentMessageObject.isContentUnread()) {
                        if (!playing || MediaController.getInstance().isMessagePaused()) {
                            this.roundVideoPlayingDrawable.stop();
                        } else {
                            this.roundVideoPlayingDrawable.start();
                        }
                        BaseCell.setDrawableBounds(this.roundVideoPlayingDrawable, (this.timeWidthAudio + x1) + AndroidUtilities.dp(6.0f), AndroidUtilities.dp(2.3f) + y1);
                        this.roundVideoPlayingDrawable.draw(canvas);
                    } else {
                        Theme.chat_docBackPaint.setColor(Theme.getColor(Theme.key_chat_mediaTimeText));
                        canvas.drawCircle((float) ((this.timeWidthAudio + x1) + AndroidUtilities.dp(12.0f)), (float) (AndroidUtilities.dp(8.3f) + y1), (float) AndroidUtilities.dp(3.0f), Theme.chat_docBackPaint);
                    }
                    x1 += AndroidUtilities.dp(4.0f);
                    y1 += AndroidUtilities.dp(1.7f);
                }
                canvas.save();
                canvas.translate((float) x1, (float) y1);
                this.durationLayout.draw(canvas);
                canvas.restore();
            }
        } else if (this.documentAttachType == 5) {
            if (this.currentMessageObject.isOutOwner()) {
                Theme.chat_audioTitlePaint.setColor(Theme.getColor(Theme.key_chat_outAudioTitleText));
                Theme.chat_audioPerformerPaint.setColor(Theme.getColor(Theme.key_chat_outAudioPerfomerText));
                Theme.chat_audioTimePaint.setColor(Theme.getColor(Theme.key_chat_outAudioDurationText));
                radialProgress = this.radialProgress;
                str = (isDrawSelectedBackground() || this.buttonPressed != 0) ? Theme.key_chat_outAudioSelectedProgress : Theme.key_chat_outAudioProgress;
                radialProgress.setProgressColor(Theme.getColor(str));
            } else {
                Theme.chat_audioTitlePaint.setColor(Theme.getColor(Theme.key_chat_inAudioTitleText));
                Theme.chat_audioPerformerPaint.setColor(Theme.getColor(Theme.key_chat_inAudioPerfomerText));
                Theme.chat_audioTimePaint.setColor(Theme.getColor(Theme.key_chat_inAudioDurationText));
                radialProgress = this.radialProgress;
                str = (isDrawSelectedBackground() || this.buttonPressed != 0) ? Theme.key_chat_inAudioSelectedProgress : Theme.key_chat_inAudioProgress;
                radialProgress.setProgressColor(Theme.getColor(str));
            }
            this.radialProgress.draw(canvas);
            canvas.save();
            canvas.translate((float) (this.timeAudioX + this.songX), (float) ((AndroidUtilities.dp(13.0f) + this.namesOffset) + this.mediaOffsetY));
            this.songLayout.draw(canvas);
            canvas.restore();
            canvas.save();
            if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                canvas.translate((float) this.seekBarX, (float) this.seekBarY);
                this.seekBar.draw(canvas);
            } else {
                canvas.translate((float) (this.timeAudioX + this.performerX), (float) ((AndroidUtilities.dp(35.0f) + this.namesOffset) + this.mediaOffsetY));
                this.performerLayout.draw(canvas);
            }
            canvas.restore();
            canvas.save();
            canvas.translate((float) this.timeAudioX, (float) ((AndroidUtilities.dp(57.0f) + this.namesOffset) + this.mediaOffsetY));
            this.durationLayout.draw(canvas);
            canvas.restore();
            Drawable menuDrawable = this.currentMessageObject.isOutOwner() ? isDrawSelectedBackground() ? Theme.chat_msgOutMenuSelectedDrawable : Theme.chat_msgOutMenuDrawable : isDrawSelectedBackground() ? Theme.chat_msgInMenuSelectedDrawable : Theme.chat_msgInMenuDrawable;
            i = (this.backgroundWidth + this.buttonX) - AndroidUtilities.dp(this.currentMessageObject.type == 0 ? 58.0f : 48.0f);
            this.otherX = i;
            i2 = this.buttonY - AndroidUtilities.dp(5.0f);
            this.otherY = i2;
            BaseCell.setDrawableBounds(menuDrawable, i, i2);
            menuDrawable.draw(canvas);
        } else if (this.documentAttachType == 3) {
            if (this.currentMessageObject.isOutOwner()) {
                Theme.chat_audioTimePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outAudioDurationSelectedText : Theme.key_chat_outAudioDurationText));
                radialProgress = this.radialProgress;
                str = (isDrawSelectedBackground() || this.buttonPressed != 0) ? Theme.key_chat_outAudioSelectedProgress : Theme.key_chat_outAudioProgress;
                radialProgress.setProgressColor(Theme.getColor(str));
            } else {
                Theme.chat_audioTimePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inAudioDurationSelectedText : Theme.key_chat_inAudioDurationText));
                radialProgress = this.radialProgress;
                str = (isDrawSelectedBackground() || this.buttonPressed != 0) ? Theme.key_chat_inAudioSelectedProgress : Theme.key_chat_inAudioProgress;
                radialProgress.setProgressColor(Theme.getColor(str));
            }
            this.radialProgress.draw(canvas);
            canvas.save();
            if (this.useSeekBarWaweform) {
                canvas.translate((float) (this.seekBarX + AndroidUtilities.dp(13.0f)), (float) this.seekBarY);
                this.seekBarWaveform.draw(canvas);
            } else {
                canvas.translate((float) this.seekBarX, (float) this.seekBarY);
                this.seekBar.draw(canvas);
            }
            canvas.restore();
            canvas.save();
            canvas.translate((float) this.timeAudioX, (float) ((AndroidUtilities.dp(44.0f) + this.namesOffset) + this.mediaOffsetY));
            this.durationLayout.draw(canvas);
            canvas.restore();
            if (this.currentMessageObject.type != 0 && this.currentMessageObject.isContentUnread()) {
                Theme.chat_docBackPaint.setColor(Theme.getColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outVoiceSeekbarFill : Theme.key_chat_inVoiceSeekbarFill));
                canvas.drawCircle((float) ((this.timeAudioX + this.timeWidthAudio) + AndroidUtilities.dp(6.0f)), (float) ((AndroidUtilities.dp(51.0f) + this.namesOffset) + this.mediaOffsetY), (float) AndroidUtilities.dp(3.0f), Theme.chat_docBackPaint);
            }
        }
        if (this.currentMessageObject.type == 1 || this.documentAttachType == 4) {
            if (this.photoImage.getVisible()) {
                if (!this.currentMessageObject.isSecretPhoto() && this.documentAttachType == 4) {
                    oldAlpha = ((BitmapDrawable) Theme.chat_msgMediaMenuDrawable).getPaint().getAlpha();
                    Theme.chat_msgMediaMenuDrawable.setAlpha((int) (((float) oldAlpha) * this.controlsAlpha));
                    drawable2 = Theme.chat_msgMediaMenuDrawable;
                    i2 = (this.photoImage.getImageX() + this.photoImage.getImageWidth()) - AndroidUtilities.dp(14.0f);
                    this.otherX = i2;
                    imageY = this.photoImage.getImageY() + AndroidUtilities.dp(8.1f);
                    this.otherY = imageY;
                    BaseCell.setDrawableBounds(drawable2, i2, imageY);
                    Theme.chat_msgMediaMenuDrawable.draw(canvas);
                    Theme.chat_msgMediaMenuDrawable.setAlpha(oldAlpha);
                }
                if (!(this.forceNotDrawTime || this.infoLayout == null || (this.buttonState != 1 && this.buttonState != 0 && this.buttonState != 3 && !this.currentMessageObject.isSecretPhoto()))) {
                    Theme.chat_infoPaint.setColor(Theme.getColor(Theme.key_chat_mediaInfoText));
                    x1 = this.photoImage.getImageX() + AndroidUtilities.dp(4.0f);
                    y1 = this.photoImage.getImageY() + AndroidUtilities.dp(4.0f);
                    this.rect.set((float) x1, (float) y1, (float) ((this.infoWidth + x1) + AndroidUtilities.dp(8.0f)), (float) (AndroidUtilities.dp(16.5f) + y1));
                    oldAlpha = Theme.chat_timeBackgroundPaint.getAlpha();
                    Theme.chat_timeBackgroundPaint.setAlpha((int) (((float) oldAlpha) * this.controlsAlpha));
                    canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(4.0f), Theme.chat_timeBackgroundPaint);
                    Theme.chat_timeBackgroundPaint.setAlpha(oldAlpha);
                    canvas.save();
                    canvas.translate((float) (this.photoImage.getImageX() + AndroidUtilities.dp(8.0f)), (float) (this.photoImage.getImageY() + AndroidUtilities.dp(5.5f)));
                    Theme.chat_infoPaint.setAlpha((int) (255.0f * this.controlsAlpha));
                    this.infoLayout.draw(canvas);
                    canvas.restore();
                    Theme.chat_infoPaint.setAlpha(255);
                }
            }
        } else if (this.currentMessageObject.type == 4) {
            if (this.docTitleLayout != null) {
                if (this.currentMessageObject.isOutOwner()) {
                    if (this.currentMessageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                        Theme.chat_locationTitlePaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
                    } else {
                        Theme.chat_locationTitlePaint.setColor(Theme.getColor(Theme.key_chat_outVenueNameText));
                    }
                    Theme.chat_locationAddressPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outVenueInfoSelectedText : Theme.key_chat_outVenueInfoText));
                } else {
                    if (this.currentMessageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                        Theme.chat_locationTitlePaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
                    } else {
                        Theme.chat_locationTitlePaint.setColor(Theme.getColor(Theme.key_chat_inVenueNameText));
                    }
                    Theme.chat_locationAddressPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inVenueInfoSelectedText : Theme.key_chat_inVenueInfoText));
                }
                if (this.currentMessageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                    int cy = this.photoImage.getImageY2() + AndroidUtilities.dp(30.0f);
                    if (!this.locationExpired) {
                        this.forceNotDrawTime = true;
                        progress = 1.0f - (((float) Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - this.currentMessageObject.messageOwner.date)) / ((float) this.currentMessageObject.messageOwner.media.period));
                        this.rect.set((float) (this.photoImage.getImageX2() - AndroidUtilities.dp(43.0f)), (float) (cy - AndroidUtilities.dp(15.0f)), (float) (this.photoImage.getImageX2() - AndroidUtilities.dp(13.0f)), (float) (AndroidUtilities.dp(15.0f) + cy));
                        if (this.currentMessageObject.isOutOwner()) {
                            Theme.chat_radialProgress2Paint.setColor(Theme.getColor(Theme.key_chat_outInstant));
                            Theme.chat_livePaint.setColor(Theme.getColor(Theme.key_chat_outInstant));
                        } else {
                            Theme.chat_radialProgress2Paint.setColor(Theme.getColor(Theme.key_chat_inInstant));
                            Theme.chat_livePaint.setColor(Theme.getColor(Theme.key_chat_inInstant));
                        }
                        Theme.chat_radialProgress2Paint.setAlpha(50);
                        canvas.drawCircle(this.rect.centerX(), this.rect.centerY(), (float) AndroidUtilities.dp(15.0f), Theme.chat_radialProgress2Paint);
                        Theme.chat_radialProgress2Paint.setAlpha(255);
                        canvas.drawArc(this.rect, -90.0f, -360.0f * progress, false, Theme.chat_radialProgress2Paint);
                        String text = LocaleController.formatLocationLeftTime(Math.abs(this.currentMessageObject.messageOwner.media.period - (ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - this.currentMessageObject.messageOwner.date)));
                        canvas.drawText(text, this.rect.centerX() - (Theme.chat_livePaint.measureText(text) / 2.0f), (float) (AndroidUtilities.dp(4.0f) + cy), Theme.chat_livePaint);
                        canvas.save();
                        canvas.translate((float) (this.photoImage.getImageX() + AndroidUtilities.dp(10.0f)), (float) (this.photoImage.getImageY2() + AndroidUtilities.dp(10.0f)));
                        this.docTitleLayout.draw(canvas);
                        canvas.translate(0.0f, (float) AndroidUtilities.dp(23.0f));
                        this.infoLayout.draw(canvas);
                        canvas.restore();
                    }
                    int cx = (this.photoImage.getImageX() + (this.photoImage.getImageWidth() / 2)) - AndroidUtilities.dp(31.0f);
                    cy = (this.photoImage.getImageY() + (this.photoImage.getImageHeight() / 2)) - AndroidUtilities.dp(38.0f);
                    BaseCell.setDrawableBounds(Theme.chat_msgAvatarLiveLocationDrawable, cx, cy);
                    Theme.chat_msgAvatarLiveLocationDrawable.draw(canvas);
                    this.locationImageReceiver.setImageCoords(AndroidUtilities.dp(5.0f) + cx, AndroidUtilities.dp(5.0f) + cy, AndroidUtilities.dp(52.0f), AndroidUtilities.dp(52.0f));
                    this.locationImageReceiver.draw(canvas);
                } else {
                    canvas.save();
                    canvas.translate((float) (((this.docTitleOffsetX + this.photoImage.getImageX()) + this.photoImage.getImageWidth()) + AndroidUtilities.dp(10.0f)), (float) (this.photoImage.getImageY() + AndroidUtilities.dp(8.0f)));
                    this.docTitleLayout.draw(canvas);
                    canvas.restore();
                    if (this.infoLayout != null) {
                        canvas.save();
                        canvas.translate((float) ((this.photoImage.getImageX() + this.photoImage.getImageWidth()) + AndroidUtilities.dp(10.0f)), (float) ((this.photoImage.getImageY() + this.docTitleLayout.getLineBottom(this.docTitleLayout.getLineCount() - 1)) + AndroidUtilities.dp(13.0f)));
                        this.infoLayout.draw(canvas);
                        canvas.restore();
                    }
                }
            }
        } else if (this.currentMessageObject.type == 16) {
            Drawable icon;
            Drawable phone;
            if (this.currentMessageObject.isOutOwner()) {
                Theme.chat_audioTitlePaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
                Theme.chat_contactPhonePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outTimeSelectedText : Theme.key_chat_outTimeText));
            } else {
                Theme.chat_audioTitlePaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
                Theme.chat_contactPhonePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inTimeSelectedText : Theme.key_chat_inTimeText));
            }
            this.forceNotDrawTime = true;
            if (this.currentMessageObject.isOutOwner()) {
                x = (this.layoutWidth - this.backgroundWidth) + AndroidUtilities.dp(16.0f);
            } else if (this.isChat && this.currentMessageObject.needDrawAvatar()) {
                x = AndroidUtilities.dp(74.0f);
            } else {
                x = AndroidUtilities.dp(25.0f);
            }
            this.otherX = x;
            if (this.titleLayout != null) {
                canvas.save();
                canvas.translate((float) x, (float) (AndroidUtilities.dp(12.0f) + this.namesOffset));
                this.titleLayout.draw(canvas);
                canvas.restore();
            }
            if (this.docTitleLayout != null) {
                canvas.save();
                canvas.translate((float) (AndroidUtilities.dp(19.0f) + x), (float) (AndroidUtilities.dp(37.0f) + this.namesOffset));
                this.docTitleLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentMessageObject.isOutOwner()) {
                icon = Theme.chat_msgCallUpGreenDrawable;
                phone = (isDrawSelectedBackground() || this.otherPressed) ? Theme.chat_msgOutCallSelectedDrawable : Theme.chat_msgOutCallDrawable;
            } else {
                PhoneCallDiscardReason reason = this.currentMessageObject.messageOwner.action.reason;
                if ((reason instanceof TL_phoneCallDiscardReasonMissed) || (reason instanceof TL_phoneCallDiscardReasonBusy)) {
                    icon = Theme.chat_msgCallDownRedDrawable;
                } else {
                    icon = Theme.chat_msgCallDownGreenDrawable;
                }
                phone = (isDrawSelectedBackground() || this.otherPressed) ? Theme.chat_msgInCallSelectedDrawable : Theme.chat_msgInCallDrawable;
            }
            BaseCell.setDrawableBounds(icon, x - AndroidUtilities.dp(3.0f), AndroidUtilities.dp(36.0f) + this.namesOffset);
            icon.draw(canvas);
            i = AndroidUtilities.dp(205.0f) + x;
            i2 = AndroidUtilities.dp(22.0f);
            this.otherY = i2;
            BaseCell.setDrawableBounds(phone, i, i2);
            phone.draw(canvas);
        } else if (this.currentMessageObject.type == 12) {
            Theme.chat_contactNamePaint.setColor(Theme.getColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outContactNameText : Theme.key_chat_inContactNameText));
            Theme.chat_contactPhonePaint.setColor(Theme.getColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outContactPhoneText : Theme.key_chat_inContactPhoneText));
            if (this.titleLayout != null) {
                canvas.save();
                canvas.translate((float) ((this.photoImage.getImageX() + this.photoImage.getImageWidth()) + AndroidUtilities.dp(9.0f)), (float) (AndroidUtilities.dp(16.0f) + this.namesOffset));
                this.titleLayout.draw(canvas);
                canvas.restore();
            }
            if (this.docTitleLayout != null) {
                canvas.save();
                canvas.translate((float) ((this.photoImage.getImageX() + this.photoImage.getImageWidth()) + AndroidUtilities.dp(9.0f)), (float) (AndroidUtilities.dp(39.0f) + this.namesOffset));
                this.docTitleLayout.draw(canvas);
                canvas.restore();
            }
            menuDrawable = this.currentMessageObject.isOutOwner() ? isDrawSelectedBackground() ? Theme.chat_msgOutMenuSelectedDrawable : Theme.chat_msgOutMenuDrawable : isDrawSelectedBackground() ? Theme.chat_msgInMenuSelectedDrawable : Theme.chat_msgInMenuDrawable;
            i = (this.photoImage.getImageX() + this.backgroundWidth) - AndroidUtilities.dp(48.0f);
            this.otherX = i;
            i2 = this.photoImage.getImageY() - AndroidUtilities.dp(5.0f);
            this.otherY = i2;
            BaseCell.setDrawableBounds(menuDrawable, i, i2);
            menuDrawable.draw(canvas);
        }
        if (this.currentPosition == null) {
            drawCaptionLayout(canvas);
        }
        if (this.hasOldCaptionPreview) {
            if (this.currentMessageObject.type == 1 || this.documentAttachType == 4 || this.currentMessageObject.type == 8) {
                linkX = this.photoImage.getImageX() + AndroidUtilities.dp(5.0f);
            } else {
                linkX = this.backgroundDrawableLeft + AndroidUtilities.dp(this.currentMessageObject.isOutOwner() ? 11.0f : 17.0f);
            }
            startY = ((this.totalHeight - AndroidUtilities.dp(this.drawPinnedTop ? 9.0f : 10.0f)) - this.linkPreviewHeight) - AndroidUtilities.dp(8.0f);
            linkPreviewY = startY;
            Theme.chat_replyLinePaint.setColor(Theme.getColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outPreviewLine : Theme.key_chat_inPreviewLine));
            canvas.drawRect((float) linkX, (float) (linkPreviewY - AndroidUtilities.dp(3.0f)), (float) (AndroidUtilities.dp(2.0f) + linkX), (float) (this.linkPreviewHeight + linkPreviewY), Theme.chat_replyLinePaint);
            if (this.siteNameLayout != null) {
                Theme.chat_replyNamePaint.setColor(Theme.getColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outSiteNameText : Theme.key_chat_inSiteNameText));
                canvas.save();
                canvas.translate((float) ((this.hasInvoicePreview ? 0 : AndroidUtilities.dp(10.0f)) + linkX), (float) (linkPreviewY - AndroidUtilities.dp(3.0f)));
                this.siteNameLayout.draw(canvas);
                canvas.restore();
                linkPreviewY += this.siteNameLayout.getLineBottom(this.siteNameLayout.getLineCount() - 1);
            }
            if (this.currentMessageObject.isOutOwner()) {
                Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
            } else {
                Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
            }
            if (this.descriptionLayout != null) {
                if (linkPreviewY != startY) {
                    linkPreviewY += AndroidUtilities.dp(2.0f);
                }
                this.descriptionY = linkPreviewY - AndroidUtilities.dp(3.0f);
                canvas.save();
                canvas.translate((float) ((AndroidUtilities.dp(10.0f) + linkX) + this.descriptionX), (float) this.descriptionY);
                this.descriptionLayout.draw(canvas);
                canvas.restore();
            }
            this.drawTime = true;
        }
        if (this.documentAttachType == 1) {
            int titleY;
            int subtitleY;
            if (this.currentMessageObject.isOutOwner()) {
                Theme.chat_docNamePaint.setColor(Theme.getColor(Theme.key_chat_outFileNameText));
                Theme.chat_infoPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outFileInfoSelectedText : Theme.key_chat_outFileInfoText));
                Theme.chat_docBackPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outFileBackgroundSelected : Theme.key_chat_outFileBackground));
                if (isDrawSelectedBackground()) {
                    menuDrawable = Theme.chat_msgOutMenuSelectedDrawable;
                } else {
                    menuDrawable = Theme.chat_msgOutMenuDrawable;
                }
            } else {
                Theme.chat_docNamePaint.setColor(Theme.getColor(Theme.key_chat_inFileNameText));
                Theme.chat_infoPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inFileInfoSelectedText : Theme.key_chat_inFileInfoText));
                Theme.chat_docBackPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inFileBackgroundSelected : Theme.key_chat_inFileBackground));
                menuDrawable = isDrawSelectedBackground() ? Theme.chat_msgInMenuSelectedDrawable : Theme.chat_msgInMenuDrawable;
            }
            if (this.drawPhotoImage) {
                if (this.currentMessageObject.type == 0) {
                    i = (this.photoImage.getImageX() + this.backgroundWidth) - AndroidUtilities.dp(56.0f);
                    this.otherX = i;
                    i2 = this.photoImage.getImageY() + AndroidUtilities.dp(1.0f);
                    this.otherY = i2;
                    BaseCell.setDrawableBounds(menuDrawable, i, i2);
                } else {
                    i = (this.photoImage.getImageX() + this.backgroundWidth) - AndroidUtilities.dp(40.0f);
                    this.otherX = i;
                    i2 = this.photoImage.getImageY() + AndroidUtilities.dp(1.0f);
                    this.otherY = i2;
                    BaseCell.setDrawableBounds(menuDrawable, i, i2);
                }
                x = (this.photoImage.getImageX() + this.photoImage.getImageWidth()) + AndroidUtilities.dp(10.0f);
                titleY = this.photoImage.getImageY() + AndroidUtilities.dp(8.0f);
                subtitleY = (this.photoImage.getImageY() + this.docTitleLayout.getLineBottom(this.docTitleLayout.getLineCount() - 1)) + AndroidUtilities.dp(13.0f);
                if (this.buttonState >= 0 && this.buttonState < 4) {
                    if (imageDrawn) {
                        this.radialProgress.swapBackground(Theme.chat_photoStatesDrawables[this.buttonState][this.buttonPressed]);
                    } else {
                        int image = this.buttonState;
                        if (this.buttonState == 0) {
                            image = this.currentMessageObject.isOutOwner() ? 7 : 10;
                        } else if (this.buttonState == 1) {
                            image = this.currentMessageObject.isOutOwner() ? 8 : 11;
                        }
                        radialProgress = this.radialProgress;
                        Drawable[] drawableArr = Theme.chat_photoStatesDrawables[image];
                        i = (isDrawSelectedBackground() || this.buttonPressed != 0) ? 1 : 0;
                        radialProgress.swapBackground(drawableArr[i]);
                    }
                }
                if (imageDrawn) {
                    if (this.buttonState == -1) {
                        this.radialProgress.setHideCurrentDrawable(true);
                    }
                    this.radialProgress.setProgressColor(Theme.getColor(Theme.key_chat_mediaProgress));
                } else {
                    this.rect.set((float) this.photoImage.getImageX(), (float) this.photoImage.getImageY(), (float) (this.photoImage.getImageX() + this.photoImage.getImageWidth()), (float) (this.photoImage.getImageY() + this.photoImage.getImageHeight()));
                    canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(3.0f), (float) AndroidUtilities.dp(3.0f), Theme.chat_docBackPaint);
                    if (this.currentMessageObject.isOutOwner()) {
                        radialProgress = this.radialProgress;
                        if (isDrawSelectedBackground()) {
                            str = Theme.key_chat_outFileProgressSelected;
                        } else {
                            str = Theme.key_chat_outFileProgress;
                        }
                        radialProgress.setProgressColor(Theme.getColor(str));
                    } else {
                        this.radialProgress.setProgressColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inFileProgressSelected : Theme.key_chat_inFileProgress));
                    }
                }
            } else {
                i = (this.backgroundWidth + this.buttonX) - AndroidUtilities.dp(this.currentMessageObject.type == 0 ? 58.0f : 48.0f);
                this.otherX = i;
                i2 = this.buttonY - AndroidUtilities.dp(5.0f);
                this.otherY = i2;
                BaseCell.setDrawableBounds(menuDrawable, i, i2);
                x = this.buttonX + AndroidUtilities.dp(53.0f);
                titleY = this.buttonY + AndroidUtilities.dp(4.0f);
                subtitleY = this.buttonY + AndroidUtilities.dp(27.0f);
                if (this.currentMessageObject.isOutOwner()) {
                    radialProgress = this.radialProgress;
                    if (isDrawSelectedBackground() || this.buttonPressed != 0) {
                        str = Theme.key_chat_outAudioSelectedProgress;
                    } else {
                        str = Theme.key_chat_outAudioProgress;
                    }
                    radialProgress.setProgressColor(Theme.getColor(str));
                } else {
                    radialProgress = this.radialProgress;
                    str = (isDrawSelectedBackground() || this.buttonPressed != 0) ? Theme.key_chat_inAudioSelectedProgress : Theme.key_chat_inAudioProgress;
                    radialProgress.setProgressColor(Theme.getColor(str));
                }
            }
            menuDrawable.draw(canvas);
            try {
                if (this.docTitleLayout != null) {
                    canvas.save();
                    canvas.translate((float) (this.docTitleOffsetX + x), (float) titleY);
                    this.docTitleLayout.draw(canvas);
                    canvas.restore();
                }
            } catch (Throwable e2) {
                FileLog.e(e2);
            }
            try {
                if (this.infoLayout != null) {
                    canvas.save();
                    canvas.translate((float) x, (float) subtitleY);
                    this.infoLayout.draw(canvas);
                    canvas.restore();
                }
            } catch (Throwable e22) {
                FileLog.e(e22);
            }
        }
        if (this.drawImageButton && this.photoImage.getVisible()) {
            if (this.controlsAlpha != 1.0f) {
                this.radialProgress.setOverrideAlpha(this.controlsAlpha);
            }
            this.radialProgress.draw(canvas);
        }
        if (!this.botButtons.isEmpty()) {
            int addX;
            if (this.currentMessageObject.isOutOwner()) {
                addX = (getMeasuredWidth() - this.widthForButtons) - AndroidUtilities.dp(10.0f);
            } else {
                addX = this.backgroundDrawableLeft + AndroidUtilities.dp(this.mediaBackground ? 1.0f : 7.0f);
            }
            a = 0;
            while (a < this.botButtons.size()) {
                BotButton button = (BotButton) this.botButtons.get(a);
                y = (button.y + this.layoutHeight) - AndroidUtilities.dp(2.0f);
                Theme.chat_systemDrawable.setColorFilter(a == this.pressedBotButton ? Theme.colorPressedFilter : Theme.colorFilter);
                Theme.chat_systemDrawable.setBounds(button.x + addX, y, (button.x + addX) + button.width, button.height + y);
                Theme.chat_systemDrawable.draw(canvas);
                canvas.save();
                canvas.translate((float) ((button.x + addX) + AndroidUtilities.dp(5.0f)), (float) (((AndroidUtilities.dp(44.0f) - button.title.getLineBottom(button.title.getLineCount() - 1)) / 2) + y));
                button.title.draw(canvas);
                canvas.restore();
                if (button.button instanceof TL_keyboardButtonUrl) {
                    BaseCell.setDrawableBounds(Theme.chat_botLinkDrawalbe, (((button.x + button.width) - AndroidUtilities.dp(3.0f)) - Theme.chat_botLinkDrawalbe.getIntrinsicWidth()) + addX, AndroidUtilities.dp(3.0f) + y);
                    Theme.chat_botLinkDrawalbe.draw(canvas);
                } else if (button.button instanceof TL_keyboardButtonSwitchInline) {
                    BaseCell.setDrawableBounds(Theme.chat_botInlineDrawable, (((button.x + button.width) - AndroidUtilities.dp(3.0f)) - Theme.chat_botInlineDrawable.getIntrinsicWidth()) + addX, AndroidUtilities.dp(3.0f) + y);
                    Theme.chat_botInlineDrawable.draw(canvas);
                } else if ((button.button instanceof TL_keyboardButtonCallback) || (button.button instanceof TL_keyboardButtonRequestGeoLocation) || (button.button instanceof TL_keyboardButtonGame) || (button.button instanceof TL_keyboardButtonBuy)) {
                    boolean drawProgress = (((button.button instanceof TL_keyboardButtonCallback) || (button.button instanceof TL_keyboardButtonGame) || (button.button instanceof TL_keyboardButtonBuy)) && SendMessagesHelper.getInstance(this.currentAccount).isSendingCallback(this.currentMessageObject, button.button)) || ((button.button instanceof TL_keyboardButtonRequestGeoLocation) && SendMessagesHelper.getInstance(this.currentAccount).isSendingCurrentLocation(this.currentMessageObject, button.button));
                    if (drawProgress || !(drawProgress || button.progressAlpha == 0.0f)) {
                        Theme.chat_botProgressPaint.setAlpha(Math.min(255, (int) (button.progressAlpha * 255.0f)));
                        x = ((button.x + button.width) - AndroidUtilities.dp(12.0f)) + addX;
                        this.rect.set((float) x, (float) (AndroidUtilities.dp(4.0f) + y), (float) (AndroidUtilities.dp(8.0f) + x), (float) (AndroidUtilities.dp(12.0f) + y));
                        canvas.drawArc(this.rect, (float) button.angle, 220.0f, false, Theme.chat_botProgressPaint);
                        invalidate(((int) this.rect.left) - AndroidUtilities.dp(2.0f), ((int) this.rect.top) - AndroidUtilities.dp(2.0f), ((int) this.rect.right) + AndroidUtilities.dp(2.0f), ((int) this.rect.bottom) + AndroidUtilities.dp(2.0f));
                        long newTime = System.currentTimeMillis();
                        if (Math.abs(button.lastUpdateTime - System.currentTimeMillis()) < 1000) {
                            long delta = newTime - button.lastUpdateTime;
                            button.angle = (int) (((float) button.angle) + (((float) (360 * delta)) / 2000.0f));
                            button.angle = button.angle - ((button.angle / 360) * 360);
                            if (drawProgress) {
                                if (button.progressAlpha < 1.0f) {
                                    button.progressAlpha = button.progressAlpha + (((float) delta) / 200.0f);
                                    if (button.progressAlpha > 1.0f) {
                                        button.progressAlpha = 1.0f;
                                    }
                                }
                            } else if (button.progressAlpha > 0.0f) {
                                button.progressAlpha = button.progressAlpha - (((float) delta) / 200.0f);
                                if (button.progressAlpha < 0.0f) {
                                    button.progressAlpha = 0.0f;
                                }
                            }
                        }
                        button.lastUpdateTime = newTime;
                    }
                }
                a++;
            }
        }
    }

    private Drawable getDrawableForCurrentState() {
        int i = 3;
        int i2 = 0;
        int i3 = 1;
        if (this.documentAttachType != 3 && this.documentAttachType != 5) {
            Drawable[] drawableArr;
            if (this.documentAttachType != 1 || this.drawPhotoImage) {
                this.radialProgress.setAlphaForPrevious(true);
                if (this.buttonState < 0 || this.buttonState >= 4) {
                    if (this.buttonState == -1 && this.documentAttachType == 1) {
                        drawableArr = Theme.chat_photoStatesDrawables[this.currentMessageObject.isOutOwner() ? 9 : 12];
                        if (!isDrawSelectedBackground()) {
                            i3 = 0;
                        }
                        return drawableArr[i3];
                    }
                } else if (this.documentAttachType != 1) {
                    return Theme.chat_photoStatesDrawables[this.buttonState][this.buttonPressed];
                } else {
                    int image = this.buttonState;
                    if (this.buttonState == 0) {
                        image = this.currentMessageObject.isOutOwner() ? 7 : 10;
                    } else if (this.buttonState == 1) {
                        image = this.currentMessageObject.isOutOwner() ? 8 : 11;
                    }
                    drawableArr = Theme.chat_photoStatesDrawables[image];
                    if (isDrawSelectedBackground() || this.buttonPressed != 0) {
                        i2 = 1;
                    }
                    return drawableArr[i2];
                }
            }
            this.radialProgress.setAlphaForPrevious(false);
            if (this.buttonState == -1) {
                Drawable[][] drawableArr2 = Theme.chat_fileStatesDrawable;
                if (!this.currentMessageObject.isOutOwner()) {
                    i = 8;
                }
                drawableArr = drawableArr2[i];
                if (!isDrawSelectedBackground()) {
                    i3 = 0;
                }
                return drawableArr[i3];
            } else if (this.buttonState == 0) {
                drawableArr = Theme.chat_fileStatesDrawable[this.currentMessageObject.isOutOwner() ? 2 : 7];
                if (!isDrawSelectedBackground()) {
                    i3 = 0;
                }
                return drawableArr[i3];
            } else if (this.buttonState == 1) {
                drawableArr = Theme.chat_fileStatesDrawable[this.currentMessageObject.isOutOwner() ? 4 : 9];
                if (!isDrawSelectedBackground()) {
                    i3 = 0;
                }
                return drawableArr[i3];
            }
            return null;
        } else if (this.buttonState == -1) {
            return null;
        } else {
            this.radialProgress.setAlphaForPrevious(false);
            Drawable[] drawableArr3 = Theme.chat_fileStatesDrawable[this.currentMessageObject.isOutOwner() ? this.buttonState : this.buttonState + 5];
            i = (isDrawSelectedBackground() || this.buttonPressed != 0) ? 1 : 0;
            return drawableArr3[i];
        }
    }

    private int getMaxNameWidth() {
        if (this.documentAttachType == 6 || this.currentMessageObject.type == 5) {
            int maxWidth;
            if (AndroidUtilities.isTablet()) {
                if (this.isChat && !this.currentMessageObject.isOutOwner() && this.currentMessageObject.needDrawAvatar()) {
                    maxWidth = AndroidUtilities.getMinTabletSide() - AndroidUtilities.dp(42.0f);
                } else {
                    maxWidth = AndroidUtilities.getMinTabletSide();
                }
            } else if (this.isChat && !this.currentMessageObject.isOutOwner() && this.currentMessageObject.needDrawAvatar()) {
                maxWidth = Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) - AndroidUtilities.dp(42.0f);
            } else {
                maxWidth = Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y);
            }
            return (maxWidth - this.backgroundWidth) - AndroidUtilities.dp(57.0f);
        } else if (this.currentMessagesGroup != null) {
            int dWidth;
            if (AndroidUtilities.isTablet()) {
                dWidth = AndroidUtilities.getMinTabletSide();
            } else {
                dWidth = AndroidUtilities.displaySize.x;
            }
            int firstLineWidth = 0;
            for (int a = 0; a < this.currentMessagesGroup.posArray.size(); a++) {
                GroupedMessagePosition position = (GroupedMessagePosition) this.currentMessagesGroup.posArray.get(a);
                if (position.minY != (byte) 0) {
                    break;
                }
                firstLineWidth = (int) (((double) firstLineWidth) + Math.ceil((double) ((((float) (position.pw + position.leftSpanOffset)) / 1000.0f) * ((float) dWidth))));
            }
            return firstLineWidth - AndroidUtilities.dp((float) ((this.isAvatarVisible ? 48 : 0) + 31));
        } else {
            return this.backgroundWidth - AndroidUtilities.dp(this.mediaBackground ? 22.0f : 31.0f);
        }
    }

    public void updateButtonState(boolean animated) {
        this.drawRadialCheckBackground = false;
        String fileName = null;
        boolean fileExists = false;
        if (this.currentMessageObject.type == 1) {
            if (this.currentPhotoObject != null) {
                fileName = FileLoader.getAttachFileName(this.currentPhotoObject);
                fileExists = this.currentMessageObject.mediaExists;
            } else {
                return;
            }
        } else if (this.currentMessageObject.type == 8 || this.currentMessageObject.type == 5 || this.documentAttachType == 7 || this.documentAttachType == 4 || this.currentMessageObject.type == 9 || this.documentAttachType == 3 || this.documentAttachType == 5) {
            if (this.currentMessageObject.useCustomPhoto) {
                this.buttonState = 1;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
                return;
            } else if (this.currentMessageObject.attachPathExists) {
                fileName = this.currentMessageObject.messageOwner.attachPath;
                fileExists = true;
            } else if (!this.currentMessageObject.isSendError() || this.documentAttachType == 3 || this.documentAttachType == 5) {
                fileName = this.currentMessageObject.getFileName();
                fileExists = this.currentMessageObject.mediaExists;
            }
        } else if (this.documentAttachType != 0) {
            fileName = FileLoader.getAttachFileName(this.documentAttach);
            fileExists = this.currentMessageObject.mediaExists;
        } else if (this.currentPhotoObject != null) {
            fileName = FileLoader.getAttachFileName(this.currentPhotoObject);
            fileExists = this.currentMessageObject.mediaExists;
        }
        if (TextUtils.isEmpty(fileName)) {
            this.radialProgress.setBackground(null, false, false);
            return;
        }
        boolean fromBot = this.currentMessageObject.messageOwner.params != null && this.currentMessageObject.messageOwner.params.containsKey("query_id");
        Float progress;
        if (this.documentAttachType == 3 || this.documentAttachType == 5) {
            if ((this.currentMessageObject.isOut() && this.currentMessageObject.isSending()) || (this.currentMessageObject.isSendError() && fromBot)) {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(this.currentMessageObject.messageOwner.attachPath, this.currentMessageObject, this);
                this.buttonState = 4;
                this.radialProgress.setBackground(getDrawableForCurrentState(), !fromBot, animated);
                if (fromBot) {
                    this.radialProgress.setProgress(0.0f, false);
                } else {
                    float floatValue;
                    progress = ImageLoader.getInstance().getFileProgress(this.currentMessageObject.messageOwner.attachPath);
                    if (progress == null && SendMessagesHelper.getInstance(this.currentAccount).isSendingMessage(this.currentMessageObject.getId())) {
                        progress = Float.valueOf(1.0f);
                    }
                    RadialProgress radialProgress = this.radialProgress;
                    if (progress != null) {
                        floatValue = progress.floatValue();
                    } else {
                        floatValue = 0.0f;
                    }
                    radialProgress.setProgress(floatValue, false);
                }
            } else if (fileExists) {
                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                boolean playing = MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
                if (!playing || (playing && MediaController.getInstance().isMessagePaused())) {
                    this.buttonState = 0;
                } else {
                    this.buttonState = 1;
                }
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
            } else {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this.currentMessageObject, this);
                if (FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                    this.buttonState = 4;
                    progress = ImageLoader.getInstance().getFileProgress(fileName);
                    if (progress != null) {
                        this.radialProgress.setProgress(progress.floatValue(), animated);
                    } else {
                        this.radialProgress.setProgress(0.0f, animated);
                    }
                    this.radialProgress.setBackground(getDrawableForCurrentState(), true, animated);
                } else {
                    this.buttonState = 2;
                    this.radialProgress.setProgress(0.0f, animated);
                    this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
                }
            }
            updatePlayingMessageProgress();
        } else if (this.currentMessageObject.type != 0 || this.documentAttachType == 1 || this.documentAttachType == 4) {
            if (!this.currentMessageObject.isOut() || !this.currentMessageObject.isSending()) {
                if (!(this.currentMessageObject.messageOwner.attachPath == null || this.currentMessageObject.messageOwner.attachPath.length() == 0)) {
                    DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                }
                if (fileExists) {
                    DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                    if (this.currentMessageObject.isSecretPhoto()) {
                        this.buttonState = -1;
                    } else if (this.currentMessageObject.type == 8 && !this.photoImage.isAllowStartAnimation()) {
                        this.buttonState = 2;
                    } else if (this.documentAttachType == 4) {
                        this.buttonState = 3;
                    } else {
                        this.buttonState = -1;
                    }
                    this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
                    if (this.photoNotSet) {
                        setMessageObject(this.currentMessageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
                    }
                    invalidate();
                    return;
                }
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this.currentMessageObject, this);
                setProgress = 0.0f;
                progressVisible = false;
                if (FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                    progressVisible = true;
                    this.buttonState = 1;
                    progress = ImageLoader.getInstance().getFileProgress(fileName);
                    setProgress = progress != null ? progress.floatValue() : 0.0f;
                } else {
                    boolean autoDownload = false;
                    if (this.currentMessageObject.type == 1) {
                        autoDownload = DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject);
                    } else if (this.currentMessageObject.type == 8 && MessageObject.isNewGifDocument(this.currentMessageObject.messageOwner.media.document)) {
                        autoDownload = DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject);
                    } else if (this.currentMessageObject.type == 5) {
                        autoDownload = DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject);
                    }
                    if (this.cancelLoading || !autoDownload) {
                        this.buttonState = 0;
                    } else {
                        progressVisible = true;
                        this.buttonState = 1;
                    }
                }
                this.radialProgress.setBackground(getDrawableForCurrentState(), progressVisible, animated);
                this.radialProgress.setProgress(setProgress, false);
                invalidate();
            } else if (this.currentMessageObject.messageOwner.attachPath != null && this.currentMessageObject.messageOwner.attachPath.length() > 0) {
                DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(this.currentMessageObject.messageOwner.attachPath, this.currentMessageObject, this);
                boolean needProgress = this.currentMessageObject.messageOwner.attachPath == null || !this.currentMessageObject.messageOwner.attachPath.startsWith("http");
                HashMap<String, String> params = this.currentMessageObject.messageOwner.params;
                if (this.currentMessageObject.messageOwner.message == null || params == null || !(params.containsKey(UpdateFragment.FRAGMENT_URL) || params.containsKey("bot"))) {
                    this.buttonState = 1;
                } else {
                    needProgress = false;
                    this.buttonState = -1;
                }
                boolean sending = SendMessagesHelper.getInstance(this.currentAccount).isSendingMessage(this.currentMessageObject.getId());
                if (this.currentPosition != null && sending && this.buttonState == 1) {
                    this.drawRadialCheckBackground = true;
                    this.radialProgress.setCheckBackground(false, animated);
                } else {
                    this.radialProgress.setBackground(getDrawableForCurrentState(), needProgress, animated);
                }
                if (needProgress) {
                    progress = ImageLoader.getInstance().getFileProgress(this.currentMessageObject.messageOwner.attachPath);
                    if (progress == null && sending) {
                        progress = Float.valueOf(1.0f);
                    }
                    this.radialProgress.setProgress(progress != null ? progress.floatValue() : 0.0f, false);
                } else {
                    this.radialProgress.setProgress(0.0f, false);
                }
                invalidate();
            }
        } else if (this.currentPhotoObject != null && this.drawImageButton) {
            if (fileExists) {
                DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
                if (this.documentAttachType != 2 || this.photoImage.isAllowStartAnimation()) {
                    this.buttonState = -1;
                } else {
                    this.buttonState = 2;
                }
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
                invalidate();
                return;
            }
            DownloadController.getInstance(this.currentAccount).addLoadingFileObserver(fileName, this.currentMessageObject, this);
            setProgress = 0.0f;
            progressVisible = false;
            if (FileLoader.getInstance(this.currentAccount).isLoadingFile(fileName)) {
                progressVisible = true;
                this.buttonState = 1;
                progress = ImageLoader.getInstance().getFileProgress(fileName);
                setProgress = progress != null ? progress.floatValue() : 0.0f;
            } else if (this.cancelLoading || !((this.documentAttachType == 0 && DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject)) || (this.documentAttachType == 2 && DownloadController.getInstance(this.currentAccount).canDownloadMedia(this.currentMessageObject)))) {
                this.buttonState = 0;
            } else {
                progressVisible = true;
                this.buttonState = 1;
            }
            this.radialProgress.setProgress(setProgress, false);
            this.radialProgress.setBackground(getDrawableForCurrentState(), progressVisible, animated);
            invalidate();
        }
    }

    private void didPressedButton(boolean animated) {
        if (this.buttonState == 0) {
            if (this.documentAttachType != 3 && this.documentAttachType != 5) {
                this.cancelLoading = false;
                this.radialProgress.setProgress(0.0f, false);
                if (this.currentMessageObject.type == 1) {
                    FileLocation fileLocation;
                    int i;
                    this.photoImage.setForceLoading(true);
                    ImageReceiver imageReceiver = this.photoImage;
                    TLObject tLObject = this.currentPhotoObject.location;
                    String str = this.currentPhotoFilter;
                    if (this.currentPhotoObjectThumb != null) {
                        fileLocation = this.currentPhotoObjectThumb.location;
                    } else {
                        fileLocation = null;
                    }
                    String str2 = this.currentPhotoFilterThumb;
                    int i2 = this.currentPhotoObject.size;
                    if (this.currentMessageObject.shouldEncryptPhotoOrVideo()) {
                        i = 2;
                    } else {
                        i = 0;
                    }
                    imageReceiver.setImage(tLObject, str, fileLocation, str2, i2, null, i);
                } else if (this.currentMessageObject.type == 8) {
                    this.currentMessageObject.gifState = 2.0f;
                    this.photoImage.setForceLoading(true);
                    this.photoImage.setImage(this.currentMessageObject.messageOwner.media.document, null, this.currentPhotoObject != null ? this.currentPhotoObject.location : null, this.currentPhotoFilterThumb, this.currentMessageObject.messageOwner.media.document.size, null, 0);
                } else if (this.currentMessageObject.isRoundVideo()) {
                    if (this.currentMessageObject.isSecretMedia()) {
                        FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.getDocument(), true, 1);
                    } else {
                        this.currentMessageObject.gifState = 2.0f;
                        Document document = this.currentMessageObject.getDocument();
                        this.photoImage.setForceLoading(true);
                        this.photoImage.setImage(document, null, this.currentPhotoObject != null ? this.currentPhotoObject.location : null, this.currentPhotoFilterThumb, document.size, null, 0);
                    }
                } else if (this.currentMessageObject.type == 9) {
                    FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.messageOwner.media.document, false, 0);
                } else if (this.documentAttachType == 4) {
                    FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, true, this.currentMessageObject.shouldEncryptPhotoOrVideo() ? 2 : 0);
                } else if (this.currentMessageObject.type != 0 || this.documentAttachType == 0) {
                    this.photoImage.setForceLoading(true);
                    this.photoImage.setImage(this.currentPhotoObject.location, this.currentPhotoFilter, this.currentPhotoObjectThumb != null ? this.currentPhotoObjectThumb.location : null, this.currentPhotoFilterThumb, 0, null, 0);
                } else if (this.documentAttachType == 2) {
                    this.photoImage.setForceLoading(true);
                    this.photoImage.setImage(this.currentMessageObject.messageOwner.media.webpage.document, null, this.currentPhotoObject.location, this.currentPhotoFilterThumb, this.currentMessageObject.messageOwner.media.webpage.document.size, null, 0);
                    this.currentMessageObject.gifState = 2.0f;
                } else if (this.documentAttachType == 1) {
                    FileLoader.getInstance(this.currentAccount).loadFile(this.currentMessageObject.messageOwner.media.webpage.document, false, 0);
                }
                this.buttonState = 1;
                this.radialProgress.setBackground(getDrawableForCurrentState(), true, animated);
                invalidate();
            } else if (this.delegate.needPlayMessage(this.currentMessageObject)) {
                this.buttonState = 1;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                invalidate();
            }
        } else if (this.buttonState == 1) {
            if (this.documentAttachType == 3 || this.documentAttachType == 5) {
                if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
                    this.buttonState = 0;
                    this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                    invalidate();
                }
            } else if (this.currentMessageObject.isOut() && this.currentMessageObject.isSending()) {
                this.delegate.didPressedCancelSendButton(this);
            } else {
                this.cancelLoading = true;
                if (this.documentAttachType == 4 || this.documentAttachType == 1) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                } else if (this.currentMessageObject.type == 0 || this.currentMessageObject.type == 1 || this.currentMessageObject.type == 8 || this.currentMessageObject.type == 5) {
                    ImageLoader.getInstance().cancelForceLoadingForImageReceiver(this.photoImage);
                    this.photoImage.cancelLoadImage();
                } else if (this.currentMessageObject.type == 9) {
                    FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.currentMessageObject.messageOwner.media.document);
                }
                this.buttonState = 0;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
                invalidate();
            }
        } else if (this.buttonState == 2) {
            if (this.documentAttachType == 3 || this.documentAttachType == 5) {
                this.radialProgress.setProgress(0.0f, false);
                FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, true, 0);
                this.buttonState = 4;
                this.radialProgress.setBackground(getDrawableForCurrentState(), true, false);
                invalidate();
                return;
            }
            this.photoImage.setAllowStartAnimation(true);
            this.photoImage.startAnimation();
            this.currentMessageObject.gifState = 0.0f;
            this.buttonState = -1;
            this.radialProgress.setBackground(getDrawableForCurrentState(), false, animated);
        } else if (this.buttonState == 3) {
            this.delegate.didPressedImage(this);
        } else if (this.buttonState != 4) {
        } else {
            if (this.documentAttachType != 3 && this.documentAttachType != 5) {
                return;
            }
            if ((!this.currentMessageObject.isOut() || !this.currentMessageObject.isSending()) && !this.currentMessageObject.isSendError()) {
                FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
                this.buttonState = 2;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                invalidate();
            } else if (this.delegate != null) {
                this.delegate.didPressedCancelSendButton(this);
            }
        }
    }

    public void onFailedDownload(String fileName) {
        boolean z = this.documentAttachType == 3 || this.documentAttachType == 5;
        updateButtonState(z);
    }

    public void onSuccessDownload(String fileName) {
        if (this.documentAttachType == 3 || this.documentAttachType == 5) {
            updateButtonState(true);
            updateWaveform();
            return;
        }
        this.radialProgress.setProgress(1.0f, true);
        if (this.currentMessageObject.type != 0) {
            if (!this.photoNotSet || ((this.currentMessageObject.type == 8 || this.currentMessageObject.type == 5) && this.currentMessageObject.gifState != 1.0f)) {
                if ((this.currentMessageObject.type == 8 || this.currentMessageObject.type == 5) && this.currentMessageObject.gifState != 1.0f) {
                    this.photoNotSet = false;
                    this.buttonState = 2;
                    didPressedButton(true);
                } else {
                    updateButtonState(true);
                }
            }
            if (this.photoNotSet) {
                setMessageObject(this.currentMessageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
            }
        } else if (this.documentAttachType == 2 && this.currentMessageObject.gifState != 1.0f) {
            this.buttonState = 2;
            didPressedButton(true);
        } else if (this.photoNotSet) {
            setMessageObject(this.currentMessageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
        } else {
            updateButtonState(true);
        }
    }

    public void didSetImage(ImageReceiver imageReceiver, boolean set, boolean thumb) {
        if (this.currentMessageObject != null && set && !thumb && !this.currentMessageObject.mediaExists && !this.currentMessageObject.attachPathExists) {
            this.currentMessageObject.mediaExists = true;
            updateButtonState(true);
        }
    }

    public void onProgressDownload(String fileName, float progress) {
        this.radialProgress.setProgress(progress, true);
        if (this.documentAttachType == 3 || this.documentAttachType == 5) {
            if (this.buttonState != 4) {
                updateButtonState(false);
            }
        } else if (this.buttonState != 1) {
            updateButtonState(false);
        }
    }

    public void onProgressUpload(String fileName, float progress, boolean isEncrypted) {
        this.radialProgress.setProgress(progress, true);
        if (progress == 1.0f && this.currentPosition != null && SendMessagesHelper.getInstance(this.currentAccount).isSendingMessage(this.currentMessageObject.getId()) && this.buttonState == 1) {
            this.drawRadialCheckBackground = true;
            this.radialProgress.setCheckBackground(false, true);
        }
    }

    public void onProvideStructure(ViewStructure structure) {
        super.onProvideStructure(structure);
        if (this.allowAssistant && VERSION.SDK_INT >= 23) {
            if (this.currentMessageObject.messageText != null && this.currentMessageObject.messageText.length() > 0) {
                structure.setText(this.currentMessageObject.messageText);
            } else if (this.currentMessageObject.caption != null && this.currentMessageObject.caption.length() > 0) {
                structure.setText(this.currentMessageObject.caption);
            }
        }
    }

    public void setDelegate(ChatMessageCellDelegate chatMessageCellDelegate) {
        this.delegate = chatMessageCellDelegate;
    }

    public void setAllowAssistant(boolean value) {
        this.allowAssistant = value;
    }

    private void measureTime(MessageObject messageObject) {
        CharSequence signString;
        String timeString;
        if (messageObject.messageOwner.post_author != null) {
            signString = messageObject.messageOwner.post_author.replace("\n", TtmlNode.ANONYMOUS_REGION_ID);
        } else if (messageObject.messageOwner.fwd_from != null && messageObject.messageOwner.fwd_from.post_author != null) {
            signString = messageObject.messageOwner.fwd_from.post_author.replace("\n", TtmlNode.ANONYMOUS_REGION_ID);
        } else if (messageObject.isOutOwner() || messageObject.messageOwner.from_id <= 0 || !messageObject.messageOwner.post) {
            signString = null;
        } else {
            User signUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.from_id));
            if (signUser != null) {
                signString = ContactsController.formatName(signUser.first_name, signUser.last_name).replace('\n', ' ');
            } else {
                signString = null;
            }
        }
        User author = null;
        if (this.currentMessageObject.isFromUser()) {
            author = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.from_id));
        }
        if (messageObject.isLiveLocation() || messageObject.messageOwner.via_bot_id != 0 || messageObject.messageOwner.via_bot_name != null || ((author != null && author.bot) || (messageObject.messageOwner.flags & TLRPC.MESSAGE_FLAG_EDITED) == 0 || this.currentPosition != null)) {
            timeString = LocaleController.getInstance().formatterDay.format(((long) messageObject.messageOwner.date) * 1000);
        } else {
            timeString = LocaleController.getString("EditedMessage", R.string.EditedMessage) + " " + LocaleController.getInstance().formatterDay.format(((long) messageObject.messageOwner.date) * 1000);
        }
        if (signString != null) {
            this.currentTimeString = ", " + timeString;
        } else {
            this.currentTimeString = timeString;
        }
        int ceil = (int) Math.ceil((double) Theme.chat_timePaint.measureText(this.currentTimeString));
        this.timeWidth = ceil;
        this.timeTextWidth = ceil;
        if ((messageObject.messageOwner.flags & 1024) != 0) {
            this.currentViewsString = String.format("%s", new Object[]{LocaleController.formatShortNumber(Math.max(1, messageObject.messageOwner.views), null)});
            this.viewsTextWidth = (int) Math.ceil((double) Theme.chat_timePaint.measureText(this.currentViewsString));
            this.timeWidth += (this.viewsTextWidth + Theme.chat_msgInViewsDrawable.getIntrinsicWidth()) + AndroidUtilities.dp(10.0f);
        }
        if (signString != null) {
            if (this.availableTimeWidth == 0) {
                this.availableTimeWidth = AndroidUtilities.dp(1000.0f);
            }
            int widthForSign = this.availableTimeWidth - this.timeWidth;
            if (messageObject.isOutOwner()) {
                if (messageObject.type == 5) {
                    widthForSign -= AndroidUtilities.dp(20.0f);
                } else {
                    widthForSign -= AndroidUtilities.dp(96.0f);
                }
            }
            int width = (int) Math.ceil((double) Theme.chat_timePaint.measureText(signString, 0, signString.length()));
            if (width > widthForSign) {
                if (widthForSign <= 0) {
                    signString = TtmlNode.ANONYMOUS_REGION_ID;
                    width = 0;
                } else {
                    signString = TextUtils.ellipsize(signString, Theme.chat_timePaint, (float) widthForSign, TruncateAt.END);
                    width = widthForSign;
                }
            }
            this.currentTimeString = signString + this.currentTimeString;
            this.timeTextWidth += width;
            this.timeWidth += width;
        }
    }

    private boolean isDrawSelectedBackground() {
        return (isPressed() && this.isCheckPressed) || ((!this.isCheckPressed && this.isPressed) || this.isHighlighted);
    }

    private boolean isOpenChatByShare(MessageObject messageObject) {
        return (messageObject.messageOwner.fwd_from == null || messageObject.messageOwner.fwd_from.saved_from_peer == null) ? false : true;
    }

    private boolean checkNeedDrawShareButton(MessageObject messageObject) {
        if (this.currentPosition != null && !this.currentPosition.last) {
            return false;
        }
        if (messageObject.eventId != 0) {
            return false;
        }
        if (messageObject.messageOwner.fwd_from != null && !messageObject.isOutOwner() && messageObject.messageOwner.fwd_from.saved_from_peer != null && messageObject.getDialogId() == ((long) UserConfig.getInstance(this.currentAccount).getClientUserId())) {
            this.drwaShareGoIcon = true;
            return true;
        } else if (messageObject.type == 13) {
            return false;
        } else {
            if (messageObject.messageOwner.fwd_from != null && messageObject.messageOwner.fwd_from.channel_id != 0 && !messageObject.isOutOwner()) {
                return true;
            }
            if (messageObject.isFromUser()) {
                if ((messageObject.messageOwner.media instanceof TL_messageMediaEmpty) || messageObject.messageOwner.media == null || ((messageObject.messageOwner.media instanceof TL_messageMediaWebPage) && !(messageObject.messageOwner.media.webpage instanceof TL_webPage))) {
                    return false;
                }
                User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.from_id));
                if (user != null && user.bot) {
                    return true;
                }
                if (!messageObject.isOut()) {
                    if ((messageObject.messageOwner.media instanceof TL_messageMediaGame) || (messageObject.messageOwner.media instanceof TL_messageMediaInvoice)) {
                        return true;
                    }
                    if (messageObject.isMegagroup()) {
                        Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(messageObject.messageOwner.to_id.channel_id));
                        if (chat == null || chat.username == null || chat.username.length() <= 0 || (messageObject.messageOwner.media instanceof TL_messageMediaContact) || (messageObject.messageOwner.media instanceof TL_messageMediaGeo)) {
                            return false;
                        }
                        return true;
                    }
                }
            } else if ((messageObject.messageOwner.from_id < 0 || messageObject.messageOwner.post) && messageObject.messageOwner.to_id.channel_id != 0 && ((messageObject.messageOwner.via_bot_id == 0 && messageObject.messageOwner.reply_to_msg_id == 0) || messageObject.type != 13)) {
                return true;
            }
            return false;
        }
    }

    public boolean isInsideBackground(float x, float y) {
        return this.currentBackgroundDrawable != null && x >= ((float) (getLeft() + this.backgroundDrawableLeft)) && x <= ((float) ((getLeft() + this.backgroundDrawableLeft) + this.backgroundDrawableRight));
    }

    private void updateCurrentUserAndChat() {
        MessagesController messagesController = MessagesController.getInstance(this.currentAccount);
        MessageFwdHeader fwd_from = this.currentMessageObject.messageOwner.fwd_from;
        int currentUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
        if (fwd_from != null && fwd_from.channel_id != 0 && this.currentMessageObject.getDialogId() == ((long) currentUserId)) {
            this.currentChat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(fwd_from.channel_id));
        } else if (fwd_from == null || fwd_from.saved_from_peer == null) {
            if (fwd_from != null && fwd_from.from_id != 0 && fwd_from.channel_id == 0 && this.currentMessageObject.getDialogId() == ((long) currentUserId)) {
                this.currentUser = messagesController.getUser(Integer.valueOf(fwd_from.from_id));
            } else if (this.currentMessageObject.isFromUser()) {
                this.currentUser = messagesController.getUser(Integer.valueOf(this.currentMessageObject.messageOwner.from_id));
            } else if (this.currentMessageObject.messageOwner.from_id < 0) {
                this.currentChat = messagesController.getChat(Integer.valueOf(-this.currentMessageObject.messageOwner.from_id));
            } else if (this.currentMessageObject.messageOwner.post) {
                this.currentChat = messagesController.getChat(Integer.valueOf(this.currentMessageObject.messageOwner.to_id.channel_id));
            }
        } else if (fwd_from.saved_from_peer.user_id != 0) {
            if (fwd_from.from_id != 0) {
                this.currentUser = messagesController.getUser(Integer.valueOf(fwd_from.from_id));
            } else {
                this.currentUser = messagesController.getUser(Integer.valueOf(fwd_from.saved_from_peer.user_id));
            }
        } else if (fwd_from.saved_from_peer.channel_id != 0) {
            if (!this.currentMessageObject.isSavedFromMegagroup() || fwd_from.from_id == 0) {
                this.currentChat = messagesController.getChat(Integer.valueOf(fwd_from.saved_from_peer.channel_id));
            } else {
                this.currentUser = messagesController.getUser(Integer.valueOf(fwd_from.from_id));
            }
        } else if (fwd_from.saved_from_peer.chat_id == 0) {
        } else {
            if (fwd_from.from_id != 0) {
                this.currentUser = messagesController.getUser(Integer.valueOf(fwd_from.from_id));
            } else {
                this.currentChat = messagesController.getChat(Integer.valueOf(fwd_from.saved_from_peer.chat_id));
            }
        }
    }

    private void setMessageObjectInternal(MessageObject messageObject) {
        SpannableStringBuilder spannableStringBuilder;
        String name;
        if (!((messageObject.messageOwner.flags & 1024) == 0 || this.currentMessageObject.viewsReloaded)) {
            MessagesController.getInstance(this.currentAccount).addToViewsQueue(this.currentMessageObject.messageOwner);
            this.currentMessageObject.viewsReloaded = true;
        }
        updateCurrentUserAndChat();
        if (this.isAvatarVisible) {
            if (this.currentUser != null) {
                if (this.currentUser.photo != null) {
                    this.currentPhoto = this.currentUser.photo.photo_small;
                } else {
                    this.currentPhoto = null;
                }
                this.avatarDrawable.setInfo(this.currentUser);
            } else if (this.currentChat != null) {
                if (this.currentChat.photo != null) {
                    this.currentPhoto = this.currentChat.photo.photo_small;
                } else {
                    this.currentPhoto = null;
                }
                this.avatarDrawable.setInfo(this.currentChat);
            } else {
                this.currentPhoto = null;
                this.avatarDrawable.setInfo(messageObject.messageOwner.from_id, null, null, false);
            }
            this.avatarImage.setImage(this.currentPhoto, "50_50", this.avatarDrawable, null, 0);
        }
        measureTime(messageObject);
        this.namesOffset = 0;
        String viaUsername = null;
        CharSequence viaString = null;
        if (messageObject.messageOwner.via_bot_id != 0) {
            User botUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.via_bot_id));
            if (!(botUser == null || botUser.username == null || botUser.username.length() <= 0)) {
                viaUsername = "@" + botUser.username;
                viaString = AndroidUtilities.replaceTags(String.format(" via <b>%s</b>", new Object[]{viaUsername}));
                this.viaWidth = (int) Math.ceil((double) Theme.chat_replyNamePaint.measureText(viaString, 0, viaString.length()));
                this.currentViaBotUser = botUser;
            }
        } else if (messageObject.messageOwner.via_bot_name != null && messageObject.messageOwner.via_bot_name.length() > 0) {
            viaUsername = "@" + messageObject.messageOwner.via_bot_name;
            viaString = AndroidUtilities.replaceTags(String.format(" via <b>%s</b>", new Object[]{viaUsername}));
            this.viaWidth = (int) Math.ceil((double) Theme.chat_replyNamePaint.measureText(viaString, 0, viaString.length()));
        }
        boolean authorName = this.drawName && this.isChat && !this.currentMessageObject.isOutOwner();
        boolean viaBot = (messageObject.messageOwner.fwd_from == null || messageObject.type == 14) && viaUsername != null;
        if (authorName || viaBot) {
            String adminString;
            int adminWidth;
            this.drawNameLayout = true;
            this.nameWidth = getMaxNameWidth();
            if (this.nameWidth < 0) {
                this.nameWidth = AndroidUtilities.dp(100.0f);
            }
            if (this.currentUser == null || this.currentMessageObject.isOutOwner() || this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5 || !this.delegate.isChatAdminCell(this.currentUser.id)) {
                adminString = null;
                adminWidth = 0;
            } else {
                adminString = LocaleController.getString("ChatAdmin", R.string.ChatAdmin);
                adminWidth = (int) Math.ceil((double) Theme.chat_adminPaint.measureText(adminString));
                this.nameWidth -= adminWidth;
            }
            if (!authorName) {
                this.currentNameString = TtmlNode.ANONYMOUS_REGION_ID;
            } else if (this.currentUser != null) {
                this.currentNameString = UserObject.getUserName(this.currentUser);
            } else if (this.currentChat != null) {
                this.currentNameString = this.currentChat.title;
            } else {
                this.currentNameString = "DELETED";
            }
            CharSequence nameStringFinal = TextUtils.ellipsize(this.currentNameString.replace('\n', ' '), Theme.chat_namePaint, (float) (this.nameWidth - (viaBot ? this.viaWidth : 0)), TruncateAt.END);
            if (viaBot) {
                int color;
                this.viaNameWidth = (int) Math.ceil((double) Theme.chat_namePaint.measureText(nameStringFinal, 0, nameStringFinal.length()));
                if (this.viaNameWidth != 0) {
                    this.viaNameWidth += AndroidUtilities.dp(4.0f);
                }
                if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                    color = Theme.getColor(Theme.key_chat_stickerViaBotNameText);
                } else {
                    color = Theme.getColor(this.currentMessageObject.isOutOwner() ? Theme.key_chat_outViaBotNameText : Theme.key_chat_inViaBotNameText);
                }
                if (this.currentNameString.length() > 0) {
                    spannableStringBuilder = new SpannableStringBuilder(String.format("%s via %s", new Object[]{nameStringFinal, viaUsername}));
                    spannableStringBuilder.setSpan(new TypefaceSpan(Typeface.DEFAULT, 0, color), nameStringFinal.length() + 1, nameStringFinal.length() + 4, 33);
                    spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, color), nameStringFinal.length() + 5, spannableStringBuilder.length(), 33);
                    nameStringFinal = spannableStringBuilder;
                } else {
                    spannableStringBuilder = new SpannableStringBuilder(String.format("via %s", new Object[]{viaUsername}));
                    spannableStringBuilder.setSpan(new TypefaceSpan(Typeface.DEFAULT, 0, color), 0, 4, 33);
                    spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf"), 0, color), 4, spannableStringBuilder.length(), 33);
                    Object nameStringFinal2 = spannableStringBuilder;
                }
                nameStringFinal = TextUtils.ellipsize(nameStringFinal, Theme.chat_namePaint, (float) this.nameWidth, TruncateAt.END);
            }
            try {
                this.nameLayout = new StaticLayout(nameStringFinal, Theme.chat_namePaint, this.nameWidth + AndroidUtilities.dp(2.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                if (this.nameLayout == null || this.nameLayout.getLineCount() <= 0) {
                    this.nameWidth = 0;
                } else {
                    this.nameWidth = (int) Math.ceil((double) this.nameLayout.getLineWidth(0));
                    if (messageObject.type != 13) {
                        this.namesOffset += AndroidUtilities.dp(19.0f);
                    }
                    this.nameOffsetX = this.nameLayout.getLineLeft(0);
                }
                if (adminString != null) {
                    this.adminLayout = new StaticLayout(adminString, Theme.chat_adminPaint, adminWidth + AndroidUtilities.dp(2.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.nameWidth = (int) (((float) this.nameWidth) + (this.adminLayout.getLineWidth(0) + ((float) AndroidUtilities.dp(8.0f))));
                } else {
                    this.adminLayout = null;
                }
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (this.currentNameString.length() == 0) {
                this.currentNameString = null;
            }
        } else {
            this.currentNameString = null;
            this.nameLayout = null;
            this.nameWidth = 0;
        }
        this.currentForwardUser = null;
        this.currentForwardNameString = null;
        this.currentForwardChannel = null;
        this.forwardedNameLayout[0] = null;
        this.forwardedNameLayout[1] = null;
        this.forwardedNameWidth = 0;
        if (this.drawForwardedName && messageObject.needDrawForwarded() && (this.currentPosition == null || this.currentPosition.minY == (byte) 0)) {
            if (messageObject.messageOwner.fwd_from.channel_id != 0) {
                this.currentForwardChannel = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(messageObject.messageOwner.fwd_from.channel_id));
            }
            if (messageObject.messageOwner.fwd_from.from_id != 0) {
                this.currentForwardUser = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.fwd_from.from_id));
            }
            if (!(this.currentForwardUser == null && this.currentForwardChannel == null)) {
                if (this.currentForwardChannel != null) {
                    if (this.currentForwardUser != null) {
                        this.currentForwardNameString = String.format("%s (%s)", new Object[]{this.currentForwardChannel.title, UserObject.getUserName(this.currentForwardUser)});
                    } else {
                        this.currentForwardNameString = this.currentForwardChannel.title;
                    }
                } else if (this.currentForwardUser != null) {
                    this.currentForwardNameString = UserObject.getUserName(this.currentForwardUser);
                }
                this.forwardedNameWidth = getMaxNameWidth();
                String fromString = LocaleController.getString("From", R.string.From);
                name = TextUtils.ellipsize(this.currentForwardNameString.replace('\n', ' '), Theme.chat_replyNamePaint, (float) ((this.forwardedNameWidth - ((int) Math.ceil((double) Theme.chat_forwardNamePaint.measureText(fromString + " ")))) - this.viaWidth), TruncateAt.END);
                if (viaString != null) {
                    spannableStringBuilder = new SpannableStringBuilder(String.format("%s %s via %s", new Object[]{fromString, name, viaUsername}));
                    this.viaNameWidth = (int) Math.ceil((double) Theme.chat_forwardNamePaint.measureText(fromString + " " + name));
                    spannableStringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), (spannableStringBuilder.length() - viaUsername.length()) - 1, spannableStringBuilder.length(), 33);
                } else {
                    spannableStringBuilder = new SpannableStringBuilder(String.format("%s %s", new Object[]{fromString, name}));
                }
                stringBuilder.setSpan(new TypefaceSpan(AndroidUtilities.getTypeface("fonts/rmedium.ttf")), fromString.length() + 1, (fromString.length() + 1) + name.length(), 33);
                try {
                    this.forwardedNameLayout[1] = new StaticLayout(TextUtils.ellipsize(stringBuilder, Theme.chat_forwardNamePaint, (float) this.forwardedNameWidth, TruncateAt.END), Theme.chat_forwardNamePaint, this.forwardedNameWidth + AndroidUtilities.dp(2.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.forwardedNameLayout[0] = new StaticLayout(TextUtils.ellipsize(AndroidUtilities.replaceTags(LocaleController.getString("ForwardedMessage", R.string.ForwardedMessage)), Theme.chat_forwardNamePaint, (float) this.forwardedNameWidth, TruncateAt.END), Theme.chat_forwardNamePaint, this.forwardedNameWidth + AndroidUtilities.dp(2.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    this.forwardedNameWidth = Math.max((int) Math.ceil((double) this.forwardedNameLayout[0].getLineWidth(0)), (int) Math.ceil((double) this.forwardedNameLayout[1].getLineWidth(0)));
                    this.forwardNameOffsetX[0] = this.forwardedNameLayout[0].getLineLeft(0);
                    this.forwardNameOffsetX[1] = this.forwardedNameLayout[1].getLineLeft(0);
                    if (messageObject.type != 5) {
                        this.namesOffset += AndroidUtilities.dp(36.0f);
                    }
                } catch (Throwable e2) {
                    FileLog.e(e2);
                }
            }
        }
        if (messageObject.hasValidReplyMessageObject() && (this.currentPosition == null || this.currentPosition.minY == (byte) 0)) {
            if (!(messageObject.type == 13 || messageObject.type == 5)) {
                this.namesOffset += AndroidUtilities.dp(42.0f);
                if (messageObject.type != 0) {
                    this.namesOffset += AndroidUtilities.dp(5.0f);
                }
            }
            int maxWidth = getMaxNameWidth();
            if (!(messageObject.type == 13 || messageObject.type == 5)) {
                maxWidth -= AndroidUtilities.dp(10.0f);
            }
            CharSequence stringFinalText = null;
            PhotoSize photoSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.replyMessageObject.photoThumbs2, 80);
            if (photoSize == null) {
                photoSize = FileLoader.getClosestPhotoSizeWithSize(messageObject.replyMessageObject.photoThumbs, 80);
            }
            if (photoSize == null || messageObject.replyMessageObject.type == 13 || ((messageObject.type == 13 && !AndroidUtilities.isTablet()) || messageObject.replyMessageObject.isSecretMedia())) {
                this.replyImageReceiver.setImageBitmap((Drawable) null);
                this.needReplyImage = false;
            } else {
                if (messageObject.replyMessageObject.isRoundVideo()) {
                    this.replyImageReceiver.setRoundRadius(AndroidUtilities.dp(22.0f));
                } else {
                    this.replyImageReceiver.setRoundRadius(0);
                }
                this.currentReplyPhoto = photoSize.location;
                this.replyImageReceiver.setImage(photoSize.location, "50_50", null, null, 1);
                this.needReplyImage = true;
                maxWidth -= AndroidUtilities.dp(44.0f);
            }
            name = null;
            if (messageObject.customReplyName != null) {
                name = messageObject.customReplyName;
            } else if (messageObject.replyMessageObject.isFromUser()) {
                User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.replyMessageObject.messageOwner.from_id));
                if (user != null) {
                    name = UserObject.getUserName(user);
                }
            } else if (messageObject.replyMessageObject.messageOwner.from_id < 0) {
                chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-messageObject.replyMessageObject.messageOwner.from_id));
                if (chat != null) {
                    name = chat.title;
                }
            } else {
                chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(messageObject.replyMessageObject.messageOwner.to_id.channel_id));
                if (chat != null) {
                    name = chat.title;
                }
            }
            if (name == null) {
                name = LocaleController.getString("Loading", R.string.Loading);
            }
            CharSequence stringFinalName = TextUtils.ellipsize(name.replace('\n', ' '), Theme.chat_replyNamePaint, (float) maxWidth, TruncateAt.END);
            if (messageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaGame) {
                stringFinalText = TextUtils.ellipsize(Emoji.replaceEmoji(messageObject.replyMessageObject.messageOwner.media.game.title, Theme.chat_replyTextPaint.getFontMetricsInt(), AndroidUtilities.dp(14.0f), false), Theme.chat_replyTextPaint, (float) maxWidth, TruncateAt.END);
            } else if (messageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaInvoice) {
                stringFinalText = TextUtils.ellipsize(Emoji.replaceEmoji(messageObject.replyMessageObject.messageOwner.media.title, Theme.chat_replyTextPaint.getFontMetricsInt(), AndroidUtilities.dp(14.0f), false), Theme.chat_replyTextPaint, (float) maxWidth, TruncateAt.END);
            } else if (messageObject.replyMessageObject.messageText != null && messageObject.replyMessageObject.messageText.length() > 0) {
                String mess = messageObject.replyMessageObject.messageText.toString();
                if (mess.length() > 150) {
                    mess = mess.substring(0, 150);
                }
                stringFinalText = TextUtils.ellipsize(Emoji.replaceEmoji(mess.replace('\n', ' '), Theme.chat_replyTextPaint.getFontMetricsInt(), AndroidUtilities.dp(14.0f), false), Theme.chat_replyTextPaint, (float) maxWidth, TruncateAt.END);
            }
            try {
                this.replyNameWidth = AndroidUtilities.dp((float) ((this.needReplyImage ? 44 : 0) + 4));
                if (stringFinalName != null) {
                    this.replyNameLayout = new StaticLayout(stringFinalName, Theme.chat_replyNamePaint, maxWidth + AndroidUtilities.dp(6.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    if (this.replyNameLayout.getLineCount() > 0) {
                        this.replyNameWidth += ((int) Math.ceil((double) this.replyNameLayout.getLineWidth(0))) + AndroidUtilities.dp(8.0f);
                        this.replyNameOffset = this.replyNameLayout.getLineLeft(0);
                    }
                }
            } catch (Throwable e22) {
                FileLog.e(e22);
            }
            try {
                this.replyTextWidth = AndroidUtilities.dp((float) ((this.needReplyImage ? 44 : 0) + 4));
                if (stringFinalText != null) {
                    this.replyTextLayout = new StaticLayout(stringFinalText, Theme.chat_replyTextPaint, maxWidth + AndroidUtilities.dp(6.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    if (this.replyTextLayout.getLineCount() > 0) {
                        this.replyTextWidth += ((int) Math.ceil((double) this.replyTextLayout.getLineWidth(0))) + AndroidUtilities.dp(8.0f);
                        this.replyTextOffset = this.replyTextLayout.getLineLeft(0);
                    }
                }
            } catch (Throwable e222) {
                FileLog.e(e222);
            }
        }
        requestLayout();
    }

    public ImageReceiver getAvatarImage() {
        return this.isAvatarVisible ? this.avatarImage : null;
    }

    protected void onDraw(Canvas canvas) {
        if (this.currentMessageObject != null) {
            if (this.wasLayout) {
                Drawable currentBackgroundSelectedDrawable;
                Drawable currentBackgroundShadowDrawable;
                int i;
                long newTime;
                long dt;
                if (this.currentMessageObject.isOutOwner()) {
                    Theme.chat_msgTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
                    Theme.chat_msgTextPaint.linkColor = Theme.getColor(Theme.key_chat_messageLinkOut);
                    Theme.chat_msgGameTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextOut));
                    Theme.chat_msgGameTextPaint.linkColor = Theme.getColor(Theme.key_chat_messageLinkOut);
                    Theme.chat_replyTextPaint.linkColor = Theme.getColor(Theme.key_chat_messageLinkOut);
                } else {
                    Theme.chat_msgTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
                    Theme.chat_msgTextPaint.linkColor = Theme.getColor(Theme.key_chat_messageLinkIn);
                    Theme.chat_msgGameTextPaint.setColor(Theme.getColor(Theme.key_chat_messageTextIn));
                    Theme.chat_msgGameTextPaint.linkColor = Theme.getColor(Theme.key_chat_messageLinkIn);
                    Theme.chat_replyTextPaint.linkColor = Theme.getColor(Theme.key_chat_messageLinkIn);
                }
                if (this.documentAttach != null) {
                    if (this.documentAttachType == 3) {
                        if (this.currentMessageObject.isOutOwner()) {
                            this.seekBarWaveform.setColors(Theme.getColor(Theme.key_chat_outVoiceSeekbar), Theme.getColor(Theme.key_chat_outVoiceSeekbarFill), Theme.getColor(Theme.key_chat_outVoiceSeekbarSelected));
                            this.seekBar.setColors(Theme.getColor(Theme.key_chat_outAudioSeekbar), Theme.getColor(Theme.key_chat_outAudioSeekbarFill), Theme.getColor(Theme.key_chat_outAudioSeekbarSelected));
                        } else {
                            this.seekBarWaveform.setColors(Theme.getColor(Theme.key_chat_inVoiceSeekbar), Theme.getColor(Theme.key_chat_inVoiceSeekbarFill), Theme.getColor(Theme.key_chat_inVoiceSeekbarSelected));
                            this.seekBar.setColors(Theme.getColor(Theme.key_chat_inAudioSeekbar), Theme.getColor(Theme.key_chat_inAudioSeekbarFill), Theme.getColor(Theme.key_chat_inAudioSeekbarSelected));
                        }
                    } else if (this.documentAttachType == 5) {
                        this.documentAttachType = 5;
                        if (this.currentMessageObject.isOutOwner()) {
                            this.seekBar.setColors(Theme.getColor(Theme.key_chat_outAudioSeekbar), Theme.getColor(Theme.key_chat_outAudioSeekbarFill), Theme.getColor(Theme.key_chat_outAudioSeekbarSelected));
                        } else {
                            this.seekBar.setColors(Theme.getColor(Theme.key_chat_inAudioSeekbar), Theme.getColor(Theme.key_chat_inAudioSeekbarFill), Theme.getColor(Theme.key_chat_inAudioSeekbarSelected));
                        }
                    }
                }
                if (this.currentMessageObject.type == 5) {
                    Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_mediaTimeText));
                } else if (this.mediaBackground) {
                    if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                        Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_serviceText));
                    } else {
                        Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_mediaTimeText));
                    }
                } else if (this.currentMessageObject.isOutOwner()) {
                    Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outTimeSelectedText : Theme.key_chat_outTimeText));
                } else {
                    Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inTimeSelectedText : Theme.key_chat_inTimeText));
                }
                int additionalTop = 0;
                int additionalBottom = 0;
                int i2;
                int offsetBottom;
                int backgroundTop;
                if (this.currentMessageObject.isOutOwner()) {
                    if (this.mediaBackground || this.drawPinnedBottom) {
                        this.currentBackgroundDrawable = Theme.chat_msgOutMediaDrawable;
                        currentBackgroundSelectedDrawable = Theme.chat_msgOutMediaSelectedDrawable;
                        currentBackgroundShadowDrawable = Theme.chat_msgOutMediaShadowDrawable;
                    } else {
                        this.currentBackgroundDrawable = Theme.chat_msgOutDrawable;
                        currentBackgroundSelectedDrawable = Theme.chat_msgOutSelectedDrawable;
                        currentBackgroundShadowDrawable = Theme.chat_msgOutShadowDrawable;
                    }
                    this.backgroundDrawableLeft = (this.layoutWidth - this.backgroundWidth) - (!this.mediaBackground ? 0 : AndroidUtilities.dp(9.0f));
                    i = this.backgroundWidth;
                    if (this.mediaBackground) {
                        i2 = 0;
                    } else {
                        i2 = AndroidUtilities.dp(3.0f);
                    }
                    this.backgroundDrawableRight = i - i2;
                    if (!(this.currentMessagesGroup == null || this.currentPosition.edge)) {
                        this.backgroundDrawableRight += AndroidUtilities.dp(10.0f);
                    }
                    int backgroundLeft = this.backgroundDrawableLeft;
                    if (!this.mediaBackground && this.drawPinnedBottom) {
                        this.backgroundDrawableRight -= AndroidUtilities.dp(6.0f);
                    }
                    if (this.currentPosition != null) {
                        if ((this.currentPosition.flags & 2) == 0) {
                            this.backgroundDrawableRight += AndroidUtilities.dp(8.0f);
                        }
                        if ((this.currentPosition.flags & 1) == 0) {
                            backgroundLeft -= AndroidUtilities.dp(8.0f);
                            this.backgroundDrawableRight += AndroidUtilities.dp(8.0f);
                        }
                        if ((this.currentPosition.flags & 4) == 0) {
                            additionalTop = 0 - AndroidUtilities.dp(9.0f);
                            additionalBottom = 0 + AndroidUtilities.dp(9.0f);
                        }
                        if ((this.currentPosition.flags & 8) == 0) {
                            additionalBottom += AndroidUtilities.dp(9.0f);
                        }
                    }
                    if (this.drawPinnedBottom && this.drawPinnedTop) {
                        offsetBottom = 0;
                    } else if (this.drawPinnedBottom) {
                        offsetBottom = AndroidUtilities.dp(1.0f);
                    } else {
                        offsetBottom = AndroidUtilities.dp(2.0f);
                    }
                    i2 = (this.drawPinnedTop || (this.drawPinnedTop && this.drawPinnedBottom)) ? 0 : AndroidUtilities.dp(1.0f);
                    backgroundTop = additionalTop + i2;
                    BaseCell.setDrawableBounds(this.currentBackgroundDrawable, backgroundLeft, backgroundTop, this.backgroundDrawableRight, (this.layoutHeight - offsetBottom) + additionalBottom);
                    BaseCell.setDrawableBounds(currentBackgroundSelectedDrawable, backgroundLeft, backgroundTop, this.backgroundDrawableRight, (this.layoutHeight - offsetBottom) + additionalBottom);
                    BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, backgroundLeft, backgroundTop, this.backgroundDrawableRight, (this.layoutHeight - offsetBottom) + additionalBottom);
                } else {
                    if (this.mediaBackground || this.drawPinnedBottom) {
                        this.currentBackgroundDrawable = Theme.chat_msgInMediaDrawable;
                        currentBackgroundSelectedDrawable = Theme.chat_msgInMediaSelectedDrawable;
                        currentBackgroundShadowDrawable = Theme.chat_msgInMediaShadowDrawable;
                    } else {
                        this.currentBackgroundDrawable = Theme.chat_msgInDrawable;
                        currentBackgroundSelectedDrawable = Theme.chat_msgInSelectedDrawable;
                        currentBackgroundShadowDrawable = Theme.chat_msgInShadowDrawable;
                    }
                    i2 = (this.isChat && this.isAvatarVisible) ? 48 : 0;
                    this.backgroundDrawableLeft = AndroidUtilities.dp((float) (i2 + (!this.mediaBackground ? 3 : 9)));
                    i = this.backgroundWidth;
                    if (this.mediaBackground) {
                        i2 = 0;
                    } else {
                        i2 = AndroidUtilities.dp(3.0f);
                    }
                    this.backgroundDrawableRight = i - i2;
                    if (this.currentMessagesGroup != null) {
                        if (!this.currentPosition.edge) {
                            this.backgroundDrawableLeft -= AndroidUtilities.dp(10.0f);
                            this.backgroundDrawableRight += AndroidUtilities.dp(10.0f);
                        }
                        if (this.currentPosition.leftSpanOffset != 0) {
                            this.backgroundDrawableLeft += (int) Math.ceil((double) ((((float) this.currentPosition.leftSpanOffset) / 1000.0f) * ((float) getGroupPhotosWidth())));
                        }
                    }
                    if (!this.mediaBackground && this.drawPinnedBottom) {
                        this.backgroundDrawableRight -= AndroidUtilities.dp(6.0f);
                        this.backgroundDrawableLeft += AndroidUtilities.dp(6.0f);
                    }
                    if (this.currentPosition != null) {
                        if ((this.currentPosition.flags & 2) == 0) {
                            this.backgroundDrawableRight += AndroidUtilities.dp(8.0f);
                        }
                        if ((this.currentPosition.flags & 1) == 0) {
                            this.backgroundDrawableLeft -= AndroidUtilities.dp(8.0f);
                            this.backgroundDrawableRight += AndroidUtilities.dp(8.0f);
                        }
                        if ((this.currentPosition.flags & 4) == 0) {
                            additionalTop = 0 - AndroidUtilities.dp(9.0f);
                            additionalBottom = 0 + AndroidUtilities.dp(9.0f);
                        }
                        if ((this.currentPosition.flags & 8) == 0) {
                            additionalBottom += AndroidUtilities.dp(10.0f);
                        }
                    }
                    if (this.drawPinnedBottom && this.drawPinnedTop) {
                        offsetBottom = 0;
                    } else if (this.drawPinnedBottom) {
                        offsetBottom = AndroidUtilities.dp(1.0f);
                    } else {
                        offsetBottom = AndroidUtilities.dp(2.0f);
                    }
                    i2 = (this.drawPinnedTop || (this.drawPinnedTop && this.drawPinnedBottom)) ? 0 : AndroidUtilities.dp(1.0f);
                    backgroundTop = additionalTop + i2;
                    BaseCell.setDrawableBounds(this.currentBackgroundDrawable, this.backgroundDrawableLeft, backgroundTop, this.backgroundDrawableRight, (this.layoutHeight - offsetBottom) + additionalBottom);
                    BaseCell.setDrawableBounds(currentBackgroundSelectedDrawable, this.backgroundDrawableLeft, backgroundTop, this.backgroundDrawableRight, (this.layoutHeight - offsetBottom) + additionalBottom);
                    BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, this.backgroundDrawableLeft, backgroundTop, this.backgroundDrawableRight, (this.layoutHeight - offsetBottom) + additionalBottom);
                }
                if (this.drawBackground && this.currentBackgroundDrawable != null) {
                    if (this.isHighlightedAnimated) {
                        float alpha;
                        this.currentBackgroundDrawable.draw(canvas);
                        if (this.highlightProgress >= 300) {
                            alpha = 1.0f;
                        } else {
                            alpha = ((float) this.highlightProgress) / 300.0f;
                        }
                        if (this.currentPosition == null) {
                            currentBackgroundSelectedDrawable.setAlpha((int) (255.0f * alpha));
                            currentBackgroundSelectedDrawable.draw(canvas);
                        }
                        newTime = System.currentTimeMillis();
                        dt = Math.abs(newTime - this.lastHighlightProgressTime);
                        if (dt > 17) {
                            dt = 17;
                        }
                        this.highlightProgress = (int) (((long) this.highlightProgress) - dt);
                        this.lastHighlightProgressTime = newTime;
                        if (this.highlightProgress <= 0) {
                            this.highlightProgress = 0;
                            this.isHighlightedAnimated = false;
                        }
                        invalidate();
                    } else if (!isDrawSelectedBackground() || (this.currentPosition != null && getBackground() == null)) {
                        this.currentBackgroundDrawable.draw(canvas);
                    } else {
                        currentBackgroundSelectedDrawable.setAlpha(255);
                        currentBackgroundSelectedDrawable.draw(canvas);
                    }
                    if (this.currentPosition == null || this.currentPosition.flags != 0) {
                        currentBackgroundShadowDrawable.draw(canvas);
                    }
                }
                drawContent(canvas);
                if (this.drawShareButton) {
                    Theme.chat_shareDrawable.setColorFilter(this.sharePressed ? Theme.colorPressedFilter : Theme.colorFilter);
                    if (this.currentMessageObject.isOutOwner()) {
                        this.shareStartX = (this.currentBackgroundDrawable.getBounds().left - AndroidUtilities.dp(8.0f)) - Theme.chat_shareDrawable.getIntrinsicWidth();
                    } else {
                        this.shareStartX = this.currentBackgroundDrawable.getBounds().right + AndroidUtilities.dp(8.0f);
                    }
                    Drawable drawable = Theme.chat_shareDrawable;
                    i = this.shareStartX;
                    int dp = this.layoutHeight - AndroidUtilities.dp(41.0f);
                    this.shareStartY = dp;
                    BaseCell.setDrawableBounds(drawable, i, dp);
                    Theme.chat_shareDrawable.draw(canvas);
                    if (this.drwaShareGoIcon) {
                        BaseCell.setDrawableBounds(Theme.chat_goIconDrawable, this.shareStartX + AndroidUtilities.dp(12.0f), this.shareStartY + AndroidUtilities.dp(9.0f));
                        Theme.chat_goIconDrawable.draw(canvas);
                    } else {
                        BaseCell.setDrawableBounds(Theme.chat_shareIconDrawable, this.shareStartX + AndroidUtilities.dp(9.0f), this.shareStartY + AndroidUtilities.dp(9.0f));
                        Theme.chat_shareIconDrawable.draw(canvas);
                    }
                }
                if (this.currentPosition == null) {
                    drawNamesLayout(canvas);
                }
                if ((this.drawTime || !this.mediaBackground) && !this.forceNotDrawTime) {
                    drawTimeLayout(canvas);
                }
                if (this.controlsAlpha != 1.0f || this.timeAlpha != 1.0f) {
                    newTime = System.currentTimeMillis();
                    dt = Math.abs(this.lastControlsAlphaChangeTime - newTime);
                    if (dt > 17) {
                        dt = 17;
                    }
                    this.totalChangeTime += dt;
                    if (this.totalChangeTime > 100) {
                        this.totalChangeTime = 100;
                    }
                    this.lastControlsAlphaChangeTime = newTime;
                    if (this.controlsAlpha != 1.0f) {
                        this.controlsAlpha = AndroidUtilities.decelerateInterpolator.getInterpolation(((float) this.totalChangeTime) / 100.0f);
                    }
                    if (this.timeAlpha != 1.0f) {
                        this.timeAlpha = AndroidUtilities.decelerateInterpolator.getInterpolation(((float) this.totalChangeTime) / 100.0f);
                    }
                    invalidate();
                    if (this.forceNotDrawTime && this.currentPosition != null && this.currentPosition.last && getParent() != null) {
                        ((View) getParent()).invalidate();
                        return;
                    }
                    return;
                }
                return;
            }
            requestLayout();
        }
    }

    public int getBackgroundDrawableLeft() {
        int i = 0;
        if (this.currentMessageObject.isOutOwner()) {
            int i2 = this.layoutWidth - this.backgroundWidth;
            if (this.mediaBackground) {
                i = AndroidUtilities.dp(9.0f);
            }
            return i2 - i;
        }
        if (this.isChat && this.isAvatarVisible) {
            i = 48;
        }
        return AndroidUtilities.dp((float) (i + (!this.mediaBackground ? 3 : 9)));
    }

    public boolean hasNameLayout() {
        return (this.drawNameLayout && this.nameLayout != null) || ((this.drawForwardedName && this.forwardedNameLayout[0] != null && this.forwardedNameLayout[1] != null && (this.currentPosition == null || (this.currentPosition.minY == (byte) 0 && this.currentPosition.minX == (byte) 0))) || this.replyNameLayout != null);
    }

    public void drawNamesLayout(Canvas canvas) {
        float f;
        int i;
        float f2 = 11.0f;
        int i2 = 0;
        if (this.drawNameLayout && this.nameLayout != null) {
            canvas.save();
            if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                Theme.chat_namePaint.setColor(Theme.getColor(Theme.key_chat_stickerNameText));
                if (this.currentMessageObject.isOutOwner()) {
                    this.nameX = (float) AndroidUtilities.dp(28.0f);
                } else {
                    this.nameX = (float) ((this.backgroundDrawableLeft + this.backgroundDrawableRight) + AndroidUtilities.dp(22.0f));
                }
                this.nameY = (float) (this.layoutHeight - AndroidUtilities.dp(38.0f));
                Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
                Theme.chat_systemDrawable.setBounds(((int) this.nameX) - AndroidUtilities.dp(12.0f), ((int) this.nameY) - AndroidUtilities.dp(5.0f), (((int) this.nameX) + AndroidUtilities.dp(12.0f)) + this.nameWidth, ((int) this.nameY) + AndroidUtilities.dp(22.0f));
                Theme.chat_systemDrawable.draw(canvas);
            } else {
                if (this.mediaBackground || this.currentMessageObject.isOutOwner()) {
                    this.nameX = ((float) (this.backgroundDrawableLeft + AndroidUtilities.dp(11.0f))) - this.nameOffsetX;
                } else {
                    int i3 = this.backgroundDrawableLeft;
                    f = (this.mediaBackground || !this.drawPinnedBottom) ? 17.0f : 11.0f;
                    this.nameX = ((float) (AndroidUtilities.dp(f) + i3)) - this.nameOffsetX;
                }
                if (this.currentUser != null) {
                    Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(this.currentUser.id));
                } else if (this.currentChat == null) {
                    Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(0));
                } else if (!ChatObject.isChannel(this.currentChat) || this.currentChat.megagroup) {
                    Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(this.currentChat.id));
                } else {
                    Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(5));
                }
                if (this.drawPinnedTop) {
                    f = 9.0f;
                } else {
                    f = 10.0f;
                }
                this.nameY = (float) AndroidUtilities.dp(f);
            }
            canvas.translate(this.nameX, this.nameY);
            this.nameLayout.draw(canvas);
            canvas.restore();
            if (this.adminLayout != null) {
                Theme.chat_adminPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_adminSelectedText : Theme.key_chat_adminText));
                canvas.save();
                canvas.translate(((float) ((this.backgroundDrawableLeft + this.backgroundDrawableRight) - AndroidUtilities.dp(11.0f))) - this.adminLayout.getLineWidth(0), this.nameY + ((float) AndroidUtilities.dp(0.5f)));
                this.adminLayout.draw(canvas);
                canvas.restore();
            }
        }
        if (this.drawForwardedName && this.forwardedNameLayout[0] != null && this.forwardedNameLayout[1] != null && (this.currentPosition == null || (this.currentPosition.minY == (byte) 0 && this.currentPosition.minX == (byte) 0))) {
            if (this.currentMessageObject.type == 5) {
                Theme.chat_forwardNamePaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyNameText));
                if (this.currentMessageObject.isOutOwner()) {
                    this.forwardNameX = AndroidUtilities.dp(23.0f);
                } else {
                    this.forwardNameX = (this.backgroundDrawableLeft + this.backgroundDrawableRight) + AndroidUtilities.dp(17.0f);
                }
                this.forwardNameY = AndroidUtilities.dp(12.0f);
                int backWidth = this.forwardedNameWidth + AndroidUtilities.dp(14.0f);
                Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
                Theme.chat_systemDrawable.setBounds(this.forwardNameX - AndroidUtilities.dp(7.0f), this.forwardNameY - AndroidUtilities.dp(6.0f), (this.forwardNameX - AndroidUtilities.dp(7.0f)) + backWidth, this.forwardNameY + AndroidUtilities.dp(38.0f));
                Theme.chat_systemDrawable.draw(canvas);
            } else {
                if (this.drawNameLayout) {
                    i = 19;
                } else {
                    i = 0;
                }
                this.forwardNameY = AndroidUtilities.dp((float) (i + 10));
                if (this.currentMessageObject.isOutOwner()) {
                    Theme.chat_forwardNamePaint.setColor(Theme.getColor(Theme.key_chat_outForwardedNameText));
                    this.forwardNameX = this.backgroundDrawableLeft + AndroidUtilities.dp(11.0f);
                } else {
                    Theme.chat_forwardNamePaint.setColor(Theme.getColor(Theme.key_chat_inForwardedNameText));
                    if (this.mediaBackground) {
                        this.forwardNameX = this.backgroundDrawableLeft + AndroidUtilities.dp(11.0f);
                    } else {
                        i = this.backgroundDrawableLeft;
                        if (this.mediaBackground || !this.drawPinnedBottom) {
                            f2 = 17.0f;
                        }
                        this.forwardNameX = i + AndroidUtilities.dp(f2);
                    }
                }
            }
            for (int a = 0; a < 2; a++) {
                canvas.save();
                canvas.translate(((float) this.forwardNameX) - this.forwardNameOffsetX[a], (float) (this.forwardNameY + (AndroidUtilities.dp(16.0f) * a)));
                this.forwardedNameLayout[a].draw(canvas);
                canvas.restore();
            }
        }
        if (this.replyNameLayout != null) {
            if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                Theme.chat_replyLinePaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyLine));
                Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyNameText));
                Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyMessageText));
                if (this.currentMessageObject.isOutOwner()) {
                    this.replyStartX = AndroidUtilities.dp(23.0f);
                } else {
                    this.replyStartX = (this.backgroundDrawableLeft + this.backgroundDrawableRight) + AndroidUtilities.dp(17.0f);
                }
                this.replyStartY = AndroidUtilities.dp(12.0f);
                if (this.nameLayout != null) {
                    this.replyStartY -= AndroidUtilities.dp(31.0f);
                }
                backWidth = Math.max(this.replyNameWidth, this.replyTextWidth) + AndroidUtilities.dp(14.0f);
                Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
                Theme.chat_systemDrawable.setBounds(this.replyStartX - AndroidUtilities.dp(7.0f), this.replyStartY - AndroidUtilities.dp(6.0f), (this.replyStartX - AndroidUtilities.dp(7.0f)) + backWidth, this.replyStartY + AndroidUtilities.dp(41.0f));
                Theme.chat_systemDrawable.draw(canvas);
            } else {
                int i4;
                if (this.currentMessageObject.isOutOwner()) {
                    Theme.chat_replyLinePaint.setColor(Theme.getColor(Theme.key_chat_outReplyLine));
                    Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_outReplyNameText));
                    if (!this.currentMessageObject.hasValidReplyMessageObject() || this.currentMessageObject.replyMessageObject.type != 0 || (this.currentMessageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaGame) || (this.currentMessageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaInvoice)) {
                        Theme.chat_replyTextPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outReplyMediaMessageSelectedText : Theme.key_chat_outReplyMediaMessageText));
                    } else {
                        Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_outReplyMessageText));
                    }
                    this.replyStartX = this.backgroundDrawableLeft + AndroidUtilities.dp(12.0f);
                } else {
                    Theme.chat_replyLinePaint.setColor(Theme.getColor(Theme.key_chat_inReplyLine));
                    Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_inReplyNameText));
                    if (!this.currentMessageObject.hasValidReplyMessageObject() || this.currentMessageObject.replyMessageObject.type != 0 || (this.currentMessageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaGame) || (this.currentMessageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaInvoice)) {
                        Theme.chat_replyTextPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inReplyMediaMessageSelectedText : Theme.key_chat_inReplyMediaMessageText));
                    } else {
                        Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_inReplyMessageText));
                    }
                    if (this.mediaBackground) {
                        this.replyStartX = this.backgroundDrawableLeft + AndroidUtilities.dp(12.0f);
                    } else {
                        i4 = this.backgroundDrawableLeft;
                        f = (this.mediaBackground || !this.drawPinnedBottom) ? 18.0f : 12.0f;
                        this.replyStartX = AndroidUtilities.dp(f) + i4;
                    }
                }
                if (!this.drawForwardedName || this.forwardedNameLayout[0] == null) {
                    i = 0;
                } else {
                    i = 36;
                }
                i4 = i + 12;
                if (!this.drawNameLayout || this.nameLayout == null) {
                    i = 0;
                } else {
                    i = 20;
                }
                this.replyStartY = AndroidUtilities.dp((float) (i + i4));
            }
            if (this.currentPosition == null || (this.currentPosition.minY == (byte) 0 && this.currentPosition.minX == (byte) 0)) {
                canvas.drawRect((float) this.replyStartX, (float) this.replyStartY, (float) (this.replyStartX + AndroidUtilities.dp(2.0f)), (float) (this.replyStartY + AndroidUtilities.dp(35.0f)), Theme.chat_replyLinePaint);
                if (this.needReplyImage) {
                    this.replyImageReceiver.setImageCoords(this.replyStartX + AndroidUtilities.dp(10.0f), this.replyStartY, AndroidUtilities.dp(35.0f), AndroidUtilities.dp(35.0f));
                    this.replyImageReceiver.draw(canvas);
                }
                if (this.replyNameLayout != null) {
                    canvas.save();
                    canvas.translate(((float) AndroidUtilities.dp((float) ((this.needReplyImage ? 44 : 0) + 10))) + (((float) this.replyStartX) - this.replyNameOffset), (float) this.replyStartY);
                    this.replyNameLayout.draw(canvas);
                    canvas.restore();
                }
                if (this.replyTextLayout != null) {
                    canvas.save();
                    f = ((float) this.replyStartX) - this.replyTextOffset;
                    if (this.needReplyImage) {
                        i2 = 44;
                    }
                    canvas.translate(f + ((float) AndroidUtilities.dp((float) (i2 + 10))), (float) (this.replyStartY + AndroidUtilities.dp(19.0f)));
                    this.replyTextLayout.draw(canvas);
                    canvas.restore();
                }
            }
        }
    }

    public boolean hasCaptionLayout() {
        return this.captionLayout != null;
    }

    public void drawCaptionLayout(Canvas canvas) {
        float f = 11.0f;
        float f2 = 9.0f;
        if (this.captionLayout != null) {
            canvas.save();
            int imageX;
            int imageY;
            if (this.currentMessageObject.type == 1 || this.documentAttachType == 4 || this.currentMessageObject.type == 8) {
                imageX = this.photoImage.getImageX() + AndroidUtilities.dp(5.0f);
                this.captionX = imageX;
                f = (float) imageX;
                imageY = (this.photoImage.getImageY() + this.photoImage.getImageHeight()) + AndroidUtilities.dp(6.0f);
                this.captionY = imageY;
                canvas.translate(f, (float) imageY);
            } else if (this.hasOldCaptionPreview) {
                r6 = this.backgroundDrawableLeft;
                if (!this.currentMessageObject.isOutOwner()) {
                    f = 17.0f;
                }
                imageX = AndroidUtilities.dp(f) + r6;
                this.captionX = imageX;
                float f3 = (float) imageX;
                imageX = (((this.totalHeight - this.captionHeight) - AndroidUtilities.dp(this.drawPinnedTop ? 9.0f : 10.0f)) - this.linkPreviewHeight) - AndroidUtilities.dp(17.0f);
                this.captionY = imageX;
                canvas.translate(f3, (float) imageX);
            } else {
                r6 = this.backgroundDrawableLeft;
                if (!this.currentMessageObject.isOutOwner()) {
                    f = 17.0f;
                }
                imageX = AndroidUtilities.dp(f) + r6;
                this.captionX = imageX;
                f = (float) imageX;
                imageY = this.totalHeight - this.captionHeight;
                if (!this.drawPinnedTop) {
                    f2 = 10.0f;
                }
                imageY -= AndroidUtilities.dp(f2);
                this.captionY = imageY;
                canvas.translate(f, (float) imageY);
            }
            if (this.pressedLink != null) {
                for (int b = 0; b < this.urlPath.size(); b++) {
                    canvas.drawPath((Path) this.urlPath.get(b), Theme.chat_urlPaint);
                }
            }
            try {
                this.captionLayout.draw(canvas);
            } catch (Throwable e) {
                FileLog.e(e);
            }
            canvas.restore();
        }
    }

    public void drawTimeLayout(Canvas canvas) {
        if ((this.drawTime && !this.groupPhotoInvisible) || !this.mediaBackground) {
            int x;
            int y;
            if (this.currentMessageObject.type == 5) {
                Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_mediaTimeText));
            } else if (this.mediaBackground && this.captionLayout == null) {
                if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                    Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_serviceText));
                } else {
                    Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_mediaTimeText));
                }
            } else if (this.currentMessageObject.isOutOwner()) {
                Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outTimeSelectedText : Theme.key_chat_outTimeText));
            } else {
                Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inTimeSelectedText : Theme.key_chat_inTimeText));
            }
            if (this.drawPinnedBottom) {
                canvas.translate(0.0f, (float) AndroidUtilities.dp(2.0f));
            }
            int additionalX;
            Drawable viewsDrawable;
            if (this.mediaBackground && this.captionLayout == null) {
                Paint paint;
                if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                    paint = Theme.chat_actionBackgroundPaint;
                } else {
                    paint = Theme.chat_timeBackgroundPaint;
                }
                int oldAlpha = paint.getAlpha();
                paint.setAlpha((int) (((float) oldAlpha) * this.timeAlpha));
                Theme.chat_timePaint.setAlpha((int) (255.0f * this.timeAlpha));
                int x1 = this.timeX - AndroidUtilities.dp(4.0f);
                int y1 = this.layoutHeight - AndroidUtilities.dp(28.0f);
                this.rect.set((float) x1, (float) y1, (float) (AndroidUtilities.dp((float) ((this.currentMessageObject.isOutOwner() ? 20 : 0) + 8)) + (x1 + this.timeWidth)), (float) (AndroidUtilities.dp(17.0f) + y1));
                canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(4.0f), (float) AndroidUtilities.dp(4.0f), paint);
                paint.setAlpha(oldAlpha);
                additionalX = (int) (-this.timeLayout.getLineLeft(0));
                if ((this.currentMessageObject.messageOwner.flags & 1024) != 0) {
                    additionalX += (int) (((float) this.timeWidth) - this.timeLayout.getLineWidth(0));
                    if (this.currentMessageObject.isSending()) {
                        if (!this.currentMessageObject.isOutOwner()) {
                            BaseCell.setDrawableBounds(Theme.chat_msgMediaClockDrawable, this.timeX + AndroidUtilities.dp(11.0f), (this.layoutHeight - AndroidUtilities.dp(14.0f)) - Theme.chat_msgMediaClockDrawable.getIntrinsicHeight());
                            Theme.chat_msgMediaClockDrawable.draw(canvas);
                        }
                    } else if (!this.currentMessageObject.isSendError()) {
                        if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                            viewsDrawable = Theme.chat_msgStickerViewsDrawable;
                        } else {
                            viewsDrawable = Theme.chat_msgMediaViewsDrawable;
                        }
                        oldAlpha = ((BitmapDrawable) viewsDrawable).getPaint().getAlpha();
                        viewsDrawable.setAlpha((int) (this.timeAlpha * ((float) oldAlpha)));
                        BaseCell.setDrawableBounds(viewsDrawable, this.timeX, (this.layoutHeight - AndroidUtilities.dp(10.5f)) - this.timeLayout.getHeight());
                        viewsDrawable.draw(canvas);
                        viewsDrawable.setAlpha(oldAlpha);
                        if (this.viewsLayout != null) {
                            canvas.save();
                            canvas.translate((float) ((this.timeX + viewsDrawable.getIntrinsicWidth()) + AndroidUtilities.dp(3.0f)), (float) ((this.layoutHeight - AndroidUtilities.dp(12.3f)) - this.timeLayout.getHeight()));
                            this.viewsLayout.draw(canvas);
                            canvas.restore();
                        }
                    } else if (!this.currentMessageObject.isOutOwner()) {
                        x = this.timeX + AndroidUtilities.dp(11.0f);
                        y = this.layoutHeight - AndroidUtilities.dp(27.5f);
                        this.rect.set((float) x, (float) y, (float) (AndroidUtilities.dp(14.0f) + x), (float) (AndroidUtilities.dp(14.0f) + y));
                        canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(1.0f), (float) AndroidUtilities.dp(1.0f), Theme.chat_msgErrorPaint);
                        BaseCell.setDrawableBounds(Theme.chat_msgErrorDrawable, AndroidUtilities.dp(6.0f) + x, AndroidUtilities.dp(2.0f) + y);
                        Theme.chat_msgErrorDrawable.draw(canvas);
                    }
                }
                canvas.save();
                canvas.translate((float) (this.timeX + additionalX), (float) ((this.layoutHeight - AndroidUtilities.dp(12.3f)) - this.timeLayout.getHeight()));
                this.timeLayout.draw(canvas);
                canvas.restore();
                Theme.chat_timePaint.setAlpha(255);
            } else {
                additionalX = (int) (-this.timeLayout.getLineLeft(0));
                if ((this.currentMessageObject.messageOwner.flags & 1024) != 0) {
                    additionalX += (int) (((float) this.timeWidth) - this.timeLayout.getLineWidth(0));
                    if (this.currentMessageObject.isSending()) {
                        if (!this.currentMessageObject.isOutOwner()) {
                            Drawable clockDrawable = isDrawSelectedBackground() ? Theme.chat_msgInSelectedClockDrawable : Theme.chat_msgInClockDrawable;
                            BaseCell.setDrawableBounds(clockDrawable, this.timeX + AndroidUtilities.dp(11.0f), (this.layoutHeight - AndroidUtilities.dp(8.5f)) - clockDrawable.getIntrinsicHeight());
                            clockDrawable.draw(canvas);
                        }
                    } else if (!this.currentMessageObject.isSendError()) {
                        if (this.currentMessageObject.isOutOwner()) {
                            viewsDrawable = isDrawSelectedBackground() ? Theme.chat_msgOutViewsSelectedDrawable : Theme.chat_msgOutViewsDrawable;
                            BaseCell.setDrawableBounds(viewsDrawable, this.timeX, (this.layoutHeight - AndroidUtilities.dp(4.5f)) - this.timeLayout.getHeight());
                            viewsDrawable.draw(canvas);
                        } else {
                            viewsDrawable = isDrawSelectedBackground() ? Theme.chat_msgInViewsSelectedDrawable : Theme.chat_msgInViewsDrawable;
                            BaseCell.setDrawableBounds(viewsDrawable, this.timeX, (this.layoutHeight - AndroidUtilities.dp(4.5f)) - this.timeLayout.getHeight());
                            viewsDrawable.draw(canvas);
                        }
                        if (this.viewsLayout != null) {
                            canvas.save();
                            canvas.translate((float) ((this.timeX + Theme.chat_msgInViewsDrawable.getIntrinsicWidth()) + AndroidUtilities.dp(3.0f)), (float) ((this.layoutHeight - AndroidUtilities.dp(6.5f)) - this.timeLayout.getHeight()));
                            this.viewsLayout.draw(canvas);
                            canvas.restore();
                        }
                    } else if (!this.currentMessageObject.isOutOwner()) {
                        x = this.timeX + AndroidUtilities.dp(11.0f);
                        y = this.layoutHeight - AndroidUtilities.dp(20.5f);
                        this.rect.set((float) x, (float) y, (float) (AndroidUtilities.dp(14.0f) + x), (float) (AndroidUtilities.dp(14.0f) + y));
                        canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(1.0f), (float) AndroidUtilities.dp(1.0f), Theme.chat_msgErrorPaint);
                        BaseCell.setDrawableBounds(Theme.chat_msgErrorDrawable, AndroidUtilities.dp(6.0f) + x, AndroidUtilities.dp(2.0f) + y);
                        Theme.chat_msgErrorDrawable.draw(canvas);
                    }
                }
                canvas.save();
                canvas.translate((float) (this.timeX + additionalX), (float) ((this.layoutHeight - AndroidUtilities.dp(6.5f)) - this.timeLayout.getHeight()));
                this.timeLayout.draw(canvas);
                canvas.restore();
            }
            if (this.currentMessageObject.isOutOwner()) {
                boolean drawCheck1 = false;
                boolean drawCheck2 = false;
                boolean drawClock = false;
                boolean drawError = false;
                boolean isBroadcast = ((int) (this.currentMessageObject.getDialogId() >> 32)) == 1;
                if (this.currentMessageObject.isSending()) {
                    drawCheck1 = false;
                    drawCheck2 = false;
                    drawClock = true;
                    drawError = false;
                } else if (this.currentMessageObject.isSendError()) {
                    drawCheck1 = false;
                    drawCheck2 = false;
                    drawClock = false;
                    drawError = true;
                } else if (this.currentMessageObject.isSent()) {
                    if (this.currentMessageObject.isUnread()) {
                        drawCheck1 = false;
                        drawCheck2 = true;
                    } else {
                        drawCheck1 = true;
                        drawCheck2 = true;
                    }
                    drawClock = false;
                    drawError = false;
                }
                if (drawClock) {
                    if (!this.mediaBackground || this.captionLayout != null) {
                        BaseCell.setDrawableBounds(Theme.chat_msgOutClockDrawable, (this.layoutWidth - AndroidUtilities.dp(18.5f)) - Theme.chat_msgOutClockDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(8.5f)) - Theme.chat_msgOutClockDrawable.getIntrinsicHeight());
                        Theme.chat_msgOutClockDrawable.draw(canvas);
                    } else if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                        BaseCell.setDrawableBounds(Theme.chat_msgStickerClockDrawable, (this.layoutWidth - AndroidUtilities.dp(22.0f)) - Theme.chat_msgStickerClockDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(13.5f)) - Theme.chat_msgStickerClockDrawable.getIntrinsicHeight());
                        Theme.chat_msgStickerClockDrawable.draw(canvas);
                    } else {
                        BaseCell.setDrawableBounds(Theme.chat_msgMediaClockDrawable, (this.layoutWidth - AndroidUtilities.dp(22.0f)) - Theme.chat_msgMediaClockDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(13.5f)) - Theme.chat_msgMediaClockDrawable.getIntrinsicHeight());
                        Theme.chat_msgMediaClockDrawable.draw(canvas);
                    }
                }
                if (!isBroadcast) {
                    Drawable drawable;
                    if (drawCheck2) {
                        if (!this.mediaBackground || this.captionLayout != null) {
                            drawable = isDrawSelectedBackground() ? Theme.chat_msgOutCheckSelectedDrawable : Theme.chat_msgOutCheckDrawable;
                            if (drawCheck1) {
                                BaseCell.setDrawableBounds(drawable, (this.layoutWidth - AndroidUtilities.dp(22.5f)) - drawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(8.0f)) - drawable.getIntrinsicHeight());
                            } else {
                                BaseCell.setDrawableBounds(drawable, (this.layoutWidth - AndroidUtilities.dp(18.5f)) - drawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(8.0f)) - drawable.getIntrinsicHeight());
                            }
                            drawable.draw(canvas);
                        } else if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                            if (drawCheck1) {
                                BaseCell.setDrawableBounds(Theme.chat_msgStickerCheckDrawable, (this.layoutWidth - AndroidUtilities.dp(26.3f)) - Theme.chat_msgStickerCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(13.5f)) - Theme.chat_msgStickerCheckDrawable.getIntrinsicHeight());
                            } else {
                                BaseCell.setDrawableBounds(Theme.chat_msgStickerCheckDrawable, (this.layoutWidth - AndroidUtilities.dp(21.5f)) - Theme.chat_msgStickerCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(13.5f)) - Theme.chat_msgStickerCheckDrawable.getIntrinsicHeight());
                            }
                            Theme.chat_msgStickerCheckDrawable.draw(canvas);
                        } else {
                            if (drawCheck1) {
                                BaseCell.setDrawableBounds(Theme.chat_msgMediaCheckDrawable, (this.layoutWidth - AndroidUtilities.dp(26.3f)) - Theme.chat_msgMediaCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(13.5f)) - Theme.chat_msgMediaCheckDrawable.getIntrinsicHeight());
                            } else {
                                BaseCell.setDrawableBounds(Theme.chat_msgMediaCheckDrawable, (this.layoutWidth - AndroidUtilities.dp(21.5f)) - Theme.chat_msgMediaCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(13.5f)) - Theme.chat_msgMediaCheckDrawable.getIntrinsicHeight());
                            }
                            Theme.chat_msgMediaCheckDrawable.setAlpha((int) (255.0f * this.timeAlpha));
                            Theme.chat_msgMediaCheckDrawable.draw(canvas);
                            Theme.chat_msgMediaCheckDrawable.setAlpha(255);
                        }
                    }
                    if (drawCheck1) {
                        if (!this.mediaBackground || this.captionLayout != null) {
                            drawable = isDrawSelectedBackground() ? Theme.chat_msgOutHalfCheckSelectedDrawable : Theme.chat_msgOutHalfCheckDrawable;
                            BaseCell.setDrawableBounds(drawable, (this.layoutWidth - AndroidUtilities.dp(18.0f)) - drawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(8.0f)) - drawable.getIntrinsicHeight());
                            drawable.draw(canvas);
                        } else if (this.currentMessageObject.type == 13 || this.currentMessageObject.type == 5) {
                            BaseCell.setDrawableBounds(Theme.chat_msgStickerHalfCheckDrawable, (this.layoutWidth - AndroidUtilities.dp(21.5f)) - Theme.chat_msgStickerHalfCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(13.5f)) - Theme.chat_msgStickerHalfCheckDrawable.getIntrinsicHeight());
                            Theme.chat_msgStickerHalfCheckDrawable.draw(canvas);
                        } else {
                            BaseCell.setDrawableBounds(Theme.chat_msgMediaHalfCheckDrawable, (this.layoutWidth - AndroidUtilities.dp(21.5f)) - Theme.chat_msgMediaHalfCheckDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(13.5f)) - Theme.chat_msgMediaHalfCheckDrawable.getIntrinsicHeight());
                            Theme.chat_msgMediaHalfCheckDrawable.setAlpha((int) (255.0f * this.timeAlpha));
                            Theme.chat_msgMediaHalfCheckDrawable.draw(canvas);
                            Theme.chat_msgMediaHalfCheckDrawable.setAlpha(255);
                        }
                    }
                } else if (drawCheck1 || drawCheck2) {
                    if (this.mediaBackground && this.captionLayout == null) {
                        BaseCell.setDrawableBounds(Theme.chat_msgBroadcastMediaDrawable, (this.layoutWidth - AndroidUtilities.dp(24.0f)) - Theme.chat_msgBroadcastMediaDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(14.0f)) - Theme.chat_msgBroadcastMediaDrawable.getIntrinsicHeight());
                        Theme.chat_msgBroadcastMediaDrawable.draw(canvas);
                    } else {
                        BaseCell.setDrawableBounds(Theme.chat_msgBroadcastDrawable, (this.layoutWidth - AndroidUtilities.dp(20.5f)) - Theme.chat_msgBroadcastDrawable.getIntrinsicWidth(), (this.layoutHeight - AndroidUtilities.dp(8.0f)) - Theme.chat_msgBroadcastDrawable.getIntrinsicHeight());
                        Theme.chat_msgBroadcastDrawable.draw(canvas);
                    }
                }
                if (drawError) {
                    if (this.mediaBackground && this.captionLayout == null) {
                        x = this.layoutWidth - AndroidUtilities.dp(34.5f);
                        y = this.layoutHeight - AndroidUtilities.dp(26.5f);
                    } else {
                        x = this.layoutWidth - AndroidUtilities.dp(32.0f);
                        y = this.layoutHeight - AndroidUtilities.dp(21.0f);
                    }
                    this.rect.set((float) x, (float) y, (float) (AndroidUtilities.dp(14.0f) + x), (float) (AndroidUtilities.dp(14.0f) + y));
                    canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(1.0f), (float) AndroidUtilities.dp(1.0f), Theme.chat_msgErrorPaint);
                    BaseCell.setDrawableBounds(Theme.chat_msgErrorDrawable, AndroidUtilities.dp(6.0f) + x, AndroidUtilities.dp(2.0f) + y);
                    Theme.chat_msgErrorDrawable.draw(canvas);
                }
            }
        }
    }

    public int getObserverTag() {
        return this.TAG;
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    public boolean isPinnedBottom() {
        return this.pinnedBottom;
    }

    public boolean isPinnedTop() {
        return this.pinnedTop;
    }

    public GroupedMessages getCurrentMessagesGroup() {
        return this.currentMessagesGroup;
    }

    public GroupedMessagePosition getCurrentPosition() {
        return this.currentPosition;
    }

    public int getLayoutHeight() {
        return this.layoutHeight;
    }
}
