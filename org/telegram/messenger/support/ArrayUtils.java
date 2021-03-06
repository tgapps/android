package org.telegram.messenger.support;

import java.lang.reflect.Array;
import org.telegram.tgnet.ConnectionsManager;

public class ArrayUtils {
    private static final int CACHE_SIZE = 73;
    private static Object[] EMPTY = new Object[0];
    private static Object[] sCache = new Object[73];

    private ArrayUtils() {
    }

    public static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++) {
            if (need <= (1 << i) - 12) {
                return (1 << i) - 12;
            }
        }
        return need;
    }

    public static int idealBooleanArraySize(int need) {
        return idealByteArraySize(need);
    }

    public static int idealShortArraySize(int need) {
        return idealByteArraySize(need * 2) / 2;
    }

    public static int idealCharArraySize(int need) {
        return idealByteArraySize(need * 2) / 2;
    }

    public static int idealIntArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealFloatArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealObjectArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealLongArraySize(int need) {
        return idealByteArraySize(need * 8) / 8;
    }

    public static boolean equals(byte[] array1, byte[] array2, int length) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length < length || array2.length < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static <T> T[] emptyArray(Class<T> kind) {
        if (kind == Object.class) {
            return EMPTY;
        }
        int bucket = ((System.identityHashCode(kind) / 8) & ConnectionsManager.DEFAULT_DATACENTER_ID) % 73;
        Object cache = sCache[bucket];
        if (cache == null || cache.getClass().getComponentType() != kind) {
            cache = Array.newInstance(kind, 0);
            sCache[bucket] = cache;
        }
        return (Object[]) cache;
    }

    public static <T> boolean contains(T[] array, T value) {
        for (T element : array) {
            if (element == null) {
                if (value == null) {
                    return true;
                }
            } else if (value != null && element.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(int[] array, int value) {
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public static long total(long[] array) {
        long total = 0;
        for (long value : array) {
            total += value;
        }
        return total;
    }

    public static <T> T[] appendElement(Class<T> kind, T[] array, T element) {
        int end;
        T[] result;
        if (array != null) {
            end = array.length;
            result = (Object[]) ((Object[]) Array.newInstance(kind, end + 1));
            System.arraycopy(array, 0, result, 0, end);
        } else {
            end = 0;
            Object[] result2 = (Object[]) Array.newInstance(kind, 1);
        }
        result[end] = element;
        return result;
    }

    public static <T> T[] removeElement(Class<T> kind, T[] array, T element) {
        if (array != null) {
            int length = array.length;
            int i = 0;
            while (i < length) {
                if (array[i] != element) {
                    i++;
                } else if (length == 1) {
                    return null;
                } else {
                    Object[] result = (Object[]) ((Object[]) Array.newInstance(kind, length - 1));
                    System.arraycopy(array, 0, result, 0, i);
                    System.arraycopy(array, i + 1, result, i, (length - i) - 1);
                    return result;
                }
            }
        }
        return array;
    }

    public static int[] appendInt(int[] cur, int val) {
        if (cur == null) {
            return new int[]{val};
        }
        for (int i : cur) {
            if (i == val) {
                return cur;
            }
        }
        int[] ret = new int[(N + 1)];
        System.arraycopy(cur, 0, ret, 0, N);
        ret[N] = val;
        return ret;
    }

    public static int[] removeInt(int[] cur, int val) {
        if (cur == null) {
            return null;
        }
        int N = cur.length;
        for (int i = 0; i < N; i++) {
            if (cur[i] == val) {
                int[] ret = new int[(N - 1)];
                if (i > 0) {
                    System.arraycopy(cur, 0, ret, 0, i);
                }
                if (i >= N - 1) {
                    return ret;
                }
                System.arraycopy(cur, i + 1, ret, i, (N - i) - 1);
                return ret;
            }
        }
        return cur;
    }
}
