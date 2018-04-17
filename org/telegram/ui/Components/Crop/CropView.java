package org.telegram.ui.Components.Crop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.beta.R;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.AlertDialog.Builder;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.Crop.CropGestureDetector.CropGestureListener;

public class CropView extends FrameLayout implements AreaViewListener, CropGestureListener {
    private static final float EPSILON = 1.0E-5f;
    private static final float MAX_SCALE = 30.0f;
    private static final int RESULT_SIDE = 1280;
    private boolean animating = false;
    private CropAreaView areaView;
    private View backView;
    private Bitmap bitmap;
    private float bottomPadding;
    private CropGestureDetector detector;
    private boolean freeform;
    private boolean hasAspectRatioDialog;
    private ImageView imageView;
    private RectF initialAreaRect = new RectF();
    private CropViewListener listener;
    private Matrix presentationMatrix = new Matrix();
    private RectF previousAreaRect = new RectF();
    private float rotationStartScale;
    private CropState state;
    private Matrix tempMatrix = new Matrix();
    private CropRectangle tempRect = new CropRectangle();

    private class CropRectangle {
        float[] coords = new float[8];

        CropRectangle() {
        }

        void setRect(RectF rect) {
            this.coords[0] = rect.left;
            this.coords[1] = rect.top;
            this.coords[2] = rect.right;
            this.coords[3] = rect.top;
            this.coords[4] = rect.right;
            this.coords[5] = rect.bottom;
            this.coords[6] = rect.left;
            this.coords[7] = rect.bottom;
        }

        void applyMatrix(Matrix m) {
            m.mapPoints(this.coords);
        }

        void getRect(RectF rect) {
            rect.set(this.coords[0], this.coords[1], this.coords[2], this.coords[7]);
        }
    }

    private class CropState {
        private float baseRotation;
        private float height;
        private Matrix matrix;
        private float minimumScale;
        private float orientation;
        private float rotation;
        private float scale;
        private float width;
        private float x;
        private float y;

        private CropState(Bitmap bitmap, int bRotation) {
            this.width = (float) bitmap.getWidth();
            this.height = (float) bitmap.getHeight();
            this.x = 0.0f;
            this.y = 0.0f;
            this.scale = 1.0f;
            this.baseRotation = (float) bRotation;
            this.rotation = 0.0f;
            this.matrix = new Matrix();
        }

        private boolean hasChanges() {
            if (Math.abs(this.x) <= CropView.EPSILON && Math.abs(this.y) <= CropView.EPSILON && Math.abs(this.scale - this.minimumScale) <= CropView.EPSILON && Math.abs(this.rotation) <= CropView.EPSILON) {
                if (Math.abs(this.orientation) <= CropView.EPSILON) {
                    return false;
                }
            }
            return true;
        }

        private float getWidth() {
            return this.width;
        }

        private float getHeight() {
            return this.height;
        }

        private float getOrientedWidth() {
            return (this.orientation + this.baseRotation) % 180.0f != 0.0f ? this.height : this.width;
        }

        private float getOrientedHeight() {
            return (this.orientation + this.baseRotation) % 180.0f != 0.0f ? this.width : this.height;
        }

        private void translate(float x, float y) {
            this.x += x;
            this.y += y;
            this.matrix.postTranslate(x, y);
        }

        private float getX() {
            return this.x;
        }

        private float getY() {
            return this.y;
        }

        private void scale(float s, float pivotX, float pivotY) {
            this.scale *= s;
            this.matrix.postScale(s, s, pivotX, pivotY);
        }

        private float getScale() {
            return this.scale;
        }

        private float getMinimumScale() {
            return this.minimumScale;
        }

        private void rotate(float angle, float pivotX, float pivotY) {
            this.rotation += angle;
            this.matrix.postRotate(angle, pivotX, pivotY);
        }

        private float getRotation() {
            return this.rotation;
        }

        private float getOrientation() {
            return this.orientation + this.baseRotation;
        }

        private float getBaseRotation() {
            return this.baseRotation;
        }

