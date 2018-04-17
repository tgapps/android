package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.messenger.beta.R;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.SimpleTextView;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.MediaActivity;
import org.telegram.ui.ProfileActivity;

public class ChatAvatarContainer extends FrameLayout implements NotificationCenterDelegate {
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private BackupImageView avatarImageView;
    private int currentAccount = UserConfig.selectedAccount;
    private int currentConnectionState;
    private CharSequence lastSubtitle;
    private boolean occupyStatusBar = true;
    private int onlineCount = -1;
    private ChatActivity parentFragment;
    private StatusDrawable[] statusDrawables = new StatusDrawable[5];
    private SimpleTextView subtitleTextView;
    private ImageView timeItem;
    private TimerDrawable timerDrawable;
    private SimpleTextView titleTextView;

    public void updateOnlineCount() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.ChatAvatarContainer.updateOnlineCount():void
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
        r0 = r7.parentFragment;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = 0;
        r7.onlineCount = r0;
        r1 = r7.parentFragment;
        r1 = r1.getCurrentChatInfo();
        if (r1 != 0) goto L_0x0011;
    L_0x0010:
        return;
    L_0x0011:
        r2 = r7.currentAccount;
        r2 = org.telegram.tgnet.ConnectionsManager.getInstance(r2);
        r2 = r2.getCurrentTime();
        r3 = r1 instanceof org.telegram.tgnet.TLRPC.TL_chatFull;
        if (r3 != 0) goto L_0x002d;
    L_0x001f:
        r3 = r1 instanceof org.telegram.tgnet.TLRPC.TL_channelFull;
        if (r3 == 0) goto L_0x007d;
        r3 = r1.participants_count;
        r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r3 > r4) goto L_0x007d;
        r3 = r1.participants;
        if (r3 == 0) goto L_0x007d;
        r3 = r1.participants;
        r3 = r3.participants;
        r3 = r3.size();
        if (r0 >= r3) goto L_0x007d;
        r3 = r1.participants;
        r3 = r3.participants;
        r3 = r3.get(r0);
        r3 = (org.telegram.tgnet.TLRPC.ChatParticipant) r3;
        r4 = r7.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r5 = r3.user_id;
        r5 = java.lang.Integer.valueOf(r5);
        r4 = r4.getUser(r5);
        if (r4 == 0) goto L_0x007a;
        r5 = r4.status;
        if (r5 == 0) goto L_0x007a;
        r5 = r4.status;
        r5 = r5.expires;
        if (r5 > r2) goto L_0x006c;
        r5 = r4.id;
        r6 = r7.currentAccount;
        r6 = org.telegram.messenger.UserConfig.getInstance(r6);
        r6 = r6.getClientUserId();
        if (r5 != r6) goto L_0x007a;
        r5 = r4.status;
        r5 = r5.expires;
        r6 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        if (r5 <= r6) goto L_0x007a;
        r5 = r7.onlineCount;
        r5 = r5 + 1;
        r7.onlineCount = r5;
        r0 = r0 + 1;
        goto L_0x002e;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ChatAvatarContainer.updateOnlineCount():void");
    }

    public void updateSubtitle() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.ChatAvatarContainer.updateSubtitle():void
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
        r0 = r11.parentFragment;
        if (r0 != 0) goto L_0x0005;
    L_0x0004:
        return;
    L_0x0005:
        r0 = r11.parentFragment;
        r0 = r0.getCurrentUser();
        r1 = org.telegram.messenger.UserObject.isUserSelf(r0);
        if (r1 == 0) goto L_0x0021;
    L_0x0011:
        r1 = r11.subtitleTextView;
        r1 = r1.getVisibility();
        r2 = 8;
        if (r1 == r2) goto L_0x0020;
    L_0x001b:
        r1 = r11.subtitleTextView;
        r1.setVisibility(r2);
    L_0x0020:
        return;
    L_0x0021:
        r1 = r11.parentFragment;
        r1 = r1.getCurrentChat();
        r2 = r11.currentAccount;
        r2 = org.telegram.messenger.MessagesController.getInstance(r2);
        r2 = r2.printingStrings;
        r3 = r11.parentFragment;
        r3 = r3.getDialogId();
        r2 = r2.get(r3);
        r2 = (java.lang.CharSequence) r2;
        r3 = 1;
        r4 = 0;
        if (r2 == 0) goto L_0x004f;
    L_0x003f:
        r5 = new java.lang.String[r3];
        r6 = "...";
        r5[r4] = r6;
        r6 = new java.lang.String[r3];
        r7 = "";
        r6[r4] = r7;
        r2 = android.text.TextUtils.replace(r2, r5, r6);
    L_0x004f:
        if (r2 == 0) goto L_0x0068;
    L_0x0051:
        r5 = r2.length();
        if (r5 == 0) goto L_0x0068;
    L_0x0057:
        r5 = org.telegram.messenger.ChatObject.isChannel(r1);
        if (r5 == 0) goto L_0x0062;
    L_0x005d:
        r5 = r1.megagroup;
        if (r5 != 0) goto L_0x0062;
    L_0x0061:
        goto L_0x0068;
    L_0x0062:
        r4 = r2;
        r11.setTypingAnimation(r3);
        goto L_0x01ee;
    L_0x0068:
        r11.setTypingAnimation(r4);
        if (r1 == 0) goto L_0x018e;
    L_0x006d:
        r5 = r11.parentFragment;
        r5 = r5.getCurrentChatInfo();
        r6 = org.telegram.messenger.ChatObject.isChannel(r1);
        r7 = 2;
        if (r6 == 0) goto L_0x0135;
    L_0x007a:
        if (r5 == 0) goto L_0x0101;
    L_0x007c:
        r6 = r5.participants_count;
        if (r6 == 0) goto L_0x0101;
    L_0x0080:
        r6 = r1.megagroup;
        if (r6 == 0) goto L_0x00ba;
    L_0x0084:
        r6 = r5.participants_count;
        r8 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r6 > r8) goto L_0x00ba;
    L_0x008a:
        r6 = r11.onlineCount;
        if (r6 <= r3) goto L_0x00b0;
    L_0x008e:
        r6 = r5.participants_count;
        if (r6 == 0) goto L_0x00b0;
    L_0x0092:
        r6 = "%s, %s";
        r7 = new java.lang.Object[r7];
        r8 = "Members";
        r9 = r5.participants_count;
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r9);
        r7[r4] = r8;
        r4 = "OnlineCount";
        r8 = r11.onlineCount;
        r4 = org.telegram.messenger.LocaleController.formatPluralString(r4, r8);
        r7[r3] = r4;
        r3 = java.lang.String.format(r6, r7);
        goto L_0x0144;
    L_0x00b0:
        r3 = "Members";
        r4 = r5.participants_count;
        r3 = org.telegram.messenger.LocaleController.formatPluralString(r3, r4);
        goto L_0x0144;
    L_0x00ba:
        r6 = new int[r3];
        r7 = r5.participants_count;
        r7 = org.telegram.messenger.LocaleController.formatShortNumber(r7, r6);
        r8 = r1.megagroup;
        if (r8 == 0) goto L_0x00e3;
    L_0x00c6:
        r8 = "Members";
        r9 = r6[r4];
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r9);
        r9 = "%d";
        r3 = new java.lang.Object[r3];
        r10 = r6[r4];
        r10 = java.lang.Integer.valueOf(r10);
        r3[r4] = r10;
        r3 = java.lang.String.format(r9, r3);
        r3 = r8.replace(r3, r7);
        goto L_0x00ff;
    L_0x00e3:
        r8 = "Subscribers";
        r9 = r6[r4];
        r8 = org.telegram.messenger.LocaleController.formatPluralString(r8, r9);
        r9 = "%d";
        r3 = new java.lang.Object[r3];
        r10 = r6[r4];
        r10 = java.lang.Integer.valueOf(r10);
        r3[r4] = r10;
        r3 = java.lang.String.format(r9, r3);
        r3 = r8.replace(r3, r7);
    L_0x00ff:
        goto L_0x018c;
    L_0x0101:
        r3 = r1.megagroup;
        if (r3 == 0) goto L_0x0113;
    L_0x0105:
        r3 = "Loading";
        r4 = 2131493762; // 0x7f0c0382 float:1.8611013E38 double:1.053097842E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r3 = r3.toLowerCase();
        goto L_0x0144;
    L_0x0113:
        r3 = r1.flags;
        r3 = r3 & 64;
        if (r3 == 0) goto L_0x0127;
    L_0x0119:
        r3 = "ChannelPublic";
        r4 = 2131493200; // 0x7f0c0150 float:1.8609873E38 double:1.0530975645E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r3 = r3.toLowerCase();
        goto L_0x0144;
    L_0x0127:
        r3 = "ChannelPrivate";
        r4 = 2131493197; // 0x7f0c014d float:1.8609867E38 double:1.053097563E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r3 = r3.toLowerCase();
        goto L_0x0144;
    L_0x0135:
        r6 = org.telegram.messenger.ChatObject.isKickedFromChat(r1);
        if (r6 == 0) goto L_0x0145;
    L_0x013b:
        r3 = "YouWereKicked";
        r4 = 2131494659; // 0x7f0c0703 float:1.8612833E38 double:1.0530982853E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
    L_0x0144:
        goto L_0x018c;
    L_0x0145:
        r6 = org.telegram.messenger.ChatObject.isLeftFromChat(r1);
        if (r6 == 0) goto L_0x0155;
    L_0x014b:
        r3 = "YouLeft";
        r4 = 2131494658; // 0x7f0c0702 float:1.861283E38 double:1.053098285E-314;
        r3 = org.telegram.messenger.LocaleController.getString(r3, r4);
        goto L_0x0144;
    L_0x0155:
        r6 = r1.participants_count;
        if (r5 == 0) goto L_0x0165;
    L_0x0159:
        r8 = r5.participants;
        if (r8 == 0) goto L_0x0165;
    L_0x015d:
        r8 = r5.participants;
        r8 = r8.participants;
        r6 = r8.size();
    L_0x0165:
        r8 = r11.onlineCount;
        if (r8 <= r3) goto L_0x0186;
    L_0x0169:
        if (r6 == 0) goto L_0x0186;
    L_0x016b:
        r8 = "%s, %s";
        r7 = new java.lang.Object[r7];
        r9 = "Members";
        r9 = org.telegram.messenger.LocaleController.formatPluralString(r9, r6);
        r7[r4] = r9;
        r4 = "OnlineCount";
        r9 = r11.onlineCount;
        r4 = org.telegram.messenger.LocaleController.formatPluralString(r4, r9);
        r7[r3] = r4;
        r3 = java.lang.String.format(r8, r7);
        goto L_0x0144;
    L_0x0186:
        r3 = "Members";
        r3 = org.telegram.messenger.LocaleController.formatPluralString(r3, r6);
    L_0x018c:
        r4 = r3;
        goto L_0x01ee;
    L_0x018e:
        if (r0 == 0) goto L_0x01ec;
    L_0x0190:
        r3 = r11.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r4 = r0.id;
        r4 = java.lang.Integer.valueOf(r4);
        r3 = r3.getUser(r4);
        if (r3 == 0) goto L_0x01a3;
    L_0x01a2:
        r0 = r3;
    L_0x01a3:
        r4 = r0.id;
        r5 = r11.currentAccount;
        r5 = org.telegram.messenger.UserConfig.getInstance(r5);
        r5 = r5.getClientUserId();
        if (r4 != r5) goto L_0x01bb;
    L_0x01b1:
        r4 = "ChatYourSelf";
        r5 = 2131493233; // 0x7f0c0171 float:1.860994E38 double:1.053097581E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
        goto L_0x01e9;
    L_0x01bb:
        r4 = r0.id;
        r5 = 333000; // 0x514c8 float:4.66632E-40 double:1.64524E-318;
        if (r4 == r5) goto L_0x01df;
        r4 = r0.id;
        r5 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
        if (r4 != r5) goto L_0x01ca;
        goto L_0x01df;
        r4 = r0.bot;
        if (r4 == 0) goto L_0x01d8;
        r4 = "Bot";
        r5 = 2131493086; // 0x7f0c00de float:1.8609642E38 double:1.053097508E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
        goto L_0x01ba;
        r4 = r11.currentAccount;
        r4 = org.telegram.messenger.LocaleController.formatUserStatus(r4, r0);
        goto L_0x01e9;
        r4 = "ServiceNotifications";
        r5 = 2131494365; // 0x7f0c05dd float:1.8612236E38 double:1.05309814E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
        goto L_0x01ba;
        goto L_0x01ee;
    L_0x01ec:
        r4 = "";
    L_0x01ee:
        r3 = r11.lastSubtitle;
        if (r3 != 0) goto L_0x01f8;
        r3 = r11.subtitleTextView;
        r3.setText(r4);
        goto L_0x01fa;
        r11.lastSubtitle = r4;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ChatAvatarContainer.updateSubtitle():void");
    }

    public ChatAvatarContainer(Context context, ChatActivity chatActivity, boolean needTime) {
        super(context);
        this.parentFragment = chatActivity;
        this.avatarImageView = new BackupImageView(context);
        this.avatarImageView.setRoundRadius(AndroidUtilities.dp(21.0f));
        addView(this.avatarImageView);
        this.titleTextView = new SimpleTextView(context);
        this.titleTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultTitle));
        this.titleTextView.setTextSize(18);
        this.titleTextView.setGravity(3);
        this.titleTextView.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextView.setLeftDrawableTopPadding(-AndroidUtilities.dp(1.3f));
        addView(this.titleTextView);
        this.subtitleTextView = new SimpleTextView(context);
        this.subtitleTextView.setTextColor(Theme.getColor(Theme.key_actionBarDefaultSubtitle));
        this.subtitleTextView.setTextSize(14);
        this.subtitleTextView.setGravity(3);
        addView(this.subtitleTextView);
        if (needTime) {
            this.timeItem = new ImageView(context);
            this.timeItem.setPadding(AndroidUtilities.dp(10.0f), AndroidUtilities.dp(10.0f), AndroidUtilities.dp(5.0f), AndroidUtilities.dp(5.0f));
            this.timeItem.setScaleType(ScaleType.CENTER);
            ImageView imageView = this.timeItem;
            Drawable timerDrawable = new TimerDrawable(context);
            this.timerDrawable = timerDrawable;
            imageView.setImageDrawable(timerDrawable);
            addView(this.timeItem);
            this.timeItem.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ChatAvatarContainer.this.parentFragment.showDialog(AlertsCreator.createTTLAlert(ChatAvatarContainer.this.getContext(), ChatAvatarContainer.this.parentFragment.getCurrentEncryptedChat()).create());
                }
            });
        }
        if (this.parentFragment != null) {
            setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    User user = ChatAvatarContainer.this.parentFragment.getCurrentUser();
                    Chat chat = ChatAvatarContainer.this.parentFragment.getCurrentChat();
                    Bundle args;
                    ProfileActivity fragment;
                    if (user != null) {
                        args = new Bundle();
                        if (UserObject.isUserSelf(user)) {
                            args.putLong("dialog_id", ChatAvatarContainer.this.parentFragment.getDialogId());
                            MediaActivity fragment2 = new MediaActivity(args);
                            fragment2.setChatInfo(ChatAvatarContainer.this.parentFragment.getCurrentChatInfo());
                            ChatAvatarContainer.this.parentFragment.presentFragment(fragment2);
                        } else {
                            args.putInt("user_id", user.id);
                            if (ChatAvatarContainer.this.timeItem != null) {
                                args.putLong("dialog_id", ChatAvatarContainer.this.parentFragment.getDialogId());
                            }
                            fragment = new ProfileActivity(args);
                            fragment.setPlayProfileAnimation(true);
                            ChatAvatarContainer.this.parentFragment.presentFragment(fragment);
                        }
                    } else if (chat != null) {
                        args = new Bundle();
                        args.putInt("chat_id", chat.id);
                        fragment = new ProfileActivity(args);
                        fragment.setChatInfo(ChatAvatarContainer.this.parentFragment.getCurrentChatInfo());
                        fragment.setPlayProfileAnimation(true);
                        ChatAvatarContainer.this.parentFragment.presentFragment(fragment);
                    }
                }
            });
            Chat chat = this.parentFragment.getCurrentChat();
            this.statusDrawables[0] = new TypingDotsDrawable();
            this.statusDrawables[1] = new RecordStatusDrawable();
            this.statusDrawables[2] = new SendingFileDrawable();
            this.statusDrawables[3] = new PlayingGameDrawable();
            this.statusDrawables[4] = new RoundStatusDrawable();
            for (StatusDrawable isChat : this.statusDrawables) {
                isChat.setIsChat(chat != null);
            }
        }
    }

    public void setOccupyStatusBar(boolean value) {
        this.occupyStatusBar = value;
    }

    public void setTitleColors(int title, int subtitle) {
        this.titleTextView.setTextColor(title);
        this.subtitleTextView.setTextColor(title);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int availableWidth = width - AndroidUtilities.dp(70.0f);
        this.avatarImageView.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(42.0f), 1073741824));
        this.titleTextView.measure(MeasureSpec.makeMeasureSpec(availableWidth, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), Integer.MIN_VALUE));
        this.subtitleTextView.measure(MeasureSpec.makeMeasureSpec(availableWidth, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(20.0f), Integer.MIN_VALUE));
        if (this.timeItem != null) {
            this.timeItem.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(34.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(34.0f), 1073741824));
        }
        setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int viewTop = (ActionBar.getCurrentActionBarHeight() - AndroidUtilities.dp(42.0f)) / 2;
        int i = (VERSION.SDK_INT < 21 || !this.occupyStatusBar) ? 0 : AndroidUtilities.statusBarHeight;
        viewTop += i;
        this.avatarImageView.layout(AndroidUtilities.dp(8.0f), viewTop, AndroidUtilities.dp(50.0f), AndroidUtilities.dp(42.0f) + viewTop);
        if (this.subtitleTextView.getVisibility() == 0) {
            this.titleTextView.layout(AndroidUtilities.dp(62.0f), AndroidUtilities.dp(1.3f) + viewTop, AndroidUtilities.dp(62.0f) + this.titleTextView.getMeasuredWidth(), (this.titleTextView.getTextHeight() + viewTop) + AndroidUtilities.dp(1.3f));
        } else {
            this.titleTextView.layout(AndroidUtilities.dp(62.0f), AndroidUtilities.dp(11.0f) + viewTop, AndroidUtilities.dp(62.0f) + this.titleTextView.getMeasuredWidth(), (this.titleTextView.getTextHeight() + viewTop) + AndroidUtilities.dp(11.0f));
        }
        if (this.timeItem != null) {
            this.timeItem.layout(AndroidUtilities.dp(24.0f), AndroidUtilities.dp(15.0f) + viewTop, AndroidUtilities.dp(58.0f), AndroidUtilities.dp(49.0f) + viewTop);
        }
        this.subtitleTextView.layout(AndroidUtilities.dp(62.0f), AndroidUtilities.dp(24.0f) + viewTop, AndroidUtilities.dp(62.0f) + this.subtitleTextView.getMeasuredWidth(), (this.subtitleTextView.getTextHeight() + viewTop) + AndroidUtilities.dp(24.0f));
    }

    public void showTimeItem() {
        if (this.timeItem != null) {
            this.timeItem.setVisibility(0);
        }
    }

    public void hideTimeItem() {
        if (this.timeItem != null) {
            this.timeItem.setVisibility(8);
        }
    }

    public void setTime(int value) {
        if (this.timerDrawable != null) {
            this.timerDrawable.setTime(value);
        }
    }

    public void setTitleIcons(int leftIcon, int rightIcon) {
        this.titleTextView.setLeftDrawable(leftIcon);
        this.titleTextView.setRightDrawable(rightIcon);
    }

    public void setTitleIcons(Drawable leftIcon, Drawable rightIcon) {
        this.titleTextView.setLeftDrawable(leftIcon);
        this.titleTextView.setRightDrawable(rightIcon);
    }

    public void setTitle(CharSequence value) {
        this.titleTextView.setText(value);
    }

    public void setSubtitle(CharSequence value) {
        if (this.lastSubtitle == null) {
            this.subtitleTextView.setText(value);
        } else {
            this.lastSubtitle = value;
        }
    }

    public ImageView getTimeItem() {
        return this.timeItem;
    }

    public SimpleTextView getTitleTextView() {
        return this.titleTextView;
    }

    public SimpleTextView getSubtitleTextView() {
        return this.subtitleTextView;
    }

    private void setTypingAnimation(boolean start) {
        int a = 0;
        if (start) {
            try {
                Integer type = (Integer) MessagesController.getInstance(this.currentAccount).printingStringsTypes.get(this.parentFragment.getDialogId());
                this.subtitleTextView.setLeftDrawable(this.statusDrawables[type.intValue()]);
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
            return;
        }
        this.subtitleTextView.setLeftDrawable(null);
        while (a < this.statusDrawables.length) {
            this.statusDrawables[a].stop();
            a++;
        }
    }

    public void setChatAvatar(Chat chat) {
        TLObject newPhoto = null;
        if (chat.photo != null) {
            newPhoto = chat.photo.photo_small;
        }
        this.avatarDrawable.setInfo(chat);
        if (this.avatarImageView != null) {
            this.avatarImageView.setImage(newPhoto, "50_50", this.avatarDrawable);
        }
    }

    public void setUserAvatar(User user) {
        TLObject newPhoto = null;
        this.avatarDrawable.setInfo(user);
        if (UserObject.isUserSelf(user)) {
            this.avatarDrawable.setSavedMessages(2);
        } else if (user.photo != null) {
            newPhoto = user.photo.photo_small;
        }
        if (this.avatarImageView != null) {
            this.avatarImageView.setImage(newPhoto, "50_50", this.avatarDrawable);
        }
    }

    public void checkAndUpdateAvatar() {
        if (this.parentFragment != null) {
            TLObject newPhoto = null;
            User user = this.parentFragment.getCurrentUser();
            Chat chat = this.parentFragment.getCurrentChat();
            if (user != null) {
                this.avatarDrawable.setInfo(user);
                if (UserObject.isUserSelf(user)) {
                    this.avatarDrawable.setSavedMessages(2);
                } else if (user.photo != null) {
                    newPhoto = user.photo.photo_small;
                }
            } else if (chat != null) {
                if (chat.photo != null) {
                    newPhoto = chat.photo.photo_small;
                }
                this.avatarDrawable.setInfo(chat);
            }
            if (this.avatarImageView != null) {
                this.avatarImageView.setImage(newPhoto, "50_50", this.avatarDrawable);
            }
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.parentFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.didUpdatedConnectionState);
            this.currentConnectionState = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            updateCurrentConnectionState();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.parentFragment != null) {
            NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.didUpdatedConnectionState);
        }
    }

    public void didReceivedNotification(int id, int account, Object... args) {
        if (id == NotificationCenter.didUpdatedConnectionState) {
            int state = ConnectionsManager.getInstance(this.currentAccount).getConnectionState();
            if (this.currentConnectionState != state) {
                this.currentConnectionState = state;
                updateCurrentConnectionState();
            }
        }
    }

    private void updateCurrentConnectionState() {
        String title = null;
        if (this.currentConnectionState == 2) {
            title = LocaleController.getString("WaitingForNetwork", R.string.WaitingForNetwork);
        } else if (this.currentConnectionState == 1) {
            title = LocaleController.getString("Connecting", R.string.Connecting);
        } else if (this.currentConnectionState == 5) {
            title = LocaleController.getString("Updating", R.string.Updating);
        } else if (this.currentConnectionState == 4) {
            title = LocaleController.getString("ConnectingToProxy", R.string.ConnectingToProxy);
        }
        if (title != null) {
            this.lastSubtitle = this.subtitleTextView.getText();
            this.subtitleTextView.setText(title);
        } else if (this.lastSubtitle != null) {
            this.subtitleTextView.setText(this.lastSubtitle);
            this.lastSubtitle = null;
        }
    }
}
