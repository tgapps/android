package org.telegram.messenger.support.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.telegram.messenger.support.widget.RecyclerView.Adapter;

public class DiffUtil {
    private static final Comparator<Snake> SNAKE_COMPARATOR = new Comparator<Snake>() {
        public int compare(Snake o1, Snake o2) {
            int cmpX = o1.x - o2.x;
            return cmpX == 0 ? o1.y - o2.y : cmpX;
        }
    };

    public static abstract class Callback {
        public abstract boolean areContentsTheSame(int i, int i2);

        public abstract boolean areItemsTheSame(int i, int i2);

        public abstract int getNewListSize();

        public abstract int getOldListSize();

        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return null;
        }
    }

    public static class DiffResult {
        private static final int FLAG_CHANGED = 2;
        private static final int FLAG_IGNORE = 16;
        private static final int FLAG_MASK = 31;
        private static final int FLAG_MOVED_CHANGED = 4;
        private static final int FLAG_MOVED_NOT_CHANGED = 8;
        private static final int FLAG_NOT_CHANGED = 1;
        private static final int FLAG_OFFSET = 5;
        private final Callback mCallback;
        private final boolean mDetectMoves;
        private final int[] mNewItemStatuses;
        private final int mNewListSize;
        private final int[] mOldItemStatuses;
        private final int mOldListSize;
        private final List<Snake> mSnakes;

        private void dispatchAdditions(java.util.List<org.telegram.messenger.support.util.DiffUtil.PostponedUpdate> r1, org.telegram.messenger.support.util.ListUpdateCallback r2, int r3, int r4, int r5) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.util.DiffUtil.DiffResult.dispatchAdditions(java.util.List, org.telegram.messenger.support.util.ListUpdateCallback, int, int, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = r7.mDetectMoves;
            if (r0 != 0) goto L_0x0008;
        L_0x0004:
            r9.onInserted(r10, r11);
            return;
        L_0x0008:
            r0 = r11 + -1;
            if (r0 < 0) goto L_0x0091;
        L_0x000c:
            r1 = r7.mNewItemStatuses;
            r2 = r12 + r0;
            r1 = r1[r2];
            r1 = r1 & 31;
            r2 = 1;
            if (r1 == 0) goto L_0x0073;
        L_0x0017:
            r3 = 4;
            if (r1 == r3) goto L_0x0054;
        L_0x001a:
            r4 = 8;
            if (r1 == r4) goto L_0x0054;
        L_0x001e:
            r2 = 16;
            if (r1 == r2) goto L_0x0048;
        L_0x0022:
            r2 = new java.lang.IllegalStateException;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "unknown flag for pos ";
            r3.append(r4);
            r4 = r12 + r0;
            r3.append(r4);
            r4 = " ";
            r3.append(r4);
            r4 = (long) r1;
            r4 = java.lang.Long.toBinaryString(r4);
            r3.append(r4);
            r3 = r3.toString();
            r2.<init>(r3);
            throw r2;
        L_0x0048:
            r2 = new org.telegram.messenger.support.util.DiffUtil$PostponedUpdate;
            r3 = r12 + r0;
            r4 = 0;
            r2.<init>(r3, r10, r4);
            r8.add(r2);
            goto L_0x008d;
        L_0x0054:
            r4 = r7.mNewItemStatuses;
            r5 = r12 + r0;
            r4 = r4[r5];
            r4 = r4 >> 5;
            r5 = removePostponedUpdate(r8, r4, r2);
            r6 = r5.currentPos;
            r9.onMoved(r6, r10);
            if (r1 != r3) goto L_0x008d;
        L_0x0067:
            r3 = r7.mCallback;
            r6 = r12 + r0;
            r3 = r3.getChangePayload(r4, r6);
            r9.onChanged(r10, r2, r3);
            goto L_0x008d;
        L_0x0073:
            r9.onInserted(r10, r2);
            r3 = r8.iterator();
            r4 = r3.hasNext();
            if (r4 == 0) goto L_0x008c;
        L_0x0080:
            r4 = r3.next();
            r4 = (org.telegram.messenger.support.util.DiffUtil.PostponedUpdate) r4;
            r5 = r4.currentPos;
            r5 = r5 + r2;
            r4.currentPos = r5;
            goto L_0x007a;
        L_0x008d:
            r0 = r0 + -1;
            goto L_0x000a;
        L_0x0091:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.util.DiffUtil.DiffResult.dispatchAdditions(java.util.List, org.telegram.messenger.support.util.ListUpdateCallback, int, int, int):void");
        }

        private void dispatchRemovals(java.util.List<org.telegram.messenger.support.util.DiffUtil.PostponedUpdate> r1, org.telegram.messenger.support.util.ListUpdateCallback r2, int r3, int r4, int r5) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.util.DiffUtil.DiffResult.dispatchRemovals(java.util.List, org.telegram.messenger.support.util.ListUpdateCallback, int, int, int):void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
            /*
            r0 = this;
            r0 = r8.mDetectMoves;
            if (r0 != 0) goto L_0x0008;
        L_0x0004:
            r10.onRemoved(r11, r12);
            return;
        L_0x0008:
            r0 = r12 + -1;
            if (r0 < 0) goto L_0x009b;
        L_0x000c:
            r1 = r8.mOldItemStatuses;
            r2 = r13 + r0;
            r1 = r1[r2];
            r1 = r1 & 31;
            r2 = 1;
            if (r1 == 0) goto L_0x007b;
        L_0x0017:
            r3 = 4;
            if (r1 == r3) goto L_0x0055;
        L_0x001a:
            r4 = 8;
            if (r1 == r4) goto L_0x0055;
        L_0x001e:
            r3 = 16;
            if (r1 == r3) goto L_0x0048;
        L_0x0022:
            r2 = new java.lang.IllegalStateException;
            r3 = new java.lang.StringBuilder;
            r3.<init>();
            r4 = "unknown flag for pos ";
            r3.append(r4);
            r4 = r13 + r0;
            r3.append(r4);
            r4 = " ";
            r3.append(r4);
            r4 = (long) r1;
            r4 = java.lang.Long.toBinaryString(r4);
            r3.append(r4);
            r3 = r3.toString();
            r2.<init>(r3);
            throw r2;
        L_0x0048:
            r3 = new org.telegram.messenger.support.util.DiffUtil$PostponedUpdate;
            r4 = r13 + r0;
            r5 = r11 + r0;
            r3.<init>(r4, r5, r2);
            r9.add(r3);
            goto L_0x0097;
        L_0x0055:
            r4 = r8.mOldItemStatuses;
            r5 = r13 + r0;
            r4 = r4[r5];
            r4 = r4 >> 5;
            r5 = 0;
            r5 = removePostponedUpdate(r9, r4, r5);
            r6 = r11 + r0;
            r7 = r5.currentPos;
            r7 = r7 - r2;
            r10.onMoved(r6, r7);
            if (r1 != r3) goto L_0x0097;
        L_0x006c:
            r3 = r5.currentPos;
            r3 = r3 - r2;
            r6 = r8.mCallback;
            r7 = r13 + r0;
            r6 = r6.getChangePayload(r7, r4);
            r10.onChanged(r3, r2, r6);
            goto L_0x0097;
        L_0x007b:
            r3 = r11 + r0;
            r10.onRemoved(r3, r2);
            r3 = r9.iterator();
            r4 = r3.hasNext();
            if (r4 == 0) goto L_0x0096;
        L_0x008a:
            r4 = r3.next();
            r4 = (org.telegram.messenger.support.util.DiffUtil.PostponedUpdate) r4;
            r5 = r4.currentPos;
            r5 = r5 - r2;
            r4.currentPos = r5;
            goto L_0x0084;
        L_0x0097:
            r0 = r0 + -1;
            goto L_0x000a;
        L_0x009b:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.util.DiffUtil.DiffResult.dispatchRemovals(java.util.List, org.telegram.messenger.support.util.ListUpdateCallback, int, int, int):void");
        }

        DiffResult(Callback callback, List<Snake> snakes, int[] oldItemStatuses, int[] newItemStatuses, boolean detectMoves) {
            this.mSnakes = snakes;
            this.mOldItemStatuses = oldItemStatuses;
            this.mNewItemStatuses = newItemStatuses;
            Arrays.fill(this.mOldItemStatuses, 0);
            Arrays.fill(this.mNewItemStatuses, 0);
            this.mCallback = callback;
            this.mOldListSize = callback.getOldListSize();
            this.mNewListSize = callback.getNewListSize();
            this.mDetectMoves = detectMoves;
            addRootSnake();
            findMatchingItems();
        }

        private void addRootSnake() {
            Snake firstSnake = this.mSnakes.isEmpty() ? null : (Snake) this.mSnakes.get(0);
            if (firstSnake == null || firstSnake.x != 0 || firstSnake.y != 0) {
                Snake root = new Snake();
                root.x = 0;
                root.y = 0;
                root.removal = false;
                root.size = 0;
                root.reverse = false;
                this.mSnakes.add(0, root);
            }
        }

        private void findMatchingItems() {
            int posOld = this.mOldListSize;
            int posNew = this.mNewListSize;
            for (int i = this.mSnakes.size() - 1; i >= 0; i--) {
                Snake snake = (Snake) this.mSnakes.get(i);
                int endX = snake.x + snake.size;
                int endY = snake.y + snake.size;
                if (this.mDetectMoves) {
                    while (posOld > endX) {
                        findAddition(posOld, posNew, i);
                        posOld--;
                    }
                    while (posNew > endY) {
                        findRemoval(posOld, posNew, i);
                        posNew--;
                    }
                }
                for (int j = 0; j < snake.size; j++) {
                    int oldItemPos = snake.x + j;
                    int newItemPos = snake.y + j;
                    int changeFlag = this.mCallback.areContentsTheSame(oldItemPos, newItemPos) ? 1 : 2;
                    this.mOldItemStatuses[oldItemPos] = (newItemPos << 5) | changeFlag;
                    this.mNewItemStatuses[newItemPos] = (oldItemPos << 5) | changeFlag;
                }
                posOld = snake.x;
                posNew = snake.y;
            }
        }

        private void findAddition(int x, int y, int snakeIndex) {
            if (this.mOldItemStatuses[x - 1] == 0) {
                findMatchingItem(x, y, snakeIndex, false);
            }
        }

        private void findRemoval(int x, int y, int snakeIndex) {
            if (this.mNewItemStatuses[y - 1] == 0) {
                findMatchingItem(x, y, snakeIndex, true);
            }
        }

        private boolean findMatchingItem(int x, int y, int snakeIndex, boolean removal) {
            int myItemPos;
            int curX;
            int curY;
            DiffResult diffResult = this;
            if (removal) {
                myItemPos = y - 1;
                curX = x;
                curY = y - 1;
            } else {
                myItemPos = x - 1;
                curX = x - 1;
                curY = y;
            }
            int curY2 = curY;
            curY = curX;
            for (curX = snakeIndex; curX >= 0; curX--) {
                Snake snake = (Snake) diffResult.mSnakes.get(curX);
                int endX = snake.x + snake.size;
                int endY = snake.y + snake.size;
                int changeFlag = 4;
                int pos;
                if (removal) {
                    for (pos = curY - 1; pos >= endX; pos--) {
                        if (diffResult.mCallback.areItemsTheSame(pos, myItemPos)) {
                            if (diffResult.mCallback.areContentsTheSame(pos, myItemPos)) {
                                changeFlag = 8;
                            }
                            diffResult.mNewItemStatuses[myItemPos] = (pos << 5) | 16;
                            diffResult.mOldItemStatuses[pos] = (myItemPos << 5) | changeFlag;
                            return true;
                        }
                    }
                    continue;
                } else {
                    for (pos = curY2 - 1; pos >= endY; pos--) {
                        if (diffResult.mCallback.areItemsTheSame(myItemPos, pos)) {
                            if (diffResult.mCallback.areContentsTheSame(myItemPos, pos)) {
                                changeFlag = 8;
                            }
                            diffResult.mOldItemStatuses[x - 1] = (pos << 5) | 16;
                            diffResult.mNewItemStatuses[pos] = ((x - 1) << 5) | changeFlag;
                            return true;
                        }
                    }
                    continue;
                }
                curY = snake.x;
                curY2 = snake.y;
            }
            return false;
        }

        public void dispatchUpdatesTo(Adapter adapter) {
            dispatchUpdatesTo(new AdapterListUpdateCallback(adapter));
        }

        public void dispatchUpdatesTo(ListUpdateCallback updateCallback) {
            BatchingListUpdateCallback batchingListUpdateCallback;
            DiffResult diffResult = this;
            ListUpdateCallback listUpdateCallback = updateCallback;
            if (listUpdateCallback instanceof BatchingListUpdateCallback) {
                batchingListUpdateCallback = (BatchingListUpdateCallback) listUpdateCallback;
            } else {
                batchingListUpdateCallback = new BatchingListUpdateCallback(listUpdateCallback);
                Object updateCallback2 = batchingListUpdateCallback;
            }
            ListUpdateCallback updateCallback3 = listUpdateCallback;
            BatchingListUpdateCallback batchingCallback = batchingListUpdateCallback;
            List<PostponedUpdate> postponedUpdates = new ArrayList();
            int posOld = diffResult.mOldListSize;
            int snakeIndex = diffResult.mSnakes.size() - 1;
            int posOld2 = posOld;
            int posNew = diffResult.mNewListSize;
            while (true) {
                int snakeIndex2 = snakeIndex;
                if (snakeIndex2 >= 0) {
                    int endY;
                    Snake snake = (Snake) diffResult.mSnakes.get(snakeIndex2);
                    int snakeSize = snake.size;
                    int endX = snake.x + snakeSize;
                    int endY2 = snake.y + snakeSize;
                    if (endX < posOld2) {
                        endY = endY2;
                        dispatchRemovals(postponedUpdates, batchingCallback, endX, posOld2 - endX, endX);
                    } else {
                        endY = endY2;
                    }
                    if (endY < posNew) {
                        posOld = snakeSize;
                        dispatchAdditions(postponedUpdates, batchingCallback, endX, posNew - endY, endY);
                    } else {
                        posOld = snakeSize;
                    }
                    snakeSize = posOld - 1;
                    while (true) {
                        int i = snakeSize;
                        if (i < 0) {
                            break;
                        }
                        if ((diffResult.mOldItemStatuses[snake.x + i] & FLAG_MASK) == 2) {
                            batchingCallback.onChanged(snake.x + i, 1, diffResult.mCallback.getChangePayload(snake.x + i, snake.y + i));
                        }
                        snakeSize = i - 1;
                    }
                    posOld2 = snake.x;
                    posNew = snake.y;
                    snakeIndex = snakeIndex2 - 1;
                    endY = 1;
                } else {
                    batchingCallback.dispatchLastEvent();
                    return;
                }
            }
        }

        private static PostponedUpdate removePostponedUpdate(List<PostponedUpdate> updates, int pos, boolean removal) {
            for (int i = updates.size() - 1; i >= 0; i--) {
                PostponedUpdate update = (PostponedUpdate) updates.get(i);
                if (update.posInOwnerList == pos && update.removal == removal) {
                    updates.remove(i);
                    for (int j = i; j < updates.size(); j++) {
                        PostponedUpdate postponedUpdate = (PostponedUpdate) updates.get(j);
                        postponedUpdate.currentPos += removal ? 1 : -1;
                    }
                    return update;
                }
            }
            return null;
        }

        List<Snake> getSnakes() {
            return this.mSnakes;
        }
    }

    public static abstract class ItemCallback<T> {
        public abstract boolean areContentsTheSame(T t, T t2);

        public abstract boolean areItemsTheSame(T t, T t2);

        public Object getChangePayload(T t, T t2) {
            return null;
        }
    }

    private static class PostponedUpdate {
        int currentPos;
        int posInOwnerList;
        boolean removal;

        public PostponedUpdate(int posInOwnerList, int currentPos, boolean removal) {
            this.posInOwnerList = posInOwnerList;
            this.currentPos = currentPos;
            this.removal = removal;
        }
    }

    static class Range {
        int newListEnd;
        int newListStart;
        int oldListEnd;
        int oldListStart;

        public Range(int oldListStart, int oldListEnd, int newListStart, int newListEnd) {
            this.oldListStart = oldListStart;
            this.oldListEnd = oldListEnd;
            this.newListStart = newListStart;
            this.newListEnd = newListEnd;
        }
    }

    static class Snake {
        boolean removal;
        boolean reverse;
        int size;
        int x;
        int y;

        Snake() {
        }
    }

    public static org.telegram.messenger.support.util.DiffUtil.DiffResult calculateDiff(org.telegram.messenger.support.util.DiffUtil.Callback r1, boolean r2) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.util.DiffUtil.calculateDiff(org.telegram.messenger.support.util.DiffUtil$Callback, boolean):org.telegram.messenger.support.util.DiffUtil$DiffResult
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r19.getOldListSize();
        r1 = r19.getNewListSize();
        r2 = new java.util.ArrayList;
        r2.<init>();
        r3 = new java.util.ArrayList;
        r3.<init>();
        r9 = r3;
        r3 = new org.telegram.messenger.support.util.DiffUtil$Range;
        r4 = 0;
        r3.<init>(r4, r0, r4, r1);
        r9.add(r3);
        r3 = r0 + r1;
        r4 = r0 - r1;
        r4 = java.lang.Math.abs(r4);
        r18 = r3 + r4;
        r3 = r18 * 2;
        r8 = new int[r3];
        r3 = r18 * 2;
        r7 = new int[r3];
        r3 = new java.util.ArrayList;
        r3.<init>();
        r6 = r3;
        r3 = r9.isEmpty();
        if (r3 != 0) goto L_0x00fd;
    L_0x003a:
        r3 = r9.size();
        r3 = r3 + -1;
        r3 = r9.remove(r3);
        r3 = (org.telegram.messenger.support.util.DiffUtil.Range) r3;
        r11 = r3.oldListStart;
        r12 = r3.oldListEnd;
        r13 = r3.newListStart;
        r14 = r3.newListEnd;
        r10 = r19;
        r15 = r8;
        r16 = r7;
        r17 = r18;
        r4 = diffPartial(r10, r11, r12, r13, r14, r15, r16, r17);
        if (r4 == 0) goto L_0x00f6;
    L_0x005b:
        r5 = r4.size;
        if (r5 <= 0) goto L_0x0062;
    L_0x005f:
        r2.add(r4);
    L_0x0062:
        r5 = r4.x;
        r10 = r3.oldListStart;
        r5 = r5 + r10;
        r4.x = r5;
        r5 = r4.y;
        r10 = r3.newListStart;
        r5 = r5 + r10;
        r4.y = r5;
        r5 = r6.isEmpty();
        if (r5 == 0) goto L_0x007c;
    L_0x0076:
        r5 = new org.telegram.messenger.support.util.DiffUtil$Range;
        r5.<init>();
        goto L_0x0088;
    L_0x007c:
        r5 = r6.size();
        r5 = r5 + -1;
        r5 = r6.remove(r5);
        r5 = (org.telegram.messenger.support.util.DiffUtil.Range) r5;
    L_0x0088:
        r10 = r3.oldListStart;
        r5.oldListStart = r10;
        r10 = r3.newListStart;
        r5.newListStart = r10;
        r10 = r4.reverse;
        if (r10 == 0) goto L_0x009d;
    L_0x0094:
        r10 = r4.x;
        r5.oldListEnd = r10;
        r10 = r4.y;
        r5.newListEnd = r10;
        goto L_0x00b6;
    L_0x009d:
        r10 = r4.removal;
        if (r10 == 0) goto L_0x00ac;
    L_0x00a1:
        r10 = r4.x;
        r10 = r10 + -1;
        r5.oldListEnd = r10;
        r10 = r4.y;
        r5.newListEnd = r10;
        goto L_0x00b6;
    L_0x00ac:
        r10 = r4.x;
        r5.oldListEnd = r10;
        r10 = r4.y;
        r10 = r10 + -1;
        r5.newListEnd = r10;
    L_0x00b6:
        r9.add(r5);
        r10 = r3;
        r11 = r4.reverse;
        if (r11 == 0) goto L_0x00e4;
    L_0x00be:
        r11 = r4.removal;
        if (r11 == 0) goto L_0x00d3;
    L_0x00c2:
        r11 = r4.x;
        r12 = r4.size;
        r11 = r11 + r12;
        r11 = r11 + 1;
        r10.oldListStart = r11;
        r11 = r4.y;
        r12 = r4.size;
        r11 = r11 + r12;
        r10.newListStart = r11;
        goto L_0x00f2;
    L_0x00d3:
        r11 = r4.x;
        r12 = r4.size;
        r11 = r11 + r12;
        r10.oldListStart = r11;
        r11 = r4.y;
        r12 = r4.size;
        r11 = r11 + r12;
        r11 = r11 + 1;
        r10.newListStart = r11;
        goto L_0x00f2;
    L_0x00e4:
        r11 = r4.x;
        r12 = r4.size;
        r11 = r11 + r12;
        r10.oldListStart = r11;
        r11 = r4.y;
        r12 = r4.size;
        r11 = r11 + r12;
        r10.newListStart = r11;
    L_0x00f2:
        r9.add(r10);
        goto L_0x00f9;
    L_0x00f6:
        r6.add(r3);
        r3 = r6;
        goto L_0x0033;
    L_0x00fd:
        r3 = SNAKE_COMPARATOR;
        java.util.Collections.sort(r2, r3);
        r10 = new org.telegram.messenger.support.util.DiffUtil$DiffResult;
        r3 = r10;
        r4 = r19;
        r5 = r2;
        r11 = r6;
        r6 = r8;
        r12 = r7;
        r13 = r8;
        r8 = r20;
        r3.<init>(r4, r5, r6, r7, r8);
        return r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.util.DiffUtil.calculateDiff(org.telegram.messenger.support.util.DiffUtil$Callback, boolean):org.telegram.messenger.support.util.DiffUtil$DiffResult");
    }

    private static org.telegram.messenger.support.util.DiffUtil.Snake diffPartial(org.telegram.messenger.support.util.DiffUtil.Callback r1, int r2, int r3, int r4, int r5, int[] r6, int[] r7, int r8) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.messenger.support.util.DiffUtil.diffPartial(org.telegram.messenger.support.util.DiffUtil$Callback, int, int, int, int, int[], int[], int):org.telegram.messenger.support.util.DiffUtil$Snake
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = r21;
        r4 = r26;
        r5 = r27;
        r6 = r23 - r22;
        r7 = r25 - r24;
        r8 = r23 - r22;
        r9 = 1;
        if (r8 < r9) goto L_0x017c;
    L_0x000f:
        r8 = r25 - r24;
        if (r8 >= r9) goto L_0x0019;
    L_0x0013:
        r18 = r6;
        r19 = r7;
        goto L_0x0180;
    L_0x0019:
        r8 = r6 - r7;
        r10 = r6 + r7;
        r10 = r10 + r9;
        r10 = r10 / 2;
        r11 = r28 - r10;
        r11 = r11 - r9;
        r12 = r28 + r10;
        r12 = r12 + r9;
        r13 = 0;
        java.util.Arrays.fill(r4, r11, r12, r13);
        r11 = r28 - r10;
        r11 = r11 - r9;
        r11 = r11 + r8;
        r12 = r28 + r10;
        r12 = r12 + r9;
        r12 = r12 + r8;
        java.util.Arrays.fill(r5, r11, r12, r6);
        r11 = r8 % 2;
        if (r11 == 0) goto L_0x003b;
    L_0x0039:
        r11 = r9;
        goto L_0x003c;
    L_0x003b:
        r11 = r13;
    L_0x003c:
        r12 = r13;
        if (r12 > r10) goto L_0x0170;
    L_0x003f:
        r13 = -r12;
        if (r13 > r12) goto L_0x00d9;
    L_0x0042:
        r9 = -r12;
        if (r13 == r9) goto L_0x005f;
    L_0x0045:
        if (r13 == r12) goto L_0x0056;
    L_0x0047:
        r9 = r28 + r13;
        r15 = 1;
        r9 = r9 - r15;
        r9 = r4[r9];
        r16 = r28 + r13;
        r16 = r16 + 1;
        r2 = r4[r16];
        if (r9 >= r2) goto L_0x0057;
    L_0x0055:
        goto L_0x0060;
    L_0x0056:
        r15 = 1;
    L_0x0057:
        r2 = r28 + r13;
        r2 = r2 - r15;
        r2 = r4[r2];
        r2 = r2 + r15;
        r9 = r15;
        goto L_0x0066;
    L_0x005f:
        r15 = 1;
    L_0x0060:
        r2 = r28 + r13;
        r2 = r2 + r15;
        r2 = r4[r2];
        r9 = 0;
        r16 = r2 - r13;
        r17 = r16;
        if (r2 >= r6) goto L_0x008d;
        r3 = r17;
        if (r3 >= r7) goto L_0x0088;
        r18 = r6;
        r6 = r22 + r2;
        r19 = r7;
        r7 = r24 + r3;
        r6 = r0.areItemsTheSame(r6, r7);
        if (r6 == 0) goto L_0x0093;
        r2 = r2 + 1;
        r16 = r3 + 1;
        r6 = r18;
        r7 = r19;
        goto L_0x0069;
        r18 = r6;
        r19 = r7;
        goto L_0x0093;
        r18 = r6;
        r19 = r7;
        r3 = r17;
        r6 = r28 + r13;
        r4[r6] = r2;
        if (r11 == 0) goto L_0x00cf;
        r6 = r8 - r12;
        r7 = 1;
        r6 = r6 + r7;
        if (r13 < r6) goto L_0x00cf;
        r6 = r8 + r12;
        r6 = r6 - r7;
        if (r13 > r6) goto L_0x00cf;
        r6 = r28 + r13;
        r6 = r4[r6];
        r7 = r28 + r13;
        r7 = r5[r7];
        if (r6 < r7) goto L_0x00cf;
        r6 = new org.telegram.messenger.support.util.DiffUtil$Snake;
        r6.<init>();
        r7 = r28 + r13;
        r7 = r5[r7];
        r6.x = r7;
        r7 = r6.x;
        r7 = r7 - r13;
        r6.y = r7;
        r7 = r28 + r13;
        r7 = r4[r7];
        r15 = r28 + r13;
        r15 = r5[r15];
        r7 = r7 - r15;
        r6.size = r7;
        r6.removal = r9;
        r7 = 0;
        r6.reverse = r7;
        return r6;
        r7 = 0;
        r13 = r13 + 2;
        r6 = r18;
        r7 = r19;
        r9 = 1;
        goto L_0x0040;
    L_0x00d9:
        r18 = r6;
        r19 = r7;
        r7 = 0;
        r2 = -r12;
        if (r2 > r12) goto L_0x0165;
        r3 = r2 + r8;
        r6 = r12 + r8;
        if (r3 == r6) goto L_0x0102;
        r6 = -r12;
        r6 = r6 + r8;
        if (r3 == r6) goto L_0x00f9;
        r6 = r28 + r3;
        r15 = 1;
        r6 = r6 - r15;
        r6 = r5[r6];
        r9 = r28 + r3;
        r9 = r9 + r15;
        r9 = r5[r9];
        if (r6 >= r9) goto L_0x00fa;
        goto L_0x0103;
        r15 = 1;
        r6 = r28 + r3;
        r6 = r6 + r15;
        r6 = r5[r6];
        r6 = r6 - r15;
        r9 = r15;
        goto L_0x0109;
        r15 = 1;
        r6 = r28 + r3;
        r6 = r6 - r15;
        r6 = r5[r6];
        r9 = 0;
        r13 = r6 - r3;
        if (r6 <= 0) goto L_0x0125;
        if (r13 <= 0) goto L_0x0125;
        r14 = r22 + r6;
        r15 = 1;
        r7 = r14 + -1;
        r14 = r24 + r13;
        r1 = r14 + -1;
        r1 = r0.areItemsTheSame(r7, r1);
        if (r1 == 0) goto L_0x0125;
        r6 = r6 + -1;
        r13 = r13 + -1;
        r7 = 0;
        goto L_0x010c;
        r1 = r28 + r3;
        r5[r1] = r6;
        if (r11 != 0) goto L_0x015f;
        r1 = r2 + r8;
        r7 = -r12;
        if (r1 < r7) goto L_0x015f;
        r1 = r2 + r8;
        if (r1 > r12) goto L_0x015f;
        r1 = r28 + r3;
        r1 = r4[r1];
        r7 = r28 + r3;
        r7 = r5[r7];
        if (r1 < r7) goto L_0x015f;
        r1 = new org.telegram.messenger.support.util.DiffUtil$Snake;
        r1.<init>();
        r7 = r28 + r3;
        r7 = r5[r7];
        r1.x = r7;
        r7 = r1.x;
        r7 = r7 - r3;
        r1.y = r7;
        r7 = r28 + r3;
        r7 = r4[r7];
        r14 = r28 + r3;
        r14 = r5[r14];
        r7 = r7 - r14;
        r1.size = r7;
        r1.removal = r9;
        r7 = 1;
        r1.reverse = r7;
        return r1;
        r7 = 1;
        r2 = r2 + 2;
        r7 = 0;
        goto L_0x00df;
        r7 = 1;
        r12 = r12 + 1;
        r9 = r7;
        r6 = r18;
        r7 = r19;
        r13 = 0;
        goto L_0x003d;
    L_0x0170:
        r18 = r6;
        r19 = r7;
        r1 = new java.lang.IllegalStateException;
        r2 = "DiffUtil hit an unexpected case while trying to calculate the optimal path. Please make sure your data is not changing during the diff calculation.";
        r1.<init>(r2);
        throw r1;
    L_0x017c:
        r18 = r6;
        r19 = r7;
    L_0x0180:
        r1 = 0;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.messenger.support.util.DiffUtil.diffPartial(org.telegram.messenger.support.util.DiffUtil$Callback, int, int, int, int, int[], int[], int):org.telegram.messenger.support.util.DiffUtil$Snake");
    }

    private DiffUtil() {
    }

    public static DiffResult calculateDiff(Callback cb) {
        return calculateDiff(cb, true);
    }
}
