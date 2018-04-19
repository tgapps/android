package org.telegram.messenger.support.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

public class SortedList<T> {
    private static final int CAPACITY_GROWTH = 10;
    private static final int DELETION = 2;
    private static final int INSERTION = 1;
    public static final int INVALID_POSITION = -1;
    private static final int LOOKUP = 4;
    private static final int MIN_CAPACITY = 10;
    private BatchedCallback mBatchedCallback;
    private Callback mCallback;
    T[] mData;
    private int mNewDataStart;
    private T[] mOldData;
    private int mOldDataSize;
    private int mOldDataStart;
    private int mSize;
    private final Class<T> mTClass;

    public static abstract class Callback<T2> implements Comparator<T2>, ListUpdateCallback {
        public abstract boolean areContentsTheSame(T2 t2, T2 t22);

        public abstract boolean areItemsTheSame(T2 t2, T2 t22);

        public abstract int compare(T2 t2, T2 t22);

        public abstract void onChanged(int i, int i2);

        public void onChanged(int position, int count, Object payload) {
            onChanged(position, count);
        }

        public Object getChangePayload(T2 t2, T2 t22) {
            return null;
        }
    }

    public static class BatchedCallback<T2> extends Callback<T2> {
        private final BatchingListUpdateCallback mBatchingListUpdateCallback = new BatchingListUpdateCallback(this.mWrappedCallback);
        final Callback<T2> mWrappedCallback;

        public BatchedCallback(Callback<T2> wrappedCallback) {
            this.mWrappedCallback = wrappedCallback;
        }

        public int compare(T2 o1, T2 o2) {
            return this.mWrappedCallback.compare(o1, o2);
        }

        public void onInserted(int position, int count) {
            this.mBatchingListUpdateCallback.onInserted(position, count);
        }

        public void onRemoved(int position, int count) {
            this.mBatchingListUpdateCallback.onRemoved(position, count);
        }

        public void onMoved(int fromPosition, int toPosition) {
            this.mBatchingListUpdateCallback.onMoved(fromPosition, toPosition);
        }

        public void onChanged(int position, int count) {
            this.mBatchingListUpdateCallback.onChanged(position, count, null);
        }

        public void onChanged(int position, int count, Object payload) {
            this.mBatchingListUpdateCallback.onChanged(position, count, payload);
        }

        public boolean areContentsTheSame(T2 oldItem, T2 newItem) {
            return this.mWrappedCallback.areContentsTheSame(oldItem, newItem);
        }

        public boolean areItemsTheSame(T2 item1, T2 item2) {
            return this.mWrappedCallback.areItemsTheSame(item1, item2);
        }

        public Object getChangePayload(T2 item1, T2 item2) {
            return this.mWrappedCallback.getChangePayload(item1, item2);
        }

        public void dispatchLastEvent() {
            this.mBatchingListUpdateCallback.dispatchLastEvent();
        }
    }

    public SortedList(Class<T> klass, Callback<T> callback) {
        this(klass, callback, 10);
    }

    public SortedList(Class<T> klass, Callback<T> callback, int initialCapacity) {
        this.mTClass = klass;
        this.mData = (Object[]) Array.newInstance(klass, initialCapacity);
        this.mCallback = callback;
        this.mSize = 0;
    }

    public int size() {
        return this.mSize;
    }

    public int add(T item) {
        throwIfInMutationOperation();
        return add(item, true);
    }

    public void addAll(T[] items, boolean mayModifyInput) {
        throwIfInMutationOperation();
        if (items.length != 0) {
            if (mayModifyInput) {
                addAllInternal(items);
            } else {
                addAllInternal(copyArray(items));
            }
        }
    }

    public void addAll(T... items) {
        addAll(items, false);
    }

    public void addAll(Collection<T> items) {
        addAll(items.toArray((Object[]) ((Object[]) Array.newInstance(this.mTClass, items.size()))), true);
    }

    public void replaceAll(T[] items, boolean mayModifyInput) {
        throwIfInMutationOperation();
        if (mayModifyInput) {
            replaceAllInternal(items);
        } else {
            replaceAllInternal(copyArray(items));
        }
    }

    public void replaceAll(T... items) {
        replaceAll(items, false);
    }

    public void replaceAll(Collection<T> items) {
        replaceAll(items.toArray((Object[]) ((Object[]) Array.newInstance(this.mTClass, items.size()))), true);
    }

