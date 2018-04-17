package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_documentEmpty;
import org.telegram.tgnet.TLRPC.TL_messageActionUserUpdatedPhoto;
import org.telegram.tgnet.TLRPC.TL_photoEmpty;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.PhotoViewer;

public class ChatActionCell extends BaseCell {
    private AvatarDrawable avatarDrawable;
    private int currentAccount = UserConfig.selectedAccount;
    private MessageObject currentMessageObject;
    private int customDate;
    private CharSequence customText;
    private ChatActionCellDelegate delegate;
    private boolean hasReplyMessage;
    private boolean imagePressed = false;
    private ImageReceiver imageReceiver = new ImageReceiver(this);
    private URLSpan pressedLink;
    private int previousWidth = 0;
    private int textHeight = 0;
    private StaticLayout textLayout;
    private int textWidth = 0;
    private int textX = 0;
    private int textXLeft = 0;
    private int textY = 0;

    public interface ChatActionCellDelegate {
        void didClickedImage(ChatActionCell chatActionCell);

        void didLongPressed(ChatActionCell chatActionCell);

        void didPressedBotButton(MessageObject messageObject, KeyboardButton keyboardButton);

        void didPressedReplyMessage(ChatActionCell chatActionCell, int i);

        void needOpenUserProfile(int i);
    }

