package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.view.View.MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;

public class ProfileSearchCell extends BaseCell {
    private AvatarDrawable avatarDrawable;
    private ImageReceiver avatarImage = new ImageReceiver(this);
    private Chat chat;
    private StaticLayout countLayout;
    private int countLeft;
    private int countTop = AndroidUtilities.dp(25.0f);
    private int countWidth;
    private int currentAccount = UserConfig.selectedAccount;
    private CharSequence currentName;
    private long dialog_id;
    private boolean drawCheck;
    private boolean drawCount;
    private boolean drawNameBot;
    private boolean drawNameBroadcast;
    private boolean drawNameGroup;
    private boolean drawNameLock;
    private EncryptedChat encryptedChat;
    private FileLocation lastAvatar;
    private String lastName;
    private int lastStatus;
    private int lastUnreadCount;
    private StaticLayout nameLayout;
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameTop;
    private int nameWidth;
    private StaticLayout onlineLayout;
    private int onlineLeft;
    private int paddingRight;
    private RectF rect = new RectF();
    private boolean savedMessages;
    private CharSequence subLabel;
    public boolean useSeparator;
    private User user;

    public void buildLayout() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ProfileSearchCell.buildLayout():void
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
        r0 = r25;
        r1 = 0;
        r0.drawNameBroadcast = r1;
        r0.drawNameLock = r1;
        r0.drawNameGroup = r1;
        r0.drawCheck = r1;
        r0.drawNameBot = r1;
        r2 = r0.encryptedChat;
        r3 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r4 = 32;
        r5 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r6 = 1;
        if (r2 == 0) goto L_0x0067;
    L_0x0018:
        r0.drawNameLock = r6;
        r2 = r0.encryptedChat;
        r2 = r2.id;
        r7 = (long) r2;
        r7 = r7 << r4;
        r0.dialog_id = r7;
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 != 0) goto L_0x0042;
    L_0x0026:
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r0.nameLockLeft = r2;
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = r2 + 4;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r7 = org.telegram.ui.ActionBar.Theme.dialogs_lockDrawable;
        r7 = r7.getIntrinsicWidth();
        r2 = r2 + r7;
        r0.nameLeft = r2;
        goto L_0x005f;
    L_0x0042:
        r2 = r25.getMeasuredWidth();
        r7 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r7 = r7 + 2;
        r7 = (float) r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r2 = r2 - r7;
        r7 = org.telegram.ui.ActionBar.Theme.dialogs_lockDrawable;
        r7 = r7.getIntrinsicWidth();
        r2 = r2 - r7;
        r0.nameLockLeft = r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r0.nameLeft = r2;
    L_0x005f:
        r2 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r0.nameLockTop = r2;
        goto L_0x017e;
    L_0x0067:
        r2 = r0.chat;
        if (r2 == 0) goto L_0x0105;
    L_0x006b:
        r2 = r0.chat;
        r2 = r2.id;
        r3 = 1105461248; // 0x41e40000 float:28.5 double:5.461704254E-315;
        if (r2 >= 0) goto L_0x0086;
    L_0x0073:
        r2 = r0.chat;
        r2 = r2.id;
        r7 = org.telegram.messenger.AndroidUtilities.makeBroadcastId(r2);
        r0.dialog_id = r7;
        r0.drawNameBroadcast = r6;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r0.nameLockTop = r2;
        goto L_0x00af;
    L_0x0086:
        r2 = r0.chat;
        r2 = r2.id;
        r2 = -r2;
        r7 = (long) r2;
        r0.dialog_id = r7;
        r2 = r0.chat;
        r2 = org.telegram.messenger.ChatObject.isChannel(r2);
        if (r2 == 0) goto L_0x00a5;
    L_0x0096:
        r2 = r0.chat;
        r2 = r2.megagroup;
        if (r2 != 0) goto L_0x00a5;
    L_0x009c:
        r0.drawNameBroadcast = r6;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r0.nameLockTop = r2;
        goto L_0x00af;
    L_0x00a5:
        r0.drawNameGroup = r6;
        r2 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r0.nameLockTop = r2;
    L_0x00af:
        r2 = r0.chat;
        r2 = r2.verified;
        r0.drawCheck = r2;
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 != 0) goto L_0x00de;
    L_0x00b9:
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r0.nameLockLeft = r2;
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = r2 + 4;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r3 = r0.drawNameGroup;
        if (r3 == 0) goto L_0x00d6;
    L_0x00cf:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
    L_0x00d1:
        r3 = r3.getIntrinsicWidth();
        goto L_0x00d9;
    L_0x00d6:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x00d1;
    L_0x00d9:
        r2 = r2 + r3;
        r0.nameLeft = r2;
        goto L_0x017e;
    L_0x00de:
        r2 = r25.getMeasuredWidth();
        r3 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r3 = r3 + 2;
        r3 = (float) r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r3 = r0.drawNameGroup;
        if (r3 == 0) goto L_0x00f7;
    L_0x00f0:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
    L_0x00f2:
        r3 = r3.getIntrinsicWidth();
        goto L_0x00fa;
    L_0x00f7:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x00f2;
    L_0x00fa:
        r2 = r2 - r3;
        r0.nameLockLeft = r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r0.nameLeft = r2;
        goto L_0x017e;
    L_0x0105:
        r2 = r0.user;
        if (r2 == 0) goto L_0x017e;
    L_0x0109:
        r2 = r0.user;
        r2 = r2.id;
        r7 = (long) r2;
        r0.dialog_id = r7;
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 != 0) goto L_0x011e;
    L_0x0114:
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r0.nameLeft = r2;
        goto L_0x0124;
    L_0x011e:
        r2 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r0.nameLeft = r2;
    L_0x0124:
        r2 = r0.user;
        r2 = r2.bot;
        if (r2 == 0) goto L_0x0170;
    L_0x012a:
        r0.drawNameBot = r6;
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 != 0) goto L_0x014c;
    L_0x0130:
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r0.nameLockLeft = r2;
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = r2 + 4;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r7 = org.telegram.ui.ActionBar.Theme.dialogs_botDrawable;
        r7 = r7.getIntrinsicWidth();
        r2 = r2 + r7;
        r0.nameLeft = r2;
        goto L_0x0169;
    L_0x014c:
        r2 = r25.getMeasuredWidth();
        r7 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r7 = r7 + 2;
        r7 = (float) r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r2 = r2 - r7;
        r7 = org.telegram.ui.ActionBar.Theme.dialogs_botDrawable;
        r7 = r7.getIntrinsicWidth();
        r2 = r2 - r7;
        r0.nameLockLeft = r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r0.nameLeft = r2;
    L_0x0169:
        r2 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r0.nameLockTop = r2;
        goto L_0x0178;
    L_0x0170:
        r2 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r0.nameLockTop = r2;
    L_0x0178:
        r2 = r0.user;
        r2 = r2.verified;
        r0.drawCheck = r2;
    L_0x017e:
        r2 = r0.currentName;
        if (r2 == 0) goto L_0x0185;
    L_0x0182:
        r2 = r0.currentName;
        goto L_0x01a0;
    L_0x0185:
        r2 = "";
        r3 = r0.chat;
        if (r3 == 0) goto L_0x0190;
    L_0x018b:
        r3 = r0.chat;
        r2 = r3.title;
        goto L_0x019a;
    L_0x0190:
        r3 = r0.user;
        if (r3 == 0) goto L_0x019a;
    L_0x0194:
        r3 = r0.user;
        r2 = org.telegram.messenger.UserObject.getUserName(r3);
    L_0x019a:
        r3 = 10;
        r2 = r2.replace(r3, r4);
    L_0x01a0:
        r3 = r2.length();
        if (r3 != 0) goto L_0x01e1;
    L_0x01a6:
        r3 = r0.user;
        if (r3 == 0) goto L_0x01d8;
    L_0x01aa:
        r3 = r0.user;
        r3 = r3.phone;
        if (r3 == 0) goto L_0x01d8;
    L_0x01b0:
        r3 = r0.user;
        r3 = r3.phone;
        r3 = r3.length();
        if (r3 == 0) goto L_0x01d8;
    L_0x01ba:
        r3 = org.telegram.PhoneFormat.PhoneFormat.getInstance();
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r7 = "+";
        r4.append(r7);
        r7 = r0.user;
        r7 = r7.phone;
        r4.append(r7);
        r4 = r4.toString();
        r2 = r3.format(r4);
        goto L_0x01e1;
    L_0x01d8:
        r3 = "HiddenName";
        r4 = 2131493648; // 0x7f0c0310 float:1.8610782E38 double:1.053097786E-314;
        r2 = org.telegram.messenger.LocaleController.getString(r3, r4);
    L_0x01e1:
        r3 = r0.encryptedChat;
        if (r3 == 0) goto L_0x01e8;
    L_0x01e5:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_nameEncryptedPaint;
        goto L_0x01ea;
    L_0x01e8:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_namePaint;
    L_0x01ea:
        r4 = org.telegram.messenger.LocaleController.isRTL;
        if (r4 != 0) goto L_0x01ff;
    L_0x01ee:
        r4 = r25.getMeasuredWidth();
        r7 = r0.nameLeft;
        r4 = r4 - r7;
        r7 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r4 = r4 - r7;
        r0.nameWidth = r4;
        goto L_0x0210;
    L_0x01ff:
        r4 = r25.getMeasuredWidth();
        r7 = r0.nameLeft;
        r4 = r4 - r7;
        r7 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r7 = (float) r7;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r4 = r4 - r7;
        r0.nameWidth = r4;
    L_0x0210:
        r7 = r0.drawNameLock;
        r8 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        if (r7 == 0) goto L_0x0227;
    L_0x0216:
        r7 = r0.nameWidth;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_lockDrawable;
        r9 = r9.getIntrinsicWidth();
        r8 = r8 + r9;
        r7 = r7 - r8;
        r0.nameWidth = r7;
        goto L_0x0265;
    L_0x0227:
        r7 = r0.drawNameBroadcast;
        if (r7 == 0) goto L_0x023c;
    L_0x022b:
        r7 = r0.nameWidth;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        r9 = r9.getIntrinsicWidth();
        r8 = r8 + r9;
        r7 = r7 - r8;
        r0.nameWidth = r7;
        goto L_0x0265;
    L_0x023c:
        r7 = r0.drawNameGroup;
        if (r7 == 0) goto L_0x0251;
    L_0x0240:
        r7 = r0.nameWidth;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
        r9 = r9.getIntrinsicWidth();
        r8 = r8 + r9;
        r7 = r7 - r8;
        r0.nameWidth = r7;
        goto L_0x0265;
    L_0x0251:
        r7 = r0.drawNameBot;
        if (r7 == 0) goto L_0x0265;
    L_0x0255:
        r7 = r0.nameWidth;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_botDrawable;
        r9 = r9.getIntrinsicWidth();
        r8 = r8 + r9;
        r7 = r7 - r8;
        r0.nameWidth = r7;
    L_0x0265:
        r7 = r0.nameWidth;
        r8 = r0.paddingRight;
        r7 = r7 - r8;
        r0.nameWidth = r7;
        r7 = r0.paddingRight;
        r4 = r4 - r7;
        r7 = r0.drawCount;
        r15 = 0;
        r14 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        if (r7 == 0) goto L_0x0303;
    L_0x0276:
        r7 = r0.currentAccount;
        r7 = org.telegram.messenger.MessagesController.getInstance(r7);
        r7 = r7.dialogs_dict;
        r8 = r0.dialog_id;
        r7 = r7.get(r8);
        r7 = (org.telegram.tgnet.TLRPC.TL_dialog) r7;
        if (r7 == 0) goto L_0x02fe;
    L_0x0288:
        r8 = r7.unread_count;
        if (r8 == 0) goto L_0x02fe;
    L_0x028c:
        r8 = r7.unread_count;
        r0.lastUnreadCount = r8;
        r8 = "%d";
        r6 = new java.lang.Object[r6];
        r9 = r7.unread_count;
        r9 = java.lang.Integer.valueOf(r9);
        r6[r1] = r9;
        r6 = java.lang.String.format(r8, r6);
        r8 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_countTextPaint;
        r9 = r9.measureText(r6);
        r9 = (double) r9;
        r9 = java.lang.Math.ceil(r9);
        r9 = (int) r9;
        r8 = java.lang.Math.max(r8, r9);
        r0.countWidth = r8;
        r8 = new android.text.StaticLayout;
        r18 = org.telegram.ui.ActionBar.Theme.dialogs_countTextPaint;
        r9 = r0.countWidth;
        r20 = android.text.Layout.Alignment.ALIGN_CENTER;
        r21 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r22 = 0;
        r23 = 0;
        r16 = r8;
        r17 = r6;
        r19 = r9;
        r16.<init>(r17, r18, r19, r20, r21, r22, r23);
        r0.countLayout = r8;
        r8 = r0.countWidth;
        r9 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r8 = r8 + r9;
        r9 = r0.nameWidth;
        r9 = r9 - r8;
        r0.nameWidth = r9;
        r9 = org.telegram.messenger.LocaleController.isRTL;
        r10 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        if (r9 != 0) goto L_0x02f2;
    L_0x02e3:
        r9 = r25.getMeasuredWidth();
        r11 = r0.countWidth;
        r9 = r9 - r11;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r9 = r9 - r10;
        r0.countLeft = r9;
        goto L_0x02fd;
    L_0x02f2:
        r9 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r0.countLeft = r9;
        r9 = r0.nameLeft;
        r9 = r9 + r8;
        r0.nameLeft = r9;
    L_0x02fd:
        goto L_0x0302;
    L_0x02fe:
        r0.lastUnreadCount = r1;
        r0.countLayout = r15;
    L_0x0302:
        goto L_0x0307;
    L_0x0303:
        r0.lastUnreadCount = r1;
        r0.countLayout = r15;
    L_0x0307:
        r6 = r0.nameWidth;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r14);
        r6 = r6 - r7;
        r6 = (float) r6;
        r7 = android.text.TextUtils.TruncateAt.END;
        r6 = android.text.TextUtils.ellipsize(r2, r3, r6, r7);
        r13 = new android.text.StaticLayout;
        r10 = r0.nameWidth;
        r11 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r12 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r16 = 0;
        r17 = 0;
        r7 = r13;
        r8 = r6;
        r9 = r3;
        r1 = r13;
        r13 = r16;
        r14 = r17;
        r7.<init>(r8, r9, r10, r11, r12, r13, r14);
        r0.nameLayout = r1;
        r1 = r0.chat;
        r8 = 1103626240; // 0x41c80000 float:25.0 double:5.45263811E-315;
        if (r1 == 0) goto L_0x0345;
    L_0x0334:
        r1 = r0.subLabel;
        if (r1 == 0) goto L_0x0339;
    L_0x0338:
        goto L_0x0345;
    L_0x0339:
        r0.onlineLayout = r15;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r0.nameTop = r1;
        r7 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        goto L_0x0429;
    L_0x0345:
        r1 = org.telegram.messenger.LocaleController.isRTL;
        if (r1 != 0) goto L_0x0353;
    L_0x0349:
        r1 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r1 = (float) r1;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r0.onlineLeft = r1;
        goto L_0x0359;
    L_0x0353:
        r1 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r0.onlineLeft = r1;
    L_0x0359:
        r1 = "";
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_offlinePaint;
        r9 = r0.subLabel;
        if (r9 == 0) goto L_0x0365;
    L_0x0361:
        r1 = r0.subLabel;
        goto L_0x03d5;
    L_0x0365:
        r9 = r0.user;
        if (r9 == 0) goto L_0x03d5;
    L_0x0369:
        r9 = r0.user;
        r9 = r9.bot;
        if (r9 == 0) goto L_0x0379;
    L_0x036f:
        r9 = "Bot";
        r10 = 2131493086; // 0x7f0c00de float:1.8609642E38 double:1.053097508E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r9, r10);
        goto L_0x03d5;
    L_0x0379:
        r9 = r0.user;
        r9 = r9.id;
        r10 = 333000; // 0x514c8 float:4.66632E-40 double:1.64524E-318;
        if (r9 == r10) goto L_0x03cc;
    L_0x0382:
        r9 = r0.user;
        r9 = r9.id;
        r10 = 777000; // 0xbdb28 float:1.088809E-39 double:3.83889E-318;
        if (r9 != r10) goto L_0x038c;
    L_0x038b:
        goto L_0x03cc;
    L_0x038c:
        r9 = r0.currentAccount;
        r10 = r0.user;
        r1 = org.telegram.messenger.LocaleController.formatUserStatus(r9, r10);
        r9 = r0.user;
        if (r9 == 0) goto L_0x03d5;
    L_0x0398:
        r9 = r0.user;
        r9 = r9.id;
        r10 = r0.currentAccount;
        r10 = org.telegram.messenger.UserConfig.getInstance(r10);
        r10 = r10.getClientUserId();
        if (r9 == r10) goto L_0x03c0;
    L_0x03a8:
        r9 = r0.user;
        r9 = r9.status;
        if (r9 == 0) goto L_0x03d5;
    L_0x03ae:
        r9 = r0.user;
        r9 = r9.status;
        r9 = r9.expires;
        r10 = r0.currentAccount;
        r10 = org.telegram.tgnet.ConnectionsManager.getInstance(r10);
        r10 = r10.getCurrentTime();
        if (r9 <= r10) goto L_0x03d5;
    L_0x03c0:
        r5 = org.telegram.ui.ActionBar.Theme.dialogs_onlinePaint;
        r9 = "Online";
        r10 = 2131494030; // 0x7f0c048e float:1.8611557E38 double:1.0530979745E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r9, r10);
        goto L_0x03d5;
    L_0x03cc:
        r9 = "ServiceNotifications";
        r10 = 2131494365; // 0x7f0c05dd float:1.8612236E38 double:1.05309814E-314;
        r1 = org.telegram.messenger.LocaleController.getString(r9, r10);
    L_0x03d5:
        r9 = r0.savedMessages;
        if (r9 == 0) goto L_0x03e4;
    L_0x03d9:
        r0.onlineLayout = r15;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r0.nameTop = r8;
        r7 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        goto L_0x0428;
    L_0x03e4:
        r15 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r8 = r4 - r8;
        r8 = (float) r8;
        r9 = android.text.TextUtils.TruncateAt.END;
        r16 = android.text.TextUtils.ellipsize(r1, r5, r8, r9);
        r14 = new android.text.StaticLayout;
        r12 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r17 = 0;
        r18 = 0;
        r8 = r14;
        r9 = r16;
        r10 = r5;
        r11 = r4;
        r7 = r14;
        r14 = r17;
        r24 = r1;
        r1 = r15;
        r15 = r18;
        r8.<init>(r9, r10, r11, r12, r13, r14, r15);
        r0.onlineLayout = r7;
        r7 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r0.nameTop = r8;
        r8 = r0.subLabel;
        if (r8 == 0) goto L_0x0428;
        r8 = r0.chat;
        if (r8 == 0) goto L_0x0428;
        r8 = r0.nameLockTop;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r8 = r8 - r1;
        r0.nameLockTop = r8;
    L_0x0429:
        r1 = org.telegram.messenger.LocaleController.isRTL;
        if (r1 != 0) goto L_0x043b;
        r1 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r1 == 0) goto L_0x0434;
        goto L_0x0436;
        r7 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r7);
        goto L_0x044f;
        r1 = r25.getMeasuredWidth();
        r5 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r5 == 0) goto L_0x0448;
        r5 = 1115815936; // 0x42820000 float:65.0 double:5.51286321E-315;
        goto L_0x044a;
        r5 = 1114898432; // 0x42740000 float:61.0 double:5.50833014E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r1 = r1 - r5;
        r5 = r0.avatarImage;
        r7 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r8 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5.setImageCoords(r1, r7, r9, r8);
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 == 0) goto L_0x04cb;
        r5 = r0.nameLayout;
        r5 = r5.getLineCount();
        if (r5 <= 0) goto L_0x0499;
        r5 = r0.nameLayout;
        r7 = 0;
        r5 = r5.getLineLeft(r7);
        r8 = 0;
        r8 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x0499;
        r8 = r0.nameLayout;
        r8 = r8.getLineWidth(r7);
        r7 = (double) r8;
        r7 = java.lang.Math.ceil(r7);
        r9 = r0.nameWidth;
        r9 = (double) r9;
        r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r11 >= 0) goto L_0x0499;
        r9 = r0.nameLeft;
        r9 = (double) r9;
        r11 = r0.nameWidth;
        r11 = (double) r11;
        r11 = r11 - r7;
        r9 = r9 + r11;
        r9 = (int) r9;
        r0.nameLeft = r9;
        r5 = r0.onlineLayout;
        if (r5 == 0) goto L_0x052f;
        r5 = r0.onlineLayout;
        r5 = r5.getLineCount();
        if (r5 <= 0) goto L_0x052f;
        r5 = r0.onlineLayout;
        r7 = 0;
        r5 = r5.getLineLeft(r7);
        r8 = 0;
        r8 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x052f;
        r8 = r0.onlineLayout;
        r7 = r8.getLineWidth(r7);
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        r9 = (double) r4;
        r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r11 >= 0) goto L_0x052f;
        r9 = r0.onlineLeft;
        r9 = (double) r9;
        r11 = (double) r4;
        r11 = r11 - r7;
        r9 = r9 + r11;
        r9 = (int) r9;
        r0.onlineLeft = r9;
        goto L_0x052f;
        r5 = r0.nameLayout;
        r5 = r5.getLineCount();
        if (r5 <= 0) goto L_0x04fe;
        r5 = r0.nameLayout;
        r7 = 0;
        r5 = r5.getLineRight(r7);
        r8 = r0.nameWidth;
        r8 = (float) r8;
        r8 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x04fe;
        r8 = r0.nameLayout;
        r8 = r8.getLineWidth(r7);
        r7 = (double) r8;
        r7 = java.lang.Math.ceil(r7);
        r9 = r0.nameWidth;
        r9 = (double) r9;
        r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r11 >= 0) goto L_0x04fe;
        r9 = r0.nameLeft;
        r9 = (double) r9;
        r11 = r0.nameWidth;
        r11 = (double) r11;
        r11 = r11 - r7;
        r9 = r9 - r11;
        r9 = (int) r9;
        r0.nameLeft = r9;
        r5 = r0.onlineLayout;
        if (r5 == 0) goto L_0x052f;
        r5 = r0.onlineLayout;
        r5 = r5.getLineCount();
        if (r5 <= 0) goto L_0x052f;
        r5 = r0.onlineLayout;
        r7 = 0;
        r5 = r5.getLineRight(r7);
        r8 = (float) r4;
        r8 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x052f;
        r8 = r0.onlineLayout;
        r7 = r8.getLineWidth(r7);
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        r9 = (double) r4;
        r11 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r11 >= 0) goto L_0x052f;
        r9 = r0.onlineLeft;
        r9 = (double) r9;
        r11 = (double) r4;
        r11 = r11 - r7;
        r9 = r9 - r11;
        r9 = (int) r9;
        r0.onlineLeft = r9;
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 == 0) goto L_0x0541;
        r5 = r0.nameLeft;
        r7 = r0.paddingRight;
        r5 = r5 + r7;
        r0.nameLeft = r5;
        r5 = r0.onlineLeft;
        r7 = r0.paddingRight;
        r5 = r5 + r7;
        r0.onlineLeft = r5;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ProfileSearchCell.buildLayout():void");
    }

    public ProfileSearchCell(Context context) {
        super(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(26.0f));
        this.avatarDrawable = new AvatarDrawable();
    }

    public void setData(TLObject object, EncryptedChat ec, CharSequence n, CharSequence s, boolean needCount, boolean saved) {
        this.currentName = n;
        if (object instanceof User) {
            this.user = (User) object;
            this.chat = null;
        } else if (object instanceof Chat) {
            this.chat = (Chat) object;
            this.user = null;
        }
        this.encryptedChat = ec;
        this.subLabel = s;
        this.drawCount = needCount;
        this.savedMessages = saved;
        update(0);
    }

    public void setPaddingRight(int padding) {
        this.paddingRight = padding;
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
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), AndroidUtilities.dp(72.0f));
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!(this.user == null && this.chat == null && this.encryptedChat == null) && changed) {
            buildLayout();
        }
    }

    public void update(int mask) {
        TLObject photo = null;
        if (this.user != null) {
            this.avatarDrawable.setInfo(this.user);
            if (this.savedMessages) {
                this.avatarDrawable.setSavedMessages(1);
            } else if (this.user.photo != null) {
                photo = this.user.photo.photo_small;
            }
        } else if (this.chat != null) {
            if (this.chat.photo != null) {
                photo = this.chat.photo.photo_small;
            }
            this.avatarDrawable.setInfo(this.chat);
        } else {
            this.avatarDrawable.setInfo(0, null, null, false);
        }
        if (mask != 0) {
            boolean continueUpdate = false;
            if (!(((mask & 2) == 0 || this.user == null) && ((mask & 8) == 0 || this.chat == null)) && ((this.lastAvatar != null && photo == null) || !(this.lastAvatar != null || photo == null || this.lastAvatar == null || photo == null || (this.lastAvatar.volume_id == photo.volume_id && this.lastAvatar.local_id == photo.local_id)))) {
                continueUpdate = true;
            }
            if (!(continueUpdate || (mask & 4) == 0 || this.user == null)) {
                int newStatus = 0;
                if (this.user.status != null) {
                    newStatus = this.user.status.expires;
                }
                if (newStatus != this.lastStatus) {
                    continueUpdate = true;
                }
            }
            if (!((continueUpdate || (mask & 1) == 0 || this.user == null) && ((mask & 16) == 0 || this.chat == null))) {
                String newName;
                if (this.user != null) {
                    newName = new StringBuilder();
                    newName.append(this.user.first_name);
                    newName.append(this.user.last_name);
                    newName = newName.toString();
                } else {
                    newName = this.chat.title;
                }
                if (!newName.equals(this.lastName)) {
                    continueUpdate = true;
                }
            }
            if (!(continueUpdate || !this.drawCount || (mask & 256) == 0)) {
                TL_dialog dialog = (TL_dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.dialog_id);
                if (!(dialog == null || dialog.unread_count == this.lastUnreadCount)) {
                    continueUpdate = true;
                }
            }
            if (!continueUpdate) {
                return;
            }
        }
        if (this.user != null) {
            if (this.user.status != null) {
                this.lastStatus = this.user.status.expires;
            } else {
                this.lastStatus = 0;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.user.first_name);
            stringBuilder.append(this.user.last_name);
            this.lastName = stringBuilder.toString();
        } else if (this.chat != null) {
            this.lastName = this.chat.title;
        }
        this.lastAvatar = photo;
        this.avatarImage.setImage(photo, "50_50", this.avatarDrawable, null, 0);
        if (getMeasuredWidth() == 0) {
            if (getMeasuredHeight() == 0) {
                requestLayout();
                postInvalidate();
            }
        }
        buildLayout();
        postInvalidate();
    }

    protected void onDraw(Canvas canvas) {
        if (this.user != null || this.chat != null || this.encryptedChat != null) {
            int x;
            if (this.useSeparator) {
                if (LocaleController.isRTL) {
                    canvas.drawLine(0.0f, (float) (getMeasuredHeight() - 1), (float) (getMeasuredWidth() - AndroidUtilities.dp((float) AndroidUtilities.leftBaseline)), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
                } else {
                    canvas.drawLine((float) AndroidUtilities.dp((float) AndroidUtilities.leftBaseline), (float) (getMeasuredHeight() - 1), (float) getMeasuredWidth(), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
                }
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
                canvas.translate((float) this.nameLeft, (float) this.nameTop);
                this.nameLayout.draw(canvas);
                canvas.restore();
                if (this.drawCheck) {
                    if (!LocaleController.isRTL) {
                        x = (int) ((((float) this.nameLeft) + this.nameLayout.getLineRight(0)) + ((float) AndroidUtilities.dp(6.0f)));
                    } else if (this.nameLayout.getLineLeft(0) == 0.0f) {
                        x = (this.nameLeft - AndroidUtilities.dp(6.0f)) - Theme.dialogs_verifiedDrawable.getIntrinsicWidth();
                    } else {
                        x = (int) (((((double) (this.nameLeft + this.nameWidth)) - Math.ceil((double) this.nameLayout.getLineWidth(0))) - ((double) AndroidUtilities.dp(6.0f))) - ((double) Theme.dialogs_verifiedDrawable.getIntrinsicWidth()));
                    }
                    BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, x, this.nameLockTop);
                    BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, x, this.nameLockTop);
                    Theme.dialogs_verifiedDrawable.draw(canvas);
                    Theme.dialogs_verifiedCheckDrawable.draw(canvas);
                }
            }
            if (this.onlineLayout != null) {
                canvas.save();
                canvas.translate((float) this.onlineLeft, (float) AndroidUtilities.dp(40.0f));
                this.onlineLayout.draw(canvas);
                canvas.restore();
            }
            if (this.countLayout != null) {
                x = this.countLeft - AndroidUtilities.dp(5.5f);
                this.rect.set((float) x, (float) this.countTop, (float) ((this.countWidth + x) + AndroidUtilities.dp(11.0f)), (float) (this.countTop + AndroidUtilities.dp(23.0f)));
                canvas.drawRoundRect(this.rect, AndroidUtilities.density * 11.5f, 11.5f * AndroidUtilities.density, MessagesController.getInstance(this.currentAccount).isDialogMuted(this.dialog_id) ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint);
                canvas.save();
                canvas.translate((float) this.countLeft, (float) (this.countTop + AndroidUtilities.dp(4.0f)));
                this.countLayout.draw(canvas);
                canvas.restore();
            }
            this.avatarImage.draw(canvas);
        }
    }
}
