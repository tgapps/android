package org.telegram.messenger.support.widget;

import android.support.v4.util.Pools.Pool;
import android.support.v4.util.Pools.SimplePool;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;

class AdapterHelper implements Callback {
    private static final boolean DEBUG = false;
    static final int POSITION_TYPE_INVISIBLE = 0;
    static final int POSITION_TYPE_NEW_OR_LAID_OUT = 1;
    private static final String TAG = "AHT";
    final Callback mCallback;
    final boolean mDisableRecycler;
    private int mExistingUpdateTypes;
    Runnable mOnItemProcessedCallback;
    final OpReorderer mOpReorderer;
    final ArrayList<UpdateOp> mPendingUpdates;
    final ArrayList<UpdateOp> mPostponedList;
    private Pool<UpdateOp> mUpdateOpPool;

    interface Callback {
        ViewHolder findViewHolder(int i);

        void markViewHoldersUpdated(int i, int i2, Object obj);

        void offsetPositionsForAdd(int i, int i2);

        void offsetPositionsForMove(int i, int i2);

        void offsetPositionsForRemovingInvisible(int i, int i2);

        void offsetPositionsForRemovingLaidOutOrNewView(int i, int i2);

        void onDispatchFirstPass(UpdateOp updateOp);

        void onDispatchSecondPass(UpdateOp updateOp);
    }

    static class UpdateOp {
        static final int ADD = 1;
        static final int MOVE = 8;
        static final int POOL_SIZE = 30;
        static final int REMOVE = 2;
        static final int UPDATE = 4;
        int cmd;
        int itemCount;
        Object payload;
        int positionStart;

        UpdateOp(int cmd, int positionStart, int itemCount, Object payload) {
            this.cmd = cmd;
            this.positionStart = positionStart;
            this.itemCount = itemCount;
            this.payload = payload;
        }

        String cmdToString() {
            int i = this.cmd;
            if (i == 4) {
                return "up";
            }
            if (i == 8) {
                return "mv";
            }
            switch (i) {
                case 1:
                    return "add";
                case 2:
                    return "rm";
                default:
                    return "??";
            }
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append("[");
            stringBuilder.append(cmdToString());
            stringBuilder.append(",s:");
            stringBuilder.append(this.positionStart);
            stringBuilder.append("c:");
            stringBuilder.append(this.itemCount);
            stringBuilder.append(",p:");
            stringBuilder.append(this.payload);
            stringBuilder.append("]");
            return stringBuilder.toString();
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o != null) {
                if (getClass() == o.getClass()) {
                    UpdateOp op = (UpdateOp) o;
                    if (this.cmd != op.cmd) {
                        return false;
                    }
                    if (this.cmd == 8 && Math.abs(this.itemCount - this.positionStart) == 1 && this.itemCount == op.positionStart && this.positionStart == op.itemCount) {
                        return true;
                    }
                    if (this.itemCount != op.itemCount || this.positionStart != op.positionStart) {
                        return false;
                    }
                    if (this.payload != null) {
                        if (!this.payload.equals(op.payload)) {
                            return false;
                        }
                    } else if (op.payload != null) {
                        return false;
                    }
                    return true;
                }
            }
            return false;
        }

