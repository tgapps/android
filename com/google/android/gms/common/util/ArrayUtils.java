package com.google.android.gms.common.util;

import com.google.android.gms.common.internal.Objects;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

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

    public static <T> T[] removeAll(T[] tArr, T... tArr2) {
        if (tArr == null) {
            return null;
        }
        if (tArr2 == null || tArr2.length == 0) {
            return Arrays.copyOf(tArr, tArr.length);
        }
        int i;
        Object[] objArr = (Object[]) Array.newInstance(tArr2.getClass().getComponentType(), tArr.length);
        int length;
        int i2;
        int i3;
        if (tArr2.length == 1) {
            length = tArr.length;
            i2 = 0;
            i = 0;
            while (i2 < length) {
                Object obj = tArr[i2];
                if (Objects.equal(tArr2[0], obj)) {
                    i3 = i;
                } else {
                    i3 = i + 1;
                    objArr[i] = obj;
                }
                i2++;
                i = i3;
            }
        } else {
            length = tArr.length;
            i2 = 0;
            i = 0;
            while (i2 < length) {
                Object obj2 = tArr[i2];
                if (contains(tArr2, obj2)) {
                    i3 = i;
                } else {
                    i3 = i + 1;
                    objArr[i] = obj2;
                }
                i2++;
                i = i3;
            }
        }
        return resize(objArr, i);
    }

    public static <T> T[] resize(T[] tArr, int i) {
        return tArr == null ? null : i != tArr.length ? Arrays.copyOf(tArr, i) : tArr;
    }
}
