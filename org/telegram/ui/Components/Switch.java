package org.telegram.ui.Components;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.annotation.Keep;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.CompoundButton;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.beta.R;
import org.telegram.ui.ActionBar.Theme;

public class Switch extends CompoundButton {
    private static final int THUMB_ANIMATION_DURATION = 250;
    private static final int TOUCH_MODE_DOWN = 1;
    private static final int TOUCH_MODE_DRAGGING = 2;
    private static final int TOUCH_MODE_IDLE = 0;
    private boolean attachedToWindow;
    private int mMinFlingVelocity;
    private ObjectAnimator mPositionAnimator;
    private boolean mSplitTrack;
    private int mSwitchBottom;
    private int mSwitchHeight;
    private int mSwitchLeft;
    private int mSwitchMinWidth;
    private int mSwitchPadding;
    private int mSwitchRight;
    private int mSwitchTop;
    private int mSwitchWidth;
    private final Rect mTempRect = new Rect();
    private Drawable mThumbDrawable;
    private int mThumbTextPadding;
    private int mThumbWidth;
    private int mTouchMode;
    private int mTouchSlop;
    private float mTouchX;
    private float mTouchY;
    private Drawable mTrackDrawable;
    private VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    private float thumbPosition;
    private boolean wasLayout;

    public static class Insets {
        public static final Insets NONE = new Insets(AndroidUtilities.dp(4.0f), 0, AndroidUtilities.dp(4.0f), 0);
        public final int bottom;
        public final int left;
        public final int right;
        public final int top;

