package org.telegram.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.PhoneFormat.PhoneFormat;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.NotificationsController;
import org.telegram.messenger.SendMessagesHelper;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.exoplayer2.extractor.ts.PsExtractor;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.ReplyMarkup;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonCallback;
import org.telegram.tgnet.TLRPC.TL_keyboardButtonRow;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBar.ActionBarMenuOnItemClick;
import org.telegram.ui.ActionBar.ActionBarMenuItem;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatActivityEnterView;
import org.telegram.ui.Components.ChatActivityEnterView.ChatActivityEnterViewDelegate;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.PlayingGameDrawable;
import org.telegram.ui.Components.PopupAudioView;
import org.telegram.ui.Components.RecordStatusDrawable;
import org.telegram.ui.Components.RoundStatusDrawable;
import org.telegram.ui.Components.SendingFileDrawable;
import org.telegram.ui.Components.SizeNotifierFrameLayout;
import org.telegram.ui.Components.StatusDrawable;
import org.telegram.ui.Components.TypingDotsDrawable;

public class PopupNotificationActivity extends Activity implements NotificationCenterDelegate {
    private static final int id_chat_compose_panel = 1000;
    private ActionBar actionBar;
    private boolean animationInProgress = false;
    private long animationStartTime = 0;
    private ArrayList<ViewGroup> audioViews = new ArrayList();
    private FrameLayout avatarContainer;
    private BackupImageView avatarImageView;
    private ViewGroup centerButtonsView;
    private ViewGroup centerView;
    private ChatActivityEnterView chatActivityEnterView;
    private int classGuid;
    private TextView countText;
    private Chat currentChat;
    private int currentMessageNum = 0;
    private MessageObject currentMessageObject = null;
    private User currentUser;
    private boolean finished = false;
    private ArrayList<ViewGroup> imageViews = new ArrayList();
    private boolean isReply;
    private CharSequence lastPrintString;
    private int lastResumedAccount = -1;
    private ViewGroup leftButtonsView;
    private ViewGroup leftView;
    private ViewGroup messageContainer;
    private float moveStartX = -1.0f;
    private TextView nameTextView;
    private Runnable onAnimationEndRunnable = null;
    private TextView onlineTextView;
    private RelativeLayout popupContainer;
    private ArrayList<MessageObject> popupMessages = new ArrayList();
    private ViewGroup rightButtonsView;
    private ViewGroup rightView;
    private boolean startedMoving = false;
    private StatusDrawable[] statusDrawables = new StatusDrawable[5];
    private ArrayList<ViewGroup> textViews = new ArrayList();
    private VelocityTracker velocityTracker = null;
    private WakeLock wakeLock = null;

    private class FrameLayoutTouch extends FrameLayout {
        public FrameLayoutTouch(Context context) {
            super(context);
        }

