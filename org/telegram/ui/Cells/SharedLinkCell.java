package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.beta.R;
import org.telegram.messenger.browser.Browser;
import org.telegram.tgnet.TLRPC.WebPage;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.CheckBox;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.LinkPath;

public class SharedLinkCell extends FrameLayout {
    private CheckBox checkBox;
    private SharedLinkCellDelegate delegate;
    private int description2Y = AndroidUtilities.dp(27.0f);
    private StaticLayout descriptionLayout;
    private StaticLayout descriptionLayout2;
    private TextPaint descriptionTextPaint;
    private int descriptionY = AndroidUtilities.dp(27.0f);
    private boolean drawLinkImageView;
    private LetterDrawable letterDrawable;
    private ImageReceiver linkImageView;
    private ArrayList<StaticLayout> linkLayout = new ArrayList();
    private boolean linkPreviewPressed;
    private int linkY;
    ArrayList<String> links = new ArrayList();
    private MessageObject message;
    private boolean needDivider;
    private int pressedLink;
    private StaticLayout titleLayout;
    private TextPaint titleTextPaint = new TextPaint(1);
    private int titleY = AndroidUtilities.dp(7.0f);
    private LinkPath urlPath = new LinkPath();

    public interface SharedLinkCellDelegate {
        boolean canPerformActions();

        void needOpenWebView(WebPage webPage);
    }

