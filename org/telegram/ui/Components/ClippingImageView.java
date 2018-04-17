package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.BitmapShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.support.annotation.Keep;
import android.view.View;
import org.telegram.messenger.ImageReceiver.BitmapHolder;

public class ClippingImageView extends View {
    private float animationProgress;
    private float[][] animationValues;
    private RectF bitmapRect;
    private BitmapShader bitmapShader;
    private BitmapHolder bmp;
    private int clipBottom;
    private int clipLeft;
    private int clipRight;
    private int clipTop;
    private RectF drawRect;
    private Matrix matrix;
    private boolean needRadius;
    private int orientation;
    private Paint paint = new Paint();
    private int radius;
    private Paint roundPaint;
    private RectF roundRect;
    private Matrix shaderMatrix;

    public void onDraw(android.graphics.Canvas r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.ClippingImageView.onDraw(android.graphics.Canvas):void
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
        r0 = r13.getVisibility();
        if (r0 == 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = r13.bmp;
        if (r0 == 0) goto L_0x0209;
    L_0x000b:
        r0 = r13.bmp;
        r0 = r0.isRecycled();
        if (r0 != 0) goto L_0x0209;
    L_0x0013:
        r0 = r13.getScaleY();
        r14.save();
        r1 = r13.needRadius;
        r2 = 270; // 0x10e float:3.78E-43 double:1.334E-321;
        r3 = 90;
        r4 = 0;
        if (r1 == 0) goto L_0x011a;
    L_0x0023:
        r1 = r13.shaderMatrix;
        r1.reset();
        r1 = r13.roundRect;
        r5 = r13.getWidth();
        r5 = (float) r5;
        r6 = r13.getHeight();
        r6 = (float) r6;
        r1.set(r4, r4, r5, r6);
        r1 = r13.orientation;
        r1 = r1 % 360;
        if (r1 == r3) goto L_0x0051;
    L_0x003d:
        r1 = r13.orientation;
        r1 = r1 % 360;
        if (r1 != r2) goto L_0x0044;
    L_0x0043:
        goto L_0x0051;
    L_0x0044:
        r1 = r13.bmp;
        r1 = r1.getWidth();
        r2 = r13.bmp;
        r2 = r2.getHeight();
        goto L_0x005d;
    L_0x0051:
        r1 = r13.bmp;
        r1 = r1.getHeight();
        r2 = r13.bmp;
        r2 = r2.getWidth();
        r3 = r13.getWidth();
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r3 == 0) goto L_0x006e;
        r3 = (float) r1;
        r6 = r13.getWidth();
        r6 = (float) r6;
        r3 = r3 / r6;
        goto L_0x006f;
        r3 = r5;
        r6 = r13.getHeight();
        if (r6 == 0) goto L_0x007d;
        r5 = (float) r2;
        r6 = r13.getHeight();
        r6 = (float) r6;
        r5 = r5 / r6;
        r6 = java.lang.Math.min(r3, r5);
        r7 = r3 - r5;
        r7 = java.lang.Math.abs(r7);
        r8 = 925353388; // 0x3727c5ac float:1.0E-5 double:4.571853193E-315;
        r7 = (r7 > r8 ? 1 : (r7 == r8 ? 0 : -1));
        if (r7 <= 0) goto L_0x00c5;
        r4 = r13.getWidth();
        r4 = (float) r4;
        r4 = r4 * r6;
        r7 = (double) r4;
        r7 = java.lang.Math.floor(r7);
        r4 = (int) r7;
        r7 = r13.getHeight();
        r7 = (float) r7;
        r7 = r7 * r6;
        r7 = (double) r7;
        r7 = java.lang.Math.floor(r7);
        r7 = (int) r7;
        r8 = r13.bitmapRect;
        r9 = r1 - r4;
        r9 = r9 / 2;
        r9 = (float) r9;
        r10 = r2 - r7;
        r10 = r10 / 2;
        r10 = (float) r10;
        r11 = (float) r4;
        r12 = (float) r7;
        r8.set(r9, r10, r11, r12);
        r8 = r13.shaderMatrix;
        r9 = r13.bitmapRect;
        r10 = r13.roundRect;
        r11 = r13.orientation;
        r12 = android.graphics.Matrix.ScaleToFit.START;
        org.telegram.messenger.AndroidUtilities.setRectToRect(r8, r9, r10, r11, r12);
        goto L_0x00e5;
        r7 = r13.bitmapRect;
        r8 = r13.bmp;
        r8 = r8.getWidth();
        r8 = (float) r8;
        r9 = r13.bmp;
        r9 = r9.getHeight();
        r9 = (float) r9;
        r7.set(r4, r4, r8, r9);
        r4 = r13.shaderMatrix;
        r7 = r13.bitmapRect;
        r8 = r13.roundRect;
        r9 = r13.orientation;
        r10 = android.graphics.Matrix.ScaleToFit.FILL;
        org.telegram.messenger.AndroidUtilities.setRectToRect(r4, r7, r8, r9, r10);
        r4 = r13.bitmapShader;
        r7 = r13.shaderMatrix;
        r4.setLocalMatrix(r7);
        r4 = r13.clipLeft;
        r4 = (float) r4;
        r4 = r4 / r0;
        r7 = r13.clipTop;
        r7 = (float) r7;
        r7 = r7 / r0;
        r8 = r13.getWidth();
        r8 = (float) r8;
        r9 = r13.clipRight;
        r9 = (float) r9;
        r9 = r9 / r0;
        r8 = r8 - r9;
        r9 = r13.getHeight();
        r9 = (float) r9;
        r10 = r13.clipBottom;
        r10 = (float) r10;
        r10 = r10 / r0;
        r9 = r9 - r10;
        r14.clipRect(r4, r7, r8, r9);
        r4 = r13.roundRect;
        r7 = r13.radius;
        r7 = (float) r7;
        r8 = r13.radius;
        r8 = (float) r8;
        r9 = r13.roundPaint;
        r14.drawRoundRect(r4, r7, r8, r9);
        goto L_0x0206;
    L_0x011a:
        r1 = r13.orientation;
        if (r1 == r3) goto L_0x018e;
        r1 = r13.orientation;
        if (r1 != r2) goto L_0x0123;
        goto L_0x018e;
        r1 = r13.orientation;
        r2 = 180; // 0xb4 float:2.52E-43 double:8.9E-322;
        if (r1 != r2) goto L_0x0173;
        r1 = r13.drawRect;
        r2 = r13.getWidth();
        r2 = -r2;
        r2 = r2 / 2;
        r2 = (float) r2;
        r3 = r13.getHeight();
        r3 = -r3;
        r3 = r3 / 2;
        r3 = (float) r3;
        r5 = r13.getWidth();
        r5 = r5 / 2;
        r5 = (float) r5;
        r6 = r13.getHeight();
        r6 = r6 / 2;
        r6 = (float) r6;
        r1.set(r2, r3, r5, r6);
        r1 = r13.matrix;
        r2 = r13.bitmapRect;
        r3 = r13.drawRect;
        r5 = android.graphics.Matrix.ScaleToFit.FILL;
        r1.setRectToRect(r2, r3, r5);
        r1 = r13.matrix;
        r2 = r13.orientation;
        r2 = (float) r2;
        r1.postRotate(r2, r4, r4);
        r1 = r13.matrix;
        r2 = r13.getWidth();
        r2 = r2 / 2;
        r2 = (float) r2;
        r3 = r13.getHeight();
        r3 = r3 / 2;
        r3 = (float) r3;
        r1.postTranslate(r2, r3);
        goto L_0x01d7;
        r1 = r13.drawRect;
        r2 = r13.getWidth();
        r2 = (float) r2;
        r3 = r13.getHeight();
        r3 = (float) r3;
        r1.set(r4, r4, r2, r3);
        r1 = r13.matrix;
        r2 = r13.bitmapRect;
        r3 = r13.drawRect;
        r4 = android.graphics.Matrix.ScaleToFit.FILL;
        r1.setRectToRect(r2, r3, r4);
        goto L_0x01d7;
        r1 = r13.drawRect;
        r2 = r13.getHeight();
        r2 = -r2;
        r2 = r2 / 2;
        r2 = (float) r2;
        r3 = r13.getWidth();
        r3 = -r3;
        r3 = r3 / 2;
        r3 = (float) r3;
        r5 = r13.getHeight();
        r5 = r5 / 2;
        r5 = (float) r5;
        r6 = r13.getWidth();
        r6 = r6 / 2;
        r6 = (float) r6;
        r1.set(r2, r3, r5, r6);
        r1 = r13.matrix;
        r2 = r13.bitmapRect;
        r3 = r13.drawRect;
        r5 = android.graphics.Matrix.ScaleToFit.FILL;
        r1.setRectToRect(r2, r3, r5);
        r1 = r13.matrix;
        r2 = r13.orientation;
        r2 = (float) r2;
        r1.postRotate(r2, r4, r4);
        r1 = r13.matrix;
        r2 = r13.getWidth();
        r2 = r2 / 2;
        r2 = (float) r2;
        r3 = r13.getHeight();
        r3 = r3 / 2;
        r3 = (float) r3;
        r1.postTranslate(r2, r3);
        r1 = r13.clipLeft;
        r1 = (float) r1;
        r1 = r1 / r0;
        r2 = r13.clipTop;
        r2 = (float) r2;
        r2 = r2 / r0;
        r3 = r13.getWidth();
        r3 = (float) r3;
        r4 = r13.clipRight;
        r4 = (float) r4;
        r4 = r4 / r0;
        r3 = r3 - r4;
        r4 = r13.getHeight();
        r4 = (float) r4;
        r5 = r13.clipBottom;
        r5 = (float) r5;
        r5 = r5 / r0;
        r4 = r4 - r5;
        r14.clipRect(r1, r2, r3, r4);
        r1 = r13.bmp;	 Catch:{ Exception -> 0x0202 }
        r1 = r1.bitmap;	 Catch:{ Exception -> 0x0202 }
        r2 = r13.matrix;	 Catch:{ Exception -> 0x0202 }
        r3 = r13.paint;	 Catch:{ Exception -> 0x0202 }
        r14.drawBitmap(r1, r2, r3);	 Catch:{ Exception -> 0x0202 }
        goto L_0x0206;
    L_0x0202:
        r1 = move-exception;
        org.telegram.messenger.FileLog.e(r1);
        r14.restore();
    L_0x0209:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ClippingImageView.onDraw(android.graphics.Canvas):void");
    }

