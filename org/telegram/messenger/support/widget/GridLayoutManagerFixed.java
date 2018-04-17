package org.telegram.messenger.support.widget;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import java.util.ArrayList;
import org.telegram.messenger.support.widget.RecyclerView.LayoutManager;
import org.telegram.messenger.support.widget.RecyclerView.LayoutParams;
import org.telegram.messenger.support.widget.RecyclerView.Recycler;

public class GridLayoutManagerFixed extends GridLayoutManager {
    private ArrayList<View> additionalViews = new ArrayList(4);
    private boolean canScrollVertically = true;

    void layoutChunk(org.telegram.messenger.support.widget.RecyclerView.Recycler r1, org.telegram.messenger.support.widget.RecyclerView.State r2, org.telegram.messenger.support.widget.LinearLayoutManager.LayoutState r3, org.telegram.messenger.support.widget.LinearLayoutManager.LayoutChunkResult r4) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.widget.GridLayoutManagerFixed.layoutChunk(org.telegram.messenger.support.widget.RecyclerView$Recycler, org.telegram.messenger.support.widget.RecyclerView$State, org.telegram.messenger.support.widget.LinearLayoutManager$LayoutState, org.telegram.messenger.support.widget.LinearLayoutManager$LayoutChunkResult):void
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
        r6 = r30;
        r7 = r31;
        r8 = r32;
        r9 = r33;
        r10 = r34;
        r0 = r6.mOrientationHelper;
        r11 = r0.getModeInOther();
        r0 = r9.mItemDirection;
        r12 = 0;
        r13 = 1;
        if (r0 != r13) goto L_0x0018;
    L_0x0016:
        r0 = r13;
        goto L_0x0019;
    L_0x0018:
        r0 = r12;
    L_0x0019:
        r14 = r0;
        r0 = 1;
        r10.mConsumed = r12;
        r15 = 0;
        r5 = r9.mCurrentPosition;
        r1 = r9.mLayoutDirection;
        r4 = -1;
        if (r1 == r4) goto L_0x007c;
    L_0x0025:
        r1 = r9.mCurrentPosition;
        r1 = r6.hasSiblingChild(r1);
        if (r1 == 0) goto L_0x007c;
    L_0x002d:
        r1 = r9.mCurrentPosition;
        r1 = r1 + r13;
        r1 = r6.findViewByPosition(r1);
        if (r1 != 0) goto L_0x007c;
    L_0x0036:
        r1 = r9.mCurrentPosition;
        r1 = r1 + r13;
        r1 = r6.hasSiblingChild(r1);
        if (r1 == 0) goto L_0x0046;
    L_0x003f:
        r1 = r9.mCurrentPosition;
        r1 = r1 + 3;
        r9.mCurrentPosition = r1;
        goto L_0x004c;
    L_0x0046:
        r1 = r9.mCurrentPosition;
        r1 = r1 + 2;
        r9.mCurrentPosition = r1;
    L_0x004c:
        r1 = r9.mCurrentPosition;
        r2 = r9.mCurrentPosition;
    L_0x0050:
        if (r2 <= r5) goto L_0x007a;
    L_0x0052:
        r3 = r9.next(r7);
        r4 = r6.additionalViews;
        r4.add(r3);
        if (r2 == r1) goto L_0x0075;
    L_0x005d:
        r4 = r6.mDecorInsets;
        r6.calculateItemDecorationsForChild(r3, r4);
        r6.measureChild(r3, r11, r12);
        r4 = r6.mOrientationHelper;
        r4 = r4.getDecoratedMeasurement(r3);
        r12 = r9.mOffset;
        r12 = r12 - r4;
        r9.mOffset = r12;
        r12 = r9.mAvailable;
        r12 = r12 + r4;
        r9.mAvailable = r12;
    L_0x0075:
        r2 = r2 + -1;
        r4 = -1;
        r12 = 0;
        goto L_0x0050;
    L_0x007a:
        r9.mCurrentPosition = r1;
    L_0x007c:
        if (r0 == 0) goto L_0x02ed;
    L_0x007e:
        r1 = 0;
        r2 = 0;
        r3 = r6.mSpanCount;
        r4 = r6.additionalViews;
        r4 = r4.isEmpty();
        r4 = r4 ^ r13;
        r0 = r4;
        r4 = r9.mCurrentPosition;
        r18 = r0;
        r12 = r1;
        r19 = r2;
        r20 = r4;
        r0 = r6.mSpanCount;
        if (r12 >= r0) goto L_0x00ed;
    L_0x0097:
        r0 = r9.hasMore(r8);
        if (r0 == 0) goto L_0x00ed;
    L_0x009d:
        if (r3 <= 0) goto L_0x00ed;
    L_0x009f:
        r0 = r9.mCurrentPosition;
        r1 = r6.getSpanSize(r7, r8, r0);
        r3 = r3 - r1;
        if (r3 >= 0) goto L_0x00a9;
    L_0x00a8:
        goto L_0x00cd;
    L_0x00a9:
        r2 = r6.additionalViews;
        r2 = r2.isEmpty();
        if (r2 != 0) goto L_0x00c6;
    L_0x00b1:
        r2 = r6.additionalViews;
        r4 = 0;
        r2 = r2.get(r4);
        r2 = (android.view.View) r2;
        r13 = r6.additionalViews;
        r13.remove(r4);
        r4 = r9.mCurrentPosition;
        r13 = 1;
        r4 = r4 - r13;
        r9.mCurrentPosition = r4;
        goto L_0x00ca;
    L_0x00c6:
        r2 = r9.next(r7);
    L_0x00ca:
        if (r2 != 0) goto L_0x00d1;
    L_0x00cd:
        r16 = r3;
        r13 = -1;
        goto L_0x00f0;
    L_0x00d1:
        r19 = r19 + r1;
        r4 = r6.mSet;
        r4[r12] = r2;
        r12 = r12 + 1;
        r4 = r9.mLayoutDirection;
        r13 = -1;
        if (r4 != r13) goto L_0x00e8;
    L_0x00de:
        if (r3 > 0) goto L_0x00e8;
        r4 = r6.hasSiblingChild(r0);
        if (r4 == 0) goto L_0x00e8;
        r18 = 1;
        r4 = r20;
        r13 = 1;
        goto L_0x0091;
    L_0x00ed:
        r13 = -1;
        r16 = r3;
    L_0x00f0:
        if (r12 != 0) goto L_0x00f6;
        r0 = 1;
        r10.mFinished = r0;
        return;
        r21 = 0;
        r22 = 0;
        r0 = r6;
        r1 = r7;
        r2 = r8;
        r3 = r12;
        r4 = r19;
        r23 = r5;
        r5 = r14;
        r0.assignSpans(r1, r2, r3, r4, r5);
        r5 = r21;
        r0 = 0;
        if (r0 >= r12) goto L_0x015a;
        r1 = r6.mSet;
        r1 = r1[r0];
        r2 = r9.mScrapList;
        if (r2 != 0) goto L_0x011f;
        if (r14 == 0) goto L_0x011a;
        r6.addView(r1);
        r2 = 0;
        goto L_0x0129;
        r2 = 0;
        r6.addView(r1, r2);
        goto L_0x0129;
        r2 = 0;
        if (r14 == 0) goto L_0x0126;
        r6.addDisappearingView(r1);
        goto L_0x0129;
        r6.addDisappearingView(r1, r2);
        r3 = r6.mDecorInsets;
        r6.calculateItemDecorationsForChild(r1, r3);
        r6.measureChild(r1, r11, r2);
        r2 = r6.mOrientationHelper;
        r2 = r2.getDecoratedMeasurement(r1);
        if (r2 <= r5) goto L_0x013b;
        r3 = r2;
        r5 = r3;
        r3 = r1.getLayoutParams();
        r3 = (org.telegram.messenger.support.widget.GridLayoutManager.LayoutParams) r3;
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        r13 = r6.mOrientationHelper;
        r13 = r13.getDecoratedMeasurementInOther(r1);
        r13 = (float) r13;
        r4 = r4 * r13;
        r13 = r3.mSpanSize;
        r13 = (float) r13;
        r4 = r4 / r13;
        r13 = (r4 > r22 ? 1 : (r4 == r22 ? 0 : -1));
        if (r13 <= 0) goto L_0x0156;
        r1 = r4;
        r22 = r1;
        r0 = r0 + 1;
        r13 = -1;
        goto L_0x0109;
        r0 = 0;
        if (r0 >= r12) goto L_0x01ab;
        r1 = r6.mSet;
        r1 = r1[r0];
        r2 = r6.mOrientationHelper;
        r2 = r2.getDecoratedMeasurement(r1);
        if (r2 == r5) goto L_0x01a4;
        r2 = r1.getLayoutParams();
        r2 = (org.telegram.messenger.support.widget.GridLayoutManager.LayoutParams) r2;
        r3 = r2.mDecorInsets;
        r4 = r3.top;
        r13 = r3.bottom;
        r4 = r4 + r13;
        r13 = r2.topMargin;
        r4 = r4 + r13;
        r13 = r2.bottomMargin;
        r4 = r4 + r13;
        r13 = r3.left;
        r7 = r3.right;
        r13 = r13 + r7;
        r7 = r2.leftMargin;
        r13 = r13 + r7;
        r7 = r2.rightMargin;
        r13 = r13 + r7;
        r7 = r6.mCachedBorders;
        r24 = r3;
        r3 = r2.mSpanSize;
        r3 = r7[r3];
        r7 = r2.width;
        r25 = r2;
        r2 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r8 = 0;
        r7 = org.telegram.messenger.support.widget.RecyclerView.LayoutManager.getChildMeasureSpec(r3, r2, r13, r7, r8);
        r8 = r5 - r4;
        r2 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r2);
        r8 = 1;
        r6.measureChildWithDecorationsAndMargin(r1, r7, r2, r8);
        r0 = r0 + 1;
        r7 = r31;
        r8 = r32;
        goto L_0x015b;
        r0 = r6.mSet;
        r7 = 0;
        r0 = r0[r7];
        r8 = r6.shouldLayoutChildFromOpositeSide(r0);
        if (r8 == 0) goto L_0x01bb;
        r0 = r9.mLayoutDirection;
        r1 = -1;
        if (r0 == r1) goto L_0x01c2;
        if (r8 != 0) goto L_0x0249;
        r0 = r9.mLayoutDirection;
        r1 = 1;
        if (r0 != r1) goto L_0x0249;
        r0 = r9.mLayoutDirection;
        r1 = -1;
        if (r0 != r1) goto L_0x01d3;
        r0 = r9.mOffset;
        r1 = r10.mConsumed;
        r0 = r0 - r1;
        r1 = r0 - r5;
        r2 = 0;
        r17 = r0;
        r13 = r1;
        goto L_0x01e1;
        r0 = r9.mOffset;
        r1 = r10.mConsumed;
        r0 = r0 + r1;
        r1 = r0 + r5;
        r2 = r30.getWidth();
        r13 = r0;
        r17 = r1;
        r0 = r2;
        r1 = r12 + -1;
        r21 = r1;
        if (r21 < 0) goto L_0x023f;
        r1 = r6.mSet;
        r4 = r1[r21];
        r1 = r4.getLayoutParams();
        r3 = r1;
        r3 = (org.telegram.messenger.support.widget.GridLayoutManager.LayoutParams) r3;
        r1 = r6.mOrientationHelper;
        r24 = r1.getDecoratedMeasurementInOther(r4);
        r1 = r9.mLayoutDirection;
        r2 = 1;
        if (r1 != r2) goto L_0x0200;
        r0 = r0 - r24;
        r25 = r0;
        r26 = r25 + r24;
        r0 = r6;
        r1 = r4;
        r2 = r25;
        r7 = r3;
        r3 = r13;
        r27 = r8;
        r8 = r4;
        r4 = r26;
        r26 = r5;
        r5 = r17;
        r0.layoutDecoratedWithMargins(r1, r2, r3, r4, r5);
        r0 = r9.mLayoutDirection;
        r1 = -1;
        if (r0 != r1) goto L_0x021d;
        r25 = r25 + r24;
        r0 = r25;
        r1 = r7.isItemRemoved();
        if (r1 != 0) goto L_0x022b;
        r1 = r7.isItemChanged();
        if (r1 == 0) goto L_0x022e;
        r1 = 1;
        r10.mIgnoreConsumed = r1;
        r1 = r10.mFocusable;
        r2 = r8.hasFocusable();
        r1 = r1 | r2;
        r10.mFocusable = r1;
        r1 = r21 + -1;
        r5 = r26;
        r8 = r27;
        r7 = 0;
        goto L_0x01e4;
        r26 = r5;
        r27 = r8;
        r29 = r11;
        r2 = -1;
        r3 = 1;
        goto L_0x02d3;
        r26 = r5;
        r27 = r8;
        r0 = r9.mLayoutDirection;
        r1 = -1;
        if (r0 != r1) goto L_0x0260;
        r0 = r9.mOffset;
        r1 = r10.mConsumed;
        r0 = r0 - r1;
        r1 = r0 - r26;
        r2 = r30.getWidth();
        r8 = r0;
        r7 = r1;
        goto L_0x026a;
        r0 = r9.mOffset;
        r1 = r10.mConsumed;
        r0 = r0 + r1;
        r5 = r0 + r26;
        r7 = r0;
        r8 = r5;
        r2 = 0;
        r0 = r2;
        r1 = r0;
        r0 = 0;
        r13 = r0;
        if (r13 >= r12) goto L_0x02c8;
        r0 = r6.mSet;
        r5 = r0[r13];
        r0 = r5.getLayoutParams();
        r4 = r0;
        r4 = (org.telegram.messenger.support.widget.GridLayoutManager.LayoutParams) r4;
        r0 = r6.mOrientationHelper;
        r17 = r0.getDecoratedMeasurementInOther(r5);
        r0 = r9.mLayoutDirection;
        r2 = -1;
        if (r0 != r2) goto L_0x0288;
        r1 = r1 - r17;
        r21 = r1;
        r24 = r21 + r17;
        r0 = r6;
        r1 = r5;
        r2 = r21;
        r3 = r7;
        r28 = r7;
        r7 = r4;
        r4 = r24;
        r29 = r11;
        r11 = r5;
        r5 = r8;
        r0.layoutDecoratedWithMargins(r1, r2, r3, r4, r5);
        r0 = r9.mLayoutDirection;
        r2 = -1;
        if (r0 == r2) goto L_0x02a4;
        r21 = r21 + r17;
        r1 = r21;
        r0 = r7.isItemRemoved();
        if (r0 != 0) goto L_0x02b5;
        r0 = r7.isItemChanged();
        if (r0 == 0) goto L_0x02b3;
        goto L_0x02b5;
        r3 = 1;
        goto L_0x02b8;
        r3 = 1;
        r10.mIgnoreConsumed = r3;
        r0 = r10.mFocusable;
        r4 = r11.hasFocusable();
        r0 = r0 | r4;
        r10.mFocusable = r0;
        r0 = r13 + 1;
        r7 = r28;
        r11 = r29;
        goto L_0x026d;
        r28 = r7;
        r29 = r11;
        r2 = -1;
        r3 = 1;
        r0 = r1;
        r17 = r8;
        r13 = r28;
        r1 = r10.mConsumed;
        r1 = r1 + r26;
        r10.mConsumed = r1;
        r1 = r6.mSet;
        r4 = 0;
        java.util.Arrays.fill(r1, r4);
        r13 = r3;
        r0 = r18;
        r5 = r23;
        r11 = r29;
        r7 = r31;
        r8 = r32;
        goto L_0x007c;
    L_0x02ed:
        r23 = r5;
        r29 = r11;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.widget.GridLayoutManagerFixed.layoutChunk(org.telegram.messenger.support.widget.RecyclerView$Recycler, org.telegram.messenger.support.widget.RecyclerView$State, org.telegram.messenger.support.widget.LinearLayoutManager$LayoutState, org.telegram.messenger.support.widget.LinearLayoutManager$LayoutChunkResult):void");
    }

    public GridLayoutManagerFixed(Context context, int spanCount) {
        super(context, spanCount);
    }

    public GridLayoutManagerFixed(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    protected boolean hasSiblingChild(int position) {
        return false;
    }

    public void setCanScrollVertically(boolean value) {
        this.canScrollVertically = value;
    }

    public boolean canScrollVertically() {
        return this.canScrollVertically;
    }

    protected void recycleViewsFromStart(Recycler recycler, int dt) {
        if (dt >= 0) {
            int childCount = getChildCount();
            if (this.mShouldReverseLayout) {
                int i = childCount - 1;
                while (i >= 0) {
                    View child = getChildAt(i);
                    if (child.getBottom() + ((LayoutParams) child.getLayoutParams()).bottomMargin <= dt) {
                        if (child.getTop() + child.getHeight() <= dt) {
                            i--;
                        }
                    }
                    recycleChildren(recycler, childCount - 1, i);
                    return;
                }
            }
            int i2 = 0;
            while (i2 < childCount) {
                View child2 = getChildAt(i2);
                if (this.mOrientationHelper.getDecoratedEnd(child2) <= dt) {
                    if (this.mOrientationHelper.getTransformedEndWithDecoration(child2) <= dt) {
                        i2++;
                    }
                }
                recycleChildren(recycler, 0, i2);
                return;
            }
        }
    }

    protected int[] calculateItemBorders(int[] cachedBorders, int spanCount, int totalSpace) {
        int i = 1;
        if (!(cachedBorders != null && cachedBorders.length == spanCount + 1 && cachedBorders[cachedBorders.length - 1] == totalSpace)) {
            cachedBorders = new int[(spanCount + 1)];
        }
        cachedBorders[0] = 0;
        while (i <= spanCount) {
            cachedBorders[i] = (int) Math.ceil((double) ((((float) i) / ((float) spanCount)) * ((float) totalSpace)));
            i++;
        }
        return cachedBorders;
    }

    public boolean shouldLayoutChildFromOpositeSide(View child) {
        return false;
    }

    protected void measureChild(View view, int otherDirParentSpecMode, boolean alreadyMeasured) {
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        Rect decorInsets = lp.mDecorInsets;
        int horizontalInsets = ((decorInsets.left + decorInsets.right) + lp.leftMargin) + lp.rightMargin;
        measureChildWithDecorationsAndMargin(view, LayoutManager.getChildMeasureSpec(this.mCachedBorders[lp.mSpanSize], otherDirParentSpecMode, horizontalInsets, lp.width, false), LayoutManager.getChildMeasureSpec(this.mOrientationHelper.getTotalSpace(), getHeightMode(), ((decorInsets.top + decorInsets.bottom) + lp.topMargin) + lp.bottomMargin, lp.height, true), alreadyMeasured);
    }
}