        public int hashCode() {
            return (31 * ((31 * this.cmd) + this.positionStart)) + this.itemCount;
        }
    }

    private void dispatchAndUpdateViewHolders(org.telegram.messenger.support.widget.AdapterHelper.UpdateOp r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.widget.AdapterHelper.dispatchAndUpdateViewHolders(org.telegram.messenger.support.widget.AdapterHelper$UpdateOp):void
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
        r0 = r14.cmd;
        r1 = 1;
        if (r0 == r1) goto L_0x0095;
    L_0x0005:
        r0 = r14.cmd;
        r2 = 8;
        if (r0 != r2) goto L_0x000d;
    L_0x000b:
        goto L_0x0095;
    L_0x000d:
        r0 = r14.positionStart;
        r2 = r14.cmd;
        r0 = r13.updatePositionWithPostponed(r0, r2);
        r2 = 1;
        r3 = r14.positionStart;
        r4 = r14.cmd;
        r5 = 2;
        r6 = 4;
        if (r4 == r5) goto L_0x0039;
    L_0x001e:
        if (r4 == r6) goto L_0x0037;
    L_0x0020:
        r1 = new java.lang.IllegalArgumentException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "op should be remove or update.";
        r4.append(r5);
        r4.append(r14);
        r4 = r4.toString();
        r1.<init>(r4);
        throw r1;
    L_0x0037:
        r4 = 1;
        goto L_0x003b;
    L_0x0039:
        r4 = 0;
        r7 = r3;
        r3 = r0;
        r0 = r1;
        r8 = r14.itemCount;
        if (r0 >= r8) goto L_0x0081;
        r8 = r14.positionStart;
        r9 = r4 * r0;
        r8 = r8 + r9;
        r9 = r14.cmd;
        r9 = r13.updatePositionWithPostponed(r8, r9);
        r10 = 0;
        r11 = r14.cmd;
        r12 = 0;
        if (r11 == r5) goto L_0x005f;
        if (r11 == r6) goto L_0x0057;
        goto L_0x0064;
        r11 = r3 + 1;
        if (r9 != r11) goto L_0x005d;
        r12 = r1;
        r10 = r12;
        goto L_0x0064;
        if (r9 != r3) goto L_0x0063;
        r12 = r1;
        r10 = r12;
        if (r10 == 0) goto L_0x0069;
        r2 = r2 + 1;
        goto L_0x007e;
        r11 = r14.cmd;
        r12 = r14.payload;
        r11 = r13.obtainUpdateOp(r11, r3, r2, r12);
        r13.dispatchFirstPassAndUpdateViewHolders(r11, r7);
        r13.recycleUpdateOp(r11);
        r12 = r14.cmd;
        if (r12 != r6) goto L_0x007c;
        r7 = r7 + r2;
        r3 = r9;
        r2 = 1;
        r0 = r0 + 1;
        goto L_0x003f;
        r0 = r14.payload;
        r13.recycleUpdateOp(r14);
        if (r2 <= 0) goto L_0x0094;
        r1 = r14.cmd;
        r1 = r13.obtainUpdateOp(r1, r3, r2, r0);
        r13.dispatchFirstPassAndUpdateViewHolders(r1, r7);
        r13.recycleUpdateOp(r1);
        return;
    L_0x0095:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "should not dispatch add or move for pre layout";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.widget.AdapterHelper.dispatchAndUpdateViewHolders(org.telegram.messenger.support.widget.AdapterHelper$UpdateOp):void");
    }

    AdapterHelper(Callback callback) {
        this(callback, false);
    }

    AdapterHelper(Callback callback, boolean disableRecycler) {
        this.mUpdateOpPool = new SimplePool(30);
        this.mPendingUpdates = new ArrayList();
        this.mPostponedList = new ArrayList();
        this.mExistingUpdateTypes = 0;
        this.mCallback = callback;
        this.mDisableRecycler = disableRecycler;
        this.mOpReorderer = new OpReorderer(this);
    }

    AdapterHelper addUpdateOp(UpdateOp... ops) {
        Collections.addAll(this.mPendingUpdates, ops);
        return this;
    }

    void reset() {
        recycleUpdateOpsAndClearList(this.mPendingUpdates);
        recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }

    void preProcess() {
        this.mOpReorderer.reorderOps(this.mPendingUpdates);
        int count = this.mPendingUpdates.size();
        for (int i = 0; i < count; i++) {
            UpdateOp op = (UpdateOp) this.mPendingUpdates.get(i);
            int i2 = op.cmd;
            if (i2 == 4) {
                applyUpdate(op);
            } else if (i2 != 8) {
                switch (i2) {
                    case 1:
                        applyAdd(op);
                        break;
                    case 2:
                        applyRemove(op);
                        break;
                    default:
                        break;
                }
            } else {
                applyMove(op);
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        this.mPendingUpdates.clear();
    }

    void consumePostponedUpdates() {
        int count = this.mPostponedList.size();
        for (int i = 0; i < count; i++) {
            this.mCallback.onDispatchSecondPass((UpdateOp) this.mPostponedList.get(i));
        }
        recycleUpdateOpsAndClearList(this.mPostponedList);
        this.mExistingUpdateTypes = 0;
    }

    private void applyMove(UpdateOp op) {
        postponeAndUpdateViewHolders(op);
    }

    private void applyRemove(UpdateOp op) {
        int tmpStart = op.positionStart;
        int tmpCount = 0;
        int tmpEnd = op.positionStart + op.itemCount;
        int type = -1;
        int position = op.positionStart;
        while (position < tmpEnd) {
            boolean typeChanged = false;
            if (this.mCallback.findViewHolder(position) == null) {
                if (!canFindInPreLayout(position)) {
                    if (type == 1) {
                        postponeAndUpdateViewHolders(obtainUpdateOp(2, tmpStart, tmpCount, null));
                        typeChanged = true;
                    }
                    type = 0;
                    if (typeChanged) {
                        tmpCount++;
                    } else {
                        position -= tmpCount;
                        tmpEnd -= tmpCount;
                        tmpCount = 1;
                    }
                    position++;
                }
            }
            if (type == 0) {
                dispatchAndUpdateViewHolders(obtainUpdateOp(2, tmpStart, tmpCount, null));
                typeChanged = true;
            }
            type = 1;
            if (typeChanged) {
                tmpCount++;
            } else {
                position -= tmpCount;
                tmpEnd -= tmpCount;
                tmpCount = 1;
            }
            position++;
        }
        if (tmpCount != op.itemCount) {
            recycleUpdateOp(op);
            op = obtainUpdateOp(2, tmpStart, tmpCount, null);
        }
        if (type == 0) {
            dispatchAndUpdateViewHolders(op);
        } else {
            postponeAndUpdateViewHolders(op);
        }
    }

    private void applyUpdate(UpdateOp op) {
        int tmpStart = op.positionStart;
        int tmpCount = 0;
        int tmpEnd = op.positionStart + op.itemCount;
        int type = -1;
        for (int position = op.positionStart; position < tmpEnd; position++) {
            if (this.mCallback.findViewHolder(position) == null) {
                if (!canFindInPreLayout(position)) {
                    if (type == 1) {
                        postponeAndUpdateViewHolders(obtainUpdateOp(4, tmpStart, tmpCount, op.payload));
                        tmpCount = 0;
                        tmpStart = position;
                    }
                    type = 0;
                    tmpCount++;
                }
            }
            if (type == 0) {
                dispatchAndUpdateViewHolders(obtainUpdateOp(4, tmpStart, tmpCount, op.payload));
                tmpCount = 0;
                tmpStart = position;
            }
            type = 1;
            tmpCount++;
        }
        if (tmpCount != op.itemCount) {
            Object payload = op.payload;
            recycleUpdateOp(op);
            op = obtainUpdateOp(4, tmpStart, tmpCount, payload);
        }
        if (type == 0) {
            dispatchAndUpdateViewHolders(op);
        } else {
            postponeAndUpdateViewHolders(op);
        }
    }

    void dispatchFirstPassAndUpdateViewHolders(UpdateOp op, int offsetStart) {
        this.mCallback.onDispatchFirstPass(op);
        int i = op.cmd;
        if (i == 2) {
            this.mCallback.offsetPositionsForRemovingInvisible(offsetStart, op.itemCount);
        } else if (i != 4) {
            throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
        } else {
            this.mCallback.markViewHoldersUpdated(offsetStart, op.itemCount, op.payload);
        }
    }

    private int updatePositionWithPostponed(int pos, int cmd) {
        int i;
        for (i = this.mPostponedList.size() - 1; i >= 0; i--) {
            UpdateOp postponed = (UpdateOp) this.mPostponedList.get(i);
            if (postponed.cmd == 8) {
                int start;
                int end;
                if (postponed.positionStart < postponed.itemCount) {
                    start = postponed.positionStart;
                    end = postponed.itemCount;
                } else {
                    start = postponed.itemCount;
                    end = postponed.positionStart;
                }
                if (pos < start || pos > end) {
                    if (pos < postponed.positionStart) {
                        if (cmd == 1) {
                            postponed.positionStart++;
                            postponed.itemCount++;
                        } else if (cmd == 2) {
                            postponed.positionStart--;
                            postponed.itemCount--;
                        }
                    }
                } else if (start == postponed.positionStart) {
                    if (cmd == 1) {
                        postponed.itemCount++;
                    } else if (cmd == 2) {
                        postponed.itemCount--;
                    }
                    pos++;
                } else {
                    if (cmd == 1) {
                        postponed.positionStart++;
                    } else if (cmd == 2) {
                        postponed.positionStart--;
                    }
                    pos--;
                }
            } else if (postponed.positionStart <= pos) {
                if (postponed.cmd == 1) {
                    pos -= postponed.itemCount;
                } else if (postponed.cmd == 2) {
                    pos += postponed.itemCount;
                }
            } else if (cmd == 1) {
                postponed.positionStart++;
            } else if (cmd == 2) {
                postponed.positionStart--;
            }
        }
        for (i = this.mPostponedList.size() - 1; i >= 0; i--) {
            UpdateOp op = (UpdateOp) this.mPostponedList.get(i);
            if (op.cmd == 8) {
                if (op.itemCount == op.positionStart || op.itemCount < 0) {
                    this.mPostponedList.remove(i);
                    recycleUpdateOp(op);
                }
            } else if (op.itemCount <= 0) {
                this.mPostponedList.remove(i);
                recycleUpdateOp(op);
            }
        }
        return pos;
    }

    private boolean canFindInPreLayout(int position) {
        int count = this.mPostponedList.size();
        for (int i = 0; i < count; i++) {
            UpdateOp op = (UpdateOp) this.mPostponedList.get(i);
            if (op.cmd == 8) {
                if (findPositionOffset(op.itemCount, i + 1) == position) {
                    return true;
                }
            } else if (op.cmd == 1) {
                int end = op.positionStart + op.itemCount;
                for (int pos = op.positionStart; pos < end; pos++) {
                    if (findPositionOffset(pos, i + 1) == position) {
                        return true;
                    }
                }
                continue;
            } else {
                continue;
            }
        }
        return false;
    }

    private void applyAdd(UpdateOp op) {
        postponeAndUpdateViewHolders(op);
    }

    private void postponeAndUpdateViewHolders(UpdateOp op) {
        this.mPostponedList.add(op);
        int i = op.cmd;
        if (i == 4) {
            this.mCallback.markViewHoldersUpdated(op.positionStart, op.itemCount, op.payload);
        } else if (i != 8) {
            switch (i) {
                case 1:
                    this.mCallback.offsetPositionsForAdd(op.positionStart, op.itemCount);
                    return;
                case 2:
                    this.mCallback.offsetPositionsForRemovingLaidOutOrNewView(op.positionStart, op.itemCount);
                    return;
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown update op type for ");
                    stringBuilder.append(op);
                    throw new IllegalArgumentException(stringBuilder.toString());
            }
        } else {
            this.mCallback.offsetPositionsForMove(op.positionStart, op.itemCount);
        }
    }

    boolean hasPendingUpdates() {
        return this.mPendingUpdates.size() > 0;
    }

    boolean hasAnyUpdateTypes(int updateTypes) {
        return (this.mExistingUpdateTypes & updateTypes) != 0;
    }

    int findPositionOffset(int position) {
        return findPositionOffset(position, 0);
    }

    int findPositionOffset(int position, int firstPostponedItem) {
        int count = this.mPostponedList.size();
        int position2 = position;
        for (position = firstPostponedItem; position < count; position++) {
            UpdateOp op = (UpdateOp) this.mPostponedList.get(position);
            if (op.cmd == 8) {
                if (op.positionStart == position2) {
                    position2 = op.itemCount;
                } else {
                    if (op.positionStart < position2) {
                        position2--;
                    }
                    if (op.itemCount <= position2) {
                        position2++;
                    }
                }
            } else if (op.positionStart > position2) {
                continue;
            } else if (op.cmd == 2) {
                if (position2 < op.positionStart + op.itemCount) {
                    return -1;
                }
                position2 -= op.itemCount;
            } else if (op.cmd == 1) {
                position2 += op.itemCount;
            }
        }
        return position2;
    }

    boolean onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        boolean z = false;
        if (itemCount < 1) {
            return false;
        }
        this.mPendingUpdates.add(obtainUpdateOp(4, positionStart, itemCount, payload));
        this.mExistingUpdateTypes |= 4;
        if (this.mPendingUpdates.size() == 1) {
            z = true;
        }
        return z;
    }

    boolean onItemRangeInserted(int positionStart, int itemCount) {
        boolean z = false;
        if (itemCount < 1) {
            return false;
        }
        this.mPendingUpdates.add(obtainUpdateOp(1, positionStart, itemCount, null));
        this.mExistingUpdateTypes |= 1;
        if (this.mPendingUpdates.size() == 1) {
            z = true;
        }
        return z;
    }

    boolean onItemRangeRemoved(int positionStart, int itemCount) {
        boolean z = false;
        if (itemCount < 1) {
            return false;
        }
        this.mPendingUpdates.add(obtainUpdateOp(2, positionStart, itemCount, null));
        this.mExistingUpdateTypes |= 2;
        if (this.mPendingUpdates.size() == 1) {
            z = true;
        }
        return z;
    }

    boolean onItemRangeMoved(int from, int to, int itemCount) {
        boolean z = false;
        if (from == to) {
            return false;
        }
        if (itemCount != 1) {
            throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
        }
        this.mPendingUpdates.add(obtainUpdateOp(8, from, to, null));
        this.mExistingUpdateTypes |= 8;
        if (this.mPendingUpdates.size() == 1) {
            z = true;
        }
        return z;
    }

    void consumeUpdatesInOnePass() {
        consumePostponedUpdates();
        int count = this.mPendingUpdates.size();
        for (int i = 0; i < count; i++) {
            UpdateOp op = (UpdateOp) this.mPendingUpdates.get(i);
            int i2 = op.cmd;
            if (i2 == 4) {
                this.mCallback.onDispatchSecondPass(op);
                this.mCallback.markViewHoldersUpdated(op.positionStart, op.itemCount, op.payload);
            } else if (i2 != 8) {
                switch (i2) {
                    case 1:
                        this.mCallback.onDispatchSecondPass(op);
                        this.mCallback.offsetPositionsForAdd(op.positionStart, op.itemCount);
                        break;
                    case 2:
                        this.mCallback.onDispatchSecondPass(op);
                        this.mCallback.offsetPositionsForRemovingInvisible(op.positionStart, op.itemCount);
                        break;
                    default:
                        break;
                }
            } else {
                this.mCallback.onDispatchSecondPass(op);
                this.mCallback.offsetPositionsForMove(op.positionStart, op.itemCount);
            }
            if (this.mOnItemProcessedCallback != null) {
                this.mOnItemProcessedCallback.run();
            }
        }
        recycleUpdateOpsAndClearList(this.mPendingUpdates);
        this.mExistingUpdateTypes = 0;
    }

    public int applyPendingUpdatesToPosition(int position) {
        int size = this.mPendingUpdates.size();
        for (int i = 0; i < size; i++) {
            UpdateOp op = (UpdateOp) this.mPendingUpdates.get(i);
            int i2 = op.cmd;
            if (i2 != 8) {
                switch (i2) {
                    case 1:
                        if (op.positionStart > position) {
                            break;
                        }
                        position += op.itemCount;
                        break;
                    case 2:
                        if (op.positionStart <= position) {
                            if (op.positionStart + op.itemCount <= position) {
                                position -= op.itemCount;
                                break;
                            }
                            return -1;
                        }
                        continue;
                    default:
                        break;
                }
            } else if (op.positionStart == position) {
                position = op.itemCount;
            } else {
                if (op.positionStart < position) {
                    position--;
                }
                if (op.itemCount <= position) {
                    position++;
                }
            }
        }
        return position;
    }

    boolean hasUpdates() {
        return (this.mPostponedList.isEmpty() || this.mPendingUpdates.isEmpty()) ? false : true;
    }

    public UpdateOp obtainUpdateOp(int cmd, int positionStart, int itemCount, Object payload) {
        UpdateOp op = (UpdateOp) this.mUpdateOpPool.acquire();
        if (op == null) {
            return new UpdateOp(cmd, positionStart, itemCount, payload);
        }
        op.cmd = cmd;
        op.positionStart = positionStart;
        op.itemCount = itemCount;
        op.payload = payload;
        return op;
    }

    public void recycleUpdateOp(UpdateOp op) {
        if (!this.mDisableRecycler) {
            op.payload = null;
            this.mUpdateOpPool.release(op);
        }
    }

    void recycleUpdateOpsAndClearList(List<UpdateOp> ops) {
        int count = ops.size();
        for (int i = 0; i < count; i++) {
            recycleUpdateOp((UpdateOp) ops.get(i));
        }
        ops.clear();
    }
}
