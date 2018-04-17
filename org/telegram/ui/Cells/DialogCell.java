package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.view.View.MeasureSpec;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DataQuery;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.DraftMessage;
import org.telegram.tgnet.TLRPC.EncryptedChat;
import org.telegram.tgnet.TLRPC.TL_dialog;
import org.telegram.tgnet.TLRPC.User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.GroupCreateCheckBox;

public class DialogCell extends BaseCell {
    private AvatarDrawable avatarDrawable = new AvatarDrawable();
    private ImageReceiver avatarImage = new ImageReceiver(this);
    private int avatarTop = AndroidUtilities.dp(10.0f);
    private Chat chat = null;
    private GroupCreateCheckBox checkBox;
    private int checkDrawLeft;
    private int checkDrawTop = AndroidUtilities.dp(18.0f);
    private StaticLayout countLayout;
    private int countLeft;
    private int countTop = AndroidUtilities.dp(39.0f);
    private int countWidth;
    private int currentAccount = UserConfig.selectedAccount;
    private long currentDialogId;
    private int currentEditDate;
    private CustomDialog customDialog;
    private boolean dialogMuted;
    private int dialogsType;
    private DraftMessage draftMessage;
    private boolean drawCheck1;
    private boolean drawCheck2;
    private boolean drawClock;
    private boolean drawCount;
    private boolean drawError;
    private boolean drawMention;
    private boolean drawNameBot;
    private boolean drawNameBroadcast;
    private boolean drawNameGroup;
    private boolean drawNameLock;
    private boolean drawPin;
    private boolean drawPinBackground;
    private boolean drawVerified;
    private EncryptedChat encryptedChat = null;
    private int errorLeft;
    private int errorTop = AndroidUtilities.dp(39.0f);
    private int halfCheckDrawLeft;
    private int index;
    private boolean isDialogCell;
    private boolean isSelected;
    private int lastMessageDate;
    private CharSequence lastMessageString;
    private CharSequence lastPrintString = null;
    private int lastSendState;
    private boolean lastUnreadState;
    private int mentionCount;
    private int mentionLeft;
    private int mentionWidth;
    private MessageObject message;
    private StaticLayout messageLayout;
    private int messageLeft;
    private int messageTop = AndroidUtilities.dp(40.0f);
    private StaticLayout nameLayout;
    private int nameLeft;
    private int nameLockLeft;
    private int nameLockTop;
    private int nameMuteLeft;
    private int pinLeft;
    private int pinTop = AndroidUtilities.dp(39.0f);
    private RectF rect = new RectF();
    private StaticLayout timeLayout;
    private int timeLeft;
    private int timeTop = AndroidUtilities.dp(17.0f);
    private int unreadCount;
    public boolean useSeparator = false;
    private User user = null;

    public static class CustomDialog {
        public int date;
        public int id;
        public boolean isMedia;
        public String message;
        public boolean muted;
        public String name;
        public boolean pinned;
        public boolean sent;
        public int type;
        public int unread_count;
        public boolean verified;
    }

