package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.PhotoFilterView.CurvesToolValue;
import org.telegram.ui.Components.PhotoFilterView.CurvesValue;

public class PhotoFilterCurvesControl extends View {
    private static final int CurvesSegmentBlacks = 1;
    private static final int CurvesSegmentHighlights = 4;
    private static final int CurvesSegmentMidtones = 3;
    private static final int CurvesSegmentNone = 0;
    private static final int CurvesSegmentShadows = 2;
    private static final int CurvesSegmentWhites = 5;
    private static final int GestureStateBegan = 1;
    private static final int GestureStateCancelled = 4;
    private static final int GestureStateChanged = 2;
    private static final int GestureStateEnded = 3;
    private static final int GestureStateFailed = 5;
    private int activeSegment = 0;
    private Rect actualArea = new Rect();
    private boolean checkForMoving = true;
    private CurvesToolValue curveValue;
    private PhotoFilterCurvesControlDelegate delegate;
    private boolean isMoving;
    private float lastX;
    private float lastY;
    private Paint paint = new Paint(1);
    private Paint paintCurve = new Paint(1);
    private Paint paintDash = new Paint(1);
    private Path path = new Path();
    private TextPaint textPaint = new TextPaint(1);

    public interface PhotoFilterCurvesControlDelegate {
        void valueChanged();
    }