    protected void onDraw(android.graphics.Canvas r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ChatActionCell.onDraw(android.graphics.Canvas):void
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
        r6 = r40;
        r7 = r41;
        r0 = r6.currentMessageObject;
        if (r0 == 0) goto L_0x0015;
    L_0x0008:
        r0 = r6.currentMessageObject;
        r0 = r0.type;
        r1 = 11;
        if (r0 != r1) goto L_0x0015;
    L_0x0010:
        r0 = r6.imageReceiver;
        r0.draw(r7);
    L_0x0015:
        r0 = r6.textLayout;
        if (r0 == 0) goto L_0x04ec;
    L_0x0019:
        r0 = r6.textLayout;
        r8 = r0.getLineCount();
        r9 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r11 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r12 = org.telegram.messenger.AndroidUtilities.dp(r11);
        r13 = r10 - r12;
        r0 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r14 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r0 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r0 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r1 = 0;
        r2 = r0;
        r0 = 0;
        r5 = r0;
        if (r5 >= r8) goto L_0x04d4;
    L_0x003f:
        r0 = r6.findMaxWidthAroundLine(r5);
        r3 = r40.getMeasuredWidth();
        r3 = r3 - r0;
        r3 = r3 - r13;
        r4 = 2;
        r3 = r3 / r4;
        r0 = r0 + r13;
        r15 = r6.textLayout;
        r15 = r15.getLineBottom(r5);
        r16 = r15 - r1;
        r17 = 0;
        r18 = r15;
        r1 = r8 + -1;
        r11 = 1;
        if (r5 != r1) goto L_0x005f;
    L_0x005d:
        r1 = r11;
        goto L_0x0060;
    L_0x005f:
        r1 = 0;
    L_0x0060:
        if (r5 != 0) goto L_0x0065;
    L_0x0062:
        r20 = r11;
        goto L_0x0067;
    L_0x0065:
        r20 = 0;
    L_0x0067:
        r9 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        if (r20 == 0) goto L_0x0077;
    L_0x006b:
        r22 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r2 = r2 - r22;
        r22 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r16 = r16 + r22;
    L_0x0077:
        r22 = r2;
        if (r1 == 0) goto L_0x0081;
    L_0x007b:
        r2 = org.telegram.messenger.AndroidUtilities.dp(r9);
        r16 = r16 + r2;
    L_0x0081:
        r2 = r22;
        r23 = r16;
        r24 = 0;
        r25 = 0;
        r26 = 0;
        r27 = 0;
        if (r1 != 0) goto L_0x00b1;
    L_0x008f:
        r9 = r5 + 1;
        if (r9 >= r8) goto L_0x00b1;
    L_0x0093:
        r9 = r5 + 1;
        r9 = r6.findMaxWidthAroundLine(r9);
        r9 = r9 + r13;
        r26 = r13 * 2;
        r4 = r9 + r26;
        if (r4 >= r0) goto L_0x00a6;
    L_0x00a0:
        r24 = 1;
        r1 = 1;
    L_0x00a3:
        r26 = r9;
        goto L_0x00b1;
    L_0x00a6:
        r4 = r13 * 2;
        r4 = r4 + r0;
        if (r4 >= r9) goto L_0x00ae;
    L_0x00ab:
        r24 = 2;
        goto L_0x00a3;
    L_0x00ae:
        r24 = 3;
        goto L_0x00a3;
    L_0x00b1:
        r9 = r24;
        r24 = r1;
        if (r20 != 0) goto L_0x00d7;
    L_0x00b7:
        if (r5 <= 0) goto L_0x00d7;
    L_0x00b9:
        r1 = r5 + -1;
        r1 = r6.findMaxWidthAroundLine(r1);
        r1 = r1 + r13;
        r4 = r13 * 2;
        r4 = r4 + r1;
        if (r4 >= r0) goto L_0x00cc;
    L_0x00c5:
        r25 = 1;
        r20 = 1;
    L_0x00c9:
        r27 = r1;
        goto L_0x00d7;
    L_0x00cc:
        r4 = r13 * 2;
        r4 = r4 + r0;
        if (r4 >= r1) goto L_0x00d4;
    L_0x00d1:
        r25 = 2;
        goto L_0x00c9;
    L_0x00d4:
        r25 = 3;
        goto L_0x00c9;
    L_0x00d7:
        r4 = r25;
        if (r9 == 0) goto L_0x0219;
    L_0x00db:
        if (r9 != r11) goto L_0x0175;
    L_0x00dd:
        r25 = r40.getMeasuredWidth();
        r25 = r25 - r26;
        r29 = 2;
        r11 = r25 / 2;
        r1 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r17 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r25 = r5 + 1;
        r30 = r0;
        r0 = r6;
        r31 = r15;
        r15 = 3;
        r1 = r26;
        r32 = r2;
        r2 = r30;
        r15 = r3;
        r3 = r25;
        r6 = r4;
        r33 = r10;
        r10 = r29;
        r4 = r8;
        r25 = r5;
        r5 = r13;
        r0 = r0.isLineBottom(r1, r2, r3, r4, r5);
        if (r0 == 0) goto L_0x0142;
    L_0x010d:
        r3 = r15 + r12;
        r1 = (float) r3;
        r0 = r22 + r16;
        r2 = (float) r0;
        r0 = r11 - r13;
        r3 = (float) r0;
        r0 = r22 + r16;
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r0 + r5;
        r4 = (float) r0;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r11 + r26;
        r0 = r0 + r13;
        r1 = (float) r0;
        r0 = r22 + r16;
        r2 = (float) r0;
        r3 = r15 + r30;
        r3 = r3 - r12;
        r3 = (float) r3;
        r0 = r22 + r16;
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r0 + r5;
        r4 = (float) r0;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        goto L_0x0173;
    L_0x0142:
        r3 = r15 + r12;
        r1 = (float) r3;
        r0 = r22 + r16;
        r2 = (float) r0;
        r3 = (float) r11;
        r0 = r22 + r16;
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r0 + r5;
        r4 = (float) r0;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r11 + r26;
        r1 = (float) r0;
        r0 = r22 + r16;
        r2 = (float) r0;
        r3 = r15 + r30;
        r3 = r3 - r12;
        r3 = (float) r3;
        r0 = r22 + r16;
        r4 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r0 + r5;
        r4 = (float) r0;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
    L_0x0173:
        goto L_0x0225;
    L_0x0175:
        r30 = r0;
        r32 = r2;
        r6 = r4;
        r25 = r5;
        r33 = r10;
        r31 = r15;
        r10 = 2;
        r15 = r3;
        if (r9 != r10) goto L_0x0212;
    L_0x0184:
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r17 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r0 = r22 + r16;
        r1 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r11 = r0 - r2;
        r3 = r15 - r14;
        if (r6 == r10) goto L_0x019c;
    L_0x0198:
        r0 = 3;
        if (r6 == r0) goto L_0x019c;
    L_0x019b:
        r3 = r3 - r13;
    L_0x019c:
        r5 = r3;
        if (r20 != 0) goto L_0x01a5;
    L_0x019f:
        if (r24 == 0) goto L_0x01a2;
    L_0x01a1:
        goto L_0x01a5;
    L_0x01a2:
        r34 = r5;
        goto L_0x01c2;
    L_0x01a5:
        r0 = r5 + r14;
        r1 = (float) r0;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r2 = r2 + r11;
        r2 = (float) r2;
        r0 = r5 + r14;
        r0 = r0 + r33;
        r3 = (float) r0;
        r0 = r11 + r33;
        r4 = (float) r0;
        r29 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r34 = r5;
        r5 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
    L_0x01c2:
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerInner;
        r0 = r0[r10];
        r3 = r34;
        r5 = r3 + r14;
        r1 = r11 + r14;
        r0.setBounds(r3, r11, r5, r1);
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerInner;
        r0 = r0[r10];
        r0.draw(r7);
        r3 = r15 + r30;
        if (r6 == r10) goto L_0x01de;
    L_0x01da:
        r0 = 3;
        if (r6 == r0) goto L_0x01de;
    L_0x01dd:
        r3 = r3 + r13;
    L_0x01de:
        r5 = r3;
        if (r20 != 0) goto L_0x01e6;
    L_0x01e1:
        if (r24 == 0) goto L_0x01e4;
    L_0x01e3:
        goto L_0x01e6;
    L_0x01e4:
        r10 = r5;
        goto L_0x01fe;
    L_0x01e6:
        r0 = r5 - r33;
        r1 = (float) r0;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r2 = r2 + r11;
        r2 = (float) r2;
        r3 = (float) r5;
        r0 = r11 + r33;
        r4 = (float) r0;
        r29 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r10 = r5;
        r5 = r29;
        r0.drawRect(r1, r2, r3, r4, r5);
    L_0x01fe:
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerInner;
        r1 = 3;
        r0 = r0[r1];
        r5 = r10 + r14;
        r2 = r11 + r14;
        r0.setBounds(r10, r11, r5, r2);
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerInner;
        r0 = r0[r1];
        r0.draw(r7);
        goto L_0x0225;
    L_0x0212:
        r0 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r17 = org.telegram.messenger.AndroidUtilities.dp(r0);
        goto L_0x0225;
    L_0x0219:
        r30 = r0;
        r32 = r2;
        r6 = r4;
        r25 = r5;
        r33 = r10;
        r31 = r15;
        r15 = r3;
    L_0x0225:
        if (r6 == 0) goto L_0x036e;
    L_0x0227:
        r0 = 1;
        if (r6 != r0) goto L_0x02b5;
    L_0x022a:
        r10 = r6;
        r6 = r40;
        r0 = r40.getMeasuredWidth();
        r0 = r0 - r27;
        r1 = 2;
        r11 = r0 / 2;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r5 = r22 - r1;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r16 = r16 + r1;
        r3 = r25 + -1;
        r0 = r6;
        r1 = r27;
        r2 = r30;
        r4 = r8;
        r35 = r8;
        r8 = r5;
        r5 = r13;
        r0 = r0.isLineTop(r1, r2, r3, r4, r5);
        if (r0 == 0) goto L_0x0285;
    L_0x0256:
        r3 = r15 + r12;
        r1 = (float) r3;
        r2 = (float) r8;
        r0 = r11 - r13;
        r3 = (float) r0;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r5 = r8 + r4;
        r4 = (float) r5;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r11 + r27;
        r0 = r0 + r13;
        r1 = (float) r0;
        r2 = (float) r8;
        r3 = r15 + r30;
        r3 = r3 - r12;
        r3 = (float) r3;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r0 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r5 = r8 + r0;
        r4 = (float) r5;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        goto L_0x02b0;
    L_0x0285:
        r3 = r15 + r12;
        r1 = (float) r3;
        r2 = (float) r8;
        r3 = (float) r11;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r5 = r8 + r4;
        r4 = (float) r5;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = r11 + r27;
        r1 = (float) r0;
        r2 = (float) r8;
        r3 = r15 + r30;
        r3 = r3 - r12;
        r3 = (float) r3;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r0 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r5 = r8 + r0;
        r4 = (float) r5;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        r28 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        goto L_0x0377;
    L_0x02b5:
        r10 = r6;
        r35 = r8;
        r6 = r40;
        r0 = 2;
        if (r10 != r0) goto L_0x035b;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r22 = r22 - r1;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r16 = r16 + r1;
        r0 = 1086744166; // 0x40c66666 float:6.2 double:5.36922958E-315;
        r0 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r8 = r22 + r0;
        r3 = r15 - r14;
        r0 = 2;
        if (r9 == r0) goto L_0x02dd;
        r0 = 3;
        if (r9 == r0) goto L_0x02dd;
        r3 = r3 - r13;
        r11 = r3;
        if (r20 != 0) goto L_0x02e2;
        if (r24 == 0) goto L_0x0302;
        r0 = r11 + r14;
        r1 = (float) r0;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r2 = r22 + r2;
        r2 = (float) r2;
        r0 = r11 + r14;
        r0 = r0 + r33;
        r3 = (float) r0;
        r0 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r4 = r22 + r4;
        r4 = (float) r4;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerInner;
        r1 = 0;
        r0 = r0[r1];
        r2 = r11 + r14;
        r3 = r8 + r14;
        r0.setBounds(r11, r8, r2, r3);
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerInner;
        r0 = r0[r1];
        r0.draw(r7);
        r3 = r15 + r30;
        r0 = 2;
        if (r9 == r0) goto L_0x031e;
        r0 = 3;
        if (r9 == r0) goto L_0x031e;
        r3 = r3 + r13;
        r11 = r3;
        if (r20 != 0) goto L_0x0327;
        if (r24 == 0) goto L_0x0324;
        goto L_0x0327;
        r28 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        goto L_0x0347;
        r0 = r11 - r33;
        r1 = (float) r0;
        r0 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        r0 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r0 = r22 + r0;
        r2 = (float) r0;
        r3 = (float) r11;
        r5 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r0 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r0 = r22 + r0;
        r4 = (float) r0;
        r21 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r28 = r5;
        r5 = r21;
        r0.drawRect(r1, r2, r3, r4, r5);
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerInner;
        r1 = 1;
        r0 = r0[r1];
        r2 = r11 + r14;
        r3 = r8 + r14;
        r0.setBounds(r11, r8, r2, r3);
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerInner;
        r0 = r0[r1];
        r0.draw(r7);
        goto L_0x036b;
        r28 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r0 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r22 = r22 - r1;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r16 = r16 + r1;
        r8 = r22;
        goto L_0x0377;
    L_0x036e:
        r10 = r6;
        r35 = r8;
        r6 = r40;
        r28 = 1093664768; // 0x41300000 float:11.0 double:5.4034219E-315;
        r8 = r22;
        if (r20 != 0) goto L_0x038f;
        if (r24 == 0) goto L_0x037e;
        r11 = r32;
        goto L_0x0391;
        r1 = (float) r15;
        r11 = r32;
        r2 = (float) r11;
        r3 = r15 + r30;
        r3 = (float) r3;
        r0 = r11 + r23;
        r4 = (float) r0;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        goto L_0x03a2;
        r11 = r32;
        r3 = r15 + r12;
        r1 = (float) r3;
        r2 = (float) r11;
        r3 = r15 + r30;
        r3 = r3 - r12;
        r3 = (float) r3;
        r0 = r11 + r23;
        r4 = (float) r0;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        r5 = r15 - r13;
        r3 = r15 + r30;
        r4 = r3 - r12;
        if (r20 == 0) goto L_0x03f5;
        if (r24 != 0) goto L_0x03f5;
        r0 = 2;
        if (r9 == r0) goto L_0x03f5;
        r1 = (float) r5;
        r0 = r8 + r33;
        r2 = (float) r0;
        r0 = r5 + r33;
        r3 = (float) r0;
        r0 = r8 + r16;
        r0 = r0 + r17;
        r36 = r4;
        r4 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r21 = org.telegram.messenger.AndroidUtilities.dp(r4);
        r0 = r0 - r21;
        r4 = (float) r0;
        r21 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r37 = r9;
        r9 = r36;
        r38 = r11;
        r11 = r5;
        r5 = r21;
        r0.drawRect(r1, r2, r3, r4, r5);
        r1 = (float) r9;
        r0 = r8 + r33;
        r2 = (float) r0;
        r4 = r9 + r33;
        r3 = (float) r4;
        r0 = r8 + r16;
        r0 = r0 + r17;
        r5 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r0 = r0 - r4;
        r4 = (float) r0;
        r19 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r21 = r5;
        r5 = r19;
        r0.drawRect(r1, r2, r3, r4, r5);
        r39 = r10;
        goto L_0x046c;
        r37 = r9;
        r38 = r11;
        r21 = 1086324736; // 0x40c00000 float:6.0 double:5.367157323E-315;
        r9 = r4;
        r11 = r5;
        if (r24 == 0) goto L_0x043e;
        if (r20 != 0) goto L_0x043e;
        r0 = 2;
        if (r10 == r0) goto L_0x043e;
        r1 = (float) r11;
        r0 = r8 + r33;
        r5 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r0 = r0 - r2;
        r2 = (float) r0;
        r0 = r11 + r33;
        r3 = (float) r0;
        r0 = r8 + r16;
        r0 = r0 + r17;
        r0 = r0 - r33;
        r4 = (float) r0;
        r19 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r39 = r10;
        r10 = r5;
        r5 = r19;
        r0.drawRect(r1, r2, r3, r4, r5);
        r1 = (float) r9;
        r0 = r8 + r33;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r0 = r0 - r2;
        r2 = (float) r0;
        r4 = r9 + r33;
        r3 = (float) r4;
        r0 = r8 + r16;
        r0 = r0 + r17;
        r0 = r0 - r33;
        r4 = (float) r0;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        goto L_0x046c;
        r39 = r10;
        if (r20 != 0) goto L_0x0444;
        if (r24 == 0) goto L_0x046c;
        r1 = (float) r11;
        r10 = r8 + r33;
        r2 = (float) r10;
        r5 = r11 + r33;
        r3 = (float) r5;
        r0 = r8 + r16;
        r0 = r0 + r17;
        r0 = r0 - r33;
        r4 = (float) r0;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        r1 = (float) r9;
        r10 = r8 + r33;
        r2 = (float) r10;
        r4 = r9 + r33;
        r3 = (float) r4;
        r0 = r8 + r16;
        r0 = r0 + r17;
        r0 = r0 - r33;
        r4 = (float) r0;
        r5 = org.telegram.ui.ActionBar.Theme.chat_actionBackgroundPaint;
        r0 = r7;
        r0.drawRect(r1, r2, r3, r4, r5);
        if (r20 == 0) goto L_0x0495;
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerOuter;
        r3 = 0;
        r0 = r0[r3];
        r5 = r11 + r33;
        r10 = r8 + r33;
        r0.setBounds(r11, r8, r5, r10);
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerOuter;
        r0 = r0[r3];
        r0.draw(r7);
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerOuter;
        r1 = 1;
        r0 = r0[r1];
        r4 = r9 + r33;
        r10 = r8 + r33;
        r0.setBounds(r9, r8, r4, r10);
        r0 = org.telegram.ui.ActionBar.Theme.chat_cornerOuter;
        r0 = r0[r1];
        r0.draw(r7);
        goto L_0x0496;
        r3 = 0;
        if (r24 == 0) goto L_0x04c4;
        r0 = r8 + r16;
        r0 = r0 + r17;
        r0 = r0 - r33;
        r1 = org.telegram.ui.ActionBar.Theme.chat_cornerOuter;
        r2 = 2;
        r1 = r1[r2];
        r4 = r9 + r33;
        r10 = r0 + r33;
        r1.setBounds(r9, r0, r4, r10);
        r1 = org.telegram.ui.ActionBar.Theme.chat_cornerOuter;
        r1 = r1[r2];
        r1.draw(r7);
        r1 = org.telegram.ui.ActionBar.Theme.chat_cornerOuter;
        r2 = 3;
        r1 = r1[r2];
        r5 = r11 + r33;
        r10 = r0 + r33;
        r1.setBounds(r11, r0, r5, r10);
        r1 = org.telegram.ui.ActionBar.Theme.chat_cornerOuter;
        r1 = r1[r2];
        r1.draw(r7);
        r2 = r8 + r16;
        r0 = r25 + 1;
        r1 = r18;
        r11 = r21;
        r9 = r28;
        r10 = r33;
        r8 = r35;
        goto L_0x003c;
    L_0x04d4:
        r35 = r8;
        r33 = r10;
        r41.save();
        r0 = r6.textXLeft;
        r0 = (float) r0;
        r3 = r6.textY;
        r3 = (float) r3;
        r7.translate(r0, r3);
        r0 = r6.textLayout;
        r0.draw(r7);
        r41.restore();
    L_0x04ec:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatActionCell.onDraw(android.graphics.Canvas):void");
    }

