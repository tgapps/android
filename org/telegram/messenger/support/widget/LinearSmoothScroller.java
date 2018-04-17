package org.telegram.messenger.support.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import org.telegram.messenger.support.widget.RecyclerView.LayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.SmoothScroller;
import org.telegram.messenger.support.widget.RecyclerView.SmoothScroller.Action;
import org.telegram.messenger.support.widget.RecyclerView.SmoothScroller.ScrollVectorProvider;
import org.telegram.messenger.support.widget.RecyclerView.State;

public class LinearSmoothScroller extends SmoothScroller {
    private static final boolean DEBUG = false;
    private static final float MILLISECONDS_PER_INCH = 25.0f;
    public static final int SNAP_TO_ANY = 0;
    public static final int SNAP_TO_END = 1;
    public static final int SNAP_TO_START = -1;
    private static final String TAG = "LinearSmoothScroller";
    private static final float TARGET_SEEK_EXTRA_SCROLL_RATIO = 1.2f;
    private static final int TARGET_SEEK_SCROLL_DISTANCE_PX = 10000;
    private final float MILLISECONDS_PER_PX;
    protected final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();
    protected int mInterimTargetDx = 0;
    protected int mInterimTargetDy = 0;
    protected final LinearInterpolator mLinearInterpolator = new LinearInterpolator();
    protected PointF mTargetVector;

    public int calculateDxToMakeVisible(android.view.View r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.widget.LinearSmoothScroller.calculateDxToMakeVisible(android.view.View, int):int
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
        r0 = r12.getLayoutManager();
        if (r0 == 0) goto L_0x003b;
    L_0x0006:
        r1 = r0.canScrollHorizontally();
        if (r1 != 0) goto L_0x000d;
    L_0x000c:
        goto L_0x003b;
        r1 = r13.getLayoutParams();
        r1 = (org.telegram.messenger.support.widget.RecyclerView.LayoutParams) r1;
        r2 = r0.getDecoratedLeft(r13);
        r3 = r1.leftMargin;
        r2 = r2 - r3;
        r3 = r0.getDecoratedRight(r13);
        r4 = r1.rightMargin;
        r3 = r3 + r4;
        r10 = r0.getPaddingLeft();
        r4 = r0.getWidth();
        r5 = r0.getPaddingRight();
        r11 = r4 - r5;
        r4 = r12;
        r5 = r2;
        r6 = r3;
        r7 = r10;
        r8 = r11;
        r9 = r14;
        r4 = r4.calculateDtToFit(r5, r6, r7, r8, r9);
        return r4;
    L_0x003b:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.widget.LinearSmoothScroller.calculateDxToMakeVisible(android.view.View, int):int");
    }

    public int calculateDyToMakeVisible(android.view.View r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.widget.LinearSmoothScroller.calculateDyToMakeVisible(android.view.View, int):int
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
        r0 = r12.getLayoutManager();
        if (r0 == 0) goto L_0x003b;
    L_0x0006:
        r1 = r0.canScrollVertically();
        if (r1 != 0) goto L_0x000d;
    L_0x000c:
        goto L_0x003b;
        r1 = r13.getLayoutParams();
        r1 = (org.telegram.messenger.support.widget.RecyclerView.LayoutParams) r1;
        r2 = r0.getDecoratedTop(r13);
        r3 = r1.topMargin;
        r2 = r2 - r3;
        r3 = r0.getDecoratedBottom(r13);
        r4 = r1.bottomMargin;
        r3 = r3 + r4;
        r10 = r0.getPaddingTop();
        r4 = r0.getHeight();
        r5 = r0.getPaddingBottom();
        r11 = r4 - r5;
        r4 = r12;
        r5 = r2;
        r6 = r3;
        r7 = r10;
        r8 = r11;
        r9 = r14;
        r4 = r4.calculateDtToFit(r5, r6, r7, r8, r9);
        return r4;
    L_0x003b:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.widget.LinearSmoothScroller.calculateDyToMakeVisible(android.view.View, int):int");
    }

    public LinearSmoothScroller(Context context) {
        this.MILLISECONDS_PER_PX = calculateSpeedPerPixel(context.getResources().getDisplayMetrics());
    }

    protected void onStart() {
    }

