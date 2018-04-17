package org.telegram.messenger.support.widget;

import org.telegram.messenger.support.widget.RecyclerView.ItemAnimator;
import org.telegram.messenger.support.widget.RecyclerView.ItemAnimator.ItemHolderInfo;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;

public abstract class SimpleItemAnimator extends ItemAnimator {
    private static final boolean DEBUG = false;
    private static final String TAG = "SimpleItemAnimator";
    boolean mSupportsChangeAnimations = true;

    public abstract boolean animateAdd(ViewHolder viewHolder);

    public abstract boolean animateChange(ViewHolder viewHolder, ViewHolder viewHolder2, int i, int i2, int i3, int i4);

    public boolean animateDisappearance(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, org.telegram.messenger.support.widget.RecyclerView.ItemAnimator.ItemHolderInfo r2, org.telegram.messenger.support.widget.RecyclerView.ItemAnimator.ItemHolderInfo r3) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.widget.SimpleItemAnimator.animateDisappearance(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, org.telegram.messenger.support.widget.RecyclerView$ItemAnimator$ItemHolderInfo, org.telegram.messenger.support.widget.RecyclerView$ItemAnimator$ItemHolderInfo):boolean
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
        r6 = r13.left;
        r7 = r13.top;
        r8 = r12.itemView;
        if (r14 != 0) goto L_0x000d;
    L_0x0008:
        r0 = r8.getLeft();
        goto L_0x000f;
    L_0x000d:
        r0 = r14.left;
    L_0x000f:
        r9 = r0;
        if (r14 != 0) goto L_0x0017;
    L_0x0012:
        r0 = r8.getTop();
        goto L_0x0019;
    L_0x0017:
        r0 = r14.top;
    L_0x0019:
        r10 = r0;
        r0 = r12.isRemoved();
        if (r0 != 0) goto L_0x003d;
    L_0x0020:
        if (r6 != r9) goto L_0x0024;
    L_0x0022:
        if (r7 == r10) goto L_0x003d;
        r0 = r8.getWidth();
        r0 = r0 + r9;
        r1 = r8.getHeight();
        r1 = r1 + r10;
        r8.layout(r9, r10, r0, r1);
        r0 = r11;
        r1 = r12;
        r2 = r6;
        r3 = r7;
        r4 = r9;
        r5 = r10;
        r0 = r0.animateMove(r1, r2, r3, r4, r5);
        return r0;
    L_0x003d:
        r0 = r11.animateRemove(r12);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.widget.SimpleItemAnimator.animateDisappearance(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, org.telegram.messenger.support.widget.RecyclerView$ItemAnimator$ItemHolderInfo, org.telegram.messenger.support.widget.RecyclerView$ItemAnimator$ItemHolderInfo):boolean");
    }

    public abstract boolean animateMove(ViewHolder viewHolder, int i, int i2, int i3, int i4);

    public abstract boolean animateRemove(ViewHolder viewHolder);

    public boolean getSupportsChangeAnimations() {
        return this.mSupportsChangeAnimations;
    }

    public void setSupportsChangeAnimations(boolean supportsChangeAnimations) {
        this.mSupportsChangeAnimations = supportsChangeAnimations;
    }

    public boolean canReuseUpdatedViewHolder(ViewHolder viewHolder) {
        if (this.mSupportsChangeAnimations) {
            if (!viewHolder.isInvalid()) {
                return false;
            }
        }
        return true;
    }

    public boolean animateAppearance(ViewHolder viewHolder, ItemHolderInfo preLayoutInfo, ItemHolderInfo postLayoutInfo) {
        if (preLayoutInfo == null || (preLayoutInfo.left == postLayoutInfo.left && preLayoutInfo.top == postLayoutInfo.top)) {
            return animateAdd(viewHolder);
        }
        return animateMove(viewHolder, preLayoutInfo.left, preLayoutInfo.top, postLayoutInfo.left, postLayoutInfo.top);
    }

    public boolean animatePersistence(ViewHolder viewHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo) {
        if (preInfo.left == postInfo.left) {
            if (preInfo.top == postInfo.top) {
                dispatchMoveFinished(viewHolder);
                return false;
            }
        }
        return animateMove(viewHolder, preInfo.left, preInfo.top, postInfo.left, postInfo.top);
    }

    public boolean animateChange(ViewHolder oldHolder, ViewHolder newHolder, ItemHolderInfo preInfo, ItemHolderInfo postInfo) {
        int toLeft;
        int i;
        int fromLeft = preInfo.left;
        int fromTop = preInfo.top;
        if (newHolder.shouldIgnore()) {
            toLeft = preInfo.left;
            i = preInfo.top;
        } else {
            toLeft = postInfo.left;
            i = postInfo.top;
        }
        return animateChange(oldHolder, newHolder, fromLeft, fromTop, toLeft, i);
    }

    public final void dispatchRemoveFinished(ViewHolder item) {
        onRemoveFinished(item);
        dispatchAnimationFinished(item);
    }

    public final void dispatchMoveFinished(ViewHolder item) {
        onMoveFinished(item);
        dispatchAnimationFinished(item);
    }

    public final void dispatchAddFinished(ViewHolder item) {
        onAddFinished(item);
        dispatchAnimationFinished(item);
    }

    public final void dispatchChangeFinished(ViewHolder item, boolean oldItem) {
        onChangeFinished(item, oldItem);
        dispatchAnimationFinished(item);
    }

    public final void dispatchRemoveStarting(ViewHolder item) {
        onRemoveStarting(item);
    }

    public final void dispatchMoveStarting(ViewHolder item) {
        onMoveStarting(item);
    }

    public final void dispatchAddStarting(ViewHolder item) {
        onAddStarting(item);
    }

    public final void dispatchChangeStarting(ViewHolder item, boolean oldItem) {
        onChangeStarting(item, oldItem);
    }

    public void onRemoveStarting(ViewHolder item) {
    }

    public void onRemoveFinished(ViewHolder item) {
    }

    public void onAddStarting(ViewHolder item) {
    }

    public void onAddFinished(ViewHolder item) {
    }

    public void onMoveStarting(ViewHolder item) {
    }

    public void onMoveFinished(ViewHolder item) {
    }

    public void onChangeStarting(ViewHolder item, boolean oldItem) {
    }

    public void onChangeFinished(ViewHolder item, boolean oldItem) {
    }
}
