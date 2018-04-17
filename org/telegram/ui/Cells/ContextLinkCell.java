package org.telegram.ui.Cells;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.animation.AccelerateInterpolator;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.DownloadController;
import org.telegram.messenger.DownloadController.FileDownloadProgressListener;
import org.telegram.messenger.Emoji;
import org.telegram.messenger.FileLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.messenger.exoplayer2.util.MimeTypes;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.BotInlineResult;
import org.telegram.tgnet.TLRPC.Document;
import org.telegram.tgnet.TLRPC.DocumentAttribute;
import org.telegram.tgnet.TLRPC.MessageMedia;
import org.telegram.tgnet.TLRPC.Peer;
import org.telegram.tgnet.TLRPC.PhotoSize;
import org.telegram.tgnet.TLRPC.TL_botInlineMessageMediaGeo;
import org.telegram.tgnet.TLRPC.TL_botInlineMessageMediaVenue;
import org.telegram.tgnet.TLRPC.TL_document;
import org.telegram.tgnet.TLRPC.TL_documentAttributeAudio;
import org.telegram.tgnet.TLRPC.TL_documentAttributeFilename;
import org.telegram.tgnet.TLRPC.TL_documentAttributeImageSize;
import org.telegram.tgnet.TLRPC.TL_documentAttributeVideo;
import org.telegram.tgnet.TLRPC.TL_message;
import org.telegram.tgnet.TLRPC.TL_messageMediaDocument;
import org.telegram.tgnet.TLRPC.TL_peerUser;
import org.telegram.tgnet.TLRPC.TL_photoSizeEmpty;
import org.telegram.tgnet.TLRPC.TL_webDocument;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.LetterDrawable;
import org.telegram.ui.Components.RadialProgress;
import org.telegram.ui.PhotoViewer;

public class ContextLinkCell extends View implements FileDownloadProgressListener {
    private static final int DOCUMENT_ATTACH_TYPE_AUDIO = 3;
    private static final int DOCUMENT_ATTACH_TYPE_DOCUMENT = 1;
    private static final int DOCUMENT_ATTACH_TYPE_GEO = 8;
    private static final int DOCUMENT_ATTACH_TYPE_GIF = 2;
    private static final int DOCUMENT_ATTACH_TYPE_MUSIC = 5;
    private static final int DOCUMENT_ATTACH_TYPE_NONE = 0;
    private static final int DOCUMENT_ATTACH_TYPE_PHOTO = 7;
    private static final int DOCUMENT_ATTACH_TYPE_STICKER = 6;
    private static final int DOCUMENT_ATTACH_TYPE_VIDEO = 4;
    private static AccelerateInterpolator interpolator = new AccelerateInterpolator(0.5f);
    private int TAG = DownloadController.getInstance(this.currentAccount).generateObserverTag();
    private boolean buttonPressed;
    private int buttonState;
    private int currentAccount = UserConfig.selectedAccount;
    private MessageObject currentMessageObject;
    private PhotoSize currentPhotoObject;
    private ContextLinkCellDelegate delegate;
    private StaticLayout descriptionLayout;
    private int descriptionY = AndroidUtilities.dp(27.0f);
    private Document documentAttach;
    private int documentAttachType;
    private boolean drawLinkImageView;
    private BotInlineResult inlineResult;
    private long lastUpdateTime;
    private LetterDrawable letterDrawable = new LetterDrawable();
    private ImageReceiver linkImageView = new ImageReceiver(this);
    private StaticLayout linkLayout;
    private int linkY;
    private boolean mediaWebpage;
    private boolean needDivider;
    private boolean needShadow;
    private RadialProgress radialProgress = new RadialProgress(this);
    private float scale;
    private boolean scaled;
    private StaticLayout titleLayout;
    private int titleY = AndroidUtilities.dp(7.0f);

    public interface ContextLinkCellDelegate {
        void didPressedImage(ContextLinkCell contextLinkCell);
    }

