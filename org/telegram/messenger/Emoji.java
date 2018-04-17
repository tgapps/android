package org.telegram.messenger;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

public class Emoji {
    private static int bigImgSize = AndroidUtilities.dp(AndroidUtilities.isTablet() ? 40.0f : 32.0f);
    private static final int[][] cols = new int[][]{new int[]{16, 16, 16, 16}, new int[]{6, 6, 6, 6}, new int[]{9, 9, 9, 9}, new int[]{9, 9, 9, 9}, new int[]{10, 10, 10, 10}};
    private static int drawImgSize = AndroidUtilities.dp(20.0f);
    private static Bitmap[][] emojiBmp = ((Bitmap[][]) Array.newInstance(Bitmap.class, new int[]{5, 4}));
    public static HashMap<String, String> emojiColor = new HashMap();
    public static HashMap<String, Integer> emojiUseHistory = new HashMap();
    private static boolean inited = false;
    private static boolean[][] loadingEmoji = ((boolean[][]) Array.newInstance(boolean.class, new int[]{5, 4}));
    private static Paint placeholderPaint = new Paint();
    public static ArrayList<String> recentEmoji = new ArrayList();
    private static boolean recentEmojiLoaded = false;
    private static HashMap<CharSequence, DrawableInfo> rects = new HashMap();
    private static final int splitCount = 4;

    static class AnonymousClass1 implements Runnable {
        final /* synthetic */ Bitmap val$finalBitmap;
        final /* synthetic */ int val$page;
        final /* synthetic */ int val$page2;

        AnonymousClass1(int i, int i2, Bitmap bitmap) {
            this.val$page = i;
            this.val$page2 = i2;
            this.val$finalBitmap = bitmap;
        }

