package org.telegram.messenger.support.widget;

import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.Pools.Pool;
import android.support.v4.util.Pools.SimplePool;
import org.telegram.messenger.support.widget.RecyclerView.ItemAnimator.ItemHolderInfo;
import org.telegram.messenger.support.widget.RecyclerView.ViewHolder;

class ViewInfoStore {
    private static final boolean DEBUG = false;
    final ArrayMap<ViewHolder, InfoRecord> mLayoutHolderMap = new ArrayMap();
    final LongSparseArray<ViewHolder> mOldChangedHolders = new LongSparseArray();

    static class InfoRecord {
        static final int FLAG_APPEAR = 2;
        static final int FLAG_APPEAR_AND_DISAPPEAR = 3;
        static final int FLAG_APPEAR_PRE_AND_POST = 14;
        static final int FLAG_DISAPPEARED = 1;
        static final int FLAG_POST = 8;
        static final int FLAG_PRE = 4;
        static final int FLAG_PRE_AND_POST = 12;
        static Pool<InfoRecord> sPool = new SimplePool(20);
        int flags;
        ItemHolderInfo postInfo;
        ItemHolderInfo preInfo;

        private InfoRecord() {
        }

        static InfoRecord obtain() {
            InfoRecord record = (InfoRecord) sPool.acquire();
            return record == null ? new InfoRecord() : record;
        }

        static void recycle(InfoRecord record) {
            record.flags = 0;
            record.preInfo = null;
            record.postInfo = null;
            sPool.release(record);
        }

        static void drainCache() {
            while (sPool.acquire() != null) {
            }
        }
    }

