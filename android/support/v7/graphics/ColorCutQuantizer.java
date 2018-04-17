package android.support.v7.graphics;

import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette.Filter;
import android.support.v7.graphics.Palette.Swatch;
import android.util.TimingLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import org.telegram.tgnet.ConnectionsManager;

final class ColorCutQuantizer {
    private static final Comparator<Vbox> VBOX_COMPARATOR_VOLUME = new Comparator<Vbox>() {
        public int compare(Vbox lhs, Vbox rhs) {
            return rhs.getVolume() - lhs.getVolume();
        }
    };
    final int[] mColors;
    final Filter[] mFilters;
    final int[] mHistogram;
    final List<Swatch> mQuantizedColors;
    private final float[] mTempHsl = new float[3];
    final TimingLogger mTimingLogger = null;

    private class Vbox {
        private int mLowerIndex;
        private int mMaxBlue;
        private int mMaxGreen;
        private int mMaxRed;
        private int mMinBlue;
        private int mMinGreen;
        private int mMinRed;
        private int mPopulation;
        private int mUpperIndex;

        Vbox(int lowerIndex, int upperIndex) {
            this.mLowerIndex = lowerIndex;
            this.mUpperIndex = upperIndex;
            fitBox();
        }

        final int getVolume() {
            return (((this.mMaxRed - this.mMinRed) + 1) * ((this.mMaxGreen - this.mMinGreen) + 1)) * ((this.mMaxBlue - this.mMinBlue) + 1);
        }

        final boolean canSplit() {
            return getColorCount() > 1;
        }

        final int getColorCount() {
            return (1 + this.mUpperIndex) - this.mLowerIndex;
        }

        final void fitBox() {
            int[] colors = ColorCutQuantizer.this.mColors;
            int[] hist = ColorCutQuantizer.this.mHistogram;
            int minRed = ConnectionsManager.DEFAULT_DATACENTER_ID;
            int minBlue = ConnectionsManager.DEFAULT_DATACENTER_ID;
            int minGreen = ConnectionsManager.DEFAULT_DATACENTER_ID;
            int maxRed = Integer.MIN_VALUE;
            int maxBlue = Integer.MIN_VALUE;
            int maxGreen = Integer.MIN_VALUE;
            int count = 0;
            for (int i = this.mLowerIndex; i <= this.mUpperIndex; i++) {
                int color = colors[i];
                count += hist[color];
                int r = ColorCutQuantizer.quantizedRed(color);
                int g = ColorCutQuantizer.quantizedGreen(color);
                int b = ColorCutQuantizer.quantizedBlue(color);
                if (r > maxRed) {
                    maxRed = r;
                }
                if (r < minRed) {
                    minRed = r;
                }
                if (g > maxGreen) {
                    maxGreen = g;
                }
                if (g < minGreen) {
                    minGreen = g;
                }
                if (b > maxBlue) {
                    maxBlue = b;
                }
                if (b < minBlue) {
                    minBlue = b;
                }
            }
            this.mMinRed = minRed;
            this.mMaxRed = maxRed;
            this.mMinGreen = minGreen;
            this.mMaxGreen = maxGreen;
            this.mMinBlue = minBlue;
            this.mMaxBlue = maxBlue;
            this.mPopulation = count;
        }

        final Vbox splitBox() {
            if (canSplit()) {
                int splitPoint = findSplitPoint();
                Vbox newBox = new Vbox(splitPoint + 1, this.mUpperIndex);
                this.mUpperIndex = splitPoint;
                fitBox();
                return newBox;
            }
            throw new IllegalStateException("Can not split a box with only 1 color");
        }

        final int getLongestColorDimension() {
            int redLength = this.mMaxRed - this.mMinRed;
            int greenLength = this.mMaxGreen - this.mMinGreen;
            int blueLength = this.mMaxBlue - this.mMinBlue;
            if (redLength >= greenLength && redLength >= blueLength) {
                return -3;
            }
            if (greenLength < redLength || greenLength < blueLength) {
                return -1;
            }
            return -2;
        }

