package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.DownloadController.FileDownloadProgressListener;
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
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.MessageFwdHeader;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_fileLocationUnavailable;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
import org.telegram.tgnet.TLRPC.TL_messageMediaContact;
import org.telegram.tgnet.TLRPC.TL_messageMediaEmpty;
import org.telegram.tgnet.TLRPC.TL_messageMediaGame;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeo;
import org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
import org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
import org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
import org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC.TL_webPage;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.tgnet.TLRPC.WebPage;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.LinkPath;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.Components.RoundVideoPlayingDrawable;
import org.telegram.ui.Components.SeekBar;
import org.telegram.ui.Components.SeekBar.SeekBarDelegate;
import org.telegram.ui.Components.SeekBarWaveform;
import org.telegram.ui.Components.StaticLayoutEx;
import org.telegram.ui.Components.URLSpanBotCommand;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanNoUnderline;

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
    private int addedCaptionHeight;
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
    private int captionOffsetX;
    private int captionWidth;
    private int captionX;
    private int captionY;
    private AvatarDrawable contactAvatarDrawable;
    private float controlsAlpha = 1.0f;
    private int currentAccount = UserConfig.selectedAccount;
    private Drawable currentBackgroundDrawable;
    private CharSequence currentCaption;
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
    private int hasMiniProgress;
    private boolean hasNewLineForTime;
    private boolean hasOldCaptionPreview;
    private int highlightProgress;
    private boolean imagePressed;
    private boolean inLayout;
    private StaticLayout infoLayout;
    private int infoWidth;
    private boolean instantButtonPressed;
    private boolean instantPressed;
    private int instantTextLeftX;
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
    private int miniButtonPressed;
    private int miniButtonState;
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
    private boolean siteNameRtl;
    private int siteNameWidth;
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
    private int unmovedTextX;
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

    class AnonymousClass2 extends Drawable {
        RectF rect = new RectF();
        final /* synthetic */ Paint val$maskPaint;

        AnonymousClass2(Paint paint) {
            this.val$maskPaint = paint;
        }

        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            this.rect.set((float) bounds.left, (float) bounds.top, (float) bounds.right, (float) bounds.bottom);
            canvas.drawRoundRect(this.rect, (float) AndroidUtilities.dp(6.0f), (float) AndroidUtilities.dp(6.0f), this.val$maskPaint);
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public int getOpacity() {
            return -1;
        }
    }

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

    private boolean checkBotButtonMotionEvent(android.view.MotionEvent r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ChatMessageCell.checkBotButtonMotionEvent(android.view.MotionEvent):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r9.botButtons;
        r0 = r0.isEmpty();
        r1 = 0;
        if (r0 != 0) goto L_0x00bb;
    L_0x0009:
        r0 = r9.currentMessageObject;
        r2 = r0.eventId;
        r4 = 0;
        r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r0 == 0) goto L_0x0015;
    L_0x0013:
        goto L_0x00bb;
    L_0x0015:
        r0 = r10.getX();
        r0 = (int) r0;
        r2 = r10.getY();
        r2 = (int) r2;
        r3 = 0;
        r4 = r10.getAction();
        if (r4 != 0) goto L_0x0093;
    L_0x0026:
        r4 = r9.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x003d;
    L_0x002e:
        r4 = r9.getMeasuredWidth();
        r5 = r9.widthForButtons;
        r4 = r4 - r5;
        r5 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        goto L_0x004d;
    L_0x003d:
        r4 = r9.backgroundDrawableLeft;
        r5 = r9.mediaBackground;
        if (r5 == 0) goto L_0x0046;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x0048;
        r5 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 + r5;
        r5 = r9.botButtons;
        r5 = r5.size();
        if (r1 >= r5) goto L_0x0092;
        r5 = r9.botButtons;
        r5 = r5.get(r1);
        r5 = (org.telegram.ui.Cells.ChatMessageCell.BotButton) r5;
        r6 = r5.y;
        r7 = r9.layoutHeight;
        r6 = r6 + r7;
        r7 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r6 = r6 - r7;
        r7 = r5.x;
        r7 = r7 + r4;
        if (r0 < r7) goto L_0x008f;
        r7 = r5.x;
        r7 = r7 + r4;
        r8 = r5.width;
        r7 = r7 + r8;
        if (r0 > r7) goto L_0x008f;
        if (r2 < r6) goto L_0x008f;
        r7 = r5.height;
        r7 = r7 + r6;
        if (r2 > r7) goto L_0x008f;
        r9.pressedBotButton = r1;
        r9.invalidate();
        r3 = 1;
        goto L_0x0092;
        r1 = r1 + 1;
        goto L_0x004e;
        goto L_0x00ba;
    L_0x0093:
        r4 = r10.getAction();
        r5 = 1;
        if (r4 != r5) goto L_0x00ba;
        r4 = r9.pressedBotButton;
        r5 = -1;
        if (r4 == r5) goto L_0x00ba;
        r9.playSoundEffect(r1);
        r1 = r9.delegate;
        r4 = r9.botButtons;
        r6 = r9.pressedBotButton;
        r4 = r4.get(r6);
        r4 = (org.telegram.ui.Cells.ChatMessageCell.BotButton) r4;
        r4 = r4.button;
        r1.didPressedBotButton(r9, r4);
        r9.pressedBotButton = r5;
        r9.invalidate();
        return r3;
    L_0x00bb:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatMessageCell.checkBotButtonMotionEvent(android.view.MotionEvent):boolean");
    }

    private void drawContent(android.graphics.Canvas r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ChatMessageCell.drawContent(android.graphics.Canvas):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r1 = r29;
        r8 = r30;
        r2 = r1.needNewVisiblePart;
        r9 = 0;
        if (r2 == 0) goto L_0x0026;
    L_0x0009:
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        if (r2 != 0) goto L_0x0026;
    L_0x000f:
        r2 = r1.scrollRect;
        r1.getLocalVisibleRect(r2);
        r2 = r1.scrollRect;
        r2 = r2.top;
        r3 = r1.scrollRect;
        r3 = r3.bottom;
        r4 = r1.scrollRect;
        r4 = r4.top;
        r3 = r3 - r4;
        r1.setVisiblePart(r2, r3);
        r1.needNewVisiblePart = r9;
    L_0x0026:
        r2 = r1.currentMessagesGroup;
        r10 = 1;
        if (r2 == 0) goto L_0x002d;
    L_0x002b:
        r2 = r10;
        goto L_0x002e;
    L_0x002d:
        r2 = r9;
    L_0x002e:
        r1.forceNotDrawTime = r2;
        r2 = r1.photoImage;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x0040;
    L_0x0038:
        r3 = r1.currentPosition;
        if (r3 == 0) goto L_0x003e;
    L_0x003c:
        r3 = 2;
        goto L_0x0041;
    L_0x003e:
        r3 = r10;
        goto L_0x0041;
    L_0x0040:
        r3 = r9;
    L_0x0041:
        r2.setPressed(r3);
        r2 = r1.photoImage;
        r3 = r1.currentMessageObject;
        r3 = org.telegram.ui.PhotoViewer.isShowingImage(r3);
        if (r3 != 0) goto L_0x005c;
    L_0x004e:
        r3 = org.telegram.ui.SecretMediaViewer.getInstance();
        r4 = r1.currentMessageObject;
        r3 = r3.isShowingImage(r4);
        if (r3 != 0) goto L_0x005c;
    L_0x005a:
        r3 = r10;
        goto L_0x005d;
    L_0x005c:
        r3 = r9;
    L_0x005d:
        r2.setVisible(r3, r9);
        r2 = r1.photoImage;
        r2 = r2.getVisible();
        r12 = 0;
        if (r2 != 0) goto L_0x006e;
    L_0x0069:
        r1.mediaWasInvisible = r10;
        r1.timeWasInvisible = r10;
        goto L_0x0097;
    L_0x006e:
        r2 = r1.groupPhotoInvisible;
        if (r2 == 0) goto L_0x0075;
    L_0x0072:
        r1.timeWasInvisible = r10;
        goto L_0x0097;
    L_0x0075:
        r2 = r1.mediaWasInvisible;
        if (r2 != 0) goto L_0x007d;
    L_0x0079:
        r2 = r1.timeWasInvisible;
        if (r2 == 0) goto L_0x0097;
    L_0x007d:
        r2 = r1.mediaWasInvisible;
        if (r2 == 0) goto L_0x0085;
    L_0x0081:
        r1.controlsAlpha = r12;
        r1.mediaWasInvisible = r9;
    L_0x0085:
        r2 = r1.timeWasInvisible;
        if (r2 == 0) goto L_0x008d;
    L_0x0089:
        r1.timeAlpha = r12;
        r1.timeWasInvisible = r9;
    L_0x008d:
        r2 = java.lang.System.currentTimeMillis();
        r1.lastControlsAlphaChangeTime = r2;
        r2 = 0;
        r1.totalChangeTime = r2;
    L_0x0097:
        r2 = r1.radialProgress;
        r2.setHideCurrentDrawable(r9);
        r2 = r1.radialProgress;
        r3 = "chat_mediaProgress";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setProgressColor(r3);
        r13 = 0;
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r15 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r7 = 4;
        r5 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r11 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r14 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        if (r2 != 0) goto L_0x086d;
    L_0x00bb:
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x00d4;
    L_0x00c3:
        r2 = r1.currentBackgroundDrawable;
        r2 = r2.getBounds();
        r2 = r2.left;
        r19 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r2 = r2 + r19;
        r1.textX = r2;
        goto L_0x00ef;
    L_0x00d4:
        r2 = r1.currentBackgroundDrawable;
        r2 = r2.getBounds();
        r2 = r2.left;
        r6 = r1.mediaBackground;
        if (r6 != 0) goto L_0x00e6;
    L_0x00e0:
        r6 = r1.drawPinnedBottom;
        if (r6 == 0) goto L_0x00e6;
    L_0x00e4:
        r6 = r15;
        goto L_0x00e8;
    L_0x00e6:
        r6 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
    L_0x00e8:
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r2 = r2 + r6;
        r1.textX = r2;
    L_0x00ef:
        r2 = r1.hasGamePreview;
        if (r2 == 0) goto L_0x011e;
    L_0x00f3:
        r2 = r1.textX;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r2 = r2 + r6;
        r1.textX = r2;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r6 = r1.namesOffset;
        r2 = r2 + r6;
        r1.textY = r2;
        r2 = r1.siteNameLayout;
        if (r2 == 0) goto L_0x014d;
    L_0x010b:
        r2 = r1.textY;
        r6 = r1.siteNameLayout;
        r15 = r1.siteNameLayout;
        r15 = r15.getLineCount();
        r15 = r15 - r10;
        r6 = r6.getLineBottom(r15);
        r2 = r2 + r6;
        r1.textY = r2;
        goto L_0x014d;
    L_0x011e:
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x0144;
    L_0x0122:
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r6 = r1.namesOffset;
        r2 = r2 + r6;
        r1.textY = r2;
        r2 = r1.siteNameLayout;
        if (r2 == 0) goto L_0x014d;
    L_0x0131:
        r2 = r1.textY;
        r6 = r1.siteNameLayout;
        r15 = r1.siteNameLayout;
        r15 = r15.getLineCount();
        r15 = r15 - r10;
        r6 = r6.getLineBottom(r15);
        r2 = r2 + r6;
        r1.textY = r2;
        goto L_0x014d;
    L_0x0144:
        r2 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r6 = r1.namesOffset;
        r2 = r2 + r6;
        r1.textY = r2;
    L_0x014d:
        r2 = r1.textX;
        r1.unmovedTextX = r2;
        r2 = r1.currentMessageObject;
        r2 = r2.textXOffset;
        r2 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1));
        if (r2 == 0) goto L_0x018c;
    L_0x0159:
        r2 = r1.replyNameLayout;
        if (r2 == 0) goto L_0x018c;
    L_0x015d:
        r2 = r1.backgroundWidth;
        r6 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r2 = r2 - r6;
        r6 = r1.currentMessageObject;
        r6 = r6.textWidth;
        r2 = r2 - r6;
        r6 = r1.hasNewLineForTime;
        if (r6 != 0) goto L_0x0185;
    L_0x016f:
        r6 = r1.timeWidth;
        r15 = r1.currentMessageObject;
        r15 = r15.isOutOwner();
        if (r15 == 0) goto L_0x017c;
    L_0x0179:
        r15 = 20;
        goto L_0x017d;
    L_0x017c:
        r15 = r9;
    L_0x017d:
        r15 = r15 + r7;
        r15 = (float) r15;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r6 = r6 + r15;
        r2 = r2 - r6;
    L_0x0185:
        if (r2 <= 0) goto L_0x018c;
    L_0x0187:
        r6 = r1.textX;
        r6 = r6 + r2;
        r1.textX = r6;
    L_0x018c:
        r2 = r1.currentMessageObject;
        r2 = r2.textLayoutBlocks;
        if (r2 == 0) goto L_0x0251;
    L_0x0192:
        r2 = r1.currentMessageObject;
        r2 = r2.textLayoutBlocks;
        r2 = r2.isEmpty();
        if (r2 != 0) goto L_0x0251;
    L_0x019c:
        r2 = r1.fullyDraw;
        if (r2 == 0) goto L_0x01ac;
    L_0x01a0:
        r1.firstVisibleBlockNum = r9;
        r2 = r1.currentMessageObject;
        r2 = r2.textLayoutBlocks;
        r2 = r2.size();
        r1.lastVisibleBlockNum = r2;
    L_0x01ac:
        r2 = r1.firstVisibleBlockNum;
        if (r2 < 0) goto L_0x0251;
    L_0x01b0:
        r2 = r1.firstVisibleBlockNum;
    L_0x01b2:
        r6 = r1.lastVisibleBlockNum;
        if (r2 > r6) goto L_0x0251;
    L_0x01b6:
        r6 = r1.currentMessageObject;
        r6 = r6.textLayoutBlocks;
        r6 = r6.size();
        if (r2 < r6) goto L_0x01c5;
    L_0x01c1:
        r23 = r13;
        goto L_0x0253;
    L_0x01c5:
        r6 = r1.currentMessageObject;
        r6 = r6.textLayoutBlocks;
        r6 = r6.get(r2);
        r6 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r6;
        r30.save();
        r15 = r1.textX;
        r19 = r6.isRtl();
        if (r19 == 0) goto L_0x01e7;
    L_0x01da:
        r7 = r1.currentMessageObject;
        r7 = r7.textXOffset;
        r23 = r13;
        r12 = (double) r7;
        r12 = java.lang.Math.ceil(r12);
        r7 = (int) r12;
        goto L_0x01ea;
    L_0x01e7:
        r23 = r13;
        r7 = r9;
    L_0x01ea:
        r15 = r15 - r7;
        r7 = (float) r15;
        r12 = r1.textY;
        r12 = (float) r12;
        r13 = r6.textYOffset;
        r12 = r12 + r13;
        r8.translate(r7, r12);
        r7 = r1.pressedLink;
        if (r7 == 0) goto L_0x0216;
    L_0x01f9:
        r7 = r1.linkBlockNum;
        if (r2 != r7) goto L_0x0216;
    L_0x01fd:
        r7 = r9;
    L_0x01fe:
        r12 = r1.urlPath;
        r12 = r12.size();
        if (r7 >= r12) goto L_0x0216;
    L_0x0206:
        r12 = r1.urlPath;
        r12 = r12.get(r7);
        r12 = (android.graphics.Path) r12;
        r13 = org.telegram.ui.ActionBar.Theme.chat_urlPaint;
        r8.drawPath(r12, r13);
        r7 = r7 + 1;
        goto L_0x01fe;
    L_0x0216:
        r7 = r1.linkSelectionBlockNum;
        if (r2 != r7) goto L_0x023b;
    L_0x021a:
        r7 = r1.urlPathSelection;
        r7 = r7.isEmpty();
        if (r7 != 0) goto L_0x023b;
    L_0x0222:
        r7 = r9;
    L_0x0223:
        r12 = r1.urlPathSelection;
        r12 = r12.size();
        if (r7 >= r12) goto L_0x023b;
    L_0x022b:
        r12 = r1.urlPathSelection;
        r12 = r12.get(r7);
        r12 = (android.graphics.Path) r12;
        r13 = org.telegram.ui.ActionBar.Theme.chat_textSearchSelectionPaint;
        r8.drawPath(r12, r13);
        r7 = r7 + 1;
        goto L_0x0223;
    L_0x023b:
        r7 = r6.textLayout;	 Catch:{ Exception -> 0x0241 }
        r7.draw(r8);	 Catch:{ Exception -> 0x0241 }
        goto L_0x0246;
    L_0x0241:
        r0 = move-exception;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);
    L_0x0246:
        r30.restore();
        r2 = r2 + 1;
        r13 = r23;
        r7 = 4;
        r12 = 0;
        goto L_0x01b2;
    L_0x0251:
        r23 = r13;
    L_0x0253:
        r2 = r1.hasLinkPreview;
        if (r2 != 0) goto L_0x0264;
    L_0x0257:
        r2 = r1.hasGamePreview;
        if (r2 != 0) goto L_0x0264;
    L_0x025b:
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x0260;
    L_0x025f:
        goto L_0x0264;
    L_0x0260:
        r13 = r23;
        goto L_0x0869;
    L_0x0264:
        r2 = r1.hasGamePreview;
        if (r2 == 0) goto L_0x027a;
    L_0x0268:
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r6 = r1.namesOffset;
        r2 = r2 + r6;
        r6 = r1.unmovedTextX;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r6 = r6 - r7;
    L_0x0278:
        r12 = r2;
        goto L_0x02a3;
    L_0x027a:
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x028f;
    L_0x027e:
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r6 = r1.namesOffset;
        r2 = r2 + r6;
        r6 = r1.unmovedTextX;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r6 = r6 + r7;
        goto L_0x0278;
    L_0x028f:
        r2 = r1.textY;
        r6 = r1.currentMessageObject;
        r6 = r6.textHeight;
        r2 = r2 + r6;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r2 = r2 + r6;
        r6 = r1.unmovedTextX;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r6 = r6 + r7;
        goto L_0x0278;
    L_0x02a3:
        r13 = r6;
        r15 = r12;
        r19 = 0;
        r2 = r1.hasInvoicePreview;
        if (r2 != 0) goto L_0x02ee;
    L_0x02ab:
        r2 = org.telegram.ui.ActionBar.Theme.chat_replyLinePaint;
        r6 = r1.currentMessageObject;
        r6 = r6.isOutOwner();
        if (r6 == 0) goto L_0x02b8;
    L_0x02b5:
        r6 = "chat_outPreviewLine";
        goto L_0x02ba;
    L_0x02b8:
        r6 = "chat_inPreviewLine";
    L_0x02ba:
        r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
        r2.setColor(r6);
        r6 = (float) r13;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r15 - r2;
        r7 = (float) r2;
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r24 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r2 = r13 + r24;
        r2 = (float) r2;
        r4 = r1.linkPreviewHeight;
        r4 = r4 + r15;
        r24 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r4 + r24;
        r4 = (float) r4;
        r24 = org.telegram.ui.ActionBar.Theme.chat_replyLinePaint;
        r26 = r2;
        r2 = r8;
        r9 = r3;
        r3 = r6;
        r6 = r4;
        r4 = r7;
        r7 = r5;
        r5 = r26;
        r7 = r24;
        r2.drawRect(r3, r4, r5, r6, r7);
        goto L_0x02ef;
    L_0x02ee:
        r9 = r3;
    L_0x02ef:
        r2 = r1.siteNameLayout;
        if (r2 == 0) goto L_0x034a;
    L_0x02f3:
        r2 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x0300;
    L_0x02fd:
        r3 = "chat_outSiteNameText";
        goto L_0x0302;
    L_0x0300:
        r3 = "chat_inSiteNameText";
    L_0x0302:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r30.save();
        r2 = r1.siteNameRtl;
        if (r2 == 0) goto L_0x031d;
    L_0x0310:
        r2 = r1.backgroundWidth;
        r3 = r1.siteNameWidth;
        r2 = r2 - r3;
        r3 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        goto L_0x0327;
    L_0x031d:
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x0323;
    L_0x0321:
        r2 = 0;
        goto L_0x0327;
    L_0x0323:
        r2 = org.telegram.messenger.AndroidUtilities.dp(r11);
    L_0x0327:
        r3 = r13 + r2;
        r3 = (float) r3;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r15 - r4;
        r4 = (float) r4;
        r8.translate(r3, r4);
        r3 = r1.siteNameLayout;
        r3.draw(r8);
        r30.restore();
        r3 = r1.siteNameLayout;
        r4 = r1.siteNameLayout;
        r4 = r4.getLineCount();
        r4 = r4 - r10;
        r3 = r3.getLineBottom(r4);
        r15 = r15 + r3;
    L_0x034a:
        r2 = r1.hasGamePreview;
        if (r2 != 0) goto L_0x0352;
    L_0x034e:
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x036c;
    L_0x0352:
        r2 = r1.currentMessageObject;
        r2 = r2.textHeight;
        if (r2 == 0) goto L_0x036c;
    L_0x0358:
        r2 = r1.currentMessageObject;
        r2 = r2.textHeight;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r2 = r2 + r3;
        r12 = r12 + r2;
        r2 = r1.currentMessageObject;
        r2 = r2.textHeight;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r2 = r2 + r3;
        r15 = r15 + r2;
    L_0x036c:
        r2 = r1.drawPhotoImage;
        if (r2 == 0) goto L_0x03ed;
    L_0x0370:
        r2 = r1.drawInstantView;
        if (r2 == 0) goto L_0x03ed;
    L_0x0374:
        if (r15 == r12) goto L_0x037d;
    L_0x0376:
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r15 = r15 + r3;
    L_0x037d:
        r2 = r1.photoImage;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3 = r3 + r13;
        r4 = r1.photoImage;
        r4 = r4.getImageWidth();
        r5 = r1.photoImage;
        r5 = r5.getImageHeight();
        r2.setImageCoords(r3, r15, r4, r5);
        r2 = r1.drawImageButton;
        if (r2 == 0) goto L_0x03d8;
    L_0x0397:
        r2 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r3 = (float) r3;
        r4 = r1.photoImage;
        r4 = r4.getImageWidth();
        r4 = r4 - r2;
        r4 = (float) r4;
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = r4 / r5;
        r3 = r3 + r4;
        r3 = (int) r3;
        r1.buttonX = r3;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r3 = (float) r3;
        r4 = r1.photoImage;
        r4 = r4.getImageHeight();
        r4 = r4 - r2;
        r4 = (float) r4;
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = r4 / r5;
        r3 = r3 + r4;
        r3 = (int) r3;
        r1.buttonY = r3;
        r3 = r1.radialProgress;
        r4 = r1.buttonX;
        r5 = r1.buttonY;
        r6 = r1.buttonX;
        r6 = r6 + r2;
        r7 = r1.buttonY;
        r7 = r7 + r2;
        r3.setProgressRect(r4, r5, r6, r7);
    L_0x03d8:
        r2 = r1.photoImage;
        r2 = r2.draw(r8);
        r3 = r1.photoImage;
        r3 = r3.getImageHeight();
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3 = r3 + r4;
        r15 = r15 + r3;
        goto L_0x03f1;
    L_0x03ed:
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r2 = r23;
    L_0x03f1:
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x0410;
    L_0x03f9:
        r3 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;
        r4 = "chat_messageTextOut";
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
        r3 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r4 = "chat_messageTextOut";
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
        goto L_0x0426;
    L_0x0410:
        r3 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;
        r4 = "chat_messageTextIn";
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
        r3 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r4 = "chat_messageTextIn";
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
    L_0x0426:
        r3 = r1.titleLayout;
        if (r3 == 0) goto L_0x0468;
    L_0x042a:
        if (r15 == r12) goto L_0x0433;
    L_0x042c:
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r15 = r15 + r4;
    L_0x0433:
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r19 = r15 - r3;
        r30.save();
        r3 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3 = r3 + r13;
        r4 = r1.titleX;
        r3 = r3 + r4;
        r3 = (float) r3;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r15 - r4;
        r4 = (float) r4;
        r8.translate(r3, r4);
        r3 = r1.titleLayout;
        r3.draw(r8);
        r30.restore();
        r3 = r1.titleLayout;
        r4 = r1.titleLayout;
        r4 = r4.getLineCount();
        r4 = r4 - r10;
        r3 = r3.getLineBottom(r4);
        r15 = r15 + r3;
        goto L_0x046a;
    L_0x0468:
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x046a:
        r3 = r1.authorLayout;
        if (r3 == 0) goto L_0x04ad;
    L_0x046e:
        if (r15 == r12) goto L_0x0477;
    L_0x0470:
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r15 = r15 + r4;
    L_0x0477:
        if (r19 != 0) goto L_0x0481;
    L_0x0479:
        r3 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r3 = r15 - r3;
        r19 = r3;
    L_0x0481:
        r30.save();
        r3 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3 = r3 + r13;
        r4 = r1.authorX;
        r3 = r3 + r4;
        r3 = (float) r3;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r15 - r4;
        r4 = (float) r4;
        r8.translate(r3, r4);
        r3 = r1.authorLayout;
        r3.draw(r8);
        r30.restore();
        r3 = r1.authorLayout;
        r4 = r1.authorLayout;
        r4 = r4.getLineCount();
        r4 = r4 - r10;
        r3 = r3.getLineBottom(r4);
        r15 = r15 + r3;
    L_0x04ad:
        r3 = r1.descriptionLayout;
        if (r3 == 0) goto L_0x051d;
    L_0x04b1:
        if (r15 == r12) goto L_0x04ba;
    L_0x04b3:
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r15 = r15 + r4;
    L_0x04ba:
        if (r19 != 0) goto L_0x04c4;
    L_0x04bc:
        r3 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r3 = r15 - r3;
        r19 = r3;
    L_0x04c4:
        r3 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r3 = r15 - r3;
        r1.descriptionY = r3;
        r30.save();
        r3 = r1.hasInvoicePreview;
        if (r3 == 0) goto L_0x04d5;
    L_0x04d3:
        r3 = 0;
        goto L_0x04d9;
    L_0x04d5:
        r3 = org.telegram.messenger.AndroidUtilities.dp(r11);
    L_0x04d9:
        r3 = r3 + r13;
        r4 = r1.descriptionX;
        r3 = r3 + r4;
        r3 = (float) r3;
        r4 = r1.descriptionY;
        r4 = (float) r4;
        r8.translate(r3, r4);
        r3 = r1.pressedLink;
        if (r3 == 0) goto L_0x0507;
    L_0x04e8:
        r3 = r1.linkBlockNum;
        r4 = -10;
        if (r3 != r4) goto L_0x0507;
    L_0x04ee:
        r3 = 0;
    L_0x04ef:
        r4 = r1.urlPath;
        r4 = r4.size();
        if (r3 >= r4) goto L_0x0507;
    L_0x04f7:
        r4 = r1.urlPath;
        r4 = r4.get(r3);
        r4 = (android.graphics.Path) r4;
        r5 = org.telegram.ui.ActionBar.Theme.chat_urlPaint;
        r8.drawPath(r4, r5);
        r3 = r3 + 1;
        goto L_0x04ef;
    L_0x0507:
        r3 = r1.descriptionLayout;
        r3.draw(r8);
        r30.restore();
        r3 = r1.descriptionLayout;
        r4 = r1.descriptionLayout;
        r4 = r4.getLineCount();
        r4 = r4 - r10;
        r3 = r3.getLineBottom(r4);
        r15 = r15 + r3;
    L_0x051d:
        r3 = r19;
        r4 = r1.drawPhotoImage;
        if (r4 == 0) goto L_0x05e0;
    L_0x0523:
        r4 = r1.drawInstantView;
        if (r4 != 0) goto L_0x05e0;
    L_0x0527:
        if (r15 == r12) goto L_0x0530;
    L_0x0529:
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r15 = r15 + r5;
    L_0x0530:
        r4 = r1.isSmallImage;
        if (r4 == 0) goto L_0x0550;
    L_0x0534:
        r4 = r1.photoImage;
        r5 = r1.backgroundWidth;
        r5 = r5 + r13;
        r6 = 1117913088; // 0x42a20000 float:81.0 double:5.52322452E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r6 = r1.photoImage;
        r6 = r6.getImageWidth();
        r9 = r1.photoImage;
        r9 = r9.getImageHeight();
        r4.setImageCoords(r5, r3, r6, r9);
        goto L_0x05b8;
    L_0x0550:
        r4 = r1.photoImage;
        r5 = r1.hasInvoicePreview;
        if (r5 == 0) goto L_0x055f;
    L_0x0556:
        r5 = 1086953882; // 0x40c9999a float:6.3 double:5.370265717E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = -r5;
        goto L_0x0563;
    L_0x055f:
        r5 = org.telegram.messenger.AndroidUtilities.dp(r11);
    L_0x0563:
        r5 = r5 + r13;
        r6 = r1.photoImage;
        r6 = r6.getImageWidth();
        r9 = r1.photoImage;
        r9 = r9.getImageHeight();
        r4.setImageCoords(r5, r15, r6, r9);
        r4 = r1.drawImageButton;
        if (r4 == 0) goto L_0x05b8;
    L_0x0577:
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r5 = r1.photoImage;
        r5 = r5.getImageX();
        r5 = (float) r5;
        r6 = r1.photoImage;
        r6 = r6.getImageWidth();
        r6 = r6 - r4;
        r6 = (float) r6;
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = r6 / r9;
        r5 = r5 + r6;
        r5 = (int) r5;
        r1.buttonX = r5;
        r5 = r1.photoImage;
        r5 = r5.getImageY();
        r5 = (float) r5;
        r6 = r1.photoImage;
        r6 = r6.getImageHeight();
        r6 = r6 - r4;
        r6 = (float) r6;
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = r6 / r9;
        r5 = r5 + r6;
        r5 = (int) r5;
        r1.buttonY = r5;
        r5 = r1.radialProgress;
        r6 = r1.buttonX;
        r9 = r1.buttonY;
        r11 = r1.buttonX;
        r11 = r11 + r4;
        r7 = r1.buttonY;
        r7 = r7 + r4;
        r5.setProgressRect(r6, r9, r11, r7);
    L_0x05b8:
        r4 = r1.currentMessageObject;
        r4 = r4.isRoundVideo();
        if (r4 == 0) goto L_0x05da;
    L_0x05c0:
        r4 = org.telegram.messenger.MediaController.getInstance();
        r5 = r1.currentMessageObject;
        r4 = r4.isPlayingMessage(r5);
        if (r4 == 0) goto L_0x05da;
    L_0x05cc:
        r4 = org.telegram.messenger.MediaController.getInstance();
        r4 = r4.isRoundVideoDrawingReady();
        if (r4 == 0) goto L_0x05da;
    L_0x05d6:
        r2 = 1;
        r1.drawTime = r10;
        goto L_0x05e0;
    L_0x05da:
        r4 = r1.photoImage;
        r2 = r4.draw(r8);
    L_0x05e0:
        r4 = r1.photosCountLayout;
        if (r4 == 0) goto L_0x0686;
    L_0x05e4:
        r4 = r1.photoImage;
        r4 = r4.getVisible();
        if (r4 == 0) goto L_0x0686;
    L_0x05ec:
        r4 = r1.photoImage;
        r4 = r4.getImageX();
        r5 = r1.photoImage;
        r5 = r5.getImageWidth();
        r4 = r4 + r5;
        r9 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r4 - r5;
        r5 = r1.photosCountWidth;
        r4 = r4 - r5;
        r5 = r1.photoImage;
        r5 = r5.getImageY();
        r6 = r1.photoImage;
        r6 = r6.getImageHeight();
        r5 = r5 + r6;
        r6 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r6 = r1.rect;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r7 = r4 - r7;
        r7 = (float) r7;
        r11 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r11 = r5 - r11;
        r11 = (float) r11;
        r10 = r1.photosCountWidth;
        r10 = r10 + r4;
        r19 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r10 = r10 + r19;
        r10 = (float) r10;
        r9 = 1097334784; // 0x41680000 float:14.5 double:5.42155419E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = r9 + r5;
        r9 = (float) r9;
        r6.set(r7, r11, r10, r9);
        r6 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r6 = r6.getAlpha();
        r7 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r9 = (float) r6;
        r10 = r1.controlsAlpha;
        r9 = r9 * r10;
        r9 = (int) r9;
        r7.setAlpha(r9);
        r7 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r9 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r10 = r1.controlsAlpha;
        r9 = r9 * r10;
        r9 = (int) r9;
        r7.setAlpha(r9);
        r7 = r1.rect;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r9 = (float) r9;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r10 = (float) r10;
        r11 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r8.drawRoundRect(r7, r9, r10, r11);
        r7 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r7.setAlpha(r6);
        r30.save();
        r7 = (float) r4;
        r9 = (float) r5;
        r8.translate(r7, r9);
        r7 = r1.photosCountLayout;
        r7.draw(r8);
        r30.restore();
        r7 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r9 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r7.setAlpha(r9);
    L_0x0686:
        r4 = r1.videoInfoLayout;
        if (r4 == 0) goto L_0x079d;
    L_0x068a:
        r4 = r1.drawPhotoImage;
        if (r4 == 0) goto L_0x0696;
    L_0x068e:
        r4 = r1.photoImage;
        r4 = r4.getVisible();
        if (r4 == 0) goto L_0x079d;
    L_0x0696:
        r4 = r1.hasGamePreview;
        if (r4 != 0) goto L_0x0702;
    L_0x069a:
        r4 = r1.hasInvoicePreview;
        if (r4 == 0) goto L_0x069f;
    L_0x069e:
        goto L_0x0702;
    L_0x069f:
        r4 = r1.photoImage;
        r4 = r4.getImageX();
        r5 = r1.photoImage;
        r5 = r5.getImageWidth();
        r4 = r4 + r5;
        r5 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r6;
        r5 = r1.durationWidth;
        r4 = r4 - r5;
        r5 = r1.photoImage;
        r5 = r5.getImageY();
        r6 = r1.photoImage;
        r6 = r6.getImageHeight();
        r5 = r5 + r6;
        r6 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r6 = r1.rect;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r7 = r4 - r7;
        r7 = (float) r7;
        r9 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = r5 - r9;
        r9 = (float) r9;
        r10 = r1.durationWidth;
        r10 = r10 + r4;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r10 = r10 + r11;
        r10 = (float) r10;
        r11 = 1097334784; // 0x41680000 float:14.5 double:5.42155419E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r11 = r11 + r5;
        r11 = (float) r11;
        r6.set(r7, r9, r10, r11);
        r6 = r1.rect;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r7 = (float) r7;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r9 = (float) r9;
        r10 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r8.drawRoundRect(r6, r7, r9, r10);
        goto L_0x075a;
    L_0x0702:
        r4 = r1.drawPhotoImage;
        if (r4 == 0) goto L_0x0758;
    L_0x0706:
        r4 = r1.photoImage;
        r4 = r4.getImageX();
        r5 = 1091043328; // 0x41080000 float:8.5 double:5.390470265E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 + r5;
        r5 = r1.photoImage;
        r5 = r5.getImageY();
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 + r7;
        r6 = r1.rect;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r7 = r4 - r7;
        r7 = (float) r7;
        r9 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = r5 - r9;
        r9 = (float) r9;
        r10 = r1.durationWidth;
        r10 = r10 + r4;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r10 = r10 + r11;
        r10 = (float) r10;
        r11 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r11 = r11 + r5;
        r11 = (float) r11;
        r6.set(r7, r9, r10, r11);
        r6 = r1.rect;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r7 = (float) r7;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r9 = (float) r9;
        r10 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r8.drawRoundRect(r6, r7, r9, r10);
        goto L_0x075a;
    L_0x0758:
        r4 = r13;
        r5 = r15;
    L_0x075a:
        r30.save();
        r6 = (float) r4;
        r7 = (float) r5;
        r8.translate(r6, r7);
        r6 = r1.hasInvoicePreview;
        if (r6 == 0) goto L_0x0795;
    L_0x0766:
        r6 = r1.drawPhotoImage;
        if (r6 == 0) goto L_0x0776;
    L_0x076a:
        r6 = org.telegram.ui.ActionBar.Theme.chat_shipmentPaint;
        r7 = "chat_previewGameText";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setColor(r7);
        goto L_0x0795;
    L_0x0776:
        r6 = r1.currentMessageObject;
        r6 = r6.isOutOwner();
        if (r6 == 0) goto L_0x078a;
    L_0x077e:
        r6 = org.telegram.ui.ActionBar.Theme.chat_shipmentPaint;
        r7 = "chat_messageTextOut";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setColor(r7);
        goto L_0x0795;
    L_0x078a:
        r6 = org.telegram.ui.ActionBar.Theme.chat_shipmentPaint;
        r7 = "chat_messageTextIn";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setColor(r7);
    L_0x0795:
        r6 = r1.videoInfoLayout;
        r6.draw(r8);
        r30.restore();
    L_0x079d:
        r4 = r1.drawInstantView;
        if (r4 == 0) goto L_0x0868;
    L_0x07a1:
        r4 = r1.linkPreviewHeight;
        r4 = r4 + r12;
        r5 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 + r6;
        r5 = org.telegram.ui.ActionBar.Theme.chat_instantViewRectPaint;
        r6 = r1.currentMessageObject;
        r6 = r6.isOutOwner();
        if (r6 == 0) goto L_0x07cc;
    L_0x07b5:
        r6 = org.telegram.ui.ActionBar.Theme.chat_msgOutInstantDrawable;
        r7 = org.telegram.ui.ActionBar.Theme.chat_instantViewPaint;
        r9 = "chat_outPreviewInstantText";
        r9 = org.telegram.ui.ActionBar.Theme.getColor(r9);
        r7.setColor(r9);
        r7 = "chat_outPreviewInstantText";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r5.setColor(r7);
        goto L_0x07e2;
    L_0x07cc:
        r6 = org.telegram.ui.ActionBar.Theme.chat_msgInInstantDrawable;
        r7 = org.telegram.ui.ActionBar.Theme.chat_instantViewPaint;
        r9 = "chat_inPreviewInstantText";
        r9 = org.telegram.ui.ActionBar.Theme.getColor(r9);
        r7.setColor(r9);
        r7 = "chat_inPreviewInstantText";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r5.setColor(r7);
    L_0x07e2:
        r7 = android.os.Build.VERSION.SDK_INT;
        r9 = 21;
        if (r7 < r9) goto L_0x07fc;
    L_0x07e8:
        r7 = r1.instantViewSelectorDrawable;
        r9 = r1.instantWidth;
        r9 = r9 + r13;
        r10 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r10 = r10 + r4;
        r7.setBounds(r13, r4, r9, r10);
        r7 = r1.instantViewSelectorDrawable;
        r7.draw(r8);
    L_0x07fc:
        r7 = r1.rect;
        r9 = (float) r13;
        r10 = (float) r4;
        r11 = r1.instantWidth;
        r11 = r11 + r13;
        r11 = (float) r11;
        r14 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r14 = r14 + r4;
        r14 = (float) r14;
        r7.set(r9, r10, r11, r14);
        r7 = r1.rect;
        r9 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r10 = (float) r10;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = (float) r11;
        r8.drawRoundRect(r7, r10, r9, r5);
        r7 = r1.drawInstantViewType;
        if (r7 != 0) goto L_0x084a;
    L_0x0824:
        r7 = r1.instantTextLeftX;
        r9 = r1.instantTextX;
        r7 = r7 + r9;
        r7 = r7 + r13;
        r9 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r7 = r7 - r9;
        r9 = 1094189056; // 0x41380000 float:11.5 double:5.406012226E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = r9 + r4;
        r10 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r11 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r6, r7, r9, r10, r11);
        r6.draw(r8);
    L_0x084a:
        r7 = r1.instantViewLayout;
        if (r7 == 0) goto L_0x0868;
    L_0x084e:
        r30.save();
        r7 = r1.instantTextX;
        r7 = r7 + r13;
        r7 = (float) r7;
        r9 = 1093140480; // 0x41280000 float:10.5 double:5.40083157E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = r9 + r4;
        r9 = (float) r9;
        r8.translate(r7, r9);
        r7 = r1.instantViewLayout;
        r7.draw(r8);
        r30.restore();
    L_0x0868:
        r13 = r2;
    L_0x0869:
        r2 = 1;
        r1.drawTime = r2;
        goto L_0x0895;
    L_0x086d:
        r23 = r13;
        r2 = r1.drawPhotoImage;
        if (r2 == 0) goto L_0x0967;
    L_0x0873:
        r2 = r1.currentMessageObject;
        r2 = r2.isRoundVideo();
        if (r2 == 0) goto L_0x0899;
    L_0x087b:
        r2 = org.telegram.messenger.MediaController.getInstance();
        r3 = r1.currentMessageObject;
        r2 = r2.isPlayingMessage(r3);
        if (r2 == 0) goto L_0x0899;
    L_0x0887:
        r2 = org.telegram.messenger.MediaController.getInstance();
        r2 = r2.isRoundVideoDrawingReady();
        if (r2 == 0) goto L_0x0899;
    L_0x0891:
        r13 = 1;
        r2 = 1;
        r1.drawTime = r2;
    L_0x0895:
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        goto L_0x096b;
    L_0x0899:
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 5;
        if (r2 != r3) goto L_0x08ea;
    L_0x08a0:
        r2 = org.telegram.ui.ActionBar.Theme.chat_roundVideoShadow;
        if (r2 == 0) goto L_0x08ea;
    L_0x08a4:
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r4;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r5;
        r4 = org.telegram.ui.ActionBar.Theme.chat_roundVideoShadow;
        r5 = r1.photoImage;
        r5 = r5.getCurrentAlpha();
        r6 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r5 = r5 * r6;
        r5 = (int) r5;
        r4.setAlpha(r5);
        r4 = org.telegram.ui.ActionBar.Theme.chat_roundVideoShadow;
        r5 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r5 = r5 + r2;
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = r5 + r6;
        r6 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r6 = r6 + r3;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r6 = r6 + r9;
        r4.setBounds(r2, r3, r5, r6);
        r4 = org.telegram.ui.ActionBar.Theme.chat_roundVideoShadow;
        r4.draw(r8);
        goto L_0x08ec;
    L_0x08ea:
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
    L_0x08ec:
        r2 = r1.photoImage;
        r13 = r2.draw(r8);
        r2 = r1.drawTime;
        r3 = r1.photoImage;
        r3 = r3.getVisible();
        r1.drawTime = r3;
        r3 = r1.currentPosition;
        if (r3 == 0) goto L_0x096b;
    L_0x0900:
        r3 = r1.drawTime;
        if (r2 == r3) goto L_0x096b;
    L_0x0904:
        r3 = r29.getParent();
        r3 = (android.view.ViewGroup) r3;
        if (r3 == 0) goto L_0x096b;
    L_0x090c:
        r4 = r1.currentPosition;
        r4 = r4.last;
        if (r4 != 0) goto L_0x0963;
    L_0x0912:
        r4 = r3.getChildCount();
        r5 = 0;
    L_0x0917:
        if (r5 >= r4) goto L_0x0962;
    L_0x0919:
        r6 = r3.getChildAt(r5);
        if (r6 == r1) goto L_0x095f;
    L_0x091f:
        r9 = r6 instanceof org.telegram.ui.Cells.ChatMessageCell;
        if (r9 != 0) goto L_0x0924;
    L_0x0923:
        goto L_0x095f;
    L_0x0924:
        r9 = r6;
        r9 = (org.telegram.ui.Cells.ChatMessageCell) r9;
        r10 = r9.getCurrentMessagesGroup();
        r11 = r1.currentMessagesGroup;
        if (r10 != r11) goto L_0x095f;
    L_0x092f:
        r10 = r9.getCurrentPosition();
        r11 = r10.last;
        if (r11 == 0) goto L_0x095f;
    L_0x0937:
        r11 = r10.maxY;
        r12 = r1.currentPosition;
        r12 = r12.maxY;
        if (r11 != r12) goto L_0x095f;
    L_0x093f:
        r11 = r9.timeX;
        r12 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r11 = r11 - r14;
        r12 = r9.getLeft();
        r11 = r11 + r12;
        r12 = r29.getRight();
        if (r11 >= r12) goto L_0x095f;
    L_0x0953:
        r11 = r1.drawTime;
        r12 = 1;
        r11 = r11 ^ r12;
        r9.groupPhotoInvisible = r11;
        r9.invalidate();
        r3.invalidate();
    L_0x095f:
        r5 = r5 + 1;
        goto L_0x0917;
    L_0x0962:
        goto L_0x096b;
    L_0x0963:
        r3.invalidate();
        goto L_0x096b;
    L_0x0967:
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r13 = r23;
    L_0x096b:
        r2 = r1.buttonState;
        r3 = -1;
        if (r2 != r3) goto L_0x0a68;
    L_0x0970:
        r2 = r1.currentMessageObject;
        r2 = r2.needDrawBluredPreview();
        if (r2 == 0) goto L_0x0a68;
    L_0x0978:
        r2 = org.telegram.messenger.MediaController.getInstance();
        r3 = r1.currentMessageObject;
        r2 = r2.isPlayingMessage(r3);
        if (r2 != 0) goto L_0x0a68;
    L_0x0984:
        r2 = r1.photoImage;
        r2 = r2.getVisible();
        if (r2 == 0) goto L_0x0a68;
    L_0x098c:
        r2 = 4;
        r3 = r1.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.destroyTime;
        if (r3 == 0) goto L_0x09a0;
    L_0x0995:
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x099f;
    L_0x099d:
        r2 = 6;
        goto L_0x09a0;
    L_0x099f:
        r2 = 5;
    L_0x09a0:
        r9 = r2;
        r2 = org.telegram.ui.ActionBar.Theme.chat_photoStatesDrawables;
        r2 = r2[r9];
        r3 = r1.buttonPressed;
        r2 = r2[r3];
        r3 = r1.buttonX;
        r4 = r1.buttonY;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r2, r3, r4);
        r2 = org.telegram.ui.ActionBar.Theme.chat_photoStatesDrawables;
        r2 = r2[r9];
        r3 = r1.buttonPressed;
        r2 = r2[r3];
        r3 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r4 = r1.radialProgress;
        r4 = r4.getAlpha();
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r4 = r6 - r4;
        r3 = r3 * r4;
        r4 = r1.controlsAlpha;
        r3 = r3 * r4;
        r3 = (int) r3;
        r2.setAlpha(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_photoStatesDrawables;
        r2 = r2[r9];
        r3 = r1.buttonPressed;
        r2 = r2[r3];
        r2.draw(r8);
        r2 = r1.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.destroyTime;
        if (r2 == 0) goto L_0x0a68;
    L_0x09df:
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 != 0) goto L_0x0a61;
    L_0x09e7:
        r2 = java.lang.System.currentTimeMillis();
        r4 = r1.currentAccount;
        r4 = org.telegram.tgnet.ConnectionsManager.getInstance(r4);
        r4 = r4.getTimeDifference();
        r4 = r4 * 1000;
        r4 = (long) r4;
        r10 = r2 + r4;
        r2 = 0;
        r4 = r1.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.destroyTime;
        r4 = (long) r4;
        r14 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r4 = r4 * r14;
        r14 = r4 - r10;
        r2 = java.lang.Math.max(r2, r14);
        r2 = (float) r2;
        r3 = r1.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.ttl;
        r3 = (float) r3;
        r4 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r3 = r3 * r4;
        r12 = r2 / r3;
        r2 = org.telegram.ui.ActionBar.Theme.chat_deleteProgressPaint;
        r3 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r4 = r1.controlsAlpha;
        r3 = r3 * r4;
        r3 = (int) r3;
        r2.setAlpha(r3);
        r3 = r1.deleteProgressRect;
        r4 = -1028390912; // 0xffffffffc2b40000 float:-90.0 double:NaN;
        r2 = -1011613696; // 0xffffffffc3b40000 float:-360.0 double:NaN;
        r5 = r2 * r12;
        r14 = 1;
        r15 = org.telegram.ui.ActionBar.Theme.chat_deleteProgressPaint;
        r2 = r8;
        r6 = r14;
        r14 = r7;
        r7 = r15;
        r2.drawArc(r3, r4, r5, r6, r7);
        r2 = 0;
        r3 = (r12 > r2 ? 1 : (r12 == r2 ? 0 : -1));
        if (r3 == 0) goto L_0x0a62;
    L_0x0a3b:
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r2 = r1.deleteProgressRect;
        r2 = r2.left;
        r2 = (int) r2;
        r2 = r2 - r3;
        r4 = r1.deleteProgressRect;
        r4 = r4.top;
        r4 = (int) r4;
        r4 = r4 - r3;
        r5 = r1.deleteProgressRect;
        r5 = r5.right;
        r5 = (int) r5;
        r6 = r3 * 2;
        r5 = r5 + r6;
        r6 = r1.deleteProgressRect;
        r6 = r6.bottom;
        r6 = (int) r6;
        r7 = r3 * 2;
        r6 = r6 + r7;
        r1.invalidate(r2, r4, r5, r6);
        goto L_0x0a62;
    L_0x0a61:
        r14 = r7;
    L_0x0a62:
        r2 = r1.currentMessageObject;
        r1.updateSecretTimeText(r2);
        goto L_0x0a69;
    L_0x0a68:
        r14 = r7;
    L_0x0a69:
        r2 = r1.documentAttachType;
        r9 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r3 = 2;
        if (r2 == r3) goto L_0x0df6;
    L_0x0a70:
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 8;
        if (r2 != r3) goto L_0x0a7a;
    L_0x0a78:
        goto L_0x0df6;
    L_0x0a7a:
        r2 = r1.documentAttachType;
        r3 = 7;
        if (r2 == r3) goto L_0x0cd4;
    L_0x0a7f:
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 5;
        if (r2 != r3) goto L_0x0a88;
    L_0x0a86:
        goto L_0x0cd4;
    L_0x0a88:
        r2 = r1.documentAttachType;
        r3 = 5;
        if (r2 != r3) goto L_0x0bd1;
    L_0x0a8d:
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0ad0;
    L_0x0a95:
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioTitlePaint;
        r3 = "chat_outAudioTitleText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioPerformerPaint;
        r3 = "chat_outAudioPerfomerText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioTimePaint;
        r3 = "chat_outAudioDurationText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = r1.radialProgress;
        r3 = r29.isDrawSelectedBackground();
        if (r3 != 0) goto L_0x0ac6;
    L_0x0abe:
        r3 = r1.buttonPressed;
        if (r3 == 0) goto L_0x0ac3;
    L_0x0ac2:
        goto L_0x0ac6;
    L_0x0ac3:
        r3 = "chat_outAudioProgress";
        goto L_0x0ac8;
    L_0x0ac6:
        r3 = "chat_outAudioSelectedProgress";
    L_0x0ac8:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setProgressColor(r3);
        goto L_0x0b0a;
    L_0x0ad0:
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioTitlePaint;
        r3 = "chat_inAudioTitleText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioPerformerPaint;
        r3 = "chat_inAudioPerfomerText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioTimePaint;
        r3 = "chat_inAudioDurationText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = r1.radialProgress;
        r3 = r29.isDrawSelectedBackground();
        if (r3 != 0) goto L_0x0b01;
    L_0x0af9:
        r3 = r1.buttonPressed;
        if (r3 == 0) goto L_0x0afe;
    L_0x0afd:
        goto L_0x0b01;
    L_0x0afe:
        r3 = "chat_inAudioProgress";
        goto L_0x0b03;
    L_0x0b01:
        r3 = "chat_inAudioSelectedProgress";
    L_0x0b03:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setProgressColor(r3);
    L_0x0b0a:
        r2 = r1.radialProgress;
        r2.draw(r8);
        r30.save();
        r2 = r1.timeAudioX;
        r3 = r1.songX;
        r2 = r2 + r3;
        r2 = (float) r2;
        r3 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r4 = r1.mediaOffsetY;
        r3 = r3 + r4;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.songLayout;
        r2.draw(r8);
        r30.restore();
        r30.save();
        r2 = org.telegram.messenger.MediaController.getInstance();
        r3 = r1.currentMessageObject;
        r2 = r2.isPlayingMessage(r3);
        if (r2 == 0) goto L_0x0b4e;
    L_0x0b3f:
        r2 = r1.seekBarX;
        r2 = (float) r2;
        r3 = r1.seekBarY;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.seekBar;
        r2.draw(r8);
        goto L_0x0b69;
    L_0x0b4e:
        r2 = r1.timeAudioX;
        r3 = r1.performerX;
        r2 = r2 + r3;
        r2 = (float) r2;
        r3 = 1108082688; // 0x420c0000 float:35.0 double:5.47465589E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r4 = r1.mediaOffsetY;
        r3 = r3 + r4;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.performerLayout;
        r2.draw(r8);
    L_0x0b69:
        r30.restore();
        r30.save();
        r2 = r1.timeAudioX;
        r2 = (float) r2;
        r3 = 1113849856; // 0x42640000 float:57.0 double:5.503149485E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r4 = r1.mediaOffsetY;
        r3 = r3 + r4;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.durationLayout;
        r2.draw(r8);
        r30.restore();
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0b9e;
    L_0x0b92:
        r2 = r29.isDrawSelectedBackground();
        if (r2 == 0) goto L_0x0b9b;
    L_0x0b98:
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgOutMenuSelectedDrawable;
        goto L_0x0b9d;
    L_0x0b9b:
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgOutMenuDrawable;
    L_0x0b9d:
        goto L_0x0ba9;
    L_0x0b9e:
        r2 = r29.isDrawSelectedBackground();
        if (r2 == 0) goto L_0x0ba7;
    L_0x0ba4:
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgInMenuSelectedDrawable;
        goto L_0x0ba9;
    L_0x0ba7:
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgInMenuDrawable;
    L_0x0ba9:
        r3 = r1.buttonX;
        r4 = r1.backgroundWidth;
        r3 = r3 + r4;
        r4 = r1.currentMessageObject;
        r4 = r4.type;
        if (r4 != 0) goto L_0x0bb7;
    L_0x0bb4:
        r4 = 1114112000; // 0x42680000 float:58.0 double:5.50444465E-315;
        goto L_0x0bb9;
    L_0x0bb7:
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
    L_0x0bb9:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r1.otherX = r3;
        r4 = r1.buttonY;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r4 - r5;
        r1.otherY = r4;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r2, r3, r4);
        r2.draw(r8);
        goto L_0x0e56;
    L_0x0bd1:
        r2 = r1.documentAttachType;
        r3 = 3;
        if (r2 != r3) goto L_0x0e56;
    L_0x0bd6:
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0c0c;
    L_0x0bde:
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioTimePaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x0be9;
    L_0x0be6:
        r3 = "chat_outAudioDurationSelectedText";
        goto L_0x0beb;
    L_0x0be9:
        r3 = "chat_outAudioDurationText";
    L_0x0beb:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = r1.radialProgress;
        r3 = r29.isDrawSelectedBackground();
        if (r3 != 0) goto L_0x0c02;
    L_0x0bfa:
        r3 = r1.buttonPressed;
        if (r3 == 0) goto L_0x0bff;
    L_0x0bfe:
        goto L_0x0c02;
    L_0x0bff:
        r3 = "chat_outAudioProgress";
        goto L_0x0c04;
    L_0x0c02:
        r3 = "chat_outAudioSelectedProgress";
    L_0x0c04:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setProgressColor(r3);
        goto L_0x0c39;
    L_0x0c0c:
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioTimePaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x0c17;
    L_0x0c14:
        r3 = "chat_inAudioDurationSelectedText";
        goto L_0x0c19;
    L_0x0c17:
        r3 = "chat_inAudioDurationText";
    L_0x0c19:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = r1.radialProgress;
        r3 = r29.isDrawSelectedBackground();
        if (r3 != 0) goto L_0x0c30;
    L_0x0c28:
        r3 = r1.buttonPressed;
        if (r3 == 0) goto L_0x0c2d;
    L_0x0c2c:
        goto L_0x0c30;
    L_0x0c2d:
        r3 = "chat_inAudioProgress";
        goto L_0x0c32;
    L_0x0c30:
        r3 = "chat_inAudioSelectedProgress";
    L_0x0c32:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setProgressColor(r3);
    L_0x0c39:
        r2 = r1.radialProgress;
        r2.draw(r8);
        r30.save();
        r2 = r1.useSeekBarWaweform;
        if (r2 == 0) goto L_0x0c5b;
    L_0x0c45:
        r2 = r1.seekBarX;
        r3 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r3;
        r2 = (float) r2;
        r3 = r1.seekBarY;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.seekBarWaveform;
        r2.draw(r8);
        goto L_0x0c69;
    L_0x0c5b:
        r2 = r1.seekBarX;
        r2 = (float) r2;
        r3 = r1.seekBarY;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.seekBar;
        r2.draw(r8);
    L_0x0c69:
        r30.restore();
        r30.save();
        r2 = r1.timeAudioX;
        r2 = (float) r2;
        r3 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r4 = r1.mediaOffsetY;
        r3 = r3 + r4;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.durationLayout;
        r2.draw(r8);
        r30.restore();
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        if (r2 == 0) goto L_0x0e56;
    L_0x0c90:
        r2 = r1.currentMessageObject;
        r2 = r2.isContentUnread();
        if (r2 == 0) goto L_0x0e56;
    L_0x0c98:
        r2 = org.telegram.ui.ActionBar.Theme.chat_docBackPaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x0ca5;
    L_0x0ca2:
        r3 = "chat_outVoiceSeekbarFill";
        goto L_0x0ca7;
    L_0x0ca5:
        r3 = "chat_inVoiceSeekbarFill";
    L_0x0ca7:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = r1.timeAudioX;
        r3 = r1.timeWidthAudio;
        r2 = r2 + r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r2 = r2 + r3;
        r2 = (float) r2;
        r3 = 1112276992; // 0x424c0000 float:51.0 double:5.495378504E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r4 = r1.mediaOffsetY;
        r3 = r3 + r4;
        r3 = (float) r3;
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = (float) r5;
        r5 = org.telegram.ui.ActionBar.Theme.chat_docBackPaint;
        r8.drawCircle(r2, r3, r4, r5);
        goto L_0x0e56;
    L_0x0cd4:
        r2 = r1.durationLayout;
        if (r2 == 0) goto L_0x0e56;
    L_0x0cd8:
        r2 = org.telegram.messenger.MediaController.getInstance();
        r3 = r1.currentMessageObject;
        r2 = r2.isPlayingMessage(r3);
        if (r2 == 0) goto L_0x0cee;
    L_0x0ce4:
        r3 = r1.currentMessageObject;
        r3 = r3.type;
        r4 = 5;
        if (r3 != r4) goto L_0x0cee;
    L_0x0ceb:
        r29.drawRoundProgress(r30);
    L_0x0cee:
        r3 = r1.documentAttachType;
        r4 = 7;
        if (r3 != r4) goto L_0x0d28;
    L_0x0cf3:
        r3 = r1.backgroundDrawableLeft;
        r4 = r1.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 != 0) goto L_0x0d05;
    L_0x0cfd:
        r4 = r1.drawPinnedBottom;
        if (r4 == 0) goto L_0x0d02;
    L_0x0d01:
        goto L_0x0d05;
    L_0x0d02:
        r4 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        goto L_0x0d07;
    L_0x0d05:
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
    L_0x0d07:
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r4;
        r4 = r1.layoutHeight;
        r5 = 1086953882; // 0x40c9999a float:6.3 double:5.370265717E-315;
        r6 = r1.drawPinnedBottom;
        if (r6 == 0) goto L_0x0d17;
    L_0x0d15:
        r6 = 2;
        goto L_0x0d18;
    L_0x0d17:
        r6 = 0;
    L_0x0d18:
        r6 = (float) r6;
        r5 = r5 - r6;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = r1.timeLayout;
        r5 = r5.getHeight();
        r4 = r4 - r5;
        goto L_0x0de5;
    L_0x0d28:
        r3 = r1.backgroundDrawableLeft;
        r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r5;
        r4 = r1.layoutHeight;
        r5 = r1.drawPinnedBottom;
        if (r5 == 0) goto L_0x0d39;
    L_0x0d37:
        r5 = 2;
        goto L_0x0d3a;
    L_0x0d39:
        r5 = 0;
    L_0x0d3a:
        r5 = 28 - r5;
        r5 = (float) r5;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = r1.rect;
        r6 = (float) r3;
        r7 = (float) r4;
        r10 = r1.timeWidthAudio;
        r10 = r10 + r3;
        r11 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r10 = r10 + r11;
        r10 = (float) r10;
        r11 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r12 = r12 + r4;
        r11 = (float) r12;
        r5.set(r6, r7, r10, r11);
        r5 = r1.rect;
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r7 = (float) r7;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = (float) r10;
        r10 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r8.drawRoundRect(r5, r7, r6, r10);
        if (r2 != 0) goto L_0x0da5;
    L_0x0d71:
        r5 = r1.currentMessageObject;
        r5 = r5.isContentUnread();
        if (r5 == 0) goto L_0x0da5;
    L_0x0d79:
        r5 = org.telegram.ui.ActionBar.Theme.chat_docBackPaint;
        r6 = "chat_mediaTimeText";
        r6 = org.telegram.ui.ActionBar.Theme.getColor(r6);
        r5.setColor(r6);
        r5 = r1.timeWidthAudio;
        r5 = r5 + r3;
        r6 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 + r6;
        r5 = (float) r5;
        r6 = 1090833613; // 0x4104cccd float:8.3 double:5.389434135E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = r6 + r4;
        r6 = (float) r6;
        r7 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = (float) r10;
        r10 = org.telegram.ui.ActionBar.Theme.chat_docBackPaint;
        r8.drawCircle(r5, r6, r7, r10);
        goto L_0x0dd6;
    L_0x0da5:
        if (r2 == 0) goto L_0x0db7;
    L_0x0da7:
        r5 = org.telegram.messenger.MediaController.getInstance();
        r5 = r5.isMessagePaused();
        if (r5 != 0) goto L_0x0db7;
    L_0x0db1:
        r5 = r1.roundVideoPlayingDrawable;
        r5.start();
        goto L_0x0dbc;
    L_0x0db7:
        r5 = r1.roundVideoPlayingDrawable;
        r5.stop();
    L_0x0dbc:
        r5 = r1.roundVideoPlayingDrawable;
        r6 = r1.timeWidthAudio;
        r6 = r6 + r3;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r6 = r6 + r7;
        r7 = 1075000115; // 0x40133333 float:2.3 double:5.31120626E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = r7 + r4;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r5, r6, r7);
        r5 = r1.roundVideoPlayingDrawable;
        r5.draw(r8);
    L_0x0dd6:
        r5 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = r3 + r6;
        r5 = 1071225242; // 0x3fd9999a float:1.7 double:5.29255591E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 + r5;
    L_0x0de5:
        r30.save();
        r5 = (float) r3;
        r6 = (float) r4;
        r8.translate(r5, r6);
        r5 = r1.durationLayout;
        r5.draw(r8);
        r30.restore();
        goto L_0x0e56;
    L_0x0df6:
        r2 = r1.photoImage;
        r2 = r2.getVisible();
        if (r2 == 0) goto L_0x0e56;
    L_0x0dfe:
        r2 = r1.hasGamePreview;
        if (r2 != 0) goto L_0x0e56;
    L_0x0e02:
        r2 = r1.currentMessageObject;
        r2 = r2.needDrawBluredPreview();
        if (r2 != 0) goto L_0x0e56;
    L_0x0e0a:
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r2 = (android.graphics.drawable.BitmapDrawable) r2;
        r2 = r2.getPaint();
        r2 = r2.getAlpha();
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r4 = (float) r2;
        r5 = r1.controlsAlpha;
        r4 = r4 * r5;
        r4 = (int) r4;
        r3.setAlpha(r4);
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r4 = r1.photoImage;
        r4 = r4.getImageX();
        r5 = r1.photoImage;
        r5 = r5.getImageWidth();
        r4 = r4 + r5;
        r5 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r1.otherX = r4;
        r5 = r1.photoImage;
        r5 = r5.getImageY();
        r6 = 1090623898; // 0x4101999a float:8.1 double:5.388398005E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 + r6;
        r1.otherY = r5;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r3, r4, r5);
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r3.draw(r8);
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r3.setAlpha(r2);
    L_0x0e56:
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 1;
        if (r2 == r3) goto L_0x1325;
    L_0x0e5d:
        r2 = r1.documentAttachType;
        r10 = 4;
        if (r2 != r10) goto L_0x0e64;
    L_0x0e62:
        goto L_0x1326;
    L_0x0e64:
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        if (r2 != r10) goto L_0x1112;
    L_0x0e6a:
        r2 = r1.docTitleLayout;
        if (r2 == 0) goto L_0x110e;
    L_0x0e6e:
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0eac;
    L_0x0e76:
        r2 = r1.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r2 == 0) goto L_0x0e8c;
    L_0x0e80:
        r2 = org.telegram.ui.ActionBar.Theme.chat_locationTitlePaint;
        r3 = "chat_messageTextOut";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        goto L_0x0e97;
    L_0x0e8c:
        r2 = org.telegram.ui.ActionBar.Theme.chat_locationTitlePaint;
        r3 = "chat_outVenueNameText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
    L_0x0e97:
        r2 = org.telegram.ui.ActionBar.Theme.chat_locationAddressPaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x0ea2;
    L_0x0e9f:
        r3 = "chat_outVenueInfoSelectedText";
        goto L_0x0ea4;
    L_0x0ea2:
        r3 = "chat_outVenueInfoText";
    L_0x0ea4:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        goto L_0x0ee1;
    L_0x0eac:
        r2 = r1.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r2 == 0) goto L_0x0ec2;
    L_0x0eb6:
        r2 = org.telegram.ui.ActionBar.Theme.chat_locationTitlePaint;
        r3 = "chat_messageTextIn";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        goto L_0x0ecd;
    L_0x0ec2:
        r2 = org.telegram.ui.ActionBar.Theme.chat_locationTitlePaint;
        r3 = "chat_inVenueNameText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
    L_0x0ecd:
        r2 = org.telegram.ui.ActionBar.Theme.chat_locationAddressPaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x0ed8;
    L_0x0ed5:
        r3 = "chat_inVenueInfoSelectedText";
        goto L_0x0eda;
    L_0x0ed8:
        r3 = "chat_inVenueInfoText";
    L_0x0eda:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
    L_0x0ee1:
        r2 = r1.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r2 == 0) goto L_0x1092;
    L_0x0eeb:
        r2 = r1.photoImage;
        r2 = r2.getImageY2();
        r3 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r11 = r2 + r3;
        r2 = r1.locationExpired;
        if (r2 != 0) goto L_0x1039;
    L_0x0efd:
        r2 = 1;
        r1.forceNotDrawTime = r2;
        r2 = r1.currentAccount;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);
        r2 = r2.getCurrentTime();
        r3 = r1.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.date;
        r2 = r2 - r3;
        r2 = java.lang.Math.abs(r2);
        r2 = (float) r2;
        r3 = r1.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.media;
        r3 = r3.period;
        r3 = (float) r3;
        r2 = r2 / r3;
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r15 = r12 - r2;
        r2 = r1.rect;
        r3 = r1.photoImage;
        r3 = r3.getImageX2();
        r4 = 1110179840; // 0x422c0000 float:43.0 double:5.485017196E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r3 = (float) r3;
        r4 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r11 - r4;
        r4 = (float) r4;
        r5 = r1.photoImage;
        r5 = r5.getImageX2();
        r6 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r5 = (float) r5;
        r6 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = r6 + r11;
        r6 = (float) r6;
        r2.set(r3, r4, r5, r6);
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0f75;
    L_0x0f5e:
        r2 = org.telegram.ui.ActionBar.Theme.chat_radialProgress2Paint;
        r3 = "chat_outInstant";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_livePaint;
        r3 = "chat_outInstant";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        goto L_0x0f8b;
    L_0x0f75:
        r2 = org.telegram.ui.ActionBar.Theme.chat_radialProgress2Paint;
        r3 = "chat_inInstant";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_livePaint;
        r3 = "chat_inInstant";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
    L_0x0f8b:
        r2 = org.telegram.ui.ActionBar.Theme.chat_radialProgress2Paint;
        r3 = 50;
        r2.setAlpha(r3);
        r2 = r1.rect;
        r2 = r2.centerX();
        r3 = r1.rect;
        r3 = r3.centerY();
        r4 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = (float) r4;
        r5 = org.telegram.ui.ActionBar.Theme.chat_radialProgress2Paint;
        r8.drawCircle(r2, r3, r4, r5);
        r2 = org.telegram.ui.ActionBar.Theme.chat_radialProgress2Paint;
        r3 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r2.setAlpha(r3);
        r3 = r1.rect;
        r4 = -1028390912; // 0xffffffffc2b40000 float:-90.0 double:NaN;
        r2 = -1011613696; // 0xffffffffc3b40000 float:-360.0 double:NaN;
        r5 = r2 * r15;
        r6 = 0;
        r7 = org.telegram.ui.ActionBar.Theme.chat_radialProgress2Paint;
        r2 = r8;
        r2.drawArc(r3, r4, r5, r6, r7);
        r2 = r1.currentMessageObject;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2.period;
        r3 = r1.currentAccount;
        r3 = org.telegram.tgnet.ConnectionsManager.getInstance(r3);
        r3 = r3.getCurrentTime();
        r4 = r1.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.date;
        r3 = r3 - r4;
        r2 = r2 - r3;
        r2 = java.lang.Math.abs(r2);
        r2 = org.telegram.messenger.LocaleController.formatLocationLeftTime(r2);
        r3 = org.telegram.ui.ActionBar.Theme.chat_livePaint;
        r3 = r3.measureText(r2);
        r4 = r1.rect;
        r4 = r4.centerX();
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r6 = r3 / r5;
        r4 = r4 - r6;
        r5 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r6 = r6 + r11;
        r5 = (float) r6;
        r6 = org.telegram.ui.ActionBar.Theme.chat_livePaint;
        r8.drawText(r2, r4, r5, r6);
        r30.save();
        r4 = r1.photoImage;
        r4 = r4.getImageX();
        r5 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 + r6;
        r4 = (float) r4;
        r6 = r1.photoImage;
        r6 = r6.getImageY2();
        r7 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r6 = r6 + r7;
        r5 = (float) r6;
        r8.translate(r4, r5);
        r4 = r1.docTitleLayout;
        r4.draw(r8);
        r4 = 1102577664; // 0x41b80000 float:23.0 double:5.447457457E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = (float) r4;
        r5 = 0;
        r8.translate(r5, r4);
        r4 = r1.infoLayout;
        r4.draw(r8);
        r30.restore();
        goto L_0x103b;
    L_0x1039:
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x103b:
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = r1.photoImage;
        r3 = r3.getImageWidth();
        r4 = 2;
        r3 = r3 / r4;
        r2 = r2 + r3;
        r3 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r5 = r1.photoImage;
        r5 = r5.getImageHeight();
        r5 = r5 / r4;
        r3 = r3 + r5;
        r4 = 1108869120; // 0x42180000 float:38.0 double:5.47854138E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgAvatarLiveLocationDrawable;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r4, r2, r3);
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgAvatarLiveLocationDrawable;
        r4.draw(r8);
        r4 = r1.locationImageReceiver;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r5 = r5 + r2;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r6 = r6 + r3;
        r7 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r11 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r4.setImageCoords(r5, r6, r7, r11);
        r4 = r1.locationImageReceiver;
        r4.draw(r8);
        goto L_0x1447;
    L_0x1092:
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r30.save();
        r2 = r1.docTitleOffsetX;
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r2 = r2 + r3;
        r3 = r1.photoImage;
        r3 = r3.getImageWidth();
        r2 = r2 + r3;
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r4;
        r2 = (float) r2;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r4 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r5;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.docTitleLayout;
        r2.draw(r8);
        r30.restore();
        r2 = r1.infoLayout;
        if (r2 == 0) goto L_0x1447;
    L_0x10cc:
        r30.save();
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = r1.photoImage;
        r3 = r3.getImageWidth();
        r2 = r2 + r3;
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r4;
        r2 = (float) r2;
        r3 = r1.photoImage;
        r3 = r3.getImageY();
        r4 = r1.docTitleLayout;
        r5 = r1.docTitleLayout;
        r5 = r5.getLineCount();
        r6 = 1;
        r5 = r5 - r6;
        r4 = r4.getLineBottom(r5);
        r3 = r3 + r4;
        r4 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r4;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.infoLayout;
        r2.draw(r8);
        r30.restore();
        goto L_0x1447;
    L_0x110e:
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x1447;
    L_0x1112:
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 16;
        if (r2 != r3) goto L_0x1249;
    L_0x111c:
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x1144;
    L_0x1124:
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioTitlePaint;
        r3 = "chat_messageTextOut";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x113a;
    L_0x1137:
        r3 = "chat_outTimeSelectedText";
        goto L_0x113c;
    L_0x113a:
        r3 = "chat_outTimeText";
    L_0x113c:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        goto L_0x1163;
    L_0x1144:
        r2 = org.telegram.ui.ActionBar.Theme.chat_audioTitlePaint;
        r3 = "chat_messageTextIn";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x115a;
    L_0x1157:
        r3 = "chat_inTimeSelectedText";
        goto L_0x115c;
    L_0x115a:
        r3 = "chat_inTimeText";
    L_0x115c:
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
    L_0x1163:
        r2 = 1;
        r1.forceNotDrawTime = r2;
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x117b;
    L_0x116e:
        r2 = r1.layoutWidth;
        r3 = r1.backgroundWidth;
        r2 = r2 - r3;
        r3 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r3;
    L_0x117a:
        goto L_0x1194;
    L_0x117b:
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x118e;
    L_0x117f:
        r2 = r1.currentMessageObject;
        r2 = r2.needDrawAvatar();
        if (r2 == 0) goto L_0x118e;
    L_0x1187:
        r2 = 1116995584; // 0x42940000 float:74.0 double:5.518691446E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        goto L_0x117a;
    L_0x118e:
        r2 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
    L_0x1194:
        r1.otherX = r2;
        r3 = r1.titleLayout;
        if (r3 == 0) goto L_0x11b3;
    L_0x119a:
        r30.save();
        r3 = (float) r2;
        r4 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r5 = r1.namesOffset;
        r4 = r4 + r5;
        r4 = (float) r4;
        r8.translate(r3, r4);
        r3 = r1.titleLayout;
        r3.draw(r8);
        r30.restore();
    L_0x11b3:
        r3 = r1.docTitleLayout;
        if (r3 == 0) goto L_0x11d7;
    L_0x11b7:
        r30.save();
        r3 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r3 = r3 + r2;
        r3 = (float) r3;
        r4 = 1108606976; // 0x42140000 float:37.0 double:5.477246216E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r5 = r1.namesOffset;
        r4 = r4 + r5;
        r4 = (float) r4;
        r8.translate(r3, r4);
        r3 = r1.docTitleLayout;
        r3.draw(r8);
        r30.restore();
    L_0x11d7:
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x11f2;
    L_0x11df:
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgCallUpGreenDrawable;
        r4 = r29.isDrawSelectedBackground();
        if (r4 != 0) goto L_0x11ef;
    L_0x11e7:
        r4 = r1.otherPressed;
        if (r4 == 0) goto L_0x11ec;
    L_0x11eb:
        goto L_0x11ef;
    L_0x11ec:
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgOutCallDrawable;
        goto L_0x11f1;
    L_0x11ef:
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgOutCallSelectedDrawable;
    L_0x11f1:
        goto L_0x121b;
    L_0x11f2:
        r3 = r1.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.action;
        r3 = r3.reason;
        r4 = r3 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
        if (r4 != 0) goto L_0x1206;
    L_0x11fe:
        r4 = r3 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
        if (r4 == 0) goto L_0x1203;
    L_0x1202:
        goto L_0x1206;
    L_0x1203:
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgCallDownGreenDrawable;
        goto L_0x1208;
    L_0x1206:
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgCallDownRedDrawable;
        r5 = r29.isDrawSelectedBackground();
        if (r5 != 0) goto L_0x1217;
        r5 = r1.otherPressed;
        if (r5 == 0) goto L_0x1214;
        goto L_0x1217;
        r5 = org.telegram.ui.ActionBar.Theme.chat_msgInCallDrawable;
        goto L_0x1219;
        r5 = org.telegram.ui.ActionBar.Theme.chat_msgInCallSelectedDrawable;
        r3 = r4;
        r4 = r5;
    L_0x121b:
        r5 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = r2 - r6;
        r6 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r7 = r1.namesOffset;
        r6 = r6 + r7;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r3, r5, r6);
        r3.draw(r8);
        r5 = 1129119744; // 0x434d0000 float:205.0 double:5.578592756E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = r5 + r2;
        r6 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r1.otherY = r6;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r4, r5, r6);
        r4.draw(r8);
        goto L_0x1447;
    L_0x1249:
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 12;
        if (r2 != r3) goto L_0x1447;
        r2 = org.telegram.ui.ActionBar.Theme.chat_contactNamePaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x125e;
        r3 = "chat_outContactNameText";
        goto L_0x1260;
        r3 = "chat_inContactNameText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x1274;
        r3 = "chat_outContactPhoneText";
        goto L_0x1276;
        r3 = "chat_inContactPhoneText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = r1.titleLayout;
        if (r2 == 0) goto L_0x12ae;
        r30.save();
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = r1.photoImage;
        r3 = r3.getImageWidth();
        r2 = r2 + r3;
        r3 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r3;
        r2 = (float) r2;
        r3 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.titleLayout;
        r2.draw(r8);
        r30.restore();
        r2 = r1.docTitleLayout;
        if (r2 == 0) goto L_0x12df;
        r30.save();
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = r1.photoImage;
        r3 = r3.getImageWidth();
        r2 = r2 + r3;
        r3 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r3;
        r2 = (float) r2;
        r3 = 1109131264; // 0x421c0000 float:39.0 double:5.479836543E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r1.docTitleLayout;
        r2.draw(r8);
        r30.restore();
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x12f3;
        r2 = r29.isDrawSelectedBackground();
        if (r2 == 0) goto L_0x12f0;
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgOutMenuSelectedDrawable;
        goto L_0x12f2;
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgOutMenuDrawable;
        goto L_0x12fe;
        r2 = r29.isDrawSelectedBackground();
        if (r2 == 0) goto L_0x12fc;
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgInMenuSelectedDrawable;
        goto L_0x12fe;
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgInMenuDrawable;
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r4 = r1.backgroundWidth;
        r3 = r3 + r4;
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r1.otherX = r3;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r5 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r4 - r5;
        r1.otherY = r4;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r2, r3, r4);
        r2.draw(r8);
        goto L_0x1447;
    L_0x1325:
        r10 = 4;
    L_0x1326:
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2 = r1.photoImage;
        r2 = r2.getVisible();
        if (r2 == 0) goto L_0x1447;
        r2 = r1.currentMessageObject;
        r2 = r2.needDrawBluredPreview();
        if (r2 != 0) goto L_0x1387;
        r2 = r1.documentAttachType;
        if (r2 != r10) goto L_0x1387;
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r2 = (android.graphics.drawable.BitmapDrawable) r2;
        r2 = r2.getPaint();
        r2 = r2.getAlpha();
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r4 = (float) r2;
        r5 = r1.controlsAlpha;
        r4 = r4 * r5;
        r4 = (int) r4;
        r3.setAlpha(r4);
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r4 = r1.photoImage;
        r4 = r4.getImageX();
        r5 = r1.photoImage;
        r5 = r5.getImageWidth();
        r4 = r4 + r5;
        r5 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r1.otherX = r4;
        r5 = r1.photoImage;
        r5 = r5.getImageY();
        r6 = 1090623898; // 0x4101999a float:8.1 double:5.388398005E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 + r6;
        r1.otherY = r5;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r3, r4, r5);
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r3.draw(r8);
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgMediaMenuDrawable;
        r3.setAlpha(r2);
        r2 = r1.forceNotDrawTime;
        if (r2 != 0) goto L_0x1447;
        r2 = r1.infoLayout;
        if (r2 == 0) goto L_0x1447;
        r2 = r1.buttonState;
        r3 = 1;
        if (r2 == r3) goto L_0x13a5;
        r2 = r1.buttonState;
        if (r2 == 0) goto L_0x13a5;
        r2 = r1.buttonState;
        r3 = 3;
        if (r2 == r3) goto L_0x13a5;
        r2 = r1.currentMessageObject;
        r2 = r2.needDrawBluredPreview();
        if (r2 == 0) goto L_0x1447;
        r2 = org.telegram.ui.ActionBar.Theme.chat_infoPaint;
        r3 = "chat_mediaInfoText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r4;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r5 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r4 + r5;
        r3 = r1.rect;
        r5 = (float) r2;
        r6 = (float) r4;
        r7 = r1.infoWidth;
        r7 = r7 + r2;
        r11 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r7 = r7 + r15;
        r7 = (float) r7;
        r11 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r11 = r11 + r4;
        r11 = (float) r11;
        r3.set(r5, r6, r7, r11);
        r3 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r3 = r3.getAlpha();
        r5 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r6 = (float) r3;
        r7 = r1.controlsAlpha;
        r6 = r6 * r7;
        r6 = (int) r6;
        r5.setAlpha(r6);
        r5 = r1.rect;
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r7 = (float) r7;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = (float) r11;
        r11 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r8.drawRoundRect(r5, r7, r6, r11);
        r5 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        r5.setAlpha(r3);
        r30.save();
        r5 = r1.photoImage;
        r5 = r5.getImageX();
        r6 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 + r7;
        r5 = (float) r5;
        r6 = r1.photoImage;
        r6 = r6.getImageY();
        r7 = 1085276160; // 0x40b00000 float:5.5 double:5.36197667E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r6 = r6 + r7;
        r6 = (float) r6;
        r8.translate(r5, r6);
        r5 = org.telegram.ui.ActionBar.Theme.chat_infoPaint;
        r6 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r7 = r1.controlsAlpha;
        r6 = r6 * r7;
        r6 = (int) r6;
        r5.setAlpha(r6);
        r5 = r1.infoLayout;
        r5.draw(r8);
        r30.restore();
        r5 = org.telegram.ui.ActionBar.Theme.chat_infoPaint;
        r6 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r5.setAlpha(r6);
    L_0x1447:
        r2 = r1.captionLayout;
        if (r2 == 0) goto L_0x14f4;
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 1;
        if (r2 == r3) goto L_0x14cd;
        r2 = r1.documentAttachType;
        if (r2 == r10) goto L_0x14cd;
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 8;
        if (r2 != r3) goto L_0x1460;
        goto L_0x14cd;
        r2 = r1.hasOldCaptionPreview;
        if (r2 == 0) goto L_0x149d;
        r2 = r1.backgroundDrawableLeft;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x1471;
        r3 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        goto L_0x1473;
        r3 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r3;
        r3 = r1.captionOffsetX;
        r2 = r2 + r3;
        r1.captionX = r2;
        r2 = r1.totalHeight;
        r3 = r1.captionHeight;
        r2 = r2 - r3;
        r3 = r1.drawPinnedTop;
        if (r3 == 0) goto L_0x1489;
        r11 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        goto L_0x148b;
        r11 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r2 = r2 - r3;
        r3 = r1.linkPreviewHeight;
        r2 = r2 - r3;
        r3 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r4;
        r1.captionY = r2;
        goto L_0x14f6;
        r3 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r2 = r1.backgroundDrawableLeft;
        r4 = r1.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x14ac;
        r4 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        goto L_0x14ad;
        r4 = r3;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r2 = r2 + r4;
        r4 = r1.captionOffsetX;
        r2 = r2 + r4;
        r1.captionX = r2;
        r2 = r1.totalHeight;
        r4 = r1.captionHeight;
        r2 = r2 - r4;
        r4 = r1.drawPinnedTop;
        if (r4 == 0) goto L_0x14c3;
        r11 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        goto L_0x14c5;
        r11 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r2 = r2 - r4;
        r1.captionY = r2;
        goto L_0x14f6;
        r3 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r4 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r2 = r2 + r4;
        r4 = r1.captionOffsetX;
        r2 = r2 + r4;
        r1.captionX = r2;
        r2 = r1.photoImage;
        r2 = r2.getImageY();
        r4 = r1.photoImage;
        r4 = r4.getImageHeight();
        r2 = r2 + r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r2 = r2 + r4;
        r1.captionY = r2;
        goto L_0x14f6;
        r3 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r2 = r1.currentPosition;
        if (r2 != 0) goto L_0x14ff;
        r11 = 0;
        r1.drawCaptionLayout(r8, r11);
        goto L_0x1500;
        r11 = 0;
        r2 = r1.hasOldCaptionPreview;
        if (r2 == 0) goto L_0x1641;
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r4 = 1;
        if (r2 == r4) goto L_0x152b;
        r2 = r1.documentAttachType;
        if (r2 == r10) goto L_0x152b;
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r4 = 8;
        if (r2 != r4) goto L_0x1518;
        goto L_0x152b;
        r2 = r1.backgroundDrawableLeft;
        r4 = r1.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x1525;
        r3 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r3;
        goto L_0x1536;
        r2 = r1.photoImage;
        r2 = r2.getImageX();
        r3 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r2 = r2 + r3;
        r14 = r2;
        r2 = r1.totalHeight;
        r3 = r1.drawPinnedTop;
        if (r3 == 0) goto L_0x1540;
        r3 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        goto L_0x1542;
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r3 = r1.linkPreviewHeight;
        r2 = r2 - r3;
        r3 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r15 = r2 - r4;
        r16 = r15;
        r2 = org.telegram.ui.ActionBar.Theme.chat_replyLinePaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x1561;
        r3 = "chat_outPreviewLine";
        goto L_0x1563;
        r3 = "chat_inPreviewLine";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r3 = (float) r14;
        r2 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r2 = r16 - r4;
        r4 = (float) r2;
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r5 = r5 + r14;
        r5 = (float) r5;
        r2 = r1.linkPreviewHeight;
        r2 = r16 + r2;
        r6 = (float) r2;
        r7 = org.telegram.ui.ActionBar.Theme.chat_replyLinePaint;
        r2 = r8;
        r2.drawRect(r3, r4, r5, r6, r7);
        r2 = r1.siteNameLayout;
        if (r2 == 0) goto L_0x15e9;
        r2 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x1598;
        r3 = "chat_outSiteNameText";
        goto L_0x159a;
        r3 = "chat_inSiteNameText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r30.save();
        r2 = r1.siteNameRtl;
        if (r2 == 0) goto L_0x15b5;
        r2 = r1.backgroundWidth;
        r3 = r1.siteNameWidth;
        r2 = r2 - r3;
        r3 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        goto L_0x15c2;
        r2 = r1.hasInvoicePreview;
        if (r2 == 0) goto L_0x15bb;
        r2 = r11;
        goto L_0x15c2;
        r2 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r2 = r3;
        r3 = r14 + r2;
        r3 = (float) r3;
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r16 - r5;
        r4 = (float) r4;
        r8.translate(r3, r4);
        r3 = r1.siteNameLayout;
        r3.draw(r8);
        r30.restore();
        r3 = r1.siteNameLayout;
        r4 = r1.siteNameLayout;
        r4 = r4.getLineCount();
        r5 = 1;
        r4 = r4 - r5;
        r3 = r3.getLineBottom(r4);
        r16 = r16 + r3;
        r2 = r16;
        r3 = r1.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 == 0) goto L_0x15ff;
        r3 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r4 = "chat_messageTextOut";
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
        goto L_0x160a;
        r3 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r4 = "chat_messageTextIn";
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r3.setColor(r4);
        r3 = r1.descriptionLayout;
        if (r3 == 0) goto L_0x163d;
        if (r2 == r15) goto L_0x1617;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r4;
        r3 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r3 = r2 - r4;
        r1.descriptionY = r3;
        r30.save();
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r4 + r14;
        r3 = r1.descriptionX;
        r4 = r4 + r3;
        r3 = (float) r4;
        r4 = r1.descriptionY;
        r4 = (float) r4;
        r8.translate(r3, r4);
        r3 = r1.descriptionLayout;
        r3.draw(r8);
        r30.restore();
        r3 = 1;
        r1.drawTime = r3;
        goto L_0x1642;
        r3 = 1;
        r2 = r1.documentAttachType;
        if (r2 != r3) goto L_0x18f4;
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x168d;
        r2 = org.telegram.ui.ActionBar.Theme.chat_docNamePaint;
        r3 = "chat_outFileNameText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_infoPaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x1664;
        r3 = "chat_outFileInfoSelectedText";
        goto L_0x1666;
        r3 = "chat_outFileInfoText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_docBackPaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x1678;
        r3 = "chat_outFileBackgroundSelected";
        goto L_0x167a;
        r3 = "chat_outFileBackground";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = r29.isDrawSelectedBackground();
        if (r2 == 0) goto L_0x168a;
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgOutMenuSelectedDrawable;
        goto L_0x168c;
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgOutMenuDrawable;
        goto L_0x16cb;
        r2 = org.telegram.ui.ActionBar.Theme.chat_docNamePaint;
        r3 = "chat_inFileNameText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_infoPaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x16a3;
        r3 = "chat_inFileInfoSelectedText";
        goto L_0x16a5;
        r3 = "chat_inFileInfoText";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_docBackPaint;
        r3 = r29.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x16b7;
        r3 = "chat_inFileBackgroundSelected";
        goto L_0x16b9;
        r3 = "chat_inFileBackground";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r2.setColor(r3);
        r2 = r29.isDrawSelectedBackground();
        if (r2 == 0) goto L_0x16c9;
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgInMenuSelectedDrawable;
        goto L_0x16cb;
        r2 = org.telegram.ui.ActionBar.Theme.chat_msgInMenuDrawable;
        r3 = r1.drawPhotoImage;
        if (r3 == 0) goto L_0x1841;
        r3 = r1.currentMessageObject;
        r3 = r3.type;
        if (r3 != 0) goto L_0x16f8;
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r4 = r1.backgroundWidth;
        r3 = r3 + r4;
        r4 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r1.otherX = r3;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r5 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r4 = r4 + r5;
        r1.otherY = r4;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r2, r3, r4);
        goto L_0x171a;
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r4 = r1.backgroundWidth;
        r3 = r3 + r4;
        r4 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r1.otherX = r3;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r5 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r4 = r4 + r5;
        r1.otherY = r4;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r2, r3, r4);
        r3 = r1.photoImage;
        r3 = r3.getImageX();
        r4 = r1.photoImage;
        r4 = r4.getImageWidth();
        r3 = r3 + r4;
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r5;
        r4 = r1.photoImage;
        r4 = r4.getImageY();
        r5 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 + r6;
        r5 = r1.photoImage;
        r5 = r5.getImageY();
        r6 = r1.docTitleLayout;
        r7 = r1.docTitleLayout;
        r7 = r7.getLineCount();
        r14 = 1;
        r7 = r7 - r14;
        r6 = r6.getLineBottom(r7);
        r5 = r5 + r6;
        r6 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 + r6;
        r6 = r1.buttonState;
        if (r6 < 0) goto L_0x17b1;
        r6 = r1.buttonState;
        if (r6 >= r10) goto L_0x17b1;
        if (r13 != 0) goto L_0x17a2;
        r6 = r1.buttonState;
        r7 = r1.buttonState;
        if (r7 != 0) goto L_0x1775;
        r7 = r1.currentMessageObject;
        r7 = r7.isOutOwner();
        if (r7 == 0) goto L_0x1771;
        r7 = 7;
        goto L_0x1773;
        r7 = 10;
        r6 = r7;
        goto L_0x1788;
        r7 = r1.buttonState;
        r10 = 1;
        if (r7 != r10) goto L_0x1788;
        r7 = r1.currentMessageObject;
        r7 = r7.isOutOwner();
        if (r7 == 0) goto L_0x1785;
        r7 = 8;
        goto L_0x1787;
        r7 = 11;
        r6 = r7;
        r7 = r1.radialProgress;
        r10 = org.telegram.ui.ActionBar.Theme.chat_photoStatesDrawables;
        r10 = r10[r6];
        r14 = r29.isDrawSelectedBackground();
        if (r14 != 0) goto L_0x179b;
        r14 = r1.buttonPressed;
        if (r14 == 0) goto L_0x1799;
        goto L_0x179b;
        r14 = r11;
        goto L_0x179c;
        r14 = 1;
        r10 = r10[r14];
        r7.swapBackground(r10);
        goto L_0x17b1;
        r6 = r1.radialProgress;
        r7 = org.telegram.ui.ActionBar.Theme.chat_photoStatesDrawables;
        r10 = r1.buttonState;
        r7 = r7[r10];
        r10 = r1.buttonPressed;
        r7 = r7[r10];
        r6.swapBackground(r7);
        if (r13 != 0) goto L_0x1829;
        r6 = r1.rect;
        r7 = r1.photoImage;
        r7 = r7.getImageX();
        r7 = (float) r7;
        r10 = r1.photoImage;
        r10 = r10.getImageY();
        r10 = (float) r10;
        r14 = r1.photoImage;
        r14 = r14.getImageX();
        r15 = r1.photoImage;
        r15 = r15.getImageWidth();
        r14 = r14 + r15;
        r14 = (float) r14;
        r15 = r1.photoImage;
        r15 = r15.getImageY();
        r11 = r1.photoImage;
        r11 = r11.getImageHeight();
        r15 = r15 + r11;
        r11 = (float) r15;
        r6.set(r7, r10, r14, r11);
        r6 = r1.rect;
        r7 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r10 = (float) r10;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = (float) r11;
        r11 = org.telegram.ui.ActionBar.Theme.chat_docBackPaint;
        r8.drawRoundRect(r6, r10, r7, r11);
        r6 = r1.currentMessageObject;
        r6 = r6.isOutOwner();
        if (r6 == 0) goto L_0x1813;
        r6 = r1.radialProgress;
        r7 = r29.isDrawSelectedBackground();
        if (r7 == 0) goto L_0x1808;
        r7 = "chat_outFileProgressSelected";
        goto L_0x180a;
        r7 = "chat_outFileProgress";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setProgressColor(r7);
        goto L_0x18ba;
        r6 = r1.radialProgress;
        r7 = r29.isDrawSelectedBackground();
        if (r7 == 0) goto L_0x181e;
        r7 = "chat_inFileProgressSelected";
        goto L_0x1820;
        r7 = "chat_inFileProgress";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setProgressColor(r7);
        goto L_0x18ba;
        r6 = r1.buttonState;
        r7 = -1;
        if (r6 != r7) goto L_0x1834;
        r6 = r1.radialProgress;
        r7 = 1;
        r6.setHideCurrentDrawable(r7);
        r6 = r1.radialProgress;
        r7 = "chat_mediaProgress";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setProgressColor(r7);
        goto L_0x18ba;
        r3 = r1.buttonX;
        r4 = r1.backgroundWidth;
        r3 = r3 + r4;
        r4 = r1.currentMessageObject;
        r4 = r4.type;
        if (r4 != 0) goto L_0x184f;
        r4 = 1114112000; // 0x42680000 float:58.0 double:5.50444465E-315;
        goto L_0x1851;
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r1.otherX = r3;
        r4 = r1.buttonY;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r4 - r5;
        r1.otherY = r4;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r2, r3, r4);
        r3 = r1.buttonX;
        r4 = 1112801280; // 0x42540000 float:53.0 double:5.49796883E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r4;
        r4 = r1.buttonY;
        r5 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 + r6;
        r5 = r1.buttonY;
        r6 = 1104674816; // 0x41d80000 float:27.0 double:5.457818764E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 + r6;
        r6 = r1.currentMessageObject;
        r6 = r6.isOutOwner();
        if (r6 == 0) goto L_0x18a1;
        r6 = r1.radialProgress;
        r7 = r29.isDrawSelectedBackground();
        if (r7 != 0) goto L_0x1897;
        r7 = r1.buttonPressed;
        if (r7 == 0) goto L_0x1894;
        goto L_0x1897;
        r7 = "chat_outAudioProgress";
        goto L_0x1899;
        r7 = "chat_outAudioSelectedProgress";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setProgressColor(r7);
        goto L_0x18ba;
        r6 = r1.radialProgress;
        r7 = r29.isDrawSelectedBackground();
        if (r7 != 0) goto L_0x18b1;
        r7 = r1.buttonPressed;
        if (r7 == 0) goto L_0x18ae;
        goto L_0x18b1;
        r7 = "chat_inAudioProgress";
        goto L_0x18b3;
        r7 = "chat_inAudioSelectedProgress";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r6.setProgressColor(r7);
        r2.draw(r8);
        r6 = r1.docTitleLayout;	 Catch:{ Exception -> 0x18d5 }
        if (r6 == 0) goto L_0x18d4;	 Catch:{ Exception -> 0x18d5 }
        r30.save();	 Catch:{ Exception -> 0x18d5 }
        r6 = r1.docTitleOffsetX;	 Catch:{ Exception -> 0x18d5 }
        r6 = r6 + r3;	 Catch:{ Exception -> 0x18d5 }
        r6 = (float) r6;	 Catch:{ Exception -> 0x18d5 }
        r7 = (float) r4;	 Catch:{ Exception -> 0x18d5 }
        r8.translate(r6, r7);	 Catch:{ Exception -> 0x18d5 }
        r6 = r1.docTitleLayout;	 Catch:{ Exception -> 0x18d5 }
        r6.draw(r8);	 Catch:{ Exception -> 0x18d5 }
        r30.restore();	 Catch:{ Exception -> 0x18d5 }
        goto L_0x18da;
    L_0x18d5:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r6 = r1.infoLayout;	 Catch:{ Exception -> 0x18ef }
        if (r6 == 0) goto L_0x18ee;	 Catch:{ Exception -> 0x18ef }
        r30.save();	 Catch:{ Exception -> 0x18ef }
        r6 = (float) r3;	 Catch:{ Exception -> 0x18ef }
        r7 = (float) r5;	 Catch:{ Exception -> 0x18ef }
        r8.translate(r6, r7);	 Catch:{ Exception -> 0x18ef }
        r6 = r1.infoLayout;	 Catch:{ Exception -> 0x18ef }
        r6.draw(r8);	 Catch:{ Exception -> 0x18ef }
        r30.restore();	 Catch:{ Exception -> 0x18ef }
        goto L_0x18f4;
    L_0x18ef:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r2 = r1.drawImageButton;
        if (r2 == 0) goto L_0x1912;
        r2 = r1.photoImage;
        r2 = r2.getVisible();
        if (r2 == 0) goto L_0x1912;
        r2 = r1.controlsAlpha;
        r2 = (r2 > r12 ? 1 : (r2 == r12 ? 0 : -1));
        if (r2 == 0) goto L_0x190d;
        r2 = r1.radialProgress;
        r3 = r1.controlsAlpha;
        r2.setOverrideAlpha(r3);
        r2 = r1.radialProgress;
        r2.draw(r8);
        r2 = r1.botButtons;
        r2 = r2.isEmpty();
        if (r2 != 0) goto L_0x1bd3;
        r2 = r1.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x1931;
        r2 = r29.getMeasuredWidth();
        r3 = r1.widthForButtons;
        r2 = r2 - r3;
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        goto L_0x1940;
        r2 = r1.backgroundDrawableLeft;
        r3 = r1.mediaBackground;
        if (r3 == 0) goto L_0x1939;
        r4 = r12;
        goto L_0x193b;
        r4 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r2 = r2 + r3;
        r10 = r2;
        r2 = 0;
        r11 = r2;
        r2 = r1.botButtons;
        r2 = r2.size();
        if (r11 >= r2) goto L_0x1bd3;
        r2 = r1.botButtons;
        r2 = r2.get(r11);
        r14 = r2;
        r14 = (org.telegram.ui.Cells.ChatMessageCell.BotButton) r14;
        r2 = r14.y;
        r3 = r1.layoutHeight;
        r2 = r2 + r3;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r15 = r2 - r4;
        r2 = org.telegram.ui.ActionBar.Theme.chat_systemDrawable;
        r3 = r1.pressedBotButton;
        if (r11 != r3) goto L_0x196c;
        r3 = org.telegram.ui.ActionBar.Theme.colorPressedFilter;
        goto L_0x196e;
        r3 = org.telegram.ui.ActionBar.Theme.colorFilter;
        r2.setColorFilter(r3);
        r2 = org.telegram.ui.ActionBar.Theme.chat_systemDrawable;
        r3 = r14.x;
        r3 = r3 + r10;
        r4 = r14.x;
        r4 = r4 + r10;
        r5 = r14.width;
        r4 = r4 + r5;
        r5 = r14.height;
        r5 = r5 + r15;
        r2.setBounds(r3, r15, r4, r5);
        r2 = org.telegram.ui.ActionBar.Theme.chat_systemDrawable;
        r2.draw(r8);
        r30.save();
        r2 = r14.x;
        r2 = r2 + r10;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r2 = r2 + r3;
        r2 = (float) r2;
        r3 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r14.title;
        r5 = r14.title;
        r5 = r5.getLineCount();
        r16 = 1;
        r5 = r5 + -1;
        r4 = r4.getLineBottom(r5);
        r3 = r3 - r4;
        r17 = 2;
        r3 = r3 / 2;
        r3 = r3 + r15;
        r3 = (float) r3;
        r8.translate(r2, r3);
        r2 = r14.title;
        r2.draw(r8);
        r30.restore();
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonUrl;
        if (r2 == 0) goto L_0x1a04;
        r2 = r14.x;
        r3 = r14.width;
        r2 = r2 + r3;
        r3 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r4;
        r4 = org.telegram.ui.ActionBar.Theme.chat_botLinkDrawalbe;
        r4 = r4.getIntrinsicWidth();
        r2 = r2 - r4;
        r2 = r2 + r10;
        r4 = org.telegram.ui.ActionBar.Theme.chat_botLinkDrawalbe;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r5 = r5 + r15;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r4, r2, r5);
        r3 = org.telegram.ui.ActionBar.Theme.chat_botLinkDrawalbe;
        r3.draw(r8);
        r27 = r10;
        r6 = 0;
        r21 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r23 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        goto L_0x1bcb;
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonSwitchInline;
        if (r2 == 0) goto L_0x1a3d;
        r2 = r14.x;
        r3 = r14.width;
        r2 = r2 + r3;
        r7 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r2 = r2 - r3;
        r3 = org.telegram.ui.ActionBar.Theme.chat_botInlineDrawable;
        r3 = r3.getIntrinsicWidth();
        r2 = r2 - r3;
        r2 = r2 + r10;
        r3 = org.telegram.ui.ActionBar.Theme.chat_botInlineDrawable;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r4 = r4 + r15;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r3, r2, r4);
        r3 = org.telegram.ui.ActionBar.Theme.chat_botInlineDrawable;
        r3.draw(r8);
        r23 = r7;
        r27 = r10;
        r6 = 0;
        r21 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        goto L_0x1bcb;
        r7 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;
        if (r2 != 0) goto L_0x1a5f;
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonRequestGeoLocation;
        if (r2 != 0) goto L_0x1a5f;
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
        if (r2 != 0) goto L_0x1a5f;
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
        if (r2 == 0) goto L_0x1a34;
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;
        if (r2 != 0) goto L_0x1a77;
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonGame;
        if (r2 != 0) goto L_0x1a77;
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
        if (r2 == 0) goto L_0x1a89;
        r2 = r1.currentAccount;
        r2 = org.telegram.messenger.SendMessagesHelper.getInstance(r2);
        r3 = r1.currentMessageObject;
        r4 = r14.button;
        r2 = r2.isSendingCallback(r3, r4);
        if (r2 != 0) goto L_0x1aa6;
        r2 = r14.button;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonRequestGeoLocation;
        if (r2 == 0) goto L_0x1aa4;
        r2 = r1.currentAccount;
        r2 = org.telegram.messenger.SendMessagesHelper.getInstance(r2);
        r3 = r1.currentMessageObject;
        r4 = r14.button;
        r2 = r2.isSendingCurrentLocation(r3, r4);
        if (r2 == 0) goto L_0x1aa4;
        goto L_0x1aa6;
        r2 = 0;
        goto L_0x1aa8;
        r2 = r16;
        r19 = r2;
        if (r19 != 0) goto L_0x1ab7;
        if (r19 != 0) goto L_0x1a34;
        r2 = r14.progressAlpha;
        r3 = 0;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 == 0) goto L_0x1a34;
        r2 = org.telegram.ui.ActionBar.Theme.chat_botProgressPaint;
        r3 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r4 = r14.progressAlpha;
        r5 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r4 = r4 * r5;
        r4 = (int) r4;
        r3 = java.lang.Math.min(r3, r4);
        r2.setAlpha(r3);
        r2 = r14.x;
        r3 = r14.width;
        r2 = r2 + r3;
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r6 = r2 + r10;
        r2 = r1.rect;
        r3 = (float) r6;
        r5 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 + r15;
        r4 = (float) r4;
        r9 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r20 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r5 = r6 + r20;
        r5 = (float) r5;
        r7 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = r7 + r15;
        r7 = (float) r7;
        r2.set(r3, r4, r5, r7);
        r3 = r1.rect;
        r2 = r14.angle;
        r4 = (float) r2;
        r5 = 1130102784; // 0x435c0000 float:220.0 double:5.58344962E-315;
        r7 = 0;
        r20 = org.telegram.ui.ActionBar.Theme.chat_botProgressPaint;
        r2 = r8;
        r21 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r22 = r6;
        r6 = r7;
        r23 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r7 = r20;
        r2.drawArc(r3, r4, r5, r6, r7);
        r2 = r1.rect;
        r2 = r2.left;
        r2 = (int) r2;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r4;
        r4 = r1.rect;
        r4 = r4.top;
        r4 = (int) r4;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r4 - r5;
        r5 = r1.rect;
        r5 = r5.right;
        r5 = (int) r5;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r5 = r5 + r6;
        r6 = r1.rect;
        r6 = r6.bottom;
        r6 = (int) r6;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r6 = r6 + r7;
        r1.invalidate(r2, r4, r5, r6);
        r4 = java.lang.System.currentTimeMillis();
        r6 = r14.lastUpdateTime;
        r24 = java.lang.System.currentTimeMillis();
        r27 = r10;
        r9 = r6 - r24;
        r6 = java.lang.Math.abs(r9);
        r9 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r2 = (r6 > r9 ? 1 : (r6 == r9 ? 0 : -1));
        if (r2 >= 0) goto L_0x1bc7;
        r6 = r14.lastUpdateTime;
        r9 = r4 - r6;
        r6 = 360; // 0x168 float:5.04E-43 double:1.78E-321;
        r6 = r6 * r9;
        r2 = (float) r6;
        r6 = 1157234688; // 0x44fa0000 float:2000.0 double:5.717499035E-315;
        r2 = r2 / r6;
        r6 = r14.angle;
        r6 = (float) r6;
        r6 = r6 + r2;
        r6 = (int) r6;
        r14.angle = r6;
        r6 = r14.angle;
        r7 = 360; // 0x168 float:5.04E-43 double:1.78E-321;
        r3 = r14.angle;
        r3 = r3 / 360;
        r7 = r7 * r3;
        r6 = r6 - r7;
        r14.angle = r6;
        if (r19 == 0) goto L_0x1ba5;
        r3 = r14.progressAlpha;
        r3 = (r3 > r12 ? 1 : (r3 == r12 ? 0 : -1));
        if (r3 >= 0) goto L_0x1bc7;
        r3 = r14.progressAlpha;
        r6 = (float) r9;
        r7 = 1128792064; // 0x43480000 float:200.0 double:5.5769738E-315;
        r6 = r6 / r7;
        r3 = r3 + r6;
        r14.progressAlpha = r3;
        r3 = r14.progressAlpha;
        r3 = (r3 > r12 ? 1 : (r3 == r12 ? 0 : -1));
        if (r3 <= 0) goto L_0x1bc7;
        r14.progressAlpha = r12;
        goto L_0x1bc7;
        r3 = r14.progressAlpha;
        r6 = 0;
        r3 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));
        if (r3 <= 0) goto L_0x1bc8;
        r3 = r14.progressAlpha;
        r7 = (float) r9;
        r18 = 1128792064; // 0x43480000 float:200.0 double:5.5769738E-315;
        r7 = r7 / r18;
        r3 = r3 - r7;
        r14.progressAlpha = r3;
        r3 = r14.progressAlpha;
        r3 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));
        if (r3 >= 0) goto L_0x1bc8;
        r14.progressAlpha = r6;
        goto L_0x1bc8;
        r6 = 0;
        r14.lastUpdateTime = r4;
        r2 = r11 + 1;
        r10 = r27;
        r9 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        goto L_0x1942;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatMessageCell.drawContent(android.graphics.Canvas):void");
    }

    private void measureTime(org.telegram.messenger.MessageObject r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ChatMessageCell.measureTime(org.telegram.messenger.MessageObject):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r11.messageOwner;
        r0 = r0.post_author;
        r1 = 0;
        if (r0 == 0) goto L_0x0014;
    L_0x0007:
        r0 = r11.messageOwner;
        r0 = r0.post_author;
        r2 = "\n";
        r3 = "";
        r0 = r0.replace(r2, r3);
    L_0x0013:
        goto L_0x006d;
    L_0x0014:
        r0 = r11.messageOwner;
        r0 = r0.fwd_from;
        if (r0 == 0) goto L_0x0031;
    L_0x001a:
        r0 = r11.messageOwner;
        r0 = r0.fwd_from;
        r0 = r0.post_author;
        if (r0 == 0) goto L_0x0031;
    L_0x0022:
        r0 = r11.messageOwner;
        r0 = r0.fwd_from;
        r0 = r0.post_author;
        r2 = "\n";
        r3 = "";
        r0 = r0.replace(r2, r3);
        goto L_0x0013;
    L_0x0031:
        r0 = r11.isOutOwner();
        if (r0 != 0) goto L_0x006c;
    L_0x0037:
        r0 = r11.messageOwner;
        r0 = r0.from_id;
        if (r0 <= 0) goto L_0x006c;
    L_0x003d:
        r0 = r11.messageOwner;
        r0 = r0.post;
        if (r0 == 0) goto L_0x006c;
    L_0x0043:
        r0 = r10.currentAccount;
        r0 = org.telegram.messenger.MessagesController.getInstance(r0);
        r2 = r11.messageOwner;
        r2 = r2.from_id;
        r2 = java.lang.Integer.valueOf(r2);
        r0 = r0.getUser(r2);
        if (r0 == 0) goto L_0x0068;
    L_0x0057:
        r2 = r0.first_name;
        r3 = r0.last_name;
        r2 = org.telegram.messenger.ContactsController.formatName(r2, r3);
        r3 = 10;
        r4 = 32;
        r2 = r2.replace(r3, r4);
        goto L_0x006a;
        r2 = r1;
        r0 = r2;
        goto L_0x006d;
    L_0x006c:
        r0 = r1;
    L_0x006d:
        r2 = 0;
        r3 = r10.currentMessageObject;
        r3 = r3.isFromUser();
        if (r3 == 0) goto L_0x0088;
        r3 = r10.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r4 = r11.messageOwner;
        r4 = r4.from_id;
        r4 = java.lang.Integer.valueOf(r4);
        r2 = r3.getUser(r4);
        r3 = r11.isLiveLocation();
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r3 != 0) goto L_0x00e9;
        r6 = r11.getDialogId();
        r8 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
        r3 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r3 == 0) goto L_0x00e9;
        r3 = r11.messageOwner;
        r3 = r3.via_bot_id;
        if (r3 != 0) goto L_0x00e9;
        r3 = r11.messageOwner;
        r3 = r3.via_bot_name;
        if (r3 != 0) goto L_0x00e9;
        if (r2 == 0) goto L_0x00ad;
        r3 = r2.bot;
        if (r3 != 0) goto L_0x00e9;
        r3 = r11.messageOwner;
        r3 = r3.flags;
        r6 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
        r3 = r3 & r6;
        if (r3 == 0) goto L_0x00e9;
        r3 = r10.currentPosition;
        if (r3 != 0) goto L_0x00e9;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r6 = "EditedMessage";
        r7 = 2131493417; // 0x7f0c0229 float:1.8610314E38 double:1.0530976717E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r7);
        r3.append(r6);
        r6 = " ";
        r3.append(r6);
        r6 = org.telegram.messenger.LocaleController.getInstance();
        r6 = r6.formatterDay;
        r7 = r11.messageOwner;
        r7 = r7.date;
        r7 = (long) r7;
        r7 = r7 * r4;
        r4 = r6.format(r7);
        r3.append(r4);
        r3 = r3.toString();
        goto L_0x00f9;
        r3 = org.telegram.messenger.LocaleController.getInstance();
        r3 = r3.formatterDay;
        r6 = r11.messageOwner;
        r6 = r6.date;
        r6 = (long) r6;
        r6 = r6 * r4;
        r3 = r3.format(r6);
        if (r0 == 0) goto L_0x010f;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = ", ";
        r4.append(r5);
        r4.append(r3);
        r4 = r4.toString();
        r10.currentTimeString = r4;
        goto L_0x0111;
        r10.currentTimeString = r3;
        r4 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r5 = r10.currentTimeString;
        r4 = r4.measureText(r5);
        r4 = (double) r4;
        r4 = java.lang.Math.ceil(r4);
        r4 = (int) r4;
        r10.timeWidth = r4;
        r10.timeTextWidth = r4;
        r4 = r11.messageOwner;
        r4 = r4.flags;
        r4 = r4 & 1024;
        r5 = 0;
        if (r4 == 0) goto L_0x016a;
        r4 = "%s";
        r6 = 1;
        r7 = new java.lang.Object[r6];
        r8 = r11.messageOwner;
        r8 = r8.views;
        r6 = java.lang.Math.max(r6, r8);
        r1 = org.telegram.messenger.LocaleController.formatShortNumber(r6, r1);
        r7[r5] = r1;
        r1 = java.lang.String.format(r4, r7);
        r10.currentViewsString = r1;
        r1 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r4 = r10.currentViewsString;
        r1 = r1.measureText(r4);
        r6 = (double) r1;
        r6 = java.lang.Math.ceil(r6);
        r1 = (int) r6;
        r10.viewsTextWidth = r1;
        r1 = r10.timeWidth;
        r4 = r10.viewsTextWidth;
        r6 = org.telegram.ui.ActionBar.Theme.chat_msgInViewsDrawable;
        r6 = r6.getIntrinsicWidth();
        r4 = r4 + r6;
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r6;
        r1 = r1 + r4;
        r10.timeWidth = r1;
        if (r0 == 0) goto L_0x01d6;
        r1 = r10.availableTimeWidth;
        if (r1 != 0) goto L_0x0178;
        r1 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r10.availableTimeWidth = r1;
        r1 = r10.availableTimeWidth;
        r4 = r10.timeWidth;
        r1 = r1 - r4;
        r4 = r11.isOutOwner();
        if (r4 == 0) goto L_0x0197;
        r4 = r11.type;
        r6 = 5;
        if (r4 != r6) goto L_0x0190;
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1 = r1 - r4;
        goto L_0x0197;
        r4 = 1119879168; // 0x42c00000 float:96.0 double:5.532938244E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1 = r1 - r4;
        r4 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r6 = r0.length();
        r4 = r4.measureText(r0, r5, r6);
        r4 = (double) r4;
        r4 = java.lang.Math.ceil(r4);
        r4 = (int) r4;
        if (r4 <= r1) goto L_0x01b9;
        if (r1 > 0) goto L_0x01af;
        r0 = "";
        r4 = 0;
        goto L_0x01b9;
        r5 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r6 = (float) r1;
        r7 = android.text.TextUtils.TruncateAt.END;
        r0 = android.text.TextUtils.ellipsize(r0, r5, r6, r7);
        r4 = r1;
        r5 = new java.lang.StringBuilder;
        r5.<init>();
        r5.append(r0);
        r6 = r10.currentTimeString;
        r5.append(r6);
        r5 = r5.toString();
        r10.currentTimeString = r5;
        r5 = r10.timeTextWidth;
        r5 = r5 + r4;
        r10.timeTextWidth = r5;
        r5 = r10.timeWidth;
        r5 = r5 + r4;
        r10.timeWidth = r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatMessageCell.measureTime(org.telegram.messenger.MessageObject):void");
    }

    private void setMessageObjectInternal(org.telegram.messenger.MessageObject r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ChatMessageCell.setMessageObjectInternal(org.telegram.messenger.MessageObject):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r1 = r30;
        r2 = r31;
        r3 = r2.messageOwner;
        r3 = r3.flags;
        r3 = r3 & 1024;
        r4 = 1;
        if (r3 == 0) goto L_0x0024;
    L_0x000d:
        r3 = r1.currentMessageObject;
        r3 = r3.viewsReloaded;
        if (r3 != 0) goto L_0x0024;
    L_0x0013:
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r5 = r1.currentMessageObject;
        r5 = r5.messageOwner;
        r3.addToViewsQueue(r5);
        r3 = r1.currentMessageObject;
        r3.viewsReloaded = r4;
    L_0x0024:
        r30.updateCurrentUserAndChat();
        r3 = r1.isAvatarVisible;
        r5 = 0;
        r6 = 0;
        if (r3 == 0) goto L_0x007f;
    L_0x002d:
        r3 = r1.currentUser;
        if (r3 == 0) goto L_0x004a;
    L_0x0031:
        r3 = r1.currentUser;
        r3 = r3.photo;
        if (r3 == 0) goto L_0x0040;
    L_0x0037:
        r3 = r1.currentUser;
        r3 = r3.photo;
        r3 = r3.photo_small;
        r1.currentPhoto = r3;
        goto L_0x0042;
    L_0x0040:
        r1.currentPhoto = r5;
    L_0x0042:
        r3 = r1.avatarDrawable;
        r7 = r1.currentUser;
        r3.setInfo(r7);
        goto L_0x0072;
    L_0x004a:
        r3 = r1.currentChat;
        if (r3 == 0) goto L_0x0067;
    L_0x004e:
        r3 = r1.currentChat;
        r3 = r3.photo;
        if (r3 == 0) goto L_0x005d;
    L_0x0054:
        r3 = r1.currentChat;
        r3 = r3.photo;
        r3 = r3.photo_small;
        r1.currentPhoto = r3;
        goto L_0x005f;
    L_0x005d:
        r1.currentPhoto = r5;
    L_0x005f:
        r3 = r1.avatarDrawable;
        r7 = r1.currentChat;
        r3.setInfo(r7);
        goto L_0x0072;
    L_0x0067:
        r1.currentPhoto = r5;
        r3 = r1.avatarDrawable;
        r7 = r2.messageOwner;
        r7 = r7.from_id;
        r3.setInfo(r7, r5, r5, r6);
    L_0x0072:
        r8 = r1.avatarImage;
        r9 = r1.currentPhoto;
        r10 = "50_50";
        r11 = r1.avatarDrawable;
        r12 = 0;
        r13 = 0;
        r8.setImage(r9, r10, r11, r12, r13);
    L_0x007f:
        r30.measureTime(r31);
        r1.namesOffset = r6;
        r3 = 0;
        r7 = 0;
        r8 = r2.messageOwner;
        r8 = r8.via_bot_id;
        if (r8 == 0) goto L_0x00e2;
    L_0x008c:
        r8 = r1.currentAccount;
        r8 = org.telegram.messenger.MessagesController.getInstance(r8);
        r9 = r2.messageOwner;
        r9 = r9.via_bot_id;
        r9 = java.lang.Integer.valueOf(r9);
        r8 = r8.getUser(r9);
        if (r8 == 0) goto L_0x00e1;
    L_0x00a0:
        r9 = r8.username;
        if (r9 == 0) goto L_0x00e1;
    L_0x00a4:
        r9 = r8.username;
        r9 = r9.length();
        if (r9 <= 0) goto L_0x00e1;
    L_0x00ac:
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "@";
        r9.append(r10);
        r10 = r8.username;
        r9.append(r10);
        r3 = r9.toString();
        r9 = " via <b>%s</b>";
        r10 = new java.lang.Object[r4];
        r10[r6] = r3;
        r9 = java.lang.String.format(r9, r10);
        r7 = org.telegram.messenger.AndroidUtilities.replaceTags(r9);
        r9 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;
        r10 = r7.length();
        r9 = r9.measureText(r7, r6, r10);
        r9 = (double) r9;
        r9 = java.lang.Math.ceil(r9);
        r9 = (int) r9;
        r1.viaWidth = r9;
        r1.currentViaBotUser = r8;
    L_0x00e1:
        goto L_0x0127;
    L_0x00e2:
        r8 = r2.messageOwner;
        r8 = r8.via_bot_name;
        if (r8 == 0) goto L_0x0127;
    L_0x00e8:
        r8 = r2.messageOwner;
        r8 = r8.via_bot_name;
        r8 = r8.length();
        if (r8 <= 0) goto L_0x0127;
    L_0x00f2:
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "@";
        r8.append(r9);
        r9 = r2.messageOwner;
        r9 = r9.via_bot_name;
        r8.append(r9);
        r3 = r8.toString();
        r8 = " via <b>%s</b>";
        r9 = new java.lang.Object[r4];
        r9[r6] = r3;
        r8 = java.lang.String.format(r8, r9);
        r7 = org.telegram.messenger.AndroidUtilities.replaceTags(r8);
        r8 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;
        r9 = r7.length();
        r8 = r8.measureText(r7, r6, r9);
        r8 = (double) r8;
        r8 = java.lang.Math.ceil(r8);
        r8 = (int) r8;
        r1.viaWidth = r8;
    L_0x0127:
        r8 = r1.drawName;
        if (r8 == 0) goto L_0x0139;
    L_0x012b:
        r8 = r1.isChat;
        if (r8 == 0) goto L_0x0139;
    L_0x012f:
        r8 = r1.currentMessageObject;
        r8 = r8.isOutOwner();
        if (r8 != 0) goto L_0x0139;
    L_0x0137:
        r8 = r4;
        goto L_0x013a;
    L_0x0139:
        r8 = r6;
    L_0x013a:
        r9 = r2.messageOwner;
        r9 = r9.fwd_from;
        if (r9 == 0) goto L_0x0146;
    L_0x0140:
        r9 = r2.type;
        r10 = 14;
        if (r9 != r10) goto L_0x014a;
    L_0x0146:
        if (r3 == 0) goto L_0x014a;
    L_0x0148:
        r9 = r4;
        goto L_0x014b;
    L_0x014a:
        r9 = r6;
    L_0x014b:
        r13 = 32;
        r14 = 10;
        r12 = 5;
        r10 = 13;
        if (r8 != 0) goto L_0x0160;
    L_0x0154:
        if (r9 == 0) goto L_0x0157;
    L_0x0156:
        goto L_0x0160;
    L_0x0157:
        r1.currentNameString = r5;
        r1.nameLayout = r5;
        r1.nameWidth = r6;
        r6 = r5;
        goto L_0x036f;
    L_0x0160:
        r1.drawNameLayout = r4;
        r5 = r30.getMaxNameWidth();
        r1.nameWidth = r5;
        r5 = r1.nameWidth;
        if (r5 >= 0) goto L_0x0174;
    L_0x016c:
        r5 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r1.nameWidth = r5;
    L_0x0174:
        r5 = r1.currentUser;
        if (r5 == 0) goto L_0x01b7;
    L_0x0178:
        r5 = r1.currentMessageObject;
        r5 = r5.isOutOwner();
        if (r5 != 0) goto L_0x01b7;
    L_0x0180:
        r5 = r1.currentMessageObject;
        r5 = r5.type;
        if (r5 == r10) goto L_0x01b7;
    L_0x0186:
        r5 = r1.currentMessageObject;
        r5 = r5.type;
        if (r5 == r12) goto L_0x01b7;
    L_0x018c:
        r5 = r1.delegate;
        r15 = r1.currentUser;
        r15 = r15.id;
        r5 = r5.isChatAdminCell(r15);
        if (r5 == 0) goto L_0x01b7;
    L_0x0198:
        r5 = "ChatAdmin";
        r15 = 2131493219; // 0x7f0c0163 float:1.8609912E38 double:1.053097574E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r15);
        r15 = org.telegram.ui.ActionBar.Theme.chat_adminPaint;
        r15 = r15.measureText(r5);
        r19 = r5;
        r4 = (double) r15;
        r4 = java.lang.Math.ceil(r4);
        r4 = (int) r4;
        r5 = r1.nameWidth;
        r5 = r5 - r4;
        r1.nameWidth = r5;
        r5 = r19;
        goto L_0x01b9;
    L_0x01b7:
        r5 = 0;
        r4 = r6;
    L_0x01b9:
        if (r8 == 0) goto L_0x01d8;
    L_0x01bb:
        r15 = r1.currentUser;
        if (r15 == 0) goto L_0x01c8;
    L_0x01bf:
        r15 = r1.currentUser;
        r15 = org.telegram.messenger.UserObject.getUserName(r15);
        r1.currentNameString = r15;
        goto L_0x01dc;
    L_0x01c8:
        r15 = r1.currentChat;
        if (r15 == 0) goto L_0x01d3;
    L_0x01cc:
        r15 = r1.currentChat;
        r15 = r15.title;
        r1.currentNameString = r15;
        goto L_0x01dc;
    L_0x01d3:
        r15 = "DELETED";
        r1.currentNameString = r15;
        goto L_0x01dc;
    L_0x01d8:
        r15 = "";
        r1.currentNameString = r15;
    L_0x01dc:
        r15 = r1.currentNameString;
        r15 = r15.replace(r14, r13);
        r13 = org.telegram.ui.ActionBar.Theme.chat_namePaint;
        r14 = r1.nameWidth;
        if (r9 == 0) goto L_0x01eb;
    L_0x01e8:
        r11 = r1.viaWidth;
        goto L_0x01ec;
    L_0x01eb:
        r11 = r6;
    L_0x01ec:
        r14 = r14 - r11;
        r11 = (float) r14;
        r14 = android.text.TextUtils.TruncateAt.END;
        r11 = android.text.TextUtils.ellipsize(r15, r13, r11, r14);
        if (r9 == 0) goto L_0x02cb;
    L_0x01f6:
        r13 = org.telegram.ui.ActionBar.Theme.chat_namePaint;
        r14 = r11.length();
        r13 = r13.measureText(r11, r6, r14);
        r13 = (double) r13;
        r13 = java.lang.Math.ceil(r13);
        r13 = (int) r13;
        r1.viaNameWidth = r13;
        r13 = r1.viaNameWidth;
        if (r13 == 0) goto L_0x0217;
    L_0x020c:
        r13 = r1.viaNameWidth;
        r14 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r13 = r13 + r14;
        r1.viaNameWidth = r13;
    L_0x0217:
        r13 = r1.currentMessageObject;
        r13 = r13.type;
        if (r13 == r10) goto L_0x0236;
    L_0x021d:
        r13 = r1.currentMessageObject;
        r13 = r13.type;
        if (r13 != r12) goto L_0x0224;
    L_0x0223:
        goto L_0x0236;
    L_0x0224:
        r13 = r1.currentMessageObject;
        r13 = r13.isOutOwner();
        if (r13 == 0) goto L_0x022f;
    L_0x022c:
        r13 = "chat_outViaBotNameText";
        goto L_0x0231;
    L_0x022f:
        r13 = "chat_inViaBotNameText";
    L_0x0231:
        r13 = org.telegram.ui.ActionBar.Theme.getColor(r13);
        goto L_0x023c;
    L_0x0236:
        r13 = "chat_stickerViaBotNameText";
        r13 = org.telegram.ui.ActionBar.Theme.getColor(r13);
        r14 = r1.currentNameString;
        r14 = r14.length();
        if (r14 <= 0) goto L_0x028d;
        r14 = new android.text.SpannableStringBuilder;
        r15 = "%s via %s";
        r10 = 2;
        r12 = new java.lang.Object[r10];
        r12[r6] = r11;
        r10 = 1;
        r12[r10] = r3;
        r12 = java.lang.String.format(r15, r12);
        r14.<init>(r12);
        r12 = r14;
        r14 = new org.telegram.ui.Components.TypefaceSpan;
        r15 = android.graphics.Typeface.DEFAULT;
        r14.<init>(r15, r6, r13);
        r15 = r11.length();
        r15 = r15 + r10;
        r10 = r11.length();
        r17 = 4;
        r10 = r10 + 4;
        r6 = 33;
        r12.setSpan(r14, r15, r10, r6);
        r10 = new org.telegram.ui.Components.TypefaceSpan;
        r14 = "fonts/rmedium.ttf";
        r14 = org.telegram.messenger.AndroidUtilities.getTypeface(r14);
        r15 = 0;
        r10.<init>(r14, r15, r13);
        r14 = r11.length();
        r15 = 5;
        r14 = r14 + r15;
        r15 = r12.length();
        r12.setSpan(r10, r14, r15, r6);
        r6 = r12;
        goto L_0x02c0;
        r6 = new android.text.SpannableStringBuilder;
        r10 = "via %s";
        r12 = 1;
        r14 = new java.lang.Object[r12];
        r12 = 0;
        r14[r12] = r3;
        r10 = java.lang.String.format(r10, r14);
        r6.<init>(r10);
        r10 = new org.telegram.ui.Components.TypefaceSpan;
        r14 = android.graphics.Typeface.DEFAULT;
        r10.<init>(r14, r12, r13);
        r14 = 33;
        r15 = 4;
        r6.setSpan(r10, r12, r15, r14);
        r10 = new org.telegram.ui.Components.TypefaceSpan;
        r14 = "fonts/rmedium.ttf";
        r14 = org.telegram.messenger.AndroidUtilities.getTypeface(r14);
        r10.<init>(r14, r12, r13);
        r12 = r6.length();
        r14 = 33;
        r6.setSpan(r10, r15, r12, r14);
        r10 = org.telegram.ui.ActionBar.Theme.chat_namePaint;
        r11 = r1.nameWidth;
        r11 = (float) r11;
        r12 = android.text.TextUtils.TruncateAt.END;
        r11 = android.text.TextUtils.ellipsize(r6, r10, r11, r12);
    L_0x02cb:
        r6 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x035d }
        r21 = org.telegram.ui.ActionBar.Theme.chat_namePaint;	 Catch:{ Exception -> 0x035d }
        r10 = r1.nameWidth;	 Catch:{ Exception -> 0x035d }
        r12 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Exception -> 0x035d }
        r13 = org.telegram.messenger.AndroidUtilities.dp(r12);	 Catch:{ Exception -> 0x035d }
        r22 = r10 + r13;	 Catch:{ Exception -> 0x035d }
        r23 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x035d }
        r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x035d }
        r25 = 0;	 Catch:{ Exception -> 0x035d }
        r26 = 0;	 Catch:{ Exception -> 0x035d }
        r19 = r6;	 Catch:{ Exception -> 0x035d }
        r20 = r11;	 Catch:{ Exception -> 0x035d }
        r19.<init>(r20, r21, r22, r23, r24, r25, r26);	 Catch:{ Exception -> 0x035d }
        r1.nameLayout = r6;	 Catch:{ Exception -> 0x035d }
        r6 = r1.nameLayout;	 Catch:{ Exception -> 0x035d }
        if (r6 == 0) goto L_0x0320;	 Catch:{ Exception -> 0x035d }
        r6 = r1.nameLayout;	 Catch:{ Exception -> 0x035d }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x035d }
        if (r6 <= 0) goto L_0x0320;	 Catch:{ Exception -> 0x035d }
        r6 = r1.nameLayout;	 Catch:{ Exception -> 0x035d }
        r10 = 0;	 Catch:{ Exception -> 0x035d }
        r6 = r6.getLineWidth(r10);	 Catch:{ Exception -> 0x035d }
        r12 = (double) r6;	 Catch:{ Exception -> 0x035d }
        r12 = java.lang.Math.ceil(r12);	 Catch:{ Exception -> 0x035d }
        r6 = (int) r12;	 Catch:{ Exception -> 0x035d }
        r1.nameWidth = r6;	 Catch:{ Exception -> 0x035d }
        r6 = r2.type;	 Catch:{ Exception -> 0x035d }
        r10 = 13;	 Catch:{ Exception -> 0x035d }
        if (r6 == r10) goto L_0x0316;	 Catch:{ Exception -> 0x035d }
        r6 = r1.namesOffset;	 Catch:{ Exception -> 0x035d }
        r10 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;	 Catch:{ Exception -> 0x035d }
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x035d }
        r6 = r6 + r10;	 Catch:{ Exception -> 0x035d }
        r1.namesOffset = r6;	 Catch:{ Exception -> 0x035d }
        r6 = r1.nameLayout;	 Catch:{ Exception -> 0x035d }
        r10 = 0;	 Catch:{ Exception -> 0x035d }
        r6 = r6.getLineLeft(r10);	 Catch:{ Exception -> 0x035d }
        r1.nameOffsetX = r6;	 Catch:{ Exception -> 0x035d }
        goto L_0x0323;	 Catch:{ Exception -> 0x035d }
        r6 = 0;	 Catch:{ Exception -> 0x035d }
        r1.nameWidth = r6;	 Catch:{ Exception -> 0x035d }
        if (r5 == 0) goto L_0x0359;	 Catch:{ Exception -> 0x035d }
        r6 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x035d }
        r21 = org.telegram.ui.ActionBar.Theme.chat_adminPaint;	 Catch:{ Exception -> 0x035d }
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Exception -> 0x035d }
        r12 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x035d }
        r22 = r4 + r12;	 Catch:{ Exception -> 0x035d }
        r23 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x035d }
        r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x035d }
        r25 = 0;	 Catch:{ Exception -> 0x035d }
        r26 = 0;	 Catch:{ Exception -> 0x035d }
        r19 = r6;	 Catch:{ Exception -> 0x035d }
        r20 = r5;	 Catch:{ Exception -> 0x035d }
        r19.<init>(r20, r21, r22, r23, r24, r25, r26);	 Catch:{ Exception -> 0x035d }
        r1.adminLayout = r6;	 Catch:{ Exception -> 0x035d }
        r6 = r1.nameWidth;	 Catch:{ Exception -> 0x035d }
        r6 = (float) r6;	 Catch:{ Exception -> 0x035d }
        r10 = r1.adminLayout;	 Catch:{ Exception -> 0x035d }
        r12 = 0;	 Catch:{ Exception -> 0x035d }
        r10 = r10.getLineWidth(r12);	 Catch:{ Exception -> 0x035d }
        r12 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;	 Catch:{ Exception -> 0x035d }
        r13 = org.telegram.messenger.AndroidUtilities.dp(r12);	 Catch:{ Exception -> 0x035d }
        r12 = (float) r13;	 Catch:{ Exception -> 0x035d }
        r10 = r10 + r12;	 Catch:{ Exception -> 0x035d }
        r6 = r6 + r10;	 Catch:{ Exception -> 0x035d }
        r6 = (int) r6;	 Catch:{ Exception -> 0x035d }
        r1.nameWidth = r6;	 Catch:{ Exception -> 0x035d }
        goto L_0x035c;	 Catch:{ Exception -> 0x035d }
        r6 = 0;	 Catch:{ Exception -> 0x035d }
        r1.adminLayout = r6;	 Catch:{ Exception -> 0x035d }
        goto L_0x0362;
    L_0x035d:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r6 = r1.currentNameString;
        r6 = r6.length();
        if (r6 != 0) goto L_0x036e;
        r6 = 0;
        r1.currentNameString = r6;
        goto L_0x036f;
        r6 = 0;
    L_0x036f:
        r1.currentForwardUser = r6;
        r1.currentForwardNameString = r6;
        r1.currentForwardChannel = r6;
        r4 = r1.forwardedNameLayout;
        r5 = 0;
        r4[r5] = r6;
        r4 = r1.forwardedNameLayout;
        r10 = 1;
        r4[r10] = r6;
        r1.forwardedNameWidth = r5;
        r4 = r1.drawForwardedName;
        if (r4 == 0) goto L_0x05ba;
        r4 = r31.needDrawForwarded();
        if (r4 == 0) goto L_0x05ba;
        r4 = r1.currentPosition;
        if (r4 == 0) goto L_0x039a;
        r4 = r1.currentPosition;
        r4 = r4.minY;
        if (r4 != 0) goto L_0x0396;
        goto L_0x039a;
        r27 = r3;
        goto L_0x05bc;
        r4 = r2.messageOwner;
        r4 = r4.fwd_from;
        r4 = r4.channel_id;
        if (r4 == 0) goto L_0x03b8;
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r5 = r2.messageOwner;
        r5 = r5.fwd_from;
        r5 = r5.channel_id;
        r5 = java.lang.Integer.valueOf(r5);
        r4 = r4.getChat(r5);
        r1.currentForwardChannel = r4;
        r4 = r2.messageOwner;
        r4 = r4.fwd_from;
        r4 = r4.from_id;
        if (r4 == 0) goto L_0x03d6;
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r5 = r2.messageOwner;
        r5 = r5.fwd_from;
        r5 = r5.from_id;
        r5 = java.lang.Integer.valueOf(r5);
        r4 = r4.getUser(r5);
        r1.currentForwardUser = r4;
        r4 = r1.currentForwardUser;
        if (r4 != 0) goto L_0x03de;
        r4 = r1.currentForwardChannel;
        if (r4 == 0) goto L_0x0396;
        r4 = r1.currentForwardChannel;
        if (r4 == 0) goto L_0x0409;
        r4 = r1.currentForwardUser;
        if (r4 == 0) goto L_0x0402;
        r4 = "%s (%s)";
        r5 = 2;
        r6 = new java.lang.Object[r5];
        r5 = r1.currentForwardChannel;
        r5 = r5.title;
        r10 = 0;
        r6[r10] = r5;
        r5 = r1.currentForwardUser;
        r5 = org.telegram.messenger.UserObject.getUserName(r5);
        r10 = 1;
        r6[r10] = r5;
        r4 = java.lang.String.format(r4, r6);
        r1.currentForwardNameString = r4;
        goto L_0x0415;
        r4 = r1.currentForwardChannel;
        r4 = r4.title;
        r1.currentForwardNameString = r4;
        goto L_0x0415;
        r4 = r1.currentForwardUser;
        if (r4 == 0) goto L_0x0415;
        r4 = r1.currentForwardUser;
        r4 = org.telegram.messenger.UserObject.getUserName(r4);
        r1.currentForwardNameString = r4;
        r4 = r30.getMaxNameWidth();
        r1.forwardedNameWidth = r4;
        r4 = "From";
        r5 = 2131493612; // 0x7f0c02ec float:1.861071E38 double:1.053097768E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
        r5 = "FromFormatted";
        r6 = 2131493620; // 0x7f0c02f4 float:1.8610725E38 double:1.053097772E-314;
        r5 = org.telegram.messenger.LocaleController.getString(r5, r6);
        r6 = "%1$s";
        r6 = r5.indexOf(r6);
        r10 = org.telegram.ui.ActionBar.Theme.chat_forwardNamePaint;
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r11.append(r4);
        r12 = " ";
        r11.append(r12);
        r11 = r11.toString();
        r10 = r10.measureText(r11);
        r10 = (double) r10;
        r10 = java.lang.Math.ceil(r10);
        r10 = (int) r10;
        r11 = r1.currentForwardNameString;
        r12 = 10;
        r13 = 32;
        r11 = r11.replace(r12, r13);
        r12 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;
        r13 = r1.forwardedNameWidth;
        r13 = r13 - r10;
        r14 = r1.viaWidth;
        r13 = r13 - r14;
        r13 = (float) r13;
        r14 = android.text.TextUtils.TruncateAt.END;
        r11 = android.text.TextUtils.ellipsize(r11, r12, r13, r14);
        r12 = 1;
        r13 = new java.lang.Object[r12];	 Catch:{ Exception -> 0x0474 }
        r12 = 0;	 Catch:{ Exception -> 0x0474 }
        r13[r12] = r11;	 Catch:{ Exception -> 0x0474 }
        r12 = java.lang.String.format(r5, r13);	 Catch:{ Exception -> 0x0474 }
        goto L_0x047a;
    L_0x0474:
        r0 = move-exception;
        r12 = r0;
        r12 = r11.toString();
        if (r7 == 0) goto L_0x04c5;
        r13 = new android.text.SpannableStringBuilder;
        r14 = "%s via %s";
        r15 = 2;
        r15 = new java.lang.Object[r15];
        r19 = 0;
        r15[r19] = r12;
        r18 = 1;
        r15[r18] = r3;
        r14 = java.lang.String.format(r14, r15);
        r13.<init>(r14);
        r14 = org.telegram.ui.ActionBar.Theme.chat_forwardNamePaint;
        r14 = r14.measureText(r12);
        r14 = (double) r14;
        r14 = java.lang.Math.ceil(r14);
        r14 = (int) r14;
        r1.viaNameWidth = r14;
        r14 = new org.telegram.ui.Components.TypefaceSpan;
        r15 = "fonts/rmedium.ttf";
        r15 = org.telegram.messenger.AndroidUtilities.getTypeface(r15);
        r14.<init>(r15);
        r15 = r13.length();
        r19 = r3.length();
        r15 = r15 - r19;
        r27 = r3;
        r3 = 1;
        r15 = r15 - r3;
        r3 = r13.length();
        r28 = r4;
        r4 = 33;
        r13.setSpan(r14, r15, r3, r4);
        goto L_0x04d8;
        r27 = r3;
        r28 = r4;
        r13 = new android.text.SpannableStringBuilder;
        r3 = 1;
        r4 = new java.lang.Object[r3];
        r3 = 0;
        r4[r3] = r11;
        r3 = java.lang.String.format(r5, r4);
        r13.<init>(r3);
        r3 = r13;
        if (r6 < 0) goto L_0x04f0;
        r4 = new org.telegram.ui.Components.TypefaceSpan;
        r13 = "fonts/rmedium.ttf";
        r13 = org.telegram.messenger.AndroidUtilities.getTypeface(r13);
        r4.<init>(r13);
        r13 = r11.length();
        r13 = r13 + r6;
        r14 = 33;
        r3.setSpan(r4, r6, r13, r14);
        r4 = r3;
        r13 = org.telegram.ui.ActionBar.Theme.chat_forwardNamePaint;
        r14 = r1.forwardedNameWidth;
        r14 = (float) r14;
        r15 = android.text.TextUtils.TruncateAt.END;
        r4 = android.text.TextUtils.ellipsize(r4, r13, r14, r15);
        r13 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x05b0 }
        r14 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x05b0 }
        r21 = org.telegram.ui.ActionBar.Theme.chat_forwardNamePaint;	 Catch:{ Exception -> 0x05b0 }
        r15 = r1.forwardedNameWidth;	 Catch:{ Exception -> 0x05b0 }
        r29 = r3;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r16 = org.telegram.messenger.AndroidUtilities.dp(r3);	 Catch:{ Exception -> 0x05ab }
        r22 = r15 + r16;	 Catch:{ Exception -> 0x05ab }
        r23 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x05ab }
        r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x05ab }
        r25 = 0;	 Catch:{ Exception -> 0x05ab }
        r26 = 0;	 Catch:{ Exception -> 0x05ab }
        r19 = r14;	 Catch:{ Exception -> 0x05ab }
        r20 = r4;	 Catch:{ Exception -> 0x05ab }
        r19.<init>(r20, r21, r22, r23, r24, r25, r26);	 Catch:{ Exception -> 0x05ab }
        r3 = 1;	 Catch:{ Exception -> 0x05ab }
        r13[r3] = r14;	 Catch:{ Exception -> 0x05ab }
        r3 = "ForwardedMessage";	 Catch:{ Exception -> 0x05ab }
        r13 = 2131493574; // 0x7f0c02c6 float:1.8610632E38 double:1.053097749E-314;	 Catch:{ Exception -> 0x05ab }
        r3 = org.telegram.messenger.LocaleController.getString(r3, r13);	 Catch:{ Exception -> 0x05ab }
        r3 = org.telegram.messenger.AndroidUtilities.replaceTags(r3);	 Catch:{ Exception -> 0x05ab }
        r13 = org.telegram.ui.ActionBar.Theme.chat_forwardNamePaint;	 Catch:{ Exception -> 0x05ab }
        r14 = r1.forwardedNameWidth;	 Catch:{ Exception -> 0x05ab }
        r14 = (float) r14;	 Catch:{ Exception -> 0x05ab }
        r15 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x05ab }
        r20 = android.text.TextUtils.ellipsize(r3, r13, r14, r15);	 Catch:{ Exception -> 0x05ab }
        r3 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x05a8 }
        r4 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x05a8 }
        r21 = org.telegram.ui.ActionBar.Theme.chat_forwardNamePaint;	 Catch:{ Exception -> 0x05a8 }
        r13 = r1.forwardedNameWidth;	 Catch:{ Exception -> 0x05a8 }
        r14 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Exception -> 0x05a8 }
        r14 = org.telegram.messenger.AndroidUtilities.dp(r14);	 Catch:{ Exception -> 0x05a8 }
        r22 = r13 + r14;	 Catch:{ Exception -> 0x05a8 }
        r23 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x05a8 }
        r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x05a8 }
        r25 = 0;	 Catch:{ Exception -> 0x05a8 }
        r26 = 0;	 Catch:{ Exception -> 0x05a8 }
        r19 = r4;	 Catch:{ Exception -> 0x05a8 }
        r19.<init>(r20, r21, r22, r23, r24, r25, r26);	 Catch:{ Exception -> 0x05a8 }
        r13 = 0;	 Catch:{ Exception -> 0x05a8 }
        r3[r13] = r4;	 Catch:{ Exception -> 0x05a8 }
        r3 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x05a8 }
        r3 = r3[r13];	 Catch:{ Exception -> 0x05a8 }
        r3 = r3.getLineWidth(r13);	 Catch:{ Exception -> 0x05a8 }
        r3 = (double) r3;	 Catch:{ Exception -> 0x05a8 }
        r3 = java.lang.Math.ceil(r3);	 Catch:{ Exception -> 0x05a8 }
        r3 = (int) r3;	 Catch:{ Exception -> 0x05a8 }
        r4 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x05a8 }
        r13 = 1;	 Catch:{ Exception -> 0x05a8 }
        r4 = r4[r13];	 Catch:{ Exception -> 0x05a8 }
        r13 = 0;	 Catch:{ Exception -> 0x05a8 }
        r4 = r4.getLineWidth(r13);	 Catch:{ Exception -> 0x05a8 }
        r13 = (double) r4;	 Catch:{ Exception -> 0x05a8 }
        r13 = java.lang.Math.ceil(r13);	 Catch:{ Exception -> 0x05a8 }
        r4 = (int) r13;	 Catch:{ Exception -> 0x05a8 }
        r3 = java.lang.Math.max(r3, r4);	 Catch:{ Exception -> 0x05a8 }
        r1.forwardedNameWidth = r3;	 Catch:{ Exception -> 0x05a8 }
        r3 = r1.forwardNameOffsetX;	 Catch:{ Exception -> 0x05a8 }
        r4 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x05a8 }
        r13 = 0;	 Catch:{ Exception -> 0x05a8 }
        r4 = r4[r13];	 Catch:{ Exception -> 0x05a8 }
        r4 = r4.getLineLeft(r13);	 Catch:{ Exception -> 0x05a8 }
        r3[r13] = r4;	 Catch:{ Exception -> 0x05a8 }
        r3 = r1.forwardNameOffsetX;	 Catch:{ Exception -> 0x05a8 }
        r4 = r1.forwardedNameLayout;	 Catch:{ Exception -> 0x05a8 }
        r13 = 1;	 Catch:{ Exception -> 0x05a8 }
        r4 = r4[r13];	 Catch:{ Exception -> 0x05a8 }
        r14 = 0;	 Catch:{ Exception -> 0x05a8 }
        r4 = r4.getLineLeft(r14);	 Catch:{ Exception -> 0x05a8 }
        r3[r13] = r4;	 Catch:{ Exception -> 0x05a8 }
        r3 = r2.type;	 Catch:{ Exception -> 0x05a8 }
        r4 = 5;	 Catch:{ Exception -> 0x05a8 }
        if (r3 == r4) goto L_0x05a7;	 Catch:{ Exception -> 0x05a8 }
        r3 = r1.namesOffset;	 Catch:{ Exception -> 0x05a8 }
        r4 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;	 Catch:{ Exception -> 0x05a8 }
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);	 Catch:{ Exception -> 0x05a8 }
        r3 = r3 + r4;	 Catch:{ Exception -> 0x05a8 }
        r1.namesOffset = r3;	 Catch:{ Exception -> 0x05a8 }
        goto L_0x05bc;
    L_0x05a8:
        r0 = move-exception;
        r3 = r0;
        goto L_0x05b6;
    L_0x05ab:
        r0 = move-exception;
        r3 = r0;
        r20 = r4;
        goto L_0x05b6;
    L_0x05b0:
        r0 = move-exception;
        r29 = r3;
        r3 = r0;
        r20 = r4;
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x05bc;
        r27 = r3;
        r3 = r31.hasValidReplyMessageObject();
        if (r3 == 0) goto L_0x085e;
        r3 = r1.currentPosition;
        if (r3 == 0) goto L_0x05cc;
        r3 = r1.currentPosition;
        r3 = r3.minY;
        if (r3 != 0) goto L_0x085e;
        r3 = r2.type;
        r4 = 13;
        if (r3 == r4) goto L_0x05f1;
        r3 = r2.type;
        r4 = 5;
        if (r3 == r4) goto L_0x05f1;
        r3 = r1.namesOffset;
        r4 = 1109917696; // 0x42280000 float:42.0 double:5.483722033E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r4;
        r1.namesOffset = r3;
        r3 = r2.type;
        if (r3 == 0) goto L_0x05f1;
        r3 = r1.namesOffset;
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r4;
        r1.namesOffset = r3;
        r3 = r30.getMaxNameWidth();
        r4 = r2.type;
        r5 = 13;
        if (r4 == r5) goto L_0x0607;
        r4 = r2.type;
        r5 = 5;
        if (r4 == r5) goto L_0x0607;
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r4 = 0;
        r5 = r2.replyMessageObject;
        r5 = r5.photoThumbs2;
        r6 = 80;
        r5 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r5, r6);
        if (r5 != 0) goto L_0x061c;
        r10 = r2.replyMessageObject;
        r10 = r10.photoThumbs;
        r5 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r10, r6);
        if (r5 == 0) goto L_0x066e;
        r6 = r2.replyMessageObject;
        r6 = r6.type;
        r10 = 13;
        if (r6 == r10) goto L_0x066e;
        r6 = r2.type;
        if (r6 != r10) goto L_0x0630;
        r6 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r6 == 0) goto L_0x066e;
        r6 = r2.replyMessageObject;
        r6 = r6.isSecretMedia();
        if (r6 == 0) goto L_0x0639;
        goto L_0x066e;
        r6 = r2.replyMessageObject;
        r6 = r6.isRoundVideo();
        if (r6 == 0) goto L_0x064d;
        r6 = r1.replyImageReceiver;
        r10 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r6.setRoundRadius(r10);
        goto L_0x0653;
        r6 = r1.replyImageReceiver;
        r10 = 0;
        r6.setRoundRadius(r10);
        r6 = r5.location;
        r1.currentReplyPhoto = r6;
        r10 = r1.replyImageReceiver;
        r11 = r5.location;
        r12 = "50_50";
        r13 = 0;
        r14 = 0;
        r15 = 1;
        r10.setImage(r11, r12, r13, r14, r15);
        r6 = 1;
        r1.needReplyImage = r6;
        r6 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r3 = r3 - r6;
        goto L_0x0679;
        r6 = r1.replyImageReceiver;
        r10 = 0;
        r10 = (android.graphics.drawable.Drawable) r10;
        r6.setImageBitmap(r10);
        r6 = 0;
        r1.needReplyImage = r6;
        r6 = 0;
        r10 = r2.customReplyName;
        if (r10 == 0) goto L_0x0681;
        r6 = r2.customReplyName;
        goto L_0x06e0;
        r10 = r2.replyMessageObject;
        r10 = r10.isFromUser();
        if (r10 == 0) goto L_0x06a4;
        r10 = r1.currentAccount;
        r10 = org.telegram.messenger.MessagesController.getInstance(r10);
        r11 = r2.replyMessageObject;
        r11 = r11.messageOwner;
        r11 = r11.from_id;
        r11 = java.lang.Integer.valueOf(r11);
        r10 = r10.getUser(r11);
        if (r10 == 0) goto L_0x06a3;
        r6 = org.telegram.messenger.UserObject.getUserName(r10);
        goto L_0x06e0;
        r10 = r2.replyMessageObject;
        r10 = r10.messageOwner;
        r10 = r10.from_id;
        if (r10 >= 0) goto L_0x06c6;
        r10 = r1.currentAccount;
        r10 = org.telegram.messenger.MessagesController.getInstance(r10);
        r11 = r2.replyMessageObject;
        r11 = r11.messageOwner;
        r11 = r11.from_id;
        r11 = -r11;
        r11 = java.lang.Integer.valueOf(r11);
        r10 = r10.getChat(r11);
        if (r10 == 0) goto L_0x06c5;
        r6 = r10.title;
        goto L_0x06e0;
        r10 = r1.currentAccount;
        r10 = org.telegram.messenger.MessagesController.getInstance(r10);
        r11 = r2.replyMessageObject;
        r11 = r11.messageOwner;
        r11 = r11.to_id;
        r11 = r11.channel_id;
        r11 = java.lang.Integer.valueOf(r11);
        r10 = r10.getChat(r11);
        if (r10 == 0) goto L_0x06e0;
        r6 = r10.title;
        if (r6 != 0) goto L_0x06eb;
        r10 = "Loading";
        r11 = 2131493762; // 0x7f0c0382 float:1.8611013E38 double:1.053097842E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r10, r11);
        r10 = 10;
        r11 = 32;
        r12 = r6.replace(r10, r11);
        r10 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;
        r11 = (float) r3;
        r13 = android.text.TextUtils.TruncateAt.END;
        r10 = android.text.TextUtils.ellipsize(r12, r10, r11, r13);
        r11 = r2.replyMessageObject;
        r11 = r11.messageOwner;
        r11 = r11.media;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        r12 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        if (r11 == 0) goto L_0x072b;
        r11 = r2.replyMessageObject;
        r11 = r11.messageOwner;
        r11 = r11.media;
        r11 = r11.game;
        r11 = r11.title;
        r13 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r13 = r13.getFontMetricsInt();
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r14 = 0;
        r4 = org.telegram.messenger.Emoji.replaceEmoji(r11, r13, r12, r14);
        r11 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r12 = (float) r3;
        r13 = android.text.TextUtils.TruncateAt.END;
        r4 = android.text.TextUtils.ellipsize(r4, r11, r12, r13);
        goto L_0x079c;
        r11 = r2.replyMessageObject;
        r11 = r11.messageOwner;
        r11 = r11.media;
        r11 = r11 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        if (r11 == 0) goto L_0x0756;
        r11 = r2.replyMessageObject;
        r11 = r11.messageOwner;
        r11 = r11.media;
        r11 = r11.title;
        r13 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r13 = r13.getFontMetricsInt();
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r14 = 0;
        r4 = org.telegram.messenger.Emoji.replaceEmoji(r11, r13, r12, r14);
        r11 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r12 = (float) r3;
        r13 = android.text.TextUtils.TruncateAt.END;
        r4 = android.text.TextUtils.ellipsize(r4, r11, r12, r13);
        goto L_0x079c;
        r11 = r2.replyMessageObject;
        r11 = r11.messageText;
        if (r11 == 0) goto L_0x079c;
        r11 = r2.replyMessageObject;
        r11 = r11.messageText;
        r11 = r11.length();
        if (r11 <= 0) goto L_0x079c;
        r11 = r2.replyMessageObject;
        r11 = r11.messageText;
        r11 = r11.toString();
        r13 = r11.length();
        r14 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        if (r13 <= r14) goto L_0x077c;
        r13 = 0;
        r11 = r11.substring(r13, r14);
        goto L_0x077d;
        r13 = 0;
        r14 = 10;
        r15 = 32;
        r11 = r11.replace(r14, r15);
        r14 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r14 = r14.getFontMetricsInt();
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r4 = org.telegram.messenger.Emoji.replaceEmoji(r11, r14, r12, r13);
        r12 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;
        r13 = (float) r3;
        r14 = android.text.TextUtils.TruncateAt.END;
        r4 = android.text.TextUtils.ellipsize(r4, r12, r13, r14);
        r11 = r1.needReplyImage;	 Catch:{ Exception -> 0x07f8 }
        if (r11 == 0) goto L_0x07a3;	 Catch:{ Exception -> 0x07f8 }
        r11 = 44;	 Catch:{ Exception -> 0x07f8 }
        goto L_0x07a4;	 Catch:{ Exception -> 0x07f8 }
        r11 = 0;	 Catch:{ Exception -> 0x07f8 }
        r12 = 4;	 Catch:{ Exception -> 0x07f8 }
        r15 = r12 + r11;	 Catch:{ Exception -> 0x07f8 }
        r11 = (float) r15;	 Catch:{ Exception -> 0x07f8 }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);	 Catch:{ Exception -> 0x07f8 }
        r1.replyNameWidth = r11;	 Catch:{ Exception -> 0x07f8 }
        if (r10 == 0) goto L_0x07f7;	 Catch:{ Exception -> 0x07f8 }
        r11 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x07f8 }
        r20 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x07f8 }
        r12 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;	 Catch:{ Exception -> 0x07f8 }
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);	 Catch:{ Exception -> 0x07f8 }
        r21 = r3 + r12;	 Catch:{ Exception -> 0x07f8 }
        r22 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x07f8 }
        r23 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x07f8 }
        r24 = 0;	 Catch:{ Exception -> 0x07f8 }
        r25 = 0;	 Catch:{ Exception -> 0x07f8 }
        r18 = r11;	 Catch:{ Exception -> 0x07f8 }
        r19 = r10;	 Catch:{ Exception -> 0x07f8 }
        r18.<init>(r19, r20, r21, r22, r23, r24, r25);	 Catch:{ Exception -> 0x07f8 }
        r1.replyNameLayout = r11;	 Catch:{ Exception -> 0x07f8 }
        r11 = r1.replyNameLayout;	 Catch:{ Exception -> 0x07f8 }
        r11 = r11.getLineCount();	 Catch:{ Exception -> 0x07f8 }
        if (r11 <= 0) goto L_0x07f7;	 Catch:{ Exception -> 0x07f8 }
        r11 = r1.replyNameWidth;	 Catch:{ Exception -> 0x07f8 }
        r12 = r1.replyNameLayout;	 Catch:{ Exception -> 0x07f8 }
        r13 = 0;	 Catch:{ Exception -> 0x07f8 }
        r12 = r12.getLineWidth(r13);	 Catch:{ Exception -> 0x07f8 }
        r12 = (double) r12;	 Catch:{ Exception -> 0x07f8 }
        r12 = java.lang.Math.ceil(r12);	 Catch:{ Exception -> 0x07f8 }
        r12 = (int) r12;	 Catch:{ Exception -> 0x07f8 }
        r13 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;	 Catch:{ Exception -> 0x07f8 }
        r14 = org.telegram.messenger.AndroidUtilities.dp(r13);	 Catch:{ Exception -> 0x07f8 }
        r12 = r12 + r14;	 Catch:{ Exception -> 0x07f8 }
        r11 = r11 + r12;	 Catch:{ Exception -> 0x07f8 }
        r1.replyNameWidth = r11;	 Catch:{ Exception -> 0x07f8 }
        r11 = r1.replyNameLayout;	 Catch:{ Exception -> 0x07f8 }
        r12 = 0;	 Catch:{ Exception -> 0x07f8 }
        r11 = r11.getLineLeft(r12);	 Catch:{ Exception -> 0x07f8 }
        r1.replyNameOffset = r11;	 Catch:{ Exception -> 0x07f8 }
        goto L_0x07fd;
    L_0x07f8:
        r0 = move-exception;
        r11 = r0;
        org.telegram.messenger.FileLog.e(r11);
        r11 = r1.needReplyImage;	 Catch:{ Exception -> 0x0859 }
        if (r11 == 0) goto L_0x0804;	 Catch:{ Exception -> 0x0859 }
        r11 = 44;	 Catch:{ Exception -> 0x0859 }
        goto L_0x0805;	 Catch:{ Exception -> 0x0859 }
        r11 = 0;	 Catch:{ Exception -> 0x0859 }
        r12 = 4;	 Catch:{ Exception -> 0x0859 }
        r15 = r12 + r11;	 Catch:{ Exception -> 0x0859 }
        r11 = (float) r15;	 Catch:{ Exception -> 0x0859 }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);	 Catch:{ Exception -> 0x0859 }
        r1.replyTextWidth = r11;	 Catch:{ Exception -> 0x0859 }
        if (r4 == 0) goto L_0x0858;	 Catch:{ Exception -> 0x0859 }
        r11 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0859 }
        r20 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x0859 }
        r12 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;	 Catch:{ Exception -> 0x0859 }
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);	 Catch:{ Exception -> 0x0859 }
        r21 = r3 + r12;	 Catch:{ Exception -> 0x0859 }
        r22 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0859 }
        r23 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0859 }
        r24 = 0;	 Catch:{ Exception -> 0x0859 }
        r25 = 0;	 Catch:{ Exception -> 0x0859 }
        r18 = r11;	 Catch:{ Exception -> 0x0859 }
        r19 = r4;	 Catch:{ Exception -> 0x0859 }
        r18.<init>(r19, r20, r21, r22, r23, r24, r25);	 Catch:{ Exception -> 0x0859 }
        r1.replyTextLayout = r11;	 Catch:{ Exception -> 0x0859 }
        r11 = r1.replyTextLayout;	 Catch:{ Exception -> 0x0859 }
        r11 = r11.getLineCount();	 Catch:{ Exception -> 0x0859 }
        if (r11 <= 0) goto L_0x0858;	 Catch:{ Exception -> 0x0859 }
        r11 = r1.replyTextWidth;	 Catch:{ Exception -> 0x0859 }
        r12 = r1.replyTextLayout;	 Catch:{ Exception -> 0x0859 }
        r13 = 0;	 Catch:{ Exception -> 0x0859 }
        r12 = r12.getLineWidth(r13);	 Catch:{ Exception -> 0x0859 }
        r12 = (double) r12;	 Catch:{ Exception -> 0x0859 }
        r12 = java.lang.Math.ceil(r12);	 Catch:{ Exception -> 0x0859 }
        r12 = (int) r12;	 Catch:{ Exception -> 0x0859 }
        r13 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;	 Catch:{ Exception -> 0x0859 }
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);	 Catch:{ Exception -> 0x0859 }
        r12 = r12 + r13;	 Catch:{ Exception -> 0x0859 }
        r11 = r11 + r12;	 Catch:{ Exception -> 0x0859 }
        r1.replyTextWidth = r11;	 Catch:{ Exception -> 0x0859 }
        r11 = r1.replyTextLayout;	 Catch:{ Exception -> 0x0859 }
        r12 = 0;	 Catch:{ Exception -> 0x0859 }
        r11 = r11.getLineLeft(r12);	 Catch:{ Exception -> 0x0859 }
        r1.replyTextOffset = r11;	 Catch:{ Exception -> 0x0859 }
        goto L_0x085e;
    L_0x0859:
        r0 = move-exception;
        r11 = r0;
        org.telegram.messenger.FileLog.e(r11);
        r30.requestLayout();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatMessageCell.setMessageObjectInternal(org.telegram.messenger.MessageObject):void");
    }

    public void drawTimeLayout(android.graphics.Canvas r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ChatMessageCell.drawTimeLayout(android.graphics.Canvas):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r18;
        r1 = r19;
        r2 = r0.drawTime;
        if (r2 == 0) goto L_0x000c;
    L_0x0008:
        r2 = r0.groupPhotoInvisible;
        if (r2 == 0) goto L_0x0014;
    L_0x000c:
        r2 = r0.mediaBackground;
        if (r2 == 0) goto L_0x0014;
    L_0x0010:
        r2 = r0.captionLayout;
        if (r2 == 0) goto L_0x0018;
    L_0x0014:
        r2 = r0.timeLayout;
        if (r2 != 0) goto L_0x0019;
    L_0x0018:
        return;
    L_0x0019:
        r2 = r0.currentMessageObject;
        r2 = r2.type;
        r3 = 13;
        r4 = 5;
        if (r2 != r4) goto L_0x002e;
    L_0x0022:
        r2 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r5 = "chat_mediaTimeText";
        r5 = org.telegram.ui.ActionBar.Theme.getColor(r5);
        r2.setColor(r5);
        goto L_0x008c;
    L_0x002e:
        r2 = r0.mediaBackground;
        if (r2 == 0) goto L_0x005b;
    L_0x0032:
        r2 = r0.captionLayout;
        if (r2 != 0) goto L_0x005b;
    L_0x0036:
        r2 = r0.currentMessageObject;
        r2 = r2.type;
        if (r2 == r3) goto L_0x004f;
    L_0x003c:
        r2 = r0.currentMessageObject;
        r2 = r2.type;
        if (r2 != r4) goto L_0x0043;
    L_0x0042:
        goto L_0x004f;
    L_0x0043:
        r2 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r5 = "chat_mediaTimeText";
        r5 = org.telegram.ui.ActionBar.Theme.getColor(r5);
        r2.setColor(r5);
        goto L_0x008c;
    L_0x004f:
        r2 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r5 = "chat_serviceText";
        r5 = org.telegram.ui.ActionBar.Theme.getColor(r5);
        r2.setColor(r5);
        goto L_0x008c;
    L_0x005b:
        r2 = r0.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0078;
    L_0x0063:
        r2 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r5 = r18.isDrawSelectedBackground();
        if (r5 == 0) goto L_0x006e;
    L_0x006b:
        r5 = "chat_outTimeSelectedText";
        goto L_0x0070;
    L_0x006e:
        r5 = "chat_outTimeText";
    L_0x0070:
        r5 = org.telegram.ui.ActionBar.Theme.getColor(r5);
        r2.setColor(r5);
        goto L_0x008c;
    L_0x0078:
        r2 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r5 = r18.isDrawSelectedBackground();
        if (r5 == 0) goto L_0x0083;
    L_0x0080:
        r5 = "chat_inTimeSelectedText";
        goto L_0x0085;
    L_0x0083:
        r5 = "chat_inTimeText";
    L_0x0085:
        r5 = org.telegram.ui.ActionBar.Theme.getColor(r5);
        r2.setColor(r5);
    L_0x008c:
        r2 = r0.drawPinnedBottom;
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r2 == 0) goto L_0x009b;
    L_0x0092:
        r2 = 0;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r6 = (float) r6;
        r1.translate(r2, r6);
    L_0x009b:
        r2 = r0.mediaBackground;
        r8 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r9 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = 0;
        r12 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        if (r2 == 0) goto L_0x0274;
    L_0x00a8:
        r2 = r0.captionLayout;
        if (r2 != 0) goto L_0x0274;
    L_0x00ac:
        r2 = r0.currentMessageObject;
        r2 = r2.type;
        if (r2 == r3) goto L_0x00bc;
    L_0x00b2:
        r2 = r0.currentMessageObject;
        r2 = r2.type;
        if (r2 != r4) goto L_0x00b9;
    L_0x00b8:
        goto L_0x00bc;
    L_0x00b9:
        r2 = org.telegram.ui.ActionBar.Theme.chat_timeBackgroundPaint;
        goto L_0x00be;
    L_0x00bc:
        r2 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r13 = r2.getAlpha();
        r14 = (float) r13;
        r15 = r0.timeAlpha;
        r14 = r14 * r15;
        r14 = (int) r14;
        r2.setAlpha(r14);
        r14 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r15 = r0.timeAlpha;
        r15 = r15 * r8;
        r15 = (int) r15;
        r14.setAlpha(r15);
        r14 = r0.timeX;
        r15 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r16 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r14 = r14 - r16;
        r8 = r0.layoutHeight;
        r6 = 1105199104; // 0x41e00000 float:28.0 double:5.46040909E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r8 = r8 - r6;
        r6 = r0.rect;
        r4 = (float) r14;
        r3 = (float) r8;
        r5 = r0.timeWidth;
        r5 = r5 + r14;
        r16 = 8;
        r7 = r0.currentMessageObject;
        r7 = r7.isOutOwner();
        if (r7 == 0) goto L_0x00fb;
        r7 = 20;
        goto L_0x00fc;
        r7 = r11;
        r7 = r16 + r7;
        r7 = (float) r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = r5 + r7;
        r5 = (float) r5;
        r7 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = r7 + r8;
        r7 = (float) r7;
        r6.set(r4, r3, r5, r7);
        r3 = r0.rect;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r4 = (float) r4;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r5 = (float) r5;
        r1.drawRoundRect(r3, r4, r5, r2);
        r2.setAlpha(r13);
        r3 = r0.timeLayout;
        r3 = r3.getLineLeft(r11);
        r3 = -r3;
        r3 = (int) r3;
        r4 = r0.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.flags;
        r4 = r4 & 1024;
        if (r4 == 0) goto L_0x0247;
        r4 = r0.timeWidth;
        r4 = (float) r4;
        r5 = r0.timeLayout;
        r5 = r5.getLineWidth(r11);
        r4 = r4 - r5;
        r4 = (int) r4;
        r3 = r3 + r4;
        r4 = r0.currentMessageObject;
        r4 = r4.isSending();
        if (r4 == 0) goto L_0x0171;
        r4 = r0.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 != 0) goto L_0x0247;
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgMediaClockDrawable;
        r5 = r0.timeX;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r5 = r5 + r6;
        r6 = r0.layoutHeight;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r6 = r6 - r7;
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgMediaClockDrawable;
        r7 = r7.getIntrinsicHeight();
        r6 = r6 - r7;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r4, r5, r6);
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgMediaClockDrawable;
        r4.draw(r1);
        goto L_0x0247;
        r4 = r0.currentMessageObject;
        r4 = r4.isSendError();
        if (r4 == 0) goto L_0x01d0;
        r4 = r0.currentMessageObject;
        r4 = r4.isOutOwner();
        if (r4 != 0) goto L_0x0247;
        r4 = r0.timeX;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r4 + r5;
        r5 = r0.layoutHeight;
        r6 = 1104936960; // 0x41dc0000 float:27.5 double:5.45911393E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r6 = r0.rect;
        r7 = (float) r4;
        r9 = (float) r5;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r15 = r15 + r4;
        r15 = (float) r15;
        r16 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r12 = r5 + r16;
        r12 = (float) r12;
        r6.set(r7, r9, r15, r12);
        r6 = r0.rect;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r7 = (float) r7;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = (float) r9;
        r12 = org.telegram.ui.ActionBar.Theme.chat_msgErrorPaint;
        r1.drawRoundRect(r6, r7, r9, r12);
        r6 = org.telegram.ui.ActionBar.Theme.chat_msgErrorDrawable;
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r9 = r9 + r4;
        r7 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r12 = r12 + r5;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r6, r9, r12);
        r6 = org.telegram.ui.ActionBar.Theme.chat_msgErrorDrawable;
        r6.draw(r1);
        goto L_0x0247;
        r4 = r0.currentMessageObject;
        r4 = r4.type;
        r5 = 13;
        if (r4 == r5) goto L_0x01e3;
        r4 = r0.currentMessageObject;
        r4 = r4.type;
        r5 = 5;
        if (r4 != r5) goto L_0x01e0;
        goto L_0x01e3;
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgMediaViewsDrawable;
        goto L_0x01e5;
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgStickerViewsDrawable;
        r5 = r4;
        r5 = (android.graphics.drawable.BitmapDrawable) r5;
        r5 = r5.getPaint();
        r13 = r5.getAlpha();
        r5 = r0.timeAlpha;
        r6 = (float) r13;
        r5 = r5 * r6;
        r5 = (int) r5;
        r4.setAlpha(r5);
        r5 = r0.timeX;
        r6 = r0.layoutHeight;
        r7 = 1093140480; // 0x41280000 float:10.5 double:5.40083157E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r6 = r6 - r7;
        r7 = r0.timeLayout;
        r7 = r7.getHeight();
        r6 = r6 - r7;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r4, r5, r6);
        r4.draw(r1);
        r4.setAlpha(r13);
        r5 = r0.viewsLayout;
        if (r5 == 0) goto L_0x0247;
        r19.save();
        r5 = r0.timeX;
        r6 = r4.getIntrinsicWidth();
        r5 = r5 + r6;
        r6 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 + r6;
        r5 = (float) r5;
        r6 = r0.layoutHeight;
        r7 = 1095027917; // 0x4144cccd float:12.3 double:5.41015675E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r6 = r6 - r7;
        r7 = r0.timeLayout;
        r7 = r7.getHeight();
        r6 = r6 - r7;
        r6 = (float) r6;
        r1.translate(r5, r6);
        r5 = r0.viewsLayout;
        r5.draw(r1);
        r19.restore();
        r19.save();
        r4 = r0.timeX;
        r4 = r4 + r3;
        r4 = (float) r4;
        r5 = r0.layoutHeight;
        r6 = 1095027917; // 0x4144cccd float:12.3 double:5.41015675E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r6 = r0.timeLayout;
        r6 = r6.getHeight();
        r5 = r5 - r6;
        r5 = (float) r5;
        r1.translate(r4, r5);
        r4 = r0.timeLayout;
        r4.draw(r1);
        r19.restore();
        r4 = org.telegram.ui.ActionBar.Theme.chat_timePaint;
        r5 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r4.setAlpha(r5);
        goto L_0x03d0;
    L_0x0274:
        r2 = r0.timeLayout;
        r2 = r2.getLineLeft(r11);
        r2 = -r2;
        r2 = (int) r2;
        r3 = r0.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.flags;
        r3 = r3 & 1024;
        if (r3 == 0) goto L_0x03ad;
        r3 = r0.timeWidth;
        r3 = (float) r3;
        r4 = r0.timeLayout;
        r4 = r4.getLineWidth(r11);
        r3 = r3 - r4;
        r3 = (int) r3;
        r2 = r2 + r3;
        r3 = r0.currentMessageObject;
        r3 = r3.isSending();
        if (r3 == 0) goto L_0x02ca;
        r3 = r0.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 != 0) goto L_0x03ad;
        r3 = r18.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x02ab;
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgInSelectedClockDrawable;
        goto L_0x02ad;
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgInClockDrawable;
        r4 = r0.timeX;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r4 + r5;
        r5 = r0.layoutHeight;
        r6 = 1091043328; // 0x41080000 float:8.5 double:5.390470265E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r6 = r3.getIntrinsicHeight();
        r5 = r5 - r6;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r3, r4, r5);
        r3.draw(r1);
        goto L_0x03ad;
        r3 = r0.currentMessageObject;
        r3 = r3.isSendError();
        if (r3 == 0) goto L_0x032a;
        r3 = r0.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 != 0) goto L_0x03ad;
        r3 = r0.timeX;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r3 = r3 + r4;
        r4 = r0.layoutHeight;
        r5 = 1101266944; // 0x41a40000 float:20.5 double:5.44098164E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = r0.rect;
        r6 = (float) r3;
        r7 = (float) r4;
        r8 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r9 = r9 + r3;
        r9 = (float) r9;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r12 = r12 + r4;
        r8 = (float) r12;
        r5.set(r6, r7, r9, r8);
        r5 = r0.rect;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r6 = (float) r6;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r7 = (float) r7;
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgErrorPaint;
        r1.drawRoundRect(r5, r6, r7, r8);
        r5 = org.telegram.ui.ActionBar.Theme.chat_msgErrorDrawable;
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r7 = r7 + r3;
        r6 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r8 = r8 + r4;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r5, r7, r8);
        r5 = org.telegram.ui.ActionBar.Theme.chat_msgErrorDrawable;
        r5.draw(r1);
        goto L_0x03ad;
        r3 = r0.currentMessageObject;
        r3 = r3.isOutOwner();
        if (r3 != 0) goto L_0x0356;
        r3 = r18.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x033b;
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgInViewsSelectedDrawable;
        goto L_0x033d;
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgInViewsDrawable;
        r4 = r0.timeX;
        r5 = r0.layoutHeight;
        r6 = 1083179008; // 0x40900000 float:4.5 double:5.35161536E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r6 = r0.timeLayout;
        r6 = r6.getHeight();
        r5 = r5 - r6;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r3, r4, r5);
        r3.draw(r1);
        goto L_0x0379;
        r3 = r18.isDrawSelectedBackground();
        if (r3 == 0) goto L_0x035f;
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgOutViewsSelectedDrawable;
        goto L_0x0361;
        r3 = org.telegram.ui.ActionBar.Theme.chat_msgOutViewsDrawable;
        r4 = r0.timeX;
        r5 = r0.layoutHeight;
        r6 = 1083179008; // 0x40900000 float:4.5 double:5.35161536E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r6 = r0.timeLayout;
        r6 = r6.getHeight();
        r5 = r5 - r6;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r3, r4, r5);
        r3.draw(r1);
        r3 = r0.viewsLayout;
        if (r3 == 0) goto L_0x03ad;
        r19.save();
        r3 = r0.timeX;
        r4 = org.telegram.ui.ActionBar.Theme.chat_msgInViewsDrawable;
        r4 = r4.getIntrinsicWidth();
        r3 = r3 + r4;
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r4;
        r3 = (float) r3;
        r4 = r0.layoutHeight;
        r5 = 1087373312; // 0x40d00000 float:6.5 double:5.372337977E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = r0.timeLayout;
        r5 = r5.getHeight();
        r4 = r4 - r5;
        r4 = (float) r4;
        r1.translate(r3, r4);
        r3 = r0.viewsLayout;
        r3.draw(r1);
        r19.restore();
        r19.save();
        r3 = r0.timeX;
        r3 = r3 + r2;
        r3 = (float) r3;
        r4 = r0.layoutHeight;
        r5 = 1087373312; // 0x40d00000 float:6.5 double:5.372337977E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = r0.timeLayout;
        r5 = r5.getHeight();
        r4 = r4 - r5;
        r4 = (float) r4;
        r1.translate(r3, r4);
        r3 = r0.timeLayout;
        r3.draw(r1);
        r19.restore();
        r2 = r0.currentMessageObject;
        r2 = r2.isOutOwner();
        if (r2 == 0) goto L_0x0743;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = r0.currentMessageObject;
        r6 = r6.getDialogId();
        r8 = 32;
        r6 = r6 >> r8;
        r6 = (int) r6;
        r7 = 1;
        if (r6 != r7) goto L_0x03eb;
        r11 = 1;
        r6 = r11;
        r7 = r0.currentMessageObject;
        r7 = r7.isSending();
        if (r7 == 0) goto L_0x03f9;
        r2 = 0;
        r3 = 0;
        r4 = 1;
        r5 = 0;
        goto L_0x041d;
        r7 = r0.currentMessageObject;
        r7 = r7.isSendError();
        if (r7 == 0) goto L_0x0406;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 1;
        goto L_0x041d;
        r7 = r0.currentMessageObject;
        r7 = r7.isSent();
        if (r7 == 0) goto L_0x041d;
        r7 = r0.currentMessageObject;
        r7 = r7.isUnread();
        if (r7 != 0) goto L_0x0419;
        r2 = 1;
        r3 = 1;
        goto L_0x041b;
        r2 = 0;
        r3 = 1;
        r4 = 0;
        r5 = 0;
        r7 = 1096286208; // 0x41580000 float:13.5 double:5.416373534E-315;
        if (r4 == 0) goto L_0x04b5;
        r8 = r0.mediaBackground;
        if (r8 == 0) goto L_0x048b;
        r8 = r0.captionLayout;
        if (r8 != 0) goto L_0x048b;
        r8 = r0.currentMessageObject;
        r8 = r8.type;
        r9 = 13;
        if (r8 == r9) goto L_0x0462;
        r8 = r0.currentMessageObject;
        r8 = r8.type;
        r9 = 5;
        if (r8 != r9) goto L_0x0439;
        goto L_0x0462;
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgMediaClockDrawable;
        r9 = r0.layoutWidth;
        r11 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgMediaClockDrawable;
        r11 = r11.getIntrinsicWidth();
        r9 = r9 - r11;
        r11 = r0.layoutHeight;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r11 = r11 - r12;
        r12 = org.telegram.ui.ActionBar.Theme.chat_msgMediaClockDrawable;
        r12 = r12.getIntrinsicHeight();
        r11 = r11 - r12;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r8, r9, r11);
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgMediaClockDrawable;
        r8.draw(r1);
        goto L_0x04b5;
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgStickerClockDrawable;
        r9 = r0.layoutWidth;
        r11 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgStickerClockDrawable;
        r11 = r11.getIntrinsicWidth();
        r9 = r9 - r11;
        r11 = r0.layoutHeight;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r11 = r11 - r12;
        r12 = org.telegram.ui.ActionBar.Theme.chat_msgStickerClockDrawable;
        r12 = r12.getIntrinsicHeight();
        r11 = r11 - r12;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r8, r9, r11);
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgStickerClockDrawable;
        r8.draw(r1);
        goto L_0x04b5;
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgOutClockDrawable;
        r9 = r0.layoutWidth;
        r11 = 1100218368; // 0x41940000 float:18.5 double:5.435800986E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgOutClockDrawable;
        r11 = r11.getIntrinsicWidth();
        r9 = r9 - r11;
        r11 = r0.layoutHeight;
        r12 = 1091043328; // 0x41080000 float:8.5 double:5.390470265E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r11 = r11 - r12;
        r12 = org.telegram.ui.ActionBar.Theme.chat_msgOutClockDrawable;
        r12 = r12.getIntrinsicHeight();
        r11 = r11 - r12;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r8, r9, r11);
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgOutClockDrawable;
        r8.draw(r1);
        r8 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        if (r6 == 0) goto L_0x051b;
        if (r2 != 0) goto L_0x04bd;
        if (r3 == 0) goto L_0x06d6;
        r7 = r0.mediaBackground;
        if (r7 == 0) goto L_0x04f1;
        r7 = r0.captionLayout;
        if (r7 != 0) goto L_0x04f1;
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgBroadcastMediaDrawable;
        r8 = r0.layoutWidth;
        r9 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 - r9;
        r9 = org.telegram.ui.ActionBar.Theme.chat_msgBroadcastMediaDrawable;
        r9 = r9.getIntrinsicWidth();
        r8 = r8 - r9;
        r9 = r0.layoutHeight;
        r11 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r12;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgBroadcastMediaDrawable;
        r11 = r11.getIntrinsicHeight();
        r9 = r9 - r11;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r7, r8, r9);
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgBroadcastMediaDrawable;
        r7.draw(r1);
        goto L_0x06d6;
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgBroadcastDrawable;
        r9 = r0.layoutWidth;
        r11 = 1101266944; // 0x41a40000 float:20.5 double:5.44098164E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgBroadcastDrawable;
        r11 = r11.getIntrinsicWidth();
        r9 = r9 - r11;
        r11 = r0.layoutHeight;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r11 = r11 - r8;
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgBroadcastDrawable;
        r8 = r8.getIntrinsicHeight();
        r11 = r11 - r8;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r7, r9, r11);
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgBroadcastDrawable;
        r7.draw(r1);
        goto L_0x06d6;
        r9 = 1101791232; // 0x41ac0000 float:21.5 double:5.443571966E-315;
        if (r3 == 0) goto L_0x0631;
        r11 = r0.mediaBackground;
        if (r11 == 0) goto L_0x05e6;
        r11 = r0.captionLayout;
        if (r11 != 0) goto L_0x05e6;
        r11 = r0.currentMessageObject;
        r11 = r11.type;
        r12 = 13;
        if (r11 == r12) goto L_0x0598;
        r11 = r0.currentMessageObject;
        r11 = r11.type;
        r12 = 5;
        if (r11 != r12) goto L_0x0537;
        goto L_0x0598;
        if (r2 == 0) goto L_0x055e;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgMediaCheckDrawable;
        r12 = r0.layoutWidth;
        r13 = 1104307814; // 0x41d26666 float:26.3 double:5.456005533E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r12 = r12 - r13;
        r13 = org.telegram.ui.ActionBar.Theme.chat_msgMediaCheckDrawable;
        r13 = r13.getIntrinsicWidth();
        r12 = r12 - r13;
        r13 = r0.layoutHeight;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r13 = r13 - r14;
        r14 = org.telegram.ui.ActionBar.Theme.chat_msgMediaCheckDrawable;
        r14 = r14.getIntrinsicHeight();
        r13 = r13 - r14;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r11, r12, r13);
        goto L_0x057f;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgMediaCheckDrawable;
        r12 = r0.layoutWidth;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r12 = r12 - r13;
        r13 = org.telegram.ui.ActionBar.Theme.chat_msgMediaCheckDrawable;
        r13 = r13.getIntrinsicWidth();
        r12 = r12 - r13;
        r13 = r0.layoutHeight;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r13 = r13 - r14;
        r14 = org.telegram.ui.ActionBar.Theme.chat_msgMediaCheckDrawable;
        r14 = r14.getIntrinsicHeight();
        r13 = r13 - r14;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r11, r12, r13);
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgMediaCheckDrawable;
        r12 = r0.timeAlpha;
        r13 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r12 = r12 * r13;
        r12 = (int) r12;
        r11.setAlpha(r12);
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgMediaCheckDrawable;
        r11.draw(r1);
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgMediaCheckDrawable;
        r12 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r11.setAlpha(r12);
        goto L_0x0631;
        if (r2 == 0) goto L_0x05bf;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgStickerCheckDrawable;
        r12 = r0.layoutWidth;
        r13 = 1104307814; // 0x41d26666 float:26.3 double:5.456005533E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r12 = r12 - r13;
        r13 = org.telegram.ui.ActionBar.Theme.chat_msgStickerCheckDrawable;
        r13 = r13.getIntrinsicWidth();
        r12 = r12 - r13;
        r13 = r0.layoutHeight;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r13 = r13 - r14;
        r14 = org.telegram.ui.ActionBar.Theme.chat_msgStickerCheckDrawable;
        r14 = r14.getIntrinsicHeight();
        r13 = r13 - r14;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r11, r12, r13);
        goto L_0x05e0;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgStickerCheckDrawable;
        r12 = r0.layoutWidth;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r12 = r12 - r13;
        r13 = org.telegram.ui.ActionBar.Theme.chat_msgStickerCheckDrawable;
        r13 = r13.getIntrinsicWidth();
        r12 = r12 - r13;
        r13 = r0.layoutHeight;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r13 = r13 - r14;
        r14 = org.telegram.ui.ActionBar.Theme.chat_msgStickerCheckDrawable;
        r14 = r14.getIntrinsicHeight();
        r13 = r13 - r14;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r11, r12, r13);
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgStickerCheckDrawable;
        r11.draw(r1);
        goto L_0x0631;
        r11 = r18.isDrawSelectedBackground();
        if (r11 == 0) goto L_0x05ef;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgOutCheckSelectedDrawable;
        goto L_0x05f1;
        r11 = org.telegram.ui.ActionBar.Theme.chat_msgOutCheckDrawable;
        if (r2 == 0) goto L_0x0611;
        r12 = r0.layoutWidth;
        r13 = 1102315520; // 0x41b40000 float:22.5 double:5.446162293E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r12 = r12 - r13;
        r13 = r11.getIntrinsicWidth();
        r12 = r12 - r13;
        r13 = r0.layoutHeight;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r13 = r13 - r14;
        r14 = r11.getIntrinsicHeight();
        r13 = r13 - r14;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r11, r12, r13);
        goto L_0x062e;
        r12 = r0.layoutWidth;
        r13 = 1100218368; // 0x41940000 float:18.5 double:5.435800986E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r12 = r12 - r13;
        r13 = r11.getIntrinsicWidth();
        r12 = r12 - r13;
        r13 = r0.layoutHeight;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r13 = r13 - r14;
        r14 = r11.getIntrinsicHeight();
        r13 = r13 - r14;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r11, r12, r13);
        r11.draw(r1);
        if (r2 == 0) goto L_0x06d6;
        r11 = r0.mediaBackground;
        if (r11 == 0) goto L_0x06ab;
        r11 = r0.captionLayout;
        if (r11 != 0) goto L_0x06ab;
        r8 = r0.currentMessageObject;
        r8 = r8.type;
        r11 = 13;
        if (r8 == r11) goto L_0x0684;
        r8 = r0.currentMessageObject;
        r8 = r8.type;
        r11 = 5;
        if (r8 != r11) goto L_0x064b;
        goto L_0x0684;
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgMediaHalfCheckDrawable;
        r11 = r0.layoutWidth;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r11 = r11 - r9;
        r9 = org.telegram.ui.ActionBar.Theme.chat_msgMediaHalfCheckDrawable;
        r9 = r9.getIntrinsicWidth();
        r11 = r11 - r9;
        r9 = r0.layoutHeight;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r9 = r9 - r7;
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgMediaHalfCheckDrawable;
        r7 = r7.getIntrinsicHeight();
        r9 = r9 - r7;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r8, r11, r9);
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgMediaHalfCheckDrawable;
        r8 = r0.timeAlpha;
        r9 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r8 = r8 * r9;
        r8 = (int) r8;
        r7.setAlpha(r8);
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgMediaHalfCheckDrawable;
        r7.draw(r1);
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgMediaHalfCheckDrawable;
        r8 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r7.setAlpha(r8);
        goto L_0x06d6;
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgStickerHalfCheckDrawable;
        r11 = r0.layoutWidth;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r11 = r11 - r9;
        r9 = org.telegram.ui.ActionBar.Theme.chat_msgStickerHalfCheckDrawable;
        r9 = r9.getIntrinsicWidth();
        r11 = r11 - r9;
        r9 = r0.layoutHeight;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r9 = r9 - r7;
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgStickerHalfCheckDrawable;
        r7 = r7.getIntrinsicHeight();
        r9 = r9 - r7;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r8, r11, r9);
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgStickerHalfCheckDrawable;
        r7.draw(r1);
        goto L_0x06d6;
        r7 = r18.isDrawSelectedBackground();
        if (r7 == 0) goto L_0x06b4;
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgOutHalfCheckSelectedDrawable;
        goto L_0x06b6;
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgOutHalfCheckDrawable;
        r9 = r0.layoutWidth;
        r11 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r11 = r7.getIntrinsicWidth();
        r9 = r9 - r11;
        r11 = r0.layoutHeight;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r11 = r11 - r8;
        r8 = r7.getIntrinsicHeight();
        r11 = r11 - r8;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r7, r9, r11);
        r7.draw(r1);
        if (r5 == 0) goto L_0x0743;
        r7 = r0.mediaBackground;
        if (r7 == 0) goto L_0x06f3;
        r7 = r0.captionLayout;
        if (r7 != 0) goto L_0x06f3;
        r7 = r0.layoutWidth;
        r8 = 1107951616; // 0x420a0000 float:34.5 double:5.474008307E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7 = r7 - r8;
        r8 = r0.layoutHeight;
        r9 = 1104412672; // 0x41d40000 float:26.5 double:5.4565236E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 - r9;
        goto L_0x0705;
        r7 = r0.layoutWidth;
        r8 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7 = r7 - r8;
        r8 = r0.layoutHeight;
        r9 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 - r9;
        r9 = r0.rect;
        r11 = (float) r7;
        r12 = (float) r8;
        r13 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r14 = r14 + r7;
        r14 = (float) r14;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r13 = r13 + r8;
        r13 = (float) r13;
        r9.set(r11, r12, r14, r13);
        r9 = r0.rect;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r11 = (float) r11;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r10 = (float) r10;
        r12 = org.telegram.ui.ActionBar.Theme.chat_msgErrorPaint;
        r1.drawRoundRect(r9, r11, r10, r12);
        r9 = org.telegram.ui.ActionBar.Theme.chat_msgErrorDrawable;
        r10 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r10 = r10 + r7;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r11 = r11 + r8;
        org.telegram.ui.Cells.BaseCell.setDrawableBounds(r9, r10, r11);
        r9 = org.telegram.ui.ActionBar.Theme.chat_msgErrorDrawable;
        r9.draw(r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatMessageCell.drawTimeLayout(android.graphics.Canvas):void");
    }

    public boolean onTouchEvent(android.view.MotionEvent r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ChatMessageCell.onTouchEvent(android.view.MotionEvent):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r16;
        r1 = r0.currentMessageObject;
        if (r1 == 0) goto L_0x0404;
    L_0x0006:
        r1 = r0.delegate;
        r1 = r1.canPerformActions();
        if (r1 != 0) goto L_0x0010;
    L_0x000e:
        goto L_0x0404;
    L_0x0010:
        r1 = 0;
        r0.disallowLongPress = r1;
        r2 = r16.checkTextBlockMotionEvent(r17);
        if (r2 != 0) goto L_0x001d;
    L_0x0019:
        r2 = r16.checkOtherButtonMotionEvent(r17);
    L_0x001d:
        if (r2 != 0) goto L_0x0023;
    L_0x001f:
        r2 = r16.checkCaptionMotionEvent(r17);
    L_0x0023:
        if (r2 != 0) goto L_0x0029;
    L_0x0025:
        r2 = r16.checkAudioMotionEvent(r17);
    L_0x0029:
        if (r2 != 0) goto L_0x002f;
    L_0x002b:
        r2 = r16.checkLinkPreviewMotionEvent(r17);
    L_0x002f:
        if (r2 != 0) goto L_0x0035;
    L_0x0031:
        r2 = r16.checkGameMotionEvent(r17);
    L_0x0035:
        if (r2 != 0) goto L_0x003b;
    L_0x0037:
        r2 = r16.checkPhotoImageMotionEvent(r17);
    L_0x003b:
        if (r2 != 0) goto L_0x0041;
    L_0x003d:
        r2 = r16.checkBotButtonMotionEvent(r17);
    L_0x0041:
        r3 = r17.getAction();
        r4 = 3;
        if (r3 != r4) goto L_0x0070;
    L_0x0048:
        r0.buttonPressed = r1;
        r0.miniButtonPressed = r1;
        r3 = -1;
        r0.pressedBotButton = r3;
        r0.linkPreviewPressed = r1;
        r0.otherPressed = r1;
        r0.imagePressed = r1;
        r0.gamePreviewPressed = r1;
        r0.instantButtonPressed = r1;
        r0.instantPressed = r1;
        r5 = android.os.Build.VERSION.SDK_INT;
        r6 = 21;
        if (r5 < r6) goto L_0x006c;
    L_0x0061:
        r5 = r0.instantViewSelectorDrawable;
        if (r5 == 0) goto L_0x006c;
    L_0x0065:
        r5 = r0.instantViewSelectorDrawable;
        r6 = android.util.StateSet.NOTHING;
        r5.setState(r6);
    L_0x006c:
        r2 = 0;
        r0.resetPressedLink(r3);
    L_0x0070:
        r3 = r0.disallowLongPress;
        if (r3 != 0) goto L_0x007f;
    L_0x0074:
        if (r2 == 0) goto L_0x007f;
    L_0x0076:
        r3 = r17.getAction();
        if (r3 != 0) goto L_0x007f;
    L_0x007c:
        r16.startCheckLongPress();
    L_0x007f:
        r3 = r17.getAction();
        r5 = 2;
        if (r3 == 0) goto L_0x008f;
    L_0x0086:
        r3 = r17.getAction();
        if (r3 == r5) goto L_0x008f;
    L_0x008c:
        r16.cancelCheckLongPress();
    L_0x008f:
        if (r2 != 0) goto L_0x0403;
    L_0x0091:
        r3 = r17.getX();
        r6 = r17.getY();
        r7 = r17.getAction();
        r8 = 1108082688; // 0x420c0000 float:35.0 double:5.47465589E-315;
        r9 = 5;
        r10 = 13;
        r11 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r12 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r13 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r14 = 1107296256; // 0x42000000 float:32.0 double:5.4707704E-315;
        r15 = 1;
        if (r7 != 0) goto L_0x01d7;
    L_0x00ad:
        r4 = r0.delegate;
        if (r4 == 0) goto L_0x00b9;
    L_0x00b1:
        r4 = r0.delegate;
        r4 = r4.canPerformActions();
        if (r4 == 0) goto L_0x0403;
    L_0x00b9:
        r4 = r0.isAvatarVisible;
        if (r4 == 0) goto L_0x00d0;
    L_0x00bd:
        r4 = r0.avatarImage;
        r5 = r16.getTop();
        r5 = (float) r5;
        r5 = r5 + r6;
        r4 = r4.isInsideImage(r3, r5);
        if (r4 == 0) goto L_0x00d0;
    L_0x00cb:
        r0.avatarPressed = r15;
        r2 = 1;
        goto L_0x01d0;
    L_0x00d0:
        r4 = r0.drawForwardedName;
        if (r4 == 0) goto L_0x0119;
    L_0x00d4:
        r4 = r0.forwardedNameLayout;
        r1 = r4[r1];
        if (r1 == 0) goto L_0x0119;
    L_0x00da:
        r1 = r0.forwardNameX;
        r1 = (float) r1;
        r1 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r1 < 0) goto L_0x0119;
    L_0x00e1:
        r1 = r0.forwardNameX;
        r4 = r0.forwardedNameWidth;
        r1 = r1 + r4;
        r1 = (float) r1;
        r1 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r1 > 0) goto L_0x0119;
    L_0x00eb:
        r1 = r0.forwardNameY;
        r1 = (float) r1;
        r1 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
        if (r1 < 0) goto L_0x0119;
    L_0x00f2:
        r1 = r0.forwardNameY;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r1 = r1 + r4;
        r1 = (float) r1;
        r1 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
        if (r1 > 0) goto L_0x0119;
    L_0x00fe:
        r1 = r0.viaWidth;
        if (r1 == 0) goto L_0x0114;
    L_0x0102:
        r1 = r0.forwardNameX;
        r4 = r0.viaNameWidth;
        r1 = r1 + r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r1 = r1 + r4;
        r1 = (float) r1;
        r1 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r1 < 0) goto L_0x0114;
    L_0x0111:
        r0.forwardBotPressed = r15;
        goto L_0x0116;
    L_0x0114:
        r0.forwardNamePressed = r15;
    L_0x0116:
        r2 = 1;
        goto L_0x01d0;
    L_0x0119:
        r1 = r0.drawNameLayout;
        if (r1 == 0) goto L_0x015a;
    L_0x011d:
        r1 = r0.nameLayout;
        if (r1 == 0) goto L_0x015a;
    L_0x0121:
        r1 = r0.viaWidth;
        if (r1 == 0) goto L_0x015a;
    L_0x0125:
        r1 = r0.nameX;
        r4 = r0.viaNameWidth;
        r4 = (float) r4;
        r1 = r1 + r4;
        r1 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r1 < 0) goto L_0x015a;
    L_0x012f:
        r1 = r0.nameX;
        r4 = r0.viaNameWidth;
        r4 = (float) r4;
        r1 = r1 + r4;
        r4 = r0.viaWidth;
        r4 = (float) r4;
        r1 = r1 + r4;
        r1 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r1 > 0) goto L_0x015a;
    L_0x013d:
        r1 = r0.nameY;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r4 = (float) r4;
        r1 = r1 - r4;
        r1 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
        if (r1 < 0) goto L_0x015a;
    L_0x0149:
        r1 = r0.nameY;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r4 = (float) r4;
        r1 = r1 + r4;
        r1 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
        if (r1 > 0) goto L_0x015a;
    L_0x0155:
        r0.forwardBotPressed = r15;
        r2 = 1;
        goto L_0x01d0;
    L_0x015a:
        r1 = r0.drawShareButton;
        if (r1 == 0) goto L_0x018b;
    L_0x015e:
        r1 = r0.shareStartX;
        r1 = (float) r1;
        r1 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r1 < 0) goto L_0x018b;
    L_0x0165:
        r1 = r0.shareStartX;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r1 = r1 + r4;
        r1 = (float) r1;
        r1 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r1 > 0) goto L_0x018b;
    L_0x0171:
        r1 = r0.shareStartY;
        r1 = (float) r1;
        r1 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
        if (r1 < 0) goto L_0x018b;
    L_0x0178:
        r1 = r0.shareStartY;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r1 = r1 + r4;
        r1 = (float) r1;
        r1 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1));
        if (r1 > 0) goto L_0x018b;
    L_0x0184:
        r0.sharePressed = r15;
        r2 = 1;
        r16.invalidate();
        goto L_0x01d0;
    L_0x018b:
        r1 = r0.replyNameLayout;
        if (r1 == 0) goto L_0x01d0;
    L_0x018f:
        r1 = r0.currentMessageObject;
        r1 = r1.type;
        if (r1 == r10) goto L_0x01a2;
    L_0x0195:
        r1 = r0.currentMessageObject;
        r1 = r1.type;
        if (r1 != r9) goto L_0x019c;
    L_0x019b:
        goto L_0x01a2;
    L_0x019c:
        r1 = r0.replyStartX;
        r4 = r0.backgroundDrawableRight;
        r1 = r1 + r4;
        goto L_0x01ad;
    L_0x01a2:
        r1 = r0.replyStartX;
        r4 = r0.replyNameWidth;
        r5 = r0.replyTextWidth;
        r4 = java.lang.Math.max(r4, r5);
        r1 = r1 + r4;
        r4 = r0.replyStartX;
        r4 = (float) r4;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x01d0;
        r4 = (float) r1;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 > 0) goto L_0x01d0;
        r4 = r0.replyStartY;
        r4 = (float) r4;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x01d0;
        r4 = r0.replyStartY;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r4 = r4 + r5;
        r4 = (float) r4;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 > 0) goto L_0x01d0;
        r0.replyPressed = r15;
        r2 = 1;
    L_0x01d0:
        if (r2 == 0) goto L_0x0403;
        r16.startCheckLongPress();
        goto L_0x0403;
    L_0x01d7:
        r7 = r17.getAction();
        if (r7 == r5) goto L_0x01e0;
        r16.cancelCheckLongPress();
        r7 = r0.avatarPressed;
        if (r7 == 0) goto L_0x0233;
        r7 = r17.getAction();
        if (r7 != r15) goto L_0x020d;
        r0.avatarPressed = r1;
        r0.playSoundEffect(r1);
        r4 = r0.delegate;
        if (r4 == 0) goto L_0x0403;
        r4 = r0.currentUser;
        if (r4 == 0) goto L_0x0200;
        r1 = r0.delegate;
        r4 = r0.currentUser;
        r1.didPressedUserAvatar(r0, r4);
        goto L_0x0403;
        r4 = r0.currentChat;
        if (r4 == 0) goto L_0x0403;
        r4 = r0.delegate;
        r5 = r0.currentChat;
        r4.didPressedChannelAvatar(r0, r5, r1);
        goto L_0x0403;
        r7 = r17.getAction();
        if (r7 != r4) goto L_0x0217;
        r0.avatarPressed = r1;
        goto L_0x0403;
        r4 = r17.getAction();
        if (r4 != r5) goto L_0x0403;
        r4 = r0.isAvatarVisible;
        if (r4 == 0) goto L_0x0403;
        r4 = r0.avatarImage;
        r5 = r16.getTop();
        r5 = (float) r5;
        r5 = r5 + r6;
        r4 = r4.isInsideImage(r3, r5);
        if (r4 != 0) goto L_0x0403;
        r0.avatarPressed = r1;
        goto L_0x0403;
        r7 = r0.forwardNamePressed;
        if (r7 == 0) goto L_0x02a0;
        r7 = r17.getAction();
        if (r7 != r15) goto L_0x0268;
        r0.forwardNamePressed = r1;
        r0.playSoundEffect(r1);
        r1 = r0.delegate;
        if (r1 == 0) goto L_0x0403;
        r1 = r0.currentForwardChannel;
        if (r1 == 0) goto L_0x025b;
        r1 = r0.delegate;
        r4 = r0.currentForwardChannel;
        r5 = r0.currentMessageObject;
        r5 = r5.messageOwner;
        r5 = r5.fwd_from;
        r5 = r5.channel_post;
        r1.didPressedChannelAvatar(r0, r4, r5);
        goto L_0x0403;
        r1 = r0.currentForwardUser;
        if (r1 == 0) goto L_0x0403;
        r1 = r0.delegate;
        r4 = r0.currentForwardUser;
        r1.didPressedUserAvatar(r0, r4);
        goto L_0x0403;
        r7 = r17.getAction();
        if (r7 != r4) goto L_0x0272;
        r0.forwardNamePressed = r1;
        goto L_0x0403;
        r4 = r17.getAction();
        if (r4 != r5) goto L_0x0403;
        r4 = r0.forwardNameX;
        r4 = (float) r4;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x029c;
        r4 = r0.forwardNameX;
        r5 = r0.forwardedNameWidth;
        r4 = r4 + r5;
        r4 = (float) r4;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 > 0) goto L_0x029c;
        r4 = r0.forwardNameY;
        r4 = (float) r4;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x029c;
        r4 = r0.forwardNameY;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r4 = r4 + r5;
        r4 = (float) r4;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x0403;
        r0.forwardNamePressed = r1;
        goto L_0x0403;
        r7 = r0.forwardBotPressed;
        if (r7 == 0) goto L_0x033f;
        r7 = r17.getAction();
        if (r7 != r15) goto L_0x02c9;
        r0.forwardBotPressed = r1;
        r0.playSoundEffect(r1);
        r1 = r0.delegate;
        if (r1 == 0) goto L_0x0403;
        r1 = r0.delegate;
        r4 = r0.currentViaBotUser;
        if (r4 == 0) goto L_0x02be;
        r4 = r0.currentViaBotUser;
        r4 = r4.username;
        goto L_0x02c4;
        r4 = r0.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.via_bot_name;
        r1.didPressedViaBot(r0, r4);
        goto L_0x0403;
        r7 = r17.getAction();
        if (r7 != r4) goto L_0x02d3;
        r0.forwardBotPressed = r1;
        goto L_0x0403;
        r4 = r17.getAction();
        if (r4 != r5) goto L_0x0403;
        r4 = r0.drawForwardedName;
        if (r4 == 0) goto L_0x030b;
        r4 = r0.forwardedNameLayout;
        r4 = r4[r1];
        if (r4 == 0) goto L_0x030b;
        r4 = r0.forwardNameX;
        r4 = (float) r4;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x0307;
        r4 = r0.forwardNameX;
        r5 = r0.forwardedNameWidth;
        r4 = r4 + r5;
        r4 = (float) r4;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 > 0) goto L_0x0307;
        r4 = r0.forwardNameY;
        r4 = (float) r4;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x0307;
        r4 = r0.forwardNameY;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r4 = r4 + r5;
        r4 = (float) r4;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x0403;
        r0.forwardBotPressed = r1;
        goto L_0x0403;
        r4 = r0.nameX;
        r5 = r0.viaNameWidth;
        r5 = (float) r5;
        r4 = r4 + r5;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x033b;
        r4 = r0.nameX;
        r5 = r0.viaNameWidth;
        r5 = (float) r5;
        r4 = r4 + r5;
        r5 = r0.viaWidth;
        r5 = (float) r5;
        r4 = r4 + r5;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 > 0) goto L_0x033b;
        r4 = r0.nameY;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r5 = (float) r5;
        r4 = r4 - r5;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x033b;
        r4 = r0.nameY;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r5 = (float) r5;
        r4 = r4 + r5;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x0403;
        r0.forwardBotPressed = r1;
        goto L_0x0403;
        r7 = r0.replyPressed;
        if (r7 == 0) goto L_0x03b0;
        r7 = r17.getAction();
        if (r7 != r15) goto L_0x035f;
        r0.replyPressed = r1;
        r0.playSoundEffect(r1);
        r1 = r0.delegate;
        if (r1 == 0) goto L_0x0403;
        r1 = r0.delegate;
        r4 = r0.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.reply_to_msg_id;
        r1.didPressedReplyMessage(r0, r4);
        goto L_0x0403;
        r7 = r17.getAction();
        if (r7 != r4) goto L_0x0369;
        r0.replyPressed = r1;
        goto L_0x0403;
        r4 = r17.getAction();
        if (r4 != r5) goto L_0x0403;
        r4 = r0.currentMessageObject;
        r4 = r4.type;
        if (r4 == r10) goto L_0x0382;
        r4 = r0.currentMessageObject;
        r4 = r4.type;
        if (r4 != r9) goto L_0x037c;
        goto L_0x0382;
        r4 = r0.replyStartX;
        r5 = r0.backgroundDrawableRight;
        r4 = r4 + r5;
        goto L_0x038d;
        r4 = r0.replyStartX;
        r5 = r0.replyNameWidth;
        r7 = r0.replyTextWidth;
        r5 = java.lang.Math.max(r5, r7);
        r4 = r4 + r5;
        r5 = r0.replyStartX;
        r5 = (float) r5;
        r5 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r5 < 0) goto L_0x03ad;
        r5 = (float) r4;
        r5 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r5 > 0) goto L_0x03ad;
        r5 = r0.replyStartY;
        r5 = (float) r5;
        r5 = (r6 > r5 ? 1 : (r6 == r5 ? 0 : -1));
        if (r5 < 0) goto L_0x03ad;
        r5 = r0.replyStartY;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 + r7;
        r5 = (float) r5;
        r5 = (r6 > r5 ? 1 : (r6 == r5 ? 0 : -1));
        if (r5 <= 0) goto L_0x03af;
        r0.replyPressed = r1;
        goto L_0x0403;
        r7 = r0.sharePressed;
        if (r7 == 0) goto L_0x0403;
        r7 = r17.getAction();
        if (r7 != r15) goto L_0x03c9;
        r0.sharePressed = r1;
        r0.playSoundEffect(r1);
        r1 = r0.delegate;
        if (r1 == 0) goto L_0x0400;
        r1 = r0.delegate;
        r1.didPressedShare(r0);
        goto L_0x0400;
        r7 = r17.getAction();
        if (r7 != r4) goto L_0x03d2;
        r0.sharePressed = r1;
        goto L_0x0400;
        r4 = r17.getAction();
        if (r4 != r5) goto L_0x0400;
        r4 = r0.shareStartX;
        r4 = (float) r4;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x03fe;
        r4 = r0.shareStartX;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r4 = r4 + r5;
        r4 = (float) r4;
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 > 0) goto L_0x03fe;
        r4 = r0.shareStartY;
        r4 = (float) r4;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 < 0) goto L_0x03fe;
        r4 = r0.shareStartY;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r4 = r4 + r5;
        r4 = (float) r4;
        r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x0400;
        r0.sharePressed = r1;
        r16.invalidate();
    L_0x0403:
        return r2;
    L_0x0404:
        r1 = super.onTouchEvent(r17);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatMessageCell.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void setMessageObject(org.telegram.messenger.MessageObject r1, org.telegram.messenger.MessageObject.GroupedMessages r2, boolean r3, boolean r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ChatMessageCell.setMessageObject(org.telegram.messenger.MessageObject, org.telegram.messenger.MessageObject$GroupedMessages, boolean, boolean):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r1 = r125;
        r2 = r126;
        r3 = r127;
        r4 = r128;
        r5 = r129;
        r6 = r126.checkLayout();
        r7 = 0;
        if (r6 != 0) goto L_0x001d;
    L_0x0011:
        r6 = r1.currentPosition;
        if (r6 == 0) goto L_0x001f;
    L_0x0015:
        r6 = r1.lastHeight;
        r8 = org.telegram.messenger.AndroidUtilities.displaySize;
        r8 = r8.y;
        if (r6 == r8) goto L_0x001f;
    L_0x001d:
        r1.currentMessageObject = r7;
    L_0x001f:
        r6 = r1.currentMessageObject;
        r8 = 1;
        r9 = 0;
        if (r6 == 0) goto L_0x0034;
    L_0x0025:
        r6 = r1.currentMessageObject;
        r6 = r6.getId();
        r10 = r126.getId();
        if (r6 == r10) goto L_0x0032;
    L_0x0031:
        goto L_0x0034;
    L_0x0032:
        r6 = r9;
        goto L_0x0035;
    L_0x0034:
        r6 = r8;
    L_0x0035:
        r10 = r1.currentMessageObject;
        if (r10 != r2) goto L_0x0040;
    L_0x0039:
        r10 = r2.forceUpdate;
        if (r10 == 0) goto L_0x003e;
    L_0x003d:
        goto L_0x0040;
    L_0x003e:
        r10 = r9;
        goto L_0x0041;
    L_0x0040:
        r10 = r8;
    L_0x0041:
        r11 = r1.currentMessageObject;
        if (r11 != r2) goto L_0x0051;
    L_0x0045:
        r11 = r125.isUserDataChanged();
        if (r11 != 0) goto L_0x004f;
    L_0x004b:
        r11 = r1.photoNotSet;
        if (r11 == 0) goto L_0x0051;
    L_0x004f:
        r11 = r8;
        goto L_0x0052;
    L_0x0051:
        r11 = r9;
    L_0x0052:
        r12 = r1.currentMessagesGroup;
        if (r3 == r12) goto L_0x0058;
    L_0x0056:
        r12 = r8;
        goto L_0x0059;
    L_0x0058:
        r12 = r9;
    L_0x0059:
        if (r12 != 0) goto L_0x007b;
    L_0x005b:
        if (r3 == 0) goto L_0x007b;
    L_0x005d:
        r13 = r3.messages;
        r13 = r13.size();
        if (r13 <= r8) goto L_0x0072;
    L_0x0065:
        r13 = r1.currentMessagesGroup;
        r13 = r13.positions;
        r14 = r1.currentMessageObject;
        r13 = r13.get(r14);
        r13 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r13;
        goto L_0x0073;
    L_0x0072:
        r13 = r7;
    L_0x0073:
        r14 = r1.currentPosition;
        if (r13 == r14) goto L_0x0079;
    L_0x0077:
        r14 = r8;
        goto L_0x007a;
    L_0x0079:
        r14 = r9;
    L_0x007a:
        r12 = r14;
    L_0x007b:
        if (r10 != 0) goto L_0x009b;
    L_0x007d:
        if (r11 != 0) goto L_0x009b;
    L_0x007f:
        if (r12 != 0) goto L_0x009b;
    L_0x0081:
        r13 = r125.isPhotoDataChanged(r126);
        if (r13 != 0) goto L_0x009b;
    L_0x0087:
        r13 = r1.pinnedBottom;
        if (r13 != r4) goto L_0x009b;
    L_0x008b:
        r13 = r1.pinnedTop;
        if (r13 == r5) goto L_0x0090;
    L_0x008f:
        goto L_0x009b;
    L_0x0090:
        r5 = r2;
        r53 = r6;
        r34 = r10;
        r35 = r11;
        r37 = r12;
        goto L_0x3105;
    L_0x009b:
        r1.pinnedBottom = r4;
        r1.pinnedTop = r5;
        r13 = -2;
        r1.lastTime = r13;
        r1.isHighlightedAnimated = r9;
        r13 = -1;
        r1.widthBeforeNewTimeLine = r13;
        r1.currentMessageObject = r2;
        r1.currentMessagesGroup = r3;
        r14 = r1.currentMessagesGroup;
        if (r14 == 0) goto L_0x00ce;
    L_0x00af:
        r14 = r1.currentMessagesGroup;
        r14 = r14.posArray;
        r14 = r14.size();
        if (r14 <= r8) goto L_0x00ce;
    L_0x00b9:
        r14 = r1.currentMessagesGroup;
        r14 = r14.positions;
        r15 = r1.currentMessageObject;
        r14 = r14.get(r15);
        r14 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r14;
        r1.currentPosition = r14;
        r14 = r1.currentPosition;
        if (r14 != 0) goto L_0x00d2;
    L_0x00cb:
        r1.currentMessagesGroup = r7;
        goto L_0x00d2;
    L_0x00ce:
        r1.currentMessagesGroup = r7;
        r1.currentPosition = r7;
    L_0x00d2:
        r14 = r1.pinnedTop;
        if (r14 == 0) goto L_0x00e4;
    L_0x00d6:
        r14 = r1.currentPosition;
        if (r14 == 0) goto L_0x00e2;
    L_0x00da:
        r14 = r1.currentPosition;
        r14 = r14.flags;
        r14 = r14 & 4;
        if (r14 == 0) goto L_0x00e4;
    L_0x00e2:
        r14 = r8;
        goto L_0x00e5;
    L_0x00e4:
        r14 = r9;
    L_0x00e5:
        r1.drawPinnedTop = r14;
        r14 = r1.pinnedBottom;
        r15 = 8;
        if (r14 == 0) goto L_0x00fa;
    L_0x00ed:
        r14 = r1.currentPosition;
        if (r14 == 0) goto L_0x00f8;
    L_0x00f1:
        r14 = r1.currentPosition;
        r14 = r14.flags;
        r14 = r14 & r15;
        if (r14 == 0) goto L_0x00fa;
    L_0x00f8:
        r14 = r8;
        goto L_0x00fb;
    L_0x00fa:
        r14 = r9;
    L_0x00fb:
        r1.drawPinnedBottom = r14;
        r14 = r1.photoImage;
        r14.setCrossfadeWithOldImage(r9);
        r14 = r2.messageOwner;
        r14 = r14.send_state;
        r1.lastSendState = r14;
        r14 = r2.messageOwner;
        r14 = r14.destroyTime;
        r1.lastDeleteDate = r14;
        r14 = r2.messageOwner;
        r14 = r14.views;
        r1.lastViewsCount = r14;
        r1.isPressed = r9;
        r1.gamePreviewPressed = r9;
        r1.isCheckPressed = r8;
        r1.hasNewLineForTime = r9;
        r14 = r1.isChat;
        if (r14 == 0) goto L_0x0138;
    L_0x0120:
        r14 = r126.isOutOwner();
        if (r14 != 0) goto L_0x0138;
    L_0x0126:
        r14 = r126.needDrawAvatar();
        if (r14 == 0) goto L_0x0138;
    L_0x012c:
        r14 = r1.currentPosition;
        if (r14 == 0) goto L_0x0136;
    L_0x0130:
        r14 = r1.currentPosition;
        r14 = r14.edge;
        if (r14 == 0) goto L_0x0138;
    L_0x0136:
        r14 = r8;
        goto L_0x0139;
    L_0x0138:
        r14 = r9;
    L_0x0139:
        r1.isAvatarVisible = r14;
        r1.wasLayout = r9;
        r1.drwaShareGoIcon = r9;
        r1.groupPhotoInvisible = r9;
        r14 = r125.checkNeedDrawShareButton(r126);
        r1.drawShareButton = r14;
        r1.replyNameLayout = r7;
        r1.adminLayout = r7;
        r1.replyTextLayout = r7;
        r1.replyNameWidth = r9;
        r1.replyTextWidth = r9;
        r1.viaWidth = r9;
        r1.viaNameWidth = r9;
        r1.addedCaptionHeight = r9;
        r1.currentReplyPhoto = r7;
        r1.currentUser = r7;
        r1.currentChat = r7;
        r1.currentViaBotUser = r7;
        r1.drawNameLayout = r9;
        r14 = r1.scheduledInvalidate;
        if (r14 == 0) goto L_0x016c;
    L_0x0165:
        r14 = r1.invalidateRunnable;
        org.telegram.messenger.AndroidUtilities.cancelRunOnUIThread(r14);
        r1.scheduledInvalidate = r9;
    L_0x016c:
        r1.resetPressedLink(r13);
        r2.forceUpdate = r9;
        r1.drawPhotoImage = r9;
        r1.hasLinkPreview = r9;
        r1.hasOldCaptionPreview = r9;
        r1.hasGamePreview = r9;
        r1.hasInvoicePreview = r9;
        r1.instantButtonPressed = r9;
        r1.instantPressed = r9;
        r14 = android.os.Build.VERSION.SDK_INT;
        r15 = 21;
        if (r14 < r15) goto L_0x0195;
    L_0x0185:
        r14 = r1.instantViewSelectorDrawable;
        if (r14 == 0) goto L_0x0195;
    L_0x0189:
        r14 = r1.instantViewSelectorDrawable;
        r14.setVisible(r9, r9);
        r14 = r1.instantViewSelectorDrawable;
        r15 = android.util.StateSet.NOTHING;
        r14.setState(r15);
    L_0x0195:
        r1.linkPreviewPressed = r9;
        r1.buttonPressed = r9;
        r1.miniButtonPressed = r9;
        r1.pressedBotButton = r13;
        r1.linkPreviewHeight = r9;
        r1.mediaOffsetY = r9;
        r1.documentAttachType = r9;
        r1.documentAttach = r7;
        r1.descriptionLayout = r7;
        r1.titleLayout = r7;
        r1.videoInfoLayout = r7;
        r1.photosCountLayout = r7;
        r1.siteNameLayout = r7;
        r1.authorLayout = r7;
        r1.captionLayout = r7;
        r1.captionOffsetX = r9;
        r1.currentCaption = r7;
        r1.docTitleLayout = r7;
        r1.drawImageButton = r9;
        r1.currentPhotoObject = r7;
        r1.currentPhotoObjectThumb = r7;
        r1.currentPhotoFilter = r7;
        r1.infoLayout = r7;
        r1.cancelLoading = r9;
        r1.buttonState = r13;
        r1.miniButtonState = r13;
        r1.hasMiniProgress = r9;
        r1.currentUrl = r7;
        r1.photoNotSet = r9;
        r1.drawBackground = r8;
        r1.drawName = r9;
        r1.useSeekBarWaweform = r9;
        r1.drawInstantView = r9;
        r1.drawInstantViewType = r9;
        r1.drawForwardedName = r9;
        r1.mediaBackground = r9;
        r14 = 0;
        r1.availableTimeWidth = r9;
        r15 = r1.photoImage;
        r15.setForceLoading(r9);
        r15 = r1.photoImage;
        r15.setNeedsQualityThumb(r9);
        r15 = r1.photoImage;
        r15.setShouldGenerateQualityThumb(r9);
        r15 = r1.photoImage;
        r15.setAllowDecodeSingleFrame(r9);
        r15 = r1.photoImage;
        r15.setParentMessageObject(r7);
        r15 = r1.photoImage;
        r7 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r15.setRoundRadius(r7);
        if (r10 == 0) goto L_0x020c;
    L_0x0206:
        r1.firstVisibleBlockNum = r9;
        r1.lastVisibleBlockNum = r9;
        r1.needNewVisiblePart = r8;
    L_0x020c:
        r7 = r2.type;
        if (r7 != 0) goto L_0x165b;
    L_0x0210:
        r1.drawForwardedName = r8;
        r7 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r7 == 0) goto L_0x0256;
    L_0x0218:
        r7 = r1.isChat;
        if (r7 == 0) goto L_0x0237;
    L_0x021c:
        r7 = r126.isOutOwner();
        if (r7 != 0) goto L_0x0237;
    L_0x0222:
        r7 = r126.needDrawAvatar();
        if (r7 == 0) goto L_0x0237;
    L_0x0228:
        r7 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r9 = 1123287040; // 0x42f40000 float:122.0 double:5.54977537E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r7 = r7 - r9;
        r1.drawName = r8;
        goto L_0x02a2;
    L_0x0237:
        r7 = r2.messageOwner;
        r7 = r7.to_id;
        r7 = r7.channel_id;
        if (r7 == 0) goto L_0x0247;
    L_0x023f:
        r7 = r126.isOutOwner();
        if (r7 != 0) goto L_0x0247;
    L_0x0245:
        r7 = r8;
        goto L_0x0248;
    L_0x0247:
        r7 = 0;
    L_0x0248:
        r1.drawName = r7;
        r7 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r9 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r7 = r7 - r9;
        goto L_0x02a2;
    L_0x0256:
        r7 = r1.isChat;
        if (r7 == 0) goto L_0x027c;
    L_0x025a:
        r7 = r126.isOutOwner();
        if (r7 != 0) goto L_0x027c;
    L_0x0260:
        r7 = r126.needDrawAvatar();
        if (r7 == 0) goto L_0x027c;
    L_0x0266:
        r7 = org.telegram.messenger.AndroidUtilities.displaySize;
        r7 = r7.x;
        r9 = org.telegram.messenger.AndroidUtilities.displaySize;
        r9 = r9.y;
        r7 = java.lang.Math.min(r7, r9);
        r9 = 1123287040; // 0x42f40000 float:122.0 double:5.54977537E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r7 = r7 - r9;
        r1.drawName = r8;
        goto L_0x02a2;
    L_0x027c:
        r7 = org.telegram.messenger.AndroidUtilities.displaySize;
        r7 = r7.x;
        r9 = org.telegram.messenger.AndroidUtilities.displaySize;
        r9 = r9.y;
        r7 = java.lang.Math.min(r7, r9);
        r9 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r7 = r7 - r9;
        r9 = r2.messageOwner;
        r9 = r9.to_id;
        r9 = r9.channel_id;
        if (r9 == 0) goto L_0x029f;
    L_0x0297:
        r9 = r126.isOutOwner();
        if (r9 != 0) goto L_0x029f;
    L_0x029d:
        r9 = r8;
        goto L_0x02a0;
    L_0x029f:
        r9 = 0;
    L_0x02a0:
        r1.drawName = r9;
    L_0x02a2:
        r1.availableTimeWidth = r7;
        r9 = r126.isRoundVideo();
        if (r9 == 0) goto L_0x02d1;
    L_0x02aa:
        r9 = r1.availableTimeWidth;
        r21 = r14;
        r13 = (double) r9;
        r9 = org.telegram.ui.ActionBar.Theme.chat_audioTimePaint;
        r15 = "00:00";
        r9 = r9.measureText(r15);
        r8 = (double) r9;
        r8 = java.lang.Math.ceil(r8);
        r15 = r126.isOutOwner();
        if (r15 == 0) goto L_0x02c4;
    L_0x02c2:
        r15 = 0;
        goto L_0x02ca;
    L_0x02c4:
        r15 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
    L_0x02ca:
        r3 = (double) r15;
        r8 = r8 + r3;
        r13 = r13 - r8;
        r3 = (int) r13;
        r1.availableTimeWidth = r3;
        goto L_0x02d3;
    L_0x02d1:
        r21 = r14;
    L_0x02d3:
        r125.measureTime(r126);
        r3 = r1.timeWidth;
        r4 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r4;
        r4 = r126.isOutOwner();
        if (r4 == 0) goto L_0x02ec;
    L_0x02e5:
        r4 = 1101266944; // 0x41a40000 float:20.5 double:5.44098164E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 + r4;
    L_0x02ec:
        r4 = r2.messageOwner;
        r4 = r4.media;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r4 == 0) goto L_0x0300;
    L_0x02f4:
        r4 = r2.messageOwner;
        r4 = r4.media;
        r4 = r4.game;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_game;
        if (r4 == 0) goto L_0x0300;
    L_0x02fe:
        r4 = 1;
        goto L_0x0301;
    L_0x0300:
        r4 = 0;
    L_0x0301:
        r1.hasGamePreview = r4;
        r4 = r2.messageOwner;
        r4 = r4.media;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaInvoice;
        r1.hasInvoicePreview = r4;
        r4 = r2.messageOwner;
        r4 = r4.media;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        if (r4 == 0) goto L_0x031f;
    L_0x0313:
        r4 = r2.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_webPage;
        if (r4 == 0) goto L_0x031f;
    L_0x031d:
        r4 = 1;
        goto L_0x0320;
    L_0x031f:
        r4 = 0;
    L_0x0320:
        r1.hasLinkPreview = r4;
        r4 = r1.hasLinkPreview;
        if (r4 == 0) goto L_0x0332;
    L_0x0326:
        r4 = r2.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r4 = r4.cached_page;
        if (r4 == 0) goto L_0x0332;
    L_0x0330:
        r4 = 1;
        goto L_0x0333;
    L_0x0332:
        r4 = 0;
    L_0x0333:
        r1.drawInstantView = r4;
        r4 = 0;
        r8 = r1.hasLinkPreview;
        if (r8 == 0) goto L_0x0343;
    L_0x033a:
        r8 = r2.messageOwner;
        r8 = r8.media;
        r8 = r8.webpage;
        r8 = r8.site_name;
        goto L_0x0344;
    L_0x0343:
        r8 = 0;
    L_0x0344:
        r9 = r1.hasLinkPreview;
        if (r9 == 0) goto L_0x0351;
    L_0x0348:
        r9 = r2.messageOwner;
        r9 = r9.media;
        r9 = r9.webpage;
        r9 = r9.type;
        goto L_0x0352;
    L_0x0351:
        r9 = 0;
    L_0x0352:
        r13 = r1.drawInstantView;
        if (r13 != 0) goto L_0x0384;
    L_0x0356:
        r13 = "telegram_channel";
        r13 = r13.equals(r9);
        if (r13 == 0) goto L_0x0367;
    L_0x035e:
        r13 = 1;
        r1.drawInstantView = r13;
        r1.drawInstantViewType = r13;
    L_0x0363:
        r33 = r9;
        goto L_0x0469;
    L_0x0367:
        r13 = 1;
        r14 = "telegram_megagroup";
        r14 = r14.equals(r9);
        if (r14 == 0) goto L_0x0376;
    L_0x0370:
        r1.drawInstantView = r13;
        r14 = 2;
        r1.drawInstantViewType = r14;
        goto L_0x0363;
    L_0x0376:
        r14 = "telegram_message";
        r14 = r14.equals(r9);
        if (r14 == 0) goto L_0x0363;
    L_0x037e:
        r1.drawInstantView = r13;
        r13 = 3;
        r1.drawInstantViewType = r13;
        goto L_0x0363;
    L_0x0384:
        if (r8 == 0) goto L_0x0467;
    L_0x0386:
        r8 = r8.toLowerCase();
        r13 = "instagram";
        r13 = r8.equals(r13);
        if (r13 != 0) goto L_0x03a9;
    L_0x0392:
        r13 = "twitter";
        r13 = r8.equals(r13);
        if (r13 != 0) goto L_0x03a9;
    L_0x039a:
        r13 = "telegram_album";
        r13 = r13.equals(r9);
        if (r13 == 0) goto L_0x03a3;
    L_0x03a2:
        goto L_0x03a9;
    L_0x03a3:
        r24 = r8;
        r33 = r9;
        goto L_0x046b;
    L_0x03a9:
        r13 = r2.messageOwner;
        r13 = r13.media;
        r13 = r13.webpage;
        r13 = r13.cached_page;
        r13 = r13 instanceof org.telegram.tgnet.TLRPC.TL_pageFull;
        if (r13 == 0) goto L_0x0462;
    L_0x03b5:
        r13 = r2.messageOwner;
        r13 = r13.media;
        r13 = r13.webpage;
        r13 = r13.photo;
        r13 = r13 instanceof org.telegram.tgnet.TLRPC.TL_photo;
        if (r13 != 0) goto L_0x03cf;
    L_0x03c1:
        r13 = r2.messageOwner;
        r13 = r13.media;
        r13 = r13.webpage;
        r13 = r13.document;
        r13 = org.telegram.messenger.MessageObject.isVideoDocument(r13);
        if (r13 == 0) goto L_0x03a3;
    L_0x03cf:
        r13 = 0;
        r1.drawInstantView = r13;
        r4 = 1;
        r13 = r2.messageOwner;
        r13 = r13.media;
        r13 = r13.webpage;
        r13 = r13.cached_page;
        r13 = r13.blocks;
        r14 = 1;
        r15 = r14;
        r14 = 0;
    L_0x03e0:
        r23 = r4;
        r4 = r13.size();
        if (r14 >= r4) goto L_0x0419;
    L_0x03e8:
        r4 = r13.get(r14);
        r4 = (org.telegram.tgnet.TLRPC.PageBlock) r4;
        r5 = r4 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow;
        if (r5 == 0) goto L_0x0400;
    L_0x03f2:
        r5 = r4;
        r5 = (org.telegram.tgnet.TLRPC.TL_pageBlockSlideshow) r5;
        r24 = r8;
        r8 = r5.items;
        r5 = r8.size();
        r15 = r5;
        goto L_0x0410;
    L_0x0400:
        r24 = r8;
        r5 = r4 instanceof org.telegram.tgnet.TLRPC.TL_pageBlockCollage;
        if (r5 == 0) goto L_0x0410;
    L_0x0406:
        r5 = r4;
        r5 = (org.telegram.tgnet.TLRPC.TL_pageBlockCollage) r5;
        r8 = r5.items;
        r4 = r8.size();
        r15 = r4;
    L_0x0410:
        r14 = r14 + 1;
        r4 = r23;
        r8 = r24;
        r5 = r129;
        goto L_0x03e0;
    L_0x0419:
        r24 = r8;
        r4 = "Of";
        r5 = 2131494029; // 0x7f0c048d float:1.8611555E38 double:1.053097974E-314;
        r8 = 2;
        r14 = new java.lang.Object[r8];
        r8 = 1;
        r16 = java.lang.Integer.valueOf(r8);
        r20 = 0;
        r14[r20] = r16;
        r16 = java.lang.Integer.valueOf(r15);
        r14[r8] = r16;
        r4 = org.telegram.messenger.LocaleController.formatString(r4, r5, r14);
        r5 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r5 = r5.measureText(r4);
        r33 = r9;
        r8 = (double) r5;
        r8 = java.lang.Math.ceil(r8);
        r5 = (int) r8;
        r1.photosCountWidth = r5;
        r5 = new android.text.StaticLayout;
        r27 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r8 = r1.photosCountWidth;
        r29 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r31 = 0;
        r32 = 0;
        r25 = r5;
        r26 = r4;
        r28 = r8;
        r25.<init>(r26, r27, r28, r29, r30, r31, r32);
        r1.photosCountLayout = r5;
        r4 = r23;
        goto L_0x046b;
    L_0x0462:
        r24 = r8;
        r33 = r9;
        goto L_0x046b;
    L_0x0467:
        r33 = r9;
    L_0x0469:
        r24 = r8;
    L_0x046b:
        r5 = android.os.Build.VERSION.SDK_INT;
        r8 = 21;
        if (r5 < r8) goto L_0x04e3;
    L_0x0471:
        r5 = r1.drawInstantView;
        if (r5 == 0) goto L_0x04e3;
    L_0x0475:
        r5 = r1.instantViewSelectorDrawable;
        if (r5 != 0) goto L_0x04c2;
    L_0x0479:
        r5 = new android.graphics.Paint;
        r8 = 1;
        r5.<init>(r8);
        r9 = -1;
        r5.setColor(r9);
        r9 = new org.telegram.ui.Cells.ChatMessageCell$2;
        r9.<init>(r5);
        r13 = new android.content.res.ColorStateList;
        r14 = new int[r8][];
        r15 = android.util.StateSet.WILD_CARD;
        r16 = 0;
        r14[r16] = r15;
        r15 = new int[r8];
        r8 = r1.currentMessageObject;
        r8 = r8.isOutOwner();
        if (r8 == 0) goto L_0x049f;
    L_0x049c:
        r8 = "chat_outPreviewInstantText";
        goto L_0x04a1;
    L_0x049f:
        r8 = "chat_inPreviewInstantText";
    L_0x04a1:
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r16 = 1610612735; // 0x5fffffff float:3.6893486E19 double:7.95748421E-315;
        r8 = r8 & r16;
        r16 = 0;
        r15[r16] = r8;
        r13.<init>(r14, r15);
        r8 = r13;
        r13 = new android.graphics.drawable.RippleDrawable;
        r14 = 0;
        r13.<init>(r8, r14, r9);
        r1.instantViewSelectorDrawable = r13;
        r13 = r1.instantViewSelectorDrawable;
        r13.setCallback(r1);
        r9 = 1;
        goto L_0x04dd;
    L_0x04c2:
        r5 = r1.instantViewSelectorDrawable;
        r8 = r1.currentMessageObject;
        r8 = r8.isOutOwner();
        if (r8 == 0) goto L_0x04cf;
    L_0x04cc:
        r8 = "chat_outPreviewInstantText";
        goto L_0x04d1;
    L_0x04cf:
        r8 = "chat_inPreviewInstantText";
    L_0x04d1:
        r8 = org.telegram.ui.ActionBar.Theme.getColor(r8);
        r9 = 1610612735; // 0x5fffffff float:3.6893486E19 double:7.95748421E-315;
        r8 = r8 & r9;
        r9 = 1;
        org.telegram.ui.ActionBar.Theme.setSelectorDrawableColor(r5, r8, r9);
    L_0x04dd:
        r5 = r1.instantViewSelectorDrawable;
        r8 = 0;
        r5.setVisible(r9, r8);
    L_0x04e3:
        r1.backgroundWidth = r7;
        r5 = r1.hasLinkPreview;
        if (r5 != 0) goto L_0x0522;
    L_0x04e9:
        r5 = r1.hasGamePreview;
        if (r5 != 0) goto L_0x0522;
    L_0x04ed:
        r5 = r1.hasInvoicePreview;
        if (r5 != 0) goto L_0x0522;
    L_0x04f1:
        r5 = r2.lastLineWidth;
        r5 = r7 - r5;
        if (r5 >= r3) goto L_0x04f8;
    L_0x04f7:
        goto L_0x0522;
    L_0x04f8:
        r5 = r1.backgroundWidth;
        r8 = r2.lastLineWidth;
        r5 = r5 - r8;
        if (r5 < 0) goto L_0x050f;
    L_0x04ff:
        if (r5 > r3) goto L_0x050f;
    L_0x0501:
        r8 = r1.backgroundWidth;
        r8 = r8 + r3;
        r8 = r8 - r5;
        r9 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 + r9;
        r1.backgroundWidth = r8;
        goto L_0x0544;
    L_0x050f:
        r8 = r1.backgroundWidth;
        r9 = r2.lastLineWidth;
        r9 = r9 + r3;
        r8 = java.lang.Math.max(r8, r9);
        r9 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 + r9;
        r1.backgroundWidth = r8;
        goto L_0x0544;
    L_0x0522:
        r5 = r1.backgroundWidth;
        r8 = r2.lastLineWidth;
        r5 = java.lang.Math.max(r5, r8);
        r8 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 + r8;
        r1.backgroundWidth = r5;
        r5 = r1.backgroundWidth;
        r8 = r1.timeWidth;
        r9 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 + r9;
        r5 = java.lang.Math.max(r5, r8);
        r1.backgroundWidth = r5;
    L_0x0544:
        r5 = r1.backgroundWidth;
        r8 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 - r8;
        r1.availableTimeWidth = r5;
        r5 = r126.isRoundVideo();
        if (r5 == 0) goto L_0x057e;
    L_0x0555:
        r5 = r1.availableTimeWidth;
        r8 = (double) r5;
        r5 = org.telegram.ui.ActionBar.Theme.chat_audioTimePaint;
        r13 = "00:00";
        r5 = r5.measureText(r13);
        r13 = (double) r5;
        r13 = java.lang.Math.ceil(r13);
        r5 = r126.isOutOwner();
        if (r5 == 0) goto L_0x056d;
    L_0x056b:
        r5 = 0;
        goto L_0x0573;
    L_0x056d:
        r5 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
    L_0x0573:
        r34 = r10;
        r35 = r11;
        r10 = (double) r5;
        r13 = r13 + r10;
        r8 = r8 - r13;
        r5 = (int) r8;
        r1.availableTimeWidth = r5;
        goto L_0x0582;
    L_0x057e:
        r34 = r10;
        r35 = r11;
    L_0x0582:
        r125.setMessageObjectInternal(r126);
        r5 = r2.textWidth;
        r8 = r1.hasGamePreview;
        if (r8 != 0) goto L_0x0592;
    L_0x058b:
        r8 = r1.hasInvoicePreview;
        if (r8 == 0) goto L_0x0590;
    L_0x058f:
        goto L_0x0592;
    L_0x0590:
        r9 = 0;
        goto L_0x0598;
    L_0x0592:
        r8 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
    L_0x0598:
        r5 = r5 + r9;
        r1.backgroundWidth = r5;
        r5 = r2.textHeight;
        r8 = 1100742656; // 0x419c0000 float:19.5 double:5.43839131E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 + r8;
        r8 = r1.namesOffset;
        r5 = r5 + r8;
        r1.totalHeight = r5;
        r5 = r1.drawPinnedTop;
        if (r5 == 0) goto L_0x05b8;
    L_0x05ad:
        r5 = r1.namesOffset;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 - r9;
        r1.namesOffset = r5;
    L_0x05b8:
        r5 = r1.backgroundWidth;
        r8 = r1.nameWidth;
        r5 = java.lang.Math.max(r5, r8);
        r8 = r1.forwardedNameWidth;
        r5 = java.lang.Math.max(r5, r8);
        r8 = r1.replyNameWidth;
        r5 = java.lang.Math.max(r5, r8);
        r8 = r1.replyTextWidth;
        r5 = java.lang.Math.max(r5, r8);
        r8 = 0;
        r9 = r1.hasLinkPreview;
        if (r9 != 0) goto L_0x05f2;
    L_0x05d7:
        r9 = r1.hasGamePreview;
        if (r9 != 0) goto L_0x05f2;
    L_0x05db:
        r9 = r1.hasInvoicePreview;
        if (r9 == 0) goto L_0x05e0;
    L_0x05df:
        goto L_0x05f2;
    L_0x05e0:
        r9 = r1.photoImage;
        r10 = 0;
        r11 = r10;
        r11 = (android.graphics.drawable.Drawable) r11;
        r9.setImageBitmap(r11);
        r1.calcBackgroundWidth(r7, r3, r5);
        r53 = r6;
        r37 = r12;
        goto L_0x1659;
    L_0x05f2:
        r9 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r9 == 0) goto L_0x0622;
    L_0x05f8:
        r9 = r1.isChat;
        if (r9 == 0) goto L_0x0616;
    L_0x05fc:
        r9 = r126.needDrawAvatar();
        if (r9 == 0) goto L_0x0616;
    L_0x0602:
        r9 = r1.currentMessageObject;
        r9 = r9.isOut();
        if (r9 != 0) goto L_0x0616;
    L_0x060a:
        r9 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r10 = 1124335616; // 0x43040000 float:132.0 double:5.554956023E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r10;
        goto L_0x0621;
    L_0x0616:
        r9 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r10 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r10;
    L_0x0621:
        goto L_0x065b;
    L_0x0622:
        r9 = r1.isChat;
        if (r9 == 0) goto L_0x0648;
    L_0x0626:
        r9 = r126.needDrawAvatar();
        if (r9 == 0) goto L_0x0648;
    L_0x062c:
        r9 = r1.currentMessageObject;
        r9 = r9.isOutOwner();
        if (r9 != 0) goto L_0x0648;
    L_0x0634:
        r9 = org.telegram.messenger.AndroidUtilities.displaySize;
        r9 = r9.x;
        r10 = org.telegram.messenger.AndroidUtilities.displaySize;
        r10 = r10.y;
        r9 = java.lang.Math.min(r9, r10);
        r10 = 1124335616; // 0x43040000 float:132.0 double:5.554956023E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r10;
        goto L_0x0621;
    L_0x0648:
        r9 = org.telegram.messenger.AndroidUtilities.displaySize;
        r9 = r9.x;
        r10 = org.telegram.messenger.AndroidUtilities.displaySize;
        r10 = r10.y;
        r9 = java.lang.Math.min(r9, r10);
        r10 = 1117782016; // 0x42a00000 float:80.0 double:5.522576936E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r10;
    L_0x065b:
        r10 = r1.drawShareButton;
        if (r10 == 0) goto L_0x0666;
    L_0x065f:
        r10 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r10;
    L_0x0666:
        r10 = r1.hasLinkPreview;
        if (r10 == 0) goto L_0x071f;
    L_0x066a:
        r10 = r2.messageOwner;
        r10 = r10.media;
        r10 = r10.webpage;
        r10 = (org.telegram.tgnet.TLRPC.TL_webPage) r10;
        r11 = r10.site_name;
        r13 = r10.title;
        r14 = r10.author;
        r15 = r10.description;
        r36 = r9;
        r9 = r10.photo;
        r16 = 0;
        r37 = r12;
        r12 = r10.document;
        r38 = r13;
        r13 = r10.type;
        r39 = r14;
        r14 = r10.duration;
        if (r11 == 0) goto L_0x06af;
    L_0x068e:
        if (r9 == 0) goto L_0x06af;
    L_0x0690:
        r40 = r9;
        r9 = r11.toLowerCase();
        r41 = r10;
        r10 = "instagram";
        r9 = r9.equals(r10);
        if (r9 == 0) goto L_0x06b3;
    L_0x06a0:
        r9 = org.telegram.messenger.AndroidUtilities.displaySize;
        r9 = r9.y;
        r10 = 3;
        r9 = r9 / r10;
        r10 = r1.currentMessageObject;
        r10 = r10.textWidth;
        r9 = java.lang.Math.max(r9, r10);
        goto L_0x06b5;
    L_0x06af:
        r40 = r9;
        r41 = r10;
    L_0x06b3:
        r9 = r36;
    L_0x06b5:
        if (r4 != 0) goto L_0x06d9;
    L_0x06b7:
        r10 = r1.drawInstantView;
        if (r10 != 0) goto L_0x06d9;
    L_0x06bb:
        if (r12 != 0) goto L_0x06d9;
    L_0x06bd:
        if (r13 == 0) goto L_0x06d9;
    L_0x06bf:
        r10 = "app";
        r10 = r13.equals(r10);
        if (r10 != 0) goto L_0x06d7;
    L_0x06c7:
        r10 = "profile";
        r10 = r13.equals(r10);
        if (r10 != 0) goto L_0x06d7;
    L_0x06cf:
        r10 = "article";
        r10 = r13.equals(r10);
        if (r10 == 0) goto L_0x06d9;
    L_0x06d7:
        r10 = 1;
        goto L_0x06da;
    L_0x06d9:
        r10 = 0;
    L_0x06da:
        if (r4 != 0) goto L_0x0708;
    L_0x06dc:
        r42 = r4;
        r4 = r1.drawInstantView;
        if (r4 != 0) goto L_0x070a;
    L_0x06e2:
        if (r12 != 0) goto L_0x070a;
    L_0x06e4:
        if (r15 == 0) goto L_0x070a;
    L_0x06e6:
        if (r13 == 0) goto L_0x070a;
    L_0x06e8:
        r4 = "app";
        r4 = r13.equals(r4);
        if (r4 != 0) goto L_0x0700;
    L_0x06f0:
        r4 = "profile";
        r4 = r13.equals(r4);
        if (r4 != 0) goto L_0x0700;
    L_0x06f8:
        r4 = "article";
        r4 = r13.equals(r4);
        if (r4 == 0) goto L_0x070a;
    L_0x0700:
        r4 = r1.currentMessageObject;
        r4 = r4.photoThumbs;
        if (r4 == 0) goto L_0x070a;
    L_0x0706:
        r4 = 1;
        goto L_0x070b;
    L_0x0708:
        r42 = r4;
    L_0x070a:
        r4 = 0;
    L_0x070b:
        r1.isSmallImage = r4;
        r53 = r6;
        r36 = r9;
        r4 = r13;
        r54 = r14;
        r6 = r16;
        r13 = r38;
        r14 = r39;
        r9 = r40;
        goto L_0x078b;
    L_0x071f:
        r42 = r4;
        r36 = r9;
        r37 = r12;
        r4 = r1.hasInvoicePreview;
        if (r4 == 0) goto L_0x075c;
    L_0x0729:
        r4 = r2.messageOwner;
        r4 = r4.media;
        r4 = (org.telegram.tgnet.TLRPC.TL_messageMediaInvoice) r4;
        r9 = r2.messageOwner;
        r9 = r9.media;
        r11 = r9.title;
        r13 = 0;
        r15 = 0;
        r9 = 0;
        r14 = 0;
        r12 = 0;
        r10 = r4.photo;
        r10 = r10 instanceof org.telegram.tgnet.TLRPC.TL_webDocument;
        if (r10 == 0) goto L_0x0745;
    L_0x0740:
        r10 = r4.photo;
        r10 = (org.telegram.tgnet.TLRPC.TL_webDocument) r10;
        goto L_0x0746;
    L_0x0745:
        r10 = 0;
    L_0x0746:
        r16 = r10;
        r10 = 0;
        r23 = "invoice";
        r43 = r4;
        r4 = 0;
        r1.isSmallImage = r4;
        r4 = 0;
        r53 = r6;
        r54 = r10;
        r6 = r16;
        r10 = r4;
        r4 = r23;
        goto L_0x078b;
    L_0x075c:
        r4 = r2.messageOwner;
        r4 = r4.media;
        r4 = r4.game;
        r11 = r4.title;
        r13 = 0;
        r16 = 0;
        r9 = r2.messageText;
        r9 = android.text.TextUtils.isEmpty(r9);
        if (r9 == 0) goto L_0x0772;
    L_0x076f:
        r9 = r4.description;
        goto L_0x0773;
    L_0x0772:
        r9 = 0;
    L_0x0773:
        r15 = r9;
        r9 = r4.photo;
        r14 = 0;
        r12 = r4.document;
        r10 = 0;
        r23 = "game";
        r44 = r4;
        r4 = 0;
        r1.isSmallImage = r4;
        r53 = r6;
        r54 = r10;
        r6 = r16;
        r4 = r23;
        r10 = 0;
    L_0x078b:
        r55 = r6;
        r6 = r1.hasInvoicePreview;
        if (r6 == 0) goto L_0x0794;
    L_0x0791:
        r16 = 0;
        goto L_0x079a;
    L_0x0794:
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r16 = org.telegram.messenger.AndroidUtilities.dp(r6);
    L_0x079a:
        r6 = r16;
        r16 = 3;
        r23 = 0;
        r56 = r4;
        r4 = r36 - r6;
        r57 = r3;
        r3 = r1.currentMessageObject;
        r3 = r3.photoThumbs;
        if (r3 != 0) goto L_0x07b7;
    L_0x07ac:
        if (r9 == 0) goto L_0x07b7;
    L_0x07ae:
        r3 = r1.currentMessageObject;
        r58 = r9;
        r9 = 1;
        r3.generateThumbs(r9);
        goto L_0x07b9;
    L_0x07b7:
        r58 = r9;
    L_0x07b9:
        if (r11 == 0) goto L_0x0835;
    L_0x07bb:
        r3 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x082d }
        r3 = r3.measureText(r11);	 Catch:{ Exception -> 0x082d }
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = r3 + r9;
        r59 = r10;
        r9 = (double) r3;
        r9 = java.lang.Math.ceil(r9);	 Catch:{ Exception -> 0x082a }
        r3 = (int) r9;	 Catch:{ Exception -> 0x082a }
        r9 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x082a }
        r27 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x082a }
        r28 = java.lang.Math.min(r3, r4);	 Catch:{ Exception -> 0x082a }
        r29 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x082a }
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x082a }
        r31 = 0;	 Catch:{ Exception -> 0x082a }
        r32 = 0;	 Catch:{ Exception -> 0x082a }
        r25 = r9;	 Catch:{ Exception -> 0x082a }
        r26 = r11;	 Catch:{ Exception -> 0x082a }
        r25.<init>(r26, r27, r28, r29, r30, r31, r32);	 Catch:{ Exception -> 0x082a }
        r1.siteNameLayout = r9;	 Catch:{ Exception -> 0x082a }
        r9 = r1.siteNameLayout;	 Catch:{ Exception -> 0x082a }
        r10 = 0;	 Catch:{ Exception -> 0x082a }
        r9 = r9.getLineLeft(r10);	 Catch:{ Exception -> 0x082a }
        r10 = 0;	 Catch:{ Exception -> 0x082a }
        r9 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1));	 Catch:{ Exception -> 0x082a }
        if (r9 == 0) goto L_0x07f3;	 Catch:{ Exception -> 0x082a }
    L_0x07f1:
        r9 = 1;	 Catch:{ Exception -> 0x082a }
        goto L_0x07f4;	 Catch:{ Exception -> 0x082a }
    L_0x07f3:
        r9 = 0;	 Catch:{ Exception -> 0x082a }
    L_0x07f4:
        r1.siteNameRtl = r9;	 Catch:{ Exception -> 0x082a }
        r9 = r1.siteNameLayout;	 Catch:{ Exception -> 0x082a }
        r10 = r1.siteNameLayout;	 Catch:{ Exception -> 0x082a }
        r10 = r10.getLineCount();	 Catch:{ Exception -> 0x082a }
        r22 = 1;	 Catch:{ Exception -> 0x082a }
        r10 = r10 + -1;	 Catch:{ Exception -> 0x082a }
        r9 = r9.getLineBottom(r10);	 Catch:{ Exception -> 0x082a }
        r10 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x082a }
        r10 = r10 + r9;	 Catch:{ Exception -> 0x082a }
        r1.linkPreviewHeight = r10;	 Catch:{ Exception -> 0x082a }
        r10 = r1.totalHeight;	 Catch:{ Exception -> 0x082a }
        r10 = r10 + r9;	 Catch:{ Exception -> 0x082a }
        r1.totalHeight = r10;	 Catch:{ Exception -> 0x082a }
        r23 = r23 + r9;	 Catch:{ Exception -> 0x082a }
        r10 = r1.siteNameLayout;	 Catch:{ Exception -> 0x082a }
        r10 = r10.getWidth();	 Catch:{ Exception -> 0x082a }
        r3 = r10;	 Catch:{ Exception -> 0x082a }
        r1.siteNameWidth = r10;	 Catch:{ Exception -> 0x082a }
        r10 = r3 + r6;	 Catch:{ Exception -> 0x082a }
        r10 = java.lang.Math.max(r5, r10);	 Catch:{ Exception -> 0x082a }
        r5 = r10;	 Catch:{ Exception -> 0x082a }
        r10 = r3 + r6;	 Catch:{ Exception -> 0x082a }
        r10 = java.lang.Math.max(r8, r10);	 Catch:{ Exception -> 0x082a }
        r8 = r10;
        goto L_0x0837;
    L_0x082a:
        r0 = move-exception;
        r3 = r0;
        goto L_0x0831;
    L_0x082d:
        r0 = move-exception;
        r59 = r10;
        r3 = r0;
    L_0x0831:
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x0837;
    L_0x0835:
        r59 = r10;
    L_0x0837:
        r3 = 0;
        if (r13 == 0) goto L_0x098b;
    L_0x083a:
        r9 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r1.titleX = r9;	 Catch:{ Exception -> 0x097b }
        r9 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x097b }
        if (r9 == 0) goto L_0x0866;
    L_0x0843:
        r9 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x085a }
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Exception -> 0x085a }
        r19 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x085a }
        r9 = r9 + r19;	 Catch:{ Exception -> 0x085a }
        r1.linkPreviewHeight = r9;	 Catch:{ Exception -> 0x085a }
        r9 = r1.totalHeight;	 Catch:{ Exception -> 0x085a }
        r25 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x085a }
        r9 = r9 + r25;	 Catch:{ Exception -> 0x085a }
        r1.totalHeight = r9;	 Catch:{ Exception -> 0x085a }
        goto L_0x0866;
    L_0x085a:
        r0 = move-exception;
        r60 = r3;
        r64 = r7;
        r63 = r11;
        r65 = r12;
    L_0x0863:
        r3 = r0;
        goto L_0x0985;
    L_0x0866:
        r9 = 0;
        r10 = r1.isSmallImage;	 Catch:{ Exception -> 0x097b }
        if (r10 == 0) goto L_0x0893;
    L_0x086b:
        if (r15 != 0) goto L_0x086e;
    L_0x086d:
        goto L_0x0893;
    L_0x086e:
        r9 = r16;
        r26 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x085a }
        r10 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;	 Catch:{ Exception -> 0x085a }
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x085a }
        r28 = r4 - r10;	 Catch:{ Exception -> 0x085a }
        r30 = 4;	 Catch:{ Exception -> 0x085a }
        r25 = r13;	 Catch:{ Exception -> 0x085a }
        r27 = r4;	 Catch:{ Exception -> 0x085a }
        r29 = r16;	 Catch:{ Exception -> 0x085a }
        r10 = generateStaticLayout(r25, r26, r27, r28, r29, r30);	 Catch:{ Exception -> 0x085a }
        r1.titleLayout = r10;	 Catch:{ Exception -> 0x085a }
        r10 = r1.titleLayout;	 Catch:{ Exception -> 0x085a }
        r10 = r10.getLineCount();	 Catch:{ Exception -> 0x085a }
        r16 = r16 - r10;
        r60 = r3;
        goto L_0x08b6;
    L_0x0893:
        r44 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x097b }
        r46 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x097b }
        r47 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r60 = r3;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x0972 }
        r3 = (float) r3;	 Catch:{ Exception -> 0x0972 }
        r49 = 0;	 Catch:{ Exception -> 0x0972 }
        r50 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x0972 }
        r52 = 4;	 Catch:{ Exception -> 0x0972 }
        r43 = r13;	 Catch:{ Exception -> 0x0972 }
        r45 = r4;	 Catch:{ Exception -> 0x0972 }
        r48 = r3;	 Catch:{ Exception -> 0x0972 }
        r51 = r4;	 Catch:{ Exception -> 0x0972 }
        r3 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r43, r44, r45, r46, r47, r48, r49, r50, r51, r52);	 Catch:{ Exception -> 0x0972 }
        r1.titleLayout = r3;	 Catch:{ Exception -> 0x0972 }
    L_0x08b6:
        r3 = r1.titleLayout;	 Catch:{ Exception -> 0x0972 }
        r10 = r1.titleLayout;	 Catch:{ Exception -> 0x0972 }
        r10 = r10.getLineCount();	 Catch:{ Exception -> 0x0972 }
        r22 = 1;	 Catch:{ Exception -> 0x0972 }
        r10 = r10 + -1;	 Catch:{ Exception -> 0x0972 }
        r3 = r3.getLineBottom(r10);	 Catch:{ Exception -> 0x0972 }
        r10 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0972 }
        r10 = r10 + r3;	 Catch:{ Exception -> 0x0972 }
        r1.linkPreviewHeight = r10;	 Catch:{ Exception -> 0x0972 }
        r10 = r1.totalHeight;	 Catch:{ Exception -> 0x0972 }
        r10 = r10 + r3;	 Catch:{ Exception -> 0x0972 }
        r1.totalHeight = r10;	 Catch:{ Exception -> 0x0972 }
        r10 = 1;
        r61 = r3;
        r3 = r8;
        r8 = r5;
        r5 = 0;
    L_0x08d6:
        r62 = r10;
        r10 = r1.titleLayout;	 Catch:{ Exception -> 0x0968 }
        r10 = r10.getLineCount();	 Catch:{ Exception -> 0x0968 }
        if (r5 >= r10) goto L_0x095f;	 Catch:{ Exception -> 0x0968 }
    L_0x08e0:
        r10 = r1.titleLayout;	 Catch:{ Exception -> 0x0968 }
        r10 = r10.getLineLeft(r5);	 Catch:{ Exception -> 0x0968 }
        r10 = (int) r10;
        if (r10 == 0) goto L_0x08eb;
    L_0x08e9:
        r60 = 1;
    L_0x08eb:
        r63 = r11;
        r11 = r1.titleX;	 Catch:{ Exception -> 0x0956 }
        r64 = r7;
        r7 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        if (r11 != r7) goto L_0x0901;
    L_0x08f6:
        r7 = -r10;
        r1.titleX = r7;	 Catch:{ Exception -> 0x08fa }
        goto L_0x090a;
    L_0x08fa:
        r0 = move-exception;
        r5 = r8;
        r65 = r12;
    L_0x08fe:
        r8 = r3;
        goto L_0x0863;
    L_0x0901:
        r7 = r1.titleX;	 Catch:{ Exception -> 0x094f }
        r11 = -r10;	 Catch:{ Exception -> 0x094f }
        r7 = java.lang.Math.max(r7, r11);	 Catch:{ Exception -> 0x094f }
        r1.titleX = r7;	 Catch:{ Exception -> 0x094f }
    L_0x090a:
        if (r10 == 0) goto L_0x0916;
    L_0x090c:
        r7 = r1.titleLayout;	 Catch:{ Exception -> 0x08fa }
        r7 = r7.getWidth();	 Catch:{ Exception -> 0x08fa }
        r7 = r7 - r10;
        r65 = r12;
        goto L_0x0924;
    L_0x0916:
        r7 = r1.titleLayout;	 Catch:{ Exception -> 0x094f }
        r7 = r7.getLineWidth(r5);	 Catch:{ Exception -> 0x094f }
        r65 = r12;
        r11 = (double) r7;
        r11 = java.lang.Math.ceil(r11);	 Catch:{ Exception -> 0x094c }
        r7 = (int) r11;	 Catch:{ Exception -> 0x094c }
    L_0x0924:
        if (r5 < r9) goto L_0x092c;	 Catch:{ Exception -> 0x094c }
    L_0x0926:
        if (r10 == 0) goto L_0x0933;	 Catch:{ Exception -> 0x094c }
    L_0x0928:
        r11 = r1.isSmallImage;	 Catch:{ Exception -> 0x094c }
        if (r11 == 0) goto L_0x0933;	 Catch:{ Exception -> 0x094c }
    L_0x092c:
        r11 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;	 Catch:{ Exception -> 0x094c }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);	 Catch:{ Exception -> 0x094c }
        r7 = r7 + r11;	 Catch:{ Exception -> 0x094c }
    L_0x0933:
        r11 = r7 + r6;	 Catch:{ Exception -> 0x094c }
        r11 = java.lang.Math.max(r8, r11);	 Catch:{ Exception -> 0x094c }
        r8 = r11;	 Catch:{ Exception -> 0x094c }
        r11 = r7 + r6;	 Catch:{ Exception -> 0x094c }
        r11 = java.lang.Math.max(r3, r11);	 Catch:{ Exception -> 0x094c }
        r3 = r11;
        r5 = r5 + 1;
        r10 = r62;
        r11 = r63;
        r7 = r64;
        r12 = r65;
        goto L_0x08d6;
    L_0x094c:
        r0 = move-exception;
        r5 = r8;
        goto L_0x08fe;
    L_0x094f:
        r0 = move-exception;
        r65 = r12;
        r5 = r8;
        r8 = r3;
        r3 = r0;
        goto L_0x0985;
    L_0x0956:
        r0 = move-exception;
        r64 = r7;
        r65 = r12;
        r5 = r8;
        r8 = r3;
        r3 = r0;
        goto L_0x0985;
    L_0x095f:
        r64 = r7;
        r63 = r11;
        r65 = r12;
        r5 = r8;
        r8 = r3;
        goto L_0x0988;
    L_0x0968:
        r0 = move-exception;
        r64 = r7;
        r63 = r11;
        r65 = r12;
        r5 = r8;
        r8 = r3;
        goto L_0x0979;
    L_0x0972:
        r0 = move-exception;
        r64 = r7;
        r63 = r11;
        r65 = r12;
    L_0x0979:
        r3 = r0;
        goto L_0x0985;
    L_0x097b:
        r0 = move-exception;
        r60 = r3;
        r64 = r7;
        r63 = r11;
        r65 = r12;
        r3 = r0;
    L_0x0985:
        org.telegram.messenger.FileLog.e(r3);
    L_0x0988:
        r3 = r16;
        goto L_0x0995;
    L_0x098b:
        r60 = r3;
        r64 = r7;
        r63 = r11;
        r65 = r12;
        r3 = r16;
    L_0x0995:
        r7 = 0;
        if (r14 == 0) goto L_0x0a45;
    L_0x0998:
        if (r13 != 0) goto L_0x0a45;
    L_0x099a:
        r9 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0a3c }
        if (r9 == 0) goto L_0x09b2;	 Catch:{ Exception -> 0x0a3c }
    L_0x099e:
        r9 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0a3c }
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Exception -> 0x0a3c }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x0a3c }
        r9 = r9 + r11;	 Catch:{ Exception -> 0x0a3c }
        r1.linkPreviewHeight = r9;	 Catch:{ Exception -> 0x0a3c }
        r9 = r1.totalHeight;	 Catch:{ Exception -> 0x0a3c }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x0a3c }
        r9 = r9 + r11;	 Catch:{ Exception -> 0x0a3c }
        r1.totalHeight = r9;	 Catch:{ Exception -> 0x0a3c }
    L_0x09b2:
        r9 = 3;	 Catch:{ Exception -> 0x0a3c }
        if (r3 != r9) goto L_0x09d3;	 Catch:{ Exception -> 0x0a3c }
    L_0x09b5:
        r9 = r1.isSmallImage;	 Catch:{ Exception -> 0x0a3c }
        if (r9 == 0) goto L_0x09bb;	 Catch:{ Exception -> 0x0a3c }
    L_0x09b9:
        if (r15 != 0) goto L_0x09d3;	 Catch:{ Exception -> 0x0a3c }
    L_0x09bb:
        r9 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0a3c }
        r27 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x0a3c }
        r29 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0a3c }
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0a3c }
        r31 = 0;	 Catch:{ Exception -> 0x0a3c }
        r32 = 0;	 Catch:{ Exception -> 0x0a3c }
        r25 = r9;	 Catch:{ Exception -> 0x0a3c }
        r26 = r14;	 Catch:{ Exception -> 0x0a3c }
        r28 = r4;	 Catch:{ Exception -> 0x0a3c }
        r25.<init>(r26, r27, r28, r29, r30, r31, r32);	 Catch:{ Exception -> 0x0a3c }
        r1.authorLayout = r9;	 Catch:{ Exception -> 0x0a3c }
        goto L_0x09f2;	 Catch:{ Exception -> 0x0a3c }
    L_0x09d3:
        r26 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x0a3c }
        r9 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;	 Catch:{ Exception -> 0x0a3c }
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);	 Catch:{ Exception -> 0x0a3c }
        r28 = r4 - r9;	 Catch:{ Exception -> 0x0a3c }
        r30 = 1;	 Catch:{ Exception -> 0x0a3c }
        r25 = r14;	 Catch:{ Exception -> 0x0a3c }
        r27 = r4;	 Catch:{ Exception -> 0x0a3c }
        r29 = r3;	 Catch:{ Exception -> 0x0a3c }
        r9 = generateStaticLayout(r25, r26, r27, r28, r29, r30);	 Catch:{ Exception -> 0x0a3c }
        r1.authorLayout = r9;	 Catch:{ Exception -> 0x0a3c }
        r9 = r1.authorLayout;	 Catch:{ Exception -> 0x0a3c }
        r9 = r9.getLineCount();	 Catch:{ Exception -> 0x0a3c }
        r3 = r3 - r9;	 Catch:{ Exception -> 0x0a3c }
    L_0x09f2:
        r9 = r1.authorLayout;	 Catch:{ Exception -> 0x0a3c }
        r10 = r1.authorLayout;	 Catch:{ Exception -> 0x0a3c }
        r10 = r10.getLineCount();	 Catch:{ Exception -> 0x0a3c }
        r11 = 1;	 Catch:{ Exception -> 0x0a3c }
        r10 = r10 - r11;	 Catch:{ Exception -> 0x0a3c }
        r9 = r9.getLineBottom(r10);	 Catch:{ Exception -> 0x0a3c }
        r10 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0a3c }
        r10 = r10 + r9;	 Catch:{ Exception -> 0x0a3c }
        r1.linkPreviewHeight = r10;	 Catch:{ Exception -> 0x0a3c }
        r10 = r1.totalHeight;	 Catch:{ Exception -> 0x0a3c }
        r10 = r10 + r9;	 Catch:{ Exception -> 0x0a3c }
        r1.totalHeight = r10;	 Catch:{ Exception -> 0x0a3c }
        r10 = r1.authorLayout;	 Catch:{ Exception -> 0x0a3c }
        r11 = 0;	 Catch:{ Exception -> 0x0a3c }
        r10 = r10.getLineLeft(r11);	 Catch:{ Exception -> 0x0a3c }
        r10 = (int) r10;	 Catch:{ Exception -> 0x0a3c }
        r11 = -r10;	 Catch:{ Exception -> 0x0a3c }
        r1.authorX = r11;	 Catch:{ Exception -> 0x0a3c }
        if (r10 == 0) goto L_0x0a20;	 Catch:{ Exception -> 0x0a3c }
    L_0x0a17:
        r11 = r1.authorLayout;	 Catch:{ Exception -> 0x0a3c }
        r11 = r11.getWidth();	 Catch:{ Exception -> 0x0a3c }
        r11 = r11 - r10;	 Catch:{ Exception -> 0x0a3c }
        r7 = 1;	 Catch:{ Exception -> 0x0a3c }
        goto L_0x0a2d;	 Catch:{ Exception -> 0x0a3c }
    L_0x0a20:
        r11 = r1.authorLayout;	 Catch:{ Exception -> 0x0a3c }
        r12 = 0;	 Catch:{ Exception -> 0x0a3c }
        r11 = r11.getLineWidth(r12);	 Catch:{ Exception -> 0x0a3c }
        r11 = (double) r11;	 Catch:{ Exception -> 0x0a3c }
        r11 = java.lang.Math.ceil(r11);	 Catch:{ Exception -> 0x0a3c }
        r11 = (int) r11;	 Catch:{ Exception -> 0x0a3c }
    L_0x0a2d:
        r12 = r11 + r6;	 Catch:{ Exception -> 0x0a3c }
        r12 = java.lang.Math.max(r5, r12);	 Catch:{ Exception -> 0x0a3c }
        r5 = r12;	 Catch:{ Exception -> 0x0a3c }
        r12 = r11 + r6;	 Catch:{ Exception -> 0x0a3c }
        r12 = java.lang.Math.max(r8, r12);	 Catch:{ Exception -> 0x0a3c }
        r8 = r12;
        goto L_0x0a45;
    L_0x0a3c:
        r0 = move-exception;
        r9 = r5;
        r5 = r3;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r3 = r5;
        r5 = r9;
    L_0x0a45:
        if (r15 == 0) goto L_0x0ba0;
    L_0x0a47:
        r9 = 0;
        r1.descriptionX = r9;	 Catch:{ Exception -> 0x0b96 }
        r9 = r1.currentMessageObject;	 Catch:{ Exception -> 0x0b96 }
        r9.generateLinkDescription();	 Catch:{ Exception -> 0x0b96 }
        r9 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0b96 }
        if (r9 == 0) goto L_0x0a70;
    L_0x0a53:
        r9 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0a68 }
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Exception -> 0x0a68 }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x0a68 }
        r9 = r9 + r11;	 Catch:{ Exception -> 0x0a68 }
        r1.linkPreviewHeight = r9;	 Catch:{ Exception -> 0x0a68 }
        r9 = r1.totalHeight;	 Catch:{ Exception -> 0x0a68 }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r10);	 Catch:{ Exception -> 0x0a68 }
        r9 = r9 + r11;	 Catch:{ Exception -> 0x0a68 }
        r1.totalHeight = r9;	 Catch:{ Exception -> 0x0a68 }
        goto L_0x0a70;	 Catch:{ Exception -> 0x0a68 }
    L_0x0a68:
        r0 = move-exception;	 Catch:{ Exception -> 0x0a68 }
        r66 = r3;	 Catch:{ Exception -> 0x0a68 }
        r67 = r4;	 Catch:{ Exception -> 0x0a68 }
    L_0x0a6d:
        r3 = r0;	 Catch:{ Exception -> 0x0a68 }
        goto L_0x0b9c;	 Catch:{ Exception -> 0x0a68 }
    L_0x0a70:
        r9 = 0;	 Catch:{ Exception -> 0x0a68 }
        r10 = 3;	 Catch:{ Exception -> 0x0a68 }
        if (r3 != r10) goto L_0x0a9c;	 Catch:{ Exception -> 0x0a68 }
    L_0x0a74:
        r10 = r1.isSmallImage;	 Catch:{ Exception -> 0x0a68 }
        if (r10 != 0) goto L_0x0a9c;	 Catch:{ Exception -> 0x0a68 }
    L_0x0a78:
        r10 = r2.linkDescription;	 Catch:{ Exception -> 0x0a68 }
        r44 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x0a68 }
        r46 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0a68 }
        r47 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0a68 }
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0a68 }
        r12 = org.telegram.messenger.AndroidUtilities.dp(r11);	 Catch:{ Exception -> 0x0a68 }
        r11 = (float) r12;	 Catch:{ Exception -> 0x0a68 }
        r49 = 0;	 Catch:{ Exception -> 0x0a68 }
        r50 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x0a68 }
        r52 = 6;	 Catch:{ Exception -> 0x0a68 }
        r43 = r10;	 Catch:{ Exception -> 0x0a68 }
        r45 = r4;	 Catch:{ Exception -> 0x0a68 }
        r48 = r11;	 Catch:{ Exception -> 0x0a68 }
        r51 = r4;	 Catch:{ Exception -> 0x0a68 }
        r10 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r43, r44, r45, r46, r47, r48, r49, r50, r51, r52);	 Catch:{ Exception -> 0x0a68 }
        r1.descriptionLayout = r10;	 Catch:{ Exception -> 0x0a68 }
        goto L_0x0ab7;
    L_0x0a9c:
        r9 = r3;
        r10 = r2.linkDescription;	 Catch:{ Exception -> 0x0b96 }
        r26 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x0b96 }
        r11 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;	 Catch:{ Exception -> 0x0b96 }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);	 Catch:{ Exception -> 0x0b96 }
        r28 = r4 - r11;	 Catch:{ Exception -> 0x0b96 }
        r30 = 6;	 Catch:{ Exception -> 0x0b96 }
        r25 = r10;	 Catch:{ Exception -> 0x0b96 }
        r27 = r4;	 Catch:{ Exception -> 0x0b96 }
        r29 = r3;	 Catch:{ Exception -> 0x0b96 }
        r10 = generateStaticLayout(r25, r26, r27, r28, r29, r30);	 Catch:{ Exception -> 0x0b96 }
        r1.descriptionLayout = r10;	 Catch:{ Exception -> 0x0b96 }
    L_0x0ab7:
        r10 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0b96 }
        r11 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0b96 }
        r11 = r11.getLineCount();	 Catch:{ Exception -> 0x0b96 }
        r12 = 1;	 Catch:{ Exception -> 0x0b96 }
        r11 = r11 - r12;	 Catch:{ Exception -> 0x0b96 }
        r10 = r10.getLineBottom(r11);	 Catch:{ Exception -> 0x0b96 }
        r11 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x0b96 }
        r11 = r11 + r10;	 Catch:{ Exception -> 0x0b96 }
        r1.linkPreviewHeight = r11;	 Catch:{ Exception -> 0x0b96 }
        r11 = r1.totalHeight;	 Catch:{ Exception -> 0x0b96 }
        r11 = r11 + r10;	 Catch:{ Exception -> 0x0b96 }
        r1.totalHeight = r11;	 Catch:{ Exception -> 0x0b96 }
        r11 = 0;
        r12 = r11;
        r11 = 0;
    L_0x0ad2:
        r66 = r3;
        r3 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0b91 }
        r3 = r3.getLineCount();	 Catch:{ Exception -> 0x0b91 }
        if (r11 >= r3) goto L_0x0b0b;	 Catch:{ Exception -> 0x0b91 }
    L_0x0adc:
        r3 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0b91 }
        r3 = r3.getLineLeft(r11);	 Catch:{ Exception -> 0x0b91 }
        r67 = r4;
        r3 = (double) r3;
        r3 = java.lang.Math.ceil(r3);	 Catch:{ Exception -> 0x0b8e }
        r3 = (int) r3;	 Catch:{ Exception -> 0x0b8e }
        if (r3 == 0) goto L_0x0b04;	 Catch:{ Exception -> 0x0b8e }
    L_0x0aec:
        r4 = 1;	 Catch:{ Exception -> 0x0b8e }
        r12 = r1.descriptionX;	 Catch:{ Exception -> 0x0b8e }
        if (r12 != 0) goto L_0x0af7;	 Catch:{ Exception -> 0x0b8e }
    L_0x0af1:
        r12 = -r3;	 Catch:{ Exception -> 0x0b8e }
        r1.descriptionX = r12;	 Catch:{ Exception -> 0x0b8e }
        r68 = r4;	 Catch:{ Exception -> 0x0b8e }
        goto L_0x0b02;	 Catch:{ Exception -> 0x0b8e }
    L_0x0af7:
        r12 = r1.descriptionX;	 Catch:{ Exception -> 0x0b8e }
        r68 = r4;	 Catch:{ Exception -> 0x0b8e }
        r4 = -r3;	 Catch:{ Exception -> 0x0b8e }
        r4 = java.lang.Math.max(r12, r4);	 Catch:{ Exception -> 0x0b8e }
        r1.descriptionX = r4;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b02:
        r12 = r68;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b04:
        r11 = r11 + 1;	 Catch:{ Exception -> 0x0b8e }
        r3 = r66;	 Catch:{ Exception -> 0x0b8e }
        r4 = r67;	 Catch:{ Exception -> 0x0b8e }
        goto L_0x0ad2;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b0b:
        r67 = r4;	 Catch:{ Exception -> 0x0b8e }
        r3 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0b8e }
        r3 = r3.getWidth();	 Catch:{ Exception -> 0x0b8e }
        r4 = 0;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b14:
        r11 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0b8e }
        r11 = r11.getLineCount();	 Catch:{ Exception -> 0x0b8e }
        if (r4 >= r11) goto L_0x0b8d;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b1c:
        r11 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0b8e }
        r11 = r11.getLineLeft(r4);	 Catch:{ Exception -> 0x0b8e }
        r69 = r10;	 Catch:{ Exception -> 0x0b8e }
        r10 = (double) r11;	 Catch:{ Exception -> 0x0b8e }
        r10 = java.lang.Math.ceil(r10);	 Catch:{ Exception -> 0x0b8e }
        r10 = (int) r10;	 Catch:{ Exception -> 0x0b8e }
        if (r10 != 0) goto L_0x0b33;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b2c:
        r11 = r1.descriptionX;	 Catch:{ Exception -> 0x0b8e }
        if (r11 == 0) goto L_0x0b33;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b30:
        r11 = 0;	 Catch:{ Exception -> 0x0b8e }
        r1.descriptionX = r11;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b33:
        if (r10 == 0) goto L_0x0b3a;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b35:
        r11 = r3 - r10;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b37:
        r70 = r12;	 Catch:{ Exception -> 0x0b8e }
        goto L_0x0b50;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b3a:
        if (r12 == 0) goto L_0x0b3e;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b3c:
        r11 = r3;	 Catch:{ Exception -> 0x0b8e }
        goto L_0x0b37;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b3e:
        r11 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0b8e }
        r11 = r11.getLineWidth(r4);	 Catch:{ Exception -> 0x0b8e }
        r70 = r12;	 Catch:{ Exception -> 0x0b8e }
        r11 = (double) r11;	 Catch:{ Exception -> 0x0b8e }
        r11 = java.lang.Math.ceil(r11);	 Catch:{ Exception -> 0x0b8e }
        r11 = (int) r11;	 Catch:{ Exception -> 0x0b8e }
        r11 = java.lang.Math.min(r11, r3);	 Catch:{ Exception -> 0x0b8e }
    L_0x0b50:
        if (r4 < r9) goto L_0x0b5a;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b52:
        if (r9 == 0) goto L_0x0b61;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b54:
        if (r10 == 0) goto L_0x0b61;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b56:
        r12 = r1.isSmallImage;	 Catch:{ Exception -> 0x0b8e }
        if (r12 == 0) goto L_0x0b61;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b5a:
        r12 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;	 Catch:{ Exception -> 0x0b8e }
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);	 Catch:{ Exception -> 0x0b8e }
        r11 = r11 + r12;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b61:
        r12 = r11 + r6;	 Catch:{ Exception -> 0x0b8e }
        if (r8 >= r12) goto L_0x0b7f;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b65:
        if (r60 == 0) goto L_0x0b71;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b67:
        r12 = r1.titleX;	 Catch:{ Exception -> 0x0b8e }
        r16 = r11 + r6;	 Catch:{ Exception -> 0x0b8e }
        r16 = r16 - r8;	 Catch:{ Exception -> 0x0b8e }
        r12 = r12 + r16;	 Catch:{ Exception -> 0x0b8e }
        r1.titleX = r12;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b71:
        if (r7 == 0) goto L_0x0b7d;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b73:
        r12 = r1.authorX;	 Catch:{ Exception -> 0x0b8e }
        r16 = r11 + r6;	 Catch:{ Exception -> 0x0b8e }
        r16 = r16 - r8;	 Catch:{ Exception -> 0x0b8e }
        r12 = r12 + r16;	 Catch:{ Exception -> 0x0b8e }
        r1.authorX = r12;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b7d:
        r8 = r11 + r6;	 Catch:{ Exception -> 0x0b8e }
    L_0x0b7f:
        r12 = r11 + r6;	 Catch:{ Exception -> 0x0b8e }
        r12 = java.lang.Math.max(r5, r12);	 Catch:{ Exception -> 0x0b8e }
        r5 = r12;
        r4 = r4 + 1;
        r10 = r69;
        r12 = r70;
        goto L_0x0b14;
    L_0x0b8d:
        goto L_0x0ba4;
    L_0x0b8e:
        r0 = move-exception;
        goto L_0x0a6d;
    L_0x0b91:
        r0 = move-exception;
        r67 = r4;
        r3 = r0;
        goto L_0x0b9c;
    L_0x0b96:
        r0 = move-exception;
        r66 = r3;
        r67 = r4;
        r3 = r0;
    L_0x0b9c:
        org.telegram.messenger.FileLog.e(r3);
        goto L_0x0ba4;
    L_0x0ba0:
        r66 = r3;
        r67 = r4;
    L_0x0ba4:
        if (r59 == 0) goto L_0x0bbc;
    L_0x0ba6:
        r3 = r1.descriptionLayout;
        if (r3 == 0) goto L_0x0bb7;
    L_0x0baa:
        r3 = r1.descriptionLayout;
        if (r3 == 0) goto L_0x0bbc;
    L_0x0bae:
        r3 = r1.descriptionLayout;
        r3 = r3.getLineCount();
        r4 = 1;
        if (r3 != r4) goto L_0x0bbc;
    L_0x0bb7:
        r10 = 0;
        r3 = 0;
        r1.isSmallImage = r3;
        goto L_0x0bbe;
    L_0x0bbc:
        r10 = r59;
    L_0x0bbe:
        if (r10 == 0) goto L_0x0bc7;
    L_0x0bc0:
        r3 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        goto L_0x0bc9;
    L_0x0bc7:
        r4 = r67;
    L_0x0bc9:
        r3 = r4;
        if (r65 == 0) goto L_0x0f5e;
    L_0x0bcc:
        r12 = r65;
        r4 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r12);
        if (r4 == 0) goto L_0x0bed;
    L_0x0bd4:
        r4 = r12.thumb;
        r1.currentPhotoObject = r4;
        r1.documentAttach = r12;
        r4 = 7;
        r1.documentAttachType = r4;
        r71 = r3;
    L_0x0bdf:
        r72 = r7;
        r74 = r8;
        r3 = r55;
        r7 = r56;
        r4 = r57;
        r75 = r64;
        goto L_0x0fc6;
    L_0x0bed:
        r4 = org.telegram.messenger.MessageObject.isGifDocument(r12);
        if (r4 == 0) goto L_0x0c76;
    L_0x0bf3:
        r4 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r4 != 0) goto L_0x0bfc;
    L_0x0bf7:
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2.gifState = r4;
        goto L_0x0bfe;
    L_0x0bfc:
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0bfe:
        r9 = r1.photoImage;
        r11 = r2.gifState;
        r11 = (r11 > r4 ? 1 : (r11 == r4 ? 0 : -1));
        if (r11 == 0) goto L_0x0c08;
    L_0x0c06:
        r4 = 1;
        goto L_0x0c09;
    L_0x0c08:
        r4 = 0;
    L_0x0c09:
        r9.setAllowStartAnimation(r4);
        r4 = r12.thumb;
        r1.currentPhotoObject = r4;
        r4 = r1.currentPhotoObject;
        if (r4 == 0) goto L_0x0c6d;
    L_0x0c14:
        r4 = r1.currentPhotoObject;
        r4 = r4.w;
        if (r4 == 0) goto L_0x0c24;
    L_0x0c1a:
        r4 = r1.currentPhotoObject;
        r4 = r4.h;
        if (r4 != 0) goto L_0x0c21;
    L_0x0c20:
        goto L_0x0c24;
    L_0x0c21:
        r71 = r3;
        goto L_0x0c6f;
    L_0x0c24:
        r4 = 0;
    L_0x0c25:
        r9 = r12.attributes;
        r9 = r9.size();
        if (r4 >= r9) goto L_0x0c50;
    L_0x0c2d:
        r9 = r12.attributes;
        r9 = r9.get(r4);
        r9 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r9;
        r11 = r9 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r11 != 0) goto L_0x0c41;
    L_0x0c39:
        r11 = r9 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r11 == 0) goto L_0x0c3e;
    L_0x0c3d:
        goto L_0x0c41;
    L_0x0c3e:
        r4 = r4 + 1;
        goto L_0x0c25;
    L_0x0c41:
        r11 = r1.currentPhotoObject;
        r71 = r3;
        r3 = r9.w;
        r11.w = r3;
        r3 = r1.currentPhotoObject;
        r11 = r9.h;
        r3.h = r11;
        goto L_0x0c52;
    L_0x0c50:
        r71 = r3;
    L_0x0c52:
        r3 = r1.currentPhotoObject;
        r3 = r3.w;
        if (r3 == 0) goto L_0x0c5e;
    L_0x0c58:
        r3 = r1.currentPhotoObject;
        r3 = r3.h;
        if (r3 != 0) goto L_0x0c6f;
    L_0x0c5e:
        r3 = r1.currentPhotoObject;
        r4 = r1.currentPhotoObject;
        r9 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4.h = r9;
        r3.w = r9;
        goto L_0x0c6f;
    L_0x0c6d:
        r71 = r3;
    L_0x0c6f:
        r1.documentAttach = r12;
        r3 = 2;
        r1.documentAttachType = r3;
        goto L_0x0bdf;
    L_0x0c76:
        r71 = r3;
        r3 = org.telegram.messenger.MessageObject.isVideoDocument(r12);
        if (r3 == 0) goto L_0x0cd7;
    L_0x0c7e:
        r3 = r12.thumb;
        r1.currentPhotoObject = r3;
        r3 = r1.currentPhotoObject;
        if (r3 == 0) goto L_0x0cd1;
    L_0x0c86:
        r3 = r1.currentPhotoObject;
        r3 = r3.w;
        if (r3 == 0) goto L_0x0c92;
    L_0x0c8c:
        r3 = r1.currentPhotoObject;
        r3 = r3.h;
        if (r3 != 0) goto L_0x0cd1;
    L_0x0c92:
        r3 = 0;
    L_0x0c93:
        r4 = r12.attributes;
        r4 = r4.size();
        if (r3 >= r4) goto L_0x0cb7;
    L_0x0c9b:
        r4 = r12.attributes;
        r4 = r4.get(r3);
        r4 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r4;
        r9 = r4 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r9 == 0) goto L_0x0cb4;
    L_0x0ca7:
        r9 = r1.currentPhotoObject;
        r11 = r4.w;
        r9.w = r11;
        r9 = r1.currentPhotoObject;
        r11 = r4.h;
        r9.h = r11;
        goto L_0x0cb7;
    L_0x0cb4:
        r3 = r3 + 1;
        goto L_0x0c93;
    L_0x0cb7:
        r3 = r1.currentPhotoObject;
        r3 = r3.w;
        if (r3 == 0) goto L_0x0cc3;
    L_0x0cbd:
        r3 = r1.currentPhotoObject;
        r3 = r3.h;
        if (r3 != 0) goto L_0x0cd1;
    L_0x0cc3:
        r3 = r1.currentPhotoObject;
        r4 = r1.currentPhotoObject;
        r9 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4.h = r9;
        r3.w = r9;
    L_0x0cd1:
        r3 = 0;
        r1.createDocumentLayout(r3, r2);
        goto L_0x0bdf;
    L_0x0cd7:
        r3 = org.telegram.messenger.MessageObject.isStickerDocument(r12);
        if (r3 == 0) goto L_0x0d37;
    L_0x0cdd:
        r3 = r12.thumb;
        r1.currentPhotoObject = r3;
        r3 = r1.currentPhotoObject;
        if (r3 == 0) goto L_0x0d30;
    L_0x0ce5:
        r3 = r1.currentPhotoObject;
        r3 = r3.w;
        if (r3 == 0) goto L_0x0cf1;
    L_0x0ceb:
        r3 = r1.currentPhotoObject;
        r3 = r3.h;
        if (r3 != 0) goto L_0x0d30;
    L_0x0cf1:
        r3 = 0;
    L_0x0cf2:
        r4 = r12.attributes;
        r4 = r4.size();
        if (r3 >= r4) goto L_0x0d16;
    L_0x0cfa:
        r4 = r12.attributes;
        r4 = r4.get(r3);
        r4 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r4;
        r9 = r4 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r9 == 0) goto L_0x0d13;
    L_0x0d06:
        r9 = r1.currentPhotoObject;
        r11 = r4.w;
        r9.w = r11;
        r9 = r1.currentPhotoObject;
        r11 = r4.h;
        r9.h = r11;
        goto L_0x0d16;
    L_0x0d13:
        r3 = r3 + 1;
        goto L_0x0cf2;
    L_0x0d16:
        r3 = r1.currentPhotoObject;
        r3 = r3.w;
        if (r3 == 0) goto L_0x0d22;
    L_0x0d1c:
        r3 = r1.currentPhotoObject;
        r3 = r3.h;
        if (r3 != 0) goto L_0x0d30;
    L_0x0d22:
        r3 = r1.currentPhotoObject;
        r4 = r1.currentPhotoObject;
        r9 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4.h = r9;
        r3.w = r9;
    L_0x0d30:
        r1.documentAttach = r12;
        r3 = 6;
        r1.documentAttachType = r3;
        goto L_0x0bdf;
    L_0x0d37:
        r4 = r57;
        r3 = r64;
        r1.calcBackgroundWidth(r3, r4, r5);
        r9 = org.telegram.messenger.MessageObject.isStickerDocument(r12);
        if (r9 != 0) goto L_0x0f52;
    L_0x0d44:
        r9 = r1.backgroundWidth;
        r11 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r11 = r11 + r3;
        if (r9 >= r11) goto L_0x0d58;
    L_0x0d4f:
        r9 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = r9 + r3;
        r1.backgroundWidth = r9;
    L_0x0d58:
        r9 = org.telegram.messenger.MessageObject.isVoiceDocument(r12);
        if (r9 == 0) goto L_0x0e14;
    L_0x0d5e:
        r9 = r1.backgroundWidth;
        r11 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r16 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r16;
        r1.createDocumentLayout(r9, r2);
        r9 = r1.currentMessageObject;
        r9 = r9.textHeight;
        r11 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 + r11;
        r11 = r1.linkPreviewHeight;
        r9 = r9 + r11;
        r1.mediaOffsetY = r9;
        r9 = r1.totalHeight;
        r11 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 + r11;
        r1.totalHeight = r9;
        r9 = r1.linkPreviewHeight;
        r11 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 + r11;
        r1.linkPreviewHeight = r9;
        r9 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r3 = r3 - r9;
        r9 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r9 == 0) goto L_0x0dd2;
    L_0x0d9e:
        r9 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r11 = r1.isChat;
        if (r11 == 0) goto L_0x0db5;
    L_0x0da6:
        r11 = r126.needDrawAvatar();
        if (r11 == 0) goto L_0x0db5;
    L_0x0dac:
        r11 = r126.isOutOwner();
        if (r11 != 0) goto L_0x0db5;
    L_0x0db2:
        r11 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        goto L_0x0db6;
    L_0x0db5:
        r11 = 0;
    L_0x0db6:
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r11 = 1130102784; // 0x435c0000 float:220.0 double:5.58344962E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = java.lang.Math.min(r9, r11);
        r11 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r9 = r9 + r6;
        r5 = java.lang.Math.max(r5, r9);
        goto L_0x0e05;
    L_0x0dd2:
        r9 = org.telegram.messenger.AndroidUtilities.displaySize;
        r9 = r9.x;
        r11 = r1.isChat;
        if (r11 == 0) goto L_0x0de9;
    L_0x0dda:
        r11 = r126.needDrawAvatar();
        if (r11 == 0) goto L_0x0de9;
    L_0x0de0:
        r11 = r126.isOutOwner();
        if (r11 != 0) goto L_0x0de9;
    L_0x0de6:
        r11 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        goto L_0x0dea;
    L_0x0de9:
        r11 = 0;
    L_0x0dea:
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r11 = 1130102784; // 0x435c0000 float:220.0 double:5.58344962E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = java.lang.Math.min(r9, r11);
        r11 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r9 = r9 + r6;
        r5 = java.lang.Math.max(r5, r9);
    L_0x0e05:
        r1.calcBackgroundWidth(r3, r4, r5);
        r72 = r7;
        r74 = r8;
        r7 = r56;
        r8 = r5;
        r5 = r3;
        r3 = r55;
        goto L_0x0fc9;
    L_0x0e14:
        r9 = org.telegram.messenger.MessageObject.isMusicDocument(r12);
        if (r9 == 0) goto L_0x0ec3;
    L_0x0e1a:
        r9 = r1.backgroundWidth;
        r11 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r16 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r16;
        r9 = r1.createDocumentLayout(r9, r2);
        r11 = r1.currentMessageObject;
        r11 = r11.textHeight;
        r72 = r7;
        r7 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r11 = r11 + r7;
        r7 = r1.linkPreviewHeight;
        r11 = r11 + r7;
        r1.mediaOffsetY = r11;
        r7 = r1.totalHeight;
        r11 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r7 = r7 + r11;
        r1.totalHeight = r7;
        r7 = r1.linkPreviewHeight;
        r11 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r7 = r7 + r11;
        r1.linkPreviewHeight = r7;
        r7 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = r3 - r11;
        r3 = r9 + r6;
        r11 = 1119617024; // 0x42bc0000 float:94.0 double:5.53164308E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3 = r3 + r11;
        r3 = java.lang.Math.max(r5, r3);
        r5 = r1.songLayout;
        if (r5 == 0) goto L_0x0e8d;
    L_0x0e69:
        r5 = r1.songLayout;
        r5 = r5.getLineCount();
        if (r5 <= 0) goto L_0x0e8d;
    L_0x0e71:
        r5 = (float) r3;
        r11 = r1.songLayout;
        r73 = r3;
        r3 = 0;
        r11 = r11.getLineWidth(r3);
        r3 = (float) r6;
        r11 = r11 + r3;
        r74 = r8;
        r3 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r3 = (float) r8;
        r11 = r11 + r3;
        r3 = java.lang.Math.max(r5, r11);
        r3 = (int) r3;
        goto L_0x0e93;
    L_0x0e8d:
        r73 = r3;
        r74 = r8;
        r3 = r73;
    L_0x0e93:
        r5 = r1.performerLayout;
        if (r5 == 0) goto L_0x0eb6;
    L_0x0e97:
        r5 = r1.performerLayout;
        r5 = r5.getLineCount();
        if (r5 <= 0) goto L_0x0eb6;
    L_0x0e9f:
        r5 = (float) r3;
        r8 = r1.performerLayout;
        r11 = 0;
        r8 = r8.getLineWidth(r11);
        r11 = (float) r6;
        r8 = r8 + r11;
        r11 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r11 = (float) r11;
        r8 = r8 + r11;
        r5 = java.lang.Math.max(r5, r8);
        r3 = (int) r5;
    L_0x0eb6:
        r5 = r3;
        r1.calcBackgroundWidth(r7, r4, r5);
        r8 = r5;
        r5 = r7;
        r3 = r55;
        r7 = r56;
        goto L_0x0fc9;
    L_0x0ec3:
        r72 = r7;
        r74 = r8;
        r7 = r1.backgroundWidth;
        r8 = 1126694912; // 0x43280000 float:168.0 double:5.566612494E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7 = r7 - r8;
        r1.createDocumentLayout(r7, r2);
        r7 = 1;
        r1.drawImageButton = r7;
        r7 = r1.drawPhotoImage;
        if (r7 == 0) goto L_0x0f0b;
    L_0x0eda:
        r7 = r1.totalHeight;
        r8 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7 = r7 + r8;
        r1.totalHeight = r7;
        r7 = r1.linkPreviewHeight;
        r8 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7 = r7 + r9;
        r1.linkPreviewHeight = r7;
        r7 = r1.photoImage;
        r9 = r1.totalHeight;
        r11 = r1.namesOffset;
        r9 = r9 + r11;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r75 = r3;
        r3 = 0;
        r7.setImageCoords(r3, r9, r11, r8);
    L_0x0f05:
        r3 = r55;
        r7 = r56;
        goto L_0x0fc6;
    L_0x0f0b:
        r75 = r3;
        r3 = r1.currentMessageObject;
        r3 = r3.textHeight;
        r7 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3 = r3 + r7;
        r7 = r1.linkPreviewHeight;
        r3 = r3 + r7;
        r1.mediaOffsetY = r3;
        r3 = r1.photoImage;
        r7 = r1.totalHeight;
        r8 = r1.namesOffset;
        r7 = r7 + r8;
        r8 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7 = r7 - r8;
        r8 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r9 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r11 = 0;
        r3.setImageCoords(r11, r7, r8, r9);
        r3 = r1.totalHeight;
        r7 = 1115684864; // 0x42800000 float:64.0 double:5.51221563E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3 = r3 + r7;
        r1.totalHeight = r3;
        r3 = r1.linkPreviewHeight;
        r7 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3 = r3 + r8;
        r1.linkPreviewHeight = r3;
        goto L_0x0f05;
    L_0x0f52:
        r75 = r3;
        r72 = r7;
        r74 = r8;
        r3 = r55;
        r7 = r56;
        goto L_0x0fc6;
    L_0x0f5e:
        r71 = r3;
        r72 = r7;
        r74 = r8;
        r4 = r57;
        r75 = r64;
        r12 = r65;
        if (r58 == 0) goto L_0x0fac;
    L_0x0f6c:
        if (r56 == 0) goto L_0x0f7a;
    L_0x0f6e:
        r3 = "photo";
        r7 = r56;
        r3 = r7.equals(r3);
        if (r3 == 0) goto L_0x0f7c;
    L_0x0f78:
        r3 = 1;
        goto L_0x0f7d;
    L_0x0f7a:
        r7 = r56;
    L_0x0f7c:
        r3 = 0;
    L_0x0f7d:
        r1.drawImageButton = r3;
        r3 = r2.photoThumbs;
        r8 = r1.drawImageButton;
        if (r8 == 0) goto L_0x0f8a;
    L_0x0f85:
        r8 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        goto L_0x0f8c;
    L_0x0f8a:
        r8 = r71;
    L_0x0f8c:
        r9 = r1.drawImageButton;
        r11 = 1;
        r9 = r9 ^ r11;
        r3 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r3, r8, r9);
        r1.currentPhotoObject = r3;
        r3 = r2.photoThumbs;
        r8 = 80;
        r3 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r3, r8);
        r1.currentPhotoObjectThumb = r3;
        r3 = r1.currentPhotoObjectThumb;
        r8 = r1.currentPhotoObject;
        if (r3 != r8) goto L_0x0fa9;
    L_0x0fa6:
        r3 = 0;
        r1.currentPhotoObjectThumb = r3;
    L_0x0fa9:
        r3 = r55;
        goto L_0x0fc6;
    L_0x0fac:
        r7 = r56;
        if (r55 == 0) goto L_0x0fc4;
    L_0x0fb0:
        r3 = r55;
        r8 = r3.mime_type;
        r9 = "image/";
        r8 = r8.startsWith(r9);
        if (r8 != 0) goto L_0x0fbd;
    L_0x0fbc:
        r3 = 0;
    L_0x0fbd:
        r8 = 0;
        r1.drawImageButton = r8;
        r8 = r5;
        r5 = r75;
        goto L_0x0fc9;
    L_0x0fc4:
        r3 = r55;
    L_0x0fc6:
        r8 = r5;
        r5 = r75;
    L_0x0fc9:
        r9 = r1.documentAttachType;
        r11 = 5;
        if (r9 == r11) goto L_0x158d;
    L_0x0fce:
        r9 = r1.documentAttachType;
        r11 = 3;
        if (r9 == r11) goto L_0x158d;
    L_0x0fd3:
        r9 = r1.documentAttachType;
        r11 = 1;
        if (r9 == r11) goto L_0x158d;
    L_0x0fd8:
        r9 = r1.currentPhotoObject;
        if (r9 != 0) goto L_0x1010;
    L_0x0fdc:
        if (r3 == 0) goto L_0x0fe1;
    L_0x0fde:
        r76 = r13;
        goto L_0x1012;
    L_0x0fe1:
        r9 = r1.photoImage;
        r76 = r13;
        r11 = 0;
        r13 = r11;
        r13 = (android.graphics.drawable.Drawable) r13;
        r9.setImageBitmap(r13);
        r9 = r1.linkPreviewHeight;
        r11 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 - r11;
        r1.linkPreviewHeight = r9;
        r9 = r1.totalHeight;
        r11 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 + r11;
        r1.totalHeight = r9;
        r84 = r3;
        r77 = r6;
        r81 = r10;
        r87 = r14;
        r83 = r63;
        r3 = r71;
        goto L_0x146c;
    L_0x1010:
        r76 = r13;
    L_0x1012:
        if (r7 == 0) goto L_0x1038;
    L_0x1014:
        r9 = "photo";
        r9 = r7.equals(r9);
        if (r9 != 0) goto L_0x1036;
    L_0x101c:
        r9 = "document";
        r9 = r7.equals(r9);
        if (r9 == 0) goto L_0x1029;
    L_0x1024:
        r9 = r1.documentAttachType;
        r11 = 6;
        if (r9 != r11) goto L_0x1036;
    L_0x1029:
        r9 = "gif";
        r9 = r7.equals(r9);
        if (r9 != 0) goto L_0x1036;
    L_0x1031:
        r9 = r1.documentAttachType;
        r11 = 4;
        if (r9 != r11) goto L_0x1038;
    L_0x1036:
        r9 = 1;
        goto L_0x1039;
    L_0x1038:
        r9 = 0;
    L_0x1039:
        r1.drawImageButton = r9;
        r9 = r1.linkPreviewHeight;
        if (r9 == 0) goto L_0x1053;
    L_0x103f:
        r9 = r1.linkPreviewHeight;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 + r13;
        r1.linkPreviewHeight = r9;
        r9 = r1.totalHeight;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r9 = r9 + r13;
        r1.totalHeight = r9;
    L_0x1053:
        r9 = r1.documentAttachType;
        r11 = 6;
        if (r9 != r11) goto L_0x1072;
    L_0x1058:
        r9 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r9 == 0) goto L_0x1068;
    L_0x105e:
        r9 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r9 = (float) r9;
        r11 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r9 = r9 * r11;
        r9 = (int) r9;
    L_0x1067:
        goto L_0x1082;
    L_0x1068:
        r9 = org.telegram.messenger.AndroidUtilities.displaySize;
        r9 = r9.x;
        r9 = (float) r9;
        r11 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r9 = r9 * r11;
        r9 = (int) r9;
        goto L_0x1067;
    L_0x1072:
        r9 = r1.documentAttachType;
        r11 = 7;
        if (r9 != r11) goto L_0x1080;
    L_0x1077:
        r9 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r11 = r1.photoImage;
        r13 = 1;
        r11.setAllowDecodeSingleFrame(r13);
        goto L_0x1082;
    L_0x1080:
        r9 = r71;
    L_0x1082:
        r11 = r1.hasInvoicePreview;
        if (r11 == 0) goto L_0x108d;
    L_0x1086:
        r11 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        goto L_0x108e;
    L_0x108d:
        r11 = 0;
    L_0x108e:
        r11 = r9 - r11;
        r11 = r11 + r6;
        r8 = java.lang.Math.max(r8, r11);
        r11 = r1.currentPhotoObject;
        if (r11 == 0) goto L_0x10a7;
    L_0x1099:
        r11 = r1.currentPhotoObject;
        r13 = -1;
        r11.size = r13;
        r11 = r1.currentPhotoObjectThumb;
        if (r11 == 0) goto L_0x10aa;
    L_0x10a2:
        r11 = r1.currentPhotoObjectThumb;
        r11.size = r13;
        goto L_0x10aa;
    L_0x10a7:
        r13 = -1;
        r3.size = r13;
    L_0x10aa:
        if (r10 != 0) goto L_0x114f;
    L_0x10ac:
        r11 = r1.documentAttachType;
        r13 = 7;
        if (r11 != r13) goto L_0x10bb;
    L_0x10b1:
        r77 = r6;
        r78 = r8;
        r81 = r10;
        r13 = r63;
        goto L_0x1157;
    L_0x10bb:
        r11 = r1.hasGamePreview;
        if (r11 != 0) goto L_0x1131;
    L_0x10bf:
        r11 = r1.hasInvoicePreview;
        if (r11 == 0) goto L_0x10cb;
    L_0x10c3:
        r77 = r6;
        r78 = r8;
        r13 = r63;
        goto L_0x1137;
    L_0x10cb:
        r11 = r1.currentPhotoObject;
        r11 = r11.w;
        r13 = r1.currentPhotoObject;
        r13 = r13.h;
        r77 = r6;
        r6 = (float) r11;
        r78 = r8;
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r16 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = r9 - r16;
        r8 = (float) r8;
        r6 = r6 / r8;
        r8 = (float) r11;
        r8 = r8 / r6;
        r8 = (int) r8;
        r11 = (float) r13;
        r11 = r11 / r6;
        r11 = (int) r11;
        if (r63 == 0) goto L_0x1118;
    L_0x10ea:
        if (r63 == 0) goto L_0x1103;
    L_0x10ec:
        r79 = r6;
        r13 = r63;
        r6 = r13.toLowerCase();
        r80 = r8;
        r8 = "instagram";
        r6 = r6.equals(r8);
        if (r6 != 0) goto L_0x1109;
    L_0x10fe:
        r6 = r1.documentAttachType;
        if (r6 != 0) goto L_0x1109;
    L_0x1102:
        goto L_0x111e;
    L_0x1103:
        r79 = r6;
        r80 = r8;
        r13 = r63;
    L_0x1109:
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r8 = 2;
        r6 = r6 / r8;
        if (r11 <= r6) goto L_0x112c;
    L_0x1111:
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r11 = r6 / 2;
        goto L_0x112c;
    L_0x1118:
        r79 = r6;
        r80 = r8;
        r13 = r63;
    L_0x111e:
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r8 = 3;
        r6 = r6 / r8;
        if (r11 <= r6) goto L_0x112c;
    L_0x1126:
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r11 = r6 / 3;
    L_0x112c:
        r81 = r10;
        r6 = r80;
        goto L_0x115a;
    L_0x1131:
        r77 = r6;
        r78 = r8;
        r13 = r63;
    L_0x1137:
        r6 = 640; // 0x280 float:8.97E-43 double:3.16E-321;
        r8 = 360; // 0x168 float:5.04E-43 double:1.78E-321;
        r11 = (float) r6;
        r81 = r10;
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r16 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r10 = r9 - r16;
        r10 = (float) r10;
        r11 = r11 / r10;
        r10 = (float) r6;
        r10 = r10 / r11;
        r6 = (int) r10;
        r10 = (float) r8;
        r10 = r10 / r11;
        r11 = (int) r10;
        goto L_0x115a;
    L_0x114f:
        r77 = r6;
        r78 = r8;
        r81 = r10;
        r13 = r63;
    L_0x1157:
        r11 = r9;
        r8 = r9;
        r6 = r8;
    L_0x115a:
        r8 = r1.isSmallImage;
        if (r8 == 0) goto L_0x119a;
    L_0x115e:
        r8 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r10 = r10 + r23;
        r8 = r1.linkPreviewHeight;
        if (r10 <= r8) goto L_0x118e;
    L_0x116a:
        r8 = r1.totalHeight;
        r10 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r16 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r16 = r16 + r23;
        r10 = r1.linkPreviewHeight;
        r16 = r16 - r10;
        r10 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r16 = r16 + r10;
        r8 = r8 + r16;
        r1.totalHeight = r8;
        r8 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = r8 + r23;
        r1.linkPreviewHeight = r8;
    L_0x118e:
        r8 = r1.linkPreviewHeight;
        r10 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r8 = r8 - r10;
        r1.linkPreviewHeight = r8;
        goto L_0x11ab;
    L_0x119a:
        r8 = r1.totalHeight;
        r10 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r10 = r10 + r11;
        r8 = r8 + r10;
        r1.totalHeight = r8;
        r8 = r1.linkPreviewHeight;
        r8 = r8 + r11;
        r1.linkPreviewHeight = r8;
    L_0x11ab:
        r8 = r1.photoImage;
        r10 = 0;
        r8.setImageCoords(r10, r10, r6, r11);
        r8 = java.util.Locale.US;
        r10 = "%d_%d";
        r82 = r9;
        r83 = r13;
        r9 = 2;
        r13 = new java.lang.Object[r9];
        r16 = java.lang.Integer.valueOf(r6);
        r17 = 0;
        r13[r17] = r16;
        r16 = java.lang.Integer.valueOf(r11);
        r18 = 1;
        r13[r18] = r16;
        r8 = java.lang.String.format(r8, r10, r13);
        r1.currentPhotoFilter = r8;
        r8 = java.util.Locale.US;
        r10 = "%d_%d_b";
        r13 = new java.lang.Object[r9];
        r9 = java.lang.Integer.valueOf(r6);
        r13[r17] = r9;
        r9 = java.lang.Integer.valueOf(r11);
        r13[r18] = r9;
        r8 = java.lang.String.format(r8, r10, r13);
        r1.currentPhotoFilterThumb = r8;
        if (r3 == 0) goto L_0x120f;
    L_0x11ec:
        r8 = r1.photoImage;
        r45 = 0;
        r9 = r1.currentPhotoFilter;
        r47 = 0;
        r48 = 0;
        r49 = "b1";
        r10 = r3.size;
        r51 = 0;
        r52 = 1;
        r43 = r8;
        r44 = r3;
        r46 = r9;
        r50 = r10;
        r43.setImage(r44, r45, r46, r47, r48, r49, r50, r51, r52);
    L_0x1209:
        r84 = r3;
        r87 = r14;
        goto L_0x13db;
    L_0x120f:
        r8 = r1.documentAttachType;
        r9 = 6;
        if (r8 != r9) goto L_0x1241;
    L_0x1214:
        r8 = r1.photoImage;
        r9 = r1.documentAttach;
        r45 = 0;
        r10 = r1.currentPhotoFilter;
        r47 = 0;
        r13 = r1.currentPhotoObject;
        if (r13 == 0) goto L_0x1229;
    L_0x1222:
        r13 = r1.currentPhotoObject;
        r13 = r13.location;
        r48 = r13;
        goto L_0x122b;
    L_0x1229:
        r48 = 0;
    L_0x122b:
        r49 = "b1";
        r13 = r1.documentAttach;
        r13 = r13.size;
        r51 = "webp";
        r52 = 1;
        r43 = r8;
        r44 = r9;
        r46 = r10;
        r50 = r13;
        r43.setImage(r44, r45, r46, r47, r48, r49, r50, r51, r52);
        goto L_0x1209;
    L_0x1241:
        r8 = r1.documentAttachType;
        r9 = 4;
        if (r8 != r9) goto L_0x1272;
    L_0x1246:
        r8 = r1.photoImage;
        r9 = 1;
        r8.setNeedsQualityThumb(r9);
        r8 = r1.photoImage;
        r8.setShouldGenerateQualityThumb(r9);
        r8 = r1.photoImage;
        r8.setParentMessageObject(r2);
        r8 = r1.photoImage;
        r26 = 0;
        r27 = 0;
        r9 = r1.currentPhotoObject;
        r9 = r9.location;
        r10 = r1.currentPhotoFilter;
        r30 = 0;
        r31 = 0;
        r32 = 0;
        r25 = r8;
        r28 = r9;
        r29 = r10;
        r25.setImage(r26, r27, r28, r29, r30, r31, r32);
        goto L_0x1209;
    L_0x1272:
        r8 = r1.documentAttachType;
        r9 = 2;
        if (r8 == r9) goto L_0x133e;
    L_0x1277:
        r8 = r1.documentAttachType;
        r9 = 7;
        if (r8 != r9) goto L_0x1282;
    L_0x127c:
        r84 = r3;
        r87 = r14;
        goto L_0x1342;
    L_0x1282:
        r8 = r2.mediaExists;
        r9 = r1.currentPhotoObject;
        r9 = org.telegram.messenger.FileLoader.getAttachFileName(r9);
        r10 = r1.hasGamePreview;
        if (r10 != 0) goto L_0x1309;
    L_0x128e:
        if (r8 != 0) goto L_0x1309;
    L_0x1290:
        r10 = r1.currentAccount;
        r10 = org.telegram.messenger.DownloadController.getInstance(r10);
        r13 = r1.currentMessageObject;
        r10 = r10.canDownloadMedia(r13);
        if (r10 != 0) goto L_0x1309;
    L_0x129e:
        r10 = r1.currentAccount;
        r10 = org.telegram.messenger.FileLoader.getInstance(r10);
        r10 = r10.isLoadingFile(r9);
        if (r10 == 0) goto L_0x12b3;
    L_0x12aa:
        r84 = r3;
        r85 = r8;
        r86 = r9;
        r87 = r14;
        goto L_0x1311;
    L_0x12b3:
        r10 = 1;
        r1.photoNotSet = r10;
        r10 = r1.currentPhotoObjectThumb;
        if (r10 == 0) goto L_0x12f6;
    L_0x12ba:
        r10 = r1.photoImage;
        r26 = 0;
        r27 = 0;
        r13 = r1.currentPhotoObjectThumb;
        r13 = r13.location;
        r84 = r3;
        r3 = java.util.Locale.US;
        r85 = r8;
        r8 = "%d_%d_b";
        r86 = r9;
        r87 = r14;
        r9 = 2;
        r14 = new java.lang.Object[r9];
        r9 = java.lang.Integer.valueOf(r6);
        r16 = 0;
        r14[r16] = r9;
        r9 = java.lang.Integer.valueOf(r11);
        r16 = 1;
        r14[r16] = r9;
        r29 = java.lang.String.format(r3, r8, r14);
        r30 = 0;
        r31 = 0;
        r32 = 0;
        r25 = r10;
        r28 = r13;
        r25.setImage(r26, r27, r28, r29, r30, r31, r32);
        goto L_0x13db;
    L_0x12f6:
        r84 = r3;
        r85 = r8;
        r86 = r9;
        r87 = r14;
        r3 = r1.photoImage;
        r8 = 0;
        r9 = r8;
        r9 = (android.graphics.drawable.Drawable) r9;
        r3.setImageBitmap(r9);
        goto L_0x13db;
    L_0x1309:
        r84 = r3;
        r85 = r8;
        r86 = r9;
        r87 = r14;
    L_0x1311:
        r3 = 0;
        r1.photoNotSet = r3;
        r3 = r1.photoImage;
        r8 = r1.currentPhotoObject;
        r8 = r8.location;
        r9 = r1.currentPhotoFilter;
        r10 = r1.currentPhotoObjectThumb;
        if (r10 == 0) goto L_0x1327;
    L_0x1320:
        r10 = r1.currentPhotoObjectThumb;
        r10 = r10.location;
        r28 = r10;
        goto L_0x1329;
    L_0x1327:
        r28 = 0;
    L_0x1329:
        r10 = r1.currentPhotoFilterThumb;
        r30 = 0;
        r31 = 0;
        r32 = 0;
        r25 = r3;
        r26 = r8;
        r27 = r9;
        r29 = r10;
        r25.setImage(r26, r27, r28, r29, r30, r31, r32);
        goto L_0x13db;
    L_0x133e:
        r84 = r3;
        r87 = r14;
    L_0x1342:
        r3 = org.telegram.messenger.FileLoader.getAttachFileName(r12);
        r8 = 0;
        r9 = org.telegram.messenger.MessageObject.isRoundVideoDocument(r12);
        if (r9 == 0) goto L_0x1363;
    L_0x134d:
        r9 = r1.photoImage;
        r10 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r13 = 2;
        r10 = r10 / r13;
        r9.setRoundRadius(r10);
        r9 = r1.currentAccount;
        r9 = org.telegram.messenger.DownloadController.getInstance(r9);
        r10 = r1.currentMessageObject;
        r8 = r9.canDownloadMedia(r10);
        goto L_0x1375;
    L_0x1363:
        r9 = org.telegram.messenger.MessageObject.isNewGifDocument(r12);
        if (r9 == 0) goto L_0x1375;
    L_0x1369:
        r9 = r1.currentAccount;
        r9 = org.telegram.messenger.DownloadController.getInstance(r9);
        r10 = r1.currentMessageObject;
        r8 = r9.canDownloadMedia(r10);
    L_0x1375:
        r9 = r126.isSending();
        if (r9 != 0) goto L_0x13b5;
    L_0x137b:
        r9 = r2.mediaExists;
        if (r9 != 0) goto L_0x138d;
    L_0x137f:
        r9 = r1.currentAccount;
        r9 = org.telegram.messenger.FileLoader.getInstance(r9);
        r9 = r9.isLoadingFile(r3);
        if (r9 != 0) goto L_0x138d;
    L_0x138b:
        if (r8 == 0) goto L_0x13b5;
    L_0x138d:
        r9 = 0;
        r1.photoNotSet = r9;
        r9 = r1.photoImage;
        r27 = 0;
        r10 = r1.currentPhotoObject;
        if (r10 == 0) goto L_0x139f;
    L_0x1398:
        r10 = r1.currentPhotoObject;
        r10 = r10.location;
        r28 = r10;
        goto L_0x13a1;
    L_0x139f:
        r28 = 0;
    L_0x13a1:
        r10 = r1.currentPhotoFilterThumb;
        r13 = r12.size;
        r31 = 0;
        r32 = 0;
        r25 = r9;
        r26 = r12;
        r29 = r10;
        r30 = r13;
        r25.setImage(r26, r27, r28, r29, r30, r31, r32);
        goto L_0x13da;
    L_0x13b5:
        r9 = 1;
        r1.photoNotSet = r9;
        r9 = r1.photoImage;
        r26 = 0;
        r27 = 0;
        r10 = r1.currentPhotoObject;
        if (r10 == 0) goto L_0x13c9;
        r10 = r1.currentPhotoObject;
        r10 = r10.location;
        r28 = r10;
        goto L_0x13cb;
        r28 = 0;
        r10 = r1.currentPhotoFilterThumb;
        r30 = 0;
        r31 = 0;
        r32 = 0;
        r25 = r9;
        r29 = r10;
        r25.setImage(r26, r27, r28, r29, r30, r31, r32);
    L_0x13db:
        r3 = 1;
        r1.drawPhotoImage = r3;
        if (r7 == 0) goto L_0x142f;
        r3 = "video";
        r3 = r7.equals(r3);
        if (r3 == 0) goto L_0x142f;
        if (r54 == 0) goto L_0x142f;
        r3 = r54 / 60;
        r8 = r3 * 60;
        r8 = r54 - r8;
        r9 = "%d:%02d";
        r10 = 2;
        r13 = new java.lang.Object[r10];
        r10 = java.lang.Integer.valueOf(r3);
        r14 = 0;
        r13[r14] = r10;
        r10 = java.lang.Integer.valueOf(r8);
        r14 = 1;
        r13[r14] = r10;
        r9 = java.lang.String.format(r9, r13);
        r10 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r10 = r10.measureText(r9);
        r13 = (double) r10;
        r13 = java.lang.Math.ceil(r13);
        r10 = (int) r13;
        r1.durationWidth = r10;
        r10 = new android.text.StaticLayout;
        r27 = org.telegram.ui.ActionBar.Theme.chat_durationPaint;
        r13 = r1.durationWidth;
        r29 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r31 = 0;
        r32 = 0;
        r25 = r10;
        r26 = r9;
        r28 = r13;
        r25.<init>(r26, r27, r28, r29, r30, r31, r32);
        r1.videoInfoLayout = r10;
        goto L_0x1467;
        r3 = r1.hasGamePreview;
        if (r3 == 0) goto L_0x1467;
        r3 = "AttachGame";
        r8 = 2131493027; // 0x7f0c00a3 float:1.8609523E38 double:1.053097479E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r8);
        r3 = r3.toUpperCase();
        r8 = org.telegram.ui.ActionBar.Theme.chat_gamePaint;
        r8 = r8.measureText(r3);
        r8 = (double) r8;
        r8 = java.lang.Math.ceil(r8);
        r8 = (int) r8;
        r1.durationWidth = r8;
        r8 = new android.text.StaticLayout;
        r27 = org.telegram.ui.ActionBar.Theme.chat_gamePaint;
        r9 = r1.durationWidth;
        r29 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r31 = 0;
        r32 = 0;
        r25 = r8;
        r26 = r3;
        r28 = r9;
        r25.<init>(r26, r27, r28, r29, r30, r31, r32);
        r1.videoInfoLayout = r8;
        r8 = r78;
        r3 = r82;
    L_0x146c:
        r6 = r1.hasInvoicePreview;
        if (r6 == 0) goto L_0x1564;
        r6 = r2.messageOwner;
        r6 = r6.media;
        r6 = r6.flags;
        r6 = r6 & 4;
        if (r6 == 0) goto L_0x1488;
        r6 = "PaymentReceipt";
        r9 = 2131494115; // 0x7f0c04e3 float:1.861173E38 double:1.0530980165E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r9);
        r6 = r6.toUpperCase();
        goto L_0x14ab;
        r6 = r2.messageOwner;
        r6 = r6.media;
        r6 = r6.test;
        if (r6 == 0) goto L_0x149e;
        r6 = "PaymentTestInvoice";
        r9 = 2131494133; // 0x7f0c04f5 float:1.8611766E38 double:1.0530980254E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r9);
        r6 = r6.toUpperCase();
        goto L_0x1487;
        r6 = "PaymentInvoice";
        r9 = 2131494102; // 0x7f0c04d6 float:1.8611703E38 double:1.05309801E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r9);
        r6 = r6.toUpperCase();
        r9 = org.telegram.messenger.LocaleController.getInstance();
        r10 = r2.messageOwner;
        r10 = r10.media;
        r10 = r10.total_amount;
        r13 = r2.messageOwner;
        r13 = r13.media;
        r13 = r13.currency;
        r9 = r9.formatCurrencyString(r10, r13);
        r10 = new android.text.SpannableStringBuilder;
        r11 = new java.lang.StringBuilder;
        r11.<init>();
        r11.append(r9);
        r13 = " ";
        r11.append(r13);
        r11.append(r6);
        r11 = r11.toString();
        r10.<init>(r11);
        r11 = new org.telegram.ui.Components.TypefaceSpan;
        r13 = "fonts/rmedium.ttf";
        r13 = org.telegram.messenger.AndroidUtilities.getTypeface(r13);
        r11.<init>(r13);
        r13 = r9.length();
        r14 = 33;
        r88 = r3;
        r3 = 0;
        r10.setSpan(r11, r3, r13, r14);
        r11 = org.telegram.ui.ActionBar.Theme.chat_shipmentPaint;
        r13 = r10.length();
        r11 = r11.measureText(r10, r3, r13);
        r13 = (double) r11;
        r13 = java.lang.Math.ceil(r13);
        r3 = (int) r13;
        r1.durationWidth = r3;
        r3 = new android.text.StaticLayout;
        r27 = org.telegram.ui.ActionBar.Theme.chat_shipmentPaint;
        r11 = r1.durationWidth;
        r13 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r28 = r11 + r14;
        r29 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r31 = 0;
        r32 = 0;
        r25 = r3;
        r26 = r10;
        r25.<init>(r26, r27, r28, r29, r30, r31, r32);
        r1.videoInfoLayout = r3;
        r3 = r1.drawPhotoImage;
        if (r3 != 0) goto L_0x1566;
        r3 = r1.totalHeight;
        r11 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3 = r3 + r11;
        r1.totalHeight = r3;
        r3 = r1.timeWidth;
        r11 = 14;
        r13 = r126.isOutOwner();
        if (r13 == 0) goto L_0x153c;
        r13 = 20;
        goto L_0x153d;
        r13 = 0;
        r11 = r11 + r13;
        r11 = (float) r11;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r3 = r3 + r11;
        r11 = r1.durationWidth;
        r11 = r11 + r3;
        if (r11 <= r5) goto L_0x155b;
        r11 = r1.durationWidth;
        r8 = java.lang.Math.max(r11, r8);
        r11 = r1.totalHeight;
        r13 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r11 = r11 + r13;
        r1.totalHeight = r11;
        goto L_0x1566;
        r11 = r1.durationWidth;
        r11 = r11 + r3;
        r3 = java.lang.Math.max(r11, r8);
        r8 = r3;
        goto L_0x1566;
        r88 = r3;
        r3 = r1.hasGamePreview;
        if (r3 == 0) goto L_0x1587;
        r3 = r2.textHeight;
        if (r3 == 0) goto L_0x1587;
        r3 = r1.linkPreviewHeight;
        r6 = r2.textHeight;
        r9 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r6 = r6 + r9;
        r3 = r3 + r6;
        r1.linkPreviewHeight = r3;
        r3 = r1.totalHeight;
        r6 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r3 = r3 + r6;
        r1.totalHeight = r3;
        r1.calcBackgroundWidth(r5, r4, r8);
        r71 = r88;
        goto L_0x1599;
    L_0x158d:
        r84 = r3;
        r77 = r6;
        r81 = r10;
        r76 = r13;
        r87 = r14;
        r83 = r63;
        r3 = r1.drawInstantView;
        if (r3 == 0) goto L_0x1658;
        r3 = 1107558400; // 0x42040000 float:33.0 double:5.47206556E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r1.instantWidth = r3;
        r3 = r1.drawInstantViewType;
        r6 = 1;
        if (r3 != r6) goto L_0x15b4;
        r3 = "OpenChannel";
        r6 = 2131494041; // 0x7f0c0499 float:1.861158E38 double:1.05309798E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r6);
        goto L_0x15db;
        r3 = r1.drawInstantViewType;
        r6 = 2;
        if (r3 != r6) goto L_0x15c3;
        r3 = "OpenGroup";
        r6 = 2131494042; // 0x7f0c049a float:1.8611581E38 double:1.0530979805E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r6);
        goto L_0x15b3;
        r3 = r1.drawInstantViewType;
        r6 = 3;
        if (r3 != r6) goto L_0x15d2;
        r3 = "OpenMessage";
        r6 = 2131494045; // 0x7f0c049d float:1.8611587E38 double:1.053097982E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r6);
        goto L_0x15b3;
        r3 = "InstantView";
        r6 = 2131493676; // 0x7f0c032c float:1.8610839E38 double:1.0530977996E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r6);
        r6 = r1.backgroundWidth;
        r9 = 1117126656; // 0x42960000 float:75.0 double:5.51933903E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r6 = r6 - r9;
        r9 = new android.text.StaticLayout;
        r10 = org.telegram.ui.ActionBar.Theme.chat_instantViewPaint;
        r11 = (float) r6;
        r13 = android.text.TextUtils.TruncateAt.END;
        r26 = android.text.TextUtils.ellipsize(r3, r10, r11, r13);
        r27 = org.telegram.ui.ActionBar.Theme.chat_instantViewPaint;
        r29 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r30 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r31 = 0;
        r32 = 0;
        r25 = r9;
        r28 = r6;
        r25.<init>(r26, r27, r28, r29, r30, r31, r32);
        r1.instantViewLayout = r9;
        r9 = r1.backgroundWidth;
        r10 = 1107820544; // 0x42080000 float:34.0 double:5.473360725E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r10;
        r1.instantWidth = r9;
        r9 = r1.totalHeight;
        r10 = 1110966272; // 0x42380000 float:46.0 double:5.488902687E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 + r10;
        r1.totalHeight = r9;
        r9 = r1.instantViewLayout;
        if (r9 == 0) goto L_0x1658;
        r9 = r1.instantViewLayout;
        r9 = r9.getLineCount();
        if (r9 <= 0) goto L_0x1658;
        r9 = r1.instantWidth;
        r9 = (double) r9;
        r11 = r1.instantViewLayout;
        r13 = 0;
        r11 = r11.getLineWidth(r13);
        r13 = (double) r11;
        r13 = java.lang.Math.ceil(r13);
        r9 = r9 - r13;
        r9 = (int) r9;
        r10 = 2;
        r9 = r9 / r10;
        r10 = r1.drawInstantViewType;
        if (r10 != 0) goto L_0x1642;
        r10 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        goto L_0x1643;
        r10 = 0;
        r9 = r9 + r10;
        r1.instantTextX = r9;
        r9 = r1.instantViewLayout;
        r10 = 0;
        r9 = r9.getLineLeft(r10);
        r9 = (int) r9;
        r1.instantTextLeftX = r9;
        r9 = r1.instantTextX;
        r10 = r1.instantTextLeftX;
        r10 = -r10;
        r9 = r9 + r10;
        r1.instantTextX = r9;
    L_0x1659:
        goto L_0x1a9f;
    L_0x165b:
        r53 = r6;
        r34 = r10;
        r35 = r11;
        r37 = r12;
        r21 = r14;
        r3 = r2.type;
        r4 = 16;
        r5 = 1132920832; // 0x43870000 float:270.0 double:5.597372625E-315;
        r13 = 1120665600; // 0x42cc0000 float:102.0 double:5.536823734E-315;
        if (r3 != r4) goto L_0x17cb;
        r3 = 0;
        r1.drawName = r3;
        r1.drawForwardedName = r3;
        r1.drawPhotoImage = r3;
        r3 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r3 == 0) goto L_0x16a3;
        r3 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x1691;
        r4 = r126.needDrawAvatar();
        if (r4 == 0) goto L_0x1691;
        r4 = r126.isOutOwner();
        if (r4 != 0) goto L_0x1691;
        goto L_0x1693;
        r13 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = java.lang.Math.min(r3, r4);
        r1.backgroundWidth = r3;
        goto L_0x16c9;
        r3 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r3.x;
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x16b8;
        r4 = r126.needDrawAvatar();
        if (r4 == 0) goto L_0x16b8;
        r4 = r126.isOutOwner();
        if (r4 != 0) goto L_0x16b8;
        goto L_0x16ba;
        r13 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = java.lang.Math.min(r3, r4);
        r1.backgroundWidth = r3;
        r3 = r1.backgroundWidth;
        r4 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r1.availableTimeWidth = r3;
        r3 = r125.getMaxNameWidth();
        r7 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3 = r3 - r4;
        if (r3 >= 0) goto L_0x16e7;
        r4 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = org.telegram.messenger.LocaleController.getInstance();
        r4 = r4.formatterDay;
        r5 = r2.messageOwner;
        r5 = r5.date;
        r5 = (long) r5;
        r7 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r5 = r5 * r7;
        r4 = r4.format(r5);
        r5 = r2.messageOwner;
        r5 = r5.action;
        r5 = (org.telegram.tgnet.TLRPC.TL_messageActionPhoneCall) r5;
        r6 = r5.reason;
        r6 = r6 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonMissed;
        r7 = r126.isOutOwner();
        if (r7 == 0) goto L_0x171f;
        if (r6 == 0) goto L_0x1715;
        r7 = "CallMessageOutgoingMissed";
        r8 = 2131493113; // 0x7f0c00f9 float:1.8609697E38 double:1.0530975215E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        goto L_0x1744;
        r7 = "CallMessageOutgoing";
        r8 = 2131493112; // 0x7f0c00f8 float:1.8609695E38 double:1.053097521E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        goto L_0x1714;
        if (r6 == 0) goto L_0x172b;
        r7 = "CallMessageIncomingMissed";
        r8 = 2131493111; // 0x7f0c00f7 float:1.8609693E38 double:1.0530975205E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        goto L_0x1714;
        r7 = r5.reason;
        r7 = r7 instanceof org.telegram.tgnet.TLRPC.TL_phoneCallDiscardReasonBusy;
        if (r7 == 0) goto L_0x173b;
        r7 = "CallMessageIncomingDeclined";
        r8 = 2131493110; // 0x7f0c00f6 float:1.860969E38 double:1.05309752E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        goto L_0x1714;
        r7 = "CallMessageIncoming";
        r8 = 2131493109; // 0x7f0c00f5 float:1.8609689E38 double:1.0530975195E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r7, r8);
        r8 = r5.duration;
        if (r8 <= 0) goto L_0x1762;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r8.append(r4);
        r9 = ", ";
        r8.append(r9);
        r9 = r5.duration;
        r9 = org.telegram.messenger.LocaleController.formatCallDuration(r9);
        r8.append(r9);
        r4 = r8.toString();
        r15 = new android.text.StaticLayout;
        r8 = org.telegram.ui.ActionBar.Theme.chat_audioTitlePaint;
        r9 = (float) r3;
        r10 = android.text.TextUtils.TruncateAt.END;
        r9 = android.text.TextUtils.ellipsize(r7, r8, r9, r10);
        r10 = org.telegram.ui.ActionBar.Theme.chat_audioTitlePaint;
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r11 = r11 + r3;
        r12 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r14 = 0;
        r16 = 0;
        r8 = r15;
        r89 = r5;
        r5 = r15;
        r15 = r16;
        r8.<init>(r9, r10, r11, r12, r13, r14, r15);
        r1.titleLayout = r5;
        r5 = new android.text.StaticLayout;
        r8 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r9 = (float) r3;
        r10 = android.text.TextUtils.TruncateAt.END;
        r25 = android.text.TextUtils.ellipsize(r4, r8, r9, r10);
        r26 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r27 = r3 + r9;
        r28 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r30 = 0;
        r31 = 0;
        r24 = r5;
        r24.<init>(r25, r26, r27, r28, r29, r30, r31);
        r1.docTitleLayout = r5;
        r125.setMessageObjectInternal(r126);
        r5 = 1115815936; // 0x42820000 float:65.0 double:5.51286321E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r8 = r1.namesOffset;
        r5 = r5 + r8;
        r1.totalHeight = r5;
        r5 = r1.drawPinnedTop;
        if (r5 == 0) goto L_0x17c9;
        r5 = r1.namesOffset;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 - r9;
        r1.namesOffset = r5;
        goto L_0x1a9f;
        r7 = 1112014848; // 0x42480000 float:50.0 double:5.49408334E-315;
        r3 = r2.type;
        r4 = 12;
        if (r3 != r4) goto L_0x19a9;
        r3 = 0;
        r1.drawName = r3;
        r3 = 1;
        r1.drawForwardedName = r3;
        r1.drawPhotoImage = r3;
        r3 = r1.photoImage;
        r4 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3.setRoundRadius(r4);
        r3 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r3 == 0) goto L_0x1812;
        r3 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x1801;
        r4 = r126.needDrawAvatar();
        if (r4 == 0) goto L_0x1801;
        r4 = r126.isOutOwner();
        if (r4 != 0) goto L_0x1801;
        goto L_0x1802;
        r13 = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = java.lang.Math.min(r3, r4);
        r1.backgroundWidth = r3;
        goto L_0x1837;
        r3 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r3.x;
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x1827;
        r4 = r126.needDrawAvatar();
        if (r4 == 0) goto L_0x1827;
        r4 = r126.isOutOwner();
        if (r4 != 0) goto L_0x1827;
        goto L_0x1828;
        r13 = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = java.lang.Math.min(r3, r4);
        r1.backgroundWidth = r3;
        r3 = r1.backgroundWidth;
        r4 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r1.availableTimeWidth = r3;
        r3 = r2.messageOwner;
        r3 = r3.media;
        r3 = r3.user_id;
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r5 = java.lang.Integer.valueOf(r3);
        r4 = r4.getUser(r5);
        r5 = r125.getMaxNameWidth();
        r6 = 1121714176; // 0x42dc0000 float:110.0 double:5.54200439E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        if (r5 >= 0) goto L_0x1869;
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = 0;
        if (r4 == 0) goto L_0x1879;
        r7 = r4.photo;
        if (r7 == 0) goto L_0x1874;
        r7 = r4.photo;
        r6 = r7.photo_small;
        r7 = r1.contactAvatarDrawable;
        r7.setInfo(r4);
        r8 = r1.photoImage;
        r10 = "50_50";
        if (r4 == 0) goto L_0x1883;
        r7 = r1.contactAvatarDrawable;
        r11 = r7;
        goto L_0x188c;
        r7 = org.telegram.ui.ActionBar.Theme.chat_contactDrawable;
        r9 = r126.isOutOwner();
        r7 = r7[r9];
        goto L_0x1881;
        r12 = 0;
        r13 = 0;
        r9 = r6;
        r8.setImage(r9, r10, r11, r12, r13);
        r7 = r2.messageOwner;
        r7 = r7.media;
        r7 = r7.phone_number;
        if (r7 == 0) goto L_0x18a9;
        r8 = r7.length();
        if (r8 == 0) goto L_0x18a9;
        r8 = org.telegram.PhoneFormat.PhoneFormat.getInstance();
        r7 = r8.format(r7);
        goto L_0x18b2;
        r8 = "NumberUnknown";
        r9 = 2131494027; // 0x7f0c048b float:1.861155E38 double:1.053097973E-314;
        r7 = org.telegram.messenger.LocaleController.getString(r8, r9);
        r8 = r2.messageOwner;
        r8 = r8.media;
        r8 = r8.first_name;
        r9 = r2.messageOwner;
        r9 = r9.media;
        r9 = r9.last_name;
        r8 = org.telegram.messenger.ContactsController.formatName(r8, r9);
        r9 = 10;
        r10 = 32;
        r8 = r8.replace(r9, r10);
        r9 = r8.length();
        if (r9 != 0) goto L_0x18d1;
        r8 = r7;
        r15 = new android.text.StaticLayout;
        r9 = org.telegram.ui.ActionBar.Theme.chat_contactNamePaint;
        r10 = (float) r5;
        r11 = android.text.TextUtils.TruncateAt.END;
        r10 = android.text.TextUtils.ellipsize(r8, r9, r10, r11);
        r11 = org.telegram.ui.ActionBar.Theme.chat_contactNamePaint;
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r12 = r12 + r5;
        r13 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r14 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r16 = 0;
        r17 = 0;
        r9 = r15;
        r90 = r15;
        r15 = r16;
        r16 = r17;
        r9.<init>(r10, r11, r12, r13, r14, r15, r16);
        r9 = r90;
        r1.titleLayout = r9;
        r9 = new android.text.StaticLayout;
        r10 = 10;
        r11 = 32;
        r10 = r7.replace(r10, r11);
        r11 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r12 = (float) r5;
        r13 = android.text.TextUtils.TruncateAt.END;
        r25 = android.text.TextUtils.ellipsize(r10, r11, r12, r13);
        r26 = org.telegram.ui.ActionBar.Theme.chat_contactPhonePaint;
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r27 = r5 + r11;
        r28 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r30 = 0;
        r31 = 0;
        r24 = r9;
        r24.<init>(r25, r26, r27, r28, r29, r30, r31);
        r1.docTitleLayout = r9;
        r125.setMessageObjectInternal(r126);
        r9 = r1.drawForwardedName;
        if (r9 == 0) goto L_0x194a;
        r9 = r126.needDrawForwarded();
        if (r9 == 0) goto L_0x194a;
        r9 = r1.currentPosition;
        if (r9 == 0) goto L_0x193e;
        r9 = r1.currentPosition;
        r9 = r9.minY;
        if (r9 != 0) goto L_0x194a;
        r9 = r1.namesOffset;
        r10 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 + r10;
        r1.namesOffset = r9;
        goto L_0x195f;
        r9 = r1.drawNameLayout;
        if (r9 == 0) goto L_0x195f;
        r9 = r2.messageOwner;
        r9 = r9.reply_to_msg_id;
        if (r9 != 0) goto L_0x195f;
        r9 = r1.namesOffset;
        r10 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 + r10;
        r1.namesOffset = r9;
        r9 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r10 = r1.namesOffset;
        r9 = r9 + r10;
        r1.totalHeight = r9;
        r9 = r1.drawPinnedTop;
        if (r9 == 0) goto L_0x1979;
        r9 = r1.namesOffset;
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r11;
        r1.namesOffset = r9;
        r9 = r1.docTitleLayout;
        r9 = r9.getLineCount();
        if (r9 <= 0) goto L_0x19a7;
        r9 = r1.backgroundWidth;
        r10 = 1121714176; // 0x42dc0000 float:110.0 double:5.54200439E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r10;
        r10 = r1.docTitleLayout;
        r11 = 0;
        r10 = r10.getLineWidth(r11);
        r10 = (double) r10;
        r10 = java.lang.Math.ceil(r10);
        r10 = (int) r10;
        r9 = r9 - r10;
        r10 = r1.timeWidth;
        if (r9 >= r10) goto L_0x19a7;
        r10 = r1.totalHeight;
        r11 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r10 = r10 + r11;
        r1.totalHeight = r10;
        goto L_0x1a9f;
        r3 = r2.type;
        r4 = 2;
        if (r3 != r4) goto L_0x1a26;
        r3 = 1;
        r1.drawForwardedName = r3;
        r3 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r3 == 0) goto L_0x19dd;
        r3 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x19cc;
        r4 = r126.needDrawAvatar();
        if (r4 == 0) goto L_0x19cc;
        r4 = r126.isOutOwner();
        if (r4 != 0) goto L_0x19cc;
        goto L_0x19cd;
        r13 = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = java.lang.Math.min(r3, r4);
        r1.backgroundWidth = r3;
        goto L_0x1a02;
        r3 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r3.x;
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x19f2;
        r4 = r126.needDrawAvatar();
        if (r4 == 0) goto L_0x19f2;
        r4 = r126.isOutOwner();
        if (r4 != 0) goto L_0x19f2;
        goto L_0x19f3;
        r13 = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = java.lang.Math.min(r3, r4);
        r1.backgroundWidth = r3;
        r3 = r1.backgroundWidth;
        r1.createDocumentLayout(r3, r2);
        r125.setMessageObjectInternal(r126);
        r3 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r1.totalHeight = r3;
        r3 = r1.drawPinnedTop;
        if (r3 == 0) goto L_0x1a9f;
        r3 = r1.namesOffset;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r5;
        r1.namesOffset = r3;
        goto L_0x1a9f;
        r3 = r2.type;
        r4 = 14;
        if (r3 != r4) goto L_0x1aa2;
        r3 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r3 == 0) goto L_0x1a58;
        r3 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x1a47;
        r4 = r126.needDrawAvatar();
        if (r4 == 0) goto L_0x1a47;
        r4 = r126.isOutOwner();
        if (r4 != 0) goto L_0x1a47;
        goto L_0x1a48;
        r13 = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = java.lang.Math.min(r3, r4);
        r1.backgroundWidth = r3;
        goto L_0x1a7d;
        r3 = org.telegram.messenger.AndroidUtilities.displaySize;
        r3 = r3.x;
        r4 = r1.isChat;
        if (r4 == 0) goto L_0x1a6d;
        r4 = r126.needDrawAvatar();
        if (r4 == 0) goto L_0x1a6d;
        r4 = r126.isOutOwner();
        if (r4 != 0) goto L_0x1a6d;
        goto L_0x1a6e;
        r13 = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = java.lang.Math.min(r3, r4);
        r1.backgroundWidth = r3;
        r3 = r1.backgroundWidth;
        r1.createDocumentLayout(r3, r2);
        r125.setMessageObjectInternal(r126);
        r3 = 1118044160; // 0x42a40000 float:82.0 double:5.5238721E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.namesOffset;
        r3 = r3 + r4;
        r1.totalHeight = r3;
        r3 = r1.drawPinnedTop;
        if (r3 == 0) goto L_0x1a9f;
        r3 = r1.namesOffset;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r5;
        r1.namesOffset = r3;
        r5 = r2;
        goto L_0x2c11;
        r3 = r2.messageOwner;
        r3 = r3.fwd_from;
        if (r3 == 0) goto L_0x1ab0;
        r3 = r2.type;
        r4 = 13;
        if (r3 == r4) goto L_0x1ab0;
        r3 = 1;
        goto L_0x1ab1;
        r3 = 0;
        r1.drawForwardedName = r3;
        r3 = r2.type;
        r4 = 9;
        if (r3 == r4) goto L_0x1abb;
        r3 = 1;
        goto L_0x1abc;
        r3 = 0;
        r1.mediaBackground = r3;
        r3 = 1;
        r1.drawImageButton = r3;
        r1.drawPhotoImage = r3;
        r3 = 0;
        r4 = 0;
        r6 = 0;
        r8 = r2.gifState;
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = (r8 > r9 ? 1 : (r8 == r9 ? 0 : -1));
        if (r8 == 0) goto L_0x1ae1;
        r8 = org.telegram.messenger.SharedConfig.autoplayGifs;
        if (r8 != 0) goto L_0x1ae1;
        r8 = r2.type;
        r9 = 8;
        if (r8 == r9) goto L_0x1add;
        r8 = r2.type;
        r9 = 5;
        if (r8 != r9) goto L_0x1ae1;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r2.gifState = r8;
        r8 = r126.isRoundVideo();
        if (r8 == 0) goto L_0x1b00;
        r8 = r1.photoImage;
        r9 = 1;
        r8.setAllowDecodeSingleFrame(r9);
        r8 = r1.photoImage;
        r9 = org.telegram.messenger.MediaController.getInstance();
        r9 = r9.getPlayingMessageObject();
        if (r9 != 0) goto L_0x1afb;
        r9 = 1;
        goto L_0x1afc;
        r9 = 0;
        r8.setAllowStartAnimation(r9);
        goto L_0x1b0f;
        r8 = r1.photoImage;
        r9 = r2.gifState;
        r10 = 0;
        r9 = (r9 > r10 ? 1 : (r9 == r10 ? 0 : -1));
        if (r9 != 0) goto L_0x1b0b;
        r9 = 1;
        goto L_0x1b0c;
        r9 = 0;
        r8.setAllowStartAnimation(r9);
        r8 = r1.photoImage;
        r9 = r126.needDrawBluredPreview();
        r8.setForcePreview(r9);
        r8 = r2.type;
        r9 = 9;
        if (r8 != r9) goto L_0x1c04;
        r8 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r8 == 0) goto L_0x1b4a;
        r8 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r9 = r1.isChat;
        if (r9 == 0) goto L_0x1b39;
        r9 = r126.needDrawAvatar();
        if (r9 == 0) goto L_0x1b39;
        r9 = r126.isOutOwner();
        if (r9 != 0) goto L_0x1b39;
        goto L_0x1b3a;
        r13 = r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r8 = r8 - r7;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = java.lang.Math.min(r8, r5);
        r1.backgroundWidth = r5;
        goto L_0x1b6f;
        r8 = org.telegram.messenger.AndroidUtilities.displaySize;
        r8 = r8.x;
        r9 = r1.isChat;
        if (r9 == 0) goto L_0x1b5f;
        r9 = r126.needDrawAvatar();
        if (r9 == 0) goto L_0x1b5f;
        r9 = r126.isOutOwner();
        if (r9 != 0) goto L_0x1b5f;
        goto L_0x1b60;
        r13 = r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r8 = r8 - r7;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = java.lang.Math.min(r8, r5);
        r1.backgroundWidth = r5;
        r5 = r125.checkNeedDrawShareButton(r126);
        if (r5 == 0) goto L_0x1b80;
        r5 = r1.backgroundWidth;
        r7 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = r5 - r7;
        r1.backgroundWidth = r5;
        r5 = r1.backgroundWidth;
        r7 = 1124728832; // 0x430a0000 float:138.0 double:5.55689877E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = r5 - r7;
        r1.createDocumentLayout(r5, r2);
        r7 = r2.caption;
        r7 = android.text.TextUtils.isEmpty(r7);
        if (r7 != 0) goto L_0x1b9c;
        r7 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = r5 + r8;
        goto L_0x1b9e;
        r7 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r8 = r1.drawPhotoImage;
        if (r8 == 0) goto L_0x1bab;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r4 = org.telegram.messenger.AndroidUtilities.dp(r7);
        goto L_0x1bc7;
        r7 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r4 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = r2.caption;
        r7 = android.text.TextUtils.isEmpty(r7);
        if (r7 == 0) goto L_0x1bc0;
        r7 = 1112276992; // 0x424c0000 float:51.0 double:5.495378504E-315;
        goto L_0x1bc2;
        r7 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = r5 + r7;
        r1.availableTimeWidth = r5;
        r7 = r1.drawPhotoImage;
        if (r7 != 0) goto L_0x1c02;
        r7 = r2.caption;
        r7 = android.text.TextUtils.isEmpty(r7);
        if (r7 == 0) goto L_0x1c02;
        r7 = r1.infoLayout;
        r7 = r7.getLineCount();
        if (r7 <= 0) goto L_0x1c02;
        r125.measureTime(r126);
        r7 = r1.backgroundWidth;
        r8 = 1123287040; // 0x42f40000 float:122.0 double:5.54977537E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7 = r7 - r8;
        r8 = r1.infoLayout;
        r9 = 0;
        r8 = r8.getLineWidth(r9);
        r8 = (double) r8;
        r8 = java.lang.Math.ceil(r8);
        r8 = (int) r8;
        r7 = r7 - r8;
        r8 = r1.timeWidth;
        if (r7 >= r8) goto L_0x1c02;
        r8 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r4 = r4 + r8;
        goto L_0x1fe9;
        r8 = r2.type;
        r9 = 4;
        if (r8 != r9) goto L_0x1fee;
        r8 = r2.messageOwner;
        r8 = r8.media;
        r8 = r8.geo;
        r8 = r8.lat;
        r10 = r2.messageOwner;
        r10 = r10.media;
        r10 = r10.geo;
        r10 = r10._long;
        r12 = r2.messageOwner;
        r12 = r12.media;
        r12 = r12 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGeoLive;
        if (r12 == 0) goto L_0x1e2f;
        r5 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r5 == 0) goto L_0x1c50;
        r5 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r12 = r1.isChat;
        if (r12 == 0) goto L_0x1c3c;
        r12 = r126.needDrawAvatar();
        if (r12 == 0) goto L_0x1c3c;
        r12 = r126.isOutOwner();
        if (r12 != 0) goto L_0x1c3c;
        goto L_0x1c3d;
        r13 = r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r5 = r5 - r7;
        r7 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = java.lang.Math.min(r5, r7);
        r1.backgroundWidth = r5;
        goto L_0x1c78;
        r5 = org.telegram.messenger.AndroidUtilities.displaySize;
        r5 = r5.x;
        r12 = r1.isChat;
        if (r12 == 0) goto L_0x1c65;
        r12 = r126.needDrawAvatar();
        if (r12 == 0) goto L_0x1c65;
        r12 = r126.isOutOwner();
        if (r12 != 0) goto L_0x1c65;
        goto L_0x1c66;
        r13 = r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r5 = r5 - r7;
        r7 = 1133543424; // 0x43908000 float:289.0 double:5.60044864E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = java.lang.Math.min(r5, r7);
        r1.backgroundWidth = r5;
        r5 = r125.checkNeedDrawShareButton(r126);
        if (r5 == 0) goto L_0x1c89;
        r5 = r1.backgroundWidth;
        r7 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = r5 - r7;
        r1.backgroundWidth = r5;
        r5 = r1.backgroundWidth;
        r7 = 1108606976; // 0x42140000 float:37.0 double:5.477246216E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = r5 - r7;
        r1.availableTimeWidth = r5;
        r7 = 1113063424; // 0x42580000 float:54.0 double:5.499263994E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = r5 - r7;
        r7 = r1.backgroundWidth;
        r12 = 1101529088; // 0x41a80000 float:21.0 double:5.442276803E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r7 = r7 - r12;
        r3 = 1128464384; // 0x43430000 float:195.0 double:5.575354847E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r12 = (double) r4;
        r14 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r12 = r12 / r14;
        r14 = (double) r4;
        r24 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r26 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r26 = r26 * r8;
        r28 = 4640537203540230144; // 0x4066800000000000 float:0.0 double:180.0;
        r92 = r5;
        r91 = r6;
        r5 = r26 / r28;
        r5 = java.lang.Math.sin(r5);
        r24 = r24 + r5;
        r5 = 4607182418800017408; // 0x3ff0000000000000 float:0.0 double:1.0;
        r26 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r26 = r26 * r8;
        r93 = r8;
        r8 = r26 / r28;
        r8 = java.lang.Math.sin(r8);
        r5 = r5 - r8;
        r5 = r24 / r5;
        r5 = java.lang.Math.log(r5);
        r5 = r5 * r12;
        r8 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
        r5 = r5 / r8;
        r14 = r14 - r5;
        r5 = java.lang.Math.round(r14);
        r8 = 1092930765; // 0x4124cccd float:10.3 double:5.399795443E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = r8 << 6;
        r8 = (long) r8;
        r14 = r5 - r8;
        r5 = (double) r14;
        r14 = 4611686018427387904; // 0x4000000000000000 float:0.0 double:2.0;
        r8 = (double) r4;
        r8 = r5 - r8;
        r8 = r8 / r12;
        r8 = java.lang.Math.exp(r8);
        r8 = java.lang.Math.atan(r8);
        r14 = r14 * r8;
        r8 = 4609753056924675352; // 0x3ff921fb54442d18 float:3.37028055E12 double:1.5707963267948966;
        r8 = r8 - r14;
        r14 = 4640537203540230144; // 0x4066800000000000 float:0.0 double:180.0;
        r8 = r8 * r14;
        r14 = 4614256656552045848; // 0x400921fb54442d18 float:3.37028055E12 double:3.141592653589793;
        r8 = r8 / r14;
        r14 = java.util.Locale.US;
        r15 = "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=%dx%d&maptype=roadmap&scale=%d&sensor=false";
        r95 = r4;
        r96 = r5;
        r4 = 5;
        r5 = new java.lang.Object[r4];
        r4 = java.lang.Double.valueOf(r8);
        r6 = 0;
        r5[r6] = r4;
        r4 = java.lang.Double.valueOf(r10);
        r6 = 1;
        r5[r6] = r4;
        r4 = (float) r7;
        r6 = org.telegram.messenger.AndroidUtilities.density;
        r4 = r4 / r6;
        r4 = (int) r4;
        r4 = java.lang.Integer.valueOf(r4);
        r6 = 2;
        r5[r6] = r4;
        r4 = (float) r3;
        r6 = org.telegram.messenger.AndroidUtilities.density;
        r4 = r4 / r6;
        r4 = (int) r4;
        r4 = java.lang.Integer.valueOf(r4);
        r6 = 3;
        r5[r6] = r4;
        r6 = org.telegram.messenger.AndroidUtilities.density;
        r98 = r5;
        r4 = (double) r6;
        r4 = java.lang.Math.ceil(r4);
        r4 = (int) r4;
        r5 = 2;
        r4 = java.lang.Math.min(r5, r4);
        r4 = java.lang.Integer.valueOf(r4);
        r5 = 4;
        r98[r5] = r4;
        r4 = r98;
        r4 = java.lang.String.format(r14, r15, r4);
        r1.currentUrl = r4;
        r4 = r125.isCurrentLocationTimeExpired(r126);
        r1.locationExpired = r4;
        if (r4 != 0) goto L_0x1d8e;
        r4 = r1.photoImage;
        r5 = 1;
        r4.setCrossfadeWithOldImage(r5);
        r4 = 0;
        r1.mediaBackground = r4;
        r4 = 1113587712; // 0x42600000 float:56.0 double:5.50185432E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r6 = r1.invalidateRunnable;
        r14 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r6, r14);
        r1.scheduledInvalidate = r5;
        r6 = r4;
        goto L_0x1d9b;
        r4 = r1.backgroundWidth;
        r5 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r1.backgroundWidth = r4;
        r6 = r91;
        r4 = new android.text.StaticLayout;
        r5 = "AttachLiveLocation";
        r14 = 2131493031; // 0x7f0c00a7 float:1.860953E38 double:1.053097481E-314;
        r25 = org.telegram.messenger.LocaleController.getString(r5, r14);
        r26 = org.telegram.ui.ActionBar.Theme.chat_locationTitlePaint;
        r28 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r30 = 0;
        r31 = 0;
        r24 = r4;
        r27 = r92;
        r24.<init>(r25, r26, r27, r28, r29, r30, r31);
        r1.docTitleLayout = r4;
        r4 = 0;
        r125.updateCurrentUserAndChat();
        r5 = r1.currentUser;
        if (r5 == 0) goto L_0x1dd5;
        r5 = r1.currentUser;
        r5 = r5.photo;
        if (r5 == 0) goto L_0x1dcd;
        r5 = r1.currentUser;
        r5 = r5.photo;
        r4 = r5.photo_small;
        r5 = r1.contactAvatarDrawable;
        r14 = r1.currentUser;
        r5.setInfo(r14);
        goto L_0x1dec;
        r5 = r1.currentChat;
        if (r5 == 0) goto L_0x1dec;
        r5 = r1.currentChat;
        r5 = r5.photo;
        if (r5 == 0) goto L_0x1de5;
        r5 = r1.currentChat;
        r5 = r5.photo;
        r4 = r5.photo_small;
        r5 = r1.contactAvatarDrawable;
        r14 = r1.currentChat;
        r5.setInfo(r14);
        r5 = r1.locationImageReceiver;
        r26 = "50_50";
        r14 = r1.contactAvatarDrawable;
        r28 = 0;
        r29 = 0;
        r24 = r5;
        r25 = r4;
        r27 = r14;
        r24.setImage(r25, r26, r27, r28, r29);
        r5 = new android.text.StaticLayout;
        r14 = r2.messageOwner;
        r14 = r14.edit_date;
        if (r14 == 0) goto L_0x1e0d;
        r14 = r2.messageOwner;
        r14 = r14.edit_date;
        r14 = (long) r14;
        goto L_0x1e12;
        r14 = r2.messageOwner;
        r14 = r14.date;
        goto L_0x1e0b;
        r25 = org.telegram.messenger.LocaleController.formatLocationUpdateDate(r14);
        r26 = org.telegram.ui.ActionBar.Theme.chat_locationAddressPaint;
        r28 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r30 = 0;
        r31 = 0;
        r24 = r5;
        r27 = r92;
        r24.<init>(r25, r26, r27, r28, r29, r30, r31);
        r1.infoLayout = r5;
        r4 = r3;
        r3 = r7;
        r13 = r8;
        goto L_0x1fcb;
        r91 = r6;
        r93 = r8;
        r6 = r2.messageOwner;
        r6 = r6.media;
        r6 = r6.title;
        r6 = android.text.TextUtils.isEmpty(r6);
        if (r6 != 0) goto L_0x1f66;
        r6 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r6 == 0) goto L_0x1e6b;
        r6 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r8 = r1.isChat;
        if (r8 == 0) goto L_0x1e5a;
        r8 = r126.needDrawAvatar();
        if (r8 == 0) goto L_0x1e5a;
        r8 = r126.isOutOwner();
        if (r8 != 0) goto L_0x1e5a;
        goto L_0x1e5b;
        r13 = r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r6 = r6 - r7;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = java.lang.Math.min(r6, r5);
        r1.backgroundWidth = r5;
        goto L_0x1e90;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.x;
        r8 = r1.isChat;
        if (r8 == 0) goto L_0x1e80;
        r8 = r126.needDrawAvatar();
        if (r8 == 0) goto L_0x1e80;
        r8 = r126.isOutOwner();
        if (r8 != 0) goto L_0x1e80;
        goto L_0x1e81;
        r13 = r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r6 = r6 - r7;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = java.lang.Math.min(r6, r5);
        r1.backgroundWidth = r5;
        r5 = r125.checkNeedDrawShareButton(r126);
        if (r5 == 0) goto L_0x1ea1;
        r5 = r1.backgroundWidth;
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r1.backgroundWidth = r5;
        r5 = r1.backgroundWidth;
        r6 = 1123418112; // 0x42f60000 float:123.0 double:5.55042295E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r6;
        r6 = r2.messageOwner;
        r6 = r6.media;
        r6 = r6.title;
        r25 = org.telegram.ui.ActionBar.Theme.chat_locationTitlePaint;
        r27 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 0;
        r30 = 0;
        r31 = android.text.TextUtils.TruncateAt.END;
        r33 = 2;
        r24 = r6;
        r26 = r5;
        r32 = r5;
        r6 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r24, r25, r26, r27, r28, r29, r30, r31, r32, r33);
        r1.docTitleLayout = r6;
        r6 = r1.docTitleLayout;
        r6 = r6.getLineCount();
        r7 = r2.messageOwner;
        r7 = r7.media;
        r7 = r7.address;
        if (r7 == 0) goto L_0x1f0a;
        r7 = r2.messageOwner;
        r7 = r7.media;
        r7 = r7.address;
        r7 = r7.length();
        if (r7 <= 0) goto L_0x1f0a;
        r7 = r2.messageOwner;
        r7 = r7.media;
        r7 = r7.address;
        r25 = org.telegram.ui.ActionBar.Theme.chat_locationAddressPaint;
        r27 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r28 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r29 = 0;
        r30 = 0;
        r31 = android.text.TextUtils.TruncateAt.END;
        r8 = 3;
        r15 = 3 - r6;
        r33 = java.lang.Math.min(r8, r15);
        r24 = r7;
        r26 = r5;
        r32 = r5;
        r7 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r24, r25, r26, r27, r28, r29, r30, r31, r32, r33);
        r1.infoLayout = r7;
        goto L_0x1f0d;
        r7 = 0;
        r1.infoLayout = r7;
        r7 = 0;
        r1.mediaBackground = r7;
        r1.availableTimeWidth = r5;
        r7 = 1118568448; // 0x42ac0000 float:86.0 double:5.526462427E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r4 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = java.util.Locale.US;
        r8 = "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:mid|%f,%f&sensor=false";
        r9 = 5;
        r12 = new java.lang.Object[r9];
        r13 = r93;
        r9 = java.lang.Double.valueOf(r13);
        r15 = 0;
        r12[r15] = r9;
        r9 = java.lang.Double.valueOf(r10);
        r15 = 1;
        r12[r15] = r9;
        r9 = org.telegram.messenger.AndroidUtilities.density;
        r99 = r3;
        r100 = r4;
        r3 = (double) r9;
        r3 = java.lang.Math.ceil(r3);
        r3 = (int) r3;
        r4 = 2;
        r3 = java.lang.Math.min(r4, r3);
        r3 = java.lang.Integer.valueOf(r3);
        r12[r4] = r3;
        r3 = java.lang.Double.valueOf(r13);
        r4 = 3;
        r12[r4] = r3;
        r3 = 4;
        r4 = java.lang.Double.valueOf(r10);
        r12[r3] = r4;
        r3 = java.lang.String.format(r7, r8, r12);
        r1.currentUrl = r3;
        r6 = r91;
        r3 = r99;
        r4 = r100;
        goto L_0x1fcb;
        r13 = r93;
        r5 = 1127874560; // 0x433a0000 float:186.0 double:5.57244073E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r1.availableTimeWidth = r5;
        r5 = 1128792064; // 0x43480000 float:200.0 double:5.5769738E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = r5 + r3;
        r1.backgroundWidth = r5;
        r5 = java.util.Locale.US;
        r6 = "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=200x100&maptype=roadmap&scale=%d&markers=color:red|size:mid|%f,%f&sensor=false";
        r7 = 5;
        r8 = new java.lang.Object[r7];
        r7 = java.lang.Double.valueOf(r13);
        r9 = 0;
        r8[r9] = r7;
        r7 = java.lang.Double.valueOf(r10);
        r9 = 1;
        r8[r9] = r7;
        r7 = org.telegram.messenger.AndroidUtilities.density;
        r101 = r3;
        r102 = r4;
        r3 = (double) r7;
        r3 = java.lang.Math.ceil(r3);
        r3 = (int) r3;
        r4 = 2;
        r3 = java.lang.Math.min(r4, r3);
        r3 = java.lang.Integer.valueOf(r3);
        r8[r4] = r3;
        r3 = java.lang.Double.valueOf(r13);
        r4 = 3;
        r8[r4] = r3;
        r3 = 4;
        r4 = java.lang.Double.valueOf(r10);
        r8[r3] = r4;
        r3 = java.lang.String.format(r5, r6, r8);
        r1.currentUrl = r3;
        r6 = r91;
        r3 = r101;
        r4 = r102;
        r5 = r1.currentUrl;
        if (r5 == 0) goto L_0x1fe8;
        r5 = r1.photoImage;
        r7 = r1.currentUrl;
        r26 = 0;
        r8 = org.telegram.ui.ActionBar.Theme.chat_locationDrawable;
        r9 = r126.isOutOwner();
        r27 = r8[r9];
        r28 = 0;
        r29 = 0;
        r24 = r5;
        r25 = r7;
        r24.setImage(r25, r26, r27, r28, r29);
        r5 = r2;
        r14 = r21;
        goto L_0x2b59;
        r91 = r6;
        r5 = r2.type;
        r6 = 13;
        if (r5 != r6) goto L_0x212d;
        r5 = 0;
        r1.drawBackground = r5;
        r5 = 0;
        r6 = r2.messageOwner;
        r6 = r6.media;
        r6 = r6.document;
        r6 = r6.attributes;
        r6 = r6.size();
        if (r5 >= r6) goto L_0x2022;
        r6 = r2.messageOwner;
        r6 = r6.media;
        r6 = r6.document;
        r6 = r6.attributes;
        r6 = r6.get(r5);
        r6 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r6;
        r7 = r6 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r7 == 0) goto L_0x201f;
        r3 = r6.w;
        r4 = r6.h;
        goto L_0x2022;
        r5 = r5 + 1;
        goto L_0x1ffa;
        r5 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r5 == 0) goto L_0x2033;
        r5 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r5 = (float) r5;
        r6 = 1053609165; // 0x3ecccccd float:0.4 double:5.205520926E-315;
        r5 = r5 * r6;
        r6 = r5;
        goto L_0x2044;
        r5 = org.telegram.messenger.AndroidUtilities.displaySize;
        r5 = r5.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r5 = java.lang.Math.min(r5, r6);
        r5 = (float) r5;
        r6 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r5 = r5 * r6;
        r6 = r5;
        if (r3 != 0) goto L_0x204f;
        r4 = (int) r5;
        r7 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r3 = r4 + r7;
        r7 = (float) r4;
        r8 = (float) r3;
        r8 = r6 / r8;
        r7 = r7 * r8;
        r4 = (int) r7;
        r3 = (int) r6;
        r7 = (float) r4;
        r7 = (r7 > r5 ? 1 : (r7 == r5 ? 0 : -1));
        if (r7 <= 0) goto L_0x2062;
        r7 = (float) r3;
        r8 = (float) r4;
        r8 = r5 / r8;
        r7 = r7 * r8;
        r3 = (int) r7;
        r4 = (int) r5;
        r7 = 6;
        r1.documentAttachType = r7;
        r7 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = r3 - r7;
        r1.availableTimeWidth = r7;
        r7 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = r7 + r3;
        r1.backgroundWidth = r7;
        r7 = r2.photoThumbs;
        r8 = 80;
        r7 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r7, r8);
        r1.currentPhotoObjectThumb = r7;
        r7 = r2.attachPathExists;
        if (r7 == 0) goto L_0x20ce;
        r7 = r1.photoImage;
        r25 = 0;
        r8 = r2.messageOwner;
        r8 = r8.attachPath;
        r9 = java.util.Locale.US;
        r10 = "%d_%d";
        r11 = 2;
        r12 = new java.lang.Object[r11];
        r11 = java.lang.Integer.valueOf(r3);
        r13 = 0;
        r12[r13] = r11;
        r11 = java.lang.Integer.valueOf(r4);
        r13 = 1;
        r12[r13] = r11;
        r27 = java.lang.String.format(r9, r10, r12);
        r28 = 0;
        r9 = r1.currentPhotoObjectThumb;
        if (r9 == 0) goto L_0x20b4;
        r9 = r1.currentPhotoObjectThumb;
        r9 = r9.location;
        r29 = r9;
        goto L_0x20b6;
        r29 = 0;
        r30 = "b1";
        r9 = r2.messageOwner;
        r9 = r9.media;
        r9 = r9.document;
        r9 = r9.size;
        r32 = "webp";
        r33 = 1;
        r24 = r7;
        r26 = r8;
        r31 = r9;
        r24.setImage(r25, r26, r27, r28, r29, r30, r31, r32, r33);
        goto L_0x2125;
        r7 = r2.messageOwner;
        r7 = r7.media;
        r7 = r7.document;
        r7 = r7.id;
        r9 = 0;
        r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r11 == 0) goto L_0x2125;
        r7 = r1.photoImage;
        r8 = r2.messageOwner;
        r8 = r8.media;
        r8 = r8.document;
        r26 = 0;
        r9 = java.util.Locale.US;
        r10 = "%d_%d";
        r11 = 2;
        r12 = new java.lang.Object[r11];
        r11 = java.lang.Integer.valueOf(r3);
        r13 = 0;
        r12[r13] = r11;
        r11 = java.lang.Integer.valueOf(r4);
        r13 = 1;
        r12[r13] = r11;
        r27 = java.lang.String.format(r9, r10, r12);
        r28 = 0;
        r9 = r1.currentPhotoObjectThumb;
        if (r9 == 0) goto L_0x210c;
        r9 = r1.currentPhotoObjectThumb;
        r9 = r9.location;
        r29 = r9;
        goto L_0x210e;
        r29 = 0;
        r30 = "b1";
        r9 = r2.messageOwner;
        r9 = r9.media;
        r9 = r9.document;
        r9 = r9.size;
        r32 = "webp";
        r33 = 1;
        r24 = r7;
        r25 = r8;
        r31 = r9;
        r24.setImage(r25, r26, r27, r28, r29, r30, r31, r32, r33);
        r5 = r2;
        r14 = r21;
        r6 = r91;
        goto L_0x2b59;
        r5 = r2.type;
        r6 = 5;
        if (r5 != r6) goto L_0x2136;
        r5 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r3 = r5;
        goto L_0x215b;
        r5 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r5 == 0) goto L_0x2148;
        r5 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r5 = (float) r5;
        r6 = 1060320051; // 0x3f333333 float:0.7 double:5.23867711E-315;
        r5 = r5 * r6;
        r5 = (int) r5;
        r3 = r5;
        goto L_0x2135;
        r5 = org.telegram.messenger.AndroidUtilities.displaySize;
        r5 = r5.x;
        r6 = org.telegram.messenger.AndroidUtilities.displaySize;
        r6 = r6.y;
        r5 = java.lang.Math.min(r5, r6);
        r5 = (float) r5;
        r6 = 1060320051; // 0x3f333333 float:0.7 double:5.23867711E-315;
        r5 = r5 * r6;
        r5 = (int) r5;
        r3 = r5;
        r6 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = r6 + r3;
        r4 = r2.type;
        r7 = 5;
        if (r4 == r7) goto L_0x217b;
        r4 = r125.checkNeedDrawShareButton(r126);
        if (r4 == 0) goto L_0x217b;
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r5 = r5 - r4;
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        if (r3 <= r4) goto L_0x2185;
        r3 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r4 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        if (r6 <= r4) goto L_0x218f;
        r6 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r4 = r2.type;
        r7 = 1;
        if (r4 != r7) goto L_0x21a3;
        r125.updateSecretTimeText(r126);
        r4 = r2.photoThumbs;
        r7 = 80;
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r7);
        r1.currentPhotoObjectThumb = r4;
        goto L_0x2232;
        r4 = r2.type;
        r7 = 3;
        if (r4 != r7) goto L_0x21c6;
        r4 = 0;
        r1.createDocumentLayout(r4, r2);
        r125.updateSecretTimeText(r126);
        r4 = r126.needDrawBluredPreview();
        if (r4 != 0) goto L_0x21c0;
        r4 = r1.photoImage;
        r7 = 1;
        r4.setNeedsQualityThumb(r7);
        r4 = r1.photoImage;
        r4.setShouldGenerateQualityThumb(r7);
        r4 = r1.photoImage;
        r4.setParentMessageObject(r2);
        goto L_0x2232;
        r4 = r2.type;
        r7 = 5;
        if (r4 != r7) goto L_0x21e2;
        r4 = r126.needDrawBluredPreview();
        if (r4 != 0) goto L_0x21dc;
        r4 = r1.photoImage;
        r7 = 1;
        r4.setNeedsQualityThumb(r7);
        r4 = r1.photoImage;
        r4.setShouldGenerateQualityThumb(r7);
        r4 = r1.photoImage;
        r4.setParentMessageObject(r2);
        goto L_0x2232;
        r4 = r2.type;
        r7 = 8;
        if (r4 != r7) goto L_0x2232;
        r4 = r2.messageOwner;
        r4 = r4.media;
        r4 = r4.document;
        r4 = r4.size;
        r7 = (long) r4;
        r4 = org.telegram.messenger.AndroidUtilities.formatFileSize(r7);
        r7 = org.telegram.ui.ActionBar.Theme.chat_infoPaint;
        r7 = r7.measureText(r4);
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        r7 = (int) r7;
        r1.infoWidth = r7;
        r7 = new android.text.StaticLayout;
        r26 = org.telegram.ui.ActionBar.Theme.chat_infoPaint;
        r8 = r1.infoWidth;
        r28 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r30 = 0;
        r31 = 0;
        r24 = r7;
        r25 = r4;
        r27 = r8;
        r24.<init>(r25, r26, r27, r28, r29, r30, r31);
        r1.infoLayout = r7;
        r7 = r126.needDrawBluredPreview();
        if (r7 != 0) goto L_0x222d;
        r7 = r1.photoImage;
        r8 = 1;
        r7.setNeedsQualityThumb(r8);
        r7 = r1.photoImage;
        r7.setShouldGenerateQualityThumb(r8);
        r7 = r1.photoImage;
        r7.setParentMessageObject(r2);
        r4 = r1.currentMessagesGroup;
        if (r4 != 0) goto L_0x223d;
        r4 = r2.caption;
        if (r4 == 0) goto L_0x223d;
        r4 = 0;
        r1.mediaBackground = r4;
        r4 = r2.photoThumbs;
        r7 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r4 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r4, r7);
        r1.currentPhotoObject = r4;
        r4 = 0;
        r7 = 0;
        r8 = r1.currentPhotoObject;
        if (r8 == 0) goto L_0x2258;
        r8 = r1.currentPhotoObject;
        r9 = r1.currentPhotoObjectThumb;
        if (r8 != r9) goto L_0x2258;
        r8 = 0;
        r1.currentPhotoObjectThumb = r8;
        r8 = r1.currentPhotoObject;
        if (r8 == 0) goto L_0x22b2;
        r8 = r1.currentPhotoObject;
        r8 = r8.w;
        r8 = (float) r8;
        r9 = (float) r3;
        r8 = r8 / r9;
        r9 = r1.currentPhotoObject;
        r9 = r9.w;
        r9 = (float) r9;
        r9 = r9 / r8;
        r4 = (int) r9;
        r9 = r1.currentPhotoObject;
        r9 = r9.h;
        r9 = (float) r9;
        r9 = r9 / r8;
        r7 = (int) r9;
        if (r4 != 0) goto L_0x2279;
        r9 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r9);
        if (r7 != 0) goto L_0x2281;
        r9 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r9);
        if (r7 <= r6) goto L_0x228b;
        r9 = (float) r7;
        r7 = r6;
        r10 = (float) r7;
        r9 = r9 / r10;
        r10 = (float) r4;
        r10 = r10 / r9;
        r4 = (int) r10;
        goto L_0x22b2;
        r9 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        if (r7 >= r9) goto L_0x22b2;
        r9 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = r1.currentPhotoObject;
        r9 = r9.h;
        r9 = (float) r9;
        r10 = (float) r7;
        r9 = r9 / r10;
        r10 = r1.currentPhotoObject;
        r10 = r10.w;
        r10 = (float) r10;
        r10 = r10 / r9;
        r11 = (float) r3;
        r10 = (r10 > r11 ? 1 : (r10 == r11 ? 0 : -1));
        if (r10 >= 0) goto L_0x22b2;
        r10 = r1.currentPhotoObject;
        r10 = r10.w;
        r10 = (float) r10;
        r10 = r10 / r9;
        r4 = (int) r10;
        r8 = r2.type;
        r9 = 5;
        if (r8 != r9) goto L_0x22bb;
        r8 = org.telegram.messenger.AndroidUtilities.roundMessageSize;
        r7 = r8;
        r4 = r8;
        if (r4 == 0) goto L_0x22bf;
        if (r7 != 0) goto L_0x2329;
        r8 = r2.type;
        r9 = 8;
        if (r8 != r9) goto L_0x2329;
        r8 = 0;
        r9 = r2.messageOwner;
        r9 = r9.media;
        r9 = r9.document;
        r9 = r9.attributes;
        r9 = r9.size();
        if (r8 >= r9) goto L_0x2329;
        r9 = r2.messageOwner;
        r9 = r9.media;
        r9 = r9.document;
        r9 = r9.attributes;
        r9 = r9.get(r8);
        r9 = (org.telegram.tgnet.TLRPC.DocumentAttribute) r9;
        r10 = r9 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
        if (r10 != 0) goto L_0x22ee;
        r10 = r9 instanceof org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
        if (r10 == 0) goto L_0x22eb;
        goto L_0x22ee;
        r8 = r8 + 1;
        goto L_0x22c6;
        r10 = r9.w;
        r10 = (float) r10;
        r11 = (float) r3;
        r10 = r10 / r11;
        r11 = r9.w;
        r11 = (float) r11;
        r11 = r11 / r10;
        r4 = (int) r11;
        r11 = r9.h;
        r11 = (float) r11;
        r11 = r11 / r10;
        r7 = (int) r11;
        if (r7 <= r6) goto L_0x2307;
        r11 = (float) r7;
        r7 = r6;
        r12 = (float) r7;
        r11 = r11 / r12;
        r12 = (float) r4;
        r12 = r12 / r11;
        r4 = (int) r12;
        goto L_0x2329;
        r11 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        if (r7 >= r11) goto L_0x2329;
        r11 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r11 = r9.h;
        r11 = (float) r11;
        r12 = (float) r7;
        r11 = r11 / r12;
        r12 = r9.w;
        r12 = (float) r12;
        r12 = r12 / r11;
        r13 = (float) r3;
        r12 = (r12 > r13 ? 1 : (r12 == r13 ? 0 : -1));
        if (r12 >= 0) goto L_0x2328;
        r12 = r9.w;
        r12 = (float) r12;
        r12 = r12 / r11;
        r4 = (int) r12;
        if (r4 == 0) goto L_0x232d;
        if (r7 != 0) goto L_0x2335;
        r8 = 1125515264; // 0x43160000 float:150.0 double:5.56078426E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7 = r8;
        r4 = r8;
        r8 = r2.type;
        r9 = 3;
        if (r8 != r9) goto L_0x234f;
        r8 = r1.infoWidth;
        r9 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 + r9;
        if (r4 >= r8) goto L_0x234f;
        r8 = r1.infoWidth;
        r9 = 1109393408; // 0x42200000 float:40.0 double:5.481131706E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r4 = r8 + r9;
        r8 = r1.currentMessagesGroup;
        if (r8 == 0) goto L_0x2393;
        r8 = 0;
        r9 = r125.getGroupPhotosWidth();
        r10 = r8;
        r8 = 0;
        r11 = r1.currentMessagesGroup;
        r11 = r11.posArray;
        r11 = r11.size();
        if (r8 >= r11) goto L_0x2388;
        r11 = r1.currentMessagesGroup;
        r11 = r11.posArray;
        r11 = r11.get(r8);
        r11 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r11;
        r12 = r11.minY;
        if (r12 != 0) goto L_0x2388;
        r12 = (double) r10;
        r14 = r11.pw;
        r15 = r11.leftSpanOffset;
        r14 = r14 + r15;
        r14 = (float) r14;
        r15 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r14 = r14 / r15;
        r15 = (float) r9;
        r14 = r14 * r15;
        r14 = (double) r14;
        r14 = java.lang.Math.ceil(r14);
        r12 = r12 + r14;
        r10 = (int) r12;
        r8 = r8 + 1;
        goto L_0x235a;
        r8 = 1108082688; // 0x420c0000 float:35.0 double:5.47465589E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = r10 - r8;
        r1.availableTimeWidth = r8;
        goto L_0x239d;
        r8 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = r5 - r8;
        r1.availableTimeWidth = r8;
        r8 = r2.type;
        r9 = 5;
        if (r8 != r9) goto L_0x23be;
        r8 = r1.availableTimeWidth;
        r8 = (double) r8;
        r10 = org.telegram.ui.ActionBar.Theme.chat_audioTimePaint;
        r11 = "00:00";
        r10 = r10.measureText(r11);
        r10 = (double) r10;
        r10 = java.lang.Math.ceil(r10);
        r12 = 1104150528; // 0x41d00000 float:26.0 double:5.455228437E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r12 = (double) r12;
        r10 = r10 + r12;
        r8 = r8 - r10;
        r8 = (int) r8;
        r1.availableTimeWidth = r8;
        r125.measureTime(r126);
        r8 = r1.timeWidth;
        r9 = 14;
        r10 = r126.isOutOwner();
        if (r10 == 0) goto L_0x23ce;
        r10 = 20;
        goto L_0x23cf;
        r10 = 0;
        r9 = r9 + r10;
        r9 = (float) r9;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 + r9;
        if (r4 >= r8) goto L_0x23d9;
        r4 = r8;
        r9 = r126.isRoundVideo();
        if (r9 == 0) goto L_0x23f0;
        r9 = java.lang.Math.min(r4, r7);
        r7 = r9;
        r4 = r9;
        r9 = 0;
        r1.drawBackground = r9;
        r9 = r1.photoImage;
        r10 = r4 / 2;
        r9.setRoundRadius(r10);
        goto L_0x241b;
        r9 = r126.needDrawBluredPreview();
        if (r9 == 0) goto L_0x241b;
        r9 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r9 == 0) goto L_0x2408;
        r9 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r9 = (float) r9;
        r10 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r9 = r9 * r10;
        r9 = (int) r9;
        r7 = r9;
        r4 = r9;
        goto L_0x241b;
        r9 = org.telegram.messenger.AndroidUtilities.displaySize;
        r9 = r9.x;
        r10 = org.telegram.messenger.AndroidUtilities.displaySize;
        r10 = r10.y;
        r9 = java.lang.Math.min(r9, r10);
        r9 = (float) r9;
        r10 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r9 = r9 * r10;
        r9 = (int) r9;
        r7 = r9;
        r4 = r9;
        r9 = 0;
        r10 = r1.currentMessagesGroup;
        if (r10 == 0) goto L_0x276f;
        r10 = org.telegram.messenger.AndroidUtilities.displaySize;
        r10 = r10.x;
        r11 = org.telegram.messenger.AndroidUtilities.displaySize;
        r11 = r11.y;
        r10 = java.lang.Math.max(r10, r11);
        r10 = (float) r10;
        r11 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r10 = r10 * r11;
        r11 = r125.getGroupPhotosWidth();
        r12 = r1.currentPosition;
        r12 = r12.pw;
        r12 = (float) r12;
        r13 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r12 = r12 / r13;
        r13 = (float) r11;
        r12 = r12 * r13;
        r12 = (double) r12;
        r12 = java.lang.Math.ceil(r12);
        r4 = (int) r12;
        r12 = r1.currentPosition;
        r12 = r12.minY;
        if (r12 == 0) goto L_0x2514;
        r12 = r126.isOutOwner();
        if (r12 == 0) goto L_0x2458;
        r12 = r1.currentPosition;
        r12 = r12.flags;
        r13 = 1;
        r12 = r12 & r13;
        if (r12 != 0) goto L_0x2466;
        r12 = r126.isOutOwner();
        if (r12 != 0) goto L_0x2514;
        r12 = r1.currentPosition;
        r12 = r12.flags;
        r13 = 2;
        r12 = r12 & r13;
        if (r12 == 0) goto L_0x2514;
        r12 = 0;
        r13 = 0;
        r14 = r13;
        r13 = r12;
        r12 = 0;
        r15 = r1.currentMessagesGroup;
        r15 = r15.posArray;
        r15 = r15.size();
        if (r12 >= r15) goto L_0x2506;
        r15 = r1.currentMessagesGroup;
        r15 = r15.posArray;
        r15 = r15.get(r12);
        r15 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r15;
        r103 = r3;
        r3 = r15.minY;
        if (r3 != 0) goto L_0x24b6;
        r104 = r5;
        r105 = r6;
        r5 = (double) r13;
        r3 = r15.pw;
        r3 = (float) r3;
        r17 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r3 = r3 / r17;
        r106 = r8;
        r8 = (float) r11;
        r3 = r3 * r8;
        r107 = r7;
        r7 = (double) r3;
        r7 = java.lang.Math.ceil(r7);
        r3 = r15.leftSpanOffset;
        if (r3 == 0) goto L_0x24af;
        r3 = r15.leftSpanOffset;
        r3 = (float) r3;
        r17 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r3 = r3 / r17;
        r2 = (float) r11;
        r3 = r3 * r2;
        r2 = (double) r3;
        r2 = java.lang.Math.ceil(r2);
        goto L_0x24b1;
        r2 = 0;
        r7 = r7 + r2;
        r5 = r5 + r7;
        r2 = (int) r5;
        r13 = r2;
        goto L_0x24f6;
        r104 = r5;
        r105 = r6;
        r107 = r7;
        r106 = r8;
        r2 = r15.minY;
        r3 = r1.currentPosition;
        r3 = r3.minY;
        if (r2 != r3) goto L_0x24ed;
        r2 = (double) r14;
        r5 = r15.pw;
        r5 = (float) r5;
        r6 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r5 = r5 / r6;
        r6 = (float) r11;
        r5 = r5 * r6;
        r5 = (double) r5;
        r5 = java.lang.Math.ceil(r5);
        r7 = r15.leftSpanOffset;
        if (r7 == 0) goto L_0x24e6;
        r7 = r15.leftSpanOffset;
        r7 = (float) r7;
        r8 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r7 = r7 / r8;
        r8 = (float) r11;
        r7 = r7 * r8;
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        goto L_0x24e8;
        r7 = 0;
        r5 = r5 + r7;
        r2 = r2 + r5;
        r2 = (int) r2;
        r14 = r2;
        goto L_0x24f6;
        r2 = r15.minY;
        r3 = r1.currentPosition;
        r3 = r3.minY;
        if (r2 <= r3) goto L_0x24f6;
        goto L_0x2510;
        r12 = r12 + 1;
        r3 = r103;
        r5 = r104;
        r6 = r105;
        r8 = r106;
        r7 = r107;
        r2 = r126;
        goto L_0x246b;
        r103 = r3;
        r104 = r5;
        r105 = r6;
        r107 = r7;
        r106 = r8;
        r2 = r13 - r14;
        r4 = r4 + r2;
        goto L_0x251e;
        r103 = r3;
        r104 = r5;
        r105 = r6;
        r107 = r7;
        r106 = r8;
        r2 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r4 = r4 - r2;
        r2 = r1.isAvatarVisible;
        if (r2 == 0) goto L_0x2530;
        r2 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r4 = r4 - r2;
        r2 = r1.currentPosition;
        r2 = r2.siblingHeights;
        if (r2 == 0) goto L_0x2564;
        r2 = 0;
        r3 = r2;
        r2 = 0;
        r5 = r1.currentPosition;
        r5 = r5.siblingHeights;
        r5 = r5.length;
        if (r2 >= r5) goto L_0x2551;
        r5 = r1.currentPosition;
        r5 = r5.siblingHeights;
        r5 = r5[r2];
        r5 = r5 * r10;
        r5 = (double) r5;
        r5 = java.lang.Math.ceil(r5);
        r5 = (int) r5;
        r3 = r3 + r5;
        r2 = r2 + 1;
        goto L_0x2539;
        r2 = r1.currentPosition;
        r2 = r2.maxY;
        r5 = r1.currentPosition;
        r5 = r5.minY;
        r2 = r2 - r5;
        r5 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r2 = r2 * r5;
        r3 = r3 + r2;
        r7 = r3;
        goto L_0x2570;
        r2 = r1.currentPosition;
        r2 = r2.ph;
        r2 = r2 * r10;
        r2 = (double) r2;
        r2 = java.lang.Math.ceil(r2);
        r2 = (int) r2;
        r7 = r2;
        r1.backgroundWidth = r4;
        r2 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r4 = r4 - r2;
        r2 = r4;
        r3 = r1.currentPosition;
        r3 = r3.edge;
        if (r3 != 0) goto L_0x2588;
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r5;
        goto L_0x258a;
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r5 = r7;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r3 = r2 - r6;
        r9 = r9 + r3;
        r3 = r1.currentPosition;
        r3 = r3.flags;
        r6 = 8;
        r3 = r3 & r6;
        if (r3 != 0) goto L_0x25b2;
        r3 = r1.currentMessagesGroup;
        r3 = r3.hasSibling;
        if (r3 == 0) goto L_0x25aa;
        r3 = r1.currentPosition;
        r3 = r3.flags;
        r3 = r3 & 4;
        if (r3 != 0) goto L_0x25aa;
        goto L_0x25b2;
        r108 = r2;
        r110 = r5;
        r112 = r7;
        goto L_0x2768;
        r3 = r1.currentPosition;
        r3 = r1.getAdditionalWidthForPosition(r3);
        r9 = r9 + r3;
        r3 = r1.currentMessagesGroup;
        r3 = r3.messages;
        r3 = r3.size();
        r6 = r4;
        r4 = 0;
        if (r4 >= r3) goto L_0x275f;
        r8 = r1.currentMessagesGroup;
        r8 = r8.messages;
        r8 = r8.get(r4);
        r8 = (org.telegram.messenger.MessageObject) r8;
        r12 = r1.currentMessagesGroup;
        r12 = r12.posArray;
        r12 = r12.get(r4);
        r12 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r12;
        r13 = r1.currentPosition;
        if (r12 == r13) goto L_0x272e;
        r13 = r12.flags;
        r14 = 8;
        r13 = r13 & r14;
        if (r13 == 0) goto L_0x272e;
        r13 = r12.pw;
        r13 = (float) r13;
        r14 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r13 = r13 / r14;
        r14 = (float) r11;
        r13 = r13 * r14;
        r13 = (double) r13;
        r13 = java.lang.Math.ceil(r13);
        r6 = (int) r13;
        r13 = r12.minY;
        if (r13 == 0) goto L_0x26c9;
        r13 = r126;
        r14 = r126.isOutOwner();
        if (r14 == 0) goto L_0x2604;
        r14 = r12.flags;
        r15 = 1;
        r14 = r14 & r15;
        if (r14 != 0) goto L_0x2610;
        r14 = r126.isOutOwner();
        if (r14 != 0) goto L_0x26c9;
        r14 = r12.flags;
        r15 = 2;
        r14 = r14 & r15;
        if (r14 == 0) goto L_0x26c9;
        r14 = 0;
        r15 = 0;
        r108 = r2;
        r2 = r15;
        r15 = r14;
        r14 = 0;
        r109 = r3;
        r3 = r1.currentMessagesGroup;
        r3 = r3.posArray;
        r3 = r3.size();
        if (r14 >= r3) goto L_0x26b9;
        r3 = r1.currentMessagesGroup;
        r3 = r3.posArray;
        r3 = r3.get(r14);
        r3 = (org.telegram.messenger.MessageObject.GroupedMessagePosition) r3;
        r110 = r5;
        r5 = r3.minY;
        if (r5 != 0) goto L_0x2668;
        r111 = r4;
        r4 = (double) r15;
        r112 = r7;
        r7 = r3.pw;
        r7 = (float) r7;
        r17 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r7 = r7 / r17;
        r113 = r10;
        r10 = (float) r11;
        r7 = r7 * r10;
        r114 = r9;
        r9 = (double) r7;
        r9 = java.lang.Math.ceil(r9);
        r7 = r3.leftSpanOffset;
        if (r7 == 0) goto L_0x265f;
        r7 = r3.leftSpanOffset;
        r7 = (float) r7;
        r17 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r7 = r7 / r17;
        r13 = (float) r11;
        r7 = r7 * r13;
        r115 = r8;
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        goto L_0x2663;
        r115 = r8;
        r7 = 0;
        r9 = r9 + r7;
        r4 = r4 + r9;
        r4 = (int) r4;
        r15 = r4;
        goto L_0x26a5;
        r111 = r4;
        r112 = r7;
        r115 = r8;
        r114 = r9;
        r113 = r10;
        r4 = r3.minY;
        r5 = r12.minY;
        if (r4 != r5) goto L_0x269e;
        r4 = (double) r2;
        r7 = r3.pw;
        r7 = (float) r7;
        r8 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r7 = r7 / r8;
        r8 = (float) r11;
        r7 = r7 * r8;
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        r9 = r3.leftSpanOffset;
        if (r9 == 0) goto L_0x2698;
        r9 = r3.leftSpanOffset;
        r9 = (float) r9;
        r10 = 1148846080; // 0x447a0000 float:1000.0 double:5.676053805E-315;
        r9 = r9 / r10;
        r10 = (float) r11;
        r9 = r9 * r10;
        r9 = (double) r9;
        r9 = java.lang.Math.ceil(r9);
        goto L_0x269a;
        r9 = 0;
        r7 = r7 + r9;
        r4 = r4 + r7;
        r2 = (int) r4;
        goto L_0x26a5;
        r4 = r3.minY;
        r5 = r12.minY;
        if (r4 <= r5) goto L_0x26a5;
        goto L_0x26c5;
        r14 = r14 + 1;
        r3 = r109;
        r5 = r110;
        r4 = r111;
        r7 = r112;
        r10 = r113;
        r9 = r114;
        r8 = r115;
        r13 = r126;
        goto L_0x2617;
        r111 = r4;
        r110 = r5;
        r112 = r7;
        r115 = r8;
        r114 = r9;
        r113 = r10;
        r3 = r15 - r2;
        r6 = r6 + r3;
        goto L_0x26d9;
        r108 = r2;
        r109 = r3;
        r111 = r4;
        r110 = r5;
        r112 = r7;
        r115 = r8;
        r114 = r9;
        r113 = r10;
        r2 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r6 = r6 - r2;
        r2 = r1.isChat;
        if (r2 == 0) goto L_0x2700;
        r2 = r115;
        r3 = r2.isOutOwner();
        if (r3 != 0) goto L_0x2702;
        r3 = r2.needDrawAvatar();
        if (r3 == 0) goto L_0x2702;
        if (r12 == 0) goto L_0x26f8;
        r3 = r12.edge;
        if (r3 == 0) goto L_0x2702;
        r3 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r6 = r6 - r3;
        goto L_0x2702;
        r2 = r115;
        r3 = r1.getAdditionalWidthForPosition(r12);
        r6 = r6 + r3;
        r3 = r12.edge;
        if (r3 != 0) goto L_0x2712;
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r6 = r6 + r4;
        r9 = r114 + r6;
        r3 = r12.minX;
        r4 = r1.currentPosition;
        r4 = r4.minX;
        if (r3 < r4) goto L_0x2728;
        r3 = r1.currentMessagesGroup;
        r3 = r3.hasSibling;
        if (r3 == 0) goto L_0x273f;
        r3 = r12.minY;
        r4 = r12.maxY;
        if (r3 == r4) goto L_0x273f;
        r3 = r1.captionOffsetX;
        r3 = r3 - r6;
        r1.captionOffsetX = r3;
        goto L_0x273f;
        r108 = r2;
        r109 = r3;
        r111 = r4;
        r110 = r5;
        r112 = r7;
        r2 = r8;
        r114 = r9;
        r113 = r10;
        r9 = r114;
        r3 = r2.caption;
        if (r3 == 0) goto L_0x2751;
        r3 = r1.currentCaption;
        if (r3 == 0) goto L_0x274d;
        r3 = 0;
        r1.currentCaption = r3;
        r4 = r6;
        goto L_0x2768;
        r3 = r2.caption;
        r1.currentCaption = r3;
        r4 = r111 + 1;
        r2 = r108;
        r3 = r109;
        r5 = r110;
        r7 = r112;
        r10 = r113;
        goto L_0x25c3;
        r108 = r2;
        r110 = r5;
        r112 = r7;
        r114 = r9;
        r4 = r6;
        r3 = r108;
        r2 = r112;
        r5 = r126;
        goto L_0x27a7;
        r103 = r3;
        r104 = r5;
        r105 = r6;
        r107 = r7;
        r106 = r8;
        r2 = r4;
        r3 = r107;
        r5 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = r5 + r4;
        r1.backgroundWidth = r5;
        r5 = r1.mediaBackground;
        if (r5 != 0) goto L_0x2794;
        r5 = r1.backgroundWidth;
        r6 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 + r6;
        r1.backgroundWidth = r5;
        r5 = r126;
        r6 = r5.caption;
        r1.currentCaption = r6;
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r9 = r2 - r7;
        r110 = r3;
        r3 = r2;
        r2 = r107;
        r6 = r1.currentCaption;
        if (r6 == 0) goto L_0x288f;
        r6 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x2883 }
        r7 = 24;
        if (r6 < r7) goto L_0x27dc;
        r6 = r1.currentCaption;	 Catch:{ Exception -> 0x27d6 }
        r7 = r1.currentCaption;	 Catch:{ Exception -> 0x27d6 }
        r7 = r7.length();	 Catch:{ Exception -> 0x27d6 }
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x27d6 }
        r10 = 0;	 Catch:{ Exception -> 0x27d6 }
        r6 = android.text.StaticLayout.Builder.obtain(r6, r10, r7, r8, r9);	 Catch:{ Exception -> 0x27d6 }
        r7 = 1;	 Catch:{ Exception -> 0x27d6 }
        r6 = r6.setBreakStrategy(r7);	 Catch:{ Exception -> 0x27d6 }
        r6 = r6.setHyphenationFrequency(r10);	 Catch:{ Exception -> 0x27d6 }
        r7 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x27d6 }
        r6 = r6.setAlignment(r7);	 Catch:{ Exception -> 0x27d6 }
        r6 = r6.build();	 Catch:{ Exception -> 0x27d6 }
        r1.captionLayout = r6;	 Catch:{ Exception -> 0x27d6 }
        goto L_0x27f5;
    L_0x27d6:
        r0 = move-exception;
        r6 = r0;
        r10 = r106;
        goto L_0x2887;
        r6 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x2883 }
        r7 = r1.currentCaption;	 Catch:{ Exception -> 0x2883 }
        r26 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x2883 }
        r28 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x2883 }
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x2883 }
        r30 = 0;	 Catch:{ Exception -> 0x2883 }
        r31 = 0;	 Catch:{ Exception -> 0x2883 }
        r24 = r6;	 Catch:{ Exception -> 0x2883 }
        r25 = r7;	 Catch:{ Exception -> 0x2883 }
        r27 = r9;	 Catch:{ Exception -> 0x2883 }
        r24.<init>(r25, r26, r27, r28, r29, r30, r31);	 Catch:{ Exception -> 0x2883 }
        r1.captionLayout = r6;	 Catch:{ Exception -> 0x2883 }
        r6 = r1.captionLayout;	 Catch:{ Exception -> 0x2883 }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x2883 }
        if (r6 <= 0) goto L_0x287c;	 Catch:{ Exception -> 0x2883 }
        r1.captionWidth = r9;	 Catch:{ Exception -> 0x2883 }
        r6 = r1.captionLayout;	 Catch:{ Exception -> 0x2883 }
        r6 = r6.getHeight();	 Catch:{ Exception -> 0x2883 }
        r1.captionHeight = r6;	 Catch:{ Exception -> 0x2883 }
        r6 = r1.captionHeight;	 Catch:{ Exception -> 0x2883 }
        r7 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;	 Catch:{ Exception -> 0x2883 }
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);	 Catch:{ Exception -> 0x2883 }
        r6 = r6 + r7;	 Catch:{ Exception -> 0x2883 }
        r1.addedCaptionHeight = r6;	 Catch:{ Exception -> 0x2883 }
        r6 = r1.currentPosition;	 Catch:{ Exception -> 0x2883 }
        if (r6 == 0) goto L_0x2826;
        r6 = r1.currentPosition;	 Catch:{ Exception -> 0x27d6 }
        r6 = r6.flags;	 Catch:{ Exception -> 0x27d6 }
        r7 = 8;	 Catch:{ Exception -> 0x27d6 }
        r6 = r6 & r7;	 Catch:{ Exception -> 0x27d6 }
        if (r6 == 0) goto L_0x2820;	 Catch:{ Exception -> 0x27d6 }
        goto L_0x2826;	 Catch:{ Exception -> 0x27d6 }
        r6 = 0;	 Catch:{ Exception -> 0x27d6 }
        r1.captionLayout = r6;	 Catch:{ Exception -> 0x27d6 }
        r10 = r106;
        goto L_0x287e;
        r6 = r1.addedCaptionHeight;	 Catch:{ Exception -> 0x2883 }
        r6 = r91 + r6;
        r7 = r1.captionLayout;	 Catch:{ Exception -> 0x2876 }
        r8 = r1.captionLayout;	 Catch:{ Exception -> 0x2876 }
        r8 = r8.getLineCount();	 Catch:{ Exception -> 0x2876 }
        r10 = 1;	 Catch:{ Exception -> 0x2876 }
        r8 = r8 - r10;	 Catch:{ Exception -> 0x2876 }
        r7 = r7.getLineWidth(r8);	 Catch:{ Exception -> 0x2876 }
        r8 = r1.captionLayout;	 Catch:{ Exception -> 0x2876 }
        r10 = r1.captionLayout;	 Catch:{ Exception -> 0x2876 }
        r10 = r10.getLineCount();	 Catch:{ Exception -> 0x2876 }
        r11 = 1;	 Catch:{ Exception -> 0x2876 }
        r10 = r10 - r11;	 Catch:{ Exception -> 0x2876 }
        r8 = r8.getLineLeft(r10);	 Catch:{ Exception -> 0x2876 }
        r7 = r7 + r8;	 Catch:{ Exception -> 0x2876 }
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Exception -> 0x2876 }
        r10 = org.telegram.messenger.AndroidUtilities.dp(r8);	 Catch:{ Exception -> 0x2876 }
        r10 = r10 + r9;
        r8 = (float) r10;
        r8 = r8 - r7;
        r10 = r106;
        r11 = (float) r10;
        r8 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1));
        if (r8 >= 0) goto L_0x2871;
        r8 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);	 Catch:{ Exception -> 0x286c }
        r6 = r6 + r8;	 Catch:{ Exception -> 0x286c }
        r8 = r1.addedCaptionHeight;	 Catch:{ Exception -> 0x286c }
        r11 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;	 Catch:{ Exception -> 0x286c }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);	 Catch:{ Exception -> 0x286c }
        r8 = r8 + r11;	 Catch:{ Exception -> 0x286c }
        r1.addedCaptionHeight = r8;	 Catch:{ Exception -> 0x286c }
        r7 = 1;
        r14 = r7;
        goto L_0x2873;
    L_0x286c:
        r0 = move-exception;
        r91 = r6;
        r6 = r0;
        goto L_0x2887;
        r14 = r21;
        r21 = r14;
        goto L_0x2880;
    L_0x2876:
        r0 = move-exception;
        r10 = r106;
        r91 = r6;
        goto L_0x2886;
        r10 = r106;
        r6 = r91;
        r14 = r21;
        goto L_0x2895;
    L_0x2883:
        r0 = move-exception;
        r10 = r106;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r14 = r21;
        r6 = r91;
        goto L_0x2895;
        r10 = r106;
        r14 = r21;
        r6 = r91;
        r7 = java.util.Locale.US;
        r8 = "%d_%d";
        r11 = 2;
        r12 = new java.lang.Object[r11];
        r11 = (float) r4;
        r13 = org.telegram.messenger.AndroidUtilities.density;
        r11 = r11 / r13;
        r11 = (int) r11;
        r11 = java.lang.Integer.valueOf(r11);
        r13 = 0;
        r12[r13] = r11;
        r11 = (float) r2;
        r13 = org.telegram.messenger.AndroidUtilities.density;
        r11 = r11 / r13;
        r11 = (int) r11;
        r11 = java.lang.Integer.valueOf(r11);
        r13 = 1;
        r12[r13] = r11;
        r7 = java.lang.String.format(r7, r8, r12);
        r1.currentPhotoFilterThumb = r7;
        r1.currentPhotoFilter = r7;
        r7 = r5.photoThumbs;
        if (r7 == 0) goto L_0x28c8;
        r7 = r5.photoThumbs;
        r7 = r7.size();
        if (r7 > r13) goto L_0x28d8;
        r7 = r5.type;
        r8 = 3;
        if (r7 == r8) goto L_0x28d8;
        r7 = r5.type;
        r8 = 8;
        if (r7 == r8) goto L_0x28d8;
        r7 = r5.type;
        r8 = 5;
        if (r7 != r8) goto L_0x291e;
        r7 = r126.needDrawBluredPreview();
        if (r7 == 0) goto L_0x2909;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r1.currentPhotoFilter;
        r7.append(r8);
        r8 = "_b2";
        r7.append(r8);
        r7 = r7.toString();
        r1.currentPhotoFilter = r7;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r1.currentPhotoFilterThumb;
        r7.append(r8);
        r8 = "_b2";
        r7.append(r8);
        r7 = r7.toString();
        r1.currentPhotoFilterThumb = r7;
        goto L_0x291e;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r1.currentPhotoFilterThumb;
        r7.append(r8);
        r8 = "_b";
        r7.append(r8);
        r7 = r7.toString();
        r1.currentPhotoFilterThumb = r7;
        r7 = 0;
        r8 = r5.type;
        r11 = 3;
        if (r8 == r11) goto L_0x292f;
        r8 = r5.type;
        r11 = 8;
        if (r8 == r11) goto L_0x292f;
        r8 = r5.type;
        r11 = 5;
        if (r8 != r11) goto L_0x2930;
        r7 = 1;
        r8 = r1.currentPhotoObject;
        if (r8 == 0) goto L_0x2941;
        if (r7 != 0) goto L_0x2941;
        r8 = r1.currentPhotoObject;
        r8 = r8.size;
        if (r8 != 0) goto L_0x2941;
        r8 = r1.currentPhotoObject;
        r11 = -1;
        r8.size = r11;
        r8 = r5.type;
        r11 = 1;
        if (r8 != r11) goto L_0x2a28;
        r8 = r5.useCustomPhoto;
        if (r8 == 0) goto L_0x295e;
        r8 = r1.photoImage;
        r11 = r125.getResources();
        r12 = 2131165671; // 0x7f0701e7 float:1.7945566E38 double:1.0529357436E-314;
        r11 = r11.getDrawable(r12);
        r8.setImageBitmap(r11);
        r117 = r3;
        goto L_0x2b55;
        r8 = r1.currentPhotoObject;
        if (r8 == 0) goto L_0x2a19;
        r8 = 1;
        r11 = r1.currentPhotoObject;
        r11 = org.telegram.messenger.FileLoader.getAttachFileName(r11);
        r12 = r5.mediaExists;
        if (r12 == 0) goto L_0x2977;
        r12 = r1.currentAccount;
        r12 = org.telegram.messenger.DownloadController.getInstance(r12);
        r12.removeLoadingFileObserver(r1);
        goto L_0x2978;
        r8 = 0;
        if (r8 != 0) goto L_0x29d5;
        r12 = r1.currentAccount;
        r12 = org.telegram.messenger.DownloadController.getInstance(r12);
        r13 = r1.currentMessageObject;
        r12 = r12.canDownloadMedia(r13);
        if (r12 != 0) goto L_0x29d5;
        r12 = r1.currentAccount;
        r12 = org.telegram.messenger.FileLoader.getInstance(r12);
        r12 = r12.isLoadingFile(r11);
        if (r12 == 0) goto L_0x2997;
        r116 = r2;
        goto L_0x29d7;
        r12 = 1;
        r1.photoNotSet = r12;
        r12 = r1.currentPhotoObjectThumb;
        if (r12 == 0) goto L_0x29c7;
        r12 = r1.photoImage;
        r25 = 0;
        r26 = 0;
        r13 = r1.currentPhotoObjectThumb;
        r13 = r13.location;
        r15 = r1.currentPhotoFilterThumb;
        r29 = 0;
        r30 = 0;
        r116 = r2;
        r2 = r1.currentMessageObject;
        r2 = r2.shouldEncryptPhotoOrVideo();
        if (r2 == 0) goto L_0x29bb;
        r31 = 2;
        goto L_0x29bd;
        r31 = 0;
        r24 = r12;
        r27 = r13;
        r28 = r15;
        r24.setImage(r25, r26, r27, r28, r29, r30, r31);
        goto L_0x29d2;
        r116 = r2;
        r2 = r1.photoImage;
        r12 = 0;
        r13 = r12;
        r13 = (android.graphics.drawable.Drawable) r13;
        r2.setImageBitmap(r13);
        r117 = r3;
        goto L_0x2a17;
        r116 = r2;
        r2 = r1.photoImage;
        r12 = r1.currentPhotoObject;
        r12 = r12.location;
        r13 = r1.currentPhotoFilter;
        r15 = r1.currentPhotoObjectThumb;
        if (r15 == 0) goto L_0x29ea;
        r15 = r1.currentPhotoObjectThumb;
        r15 = r15.location;
        r27 = r15;
        goto L_0x29ec;
        r27 = 0;
        r15 = r1.currentPhotoFilterThumb;
        if (r7 == 0) goto L_0x29f5;
        r117 = r3;
        r29 = 0;
        goto L_0x29fd;
        r117 = r3;
        r3 = r1.currentPhotoObject;
        r3 = r3.size;
        r29 = r3;
        r30 = 0;
        r3 = r1.currentMessageObject;
        r3 = r3.shouldEncryptPhotoOrVideo();
        if (r3 == 0) goto L_0x2a0a;
        r31 = 2;
        goto L_0x2a0c;
        r31 = 0;
        r24 = r2;
        r25 = r12;
        r26 = r13;
        r28 = r15;
        r24.setImage(r25, r26, r27, r28, r29, r30, r31);
        goto L_0x2b55;
        r116 = r2;
        r117 = r3;
        r2 = r1.photoImage;
        r3 = 0;
        r8 = r3;
        r8 = (android.graphics.drawable.BitmapDrawable) r8;
        r2.setImageBitmap(r8);
        goto L_0x2b55;
        r116 = r2;
        r117 = r3;
        r2 = r5.type;
        r3 = 8;
        if (r2 == r3) goto L_0x2a67;
        r2 = r5.type;
        r3 = 5;
        if (r2 != r3) goto L_0x2a38;
        goto L_0x2a67;
        r2 = r1.photoImage;
        r25 = 0;
        r26 = 0;
        r3 = r1.currentPhotoObject;
        if (r3 == 0) goto L_0x2a49;
        r3 = r1.currentPhotoObject;
        r3 = r3.location;
        r27 = r3;
        goto L_0x2a4b;
        r27 = 0;
        r3 = r1.currentPhotoFilterThumb;
        r29 = 0;
        r30 = 0;
        r8 = r1.currentMessageObject;
        r8 = r8.shouldEncryptPhotoOrVideo();
        if (r8 == 0) goto L_0x2a5c;
        r31 = 2;
        goto L_0x2a5e;
        r31 = 0;
        r24 = r2;
        r28 = r3;
        r24.setImage(r25, r26, r27, r28, r29, r30, r31);
        goto L_0x2b55;
        r2 = r5.messageOwner;
        r2 = r2.media;
        r2 = r2.document;
        r2 = org.telegram.messenger.FileLoader.getAttachFileName(r2);
        r3 = 0;
        r8 = r5.attachPathExists;
        if (r8 == 0) goto L_0x2a81;
        r8 = r1.currentAccount;
        r8 = org.telegram.messenger.DownloadController.getInstance(r8);
        r8.removeLoadingFileObserver(r1);
        r3 = 1;
        goto L_0x2a86;
        r8 = r5.mediaExists;
        if (r8 == 0) goto L_0x2a86;
        r3 = 2;
        r8 = 0;
        r11 = r5.messageOwner;
        r11 = r11.media;
        r11 = r11.document;
        r11 = org.telegram.messenger.MessageObject.isNewGifDocument(r11);
        if (r11 == 0) goto L_0x2aa0;
        r11 = r1.currentAccount;
        r11 = org.telegram.messenger.DownloadController.getInstance(r11);
        r12 = r1.currentMessageObject;
        r8 = r11.canDownloadMedia(r12);
        goto L_0x2ab1;
        r11 = r5.type;
        r12 = 5;
        if (r11 != r12) goto L_0x2ab1;
        r11 = r1.currentAccount;
        r11 = org.telegram.messenger.DownloadController.getInstance(r11);
        r12 = r1.currentMessageObject;
        r8 = r11.canDownloadMedia(r12);
        r11 = r126.isSending();
        if (r11 != 0) goto L_0x2b2f;
        if (r3 != 0) goto L_0x2ac7;
        r11 = r1.currentAccount;
        r11 = org.telegram.messenger.FileLoader.getInstance(r11);
        r11 = r11.isLoadingFile(r2);
        if (r11 != 0) goto L_0x2ac7;
        if (r8 == 0) goto L_0x2b2f;
        r11 = 1;
        if (r3 != r11) goto L_0x2afe;
        r11 = r1.photoImage;
        r25 = 0;
        r12 = r126.isSendError();
        if (r12 == 0) goto L_0x2ad7;
        r26 = 0;
        goto L_0x2add;
        r12 = r5.messageOwner;
        r12 = r12.attachPath;
        r26 = r12;
        r27 = 0;
        r28 = 0;
        r12 = r1.currentPhotoObject;
        if (r12 == 0) goto L_0x2aec;
        r12 = r1.currentPhotoObject;
        r12 = r12.location;
        r29 = r12;
        goto L_0x2aee;
        r29 = 0;
        r12 = r1.currentPhotoFilterThumb;
        r31 = 0;
        r32 = 0;
        r33 = 0;
        r24 = r11;
        r30 = r12;
        r24.setImage(r25, r26, r27, r28, r29, r30, r31, r32, r33);
        goto L_0x2b54;
        r11 = r1.photoImage;
        r12 = r5.messageOwner;
        r12 = r12.media;
        r12 = r12.document;
        r40 = 0;
        r13 = r1.currentPhotoObject;
        if (r13 == 0) goto L_0x2b13;
        r13 = r1.currentPhotoObject;
        r13 = r13.location;
        r41 = r13;
        goto L_0x2b15;
        r41 = 0;
        r13 = r1.currentPhotoFilterThumb;
        r15 = r5.messageOwner;
        r15 = r15.media;
        r15 = r15.document;
        r15 = r15.size;
        r44 = 0;
        r45 = 0;
        r38 = r11;
        r39 = r12;
        r42 = r13;
        r43 = r15;
        r38.setImage(r39, r40, r41, r42, r43, r44, r45);
        goto L_0x2b54;
        r11 = 1;
        r1.photoNotSet = r11;
        r11 = r1.photoImage;
        r25 = 0;
        r26 = 0;
        r12 = r1.currentPhotoObject;
        if (r12 == 0) goto L_0x2b43;
        r12 = r1.currentPhotoObject;
        r12 = r12.location;
        r27 = r12;
        goto L_0x2b45;
        r27 = 0;
        r12 = r1.currentPhotoFilterThumb;
        r29 = 0;
        r30 = 0;
        r31 = 0;
        r24 = r11;
        r28 = r12;
        r24.setImage(r25, r26, r27, r28, r29, r30, r31);
        r4 = r110;
        r3 = r117;
        r125.setMessageObjectInternal(r126);
        r2 = r1.drawForwardedName;
        if (r2 == 0) goto L_0x2b81;
        r2 = r126.needDrawForwarded();
        if (r2 == 0) goto L_0x2b81;
        r2 = r1.currentPosition;
        if (r2 == 0) goto L_0x2b70;
        r2 = r1.currentPosition;
        r2 = r2.minY;
        if (r2 != 0) goto L_0x2b81;
        r2 = r5.type;
        r7 = 5;
        if (r2 == r7) goto L_0x2b96;
        r2 = r1.namesOffset;
        r7 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r2 = r2 + r7;
        r1.namesOffset = r2;
        goto L_0x2b96;
        r2 = r1.drawNameLayout;
        if (r2 == 0) goto L_0x2b96;
        r2 = r5.messageOwner;
        r2 = r2.reply_to_msg_id;
        if (r2 != 0) goto L_0x2b96;
        r2 = r1.namesOffset;
        r7 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r2 = r2 + r7;
        r1.namesOffset = r2;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r2 = r2 + r4;
        r7 = r1.namesOffset;
        r2 = r2 + r7;
        r2 = r2 + r6;
        r1.totalHeight = r2;
        r2 = r1.currentPosition;
        if (r2 == 0) goto L_0x2bbb;
        r2 = r1.currentPosition;
        r2 = r2.flags;
        r7 = 8;
        r2 = r2 & r7;
        if (r2 != 0) goto L_0x2bbb;
        r2 = r1.totalHeight;
        r7 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r2 = r2 - r7;
        r1.totalHeight = r2;
        r2 = 0;
        r7 = r1.currentPosition;
        if (r7 == 0) goto L_0x2bed;
        r7 = r1.currentPosition;
        r7 = r1.getAdditionalWidthForPosition(r7);
        r3 = r3 + r7;
        r7 = r1.currentPosition;
        r7 = r7.flags;
        r7 = r7 & 4;
        if (r7 != 0) goto L_0x2bdd;
        r7 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r4 = r4 + r7;
        r7 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r2 = r2 - r7;
        r7 = r1.currentPosition;
        r7 = r7.flags;
        r8 = 8;
        r7 = r7 & r8;
        if (r7 != 0) goto L_0x2bed;
        r7 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r4 = r4 + r7;
        r7 = r1.drawPinnedTop;
        if (r7 == 0) goto L_0x2bfc;
        r7 = r1.namesOffset;
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r7 = r7 - r9;
        r1.namesOffset = r7;
        r7 = r1.photoImage;
        r8 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r9 = r1.namesOffset;
        r8 = r8 + r9;
        r8 = r8 + r2;
        r9 = 0;
        r7.setImageCoords(r9, r8, r3, r4);
        r125.invalidate();
        r21 = r14;
        r2 = r1.currentPosition;
        if (r2 != 0) goto L_0x2cf3;
        r2 = r1.captionLayout;
        if (r2 != 0) goto L_0x2cf3;
        r2 = r5.caption;
        if (r2 == 0) goto L_0x2cf3;
        r2 = r5.type;
        r3 = 13;
        if (r2 == r3) goto L_0x2cf3;
        r2 = r5.caption;	 Catch:{ Exception -> 0x2ced }
        r1.currentCaption = r2;	 Catch:{ Exception -> 0x2ced }
        r2 = r1.backgroundWidth;	 Catch:{ Exception -> 0x2ced }
        r3 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;	 Catch:{ Exception -> 0x2ced }
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);	 Catch:{ Exception -> 0x2ced }
        r2 = r2 - r3;	 Catch:{ Exception -> 0x2ced }
        r3 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;	 Catch:{ Exception -> 0x2ced }
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);	 Catch:{ Exception -> 0x2ced }
        r3 = r2 - r4;	 Catch:{ Exception -> 0x2ced }
        r4 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Exception -> 0x2ced }
        r6 = 24;	 Catch:{ Exception -> 0x2ced }
        if (r4 < r6) goto L_0x2c63;	 Catch:{ Exception -> 0x2ced }
        r4 = r5.caption;	 Catch:{ Exception -> 0x2ced }
        r6 = r5.caption;	 Catch:{ Exception -> 0x2ced }
        r6 = r6.length();	 Catch:{ Exception -> 0x2ced }
        r7 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x2ced }
        r8 = 0;	 Catch:{ Exception -> 0x2ced }
        r4 = android.text.StaticLayout.Builder.obtain(r4, r8, r6, r7, r3);	 Catch:{ Exception -> 0x2ced }
        r6 = 1;	 Catch:{ Exception -> 0x2ced }
        r4 = r4.setBreakStrategy(r6);	 Catch:{ Exception -> 0x2ced }
        r4 = r4.setHyphenationFrequency(r8);	 Catch:{ Exception -> 0x2ced }
        r6 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x2ced }
        r4 = r4.setAlignment(r6);	 Catch:{ Exception -> 0x2ced }
        r4 = r4.build();	 Catch:{ Exception -> 0x2ced }
        r1.captionLayout = r4;	 Catch:{ Exception -> 0x2ced }
        goto L_0x2c76;	 Catch:{ Exception -> 0x2ced }
        r4 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x2ced }
        r7 = r5.caption;	 Catch:{ Exception -> 0x2ced }
        r8 = org.telegram.ui.ActionBar.Theme.chat_msgTextPaint;	 Catch:{ Exception -> 0x2ced }
        r10 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x2ced }
        r11 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x2ced }
        r12 = 0;	 Catch:{ Exception -> 0x2ced }
        r13 = 0;	 Catch:{ Exception -> 0x2ced }
        r6 = r4;	 Catch:{ Exception -> 0x2ced }
        r9 = r3;	 Catch:{ Exception -> 0x2ced }
        r6.<init>(r7, r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x2ced }
        r1.captionLayout = r4;	 Catch:{ Exception -> 0x2ced }
        r4 = r1.captionLayout;	 Catch:{ Exception -> 0x2ced }
        r4 = r4.getLineCount();	 Catch:{ Exception -> 0x2ced }
        if (r4 <= 0) goto L_0x2cec;	 Catch:{ Exception -> 0x2ced }
        r1.captionWidth = r3;	 Catch:{ Exception -> 0x2ced }
        r4 = r1.timeWidth;	 Catch:{ Exception -> 0x2ced }
        r6 = r126.isOutOwner();	 Catch:{ Exception -> 0x2ced }
        if (r6 == 0) goto L_0x2c8f;	 Catch:{ Exception -> 0x2ced }
        r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;	 Catch:{ Exception -> 0x2ced }
        r9 = org.telegram.messenger.AndroidUtilities.dp(r6);	 Catch:{ Exception -> 0x2ced }
        goto L_0x2c90;	 Catch:{ Exception -> 0x2ced }
        r9 = 0;	 Catch:{ Exception -> 0x2ced }
        r4 = r4 + r9;	 Catch:{ Exception -> 0x2ced }
        r6 = r1.captionLayout;	 Catch:{ Exception -> 0x2ced }
        r6 = r6.getHeight();	 Catch:{ Exception -> 0x2ced }
        r1.captionHeight = r6;	 Catch:{ Exception -> 0x2ced }
        r6 = r1.totalHeight;	 Catch:{ Exception -> 0x2ced }
        r7 = r1.captionHeight;	 Catch:{ Exception -> 0x2ced }
        r8 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;	 Catch:{ Exception -> 0x2ced }
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);	 Catch:{ Exception -> 0x2ced }
        r7 = r7 + r8;	 Catch:{ Exception -> 0x2ced }
        r6 = r6 + r7;	 Catch:{ Exception -> 0x2ced }
        r1.totalHeight = r6;	 Catch:{ Exception -> 0x2ced }
        r6 = r1.captionLayout;	 Catch:{ Exception -> 0x2ced }
        r7 = r1.captionLayout;	 Catch:{ Exception -> 0x2ced }
        r7 = r7.getLineCount();	 Catch:{ Exception -> 0x2ced }
        r8 = 1;	 Catch:{ Exception -> 0x2ced }
        r7 = r7 - r8;	 Catch:{ Exception -> 0x2ced }
        r6 = r6.getLineWidth(r7);	 Catch:{ Exception -> 0x2ced }
        r7 = r1.captionLayout;	 Catch:{ Exception -> 0x2ced }
        r8 = r1.captionLayout;	 Catch:{ Exception -> 0x2ced }
        r8 = r8.getLineCount();	 Catch:{ Exception -> 0x2ced }
        r9 = 1;	 Catch:{ Exception -> 0x2ced }
        r8 = r8 - r9;	 Catch:{ Exception -> 0x2ced }
        r7 = r7.getLineLeft(r8);	 Catch:{ Exception -> 0x2ced }
        r6 = r6 + r7;	 Catch:{ Exception -> 0x2ced }
        r7 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;	 Catch:{ Exception -> 0x2ced }
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);	 Catch:{ Exception -> 0x2ced }
        r7 = r2 - r7;	 Catch:{ Exception -> 0x2ced }
        r7 = (float) r7;	 Catch:{ Exception -> 0x2ced }
        r7 = r7 - r6;	 Catch:{ Exception -> 0x2ced }
        r8 = (float) r4;	 Catch:{ Exception -> 0x2ced }
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));	 Catch:{ Exception -> 0x2ced }
        if (r7 >= 0) goto L_0x2cec;	 Catch:{ Exception -> 0x2ced }
        r7 = r1.totalHeight;	 Catch:{ Exception -> 0x2ced }
        r8 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;	 Catch:{ Exception -> 0x2ced }
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);	 Catch:{ Exception -> 0x2ced }
        r7 = r7 + r8;	 Catch:{ Exception -> 0x2ced }
        r1.totalHeight = r7;	 Catch:{ Exception -> 0x2ced }
        r7 = r1.captionHeight;	 Catch:{ Exception -> 0x2ced }
        r8 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;	 Catch:{ Exception -> 0x2ced }
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);	 Catch:{ Exception -> 0x2ced }
        r7 = r7 + r8;	 Catch:{ Exception -> 0x2ced }
        r1.captionHeight = r7;	 Catch:{ Exception -> 0x2ced }
        r2 = 2;
        r21 = r2;
        goto L_0x2d0c;
    L_0x2ced:
        r0 = move-exception;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);
        goto L_0x2cec;
        r2 = r1.widthBeforeNewTimeLine;
        r3 = -1;
        if (r2 == r3) goto L_0x2d0c;
        r2 = r1.availableTimeWidth;
        r3 = r1.widthBeforeNewTimeLine;
        r2 = r2 - r3;
        r3 = r1.timeWidth;
        if (r2 >= r3) goto L_0x2d0c;
        r2 = r1.totalHeight;
        r3 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 + r3;
        r1.totalHeight = r2;
        r2 = r21;
        r3 = r1.currentMessageObject;
        r3 = r3.eventId;
        r6 = 0;
        r8 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));
        if (r8 == 0) goto L_0x2e35;
        r3 = r1.currentMessageObject;
        r3 = r3.isMediaEmpty();
        if (r3 != 0) goto L_0x2e35;
        r3 = r1.currentMessageObject;
        r3 = r3.messageOwner;
        r3 = r3.media;
        r3 = r3.webpage;
        if (r3 == 0) goto L_0x2e35;
        r3 = r1.backgroundWidth;
        r4 = 1109655552; // 0x42240000 float:41.0 double:5.48242687E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r4 = 1;
        r1.hasOldCaptionPreview = r4;
        r4 = 0;
        r1.linkPreviewHeight = r4;
        r4 = r1.currentMessageObject;
        r4 = r4.messageOwner;
        r4 = r4.media;
        r4 = r4.webpage;
        r6 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x2d94 }
        r7 = r4.site_name;	 Catch:{ Exception -> 0x2d94 }
        r6 = r6.measureText(r7);	 Catch:{ Exception -> 0x2d94 }
        r7 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x2d94 }
        r6 = r6 + r7;	 Catch:{ Exception -> 0x2d94 }
        r6 = (double) r6;	 Catch:{ Exception -> 0x2d94 }
        r6 = java.lang.Math.ceil(r6);	 Catch:{ Exception -> 0x2d94 }
        r6 = (int) r6;	 Catch:{ Exception -> 0x2d94 }
        r1.siteNameWidth = r6;	 Catch:{ Exception -> 0x2d94 }
        r15 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x2d94 }
        r8 = r4.site_name;	 Catch:{ Exception -> 0x2d94 }
        r9 = org.telegram.ui.ActionBar.Theme.chat_replyNamePaint;	 Catch:{ Exception -> 0x2d94 }
        r10 = java.lang.Math.min(r6, r3);	 Catch:{ Exception -> 0x2d94 }
        r11 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x2d94 }
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x2d94 }
        r13 = 0;	 Catch:{ Exception -> 0x2d94 }
        r14 = 0;	 Catch:{ Exception -> 0x2d94 }
        r7 = r15;	 Catch:{ Exception -> 0x2d94 }
        r7.<init>(r8, r9, r10, r11, r12, r13, r14);	 Catch:{ Exception -> 0x2d94 }
        r1.siteNameLayout = r15;	 Catch:{ Exception -> 0x2d94 }
        r7 = r1.siteNameLayout;	 Catch:{ Exception -> 0x2d94 }
        r8 = 0;	 Catch:{ Exception -> 0x2d94 }
        r7 = r7.getLineLeft(r8);	 Catch:{ Exception -> 0x2d94 }
        r8 = 0;	 Catch:{ Exception -> 0x2d94 }
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));	 Catch:{ Exception -> 0x2d94 }
        if (r7 == 0) goto L_0x2d78;	 Catch:{ Exception -> 0x2d94 }
        r7 = 1;	 Catch:{ Exception -> 0x2d94 }
        goto L_0x2d79;	 Catch:{ Exception -> 0x2d94 }
        r7 = 0;	 Catch:{ Exception -> 0x2d94 }
        r1.siteNameRtl = r7;	 Catch:{ Exception -> 0x2d94 }
        r7 = r1.siteNameLayout;	 Catch:{ Exception -> 0x2d94 }
        r8 = r1.siteNameLayout;	 Catch:{ Exception -> 0x2d94 }
        r8 = r8.getLineCount();	 Catch:{ Exception -> 0x2d94 }
        r9 = 1;	 Catch:{ Exception -> 0x2d94 }
        r8 = r8 - r9;	 Catch:{ Exception -> 0x2d94 }
        r7 = r7.getLineBottom(r8);	 Catch:{ Exception -> 0x2d94 }
        r8 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x2d94 }
        r8 = r8 + r7;	 Catch:{ Exception -> 0x2d94 }
        r1.linkPreviewHeight = r8;	 Catch:{ Exception -> 0x2d94 }
        r8 = r1.totalHeight;	 Catch:{ Exception -> 0x2d94 }
        r8 = r8 + r7;	 Catch:{ Exception -> 0x2d94 }
        r1.totalHeight = r8;	 Catch:{ Exception -> 0x2d94 }
        goto L_0x2d99;
    L_0x2d94:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r6 = 0;
        r1.descriptionX = r6;	 Catch:{ Exception -> 0x2e0a }
        r6 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x2e0a }
        if (r6 == 0) goto L_0x2dab;	 Catch:{ Exception -> 0x2e0a }
        r6 = r1.totalHeight;	 Catch:{ Exception -> 0x2e0a }
        r7 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Exception -> 0x2e0a }
        r8 = org.telegram.messenger.AndroidUtilities.dp(r7);	 Catch:{ Exception -> 0x2e0a }
        r6 = r6 + r8;	 Catch:{ Exception -> 0x2e0a }
        r1.totalHeight = r6;	 Catch:{ Exception -> 0x2e0a }
        r6 = r4.description;	 Catch:{ Exception -> 0x2e0a }
        r7 = org.telegram.ui.ActionBar.Theme.chat_replyTextPaint;	 Catch:{ Exception -> 0x2e0a }
        r9 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x2e0a }
        r10 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x2e0a }
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x2e0a }
        r11 = org.telegram.messenger.AndroidUtilities.dp(r8);	 Catch:{ Exception -> 0x2e0a }
        r11 = (float) r11;	 Catch:{ Exception -> 0x2e0a }
        r12 = 0;	 Catch:{ Exception -> 0x2e0a }
        r13 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x2e0a }
        r15 = 6;	 Catch:{ Exception -> 0x2e0a }
        r8 = r3;	 Catch:{ Exception -> 0x2e0a }
        r14 = r3;	 Catch:{ Exception -> 0x2e0a }
        r6 = org.telegram.ui.Components.StaticLayoutEx.createStaticLayout(r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);	 Catch:{ Exception -> 0x2e0a }
        r1.descriptionLayout = r6;	 Catch:{ Exception -> 0x2e0a }
        r6 = r1.descriptionLayout;	 Catch:{ Exception -> 0x2e0a }
        r7 = r1.descriptionLayout;	 Catch:{ Exception -> 0x2e0a }
        r7 = r7.getLineCount();	 Catch:{ Exception -> 0x2e0a }
        r8 = 1;	 Catch:{ Exception -> 0x2e0a }
        r7 = r7 - r8;	 Catch:{ Exception -> 0x2e0a }
        r6 = r6.getLineBottom(r7);	 Catch:{ Exception -> 0x2e0a }
        r7 = r1.linkPreviewHeight;	 Catch:{ Exception -> 0x2e0a }
        r7 = r7 + r6;	 Catch:{ Exception -> 0x2e0a }
        r1.linkPreviewHeight = r7;	 Catch:{ Exception -> 0x2e0a }
        r7 = r1.totalHeight;	 Catch:{ Exception -> 0x2e0a }
        r7 = r7 + r6;	 Catch:{ Exception -> 0x2e0a }
        r1.totalHeight = r7;	 Catch:{ Exception -> 0x2e0a }
        r7 = 0;	 Catch:{ Exception -> 0x2e0a }
        r8 = r1.descriptionLayout;	 Catch:{ Exception -> 0x2e0a }
        r8 = r8.getLineCount();	 Catch:{ Exception -> 0x2e0a }
        if (r7 >= r8) goto L_0x2e09;	 Catch:{ Exception -> 0x2e0a }
        r8 = r1.descriptionLayout;	 Catch:{ Exception -> 0x2e0a }
        r8 = r8.getLineLeft(r7);	 Catch:{ Exception -> 0x2e0a }
        r8 = (double) r8;	 Catch:{ Exception -> 0x2e0a }
        r8 = java.lang.Math.ceil(r8);	 Catch:{ Exception -> 0x2e0a }
        r8 = (int) r8;	 Catch:{ Exception -> 0x2e0a }
        if (r8 == 0) goto L_0x2e06;	 Catch:{ Exception -> 0x2e0a }
        r9 = r1.descriptionX;	 Catch:{ Exception -> 0x2e0a }
        if (r9 != 0) goto L_0x2dfd;	 Catch:{ Exception -> 0x2e0a }
        r9 = -r8;	 Catch:{ Exception -> 0x2e0a }
        r1.descriptionX = r9;	 Catch:{ Exception -> 0x2e0a }
        goto L_0x2e06;	 Catch:{ Exception -> 0x2e0a }
        r9 = r1.descriptionX;	 Catch:{ Exception -> 0x2e0a }
        r10 = -r8;	 Catch:{ Exception -> 0x2e0a }
        r9 = java.lang.Math.max(r9, r10);	 Catch:{ Exception -> 0x2e0a }
        r1.descriptionX = r9;	 Catch:{ Exception -> 0x2e0a }
        r7 = r7 + 1;
        goto L_0x2ddf;
        goto L_0x2e0f;
    L_0x2e0a:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r6 = r1.totalHeight;
        r7 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r6 = r6 + r7;
        r1.totalHeight = r6;
        if (r2 == 0) goto L_0x2e35;
        r6 = r1.totalHeight;
        r7 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r6 = r6 - r7;
        r1.totalHeight = r6;
        r6 = 2;
        if (r2 != r6) goto L_0x2e35;
        r6 = r1.captionHeight;
        r7 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r6 = r6 - r7;
        r1.captionHeight = r6;
        r3 = r1.botButtons;
        r3.clear();
        if (r53 == 0) goto L_0x2e49;
        r3 = r1.botButtonsByData;
        r3.clear();
        r3 = r1.botButtonsByPosition;
        r3.clear();
        r3 = 0;
        r1.botButtonsLayout = r3;
        r3 = r1.currentPosition;
        if (r3 != 0) goto L_0x30a5;
        r3 = r5.messageOwner;
        r3 = r3.reply_markup;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_replyInlineMarkup;
        if (r3 == 0) goto L_0x30a5;
        r3 = r5.messageOwner;
        r3 = r3.reply_markup;
        r3 = r3.rows;
        r3 = r3.size();
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 * r3;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4 = r4 + r7;
        r1.keyboardHeight = r4;
        r1.substractBackgroundHeight = r4;
        r4 = r1.backgroundWidth;
        r1.widthForButtons = r4;
        r4 = 0;
        r6 = r5.wantedBotKeyboardWidth;
        r7 = r1.widthForButtons;
        if (r6 <= r7) goto L_0x2ebe;
        r6 = r1.isChat;
        if (r6 == 0) goto L_0x2e8f;
        r6 = r126.needDrawAvatar();
        if (r6 == 0) goto L_0x2e8f;
        r6 = r126.isOutOwner();
        if (r6 != 0) goto L_0x2e8f;
        r15 = 1115160576; // 0x42780000 float:62.0 double:5.5096253E-315;
        goto L_0x2e91;
        r15 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r6 = -r6;
        r7 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r7 == 0) goto L_0x2ea2;
        r7 = org.telegram.messenger.AndroidUtilities.getMinTabletSide();
        r6 = r6 + r7;
        goto L_0x2eaf;
        r7 = org.telegram.messenger.AndroidUtilities.displaySize;
        r7 = r7.x;
        r8 = org.telegram.messenger.AndroidUtilities.displaySize;
        r8 = r8.y;
        r7 = java.lang.Math.min(r7, r8);
        r6 = r6 + r7;
        r7 = r1.backgroundWidth;
        r8 = r5.wantedBotKeyboardWidth;
        r8 = java.lang.Math.min(r8, r6);
        r7 = java.lang.Math.max(r7, r8);
        r1.widthForButtons = r7;
        r4 = 1;
        r6 = 0;
        r7 = new java.util.HashMap;
        r8 = r1.botButtonsByData;
        r7.<init>(r8);
        r8 = r5.botButtonsLayout;
        if (r8 == 0) goto L_0x2ee4;
        r8 = r1.botButtonsLayout;
        if (r8 == 0) goto L_0x2ee4;
        r8 = r1.botButtonsLayout;
        r9 = r5.botButtonsLayout;
        r9 = r9.toString();
        r8 = r8.equals(r9);
        if (r8 == 0) goto L_0x2ee4;
        r8 = new java.util.HashMap;
        r9 = r1.botButtonsByPosition;
        r8.<init>(r9);
        goto L_0x2ef1;
        r8 = r5.botButtonsLayout;
        if (r8 == 0) goto L_0x2ef0;
        r8 = r5.botButtonsLayout;
        r8 = r8.toString();
        r1.botButtonsLayout = r8;
        r8 = 0;
        r9 = r1.botButtonsByData;
        r9.clear();
        r9 = r6;
        r6 = 0;
        if (r6 >= r3) goto L_0x3098;
        r10 = r5.messageOwner;
        r10 = r10.reply_markup;
        r10 = r10.rows;
        r10 = r10.get(r6);
        r10 = (org.telegram.tgnet.TLRPC.TL_keyboardButtonRow) r10;
        r11 = r10.buttons;
        r11 = r11.size();
        if (r11 != 0) goto L_0x2f1e;
        r118 = r2;
        r119 = r3;
        r120 = r4;
        r121 = r7;
        r122 = r8;
        r7 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r8 = 1;
        goto L_0x308a;
        r12 = r1.widthForButtons;
        r13 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r14 = r11 + -1;
        r13 = r13 * r14;
        r12 = r12 - r13;
        if (r4 != 0) goto L_0x2f32;
        r13 = r1.mediaBackground;
        if (r13 == 0) goto L_0x2f32;
        r13 = 0;
        goto L_0x2f34;
        r13 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r12 = r12 - r13;
        r13 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r12 = r12 - r14;
        r12 = r12 / r11;
        r13 = r9;
        r9 = 0;
        r14 = r10.buttons;
        r14 = r14.size();
        if (r9 >= r14) goto L_0x307c;
        r14 = new org.telegram.ui.Cells.ChatMessageCell$BotButton;
        r15 = 0;
        r14.<init>();
        r15 = r10.buttons;
        r15 = r15.get(r9);
        r15 = (org.telegram.tgnet.TLRPC.KeyboardButton) r15;
        r14.button = r15;
        r15 = r14.button;
        r15 = r15.data;
        r15 = org.telegram.messenger.Utilities.bytesToHex(r15);
        r118 = r2;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r2.append(r6);
        r119 = r3;
        r3 = "";
        r2.append(r3);
        r2.append(r9);
        r2 = r2.toString();
        if (r8 == 0) goto L_0x2f87;
        r3 = r8.get(r2);
        r3 = (org.telegram.ui.Cells.ChatMessageCell.BotButton) r3;
        goto L_0x2f8d;
        r3 = r7.get(r15);
        r3 = (org.telegram.ui.Cells.ChatMessageCell.BotButton) r3;
        if (r3 == 0) goto L_0x2fab;
        r120 = r4;
        r4 = r3.progressAlpha;
        r14.progressAlpha = r4;
        r4 = r3.angle;
        r14.angle = r4;
        r121 = r7;
        r122 = r8;
        r7 = r3.lastUpdateTime;
        r14.lastUpdateTime = r7;
        goto L_0x2fb8;
        r120 = r4;
        r121 = r7;
        r122 = r8;
        r7 = java.lang.System.currentTimeMillis();
        r14.lastUpdateTime = r7;
        r4 = r1.botButtonsByData;
        r4.put(r15, r14);
        r4 = r1.botButtonsByPosition;
        r4.put(r2, r14);
        r4 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 + r12;
        r4 = r4 * r9;
        r14.x = r4;
        r4 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r4 = r4 * r6;
        r7 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r4 = r4 + r7;
        r14.y = r4;
        r14.width = r12;
        r4 = 1110441984; // 0x42300000 float:44.0 double:5.48631236E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r14.height = r4;
        r4 = r14.button;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_keyboardButtonBuy;
        if (r4 == 0) goto L_0x3008;
        r4 = r5.messageOwner;
        r4 = r4.media;
        r4 = r4.flags;
        r4 = r4 & 4;
        if (r4 == 0) goto L_0x3008;
        r4 = "PaymentReceipt";
        r7 = 2131494115; // 0x7f0c04e3 float:1.861173E38 double:1.0530980165E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r7);
        r123 = r2;
        goto L_0x3032;
        r4 = r14.button;
        r4 = r4.text;
        r7 = org.telegram.ui.ActionBar.Theme.chat_botButtonPaint;
        r7 = r7.getFontMetricsInt();
        r8 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r123 = r2;
        r2 = 0;
        r4 = org.telegram.messenger.Emoji.replaceEmoji(r4, r7, r8, r2);
        r2 = org.telegram.ui.ActionBar.Theme.chat_botButtonPaint;
        r7 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r8 = r12 - r8;
        r8 = (float) r8;
        r7 = android.text.TextUtils.TruncateAt.END;
        r4 = android.text.TextUtils.ellipsize(r4, r2, r8, r7);
        r2 = new android.text.StaticLayout;
        r26 = org.telegram.ui.ActionBar.Theme.chat_botButtonPaint;
        r7 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r27 = r12 - r8;
        r28 = android.text.Layout.Alignment.ALIGN_CENTER;
        r29 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r30 = 0;
        r31 = 0;
        r24 = r2;
        r25 = r4;
        r24.<init>(r25, r26, r27, r28, r29, r30, r31);
        r14.title = r2;
        r2 = r1.botButtons;
        r2.add(r14);
        r2 = r10.buttons;
        r2 = r2.size();
        r8 = 1;
        r2 = r2 - r8;
        if (r9 != r2) goto L_0x306e;
        r2 = r14.x;
        r16 = r14.width;
        r2 = r2 + r16;
        r2 = java.lang.Math.max(r13, r2);
        r13 = r2;
        r9 = r9 + 1;
        r2 = r118;
        r3 = r119;
        r4 = r120;
        r7 = r121;
        r8 = r122;
        goto L_0x2f43;
        r118 = r2;
        r119 = r3;
        r120 = r4;
        r121 = r7;
        r122 = r8;
        r7 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r8 = 1;
        r9 = r13;
        r6 = r6 + 1;
        r2 = r118;
        r3 = r119;
        r4 = r120;
        r7 = r121;
        r8 = r122;
        goto L_0x2ef8;
        r118 = r2;
        r119 = r3;
        r120 = r4;
        r121 = r7;
        r122 = r8;
        r1.widthForButtons = r9;
        goto L_0x30ac;
        r118 = r2;
        r2 = 0;
        r1.substractBackgroundHeight = r2;
        r1.keyboardHeight = r2;
        r2 = r1.drawPinnedBottom;
        if (r2 == 0) goto L_0x30c0;
        r2 = r1.drawPinnedTop;
        if (r2 == 0) goto L_0x30c0;
        r2 = r1.totalHeight;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r1.totalHeight = r2;
        goto L_0x30ed;
        r2 = r1.drawPinnedBottom;
        if (r2 == 0) goto L_0x30d0;
        r2 = r1.totalHeight;
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r1.totalHeight = r2;
        goto L_0x30ed;
        r2 = r1.drawPinnedTop;
        if (r2 == 0) goto L_0x30ed;
        r2 = r1.pinnedBottom;
        if (r2 == 0) goto L_0x30ed;
        r2 = r1.currentPosition;
        if (r2 == 0) goto L_0x30ed;
        r2 = r1.currentPosition;
        r2 = r2.siblingHeights;
        if (r2 != 0) goto L_0x30ed;
        r2 = r1.totalHeight;
        r3 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r1.totalHeight = r2;
        r2 = r5.type;
        r3 = 13;
        if (r2 != r3) goto L_0x3105;
        r2 = r1.totalHeight;
        r3 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        if (r2 >= r3) goto L_0x3105;
        r2 = 1116471296; // 0x428c0000 float:70.0 double:5.51610112E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.totalHeight = r2;
    L_0x3105:
        r125.updateWaveform();
        r2 = r35;
        r1.updateButtonState(r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatMessageCell.setMessageObject(org.telegram.messenger.MessageObject, org.telegram.messenger.MessageObject$GroupedMessages, boolean, boolean):void");
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
        if (this.pressedLink != null) {
            if (this.pressedLinkType == type || type == -1) {
                resetUrlPaths(false);
                this.pressedLink = null;
                this.pressedLinkType = -1;
                invalidate();
            }
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean checkTextBlockMotionEvent(android.view.MotionEvent r26) {
        /*
        r25 = this;
        r1 = r25;
        r2 = r1.currentMessageObject;
        r2 = r2.type;
        r3 = 0;
        if (r2 != 0) goto L_0x0280;
    L_0x0009:
        r2 = r1.currentMessageObject;
        r2 = r2.textLayoutBlocks;
        if (r2 == 0) goto L_0x0280;
    L_0x000f:
        r2 = r1.currentMessageObject;
        r2 = r2.textLayoutBlocks;
        r2 = r2.isEmpty();
        if (r2 != 0) goto L_0x0280;
    L_0x0019:
        r2 = r1.currentMessageObject;
        r2 = r2.messageText;
        r2 = r2 instanceof android.text.Spannable;
        if (r2 != 0) goto L_0x0023;
    L_0x0021:
        goto L_0x0280;
    L_0x0023:
        r2 = r26.getAction();
        r4 = 1;
        if (r2 == 0) goto L_0x0034;
    L_0x002a:
        r2 = r26.getAction();
        if (r2 != r4) goto L_0x027e;
    L_0x0030:
        r2 = r1.pressedLinkType;
        if (r2 != r4) goto L_0x027e;
    L_0x0034:
        r2 = r26.getX();
        r2 = (int) r2;
        r5 = r26.getY();
        r5 = (int) r5;
        r6 = r1.textX;
        if (r2 < r6) goto L_0x027a;
    L_0x0042:
        r6 = r1.textY;
        if (r5 < r6) goto L_0x027a;
    L_0x0046:
        r6 = r1.textX;
        r7 = r1.currentMessageObject;
        r7 = r7.textWidth;
        r6 = r6 + r7;
        if (r2 > r6) goto L_0x027a;
    L_0x004f:
        r6 = r1.textY;
        r7 = r1.currentMessageObject;
        r7 = r7.textHeight;
        r6 = r6 + r7;
        if (r5 > r6) goto L_0x027a;
    L_0x0058:
        r6 = r1.textY;
        r5 = r5 - r6;
        r6 = 0;
        r7 = r6;
        r6 = r3;
    L_0x005e:
        r8 = r1.currentMessageObject;
        r8 = r8.textLayoutBlocks;
        r8 = r8.size();
        if (r6 >= r8) goto L_0x007e;
    L_0x0068:
        r8 = r1.currentMessageObject;
        r8 = r8.textLayoutBlocks;
        r8 = r8.get(r6);
        r8 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r8;
        r8 = r8.textYOffset;
        r9 = (float) r5;
        r8 = (r8 > r9 ? 1 : (r8 == r9 ? 0 : -1));
        if (r8 <= 0) goto L_0x007a;
    L_0x0079:
        goto L_0x007e;
    L_0x007a:
        r7 = r6;
        r6 = r6 + 1;
        goto L_0x005e;
    L_0x007e:
        r6 = r1.currentMessageObject;	 Catch:{ Exception -> 0x0270 }
        r6 = r6.textLayoutBlocks;	 Catch:{ Exception -> 0x0270 }
        r6 = r6.get(r7);	 Catch:{ Exception -> 0x0270 }
        r6 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r6;	 Catch:{ Exception -> 0x0270 }
        r8 = (float) r2;	 Catch:{ Exception -> 0x0270 }
        r9 = r1.textX;	 Catch:{ Exception -> 0x0270 }
        r9 = (float) r9;	 Catch:{ Exception -> 0x0270 }
        r10 = r6.isRtl();	 Catch:{ Exception -> 0x0270 }
        if (r10 == 0) goto L_0x0097;
    L_0x0092:
        r10 = r1.currentMessageObject;	 Catch:{ Exception -> 0x0270 }
        r10 = r10.textXOffset;	 Catch:{ Exception -> 0x0270 }
        goto L_0x0098;
    L_0x0097:
        r10 = 0;
    L_0x0098:
        r9 = r9 - r10;
        r8 = r8 - r9;
        r2 = (int) r8;
        r8 = (float) r5;
        r9 = r6.textYOffset;	 Catch:{ Exception -> 0x026b }
        r8 = r8 - r9;
        r5 = (int) r8;
        r8 = r6.textLayout;	 Catch:{ Exception -> 0x0264 }
        r8 = r8.getLineForVertical(r5);	 Catch:{ Exception -> 0x0264 }
        r9 = r6.textLayout;	 Catch:{ Exception -> 0x0264 }
        r10 = (float) r2;	 Catch:{ Exception -> 0x0264 }
        r9 = r9.getOffsetForHorizontal(r8, r10);	 Catch:{ Exception -> 0x0264 }
        r10 = r6.textLayout;	 Catch:{ Exception -> 0x0264 }
        r10 = r10.getLineLeft(r8);	 Catch:{ Exception -> 0x0264 }
        r12 = (float) r2;	 Catch:{ Exception -> 0x0264 }
        r12 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1));
        if (r12 > 0) goto L_0x025f;
    L_0x00b8:
        r12 = r6.textLayout;	 Catch:{ Exception -> 0x0264 }
        r12 = r12.getLineWidth(r8);	 Catch:{ Exception -> 0x0264 }
        r12 = r12 + r10;
        r13 = (float) r2;	 Catch:{ Exception -> 0x0264 }
        r12 = (r12 > r13 ? 1 : (r12 == r13 ? 0 : -1));
        if (r12 < 0) goto L_0x025f;
    L_0x00c4:
        r12 = r1.currentMessageObject;	 Catch:{ Exception -> 0x0264 }
        r12 = r12.messageText;	 Catch:{ Exception -> 0x0264 }
        r12 = (android.text.Spannable) r12;	 Catch:{ Exception -> 0x0264 }
        r13 = android.text.style.ClickableSpan.class;
        r13 = r12.getSpans(r9, r9, r13);	 Catch:{ Exception -> 0x0264 }
        r13 = (android.text.style.CharacterStyle[]) r13;	 Catch:{ Exception -> 0x0264 }
        r14 = 0;
        if (r13 == 0) goto L_0x00d8;
    L_0x00d5:
        r15 = r13.length;	 Catch:{ Exception -> 0x0270 }
        if (r15 != 0) goto L_0x00e2;
    L_0x00d8:
        r15 = org.telegram.ui.Components.URLSpanMono.class;
        r15 = r12.getSpans(r9, r9, r15);	 Catch:{ Exception -> 0x0264 }
        r15 = (android.text.style.CharacterStyle[]) r15;	 Catch:{ Exception -> 0x0264 }
        r13 = r15;
        r14 = 1;
    L_0x00e2:
        r15 = 0;
        r11 = r13.length;	 Catch:{ Exception -> 0x0264 }
        if (r11 == 0) goto L_0x00f3;
    L_0x00e6:
        r11 = r13.length;	 Catch:{ Exception -> 0x0270 }
        if (r11 == 0) goto L_0x00f4;
    L_0x00e9:
        r11 = r13[r3];	 Catch:{ Exception -> 0x0270 }
        r11 = r11 instanceof org.telegram.ui.Components.URLSpanBotCommand;	 Catch:{ Exception -> 0x0270 }
        if (r11 == 0) goto L_0x00f4;
    L_0x00ef:
        r11 = org.telegram.ui.Components.URLSpanBotCommand.enabled;	 Catch:{ Exception -> 0x0270 }
        if (r11 != 0) goto L_0x00f4;
    L_0x00f3:
        r15 = 1;
    L_0x00f4:
        if (r15 != 0) goto L_0x025f;
    L_0x00f6:
        r11 = r26.getAction();	 Catch:{ Exception -> 0x0264 }
        if (r11 != 0) goto L_0x023a;
    L_0x00fc:
        r11 = r13[r3];	 Catch:{ Exception -> 0x0264 }
        r1.pressedLink = r11;	 Catch:{ Exception -> 0x0264 }
        r1.linkBlockNum = r7;	 Catch:{ Exception -> 0x0264 }
        r1.pressedLinkType = r4;	 Catch:{ Exception -> 0x0264 }
        r1.resetUrlPaths(r3);	 Catch:{ Exception -> 0x0264 }
        r11 = r1.obtainNewUrlPath(r3);	 Catch:{ Exception -> 0x0226 }
        r4 = r1.pressedLink;	 Catch:{ Exception -> 0x0226 }
        r4 = r12.getSpanStart(r4);	 Catch:{ Exception -> 0x0226 }
        r3 = r1.pressedLink;	 Catch:{ Exception -> 0x0226 }
        r3 = r12.getSpanEnd(r3);	 Catch:{ Exception -> 0x0226 }
        r17 = r2;
        r2 = r6.textLayout;	 Catch:{ Exception -> 0x021b }
        r18 = r5;
        r5 = 0;
        r11.setCurrentLayout(r2, r4, r5);	 Catch:{ Exception -> 0x0212 }
        r2 = r6.textLayout;	 Catch:{ Exception -> 0x0212 }
        r2.getSelectionPath(r4, r3, r11);	 Catch:{ Exception -> 0x0212 }
        r2 = r6.charactersEnd;	 Catch:{ Exception -> 0x0212 }
        if (r3 < r2) goto L_0x01a2;
    L_0x012a:
        r2 = r7 + 1;
    L_0x012c:
        r5 = r1.currentMessageObject;	 Catch:{ Exception -> 0x0212 }
        r5 = r5.textLayoutBlocks;	 Catch:{ Exception -> 0x0212 }
        r5 = r5.size();	 Catch:{ Exception -> 0x0212 }
        if (r2 >= r5) goto L_0x01a2;
    L_0x0136:
        r5 = r1.currentMessageObject;	 Catch:{ Exception -> 0x0212 }
        r5 = r5.textLayoutBlocks;	 Catch:{ Exception -> 0x0212 }
        r5 = r5.get(r2);	 Catch:{ Exception -> 0x0212 }
        r5 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r5;	 Catch:{ Exception -> 0x0212 }
        r19 = r8;
        r8 = r5.charactersOffset;	 Catch:{ Exception -> 0x019a }
        r20 = r9;
        r9 = r5.charactersOffset;	 Catch:{ Exception -> 0x0194 }
        if (r14 == 0) goto L_0x0153;
    L_0x014a:
        r16 = org.telegram.ui.Components.URLSpanMono.class;
        goto L_0x0155;
    L_0x014d:
        r0 = move-exception;
        r2 = r0;
        r21 = r10;
        goto L_0x0232;
    L_0x0153:
        r16 = android.text.style.ClickableSpan.class;
    L_0x0155:
        r21 = r10;
        r10 = r16;
        r8 = r12.getSpans(r8, r9, r10);	 Catch:{ Exception -> 0x020f }
        r8 = (android.text.style.CharacterStyle[]) r8;	 Catch:{ Exception -> 0x020f }
        if (r8 == 0) goto L_0x01a8;
    L_0x0161:
        r9 = r8.length;	 Catch:{ Exception -> 0x020f }
        if (r9 == 0) goto L_0x01a8;
    L_0x0164:
        r9 = 0;
        r10 = r8[r9];	 Catch:{ Exception -> 0x020f }
        r9 = r1.pressedLink;	 Catch:{ Exception -> 0x020f }
        if (r10 == r9) goto L_0x016c;
    L_0x016b:
        goto L_0x01a8;
    L_0x016c:
        r9 = 0;
        r10 = r1.obtainNewUrlPath(r9);	 Catch:{ Exception -> 0x020f }
        r11 = r10;
        r9 = r5.textLayout;	 Catch:{ Exception -> 0x020f }
        r10 = r5.textYOffset;	 Catch:{ Exception -> 0x020f }
        r22 = r8;
        r8 = r6.textYOffset;	 Catch:{ Exception -> 0x020f }
        r10 = r10 - r8;
        r8 = 0;
        r11.setCurrentLayout(r9, r8, r10);	 Catch:{ Exception -> 0x020f }
        r9 = r5.textLayout;	 Catch:{ Exception -> 0x020f }
        r9.getSelectionPath(r8, r3, r11);	 Catch:{ Exception -> 0x020f }
        r8 = r5.charactersEnd;	 Catch:{ Exception -> 0x020f }
        r9 = 1;
        r8 = r8 - r9;
        if (r3 >= r8) goto L_0x018b;
    L_0x018a:
        goto L_0x01a8;
    L_0x018b:
        r2 = r2 + 1;
        r8 = r19;
        r9 = r20;
        r10 = r21;
        goto L_0x012c;
    L_0x0194:
        r0 = move-exception;
        r21 = r10;
        r2 = r0;
        goto L_0x0232;
    L_0x019a:
        r0 = move-exception;
        r20 = r9;
        r21 = r10;
        r2 = r0;
        goto L_0x0232;
    L_0x01a2:
        r19 = r8;
        r20 = r9;
        r21 = r10;
    L_0x01a8:
        r2 = r6.charactersOffset;	 Catch:{ Exception -> 0x020f }
        if (r4 > r2) goto L_0x020e;
    L_0x01ac:
        r2 = 0;
        r5 = r7 + -1;
    L_0x01af:
        if (r5 < 0) goto L_0x020e;
    L_0x01b1:
        r8 = r1.currentMessageObject;	 Catch:{ Exception -> 0x020f }
        r8 = r8.textLayoutBlocks;	 Catch:{ Exception -> 0x020f }
        r8 = r8.get(r5);	 Catch:{ Exception -> 0x020f }
        r8 = (org.telegram.messenger.MessageObject.TextLayoutBlock) r8;	 Catch:{ Exception -> 0x020f }
        r9 = r8.charactersEnd;	 Catch:{ Exception -> 0x020f }
        r10 = 1;
        r9 = r9 - r10;
        r10 = r8.charactersEnd;	 Catch:{ Exception -> 0x020f }
        r16 = 1;
        r10 = r10 + -1;
        if (r14 == 0) goto L_0x01ca;
    L_0x01c7:
        r16 = org.telegram.ui.Components.URLSpanMono.class;
        goto L_0x01cc;
    L_0x01ca:
        r16 = android.text.style.ClickableSpan.class;
    L_0x01cc:
        r23 = r3;
        r3 = r16;
        r3 = r12.getSpans(r9, r10, r3);	 Catch:{ Exception -> 0x020f }
        r3 = (android.text.style.CharacterStyle[]) r3;	 Catch:{ Exception -> 0x020f }
        if (r3 == 0) goto L_0x020e;
    L_0x01d8:
        r9 = r3.length;	 Catch:{ Exception -> 0x020f }
        if (r9 == 0) goto L_0x020e;
    L_0x01db:
        r9 = 0;
        r10 = r3[r9];	 Catch:{ Exception -> 0x020f }
        r9 = r1.pressedLink;	 Catch:{ Exception -> 0x020f }
        if (r10 == r9) goto L_0x01e3;
    L_0x01e2:
        goto L_0x020e;
    L_0x01e3:
        r9 = 0;
        r10 = r1.obtainNewUrlPath(r9);	 Catch:{ Exception -> 0x020f }
        r11 = r10;
        r9 = r1.pressedLink;	 Catch:{ Exception -> 0x020f }
        r9 = r12.getSpanStart(r9);	 Catch:{ Exception -> 0x020f }
        r4 = r9;
        r9 = r8.height;	 Catch:{ Exception -> 0x020f }
        r2 = r2 - r9;
        r9 = r8.textLayout;	 Catch:{ Exception -> 0x020f }
        r10 = (float) r2;	 Catch:{ Exception -> 0x020f }
        r11.setCurrentLayout(r9, r4, r10);	 Catch:{ Exception -> 0x020f }
        r9 = r8.textLayout;	 Catch:{ Exception -> 0x020f }
        r10 = r1.pressedLink;	 Catch:{ Exception -> 0x020f }
        r10 = r12.getSpanEnd(r10);	 Catch:{ Exception -> 0x020f }
        r9.getSelectionPath(r4, r10, r11);	 Catch:{ Exception -> 0x020f }
        r9 = r8.charactersOffset;	 Catch:{ Exception -> 0x020f }
        if (r4 <= r9) goto L_0x0209;
    L_0x0208:
        goto L_0x020e;
    L_0x0209:
        r5 = r5 + -1;
        r3 = r23;
        goto L_0x01af;
    L_0x020e:
        goto L_0x0235;
    L_0x020f:
        r0 = move-exception;
        r2 = r0;
        goto L_0x0232;
    L_0x0212:
        r0 = move-exception;
        r19 = r8;
        r20 = r9;
        r21 = r10;
        r2 = r0;
        goto L_0x0232;
    L_0x021b:
        r0 = move-exception;
        r18 = r5;
        r19 = r8;
        r20 = r9;
        r21 = r10;
        r2 = r0;
        goto L_0x0232;
    L_0x0226:
        r0 = move-exception;
        r17 = r2;
        r18 = r5;
        r19 = r8;
        r20 = r9;
        r21 = r10;
        r2 = r0;
    L_0x0232:
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Exception -> 0x025a }
    L_0x0235:
        r25.invalidate();	 Catch:{ Exception -> 0x025a }
        r2 = 1;
        return r2;
    L_0x023a:
        r17 = r2;
        r18 = r5;
        r19 = r8;
        r20 = r9;
        r21 = r10;
        r2 = 0;
        r3 = r13[r2];	 Catch:{ Exception -> 0x025a }
        r2 = r1.pressedLink;	 Catch:{ Exception -> 0x025a }
        if (r3 != r2) goto L_0x0263;
    L_0x024b:
        r2 = r1.delegate;	 Catch:{ Exception -> 0x025a }
        r3 = r1.currentMessageObject;	 Catch:{ Exception -> 0x025a }
        r4 = r1.pressedLink;	 Catch:{ Exception -> 0x025a }
        r5 = 0;
        r2.didPressedUrl(r3, r4, r5);	 Catch:{ Exception -> 0x025a }
        r2 = 1;
        r1.resetPressedLink(r2);	 Catch:{ Exception -> 0x025a }
        return r2;
    L_0x025a:
        r0 = move-exception;
        r2 = r0;
        r5 = r18;
        goto L_0x0274;
    L_0x025f:
        r17 = r2;
        r18 = r5;
    L_0x0263:
        goto L_0x0279;
    L_0x0264:
        r0 = move-exception;
        r17 = r2;
        r18 = r5;
        r2 = r0;
        goto L_0x0274;
    L_0x026b:
        r0 = move-exception;
        r17 = r2;
        r2 = r0;
        goto L_0x0274;
    L_0x0270:
        r0 = move-exception;
        r17 = r2;
        r2 = r0;
    L_0x0274:
        org.telegram.messenger.FileLog.e(r2);
        r18 = r5;
    L_0x0279:
        goto L_0x027e;
    L_0x027a:
        r3 = 1;
        r1.resetPressedLink(r3);
    L_0x027e:
        r2 = 0;
        return r2;
    L_0x0280:
        r2 = r3;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatMessageCell.checkTextBlockMotionEvent(android.view.MotionEvent):boolean");
    }

    private boolean checkCaptionMotionEvent(MotionEvent event) {
        if (this.currentCaption instanceof Spannable) {
            if (this.captionLayout != null) {
                if (event.getAction() == 0 || ((this.linkPreviewPressed || this.pressedLink != null) && event.getAction() == 1)) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    if (x < this.captionX || x > this.captionX + this.captionWidth || y < this.captionY || y > this.captionY + this.captionHeight) {
                        resetPressedLink(3);
                    } else if (event.getAction() == 0) {
                        try {
                            x -= this.captionX;
                            int line = this.captionLayout.getLineForVertical(y - this.captionY);
                            int off = this.captionLayout.getOffsetForHorizontal(line, (float) x);
                            float left = this.captionLayout.getLineLeft(line);
                            if (left <= ((float) x) && this.captionLayout.getLineWidth(line) + left >= ((float) x)) {
                                Spannable buffer = this.currentCaption;
                                CharacterStyle[] link = (CharacterStyle[]) buffer.getSpans(off, off, ClickableSpan.class);
                                if (link == null || link.length == 0) {
                                    link = (CharacterStyle[]) buffer.getSpans(off, off, URLSpanMono.class);
                                }
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
                                    if (!(this.currentMessagesGroup == null || getParent() == null)) {
                                        ((ViewGroup) getParent()).invalidate();
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
        }
        return false;
    }

    private boolean checkGameMotionEvent(MotionEvent event) {
        if (!this.hasGamePreview) {
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        int line;
        if (event.getAction() == 0) {
            if (this.drawPhotoImage && this.photoImage.isInsideImage((float) x, (float) y)) {
                this.gamePreviewPressed = true;
                return true;
            } else if (this.descriptionLayout != null && y >= this.descriptionY) {
                try {
                    x -= (this.unmovedTextX + AndroidUtilities.dp(10.0f)) + this.descriptionX;
                    line = this.descriptionLayout.getLineForVertical(y - this.descriptionY);
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
            if (this.pressedLinkType != 2) {
                if (!this.gamePreviewPressed) {
                    resetPressedLink(2);
                }
            }
            if (this.pressedLink != null) {
                if (this.pressedLink instanceof URLSpan) {
                    Browser.openUrl(getContext(), ((URLSpan) this.pressedLink).getURL());
                } else if (this.pressedLink instanceof ClickableSpan) {
                    ((ClickableSpan) this.pressedLink).onClick(this);
                }
                resetPressedLink(2);
            } else {
                this.gamePreviewPressed = false;
                for (line = 0; line < this.botButtons.size(); line++) {
                    BotButton button = (BotButton) this.botButtons.get(line);
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
        if (this.currentMessageObject.type == 0) {
            if (r1.hasLinkPreview) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (x >= r1.unmovedTextX && x <= r1.unmovedTextX + r1.backgroundWidth && y >= r1.textY + r1.currentMessageObject.textHeight) {
                    if (y <= ((r1.textY + r1.currentMessageObject.textHeight) + r1.linkPreviewHeight) + AndroidUtilities.dp((float) (8 + (r1.drawInstantView ? 46 : 0)))) {
                        WebPage webPage;
                        if (event.getAction() == 0) {
                            int checkX;
                            if (r1.descriptionLayout != null && y >= r1.descriptionY) {
                                try {
                                    checkX = x - ((r1.unmovedTextX + AndroidUtilities.dp(10.0f)) + r1.descriptionX);
                                    int checkY = y - r1.descriptionY;
                                    if (checkY <= r1.descriptionLayout.getHeight()) {
                                        int line = r1.descriptionLayout.getLineForVertical(checkY);
                                        int off = r1.descriptionLayout.getOffsetForHorizontal(line, (float) checkX);
                                        float left = r1.descriptionLayout.getLineLeft(line);
                                        if (left <= ((float) checkX) && r1.descriptionLayout.getLineWidth(line) + left >= ((float) checkX)) {
                                            Spannable buffer = r1.currentMessageObject.linkDescription;
                                            ClickableSpan[] link = (ClickableSpan[]) buffer.getSpans(off, off, ClickableSpan.class);
                                            boolean ignore = false;
                                            if (link.length == 0 || !(link.length == 0 || !(link[0] instanceof URLSpanBotCommand) || URLSpanBotCommand.enabled)) {
                                                ignore = true;
                                            }
                                            if (!ignore) {
                                                r1.pressedLink = link[0];
                                                r1.linkBlockNum = -10;
                                                r1.pressedLinkType = 2;
                                                resetUrlPaths(false);
                                                try {
                                                    LinkPath path = obtainNewUrlPath(false);
                                                    int start = buffer.getSpanStart(r1.pressedLink);
                                                    path.setCurrentLayout(r1.descriptionLayout, start, 0.0f);
                                                    r1.descriptionLayout.getSelectionPath(start, buffer.getSpanEnd(r1.pressedLink), path);
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
                            if (r1.pressedLink == null) {
                                checkX = AndroidUtilities.dp(48.0f);
                                boolean area2 = false;
                                if (r1.miniButtonState >= 0) {
                                    int offset = AndroidUtilities.dp(1104674816);
                                    boolean z = x >= r1.buttonX + offset && x <= (r1.buttonX + offset) + checkX && y >= r1.buttonY + offset && y <= (r1.buttonY + offset) + checkX;
                                    area2 = z;
                                }
                                if (area2) {
                                    r1.miniButtonPressed = 1;
                                    invalidate();
                                    return true;
                                } else if (r1.drawPhotoImage && r1.drawImageButton && r1.buttonState != -1 && x >= r1.buttonX && x <= r1.buttonX + AndroidUtilities.dp(48.0f) && y >= r1.buttonY && y <= r1.buttonY + AndroidUtilities.dp(48.0f)) {
                                    r1.buttonPressed = 1;
                                    return true;
                                } else if (r1.drawInstantView) {
                                    r1.instantPressed = true;
                                    if (VERSION.SDK_INT >= 21 && r1.instantViewSelectorDrawable != null && r1.instantViewSelectorDrawable.getBounds().contains(x, y)) {
                                        r1.instantViewSelectorDrawable.setState(r1.pressedState);
                                        r1.instantViewSelectorDrawable.setHotspot((float) x, (float) y);
                                        r1.instantButtonPressed = true;
                                    }
                                    invalidate();
                                    return true;
                                } else if (r1.documentAttachType != 1 && r1.drawPhotoImage && r1.photoImage.isInsideImage((float) x, (float) y)) {
                                    r1.linkPreviewPressed = true;
                                    webPage = r1.currentMessageObject.messageOwner.media.webpage;
                                    if (r1.documentAttachType != 2 || r1.buttonState != -1 || !SharedConfig.autoplayGifs || (r1.photoImage.getAnimation() != null && TextUtils.isEmpty(webPage.embed_url))) {
                                        return true;
                                    }
                                    r1.linkPreviewPressed = false;
                                    return false;
                                }
                            }
                        } else if (event.getAction() == 1) {
                            if (r1.instantPressed) {
                                if (r1.delegate != null) {
                                    r1.delegate.didPressedInstantButton(r1, r1.drawInstantViewType);
                                }
                                playSoundEffect(0);
                                if (VERSION.SDK_INT >= 21 && r1.instantViewSelectorDrawable != null) {
                                    r1.instantViewSelectorDrawable.setState(StateSet.NOTHING);
                                }
                                r1.instantButtonPressed = false;
                                r1.instantPressed = false;
                                invalidate();
                            } else {
                                if (r1.pressedLinkType != 2 && r1.buttonPressed == 0 && r1.miniButtonPressed == 0) {
                                    if (!r1.linkPreviewPressed) {
                                        resetPressedLink(2);
                                    }
                                }
                                if (r1.buttonPressed != 0) {
                                    r1.buttonPressed = 0;
                                    playSoundEffect(0);
                                    didPressedButton(false);
                                    invalidate();
                                } else if (r1.miniButtonPressed != 0) {
                                    r1.miniButtonPressed = 0;
                                    playSoundEffect(0);
                                    didPressedMiniButton(false);
                                    invalidate();
                                } else if (r1.pressedLink != null) {
                                    if (r1.pressedLink instanceof URLSpan) {
                                        Browser.openUrl(getContext(), ((URLSpan) r1.pressedLink).getURL());
                                    } else if (r1.pressedLink instanceof ClickableSpan) {
                                        ((ClickableSpan) r1.pressedLink).onClick(r1);
                                    }
                                    resetPressedLink(2);
                                } else {
                                    if (r1.documentAttachType == 7) {
                                        if (MediaController.getInstance().isPlayingMessage(r1.currentMessageObject)) {
                                            if (!MediaController.getInstance().isMessagePaused()) {
                                                MediaController.getInstance().pauseMessage(r1.currentMessageObject);
                                            }
                                        }
                                        r1.delegate.needPlayMessage(r1.currentMessageObject);
                                    } else if (r1.documentAttachType != 2 || !r1.drawImageButton) {
                                        webPage = r1.currentMessageObject.messageOwner.media.webpage;
                                        if (webPage == null || TextUtils.isEmpty(webPage.embed_url)) {
                                            if (r1.buttonState != -1) {
                                                if (r1.buttonState != 3) {
                                                    if (webPage != null) {
                                                        Browser.openUrl(getContext(), webPage.url);
                                                    }
                                                }
                                            }
                                            r1.delegate.didPressedImage(r1);
                                            playSoundEffect(0);
                                        } else {
                                            r1.delegate.needOpenWebView(webPage.embed_url, webPage.site_name, webPage.title, webPage.url, webPage.embed_width, webPage.embed_height);
                                        }
                                    } else if (r1.buttonState == -1) {
                                        if (SharedConfig.autoplayGifs) {
                                            r1.delegate.didPressedImage(r1);
                                        } else {
                                            r1.buttonState = 2;
                                            r1.currentMessageObject.gifState = 1.0f;
                                            r1.photoImage.setAllowStartAnimation(false);
                                            r1.photoImage.stopAnimation();
                                            r1.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                                            invalidate();
                                            playSoundEffect(0);
                                        }
                                    } else if (r1.buttonState == 2 || r1.buttonState == 0) {
                                        didPressedButton(false);
                                        playSoundEffect(0);
                                    }
                                    resetPressedLink(2);
                                    return true;
                                }
                            }
                        } else if (event.getAction() == 2 && r1.instantButtonPressed && VERSION.SDK_INT >= 21 && r1.instantViewSelectorDrawable != null) {
                            r1.instantViewSelectorDrawable.setHotspot((float) x, (float) y);
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean checkOtherButtonMotionEvent(MotionEvent event) {
        boolean allow = this.currentMessageObject.type == 16;
        if (!allow) {
            boolean z = ((this.documentAttachType != 1 && this.currentMessageObject.type != 12 && this.documentAttachType != 5 && this.documentAttachType != 4 && this.documentAttachType != 2 && this.currentMessageObject.type != 8) || this.hasGamePreview || this.hasInvoicePreview) ? false : true;
            allow = z;
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
            boolean area2 = false;
            int side = AndroidUtilities.dp(1111490560);
            if (this.miniButtonState >= 0) {
                int offset = AndroidUtilities.dp(1104674816);
                boolean z = x >= this.buttonX + offset && x <= (this.buttonX + offset) + side && y >= this.buttonY + offset && y <= (this.buttonY + offset) + side;
                area2 = z;
            }
            if (area2) {
                this.miniButtonPressed = 1;
                invalidate();
                result = true;
            } else if (this.buttonState != -1 && x >= this.buttonX && x <= this.buttonX + side && y >= this.buttonY && y <= this.buttonY + side) {
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
            if (this.imagePressed) {
                boolean z2;
                if (this.currentMessageObject.isSendError()) {
                    this.imagePressed = false;
                    z2 = false;
                } else if (this.currentMessageObject.type == 8 && this.buttonState == -1 && SharedConfig.autoplayGifs && this.photoImage.getAnimation() == null) {
                    this.imagePressed = false;
                    z2 = false;
                } else if (this.currentMessageObject.type == 5 && this.buttonState != -1) {
                    this.imagePressed = false;
                    z2 = false;
                }
                result = z2;
            }
        } else if (event.getAction() == 1) {
            if (this.buttonPressed == 1) {
                this.buttonPressed = 0;
                playSoundEffect(0);
                didPressedButton(false);
                updateRadialProgressBackground();
                invalidate();
            } else if (this.miniButtonPressed == 1) {
                this.miniButtonPressed = 0;
                playSoundEffect(0);
                didPressedMiniButton(false);
                invalidate();
            } else if (this.imagePressed) {
                this.imagePressed = false;
                if (!(this.buttonState == -1 || this.buttonState == 2)) {
                    if (this.buttonState != 3) {
                        if (this.buttonState == 0 && this.documentAttachType == 1) {
                            playSoundEffect(0);
                            didPressedButton(false);
                        }
                        invalidate();
                    }
                }
                playSoundEffect(0);
                didClickedImage();
                invalidate();
            }
        }
        return result;
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
        } else {
            boolean z;
            int side = AndroidUtilities.dp(1108344832);
            boolean area = false;
            boolean area2 = false;
            if (this.miniButtonState >= 0) {
                int offset = AndroidUtilities.dp(1104674816);
                z = x >= this.buttonX + offset && x <= (this.buttonX + offset) + side && y >= this.buttonY + offset && y <= (this.buttonY + offset) + side;
                area2 = z;
            }
            if (!area2) {
                if (!(this.buttonState == 0 || this.buttonState == 1)) {
                    if (this.buttonState != 2) {
                        z = x >= this.buttonX && x <= this.buttonX + side && y >= this.buttonY && y <= this.buttonY + side;
                        area = z;
                    }
                }
                if (x >= this.buttonX - AndroidUtilities.dp(12.0f) && x <= (this.buttonX - AndroidUtilities.dp(12.0f)) + this.backgroundWidth) {
                    if (y >= (this.drawInstantView ? this.buttonY : this.namesOffset + this.mediaOffsetY)) {
                        if (y <= (this.drawInstantView ? this.buttonY + side : (this.namesOffset + this.mediaOffsetY) + AndroidUtilities.dp(82.0f))) {
                            z = true;
                            area = z;
                        }
                    }
                }
                z = false;
                area = z;
            }
            if (event.getAction() == 0) {
                if (area || area2) {
                    if (area) {
                        this.buttonPressed = 1;
                    } else {
                        this.miniButtonPressed = 1;
                    }
                    invalidate();
                    result = true;
                    updateRadialProgressBackground();
                }
            } else if (this.buttonPressed != 0) {
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
                updateRadialProgressBackground();
            } else if (this.miniButtonPressed != 0) {
                if (event.getAction() == 1) {
                    this.miniButtonPressed = 0;
                    playSoundEffect(0);
                    didPressedMiniButton(true);
                    invalidate();
                } else if (event.getAction() == 3) {
                    this.miniButtonPressed = 0;
                    invalidate();
                } else if (event.getAction() == 2 && !area2) {
                    this.miniButtonPressed = 0;
                    invalidate();
                }
                updateRadialProgressBackground();
            }
        }
        return result;
    }

    public void updatePlayingMessageProgress() {
        if (this.currentMessageObject != null) {
            int duration;
            String timeString;
            if (this.currentMessageObject.isRoundVideo()) {
                duration = 0;
                Document document = this.currentMessageObject.getDocument();
                for (int a = 0; a < document.attributes.size(); a++) {
                    DocumentAttribute attribute = (DocumentAttribute) document.attributes.get(a);
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
                    this.seekBar.setBufferedProgress(this.currentMessageObject.bufferedProgress);
                }
                duration = 0;
                int a2;
                if (this.documentAttachType == 3) {
                    if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                        duration = this.currentMessageObject.audioProgressSec;
                    } else {
                        for (a2 = 0; a2 < this.documentAttach.attributes.size(); a2++) {
                            DocumentAttribute attribute2 = (DocumentAttribute) this.documentAttach.attributes.get(a2);
                            if (attribute2 instanceof TL_documentAttributeAudio) {
                                duration = attribute2.duration;
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
                    a2 = 0;
                    duration = this.currentMessageObject.getDuration();
                    if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                        a2 = this.currentMessageObject.audioProgressSec;
                    }
                    if (this.lastTime != a2) {
                        this.lastTime = a2;
                        if (duration == 0) {
                            timeString = String.format("%d:%02d / -:--", new Object[]{Integer.valueOf(a2 / 60), Integer.valueOf(a2 % 60)});
                        } else {
                            timeString = String.format("%d:%02d / %d:%02d", new Object[]{Integer.valueOf(a2 / 60), Integer.valueOf(a2 % 60), Integer.valueOf(duration / 60), Integer.valueOf(duration % 60)});
                        }
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
        if (this.currentMessageObject != null) {
            if (this.currentMessageObject.textLayoutBlocks != null) {
                int a;
                position -= this.textY;
                int startBlock = 0;
                for (a = 0; a < this.currentMessageObject.textLayoutBlocks.size(); a++) {
                    if (((TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(a)).textYOffset > ((float) position)) {
                        break;
                    }
                    startBlock = a;
                }
                a = 0;
                int newLast = -1;
                int newFirst = -1;
                for (int a2 = startBlock; a2 < this.currentMessageObject.textLayoutBlocks.size(); a2++) {
                    TextLayoutBlock block = (TextLayoutBlock) this.currentMessageObject.textLayoutBlocks.get(a2);
                    float y = block.textYOffset;
                    if (intersect(y, ((float) block.height) + y, (float) position, (float) (position + height))) {
                        if (newFirst == -1) {
                            newFirst = a2;
                        }
                        newLast = a2;
                        a++;
                    } else if (y > ((float) position)) {
                        break;
                    }
                }
                if (!(this.lastVisibleBlockNum == newLast && this.firstVisibleBlockNum == newFirst && this.totalVisibleBlocksCount == a)) {
                    this.lastVisibleBlockNum = newLast;
                    this.firstVisibleBlockNum = newFirst;
                    this.totalVisibleBlocksCount = a;
                    invalidate();
                }
            }
        }
    }

    private boolean intersect(float left1, float right1, float left2, float right2) {
        boolean z = false;
        if (left1 <= left2) {
            if (right1 >= left2) {
                z = true;
            }
            return z;
        }
        if (left1 <= right2) {
            z = true;
        }
        return z;
    }

    public static StaticLayout generateStaticLayout(CharSequence text, TextPaint paint, int maxWidth, int smallWidth, int linesCount, int maxLines) {
        CharSequence charSequence = text;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(charSequence);
        int addedChars = 0;
        StaticLayout layout = new StaticLayout(charSequence, paint, smallWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        int a = 0;
        int maxWidth2 = maxWidth;
        while (a < linesCount) {
            Directions directions = layout.getLineDirections(a);
            if (layout.getLineLeft(a) != 0.0f || layout.isRtlCharAt(layout.getLineStart(a)) || layout.isRtlCharAt(layout.getLineEnd(a))) {
                maxWidth2 = smallWidth;
            }
            int pos = layout.getLineEnd(a);
            if (pos == text.length()) {
                break;
            }
            pos--;
            if (stringBuilder.charAt(pos + addedChars) == ' ') {
                stringBuilder.replace(pos + addedChars, (pos + addedChars) + 1, "\n");
            } else if (stringBuilder.charAt(pos + addedChars) != '\n') {
                stringBuilder.insert(pos + addedChars, "\n");
                addedChars++;
            }
            if (a == layout.getLineCount() - 1) {
                break;
            } else if (a == maxLines - 1) {
                break;
            } else {
                a++;
            }
        }
        return StaticLayoutEx.createStaticLayout(stringBuilder, paint, maxWidth2, Alignment.ALIGN_NORMAL, 1.0f, (float) AndroidUtilities.dp(1.0f), false, TruncateAt.END, maxWidth2, maxLines);
    }

    private void didClickedImage() {
        if (this.currentMessageObject.type != 1) {
            if (this.currentMessageObject.type != 13) {
                if (this.currentMessageObject.type == 12) {
                    this.delegate.didPressedUserAvatar(this, MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.currentMessageObject.messageOwner.media.user_id)));
                    return;
                } else if (this.currentMessageObject.type == 5) {
                    if (MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                        if (!MediaController.getInstance().isMessagePaused()) {
                            MediaController.getInstance().pauseMessage(this.currentMessageObject);
                            return;
                        }
                    }
                    this.delegate.needPlayMessage(this.currentMessageObject);
                    return;
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
                        return;
                    } else if (this.buttonState == 2 || this.buttonState == 0) {
                        didPressedButton(false);
                        return;
                    } else {
                        return;
                    }
                } else if (this.documentAttachType == 4) {
                    if (this.buttonState == -1) {
                        this.delegate.didPressedImage(this);
                        return;
                    } else if (this.buttonState == 0 || this.buttonState == 3) {
                        didPressedButton(false);
                        return;
                    } else {
                        return;
                    }
                } else if (this.currentMessageObject.type == 4) {
                    this.delegate.didPressedImage(this);
                    return;
                } else if (this.documentAttachType == 1) {
                    if (this.buttonState == -1) {
                        this.delegate.didPressedImage(this);
                        return;
                    }
                    return;
                } else if (this.documentAttachType == 2) {
                    if (this.buttonState == -1) {
                        WebPage webPage = this.currentMessageObject.messageOwner.media.webpage;
                        if (webPage != null) {
                            if (webPage.embed_url == null || webPage.embed_url.length() == 0) {
                                Browser.openUrl(getContext(), webPage.url);
                            } else {
                                this.delegate.needOpenWebView(webPage.embed_url, webPage.site_name, webPage.description, webPage.url, webPage.embed_width, webPage.embed_height);
                            }
                        }
                        return;
                    }
                    return;
                } else if (this.hasInvoicePreview && this.buttonState == -1) {
                    this.delegate.didPressedImage(this);
                    return;
                } else {
                    return;
                }
            }
        }
        if (this.buttonState == -1) {
            this.delegate.didPressedImage(this);
        } else if (this.buttonState == 0) {
            didPressedButton(false);
        }
    }

    private void updateSecretTimeText(MessageObject messageObject) {
        if (messageObject != null) {
            if (messageObject.needDrawBluredPreview()) {
                String str = messageObject.getSecretTimeString();
                if (str != null) {
                    this.infoWidth = (int) Math.ceil((double) Theme.chat_infoPaint.measureText(str));
                    this.infoLayout = new StaticLayout(TextUtils.ellipsize(str, Theme.chat_infoPaint, (float) this.infoWidth, TruncateAt.END), Theme.chat_infoPaint, this.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                    invalidate();
                }
            }
        }
    }

    private boolean isPhotoDataChanged(MessageObject object) {
        ChatMessageCell chatMessageCell = this;
        MessageObject messageObject = object;
        if (messageObject.type != 0) {
            if (messageObject.type != 14) {
                if (messageObject.type != 4) {
                    if (chatMessageCell.currentPhotoObject != null) {
                        if (!(chatMessageCell.currentPhotoObject.location instanceof TL_fileLocationUnavailable)) {
                            if (chatMessageCell.currentMessageObject != null && chatMessageCell.photoNotSet && FileLoader.getPathToMessage(chatMessageCell.currentMessageObject.messageOwner).exists()) {
                                return true;
                            }
                        }
                    }
                    if (!(messageObject.type == 1 || messageObject.type == 5 || messageObject.type == 3 || messageObject.type == 8)) {
                        if (messageObject.type == 13) {
                        }
                    }
                    return true;
                } else if (chatMessageCell.currentUrl == null) {
                    return true;
                } else {
                    String url;
                    double lat = messageObject.messageOwner.media.geo.lat;
                    double lon = messageObject.messageOwner.media.geo._long;
                    if (messageObject.messageOwner.media instanceof TL_messageMediaGeoLive) {
                        int photoWidth = chatMessageCell.backgroundWidth - AndroidUtilities.dp(21.0f);
                        int photoHeight = AndroidUtilities.dp(1128464384);
                        double rad = ((double) 268435456) / 3.141592653589793d;
                        double y = (double) (Math.round(((double) 268435456) - ((Math.log((1.0d + Math.sin((lat * 3.141592653589793d) / 180.0d)) / (1.0d - Math.sin((lat * 3.141592653589793d) / 180.0d))) * rad) / 2.0d)) - ((long) (AndroidUtilities.dp(10.3f) << 6)));
                        lat = ((1.5707963267948966d - (Math.atan(Math.exp((y - ((double) 268435456)) / rad)) * 2.0d)) * 180.0d) / 3.141592653589793d;
                        r7 = new Object[5];
                        r7[2] = Integer.valueOf((int) (((float) photoWidth) / AndroidUtilities.density));
                        r7[3] = Integer.valueOf((int) (((float) photoHeight) / AndroidUtilities.density));
                        r7[4] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
                        url = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=%dx%d&maptype=roadmap&scale=%d&sensor=false", r7);
                    } else {
                        double lat2 = lat;
                        if (TextUtils.isEmpty(messageObject.messageOwner.media.title)) {
                            url = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=200x100&maptype=roadmap&scale=%d&markers=color:red|size:mid|%f,%f&sensor=false", new Object[]{Double.valueOf(lat), Double.valueOf(lon), Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density))), Double.valueOf(lat), Double.valueOf(lon)});
                        } else {
                            r4 = new Object[5];
                            lat = lat2;
                            r4[0] = Double.valueOf(lat);
                            r4[1] = Double.valueOf(lon);
                            r4[2] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
                            r4[3] = Double.valueOf(lat);
                            r4[4] = Double.valueOf(lon);
                            url = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:mid|%f,%f&sensor=false", r4);
                        }
                    }
                    if (!url.equals(chatMessageCell.currentUrl)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private boolean isUserDataChanged() {
        boolean z = true;
        if (this.currentMessageObject != null && !this.hasLinkPreview && this.currentMessageObject.messageOwner.media != null && (this.currentMessageObject.messageOwner.media.webpage instanceof TL_webPage)) {
            return true;
        }
        if (this.currentMessageObject != null) {
            if (this.currentUser != null || this.currentChat != null) {
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
                if ((this.currentPhoto == null && newPhoto != null) || ((this.currentPhoto != null && newPhoto == null) || (this.currentPhoto != null && newPhoto != null && (this.currentPhoto.local_id != newPhoto.local_id || this.currentPhoto.volume_id != newPhoto.volume_id)))) {
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
                if ((this.currentNameString == null && newNameString != null) || ((this.currentNameString != null && newNameString == null) || (this.currentNameString != null && newNameString != null && !this.currentNameString.equals(newNameString)))) {
                    return true;
                }
                if (!this.drawForwardedName) {
                    return false;
                }
                newNameString = this.currentMessageObject.getForwardedName();
                if ((this.currentForwardNameString != null || newNameString == null) && ((this.currentForwardNameString == null || newNameString != null) && (this.currentForwardNameString == null || newNameString == null || this.currentForwardNameString.equals(newNameString)))) {
                    z = false;
                }
                return z;
            }
        }
        return false;
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
        setTranslationX(0.0f);
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
        if (!(this.buttonPressed == 0 && this.miniButtonPressed == 0 && this.pressedBotButton == -1)) {
            this.buttonPressed = 0;
            this.miniButtonState = 0;
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
        updateRadialProgressBackground();
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
            if (this.isHighlighted) {
                this.isHighlightedAnimated = false;
                this.highlightProgress = 0;
            } else {
                this.lastHighlightProgressTime = System.currentTimeMillis();
                this.isHighlightedAnimated = true;
                this.highlightProgress = 300;
            }
            updateRadialProgressBackground();
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
        updateRadialProgressBackground();
        if (this.useSeekBarWaweform) {
            this.seekBarWaveform.setSelected(isDrawSelectedBackground());
        } else {
            this.seekBar.setSelected(isDrawSelectedBackground());
        }
        invalidate();
    }

    private void updateRadialProgressBackground() {
        if (!this.drawRadialCheckBackground) {
            this.radialProgress.swapBackground(getDrawableForCurrentState());
            if (this.hasMiniProgress != 0) {
                this.radialProgress.swapMiniBackground(getMiniDrawableForCurrentState());
            }
        }
    }

    public void onSeekBarDrag(float progress) {
        if (this.currentMessageObject != null) {
            this.currentMessageObject.audioProgress = progress;
            MediaController.getInstance().seekToProgress(this.currentMessageObject, progress);
        }
    }

    private void updateWaveform() {
        if (this.currentMessageObject != null) {
            if (this.documentAttachType == 3) {
                boolean z = false;
                for (int a = 0; a < this.documentAttach.attributes.size(); a++) {
                    DocumentAttribute attribute = (DocumentAttribute) this.documentAttach.attributes.get(a);
                    if (attribute instanceof TL_documentAttributeAudio) {
                        if (attribute.waveform == null || attribute.waveform.length == 0) {
                            MediaController.getInstance().generateWaveform(this.currentMessageObject);
                        }
                        if (attribute.waveform != null) {
                            z = true;
                        }
                        this.useSeekBarWaweform = z;
                        this.seekBarWaveform.setWaveform(attribute.waveform);
                    }
                }
            }
        }
    }

    private int createDocumentLayout(int maxWidth, MessageObject messageObject) {
        ChatMessageCell chatMessageCell = this;
        int maxWidth2 = maxWidth;
        MessageObject messageObject2 = messageObject;
        if (messageObject2.type == 0) {
            chatMessageCell.documentAttach = messageObject2.messageOwner.media.webpage.document;
        } else {
            chatMessageCell.documentAttach = messageObject2.messageOwner.media.document;
        }
        int a = 0;
        if (chatMessageCell.documentAttach == null) {
            return 0;
        }
        int duration;
        int a2;
        if (MessageObject.isVoiceDocument(chatMessageCell.documentAttach)) {
            chatMessageCell.documentAttachType = 3;
            duration = 0;
            for (a2 = 0; a2 < chatMessageCell.documentAttach.attributes.size(); a2++) {
                DocumentAttribute attribute = (DocumentAttribute) chatMessageCell.documentAttach.attributes.get(a2);
                if (attribute instanceof TL_documentAttributeAudio) {
                    duration = attribute.duration;
                    break;
                }
            }
            chatMessageCell.widthBeforeNewTimeLine = (maxWidth2 - AndroidUtilities.dp(94.0f)) - ((int) Math.ceil((double) Theme.chat_audioTimePaint.measureText("00:00")));
            chatMessageCell.availableTimeWidth = maxWidth2 - AndroidUtilities.dp(18.0f);
            measureTime(messageObject2);
            a2 = AndroidUtilities.dp(174.0f) + chatMessageCell.timeWidth;
            if (!chatMessageCell.hasLinkPreview) {
                chatMessageCell.backgroundWidth = Math.min(maxWidth2, (AndroidUtilities.dp(10.0f) * duration) + a2);
            }
            chatMessageCell.seekBarWaveform.setMessageObject(messageObject2);
            return 0;
        } else if (MessageObject.isMusicDocument(chatMessageCell.documentAttach)) {
            chatMessageCell.documentAttachType = 5;
            maxWidth2 -= AndroidUtilities.dp(86.0f);
            StaticLayout staticLayout = r11;
            char c = '\n';
            char c2 = ' ';
            StaticLayout staticLayout2 = new StaticLayout(TextUtils.ellipsize(messageObject.getMusicTitle().replace('\n', ' '), Theme.chat_audioTitlePaint, (float) (maxWidth2 - AndroidUtilities.dp(12.0f)), TruncateAt.END), Theme.chat_audioTitlePaint, maxWidth2, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            chatMessageCell.songLayout = staticLayout;
            if (chatMessageCell.songLayout.getLineCount() > 0) {
                chatMessageCell.songX = -((int) Math.ceil((double) chatMessageCell.songLayout.getLineLeft(0)));
            }
            chatMessageCell.performerLayout = new StaticLayout(TextUtils.ellipsize(messageObject.getMusicAuthor().replace(c, c2), Theme.chat_audioPerformerPaint, (float) maxWidth2, TruncateAt.END), Theme.chat_audioPerformerPaint, maxWidth2, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            if (chatMessageCell.performerLayout.getLineCount() > 0) {
                chatMessageCell.performerX = -((int) Math.ceil((double) chatMessageCell.performerLayout.getLineLeft(0)));
            }
            duration = 0;
            for (a2 = 0; a2 < chatMessageCell.documentAttach.attributes.size(); a2++) {
                DocumentAttribute attribute2 = (DocumentAttribute) chatMessageCell.documentAttach.attributes.get(a2);
                if (attribute2 instanceof TL_documentAttributeAudio) {
                    duration = attribute2.duration;
                    break;
                }
            }
            a = (int) Math.ceil((double) Theme.chat_audioTimePaint.measureText(String.format("%d:%02d / %d:%02d", new Object[]{Integer.valueOf(duration / 60), Integer.valueOf(duration % 60), Integer.valueOf(duration / 60), Integer.valueOf(duration % 60)})));
            chatMessageCell.widthBeforeNewTimeLine = (chatMessageCell.backgroundWidth - AndroidUtilities.dp(86.0f)) - a;
            chatMessageCell.availableTimeWidth = chatMessageCell.backgroundWidth - AndroidUtilities.dp(28.0f);
            return a;
        } else if (MessageObject.isVideoDocument(chatMessageCell.documentAttach)) {
            chatMessageCell.documentAttachType = 4;
            if (!messageObject.needDrawBluredPreview()) {
                duration = 0;
                for (duration = 0; duration < chatMessageCell.documentAttach.attributes.size(); duration++) {
                    DocumentAttribute attribute3 = (DocumentAttribute) chatMessageCell.documentAttach.attributes.get(duration);
                    if (attribute3 instanceof TL_documentAttributeVideo) {
                        duration = attribute3.duration;
                        break;
                    }
                }
                a2 = duration - ((duration / 60) * 60);
                str = String.format("%d:%02d, %s", new Object[]{Integer.valueOf(duration / 60), Integer.valueOf(a2), AndroidUtilities.formatFileSize((long) chatMessageCell.documentAttach.size)});
                chatMessageCell.infoWidth = (int) Math.ceil((double) Theme.chat_infoPaint.measureText(str));
                chatMessageCell.infoLayout = new StaticLayout(str, Theme.chat_infoPaint, chatMessageCell.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            return 0;
        } else {
            boolean z = (chatMessageCell.documentAttach.mime_type != null && chatMessageCell.documentAttach.mime_type.toLowerCase().startsWith("image/")) || !(chatMessageCell.documentAttach.thumb == null || (chatMessageCell.documentAttach.thumb instanceof TL_photoSizeEmpty) || (chatMessageCell.documentAttach.thumb.location instanceof TL_fileLocationUnavailable));
            chatMessageCell.drawPhotoImage = z;
            if (!chatMessageCell.drawPhotoImage) {
                maxWidth2 += AndroidUtilities.dp(30.0f);
            }
            chatMessageCell.documentAttachType = 1;
            String name = FileLoader.getDocumentFileName(chatMessageCell.documentAttach);
            if (name == null || name.length() == 0) {
                name = LocaleController.getString("AttachDocument", R.string.AttachDocument);
            }
            chatMessageCell.docTitleLayout = StaticLayoutEx.createStaticLayout(name, Theme.chat_docNamePaint, maxWidth2, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false, TruncateAt.MIDDLE, maxWidth2, chatMessageCell.drawPhotoImage ? 2 : 1);
            chatMessageCell.docTitleOffsetX = Integer.MIN_VALUE;
            int width;
            if (chatMessageCell.docTitleLayout == null || chatMessageCell.docTitleLayout.getLineCount() <= 0) {
                width = maxWidth2;
                chatMessageCell.docTitleOffsetX = 0;
                a = width;
            } else {
                width = 0;
                while (a < chatMessageCell.docTitleLayout.getLineCount()) {
                    width = Math.max(width, (int) Math.ceil((double) chatMessageCell.docTitleLayout.getLineWidth(a)));
                    chatMessageCell.docTitleOffsetX = Math.max(chatMessageCell.docTitleOffsetX, (int) Math.ceil((double) (-chatMessageCell.docTitleLayout.getLineLeft(a))));
                    a++;
                }
                a = Math.min(maxWidth2, width);
            }
            str = new StringBuilder();
            str.append(AndroidUtilities.formatFileSize((long) chatMessageCell.documentAttach.size));
            str.append(" ");
            str.append(FileLoader.getDocumentExtension(chatMessageCell.documentAttach));
            str = str.toString();
            chatMessageCell.infoWidth = Math.min(maxWidth2 - AndroidUtilities.dp(30.0f), (int) Math.ceil((double) Theme.chat_infoPaint.measureText(str)));
            CharSequence str2 = TextUtils.ellipsize(str, Theme.chat_infoPaint, (float) chatMessageCell.infoWidth, TruncateAt.END);
            try {
                if (chatMessageCell.infoWidth < 0) {
                    chatMessageCell.infoWidth = AndroidUtilities.dp(10.0f);
                }
                chatMessageCell.infoLayout = new StaticLayout(str2, Theme.chat_infoPaint, chatMessageCell.infoWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            } catch (Throwable e) {
                FileLog.e(e);
            }
            if (chatMessageCell.drawPhotoImage) {
                chatMessageCell.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(messageObject2.photoThumbs, AndroidUtilities.getPhotoSize());
                chatMessageCell.photoImage.setNeedsQualityThumb(true);
                chatMessageCell.photoImage.setShouldGenerateQualityThumb(true);
                chatMessageCell.photoImage.setParentMessageObject(messageObject2);
                if (chatMessageCell.currentPhotoObject != null) {
                    chatMessageCell.currentPhotoFilter = "86_86_b";
                    chatMessageCell.photoImage.setImage(null, null, null, null, chatMessageCell.currentPhotoObject.location, chatMessageCell.currentPhotoFilter, 0, null, 1);
                } else {
                    chatMessageCell.photoImage.setImageBitmap((BitmapDrawable) null);
                }
            }
            return a;
        }
    }

    private void calcBackgroundWidth(int maxWidth, int timeMore, int maxChildWidth) {
        if (!(this.hasLinkPreview || this.hasOldCaptionPreview || this.hasGamePreview || this.hasInvoicePreview || maxWidth - this.currentMessageObject.lastLineWidth < timeMore)) {
            if (!this.currentMessageObject.hasRtl) {
                int diff = maxChildWidth - this.currentMessageObject.lastLineWidth;
                if (diff < 0 || diff > timeMore) {
                    this.backgroundWidth = Math.max(maxChildWidth, this.currentMessageObject.lastLineWidth + timeMore) + AndroidUtilities.dp(31.0f);
                    return;
                } else {
                    this.backgroundWidth = ((maxChildWidth + timeMore) - diff) + AndroidUtilities.dp(31.0f);
                    return;
                }
            }
        }
        this.totalHeight += AndroidUtilities.dp(14.0f);
        this.hasNewLineForTime = true;
        this.backgroundWidth = Math.max(maxChildWidth, this.currentMessageObject.lastLineWidth) + AndroidUtilities.dp(31.0f);
        this.backgroundWidth = Math.max(this.backgroundWidth, (this.currentMessageObject.isOutOwner() ? this.timeWidth + AndroidUtilities.dp(17.0f) : this.timeWidth) + AndroidUtilities.dp(31.0f));
    }

    public void setHighlightedText(String text) {
        if (!(this.currentMessageObject.messageOwner.message == null || this.currentMessageObject == null || this.currentMessageObject.type != 0 || TextUtils.isEmpty(this.currentMessageObject.messageText))) {
            if (text != null) {
                int start = TextUtils.indexOf(this.currentMessageObject.messageOwner.message.toLowerCase(), text.toLowerCase());
                if (start == -1) {
                    if (!this.urlPathSelection.isEmpty()) {
                        this.linkSelectionBlockNum = -1;
                        resetUrlPaths(true);
                        invalidate();
                    }
                    return;
                }
                int end = text.length() + start;
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
                return;
            }
        }
        if (!this.urlPathSelection.isEmpty()) {
            this.linkSelectionBlockNum = -1;
            resetUrlPaths(true);
            invalidate();
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        if (!super.verifyDrawable(who)) {
            if (who != this.instantViewSelectorDrawable) {
                return false;
            }
        }
        return true;
    }

    private boolean isCurrentLocationTimeExpired(MessageObject messageObject) {
        boolean z = false;
        if (this.currentMessageObject.messageOwner.media.period % 60 == 0) {
            if (Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - messageObject.messageOwner.date) > messageObject.messageOwner.media.period) {
                z = true;
            }
            return z;
        }
        if (Math.abs(ConnectionsManager.getInstance(this.currentAccount).getCurrentTime() - messageObject.messageOwner.date) > messageObject.messageOwner.media.period - 5) {
            z = true;
        }
        return z;
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
                } else {
                    AndroidUtilities.runOnUIThread(this.invalidateRunnable, 1000);
                    this.scheduledInvalidate = true;
                    this.docTitleLayout = new StaticLayout(LocaleController.getString("AttachLiveLocation", R.string.AttachLiveLocation), Theme.chat_locationTitlePaint, this.backgroundWidth - AndroidUtilities.dp(91.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                }
            }
        }
    }

    private int getAdditionalWidthForPosition(GroupedMessagePosition position) {
        int w = 0;
        if (position == null) {
            return 0;
        }
        if ((position.flags & 2) == 0) {
            w = 0 + AndroidUtilities.dp(4.0f);
        }
        if ((position.flags & 1) == 0) {
            return w + AndroidUtilities.dp(4.0f);
        }
        return w;
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
            if (changed || !r0.wasLayout) {
                r0.layoutWidth = getMeasuredWidth();
                r0.layoutHeight = getMeasuredHeight() - r0.substractBackgroundHeight;
                if (r0.timeTextWidth < 0) {
                    r0.timeTextWidth = AndroidUtilities.dp(10.0f);
                }
                r0.timeLayout = new StaticLayout(r0.currentTimeString, Theme.chat_timePaint, AndroidUtilities.dp(100.0f) + r0.timeTextWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                if (r0.mediaBackground) {
                    if (r0.currentMessageObject.isOutOwner()) {
                        r0.timeX = (r0.layoutWidth - r0.timeWidth) - AndroidUtilities.dp(42.0f);
                    } else {
                        r0.timeX = ((r0.backgroundWidth - AndroidUtilities.dp(4.0f)) - r0.timeWidth) + (r0.isAvatarVisible ? AndroidUtilities.dp(48.0f) : 0);
                        if (!(r0.currentPosition == null || r0.currentPosition.leftSpanOffset == 0)) {
                            r0.timeX += (int) Math.ceil((double) ((((float) r0.currentPosition.leftSpanOffset) / 1000.0f) * ((float) getGroupPhotosWidth())));
                        }
                    }
                } else if (r0.currentMessageObject.isOutOwner()) {
                    r0.timeX = (r0.layoutWidth - r0.timeWidth) - AndroidUtilities.dp(38.5f);
                } else {
                    r0.timeX = ((r0.backgroundWidth - AndroidUtilities.dp(9.0f)) - r0.timeWidth) + (r0.isAvatarVisible ? AndroidUtilities.dp(48.0f) : 0);
                }
                if ((r0.currentMessageObject.messageOwner.flags & 1024) != 0) {
                    r0.viewsLayout = new StaticLayout(r0.currentViewsString, Theme.chat_timePaint, r0.viewsTextWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                } else {
                    r0.viewsLayout = null;
                }
                if (r0.isAvatarVisible) {
                    r0.avatarImage.setImageCoords(AndroidUtilities.dp(6.0f), r0.avatarImage.getImageY(), AndroidUtilities.dp(42.0f), AndroidUtilities.dp(42.0f));
                }
                r0.wasLayout = true;
            }
            if (r0.currentMessageObject.type == 0) {
                r0.textY = AndroidUtilities.dp(10.0f) + r0.namesOffset;
            }
            if (r0.currentMessageObject.isRoundVideo()) {
                updatePlayingMessageProgress();
            }
            int i = 10;
            SeekBar seekBar;
            int i2;
            if (r0.documentAttachType == 3) {
                if (r0.currentMessageObject.isOutOwner()) {
                    r0.seekBarX = (r0.layoutWidth - r0.backgroundWidth) + AndroidUtilities.dp(57.0f);
                    r0.buttonX = (r0.layoutWidth - r0.backgroundWidth) + AndroidUtilities.dp(14.0f);
                    r0.timeAudioX = (r0.layoutWidth - r0.backgroundWidth) + AndroidUtilities.dp(67.0f);
                } else if (r0.isChat && r0.currentMessageObject.needDrawAvatar()) {
                    r0.seekBarX = AndroidUtilities.dp(114.0f);
                    r0.buttonX = AndroidUtilities.dp(71.0f);
                    r0.timeAudioX = AndroidUtilities.dp(124.0f);
                } else {
                    r0.seekBarX = AndroidUtilities.dp(66.0f);
                    r0.buttonX = AndroidUtilities.dp(23.0f);
                    r0.timeAudioX = AndroidUtilities.dp(76.0f);
                }
                if (r0.hasLinkPreview) {
                    r0.seekBarX += AndroidUtilities.dp(10.0f);
                    r0.buttonX += AndroidUtilities.dp(10.0f);
                    r0.timeAudioX += AndroidUtilities.dp(10.0f);
                }
                r0.seekBarWaveform.setSize(r0.backgroundWidth - AndroidUtilities.dp((float) (92 + (r0.hasLinkPreview ? 10 : 0))), AndroidUtilities.dp(30.0f));
                seekBar = r0.seekBar;
                i2 = r0.backgroundWidth;
                if (!r0.hasLinkPreview) {
                    i = 0;
                }
                seekBar.setSize(i2 - AndroidUtilities.dp((float) (72 + i)), AndroidUtilities.dp(30.0f));
                r0.seekBarY = (AndroidUtilities.dp(13.0f) + r0.namesOffset) + r0.mediaOffsetY;
                r0.buttonY = (AndroidUtilities.dp(13.0f) + r0.namesOffset) + r0.mediaOffsetY;
                r0.radialProgress.setProgressRect(r0.buttonX, r0.buttonY, r0.buttonX + AndroidUtilities.dp(44.0f), r0.buttonY + AndroidUtilities.dp(44.0f));
                updatePlayingMessageProgress();
            } else if (r0.documentAttachType == 5) {
                if (r0.currentMessageObject.isOutOwner()) {
                    r0.seekBarX = (r0.layoutWidth - r0.backgroundWidth) + AndroidUtilities.dp(56.0f);
                    r0.buttonX = (r0.layoutWidth - r0.backgroundWidth) + AndroidUtilities.dp(14.0f);
                    r0.timeAudioX = (r0.layoutWidth - r0.backgroundWidth) + AndroidUtilities.dp(67.0f);
                } else if (r0.isChat && r0.currentMessageObject.needDrawAvatar()) {
                    r0.seekBarX = AndroidUtilities.dp(113.0f);
                    r0.buttonX = AndroidUtilities.dp(71.0f);
                    r0.timeAudioX = AndroidUtilities.dp(124.0f);
                } else {
                    r0.seekBarX = AndroidUtilities.dp(65.0f);
                    r0.buttonX = AndroidUtilities.dp(23.0f);
                    r0.timeAudioX = AndroidUtilities.dp(76.0f);
                }
                if (r0.hasLinkPreview) {
                    r0.seekBarX += AndroidUtilities.dp(10.0f);
                    r0.buttonX += AndroidUtilities.dp(10.0f);
                    r0.timeAudioX += AndroidUtilities.dp(10.0f);
                }
                seekBar = r0.seekBar;
                i2 = r0.backgroundWidth;
                if (!r0.hasLinkPreview) {
                    i = 0;
                }
                seekBar.setSize(i2 - AndroidUtilities.dp((float) (65 + i)), AndroidUtilities.dp(30.0f));
                r0.seekBarY = (AndroidUtilities.dp(29.0f) + r0.namesOffset) + r0.mediaOffsetY;
                r0.buttonY = (AndroidUtilities.dp(13.0f) + r0.namesOffset) + r0.mediaOffsetY;
                r0.radialProgress.setProgressRect(r0.buttonX, r0.buttonY, r0.buttonX + AndroidUtilities.dp(44.0f), r0.buttonY + AndroidUtilities.dp(44.0f));
                updatePlayingMessageProgress();
            } else if (r0.documentAttachType == 1 && !r0.drawPhotoImage) {
                if (r0.currentMessageObject.isOutOwner()) {
                    r0.buttonX = (r0.layoutWidth - r0.backgroundWidth) + AndroidUtilities.dp(14.0f);
                } else if (r0.isChat && r0.currentMessageObject.needDrawAvatar()) {
                    r0.buttonX = AndroidUtilities.dp(71.0f);
                } else {
                    r0.buttonX = AndroidUtilities.dp(23.0f);
                }
                if (r0.hasLinkPreview) {
                    r0.buttonX += AndroidUtilities.dp(10.0f);
                }
                r0.buttonY = (AndroidUtilities.dp(13.0f) + r0.namesOffset) + r0.mediaOffsetY;
                r0.radialProgress.setProgressRect(r0.buttonX, r0.buttonY, r0.buttonX + AndroidUtilities.dp(44.0f), r0.buttonY + AndroidUtilities.dp(44.0f));
                r0.photoImage.setImageCoords(r0.buttonX - AndroidUtilities.dp(10.0f), r0.buttonY - AndroidUtilities.dp(10.0f), r0.photoImage.getImageWidth(), r0.photoImage.getImageHeight());
            } else if (r0.currentMessageObject.type == 12) {
                int x;
                if (r0.currentMessageObject.isOutOwner()) {
                    x = (r0.layoutWidth - r0.backgroundWidth) + AndroidUtilities.dp(14.0f);
                } else if (r0.isChat && r0.currentMessageObject.needDrawAvatar()) {
                    x = AndroidUtilities.dp(72.0f);
                } else {
                    x = AndroidUtilities.dp(23.0f);
                    r0.photoImage.setImageCoords(x, AndroidUtilities.dp(13.0f) + r0.namesOffset, AndroidUtilities.dp(44.0f), AndroidUtilities.dp(44.0f));
                }
                r0.photoImage.setImageCoords(x, AndroidUtilities.dp(13.0f) + r0.namesOffset, AndroidUtilities.dp(44.0f), AndroidUtilities.dp(44.0f));
            } else {
                int linkX;
                if (r0.currentMessageObject.type == 0 && (r0.hasLinkPreview || r0.hasGamePreview || r0.hasInvoicePreview)) {
                    int x2;
                    if (r0.hasGamePreview) {
                        linkX = r0.unmovedTextX - AndroidUtilities.dp(10.0f);
                    } else if (r0.hasInvoicePreview) {
                        linkX = r0.unmovedTextX + AndroidUtilities.dp(1.0f);
                    } else {
                        linkX = r0.unmovedTextX + AndroidUtilities.dp(1.0f);
                        if (r0.isSmallImage) {
                            x2 = (r0.hasInvoicePreview ? -AndroidUtilities.dp(6.3f) : AndroidUtilities.dp(10.0f)) + linkX;
                        } else {
                            x2 = (r0.backgroundWidth + linkX) - AndroidUtilities.dp(81.0f);
                        }
                        linkX = x2;
                    }
                    if (r0.isSmallImage) {
                        if (r0.hasInvoicePreview) {
                        }
                        x2 = (r0.hasInvoicePreview ? -AndroidUtilities.dp(6.3f) : AndroidUtilities.dp(10.0f)) + linkX;
                    } else {
                        x2 = (r0.backgroundWidth + linkX) - AndroidUtilities.dp(81.0f);
                    }
                    linkX = x2;
                } else if (!r0.currentMessageObject.isOutOwner()) {
                    if (r0.isChat && r0.isAvatarVisible) {
                        linkX = AndroidUtilities.dp(63.0f);
                    } else {
                        linkX = AndroidUtilities.dp(15.0f);
                    }
                    if (!(r0.currentPosition == null || r0.currentPosition.edge)) {
                        linkX -= AndroidUtilities.dp(10.0f);
                    }
                } else if (r0.mediaBackground) {
                    linkX = (r0.layoutWidth - r0.backgroundWidth) - AndroidUtilities.dp(3.0f);
                } else {
                    linkX = (r0.layoutWidth - r0.backgroundWidth) + AndroidUtilities.dp(6.0f);
                }
                if (r0.currentPosition != null) {
                    if ((1 & r0.currentPosition.flags) == 0) {
                        linkX -= AndroidUtilities.dp(4.0f);
                    }
                    if (r0.currentPosition.leftSpanOffset != 0) {
                        linkX += (int) Math.ceil((double) ((((float) r0.currentPosition.leftSpanOffset) / 1000.0f) * ((float) getGroupPhotosWidth())));
                    }
                }
                r0.photoImage.setImageCoords(linkX, r0.photoImage.getImageY(), r0.photoImage.getImageWidth(), r0.photoImage.getImageHeight());
                r0.buttonX = (int) (((float) linkX) + (((float) (r0.photoImage.getImageWidth() - AndroidUtilities.dp(48.0f))) / 2.0f));
                r0.buttonY = r0.photoImage.getImageY() + ((r0.photoImage.getImageHeight() - AndroidUtilities.dp(48.0f)) / 2);
                r0.radialProgress.setProgressRect(r0.buttonX, r0.buttonY, r0.buttonX + AndroidUtilities.dp(48.0f), r0.buttonY + AndroidUtilities.dp(48.0f));
                r0.deleteProgressRect.set((float) (r0.buttonX + AndroidUtilities.dp(3.0f)), (float) (r0.buttonY + AndroidUtilities.dp(3.0f)), (float) (r0.buttonX + AndroidUtilities.dp(45.0f)), (float) (r0.buttonY + AndroidUtilities.dp(45.0f)));
            }
        }
    }

    public boolean needDelayRoundProgressDraw() {
        return this.documentAttachType == 7 && this.currentMessageObject.type != 5 && MediaController.getInstance().isPlayingMessage(this.currentMessageObject);
    }

    public void drawRoundProgress(Canvas canvas) {
        this.rect.set(((float) this.photoImage.getImageX()) + AndroidUtilities.dpf2(1.5f), ((float) this.photoImage.getImageY()) + AndroidUtilities.dpf2(1.5f), ((float) this.photoImage.getImageX2()) - AndroidUtilities.dpf2(1.5f), ((float) this.photoImage.getImageY2()) - AndroidUtilities.dpf2(1.5f));
        canvas.drawArc(this.rect, -90.0f, 360.0f * this.currentMessageObject.audioProgress, false, Theme.chat_radialProgressPaint);
    }

    private Drawable getMiniDrawableForCurrentState() {
        if (this.miniButtonState < 0) {
            return null;
        }
        CombinedDrawable[] combinedDrawableArr;
        int i = 1;
        if (this.documentAttachType != 3) {
            if (this.documentAttachType != 5) {
                if (this.documentAttachType != 4) {
                    return null;
                }
                combinedDrawableArr = Theme.chat_fileMiniStatesDrawable[4 + this.miniButtonState];
                if (this.miniButtonPressed == 0) {
                    i = 0;
                }
                return combinedDrawableArr[i];
            }
        }
        this.radialProgress.setAlphaForPrevious(false);
        combinedDrawableArr = Theme.chat_fileMiniStatesDrawable[this.currentMessageObject.isOutOwner() ? this.miniButtonState : this.miniButtonState + 2];
        if (!isDrawSelectedBackground()) {
            if (this.miniButtonPressed == 0) {
                i = 0;
            }
        }
        return combinedDrawableArr[i];
    }

    private Drawable getDrawableForCurrentState() {
        int i = 3;
        int i2 = 0;
        if (this.documentAttachType != 3) {
            if (this.documentAttachType != 5) {
                int i3 = 9;
                int i4 = 7;
                int i5 = 8;
                Drawable[][] drawableArr;
                if (this.documentAttachType != 1 || this.drawPhotoImage) {
                    this.radialProgress.setAlphaForPrevious(true);
                    if (this.buttonState < 0 || this.buttonState >= 4) {
                        if (this.buttonState == -1 && this.documentAttachType == 1) {
                            drawableArr = Theme.chat_photoStatesDrawables;
                            if (!this.currentMessageObject.isOutOwner()) {
                                i3 = 12;
                            }
                            return drawableArr[i3][isDrawSelectedBackground()];
                        }
                    } else if (this.documentAttachType != 1) {
                        return Theme.chat_photoStatesDrawables[this.buttonState][this.buttonPressed];
                    } else {
                        int image = this.buttonState;
                        if (this.buttonState == 0) {
                            if (!this.currentMessageObject.isOutOwner()) {
                                i4 = 10;
                            }
                            image = i4;
                        } else if (this.buttonState == 1) {
                            if (!this.currentMessageObject.isOutOwner()) {
                                i5 = 11;
                            }
                            image = i5;
                        }
                        Drawable[] drawableArr2 = Theme.chat_photoStatesDrawables[image];
                        if (!isDrawSelectedBackground()) {
                            if (this.buttonPressed == 0) {
                                return drawableArr2[i2];
                            }
                        }
                        i2 = 1;
                        return drawableArr2[i2];
                    }
                }
                this.radialProgress.setAlphaForPrevious(false);
                if (this.buttonState == -1) {
                    drawableArr = Theme.chat_fileStatesDrawable;
                    if (!this.currentMessageObject.isOutOwner()) {
                        i = 8;
                    }
                    return drawableArr[i][isDrawSelectedBackground()];
                } else if (this.buttonState == 0) {
                    drawableArr = Theme.chat_fileStatesDrawable;
                    if (this.currentMessageObject.isOutOwner()) {
                        i4 = 2;
                    }
                    return drawableArr[i4][isDrawSelectedBackground()];
                } else if (this.buttonState == 1) {
                    drawableArr = Theme.chat_fileStatesDrawable;
                    if (this.currentMessageObject.isOutOwner()) {
                        i3 = 4;
                    }
                    return drawableArr[i3][isDrawSelectedBackground()];
                }
                return null;
            }
        }
        if (this.buttonState == -1) {
            return null;
        }
        this.radialProgress.setAlphaForPrevious(false);
        this.radialProgress.setAlphaForMiniPrevious(true);
        Drawable[] drawableArr3 = Theme.chat_fileStatesDrawable[this.currentMessageObject.isOutOwner() ? this.buttonState : this.buttonState + 5];
        if (!isDrawSelectedBackground()) {
            if (this.buttonPressed == 0) {
                return drawableArr3[i2];
            }
        }
        i2 = 1;
        return drawableArr3[i2];
    }

    private int getMaxNameWidth() {
        int dWidth;
        if (this.documentAttachType != 6) {
            if (this.currentMessageObject.type != 5) {
                if (this.currentMessagesGroup != null) {
                    if (AndroidUtilities.isTablet()) {
                        dWidth = AndroidUtilities.getMinTabletSide();
                    } else {
                        dWidth = AndroidUtilities.displaySize.x;
                    }
                    int i = 0;
                    int firstLineWidth = 0;
                    for (int a = 0; a < this.currentMessagesGroup.posArray.size(); a++) {
                        GroupedMessagePosition position = (GroupedMessagePosition) this.currentMessagesGroup.posArray.get(a);
                        if (position.minY != (byte) 0) {
                            break;
                        }
                        firstLineWidth = (int) (((double) firstLineWidth) + Math.ceil((double) ((((float) (position.pw + position.leftSpanOffset)) / 1000.0f) * ((float) dWidth))));
                    }
                    if (this.isAvatarVisible) {
                        i = 48;
                    }
                    return firstLineWidth - AndroidUtilities.dp((float) (31 + i));
                }
                return this.backgroundWidth - AndroidUtilities.dp(this.mediaBackground ? 22.0f : 31.0f);
            }
        }
        if (AndroidUtilities.isTablet()) {
            if (this.isChat && !this.currentMessageObject.isOutOwner() && this.currentMessageObject.needDrawAvatar()) {
                dWidth = AndroidUtilities.getMinTabletSide() - AndroidUtilities.dp(42.0f);
            } else {
                dWidth = AndroidUtilities.getMinTabletSide();
            }
        } else if (this.isChat && !this.currentMessageObject.isOutOwner() && this.currentMessageObject.needDrawAvatar()) {
            dWidth = Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y) - AndroidUtilities.dp(42.0f);
        } else {
            dWidth = Math.min(AndroidUtilities.displaySize.x, AndroidUtilities.displaySize.y);
            return (dWidth - this.backgroundWidth) - AndroidUtilities.dp(57.0f);
        }
        return (dWidth - this.backgroundWidth) - AndroidUtilities.dp(57.0f);
    }

    public void updateButtonState(boolean animated) {
        boolean z = animated;
        this.drawRadialCheckBackground = false;
        String fileName = null;
        boolean fileExists = false;
        boolean z2 = true;
        if (this.currentMessageObject.type != 1) {
            if (!(r0.currentMessageObject.type == 8 || r0.currentMessageObject.type == 5 || r0.documentAttachType == 7 || r0.documentAttachType == 4 || r0.currentMessageObject.type == 9 || r0.documentAttachType == 3)) {
                if (r0.documentAttachType != 5) {
                    if (r0.documentAttachType != 0) {
                        fileName = FileLoader.getAttachFileName(r0.documentAttach);
                        fileExists = r0.currentMessageObject.mediaExists;
                    } else if (r0.currentPhotoObject != null) {
                        fileName = FileLoader.getAttachFileName(r0.currentPhotoObject);
                        fileExists = r0.currentMessageObject.mediaExists;
                    }
                }
            }
            if (r0.currentMessageObject.useCustomPhoto) {
                r0.buttonState = 1;
                r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                return;
            } else if (r0.currentMessageObject.attachPathExists) {
                fileName = r0.currentMessageObject.messageOwner.attachPath;
                fileExists = true;
            } else if (!r0.currentMessageObject.isSendError() || r0.documentAttachType == 3 || r0.documentAttachType == 5) {
                fileName = r0.currentMessageObject.getFileName();
                fileExists = r0.currentMessageObject.mediaExists;
            }
        } else if (r0.currentPhotoObject != null) {
            fileName = FileLoader.getAttachFileName(r0.currentPhotoObject);
            fileExists = r0.currentMessageObject.mediaExists;
        } else {
            return;
        }
        if (SharedConfig.streamMedia && ((int) r0.currentMessageObject.getDialogId()) != 0 && !r0.currentMessageObject.isSecretMedia() && (r0.documentAttachType == 5 || (r0.documentAttachType == 4 && r0.currentMessageObject.canStreamVideo()))) {
            r0.hasMiniProgress = fileExists ? 1 : 2;
            fileExists = true;
        }
        if (TextUtils.isEmpty(fileName)) {
            r0.radialProgress.setBackground(null, false, false);
            r0.radialProgress.setMiniBackground(null, false, false);
            return;
        }
        boolean z3;
        Float progress;
        boolean fromBot = r0.currentMessageObject.messageOwner.params != null && r0.currentMessageObject.messageOwner.params.containsKey("query_id");
        float f = 0.0f;
        if (r0.documentAttachType != 3) {
            if (r0.documentAttachType != 5) {
                if (r0.currentMessageObject.type == 0 && r0.documentAttachType != 1 && r0.documentAttachType != 4) {
                    if (r0.currentPhotoObject != null) {
                        if (r0.drawImageButton) {
                            if (fileExists) {
                                DownloadController.getInstance(r0.currentAccount).removeLoadingFileObserver(r0);
                                if (r0.documentAttachType != 2 || r0.photoImage.isAllowStartAnimation()) {
                                    r0.buttonState = -1;
                                } else {
                                    r0.buttonState = 2;
                                }
                                r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                                invalidate();
                            } else {
                                DownloadController.getInstance(r0.currentAccount).addLoadingFileObserver(fileName, r0.currentMessageObject, r0);
                                float setProgress = 0.0f;
                                boolean progressVisible = false;
                                if (FileLoader.getInstance(r0.currentAccount).isLoadingFile(fileName)) {
                                    progressVisible = true;
                                    r0.buttonState = 1;
                                    Float progress2 = ImageLoader.getInstance().getFileProgress(fileName);
                                    if (progress2 != null) {
                                        f = progress2.floatValue();
                                    }
                                    setProgress = f;
                                } else if (r0.cancelLoading || !((r0.documentAttachType == 0 && DownloadController.getInstance(r0.currentAccount).canDownloadMedia(r0.currentMessageObject)) || (r0.documentAttachType == 2 && MessageObject.isNewGifDocument(r0.documentAttach) && DownloadController.getInstance(r0.currentAccount).canDownloadMedia(r0.currentMessageObject)))) {
                                    r0.buttonState = 0;
                                } else {
                                    progressVisible = true;
                                    r0.buttonState = 1;
                                }
                                r0.radialProgress.setProgress(setProgress, false);
                                r0.radialProgress.setBackground(getDrawableForCurrentState(), progressVisible, z);
                                invalidate();
                            }
                            if (r0.hasMiniProgress == 0) {
                                r0.radialProgress.setMiniBackground(null, false, z);
                            }
                        }
                    }
                    return;
                } else if (r0.currentMessageObject.isOut() && r0.currentMessageObject.isSending()) {
                    if (r0.currentMessageObject.messageOwner.attachPath != null && r0.currentMessageObject.messageOwner.attachPath.length() > 0) {
                        HashMap<String, String> params;
                        Float progress3;
                        RadialProgress radialProgress;
                        DownloadController.getInstance(r0.currentAccount).addLoadingFileObserver(r0.currentMessageObject.messageOwner.attachPath, r0.currentMessageObject, r0);
                        if (r0.currentMessageObject.messageOwner.attachPath != null) {
                            if (r0.currentMessageObject.messageOwner.attachPath.startsWith("http")) {
                                z3 = false;
                                params = r0.currentMessageObject.messageOwner.params;
                                if (r0.currentMessageObject.messageOwner.message == null && params != null && (params.containsKey(UpdateFragment.FRAGMENT_URL) || params.containsKey("bot"))) {
                                    z3 = false;
                                    r0.buttonState = -1;
                                } else {
                                    r0.buttonState = 1;
                                }
                                sending = SendMessagesHelper.getInstance(r0.currentAccount).isSendingMessage(r0.currentMessageObject.getId());
                                if (r0.currentPosition == null && sending && r0.buttonState == 1) {
                                    r0.drawRadialCheckBackground = true;
                                    r0.radialProgress.setCheckBackground(false, z);
                                } else {
                                    r0.radialProgress.setBackground(getDrawableForCurrentState(), z3, z);
                                }
                                if (z3) {
                                    r0.radialProgress.setProgress(0.0f, false);
                                } else {
                                    progress3 = ImageLoader.getInstance().getFileProgress(r0.currentMessageObject.messageOwner.attachPath);
                                    if (progress3 == null && sending) {
                                        progress3 = Float.valueOf(1.0f);
                                    }
                                    radialProgress = r0.radialProgress;
                                    if (progress3 != null) {
                                        f = progress3.floatValue();
                                    }
                                    radialProgress.setProgress(f, false);
                                }
                                invalidate();
                            }
                        }
                        z3 = true;
                        params = r0.currentMessageObject.messageOwner.params;
                        if (r0.currentMessageObject.messageOwner.message == null) {
                        }
                        r0.buttonState = 1;
                        sending = SendMessagesHelper.getInstance(r0.currentAccount).isSendingMessage(r0.currentMessageObject.getId());
                        if (r0.currentPosition == null) {
                        }
                        r0.radialProgress.setBackground(getDrawableForCurrentState(), z3, z);
                        if (z3) {
                            r0.radialProgress.setProgress(0.0f, false);
                        } else {
                            progress3 = ImageLoader.getInstance().getFileProgress(r0.currentMessageObject.messageOwner.attachPath);
                            progress3 = Float.valueOf(1.0f);
                            radialProgress = r0.radialProgress;
                            if (progress3 != null) {
                                f = progress3.floatValue();
                            }
                            radialProgress.setProgress(f, false);
                        }
                        invalidate();
                    }
                    if (r0.hasMiniProgress == 0) {
                        r0.radialProgress.setMiniBackground(null, false, z);
                    }
                } else {
                    if (!(r0.currentMessageObject.messageOwner.attachPath == null || r0.currentMessageObject.messageOwner.attachPath.length() == 0)) {
                        DownloadController.getInstance(r0.currentAccount).removeLoadingFileObserver(r0);
                    }
                    if (r0.hasMiniProgress != 0) {
                        r0.radialProgress.setMiniProgressBackgroundColor(Theme.getColor(Theme.key_chat_inLoaderPhoto));
                        r0.buttonState = 3;
                        r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                        if (r0.hasMiniProgress == 1) {
                            DownloadController.getInstance(r0.currentAccount).removeLoadingFileObserver(r0);
                            r0.miniButtonState = -1;
                        } else {
                            DownloadController.getInstance(r0.currentAccount).addLoadingFileObserver(fileName, r0.currentMessageObject, r0);
                            if (FileLoader.getInstance(r0.currentAccount).isLoadingFile(fileName)) {
                                r0.miniButtonState = 1;
                                progress = ImageLoader.getInstance().getFileProgress(fileName);
                                if (progress != null) {
                                    r0.radialProgress.setProgress(progress.floatValue(), z);
                                } else {
                                    r0.radialProgress.setProgress(0.0f, z);
                                }
                            } else {
                                r0.radialProgress.setProgress(0.0f, z);
                                r0.miniButtonState = 0;
                            }
                        }
                        RadialProgress radialProgress2 = r0.radialProgress;
                        Drawable miniDrawableForCurrentState = getMiniDrawableForCurrentState();
                        if (r0.miniButtonState != 1) {
                            z2 = false;
                        }
                        radialProgress2.setMiniBackground(miniDrawableForCurrentState, z2, z);
                    } else if (fileExists) {
                        DownloadController.getInstance(r0.currentAccount).removeLoadingFileObserver(r0);
                        if (r0.currentMessageObject.needDrawBluredPreview()) {
                            r0.buttonState = -1;
                        } else if (r0.currentMessageObject.type == 8 && !r0.photoImage.isAllowStartAnimation()) {
                            r0.buttonState = 2;
                        } else if (r0.documentAttachType == 4) {
                            r0.buttonState = 3;
                        } else {
                            r0.buttonState = -1;
                        }
                        r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                        if (r0.photoNotSet) {
                            setMessageObject(r0.currentMessageObject, r0.currentMessagesGroup, r0.pinnedBottom, r0.pinnedTop);
                        }
                        invalidate();
                    } else {
                        DownloadController.getInstance(r0.currentAccount).addLoadingFileObserver(fileName, r0.currentMessageObject, r0);
                        float setProgress2 = 0.0f;
                        sending = false;
                        if (FileLoader.getInstance(r0.currentAccount).isLoadingFile(fileName)) {
                            sending = true;
                            r0.buttonState = 1;
                            progress = ImageLoader.getInstance().getFileProgress(fileName);
                            if (progress != null) {
                                f = progress.floatValue();
                            }
                            setProgress2 = f;
                        } else {
                            boolean autoDownload = false;
                            if (r0.currentMessageObject.type == 1) {
                                autoDownload = DownloadController.getInstance(r0.currentAccount).canDownloadMedia(r0.currentMessageObject);
                            } else if (r0.currentMessageObject.type == 8 && MessageObject.isNewGifDocument(r0.currentMessageObject.messageOwner.media.document)) {
                                autoDownload = DownloadController.getInstance(r0.currentAccount).canDownloadMedia(r0.currentMessageObject);
                            } else if (r0.currentMessageObject.type == 5) {
                                autoDownload = DownloadController.getInstance(r0.currentAccount).canDownloadMedia(r0.currentMessageObject);
                            }
                            if (r0.cancelLoading || !autoDownload) {
                                r0.buttonState = 0;
                            } else {
                                sending = true;
                                r0.buttonState = 1;
                            }
                        }
                        r0.radialProgress.setBackground(getDrawableForCurrentState(), sending, z);
                        r0.radialProgress.setProgress(setProgress2, false);
                        invalidate();
                    }
                    if (r0.hasMiniProgress == 0) {
                        r0.radialProgress.setMiniBackground(null, false, z);
                    }
                }
            }
        }
        RadialProgress radialProgress3;
        if ((r0.currentMessageObject.isOut() && r0.currentMessageObject.isSending()) || (r0.currentMessageObject.isSendError() && fromBot)) {
            DownloadController.getInstance(r0.currentAccount).addLoadingFileObserver(r0.currentMessageObject.messageOwner.attachPath, r0.currentMessageObject, r0);
            r0.buttonState = 4;
            radialProgress2 = r0.radialProgress;
            miniDrawableForCurrentState = getDrawableForCurrentState();
            if (fromBot) {
                z2 = false;
            }
            radialProgress2.setBackground(miniDrawableForCurrentState, z2, z);
            if (fromBot) {
                r0.radialProgress.setProgress(0.0f, false);
            } else {
                progress = ImageLoader.getInstance().getFileProgress(r0.currentMessageObject.messageOwner.attachPath);
                if (progress == null && SendMessagesHelper.getInstance(r0.currentAccount).isSendingMessage(r0.currentMessageObject.getId())) {
                    progress = Float.valueOf(1.0f);
                }
                radialProgress3 = r0.radialProgress;
                if (progress != null) {
                    f = progress.floatValue();
                }
                radialProgress3.setProgress(f, false);
            }
        } else if (r0.hasMiniProgress != 0) {
            Float progress4;
            Drawable miniDrawableForCurrentState2;
            r0.radialProgress.setMiniProgressBackgroundColor(Theme.getColor(r0.currentMessageObject.isOutOwner() ? Theme.key_chat_outLoader : Theme.key_chat_inLoader));
            z3 = MediaController.getInstance().isPlayingMessage(r0.currentMessageObject);
            if (z3) {
                if (!z3 || !MediaController.getInstance().isMessagePaused()) {
                    r0.buttonState = 1;
                    r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                    if (r0.hasMiniProgress != 1) {
                        DownloadController.getInstance(r0.currentAccount).removeLoadingFileObserver(r0);
                        r0.miniButtonState = -1;
                    } else {
                        DownloadController.getInstance(r0.currentAccount).addLoadingFileObserver(fileName, r0.currentMessageObject, r0);
                        if (FileLoader.getInstance(r0.currentAccount).isLoadingFile(fileName)) {
                            r0.radialProgress.setProgress(0.0f, z);
                            r0.miniButtonState = 0;
                        } else {
                            r0.miniButtonState = 1;
                            progress4 = ImageLoader.getInstance().getFileProgress(fileName);
                            if (progress4 == null) {
                                r0.radialProgress.setProgress(progress4.floatValue(), z);
                            } else {
                                r0.radialProgress.setProgress(0.0f, z);
                            }
                        }
                    }
                    radialProgress3 = r0.radialProgress;
                    miniDrawableForCurrentState2 = getMiniDrawableForCurrentState();
                    if (r0.miniButtonState == 1) {
                        z2 = false;
                    }
                    radialProgress3.setMiniBackground(miniDrawableForCurrentState2, z2, z);
                }
            }
            r0.buttonState = 0;
            r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
            if (r0.hasMiniProgress != 1) {
                DownloadController.getInstance(r0.currentAccount).addLoadingFileObserver(fileName, r0.currentMessageObject, r0);
                if (FileLoader.getInstance(r0.currentAccount).isLoadingFile(fileName)) {
                    r0.miniButtonState = 1;
                    progress4 = ImageLoader.getInstance().getFileProgress(fileName);
                    if (progress4 == null) {
                        r0.radialProgress.setProgress(0.0f, z);
                    } else {
                        r0.radialProgress.setProgress(progress4.floatValue(), z);
                    }
                } else {
                    r0.radialProgress.setProgress(0.0f, z);
                    r0.miniButtonState = 0;
                }
            } else {
                DownloadController.getInstance(r0.currentAccount).removeLoadingFileObserver(r0);
                r0.miniButtonState = -1;
            }
            radialProgress3 = r0.radialProgress;
            miniDrawableForCurrentState2 = getMiniDrawableForCurrentState();
            if (r0.miniButtonState == 1) {
                z2 = false;
            }
            radialProgress3.setMiniBackground(miniDrawableForCurrentState2, z2, z);
        } else if (fileExists) {
            DownloadController.getInstance(r0.currentAccount).removeLoadingFileObserver(r0);
            z3 = MediaController.getInstance().isPlayingMessage(r0.currentMessageObject);
            if (z3) {
                if (!z3 || !MediaController.getInstance().isMessagePaused()) {
                    r0.buttonState = 1;
                    r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                }
            }
            r0.buttonState = 0;
            r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
        } else {
            DownloadController.getInstance(r0.currentAccount).addLoadingFileObserver(fileName, r0.currentMessageObject, r0);
            if (FileLoader.getInstance(r0.currentAccount).isLoadingFile(fileName)) {
                r0.buttonState = 4;
                progress = ImageLoader.getInstance().getFileProgress(fileName);
                if (progress != null) {
                    r0.radialProgress.setProgress(progress.floatValue(), z);
                } else {
                    r0.radialProgress.setProgress(0.0f, z);
                }
                r0.radialProgress.setBackground(getDrawableForCurrentState(), true, z);
            } else {
                r0.buttonState = 2;
                r0.radialProgress.setProgress(0.0f, z);
                r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
            }
        }
        updatePlayingMessageProgress();
        if (r0.hasMiniProgress == 0) {
            r0.radialProgress.setMiniBackground(null, false, z);
        }
    }

    private void didPressedMiniButton(boolean animated) {
        if (this.miniButtonState == 0) {
            this.miniButtonState = 1;
            this.radialProgress.setProgress(0.0f, false);
            if (this.documentAttachType != 3) {
                if (this.documentAttachType != 5) {
                    if (this.documentAttachType == 4) {
                        FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, true, this.currentMessageObject.shouldEncryptPhotoOrVideo() ? 2 : 0);
                    }
                    this.radialProgress.setMiniBackground(getMiniDrawableForCurrentState(), true, false);
                    invalidate();
                }
            }
            FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, true, 0);
            this.radialProgress.setMiniBackground(getMiniDrawableForCurrentState(), true, false);
            invalidate();
        } else if (this.miniButtonState == 1) {
            if ((this.documentAttachType == 3 || this.documentAttachType == 5) && MediaController.getInstance().isPlayingMessage(this.currentMessageObject)) {
                MediaController.getInstance().cleanupPlayer(true, true);
            }
            this.miniButtonState = 0;
            FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
            this.radialProgress.setMiniBackground(getMiniDrawableForCurrentState(), true, false);
            invalidate();
        }
    }

    private void didPressedButton(boolean animated) {
        boolean z = animated;
        int i = 2;
        if (this.buttonState == 0) {
            if (r0.documentAttachType != 3) {
                if (r0.documentAttachType != 5) {
                    r0.cancelLoading = false;
                    r0.radialProgress.setProgress(0.0f, false);
                    FileLocation fileLocation = null;
                    ImageReceiver imageReceiver;
                    TLObject tLObject;
                    String str;
                    if (r0.currentMessageObject.type == 1) {
                        r0.photoImage.setForceLoading(true);
                        imageReceiver = r0.photoImage;
                        tLObject = r0.currentPhotoObject.location;
                        str = r0.currentPhotoFilter;
                        if (r0.currentPhotoObjectThumb != null) {
                            fileLocation = r0.currentPhotoObjectThumb.location;
                        }
                        imageReceiver.setImage(tLObject, str, fileLocation, r0.currentPhotoFilterThumb, r0.currentPhotoObject.size, null, r0.currentMessageObject.shouldEncryptPhotoOrVideo() ? 2 : 0);
                    } else if (r0.currentMessageObject.type == 8) {
                        r0.currentMessageObject.gifState = 2.0f;
                        r0.photoImage.setForceLoading(true);
                        imageReceiver = r0.photoImage;
                        tLObject = r0.currentMessageObject.messageOwner.media.document;
                        if (r0.currentPhotoObject != null) {
                            fileLocation = r0.currentPhotoObject.location;
                        }
                        imageReceiver.setImage(tLObject, null, fileLocation, r0.currentPhotoFilterThumb, r0.currentMessageObject.messageOwner.media.document.size, null, 0);
                    } else if (r0.currentMessageObject.isRoundVideo()) {
                        if (r0.currentMessageObject.isSecretMedia()) {
                            FileLoader.getInstance(r0.currentAccount).loadFile(r0.currentMessageObject.getDocument(), true, 1);
                        } else {
                            r0.currentMessageObject.gifState = 2.0f;
                            TLObject document = r0.currentMessageObject.getDocument();
                            r0.photoImage.setForceLoading(true);
                            imageReceiver = r0.photoImage;
                            if (r0.currentPhotoObject != null) {
                                fileLocation = r0.currentPhotoObject.location;
                            }
                            imageReceiver.setImage(document, null, fileLocation, r0.currentPhotoFilterThumb, document.size, null, 0);
                        }
                    } else if (r0.currentMessageObject.type == 9) {
                        FileLoader.getInstance(r0.currentAccount).loadFile(r0.currentMessageObject.messageOwner.media.document, false, 0);
                    } else if (r0.documentAttachType == 4) {
                        FileLoader instance = FileLoader.getInstance(r0.currentAccount);
                        Document document2 = r0.documentAttach;
                        if (!r0.currentMessageObject.shouldEncryptPhotoOrVideo()) {
                            i = 0;
                        }
                        instance.loadFile(document2, true, i);
                    } else if (r0.currentMessageObject.type != 0 || r0.documentAttachType == 0) {
                        r0.photoImage.setForceLoading(true);
                        imageReceiver = r0.photoImage;
                        tLObject = r0.currentPhotoObject.location;
                        str = r0.currentPhotoFilter;
                        if (r0.currentPhotoObjectThumb != null) {
                            fileLocation = r0.currentPhotoObjectThumb.location;
                        }
                        imageReceiver.setImage(tLObject, str, fileLocation, r0.currentPhotoFilterThumb, 0, null, 0);
                    } else if (r0.documentAttachType == 2) {
                        r0.photoImage.setForceLoading(true);
                        r0.photoImage.setImage(r0.currentMessageObject.messageOwner.media.webpage.document, null, r0.currentPhotoObject.location, r0.currentPhotoFilterThumb, r0.currentMessageObject.messageOwner.media.webpage.document.size, null, 0);
                        r0.currentMessageObject.gifState = 2.0f;
                    } else if (r0.documentAttachType == 1) {
                        FileLoader.getInstance(r0.currentAccount).loadFile(r0.currentMessageObject.messageOwner.media.webpage.document, false, 0);
                    }
                    r0.buttonState = 1;
                    r0.radialProgress.setBackground(getDrawableForCurrentState(), true, z);
                    invalidate();
                    return;
                }
            }
            if (r0.miniButtonState == 0) {
                FileLoader.getInstance(r0.currentAccount).loadFile(r0.documentAttach, true, 0);
            }
            if (r0.delegate.needPlayMessage(r0.currentMessageObject)) {
                if (r0.hasMiniProgress == 2 && r0.miniButtonState != 1) {
                    r0.miniButtonState = 1;
                    r0.radialProgress.setProgress(0.0f, false);
                    r0.radialProgress.setMiniBackground(getMiniDrawableForCurrentState(), true, false);
                }
                updatePlayingMessageProgress();
                r0.buttonState = 1;
                r0.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                invalidate();
            }
        } else if (r0.buttonState == 1) {
            if (r0.documentAttachType != 3) {
                if (r0.documentAttachType != 5) {
                    if (!r0.currentMessageObject.isOut() || !r0.currentMessageObject.isSending()) {
                        r0.cancelLoading = true;
                        if (r0.documentAttachType != 4) {
                            if (r0.documentAttachType != 1) {
                                if (!(r0.currentMessageObject.type == 0 || r0.currentMessageObject.type == 1 || r0.currentMessageObject.type == 8)) {
                                    if (r0.currentMessageObject.type != 5) {
                                        if (r0.currentMessageObject.type == 9) {
                                            FileLoader.getInstance(r0.currentAccount).cancelLoadFile(r0.currentMessageObject.messageOwner.media.document);
                                        }
                                        r0.buttonState = 0;
                                        r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                                        invalidate();
                                        return;
                                    }
                                }
                                ImageLoader.getInstance().cancelForceLoadingForImageReceiver(r0.photoImage);
                                r0.photoImage.cancelLoadImage();
                                r0.buttonState = 0;
                                r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                                invalidate();
                                return;
                            }
                        }
                        FileLoader.getInstance(r0.currentAccount).cancelLoadFile(r0.documentAttach);
                        r0.buttonState = 0;
                        r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                        invalidate();
                        return;
                    } else if (!r0.radialProgress.isDrawCheckDrawable()) {
                        r0.delegate.didPressedCancelSendButton(r0);
                        return;
                    } else {
                        return;
                    }
                }
            }
            if (MediaController.getInstance().pauseMessage(r0.currentMessageObject)) {
                r0.buttonState = 0;
                r0.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                invalidate();
            }
        } else if (r0.buttonState == 2) {
            if (r0.documentAttachType != 3) {
                if (r0.documentAttachType != 5) {
                    r0.photoImage.setAllowStartAnimation(true);
                    r0.photoImage.startAnimation();
                    r0.currentMessageObject.gifState = 0.0f;
                    r0.buttonState = -1;
                    r0.radialProgress.setBackground(getDrawableForCurrentState(), false, z);
                    return;
                }
            }
            r0.radialProgress.setProgress(0.0f, false);
            FileLoader.getInstance(r0.currentAccount).loadFile(r0.documentAttach, true, 0);
            r0.buttonState = 4;
            r0.radialProgress.setBackground(getDrawableForCurrentState(), true, false);
            invalidate();
        } else if (r0.buttonState == 3) {
            if (r0.hasMiniProgress == 2 && r0.miniButtonState != 1) {
                r0.miniButtonState = 1;
                r0.radialProgress.setProgress(0.0f, false);
                r0.radialProgress.setMiniBackground(getMiniDrawableForCurrentState(), true, false);
            }
            r0.delegate.didPressedImage(r0);
        } else if (r0.buttonState != 4) {
        } else {
            if (r0.documentAttachType != 3 && r0.documentAttachType != 5) {
                return;
            }
            if ((!r0.currentMessageObject.isOut() || !r0.currentMessageObject.isSending()) && !r0.currentMessageObject.isSendError()) {
                FileLoader.getInstance(r0.currentAccount).cancelLoadFile(r0.documentAttach);
                r0.buttonState = 2;
                r0.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                invalidate();
            } else if (r0.delegate != null) {
                r0.delegate.didPressedCancelSendButton(r0);
            }
        }
    }

    public void onFailedDownload(String fileName) {
        boolean z;
        if (this.documentAttachType != 3) {
            if (this.documentAttachType != 5) {
                z = false;
                updateButtonState(z);
            }
        }
        z = true;
        updateButtonState(z);
    }

    public void onSuccessDownload(String fileName) {
        if (this.documentAttachType != 3) {
            if (this.documentAttachType != 5) {
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
                        return;
                    }
                    return;
                } else if (this.documentAttachType == 2 && this.currentMessageObject.gifState != 1.0f) {
                    this.buttonState = 2;
                    didPressedButton(true);
                    return;
                } else if (this.photoNotSet) {
                    setMessageObject(this.currentMessageObject, this.currentMessagesGroup, this.pinnedBottom, this.pinnedTop);
                    return;
                } else {
                    updateButtonState(true);
                    return;
                }
            }
        }
        updateButtonState(true);
        updateWaveform();
    }

    public void didSetImage(ImageReceiver imageReceiver, boolean set, boolean thumb) {
        if (this.currentMessageObject != null && set && !thumb && !this.currentMessageObject.mediaExists && !this.currentMessageObject.attachPathExists) {
            this.currentMessageObject.mediaExists = true;
            updateButtonState(true);
        }
    }

    public void onProgressDownload(String fileName, float progress) {
        this.radialProgress.setProgress(progress, true);
        if (this.documentAttachType != 3) {
            if (this.documentAttachType != 5) {
                if (this.hasMiniProgress != 0) {
                    if (this.miniButtonState != 1) {
                        updateButtonState(false);
                        return;
                    }
                    return;
                } else if (this.buttonState != 1) {
                    updateButtonState(false);
                    return;
                } else {
                    return;
                }
            }
        }
        if (this.hasMiniProgress != 0) {
            if (this.miniButtonState != 1) {
                updateButtonState(false);
            }
        } else if (this.buttonState != 4) {
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

    private boolean isDrawSelectedBackground() {
        return (isPressed() && this.isCheckPressed) || ((!this.isCheckPressed && this.isPressed) || this.isHighlighted);
    }

    private boolean isOpenChatByShare(MessageObject messageObject) {
        return (messageObject.messageOwner.fwd_from == null || messageObject.messageOwner.fwd_from.saved_from_peer == null) ? false : true;
    }

    private boolean checkNeedDrawShareButton(MessageObject messageObject) {
        boolean z = false;
        if ((this.currentPosition != null && !this.currentPosition.last) || messageObject.eventId != 0) {
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
                if (!((messageObject.messageOwner.media instanceof TL_messageMediaEmpty) || messageObject.messageOwner.media == null)) {
                    if (!(messageObject.messageOwner.media instanceof TL_messageMediaWebPage) || (messageObject.messageOwner.media.webpage instanceof TL_webPage)) {
                        User user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(messageObject.messageOwner.from_id));
                        if (user != null && user.bot) {
                            return true;
                        }
                        if (!messageObject.isOut()) {
                            if (!(messageObject.messageOwner.media instanceof TL_messageMediaGame)) {
                                if (!(messageObject.messageOwner.media instanceof TL_messageMediaInvoice)) {
                                    if (messageObject.isMegagroup()) {
                                        Chat chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(messageObject.messageOwner.to_id.channel_id));
                                        if (!(chat == null || chat.username == null || chat.username.length() <= 0 || (messageObject.messageOwner.media instanceof TL_messageMediaContact) || (messageObject.messageOwner.media instanceof TL_messageMediaGeo))) {
                                            z = true;
                                        }
                                        return z;
                                    }
                                }
                            }
                            return true;
                        }
                    }
                }
                return false;
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

    public int getCaptionHeight() {
        return this.addedCaptionHeight;
    }

    public ImageReceiver getAvatarImage() {
        return this.isAvatarVisible ? this.avatarImage : null;
    }

    protected void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        if (this.currentMessageObject != null) {
            if (r0.wasLayout) {
                Drawable drawable;
                Drawable currentBackgroundShadowDrawable;
                int backgroundLeft;
                int offsetBottom;
                Drawable currentBackgroundShadowDrawable2;
                if (r0.currentMessageObject.isOutOwner()) {
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
                if (r0.documentAttach != null) {
                    if (r0.documentAttachType == 3) {
                        if (r0.currentMessageObject.isOutOwner()) {
                            r0.seekBarWaveform.setColors(Theme.getColor(Theme.key_chat_outVoiceSeekbar), Theme.getColor(Theme.key_chat_outVoiceSeekbarFill), Theme.getColor(Theme.key_chat_outVoiceSeekbarSelected));
                            r0.seekBar.setColors(Theme.getColor(Theme.key_chat_outAudioSeekbar), Theme.getColor(Theme.key_chat_outAudioCacheSeekbar), Theme.getColor(Theme.key_chat_outAudioSeekbarFill), Theme.getColor(Theme.key_chat_outAudioSeekbarFill), Theme.getColor(Theme.key_chat_outAudioSeekbarSelected));
                        } else {
                            r0.seekBarWaveform.setColors(Theme.getColor(Theme.key_chat_inVoiceSeekbar), Theme.getColor(Theme.key_chat_inVoiceSeekbarFill), Theme.getColor(Theme.key_chat_inVoiceSeekbarSelected));
                            r0.seekBar.setColors(Theme.getColor(Theme.key_chat_inAudioSeekbar), Theme.getColor(Theme.key_chat_inAudioCacheSeekbar), Theme.getColor(Theme.key_chat_inAudioSeekbarFill), Theme.getColor(Theme.key_chat_inAudioSeekbarFill), Theme.getColor(Theme.key_chat_inAudioSeekbarSelected));
                        }
                    } else if (r0.documentAttachType == 5) {
                        r0.documentAttachType = 5;
                        if (r0.currentMessageObject.isOutOwner()) {
                            r0.seekBar.setColors(Theme.getColor(Theme.key_chat_outAudioSeekbar), Theme.getColor(Theme.key_chat_outAudioCacheSeekbar), Theme.getColor(Theme.key_chat_outAudioSeekbarFill), Theme.getColor(Theme.key_chat_outAudioSeekbarFill), Theme.getColor(Theme.key_chat_outAudioSeekbarSelected));
                        } else {
                            r0.seekBar.setColors(Theme.getColor(Theme.key_chat_inAudioSeekbar), Theme.getColor(Theme.key_chat_inAudioCacheSeekbar), Theme.getColor(Theme.key_chat_inAudioSeekbarFill), Theme.getColor(Theme.key_chat_inAudioSeekbarFill), Theme.getColor(Theme.key_chat_inAudioSeekbarSelected));
                        }
                    }
                }
                if (r0.currentMessageObject.type == 5) {
                    Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_mediaTimeText));
                } else if (r0.mediaBackground) {
                    if (r0.currentMessageObject.type != 13) {
                        if (r0.currentMessageObject.type != 5) {
                            Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_mediaTimeText));
                        }
                    }
                    Theme.chat_timePaint.setColor(Theme.getColor(Theme.key_chat_serviceText));
                } else if (r0.currentMessageObject.isOutOwner()) {
                    Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outTimeSelectedText : Theme.key_chat_outTimeText));
                } else {
                    Theme.chat_timePaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inTimeSelectedText : Theme.key_chat_inTimeText));
                }
                int additionalTop = 0;
                int additionalBottom = 0;
                if (r0.currentMessageObject.isOutOwner()) {
                    int backgroundTop;
                    if (r0.mediaBackground || r0.drawPinnedBottom) {
                        r0.currentBackgroundDrawable = Theme.chat_msgOutMediaDrawable;
                        drawable = Theme.chat_msgOutMediaSelectedDrawable;
                        currentBackgroundShadowDrawable = Theme.chat_msgOutMediaShadowDrawable;
                    } else {
                        r0.currentBackgroundDrawable = Theme.chat_msgOutDrawable;
                        drawable = Theme.chat_msgOutSelectedDrawable;
                        currentBackgroundShadowDrawable = Theme.chat_msgOutShadowDrawable;
                    }
                    r0.backgroundDrawableLeft = (r0.layoutWidth - r0.backgroundWidth) - (!r0.mediaBackground ? 0 : AndroidUtilities.dp(9.0f));
                    r0.backgroundDrawableRight = r0.backgroundWidth - (r0.mediaBackground ? 0 : AndroidUtilities.dp(3.0f));
                    if (!(r0.currentMessagesGroup == null || r0.currentPosition.edge)) {
                        r0.backgroundDrawableRight += AndroidUtilities.dp(10.0f);
                    }
                    backgroundLeft = r0.backgroundDrawableLeft;
                    if (!r0.mediaBackground && r0.drawPinnedBottom) {
                        r0.backgroundDrawableRight -= AndroidUtilities.dp(6.0f);
                    }
                    if (r0.currentPosition != null) {
                        if ((r0.currentPosition.flags & 2) == 0) {
                            r0.backgroundDrawableRight += AndroidUtilities.dp(8.0f);
                        }
                        if ((r0.currentPosition.flags & 1) == 0) {
                            backgroundLeft -= AndroidUtilities.dp(8.0f);
                            r0.backgroundDrawableRight += AndroidUtilities.dp(8.0f);
                        }
                        if ((r0.currentPosition.flags & 4) == 0) {
                            additionalTop = 0 - AndroidUtilities.dp(9.0f);
                            additionalBottom = 0 + AndroidUtilities.dp(9.0f);
                        }
                        if ((r0.currentPosition.flags & 8) == 0) {
                            additionalBottom += AndroidUtilities.dp(9.0f);
                        }
                    }
                    if (r0.drawPinnedBottom && r0.drawPinnedTop) {
                        offsetBottom = 0;
                    } else if (r0.drawPinnedBottom) {
                        offsetBottom = AndroidUtilities.dp(1.0f);
                    } else {
                        offsetBottom = AndroidUtilities.dp(2.0f);
                        if (!r0.drawPinnedTop) {
                            if (r0.drawPinnedTop || !r0.drawPinnedBottom) {
                                backgroundTop = AndroidUtilities.dp(1.0f);
                                backgroundTop += additionalTop;
                                BaseCell.setDrawableBounds(r0.currentBackgroundDrawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                                BaseCell.setDrawableBounds(drawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                                BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                                currentBackgroundShadowDrawable2 = currentBackgroundShadowDrawable;
                            }
                        }
                        backgroundTop = 0;
                        backgroundTop += additionalTop;
                        BaseCell.setDrawableBounds(r0.currentBackgroundDrawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                        BaseCell.setDrawableBounds(drawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                        BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                        currentBackgroundShadowDrawable2 = currentBackgroundShadowDrawable;
                    }
                    if (r0.drawPinnedTop) {
                        if (r0.drawPinnedTop) {
                        }
                        backgroundTop = AndroidUtilities.dp(1.0f);
                        backgroundTop += additionalTop;
                        BaseCell.setDrawableBounds(r0.currentBackgroundDrawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                        BaseCell.setDrawableBounds(drawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                        BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                        currentBackgroundShadowDrawable2 = currentBackgroundShadowDrawable;
                    }
                    backgroundTop = 0;
                    backgroundTop += additionalTop;
                    BaseCell.setDrawableBounds(r0.currentBackgroundDrawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                    BaseCell.setDrawableBounds(drawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                    BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, backgroundLeft, backgroundTop, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom) + additionalBottom);
                    currentBackgroundShadowDrawable2 = currentBackgroundShadowDrawable;
                } else {
                    int offsetBottom2;
                    if (r0.mediaBackground || r0.drawPinnedBottom) {
                        r0.currentBackgroundDrawable = Theme.chat_msgInMediaDrawable;
                        currentBackgroundShadowDrawable = Theme.chat_msgInMediaSelectedDrawable;
                        currentBackgroundShadowDrawable2 = Theme.chat_msgInMediaShadowDrawable;
                    } else {
                        r0.currentBackgroundDrawable = Theme.chat_msgInDrawable;
                        currentBackgroundShadowDrawable = Theme.chat_msgInSelectedDrawable;
                        currentBackgroundShadowDrawable2 = Theme.chat_msgInShadowDrawable;
                    }
                    int i = (r0.isChat && r0.isAvatarVisible) ? 48 : 0;
                    r0.backgroundDrawableLeft = AndroidUtilities.dp((float) (i + (!r0.mediaBackground ? 3 : 9)));
                    r0.backgroundDrawableRight = r0.backgroundWidth - (r0.mediaBackground ? 0 : AndroidUtilities.dp(3.0f));
                    if (r0.currentMessagesGroup != null) {
                        if (!r0.currentPosition.edge) {
                            r0.backgroundDrawableLeft -= AndroidUtilities.dp(10.0f);
                            r0.backgroundDrawableRight += AndroidUtilities.dp(10.0f);
                        }
                        if (r0.currentPosition.leftSpanOffset != 0) {
                            r0.backgroundDrawableLeft += (int) Math.ceil((double) ((((float) r0.currentPosition.leftSpanOffset) / 1000.0f) * ((float) getGroupPhotosWidth())));
                        }
                    }
                    if (!r0.mediaBackground && r0.drawPinnedBottom) {
                        r0.backgroundDrawableRight -= AndroidUtilities.dp(6.0f);
                        r0.backgroundDrawableLeft += AndroidUtilities.dp(6.0f);
                    }
                    if (r0.currentPosition != null) {
                        if ((r0.currentPosition.flags & 2) == 0) {
                            r0.backgroundDrawableRight += AndroidUtilities.dp(8.0f);
                        }
                        if ((r0.currentPosition.flags & 1) == 0) {
                            r0.backgroundDrawableLeft -= AndroidUtilities.dp(8.0f);
                            r0.backgroundDrawableRight += AndroidUtilities.dp(8.0f);
                        }
                        if ((r0.currentPosition.flags & 4) == 0) {
                            additionalTop = 0 - AndroidUtilities.dp(9.0f);
                            additionalBottom = 0 + AndroidUtilities.dp(9.0f);
                        }
                        if ((r0.currentPosition.flags & 8) == 0) {
                            additionalBottom += AndroidUtilities.dp(10.0f);
                        }
                    }
                    if (r0.drawPinnedBottom && r0.drawPinnedTop) {
                        offsetBottom2 = 0;
                    } else if (r0.drawPinnedBottom) {
                        offsetBottom2 = AndroidUtilities.dp(1.0f);
                    } else {
                        offsetBottom2 = AndroidUtilities.dp(2.0f);
                        if (!r0.drawPinnedTop) {
                            if (r0.drawPinnedTop || !r0.drawPinnedBottom) {
                                offsetBottom = AndroidUtilities.dp(1.0f);
                                offsetBottom += additionalTop;
                                BaseCell.setDrawableBounds(r0.currentBackgroundDrawable, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                                BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                                BaseCell.setDrawableBounds(currentBackgroundShadowDrawable2, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                                drawable = currentBackgroundShadowDrawable;
                            }
                        }
                        offsetBottom = 0;
                        offsetBottom += additionalTop;
                        BaseCell.setDrawableBounds(r0.currentBackgroundDrawable, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                        BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                        BaseCell.setDrawableBounds(currentBackgroundShadowDrawable2, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                        drawable = currentBackgroundShadowDrawable;
                    }
                    if (r0.drawPinnedTop) {
                        if (r0.drawPinnedTop) {
                        }
                        offsetBottom = AndroidUtilities.dp(1.0f);
                        offsetBottom += additionalTop;
                        BaseCell.setDrawableBounds(r0.currentBackgroundDrawable, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                        BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                        BaseCell.setDrawableBounds(currentBackgroundShadowDrawable2, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                        drawable = currentBackgroundShadowDrawable;
                    }
                    offsetBottom = 0;
                    offsetBottom += additionalTop;
                    BaseCell.setDrawableBounds(r0.currentBackgroundDrawable, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                    BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                    BaseCell.setDrawableBounds(currentBackgroundShadowDrawable2, r0.backgroundDrawableLeft, offsetBottom, r0.backgroundDrawableRight, (r0.layoutHeight - offsetBottom2) + additionalBottom);
                    drawable = currentBackgroundShadowDrawable;
                }
                if (r0.drawBackground && r0.currentBackgroundDrawable != null) {
                    if (r0.isHighlightedAnimated) {
                        r0.currentBackgroundDrawable.draw(canvas2);
                        float alpha = r0.highlightProgress >= 300 ? 1.0f : ((float) r0.highlightProgress) / 300.0f;
                        if (r0.currentPosition == null) {
                            drawable.setAlpha((int) (255.0f * alpha));
                            drawable.draw(canvas2);
                        }
                        long newTime = System.currentTimeMillis();
                        long dt = Math.abs(newTime - r0.lastHighlightProgressTime);
                        if (dt > 17) {
                            dt = 17;
                        }
                        r0.highlightProgress = (int) (((long) r0.highlightProgress) - dt);
                        r0.lastHighlightProgressTime = newTime;
                        if (r0.highlightProgress <= 0) {
                            r0.highlightProgress = 0;
                            r0.isHighlightedAnimated = false;
                        }
                        invalidate();
                    } else if (!isDrawSelectedBackground() || (r0.currentPosition != null && getBackground() == null)) {
                        r0.currentBackgroundDrawable.draw(canvas2);
                    } else {
                        drawable.setAlpha(255);
                        drawable.draw(canvas2);
                    }
                    if (r0.currentPosition == null || r0.currentPosition.flags != 0) {
                        currentBackgroundShadowDrawable2.draw(canvas2);
                    }
                }
                drawContent(canvas);
                if (r0.drawShareButton) {
                    Theme.chat_shareDrawable.setColorFilter(r0.sharePressed ? Theme.colorPressedFilter : Theme.colorFilter);
                    if (r0.currentMessageObject.isOutOwner()) {
                        r0.shareStartX = (r0.currentBackgroundDrawable.getBounds().left - AndroidUtilities.dp(8.0f)) - Theme.chat_shareDrawable.getIntrinsicWidth();
                    } else {
                        r0.shareStartX = r0.currentBackgroundDrawable.getBounds().right + AndroidUtilities.dp(8.0f);
                    }
                    currentBackgroundShadowDrawable = Theme.chat_shareDrawable;
                    offsetBottom = r0.shareStartX;
                    backgroundLeft = r0.layoutHeight - AndroidUtilities.dp(41.0f);
                    r0.shareStartY = backgroundLeft;
                    BaseCell.setDrawableBounds(currentBackgroundShadowDrawable, offsetBottom, backgroundLeft);
                    Theme.chat_shareDrawable.draw(canvas2);
                    if (r0.drwaShareGoIcon) {
                        BaseCell.setDrawableBounds(Theme.chat_goIconDrawable, r0.shareStartX + AndroidUtilities.dp(12.0f), r0.shareStartY + AndroidUtilities.dp(9.0f));
                        Theme.chat_goIconDrawable.draw(canvas2);
                    } else {
                        BaseCell.setDrawableBounds(Theme.chat_shareIconDrawable, r0.shareStartX + AndroidUtilities.dp(9.0f), r0.shareStartY + AndroidUtilities.dp(9.0f));
                        Theme.chat_shareIconDrawable.draw(canvas2);
                    }
                }
                if (r0.currentPosition == null) {
                    drawNamesLayout(canvas);
                }
                if ((r0.drawTime || !r0.mediaBackground) && !r0.forceNotDrawTime) {
                    drawTimeLayout(canvas);
                }
                if (!(r0.controlsAlpha == 1.0f && r0.timeAlpha == 1.0f)) {
                    long newTime2 = System.currentTimeMillis();
                    long dt2 = Math.abs(r0.lastControlsAlphaChangeTime - newTime2);
                    if (dt2 > 17) {
                        dt2 = 17;
                    }
                    r0.totalChangeTime += dt2;
                    if (r0.totalChangeTime > 100) {
                        r0.totalChangeTime = 100;
                    }
                    r0.lastControlsAlphaChangeTime = newTime2;
                    if (r0.controlsAlpha != 1.0f) {
                        r0.controlsAlpha = AndroidUtilities.decelerateInterpolator.getInterpolation(((float) r0.totalChangeTime) / 100.0f);
                    }
                    if (r0.timeAlpha != 1.0f) {
                        r0.timeAlpha = AndroidUtilities.decelerateInterpolator.getInterpolation(((float) r0.totalChangeTime) / 100.0f);
                    }
                    invalidate();
                    if (r0.forceNotDrawTime && r0.currentPosition != null && r0.currentPosition.last && getParent() != null) {
                        ((View) getParent()).invalidate();
                    }
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
        Canvas canvas2 = canvas;
        float f = 11.0f;
        float f2 = 12.0f;
        int i = 0;
        if (this.drawNameLayout && r0.nameLayout != null) {
            canvas.save();
            if (r0.currentMessageObject.type != 13) {
                if (r0.currentMessageObject.type != 5) {
                    if (!r0.mediaBackground) {
                        if (!r0.currentMessageObject.isOutOwner()) {
                            int i2 = r0.backgroundDrawableLeft;
                            float f3 = (r0.mediaBackground || !r0.drawPinnedBottom) ? 17.0f : 11.0f;
                            r0.nameX = ((float) (i2 + AndroidUtilities.dp(f3))) - r0.nameOffsetX;
                            if (r0.currentUser != null) {
                                Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(r0.currentUser.id));
                            } else if (r0.currentChat != null) {
                                Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(0));
                            } else if (ChatObject.isChannel(r0.currentChat) || r0.currentChat.megagroup) {
                                Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(r0.currentChat.id));
                            } else {
                                Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(5));
                            }
                            r0.nameY = (float) AndroidUtilities.dp(r0.drawPinnedTop ? 9.0f : 10.0f);
                            canvas2.translate(r0.nameX, r0.nameY);
                            r0.nameLayout.draw(canvas2);
                            canvas.restore();
                            if (r0.adminLayout != null) {
                                Theme.chat_adminPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_adminSelectedText : Theme.key_chat_adminText));
                                canvas.save();
                                canvas2.translate(((float) ((r0.backgroundDrawableLeft + r0.backgroundDrawableRight) - AndroidUtilities.dp(11.0f))) - r0.adminLayout.getLineWidth(0), r0.nameY + ((float) AndroidUtilities.dp(0.5f)));
                                r0.adminLayout.draw(canvas2);
                                canvas.restore();
                            }
                        }
                    }
                    r0.nameX = ((float) (r0.backgroundDrawableLeft + AndroidUtilities.dp(11.0f))) - r0.nameOffsetX;
                    if (r0.currentUser != null) {
                        Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(r0.currentUser.id));
                    } else if (r0.currentChat != null) {
                        Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(0));
                    } else {
                        if (ChatObject.isChannel(r0.currentChat)) {
                        }
                        Theme.chat_namePaint.setColor(AvatarDrawable.getNameColorForId(r0.currentChat.id));
                    }
                    if (r0.drawPinnedTop) {
                    }
                    r0.nameY = (float) AndroidUtilities.dp(r0.drawPinnedTop ? 9.0f : 10.0f);
                    canvas2.translate(r0.nameX, r0.nameY);
                    r0.nameLayout.draw(canvas2);
                    canvas.restore();
                    if (r0.adminLayout != null) {
                        if (isDrawSelectedBackground()) {
                        }
                        Theme.chat_adminPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_adminSelectedText : Theme.key_chat_adminText));
                        canvas.save();
                        canvas2.translate(((float) ((r0.backgroundDrawableLeft + r0.backgroundDrawableRight) - AndroidUtilities.dp(11.0f))) - r0.adminLayout.getLineWidth(0), r0.nameY + ((float) AndroidUtilities.dp(0.5f)));
                        r0.adminLayout.draw(canvas2);
                        canvas.restore();
                    }
                }
            }
            Theme.chat_namePaint.setColor(Theme.getColor(Theme.key_chat_stickerNameText));
            if (r0.currentMessageObject.isOutOwner()) {
                r0.nameX = (float) AndroidUtilities.dp(28.0f);
            } else {
                r0.nameX = (float) ((r0.backgroundDrawableLeft + r0.backgroundDrawableRight) + AndroidUtilities.dp(22.0f));
            }
            r0.nameY = (float) (r0.layoutHeight - AndroidUtilities.dp(38.0f));
            Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
            Theme.chat_systemDrawable.setBounds(((int) r0.nameX) - AndroidUtilities.dp(12.0f), ((int) r0.nameY) - AndroidUtilities.dp(5.0f), (((int) r0.nameX) + AndroidUtilities.dp(12.0f)) + r0.nameWidth, ((int) r0.nameY) + AndroidUtilities.dp(22.0f));
            Theme.chat_systemDrawable.draw(canvas2);
            canvas2.translate(r0.nameX, r0.nameY);
            r0.nameLayout.draw(canvas2);
            canvas.restore();
            if (r0.adminLayout != null) {
                if (isDrawSelectedBackground()) {
                }
                Theme.chat_adminPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_adminSelectedText : Theme.key_chat_adminText));
                canvas.save();
                canvas2.translate(((float) ((r0.backgroundDrawableLeft + r0.backgroundDrawableRight) - AndroidUtilities.dp(11.0f))) - r0.adminLayout.getLineWidth(0), r0.nameY + ((float) AndroidUtilities.dp(0.5f)));
                r0.adminLayout.draw(canvas2);
                canvas.restore();
            }
        }
        if (r0.drawForwardedName && r0.forwardedNameLayout[0] != null && r0.forwardedNameLayout[1] != null && (r0.currentPosition == null || (r0.currentPosition.minY == (byte) 0 && r0.currentPosition.minX == (byte) 0))) {
            if (r0.currentMessageObject.type == 5) {
                Theme.chat_forwardNamePaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyNameText));
                if (r0.currentMessageObject.isOutOwner()) {
                    r0.forwardNameX = AndroidUtilities.dp(23.0f);
                } else {
                    r0.forwardNameX = (r0.backgroundDrawableLeft + r0.backgroundDrawableRight) + AndroidUtilities.dp(17.0f);
                }
                r0.forwardNameY = AndroidUtilities.dp(12.0f);
                i2 = r0.forwardedNameWidth + AndroidUtilities.dp(14.0f);
                Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
                Theme.chat_systemDrawable.setBounds(r0.forwardNameX - AndroidUtilities.dp(7.0f), r0.forwardNameY - AndroidUtilities.dp(6.0f), (r0.forwardNameX - AndroidUtilities.dp(7.0f)) + i2, r0.forwardNameY + AndroidUtilities.dp(38.0f));
                Theme.chat_systemDrawable.draw(canvas2);
            } else {
                r0.forwardNameY = AndroidUtilities.dp((float) ((r0.drawNameLayout ? 19 : 0) + 10));
                if (r0.currentMessageObject.isOutOwner()) {
                    Theme.chat_forwardNamePaint.setColor(Theme.getColor(Theme.key_chat_outForwardedNameText));
                    r0.forwardNameX = r0.backgroundDrawableLeft + AndroidUtilities.dp(11.0f);
                } else {
                    Theme.chat_forwardNamePaint.setColor(Theme.getColor(Theme.key_chat_inForwardedNameText));
                    if (r0.mediaBackground) {
                        r0.forwardNameX = r0.backgroundDrawableLeft + AndroidUtilities.dp(11.0f);
                    } else {
                        i2 = r0.backgroundDrawableLeft;
                        if (r0.mediaBackground || !r0.drawPinnedBottom) {
                            f = 17.0f;
                        }
                        r0.forwardNameX = i2 + AndroidUtilities.dp(f);
                    }
                }
            }
            for (i2 = 0; i2 < 2; i2++) {
                canvas.save();
                canvas2.translate(((float) r0.forwardNameX) - r0.forwardNameOffsetX[i2], (float) (r0.forwardNameY + (AndroidUtilities.dp(16.0f) * i2)));
                r0.forwardedNameLayout[i2].draw(canvas2);
                canvas.restore();
            }
        }
        if (r0.replyNameLayout != null) {
            float f4;
            if (r0.currentMessageObject.type != 13) {
                if (r0.currentMessageObject.type != 5) {
                    if (r0.currentMessageObject.isOutOwner()) {
                        Theme.chat_replyLinePaint.setColor(Theme.getColor(Theme.key_chat_outReplyLine));
                        Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_outReplyNameText));
                        if (!r0.currentMessageObject.hasValidReplyMessageObject() || r0.currentMessageObject.replyMessageObject.type != 0 || (r0.currentMessageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaGame) || (r0.currentMessageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaInvoice)) {
                            Theme.chat_replyTextPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_outReplyMediaMessageSelectedText : Theme.key_chat_outReplyMediaMessageText));
                        } else {
                            Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_outReplyMessageText));
                        }
                        r0.replyStartX = r0.backgroundDrawableLeft + AndroidUtilities.dp(12.0f);
                    } else {
                        Theme.chat_replyLinePaint.setColor(Theme.getColor(Theme.key_chat_inReplyLine));
                        Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_inReplyNameText));
                        if (!r0.currentMessageObject.hasValidReplyMessageObject() || r0.currentMessageObject.replyMessageObject.type != 0 || (r0.currentMessageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaGame) || (r0.currentMessageObject.replyMessageObject.messageOwner.media instanceof TL_messageMediaInvoice)) {
                            Theme.chat_replyTextPaint.setColor(Theme.getColor(isDrawSelectedBackground() ? Theme.key_chat_inReplyMediaMessageSelectedText : Theme.key_chat_inReplyMediaMessageText));
                        } else {
                            Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_inReplyMessageText));
                        }
                        if (r0.mediaBackground) {
                            r0.replyStartX = r0.backgroundDrawableLeft + AndroidUtilities.dp(12.0f);
                        } else {
                            i2 = r0.backgroundDrawableLeft;
                            if (r0.mediaBackground || !r0.drawPinnedBottom) {
                                f2 = 18.0f;
                            }
                            r0.replyStartX = i2 + AndroidUtilities.dp(f2);
                        }
                    }
                    int i3 = (!r0.drawForwardedName || r0.forwardedNameLayout[0] == null) ? 0 : 36;
                    i2 = 12 + i3;
                    i3 = (!r0.drawNameLayout || r0.nameLayout == null) ? 0 : 20;
                    r0.replyStartY = AndroidUtilities.dp((float) (i2 + i3));
                    if (r0.currentPosition != null || (r0.currentPosition.minY == (byte) 0 && r0.currentPosition.minX == (byte) 0)) {
                        canvas2.drawRect((float) r0.replyStartX, (float) r0.replyStartY, (float) (r0.replyStartX + AndroidUtilities.dp(2.0f)), (float) (r0.replyStartY + AndroidUtilities.dp(35.0f)), Theme.chat_replyLinePaint);
                        if (r0.needReplyImage) {
                            r0.replyImageReceiver.setImageCoords(r0.replyStartX + AndroidUtilities.dp(10.0f), r0.replyStartY, AndroidUtilities.dp(35.0f), AndroidUtilities.dp(35.0f));
                            r0.replyImageReceiver.draw(canvas2);
                        }
                        if (r0.replyNameLayout != null) {
                            canvas.save();
                            canvas2.translate((((float) r0.replyStartX) - r0.replyNameOffset) + ((float) AndroidUtilities.dp((float) ((r0.needReplyImage ? 44 : 0) + 10))), (float) r0.replyStartY);
                            r0.replyNameLayout.draw(canvas2);
                            canvas.restore();
                        }
                        if (r0.replyTextLayout != null) {
                            canvas.save();
                            f4 = ((float) r0.replyStartX) - r0.replyTextOffset;
                            if (r0.needReplyImage) {
                                i = 44;
                            }
                            canvas2.translate(f4 + ((float) AndroidUtilities.dp((float) (10 + i))), (float) (r0.replyStartY + AndroidUtilities.dp(19.0f)));
                            r0.replyTextLayout.draw(canvas2);
                            canvas.restore();
                        }
                    }
                    return;
                }
            }
            Theme.chat_replyLinePaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyLine));
            Theme.chat_replyNamePaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyNameText));
            Theme.chat_replyTextPaint.setColor(Theme.getColor(Theme.key_chat_stickerReplyMessageText));
            if (r0.currentMessageObject.isOutOwner()) {
                r0.replyStartX = AndroidUtilities.dp(23.0f);
            } else {
                r0.replyStartX = (r0.backgroundDrawableLeft + r0.backgroundDrawableRight) + AndroidUtilities.dp(17.0f);
            }
            r0.replyStartY = AndroidUtilities.dp(12.0f);
            i2 = Math.max(r0.replyNameWidth, r0.replyTextWidth) + AndroidUtilities.dp(14.0f);
            Theme.chat_systemDrawable.setColorFilter(Theme.colorFilter);
            Theme.chat_systemDrawable.setBounds(r0.replyStartX - AndroidUtilities.dp(7.0f), r0.replyStartY - AndroidUtilities.dp(6.0f), (r0.replyStartX - AndroidUtilities.dp(7.0f)) + i2, r0.replyStartY + AndroidUtilities.dp(41.0f));
            Theme.chat_systemDrawable.draw(canvas2);
            if (r0.currentPosition != null) {
            }
            canvas2.drawRect((float) r0.replyStartX, (float) r0.replyStartY, (float) (r0.replyStartX + AndroidUtilities.dp(2.0f)), (float) (r0.replyStartY + AndroidUtilities.dp(35.0f)), Theme.chat_replyLinePaint);
            if (r0.needReplyImage) {
                r0.replyImageReceiver.setImageCoords(r0.replyStartX + AndroidUtilities.dp(10.0f), r0.replyStartY, AndroidUtilities.dp(35.0f), AndroidUtilities.dp(35.0f));
                r0.replyImageReceiver.draw(canvas2);
            }
            if (r0.replyNameLayout != null) {
                canvas.save();
                if (r0.needReplyImage) {
                }
                canvas2.translate((((float) r0.replyStartX) - r0.replyNameOffset) + ((float) AndroidUtilities.dp((float) ((r0.needReplyImage ? 44 : 0) + 10))), (float) r0.replyStartY);
                r0.replyNameLayout.draw(canvas2);
                canvas.restore();
            }
            if (r0.replyTextLayout != null) {
                canvas.save();
                f4 = ((float) r0.replyStartX) - r0.replyTextOffset;
                if (r0.needReplyImage) {
                    i = 44;
                }
                canvas2.translate(f4 + ((float) AndroidUtilities.dp((float) (10 + i))), (float) (r0.replyStartY + AndroidUtilities.dp(19.0f)));
                r0.replyTextLayout.draw(canvas2);
                canvas.restore();
            }
        }
    }

    public boolean hasCaptionLayout() {
        return this.captionLayout != null;
    }

    public void drawCaptionLayout(Canvas canvas, boolean selectionOnly) {
        if (this.captionLayout != null) {
            if (!selectionOnly || this.pressedLink != null) {
                canvas.save();
                canvas.translate((float) this.captionX, (float) this.captionY);
                if (this.pressedLink != null) {
                    for (int b = 0; b < this.urlPath.size(); b++) {
                        canvas.drawPath((Path) this.urlPath.get(b), Theme.chat_urlPaint);
                    }
                }
                if (!selectionOnly) {
                    try {
                        this.captionLayout.draw(canvas);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
                canvas.restore();
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