    interface ProcessCallback {
        void processAppeared(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        void processDisappeared(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        void processPersistent(ViewHolder viewHolder, ItemHolderInfo itemHolderInfo, ItemHolderInfo itemHolderInfo2);

        void unused(ViewHolder viewHolder);
    }

    private org.telegram.messenger.support.widget.RecyclerView.ItemAnimator.ItemHolderInfo popFromLayoutStep(org.telegram.messenger.support.widget.RecyclerView.ViewHolder r1, int r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.widget.ViewInfoStore.popFromLayoutStep(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):org.telegram.messenger.support.widget.RecyclerView$ItemAnimator$ItemHolderInfo
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
        r0 = r4.mLayoutHolderMap;
        r0 = r0.indexOfKey(r5);
        r1 = 0;
        if (r0 >= 0) goto L_0x000a;
    L_0x0009:
        return r1;
    L_0x000a:
        r2 = r4.mLayoutHolderMap;
        r2 = r2.valueAt(r0);
        r2 = (org.telegram.messenger.support.widget.ViewInfoStore.InfoRecord) r2;
        if (r2 == 0) goto L_0x0045;
    L_0x0014:
        r3 = r2.flags;
        r3 = r3 & r6;
        if (r3 == 0) goto L_0x0045;
    L_0x0019:
        r1 = r2.flags;
        r3 = r6 ^ -1;
        r1 = r1 & r3;
        r2.flags = r1;
        r1 = 4;
        if (r6 != r1) goto L_0x0026;
    L_0x0023:
        r1 = r2.preInfo;
        goto L_0x002d;
    L_0x0026:
        r1 = 8;
        if (r6 != r1) goto L_0x003d;
        r1 = r2.postInfo;
        goto L_0x0025;
        r3 = r2.flags;
        r3 = r3 & 12;
        if (r3 != 0) goto L_0x003c;
        r3 = r4.mLayoutHolderMap;
        r3.removeAt(r0);
        org.telegram.messenger.support.widget.ViewInfoStore.InfoRecord.recycle(r2);
        return r1;
        r1 = new java.lang.IllegalArgumentException;
        r3 = "Must provide flag PRE or POST";
        r1.<init>(r3);
        throw r1;
    L_0x0045:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.widget.ViewInfoStore.popFromLayoutStep(org.telegram.messenger.support.widget.RecyclerView$ViewHolder, int):org.telegram.messenger.support.widget.RecyclerView$ItemAnimator$ItemHolderInfo");
    }

    ViewInfoStore() {
    }

    void clear() {
        this.mLayoutHolderMap.clear();
        this.mOldChangedHolders.clear();
    }

    void addToPreLayout(ViewHolder holder, ItemHolderInfo info) {
        InfoRecord record = (InfoRecord) this.mLayoutHolderMap.get(holder);
        if (record == null) {
            record = InfoRecord.obtain();
            this.mLayoutHolderMap.put(holder, record);
        }
        record.preInfo = info;
        record.flags |= 4;
    }

    boolean isDisappearing(ViewHolder holder) {
        InfoRecord record = (InfoRecord) this.mLayoutHolderMap.get(holder);
        return (record == null || (record.flags & 1) == 0) ? false : true;
    }

    ItemHolderInfo popFromPreLayout(ViewHolder vh) {
        return popFromLayoutStep(vh, 4);
    }

    ItemHolderInfo popFromPostLayout(ViewHolder vh) {
        return popFromLayoutStep(vh, 8);
    }

    void addToOldChangeHolders(long key, ViewHolder holder) {
        this.mOldChangedHolders.put(key, holder);
    }

    void addToAppearedInPreLayoutHolders(ViewHolder holder, ItemHolderInfo info) {
        InfoRecord record = (InfoRecord) this.mLayoutHolderMap.get(holder);
        if (record == null) {
            record = InfoRecord.obtain();
            this.mLayoutHolderMap.put(holder, record);
        }
        record.flags |= 2;
        record.preInfo = info;
    }

    boolean isInPreLayout(ViewHolder viewHolder) {
        InfoRecord record = (InfoRecord) this.mLayoutHolderMap.get(viewHolder);
        return (record == null || (record.flags & 4) == 0) ? false : true;
    }

    ViewHolder getFromOldChangeHolders(long key) {
        return (ViewHolder) this.mOldChangedHolders.get(key);
    }

    void addToPostLayout(ViewHolder holder, ItemHolderInfo info) {
        InfoRecord record = (InfoRecord) this.mLayoutHolderMap.get(holder);
        if (record == null) {
            record = InfoRecord.obtain();
            this.mLayoutHolderMap.put(holder, record);
        }
        record.postInfo = info;
        record.flags |= 8;
    }

    void addToDisappearedInLayout(ViewHolder holder) {
        InfoRecord record = (InfoRecord) this.mLayoutHolderMap.get(holder);
        if (record == null) {
            record = InfoRecord.obtain();
            this.mLayoutHolderMap.put(holder, record);
        }
        record.flags |= 1;
    }

    void removeFromDisappearedInLayout(ViewHolder holder) {
        InfoRecord record = (InfoRecord) this.mLayoutHolderMap.get(holder);
        if (record != null) {
            record.flags &= -2;
        }
    }

    void process(ProcessCallback callback) {
        for (int index = this.mLayoutHolderMap.size() - 1; index >= 0; index--) {
            ViewHolder viewHolder = (ViewHolder) this.mLayoutHolderMap.keyAt(index);
            InfoRecord record = (InfoRecord) this.mLayoutHolderMap.removeAt(index);
            if ((record.flags & 3) == 3) {
                callback.unused(viewHolder);
            } else if ((record.flags & 1) != 0) {
                if (record.preInfo == null) {
                    callback.unused(viewHolder);
                } else {
                    callback.processDisappeared(viewHolder, record.preInfo, record.postInfo);
                }
            } else if ((record.flags & 14) == 14) {
                callback.processAppeared(viewHolder, record.preInfo, record.postInfo);
            } else if ((record.flags & 12) == 12) {
                callback.processPersistent(viewHolder, record.preInfo, record.postInfo);
            } else if ((record.flags & 4) != 0) {
                callback.processDisappeared(viewHolder, record.preInfo, null);
            } else if ((record.flags & 8) != 0) {
                callback.processAppeared(viewHolder, record.preInfo, record.postInfo);
            } else {
                int i = record.flags;
            }
            InfoRecord.recycle(record);
        }
    }

    void removeViewHolder(ViewHolder holder) {
        for (int i = this.mOldChangedHolders.size() - 1; i >= 0; i--) {
            if (holder == this.mOldChangedHolders.valueAt(i)) {
                this.mOldChangedHolders.removeAt(i);
                break;
            }
        }
        InfoRecord info = (InfoRecord) this.mLayoutHolderMap.remove(holder);
        if (info != null) {
            InfoRecord.recycle(info);
        }
    }

    void onDetach() {
        InfoRecord.drainCache();
    }

    public void onViewDetached(ViewHolder viewHolder) {
        removeFromDisappearedInLayout(viewHolder);
    }
}