        final int findSplitPoint() {
            int longestDimension = getLongestColorDimension();
            int[] colors = ColorCutQuantizer.this.mColors;
            int[] hist = ColorCutQuantizer.this.mHistogram;
            ColorCutQuantizer.modifySignificantOctet(colors, longestDimension, this.mLowerIndex, this.mUpperIndex);
            Arrays.sort(colors, this.mLowerIndex, this.mUpperIndex + 1);
            ColorCutQuantizer.modifySignificantOctet(colors, longestDimension, this.mLowerIndex, this.mUpperIndex);
            int midPoint = this.mPopulation / 2;
            int count = 0;
            for (int i = this.mLowerIndex; i <= this.mUpperIndex; i++) {
                count += hist[colors[i]];
                if (count >= midPoint) {
                    return Math.min(this.mUpperIndex - 1, i);
                }
            }
            return this.mLowerIndex;
        }

        final Swatch getAverageColor() {
            int[] colors = ColorCutQuantizer.this.mColors;
            int[] hist = ColorCutQuantizer.this.mHistogram;
            int redSum = 0;
            int greenSum = 0;
            int blueSum = 0;
            int totalPopulation = 0;
            for (int i = this.mLowerIndex; i <= this.mUpperIndex; i++) {
                int color = colors[i];
                int colorPopulation = hist[color];
                totalPopulation += colorPopulation;
                redSum += ColorCutQuantizer.quantizedRed(color) * colorPopulation;
                greenSum += ColorCutQuantizer.quantizedGreen(color) * colorPopulation;
                blueSum += ColorCutQuantizer.quantizedBlue(color) * colorPopulation;
            }
            return new Swatch(ColorCutQuantizer.approximateToRgb888(Math.round(((float) redSum) / ((float) totalPopulation)), Math.round(((float) greenSum) / ((float) totalPopulation)), Math.round(((float) blueSum) / ((float) totalPopulation))), totalPopulation);
        }
    }

