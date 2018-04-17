package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import org.telegram.messenger.AndroidUtilities;

public class PhotoFilterBlurControl extends FrameLayout {
    private static final float BlurInsetProximity = ((float) AndroidUtilities.dp(20.0f));
    private static final float BlurMinimumDifference = 0.02f;
    private static final float BlurMinimumFalloff = 0.1f;
    private static final float BlurViewCenterInset = ((float) AndroidUtilities.dp(30.0f));
    private static final float BlurViewRadiusInset = ((float) AndroidUtilities.dp(30.0f));
    private final int GestureStateBegan = 1;
    private final int GestureStateCancelled = 4;
    private final int GestureStateChanged = 2;
    private final int GestureStateEnded = 3;
    private final int GestureStateFailed = 5;
    private BlurViewActiveControl activeControl;
    private Size actualAreaSize = new Size();
    private float angle;
    private Paint arcPaint = new Paint(1);
    private RectF arcRect = new RectF();
    private Point centerPoint = new Point(0.5f, 0.5f);
    private boolean checkForMoving = true;
    private boolean checkForZooming;
    private PhotoFilterLinearBlurControlDelegate delegate;
    private float falloff = 0.15f;
    private boolean isMoving;
    private boolean isZooming;
    private Paint paint = new Paint(1);
    private float pointerScale = 1.0f;
    private float pointerStartX;
    private float pointerStartY;
    private float size = 0.35f;
    private Point startCenterPoint = new Point();
    private float startDistance;
    private float startPointerDistance;
    private float startRadius;
    private int type;

    private enum BlurViewActiveControl {
        BlurViewActiveControlNone,
        BlurViewActiveControlCenter,
        BlurViewActiveControlInnerRadius,
        BlurViewActiveControlOuterRadius,
        BlurViewActiveControlWholeArea,
        BlurViewActiveControlRotation
    }

    public interface PhotoFilterLinearBlurControlDelegate {
        void valueChanged(Point point, float f, float f2, float f3);
    }

    private void handlePinch(int r1, android.view.MotionEvent r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.PhotoFilterBlurControl.handlePinch(int, android.view.MotionEvent):void
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
        r0 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r1 = 1;
        switch(r8) {
            case 1: goto L_0x0010;
            case 2: goto L_0x001f;
            case 3: goto L_0x0007;
            case 4: goto L_0x0007;
            case 5: goto L_0x0007;
            default: goto L_0x0006;
        };
    L_0x0006:
        goto L_0x0073;
    L_0x0007:
        r0 = org.telegram.ui.Components.PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlNone;
        r7.activeControl = r0;
        r0 = 0;
        r7.setSelected(r0, r1);
        goto L_0x0073;
    L_0x0010:
        r2 = r7.getDistance(r9);
        r7.startPointerDistance = r2;
        r7.pointerScale = r0;
        r2 = org.telegram.ui.Components.PhotoFilterBlurControl.BlurViewActiveControl.BlurViewActiveControlWholeArea;
        r7.activeControl = r2;
        r7.setSelected(r1, r1);
    L_0x001f:
        r1 = r7.getDistance(r9);
        r2 = r7.pointerScale;
        r3 = r7.startPointerDistance;
        r3 = r1 - r3;
        r4 = org.telegram.messenger.AndroidUtilities.density;
        r3 = r3 / r4;
        r4 = 1008981770; // 0x3c23d70a float:0.01 double:4.9850323E-315;
        r3 = r3 * r4;
        r2 = r2 + r3;
        r7.pointerScale = r2;
        r2 = 1036831949; // 0x3dcccccd float:0.1 double:5.122630465E-315;
        r3 = r7.falloff;
        r4 = r7.pointerScale;
        r3 = r3 * r4;
        r2 = java.lang.Math.max(r2, r3);
        r7.falloff = r2;
        r2 = r7.falloff;
        r3 = 1017370378; // 0x3ca3d70a float:0.02 double:5.02647753E-315;
        r2 = r2 + r3;
        r3 = r7.size;
        r4 = r7.pointerScale;
        r3 = r3 * r4;
        r2 = java.lang.Math.max(r2, r3);
        r7.size = r2;
        r7.pointerScale = r0;
        r7.startPointerDistance = r1;
        r7.invalidate();
        r0 = r7.delegate;
        if (r0 == 0) goto L_0x0072;
    L_0x005d:
        r0 = r7.delegate;
        r2 = r7.centerPoint;
        r3 = r7.falloff;
        r4 = r7.size;
        r5 = r7.angle;
        r5 = r7.degreesToRadians(r5);
        r6 = 1070141403; // 0x3fc90fdb float:1.5707964 double:5.287201034E-315;
        r5 = r5 + r6;
        r0.valueChanged(r2, r3, r4, r5);
    L_0x0073:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.PhotoFilterBlurControl.handlePinch(int, android.view.MotionEvent):void");
    }