    private void addAllInternal(T[] newItems) {
        if (newItems.length >= 1) {
            int newSize = sortAndDedup(newItems);
            if (this.mSize == 0) {
                this.mData = newItems;
                this.mSize = newSize;
                this.mCallback.onInserted(0, newSize);
                return;
            }
            merge(newItems, newSize);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void replaceAllInternal(T[] r12) {
        /*
        r11 = this;
        r8 = 1;
        r7 = 0;
        r9 = r11.mCallback;
        r9 = r9 instanceof org.telegram.messenger.support.util.SortedList.BatchedCallback;
        if (r9 != 0) goto L_0x005d;
    L_0x0008:
        r0 = r8;
    L_0x0009:
        if (r0 == 0) goto L_0x000e;
    L_0x000b:
        r11.beginBatchedUpdates();
    L_0x000e:
        r11.mOldDataStart = r7;
        r9 = r11.mSize;
        r11.mOldDataSize = r9;
        r9 = r11.mData;
        r11.mOldData = r9;
        r11.mNewDataStart = r7;
        r4 = r11.sortAndDedup(r12);
        r7 = r11.mTClass;
        r7 = java.lang.reflect.Array.newInstance(r7, r4);
        r7 = (java.lang.Object[]) r7;
        r7 = (java.lang.Object[]) r7;
        r11.mData = r7;
    L_0x002a:
        r7 = r11.mNewDataStart;
        if (r7 < r4) goto L_0x0034;
    L_0x002e:
        r7 = r11.mOldDataStart;
        r9 = r11.mOldDataSize;
        if (r7 >= r9) goto L_0x0054;
    L_0x0034:
        r7 = r11.mOldDataStart;
        r9 = r11.mOldDataSize;
        if (r7 < r9) goto L_0x005f;
    L_0x003a:
        r1 = r11.mNewDataStart;
        r7 = r11.mNewDataStart;
        r2 = r4 - r7;
        r7 = r11.mData;
        java.lang.System.arraycopy(r12, r1, r7, r1, r2);
        r7 = r11.mNewDataStart;
        r7 = r7 + r2;
        r11.mNewDataStart = r7;
        r7 = r11.mSize;
        r7 = r7 + r2;
        r11.mSize = r7;
        r7 = r11.mCallback;
        r7.onInserted(r1, r2);
    L_0x0054:
        r7 = 0;
        r11.mOldData = r7;
        if (r0 == 0) goto L_0x005c;
    L_0x0059:
        r11.endBatchedUpdates();
    L_0x005c:
        return;
    L_0x005d:
        r0 = r7;
        goto L_0x0009;
    L_0x005f:
        r7 = r11.mNewDataStart;
        if (r7 < r4) goto L_0x0076;
    L_0x0063:
        r7 = r11.mOldDataSize;
        r8 = r11.mOldDataStart;
        r2 = r7 - r8;
        r7 = r11.mSize;
        r7 = r7 - r2;
        r11.mSize = r7;
        r7 = r11.mCallback;
        r8 = r11.mNewDataStart;
        r7.onRemoved(r8, r2);
        goto L_0x0054;
    L_0x0076:
        r7 = r11.mOldData;
        r9 = r11.mOldDataStart;
        r5 = r7[r9];
        r7 = r11.mNewDataStart;
        r3 = r12[r7];
        r7 = r11.mCallback;
        r6 = r7.compare(r5, r3);
        if (r6 >= 0) goto L_0x008c;
    L_0x0088:
        r11.replaceAllRemove();
        goto L_0x002a;
    L_0x008c:
        if (r6 <= 0) goto L_0x0092;
    L_0x008e:
        r11.replaceAllInsert(r3);
        goto L_0x002a;
    L_0x0092:
        r7 = r11.mCallback;
        r7 = r7.areItemsTheSame(r5, r3);
        if (r7 != 0) goto L_0x00a1;
    L_0x009a:
        r11.replaceAllRemove();
        r11.replaceAllInsert(r3);
        goto L_0x002a;
    L_0x00a1:
        r7 = r11.mData;
        r9 = r11.mNewDataStart;
        r7[r9] = r3;
        r7 = r11.mOldDataStart;
        r7 = r7 + 1;
        r11.mOldDataStart = r7;
        r7 = r11.mNewDataStart;
        r7 = r7 + 1;
        r11.mNewDataStart = r7;
        r7 = r11.mCallback;
        r7 = r7.areContentsTheSame(r5, r3);
        if (r7 != 0) goto L_0x002a;
    L_0x00bb:
        r7 = r11.mCallback;
        r9 = r11.mNewDataStart;
        r9 = r9 + -1;
        r10 = r11.mCallback;
        r10 = r10.getChangePayload(r5, r3);
        r7.onChanged(r9, r8, r10);
        goto L_0x002a;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.util.SortedList.replaceAllInternal(java.lang.Object[]):void");
    }

    private void replaceAllInsert(T newItem) {
        this.mData[this.mNewDataStart] = newItem;
        this.mNewDataStart++;
        this.mSize++;
        this.mCallback.onInserted(this.mNewDataStart - 1, 1);
    }

    private void replaceAllRemove() {
        this.mSize--;
        this.mOldDataStart++;
        this.mCallback.onRemoved(this.mNewDataStart, 1);
    }

    private int sortAndDedup(T[] items) {
        if (items.length == 0) {
            return 0;
        }
        Arrays.sort(items, this.mCallback);
        int rangeStart = 0;
        int rangeEnd = 1;
        for (int i = 1; i < items.length; i++) {
            T currentItem = items[i];
            if (this.mCallback.compare(items[rangeStart], currentItem) == 0) {
                int sameItemPos = findSameItem(currentItem, items, rangeStart, rangeEnd);
                if (sameItemPos != -1) {
                    items[sameItemPos] = currentItem;
                } else {
                    if (rangeEnd != i) {
                        items[rangeEnd] = currentItem;
                    }
                    rangeEnd++;
                }
            } else {
                if (rangeEnd != i) {
                    items[rangeEnd] = currentItem;
                }
                rangeStart = rangeEnd;
                rangeEnd++;
            }
        }
        return rangeEnd;
    }

    private int findSameItem(T item, T[] items, int from, int to) {
        for (int pos = from; pos < to; pos++) {
            if (this.mCallback.areItemsTheSame(items[pos], item)) {
                return pos;
            }
        }
        return -1;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void merge(T[] r12, int r13) {
        /*
        r11 = this;
        r8 = 1;
        r9 = 0;
        r7 = r11.mCallback;
        r7 = r7 instanceof org.telegram.messenger.support.util.SortedList.BatchedCallback;
        if (r7 != 0) goto L_0x005e;
    L_0x0008:
        r1 = r8;
    L_0x0009:
        if (r1 == 0) goto L_0x000e;
    L_0x000b:
        r11.beginBatchedUpdates();
    L_0x000e:
        r7 = r11.mData;
        r11.mOldData = r7;
        r11.mOldDataStart = r9;
        r7 = r11.mSize;
        r11.mOldDataSize = r7;
        r7 = r11.mSize;
        r7 = r7 + r13;
        r3 = r7 + 10;
        r7 = r11.mTClass;
        r7 = java.lang.reflect.Array.newInstance(r7, r3);
        r7 = (java.lang.Object[]) r7;
        r7 = (java.lang.Object[]) r7;
        r11.mData = r7;
        r11.mNewDataStart = r9;
        r4 = 0;
    L_0x002c:
        r7 = r11.mOldDataStart;
        r9 = r11.mOldDataSize;
        if (r7 < r9) goto L_0x0034;
    L_0x0032:
        if (r4 >= r13) goto L_0x0055;
    L_0x0034:
        r7 = r11.mOldDataStart;
        r9 = r11.mOldDataSize;
        if (r7 != r9) goto L_0x0060;
    L_0x003a:
        r2 = r13 - r4;
        r7 = r11.mData;
        r8 = r11.mNewDataStart;
        java.lang.System.arraycopy(r12, r4, r7, r8, r2);
        r7 = r11.mNewDataStart;
        r7 = r7 + r2;
        r11.mNewDataStart = r7;
        r7 = r11.mSize;
        r7 = r7 + r2;
        r11.mSize = r7;
        r7 = r11.mCallback;
        r8 = r11.mNewDataStart;
        r8 = r8 - r2;
        r7.onInserted(r8, r2);
    L_0x0055:
        r7 = 0;
        r11.mOldData = r7;
        if (r1 == 0) goto L_0x005d;
    L_0x005a:
        r11.endBatchedUpdates();
    L_0x005d:
        return;
    L_0x005e:
        r1 = r9;
        goto L_0x0009;
    L_0x0060:
        if (r4 != r13) goto L_0x0079;
    L_0x0062:
        r7 = r11.mOldDataSize;
        r8 = r11.mOldDataStart;
        r2 = r7 - r8;
        r7 = r11.mOldData;
        r8 = r11.mOldDataStart;
        r9 = r11.mData;
        r10 = r11.mNewDataStart;
        java.lang.System.arraycopy(r7, r8, r9, r10, r2);
        r7 = r11.mNewDataStart;
        r7 = r7 + r2;
        r11.mNewDataStart = r7;
        goto L_0x0055;
    L_0x0079:
        r7 = r11.mOldData;
        r9 = r11.mOldDataStart;
        r6 = r7[r9];
        r5 = r12[r4];
        r7 = r11.mCallback;
        r0 = r7.compare(r6, r5);
        if (r0 <= 0) goto L_0x00a5;
    L_0x0089:
        r7 = r11.mData;
        r9 = r11.mNewDataStart;
        r10 = r9 + 1;
        r11.mNewDataStart = r10;
        r7[r9] = r5;
        r7 = r11.mSize;
        r7 = r7 + 1;
        r11.mSize = r7;
        r4 = r4 + 1;
        r7 = r11.mCallback;
        r9 = r11.mNewDataStart;
        r9 = r9 + -1;
        r7.onInserted(r9, r8);
        goto L_0x002c;
    L_0x00a5:
        if (r0 != 0) goto L_0x00da;
    L_0x00a7:
        r7 = r11.mCallback;
        r7 = r7.areItemsTheSame(r6, r5);
        if (r7 == 0) goto L_0x00da;
    L_0x00af:
        r7 = r11.mData;
        r9 = r11.mNewDataStart;
        r10 = r9 + 1;
        r11.mNewDataStart = r10;
        r7[r9] = r5;
        r4 = r4 + 1;
        r7 = r11.mOldDataStart;
        r7 = r7 + 1;
        r11.mOldDataStart = r7;
        r7 = r11.mCallback;
        r7 = r7.areContentsTheSame(r6, r5);
        if (r7 != 0) goto L_0x002c;
    L_0x00c9:
        r7 = r11.mCallback;
        r9 = r11.mNewDataStart;
        r9 = r9 + -1;
        r10 = r11.mCallback;
        r10 = r10.getChangePayload(r6, r5);
        r7.onChanged(r9, r8, r10);
        goto L_0x002c;
    L_0x00da:
        r7 = r11.mData;
        r9 = r11.mNewDataStart;
        r10 = r9 + 1;
        r11.mNewDataStart = r10;
        r7[r9] = r6;
        r7 = r11.mOldDataStart;
        r7 = r7 + 1;
        r11.mOldDataStart = r7;
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.util.SortedList.merge(java.lang.Object[], int):void");
    }

    private void throwIfInMutationOperation() {
        if (this.mOldData != null) {
            throw new IllegalStateException("Data cannot be mutated in the middle of a batch update operation such as addAll or replaceAll.");
        }
    }

    public void beginBatchedUpdates() {
        throwIfInMutationOperation();
        if (!(this.mCallback instanceof BatchedCallback)) {
            if (this.mBatchedCallback == null) {
                this.mBatchedCallback = new BatchedCallback(this.mCallback);
            }
            this.mCallback = this.mBatchedCallback;
        }
    }

    public void endBatchedUpdates() {
        throwIfInMutationOperation();
        if (this.mCallback instanceof BatchedCallback) {
            ((BatchedCallback) this.mCallback).dispatchLastEvent();
        }
        if (this.mCallback == this.mBatchedCallback) {
            this.mCallback = this.mBatchedCallback.mWrappedCallback;
        }
    }

    private int add(T item, boolean notify) {
        int index = findIndexOf(item, this.mData, 0, this.mSize, 1);
        if (index == -1) {
            index = 0;
        } else if (index < this.mSize) {
            T existing = this.mData[index];
            if (this.mCallback.areItemsTheSame(existing, item)) {
                int i;
                if (this.mCallback.areContentsTheSame(existing, item)) {
                    this.mData[index] = item;
                    i = index;
                    return index;
                }
                this.mData[index] = item;
                this.mCallback.onChanged(index, 1, this.mCallback.getChangePayload(existing, item));
                i = index;
                return index;
            }
        }
        addToData(index, item);
        if (notify) {
            this.mCallback.onInserted(index, 1);
        }
        return index;
    }

    public boolean remove(T item) {
        throwIfInMutationOperation();
        return remove(item, true);
    }

    public T removeItemAt(int index) {
        throwIfInMutationOperation();
        T item = get(index);
        removeItemAtIndex(index, true);
        return item;
    }

    private boolean remove(T item, boolean notify) {
        int index = findIndexOf(item, this.mData, 0, this.mSize, 2);
        if (index == -1) {
            return false;
        }
        removeItemAtIndex(index, notify);
        return true;
    }

    private void removeItemAtIndex(int index, boolean notify) {
        System.arraycopy(this.mData, index + 1, this.mData, index, (this.mSize - index) - 1);
        this.mSize--;
        this.mData[this.mSize] = null;
        if (notify) {
            this.mCallback.onRemoved(index, 1);
        }
    }

    public void updateItemAt(int index, T item) {
        boolean contentsChanged;
        throwIfInMutationOperation();
        T existing = get(index);
        if (existing == item || !this.mCallback.areContentsTheSame(existing, item)) {
            contentsChanged = true;
        } else {
            contentsChanged = false;
        }
        if (existing == item || this.mCallback.compare(existing, item) != 0) {
            if (contentsChanged) {
                this.mCallback.onChanged(index, 1, this.mCallback.getChangePayload(existing, item));
            }
            removeItemAtIndex(index, false);
            int newIndex = add(item, false);
            if (index != newIndex) {
                this.mCallback.onMoved(index, newIndex);
                return;
            }
            return;
        }
        this.mData[index] = item;
        if (contentsChanged) {
            this.mCallback.onChanged(index, 1, this.mCallback.getChangePayload(existing, item));
        }
    }

    public void recalculatePositionOfItemAt(int index) {
        throwIfInMutationOperation();
        T item = get(index);
        removeItemAtIndex(index, false);
        int newIndex = add(item, false);
        if (index != newIndex) {
            this.mCallback.onMoved(index, newIndex);
        }
    }

    public T get(int index) throws IndexOutOfBoundsException {
        if (index >= this.mSize || index < 0) {
            throw new IndexOutOfBoundsException("Asked to get item at " + index + " but size is " + this.mSize);
        } else if (this.mOldData == null || index < this.mNewDataStart) {
            return this.mData[index];
        } else {
            return this.mOldData[(index - this.mNewDataStart) + this.mOldDataStart];
        }
    }

    public int indexOf(T item) {
        if (this.mOldData != null) {
            int index = findIndexOf(item, this.mData, 0, this.mNewDataStart, 4);
            if (index != -1) {
                return index;
            }
            index = findIndexOf(item, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
            return index != -1 ? (index - this.mOldDataStart) + this.mNewDataStart : -1;
        } else {
            return findIndexOf(item, this.mData, 0, this.mSize, 4);
        }
    }

    private int findIndexOf(T item, T[] mData, int left, int right, int reason) {
        while (left < right) {
            int middle = (left + right) / 2;
            T myItem = mData[middle];
            int cmp = this.mCallback.compare(myItem, item);
            if (cmp < 0) {
                left = middle + 1;
            } else if (cmp != 0) {
                right = middle;
            } else if (this.mCallback.areItemsTheSame(myItem, item)) {
                return middle;
            } else {
                int exact = linearEqualitySearch(item, middle, left, right);
                if (reason != 1) {
                    return exact;
                }
                if (exact != -1) {
                    return exact;
                }
                return middle;
            }
        }
        if (reason != 1) {
            left = -1;
        }
        return left;
    }

    private int linearEqualitySearch(T item, int middle, int left, int right) {
        int i;
        int next = middle - 1;
        while (next >= left) {
            T nextItem = this.mData[next];
            if (this.mCallback.compare(nextItem, item) != 0) {
                break;
            } else if (this.mCallback.areItemsTheSame(nextItem, item)) {
                i = next;
                return next;
            } else {
                next--;
            }
        }
        next = middle + 1;
        while (next < right) {
            nextItem = this.mData[next];
            if (this.mCallback.compare(nextItem, item) != 0) {
                break;
            } else if (this.mCallback.areItemsTheSame(nextItem, item)) {
                i = next;
                return next;
            } else {
                next++;
            }
        }
        i = next;
        return -1;
    }

    private void addToData(int index, T item) {
        if (index > this.mSize) {
            throw new IndexOutOfBoundsException("cannot add item to " + index + " because size is " + this.mSize);
        }
        if (this.mSize == this.mData.length) {
            Object[] newData = (Object[]) ((Object[]) Array.newInstance(this.mTClass, this.mData.length + 10));
            System.arraycopy(this.mData, 0, newData, 0, index);
            newData[index] = item;
            System.arraycopy(this.mData, index, newData, index + 1, this.mSize - index);
            this.mData = newData;
        } else {
            System.arraycopy(this.mData, index, this.mData, index + 1, this.mSize - index);
            this.mData[index] = item;
        }
        this.mSize++;
    }

    private T[] copyArray(T[] items) {
        Object[] copy = (Object[]) ((Object[]) Array.newInstance(this.mTClass, items.length));
        System.arraycopy(items, 0, copy, 0, items.length);
        return copy;
    }

    public void clear() {
        throwIfInMutationOperation();
        if (this.mSize != 0) {
            int prevSize = this.mSize;
            Arrays.fill(this.mData, 0, prevSize, null);
            this.mSize = 0;
            this.mCallback.onRemoved(0, prevSize);
        }
    }
}
