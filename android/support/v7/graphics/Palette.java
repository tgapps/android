package android.support.v7.graphics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.util.ArrayMap;
import android.util.SparseBooleanArray;
import android.util.TimingLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.telegram.ui.ActionBar.Theme;

public final class Palette {
    static final Filter DEFAULT_FILTER = new Filter() {
        public boolean isAllowed(int rgb, float[] hsl) {
            return (isWhite(hsl) || isBlack(hsl) || isNearRedILine(hsl)) ? false : true;
        }

        private boolean isBlack(float[] hslColor) {
            return hslColor[2] <= 0.05f;
        }

        private boolean isWhite(float[] hslColor) {
            return hslColor[2] >= 0.95f;
        }

        private boolean isNearRedILine(float[] hslColor) {
            return hslColor[0] >= 10.0f && hslColor[0] <= 37.0f && hslColor[1] <= 0.82f;
        }
    };
    private final Swatch mDominantSwatch = findDominantSwatch();
    private final Map<Target, Swatch> mSelectedSwatches = new ArrayMap();
    private final List<Swatch> mSwatches;
    private final List<Target> mTargets;
    private final SparseBooleanArray mUsedColors = new SparseBooleanArray();

    public static final class Builder {
        private final Bitmap mBitmap;
        private final List<Filter> mFilters = new ArrayList();
        private int mMaxColors = 16;
        private Rect mRegion;
        private int mResizeArea = 12544;
        private int mResizeMaxDimension = -1;
        private final List<Swatch> mSwatches;
        private final List<Target> mTargets = new ArrayList();

        private android.graphics.Bitmap scaleBitmapDown(android.graphics.Bitmap r1) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v7.graphics.Palette.Builder.scaleBitmapDown(android.graphics.Bitmap):android.graphics.Bitmap
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
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
            r0 = -4616189618054758400; // 0xbff0000000000000 float:0.0 double:-1.0;
            r2 = r7.mResizeArea;
            if (r2 <= 0) goto L_0x001d;
        L_0x0006:
            r2 = r8.getWidth();
            r3 = r8.getHeight();
            r2 = r2 * r3;
            r3 = r7.mResizeArea;
            if (r2 <= r3) goto L_0x001c;
        L_0x0013:
            r3 = r7.mResizeArea;
            r3 = (double) r3;
            r5 = (double) r2;
            r3 = r3 / r5;
            r0 = java.lang.Math.sqrt(r3);
        L_0x001c:
            goto L_0x0037;
        L_0x001d:
            r2 = r7.mResizeMaxDimension;
            if (r2 <= 0) goto L_0x0037;
        L_0x0021:
            r2 = r8.getWidth();
            r3 = r8.getHeight();
            r2 = java.lang.Math.max(r2, r3);
            r3 = r7.mResizeMaxDimension;
            if (r2 <= r3) goto L_0x0037;
        L_0x0031:
            r3 = r7.mResizeMaxDimension;
            r3 = (double) r3;
            r5 = (double) r2;
            r0 = r3 / r5;
        L_0x0037:
            r2 = 0;
            r4 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
            if (r4 > 0) goto L_0x003e;
        L_0x003d:
            return r8;
            r2 = r8.getWidth();
            r2 = (double) r2;
            r2 = r2 * r0;
            r2 = java.lang.Math.ceil(r2);
            r2 = (int) r2;
            r3 = r8.getHeight();
            r3 = (double) r3;
            r3 = r3 * r0;
            r3 = java.lang.Math.ceil(r3);
            r3 = (int) r3;
            r4 = 0;
            r2 = android.graphics.Bitmap.createScaledBitmap(r8, r2, r3, r4);
            return r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.support.v7.graphics.Palette.Builder.scaleBitmapDown(android.graphics.Bitmap):android.graphics.Bitmap");
        }

        public Builder(Bitmap bitmap) {
            if (bitmap != null) {
                if (!bitmap.isRecycled()) {
                    this.mFilters.add(Palette.DEFAULT_FILTER);
                    this.mBitmap = bitmap;
                    this.mSwatches = null;
                    this.mTargets.add(Target.LIGHT_VIBRANT);
                    this.mTargets.add(Target.VIBRANT);
                    this.mTargets.add(Target.DARK_VIBRANT);
                    this.mTargets.add(Target.LIGHT_MUTED);
                    this.mTargets.add(Target.MUTED);
                    this.mTargets.add(Target.DARK_MUTED);
                    return;
                }
            }
            throw new IllegalArgumentException("Bitmap is not valid");
        }