    public PhotoFilterBlurControl(Context context) {
        super(context);
        setWillNotDraw(false);
        this.paint.setColor(-1);
        this.arcPaint.setColor(-1);
        this.arcPaint.setStrokeWidth((float) AndroidUtilities.dp(2.0f));
        this.arcPaint.setStyle(Style.STROKE);
    }

    public void setType(int blurType) {
        this.type = blurType;
        invalidate();
    }

    public void setDelegate(PhotoFilterLinearBlurControlDelegate delegate) {
        this.delegate = delegate;
    }

    private float getDistance(MotionEvent event) {
        if (event.getPointerCount() != 2) {
            return BlurInsetProximity;
        }
        float x1 = event.getX(0);
        float y1 = event.getY(0);
        float x2 = event.getX(1);
        float y2 = event.getY(1);
        return (float) Math.sqrt((double) (((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2))));
    }

    private float degreesToRadians(float degrees) {
        return (3.1415927f * degrees) / 180.0f;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r23) {
        /*
        r22 = this;
        r0 = r22;
        r1 = r23;
        r2 = r23.getActionMasked();
        r3 = 2;
        r4 = 3;
        r5 = 0;
        r6 = 1;
        switch(r2) {
            case 0: goto L_0x0040;
            case 1: goto L_0x0028;
            case 2: goto L_0x0014;
            case 3: goto L_0x0028;
            case 4: goto L_0x000f;
            case 5: goto L_0x0040;
            case 6: goto L_0x0028;
            default: goto L_0x000f;
        };
    L_0x000f:
        r16 = r2;
        r2 = r6;
        goto L_0x018e;
    L_0x0014:
        r4 = r0.isMoving;
        if (r4 == 0) goto L_0x0020;
    L_0x0018:
        r0.handlePan(r3, r1);
    L_0x001b:
        r16 = r2;
        r2 = r6;
        goto L_0x018e;
    L_0x0020:
        r4 = r0.isZooming;
        if (r4 == 0) goto L_0x001b;
    L_0x0024:
        r0.handlePinch(r3, r1);
        goto L_0x001b;
    L_0x0028:
        r3 = r0.isMoving;
        if (r3 == 0) goto L_0x0032;
    L_0x002c:
        r0.handlePan(r4, r1);
        r0.isMoving = r5;
        goto L_0x003b;
    L_0x0032:
        r3 = r0.isZooming;
        if (r3 == 0) goto L_0x003b;
    L_0x0036:
        r0.handlePinch(r4, r1);
        r0.isZooming = r5;
    L_0x003b:
        r0.checkForMoving = r6;
        r0.checkForZooming = r6;
        goto L_0x001b;
    L_0x0040:
        r7 = r23.getPointerCount();
        if (r7 != r6) goto L_0x015e;
    L_0x0046:
        r3 = r0.checkForMoving;
        if (r3 == 0) goto L_0x000f;
    L_0x004a:
        r3 = r0.isMoving;
        if (r3 != 0) goto L_0x000f;
    L_0x004e:
        r3 = r23.getX();
        r4 = r23.getY();
        r7 = r22.getActualCenterPoint();
        r8 = new org.telegram.ui.Components.Point;
        r9 = r7.x;
        r9 = r3 - r9;
        r10 = r7.y;
        r10 = r4 - r10;
        r8.<init>(r9, r10);
        r9 = r8.x;
        r10 = r8.x;
        r9 = r9 * r10;
        r10 = r8.y;
        r11 = r8.y;
        r10 = r10 * r11;
        r9 = r9 + r10;
        r9 = (double) r9;
        r9 = java.lang.Math.sqrt(r9);
        r9 = (float) r9;
        r10 = r22.getActualInnerRadius();
        r11 = r22.getActualOuterRadius();
        r12 = r11 - r10;
        r12 = java.lang.Math.abs(r12);
        r13 = BlurInsetProximity;
        r12 = (r12 > r13 ? 1 : (r12 == r13 ? 0 : -1));
        if (r12 >= 0) goto L_0x008e;
    L_0x008c:
        r12 = r6;
        goto L_0x008f;
    L_0x008e:
        r12 = r5;
    L_0x008f:
        r13 = 0;
        if (r12 == 0) goto L_0x0094;
    L_0x0092:
        r14 = r13;
        goto L_0x0096;
    L_0x0094:
        r14 = BlurViewRadiusInset;
    L_0x0096:
        if (r12 == 0) goto L_0x0099;
    L_0x0098:
        goto L_0x009b;
    L_0x0099:
        r13 = BlurViewRadiusInset;
    L_0x009b:
        r15 = r0.type;
        if (r15 != 0) goto L_0x0117;
    L_0x009f:
        r15 = r8.x;
        r5 = (double) r15;
        r15 = r0.angle;
        r15 = r0.degreesToRadians(r15);
        r16 = r2;
        r17 = r3;
        r2 = (double) r15;
        r18 = 4609753056924675352; // 0x3ff921fb54442d18 float:3.37028055E12 double:1.5707963267948966;
        r2 = r2 + r18;
        r2 = java.lang.Math.cos(r2);
        r5 = r5 * r2;
        r2 = r8.y;
        r2 = (double) r2;
        r15 = r0.angle;
        r15 = r0.degreesToRadians(r15);
        r20 = r7;
        r21 = r8;
        r7 = (double) r15;
        r7 = r7 + r18;
        r7 = java.lang.Math.sin(r7);
        r2 = r2 * r7;
        r5 = r5 + r2;
        r2 = java.lang.Math.abs(r5);
        r2 = (float) r2;
        r3 = BlurViewCenterInset;
        r3 = (r9 > r3 ? 1 : (r9 == r3 ? 0 : -1));
        if (r3 >= 0) goto L_0x00de;
    L_0x00da:
        r3 = 1;
        r0.isMoving = r3;
        goto L_0x0116;
    L_0x00de:
        r3 = BlurViewRadiusInset;
        r3 = r10 - r3;
        r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r3 <= 0) goto L_0x00f0;
    L_0x00e6:
        r3 = r10 + r14;
        r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r3 >= 0) goto L_0x00f0;
    L_0x00ec:
        r3 = 1;
        r0.isMoving = r3;
        goto L_0x0116;
    L_0x00f0:
        r3 = r11 - r13;
        r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r3 <= 0) goto L_0x0101;
    L_0x00f6:
        r3 = BlurViewRadiusInset;
        r3 = r3 + r11;
        r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r3 >= 0) goto L_0x0101;
    L_0x00fd:
        r3 = 1;
        r0.isMoving = r3;
        goto L_0x0116;
    L_0x0101:
        r3 = BlurViewRadiusInset;
        r3 = r10 - r3;
        r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r3 <= 0) goto L_0x0113;
    L_0x0109:
        r3 = BlurViewRadiusInset;
        r3 = r3 + r11;
        r3 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r3 < 0) goto L_0x0111;
    L_0x0110:
        goto L_0x0113;
    L_0x0111:
        r3 = 1;
        goto L_0x0116;
    L_0x0113:
        r3 = 1;
        r0.isMoving = r3;
    L_0x0116:
        goto L_0x0152;
    L_0x0117:
        r16 = r2;
        r17 = r3;
        r3 = r6;
        r20 = r7;
        r21 = r8;
        r2 = r0.type;
        if (r2 != r3) goto L_0x0152;
    L_0x0124:
        r2 = BlurViewCenterInset;
        r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1));
        if (r2 >= 0) goto L_0x012d;
    L_0x012a:
        r0.isMoving = r3;
        goto L_0x0152;
    L_0x012d:
        r2 = BlurViewRadiusInset;
        r2 = r10 - r2;
        r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x013f;
    L_0x0135:
        r2 = r10 + r14;
        r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1));
        if (r2 >= 0) goto L_0x013f;
    L_0x013b:
        r2 = 1;
        r0.isMoving = r2;
        goto L_0x0153;
    L_0x013f:
        r2 = r11 - r13;
        r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1));
        if (r2 <= 0) goto L_0x0150;
    L_0x0145:
        r2 = BlurViewRadiusInset;
        r2 = r2 + r11;
        r2 = (r9 > r2 ? 1 : (r9 == r2 ? 0 : -1));
        if (r2 >= 0) goto L_0x0150;
    L_0x014c:
        r2 = 1;
        r0.isMoving = r2;
        goto L_0x0153;
    L_0x0150:
        r2 = 1;
        goto L_0x0153;
    L_0x0152:
        r2 = r3;
    L_0x0153:
        r3 = 0;
        r0.checkForMoving = r3;
        r3 = r0.isMoving;
        if (r3 == 0) goto L_0x015d;
    L_0x015a:
        r0.handlePan(r2, r1);
    L_0x015d:
        goto L_0x0182;
    L_0x015e:
        r16 = r2;
        r2 = r0.isMoving;
        if (r2 == 0) goto L_0x016d;
    L_0x0164:
        r0.handlePan(r4, r1);
        r2 = 1;
        r0.checkForMoving = r2;
        r2 = 0;
        r0.isMoving = r2;
    L_0x016d:
        r2 = r23.getPointerCount();
        if (r2 != r3) goto L_0x0184;
    L_0x0173:
        r2 = r0.checkForZooming;
        if (r2 == 0) goto L_0x0182;
    L_0x0177:
        r2 = r0.isZooming;
        if (r2 != 0) goto L_0x0182;
    L_0x017b:
        r2 = 1;
        r0.handlePinch(r2, r1);
        r0.isZooming = r2;
        goto L_0x018e;
    L_0x0182:
        r2 = 1;
        goto L_0x018e;
    L_0x0184:
        r2 = 1;
        r0.handlePinch(r4, r1);
        r0.checkForZooming = r2;
        r3 = 0;
        r0.isZooming = r3;
    L_0x018e:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.PhotoFilterBlurControl.onTouchEvent(android.view.MotionEvent):boolean");
    }

    private void handlePan(int state, MotionEvent event) {
        float locationX = event.getX();
        float locationY = event.getY();
        Point actualCenterPoint = getActualCenterPoint();
        Point delta = new Point(locationX - actualCenterPoint.x, locationY - actualCenterPoint.y);
        float radialDistance = (float) Math.sqrt((double) ((delta.x * delta.x) + (delta.y * delta.y)));
        float shorterSide = this.actualAreaSize.width > this.actualAreaSize.height ? r0.actualAreaSize.height : r0.actualAreaSize.width;
        float innerRadius = r0.falloff * shorterSide;
        float outerRadius = r0.size * shorterSide;
        float distance = (float) Math.abs((((double) delta.x) * Math.cos(((double) degreesToRadians(r0.angle)) + 1.5707963267948966d)) + (((double) delta.y) * Math.sin(((double) degreesToRadians(r0.angle)) + 1.5707963267948966d)));
        float outerRadiusInnerInset = BlurInsetProximity;
        float innerRadiusOuterInset;
        Point point;
        switch (state) {
            case 1:
                r0.pointerStartX = event.getX();
                r0.pointerStartY = event.getY();
                boolean close = Math.abs(outerRadius - innerRadius) < BlurInsetProximity;
                innerRadiusOuterInset = close ? BlurInsetProximity : BlurViewRadiusInset;
                if (!close) {
                    outerRadiusInnerInset = BlurViewRadiusInset;
                }
                float innerRadius2;
                float outerRadius2;
                if (r0.type == 0) {
                    if (radialDistance < BlurViewCenterInset) {
                        r0.activeControl = BlurViewActiveControl.BlurViewActiveControlCenter;
                        r0.startCenterPoint = actualCenterPoint;
                    } else if (distance <= innerRadius - BlurViewRadiusInset || distance >= innerRadius + innerRadiusOuterInset) {
                        innerRadius2 = innerRadius;
                        if (distance <= outerRadius - outerRadiusInnerInset || distance >= outerRadius + BlurViewRadiusInset) {
                            outerRadius2 = outerRadius;
                            if (distance <= innerRadius2 - BlurViewRadiusInset || distance >= BlurViewRadiusInset + outerRadius2) {
                                r0.activeControl = BlurViewActiveControl.BlurViewActiveControlRotation;
                            }
                        } else {
                            r0.activeControl = BlurViewActiveControl.BlurViewActiveControlOuterRadius;
                            r0.startDistance = distance;
                            r0.startRadius = outerRadius;
                        }
                    } else {
                        r0.activeControl = BlurViewActiveControl.BlurViewActiveControlInnerRadius;
                        r0.startDistance = distance;
                        r0.startRadius = innerRadius;
                    }
                } else {
                    innerRadius2 = innerRadius;
                    outerRadius2 = outerRadius;
                    if (r0.type == 1) {
                        if (radialDistance < BlurViewCenterInset) {
                            r0.activeControl = BlurViewActiveControl.BlurViewActiveControlCenter;
                            r0.startCenterPoint = actualCenterPoint;
                        } else if (radialDistance > innerRadius2 - BlurViewRadiusInset && radialDistance < innerRadius2 + innerRadiusOuterInset) {
                            r0.activeControl = BlurViewActiveControl.BlurViewActiveControlInnerRadius;
                            r0.startDistance = radialDistance;
                            r0.startRadius = innerRadius2;
                        } else if (radialDistance > outerRadius2 - outerRadiusInnerInset && radialDistance < BlurViewRadiusInset + outerRadius2) {
                            r0.activeControl = BlurViewActiveControl.BlurViewActiveControlOuterRadius;
                            r0.startDistance = radialDistance;
                            r0.startRadius = outerRadius2;
                        }
                    }
                }
                setSelected(true, true);
                return;
            case 2:
                if (r0.type != 0) {
                    if (r0.type == 1) {
                        switch (r0.activeControl) {
                            case BlurViewActiveControlCenter:
                                delta = locationX - r0.pointerStartX;
                                innerRadiusOuterInset = locationY - r0.pointerStartY;
                                Rect actualArea = new Rect((((float) getWidth()) - r0.actualAreaSize.width) / 2.0f, (((float) getHeight()) - r0.actualAreaSize.height) / 2.0f, r0.actualAreaSize.width, r0.actualAreaSize.height);
                                Point newPoint = new Point(Math.max(actualArea.x, Math.min(actualArea.x + actualArea.width, r0.startCenterPoint.x + delta)), Math.max(actualArea.y, Math.min(actualArea.y + actualArea.height, r0.startCenterPoint.y + innerRadiusOuterInset)));
                                r0.centerPoint = new Point((newPoint.x - actualArea.x) / r0.actualAreaSize.width, ((newPoint.y - actualArea.y) + ((r0.actualAreaSize.width - r0.actualAreaSize.height) / 2.0f)) / r0.actualAreaSize.width);
                                break;
                            case BlurViewActiveControlInnerRadius:
                                r0.falloff = Math.min(Math.max(0.1f, (r0.startRadius + (radialDistance - r0.startDistance)) / shorterSide), r0.size - BlurMinimumDifference);
                                break;
                            case BlurViewActiveControlOuterRadius:
                                r0.size = Math.max(r0.falloff + BlurMinimumDifference, (r0.startRadius + (radialDistance - r0.startDistance)) / shorterSide);
                                break;
                            default:
                                break;
                        }
                    }
                }
                switch (r0.activeControl) {
                    case BlurViewActiveControlCenter:
                        innerRadiusOuterInset = locationX - r0.pointerStartX;
                        outerRadiusInnerInset = locationY - r0.pointerStartY;
                        Rect actualArea2 = new Rect((((float) getWidth()) - r0.actualAreaSize.width) / 2.0f, (((float) getHeight()) - r0.actualAreaSize.height) / 2.0f, r0.actualAreaSize.width, r0.actualAreaSize.height);
                        Point newPoint2 = new Point(Math.max(actualArea2.x, Math.min(actualArea2.x + actualArea2.width, r0.startCenterPoint.x + innerRadiusOuterInset)), Math.max(actualArea2.y, Math.min(actualArea2.y + actualArea2.height, r0.startCenterPoint.y + outerRadiusInnerInset)));
                        point = delta;
                        r0.centerPoint = new Point((newPoint2.x - actualArea2.x) / r0.actualAreaSize.width, ((newPoint2.y - actualArea2.y) + ((r0.actualAreaSize.width - r0.actualAreaSize.height) / 2.0f)) / r0.actualAreaSize.width);
                        break;
                    case BlurViewActiveControlInnerRadius:
                        r0.falloff = Math.min(Math.max(0.1f, (r0.startRadius + (distance - r0.startDistance)) / shorterSide), r0.size - BlurMinimumDifference);
                        break;
                    case BlurViewActiveControlOuterRadius:
                        r0.size = Math.max(r0.falloff + BlurMinimumDifference, (r0.startRadius + (distance - r0.startDistance)) / shorterSide);
                        break;
                    case BlurViewActiveControlRotation:
                        innerRadiusOuterInset = locationX - r0.pointerStartX;
                        float translationY = locationY - r0.pointerStartY;
                        boolean clockwise = false;
                        boolean right = locationX > actualCenterPoint.x;
                        boolean bottom = locationY > actualCenterPoint.y;
                        if (right || bottom) {
                            if (!right || bottom) {
                                if (right && bottom) {
                                    if (Math.abs(translationY) > Math.abs(innerRadiusOuterInset)) {
                                        if (translationY > BlurInsetProximity) {
                                            clockwise = true;
                                        }
                                    } else if (innerRadiusOuterInset < BlurInsetProximity) {
                                        clockwise = true;
                                    }
                                } else if (Math.abs(translationY) > Math.abs(innerRadiusOuterInset)) {
                                    if (translationY < BlurInsetProximity) {
                                        clockwise = true;
                                    }
                                } else if (innerRadiusOuterInset < BlurInsetProximity) {
                                    clockwise = true;
                                }
                            } else if (Math.abs(translationY) > Math.abs(innerRadiusOuterInset)) {
                                if (translationY > BlurInsetProximity) {
                                    clockwise = true;
                                }
                            } else if (innerRadiusOuterInset > BlurInsetProximity) {
                                clockwise = true;
                            }
                        } else if (Math.abs(translationY) > Math.abs(innerRadiusOuterInset)) {
                            if (translationY < BlurInsetProximity) {
                                clockwise = true;
                            }
                        } else if (innerRadiusOuterInset > BlurInsetProximity) {
                            clockwise = true;
                        }
                        r0.angle += ((((float) (((clockwise ? 1 : 0) * 2) - 1)) * ((float) Math.sqrt((double) ((innerRadiusOuterInset * innerRadiusOuterInset) + (translationY * translationY))))) / 3.1415927f) / 1.15f;
                        r0.pointerStartX = locationX;
                        r0.pointerStartY = locationY;
                        break;
                    default:
                        point = delta;
                        break;
                }
                invalidate();
                if (r0.delegate != null) {
                    r0.delegate.valueChanged(r0.centerPoint, r0.falloff, r0.size, degreesToRadians(r0.angle) + 1.5707964f);
                    break;
                }
                break;
            case 3:
            case 4:
            case 5:
                r0.activeControl = BlurViewActiveControl.BlurViewActiveControlNone;
                setSelected(false, true);
                point = delta;
                break;
            default:
                return;
        }
    }

    private void setSelected(boolean selected, boolean animated) {
    }

    public void setActualAreaSize(float width, float height) {
        this.actualAreaSize.width = width;
        this.actualAreaSize.height = height;
    }

    protected void onDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.onDraw(canvas);
        Point centerPoint = getActualCenterPoint();
        float innerRadius = getActualInnerRadius();
        float outerRadius = getActualOuterRadius();
        canvas2.translate(centerPoint.x, centerPoint.y);
        int i;
        Canvas canvas3;
        int i2;
        if (this.type == 0) {
            float f;
            float f2;
            canvas2.rotate(r0.angle);
            float space = (float) AndroidUtilities.dp(6.0f);
            float length = (float) AndroidUtilities.dp(12.0f);
            float thickness = (float) AndroidUtilities.dp(1.5f);
            i = 0;
            while (true) {
                int i3 = i;
                if (i3 >= 30) {
                    break;
                }
                f = thickness - innerRadius;
                int i4 = i3;
                float f3 = f;
                f = thickness;
                canvas2.drawRect((length + space) * ((float) i3), -innerRadius, (((float) i3) * (length + space)) + length, f3, r0.paint);
                canvas2.drawRect(((((float) (-i4)) * (length + space)) - space) - length, -innerRadius, (((float) (-i4)) * (length + space)) - space, f - innerRadius, r0.paint);
                canvas3 = canvas2;
                f2 = innerRadius;
                canvas3.drawRect((length + space) * ((float) i4), f2, length + (((float) i4) * (length + space)), f + innerRadius, r0.paint);
                canvas2.drawRect(((((float) (-i4)) * (length + space)) - space) - length, innerRadius, (((float) (-i4)) * (length + space)) - space, f + innerRadius, r0.paint);
                i = i4 + 1;
                thickness = f;
            }
            f = thickness;
            float length2 = (float) AndroidUtilities.dp(6.0f);
            i2 = 0;
            while (true) {
                int i5 = i2;
                if (i5 >= 64) {
                    break;
                }
                canvas3 = canvas2;
                canvas3.drawRect((length2 + space) * ((float) i5), -outerRadius, length2 + (((float) i5) * (length2 + space)), f - outerRadius, r0.paint);
                canvas2.drawRect(((((float) (-i5)) * (length2 + space)) - space) - length2, -outerRadius, (((float) (-i5)) * (length2 + space)) - space, f - outerRadius, r0.paint);
                canvas3 = canvas2;
                f2 = outerRadius;
                canvas3.drawRect((length2 + space) * ((float) i5), f2, length2 + (((float) i5) * (length2 + space)), f + outerRadius, r0.paint);
                canvas2.drawRect(((((float) (-i5)) * (length2 + space)) - space) - length2, outerRadius, (((float) (-i5)) * (length2 + space)) - space, f + outerRadius, r0.paint);
                i2 = i5 + 1;
            }
        } else if (r0.type == 1) {
            int i6;
            float f4;
            r0.arcRect.set(-innerRadius, -innerRadius, innerRadius, innerRadius);
            i = 0;
            while (true) {
                i6 = i;
                if (i6 >= 22) {
                    break;
                }
                canvas3 = canvas2;
                f4 = 10.2f;
                canvas3.drawArc(r0.arcRect, (6.15f + 10.2f) * ((float) i6), f4, false, r0.arcPaint);
                i = i6 + 1;
            }
            r0.arcRect.set(-outerRadius, -outerRadius, outerRadius, outerRadius);
            i2 = 0;
            while (true) {
                i6 = i2;
                if (i6 >= 64) {
                    break;
                }
                canvas3 = canvas2;
                f4 = 3.6f;
                canvas3.drawArc(r0.arcRect, (2.02f + 3.6f) * ((float) i6), f4, false, r0.arcPaint);
                i2 = i6 + 1;
            }
        }
        canvas2.drawCircle(BlurInsetProximity, BlurInsetProximity, (float) AndroidUtilities.dp(8.0f), r0.paint);
    }

    private Point getActualCenterPoint() {
        return new Point(((((float) getWidth()) - this.actualAreaSize.width) / 2.0f) + (this.centerPoint.x * this.actualAreaSize.width), ((((float) (VERSION.SDK_INT >= 21 ? AndroidUtilities.statusBarHeight : 0)) + ((((float) getHeight()) - this.actualAreaSize.height) / 2.0f)) - ((this.actualAreaSize.width - this.actualAreaSize.height) / 2.0f)) + (this.centerPoint.y * this.actualAreaSize.width));
    }

    private float getActualInnerRadius() {
        return (this.actualAreaSize.width > this.actualAreaSize.height ? this.actualAreaSize.height : this.actualAreaSize.width) * this.falloff;
    }

    private float getActualOuterRadius() {
        return (this.actualAreaSize.width > this.actualAreaSize.height ? this.actualAreaSize.height : this.actualAreaSize.width) * this.size;
    }
}
