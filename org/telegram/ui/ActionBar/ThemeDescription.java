package org.telegram.ui.ActionBar;

import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.exoplayer2.C;
import org.telegram.messenger.exoplayer2.source.ExtractorMediaSource;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.ui.Components.AvatarDrawable;
import org.telegram.ui.Components.BackupImageView;
import org.telegram.ui.Components.ChatBigEmptyView;
import org.telegram.ui.Components.CombinedDrawable;
import org.telegram.ui.Components.ContextProgressView;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.EmptyTextProgressView;
import org.telegram.ui.Components.LineProgressView;
import org.telegram.ui.Components.NumberTextView;
import org.telegram.ui.Components.RadialProgressView;
import org.telegram.ui.Components.RecyclerListView;

public class ThemeDescription {
    public static int FLAG_AB_AM_BACKGROUND = ExtractorMediaSource.DEFAULT_LOADING_CHECK_INTERVAL_BYTES;
    public static int FLAG_AB_AM_ITEMSCOLOR = 512;
    public static int FLAG_AB_AM_SELECTORCOLOR = 4194304;
    public static int FLAG_AB_AM_TOPBACKGROUND = 2097152;
    public static int FLAG_AB_ITEMSCOLOR = 64;
    public static int FLAG_AB_SEARCH = 134217728;
    public static int FLAG_AB_SEARCHPLACEHOLDER = ConnectionsManager.FileTypeFile;
    public static int FLAG_AB_SELECTORCOLOR = 256;
    public static int FLAG_AB_SUBMENUBACKGROUND = Integer.MIN_VALUE;
    public static int FLAG_AB_SUBMENUITEM = 1073741824;
    public static int FLAG_AB_SUBTITLECOLOR = 1024;
    public static int FLAG_AB_TITLECOLOR = 128;
    public static int FLAG_BACKGROUND = 1;
    public static int FLAG_BACKGROUNDFILTER = 32;
    public static int FLAG_CELLBACKGROUNDCOLOR = 16;
    public static int FLAG_CHECKBOX = MessagesController.UPDATE_MASK_CHANNEL;
    public static int FLAG_CHECKBOXCHECK = MessagesController.UPDATE_MASK_CHAT_ADMINS;
    public static int FLAG_CHECKTAG = 262144;
    public static int FLAG_CURSORCOLOR = 16777216;
    public static int FLAG_DRAWABLESELECTEDSTATE = C.DEFAULT_BUFFER_SEGMENT_SIZE;
    public static int FLAG_FASTSCROLL = ConnectionsManager.FileTypeVideo;
    public static int FLAG_HINTTEXTCOLOR = 8388608;
    public static int FLAG_IMAGECOLOR = 8;
    public static int FLAG_LINKCOLOR = 2;
    public static int FLAG_LISTGLOWCOLOR = 32768;
    public static int FLAG_PROGRESSBAR = 2048;
    public static int FLAG_SECTIONS = 524288;
    public static int FLAG_SELECTOR = 4096;
    public static int FLAG_SELECTORWHITE = 268435456;
    public static int FLAG_SERVICEBACKGROUND = 536870912;
    public static int FLAG_TEXTCOLOR = 4;
    public static int FLAG_USEBACKGROUNDDRAWABLE = 131072;
    private HashMap<String, Field> cachedFields;
    private int changeFlags;
    private int currentColor;
    private String currentKey;
    private int defaultColor;
    private ThemeDescriptionDelegate delegate;
    private Drawable[] drawablesToUpdate;
    private Class[] listClasses;
    private String[] listClassesFieldName;
    private HashMap<String, Boolean> notFoundCachedFields;
    private Paint[] paintToUpdate;
    private int previousColor;
    private boolean[] previousIsDefault;
    private View viewToInvalidate;

    public interface ThemeDescriptionDelegate {
        void didSetColor();
    }