    @android.annotation.SuppressLint({"DrawAllocation"})
    protected void onDraw(android.graphics.Canvas r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.PhotoFilterCurvesControl.onDraw(android.graphics.Canvas):void
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
        r0 = r11.actualArea;
        r0 = r0.width;
        r1 = 1084227584; // 0x40a00000 float:5.0 double:5.356796015E-315;
        r0 = r0 / r1;
        r1 = 0;
        r2 = r1;
    L_0x0009:
        r3 = 4;
        if (r2 >= r3) goto L_0x0035;
    L_0x000c:
        r3 = r11.actualArea;
        r3 = r3.x;
        r3 = r3 + r0;
        r4 = (float) r2;
        r4 = r4 * r0;
        r6 = r3 + r4;
        r3 = r11.actualArea;
        r7 = r3.y;
        r3 = r11.actualArea;
        r3 = r3.x;
        r3 = r3 + r0;
        r4 = (float) r2;
        r4 = r4 * r0;
        r8 = r3 + r4;
        r3 = r11.actualArea;
        r3 = r3.y;
        r4 = r11.actualArea;
        r4 = r4.height;
        r9 = r3 + r4;
        r10 = r11.paint;
        r5 = r12;
        r5.drawLine(r6, r7, r8, r9, r10);
        r2 = r2 + 1;
        goto L_0x0009;
    L_0x0035:
        r2 = r11.actualArea;
        r4 = r2.x;
        r2 = r11.actualArea;
        r2 = r2.y;
        r3 = r11.actualArea;
        r3 = r3.height;
        r5 = r2 + r3;
        r2 = r11.actualArea;
        r2 = r2.x;
        r3 = r11.actualArea;
        r3 = r3.width;
        r6 = r2 + r3;
        r2 = r11.actualArea;
        r7 = r2.y;
        r8 = r11.paintDash;
        r3 = r12;
        r3.drawLine(r4, r5, r6, r7, r8);
        r2 = 0;
        r3 = r11.curveValue;
        r3 = r3.activeType;
        switch(r3) {
            case 0: goto L_0x0087;
            case 1: goto L_0x007a;
            case 2: goto L_0x006d;
            case 3: goto L_0x0060;
            default: goto L_0x005f;
        };
    L_0x005f:
        goto L_0x0092;
    L_0x0060:
        r3 = r11.paintCurve;
        r4 = -13404165; // 0xffffffffff3377fb float:-2.3855479E38 double:NaN;
        r3.setColor(r4);
        r3 = r11.curveValue;
        r2 = r3.blueCurve;
        goto L_0x0092;
    L_0x006d:
        r3 = r11.paintCurve;
        r4 = -15667555; // 0xffffffffff10ee9d float:-1.9264778E38 double:NaN;
        r3.setColor(r4);
        r3 = r11.curveValue;
        r2 = r3.greenCurve;
        goto L_0x0092;
    L_0x007a:
        r3 = r11.paintCurve;
        r4 = -1229492; // 0xffffffffffed3d4c float:NaN double:NaN;
        r3.setColor(r4);
        r3 = r11.curveValue;
        r2 = r3.redCurve;
        goto L_0x0092;
    L_0x0087:
        r3 = r11.paintCurve;
        r4 = -1;
        r3.setColor(r4);
        r3 = r11.curveValue;
        r2 = r3.luminanceCurve;
    L_0x0092:
        r3 = r1;
        r4 = 5;
        r5 = 1;
        if (r3 >= r4) goto L_0x0131;
    L_0x0097:
        r4 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        switch(r3) {
            case 0: goto L_0x00ef;
            case 1: goto L_0x00db;
            case 2: goto L_0x00c7;
            case 3: goto L_0x00b3;
            case 4: goto L_0x009f;
            default: goto L_0x009c;
        };
    L_0x009c:
        r4 = "";
        goto L_0x0103;
    L_0x009f:
        r6 = java.util.Locale.US;
        r7 = "%.2f";
        r5 = new java.lang.Object[r5];
        r8 = r2.whitesLevel;
        r8 = r8 / r4;
        r4 = java.lang.Float.valueOf(r8);
        r5[r1] = r4;
        r4 = java.lang.String.format(r6, r7, r5);
        goto L_0x0103;
    L_0x00b3:
        r6 = java.util.Locale.US;
        r7 = "%.2f";
        r5 = new java.lang.Object[r5];
        r8 = r2.highlightsLevel;
        r8 = r8 / r4;
        r4 = java.lang.Float.valueOf(r8);
        r5[r1] = r4;
        r4 = java.lang.String.format(r6, r7, r5);
        goto L_0x0103;
    L_0x00c7:
        r6 = java.util.Locale.US;
        r7 = "%.2f";
        r5 = new java.lang.Object[r5];
        r8 = r2.midtonesLevel;
        r8 = r8 / r4;
        r4 = java.lang.Float.valueOf(r8);
        r5[r1] = r4;
        r4 = java.lang.String.format(r6, r7, r5);
        goto L_0x0103;
    L_0x00db:
        r6 = java.util.Locale.US;
        r7 = "%.2f";
        r5 = new java.lang.Object[r5];
        r8 = r2.shadowsLevel;
        r8 = r8 / r4;
        r4 = java.lang.Float.valueOf(r8);
        r5[r1] = r4;
        r4 = java.lang.String.format(r6, r7, r5);
        goto L_0x0103;
    L_0x00ef:
        r6 = java.util.Locale.US;
        r7 = "%.2f";
        r5 = new java.lang.Object[r5];
        r8 = r2.blacksLevel;
        r8 = r8 / r4;
        r4 = java.lang.Float.valueOf(r8);
        r5[r1] = r4;
        r4 = java.lang.String.format(r6, r7, r5);
        r5 = r11.textPaint;
        r5 = r5.measureText(r4);
        r6 = r11.actualArea;
        r6 = r6.x;
        r7 = r0 - r5;
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = r7 / r8;
        r6 = r6 + r7;
        r7 = (float) r3;
        r7 = r7 * r0;
        r6 = r6 + r7;
        r7 = r11.actualArea;
        r7 = r7.y;
        r8 = r11.actualArea;
        r8 = r8.height;
        r7 = r7 + r8;
        r8 = 1082130432; // 0x40800000 float:4.0 double:5.34643471E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r8);
        r8 = (float) r8;
        r7 = r7 - r8;
        r8 = r11.textPaint;
        r12.drawText(r4, r6, r7, r8);
        r3 = r3 + 1;
        goto L_0x0093;
    L_0x0131:
        r3 = r2.interpolateCurve();
        r11.invalidate();
        r4 = r11.path;
        r4.reset();
        r4 = r3.length;
        r4 = r4 / 2;
        if (r1 >= r4) goto L_0x0191;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r1 != 0) goto L_0x016b;
        r6 = r11.path;
        r7 = r11.actualArea;
        r7 = r7.x;
        r8 = r1 * 2;
        r8 = r3[r8];
        r9 = r11.actualArea;
        r9 = r9.width;
        r8 = r8 * r9;
        r7 = r7 + r8;
        r8 = r11.actualArea;
        r8 = r8.y;
        r9 = r1 * 2;
        r9 = r9 + r5;
        r9 = r3[r9];
        r4 = r4 - r9;
        r9 = r11.actualArea;
        r9 = r9.height;
        r4 = r4 * r9;
        r8 = r8 + r4;
        r6.moveTo(r7, r8);
        goto L_0x018e;
        r6 = r11.path;
        r7 = r11.actualArea;
        r7 = r7.x;
        r8 = r1 * 2;
        r8 = r3[r8];
        r9 = r11.actualArea;
        r9 = r9.width;
        r8 = r8 * r9;
        r7 = r7 + r8;
        r8 = r11.actualArea;
        r8 = r8.y;
        r9 = r1 * 2;
        r9 = r9 + r5;
        r9 = r3[r9];
        r4 = r4 - r9;
        r9 = r11.actualArea;
        r9 = r9.height;
        r4 = r4 * r9;
        r8 = r8 + r4;
        r6.lineTo(r7, r8);
        r1 = r1 + 1;
        goto L_0x013e;
        r1 = r11.path;
        r4 = r11.paintCurve;
        r12.drawPath(r1, r4);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.PhotoFilterCurvesControl.onDraw(android.graphics.Canvas):void");
    }

    public PhotoFilterCurvesControl(Context context, CurvesToolValue value) {
        super(context);
        setWillNotDraw(false);
        this.curveValue = value;
        this.paint.setColor(-1711276033);
        this.paint.setStrokeWidth((float) AndroidUtilities.dp(1.0f));
        this.paint.setStyle(Style.STROKE);
        this.paintDash.setColor(-1711276033);
        this.paintDash.setStrokeWidth((float) AndroidUtilities.dp(2.0f));
        this.paintDash.setStyle(Style.STROKE);
        this.paintCurve.setColor(-1);
        this.paintCurve.setStrokeWidth((float) AndroidUtilities.dp(2.0f));
        this.paintCurve.setStyle(Style.STROKE);
        this.textPaint.setColor(-4210753);
        this.textPaint.setTextSize((float) AndroidUtilities.dp(13.0f));
    }

    public void setDelegate(PhotoFilterCurvesControlDelegate photoFilterCurvesControlDelegate) {
        this.delegate = photoFilterCurvesControlDelegate;
    }

    public void setActualArea(float x, float y, float width, float height) {
        this.actualArea.x = x;
        this.actualArea.y = y;
        this.actualArea.width = width;
        this.actualArea.height = height;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case 0:
            case 5:
                if (event.getPointerCount() != 1) {
                    if (this.isMoving) {
                        handlePan(3, event);
                        this.checkForMoving = true;
                        this.isMoving = false;
                        break;
                    }
                } else if (this.checkForMoving && !this.isMoving) {
                    float locationX = event.getX();
                    float locationY = event.getY();
                    this.lastX = locationX;
                    this.lastY = locationY;
                    if (locationX >= this.actualArea.x && locationX <= this.actualArea.x + this.actualArea.width && locationY >= this.actualArea.y && locationY <= this.actualArea.y + this.actualArea.height) {
                        this.isMoving = true;
                    }
                    this.checkForMoving = false;
                    if (this.isMoving) {
                        handlePan(1, event);
                    }
                    break;
                }
                break;
            case 1:
            case 3:
            case 6:
                if (this.isMoving) {
                    handlePan(3, event);
                    this.isMoving = false;
                }
                this.checkForMoving = true;
                break;
            case 2:
                if (this.isMoving) {
                    handlePan(2, event);
                    break;
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void handlePan(int state, MotionEvent event) {
        float locationX = event.getX();
        float locationY = event.getY();
        switch (state) {
            case 1:
                selectSegmentWithPoint(locationX);
                return;
            case 2:
                float delta = Math.min(2.0f, (this.lastY - locationY) / 8.0f);
                CurvesValue curveValue = null;
                switch (this.curveValue.activeType) {
                    case 0:
                        curveValue = this.curveValue.luminanceCurve;
                        break;
                    case 1:
                        curveValue = this.curveValue.redCurve;
                        break;
                    case 2:
                        curveValue = this.curveValue.greenCurve;
                        break;
                    case 3:
                        curveValue = this.curveValue.blueCurve;
                        break;
                    default:
                        break;
                }
                switch (this.activeSegment) {
                    case 1:
                        curveValue.blacksLevel = Math.max(0.0f, Math.min(100.0f, curveValue.blacksLevel + delta));
                        break;
                    case 2:
                        curveValue.shadowsLevel = Math.max(0.0f, Math.min(100.0f, curveValue.shadowsLevel + delta));
                        break;
                    case 3:
                        curveValue.midtonesLevel = Math.max(0.0f, Math.min(100.0f, curveValue.midtonesLevel + delta));
                        break;
                    case 4:
                        curveValue.highlightsLevel = Math.max(0.0f, Math.min(100.0f, curveValue.highlightsLevel + delta));
                        break;
                    case 5:
                        curveValue.whitesLevel = Math.max(0.0f, Math.min(100.0f, curveValue.whitesLevel + delta));
                        break;
                    default:
                        break;
                }
                invalidate();
                if (this.delegate != null) {
                    this.delegate.valueChanged();
                }
                this.lastX = locationX;
                this.lastY = locationY;
                return;
            case 3:
            case 4:
            case 5:
                unselectSegments();
                return;
            default:
                return;
        }
    }

    private void selectSegmentWithPoint(float pointx) {
        if (this.activeSegment == 0) {
            this.activeSegment = (int) Math.floor((double) (((pointx - this.actualArea.x) / (this.actualArea.width / 5.0f)) + 1.0f));
        }
    }

    private void unselectSegments() {
        if (this.activeSegment != 0) {
            this.activeSegment = 0;
        }
    }
}