    static void modifySignificantOctet(int[] r1, int r2, int r3, int r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v7.graphics.ColorCutQuantizer.modifySignificantOctet(int[], int, int, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        switch(r5) {
            case -3: goto L_0x003d;
            case -2: goto L_0x0020;
            case -1: goto L_0x0004;
            default: goto L_0x0003;
        };
        goto L_0x003e;
        r0 = r6;
        if (r0 > r7) goto L_0x003e;
        r1 = r4[r0];
        r2 = quantizedBlue(r1);
        r2 = r2 << 10;
        r3 = quantizedGreen(r1);
        r3 = r3 << 5;
        r2 = r2 | r3;
        r3 = quantizedRed(r1);
        r2 = r2 | r3;
        r4[r0] = r2;
        r0 = r0 + 1;
        goto L_0x0005;
        r0 = r6;
        if (r0 > r7) goto L_0x003c;
        r1 = r4[r0];
        r2 = quantizedGreen(r1);
        r2 = r2 << 10;
        r3 = quantizedRed(r1);
        r3 = r3 << 5;
        r2 = r2 | r3;
        r3 = quantizedBlue(r1);
        r2 = r2 | r3;
        r4[r0] = r2;
        r0 = r0 + 1;
        goto L_0x0021;
        goto L_0x003e;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.graphics.ColorCutQuantizer.modifySignificantOctet(int[], int, int, int):void");
    }

    ColorCutQuantizer(int[] pixels, int maxColors, Filter[] filters) {
        int i;
        int quantizedColor;
        int color;
        this.mFilters = filters;
        int[] hist = new int[32768];
        this.mHistogram = hist;
        int i2 = 0;
        for (i = 0; i < pixels.length; i++) {
            quantizedColor = quantizeFromRgb888(pixels[i]);
            pixels[i] = quantizedColor;
            hist[quantizedColor] = hist[quantizedColor] + 1;
        }
        quantizedColor = 0;
        i = 0;
        while (i < hist.length) {
            if (hist[i] > 0 && shouldIgnoreColor(i)) {
                hist[i] = 0;
            }
            if (hist[i] > 0) {
                quantizedColor++;
            }
            i++;
        }
        int[] colors = new int[quantizedColor];
        this.mColors = colors;
        int distinctColorIndex = 0;
        for (color = 0; color < hist.length; color++) {
            if (hist[color] > 0) {
                int distinctColorIndex2 = distinctColorIndex + 1;
                colors[distinctColorIndex] = color;
                distinctColorIndex = distinctColorIndex2;
            }
        }
        if (quantizedColor <= maxColors) {
            this.mQuantizedColors = new ArrayList();
            color = colors.length;
            while (i2 < color) {
                distinctColorIndex2 = colors[i2];
                this.mQuantizedColors.add(new Swatch(approximateToRgb888(distinctColorIndex2), hist[distinctColorIndex2]));
                i2++;
            }
            return;
        }
        this.mQuantizedColors = quantizePixels(maxColors);
    }

    List<Swatch> getQuantizedColors() {
        return this.mQuantizedColors;
    }

    private List<Swatch> quantizePixels(int maxColors) {
        PriorityQueue<Vbox> pq = new PriorityQueue(maxColors, VBOX_COMPARATOR_VOLUME);
        pq.offer(new Vbox(0, this.mColors.length - 1));
        splitBoxes(pq, maxColors);
        return generateAverageColors(pq);
    }

    private void splitBoxes(PriorityQueue<Vbox> queue, int maxSize) {
        while (queue.size() < maxSize) {
            Vbox vbox = (Vbox) queue.poll();
            if (vbox != null && vbox.canSplit()) {
                queue.offer(vbox.splitBox());
                queue.offer(vbox);
            } else {
                return;
            }
        }
    }

    private List<Swatch> generateAverageColors(Collection<Vbox> vboxes) {
        ArrayList<Swatch> colors = new ArrayList(vboxes.size());
        for (Vbox vbox : vboxes) {
            Swatch swatch = vbox.getAverageColor();
            if (!shouldIgnoreColor(swatch)) {
                colors.add(swatch);
            }
        }
        return colors;
    }

    private boolean shouldIgnoreColor(int color565) {
        int rgb = approximateToRgb888(color565);
        ColorUtils.colorToHSL(rgb, this.mTempHsl);
        return shouldIgnoreColor(rgb, this.mTempHsl);
    }

    private boolean shouldIgnoreColor(Swatch color) {
        return shouldIgnoreColor(color.getRgb(), color.getHsl());
    }

    private boolean shouldIgnoreColor(int rgb, float[] hsl) {
        if (this.mFilters != null && this.mFilters.length > 0) {
            for (Filter isAllowed : this.mFilters) {
                if (!isAllowed.isAllowed(rgb, hsl)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int quantizeFromRgb888(int color) {
        int r = modifyWordWidth(Color.red(color), 8, 5);
        int g = modifyWordWidth(Color.green(color), 8, 5);
        return ((r << 10) | (g << 5)) | modifyWordWidth(Color.blue(color), 8, 5);
    }

    static int approximateToRgb888(int r, int g, int b) {
        return Color.rgb(modifyWordWidth(r, 5, 8), modifyWordWidth(g, 5, 8), modifyWordWidth(b, 5, 8));
    }

    private static int approximateToRgb888(int color) {
        return approximateToRgb888(quantizedRed(color), quantizedGreen(color), quantizedBlue(color));
    }

    static int quantizedRed(int color) {
        return (color >> 10) & 31;
    }

    static int quantizedGreen(int color) {
        return (color >> 5) & 31;
    }

    static int quantizedBlue(int color) {
        return color & 31;
    }

    private static int modifyWordWidth(int value, int currentWidth, int targetWidth) {
        int newValue;
        if (targetWidth > currentWidth) {
            newValue = value << (targetWidth - currentWidth);
        } else {
            newValue = value >> (currentWidth - targetWidth);
        }
        return newValue & ((1 << targetWidth) - 1);
    }
}
