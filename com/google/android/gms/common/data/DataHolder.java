package com.google.android.gms.common.data;

import android.content.ContentValues;
import android.database.CursorIndexOutOfBoundsException;
import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@KeepName
public final class DataHolder extends AbstractSafeParcelable implements Closeable {
    public static final Creator<DataHolder> CREATOR = new DataHolderCreator();
    private static final Builder zznt = new zza(new String[0], null);
    private boolean mClosed;
    private final int zzal;
    private final int zzam;
    private final String[] zznm;
    private Bundle zznn;
    private final CursorWindow[] zzno;
    private final Bundle zznp;
    private int[] zznq;
    private int zznr;
    private boolean zzns;

    public static class Builder {
        private final String[] zznm;
        private final ArrayList<HashMap<String, Object>> zznu;
        private final String zznv;
        private final HashMap<Object, Integer> zznw;
        private boolean zznx;
        private String zzny;

        private Builder(String[] strArr, String str) {
            this.zznm = (String[]) Preconditions.checkNotNull(strArr);
            this.zznu = new ArrayList();
            this.zznv = str;
            this.zznw = new HashMap();
            this.zznx = false;
            this.zzny = null;
        }

        public DataHolder build(int i) {
            return new DataHolder(this, i);
        }

        public Builder withRow(ContentValues contentValues) {
            Asserts.checkNotNull(contentValues);
            HashMap hashMap = new HashMap(contentValues.size());
            for (Entry entry : contentValues.valueSet()) {
                hashMap.put((String) entry.getKey(), entry.getValue());
            }
            return withRow(hashMap);
        }

        public Builder withRow(HashMap<String, Object> hashMap) {
            int intValue;
            Asserts.checkNotNull(hashMap);
            if (this.zznv != null) {
                Object obj = hashMap.get(this.zznv);
                if (obj != null) {
                    Integer num = (Integer) this.zznw.get(obj);
                    if (num == null) {
                        this.zznw.put(obj, Integer.valueOf(this.zznu.size()));
                    } else {
                        intValue = num.intValue();
                        if (intValue != -1) {
                            this.zznu.add(hashMap);
                        } else {
                            this.zznu.remove(intValue);
                            this.zznu.add(intValue, hashMap);
                        }
                        this.zznx = false;
                        return this;
                    }
                }
            }
            intValue = -1;
            if (intValue != -1) {
                this.zznu.remove(intValue);
                this.zznu.add(intValue, hashMap);
            } else {
                this.zznu.add(hashMap);
            }
            this.zznx = false;
            return this;
        }
    }

    public static class DataHolderException extends RuntimeException {
        public DataHolderException(String str) {
            super(str);
        }
    }

    DataHolder(int i, String[] strArr, CursorWindow[] cursorWindowArr, int i2, Bundle bundle) {
        this.mClosed = false;
        this.zzns = true;
        this.zzal = i;
        this.zznm = strArr;
        this.zzno = cursorWindowArr;
        this.zzam = i2;
        this.zznp = bundle;
    }

    private DataHolder(Builder builder, int i, Bundle bundle) {
        this(builder.zznm, zza(builder, -1), i, bundle);
    }

    public DataHolder(String[] strArr, CursorWindow[] cursorWindowArr, int i, Bundle bundle) {
        this.mClosed = false;
        this.zzns = true;
        this.zzal = 1;
        this.zznm = (String[]) Preconditions.checkNotNull(strArr);
        this.zzno = (CursorWindow[]) Preconditions.checkNotNull(cursorWindowArr);
        this.zzam = i;
        this.zznp = bundle;
        validateContents();
    }

    public static Builder builder(String[] strArr) {
        return new Builder(strArr, null);
    }

    private final void zza(String str, int i) {
        if (this.zznn != null) {
            if (this.zznn.containsKey(str)) {
                if (isClosed()) {
                    throw new IllegalArgumentException("Buffer is closed.");
                }
                if (i >= 0) {
                    if (i < this.zznr) {
                        return;
                    }
                }
                throw new CursorIndexOutOfBoundsException(i, this.zznr);
            }
        }
        String str2 = "No such column: ";
        str = String.valueOf(str);
        throw new IllegalArgumentException(str.length() != 0 ? str2.concat(str) : new String(str2));
    }

