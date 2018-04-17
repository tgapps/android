package org.telegram.messenger.support.widget;

import android.view.View;
import org.telegram.messenger.support.widget.RecyclerView.LayoutManager;
import org.telegram.tgnet.ConnectionsManager;

public class LinearSnapHelper extends SnapHelper {
    private static final float INVALID_DISTANCE = 1.0f;
    private OrientationHelper mHorizontalHelper;
    private OrientationHelper mVerticalHelper;

    public int findTargetSnapPosition(org.telegram.messenger.support.widget.RecyclerView.LayoutManager r1, int r2, int r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.widget.LinearSnapHelper.findTargetSnapPosition(org.telegram.messenger.support.widget.RecyclerView$LayoutManager, int, int):int
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
        r0 = r11 instanceof org.telegram.messenger.support.widget.RecyclerView.SmoothScroller.ScrollVectorProvider;
        r1 = -1;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r11.getItemCount();
        if (r0 != 0) goto L_0x000d;
    L_0x000c:
        return r1;
    L_0x000d:
        r2 = r10.findSnapView(r11);
        if (r2 != 0) goto L_0x0014;
    L_0x0013:
        return r1;
    L_0x0014:
        r3 = r11.getPosition(r2);
        if (r3 != r1) goto L_0x001b;
    L_0x001a:
        return r1;
    L_0x001b:
        r4 = r11;
        r4 = (org.telegram.messenger.support.widget.RecyclerView.SmoothScroller.ScrollVectorProvider) r4;
        r5 = r0 + -1;
        r5 = r4.computeScrollVectorForPosition(r5);
        if (r5 != 0) goto L_0x0027;
    L_0x0026:
        return r1;
    L_0x0027:
        r6 = r11.canScrollHorizontally();
        r7 = 0;
        r8 = 0;
        if (r6 == 0) goto L_0x0040;
    L_0x0030:
        r6 = r10.getHorizontalHelper(r11);
        r6 = r10.estimateNextPositionDiffForFling(r11, r6, r12, r8);
        r9 = r5.x;
        r9 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
        if (r9 >= 0) goto L_0x0041;
    L_0x003e:
        r6 = -r6;
        goto L_0x0041;
    L_0x0040:
        r6 = r8;
    L_0x0041:
        r9 = r11.canScrollVertically();
        if (r9 == 0) goto L_0x0058;
    L_0x0048:
        r9 = r10.getVerticalHelper(r11);
        r8 = r10.estimateNextPositionDiffForFling(r11, r9, r8, r13);
        r9 = r5.y;
        r7 = (r9 > r7 ? 1 : (r9 == r7 ? 0 : -1));
        if (r7 >= 0) goto L_0x0059;
        r8 = -r8;
        goto L_0x0059;
        r7 = r8;
        r8 = r11.canScrollVertically();
        if (r8 == 0) goto L_0x0062;
        r8 = r7;
        goto L_0x0063;
        r8 = r6;
        if (r8 != 0) goto L_0x0066;
        return r1;
        r1 = r3 + r8;
        if (r1 >= 0) goto L_0x006b;
        r1 = 0;
        if (r1 < r0) goto L_0x006f;
        r1 = r0 + -1;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.widget.LinearSnapHelper.findTargetSnapPosition(org.telegram.messenger.support.widget.RecyclerView$LayoutManager, int, int):int");
    }

    public int[] calculateDistanceToFinalSnap(LayoutManager layoutManager, View targetView) {
        int[] out = new int[2];
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToCenter(layoutManager, targetView, getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }
        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToCenter(layoutManager, targetView, getVerticalHelper(layoutManager));
        } else {
            out[1] = 0;
        }
        return out;
    }

    public View findSnapView(LayoutManager layoutManager) {
        if (layoutManager.canScrollVertically()) {
            return findCenterView(layoutManager, getVerticalHelper(layoutManager));
        }
        if (layoutManager.canScrollHorizontally()) {
            return findCenterView(layoutManager, getHorizontalHelper(layoutManager));
        }
        return null;
    }

    private int distanceToCenter(LayoutManager layoutManager, View targetView, OrientationHelper helper) {
        int containerCenter;
        int childCenter = helper.getDecoratedStart(targetView) + (helper.getDecoratedMeasurement(targetView) / 2);
        if (layoutManager.getClipToPadding()) {
            containerCenter = helper.getStartAfterPadding() + (helper.getTotalSpace() / 2);
        } else {
            containerCenter = helper.getEnd() / 2;
        }
        return childCenter - containerCenter;
    }

    private int estimateNextPositionDiffForFling(LayoutManager layoutManager, OrientationHelper helper, int velocityX, int velocityY) {
        int[] distances = calculateScrollDistance(velocityX, velocityY);
        float distancePerChild = computeDistancePerChild(layoutManager, helper);
        if (distancePerChild <= 0.0f) {
            return 0;
        }
        return Math.round(((float) (Math.abs(distances[0]) > Math.abs(distances[1]) ? distances[0] : distances[1])) / distancePerChild);
    }

    private View findCenterView(LayoutManager layoutManager, OrientationHelper helper) {
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return null;
        }
        int center;
        View closestChild = null;
        if (layoutManager.getClipToPadding()) {
            center = helper.getStartAfterPadding() + (helper.getTotalSpace() / 2);
        } else {
            center = helper.getEnd() / 2;
        }
        int absClosest = ConnectionsManager.DEFAULT_DATACENTER_ID;
        for (int i = 0; i < childCount; i++) {
            View child = layoutManager.getChildAt(i);
            int absDistance = Math.abs((helper.getDecoratedStart(child) + (helper.getDecoratedMeasurement(child) / 2)) - center);
            if (absDistance < absClosest) {
                absClosest = absDistance;
                closestChild = child;
            }
        }
        return closestChild;
    }

    private float computeDistancePerChild(LayoutManager layoutManager, OrientationHelper helper) {
        View minPosView = null;
        View maxPosView = null;
        int minPos = ConnectionsManager.DEFAULT_DATACENTER_ID;
        int maxPos = Integer.MIN_VALUE;
        int childCount = layoutManager.getChildCount();
        if (childCount == 0) {
            return INVALID_DISTANCE;
        }
        for (int i = 0; i < childCount; i++) {
            View child = layoutManager.getChildAt(i);
            int pos = layoutManager.getPosition(child);
            if (pos != -1) {
                if (pos < minPos) {
                    minPos = pos;
                    minPosView = child;
                }
                if (pos > maxPos) {
                    maxPos = pos;
                    maxPosView = child;
                }
            }
        }
        if (minPosView != null) {
            if (maxPosView != null) {
                pos = Math.max(helper.getDecoratedEnd(minPosView), helper.getDecoratedEnd(maxPosView)) - Math.min(helper.getDecoratedStart(minPosView), helper.getDecoratedStart(maxPosView));
                if (pos == 0) {
                    return INVALID_DISTANCE;
                }
                return (INVALID_DISTANCE * ((float) pos)) / ((float) ((maxPos - minPos) + 1));
            }
        }
        return INVALID_DISTANCE;
    }

    private OrientationHelper getVerticalHelper(LayoutManager layoutManager) {
        if (this.mVerticalHelper == null || this.mVerticalHelper.mLayoutManager != layoutManager) {
            this.mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager);
        }
        return this.mVerticalHelper;
    }

    private OrientationHelper getHorizontalHelper(LayoutManager layoutManager) {
        if (this.mHorizontalHelper == null || this.mHorizontalHelper.mLayoutManager != layoutManager) {
            this.mHorizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return this.mHorizontalHelper;
    }
}
