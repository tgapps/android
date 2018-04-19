package com.google.android.gms.common.data;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;

public class DataBufferRef {
    protected final DataHolder mDataHolder;
    protected int mDataRow;
    private int zznj;

    public DataBufferRef(DataHolder dataHolder, int i) {
        this.mDataHolder = (DataHolder) Preconditions.checkNotNull(dataHolder);
        setDataRow(i);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof DataBufferRef)) {
            return false;
        }
        DataBufferRef dataBufferRef = (DataBufferRef) obj;
        return Objects.equal(Integer.valueOf(dataBufferRef.mDataRow), Integer.valueOf(this.mDataRow)) && Objects.equal(Integer.valueOf(dataBufferRef.zznj), Integer.valueOf(this.zznj)) && dataBufferRef.mDataHolder == this.mDataHolder;
    }

    protected byte[] getByteArray(String str) {
        return this.mDataHolder.getByteArray(str, this.mDataRow, this.zznj);
    }

    protected int getInteger(String str) {
        return this.mDataHolder.getInteger(str, this.mDataRow, this.zznj);
    }

    protected String getString(String str) {
        return this.mDataHolder.getString(str, this.mDataRow, this.zznj);
    }

    public int hashCode() {
        return Objects.hashCode(Integer.valueOf(this.mDataRow), Integer.valueOf(this.zznj), this.mDataHolder);
    }

    protected void setDataRow(int i) {
        boolean z = i >= 0 && i < this.mDataHolder.getCount();
        Preconditions.checkState(z);
        this.mDataRow = i;
        this.zznj = this.mDataHolder.getWindowIndex(this.mDataRow);
    }
}