    public ChatActionCell(Context context) {
        super(context);
        this.imageReceiver.setRoundRadius(AndroidUtilities.dp(32.0f));
        this.avatarDrawable = new AvatarDrawable();
    }

    public void setDelegate(ChatActionCellDelegate delegate) {
        this.delegate = delegate;
    }

    public void setCustomDate(int date) {
        if (this.customDate != date) {
            CharSequence newText = LocaleController.formatDateChat((long) date);
            if (this.customText == null || !TextUtils.equals(newText, this.customText)) {
                this.previousWidth = 0;
                this.customDate = date;
                this.customText = newText;
                if (getMeasuredWidth() != 0) {
                    createLayout(this.customText, getMeasuredWidth());
                    invalidate();
                }
                AndroidUtilities.runOnUIThread(new Runnable() {
                    public void run() {
                        ChatActionCell.this.requestLayout();
                    }
                });
            }
        }
    }

    public void setMessageObject(MessageObject messageObject) {
        if (this.currentMessageObject != messageObject || (!this.hasReplyMessage && messageObject.replyMessageObject != null)) {
            this.currentMessageObject = messageObject;
            this.hasReplyMessage = messageObject.replyMessageObject != null;
            this.previousWidth = 0;
            if (this.currentMessageObject.type == 11) {
                int id = 0;
                if (messageObject.messageOwner.to_id != null) {
                    if (messageObject.messageOwner.to_id.chat_id != 0) {
                        id = messageObject.messageOwner.to_id.chat_id;
                    } else if (messageObject.messageOwner.to_id.channel_id != 0) {
                        id = messageObject.messageOwner.to_id.channel_id;
                    } else {
                        id = messageObject.messageOwner.to_id.user_id;
                        if (id == UserConfig.getInstance(this.currentAccount).getClientUserId()) {
                            id = messageObject.messageOwner.from_id;
                        }
                    }
                }
                this.avatarDrawable.setInfo(id, null, null, false);
                if (this.currentMessageObject.messageOwner.action instanceof TL_messageActionUserUpdatedPhoto) {
                    this.imageReceiver.setImage(this.currentMessageObject.messageOwner.action.newUserPhoto.photo_small, "50_50", this.avatarDrawable, null, 0);
                } else {
                    PhotoSize photo = FileLoader.getClosestPhotoSizeWithSize(this.currentMessageObject.photoThumbs, AndroidUtilities.dp(64.0f));
                    if (photo != null) {
                        this.imageReceiver.setImage(photo.location, "50_50", this.avatarDrawable, null, 0);
                    } else {
                        this.imageReceiver.setImageBitmap(this.avatarDrawable);
                    }
                }
                this.imageReceiver.setVisible(true ^ PhotoViewer.isShowingImage(this.currentMessageObject), false);
            } else {
                this.imageReceiver.setImageBitmap((Bitmap) null);
            }
            requestLayout();
        }
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    public ImageReceiver getPhotoImage() {
        return this.imageReceiver;
    }

    protected void onLongPress() {
        if (this.delegate != null) {
            this.delegate.didLongPressed(this);
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.currentMessageObject == null) {
            return super.onTouchEvent(event);
        }
        float x = event.getX();
        float y = event.getY();
        boolean result = false;
        if (event.getAction() != 0) {
            if (event.getAction() != 2) {
                cancelCheckLongPress();
            }
            if (this.imagePressed) {
                if (event.getAction() == 1) {
                    this.imagePressed = false;
                    if (this.delegate != null) {
                        this.delegate.didClickedImage(this);
                        playSoundEffect(0);
                    }
                } else if (event.getAction() == 3) {
                    this.imagePressed = false;
                } else if (event.getAction() == 2 && !this.imageReceiver.isInsideImage(x, y)) {
                    this.imagePressed = false;
                }
            }
        } else if (this.delegate != null) {
            if (this.currentMessageObject.type == 11 && this.imageReceiver.isInsideImage(x, y)) {
                this.imagePressed = true;
                result = true;
            }
            if (result) {
                startCheckLongPress();
            }
        }
        if (!result && (event.getAction() == 0 || (this.pressedLink != null && event.getAction() == 1))) {
            if (x < ((float) this.textX) || y < ((float) this.textY) || x > ((float) (this.textX + this.textWidth)) || y > ((float) (this.textY + this.textHeight))) {
                this.pressedLink = null;
            } else {
                x -= (float) this.textXLeft;
                int line = this.textLayout.getLineForVertical((int) (y - ((float) this.textY)));
                int off = this.textLayout.getOffsetForHorizontal(line, x);
                float left = this.textLayout.getLineLeft(line);
                if (left > x || this.textLayout.getLineWidth(line) + left < x || !(this.currentMessageObject.messageText instanceof Spannable)) {
                    this.pressedLink = null;
                } else {
                    URLSpan[] link = (URLSpan[]) this.currentMessageObject.messageText.getSpans(off, off, URLSpan.class);
                    if (link.length == 0) {
                        this.pressedLink = null;
                    } else if (event.getAction() == 0) {
                        this.pressedLink = link[0];
                        result = true;
                    } else if (link[0] == this.pressedLink) {
                        if (this.delegate != null) {
                            String url = link[0].getURL();
                            if (url.startsWith("game")) {
                                this.delegate.didPressedReplyMessage(this, this.currentMessageObject.messageOwner.reply_to_msg_id);
                            } else if (url.startsWith("http")) {
                                Browser.openUrl(getContext(), url);
                            } else {
                                this.delegate.needOpenUserProfile(Integer.parseInt(url));
                            }
                        }
                        result = true;
                    }
                }
            }
        }
        if (!result) {
            result = super.onTouchEvent(event);
        }
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void createLayout(java.lang.CharSequence r11, int r12) {
        /*
        r10 = this;
        r0 = 1106247680; // 0x41f00000 float:30.0 double:5.465589745E-315;
        r0 = org.telegram.messenger.AndroidUtilities.dp(r0);
        r0 = r12 - r0;
        r9 = new android.text.StaticLayout;
        r3 = org.telegram.ui.ActionBar.Theme.chat_actionTextPaint;
        r5 = android.text.Layout.Alignment.ALIGN_CENTER;
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r7 = 0;
        r8 = 0;
        r1 = r9;
        r2 = r11;
        r4 = r0;
        r1.<init>(r2, r3, r4, r5, r6, r7, r8);
        r10.textLayout = r9;
        r1 = 0;
        r10.textHeight = r1;
        r10.textWidth = r1;
        r2 = r10.textLayout;	 Catch:{ Exception -> 0x0063 }
        r2 = r2.getLineCount();	 Catch:{ Exception -> 0x0063 }
    L_0x0026:
        if (r1 >= r2) goto L_0x0062;
    L_0x0028:
        r3 = r10.textLayout;	 Catch:{ Exception -> 0x005d }
        r3 = r3.getLineWidth(r1);	 Catch:{ Exception -> 0x005d }
        r4 = (float) r0;	 Catch:{ Exception -> 0x005d }
        r4 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r4 <= 0) goto L_0x0034;
    L_0x0033:
        r3 = (float) r0;	 Catch:{ Exception -> 0x005d }
    L_0x0034:
        r4 = r10.textHeight;	 Catch:{ Exception -> 0x005d }
        r4 = (double) r4;	 Catch:{ Exception -> 0x005d }
        r6 = r10.textLayout;	 Catch:{ Exception -> 0x005d }
        r6 = r6.getLineBottom(r1);	 Catch:{ Exception -> 0x005d }
        r6 = (double) r6;	 Catch:{ Exception -> 0x005d }
        r6 = java.lang.Math.ceil(r6);	 Catch:{ Exception -> 0x005d }
        r4 = java.lang.Math.max(r4, r6);	 Catch:{ Exception -> 0x005d }
        r4 = (int) r4;	 Catch:{ Exception -> 0x005d }
        r10.textHeight = r4;	 Catch:{ Exception -> 0x005d }
        r4 = r10.textWidth;	 Catch:{ Exception -> 0x0063 }
        r4 = (double) r4;	 Catch:{ Exception -> 0x0063 }
        r6 = (double) r3;	 Catch:{ Exception -> 0x0063 }
        r6 = java.lang.Math.ceil(r6);	 Catch:{ Exception -> 0x0063 }
        r4 = java.lang.Math.max(r4, r6);	 Catch:{ Exception -> 0x0063 }
        r4 = (int) r4;	 Catch:{ Exception -> 0x0063 }
        r10.textWidth = r4;	 Catch:{ Exception -> 0x0063 }
        r1 = r1 + 1;
        goto L_0x0026;
    L_0x005d:
        r3 = move-exception;
        org.telegram.messenger.FileLog.e(r3);	 Catch:{ Exception -> 0x0063 }
        return;
    L_0x0062:
        goto L_0x0067;
    L_0x0063:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
    L_0x0067:
        r1 = r10.textWidth;
        r1 = r12 - r1;
        r1 = r1 / 2;
        r10.textX = r1;
        r1 = 1088421888; // 0x40e00000 float:7.0 double:5.37751863E-315;
        r1 = org.telegram.messenger.AndroidUtilities.dp(r1);
        r10.textY = r1;
        r1 = r10.textLayout;
        r1 = r1.getWidth();
        r1 = r12 - r1;
        r1 = r1 / 2;
        r10.textXLeft = r1;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ChatActionCell.createLayout(java.lang.CharSequence, int):void");
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.currentMessageObject == null && this.customText == null) {
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), this.textHeight + AndroidUtilities.dp(14.0f));
            return;
        }
        int width = Math.max(AndroidUtilities.dp(30.0f), MeasureSpec.getSize(widthMeasureSpec));
        if (width != this.previousWidth) {
            CharSequence text;
            if (this.currentMessageObject == null) {
                text = this.customText;
            } else if (this.currentMessageObject.messageOwner == null || this.currentMessageObject.messageOwner.media == null || this.currentMessageObject.messageOwner.media.ttl_seconds == 0) {
                text = this.currentMessageObject.messageText;
            } else if (this.currentMessageObject.messageOwner.media.photo instanceof TL_photoEmpty) {
                text = LocaleController.getString("AttachPhotoExpired", R.string.AttachPhotoExpired);
            } else if (this.currentMessageObject.messageOwner.media.document instanceof TL_documentEmpty) {
                text = LocaleController.getString("AttachVideoExpired", R.string.AttachVideoExpired);
            } else {
                text = this.currentMessageObject.messageText;
            }
            this.previousWidth = width;
            createLayout(text, width);
            if (this.currentMessageObject != null && this.currentMessageObject.type == 11) {
                this.imageReceiver.setImageCoords((width - AndroidUtilities.dp(64.0f)) / 2, this.textHeight + AndroidUtilities.dp(15.0f), AndroidUtilities.dp(64.0f), AndroidUtilities.dp(64.0f));
            }
        }
        int i = this.textHeight;
        int i2 = (this.currentMessageObject == null || this.currentMessageObject.type != 11) ? 0 : 70;
        setMeasuredDimension(width, i + AndroidUtilities.dp((float) (14 + i2)));
    }

    public int getCustomDate() {
        return this.customDate;
    }

    private int findMaxWidthAroundLine(int line) {
        int a;
        int width = (int) Math.ceil((double) this.textLayout.getLineWidth(line));
        int count = this.textLayout.getLineCount();
        for (a = line + 1; a < count; a++) {
            int w = (int) Math.ceil((double) this.textLayout.getLineWidth(a));
            if (Math.abs(w - width) >= AndroidUtilities.dp(10.0f)) {
                break;
            }
            width = Math.max(w, width);
        }
        for (a = line - 1; a >= 0; a--) {
            w = (int) Math.ceil((double) this.textLayout.getLineWidth(a));
            if (Math.abs(w - width) >= AndroidUtilities.dp(10.0f)) {
                break;
            }
            width = Math.max(w, width);
        }
        return width;
    }

    private boolean isLineTop(int prevWidth, int currentWidth, int line, int count, int cornerRest) {
        if (line != 0) {
            if (line < 0 || line >= count || findMaxWidthAroundLine(line - 1) + (cornerRest * 3) >= prevWidth) {
                return false;
            }
        }
        return true;
    }

    private boolean isLineBottom(int nextWidth, int currentWidth, int line, int count, int cornerRest) {
        if (line != count - 1) {
            if (line < 0 || line > count - 1 || findMaxWidthAroundLine(line + 1) + (cornerRest * 3) >= nextWidth) {
                return false;
            }
        }
        return true;
    }
}
