package com.google.android.gms.vision;

import android.util.SparseArray;
import javax.annotation.concurrent.GuardedBy;

public final class zzc {
    private static final Object lock = new Object();
    @GuardedBy("lock")
    private static int zzau = 0;
    @GuardedBy("lock")
    private SparseArray<Integer> zzav = new SparseArray();
    @GuardedBy("lock")
    private SparseArray<Integer> zzaw = new SparseArray();

    public final int zzb(int i) {
        int intValue;
        synchronized (lock) {
            Integer num = (Integer) this.zzav.get(i);
            if (num != null) {
                intValue = num.intValue();
            } else {
                intValue = zzau;
                zzau++;
                this.zzav.append(i, Integer.valueOf(intValue));
                this.zzaw.append(intValue, Integer.valueOf(i));
            }
        }
        return intValue;
    }
}
