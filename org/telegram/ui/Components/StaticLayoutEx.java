package org.telegram.ui.Components;

import android.os.Build.VERSION;
import android.text.Layout.Alignment;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import java.lang.reflect.Constructor;
import org.telegram.messenger.FileLog;

public class StaticLayoutEx {
    private static final String TEXT_DIRS_CLASS = "android.text.TextDirectionHeuristics";
    private static final String TEXT_DIR_CLASS = "android.text.TextDirectionHeuristic";
    private static final String TEXT_DIR_FIRSTSTRONG_LTR = "FIRSTSTRONG_LTR";
    private static boolean initialized;
    private static Constructor<StaticLayout> sConstructor;
    private static Object[] sConstructorArgs;
    private static Object sTextDirection;

    public static void init() {
        if (!initialized) {
            try {
                Class<?> textDirClass;
                if (VERSION.SDK_INT >= 18) {
                    textDirClass = TextDirectionHeuristic.class;
                    sTextDirection = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                } else {
                    ClassLoader loader = StaticLayoutEx.class.getClassLoader();
                    Class<?> textDirClass2 = loader.loadClass(TEXT_DIR_CLASS);
                    Class<?> textDirsClass = loader.loadClass(TEXT_DIRS_CLASS);
                    sTextDirection = textDirsClass.getField(TEXT_DIR_FIRSTSTRONG_LTR).get(textDirsClass);
                    textDirClass = textDirClass2;
                }
                Class<?>[] signature = new Class[]{CharSequence.class, Integer.TYPE, Integer.TYPE, TextPaint.class, Integer.TYPE, Alignment.class, textDirClass, Float.TYPE, Float.TYPE, Boolean.TYPE, TruncateAt.class, Integer.TYPE, Integer.TYPE};
                sConstructor = StaticLayout.class.getDeclaredConstructor(signature);
                sConstructor.setAccessible(true);
                sConstructorArgs = new Object[signature.length];
                initialized = true;
            } catch (Throwable e) {
                FileLog.e(e);
            }
        }
    }

    public static StaticLayout createStaticLayout(CharSequence source, TextPaint paint, int width, Alignment align, float spacingmult, float spacingadd, boolean includepad, TruncateAt ellipsize, int ellipsisWidth, int maxLines) {
        return createStaticLayout(source, 0, source.length(), paint, width, align, spacingmult, spacingadd, includepad, ellipsize, ellipsisWidth, maxLines);
    }

    public static StaticLayout createStaticLayout(CharSequence source, int bufstart, int bufend, TextPaint paint, int outerWidth, Alignment align, float spacingMult, float spacingAdd, boolean includePad, TruncateAt ellipsize, int ellipsisWidth, int maxLines) {
        CharSequence charSequence = source;
        TextPaint textPaint = paint;
        int i = ellipsisWidth;
        int i2 = maxLines;
        if (i2 == 1) {
            try {
                CharSequence text = TextUtils.ellipsize(charSequence, textPaint, (float) i, TruncateAt.END);
                return new StaticLayout(text, 0, text.length(), textPaint, outerWidth, align, spacingMult, spacingAdd, includePad);
            } catch (Exception e) {
                Throwable e2 = e;
                int i3 = outerWidth;
                Alignment alignment = align;
                float f = spacingMult;
                float f2 = spacingAdd;
                boolean z = includePad;
                Throwable e3 = e2;
                StaticLayout staticLayout = null;
                int i4 = i2;
                FileLog.e(e3);
                return staticLayout;
            }
        }
        try {
            StaticLayout layout;
            if (VERSION.SDK_INT >= 23) {
                i3 = outerWidth;
                try {
                    alignment = align;
                } catch (Exception e4) {
                    e2 = e4;
                    Alignment alignment2 = align;
                    float f3 = spacingMult;
                    float f22 = spacingAdd;
                    boolean z2 = includePad;
                    Throwable e32 = e2;
                    StaticLayout staticLayout2 = null;
                    int i42 = i2;
                    FileLog.e(e32);
                    return staticLayout2;
                }
                try {
                    f3 = spacingMult;
                    f22 = spacingAdd;
                } catch (Exception e5) {
                    e2 = e5;
                    float f32 = spacingMult;
                    float f222 = spacingAdd;
                    boolean z22 = includePad;
                    Throwable e322 = e2;
                    StaticLayout staticLayout22 = null;
                    int i422 = i2;
                    FileLog.e(e322);
                    return staticLayout22;
                }
                try {
                    z22 = includePad;
                } catch (Exception e6) {
                    e2 = e6;
                    boolean z222 = includePad;
                    Throwable e3222 = e2;
                    StaticLayout staticLayout222 = null;
                    int i4222 = i2;
                    FileLog.e(e3222);
                    return staticLayout222;
                }
                try {
                    layout = Builder.obtain(charSequence, 0, source.length(), textPaint, i3).setAlignment(alignment2).setLineSpacing(f222, f32).setIncludePad(z222).setEllipsize(null).setEllipsizedWidth(i).setBreakStrategy(1).setHyphenationFrequency(1).build();
                    TruncateAt truncateAt = null;
                    i4222 = i2;
                } catch (Exception e7) {
                    e2 = e7;
                    Throwable e32222 = e2;
                    StaticLayout staticLayout2222 = null;
                    int i42222 = i2;
                    FileLog.e(e32222);
                    return staticLayout2222;
                }
            }
            StaticLayout staticLayout3;
            i3 = outerWidth;
            alignment2 = align;
            f32 = spacingMult;
            f222 = spacingAdd;
            z222 = includePad;
            try {
                staticLayout3 = staticLayout3;
                staticLayout2222 = null;
                i42222 = i2;
            } catch (Exception e8) {
                e2 = e8;
                staticLayout2222 = null;
                i42222 = i2;
                e32222 = e2;
                FileLog.e(e32222);
                return staticLayout2222;
            }
            try {
                staticLayout3 = new StaticLayout(charSequence, textPaint, i3, alignment2, f32, f222, z222);
            } catch (Exception e9) {
                e2 = e9;
                e32222 = e2;
                FileLog.e(e32222);
                return staticLayout2222;
            }
            StaticLayout layout2 = layout;
            if (layout2.getLineCount() <= i42222) {
                return layout2;
            }
            int off;
            float left = layout2.getLineLeft(i42222 - 1);
            if (left != 0.0f) {
                off = layout2.getOffsetForHorizontal(i42222 - 1, left);
            } else {
                off = layout2.getOffsetForHorizontal(i42222 - 1, layout2.getLineWidth(i42222 - 1));
            }
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(charSequence.subSequence(0, Math.max(0, off - 1)));
            stringBuilder.append("…");
            return new StaticLayout(stringBuilder, paint, i3, alignment2, f32, f222, z222);
        } catch (Exception e10) {
            e2 = e10;
            i3 = outerWidth;
            alignment2 = align;
            f32 = spacingMult;
            f222 = spacingAdd;
            z222 = includePad;
            staticLayout2222 = null;
            i42222 = i2;
            e32222 = e2;
            FileLog.e(e32222);
            return staticLayout2222;
        }
    }
}
