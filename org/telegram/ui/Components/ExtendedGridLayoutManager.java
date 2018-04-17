package org.telegram.ui.Components;

import android.content.Context;
import android.util.SparseIntArray;
import java.util.ArrayList;
import org.telegram.messenger.support.widget.GridLayoutManager;
import org.telegram.tgnet.ConnectionsManager;

public class ExtendedGridLayoutManager extends GridLayoutManager {
    private int calculatedWidth;
    private SparseIntArray itemSpans = new SparseIntArray();
    private SparseIntArray itemsToRow = new SparseIntArray();
    private ArrayList<ArrayList<Integer>> rows;

    private void prepareLayout(float r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.ui.Components.ExtendedGridLayoutManager.prepareLayout(float):void
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
        r0 = r21;
        r2 = r0.itemSpans;
        r2.clear();
        r2 = r0.itemsToRow;
        r2.clear();
        r2 = 1120403456; // 0x42c80000 float:100.0 double:5.53552857E-315;
        r3 = org.telegram.messenger.AndroidUtilities.dp(r2);
        r4 = 0;
        r5 = r21.getFlowItemCount();
        r6 = new int[r5];
        r7 = 0;
        r8 = r4;
        r4 = r7;
    L_0x001c:
        if (r4 >= r5) goto L_0x0039;
    L_0x001e:
        r9 = r0.sizeForItem(r4);
        r10 = r9.width;
        r11 = r9.height;
        r10 = r10 / r11;
        r11 = (float) r3;
        r10 = r10 * r11;
        r8 = r8 + r10;
        r10 = r9.width;
        r11 = r9.height;
        r10 = r10 / r11;
        r10 = r10 * r2;
        r10 = java.lang.Math.round(r10);
        r6[r4] = r10;
        r4 = r4 + 1;
        goto L_0x001c;
    L_0x0039:
        r2 = r8 / r22;
        r2 = java.lang.Math.round(r2);
        r4 = 1;
        r2 = java.lang.Math.max(r2, r4);
        r9 = r0.getLinearPartitionForSequence(r6, r2);
        r0.rows = r9;
        r9 = 0;
        r10 = 0;
        r11 = r0.rows;
        r11 = r11.size();
        if (r7 >= r11) goto L_0x010c;
    L_0x0055:
        r11 = r0.rows;
        r11 = r11.get(r7);
        r11 = (java.util.ArrayList) r11;
        r12 = 0;
        r13 = r9;
        r14 = r11.size();
        r14 = r14 + r9;
    L_0x0064:
        if (r13 >= r14) goto L_0x0078;
    L_0x0066:
        r15 = r0.sizeForItem(r13);
        r4 = r15.width;
        r17 = r2;
        r2 = r15.height;
        r4 = r4 / r2;
        r12 = r12 + r4;
        r13 = r13 + 1;
        r2 = r17;
        r4 = 1;
        goto L_0x0064;
    L_0x0078:
        r17 = r2;
        r2 = r22;
        r4 = r0.rows;
        r4 = r4.size();
        r13 = 3;
        r14 = 1;
        if (r4 != r14) goto L_0x00b3;
    L_0x0086:
        r4 = r0.rows;
        r4 = r4.size();
        r4 = r4 - r14;
        if (r7 != r4) goto L_0x00b3;
    L_0x008f:
        r4 = r11.size();
        r15 = 2;
        r16 = 1077936128; // 0x40400000 float:3.0 double:5.325712093E-315;
        if (r4 >= r15) goto L_0x00a1;
    L_0x0098:
        r4 = r22 / r16;
        r14 = (double) r4;
        r14 = java.lang.Math.floor(r14);
        r2 = (float) r14;
        goto L_0x00b3;
    L_0x00a1:
        r4 = r11.size();
        if (r4 >= r13) goto L_0x00b3;
    L_0x00a7:
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = r4 * r22;
        r4 = r4 / r16;
        r14 = (double) r4;
        r14 = java.lang.Math.floor(r14);
        r2 = (float) r14;
    L_0x00b3:
        r4 = r21.getSpanCount();
        r14 = r9;
        r15 = r11.size();
        r15 = r15 + r9;
        if (r14 >= r15) goto L_0x00fc;
    L_0x00bf:
        r13 = r0.sizeForItem(r14);
        r16 = r2 / r12;
        r18 = r2;
        r2 = r13.width;
        r19 = r3;
        r3 = r13.height;
        r2 = r2 / r3;
        r2 = r2 * r16;
        r2 = java.lang.Math.round(r2);
        r3 = 3;
        if (r5 < r3) goto L_0x00e3;
    L_0x00d7:
        r3 = r15 + -1;
        if (r14 == r3) goto L_0x00dc;
    L_0x00db:
        goto L_0x00e3;
    L_0x00dc:
        r3 = r0.itemsToRow;
        r3.put(r14, r7);
        r1 = r4;
        goto L_0x00ee;
    L_0x00e3:
        r3 = (float) r2;
        r3 = r3 / r22;
        r1 = r21.getSpanCount();
        r1 = (float) r1;
        r3 = r3 * r1;
        r1 = (int) r3;
        r4 = r4 - r1;
        r3 = r0.itemSpans;
        r3.put(r14, r1);
        r14 = r14 + 1;
        r2 = r18;
        r3 = r19;
        r13 = 3;
        goto L_0x00bd;
    L_0x00fc:
        r18 = r2;
        r19 = r3;
        r1 = r11.size();
        r9 = r9 + r1;
        r7 = r7 + 1;
        r2 = r17;
        r4 = 1;
        goto L_0x004d;
    L_0x010c:
        r17 = r2;
        r19 = r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.ui.Components.ExtendedGridLayoutManager.prepareLayout(float):void");
    }