    public void buildLayout() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.DialogCell.buildLayout():void
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
        r1 = r41;
        r2 = "";
        r3 = "";
        r4 = 0;
        r5 = 0;
        r6 = "";
        r7 = 0;
        r8 = r1.isDialogCell;
        if (r8 == 0) goto L_0x0020;
    L_0x000f:
        r8 = r1.currentAccount;
        r8 = org.telegram.messenger.MessagesController.getInstance(r8);
        r8 = r8.printingStrings;
        r9 = r1.currentDialogId;
        r8 = r8.get(r9);
        r7 = r8;
        r7 = (java.lang.CharSequence) r7;
    L_0x0020:
        r8 = org.telegram.ui.ActionBar.Theme.dialogs_namePaint;
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_messagePaint;
        r10 = 1;
        r11 = 0;
        r1.drawNameGroup = r11;
        r1.drawNameBroadcast = r11;
        r1.drawNameLock = r11;
        r1.drawNameBot = r11;
        r1.drawVerified = r11;
        r1.drawPinBackground = r11;
        r12 = r1.user;
        r12 = org.telegram.messenger.UserObject.isUserSelf(r12);
        r13 = 1;
        r12 = r12 ^ r13;
        r14 = 1;
        r15 = android.os.Build.VERSION.SDK_INT;
        r11 = 18;
        if (r15 < r11) goto L_0x0044;
    L_0x0041:
        r11 = "%s: ⁨%s⁩";
        goto L_0x0046;
    L_0x0044:
        r11 = "%s: %s";
    L_0x0046:
        r15 = r1.message;
        if (r15 == 0) goto L_0x004f;
    L_0x004a:
        r15 = r1.message;
        r15 = r15.messageText;
        goto L_0x0050;
    L_0x004f:
        r15 = 0;
    L_0x0050:
        r1.lastMessageString = r15;
        r15 = r1.customDialog;
        r13 = 2;
        if (r15 == 0) goto L_0x0243;
    L_0x0057:
        r15 = r1.customDialog;
        r15 = r15.type;
        if (r15 != r13) goto L_0x00a8;
    L_0x005d:
        r15 = 1;
        r1.drawNameLock = r15;
        r15 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r1.nameLockTop = r15;
        r15 = org.telegram.messenger.LocaleController.isRTL;
        if (r15 != 0) goto L_0x0089;
    L_0x006c:
        r15 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r15 = (float) r15;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r1.nameLockLeft = r15;
        r15 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r15 = r15 + 4;
        r15 = (float) r15;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13 = org.telegram.ui.ActionBar.Theme.dialogs_lockDrawable;
        r13 = r13.getIntrinsicWidth();
        r15 = r15 + r13;
        r1.nameLeft = r15;
        goto L_0x0123;
    L_0x0089:
        r13 = r41.getMeasuredWidth();
        r15 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r15 = (float) r15;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13 = r13 - r15;
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_lockDrawable;
        r15 = r15.getIntrinsicWidth();
        r13 = r13 - r15;
        r1.nameLockLeft = r13;
        r13 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r1.nameLeft = r15;
        goto L_0x0123;
    L_0x00a8:
        r13 = r1.customDialog;
        r13 = r13.verified;
        r1.drawVerified = r13;
        r13 = r1.customDialog;
        r13 = r13.type;
        r15 = 1;
        if (r13 != r15) goto L_0x010d;
    L_0x00b5:
        r1.drawNameGroup = r15;
        r13 = 1099694080; // 0x418c0000 float:17.5 double:5.43321066E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r1.nameLockTop = r13;
        r13 = org.telegram.messenger.LocaleController.isRTL;
        if (r13 != 0) goto L_0x00e7;
    L_0x00c3:
        r13 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r13 = (float) r13;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r1.nameLockLeft = r13;
        r13 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r13 = r13 + 4;
        r13 = (float) r13;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r15 = r1.drawNameGroup;
        if (r15 == 0) goto L_0x00e0;
    L_0x00d9:
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
    L_0x00db:
        r15 = r15.getIntrinsicWidth();
        goto L_0x00e3;
    L_0x00e0:
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x00db;
    L_0x00e3:
        r13 = r13 + r15;
        r1.nameLeft = r13;
        goto L_0x0123;
    L_0x00e7:
        r13 = r41.getMeasuredWidth();
        r15 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r15 = (float) r15;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13 = r13 - r15;
        r15 = r1.drawNameGroup;
        if (r15 == 0) goto L_0x00fe;
    L_0x00f7:
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
    L_0x00f9:
        r15 = r15.getIntrinsicWidth();
        goto L_0x0101;
    L_0x00fe:
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x00f9;
    L_0x0101:
        r13 = r13 - r15;
        r1.nameLockLeft = r13;
        r13 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r1.nameLeft = r15;
        goto L_0x0123;
    L_0x010d:
        r13 = org.telegram.messenger.LocaleController.isRTL;
        if (r13 != 0) goto L_0x011b;
    L_0x0111:
        r13 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r13 = (float) r13;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r1.nameLeft = r13;
        goto L_0x0123;
    L_0x011b:
        r13 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r1.nameLeft = r15;
    L_0x0123:
        r13 = r1.customDialog;
        r13 = r13.type;
        r15 = 1;
        if (r13 != r15) goto L_0x01da;
    L_0x012a:
        r13 = "FromYou";
        r15 = 2131493622; // 0x7f0c02f6 float:1.861073E38 double:1.053097773E-314;
        r13 = org.telegram.messenger.LocaleController.getString(r13, r15);
        r10 = 0;
        r15 = r1.customDialog;
        r15 = r15.isMedia;
        if (r15 == 0) goto L_0x0177;
    L_0x013a:
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r20 = r2;
        r15 = 2;
        r2 = new java.lang.Object[r15];
        r15 = 0;
        r2[r15] = r13;
        r15 = r1.message;
        r15 = r15.messageText;
        r16 = 1;
        r2[r16] = r15;
        r2 = java.lang.String.format(r11, r2);
        r2 = android.text.SpannableStringBuilder.valueOf(r2);
        r15 = new android.text.style.ForegroundColorSpan;
        r21 = r3;
        r3 = "chats_attachMessage";
        r3 = org.telegram.ui.ActionBar.Theme.getColor(r3);
        r15.<init>(r3);
        r3 = r13.length();
        r18 = 2;
        r3 = r3 + 2;
        r22 = r4;
        r4 = r2.length();
        r23 = r5;
        r5 = 33;
        r2.setSpan(r15, r3, r4, r5);
        goto L_0x01aa;
    L_0x0177:
        r20 = r2;
        r21 = r3;
        r22 = r4;
        r23 = r5;
        r2 = r1.customDialog;
        r2 = r2.message;
        r3 = r2.length();
        r4 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        if (r3 <= r4) goto L_0x0191;
    L_0x018b:
        r3 = 0;
        r2 = r2.substring(r3, r4);
        goto L_0x0192;
    L_0x0191:
        r3 = 0;
    L_0x0192:
        r4 = 2;
        r5 = new java.lang.Object[r4];
        r5[r3] = r13;
        r3 = 10;
        r4 = 32;
        r15 = r2.replace(r3, r4);
        r3 = 1;
        r5[r3] = r15;
        r3 = java.lang.String.format(r11, r5);
        r2 = android.text.SpannableStringBuilder.valueOf(r3);
    L_0x01aa:
        r3 = r2.length();
        if (r3 <= 0) goto L_0x01c8;
    L_0x01b0:
        r3 = new android.text.style.ForegroundColorSpan;
        r4 = "chats_nameMessage";
        r4 = org.telegram.ui.ActionBar.Theme.getColor(r4);
        r3.<init>(r4);
        r4 = r13.length();
        r5 = 1;
        r4 = r4 + r5;
        r5 = 0;
        r15 = 33;
        r2.setSpan(r3, r5, r4, r15);
        goto L_0x01c9;
    L_0x01c8:
        r5 = 0;
    L_0x01c9:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_messagePaint;
        r3 = r3.getFontMetricsInt();
        r4 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r2 = org.telegram.messenger.Emoji.replaceEmoji(r2, r3, r4, r5);
        goto L_0x01ee;
    L_0x01da:
        r20 = r2;
        r21 = r3;
        r22 = r4;
        r23 = r5;
        r2 = r1.customDialog;
        r2 = r2.message;
        r3 = r1.customDialog;
        r3 = r3.isMedia;
        if (r3 == 0) goto L_0x01ee;
    L_0x01ec:
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_messagePrintingPaint;
    L_0x01ee:
        r3 = r1.customDialog;
        r3 = r3.date;
        r3 = (long) r3;
        r3 = org.telegram.messenger.LocaleController.stringForMessageListDate(r3);
        r4 = r1.customDialog;
        r4 = r4.unread_count;
        if (r4 == 0) goto L_0x0214;
    L_0x01fd:
        r4 = 1;
        r1.drawCount = r4;
        r5 = "%d";
        r6 = new java.lang.Object[r4];
        r4 = r1.customDialog;
        r4 = r4.unread_count;
        r4 = java.lang.Integer.valueOf(r4);
        r13 = 0;
        r6[r13] = r4;
        r4 = java.lang.String.format(r5, r6);
        goto L_0x0219;
    L_0x0214:
        r13 = 0;
        r1.drawCount = r13;
        r4 = r22;
    L_0x0219:
        r5 = r1.customDialog;
        r5 = r5.sent;
        if (r5 == 0) goto L_0x0229;
    L_0x021f:
        r5 = 1;
        r1.drawCheck1 = r5;
        r1.drawCheck2 = r5;
        r1.drawClock = r13;
        r1.drawError = r13;
        goto L_0x0231;
    L_0x0229:
        r1.drawCheck1 = r13;
        r1.drawCheck2 = r13;
        r1.drawClock = r13;
        r1.drawError = r13;
    L_0x0231:
        r5 = r1.customDialog;
        r5 = r5.name;
        r6 = r1.customDialog;
        r6 = r6.type;
        r13 = 2;
        if (r6 != r13) goto L_0x023e;
    L_0x023c:
        r8 = org.telegram.ui.ActionBar.Theme.dialogs_nameEncryptedPaint;
    L_0x023e:
        r6 = r2;
        r2 = r23;
        goto L_0x0a49;
    L_0x0243:
        r20 = r2;
        r21 = r3;
        r22 = r4;
        r23 = r5;
        r2 = r1.encryptedChat;
        if (r2 == 0) goto L_0x029a;
    L_0x024f:
        r2 = 1;
        r1.drawNameLock = r2;
        r2 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLockTop = r2;
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 != 0) goto L_0x027b;
    L_0x025e:
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLockLeft = r2;
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = r2 + 4;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_lockDrawable;
        r3 = r3.getIntrinsicWidth();
        r2 = r2 + r3;
        r1.nameLeft = r2;
        goto L_0x038d;
    L_0x027b:
        r2 = r41.getMeasuredWidth();
        r3 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r3 = (float) r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_lockDrawable;
        r3 = r3.getIntrinsicWidth();
        r2 = r2 - r3;
        r1.nameLockLeft = r2;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLeft = r3;
        goto L_0x038d;
    L_0x029a:
        r2 = r1.chat;
        if (r2 == 0) goto L_0x031f;
    L_0x029e:
        r2 = r1.chat;
        r2 = r2.id;
        if (r2 < 0) goto L_0x02bf;
    L_0x02a4:
        r2 = r1.chat;
        r2 = org.telegram.messenger.ChatObject.isChannel(r2);
        if (r2 == 0) goto L_0x02b3;
    L_0x02ac:
        r2 = r1.chat;
        r2 = r2.megagroup;
        if (r2 != 0) goto L_0x02b3;
    L_0x02b2:
        goto L_0x02bf;
    L_0x02b3:
        r2 = 1;
        r1.drawNameGroup = r2;
        r3 = 1099694080; // 0x418c0000 float:17.5 double:5.43321066E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r1.nameLockTop = r3;
        goto L_0x02ca;
    L_0x02bf:
        r2 = 1;
        r1.drawNameBroadcast = r2;
        r2 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLockTop = r2;
    L_0x02ca:
        r2 = r1.chat;
        r2 = r2.verified;
        r1.drawVerified = r2;
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 != 0) goto L_0x02f9;
    L_0x02d4:
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLockLeft = r2;
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = r2 + 4;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r3 = r1.drawNameGroup;
        if (r3 == 0) goto L_0x02f1;
    L_0x02ea:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
    L_0x02ec:
        r3 = r3.getIntrinsicWidth();
        goto L_0x02f4;
    L_0x02f1:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x02ec;
    L_0x02f4:
        r2 = r2 + r3;
        r1.nameLeft = r2;
        goto L_0x038d;
    L_0x02f9:
        r2 = r41.getMeasuredWidth();
        r3 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r3 = (float) r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r3 = r1.drawNameGroup;
        if (r3 == 0) goto L_0x0310;
    L_0x0309:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
    L_0x030b:
        r3 = r3.getIntrinsicWidth();
        goto L_0x0313;
    L_0x0310:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        goto L_0x030b;
    L_0x0313:
        r2 = r2 - r3;
        r1.nameLockLeft = r2;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLeft = r3;
        goto L_0x038d;
    L_0x031f:
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 != 0) goto L_0x032d;
    L_0x0323:
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLeft = r2;
        goto L_0x0335;
    L_0x032d:
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLeft = r3;
    L_0x0335:
        r2 = r1.user;
        if (r2 == 0) goto L_0x038d;
    L_0x0339:
        r2 = r1.user;
        r2 = r2.bot;
        if (r2 == 0) goto L_0x0387;
    L_0x033f:
        r2 = 1;
        r1.drawNameBot = r2;
        r2 = 1099169792; // 0x41840000 float:16.5 double:5.43062033E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLockTop = r2;
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 != 0) goto L_0x036a;
    L_0x034e:
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLockLeft = r2;
        r2 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r2 = r2 + 4;
        r2 = (float) r2;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_botDrawable;
        r3 = r3.getIntrinsicWidth();
        r2 = r2 + r3;
        r1.nameLeft = r2;
        goto L_0x0387;
    L_0x036a:
        r2 = r41.getMeasuredWidth();
        r3 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r3 = (float) r3;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r2 = r2 - r3;
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_botDrawable;
        r3 = r3.getIntrinsicWidth();
        r2 = r2 - r3;
        r1.nameLockLeft = r2;
        r2 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r1.nameLeft = r3;
    L_0x0387:
        r2 = r1.user;
        r2 = r2.verified;
        r1.drawVerified = r2;
    L_0x038d:
        r2 = r1.lastMessageDate;
        r3 = r1.lastMessageDate;
        if (r3 != 0) goto L_0x039d;
    L_0x0393:
        r3 = r1.message;
        if (r3 == 0) goto L_0x039d;
    L_0x0397:
        r3 = r1.message;
        r3 = r3.messageOwner;
        r2 = r3.date;
    L_0x039d:
        r3 = r1.isDialogCell;
        if (r3 == 0) goto L_0x0403;
    L_0x03a1:
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.DataQuery.getInstance(r3);
        r4 = r1.currentDialogId;
        r3 = r3.getDraft(r4);
        r1.draftMessage = r3;
        r3 = r1.draftMessage;
        if (r3 == 0) goto L_0x03cd;
    L_0x03b3:
        r3 = r1.draftMessage;
        r3 = r3.message;
        r3 = android.text.TextUtils.isEmpty(r3);
        if (r3 == 0) goto L_0x03c3;
    L_0x03bd:
        r3 = r1.draftMessage;
        r3 = r3.reply_to_msg_id;
        if (r3 == 0) goto L_0x03ff;
    L_0x03c3:
        r3 = r1.draftMessage;
        r3 = r3.date;
        if (r2 <= r3) goto L_0x03cd;
    L_0x03c9:
        r3 = r1.unreadCount;
        if (r3 != 0) goto L_0x03ff;
    L_0x03cd:
        r3 = r1.chat;
        r3 = org.telegram.messenger.ChatObject.isChannel(r3);
        if (r3 == 0) goto L_0x03ef;
    L_0x03d5:
        r3 = r1.chat;
        r3 = r3.megagroup;
        if (r3 != 0) goto L_0x03ef;
    L_0x03db:
        r3 = r1.chat;
        r3 = r3.creator;
        if (r3 != 0) goto L_0x03ef;
    L_0x03e1:
        r3 = r1.chat;
        r3 = r3.admin_rights;
        if (r3 == 0) goto L_0x03ff;
    L_0x03e7:
        r3 = r1.chat;
        r3 = r3.admin_rights;
        r3 = r3.post_messages;
        if (r3 == 0) goto L_0x03ff;
    L_0x03ef:
        r3 = r1.chat;
        if (r3 == 0) goto L_0x0406;
    L_0x03f3:
        r3 = r1.chat;
        r3 = r3.left;
        if (r3 != 0) goto L_0x03ff;
    L_0x03f9:
        r3 = r1.chat;
        r3 = r3.kicked;
        if (r3 == 0) goto L_0x0406;
    L_0x03ff:
        r3 = 0;
        r1.draftMessage = r3;
        goto L_0x0406;
    L_0x0403:
        r3 = 0;
        r1.draftMessage = r3;
    L_0x0406:
        if (r7 == 0) goto L_0x0412;
    L_0x0408:
        r6 = r7;
        r1.lastPrintString = r7;
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r25 = r2;
    L_0x040f:
        r2 = r6;
        goto L_0x0877;
    L_0x0412:
        r4 = 0;
        r1.lastPrintString = r4;
        r4 = r1.draftMessage;
        if (r4 == 0) goto L_0x04aa;
    L_0x0419:
        r10 = 0;
        r4 = r1.draftMessage;
        r4 = r4.message;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 == 0) goto L_0x044a;
    L_0x0424:
        r4 = "Draft";
        r5 = 2131493396; // 0x7f0c0214 float:1.861027E38 double:1.0530976613E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
        r5 = android.text.SpannableStringBuilder.valueOf(r4);
        r13 = new android.text.style.ForegroundColorSpan;
        r15 = "chats_draft";
        r15 = org.telegram.ui.ActionBar.Theme.getColor(r15);
        r13.<init>(r15);
        r15 = r4.length();
        r25 = r2;
        r2 = 33;
        r3 = 0;
        r5.setSpan(r13, r3, r15, r2);
        r6 = r5;
        goto L_0x040f;
    L_0x044a:
        r25 = r2;
        r2 = r1.draftMessage;
        r2 = r2.message;
        r3 = r2.length();
        r4 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        if (r3 <= r4) goto L_0x045e;
    L_0x0458:
        r3 = 0;
        r2 = r2.substring(r3, r4);
        goto L_0x045f;
    L_0x045e:
        r3 = 0;
    L_0x045f:
        r4 = "Draft";
        r5 = 2131493396; // 0x7f0c0214 float:1.861027E38 double:1.0530976613E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
        r5 = 2;
        r5 = new java.lang.Object[r5];
        r5[r3] = r4;
        r3 = 10;
        r13 = 32;
        r15 = r2.replace(r3, r13);
        r3 = 1;
        r5[r3] = r15;
        r5 = java.lang.String.format(r11, r5);
        r5 = android.text.SpannableStringBuilder.valueOf(r5);
        r13 = new android.text.style.ForegroundColorSpan;
        r15 = "chats_draft";
        r15 = org.telegram.ui.ActionBar.Theme.getColor(r15);
        r13.<init>(r15);
        r15 = r4.length();
        r15 = r15 + r3;
        r26 = r2;
        r2 = 33;
        r3 = 0;
        r5.setSpan(r13, r3, r15, r2);
        r2 = org.telegram.ui.ActionBar.Theme.dialogs_messagePaint;
        r2 = r2.getFontMetricsInt();
        r13 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r6 = org.telegram.messenger.Emoji.replaceEmoji(r5, r2, r13, r3);
        goto L_0x040f;
    L_0x04aa:
        r25 = r2;
        r2 = r1.message;
        if (r2 != 0) goto L_0x0563;
    L_0x04b0:
        r2 = r1.encryptedChat;
        if (r2 == 0) goto L_0x040f;
    L_0x04b4:
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r2 = r1.encryptedChat;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChatRequested;
        if (r2 == 0) goto L_0x04c7;
    L_0x04bc:
        r2 = "EncryptionProcessing";
        r3 = 2131493441; // 0x7f0c0241 float:1.8610362E38 double:1.0530976835E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r2, r3);
        goto L_0x040f;
    L_0x04c7:
        r2 = r1.encryptedChat;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChatWaiting;
        if (r2 == 0) goto L_0x04ff;
    L_0x04cd:
        r2 = r1.user;
        if (r2 == 0) goto L_0x04ec;
    L_0x04d1:
        r2 = r1.user;
        r2 = r2.first_name;
        if (r2 == 0) goto L_0x04ec;
    L_0x04d7:
        r2 = "AwaitingEncryption";
        r3 = 2131493075; // 0x7f0c00d3 float:1.860962E38 double:1.0530975027E-314;
        r4 = 1;
        r5 = new java.lang.Object[r4];
        r13 = r1.user;
        r13 = r13.first_name;
        r15 = 0;
        r5[r15] = r13;
        r6 = org.telegram.messenger.LocaleController.formatString(r2, r3, r5);
        goto L_0x040f;
    L_0x04ec:
        r4 = 1;
        r15 = 0;
        r2 = "AwaitingEncryption";
        r3 = 2131493075; // 0x7f0c00d3 float:1.860962E38 double:1.0530975027E-314;
        r5 = new java.lang.Object[r4];
        r4 = "";
        r5[r15] = r4;
        r6 = org.telegram.messenger.LocaleController.formatString(r2, r3, r5);
        goto L_0x040f;
    L_0x04ff:
        r2 = r1.encryptedChat;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChatDiscarded;
        if (r2 == 0) goto L_0x0510;
    L_0x0505:
        r2 = "EncryptionRejected";
        r3 = 2131493442; // 0x7f0c0242 float:1.8610364E38 double:1.053097684E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r2, r3);
        goto L_0x040f;
    L_0x0510:
        r2 = r1.encryptedChat;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_encryptedChat;
        if (r2 == 0) goto L_0x040f;
    L_0x0516:
        r2 = r1.encryptedChat;
        r2 = r2.admin_id;
        r3 = r1.currentAccount;
        r3 = org.telegram.messenger.UserConfig.getInstance(r3);
        r3 = r3.getClientUserId();
        if (r2 != r3) goto L_0x0558;
    L_0x0526:
        r2 = r1.user;
        if (r2 == 0) goto L_0x0545;
    L_0x052a:
        r2 = r1.user;
        r2 = r2.first_name;
        if (r2 == 0) goto L_0x0545;
    L_0x0530:
        r2 = "EncryptedChatStartedOutgoing";
        r3 = 2131493430; // 0x7f0c0236 float:1.861034E38 double:1.053097678E-314;
        r4 = 1;
        r5 = new java.lang.Object[r4];
        r13 = r1.user;
        r13 = r13.first_name;
        r15 = 0;
        r5[r15] = r13;
        r6 = org.telegram.messenger.LocaleController.formatString(r2, r3, r5);
        goto L_0x040f;
    L_0x0545:
        r4 = 1;
        r15 = 0;
        r2 = "EncryptedChatStartedOutgoing";
        r3 = 2131493430; // 0x7f0c0236 float:1.861034E38 double:1.053097678E-314;
        r5 = new java.lang.Object[r4];
        r4 = "";
        r5[r15] = r4;
        r6 = org.telegram.messenger.LocaleController.formatString(r2, r3, r5);
        goto L_0x040f;
    L_0x0558:
        r2 = "EncryptedChatStartedIncoming";
        r3 = 2131493429; // 0x7f0c0235 float:1.8610338E38 double:1.0530976776E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r2, r3);
        goto L_0x040f;
    L_0x0563:
        r2 = 0;
        r3 = 0;
        r4 = r1.message;
        r4 = r4.isFromUser();
        if (r4 == 0) goto L_0x0582;
    L_0x056d:
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r5 = r1.message;
        r5 = r5.messageOwner;
        r5 = r5.from_id;
        r5 = java.lang.Integer.valueOf(r5);
        r2 = r4.getUser(r5);
        goto L_0x0598;
    L_0x0582:
        r4 = r1.currentAccount;
        r4 = org.telegram.messenger.MessagesController.getInstance(r4);
        r5 = r1.message;
        r5 = r5.messageOwner;
        r5 = r5.to_id;
        r5 = r5.channel_id;
        r5 = java.lang.Integer.valueOf(r5);
        r3 = r4.getChat(r5);
    L_0x0598:
        r4 = r1.dialogsType;
        r5 = 3;
        if (r4 != r5) goto L_0x05b3;
    L_0x059d:
        r4 = r1.user;
        r4 = org.telegram.messenger.UserObject.isUserSelf(r4);
        if (r4 == 0) goto L_0x05b3;
    L_0x05a5:
        r4 = "SavedMessagesInfo";
        r5 = 2131494294; // 0x7f0c0596 float:1.8612092E38 double:1.053098105E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r4, r5);
        r12 = 0;
        r4 = 0;
        r14 = r4;
        goto L_0x040f;
    L_0x05b3:
        r4 = r1.message;
        r4 = r4.messageOwner;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageService;
        if (r4 == 0) goto L_0x05dc;
    L_0x05bb:
        r4 = r1.chat;
        r4 = org.telegram.messenger.ChatObject.isChannel(r4);
        if (r4 == 0) goto L_0x05d3;
    L_0x05c3:
        r4 = r1.message;
        r4 = r4.messageOwner;
        r4 = r4.action;
        r4 = r4 instanceof org.telegram.tgnet.TLRPC.TL_messageActionHistoryClear;
        if (r4 == 0) goto L_0x05d3;
    L_0x05cd:
        r4 = "";
        r5 = 0;
        r6 = r4;
        r12 = r5;
        goto L_0x05d8;
    L_0x05d3:
        r4 = r1.message;
        r4 = r4.messageText;
        r6 = r4;
    L_0x05d8:
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        goto L_0x040f;
    L_0x05dc:
        r4 = r1.chat;
        if (r4 == 0) goto L_0x07ad;
    L_0x05e0:
        r4 = r1.chat;
        r4 = r4.id;
        if (r4 <= 0) goto L_0x07ad;
    L_0x05e6:
        if (r3 != 0) goto L_0x07ad;
    L_0x05e8:
        r4 = r1.message;
        r4 = r4.isOutOwner();
        if (r4 == 0) goto L_0x05fa;
    L_0x05f0:
        r4 = "FromYou";
        r5 = 2131493622; // 0x7f0c02f6 float:1.861073E38 double:1.053097773E-314;
        r4 = org.telegram.messenger.LocaleController.getString(r4, r5);
    L_0x05f9:
        goto L_0x0618;
    L_0x05fa:
        if (r2 == 0) goto L_0x0609;
    L_0x05fc:
        r4 = org.telegram.messenger.UserObject.getFirstName(r2);
        r5 = "\n";
        r13 = "";
        r4 = r4.replace(r5, r13);
        goto L_0x05f9;
    L_0x0609:
        if (r3 == 0) goto L_0x0616;
    L_0x060b:
        r4 = r3.title;
        r5 = "\n";
        r13 = "";
        r4 = r4.replace(r5, r13);
        goto L_0x05f9;
    L_0x0616:
        r4 = "DELETED";
    L_0x0618:
        r10 = 0;
        r5 = r1.message;
        r5 = r5.caption;
        if (r5 == 0) goto L_0x0652;
    L_0x061f:
        r5 = r1.message;
        r5 = r5.caption;
        r5 = r5.toString();
        r13 = r5.length();
        r15 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        if (r13 <= r15) goto L_0x0635;
    L_0x062f:
        r13 = 0;
        r5 = r5.substring(r13, r15);
        goto L_0x0636;
    L_0x0635:
        r13 = 0;
    L_0x0636:
        r15 = 2;
        r15 = new java.lang.Object[r15];
        r15[r13] = r4;
        r27 = r2;
        r2 = 32;
        r13 = 10;
        r18 = r5.replace(r13, r2);
        r2 = 1;
        r15[r2] = r18;
        r2 = java.lang.String.format(r11, r15);
        r2 = android.text.SpannableStringBuilder.valueOf(r2);
        goto L_0x0778;
    L_0x0652:
        r27 = r2;
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.media;
        if (r2 == 0) goto L_0x073c;
    L_0x065c:
        r2 = r1.message;
        r2 = r2.isMediaEmpty();
        if (r2 != 0) goto L_0x073c;
    L_0x0664:
        r2 = org.telegram.ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        r5 = r1.message;
        r5 = r5.messageOwner;
        r5 = r5.media;
        r5 = r5 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r5 == 0) goto L_0x06b3;
    L_0x0670:
        r5 = android.os.Build.VERSION.SDK_INT;
        r9 = 18;
        if (r5 < r9) goto L_0x0695;
    L_0x0676:
        r5 = "%s: 🎮 ⁨%s⁩";
        r9 = 2;
        r13 = new java.lang.Object[r9];
        r9 = 0;
        r13[r9] = r4;
        r9 = r1.message;
        r9 = r9.messageOwner;
        r9 = r9.media;
        r9 = r9.game;
        r9 = r9.title;
        r15 = 1;
        r13[r15] = r9;
        r5 = java.lang.String.format(r5, r13);
        r5 = android.text.SpannableStringBuilder.valueOf(r5);
    L_0x0693:
        goto L_0x071c;
    L_0x0695:
        r5 = "%s: 🎮 %s";
        r9 = 2;
        r13 = new java.lang.Object[r9];
        r9 = 0;
        r13[r9] = r4;
        r9 = r1.message;
        r9 = r9.messageOwner;
        r9 = r9.media;
        r9 = r9.game;
        r9 = r9.title;
        r15 = 1;
        r13[r15] = r9;
        r5 = java.lang.String.format(r5, r13);
        r5 = android.text.SpannableStringBuilder.valueOf(r5);
        goto L_0x0693;
    L_0x06b3:
        r5 = r1.message;
        r5 = r5.type;
        r9 = 14;
        if (r5 != r9) goto L_0x0707;
    L_0x06bb:
        r5 = android.os.Build.VERSION.SDK_INT;
        r9 = 18;
        if (r5 < r9) goto L_0x06e4;
    L_0x06c1:
        r5 = "%s: 🎧 ⁨%s - %s⁩";
        r9 = 3;
        r13 = new java.lang.Object[r9];
        r9 = 0;
        r13[r9] = r4;
        r9 = r1.message;
        r9 = r9.getMusicAuthor();
        r15 = 1;
        r13[r15] = r9;
        r9 = r1.message;
        r9 = r9.getMusicTitle();
        r15 = 2;
        r13[r15] = r9;
        r5 = java.lang.String.format(r5, r13);
        r5 = android.text.SpannableStringBuilder.valueOf(r5);
        goto L_0x0693;
    L_0x06e4:
        r5 = "%s: 🎧 %s - %s";
        r9 = 3;
        r13 = new java.lang.Object[r9];
        r9 = 0;
        r13[r9] = r4;
        r9 = r1.message;
        r9 = r9.getMusicAuthor();
        r15 = 1;
        r13[r15] = r9;
        r9 = r1.message;
        r9 = r9.getMusicTitle();
        r15 = 2;
        r13[r15] = r9;
        r5 = java.lang.String.format(r5, r13);
        r5 = android.text.SpannableStringBuilder.valueOf(r5);
        goto L_0x0693;
    L_0x0707:
        r15 = 2;
        r5 = new java.lang.Object[r15];
        r9 = 0;
        r5[r9] = r4;
        r9 = r1.message;
        r9 = r9.messageText;
        r13 = 1;
        r5[r13] = r9;
        r5 = java.lang.String.format(r11, r5);
        r5 = android.text.SpannableStringBuilder.valueOf(r5);
    L_0x071c:
        r9 = new android.text.style.ForegroundColorSpan;
        r13 = "chats_attachMessage";
        r13 = org.telegram.ui.ActionBar.Theme.getColor(r13);
        r9.<init>(r13);
        r13 = r4.length();
        r15 = 2;
        r13 = r13 + r15;
        r15 = r5.length();
        r28 = r2;
        r2 = 33;
        r5.setSpan(r9, r13, r15, r2);
        r2 = r5;
        r9 = r28;
        goto L_0x0778;
    L_0x073c:
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.message;
        if (r2 == 0) goto L_0x0772;
    L_0x0744:
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.message;
        r5 = r2.length();
        r13 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        if (r5 <= r13) goto L_0x0758;
    L_0x0752:
        r5 = 0;
        r2 = r2.substring(r5, r13);
        goto L_0x0759;
    L_0x0758:
        r5 = 0;
    L_0x0759:
        r13 = 2;
        r13 = new java.lang.Object[r13];
        r13[r5] = r4;
        r5 = 10;
        r15 = 32;
        r18 = r2.replace(r5, r15);
        r5 = 1;
        r13[r5] = r18;
        r5 = java.lang.String.format(r11, r13);
        r2 = android.text.SpannableStringBuilder.valueOf(r5);
        goto L_0x0778;
    L_0x0772:
        r2 = "";
        r2 = android.text.SpannableStringBuilder.valueOf(r2);
    L_0x0778:
        r5 = r2.length();
        if (r5 <= 0) goto L_0x0798;
    L_0x077e:
        r5 = new android.text.style.ForegroundColorSpan;
        r13 = "chats_nameMessage";
        r13 = org.telegram.ui.ActionBar.Theme.getColor(r13);
        r5.<init>(r13);
        r13 = r4.length();
        r15 = 1;
        r13 = r13 + r15;
        r29 = r3;
        r3 = 33;
        r15 = 0;
        r2.setSpan(r5, r15, r13, r3);
        goto L_0x079b;
    L_0x0798:
        r29 = r3;
        r15 = 0;
    L_0x079b:
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_messagePaint;
        r3 = r3.getFontMetricsInt();
        r5 = 1101004800; // 0x41a00000 float:20.0 double:5.439686476E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r6 = org.telegram.messenger.Emoji.replaceEmoji(r2, r3, r5, r15);
        goto L_0x040f;
    L_0x07ad:
        r27 = r2;
        r29 = r3;
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaPhoto;
        if (r2 == 0) goto L_0x07dc;
    L_0x07bb:
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2.photo;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_photoEmpty;
        if (r2 == 0) goto L_0x07dc;
    L_0x07c7:
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2.ttl_seconds;
        if (r2 == 0) goto L_0x07dc;
    L_0x07d1:
        r2 = "AttachPhotoExpired";
        r3 = 2131493038; // 0x7f0c00ae float:1.8609545E38 double:1.0530974844E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r2, r3);
        goto L_0x040f;
    L_0x07dc:
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
        if (r2 == 0) goto L_0x0807;
    L_0x07e6:
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2.document;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_documentEmpty;
        if (r2 == 0) goto L_0x0807;
    L_0x07f2:
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2.ttl_seconds;
        if (r2 == 0) goto L_0x0807;
    L_0x07fc:
        r2 = "AttachVideoExpired";
        r3 = 2131493044; // 0x7f0c00b4 float:1.8609557E38 double:1.0530974874E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r2, r3);
        goto L_0x040f;
    L_0x0807:
        r2 = r1.message;
        r2 = r2.caption;
        if (r2 == 0) goto L_0x0813;
    L_0x080d:
        r2 = r1.message;
        r6 = r2.caption;
        goto L_0x040f;
    L_0x0813:
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.media;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaGame;
        if (r2 == 0) goto L_0x083a;
    L_0x081d:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "🎮 ";
        r2.append(r3);
        r3 = r1.message;
        r3 = r3.messageOwner;
        r3 = r3.media;
        r3 = r3.game;
        r3 = r3.title;
        r2.append(r3);
        r2 = r2.toString();
    L_0x0838:
        r6 = r2;
        goto L_0x0863;
    L_0x083a:
        r2 = r1.message;
        r2 = r2.type;
        r3 = 14;
        if (r2 != r3) goto L_0x085e;
    L_0x0842:
        r2 = "🎧 %s - %s";
        r3 = 2;
        r3 = new java.lang.Object[r3];
        r4 = r1.message;
        r4 = r4.getMusicAuthor();
        r5 = 0;
        r3[r5] = r4;
        r4 = r1.message;
        r4 = r4.getMusicTitle();
        r5 = 1;
        r3[r5] = r4;
        r2 = java.lang.String.format(r2, r3);
        goto L_0x0838;
    L_0x085e:
        r2 = r1.message;
        r2 = r2.messageText;
        goto L_0x0838;
    L_0x0863:
        r2 = r1.message;
        r2 = r2.messageOwner;
        r2 = r2.media;
        if (r2 == 0) goto L_0x040f;
    L_0x086b:
        r2 = r1.message;
        r2 = r2.isMediaEmpty();
        if (r2 != 0) goto L_0x040f;
    L_0x0873:
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_messagePrintingPaint;
        goto L_0x040f;
    L_0x0877:
        r3 = r1.draftMessage;
        if (r3 == 0) goto L_0x0885;
    L_0x087b:
        r3 = r1.draftMessage;
        r3 = r3.date;
        r3 = (long) r3;
        r3 = org.telegram.messenger.LocaleController.stringForMessageListDate(r3);
    L_0x0884:
        goto L_0x08a3;
    L_0x0885:
        r3 = r1.lastMessageDate;
        if (r3 == 0) goto L_0x0891;
    L_0x0889:
        r3 = r1.lastMessageDate;
        r3 = (long) r3;
        r3 = org.telegram.messenger.LocaleController.stringForMessageListDate(r3);
        goto L_0x0884;
    L_0x0891:
        r3 = r1.message;
        if (r3 == 0) goto L_0x08a1;
    L_0x0895:
        r3 = r1.message;
        r3 = r3.messageOwner;
        r3 = r3.date;
        r3 = (long) r3;
        r3 = org.telegram.messenger.LocaleController.stringForMessageListDate(r3);
        goto L_0x0884;
    L_0x08a1:
        r3 = r21;
    L_0x08a3:
        r4 = r1.message;
        if (r4 != 0) goto L_0x08ba;
    L_0x08a7:
        r4 = 0;
        r1.drawCheck1 = r4;
        r1.drawCheck2 = r4;
        r1.drawClock = r4;
        r1.drawCount = r4;
        r1.drawMention = r4;
        r1.drawError = r4;
        r4 = r22;
    L_0x08b6:
        r5 = r23;
        goto L_0x096e;
    L_0x08ba:
        r4 = r1.unreadCount;
        if (r4 == 0) goto L_0x08ea;
    L_0x08be:
        r4 = r1.unreadCount;
        r5 = 1;
        if (r4 != r5) goto L_0x08d5;
    L_0x08c3:
        r4 = r1.unreadCount;
        r5 = r1.mentionCount;
        if (r4 != r5) goto L_0x08d5;
    L_0x08c9:
        r4 = r1.message;
        if (r4 == 0) goto L_0x08d5;
    L_0x08cd:
        r4 = r1.message;
        r4 = r4.messageOwner;
        r4 = r4.mentioned;
        if (r4 != 0) goto L_0x08ea;
    L_0x08d5:
        r4 = 1;
        r1.drawCount = r4;
        r5 = "%d";
        r6 = new java.lang.Object[r4];
        r4 = r1.unreadCount;
        r4 = java.lang.Integer.valueOf(r4);
        r13 = 0;
        r6[r13] = r4;
        r4 = java.lang.String.format(r5, r6);
        goto L_0x08ef;
    L_0x08ea:
        r13 = 0;
        r1.drawCount = r13;
        r4 = r22;
    L_0x08ef:
        r5 = r1.mentionCount;
        if (r5 == 0) goto L_0x08fb;
    L_0x08f3:
        r5 = 1;
        r1.drawMention = r5;
        r5 = "@";
        r23 = r5;
        goto L_0x08fd;
    L_0x08fb:
        r1.drawMention = r13;
    L_0x08fd:
        r5 = r1.message;
        r5 = r5.isOut();
        if (r5 == 0) goto L_0x0963;
    L_0x0905:
        r5 = r1.draftMessage;
        if (r5 != 0) goto L_0x0963;
    L_0x0909:
        if (r12 == 0) goto L_0x0963;
    L_0x090b:
        r5 = r1.message;
        r5 = r5.isSending();
        if (r5 == 0) goto L_0x091e;
    L_0x0913:
        r5 = 0;
        r1.drawCheck1 = r5;
        r1.drawCheck2 = r5;
        r6 = 1;
        r1.drawClock = r6;
        r1.drawError = r5;
        goto L_0x08b6;
    L_0x091e:
        r5 = 0;
        r6 = r1.message;
        r6 = r6.isSendError();
        if (r6 == 0) goto L_0x0935;
    L_0x0927:
        r1.drawCheck1 = r5;
        r1.drawCheck2 = r5;
        r1.drawClock = r5;
        r6 = 1;
        r1.drawError = r6;
        r1.drawCount = r5;
        r1.drawMention = r5;
        goto L_0x08b6;
    L_0x0935:
        r5 = r1.message;
        r5 = r5.isSent();
        if (r5 == 0) goto L_0x08b6;
    L_0x093d:
        r5 = r1.message;
        r5 = r5.isUnread();
        if (r5 == 0) goto L_0x0956;
    L_0x0945:
        r5 = r1.chat;
        r5 = org.telegram.messenger.ChatObject.isChannel(r5);
        if (r5 == 0) goto L_0x0954;
    L_0x094d:
        r5 = r1.chat;
        r5 = r5.megagroup;
        if (r5 != 0) goto L_0x0954;
    L_0x0953:
        goto L_0x0956;
    L_0x0954:
        r5 = 0;
        goto L_0x0957;
    L_0x0956:
        r5 = 1;
    L_0x0957:
        r1.drawCheck1 = r5;
        r5 = 1;
        r1.drawCheck2 = r5;
        r5 = 0;
        r1.drawClock = r5;
        r1.drawError = r5;
        goto L_0x08b6;
    L_0x0963:
        r5 = 0;
        r1.drawCheck1 = r5;
        r1.drawCheck2 = r5;
        r1.drawClock = r5;
        r1.drawError = r5;
        goto L_0x08b6;
    L_0x096e:
        r6 = r1.chat;
        if (r6 == 0) goto L_0x0978;
    L_0x0972:
        r6 = r1.chat;
        r6 = r6.title;
        goto L_0x0a34;
    L_0x0978:
        r6 = r1.user;
        if (r6 == 0) goto L_0x0a32;
    L_0x097c:
        r6 = r1.user;
        r6 = org.telegram.messenger.UserObject.isUserSelf(r6);
        if (r6 == 0) goto L_0x0997;
    L_0x0984:
        r6 = r1.dialogsType;
        r13 = 3;
        if (r6 != r13) goto L_0x098c;
    L_0x0989:
        r6 = 1;
        r1.drawPinBackground = r6;
    L_0x098c:
        r6 = "SavedMessages";
        r13 = 2131494293; // 0x7f0c0595 float:1.861209E38 double:1.0530981045E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r6, r13);
    L_0x0995:
        goto L_0x0a2b;
    L_0x0997:
        r6 = r1.user;
        r6 = r6.id;
        r6 = r6 / 1000;
        r13 = 777; // 0x309 float:1.089E-42 double:3.84E-321;
        if (r6 == r13) goto L_0x0a23;
    L_0x09a1:
        r6 = r1.user;
        r6 = r6.id;
        r6 = r6 / 1000;
        r13 = 333; // 0x14d float:4.67E-43 double:1.645E-321;
        if (r6 == r13) goto L_0x0a23;
    L_0x09ab:
        r6 = r1.currentAccount;
        r6 = org.telegram.messenger.ContactsController.getInstance(r6);
        r6 = r6.contactsDict;
        r13 = r1.user;
        r13 = r13.id;
        r13 = java.lang.Integer.valueOf(r13);
        r6 = r6.get(r13);
        if (r6 != 0) goto L_0x0a23;
    L_0x09c1:
        r6 = r1.currentAccount;
        r6 = org.telegram.messenger.ContactsController.getInstance(r6);
        r6 = r6.contactsDict;
        r6 = r6.size();
        if (r6 != 0) goto L_0x09ec;
    L_0x09cf:
        r6 = r1.currentAccount;
        r6 = org.telegram.messenger.ContactsController.getInstance(r6);
        r6 = r6.contactsLoaded;
        if (r6 == 0) goto L_0x09e5;
    L_0x09d9:
        r6 = r1.currentAccount;
        r6 = org.telegram.messenger.ContactsController.getInstance(r6);
        r6 = r6.isLoadingContacts();
        if (r6 == 0) goto L_0x09ec;
    L_0x09e5:
        r6 = r1.user;
        r6 = org.telegram.messenger.UserObject.getUserName(r6);
        goto L_0x0995;
    L_0x09ec:
        r6 = r1.user;
        r6 = r6.phone;
        if (r6 == 0) goto L_0x0a1b;
    L_0x09f2:
        r6 = r1.user;
        r6 = r6.phone;
        r6 = r6.length();
        if (r6 == 0) goto L_0x0a1b;
    L_0x09fc:
        r6 = org.telegram.PhoneFormat.PhoneFormat.getInstance();
        r13 = new java.lang.StringBuilder;
        r13.<init>();
        r15 = "+";
        r13.append(r15);
        r15 = r1.user;
        r15 = r15.phone;
        r13.append(r15);
        r13 = r13.toString();
        r6 = r6.format(r13);
        goto L_0x0995;
    L_0x0a1b:
        r6 = r1.user;
        r6 = org.telegram.messenger.UserObject.getUserName(r6);
        goto L_0x0995;
    L_0x0a23:
        r6 = r1.user;
        r6 = org.telegram.messenger.UserObject.getUserName(r6);
        goto L_0x0995;
    L_0x0a2b:
        r13 = r1.encryptedChat;
        if (r13 == 0) goto L_0x0a34;
    L_0x0a2f:
        r8 = org.telegram.ui.ActionBar.Theme.dialogs_nameEncryptedPaint;
        goto L_0x0a34;
    L_0x0a32:
        r6 = r20;
    L_0x0a34:
        r13 = r6.length();
        if (r13 != 0) goto L_0x0a43;
    L_0x0a3a:
        r13 = "HiddenName";
        r15 = 2131493648; // 0x7f0c0310 float:1.8610782E38 double:1.053097786E-314;
        r6 = org.telegram.messenger.LocaleController.getString(r13, r15);
    L_0x0a43:
        r40 = r6;
        r6 = r2;
        r2 = r5;
        r5 = r40;
    L_0x0a49:
        if (r14 == 0) goto L_0x0a8e;
    L_0x0a4b:
        r13 = org.telegram.ui.ActionBar.Theme.dialogs_timePaint;
        r13 = r13.measureText(r3);
        r30 = r11;
        r31 = r12;
        r11 = (double) r13;
        r11 = java.lang.Math.ceil(r11);
        r11 = (int) r11;
        r12 = new android.text.StaticLayout;
        r21 = org.telegram.ui.ActionBar.Theme.dialogs_timePaint;
        r23 = android.text.Layout.Alignment.ALIGN_NORMAL;
        r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r25 = 0;
        r26 = 0;
        r19 = r12;
        r20 = r3;
        r22 = r11;
        r19.<init>(r20, r21, r22, r23, r24, r25, r26);
        r1.timeLayout = r12;
        r12 = org.telegram.messenger.LocaleController.isRTL;
        if (r12 != 0) goto L_0x0a85;
    L_0x0a76:
        r12 = r41.getMeasuredWidth();
        r13 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r12 = r12 - r13;
        r12 = r12 - r11;
        r1.timeLeft = r12;
        goto L_0x0a99;
    L_0x0a85:
        r12 = 1097859072; // 0x41700000 float:15.0 double:5.424144515E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r12);
        r1.timeLeft = r12;
        goto L_0x0a99;
    L_0x0a8e:
        r30 = r11;
        r31 = r12;
        r11 = 0;
        r12 = 0;
        r1.timeLayout = r12;
        r12 = 0;
        r1.timeLeft = r12;
    L_0x0a99:
        r12 = org.telegram.messenger.LocaleController.isRTL;
        if (r12 != 0) goto L_0x0aad;
    L_0x0a9d:
        r12 = r41.getMeasuredWidth();
        r13 = r1.nameLeft;
        r12 = r12 - r13;
        r13 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r12 = r12 - r15;
        r12 = r12 - r11;
        goto L_0x0ac2;
    L_0x0aad:
        r12 = r41.getMeasuredWidth();
        r13 = r1.nameLeft;
        r12 = r12 - r13;
        r13 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r13 = (float) r13;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r12 = r12 - r13;
        r12 = r12 - r11;
        r13 = r1.nameLeft;
        r13 = r13 + r11;
        r1.nameLeft = r13;
    L_0x0ac2:
        r13 = r1.drawNameLock;
        if (r13 == 0) goto L_0x0ad5;
    L_0x0ac6:
        r13 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_lockDrawable;
        r15 = r15.getIntrinsicWidth();
        r13 = r13 + r15;
        r12 = r12 - r13;
        goto L_0x0b0d;
    L_0x0ad5:
        r13 = r1.drawNameGroup;
        if (r13 == 0) goto L_0x0ae8;
    L_0x0ad9:
        r13 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_groupDrawable;
        r15 = r15.getIntrinsicWidth();
        r13 = r13 + r15;
        r12 = r12 - r13;
        goto L_0x0b0d;
    L_0x0ae8:
        r13 = r1.drawNameBroadcast;
        if (r13 == 0) goto L_0x0afb;
    L_0x0aec:
        r13 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_broadcastDrawable;
        r15 = r15.getIntrinsicWidth();
        r13 = r13 + r15;
        r12 = r12 - r13;
        goto L_0x0b0d;
    L_0x0afb:
        r13 = r1.drawNameBot;
        if (r13 == 0) goto L_0x0b0d;
    L_0x0aff:
        r13 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_botDrawable;
        r15 = r15.getIntrinsicWidth();
        r13 = r13 + r15;
        r12 = r12 - r13;
    L_0x0b0d:
        r13 = r1.drawClock;
        r15 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        if (r13 == 0) goto L_0x0b42;
    L_0x0b13:
        r13 = org.telegram.ui.ActionBar.Theme.dialogs_clockDrawable;
        r13 = r13.getIntrinsicWidth();
        r17 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13 = r13 + r17;
        r12 = r12 - r13;
        r17 = org.telegram.messenger.LocaleController.isRTL;
        if (r17 != 0) goto L_0x0b2c;
    L_0x0b24:
        r15 = r1.timeLeft;
        r15 = r15 - r13;
        r1.checkDrawLeft = r15;
        r32 = r3;
        goto L_0x0b3d;
    L_0x0b2c:
        r32 = r3;
        r3 = r1.timeLeft;
        r3 = r3 + r11;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r3 = r3 + r15;
        r1.checkDrawLeft = r3;
        r3 = r1.nameLeft;
        r3 = r3 + r13;
        r1.nameLeft = r3;
        r33 = r7;
        goto L_0x0bc8;
    L_0x0b42:
        r32 = r3;
        r3 = r1.drawCheck2;
        if (r3 == 0) goto L_0x0bc6;
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_checkDrawable;
        r3 = r3.getIntrinsicWidth();
        r13 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r3 = r3 + r13;
        r12 = r12 - r3;
        r13 = r1.drawCheck1;
        if (r13 == 0) goto L_0x0ba8;
        r13 = org.telegram.ui.ActionBar.Theme.dialogs_halfCheckDrawable;
        r13 = r13.getIntrinsicWidth();
        r15 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13 = r13 - r15;
        r12 = r12 - r13;
        r13 = org.telegram.messenger.LocaleController.isRTL;
        if (r13 != 0) goto L_0x0b7b;
        r13 = r1.timeLeft;
        r13 = r13 - r3;
        r1.halfCheckDrawLeft = r13;
        r13 = r1.halfCheckDrawLeft;
        r15 = 1085276160; // 0x40b00000 float:5.5 double:5.36197667E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13 = r13 - r15;
        r1.checkDrawLeft = r13;
        goto L_0x0b3e;
        r13 = r1.timeLeft;
        r13 = r13 + r11;
        r15 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13 = r13 + r15;
        r1.checkDrawLeft = r13;
        r13 = r1.checkDrawLeft;
        r15 = 1085276160; // 0x40b00000 float:5.5 double:5.36197667E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13 = r13 + r15;
        r1.halfCheckDrawLeft = r13;
        r13 = r1.nameLeft;
        r15 = org.telegram.ui.ActionBar.Theme.dialogs_halfCheckDrawable;
        r15 = r15.getIntrinsicWidth();
        r15 = r15 + r3;
        r33 = r7;
        r7 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r15 = r15 - r7;
        r13 = r13 + r15;
        r1.nameLeft = r13;
        goto L_0x0bc8;
        r33 = r7;
        r7 = org.telegram.messenger.LocaleController.isRTL;
        if (r7 != 0) goto L_0x0bb4;
        r7 = r1.timeLeft;
        r7 = r7 - r3;
        r1.checkDrawLeft = r7;
        goto L_0x0bc8;
        r7 = r1.timeLeft;
        r7 = r7 + r11;
        r13 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r7 = r7 + r13;
        r1.checkDrawLeft = r7;
        r7 = r1.nameLeft;
        r7 = r7 + r3;
        r1.nameLeft = r7;
        goto L_0x0bc8;
        r33 = r7;
        r3 = r1.dialogMuted;
        r7 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        if (r3 == 0) goto L_0x0be8;
        r3 = r1.drawVerified;
        if (r3 != 0) goto L_0x0be8;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r13 = org.telegram.ui.ActionBar.Theme.dialogs_muteDrawable;
        r13 = r13.getIntrinsicWidth();
        r3 = r3 + r13;
        r12 = r12 - r3;
        r13 = org.telegram.messenger.LocaleController.isRTL;
        if (r13 == 0) goto L_0x0be7;
        r13 = r1.nameLeft;
        r13 = r13 + r3;
        r1.nameLeft = r13;
        goto L_0x0c01;
        r3 = r1.drawVerified;
        if (r3 == 0) goto L_0x0c01;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r13 = org.telegram.ui.ActionBar.Theme.dialogs_verifiedDrawable;
        r13 = r13.getIntrinsicWidth();
        r3 = r3 + r13;
        r12 = r12 - r3;
        r13 = org.telegram.messenger.LocaleController.isRTL;
        if (r13 == 0) goto L_0x0c01;
        r13 = r1.nameLeft;
        r13 = r13 + r3;
        r1.nameLeft = r13;
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r12 = java.lang.Math.max(r13, r12);
        r13 = 10;
        r15 = 32;
        r7 = r5.replace(r13, r15);	 Catch:{ Exception -> 0x0c36 }
        r13 = org.telegram.messenger.AndroidUtilities.dp(r3);	 Catch:{ Exception -> 0x0c36 }
        r13 = r12 - r13;	 Catch:{ Exception -> 0x0c36 }
        r13 = (float) r13;	 Catch:{ Exception -> 0x0c36 }
        r15 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x0c36 }
        r20 = android.text.TextUtils.ellipsize(r7, r8, r13, r15);	 Catch:{ Exception -> 0x0c36 }
        r7 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0c36 }
        r23 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0c36 }
        r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0c36 }
        r25 = 0;	 Catch:{ Exception -> 0x0c36 }
        r26 = 0;	 Catch:{ Exception -> 0x0c36 }
        r19 = r7;	 Catch:{ Exception -> 0x0c36 }
        r21 = r8;	 Catch:{ Exception -> 0x0c36 }
        r22 = r12;	 Catch:{ Exception -> 0x0c36 }
        r19.<init>(r20, r21, r22, r23, r24, r25, r26);	 Catch:{ Exception -> 0x0c36 }
        r1.nameLayout = r7;	 Catch:{ Exception -> 0x0c36 }
        goto L_0x0c3b;
    L_0x0c36:
        r0 = move-exception;
        r7 = r0;
        org.telegram.messenger.FileLog.e(r7);
        r7 = r41.getMeasuredWidth();
        r13 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r13 = r13 + 16;
        r13 = (float) r13;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r7 = r7 - r13;
        r13 = org.telegram.messenger.LocaleController.isRTL;
        if (r13 != 0) goto L_0x0c66;
        r13 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r13 = (float) r13;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r1.messageLeft = r13;
        r13 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r13 == 0) goto L_0x0c5f;
        r13 = 1095761920; // 0x41500000 float:13.0 double:5.413783207E-315;
        goto L_0x0c61;
        r13 = 1091567616; // 0x41100000 float:9.0 double:5.39306059E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        goto L_0x0c82;
        r13 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r1.messageLeft = r13;
        r13 = r41.getMeasuredWidth();
        r15 = org.telegram.messenger.AndroidUtilities.isTablet();
        if (r15 == 0) goto L_0x0c7b;
        r15 = 1115815936; // 0x42820000 float:65.0 double:5.51286321E-315;
        goto L_0x0c7d;
        r15 = 1114898432; // 0x42740000 float:61.0 double:5.50833014E-315;
        r15 = org.telegram.messenger.AndroidUtilities.dp(r15);
        r13 = r13 - r15;
        r15 = r1.avatarImage;
        r3 = r1.avatarTop;
        r34 = r5;
        r5 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r35 = r8;
        r8 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r15.setImageCoords(r13, r3, r5, r8);
        r3 = r1.drawError;
        if (r3 == 0) goto L_0x0cca;
        r3 = 1106771968; // 0x41f80000 float:31.0 double:5.46818007E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r7 = r7 - r3;
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 != 0) goto L_0x0cb6;
        r5 = r41.getMeasuredWidth();
        r8 = 1107820544; // 0x42080000 float:34.0 double:5.473360725E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 - r8;
        r1.errorLeft = r5;
        goto L_0x0cc3;
        r5 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r1.errorLeft = r5;
        r5 = r1.messageLeft;
        r5 = r5 + r3;
        r1.messageLeft = r5;
        r37 = r13;
        r36 = r14;
        goto L_0x0dd5;
        if (r4 != 0) goto L_0x0d0d;
        if (r2 == 0) goto L_0x0ccf;
        goto L_0x0d0d;
        r3 = r1.drawPin;
        if (r3 == 0) goto L_0x0d07;
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_pinnedDrawable;
        r3 = r3.getIntrinsicWidth();
        r5 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = r3 + r5;
        r7 = r7 - r3;
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 != 0) goto L_0x0cfa;
        r5 = r41.getMeasuredWidth();
        r8 = org.telegram.ui.ActionBar.Theme.dialogs_pinnedDrawable;
        r8 = r8.getIntrinsicWidth();
        r5 = r5 - r8;
        r8 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 - r8;
        r1.pinLeft = r5;
        goto L_0x0d07;
        r8 = 1096810496; // 0x41600000 float:14.0 double:5.41896386E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r1.pinLeft = r5;
        r5 = r1.messageLeft;
        r5 = r5 + r3;
        r1.messageLeft = r5;
        r3 = 0;
        r1.drawCount = r3;
        r1.drawMention = r3;
        goto L_0x0cc4;
        if (r4 == 0) goto L_0x0d74;
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r3 = org.telegram.ui.ActionBar.Theme.dialogs_countTextPaint;
        r3 = r3.measureText(r4);
        r37 = r13;
        r36 = r14;
        r13 = (double) r3;
        r13 = java.lang.Math.ceil(r13);
        r3 = (int) r13;
        r3 = java.lang.Math.max(r5, r3);
        r1.countWidth = r3;
        r3 = new android.text.StaticLayout;
        r21 = org.telegram.ui.ActionBar.Theme.dialogs_countTextPaint;
        r5 = r1.countWidth;
        r23 = android.text.Layout.Alignment.ALIGN_CENTER;
        r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r25 = 0;
        r26 = 0;
        r19 = r3;
        r20 = r4;
        r22 = r5;
        r19.<init>(r20, r21, r22, r23, r24, r25, r26);
        r1.countLayout = r3;
        r3 = r1.countWidth;
        r5 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = r3 + r5;
        r7 = r7 - r3;
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 != 0) goto L_0x0d63;
        r5 = r41.getMeasuredWidth();
        r8 = r1.countWidth;
        r5 = r5 - r8;
        r8 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 - r8;
        r1.countLeft = r5;
        goto L_0x0d70;
        r5 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r1.countLeft = r5;
        r5 = r1.messageLeft;
        r5 = r5 + r3;
        r1.messageLeft = r5;
        r5 = 1;
        r1.drawCount = r5;
        goto L_0x0d7b;
        r37 = r13;
        r36 = r14;
        r3 = 0;
        r1.countWidth = r3;
        if (r2 == 0) goto L_0x0dd5;
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r1.mentionWidth = r5;
        r3 = r1.mentionWidth;
        r5 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r3 = r3 + r5;
        r7 = r7 - r3;
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 != 0) goto L_0x0db4;
        r5 = r41.getMeasuredWidth();
        r8 = r1.mentionWidth;
        r5 = r5 - r8;
        r8 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r5 = r5 - r8;
        r8 = r1.countWidth;
        if (r8 == 0) goto L_0x0daf;
        r8 = r1.countWidth;
        r13 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r8 = r8 + r13;
        goto L_0x0db0;
        r8 = 0;
        r5 = r5 - r8;
        r1.mentionLeft = r5;
        goto L_0x0dd1;
        r5 = 1100480512; // 0x41980000 float:19.0 double:5.43709615E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r8 = r1.countWidth;
        if (r8 == 0) goto L_0x0dc8;
        r8 = r1.countWidth;
        r13 = 1099956224; // 0x41900000 float:18.0 double:5.43450582E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r8 = r8 + r13;
        goto L_0x0dc9;
        r8 = 0;
        r5 = r5 + r8;
        r1.mentionLeft = r5;
        r5 = r1.messageLeft;
        r5 = r5 + r3;
        r1.messageLeft = r5;
        r5 = 1;
        r1.drawMention = r5;
        if (r10 == 0) goto L_0x0e06;
        if (r6 != 0) goto L_0x0ddb;
        r6 = "";
        r3 = r6.toString();
        r5 = r3.length();
        r8 = 150; // 0x96 float:2.1E-43 double:7.4E-322;
        if (r5 <= r8) goto L_0x0ded;
        r5 = 0;
        r3 = r3.substring(r5, r8);
        goto L_0x0dee;
        r5 = 0;
        r8 = 10;
        r13 = 32;
        r3 = r3.replace(r8, r13);
        r8 = org.telegram.ui.ActionBar.Theme.dialogs_messagePaint;
        r8 = r8.getFontMetricsInt();
        r13 = 1099431936; // 0x41880000 float:17.0 double:5.431915495E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r6 = org.telegram.messenger.Emoji.replaceEmoji(r3, r8, r13, r5);
        r3 = 1094713344; // 0x41400000 float:12.0 double:5.408602553E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r5 = java.lang.Math.max(r5, r7);
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r3 = r5 - r3;
        r3 = (float) r3;
        r7 = android.text.TextUtils.TruncateAt.END;
        r20 = android.text.TextUtils.ellipsize(r6, r9, r3, r7);
        r3 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x0e33 }
        r23 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x0e33 }
        r24 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Exception -> 0x0e33 }
        r25 = 0;	 Catch:{ Exception -> 0x0e33 }
        r26 = 0;	 Catch:{ Exception -> 0x0e33 }
        r19 = r3;	 Catch:{ Exception -> 0x0e33 }
        r21 = r9;	 Catch:{ Exception -> 0x0e33 }
        r22 = r5;	 Catch:{ Exception -> 0x0e33 }
        r19.<init>(r20, r21, r22, r23, r24, r25, r26);	 Catch:{ Exception -> 0x0e33 }
        r1.messageLayout = r3;	 Catch:{ Exception -> 0x0e33 }
        goto L_0x0e38;
    L_0x0e33:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        r3 = org.telegram.messenger.LocaleController.isRTL;
        if (r3 == 0) goto L_0x0eec;
        r3 = r1.nameLayout;
        if (r3 == 0) goto L_0x0eb5;
        r3 = r1.nameLayout;
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x0eb5;
        r3 = r1.nameLayout;
        r7 = 0;
        r3 = r3.getLineLeft(r7);
        r8 = r1.nameLayout;
        r8 = r8.getLineWidth(r7);
        r7 = (double) r8;
        r7 = java.lang.Math.ceil(r7);
        r13 = r1.dialogMuted;
        if (r13 == 0) goto L_0x0e80;
        r13 = r1.drawVerified;
        if (r13 != 0) goto L_0x0e80;
        r13 = r1.nameLeft;
        r13 = (double) r13;
        r38 = r9;
        r39 = r10;
        r9 = (double) r12;
        r9 = r9 - r7;
        r13 = r13 + r9;
        r9 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r9 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r9 = (double) r9;
        r13 = r13 - r9;
        r9 = org.telegram.ui.ActionBar.Theme.dialogs_muteDrawable;
        r9 = r9.getIntrinsicWidth();
        r9 = (double) r9;
        r13 = r13 - r9;
        r9 = (int) r13;
        r1.nameMuteLeft = r9;
        goto L_0x0ea1;
        r38 = r9;
        r39 = r10;
        r9 = r1.drawVerified;
        if (r9 == 0) goto L_0x0ea1;
        r9 = r1.nameLeft;
        r9 = (double) r9;
        r13 = (double) r12;
        r13 = r13 - r7;
        r9 = r9 + r13;
        r13 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r13 = org.telegram.messenger.AndroidUtilities.dp(r13);
        r13 = (double) r13;
        r9 = r9 - r13;
        r13 = org.telegram.ui.ActionBar.Theme.dialogs_verifiedDrawable;
        r13 = r13.getIntrinsicWidth();
        r13 = (double) r13;
        r9 = r9 - r13;
        r9 = (int) r9;
        r1.nameMuteLeft = r9;
        r9 = 0;
        r9 = (r3 > r9 ? 1 : (r3 == r9 ? 0 : -1));
        if (r9 != 0) goto L_0x0eb9;
        r9 = (double) r12;
        r13 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r13 >= 0) goto L_0x0eb9;
        r9 = r1.nameLeft;
        r9 = (double) r9;
        r13 = (double) r12;
        r13 = r13 - r7;
        r9 = r9 + r13;
        r9 = (int) r9;
        r1.nameLeft = r9;
        goto L_0x0eb9;
        r38 = r9;
        r39 = r10;
        r3 = r1.messageLayout;
        if (r3 == 0) goto L_0x0f69;
        r3 = r1.messageLayout;
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x0f69;
        r3 = r1.messageLayout;
        r7 = 0;
        r3 = r3.getLineLeft(r7);
        r8 = 0;
        r8 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x0f69;
        r8 = r1.messageLayout;
        r7 = r8.getLineWidth(r7);
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        r9 = (double) r5;
        r13 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r13 >= 0) goto L_0x0f69;
        r9 = r1.messageLeft;
        r9 = (double) r9;
        r13 = (double) r5;
        r13 = r13 - r7;
        r9 = r9 + r13;
        r9 = (int) r9;
        r1.messageLeft = r9;
        goto L_0x0f69;
        r38 = r9;
        r39 = r10;
        r3 = r1.nameLayout;
        if (r3 == 0) goto L_0x0f38;
        r3 = r1.nameLayout;
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x0f38;
        r3 = r1.nameLayout;
        r7 = 0;
        r3 = r3.getLineRight(r7);
        r8 = (float) r12;
        r8 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x0f21;
        r8 = r1.nameLayout;
        r8 = r8.getLineWidth(r7);
        r7 = (double) r8;
        r7 = java.lang.Math.ceil(r7);
        r9 = (double) r12;
        r13 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r13 >= 0) goto L_0x0f21;
        r9 = r1.nameLeft;
        r9 = (double) r9;
        r13 = (double) r12;
        r13 = r13 - r7;
        r9 = r9 - r13;
        r9 = (int) r9;
        r1.nameLeft = r9;
        r7 = r1.dialogMuted;
        if (r7 != 0) goto L_0x0f29;
        r7 = r1.drawVerified;
        if (r7 == 0) goto L_0x0f38;
        r7 = r1.nameLeft;
        r7 = (float) r7;
        r7 = r7 + r3;
        r8 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = (float) r8;
        r7 = r7 + r8;
        r7 = (int) r7;
        r1.nameMuteLeft = r7;
        r3 = r1.messageLayout;
        if (r3 == 0) goto L_0x0f69;
        r3 = r1.messageLayout;
        r3 = r3.getLineCount();
        if (r3 <= 0) goto L_0x0f69;
        r3 = r1.messageLayout;
        r7 = 0;
        r3 = r3.getLineRight(r7);
        r8 = (float) r5;
        r8 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1));
        if (r8 != 0) goto L_0x0f69;
        r8 = r1.messageLayout;
        r7 = r8.getLineWidth(r7);
        r7 = (double) r7;
        r7 = java.lang.Math.ceil(r7);
        r9 = (double) r5;
        r13 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r13 >= 0) goto L_0x0f69;
        r9 = r1.messageLeft;
        r9 = (double) r9;
        r13 = (double) r5;
        r13 = r13 - r7;
        r9 = r9 - r13;
        r9 = (int) r9;
        r1.messageLeft = r9;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.DialogCell.buildLayout():void");
    }

    public DialogCell(Context context, boolean needCheck) {
        super(context);
        Theme.createDialogsResources(context);
        this.avatarImage.setRoundRadius(AndroidUtilities.dp(26.0f));
        if (needCheck) {
            this.checkBox = new GroupCreateCheckBox(context);
            this.checkBox.setVisibility(0);
            addView(this.checkBox);
        }
    }

    public void setDialog(TL_dialog dialog, int i, int type) {
        this.currentDialogId = dialog.id;
        this.isDialogCell = true;
        this.index = i;
        this.dialogsType = type;
        update(0);
    }

    public void setDialog(CustomDialog dialog) {
        this.customDialog = dialog;
        update(0);
    }

    public void setDialog(long dialog_id, MessageObject messageObject, int date) {
        this.currentDialogId = dialog_id;
        this.message = messageObject;
        this.isDialogCell = false;
        this.lastMessageDate = date;
        this.currentEditDate = messageObject != null ? messageObject.messageOwner.edit_date : 0;
        this.unreadCount = 0;
        this.mentionCount = 0;
        boolean z = messageObject != null && messageObject.isUnread();
        this.lastUnreadState = z;
        if (this.message != null) {
            this.lastSendState = this.message.messageOwner.send_state;
        }
        update(0);
    }

    public long getDialogId() {
        return this.currentDialogId;
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
        if (this.checkBox != null) {
            this.checkBox.measure(MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(24.0f), 1073741824));
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), AndroidUtilities.dp(72.0f) + this.useSeparator);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (this.currentDialogId != 0 || this.customDialog != null) {
            if (this.checkBox != null) {
                int x = LocaleController.isRTL ? (right - left) - AndroidUtilities.dp(42.0f) : AndroidUtilities.dp(42.0f);
                int y = AndroidUtilities.dp(1110179840);
                this.checkBox.layout(x, y, this.checkBox.getMeasuredWidth() + x, this.checkBox.getMeasuredHeight() + y);
            }
            if (changed) {
                try {
                    buildLayout();
                } catch (Throwable e) {
                    FileLog.e(e);
                }
            }
        }
    }

    public void setDialogSelected(boolean value) {
        if (this.isSelected != value) {
            invalidate();
        }
        this.isSelected = value;
    }

    private ArrayList<TL_dialog> getDialogsArray() {
        if (this.dialogsType == 0) {
            return MessagesController.getInstance(this.currentAccount).dialogs;
        }
        if (this.dialogsType == 1) {
            return MessagesController.getInstance(this.currentAccount).dialogsServerOnly;
        }
        if (this.dialogsType == 2) {
            return MessagesController.getInstance(this.currentAccount).dialogsGroupsOnly;
        }
        if (this.dialogsType == 3) {
            return MessagesController.getInstance(this.currentAccount).dialogsForward;
        }
        return null;
    }

    public void checkCurrentDialogIndex() {
        if (this.index < getDialogsArray().size()) {
            TL_dialog dialog = (TL_dialog) getDialogsArray().get(this.index);
            DraftMessage newDraftMessage = DataQuery.getInstance(this.currentAccount).getDraft(this.currentDialogId);
            MessageObject newMessageObject = (MessageObject) MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.id);
            if (this.currentDialogId != dialog.id || ((this.message != null && this.message.getId() != dialog.top_message) || ((newMessageObject != null && newMessageObject.messageOwner.edit_date != this.currentEditDate) || this.unreadCount != dialog.unread_count || this.mentionCount != dialog.unread_mentions_count || this.message != newMessageObject || ((this.message == null && newMessageObject != null) || newDraftMessage != this.draftMessage || this.drawPin != dialog.pinned)))) {
                this.currentDialogId = dialog.id;
                update(0);
            }
        }
    }

    public void setChecked(boolean checked, boolean animated) {
        if (this.checkBox != null) {
            this.checkBox.setChecked(checked, animated);
        }
    }

    public void update(int mask) {
        boolean z = false;
        boolean z2 = true;
        if (this.customDialog != null) {
            this.lastMessageDate = this.customDialog.date;
            if (this.customDialog.unread_count == 0) {
                z2 = false;
            }
            this.lastUnreadState = z2;
            this.unreadCount = this.customDialog.unread_count;
            this.drawPin = this.customDialog.pinned;
            this.dialogMuted = this.customDialog.muted;
            this.avatarDrawable.setInfo(this.customDialog.id, this.customDialog.name, null, false);
            this.avatarImage.setImage(null, "50_50", this.avatarDrawable, null, 0);
        } else {
            if (this.isDialogCell) {
                TL_dialog dialog = (TL_dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                if (dialog != null && mask == 0) {
                    this.message = (MessageObject) MessagesController.getInstance(this.currentAccount).dialogMessage.get(dialog.id);
                    boolean z3 = this.message != null && this.message.isUnread();
                    this.lastUnreadState = z3;
                    this.unreadCount = dialog.unread_count;
                    this.mentionCount = dialog.unread_mentions_count;
                    this.currentEditDate = this.message != null ? this.message.messageOwner.edit_date : 0;
                    this.lastMessageDate = dialog.last_message_date;
                    this.drawPin = dialog.pinned;
                    if (this.message != null) {
                        this.lastSendState = this.message.messageOwner.send_state;
                    }
                }
            } else {
                this.drawPin = false;
            }
            if (mask != 0) {
                boolean continueUpdate = false;
                if (this.isDialogCell && (mask & 64) != 0) {
                    CharSequence printString = (CharSequence) MessagesController.getInstance(this.currentAccount).printingStrings.get(this.currentDialogId);
                    if ((this.lastPrintString != null && printString == null) || ((this.lastPrintString == null && printString != null) || !(this.lastPrintString == null || printString == null || this.lastPrintString.equals(printString)))) {
                        continueUpdate = true;
                    }
                }
                if (!(continueUpdate || (32768 & mask) == 0 || this.message == null || this.message.messageText == this.lastMessageString)) {
                    continueUpdate = true;
                }
                if (!(continueUpdate || (mask & 2) == 0 || this.chat != null)) {
                    continueUpdate = true;
                }
                if (!(continueUpdate || (mask & 1) == 0 || this.chat != null)) {
                    continueUpdate = true;
                }
                if (!(continueUpdate || (mask & 8) == 0 || this.user != null)) {
                    continueUpdate = true;
                }
                if (!(continueUpdate || (mask & 16) == 0 || this.user != null)) {
                    continueUpdate = true;
                }
                if (!(continueUpdate || (mask & 256) == 0)) {
                    if (this.message != null && this.lastUnreadState != this.message.isUnread()) {
                        this.lastUnreadState = this.message.isUnread();
                        continueUpdate = true;
                    } else if (this.isDialogCell) {
                        TL_dialog dialog2 = (TL_dialog) MessagesController.getInstance(this.currentAccount).dialogs_dict.get(this.currentDialogId);
                        if (!(dialog2 == null || (this.unreadCount == dialog2.unread_count && this.mentionCount == dialog2.unread_mentions_count))) {
                            this.unreadCount = dialog2.unread_count;
                            this.mentionCount = dialog2.unread_mentions_count;
                            continueUpdate = true;
                        }
                    }
                }
                if (!(continueUpdate || (mask & 4096) == 0 || this.message == null || this.lastSendState == this.message.messageOwner.send_state)) {
                    this.lastSendState = this.message.messageOwner.send_state;
                    continueUpdate = true;
                }
                if (!continueUpdate) {
                    return;
                }
            }
            if (this.isDialogCell && MessagesController.getInstance(this.currentAccount).isDialogMuted(this.currentDialogId)) {
                z = true;
            }
            this.dialogMuted = z;
            this.user = null;
            this.chat = null;
            this.encryptedChat = null;
            int lower_id = (int) this.currentDialogId;
            int high_id = (int) (this.currentDialogId >> 32);
            if (lower_id == 0) {
                this.encryptedChat = MessagesController.getInstance(this.currentAccount).getEncryptedChat(Integer.valueOf(high_id));
                if (this.encryptedChat != null) {
                    this.user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(this.encryptedChat.user_id));
                }
            } else if (high_id == 1) {
                this.chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(lower_id));
            } else if (lower_id < 0) {
                this.chat = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(-lower_id));
                if (!(this.isDialogCell || this.chat == null || this.chat.migrated_to == null)) {
                    Chat chat2 = MessagesController.getInstance(this.currentAccount).getChat(Integer.valueOf(this.chat.migrated_to.channel_id));
                    if (chat2 != null) {
                        this.chat = chat2;
                    }
                }
            } else {
                this.user = MessagesController.getInstance(this.currentAccount).getUser(Integer.valueOf(lower_id));
            }
            TLObject photo = null;
            if (this.user != null) {
                this.avatarDrawable.setInfo(this.user);
                if (UserObject.isUserSelf(this.user)) {
                    this.avatarDrawable.setSavedMessages(1);
                } else if (this.user.photo != null) {
                    photo = this.user.photo.photo_small;
                }
            } else if (this.chat != null) {
                if (this.chat.photo != null) {
                    photo = this.chat.photo.photo_small;
                }
                this.avatarDrawable.setInfo(this.chat);
            }
            this.avatarImage.setImage(photo, "50_50", this.avatarDrawable, null, 0);
        }
        if (getMeasuredWidth() == 0) {
            if (getMeasuredHeight() == 0) {
                requestLayout();
                invalidate();
            }
        }
        buildLayout();
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        if (this.currentDialogId != 0 || this.customDialog != null) {
            if (this.isSelected) {
                canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), Theme.dialogs_tabletSeletedPaint);
            }
            if (this.drawPin || this.drawPinBackground) {
                canvas.drawRect(0.0f, 0.0f, (float) getMeasuredWidth(), (float) getMeasuredHeight(), Theme.dialogs_pinnedPaint);
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
            if (this.timeLayout != null) {
                canvas.save();
                canvas.translate((float) this.timeLeft, (float) this.timeTop);
                this.timeLayout.draw(canvas);
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
            if (this.drawClock) {
                BaseCell.setDrawableBounds(Theme.dialogs_clockDrawable, this.checkDrawLeft, this.checkDrawTop);
                Theme.dialogs_clockDrawable.draw(canvas);
            } else if (this.drawCheck2) {
                if (this.drawCheck1) {
                    BaseCell.setDrawableBounds(Theme.dialogs_halfCheckDrawable, this.halfCheckDrawLeft, this.checkDrawTop);
                    Theme.dialogs_halfCheckDrawable.draw(canvas);
                    BaseCell.setDrawableBounds(Theme.dialogs_checkDrawable, this.checkDrawLeft, this.checkDrawTop);
                    Theme.dialogs_checkDrawable.draw(canvas);
                } else {
                    BaseCell.setDrawableBounds(Theme.dialogs_checkDrawable, this.checkDrawLeft, this.checkDrawTop);
                    Theme.dialogs_checkDrawable.draw(canvas);
                }
            }
            if (this.dialogMuted && !this.drawVerified) {
                BaseCell.setDrawableBounds(Theme.dialogs_muteDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
                Theme.dialogs_muteDrawable.draw(canvas);
            } else if (this.drawVerified) {
                BaseCell.setDrawableBounds(Theme.dialogs_verifiedDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
                BaseCell.setDrawableBounds(Theme.dialogs_verifiedCheckDrawable, this.nameMuteLeft, AndroidUtilities.dp(16.5f));
                Theme.dialogs_verifiedDrawable.draw(canvas);
                Theme.dialogs_verifiedCheckDrawable.draw(canvas);
            }
            if (this.drawError) {
                this.rect.set((float) this.errorLeft, (float) this.errorTop, (float) (this.errorLeft + AndroidUtilities.dp(23.0f)), (float) (this.errorTop + AndroidUtilities.dp(23.0f)));
                canvas.drawRoundRect(this.rect, AndroidUtilities.density * 11.5f, 11.5f * AndroidUtilities.density, Theme.dialogs_errorPaint);
                BaseCell.setDrawableBounds(Theme.dialogs_errorDrawable, this.errorLeft + AndroidUtilities.dp(5.5f), this.errorTop + AndroidUtilities.dp(5.0f));
                Theme.dialogs_errorDrawable.draw(canvas);
            } else {
                int x;
                if (!this.drawCount) {
                    if (!this.drawMention) {
                        if (this.drawPin) {
                            BaseCell.setDrawableBounds(Theme.dialogs_pinnedDrawable, this.pinLeft, this.pinTop);
                            Theme.dialogs_pinnedDrawable.draw(canvas);
                        }
                    }
                }
                if (this.drawCount) {
                    x = this.countLeft - AndroidUtilities.dp(5.5f);
                    this.rect.set((float) x, (float) this.countTop, (float) ((this.countWidth + x) + AndroidUtilities.dp(11.0f)), (float) (this.countTop + AndroidUtilities.dp(23.0f)));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.density * 11.5f, AndroidUtilities.density * 11.5f, this.dialogMuted ? Theme.dialogs_countGrayPaint : Theme.dialogs_countPaint);
                    canvas.save();
                    canvas.translate((float) this.countLeft, (float) (this.countTop + AndroidUtilities.dp(4.0f)));
                    if (this.countLayout != null) {
                        this.countLayout.draw(canvas);
                    }
                    canvas.restore();
                }
                if (this.drawMention) {
                    x = this.mentionLeft - AndroidUtilities.dp(5.5f);
                    this.rect.set((float) x, (float) this.countTop, (float) ((this.mentionWidth + x) + AndroidUtilities.dp(11.0f)), (float) (this.countTop + AndroidUtilities.dp(23.0f)));
                    canvas.drawRoundRect(this.rect, AndroidUtilities.density * 11.5f, 11.5f * AndroidUtilities.density, Theme.dialogs_countPaint);
                    BaseCell.setDrawableBounds(Theme.dialogs_mentionDrawable, this.mentionLeft - AndroidUtilities.dp(2.0f), this.countTop + AndroidUtilities.dp(3.2f), AndroidUtilities.dp(16.0f), AndroidUtilities.dp(16.0f));
                    Theme.dialogs_mentionDrawable.draw(canvas);
                }
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
    }

    public boolean hasOverlappingRendering() {
        return false;
    }
}
