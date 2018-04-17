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
        addAll(items.toArray((Object[]) Array.newInstance(this.mTClass, items.size())), true);
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
        replaceAll(items.toArray((Object[]) Array.newInstance(this.mTClass, items.size())), true);
    }

    private void addAllInternal(T[] newItems) {
        if (newItems.length >= 1) {
            int newSize = sortAndDedup(newItems);
            if (this.mSize == 0) {
                this.mData = newItems;
                this.mSize = newSize;
                this.mCallback.onInserted(0, newSize);
            } else {
                merge(newItems, newSize);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void replaceAllInternal(T[] r10) {
        /*
        r9 = this;
        r0 = r9.mCallback;
        r0 = r0 instanceof org.telegram.messenger.support.util.SortedList.BatchedCallback;
        r1 = 0;
        r2 = 1;
        if (r0 != 0) goto L_0x000a;
    L_0x0008:
        r0 = r2;
        goto L_0x000b;
    L_0x000a:
        r0 = r1;
    L_0x000b:
        if (r0 == 0) goto L_0x0010;
    L_0x000d:
        r9.beginBatchedUpdates();
    L_0x0010:
        r9.mOldDataStart = r1;
        r3 = r9.mSize;
        r9.mOldDataSize = r3;
        r3 = r9.mData;
        r9.mOldData = r3;
        r9.mNewDataStart = r1;
        r1 = r9.sortAndDedup(r10);
        r3 = r9.mTClass;
        r3 = java.lang.reflect.Array.newInstance(r3, r1);
        r3 = (java.lang.Object[]) r3;
        r9.mData = r3;
    L_0x002a:
        r3 = r9.mNewDataStart;
        if (r3 < r1) goto L_0x0034;
    L_0x002e:
        r3 = r9.mOldDataStart;
        r4 = r9.mOldDataSize;
        if (r3 >= r4) goto L_0x006b;
    L_0x0034:
        r3 = r9.mOldDataStart;
        r4 = r9.mOldDataSize;
        if (r3 < r4) goto L_0x0055;
    L_0x003a:
        r2 = r9.mNewDataStart;
        r3 = r9.mNewDataStart;
        r3 = r1 - r3;
        r4 = r9.mData;
        java.lang.System.arraycopy(r10, r2, r4, r2, r3);
        r4 = r9.mNewDataStart;
        r4 = r4 + r3;
        r9.mNewDataStart = r4;
        r4 = r9.mSize;
        r4 = r4 + r3;
        r9.mSize = r4;
        r4 = r9.mCallback;
        r4.onInserted(r2, r3);
        goto L_0x006b;
    L_0x0055:
        r3 = r9.mNewDataStart;
        if (r3 < r1) goto L_0x0074;
    L_0x0059:
        r2 = r9.mOldDataSize;
        r3 = r9.mOldDataStart;
        r2 = r2 - r3;
        r3 = r9.mSize;
        r3 = r3 - r2;
        r9.mSize = r3;
        r3 = r9.mCallback;
        r4 = r9.mNewDataStart;
        r3.onRemoved(r4, r2);
    L_0x006b:
        r2 = 0;
        r9.mOldData = r2;
        if (r0 == 0) goto L_0x0073;
    L_0x0070:
        r9.endBatchedUpdates();
    L_0x0073:
        return;
    L_0x0074:
        r3 = r9.mOldData;
        r4 = r9.mOldDataStart;
        r3 = r3[r4];
        r4 = r9.mNewDataStart;
        r4 = r10[r4];
        r5 = r9.mCallback;
        r5 = r5.compare(r3, r4);
        if (r5 >= 0) goto L_0x008a;
    L_0x0086:
        r9.replaceAllRemove();
        goto L_0x00c5;
    L_0x008a:
        if (r5 <= 0) goto L_0x0090;
    L_0x008c:
        r9.replaceAllInsert(r4);
        goto L_0x00c5;
    L_0x0090:
        r6 = r9.mCallback;
        r6 = r6.areItemsTheSame(r3, r4);
        if (r6 != 0) goto L_0x009f;
    L_0x0098:
        r9.replaceAllRemove();
        r9.replaceAllInsert(r4);
        goto L_0x00c5;
    L_0x009f:
        r6 = r9.mData;
        r7 = r9.mNewDataStart;
        r6[r7] = r4;
        r6 = r9.mOldDataStart;
        r6 = r6 + r2;
        r9.mOldDataStart = r6;
        r6 = r9.mNewDataStart;
        r6 = r6 + r2;
        r9.mNewDataStart = r6;
        r6 = r9.mCallback;
        r6 = r6.areContentsTheSame(r3, r4);
        if (r6 != 0) goto L_0x00c5;
    L_0x00b7:
        r6 = r9.mCallback;
        r7 = r9.mNewDataStart;
        r7 = r7 - r2;
        r8 = r9.mCallback;
        r8 = r8.getChangePayload(r3, r4);
        r6.onChanged(r7, r2, r8);
    L_0x00c5:
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
    private void merge(T[] r11, int r12) {
        /*
        r10 = this;
        r0 = r10.mCallback;
        r0 = r0 instanceof org.telegram.messenger.support.util.SortedList.BatchedCallback;
        r1 = 0;
        r2 = 1;
        if (r0 != 0) goto L_0x000a;
    L_0x0008:
        r0 = r2;
        goto L_0x000b;
    L_0x000a:
        r0 = r1;
    L_0x000b:
        if (r0 == 0) goto L_0x0010;
    L_0x000d:
        r10.beginBatchedUpdates();
    L_0x0010:
        r3 = r10.mData;
        r10.mOldData = r3;
        r10.mOldDataStart = r1;
        r3 = r10.mSize;
        r10.mOldDataSize = r3;
        r3 = r10.mSize;
        r3 = r3 + r12;
        r3 = r3 + 10;
        r4 = r10.mTClass;
        r4 = java.lang.reflect.Array.newInstance(r4, r3);
        r4 = (java.lang.Object[]) r4;
        r10.mData = r4;
        r10.mNewDataStart = r1;
    L_0x002c:
        r4 = r10.mOldDataStart;
        r5 = r10.mOldDataSize;
        if (r4 < r5) goto L_0x0034;
    L_0x0032:
        if (r1 >= r12) goto L_0x006e;
    L_0x0034:
        r4 = r10.mOldDataStart;
        r5 = r10.mOldDataSize;
        if (r4 != r5) goto L_0x0056;
    L_0x003a:
        r2 = r12 - r1;
        r4 = r10.mData;
        r5 = r10.mNewDataStart;
        java.lang.System.arraycopy(r11, r1, r4, r5, r2);
        r4 = r10.mNewDataStart;
        r4 = r4 + r2;
        r10.mNewDataStart = r4;
        r4 = r10.mSize;
        r4 = r4 + r2;
        r10.mSize = r4;
        r4 = r10.mCallback;
        r5 = r10.mNewDataStart;
        r5 = r5 - r2;
        r4.onInserted(r5, r2);
        goto L_0x006e;
    L_0x0056:
        if (r1 != r12) goto L_0x0077;
    L_0x0058:
        r2 = r10.mOldDataSize;
        r4 = r10.mOldDataStart;
        r2 = r2 - r4;
        r4 = r10.mOldData;
        r5 = r10.mOldDataStart;
        r6 = r10.mData;
        r7 = r10.mNewDataStart;
        java.lang.System.arraycopy(r4, r5, r6, r7, r2);
        r4 = r10.mNewDataStart;
        r4 = r4 + r2;
        r10.mNewDataStart = r4;
    L_0x006e:
        r2 = 0;
        r10.mOldData = r2;
        if (r0 == 0) goto L_0x0076;
    L_0x0073:
        r10.endBatchedUpdates();
    L_0x0076:
        return;
    L_0x0077:
        r4 = r10.mOldData;
        r5 = r10.mOldDataStart;
        r4 = r4[r5];
        r5 = r11[r1];
        r6 = r10.mCallback;
        r6 = r6.compare(r4, r5);
        if (r6 <= 0) goto L_0x00a1;
    L_0x0087:
        r7 = r10.mData;
        r8 = r10.mNewDataStart;
        r9 = r8 + 1;
        r10.mNewDataStart = r9;
        r7[r8] = r5;
        r7 = r10.mSize;
        r7 = r7 + r2;
        r10.mSize = r7;
        r1 = r1 + 1;
        r7 = r10.mCallback;
        r8 = r10.mNewDataStart;
        r8 = r8 - r2;
        r7.onInserted(r8, r2);
        goto L_0x00e2;
    L_0x00a1:
        if (r6 != 0) goto L_0x00d3;
    L_0x00a3:
        r7 = r10.mCallback;
        r7 = r7.areItemsTheSame(r4, r5);
        if (r7 == 0) goto L_0x00d3;
    L_0x00ab:
        r7 = r10.mData;
        r8 = r10.mNewDataStart;
        r9 = r8 + 1;
        r10.mNewDataStart = r9;
        r7[r8] = r5;
        r1 = r1 + 1;
        r7 = r10.mOldDataStart;
        r7 = r7 + r2;
        r10.mOldDataStart = r7;
        r7 = r10.mCallback;
        r7 = r7.areContentsTheSame(r4, r5);
        if (r7 != 0) goto L_0x00e2;
    L_0x00c4:
        r7 = r10.mCallback;
        r8 = r10.mNewDataStart;
        r8 = r8 - r2;
        r9 = r10.mCallback;
        r9 = r9.getChangePayload(r4, r5);
        r7.onChanged(r8, r2, r9);
        goto L_0x00e2;
    L_0x00d3:
        r7 = r10.mData;
        r8 = r10.mNewDataStart;
        r9 = r8 + 1;
        r10.mNewDataStart = r9;
        r7[r8] = r4;
        r7 = r10.mOldDataStart;
        r7 = r7 + r2;
        r10.mOldDataStart = r7;
    L_0x00e2:
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
                if (this.mCallback.areContentsTheSame(existing, item)) {
                    this.mData[index] = item;
                    return index;
                }
                this.mData[index] = item;
                this.mCallback.onChanged(index, 1, this.mCallback.getChangePayload(existing, item));
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
        int newIndex;
        throwIfInMutationOperation();
        T existing = get(index);
        if (existing != item) {
            if (this.mCallback.areContentsTheSame(existing, item)) {
                contentsChanged = false;
                if (existing == item && this.mCallback.compare(existing, item) == 0) {
                    this.mData[index] = item;
                    if (contentsChanged) {
                        this.mCallback.onChanged(index, 1, this.mCallback.getChangePayload(existing, item));
                    }
                    return;
                }
                if (contentsChanged) {
                    this.mCallback.onChanged(index, 1, this.mCallback.getChangePayload(existing, item));
                }
                removeItemAtIndex(index, false);
                newIndex = add(item, false);
                if (index != newIndex) {
                    this.mCallback.onMoved(index, newIndex);
                }
            }
        }
        contentsChanged = true;
        if (existing == item) {
        }
        if (contentsChanged) {
            this.mCallback.onChanged(index, 1, this.mCallback.getChangePayload(existing, item));
        }
        removeItemAtIndex(index, false);
        newIndex = add(item, false);
        if (index != newIndex) {
            this.mCallback.onMoved(index, newIndex);
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
        if (index < this.mSize) {
            if (index >= 0) {
                if (this.mOldData == null || index < this.mNewDataStart) {
                    return this.mData[index];
                }
                return this.mOldData[(index - this.mNewDataStart) + this.mOldDataStart];
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Asked to get item at ");
        stringBuilder.append(index);
        stringBuilder.append(" but size is ");
        stringBuilder.append(this.mSize);
        throw new IndexOutOfBoundsException(stringBuilder.toString());
    }

    public int indexOf(T item) {
        if (this.mOldData != null) {
            int index = findIndexOf(item, this.mData, 0, this.mNewDataStart, 4);
            if (index != -1) {
                return index;
            }
            index = findIndexOf(item, this.mOldData, this.mOldDataStart, this.mOldDataSize, 4);
            if (index != -1) {
                return (index - this.mOldDataStart) + this.mNewDataStart;
            }
            return -1;
        }
        return findIndexOf(item, this.mData, 0, this.mSize, 4);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int findIndexOf(T r7, T[] r8, int r9, int r10, int r11) {
        /*
        r6 = this;
    L_0x0000:
        r0 = -1;
        r1 = 1;
        if (r9 >= r10) goto L_0x002f;
    L_0x0004:
        r2 = r9 + r10;
        r2 = r2 / 2;
        r3 = r8[r2];
        r4 = r6.mCallback;
        r4 = r4.compare(r3, r7);
        if (r4 >= 0) goto L_0x0015;
    L_0x0012:
        r9 = r2 + 1;
        goto L_0x002e;
    L_0x0015:
        if (r4 != 0) goto L_0x002d;
    L_0x0017:
        r5 = r6.mCallback;
        r5 = r5.areItemsTheSame(r3, r7);
        if (r5 == 0) goto L_0x0020;
    L_0x001f:
        return r2;
    L_0x0020:
        r5 = r6.linearEqualitySearch(r7, r2, r9, r10);
        if (r11 != r1) goto L_0x002c;
    L_0x0026:
        if (r5 != r0) goto L_0x002a;
    L_0x0028:
        r0 = r2;
        goto L_0x002b;
    L_0x002a:
        r0 = r5;
    L_0x002b:
        return r0;
    L_0x002c:
        return r5;
    L_0x002d:
        r10 = r2;
    L_0x002e:
        goto L_0x0000;
    L_0x002f:
        if (r11 != r1) goto L_0x0033;
    L_0x0031:
        r0 = r9;
    L_0x0033:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.util.SortedList.findIndexOf(java.lang.Object, java.lang.Object[], int, int, int):int");
    }

    private int linearEqualitySearch(T item, int middle, int left, int right) {
        int next = middle - 1;
        while (next >= left) {
            T nextItem = this.mData[next];
            if (this.mCallback.compare(nextItem, item) != 0) {
                break;
            } else if (this.mCallback.areItemsTheSame(nextItem, item)) {
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
                return next;
            } else {
                next++;
            }
        }
        return -1;
    }

    private void addToData(int index, T item) {
        if (index > this.mSize) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("cannot add item to ");
            stringBuilder.append(index);
            stringBuilder.append(" because size is ");
            stringBuilder.append(this.mSize);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }
        if (this.mSize == this.mData.length) {
            Object[] newData = (Object[]) Array.newInstance(this.mTClass, this.mData.length + 10);
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
        Object[] copy = (Object[]) Array.newInstance(this.mTClass, items.length);
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