        private void reset(CropAreaView areaView, float orient, boolean freeform) {
            this.matrix.reset();
            this.x = 0.0f;
            this.y = 0.0f;
            this.rotation = 0.0f;
            this.orientation = orient;
            float w = (this.orientation + this.baseRotation) % 180.0f != 0.0f ? this.height : this.width;
            float h = (this.orientation + this.baseRotation) % 180.0f != 0.0f ? this.width : this.height;
            if (freeform) {
                this.minimumScale = areaView.getCropWidth() / w;
            } else {
                this.minimumScale = Math.max(areaView.getCropWidth() / w, areaView.getCropHeight() / h);
            }
            this.scale = this.minimumScale;
            this.matrix.postScale(this.scale, this.scale);
        }

        private void getConcatMatrix(Matrix toMatrix) {
            toMatrix.postConcat(this.matrix);
        }

        private Matrix getMatrix() {
            Matrix m = new Matrix();
            m.set(this.matrix);
            return m;
        }
    }

    public interface CropViewListener {
        void onAspectLock(boolean z);

        void onChange(boolean z);
    }

    public CropView(Context context) {
        super(context);
        this.backView = new View(context);
        this.backView.setBackgroundColor(Theme.ACTION_BAR_VIDEO_EDIT_COLOR);
        this.backView.setVisibility(4);
        addView(this.backView);
        this.imageView = new ImageView(context);
        this.imageView.setDrawingCacheEnabled(true);
        this.imageView.setScaleType(ScaleType.MATRIX);
        addView(this.imageView);
        this.detector = new CropGestureDetector(context);
        this.detector.setOnGestureListener(this);
        this.areaView = new CropAreaView(context);
        this.areaView.setListener(this);
        addView(this.areaView);
    }

    public boolean isReady() {
        return (this.detector.isScaling() || this.detector.isDragging() || this.areaView.isDragging()) ? false : true;
    }

    public void setListener(CropViewListener l) {
        this.listener = l;
    }

    public void setBottomPadding(float value) {
        this.bottomPadding = value;
        this.areaView.setBottomPadding(value);
    }

    public void setBitmap(Bitmap b, int rotation, boolean fform) {
        this.bitmap = b;
        this.freeform = fform;
        this.state = new CropState(this.bitmap, rotation);
        this.backView.setVisibility(4);
        this.imageView.setVisibility(4);
        if (fform) {
            this.areaView.setDimVisibility(false);
        }
        this.imageView.getViewTreeObserver().addOnPreDrawListener(new OnPreDrawListener() {
            public boolean onPreDraw() {
                CropView.this.reset();
                CropView.this.imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                return false;
            }
        });
        this.imageView.setImageBitmap(this.bitmap);
    }

    public void willShow() {
        this.areaView.setFrameVisibility(true);
        this.areaView.setDimVisibility(true);
        this.areaView.invalidate();
    }

    public void show() {
        this.backView.setVisibility(0);
        this.imageView.setVisibility(0);
        this.areaView.setDimVisibility(true);
        this.areaView.setFrameVisibility(true);
        this.areaView.invalidate();
    }

    public void hide() {
        this.backView.setVisibility(4);
        this.imageView.setVisibility(4);
        this.areaView.setDimVisibility(false);
        this.areaView.setFrameVisibility(false);
        this.areaView.invalidate();
    }

    public void reset() {
        this.areaView.resetAnimator();
        this.areaView.setBitmap(this.bitmap, this.state.getBaseRotation() % 180.0f != 0.0f, this.freeform);
        this.areaView.setLockedAspectRatio(this.freeform ? 0.0f : 1.0f);
        this.state.reset(this.areaView, 0.0f, this.freeform);
        this.areaView.getCropRect(this.initialAreaRect);
        updateMatrix();
        resetRotationStartScale();
        if (this.listener != null) {
            this.listener.onChange(true);
            this.listener.onAspectLock(false);
        }
    }

    public void updateMatrix() {
        this.presentationMatrix.reset();
        this.presentationMatrix.postTranslate((-this.state.getWidth()) / 2.0f, (-this.state.getHeight()) / 2.0f);
        this.presentationMatrix.postRotate(this.state.getOrientation());
        this.state.getConcatMatrix(this.presentationMatrix);
        this.presentationMatrix.postTranslate(this.areaView.getCropCenterX(), this.areaView.getCropCenterY());
        this.imageView.setImageMatrix(this.presentationMatrix);
    }

