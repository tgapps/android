package com.google.android.gms.common.util;

import com.google.android.gms.common.internal.Objects;
import java.util.ArrayList;

public final class ArrayUtils {
    public static <T> boolean contains(T[] tArr, T t) {
        return indexOf(tArr, t) >= 0;
    }

    public static <T> int indexOf(T[] tArr, T t) {
        int i = 0;
        int length = tArr != null ? tArr.length : 0;
        while (i < length) {
            if (Objects.equal(tArr[i], t)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static <T> ArrayList<T> newArrayList() {
        return new ArrayList();
    }
}
