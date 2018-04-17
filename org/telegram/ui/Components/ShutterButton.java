package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.beta.R;

public class ShutterButton extends View {
    private static final int LONG_PRESS_TIME = 800;
    private ShutterButtonDelegate delegate;
    private DecelerateInterpolator interpolator = new DecelerateInterpolator();
    private long lastUpdateTime;
    private Runnable longPressed = new Runnable() {
        public void run() {
            if (ShutterButton.this.delegate != null && !ShutterButton.this.delegate.shutterLongPressed()) {
                ShutterButton.this.processRelease = false;
            }
        }
    };
    private boolean pressed;
    private boolean processRelease;
    private Paint redPaint;
    private float redProgress;
    private Drawable shadowDrawable = getResources().getDrawable(R.drawable.camera_btn);
    private State state;
    private long totalTime;
    private Paint whitePaint = new Paint(1);

    public interface ShutterButtonDelegate {
        void shutterCancel();

        boolean shutterLongPressed();

        void shutterReleased();
    }

    public enum State {
        DEFAULT,
        RECORDING
    }

    protected void onDraw(android.graphics.Canvas r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.ShutterButton.onDraw(android.graphics.Canvas):void
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
        r0 = r11.getMeasuredWidth();
        r0 = r0 / 2;
        r1 = r11.getMeasuredHeight();
        r1 = r1 / 2;
        r2 = r11.shadowDrawable;
        r3 = 1108344832; // 0x42100000 float:36.0 double:5.47595105E-315;
        r4 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r4 = r0 - r4;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r5 = r1 - r5;
        r6 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r6 = r6 + r0;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r3);
        r3 = r3 + r1;
        r2.setBounds(r4, r5, r6, r3);
        r2 = r11.shadowDrawable;
        r2.draw(r12);
        r2 = r11.pressed;
        r3 = 0;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r2 != 0) goto L_0x0048;
    L_0x0035:
        r2 = r11.getScaleX();
        r2 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r2 == 0) goto L_0x003e;
    L_0x003d:
        goto L_0x0048;
    L_0x003e:
        r2 = r11.redProgress;
        r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1));
        if (r2 == 0) goto L_0x00ce;
    L_0x0044:
        r11.redProgress = r3;
        goto L_0x00ce;
    L_0x0048:
        r2 = r11.getScaleX();
        r2 = r2 - r4;
        r5 = 1031127695; // 0x3d75c28f float:0.06 double:5.094447706E-315;
        r2 = r2 / r5;
        r5 = r11.whitePaint;
        r6 = 1132396544; // 0x437f0000 float:255.0 double:5.5947823E-315;
        r6 = r6 * r2;
        r6 = (int) r6;
        r5.setAlpha(r6);
        r5 = (float) r0;
        r6 = (float) r1;
        r7 = 1104150528; // 0x41d00000 float:26.0 double:5.455228437E-315;
        r8 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r8 = (float) r8;
        r9 = r11.whitePaint;
        r12.drawCircle(r5, r6, r8, r9);
        r5 = r11.state;
        r6 = org.telegram.ui.Components.ShutterButton.State.RECORDING;
        if (r5 != r6) goto L_0x00ba;
    L_0x006e:
        r3 = r11.redProgress;
        r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1));
        if (r3 == 0) goto L_0x00a9;
    L_0x0074:
        r3 = java.lang.System.currentTimeMillis();
        r5 = r11.lastUpdateTime;
        r8 = r3 - r5;
        r3 = java.lang.Math.abs(r8);
        r5 = 17;
        r8 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r8 <= 0) goto L_0x0088;
    L_0x0086:
        r3 = 17;
    L_0x0088:
        r5 = r11.totalTime;
        r8 = r5 + r3;
        r11.totalTime = r8;
        r5 = r11.totalTime;
        r8 = 120; // 0x78 float:1.68E-43 double:5.93E-322;
        r10 = (r5 > r8 ? 1 : (r5 == r8 ? 0 : -1));
        if (r10 <= 0) goto L_0x0098;
    L_0x0096:
        r11.totalTime = r8;
    L_0x0098:
        r5 = r11.interpolator;
        r8 = r11.totalTime;
        r6 = (float) r8;
        r8 = 1123024896; // 0x42f00000 float:120.0 double:5.548480205E-315;
        r6 = r6 / r8;
        r5 = r5.getInterpolation(r6);
        r11.redProgress = r5;
        r11.invalidate();
    L_0x00a9:
        r3 = (float) r0;
        r4 = (float) r1;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = (float) r5;
        r5 = r5 * r2;
        r6 = r11.redProgress;
        r5 = r5 * r6;
        r6 = r11.redPaint;
        r12.drawCircle(r3, r4, r5, r6);
        goto L_0x00cd;
    L_0x00ba:
        r4 = r11.redProgress;
        r3 = (r4 > r3 ? 1 : (r4 == r3 ? 0 : -1));
        if (r3 == 0) goto L_0x00cd;
        r3 = (float) r0;
        r4 = (float) r1;
        r5 = org.telegram.messenger.AndroidUtilities.dp(r7);
        r5 = (float) r5;
        r5 = r5 * r2;
        r6 = r11.redPaint;
        r12.drawCircle(r3, r4, r5, r6);
    L_0x00ce:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ShutterButton.onDraw(android.graphics.Canvas):void");
    }

    public ShutterButton(Context context) {
        super(context);
        this.whitePaint.setStyle(Style.FILL);
        this.whitePaint.setColor(-1);
        this.redPaint = new Paint(1);
        this.redPaint.setStyle(Style.FILL);
        this.redPaint.setColor(-3324089);
        this.state = State.DEFAULT;
    }

    public void setDelegate(ShutterButtonDelegate shutterButtonDelegate) {
        this.delegate = shutterButtonDelegate;
    }

    public ShutterButtonDelegate getDelegate() {
        return this.delegate;
    }

    private void setHighlighted(boolean value) {
        AnimatorSet animatorSet = new AnimatorSet();
        Animator[] animatorArr;
        if (value) {
            animatorArr = new Animator[2];
            animatorArr[0] = ObjectAnimator.ofFloat(this, "scaleX", new float[]{1.06f});
            animatorArr[1] = ObjectAnimator.ofFloat(this, "scaleY", new float[]{1.06f});
            animatorSet.playTogether(animatorArr);
        } else {
            animatorArr = new Animator[2];
            animatorArr[0] = ObjectAnimator.ofFloat(this, "scaleX", new float[]{1.0f});
            animatorArr[1] = ObjectAnimator.ofFloat(this, "scaleY", new float[]{1.0f});
            animatorSet.playTogether(animatorArr);
            animatorSet.setStartDelay(40);
        }
        animatorSet.setDuration(120);
        animatorSet.setInterpolator(this.interpolator);
        animatorSet.start();
    }

    public void setScaleX(float scaleX) {
        super.setScaleX(scaleX);
        invalidate();
    }

    public State getState() {
        return this.state;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(AndroidUtilities.dp(84.0f), AndroidUtilities.dp(84.0f));
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getX();
        switch (motionEvent.getAction()) {
            case 0:
                AndroidUtilities.runOnUIThread(this.longPressed, 800);
                this.pressed = true;
                this.processRelease = true;
                setHighlighted(true);
                break;
            case 1:
                setHighlighted(false);
                AndroidUtilities.cancelRunOnUIThread(this.longPressed);
                if (this.processRelease && x >= 0.0f && y >= 0.0f && x <= ((float) getMeasuredWidth()) && y <= ((float) getMeasuredHeight())) {
                    this.delegate.shutterReleased();
                    break;
                }
            case 2:
                if (x < 0.0f || y < 0.0f || x > ((float) getMeasuredWidth()) || y > ((float) getMeasuredHeight())) {
                    AndroidUtilities.cancelRunOnUIThread(this.longPressed);
                    if (this.state == State.RECORDING) {
                        setHighlighted(false);
                        this.delegate.shutterCancel();
                        setState(State.DEFAULT, true);
                        break;
                    }
                }
                break;
            case 3:
                setHighlighted(false);
                this.pressed = false;
                break;
            default:
                break;
        }
        return true;
    }

    public void setState(State value, boolean animated) {
        if (this.state != value) {
            this.state = value;
            if (animated) {
                this.lastUpdateTime = System.currentTimeMillis();
                this.totalTime = 0;
                if (this.state != State.RECORDING) {
                    this.redProgress = 0.0f;
                }
            } else if (this.state == State.RECORDING) {
                this.redProgress = 1.0f;
            } else {
                this.redProgress = 0.0f;
            }
            invalidate();
        }
    }
}