    @android.annotation.SuppressLint({"DrawAllocation"})
    protected void onMeasure(int r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.SharedLinkCell.onMeasure(int, int):void
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
        r1 = r38;
        r2 = 0;
        r1.drawLinkImageView = r2;
        r3 = 0;
        r1.descriptionLayout = r3;
        r1.titleLayout = r3;
        r1.descriptionLayout2 = r3;
        r4 = r1.descriptionY;
        r1.description2Y = r4;
        r4 = r1.linkLayout;
        r4.clear();
        r4 = r1.links;
        r4.clear();
        r4 = android.view.View.MeasureSpec.getSize(r39);
        r5 = org.telegram.messenger.AndroidUtilities.leftBaseline;
        r5 = (float) r5;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r5);
        r4 = r4 - r5;
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = r1.message;
        r10 = r10.messageOwner;
        r10 = r10.media;
        r10 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageMediaWebPage;
        r14 = 1;
        if (r10 == 0) goto L_0x0078;
    L_0x003d:
        r10 = r1.message;
        r10 = r10.messageOwner;
        r10 = r10.media;
        r10 = r10.webpage;
        r10 = r10 instanceof org.telegram.tgnet.TLRPC.TL_webPage;
        if (r10 == 0) goto L_0x0078;
    L_0x0049:
        r10 = r1.message;
        r10 = r10.messageOwner;
        r10 = r10.media;
        r10 = r10.webpage;
        r11 = r1.message;
        r11 = r11.photoThumbs;
        if (r11 != 0) goto L_0x0060;
    L_0x0057:
        r11 = r10.photo;
        if (r11 == 0) goto L_0x0060;
    L_0x005b:
        r11 = r1.message;
        r11.generateThumbs(r14);
    L_0x0060:
        r11 = r10.photo;
        if (r11 == 0) goto L_0x006c;
    L_0x0064:
        r11 = r1.message;
        r11 = r11.photoThumbs;
        if (r11 == 0) goto L_0x006c;
    L_0x006a:
        r11 = r14;
        goto L_0x006d;
    L_0x006c:
        r11 = r2;
    L_0x006d:
        r9 = r11;
        r5 = r10.title;
        if (r5 != 0) goto L_0x0074;
    L_0x0072:
        r5 = r10.site_name;
    L_0x0074:
        r6 = r10.description;
        r8 = r10.url;
    L_0x0078:
        r15 = r9;
        r37 = r6;
        r6 = r5;
        r5 = r8;
        r8 = r37;
        r9 = r1.message;
        if (r9 == 0) goto L_0x024e;
    L_0x0083:
        r9 = r1.message;
        r9 = r9.messageOwner;
        r9 = r9.entities;
        r9 = r9.isEmpty();
        if (r9 != 0) goto L_0x024e;
    L_0x008f:
        r9 = r7;
        r7 = r6;
        r6 = r2;
        r10 = r1.message;
        r10 = r10.messageOwner;
        r10 = r10.entities;
        r10 = r10.size();
        if (r6 >= r10) goto L_0x024a;
    L_0x009e:
        r10 = r1.message;
        r10 = r10.messageOwner;
        r10 = r10.entities;
        r10 = r10.get(r6);
        r10 = (org.telegram.tgnet.TLRPC.MessageEntity) r10;
        r11 = r10.length;
        if (r11 <= 0) goto L_0x0243;
    L_0x00ae:
        r11 = r10.offset;
        if (r11 < 0) goto L_0x0243;
    L_0x00b2:
        r11 = r10.offset;
        r12 = r1.message;
        r12 = r12.messageOwner;
        r12 = r12.message;
        r12 = r12.length();
        if (r11 < r12) goto L_0x00c2;
    L_0x00c0:
        goto L_0x0243;
    L_0x00c2:
        r11 = r10.offset;
        r12 = r10.length;
        r11 = r11 + r12;
        r12 = r1.message;
        r12 = r12.messageOwner;
        r12 = r12.message;
        r12 = r12.length();
        if (r11 <= r12) goto L_0x00e2;
    L_0x00d3:
        r11 = r1.message;
        r11 = r11.messageOwner;
        r11 = r11.message;
        r11 = r11.length();
        r12 = r10.offset;
        r11 = r11 - r12;
        r10.length = r11;
    L_0x00e2:
        if (r6 != 0) goto L_0x0113;
    L_0x00e4:
        if (r5 == 0) goto L_0x0113;
    L_0x00e6:
        r11 = r10.offset;
        if (r11 != 0) goto L_0x00f8;
    L_0x00ea:
        r11 = r10.length;
        r12 = r1.message;
        r12 = r12.messageOwner;
        r12 = r12.message;
        r12 = r12.length();
        if (r11 == r12) goto L_0x0113;
    L_0x00f8:
        r11 = r1.message;
        r11 = r11.messageOwner;
        r11 = r11.entities;
        r11 = r11.size();
        if (r11 != r14) goto L_0x010d;
    L_0x0104:
        if (r8 != 0) goto L_0x0113;
    L_0x0106:
        r11 = r1.message;
        r11 = r11.messageOwner;
        r9 = r11.message;
        goto L_0x0113;
    L_0x010d:
        r11 = r1.message;
        r11 = r11.messageOwner;
        r9 = r11.message;
    L_0x0113:
        r11 = 0;
        r12 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageEntityTextUrl;	 Catch:{ Exception -> 0x023e }
        if (r12 != 0) goto L_0x0179;	 Catch:{ Exception -> 0x023e }
    L_0x0118:
        r12 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageEntityUrl;	 Catch:{ Exception -> 0x023e }
        if (r12 == 0) goto L_0x011d;	 Catch:{ Exception -> 0x023e }
    L_0x011c:
        goto L_0x0179;	 Catch:{ Exception -> 0x023e }
    L_0x011d:
        r12 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageEntityEmail;	 Catch:{ Exception -> 0x023e }
        if (r12 == 0) goto L_0x0204;	 Catch:{ Exception -> 0x023e }
    L_0x0121:
        if (r7 == 0) goto L_0x0129;	 Catch:{ Exception -> 0x023e }
    L_0x0123:
        r12 = r7.length();	 Catch:{ Exception -> 0x023e }
        if (r12 != 0) goto L_0x0204;	 Catch:{ Exception -> 0x023e }
    L_0x0129:
        r12 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x023e }
        r12.<init>();	 Catch:{ Exception -> 0x023e }
        r13 = "mailto:";	 Catch:{ Exception -> 0x023e }
        r12.append(r13);	 Catch:{ Exception -> 0x023e }
        r13 = r1.message;	 Catch:{ Exception -> 0x023e }
        r13 = r13.messageOwner;	 Catch:{ Exception -> 0x023e }
        r13 = r13.message;	 Catch:{ Exception -> 0x023e }
        r3 = r10.offset;	 Catch:{ Exception -> 0x023e }
        r14 = r10.offset;	 Catch:{ Exception -> 0x023e }
        r2 = r10.length;	 Catch:{ Exception -> 0x023e }
        r14 = r14 + r2;	 Catch:{ Exception -> 0x023e }
        r2 = r13.substring(r3, r14);	 Catch:{ Exception -> 0x023e }
        r12.append(r2);	 Catch:{ Exception -> 0x023e }
        r2 = r12.toString();	 Catch:{ Exception -> 0x023e }
        r11 = r2;	 Catch:{ Exception -> 0x023e }
        r2 = r1.message;	 Catch:{ Exception -> 0x023e }
        r2 = r2.messageOwner;	 Catch:{ Exception -> 0x023e }
        r2 = r2.message;	 Catch:{ Exception -> 0x023e }
        r3 = r10.offset;	 Catch:{ Exception -> 0x023e }
        r12 = r10.offset;	 Catch:{ Exception -> 0x023e }
        r13 = r10.length;	 Catch:{ Exception -> 0x023e }
        r12 = r12 + r13;	 Catch:{ Exception -> 0x023e }
        r2 = r2.substring(r3, r12);	 Catch:{ Exception -> 0x023e }
        r3 = r10.offset;	 Catch:{ Exception -> 0x023b }
        if (r3 != 0) goto L_0x016f;	 Catch:{ Exception -> 0x023b }
    L_0x0161:
        r3 = r10.length;	 Catch:{ Exception -> 0x023b }
        r7 = r1.message;	 Catch:{ Exception -> 0x023b }
        r7 = r7.messageOwner;	 Catch:{ Exception -> 0x023b }
        r7 = r7.message;	 Catch:{ Exception -> 0x023b }
        r7 = r7.length();	 Catch:{ Exception -> 0x023b }
        if (r3 == r7) goto L_0x0203;	 Catch:{ Exception -> 0x023b }
    L_0x016f:
        r3 = r1.message;	 Catch:{ Exception -> 0x023b }
        r3 = r3.messageOwner;	 Catch:{ Exception -> 0x023b }
        r3 = r3.message;	 Catch:{ Exception -> 0x023b }
        r7 = r2;
        r8 = r3;
        goto L_0x0204;
    L_0x0179:
        r2 = r10 instanceof org.telegram.tgnet.TLRPC.TL_messageEntityUrl;	 Catch:{ Exception -> 0x023e }
        if (r2 == 0) goto L_0x018f;	 Catch:{ Exception -> 0x023e }
    L_0x017d:
        r2 = r1.message;	 Catch:{ Exception -> 0x023e }
        r2 = r2.messageOwner;	 Catch:{ Exception -> 0x023e }
        r2 = r2.message;	 Catch:{ Exception -> 0x023e }
        r3 = r10.offset;	 Catch:{ Exception -> 0x023e }
        r12 = r10.offset;	 Catch:{ Exception -> 0x023e }
        r13 = r10.length;	 Catch:{ Exception -> 0x023e }
        r12 = r12 + r13;	 Catch:{ Exception -> 0x023e }
        r2 = r2.substring(r3, r12);	 Catch:{ Exception -> 0x023e }
        goto L_0x0191;	 Catch:{ Exception -> 0x023e }
    L_0x018f:
        r2 = r10.url;	 Catch:{ Exception -> 0x023e }
    L_0x0191:
        r11 = r2;	 Catch:{ Exception -> 0x023e }
        if (r7 == 0) goto L_0x019a;	 Catch:{ Exception -> 0x023e }
    L_0x0194:
        r2 = r7.length();	 Catch:{ Exception -> 0x023e }
        if (r2 != 0) goto L_0x0204;
    L_0x019a:
        r2 = r11;
        r3 = android.net.Uri.parse(r2);	 Catch:{ Exception -> 0x023b }
        r7 = r3.getHost();	 Catch:{ Exception -> 0x023b }
        r2 = r7;	 Catch:{ Exception -> 0x023b }
        if (r2 != 0) goto L_0x01a7;	 Catch:{ Exception -> 0x023b }
    L_0x01a6:
        r2 = r11;	 Catch:{ Exception -> 0x023b }
    L_0x01a7:
        if (r2 == 0) goto L_0x01e7;	 Catch:{ Exception -> 0x023b }
    L_0x01a9:
        r7 = 46;	 Catch:{ Exception -> 0x023b }
        r12 = r2.lastIndexOf(r7);	 Catch:{ Exception -> 0x023b }
        r13 = r12;	 Catch:{ Exception -> 0x023b }
        if (r12 < 0) goto L_0x01e7;	 Catch:{ Exception -> 0x023b }
    L_0x01b2:
        r12 = 0;	 Catch:{ Exception -> 0x023b }
        r14 = r2.substring(r12, r13);	 Catch:{ Exception -> 0x023b }
        r2 = r14;	 Catch:{ Exception -> 0x023b }
        r7 = r2.lastIndexOf(r7);	 Catch:{ Exception -> 0x023b }
        r12 = r7;	 Catch:{ Exception -> 0x023b }
        if (r7 < 0) goto L_0x01c6;	 Catch:{ Exception -> 0x023b }
    L_0x01bf:
        r7 = r12 + 1;	 Catch:{ Exception -> 0x023b }
        r7 = r2.substring(r7);	 Catch:{ Exception -> 0x023b }
        r2 = r7;	 Catch:{ Exception -> 0x023b }
    L_0x01c6:
        r7 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x023b }
        r7.<init>();	 Catch:{ Exception -> 0x023b }
        r19 = r3;	 Catch:{ Exception -> 0x023b }
        r13 = 1;	 Catch:{ Exception -> 0x023b }
        r14 = 0;	 Catch:{ Exception -> 0x023b }
        r3 = r2.substring(r14, r13);	 Catch:{ Exception -> 0x023b }
        r3 = r3.toUpperCase();	 Catch:{ Exception -> 0x023b }
        r7.append(r3);	 Catch:{ Exception -> 0x023b }
        r3 = r2.substring(r13);	 Catch:{ Exception -> 0x023b }
        r7.append(r3);	 Catch:{ Exception -> 0x023b }
        r3 = r7.toString();	 Catch:{ Exception -> 0x023b }
        r2 = r3;	 Catch:{ Exception -> 0x023b }
        goto L_0x01e9;	 Catch:{ Exception -> 0x023b }
    L_0x01e7:
        r19 = r3;	 Catch:{ Exception -> 0x023b }
    L_0x01e9:
        r3 = r10.offset;	 Catch:{ Exception -> 0x023b }
        if (r3 != 0) goto L_0x01fb;	 Catch:{ Exception -> 0x023b }
    L_0x01ed:
        r3 = r10.length;	 Catch:{ Exception -> 0x023b }
        r7 = r1.message;	 Catch:{ Exception -> 0x023b }
        r7 = r7.messageOwner;	 Catch:{ Exception -> 0x023b }
        r7 = r7.message;	 Catch:{ Exception -> 0x023b }
        r7 = r7.length();	 Catch:{ Exception -> 0x023b }
        if (r3 == r7) goto L_0x0202;	 Catch:{ Exception -> 0x023b }
    L_0x01fb:
        r3 = r1.message;	 Catch:{ Exception -> 0x023b }
        r3 = r3.messageOwner;	 Catch:{ Exception -> 0x023b }
        r3 = r3.message;	 Catch:{ Exception -> 0x023b }
        r8 = r3;
    L_0x0203:
        r7 = r2;
    L_0x0204:
        if (r11 == 0) goto L_0x023a;
        r2 = r11.toLowerCase();	 Catch:{ Exception -> 0x023e }
        r3 = "http";	 Catch:{ Exception -> 0x023e }
        r2 = r2.indexOf(r3);	 Catch:{ Exception -> 0x023e }
        if (r2 == 0) goto L_0x0235;	 Catch:{ Exception -> 0x023e }
        r2 = r11.toLowerCase();	 Catch:{ Exception -> 0x023e }
        r3 = "mailto";	 Catch:{ Exception -> 0x023e }
        r2 = r2.indexOf(r3);	 Catch:{ Exception -> 0x023e }
        if (r2 == 0) goto L_0x0235;	 Catch:{ Exception -> 0x023e }
        r2 = r1.links;	 Catch:{ Exception -> 0x023e }
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x023e }
        r3.<init>();	 Catch:{ Exception -> 0x023e }
        r12 = "http://";	 Catch:{ Exception -> 0x023e }
        r3.append(r12);	 Catch:{ Exception -> 0x023e }
        r3.append(r11);	 Catch:{ Exception -> 0x023e }
        r3 = r3.toString();	 Catch:{ Exception -> 0x023e }
        r2.add(r3);	 Catch:{ Exception -> 0x023e }
        goto L_0x023a;	 Catch:{ Exception -> 0x023e }
        r2 = r1.links;	 Catch:{ Exception -> 0x023e }
        r2.add(r11);	 Catch:{ Exception -> 0x023e }
        goto L_0x0243;
    L_0x023b:
        r0 = move-exception;
        r7 = r2;
        goto L_0x023f;
    L_0x023e:
        r0 = move-exception;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);
    L_0x0243:
        r6 = r6 + 1;
        r2 = 0;
        r3 = 0;
        r14 = 1;
        goto L_0x0092;
    L_0x024a:
        r2 = r7;
        r3 = r8;
        r14 = r9;
        goto L_0x0251;
    L_0x024e:
        r2 = r6;
        r14 = r7;
        r3 = r8;
        if (r5 == 0) goto L_0x0260;
        r6 = r1.links;
        r6 = r6.isEmpty();
        if (r6 == 0) goto L_0x0260;
        r6 = r1.links;
        r6.add(r5);
        r13 = 32;
        r12 = 10;
        if (r2 == 0) goto L_0x02b9;
        r6 = r1.titleTextPaint;	 Catch:{ Exception -> 0x02ab }
        r6 = r6.measureText(r2);	 Catch:{ Exception -> 0x02ab }
        r6 = (double) r6;	 Catch:{ Exception -> 0x02ab }
        r6 = java.lang.Math.ceil(r6);	 Catch:{ Exception -> 0x02ab }
        r11 = (int) r6;	 Catch:{ Exception -> 0x02ab }
        r6 = r2.replace(r12, r13);	 Catch:{ Exception -> 0x02ab }
        r7 = r1.titleTextPaint;	 Catch:{ Exception -> 0x02ab }
        r8 = java.lang.Math.min(r11, r4);	 Catch:{ Exception -> 0x02ab }
        r8 = (float) r8;	 Catch:{ Exception -> 0x02ab }
        r9 = android.text.TextUtils.TruncateAt.END;	 Catch:{ Exception -> 0x02ab }
        r7 = android.text.TextUtils.ellipsize(r6, r7, r8, r9);	 Catch:{ Exception -> 0x02ab }
        r10 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x02ab }
        r8 = r1.titleTextPaint;	 Catch:{ Exception -> 0x02ab }
        r19 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x02ab }
        r20 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r21 = 0;
        r22 = 0;
        r6 = r10;
        r9 = r4;
        r23 = r10;
        r10 = r19;
        r19 = r11;
        r11 = r20;
        r12 = r21;
        r25 = r5;
        r5 = r13;
        r13 = r22;
        r6.<init>(r7, r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x02a8 }
        r6 = r23;	 Catch:{ Exception -> 0x02a8 }
        r1.titleLayout = r6;	 Catch:{ Exception -> 0x02a8 }
        goto L_0x02b3;
    L_0x02a8:
        r0 = move-exception;
        r6 = r0;
        goto L_0x02b0;
    L_0x02ab:
        r0 = move-exception;
        r25 = r5;
        r5 = r13;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r6 = r1.letterDrawable;
        r6.setTitle(r2);
        goto L_0x02bc;
        r25 = r5;
        r5 = r13;
        r13 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r3 == 0) goto L_0x02f3;
        r7 = r1.descriptionTextPaint;	 Catch:{ Exception -> 0x02ee }
        r10 = 0;	 Catch:{ Exception -> 0x02ee }
        r11 = 3;	 Catch:{ Exception -> 0x02ee }
        r6 = r3;	 Catch:{ Exception -> 0x02ee }
        r8 = r4;	 Catch:{ Exception -> 0x02ee }
        r9 = r4;	 Catch:{ Exception -> 0x02ee }
        r6 = org.telegram.ui.Cells.ChatMessageCell.generateStaticLayout(r6, r7, r8, r9, r10, r11);	 Catch:{ Exception -> 0x02ee }
        r1.descriptionLayout = r6;	 Catch:{ Exception -> 0x02ee }
        r6 = r1.descriptionLayout;	 Catch:{ Exception -> 0x02ee }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x02ee }
        if (r6 <= 0) goto L_0x02ed;	 Catch:{ Exception -> 0x02ee }
        r6 = r1.descriptionY;	 Catch:{ Exception -> 0x02ee }
        r7 = r1.descriptionLayout;	 Catch:{ Exception -> 0x02ee }
        r8 = r1.descriptionLayout;	 Catch:{ Exception -> 0x02ee }
        r8 = r8.getLineCount();	 Catch:{ Exception -> 0x02ee }
        r9 = 1;	 Catch:{ Exception -> 0x02ee }
        r8 = r8 - r9;	 Catch:{ Exception -> 0x02ee }
        r7 = r7.getLineBottom(r8);	 Catch:{ Exception -> 0x02ee }
        r6 = r6 + r7;	 Catch:{ Exception -> 0x02ee }
        r7 = org.telegram.messenger.AndroidUtilities.dp(r13);	 Catch:{ Exception -> 0x02ee }
        r6 = r6 + r7;	 Catch:{ Exception -> 0x02ee }
        r1.description2Y = r6;	 Catch:{ Exception -> 0x02ee }
        goto L_0x02f3;
    L_0x02ee:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r12 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        if (r14 == 0) goto L_0x0325;
        r7 = r1.descriptionTextPaint;	 Catch:{ Exception -> 0x0320 }
        r10 = 0;	 Catch:{ Exception -> 0x0320 }
        r11 = 3;	 Catch:{ Exception -> 0x0320 }
        r6 = r14;	 Catch:{ Exception -> 0x0320 }
        r8 = r4;	 Catch:{ Exception -> 0x0320 }
        r9 = r4;	 Catch:{ Exception -> 0x0320 }
        r6 = org.telegram.ui.Cells.ChatMessageCell.generateStaticLayout(r6, r7, r8, r9, r10, r11);	 Catch:{ Exception -> 0x0320 }
        r1.descriptionLayout2 = r6;	 Catch:{ Exception -> 0x0320 }
        r6 = r1.descriptionLayout2;	 Catch:{ Exception -> 0x0320 }
        r7 = r1.descriptionLayout2;	 Catch:{ Exception -> 0x0320 }
        r7 = r7.getLineCount();	 Catch:{ Exception -> 0x0320 }
        r8 = 1;	 Catch:{ Exception -> 0x0320 }
        r7 = r7 - r8;	 Catch:{ Exception -> 0x0320 }
        r6 = r6.getLineBottom(r7);	 Catch:{ Exception -> 0x0320 }
        r7 = r1.descriptionLayout;	 Catch:{ Exception -> 0x0320 }
        if (r7 == 0) goto L_0x031f;	 Catch:{ Exception -> 0x0320 }
        r7 = r1.description2Y;	 Catch:{ Exception -> 0x0320 }
        r8 = org.telegram.messenger.AndroidUtilities.dp(r12);	 Catch:{ Exception -> 0x0320 }
        r7 = r7 + r8;	 Catch:{ Exception -> 0x0320 }
        r1.description2Y = r7;	 Catch:{ Exception -> 0x0320 }
        goto L_0x0325;
    L_0x0320:
        r0 = move-exception;
        r6 = r0;
        org.telegram.messenger.FileLog.e(r6);
        r6 = r1.links;
        r6 = r6.isEmpty();
        if (r6 != 0) goto L_0x03d4;
        r6 = 0;
        r11 = r6;
        r6 = r1.links;
        r6 = r6.size();
        if (r11 >= r6) goto L_0x03d4;
        r6 = r1.links;	 Catch:{ Exception -> 0x03bd }
        r6 = r6.get(r11);	 Catch:{ Exception -> 0x03bd }
        r6 = (java.lang.String) r6;	 Catch:{ Exception -> 0x03bd }
        r10 = r6;	 Catch:{ Exception -> 0x03bd }
        r6 = r1.descriptionTextPaint;	 Catch:{ Exception -> 0x03bd }
        r6 = r6.measureText(r10);	 Catch:{ Exception -> 0x03bd }
        r6 = (double) r6;	 Catch:{ Exception -> 0x03bd }
        r6 = java.lang.Math.ceil(r6);	 Catch:{ Exception -> 0x03bd }
        r9 = (int) r6;
        r8 = 10;
        r6 = r10.replace(r8, r5);	 Catch:{ Exception -> 0x03b4 }
        r7 = r1.descriptionTextPaint;	 Catch:{ Exception -> 0x03b4 }
        r5 = java.lang.Math.min(r9, r4);	 Catch:{ Exception -> 0x03b4 }
        r5 = (float) r5;
        r8 = android.text.TextUtils.TruncateAt.MIDDLE;	 Catch:{ Exception -> 0x03bd }
        r7 = android.text.TextUtils.ellipsize(r6, r7, r5, r8);	 Catch:{ Exception -> 0x03bd }
        r5 = new android.text.StaticLayout;	 Catch:{ Exception -> 0x03bd }
        r8 = r1.descriptionTextPaint;	 Catch:{ Exception -> 0x03bd }
        r19 = android.text.Layout.Alignment.ALIGN_NORMAL;	 Catch:{ Exception -> 0x03bd }
        r20 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r21 = 0;
        r22 = 0;
        r6 = r5;
        r23 = 10;
        r24 = r9;
        r9 = r4;
        r26 = r10;
        r10 = r19;
        r19 = r11;
        r11 = r20;
        r12 = r21;
        r27 = r2;
        r2 = r13;
        r13 = r22;
        r6.<init>(r7, r8, r9, r10, r11, r12, r13);	 Catch:{ Exception -> 0x03b1 }
        r6 = r1.description2Y;	 Catch:{ Exception -> 0x03b1 }
        r1.linkY = r6;	 Catch:{ Exception -> 0x03b1 }
        r6 = r1.descriptionLayout2;	 Catch:{ Exception -> 0x03b1 }
        if (r6 == 0) goto L_0x03ab;	 Catch:{ Exception -> 0x03b1 }
        r6 = r1.descriptionLayout2;	 Catch:{ Exception -> 0x03b1 }
        r6 = r6.getLineCount();	 Catch:{ Exception -> 0x03b1 }
        if (r6 == 0) goto L_0x03ab;	 Catch:{ Exception -> 0x03b1 }
        r6 = r1.linkY;	 Catch:{ Exception -> 0x03b1 }
        r8 = r1.descriptionLayout2;	 Catch:{ Exception -> 0x03b1 }
        r9 = r1.descriptionLayout2;	 Catch:{ Exception -> 0x03b1 }
        r9 = r9.getLineCount();	 Catch:{ Exception -> 0x03b1 }
        r10 = 1;	 Catch:{ Exception -> 0x03b1 }
        r9 = r9 - r10;	 Catch:{ Exception -> 0x03b1 }
        r8 = r8.getLineBottom(r9);	 Catch:{ Exception -> 0x03b1 }
        r9 = org.telegram.messenger.AndroidUtilities.dp(r2);	 Catch:{ Exception -> 0x03b1 }
        r8 = r8 + r9;	 Catch:{ Exception -> 0x03b1 }
        r6 = r6 + r8;	 Catch:{ Exception -> 0x03b1 }
        r1.linkY = r6;	 Catch:{ Exception -> 0x03b1 }
        r6 = r1.linkLayout;	 Catch:{ Exception -> 0x03b1 }
        r6.add(r5);	 Catch:{ Exception -> 0x03b1 }
        goto L_0x03c9;
    L_0x03b1:
        r0 = move-exception;
        r5 = r0;
        goto L_0x03c6;
    L_0x03b4:
        r0 = move-exception;
        r27 = r2;
        r23 = r8;
        r19 = r11;
        r2 = r13;
        goto L_0x03c5;
    L_0x03bd:
        r0 = move-exception;
        r27 = r2;
        r19 = r11;
        r2 = r13;
        r23 = 10;
        r5 = r0;
        org.telegram.messenger.FileLog.e(r5);
        r6 = r19 + 1;
        r13 = r2;
        r2 = r27;
        r5 = 32;
        r12 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        goto L_0x032e;
        r27 = r2;
        r2 = 1112539136; // 0x42500000 float:52.0 double:5.496673668E-315;
        r2 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r5 = org.telegram.messenger.LocaleController.isRTL;
        if (r5 == 0) goto L_0x03ed;
        r5 = android.view.View.MeasureSpec.getSize(r39);
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r5 = r5 - r7;
        r5 = r5 - r2;
        goto L_0x03f3;
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r7 = r1.letterDrawable;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r9 = r5 + r2;
        r10 = 1115160576; // 0x42780000 float:62.0 double:5.5096253E-315;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r10);
        r7.setBounds(r5, r8, r9, r10);
        if (r15 == 0) goto L_0x0487;
        r7 = r1.message;
        r7 = r7.photoThumbs;
        r8 = 1;
        r7 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r7, r2, r8);
        r8 = r1.message;
        r8 = r8.photoThumbs;
        r9 = 80;
        r8 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r8, r9);
        if (r8 != r7) goto L_0x041c;
        r8 = 0;
        r9 = -1;
        r7.size = r9;
        if (r8 == 0) goto L_0x0423;
        r8.size = r9;
        r9 = r1.linkImageView;
        r10 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r9.setImageCoords(r5, r10, r2, r2);
        r9 = org.telegram.messenger.FileLoader.getAttachFileName(r7);
        r10 = java.util.Locale.US;
        r11 = "%d_%d";
        r12 = 2;
        r13 = new java.lang.Object[r12];
        r19 = java.lang.Integer.valueOf(r2);
        r18 = 0;
        r13[r18] = r19;
        r19 = java.lang.Integer.valueOf(r2);
        r17 = 1;
        r13[r17] = r19;
        r10 = java.lang.String.format(r10, r11, r13);
        r11 = r1.linkImageView;
        r13 = r7.location;
        if (r8 == 0) goto L_0x0456;
        r6 = r8.location;
        r31 = r6;
        goto L_0x0458;
        r31 = 0;
        r6 = java.util.Locale.US;
        r36 = r3;
        r3 = "%d_%d_b";
        r12 = new java.lang.Object[r12];
        r16 = java.lang.Integer.valueOf(r2);
        r18 = 0;
        r12[r18] = r16;
        r16 = java.lang.Integer.valueOf(r2);
        r17 = 1;
        r12[r17] = r16;
        r32 = java.lang.String.format(r6, r3, r12);
        r33 = 0;
        r34 = 0;
        r35 = 0;
        r28 = r11;
        r29 = r13;
        r30 = r10;
        r28.setImage(r29, r30, r31, r32, r33, r34, r35);
        r3 = 1;
        r1.drawLinkImageView = r3;
        goto L_0x048b;
        r36 = r3;
        r18 = 0;
        r3 = 0;
        r6 = r1.titleLayout;
        if (r6 == 0) goto L_0x04a7;
        r6 = r1.titleLayout;
        r6 = r6.getLineCount();
        if (r6 == 0) goto L_0x04a7;
        r6 = r1.titleLayout;
        r7 = r1.titleLayout;
        r7 = r7.getLineCount();
        r8 = 1;
        r7 = r7 - r8;
        r6 = r6.getLineBottom(r7);
        r3 = r3 + r6;
        r6 = r1.descriptionLayout;
        if (r6 == 0) goto L_0x04c2;
        r6 = r1.descriptionLayout;
        r6 = r6.getLineCount();
        if (r6 == 0) goto L_0x04c2;
        r6 = r1.descriptionLayout;
        r7 = r1.descriptionLayout;
        r7 = r7.getLineCount();
        r8 = 1;
        r7 = r7 - r8;
        r6 = r6.getLineBottom(r7);
        r3 = r3 + r6;
        r6 = r1.descriptionLayout2;
        if (r6 == 0) goto L_0x04e8;
        r6 = r1.descriptionLayout2;
        r6 = r6.getLineCount();
        if (r6 == 0) goto L_0x04e8;
        r6 = r1.descriptionLayout2;
        r7 = r1.descriptionLayout2;
        r7 = r7.getLineCount();
        r8 = 1;
        r7 = r7 - r8;
        r6 = r6.getLineBottom(r7);
        r3 = r3 + r6;
        r6 = r1.descriptionLayout;
        if (r6 == 0) goto L_0x04e8;
        r6 = 1092616192; // 0x41200000 float:10.0 double:5.398241246E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r3 = r3 + r6;
        r6 = r18;
        r7 = r1.linkLayout;
        r7 = r7.size();
        if (r6 >= r7) goto L_0x0511;
        r7 = r1.linkLayout;
        r7 = r7.get(r6);
        r7 = (android.text.StaticLayout) r7;
        r8 = r7.getLineCount();
        if (r8 <= 0) goto L_0x050d;
        r8 = r7.getLineCount();
        r9 = 1;
        r8 = r8 - r9;
        r8 = r7.getLineBottom(r8);
        r3 = r3 + r8;
        goto L_0x050e;
        r9 = 1;
        r18 = r6 + 1;
        goto L_0x04e9;
        if (r15 == 0) goto L_0x051d;
        r6 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r6);
        r3 = java.lang.Math.max(r6, r3);
        r6 = r1.checkBox;
        r7 = 1102053376; // 0x41b00000 float:22.0 double:5.44486713E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r9);
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r7 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r9);
        r6.measure(r8, r7);
        r6 = android.view.View.MeasureSpec.getSize(r39);
        r7 = 1116733440; // 0x42900000 float:72.0 double:5.517396283E-315;
        r7 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r8 = 1098907648; // 0x41800000 float:16.0 double:5.42932517E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = r8 + r3;
        r7 = java.lang.Math.max(r7, r8);
        r8 = r1.needDivider;
        r7 = r7 + r8;
        r1.setMeasuredDimension(r6, r7);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.SharedLinkCell.onMeasure(int, int):void");
    }

    public SharedLinkCell(Context context) {
        super(context);
        this.titleTextPaint.setTypeface(AndroidUtilities.getTypeface("fonts/rmedium.ttf"));
        this.titleTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
        this.descriptionTextPaint = new TextPaint(1);
        this.titleTextPaint.setTextSize((float) AndroidUtilities.dp(16.0f));
        this.descriptionTextPaint.setTextSize((float) AndroidUtilities.dp(16.0f));
        setWillNotDraw(false);
        this.linkImageView = new ImageReceiver(this);
        this.letterDrawable = new LetterDrawable();
        this.checkBox = new CheckBox(context, R.drawable.round_check2);
        this.checkBox.setVisibility(4);
        this.checkBox.setColor(Theme.getColor(Theme.key_checkbox), Theme.getColor(Theme.key_checkboxCheck));
        addView(this.checkBox, LayoutHelper.createFrame(22, 22.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 0.0f : 44.0f, 44.0f, LocaleController.isRTL ? 44.0f : 0.0f, 0.0f));
    }

    public void setLink(MessageObject messageObject, boolean divider) {
        this.needDivider = divider;
        resetPressedLink();
        this.message = messageObject;
        requestLayout();
    }

    public void setDelegate(SharedLinkCellDelegate sharedLinkCellDelegate) {
        this.delegate = sharedLinkCellDelegate;
    }

    public MessageObject getMessage() {
        return this.message;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onDetachedFromWindow();
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onAttachedToWindow();
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        boolean result = false;
        boolean z = true;
        if (this.message == null || r1.linkLayout.isEmpty() || r1.delegate == null || !r1.delegate.canPerformActions()) {
            resetPressedLink();
        } else {
            if (event.getAction() != 0) {
                if (!r1.linkPreviewPressed || event.getAction() != 1) {
                    if (event.getAction() == 3) {
                        resetPressedLink();
                    }
                }
            }
            int x = (int) event.getX();
            int y = (int) event.getY();
            boolean ok = false;
            int offset = 0;
            int a = 0;
            while (a < r1.linkLayout.size()) {
                StaticLayout layout = (StaticLayout) r1.linkLayout.get(a);
                if (layout.getLineCount() > 0) {
                    int height = layout.getLineBottom(layout.getLineCount() - z);
                    int linkPosX = AndroidUtilities.dp(LocaleController.isRTL ? 1090519040 : (float) AndroidUtilities.leftBaseline);
                    if (((float) x) < ((float) linkPosX) + layout.getLineLeft(0) || ((float) x) > ((float) linkPosX) + layout.getLineWidth(0) || y < r1.linkY + offset || y > (r1.linkY + offset) + height) {
                        offset += height;
                    } else {
                        ok = true;
                        if (event.getAction() == 0) {
                            resetPressedLink();
                            r1.pressedLink = a;
                            r1.linkPreviewPressed = z;
                            try {
                                r1.urlPath.setCurrentLayout(layout, 0, 0.0f);
                                layout.getSelectionPath(0, layout.getText().length(), r1.urlPath);
                            } catch (Throwable e) {
                                FileLog.e(e);
                            }
                            result = true;
                        } else if (r1.linkPreviewPressed) {
                            try {
                                WebPage webPage = (r1.pressedLink != 0 || r1.message.messageOwner.media == null) ? null : r1.message.messageOwner.media.webpage;
                                if (webPage == null || webPage.embed_url == null || webPage.embed_url.length() == 0) {
                                    Browser.openUrl(getContext(), (String) r1.links.get(r1.pressedLink));
                                } else {
                                    r1.delegate.needOpenWebView(webPage);
                                }
                            } catch (Throwable e2) {
                                FileLog.e(e2);
                            }
                            resetPressedLink();
                            result = true;
                        }
                        if (!ok) {
                            resetPressedLink();
                        }
                    }
                }
                a++;
                z = true;
            }
            if (ok) {
                resetPressedLink();
            }
        }
        if (!result) {
            if (!super.onTouchEvent(event)) {
                return false;
            }
        }
        return true;
    }

    public String getLink(int num) {
        if (num >= 0) {
            if (num < this.links.size()) {
                return (String) this.links.get(num);
            }
        }
        return null;
    }

    protected void resetPressedLink() {
        this.pressedLink = -1;
        this.linkPreviewPressed = false;
        invalidate();
    }

    public void setChecked(boolean checked, boolean animated) {
        if (this.checkBox.getVisibility() != 0) {
            this.checkBox.setVisibility(0);
        }
        this.checkBox.setChecked(checked, animated);
    }

    protected void onDraw(Canvas canvas) {
        if (this.titleLayout != null) {
            canvas.save();
            canvas.translate((float) AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : (float) AndroidUtilities.leftBaseline), (float) this.titleY);
            this.titleLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            canvas.save();
            canvas.translate((float) AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : (float) AndroidUtilities.leftBaseline), (float) this.descriptionY);
            this.descriptionLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout2 != null) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            canvas.save();
            canvas.translate((float) AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : (float) AndroidUtilities.leftBaseline), (float) this.description2Y);
            this.descriptionLayout2.draw(canvas);
            canvas.restore();
        }
        if (!this.linkLayout.isEmpty()) {
            this.descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText));
            int offset = 0;
            for (int a = 0; a < this.linkLayout.size(); a++) {
                StaticLayout layout = (StaticLayout) this.linkLayout.get(a);
                if (layout.getLineCount() > 0) {
                    canvas.save();
                    canvas.translate((float) AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : (float) AndroidUtilities.leftBaseline), (float) (this.linkY + offset));
                    if (this.pressedLink == a) {
                        canvas.drawPath(this.urlPath, Theme.linkSelectionPaint);
                    }
                    layout.draw(canvas);
                    canvas.restore();
                    offset += layout.getLineBottom(layout.getLineCount() - 1);
                }
            }
        }
        this.letterDrawable.draw(canvas);
        if (this.drawLinkImageView) {
            this.linkImageView.draw(canvas);
        }
        if (!this.needDivider) {
            return;
        }
        if (LocaleController.isRTL) {
            canvas.drawLine(0.0f, (float) (getMeasuredHeight() - 1), (float) (getMeasuredWidth() - AndroidUtilities.dp((float) AndroidUtilities.leftBaseline)), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
        } else {
            canvas.drawLine((float) AndroidUtilities.dp((float) AndroidUtilities.leftBaseline), (float) (getMeasuredHeight() - 1), (float) getMeasuredWidth(), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
        }
    }
}