        private Insets(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    protected void onLayout(boolean r1, int r2, int r3, int r4, int r5) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.Switch.onLayout(boolean, int, int, int, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
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
        super.onLayout(r8, r9, r10, r11, r12);
        r0 = 1;
        r7.wasLayout = r0;
        r0 = 0;
        r1 = 0;
        r2 = r7.mThumbDrawable;
        if (r2 == 0) goto L_0x0030;
    L_0x000c:
        r2 = r7.mTempRect;
        r3 = r7.mTrackDrawable;
        if (r3 == 0) goto L_0x0018;
    L_0x0012:
        r3 = r7.mTrackDrawable;
        r3.getPadding(r2);
        goto L_0x001b;
    L_0x0018:
        r2.setEmpty();
    L_0x001b:
        r3 = org.telegram.ui.Components.Switch.Insets.NONE;
        r4 = r3.left;
        r5 = r2.left;
        r4 = r4 - r5;
        r5 = 0;
        r0 = java.lang.Math.max(r5, r4);
        r4 = r3.right;
        r6 = r2.right;
        r4 = r4 - r6;
        r1 = java.lang.Math.max(r5, r4);
    L_0x0030:
        r2 = org.telegram.messenger.LocaleController.isRTL;
        if (r2 == 0) goto L_0x003f;
    L_0x0034:
        r2 = r7.getPaddingLeft();
        r2 = r2 + r0;
        r3 = r7.mSwitchWidth;
        r3 = r3 + r2;
        r3 = r3 - r0;
        r3 = r3 - r1;
        goto L_0x0050;
    L_0x003f:
        r2 = r7.getWidth();
        r3 = r7.getPaddingRight();
        r2 = r2 - r3;
        r3 = r2 - r1;
        r2 = r7.mSwitchWidth;
        r2 = r3 - r2;
        r2 = r2 + r0;
        r2 = r2 + r1;
    L_0x0050:
        r4 = r7.getGravity();
        r4 = r4 & 112;
        r5 = 16;
        if (r4 == r5) goto L_0x0075;
    L_0x005a:
        r5 = 80;
        if (r4 == r5) goto L_0x0066;
    L_0x005e:
        r4 = r7.getPaddingTop();
        r5 = r7.mSwitchHeight;
        r5 = r5 + r4;
        goto L_0x008e;
    L_0x0066:
        r4 = r7.getHeight();
        r5 = r7.getPaddingBottom();
        r5 = r4 - r5;
        r4 = r7.mSwitchHeight;
        r4 = r5 - r4;
        goto L_0x008e;
    L_0x0075:
        r4 = r7.getPaddingTop();
        r5 = r7.getHeight();
        r4 = r4 + r5;
        r5 = r7.getPaddingBottom();
        r4 = r4 - r5;
        r4 = r4 / 2;
        r5 = r7.mSwitchHeight;
        r5 = r5 / 2;
        r4 = r4 - r5;
        r5 = r7.mSwitchHeight;
        r5 = r5 + r4;
        r7.mSwitchLeft = r2;
        r7.mSwitchTop = r4;
        r7.mSwitchBottom = r5;
        r7.mSwitchRight = r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.Switch.onLayout(boolean, int, int, int, int):void");
    }

    public boolean onTouchEvent(android.view.MotionEvent r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.Switch.onTouchEvent(android.view.MotionEvent):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
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
        r0 = r9.mVelocityTracker;
        r0.addMovement(r10);
        r0 = r10.getActionMasked();
        r1 = 2;
        r2 = 1;
        switch(r0) {
            case 0: goto L_0x0095;
            case 1: goto L_0x0081;
            case 2: goto L_0x0010;
            case 3: goto L_0x0081;
            default: goto L_0x000e;
        };
    L_0x000e:
        goto L_0x00af;
    L_0x0010:
        r3 = r9.mTouchMode;
        switch(r3) {
            case 0: goto L_0x007f;
            case 1: goto L_0x004b;
            case 2: goto L_0x0016;
            default: goto L_0x0015;
        };
        goto L_0x0080;
        r1 = r10.getX();
        r3 = r9.getThumbScrollRange();
        r4 = r9.mTouchX;
        r4 = r1 - r4;
        r5 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r6 = 0;
        if (r3 == 0) goto L_0x002b;
        r7 = (float) r3;
        r7 = r4 / r7;
        goto L_0x0033;
        r7 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r7 <= 0) goto L_0x0031;
        r7 = r5;
        goto L_0x0033;
        r7 = -1082130432; // 0xffffffffbf800000 float:-1.0 double:NaN;
        r8 = org.telegram.messenger.LocaleController.isRTL;
        if (r8 == 0) goto L_0x0038;
        r7 = -r7;
        r8 = r9.thumbPosition;
        r8 = r8 + r7;
        r5 = constrain(r8, r6, r5);
        r6 = r9.thumbPosition;
        r6 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r6 == 0) goto L_0x004a;
        r9.mTouchX = r1;
        r9.setThumbPosition(r5);
        return r2;
        r3 = r10.getX();
        r4 = r10.getY();
        r5 = r9.mTouchX;
        r5 = r3 - r5;
        r5 = java.lang.Math.abs(r5);
        r6 = r9.mTouchSlop;
        r6 = (float) r6;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 > 0) goto L_0x0071;
        r5 = r9.mTouchY;
        r5 = r4 - r5;
        r5 = java.lang.Math.abs(r5);
        r6 = r9.mTouchSlop;
        r6 = (float) r6;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 <= 0) goto L_0x0080;
        r9.mTouchMode = r1;
        r1 = r9.getParent();
        r1.requestDisallowInterceptTouchEvent(r2);
        r9.mTouchX = r3;
        r9.mTouchY = r4;
        return r2;
        goto L_0x00af;
    L_0x0081:
        r3 = r9.mTouchMode;
        if (r3 != r1) goto L_0x008c;
        r9.stopDrag(r10);
        super.onTouchEvent(r10);
        return r2;
        r1 = 0;
        r9.mTouchMode = r1;
        r1 = r9.mVelocityTracker;
        r1.clear();
        goto L_0x00af;
    L_0x0095:
        r1 = r10.getX();
        r3 = r10.getY();
        r4 = r9.isEnabled();
        if (r4 == 0) goto L_0x00af;
        r4 = r9.hitThumb(r1, r3);
        if (r4 == 0) goto L_0x00af;
        r9.mTouchMode = r2;
        r9.mTouchX = r1;
        r9.mTouchY = r3;
    L_0x00af:
        r1 = super.onTouchEvent(r10);
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.Switch.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public static float constrain(float amount, float low, float high) {
        if (amount < low) {
            return low;
        }
        return amount > high ? high : amount;
    }

    public Switch(Context context) {
        super(context);
        this.mThumbDrawable = context.getResources().getDrawable(R.drawable.switch_thumb);
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setCallback(this);
        }
        this.mTrackDrawable = context.getResources().getDrawable(R.drawable.switch_track);
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setCallback(this);
        }
        if (AndroidUtilities.density < 1.0f) {
            this.mSwitchMinWidth = AndroidUtilities.dp(30.0f);
        } else {
            this.mSwitchMinWidth = 0;
        }
        this.mSwitchPadding = 0;
        this.mSplitTrack = false;
        ViewConfiguration config = ViewConfiguration.get(context);
        this.mTouchSlop = config.getScaledTouchSlop();
        this.mMinFlingVelocity = config.getScaledMinimumFlingVelocity();
        refreshDrawableState();
        setChecked(isChecked());
    }

    public void setSwitchPadding(int pixels) {
        this.mSwitchPadding = pixels;
        requestLayout();
    }

    public int getSwitchPadding() {
        return this.mSwitchPadding;
    }

    public void setSwitchMinWidth(int pixels) {
        this.mSwitchMinWidth = pixels;
        requestLayout();
    }

    public int getSwitchMinWidth() {
        return this.mSwitchMinWidth;
    }

    public void setThumbTextPadding(int pixels) {
        this.mThumbTextPadding = pixels;
        requestLayout();
    }

    public int getThumbTextPadding() {
        return this.mThumbTextPadding;
    }

    public void setTrackDrawable(Drawable track) {
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setCallback(null);
        }
        this.mTrackDrawable = track;
        if (track != null) {
            track.setCallback(this);
        }
        requestLayout();
    }

    public Drawable getTrackDrawable() {
        return this.mTrackDrawable;
    }

    public void setThumbDrawable(Drawable thumb) {
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setCallback(null);
        }
        this.mThumbDrawable = thumb;
        if (thumb != null) {
            thumb.setCallback(this);
        }
        requestLayout();
    }

    public Drawable getThumbDrawable() {
        return this.mThumbDrawable;
    }

    public void setSplitTrack(boolean splitTrack) {
        this.mSplitTrack = splitTrack;
        invalidate();
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int thumbWidth;
        int thumbHeight;
        Rect padding = this.mTempRect;
        int trackHeight = 0;
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.getPadding(padding);
            thumbWidth = (this.mThumbDrawable.getIntrinsicWidth() - padding.left) - padding.right;
            thumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        } else {
            thumbWidth = 0;
            thumbHeight = 0;
        }
        this.mThumbWidth = thumbWidth;
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.getPadding(padding);
            trackHeight = this.mTrackDrawable.getIntrinsicHeight();
        } else {
            padding.setEmpty();
        }
        int paddingLeft = padding.left;
        int paddingRight = padding.right;
        if (this.mThumbDrawable != null) {
            Insets inset = Insets.NONE;
            paddingLeft = Math.max(paddingLeft, inset.left);
            paddingRight = Math.max(paddingRight, inset.right);
        }
        int switchWidth = Math.max(this.mSwitchMinWidth, ((2 * this.mThumbWidth) + paddingLeft) + paddingRight);
        int switchHeight = Math.max(trackHeight, thumbHeight);
        this.mSwitchWidth = switchWidth;
        this.mSwitchHeight = switchHeight;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getMeasuredHeight() < switchHeight) {
            setMeasuredDimension(switchWidth, switchHeight);
        }
    }

    private boolean hitThumb(float x, float y) {
        int thumbOffset = getThumbOffset();
        this.mThumbDrawable.getPadding(this.mTempRect);
        int thumbLeft = (this.mSwitchLeft + thumbOffset) - this.mTouchSlop;
        return x > ((float) thumbLeft) && x < ((float) ((((this.mThumbWidth + thumbLeft) + this.mTempRect.left) + this.mTempRect.right) + this.mTouchSlop)) && y > ((float) (this.mSwitchTop - this.mTouchSlop)) && y < ((float) (this.mSwitchBottom + this.mTouchSlop));
    }

    private void cancelSuperTouch(MotionEvent ev) {
        MotionEvent cancel = MotionEvent.obtain(ev);
        cancel.setAction(3);
        super.onTouchEvent(cancel);
        cancel.recycle();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void stopDrag(android.view.MotionEvent r7) {
        /*
        r6 = this;
        r0 = 0;
        r6.mTouchMode = r0;
        r1 = r7.getAction();
        r2 = 1;
        if (r1 != r2) goto L_0x0012;
    L_0x000a:
        r1 = r6.isEnabled();
        if (r1 == 0) goto L_0x0012;
    L_0x0010:
        r1 = r2;
        goto L_0x0013;
    L_0x0012:
        r1 = r0;
    L_0x0013:
        if (r1 == 0) goto L_0x0044;
    L_0x0015:
        r3 = r6.mVelocityTracker;
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r3.computeCurrentVelocity(r4);
        r3 = r6.mVelocityTracker;
        r3 = r3.getXVelocity();
        r4 = java.lang.Math.abs(r3);
        r5 = r6.mMinFlingVelocity;
        r5 = (float) r5;
        r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1));
        if (r4 <= 0) goto L_0x003f;
    L_0x002d:
        r4 = org.telegram.messenger.LocaleController.isRTL;
        r5 = 0;
        if (r4 == 0) goto L_0x0039;
    L_0x0032:
        r4 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r4 >= 0) goto L_0x0038;
    L_0x0036:
        r0 = r2;
        goto L_0x003e;
    L_0x0038:
        goto L_0x003e;
    L_0x0039:
        r4 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r4 <= 0) goto L_0x0038;
    L_0x003d:
        goto L_0x0036;
    L_0x003e:
        goto L_0x0043;
    L_0x003f:
        r0 = r6.getTargetCheckedState();
    L_0x0043:
        goto L_0x0048;
    L_0x0044:
        r0 = r6.isChecked();
    L_0x0048:
        r6.setChecked(r0);
        r6.cancelSuperTouch(r7);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.Switch.stopDrag(android.view.MotionEvent):void");
    }

    private void animateThumbToCheckedState(boolean newCheckedState) {
        float targetPosition = newCheckedState ? 1.0f : 0.0f;
        this.mPositionAnimator = ObjectAnimator.ofFloat(this, "thumbPosition", new float[]{targetPosition});
        this.mPositionAnimator.setDuration(250);
        this.mPositionAnimator.start();
    }

    private void cancelPositionAnimator() {
        if (this.mPositionAnimator != null) {
            this.mPositionAnimator.cancel();
        }
    }

    private boolean getTargetCheckedState() {
        return this.thumbPosition > 0.5f;
    }

    @Keep
    private void setThumbPosition(float position) {
        this.thumbPosition = position;
        invalidate();
    }

    public float getThumbPosition() {
        return this.thumbPosition;
    }

    public void toggle() {
        setChecked(isChecked() ^ 1);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.attachedToWindow = true;
        requestLayout();
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.attachedToWindow = false;
        this.wasLayout = false;
    }

    public void resetLayout() {
        this.wasLayout = false;
    }

    public void setChecked(boolean checked) {
        super.setChecked(checked);
        checked = isChecked();
        if (this.attachedToWindow && this.wasLayout) {
            animateThumbToCheckedState(checked);
        } else {
            cancelPositionAnimator();
            setThumbPosition(checked ? 1.0f : 0.0f);
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(checked ? Theme.key_switchTrackChecked : Theme.key_switchTrack), Mode.MULTIPLY));
        }
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(checked ? Theme.key_switchThumbChecked : Theme.key_switchThumb), Mode.MULTIPLY));
        }
    }

    public void checkColorFilters() {
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(isChecked() ? Theme.key_switchTrackChecked : Theme.key_switchTrack), Mode.MULTIPLY));
        }
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setColorFilter(new PorterDuffColorFilter(Theme.getColor(isChecked() ? Theme.key_switchThumbChecked : Theme.key_switchThumb), Mode.MULTIPLY));
        }
    }

    public void draw(Canvas c) {
        Insets thumbInsets;
        int trackLeft;
        int trackTop;
        int trackRight;
        Rect padding = this.mTempRect;
        int switchLeft = this.mSwitchLeft;
        int switchTop = this.mSwitchTop;
        int switchRight = this.mSwitchRight;
        int switchBottom = this.mSwitchBottom;
        int thumbInitialLeft = getThumbOffset() + switchLeft;
        if (this.mThumbDrawable != null) {
            thumbInsets = Insets.NONE;
        } else {
            thumbInsets = Insets.NONE;
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.getPadding(padding);
            thumbInitialLeft += padding.left;
            trackLeft = switchLeft;
            trackTop = switchTop;
            trackRight = switchRight;
            int trackBottom = switchBottom;
            if (thumbInsets != Insets.NONE) {
                if (thumbInsets.left > padding.left) {
                    trackLeft += thumbInsets.left - padding.left;
                }
                if (thumbInsets.top > padding.top) {
                    trackTop += thumbInsets.top - padding.top;
                }
                if (thumbInsets.right > padding.right) {
                    trackRight -= thumbInsets.right - padding.right;
                }
                if (thumbInsets.bottom > padding.bottom) {
                    trackBottom -= thumbInsets.bottom - padding.bottom;
                }
            }
            this.mTrackDrawable.setBounds(trackLeft, trackTop, trackRight, trackBottom);
        }
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.getPadding(padding);
            trackLeft = thumbInitialLeft - padding.left;
            trackTop = (this.mThumbWidth + thumbInitialLeft) + padding.right;
            trackRight = AndroidUtilities.density == 1.5f ? AndroidUtilities.dp(1.0f) : 0;
            this.mThumbDrawable.setBounds(trackLeft, switchTop + trackRight, trackTop, switchBottom + trackRight);
            Drawable background = getBackground();
            if (background != null && VERSION.SDK_INT >= 21) {
                background.setHotspotBounds(trackLeft, switchTop, trackTop, switchBottom);
            }
        }
        super.draw(c);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect padding = this.mTempRect;
        Drawable trackDrawable = this.mTrackDrawable;
        if (trackDrawable != null) {
            trackDrawable.getPadding(padding);
        } else {
            padding.setEmpty();
        }
        int switchTop = this.mSwitchTop;
        int switchBottom = this.mSwitchBottom;
        Drawable thumbDrawable = this.mThumbDrawable;
        if (trackDrawable != null) {
            if (!this.mSplitTrack || thumbDrawable == null) {
                trackDrawable.draw(canvas);
            } else {
                Insets insets = Insets.NONE;
                thumbDrawable.copyBounds(padding);
                padding.left += insets.left;
                padding.right -= insets.right;
                int saveCount = canvas.save();
                canvas.clipRect(padding, Op.DIFFERENCE);
                trackDrawable.draw(canvas);
                canvas.restoreToCount(saveCount);
            }
        }
        int saveCount2 = canvas.save();
        if (thumbDrawable != null) {
            thumbDrawable.draw(canvas);
        }
        canvas.restoreToCount(saveCount2);
    }

    public int getCompoundPaddingLeft() {
        if (LocaleController.isRTL) {
            return super.getCompoundPaddingLeft() + this.mSwitchWidth;
        }
        return super.getCompoundPaddingLeft();
    }

    public int getCompoundPaddingRight() {
        if (LocaleController.isRTL) {
            return super.getCompoundPaddingRight();
        }
        return super.getCompoundPaddingRight() + this.mSwitchWidth;
    }

    private int getThumbOffset() {
        float position;
        if (LocaleController.isRTL) {
            position = 1.0f - this.thumbPosition;
        } else {
            position = this.thumbPosition;
        }
        return (int) ((((float) getThumbScrollRange()) * position) + 0.5f);
    }

    private int getThumbScrollRange() {
        if (this.mTrackDrawable == null) {
            return 0;
        }
        Insets insets;
        Rect padding = this.mTempRect;
        this.mTrackDrawable.getPadding(padding);
        if (this.mThumbDrawable != null) {
            insets = Insets.NONE;
        } else {
            insets = Insets.NONE;
        }
        return ((((this.mSwitchWidth - this.mThumbWidth) - padding.left) - padding.right) - insets.left) - insets.right;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] myDrawableState = getDrawableState();
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setState(myDrawableState);
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setState(myDrawableState);
        }
        invalidate();
    }

    @SuppressLint({"NewApi"})
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.setHotspot(x, y);
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.setHotspot(x, y);
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        if (!(super.verifyDrawable(who) || who == this.mThumbDrawable)) {
            if (who != this.mTrackDrawable) {
                return false;
            }
        }
        return true;
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mThumbDrawable != null) {
            this.mThumbDrawable.jumpToCurrentState();
        }
        if (this.mTrackDrawable != null) {
            this.mTrackDrawable.jumpToCurrentState();
        }
        if (this.mPositionAnimator != null && this.mPositionAnimator.isRunning()) {
            this.mPositionAnimator.end();
            this.mPositionAnimator = null;
        }
    }
}