    public ExtendedGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public boolean supportsPredictiveItemAnimations() {
        return false;
    }

    private int[] getLinearPartitionTable(int[] sequence, int numPartitions) {
        int j;
        int n = sequence.length;
        int[] tmpTable = new int[(n * numPartitions)];
        int[] solution = new int[((n - 1) * (numPartitions - 1))];
        int i = 0;
        while (i < n) {
            tmpTable[i * numPartitions] = sequence[i] + (i != 0 ? tmpTable[(i - 1) * numPartitions] : 0);
            i++;
        }
        for (j = 0; j < numPartitions; j++) {
            tmpTable[j] = sequence[0];
        }
        for (i = 1; i < n; i++) {
            for (j = 1; j < numPartitions; j++) {
                int minX = ConnectionsManager.DEFAULT_DATACENTER_ID;
                int currentMin = 0;
                for (int x = 0; x < i; x++) {
                    int cost = Math.max(tmpTable[(x * numPartitions) + (j - 1)], tmpTable[i * numPartitions] - tmpTable[x * numPartitions]);
                    if (x == 0 || cost < currentMin) {
                        currentMin = cost;
                        minX = x;
                    }
                }
                tmpTable[(i * numPartitions) + j] = currentMin;
                solution[((i - 1) * (numPartitions - 1)) + (j - 1)] = minX;
            }
        }
        return solution;
    }

    private ArrayList<ArrayList<Integer>> getLinearPartitionForSequence(int[] sequence, int numberOfPartitions) {
        int n = sequence.length;
        int k = numberOfPartitions;
        if (k <= 0) {
            return new ArrayList();
        }
        int i = 0;
        if (k < n) {
            if (n != 1) {
                int i2;
                int[] solution = getLinearPartitionTable(sequence, numberOfPartitions);
                int solutionRowSize = numberOfPartitions - 1;
                n--;
                ArrayList<ArrayList<Integer>> answer = new ArrayList();
                for (k -= 2; k >= 0; k--) {
                    if (n < 1) {
                        answer.add(0, new ArrayList());
                    } else {
                        ArrayList<Integer> currentAnswer = new ArrayList();
                        int range = n + 1;
                        for (i2 = solution[((n - 1) * solutionRowSize) + k] + 1; i2 < range; i2++) {
                            currentAnswer.add(Integer.valueOf(sequence[i2]));
                        }
                        answer.add(0, currentAnswer);
                        n = solution[((n - 1) * solutionRowSize) + k];
                    }
                }
                ArrayList<Integer> currentAnswer2 = new ArrayList();
                i2 = n + 1;
                for (int i3 = 0; i3 < i2; i3++) {
                    currentAnswer2.add(Integer.valueOf(sequence[i3]));
                }
                answer.add(0, currentAnswer2);
                return answer;
            }
        }
        ArrayList<ArrayList<Integer>> partition = new ArrayList(sequence.length);
        while (i < sequence.length) {
            ArrayList<Integer> arrayList = new ArrayList(1);
            arrayList.add(Integer.valueOf(sequence[i]));
            partition.add(arrayList);
            i++;
        }
        return partition;
    }

    private Size sizeForItem(int i) {
        Size size = getSizeForItem(i);
        if (size.width == 0.0f) {
            size.width = 100.0f;
        }
        if (size.height == 0.0f) {
            size.height = 100.0f;
        }
        float aspect = size.width / size.height;
        if (aspect > 4.0f || aspect < 0.2f) {
            float max = Math.max(size.width, size.height);
            size.width = max;
            size.height = max;
        }
        return size;
    }

    protected Size getSizeForItem(int i) {
        return new Size(100.0f, 100.0f);
    }

    private void checkLayout() {
        if (this.itemSpans.size() != getFlowItemCount() || this.calculatedWidth != getWidth()) {
            this.calculatedWidth = getWidth();
            prepareLayout((float) getWidth());
        }
    }

    public int getSpanSizeForItem(int i) {
        checkLayout();
        return this.itemSpans.get(i);
    }

    public int getRowsCount(int width) {
        if (this.rows == null) {
            prepareLayout((float) width);
        }
        return this.rows != null ? this.rows.size() : 0;
    }

    public boolean isLastInRow(int i) {
        checkLayout();
        return this.itemsToRow.get(i, ConnectionsManager.DEFAULT_DATACENTER_ID) != ConnectionsManager.DEFAULT_DATACENTER_ID;
    }

    public boolean isFirstRow(int i) {
        checkLayout();
        return (this.rows == null || this.rows.isEmpty() || i >= ((ArrayList) this.rows.get(0)).size()) ? false : true;
    }

    protected int getFlowItemCount() {
        return getItemCount();
    }
}
