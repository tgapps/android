package com.google.android.gms.vision;

import android.util.SparseArray;
import javax.annotation.concurrent.GuardedBy;

public final class zzc {
    private static final Object sLock = new Object();
    @GuardedBy("sLock")
    private static int zzas = 0;
    @GuardedBy("sLock")
    private SparseArray<Integer> zzat = new SparseArray();
    @GuardedBy("sLock")
    private SparseArray<Integer> zzau = new SparseArray();

    public final int zzb(int i) {
        int intValue;
        synchronized (sLock) {
            Integer num = (Integer) this.zzat.get(i);
            if (num != null) {
                intValue = num.intValue();
            } else {
                intValue = zzas;
                zzas++;
                this.zzat.append(i, Integer.valueOf(intValue));
                this.zzau.append(intValue, Integer.valueOf(i));
            }
        }
        return intValue;
    }
}