    private static CursorWindow[] zza(Builder builder, int i) {
        int i2 = 0;
        if (builder.zznm.length == 0) {
            return new CursorWindow[0];
        }
        List subList;
        int size;
        CursorWindow cursorWindow;
        ArrayList arrayList;
        int i3;
        CursorWindow cursorWindow2;
        int i4;
        Map map;
        boolean z;
        int i5;
        String str;
        Object obj;
        long longValue;
        if (i >= 0) {
            if (i < builder.zznu.size()) {
                subList = builder.zznu.subList(0, i);
                size = subList.size();
                cursorWindow = new CursorWindow(false);
                arrayList = new ArrayList();
                arrayList.add(cursorWindow);
                cursorWindow.setNumColumns(builder.zznm.length);
                i3 = 0;
                cursorWindow2 = cursorWindow;
                i4 = i3;
                while (i4 < size) {
                    try {
                        if (!cursorWindow2.allocRow()) {
                            StringBuilder stringBuilder = new StringBuilder(72);
                            stringBuilder.append("Allocating additional cursor window for large data set (row ");
                            stringBuilder.append(i4);
                            stringBuilder.append(")");
                            Log.d("DataHolder", stringBuilder.toString());
                            cursorWindow2 = new CursorWindow(false);
                            cursorWindow2.setStartPosition(i4);
                            cursorWindow2.setNumColumns(builder.zznm.length);
                            arrayList.add(cursorWindow2);
                            if (!cursorWindow2.allocRow()) {
                                Log.e("DataHolder", "Unable to allocate row to hold data.");
                                arrayList.remove(cursorWindow2);
                                return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
                            }
                        }
                        map = (Map) subList.get(i4);
                        z = true;
                        for (i5 = 0; i5 < builder.zznm.length && z; i5++) {
                            str = builder.zznm[i5];
                            obj = map.get(str);
                            if (obj == null) {
                                z = cursorWindow2.putNull(i4, i5);
                            } else if (obj instanceof String) {
                                if (obj instanceof Long) {
                                    longValue = ((Long) obj).longValue();
                                } else if (obj instanceof Integer) {
                                    z = cursorWindow2.putLong((long) ((Integer) obj).intValue(), i4, i5);
                                } else if (obj instanceof Boolean) {
                                    longValue = ((Boolean) obj).booleanValue() ? 1 : 0;
                                } else if (obj instanceof byte[]) {
                                    z = cursorWindow2.putBlob((byte[]) obj, i4, i5);
                                } else if (obj instanceof Double) {
                                    z = cursorWindow2.putDouble(((Double) obj).doubleValue(), i4, i5);
                                } else if (obj instanceof Float) {
                                    String valueOf = String.valueOf(obj);
                                    StringBuilder stringBuilder2 = new StringBuilder((32 + String.valueOf(str).length()) + String.valueOf(valueOf).length());
                                    stringBuilder2.append("Unsupported object for column ");
                                    stringBuilder2.append(str);
                                    stringBuilder2.append(": ");
                                    stringBuilder2.append(valueOf);
                                    throw new IllegalArgumentException(stringBuilder2.toString());
                                } else {
                                    z = cursorWindow2.putDouble((double) ((Float) obj).floatValue(), i4, i5);
                                }
                                z = cursorWindow2.putLong(longValue, i4, i5);
                            } else {
                                z = cursorWindow2.putString((String) obj, i4, i5);
                            }
                        }
                        if (!z) {
                            i3 = 0;
                        } else if (i3 == 0) {
                            throw new DataHolderException("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
                        } else {
                            StringBuilder stringBuilder3 = new StringBuilder(74);
                            stringBuilder3.append("Couldn't populate window data for row ");
                            stringBuilder3.append(i4);
                            stringBuilder3.append(" - allocating new window.");
                            Log.d("DataHolder", stringBuilder3.toString());
                            cursorWindow2.freeLastRow();
                            cursorWindow2 = new CursorWindow(false);
                            cursorWindow2.setStartPosition(i4);
                            cursorWindow2.setNumColumns(builder.zznm.length);
                            arrayList.add(cursorWindow2);
                            i4--;
                            i3 = 1;
                        }
                        i4++;
                    } catch (RuntimeException e) {
                        i = arrayList.size();
                        while (i2 < i) {
                            ((CursorWindow) arrayList.get(i2)).close();
                            i2++;
                        }
                        throw e;
                    }
                }
                return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
            }
        }
        subList = builder.zznu;
        size = subList.size();
        cursorWindow = new CursorWindow(false);
        arrayList = new ArrayList();
        arrayList.add(cursorWindow);
        cursorWindow.setNumColumns(builder.zznm.length);
        i3 = 0;
        cursorWindow2 = cursorWindow;
        i4 = i3;
        while (i4 < size) {
            if (cursorWindow2.allocRow()) {
                StringBuilder stringBuilder4 = new StringBuilder(72);
                stringBuilder4.append("Allocating additional cursor window for large data set (row ");
                stringBuilder4.append(i4);
                stringBuilder4.append(")");
                Log.d("DataHolder", stringBuilder4.toString());
                cursorWindow2 = new CursorWindow(false);
                cursorWindow2.setStartPosition(i4);
                cursorWindow2.setNumColumns(builder.zznm.length);
                arrayList.add(cursorWindow2);
                if (cursorWindow2.allocRow()) {
                    Log.e("DataHolder", "Unable to allocate row to hold data.");
                    arrayList.remove(cursorWindow2);
                    return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
                }
            }
            map = (Map) subList.get(i4);
            z = true;
            for (i5 = 0; i5 < builder.zznm.length; i5++) {
                str = builder.zznm[i5];
                obj = map.get(str);
                if (obj == null) {
                    z = cursorWindow2.putNull(i4, i5);
                } else if (obj instanceof String) {
                    if (obj instanceof Long) {
                        longValue = ((Long) obj).longValue();
                    } else if (obj instanceof Integer) {
                        z = cursorWindow2.putLong((long) ((Integer) obj).intValue(), i4, i5);
                    } else if (obj instanceof Boolean) {
                        if (((Boolean) obj).booleanValue()) {
                        }
                    } else if (obj instanceof byte[]) {
                        z = cursorWindow2.putBlob((byte[]) obj, i4, i5);
                    } else if (obj instanceof Double) {
                        z = cursorWindow2.putDouble(((Double) obj).doubleValue(), i4, i5);
                    } else if (obj instanceof Float) {
                        String valueOf2 = String.valueOf(obj);
                        StringBuilder stringBuilder22 = new StringBuilder((32 + String.valueOf(str).length()) + String.valueOf(valueOf2).length());
                        stringBuilder22.append("Unsupported object for column ");
                        stringBuilder22.append(str);
                        stringBuilder22.append(": ");
                        stringBuilder22.append(valueOf2);
                        throw new IllegalArgumentException(stringBuilder22.toString());
                    } else {
                        z = cursorWindow2.putDouble((double) ((Float) obj).floatValue(), i4, i5);
                    }
                    z = cursorWindow2.putLong(longValue, i4, i5);
                } else {
                    z = cursorWindow2.putString((String) obj, i4, i5);
                }
            }
            if (!z) {
                i3 = 0;
            } else if (i3 == 0) {
                StringBuilder stringBuilder32 = new StringBuilder(74);
                stringBuilder32.append("Couldn't populate window data for row ");
                stringBuilder32.append(i4);
                stringBuilder32.append(" - allocating new window.");
                Log.d("DataHolder", stringBuilder32.toString());
                cursorWindow2.freeLastRow();
                cursorWindow2 = new CursorWindow(false);
                cursorWindow2.setStartPosition(i4);
                cursorWindow2.setNumColumns(builder.zznm.length);
                arrayList.add(cursorWindow2);
                i4--;
                i3 = 1;
            } else {
                throw new DataHolderException("Could not add the value to a new CursorWindow. The size of value may be larger than what a CursorWindow can handle.");
            }
            i4++;
        }
        return (CursorWindow[]) arrayList.toArray(new CursorWindow[arrayList.size()]);
    }

    public final void close() {
        synchronized (this) {
            if (!this.mClosed) {
                this.mClosed = true;
                for (CursorWindow close : this.zzno) {
                    close.close();
                }
            }
        }
    }

    protected final void finalize() throws Throwable {
        try {
            if (this.zzns && this.zzno.length > 0 && !isClosed()) {
                close();
                String obj = toString();
                StringBuilder stringBuilder = new StringBuilder(178 + String.valueOf(obj).length());
                stringBuilder.append("Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (internal object: ");
                stringBuilder.append(obj);
                stringBuilder.append(")");
                Log.e("DataBuffer", stringBuilder.toString());
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public final byte[] getByteArray(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].getBlob(i, this.zznn.getInt(str));
    }

    public final int getCount() {
        return this.zznr;
    }

    public final int getInteger(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].getInt(i, this.zznn.getInt(str));
    }

    public final Bundle getMetadata() {
        return this.zznp;
    }

    public final int getStatusCode() {
        return this.zzam;
    }

    public final String getString(String str, int i, int i2) {
        zza(str, i);
        return this.zzno[i2].getString(i, this.zznn.getInt(str));
    }

    public final int getWindowIndex(int i) {
        int i2 = 0;
        boolean z = i >= 0 && i < this.zznr;
        Preconditions.checkState(z);
        while (i2 < this.zznq.length) {
            if (i < this.zznq[i2]) {
                i2--;
                break;
            }
            i2++;
        }
        return i2 == this.zznq.length ? i2 - 1 : i2;
    }

    public final boolean isClosed() {
        boolean z;
        synchronized (this) {
            z = this.mClosed;
        }
        return z;
    }

    public final void validateContents() {
        int i;
        this.zznn = new Bundle();
        int i2 = 0;
        for (i = 0; i < this.zznm.length; i++) {
            this.zznn.putInt(this.zznm[i], i);
        }
        this.zznq = new int[this.zzno.length];
        i = 0;
        while (i2 < this.zzno.length) {
            this.zznq[i2] = i;
            i += this.zzno[i2].getNumRows() - (i - this.zzno[i2].getStartPosition());
            i2++;
        }
        this.zznr = i;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeStringArray(parcel, 1, this.zznm, false);
        SafeParcelWriter.writeTypedArray(parcel, 2, this.zzno, i, false);
        SafeParcelWriter.writeInt(parcel, 3, getStatusCode());
        SafeParcelWriter.writeBundle(parcel, 4, getMetadata(), false);
        SafeParcelWriter.writeInt(parcel, 1000, this.zzal);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
        if ((i & 1) != 0) {
            close();
        }
    }
}