    public boolean onTouchEvent(android.view.MotionEvent r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ContextLinkCell.onTouchEvent(android.view.MotionEvent):boolean
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
        r0 = r10.mediaWebpage;
        if (r0 != 0) goto L_0x00f1;
    L_0x0004:
        r0 = r10.delegate;
        if (r0 == 0) goto L_0x00f1;
    L_0x0008:
        r0 = r10.inlineResult;
        if (r0 != 0) goto L_0x000e;
    L_0x000c:
        goto L_0x00f1;
    L_0x000e:
        r0 = r11.getX();
        r0 = (int) r0;
        r1 = r11.getY();
        r1 = (int) r1;
        r2 = 0;
        r3 = 1111490560; // 0x42400000 float:48.0 double:5.491493014E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r10.documentAttachType;
        r5 = 2;
        r6 = 3;
        r7 = 1;
        r8 = 0;
        if (r4 == r6) goto L_0x008f;
    L_0x0027:
        r4 = r10.documentAttachType;
        r9 = 5;
        if (r4 != r9) goto L_0x002d;
    L_0x002c:
        goto L_0x008f;
    L_0x002d:
        r4 = r10.inlineResult;
        if (r4 == 0) goto L_0x00ea;
    L_0x0031:
        r4 = r10.inlineResult;
        r4 = r4.content;
        if (r4 == 0) goto L_0x00ea;
    L_0x0037:
        r4 = r10.inlineResult;
        r4 = r4.content;
        r4 = r4.url;
        r4 = android.text.TextUtils.isEmpty(r4);
        if (r4 != 0) goto L_0x00ea;
    L_0x0043:
        r4 = r11.getAction();
        if (r4 != 0) goto L_0x005a;
    L_0x0049:
        r4 = r10.letterDrawable;
        r4 = r4.getBounds();
        r4 = r4.contains(r0, r1);
        if (r4 == 0) goto L_0x00ea;
    L_0x0055:
        r10.buttonPressed = r7;
        r2 = 1;
        goto L_0x00ea;
    L_0x005a:
        r4 = r10.buttonPressed;
        if (r4 == 0) goto L_0x00ea;
    L_0x005e:
        r4 = r11.getAction();
        if (r4 != r7) goto L_0x0070;
    L_0x0064:
        r10.buttonPressed = r8;
        r10.playSoundEffect(r8);
        r4 = r10.delegate;
        r4.didPressedImage(r10);
        goto L_0x00ea;
    L_0x0070:
        r4 = r11.getAction();
        if (r4 != r6) goto L_0x007a;
    L_0x0076:
        r10.buttonPressed = r8;
        goto L_0x00ea;
    L_0x007a:
        r4 = r11.getAction();
        if (r4 != r5) goto L_0x00ea;
    L_0x0080:
        r4 = r10.letterDrawable;
        r4 = r4.getBounds();
        r4 = r4.contains(r0, r1);
        if (r4 != 0) goto L_0x00ea;
    L_0x008c:
        r10.buttonPressed = r8;
        goto L_0x00ea;
    L_0x008f:
        r4 = r10.letterDrawable;
        r4 = r4.getBounds();
        r4 = r4.contains(r0, r1);
        r9 = r11.getAction();
        if (r9 != 0) goto L_0x00b1;
    L_0x009f:
        if (r4 == 0) goto L_0x00e9;
    L_0x00a1:
        r10.buttonPressed = r7;
        r10.invalidate();
        r2 = 1;
        r5 = r10.radialProgress;
        r6 = r10.getDrawableForCurrentState();
        r5.swapBackground(r6);
        goto L_0x00e9;
    L_0x00b1:
        r9 = r10.buttonPressed;
        if (r9 == 0) goto L_0x00e9;
        r9 = r11.getAction();
        if (r9 != r7) goto L_0x00c7;
        r10.buttonPressed = r8;
        r10.playSoundEffect(r8);
        r10.didPressedButton();
        r10.invalidate();
        goto L_0x00e0;
        r7 = r11.getAction();
        if (r7 != r6) goto L_0x00d3;
        r10.buttonPressed = r8;
        r10.invalidate();
        goto L_0x00e0;
        r6 = r11.getAction();
        if (r6 != r5) goto L_0x00e0;
        if (r4 != 0) goto L_0x00e0;
        r10.buttonPressed = r8;
        r10.invalidate();
        r5 = r10.radialProgress;
        r6 = r10.getDrawableForCurrentState();
        r5.swapBackground(r6);
    L_0x00ea:
        if (r2 != 0) goto L_0x00f0;
        r2 = super.onTouchEvent(r11);
        return r2;
    L_0x00f1:
        r0 = super.onTouchEvent(r11);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ContextLinkCell.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public void updateButtonState(boolean r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ContextLinkCell.updateButtonState(boolean):void
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
        r0 = 0;
        r1 = 0;
        r2 = r9.documentAttachType;
        r3 = 3;
        r4 = 4;
        r5 = 5;
        r6 = 1;
        if (r2 == r5) goto L_0x00e4;
    L_0x000a:
        r2 = r9.documentAttachType;
        if (r2 != r3) goto L_0x0010;
    L_0x000e:
        goto L_0x00e4;
    L_0x0010:
        r2 = r9.mediaWebpage;
        if (r2 == 0) goto L_0x0138;
    L_0x0014:
        r2 = r9.inlineResult;
        if (r2 == 0) goto L_0x00d3;
    L_0x0018:
        r2 = r9.inlineResult;
        r2 = r2.document;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_document;
        if (r2 == 0) goto L_0x0032;
    L_0x0020:
        r2 = r9.inlineResult;
        r2 = r2.document;
        r0 = org.telegram.messenger.FileLoader.getAttachFileName(r2);
        r2 = r9.inlineResult;
        r2 = r2.document;
        r1 = org.telegram.messenger.FileLoader.getPathToAttach(r2);
        goto L_0x0138;
    L_0x0032:
        r2 = r9.inlineResult;
        r2 = r2.photo;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_photo;
        if (r2 == 0) goto L_0x0058;
    L_0x003a:
        r2 = r9.inlineResult;
        r2 = r2.photo;
        r2 = r2.sizes;
        r7 = org.telegram.messenger.AndroidUtilities.getPhotoSize();
        r2 = org.telegram.messenger.FileLoader.getClosestPhotoSizeWithSize(r2, r7, r6);
        r9.currentPhotoObject = r2;
        r2 = r9.currentPhotoObject;
        r0 = org.telegram.messenger.FileLoader.getAttachFileName(r2);
        r2 = r9.currentPhotoObject;
        r1 = org.telegram.messenger.FileLoader.getPathToAttach(r2);
        goto L_0x0138;
    L_0x0058:
        r2 = r9.inlineResult;
        r2 = r2.content;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_webDocument;
        if (r2 == 0) goto L_0x0096;
    L_0x0060:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r7 = r9.inlineResult;
        r7 = r7.content;
        r7 = r7.url;
        r7 = org.telegram.messenger.Utilities.MD5(r7);
        r2.append(r7);
        r7 = ".";
        r2.append(r7);
        r7 = r9.inlineResult;
        r7 = r7.content;
        r7 = r7.url;
        r8 = "jpg";
        r7 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r7, r8);
        r2.append(r7);
        r0 = r2.toString();
        r2 = new java.io.File;
        r7 = org.telegram.messenger.FileLoader.getDirectory(r4);
        r2.<init>(r7, r0);
        r1 = r2;
        goto L_0x0138;
    L_0x0096:
        r2 = r9.inlineResult;
        r2 = r2.thumb;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_webDocument;
        if (r2 == 0) goto L_0x0138;
    L_0x009e:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r7 = r9.inlineResult;
        r7 = r7.thumb;
        r7 = r7.url;
        r7 = org.telegram.messenger.Utilities.MD5(r7);
        r2.append(r7);
        r7 = ".";
        r2.append(r7);
        r7 = r9.inlineResult;
        r7 = r7.thumb;
        r7 = r7.url;
        r8 = "jpg";
        r7 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r7, r8);
        r2.append(r7);
        r0 = r2.toString();
        r2 = new java.io.File;
        r7 = org.telegram.messenger.FileLoader.getDirectory(r4);
        r2.<init>(r7, r0);
        r1 = r2;
        goto L_0x0138;
    L_0x00d3:
        r2 = r9.documentAttach;
        if (r2 == 0) goto L_0x0138;
    L_0x00d7:
        r2 = r9.documentAttach;
        r0 = org.telegram.messenger.FileLoader.getAttachFileName(r2);
        r2 = r9.documentAttach;
        r1 = org.telegram.messenger.FileLoader.getPathToAttach(r2);
        goto L_0x0138;
    L_0x00e4:
        r2 = r9.documentAttach;
        if (r2 == 0) goto L_0x00f5;
    L_0x00e8:
        r2 = r9.documentAttach;
        r0 = org.telegram.messenger.FileLoader.getAttachFileName(r2);
        r2 = r9.documentAttach;
        r1 = org.telegram.messenger.FileLoader.getPathToAttach(r2);
        goto L_0x0138;
    L_0x00f5:
        r2 = r9.inlineResult;
        r2 = r2.content;
        r2 = r2 instanceof org.telegram.tgnet.TLRPC.TL_webDocument;
        if (r2 == 0) goto L_0x0138;
    L_0x00fd:
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r7 = r9.inlineResult;
        r7 = r7.content;
        r7 = r7.url;
        r7 = org.telegram.messenger.Utilities.MD5(r7);
        r2.append(r7);
        r7 = ".";
        r2.append(r7);
        r7 = r9.inlineResult;
        r7 = r7.content;
        r7 = r7.url;
        r8 = r9.documentAttachType;
        if (r8 != r5) goto L_0x0121;
    L_0x011e:
        r8 = "mp3";
        goto L_0x0123;
    L_0x0121:
        r8 = "ogg";
    L_0x0123:
        r7 = org.telegram.messenger.ImageLoader.getHttpUrlExtension(r7, r8);
        r2.append(r7);
        r0 = r2.toString();
        r2 = new java.io.File;
        r7 = org.telegram.messenger.FileLoader.getDirectory(r4);
        r2.<init>(r7, r0);
        r1 = r2;
    L_0x0138:
        r2 = android.text.TextUtils.isEmpty(r0);
        r7 = 0;
        if (r2 == 0) goto L_0x0146;
    L_0x013f:
        r2 = r9.radialProgress;
        r3 = 0;
        r2.setBackground(r3, r7, r7);
        return;
    L_0x0146:
        r2 = r1.exists();
        if (r2 != 0) goto L_0x01d4;
    L_0x014c:
        r2 = r9.currentAccount;
        r2 = org.telegram.messenger.DownloadController.getInstance(r2);
        r2.addLoadingFileObserver(r0, r9);
        r2 = r9.documentAttachType;
        r8 = 0;
        if (r2 == r5) goto L_0x0180;
    L_0x015a:
        r2 = r9.documentAttachType;
        if (r2 != r3) goto L_0x015f;
    L_0x015e:
        goto L_0x0180;
    L_0x015f:
        r9.buttonState = r6;
        r2 = org.telegram.messenger.ImageLoader.getInstance();
        r2 = r2.getFileProgress(r0);
        if (r2 == 0) goto L_0x0170;
    L_0x016b:
        r8 = r2.floatValue();
    L_0x0170:
        r3 = r8;
        r4 = r9.radialProgress;
        r4.setProgress(r3, r7);
        r4 = r9.radialProgress;
        r5 = r9.getDrawableForCurrentState();
        r4.setBackground(r5, r6, r10);
        goto L_0x01d0;
    L_0x0180:
        r2 = r9.documentAttach;
        if (r2 == 0) goto L_0x018f;
    L_0x0184:
        r2 = r9.currentAccount;
        r2 = org.telegram.messenger.FileLoader.getInstance(r2);
        r2 = r2.isLoadingFile(r0);
        goto L_0x0197;
    L_0x018f:
        r2 = org.telegram.messenger.ImageLoader.getInstance();
        r2 = r2.isLoadingHttpFile(r0);
    L_0x0197:
        if (r2 != 0) goto L_0x01ab;
    L_0x0199:
        r3 = 2;
        r9.buttonState = r3;
        r3 = r9.radialProgress;
        r3.setProgress(r8, r10);
        r3 = r9.radialProgress;
        r4 = r9.getDrawableForCurrentState();
        r3.setBackground(r4, r7, r10);
        goto L_0x01cf;
    L_0x01ab:
        r9.buttonState = r4;
        r3 = org.telegram.messenger.ImageLoader.getInstance();
        r3 = r3.getFileProgress(r0);
        if (r3 == 0) goto L_0x01c1;
        r4 = r9.radialProgress;
        r5 = r3.floatValue();
        r4.setProgress(r5, r10);
        goto L_0x01c6;
        r4 = r9.radialProgress;
        r4.setProgress(r8, r10);
        r4 = r9.radialProgress;
        r5 = r9.getDrawableForCurrentState();
        r4.setBackground(r5, r6, r10);
    L_0x01d0:
        r9.invalidate();
        goto L_0x0215;
    L_0x01d4:
        r2 = r9.currentAccount;
        r2 = org.telegram.messenger.DownloadController.getInstance(r2);
        r2.removeLoadingFileObserver(r9);
        r2 = r9.documentAttachType;
        if (r2 == r5) goto L_0x01ea;
        r2 = r9.documentAttachType;
        if (r2 != r3) goto L_0x01e6;
        goto L_0x01ea;
        r2 = -1;
        r9.buttonState = r2;
        goto L_0x0209;
        r2 = org.telegram.messenger.MediaController.getInstance();
        r3 = r9.currentMessageObject;
        r2 = r2.isPlayingMessage(r3);
        if (r2 == 0) goto L_0x0206;
        if (r2 == 0) goto L_0x0203;
        r3 = org.telegram.messenger.MediaController.getInstance();
        r3 = r3.isMessagePaused();
        if (r3 == 0) goto L_0x0203;
        goto L_0x0206;
        r9.buttonState = r6;
        goto L_0x0208;
        r9.buttonState = r7;
        r2 = r9.radialProgress;
        r3 = r9.getDrawableForCurrentState();
        r2.setBackground(r3, r7, r10);
        r9.invalidate();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ContextLinkCell.updateButtonState(boolean):void");
    }

    public ContextLinkCell(Context context) {
        super(context);
    }

    @SuppressLint({"DrawAllocation"})
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float f;
        Throwable e;
        char c;
        char c2;
        int width;
        CharSequence linkFinal;
        StaticLayout staticLayout;
        Throwable e2;
        int w;
        int h;
        DocumentAttribute attribute;
        int dp;
        float f2;
        int x;
        this.drawLinkImageView = false;
        this.descriptionLayout = null;
        this.titleLayout = null;
        this.linkLayout = null;
        this.currentPhotoObject = null;
        this.linkY = AndroidUtilities.dp(27.0f);
        if (this.inlineResult == null && r1.documentAttach == null) {
            setMeasuredDimension(AndroidUtilities.dp(100.0f), AndroidUtilities.dp(100.0f));
            return;
        }
        boolean z;
        ArrayList<PhotoSize> photoThumbs;
        String ext;
        int viewWidth;
        int[] result;
        String currentPhotoFilterThumb;
        String currentPhotoFilter;
        String format;
        StringBuilder stringBuilder;
        int x2;
        int y;
        double lat;
        Object[] objArr;
        double lon;
        int viewWidth2 = MeasureSpec.getSize(widthMeasureSpec);
        int maxWidth = (viewWidth2 - AndroidUtilities.dp((float) AndroidUtilities.leftBaseline)) - AndroidUtilities.dp(8.0f);
        PhotoSize currentPhotoObjectThumb = null;
        ArrayList<PhotoSize> photoThumbs2 = null;
        TLObject tLObject = null;
        String urlLocation = null;
        if (r1.documentAttach != null) {
            photoThumbs2 = new ArrayList();
            photoThumbs2.add(r1.documentAttach.thumb);
        } else if (!(r1.inlineResult == null || r1.inlineResult.photo == null)) {
            photoThumbs2 = new ArrayList(r1.inlineResult.photo.sizes);
        }
        ArrayList<PhotoSize> photoThumbs3 = photoThumbs2;
        if (r1.mediaWebpage || r1.inlineResult == null) {
            z = true;
            f = 4.0f;
        } else {
            if (r1.inlineResult.title != null) {
                try {
                    r1.titleLayout = new StaticLayout(TextUtils.ellipsize(Emoji.replaceEmoji(r1.inlineResult.title.replace('\n', ' '), Theme.chat_contextResult_titleTextPaint.getFontMetricsInt(), AndroidUtilities.dp(15.0f), false), Theme.chat_contextResult_titleTextPaint, (float) Math.min((int) Math.ceil((double) Theme.chat_contextResult_titleTextPaint.measureText(r1.inlineResult.title)), maxWidth), TruncateAt.END), Theme.chat_contextResult_titleTextPaint, maxWidth + AndroidUtilities.dp(4.0f), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                } catch (Throwable e3) {
                    FileLog.e(e3);
                }
                r1.letterDrawable.setTitle(r1.inlineResult.title);
            }
            if (r1.inlineResult.description != null) {
                try {
                    c = '\n';
                    c2 = ' ';
                    z = true;
                    f = 4.0f;
                    try {
                        r1.descriptionLayout = ChatMessageCell.generateStaticLayout(Emoji.replaceEmoji(r1.inlineResult.description, Theme.chat_contextResult_descriptionTextPaint.getFontMetricsInt(), AndroidUtilities.dp(13.0f), false), Theme.chat_contextResult_descriptionTextPaint, maxWidth, maxWidth, 0, 3);
                        if (r1.descriptionLayout.getLineCount() > 0) {
                            r1.linkY = (r1.descriptionY + r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - z)) + AndroidUtilities.dp(1.0f);
                        }
                    } catch (Exception e4) {
                        e3 = e4;
                        FileLog.e(e3);
                        if (r1.inlineResult.url != null) {
                            try {
                                width = (int) Math.ceil((double) Theme.chat_contextResult_descriptionTextPaint.measureText(r1.inlineResult.url));
                                linkFinal = TextUtils.ellipsize(r1.inlineResult.url.replace(c, c2), Theme.chat_contextResult_descriptionTextPaint, (float) Math.min(width, maxWidth), TruncateAt.MIDDLE);
                                staticLayout = staticLayout;
                                photoThumbs = photoThumbs3;
                                try {
                                    r1.linkLayout = new StaticLayout(linkFinal, Theme.chat_contextResult_descriptionTextPaint, maxWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                                } catch (Throwable e32) {
                                    e2 = e32;
                                    FileLog.e(e2);
                                    ext = null;
                                    if (r1.documentAttach != null) {
                                        r1.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, AndroidUtilities.getPhotoSize(), z);
                                        currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, 80);
                                        if (currentPhotoObjectThumb == r1.currentPhotoObject) {
                                            currentPhotoObjectThumb = null;
                                        }
                                    } else if (!MessageObject.isGifDocument(r1.documentAttach)) {
                                        r1.currentPhotoObject = r1.documentAttach.thumb;
                                    } else if (MessageObject.isStickerDocument(r1.documentAttach)) {
                                        r1.currentPhotoObject = r1.documentAttach.thumb;
                                    } else {
                                        r1.currentPhotoObject = r1.documentAttach.thumb;
                                        ext = "webp";
                                    }
                                    if (r1.inlineResult != null) {
                                        if (r1.inlineResult.type.startsWith("gif")) {
                                            if (r1.inlineResult.type.equals("photo")) {
                                                tLObject = r1.inlineResult.thumb instanceof TL_webDocument ? (TL_webDocument) r1.inlineResult.thumb : (TL_webDocument) r1.inlineResult.content;
                                            }
                                        } else if (r1.documentAttachType != 2) {
                                            tLObject = (TL_webDocument) r1.inlineResult.content;
                                            r1.documentAttachType = 2;
                                        }
                                        tLObject = r1.inlineResult.thumb;
                                        if (!(r1.inlineResult.send_message instanceof TL_botInlineMessageMediaVenue)) {
                                            if (!(r1.inlineResult.send_message instanceof TL_botInlineMessageMediaGeo)) {
                                                viewWidth = viewWidth2;
                                                w = 0;
                                                h = 0;
                                                if (r1.documentAttach != null) {
                                                    viewWidth2 = 0;
                                                    while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                                        attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                                        if (attribute instanceof TL_documentAttributeImageSize) {
                                                            if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                                viewWidth2++;
                                                            }
                                                        }
                                                        w = attribute.w;
                                                        h = attribute.h;
                                                    }
                                                }
                                                if (r1.currentPhotoObject == null) {
                                                    if (currentPhotoObjectThumb != null) {
                                                        currentPhotoObjectThumb.size = -1;
                                                    }
                                                    w = r1.currentPhotoObject.w;
                                                    h = r1.currentPhotoObject.h;
                                                } else if (r1.inlineResult != null) {
                                                    result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                                    w = result[0];
                                                    h = result[1];
                                                }
                                                dp = AndroidUtilities.dp(80.0f);
                                                h = dp;
                                                w = dp;
                                                currentPhotoFilterThumb = "52_52_b";
                                                if (r1.mediaWebpage) {
                                                    currentPhotoFilter = "52_52";
                                                } else {
                                                    viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                                    if (r1.documentAttachType != 2) {
                                                        format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                                        currentPhotoFilterThumb = format;
                                                        currentPhotoFilter = format;
                                                    } else {
                                                        format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                                        stringBuilder = new StringBuilder();
                                                        stringBuilder.append(format);
                                                        stringBuilder.append("_b");
                                                        currentPhotoFilterThumb = stringBuilder.toString();
                                                        currentPhotoFilter = format;
                                                    }
                                                }
                                                r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                                if (r1.documentAttachType != 2) {
                                                    if (r1.documentAttach == null) {
                                                        r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                                    } else {
                                                        r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                                    }
                                                } else if (r1.currentPhotoObject == null) {
                                                    r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                                } else {
                                                    r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                                }
                                                r1.drawLinkImageView = true;
                                                if (r1.mediaWebpage) {
                                                    viewWidth2 = 0;
                                                    viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                                    viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                                    viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                                    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                                    dp = AndroidUtilities.dp(52.0f);
                                                    if (LocaleController.isRTL) {
                                                        f2 = 8.0f;
                                                        x = AndroidUtilities.dp(8.0f);
                                                    } else {
                                                        f2 = 8.0f;
                                                        x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                                    }
                                                    r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                                    r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                                    r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                                } else {
                                                    viewWidth2 = viewWidth;
                                                    dp = MeasureSpec.getSize(heightMeasureSpec);
                                                    if (dp == 0) {
                                                        dp = AndroidUtilities.dp(100.0f);
                                                    }
                                                    setMeasuredDimension(viewWidth2, dp);
                                                    x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                                    y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                                    r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                                    r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                                }
                                            }
                                        }
                                        lat = r1.inlineResult.send_message.geo.lat;
                                        viewWidth = viewWidth2;
                                        objArr = new Object[5];
                                        lon = r1.inlineResult.send_message.geo._long;
                                        objArr[2] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
                                        objArr[3] = Double.valueOf(lat);
                                        objArr[4] = Double.valueOf(lon);
                                        urlLocation = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:small|%f,%f&sensor=false", objArr);
                                        w = 0;
                                        h = 0;
                                        if (r1.documentAttach != null) {
                                            viewWidth2 = 0;
                                            while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                                attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                                if (attribute instanceof TL_documentAttributeImageSize) {
                                                    if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                        viewWidth2++;
                                                    }
                                                }
                                                w = attribute.w;
                                                h = attribute.h;
                                            }
                                        }
                                        if (r1.currentPhotoObject == null) {
                                            if (currentPhotoObjectThumb != null) {
                                                currentPhotoObjectThumb.size = -1;
                                            }
                                            w = r1.currentPhotoObject.w;
                                            h = r1.currentPhotoObject.h;
                                        } else if (r1.inlineResult != null) {
                                            result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                            w = result[0];
                                            h = result[1];
                                        }
                                        dp = AndroidUtilities.dp(80.0f);
                                        h = dp;
                                        w = dp;
                                        currentPhotoFilterThumb = "52_52_b";
                                        if (r1.mediaWebpage) {
                                            currentPhotoFilter = "52_52";
                                        } else {
                                            viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                            if (r1.documentAttachType != 2) {
                                                format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                                stringBuilder = new StringBuilder();
                                                stringBuilder.append(format);
                                                stringBuilder.append("_b");
                                                currentPhotoFilterThumb = stringBuilder.toString();
                                                currentPhotoFilter = format;
                                            } else {
                                                format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                                currentPhotoFilterThumb = format;
                                                currentPhotoFilter = format;
                                            }
                                        }
                                        if (r1.documentAttachType != 6) {
                                        }
                                        r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                        if (r1.documentAttachType != 2) {
                                            if (r1.currentPhotoObject == null) {
                                                if (currentPhotoObjectThumb == null) {
                                                }
                                                r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                            } else {
                                                if (currentPhotoObjectThumb == null) {
                                                }
                                                r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                            }
                                        } else if (r1.documentAttach == null) {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                        } else {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                        }
                                        r1.drawLinkImageView = true;
                                        if (r1.mediaWebpage) {
                                            viewWidth2 = 0;
                                            viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                            viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                            viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                            dp = AndroidUtilities.dp(52.0f);
                                            if (LocaleController.isRTL) {
                                                f2 = 8.0f;
                                                x = AndroidUtilities.dp(8.0f);
                                            } else {
                                                f2 = 8.0f;
                                                x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                            }
                                            r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                            r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                            r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                        } else {
                                            viewWidth2 = viewWidth;
                                            dp = MeasureSpec.getSize(heightMeasureSpec);
                                            if (dp == 0) {
                                                dp = AndroidUtilities.dp(100.0f);
                                            }
                                            setMeasuredDimension(viewWidth2, dp);
                                            x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                            y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                            r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                            r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                        }
                                    }
                                    viewWidth = viewWidth2;
                                    w = 0;
                                    h = 0;
                                    if (r1.documentAttach != null) {
                                        viewWidth2 = 0;
                                        while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                            attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                            if (attribute instanceof TL_documentAttributeImageSize) {
                                                if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                    viewWidth2++;
                                                }
                                            }
                                            w = attribute.w;
                                            h = attribute.h;
                                        }
                                    }
                                    if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb != null) {
                                            currentPhotoObjectThumb.size = -1;
                                        }
                                        w = r1.currentPhotoObject.w;
                                        h = r1.currentPhotoObject.h;
                                    } else if (r1.inlineResult != null) {
                                        result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                        w = result[0];
                                        h = result[1];
                                    }
                                    dp = AndroidUtilities.dp(80.0f);
                                    h = dp;
                                    w = dp;
                                    currentPhotoFilterThumb = "52_52_b";
                                    if (r1.mediaWebpage) {
                                        viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                        if (r1.documentAttachType != 2) {
                                            format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            currentPhotoFilterThumb = format;
                                            currentPhotoFilter = format;
                                        } else {
                                            format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(format);
                                            stringBuilder.append("_b");
                                            currentPhotoFilterThumb = stringBuilder.toString();
                                            currentPhotoFilter = format;
                                        }
                                    } else {
                                        currentPhotoFilter = "52_52";
                                    }
                                    if (r1.documentAttachType != 6) {
                                    }
                                    r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                    if (r1.documentAttachType != 2) {
                                        if (r1.documentAttach == null) {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                        } else {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                        }
                                    } else if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                    } else {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                    }
                                    r1.drawLinkImageView = true;
                                    if (r1.mediaWebpage) {
                                        viewWidth2 = viewWidth;
                                        dp = MeasureSpec.getSize(heightMeasureSpec);
                                        if (dp == 0) {
                                            dp = AndroidUtilities.dp(100.0f);
                                        }
                                        setMeasuredDimension(viewWidth2, dp);
                                        x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                        y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                        r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                        r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                    } else {
                                        viewWidth2 = 0;
                                        viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                        viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                        viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                        dp = AndroidUtilities.dp(52.0f);
                                        if (LocaleController.isRTL) {
                                            f2 = 8.0f;
                                            x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                        } else {
                                            f2 = 8.0f;
                                            x = AndroidUtilities.dp(8.0f);
                                        }
                                        r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                        r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                        r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                    }
                                }
                            } catch (Throwable e322) {
                                photoThumbs = photoThumbs3;
                                e2 = e322;
                                FileLog.e(e2);
                                ext = null;
                                if (r1.documentAttach != null) {
                                    r1.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, AndroidUtilities.getPhotoSize(), z);
                                    currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, 80);
                                    if (currentPhotoObjectThumb == r1.currentPhotoObject) {
                                        currentPhotoObjectThumb = null;
                                    }
                                } else if (!MessageObject.isGifDocument(r1.documentAttach)) {
                                    r1.currentPhotoObject = r1.documentAttach.thumb;
                                } else if (MessageObject.isStickerDocument(r1.documentAttach)) {
                                    r1.currentPhotoObject = r1.documentAttach.thumb;
                                } else {
                                    r1.currentPhotoObject = r1.documentAttach.thumb;
                                    ext = "webp";
                                }
                                if (r1.inlineResult != null) {
                                    if (r1.inlineResult.type.startsWith("gif")) {
                                        if (r1.inlineResult.type.equals("photo")) {
                                            if (r1.inlineResult.thumb instanceof TL_webDocument) {
                                            }
                                        }
                                    } else if (r1.documentAttachType != 2) {
                                        tLObject = (TL_webDocument) r1.inlineResult.content;
                                        r1.documentAttachType = 2;
                                    }
                                    tLObject = r1.inlineResult.thumb;
                                    if (r1.inlineResult.send_message instanceof TL_botInlineMessageMediaVenue) {
                                        if (!(r1.inlineResult.send_message instanceof TL_botInlineMessageMediaGeo)) {
                                            viewWidth = viewWidth2;
                                            w = 0;
                                            h = 0;
                                            if (r1.documentAttach != null) {
                                                viewWidth2 = 0;
                                                while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                                    attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                                    if (attribute instanceof TL_documentAttributeImageSize) {
                                                        if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                            viewWidth2++;
                                                        }
                                                    }
                                                    w = attribute.w;
                                                    h = attribute.h;
                                                }
                                            }
                                            if (r1.currentPhotoObject == null) {
                                                if (currentPhotoObjectThumb != null) {
                                                    currentPhotoObjectThumb.size = -1;
                                                }
                                                w = r1.currentPhotoObject.w;
                                                h = r1.currentPhotoObject.h;
                                            } else if (r1.inlineResult != null) {
                                                result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                                w = result[0];
                                                h = result[1];
                                            }
                                            dp = AndroidUtilities.dp(80.0f);
                                            h = dp;
                                            w = dp;
                                            currentPhotoFilterThumb = "52_52_b";
                                            if (r1.mediaWebpage) {
                                                currentPhotoFilter = "52_52";
                                            } else {
                                                viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                                if (r1.documentAttachType != 2) {
                                                    format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                                    stringBuilder = new StringBuilder();
                                                    stringBuilder.append(format);
                                                    stringBuilder.append("_b");
                                                    currentPhotoFilterThumb = stringBuilder.toString();
                                                    currentPhotoFilter = format;
                                                } else {
                                                    format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                                    currentPhotoFilterThumb = format;
                                                    currentPhotoFilter = format;
                                                }
                                            }
                                            if (r1.documentAttachType != 6) {
                                            }
                                            r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                            if (r1.documentAttachType != 2) {
                                                if (r1.currentPhotoObject == null) {
                                                    if (currentPhotoObjectThumb == null) {
                                                    }
                                                    r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                                } else {
                                                    if (currentPhotoObjectThumb == null) {
                                                    }
                                                    r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                                }
                                            } else if (r1.documentAttach == null) {
                                                if (r1.currentPhotoObject == null) {
                                                }
                                                r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                            } else {
                                                if (r1.currentPhotoObject == null) {
                                                }
                                                r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                            }
                                            r1.drawLinkImageView = true;
                                            if (r1.mediaWebpage) {
                                                viewWidth2 = 0;
                                                viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                                viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                                viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                                dp = AndroidUtilities.dp(52.0f);
                                                if (LocaleController.isRTL) {
                                                    f2 = 8.0f;
                                                    x = AndroidUtilities.dp(8.0f);
                                                } else {
                                                    f2 = 8.0f;
                                                    x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                                }
                                                r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                                r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                                r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                            } else {
                                                viewWidth2 = viewWidth;
                                                dp = MeasureSpec.getSize(heightMeasureSpec);
                                                if (dp == 0) {
                                                    dp = AndroidUtilities.dp(100.0f);
                                                }
                                                setMeasuredDimension(viewWidth2, dp);
                                                x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                                y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                                r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                                r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                            }
                                        }
                                    }
                                    lat = r1.inlineResult.send_message.geo.lat;
                                    viewWidth = viewWidth2;
                                    objArr = new Object[5];
                                    lon = r1.inlineResult.send_message.geo._long;
                                    objArr[2] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
                                    objArr[3] = Double.valueOf(lat);
                                    objArr[4] = Double.valueOf(lon);
                                    urlLocation = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:small|%f,%f&sensor=false", objArr);
                                    w = 0;
                                    h = 0;
                                    if (r1.documentAttach != null) {
                                        viewWidth2 = 0;
                                        while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                            attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                            if (attribute instanceof TL_documentAttributeImageSize) {
                                                if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                    viewWidth2++;
                                                }
                                            }
                                            w = attribute.w;
                                            h = attribute.h;
                                        }
                                    }
                                    if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb != null) {
                                            currentPhotoObjectThumb.size = -1;
                                        }
                                        w = r1.currentPhotoObject.w;
                                        h = r1.currentPhotoObject.h;
                                    } else if (r1.inlineResult != null) {
                                        result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                        w = result[0];
                                        h = result[1];
                                    }
                                    dp = AndroidUtilities.dp(80.0f);
                                    h = dp;
                                    w = dp;
                                    currentPhotoFilterThumb = "52_52_b";
                                    if (r1.mediaWebpage) {
                                        viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                        if (r1.documentAttachType != 2) {
                                            format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            currentPhotoFilterThumb = format;
                                            currentPhotoFilter = format;
                                        } else {
                                            format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(format);
                                            stringBuilder.append("_b");
                                            currentPhotoFilterThumb = stringBuilder.toString();
                                            currentPhotoFilter = format;
                                        }
                                    } else {
                                        currentPhotoFilter = "52_52";
                                    }
                                    if (r1.documentAttachType != 6) {
                                    }
                                    r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                    if (r1.documentAttachType != 2) {
                                        if (r1.documentAttach == null) {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                        } else {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                        }
                                    } else if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                    } else {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                    }
                                    r1.drawLinkImageView = true;
                                    if (r1.mediaWebpage) {
                                        viewWidth2 = viewWidth;
                                        dp = MeasureSpec.getSize(heightMeasureSpec);
                                        if (dp == 0) {
                                            dp = AndroidUtilities.dp(100.0f);
                                        }
                                        setMeasuredDimension(viewWidth2, dp);
                                        x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                        y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                        r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                        r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                    } else {
                                        viewWidth2 = 0;
                                        viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                        viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                        viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                        dp = AndroidUtilities.dp(52.0f);
                                        if (LocaleController.isRTL) {
                                            f2 = 8.0f;
                                            x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                        } else {
                                            f2 = 8.0f;
                                            x = AndroidUtilities.dp(8.0f);
                                        }
                                        r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                        r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                        r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                    }
                                }
                                viewWidth = viewWidth2;
                                w = 0;
                                h = 0;
                                if (r1.documentAttach != null) {
                                    viewWidth2 = 0;
                                    while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                        attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                        if (attribute instanceof TL_documentAttributeImageSize) {
                                            if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                viewWidth2++;
                                            }
                                        }
                                        w = attribute.w;
                                        h = attribute.h;
                                    }
                                }
                                if (r1.currentPhotoObject == null) {
                                    if (currentPhotoObjectThumb != null) {
                                        currentPhotoObjectThumb.size = -1;
                                    }
                                    w = r1.currentPhotoObject.w;
                                    h = r1.currentPhotoObject.h;
                                } else if (r1.inlineResult != null) {
                                    result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                    w = result[0];
                                    h = result[1];
                                }
                                dp = AndroidUtilities.dp(80.0f);
                                h = dp;
                                w = dp;
                                currentPhotoFilterThumb = "52_52_b";
                                if (r1.mediaWebpage) {
                                    currentPhotoFilter = "52_52";
                                } else {
                                    viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                    if (r1.documentAttachType != 2) {
                                        format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(format);
                                        stringBuilder.append("_b");
                                        currentPhotoFilterThumb = stringBuilder.toString();
                                        currentPhotoFilter = format;
                                    } else {
                                        format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                        currentPhotoFilterThumb = format;
                                        currentPhotoFilter = format;
                                    }
                                }
                                if (r1.documentAttachType != 6) {
                                }
                                r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                if (r1.documentAttachType != 2) {
                                    if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                    } else {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                    }
                                } else if (r1.documentAttach == null) {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                } else {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                }
                                r1.drawLinkImageView = true;
                                if (r1.mediaWebpage) {
                                    viewWidth2 = 0;
                                    viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                    viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                    viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                    dp = AndroidUtilities.dp(52.0f);
                                    if (LocaleController.isRTL) {
                                        f2 = 8.0f;
                                        x = AndroidUtilities.dp(8.0f);
                                    } else {
                                        f2 = 8.0f;
                                        x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                    }
                                    r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                    r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                    r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                } else {
                                    viewWidth2 = viewWidth;
                                    dp = MeasureSpec.getSize(heightMeasureSpec);
                                    if (dp == 0) {
                                        dp = AndroidUtilities.dp(100.0f);
                                    }
                                    setMeasuredDimension(viewWidth2, dp);
                                    x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                    y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                    r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                    r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                }
                            }
                            ext = null;
                            if (r1.documentAttach != null) {
                                r1.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, AndroidUtilities.getPhotoSize(), z);
                                currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, 80);
                                if (currentPhotoObjectThumb == r1.currentPhotoObject) {
                                    currentPhotoObjectThumb = null;
                                }
                            } else if (!MessageObject.isGifDocument(r1.documentAttach)) {
                                r1.currentPhotoObject = r1.documentAttach.thumb;
                            } else if (MessageObject.isStickerDocument(r1.documentAttach)) {
                                r1.currentPhotoObject = r1.documentAttach.thumb;
                                ext = "webp";
                            } else {
                                r1.currentPhotoObject = r1.documentAttach.thumb;
                            }
                            if (r1.inlineResult != null) {
                                if (r1.inlineResult.type.startsWith("gif")) {
                                    if (r1.documentAttachType != 2) {
                                        tLObject = (TL_webDocument) r1.inlineResult.content;
                                        r1.documentAttachType = 2;
                                    }
                                } else if (r1.inlineResult.type.equals("photo")) {
                                    if (r1.inlineResult.thumb instanceof TL_webDocument) {
                                    }
                                }
                                tLObject = r1.inlineResult.thumb;
                                if (r1.inlineResult.send_message instanceof TL_botInlineMessageMediaVenue) {
                                    if (!(r1.inlineResult.send_message instanceof TL_botInlineMessageMediaGeo)) {
                                        viewWidth = viewWidth2;
                                        w = 0;
                                        h = 0;
                                        if (r1.documentAttach != null) {
                                            viewWidth2 = 0;
                                            while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                                attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                                if (attribute instanceof TL_documentAttributeImageSize) {
                                                    if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                        viewWidth2++;
                                                    }
                                                }
                                                w = attribute.w;
                                                h = attribute.h;
                                            }
                                        }
                                        if (r1.currentPhotoObject == null) {
                                            if (currentPhotoObjectThumb != null) {
                                                currentPhotoObjectThumb.size = -1;
                                            }
                                            w = r1.currentPhotoObject.w;
                                            h = r1.currentPhotoObject.h;
                                        } else if (r1.inlineResult != null) {
                                            result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                            w = result[0];
                                            h = result[1];
                                        }
                                        dp = AndroidUtilities.dp(80.0f);
                                        h = dp;
                                        w = dp;
                                        currentPhotoFilterThumb = "52_52_b";
                                        if (r1.mediaWebpage) {
                                            viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                            if (r1.documentAttachType != 2) {
                                                format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                                currentPhotoFilterThumb = format;
                                                currentPhotoFilter = format;
                                            } else {
                                                format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                                stringBuilder = new StringBuilder();
                                                stringBuilder.append(format);
                                                stringBuilder.append("_b");
                                                currentPhotoFilterThumb = stringBuilder.toString();
                                                currentPhotoFilter = format;
                                            }
                                        } else {
                                            currentPhotoFilter = "52_52";
                                        }
                                        if (r1.documentAttachType != 6) {
                                        }
                                        r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                        if (r1.documentAttachType != 2) {
                                            if (r1.documentAttach == null) {
                                                if (r1.currentPhotoObject == null) {
                                                }
                                                r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                            } else {
                                                if (r1.currentPhotoObject == null) {
                                                }
                                                r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                            }
                                        } else if (r1.currentPhotoObject == null) {
                                            if (currentPhotoObjectThumb == null) {
                                            }
                                            r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                        } else {
                                            if (currentPhotoObjectThumb == null) {
                                            }
                                            r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                        }
                                        r1.drawLinkImageView = true;
                                        if (r1.mediaWebpage) {
                                            viewWidth2 = viewWidth;
                                            dp = MeasureSpec.getSize(heightMeasureSpec);
                                            if (dp == 0) {
                                                dp = AndroidUtilities.dp(100.0f);
                                            }
                                            setMeasuredDimension(viewWidth2, dp);
                                            x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                            y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                            r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                            r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                        } else {
                                            viewWidth2 = 0;
                                            viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                            viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                            viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                            dp = AndroidUtilities.dp(52.0f);
                                            if (LocaleController.isRTL) {
                                                f2 = 8.0f;
                                                x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                            } else {
                                                f2 = 8.0f;
                                                x = AndroidUtilities.dp(8.0f);
                                            }
                                            r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                            r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                            r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                        }
                                    }
                                }
                                lat = r1.inlineResult.send_message.geo.lat;
                                viewWidth = viewWidth2;
                                objArr = new Object[5];
                                lon = r1.inlineResult.send_message.geo._long;
                                objArr[2] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
                                objArr[3] = Double.valueOf(lat);
                                objArr[4] = Double.valueOf(lon);
                                urlLocation = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:small|%f,%f&sensor=false", objArr);
                                w = 0;
                                h = 0;
                                if (r1.documentAttach != null) {
                                    viewWidth2 = 0;
                                    while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                        attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                        if (attribute instanceof TL_documentAttributeImageSize) {
                                            if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                viewWidth2++;
                                            }
                                        }
                                        w = attribute.w;
                                        h = attribute.h;
                                    }
                                }
                                if (r1.currentPhotoObject == null) {
                                    if (currentPhotoObjectThumb != null) {
                                        currentPhotoObjectThumb.size = -1;
                                    }
                                    w = r1.currentPhotoObject.w;
                                    h = r1.currentPhotoObject.h;
                                } else if (r1.inlineResult != null) {
                                    result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                    w = result[0];
                                    h = result[1];
                                }
                                dp = AndroidUtilities.dp(80.0f);
                                h = dp;
                                w = dp;
                                currentPhotoFilterThumb = "52_52_b";
                                if (r1.mediaWebpage) {
                                    currentPhotoFilter = "52_52";
                                } else {
                                    viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                    if (r1.documentAttachType != 2) {
                                        format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(format);
                                        stringBuilder.append("_b");
                                        currentPhotoFilterThumb = stringBuilder.toString();
                                        currentPhotoFilter = format;
                                    } else {
                                        format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                        currentPhotoFilterThumb = format;
                                        currentPhotoFilter = format;
                                    }
                                }
                                if (r1.documentAttachType != 6) {
                                }
                                r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                if (r1.documentAttachType != 2) {
                                    if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                    } else {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                    }
                                } else if (r1.documentAttach == null) {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                } else {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                }
                                r1.drawLinkImageView = true;
                                if (r1.mediaWebpage) {
                                    viewWidth2 = 0;
                                    viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                    viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                    viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                    dp = AndroidUtilities.dp(52.0f);
                                    if (LocaleController.isRTL) {
                                        f2 = 8.0f;
                                        x = AndroidUtilities.dp(8.0f);
                                    } else {
                                        f2 = 8.0f;
                                        x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                    }
                                    r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                    r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                    r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                } else {
                                    viewWidth2 = viewWidth;
                                    dp = MeasureSpec.getSize(heightMeasureSpec);
                                    if (dp == 0) {
                                        dp = AndroidUtilities.dp(100.0f);
                                    }
                                    setMeasuredDimension(viewWidth2, dp);
                                    x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                    y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                    r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                    r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                }
                            }
                            viewWidth = viewWidth2;
                            w = 0;
                            h = 0;
                            if (r1.documentAttach != null) {
                                viewWidth2 = 0;
                                while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                    attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                    if (attribute instanceof TL_documentAttributeImageSize) {
                                        if (!(attribute instanceof TL_documentAttributeVideo)) {
                                            viewWidth2++;
                                        }
                                    }
                                    w = attribute.w;
                                    h = attribute.h;
                                }
                            }
                            if (r1.currentPhotoObject == null) {
                                if (currentPhotoObjectThumb != null) {
                                    currentPhotoObjectThumb.size = -1;
                                }
                                w = r1.currentPhotoObject.w;
                                h = r1.currentPhotoObject.h;
                            } else if (r1.inlineResult != null) {
                                result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                w = result[0];
                                h = result[1];
                            }
                            dp = AndroidUtilities.dp(80.0f);
                            h = dp;
                            w = dp;
                            currentPhotoFilterThumb = "52_52_b";
                            if (r1.mediaWebpage) {
                                viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                if (r1.documentAttachType != 2) {
                                    format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                    currentPhotoFilterThumb = format;
                                    currentPhotoFilter = format;
                                } else {
                                    format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(format);
                                    stringBuilder.append("_b");
                                    currentPhotoFilterThumb = stringBuilder.toString();
                                    currentPhotoFilter = format;
                                }
                            } else {
                                currentPhotoFilter = "52_52";
                            }
                            if (r1.documentAttachType != 6) {
                            }
                            r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                            if (r1.documentAttachType != 2) {
                                if (r1.documentAttach == null) {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                } else {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                }
                            } else if (r1.currentPhotoObject == null) {
                                if (currentPhotoObjectThumb == null) {
                                }
                                r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                            } else {
                                if (currentPhotoObjectThumb == null) {
                                }
                                r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                            }
                            r1.drawLinkImageView = true;
                            if (r1.mediaWebpage) {
                                viewWidth2 = viewWidth;
                                dp = MeasureSpec.getSize(heightMeasureSpec);
                                if (dp == 0) {
                                    dp = AndroidUtilities.dp(100.0f);
                                }
                                setMeasuredDimension(viewWidth2, dp);
                                x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                            } else {
                                viewWidth2 = 0;
                                viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                dp = AndroidUtilities.dp(52.0f);
                                if (LocaleController.isRTL) {
                                    f2 = 8.0f;
                                    x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                } else {
                                    f2 = 8.0f;
                                    x = AndroidUtilities.dp(8.0f);
                                }
                                r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                            }
                        }
                        photoThumbs = photoThumbs3;
                        ext = null;
                        if (r1.documentAttach != null) {
                            r1.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, AndroidUtilities.getPhotoSize(), z);
                            currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, 80);
                            if (currentPhotoObjectThumb == r1.currentPhotoObject) {
                                currentPhotoObjectThumb = null;
                            }
                        } else if (!MessageObject.isGifDocument(r1.documentAttach)) {
                            r1.currentPhotoObject = r1.documentAttach.thumb;
                        } else if (MessageObject.isStickerDocument(r1.documentAttach)) {
                            r1.currentPhotoObject = r1.documentAttach.thumb;
                        } else {
                            r1.currentPhotoObject = r1.documentAttach.thumb;
                            ext = "webp";
                        }
                        if (r1.inlineResult != null) {
                            if (r1.inlineResult.type.startsWith("gif")) {
                                if (r1.inlineResult.type.equals("photo")) {
                                    if (r1.inlineResult.thumb instanceof TL_webDocument) {
                                    }
                                }
                            } else if (r1.documentAttachType != 2) {
                                tLObject = (TL_webDocument) r1.inlineResult.content;
                                r1.documentAttachType = 2;
                            }
                            tLObject = r1.inlineResult.thumb;
                            if (r1.inlineResult.send_message instanceof TL_botInlineMessageMediaVenue) {
                                if (!(r1.inlineResult.send_message instanceof TL_botInlineMessageMediaGeo)) {
                                    viewWidth = viewWidth2;
                                    w = 0;
                                    h = 0;
                                    if (r1.documentAttach != null) {
                                        viewWidth2 = 0;
                                        while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                            attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                            if (attribute instanceof TL_documentAttributeImageSize) {
                                                if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                    viewWidth2++;
                                                }
                                            }
                                            w = attribute.w;
                                            h = attribute.h;
                                        }
                                    }
                                    if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb != null) {
                                            currentPhotoObjectThumb.size = -1;
                                        }
                                        w = r1.currentPhotoObject.w;
                                        h = r1.currentPhotoObject.h;
                                    } else if (r1.inlineResult != null) {
                                        result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                        w = result[0];
                                        h = result[1];
                                    }
                                    dp = AndroidUtilities.dp(80.0f);
                                    h = dp;
                                    w = dp;
                                    currentPhotoFilterThumb = "52_52_b";
                                    if (r1.mediaWebpage) {
                                        currentPhotoFilter = "52_52";
                                    } else {
                                        viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                        if (r1.documentAttachType != 2) {
                                            format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(format);
                                            stringBuilder.append("_b");
                                            currentPhotoFilterThumb = stringBuilder.toString();
                                            currentPhotoFilter = format;
                                        } else {
                                            format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            currentPhotoFilterThumb = format;
                                            currentPhotoFilter = format;
                                        }
                                    }
                                    if (r1.documentAttachType != 6) {
                                    }
                                    r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                    if (r1.documentAttachType != 2) {
                                        if (r1.currentPhotoObject == null) {
                                            if (currentPhotoObjectThumb == null) {
                                            }
                                            r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                        } else {
                                            if (currentPhotoObjectThumb == null) {
                                            }
                                            r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                        }
                                    } else if (r1.documentAttach == null) {
                                        if (r1.currentPhotoObject == null) {
                                        }
                                        r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                    } else {
                                        if (r1.currentPhotoObject == null) {
                                        }
                                        r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                    }
                                    r1.drawLinkImageView = true;
                                    if (r1.mediaWebpage) {
                                        viewWidth2 = 0;
                                        viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                        viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                        viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                        dp = AndroidUtilities.dp(52.0f);
                                        if (LocaleController.isRTL) {
                                            f2 = 8.0f;
                                            x = AndroidUtilities.dp(8.0f);
                                        } else {
                                            f2 = 8.0f;
                                            x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                        }
                                        r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                        r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                        r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                    } else {
                                        viewWidth2 = viewWidth;
                                        dp = MeasureSpec.getSize(heightMeasureSpec);
                                        if (dp == 0) {
                                            dp = AndroidUtilities.dp(100.0f);
                                        }
                                        setMeasuredDimension(viewWidth2, dp);
                                        x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                        y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                        r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                        r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                    }
                                }
                            }
                            lat = r1.inlineResult.send_message.geo.lat;
                            viewWidth = viewWidth2;
                            objArr = new Object[5];
                            lon = r1.inlineResult.send_message.geo._long;
                            objArr[2] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
                            objArr[3] = Double.valueOf(lat);
                            objArr[4] = Double.valueOf(lon);
                            urlLocation = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:small|%f,%f&sensor=false", objArr);
                            w = 0;
                            h = 0;
                            if (r1.documentAttach != null) {
                                viewWidth2 = 0;
                                while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                    attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                    if (attribute instanceof TL_documentAttributeImageSize) {
                                        if (!(attribute instanceof TL_documentAttributeVideo)) {
                                            viewWidth2++;
                                        }
                                    }
                                    w = attribute.w;
                                    h = attribute.h;
                                }
                            }
                            if (r1.currentPhotoObject == null) {
                                if (currentPhotoObjectThumb != null) {
                                    currentPhotoObjectThumb.size = -1;
                                }
                                w = r1.currentPhotoObject.w;
                                h = r1.currentPhotoObject.h;
                            } else if (r1.inlineResult != null) {
                                result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                w = result[0];
                                h = result[1];
                            }
                            dp = AndroidUtilities.dp(80.0f);
                            h = dp;
                            w = dp;
                            currentPhotoFilterThumb = "52_52_b";
                            if (r1.mediaWebpage) {
                                viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                if (r1.documentAttachType != 2) {
                                    format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                    currentPhotoFilterThumb = format;
                                    currentPhotoFilter = format;
                                } else {
                                    format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(format);
                                    stringBuilder.append("_b");
                                    currentPhotoFilterThumb = stringBuilder.toString();
                                    currentPhotoFilter = format;
                                }
                            } else {
                                currentPhotoFilter = "52_52";
                            }
                            if (r1.documentAttachType != 6) {
                            }
                            r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                            if (r1.documentAttachType != 2) {
                                if (r1.documentAttach == null) {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                } else {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                }
                            } else if (r1.currentPhotoObject == null) {
                                if (currentPhotoObjectThumb == null) {
                                }
                                r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                            } else {
                                if (currentPhotoObjectThumb == null) {
                                }
                                r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                            }
                            r1.drawLinkImageView = true;
                            if (r1.mediaWebpage) {
                                viewWidth2 = viewWidth;
                                dp = MeasureSpec.getSize(heightMeasureSpec);
                                if (dp == 0) {
                                    dp = AndroidUtilities.dp(100.0f);
                                }
                                setMeasuredDimension(viewWidth2, dp);
                                x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                            } else {
                                viewWidth2 = 0;
                                viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                dp = AndroidUtilities.dp(52.0f);
                                if (LocaleController.isRTL) {
                                    f2 = 8.0f;
                                    x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                } else {
                                    f2 = 8.0f;
                                    x = AndroidUtilities.dp(8.0f);
                                }
                                r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                            }
                        }
                        viewWidth = viewWidth2;
                        w = 0;
                        h = 0;
                        if (r1.documentAttach != null) {
                            viewWidth2 = 0;
                            while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                if (attribute instanceof TL_documentAttributeImageSize) {
                                    if (!(attribute instanceof TL_documentAttributeVideo)) {
                                        viewWidth2++;
                                    }
                                }
                                w = attribute.w;
                                h = attribute.h;
                            }
                        }
                        if (r1.currentPhotoObject == null) {
                            if (currentPhotoObjectThumb != null) {
                                currentPhotoObjectThumb.size = -1;
                            }
                            w = r1.currentPhotoObject.w;
                            h = r1.currentPhotoObject.h;
                        } else if (r1.inlineResult != null) {
                            result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                            w = result[0];
                            h = result[1];
                        }
                        dp = AndroidUtilities.dp(80.0f);
                        h = dp;
                        w = dp;
                        currentPhotoFilterThumb = "52_52_b";
                        if (r1.mediaWebpage) {
                            currentPhotoFilter = "52_52";
                        } else {
                            viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                            if (r1.documentAttachType != 2) {
                                format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(format);
                                stringBuilder.append("_b");
                                currentPhotoFilterThumb = stringBuilder.toString();
                                currentPhotoFilter = format;
                            } else {
                                format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                currentPhotoFilterThumb = format;
                                currentPhotoFilter = format;
                            }
                        }
                        if (r1.documentAttachType != 6) {
                        }
                        r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                        if (r1.documentAttachType != 2) {
                            if (r1.currentPhotoObject == null) {
                                if (currentPhotoObjectThumb == null) {
                                }
                                r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                            } else {
                                if (currentPhotoObjectThumb == null) {
                                }
                                r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                            }
                        } else if (r1.documentAttach == null) {
                            if (r1.currentPhotoObject == null) {
                            }
                            r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                        } else {
                            if (r1.currentPhotoObject == null) {
                            }
                            r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                        }
                        r1.drawLinkImageView = true;
                        if (r1.mediaWebpage) {
                            viewWidth2 = 0;
                            viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                            viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                            viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                            dp = AndroidUtilities.dp(52.0f);
                            if (LocaleController.isRTL) {
                                f2 = 8.0f;
                                x = AndroidUtilities.dp(8.0f);
                            } else {
                                f2 = 8.0f;
                                x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                            }
                            r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                            r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                            r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                        } else {
                            viewWidth2 = viewWidth;
                            dp = MeasureSpec.getSize(heightMeasureSpec);
                            if (dp == 0) {
                                dp = AndroidUtilities.dp(100.0f);
                            }
                            setMeasuredDimension(viewWidth2, dp);
                            x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                            y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                            r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                            r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                        }
                    }
                } catch (Exception e5) {
                    e322 = e5;
                    z = true;
                    f = 4.0f;
                    c2 = ' ';
                    c = '\n';
                    FileLog.e(e322);
                    if (r1.inlineResult.url != null) {
                        width = (int) Math.ceil((double) Theme.chat_contextResult_descriptionTextPaint.measureText(r1.inlineResult.url));
                        linkFinal = TextUtils.ellipsize(r1.inlineResult.url.replace(c, c2), Theme.chat_contextResult_descriptionTextPaint, (float) Math.min(width, maxWidth), TruncateAt.MIDDLE);
                        staticLayout = staticLayout;
                        photoThumbs = photoThumbs3;
                        r1.linkLayout = new StaticLayout(linkFinal, Theme.chat_contextResult_descriptionTextPaint, maxWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                        ext = null;
                        if (r1.documentAttach != null) {
                            r1.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, AndroidUtilities.getPhotoSize(), z);
                            currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, 80);
                            if (currentPhotoObjectThumb == r1.currentPhotoObject) {
                                currentPhotoObjectThumb = null;
                            }
                        } else if (!MessageObject.isGifDocument(r1.documentAttach)) {
                            r1.currentPhotoObject = r1.documentAttach.thumb;
                        } else if (MessageObject.isStickerDocument(r1.documentAttach)) {
                            r1.currentPhotoObject = r1.documentAttach.thumb;
                            ext = "webp";
                        } else {
                            r1.currentPhotoObject = r1.documentAttach.thumb;
                        }
                        if (r1.inlineResult != null) {
                            if (r1.inlineResult.type.startsWith("gif")) {
                                if (r1.documentAttachType != 2) {
                                    tLObject = (TL_webDocument) r1.inlineResult.content;
                                    r1.documentAttachType = 2;
                                }
                            } else if (r1.inlineResult.type.equals("photo")) {
                                if (r1.inlineResult.thumb instanceof TL_webDocument) {
                                }
                            }
                            tLObject = r1.inlineResult.thumb;
                            if (r1.inlineResult.send_message instanceof TL_botInlineMessageMediaVenue) {
                                if (!(r1.inlineResult.send_message instanceof TL_botInlineMessageMediaGeo)) {
                                    viewWidth = viewWidth2;
                                    w = 0;
                                    h = 0;
                                    if (r1.documentAttach != null) {
                                        viewWidth2 = 0;
                                        while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                            attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                            if (attribute instanceof TL_documentAttributeImageSize) {
                                                if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                    viewWidth2++;
                                                }
                                            }
                                            w = attribute.w;
                                            h = attribute.h;
                                        }
                                    }
                                    if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb != null) {
                                            currentPhotoObjectThumb.size = -1;
                                        }
                                        w = r1.currentPhotoObject.w;
                                        h = r1.currentPhotoObject.h;
                                    } else if (r1.inlineResult != null) {
                                        result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                        w = result[0];
                                        h = result[1];
                                    }
                                    dp = AndroidUtilities.dp(80.0f);
                                    h = dp;
                                    w = dp;
                                    currentPhotoFilterThumb = "52_52_b";
                                    if (r1.mediaWebpage) {
                                        viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                        if (r1.documentAttachType != 2) {
                                            format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            currentPhotoFilterThumb = format;
                                            currentPhotoFilter = format;
                                        } else {
                                            format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(format);
                                            stringBuilder.append("_b");
                                            currentPhotoFilterThumb = stringBuilder.toString();
                                            currentPhotoFilter = format;
                                        }
                                    } else {
                                        currentPhotoFilter = "52_52";
                                    }
                                    if (r1.documentAttachType != 6) {
                                    }
                                    r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                    if (r1.documentAttachType != 2) {
                                        if (r1.documentAttach == null) {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                        } else {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                        }
                                    } else if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                    } else {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                    }
                                    r1.drawLinkImageView = true;
                                    if (r1.mediaWebpage) {
                                        viewWidth2 = viewWidth;
                                        dp = MeasureSpec.getSize(heightMeasureSpec);
                                        if (dp == 0) {
                                            dp = AndroidUtilities.dp(100.0f);
                                        }
                                        setMeasuredDimension(viewWidth2, dp);
                                        x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                        y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                        r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                        r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                    } else {
                                        viewWidth2 = 0;
                                        viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                        viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                        viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                        dp = AndroidUtilities.dp(52.0f);
                                        if (LocaleController.isRTL) {
                                            f2 = 8.0f;
                                            x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                        } else {
                                            f2 = 8.0f;
                                            x = AndroidUtilities.dp(8.0f);
                                        }
                                        r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                        r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                        r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                    }
                                }
                            }
                            lat = r1.inlineResult.send_message.geo.lat;
                            viewWidth = viewWidth2;
                            objArr = new Object[5];
                            lon = r1.inlineResult.send_message.geo._long;
                            objArr[2] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
                            objArr[3] = Double.valueOf(lat);
                            objArr[4] = Double.valueOf(lon);
                            urlLocation = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:small|%f,%f&sensor=false", objArr);
                            w = 0;
                            h = 0;
                            if (r1.documentAttach != null) {
                                viewWidth2 = 0;
                                while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                    attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                    if (attribute instanceof TL_documentAttributeImageSize) {
                                        if (!(attribute instanceof TL_documentAttributeVideo)) {
                                            viewWidth2++;
                                        }
                                    }
                                    w = attribute.w;
                                    h = attribute.h;
                                }
                            }
                            if (r1.currentPhotoObject == null) {
                                if (currentPhotoObjectThumb != null) {
                                    currentPhotoObjectThumb.size = -1;
                                }
                                w = r1.currentPhotoObject.w;
                                h = r1.currentPhotoObject.h;
                            } else if (r1.inlineResult != null) {
                                result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                w = result[0];
                                h = result[1];
                            }
                            dp = AndroidUtilities.dp(80.0f);
                            h = dp;
                            w = dp;
                            currentPhotoFilterThumb = "52_52_b";
                            if (r1.mediaWebpage) {
                                currentPhotoFilter = "52_52";
                            } else {
                                viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                if (r1.documentAttachType != 2) {
                                    format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(format);
                                    stringBuilder.append("_b");
                                    currentPhotoFilterThumb = stringBuilder.toString();
                                    currentPhotoFilter = format;
                                } else {
                                    format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                    currentPhotoFilterThumb = format;
                                    currentPhotoFilter = format;
                                }
                            }
                            if (r1.documentAttachType != 6) {
                            }
                            r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                            if (r1.documentAttachType != 2) {
                                if (r1.currentPhotoObject == null) {
                                    if (currentPhotoObjectThumb == null) {
                                    }
                                    r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                } else {
                                    if (currentPhotoObjectThumb == null) {
                                    }
                                    r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                }
                            } else if (r1.documentAttach == null) {
                                if (r1.currentPhotoObject == null) {
                                }
                                r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                            } else {
                                if (r1.currentPhotoObject == null) {
                                }
                                r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                            }
                            r1.drawLinkImageView = true;
                            if (r1.mediaWebpage) {
                                viewWidth2 = 0;
                                viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                dp = AndroidUtilities.dp(52.0f);
                                if (LocaleController.isRTL) {
                                    f2 = 8.0f;
                                    x = AndroidUtilities.dp(8.0f);
                                } else {
                                    f2 = 8.0f;
                                    x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                }
                                r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                            } else {
                                viewWidth2 = viewWidth;
                                dp = MeasureSpec.getSize(heightMeasureSpec);
                                if (dp == 0) {
                                    dp = AndroidUtilities.dp(100.0f);
                                }
                                setMeasuredDimension(viewWidth2, dp);
                                x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                            }
                        }
                        viewWidth = viewWidth2;
                        w = 0;
                        h = 0;
                        if (r1.documentAttach != null) {
                            viewWidth2 = 0;
                            while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                if (attribute instanceof TL_documentAttributeImageSize) {
                                    if (!(attribute instanceof TL_documentAttributeVideo)) {
                                        viewWidth2++;
                                    }
                                }
                                w = attribute.w;
                                h = attribute.h;
                            }
                        }
                        if (r1.currentPhotoObject == null) {
                            if (currentPhotoObjectThumb != null) {
                                currentPhotoObjectThumb.size = -1;
                            }
                            w = r1.currentPhotoObject.w;
                            h = r1.currentPhotoObject.h;
                        } else if (r1.inlineResult != null) {
                            result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                            w = result[0];
                            h = result[1];
                        }
                        dp = AndroidUtilities.dp(80.0f);
                        h = dp;
                        w = dp;
                        currentPhotoFilterThumb = "52_52_b";
                        if (r1.mediaWebpage) {
                            viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                            if (r1.documentAttachType != 2) {
                                format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                currentPhotoFilterThumb = format;
                                currentPhotoFilter = format;
                            } else {
                                format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(format);
                                stringBuilder.append("_b");
                                currentPhotoFilterThumb = stringBuilder.toString();
                                currentPhotoFilter = format;
                            }
                        } else {
                            currentPhotoFilter = "52_52";
                        }
                        if (r1.documentAttachType != 6) {
                        }
                        r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                        if (r1.documentAttachType != 2) {
                            if (r1.documentAttach == null) {
                                if (r1.currentPhotoObject == null) {
                                }
                                r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                            } else {
                                if (r1.currentPhotoObject == null) {
                                }
                                r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                            }
                        } else if (r1.currentPhotoObject == null) {
                            if (currentPhotoObjectThumb == null) {
                            }
                            r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                        } else {
                            if (currentPhotoObjectThumb == null) {
                            }
                            r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                        }
                        r1.drawLinkImageView = true;
                        if (r1.mediaWebpage) {
                            viewWidth2 = viewWidth;
                            dp = MeasureSpec.getSize(heightMeasureSpec);
                            if (dp == 0) {
                                dp = AndroidUtilities.dp(100.0f);
                            }
                            setMeasuredDimension(viewWidth2, dp);
                            x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                            y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                            r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                            r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                        } else {
                            viewWidth2 = 0;
                            viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                            viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                            viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                            dp = AndroidUtilities.dp(52.0f);
                            if (LocaleController.isRTL) {
                                f2 = 8.0f;
                                x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                            } else {
                                f2 = 8.0f;
                                x = AndroidUtilities.dp(8.0f);
                            }
                            r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                            r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                            r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                        }
                    }
                    photoThumbs = photoThumbs3;
                    ext = null;
                    if (r1.documentAttach != null) {
                        r1.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, AndroidUtilities.getPhotoSize(), z);
                        currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, 80);
                        if (currentPhotoObjectThumb == r1.currentPhotoObject) {
                            currentPhotoObjectThumb = null;
                        }
                    } else if (!MessageObject.isGifDocument(r1.documentAttach)) {
                        r1.currentPhotoObject = r1.documentAttach.thumb;
                    } else if (MessageObject.isStickerDocument(r1.documentAttach)) {
                        r1.currentPhotoObject = r1.documentAttach.thumb;
                    } else {
                        r1.currentPhotoObject = r1.documentAttach.thumb;
                        ext = "webp";
                    }
                    if (r1.inlineResult != null) {
                        if (r1.inlineResult.type.startsWith("gif")) {
                            if (r1.inlineResult.type.equals("photo")) {
                                if (r1.inlineResult.thumb instanceof TL_webDocument) {
                                }
                            }
                        } else if (r1.documentAttachType != 2) {
                            tLObject = (TL_webDocument) r1.inlineResult.content;
                            r1.documentAttachType = 2;
                        }
                        tLObject = r1.inlineResult.thumb;
                        if (r1.inlineResult.send_message instanceof TL_botInlineMessageMediaVenue) {
                            if (!(r1.inlineResult.send_message instanceof TL_botInlineMessageMediaGeo)) {
                                viewWidth = viewWidth2;
                                w = 0;
                                h = 0;
                                if (r1.documentAttach != null) {
                                    viewWidth2 = 0;
                                    while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                        attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                        if (attribute instanceof TL_documentAttributeImageSize) {
                                            if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                viewWidth2++;
                                            }
                                        }
                                        w = attribute.w;
                                        h = attribute.h;
                                    }
                                }
                                if (r1.currentPhotoObject == null) {
                                    if (currentPhotoObjectThumb != null) {
                                        currentPhotoObjectThumb.size = -1;
                                    }
                                    w = r1.currentPhotoObject.w;
                                    h = r1.currentPhotoObject.h;
                                } else if (r1.inlineResult != null) {
                                    result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                    w = result[0];
                                    h = result[1];
                                }
                                dp = AndroidUtilities.dp(80.0f);
                                h = dp;
                                w = dp;
                                currentPhotoFilterThumb = "52_52_b";
                                if (r1.mediaWebpage) {
                                    currentPhotoFilter = "52_52";
                                } else {
                                    viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                    if (r1.documentAttachType != 2) {
                                        format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(format);
                                        stringBuilder.append("_b");
                                        currentPhotoFilterThumb = stringBuilder.toString();
                                        currentPhotoFilter = format;
                                    } else {
                                        format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                        currentPhotoFilterThumb = format;
                                        currentPhotoFilter = format;
                                    }
                                }
                                if (r1.documentAttachType != 6) {
                                }
                                r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                if (r1.documentAttachType != 2) {
                                    if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                    } else {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                    }
                                } else if (r1.documentAttach == null) {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                } else {
                                    if (r1.currentPhotoObject == null) {
                                    }
                                    r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                }
                                r1.drawLinkImageView = true;
                                if (r1.mediaWebpage) {
                                    viewWidth2 = 0;
                                    viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                    viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                    viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                    dp = AndroidUtilities.dp(52.0f);
                                    if (LocaleController.isRTL) {
                                        f2 = 8.0f;
                                        x = AndroidUtilities.dp(8.0f);
                                    } else {
                                        f2 = 8.0f;
                                        x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                    }
                                    r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                    r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                    r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                } else {
                                    viewWidth2 = viewWidth;
                                    dp = MeasureSpec.getSize(heightMeasureSpec);
                                    if (dp == 0) {
                                        dp = AndroidUtilities.dp(100.0f);
                                    }
                                    setMeasuredDimension(viewWidth2, dp);
                                    x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                    y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                    r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                    r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                }
                            }
                        }
                        lat = r1.inlineResult.send_message.geo.lat;
                        viewWidth = viewWidth2;
                        objArr = new Object[5];
                        lon = r1.inlineResult.send_message.geo._long;
                        objArr[2] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
                        objArr[3] = Double.valueOf(lat);
                        objArr[4] = Double.valueOf(lon);
                        urlLocation = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:small|%f,%f&sensor=false", objArr);
                        w = 0;
                        h = 0;
                        if (r1.documentAttach != null) {
                            viewWidth2 = 0;
                            while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                if (attribute instanceof TL_documentAttributeImageSize) {
                                    if (!(attribute instanceof TL_documentAttributeVideo)) {
                                        viewWidth2++;
                                    }
                                }
                                w = attribute.w;
                                h = attribute.h;
                            }
                        }
                        if (r1.currentPhotoObject == null) {
                            if (currentPhotoObjectThumb != null) {
                                currentPhotoObjectThumb.size = -1;
                            }
                            w = r1.currentPhotoObject.w;
                            h = r1.currentPhotoObject.h;
                        } else if (r1.inlineResult != null) {
                            result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                            w = result[0];
                            h = result[1];
                        }
                        dp = AndroidUtilities.dp(80.0f);
                        h = dp;
                        w = dp;
                        currentPhotoFilterThumb = "52_52_b";
                        if (r1.mediaWebpage) {
                            viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                            if (r1.documentAttachType != 2) {
                                format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                currentPhotoFilterThumb = format;
                                currentPhotoFilter = format;
                            } else {
                                format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(format);
                                stringBuilder.append("_b");
                                currentPhotoFilterThumb = stringBuilder.toString();
                                currentPhotoFilter = format;
                            }
                        } else {
                            currentPhotoFilter = "52_52";
                        }
                        if (r1.documentAttachType != 6) {
                        }
                        r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                        if (r1.documentAttachType != 2) {
                            if (r1.documentAttach == null) {
                                if (r1.currentPhotoObject == null) {
                                }
                                r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                            } else {
                                if (r1.currentPhotoObject == null) {
                                }
                                r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                            }
                        } else if (r1.currentPhotoObject == null) {
                            if (currentPhotoObjectThumb == null) {
                            }
                            r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                        } else {
                            if (currentPhotoObjectThumb == null) {
                            }
                            r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                        }
                        r1.drawLinkImageView = true;
                        if (r1.mediaWebpage) {
                            viewWidth2 = viewWidth;
                            dp = MeasureSpec.getSize(heightMeasureSpec);
                            if (dp == 0) {
                                dp = AndroidUtilities.dp(100.0f);
                            }
                            setMeasuredDimension(viewWidth2, dp);
                            x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                            y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                            r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                            r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                        } else {
                            viewWidth2 = 0;
                            viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                            viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                            viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                            dp = AndroidUtilities.dp(52.0f);
                            if (LocaleController.isRTL) {
                                f2 = 8.0f;
                                x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                            } else {
                                f2 = 8.0f;
                                x = AndroidUtilities.dp(8.0f);
                            }
                            r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                            r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                            r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                        }
                    }
                    viewWidth = viewWidth2;
                    w = 0;
                    h = 0;
                    if (r1.documentAttach != null) {
                        viewWidth2 = 0;
                        while (viewWidth2 < r1.documentAttach.attributes.size()) {
                            attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                            if (attribute instanceof TL_documentAttributeImageSize) {
                                if (!(attribute instanceof TL_documentAttributeVideo)) {
                                    viewWidth2++;
                                }
                            }
                            w = attribute.w;
                            h = attribute.h;
                        }
                    }
                    if (r1.currentPhotoObject == null) {
                        if (currentPhotoObjectThumb != null) {
                            currentPhotoObjectThumb.size = -1;
                        }
                        w = r1.currentPhotoObject.w;
                        h = r1.currentPhotoObject.h;
                    } else if (r1.inlineResult != null) {
                        result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                        w = result[0];
                        h = result[1];
                    }
                    dp = AndroidUtilities.dp(80.0f);
                    h = dp;
                    w = dp;
                    currentPhotoFilterThumb = "52_52_b";
                    if (r1.mediaWebpage) {
                        currentPhotoFilter = "52_52";
                    } else {
                        viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                        if (r1.documentAttachType != 2) {
                            format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(format);
                            stringBuilder.append("_b");
                            currentPhotoFilterThumb = stringBuilder.toString();
                            currentPhotoFilter = format;
                        } else {
                            format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                            currentPhotoFilterThumb = format;
                            currentPhotoFilter = format;
                        }
                    }
                    if (r1.documentAttachType != 6) {
                    }
                    r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                    if (r1.documentAttachType != 2) {
                        if (r1.currentPhotoObject == null) {
                            if (currentPhotoObjectThumb == null) {
                            }
                            r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                        } else {
                            if (currentPhotoObjectThumb == null) {
                            }
                            r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                        }
                    } else if (r1.documentAttach == null) {
                        if (r1.currentPhotoObject == null) {
                        }
                        r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                    } else {
                        if (r1.currentPhotoObject == null) {
                        }
                        r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                    }
                    r1.drawLinkImageView = true;
                    if (r1.mediaWebpage) {
                        viewWidth2 = 0;
                        viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                        viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                        viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                        dp = AndroidUtilities.dp(52.0f);
                        if (LocaleController.isRTL) {
                            f2 = 8.0f;
                            x = AndroidUtilities.dp(8.0f);
                        } else {
                            f2 = 8.0f;
                            x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                        }
                        r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                        r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                        r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                    } else {
                        viewWidth2 = viewWidth;
                        dp = MeasureSpec.getSize(heightMeasureSpec);
                        if (dp == 0) {
                            dp = AndroidUtilities.dp(100.0f);
                        }
                        setMeasuredDimension(viewWidth2, dp);
                        x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                        y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                        r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                        r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                    }
                }
            }
            z = true;
            f = 4.0f;
            c2 = ' ';
            c = '\n';
            if (r1.inlineResult.url != null) {
                width = (int) Math.ceil((double) Theme.chat_contextResult_descriptionTextPaint.measureText(r1.inlineResult.url));
                linkFinal = TextUtils.ellipsize(r1.inlineResult.url.replace(c, c2), Theme.chat_contextResult_descriptionTextPaint, (float) Math.min(width, maxWidth), TruncateAt.MIDDLE);
                staticLayout = staticLayout;
                photoThumbs = photoThumbs3;
                r1.linkLayout = new StaticLayout(linkFinal, Theme.chat_contextResult_descriptionTextPaint, maxWidth, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                ext = null;
                if (r1.documentAttach != null) {
                    if (!MessageObject.isGifDocument(r1.documentAttach)) {
                        r1.currentPhotoObject = r1.documentAttach.thumb;
                    } else if (MessageObject.isStickerDocument(r1.documentAttach)) {
                        r1.currentPhotoObject = r1.documentAttach.thumb;
                        ext = "webp";
                    } else if (!(r1.documentAttachType == 5 || r1.documentAttachType == 3)) {
                        r1.currentPhotoObject = r1.documentAttach.thumb;
                    }
                } else if (!(r1.inlineResult == null || r1.inlineResult.photo == null)) {
                    r1.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, AndroidUtilities.getPhotoSize(), z);
                    currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, 80);
                    if (currentPhotoObjectThumb == r1.currentPhotoObject) {
                        currentPhotoObjectThumb = null;
                    }
                }
                if (r1.inlineResult != null) {
                    if ((r1.inlineResult.content instanceof TL_webDocument) && r1.inlineResult.type != null) {
                        if (r1.inlineResult.type.startsWith("gif")) {
                            if (r1.documentAttachType != 2) {
                                tLObject = (TL_webDocument) r1.inlineResult.content;
                                r1.documentAttachType = 2;
                            }
                        } else if (r1.inlineResult.type.equals("photo")) {
                            if (r1.inlineResult.thumb instanceof TL_webDocument) {
                            }
                        }
                    }
                    if (tLObject == null && (r1.inlineResult.thumb instanceof TL_webDocument)) {
                        tLObject = r1.inlineResult.thumb;
                    }
                    if (tLObject == null && r1.currentPhotoObject == null && currentPhotoObjectThumb == null) {
                        if (r1.inlineResult.send_message instanceof TL_botInlineMessageMediaVenue) {
                            if (!(r1.inlineResult.send_message instanceof TL_botInlineMessageMediaGeo)) {
                                viewWidth = viewWidth2;
                                w = 0;
                                h = 0;
                                if (r1.documentAttach != null) {
                                    viewWidth2 = 0;
                                    while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                        attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                        if (attribute instanceof TL_documentAttributeImageSize) {
                                            if (!(attribute instanceof TL_documentAttributeVideo)) {
                                                viewWidth2++;
                                            }
                                        }
                                        w = attribute.w;
                                        h = attribute.h;
                                    }
                                }
                                if (w == 0 || h == 0) {
                                    if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb != null) {
                                            currentPhotoObjectThumb.size = -1;
                                        }
                                        w = r1.currentPhotoObject.w;
                                        h = r1.currentPhotoObject.h;
                                    } else if (r1.inlineResult != null) {
                                        result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                                        w = result[0];
                                        h = result[1];
                                    }
                                }
                                if (w == 0 || h == 0) {
                                    dp = AndroidUtilities.dp(80.0f);
                                    h = dp;
                                    w = dp;
                                }
                                if (!(r1.documentAttach == null && r1.currentPhotoObject == null && tLObject == null && urlLocation == null)) {
                                    currentPhotoFilterThumb = "52_52_b";
                                    if (r1.mediaWebpage) {
                                        viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                                        if (r1.documentAttachType != 2) {
                                            format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            currentPhotoFilterThumb = format;
                                            currentPhotoFilter = format;
                                        } else {
                                            format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                            stringBuilder = new StringBuilder();
                                            stringBuilder.append(format);
                                            stringBuilder.append("_b");
                                            currentPhotoFilterThumb = stringBuilder.toString();
                                            currentPhotoFilter = format;
                                        }
                                    } else {
                                        currentPhotoFilter = "52_52";
                                    }
                                    if (r1.documentAttachType != 6) {
                                    }
                                    r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                                    if (r1.documentAttachType != 2) {
                                        if (r1.documentAttach == null) {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                                        } else {
                                            if (r1.currentPhotoObject == null) {
                                            }
                                            r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                                        }
                                    } else if (r1.currentPhotoObject == null) {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                                    } else {
                                        if (currentPhotoObjectThumb == null) {
                                        }
                                        r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                                    }
                                    r1.drawLinkImageView = true;
                                }
                                if (r1.mediaWebpage) {
                                    viewWidth2 = viewWidth;
                                    dp = MeasureSpec.getSize(heightMeasureSpec);
                                    if (dp == 0) {
                                        dp = AndroidUtilities.dp(100.0f);
                                    }
                                    setMeasuredDimension(viewWidth2, dp);
                                    x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                                    y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                                    r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                                    r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                                } else {
                                    viewWidth2 = 0;
                                    if (!(r1.titleLayout == null || r1.titleLayout.getLineCount() == 0)) {
                                        viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                                    }
                                    if (!(r1.descriptionLayout == null || r1.descriptionLayout.getLineCount() == 0)) {
                                        viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                                    }
                                    if (r1.linkLayout != null && r1.linkLayout.getLineCount() > 0) {
                                        viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                                    }
                                    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                                    dp = AndroidUtilities.dp(52.0f);
                                    if (LocaleController.isRTL) {
                                        f2 = 8.0f;
                                        x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                                    } else {
                                        f2 = 8.0f;
                                        x = AndroidUtilities.dp(8.0f);
                                    }
                                    r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                                    r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                                    if (r1.documentAttachType == 3 || r1.documentAttachType == 5) {
                                        r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                                    }
                                }
                            }
                        }
                        lat = r1.inlineResult.send_message.geo.lat;
                        viewWidth = viewWidth2;
                        objArr = new Object[5];
                        lon = r1.inlineResult.send_message.geo._long;
                        objArr[2] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
                        objArr[3] = Double.valueOf(lat);
                        objArr[4] = Double.valueOf(lon);
                        urlLocation = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:small|%f,%f&sensor=false", objArr);
                        w = 0;
                        h = 0;
                        if (r1.documentAttach != null) {
                            viewWidth2 = 0;
                            while (viewWidth2 < r1.documentAttach.attributes.size()) {
                                attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                                if (attribute instanceof TL_documentAttributeImageSize) {
                                    if (!(attribute instanceof TL_documentAttributeVideo)) {
                                        viewWidth2++;
                                    }
                                }
                                w = attribute.w;
                                h = attribute.h;
                            }
                        }
                        if (r1.currentPhotoObject == null) {
                            if (currentPhotoObjectThumb != null) {
                                currentPhotoObjectThumb.size = -1;
                            }
                            w = r1.currentPhotoObject.w;
                            h = r1.currentPhotoObject.h;
                        } else if (r1.inlineResult != null) {
                            result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                            w = result[0];
                            h = result[1];
                        }
                        dp = AndroidUtilities.dp(80.0f);
                        h = dp;
                        w = dp;
                        currentPhotoFilterThumb = "52_52_b";
                        if (r1.mediaWebpage) {
                            currentPhotoFilter = "52_52";
                        } else {
                            viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                            if (r1.documentAttachType != 2) {
                                format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(format);
                                stringBuilder.append("_b");
                                currentPhotoFilterThumb = stringBuilder.toString();
                                currentPhotoFilter = format;
                            } else {
                                format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                                currentPhotoFilterThumb = format;
                                currentPhotoFilter = format;
                            }
                        }
                        if (r1.documentAttachType != 6) {
                        }
                        r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                        if (r1.documentAttachType != 2) {
                            if (r1.currentPhotoObject == null) {
                                if (currentPhotoObjectThumb == null) {
                                }
                                r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                            } else {
                                if (currentPhotoObjectThumb == null) {
                                }
                                r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                            }
                        } else if (r1.documentAttach == null) {
                            if (r1.currentPhotoObject == null) {
                            }
                            r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                        } else {
                            if (r1.currentPhotoObject == null) {
                            }
                            r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                        }
                        r1.drawLinkImageView = true;
                        if (r1.mediaWebpage) {
                            viewWidth2 = 0;
                            viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                            viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                            viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                            dp = AndroidUtilities.dp(52.0f);
                            if (LocaleController.isRTL) {
                                f2 = 8.0f;
                                x = AndroidUtilities.dp(8.0f);
                            } else {
                                f2 = 8.0f;
                                x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                            }
                            r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                            r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                            r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                        } else {
                            viewWidth2 = viewWidth;
                            dp = MeasureSpec.getSize(heightMeasureSpec);
                            if (dp == 0) {
                                dp = AndroidUtilities.dp(100.0f);
                            }
                            setMeasuredDimension(viewWidth2, dp);
                            x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                            y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                            r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                            r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                        }
                    }
                }
                viewWidth = viewWidth2;
                w = 0;
                h = 0;
                if (r1.documentAttach != null) {
                    viewWidth2 = 0;
                    while (viewWidth2 < r1.documentAttach.attributes.size()) {
                        attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                        if (attribute instanceof TL_documentAttributeImageSize) {
                            if (!(attribute instanceof TL_documentAttributeVideo)) {
                                viewWidth2++;
                            }
                        }
                        w = attribute.w;
                        h = attribute.h;
                    }
                }
                if (r1.currentPhotoObject == null) {
                    if (currentPhotoObjectThumb != null) {
                        currentPhotoObjectThumb.size = -1;
                    }
                    w = r1.currentPhotoObject.w;
                    h = r1.currentPhotoObject.h;
                } else if (r1.inlineResult != null) {
                    result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                    w = result[0];
                    h = result[1];
                }
                dp = AndroidUtilities.dp(80.0f);
                h = dp;
                w = dp;
                currentPhotoFilterThumb = "52_52_b";
                if (r1.mediaWebpage) {
                    viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                    if (r1.documentAttachType != 2) {
                        format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                        currentPhotoFilterThumb = format;
                        currentPhotoFilter = format;
                    } else {
                        format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(format);
                        stringBuilder.append("_b");
                        currentPhotoFilterThumb = stringBuilder.toString();
                        currentPhotoFilter = format;
                    }
                } else {
                    currentPhotoFilter = "52_52";
                }
                if (r1.documentAttachType != 6) {
                }
                r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                if (r1.documentAttachType != 2) {
                    if (r1.documentAttach == null) {
                        if (r1.currentPhotoObject == null) {
                        }
                        r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                    } else {
                        if (r1.currentPhotoObject == null) {
                        }
                        r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                    }
                } else if (r1.currentPhotoObject == null) {
                    if (currentPhotoObjectThumb == null) {
                    }
                    r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                } else {
                    if (currentPhotoObjectThumb == null) {
                    }
                    r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                }
                r1.drawLinkImageView = true;
                if (r1.mediaWebpage) {
                    viewWidth2 = viewWidth;
                    dp = MeasureSpec.getSize(heightMeasureSpec);
                    if (dp == 0) {
                        dp = AndroidUtilities.dp(100.0f);
                    }
                    setMeasuredDimension(viewWidth2, dp);
                    x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                    y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                    r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                    r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                } else {
                    viewWidth2 = 0;
                    viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                    viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                    viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                    setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                    dp = AndroidUtilities.dp(52.0f);
                    if (LocaleController.isRTL) {
                        f2 = 8.0f;
                        x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                    } else {
                        f2 = 8.0f;
                        x = AndroidUtilities.dp(8.0f);
                    }
                    r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                    r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                    r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                }
            }
        }
        photoThumbs = photoThumbs3;
        ext = null;
        if (r1.documentAttach != null) {
            r1.currentPhotoObject = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, AndroidUtilities.getPhotoSize(), z);
            currentPhotoObjectThumb = FileLoader.getClosestPhotoSizeWithSize(photoThumbs, 80);
            if (currentPhotoObjectThumb == r1.currentPhotoObject) {
                currentPhotoObjectThumb = null;
            }
        } else if (!MessageObject.isGifDocument(r1.documentAttach)) {
            r1.currentPhotoObject = r1.documentAttach.thumb;
        } else if (MessageObject.isStickerDocument(r1.documentAttach)) {
            r1.currentPhotoObject = r1.documentAttach.thumb;
        } else {
            r1.currentPhotoObject = r1.documentAttach.thumb;
            ext = "webp";
        }
        if (r1.inlineResult != null) {
            if (r1.inlineResult.type.startsWith("gif")) {
                if (r1.inlineResult.type.equals("photo")) {
                    if (r1.inlineResult.thumb instanceof TL_webDocument) {
                    }
                }
            } else if (r1.documentAttachType != 2) {
                tLObject = (TL_webDocument) r1.inlineResult.content;
                r1.documentAttachType = 2;
            }
            tLObject = r1.inlineResult.thumb;
            if (r1.inlineResult.send_message instanceof TL_botInlineMessageMediaVenue) {
                if (!(r1.inlineResult.send_message instanceof TL_botInlineMessageMediaGeo)) {
                    viewWidth = viewWidth2;
                    w = 0;
                    h = 0;
                    if (r1.documentAttach != null) {
                        viewWidth2 = 0;
                        while (viewWidth2 < r1.documentAttach.attributes.size()) {
                            attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                            if (attribute instanceof TL_documentAttributeImageSize) {
                                if (!(attribute instanceof TL_documentAttributeVideo)) {
                                    viewWidth2++;
                                }
                            }
                            w = attribute.w;
                            h = attribute.h;
                        }
                    }
                    if (r1.currentPhotoObject == null) {
                        if (currentPhotoObjectThumb != null) {
                            currentPhotoObjectThumb.size = -1;
                        }
                        w = r1.currentPhotoObject.w;
                        h = r1.currentPhotoObject.h;
                    } else if (r1.inlineResult != null) {
                        result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                        w = result[0];
                        h = result[1];
                    }
                    dp = AndroidUtilities.dp(80.0f);
                    h = dp;
                    w = dp;
                    currentPhotoFilterThumb = "52_52_b";
                    if (r1.mediaWebpage) {
                        currentPhotoFilter = "52_52";
                    } else {
                        viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                        if (r1.documentAttachType != 2) {
                            format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(format);
                            stringBuilder.append("_b");
                            currentPhotoFilterThumb = stringBuilder.toString();
                            currentPhotoFilter = format;
                        } else {
                            format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                            currentPhotoFilterThumb = format;
                            currentPhotoFilter = format;
                        }
                    }
                    if (r1.documentAttachType != 6) {
                    }
                    r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
                    if (r1.documentAttachType != 2) {
                        if (r1.currentPhotoObject == null) {
                            if (currentPhotoObjectThumb == null) {
                            }
                            r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
                        } else {
                            if (currentPhotoObjectThumb == null) {
                            }
                            r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
                        }
                    } else if (r1.documentAttach == null) {
                        if (r1.currentPhotoObject == null) {
                        }
                        r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                    } else {
                        if (r1.currentPhotoObject == null) {
                        }
                        r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                    }
                    r1.drawLinkImageView = true;
                    if (r1.mediaWebpage) {
                        viewWidth2 = 0;
                        viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                        viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                        viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                        dp = AndroidUtilities.dp(52.0f);
                        if (LocaleController.isRTL) {
                            f2 = 8.0f;
                            x = AndroidUtilities.dp(8.0f);
                        } else {
                            f2 = 8.0f;
                            x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                        }
                        r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                        r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                        r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
                    } else {
                        viewWidth2 = viewWidth;
                        dp = MeasureSpec.getSize(heightMeasureSpec);
                        if (dp == 0) {
                            dp = AndroidUtilities.dp(100.0f);
                        }
                        setMeasuredDimension(viewWidth2, dp);
                        x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                        y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                        r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                        r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
                    }
                }
            }
            lat = r1.inlineResult.send_message.geo.lat;
            viewWidth = viewWidth2;
            objArr = new Object[5];
            lon = r1.inlineResult.send_message.geo._long;
            objArr[2] = Integer.valueOf(Math.min(2, (int) Math.ceil((double) AndroidUtilities.density)));
            objArr[3] = Double.valueOf(lat);
            objArr[4] = Double.valueOf(lon);
            urlLocation = String.format(Locale.US, "https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=15&size=72x72&maptype=roadmap&scale=%d&markers=color:red|size:small|%f,%f&sensor=false", objArr);
            w = 0;
            h = 0;
            if (r1.documentAttach != null) {
                viewWidth2 = 0;
                while (viewWidth2 < r1.documentAttach.attributes.size()) {
                    attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                    if (attribute instanceof TL_documentAttributeImageSize) {
                        if (!(attribute instanceof TL_documentAttributeVideo)) {
                            viewWidth2++;
                        }
                    }
                    w = attribute.w;
                    h = attribute.h;
                }
            }
            if (r1.currentPhotoObject == null) {
                if (currentPhotoObjectThumb != null) {
                    currentPhotoObjectThumb.size = -1;
                }
                w = r1.currentPhotoObject.w;
                h = r1.currentPhotoObject.h;
            } else if (r1.inlineResult != null) {
                result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
                w = result[0];
                h = result[1];
            }
            dp = AndroidUtilities.dp(80.0f);
            h = dp;
            w = dp;
            currentPhotoFilterThumb = "52_52_b";
            if (r1.mediaWebpage) {
                viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
                if (r1.documentAttachType != 2) {
                    format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                    currentPhotoFilterThumb = format;
                    currentPhotoFilter = format;
                } else {
                    format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(format);
                    stringBuilder.append("_b");
                    currentPhotoFilterThumb = stringBuilder.toString();
                    currentPhotoFilter = format;
                }
            } else {
                currentPhotoFilter = "52_52";
            }
            if (r1.documentAttachType != 6) {
            }
            r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
            if (r1.documentAttachType != 2) {
                if (r1.documentAttach == null) {
                    if (r1.currentPhotoObject == null) {
                    }
                    r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
                } else {
                    if (r1.currentPhotoObject == null) {
                    }
                    r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
                }
            } else if (r1.currentPhotoObject == null) {
                if (currentPhotoObjectThumb == null) {
                }
                r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
            } else {
                if (currentPhotoObjectThumb == null) {
                }
                r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
            }
            r1.drawLinkImageView = true;
            if (r1.mediaWebpage) {
                viewWidth2 = viewWidth;
                dp = MeasureSpec.getSize(heightMeasureSpec);
                if (dp == 0) {
                    dp = AndroidUtilities.dp(100.0f);
                }
                setMeasuredDimension(viewWidth2, dp);
                x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
                y = (dp - AndroidUtilities.dp(24.0f)) / 2;
                r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
                r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
            } else {
                viewWidth2 = 0;
                viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
                viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
                viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
                setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
                dp = AndroidUtilities.dp(52.0f);
                if (LocaleController.isRTL) {
                    f2 = 8.0f;
                    x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
                } else {
                    f2 = 8.0f;
                    x = AndroidUtilities.dp(8.0f);
                }
                r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
                r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
                r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
            }
        }
        viewWidth = viewWidth2;
        w = 0;
        h = 0;
        if (r1.documentAttach != null) {
            viewWidth2 = 0;
            while (viewWidth2 < r1.documentAttach.attributes.size()) {
                attribute = (DocumentAttribute) r1.documentAttach.attributes.get(viewWidth2);
                if (attribute instanceof TL_documentAttributeImageSize) {
                    if (!(attribute instanceof TL_documentAttributeVideo)) {
                        viewWidth2++;
                    }
                }
                w = attribute.w;
                h = attribute.h;
            }
        }
        if (r1.currentPhotoObject == null) {
            if (currentPhotoObjectThumb != null) {
                currentPhotoObjectThumb.size = -1;
            }
            w = r1.currentPhotoObject.w;
            h = r1.currentPhotoObject.h;
        } else if (r1.inlineResult != null) {
            result = MessageObject.getInlineResultWidthAndHeight(r1.inlineResult);
            w = result[0];
            h = result[1];
        }
        dp = AndroidUtilities.dp(80.0f);
        h = dp;
        w = dp;
        currentPhotoFilterThumb = "52_52_b";
        if (r1.mediaWebpage) {
            currentPhotoFilter = "52_52";
        } else {
            viewWidth2 = (int) (((float) w) / (((float) h) / ((float) AndroidUtilities.dp(80.0f))));
            if (r1.documentAttachType != 2) {
                format = String.format(Locale.US, "%d_%d", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                stringBuilder = new StringBuilder();
                stringBuilder.append(format);
                stringBuilder.append("_b");
                currentPhotoFilterThumb = stringBuilder.toString();
                currentPhotoFilter = format;
            } else {
                format = String.format(Locale.US, "%d_%d_b", new Object[]{Integer.valueOf((int) (((float) viewWidth2) / AndroidUtilities.density)), Integer.valueOf(80)});
                currentPhotoFilterThumb = format;
                currentPhotoFilter = format;
            }
        }
        if (r1.documentAttachType != 6) {
        }
        r1.linkImageView.setAspectFit(r1.documentAttachType != 6);
        if (r1.documentAttachType != 2) {
            if (r1.currentPhotoObject == null) {
                if (currentPhotoObjectThumb == null) {
                }
                r1.linkImageView.setImage(tLObject, urlLocation, currentPhotoFilter, null, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, -1, ext, 1);
            } else {
                if (currentPhotoObjectThumb == null) {
                }
                r1.linkImageView.setImage(r1.currentPhotoObject.location, currentPhotoFilter, currentPhotoObjectThumb == null ? currentPhotoObjectThumb.location : null, currentPhotoFilterThumb, r1.currentPhotoObject.size, ext, 0);
            }
        } else if (r1.documentAttach == null) {
            if (r1.currentPhotoObject == null) {
            }
            r1.linkImageView.setImage(tLObject, urlLocation, null, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, -1, ext, 1);
        } else {
            if (r1.currentPhotoObject == null) {
            }
            r1.linkImageView.setImage(r1.documentAttach, null, r1.currentPhotoObject == null ? r1.currentPhotoObject.location : null, currentPhotoFilter, r1.documentAttach.size, ext, 0);
        }
        r1.drawLinkImageView = true;
        if (r1.mediaWebpage) {
            viewWidth2 = 0;
            viewWidth2 = 0 + r1.titleLayout.getLineBottom(r1.titleLayout.getLineCount() - 1);
            viewWidth2 += r1.descriptionLayout.getLineBottom(r1.descriptionLayout.getLineCount() - 1);
            viewWidth2 += r1.linkLayout.getLineBottom(r1.linkLayout.getLineCount() - 1);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(AndroidUtilities.dp(68.0f), AndroidUtilities.dp(16.0f) + Math.max(AndroidUtilities.dp(52.0f), viewWidth2)) + r1.needDivider);
            dp = AndroidUtilities.dp(52.0f);
            if (LocaleController.isRTL) {
                f2 = 8.0f;
                x = AndroidUtilities.dp(8.0f);
            } else {
                f2 = 8.0f;
                x = (MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp(8.0f)) - dp;
            }
            r1.letterDrawable.setBounds(x, AndroidUtilities.dp(f2), x + dp, AndroidUtilities.dp(60.0f));
            r1.linkImageView.setImageCoords(x, AndroidUtilities.dp(f2), dp, dp);
            r1.radialProgress.setProgressRect(AndroidUtilities.dp(4.0f) + x, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(48.0f) + x, AndroidUtilities.dp(56.0f));
        } else {
            viewWidth2 = viewWidth;
            dp = MeasureSpec.getSize(heightMeasureSpec);
            if (dp == 0) {
                dp = AndroidUtilities.dp(100.0f);
            }
            setMeasuredDimension(viewWidth2, dp);
            x2 = (viewWidth2 - AndroidUtilities.dp(24.0f)) / 2;
            y = (dp - AndroidUtilities.dp(24.0f)) / 2;
            r1.radialProgress.setProgressRect(x2, y, AndroidUtilities.dp(24.0f) + x2, AndroidUtilities.dp(24.0f) + y);
            r1.linkImageView.setImageCoords(0, 0, viewWidth2, dp);
        }
    }

    private void setAttachType() {
        this.currentMessageObject = null;
        this.documentAttachType = 0;
        if (this.documentAttach != null) {
            if (MessageObject.isGifDocument(this.documentAttach)) {
                this.documentAttachType = 2;
            } else if (MessageObject.isStickerDocument(this.documentAttach)) {
                this.documentAttachType = 6;
            } else if (MessageObject.isMusicDocument(this.documentAttach)) {
                this.documentAttachType = 5;
            } else if (MessageObject.isVoiceDocument(this.documentAttach)) {
                this.documentAttachType = 3;
            }
        } else if (this.inlineResult != null) {
            if (this.inlineResult.photo != null) {
                this.documentAttachType = 7;
            } else if (this.inlineResult.type.equals(MimeTypes.BASE_TYPE_AUDIO)) {
                this.documentAttachType = 5;
            } else if (this.inlineResult.type.equals("voice")) {
                this.documentAttachType = 3;
            }
        }
        if (this.documentAttachType == 3 || this.documentAttachType == 5) {
            TL_message message = new TL_message();
            message.out = true;
            message.id = -Utilities.random.nextInt();
            message.to_id = new TL_peerUser();
            Peer peer = message.to_id;
            int clientUserId = UserConfig.getInstance(this.currentAccount).getClientUserId();
            message.from_id = clientUserId;
            peer.user_id = clientUserId;
            message.date = (int) (System.currentTimeMillis() / 1000);
            message.message = TtmlNode.ANONYMOUS_REGION_ID;
            message.media = new TL_messageMediaDocument();
            MessageMedia messageMedia = message.media;
            messageMedia.flags |= 3;
            message.media.document = new TL_document();
            message.flags |= 768;
            if (this.documentAttach != null) {
                message.media.document = this.documentAttach;
                message.attachPath = TtmlNode.ANONYMOUS_REGION_ID;
            } else {
                String ext = ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? "mp3" : "ogg");
                message.media.document.id = 0;
                message.media.document.access_hash = 0;
                message.media.document.date = message.date;
                Document document = message.media.document;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("audio/");
                stringBuilder.append(ext);
                document.mime_type = stringBuilder.toString();
                message.media.document.size = 0;
                message.media.document.thumb = new TL_photoSizeEmpty();
                message.media.document.thumb.type = "s";
                message.media.document.dc_id = 0;
                TL_documentAttributeAudio attributeAudio = new TL_documentAttributeAudio();
                attributeAudio.duration = MessageObject.getInlineResultDuration(this.inlineResult);
                attributeAudio.title = this.inlineResult.title != null ? this.inlineResult.title : TtmlNode.ANONYMOUS_REGION_ID;
                attributeAudio.performer = this.inlineResult.description != null ? this.inlineResult.description : TtmlNode.ANONYMOUS_REGION_ID;
                attributeAudio.flags |= 3;
                if (this.documentAttachType == 3) {
                    attributeAudio.voice = true;
                }
                message.media.document.attributes.add(attributeAudio);
                TL_documentAttributeFilename fileName = new TL_documentAttributeFilename();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(Utilities.MD5(this.inlineResult.content.url));
                stringBuilder2.append(".");
                stringBuilder2.append(ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? "mp3" : "ogg"));
                fileName.file_name = stringBuilder2.toString();
                message.media.document.attributes.add(fileName);
                File directory = FileLoader.getDirectory(4);
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(Utilities.MD5(this.inlineResult.content.url));
                stringBuilder3.append(".");
                stringBuilder3.append(ImageLoader.getHttpUrlExtension(this.inlineResult.content.url, this.documentAttachType == 5 ? "mp3" : "ogg"));
                message.attachPath = new File(directory, stringBuilder3.toString()).getAbsolutePath();
            }
            this.currentMessageObject = new MessageObject(this.currentAccount, message, false);
        }
    }

    public void setLink(BotInlineResult contextResult, boolean media, boolean divider, boolean shadow) {
        this.needDivider = divider;
        this.needShadow = shadow;
        this.inlineResult = contextResult;
        if (this.inlineResult == null || this.inlineResult.document == null) {
            this.documentAttach = null;
        } else {
            this.documentAttach = this.inlineResult.document;
        }
        this.mediaWebpage = media;
        setAttachType();
        requestLayout();
        updateButtonState(false);
    }

    public void setGif(Document document, boolean divider) {
        this.needDivider = divider;
        this.needShadow = false;
        this.inlineResult = null;
        this.documentAttach = document;
        this.mediaWebpage = true;
        setAttachType();
        requestLayout();
        updateButtonState(false);
    }

    public boolean isSticker() {
        return this.documentAttachType == 6;
    }

    public boolean showingBitmap() {
        return this.linkImageView.getBitmap() != null;
    }

    public Document getDocument() {
        return this.documentAttach;
    }

    public ImageReceiver getPhotoImage() {
        return this.linkImageView;
    }

    public void setScaled(boolean value) {
        this.scaled = value;
        this.lastUpdateTime = System.currentTimeMillis();
        invalidate();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.drawLinkImageView) {
            this.linkImageView.onDetachedFromWindow();
        }
        DownloadController.getInstance(this.currentAccount).removeLoadingFileObserver(this);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.drawLinkImageView && this.linkImageView.onAttachedToWindow()) {
            updateButtonState(false);
        }
    }

    public MessageObject getMessageObject() {
        return this.currentMessageObject;
    }

    private void didPressedButton() {
        if (this.documentAttachType != 3 && this.documentAttachType != 5) {
            return;
        }
        if (this.buttonState == 0) {
            if (MediaController.getInstance().playMessage(this.currentMessageObject)) {
                this.buttonState = 1;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                invalidate();
            }
        } else if (this.buttonState == 1) {
            if (MediaController.getInstance().pauseMessage(this.currentMessageObject)) {
                this.buttonState = 0;
                this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
                invalidate();
            }
        } else if (this.buttonState == 2) {
            this.radialProgress.setProgress(0.0f, false);
            if (this.documentAttach != null) {
                FileLoader.getInstance(this.currentAccount).loadFile(this.documentAttach, true, 0);
            } else if (this.inlineResult.content instanceof TL_webDocument) {
                FileLoader.getInstance(this.currentAccount).loadFile((TL_webDocument) this.inlineResult.content, true, 1);
            }
            this.buttonState = 4;
            this.radialProgress.setBackground(getDrawableForCurrentState(), true, false);
            invalidate();
        } else if (this.buttonState == 4) {
            if (this.documentAttach != null) {
                FileLoader.getInstance(this.currentAccount).cancelLoadFile(this.documentAttach);
            } else if (this.inlineResult.content instanceof TL_webDocument) {
                FileLoader.getInstance(this.currentAccount).cancelLoadFile((TL_webDocument) this.inlineResult.content);
            }
            this.buttonState = 2;
            this.radialProgress.setBackground(getDrawableForCurrentState(), false, false);
            invalidate();
        }
    }

    protected void onDraw(Canvas canvas) {
        float f = 8.0f;
        if (this.titleLayout != null) {
            canvas.save();
            canvas.translate((float) AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : (float) AndroidUtilities.leftBaseline), (float) this.titleY);
            this.titleLayout.draw(canvas);
            canvas.restore();
        }
        if (this.descriptionLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText2));
            canvas.save();
            canvas.translate((float) AndroidUtilities.dp(LocaleController.isRTL ? 8.0f : (float) AndroidUtilities.leftBaseline), (float) this.descriptionY);
            this.descriptionLayout.draw(canvas);
            canvas.restore();
        }
        if (this.linkLayout != null) {
            Theme.chat_contextResult_descriptionTextPaint.setColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText));
            canvas.save();
            if (!LocaleController.isRTL) {
                f = (float) AndroidUtilities.leftBaseline;
            }
            canvas.translate((float) AndroidUtilities.dp(f), (float) this.linkY);
            this.linkLayout.draw(canvas);
            canvas.restore();
        }
        int w;
        int h;
        int x;
        if (!this.mediaWebpage) {
            if (this.documentAttachType != 3) {
                if (this.documentAttachType != 5) {
                    int y;
                    if (this.inlineResult != null && this.inlineResult.type.equals("file")) {
                        w = Theme.chat_inlineResultFile.getIntrinsicWidth();
                        h = Theme.chat_inlineResultFile.getIntrinsicHeight();
                        x = this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - w) / 2);
                        y = this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - h) / 2);
                        canvas.drawRect((float) this.linkImageView.getImageX(), (float) this.linkImageView.getImageY(), (float) (this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f)), (float) (this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f)), LetterDrawable.paint);
                        Theme.chat_inlineResultFile.setBounds(x, y, x + w, y + h);
                        Theme.chat_inlineResultFile.draw(canvas);
                    } else if (this.inlineResult != null && (this.inlineResult.type.equals(MimeTypes.BASE_TYPE_AUDIO) || this.inlineResult.type.equals("voice"))) {
                        w = Theme.chat_inlineResultAudio.getIntrinsicWidth();
                        h = Theme.chat_inlineResultAudio.getIntrinsicHeight();
                        x = this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - w) / 2);
                        y = this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - h) / 2);
                        canvas.drawRect((float) this.linkImageView.getImageX(), (float) this.linkImageView.getImageY(), (float) (this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f)), (float) (this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f)), LetterDrawable.paint);
                        Theme.chat_inlineResultAudio.setBounds(x, y, x + w, y + h);
                        Theme.chat_inlineResultAudio.draw(canvas);
                    } else if (this.inlineResult == null || !(this.inlineResult.type.equals("venue") || this.inlineResult.type.equals("geo"))) {
                        this.letterDrawable.draw(canvas);
                    } else {
                        w = Theme.chat_inlineResultLocation.getIntrinsicWidth();
                        h = Theme.chat_inlineResultLocation.getIntrinsicHeight();
                        x = this.linkImageView.getImageX() + ((AndroidUtilities.dp(52.0f) - w) / 2);
                        y = this.linkImageView.getImageY() + ((AndroidUtilities.dp(52.0f) - h) / 2);
                        canvas.drawRect((float) this.linkImageView.getImageX(), (float) this.linkImageView.getImageY(), (float) (this.linkImageView.getImageX() + AndroidUtilities.dp(52.0f)), (float) (this.linkImageView.getImageY() + AndroidUtilities.dp(52.0f)), LetterDrawable.paint);
                        Theme.chat_inlineResultLocation.setBounds(x, y, x + w, y + h);
                        Theme.chat_inlineResultLocation.draw(canvas);
                    }
                }
            }
            this.radialProgress.setProgressColor(Theme.getColor(this.buttonPressed ? Theme.key_chat_inAudioSelectedProgress : Theme.key_chat_inAudioProgress));
            this.radialProgress.draw(canvas);
        } else if (this.inlineResult != null && ((this.inlineResult.send_message instanceof TL_botInlineMessageMediaGeo) || (this.inlineResult.send_message instanceof TL_botInlineMessageMediaVenue))) {
            w = Theme.chat_inlineResultLocation.getIntrinsicWidth();
            int h2 = Theme.chat_inlineResultLocation.getIntrinsicHeight();
            h = this.linkImageView.getImageX() + ((this.linkImageView.getImageWidth() - w) / 2);
            x = this.linkImageView.getImageY() + ((this.linkImageView.getImageHeight() - h2) / 2);
            canvas.drawRect((float) this.linkImageView.getImageX(), (float) this.linkImageView.getImageY(), (float) (this.linkImageView.getImageX() + this.linkImageView.getImageWidth()), (float) (this.linkImageView.getImageY() + this.linkImageView.getImageHeight()), LetterDrawable.paint);
            Theme.chat_inlineResultLocation.setBounds(h, x, h + w, x + h2);
            Theme.chat_inlineResultLocation.draw(canvas);
        }
        if (this.drawLinkImageView) {
            if (this.inlineResult != null) {
                this.linkImageView.setVisible(PhotoViewer.isShowingImage(this.inlineResult) ^ 1, false);
            }
            canvas.save();
            if ((this.scaled && this.scale != 0.8f) || !(this.scaled || this.scale == 1.0f)) {
                long newTime = System.currentTimeMillis();
                long dt = newTime - this.lastUpdateTime;
                this.lastUpdateTime = newTime;
                if (!this.scaled || this.scale == 0.8f) {
                    this.scale += ((float) dt) / 400.0f;
                    if (this.scale > 1.0f) {
                        this.scale = 1.0f;
                    }
                } else {
                    this.scale -= ((float) dt) / 400.0f;
                    if (this.scale < 0.8f) {
                        this.scale = 0.8f;
                    }
                }
                invalidate();
            }
            canvas.scale(this.scale, this.scale, (float) (getMeasuredWidth() / 2), (float) (getMeasuredHeight() / 2));
            this.linkImageView.draw(canvas);
            canvas.restore();
        }
        if (this.mediaWebpage && (this.documentAttachType == 7 || this.documentAttachType == 2)) {
            this.radialProgress.draw(canvas);
        }
        if (this.needDivider && !this.mediaWebpage) {
            if (LocaleController.isRTL) {
                canvas.drawLine(0.0f, (float) (getMeasuredHeight() - 1), (float) (getMeasuredWidth() - AndroidUtilities.dp((float) AndroidUtilities.leftBaseline)), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
            } else {
                canvas.drawLine((float) AndroidUtilities.dp((float) AndroidUtilities.leftBaseline), (float) (getMeasuredHeight() - 1), (float) getMeasuredWidth(), (float) (getMeasuredHeight() - 1), Theme.dividerPaint);
            }
        }
        if (this.needShadow) {
            Theme.chat_contextResult_shadowUnderSwitchDrawable.setBounds(0, 0, getMeasuredWidth(), AndroidUtilities.dp(3.0f));
            Theme.chat_contextResult_shadowUnderSwitchDrawable.draw(canvas);
        }
    }

    private Drawable getDrawableForCurrentState() {
        Drawable drawable = null;
        if (this.documentAttachType != 3) {
            if (this.documentAttachType != 5) {
                if (this.buttonState == 1) {
                    drawable = Theme.chat_photoStatesDrawables[5][0];
                }
                return drawable;
            }
        }
        if (this.buttonState == -1) {
            return null;
        }
        this.radialProgress.setAlphaForPrevious(false);
        return Theme.chat_fileStatesDrawable[this.buttonState + 5][this.buttonPressed];
    }

    public void setDelegate(ContextLinkCellDelegate contextLinkCellDelegate) {
        this.delegate = contextLinkCellDelegate;
    }

    public BotInlineResult getResult() {
        return this.inlineResult;
    }

    public void onFailedDownload(String fileName) {
        updateButtonState(false);
    }

    public void onSuccessDownload(String fileName) {
        this.radialProgress.setProgress(1.0f, true);
        updateButtonState(true);
    }

    public void onProgressDownload(String fileName, float progress) {
        this.radialProgress.setProgress(progress, true);
        if (this.documentAttachType != 3) {
            if (this.documentAttachType != 5) {
                if (this.buttonState != 1) {
                    updateButtonState(false);
                    return;
                }
                return;
            }
        }
        if (this.buttonState != 4) {
            updateButtonState(false);
        }
    }

    public void onProgressUpload(String fileName, float progress, boolean isEncrypted) {
    }

    public int getObserverTag() {
        return this.TAG;
    }
}