    protected void onTargetFound(View targetView, State state, Action action) {
        int dx = calculateDxToMakeVisible(targetView, getHorizontalSnapPreference());
        int dy = calculateDyToMakeVisible(targetView, getVerticalSnapPreference());
        int time = calculateTimeForDeceleration((int) Math.sqrt((double) ((dx * dx) + (dy * dy))));
        if (time > 0) {
            action.update(-dx, -dy, time, this.mDecelerateInterpolator);
        }
    }

    protected void onSeekTargetStep(int dx, int dy, State state, Action action) {
        if (getChildCount() == 0) {
            stop();
            return;
        }
        this.mInterimTargetDx = clampApplyScroll(this.mInterimTargetDx, dx);
        this.mInterimTargetDy = clampApplyScroll(this.mInterimTargetDy, dy);
        if (this.mInterimTargetDx == 0 && this.mInterimTargetDy == 0) {
            updateActionForInterimTarget(action);
        }
    }

    protected void onStop() {
        this.mInterimTargetDy = 0;
        this.mInterimTargetDx = 0;
        this.mTargetVector = null;
    }

    protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return MILLISECONDS_PER_INCH / ((float) displayMetrics.densityDpi);
    }

    protected int calculateTimeForDeceleration(int dx) {
        return (int) Math.ceil(((double) calculateTimeForScrolling(dx)) / 0.3356d);
    }

    protected int calculateTimeForScrolling(int dx) {
        return (int) Math.ceil((double) (((float) Math.abs(dx)) * this.MILLISECONDS_PER_PX));
    }

    protected int getHorizontalSnapPreference() {
        if (this.mTargetVector != null) {
            if (this.mTargetVector.x != 0.0f) {
                return this.mTargetVector.x > 0.0f ? 1 : -1;
            }
        }
        return 0;
    }

    protected int getVerticalSnapPreference() {
        if (this.mTargetVector != null) {
            if (this.mTargetVector.y != 0.0f) {
                return this.mTargetVector.y > 0.0f ? 1 : -1;
            }
        }
        return 0;
    }

    protected void updateActionForInterimTarget(Action action) {
        PointF scrollVector = computeScrollVectorForPosition(getTargetPosition());
        if (scrollVector != null) {
            if (scrollVector.x != 0.0f || scrollVector.y != 0.0f) {
                normalize(scrollVector);
                this.mTargetVector = scrollVector;
                this.mInterimTargetDx = (int) (scrollVector.x * 10000.0f);
                this.mInterimTargetDy = (int) (10000.0f * scrollVector.y);
                action.update((int) (((float) this.mInterimTargetDx) * TARGET_SEEK_EXTRA_SCROLL_RATIO), (int) (((float) this.mInterimTargetDy) * TARGET_SEEK_EXTRA_SCROLL_RATIO), (int) (((float) calculateTimeForScrolling(10000)) * TARGET_SEEK_EXTRA_SCROLL_RATIO), this.mLinearInterpolator);
                return;
            }
        }
        action.jumpTo(getTargetPosition());
        stop();
    }

    private int clampApplyScroll(int tmpDt, int dt) {
        int before = tmpDt;
        tmpDt -= dt;
        if (before * tmpDt <= 0) {
            return 0;
        }
        return tmpDt;
    }

    public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
        switch (snapPreference) {
            case -1:
                return boxStart - viewStart;
            case 0:
                int dtStart = boxStart - viewStart;
                if (dtStart > 0) {
                    return dtStart;
                }
                int dtEnd = boxEnd - viewEnd;
                if (dtEnd < 0) {
                    return dtEnd;
                }
                return 0;
            case 1:
                return boxEnd - viewEnd;
            default:
                throw new IllegalArgumentException("snap preference should be one of the constants defined in SmoothScroller, starting with SNAP_");
        }
    }

    public PointF computeScrollVectorForPosition(int targetPosition) {
        LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof ScrollVectorProvider) {
            return ((ScrollVectorProvider) layoutManager).computeScrollVectorForPosition(targetPosition);
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("You should override computeScrollVectorForPosition when the LayoutManager does not implement ");
        stringBuilder.append(ScrollVectorProvider.class.getCanonicalName());
        Log.w(str, stringBuilder.toString());
        return null;
    }
}