    private void processViewColor(android.view.View r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.ActionBar.ThemeDescription.processViewColor(android.view.View, int):void
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
        r1 = r0;
        r2 = r13.listClasses;
        r2 = r2.length;
        if (r1 >= r2) goto L_0x03b8;
    L_0x0007:
        r2 = r13.listClasses;
        r2 = r2[r1];
        r2 = r2.isInstance(r14);
        if (r2 == 0) goto L_0x03b4;
    L_0x0011:
        r14.invalidate();
        r2 = r13.changeFlags;
        r3 = FLAG_CHECKTAG;
        r2 = r2 & r3;
        if (r2 == 0) goto L_0x0032;
    L_0x001b:
        r2 = r13.changeFlags;
        r3 = FLAG_CHECKTAG;
        r2 = r2 & r3;
        if (r2 == 0) goto L_0x002f;
    L_0x0022:
        r2 = r13.currentKey;
        r3 = r14.getTag();
        r2 = r2.equals(r3);
        if (r2 == 0) goto L_0x002f;
    L_0x002e:
        goto L_0x0032;
    L_0x002f:
        r2 = r0;
        goto L_0x00a6;
    L_0x0032:
        r2 = 1;
        r14.invalidate();
        r3 = r13.changeFlags;
        r4 = FLAG_BACKGROUNDFILTER;
        r3 = r3 & r4;
        if (r3 == 0) goto L_0x0076;
        r3 = r14.getBackground();
        if (r3 == 0) goto L_0x0075;
        r4 = r13.changeFlags;
        r5 = FLAG_CELLBACKGROUNDCOLOR;
        r4 = r4 & r5;
        if (r4 == 0) goto L_0x0060;
        r4 = r3 instanceof org.telegram.ui.Components.CombinedDrawable;
        if (r4 == 0) goto L_0x0075;
        r4 = r3;
        r4 = (org.telegram.ui.Components.CombinedDrawable) r4;
        r4 = r4.getBackground();
        r5 = r4 instanceof android.graphics.drawable.ColorDrawable;
        if (r5 == 0) goto L_0x005f;
        r5 = r4;
        r5 = (android.graphics.drawable.ColorDrawable) r5;
        r5.setColor(r15);
        goto L_0x0075;
        r4 = r3 instanceof org.telegram.ui.Components.CombinedDrawable;
        if (r4 == 0) goto L_0x006b;
        r4 = r3;
        r4 = (org.telegram.ui.Components.CombinedDrawable) r4;
        r3 = r4.getIcon();
        r4 = new android.graphics.PorterDuffColorFilter;
        r5 = android.graphics.PorterDuff.Mode.MULTIPLY;
        r4.<init>(r15, r5);
        r3.setColorFilter(r4);
        goto L_0x00a6;
        r3 = r13.changeFlags;
        r4 = FLAG_CELLBACKGROUNDCOLOR;
        r3 = r3 & r4;
        if (r3 == 0) goto L_0x0081;
        r14.setBackgroundColor(r15);
        goto L_0x00a6;
        r3 = r13.changeFlags;
        r4 = FLAG_TEXTCOLOR;
        r3 = r3 & r4;
        if (r3 == 0) goto L_0x0093;
        r3 = r14 instanceof android.widget.TextView;
        if (r3 == 0) goto L_0x00a6;
        r3 = r14;
        r3 = (android.widget.TextView) r3;
        r3.setTextColor(r15);
        goto L_0x00a6;
        r3 = r13.changeFlags;
        r4 = FLAG_SERVICEBACKGROUND;
        r3 = r3 & r4;
        if (r3 == 0) goto L_0x00a6;
        r3 = r14.getBackground();
        if (r3 == 0) goto L_0x00a5;
        r4 = org.telegram.ui.ActionBar.Theme.colorFilter;
        r3.setColorFilter(r4);
        r3 = r13.listClassesFieldName;
        if (r3 == 0) goto L_0x03aa;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = r13.listClasses;
        r4 = r4[r1];
        r3.append(r4);
        r4 = "_";
        r3.append(r4);
        r4 = r13.listClassesFieldName;
        r4 = r4[r1];
        r3.append(r4);
        r3 = r3.toString();
        r4 = r13.notFoundCachedFields;
        if (r4 == 0) goto L_0x00d5;
        r4 = r13.notFoundCachedFields;
        r4 = r4.containsKey(r3);
        if (r4 == 0) goto L_0x00d5;
        goto L_0x03b4;
        r4 = 1;
        r5 = r13.cachedFields;	 Catch:{ Throwable -> 0x039c }
        r5 = r5.get(r3);	 Catch:{ Throwable -> 0x039c }
        r5 = (java.lang.reflect.Field) r5;	 Catch:{ Throwable -> 0x039c }
        if (r5 != 0) goto L_0x00f7;	 Catch:{ Throwable -> 0x039c }
        r6 = r13.listClasses;	 Catch:{ Throwable -> 0x039c }
        r6 = r6[r1];	 Catch:{ Throwable -> 0x039c }
        r7 = r13.listClassesFieldName;	 Catch:{ Throwable -> 0x039c }
        r7 = r7[r1];	 Catch:{ Throwable -> 0x039c }
        r6 = r6.getDeclaredField(r7);	 Catch:{ Throwable -> 0x039c }
        r5 = r6;	 Catch:{ Throwable -> 0x039c }
        if (r5 == 0) goto L_0x00f7;	 Catch:{ Throwable -> 0x039c }
        r5.setAccessible(r4);	 Catch:{ Throwable -> 0x039c }
        r6 = r13.cachedFields;	 Catch:{ Throwable -> 0x039c }
        r6.put(r3, r5);	 Catch:{ Throwable -> 0x039c }
        if (r5 == 0) goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r6 = r5.get(r14);	 Catch:{ Throwable -> 0x039c }
        if (r6 == 0) goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        if (r2 != 0) goto L_0x0116;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.view.View;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0116;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.currentKey;	 Catch:{ Throwable -> 0x039c }
        r8 = r6;	 Catch:{ Throwable -> 0x039c }
        r8 = (android.view.View) r8;	 Catch:{ Throwable -> 0x039c }
        r8 = r8.getTag();	 Catch:{ Throwable -> 0x039c }
        r7 = r7.equals(r8);	 Catch:{ Throwable -> 0x039c }
        if (r7 != 0) goto L_0x0116;	 Catch:{ Throwable -> 0x039c }
        goto L_0x03b4;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.view.View;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0120;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.view.View) r7;	 Catch:{ Throwable -> 0x039c }
        r7.invalidate();	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_USEBACKGROUNDDRAWABLE;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0133;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.view.View;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0133;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.view.View) r7;	 Catch:{ Throwable -> 0x039c }
        r7 = r7.getBackground();	 Catch:{ Throwable -> 0x039c }
        r6 = r7;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_BACKGROUND;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0146;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.view.View;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0146;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.view.View) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setBackgroundColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.Switch;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0152;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.Switch) r7;	 Catch:{ Throwable -> 0x039c }
        r7.checkColorFilters();	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.EditTextCaption;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0173;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_HINTTEXTCOLOR;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x016b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.EditTextCaption) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setHintColor(r15);	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.EditTextCaption) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setHintTextColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.EditTextCaption) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setTextColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.ActionBar.SimpleTextView;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x018e;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_LINKCOLOR;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0186;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.ActionBar.SimpleTextView) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setLinkTextColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.ActionBar.SimpleTextView) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setTextColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.widget.TextView;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0200;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.widget.TextView) r7;	 Catch:{ Throwable -> 0x039c }
        r8 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r9 = FLAG_IMAGECOLOR;	 Catch:{ Throwable -> 0x039c }
        r8 = r8 & r9;	 Catch:{ Throwable -> 0x039c }
        if (r8 == 0) goto L_0x01ba;	 Catch:{ Throwable -> 0x039c }
        r8 = r7.getCompoundDrawables();	 Catch:{ Throwable -> 0x039c }
        if (r8 == 0) goto L_0x01b9;	 Catch:{ Throwable -> 0x039c }
        r9 = r0;	 Catch:{ Throwable -> 0x039c }
        r10 = r8.length;	 Catch:{ Throwable -> 0x039c }
        if (r9 >= r10) goto L_0x01b9;	 Catch:{ Throwable -> 0x039c }
        r10 = r8[r9];	 Catch:{ Throwable -> 0x039c }
        if (r10 == 0) goto L_0x01b6;	 Catch:{ Throwable -> 0x039c }
        r10 = r8[r9];	 Catch:{ Throwable -> 0x039c }
        r11 = new android.graphics.PorterDuffColorFilter;	 Catch:{ Throwable -> 0x039c }
        r12 = android.graphics.PorterDuff.Mode.MULTIPLY;	 Catch:{ Throwable -> 0x039c }
        r11.<init>(r15, r12);	 Catch:{ Throwable -> 0x039c }
        r10.setColorFilter(r11);	 Catch:{ Throwable -> 0x039c }
        r9 = r9 + 1;	 Catch:{ Throwable -> 0x039c }
        goto L_0x01a3;	 Catch:{ Throwable -> 0x039c }
        goto L_0x01fe;	 Catch:{ Throwable -> 0x039c }
        r8 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r9 = FLAG_LINKCOLOR;	 Catch:{ Throwable -> 0x039c }
        r8 = r8 & r9;	 Catch:{ Throwable -> 0x039c }
        if (r8 == 0) goto L_0x01cb;	 Catch:{ Throwable -> 0x039c }
        r8 = r7.getPaint();	 Catch:{ Throwable -> 0x039c }
        r8.linkColor = r15;	 Catch:{ Throwable -> 0x039c }
        r7.invalidate();	 Catch:{ Throwable -> 0x039c }
        goto L_0x01fe;	 Catch:{ Throwable -> 0x039c }
        r8 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r9 = FLAG_FASTSCROLL;	 Catch:{ Throwable -> 0x039c }
        r8 = r8 & r9;	 Catch:{ Throwable -> 0x039c }
        if (r8 == 0) goto L_0x01fb;	 Catch:{ Throwable -> 0x039c }
        r8 = r7.getText();	 Catch:{ Throwable -> 0x039c }
        r9 = r8 instanceof android.text.SpannedString;	 Catch:{ Throwable -> 0x039c }
        if (r9 == 0) goto L_0x01fa;	 Catch:{ Throwable -> 0x039c }
        r9 = r8;	 Catch:{ Throwable -> 0x039c }
        r9 = (android.text.SpannedString) r9;	 Catch:{ Throwable -> 0x039c }
        r10 = r8.length();	 Catch:{ Throwable -> 0x039c }
        r11 = org.telegram.ui.Components.TypefaceSpan.class;	 Catch:{ Throwable -> 0x039c }
        r9 = r9.getSpans(r0, r10, r11);	 Catch:{ Throwable -> 0x039c }
        r9 = (org.telegram.ui.Components.TypefaceSpan[]) r9;	 Catch:{ Throwable -> 0x039c }
        if (r9 == 0) goto L_0x01fa;	 Catch:{ Throwable -> 0x039c }
        r10 = r9.length;	 Catch:{ Throwable -> 0x039c }
        if (r10 <= 0) goto L_0x01fa;	 Catch:{ Throwable -> 0x039c }
        r10 = r0;	 Catch:{ Throwable -> 0x039c }
        r11 = r9.length;	 Catch:{ Throwable -> 0x039c }
        if (r10 >= r11) goto L_0x01fa;	 Catch:{ Throwable -> 0x039c }
        r11 = r9[r10];	 Catch:{ Throwable -> 0x039c }
        r11.setColor(r15);	 Catch:{ Throwable -> 0x039c }
        r10 = r10 + 1;	 Catch:{ Throwable -> 0x039c }
        goto L_0x01ef;	 Catch:{ Throwable -> 0x039c }
        goto L_0x01fe;	 Catch:{ Throwable -> 0x039c }
        r7.setTextColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.widget.ImageView;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0213;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.widget.ImageView) r7;	 Catch:{ Throwable -> 0x039c }
        r8 = new android.graphics.PorterDuffColorFilter;	 Catch:{ Throwable -> 0x039c }
        r9 = android.graphics.PorterDuff.Mode.MULTIPLY;	 Catch:{ Throwable -> 0x039c }
        r8.<init>(r15, r9);	 Catch:{ Throwable -> 0x039c }
        r7.setColorFilter(r8);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.BackupImageView;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x025f;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.BackupImageView) r7;	 Catch:{ Throwable -> 0x039c }
        r7 = r7.getImageReceiver();	 Catch:{ Throwable -> 0x039c }
        r7 = r7.getStaticThumb();	 Catch:{ Throwable -> 0x039c }
        r8 = r7 instanceof org.telegram.ui.Components.CombinedDrawable;	 Catch:{ Throwable -> 0x039c }
        if (r8 == 0) goto L_0x0251;	 Catch:{ Throwable -> 0x039c }
        r8 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r9 = FLAG_BACKGROUNDFILTER;	 Catch:{ Throwable -> 0x039c }
        r8 = r8 & r9;	 Catch:{ Throwable -> 0x039c }
        if (r8 == 0) goto L_0x023f;	 Catch:{ Throwable -> 0x039c }
        r8 = r7;	 Catch:{ Throwable -> 0x039c }
        r8 = (org.telegram.ui.Components.CombinedDrawable) r8;	 Catch:{ Throwable -> 0x039c }
        r8 = r8.getBackground();	 Catch:{ Throwable -> 0x039c }
        r9 = new android.graphics.PorterDuffColorFilter;	 Catch:{ Throwable -> 0x039c }
        r10 = android.graphics.PorterDuff.Mode.MULTIPLY;	 Catch:{ Throwable -> 0x039c }
        r9.<init>(r15, r10);	 Catch:{ Throwable -> 0x039c }
        r8.setColorFilter(r9);	 Catch:{ Throwable -> 0x039c }
        goto L_0x025d;	 Catch:{ Throwable -> 0x039c }
        r8 = r7;	 Catch:{ Throwable -> 0x039c }
        r8 = (org.telegram.ui.Components.CombinedDrawable) r8;	 Catch:{ Throwable -> 0x039c }
        r8 = r8.getIcon();	 Catch:{ Throwable -> 0x039c }
        r9 = new android.graphics.PorterDuffColorFilter;	 Catch:{ Throwable -> 0x039c }
        r10 = android.graphics.PorterDuff.Mode.MULTIPLY;	 Catch:{ Throwable -> 0x039c }
        r9.<init>(r15, r10);	 Catch:{ Throwable -> 0x039c }
        r8.setColorFilter(r9);	 Catch:{ Throwable -> 0x039c }
        goto L_0x025d;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x025d;	 Catch:{ Throwable -> 0x039c }
        r8 = new android.graphics.PorterDuffColorFilter;	 Catch:{ Throwable -> 0x039c }
        r9 = android.graphics.PorterDuff.Mode.MULTIPLY;	 Catch:{ Throwable -> 0x039c }
        r8.<init>(r15, r9);	 Catch:{ Throwable -> 0x039c }
        r7.setColorFilter(r8);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.graphics.drawable.Drawable;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x02df;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.LetterDrawable;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x027e;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_BACKGROUNDFILTER;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0276;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.LetterDrawable) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setBackgroundColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.LetterDrawable) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.CombinedDrawable;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x02af;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_BACKGROUNDFILTER;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x029c;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.CombinedDrawable) r7;	 Catch:{ Throwable -> 0x039c }
        r7 = r7.getBackground();	 Catch:{ Throwable -> 0x039c }
        r8 = new android.graphics.PorterDuffColorFilter;	 Catch:{ Throwable -> 0x039c }
        r9 = android.graphics.PorterDuff.Mode.MULTIPLY;	 Catch:{ Throwable -> 0x039c }
        r8.<init>(r15, r9);	 Catch:{ Throwable -> 0x039c }
        r7.setColorFilter(r8);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.CombinedDrawable) r7;	 Catch:{ Throwable -> 0x039c }
        r7 = r7.getIcon();	 Catch:{ Throwable -> 0x039c }
        r8 = new android.graphics.PorterDuffColorFilter;	 Catch:{ Throwable -> 0x039c }
        r9 = android.graphics.PorterDuff.Mode.MULTIPLY;	 Catch:{ Throwable -> 0x039c }
        r8.<init>(r15, r9);	 Catch:{ Throwable -> 0x039c }
        r7.setColorFilter(r8);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.graphics.drawable.StateListDrawable;	 Catch:{ Throwable -> 0x039c }
        if (r7 != 0) goto L_0x02cd;	 Catch:{ Throwable -> 0x039c }
        r7 = android.os.Build.VERSION.SDK_INT;	 Catch:{ Throwable -> 0x039c }
        r8 = 21;	 Catch:{ Throwable -> 0x039c }
        if (r7 < r8) goto L_0x02be;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.graphics.drawable.RippleDrawable;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x02be;	 Catch:{ Throwable -> 0x039c }
        goto L_0x02cd;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.graphics.drawable.Drawable) r7;	 Catch:{ Throwable -> 0x039c }
        r8 = new android.graphics.PorterDuffColorFilter;	 Catch:{ Throwable -> 0x039c }
        r9 = android.graphics.PorterDuff.Mode.MULTIPLY;	 Catch:{ Throwable -> 0x039c }
        r8.<init>(r15, r9);	 Catch:{ Throwable -> 0x039c }
        r7.setColorFilter(r8);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.graphics.drawable.Drawable) r7;	 Catch:{ Throwable -> 0x039c }
        r8 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r9 = FLAG_DRAWABLESELECTEDSTATE;	 Catch:{ Throwable -> 0x039c }
        r8 = r8 & r9;	 Catch:{ Throwable -> 0x039c }
        if (r8 == 0) goto L_0x02d9;	 Catch:{ Throwable -> 0x039c }
        r8 = r4;	 Catch:{ Throwable -> 0x039c }
        goto L_0x02da;	 Catch:{ Throwable -> 0x039c }
        r8 = r0;	 Catch:{ Throwable -> 0x039c }
        org.telegram.ui.ActionBar.Theme.setSelectorDrawableColor(r7, r15, r8);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.CheckBox;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0301;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_CHECKBOX;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x02f2;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.CheckBox) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setBackgroundColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_CHECKBOXCHECK;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.CheckBox) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setCheckColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.GroupCreateCheckBox;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x030d;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.GroupCreateCheckBox) r7;	 Catch:{ Throwable -> 0x039c }
        r7.updateColors();	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof java.lang.Integer;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x031a;	 Catch:{ Throwable -> 0x039c }
        r7 = java.lang.Integer.valueOf(r15);	 Catch:{ Throwable -> 0x039c }
        r5.set(r14, r7);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.RadioButton;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0347;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_CHECKBOX;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0333;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.RadioButton) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setBackgroundColor(r15);	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.RadioButton) r7;	 Catch:{ Throwable -> 0x039c }
        r7.invalidate();	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_CHECKBOXCHECK;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.RadioButton) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setCheckedColor(r15);	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.RadioButton) r7;	 Catch:{ Throwable -> 0x039c }
        r7.invalidate();	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.text.TextPaint;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x035f;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_LINKCOLOR;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0358;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.text.TextPaint) r7;	 Catch:{ Throwable -> 0x039c }
        r7.linkColor = r15;	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.text.TextPaint) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.LineProgressView;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0378;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_PROGRESSBAR;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0371;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.LineProgressView) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setProgressColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.LineProgressView) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setBackColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof android.graphics.Paint;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0383;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (android.graphics.Paint) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6 instanceof org.telegram.ui.Components.SeekBarView;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r13.changeFlags;	 Catch:{ Throwable -> 0x039c }
        r8 = FLAG_PROGRESSBAR;	 Catch:{ Throwable -> 0x039c }
        r7 = r7 & r8;	 Catch:{ Throwable -> 0x039c }
        if (r7 == 0) goto L_0x0395;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.SeekBarView) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setOuterColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x039b;	 Catch:{ Throwable -> 0x039c }
        r7 = r6;	 Catch:{ Throwable -> 0x039c }
        r7 = (org.telegram.ui.Components.SeekBarView) r7;	 Catch:{ Throwable -> 0x039c }
        r7.setInnerColor(r15);	 Catch:{ Throwable -> 0x039c }
        goto L_0x03a9;
    L_0x039c:
        r5 = move-exception;
        org.telegram.messenger.FileLog.e(r5);
        r6 = r13.notFoundCachedFields;
        r4 = java.lang.Boolean.valueOf(r4);
        r6.put(r3, r4);
        goto L_0x03b4;
        r3 = r14 instanceof org.telegram.ui.Components.GroupCreateSpan;
        if (r3 == 0) goto L_0x03b4;
        r3 = r14;
        r3 = (org.telegram.ui.Components.GroupCreateSpan) r3;
        r3.updateColors();
    L_0x03b4:
        r1 = r1 + 1;
        goto L_0x0002;
    L_0x03b8:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.ActionBar.ThemeDescription.processViewColor(android.view.View, int):void");
    }

    public ThemeDescription(View view, int flags, Class[] classes, Paint[] paint, Drawable[] drawables, ThemeDescriptionDelegate themeDescriptionDelegate, String key, Object unused) {
        this.previousIsDefault = new boolean[1];
        this.currentKey = key;
        this.paintToUpdate = paint;
        this.drawablesToUpdate = drawables;
        this.viewToInvalidate = view;
        this.changeFlags = flags;
        this.listClasses = classes;
        this.delegate = themeDescriptionDelegate;
    }

    public ThemeDescription(View view, int flags, Class[] classes, Paint paint, Drawable[] drawables, ThemeDescriptionDelegate themeDescriptionDelegate, String key) {
        this.previousIsDefault = new boolean[1];
        this.currentKey = key;
        if (paint != null) {
            this.paintToUpdate = new Paint[]{paint};
        }
        this.drawablesToUpdate = drawables;
        this.viewToInvalidate = view;
        this.changeFlags = flags;
        this.listClasses = classes;
        this.delegate = themeDescriptionDelegate;
    }

    public ThemeDescription(View view, int flags, Class[] classes, String[] classesFields, Paint[] paint, Drawable[] drawables, ThemeDescriptionDelegate themeDescriptionDelegate, String key) {
        this.previousIsDefault = new boolean[1];
        this.currentKey = key;
        this.paintToUpdate = paint;
        this.drawablesToUpdate = drawables;
        this.viewToInvalidate = view;
        this.changeFlags = flags;
        this.listClasses = classes;
        this.listClassesFieldName = classesFields;
        this.delegate = themeDescriptionDelegate;
        this.cachedFields = new HashMap();
        this.notFoundCachedFields = new HashMap();
    }

    public ThemeDescriptionDelegate setDelegateDisabled() {
        ThemeDescriptionDelegate oldDelegate = this.delegate;
        this.delegate = null;
        return oldDelegate;
    }

    public void setColor(int color, boolean useDefault) {
        setColor(color, useDefault, true);
    }

    public void setColor(int color, boolean useDefault, boolean save) {
        int a;
        Drawable drawable;
        RecyclerListView recyclerListView;
        int a2;
        if (save) {
            Theme.setColor(this.currentKey, color, useDefault);
        }
        int a3 = 0;
        if (this.paintToUpdate != null) {
            a = 0;
            while (a < this.paintToUpdate.length) {
                if ((this.changeFlags & FLAG_LINKCOLOR) == 0 || !(this.paintToUpdate[a] instanceof TextPaint)) {
                    this.paintToUpdate[a].setColor(color);
                } else {
                    ((TextPaint) this.paintToUpdate[a]).linkColor = color;
                }
                a++;
            }
        }
        if (this.drawablesToUpdate != null) {
            for (a = 0; a < this.drawablesToUpdate.length; a++) {
                if (this.drawablesToUpdate[a] != null) {
                    if (this.drawablesToUpdate[a] instanceof CombinedDrawable) {
                        if ((this.changeFlags & FLAG_BACKGROUNDFILTER) != 0) {
                            ((CombinedDrawable) this.drawablesToUpdate[a]).getBackground().setColorFilter(new PorterDuffColorFilter(color, Mode.MULTIPLY));
                        } else {
                            ((CombinedDrawable) this.drawablesToUpdate[a]).getIcon().setColorFilter(new PorterDuffColorFilter(color, Mode.MULTIPLY));
                        }
                    } else if (this.drawablesToUpdate[a] instanceof AvatarDrawable) {
                        ((AvatarDrawable) this.drawablesToUpdate[a]).setColor(color);
                    } else {
                        this.drawablesToUpdate[a].setColorFilter(new PorterDuffColorFilter(color, Mode.MULTIPLY));
                    }
                }
            }
        }
        if (this.viewToInvalidate != null && this.listClasses == null && this.listClassesFieldName == null && ((this.changeFlags & FLAG_CHECKTAG) == 0 || ((this.changeFlags & FLAG_CHECKTAG) != 0 && this.currentKey.equals(this.viewToInvalidate.getTag())))) {
            if ((this.changeFlags & FLAG_BACKGROUND) != 0) {
                this.viewToInvalidate.setBackgroundColor(color);
            }
            if ((this.changeFlags & FLAG_BACKGROUNDFILTER) != 0) {
                drawable = this.viewToInvalidate.getBackground();
                if (drawable instanceof CombinedDrawable) {
                    if ((this.changeFlags & FLAG_DRAWABLESELECTEDSTATE) != 0) {
                        drawable = ((CombinedDrawable) drawable).getBackground();
                    } else {
                        drawable = ((CombinedDrawable) drawable).getIcon();
                    }
                }
                if (drawable != null) {
                    if (!(drawable instanceof StateListDrawable)) {
                        if (VERSION.SDK_INT < 21 || !(drawable instanceof RippleDrawable)) {
                            if (drawable instanceof ShapeDrawable) {
                                ((ShapeDrawable) drawable).getPaint().setColor(color);
                            } else {
                                drawable.setColorFilter(new PorterDuffColorFilter(color, Mode.MULTIPLY));
                            }
                        }
                    }
                    Theme.setSelectorDrawableColor(drawable, color, (this.changeFlags & FLAG_DRAWABLESELECTEDSTATE) != 0);
                }
            }
        }
        if (this.viewToInvalidate instanceof ActionBar) {
            if ((this.changeFlags & FLAG_AB_ITEMSCOLOR) != 0) {
                ((ActionBar) this.viewToInvalidate).setItemsColor(color, false);
            }
            if ((this.changeFlags & FLAG_AB_TITLECOLOR) != 0) {
                ((ActionBar) this.viewToInvalidate).setTitleColor(color);
            }
            if ((this.changeFlags & FLAG_AB_SELECTORCOLOR) != 0) {
                ((ActionBar) this.viewToInvalidate).setItemsBackgroundColor(color, false);
            }
            if ((this.changeFlags & FLAG_AB_AM_SELECTORCOLOR) != 0) {
                ((ActionBar) this.viewToInvalidate).setItemsBackgroundColor(color, true);
            }
            if ((this.changeFlags & FLAG_AB_AM_ITEMSCOLOR) != 0) {
                ((ActionBar) this.viewToInvalidate).setItemsColor(color, true);
            }
            if ((this.changeFlags & FLAG_AB_SUBTITLECOLOR) != 0) {
                ((ActionBar) this.viewToInvalidate).setSubtitleColor(color);
            }
            if ((this.changeFlags & FLAG_AB_AM_BACKGROUND) != 0) {
                ((ActionBar) this.viewToInvalidate).setActionModeColor(color);
            }
            if ((this.changeFlags & FLAG_AB_AM_TOPBACKGROUND) != 0) {
                ((ActionBar) this.viewToInvalidate).setActionModeTopColor(color);
            }
            if ((this.changeFlags & FLAG_AB_SEARCHPLACEHOLDER) != 0) {
                ((ActionBar) this.viewToInvalidate).setSearchTextColor(color, true);
            }
            if ((this.changeFlags & FLAG_AB_SEARCH) != 0) {
                ((ActionBar) this.viewToInvalidate).setSearchTextColor(color, false);
            }
            if ((this.changeFlags & FLAG_AB_SUBMENUITEM) != 0) {
                ((ActionBar) this.viewToInvalidate).setPopupItemsColor(color);
            }
            if ((this.changeFlags & FLAG_AB_SUBMENUBACKGROUND) != 0) {
                ((ActionBar) this.viewToInvalidate).setPopupBackgroundColor(color);
            }
        }
        if (this.viewToInvalidate instanceof EmptyTextProgressView) {
            if ((this.changeFlags & FLAG_TEXTCOLOR) != 0) {
                ((EmptyTextProgressView) this.viewToInvalidate).setTextColor(color);
            } else if ((this.changeFlags & FLAG_PROGRESSBAR) != 0) {
                ((EmptyTextProgressView) this.viewToInvalidate).setProgressBarColor(color);
            }
        }
        if (this.viewToInvalidate instanceof RadialProgressView) {
            ((RadialProgressView) this.viewToInvalidate).setProgressColor(color);
        } else if (this.viewToInvalidate instanceof LineProgressView) {
            if ((this.changeFlags & FLAG_PROGRESSBAR) != 0) {
                ((LineProgressView) this.viewToInvalidate).setProgressColor(color);
            } else {
                ((LineProgressView) this.viewToInvalidate).setBackColor(color);
            }
        } else if (this.viewToInvalidate instanceof ContextProgressView) {
            ((ContextProgressView) this.viewToInvalidate).updateColors();
        }
        if ((this.changeFlags & FLAG_TEXTCOLOR) != 0 && ((this.changeFlags & FLAG_CHECKTAG) == 0 || !(this.viewToInvalidate == null || (this.changeFlags & FLAG_CHECKTAG) == 0 || !this.currentKey.equals(this.viewToInvalidate.getTag())))) {
            if (this.viewToInvalidate instanceof TextView) {
                ((TextView) this.viewToInvalidate).setTextColor(color);
            } else if (this.viewToInvalidate instanceof NumberTextView) {
                ((NumberTextView) this.viewToInvalidate).setTextColor(color);
            } else if (this.viewToInvalidate instanceof SimpleTextView) {
                ((SimpleTextView) this.viewToInvalidate).setTextColor(color);
            } else if (this.viewToInvalidate instanceof ChatBigEmptyView) {
                ((ChatBigEmptyView) this.viewToInvalidate).setTextColor(color);
            }
        }
        if ((this.changeFlags & FLAG_CURSORCOLOR) != 0 && (this.viewToInvalidate instanceof EditTextBoldCursor)) {
            ((EditTextBoldCursor) this.viewToInvalidate).setCursorColor(color);
        }
        if ((this.changeFlags & FLAG_HINTTEXTCOLOR) != 0) {
            if (this.viewToInvalidate instanceof EditTextBoldCursor) {
                ((EditTextBoldCursor) this.viewToInvalidate).setHintColor(color);
            } else if (this.viewToInvalidate instanceof EditText) {
                ((EditText) this.viewToInvalidate).setHintTextColor(color);
            }
        }
        if (!(this.viewToInvalidate == null || (this.changeFlags & FLAG_SERVICEBACKGROUND) == 0)) {
            drawable = this.viewToInvalidate.getBackground();
            if (drawable != null) {
                drawable.setColorFilter(Theme.colorFilter);
            }
        }
        if ((this.changeFlags & FLAG_IMAGECOLOR) != 0 && ((this.changeFlags & FLAG_CHECKTAG) == 0 || ((this.changeFlags & FLAG_CHECKTAG) != 0 && this.currentKey.equals(this.viewToInvalidate.getTag())))) {
            if (!(this.viewToInvalidate instanceof ImageView)) {
                boolean z = this.viewToInvalidate instanceof BackupImageView;
            } else if ((this.changeFlags & FLAG_USEBACKGROUNDDRAWABLE) != 0) {
                drawable = ((ImageView) this.viewToInvalidate).getDrawable();
                if ((drawable instanceof StateListDrawable) || (VERSION.SDK_INT >= 21 && (drawable instanceof RippleDrawable))) {
                    Theme.setSelectorDrawableColor(drawable, color, (this.changeFlags & FLAG_DRAWABLESELECTEDSTATE) != 0);
                }
            } else {
                ((ImageView) this.viewToInvalidate).setColorFilter(new PorterDuffColorFilter(color, Mode.MULTIPLY));
            }
        }
        if ((this.viewToInvalidate instanceof ScrollView) && (this.changeFlags & FLAG_LISTGLOWCOLOR) != 0) {
            AndroidUtilities.setScrollViewEdgeEffectColor((ScrollView) this.viewToInvalidate, color);
        }
        if (this.viewToInvalidate instanceof RecyclerListView) {
            recyclerListView = this.viewToInvalidate;
            if ((this.changeFlags & FLAG_SELECTOR) != 0 && this.currentKey.equals(Theme.key_listSelector)) {
                recyclerListView.setListSelectorColor(color);
            }
            if ((this.changeFlags & FLAG_FASTSCROLL) != 0) {
                recyclerListView.updateFastScrollColors();
            }
            if ((this.changeFlags & FLAG_LISTGLOWCOLOR) != 0) {
                recyclerListView.setGlowColor(color);
            }
            if ((this.changeFlags & FLAG_SECTIONS) != 0) {
                ArrayList<View> headers = recyclerListView.getHeaders();
                if (headers != null) {
                    for (a2 = 0; a2 < headers.size(); a2++) {
                        processViewColor((View) headers.get(a2), color);
                    }
                }
                headers = recyclerListView.getHeadersCache();
                if (headers != null) {
                    for (a2 = 0; a2 < headers.size(); a2++) {
                        processViewColor((View) headers.get(a2), color);
                    }
                }
                View header = recyclerListView.getPinnedHeader();
                if (header != null) {
                    processViewColor(header, color);
                }
            }
        } else if (this.viewToInvalidate != null) {
            if ((this.changeFlags & FLAG_SELECTOR) != 0) {
                this.viewToInvalidate.setBackgroundDrawable(Theme.getSelectorDrawable(false));
            } else if ((this.changeFlags & FLAG_SELECTORWHITE) != 0) {
                this.viewToInvalidate.setBackgroundDrawable(Theme.getSelectorDrawable(true));
            }
        }
        if (this.listClasses != null) {
            int count;
            if (this.viewToInvalidate instanceof RecyclerListView) {
                recyclerListView = (RecyclerListView) this.viewToInvalidate;
                recyclerListView.getRecycledViewPool().clear();
                count = recyclerListView.getHiddenChildCount();
                for (a2 = 0; a2 < count; a2++) {
                    processViewColor(recyclerListView.getHiddenChildAt(a2), color);
                }
                count = recyclerListView.getCachedChildCount();
                for (a2 = 0; a2 < count; a2++) {
                    processViewColor(recyclerListView.getCachedChildAt(a2), color);
                }
                count = recyclerListView.getAttachedScrapChildCount();
                for (a2 = 0; a2 < count; a2++) {
                    processViewColor(recyclerListView.getAttachedScrapChildAt(a2), color);
                }
            }
            if (this.viewToInvalidate instanceof ViewGroup) {
                ViewGroup viewGroup = this.viewToInvalidate;
                count = viewGroup.getChildCount();
                while (a3 < count) {
                    processViewColor(viewGroup.getChildAt(a3), color);
                    a3++;
                }
            }
            processViewColor(this.viewToInvalidate, color);
        }
        this.currentColor = color;
        if (this.delegate != null) {
            this.delegate.didSetColor();
        }
        if (this.viewToInvalidate != null) {
            this.viewToInvalidate.invalidate();
        }
    }

    public String getCurrentKey() {
        return this.currentKey;
    }

    public void startEditing() {
        int color = Theme.getColor(this.currentKey, this.previousIsDefault);
        this.previousColor = color;
        this.currentColor = color;
    }

    public int getCurrentColor() {
        return this.currentColor;
    }

    public int getSetColor() {
        return Theme.getColor(this.currentKey);
    }

    public void setDefaultColor() {
        setColor(Theme.getDefaultColor(this.currentKey), true);
    }

    public void setPreviousColor() {
        setColor(this.previousColor, this.previousIsDefault[0]);
    }

    public String getTitle() {
        return this.currentKey;
    }
}