    private void fillAreaView(RectF targetRect, boolean allowZoomOut) {
        boolean ensureFit;
        float scale;
        RectF rectF = targetRect;
        final float[] currentScale = new float[1];
        int i = 0;
        currentScale[0] = 1.0f;
        float scale2 = Math.max(targetRect.width() / this.areaView.getCropWidth(), targetRect.height() / this.areaView.getCropHeight());
        float newScale = this.state.getScale() * scale2;
        if (newScale > MAX_SCALE) {
            ensureFit = true;
            scale = MAX_SCALE / r6.state.getScale();
        } else {
            scale = scale2;
            ensureFit = false;
        }
        if (VERSION.SDK_INT >= 21) {
            i = AndroidUtilities.statusBarHeight;
        }
        float y = ((targetRect.centerY() - (((((float) r6.imageView.getHeight()) - r6.bottomPadding) + ((float) i)) / 2.0f)) / r6.areaView.getCropHeight()) * r6.state.getOrientedHeight();
        final float targetScale = scale;
        final boolean animEnsureFit = ensureFit;
        AnonymousClass2 anonymousClass2 = r0;
        final float centerX = ((targetRect.centerX() - ((float) (r6.imageView.getWidth() / 2))) / r6.areaView.getCropWidth()) * r6.state.getOrientedWidth();
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        final ValueAnimator animator2 = y;
        AnonymousClass2 anonymousClass22 = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float deltaScale = (1.0f + ((targetScale - 1.0f) * ((Float) animation.getAnimatedValue()).floatValue())) / currentScale[0];
                float[] fArr = currentScale;
                fArr[0] = fArr[0] * deltaScale;
                CropView.this.state.scale(deltaScale, centerX, animator2);
                CropView.this.updateMatrix();
            }
        };
        animator.addUpdateListener(anonymousClass2);
        animator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (animEnsureFit) {
                    CropView.this.fitContentInBounds(false, false, true);
                }
            }
        });
        r6.areaView.fill(rectF, animator, true);
        r6.initialAreaRect.set(rectF);
    }

    private float fitScale(RectF contentRect, float scale, float ratio) {
        float scaledW = contentRect.width() * ratio;
        float scaledH = contentRect.height() * ratio;
        float scaledX = (contentRect.width() - scaledW) / 2.0f;
        float scaledY = (contentRect.height() - scaledH) / 2.0f;
        contentRect.set(contentRect.left + scaledX, contentRect.top + scaledY, (contentRect.left + scaledX) + scaledW, (contentRect.top + scaledY) + scaledH);
        return scale * ratio;
    }

    private void fitTranslation(RectF contentRect, RectF boundsRect, PointF translation, float radians) {
        RectF rectF = contentRect;
        RectF rectF2 = boundsRect;
        PointF pointF = translation;
        float f = radians;
        float frameLeft = rectF2.left;
        float frameTop = rectF2.top;
        float frameRight = rectF2.right;
        float frameBottom = rectF2.bottom;
        if (rectF.left > frameLeft) {
            frameRight += rectF.left - frameLeft;
            frameLeft = rectF.left;
        }
        if (rectF.top > frameTop) {
            frameBottom += rectF.top - frameTop;
            frameTop = rectF.top;
        }
        if (rectF.right < frameRight) {
            frameLeft += rectF.right - frameRight;
        }
        if (rectF.bottom < frameBottom) {
            frameTop += rectF.bottom - frameBottom;
        }
        float deltaX = boundsRect.centerX() - ((boundsRect.width() / 2.0f) + frameLeft);
        float deltaY = boundsRect.centerY() - ((boundsRect.height() / 2.0f) + frameTop);
        float yCompX = (float) (Math.cos(((double) f) + 1.5707963267948966d) * ((double) deltaY));
        float yCompY = (float) (Math.sin(((double) f) + 1.5707963267948966d) * ((double) deltaY));
        pointF.set((pointF.x + ((float) (Math.sin(1.5707963267948966d - ((double) f)) * ((double) deltaX)))) + yCompX, (pointF.y + ((float) (Math.cos(1.5707963267948966d - ((double) f)) * ((double) deltaX)))) + yCompY);
    }

    public RectF calculateBoundingBox(float w, float h, float rotation) {
        RectF result = new RectF(0.0f, 0.0f, w, h);
        Matrix m = new Matrix();
        m.postRotate(rotation, w / 2.0f, h / 2.0f);
        m.mapRect(result);
        return result;
    }

    public float scaleWidthToMaxSize(RectF sizeRect, RectF maxSizeRect) {
        float w = maxSizeRect.width();
        if (((float) Math.floor((double) ((sizeRect.height() * w) / sizeRect.width()))) <= maxSizeRect.height()) {
            return w;
        }
        return (float) Math.floor((double) ((sizeRect.width() * maxSizeRect.height()) / sizeRect.height()));
    }

    private void fitContentInBounds(boolean allowScale, boolean maximize, boolean animated) {
        fitContentInBounds(allowScale, maximize, animated, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void fitContentInBounds(boolean r30, boolean r31, boolean r32, boolean r33) {
        /*
        r29 = this;
        r10 = r29;
        r0 = r10.areaView;
        r11 = r0.getCropWidth();
        r0 = r10.areaView;
        r12 = r0.getCropHeight();
        r0 = r10.state;
        r13 = r0.getOrientedWidth();
        r0 = r10.state;
        r14 = r0.getOrientedHeight();
        r0 = r10.state;
        r15 = r0.getRotation();
        r0 = (double) r15;
        r0 = java.lang.Math.toRadians(r0);
        r9 = (float) r0;
        r8 = r10.calculateBoundingBox(r11, r12, r15);
        r0 = new android.graphics.RectF;
        r1 = 0;
        r0.<init>(r1, r1, r13, r14);
        r7 = r0;
        r0 = r11 - r13;
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r16 = r0 / r2;
        r0 = r12 - r14;
        r17 = r0 / r2;
        r0 = r10.state;
        r6 = r0.getScale();
        r0 = r10.tempRect;
        r0.setRect(r7);
        r0 = r10.state;
        r5 = r0.getMatrix();
        r0 = r16 / r6;
        r3 = r17 / r6;
        r5.preTranslate(r0, r3);
        r0 = r10.tempMatrix;
        r0.reset();
        r0 = r10.tempMatrix;
        r3 = r7.centerX();
        r4 = r7.centerY();
        r0.setTranslate(r3, r4);
        r0 = r10.tempMatrix;
        r3 = r10.tempMatrix;
        r0.setConcat(r3, r5);
        r0 = r10.tempMatrix;
        r3 = r7.centerX();
        r3 = -r3;
        r4 = r7.centerY();
        r4 = -r4;
        r0.preTranslate(r3, r4);
        r0 = r10.tempRect;
        r3 = r10.tempMatrix;
        r0.applyMatrix(r3);
        r0 = r10.tempMatrix;
        r0.reset();
        r0 = r10.tempMatrix;
        r3 = -r15;
        r4 = r13 / r2;
        r2 = r14 / r2;
        r0.preRotate(r3, r4, r2);
        r0 = r10.tempRect;
        r2 = r10.tempMatrix;
        r0.applyMatrix(r2);
        r0 = r10.tempRect;
        r0.getRect(r7);
        r0 = new android.graphics.PointF;
        r2 = r10.state;
        r2 = r2.getX();
        r3 = r10.state;
        r3 = r3.getY();
        r0.<init>(r2, r3);
        r4 = r0;
        r0 = r6;
        r2 = r7.contains(r8);
        if (r2 != 0) goto L_0x00e1;
    L_0x00b6:
        if (r30 == 0) goto L_0x00dd;
    L_0x00b8:
        r2 = r8.width();
        r3 = r7.width();
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 > 0) goto L_0x00d0;
    L_0x00c4:
        r2 = r8.height();
        r3 = r7.height();
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 <= 0) goto L_0x00dd;
    L_0x00d0:
        r2 = r8.width();
        r3 = r10.scaleWidthToMaxSize(r8, r7);
        r2 = r2 / r3;
        r0 = r10.fitScale(r7, r6, r2);
    L_0x00dd:
        r10.fitTranslation(r7, r8, r4, r9);
        goto L_0x0108;
    L_0x00e1:
        if (r31 == 0) goto L_0x0108;
    L_0x00e3:
        r2 = r10.rotationStartScale;
        r2 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1));
        if (r2 <= 0) goto L_0x0108;
    L_0x00e9:
        r2 = r8.width();
        r3 = r10.scaleWidthToMaxSize(r8, r7);
        r2 = r2 / r3;
        r3 = r10.state;
        r3 = r3.getScale();
        r3 = r3 * r2;
        r1 = r10.rotationStartScale;
        r1 = (r3 > r1 ? 1 : (r3 == r1 ? 0 : -1));
        if (r1 >= 0) goto L_0x0101;
    L_0x00ff:
        r2 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0101:
        r0 = r10.fitScale(r7, r6, r2);
        r10.fitTranslation(r7, r8, r4, r9);
    L_0x0108:
        r19 = r0;
        r0 = r4.x;
        r1 = r10.state;
        r1 = r1.getX();
        r2 = r0 - r1;
        r0 = r4.y;
        r1 = r10.state;
        r1 = r1.getY();
        r1 = r0 - r1;
        if (r32 == 0) goto L_0x01ae;
    L_0x0120:
        r18 = r19 / r6;
        r0 = r2;
        r3 = r1;
        r20 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r21 = r1;
        r1 = r18 - r20;
        r1 = java.lang.Math.abs(r1);
        r20 = 925353388; // 0x3727c5ac float:1.0E-5 double:4.571853193E-315;
        r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1));
        if (r1 >= 0) goto L_0x0146;
    L_0x0135:
        r1 = java.lang.Math.abs(r0);
        r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1));
        if (r1 >= 0) goto L_0x0146;
    L_0x013d:
        r1 = java.lang.Math.abs(r3);
        r1 = (r1 > r20 ? 1 : (r1 == r20 ? 0 : -1));
        if (r1 >= 0) goto L_0x0146;
    L_0x0145:
        return;
    L_0x0146:
        r1 = 1;
        r10.animating = r1;
        r1 = 3;
        r1 = new float[r1];
        r1 = {1065353216, 0, 0};
        r20 = r3;
        r3 = r1;
        r1 = 2;
        r1 = new float[r1];
        r1 = {0, 1065353216};
        r1 = android.animation.ValueAnimator.ofFloat(r1);
        r22 = r6;
        r6 = new org.telegram.ui.Components.Crop.CropView$4;
        r23 = r0;
        r0 = r6;
        r24 = r11;
        r25 = r12;
        r11 = r21;
        r12 = r1;
        r1 = r10;
        r26 = r13;
        r13 = r2;
        r2 = r23;
        r21 = r4;
        r4 = r20;
        r27 = r5;
        r5 = r18;
        r0.<init>(r2, r3, r4, r5);
        r12.addUpdateListener(r6);
        r0 = new org.telegram.ui.Components.Crop.CropView$5;
        r4 = r0;
        r5 = r10;
        r1 = r22;
        r6 = r33;
        r2 = r7;
        r7 = r30;
        r22 = r8;
        r8 = r31;
        r28 = r9;
        r9 = r32;
        r4.<init>(r6, r7, r8, r9);
        r12.addListener(r0);
        r0 = r10.areaView;
        r0 = r0.getInterpolator();
        r12.setInterpolator(r0);
        if (r33 == 0) goto L_0x01a5;
    L_0x01a2:
        r4 = 100;
        goto L_0x01a7;
    L_0x01a5:
        r4 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
    L_0x01a7:
        r12.setDuration(r4);
        r12.start();
        goto L_0x01d0;
    L_0x01ae:
        r21 = r4;
        r27 = r5;
        r22 = r8;
        r28 = r9;
        r24 = r11;
        r25 = r12;
        r26 = r13;
        r11 = r1;
        r13 = r2;
        r1 = r6;
        r2 = r7;
        r3 = r10.state;
        r3.translate(r13, r11);
        r3 = r10.state;
        r4 = r19 / r1;
        r5 = 0;
        r3.scale(r4, r5, r5);
        r29.updateMatrix();
    L_0x01d0:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.Crop.CropView.fitContentInBounds(boolean, boolean, boolean, boolean):void");
    }

    public void rotate90Degrees() {
        this.areaView.resetAnimator();
        resetRotationStartScale();
        float orientation = ((this.state.getOrientation() - this.state.getBaseRotation()) - 90.0f) % 360.0f;
        boolean fform = this.freeform;
        boolean z = false;
        if (!this.freeform || this.areaView.getLockAspectRatio() <= 0.0f) {
            this.areaView.setBitmap(this.bitmap, (this.state.getBaseRotation() + orientation) % 180.0f != 0.0f, this.freeform);
        } else {
            this.areaView.setLockedAspectRatio(1.0f / this.areaView.getLockAspectRatio());
            this.areaView.setActualRect(this.areaView.getLockAspectRatio());
            fform = false;
        }
        this.state.reset(this.areaView, orientation, fform);
        updateMatrix();
        if (this.listener != null) {
            CropViewListener cropViewListener = this.listener;
            if (orientation == 0.0f && this.areaView.getLockAspectRatio() == 0.0f) {
                z = true;
            }
            cropViewListener.onChange(z);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (this.animating) {
            return true;
        }
        boolean result = false;
        if (this.areaView.onTouchEvent(event)) {
            return true;
        }
        int action = event.getAction();
        if (action != 3) {
            switch (action) {
                case 0:
                    onScrollChangeBegan();
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }
        onScrollChangeEnded();
        try {
            result = this.detector.onTouchEvent(event);
        } catch (Exception e) {
        }
        return result;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    public void onAreaChangeBegan() {
        this.areaView.getCropRect(this.previousAreaRect);
        resetRotationStartScale();
        if (this.listener != null) {
            this.listener.onChange(false);
        }
    }

    public void onAreaChange() {
        this.areaView.setGridType(GridType.MAJOR, false);
        this.state.translate(this.previousAreaRect.centerX() - this.areaView.getCropCenterX(), this.previousAreaRect.centerY() - this.areaView.getCropCenterY());
        updateMatrix();
        this.areaView.getCropRect(this.previousAreaRect);
        fitContentInBounds(true, false, false);
    }

    public void onAreaChangeEnded() {
        this.areaView.setGridType(GridType.NONE, true);
        fillAreaView(this.areaView.getTargetRectToFill(), false);
    }

    public void onDrag(float dx, float dy) {
        if (!this.animating) {
            this.state.translate(dx, dy);
            updateMatrix();
        }
    }

    public void onFling(float startX, float startY, float velocityX, float velocityY) {
    }

    public void onScrollChangeBegan() {
        if (!this.animating) {
            this.areaView.setGridType(GridType.MAJOR, true);
            resetRotationStartScale();
            if (this.listener != null) {
                this.listener.onChange(false);
            }
        }
    }

    public void onScrollChangeEnded() {
        this.areaView.setGridType(GridType.NONE, true);
        fitContentInBounds(true, false, true);
    }

    public void onScale(float scale, float x, float y) {
        if (!this.animating) {
            if (this.state.getScale() * scale > MAX_SCALE) {
                scale = MAX_SCALE / this.state.getScale();
            }
            this.state.scale(scale, ((x - ((float) (this.imageView.getWidth() / 2))) / this.areaView.getCropWidth()) * this.state.getOrientedWidth(), ((y - (((((float) this.imageView.getHeight()) - this.bottomPadding) - ((float) (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0.0f))) / 2.0f)) / this.areaView.getCropHeight()) * this.state.getOrientedHeight());
            updateMatrix();
        }
    }

    public void onRotationBegan() {
        this.areaView.setGridType(GridType.MINOR, false);
        if (this.rotationStartScale < EPSILON) {
            this.rotationStartScale = this.state.getScale();
        }
    }

    public void onRotationEnded() {
        this.areaView.setGridType(GridType.NONE, true);
    }

    private void resetRotationStartScale() {
        this.rotationStartScale = 0.0f;
    }

    public void setRotation(float angle) {
        this.state.rotate(angle - this.state.getRotation(), 0.0f, 0.0f);
        fitContentInBounds(true, true, false);
    }

    public Bitmap getResult() {
        if (!this.state.hasChanges() && this.state.getBaseRotation() < EPSILON && this.freeform) {
            return this.bitmap;
        }
        RectF cropRect = new RectF();
        this.areaView.getCropRect(cropRect);
        int width = (int) Math.ceil((double) scaleWidthToMaxSize(cropRect, new RectF(0.0f, 0.0f, 1280.0f, 1280.0f)));
        int height = (int) Math.ceil((double) (((float) width) / this.areaView.getAspectRatio()));
        Bitmap resultBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Matrix matrix = new Matrix();
        matrix.postTranslate((-this.state.getWidth()) / 2.0f, (-this.state.getHeight()) / 2.0f);
        matrix.postRotate(this.state.getOrientation());
        this.state.getConcatMatrix(matrix);
        float scale = ((float) width) / this.areaView.getCropWidth();
        matrix.postScale(scale, scale);
        matrix.postTranslate((float) (width / 2), (float) (height / 2));
        new Canvas(resultBitmap).drawBitmap(this.bitmap, matrix, new Paint(2));
        return resultBitmap;
    }

    private void setLockedAspectRatio(float aspectRatio) {
        this.areaView.setLockedAspectRatio(aspectRatio);
        RectF targetRect = new RectF();
        this.areaView.calculateRect(targetRect, aspectRatio);
        fillAreaView(targetRect, true);
        if (this.listener != null) {
            this.listener.onChange(false);
            this.listener.onAspectLock(true);
        }
    }

    public void showAspectRatioDialog() {
        if (this.areaView.getLockAspectRatio() > 0.0f) {
            this.areaView.setLockedAspectRatio(0.0f);
            if (this.listener != null) {
                this.listener.onAspectLock(false);
            }
        } else if (!this.hasAspectRatioDialog) {
            this.hasAspectRatioDialog = true;
            actions = new String[8];
            ratios = new Integer[6][];
            ratios[0] = new Integer[]{Integer.valueOf(3), Integer.valueOf(2)};
            ratios[1] = new Integer[]{Integer.valueOf(5), Integer.valueOf(3)};
            ratios[2] = new Integer[]{Integer.valueOf(4), Integer.valueOf(3)};
            ratios[3] = new Integer[]{Integer.valueOf(5), Integer.valueOf(4)};
            ratios[4] = new Integer[]{Integer.valueOf(7), Integer.valueOf(5)};
            ratios[5] = new Integer[]{Integer.valueOf(16), Integer.valueOf(9)};
            actions[0] = LocaleController.getString("CropOriginal", R.string.CropOriginal);
            actions[1] = LocaleController.getString("CropSquare", R.string.CropSquare);
            int i = 2;
            for (Integer[] ratioPair : ratios) {
                if (this.areaView.getAspectRatio() > 1.0f) {
                    actions[i] = String.format("%d:%d", new Object[]{ratioPair[0], ratioPair[1]});
                } else {
                    actions[i] = String.format("%d:%d", new Object[]{ratioPair[1], ratioPair[0]});
                }
                i++;
            }
            AlertDialog dialog = new Builder(getContext()).setItems(actions, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    CropView.this.hasAspectRatioDialog = false;
                    switch (which) {
                        case 0:
                            CropView.this.setLockedAspectRatio((CropView.this.state.getBaseRotation() % 180.0f != 0.0f ? CropView.this.state.getHeight() : CropView.this.state.getWidth()) / (CropView.this.state.getBaseRotation() % 180.0f != 0.0f ? CropView.this.state.getWidth() : CropView.this.state.getHeight()));
                            return;
                        case 1:
                            CropView.this.setLockedAspectRatio(1.0f);
                            return;
                        default:
                            Integer[] ratioPair = ratios[which - 2];
                            if (CropView.this.areaView.getAspectRatio() > 1.0f) {
                                CropView.this.setLockedAspectRatio(((float) ratioPair[0].intValue()) / ((float) ratioPair[1].intValue()));
                                return;
                            } else {
                                CropView.this.setLockedAspectRatio(((float) ratioPair[1].intValue()) / ((float) ratioPair[0].intValue()));
                                return;
                            }
                    }
                }
            }).create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    CropView.this.hasAspectRatioDialog = false;
                }
            });
            dialog.show();
        }
    }

    public void updateLayout() {
        float w = this.areaView.getCropWidth();
        this.areaView.calculateRect(this.initialAreaRect, this.state.getWidth() / this.state.getHeight());
        this.areaView.setActualRect(this.areaView.getAspectRatio());
        this.areaView.getCropRect(this.previousAreaRect);
        this.state.scale(this.areaView.getCropWidth() / w, 0.0f, 0.0f);
        updateMatrix();
    }

    public float getCropLeft() {
        return this.areaView.getCropLeft();
    }

    public float getCropTop() {
        return this.areaView.getCropTop();
    }

    public float getCropWidth() {
        return this.areaView.getCropWidth();
    }

    public float getCropHeight() {
        return this.areaView.getCropHeight();
    }
}