        public void run() {
            Emoji.emojiBmp[this.val$page][this.val$page2] = this.val$finalBitmap;
            NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.emojiDidLoaded, new Object[0]);
        }
    }

    private static class DrawableInfo {
        public int emojiIndex;
        public byte page;
        public byte page2;
        public Rect rect;

        public DrawableInfo(Rect r, byte p, byte p2, int index) {
            this.rect = r;
            this.page = p;
            this.page2 = p2;
            this.emojiIndex = index;
        }
    }

    public static class EmojiDrawable extends Drawable {
        private static Paint paint = new Paint(2);
        private static Rect rect = new Rect();
        private static TextPaint textPaint = new TextPaint(1);
        private boolean fullSize = false;
        private DrawableInfo info;

        public EmojiDrawable(DrawableInfo i) {
            this.info = i;
        }

        public DrawableInfo getDrawableInfo() {
            return this.info;
        }

        public Rect getDrawRect() {
            Rect original = getBounds();
            int cX = original.centerX();
            int cY = original.centerY();
            rect.left = cX - ((this.fullSize ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            rect.right = ((this.fullSize ? Emoji.bigImgSize : Emoji.drawImgSize) / 2) + cX;
            rect.top = cY - ((this.fullSize ? Emoji.bigImgSize : Emoji.drawImgSize) / 2);
            rect.bottom = ((this.fullSize ? Emoji.bigImgSize : Emoji.drawImgSize) / 2) + cY;
            return rect;
        }

        public void draw(Canvas canvas) {
            if (Emoji.emojiBmp[this.info.page][this.info.page2] != null) {
                Rect b;
                if (this.fullSize) {
                    b = getDrawRect();
                } else {
                    b = getBounds();
                }
                canvas.drawBitmap(Emoji.emojiBmp[this.info.page][this.info.page2], this.info.rect, b, paint);
            } else if (!Emoji.loadingEmoji[this.info.page][this.info.page2]) {
                Emoji.loadingEmoji[this.info.page][this.info.page2] = true;
                Utilities.globalQueue.postRunnable(new Runnable() {
                    public void run() {
                        Emoji.loadEmoji(EmojiDrawable.this.info.page, EmojiDrawable.this.info.page2);
                        Emoji.loadingEmoji[EmojiDrawable.this.info.page][EmojiDrawable.this.info.page2] = false;
                    }
                });
                canvas.drawRect(getBounds(), Emoji.placeholderPaint);
            }
        }

        public int getOpacity() {
            return -2;
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter cf) {
        }
    }

    public static class EmojiSpan extends ImageSpan {
        private FontMetricsInt fontMetrics = null;
        private int size = AndroidUtilities.dp(20.0f);

        public EmojiSpan(EmojiDrawable d, int verticalAlignment, int s, FontMetricsInt original) {
            super(d, verticalAlignment);
            this.fontMetrics = original;
            if (original != null) {
                this.size = Math.abs(this.fontMetrics.descent) + Math.abs(this.fontMetrics.ascent);
                if (this.size == 0) {
                    this.size = AndroidUtilities.dp(20.0f);
                }
            }
        }

        public void replaceFontMetrics(FontMetricsInt newMetrics, int newSize) {
            this.fontMetrics = newMetrics;
            this.size = newSize;
        }

        public int getSize(Paint paint, CharSequence text, int start, int end, FontMetricsInt fm) {
            if (fm == null) {
                fm = new FontMetricsInt();
            }
            if (this.fontMetrics == null) {
                int sz = super.getSize(paint, text, start, end, fm);
                int offset = AndroidUtilities.dp(1090519040);
                int w = AndroidUtilities.dp(1092616192);
                fm.top = (-w) - offset;
                fm.bottom = w - offset;
                fm.ascent = (-w) - offset;
                fm.leading = 0;
                fm.descent = w - offset;
                return sz;
            }
            if (fm != null) {
                fm.ascent = this.fontMetrics.ascent;
                fm.descent = this.fontMetrics.descent;
                fm.top = this.fontMetrics.top;
                fm.bottom = this.fontMetrics.bottom;
            }
            if (getDrawable() != null) {
                getDrawable().setBounds(0, 0, this.size, this.size);
            }
            return this.size;
        }
    }

    public static native Object[] getSuggestion(String str);

    private static void loadEmoji(int r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.Emoji.loadEmoji(int, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = 1;
        r1 = org.telegram.messenger.AndroidUtilities.density;	 Catch:{ Throwable -> 0x011d }
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;	 Catch:{ Throwable -> 0x011d }
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));	 Catch:{ Throwable -> 0x011d }
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Throwable -> 0x011d }
        if (r1 > 0) goto L_0x000f;	 Catch:{ Throwable -> 0x011d }
    L_0x000b:
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Throwable -> 0x011d }
        r0 = 2;	 Catch:{ Throwable -> 0x011d }
        goto L_0x0024;	 Catch:{ Throwable -> 0x011d }
    L_0x000f:
        r1 = org.telegram.messenger.AndroidUtilities.density;	 Catch:{ Throwable -> 0x011d }
        r3 = 1069547520; // 0x3fc00000 float:1.5 double:5.28426686E-315;	 Catch:{ Throwable -> 0x011d }
        r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));	 Catch:{ Throwable -> 0x011d }
        if (r1 > 0) goto L_0x001a;	 Catch:{ Throwable -> 0x011d }
    L_0x0017:
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;	 Catch:{ Throwable -> 0x011d }
        goto L_0x0024;	 Catch:{ Throwable -> 0x011d }
    L_0x001a:
        r1 = org.telegram.messenger.AndroidUtilities.density;	 Catch:{ Throwable -> 0x011d }
        r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1));
        if (r1 > 0) goto L_0x0023;
    L_0x0020:
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x0019;
    L_0x0024:
        r1 = r2;
        r2 = 4;
        r3 = 7;
        r4 = 2;
        r5 = 3;
        r6 = 0;
        r7 = 1;
        if (r2 >= r3) goto L_0x008a;
        r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x0088 }
        r8 = "v%d_emoji%.01fx_%d.jpg";	 Catch:{ Exception -> 0x0088 }
        r9 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x0088 }
        r10 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x0088 }
        r9[r6] = r10;	 Catch:{ Exception -> 0x0088 }
        r10 = java.lang.Float.valueOf(r1);	 Catch:{ Exception -> 0x0088 }
        r9[r7] = r10;	 Catch:{ Exception -> 0x0088 }
        r10 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x0088 }
        r9[r4] = r10;	 Catch:{ Exception -> 0x0088 }
        r3 = java.lang.String.format(r3, r8, r9);	 Catch:{ Exception -> 0x0088 }
        r8 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0088 }
        r8 = r8.getFileStreamPath(r3);	 Catch:{ Exception -> 0x0088 }
        r9 = r8.exists();	 Catch:{ Exception -> 0x0088 }
        if (r9 == 0) goto L_0x0058;	 Catch:{ Exception -> 0x0088 }
        r8.delete();	 Catch:{ Exception -> 0x0088 }
        r9 = java.util.Locale.US;	 Catch:{ Exception -> 0x0088 }
        r10 = "v%d_emoji%.01fx_a_%d.jpg";	 Catch:{ Exception -> 0x0088 }
        r11 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x0088 }
        r12 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x0088 }
        r11[r6] = r12;	 Catch:{ Exception -> 0x0088 }
        r12 = java.lang.Float.valueOf(r1);	 Catch:{ Exception -> 0x0088 }
        r11[r7] = r12;	 Catch:{ Exception -> 0x0088 }
        r12 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x0088 }
        r11[r4] = r12;	 Catch:{ Exception -> 0x0088 }
        r9 = java.lang.String.format(r9, r10, r11);	 Catch:{ Exception -> 0x0088 }
        r3 = r9;	 Catch:{ Exception -> 0x0088 }
        r9 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0088 }
        r9 = r9.getFileStreamPath(r3);	 Catch:{ Exception -> 0x0088 }
        r8 = r9;	 Catch:{ Exception -> 0x0088 }
        r9 = r8.exists();	 Catch:{ Exception -> 0x0088 }
        if (r9 == 0) goto L_0x0085;	 Catch:{ Exception -> 0x0088 }
        r8.delete();	 Catch:{ Exception -> 0x0088 }
        r2 = r2 + 1;	 Catch:{ Exception -> 0x0088 }
        goto L_0x0026;	 Catch:{ Exception -> 0x0088 }
    L_0x0088:
        r2 = move-exception;	 Catch:{ Exception -> 0x0088 }
        goto L_0x00be;	 Catch:{ Exception -> 0x0088 }
        r2 = 8;	 Catch:{ Exception -> 0x0088 }
        r3 = 12;	 Catch:{ Exception -> 0x0088 }
        if (r2 >= r3) goto L_0x00c3;	 Catch:{ Exception -> 0x0088 }
        r3 = java.util.Locale.US;	 Catch:{ Exception -> 0x0088 }
        r8 = "v%d_emoji%.01fx_%d.png";	 Catch:{ Exception -> 0x0088 }
        r9 = new java.lang.Object[r5];	 Catch:{ Exception -> 0x0088 }
        r10 = java.lang.Integer.valueOf(r2);	 Catch:{ Exception -> 0x0088 }
        r9[r6] = r10;	 Catch:{ Exception -> 0x0088 }
        r10 = java.lang.Float.valueOf(r1);	 Catch:{ Exception -> 0x0088 }
        r9[r7] = r10;	 Catch:{ Exception -> 0x0088 }
        r10 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x0088 }
        r9[r4] = r10;	 Catch:{ Exception -> 0x0088 }
        r3 = java.lang.String.format(r3, r8, r9);	 Catch:{ Exception -> 0x0088 }
        r8 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Exception -> 0x0088 }
        r8 = r8.getFileStreamPath(r3);	 Catch:{ Exception -> 0x0088 }
        r9 = r8.exists();	 Catch:{ Exception -> 0x0088 }
        if (r9 == 0) goto L_0x00bb;	 Catch:{ Exception -> 0x0088 }
        r8.delete();	 Catch:{ Exception -> 0x0088 }
        r2 = r2 + 1;
        goto L_0x008c;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Throwable -> 0x011d }
        goto L_0x00c4;
        r2 = 0;
        r3 = r2;
        r8 = org.telegram.messenger.ApplicationLoader.applicationContext;	 Catch:{ Throwable -> 0x010f }
        r8 = r8.getAssets();	 Catch:{ Throwable -> 0x010f }
        r9 = new java.lang.StringBuilder;	 Catch:{ Throwable -> 0x010f }
        r9.<init>();	 Catch:{ Throwable -> 0x010f }
        r10 = "emoji/";	 Catch:{ Throwable -> 0x010f }
        r9.append(r10);	 Catch:{ Throwable -> 0x010f }
        r10 = java.util.Locale.US;	 Catch:{ Throwable -> 0x010f }
        r11 = "v12_emoji%.01fx_%d_%d.png";	 Catch:{ Throwable -> 0x010f }
        r5 = new java.lang.Object[r5];	 Catch:{ Throwable -> 0x010f }
        r12 = java.lang.Float.valueOf(r1);	 Catch:{ Throwable -> 0x010f }
        r5[r6] = r12;	 Catch:{ Throwable -> 0x010f }
        r12 = java.lang.Integer.valueOf(r13);	 Catch:{ Throwable -> 0x010f }
        r5[r7] = r12;	 Catch:{ Throwable -> 0x010f }
        r7 = java.lang.Integer.valueOf(r14);	 Catch:{ Throwable -> 0x010f }
        r5[r4] = r7;	 Catch:{ Throwable -> 0x010f }
        r4 = java.lang.String.format(r10, r11, r5);	 Catch:{ Throwable -> 0x010f }
        r9.append(r4);	 Catch:{ Throwable -> 0x010f }
        r4 = r9.toString();	 Catch:{ Throwable -> 0x010f }
        r4 = r8.open(r4);	 Catch:{ Throwable -> 0x010f }
        r5 = new android.graphics.BitmapFactory$Options;	 Catch:{ Throwable -> 0x010f }
        r5.<init>();	 Catch:{ Throwable -> 0x010f }
        r5.inJustDecodeBounds = r6;	 Catch:{ Throwable -> 0x010f }
        r5.inSampleSize = r0;	 Catch:{ Throwable -> 0x010f }
        r2 = android.graphics.BitmapFactory.decodeStream(r4, r2, r5);	 Catch:{ Throwable -> 0x010f }
        r3 = r2;	 Catch:{ Throwable -> 0x010f }
        r4.close();	 Catch:{ Throwable -> 0x010f }
        goto L_0x0113;
    L_0x010f:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ Throwable -> 0x011d }
        r2 = r3;	 Catch:{ Throwable -> 0x011d }
        r4 = new org.telegram.messenger.Emoji$1;	 Catch:{ Throwable -> 0x011d }
        r4.<init>(r13, r14, r2);	 Catch:{ Throwable -> 0x011d }
        org.telegram.messenger.AndroidUtilities.runOnUIThread(r4);	 Catch:{ Throwable -> 0x011d }
        goto L_0x0127;
    L_0x011d:
        r0 = move-exception;
        r1 = org.telegram.messenger.BuildVars.LOGS_ENABLED;
        if (r1 == 0) goto L_0x0127;
        r1 = "Error loading emoji";
        org.telegram.messenger.FileLog.e(r1, r0);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.Emoji.loadEmoji(int, int):void");
    }

    static {
        int emojiFullSize;
        int add = 2;
        if (AndroidUtilities.density <= 1.0f) {
            emojiFullSize = 32;
            add = 1;
        } else if (AndroidUtilities.density <= 1.5f) {
            emojiFullSize = 64;
        } else if (AndroidUtilities.density <= 2.0f) {
            emojiFullSize = 64;
        } else {
            emojiFullSize = 64;
        }
        for (int j = 0; j < EmojiData.data.length; j++) {
            int count2 = (int) Math.ceil((double) (((float) EmojiData.data[j].length) / 4.0f));
            for (int i = 0; i < EmojiData.data[j].length; i++) {
                int page = i / count2;
                int position = i - (page * count2);
                int row = position % cols[j][page];
                int col = position / cols[j][page];
                rects.put(EmojiData.data[j][i], new DrawableInfo(new Rect((row * emojiFullSize) + (row * add), (col * emojiFullSize) + (col * add), ((row + 1) * emojiFullSize) + (row * add), ((col + 1) * emojiFullSize) + (col * add)), (byte) j, (byte) page, i));
            }
        }
        placeholderPaint.setColor(0);
    }

    public static void invalidateAll(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup g = (ViewGroup) view;
            for (int i = 0; i < g.getChildCount(); i++) {
                invalidateAll(g.getChildAt(i));
            }
        } else if (view instanceof TextView) {
            view.invalidate();
        }
    }

    public static String fixEmoji(String emoji) {
        int lenght = emoji.length();
        String emoji2 = emoji;
        int a = 0;
        while (a < lenght) {
            char ch = emoji2.charAt(a);
            StringBuilder stringBuilder;
            if (ch < '?' || ch > '?') {
                if (ch == '⃣') {
                    return emoji2;
                }
                if (ch >= '‼' && ch <= '㊙' && EmojiData.emojiToFE0FMap.containsKey(Character.valueOf(ch))) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(emoji2.substring(0, a + 1));
                    stringBuilder.append("️");
                    stringBuilder.append(emoji2.substring(a + 1));
                    emoji2 = stringBuilder.toString();
                    lenght++;
                    a++;
                }
            } else if (ch != '?' || a >= lenght - 1) {
                a++;
            } else {
                ch = emoji2.charAt(a + 1);
                if (!(ch == '?' || ch == '?' || ch == '?')) {
                    if (ch != '?') {
                        a++;
                    }
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append(emoji2.substring(0, a + 2));
                stringBuilder.append("️");
                stringBuilder.append(emoji2.substring(a + 2));
                emoji2 = stringBuilder.toString();
                lenght++;
                a += 2;
            }
            a++;
        }
        return emoji2;
    }

    public static EmojiDrawable getEmojiDrawable(CharSequence code) {
        DrawableInfo info = (DrawableInfo) rects.get(code);
        if (info == null) {
            CharSequence newCode = (CharSequence) EmojiData.emojiAliasMap.get(code);
            if (newCode != null) {
                info = (DrawableInfo) rects.get(newCode);
            }
        }
        if (info == null) {
            if (BuildVars.LOGS_ENABLED) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("No drawable for emoji ");
                stringBuilder.append(code);
                FileLog.d(stringBuilder.toString());
            }
            return null;
        }
        EmojiDrawable ed = new EmojiDrawable(info);
        ed.setBounds(0, 0, drawImgSize, drawImgSize);
        return ed;
    }

    public static boolean isValidEmoji(String code) {
        DrawableInfo info = (DrawableInfo) rects.get(code);
        if (info == null) {
            CharSequence newCode = (CharSequence) EmojiData.emojiAliasMap.get(code);
            if (newCode != null) {
                info = (DrawableInfo) rects.get(newCode);
            }
        }
        return info != null;
    }

    public static Drawable getEmojiBigDrawable(String code) {
        EmojiDrawable ed = getEmojiDrawable(code);
        if (ed == null) {
            CharSequence newCode = (CharSequence) EmojiData.emojiAliasMap.get(code);
            if (newCode != null) {
                ed = getEmojiDrawable(newCode);
            }
        }
        if (ed == null) {
            return null;
        }
        ed.setBounds(0, 0, bigImgSize, bigImgSize);
        ed.fullSize = true;
        return ed;
    }

    private static boolean inArray(char c, char[] a) {
        for (char cc : a) {
            if (cc == c) {
                return true;
            }
        }
        return false;
    }

    public static CharSequence replaceEmoji(CharSequence cs, FontMetricsInt fontMetrics, int size, boolean createNew) {
        return replaceEmoji(cs, fontMetrics, size, createNew, null);
    }

    public static CharSequence replaceEmoji(CharSequence cs, FontMetricsInt fontMetrics, int size, boolean createNew, int[] emojiOnly) {
        Throwable e;
        CharSequence charSequence = cs;
        if (!(SharedConfig.useSystemEmoji || charSequence == null)) {
            if (cs.length() != 0) {
                Spannable s;
                if (createNew || !(charSequence instanceof Spannable)) {
                    s = Factory.getInstance().newSpannable(cs.toString());
                } else {
                    s = (Spannable) charSequence;
                }
                int startIndex = -1;
                int startLength = 0;
                int previousGoodIndex = 0;
                StringBuilder emojiCode = new StringBuilder(16);
                StringBuilder addionalCode = new StringBuilder(2);
                int length = cs.length();
                boolean doneEmoji = false;
                int[] emojiOnly2 = emojiOnly;
                int emojiCount = 0;
                long buf = 0;
                int i = 0;
                while (i < length) {
                    long j;
                    char c2;
                    int a;
                    int emojiCount2;
                    char c = charSequence.charAt(i);
                    if ((c >= '?' && c <= '?') || (buf != 0 && (buf & -4294967296L) == 0 && (buf & 65535) == 55356 && c >= '?' && c <= '?')) {
                        if (startIndex == -1) {
                            startIndex = i;
                        }
                        try {
                            emojiCode.append(c);
                            buf = (buf << 16) | ((long) c);
                            startIndex = startIndex;
                            startLength++;
                        } catch (Throwable e2) {
                            int i2 = startIndex;
                            e = e2;
                            j = buf;
                            buf = size;
                        }
                    } else if (emojiCode.length() > 0 && (c == '♀' || c == '♂' || c == '⚕')) {
                        try {
                            emojiCode.append(c);
                            startLength++;
                            buf = 0;
                            doneEmoji = true;
                        } catch (Throwable e22) {
                            e = e22;
                            j = buf;
                            buf = size;
                        }
                    } else if (buf > 0 && (61440 & c) == 53248) {
                        emojiCode.append(c);
                        startLength++;
                        buf = 0;
                        doneEmoji = true;
                    } else if (c != '⃣') {
                        if (c == '©' || c == '®' || (c >= '‼' && c <= '㊙')) {
                            try {
                                if (EmojiData.dataCharsMap.containsKey(Character.valueOf(c))) {
                                    if (startIndex == -1) {
                                        startIndex = i;
                                    }
                                    startLength++;
                                    emojiCode.append(c);
                                    doneEmoji = true;
                                }
                            } catch (Throwable e222) {
                                j = buf;
                                buf = size;
                                e = e222;
                            }
                        }
                        if (startIndex != -1) {
                            emojiCode.setLength(0);
                            startIndex = -1;
                            startLength = 0;
                            doneEmoji = false;
                        } else if (!(c == '️' || emojiOnly2 == null)) {
                            emojiOnly2[0] = 0;
                            emojiOnly2 = null;
                        }
                    } else if (i > 0) {
                        c2 = charSequence.charAt(previousGoodIndex);
                        if ((c2 >= '0' && c2 <= '9') || c2 == '#' || c2 == '*') {
                            startIndex = previousGoodIndex;
                            startLength = (i - previousGoodIndex) + 1;
                            emojiCode.append(c2);
                            emojiCode.append(c);
                            doneEmoji = true;
                        }
                    }
                    if (doneEmoji && i + 2 < length) {
                        c2 = charSequence.charAt(i + 1);
                        if (c2 == '?') {
                            c2 = charSequence.charAt(i + 2);
                            if (c2 >= '?' && c2 <= '?') {
                                emojiCode.append(charSequence.subSequence(i + 1, i + 3));
                                startLength += 2;
                                i += 2;
                            }
                        } else if (emojiCode.length() >= 2 && emojiCode.charAt(0) == '?' && emojiCode.charAt(1) == '?' && c2 == '?') {
                            i++;
                            do {
                                emojiCode.append(charSequence.subSequence(i, i + 2));
                                startLength += 2;
                                i += 2;
                                if (i >= cs.length()) {
                                    break;
                                }
                            } while (charSequence.charAt(i) == '?');
                            i--;
                        }
                    }
                    previousGoodIndex = i;
                    for (a = 0; a < 3; a++) {
                        if (i + 1 < length) {
                            c = charSequence.charAt(i + 1);
                            if (a == 1) {
                                if (c == '‍' && emojiCode.length() > 0) {
                                    emojiCode.append(c);
                                    i++;
                                    startLength++;
                                    doneEmoji = false;
                                }
                            } else if (c >= '︀') {
                                if (c <= '️') {
                                    i++;
                                    startLength++;
                                }
                            }
                        }
                    }
                    if (doneEmoji && i + 2 < length && charSequence.charAt(i + 1) == '?') {
                        char next = charSequence.charAt(i + 2);
                        if (next >= '?' && next <= '?') {
                            emojiCode.append(charSequence.subSequence(i + 1, i + 3));
                            startLength += 2;
                            i += 2;
                        }
                    }
                    if (doneEmoji) {
                        if (emojiOnly2 != null) {
                            emojiOnly2[0] = emojiOnly2[0] + 1;
                        }
                        CharSequence code = emojiCode.subSequence(0, emojiCode.length());
                        EmojiDrawable drawable = getEmojiDrawable(code);
                        if (drawable != null) {
                            j = buf;
                            try {
                                s.setSpan(new EmojiSpan(drawable, 0, size, fontMetrics), startIndex, startIndex + startLength, 33);
                                emojiCount++;
                            } catch (Throwable e2222) {
                                e = e2222;
                            }
                        } else {
                            j = buf;
                            buf = size;
                        }
                        startLength = 0;
                        startIndex = -1;
                        a = 0;
                        emojiCode.setLength(0);
                        doneEmoji = false;
                        emojiCount2 = emojiCount;
                    } else {
                        j = buf;
                        a = 0;
                        buf = size;
                        emojiCount2 = emojiCount;
                    }
                    try {
                        if (VERSION.SDK_INT < 23 && emojiCount2 >= 50) {
                            emojiCount = emojiCount2;
                            break;
                        }
                        i++;
                        int i3 = a;
                        emojiCount = emojiCount2;
                        buf = j;
                    } catch (Throwable e22222) {
                        e = e22222;
                        emojiCount = emojiCount2;
                    }
                }
                buf = size;
                return s;
            }
        }
        int i4 = size;
        return charSequence;
        FileLog.e(e);
        return charSequence;
    }

    public static void addRecentEmoji(String code) {
        Integer count = (Integer) emojiUseHistory.get(code);
        if (count == null) {
            count = Integer.valueOf(0);
        }
        if (count.intValue() == 0 && emojiUseHistory.size() > 50) {
            for (int a = recentEmoji.size() - 1; a >= 0; a--) {
                emojiUseHistory.remove((String) recentEmoji.get(a));
                recentEmoji.remove(a);
                if (emojiUseHistory.size() <= 50) {
                    break;
                }
            }
        }
        HashMap hashMap = emojiUseHistory;
        Integer valueOf = Integer.valueOf(count.intValue() + 1);
        count = valueOf;
        hashMap.put(code, valueOf);
    }

    public static void sortEmoji() {
        recentEmoji.clear();
        for (Entry<String, Integer> entry : emojiUseHistory.entrySet()) {
            recentEmoji.add(entry.getKey());
        }
        Collections.sort(recentEmoji, new Comparator<String>() {
            public int compare(String lhs, String rhs) {
                Integer count1 = (Integer) Emoji.emojiUseHistory.get(lhs);
                Integer count2 = (Integer) Emoji.emojiUseHistory.get(rhs);
                if (count1 == null) {
                    count1 = Integer.valueOf(0);
                }
                if (count2 == null) {
                    count2 = Integer.valueOf(0);
                }
                if (count1.intValue() > count2.intValue()) {
                    return -1;
                }
                if (count1.intValue() < count2.intValue()) {
                    return 1;
                }
                return 0;
            }
        });
        while (recentEmoji.size() > 50) {
            recentEmoji.remove(recentEmoji.size() - 1);
        }
    }

    public static void saveRecentEmoji() {
        SharedPreferences preferences = MessagesController.getGlobalEmojiSettings();
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry<String, Integer> entry : emojiUseHistory.entrySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
        }
        preferences.edit().putString("emojis2", stringBuilder.toString()).commit();
    }

    public static void clearRecentEmoji() {
        MessagesController.getGlobalEmojiSettings().edit().putBoolean("filled_default", true).commit();
        emojiUseHistory.clear();
        recentEmoji.clear();
        saveRecentEmoji();
    }

    public static void loadRecentEmoji() {
        Throwable e;
        SharedPreferences preferences;
        String str;
        if (!recentEmojiLoaded) {
            String[] args;
            String[] args2;
            int a;
            recentEmojiLoaded = true;
            SharedPreferences preferences2 = MessagesController.getGlobalEmojiSettings();
            try {
                emojiUseHistory.clear();
                long j = 16;
                String str2;
                if (preferences2.contains("emojis")) {
                    str2 = preferences2.getString("emojis", TtmlNode.ANONYMOUS_REGION_ID);
                    if (str2 != null) {
                        try {
                            if (str2.length() > 0) {
                                args = str2.split(",");
                                int length = args.length;
                                int i = 0;
                                while (i < length) {
                                    String[] args22 = args[i].split("=");
                                    long value = Utilities.parseLong(args22[0]).longValue();
                                    StringBuilder string = new StringBuilder();
                                    SharedPreferences preferences3 = preferences2;
                                    long value2 = value;
                                    int a2 = 0;
                                    while (a2 < 4) {
                                        try {
                                            string.insert(0, String.valueOf((char) ((int) value2)));
                                            value2 >>= j;
                                            if (value2 == 0) {
                                                break;
                                            }
                                            a2++;
                                        } catch (Throwable e2) {
                                            e = e2;
                                            preferences = preferences3;
                                        }
                                    }
                                    if (string.length() > 0) {
                                        emojiUseHistory.put(string.toString(), Utilities.parseInt(args22[1]));
                                    }
                                    i++;
                                    preferences2 = preferences3;
                                    j = 16;
                                }
                            }
                        } catch (Throwable e22) {
                            preferences = preferences2;
                            e = e22;
                            FileLog.e(e);
                            str = preferences.getString(TtmlNode.ATTR_TTS_COLOR, TtmlNode.ANONYMOUS_REGION_ID);
                            args2 = str.split(",");
                            for (String arg : args2) {
                                args = arg.split("=");
                                emojiColor.put(args[0], args[1]);
                            }
                        }
                    }
                    preferences = preferences2;
                    try {
                        preferences.edit().remove("emojis").commit();
                        saveRecentEmoji();
                    } catch (Throwable e222) {
                        e = e222;
                        FileLog.e(e);
                        str = preferences.getString(TtmlNode.ATTR_TTS_COLOR, TtmlNode.ANONYMOUS_REGION_ID);
                        args2 = str.split(",");
                        while (a < args2.length) {
                            args = arg.split("=");
                            emojiColor.put(args[0], args[1]);
                        }
                    }
                }
                preferences = preferences2;
                str2 = preferences.getString("emojis2", TtmlNode.ANONYMOUS_REGION_ID);
                if (str2 != null && str2.length() > 0) {
                    for (String arg2 : str2.split(",")) {
                        String[] args23 = arg2.split("=");
                        emojiUseHistory.put(args23[0], Utilities.parseInt(args23[1]));
                    }
                }
                if (emojiUseHistory.isEmpty() && !preferences.getBoolean("filled_default", false)) {
                    String[] newRecent = new String[]{"😂", "😘", "❤", "😍", "😊", "😁", "👍", "☺", "😔", "😄", "😭", "💋", "😒", "😳", "😜", "🙈", "😉", "😃", "😢", "😝", "😱", "😡", "😏", "😞", "😅", "😚", "🙊", "😌", "😀", "😋", "😆", "👌", "😐", "😕"};
                    for (a = 0; a < newRecent.length; a++) {
                        emojiUseHistory.put(newRecent[a], Integer.valueOf(newRecent.length - a));
                    }
                    preferences.edit().putBoolean("filled_default", true).commit();
                    saveRecentEmoji();
                }
                sortEmoji();
            } catch (Throwable e2222) {
                preferences = preferences2;
                e = e2222;
                FileLog.e(e);
                str = preferences.getString(TtmlNode.ATTR_TTS_COLOR, TtmlNode.ANONYMOUS_REGION_ID);
                args2 = str.split(",");
                while (a < args2.length) {
                    args = arg.split("=");
                    emojiColor.put(args[0], args[1]);
                }
            }
            try {
                str = preferences.getString(TtmlNode.ATTR_TTS_COLOR, TtmlNode.ANONYMOUS_REGION_ID);
                if (str != null && str.length() > 0) {
                    args2 = str.split(",");
                    while (a < args2.length) {
                        args = arg.split("=");
                        emojiColor.put(args[0], args[1]);
                    }
                }
            } catch (Throwable e22222) {
                FileLog.e(e22222);
            }
        }
    }

    public static void saveEmojiColors() {
        SharedPreferences preferences = MessagesController.getGlobalEmojiSettings();
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry<String, String> entry : emojiColor.entrySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append((String) entry.getValue());
        }
        preferences.edit().putString(TtmlNode.ATTR_TTS_COLOR, stringBuilder.toString()).commit();
    }
}
