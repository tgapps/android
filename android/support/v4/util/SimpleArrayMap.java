package android.support.v4.util;

import java.util.ConcurrentModificationException;

public class SimpleArrayMap<K, V> {
    static Object[] mBaseCache;
    static int mBaseCacheSize;
    static Object[] mTwiceBaseCache;
    static int mTwiceBaseCacheSize;
    Object[] mArray;
    int[] mHashes;
    int mSize;

    public boolean equals(java.lang.Object r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v4.util.SimpleArrayMap.equals(java.lang.Object):boolean
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = 1;
        if (r8 != r9) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = r9 instanceof android.support.v4.util.SimpleArrayMap;
        r2 = 0;
        if (r1 == 0) goto L_0x0043;
    L_0x0009:
        r1 = r9;
        r1 = (android.support.v4.util.SimpleArrayMap) r1;
        r3 = r8.size();
        r4 = r1.size();
        if (r3 == r4) goto L_0x0017;
    L_0x0016:
        return r2;
    L_0x0017:
        r3 = r2;
        r4 = r8.mSize;	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        if (r3 >= r4) goto L_0x003d;	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
    L_0x001c:
        r4 = r8.keyAt(r3);	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        r5 = r8.valueAt(r3);	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        r6 = r1.get(r4);	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        if (r5 != 0) goto L_0x0033;	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        if (r6 != 0) goto L_0x0032;	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        r7 = r1.containsKey(r4);	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        if (r7 != 0) goto L_0x003a;	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        return r2;	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        r7 = r5.equals(r6);	 Catch:{ NullPointerException -> 0x0041, ClassCastException -> 0x003f }
        if (r7 != 0) goto L_0x003a;
        return r2;
        r3 = r3 + 1;
        goto L_0x0018;
        return r0;
    L_0x003f:
        r0 = move-exception;
        return r2;
    L_0x0041:
        r0 = move-exception;
        return r2;
    L_0x0043:
        r1 = r9 instanceof java.util.Map;
        if (r1 == 0) goto L_0x0081;
        r1 = r9;
        r1 = (java.util.Map) r1;
        r3 = r8.size();
        r4 = r1.size();
        if (r3 == r4) goto L_0x0055;
        return r2;
        r3 = r2;
        r4 = r8.mSize;	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        if (r3 >= r4) goto L_0x007b;	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        r4 = r8.keyAt(r3);	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        r5 = r8.valueAt(r3);	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        r6 = r1.get(r4);	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        if (r5 != 0) goto L_0x0071;	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        if (r6 != 0) goto L_0x0070;	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        r7 = r1.containsKey(r4);	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        if (r7 != 0) goto L_0x0078;	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        return r2;	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        r7 = r5.equals(r6);	 Catch:{ NullPointerException -> 0x007f, ClassCastException -> 0x007d }
        if (r7 != 0) goto L_0x0078;
        return r2;
        r3 = r3 + 1;
        goto L_0x0056;
        return r0;
    L_0x007d:
        r0 = move-exception;
        return r2;
    L_0x007f:
        r0 = move-exception;
        return r2;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.SimpleArrayMap.equals(java.lang.Object):boolean");
    }