        public Palette generate() {
            List<Swatch> swatches;
            TimingLogger logger = null;
            if (this.mBitmap != null) {
                Bitmap bitmap = scaleBitmapDown(this.mBitmap);
                if (logger != null) {
                    logger.addSplit("Processed Bitmap");
                }
                Rect region = this.mRegion;
                if (!(bitmap == this.mBitmap || region == null)) {
                    double scale = ((double) bitmap.getWidth()) / ((double) this.mBitmap.getWidth());
                    region.left = (int) Math.floor(((double) region.left) * scale);
                    region.top = (int) Math.floor(((double) region.top) * scale);
                    region.right = Math.min((int) Math.ceil(((double) region.right) * scale), bitmap.getWidth());
                    region.bottom = Math.min((int) Math.ceil(((double) region.bottom) * scale), bitmap.getHeight());
                }
                ColorCutQuantizer quantizer = new ColorCutQuantizer(getPixelsFromBitmap(bitmap), this.mMaxColors, this.mFilters.isEmpty() ? null : (Filter[]) this.mFilters.toArray(new Filter[this.mFilters.size()]));
                if (bitmap != this.mBitmap) {
                    bitmap.recycle();
                }
                swatches = quantizer.getQuantizedColors();
                if (logger != null) {
                    logger.addSplit("Color quantization completed");
                }
            } else {
                swatches = this.mSwatches;
            }
            Palette p = new Palette(swatches, this.mTargets);
            p.generate();
            if (logger != null) {
                logger.addSplit("Created Palette");
                logger.dumpToLog();
            }
            return p;
        }