        public FrameLayoutTouch(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public FrameLayoutTouch(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public boolean onInterceptTouchEvent(MotionEvent ev) {
            if (!PopupNotificationActivity.this.checkTransitionAnimation()) {
                if (!((PopupNotificationActivity) getContext()).onTouchEventMy(ev)) {
                    return false;
                }
            }
            return true;
        }

        public boolean onTouchEvent(MotionEvent ev) {
            if (!PopupNotificationActivity.this.checkTransitionAnimation()) {
                if (!((PopupNotificationActivity) getContext()).onTouchEventMy(ev)) {
                    return false;
                }
            }
            return true;
        }

        public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            ((PopupNotificationActivity) getContext()).onTouchEventMy(null);
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    private android.view.ViewGroup getViewForMessage(int r1, boolean r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.PopupNotificationActivity.getViewForMessage(int, boolean):android.view.ViewGroup
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
        r0 = r24;
        r1 = r25;
        r2 = r0.popupMessages;
        r2 = r2.size();
        r3 = 0;
        r4 = 1;
        if (r2 != r4) goto L_0x0019;
    L_0x000e:
        if (r1 < 0) goto L_0x0018;
    L_0x0010:
        r2 = r0.popupMessages;
        r2 = r2.size();
        if (r1 < r2) goto L_0x0019;
    L_0x0018:
        return r3;
    L_0x0019:
        r2 = -1;
        if (r1 != r2) goto L_0x0025;
    L_0x001c:
        r5 = r0.popupMessages;
        r5 = r5.size();
        r1 = r5 + -1;
    L_0x0024:
        goto L_0x002f;
    L_0x0025:
        r5 = r0.popupMessages;
        r5 = r5.size();
        if (r1 != r5) goto L_0x002f;
    L_0x002d:
        r1 = 0;
        goto L_0x0024;
    L_0x002f:
        r5 = r0.popupMessages;
        r5 = r5.get(r1);
        r5 = (org.telegram.messenger.MessageObject) r5;
        r6 = r5.type;
        r7 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r8 = 3;
        r9 = -2;
        r10 = 4;
        r11 = 17;
        r12 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r13 = 2;
        r14 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r15 = 0;
        if (r6 == r4) goto L_0x0195;
    L_0x0048:
        r6 = r5.type;
        if (r6 != r10) goto L_0x004e;
    L_0x004c:
        goto L_0x0195;
    L_0x004e:
        r3 = r5.type;
        if (r3 != r13) goto L_0x00ec;
    L_0x0052:
        r3 = r0.audioViews;
        r3 = r3.size();
        r6 = 300; // 0x12c float:4.2E-43 double:1.48E-321;
        if (r3 <= 0) goto L_0x0074;
    L_0x005c:
        r3 = r0.audioViews;
        r3 = r3.get(r15);
        r3 = (android.view.ViewGroup) r3;
        r7 = r0.audioViews;
        r7.remove(r15);
        r6 = java.lang.Integer.valueOf(r6);
        r6 = r3.findViewWithTag(r6);
        r6 = (org.telegram.ui.Components.PopupAudioView) r6;
        goto L_0x00d8;
    L_0x0074:
        r3 = new android.widget.FrameLayout;
        r3.<init>(r0);
        r7 = new android.widget.FrameLayout;
        r7.<init>(r0);
        r9 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r10 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r11 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r13 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r7.setPadding(r9, r10, r11, r13);
        r9 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r15);
        r7.setBackgroundDrawable(r9);
        r9 = org.telegram.ui.Components.LayoutHelper.createFrame(r2, r12);
        r3.addView(r7, r9);
        r9 = new android.widget.FrameLayout;
        r9.<init>(r0);
        r16 = -1;
        r17 = -1073741824; // 0xffffffffc0000000 float:-2.0 double:NaN;
        r18 = 17;
        r19 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r20 = 0;
        r21 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r22 = 0;
        r10 = org.telegram.ui.Components.LayoutHelper.createFrame(r16, r17, r18, r19, r20, r21, r22);
        r7.addView(r9, r10);
        r10 = new org.telegram.ui.Components.PopupAudioView;
        r10.<init>(r0);
        r6 = java.lang.Integer.valueOf(r6);
        r10.setTag(r6);
        r9.addView(r10);
        r6 = java.lang.Integer.valueOf(r8);
        r3.setTag(r6);
        r6 = new org.telegram.ui.PopupNotificationActivity$13;
        r6.<init>();
        r3.setOnClickListener(r6);
        r6 = r10;
    L_0x00d8:
        r6.setMessageObject(r5);
        r7 = r5.currentAccount;
        r7 = org.telegram.messenger.DownloadController.getInstance(r7);
        r7 = r7.canDownloadMedia(r5);
        if (r7 == 0) goto L_0x00ea;
    L_0x00e7:
        r6.downloadAudioIfNeed();
    L_0x00ea:
        goto L_0x0191;
    L_0x00ec:
        r3 = r0.textViews;
        r3 = r3.size();
        r6 = 301; // 0x12d float:4.22E-43 double:1.487E-321;
        if (r3 <= 0) goto L_0x0104;
    L_0x00f6:
        r3 = r0.textViews;
        r3 = r3.get(r15);
        r3 = (android.view.ViewGroup) r3;
        r7 = r0.textViews;
        r7.remove(r15);
        goto L_0x017b;
    L_0x0104:
        r3 = new android.widget.FrameLayout;
        r3.<init>(r0);
        r8 = new android.widget.ScrollView;
        r8.<init>(r0);
        r8.setFillViewport(r4);
        r10 = org.telegram.ui.Components.LayoutHelper.createFrame(r2, r12);
        r3.addView(r8, r10);
        r10 = new android.widget.LinearLayout;
        r10.<init>(r0);
        r10.setOrientation(r15);
        r12 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r15);
        r10.setBackgroundDrawable(r12);
        r12 = org.telegram.ui.Components.LayoutHelper.createScroll(r2, r9, r4);
        r8.addView(r10, r12);
        r12 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r15 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r13 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r14 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r10.setPadding(r12, r15, r13, r14);
        r12 = new org.telegram.ui.PopupNotificationActivity$14;
        r12.<init>();
        r10.setOnClickListener(r12);
        r12 = new android.widget.TextView;
        r12.<init>(r0);
        r12.setTextSize(r4, r7);
        r7 = java.lang.Integer.valueOf(r6);
        r12.setTag(r7);
        r7 = "windowBackgroundWhiteBlackText";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r12.setTextColor(r7);
        r7 = "windowBackgroundWhiteBlackText";
        r7 = org.telegram.ui.ActionBar.Theme.getColor(r7);
        r12.setLinkTextColor(r7);
        r12.setGravity(r11);
        r7 = org.telegram.ui.Components.LayoutHelper.createLinear(r2, r9, r11);
        r10.addView(r12, r7);
        r7 = java.lang.Integer.valueOf(r4);
        r3.setTag(r7);
    L_0x017b:
        r6 = java.lang.Integer.valueOf(r6);
        r6 = r3.findViewWithTag(r6);
        r6 = (android.widget.TextView) r6;
        r7 = org.telegram.messenger.SharedConfig.fontSize;
        r7 = (float) r7;
        r8 = 2;
        r6.setTextSize(r8, r7);
        r7 = r5.messageText;
        r6.setText(r7);
    L_0x0191:
        r23 = r5;
        goto L_0x0315;
    L_0x0195:
        r6 = r0.imageViews;
        r6 = r6.size();
        if (r6 <= 0) goto L_0x01ac;
    L_0x019d:
        r6 = r0.imageViews;
        r7 = 0;
        r6 = r6.get(r7);
        r6 = (android.view.ViewGroup) r6;
        r9 = r0.imageViews;
        r9.remove(r7);
        goto L_0x0221;
    L_0x01ac:
        r6 = new android.widget.FrameLayout;
        r6.<init>(r0);
        r13 = new android.widget.FrameLayout;
        r13.<init>(r0);
        r15 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r8 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r10 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r14 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r13.setPadding(r15, r8, r10, r14);
        r8 = 0;
        r10 = org.telegram.ui.ActionBar.Theme.getSelectorDrawable(r8);
        r13.setBackgroundDrawable(r10);
        r8 = org.telegram.ui.Components.LayoutHelper.createFrame(r2, r12);
        r6.addView(r13, r8);
        r8 = new org.telegram.ui.Components.BackupImageView;
        r8.<init>(r0);
        r10 = 311; // 0x137 float:4.36E-43 double:1.537E-321;
        r10 = java.lang.Integer.valueOf(r10);
        r8.setTag(r10);
        r10 = org.telegram.ui.Components.LayoutHelper.createFrame(r2, r12);
        r13.addView(r8, r10);
        r10 = new android.widget.TextView;
        r10.<init>(r0);
        r12 = "windowBackgroundWhiteBlackText";
        r12 = org.telegram.ui.ActionBar.Theme.getColor(r12);
        r10.setTextColor(r12);
        r10.setTextSize(r4, r7);
        r10.setGravity(r11);
        r7 = 312; // 0x138 float:4.37E-43 double:1.54E-321;
        r7 = java.lang.Integer.valueOf(r7);
        r10.setTag(r7);
        r7 = org.telegram.ui.Components.LayoutHelper.createFrame(r2, r9, r11);
        r13.addView(r10, r7);
        r7 = 2;
        r9 = java.lang.Integer.valueOf(r7);
        r6.setTag(r9);
        r7 = new org.telegram.ui.PopupNotificationActivity$12;
        r7.<init>();
        r6.setOnClickListener(r7);
    L_0x0221:
        r7 = 312; // 0x138 float:4.37E-43 double:1.54E-321;
        r7 = java.lang.Integer.valueOf(r7);
        r7 = r6.findViewWithTag(r7);
        r7 = (android.widget.TextView) r7;
        r8 = 311; // 0x137 float:4.36E-43 double:1.537E-321;
        r8 = java.lang.Integer.valueOf(r8);
        r8 = r6.findViewWithTag(r8);
        r8 = (org.telegram.ui.Components.BackupImageView) r8;
        r8.setAspectFit(r4);
        r9 = r5.type;
        r10 = 8;
        if (r9 != r4) goto L_0x02b1;
    L_0x0242:
        r9 = r5.photoThumbs;
        r11 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r9 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r9, r11);
        r11 = r5.photoThumbs;
        r12 = 100;
        r11 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r11, r12);
        r12 = 0;
        if (r9 == 0) goto L_0x0290;
    L_0x0257:
        r13 = 1;
        r14 = r5.type;
        if (r14 != r4) goto L_0x0269;
    L_0x025c:
        r14 = r5.messageOwner;
        r14 = org.telegram.messenger.FileLoader.getPathToMessage(r14);
        r15 = r14.exists();
        if (r15 != 0) goto L_0x0269;
    L_0x0268:
        r13 = 0;
    L_0x0269:
        if (r13 != 0) goto L_0x0284;
    L_0x026b:
        r14 = r5.currentAccount;
        r14 = org.telegram.messenger.DownloadController.getInstance(r14);
        r14 = r14.canDownloadMedia(r5);
        if (r14 == 0) goto L_0x0278;
    L_0x0277:
        goto L_0x0284;
    L_0x0278:
        if (r11 == 0) goto L_0x0290;
    L_0x027a:
        r14 = r11.location;
        r15 = r3;
        r15 = (android.graphics.drawable.Drawable) r15;
        r8.setImage(r14, r3, r15);
        r12 = 1;
        goto L_0x0290;
    L_0x0284:
        r3 = r9.location;
        r14 = "100_100";
        r15 = r11.location;
        r2 = r9.size;
        r8.setImage(r3, r14, r15, r2);
        r12 = 1;
    L_0x0290:
        if (r12 != 0) goto L_0x02a6;
    L_0x0292:
        r8.setVisibility(r10);
        r2 = 0;
        r7.setVisibility(r2);
        r3 = org.telegram.messenger.SharedConfig.fontSize;
        r3 = (float) r3;
        r10 = 2;
        r7.setTextSize(r10, r3);
        r3 = r5.messageText;
        r7.setText(r3);
        goto L_0x02ad;
    L_0x02a6:
        r2 = 0;
        r8.setVisibility(r2);
        r7.setVisibility(r10);
        r23 = r5;
        goto L_0x0314;
    L_0x02b1:
        r2 = r5.type;
        r9 = 4;
        if (r2 != r9) goto L_0x0312;
        r7.setVisibility(r10);
        r2 = r5.messageText;
        r7.setText(r2);
        r2 = 0;
        r8.setVisibility(r2);
        r2 = r5.messageOwner;
        r2 = r2.media;
        r2 = r2.geo;
        r9 = r2.lat;
        r2 = r5.messageOwner;
        r2 = r2.media;
        r2 = r2.geo;
        r11 = r2._long;
        r2 = java.util.Locale.US;
        r13 = "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=13&size=100x100&maptype=roadmap&scale=%d&markers=color:red|size:big|%f,%f&sensor=false";
        r14 = 5;
        r14 = new java.lang.Object[r14];
        r15 = java.lang.Double.valueOf(r9);
        r16 = 0;
        r14[r16] = r15;
        r15 = java.lang.Double.valueOf(r11);
        r14[r4] = r15;
        r15 = org.telegram.messenger.AndroidUtilities.density;
        r23 = r5;
        r4 = (double) r15;
        r4 = java.lang.Math.ceil(r4);
        r4 = (int) r4;
        r5 = 2;
        r4 = java.lang.Math.min(r5, r4);
        r4 = java.lang.Integer.valueOf(r4);
        r14[r5] = r4;
        r4 = java.lang.Double.valueOf(r9);
        r5 = 3;
        r14[r5] = r4;
        r4 = java.lang.Double.valueOf(r11);
        r5 = 4;
        r14[r5] = r4;
        r2 = java.lang.String.format(r2, r13, r14);
        r8.setImage(r2, r3, r3);
        goto L_0x0314;
        r23 = r5;
        r3 = r6;
    L_0x0315:
        r2 = r3.getParent();
        if (r2 != 0) goto L_0x0320;
        r2 = r0.messageContainer;
        r2.addView(r3);
        r2 = 0;
        r3.setVisibility(r2);
        if (r26 == 0) goto L_0x0364;
        r4 = org.telegram.messenger.AndroidUtilities.displaySize;
        r4 = r4.x;
        r5 = 1103101952; // 0x41c00000 float:24.0 double:5.450047783E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = r3.getLayoutParams();
        r5 = (android.widget.FrameLayout.LayoutParams) r5;
        r6 = 51;
        r5.gravity = r6;
        r6 = -1;
        r5.height = r6;
        r5.width = r4;
        r6 = r0.currentMessageNum;
        if (r1 != r6) goto L_0x0349;
        r6 = 0;
        r3.setTranslationX(r6);
        goto L_0x035e;
        r6 = r0.currentMessageNum;
        r7 = 1;
        r6 = r6 - r7;
        if (r1 != r6) goto L_0x0355;
        r6 = -r4;
        r6 = (float) r6;
        r3.setTranslationX(r6);
        goto L_0x035e;
        r6 = r0.currentMessageNum;
        r6 = r6 + r7;
        if (r1 != r6) goto L_0x035e;
        r6 = (float) r4;
        r3.setTranslationX(r6);
        r3.setLayoutParams(r5);
        r3.invalidate();
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PopupNotificationActivity.getViewForMessage(int, boolean):android.view.ViewGroup");
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Theme.createChatResources(this, false);
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            AndroidUtilities.statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        for (int a = 0; a < 3; a++) {
            NotificationCenter.getInstance(a).addObserver(r0, NotificationCenter.appDidLogout);
            NotificationCenter.getInstance(a).addObserver(r0, NotificationCenter.updateInterfaces);
            NotificationCenter.getInstance(a).addObserver(r0, NotificationCenter.messagePlayingProgressDidChanged);
            NotificationCenter.getInstance(a).addObserver(r0, NotificationCenter.messagePlayingDidReset);
            NotificationCenter.getInstance(a).addObserver(r0, NotificationCenter.contactsDidLoaded);
        }
        NotificationCenter.getGlobalInstance().addObserver(r0, NotificationCenter.pushMessagesUpdated);
        NotificationCenter.getGlobalInstance().addObserver(r0, NotificationCenter.emojiDidLoaded);
        r0.classGuid = ConnectionsManager.generateClassGuid();
        r0.statusDrawables[0] = new TypingDotsDrawable();
        r0.statusDrawables[1] = new RecordStatusDrawable();
        r0.statusDrawables[2] = new SendingFileDrawable();
        r0.statusDrawables[3] = new PlayingGameDrawable();
        r0.statusDrawables[4] = new RoundStatusDrawable();
        SizeNotifierFrameLayout contentView = new SizeNotifierFrameLayout(r0) {
            protected void onLayout(boolean r1, int r2, int r3, int r4, int r5) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.PopupNotificationActivity.1.onLayout(boolean, int, int, int, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
                /*
                r0 = this;
                r0 = r18;
                r4 = r18.getChildCount();
                r5 = r18.getKeyboardHeight();
                r6 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
                r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
                r7 = 0;
                if (r5 > r6) goto L_0x001e;
            L_0x0013:
                r5 = org.telegram.ui.PopupNotificationActivity.this;
                r5 = r5.chatActivityEnterView;
                r5 = r5.getEmojiPadding();
                goto L_0x001f;
            L_0x001e:
                r5 = r7;
                r6 = r7;
                if (r6 >= r4) goto L_0x010c;
                r7 = r0.getChildAt(r6);
                r8 = r7.getVisibility();
                r9 = 8;
                if (r8 != r9) goto L_0x0031;
                goto L_0x0108;
                r8 = r7.getLayoutParams();
                r8 = (android.widget.FrameLayout.LayoutParams) r8;
                r9 = r7.getMeasuredWidth();
                r10 = r7.getMeasuredHeight();
                r11 = r8.gravity;
                r12 = -1;
                if (r11 != r12) goto L_0x0046;
                r11 = 51;
                r12 = r11 & 7;
                r13 = r11 & 112;
                r14 = r12 & 7;
                r15 = 1;
                if (r14 == r15) goto L_0x005c;
                r15 = 5;
                if (r14 == r15) goto L_0x0056;
                r14 = r8.leftMargin;
                r2 = r14;
                goto L_0x0069;
                r14 = r22 - r9;
                r15 = r8.rightMargin;
                r14 = r14 - r15;
                goto L_0x0054;
                r15 = r22 - r20;
                r15 = r15 - r9;
                r15 = r15 / 2;
                r2 = r8.leftMargin;
                r15 = r15 + r2;
                r2 = r8.rightMargin;
                r2 = r15 - r2;
                r15 = 16;
                if (r13 == r15) goto L_0x008b;
                r15 = 48;
                if (r13 == r15) goto L_0x0086;
                r15 = 80;
                if (r13 == r15) goto L_0x007b;
                r15 = r8.topMargin;
                r16 = r2;
                goto L_0x009c;
                r15 = r23 - r5;
                r15 = r15 - r21;
                r15 = r15 - r10;
                r16 = r2;
                r2 = r8.bottomMargin;
                r15 = r15 - r2;
                goto L_0x009c;
                r16 = r2;
                r15 = r8.topMargin;
                goto L_0x009c;
                r16 = r2;
                r2 = r23 - r5;
                r2 = r2 - r21;
                r2 = r2 - r10;
                r2 = r2 / 2;
                r15 = r8.topMargin;
                r2 = r2 + r15;
                r15 = r8.bottomMargin;
                r15 = r2 - r15;
                r2 = r15;
                r15 = org.telegram.ui.PopupNotificationActivity.this;
                r15 = r15.chatActivityEnterView;
                r15 = r15.isPopupView(r7);
                if (r15 == 0) goto L_0x00b7;
                if (r5 == 0) goto L_0x00b1;
                r15 = r18.getMeasuredHeight();
                r15 = r15 - r5;
                goto L_0x00b5;
                r15 = r18.getMeasuredHeight();
                r2 = r15;
                goto L_0x00ff;
                r15 = org.telegram.ui.PopupNotificationActivity.this;
                r15 = r15.chatActivityEnterView;
                r15 = r15.isRecordCircle(r7);
                if (r15 == 0) goto L_0x00ff;
                r15 = org.telegram.ui.PopupNotificationActivity.this;
                r15 = r15.popupContainer;
                r15 = r15.getTop();
                r1 = org.telegram.ui.PopupNotificationActivity.this;
                r1 = r1.popupContainer;
                r1 = r1.getMeasuredHeight();
                r15 = r15 + r1;
                r1 = r7.getMeasuredHeight();
                r15 = r15 - r1;
                r1 = r8.bottomMargin;
                r2 = r15 - r1;
                r1 = org.telegram.ui.PopupNotificationActivity.this;
                r1 = r1.popupContainer;
                r1 = r1.getLeft();
                r15 = org.telegram.ui.PopupNotificationActivity.this;
                r15 = r15.popupContainer;
                r15 = r15.getMeasuredWidth();
                r1 = r1 + r15;
                r15 = r7.getMeasuredWidth();
                r1 = r1 - r15;
                r15 = r8.rightMargin;
                r1 = r1 - r15;
                goto L_0x0101;
                r1 = r16;
                r15 = r1 + r9;
                r3 = r2 + r10;
                r7.layout(r1, r2, r15, r3);
                r7 = r6 + 1;
                goto L_0x0020;
                r18.notifyHeightChanged();
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.PopupNotificationActivity.1.onLayout(boolean, int, int, int, int):void");
            }

            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                int widthMode = MeasureSpec.getMode(widthMeasureSpec);
                int heightMode = MeasureSpec.getMode(heightMeasureSpec);
                int widthSize = MeasureSpec.getSize(widthMeasureSpec);
                int heightSize = MeasureSpec.getSize(heightMeasureSpec);
                setMeasuredDimension(widthSize, heightSize);
                if (getKeyboardHeight() <= AndroidUtilities.dp(20.0f)) {
                    heightSize -= PopupNotificationActivity.this.chatActivityEnterView.getEmojiPadding();
                }
                int heightSize2 = heightSize;
                int childCount = getChildCount();
                heightSize = 0;
                while (true) {
                    int i = heightSize;
                    if (i < childCount) {
                        View child = getChildAt(i);
                        if (child.getVisibility() != 8) {
                            if (PopupNotificationActivity.this.chatActivityEnterView.isPopupView(child)) {
                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, 1073741824));
                            } else if (PopupNotificationActivity.this.chatActivityEnterView.isRecordCircle(child)) {
                                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                            } else {
                                child.measure(MeasureSpec.makeMeasureSpec(widthSize, 1073741824), MeasureSpec.makeMeasureSpec(Math.max(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(2.0f) + heightSize2), 1073741824));
                            }
                        }
                        heightSize = i + 1;
                    } else {
                        return;
                    }
                }
            }
        };
        setContentView(contentView);
        contentView.setBackgroundColor(-1728053248);
        RelativeLayout relativeLayout = new RelativeLayout(r0);
        contentView.addView(relativeLayout, LayoutHelper.createFrame(-1, -1.0f));
        r0.popupContainer = new RelativeLayout(r0) {
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                int w = PopupNotificationActivity.this.chatActivityEnterView.getMeasuredWidth();
                int h = PopupNotificationActivity.this.chatActivityEnterView.getMeasuredHeight();
                for (int a = 0; a < getChildCount(); a++) {
                    View v = getChildAt(a);
                    if (v.getTag() instanceof String) {
                        v.measure(MeasureSpec.makeMeasureSpec(w, 1073741824), MeasureSpec.makeMeasureSpec(h - AndroidUtilities.dp(3.0f), 1073741824));
                    }
                }
            }

            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                super.onLayout(changed, l, t, r, b);
                for (int a = 0; a < getChildCount(); a++) {
                    View v = getChildAt(a);
                    if (v.getTag() instanceof String) {
                        v.layout(v.getLeft(), PopupNotificationActivity.this.chatActivityEnterView.getTop() + AndroidUtilities.dp(3.0f), v.getRight(), PopupNotificationActivity.this.chatActivityEnterView.getBottom());
                    }
                }
            }
        };
        r0.popupContainer.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        relativeLayout.addView(r0.popupContainer, LayoutHelper.createRelative(-1, PsExtractor.VIDEO_STREAM_MASK, 12, 0, 12, 0, 13));
        if (r0.chatActivityEnterView != null) {
            r0.chatActivityEnterView.onDestroy();
        }
        r0.chatActivityEnterView = new ChatActivityEnterView(r0, contentView, null, false);
        r0.chatActivityEnterView.setId(id_chat_compose_panel);
        r0.popupContainer.addView(r0.chatActivityEnterView, LayoutHelper.createRelative(-1, -2, 12));
        r0.chatActivityEnterView.setDelegate(new ChatActivityEnterViewDelegate() {
            public void onMessageSend(CharSequence message) {
                if (PopupNotificationActivity.this.currentMessageObject != null) {
                    if (PopupNotificationActivity.this.currentMessageNum >= 0 && PopupNotificationActivity.this.currentMessageNum < PopupNotificationActivity.this.popupMessages.size()) {
                        PopupNotificationActivity.this.popupMessages.remove(PopupNotificationActivity.this.currentMessageNum);
                    }
                    MessagesController.getInstance(PopupNotificationActivity.this.currentMessageObject.currentAccount).markDialogAsRead(PopupNotificationActivity.this.currentMessageObject.getDialogId(), PopupNotificationActivity.this.currentMessageObject.getId(), Math.max(0, PopupNotificationActivity.this.currentMessageObject.getId()), PopupNotificationActivity.this.currentMessageObject.messageOwner.date, true, 0, true);
                    PopupNotificationActivity.this.currentMessageObject = null;
                    PopupNotificationActivity.this.getNewMessage();
                }
            }

            public void onTextChanged(CharSequence text, boolean big) {
            }

            public void onStickersExpandedChange() {
            }

            public void onSwitchRecordMode(boolean video) {
            }

            public void onPreAudioVideoRecord() {
            }

            public void onMessageEditEnd(boolean loading) {
            }

            public void needSendTyping() {
                if (PopupNotificationActivity.this.currentMessageObject != null) {
                    MessagesController.getInstance(PopupNotificationActivity.this.currentMessageObject.currentAccount).sendTyping(PopupNotificationActivity.this.currentMessageObject.getDialogId(), 0, PopupNotificationActivity.this.classGuid);
                }
            }

            public void onAttachButtonHidden() {
            }

            public void onAttachButtonShow() {
            }

            public void onWindowSizeChanged(int size) {
            }

            public void onStickersTab(boolean opened) {
            }

            public void didPressedAttachButton() {
            }

            public void needStartRecordVideo(int state) {
            }

            public void needStartRecordAudio(int state) {
            }

            public void needChangeVideoPreviewState(int state, float seekProgress) {
            }

            public void needShowMediaBanHint() {
            }
        });
        r0.messageContainer = new FrameLayoutTouch(r0);
        r0.popupContainer.addView(r0.messageContainer, 0);
        r0.actionBar = new ActionBar(r0);
        r0.actionBar.setOccupyStatusBar(false);
        r0.actionBar.setBackButtonImage(R.drawable.ic_close_white);
        r0.actionBar.setBackgroundColor(Theme.getColor(Theme.key_actionBarDefault));
        r0.actionBar.setItemsBackgroundColor(Theme.getColor(Theme.key_actionBarDefaultSelector), false);
        r0.popupContainer.addView(r0.actionBar);
        LayoutParams layoutParams = r0.actionBar.getLayoutParams();
        layoutParams.width = -1;
        r0.actionBar.setLayoutParams(layoutParams);
        ActionBarMenuItem view = r0.actionBar.createMenu().addItemWithWidth(2, 0, AndroidUtilities.dp(56.0f));
        r0.countText = new TextView(r0);
        r0.countText.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubtitle));
        r0.countText.setTextSize(1, 14.0f);
        r0.countText.setGravity(17);
        view.addView(r0.countText, LayoutHelper.createFrame(56, -1.0f));
        r0.avatarContainer = new FrameLayout(r0);
        r0.avatarContainer.setPadding(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
        r0.actionBar.addView(r0.avatarContainer);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) r0.avatarContainer.getLayoutParams();
        layoutParams2.height = -1;
        layoutParams2.width = -2;
        layoutParams2.rightMargin = AndroidUtilities.dp(48.0f);
        layoutParams2.leftMargin = AndroidUtilities.dp(60.0f);
        layoutParams2.gravity = 51;
        r0.avatarContainer.setLayoutParams(layoutParams2);
        r0.avatarImageView = new BackupImageView(r0);
        r0.avatarImageView.setRoundRadius(AndroidUtilities.dp(21.0f));
        r0.avatarContainer.addView(r0.avatarImageView);
        layoutParams2 = (FrameLayout.LayoutParams) r0.avatarImageView.getLayoutParams();
        layoutParams2.width = AndroidUtilities.dp(42.0f);
        layoutParams2.height = AndroidUtilities.dp(42.0f);
        layoutParams2.topMargin = AndroidUtilities.dp(3.0f);
        r0.avatarImageView.setLayoutParams(layoutParams2);
        r0.nameTextView = new TextView(r0);
        r0.nameTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
        r0.nameTextView.setTextSize(1, 18.0f);
        r0.nameTextView.setLines(1);
        r0.nameTextView.setMaxLines(1);
        r0.nameTextView.setSingleLine(true);
        r0.nameTextView.setEllipsize(TruncateAt.END);
        r0.nameTextView.setGravity(3);
        r0.nameTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        r0.avatarContainer.addView(r0.nameTextView);
        layoutParams2 = (FrameLayout.LayoutParams) r0.nameTextView.getLayoutParams();
        layoutParams2.width = -2;
        layoutParams2.height = -2;
        layoutParams2.leftMargin = AndroidUtilities.dp(54.0f);
        layoutParams2.bottomMargin = AndroidUtilities.dp(22.0f);
        layoutParams2.gravity = 80;
        r0.nameTextView.setLayoutParams(layoutParams2);
        r0.onlineTextView = new TextView(r0);
        r0.onlineTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubtitle));
        r0.onlineTextView.setTextSize(1, 14.0f);
        r0.onlineTextView.setLines(1);
        r0.onlineTextView.setMaxLines(1);
        r0.onlineTextView.setSingleLine(true);
        r0.onlineTextView.setEllipsize(TruncateAt.END);
        r0.onlineTextView.setGravity(3);
        r0.avatarContainer.addView(r0.onlineTextView);
        FrameLayout.LayoutParams layoutParams22 = (FrameLayout.LayoutParams) r0.onlineTextView.getLayoutParams();
        layoutParams22.width = -2;
        layoutParams22.height = -2;
        layoutParams22.leftMargin = AndroidUtilities.dp(54.0f);
        layoutParams22.bottomMargin = AndroidUtilities.dp(4.0f);
        layoutParams22.gravity = 80;
        r0.onlineTextView.setLayoutParams(layoutParams22);
        r0.actionBar.setActionBarMenuOnItemClick(new ActionBarMenuOnItemClick() {
            public void onItemClick(int id) {
                if (id == -1) {
                    PopupNotificationActivity.this.onFinish();
                    PopupNotificationActivity.this.finish();
                } else if (id == 1) {
                    PopupNotificationActivity.this.openCurrentMessage();
                } else if (id == 2) {
                    PopupNotificationActivity.this.switchToNextMessage();
                }
            }
        });
        r0.wakeLock = ((PowerManager) ApplicationLoader.applicationContext.getSystemService("power")).newWakeLock(268435462, "screen");
        r0.wakeLock.setReferenceCounted(false);
        handleIntent(getIntent());
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        AndroidUtilities.checkDisplaySize(this, newConfig);
        fixLayout();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 3 && grantResults[0] != 0) {
            Builder builder = new Builder((Context) this);
            builder.setTitle(LocaleController.getString("AppName", R.string.AppName));
            builder.setMessage(LocaleController.getString("PermissionNoAudio", R.string.PermissionNoAudio));
            builder.setNegativeButton(LocaleController.getString("PermissionOpenSettings", R.string.PermissionOpenSettings), new OnClickListener() {
                @TargetApi(9)
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("package:");
                        stringBuilder.append(ApplicationLoader.applicationContext.getPackageName());
                        intent.setData(Uri.parse(stringBuilder.toString()));
                        PopupNotificationActivity.this.startActivity(intent);
                    } catch (Throwable e) {
                        FileLog.e(e);
                    }
                }
            });
            builder.setPositiveButton(LocaleController.getString("OK", R.string.OK), null);
            builder.show();
        }
    }

    private void switchToNextMessage() {
        if (this.popupMessages.size() > 1) {
            if (this.currentMessageNum < this.popupMessages.size() - 1) {
                this.currentMessageNum++;
            } else {
                this.currentMessageNum = 0;
            }
            this.currentMessageObject = (MessageObject) this.popupMessages.get(this.currentMessageNum);
            updateInterfaceForCurrentMessage(2);
            this.countText.setText(String.format("%d/%d", new Object[]{Integer.valueOf(this.currentMessageNum + 1), Integer.valueOf(this.popupMessages.size())}));
        }
    }

    private void switchToPreviousMessage() {
        if (this.popupMessages.size() > 1) {
            if (this.currentMessageNum > 0) {
                this.currentMessageNum--;
            } else {
                this.currentMessageNum = this.popupMessages.size() - 1;
            }
            this.currentMessageObject = (MessageObject) this.popupMessages.get(this.currentMessageNum);
            updateInterfaceForCurrentMessage(1);
            this.countText.setText(String.format("%d/%d", new Object[]{Integer.valueOf(this.currentMessageNum + 1), Integer.valueOf(this.popupMessages.size())}));
        }
    }

    public boolean checkTransitionAnimation() {
        if (this.animationInProgress && this.animationStartTime < System.currentTimeMillis() - 400) {
            this.animationInProgress = false;
            if (this.onAnimationEndRunnable != null) {
                this.onAnimationEndRunnable.run();
                this.onAnimationEndRunnable = null;
            }
        }
        return this.animationInProgress;
    }

    public boolean onTouchEventMy(MotionEvent motionEvent) {
        PopupNotificationActivity popupNotificationActivity = this;
        MotionEvent motionEvent2 = motionEvent;
        if (checkTransitionAnimation()) {
            return false;
        }
        if (motionEvent2 != null && motionEvent.getAction() == 0) {
            popupNotificationActivity.moveStartX = motionEvent.getX();
        } else if (motionEvent2 != null && motionEvent.getAction() == 2) {
            float x = motionEvent.getX();
            diff = (int) (x - popupNotificationActivity.moveStartX);
            if (!(popupNotificationActivity.moveStartX == -1.0f || popupNotificationActivity.startedMoving || Math.abs(diff) <= AndroidUtilities.dp(10.0f))) {
                popupNotificationActivity.startedMoving = true;
                popupNotificationActivity.moveStartX = x;
                AndroidUtilities.lockOrientation(this);
                diff = 0;
                if (popupNotificationActivity.velocityTracker == null) {
                    popupNotificationActivity.velocityTracker = VelocityTracker.obtain();
                } else {
                    popupNotificationActivity.velocityTracker.clear();
                }
            }
            if (popupNotificationActivity.startedMoving) {
                if (popupNotificationActivity.leftView == null && diff > 0) {
                    diff = 0;
                }
                if (popupNotificationActivity.rightView == null && diff < 0) {
                    diff = 0;
                }
                if (popupNotificationActivity.velocityTracker != null) {
                    popupNotificationActivity.velocityTracker.addMovement(motionEvent2);
                }
                applyViewsLayoutParams(diff);
            }
        } else if (motionEvent2 == null || motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            if (motionEvent2 == null || !popupNotificationActivity.startedMoving) {
                applyViewsLayoutParams(0);
            } else {
                int diff = (int) (motionEvent.getX() - popupNotificationActivity.moveStartX);
                int width = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
                float moveDiff = 0.0f;
                int forceMove = 0;
                View otherView = null;
                View otherButtonsView = null;
                if (popupNotificationActivity.velocityTracker != null) {
                    popupNotificationActivity.velocityTracker.computeCurrentVelocity(id_chat_compose_panel);
                    if (popupNotificationActivity.velocityTracker.getXVelocity() >= 3500.0f) {
                        forceMove = 1;
                    } else if (popupNotificationActivity.velocityTracker.getXVelocity() <= -3500.0f) {
                        forceMove = 2;
                    }
                }
                if ((forceMove == 1 || diff > width / 3) && popupNotificationActivity.leftView != null) {
                    moveDiff = ((float) width) - popupNotificationActivity.centerView.getTranslationX();
                    otherView = popupNotificationActivity.leftView;
                    otherButtonsView = popupNotificationActivity.leftButtonsView;
                    popupNotificationActivity.onAnimationEndRunnable = new Runnable() {
                        public void run() {
                            PopupNotificationActivity.this.animationInProgress = false;
                            PopupNotificationActivity.this.switchToPreviousMessage();
                            AndroidUtilities.unlockOrientation(PopupNotificationActivity.this);
                        }
                    };
                } else if ((forceMove == 2 || diff < (-width) / 3) && popupNotificationActivity.rightView != null) {
                    moveDiff = ((float) (-width)) - popupNotificationActivity.centerView.getTranslationX();
                    otherView = popupNotificationActivity.rightView;
                    otherButtonsView = popupNotificationActivity.rightButtonsView;
                    popupNotificationActivity.onAnimationEndRunnable = new Runnable() {
                        public void run() {
                            PopupNotificationActivity.this.animationInProgress = false;
                            PopupNotificationActivity.this.switchToNextMessage();
                            AndroidUtilities.unlockOrientation(PopupNotificationActivity.this);
                        }
                    };
                } else if (popupNotificationActivity.centerView.getTranslationX() != 0.0f) {
                    moveDiff = -popupNotificationActivity.centerView.getTranslationX();
                    otherView = diff > 0 ? popupNotificationActivity.leftView : popupNotificationActivity.rightView;
                    otherButtonsView = diff > 0 ? popupNotificationActivity.leftButtonsView : popupNotificationActivity.rightButtonsView;
                    popupNotificationActivity.onAnimationEndRunnable = new Runnable() {
                        public void run() {
                            PopupNotificationActivity.this.animationInProgress = false;
                            PopupNotificationActivity.this.applyViewsLayoutParams(0);
                            AndroidUtilities.unlockOrientation(PopupNotificationActivity.this);
                        }
                    };
                }
                if (moveDiff != 0.0f) {
                    diff = (int) (Math.abs(moveDiff / ((float) width)) * 1128792064);
                    ArrayList<Animator> animators = new ArrayList();
                    animators.add(ObjectAnimator.ofFloat(popupNotificationActivity.centerView, "translationX", new float[]{popupNotificationActivity.centerView.getTranslationX() + moveDiff}));
                    if (popupNotificationActivity.centerButtonsView != null) {
                        animators.add(ObjectAnimator.ofFloat(popupNotificationActivity.centerButtonsView, "translationX", new float[]{popupNotificationActivity.centerButtonsView.getTranslationX() + moveDiff}));
                    }
                    if (otherView != null) {
                        animators.add(ObjectAnimator.ofFloat(otherView, "translationX", new float[]{otherView.getTranslationX() + moveDiff}));
                    }
                    if (otherButtonsView != null) {
                        animators.add(ObjectAnimator.ofFloat(otherButtonsView, "translationX", new float[]{otherButtonsView.getTranslationX() + moveDiff}));
                    }
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(animators);
                    animatorSet.setDuration((long) diff);
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            if (PopupNotificationActivity.this.onAnimationEndRunnable != null) {
                                PopupNotificationActivity.this.onAnimationEndRunnable.run();
                                PopupNotificationActivity.this.onAnimationEndRunnable = null;
                            }
                        }
                    });
                    animatorSet.start();
                    popupNotificationActivity.animationInProgress = true;
                    popupNotificationActivity.animationStartTime = System.currentTimeMillis();
                }
            }
            if (popupNotificationActivity.velocityTracker != null) {
                popupNotificationActivity.velocityTracker.recycle();
                popupNotificationActivity.velocityTracker = null;
            }
            popupNotificationActivity.startedMoving = false;
            popupNotificationActivity.moveStartX = -1.0f;
        }
        return popupNotificationActivity.startedMoving;
    }

    private void applyViewsLayoutParams(int xOffset) {
        FrameLayout.LayoutParams layoutParams;
        int widht = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
        if (this.leftView != null) {
            layoutParams = (FrameLayout.LayoutParams) this.leftView.getLayoutParams();
            if (layoutParams.width != widht) {
                layoutParams.width = widht;
                this.leftView.setLayoutParams(layoutParams);
            }
            this.leftView.setTranslationX((float) ((-widht) + xOffset));
        }
        if (this.leftButtonsView != null) {
            this.leftButtonsView.setTranslationX((float) ((-widht) + xOffset));
        }
        if (this.centerView != null) {
            layoutParams = (FrameLayout.LayoutParams) this.centerView.getLayoutParams();
            if (layoutParams.width != widht) {
                layoutParams.width = widht;
                this.centerView.setLayoutParams(layoutParams);
            }
            this.centerView.setTranslationX((float) xOffset);
        }
        if (this.centerButtonsView != null) {
            this.centerButtonsView.setTranslationX((float) xOffset);
        }
        if (this.rightView != null) {
            layoutParams = (FrameLayout.LayoutParams) this.rightView.getLayoutParams();
            if (layoutParams.width != widht) {
                layoutParams.width = widht;
                this.rightView.setLayoutParams(layoutParams);
            }
            this.rightView.setTranslationX((float) (widht + xOffset));
        }
        if (this.rightButtonsView != null) {
            this.rightButtonsView.setTranslationX((float) (widht + xOffset));
        }
        this.messageContainer.invalidate();
    }

    private LinearLayout getButtonsViewForMessage(int num, boolean applyOffset) {
        int num2 = num;
        if (this.popupMessages.size() == 1 && (num2 < 0 || num2 >= r0.popupMessages.size())) {
            return null;
        }
        LinearLayout view;
        final MessageObject messageObject;
        int buttonsCount;
        ReplyMarkup markup;
        ArrayList<TL_keyboardButtonRow> rows;
        int size;
        int a;
        TL_keyboardButtonRow row;
        int size2;
        int b;
        final int account;
        ArrayList<TL_keyboardButtonRow> rows2;
        int size3;
        TL_keyboardButtonRow row2;
        int size22;
        KeyboardButton button;
        ReplyMarkup markup2;
        int widht;
        if (num2 == -1) {
            num2 = r0.popupMessages.size() - 1;
        } else {
            if (num2 == r0.popupMessages.size()) {
                num2 = 0;
            }
            view = null;
            messageObject = (MessageObject) r0.popupMessages.get(num2);
            buttonsCount = 0;
            markup = messageObject.messageOwner.reply_markup;
            if (messageObject.getDialogId() == 777000 && markup != null) {
                rows = markup.rows;
                size = rows.size();
                for (a = 0; a < size; a++) {
                    row = (TL_keyboardButtonRow) rows.get(a);
                    size2 = row.buttons.size();
                    for (b = 0; b < size2; b++) {
                        if (((KeyboardButton) row.buttons.get(b)) instanceof TL_keyboardButtonCallback) {
                            buttonsCount++;
                        }
                    }
                }
            }
            account = messageObject.currentAccount;
            if (buttonsCount > 0) {
                rows2 = markup.rows;
                size3 = rows2.size();
                for (size = 0; size < size3; size++) {
                    row2 = (TL_keyboardButtonRow) rows2.get(size);
                    size2 = 0;
                    size22 = row2.buttons.size();
                    while (size2 < size22) {
                        button = (KeyboardButton) row2.buttons.get(size2);
                        if (button instanceof TL_keyboardButtonCallback) {
                            markup2 = markup;
                        } else {
                            if (view == null) {
                                view = new LinearLayout(r0);
                                view.setOrientation(0);
                                view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                                view.setWeightSum(100.0f);
                                view.setTag("b");
                                view.setOnTouchListener(new OnTouchListener() {
                                    public boolean onTouch(View v, MotionEvent event) {
                                        return true;
                                    }
                                });
                            }
                            TextView textView = new TextView(r0);
                            markup2 = markup;
                            textView.setTextSize(1, 16.0f);
                            textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText));
                            textView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                            textView.setText(button.text.toUpperCase());
                            textView.setTag(button);
                            textView.setGravity(17);
                            textView.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                            view.addView(textView, LayoutHelper.createLinear(-1, -1, 100.0f / ((float) buttonsCount)));
                            textView.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    KeyboardButton button = (KeyboardButton) v.getTag();
                                    if (button != null) {
                                        SendMessagesHelper.getInstance(account).sendNotificationCallback(messageObject.getDialogId(), messageObject.getId(), button.data);
                                    }
                                }
                            });
                        }
                        size2++;
                        markup = markup2;
                    }
                }
            }
            if (view != null) {
                widht = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
                layoutParams.addRule(12);
                if (applyOffset) {
                    if (num2 == r0.currentMessageNum) {
                        view.setTranslationX(0.0f);
                    } else if (num2 == r0.currentMessageNum - 1) {
                        view.setTranslationX((float) (-widht));
                    } else if (num2 == r0.currentMessageNum + 1) {
                        view.setTranslationX((float) widht);
                    }
                }
                r0.popupContainer.addView(view, layoutParams);
            }
            return view;
        }
        view = null;
        messageObject = (MessageObject) r0.popupMessages.get(num2);
        buttonsCount = 0;
        markup = messageObject.messageOwner.reply_markup;
        rows = markup.rows;
        size = rows.size();
        for (a = 0; a < size; a++) {
            row = (TL_keyboardButtonRow) rows.get(a);
            size2 = row.buttons.size();
            for (b = 0; b < size2; b++) {
                if (((KeyboardButton) row.buttons.get(b)) instanceof TL_keyboardButtonCallback) {
                    buttonsCount++;
                }
            }
        }
        account = messageObject.currentAccount;
        if (buttonsCount > 0) {
            rows2 = markup.rows;
            size3 = rows2.size();
            for (size = 0; size < size3; size++) {
                row2 = (TL_keyboardButtonRow) rows2.get(size);
                size2 = 0;
                size22 = row2.buttons.size();
                while (size2 < size22) {
                    button = (KeyboardButton) row2.buttons.get(size2);
                    if (button instanceof TL_keyboardButtonCallback) {
                        markup2 = markup;
                    } else {
                        if (view == null) {
                            view = new LinearLayout(r0);
                            view.setOrientation(0);
                            view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                            view.setWeightSum(100.0f);
                            view.setTag("b");
                            view.setOnTouchListener(/* anonymous class already generated */);
                        }
                        TextView textView2 = new TextView(r0);
                        markup2 = markup;
                        textView2.setTextSize(1, 16.0f);
                        textView2.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlueText));
                        textView2.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
                        textView2.setText(button.text.toUpperCase());
                        textView2.setTag(button);
                        textView2.setGravity(17);
                        textView2.setBackgroundDrawable(Theme.getSelectorDrawable(true));
                        view.addView(textView2, LayoutHelper.createLinear(-1, -1, 100.0f / ((float) buttonsCount)));
                        textView2.setOnClickListener(/* anonymous class already generated */);
                    }
                    size2++;
                    markup = markup2;
                }
            }
        }
        if (view != null) {
            widht = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
            RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(-1, -2);
            layoutParams2.addRule(12);
            if (applyOffset) {
                if (num2 == r0.currentMessageNum) {
                    view.setTranslationX(0.0f);
                } else if (num2 == r0.currentMessageNum - 1) {
                    view.setTranslationX((float) (-widht));
                } else if (num2 == r0.currentMessageNum + 1) {
                    view.setTranslationX((float) widht);
                }
            }
            r0.popupContainer.addView(view, layoutParams2);
        }
        return view;
    }

    private void reuseButtonsView(ViewGroup view) {
        if (view != null) {
            this.popupContainer.removeView(view);
        }
    }

    private void reuseView(ViewGroup view) {
        if (view != null) {
            int tag = ((Integer) view.getTag()).intValue();
            view.setVisibility(8);
            if (tag == 1) {
                this.textViews.add(view);
            } else if (tag == 2) {
                this.imageViews.add(view);
            } else if (tag == 3) {
                this.audioViews.add(view);
            }
        }
    }

    private void prepareLayouts(int move) {
        int widht = AndroidUtilities.displaySize.x - AndroidUtilities.dp(24.0f);
        if (move == 0) {
            reuseView(this.centerView);
            reuseView(this.leftView);
            reuseView(this.rightView);
            reuseButtonsView(this.centerButtonsView);
            reuseButtonsView(this.leftButtonsView);
            reuseButtonsView(this.rightButtonsView);
            for (int a = this.currentMessageNum - 1; a < this.currentMessageNum + 2; a++) {
                if (a == this.currentMessageNum - 1) {
                    this.leftView = getViewForMessage(a, true);
                    this.leftButtonsView = getButtonsViewForMessage(a, true);
                } else if (a == this.currentMessageNum) {
                    this.centerView = getViewForMessage(a, true);
                    this.centerButtonsView = getButtonsViewForMessage(a, true);
                } else if (a == this.currentMessageNum + 1) {
                    this.rightView = getViewForMessage(a, true);
                    this.rightButtonsView = getButtonsViewForMessage(a, true);
                }
            }
        } else if (move == 1) {
            reuseView(this.rightView);
            reuseButtonsView(this.rightButtonsView);
            this.rightView = this.centerView;
            this.centerView = this.leftView;
            this.leftView = getViewForMessage(this.currentMessageNum - 1, true);
            this.rightButtonsView = this.centerButtonsView;
            this.centerButtonsView = this.leftButtonsView;
            this.leftButtonsView = getButtonsViewForMessage(this.currentMessageNum - 1, true);
        } else if (move == 2) {
            reuseView(this.leftView);
            reuseButtonsView(this.leftButtonsView);
            this.leftView = this.centerView;
            this.centerView = this.rightView;
            this.rightView = getViewForMessage(this.currentMessageNum + 1, true);
            this.leftButtonsView = this.centerButtonsView;
            this.centerButtonsView = this.rightButtonsView;
            this.rightButtonsView = getButtonsViewForMessage(this.currentMessageNum + 1, true);
        } else if (move == 3) {
            if (this.rightView != null) {
                offset = this.rightView.getTranslationX();
                reuseView(this.rightView);
                ViewGroup viewForMessage = getViewForMessage(this.currentMessageNum + 1, false);
                this.rightView = viewForMessage;
                if (viewForMessage != null) {
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.rightView.getLayoutParams();
                    layoutParams.width = widht;
                    this.rightView.setLayoutParams(layoutParams);
                    this.rightView.setTranslationX(offset);
                    this.rightView.invalidate();
                }
            }
            if (this.rightButtonsView != null) {
                offset = this.rightButtonsView.getTranslationX();
                reuseButtonsView(this.rightButtonsView);
                r2 = getButtonsViewForMessage(this.currentMessageNum + 1, false);
                this.rightButtonsView = r2;
                if (r2 != null) {
                    this.rightButtonsView.setTranslationX(offset);
                }
            }
        } else if (move == 4) {
            if (this.leftView != null) {
                offset = this.leftView.getTranslationX();
                reuseView(this.leftView);
                r2 = getViewForMessage(0, false);
                this.leftView = r2;
                if (r2 != null) {
                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.leftView.getLayoutParams();
                    layoutParams2.width = widht;
                    this.leftView.setLayoutParams(layoutParams2);
                    this.leftView.setTranslationX(offset);
                    this.leftView.invalidate();
                }
            }
            if (this.leftButtonsView != null) {
                offset = this.leftButtonsView.getTranslationX();
                reuseButtonsView(this.leftButtonsView);
                r2 = getButtonsViewForMessage(0, false);
                this.leftButtonsView = r2;
                if (r2 != null) {
                    this.leftButtonsView.setTranslationX(offset);
                }
            }
        }
    }

    private void fixLayout() {
        if (this.avatarContainer != null) {
            this.avatarContainer.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    if (PopupNotificationActivity.this.avatarContainer != null) {
                        PopupNotificationActivity.this.avatarContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                    }
                    int padding = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(48.0f)) / 2;
                    PopupNotificationActivity.this.avatarContainer.setPadding(PopupNotificationActivity.this.avatarContainer.getPaddingLeft(), padding, PopupNotificationActivity.this.avatarContainer.getPaddingRight(), padding);
                    return true;
                }
            });
        }
        if (this.messageContainer != null) {
            this.messageContainer.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
                public boolean onPreDraw() {
                    PopupNotificationActivity.this.messageContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (!(PopupNotificationActivity.this.checkTransitionAnimation() || PopupNotificationActivity.this.startedMoving)) {
                        MarginLayoutParams layoutParams = (MarginLayoutParams) PopupNotificationActivity.this.messageContainer.getLayoutParams();
                        layoutParams.topMargin = ActionBar.getCurrentActionBarHeight();
                        layoutParams.bottomMargin = AndroidUtilities.dp(48.0f);
                        layoutParams.width = -1;
                        layoutParams.height = -1;
                        PopupNotificationActivity.this.messageContainer.setLayoutParams(layoutParams);
                        PopupNotificationActivity.this.applyViewsLayoutParams(0);
                    }
                    return true;
                }
            });
        }
    }

    private void handleIntent(Intent intent) {
        boolean z = intent != null && intent.getBooleanExtra("force", false);
        this.isReply = z;
        this.popupMessages.clear();
        if (this.isReply) {
            this.popupMessages.addAll(NotificationsController.getInstance(intent != null ? intent.getIntExtra("currentAccount", UserConfig.selectedAccount) : UserConfig.selectedAccount).popupReplyMessages);
        } else {
            for (int a = 0; a < 3; a++) {
                if (UserConfig.getInstance(a).isClientActivated()) {
                    this.popupMessages.addAll(NotificationsController.getInstance(a).popupMessages);
                }
            }
        }
        if (!((KeyguardManager) getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
            if (ApplicationLoader.isScreenOn) {
                getWindow().addFlags(2623488);
                getWindow().clearFlags(2);
                if (this.currentMessageObject == null) {
                    this.currentMessageNum = 0;
                }
                getNewMessage();
            }
        }
        getWindow().addFlags(2623490);
        if (this.currentMessageObject == null) {
            this.currentMessageNum = 0;
        }
        getNewMessage();
    }

    private void getNewMessage() {
        if (this.popupMessages.isEmpty()) {
            onFinish();
            finish();
            return;
        }
        boolean found = false;
        if ((this.currentMessageNum != 0 || this.chatActivityEnterView.hasText() || this.startedMoving) && this.currentMessageObject != null) {
            int size = this.popupMessages.size();
            for (int a = 0; a < size; a++) {
                MessageObject messageObject = (MessageObject) this.popupMessages.get(a);
                if (messageObject.currentAccount == this.currentMessageObject.currentAccount && messageObject.getDialogId() == this.currentMessageObject.getDialogId() && messageObject.getId() == this.currentMessageObject.getId()) {
                    this.currentMessageNum = a;
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            this.currentMessageNum = 0;
            this.currentMessageObject = (MessageObject) this.popupMessages.get(0);
            updateInterfaceForCurrentMessage(0);
        } else if (this.startedMoving) {
            if (this.currentMessageNum == this.popupMessages.size() - 1) {
                prepareLayouts(3);
            } else if (this.currentMessageNum == 1) {
                prepareLayouts(4);
            }
        }
        this.countText.setText(String.format("%d/%d", new Object[]{Integer.valueOf(this.currentMessageNum + 1), Integer.valueOf(this.popupMessages.size())}));
    }

    private void openCurrentMessage() {
        if (this.currentMessageObject != null) {
            Intent intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            long dialog_id = this.currentMessageObject.getDialogId();
            if (((int) dialog_id) != 0) {
                int lower_id = (int) dialog_id;
                if (lower_id < 0) {
                    intent.putExtra("chatId", -lower_id);
                } else {
                    intent.putExtra("userId", lower_id);
                }
            } else {
                intent.putExtra("encId", (int) (dialog_id >> 32));
            }
            intent.putExtra("currentAccount", this.currentMessageObject.currentAccount);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("com.tmessages.openchat");
            stringBuilder.append(Math.random());
            stringBuilder.append(ConnectionsManager.DEFAULT_DATACENTER_ID);
            intent.setAction(stringBuilder.toString());
            intent.setFlags(32768);
            startActivity(intent);
            onFinish();
            finish();
        }
    }

    private void updateInterfaceForCurrentMessage(int move) {
        if (this.actionBar != null) {
            if (this.lastResumedAccount != this.currentMessageObject.currentAccount) {
                if (this.lastResumedAccount >= 0) {
                    ConnectionsManager.getInstance(this.lastResumedAccount).setAppPaused(true, false);
                }
                this.lastResumedAccount = this.currentMessageObject.currentAccount;
                ConnectionsManager.getInstance(this.lastResumedAccount).setAppPaused(false, false);
            }
            this.currentChat = null;
            this.currentUser = null;
            long dialog_id = this.currentMessageObject.getDialogId();
            this.chatActivityEnterView.setDialogId(dialog_id, this.currentMessageObject.currentAccount);
            if (((int) dialog_id) != 0) {
                int lower_id = (int) dialog_id;
                if (lower_id > 0) {
                    this.currentUser = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(Integer.valueOf(lower_id));
                } else {
                    this.currentChat = MessagesController.getInstance(this.currentMessageObject.currentAccount).getChat(Integer.valueOf(-lower_id));
                    this.currentUser = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(Integer.valueOf(this.currentMessageObject.messageOwner.from_id));
                }
            } else {
                this.currentUser = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(Integer.valueOf(MessagesController.getInstance(this.currentMessageObject.currentAccount).getEncryptedChat(Integer.valueOf((int) (dialog_id >> 32))).user_id));
            }
            if (this.currentChat != null && this.currentUser != null) {
                this.nameTextView.setText(this.currentChat.title);
                this.onlineTextView.setText(UserObject.getUserName(this.currentUser));
                this.nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                this.nameTextView.setCompoundDrawablePadding(0);
            } else if (this.currentUser != null) {
                this.nameTextView.setText(UserObject.getUserName(this.currentUser));
                if (((int) dialog_id) == 0) {
                    this.nameTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_white, 0, 0, 0);
                    this.nameTextView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                } else {
                    this.nameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    this.nameTextView.setCompoundDrawablePadding(0);
                }
            }
            prepareLayouts(move);
            updateSubtitle();
            checkAndUpdateAvatar();
            applyViewsLayoutParams(0);
        }
    }

    private void updateSubtitle() {
        if (this.actionBar != null) {
            if (this.currentMessageObject != null) {
                if (this.currentChat == null) {
                    if (this.currentUser != null) {
                        if (this.currentUser.id / id_chat_compose_panel == 777 || this.currentUser.id / id_chat_compose_panel == 333 || ContactsController.getInstance(this.currentMessageObject.currentAccount).contactsDict.get(Integer.valueOf(this.currentUser.id)) != null || (ContactsController.getInstance(this.currentMessageObject.currentAccount).contactsDict.size() == 0 && ContactsController.getInstance(this.currentMessageObject.currentAccount).isLoadingContacts())) {
                            this.nameTextView.setText(UserObject.getUserName(this.currentUser));
                        } else if (this.currentUser.phone == null || this.currentUser.phone.length() == 0) {
                            this.nameTextView.setText(UserObject.getUserName(this.currentUser));
                        } else {
                            TextView textView = this.nameTextView;
                            PhoneFormat instance = PhoneFormat.getInstance();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("+");
                            stringBuilder.append(this.currentUser.phone);
                            textView.setText(instance.format(stringBuilder.toString()));
                        }
                        if (this.currentUser == null || this.currentUser.id != 777000) {
                            CharSequence printString = (CharSequence) MessagesController.getInstance(this.currentMessageObject.currentAccount).printingStrings.get(this.currentMessageObject.getDialogId());
                            if (printString != null) {
                                if (printString.length() != 0) {
                                    this.lastPrintString = printString;
                                    this.onlineTextView.setText(printString);
                                    setTypingAnimation(true);
                                }
                            }
                            this.lastPrintString = null;
                            setTypingAnimation(false);
                            User user = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(Integer.valueOf(this.currentUser.id));
                            if (user != null) {
                                this.currentUser = user;
                            }
                            this.onlineTextView.setText(LocaleController.formatUserStatus(this.currentMessageObject.currentAccount, this.currentUser));
                        } else {
                            this.onlineTextView.setText(LocaleController.getString("ServiceNotifications", R.string.ServiceNotifications));
                        }
                    }
                }
            }
        }
    }

    private void checkAndUpdateAvatar() {
        if (this.currentMessageObject != null) {
            TLObject newPhoto = null;
            Drawable avatarDrawable = null;
            if (this.currentChat != null) {
                Chat chat = MessagesController.getInstance(this.currentMessageObject.currentAccount).getChat(Integer.valueOf(this.currentChat.id));
                if (chat != null) {
                    this.currentChat = chat;
                    if (this.currentChat.photo != null) {
                        newPhoto = this.currentChat.photo.photo_small;
                    }
                    avatarDrawable = new AvatarDrawable(this.currentChat);
                } else {
                    return;
                }
            } else if (this.currentUser != null) {
                User user = MessagesController.getInstance(this.currentMessageObject.currentAccount).getUser(Integer.valueOf(this.currentUser.id));
                if (user != null) {
                    this.currentUser = user;
                    if (this.currentUser.photo != null) {
                        newPhoto = this.currentUser.photo.photo_small;
                    }
                    avatarDrawable = new AvatarDrawable(this.currentUser);
                } else {
                    return;
                }
            }
            if (this.avatarImageView != null) {
                this.avatarImageView.setImage(newPhoto, "50_50", avatarDrawable);
            }
        }
    }

    private void setTypingAnimation(boolean start) {
        if (this.actionBar != null) {
            int a = 0;
            if (start) {
                try {
                    Integer type = (Integer) MessagesController.getInstance(this.currentMessageObject.currentAccount).printingStringsTypes.get(this.currentMessageObject.getDialogId());
                    this.onlineTextView.setCompoundDrawablesWithIntrinsicBounds(this.statusDrawables[type.intValue()], null, null, null);
                    this.onlineTextView.setCompoundDrawablePadding(AndroidUtilities.dp(4.0f));
                    while (a < this.statusDrawables.length) {
                        if (a == type.intValue()) {
                            this.statusDrawables[a].start();
                        } else {
                            this.statusDrawables[a].stop();
                        }
                        a++;
                    }
                } catch (Throwable a2) {
                    FileLog.e(a2);
                }
            } else {
                this.onlineTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                this.onlineTextView.setCompoundDrawablePadding(0);
                while (a < this.statusDrawables.length) {
                    this.statusDrawables[a].stop();
                    a++;
                }
            }
        }
    }

    public void onBackPressed() {
        if (this.chatActivityEnterView.isPopupShowing()) {
            this.chatActivityEnterView.hidePopup(true);
        } else {
            super.onBackPressed();
        }
    }

    protected void onResume() {
        super.onResume();
        MediaController.getInstance().setFeedbackView(this.chatActivityEnterView, true);
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.setFieldFocused(true);
        }
        fixLayout();
        checkAndUpdateAvatar();
        this.wakeLock.acquire(7000);
    }

    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
        if (this.chatActivityEnterView != null) {
            this.chatActivityEnterView.hidePopup(false);
            this.chatActivityEnterView.setFieldFocused(false);
        }
        if (this.lastResumedAccount >= 0) {
            ConnectionsManager.getInstance(this.lastResumedAccount).setAppPaused(true, false);
        }
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id != NotificationCenter.appDidLogout) {
            int a = 0;
            int a2;
            if (id == NotificationCenter.pushMessagesUpdated) {
                if (!this.isReply) {
                    this.popupMessages.clear();
                    while (true) {
                        a2 = a;
                        if (a2 >= 3) {
                            break;
                        }
                        if (UserConfig.getInstance(a2).isClientActivated()) {
                            this.popupMessages.addAll(NotificationsController.getInstance(a2).popupMessages);
                        }
                        a = a2 + 1;
                    }
                    getNewMessage();
                }
            } else if (id == NotificationCenter.updateInterfaces) {
                if (this.currentMessageObject != null) {
                    if (account == this.lastResumedAccount) {
                        a2 = ((Integer) args[0]).intValue();
                        if (!((a2 & 1) == 0 && (a2 & 4) == 0 && (a2 & 16) == 0 && (a2 & 32) == 0)) {
                            updateSubtitle();
                        }
                        if (!((a2 & 2) == 0 && (a2 & 8) == 0)) {
                            checkAndUpdateAvatar();
                        }
                        if ((a2 & 64) != 0) {
                            CharSequence printString = (CharSequence) MessagesController.getInstance(this.currentMessageObject.currentAccount).printingStrings.get(this.currentMessageObject.getDialogId());
                            if ((this.lastPrintString != null && printString == null) || ((this.lastPrintString == null && printString != null) || !(this.lastPrintString == null || printString == null || this.lastPrintString.equals(printString)))) {
                                updateSubtitle();
                            }
                        }
                    }
                }
            } else if (id == NotificationCenter.messagePlayingDidReset) {
                mid = args[0];
                if (this.messageContainer != null) {
                    count = this.messageContainer.getChildCount();
                    while (a < count) {
                        view = this.messageContainer.getChildAt(a);
                        if (((Integer) view.getTag()).intValue() == 3) {
                            cell = (PopupAudioView) view.findViewWithTag(Integer.valueOf(300));
                            messageObject = cell.getMessageObject();
                            if (messageObject != null && messageObject.currentAccount == account && messageObject.getId() == mid.intValue()) {
                                cell.updateButtonState();
                                break;
                            }
                        }
                        a++;
                    }
                }
            } else if (id == NotificationCenter.messagePlayingProgressDidChanged) {
                mid = (Integer) args[0];
                if (this.messageContainer != null) {
                    count = this.messageContainer.getChildCount();
                    while (a < count) {
                        view = this.messageContainer.getChildAt(a);
                        if (((Integer) view.getTag()).intValue() == 3) {
                            cell = (PopupAudioView) view.findViewWithTag(Integer.valueOf(300));
                            messageObject = cell.getMessageObject();
                            if (messageObject != null && messageObject.currentAccount == account && messageObject.getId() == mid.intValue()) {
                                cell.updateProgress();
                                break;
                            }
                        }
                        a++;
                    }
                }
            } else if (id == NotificationCenter.emojiDidLoaded) {
                if (this.messageContainer != null) {
                    a2 = this.messageContainer.getChildCount();
                    while (true) {
                        int a3 = a;
                        if (a3 >= a2) {
                            break;
                        }
                        View view = this.messageContainer.getChildAt(a3);
                        if (((Integer) view.getTag()).intValue() == 1) {
                            TextView textView = (TextView) view.findViewWithTag(Integer.valueOf(301));
                            if (textView != null) {
                                textView.invalidate();
                            }
                        }
                        a = a3 + 1;
                    }
                }
            } else if (id == NotificationCenter.contactsDidLoaded && account == this.lastResumedAccount) {
                updateSubtitle();
            }
        } else if (account == this.lastResumedAccount) {
            onFinish();
            finish();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        onFinish();
        MediaController.getInstance().setFeedbackView(this.chatActivityEnterView, false);
        if (this.wakeLock.isHeld()) {
            this.wakeLock.release();
        }
        if (this.avatarImageView != null) {
            this.avatarImageView.setImageDrawable(null);
        }
    }

    protected void onFinish() {
        if (!this.finished) {
            this.finished = true;
            if (this.isReply) {
                this.popupMessages.clear();
            }
            for (int a = 0; a < 3; a++) {
                NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.appDidLogout);
                NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.updateInterfaces);
                NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.messagePlayingProgressDidChanged);
                NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.messagePlayingDidReset);
                NotificationCenter.getInstance(a).removeObserver(this, NotificationCenter.contactsDidLoaded);
            }
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.pushMessagesUpdated);
            NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.emojiDidLoaded);
            if (this.chatActivityEnterView != null) {
                this.chatActivityEnterView.onDestroy();
            }
            if (this.wakeLock.isHeld()) {
                this.wakeLock.release();
            }
        }
    }
}