    int indexOfValue(java.lang.Object r1) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.support.v4.util.SimpleArrayMap.indexOfValue(java.lang.Object):int
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:116)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:249)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
Caused by: java.lang.NullPointerException
*/
        /*
        r0 = this;
        r0 = r4.mSize;
        r0 = r0 * 2;
        r1 = r4.mArray;
        r2 = 1;
        if (r5 != 0) goto L_0x0016;
    L_0x000a:
        if (r2 >= r0) goto L_0x0027;
        r3 = r1[r2];
        if (r3 != 0) goto L_0x0013;
        r3 = r2 >> 1;
        return r3;
        r2 = r2 + 2;
        goto L_0x000a;
        if (r2 >= r0) goto L_0x0027;
        r3 = r1[r2];
        r3 = r5.equals(r3);
        if (r3 == 0) goto L_0x0024;
        r3 = r2 >> 1;
        return r3;
        r2 = r2 + 2;
        goto L_0x0017;
        r2 = -1;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v4.util.SimpleArrayMap.indexOfValue(java.lang.Object):int");
    }

    private static int binarySearchHashes(int[] hashes, int N, int hash) {
        try {
            return ContainerHelpers.binarySearch(hashes, N, hash);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ConcurrentModificationException();
        }
    }

    int indexOf(Object key, int hash) {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = binarySearchHashes(this.mHashes, N, hash);
        if (index < 0 || key.equals(this.mArray[index << 1])) {
            return index;
        }
        int end = index + 1;
        while (end < N && this.mHashes[end] == hash) {
            if (key.equals(this.mArray[end << 1])) {
                return end;
            }
            end++;
        }
        int i = index - 1;
        while (i >= 0 && this.mHashes[i] == hash) {
            if (key.equals(this.mArray[i << 1])) {
                return i;
            }
            i--;
        }
        return end ^ -1;
    }

    int indexOfNull() {
        int N = this.mSize;
        if (N == 0) {
            return -1;
        }
        int index = binarySearchHashes(this.mHashes, N, 0);
        if (index < 0 || this.mArray[index << 1] == null) {
            return index;
        }
        int end = index + 1;
        while (end < N && this.mHashes[end] == 0) {
            if (this.mArray[end << 1] == null) {
                return end;
            }
            end++;
        }
        int i = index - 1;
        while (i >= 0 && this.mHashes[i] == 0) {
            if (this.mArray[i << 1] == null) {
                return i;
            }
            i--;
        }
        return end ^ -1;
    }

    private void allocArrays(int size) {
        Object[] array;
        if (size == 8) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCache != null) {
                    array = mTwiceBaseCache;
                    this.mArray = array;
                    mTwiceBaseCache = (Object[]) array[0];
                    this.mHashes = (int[]) array[1];
                    array[1] = null;
                    array[0] = null;
                    mTwiceBaseCacheSize--;
                    return;
                }
            }
        } else if (size == 4) {
            synchronized (ArrayMap.class) {
                if (mBaseCache != null) {
                    array = mBaseCache;
                    this.mArray = array;
                    mBaseCache = (Object[]) array[0];
                    this.mHashes = (int[]) array[1];
                    array[1] = null;
                    array[0] = null;
                    mBaseCacheSize--;
                    return;
                }
            }
        }
        this.mHashes = new int[size];
        this.mArray = new Object[(size << 1)];
    }

    private static void freeArrays(int[] hashes, Object[] array, int size) {
        int i;
        if (hashes.length == 8) {
            synchronized (ArrayMap.class) {
                if (mTwiceBaseCacheSize < 10) {
                    array[0] = mTwiceBaseCache;
                    array[1] = hashes;
                    for (i = (size << 1) - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    mTwiceBaseCache = array;
                    mTwiceBaseCacheSize++;
                }
            }
        } else if (hashes.length == 4) {
            synchronized (ArrayMap.class) {
                if (mBaseCacheSize < 10) {
                    array[0] = mBaseCache;
                    array[1] = hashes;
                    for (i = (size << 1) - 1; i >= 2; i--) {
                        array[i] = null;
                    }
                    mBaseCache = array;
                    mBaseCacheSize++;
                }
            }
        }
    }

    public SimpleArrayMap() {
        this.mHashes = ContainerHelpers.EMPTY_INTS;
        this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        this.mSize = 0;
    }

    public SimpleArrayMap(int capacity) {
        if (capacity == 0) {
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            allocArrays(capacity);
        }
        this.mSize = 0;
    }

    public void clear() {
        if (this.mSize > 0) {
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            int osize = this.mSize;
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            this.mSize = 0;
            freeArrays(ohashes, oarray, osize);
        }
        if (this.mSize > 0) {
            throw new ConcurrentModificationException();
        }
    }

    public void ensureCapacity(int minimumCapacity) {
        int osize = this.mSize;
        if (this.mHashes.length < minimumCapacity) {
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(minimumCapacity);
            if (this.mSize > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, osize);
                System.arraycopy(oarray, 0, this.mArray, 0, osize << 1);
            }
            freeArrays(ohashes, oarray, osize);
        }
        if (this.mSize != osize) {
            throw new ConcurrentModificationException();
        }
    }

    public boolean containsKey(Object key) {
        return indexOfKey(key) >= 0;
    }

    public int indexOfKey(Object key) {
        return key == null ? indexOfNull() : indexOf(key, key.hashCode());
    }

    public boolean containsValue(Object value) {
        return indexOfValue(value) >= 0;
    }

    public V get(Object key) {
        int index = indexOfKey(key);
        return index >= 0 ? this.mArray[(index << 1) + 1] : null;
    }

    public K keyAt(int index) {
        return this.mArray[index << 1];
    }

    public V valueAt(int index) {
        return this.mArray[(index << 1) + 1];
    }

    public V setValueAt(int index, V value) {
        int index2 = (index << 1) + 1;
        V old = this.mArray[index2];
        this.mArray[index2] = value;
        return old;
    }

    public boolean isEmpty() {
        return this.mSize <= 0;
    }

    public V put(K key, V value) {
        int hash;
        int index;
        int osize = this.mSize;
        if (key == null) {
            hash = 0;
            index = indexOfNull();
        } else {
            hash = key.hashCode();
            index = indexOf(key, hash);
        }
        if (index >= 0) {
            int index2 = (index << 1) + 1;
            V old = this.mArray[index2];
            this.mArray[index2] = value;
            return old;
        }
        index ^= -1;
        if (osize >= this.mHashes.length) {
            index2 = 4;
            if (osize >= 8) {
                index2 = (osize >> 1) + osize;
            } else if (osize >= 4) {
                index2 = 8;
            }
            int[] ohashes = this.mHashes;
            Object[] oarray = this.mArray;
            allocArrays(index2);
            if (osize != this.mSize) {
                throw new ConcurrentModificationException();
            }
            if (this.mHashes.length > 0) {
                System.arraycopy(ohashes, 0, this.mHashes, 0, ohashes.length);
                System.arraycopy(oarray, 0, this.mArray, 0, oarray.length);
            }
            freeArrays(ohashes, oarray, osize);
        }
        if (index < osize) {
            System.arraycopy(this.mHashes, index, this.mHashes, index + 1, osize - index);
            System.arraycopy(this.mArray, index << 1, this.mArray, (index + 1) << 1, (this.mSize - index) << 1);
        }
        if (osize == this.mSize) {
            if (index < this.mHashes.length) {
                this.mHashes[index] = hash;
                this.mArray[index << 1] = key;
                this.mArray[(index << 1) + 1] = value;
                this.mSize++;
                return null;
            }
        }
        throw new ConcurrentModificationException();
    }

    public V remove(Object key) {
        int index = indexOfKey(key);
        if (index >= 0) {
            return removeAt(index);
        }
        return null;
    }

    public V removeAt(int index) {
        int nsize;
        Object old = this.mArray[(index << 1) + 1];
        int osize = this.mSize;
        if (osize <= 1) {
            freeArrays(this.mHashes, this.mArray, osize);
            this.mHashes = ContainerHelpers.EMPTY_INTS;
            this.mArray = ContainerHelpers.EMPTY_OBJECTS;
            nsize = 0;
        } else {
            int nsize2 = osize - 1;
            int i = 8;
            if (this.mHashes.length <= 8 || this.mSize >= this.mHashes.length / 3) {
                if (index < nsize2) {
                    System.arraycopy(this.mHashes, index + 1, this.mHashes, index, nsize2 - index);
                    System.arraycopy(this.mArray, (index + 1) << 1, this.mArray, index << 1, (nsize2 - index) << 1);
                }
                this.mArray[nsize2 << 1] = null;
                this.mArray[(nsize2 << 1) + 1] = null;
            } else {
                if (osize > 8) {
                    i = osize + (osize >> 1);
                }
                int n = i;
                int[] ohashes = this.mHashes;
                Object[] oarray = this.mArray;
                allocArrays(n);
                if (osize != this.mSize) {
                    throw new ConcurrentModificationException();
                }
                if (index > 0) {
                    System.arraycopy(ohashes, 0, this.mHashes, 0, index);
                    System.arraycopy(oarray, 0, this.mArray, 0, index << 1);
                }
                if (index < nsize2) {
                    System.arraycopy(ohashes, index + 1, this.mHashes, index, nsize2 - index);
                    System.arraycopy(oarray, (index + 1) << 1, this.mArray, index << 1, (nsize2 - index) << 1);
                }
            }
            nsize = nsize2;
        }
        if (osize != this.mSize) {
            throw new ConcurrentModificationException();
        }
        this.mSize = nsize;
        return old;
    }

    public int size() {
        return this.mSize;
    }

    public int hashCode() {
        int[] hashes = this.mHashes;
        Object[] array = this.mArray;
        int result = 0;
        int i = 0;
        int v = 1;
        int s = this.mSize;
        while (i < s) {
            Object value = array[v];
            result += hashes[i] ^ (value == null ? 0 : value.hashCode());
            i++;
            v += 2;
        }
        return result;
    }

    public String toString() {
        if (isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 28);
        buffer.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            SimpleArrayMap key = keyAt(i);
            if (key != this) {
                buffer.append(key);
            } else {
                buffer.append("(this Map)");
            }
            buffer.append('=');
            SimpleArrayMap value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }
}