        private int[] getPixelsFromBitmap(Bitmap bitmap) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();
            int[] pixels = new int[(bitmapWidth * bitmapHeight)];
            bitmap.getPixels(pixels, 0, bitmapWidth, 0, 0, bitmapWidth, bitmapHeight);
            if (this.mRegion == null) {
                return pixels;
            }
            int regionWidth = this.mRegion.width();
            int regionHeight = this.mRegion.height();
            int[] subsetPixels = new int[(regionWidth * regionHeight)];
            for (int row = 0; row < regionHeight; row++) {
                System.arraycopy(pixels, ((this.mRegion.top + row) * bitmapWidth) + this.mRegion.left, subsetPixels, row * regionWidth, regionWidth);
            }
            return subsetPixels;
        }
    }

    public interface Filter {
        boolean isAllowed(int i, float[] fArr);
    }

    public static final class Swatch {
        private final int mBlue;
        private int mBodyTextColor;
        private boolean mGeneratedTextColors;
        private final int mGreen;
        private float[] mHsl;
        private final int mPopulation;
        private final int mRed;
        private final int mRgb;
        private int mTitleTextColor;

        public Swatch(int color, int population) {
            this.mRed = Color.red(color);
            this.mGreen = Color.green(color);
            this.mBlue = Color.blue(color);
            this.mRgb = color;
            this.mPopulation = population;
        }

        public int getRgb() {
            return this.mRgb;
        }

        public float[] getHsl() {
            if (this.mHsl == null) {
                this.mHsl = new float[3];
            }
            ColorUtils.RGBToHSL(this.mRed, this.mGreen, this.mBlue, this.mHsl);
            return this.mHsl;
        }

        public int getPopulation() {
            return this.mPopulation;
        }

        public int getTitleTextColor() {
            ensureTextColorsGenerated();
            return this.mTitleTextColor;
        }

        public int getBodyTextColor() {
            ensureTextColorsGenerated();
            return this.mBodyTextColor;
        }

        private void ensureTextColorsGenerated() {
            if (!this.mGeneratedTextColors) {
                int lightBodyAlpha = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 4.5f);
                int lightTitleAlpha = ColorUtils.calculateMinimumAlpha(-1, this.mRgb, 3.0f);
                if (lightBodyAlpha == -1 || lightTitleAlpha == -1) {
                    int darkBodyAlpha = ColorUtils.calculateMinimumAlpha(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, this.mRgb, 4.5f);
                    int darkTitleAlpha = ColorUtils.calculateMinimumAlpha(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, this.mRgb, 3.0f);
                    if (darkBodyAlpha == -1 || darkTitleAlpha == -1) {
                        int alphaComponent;
                        int alphaComponent2;
                        if (lightBodyAlpha != -1) {
                            alphaComponent = ColorUtils.setAlphaComponent(-1, lightBodyAlpha);
                        } else {
                            alphaComponent = ColorUtils.setAlphaComponent(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, darkBodyAlpha);
                        }
                        this.mBodyTextColor = alphaComponent;
                        if (lightTitleAlpha != -1) {
                            alphaComponent2 = ColorUtils.setAlphaComponent(-1, lightTitleAlpha);
                        } else {
                            alphaComponent2 = ColorUtils.setAlphaComponent(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, darkTitleAlpha);
                        }
                        this.mTitleTextColor = alphaComponent2;
                        this.mGeneratedTextColors = true;
                    } else {
                        this.mBodyTextColor = ColorUtils.setAlphaComponent(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, darkBodyAlpha);
                        this.mTitleTextColor = ColorUtils.setAlphaComponent(Theme.ACTION_BAR_VIDEO_EDIT_COLOR, darkTitleAlpha);
                        this.mGeneratedTextColors = true;
                        return;
                    }
                }
                this.mBodyTextColor = ColorUtils.setAlphaComponent(-1, lightBodyAlpha);
                this.mTitleTextColor = ColorUtils.setAlphaComponent(-1, lightTitleAlpha);
                this.mGeneratedTextColors = true;
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(getClass().getSimpleName());
            stringBuilder.append(" [RGB: #");
            stringBuilder.append(Integer.toHexString(getRgb()));
            stringBuilder.append(']');
            stringBuilder.append(" [HSL: ");
            stringBuilder.append(Arrays.toString(getHsl()));
            stringBuilder.append(']');
            stringBuilder.append(" [Population: ");
            stringBuilder.append(this.mPopulation);
            stringBuilder.append(']');
            stringBuilder.append(" [Title Text: #");
            stringBuilder.append(Integer.toHexString(getTitleTextColor()));
            stringBuilder.append(']');
            stringBuilder.append(" [Body Text: #");
            stringBuilder.append(Integer.toHexString(getBodyTextColor()));
            stringBuilder.append(']');
            return stringBuilder.toString();
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (o != null) {
                if (getClass() == o.getClass()) {
                    Swatch swatch = (Swatch) o;
                    if (this.mPopulation != swatch.mPopulation || this.mRgb != swatch.mRgb) {
                        z = false;
                    }
                    return z;
                }
            }
            return false;
        }

        public int hashCode() {
            return (31 * this.mRgb) + this.mPopulation;
        }
    }

    public static Builder from(Bitmap bitmap) {
        return new Builder(bitmap);
    }

    Palette(List<Swatch> swatches, List<Target> targets) {
        this.mSwatches = swatches;
        this.mTargets = targets;
    }

    public int getDarkMutedColor(int defaultColor) {
        return getColorForTarget(Target.DARK_MUTED, defaultColor);
    }

    public Swatch getSwatchForTarget(Target target) {
        return (Swatch) this.mSelectedSwatches.get(target);
    }

    public int getColorForTarget(Target target, int defaultColor) {
        Swatch swatch = getSwatchForTarget(target);
        return swatch != null ? swatch.getRgb() : defaultColor;
    }

    void generate() {
        int count = this.mTargets.size();
        for (int i = 0; i < count; i++) {
            Target target = (Target) this.mTargets.get(i);
            target.normalizeWeights();
            this.mSelectedSwatches.put(target, generateScoredTarget(target));
        }
        this.mUsedColors.clear();
    }

    private Swatch generateScoredTarget(Target target) {
        Swatch maxScoreSwatch = getMaxScoredSwatchForTarget(target);
        if (maxScoreSwatch != null && target.isExclusive()) {
            this.mUsedColors.append(maxScoreSwatch.getRgb(), true);
        }
        return maxScoreSwatch;
    }

    private Swatch getMaxScoredSwatchForTarget(Target target) {
        float maxScore = 0.0f;
        Swatch maxScoreSwatch = null;
        int count = this.mSwatches.size();
        for (int i = 0; i < count; i++) {
            Swatch swatch = (Swatch) this.mSwatches.get(i);
            if (shouldBeScoredForTarget(swatch, target)) {
                float score = generateScore(swatch, target);
                if (maxScoreSwatch == null || score > maxScore) {
                    maxScoreSwatch = swatch;
                    maxScore = score;
                }
            }
        }
        return maxScoreSwatch;
    }

    private boolean shouldBeScoredForTarget(Swatch swatch, Target target) {
        float[] hsl = swatch.getHsl();
        if (hsl[1] < target.getMinimumSaturation() || hsl[1] > target.getMaximumSaturation() || hsl[2] < target.getMinimumLightness() || hsl[2] > target.getMaximumLightness() || this.mUsedColors.get(swatch.getRgb())) {
            return false;
        }
        return true;
    }

    private float generateScore(Swatch swatch, Target target) {
        float[] hsl = swatch.getHsl();
        float saturationScore = 0.0f;
        float luminanceScore = 0.0f;
        float populationScore = 0.0f;
        int maxPopulation = this.mDominantSwatch != null ? this.mDominantSwatch.getPopulation() : 1;
        if (target.getSaturationWeight() > 0.0f) {
            saturationScore = target.getSaturationWeight() * (1.0f - Math.abs(hsl[1] - target.getTargetSaturation()));
        }
        if (target.getLightnessWeight() > 0.0f) {
            luminanceScore = target.getLightnessWeight() * (1.0f - Math.abs(hsl[2] - target.getTargetLightness()));
        }
        if (target.getPopulationWeight() > 0.0f) {
            populationScore = target.getPopulationWeight() * (((float) swatch.getPopulation()) / ((float) maxPopulation));
        }
        return (saturationScore + luminanceScore) + populationScore;
    }

    private Swatch findDominantSwatch() {
        int maxPop = Integer.MIN_VALUE;
        Swatch maxSwatch = null;
        int count = this.mSwatches.size();
        for (int i = 0; i < count; i++) {
            Swatch swatch = (Swatch) this.mSwatches.get(i);
            if (swatch.getPopulation() > maxPop) {
                maxSwatch = swatch;
                maxPop = swatch.getPopulation();
            }
        }
        return maxSwatch;
    }
}
