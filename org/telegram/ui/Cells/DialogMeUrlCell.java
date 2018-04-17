package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;
import android.view.View.MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLRPC.RecentMeUrl;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;

public class DialogMeUrlCell extends BaseCell {
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private ImageReceiver avatarImage = new ImageReceiver(this);
    private int avatarTop = AndroidUtilities.dp(10.0f);
    private int currentAccount = UserConfig.selectedAccount;
    private boolean drawNameBot;
    private boolean drawNameBroadcast;
    private boolean drawNameGroup;
    private boolean drawNameLock;
    private boolean drawVerified;
    private boolean isSelected;
    private StaticLayout messageLayout;
    private int messageLeft;
    private int messageTop = AndroidUtilities.dp(40.0f);
    private StaticLayout nameLayout;
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameMuteLeft;
    private RecentMeUrl recentMeUrl;
    public boolean useSeparator;

    public void buildLayout() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.DialogMeUrlCell.buildLayout():void
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
        r1 = r26;
        r2 = "";
        r11 = org.telegram.ui.ActionBar.Theme.dialogs_namePaint;
        r12 = org.telegram.ui.ActionBar.Theme.dialogs_messagePaint;
        r13 = 0;
        r1.drawNameGroup = r13;
        r1.drawNameBroadcast = r13;
        r1.drawNameLock = r13;
        r1.drawNameBot = r13;
        r1.drawVerified = r13;
        r3 = r1.recentMeUrl;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_recentMeUrlChat;
        r4 = 1099694080; // 0x418c0000 float:17.5 double:5.43321066E-315;
        r5 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r6 = 0;
        r7 = 1;
        r8 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        if (r3 == 0) goto L_0x00b5;
    L_0x0021:
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r9 = r1.recentMeUrl;
        r9 = r9.chat_id;
        r9 = java.lang.Integer.valueOf(r9);
        r3 = r3.getChat(r9);
        r9 = r3.id;
        if (r9 < 0) goto L_0x004b;
    L_0x0037:
        r9 = org.telegram.messenger.ChatObject.isChannel(r3);
        if (r9 == 0) goto L_0x0042;
    L_0x003d:
        r9 = r3.megagroup;
        if (r9 != 0) goto L_0x0042;
    L_0x0041:
        goto L_0x004b;
    L_0x0042:
        r1.drawNameGroup = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1.nameLockTop = r4;
        goto L_0x0053;
    L_0x004b:
        r1.drawNameBroadcast = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r1.nameLockTop = r4;
    L_0x0053:
        r4 = r3.verified;
        r1.drawVerified = r4;
        r4 = org.telegram.messenger.LocaleController.isRTL;
        if (r4 != 0) goto L_0x007f;
    L_0x005b:
        r4 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1.nameLockLeft = r4;
        r4 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r4 = r4 + 4;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r5 = r1.drawNameGroup;
        if (r5 == 0) goto L_0x0078;
    L_0x0071:
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
    L_0x0073:
        r5 = r5.getIntrinsicWidth();
        goto L_0x007b;
    L_0x0078:
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x0073;
    L_0x007b:
        r4 = r4 + r5;
        r1.nameLeft = r4;
        goto L_0x00a2;
    L_0x007f:
        r4 = r26.getMeasuredWidth();
        r5 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r5 = (float) r5;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = r1.drawNameGroup;
        if (r5 == 0) goto L_0x0096;
    L_0x008f:
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
    L_0x0091:
        r5 = r5.getIntrinsicWidth();
        goto L_0x0099;
    L_0x0096:
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x0091;
    L_0x0099:
        r4 = r4 - r5;
        r1.nameLockLeft = r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r1.nameLeft = r4;
    L_0x00a2:
        r2 = r3.title;
        r4 = r3.photo;
        if (r4 == 0) goto L_0x00ad;
    L_0x00a8:
        r4 = r3.photo;
        r6 = r4.photo_small;
        goto L_0x00ae;
        r4 = r1.avatarDrawable;
        r4.setInfo(r3);
        goto L_0x02ac;
    L_0x00b5:
        r3 = r1.recentMeUrl;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_recentMeUrlUser;
        if (r3 == 0) goto L_0x0143;
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.MessagesController.getInstance(r3);
        r4 = r1.recentMeUrl;
        r4 = r4.user_id;
        r4 = java.lang.Integer.valueOf(r4);
        r3 = r3.getUser(r4);
        r4 = org.telegram.messenger.LocaleController.isRTL;
        if (r4 != 0) goto L_0x00db;
        r4 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1.nameLeft = r4;
        goto L_0x00e1;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r1.nameLeft = r4;
        if (r3 == 0) goto L_0x012e;
        r4 = r3.bot;
        if (r4 == 0) goto L_0x012a;
        r1.drawNameBot = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r1.nameLockTop = r4;
        r4 = org.telegram.messenger.LocaleController.isRTL;
        if (r4 != 0) goto L_0x010f;
        r4 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1.nameLockLeft = r4;
        r4 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r4 = r4 + 4;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_botDrawable;
        r5 = r5.getIntrinsicWidth();
        r4 = r4 + r5;
        r1.nameLeft = r4;
        goto L_0x012a;
        r4 = r26.getMeasuredWidth();
        r5 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r5 = (float) r5;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_botDrawable;
        r5 = r5.getIntrinsicWidth();
        r4 = r4 - r5;
        r1.nameLockLeft = r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r1.nameLeft = r4;
        r4 = r3.verified;
        r1.drawVerified = r4;
        r2 = org.telegram.messenger.UserObject.getUserName(r3);
        r4 = r3.photo;
        if (r4 == 0) goto L_0x013b;
        r4 = r3.photo;
        r6 = r4.photo_small;
        goto L_0x013c;
        r4 = r1.avatarDrawable;
        r4.setInfo(r3);
        goto L_0x02ac;
        r3 = r1.recentMeUrl;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_recentMeUrlStickerSet;
        r9 = 5;
        if (r3 == 0) goto L_0x017c;
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 != 0) goto L_0x0158;
        r3 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r3 = (float) r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r1.nameLeft = r3;
        goto L_0x015e;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r1.nameLeft = r3;
        r3 = r1.recentMeUrl;
        r3 = r3.set;
        r3 = r3.set;
        r2 = r3.title;
        r3 = r1.recentMeUrl;
        r3 = r3.set;
        r3 = r3.cover;
        r4 = r1.avatarDrawable;
        r5 = r1.recentMeUrl;
        r5 = r5.set;
        r5 = r5.set;
        r5 = r5.title;
        r4.setInfo(r9, r5, r6, r13);
        r15 = r3;
        goto L_0x02ad;
        r3 = r1.recentMeUrl;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_recentMeUrlChatInvite;
        if (r3 == 0) goto L_0x028d;
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 != 0) goto L_0x0190;
        r3 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r3 = (float) r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r1.nameLeft = r3;
        goto L_0x0196;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r1.nameLeft = r3;
        r3 = r1.recentMeUrl;
        r3 = r3.chat_invite;
        r3 = r3.chat;
        if (r3 == 0) goto L_0x0205;
        r3 = r1.avatarDrawable;
        r9 = r1.recentMeUrl;
        r9 = r9.chat_invite;
        r9 = r9.chat;
        r3.setInfo(r9);
        r3 = r1.recentMeUrl;
        r3 = r3.chat_invite;
        r3 = r3.chat;
        r2 = r3.title;
        r3 = r1.recentMeUrl;
        r3 = r3.chat_invite;
        r3 = r3.chat;
        r3 = r3.photo;
        if (r3 == 0) goto L_0x01c6;
        r3 = r1.recentMeUrl;
        r3 = r3.chat_invite;
        r3 = r3.chat;
        r3 = r3.photo;
        r6 = r3.photo_small;
        goto L_0x01c7;
        r3 = r6;
        r6 = r1.recentMeUrl;
        r6 = r6.chat_invite;
        r6 = r6.chat;
        r6 = r6.id;
        if (r6 < 0) goto L_0x01f2;
        r6 = r1.recentMeUrl;
        r6 = r6.chat_invite;
        r6 = r6.chat;
        r6 = org.telegram.messenger.ChatObject.isChannel(r6);
        if (r6 == 0) goto L_0x01e9;
        r6 = r1.recentMeUrl;
        r6 = r6.chat_invite;
        r6 = r6.chat;
        r6 = r6.megagroup;
        if (r6 != 0) goto L_0x01e9;
        goto L_0x01f2;
        r1.drawNameGroup = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1.nameLockTop = r4;
        goto L_0x01fa;
        r1.drawNameBroadcast = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r1.nameLockTop = r4;
        r4 = r1.recentMeUrl;
        r4 = r4.chat_invite;
        r4 = r4.chat;
        r4 = r4.verified;
        r1.drawVerified = r4;
        goto L_0x0240;
        r3 = r1.recentMeUrl;
        r3 = r3.chat_invite;
        r2 = r3.title;
        r3 = r1.recentMeUrl;
        r3 = r3.chat_invite;
        r3 = r3.photo;
        r3 = r3.photo_small;
        r10 = r1.avatarDrawable;
        r14 = r1.recentMeUrl;
        r14 = r14.chat_invite;
        r14 = r14.title;
        r10.setInfo(r9, r14, r6, r13);
        r6 = r1.recentMeUrl;
        r6 = r6.chat_invite;
        r6 = r6.broadcast;
        if (r6 != 0) goto L_0x0238;
        r6 = r1.recentMeUrl;
        r6 = r6.chat_invite;
        r6 = r6.channel;
        if (r6 == 0) goto L_0x022f;
        goto L_0x0238;
        r1.drawNameGroup = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1.nameLockTop = r4;
        goto L_0x0240;
        r1.drawNameBroadcast = r7;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r1.nameLockTop = r4;
        r6 = r3;
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 != 0) goto L_0x0269;
        r3 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r3 = (float) r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r1.nameLockLeft = r3;
        r3 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r3 = r3 + 4;
        r3 = (float) r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r1.drawNameGroup;
        if (r4 == 0) goto L_0x0262;
        r4 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
        r4 = r4.getIntrinsicWidth();
        goto L_0x0265;
        r4 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x025d;
        r3 = r3 + r4;
        r1.nameLeft = r3;
        goto L_0x02ac;
        r3 = r26.getMeasuredWidth();
        r4 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r4 = r1.drawNameGroup;
        if (r4 == 0) goto L_0x0280;
        r4 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
        r4 = r4.getIntrinsicWidth();
        goto L_0x0283;
        r4 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x027b;
        r3 = r3 - r4;
        r1.nameLockLeft = r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r1.nameLeft = r3;
        goto L_0x02ac;
        r3 = r1.recentMeUrl;
        r3 = r3 instanceof org.telegram.tgnet.TLRPC.TL_recentMeUrlUnknown;
        if (r3 == 0) goto L_0x02ab;
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 != 0) goto L_0x02a1;
        r3 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r3 = (float) r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r1.nameLeft = r3;
        goto L_0x02a7;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r1.nameLeft = r3;
        r2 = "Url";
        r6 = 0;
        goto L_0x02ac;
        r15 = r6;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r4 = r4.linkPrefix;
        r3.append(r4);
        r4 = "/";
        r3.append(r4);
        r4 = r1.recentMeUrl;
        r4 = r4.url;
        r3.append(r4);
        r10 = r3.toString();
        r14 = r1.avatarImage;
        r16 = "50_50";
        r3 = r1.avatarDrawable;
        r18 = 0;
        r19 = 0;
        r17 = r3;
        r14.setImage(r15, r16, r17, r18, r19);
        r3 = android.text.TextUtils.isEmpty(r2);
        if (r3 == 0) goto L_0x02eb;
        r3 = "HiddenName";
        r4 = 2131493648; // 0x7f0c0310 float:1.8610782E38 double:1.053097786E-314;
        r2 = org.telegram.messenger.LocaleController.getString(r3, r4);
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 != 0) goto L_0x02fc;
        r3 = r26.getMeasuredWidth();
        r4 = r1.nameLeft;
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r3 = r3 - r4;
        goto L_0x030b;
        r3 = r26.getMeasuredWidth();
        r4 = r1.nameLeft;
        r3 = r3 - r4;
        r4 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r4 = r1.drawNameLock;
        r5 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        if (r4 == 0) goto L_0x031e;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_lockDrawable;
        r5 = r5.getIntrinsicWidth();
        r4 = r4 + r5;
        r3 = r3 - r4;
        goto L_0x0350;
        r4 = r1.drawNameGroup;
        if (r4 == 0) goto L_0x032f;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
        r5 = r5.getIntrinsicWidth();
        r4 = r4 + r5;
        r3 = r3 - r4;
        goto L_0x0350;
        r4 = r1.drawNameBroadcast;
        if (r4 == 0) goto L_0x0340;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        r5 = r5.getIntrinsicWidth();
        r4 = r4 + r5;
        r3 = r3 - r4;
        goto L_0x0350;
        r4 = r1.drawNameBot;
        if (r4 == 0) goto L_0x0350;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_botDrawable;
        r5 = r5.getIntrinsicWidth();
        r4 = r4 + r5;
        r3 = r3 - r4;
        r4 = r1.drawVerified;
        r14 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        if (r4 == 0) goto L_0x036b;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_verifiedDrawable;
        r5 = r5.getIntrinsicWidth();
        r4 = r4 + r5;
        r3 = r3 - r4;
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 == 0) goto L_0x036b;
        r5 = r1.nameLeft;
        r5 = r5 + r4;
        r1.nameLeft = r5;
        r9 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = java.lang.Math.max(r4, r3);
        r3 = 10;
        r4 = 32;
        r3 = r2.replace(r3, r4);	 Catch:{ Exception -> 0x03ae }
        r4 = org.telegram.messenger.AndroidUtilities.dp(r9);	 Catch:{ Exception -> 0x03ae }
        r4 = r8 - r4;	 Catch:{ Exception -> 0x03ae }
        r4 = (float) r4;	 Catch:{ Exception -> 0x03ae }
        r5 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x03ae }
        r4 = android.text.TextUtils.ellipsize(r3, r11, r4, r5);	 Catch:{ Exception -> 0x03ae }
        r7 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x03ae }
        r16 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x03ae }
        r17 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r18 = 0;
        r19 = 0;
        r3 = r7;
        r5 = r11;
        r6 = r8;
        r14 = r7;
        r7 = r16;
        r20 = r8;
        r8 = r17;
        r13 = r9;
        r9 = r18;
        r21 = r10;
        r10 = r19;
        r3.<init>(r4, r5, r6, r7, r8, r9, r10);	 Catch:{ Exception -> 0x03ab }
        r1.nameLayout = r14;	 Catch:{ Exception -> 0x03ab }
        goto L_0x03b8;
    L_0x03ab:
        r0 = move-exception;
        r3 = r0;
        goto L_0x03b5;
    L_0x03ae:
        r0 = move-exception;
        r20 = r8;
        r13 = r9;
        r21 = r10;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r3 = r26.getMeasuredWidth();
        r4 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r4 = r4 + 16;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r3 = r3 - r4;
        r4 = org.telegram.messenger.LocaleController.isRTL;
        if (r4 != 0) goto L_0x03e3;
        r4 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r4 = (float) r4;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1.messageLeft = r4;
        r4 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r4 == 0) goto L_0x03dc;
        r4 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        goto L_0x03de;
        r4 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        goto L_0x03ff;
        r4 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r1.messageLeft = r4;
        r4 = r26.getMeasuredWidth();
        r5 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r5 == 0) goto L_0x03f8;
        r5 = 1115815936; // 0x42820000 float:65.0 double:5.51286321E-315;
        goto L_0x03fa;
        r5 = 1114898432; // 0x42740000 float:61.0 double:5.50833014E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r14 = r4;
        r4 = r1.avatarImage;
        r5 = r1.avatarTop;
        r6 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r4.setImageCoords(r14, r5, r7, r6);
        r4 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r10 = java.lang.Math.max(r4, r3);
        r3 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r3 = r10 - r3;
        r3 = (float) r3;
        r4 = android.text.TextUtils.TruncateAt.END;
        r13 = r21;
        r4 = android.text.TextUtils.ellipsize(r13, r12, r3, r4);
        r9 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0449 }
        r7 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0449 }
        r8 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r16 = 0;
        r17 = 0;
        r3 = r9;
        r5 = r12;
        r6 = r10;
        r22 = r9;
        r9 = r16;
        r23 = r2;
        r2 = r10;
        r10 = r17;
        r3.<init>(r4, r5, r6, r7, r8, r9, r10);	 Catch:{ Exception -> 0x0446 }
        r3 = r22;	 Catch:{ Exception -> 0x0446 }
        r1.messageLayout = r3;	 Catch:{ Exception -> 0x0446 }
        goto L_0x0451;
    L_0x0446:
        r0 = move-exception;
        r3 = r0;
        goto L_0x044e;
    L_0x0449:
        r0 = move-exception;
        r23 = r2;
        r2 = r10;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 == 0) goto L_0x04e9;
        r3 = r1.nameLayout;
        r5 = 0;
        if (r3 == 0) goto L_0x04b1;
        r3 = r1.nameLayout;
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x04b1;
        r3 = r1.nameLayout;
        r6 = 0;
        r3 = r3.getLineLeft(r6);
        r7 = r1.nameLayout;
        r7 = r7.getLineWidth(r6);
        r6 = (double) r7;
        r6 = java.lang.Math.ceil(r6);
        r8 = r1.drawVerified;
        if (r8 == 0) goto L_0x0498;
        r8 = r1.nameLeft;
        r8 = (double) r8;
        r24 = r11;
        r25 = r12;
        r10 = r20;
        r11 = (double) r10;
        r11 = r11 - r6;
        r8 = r8 + r11;
        r11 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r11 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r11 = (double) r11;
        r8 = r8 - r11;
        r11 = org.telegram.ui.ActionBar.Theme.dialogs_verifiedDrawable;
        r11 = r11.getIntrinsicWidth();
        r11 = (double) r11;
        r8 = r8 - r11;
        r8 = (int) r8;
        r1.nameMuteLeft = r8;
        goto L_0x049e;
        r24 = r11;
        r25 = r12;
        r10 = r20;
        r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r8 != 0) goto L_0x04b7;
        r8 = (double) r10;
        r11 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1));
        if (r11 >= 0) goto L_0x04b7;
        r8 = r1.nameLeft;
        r8 = (double) r8;
        r11 = (double) r10;
        r11 = r11 - r6;
        r8 = r8 + r11;
        r8 = (int) r8;
        r1.nameLeft = r8;
        goto L_0x04b7;
        r24 = r11;
        r25 = r12;
        r10 = r20;
        r3 = r1.messageLayout;
        if (r3 == 0) goto L_0x0564;
        r3 = r1.messageLayout;
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x0564;
        r3 = r1.messageLayout;
        r6 = 0;
        r3 = r3.getLineLeft(r6);
        r5 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r5 != 0) goto L_0x0564;
        r5 = r1.messageLayout;
        r5 = r5.getLineWidth(r6);
        r5 = (double) r5;
        r5 = java.lang.Math.ceil(r5);
        r7 = (double) r2;
        r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r9 >= 0) goto L_0x0564;
        r7 = r1.messageLeft;
        r7 = (double) r7;
        r11 = (double) r2;
        r11 = r11 - r5;
        r7 = r7 + r11;
        r7 = (int) r7;
        r1.messageLeft = r7;
        goto L_0x0564;
        r24 = r11;
        r25 = r12;
        r10 = r20;
        r3 = r1.nameLayout;
        if (r3 == 0) goto L_0x0533;
        r3 = r1.nameLayout;
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x0533;
        r3 = r1.nameLayout;
        r5 = 0;
        r3 = r3.getLineRight(r5);
        r6 = (float) r10;
        r6 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));
        if (r6 != 0) goto L_0x0520;
        r6 = r1.nameLayout;
        r6 = r6.getLineWidth(r5);
        r5 = (double) r6;
        r5 = java.lang.Math.ceil(r5);
        r7 = (double) r10;
        r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r9 >= 0) goto L_0x0520;
        r7 = r1.nameLeft;
        r7 = (double) r7;
        r11 = (double) r10;
        r11 = r11 - r5;
        r7 = r7 - r11;
        r7 = (int) r7;
        r1.nameLeft = r7;
        r5 = r1.drawVerified;
        if (r5 == 0) goto L_0x0533;
        r5 = r1.nameLeft;
        r5 = (float) r5;
        r5 = r5 + r3;
        r6 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r6 = (float) r6;
        r5 = r5 + r6;
        r5 = (int) r5;
        r1.nameMuteLeft = r5;
        r3 = r1.messageLayout;
        if (r3 == 0) goto L_0x0564;
        r3 = r1.messageLayout;
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x0564;
        r3 = r1.messageLayout;
        r5 = 0;
        r3 = r3.getLineRight(r5);
        r6 = (float) r2;
        r6 = (r3 > r6 ? 1 : (r3 == r6 ? 0 : -1));
        if (r6 != 0) goto L_0x0564;
        r6 = r1.messageLayout;
        r5 = r6.getLineWidth(r5);
        r5 = (double) r5;
        r5 = java.lang.Math.ceil(r5);
        r7 = (double) r2;
        r9 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r9 >= 0) goto L_0x0564;
        r7 = r1.messageLeft;
        r7 = (double) r7;
        r11 = (double) r2;
        r11 = r11 - r5;
        r7 = r7 - r11;
        r7 = (int) r7;
        r1.messageLeft = r7;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.DialogMeUrlCell.buildLayout():void");
    }

    public DialogMeUrlCell(Context context) {
        super(context);
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(26.0f));
    }

    public void setRecentMeUrl(RecentMeUrl url) {
        this.recentMeUrl = url;
        requestLayout();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.avatarImage.onDetachedFromWindow();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.avatarImage.onAttachedToWindow();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), AndroidUtilities.dp(72.0f) + this.useSeparator);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed) {
            buildLayout();
        }
    }

    public void setDialogSelected(boolean value) {
        if (this.isSelected != value) {
            invalidate();
        }
        this.isSelected = value;
    }

    protected void onDraw(Canvas canvas) {
        if (this.isSelected) {
            canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), Theme.dialogs_tabletSeletedPaint);
        }
        if (this.drawNameLock) {
            BaseCell.setDrawableBounds(Theme.dialogs_lockDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_lockDrawable.draw(canvas);
        } else if (this.drawNameGroup) {
            BaseCell.setDrawableBounds(Theme.dialogs_groupDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_groupDrawable.draw(canvas);
        } else if (this.drawNameBroadcast) {
            BaseCell.setDrawableBounds(Theme.dialogs_broadcastDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_broadcastDrawable.draw(canvas);
        } else if (this.drawNameBot) {
            BaseCell.setDrawableBounds(Theme.dialogs_botDrawable, this.nameLockLeft, this.nameLockTop);
            Theme.dialogs_botDrawable.draw(canvas);
        }
        if (this.nameLayout != null) {
            canvas.save();
            canvas.translate((float) this.nameLeft, (float) AndroidUtilities.dp(13.0f));
            this.nameLayout.draw(canvas);
            canvas.restore();
        }
        if (this.messageLayout != null) {
            canvas.save();
            canvas.translate((float) this.messageLeft, (float) this.messageTop);
            try {
                this.messageLayout.draw(canvas);
            } catch (Throwable e) {
                FileLog.e(e);
            }
            canvas.restore();
        }
        if (this.drawVerified) {
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
            BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
            Theme.dialogs_verifiedDrawable.draw(canvas);
            Theme.dialogs_verifiedCheckDrawable.draw(canvas);
        }
        if (this.useSeparator) {
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, (float) (getMeasuredHeight() - 1), (float) (getMeasuredWidth() - AndroidUtilities.dp((float) AndroidUtilities.leftBaseline)), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
            } else {
                canvas.drawLine((float) AndroidUtilities.dp((float) AndroidUtilities.leftBaseline), (float) (getMeasuredHeight() - 1), (float) getMeasuredWidth(), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
            }
        }
        this.avatarImage.draw(canvas);
    }

    public boolean hasOverlappingRendering() {
        return false;
    }
}
