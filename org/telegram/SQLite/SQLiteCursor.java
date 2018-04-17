package org.telegram.SQLite;

import org.telegram.tgnet.NativeByteBuffer;

public class SQLiteCursor {
    public static final int FIELD_TYPE_BYTEARRAY = 4;
    public static final int FIELD_TYPE_FLOAT = 2;
    public static final int FIELD_TYPE_INT = 1;
    public static final int FIELD_TYPE_NULL = 5;
    public static final int FIELD_TYPE_STRING = 3;
    private boolean inRow = false;
    private SQLitePreparedStatement preparedStatement;

    native byte[] columnByteArrayValue(long j, int i);

    native long columnByteBufferValue(long j, int i);

    native double columnDoubleValue(long j, int i);

    native int columnIntValue(long j, int i);

    native int columnIsNull(long j, int i);

    native long columnLongValue(long j, int i);

    native String columnStringValue(long j, int i);

    native int columnType(long j, int i);

    public boolean next() throws org.telegram.SQLite.SQLiteException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: org.telegram.SQLite.SQLiteCursor.next():boolean
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
        r0 = r6.preparedStatement;
        r1 = r6.preparedStatement;
        r1 = r1.getStatementHandle();
        r0 = r0.step(r1);
        r1 = -1;
        if (r0 != r1) goto L_0x003d;
    L_0x000f:
        r2 = 6;
        r3 = r2 + -1;
        if (r2 == 0) goto L_0x0033;
    L_0x0014:
        r2 = org.telegram.messenger.BuildVars.LOGS_ENABLED;	 Catch:{ Exception -> 0x002c }
        if (r2 == 0) goto L_0x001d;	 Catch:{ Exception -> 0x002c }
    L_0x0018:
        r2 = "sqlite busy, waiting...";	 Catch:{ Exception -> 0x002c }
        org.telegram.messenger.FileLog.d(r2);	 Catch:{ Exception -> 0x002c }
    L_0x001d:
        r4 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;	 Catch:{ Exception -> 0x002c }
        java.lang.Thread.sleep(r4);	 Catch:{ Exception -> 0x002c }
        r2 = r6.preparedStatement;	 Catch:{ Exception -> 0x002c }
        r2 = r2.step();	 Catch:{ Exception -> 0x002c }
        r0 = r2;
        if (r0 != 0) goto L_0x0030;
    L_0x002b:
        goto L_0x0033;
    L_0x002c:
        r2 = move-exception;
        org.telegram.messenger.FileLog.e(r2);
        r2 = r3;
        goto L_0x0010;
    L_0x0033:
        if (r0 != r1) goto L_0x003d;
        r1 = new org.telegram.SQLite.SQLiteException;
        r2 = "sqlite busy";
        r1.<init>(r2);
        throw r1;
    L_0x003d:
        if (r0 != 0) goto L_0x0041;
        r1 = 1;
        goto L_0x0042;
        r1 = 0;
        r6.inRow = r1;
        r1 = r6.inRow;
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.telegram.SQLite.SQLiteCursor.next():boolean");
    }

    public SQLiteCursor(SQLitePreparedStatement stmt) {
        this.preparedStatement = stmt;
    }

    public boolean isNull(int columnIndex) throws SQLiteException {
        checkRow();
        return columnIsNull(this.preparedStatement.getStatementHandle(), columnIndex) == 1;
    }

    public int intValue(int columnIndex) throws SQLiteException {
        checkRow();
        return columnIntValue(this.preparedStatement.getStatementHandle(), columnIndex);
    }

    public double doubleValue(int columnIndex) throws SQLiteException {
        checkRow();
        return columnDoubleValue(this.preparedStatement.getStatementHandle(), columnIndex);
    }

    public long longValue(int columnIndex) throws SQLiteException {
        checkRow();
        return columnLongValue(this.preparedStatement.getStatementHandle(), columnIndex);
    }

    public String stringValue(int columnIndex) throws SQLiteException {
        checkRow();
        return columnStringValue(this.preparedStatement.getStatementHandle(), columnIndex);
    }

    public byte[] byteArrayValue(int columnIndex) throws SQLiteException {
        checkRow();
        return columnByteArrayValue(this.preparedStatement.getStatementHandle(), columnIndex);
    }

    public NativeByteBuffer byteBufferValue(int columnIndex) throws SQLiteException {
        checkRow();
        long ptr = columnByteBufferValue(this.preparedStatement.getStatementHandle(), columnIndex);
        if (ptr != 0) {
            return NativeByteBuffer.wrap(ptr);
        }
        return null;
    }

    public int getTypeOf(int columnIndex) throws SQLiteException {
        checkRow();
        return columnType(this.preparedStatement.getStatementHandle(), columnIndex);
    }

    public long getStatementHandle() {
        return this.preparedStatement.getStatementHandle();
    }

    public void dispose() {
        this.preparedStatement.dispose();
    }

    void checkRow() throws SQLiteException {
        if (!this.inRow) {
            throw new SQLiteException("You must call next before");
        }
    }
}