    public ClippingImageView(Context context) {
        super(context);
        this.paint.setFilterBitmap(true);
        this.matrix = new Matrix();
        this.drawRect = new RectF();
        this.bitmapRect = new RectF();
        this.roundPaint = new Paint(1);
        this.roundRect = new RectF();
        this.shaderMatrix = new Matrix();
    }

    public void setAnimationValues(float[][] values) {
        this.animationValues = values;
    }

    @Keep
    public float getAnimationProgress() {
        return this.animationProgress;
    }

    @Keep
    public void setAnimationProgress(float progress) {
        this.animationProgress = progress;
        setScaleX(this.animationValues[0][0] + ((this.animationValues[1][0] - this.animationValues[0][0]) * this.animationProgress));
        setScaleY(this.animationValues[0][1] + ((this.animationValues[1][1] - this.animationValues[0][1]) * this.animationProgress));
        setTranslationX(this.animationValues[0][2] + ((this.animationValues[1][2] - this.animationValues[0][2]) * this.animationProgress));
        setTranslationY(this.animationValues[0][3] + ((this.animationValues[1][3] - this.animationValues[0][3]) * this.animationProgress));
        setClipHorizontal((int) (this.animationValues[0][4] + ((this.animationValues[1][4] - this.animationValues[0][4]) * this.animationProgress)));
        setClipTop((int) (this.animationValues[0][5] + ((this.animationValues[1][5] - this.animationValues[0][5]) * this.animationProgress)));
        setClipBottom((int) (this.animationValues[0][6] + ((this.animationValues[1][6] - this.animationValues[0][6]) * this.animationProgress)));
        setRadius((int) (this.animationValues[0][7] + ((this.animationValues[1][7] - this.animationValues[0][7]) * this.animationProgress)));
        invalidate();
    }

