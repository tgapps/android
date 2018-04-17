package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.beta.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.Theme.ThemeInfo;
import org.telegram.ui.Components.LayoutHelper;

public class ThemeCell extends FrameLayout {
    private static byte[] bytes = new byte[1024];
    private ImageView checkImage;
    private ThemeInfo currentThemeInfo;
    private boolean isNightTheme;
    private boolean needDivider;
    private ImageView optionsButton;
    private Paint paint = new Paint(1);
    private TextView textView;

    public void setTheme(org.telegram.ui.ActionBar.Theme.ThemeInfo r1, boolean r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Cells.ThemeCell.setTheme(org.telegram.ui.ActionBar.Theme$ThemeInfo, boolean):void
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
        r1 = r21;
        r2 = r22;
        r1.currentThemeInfo = r2;
        r3 = r22.getName();
        r4 = ".attheme";
        r4 = r3.endsWith(r4);
        r5 = 0;
        if (r4 == 0) goto L_0x001d;
    L_0x0013:
        r4 = 46;
        r4 = r3.lastIndexOf(r4);
        r3 = r3.substring(r5, r4);
    L_0x001d:
        r4 = r1.textView;
        r4.setText(r3);
        r4 = r23;
        r1.needDivider = r4;
        r21.updateCurrentThemeCheck();
        r6 = 0;
        r7 = r2.pathToFile;
        if (r7 != 0) goto L_0x0037;
    L_0x002e:
        r7 = r2.assetName;
        if (r7 == 0) goto L_0x0033;
    L_0x0032:
        goto L_0x0037;
    L_0x0033:
        r17 = r3;
        goto L_0x0164;
    L_0x0037:
        r7 = 0;
        r8 = 0;
        r9 = r2.assetName;	 Catch:{ Throwable -> 0x0157, all -> 0x0151 }
        if (r9 == 0) goto L_0x0050;
    L_0x003d:
        r9 = r2.assetName;	 Catch:{ Throwable -> 0x004a, all -> 0x0044 }
        r9 = org.telegram.ui.ActionBar.Theme.getAssetFile(r9);	 Catch:{ Throwable -> 0x004a, all -> 0x0044 }
        goto L_0x0057;
    L_0x0044:
        r0 = move-exception;
        r2 = r0;
        r17 = r3;
        goto L_0x0174;
    L_0x004a:
        r0 = move-exception;
        r2 = r0;
        r17 = r3;
        goto L_0x015b;
    L_0x0050:
        r9 = new java.io.File;	 Catch:{ Throwable -> 0x0157, all -> 0x0151 }
        r10 = r2.pathToFile;	 Catch:{ Throwable -> 0x0157, all -> 0x0151 }
        r9.<init>(r10);	 Catch:{ Throwable -> 0x0157, all -> 0x0151 }
    L_0x0057:
        r10 = new java.io.FileInputStream;	 Catch:{ Throwable -> 0x0157, all -> 0x0151 }
        r10.<init>(r9);	 Catch:{ Throwable -> 0x0157, all -> 0x0151 }
        r7 = r10;
        r10 = r6;
        r6 = r5;
        r11 = bytes;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r11 = r7.read(r11);	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r12 = r11;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r13 = -1;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        if (r11 == r13) goto L_0x0136;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
    L_0x0069:
        r11 = r8;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r14 = 0;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r15 = r8;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r8 = r6;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r6 = r5;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
    L_0x006e:
        if (r6 >= r12) goto L_0x010d;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
    L_0x0070:
        r16 = bytes;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r5 = r16[r6];	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r13 = 10;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        if (r5 != r13) goto L_0x00ff;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
    L_0x0078:
        r8 = r8 + 1;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r5 = r6 - r14;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r5 = r5 + 1;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r13 = new java.lang.String;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r2 = bytes;	 Catch:{ Throwable -> 0x014b, all -> 0x0146 }
        r17 = r3;
        r3 = r5 + -1;
        r4 = "UTF-8";	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r13.<init>(r2, r14, r3, r4);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r2 = r13;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r3 = "WPS";	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r3 = r2.startsWith(r3);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        if (r3 == 0) goto L_0x0098;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
    L_0x0095:
        r6 = r8;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        goto L_0x0110;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
    L_0x0098:
        r3 = 61;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r3 = r2.indexOf(r3);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r4 = r3;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r13 = -1;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        if (r3 == r13) goto L_0x00fa;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
    L_0x00a2:
        r3 = 0;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r16 = r2.substring(r3, r4);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r3 = r16;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r13 = "actionBarDefault";	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r13 = r3.equals(r13);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        if (r13 == 0) goto L_0x00fa;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
    L_0x00b1:
        r13 = r4 + 1;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r13 = r2.substring(r13);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r16 = r13.length();	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        if (r16 <= 0) goto L_0x00db;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
    L_0x00bd:
        r18 = r2;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r19 = r3;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r2 = 0;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r3 = r13.charAt(r2);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r2 = 35;
        if (r3 != r2) goto L_0x00df;
    L_0x00ca:
        r2 = android.graphics.Color.parseColor(r13);	 Catch:{ Exception -> 0x00cf }
    L_0x00ce:
        goto L_0x00e7;
    L_0x00cf:
        r0 = move-exception;
        r2 = r0;
        r3 = org.telegram.messenger.Utilities.parseInt(r13);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r3 = r3.intValue();	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r2 = r3;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        goto L_0x00ce;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
    L_0x00db:
        r18 = r2;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r19 = r3;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
    L_0x00df:
        r2 = org.telegram.messenger.Utilities.parseInt(r13);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r2 = r2.intValue();	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
    L_0x00e7:
        r3 = 1;
        r10 = r1.paint;	 Catch:{ Throwable -> 0x00f5, all -> 0x00f0 }
        r10.setColor(r2);	 Catch:{ Throwable -> 0x00f5, all -> 0x00f0 }
        r10 = r3;
        goto L_0x0095;
    L_0x00f0:
        r0 = move-exception;
        r2 = r0;
        r10 = r3;
        goto L_0x0175;
    L_0x00f5:
        r0 = move-exception;
        r2 = r0;
        r6 = r3;
        goto L_0x015b;
    L_0x00fa:
        r18 = r2;
        r14 = r14 + r5;
        r15 = r15 + r5;
        goto L_0x0101;
    L_0x00ff:
        r17 = r3;
    L_0x0101:
        r6 = r6 + 1;
        r3 = r17;
        r2 = r22;
        r4 = r23;
        r5 = 0;
        r13 = -1;
        goto L_0x006e;
    L_0x010d:
        r17 = r3;
        r6 = r8;
    L_0x0110:
        if (r11 == r15) goto L_0x0134;
    L_0x0112:
        r2 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        if (r6 < r2) goto L_0x0117;
    L_0x0116:
        goto L_0x0134;
    L_0x0117:
        r2 = r7.getChannel();	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r3 = (long) r15;	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        r2.position(r3);	 Catch:{ Throwable -> 0x0130, all -> 0x012d }
        if (r10 == 0) goto L_0x0122;
    L_0x0121:
        goto L_0x0134;
        r8 = r15;
        r3 = r17;
        r2 = r22;
        r4 = r23;
        r5 = 0;
        goto L_0x005f;
    L_0x012d:
        r0 = move-exception;
        r2 = r0;
        goto L_0x0175;
    L_0x0130:
        r0 = move-exception;
        r2 = r0;
        r6 = r10;
        goto L_0x015b;
    L_0x0134:
        r6 = r10;
        goto L_0x0139;
    L_0x0136:
        r17 = r3;
        r6 = r10;
        if (r7 == 0) goto L_0x0145;
        r7.close();	 Catch:{ Exception -> 0x013f }
        goto L_0x0145;
    L_0x013f:
        r0 = move-exception;
        r2 = r0;
        org.telegram.messenger.FileLog.e(r2);
        goto L_0x0164;
        goto L_0x0164;
    L_0x0146:
        r0 = move-exception;
        r17 = r3;
        r2 = r0;
        goto L_0x0156;
    L_0x014b:
        r0 = move-exception;
        r17 = r3;
        r2 = r0;
        r6 = r10;
        goto L_0x015b;
    L_0x0151:
        r0 = move-exception;
        r17 = r3;
        r2 = r0;
        r10 = r6;
        goto L_0x0175;
    L_0x0157:
        r0 = move-exception;
        r17 = r3;
        r2 = r0;
    L_0x015b:
        org.telegram.messenger.FileLog.e(r2);	 Catch:{ all -> 0x0172 }
        if (r7 == 0) goto L_0x0145;
        r7.close();	 Catch:{ Exception -> 0x013f }
        goto L_0x0145;
    L_0x0164:
        if (r6 != 0) goto L_0x0171;
        r2 = r1.paint;
        r3 = "actionBarDefault";
        r3 = org.telegram.ui.ActionBar.Theme.getDefaultColor(r3);
        r2.setColor(r3);
        return;
    L_0x0172:
        r0 = move-exception;
        r2 = r0;
    L_0x0174:
        r10 = r6;
    L_0x0175:
        if (r7 == 0) goto L_0x0181;
        r7.close();	 Catch:{ Exception -> 0x017b }
        goto L_0x0181;
    L_0x017b:
        r0 = move-exception;
        r3 = r0;
        org.telegram.messenger.FileLog.e(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Cells.ThemeCell.setTheme(org.telegram.ui.ActionBar.Theme$ThemeInfo, boolean):void");
    }

    public ThemeCell(Context context, boolean nightTheme) {
        Context context2 = context;
        super(context);
        setWillNotDraw(false);
        this.isNightTheme = nightTheme;
        this.textView = new TextView(context2);
        this.textView.setTextSize(1, 16.0f);
        this.textView.setLines(1);
        this.textView.setMaxLines(1);
        this.textView.setSingleLine(true);
        this.textView.setPadding(0, 0, 0, AndroidUtilities.dp(1.0f));
        this.textView.setEllipsize(TruncateAt.END);
        int i = 3;
        this.textView.setGravity((LocaleController.isRTL ? 5 : 3) | 16);
        addView(r0.textView, LayoutHelper.createFrame(-1, -1.0f, (LocaleController.isRTL ? 5 : 3) | 48, LocaleController.isRTL ? 101.0f : 60.0f, 0.0f, LocaleController.isRTL ? 60.0f : 101.0f, 0.0f));
        r0.checkImage = new ImageView(context2);
        r0.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_featuredStickers_addedIcon), Mode.MULTIPLY));
        r0.checkImage.setImageResource(R.drawable.sticker_added);
        if (r0.isNightTheme) {
            View view = r0.checkImage;
            if (!LocaleController.isRTL) {
                i = 5;
            }
            addView(view, LayoutHelper.createFrame(19, 14.0f, i | 16, 17.0f, 0.0f, 17.0f, 0.0f));
            return;
        }
        addView(r0.checkImage, LayoutHelper.createFrame(19, 14.0f, (LocaleController.isRTL ? 3 : 5) | 16, 55.0f, 0.0f, 55.0f, 0.0f));
        r0.optionsButton = new ImageView(context2);
        r0.optionsButton.setFocusable(false);
        r0.optionsButton.setBackgroundDrawable(Theme.createSelectorDrawable(Theme.getColor(Theme.key_stickers_menuSelector)));
        r0.optionsButton.setImageResource(R.drawable.ic_ab_other);
        r0.optionsButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_stickers_menu), Mode.MULTIPLY));
        r0.optionsButton.setScaleType(ScaleType.CENTER);
        view = r0.optionsButton;
        if (!LocaleController.isRTL) {
            i = 5;
        }
        addView(view, LayoutHelper.createFrame(48, 48, i | 48));
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.checkImage.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_featuredStickers_addedIcon), Mode.MULTIPLY));
        this.textView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), 1073741824), MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(48.0f) + this.needDivider, 1073741824));
    }

    public void setOnOptionsClick(OnClickListener listener) {
        this.optionsButton.setOnClickListener(listener);
    }

    public TextView getTextView() {
        return this.textView;
    }

    public void setTextColor(int color) {
        this.textView.setTextColor(color);
    }

    public ThemeInfo getCurrentThemeInfo() {
        return this.currentThemeInfo;
    }

    public void updateCurrentThemeCheck() {
        ThemeInfo currentTheme;
        if (this.isNightTheme) {
            currentTheme = Theme.getCurrentNightTheme();
        } else {
            currentTheme = Theme.getCurrentTheme();
        }
        int newVisibility = this.currentThemeInfo == currentTheme ? 0 : 4;
        if (this.checkImage.getVisibility() != newVisibility) {
            this.checkImage.setVisibility(newVisibility);
        }
    }

    protected void onDraw(Canvas canvas) {
        if (this.needDivider) {
            canvas.drawLine((float) getPaddingLeft(), (float) (getHeight() - 1), (float) (getWidth() - getPaddingRight()), (float) (getHeight() - 1), Theme.dividerPaint);
        }
        int x = AndroidUtilities.dp(1104674816);
        if (LocaleController.isRTL) {
            x = getWidth() - x;
        }
        canvas.drawCircle((float) x, (float) AndroidUtilities.dp(24.0f), (float) AndroidUtilities.dp(11.0f), this.paint);
    }
}
