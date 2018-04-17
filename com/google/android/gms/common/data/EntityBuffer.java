package com.google.android.gms.common.data;

import java.util.ArrayList;

public abstract class EntityBuffer<T> extends AbstractDataBuffer<T> {
    private boolean zzoa = false;
    private ArrayList<Integer> zzob;

    protected EntityBuffer(DataHolder dataHolder) {
        super(dataHolder);
    }

    private final void zzck() {
        synchronized (this) {
            if (!this.zzoa) {
                int count = this.mDataHolder.getCount();
                this.zzob = new ArrayList();
                if (count > 0) {
                    this.zzob.add(Integer.valueOf(0));
                    String primaryDataMarkerColumn = getPrimaryDataMarkerColumn();
                    Object string = this.mDataHolder.getString(primaryDataMarkerColumn, 0, this.mDataHolder.getWindowIndex(0));
                    for (int i = 1; i < count; i++) {
                        int windowIndex = this.mDataHolder.getWindowIndex(i);
                        String string2 = this.mDataHolder.getString(primaryDataMarkerColumn, i, windowIndex);
                        if (string2 == null) {
                            StringBuilder stringBuilder = new StringBuilder(78 + String.valueOf(primaryDataMarkerColumn).length());
                            stringBuilder.append("Missing value for markerColumn: ");
                            stringBuilder.append(primaryDataMarkerColumn);
                            stringBuilder.append(", at row: ");
                            stringBuilder.append(i);
                            stringBuilder.append(", for window: ");
                            stringBuilder.append(windowIndex);
                            throw new NullPointerException(stringBuilder.toString());
                        }
                        if (!string2.equals(string)) {
                            this.zzob.add(Integer.valueOf(i));
                            string = string2;
                        }
                    }
                }
                this.zzoa = true;
            }
        }
    }

    public final T get(int i) {
        zzck();
        return getEntry(zzi(i), getChildCount(i));
    }

    protected int getChildCount(int i) {
        if (i < 0 || i == this.zzob.size()) {
            return 0;
        }
        int count = (i == this.zzob.size() - 1 ? this.mDataHolder.getCount() : ((Integer) this.zzob.get(i + 1)).intValue()) - ((Integer) this.zzob.get(i)).intValue();
        if (count == 1) {
            i = zzi(i);
            int windowIndex = this.mDataHolder.getWindowIndex(i);
            String childDataMarkerColumn = getChildDataMarkerColumn();
            return (childDataMarkerColumn == null || this.mDataHolder.getString(childDataMarkerColumn, i, windowIndex) != null) ? count : 0;
        }
    }

    protected String getChildDataMarkerColumn() {
        return null;
    }

    public int getCount() {
        zzck();
        return this.zzob.size();
    }

    protected abstract T getEntry(int i, int i2);

    protected abstract String getPrimaryDataMarkerColumn();

    final int zzi(int i) {
        if (i >= 0) {
            if (i < this.zzob.size()) {
                return ((Integer) this.zzob.get(i)).intValue();
            }
        }
        StringBuilder stringBuilder = new StringBuilder(53);
        stringBuilder.append("Position ");
        stringBuilder.append(i);
        stringBuilder.append(" is out of bounds for this buffer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