    public int getClipBottom() {
        return this.clipBottom;
    }

    public int getClipHorizontal() {
        return this.clipRight;
    }

    public int getClipLeft() {
        return this.clipLeft;
    }

    public int getClipRight() {
        return this.clipRight;
    }

    public int getClipTop() {
        return this.clipTop;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setClipBottom(int value) {
        this.clipBottom = value;
        invalidate();
    }

    public void setClipHorizontal(int value) {
        this.clipRight = value;
        this.clipLeft = value;
        invalidate();
    }

    public void setClipLeft(int value) {
        this.clipLeft = value;
        invalidate();
    }

    public void setClipRight(int value) {
        this.clipRight = value;
        invalidate();
    }

    public void setClipTop(int value) {
        this.clipTop = value;
        invalidate();
    }

    public void setClipVertical(int value) {
        this.clipBottom = value;
        this.clipTop = value;
        invalidate();
    }

    public void setOrientation(int angle) {
        this.orientation = angle;
    }

    public void setImageBitmap(BitmapHolder bitmap) {
        if (this.bmp != null) {
            this.bmp.release();
            this.bitmapShader = null;
        }
        this.bmp = bitmap;
        if (bitmap != null) {
            this.bitmapRect.set(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight());
            if (this.needRadius) {
                this.bitmapShader = new BitmapShader(bitmap.bitmap, TileMode.CLAMP, TileMode.CLAMP);
                this.roundPaint.setShader(this.bitmapShader);
            }
        }
        invalidate();
    }

    public void setNeedRadius(boolean value) {
        this.needRadius = value;
    }

    public void setRadius(int value) {
        this.radius = value;
    }
}
